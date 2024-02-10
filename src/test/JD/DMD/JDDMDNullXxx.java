///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDNullXxx.java
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
<li>nullPlusNonNullIsNull()
<li>nullsAreSortedAtEnd()
<li>nullsAreSortedAtStart()
<li>nullsAreSortedHigh()
<li>nullsAreSortedLow()
</ul>
**/
public class JDDMDNullXxx
extends JDTestcase
{


    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;



/**
Constructor.
**/
    public JDDMDNullXxx (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDNullXxx",
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
nullPlusNonNullIsNull() - Should return the correct value as
when the connection is open.
**/
    public void Var001()
    {
        try {
            assertCondition (dmd_.nullPlusNonNullIsNull () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
nullPlusNonNullIsNull() - Should return the correct value as
when the connection is closed.
**/
    public void Var002()
    {
        try {
            assertCondition (dmd2_.nullPlusNonNullIsNull () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
nullsAreSortedAtEnd() - Should return the correct value as
when the connection is open.
**/
    public void Var003()
    {
        try {
            assertCondition (dmd_.nullsAreSortedAtEnd () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
nullsAreSortedAtEnd() - Should return the correct value as
when the connection is closed.
**/
    public void Var004()
    {
        try {
            assertCondition (dmd2_.nullsAreSortedAtEnd () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
nullsAreSortedAtStart() - Should return the correct value as
when the connection is open.
**/
    public void Var005()
    {
        try {
            assertCondition (dmd_.nullsAreSortedAtStart () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
nullsAreSortedAtStart() - Should return the correct value as
when the connection is closed.
**/
    public void Var006()
    {
        try {
            assertCondition (dmd2_.nullsAreSortedAtStart () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
nullsAreSortedHigh() - Should return the correct value as
when the connection is open.
**/
    public void Var007()
    {
        try {
            assertCondition (dmd_.nullsAreSortedHigh () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
nullsAreSortedHigh() - Should return the correct value as
when the connection is closed.
**/
    public void Var008()
    {
        try {
            assertCondition (dmd2_.nullsAreSortedHigh () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
nullsAreSortedLow() - Should return the correct value as
when the connection is open.
**/
    public void Var009()
    {
        try {
            assertCondition (dmd_.nullsAreSortedLow () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
nullsAreSortedLow() - Should return the correct value as
when the connection is closed.
**/
    public void Var010()
    {
        try {
            assertCondition (dmd2_.nullsAreSortedLow () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



}



