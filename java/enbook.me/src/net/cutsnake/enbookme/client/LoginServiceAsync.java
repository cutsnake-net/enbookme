// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of {@link LoginService}.
 */
public interface LoginServiceAsync {

  void login(String dest, AsyncCallback<LoginInfo> callback);

}
