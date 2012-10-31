package net.cutsnake.enbookme.server;

import java.util.List;

import net.cutsnake.enbookme.shared.Book;

import com.google.common.base.Optional;

/**
 * Definition of a book data management interface.
 *
 * @author jamie
 */
public interface BookManager {
  /**
   * Adds a new book, updating if it already exists.
   * @param book
   */
  Book add(Book book);

  /**
   * @return an iterable of all books
   */
  Iterable<Book> list();

  /**
   * Removes a collection of books.
   */
  void remove(List<Book> books);

  Iterable<Book> listLastCheckedBefore(long timestamp);

  Iterable<Book> listForUser(String userId);

  Optional<Book> lookup(String createKey);
}
