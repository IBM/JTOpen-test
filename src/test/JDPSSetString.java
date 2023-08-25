///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetString.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSSetString.java
//
// Classes:      JDPSSetString
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Random;
import java.math.BigDecimal;


/**
Testcase JDPSSetString.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDPSSetString
extends JDTestcase {



    // Private data.
    private Connection          connection_;
    private Statement           statement_;
    private String              properties_;

/**
Constructor.
**/
    public JDPSSetString (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDPSSetString",
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
        properties_ = "X";
        reconnect ("");
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        statement_.close ();
        connection_.close ();
    }



/**
Reconnects with different properties, if needed.
**/
    private void reconnect (String properties)
    throws Exception
    {
        if (! properties_.equals (properties)) {
            properties_ = properties;
            if (connection_ != null)
                cleanup ();

            String url = baseURL_
                         
                         
                         + ";data truncation=true"
                         + ";" + properties_;
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            statement_ = connection_.createStatement ();
        }
    }



/**
setString() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER) VALUES (?)");
            ps.close ();
            ps.setString (1, "Test");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setString (100, "Test");
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setString (0, "Test");
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setString (0, "Test");
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Should set to SQL NULL when the value is null.
**/
    public void Var005()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setString (1, null);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition ((check == null) && (wn == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setString() - Should work with a valid parameter index
greater than 1.
**/
    public void Var006()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_KEY, C_VARCHAR_50) VALUES (?, ?)");
            ps.setString (1, "Muchas");
            ps.setString (2, "Gracias");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Gracias"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setString() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var007()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            ps.setString (2, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Verify that a data truncation exception is
posted when data is truncated.
**/
    public void Var008()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setString (1, "This string is a little bit longer than fifty characters.");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (DataTruncation dt) {
            assertCondition ((dt.getIndex() == 1)
                    && (dt.getParameter() == true)
                    && (dt.getRead() == false)
                    && (dt.getDataSize() == 57)
                    && (dt.getTransferSize() == 50), "DataTruncation "+dt.getIndex()+" "+dt.getParameter()+" "+dt.getRead()+" "+dt.getDataSize()+" "+dt.getTransferSize()+" "+dt.getRead());
        }
        catch (Exception e) {

	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		String exMessage = e.toString();
		String expectedMessage = "-302";
		assertCondition(exMessage.indexOf(expectedMessage) > 0,
				"Expected message '"+expectedMessage+"' not found in "+
				exMessage);
	    } else {
		failed (e, "Unexpected Exception");
	    }
        }
    }





/**
setString() - Set a SMALLINT parameter.
**/
    public void Var009()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_SMALLINT) VALUES (?)");
            ps.setString (1, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Set a INTEGER parameter.
**/
    public void Var010()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER) VALUES (?)");
            ps.setString (1, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Set a REAL parameter.
**/
    public void Var011()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_REAL) VALUES (?)");
            ps.setString (1, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Set a FLOAT parameter.
**/
    public void Var012()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_FLOAT) VALUES (?)");
            ps.setString (1, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Set a DOUBLE parameter.
**/
    public void Var013()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DOUBLE) VALUES (?)");
            ps.setString (1, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Set a DECIMAL parameter.
**/
    public void Var014()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setString (1, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Set a NUMERIC parameter.
**/
    public void Var015()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_50) VALUES (?)");
            ps.setString (1, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Set a CHAR(1) parameter, where the string is
longer.
**/
    public void Var016()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_1) VALUES (?)");
            ps.setString (1, "L");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("L"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
setString() - Set a CHAR(50) parameter.
**/
    public void Var017()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_50) VALUES (?)");
            ps.setString (1, "PROFS");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("PROFS                                             "));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setString() - Set a VARCHAR(50) parameter.
**/
    public void Var018()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setString (1, "Borland JBuilder");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Borland JBuilder"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setString() - Set a CLOB parameter.
**/
    public void Var019()
    {
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_CLOB) VALUES (?)");
                ps.setString (1, "Test");
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("Test"), "check('"+check+"') != 'TEST'");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
setString() - Set a DBCLOB parameter.
**/
    public void Var020()
    {
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DBCLOB) VALUES (?)");
                ps.setString (1, "Test double byte");
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DBCLOB FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();
                assertCondition (check.equals ("Test double byte"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setString() - Set a BINARY parameter, with translation turned on.

SQL400 - Close to the right value returned but difference in trailing
         spaces needs to get dealt with.
**/
    public void Var021()
    {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}
        try {
            reconnect ("translate binary=true");
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BINARY_20) VALUES (?)");
            ps.setString (1, "Symantec Cafe");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            if (isToolboxDriver())
            {
                if(check.equals("Symantec Cafe       "))
                    succeeded();
                else
                    failed("Strings did not match. \"Symantec Cafe       \" != " + check);
            }
            else
                // Spaces get translated different, so we kluge this
                // comparison.
                assertCondition (check.substring (0, 13).equals ("Symantec Cafe")
                        && (check.length() == 20));

        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
setString() - Set a VARBINARY parameter, with translation turned on.
**/
    public void Var022()
    {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}
        try {
            reconnect ("translate binary=true");
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            ps.setString (1, "Visual J++");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            if(check.equals("Visual J++"))
                succeeded();
            else
                failed("Strings did not match. \"Visual J++\" != " + check);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
setString() - Set a BINARY parameter, with translation turned off.
**/
    public void Var023()
    {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}
        try {
            reconnect ("translate binary=false");
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BINARY_20) VALUES (?)");

            String expected = null;

            if (isToolboxDriver())
               expected = "4869204D6F6D";   // Hi Mom in hex
            else
               expected = "Symantec Cafe";

            ps.setString (1, expected);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            // Spaces get translated different, so we kluge this
            // comparison.
            if (isToolboxDriver())
               assertCondition (check.startsWith(expected));
            else
               assertCondition ((check.startsWith ("Symantec Cafe"))
                       && (check.length() == 20));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
setString() - Set a VARBINARY parameter, with translation turned off.
**/
    public void Var024()
    {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}
        try {
            String expected = null;

            if (isToolboxDriver())
               expected = "4869204D6F6D";   // Hi Mom in hex
            else
               expected = "Visual J++";

            reconnect ("translate binary=false");
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            ps.setString (1, expected);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals (expected));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
setString() - Set a BLOB parameter.
**/
    public void Var025()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BLOB) VALUES (?)");
            ps.setString (1, "Test");
            ps.execute();
            ps.close();
            failed("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setString() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. D1C
**/
    public void Var026()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DATE) VALUES (?)");
            ps.setString (1, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. D1C.
**/
    public void Var027()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIME) VALUES (?)");
            ps.setString (1, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setString() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
**/
    public void Var028()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIMESTAMP) VALUES (?)");
            ps.setString (1, "Test");
	    ps.executeUpdate();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
setString() - Set a DATALINK parameter.
**/
    // @C0C
    public void Var029()
    {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}

        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(50))))");
                ps.setString (1, "http://www.pepsi.com/index.html");
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
		if (check == null) check = "null";
                rs.close ();

                assertCondition (check.equalsIgnoreCase("http://www.pepsi.com/index.html"), "got "+check+" expected http://www.pepsi.com/index.html" );
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setString() - Set a DISTINCT parameter.
**/
    // @C0C
    public void Var030()
    {
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DISTINCT) VALUES (?)");
                ps.setString (1, "12345678");
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("12345678"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setString() - Set a BIGINT parameter.
**/
    public void Var031()
    {
        if (checkBigintSupport()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BIGINT) VALUES (?)");
                ps.setString (1, "Test");
		ps.executeUpdate();
                ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setString() - Set strings in a table that has columns
with different CCSIDs.
**/
    public void Var032()
    {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}
        try {
            statement_.executeUpdate ("CREATE TABLE "
                                      + JDPSTest.COLLECTION + ".CCSIDFUN "
                                      + "(COL1 VARCHAR(20) CCSID 37, "
                                      + " COL2 VARCHAR(20) CCSID " + systemObject_.getCcsid() + ", "
                                      + " COL3 VARCHAR(20) CCSID 937)");

            statement_.executeUpdate ("DELETE FROM "
                                      + JDPSTest.COLLECTION + ".CCSIDFUN ");

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.COLLECTION + ".CCSIDFUN "
                                                                + "(COL1, COL2, COL3) VALUES (?, ?, ?)");
            ps.setString (1, "Bernard");
            ps.setString (2, "The");
            ps.setString (3, "Cat");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDPSTest.COLLECTION + ".CCSIDFUN");
            rs.next ();
            String col1 = rs.getString (1);
            String col2 = rs.getString (2);
            String col3 = rs.getString (3);
            rs.close ();

            assertCondition ((col1.equals ("Bernard"))
                    && (col2.equals ("The"))
                    && (col3.equals ("Cat")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally {
            try {
                statement_.executeUpdate ("DROP TABLE "
                                          + JDPSTest.COLLECTION + ".CCSIDFUN ");
            }
            catch (SQLException e) {
                // Ignore.
            }
        }
    }



/**
setString() - Set a VARCHAR(50) parameter.
**/
    public void Var033()
    {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}
        try {
            if (isToolboxDriver()){
                reconnect ("extended dynamic=true;package=PSTEST"
                        + ";package library=" + JDPSTest.COLLECTION
                        + ";package cache=false");
            }else{
            reconnect ("extended dynamic=true;package=PSTEST"
                       + ";package library=" + JDPSTest.COLLECTION
                       + ";package cache=true");
            }

            String sql = "INSERT INTO " + JDPSTest.PSTEST_SET
                         + " (C_VARCHAR_50) VALUES (?)";


            if (isToolboxDriver())
                JDSetupPackage.prime (systemObject_,
                                      "PSTEST", JDPSTest.COLLECTION, sql);
            else
                JDSetupPackage.prime (systemObject_, encryptedPassword_,
                                      "PSTEST", JDPSTest.COLLECTION, sql, "", getDriver());


            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (sql);
            ps.setString (1, "Borland JBuilder");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Borland JBuilder"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/** D1A
setString() - Set a DATE parameter to a value that is a valid date.  This should work.
**/
    public void Var034()
    {
        if (checkNative()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATE) VALUES (?)");
                ps.setString (1, "1999-12-31");
                ps.executeUpdate();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("1999-12-31"));

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/** D1A
setString() - Set a TIME parameter to a value that is valid.  This should work.
**/
    public void Var035()
    {
        if (checkNative()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_TIME) VALUES (?)");
                ps.setString (1, "14:04:55");
                ps.executeUpdate();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                // Iso format is the default for CLI and JDBC.
                assertCondition (check.equals ("14.04.55"));

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/** D1A
setString() - Set a TIMESTAMP parameter to a value that is valid.  This should work.
**/
    public void Var036()
    {
        if (checkNative()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_TIMESTAMP) VALUES (?)");
                ps.setString (1, "1998-04-08-03.15.30.123456");
                ps.executeUpdate();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_TIMESTAMP FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("1998-04-08 03:15:30.123456"));   /* @E1C */

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/** D1A
setString() - Should set to SQL NULL when the value is null.  This test uses a Statement without
truncation turned on because we use different internal paths.
**/
    public void Var037()
    {
        if (checkNative()) {
            try {
                String url = baseURL_
                             
                              + ";";

                Connection connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
                Statement statement = connection_.createStatement ();

                statement.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection.prepareStatement (
                                                                   "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                   + " (C_VARCHAR_50) VALUES (?)");
                ps.setString (1, null);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                boolean wn = rs.wasNull ();

                rs.close ();
                statement.close();
                connection.close();

                assertCondition ((check == null) && (wn == true));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setString() - make sure pad is correct for graphic fields
**/
    public void Var038()
    {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Graphic test not applicable for JCC");
	    return;
	}

        String tableName = JDPSTest.COLLECTION + ".GRAPHIC_PAD";
        try
        {

            try { statement_.executeUpdate("drop table " + tableName); } catch (Exception e) {}
            if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
              statement_.executeUpdate ("CREATE TABLE "
                  + tableName
                  + " (COL1 CHAR(4) CCSID 37, "          // padded with single byte space
                  + "  COL2 GRAPHIC(4) CCSID 13488, "    // padded with single byte space
                  + "  COL3 GRAPHIC(4) CCSID 1200)");     // padded with double byte space

            } else {
            statement_.executeUpdate ("CREATE TABLE "
                                      + tableName
                                      + " (COL1 CHAR(4) CCSID 37, "          // padded with single byte space
                                      + "  COL2 GRAPHIC(4) CCSID 13488, "    // padded with single byte space
                                      + "  COL3 GRAPHIC(4) CCSID 835)");     // padded with double byte space
            }

            PreparedStatement ps = connection_.prepareStatement (
                                   "INSERT INTO " + tableName +
                                   "(COL1, COL2, COL3) VALUES (?, ?, ?)");
            ps.setString (1, "AA");
            ps.setString (2, "BB");
            ps.setString (3, "\u6f22\u5b57");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tableName);
            rs.next ();
            String col1 = rs.getString (1);
            String col2 = rs.getString (2);
            String col3 = rs.getString (3);
            rs.close ();


            if (! col1.equals("AA  "))
               failed("Col1 not padded correctly.  Expected 'AA  ', received ->>" + col1 + "<--");
            else
            {
               char[] ch = col2.toCharArray();
               int digit1 = 0x00FFFF & ch[0];
               int digit2 = 0x00FFFF & ch[1];
               int digit3 = 0x00FFFF & ch[2];
               int digit4 = 0x00FFFF & ch[3];

               if ((digit1 == 0x42) &&   // 66 = x42 is a "B" in unicode
                   (digit2 == 0x42) &&
                   (digit3 == 0x20) &&   // 32 = x20 which is a space
                   (digit4 == 0x20))
               {
		   char expectedSpace = '\u3000';
		   if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		       expectedSpace = ' '; 
		   } 
                  ch = col3.toCharArray();
                  digit1 = 0x00FFFF & ch[0];
                  digit2 = 0x00FFFF & ch[1];
                  digit3 = 0x00FFFF & ch[2];
                  digit4 = 0x00FFFF & ch[3];

                  if ((digit1 == 0x6F22) &&   // get original data back?
                      (digit2 == 0x5B57) &&   // get original data back?
                      (digit3 == expectedSpace) &&   // get double byte space (0x3000)?
                      (digit4 == expectedSpace))     // get double byte space (0x3000)?
                  {
                     succeeded();
                  }
                  else
                     failed("Col3 not padded correctly.  Expected 'AA"+expectedSpace+expectedSpace+"', received " + Integer.toHexString(digit1) + " "+Integer.toHexString(digit2) + " "+Integer.toHexString(digit3) + " "+Integer.toHexString(digit4));
               }
               else
                  failed("Col2 not padded correctly.  Expected 'AA\u0020\u0020', received " + " "+Integer.toHexString(digit1) + " "+Integer.toHexString(digit2) + " "+Integer.toHexString(digit3) + " "+Integer.toHexString(digit4));
            }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally
        {
            try { statement_.executeUpdate ("DROP TABLE " + tableName); } catch (SQLException e) { }
        }
    }

/**
setString() - make sure pad is correct for graphic fields
**/
    public void Var039()
    {

	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}

        if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)                                     //@G1A
        {                                                                                   //@G1A
            String tableName = JDPSTest.COLLECTION + ".GRAPHIC_PAD";
            try
            {

                try { statement_.executeUpdate("drop table " + tableName); } catch (Exception e) {}
                if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
                  statement_.executeUpdate ("CREATE TABLE "
                      + tableName
                      + " (COL1 CHAR(4) CCSID 1208, "        // padded with single byte space
                      + "  COL2 GRAPHIC(4) CCSID 1200, "    // padded with single byte space
                      + "  COL3 GRAPHIC(4) CCSID 1200)");     // padded with double byte space

                } else {
                statement_.executeUpdate ("CREATE TABLE "
                                          + tableName
                                          + " (COL1 CHAR(4) CCSID 1208, "        // padded with single byte space
                                          + "  COL2 GRAPHIC(4) CCSID 1200, "    // padded with single byte space
                                          + "  COL3 GRAPHIC(4) CCSID 835)");     // padded with double byte space
                }

                PreparedStatement ps = connection_.prepareStatement (
                                       "INSERT INTO " + tableName +
                                       "(COL1, COL2, COL3) VALUES (?, ?, ?)");
                ps.setString (1, "AA");
                ps.setString (2, "BB");
                ps.setString (3, "\u6f22\u5b57");
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tableName);
                rs.next ();
                String col1 = rs.getString (1);
                String col2 = rs.getString (2);
                String col3 = rs.getString (3);
                rs.close ();


                if (! col1.equals("AA  "))
                   failed("Col1 not padded correctly.  Expected 'AA  ', received ->>" + col1 + "<--");
                else
                {
                   char[] ch = col2.toCharArray();
                   int digit1 = 0x00FFFF & ch[0];
                   int digit2 = 0x00FFFF & ch[1];
                   int digit3 = 0x00FFFF & ch[2];
                   int digit4 = 0x00FFFF & ch[3];

                   if ((digit1 == 0x42) &&   // 66 = x42 is a "B" in unicode
                       (digit2 == 0x42) &&
                       (digit3 == 0x20) &&   // 32 = x20 which is a space
                       (digit4 == 0x20))
                    {
		       char expectedSpace = '\u3000';
		       if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
			   expectedSpace = ' '; 
		       } 

                      ch = col3.toCharArray();
                      digit1 = 0x00FFFF & ch[0];
                      digit2 = 0x00FFFF & ch[1];
                    digit3 = 0x00FFFF & ch[2];
                    digit4 = 0x00FFFF & ch[3];

                      if ((digit1 == 0x6F22) &&   // get original data back?
                          (digit2 == 0x5B57) &&   // get original data back?
                          (digit3 == expectedSpace) &&   // get double byte space (0x3000)?
                          (digit4 == expectedSpace))     // get double byte space (0x3000)?
                      {
                         succeeded();
                      }
                      else
                         failed("Col3 not padded correctly.  Expected 'AA"+expectedSpace+expectedSpace+"', received " + " "+Integer.toHexString(digit1) + " "+Integer.toHexString(digit2) + " "+Integer.toHexString(digit3) + " "+Integer.toHexString(digit4));
                   }
                   else
                      failed("Col2 not padded correctly.  Expected 'AA\u0020\u0020', received " + " "+Integer.toHexString(digit1) + " "+Integer.toHexString(digit2) + " "+Integer.toHexString(digit3) + " "+Integer.toHexString(digit4));
                }
            }

            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
            finally
            {
                try { statement_.executeUpdate ("DROP TABLE " + tableName); } catch (SQLException e) { }
            }
        }
        else                                                                            //@G1A
            notApplicable("V5R3 or higher variation.");                                 //@G1A
    }

    public String formatUnicode(String inString) {
	String outString="";
        //
        // See if all the characters are 7 bit ASCII.. If so just print
        //
	if (inString != null) {
	    char chars[] = inString.toCharArray();
	    boolean nonAsciiFound = false;
	    for (int i = 0; !nonAsciiFound && i < chars.length; i++) {
		if ( chars[i] != 0x0d && chars[i] != 0x0a && chars[i] != 0x09 && (chars[i] >= 0x7F || chars[i] < 0x20) ) {
		    nonAsciiFound = true;
		}
	    }
	    if (! nonAsciiFound) {
		return(inString);
	    } else {
		outString+="U'";
		for (int i = 0; i < chars.length; i++) {
		    int showInt = chars[i] & 0xFFFF;

		    String showString = Integer.toHexString(showInt);
		    if (showInt >= 0x1000) {
			outString += showString;
		    } else if (showInt >= 0x0100) {
			outString += "0"+showString;
		    } else if (showInt >= 0x0010) {
			outString += "00"+showString;
		    } else {
			outString += "000"+showString;
		    }

		}
		outString += "'";
		return(outString);
	    }
	} else {
	    return inString;
	}

    } /* formatUnicode */

