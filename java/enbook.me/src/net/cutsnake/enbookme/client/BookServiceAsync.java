// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import java.util.List;

import net.cutsnake.enbookme.shared.Book;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface BookServiceAsync {

  void list(AsyncCallback<List<Book>> callback);

  void add(Book book, AsyncCallback<List<Book>> callback);

  void remove(List<Book> books, AsyncCallback<List<Book>> callback);
}
