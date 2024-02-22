///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionClose12.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionClose12.java
//
// Classes:      JDConnectionClose12
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDJobName;
import test.JDReflectionUtil;
import test.JDTestDriver;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;



/**
Testcase JDConnectionClose12.  his tests the following methods
of the JDBC Connection class:

<ul>
<li>close()</li>
<li>isClosed()</li>
</ul>
**/
public class JDConnectionClose12
extends JDConnectionClose {






/**
Constructor.
**/
    public JDConnectionClose12 (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              String pwrSysUserID, 
                              String pwrSysPassword)
    {
        super (systemObject, "JDConnectionClose12",
               namesAndVars, runMode, fileOutputStream,
               password,
               pwrSysUserID,
               pwrSysPassword);
    }



    public void Var001 () { notApplicable(); }
    public void Var002 () { notApplicable(); }
    public void Var003 () { notApplicable(); }
    public void Var004 () { notApplicable(); }
    public void Var005 () { notApplicable(); }
    public void Var006 () { notApplicable(); }
    public void Var007 () { notApplicable(); }
    public void Var008 () { notApplicable(); }
    public void Var009 () { notApplicable(); }


    
    /* New Variation to check leaking by UDBDataSource */ 
  public void Var012() {
    if (getDriver() != JDTestDriver.DRIVER_NATIVE) {
      notApplicable("Native JDBC test for UDBDataSource"); 
      return; 
    }
    
    boolean passed = true;
    StringBuffer sb = new StringBuffer();

    int CONNECTIONS_TO_TEST = 300;
    int STATEMENTS_PER_CONNECTION = 300;
    if (isToolboxDriver()) {
      CONNECTIONS_TO_TEST = 100;
      STATEMENTS_PER_CONNECTION = 100;
    }

    STATEMENTS_PER_CONNECTION = overrideFromProperty(
        "test.JDConnectionClose.STATEMENTS_PER_CONNECTION",
        STATEMENTS_PER_CONNECTION);
    CONNECTIONS_TO_TEST = overrideFromProperty(
        "test.JDConnectionClose.CONNECTIONS_TO_TEST", CONNECTIONS_TO_TEST);

    
    // Run for a maximum of 5 minutes = 300 seconds
    long lastTime = System.currentTimeMillis() + 300000;
    long nextMessage = System.currentTimeMillis() + 15000;
    try {

      // Create a ConnectionPoolDataSource implementation
      ConnectionPoolDataSource cpds = (ConnectionPoolDataSource) 
    		  JDReflectionUtil.createObject( "com.ibm.db2.jdbc.app.UDBConnectionPoolDataSource");
      JDReflectionUtil.callMethod_V(cpds, "setDescription", "Connection Pooling DataSource object");

      // Establish a JNDI context and bind the connection pool data source
      System.setProperty(Context.INITIAL_CONTEXT_FACTORY,  "com.sun.jndi.fscontext.RefFSContextFactory");
      Context ctx = new InitialContext();
      ctx.rebind("ConnectionSupport", cpds);

      // Create a standard data source that references it.
      DataSource ds = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBDataSource"); 
      JDReflectionUtil.callMethod_V(ds, "setDescription","DataSource supporting pooling");
      JDReflectionUtil.callMethod_V(ds, "setDataSourceName", "ConnectionSupport");
     
      JDReflectionUtil.callMethod_V(ds, "setFullErrors",true); 
      ctx.rebind("PoolingDataSource", ds);
      
      for (int i = 0; i < CONNECTIONS_TO_TEST / 2
          && System.currentTimeMillis() < lastTime; i++) {

        if (System.currentTimeMillis() > nextMessage) {
          long timeleft = (lastTime - System.currentTimeMillis()) / 1000;
          String message = "JDConnectionClose.Var012 loop=" + i + "/"
              + CONNECTIONS_TO_TEST + " or timeLeft = " + timeleft + "s";
          JDJobName.sendProgramMessage(message);
          System.out.println(message);
          nextMessage = System.currentTimeMillis() + 15000;
        }

        // Allocate two connections
        // Leak the first and use the second
        ds.getConnection(); 
        Connection con2 = ds.getConnection(); 
        
        PreparedStatement[] ps = new PreparedStatement[STATEMENTS_PER_CONNECTION]; 
        for (int j = 0; j < STATEMENTS_PER_CONNECTION; j++) {
	    try {

		ps[j] = con2.prepareStatement("Select * from sysibm.sysdummy1"); 

	    } catch (Exception e) {
		String message = e.toString();
		if ((message.indexOf("Error occurred in SQL Call Level Interface") >=  0) &&
		    (message.indexOf("The error code is 14") >= 0 )) {
		    System.out.println("Warning.. out of handles, forcing gc");
		    System.gc();
		    j = STATEMENTS_PER_CONNECTION; 
		} 
	    } 

        } 
        for (int j = 0; j < STATEMENTS_PER_CONNECTION; j++) {
	    if (ps[j] != null) { 
		ps[j].executeQuery();
	    }
        } 
        
        con2.close(); 
        
        
        
      } /* for */ 

      // We pass if we complete without any exceptions.
      assertCondition(passed);
    } catch (Throwable e) {
      failed(e, "Unexpected Exception\n " + sb.toString());
    }

  }    
    
}



