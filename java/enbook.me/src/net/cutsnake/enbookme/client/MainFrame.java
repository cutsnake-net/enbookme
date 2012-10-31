// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A frame surrounding the whole app.
 * 
 * @author jamie
 */
public class MainFrame extends Composite implements AcceptsOneWidget, UserLoggedInEvent.Handler,
    UserNotLoggedInEvent.Handler {

  private static MainFrameUiBinder uiBinder = GWT.create(MainFrameUiBinder.class);

  interface MainFrameUiBinder extends UiBinder<Widget, MainFrame> {
  }

  @UiField
  Anchor username;
  @UiField
  Label help;
  @UiField
  SimplePanel content;
  @UiField
  SimplePanel helpPanel;
  @UiField
  LayoutPanel layoutPanel;

  public MainFrame(IsWidget w) {
    initWidget(uiBinder.createAndBindUi(this));
    content.setWidget(w);
    layoutPanel.setWidgetVisible(helpPanel, false);
    layoutPanel.setWidgetVisible(content, true);
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

  @UiHandler("help")
  public void onHelp(ClickEvent e) {
    if (help.getText().equals("help")) {
      layoutPanel.setWidgetVisible(content, false);
      layoutPanel.setWidgetVisible(helpPanel, true);
      help.setText("back");
      // helpPanel.setVisible(true);
      // content.setVisible(false);
    } else {
      layoutPanel.setWidgetVisible(helpPanel, false);
      layoutPanel.setWidgetVisible(content, true);
      help.setText("help");
      // helpPanel.setVisible(false);
      // content.setVisible(true);
    }
  }
}
