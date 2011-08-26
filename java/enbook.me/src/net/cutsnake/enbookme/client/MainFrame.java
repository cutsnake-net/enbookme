// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author jamie
 */
public class MainFrame extends Composite implements AcceptsOneWidget, UserLoggedInEvent.Handler, UserNotLoggedInEvent.Handler {

  private static MainFrameUiBinder uiBinder = GWT.create(MainFrameUiBinder.class);

  interface MainFrameUiBinder extends UiBinder<Widget, MainFrame> {}

  @UiField Anchor username;
  @UiField AcceptsOneWidget content;
  
  public MainFrame(IsWidget w) {
    initWidget(uiBinder.createAndBindUi(this));
    content.setWidget(w);
  }

  @Override
  public void setWidget(IsWidget w) {
    content.setWidget(w);
  }

  @Override
  public void handle(UserLoggedInEvent e) {
    username.setText(e.getLoginInfo().getNickname());
    username.setHref(e.getLoginInfo().getLogoutUrl());
    username.setTitle("Sign out");
  }

  @Override
  public void handle(UserNotLoggedInEvent e) {
    username.setText("Sign in");
    username.setHref(e.getLoginInfo().getLoginUrl());
    username.setTitle("Sign in");
  }
}
