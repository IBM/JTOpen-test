///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSConnectionFormat.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCPDSConnectionFormat.java
//
// Classes:      JDCPDSConnectionFormat
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
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;
import javax.sql.DataSource;


/**
Testcase JDCPDSConnectionFormat.  This tests the following
properties with respect to the JDBC Connection class:
The connection is obtained using DB2ConnectionPool class
<ul>
<li>decimal separator
<li>date format
<li>date separator
<li>time format
<li>time separator
</ul>
**/
public class JDCPDSConnectionFormat
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCPDSConnectionFormat";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCPDSTest.main(newArgs); 
   }



    // Private data.
    private static String table_      = JDCPDSTest.COLLECTION + ".JDCFORMAT";

    private String clearPassword_ = null;


/**
Constructor.
**/
    public JDCPDSConnectionFormat (AS400 systemObject,
                                   Hashtable<String,Vector<String>> namesAndVars,
                                   int runMode,
                                   FileOutputStream fileOutputStream,
                                   String password)
    {
        super (systemObject, "JDCPDSConnectionFormat",
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
        table_      = JDCPDSTest.COLLECTION + ".JDCFORMAT";
        if (isJdbc20StdExt()) {
            DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
            JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
	    try { 
		char[] password = PasswordVault.decryptPassword(encryptedPassword_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",password);
		Arrays.fill(password,'\0');
	    } catch (Exception e) {
	        clearPassword_ = PasswordVault.decryptPasswordLeak(encryptedPassword_);
	        if (clearPassword_ == null) {
	          output_.println("Warning... clearPassword_ is null!!!!");
	        }
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_); 
	    }

            // Create the table.
            connection_ = dataSource.getConnection ();
            Statement s = connection_.createStatement ();
            JDSetupCollection.create(connection_, JDCPDSTest.COLLECTION);
            s.executeUpdate ("CREATE TABLE " + table_
                             + " (COL1 DECIMAL(5,1), COL2 DATE, COL3 TIME)");
            s.close ();
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
            Statement s = connection_.createStatement ();
            s.executeUpdate ("DROP TABLE " + table_);
            s.close ();
        }
        connection_.close(); 
        connection_=null; 

    }


    void setPassword(DataSource dataSource) throws Exception {
      if (clearPassword_ == null) {
        char[] password = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(dataSource, "setPassword", password);
        Arrays.fill(password, '\0');
      } else {
        JDReflectionUtil.callMethod_V(dataSource, "setPassword", clearPassword_);
      }
    }

/**
decimal separator - Use the default for the decimal separator.
Insert a records using a "." and "," decimal separators.
The default should be what the server job is set to.  This
is a bit difficult to check, so we make sure exactly one of the
inserts succeeds.
**/
    public void Var001 ()
    {
	if (checkJdbc20StdExt()) {
	    try {
               DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                setPassword(dataSource); 
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();

                int inserts = 0;
                try {
                    s.executeUpdate ("INSERT INTO " + table_
                                     + " (COL1) VALUES (1.2)");
                    ++inserts;
                }
                catch (SQLException e) {
                    // Ignore.
                }
                try {
                    s.executeUpdate ("INSERT INTO " + table_
                                     + " (COL1) VALUES (1,2)");
                    ++inserts;
                }
                catch (SQLException e) {
                    // Ignore.
                }

                c.close ();
                assertCondition (inserts == 1);
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
decimal separator - Specify an invalid value for the decimal
separator.  This should not thow an exception but use what
the server job is set to.  Insert a records using a "." and
"," decimal separators.  Make sure exactly one of the inserts
succeeds.
**/
/* DB2GenericDataSource.java (this contains the code for DataSource
implementation) checks to make sure that the decimal separator that
is specified is a valid one. If it is not, a SQLException is thrown */

    public void Var002 ()
    {
        if (checkJdbc20StdExt()) {
            try {
              DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDecimalSeparator",
                   "bogus");
                failed("Did not throw exception");
                /*
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
    
                int inserts = 0;
                if(checkJdbc20StdExt()) { try {
                    s.executeUpdate ("INSERT INTO " + table_
                        + " (COL1) VALUES (1.2)");
                    ++inserts;
                }
                catch (SQLException e) {
                    // Ignore.
                }
                if(checkJdbc20StdExt()) { try {
                    s.executeUpdate ("INSERT INTO " + table_
                        + " (COL1) VALUES (1,2)");
                    ++inserts;
                }
                catch (SQLException e) {
                    // Ignore.
                }
    
                c.close ();
                assertCondition (inserts == 1);
                */
            }

            catch (Exception e) {
                assertExceptionIsInstanceOf(e,"java.sql.SQLException");
            }
        }
    }



/**
decimal separator - Specify "." for the decimal separator.
Insert a record using a "." decimal separator.  This should work.
**/
    public void Var003 ()
    {
        if (checkJdbc20StdExt()) {
            try {
              DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDecimalSeparator",".");
                Connection c = dataSource.getConnection ();
                
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL1) VALUES (1.2)");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
decimal separator - Specify "." for the decimal separator.
Insert a record using a "," decimal separator.  This should
throw an exception.
**/
    public void Var004 ()
    {
        if (checkJdbc20StdExt()) {
            try {
              DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDecimalSeparator",".");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL1) VALUES (1,2)");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
decimal separator - Specify "," for the decimal separator.
Insert a record using a "," decimal separator.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var005 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
              DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDecimalSeparator",",");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL1) VALUES (1,2)");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
decimal separator - Specify "," for the decimal separator.
Insert a record using a "." decimal separator.  This should
throw an exception.

SQL400 - for now the Native driver can't do this.
**/
    public void Var006 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
              DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDecimalSeparator",",");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL1) VALUES (1.2)");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
date format - Use the default for the date format.
This will depend on the server job, but iso should always
work.
**/
    public void Var007 ()
    {
        if (checkJdbc20StdExt()) {
            try {
              DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('1997-03-19')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }




/**
date format - Specify an invalid value for the date format.
The default will depend on the server job, but iso should always
work.
**/

/* DB2GenericDataSource.java (this contains the code for DataSource
implementation) checks to make sure that the date format that
is specified is a valid one. If it is not, a SQLException is thrown */

    public void Var008 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","bogus");
                failed("Did not throw exception");
                /*
                Connection c = dataSource.getConnection ();
                 Statement s = c.createStatement ();
                 s.executeUpdate ("INSERT INTO " + table_
                     + " (COL2) VALUES ('1997-03-19')");
                 c.close ();
                 succeeded ();
                 */
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e,"java.sql.SQLException");
            }
        }
    }



/**
date format - Specify "mdy" for the date format.
Insert a date in mdy format.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var009 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03-19-97')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
date format - Specify "mdy" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var010 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","/");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('97/100')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
date format - Specify "dmy" for the date format.
Insert a date in dmy format.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var011 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","dmy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('19-03-97')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
date format - Specify "dmy" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var012 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","dmy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","/");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('97/100')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
date format - Specify "ymd" for the date format.
Insert a date in dmy format.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var013 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","ymd");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('97-03-19')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }}



/**
date format - Specify "ymd" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var014 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","ymd");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","/");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('97/100')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }}



/**
date format - Specify "usa" for the date format.
Insert a date in usa format.  This should work.
**/
    public void Var015 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","usa");
                //JDReflectionUtil.callMethod_V(dataSource,"setDateSeprator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03/19/1997')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
date format - Specify "usa" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var016 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","usa");
                //JDReflectionUtil.callMethod_V(dataSource,"setDateSeprator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('97/100')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
date format - Specify "iso" for the date format.
Insert a date in usa format.  This should work.
**/
    public void Var017 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","iso");
                //JDReflectionUtil.callMethod_V(dataSource,"setDateSeprator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('1997-03-19')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }}



/**
date format - Specify "iso" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var018 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","iso");
                //JDReflectionUtil.callMethod_V(dataSource,"setDateSeprator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('97/100')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
date format - Specify "eur" for the date format.
Insert a date in eur format.  This should work.
**/
    public void Var019 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","eur");
                //JDReflectionUtil.callMethod_V(dataSource,"setDateSeprator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('19.03.1997')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
date format - Specify "eur" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var020 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","eur");
                //JDReflectionUtil.callMethod_V(dataSource,"setDateSeprator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('97/100')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
date format - Specify "jis" for the date format.
Insert a date in jis format.  This should work.
**/
    public void Var021 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","jis");
                //JDReflectionUtil.callMethod_V(dataSource,"setDateSeprator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('1997-03-19')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }}



/**
date format - Specify "jis" for the date format.
Insert a date in eur format.  This should throw an exception.
**/
    public void Var022 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","jis");
                //JDReflectionUtil.callMethod_V(dataSource,"setDateSeprator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('19-03-1997')");
                c.close ();
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
date format - Specify "julian" for the date format.
Insert a date in julian format.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var023 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","julian");
                //JDReflectionUtil.callMethod_V(dataSource,"setDateSeprator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('98/154')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
date format - Specify "julian" for the date format.
Insert a date in eur format.  This should throw an exception.
**/
    public void Var024 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","julian");
                //JDReflectionUtil.callMethod_V(dataSource,"setDateSeprator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('19-03-1997')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
date separator - Use the default for the date separator.
This will depend on the server job, but iso should always
work.
**/
    public void Var025 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('1997-03-19')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }




/**
date separator - Specify an invalid value for the date separator.
The default will depend on the server job, but iso should always
work.
**/
/* DB2GenericDataSource.java (this contains the code for DataSource
implementation) checks to make sure that the date separator that
is specified is a valid one. If it is not, a SQLException is thrown */

    public void Var026 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","bogus");
                failed("Did not throw exception");
                /*
                Connection c = dataSource.getConnection ();
                 Statement s = c.createStatement ();
                 s.executeUpdate ("INSERT INTO " + table_
                     + " (COL2) VALUES ('1997-03-19')");
                 c.close ();
                 succeeded ();
                 */
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e,"java.sql.SQLException");
            }
        }
    }



