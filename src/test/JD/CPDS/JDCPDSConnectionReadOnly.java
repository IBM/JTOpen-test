///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSConnectionReadOnly.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCPDSConnectionReadOnly.java
//
// Classes:      JDCPDSConnectionReadOnly
//
////////////////////////////////////////////////////////////////////////

package test.JD.CPDS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.sql.*; 
import com.ibm.as400.access.AS400;

import test.JDCPDSTest;
import test.JDReflectionUtil;
import test.JDTestcase;
import test.PasswordVault;


/**
Testcase JDCPDSConnectionReadOnly.  This tests the following
methods of the JDBC Connection class:
The connection is obtained by using DB2ConnectionPool
<ul>
<li>access property
<li>isReadOnly()
<li>setReadOnly()
</ul>
**/
public class JDCPDSConnectionReadOnly
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCPDSConnectionReadOnly";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCPDSTest.main(newArgs); 
   }



    // Private data.
    private static String table_      = JDCPDSTest.COLLECTION + ".JDCACCESS";
    String clearPassword_; 


/**
Constructor.
**/
    public JDCPDSConnectionReadOnly (AS400 systemObject,
                                     Hashtable<String,Vector<String>> namesAndVars,
                                     int runMode,
                                     FileOutputStream fileOutputStream,
                                     
                                     String password)
    {
        super (systemObject, "JDCPDSConnectionReadOnly",
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
        table_      = JDCPDSTest.COLLECTION + ".JDCACCESS";
        if (isJdbc20StdExt()) {
            // Create the table.
	    clearPassword_ = PasswordVault.decryptPasswordLeak(encryptedPassword_);

            DataSource dataSource = (DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
            JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
            JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
            Connection c = dataSource.getConnection ();
            Statement s = c.createStatement ();
            s.executeUpdate ("CREATE TABLE " + table_
                             + " (NAME CHAR(10), ID INT)");
            s.close ();
            c.close ();
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20StdExt()) {
            // Drop the table.
            DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
            JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
            JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
            Connection c = dataSource.getConnection ();
            Statement s = c.createStatement ();
            s.executeUpdate ("DROP TABLE " + table_);
            s.close ();
            c.close ();
        }
    }



/**
Checks that a connection can query a table.

@param  c       The connection.
@return         true if the connection can query a table,
                false otherwise.
**/
    private boolean canRead (Connection c)
    throws Exception
    {
        boolean success = false;
        Statement s = c.createStatement ();
        try {
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            success = rs.next ();
            rs.close ();
            success = true;
        }
        catch (SQLException e) {
            // Ignore.
        }

        s.close ();
        return success;
    }



/**
Checks that a connection can update a table.

@param  c       The connection.
@return         true if the connection can update a table,
                false otherwise.
**/
    private boolean canWrite (Connection c)
    throws Exception
    {
        boolean success = false;
        Statement s = c.createStatement ();
        try {
            s.executeUpdate ("INSERT INTO " + table_
                             + " (NAME, ID) VALUES ('Josh', 123)");
            success = true;
        }
        catch (SQLException e) {
            // Ignore.
        }

        s.close ();
        return success;
    }



/**
Checks that a connection can call a program.

@param  c       The connection.
@return         true if the connection can call a program,
                false otherwise.
**/
    private boolean canCall (Connection c)
    throws Exception
    {
        boolean success = false;
        Statement s = c.createStatement ();
        try {
            s.execute ("CALL QSYS.QCMDEXC('DSPLIBL *PRINT', 0000000014.00000)");
            success = true;
        }
        catch (SQLException e) {
            // Ignore.
        }

        s.close ();
        return success;
    }



/**
access property - Uses default access.  Connection should be able to
read, write, and call.
**/
    public void Var001()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();
                boolean read = canRead (c);
                boolean write = canWrite (c);
                boolean call = canCall (c);
                c.close ();
                assertCondition (read && write && call);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
access property - Specifies invalid access.  Connection should be
able to read, write, and call.
**/
/* DB2GenericDataSource.java (this contains the code for DataSource
implementation) checks to make sure that the access that
is specified is a valid one. If it is not, a SQLException is thrown */
    public void Var002()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setAccess","bogus");
                failed("Did not throw exception");
                /*
                Connection c = dataSource.getConnection ();
                 boolean read = canRead (c);
                 boolean write = canWrite (c);
                 boolean call = canCall (c);
                 c.close ();
                 assertCondition (read && write && call);
                 */
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
access property - Specifies "all" access.  Connection should be
able to read, write, and call.
**/
    public void Var003()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setAccess","all"); 
                Connection c = dataSource.getConnection ();
                boolean read = canRead (c);
                boolean write = canWrite (c);
                boolean call = canCall (c);
                c.close ();
                assertCondition (read && write && call);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
access property - Specifies "read call" access.  Connection should be
able to read and call, but not write.
**/
    public void Var004()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setAccess","read call"); 
                Connection c = dataSource.getConnection ();
                boolean read = canRead (c);
                boolean write = canWrite (c);
                boolean call = canCall (c);
                c.close ();
                assertCondition (read && !write && call);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
access property - Specifies "read only" access.  Connection should be
able to read, but not write or call.
**/
    public void Var005()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setAccess","read only"); 
                Connection c = dataSource.getConnection ();
                boolean read = canRead (c);
                boolean write = canWrite (c);
                boolean call = canCall (c);
                c.close ();
                assertCondition (read && !write && !call);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
access property - Specifies weird case.  The property should
still be recognized.
**/
    public void Var006()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setAccess","read OnLy");
                Connection c = dataSource.getConnection ();
                boolean read = canRead (c);
                boolean write = canWrite (c);
                boolean call = canCall (c);
                c.close ();
                assertCondition (read && !write && !call);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e,"java.sql.SQLException");
            }
        }
    }



