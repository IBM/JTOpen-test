///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDIsSigned.java
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
// File Name:    JDRSMDIsSigned.java
//
// Classes:      JDRSMDIsSigned
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
import java.util.Hashtable;



/**
Testcase JDRSMDIsSigned.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>isSigned()
</ul>
**/
public class JDRSMDIsSigned
extends JDTestcase
{



    // Private data.
    private ResultSet           rs_             = null;
    private ResultSetMetaData   rsmd_           = null;
    private Statement           statement_      = null;

    private ResultSet           rs2_             = null;
    private ResultSetMetaData   rsmd2_           = null;
    private Statement           statement2_      = null;



/**
Constructor.
**/
    public JDRSMDIsSigned (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDIsSigned",
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
        rs2_.close ();
        statement2_.close ();
        connection_.close ();
    }



/**
isSigned() - Check column -1.  Should throw an exception.
**/
    public void Var001()
    {
        try {
            rsmd_.isSigned (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isSigned() - Check column 0.  Should throw an exception.
**/
    public void Var002()
    {
        try {
            rsmd_.isSigned (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isSigned() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            rsmd_.isSigned (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
isSigned() - Check when the result set is closed.
**/
    public void Var004()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT C_SMALLINT FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            boolean b = rsmd.isSigned (1);
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check when the meta data comes from a prepared statement.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
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
                boolean b = rsmd.isSigned (1);
                ps.close ();
                assertCondition (b == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isSigned() - Check a SMALLINT column.
**/
    public void Var006()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_SMALLINT"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check an INTEGER column.
**/
    public void Var007()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_INTEGER"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a REAL column.
**/
    public void Var008()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_REAL"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a FLOAT column.
**/
    public void Var009()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_FLOAT"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a DOUBLE column.
**/
    public void Var010()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_DOUBLE"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a DECIMAL column.
**/
    public void Var011()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a NUMERIC column.
**/
    public void Var012()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a CHAR column.
**/
    public void Var013()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_CHAR_50"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a VARCHAR column.
**/
    public void Var014()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a BINARY column.
**/
    public void Var015()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_BINARY_20"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a VARBINARY column.
**/
    public void Var016()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a DATE column.
**/
    public void Var017()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_DATE"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a TIME column.
**/
    public void Var018()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_TIME"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a TIMESTAMP column.
**/
    public void Var019()
    {
        try {
            boolean b = rsmd_.isSigned (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isSigned() - Check a BLOB column.
**/
    public void Var020()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isSigned (rs_.findColumn ("C_BLOB"));
                assertCondition (b == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSigned() - Check a CLOB column.
**/
    public void Var021()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isSigned (rs_.findColumn ("C_CLOB"));
                assertCondition (b == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSigned() - Check a DBCLOB column.
**/
    public void Var022()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isSigned (rs_.findColumn ("C_DBCLOB"));
                assertCondition (b == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSigned() - Check a DATALINK column.
**/
    public void Var023()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isSigned (rs_.findColumn ("C_DATALINK"));
                assertCondition (b == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSigned() - Check a DISTINCT column.
**/
    public void Var024()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isSigned (rs_.findColumn ("C_DISTINCT"));
                assertCondition (b == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



// @D0A
/**
isSigned() - Check a BIGINT column.
**/
    public void Var025()
    {
        if (checkBigintSupport ()) {
            try {
                boolean b = rsmd_.isSigned (rs_.findColumn ("C_BIGINT"));
                assertCondition (b == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
isSigned() - Check an XML column.
**/
    public void Var026()
    {
        if (checkXmlSupport ()) {
            try {
                boolean b = rsmd2_.isSigned (rs2_.findColumn ("C_XML"));
                assertCondition (b == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isSigned() - Check a BOOLEAN column.
**/
    public void Var027()
    {
        if (checkBooleanSupport ()) {
            try {
                boolean b = rsmd_.isSigned (rs_.findColumn ("C_BOOLEAN"));
                assertCondition (b == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


}



