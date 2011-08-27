// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import net.cutsnake.enbookme.shared.Book;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

/**
 * Servlet that checks if the checksum of a given URL has changed, and if so
 * triggers and update.
 * 
 * @author jamie
 */
@SuppressWarnings("serial")
public class UpdateCheckerServlet extends AbstractBookServlet {
  private UrlChecksummer checker = new UrlChecksummer();

  @Override
  protected int service(Book book) {
    book.setLastChecked(System.currentTimeMillis());
    String md5 = checker.checksum(book.getUrl());
    if (md5 != null && !md5.equals(book.getChecksum())) {
      // Source has changed. Update.
      book.setChecksum(md5);
      Queue queue = QueueFactory.getQueue("update-queue");
      queue.add(withUrl("/tasks/update/" + book.getKey()));
    }
    return 200;
  }
}