/**
date separator - Specify "/" for the date separator.
Insert a date using the "/" separator.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var027 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","/");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03/19/98')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
date separator - Specify "/" for the date separator.
Insert a date using the "-" separator.  This should throw
an exception.
**/
    public void Var028 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","/");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03-19-98')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
date separator - Specify "-" for the date separator.
Insert a date using the "-" separator.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var029 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03-19-98')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
date separator - Specify "-" for the date separator.
Insert a date using the "." separator.  This should throw
an exception.
**/
    public void Var030 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","-");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03.19.98')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
date separator - Specify "." for the date separator.
Insert a date using the "." separator.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var031 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator",".");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03.19.98')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
date separator - Specify "." for the date separator.
Insert a date using the "," separator.  This should throw
an exception.
**/
    public void Var032 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator",".");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03,19,98')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
date separator - Specify "," for the date separator.
Insert a date using the "," separator.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var033 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator",",");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03,19,98')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
date separator - Specify "," for the date separator.
Insert a date using the " " separator.  This should throw
an exception.
**/
    public void Var034 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator",",");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03 19 98')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
date separator - Specify "b" for the date separator.
Insert a date using the " " separator.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var035 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","b");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03 19 98')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
date separator - Specify "b" for the date separator.
Insert a date using the "/" separator.  This should throw
an exception.
**/
    public void Var036 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setDateFormat","mdy");
                JDReflectionUtil.callMethod_V(dataSource,"setDateSeparator","b");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL2) VALUES ('03/19/98')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
