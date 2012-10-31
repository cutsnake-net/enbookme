/**
 * 
 */
package net.cutsnake.enbookme.server.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * @author jamie
 *
 */
public class ContextListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule(), new ServerModule(), new DatastoreModule());
  }

}
