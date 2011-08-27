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
  private String Email;
  
  @Persistent
  private long Created = -1;
  
  /**
   * The last time the content at the URL was checked for updates.
   */
  @Persistent
  private long LastChecked = -1;
  
  /**
   * The last time the content at the URL was found to have changed.
   */
  @Persistent
  private long LastChanged = -1;
  
  /**
   * The last time the content at the URL was sent as an update.
   */
  @Persistent
  private long LastUpdateSent = -1;
  
  @Persistent
  private String Checksum;
  
  @Persistent
  private int Length = -1;

  public String getKey() {
    return Key;
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

  public String getEmail() {
    return Email;
  }

  public Book setEmail(String email) {
    Email = email;
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

  public long getLastUpdateSent() {
    return LastUpdateSent;
  }

  public Book setLastUpdateSent(long lastUpdateSent) {
    LastUpdateSent = lastUpdateSent;
    return this;
  }

  public String getChecksum() {
    return Checksum;
  }

  public Book setChecksum(String checksum) {
    Checksum = checksum;
    return this;
  }

  public int getLength() {
    return Length;
  }

  public Book setLength(int length) {
    Length = length;
    return this;
  }
}
