// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Style;

/**
 *
 * @author jamie
 */
public interface Resources extends CellTable.Resources {
  public static Resources INSTANCE = GWT.create(Resources.class);
  
  @Override
  @Source({"com/google/gwt/user/cellview/client/CellTable.css", "cellTable.css"})
  public Style cellTableStyle();
}
