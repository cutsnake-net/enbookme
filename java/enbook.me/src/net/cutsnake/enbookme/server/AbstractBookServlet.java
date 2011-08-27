// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cutsnake.enbookme.shared.Book;

/**
 * Base class for a servlet that looks a book and does something to it.
 * 
 * @author jamie
 */
@SuppressWarnings("serial")
public abstract class AbstractBookServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(AbstractBookServlet.class.getName());
  private PersistenceManager pm;
  private HttpServletResponse response;

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    this.response = resp;
    String keyStr = req.getPathInfo().substring(1);
    pm = PMF.getPersistenceManagerFactory().getPersistenceManager();
    Transaction txn = pm.currentTransaction();
    try {
      txn.begin();
      Book book = pm.getObjectById(Book.class, keyStr);
      int status = service(book);
      txn.commit();
      resp.setStatus(status);
    } catch (JDOObjectNotFoundException e) {
      log.warning("Could not find scheduled book [" + keyStr + "]");
    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
      pm.close();
    }
  }

  protected abstract int service(Book book);
  
  protected HttpServletResponse getResponse() {
    return response;
  }
}
