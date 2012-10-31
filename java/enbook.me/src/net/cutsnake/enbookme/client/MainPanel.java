// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import java.util.Date;
import java.util.List;

import net.cutsnake.enbookme.shared.Book;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Main interaction widget.
 * 
 * @author jamie
 */
public class MainPanel extends Composite implements UserLoggedInEvent.Handler {

  private static MainPanelUiBinder uiBinder = GWT.create(MainPanelUiBinder.class);
  private final BookServiceAsync bookService;
  private static final String URL_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

  interface MainPanelUiBinder extends UiBinder<Widget, MainPanel> {
  }

  @UiField(provided = true)
  CellTable<Book> bookList;
  @UiField
  TextBox urlBox;
  @UiField
  TextBox nameBox;
  @UiField
  TextBox emailBox;
  private String userEmail;
  private RegExp urlRegex = RegExp.compile(URL_REGEX);

  public MainPanel(BookServiceAsync bookService) {
    this.bookService = bookService;
    this.bookList = new CellTable<Book>(10, Resources.INSTANCE);
    bookList.addColumn(new TextColumn<Book>() {
      @Override
      public String getValue(Book object) {
        return object.getName();
      }
    }, "Name");
    bookList.addColumn(new TextColumn<Book>() {
      @Override
      public String getValue(Book object) {
        return object.getUrl();
      }
    }, "URL");
    bookList.addColumn(new TextColumn<Book>() {
      @Override
      public String getValue(Book object) {
        return new Date(object.getCreated()).toLocaleString();
      }
    }, "Created");
    bookList.addColumn(new TextColumn<Book>() {
      @Override
      public String getValue(Book object) {
        long lastChecked = object.getLastChecked();
        if (lastChecked < 0) {
          return "";
        }
        return new Date(lastChecked).toLocaleString();
      }
    }, "Last Checked");
    bookList.addColumn(new TextColumn<Book>() {
      @Override
      public String getValue(Book object) {
        long lastChanged = object.getLastChecked();
        if (lastChanged < 0) {
          return "";
        }
        return new Date(lastChanged).toLocaleString();
      }
    }, "Last Updated");
    initWidget(uiBinder.createAndBindUi(this));

    nameBox.getElement().setAttribute("placeholder", "the name the file should be sent as");
    urlBox.getElement().setAttribute("placeholder", "the URL of a document you'd like to read");
    emailBox.getElement().setAttribute("placeholder", "the email of your Kindle device");
  }

  @Override
  public void handle(UserLoggedInEvent e) {
    userEmail = e.getLoginInfo().getEmail();
    bookService.list(new AsyncCallback<List<Book>>() {

      @Override
      public void onSuccess(List<Book> result) {
        setBooks(result);
      }

      @Override
      public void onFailure(Throwable caught) {
        Window.alert("Failed to talk to server " + caught.getMessage());
        bookList.setVisible(false);
      }
    });
  }

  private void setBooks(List<Book> result) {
    bookList.setVisible(true);
    bookList.setRowData(result);
  }

  @UiHandler("create")
  public void onCreate(ClickEvent e) {
    String emailAddr = emailBox.getText();
    if (!emailAddr.endsWith("@kindle.com") && !emailAddr.endsWith("@free.kindle.com")
        && !emailAddr.equalsIgnoreCase(userEmail)) {
      Window.alert("Please only use Kindle device emails or the email registered to this account.");
    } else if (!urlRegex.test(urlBox.getText())) {
      Window.alert("Please use a valid URL");
    } else {
      Book book = new Book();
      book.setName(nameBox.getText());
      book.setUrl(urlBox.getText());
      book.setEmail(emailAddr);
      bookService.add(book, new AsyncCallback<List<Book>>() {
        @Override
        public void onFailure(Throwable caught) {
        }

        @Override
        public void onSuccess(List<Book> result) {
          setBooks(result);
        }
      });
      nameBox.setText(null);
      urlBox.setText(null);
      emailBox.setText(null);
    }
  }
}
