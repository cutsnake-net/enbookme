// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Defines a book mapping.
 * 
 * @author jamie
 */
@SuppressWarnings("serial")
@PersistenceCapable(detachable = "true")
public class Book implements Serializable {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
  private String Key;
  
  @Persistent
  private String Url;
  
  @Persistent
  private String Name;
  
  @Persistent
  private String Owner;
  
  @Persistent
  private long Created = -1;
  
  @Persistent
  private long LastChecked = -1;
  
  @Persistent
  private long LastChanged = -1;

  public String getKey() {
    return Key;
  }

  public Book setKey(String key) {
    Key = key;
    return this;
  }

  public String getUrl() {
    return Url;
  }

  public Book setUrl(String url) {
    Url = url;
    return this;
  }

  public String getName() {
    return Name;
  }

  public Book setName(String name) {
    Name = name;
    return this;
  }

  public String getOwner() {
    return Owner;
  }

  public Book setOwner(String owner) {
    Owner = owner;
    return this;
  }

  public long getCreated() {
    return Created;
  }

  public Book setCreated(long created) {
    Created = created;
    return this;
  }

  public long getLastChecked() {
    return LastChecked;
  }

  public Book setLastChecked(long lastChecked) {
    LastChecked = lastChecked;
    return this;
  }

  public long getLastChanged() {
    return LastChanged;
  }

  public Book setLastChanged(long lastChanged) {
    LastChanged = lastChanged;
    return this;
  }
}
