// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import java.util.List;

import net.cutsnake.enbookme.shared.Book;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("books")
public interface BookService extends RemoteService {
	List<Book> list() throws IllegalArgumentException;
  List<Book> add(Book book) throws IllegalArgumentException;
  List<Book> remove(List<Book> books) throws IllegalArgumentException;
}
