///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/**
 * Test for native JDBC to access *LOCAL while the ASPGRP for the job
 * is set.  The *LOCAL access is really to the IASP.  This
 * detects a bug in the native driver where the right RDB was not
 * being used by the XA code, thus causing confusing in NTS. 
 */ 
package test; 

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import javax.sql.XADataSource;; 

public class JTAUseIASP {



  public static void main(String args[]) {

    try {

      String libraryName="JWEIAPS";

      //
      // Figure out the ASP name
      //
      String aspName = executeShellCommand("system wrkcfgsts *dev *asp 2>&1 | grep AVAILABLE | head -1 | sed 's/^ *\\([^ ][^ ]*\\) .*/\\1/'");

      System.out.println("IASP name is " + aspName);

      //
      // Set the library name based on the IASP name 
      //

      libraryName = "JD"+aspName;
      if (libraryName.length() > 10) {
	  libraryName = libraryName.substring(0,10); 
      } 

      //
      // Switch the ASP group of the thread
      //
      JDJobName.setaspgrp(aspName); 

      //
      // Get the iasp data source
      //
      XADataSource iaspDS = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBXADataSource");
      JDReflectionUtil.callMethod_V(iaspDS,"setDatabaseName",aspName);
      JDReflectionUtil.callMethod_V(iaspDS,"setFullErrors",true);
      XAConnection iaspXAConnection = iaspDS.getXAConnection();
      XAResource iaspResource = iaspXAConnection.getXAResource();
      Connection iaspConnection = iaspXAConnection.getConnection();
      iaspConnection
          .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
      iaspConnection.setAutoCommit(false);

      //
      // Get the sysbas data source
      //
      XADataSource sysbasDS = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBXADataSource");
      JDReflectionUtil.callMethod_V(sysbasDS,"setFullErrors",true);
      XAConnection sysbasXAConnection = sysbasDS.getXAConnection();
      XAResource sysbasResource = sysbasXAConnection.getXAResource();
      Connection sysbasConnection = sysbasXAConnection.getConnection();
      sysbasConnection
          .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
      sysbasConnection.setAutoCommit(false);

      Xid xid;
      Statement s;

      iaspConnection
          .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      Statement stmt = iaspConnection.createStatement();

      try {
        stmt.executeUpdate("create collection "+libraryName+"");
      } catch (Exception e) {
      }

      try {
        stmt.executeUpdate("drop table "+libraryName+".iaspcommit2");
      } catch (Exception e) {
	  String message = e.toString();
	  if (message.indexOf("not found") < 0) {
	      System.out.println("Unexpected exception ");
	      e.printStackTrace(System.out); 
	  } 
      }

      stmt.executeUpdate("Create table "+libraryName+".iaspcommit2(c1 int)");

      iaspConnection.commit();

      iaspConnection
          .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
      // 
      // Do some work on the iasp connection
      //
      System.out.println("IASP " + aspName + " work");
      System.out.println("Catalog is "+iaspConnection.getCatalog());
      xid = new test.TestXid();
      iaspResource.start(xid, XAResource.TMNOFLAGS);
      s = iaspConnection.createStatement();
      s.executeUpdate("Insert into "+libraryName+".iaspcommit2 values(1)");
      s.close();
      iaspResource.end(xid, XAResource.TMSUCCESS);
      iaspResource.commit(xid, true);

      // 
      // Do no work on the sysbase connection -- for the buggy case, this threw
      // an exception
      //
      System.out.println("SYSBASE no work");
      System.out.println("Catalog is "+sysbasConnection.getCatalog()); 
      xid = new test.TestXid();
      sysbasResource.start(xid, XAResource.TMNOFLAGS);
      sysbasResource.end(xid, XAResource.TMSUCCESS);
      sysbasResource.commit(xid, true);

      // 
      // Do some work on the iasp connection
      //
      System.out.println("IASP " + aspName + " work");
      System.out.println("Catalog is "+iaspConnection.getCatalog());
      xid = new test.TestXid();
      iaspResource.start(xid, XAResource.TMNOFLAGS);
      s = iaspConnection.createStatement();
      s.executeUpdate("Insert into "+libraryName+".iaspcommit2 values(2)");
      s.close();
      iaspResource.end(xid, XAResource.TMSUCCESS);
      iaspResource.commit(xid, true);

      // 
      // Do work on the sysbase connection
      //
      System.out.println("SYSBASE work");
      System.out.println("Catalog is "+sysbasConnection.getCatalog());
      xid = new test.TestXid();
      sysbasResource.start(xid, XAResource.TMNOFLAGS);
      s = sysbasConnection.createStatement();
      s.executeUpdate("Insert into "+libraryName+".iaspcommit2 values(2)");
      s.close();
      sysbasResource.end(xid, XAResource.TMSUCCESS);
      sysbasResource.commit(xid, true);

      // 
      // Do some work on the iasp connection
      //
      System.out.println("IASP " + aspName + " work");
      System.out.println("Catalog is "+iaspConnection.getCatalog());

      xid = new test.TestXid();
      iaspResource.start(xid, XAResource.TMNOFLAGS);
      s = iaspConnection.createStatement();
      s.executeUpdate("Insert into "+libraryName+".iaspcommit2 values(4)");
      s.close();
      iaspResource.end(xid, XAResource.TMSUCCESS);
      iaspResource.commit(xid, true);

      // 
      // Do no work on the sysbase connection
      //
      System.out.println("SYSBASE no work");
      System.out.println("Catalog is "+sysbasConnection.getCatalog());
      xid = new test.TestXid();
      sysbasResource.start(xid, XAResource.TMNOFLAGS);
      sysbasResource.end(xid, XAResource.TMSUCCESS);
      sysbasResource.commit(xid, true);
      
      System.out.println("Testcase passed"); 
      
    } catch (Exception e) {
      System.out.println("Unexpected Exception: " + e.getMessage());

      e.printStackTrace();
      System.out.println("Testcase failed"); 

    }
  }

  public static String executeShellCommand(String command) {
    Runtime runtime = Runtime.getRuntime();
    String[] commandArray = new String[3];
    commandArray[0] = "qsh";
    commandArray[1] = "-c";
    commandArray[2] = command;
    try {
      Process p = runtime.exec(commandArray);

      String output = null;

      JDJSTPOutputThread stdoutThread = JDJSTPTestcase.startProcessOutput(p,
          output, true, null, null, null, JDJSTPOutputThread.ENCODING_UNKNOWN);

      p.waitFor();
      stdoutThread.join();

      return stdoutThread.getOutput().trim();
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }





}