/**
setString() - make sure UTF-8 parameter can be passed
**/
    public void Var040() {

	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}

	if(getRelease() >= JDTestDriver.RELEASE_V5R3M0) {
            String tableName = JDPSTest.COLLECTION + ".UTF8";
            try  {
		try { statement_.executeUpdate("drop table " + tableName); } catch (Exception e) {}
                statement_.executeUpdate ("CREATE TABLE " + tableName
                                          + " (COL1 VARCHAR(30000) CCSID 1208)");
                PreparedStatement ps = connection_.prepareStatement (
                                       "INSERT INTO " + tableName +
                                       "(COL1) VALUES (?)");
		String insertString = "\u05D0";
                ps.setString (1, insertString );
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tableName);
                String col1;
                if (rs.next ()) {
                 col1 = rs.getString (1);
                } else {
                  col1 = "NO DATA FETCHED";
                }
                rs.close ();

		assertCondition(col1.equals(insertString), "retrieved value("+formatUnicode(col1)+") != expected("+formatUnicode(insertString)+") -- Added 10/21/03 by native driver");

            } catch (Exception e) {
                failed (e, "Unexpected Exception -- Added 10/21/03 by native driver");
            } finally {
                try { statement_.executeUpdate ("DROP TABLE " + tableName); } catch (SQLException e) { }
            }
	} else {
            notApplicable("V5R3 or higher variation.");
	}
    }


/**
setString() - make sure EURO symbol parameter can be passed and retrieved
            - This does not work for native driver (pre v5r3).
**/
    public void Var041() {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}

	if(getRelease() >= JDTestDriver.RELEASE_V5R3M0) {
            String tableName = JDPSTest.COLLECTION + ".EURO";
            try  {
		try { statement_.executeUpdate("drop table " + tableName); } catch (Exception e) {}
                statement_.executeUpdate ("CREATE TABLE " + tableName
                                          + " (COL1 VARCHAR(30000) CCSID 1140)");
                PreparedStatement ps = connection_.prepareStatement (
                                       "INSERT INTO " + tableName +
                                       "(COL1) VALUES (?)");
		String insertString = "\u20AC";
                ps.setString (1, insertString );
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tableName);
                rs.next ();
                String col1 = rs.getString (1);
                rs.close ();

		assertCondition(col1.equals(insertString), "retrieved value("+formatUnicode(col1)+") != expected("+formatUnicode(insertString)+") -- Added 11/05/03 by native driver");

            } catch (Exception e) {
                failed (e, "Unexpected Exception -- Added 11/05/03 by native driver");
            } finally {
                try { statement_.executeUpdate ("DROP TABLE " + tableName); } catch (SQLException e) { }
            }
	} else {
            notApplicable("V5R3 or higher variation.");
	}
    }


