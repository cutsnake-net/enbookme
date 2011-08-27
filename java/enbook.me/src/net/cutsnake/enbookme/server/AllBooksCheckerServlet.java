// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.common.collect.Lists;

import net.cutsnake.enbookme.shared.Book;

/**
 * Base class for a servlet that looks a book and does something to it.
 * 
 * @author jamie
 */
@SuppressWarnings("serial")
public class AllBooksCheckerServlet extends HttpServlet {

  private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 100;
  private static final Logger log = Logger.getLogger(AllBooksCheckerServlet.class.getName());
  private PersistenceManager pm;
  private HttpServletResponse response;

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    this.response = resp;
    PersistenceManager pm = PMF.getPersistenceManagerFactory().getPersistenceManager();
    Transaction txn = pm.currentTransaction();
    long oneDayAgo = System.currentTimeMillis() - ONE_DAY_IN_MILLIS;
    try {        
      txn.begin();
      Query query = pm.newQuery("select Key from " + Book.class.getSimpleName());
      query.setFilter("LastChecked < :oneDayAgo");
      query.setIgnoreCache(false);
      @SuppressWarnings("unchecked")
      List<String> results = Lists.newArrayList(pm.detachCopyAll((List<String>) query.execute(oneDayAgo)));
      txn.commit();
      log.info("Scheduling " + results.size() + " new checks");
      for (String key : results) {
        Queue queue = QueueFactory.getQueue("check-queue");
        queue.add(withUrl("/tasks/check/" + key));
      }
    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
      pm.close();
    }
  }
  
  protected HttpServletResponse getResponse() {
    return response;
  }
}
