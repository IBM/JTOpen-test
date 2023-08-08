///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSWarnings.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSWarnings.  This tests the following methods
of the JDBC ResultSet class:

<ul>
<li>clearWarnings()
<li>getWarning()
</ul>
**/
public class JDRSWarnings
extends JDTestcase {



    // Private data.
    private             Statement       statement_;
    private             Statement       statement0_;


    private static String  select_ = "SELECT C_KEY,C_CHAR_50 FROM " + JDRSTest.RSTEST_GET;


/**
Constructor.
**/
    public JDRSWarnings (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDRSWarnings",
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
        select_ = "SELECT C_KEY,C_CHAR_50 FROM " + JDRSTest.RSTEST_GET;
        connection_ = testDriver_.getConnection (baseURL_ + ";data truncation=true",
                                                 userId_, encryptedPassword_);
        statement0_ = connection_.createStatement ();

        if (isJdbc20 ()) {
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);
            statement_.setMaxFieldSize (25);
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20 ())
            statement_.close ();
        statement0_.close ();
        connection_.close ();
    }



/**
Forces a single warning to be posted to the statement.

@param s The statement.

@exception Exception If an exception occurs.
**/
    public void forceWarning (ResultSet rs)
    throws Exception
    {
        // This should force the warning "data truncation".
        JDRSTest.position0 (rs, "CHAR_FLOAT");

        // Force a warning (data truncation).
        rs.getBigDecimal("C_CHAR_50", 0);
    }



/**
clearWarnings() - Has no effect when there are no
warnings.
**/
    public void Var001()
    {
        try {
            ResultSet rs = statement0_.executeQuery (select_);
            rs.clearWarnings ();
            SQLWarning w1 = rs.getWarnings ();
            rs.clearWarnings ();
            SQLWarning w2 = rs.getWarnings ();
            rs.close ();
            assertCondition ((w1 == null) && (w2 == null));
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
clearWarnings() - Clears warnings after 1 has been posted.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery (select_);
                forceWarning (rs);
                SQLWarning w1 = rs.getWarnings ();
                rs.clearWarnings ();
                SQLWarning w2 = rs.getWarnings ();
                rs.close ();
                assertCondition ((w1 != null) && (w2 == null));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
clearWarnings() - Clears warnings when a result set is
closed.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery (select_);
                rs.next ();
                forceWarning (rs);
                rs.close ();
                SQLWarning w1 = rs.getWarnings ();
                rs.clearWarnings ();
                SQLWarning w2 = rs.getWarnings ();
                assertCondition ((w1 != null) && (w2 == null));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
getWarning() - Returns null if no warnings have been
reported.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery (select_);
            rs.next ();
            rs.clearWarnings ();
            SQLWarning w = rs.getWarnings();
            rs.close ();
            assertCondition (w == null);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
getWarning() - Returns the first warning when 1 warning has been
reported.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery (select_);
                rs.next ();
                rs.clearWarnings ();
                forceWarning (rs);
                SQLWarning w1 = rs.getWarnings ();
                SQLWarning w2 = null;
                if (w1 != null) { 
                w2 = w1.getNextWarning ();
                }
                rs.close ();
                assertCondition ((w1 != null) && (w2 == null));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
getWarning() - Returns the first warning when 2 warnings have been
reported.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery (select_);
                rs.next ();
                forceWarning (rs);
                // Force another warning (data truncation).
                rs.getBigDecimal("C_CHAR_50", 0);
                SQLWarning w1 = rs.getWarnings ();
                SQLWarning w2 = w1.getNextWarning ();
                SQLWarning w3 = w2.getNextWarning ();
                rs.close ();
                assertCondition ((w1 != null) && (w2 != null) && (w3 == null));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }




/**
getWarning() - Returns the first warning even after the result set
is closed.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery (select_);
                rs.next ();
                forceWarning (rs);
                rs.close ();
                SQLWarning w = rs.getWarnings ();
                assertCondition (w != null);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
getWarning() - Return warnings resulting from a fetch operation.
  Before 8/2/2011, the toolbox did not return the warning.
**/
    public void Var008()
    {
	String info = " -- added 8/2/2011 to test issue 45931 warnings should be returned by fetch operation "; 
        if (checkJdbc20 ()) {
	    try {
		SQLWarning warnings = null; 
		String sql = "select 1/0 from sysibm.sqlcolumns fetch first 1000 rows only "; 
		ResultSet rs = statement_.executeQuery (sql);
		while ( rs.next () )  {

		    SQLWarning w = rs.getWarnings ();
		    if (w != null) {
			if (warnings == null) {
			    warnings = w; 
			} else {
			    warnings.setNextException(w); 
			} 
		    } 
		}
                rs.close ();

                assertCondition (warnings != null, "warning not reflected "+info);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception"+info);
            }
        }
    }




}



