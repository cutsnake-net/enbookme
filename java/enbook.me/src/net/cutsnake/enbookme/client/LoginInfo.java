// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import java.io.Serializable;

/**
 * Defines login details for the user
 * 
 * @author jamie
 */
public class LoginInfo implements Serializable {
  private boolean isLoggedIn;
  private String loginUrl;
  private String logoutUrl;
  private boolean isSuperUser;
  private String email;
  private String nickname;
  private String userId;
  
  public boolean isLoggedIn() {
    return isLoggedIn;
  }
  
  public LoginInfo setLoggedIn(boolean isLoggedIn) {
    this.isLoggedIn = isLoggedIn;
    return this;
  }
  
  public String getLoginUrl() {
    return loginUrl;
  }
  
  public LoginInfo setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
    return this;
  }
  
  public String getLogoutUrl() {
    return logoutUrl;
  }
  
  public LoginInfo setLogoutUrl(String logoutUrl) {
    this.logoutUrl = logoutUrl;
    return this;
  }
  
  public boolean isSuperUser() {
    return isSuperUser;
  }
  
  public LoginInfo setSuperUser(boolean isSuperUser) {
    this.isSuperUser = isSuperUser;
    return this;
  }
  
  public String getEmail() {
    return email;
  }
  
  public LoginInfo setEmail(String email) {
    this.email = email;
    return this;
  }
  
  public String getNickname() {
    return nickname;
  }
  
  public LoginInfo setNickname(String nickname) {
    this.nickname = nickname;
    return this;
  }
  
  public String getUserId() {
    return userId;
  }
  
  public LoginInfo setUserId(String userId) {
    this.userId = userId;
    return this;
  }
}
