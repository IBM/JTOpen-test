///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSCPDSConnectionClose.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/* This testcase is based on JDConnectionClose.java */
//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDSCPDSConnectionClose.java
//
// Classes:      JDSCPDSConnectionClose
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.SCPDS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

import javax.sql.DataSource;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDSCPDSTest;
import test.JDSetupProcedure;
import test.JDSupportedFeatures;
import test.JDTestcase;
import test.PasswordVault;
import test.JD.JDSetupCollection;


/**
Testcase JDSCPDSConnectionClose.  his tests the following methods
of the JDBC Connection class:
The connection is obtained using the DB2StdConnectionPool class
<ul>
<li>close()</li>
<li>isClosed()</li>
</ul>
**/
public class JDSCPDSConnectionClose
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDSCPDSConnectionClose";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDSCPDSTest.main(newArgs); 
   }



    // Private data.
    private static String         table_      = JDSCPDSTest.COLLECTION + ".JDCCLOSE";
    private DataSource dataSource;


/**
Constructor.
**/
    public JDSCPDSConnectionClose (AS400 systemObject,
                                  Hashtable<String,Vector<String>> namesAndVars,
                                  int runMode,
                                  FileOutputStream fileOutputStream,
                                  
                                  String password)
    {
        super (systemObject, "JDSCPDSConnectionClose",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Setup.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        table_      = JDSCPDSTest.COLLECTION + ".JDCCLOSE";
        if (isJdbc20StdExt()) {
            // Create the table.
            dataSource =  (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdConnectionPool");
            JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
            JDReflectionUtil.callMethod_V(dataSource,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
            connection_ = dataSource.getConnection ();
            Statement s = connection_.createStatement ();
            JDSetupCollection.create(connection_, JDSCPDSTest.COLLECTION);
            s.executeUpdate ("CREATE TABLE " + table_ + " (COL1 INT)");
            s.close ();

            JDSupportedFeatures supportedFeatures_= new JDSupportedFeatures(this); 
                JDSetupProcedure.create (systemObject_, connection_, 
                                         JDSetupProcedure.STP_CSPARMS, supportedFeatures_, collection_);        

            connection_.close ();
            connection_ = null; 

        }
    }



/**
Cleanup.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20StdExt()) {
            // Drop the table.
            Connection c = dataSource.getConnection ();
            Statement s = c.createStatement ();
            s.executeUpdate ("DROP TABLE " + table_);
            s.close ();
            c.close ();
        }
    }



/**
Indicates if a result set is closed.
**/
    boolean isResultSetClosed (ResultSet rs)
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
    boolean isStatementClosed (Statement s)
    {
        // Try to get the concurrency.  An exception will be
        // thrown if the statement is closed.
        try {
            s.getResultSetConcurrency (); 
            return false;
        }
        catch (SQLException e) {
            return true;
        }
    }




/**
isClosed() - Should return false on an open connection.
**/
    public void Var001 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                Connection c = dataSource.getConnection ();
                boolean closed = c.isClosed ();
                c.close ();
                assertCondition (!closed);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
isClosed() - Should return true on a closed connection.
The way the connection pooling is implemented we are not going
to get true from c.isClosed() since the Connection object is
not actually closed, but rather sent to the pool. The spec is not
too exact on which approach to implement.
**/
    public void Var002 ()
    {
        notApplicable();
        /*
         if(checkJdbc20StdExt()) { try {
             Connection c = dataSource.getConnection ();
             c.close ();
             boolean closed = c.isClosed ();
             assertCondition (closed);
         }
         catch(Exception e) {
             failed(e, "Unexpected Exception");
         }
         */
    }



/**
close() - should have no effect when the connection is already closed.
**/
    public void Var003 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                Connection c = dataSource.getConnection ();
                c.close ();
                c.close ();
                assertCondition(true);
            }
            catch (Exception e) {
                failed(e, " Unexpected Exception");
            }
        }
    }




