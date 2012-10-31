package net.cutsnake.enbookme.server.inject;

import net.cutsnake.enbookme.server.AllBooksCheckerServlet;
import net.cutsnake.enbookme.server.BookServiceImpl;
import net.cutsnake.enbookme.server.EmailUpdaterServlet;
import net.cutsnake.enbookme.server.LoginServiceImpl;
import net.cutsnake.enbookme.server.UpdateCheckerServlet;

/**
 * Servlet path bindings.
 *
 * @author jamie
 */
public class ServletModule extends com.google.inject.servlet.ServletModule {

  @Override
  public void configureServlets() {
    serve("/enbookme/login").with(LoginServiceImpl.class);
    serve("/enbookme/books").with(BookServiceImpl.class);
    serve("/tasks/checkall").with(AllBooksCheckerServlet.class);
    serve("/tasks/check").with(UpdateCheckerServlet.class);
    serve("/tasks/update").with(EmailUpdaterServlet.class);
  }
}
