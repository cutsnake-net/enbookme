// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.cutsnake.enbookme.client.BookService;
import net.cutsnake.enbookme.shared.Book;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.labs.repackaged.com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the Book service.
 */
@SuppressWarnings("serial")
@Singleton
public class BookServiceImpl extends RemoteServiceServlet implements BookService {

  private final UserService userService;
  private final BookManager books;

  @Inject
  public BookServiceImpl(UserService userService, BookManager books) {
    this.userService = userService;
    this.books = books;
  }

  @Override
  public List<Book> list() throws IllegalArgumentException {
    if (!userService.isUserLoggedIn()) {
      return Lists.newArrayList();
    }
    User currentUser = userService.getCurrentUser();
    return Lists.newArrayList(books.listForUser(currentUser.getUserId()));
  }

  @Override
  public List<Book> add(final Book book) throws IllegalArgumentException {
    Preconditions.checkArgument(book != null);
    if (!userService.isUserLoggedIn()) {
      return Lists.newArrayList();
    }
    User currentUser = userService.getCurrentUser();
    if (Strings.isNullOrEmpty(book.getOwner())) {
      book.setOwner(currentUser.getUserId());
    }
    return Lists.newArrayList(books.add(book));
  }

  @Override
  public List<Book> remove(final List<Book> bookList) throws IllegalArgumentException {
    Preconditions.checkArgument(bookList != null);
    if (!userService.isUserLoggedIn()) {
      return Lists.newArrayList();
    }
    User currentUser = userService.getCurrentUser();
    List<Book> validBooks = Lists.newArrayList();
    for (Book book : bookList) {
      if (book.getOwner().equals(currentUser.getUserId())) {
        validBooks.add(book);
      }
    }
    books.remove(validBooks);
    return validBooks;
  }
}
