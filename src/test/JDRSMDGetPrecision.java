///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetPrecision.java
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
// File Name:    JDRSMDGetPrecision.java
//
// Classes:      JDRSMDGetPrecision
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSMDGetPrecision.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getPrecision()
</ul>
**/
public class JDRSMDGetPrecision
extends JDTestcase
{



    // Constants.
    private static final String PACKAGE             = "JDRSMDGS";
    private static final String PACKAGE_CACHE_NO    = "extended dynamic=false";
    private static final String PACKAGE_CACHE_NO_LARGE_PRECISION = "extended dynamic=false;maximum precision=63;maximum scale=63";
    private static String PACKAGE_CACHE_YES = "extended dynamic=true;package=" + PACKAGE + ";package library="
                                              + JDRSMDTest.COLLECTION + ";package cache=true";
    private static String PACKAGE_CACHE_YES_LARGE_PRECISION = "extended dynamic=true;package=" + PACKAGE + ";package library="
                                                              + JDRSMDTest.COLLECTION + ";package cache=true;maximum precision=63;maximum scale=63";

    // Private data.
    private String              properties_     = "";
    private ResultSet           rs_             = null;
    private ResultSetMetaData   rsmd_           = null;
    private Statement           statement_      = null;
    private ResultSet           rs2_            = null;
    private ResultSetMetaData   rsmd2_          = null;
    private Statement           statement2_     = null;
    private ResultSet           rs3_            = null;
    private ResultSetMetaData   rsmd3_          = null;
    private Statement           statement3_     = null;



    /**
    Constructor.
    **/
    public JDRSMDGetPrecision (AS400 systemObject,
                               Hashtable namesAndVars,
                               int runMode,
                               FileOutputStream fileOutputStream,
                               
                               String password)
    {
        super (systemObject, "JDRSMDGetPrecision",
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
        PACKAGE_CACHE_YES = "extended dynamic=true;package=" + PACKAGE + ";package library="
                            + JDRSMDTest.COLLECTION + ";package cache=true";
        PACKAGE_CACHE_YES_LARGE_PRECISION = "extended dynamic=true;package=" + PACKAGE + ";package library="
                                            + JDRSMDTest.COLLECTION + ";package cache=true;maximum precision=63;maximum scale=63";

        if(isToolboxDriver())
        {
            JDSetupPackage.prime(systemObject_,  PACKAGE,
                                 JDRSMDTest.COLLECTION, "SELECT * FROM "
                                 + JDRSMDTest.RSMDTEST_GET);

            if(areLargeDecimalPrecisionsSupported())
            {
                // also prime from the large precision table
                JDSetupPackage.prime(systemObject_, encryptedPassword_,  PACKAGE,
                                     JDRSMDTest.COLLECTION, "SELECT * FROM "
                                     + JDRSMDTest.RSMDTEST_GET_BIG_PRECISION, "maximum precision=63;maximum scale=63", getDriver());
            }
        }
        else
        {
            JDSetupPackage.prime (systemObject_, encryptedPassword_, PACKAGE,
                                  JDRSMDTest.COLLECTION, "SELECT * FROM "
                                  + JDRSMDTest.RSMDTEST_GET, "", getDriver());

            if(areLargeDecimalPrecisionsSupported())
            {
                // also prime from the large precision table
                JDSetupPackage.prime(systemObject_, encryptedPassword_, PACKAGE, JDRSMDTest.COLLECTION,
                                     "SELECT * FROM " + JDRSMDTest.RSMDTEST_GET_BIG_PRECISION,
                                     "maximum precision=63;maximum scale=63", getDriver());
            }
        }

        reconnect (PACKAGE_CACHE_NO);
    }



    /**
    Reconnects with different properties, if needed.

    @exception Exception If an exception occurs.
    **/
    private void reconnect (String properties)
    throws Exception
    {
        if(! properties_.equals (properties))
        {
            properties_ = properties;
            if(connection_ != null)
                cleanup ();

            // SQL400 - driver neutral...
            String url = baseURL_
                         // String url = "jdbc:as400://" + systemObject_.getSystemName()
                         
                          + ";" + properties_;
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            statement_ = connection_.createStatement ();
            rs_ = statement_.executeQuery ("SELECT * FROM " + JDRSMDTest.RSMDTEST_GET);
            rsmd_ = rs_.getMetaData ();

            statement2_ = connection_.createStatement ();
            rs2_ = statement2_.executeQuery ("SELECT * FROM " + JDRSMDTest.RSMDTEST_GET2);
            rsmd2_ = rs2_.getMetaData ();

            if(areLargeDecimalPrecisionsSupported())
            {
                statement3_ = connection_.createStatement();
                rs3_ = statement3_.executeQuery("SELECT * FROM " + JDRSMDTest.RSMDTEST_GET_BIG_PRECISION);
                rsmd3_ = rs3_.getMetaData();
            }
        }
    }



    /**
    Performs cleanup needed after running variations.

    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
        if(areLargeDecimalPrecisionsSupported())
        {
            rs3_.close();
            statement3_.close();
        }

        rs_.close ();
        statement_.close ();
        rs2_.close ();
        statement2_.close ();
        connection_.close ();
    }



    /**
    getPrecision() - Check column -1.  Should throw an exception.
    (Package cache = false)
    **/
    public void Var001()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getPrecision (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getPrecision() - Check column 0.  Should throw an exception.
    (Package cache = false)
    **/
    public void Var002()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getPrecision (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getPrecision() - Check a column greater than the max.
    Should throw an exception. (Package cache = false)
    **/
    public void Var003()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getPrecision (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getPrecision() - Check when the result set is closed.
    **/
    public void Var004()
    {
        try
        {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT C_SMALLINT FROM "
                                           + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            int v = rsmd.getPrecision (1);
            assertCondition (v == 5, "v="+v+" sb 5");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check when the meta data comes from a prepared statement.
    **/
    public void Var005()
    {
        if(checkJdbc20 ())
        {
            try
            {
		PreparedStatement ps;
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    ps = connection_.prepareStatement ("SELECT C_SMALLINT FROM "
									 + JDRSMDTest.RSMDTEST_GET);
		} else {
		    ps = connection_.prepareStatement ("SELECT C_SMALLINT FROM "
									 + JDRSMDTest.RSMDTEST_GET, ResultSet.TYPE_SCROLL_INSENSITIVE,
									 ResultSet.CONCUR_READ_ONLY);
		}
                ResultSetMetaData rsmd = ps.getMetaData ();
                int v = rsmd.getPrecision (1);
                ps.close ();
                assertCondition (v == 5, "v="+v+" sb 5");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a SMALLINT column. (Package cache = false)
    **/
    public void Var006()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_SMALLINT"));
            assertCondition (s == 5,  "s="+s+" sb 5");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check an INTEGER column. (Package cache = false)
    **/
    public void Var007()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_INTEGER"));
            assertCondition (s == 10, "s = "+s+" sb 10");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a REAL column. (Package cache = false)
    **/
    public void Var008()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_REAL"));
            assertCondition (s == 24, "s = "+s+" SB = 24");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a FLOAT column. (Package cache = false)
    **/
    public void Var009()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_FLOAT"));
            assertCondition (s == 53, "s = "+s+" SB = 53");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a FLOAT(3) column. (Package cache = false)
    **/
    public void Var010()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_FLOAT_3"));
            assertCondition (s == 24, "s = "+s+" SB = 24");    // Treated as REAL.
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a DOUBLE column. (Package cache = false)
    **/
    public void Var011()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_DOUBLE"));
            assertCondition (s == 53, "s = "+s+" SB = 53");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a DECIMAL(5,0) column. (Package cache = false)
    **/
    public void Var012()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_DECIMAL_50"));
            assertCondition (s == 5);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a DECIMAL(10,5) column. (Package cache = false)
    **/
    public void Var013()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (s == 10);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a NUMERIC(5,0) column. (Package cache = false)
    **/
    public void Var014()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_NUMERIC_50"));
            assertCondition (s == 5);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a NUMERIC(10,5) column. (Package cache = false)
    **/
    public void Var015()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (s == 10);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a CHAR column. (Package cache = false)
    **/
    public void Var016()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_CHAR_1"));
            assertCondition (s == 1, "s="+s+" sb 1");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a CHAR(50) column. (Package cache = false)
    **/
    public void Var017()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_CHAR_50"));
            assertCondition (s == 50, "s="+s+" sb 50");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a VARCHAR(50) column. (Package cache = false)
    **/
    public void Var018()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s == 50, "s="+s+" sb 50");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a BINARY column. (Package cache = false)
    **/
    public void Var019()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_BINARY_1"));
            assertCondition (s == 1, "s="+s+" sb 1");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a BINARY(20) column. (Package cache = false)
    **/
    public void Var020()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_BINARY_20"));
            assertCondition (s == 20, "s="+s+" sb 20");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a VARBINARY(20) column. (Package cache = false)
    **/
    public void Var021()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (s == 20, "s="+s+" sb 20");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a DATE column. (Package cache = false)
    **/
    public void Var022()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_DATE"));
            assertCondition (s == 10, "s="+s+" sb 10");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a TIME column. (Package cache = false)
    **/
    public void Var023()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_TIME"));
            assertCondition (s == 8, "s="+s+" sb 8");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a TIMESTAMP column. (Package cache = false)
    **/
    public void Var024()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (s == 26, "s="+s+" sb 26");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a BLOB column. (Package cache = false)
    **/
    public void Var025()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_BLOB"));
                assertCondition (s == 1048576, "s="+s+" sb 1048576");    // @C0C
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a CLOB column. (Package cache = false)
    **/
    public void Var026()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_CLOB"));
                assertCondition (s == 1048576, "s="+s+" sb 1048576");    // @C0C
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a DBCLOB column. (Package cache = false)
    **/
    public void Var027()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_DBCLOB"));
                assertCondition (s == 1048576, "s="+s+" sb 1048576");    // @C0C
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a DATALINK column. (Package cache = false)
    **/
    public void Var028()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_DATALINK"));
                assertCondition (s == 200, "s="+s+" sb 200");    // @C0C
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a DISTINCT column. (Package cache = false)
    **/
    public void Var029()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_DISTINCT"));
                assertCondition (s == 10, "s="+s+" sb 10");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    // @D0A
    /**
    getPrecision() - Check a BIGINT column. (Package cache = false)
    **/
    public void Var030()
    {
        if(checkBigintSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_BIGINT"));
                assertCondition (s == 19, "s="+s+" sb 19");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check column -1.  Should throw an exception.
    (Package cache = true)
    **/
    public void Var031()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getPrecision (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getPrecision() - Check column 0.  Should throw an exception.
    (Package cache = true)
    **/
    public void Var032()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getPrecision (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getPrecision() - Check a column greater than the max.
    Should throw an exception. (Package cache = true)
    **/
    public void Var033()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getPrecision (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getPrecision() - Check a SMALLINT column. (Package cache = true)
    **/
    public void Var034()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_SMALLINT"));
            assertCondition (s == 5, "s="+s+" sb 5");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check an INTEGER column. (Package cache = true)
    **/
    public void Var035()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_INTEGER"));
            assertCondition (s == 10);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a REAL column. (Package cache = true)
    **/
    public void Var036()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_REAL"));
            assertCondition (s == 24, "s = "+s+" SB = 24");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a FLOAT column. (Package cache = true)
    **/
    public void Var037()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_FLOAT"));
            assertCondition (s == 53, "s = "+s+" SB = 53");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a FLOAT(3) column. (Package cache = true)
    **/
    public void Var038()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_FLOAT_3"));
            assertCondition (s == 24, "s = "+s+" SB = 24");    // Treated as REAL.
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a DOUBLE column. (Package cache = true)
    **/
    public void Var039()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_DOUBLE"));
            assertCondition (s == 53, "s = "+s+" SB = 53");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a DECIMAL(5,0) column. (Package cache = true)
    **/
    public void Var040()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_DECIMAL_50"));
            assertCondition (s == 5);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a DECIMAL(10,5) column. (Package cache = true)
    **/
    public void Var041()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (s == 10);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a NUMERIC(5,0) column. (Package cache = true)
    **/
    public void Var042()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_NUMERIC_50"));
            assertCondition (s == 5);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a NUMERIC(10,5) column. (Package cache = true)
    **/
    public void Var043()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (s == 10);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a CHAR column. (Package cache = true)
    **/
    public void Var044()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_CHAR_1"));
            assertCondition (s == 1);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a CHAR(50) column. (Package cache = true)
    **/
    public void Var045()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_CHAR_50"));
            assertCondition (s == 50);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a VARCHAR(50) column. (Package cache = true)
    **/
    public void Var046()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s == 50);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a BINARY column. (Package cache = true)
    **/
    public void Var047()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_BINARY_1"));
            assertCondition (s == 1, "got "+s+" expected 1 for binary 1");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a BINARY(20) column. (Package cache = true)
    **/
    public void Var048()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_BINARY_20"));
            assertCondition (s == 20, "got "+s+" expected 20 for binary 20");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a VARBINARY(20) column. (Package cache = true)
    **/
    public void Var049()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (s == 20, "got "+s+" expected 20 for varbinary 20");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a DATE column. (Package cache = true)
    **/
    public void Var050()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_DATE"));
            assertCondition (s == 10);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a TIME column. (Package cache = true)
    **/
    public void Var051()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_TIME"));
            assertCondition (s == 8);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a TIMESTAMP column. (Package cache = true)
    **/
    public void Var052()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (s == 26);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getPrecision() - Check a BLOB column. (Package cache = true)
    **/
    public void Var053()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_BLOB"));
                assertCondition (s == 1048576);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a CLOB column. (Package cache = true)
    **/
    public void Var054()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_CLOB"));
                assertCondition (s == 1048576);    // @
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a DBCLOB column. (Package cache = true)
    **/
    public void Var055()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_DBCLOB"));
                assertCondition (s == 1048576);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a DATALINK column. (Package cache = true)
    **/
    public void Var056()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_DATALINK"));
                assertCondition (s == 200);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a DISTINCT column. (Package cache = true)
    **/
    public void Var057()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_DISTINCT"));
                assertCondition (s == 10);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    // @D0A
    /**
    getPrecision() - Check a BIGINT column. (Package cache = true)
    **/
    public void Var058()
    {
        if(checkBigintSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_BIGINT"));
                assertCondition (s == 19);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var059()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_630"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var060()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_632"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var061()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6331"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var062()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6333"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var063()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var064()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_310"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var065()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_312"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var066()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var067()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_630"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var068()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_632"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var069()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6331"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var070()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6333"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var071()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var072()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_310"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var073()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_312"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var074()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var075()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_630"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var076()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_632"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var077()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6331"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var078()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6333"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var079()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var080()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_310"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var081()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_312"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var082()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var083()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_630"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var084()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_632"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var085()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6331"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var086()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6333"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var087()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var088()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_310"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var089()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_312"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var090()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var091()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_630"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var092()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_632"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var093()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6331"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var094()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6333"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var095()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var096()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_310"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var097()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_312"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var098()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var099()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_630"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var100()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_632"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var101()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6331"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var102()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6333"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var103()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var104()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_310"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var105()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_312"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a NUMERIC column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var106()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_NUMERIC_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var107()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_630"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var108()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_632"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var109()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6331"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var110()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6333"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var111()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var112()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_310"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var113()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_312"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = false)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var114()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var115()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_630"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var116()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_632"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var117()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6331"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var118()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6333"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var119()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected precision of 63, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var120()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_310"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var121()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_312"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a DECIMAL column. (Package cache = true)
    this is checking the precision of a 63 digit column in 31 digit mode
    **/
    public void Var122()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getPrecision(rs3_.findColumn("C_DECIMAL_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected precision of 31, got precision of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a DECFLOAT 16 column. (Package cache = false)
    **/
    public void Var123()
    {
        if(checkDecFloatSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_DECFLOAT16"));
                assertCondition (s == 16, "got "+s+" sb 16 for DECFLOAT 16");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getPrecision() - Check a DECFLOAT 34 column. (Package cache = false)
    **/
    public void Var124()
    {
        if(checkDecFloatSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_DECFLOAT34"));
                assertCondition (s == 34, "got "+s+" sb 34 for DECFLOAT 34");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getPrecision() - Check a DECFLOAT 16 column. (Package cache = true)
    **/
    public void Var125()
    {
        if(checkDecFloatSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_DECFLOAT16"));
                assertCondition (s == 16, "got "+s+" sb 16 for DECFLOAT 16");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getPrecision() - Check a DECFLOAT 34 column. (Package cache = true)
    **/
    public void Var126()
    {
        if(checkDecFloatSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_DECFLOAT34"));
                assertCondition (s == 34, "got "+s+" sb 34 for DECFLOAT 34");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getPrecision() - Check a XML column. (Package cache = false)
    **/
    public void Var127()
    {
        if(checkXmlSupport ())
        {
            try
            {
		int expected = 2147483647;
		if (isToolboxDriver()) {
		    expected = 2147483646;
		    // Toolbox code says.
		    // the DB2 SQL reference says this should be 2147483647 b
		    // but we return 1 less to allow for NOT NULL columns
		}

                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd2_.getPrecision (rs2_.findColumn ("C_XML"));
                assertCondition (s == expected, "got "+s+" sb "+expected+" for XML");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getPrecision() - Check a XML column. (Package cache = true)
    **/
    public void Var128()
    {
        if(checkXmlSupport ())
        {
            try
            {
		int expected = 2147483647;
		if (isToolboxDriver()) {
		    expected = 2147483646;
		    // Toolbox code says.
		    // the DB2 SQL reference says this should be 2147483647 b
		    // but we return 1 less to allow for NOT NULL columns
		}

                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd2_.getPrecision (rs2_.findColumn ("C_XML"));
                assertCondition (s == expected, "got "+s+" sb "+expected+" for XML");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


   /*
    * Test lots of different types
    */

    public boolean testCombination(String firstValidRelease, int testcase,  String type, String expectedAnswer, StringBuffer sb) {


	String rsmdExpectedAnswer = expectedAnswer;
	String dmdExpectedAnswer = expectedAnswer; 

	int splitIndex = expectedAnswer.indexOf(" S=");
	if (splitIndex > 0) {
	    rsmdExpectedAnswer=expectedAnswer.substring(0,splitIndex);
	    dmdExpectedAnswer=expectedAnswer.substring(splitIndex+3); 
	} 

	if (Integer.parseInt(firstValidRelease) > getRelease()) {
	    sb.append("Skipping test for type "+type+"\n");
	    return true; 
	}

	boolean passed = true;
  String sql = "";
	try {
	    Statement s = connection_.createStatement();
	    String baseTableName = "JDRSMDGPC"+testcase; 
	    String tableName = JDRSMDTest.COLLECTION+"."+baseTableName;
	    initTable(s, tableName, " (C1 "+type+")");
	    
	    connection_.commit();
	    sql = "SELECT * FROM "+tableName;
	    ResultSet rs = s.executeQuery(sql);
	    ResultSetMetaData rsmd  = rs.getMetaData();
	    String answer=""+rsmd.getPrecision(1);
	    if (rsmdExpectedAnswer.equals(answer)) {
	    } else {
		sb.append("***For type "+type+" rsmd.getPrecision() got "+answer+" expected "+rsmdExpectedAnswer+"\n");
		sb.append("***For type "+type+" rsmd.getColumnTypeName(1) = "+rsmd.getColumnTypeName(1)+"\n");
                sb.append("------------------"); 
		passed = false;
	    }

	    /* also check the metadata */
	    rs = s.executeQuery("SELECT COLUMN_SIZE FROM SYSIBM.SQLCOLUMNS " +
	    		"WHERE TABLE_SCHEM='"+JDRSMDTest.COLLECTION+"' " +
	    				"AND TABLE_NAME='"+baseTableName+"' AND COLUMN_NAME='C1'");
	    rs.next();
	    answer = rs.getString(1);
      if (dmdExpectedAnswer.equals(answer)) {
      } else {
	  sb.append("***For type "+type+" metadata(SYSIBM.SQLCOLUMNS) got "+answer+" expected "+dmdExpectedAnswer+"\n");
	  passed = false;
      }
      connection_.commit();

      cleanupTable(s, tableName);
      
      connection_.commit();


	    s.close();


	} catch (Exception e) {
	    passed = false;
	    sb.append("Sql = "+sql+"\n");
	    sb.append("Caught exception "+e+"\n");
	    printStackTraceToStringBuffer(e, sb); 
	}
	return passed;
    }


    public void Var129() {
	boolean passed = true;
	StringBuffer sb = new StringBuffer();

	String [][] typeAnswer = {
	    {"540", "SMALLINT", "5"},
	    {"540", "INTEGER", "10"},
	    {"540", "INT", "10"},
	    {"540", "BIGINT", "19"},
	    {"540", "DECIMAL", "5"},
      {"540", "DECIMAL(10,5)", "10"},
      {"540", "DECIMAL(10,10)", "10"},
      {"540", "DECIMAL(10,0)", "10"},
      {"540", "NUMERIC", "5"},
      {"540", "NUMERIC(10,5)", "10"},
      {"540", "NUMERIC(10,10)", "10"},
      {"540", "NUMERIC(10,0)", "10"},
      /* In V7R1 the SYSIBM.SQLCOLUMNS metadata is incorrect for FLOAT */ 
      /* 616CN returns 52 */
      /* The value for float is 52 -- per Mark Anderson 10/1/2013 */ 
      {"610", "FLOAT",        "53 S=52"},  
      {"540", "FLOAT(53)",    "53"},
      /* In V7R1 the SYSIBM.SQLCOLUMNS metadata is incorrect for FLOAT(1) */
      /* 616CN returns 3 */
      /* The value for float(1) is 3 -- per Mark Anderson 10/1/2013 */ 
      {"610", "FLOAT(1)",    "24 S=3"}, 
      {"540", "REAL",        "24"},
      /* In V7R1 the SYSIBM.SQLCOLUMNS metadata is incorrect for DOUBLE */
      /* 616CN returns 52 */
      /* The value for double is 52 -- per Mark Anderson 10/1/2013 */ 
      {"610", "DOUBLE",       "53 S=52"},   
      {"610", "DECFLOAT",       "34"},
      {"610", "DECFLOAT(34)",       "34"},
      {"610", "DECFLOAT(16)",       "16"},
      {"540", "CHAR",            "1"},
      {"540", "CHAR(1)",   "1"},
      {"540", "CHAR(20)",   "20"},
      {"540", "NCHAR(20)",   "20"},
      {"540", "CHAR(32765)",   "32765"},
      {"540", "VARCHAR(1)",   "1"},
      {"540", "VARCHAR(20)",   "20"},
      {"540", "NVARCHAR(20)",   "20"},
      {"540", "VARCHAR(32739)",   "32739"},
      {"540", "CLOB",            "1048576"},
      {"540", "CLOB(100)",       "100"},
      {"540", "NCLOB(100)",       "100"},
      {"540", "CLOB(1K)",       "1024"},
      {"540", "CLOB(1M)",      "1048576"},
      {"540", "CLOB(1G)",      "1073741824"},
      {"540", "GRAPHIC",            "1"},
      {"540", "GRAPHIC(1)",   "1"},
      {"540", "GRAPHIC(20)",   "20"},
      {"540", "GRAPHIC(16382)",   "16382"},
      {"540", "VARGRAPHIC(1)",   "1"},
      {"540", "VARGRAPHIC(20)",   "20"},
      {"540", "VARGRAPHIC(16362)",   "16362"},
      {"540", "DBCLOB",            "1048576"},
      {"540", "DBCLOB(100)",       "100"},
      {"540", "DBCLOB(1K)",       "1024"},
      {"540", "DBCLOB(1M)",      "1048576"},
      {"540", "BINARY(1)",   "1"},
      {"540", "BINARY(20)",   "20"},
      {"540", "BINARY(32765)",   "32765"},
      {"540", "VARBINARY(1)",   "1"},
      {"540", "VARBINARY(20)",   "20"},
      {"540", "VARBINARY(32739)",   "32739"},
      {"540", "DATE", "10"},
      {"540", "TIME", "8"},
      {"540", "TIMESTAMP","26"},

	};

	for (int i = 0; i < typeAnswer.length; i++) {
	    passed = testCombination(typeAnswer[i][0], i, typeAnswer[i][1], typeAnswer[i][2], sb) && passed;
	}

	String outInfo = "";
	if (!passed) {
	  outInfo = sb.toString();
	}
	assertCondition(passed, "\n"+outInfo);

    }

    /**
    getPrecision() - Check a NCHAR(50) column. (Package cache = false)
    **/
    public void Var130()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_NCHAR_50"));
            assertCondition (s == 50, "s="+s+" sb 50");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getPrecision() - Check a VARCHAR(50) column. (Package cache = false)
    **/
    public void Var131()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getPrecision (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s == 50, "s="+s+" sb 50");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getPrecision() - Check a BOOLEAN column. (Package cache = false)
    **/
    public void Var132()
    {
        if(checkBooleanSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getPrecision (rs_.findColumn ("C_BOOLEAN"));
                assertCondition (s == 1, "s="+s+" sb 1");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


}


