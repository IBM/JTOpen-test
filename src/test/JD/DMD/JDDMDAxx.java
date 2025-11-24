///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDAxx.java
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
 // File Name:    JDDMDAxx.java
 //
 // Classes:      JDDMDAxx
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

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDDMDAxx.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>allProceduresAreCallable()
<li>allTablesAreSelectable()
<li>generateKeyAlwaysReturned() 
</ul>
**/
public class JDDMDAxx
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDAxx";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }


    // Private data.
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;



/**
Constructor.
**/
    public JDDMDAxx (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDAxx",
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
        String url; 
        if(getDriver() == JDTestDriver.DRIVER_JCC) {
          url = baseURL_;
      connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_ );
        
        } else {
         url = baseURL_
            ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        }
        dmd_ = connection_.getMetaData ();
        if(getDriver() == JDTestDriver.DRIVER_JCC) {
          closedConnection_ = testDriver_.getConnection (url, systemObject_.getUserId(), encryptedPassword_ );
        } else {
        closedConnection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
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
        connection_ = null; 

    }



/**
allProceduresAreCallable() - Should return false on an open connection.
**/
    public void Var001()
    {
        try {
            assertCondition (dmd_.allProceduresAreCallable() == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
allProceduresAreCallable() - Should return false on a closed connection.
**/
    public void Var002() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit use of closed connection");
    } else {
      try {
        assertCondition(dmd2_.allProceduresAreCallable() == false);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }



/**
 * allTablesAreSelectable() - Should return false on an open connection.
 */
    public void Var003()
    {
        try {
            assertCondition (dmd_.allTablesAreSelectable() == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
allTablesAreSelectable() - Should return false on a closed connection.
**/
    public void Var004()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't permit use of closed connection");
      } else {

        try {
            assertCondition (dmd2_.allTablesAreSelectable() == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
generatedKeyAlwaysReturned() - Should return false on an open connection.
**/
    public void Var005()
    {
	if (checkJdbc41()) { 
	    try {
		assertCondition (JDReflectionUtil.callMethod_B(dmd_,"generatedKeyAlwaysReturned") == false);
	    }
	    catch (Exception e)  {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
generatedKeyAlwaysReturned() - Should return false on a closed connection.
**/
    public void Var006() {
	if (checkJdbc41()) { 
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		notApplicable("JCC doesn't permit use of closed connection");
	    } else {
		try {
		    assertCondition (JDReflectionUtil.callMethod_B(dmd2_,"generatedKeyAlwaysReturned") == false);
		} catch (Exception e) {
		    failed(e, "Unexpected Exception");
		}
	    }
	}
  }





}



