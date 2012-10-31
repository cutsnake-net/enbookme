// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the login RPC service.
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
  LoginInfo login(String dest) throws IllegalArgumentException;
}
