///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDDxx.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 //////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDDMDDxx.java
 //
 // Classes:      JDDMDDxx
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.util.Hashtable;



/**
Testcase JDDMDDxx.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>dataDefinitionCausesTransactionCommit()
<li>dataDefinitionIgnoredInTransactions()
<li>doesMaxRowSizeIncludeBlobs()
</ul>
**/
public class JDDMDDxx
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDDxx";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }
    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;



/**
Constructor.
**/
    public JDDMDDxx (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDDxx",
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
        // SQL400 - changed to use a generic URL so that it would 
        //          work with both the toolbox and the native driver.
        
        String url = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
            
            ;
        if(getDriver() == JDTestDriver.DRIVER_JCC) {
          url = baseURL_; 
          connection_ = testDriver_.getConnection (url,
              systemObject_.getUserId(),
              encryptedPassword_);
          dmd_ = connection_.getMetaData ();

          closedConnection_ = testDriver_.getConnection (url,
              systemObject_.getUserId(),
              encryptedPassword_);

        } else { 
          connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          closedConnection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

        }
        dmd_ = connection_.getMetaData ();

        dmd2_ = closedConnection_.getMetaData ();
        closedConnection_.close ();
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
dataDefinitionCausesTransactionCommit() - Should return false on an open connection.
**/
    public void Var001()
    {
        try {
            assertCondition (dmd_.dataDefinitionCausesTransactionCommit() == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
dataDefinitionCausesTransactionCommit() - Should return false on a closed connection.
**/
    public void Var002()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't permit use of closed connection");
      } else {

        try {
            assertCondition (dmd2_.dataDefinitionCausesTransactionCommit() == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
dataDefinitionIgnoredInTransactions() - Should return false on an open connection.
**/
    public void Var003()
    {
        try {
            assertCondition (dmd_.dataDefinitionIgnoredInTransactions() == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
dataDefinitionIgnoredInTransactions() - Should return false on a closed connection.
**/
    public void Var004()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't permit use of closed connection");
      } else {

        try {
            assertCondition (dmd2_.dataDefinitionIgnoredInTransactions() == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
doesMaxRowSizeIncludeBlobs() - Should return true on an open connection.
**/
    public void Var005()
    {
        try {
          if (JDTestDriver.isLUW()) {
            // LUW returns false 
            assertCondition (dmd_.doesMaxRowSizeIncludeBlobs() == false);
          } else { 
            assertCondition (dmd_.doesMaxRowSizeIncludeBlobs() == true);
          }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
doesMaxRowSizeIncludeBlobs() - Should return true on a closed connection.
**/
    public void Var006()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't permit use of closed connection");
      } else {

        try {
            assertCondition (dmd2_.doesMaxRowSizeIncludeBlobs() == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



}



