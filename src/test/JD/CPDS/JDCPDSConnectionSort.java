///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSConnectionSort.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCPDSConnectionSort.java
//
// Classes:      JDCPDSConnectionSort
//
////////////////////////////////////////////////////////////////////////

package test.JD.CPDS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.sql.*; 

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestcase;
import test.PasswordVault;


/**
Testcase JDCPDSConnectionSort.  This tests the following methods
of the JDBC Connection class:

The connection is obtained by using DB2ConnectionPool
<ul>
<li>"sort" property
<li>"sort language" property
<li>"sort table" property
<li>"sort weight" property
</ul>
**/
public class JDCPDSConnectionSort
extends JDTestcase {

    String clearPassword_ = null; 

/**
Constructor.
**/
    public JDCPDSConnectionSort (AS400 systemObject,
                                 Hashtable<String,Vector<String>> namesAndVars,
                                 int runMode,
                                 FileOutputStream fileOutputStream,
                                 
                                 String password)
    {
        super (systemObject, "JDCPDSConnectionSort",
               namesAndVars, runMode, fileOutputStream,
               password);


	clearPassword_ = PasswordVault.decryptPasswordLeak(encryptedPassword_);

    }



/**
Verifies that the server is sorting.

@exception Exception    If an exception occurs.
**/
    private boolean verifySort (Connection connection)
    throws Exception
    {
        Statement s = connection.createStatement ();       
        ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT ORDER BY LSTNAM");
        int count = 0;
        boolean check = true;
        String previous = null;
        while (rs.next ()) {
            ++count;
            String current = rs.getString ("LSTNAM");
            if (previous != null) {
                if (previous.compareTo (current) > 0)
                    check = false;
            }
            previous = current;
        }
        rs.close ();
        s.close ();
        return(check && (count > 5));
    }



/**
"sort" property - Specify an invalid value.

SQL400 - This property is not supported by the native driver today.
**/
    public void Var001()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "invalid");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);

                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @A1A
                // @A1D failed(e, "Unexpected Exception");
            }
        }
    }



/**
"sort" property - Specify "hex".

SQL400 - This property is not supported by the native driver today.
**/
    public void Var002()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "hex");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
"sort" property - Specify "job".

SQL400 - This property is not supported by the native driver today.
**/
    public void Var003()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "job");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
"sort" property - Specify "language" with no language specified.

SQL400 - This property is not supported by the native driver today.
**/
    public void Var004()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "language");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @A1A
                // @A1D failed(e, "Unexpected Exception");
            }
        }
    }



/**
"sort" property - Specify "language" with an invalid language specified.

SQL400 - This property is not supported by the native driver today.
**/
    public void Var005()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "language");
                properties.put ("sort language", "bad");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);

                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @A1A
                // @A1D failed(e, "Unexpected Exception");
            }
        }
    }



/**
"sort" property - Specify "language" with a valid language specified.

SQL400 - This property is not supported by the native driver today.
**/
    public void Var006()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "language");
                properties.put ("sort language", "enu");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
"sort" property - Specify "language" with an invalid weight.

SQL400 - This property is not supported by the native driver today.
**/
    public void Var007()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "language");
                properties.put ("sort language", "enu");
                properties.put ("sort weight", "invalid");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);

                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
"sort" property - Specify "language" with shared weight.

SQL400 - This property is not supported by the native driver today.
**/
    public void Var008()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "language");
                properties.put ("sort language", "enu");
                properties.put ("sort weight", "shared");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);

                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
"sort" property - Specify "language" with unique weight.

SQL400 - This property is not supported by the native driver today.
**/
    public void Var009()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "language");
                properties.put ("sort language", "enu");
                properties.put ("sort weight", "unique");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);

                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
"sort" property - Specify "table" with no table specified.

SQL400 - This property is not supported by the native driver today.
**/
    public void Var010()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "table");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);

                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @A1A
                // @A1D failed(e, "Unexpected Exception");
            }
        }
    }



/**
"sort" property - Specify "table" with an invalid table specified.

SQL400 - This property is not supported by the native driver today.
**/
    public void Var011()
    {
        if (checkJdbc20StdExt()) {
            try {
                Properties properties = new Properties ();
                properties.put ("user", userId_);
                properties.put ("password", clearPassword_);
                properties.put ("sort", "table");
                properties.put ("sort table", "qsys/bogus");

                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);

                Connection c = dataSource.getConnection ();

                boolean check = verifySort (c);
                c.close ();
                assertCondition (check);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @A1A
                // @A1D failed(e, "Unexpected Exception");
            }
        }
    }


/**
"sort" property - Specify "table" with a valid uppercase slash-separated table specified.
**/
    public void Var012()
    {
        if (checkJdbc20StdExt()) {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", clearPassword_);
            properties.put ("sort", "table");
            properties.put ("sort table", "QSYS/QTOCSSTBL");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
        }
    }


/**
"sort" property - Specify "table" with a valid uppercase dot-separated table specified.
**/
    public void Var013()
    {
        if (checkJdbc20StdExt()) {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", clearPassword_);
            properties.put ("sort", "table");
            properties.put ("sort table", "QSYS.QTOCSSTBL");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
        }
    }


/**
"sort" property - Specify "table" with a valid lowercase table specified.
**/
    public void Var014()
    {
        if (checkJdbc20StdExt()) {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", clearPassword_);
            properties.put ("sort", "table");
            properties.put ("sort table", "qsys/qtocsstbl");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
        }
    }
}



