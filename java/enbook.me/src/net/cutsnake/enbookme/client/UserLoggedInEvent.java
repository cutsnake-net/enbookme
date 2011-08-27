// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import net.cutsnake.enbookme.client.UserLoggedInEvent.Handler;

import com.google.web.bindery.event.shared.Event;

/**
 *  Event fired when a user is logged in.
 *  
 * @author jamie
 */
public class UserLoggedInEvent extends Event<Handler> {

  public static Type<Handler> TYPE = new Type<Handler>();
  
  public interface Handler {
    void handle(UserLoggedInEvent e);
  }
  
  private LoginInfo loginInfo;
  
  public UserLoggedInEvent(LoginInfo loginInfo) {
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