/**
setString() - make sure MIXED data can be passed to a CLOB
Found by customer in V5R3 in CPS discussion 6FZJF4
Also needs to work for V5R2
**/
    public void Var042() {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("Not applicable for JCC");
	    return;
	}

	if(getRelease() >= JDTestDriver.RELEASE_V5R2M0) {
            String tableName = JDPSTest.COLLECTION + ".MIXED5035";
            try  {

		//
		// In V5R2, all translations for native JDBC driver
                // go through job CCSID.  Must change CCSId for 5035 for
		// this to work.
		//
		if ((getRelease() == JDTestDriver.RELEASE_V5R2M0) &&
		    (getDriver () == JDTestDriver.DRIVER_NATIVE)) {

		   System.out.println("The CCSID is "+JDJobName.getJobCCSID());
		    JDJobName.setIGC(5035);

		   System.out.println("The CCSID is "+JDJobName.getJobCCSID());

		}
		try { statement_.executeUpdate("drop table " + tableName); } catch (Exception e) {}
                statement_.executeUpdate ("CREATE TABLE " + tableName
                                          + " (COL1 CLOB(30000) CCSID 5035)");
                PreparedStatement ps = connection_.prepareStatement (
                                       "INSERT INTO " + tableName +
                                       "(COL1) VALUES (?)");
		String insertString = "\u3042\u3044\u3046\u3048\u304a";
                ps.setString (1, insertString );
                ps.executeUpdate ();
                ps.close ();

		//
		// Get a new stmt with the right CCSID
		//

		Statement stmt = connection_.createStatement ();
                ResultSet rs = stmt.executeQuery ("SELECT * FROM " + tableName);
                rs.next ();
                String col1 = rs.getString (1);
                rs.close ();
		stmt.close();

		assertCondition(col1.equals(insertString), "retrieved value("+formatUnicode(col1)+") != expected("+formatUnicode(insertString)+") -- Added 09/07/05 by native driver for CPS 6FZJF4");

            } catch (Exception e) {
                failed (e, "Unexpected Exception -- Added 09/09/05 by native driver for CPS 6FZJF4");
            } finally {
                try { statement_.executeUpdate ("DROP TABLEX " + tableName); } catch (SQLException e) { }
            }
	} else {
            notApplicable("V5R2 or higher variation.");
	}
    }


    String dfp16values[][] = {
	{"valid", "NaN", "NaN"},
	{"valid", "NAN", "NaN"},
	{"valid", "+NaN", "NaN"},
	{"valid", "-NaN", "-NaN"},
	{"valid", "QNaN", "NaN"},
	{"valid", "+QNaN", "NaN"},
	{"valid", "-QNaN", "-NaN"},
	{"valid", "SNaN", "SNaN"},
	{"valid", "+SNaN", "SNaN"},
	{"valid", "-SNaN", "-SNaN"},
	{"valid", "INF", "Infinity"},
	{"valid", "+INF", "Infinity"},
	{"valid", "-INF", "-Infinity"},
	{"valid", "Infinity", "Infinity"},
	{"valid", "+Infinity", "Infinity"},
	{"valid", "-Infinity", "-Infinity"},
	{"valid", "1234567890123456", "1234567890123456"},
	{"valid", "-1234567890123456", "-1234567890123456"},
	{"valid", "+1234567890123456","1234567890123456"},
	{"valid", "+1234567890123456E28","1.234567890123456E+43"},
	{"valid", "+1234567890123456E+28","1.234567890123456E+43"},
	{"valid", "+123456789012345.6E+29","1.234567890123456E+43"},
	{"valid", "+12345678901234.56E+30","1.234567890123456E+43"},
	{"valid", "+1234567890123.456E+31","1.234567890123456E+43"},
	{"valid", "+123456789012.3456E+32","1.234567890123456E+43"},
	{"valid", "+12345678901.23456E+33","1.234567890123456E+43"},
	{"valid", "+1234567890.123456E+34","1.234567890123456E+43"},
	{"valid", "+123456789.0123456E+35","1.234567890123456E+43"},
	{"valid", "+12345678.90123456E+36","1.234567890123456E+43"},
	{"valid", "+1234567.890123456E+37","1.234567890123456E+43"},
	{"valid", "+123456.7890123456E+38","1.234567890123456E+43"},
	{"valid", "+12345.67890123456E+39","1.234567890123456E+43"},
	{"valid", "+1234.567890123456E+40","1.234567890123456E+43"},
	{"valid", "+123.4567890123456E+41","1.234567890123456E+43"},
	{"valid", "+12.34567890123456E+42","1.234567890123456E+43"},
	{"valid", "+1.234567890123456E+43","1.234567890123456E+43"},
	{"valid", "+.1234567890123456E+44","1.234567890123456E+43"},
	{"valid", "+0.1234567890123456E+44","1.234567890123456E+43"},
	{"valid", "+0.01234567890123456E+45","1.234567890123456E+43"},
	{"valid", "-1234567890123456E28","-1.234567890123456E+43"},
	{"valid", "1E0", "1"},
	{"valid", "1.1", "1.1"},
	{"valid", "1.1E0", "1.1"},
	{"valid", "0.0000000000000000000000000000000000000000000000000000000000001234567890123456",
	"1.234567890123456E-61"},
	{"valid", null, null},
	{"truncation", "12345678901234567", "1.234567890123457E+16"}

    };



    String dfp16valuesJDK14[][] = {
	{"valid", "NaN", "NaN"},
	{"valid", "NAN", "NaN"},
	{"valid", "+NaN", "NaN"},
	{"valid", "-NaN", "-NaN"},
	{"valid", "QNaN", "NaN"},
	{"valid", "+QNaN", "NaN"},
	{"valid", "-QNaN", "-NaN"},
	{"valid", "SNaN", "SNaN"},
	{"valid", "+SNaN", "SNaN"},
	{"valid", "-SNaN", "-SNaN"},
	{"valid", "INF", "Infinity"},
	{"valid", "+INF", "Infinity"},
	{"valid", "-INF", "-Infinity"},
	{"valid", "Infinity", "Infinity"},
	{"valid", "+Infinity", "Infinity"},
	{"valid", "-Infinity", "-Infinity"},
	{"valid", "1234567890123456", "1234567890123456"},
	{"valid", "-1234567890123456", "-1234567890123456"},
	{"valid", "+1234567890123456","1234567890123456"},
	{"valid", "+1234567890123456E28",  "12345678901234560000000000000000000000000000"},
	{"valid", "+1234567890123456E+28", "12345678901234560000000000000000000000000000"},
	{"valid", "+123456789012345.6E+29","12345678901234560000000000000000000000000000"},
	{"valid", "+12345678901234.56E+30","12345678901234560000000000000000000000000000"},
	{"valid", "+1234567890123.456E+31","12345678901234560000000000000000000000000000"},
	{"valid", "+123456789012.3456E+32","12345678901234560000000000000000000000000000"},
	{"valid", "+12345678901.23456E+33","12345678901234560000000000000000000000000000"},
	{"valid", "+1234567890.123456E+34","12345678901234560000000000000000000000000000"},
	{"valid", "+123456789.0123456E+35","12345678901234560000000000000000000000000000"},
	{"valid", "+12345678.90123456E+36","12345678901234560000000000000000000000000000"},
	{"valid", "+1234567.890123456E+37","12345678901234560000000000000000000000000000"},
	{"valid", "+123456.7890123456E+38","12345678901234560000000000000000000000000000"},
	{"valid", "+12345.67890123456E+39","12345678901234560000000000000000000000000000"},
	{"valid", "+1234.567890123456E+40","12345678901234560000000000000000000000000000"},
	{"valid", "+123.4567890123456E+41","12345678901234560000000000000000000000000000"},
	{"valid", "+12.34567890123456E+42","12345678901234560000000000000000000000000000"},
	{"valid", "+1.234567890123456E+43","12345678901234560000000000000000000000000000"},
	{"valid", "+.1234567890123456E+44","12345678901234560000000000000000000000000000"},
	{"valid", "+0.1234567890123456E+44","12345678901234560000000000000000000000000000"},
	{"valid", "+0.01234567890123456E+45","12345678901234560000000000000000000000000000"},
	{"valid", "-1234567890123456E28","-12345678901234560000000000000000000000000000"},
	{"valid", "1E0", "1"},
	{"valid", "1.1", "1.1"},
	{"valid", "1.1E0", "1.1"},
	{"valid", "0.0000000000000000000000000000000000000000000000000000000000001234567890123456",
	"0.0000000000000000000000000000000000000000000000000000000000001234567890123456"},
	{"valid", null, null},
	{"truncation", "12345678901234567", "12345678901234570"}

    };


    String dfp16valuesTB[][] = {
    {"valid", "NaN", "NaN"},
    {"valid", "NAN", "NaN"},
    {"valid", "+NaN", "NaN"},
    {"valid", "-NaN", "-NaN"},
    {"valid", "QNaN", "NaN"},
    {"valid", "+QNaN", "NaN"},
    {"valid", "-QNaN", "-NaN"},
    {"valid", "SNaN", "SNaN"},
    {"valid", "+SNaN", "SNaN"},
    {"valid", "-SNaN", "-SNaN"},
    {"valid", "INF", "Infinity"},
    {"valid", "+INF", "Infinity"},
    {"valid", "-INF", "-Infinity"},
    {"valid", "Infinity", "Infinity"},
    {"valid", "+Infinity", "Infinity"},
    {"valid", "-Infinity", "-Infinity"},
    {"valid", "1234567890123456", "1234567890123456"},
    {"valid", "-1234567890123456", "-1234567890123456"},
    {"valid", "+1234567890123456","1234567890123456"},
    {"valid", "+1234567890123456E28","1.234567890123456E+43"},
    {"valid", "+1234567890123456E+28","1.234567890123456E+43"},
    {"valid", "+123456789012345.6E+29","1.234567890123456E+43"},
    {"valid", "+12345678901234.56E+30","1.234567890123456E+43"},
    {"valid", "+1234567890123.456E+31","1.234567890123456E+43"},
    {"valid", "+123456789012.3456E+32","1.234567890123456E+43"},
    {"valid", "+12345678901.23456E+33","1.234567890123456E+43"},
    {"valid", "+1234567890.123456E+34","1.234567890123456E+43"},
    {"valid", "+123456789.0123456E+35","1.234567890123456E+43"},
    {"valid", "+12345678.90123456E+36","1.234567890123456E+43"},
    {"valid", "+1234567.890123456E+37","1.234567890123456E+43"},
    {"valid", "+123456.7890123456E+38","1.234567890123456E+43"},
    {"valid", "+12345.67890123456E+39","1.234567890123456E+43"},
    {"valid", "+1234.567890123456E+40","1.234567890123456E+43"},
    {"valid", "+123.4567890123456E+41","1.234567890123456E+43"},
    {"valid", "+12.34567890123456E+42","1.234567890123456E+43"},
    {"valid", "+1.234567890123456E+43","1.234567890123456E+43"},
    {"valid", "+.1234567890123456E+44","1.234567890123456E+43"},
    {"valid", "+0.1234567890123456E+44","1.234567890123456E+43"},
    {"valid", "+0.01234567890123456E+45","1.234567890123456E+43"},
    {"valid", "-1234567890123456E28","-1.234567890123456E+43"},
    {"valid", "1E0", "1"},
    {"valid", "1.1", "1.1"},
    {"valid", "1.1E0", "1.1"},
    {"valid", "0.0000000000000000000000000000000000000000000000000000000000001234567890123456",
    "1.234567890123456E-61"},
    {"valid", null, null},
    {"truncation", "12345678901234567", "1.234567890123457E+16"}

    };



    String dfp16valuesJDK14TB[][] = {
    {"valid", "NaN", "NaN"},
    {"valid", "NAN", "NaN"},
    {"valid", "+NaN", "NaN"},
    {"valid", "-NaN", "-NaN"},
    {"valid", "QNaN", "NaN"},
    {"valid", "+QNaN", "NaN"},
    {"valid", "-QNaN", "-NaN"},
    {"valid", "SNaN", "SNaN"},
    {"valid", "+SNaN", "SNaN"},
    {"valid", "-SNaN", "-SNaN"},
    {"valid", "INF", "Infinity"},
    {"valid", "+INF", "Infinity"},
    {"valid", "-INF", "-Infinity"},
    {"valid", "Infinity", "Infinity"},
    {"valid", "+Infinity", "Infinity"},
    {"valid", "-Infinity", "-Infinity"},
    {"valid", "1234567890123456", "1234567890123456"},
    {"valid", "-1234567890123456", "-1234567890123456"},
    {"valid", "+1234567890123456","1234567890123456"},
    {"valid", "+1234567890123456E28",  "12345678901234560000000000000000000000000000"},
    {"valid", "+1234567890123456E+28", "12345678901234560000000000000000000000000000"},
    {"valid", "+123456789012345.6E+29","12345678901234560000000000000000000000000000"},
    {"valid", "+12345678901234.56E+30","12345678901234560000000000000000000000000000"},
    {"valid", "+1234567890123.456E+31","12345678901234560000000000000000000000000000"},
    {"valid", "+123456789012.3456E+32","12345678901234560000000000000000000000000000"},
    {"valid", "+12345678901.23456E+33","12345678901234560000000000000000000000000000"},
    {"valid", "+1234567890.123456E+34","12345678901234560000000000000000000000000000"},
    {"valid", "+123456789.0123456E+35","12345678901234560000000000000000000000000000"},
    {"valid", "+12345678.90123456E+36","12345678901234560000000000000000000000000000"},
    {"valid", "+1234567.890123456E+37","12345678901234560000000000000000000000000000"},
    {"valid", "+123456.7890123456E+38","12345678901234560000000000000000000000000000"},
    {"valid", "+12345.67890123456E+39","12345678901234560000000000000000000000000000"},
    {"valid", "+1234.567890123456E+40","12345678901234560000000000000000000000000000"},
    {"valid", "+123.4567890123456E+41","12345678901234560000000000000000000000000000"},
    {"valid", "+12.34567890123456E+42","12345678901234560000000000000000000000000000"},
    {"valid", "+1.234567890123456E+43","12345678901234560000000000000000000000000000"},
    {"valid", "+.1234567890123456E+44","12345678901234560000000000000000000000000000"},
    {"valid", "+0.1234567890123456E+44","12345678901234560000000000000000000000000000"},
    {"valid", "+0.01234567890123456E+45","12345678901234560000000000000000000000000000"},
    {"valid", "-1234567890123456E28","-12345678901234560000000000000000000000000000"},
    {"valid", "1E0", "1"},
    {"valid", "1.1", "1.1"},
    {"valid", "1.1E0", "1.1"},
    {"valid", "0.0000000000000000000000000000000000000000000000000000000000001234567890123456",
    "0.0000000000000000000000000000000000000000000000000000000000001234567890123456"},
    {"valid", null, null},
    {"truncation", "12345678901234567", "12345678901234570"}

    };


/**
setString() - Set a DECFLOAT 16 parameter to valid values.  This should work.
**/
    public void Var043()
    {
        String setValue = null;
        String added = " -- DECFLOAT(16) test added by native driver 11/18/2006";
        if (checkDecFloatSupport()) {
            try {
                boolean success = true;
                StringBuffer sb = new StringBuffer();
                PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                        + " VALUES (?)");
                if (getDriver() == JDTestDriver.DRIVER_NATIVE && getJDK() <= JVMInfo.JDK_142) {
                    dfp16values = dfp16valuesJDK14;
                }else if (isToolboxDriver() && getJDK() <= JVMInfo.JDK_142) {
                    dfp16values = dfp16valuesJDK14TB;
                }else if (isToolboxDriver()) {
                    dfp16values = dfp16valuesTB;
                }
                for (int i = 0; i < dfp16values.length; i++) {
                    if (dfp16values[i][0] == "valid") {
                        statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);
                        setValue =  dfp16values[i][1];
                        ps.setString (1, setValue);
                        ps.executeUpdate();

                        ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
                        rs.next ();
                        String check = rs.getString (1);

                        if (check == null) {
                            if (dfp16values[i][2] != null) {
                                success = false;
                                sb.append("\n Expected "+dfp16values[i][2]+" from "+dfp16values[i][1]+
                                        " but got "+check);
                            }
                        } else {
                            if (!check.equals(dfp16values[i][2])) {
                                success = false;
                                sb.append("\n Expected "+dfp16values[i][2]+" from "+dfp16values[i][1]+
                                        " but got "+check);
                            }
                        }
                        rs.close ();

                    } /* if valid */
                } /* end for */

                ps.close ();

                assertCondition (success, added + " table="+ JDPSTest.PSTEST_SETDFP16 + sb.toString());

            }
            catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    System.out.println("Info: jcc doesn't allow NaN value for DECFLOAT");
		    String exMessage = e.toString();
		    String expectedMessage = "Invalid data conversion:  Parameter instance NaN";
		    assertCondition(exMessage.indexOf(expectedMessage) > 0,
				    "Expected message '"+expectedMessage+"' not found in "+
				    exMessage);

		} else {
		    failed (e, "Unexpected Exception: probably setting "+setValue+" "+added);
		}
            }
        }
    }

/**
setString() - Set a DECFLOAT 16 parameter to random values.  This should work.
**/
    public void Var044()
    {
	int count= 0;
	long runMillis=20000;
	String setValue = null;
	String added = " -- DECFLOAT(16) test added by native driver 11/18/2006";
	if (checkDecFloatSupport()) {
            try {
		long endTime = System.currentTimeMillis() + runMillis;
		int errorCount = 0 ;
		StringBuffer sb = new StringBuffer();
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                                                                    + " VALUES (?)");
		Random random = new Random(System.currentTimeMillis());
		while((System.currentTimeMillis() < endTime) && (errorCount < 10)) {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		    int nineDigits = 100000000 + random.nextInt(900000000);
		    int sevenDigits = random.nextInt(10000000);
		    long number = ((long) sevenDigits) * 1000000000 + nineDigits;
		    int negative = random.nextInt(2);
		    int exponent= -200 + random.nextInt(400);

		    if (negative == 0) {
			setValue= number +"E"+exponent;
		    } else {
			setValue= "-"+number +"E"+exponent;
		    }

		    BigDecimal expBd = new BigDecimal(setValue);
		    String expected = expBd.toString();

		    ps.setString (1, setValue);
		    ps.executeUpdate();

		    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
		    rs.next ();
		    String check = rs.getString (1);
		    if (check == null) {
			errorCount++;
			sb.append("\n Expected "+expected+" from "+setValue+" but got "+check);
		    } else {
			BigDecimal bd = new BigDecimal(check);

			if (expBd.compareTo(bd) != 0) {
			    errorCount++;
			    sb.append("\n Expected "+expected+" from "+setValue+" but got "+check+"="+bd.toString());
			}
		    }
		    count++;
		    rs.close ();

		} /* while */

		ps.close ();
		System.out.println("      Insert DFP16:  "+count+" Samples tried in "+runMillis+" milliseconds");
                assertCondition (errorCount == 0, added + " table="+ JDPSTest.PSTEST_SETDFP16 + sb.toString());

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception: probably setting '"+setValue+"' "+added);
            }
        }
    }



