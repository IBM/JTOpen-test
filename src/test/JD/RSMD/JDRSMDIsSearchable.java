///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDIsSearchable.java
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
// File Name:    JDRSMDIsSearchable.java
//
// Classes:      JDRSMDIsSearchable
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSMDIsSearchable.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>isSearchable()
</ul>
**/
public class JDRSMDIsSearchable
extends JDTestcase
{



    // Private data.  
    private Connection          connectionExtendedMetadata_     = null;       //@F1A
    private ResultSet           rs_             = null;
    private ResultSet           rsExtendedMetadata_             = null;   //@F1A
    private ResultSetMetaData   rsmd_           = null;
    private ResultSetMetaData   rsmdExtendedMetadata_           = null;   //@F1A
    private Statement           statement_      = null;
    private Statement           statementExtendedMetadata_      = null;   //@F1A


    private ResultSet           rs2_             = null;
    private ResultSetMetaData   rsmd2_           = null;
    private Statement           statement2_      = null;


/**
Constructor.
**/
    public JDRSMDIsSearchable (AS400 systemObject,
                               Hashtable namesAndVars,
                               int runMode,
                               FileOutputStream fileOutputStream,
                               
                               String password)
    {
        super (systemObject, "JDRSMDIsSearchable",
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
        // SQL400 - driver neutral...
        String url = baseURL_
                     // String url = "jdbc:as400://" + systemObject_.getSystemName()
                     
                      ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        statement_ = connection_.createStatement ();
        rs_ = statement_.executeQuery ("SELECT * FROM "
                                       + JDRSMDTest.RSMDTEST_GET);
        rsmd_ = rs_.getMetaData ();

        statement2_ = connection_.createStatement ();
        rs2_ = statement2_.executeQuery ("SELECT * FROM "
                                       + JDRSMDTest.RSMDTEST_GET2);
        rsmd2_ = rs2_.getMetaData ();


        //@F1A
        String url2 = baseURL_
                      + ";extended metadata=true";
        connectionExtendedMetadata_ = testDriver_.getConnection (url2,systemObject_.getUserId(), encryptedPassword_);
        statementExtendedMetadata_ = connectionExtendedMetadata_.createStatement ();
        rsExtendedMetadata_ = statementExtendedMetadata_.executeQuery ("SELECT * FROM "
                                         + JDRSMDTest.RSMDTEST_GET);
        rsmdExtendedMetadata_ = rsExtendedMetadata_.getMetaData ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        rs_.close ();
        rs2_.close ();
        rsExtendedMetadata_.close();         //@F1A
        statementExtendedMetadata_.close();  //@F1A
        statement_.close ();
        statement2_.close ();
        connectionExtendedMetadata_.close(); //@F1A
        connection_.close ();
    }

    protected void cleanupConnection ()
    throws Exception
    {
        connectionExtendedMetadata_.close(); //@F1A
        connection_.close ();
    }



/**
isSearchable() - Check column -1.  Should throw an exception.
**/
    public void Var001()
    {
        try {
            rsmd_.isSearchable (-1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isSearchable() - Check column 0.  Should throw an exception.
**/
    public void Var002()
    {
        try {
            rsmd_.isSearchable (0);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isSearchable() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            rsmd_.isSearchable (35);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isSearchable() - Check when the result set is closed.
**/
    public void Var004()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT C_SMALLINT FROM "
                                           + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            boolean b = rsmd.isSearchable (1);
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check when the meta data is from a prepared statement.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps;
		try { 
		    ps = connection_.prepareStatement ("SELECT C_SMALLINT FROM "
						       + JDRSMDTest.RSMDTEST_GET, ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
		} catch (Exception e) {
		    ps = connection_.prepareStatement ("SELECT C_SMALLINT FROM "
						       + JDRSMDTest.RSMDTEST_GET);

		}

                ResultSetMetaData rsmd = ps.getMetaData ();
                boolean b = rsmd.isSearchable (1);
                ps.close ();
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a SMALLINT column.
**/
    public void Var006()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_SMALLINT"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check an INTEGER column.
**/
    public void Var007()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_INTEGER"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a REAL column.
**/
    public void Var008()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_REAL"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a FLOAT column.
**/
    public void Var009()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_FLOAT"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a DOUBLE column.
**/
    public void Var010()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_DOUBLE"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a DECIMAL column.
**/
    public void Var011()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a NUMERIC column.
**/
    public void Var012()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a CHAR column.
**/
    public void Var013()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_CHAR_50"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a VARCHAR column.
**/
    public void Var014()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a BINARY column.
**/
    public void Var015()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_BINARY_20"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a VARBINARY column.
**/
    public void Var016()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a DATE column.
**/
    public void Var017()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_DATE"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a TIME column.
**/
    public void Var018()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_TIME"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a TIMESTAMP column.
**/
    public void Var019()
    {
        try {
            boolean b = rsmd_.isSearchable (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a BLOB column.
**/
    public void Var020()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isSearchable (rs_.findColumn ("C_BLOB"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a CLOB column.
**/
    public void Var021()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isSearchable (rs_.findColumn ("C_CLOB"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a DBCLOB column.
**/
    public void Var022()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isSearchable (rs_.findColumn ("C_DBCLOB"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a DATALINK column.
**/
    public void Var023()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isSearchable (rs_.findColumn ("C_DATALINK"));
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    assertCondition (b == false, "Got "+b+" expected false from DATALINK for JTOPENlite");
		} else { 
		    assertCondition (b == true, "Got "+b+" expected true from DATALINK");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a DISTINCT column.
**/
    public void Var024()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isSearchable (rs_.findColumn ("C_DISTINCT"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a BIGINT column.
**/
    public void Var025()
    {
        if (checkBigintSupport ()) {
            try {
                boolean b = rsmd_.isSearchable (rs_.findColumn ("C_BIGINT"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



//@F1A  New testcases to test "extended metadata" property.
/**
isSearchable() - Check column -1.  Should throw an exception.
**/
    public void Var026()
    {
        try {
            rsmdExtendedMetadata_.isSearchable (-1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isSearchable() - Check column 0.  Should throw an exception.
**/
    public void Var027()
    {
        try {
            rsmdExtendedMetadata_.isSearchable (0);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isSearchable() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var028()
    {
        try {
            rsmdExtendedMetadata_.isSearchable (35);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isSearchable() - Check when the result set is closed.
**/
    public void Var029()
    {
        try {
            Statement s = connectionExtendedMetadata_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT C_SMALLINT FROM "
                                           + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            boolean b = rsmd.isSearchable (1);
            assertCondition (b == true, "true not returned when result set is closed");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check when the meta data is from a prepared statement.
**/
    public void Var030()
    {
        if (checkJdbc20 ()) {
            try {

                PreparedStatement ps;
		try { 
		ps = connectionExtendedMetadata_.prepareStatement ("SELECT C_SMALLINT FROM "
                                                                     + JDRSMDTest.RSMDTEST_GET, ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                     ResultSet.CONCUR_READ_ONLY);
		} catch (Exception e) {

		    ps = connectionExtendedMetadata_.prepareStatement ("SELECT C_SMALLINT FROM "
								       + JDRSMDTest.RSMDTEST_GET); 
		} 
                ResultSetMetaData rsmd = ps.getMetaData ();
                boolean b = rsmd.isSearchable (1);
                ps.close ();
                assertCondition (b == true, "true not returned when metadata is from prepared statement" );
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a SMALLINT column.
**/
    public void Var031()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_SMALLINT"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check an INTEGER column.
**/
    public void Var032()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_INTEGER"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a REAL column.
**/
    public void Var033()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_REAL"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a FLOAT column.
**/
    public void Var034()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_FLOAT"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a DOUBLE column.
**/
    public void Var035()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_DOUBLE"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a DECIMAL column.
**/
    public void Var036()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_DECIMAL_105"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a NUMERIC column.
**/
    public void Var037()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_NUMERIC_105"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a CHAR column.
**/
    public void Var038()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_CHAR_50"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a VARCHAR column.
**/
    public void Var039()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_VARCHAR_50"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a BINARY column.
**/
    public void Var040()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_BINARY_20"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a VARBINARY column.
**/
    public void Var041()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_VARBINARY_20"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a DATE column.
**/
    public void Var042()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_DATE"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a TIME column.
**/
    public void Var043()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_TIME"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a TIMESTAMP column.
**/
    public void Var044()
    {
        try {
            boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_TIMESTAMP"));
            assertCondition (b == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSearchable() - Check a BLOB column.
**/
    public void Var045()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_BLOB"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a CLOB column.
**/
    public void Var046()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_CLOB"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a DBCLOB column.
**/
    public void Var047()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_DBCLOB"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - DATALINK column are not searchable.  

This is the only case I know of that isSearchable should return false with our new "extended
metadata" property, i.e.-this is the only example I know of where our new support should
return anything differently than the old support.
**/
    public void Var048()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_DATALINK"));
                assertCondition (b == false, "isSearchable returned "+b+" sb false");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a DISTINCT column.
**/
    public void Var049()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_DISTINCT"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check a BIGINT column.
**/
    public void Var050()
    {
        if (checkBigintSupport ()) {
            try {
                boolean b = rsmdExtendedMetadata_.isSearchable (rsExtendedMetadata_.findColumn ("C_BIGINT"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


//@F1A Test with "extended metadata=false".

/**
isSearchable() - Make sure "extended metadata=false" returns old value.
**/
    public void Var051()
    {
        if (checkBigintSupport ()) {
            try {
                Connection connection3 = testDriver_.getConnection(baseURL_
                                                                   + ";extended metadata=false");

                // Get an updatable result set from a query.
                Statement statement = connection3.createStatement ();
                ResultSet rs = statement.executeQuery ("SELECT * FROM "
                                                       + JDRSMDTest.RSMDTEST_GET);
                ResultSetMetaData rsmd = rs.getMetaData ();

                boolean b = rsmd.isSearchable (rs.findColumn ("C_DATALINK"));
		connection3.close(); 
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSearchable() - Check an XML column.
**/
    public void Var052()
    {
        if (checkXmlSupport ()) {
            try {
                boolean b = rsmd2_.isSearchable (rs2_.findColumn ("C_XML"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
isSearchable() - Check a BOOLEAN column.
**/
    public void Var053()
    {
        if (checkBooleanSupport ()) {
            try {
                boolean b = rsmd_.isSearchable (rs_.findColumn ("C_BOOLEAN"));
                assertCondition (b == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }





}



