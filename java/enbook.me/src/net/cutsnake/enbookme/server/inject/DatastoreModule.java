package net.cutsnake.enbookme.server.inject;

import javax.inject.Singleton;

import net.cutsnake.enbookme.server.BookManager;
import net.cutsnake.enbookme.server.ObjectifyBookManager;
import net.cutsnake.enbookme.shared.Book;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

/**
 * Datsastore bindings.
 *
 * @author jamie
 */
public class DatastoreModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(BookManager.class).to(ObjectifyBookManager.class);
  }
  
  @Provides
  Objectify provideObjectify(ObjectifyFactory factory) {
    return factory.begin().cache(true);
  }

  @Provides @Singleton ObjectifyFactory provideObjectifyFactory() {
    ObjectifyFactory factory = new ObjectifyFactory();
    factory.register(Book.class);
    return factory;
  }
}
