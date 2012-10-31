// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cutsnake.enbookme.shared.Book;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

/**
 * Base class for a servlet that looks a book and does something to it.
 * 
 * @author jamie
 */
@SuppressWarnings("serial")
@Singleton
public class AllBooksCheckerServlet extends HttpServlet {

  private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 100;
  private static final Logger log = Logger.getLogger(AllBooksCheckerServlet.class.getName());
  private BookManager books;

  @Inject
  public AllBooksCheckerServlet(BookManager books) {
    super();
    this.books = books;
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    long oneDayAgo = System.currentTimeMillis() - ONE_DAY_IN_MILLIS;
      Iterable<Book> results = books.listLastCheckedBefore(oneDayAgo);
      for (Book book : results) {
        log.info("Scheduling new checks");
        Queue queue = QueueFactory.getQueue("check-queue");
        queue.add(withUrl("/tasks/check").param("book", book.getKey()));
      }
  }
}
