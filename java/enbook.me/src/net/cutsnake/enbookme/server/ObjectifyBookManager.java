// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

import net.cutsnake.enbookme.shared.Book;

import com.google.common.base.Optional;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.cmd.Query;

/**
 * The server side implementation of the Book service.
 */
public class ObjectifyBookManager implements BookManager {
  private static final Logger log = Logger.getLogger(ObjectifyBookManager.class.getName());

  private final Provider<Objectify> objectifies;

  @Inject
  public ObjectifyBookManager(Provider<Objectify> objectifies) {
    this.objectifies = objectifies;
  }

  @Override
  public Iterable<Book> list() {
    return objectifies.get().load().type(Book.class);
  }

  @Override
  public Iterable<Book> listLastCheckedBefore(long timestamp) {
    return objectifies.get().load().type(Book.class).filter("lastChecked <=", timestamp);
  }

  @Override
  public Book add(final Book book) {
    objectifies.get().save().entity(book).now();
    return book;
  }

  @Override
  public void remove(final List<Book> books) {
    objectifies.get().delete().entities(books).now();
  }

  @Override
  public Iterable<Book> listForUser(String userId) {
    return objectifies.get().load().type(Book.class).filter("owner", userId);
  }

  @Override
  public Optional<Book> lookup(String createKey) {
    Key<Book> key = Key.create(Book.class, createKey);
    return Optional.fromNullable(objectifies.get().load().key(key).get());
  }
}
