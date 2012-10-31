// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import javax.inject.Singleton;

import net.cutsnake.enbookme.client.LoginInfo;
import net.cutsnake.enbookme.client.LoginService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Singleton
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

  @Override
  public LoginInfo login(String dest) throws IllegalArgumentException {
    UserService userService = UserServiceFactory.getUserService();
    LoginInfo info = new LoginInfo().setLoggedIn(userService.isUserLoggedIn());
    if (info.isLoggedIn()) {
      User currentUser = userService.getCurrentUser();
      info.setLogoutUrl(userService.createLogoutURL(dest)).setSuperUser(userService.isUserAdmin())
          .setEmail(currentUser.getEmail()).setNickname(currentUser.getNickname())
          .setUserId(currentUser.getUserId());
    } else {
      info.setLoginUrl(userService.createLoginURL(dest));
    }
    return info;
  }
}
