// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import net.cutsnake.enbookme.shared.Book;

import com.google.common.io.Closeables;


/**
 * Servlet for sending out email updates.
 * 
 * @author jamie
 */
@SuppressWarnings("serial")
public class EmailUpdaterServlet extends AbstractBookServlet {
  private static final Logger log = Logger.getLogger(EmailUpdaterServlet.class.getName());
  private static final int MAX_SIZE = 10 * 1024 * 1024; // 10MB

  @Override
  protected int service(Book book) {
    String urlStr = book.getUrl();
    book.setLastChanged(System.currentTimeMillis());
    URLConnection conn = null;
    InputStream in = null;
    try {
      URL url = new URL(urlStr);
      conn = url.openConnection();
      int contentLength = conn.getContentLength();
      String contentType = conn.getContentType();
      String contentEncoding = conn.getContentEncoding();
      book.setLength(contentLength);
      if (contentLength > MAX_SIZE) {
        throw new ContentTooLargeException(urlStr, contentLength, MAX_SIZE);
      }
      in = conn.getInputStream();
      ByteArrayOutputStream os = new ByteArrayOutputStream(contentLength);
      int size = 0;
      byte[] b = new byte[1024];
      while (size < MAX_SIZE) {
        int c = in.read(b);
        if (c == -1) {
          break;
        }
        os.write(b);
        size++;
      }
      os.flush();
      os.close();
      if (size >= MAX_SIZE) {
        throw new ContentTooLargeException(urlStr, size, MAX_SIZE);
      }
      MimeMessage message = createMail(book, contentType, contentEncoding, os.toByteArray());
      Transport.send(message);
      book.setLastUpdateSent(System.currentTimeMillis());
      return 200;
    } catch (MessagingException e) {
      log.log(Level.SEVERE, "Could not send message.", e);
      return 500;
    } catch (MalformedURLException e) {
      log.log(Level.SEVERE, "Could not parse URL.", e);
      return 500;
    } catch (IOException e) {
      log.log(Level.SEVERE, "Could not read data.", e);
      return 500;
    } catch (ContentTooLargeException e) {
      log.warning(e.getMessage());
      return 500;
    } finally {
      Closeables.closeQuietly(in);
    }
  }

  private MimeMessage createMail(Book book, String contentType, String contentEncoding, byte[] attachmentData) throws MessagingException {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);
    
    MimeMessage message = new MimeMessage(session);
    message.setRecipient(Message.RecipientType.TO, new InternetAddress(book.getEmail()));
    message.setSender(new InternetAddress("updates@enbookme.appspotmail.com"));
    
    Multipart mp = new MimeMultipart();

    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setText("Update from Enbook.me");
    mp.addBodyPart(htmlPart);

    MimeBodyPart attachment = new MimeBodyPart();
    log.info("Adding an attachment of type " + contentType);
    DataSource src = new ByteArrayDataSource(attachmentData, contentType); 
    attachment.setFileName(book.getName().replaceAll("\\s", "_") + getFileExtension(contentType));
    attachment.setDataHandler(new DataHandler(src)); 
    attachment.setDisposition(Message.ATTACHMENT);
    mp.addBodyPart(attachment);

    message.setSubject("Update from Enbook.me");
    message.setContent(mp);
    message.saveChanges();
    return message;
  }

  private String getFileExtension(String contentType) {
    if ("application/pdf".equalsIgnoreCase(contentType)) {
      return ".pdf";
    } else if ("text/html".equalsIgnoreCase(contentType)) {
      return ".html";
    } else if ("text/css".equalsIgnoreCase(contentType)) {
      return ".css";
    } else if ("text/plain".equalsIgnoreCase(contentType)) {
      return ".txt";
    } else {
      return ".unknown";
    }
  }

  public static class ContentTooLargeException extends Exception {
    public ContentTooLargeException(String url, int size, int maxSize) {
      super("Content at [" + url + "] exceeds maximum allowed size of [" + maxSize + "] bytes (["
              + size + "] bytes).");
    }
  }
}
