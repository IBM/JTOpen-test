///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetColumnType.java
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
// File Name:    JDRSMDGetColumnType.java
//
// Classes:      JDRSMDGetColumnType
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDRSMDGetColumnType.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getColumnType()
</ul>
**/
public class JDRSMDGetColumnType
extends JDTestcase
{



    // Constants.
    private static final String PACKAGE             = "JDRSMDGCT";
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
    public JDRSMDGetColumnType (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDGetColumnType",
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
        PACKAGE_CACHE_YES   = "extended dynamic=true;package="
          + PACKAGE + ";package library="
          + JDRSMDTest.COLLECTION
          + ";package cache=true";

        if (isToolboxDriver())
        {
            JDSetupPackage.prime (systemObject_, PACKAGE,
                JDRSMDTest.COLLECTION, "SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
        }
        else
        {
            if (getDatabaseType() == JDTestDriver.DB_SYSTEMI) {
            JDSetupPackage.prime (systemObject_, encryptedPassword_, PACKAGE,
                JDRSMDTest.COLLECTION, "SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET, "", getDriver());
            }
        }

        reconnect (PACKAGE_CACHE_NO);

        if (areLobsSupported ()) {
            Statement statement = connection_.createStatement ();
            statement.executeUpdate ("CREATE TYPE " + JDRSMDTest.COLLECTION
                                     + ".JDTESTMONEY AS NUMERIC(10,2)");
            statement.close ();
        }
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
            if (connection_ != null) {
                rs_.close ();
                statement_.close ();
                connection_.close ();
            }

            // SQL400 - driver neutral...
            String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
                
                ;

            connection_ = testDriver_.getConnection (url + ";" + properties_,systemObject_.getUserId(), encryptedPassword_);
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
        if (areLobsSupported ())
            statement_.executeUpdate ("DROP TYPE " + JDRSMDTest.COLLECTION + ".JDTESTMONEY");

        rs_.close ();
        statement_.close ();
        connection_.close ();
    }



/**
getColumnType() - Check column -1.  Should throw an exception.
(Package cache = false)
**/
    public void Var001()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getColumnType (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnType() - Check column 0.  Should throw an exception.
(Package cache = false)
**/
    public void Var002()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getColumnType (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnType() - Check a column greater than the max.
Should throw an exception. (Package cache = false)
**/
    public void Var003()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getColumnType (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnType() - Check when the result set is closed.
**/
    public void Var004()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            int v = rsmd.getColumnType (rs_.findColumn ("C_SMALLINT"));
            s.close ();
            assertCondition (v == Types.SMALLINT);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check when the meta data comes from a prepared statement.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps;
		try { 
		    ps = connection_.prepareStatement ("SELECT * FROM "
						       + JDRSMDTest.RSMDTEST_GET, ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
		} catch (Exception e) {
		    ps = connection_.prepareStatement ("SELECT * FROM " + JDRSMDTest.RSMDTEST_GET); 
		}

                ResultSetMetaData rsmd = ps.getMetaData ();
                int v = rsmd.getColumnType (rs_.findColumn ("C_SMALLINT"));
                ps.close ();
                assertCondition (v == Types.SMALLINT);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnType() - Check a SMALLINT column. (Package cache = false)
**/
    public void Var006()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_SMALLINT"));
            assertCondition (s == Types.SMALLINT);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check an INTEGER column. (Package cache = false)
**/
    public void Var007()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_INTEGER"));
            assertCondition (s == Types.INTEGER);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a REAL column. (Package cache = false)
**/
    public void Var008()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_REAL"));
            assertCondition (s == Types.REAL, "Got "+s+" expected Types.REAL");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a FLOAT column. (Package cache = false)
**/
    public void Var009()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_FLOAT"));
            assertCondition (s == Types.DOUBLE);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a FLOAT(3) column. (Package cache = false)
**/
    public void Var010()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_FLOAT_3"));
            assertCondition (s == Types.REAL, "Got "+s+" expected Types.REAL");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a DOUBLE column. (Package cache = false)
**/
    public void Var011()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_DOUBLE"));
            assertCondition (s == Types.DOUBLE);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a DECIMAL(10,5) column. (Package cache = false)
**/
    public void Var012()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (s == Types.DECIMAL);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a NUMERIC(10,5) column. (Package cache = false)
**/
    public void Var013()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (s == Types.NUMERIC);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a CHAR(50) column. (Package cache = false)
**/
    public void Var014()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_CHAR_50"));
            assertCondition (s == Types.CHAR);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a VARCHAR(50) column. (Package cache = false)
**/
    public void Var015()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s == Types.VARCHAR);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a BINARY(20) column. (Package cache = false)
**/
    public void Var016()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_BINARY_20"));
            assertCondition (s == Types.BINARY);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a VARBINARY(20) column. (Package cache = false)
**/
    public void Var017()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (s == Types.VARBINARY);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a DATE column. (Package cache = false)
**/
    public void Var018()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_DATE"));
            assertCondition (s == Types.DATE);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a TIME column. (Package cache = false)
**/
    public void Var019()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_TIME"));
            assertCondition (s == Types.TIME);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a TIMESTAMP column. (Package cache = false)
**/
    public void Var020()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (s == Types.TIMESTAMP);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a BLOB column. (Package cache = false)
**/
    public void Var021()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_BLOB"));
                assertCondition (s == Types.BLOB);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnType() - Check a CLOB column. (Package cache = false)
**/
    public void Var022()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_CLOB"));
                assertCondition (s == Types.CLOB);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnType() - Check a DBCLOB column. (Package cache = false)
**/
    public void Var023()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_DBCLOB"));
                assertCondition (s == Types.CLOB);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnType() - Check a DATALINK column. (Package cache = false)
**/
    public void Var024()
    {

 	if (isToolboxDriver() && System.getProperty("java.home").indexOf("13") > 0 )
 	{
 	    notApplicable("Datalink not working in JDK 1.3 for toolbox ");
 	    return;
 	}



	if (checkLobSupport ()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		int s = rsmd_.getColumnType (rs_.findColumn ("C_DATALINK"));
		      assertCondition (s == 70); //@B2A 70 is the constant from java.sql.Types for DATALINK
	    }
	    catch(Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
getColumnType() - Check a DISTINCT column. (Package cache = false)
**/
    public void Var025()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_DISTINCT"));
                assertCondition (s == Types.NUMERIC);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnType() - Check a BIGINT column. (Package cache = false)
**/
    public void Var026()
    {
        if (checkBigintSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_BIGINT"));
                assertCondition (s == Types.BIGINT);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnType() - Check column -1.  Should throw an exception.
(Package cache = true)
**/
    public void Var027()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getColumnType (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnType() - Check column 0.  Should throw an exception.
(Package cache = true)
**/
    public void Var028()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getColumnType (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnType() - Check a column greater than the max.
Should throw an exception. (Package cache = true)
**/
    public void Var029()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getColumnType (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnType() - Check a SMALLINT column. (Package cache = true)
**/
    public void Var030()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_SMALLINT"));
            assertCondition (s == Types.SMALLINT);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check an INTEGER column. (Package cache = true)
**/
    public void Var031()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_INTEGER"));
            assertCondition (s == Types.INTEGER);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a REAL column. (Package cache = true)
**/
    public void Var032()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_REAL"));
            assertCondition (s == Types.REAL, "Got "+s+" expected Types.REAL");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a FLOAT column. (Package cache = true)
**/
    public void Var033()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_FLOAT"));
            assertCondition (s == Types.DOUBLE);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a FLOAT(3) column. (Package cache = true)
**/
    public void Var034()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_FLOAT_3"));
            assertCondition (s == Types.REAL, "Got "+s+" expected Types.REAL");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a DOUBLE column. (Package cache = true)
**/
    public void Var035()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_DOUBLE"));
            assertCondition (s == Types.DOUBLE);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a DECIMAL(10,5) column. (Package cache = true)
**/
    public void Var036()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (s == Types.DECIMAL);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a NUMERIC(10,5) column. (Package cache = true)
**/
    public void Var037()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (s == Types.NUMERIC);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a CHAR(50) column. (Package cache = true)
**/
    public void Var038()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_CHAR_50"));
            assertCondition (s == Types.CHAR);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a VARCHAR(50) column. (Package cache = true)
**/
    public void Var039()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s == Types.VARCHAR);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a BINARY(20) column. (Package cache = true)
**/
    public void Var040()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_BINARY_20"));
            assertCondition (s == Types.BINARY);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a VARBINARY(20) column. (Package cache = true)
**/
    public void Var041()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (s == Types.VARBINARY);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a DATE column. (Package cache = true)
**/
    public void Var042()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_DATE"));
            assertCondition (s == Types.DATE);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a TIME column. (Package cache = true)
**/
    public void Var043()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_TIME"));
            assertCondition (s == Types.TIME);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a TIMESTAMP column. (Package cache = true)
**/
    public void Var044()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getColumnType (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (s == Types.TIMESTAMP);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnType() - Check a BLOB column. (Package cache = true)
**/
    public void Var045()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_BLOB"));
                assertCondition (s == Types.BLOB);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnType() - Check a CLOB column. (Package cache = true)
**/
    public void Var046()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_CLOB"));
                assertCondition (s == Types.CLOB);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnType() - Check a DBCLOB column. (Package cache = true)
**/
    public void Var047()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_DBCLOB"));
                assertCondition (s == Types.CLOB);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnType() - Check a DATALINK column. (Package cache = true)
**/
    public void Var048()
    {

 	if (isToolboxDriver() && System.getProperty("java.home").indexOf("13") > 0 )
 	{
 	    notApplicable("Datalink not working in JDK 1.3 for toolbox ");
 	    return;
 	}

	if (checkLobSupport ()) {
	    try {
		reconnect (PACKAGE_CACHE_YES);
		int s = rsmd_.getColumnType (rs_.findColumn ("C_DATALINK"));
		assertCondition (s == 70); //@B2A 70 is the constant from java.sql.Types for DATALINK
	    }
	    catch(Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
getColumnType() - Check a DISTINCT column. (Package cache = true)
**/
    public void Var049()
    {
        if (checkLobSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_DISTINCT"));
                assertCondition (s == Types.NUMERIC);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getColumnType() - Check a BIGINT column. (Package cache = true)
**/
    public void Var050()
    {
        if (checkBigintSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_BIGINT"));
                assertCondition (s == Types.BIGINT);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnType() - Check a DECFLOAT 16 column. (Package cache = false)
**/
    public void Var051()
    {
        if (checkDecFloatSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_DECFLOAT16"));
                assertCondition (s == Types.OTHER, "found type "+s+" expected Types.OTHER");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception checking DECFLOAT16");
            }
        }
    }


/**
getColumnType() - Check a DECFLOAT 34 column. (Package cache = false)
**/
    public void Var052()
    {
        if (checkDecFloatSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_DECFLOAT34"));
                assertCondition (s == Types.OTHER, "found type "+s+" expected Types.OTHER");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception checking DECFLOAT34");
            }
        }
    }



/**
getColumnType() - Check a DECFLOAT 16 column. (Package cache = true)
**/
    public void Var053()
    {
        if (checkDecFloatSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_DECFLOAT16"));
                assertCondition (s == Types.OTHER, "found type "+s+" expected Types.OTHER");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception checking DECFLOAT16");
            }
        }
    }


/**
getColumnType() - Check a DECFLOAT 34 column. (Package cache = true)
**/
    public void Var054()
    {
        if (checkDecFloatSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_DECFLOAT34"));
                assertCondition (s == Types.OTHER, "found type "+s+" expected Types.OTHER");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception checking DECFLOAT34");
            }
        }
    }

    /**
    getColumnType() - Check SQLXML column
    **/
        public void Var055()
        {
          if (checkXmlSupport()) {
	      // Note:  We must support the XML type, even if we are not using
	      // JDBC 4.0
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                    + JDRSMDTest.RSMDTEST_GET2);
                ResultSetMetaData rsmd = rs.getMetaData ();

                int v = rsmd.getColumnType (rs.findColumn ("C_XML"));
                rs.close ();
                s.close ();
		if (isJdbc40()) {
		    assertCondition (v == 2009, "Got type "+v+" expected 2009");
		} else {
		    assertCondition (v == Types.CLOB, "Got type "+v+" expected Types.CLOB");
		}

            }
            catch(Exception e) {
                failed (e, "Unexpected Exception - V7R1 XML");
            }
          }
        }


    /**
    getColumnType() - Check UTF-8 VARCHAR column
    **/
        public void Var056()
        {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT CAST('A' AS VARCHAR(100) CCSID 1208) AS C1 FROM SYSIBM.SYSDUMMY1"); 
                ResultSetMetaData rsmd = rs.getMetaData ();

                int v = rsmd.getColumnType (rs.findColumn ("C1"));
                rs.close ();
                s.close ();
		assertCondition (v == Types.VARCHAR, "Got type "+v+" expected Types.VARCHAR");


            }
            catch(Exception e) {
                failed (e, "Unexpected Exception - UTF-8");
            }
        }



    /**
    getColumnType() - Check UTF-8 CHAR column
    **/
        public void Var057()
        {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT CAST('A' AS CHAR(100) CCSID 1208) AS C1 FROM SYSIBM.SYSDUMMY1"); 
                ResultSetMetaData rsmd = rs.getMetaData ();

                int v = rsmd.getColumnType (rs.findColumn ("C1"));
                rs.close ();
                s.close ();
		assertCondition (v == Types.CHAR, "Got type "+v+" expected Types.CHAR");

            }
            catch(Exception e) {
                failed (e, "Unexpected Exception - UTF-8");
            }
        }




    /**
    getColumnType() - Check UCS2 vargraphic column
    **/
        public void Var058()
        {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT CAST('A' AS VARGRAPHIC(512) CCSID 13488) AS C1 FROM SYSIBM.SYSDUMMY1"); 
                ResultSetMetaData rsmd = rs.getMetaData ();

                int v = rsmd.getColumnType (rs.findColumn ("C1"));
                rs.close ();
                s.close ();
		assertCondition (v == Types.VARCHAR, "Got type "+v+" expected Types.VARCHAR");


            }
            catch(Exception e) {
                failed (e, "Unexpected Exception - UTF-8");
            }
        }


    /**
    getColumnType() - Check UCS2 graphic column
    **/
        public void Var059()
        {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT CAST('A' AS GRAPHIC(512) CCSID 13488) AS C1 FROM SYSIBM.SYSDUMMY1"); 
                ResultSetMetaData rsmd = rs.getMetaData ();

                int v = rsmd.getColumnType (rs.findColumn ("C1"));
                rs.close ();
                s.close ();
		assertCondition (v == Types.CHAR, "Got type "+v+" expected Types.CHAR");

            }
            catch(Exception e) {
                failed (e, "Unexpected Exception - UTF-16");
            }
        }



    /**
    getColumnType() - Check UTF-16 vargraphic column
    **/
        public void Var060()
        {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT CAST('A' AS VARGRAPHIC(100) CCSID 1200) AS C1 FROM SYSIBM.SYSDUMMY1"); 
                ResultSetMetaData rsmd = rs.getMetaData ();

                int v = rsmd.getColumnType (rs.findColumn ("C1"));
                rs.close ();
                s.close ();
		if (isJdbc40()) {
		    assertCondition (v == -9, "Got type "+v+" expected Types.NVARCHAR");
		} else { 
		    assertCondition (v == Types.VARCHAR, "Got type "+v+" expected Types.VARCHAR");
		}


            }
            catch(Exception e) {
                failed (e, "Unexpected Exception - UTF-16");
            }
        }


    /**
    getColumnType() - Check UCS2 graphic column
    **/
        public void Var061()
        {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT CAST('A' AS GRAPHIC(100) CCSID 13488) AS C1 FROM SYSIBM.SYSDUMMY1"); 
                ResultSetMetaData rsmd = rs.getMetaData ();

                int v = rsmd.getColumnType (rs.findColumn ("C1"));
                rs.close ();
                s.close ();
		assertCondition (v == Types.CHAR, "Got type "+v+" expected Types.CHAR");


            }
            catch(Exception e) {
                failed (e, "Unexpected Exception - UCS-2");
            }
        }


    /**
    getColumnType() - Check NVARCHAR column
    **/
        public void Var062()
        {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT CAST('A' AS NVARCHAR(100)) AS C1 FROM SYSIBM.SYSDUMMY1"); 
                ResultSetMetaData rsmd = rs.getMetaData ();

                int v = rsmd.getColumnType (rs.findColumn ("C1"));
                rs.close ();
                s.close ();
		if (isJdbc40()) {
		    assertCondition (v == -9, "Got type "+v+" expected Types.NVARCHAR");

		} else { 
		    assertCondition (v == Types.VARCHAR, "NOT JDBC4.0: Got type "+v+" expected Types.VARCHAR");
		}

            }
            catch(Exception e) {
                failed (e, "Unexpected Exception - NVARCHAR");
            }
        }


    /**
    getColumnType() - Check NCHAR column
    **/
        public void Var063()
        {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT CAST('A' AS NCHAR(100)) AS C1 FROM SYSIBM.SYSDUMMY1"); 
                ResultSetMetaData rsmd = rs.getMetaData ();

                int v = rsmd.getColumnType (rs.findColumn ("C1"));
                rs.close ();
                s.close ();
		if (isJdbc40()) {
		    assertCondition (v == -15, "Got type "+v+" expected Types.NCHAR");
		} else { 
		    assertCondition (v == Types.CHAR, "Got type "+v+" expected Types.CHAR");
		}

            }
            catch(Exception e) {
                failed (e, "Unexpected Exception - NCHAR");
            }
        }



    /**
    getColumnType() - Check NCLOB column
    **/
        public void Var064()
        {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT CAST('A' AS NCLOB(100)) AS C1 FROM SYSIBM.SYSDUMMY1"); 
                ResultSetMetaData rsmd = rs.getMetaData ();

                int v = rsmd.getColumnType (rs.findColumn ("C1"));
                rs.close ();
                s.close ();
		if (isJdbc40()) {
		    assertCondition (v == 2011, "Got type "+v+" expected Types.NCLOB(2011)");

		} else {  
		assertCondition (v == Types.CLOB, "Got type "+v+" expected Types.CLOB("+Types.CLOB+")");
		}

            }
            catch(Exception e) {
                failed (e, "Unexpected Exception - NCLOB");
            }
        }



/**
getColumnType() - Check a BOOLEAN column. (Package cache = true)
**/
    public void Var065()
    {
        if (checkBooleanSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_BOOLEAN"));
                assertCondition (s == Types.BOOLEAN);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getColumnType() - Check a BOOLEAN column. (Package cache = false)
**/
    public void Var066()
    {
        if (checkBooleanSupport ()) {
            try {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getColumnType (rs_.findColumn ("C_BOOLEAN"));
                assertCondition (s == Types.BOOLEAN);
            }	
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }







}


