// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EnbookMe implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  /**
   * Create a remote service proxy to talk to the server-side Login service.
   */
  private final LoginServiceAsync loginService = GWT.create(LoginService.class);

  /**
   * Create a remote service proxy to talk to the server-side Book service.
   */
  private final BookServiceAsync bookService = GWT.create(BookService.class);
  private final EventBus eventBus = new SimpleEventBus();

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    MainPanel mainWidget = new MainPanel(bookService);
    MainFrame frame = new MainFrame(mainWidget);
    eventBus.addHandler(UserLoggedInEvent.TYPE, frame);
    eventBus.addHandler(UserLoggedInEvent.TYPE, mainWidget);
    eventBus.addHandler(UserNotLoggedInEvent.TYPE, frame);
    RootPanel.get("rootPanel").add(frame);
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      @Override
      public void onSuccess(LoginInfo result) {
        if (result.isLoggedIn()) {
          eventBus.fireEvent(new UserLoggedInEvent(result));
        } else {
          eventBus.fireEvent(new UserNotLoggedInEvent(result));
        }
      }

      @Override
      public void onFailure(Throwable caught) {
        Window.alert("Could not contact server for login details");
      }
    });

  }
}