/**
setString() - Set a DECFLOAT 16 parameter to truncation values.  This should work.
**/
    public void Var045()
    {
	String setValue = null;
	String added = " -- DECFLOAT(16) test added by native driver 11/18/2006";
	if (getDriver() == JDTestDriver.DRIVER_NATIVE && getJDK() <= JVMInfo.JDK_142) {
	    dfp16values = dfp16valuesJDK14;
	}
	if (isToolboxDriver() && getJDK() <= JVMInfo.JDK_142) {
        dfp16values = dfp16valuesJDK14;
    }

	if (checkDecFloatSupport()) {
            try {
		boolean success = true;
		StringBuffer sb = new StringBuffer();
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                                                                    + " VALUES (?)");
		for (int i = 0; i < dfp16values.length; i++) {
		    if (dfp16values[i][0] == "truncation") {
			statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);
			setValue =  dfp16values[i][1];
			boolean valueSet = false;
			try {
			    ps.setString (1, setValue);
			    valueSet = true;
			} catch (Exception e) {
			    String check = e.toString();
			    if (!check.equals(dfp16values[i][2])) {
				success=false;
				sb.append("\n Expected "+dfp16values[i][2]+" from "+dfp16values[i][1]+
					  " but got "+check);
				e.printStackTrace();
			    }
			}
			if (valueSet) {
			    ps.executeUpdate();

			    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
			    rs.next ();
			    String check = rs.getString (1);
			    if (check == null) {
				if (dfp16values[i][2] != null) {
				    success = false;
				    sb.append("\n Expected "+dfp16values[i][2]+" from "+dfp16values[i][1]+
					      " but got "+check);
				}
			    } else {
				if (!check.equals(dfp16values[i][2])) {
				    success = false;
				    sb.append("\n Expected "+dfp16values[i][2]+" from "+dfp16values[i][1]+
					      " but got "+check);
				}
			    }
			    rs.close ();
			}
		    } /* if valid */
		} /* end for */

		ps.close ();

                assertCondition (success, added + " table="+ JDPSTest.PSTEST_SETDFP16 + sb.toString());

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception: probably setting "+setValue+" "+added);
            }
        }
    }

    String dfp34valuesJDK14[][] = {
	{"valid", "NaN", "NaN"},
	{"valid", "NAN", "NaN"},
	{"valid", "+NaN", "NaN"},
	{"valid", "-NaN", "-NaN"},
	{"valid", "QNaN", "NaN"},
	{"valid", "+QNaN", "NaN"},
	{"valid", "-QNaN", "-NaN"},
	{"valid", "SNaN", "SNaN"},
	{"valid", "+SNaN", "SNaN"},
	{"valid", "-SNaN", "-SNaN"},
	{"valid", "INF", "Infinity"},
	{"valid", "+INF", "Infinity"},
	{"valid", "-INF", "-Infinity"},
	{"valid", "Infinity", "Infinity"},
	{"valid", "+Infinity", "Infinity"},
	{"valid", "-Infinity", "-Infinity"},
	{"valid", "1234567890123456", "1234567890123456"},
	{"valid", "-1234567890123456", "-1234567890123456"},
	{"valid", "+1234567890123456","1234567890123456"},
	{"valid", "+1234567890123456E28",  "12345678901234560000000000000000000000000000"},
	{"valid", "+1234567890123456E+28", "12345678901234560000000000000000000000000000"},
	{"valid", "+123456789012345.6E+29","12345678901234560000000000000000000000000000"},
	{"valid", "+12345678901234.56E+30","12345678901234560000000000000000000000000000"},
	{"valid", "+1234567890123.456E+31","12345678901234560000000000000000000000000000"},
	{"valid", "+123456789012.3456E+32","12345678901234560000000000000000000000000000"},
	{"valid", "+12345678901.23456E+33","12345678901234560000000000000000000000000000"},
	{"valid", "+1234567890.123456E+34","12345678901234560000000000000000000000000000"},
	{"valid", "+123456789.0123456E+35","12345678901234560000000000000000000000000000"},
	{"valid", "+12345678.90123456E+36","12345678901234560000000000000000000000000000"},
	{"valid", "+1234567.890123456E+37","12345678901234560000000000000000000000000000"},
	{"valid", "+123456.7890123456E+38","12345678901234560000000000000000000000000000"},
	{"valid", "+12345.67890123456E+39","12345678901234560000000000000000000000000000"},
	{"valid", "+1234.567890123456E+40","12345678901234560000000000000000000000000000"},
	{"valid", "+123.4567890123456E+41","12345678901234560000000000000000000000000000"},
	{"valid", "+12.34567890123456E+42","12345678901234560000000000000000000000000000"},
	{"valid", "+1.234567890123456E+43","12345678901234560000000000000000000000000000"},
	{"valid", "+.1234567890123456E+44","12345678901234560000000000000000000000000000"},
	{"valid", "+0.1234567890123456E+44","12345678901234560000000000000000000000000000"},
	{"valid", "+0.01234567890123456E+45","12345678901234560000000000000000000000000000"},
	{"valid", "-1234567890123456E28","-12345678901234560000000000000000000000000000"},
	{"valid", "1E0", "1"},
	{"valid", "1.1", "1.1"},
	{"valid", "1.1E0", "1.1"},
	{"valid", null, null},
	{"truncation", "12345678901234567890123456789012345", "12345678901234567890123456789012340"}

    };


    String dfp34values[][] = {
	{"valid", "NaN", "NaN"},
	{"valid", "NAN", "NaN"},
	{"valid", "+NaN", "NaN"},
	{"valid", "-NaN", "-NaN"},
	{"valid", "QNaN", "NaN"},
	{"valid", "+QNaN", "NaN"},
	{"valid", "-QNaN", "-NaN"},
	{"valid", "SNaN", "SNaN"},
	{"valid", "+SNaN", "SNaN"},
	{"valid", "-SNaN", "-SNaN"},
	{"valid", "INF", "Infinity"},
	{"valid", "+INF", "Infinity"},
	{"valid", "-INF", "-Infinity"},
	{"valid", "Infinity", "Infinity"},
	{"valid", "+Infinity", "Infinity"},
	{"valid", "-Infinity", "-Infinity"},
	{"valid", "1234567890123456", "1234567890123456"},
	{"valid", "-1234567890123456", "-1234567890123456"},
	{"valid", "+1234567890123456","1234567890123456"},
	{"valid", "+1234567890123456E28","1.234567890123456E+43"},
	{"valid", "+1234567890123456E+28","1.234567890123456E+43"},
	{"valid", "+123456789012345.6E+29","1.234567890123456E+43"},
	{"valid", "+12345678901234.56E+30","1.234567890123456E+43"},
	{"valid", "+1234567890123.456E+31","1.234567890123456E+43"},
	{"valid", "+123456789012.3456E+32","1.234567890123456E+43"},
	{"valid", "+12345678901.23456E+33","1.234567890123456E+43"},
	{"valid", "+1234567890.123456E+34","1.234567890123456E+43"},
	{"valid", "+123456789.0123456E+35","1.234567890123456E+43"},
	{"valid", "+12345678.90123456E+36","1.234567890123456E+43"},
	{"valid", "+1234567.890123456E+37","1.234567890123456E+43"},
	{"valid", "+123456.7890123456E+38","1.234567890123456E+43"},
	{"valid", "+12345.67890123456E+39","1.234567890123456E+43"},
	{"valid", "+1234.567890123456E+40","1.234567890123456E+43"},
	{"valid", "+123.4567890123456E+41","1.234567890123456E+43"},
	{"valid", "+12.34567890123456E+42","1.234567890123456E+43"},
	{"valid", "+1.234567890123456E+43","1.234567890123456E+43"},
	{"valid", "+.1234567890123456E+44","1.234567890123456E+43"},
	{"valid", "+0.1234567890123456E+44","1.234567890123456E+43"},
	{"valid", "+0.01234567890123456E+45","1.234567890123456E+43"},
	{"valid", "-1234567890123456E28","-1.234567890123456E+43"},
	{"valid", "1E0", "1"},
	{"valid", "1.1", "1.1"},
	{"valid", "1.1E0", "1.1"},
	{"valid", null, null},
	{"truncation", "12345678901234567890123456789012345", "1.234567890123456789012345678901234E+34"}

    };

    String dfp34valuesJDK14TB[][] = {
            {"valid", "NaN", "NaN"},
            {"valid", "NAN", "NaN"},
            {"valid", "+NaN", "NaN"},
            {"valid", "-NaN", "-NaN"},
            {"valid", "QNaN", "NaN"},
            {"valid", "+QNaN", "NaN"},
            {"valid", "-QNaN", "-NaN"},
            {"valid", "SNaN", "SNaN"},   //snan
            {"valid", "+SNaN", "SNaN"},  //snan
            {"valid", "-SNaN", "-SNaN"}, //snan
            {"valid", "INF", "Infinity"},
            {"valid", "+INF", "Infinity"},
            {"valid", "-INF", "-Infinity"},
            {"valid", "Infinity", "Infinity"},
            {"valid", "+Infinity", "Infinity"},
            {"valid", "-Infinity", "-Infinity"},
            {"valid", "1234567890123456", "1234567890123456"},
            {"valid", "-1234567890123456", "-1234567890123456"},
            {"valid", "+1234567890123456","1234567890123456"},
            {"valid", "+1234567890123456E28",  "12345678901234560000000000000000000000000000"},
            {"valid", "+1234567890123456E+28", "12345678901234560000000000000000000000000000"},
            {"valid", "+123456789012345.6E+29","12345678901234560000000000000000000000000000"},
            {"valid", "+12345678901234.56E+30","12345678901234560000000000000000000000000000"},
            {"valid", "+1234567890123.456E+31","12345678901234560000000000000000000000000000"},
            {"valid", "+123456789012.3456E+32","12345678901234560000000000000000000000000000"},
            {"valid", "+12345678901.23456E+33","12345678901234560000000000000000000000000000"},
            {"valid", "+1234567890.123456E+34","12345678901234560000000000000000000000000000"},
            {"valid", "+123456789.0123456E+35","12345678901234560000000000000000000000000000"},
            {"valid", "+12345678.90123456E+36","12345678901234560000000000000000000000000000"},
            {"valid", "+1234567.890123456E+37","12345678901234560000000000000000000000000000"},
            {"valid", "+123456.7890123456E+38","12345678901234560000000000000000000000000000"},
            {"valid", "+12345.67890123456E+39","12345678901234560000000000000000000000000000"},
            {"valid", "+1234.567890123456E+40","12345678901234560000000000000000000000000000"},
            {"valid", "+123.4567890123456E+41","12345678901234560000000000000000000000000000"},
            {"valid", "+12.34567890123456E+42","12345678901234560000000000000000000000000000"},
            {"valid", "+1.234567890123456E+43","12345678901234560000000000000000000000000000"},
            {"valid", "+.1234567890123456E+44","12345678901234560000000000000000000000000000"},
            {"valid", "+0.1234567890123456E+44","12345678901234560000000000000000000000000000"},
            {"valid", "+0.01234567890123456E+45","12345678901234560000000000000000000000000000"},
            {"valid", "-1234567890123456E28","-12345678901234560000000000000000000000000000"},
            {"valid", "1E0", "1"},
            {"valid", "1.1", "1.1"},
            {"valid", "1.1E0", "1.1"},
            {"valid", null, null},
            {"truncation", "12345678901234567890123456789012345", "12345678901234567890123456789012340"}

            };


            String dfp34valuesTB[][] = {
            {"valid", "NaN", "NaN"},
            {"valid", "NAN", "NaN"},
            {"valid", "+NaN", "NaN"},
            {"valid", "-NaN", "-NaN"},
            {"valid", "QNaN", "NaN"},
            {"valid", "+QNaN", "NaN"},
            {"valid", "-QNaN", "-NaN"},
            {"valid", "SNaN", "SNaN"},   //snan
            {"valid", "+SNaN", "SNaN"},  //snan
            {"valid", "-SNaN", "-SNaN"}, //snan
            {"valid", "INF", "Infinity"},
            {"valid", "+INF", "Infinity"},
            {"valid", "-INF", "-Infinity"},
            {"valid", "Infinity", "Infinity"},
            {"valid", "+Infinity", "Infinity"},
            {"valid", "-Infinity", "-Infinity"},
            {"valid", "1234567890123456", "1234567890123456"},
            {"valid", "-1234567890123456", "-1234567890123456"},
            {"valid", "+1234567890123456","1234567890123456"},
            {"valid", "+1234567890123456E28","1.234567890123456E+43"},
            {"valid", "+1234567890123456E+28","1.234567890123456E+43"},
            {"valid", "+123456789012345.6E+29","1.234567890123456E+43"},
            {"valid", "+12345678901234.56E+30","1.234567890123456E+43"},
            {"valid", "+1234567890123.456E+31","1.234567890123456E+43"},
            {"valid", "+123456789012.3456E+32","1.234567890123456E+43"},
            {"valid", "+12345678901.23456E+33","1.234567890123456E+43"},
            {"valid", "+1234567890.123456E+34","1.234567890123456E+43"},
            {"valid", "+123456789.0123456E+35","1.234567890123456E+43"},
            {"valid", "+12345678.90123456E+36","1.234567890123456E+43"},
            {"valid", "+1234567.890123456E+37","1.234567890123456E+43"},
            {"valid", "+123456.7890123456E+38","1.234567890123456E+43"},
            {"valid", "+12345.67890123456E+39","1.234567890123456E+43"},
            {"valid", "+1234.567890123456E+40","1.234567890123456E+43"},
            {"valid", "+123.4567890123456E+41","1.234567890123456E+43"},
            {"valid", "+12.34567890123456E+42","1.234567890123456E+43"},
            {"valid", "+1.234567890123456E+43","1.234567890123456E+43"},
            {"valid", "+.1234567890123456E+44","1.234567890123456E+43"},
            {"valid", "+0.1234567890123456E+44","1.234567890123456E+43"},
            {"valid", "+0.01234567890123456E+45","1.234567890123456E+43"},
            {"valid", "-1234567890123456E28","-1.234567890123456E+43"},
            {"valid", "1E0", "1"},
            {"valid", "1.1", "1.1"},
            {"valid", "1.1E0", "1.1"},
            {"valid", null, null},
            {"truncation", "12345678901234567890123456789012345", "1.234567890123456789012345678901234E+34"}

            };


/**
setString() - Set a DECFLOAT 34 parameter to valid values.  This should work.
**/
    public void Var046()
    {
	String setValue = null;
	String added = " -- DECFLOAT(34) test added by native driver 11/18/2006";

	if (getDriver() == JDTestDriver.DRIVER_NATIVE && getJDK() <= JVMInfo.JDK_142) {
	    dfp34values = dfp34valuesJDK14;
	}
	if (isToolboxDriver() ) {
        dfp34values = dfp34valuesTB;
    }
	if (isToolboxDriver() && getJDK() <= JVMInfo.JDK_142) {
        dfp34values = dfp34valuesJDK14TB;
    }


	if (checkDecFloatSupport()) {
            try {
		boolean success = true;
		StringBuffer sb = new StringBuffer();
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                                                                    + " VALUES (?)");
		for (int i = 0; i < dfp34values.length; i++) {
		    if (dfp34values[i][0] == "valid") {
			statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);
			setValue =  dfp34values[i][1];
			ps.setString (1, setValue);
			ps.executeUpdate();

			ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
			rs.next ();
			String check = rs.getString (1);
			if (check == null) {
			    if (dfp34values[i][2] != null) {
				success = false;
				sb.append("\n Expected "+dfp34values[i][2]+" from "+dfp34values[i][1]+
					  " but got "+check);
			    }
			} else {
			    if (!check.equals(dfp34values[i][2])) {
				success = false;
				sb.append("\n Expected "+dfp34values[i][2]+" from "+dfp34values[i][1]+
					  " but got "+check);
			    }
			}
			rs.close ();

		    } /* if valid */
		} /* end for */

		ps.close ();

                assertCondition (success, added + " table="+ JDPSTest.PSTEST_SETDFP34 + sb.toString());

            }
            catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    System.out.println("Info: jcc doesn't allow NaN value for DECFLOAT");
		    String exMessage = e.toString();
		    String expectedMessage = "Invalid data conversion:  Parameter instance NaN";
		    assertCondition(exMessage.indexOf(expectedMessage) > 0,
				    "Expected message '"+expectedMessage+"' not found in "+
				    exMessage);

		} else {

		    failed (e, "Unexpected Exception: probably setting "+setValue+" "+added);
		}
            }
        }
    }

/**
setString() - Set a DECFLOAT 34 parameter to random values.  This should work.
**/
    public void Var047()
    {
	int count= 0;
	long runMillis=20000;
	String setValue = null;
	String added = " -- DECFLOAT(34) test added by native driver 11/18/2006";

	if (checkDecFloatSupport()) {
            try {
		long endTime = System.currentTimeMillis() + runMillis;
		int errorCount = 0 ;
		StringBuffer sb = new StringBuffer();
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                                                                    + " VALUES (?)");
		Random random = new Random(System.currentTimeMillis());
		while((System.currentTimeMillis() < endTime) && (errorCount < 10)) {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

		    int nineDigits = 100000000 + random.nextInt(900000000);
		    int sevenDigits = random.nextInt(10000000);
		    long number = ((long) sevenDigits) * 1000000000 + nineDigits;
		    int negative = random.nextInt(2);
		    int exponent= -200 + random.nextInt(400);

		    if (negative == 0) {
			setValue= number +"E"+exponent;
		    } else {
			setValue= "-"+number +"E"+exponent;
		    }

		    BigDecimal expBd = new BigDecimal(setValue);
		    String expected = expBd.toString();

		    ps.setString (1, setValue);
		    ps.executeUpdate();

		    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
		    rs.next ();
		    String check = rs.getString (1);
		    if (check == null) {
			errorCount++;
			sb.append("\n Expected "+expected+" from "+setValue+" but got "+check);
		    } else {
			BigDecimal bd = new BigDecimal(check);

			if (expBd.compareTo(bd) != 0 ) {
			    errorCount++;
			    sb.append("\n Expected "+expected+" from "+setValue+" but got "+check+"="+bd.toString());
			}
		    }
		    count++;
		    rs.close ();

		} /* while */

		ps.close ();
		System.out.println("      Insert DFP34:  "+count+" Samples tried in "+runMillis+" milliseconds");
                assertCondition (errorCount == 0, added + " table="+ JDPSTest.PSTEST_SETDFP34 + sb.toString());

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception: probably setting '"+setValue+"' "+added);
            }
        }
    }



