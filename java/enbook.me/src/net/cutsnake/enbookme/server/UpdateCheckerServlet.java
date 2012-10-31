// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cutsnake.enbookme.shared.Book;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

/**
 * Servlet that checks if the checksum of a given URL has changed, and if so
 * triggers and update.
 * 
 * @author jamie
 */
@SuppressWarnings("serial")
@Singleton
public class UpdateCheckerServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(UpdateCheckerServlet.class.getName());
  private UrlChecksummer checker = new UrlChecksummer();
  private final BookManager books;

  @Inject
  public UpdateCheckerServlet(UrlChecksummer checker, BookManager books) {
    this.checker = checker;
    this.books = books;
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    String bookKey = req.getParameter("book");
    if (Strings.isNullOrEmpty(bookKey)) {
      log.warning("Book key not supplied.  Exiting");
      return;
    } else {
      log.log(Level.INFO, "Checking book {0}", bookKey);
    }
    Optional<Book> lookup = books.lookup(bookKey);
    if (!lookup.isPresent()) {
      log.warning("Book with key " + bookKey + " does not exist.  Exiting");
      return;
    }
    Book book = lookup.get();
    book.setLastChecked(System.currentTimeMillis());
    books.add(book);
    String md5 = checker.checksum(book.getUrl());
    if (md5 != null && !md5.equals(book.getChecksum())) {
      // Source has changed. Update.
      book.setChecksum(md5);
      Queue queue = QueueFactory.getQueue("update-queue");
      queue.add(withUrl("/tasks/update").param("book", book.getKey()));
    }
    resp.setStatus(200);
  }
}
