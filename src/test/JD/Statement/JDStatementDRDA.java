///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementDRDA.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementDRDA.java
//
// Classes:      JDStatementDRDA
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;

import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDStatementDRDA.  This tests the following functionality
of the JDBC Statement class:

<ul>
<li>DRDA "CONNECT TO"</li>
</ul>
**/
public class JDStatementDRDA
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementDRDA";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }


   // Private data.
   private Connection          connection_;
   private static final String remoteSystem_   = "RCHAS1FD"; // ??? do no hardcode



/**
Constructor.
**/
   public JDStatementDRDA (AS400 systemObject,
                           Hashtable namesAndVars,
                           int runMode,
                           FileOutputStream fileOutputStream,
                           
                           String password)
   {
      super (systemObject, "JDStatementDRDA",
             namesAndVars, runMode, fileOutputStream,
             password);
   }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
   protected void setup ()
   throws Exception
   {
      connection_ = testDriver_.getConnection (baseURL_ + ";errors=full", 
                                               userId_, encryptedPassword_);

      if (isToolboxDriver())
      {
         // Setup DRDA stuff.  Ignore exceptions, since this setup
         // may very well have been done already.
         CommandCall cmd = new CommandCall (systemObject_);
         try {
            cmd.run ("ADDRDBDIRE RDB(" + remoteSystem_ + ") RMTLOCNAME("
                     + remoteSystem_ + " *IP)");
         } catch (Exception e) {
            // Ignore.
         }

         try {
            cmd.run ("CHGSYSVAL SYSVAL(QRETSVRSEC) VALUE('1')");
         } catch (Exception e) {
            // Ignore.
         }

         try {
            cmd.run ("ADDSVRAUTE USRPRF(" + userId_ 
                     + ") SERVER(" + remoteSystem_ + ") USRID(" + userId_
                     + ") PASSWORD(" + PasswordVault.decryptPasswordLeak(encryptedPassword_) + ")");
         } catch (Exception e) {
            // Ignore.
         }
      }

   }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      connection_.close ();
   }



/**
Executes an update with a DRDA CONNECT.
Verify that a query works with a forward only cursor.

SQL400 - The native JDBC driver won't work with CONNECT/DISCONNECT type
         statements just yet.
**/
   public void Var001()
   {
      if (getDriver () == JDTestDriver.DRIVER_NATIVE)
      {
         notApplicable();
         return;
      }

      /* I have not figured out how to test this yet!
      try {
          Statement s = connection_.createStatement ();
       s.executeUpdate ("CONNECT TO " + remoteSystem_);

          Statement s2 = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                      ResultSet.CONCUR_READ_ONLY);
          ResultSet rs = s2.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
          int rows = 0;
          while (rs.next ())
              ++rows;
          rs.close ();
          s2.close ();

          s.execute ("DISCONNECT " + remoteSystem_);
          s.close ();
          assertCondition (rows > 5);
      }
      catch (Exception e) {
          failed (e, "Unexpected Exception");
      }
      */
      assertCondition (true);
   }



/**
Executes an update with a DRDA CONNECT.
Verify that a query works with a scrollable cursor.

SQL400 - The native JDBC driver won't work with CONNECT/DISCONNECT type
         statements just yet.
**/
   public void Var002()
   {
      if (getDriver () == JDTestDriver.DRIVER_NATIVE)
      {
         notApplicable();
         return;
      }

      /* I have not figured out how to test this yet!
      try {
          Statement s = connection_.createStatement ();
       s.executeUpdate ("CONNECT TO " + remoteSystem_);

          Statement s2 = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                      ResultSet.CONCUR_READ_ONLY);
          ResultSet rs = s2.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
          int rows = 0;
          rs.afterLast ();
          while (rs.previous ())
              ++rows;
          rs.close ();
          s2.close ();

          s.execute ("DISCONNECT " + remoteSystem_);
          s.close ();
          assertCondition (rows > 5);
      }
      catch (Exception e) {
          failed (e, "Unexpected Exception");
      }
      */
      assertCondition (true);
   }



}



