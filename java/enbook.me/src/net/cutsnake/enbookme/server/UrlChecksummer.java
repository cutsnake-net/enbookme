// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.io.Closeables;


/**
 * Tool for checksumming URL contents.
 * 
 * @author jamie
 */
public class UrlChecksummer {
  public String checksum(String urlStr) {
    InputStream is = null;
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      URL url = new URL(urlStr);
      is = url.openStream();
      byte[] buffer = new byte[8192];
      int read = 0;
      while( (read = is.read(buffer)) > 0) {
        digest.update(buffer, 0, read);
      }   
      byte[] md5sum = digest.digest();
      BigInteger bigInt = new BigInteger(1, md5sum);
      return bigInt.toString(16);
    } catch(IOException e) {
      throw new RuntimeException("Unable to process file for MD5", e);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Unable to find algorithm for MD5", e);
    } finally {
      Closeables.closeQuietly(is);
    }
  }
}
