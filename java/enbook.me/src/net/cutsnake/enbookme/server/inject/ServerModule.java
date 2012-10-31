/**
 * 
 */
package net.cutsnake.enbookme.server.inject;

import net.cutsnake.enbookme.client.BookService;
import net.cutsnake.enbookme.client.LoginService;
import net.cutsnake.enbookme.server.BookServiceImpl;
import net.cutsnake.enbookme.server.LoginServiceImpl;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * @author jamie
 *
 */
public class ServerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(LoginService.class).to(LoginServiceImpl.class);
    bind(BookService.class).to(BookServiceImpl.class);
  }

  @Provides UserService provideUserService() {
    return UserServiceFactory.getUserService();
  }
}
