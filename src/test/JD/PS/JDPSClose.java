///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSClose.java
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
 // File Name:    JDPSClose.java
 //
 // Classes:      JDPSClose
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDTestcase;



/**
Testcase JDPSClose.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>close()</li>
</ul>
**/
public class JDPSClose
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSClose";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Private data.
    private Connection  connection_;



/**
Constructor.
**/
    public JDPSClose (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSClose",
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
    private boolean isStatementClosed (PreparedStatement ps)
    {
        // Try to run a query.  An exception will be
        // thrown if the statement is closed.
        try {
            ps.executeQuery ();
            return false;
        }
        catch (SQLException e) {
            return true;
        }
    }



/**
close() - Should close an open prepared statement that has not been executed.
**/
    public void Var001 ()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            boolean sBefore = isStatementClosed (ps);
            ps.close ();
            boolean sAfter = isStatementClosed (ps);
            assertCondition (!sBefore && sAfter);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
close() - Should close an open prepared statement that has been executed.
**/
    public void Var002 ()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            ps.executeQuery ();
            boolean sBefore = isStatementClosed (ps);
            ps.close ();
            boolean sAfter = isStatementClosed (ps);
            assertCondition (!sBefore && sAfter);
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
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            ps.close ();
            boolean sBefore = isStatementClosed (ps);
            ps.close ();
            boolean sAfter = isStatementClosed (ps);
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
        try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");

            ResultSet rs1 = ps.executeQuery ();

            PreparedStatement psX = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");

            ResultSet rsX = psX.executeQuery ();

            boolean rs1Before = isResultSetClosed (rs1);
            boolean rsXBefore = isResultSetClosed (rsX);

            ps.close ();

            boolean rs1After = isResultSetClosed (rs1);
            boolean rsXAfter = isResultSetClosed (rsX);

            psX.close ();

            assertCondition (!rs1Before && !rsXBefore
                    && rs1After && !rsXAfter);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



}



