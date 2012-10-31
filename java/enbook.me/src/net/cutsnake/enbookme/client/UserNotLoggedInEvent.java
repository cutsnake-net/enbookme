// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import net.cutsnake.enbookme.client.UserNotLoggedInEvent.Handler;

import com.google.web.bindery.event.shared.Event;

/**
 * Event fired when a user is not logged in.
 * 
 * @author jamie
 */
public class UserNotLoggedInEvent extends Event<Handler> {

  public static Type<Handler> TYPE = new Type<Handler>();

  public interface Handler {
    void handle(UserNotLoggedInEvent e);
  }

  private LoginInfo loginInfo;

  public UserNotLoggedInEvent(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
  }

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.handle(this);
  }
}
