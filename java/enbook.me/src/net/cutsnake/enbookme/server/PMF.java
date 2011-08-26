// Copyright cutsnake.net 2011.  All Rights Reserved.

package net.cutsnake.enbookme.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * 
 * @author jamie
 */
public class PMF {
  private static final PersistenceManagerFactory FACTORY = JDOHelper
          .getPersistenceManagerFactory("transactions-mandatory");

  public static PersistenceManagerFactory getPersistenceManagerFactory() {
    return FACTORY;
  }

  private PMF() {}
}
