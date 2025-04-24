///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetScale.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RSMD;

import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestcase;
import test.JD.JDSetupPackage;



/**
Testcase JDRSMDGetScale.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getScale()
</ul>
**/
public class JDRSMDGetScale
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDGetScale";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }



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
    public JDRSMDGetScale (AS400 systemObject,
                           Hashtable<String,Vector<String>> namesAndVars,
                           int runMode,
                           FileOutputStream fileOutputStream,
                           
                           String password)
    {
        super (systemObject, "JDRSMDGetScale",
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
            JDSetupPackage.prime (systemObject_, PACKAGE,
                                  JDRSMDTest.COLLECTION, "SELECT * FROM "
                                  + JDRSMDTest.RSMDTEST_GET);

            if(areLargeDecimalPrecisionsSupported())
            {
                // also prime from the large precision table
                JDSetupPackage.prime(systemObject_, encryptedPassword_, PACKAGE,
                                     JDRSMDTest.COLLECTION, "SELECT * FROM "
                                     + JDRSMDTest.RSMDTEST_GET_BIG_PRECISION, "maximum precision=63;maximum scale=63", getDriver());
            }
        }
        else
        {
            JDSetupPackage.prime (systemObject_,encryptedPassword_, PACKAGE,
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
            connection_ = testDriver_.getConnection (url + ";" + properties_,systemObject_.getUserId(), encryptedPassword_);
            statement_ = connection_.createStatement ();
            rs_ = statement_.executeQuery ("SELECT * FROM "
                                           + JDRSMDTest.RSMDTEST_GET);
            rsmd_ = rs_.getMetaData ();

            statement2_ = connection_.createStatement ();
            rs2_ = statement2_.executeQuery ("SELECT * FROM "
                                           + JDRSMDTest.RSMDTEST_GET2);
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
        connection_.close ();
    }



    /**
    getScale() - Check column -1.  Should throw an exception.
    (Package cache = false)
    **/
    public void Var001()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getScale (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getScale() - Check column 0.  Should throw an exception.
    (Package cache = false)
    **/
    public void Var002()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getScale (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getScale() - Check a column greater than the max.
    Should throw an exception. (Package cache = false)
    **/
    public void Var003()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getScale (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getScale() - Check when the result set is closed.
    **/
    public void Var004()
    {
        try
        {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT C_DECIMAL_105 FROM "
                                           + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            int v = rsmd.getScale (1);
            assertCondition (v == 5);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check when the meta data comes from a prepared statement.
    **/
    public void Var005()
    {
        if(checkJdbc20 ())
        {
            try
            {
                PreparedStatement ps = connection_.prepareStatement ("SELECT C_DECIMAL_105 FROM "
                                                                     + JDRSMDTest.RSMDTEST_GET);
                ResultSetMetaData rsmd = ps.getMetaData ();
                int v = rsmd.getScale (1);
                ps.close ();
                assertCondition (v == 5);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check a SMALLINT column. (Package cache = false)
    **/
    public void Var006()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_SMALLINT"));
            assertCondition (s == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check an INTEGER column. (Package cache = false)
    **/
    public void Var007()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_INTEGER"));
            assertCondition (s == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a REAL column. (Package cache = false)
    **/
    public void Var008()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_REAL"));
            assertCondition (s == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a FLOAT column. (Package cache = false)
    **/
    public void Var009()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_FLOAT"));
            assertCondition (s == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a FLOAT(3) column. (Package cache = false)
    **/
    public void Var010()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_FLOAT_3"));
            assertCondition (s == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a DOUBLE column. (Package cache = false)
    **/
    public void Var011()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_DOUBLE"));
            assertCondition (s == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a DECIMAL(5,0) column. (Package cache = false)
    **/
    public void Var012()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_DECIMAL_50"));
            assertCondition (s == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a DECIMAL(10,5) column. (Package cache = false)
    **/
    public void Var013()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (s == 5);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a NUMERIC(5,0) column. (Package cache = false)
    **/
    public void Var014()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_NUMERIC_50"));
            assertCondition (s == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a NUMERIC(10,5) column. (Package cache = false)
    **/
    public void Var015()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (s == 5);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a CHAR column. (Package cache = false)
    **/
    public void Var016()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_CHAR_1"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a CHAR(50) column. (Package cache = false)
    **/
    public void Var017()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_CHAR_50"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a VARCHAR(50) column. (Package cache = false)
    **/
    public void Var018()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a BINARY column. (Package cache = false)
    **/
    public void Var019()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_BINARY_1"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a BINARY(20) column. (Package cache = false)
    **/
    public void Var020()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_BINARY_20"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a VARBINARY(20) column. (Package cache = false)
    **/
    public void Var021()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a DATE column. (Package cache = false)
    **/
    public void Var022()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_DATE"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a TIME column. (Package cache = false)
    **/
    public void Var023()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_TIME"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a TIMESTAMP column. (Package cache = false)
    **/
    public void Var024()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (s == 6, "got "+s+" sb 6");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a BLOB column. (Package cache = false)
    **/
    public void Var025()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getScale (rs_.findColumn ("C_BLOB"));
                assertCondition (s == 0, "got "+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check a CLOB column. (Package cache = false)
    **/
    public void Var026()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getScale (rs_.findColumn ("C_CLOB"));
                assertCondition (s == 0, "got "+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check a DBCLOB column. (Package cache = false)
    **/
    public void Var027()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getScale (rs_.findColumn ("C_DBCLOB"));
                assertCondition (s == 0, "got "+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check a DATALINK column. (Package cache = false)
    **/
    public void Var028()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getScale (rs_.findColumn ("C_DATALINK"));
                assertCondition (s == 0, "got "+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check a DISTINCT column. (Package cache = false)
    **/
    public void Var029()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getScale (rs_.findColumn ("C_DISTINCT"));
                assertCondition (s == 2, "got "+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check a BIGINT column. (Package cache = false)
    **/
    public void Var030()
    {
        if(checkBigintSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getScale (rs_.findColumn ("C_BIGINT"));
                assertCondition (s == 0, "got "+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check column -1.  Should throw an exception.
    (Package cache = true)
    **/
    public void Var031()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getScale (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getScale() - Check column 0.  Should throw an exception.
    (Package cache = true)
    **/
    public void Var032()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getScale (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getScale() - Check a column greater than the max.
    Should throw an exception. (Package cache = true)
    **/
    public void Var033()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getScale (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getScale() - Check a SMALLINT column. (Package cache = true)
    **/
    public void Var034()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_SMALLINT"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check an INTEGER column. (Package cache = true)
    **/
    public void Var035()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_INTEGER"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a REAL column. (Package cache = true)
    **/
    public void Var036()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_REAL"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a FLOAT column. (Package cache = true)
    **/
    public void Var037()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_FLOAT"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a FLOAT(3) column. (Package cache = true)
    **/
    public void Var038()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_FLOAT_3"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a DOUBLE column. (Package cache = true)
    **/
    public void Var039()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_DOUBLE"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a DECIMAL(5,0) column. (Package cache = true)
    **/
    public void Var040()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_DECIMAL_50"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a DECIMAL(10,5) column. (Package cache = true)
    **/
    public void Var041()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (s == 5, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a NUMERIC(5,0) column. (Package cache = true)
    **/
    public void Var042()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_NUMERIC_50"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a NUMERIC(10,5) column. (Package cache = true)
    **/
    public void Var043()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (s == 5, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a CHAR column. (Package cache = true)
    **/
    public void Var044()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_CHAR_1"));
            assertCondition (s == 0, "s="+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a CHAR(50) column. (Package cache = true)
    **/
    public void Var045()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_CHAR_50"));
            assertCondition (s == 0, "s="+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a VARCHAR(50) column. (Package cache = true)
    **/
    public void Var046()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s == 0, "s="+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a BINARY column. (Package cache = true)
    **/
    public void Var047()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_BINARY_1"));
            assertCondition (s == 0, "s="+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a BINARY(20) column. (Package cache = true)
    **/
    public void Var048()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_BINARY_20"));
            assertCondition (s == 0, "s="+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a VARBINARY(20) column. (Package cache = true)
    **/
    public void Var049()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (s == 0, "s="+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a DATE column. (Package cache = true)
    **/
    public void Var050()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_DATE"));
            assertCondition (s == 0, "s="+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a TIME column. (Package cache = true)
    **/
    public void Var051()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_TIME"));
            assertCondition (s == 0, "s="+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a TIMESTAMP column. (Package cache = true)
    **/
    public void Var052()
    {
        try
        {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.getScale (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (s == 6, "s="+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getScale() - Check a BLOB column. (Package cache = true)
    **/
    public void Var053()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getScale (rs_.findColumn ("C_BLOB"));
                assertCondition (s == 0, "s="+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check a CLOB column. (Package cache = true)
    **/
    public void Var054()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getScale (rs_.findColumn ("C_CLOB"));
                assertCondition (s == 0, "s="+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check a DBCLOB column. (Package cache = true)
    **/
    public void Var055()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getScale (rs_.findColumn ("C_DBCLOB"));
                assertCondition (s == 0, "s="+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check a DATALINK column. (Package cache = true)
    **/
    public void Var056()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getScale (rs_.findColumn ("C_DATALINK"));
                assertCondition (s == 0, "s="+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check a DISTINCT column. (Package cache = true)
    **/
    public void Var057()
    {
        if(checkLobSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getScale (rs_.findColumn ("C_DISTINCT"));
                assertCondition (s == 2, "s="+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getScale() - Check a BIGINT column. (Package cache = true)
    **/
    public void Var058()
    {
        if(checkBigintSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_YES);
                int s = rsmd_.getScale (rs_.findColumn ("C_BIGINT"));
                assertCondition (s == 0, "s="+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var059()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_630"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var060()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_632"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var061()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6331"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var062()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6333"));
                if(s == 33)
                    succeeded();
                else
                    failed("Expected scale of 33, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var063()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected scale of 63, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var064()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_310"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var065()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_312"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var066()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var067()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_630"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var068()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_632"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var069()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6331"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var070()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6333"));
                if(s == 33)
                    succeeded();
                else
                    failed("Expected scale of 33, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var071()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected scale of 63, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var072()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_310"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var073()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_312"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var074()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var075()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_630"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var076()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_632"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var077()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6331"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var078()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6333"));
                if(s == 33)
                    succeeded();
                else
                    failed("Expected scale of 33, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var079()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected scale of 63, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var080()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_310"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var081()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_312"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var082()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var083()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_630"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var084()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_632"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var085()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6331"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var086()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6333"));
                if(s == 33)
                    succeeded();
                else
                    failed("Expected scale of 33, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var087()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected scale of 63, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var088()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_310"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var089()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_312"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var090()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var091()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_630"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var092()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_632"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var093()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6331"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var094()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6333"));
                if(s == 33)
                    succeeded();
                else
                    failed("Expected scale of 33, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var095()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected scale of 63, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var096()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_310"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var097()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_312"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var098()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var099()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_630"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var100()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_632"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var101()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6331"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var102()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6333"));
                if(s == 33)
                    succeeded();
                else
                    failed("Expected scale of 33, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var103()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected scale of 63, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var104()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_310"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var105()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_312"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a NUMERIC column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var106()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_NUMERIC_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var107()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_630"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var108()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_632"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var109()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6331"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var110()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6333"));
                if(s == 33)
                    succeeded();
                else
                    failed("Expected scale of 33, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var111()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected scale of 63, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var112()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_310"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var113()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_312"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = false)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var114()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_NO_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var115()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_630"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var116()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_632"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var117()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6331"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var118()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6333"));
                if(s == 33)
                    succeeded();
                else
                    failed("Expected scale of 33, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var119()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_6363"));
                if(s == 63)
                    succeeded();
                else
                    failed("Expected scale of 63, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var120()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_310"));
                if(s == 0)
                    succeeded();
                else
                    failed("Expected scale of 0, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var121()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_312"));
                if(s == 2)
                    succeeded();
                else
                    failed("Expected scale of 2, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check a DECIMAL column. (Package cache = true)
    this is checking the scale of a 63 digit column in 31 digit mode
    **/
    public void Var122()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            try
            {
                reconnect(PACKAGE_CACHE_YES_LARGE_PRECISION);
                int s = rsmd3_.getScale(rs3_.findColumn("C_DECIMAL_3131"));
                if(s == 31)
                    succeeded();
                else
                    failed("Expected scale of 31, got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getScale() - Check an XML column. (Package cache = false)
    **/
    public void Var123()
    {
        if(checkXmlSupport())
        {
            try
            {
		int expected = 0; 
                reconnect(PACKAGE_CACHE_NO);
                int s = rsmd2_.getScale(rs2_.findColumn("C_XML"));
		assertCondition ( s == expected,
				  "Expected scale of "+expected+", got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getScale() - Check an XML column. (Package cache = true)
    **/
    public void Var124()
    {
        if(checkXmlSupport())
        {
            try
            {
                int expected = 0; 
                reconnect(PACKAGE_CACHE_YES);
                int s = rsmd2_.getScale(rs2_.findColumn("C_XML"));
                assertCondition ( s == expected,
                                  "Expected scale of "+expected+", got scale of " + s);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getScale() - Check a NVARCHAR(50) column. (Package cache = false)
    **/
    public void Var125()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_NVARCHAR_50"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getScale() - Check a NCHAR(50) column. (Package cache = false)
    **/
    public void Var126()
    {
        try
        {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.getScale (rs_.findColumn ("C_NCHAR_50"));
            assertCondition (s == 0, "got "+s+" sb 0");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


   /**
    getScale() - Check a BOOLEAN column. (Package cache = false)
    **/
    public void Var127()
    {
        if(checkBooleanSupport ())
        {
            try
            {
                reconnect (PACKAGE_CACHE_NO);
                int s = rsmd_.getScale (rs_.findColumn ("C_BOOLEAN"));
                assertCondition (s == 0, "got "+s+" sb 0");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




}


