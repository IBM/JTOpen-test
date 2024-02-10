///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementClose.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDStatementClose.java
 //
 // Classes:      JDStatementClose
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDStatementClose.  This tests the following method
of the JDBC Statement class:

<ul>
<li>close()</li>
</ul>
**/
public class JDStatementClose
extends JDTestcase
{



    // Private data.
    private Connection  connection_;



/**
Constructor.
**/
    public JDStatementClose (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDStatementClose",
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
        // Initialize private data.
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
    }



/**
Indicates if a result set is closed.
**/
    private boolean isResultSetClosed (ResultSet rs)
    {
        // Try to get the value of the first column of the
        // next row.  An exception will be thrown if the
        // result set is closed.
        try {
            rs.next ();
            rs.getString (1);
            return false;
        }
        catch (SQLException e) {
            return true;
        }
    }



/**
Indicates if a statement is closed.
**/
    private boolean isStatementClosed (Statement s)
    {
        // Try to run a query.  An exception will be
        // thrown if the statement is closed.
        try {
            s.executeQuery ("SELECT * FROM QSYS2.SYSPROCS");
            return false;
        }
        catch (SQLException e) {
            return true;
        }
    }



/**
close() - Should close an open statement.
**/
    public void Var001 ()
    {
        try {
            Statement s = connection_.createStatement ();
            boolean sBefore = isStatementClosed (s);
            s.close ();
            boolean sAfter = isStatementClosed (s);
            assertCondition (!sBefore && sAfter);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
close() - Should close an open statement after a bogus
query.  (This variation is here because of a bug I was
witnessing on occasion.)
**/
    public void Var002 ()
    {
        try {
            Statement s = connection_.createStatement ();
            try {
                s.executeQuery ("SELE * FROM BOGUS");
            }
            catch (SQLException e) {
                // Ignore.
            }
            s.close ();
            boolean sAfter = isStatementClosed (s);
            assertCondition (sAfter);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
close() - Should have no effect when the statement is
already closed.
**/
    public void Var003 ()
    {
        try {
            Statement s = connection_.createStatement ();
            s.close ();
            boolean sBefore = isStatementClosed (s);
            s.close ();
            boolean sAfter = isStatementClosed (s);
            assertCondition (sBefore && sAfter);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
close() - Should close its result set, but not those of
different statements.
**/
    public void Var004 ()
    {
	StringBuffer sb = new StringBuffer("Should close its result set, but not those of different statements"); 
        try {
            Statement s = connection_.createStatement ();

            ResultSet rs1 = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");

            Statement sX = connection_.createStatement ();

            ResultSet rsX = sX.executeQuery ("SELECT * FROM QSYS2.SYSPROCS");
	    sb.append("Checking rs1\n"); 
            boolean rs1Before = isResultSetClosed (rs1);
	    sb.append("Checking rsX\n"); 
            boolean rsXBefore = isResultSetClosed (rsX);

	    sb.append("Closing statement for rs1\n"); 
            s.close ();

            boolean rs1After = isResultSetClosed (rs1);
	    sb.append("Checked rs1 closed = "+rs1After +"\n"); 
            boolean rsXAfter = isResultSetClosed (rsX);
	    sb.append("Checked rsX closed = "+rsXAfter +"\n"); 

	    sb.append("Closing statement for rsX\n"); 

            sX.close ();

            assertCondition (!rs1Before && !rsXBefore
			     && rs1After && !rsXAfter,
			     "\nrs1Before="+rs1Before+" sb false "+
			     "\nrsXBefore="+rsXBefore+" sb false "+
			     "\nrs1After="+rs1After+" sb true "+
			     "\nrsXAfter="+rsXAfter+" sb false \n"+sb.toString());
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception\n"+sb.toString());
        }
    }



}



