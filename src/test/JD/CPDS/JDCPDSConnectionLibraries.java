///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSConnectionLibraries.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCPDSConnectionLibraries.java
//
// Classes:      JDCPDSConnectionLibraries
//
////////////////////////////////////////////////////////////////////////

package test.JD.CPDS;

import com.ibm.as400.access.AS400;

import test.JDCPDSTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;
import test.JD.JDSetupCollection;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.sql.*;
import javax.sql.*;



/**
Testcase JDCPDSConnectionLibraries.  This tests the following
properties with respect to the JDBC Connection class:

<ul>
<li>default schema (specified in URL)
<li>libraries
</ul>
**/
public class JDCPDSConnectionLibraries
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCPDSConnectionLibraries";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCPDSTest.main(newArgs); 
   }



    // Private data.
    public static  String COLLECTION2    = "JDTESTCON2";
    String clearPassword_; 

/**
Constructor.
**/
    public JDCPDSConnectionLibraries (AS400 systemObject,
                                      Hashtable<String,Vector<String>> namesAndVars,
                                      int runMode,
                                      FileOutputStream fileOutputStream,
                                      
                                      String password)
    {
        super (systemObject, "JDCPDSConnectionLibraries",
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
        if (isJdbc20StdExt()) {
	    clearPassword_ = PasswordVault.decryptPasswordLeak(encryptedPassword_);

            DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
            JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
            JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
            //dataSource.setDecimalSeparator("bogus");
            Connection c = dataSource.getConnection ();

            if(collection_ != null) {
                COLLECTION2 = collection_;
            }
            JDSetupCollection.create (systemObject_,  c,
                                      COLLECTION2);
            c.close ();
        }
    }



/**
Checks that a schema is in the library list.

@param  c       The connection.
@param  schema  The schema.
@return         true if the schema is in the library list,
                false otherwise.
**/
    private boolean checkLibraryList (Connection c, String schema)
    throws Exception
    {
        DatabaseMetaData dmd = c.getMetaData ();
        ResultSet rs = dmd.getSchemas ();
        boolean found = false;
        while (rs.next ()) {
            String schema2 = rs.getString ("TABLE_SCHEM");
            if (schema2.equalsIgnoreCase (schema))
                found = true;
        }
        return found;
    }



/**
Checks that a schema is the default schema.

@param  c       The connection.
@param  schema  The schema.
@return         true if the schema is the default schema,
                false otherwise.

SQL400 - The native JDBC driver gets different error messages from
         the Toolbox and therefore must examine the error messages
         for different values.
**/
    private String getDefaultSchema (Connection c)
    throws Exception
    {
        String defaultSchema = "";

        Statement s = c.createStatement ();
        try {
            s.executeQuery ("SELECT * FROM NOT_EXIST");
        }
        catch (SQLException e) {
            String message = e.getMessage ();
            String token = "";
            int word;
            if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
                // There are 2 possible messages that you can get natively...
                // 1) <lib> in QSYS type *LIB not found.  -> library doesn't exist
                // 2) <file> in <lib> type *file not found     -> file doesn't exist, library does

                // Tokenize the message on word boundaries, ignore spaces.
                StringTokenizer tokenizer = new StringTokenizer (message, " ", false);

                // There should be at least 3 words in the message.
                if (tokenizer.countTokens () < 5)
                    return null;

                // Get words 1, 3, and 5 from the tokenizer.
                String word1 = tokenizer.nextToken();
                tokenizer.nextToken();
                String word3 = tokenizer.nextToken();
                tokenizer.nextToken();
                String word5 = tokenizer.nextToken();

                if (word5.equals("*LIB"))
                    return(word1);
                else
                    return(word3);
            }
            else {
                StringTokenizer tokenizer = new StringTokenizer (message);
                for (word = 0; tokenizer.hasMoreTokens () && word < 4;token = tokenizer.nextToken (), ++word);
                {
                    if (word == 4) {
                        if (! token.equals ("*LIBL"))
                            defaultSchema = token;
                    }
                }
            }
        }

        return defaultSchema;
    }



/**
default schema - No default schema is specified in the URL,
no libraries are specified, and sql naming.  Should use the schema with
the same name as the userId as the default.
**/
    public void Var001()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setNamingOption","sql");
                Connection c = dataSource.getConnection ();
                String defaultSchema = getDefaultSchema (c);
                c.close ();
                assertCondition (defaultSchema.equalsIgnoreCase (userId_));
            }
            catch (Exception e) {
                failed(e, "Unexpected exception");
            }
        }
    }



/**
default schema - No default schema is specified in the URL,
no libraries are specified, and system naming.  Should have no default.

SQL400 - for the native driver, the value *libl is going to come back from
         the exception string that gets parsed.  The effect should be the
         same.  You are using AS/400 system naming without specifying a 
         default library.  Therefore, you should expect the library list
         to be checked.
**/

    public void Var002()
    {
        if (checkJdbc20StdExt()) {
            try {
              DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setNamingOption","system");
                Connection c = dataSource.getConnection ();
                String defaultSchema = getDefaultSchema (c);
                c.close ();
                System.out.println("Default schema is " + defaultSchema);
                if (getDriver() == JDTestDriver.DRIVER_NATIVE)
                    assertCondition (defaultSchema.equalsIgnoreCase ("*LIBL"));
                else
                    assertCondition (defaultSchema.equalsIgnoreCase (""));
            }
            catch (Exception e) {
                failed(e, "Unexpected exception");
            }
        }
    }





/**
libraries - No default schema is specified in the URL,
no libraries are specified.  The schema with the same name
as the userId should be in the library list.
**/
    public void Var003()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();
                boolean success = checkLibraryList (c, userId_);
                c.close ();
                assertCondition (success);
            }
            catch (Exception e) {
                failed(e, "Unexpected exception");
            }
        }
    }


/**
libraries - No default schema is specified in
the URL, but libraries are specified (delimited by spaces).
Those libraries should be in the library list.
**/
    public void Var004()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setLibraries",JDCPDSTest.COLLECTION
                                        + " " + COLLECTION2);
                Connection c = dataSource.getConnection ();
                boolean successA = checkLibraryList (c, JDCPDSTest.COLLECTION);
                boolean successB = checkLibraryList (c, COLLECTION2);
                c.close ();
                assertCondition (successA && successB);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
libraries - No default schema is specified in
the URL, but libraries are specified (delimited by commas).
Those libraries should be in the library list.
**/
    public void Var005()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setLibraries",JDCPDSTest.COLLECTION
                                        + "," + COLLECTION2);
                Connection c = dataSource.getConnection ();
                boolean successA = checkLibraryList (c, JDCPDSTest.COLLECTION);
                boolean successB = checkLibraryList (c, COLLECTION2);
                c.close ();
                assertCondition (successA && successB);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }


/**
libraries - Specifies a bad library in the library list.
Should throw an exception.

SQL400 - the Native JDBC driver will not validate that the library
passed in is a valid library that currently exists on the system.
To do so is error prone due to the fact that DRDA has no mechanism
to check and return information about a default library at connection
time.  The native driver will only check that the length of the string
passed in is valid.
**/
    public void Var006()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setLibraries","BADLIBRARY");
                Connection c = dataSource.getConnection ();
                if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
                    assertCondition(true);
                    c.close();
                }
                else
                    failed ("Did not throw exception.");
            }
            catch (Exception e) {
                if (getDriver() == JDTestDriver.DRIVER_NATIVE)
                    failed(e, "Unexpected exception");
                else
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



}












