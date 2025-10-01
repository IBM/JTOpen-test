///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementListener.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementListener.java
//
// Classes:      JDStatementListener
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.*;

import test.JDTestcase;
import test.JD.JDTestStatementHelper;

import java.sql.*;

import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;

/**
Testcase JDStatementListener.  This tests the following methods
of the JDBC Statement Listener class:

<ul>
<li>modifySQL()
<li>commentsStripped()
</ul>
**/
public class JDStatementListener
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementListener";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDSLTest.main(newArgs); 
   }



    // Private data.
    private              Connection     connection_;



/**
Constructor.
**/
    public JDStatementListener (AS400 systemObject,
                                Hashtable<String,Vector<String>> namesAndVars,
                                int runMode,
                                FileOutputStream fileOutputStream,
                                
                                String password)
    {
        super (systemObject, "JDStatementListener",
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
        connection_ = testDriver_.getConnection (baseURL_,
                                                 userId_, encryptedPassword_);
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
   * modifySQL() - Throw an exception to prevent statement from executing.
  **/
    public void Var001()
    {
        try {
            // This should work.
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
            rs.next();
            rs.next();
            int row = rs.getRow();
            rs.close();
            if (row != 2) {
                failed("Wrong row number: "+row);
                return;
            }

            // Now register a statement listener.
            AS400JDBCStatementListener listener = new AS400JDBCStatementListener()
            {
                public String modifySQL(Connection conn, String sql) throws SQLException
                {
                    throw new SQLException("denied!");
                }

                public void commentsStripped(Connection conn, String oldSQL, String newSQL) throws SQLException
                {
                }
            };
            JDTestStatementHelper.addListener(listener);

            try {
                rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                failed("Did not throw exception.");
                return;
            } catch (SQLException e) {
                if (e.toString().indexOf("denied!") < 0) {
                    failed(e, "Wrong exception text.");
                    return;
                }
            } finally {
                JDTestStatementHelper.removeListener(listener);
                rs.close();
            }
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * modifySQL() - Modify the statement being executed.
    **/
    public void Var002()
    {
        try {
            // This should work.
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
            rs.next();
            rs.next();
            int row = rs.getRow();
            rs.close();
            if (row != 2) {
                failed("Wrong row number: "+row);
                return;
            }

            // Now register a statement listener.
            AS400JDBCStatementListener listener = new AS400JDBCStatementListener()
            {
                public String modifySQL(Connection conn, String sql) throws SQLException
                {
                    return "SELECT * FROM QIWS.QCUSTCDT WHERE LSTNAM='Henning'";
                }

                public void commentsStripped(Connection conn, String oldSQL, String newSQL) throws SQLException
                {
                }
            };
            JDTestStatementHelper.addListener(listener);
            try {
                rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                boolean rowOne = rs.next();
                if (!rowOne) {
                    failed("Bad first row");
                    return;
                }
                boolean rowTwo = rs.next();
                if (rowTwo) {
                    failed("More than one row returned");
                    return;
                }
            } finally {
                JDTestStatementHelper.removeListener(listener);
                rs.close();
            }
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * commentsStripped() - Throw an exception to prevent statement from executing.
    **/
    public void Var003()
    {
        try {
            // This should work.
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
            rs.next();
            rs.next();
            int row = rs.getRow();
            rs.close();
            if (row != 2) {
                failed("Wrong row number: "+row);
                return;
            }

            // Now register a statement listener.
            AS400JDBCStatementListener listener = new AS400JDBCStatementListener()
            {
                public String modifySQL(Connection conn, String sql) throws SQLException
                {
                    return sql;
                }

                public void commentsStripped(Connection conn, String oldSQL, String newSQL) throws SQLException
                {
                    throw new SQLException("iwantcomments!");
                }
            };
            JDTestStatementHelper.addListener(listener);

            // This should also work, since comments don't get stripped unless the string is a certain length.
            rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT /* blah */");
            rs.close();

            // Now try it with a string length.
            try {
                StringBuffer queryString = new StringBuffer("SELECT * FROM QIWS.QCUSTCDT /*");
                for (int i=0; i<2098000; ++i) {
                    queryString.append("C");
                }
                queryString.append("*/");

                rs = s.executeQuery(queryString.toString());
                failed("Expected exception did not occur.");
            } catch (SQLException e) {
                if (e.toString().indexOf("iwantcomments!") < 0) {
                    failed(e, "Wrong exception text.");
                }
            } finally {
                JDTestStatementHelper.removeListener(listener);
                rs.close();
            }
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * modifySQL() - Verify the parms and let the execution continue.
    **/
    public void Var004()
    {
        try {
            // This should work.
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
            rs.next();
            rs.next();
            int row = rs.getRow();
            rs.close();
            if (row != 2) {
                failed("Wrong row number: "+row);
                return;
            }

            final StringBuffer queryString = new StringBuffer("SELECT * FROM QIWS.QCUSTCDT /*");
            for (int i=0; i<33000; ++i) {
                queryString.append("C");
            }
            queryString.append("*/");
            final String expectedQuery = "SELECT * FROM QIWS.QCUSTCDT "; // With the comments stripped off.

            // Now register a statement listener.
            AS400JDBCStatementListener listener = new AS400JDBCStatementListener()
            {
                public String modifySQL(Connection conn, String sql) throws SQLException
                {
                    if (conn != connection_) {
                        throw new SQLException("Bad connection");
                    }
                    if (!sql.equals(queryString.toString())) {
                        throw new SQLException("Bad SQL");
                    }
                    return sql;
                }

                public void commentsStripped(Connection conn, String oldSQL, String newSQL) throws SQLException
                {
                    if (conn != connection_) {
                        throw new SQLException("Bad connection");
                    }
                    if (!oldSQL.equals(queryString.toString())) {
                        throw new SQLException("Bad oldSQL");
                    }
                    if (!newSQL.equals(expectedQuery)) {
                        throw new SQLException("Bad newSQL");
                    }
                }
            };
            JDTestStatementHelper.addListener(listener);

            try {
                rs = s.executeQuery(queryString.toString());
            } finally {
                JDTestStatementHelper.removeListener(listener);
                rs.close();
            }
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }

}