/**
setString() - Set a DECFLOAT 34 parameter to truncation values.  This should work.
**/
    public void Var048()
    {

	if (getDriver() == JDTestDriver.DRIVER_NATIVE && getJDK() <= JVMInfo.JDK_142) {
	    dfp34values = dfp34valuesJDK14;
	}
	if (isToolboxDriver() && getJDK() <= JVMInfo.JDK_142) {
        dfp34values = dfp34valuesJDK14;
    }

	String setValue = null;
	String added = " -- DECFLOAT(34) test added by native driver 11/18/2006";
	if (checkDecFloatSupport()) {
            try {
		boolean success = true;
		StringBuffer sb = new StringBuffer();
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                                                                    + " VALUES (?)");
		for (int i = 0; i < dfp34values.length; i++) {
		    if (dfp34values[i][0] == "truncation") {
			statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);
			setValue =  dfp34values[i][1];
			boolean valueSet = false;
			try {
			    ps.setString (1, setValue);
			    valueSet = true;
			} catch (Exception e) {
			    String check = e.toString();
			    if (!check.equals(dfp34values[i][2])) {
				success=false;
				sb.append("\n Expected "+dfp34values[i][2]+" from "+dfp34values[i][1]+
					  " but got "+check);
				e.printStackTrace();
			    }
			}
			if (valueSet) {
			    ps.executeUpdate();

			    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
			    rs.next ();
			    String check = rs.getString (1);
			    if (check == null) {
				if (dfp34values[i][2] != null) {
				    success = false;
				    sb.append("\n Expected "+dfp34values[i][2]+" from "+dfp34values[i][1]+
					      " but got "+check);
				}
			    } else {
				if (!check.equals(dfp34values[i][2])) {
				    success = false;
				    sb.append("\n Expected "+dfp34values[i][2]+" from "+dfp34values[i][1]+
					      " but got "+check);
				}
			    }
			    rs.close ();
			}
		    } /* if valid */
		} /* end for */

		ps.close ();

                assertCondition (success, added + " table="+ JDPSTest.PSTEST_SETDFP34 + sb.toString());

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception: probably setting "+setValue+" "+added);
            }
        }
    }



    public void dfpRoundTest(String roundingMode, String table, String value, String expected) {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("decfloat rounding mode test not applicable for JCC");
	    return;
	}

      if (checkDecFloatSupport()) {
        try {
            if (isToolboxDriver())
                roundingMode = roundingMode.substring(6);

          String url = baseURL_
          
          
          + ";data truncation=true"
          + ";decfloat rounding mode="+roundingMode;
          Connection connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);


          Statement s = connection.createStatement();
	  s.executeUpdate("DELETE FROM "+table);

	  PreparedStatement ps = connection.prepareStatement (
							      "INSERT INTO " + table
							       + " VALUES (?)");
	  ps.setString(1, value);
          ps.execute();

          ResultSet rs2 = s.executeQuery("SELECT * FROM " + table);
          rs2.next();
          String v = rs2.getString(1);
          rs2.close();
          s.close();
          connection.close();
          assertCondition(v.equals(expected), "Got " + v + " sb " + expected +" from "+value+" for mode "+roundingMode);
        } catch (Exception e) {
          failed(e, "Unexpected Exception for value "+ value);
        }
      }
    }

    public void Var049() {  notApplicable(); }
    /*
     * setString -- using different rounding modes
     */
    String RHE="round half even";
    public void Var050 () { dfpRoundTest(RHE, JDPSTest.PSTEST_SETDFP16,  "1.2345678901234545",  "1.234567890123454"); }
    public void Var051 () { dfpRoundTest(RHE, JDPSTest.PSTEST_SETDFP16,  "1.2345678901234555",  "1.234567890123456"); }
    public void Var052 () { dfpRoundTest(RHE, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234545", "-1.234567890123454"); }
    public void Var053 () { dfpRoundTest(RHE, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234555", "-1.234567890123456"); }

    /**
     *  setString -- set a DFP16 with rounding mode "round half up"
     */
    String RHU = "round half up";
    public void Var054 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP16, "1.2345678901234555", "1.234567890123456"); }
    public void Var055 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP16, "1.2345678901234545", "1.234567890123455"); }
    public void Var056 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP16, "1.2345678901234565", "1.234567890123457"); }
    public void Var057 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234555", "-1.234567890123456"); }
    public void Var058 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234545", "-1.234567890123455"); }
    public void Var059 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234565", "-1.234567890123457"); }

    /**
     *  setString -- set a DFP16 with rounding mode "round down"
     */
    String RD = "round down";
    public void Var060 () { dfpRoundTest(RD, JDPSTest.PSTEST_SETDFP16, "1.2345678901234555",       "1.234567890123455"); }
    public void Var061 () { dfpRoundTest(RD, JDPSTest.PSTEST_SETDFP16, "1.2345678901234559999999", "1.234567890123455"); }
    public void Var062 () { dfpRoundTest(RD, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234555",       "-1.234567890123455"); }
    public void Var063 () { dfpRoundTest(RD, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234559999999", "-1.234567890123455"); }



    /**
     *  setString -- set a DFP16 with rounding mode "round ceiling"
     */
    String RC = "round ceiling";
    public void Var064 () { dfpRoundTest(RC, JDPSTest.PSTEST_SETDFP16, "1.2345678901234555",       "1.234567890123456"); }
    public void Var065 () { dfpRoundTest(RC, JDPSTest.PSTEST_SETDFP16, "1.2345678901234559999999", "1.234567890123456"); }
    public void Var066 () { dfpRoundTest(RC, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234555",       "-1.234567890123455"); }
    public void Var067 () { dfpRoundTest(RC, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234559999999", "-1.234567890123455"); }


    /**
     *  setString -- set a DFP16  with rounding mode "round floor"
     */
        String RF = "round floor";
    public void Var068 () { dfpRoundTest(RF, JDPSTest.PSTEST_SETDFP16, "1.2345678901234555",       "1.234567890123455"); }
    public void Var069 () { dfpRoundTest(RF, JDPSTest.PSTEST_SETDFP16, "1.2345678901234559999999", "1.234567890123455"); }
    public void Var070 () { dfpRoundTest(RF, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234555",       "-1.234567890123456"); }
    public void Var071 () { dfpRoundTest(RF, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234559999999", "-1.234567890123456"); }


    /**
     *  setString -- set a DFP16 with rounding mode "round half down"
     */
    String RHD = "round half down";
    public void Var072 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP16, "1.2345678901234555", "1.234567890123455"); }
    public void Var073 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP16, "1.2345678901234545", "1.234567890123454"); }
    public void Var074 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP16, "1.2345678901234565", "1.234567890123456"); }
    public void Var075 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234555", "-1.234567890123455"); }
    public void Var076 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234545", "-1.234567890123454"); }
    public void Var077 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234565", "-1.234567890123456"); }



   /**
     *  setString -- set a DFP16 with rounding mode "round up"
     */
    String RU = "round up";
    public void Var078 () { dfpRoundTest(RU, JDPSTest.PSTEST_SETDFP16, "1.2345678901234555",       "1.234567890123456"); }
    public void Var079 () { dfpRoundTest(RU, JDPSTest.PSTEST_SETDFP16, "1.2345678901234559999999", "1.234567890123456"); }
    public void Var080 () { dfpRoundTest(RU, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234555",       "-1.234567890123456"); }
    public void Var081 () { dfpRoundTest(RU, JDPSTest.PSTEST_SETDFP16, "-1.2345678901234559999999", "-1.234567890123456"); }


    /*
     * setString -- using different rounding modes
     */
    public void Var082 () { dfpRoundTest(RHE, JDPSTest.PSTEST_SETDFP34,"1.1818181818181818182345678901234545","1.181818181818181818234567890123454"); }
    public void Var083 () { dfpRoundTest(RHE, JDPSTest.PSTEST_SETDFP34,  "1.1818181818181818182345678901234555","1.181818181818181818234567890123456"); }
    public void Var084 () { dfpRoundTest(RHE, JDPSTest.PSTEST_SETDFP34, "-1.1818181818181818182345678901234545","-1.181818181818181818234567890123454"); }
    public void Var085 () { dfpRoundTest(RHE, JDPSTest.PSTEST_SETDFP34, "-1.1818181818181818182345678901234555","-1.181818181818181818234567890123456"); }

    /**
     *  setString -- set a DFP34 with rounding mode "round half up"
     */
    public void Var086 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP34, "1.1818181818181818182345678901234555","1.181818181818181818234567890123456"); }
    public void Var087 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP34, "1.1818181818181818182345678901234545","1.181818181818181818234567890123455"); }
    public void Var088 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP34, "1.1818181818181818182345678901234565","1.181818181818181818234567890123457"); }
    public void Var089 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP34, "-1.1818181818181818182345678901234555","-1.181818181818181818234567890123456"); }
    public void Var090 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP34, "-1.1818181818181818182345678901234545","-1.181818181818181818234567890123455"); }
    public void Var091 () { dfpRoundTest(RHU, JDPSTest.PSTEST_SETDFP34, "-1.1818181818181818182345678901234565","-1.181818181818181818234567890123457"); }

    /**
     *  setString -- set a DFP34 with rounding mode "round down"
     */
    public void Var092 () { dfpRoundTest(RD, JDPSTest.PSTEST_SETDFP34, "1.1818181818181818182345678901234555","1.181818181818181818234567890123455"); }
    public void Var093 () { dfpRoundTest(RD, JDPSTest.PSTEST_SETDFP34, "1.1818181818181818182345678901234559999999","1.181818181818181818234567890123455"); }
    public void Var094 () { dfpRoundTest(RD, JDPSTest.PSTEST_SETDFP34, "-1.1818181818181818182345678901234555","-1.181818181818181818234567890123455"); }
    public void Var095 () { dfpRoundTest(RD, JDPSTest.PSTEST_SETDFP34, "-1.1818181818181818182345678901234559999999","-1.181818181818181818234567890123455"); }



    /**
     *  setString -- set a DFP34 with rounding mode "round ceiling"
     */
    public void Var096 () { dfpRoundTest(RC, JDPSTest.PSTEST_SETDFP34, "1.1818181818181818182345678901234555","1.181818181818181818234567890123456"); }
    public void Var097 () { dfpRoundTest(RC, JDPSTest.PSTEST_SETDFP34, "1.1818181818181818182345678901234559999999","1.181818181818181818234567890123456"); }
    public void Var098 () { dfpRoundTest(RC, JDPSTest.PSTEST_SETDFP34, "-1.1818181818181818182345678901234555","-1.181818181818181818234567890123455"); }
    public void Var099 () { dfpRoundTest(RC, JDPSTest.PSTEST_SETDFP34, "-1.1818181818181818182345678901234559999999","-1.181818181818181818234567890123455"); }


    /**
     *  setString -- set a DFP34  with rounding mode "round floor"
     */
    public void Var100 () { dfpRoundTest(RF, JDPSTest.PSTEST_SETDFP34, "1.1818181818181818182345678901234555","1.181818181818181818234567890123455"); }
    public void Var101 () { dfpRoundTest(RF, JDPSTest.PSTEST_SETDFP34,"1.1818181818181818182345678901234559999999","1.181818181818181818234567890123455"); }
    public void Var102 () { dfpRoundTest(RF, JDPSTest.PSTEST_SETDFP34,"-1.1818181818181818182345678901234555","-1.181818181818181818234567890123456"); }
    public void Var103 () { dfpRoundTest(RF, JDPSTest.PSTEST_SETDFP34,"-1.1818181818181818182345678901234559999999","-1.181818181818181818234567890123456"); }


    /**
     *  setString -- set a DFP34 with rounding mode "round half down"
     */
    public void Var104 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP34,"1.1818181818181818182345678901234555","1.181818181818181818234567890123455"); }
    public void Var105 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP34,"1.1818181818181818182345678901234545","1.181818181818181818234567890123454"); }
    public void Var106 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP34,"1.1818181818181818182345678901234565","1.181818181818181818234567890123456"); }
    public void Var107 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP34,"-1.1818181818181818182345678901234555","-1.181818181818181818234567890123455"); }
    public void Var108 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP34,"-1.1818181818181818182345678901234545","-1.181818181818181818234567890123454"); }
    public void Var109 () { dfpRoundTest(RHD, JDPSTest.PSTEST_SETDFP34,"-1.1818181818181818182345678901234565","-1.181818181818181818234567890123456"); }



   /**
     *  setString -- set a DFP34 with rounding mode "round up"
     */
    public void Var110 () { dfpRoundTest(RU, JDPSTest.PSTEST_SETDFP34,"1.1818181818181818182345678901234555","1.181818181818181818234567890123456"); }
    public void Var111 () { dfpRoundTest(RU, JDPSTest.PSTEST_SETDFP34,"1.1818181818181818182345678901234559999999","1.181818181818181818234567890123456"); }
    public void Var112 () { dfpRoundTest(RU, JDPSTest.PSTEST_SETDFP34,"-1.1818181818181818182345678901234555","-1.181818181818181818234567890123456"); }
    public void Var113 () { dfpRoundTest(RU, JDPSTest.PSTEST_SETDFP34,"-1.1818181818181818182345678901234559999999","-1.181818181818181818234567890123456"); }


    public void testCCSID(int ccsid, String[][] inputAndExpected) {
	if (getDriver () == JDTestDriver.DRIVER_JCC)
	{
	    notApplicable("testCCSID Not applicable for JCC");
	    return;
	}

	String sql ="NOT SET";
	StringBuffer sb = new StringBuffer();
	try {
	    sb.append("Testing CCSID "+ccsid);
	    boolean passed =true;
		String tableName = JDPSTest.COLLECTION+".JDPSSS"+ccsid;

		Statement stmt = connection_.createStatement();

		try {
		    sql = "DROP TABLE "+tableName;
		    stmt.executeUpdate(sql);
		} catch (Exception e) {
		}
		sql = "CREATE TABLE "+tableName+" (C1 VARCHAR(800) CCSID "+ccsid+")";
		stmt.executeUpdate(sql);
		sql = "INSERT INTO " + tableName+ " VALUES (?)";
		PreparedStatement ps = connection_.prepareStatement (sql);
		for (int i = 0; i < inputAndExpected.length; i++) {
		    ps.setString(1,inputAndExpected[i][0]);
		    ps.executeUpdate();
		}
		ps.close();

		int row = 0;
		sql = "SELECT  C1, HEX(C1) FROM "+tableName;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
		    String stringOut = rs.getString(1);
		    String hexOut    = rs.getString(2);

		    if (!inputAndExpected[row][1].equals(stringOut)) {
			sb.append("\nExpected '"+inputAndExpected[row][1]+"'"+
                                  "\nGot      '"+stringOut+"'");
			passed = false;
		    }

		    if (!inputAndExpected[row][2].equals(hexOut)) {
			sb.append("\nExpected Hex '"+inputAndExpected[row][2]+"'"+
                                  "\nGot Hex      '"+hexOut+"'");
			passed = false;
		    }

		    row++;
		}
		if (row != inputAndExpected.length) {
		    passed = false;
		    sb.append("\nExpected "+inputAndExpected.length+" rows but got "+row+" rows");
		} else {
		    sb.append("\nNumber of rows is "+row);
		}
		rs.close();
		stmt.close();

		assertCondition(passed, sb.toString()+" -- added 4/10/2009 by native driver");
	    } catch (Exception e) {
		failed(e, "Unexpected Exception :  SQL was probably "+sql);
	    }


    }


    public void Var114 () {
	String[][] testStrings = {
	    {
		" \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00a2.<(+|"+
		  "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df!$*);\u00ac"+
		  "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"+
		  "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""+
		  "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"+
		  "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"+
		  "\u00b5~stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"+
		  "^\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be[]\u00af\u00a8\u00b4\u00d7"+
		  "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"+
		  "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"+
		  "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"+
		  "0123456789\u00b3\u00db\u00dc\u00d9\u00da",
		" \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00a2.<(+|"+
		"&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df!$*);\u00ac"+
		"-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"+
		"\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""+
		"\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"+
		"\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"+
		"\u00b5~stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"+
		"^\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be[]\u00af\u00a8\u00b4\u00d7"+
		"{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"+
		"}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"+
		"\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"+
		"0123456789\u00b3\u00db\u00dc\u00d9\u00da",
		"404142434445464748494A4B4C4D4E4F"+
		"505152535455565758595A5B5C5D5E5F"+
		"606162636465666768696A6B6C6D6E6F"+
		"707172737475767778797A7B7C7D7E7F"+
		"808182838485868788898A8B8C8D8E8F"+
		"909192939495969798999A9B9C9D9E9F"+
		"A0A1A2A3A4A5A6A7A8A9AAABACADAEAF"+
		"B0B1B2B3B4B5B6B7B8B9BABBBCBDBEBF"+
		"C0C1C2C3C4C5C6C7C8C9CACBCCCDCECF"+
		"D0D1D2D3D4D5D6D7D8D9DADBDCDDDEDF"+
		"E0E1E2E3E4E5E6E7E8E9EAEBECEDEEEF"+
		"F0F1F2F3F4F5F6F7F8F9FAFBFCFDFE"},

	    {
		"SUB \u01cc SUB",
		"SUB \u001A SUB",
		"E2E4C2403F40E2E4C2"
	    },

		{ "SUB\u02c7SUB",
		  "SUB\u001ASUB",
		"E2E4C23FE2E4C2"},

		{ "SUB \u02c7 SUB",
		  "SUB \u001A SUB",
		"E2E4C2403F40E2E4C2"}
	};
	if (getRelease() <= JDTestDriver.RELEASE_V5R3M0) {
	    notApplicable("Not fixing v5r3 -- going out of service");
	} else {
	    testCCSID(37,testStrings);
	}
    }


    public void Var115 () {
	String[][] testStrings = {
	    {
		" \u00a0\u00e2{\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00c4.<(+!"+
		  "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec~\u00dc$*);^"+
		  "-/\u00c2[\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00f6,%_>\u003f"+
		  "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#\u00a7\u0027=\""+
		  "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"+
		  "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"+
		  "\u00b5\u00dfstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"+
		  "\u00a2\u00a3\u00a5\u00b7\u00a9@\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"+
		  "\u00e4ABCDEFGHI\u00ad\u00f4\u00a6\u00f2\u00f3\u00f5"+
		  "\u00fcJKLMNOPQR\u00b9\u00fb}\u00f9\u00fa\u00ff"+
		  "\u00d6\u00f7STUVWXYZ\u00b2\u00d4\\\u00d2\u00d3\u00d5"+
		  "0123456789\u00b3\u00db]\u00d9\u00da",

		" \u00a0\u00e2{\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00c4.<(+!"+
		"&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec~\u00dc$*);^"+
		"-/\u00c2[\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00f6,%_>\u003f"+
		"\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#\u00a7\u0027=\""+
		"\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"+
		"\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"+
		"\u00b5\u00dfstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"+
		"\u00a2\u00a3\u00a5\u00b7\u00a9@\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"+
		"\u00e4ABCDEFGHI\u00ad\u00f4\u00a6\u00f2\u00f3\u00f5"+
		"\u00fcJKLMNOPQR\u00b9\u00fb}\u00f9\u00fa\u00ff"+
		"\u00d6\u00f7STUVWXYZ\u00b2\u00d4\\\u00d2\u00d3\u00d5"+
		"0123456789\u00b3\u00db]\u00d9\u00da",

		"404142434445464748494A4B4C4D4E4F"+
		"505152535455565758595A5B5C5D5E5F"+
		"606162636465666768696A6B6C6D6E6F"+
		"707172737475767778797A7B7C7D7E7F"+
		"808182838485868788898A8B8C8D8E8F"+
		"909192939495969798999A9B9C9D9E9F"+
		"A0A1A2A3A4A5A6A7A8A9AAABACADAEAF"+
		"B0B1B2B3B4B5B6B7B8B9BABBBCBDBEBF"+
		"C0C1C2C3C4C5C6C7C8C9CACBCCCDCECF"+
		"D0D1D2D3D4D5D6D7D8D9DADBDCDDDEDF"+
		"E0E1E2E3E4E5E6E7E8E9EAEBECEDEEEF"+
		"F0F1F2F3F4F5F6F7F8F9FAFBFCFDFE"
	    },

	    {
		"SUB \u01cc SUB",
		"SUB \u001A SUB",
		"E2E4C2403F40E2E4C2"
	    },

		{ "SUB\u02c7SUB",
		  "SUB\u001ASUB",
		"E2E4C23FE2E4C2"},

	    {
		"SUB \u02c7 SUB",
		"SUB \u001A SUB",
		"E2E4C2403F40E2E4C2"

	    },
	    {
		"SUB\u0130\u017f\u0233\u02ad\u047f\u04ef\u0f28\u20b3\u3013\u02c7SUB",
		"SUB\u001A\u001A\u001A\u001A\u001A\u001A\u001A\u001A\u001A\u001ASUB",
		"E2E4C23F3F3F3F3F3F3F3F3F3FE2E4C2"
	    }


	};
	if (getRelease() <= JDTestDriver.RELEASE_V5R3M0) {
	    notApplicable("Not fixing v5r3 -- going out of service");
	} else {
	    testCCSID(273,testStrings);
	}
    }



    /**
    setString() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. D1C
    **/
        public void Var116()
        {
	    if (getRelease() <= JDTestDriver.RELEASE_V5R4M0 &&
		getDriver() == JDTestDriver.DRIVER_NATIVE) {
		notApplicable("Invalid DATE test not applicable for V5R4");
		return;
	    }
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATE) VALUES (?)");

                if (isToolboxDriver())
                    ps.setString (1, "01/33/2009"); //33 is bad
                else
                    ps.setString (1, "2009-01-33");
		ps.executeUpdate();
                ps.close ();
                failed ("Didn't throw SQLException for invalid date - Added by TB 6/16/2009");
            }
            catch (Exception e) {

                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }



    /**
    setString() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. D1C.
    **/
        public void Var117()
        {
	    if (getRelease() <= JDTestDriver.RELEASE_V5R4M0 &&
		getDriver() == JDTestDriver.DRIVER_NATIVE) {
		notApplicable("Invalid TIME test not applicable for V5R4");
		return;
	    }
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_TIME) VALUES (?)");
                ps.setString (1, "14:61var0:55"); //61 is bad
		ps.executeUpdate();

                ps.close ();
                failed ("Didn't throw SQLException for invalid time - Added by TB 6/16/2009");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }

    /**
    setString() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
    **/
        public void Var118()
        {

          Statement s = null ; 
            PreparedStatement ps = null; 
            
            try {
              
              s = connection_.createStatement(); 
              s.executeUpdate("delete from "+JDPSTest.PSTEST_SET) ;
              
               ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_TIMESTAMP) VALUES (?)");
                ps.setString (1,"2009-01-32-00.00.00.000000");//day 32 is bad
		ps.executeUpdate();

                ps.close ();
                
                ResultSet rs = s.executeQuery ("SELECT C_TIMESTAMP FROM "+JDPSTest.PSTEST_SET);
                String timestampString = "NOT SET"; 
                while (rs.next()) { 
                  timestampString = rs.getString(1); 
                }
                s.executeUpdate("delete from "+JDPSTest.PSTEST_SET) ;
                
                // Note:  toolbox now allows a bad timestamp to be passed.  
                // We let the database take the appropriate action. 
                if (timestampString.equals("2009-02-01 00:00:00.000000")) {
                  assertCondition(true); 
                } else { 
                   failed ("Didn't throw SQLException for invalid timestamp but got "+timestampString+" - Updated by TB 5/18/2016");
                } 
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            } finally { 
                try { if (s != null) s.close(); } catch (Exception e) {}  
                try {if (ps != null) ps.close(); } catch (Exception e) {} 
            }
        }

	public void Var119() { notApplicable(); }
	public void Var120() { notApplicable(); }
	public void Var121() { notApplicable(); }
	public void Var122() { notApplicable(); }
	public void Var123() { notApplicable(); }


