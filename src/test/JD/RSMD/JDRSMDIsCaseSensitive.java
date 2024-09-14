///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDIsCaseSensitive.java
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
// File Name:    JDRSMDIsCaseSensitive.java
//
// Classes:      JDRSMDIsCaseSensitive
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSMDIsCaseSensitive.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>isCaseSensitive()
</ul>
**/
public class JDRSMDIsCaseSensitive
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDIsCaseSensitive";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }



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
    public JDRSMDIsCaseSensitive (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDIsCaseSensitive",
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
isCaseSensitive() - Check column -1.  Should throw an exception.
**/
    public void Var001()
    {
        try {
            rsmd_.isCaseSensitive (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isCaseSensitive() - Check column 0.  Should throw an exception.
**/
    public void Var002()
    {
        try {
            rsmd_.isCaseSensitive (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isCaseSensitive() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            rsmd_.isCaseSensitive (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isCaseSensitive() - Check when the result set is closed.
**/
    public void Var004()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT C_SMALLINT FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            boolean b = rsmd.isCaseSensitive (1);
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check when the meta data is from a prepared statement.
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
                boolean b = rsmd.isCaseSensitive (1);
                ps.close ();
                assertCondition (b == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isCaseSensitive() - Check a SMALLINT column.
**/
    public void Var006()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_SMALLINT"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check an INTEGER column.
**/
    public void Var007()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_INTEGER"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a REAL column.
**/
    public void Var008()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_REAL"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a FLOAT column.
**/
    public void Var009()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_FLOAT"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a DOUBLE column.
**/
    public void Var010()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_DOUBLE"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a DECIMAL column.
**/
    public void Var011()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a NUMERIC column.
**/
    public void Var012()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a CHAR column.
**/
    public void Var013()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_CHAR_50"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a VARCHAR column.
**/
    public void Var014()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a BINARY column.
**/
    public void Var015()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_BINARY_20"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a VARBINARY column.
**/
    public void Var016()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (b == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a DATE column.
**/
    public void Var017()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_DATE"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a TIME column.
**/
    public void Var018()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_TIME"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a TIMESTAMP column.
**/
    public void Var019()
    {
        try {
            boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (b == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isCaseSensitive() - Check a BLOB column.
**/
    public void Var020()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_BLOB"));
                assertCondition (b == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isCaseSensitive() - Check a CLOB column.
**/
    public void Var021()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_CLOB"));
                assertCondition (b == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isCaseSensitive() - Check a DBCLOB column.
**/
    public void Var022()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_DBCLOB"));
                assertCondition (b == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isCaseSensitive() - Check a DATALINK column.
**/
    public void Var023()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_DATALINK"));
                assertCondition (b == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isCaseSensitive() - Check a DISTINCT column.
**/
    public void Var024()
    {
        if (checkLobSupport ()) {
            try {
                boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_DISTINCT"));
                assertCondition (b == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isCaseSensitive() - Check a BIGINT column.
**/
    public void Var025()
    {
        if (checkBigintSupport ()) {
            try {
                boolean b = rsmd_.isCaseSensitive (rs_.findColumn ("C_BIGINT"));
                assertCondition (b == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
isCaseSensitive() - Check a XML column.
**/
    public void Var026()
    {
        if (checkXmlSupport ()) {
            try {
                boolean b = rsmd2_.isCaseSensitive (rs2_.findColumn ("C_XML"));
                assertCondition (b == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



}



