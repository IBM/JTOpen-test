///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDIsXxx.java
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
 // File Name:    JDDMDIsXxx.java
 //
 // Classes:      JDDMDIsXxx
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
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
Testcase JDDMDGetXxx.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>isCatalogAtStart()
<li>isReadOnly()
</ul>
**/
public class JDDMDIsXxx
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;
    private String              url_;



/**
Constructor.
**/
    public JDDMDIsXxx (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDIsXxx",
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
      if (getDriver() == JDTestDriver.DRIVER_JCC ) {
        url_ = "jdbc:db2://"+systemObject_.getSystemName()+":"+JDTestDriver.jccPort+"/"+JDTestDriver.jccDatabase;
        connection_ = testDriver_.getConnection (url_, systemObject_.getUserId(), encryptedPassword_ );
        dmd_ = connection_.getMetaData ();

        closedConnection_ = testDriver_.getConnection (url_, systemObject_.getUserId(), encryptedPassword_);
  
      } else { 
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            url_ = "jdbc:as400://" + systemObject_.getSystemName()
                ;
	else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE)
	    url_ = "jdbc:jtopenlite://" + systemObject_.getSystemName()
	     ;

        else
            url_ = "jdbc:db2://" + systemObject_.getSystemName()
                ;


        connection_ = testDriver_.getConnection (url_,systemObject_.getUserId(), encryptedPassword_);
        dmd_ = connection_.getMetaData ();

        closedConnection_ = testDriver_.getConnection (url_,systemObject_.getUserId(), encryptedPassword_);
      }
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
isCatalogAtStart() - Should return the correct value on an
open connection.
**/
    public void Var001()
    {
        try {
            assertCondition (dmd_.isCatalogAtStart() == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCatalogAtStart() - Should return the correct value on a
closed connection.
**/
    public void Var002()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support calls on closed driver"); 
      } else { 
        try {
            assertCondition (dmd2_.isCatalogAtStart() == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
isReadOnly() - Should return false on an open connection with
default access.
**/
    public void Var003()
    {
        try {
            assertCondition (dmd_.isReadOnly() == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
isReadOnly() - Should return true on an open connection with
the read only access property.
**/
    public void Var004()
    {
        try {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support readonly property "); 
          } else { 

            Connection c = testDriver_.getConnection (url_
                + ";access=read only");
            DatabaseMetaData dmd = c.getMetaData ();
            boolean ro = dmd.isReadOnly ();
            c.close ();
            assertCondition (ro == true);
          }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
isReadOnly() - Should return true on an open connection with
the read call access property.
**/
    public void Var005()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support readcall property "); 
      } else { 
        try {
            Connection c = testDriver_.getConnection (url_
                + ";access=read call");
            DatabaseMetaData dmd = c.getMetaData ();
            boolean ro = dmd.isReadOnly ();
            c.close ();
            assertCondition (ro == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
isReadOnly() - Should return true on an open connection
after setReadOnly() has been called.
**/
    public void Var006()
    {
        try {
            Connection c = testDriver_.getConnection (url_,systemObject_.getUserId(), encryptedPassword_);
            DatabaseMetaData dmd = c.getMetaData ();
            c.setReadOnly (true);
            boolean ro = dmd.isReadOnly ();
            c.close ();
            assertCondition (ro == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
isReadOnly() - Should throw an exception on a closed connection.
**/
    public void Var007()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support calls on closed driver"); 
      } else { 
        try {
            dmd2_.isReadOnly();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }



}