/**
setString() - Set an XML  parameter.
**/
	public void setXML(String tablename, String data, String expected) {
	    String added = " -- added by native driver 08/21/2009";

	    if (getDriver() == JDTestDriver.DRIVER_JCC) {


		if ((tablename.indexOf("13488") > 0) ||
		    (tablename.indexOf("1200") > 0) ||
		    (tablename.indexOf("37") > 0) ||
		    (tablename.indexOf("290") > 0) ) {
		    notApplicable("XML CCSID Not applicable for JCC");
		    return;
		}

	    }

		if (checkXmlSupport ()) {
		    try {
			statement_.executeUpdate ("DELETE FROM " + tablename);

			PreparedStatement ps = connection_.prepareStatement (
									     "INSERT INTO " + tablename
									     + "  VALUES (?)");

			ps.setString(1, data);
			ps.executeUpdate ();
			ps.close ();

			ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename);
			rs.next ();
			String check = rs.getString (1);
			rs.close ();

			assertCondition (check.equals (expected),
					 "check = "+
					 JDTestUtilities.getMixedString(check)+
					 " And SB "+
					 JDTestUtilities.getMixedString(expected)+
					 added);
		    }
		    catch (Exception e) {
			failed (e, "Unexpected Exception"+added);
		    }
		}

	}