/**
close() - Should close all its statements, but not those of a different connection.
**/
    public void Var004 ()
    {
        notApplicable(); // see comments in var002 above
        /*
         if(checkJdbc20StdExt()) { try {
            Connection c = dataSource.getConnection ();
    
             Statement s1 = c.createStatement ();
             PreparedStatement s2 = c.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
             CallableStatement s3 = c.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
    
             Connection c2 = dataSource.getConnection ();
    
             Statement sX = c2.createStatement ();
    
             boolean s1Before = isStatementClosed ((DB2Statement)s1);
             boolean s2Before = isStatementClosed ((DB2Statement)s2);
             boolean s3Before = isStatementClosed ((DB2Statement)s3);
             boolean sXBefore = isStatementClosed ((DB2Statement)sX);
    
             c.close ();
    
             boolean s1After = isStatementClosed ((DB2Statement)s1);
             boolean s2After = isStatementClosed ((DB2Statement)s2);
             boolean s3After = isStatementClosed ((DB2Statement)s3);
             boolean sXAfter = isStatementClosed ((DB2Statement)sX);
    
             c2.close ();
    
             System.out.println(s1Before + " " + s2Before + " " + s3Before + " " + sXBefore
                     + " " + s1After + " " + s2After + " " + s3After  + " " + sXAfter);
             assertCondition (!s1Before && !s2Before && !s3Before && !sXBefore
                     && s1After && s2After && s3After && !sXAfter);
         }
         catch(Exception e) {
             failed(e, "Unexpected Exception");
         }
         */
    }



/**
close() - Should close all its result sets, but not those of a different connection.

Similar to var002, the way it is implemented, none of the isClosed() will return 
tru, since the physical connection is not closed but the connection being put
back in the pool
**/
    public void Var005 ()
    {
        notApplicable();
        /*
         if(checkJdbc20StdExt()) { try {
            Connection c = dataSource.getConnection ();
    
             Statement s1 = c.createStatement ();
             PreparedStatement s2 = c.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
    
             ResultSet rs1 = s1.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
             ResultSet rs2 = s2.executeQuery ();
    
             Connection c2 = dataSource.getConnection ();
    
             Statement sX = c2.createStatement ();
             ResultSet rsX = sX.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
    
             boolean rs1Before = isResultSetClosed (rs1);
             boolean rs2Before = isResultSetClosed (rs2);
             boolean rsXBefore = isResultSetClosed (rsX);
    
             c.close ();
    
             boolean rs1After = isResultSetClosed (rs1);
             boolean rs2After = isResultSetClosed (rs2);
             boolean rsXAfter = isResultSetClosed (rsX);
    
             c2.close ();
    
             System.out.println(rs1Before + " " + rs2Before + " " + rsXBefore
                     + " " + rs1After + " " + rs2After + " " + rsXAfter);
             assertCondition (!rs1Before && !rs2Before && !rsXBefore
                     && rs1After && rs2After && !rsXAfter);
         }
         catch(Exception e) {
             failed(e, "Unexpected Exception");
         }
         */
    }



/**
close() - Should rollback a transaction.
**/
    public void Var006 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                // need to have a Connection Event Listener
                // Refer to JAVA PTR 9900535
                notApplicable();
                /*
                // Add a row with another connection.
                Connection c = dataSource.getConnection ();
                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_ + " (COL1) VALUES (98)");

                // Count the number of rows before closing.
                int rowsBefore = 0;
                ResultSet rs = s.executeQuery ("SELECT * FROM " + table_);
                while (rs.next ())
                    ++rowsBefore;

                // Close the connection without committing.
                c.close ();

                // Count the number of rows after closing.
                Connection c2 = dataSource.getConnection ();
                Statement s2 = c2.createStatement ();
                int rowsAfter = 0;
                ResultSet rs2 = s2.executeQuery ("SELECT * FROM " + table_);
                while (rs2.next ())
                    ++rowsBefore;
                c2.close ();

                assertCondition ((rowsBefore == 1) && (rowsAfter == 0));
                */
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

}



    
