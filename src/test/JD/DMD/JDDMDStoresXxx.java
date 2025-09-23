///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDStoresXxx.java
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
 // File Name:    JDDMDNullXxx.java
 //
 // Classes:      JDDMDNullXxx
 //
 ////////////////////////////////////////////////////////////////////////
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
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDDMDStoresXxx.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>storesLowerCaseIdentifies()
<li>storesLowerCaseQuotedIdentifiers()
<li>storesMixedCaseIdentifiers()
<li>storesMixedCaseQuotedIdentifiers()
<li>storesUpperCaseIdentifiers()
<li>storesUpperCaseQuotedIdentifiers()
</ul>
**/
public class JDDMDStoresXxx
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDStoresXxx";
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
    public JDDMDStoresXxx (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDStoresXxx",
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
        String url;

        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            url = "jdbc:as400://" + systemObject_.getSystemName()
                ;
	else if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE)
            url = "jdbc:jtopenlite://" + systemObject_.getSystemName()
                ;
        else
            url = "jdbc:db2://" + systemObject_.getSystemName()
               ;

        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        dmd_ = connection_.getMetaData ();

        closedConnection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
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
storesLowerCaseIdentifiers() - Should return the correct value as
when the connection is open.

storesLowerCaseIdentifiers is defined as ..

Retrieves whether this database treats mixed case unquoted SQL identifiers as case insensitive and stores them in lower case. 

Should be false, since they are stored in uppercase / not lower case
Fixed by native driver in V6R1



Excepts from note on 11/02/2007 by

The simplest way to express the standard SQL rules are that unquoted identifiers are converted to UPPER CASE during the parsing process, that all quoted (delimited) identifiers are left in their given, specified case, and all identifier comparison is done in a case sensitive manner after the parse completes.

Hence in the standard:
storesLowerCaseIdentifiers()        false; doesn't store unquoted in lower case
 whether database treats mixed case unquoted SQL identifiers as case insensitive and stores them lower case 

storesLowerCaseQuotedIdentifiers()  false; not case insensitive and stored as given, maybe lower case but not forced to be
 whether database treats mixed case quoted SQL identifiers as case insensitive and stores them lower case 

storesMixedCaseIdentifiers()        false; doesn't store unquoted in mixed case 
 whether database treats mixed case unquoted SQL identifiers as case insensitive and stores them mixed case 

storesMixedCaseQuotedIdentifiers()  false; doesn't treat them as case insensitive
 whether database treats mixed case quoted SQL identifiers as case insensitive and stores them mixed case 

storesUpperCaseIdentifiers()        true
 whether database treats mixed case unquoted SQL identifiers as case insensitive and stores them upper case 

storesUpperCaseQuotedIdentifiers()  false; not case insensitive and stored as given, maybe upper case but not forced to be
 whether database treats mixed case quoted SQL identifiers as case insensitive and stores them upper case 


**/
    public void Var001()
    {
        try {
	    assertCondition (dmd_.storesLowerCaseIdentifiers () == false,
				 "storesLowerCaseIdentifiers() returned true, but should return false because even though unquoted SQL identifiers are case insensitive they are stored in upper case -- changed 11/12/2007 by native driver");

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
storesLowerCaseIdentifiers() - Should return the correct value as
when the connection is closed.
**/
    public void Var002()
    {
        try {
	    assertCondition (dmd2_.storesLowerCaseIdentifiers () == false);

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
storesLowerCaseQuotedIdentifiers() - Should return the correct value as
when the connection is open.
**/
    public void Var003()
    {
        try {
            assertCondition (dmd_.storesLowerCaseQuotedIdentifiers () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
storesLowerCaseQuotedIdentifiers() - Should return the correct value as
when the connection is closed.
**/
    public void Var004()
    {
        try {
            assertCondition (dmd2_.storesLowerCaseQuotedIdentifiers () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
storesMixedCaseIdentifiers() - Should return the correct value as
when the connection is open.
**/
    public void Var005()
    {
        try {

	    
	    assertCondition (dmd_.storesMixedCaseIdentifiers () == false);

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
nstoresMixedCaseIdentifiers() - Should return the correct value as
when the connection is closed.
**/
    public void Var006()
    {
        try {

		
		assertCondition (dmd2_.storesMixedCaseIdentifiers () == false);


        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
storesMixedCaseQuotedIdentifiers() - Should return the correct value as
when the connection is open.
**/
    public void Var007()
    {
        try {


	    assertCondition (dmd_.storesMixedCaseQuotedIdentifiers () == false,
           "storesMixedCaseQuotedIdentifiers() returned true, but should return false because quoted SQL identifiers are case sensitive -- changed 12/31/2007 by native driver"); 


        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
storesMixedCaseQuotedIdentifiers() - Should return the correct value as
when the connection is closed.
**/
    public void Var008()
    {
        try {


	    assertCondition (dmd2_.storesMixedCaseQuotedIdentifiers () == false,
           "storesMixedCaseQuotedIdentifiers() returned true, but should return false because quoted SQL identifiers are case sensitive -- changed 12/31/2007 by native driver"); 

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
storesUpperCaseIdentifiers() - Should return the correct value as
when the connection is open.
**/
    public void Var009()
    {
        try {
            assertCondition (dmd_.storesUpperCaseIdentifiers () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
storesUpperCaseIdentifiers() - Should return the correct value as
when the connection is closed.
**/
    public void Var010()
    {
        try {
            assertCondition (dmd2_.storesUpperCaseIdentifiers () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
storesUpperCaseQuotedIdentifiers() - Should return the correct value as
when the connection is open.
**/
    public void Var011()
    {
        try {
            assertCondition (dmd_.storesUpperCaseQuotedIdentifiers () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
storesUpperCaseQuotedIdentifiers() - Should return the correct value as
when the connection is closed.
**/
    public void Var012()
    {
        try {
            assertCondition (dmd2_.storesUpperCaseQuotedIdentifiers () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



}