time format - Use the default for the time format.
This will depend on the server job, but iso should always
work.
**/
    public void Var037 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02:12:07')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }




/**
time format - Specify an invalid value for the time format.
The default will depend on the server job, but iso should always
work.
**/
/* DB2GenericDataSource.java (this contains the code for DataSource
implementation) checks to make sure that the time format that
is specified is a valid one. If it is not, a SQLException is thrown */

    public void Var038 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","bogus");
                failed("Did not throw exception");
                /*
                Connection c = dataSource.getConnection ();
                 Statement s = c.createStatement ();
                 s.executeUpdate ("INSERT INTO " + table_
                     + " (COL3) VALUES ('02:12:08')");
                 c.close ();
                 succeeded ();
                 */
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e,"java.sql.SQLException");
            }
        }
    }



/**
time format - Specify "hms" for the time format.
Insert a time in hms format.  This should work.
**/
    public void Var039 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","hms");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02:36:37')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
time format - Specify "hms" for the time format.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid formats.)  This should throw an
exception.
**/
    public void Var040 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","hms");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('21340')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
time format - Specify "usa" for the time format.
Insert a time in usa format.  This should work.
**/
    public void Var041 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","usa");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('2:15 PM')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
time format - Specify "usa" for the time format.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid formats.)  This should throw an
exception.
**/
    public void Var042 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","usa");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('021512')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
