// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import net.cutsnake.enbookme.client.BookService;
import net.cutsnake.enbookme.shared.Book;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the Book service.
 */
@SuppressWarnings("serial")
public class BookServiceImpl extends RemoteServiceServlet implements
		BookService {

  private static final Logger log = Logger.getLogger(BookServiceImpl.class.getName());
  
  @Override
  public List<Book> list() throws IllegalArgumentException {
    return new BookQueryTask().execute();
  }

  @Override
  public List<Book> add(final Book book) throws IllegalArgumentException {
    return new BookQueryTask() {
      @Override
      void preQuery(User currentUser, PersistenceManager pm) {
        if (Strings.isNullOrEmpty(book.getOwner())) {
          book.setOwner(currentUser.getUserId());
        }
        if (book.getCreated() < 0) {
          book.setCreated(System.currentTimeMillis());
        }
        pm.makePersistent(book);
        Queue queue = QueueFactory.getQueue("check-queue");
        queue.add(withUrl("/tasks/check/" + book.getKey()));
      }
      @Override
      public List<Book> postQuery(List<Book> results) {
        if (!results.contains(book)) {
          results.add(0, book);
        }
        return results;
      };
    }.execute();
  }

  @Override
  public List<Book> remove(final List<Book> books) throws IllegalArgumentException {
    return new BookQueryTask() {
      @Override
      void preQuery(User currentUser, PersistenceManager pm) {
        for (Book book : books) {
          pm.deletePersistent(book);
        }
      }
      @Override
      public List<Book> postQuery(List<Book> results) {
        results.removeAll(books);
        return results;
      };
    }.execute();
  }

  private static class BookQueryTask {
    void preQuery(User currentUser, PersistenceManager pm) {}
    List<Book> execute() {
      UserService userService = UserServiceFactory.getUserService();
      if (!userService.isUserLoggedIn()) {
        return Lists.newArrayList();
      }
      User currentUser = userService.getCurrentUser();
      PersistenceManager pm = PMF.getPersistenceManagerFactory().getPersistenceManager();
      Transaction txn = pm.currentTransaction();
      try {        
        txn.begin();
        preQuery(currentUser, pm);
        pm.flush();
        Query query = pm.newQuery(Book.class);
        query.setOrdering("Created desc");
        query.setFilter("Owner == :userId");
        query.setIgnoreCache(false);
        @SuppressWarnings("unchecked")
        List<Book> results = Lists.newArrayList(pm.detachCopyAll((List<Book>) query.execute(currentUser.getUserId())));
        txn.commit();
        // TODO(jamie): This can't be necessary can it?
        return postQuery(results);
      } finally {
        if (txn.isActive()) {
          txn.rollback();
        }
        pm.close();
      }
    }

    public List<Book> postQuery(List<Book> results) {
      return results;
    }
  }
}