/**
isReadOnly() - Verify that the default is false when
no access property is set.
**/
    public void Var007()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();
                boolean ro = c.isReadOnly ();
                c.close ();
                assertCondition (ro == false);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
isReadOnly() - Verify that the default is false when
the access property is set to "all".
**/
    public void Var008()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setAccess","all"); 
                Connection c = dataSource.getConnection ();
                boolean ro = c.isReadOnly ();
                c.close ();
                assertCondition (ro == false);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
isReadOnly() - Verify that the default is true when
the access property is set to "read call".
**/
    public void Var009()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setAccess","read call"); 
                Connection c = dataSource.getConnection ();
                boolean ro = c.isReadOnly ();
                c.close ();
                assertCondition (ro == true);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
isReadOnly() - Verify that the default is true when
the access property is set to "read only".
**/
    public void Var010()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setAccess","read only"); 
                Connection c = dataSource.getConnection ();
                boolean ro = c.isReadOnly ();
                c.close ();
                assertCondition (ro == true);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
isReadOnly() - Verify that an exception is thrown when
the connection is closed.
**/

    public void Var011()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();
                c.close ();
                c.isReadOnly ();
                failed("Didn't throw Exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setReadOnly()/isReadOnly() - Verify that the value that was
set is then returned.
**/
    public void Var012()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                //JDReflectionUtil.callMethod_V(dataSource,"setAccess(DB2Properties.ACCESS_READ_ONLY);
                Connection c = dataSource.getConnection ();
                c.setReadOnly (true);
                boolean ro = c.isReadOnly ();
                c.close ();
                assertCondition (ro == true);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
setReadOnly()/isReadOnly() - Verify that the value that was
set is then returned.
**/
    public void Var013()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                //JDReflectionUtil.callMethod_V(dataSource,"setAccess(DB2Properties.ACCESS_READ_ONLY);
                Connection c = dataSource.getConnection ();
                c.setReadOnly (true);
                boolean ro = c.isReadOnly ();
                c.close ();
                assertCondition (ro == true);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
setReadOnly() - Verify that when set to false, we have all access.
**/
    public void Var014()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                //JDReflectionUtil.callMethod_V(dataSource,"setAccess(DB2Properties.ACCESS_READ_ONLY);
                Connection c = dataSource.getConnection ();
                c.setReadOnly (false);
                boolean read = canRead (c);
                boolean write = canWrite (c);
                boolean call = canCall (c);
                c.close ();
                assertCondition (read && write && call);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
setReadOnly() - Verify that when set to true, we only have
read and call access.
**/
    public void Var015()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                //JDReflectionUtil.callMethod_V(dataSource,"setAccess(DB2Properties.ACCESS_READ_ONLY);
                Connection c = dataSource.getConnection ();
                c.setReadOnly (true);
                boolean read = canRead (c);
                boolean write = canWrite (c);
                boolean call = canCall (c);
                c.close ();
                assertCondition (read && ! write && call);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
setReadOnly() - Verify that an exception is thrown when
the connection is closed.
**/

    public void Var016()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                //JDReflectionUtil.callMethod_V(dataSource,"setAccess(DB2Properties.ACCESS_READ_ONLY);
                Connection c = dataSource.getConnection ();
                c.close ();
                c.setReadOnly (true);
                failed("Didn't throw Exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setReadOnly() - Verify that an exception is thrown when
a transaction is active.
**/
    public void Var017()
    {           
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                //JDReflectionUtil.callMethod_V(dataSource,"setAccess(DB2Properties.ACCESS_READ_ONLY);
                Connection c = dataSource.getConnection ();
                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (NAME, ID) VALUES ('Jim', -754)");

                boolean success = false;
                try {
                    c.setReadOnly (true);
                }
                catch (SQLException e) {
                    success = true;
                }

                s.close ();
                c.close ();
                assertCondition (success);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
setReadOnly() - Verify that an exception is thrown when
a setting read only to false, but the access property is
set to "read only".
**/
    public void Var018()
    {           
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setAccess","read only"); 
                Connection c = dataSource.getConnection ();

                boolean success = false;
                try {
                    c.setReadOnly (false);
                }
                catch (SQLException e) {
                    success = true;
                }

                c.close ();
                assertCondition (success);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
setReadOnly() - Verify that an exception is thrown when
a setting read only to false, but the access property is
set to "read call".
**/
    public void Var019()
    {          
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setAccess","read call"); 
                Connection c = dataSource.getConnection ();

                boolean success = false;
                try {
                    c.setReadOnly (false);
                }
                catch (SQLException e) {
                    success = true;
                }

                c.close ();
                assertCondition (success);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



}













