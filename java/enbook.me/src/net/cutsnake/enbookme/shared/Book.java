// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

/**
 * Defines a book mapping.
 * 
 * @author jamie
 */
@SuppressWarnings("serial")
@Cache @Entity
public class Book implements Serializable {

  @Id private String key;
  private String url;
  private String name;
  @Index private String owner;
  private String email;
  private long created = -1;

  /**
   * The last time the content at the url was checked for updates.
   */
  @Index private long lastChecked = -1;

  /**
   * The last time the content at the url was found to have changed.
   */
  private long lastChanged = -1;

  /**
   * The last time the content at the url was sent as an update.
   */
  private long lastUpdateSent = -1;
  private String checksum;
  private int length = -1;

  public String getKey() {
    return key;
  }

  public String getUrl() {
    return url;
  }

  public Book setUrl(String url) {
    this.url = url;
    return this;
  }

  public String getName() {
    return name;
  }

  public Book setName(String name) {
    this.name = name;
    return this;
  }

  public String getOwner() {
    return owner;
  }

  public Book setOwner(String owner) {
    this.owner = owner;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Book setEmail(String email) {
    this.email = email;
    return this;
  }

  public long getCreated() {
    return created;
  }

  public Book setCreated(long created) {
    this.created = created;
    return this;
  }

  public long getLastChecked() {
    return lastChecked;
  }

  public Book setLastChecked(long lastChecked) {
    this.lastChecked = lastChecked;
    return this;
  }

  public long getLastChanged() {
    return lastChanged;
  }

  public Book setLastChanged(long lastChanged) {
    this.lastChanged = lastChanged;
    return this;
  }

  public long getLastUpdateSent() {
    return lastUpdateSent;
  }

  public Book setLastUpdateSent(long lastUpdateSent) {
    this.lastUpdateSent = lastUpdateSent;
    return this;
  }

  public String getChecksum() {
    return checksum;
  }

  public Book setChecksum(String checksum) {
    this.checksum = checksum;
    return this;
  }

  public int getLength() {
    return length;
  }

  public Book setLength(int length) {
    this.length = length;
    return this;
  }

  @OnSave void updateRecord() {
    if (created < 0) {
      created = System.currentTimeMillis();
    }
    // Following serves to ensure we don't stomp all over someone else's record merely by
    // changing the owner etc
    key = createKey(owner, url, email);
  }

  public static String createKey(String ownerId, String url, String email) {
    return ownerId + "|" + url + "|" + email;
  }
}