time format - Specify "iso" for the time format.
Insert a time in iso format.  This should work.
**/
    public void Var043 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","iso");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02:16:13')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
time format - Specify "iso" for the time format.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid formats.)  This should throw an
exception.
**/
    public void Var044 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","iso");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('216PM')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
time format - Specify "eur" for the time format.
Insert a time in eur format.  This should work.
**/
    public void Var045 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","eur");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02:17:15')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
time format - Specify "eur" for the time format.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid formats.)  This should throw an
exception.
**/
    public void Var046 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","eur");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('217PM')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
time format - Specify "jis" for the time format.
Insert a time in jis format.  This should work.
**/
    public void Var047 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","jis");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02:18:17')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
time format - Specify "jis" for the time format.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid formats.)  This should throw an
exception.
**/
    public void Var048 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","jis");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('218PM')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
time separator - Use the default for the time separator.
This will depend on the server job, but iso should always
work.
**/
    public void Var049 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02:20:23')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }




/**
time separator - Specify an invalid value for the time separator.
The default will depend on the server job, but iso should always
work.
**/
/* DB2GenericDataSource.java (this contains the code for DataSource
implementation) checks to make sure that the time separator that
is specified is a valid one. If it is not, a SQLException is thrown */
    public void Var050 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                //JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","jis");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator","bogus");
                failed("Did not throw exception");
                /*
                 Connection c = dataSource.getConnection ();
                 Statement s = c.createStatement ();
                 s.executeUpdate ("INSERT INTO " + table_
                     + " (COL3) VALUES ('02:20:23')");
                 c.close ();
                 succeeded ();
                 */
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e,"java.sql.SQLException");
            }
        }
    }



/**
time separator - Specify ":" for the time separator.
Insert a time using the ":" separator.  This should work.
**/
    public void Var051 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","hms");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",":");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02:22:25')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
time separator - Specify ":" for the time separator.
Insert a time using the " " separator.  This should throw
an exception.
**/
    public void Var052 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","hms");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",".");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02 22 26')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
time separator - Specify "." for the time separator.
Insert a time using the "." separator.  This should work.
**/
    public void Var053 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","hms");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",".");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02.22.27')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
time separator - Specify "." for the time separator.
Insert a time using the " " separator.  This should throw
an exception.
**/
    public void Var054 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","hms");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",".");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02 27 28')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
time separator - Specify "," for the time separator.
Insert a time using the "," separator.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var055 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","hms");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",",");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02,29,28')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
time separator - Specify "," for the time separator.
Insert a time using the " " separator.  This should throw
an exception.
**/
    public void Var056 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","hms");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator",",");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02 29 28')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
time separator - Specify "b" for the time separator.
Insert a time using the " " separator.  This should work.

SQL400 - for now the Native driver can't do this.
**/
    public void Var057 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable();
            return;
        }

        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                           setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","hms");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator","b");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('02 29 28')");
                c.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
time separator - Specify "b" for the time separator.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid separators.)  This should throw an
exception.
**/
    public void Var058 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                setPassword(dataSource); 
                JDReflectionUtil.callMethod_V(dataSource,"setTimeFormat","hms");
                JDReflectionUtil.callMethod_V(dataSource,"setTimeSeparator","b");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_
                                 + " (COL3) VALUES ('022932')");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



}

