/**
setString() - Set an XML  parameter using invalid data.
**/
	public void setInvalidXML(String tablename, String data, String expectedException) {
	    String added = " -- added by native driver 08/21/2009";
	    if (checkJdbc40()) {
		if (checkXmlSupport ()) {
		    try {
			statement_.executeUpdate ("DELETE FROM " + tablename);

			PreparedStatement ps = connection_.prepareStatement (
									     "INSERT INTO " + tablename
									     + "  VALUES (?)");

				ps.setString(1, data);

			ps.executeUpdate ();
			ps.close ();

			ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename);
			rs.next ();
			String check = rs.getString (1);
			rs.close ();
			failed("Didn't throw exception but got "+
			       JDTestUtilities.getMixedString(check)+added);

		    }
		    catch (Exception e) {

			String message = e.toString();
			if (message.indexOf(expectedException) >= 0) {
			    assertCondition(true);
			} else {
			    failed (e, "Unexpected Exception.  Expected "+expectedException+added);
			}
		    }
		}
	    }
	}
	public void Var124() { setXML(JDPSTest.PSTEST_SETXML,  "<Test>Var124\u00a2</Test>",  "<Test>Var124\u00a2</Test>"); }
	public void Var125() { setXML(JDPSTest.PSTEST_SETXML,  "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var125\u00a2</Test>",  "<Test>Var125\u00a2</Test>"); }

	public void Var126() { setXML(JDPSTest.PSTEST_SETXML,  "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>Var126\u0130\u3041\ud800\udf30</Test>",  "<Test>Var126\u0130\u3041\ud800\udf30</Test>"); }

	public void Var127() { setXML(JDPSTest.PSTEST_SETXML13488, "<Test>Var127</Test>",  "<Test>Var127</Test>"); }
	public void Var128() { setXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var128\u00a2</Test>",  "<Test>Var128\u00a2</Test>"); }
	public void Var129() { setXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>Var129\u0130\u3041</Test>",  "<Test>Var129\u0130\u3041</Test>"); }

	public void Var130() { setXML(JDPSTest.PSTEST_SETXML1200, "<Test>Var130</Test>",  "<Test>Var130</Test>"); }
	public void Var131() { setXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var131\u00a2</Test>",  "<Test>Var131\u00a2</Test>"); }
	public void Var132() { setXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>Var132\u0130\u3041\ud800\udf30</Test>",  "<Test>Var132\u0130\u3041\ud800\udf30</Test>"); }

	public void Var133() { setXML(JDPSTest.PSTEST_SETXML37, "<Test>Var133\u00a2</Test>",  "<Test>Var133\u00a2</Test>"); }
	public void Var134() { setXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var134\u00a2</Test>",  "<Test>Var134\u00a2</Test>"); }
	public void Var135() { setXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>Var135\u00a2</Test>",  "<Test>Var135\u00a2</Test>"); }

	public void Var136() { setXML(JDPSTest.PSTEST_SETXML937, "<Test>Var136\u672b</Test>",  "<Test>Var136\u672b</Test>"); }
	public void Var137() { setXML(JDPSTest.PSTEST_SETXML937, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var137\u672b</Test>",  "<Test>Var137\u672b</Test>"); }
	public void Var138() { setXML(JDPSTest.PSTEST_SETXML937, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>Var138\u672b</Test>",  "<Test>Var138\u672b</Test>"); }

	public void Var139() { setXML(JDPSTest.PSTEST_SETXML290, "<Test>Var139</Test>",  "<Test>Var139</Test>"); }
	public void Var140() { setXML(JDPSTest.PSTEST_SETXML290, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var140\uff7a</Test>",  "<Test>Var140\uff7a</Test>"); }
	public void Var141() { setXML(JDPSTest.PSTEST_SETXML290, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>Var141\uff98</Test>",  "<Test>Var141\uff98</Test>"); }



	   /* Encoding is stripped for character data since we know is is UTF-16 */
	public void Var142() { setXML(JDPSTest.PSTEST_SETXML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>Var142</Test>",  "<Test>Var142</Test>"); }
	public void Var143() { setInvalidXML(JDPSTest.PSTEST_SETXML, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>Var143</Tes>",  "XML parsing failed"); }

	public void Var144() { setXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>Var144</Test>",  "<Test>Var144</Test>" ); }
	public void Var145() { setInvalidXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tes>Var145</Test>",  "XML parsing failed"); }

	public void Var146() { setXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>Var146</Test>",  "<Test>Var146</Test>"); }
	public void Var147() { setInvalidXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tes>Var147</Test>",  "XML parsing failed"); }

	public void Var148() { setXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>Var148\u00a2</Test>",  "<Test>Var148\u00a2</Test>"); }
	public void Var149() { setInvalidXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tet>Var149</Test>",  "XML parsing failed"); }



	/* Set XML with a processing instruction (which should be left) */
	public void Var150() { setXML(JDPSTest.PSTEST_SETXML,  "<?attribute list?><Test>Var150</Test>",  "<?attribute list?><Test>Var150</Test>"); }



	/* Check for truncation on setting parameter marker for a query.  This should throw an exception. */
        /* Earlier version of the driver did not cause this exception.  */

	public void Var151() {
	    if (getRelease() <= JDTestDriver.RELEASE_V5R4M0) {
		notApplicable("V6R1 or later test");
		return;
	    }
	    try {

		// Use a connection with query replace truncated parameter set
		  String url = baseURL_
		  
		  
		  + ";query replace truncated parameter=\u0164"; 
		Connection connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

		PreparedStatement ps = connection.prepareStatement (
								     "select * from sysibm.sysdummy1 where IBMREQD=?");
		String parameterMarker = "YY";
		ps.setString (1, parameterMarker);

		ResultSet rs = ps.executeQuery();
		int resultCount = 0;
		while (rs.next()) {
		    resultCount++;
		}
		ps.close ();
		connection.close(); 
		assertCondition(resultCount == 0, "Didn't throw SQLException and result count was "+resultCount+" Query used a parameter marker of '"+parameterMarker+"' but column is CHAR(1)" );
	    }
	    catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1)
				 && (dt.getParameter() == true)
				 && (dt.getRead() == false)
				 && (dt.getDataSize() == 2)
				 && (dt.getTransferSize() == 1), "DataTruncation "+dt.getIndex()+" "+dt.getParameter()+" "+dt.getRead()+" "+dt.getDataSize()+" "+dt.getTransferSize()+" "+dt.getRead());
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }

	}


	public void setDecimalValueWithDecimalSeparatorComma(String query, String value) {

	    if (getRelease() == JDTestDriver.RELEASE_V5R4M0 && getDriver() == JDTestDriver.DRIVER_NATIVE) {
		notApplicable("Not fixing decimal separator problem on V5R4");
		return; 
	    } 
	    try {


		  String url = baseURL_
		  
		  
		  + ";decimal separator=,";

		Connection connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

		PreparedStatement ps = connection.prepareStatement(query);
		ps.setString(1,value);
		ResultSet rs = ps.executeQuery();
		rs.next();
		String output = rs.getString(1);
		connection.close();

		assertCondition(output.equals(value), "got "+output+" sb "+value+" from query "+query);

	    } catch (Exception e) {
		failed (e, "Unexpected Exception -- setting "+value+" when decimal separator is a comma query = "+query);
	    }

	}

	/* Test setting a value using a value with a decimal separator */

	public void Var152() { setDecimalValueWithDecimalSeparatorComma(" select cast(? as decimal(10, 2)) from sysibm.sysdummy1", "12345,68"); }
  public void Var153() { setDecimalValueWithDecimalSeparatorComma(" select cast(? as numeric(10, 2)) from sysibm.sysdummy1", "12345,68"); }
  public void Var154() {
      if (checkDecFloatSupport()) {
	  setDecimalValueWithDecimalSeparatorComma(" select cast(? as decfloat(16)) from sysibm.sysdummy1", "12345,68");
      }
  }
  public void Var155() {
      if (checkDecFloatSupport()) {

	  setDecimalValueWithDecimalSeparatorComma(" select cast(? as decfloat(34)) from sysibm.sysdummy1", "12345,68");
      }
  }
  public void Var156() { setDecimalValueWithDecimalSeparatorComma(" select cast(? as real) from sysibm.sysdummy1", "12345,68"); }
  public void Var157() { setDecimalValueWithDecimalSeparatorComma(" select cast(? as float) from sysibm.sysdummy1", "12345,68"); }
  public void Var158() { setDecimalValueWithDecimalSeparatorComma(" select cast(? as double) from sysibm.sysdummy1", "12345,68"); }


  // Testing the setting of CHAR parameters for UTF-8 and MIXED CCSIDS
  public void testSetMixed(String sql,
			   String parameter,
			   String expectedOutput) {
      try { 
      SQLWarning warning = null; 
      PreparedStatement ps = connection_.prepareStatement(sql);
      ps.setString(1,parameter);
      warning = ps.getWarnings(); 
      ResultSet rs = ps.executeQuery();
      if (warning == null) warning = ps.getWarnings(); 
      rs.next();
      if (warning == null) warning = rs.getWarnings(); 
      String output = rs.getString(1);
      if (warning == null) warning = rs.getWarnings(); 
      ps.close();
      assertCondition(warning == null && expectedOutput.equals(output), "warning="+warning+" expected '"+expectedOutput+"' got '"+output+"' for query="+sql+" and parameter='"+parameter+"'"); 
      } catch (Exception e) {
	  failed (e, "Unexpected Exception -- for sql="+sql+" parameter="+parameter); 
      }
  }

  public void Var159() { testSetMixed("select cast(? as CHAR(10) CCSID 1208) from sysibm.sysdummy1",
				      "\u00f0\u00f1",
				      "\u00f0\u00f1      ");
  }


  public void Var160() { testSetMixed("select cast(? as CHAR(10) CCSID 930) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885      ");
  }

  public void Var161() { testSetMixed("select cast(? as CHAR(10) CCSID 933) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885      ");
  }

  public void Var162() { testSetMixed("select cast(? as CHAR(10) CCSID 935) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885      ");
  }

  public void Var163() {
/* 
      if (isNative) {
	  testSetMixed("select cast(? as CHAR(10) CCSID 937) from sysibm.sysdummy1",
		       "\u6885",
		       "\u6885         ");
      } else {
*/ 
	  testSetMixed("select cast(? as CHAR(10) CCSID 937) from sysibm.sysdummy1",
		       "\u6885",
		       "\u6885      ");
/*
      }
 */ 
  }

  public void Var164() { testSetMixed("select cast(? as CHAR(10) CCSID 939) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885      ");
  }

  public void Var165() { testSetMixed("select cast(? as CHAR(10) CCSID 1364) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885      ");
  }

  public void Var166() { testSetMixed("select cast(? as CHAR(10) CCSID 1371) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885      ");
  }
    
  public void Var167() { testSetMixed("select cast(? as CHAR(10) CCSID 1388) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885      ");
  }

  public void Var168() { testSetMixed("select cast(? as CHAR(10) CCSID 1399) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885      ");
  }

  public void Var169() { testSetMixed("select cast(? as CHAR(10) CCSID 5026) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885      ");
  }

  public void Var170() { testSetMixed("select cast(? as CHAR(10) CCSID 5035) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885      ");
  }

  public void Var171() { testSetMixed("select cast(? as CHAR(10) CCSID 1208) from sysibm.sysdummy1",
				      "\u6885",
				      "\u6885       ");
  }


  public void testSetDBCS(String query,
			  String inputParameter,
			  String expectedResult) {
   StringBuffer sb = new StringBuffer();
   PreparedStatement ps = null;
   try {
       sb.append("Preparing "+query+"\n"); 
      ps  = connection_.prepareStatement(query);
      sb.append("Setting parm to "+JDTestUtilities.getMixedString(inputParameter)+"\n"); 
      ps.setString(1,inputParameter);
      sb.append("Executing query\n"); 
      ResultSet rs = ps.executeQuery();
      sb.append("Calling rs.next\n"); 
      rs.next();
      sb.append("Getting result\n"); 
      String result = rs.getString(1);


       assertCondition(expectedResult.equals(result), "Error: \n" +
       		"got       '"+JDTestUtilities.getMixedString(result)+"'\n" +
       		"should be '"+JDTestUtilities.getMixedString(expectedResult)+"'\n"+sb.toString()); 

   } catch (Exception e) {
       failed(e, "Unexpected exception "+sb.toString());
       
   } finally {
       try {
	   if (ps != null)   ps.close(); 
       } catch (Exception e) { 
         System.out.println("Unexpected error "); 
         e.printStackTrace(System.out); 
       }
   } 

  } 



/**
 * Test the following combinations
 *
Unicode => EBCDIC Conversion.                                            
+---------+--------------+                                               
| UNICODE |TARGET EBCDIC |                                               
| SOUCRE  | 5035 | 1399  |                                               
|         | 5026 |       |                                               
|         |  300 | 16684 |                                               
|         | 4396 |       |                                               
+---------+------+-------+                                               
| 2212    | 4260 | 4260  |                                               
| FF0D    | 4260 | E9F3  |                                               
| 2014    | 444A | 444A  |                                               
| 2015    | 444A | DDB7  |                                               
| 301C    | 43A1 | 43A1  |                                               
| FF5E    | 43A1 | E9F4  |                                               
| 2016    | 447C | 447C  |                                               
| 2225    | 447C | DFE5  |                                               
| 00A6    | 426A | 426A  |                                               
| FFE4    | 426A | E9F5  |                                               
+---------+--------------+

*/

  public void Var172() {
      testSetDBCS(
		  "select HEX(CAST(? AS VARCHAR(80) CCSID 5035)) from sysibm.sysdummy1",
		  "\u2212\uFF0D\u2014\u2015\u301C\uFF5E\u2016\u2225\u00A6\uFFE4",
		  "0E42604260444A444A43A143A1447C447C426A426A0F"); 

  } 

  public void Var173() {
    testSetDBCS(
    "select HEX(CAST(? AS VARCHAR(80) CCSID 5026)) from sysibm.sysdummy1",
    "\u2212\uFF0D\u2014\u2015\u301C\uFF5E\u2016\u2225\u00A6\uFFE4",
    "0E42604260444A444A43A143A1447C447C426A426A0F"); 
  }

  public void Var174() {
    testSetDBCS(
    "select HEX(CAST(? AS VARGRAPHIC(80) CCSID 300)) from sysibm.sysdummy1",
    "\u2212\uFF0D\u2014\u2015\u301C\uFF5E\u2016\u2225\u00A6\uFFE4",
    "42604260444A444A43A143A1447C447C426A426A"); 
  }
  
  public void Var175() {
    testSetDBCS(
    "select HEX(CAST(? AS VARGRAPHIC(80) CCSID 4396)) from sysibm.sysdummy1",
    "\u2212\uFF0D\u2014\u2015\u301C\uFF5E\u2016\u2225\u00A6\uFFE4",
    "42604260444A444A43A143A1447C447C426A426A"); 
  }
  


  public void Var176() {
      testSetDBCS(
		  "select HEX(CAST(? AS VARCHAR(80) CCSID 1399)) from sysibm.sysdummy1",
		  "\u2212\uFF0D\u2014\u2015\u301C\uFF5E\u2016\u2225\u00A6\uFFE4",
		  "0E4260E9F3444ADDB743A1E9F4447CDFE5426AE9F50F"); 

  } 


  public void Var177() {
      testSetDBCS(
		  "select HEX(CAST(? AS VARGRAPHIC(80) CCSID 16684)) from sysibm.sysdummy1",
		  "\u2212\uFF0D\u2014\u2015\u301C\uFF5E\u2016\u2225\u00A6\uFFE4",
		  "4260E9F3444ADDB743A1E9F4447CDFE5426AE9F5"); 

  } 


  /**
   * Call set string with a timestamp that may not exist in the current
   * timezone
   */

  public void Var178() {
      StringBuffer sb = new StringBuffer(" -- added 5/16/2016 to test timestamp that may not exist in timezeon\n");

      PreparedStatement ps = null; 
      String query="select cast(? as timestamp) from sysibm.sysdummy1";
      String inputParameter = "2016-03-13 02:15:00.000000"; 
      try {
	  
	  sb.append("Preparing "+query+"\n"); 
	  ps  = connection_.prepareStatement(query);
	  sb.append("Setting parm to "+inputParameter+"\n"); 
	  ps.setString(1,inputParameter);
	  sb.append("Executing query\n"); 
	  ResultSet rs = ps.executeQuery();
	  sb.append("Calling rs.next\n"); 
	  rs.next();
	  sb.append("Getting result\n"); 
	  String result = rs.getString(1);
	  ps.close();
	  ps = null; 
	  assertCondition(inputParameter.equals(result), "Error: \n" +
			  "got       '"+result+"'\n" +
			  "should be '"+inputParameter+"'\n"+sb.toString()); 


      } catch (Exception e) {
	  try { 
	      if (ps != null) ps.close();
	  } catch (Exception e2) {}
	  failed(e, sb.toString()); 
      } 

      

  } 





  /* Test all types of invalid timestamp.. Both native and toolbox should throw errors */

  public void Var179() {
      StringBuffer sb = new StringBuffer(" -- added 3/15/2017 to test for invalid timestamps\n"); 
      try {
	  boolean passed = true;

	  PreparedStatement pstmt = connection_.prepareStatement("select cast(? as timestamp) from sysibm.sysdummy1"); 
	  String[]  exceptionStrings = {
	      "-001-01-01-01:01:01.000001",
	      "1999--1-01-01:01:01.000001",
	      "1999-13-01-01:01:01.000001",
	      "1999-01--1-01:01:01.000001",
	      "1999-01-32-01:01:01.000001",
	      "1999-01-01--1:01:01.000001",
	      "1999-01-01-60:01:01.000001",
	      "1999-01-01-01:-1:01.000001",
	      "1999-01-01-01:60:01.000001",
	      "1999-01-01-01:01:-1.000001",
	      "1999-01-01-01:01:60.000001",
	      "1999-01-01-01:01:01.-00001",
        "1999-01-01-01:01:01.0-00001",
	      "1999-01-01-24:01:00.000000",
	      "1999-01-01-24:00:01.000000",
	      "1999-01-01-24:00:00.000001",
	      "1999-02-29-01:00:00.000000",
	      "2000-02-30-01:00:00.000000",
	      "2000-04-31-01:00:00.000000",
	      "2000-06-31-01:00:00.000000",
	      "2000-09-31-01:00:00.000000",
	      "2000-11-31-01:00:00.000000",
        "199a-01-01-12:01:00.000000",
        "1999-0a-01-12:01:00.000000",
        "1999-01-0a-12:01:00.000000",
        "1999-01-01-1a:01:00.000000",
        "1999-01-01-12:0a:00.000000",
        "1999-01-01-12:01:0a.000000",
        "1999-01-01-12:01:00.00000a",

	  }; 

	  for (int i = 0; i < exceptionStrings.length; i++) {
	      try {
	        pstmt.setString(1, exceptionStrings[i]);
	        ResultSet rs = pstmt.executeQuery();
	        rs.next(); 
	        String value = rs.getString(1); 
		  passed = false;
		  sb.append("Did not throw exception for "+exceptionStrings[i]+" but got "+value+"\n"); 
	      } catch (SQLException sqlex) {
	        boolean headerPrinted = false; 
	        String sqlState = sqlex.getSQLState();
	        String message =  sqlex.getMessage();
	        int    sqlCode =  sqlex.getErrorCode();
	        // -180 and -181 found when toolbox sends bad data to server
	        if ((sqlCode != -180) &&
	            (sqlCode != -181) && 
	            (sqlCode != -99999)) {
	          passed = false; 
	          if (!headerPrinted) {
	            headerPrinted = true; 
	            sb.append("For "+exceptionStrings[i]+"\n"); 
              printStackTraceToStringBuffer(sqlex, sb); 
	          }
	          sb.append("sqlCode = "+sqlCode+"sb -180, -181, or -99999\n");
	        }
	        if (!"22007".equals(sqlState) &&
	            !"07006".equals(sqlState)) {
            passed = false; 
            if (!headerPrinted) {
              headerPrinted = true; 
              sb.append("For "+exceptionStrings[i]+"\n");
              printStackTraceToStringBuffer(sqlex, sb); 
            }
	          sb.append("sqlState = "+sqlState+" sb 22007 or 07006 \n");
	        }
	        if ((message.indexOf("value not valid") < 0) &&
	            (message.indexOf("string not valid")< 0) &&
              (message.indexOf("Data type mismatch")< 0)) {
            passed = false; 
            if (!headerPrinted) {
              headerPrinted = true; 
              sb.append("For "+exceptionStrings[i]+"\n");
              printStackTraceToStringBuffer(sqlex, sb); 
            }
            sb.append("sqlMessage = "+message+" should have 'value not valid' or 'string not valid'\n");
	          
	        }
	      } catch (Throwable e) {
		  passed = false;
		  sb.append("Unexpected throwablefor "+exceptionStrings[i]+"\n");
		  printStackTraceToStringBuffer(e, sb); 
	      }
	  } 
	  pstmt.close(); 
	  assertCondition(passed,sb); 

      } catch (Exception e) {
	  failed(e, "Unexpected exception "+sb.toString()); 
      }
  }


  /**
   * Make sure we don't get truncation from types where the
   * database does the translation.
   */

  public void Var180() {
      StringBuffer sb = new StringBuffer(" -- added 5/8/2017 to test type where database does the translation -- See issue 58501\n");

      PreparedStatement ps = null; 
      String query="select * from sysibm.sysdummy1 where ? = CAST(UX'e080e080005a005a' AS CHAR(8) CCSID 937)";
      String inputParameter = "\ue080\ue080\u005a\u005a"; 
      try {
	  
	  sb.append("Preparing "+query+"\n"); 
	  ps  = connection_.prepareStatement(query);
	  sb.append("Setting parm to "+inputParameter+"\n"); 
	  ps.setString(1,inputParameter);
	  sb.append("Executing query\n"); 
	  ResultSet rs = ps.executeQuery();
	  sb.append("Calling rs.next\n"); 
	  rs.next();
	  sb.append("Getting result\n"); 
	  String result = rs.getString(1);
	   SQLWarning rsWarn = rs.getWarnings();
	   SQLWarning psWarn = ps.getWarnings(); 
	  ps.close();
	  ps = null; 
	  assertCondition((rsWarn == null) && (psWarn == null ) && "Y".equals(result), "Error: \n" +
			  "got       '"+result+"' sb 'Y'\n" +
			  "or warnings not null\n"+
			  "rsWarn="+rsWarn+"\n"+
			  "psWarn="+psWarn+"\n" 
			  +sb.toString()); 


      } catch (Exception e) {
	  try { 
	      if (ps != null) ps.close();
	  } catch (Exception e2) {}
	  failed(e, sb.toString()); 
      } 

      

  } 


  public void Var181() {

      StringBuffer sb = new StringBuffer(" -- added 9/12/2017 to test type where database does the translation -- See CPS AQZDPW\n");

      PreparedStatement ps = null; 
      String query="select cast(? AS CHAR(4) CCSID 937), CAST( ? AS CHAR(4)), CAST( ? AS CHAR(4) CCSID 937), CAST(? AS CHAR(4)) from sysibm.sysdummy1 ";
      String p1 = "1234";
      String p2 = "5678";
      String p3 = "abcd";
      String p4 = "wxyz"; 
      try {
	  boolean passed = true; 
	  sb.append("Preparing "+query+"\n"); 
	  ps  = connection_.prepareStatement(query);
	  sb.append("Setting parm1 to "+p1+"\n"); 
	  ps.setString(1,p1);
	  sb.append("Setting parm2 to "+p2+"\n"); 
	  ps.setString(2,p2);
	  sb.append("Setting parm3 to "+p3+"\n"); 
	  ps.setString(3,p3);
	  sb.append("Setting parm4 to "+p4+"\n"); 
	  ps.setString(4,p4);
	  sb.append("Executing query\n"); 
	  ResultSet rs = ps.executeQuery();
	  sb.append("Calling rs.next\n"); 
	  rs.next();
	  sb.append("Getting result\n"); 
	  String r1 = rs.getString(1);
	  if (!p1.equals(r1)) {
	      passed = false;
	      sb.append("Got '"+r1+"' expected '"+p1+"'\n"); 
	  } 
	  String r2 = rs.getString(2);
	  if (!p2.equals(r2)) {
	      passed = false;
	      sb.append("Got '"+r2+"' expected '"+p2+"'\n"); 
	  } 
	  String r3 = rs.getString(3);
	  if (!p3.equals(r3)) {
	      passed = false;
	      sb.append("Got '"+r3+"' expected '"+p3+"'\n"); 
	  } 
	  String r4 = rs.getString(4);
	  if (!p4.equals(r4)) {
	      passed = false;
	      sb.append("Got '"+r4+"' expected '"+p4+"'\n"); 
	  } 

          SQLWarning rsWarn = rs.getWarnings();
	  SQLWarning psWarn = ps.getWarnings(); 
	  ps.close();
	  ps = null;

	  if (rsWarn != null) {
	      passed = false;
	      sb.append("Got warning "+rsWarn+"\n");
	  } 
	  if (psWarn != null) {
	      passed = false;
	      sb.append("Got warning "+psWarn+"\n");
	  } 

	  assertCondition(passed, "Error: \n" + sb.toString()); 


      } catch (Exception e) {
	  try { 
	      if (ps != null) ps.close();
	  } catch (Exception e2) {}
	  failed(e, sb.toString()); 
      } 

      


  }



  public void Var182() {

      StringBuffer sb = new StringBuffer(" -- added 9/12/2017 to test type where database does the translation -- See CPS AQZDPW\n");

      PreparedStatement ps = null; 
      String query="select cast(? AS CHAR(4) CCSID 937), CAST( ? AS CHAR(4)), CAST( ? AS CHAR(4) CCSID 937), CAST(? AS CHAR(4)) from sysibm.sysdummy1 ";
      String p1 = "1234";
      String p2 = "5678";
      String p3 = "abcd";
      String p4 = "wxyz";

      try {
	  boolean passed = true; 
	  sb.append("Preparing "+query+"\n"); 
	  ps  = connection_.prepareStatement(query);
	  sb.append("Setting parm4 to "+p4+"\n"); 
	  ps.setString(4,p4);
	  sb.append("Setting parm3 to "+p3+"\n"); 
	  ps.setString(3,p3);
	  sb.append("Setting parm2 to "+p2+"\n"); 
	  ps.setString(2,p2);
	  sb.append("Setting parm1 to "+p1+"\n"); 
	  ps.setString(1,p1);

	  sb.append("Executing query\n"); 
	  ResultSet rs = ps.executeQuery();
	  sb.append("Calling rs.next\n"); 
	  rs.next();
	  sb.append("Getting result\n"); 
	  String r1 = rs.getString(1);
	  if (!p1.equals(r1)) {
	      passed = false;
	      sb.append("Got '"+r1+"' expected '"+p1+"'\n"); 
	  } 
	  String r2 = rs.getString(2);
	  if (!p2.equals(r2)) {
	      passed = false;
	      sb.append("Got '"+r2+"' expected '"+p2+"'\n"); 
	  } 
	  String r3 = rs.getString(3);
	  if (!p3.equals(r3)) {
	      passed = false;
	      sb.append("Got '"+r3+"' expected '"+p3+"'\n"); 
	  } 
	  String r4 = rs.getString(4);
	  if (!p4.equals(r4)) {
	      passed = false;
	      sb.append("Got '"+r4+"' expected '"+p4+"'\n"); 
	  } 

          SQLWarning rsWarn = rs.getWarnings();
	  SQLWarning psWarn = ps.getWarnings(); 
	  ps.close();
	  ps = null;

	  if (rsWarn != null) {
	      passed = false;
	      sb.append("Got warning "+rsWarn+"\n");
	  } 
	  if (psWarn != null) {
	      passed = false;
	      sb.append("Got warning "+psWarn+"\n");
	  } 

	  assertCondition(passed, "Error: \n" + sb.toString()); 


      } catch (Exception e) {
	  try { 
	      if (ps != null) ps.close();
	  } catch (Exception e2) {}
	  failed(e, sb.toString()); 
      } 

      


  }




  public void Var183() {

      StringBuffer sb = new StringBuffer(" -- added 9/12/2017 to test type where database does the translation -- See CPS AQZDPW\n");

      PreparedStatement ps = null; 
      String query="select cast(? AS CHAR(17000) CCSID 937), CAST( ? AS CHAR(4)) from sysibm.sysdummy1 ";
      char[] sampleChars = { 'a','b','c','d','e','f','g' };

      char[] chars = new char[17000];
      for (int i = 0; i < chars.length; i++) {
	  chars[i] = sampleChars[i%sampleChars.length]; 
      } 
      String p1 = new String(chars); 
      String p2 = "1234";

      try {
	  boolean passed = true; 
	  sb.append("Preparing "+query+"\n"); 
	  ps  = connection_.prepareStatement(query);
	  sb.append("Setting parm1 to "+p1+"\n"); 
	  ps.setString(1,p1);
	  sb.append("Setting parm2 to "+p2+"\n"); 
	  ps.setString(2,p2);
	  sb.append("Executing query\n"); 
	  ResultSet rs = ps.executeQuery();
	  sb.append("Calling rs.next\n"); 
	  rs.next();
	  sb.append("Getting result\n"); 
	  String r1 = rs.getString(1);
	  if (!p1.equals(r1)) {
	      passed = false;
	      sb.append("Got '"+r1+"' expected '"+p1+"'\n"); 
	  } 
	  String r2 = rs.getString(2);
	  if (!p2.equals(r2)) {
	      passed = false;
	      sb.append("Got '"+r2+"' expected '"+p2+"'\n"); 
	  } 

          SQLWarning rsWarn = rs.getWarnings();
	  SQLWarning psWarn = ps.getWarnings(); 
	  ps.close();
	  ps = null;

	  if (rsWarn != null) {
	      passed = false;
	      sb.append("Got warning "+rsWarn+"\n");
	  } 
	  if (psWarn != null) {
	      passed = false;
	      sb.append("Got warning "+psWarn+"\n");
	  } 

	  assertCondition(passed, "Error: \n" + sb.toString()); 


      } catch (Exception e) {
	  try { 
	      if (ps != null) ps.close();
	  } catch (Exception e2) {}
	  failed(e, sb.toString()); 
      } 

      


  }

  public void Var184() {

      StringBuffer sb = new StringBuffer(" -- added 9/12/2017 to test type where database does the translation -- See CPS AQZDPW\n");

      PreparedStatement ps = null; 
      String query="select cast(? AS CHAR(17000) CCSID 937), CAST( ? AS CHAR(4)) from sysibm.sysdummy1 ";
      char[] sampleChars = { 'a','b','c','d','e','f','g' };

      char[] chars = new char[17000];
      for (int i = 0; i < chars.length; i++) {
	  chars[i] = sampleChars[i%sampleChars.length]; 
      } 
      String p1 = new String(chars); 
      String p2 = "1234";

      try {
	  boolean passed = true; 
	  sb.append("Preparing "+query+"\n"); 
	  ps  = connection_.prepareStatement(query);
	  sb.append("Setting parm2 to "+p2+"\n"); 
	  ps.setString(2,p2);
	  sb.append("Setting parm1 to "+p1+"\n"); 
	  ps.setString(1,p1);
	  sb.append("Executing query\n"); 
	  ResultSet rs = ps.executeQuery();
	  sb.append("Calling rs.next\n"); 
	  rs.next();
	  sb.append("Getting result\n"); 
	  String r1 = rs.getString(1);
	  if (!p1.equals(r1)) {
	      passed = false;
	      sb.append("Got '"+r1+"' expected '"+p1+"'\n"); 
	  } 
	  String r2 = rs.getString(2);
	  if (!p2.equals(r2)) {
	      passed = false;
	      sb.append("Got '"+r2+"' expected '"+p2+"'\n"); 
	  } 

          SQLWarning rsWarn = rs.getWarnings();
	  SQLWarning psWarn = ps.getWarnings(); 
	  ps.close();
	  ps = null;

	  if (rsWarn != null) {
	      passed = false;
	      sb.append("Got warning "+rsWarn+"\n");
	  } 
	  if (psWarn != null) {
	      passed = false;
	      sb.append("Got warning "+psWarn+"\n");
	  } 

	  assertCondition(passed, "Error: \n" + sb.toString()); 


      } catch (Exception e) {
	  try { 
	      if (ps != null) ps.close();
	  } catch (Exception e2) {}
	  failed(e, sb.toString()); 
      } 

      


  }


  
  /**
   * setInt() - Set an parameter for a column of a specified type.
   **/
  public void testSetString(String columnName, String[][] testValues) {
    try {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");

      for (int i = 0; i < testValues.length; i++) {
        String value = testValues[i][0];
        String expectedValue = testValues[i][1];

        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        ps.setString(1, value);
        ps.executeUpdate();

        ResultSet rs = statement_.executeQuery(
            "SELECT " + columnName + " FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        String check = "" + rs.getString(1);
        rs.close();
        if (!expectedValue.equals(check)) {
          passed = false;
          sb.append(" got " + check + " sb " + expectedValue + " from " + value
              + "\n");
        }

      }
      ps.close();
      assertCondition(passed, sb);
      
    } catch (Exception e) {
      failed(e, "Unexpected Exception ");
    }
  }


  public void Var185() {
    if (checkBooleanSupport()) {
      String[][] testValues = { { "1", "1" }, { "0", "0" }, { "1000", "1" },
          { "-100", "1" }, { null, "null" },

      };
      testSetString("C_BOOLEAN", testValues);
    }
  }

  public void Var186() {
    if (checkBooleanSupport()) {
      String[][] testValues = { { "true", "1" }, { "false", "0" }, { "T", "1" },
          { "F", "0" }, { "yes", "1" }, { "no", "0" }, { null, "null" }, };
      testSetString("C_BOOLEAN", testValues);
    }
  }


}

