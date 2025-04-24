///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetColumnDisplaySize.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDRSMDGetColumnDisplaySize.java
//
// Classes:      JDRSMDGetColumnDisplaySize
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;

import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDRSMDGetColumnDisplaySize.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getColumnDisplaySize()
</ul>
**/
public class JDRSMDGetColumnDisplaySize
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDGetColumnDisplaySize";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }



    // Constants.
    private static final String PACKAGE             = "JDRSMDGCDS";
    private static final String PACKAGE_CACHE_NO    = "extended dynamic=false";
    private static String PACKAGE_CACHE_YES   = "extended dynamic=true;package="
                                                    + PACKAGE + ";package library="
                                                    + JDRSMDTest.COLLECTION
                                                    + ";package cache=true";

    // Private data.
    private String              properties_     = "";
    private ResultSet           rs_             = null;
    private ResultSetMetaData   rsmd_           = null;
    private Statement           statement_      = null;



/**
Constructor.
**/
    public JDRSMDGetColumnDisplaySize (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDGetColumnDisplaySize",
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

        if (isToolboxDriver())
        {
            JDSetupPackage.prime (systemObject_, PACKAGE,
                JDRSMDTest.COLLECTION, "SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
        }
	else if (getDriver () == JDTestDriver.DRIVER_NATIVE)
        {
            JDSetupPackage.prime (systemObject_, encryptedPassword_, PACKAGE,
                JDRSMDTest.COLLECTION, "SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET, "", JDTestDriver.DRIVER_NATIVE);
        }
	else
	{
	    // Not package setup for other JDBC drivers. 
	}

        PACKAGE_CACHE_YES   = "extended dynamic=true;package="
          + PACKAGE + ";package library="
          + JDRSMDTest.COLLECTION
          + ";package cache=true";
        reconnect (PACKAGE_CACHE_NO);
    }



/**
Reconnects with different properties, if needed.

@exception Exception If an exception occurs.
**/
    private void reconnect (String properties)
        throws Exception
    {
        if (! properties_.equals (properties)) {
            properties_ = properties;
            if (connection_ != null)
                cleanup ();

            // SQL400 - driver neutral...
            String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
                
                ;
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            statement_ = connection_.createStatement ();
            rs_ = statement_.executeQuery ("SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
            rsmd_ = rs_.getMetaData ();
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        rs_.close ();
        statement_.close ();
        connection_.close ();
    }



/**
getColumnDisplaySize() - Check column -1.  Should throw an exception.
(Package cache = false)
**/
    public void Var001()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getColumnDisplaySize (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnDisplaySize() - Check column 0.  Should throw an exception.
(Package cache = false)
**/
    public void Var002()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getColumnDisplaySize (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnDisplaySize() - Check a column greater than the max.
Should throw an exception. (Package cache = false)
**/
    public void Var003()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getColumnDisplaySize (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnDisplaySize() - Check when the result set is closed.
**/
    public void Var004()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            int v = rsmd.getColumnDisplaySize (rs_.findColumn ("C_SMALLINT"));
            s.close ();
            assertCondition (v == 6);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check when the meta data comes from a prepared
statement.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("SELECT C_SMALLINT FROM "
                    + JDRSMDTest.RSMDTEST_GET);
                ResultSetMetaData rsmd = ps.getMetaData ();
                int v = rsmd.getColumnDisplaySize (1);
                ps.close ();
                assertCondition (v == 6);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check a SMALLINT column. (Package cache = false)
**/
    public void Var006()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_SMALLINT"));
            assertCondition (s == 6);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check an INTEGER column. (Package cache = false)
**/
    public void Var007()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_INTEGER"));
            assertCondition (s == 11);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a REAL column. (Package cache = false)
**/
    public void Var008()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_REAL"));
            assertCondition (s == 13, "Got "+s+" expected 13 for real");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a FLOAT column. (Package cache = false)
**/
    public void Var009()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_FLOAT"));
            assertCondition (s == 22);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a FLOAT(3) column. (Package cache = false)
**/
    public void Var010()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_FLOAT_3"));
            assertCondition (s == 13, "Got "+s+" expected 13 for real"); // Treated as REAL.
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a DOUBLE column. (Package cache = false)
**/
    public void Var011()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DOUBLE"));
            assertCondition (s == 22);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a DECIMAL(5,0) column. (Package cache = false)
**/
    public void Var012()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DECIMAL_50"));
            assertCondition (s == 7);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a DECIMAL(10,5) column. (Package cache = false)
**/
    public void Var013()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (s == 12);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a NUMERIC(5,0) column. (Package cache = false)
**/
    public void Var014()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_NUMERIC_50"));
            assertCondition (s == 7);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a NUMERIC(10,5) column. (Package cache = false)
**/
    public void Var015()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (s == 12);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a CHAR column. (Package cache = false)
**/
    public void Var016()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_CHAR_1"));
            assertCondition (s == 1);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a CHAR(50) column. (Package cache = false)
**/
    public void Var017()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_CHAR_50"));
            assertCondition (s == 50);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a VARCHAR(50) column. (Package cache = false)
**/
    public void Var018()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s == 50);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a BINARY column. (Package cache = false)
**/
    public void Var019()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_BINARY_1"));
            assertCondition (s == 1, "Got "+s+" expected 1 for BINARY_1");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a BINARY(20) column. (Package cache = false)
**/
    public void Var020()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_BINARY_20"));
            assertCondition (s == 20, "Got "+s+" expected 20 for binary 20");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a VARBINARY(20) column. (Package cache = false)
**/
    public void Var021()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (s == 20, "Got "+s+" expected 20 for varbinary 20");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a DATE column. (Package cache = false)
**/
    public void Var022()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DATE"));
            assertCondition (s == 10);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a TIME column. (Package cache = false)
**/
    public void Var023()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_TIME"));
            assertCondition (s == 8);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a TIMESTAMP column. (Package cache = false)
**/
    public void Var024()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (s == 26);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a BLOB column. (Package cache = false)
**/
    public void Var025()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_BLOB"));
                assertCondition (s == 1048576);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check a CLOB column. (Package cache = false)
**/
    public void Var026()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_CLOB"));
		assertCondition (s == 1048576,"s = "+s+" SB 1048576");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check a DBCLOB column. (Package cache = false)
**/
    public void Var027()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DBCLOB"));
                assertCondition (s == 524288, "Got "+s+" expected 524288 for dbclob");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check a DATALINK column. (Package cache = false)
**/
    public void Var028()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DATALINK"));
                assertCondition (s == 200);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check a DISTINCT column. (Package cache = false)
**/
    public void Var029()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DISTINCT"));
                assertCondition (s == 12);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



// $B0A
/**
getColumnDisplaySize() - Check a BIGINT column. (Package cache = false)
**/
    public void Var030()
    {
        if (checkBigintSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_BIGINT"));
                assertCondition (s == 20);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check column -1.  Should throw an exception.
(Package cache = true)
**/
    public void Var031()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getColumnDisplaySize (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnDisplaySize() - Check column 0.  Should throw an exception.
(Package cache = true)
**/
    public void Var032()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getColumnDisplaySize (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnDisplaySize() - Check a column greater than the max.
Should throw an exception. (Package cache = true)
**/
    public void Var033()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getColumnDisplaySize (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnDisplaySize() - Check a SMALLINT column. (Package cache = true)
**/
    public void Var034()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_SMALLINT"));
            assertCondition (s == 6);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check an INTEGER column. (Package cache = true)
**/
    public void Var035()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_INTEGER"));
            assertCondition (s == 11);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a REAL column. (Package cache = true)
**/
    public void Var036()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_REAL"));
            assertCondition (s == 13, "Got "+s+" expected 13 for real");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a FLOAT column. (Package cache = true)
**/
    public void Var037()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_FLOAT"));
            assertCondition (s == 22);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a FLOAT(3) column. (Package cache = true)
**/
    public void Var038()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_FLOAT_3"));
            assertCondition (s == 13, "Got "+s+" expected 13 for real"); // Treated as REAL.
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a DOUBLE column. (Package cache = true)
**/
    public void Var039()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DOUBLE"));
            assertCondition (s == 22);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a DECIMAL(5,0) column. (Package cache = true)
**/
    public void Var040()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DECIMAL_50"));
            assertCondition (s == 7);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a DECIMAL(10,5) column. (Package cache = true)
**/
    public void Var041()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (s == 12);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a NUMERIC(5,0) column. (Package cache = true)
**/
    public void Var042()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_NUMERIC_50"));
            assertCondition (s == 7);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a NUMERIC(10,5) column. (Package cache = true)
**/
    public void Var043()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (s == 12);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a CHAR column. (Package cache = true)
**/
    public void Var044()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_CHAR_1"));
            assertCondition (s == 1);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a CHAR(50) column. (Package cache = true)
**/
    public void Var045()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_CHAR_50"));
            assertCondition (s == 50);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a VARCHAR(50) column. (Package cache = true)
**/
    public void Var046()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s == 50);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a BINARY column. (Package cache = true)
**/
    public void Var047()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_BINARY_1"));
            assertCondition (s == 1, "Got "+s+" expected 1 for binary(1)");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a BINARY(20) column. (Package cache = true)
**/
    public void Var048()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_BINARY_20"));
            assertCondition (s == 20, "Got "+s+" expected 20 for binary(20)");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a VARBINARY(20) column. (Package cache = true)
**/
    public void Var049()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (s == 20, "Got "+s+" expected 20 for varbinary(1)");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a DATE column. (Package cache = true)
**/
    public void Var050()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DATE"));
            assertCondition (s == 10);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a TIME column. (Package cache = true)
**/
    public void Var051()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_TIME"));
            assertCondition (s == 8);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a TIMESTAMP column. (Package cache = true)
**/
    public void Var052()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (s == 26);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnDisplaySize() - Check a BLOB column. (Package cache = true)
**/
    public void Var053()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_BLOB"));
                assertCondition (s == 1048576);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check a CLOB column. (Package cache = true)
**/
    public void Var054()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_CLOB"));
		assertCondition (s == 1048576,"s = "+s+" SB 1048576");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check a DBCLOB column. (Package cache = true)
**/
    public void Var055()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DBCLOB"));
                assertCondition (s == 524288, "Got "+s+" expected 524288 for dbclob");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check a DATALINK column. (Package cache = true)
**/
    public void Var056()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DATALINK"));
                assertCondition (s == 200);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check a DISTINCT column. (Package cache = true)
**/
    public void Var057()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DISTINCT"));
                assertCondition (s == 12);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


// $B0A
/**
getColumnDisplaySize() - Check a BIGINT column. (Package cache = true)
**/
    public void Var058()
    {
        if (checkBigintSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_BIGINT"));
                assertCondition (s == 20);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getColumnDisplaySize() - Check a DECFLOAT 16 column. (Package cache = false)
Size = 16 digits + leading sign + decimal point + E + exponent size + 3 exponent digits
**/
    public void Var059()
    {
        if (checkDecFloatSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DECFLOAT16"));
                assertCondition (s == 23, "Got "+s+" sb 23 for DECFLOAT 16");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getColumnDisplaySize() - Check a DECFLOAT 34 column. (Package cache = false)
Size = 34 digits + leading sign + decimal point + E + exponent size + 4 exponent digits
**/
    public void Var060()
    {
        if (checkDecFloatSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DECFLOAT34"));
                assertCondition (s == 42, "Got "+s+" sb 42 for DECFLOAT 34");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnDisplaySize() - Check a DECFLOAT 16 column. (Package cache = true)
**/
    public void Var061()
    {
        if (checkDecFloatSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DECFLOAT16"));
                assertCondition (s == 23, "Got "+s+" sb 23 for DECFLOAT 16");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getColumnDisplaySize() - Check a DECFLOAT 34 column. (Package cache = false)
**/
    public void Var062()
    {
        if (checkDecFloatSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_DECFLOAT34"));
                assertCondition (s == 42, "Got "+s+" sb 42 for DECFLOAT 34");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getColumnDisplaySize() - Check a BOOLEAN column.
**/
    public void Var063()
    {
        if (checkBooleanSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnDisplaySize (rs_.findColumn ("C_BOOLEAN"));
                assertCondition (s == 1, "Got "+s+" sb 1 for BOOLEAN");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




}


