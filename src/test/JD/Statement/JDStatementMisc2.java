///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementMisc2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDStatementMisc2.java
 //
 // Classes:      JDStatementMisc2
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDTestcase;


/**
Testcase JDStatementMisc2.  This tests the following method
of the JDBC Statement class:

<ul>
<li>getConnection()</li>
<li>getFetchDirection()</li>
<li>setFetchDirection()</li>
<li>getFetchSize()</li>
<li>setFetchSize()</li>
<li>getResultSetConcurrency()</li>
<li>getResultSetType()</li>
</ul>
**/
public class JDStatementMisc2
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementMisc2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }



    // Private data.




/**
Constructor.
**/
    public JDStatementMisc2 (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDStatementMisc2",
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
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
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
getConnection() - Returns the connection for this statement.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                Connection c = s.getConnection ();
                s.close ();
                assertCondition (c == connection_);
            }
            catch (SQLException e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getConnection() - Returns the connection for this statement
even when the statement is closed.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.close ();
                Connection c = s.getConnection ();
                assertCondition (c == connection_);
            }
            catch (SQLException e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getFetchDirection() - Returns the default fetch direction
when none is set.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                int fetchDirection = s.getFetchDirection ();
                s.close ();
                assertCondition (fetchDirection == ResultSet.FETCH_FORWARD);
            }
            catch (SQLException e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getFetchDirection()/setFetchDirection() - Returns the default
fetch direction when it has been set.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
                s.setFetchDirection (ResultSet.FETCH_UNKNOWN);
                int fetchDirection = s.getFetchDirection ();
                s.close ();
                assertCondition (fetchDirection == ResultSet.FETCH_UNKNOWN);
            }
            catch (SQLException e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getFetchDirection() - Throws an exception when called on a
closed statement.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.close ();
                int fetchDirection = s.getFetchDirection ();
                failed("Didn't throw SQLException"+fetchDirection);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFetchDirection() - Throws an exception when a bogus
fetch direction is passed.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
                s.setFetchDirection (238490);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFetchDirection() - Throws an exception when the result
set type is ResultSet.TYPE_FORWARD_ONLY and the fetch direction
is something other than ResultSet.FETCH_FORWARD.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.setFetchDirection (ResultSet.FETCH_REVERSE);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getFetchSize() - Returns the default fetch size
when none is set.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                int fetchSize = s.getFetchSize ();
                s.close ();
                assertCondition (fetchSize == 0);
            }
            catch (SQLException e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getFetchSize()/setFetchSize() - Returns the default
fetch direction when it has been set.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.setFetchSize (100);
                int fetchSize = s.getFetchSize ();
                s.close ();
                assertCondition (fetchSize == 100);
            }
            catch (SQLException e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getFetchSize() - Throws an exception when called on a
closed statement.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.close ();
                int fetchSize = s.getFetchSize ();
                failed("Didn't throw SQLException"+fetchSize);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFetchSize() - Throws an exception when a negative
fetch size is passed.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.setFetchSize (-90);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFetchSize() - Throws an exception when a fetch size
larger than max rows is passed.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.setMaxRows (50);
                s.setFetchSize (51);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getResultSetConcurrency() - Returns the default result set
concurrency when none is set.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                int resultSetConcurrency = s.getResultSetConcurrency ();
                s.close ();
                assertCondition (resultSetConcurrency == ResultSet.CONCUR_READ_ONLY);
            }
            catch (SQLException e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getResultSetConcurrency() - Returns the
result set concurrency when it has been set.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
                int resultSetConcurrency = s.getResultSetConcurrency ();
                s.close ();
                assertCondition (resultSetConcurrency == ResultSet.CONCUR_UPDATABLE);
            }
            catch (SQLException e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getResultSetConcurrency() - Throws an exception when called on a
closed statement.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.close ();
                int resultSetConcurrency = s.getResultSetConcurrency ();
                failed("Didn't throw SQLException"+resultSetConcurrency);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getResultSetType() - Returns the default result set
type when none is set.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                int resultSetType = s.getResultSetType ();
                s.close ();
                assertCondition (resultSetType == ResultSet.TYPE_FORWARD_ONLY);
            }
            catch (SQLException e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getResultSetType() - Returns the
result set type when it has been set.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
                int resultSetType = s.getResultSetType ();
                s.close ();
                assertCondition (resultSetType == ResultSet.TYPE_SCROLL_SENSITIVE);
            }
            catch (SQLException e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getResultSetType() - Throws an exception when called on a
closed statement.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.close ();
                int resultSetType = s.getResultSetType ();
                failed("Didn't throw SQLException"+resultSetType);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



}



