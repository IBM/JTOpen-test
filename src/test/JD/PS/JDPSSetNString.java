///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetNString.java
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
// File Name:    JDPSSetNString.java
//
// Classes:      JDPSSetNString
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestcase;
import test.JD.JDSetupPackage;
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDPSSetNString.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setNString()
</ul>
**/
public class JDPSSetNString
extends JDTestcase {



    // Private data.
    private Connection          connection_;
    private Statement           statement_;
    private String              properties_;


/**
Constructor.
**/
    public JDPSSetNString (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDPSSetNString",
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
setNString() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
	if (checkJdbc40()) { 
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER) VALUES (?)");
		ps.close ();
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
setNString() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
	if (checkJdbc40()) { 
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 100, "Test");
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
setNString() - Should throw exception when index is 0.
**/
    public void Var003()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 0, "Test");
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setNString() - Should throw exception when index is -1.
**/
    public void Var004()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", -1, "Test");
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setNString() - Should set to SQL NULL when the value is null.
**/
    public void Var005()
    {
	if (checkJdbc40()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARCHAR_50) VALUES (?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, null);
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
                String check =  rs.getString(1);
		boolean wn = rs.wasNull ();
		rs.close ();

		assertCondition ((check == null) && (wn == true));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setNString() - Should work with a valid parameter index
greater than 1.
**/
    public void Var006()
    {
	if (checkJdbc40()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_KEY, C_VARCHAR_50) VALUES (?, ?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Muchas");
                JDReflectionUtil.callMethod_V(ps, "setNString", 2, "Gracias");
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		String check = rs.getString (1);
		rs.close ();

		assertCondition (check.equals ("Gracias"), "got \""+check+"\" expected \"Gracias\"");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
setNString() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var007()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 2, "Test");
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
setNString() - Verify that a data truncation warning is
posted when data is truncated.
**/
    public void Var008()
    {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setNString", 1,
            "This string is a little bit longer than fifty characters.");

        ps.close();
        failed("Didn't throw SQLException");
      } catch (DataTruncation dt) {
        assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
            && (dt.getRead() == false) && (dt.getDataSize() == 57)
            && (dt.getTransferSize() == 50), "DataTruncation " + dt.getIndex()
            + " " + dt.getParameter() + " " + dt.getRead() + " "
            + dt.getDataSize() + " " + dt.getTransferSize() + " "
            + dt.getRead());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }	

    }



/**
setNString() - Set a SMALLINT parameter.
**/
    public void Var009()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_SMALLINT) VALUES (?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test"); 
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
setNString() - Set a INTEGER parameter.
**/
    public void Var010() {
    if (checkJdbc40()) {
      try {

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

/**
 * setNString() - Set a REAL parameter.
 */
    public void Var011()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_REAL) VALUES (?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
               
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
setNString() - Set a FLOAT parameter.
**/
    public void Var012()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_FLOAT) VALUES (?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
                
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setNString() - Set a DOUBLE parameter.
**/
    public void Var013()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DOUBLE) VALUES (?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setNString() - Set a DECIMAL parameter.
**/
    public void Var014()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DECIMAL_105) VALUES (?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
setNString() - Set a NUMERIC parameter.
**/
    public void Var015()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_NUMERIC_50) VALUES (?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setNString() - Set a CHAR(1) parameter, where the string is
longer.
**/
    public void Var016()
    {
	if (checkJdbc40()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_CHAR_1) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "L");
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

    }


/**
setNString() - Set a CHAR(50) parameter.
**/
    public void Var017()
    {
	if (checkJdbc40()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_CHAR_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "PROFS");
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
    }


/**
setNString() - Set a VARCHAR(50) parameter.
**/
    public void Var018()
    {
	if (checkJdbc40()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARCHAR_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Borland JBuilder");
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

    }

/**
setNString() - Set a CLOB parameter.
**/
    public void Var019()
    {
	if (checkJdbc40()) {
	    if (checkLobSupport ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SET
									 + " (C_CLOB) VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");

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

    }


/**
setNString() - Set a DBCLOB parameter.
**/
    public void Var020()
    {
	if (checkLobSupport ()) {
	    if (checkJdbc40()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SET
									 + " (C_DBCLOB) VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test double byte");
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
    }


/**
setNString() - Set a BINARY parameter, with translation turned on.

SQL400 - Close to the right value returned but difference in trailing
         spaces needs to get dealt with.
**/
    public void Var021()
    {
	if (checkJdbc40()) {
	    try {
		reconnect ("translate binary=true");
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BINARY_20) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Symantec Cafe");
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
    }



/**
setNString() - Set a VARBINARY parameter, with translation turned on.
**/
    public void Var022()
    {
	if (checkJdbc40()) {
	    try {
		reconnect ("translate binary=true");
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARBINARY_20) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Visual J++");
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

    }


/**
setNString() - Set a BINARY parameter, with translation turned off.
**/
    public void Var023()
    {
	if (checkJdbc40()) {
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

		JDReflectionUtil.callMethod_V(ps, "setNString", 1, expected);
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

    }


/**
setNString() - Set a VARBINARY parameter, with translation turned off.
**/
    public void Var024()
    {
	if (checkJdbc40()) {
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
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, expected);
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

    }


/**
setNString() - Set a BLOB parameter.
**/
    public void Var025()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BLOB) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
		ps.execute();
		ps.close();
		failed("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}



    }
/**
setNString() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. D1C
**/
    public void Var026()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DATE) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setNString() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. D1C.
**/
    public void Var027()
    {
	if (checkJdbc40()) {

	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_TIME) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
setNString() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
**/
    public void Var028()
    {
	if (checkJdbc40()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_TIMESTAMP) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
		ps.close ();
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setNString() - Set a DATALINK parameter.
**/
    // @C0C
    public void Var029()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(50))))");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, "http://www.pepsi.com/index.html");
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition ("http://www.pepsi.com/index.html".equalsIgnoreCase(check), "expected \"http://www.pepsi.com/index.html\" got \""+check+"\"");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setNString() - Set a DISTINCT parameter.
**/
    // @C0C
    public void Var030()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DISTINCT) VALUES (?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, "12345678");
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
setNString() - Set a BIGINT parameter.
**/
    public void Var031()
    {
        if (checkJdbc40()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BIGINT) VALUES (?)");
                JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Test");
                ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNString() - Set strings in a table that has columns
with different CCSIDs.
**/
    public void Var032()
    {
	if (checkJdbc40()) {
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
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Bernard");
		JDReflectionUtil.callMethod_V(ps, "setNString", 2, "The");
		JDReflectionUtil.callMethod_V(ps, "setNString", 3, "Cat");
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

    }


/**
setNString() - Set a VARCHAR(50) parameter.
**/
    public void Var033()
    {
	if (checkJdbc40()) {
	    try {
		reconnect ("extended dynamic=true;package=PSTEST"
			   + ";package library=" + JDPSTest.COLLECTION
			   + ";package cache=true");

		String sql = "INSERT INTO " + JDPSTest.PSTEST_SET
		  + " (C_VARCHAR_50) VALUES (?)";


		if (isToolboxDriver())
		    JDSetupPackage.prime (systemObject_,
					  "PSTEST", JDPSTest.COLLECTION, sql);
		else
		    JDSetupPackage.prime (systemObject_,encryptedPassword_, 
					  "PSTEST", JDPSTest.COLLECTION, sql, "", getDriver());


		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (sql);
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "Borland JBuilder");
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
	    } finally {
		/* Turn off package cache */
		try { 
		    reconnect("");
		} catch (Exception e) {
		    e.printStackTrace(); 
		} 
	    }
	}
    }

/** D1A
setNString() - Set a DATE parameter to a value that is a valid date.  This should work.
**/
    public void Var034()
    {
	if (checkJdbc40()) {
	    if (checkNative()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SET
									 + " (C_DATE) VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps, "setNString", 1, "1999-12-31");
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

    }

/** D1A
setNString() - Set a TIME parameter to a value that is valid.  This should work.
**/
    public void Var035()
    {
	if (checkJdbc40()) {
	    if (checkNative()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SET
									 + " (C_TIME) VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps, "setNString", 1, "14:04:55");
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
    }

/** D1A
setNString() - Set a TIMESTAMP parameter to a value that is valid.  This should work.
**/
    public void Var036()
    {
	if (checkJdbc40()) {
	    if (checkNative()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SET
									 + " (C_TIMESTAMP) VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps, "setNString", 1, "1998-04-08-03.15.30.123456");
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
    }
/** D1A
setNString() - Should set to SQL NULL when the value is null.  This test uses a Statement without
truncation turned on because we use different internal paths.
**/
    public void Var037()
    {
	if (checkJdbc40()) {
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
		    JDReflectionUtil.callMethod_V(ps, "setNString", 1, null);
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
    }
/**
setNString() - make sure pad is correct for graphic fields
**/
    public void Var038()
    {
	if (checkJdbc40()) {

	    String tableName = JDPSTest.COLLECTION + ".GRAPHIC_PAD";
	    try 
	    {                              

		try { statement_.executeUpdate("drop table " + tableName); } catch (Exception e) {}
		statement_.executeUpdate ("CREATE TABLE "
					  + tableName
					  + " (COL1 CHAR(4) CCSID 37, "          // padded with single byte space
					  + "  COL2 GRAPHIC(4) CCSID 13488, "    // padded with single byte space
					  + "  COL3 GRAPHIC(4) CCSID 835)");     // padded with double byte space

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + tableName +
								     "(COL1, COL2, COL3) VALUES (?, ?, ?)");
		JDReflectionUtil.callMethod_V(ps, "setNString", 1, "AA");
		JDReflectionUtil.callMethod_V(ps, "setNString", 2, "BB");
		JDReflectionUtil.callMethod_V(ps, "setNString", 3, "\u6f22\u5b57");
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
			ch = col3.toCharArray();      
			digit1 = 0x00FFFF & ch[0];
			digit2 = 0x00FFFF & ch[1];
			digit3 = 0x00FFFF & ch[2];
			digit4 = 0x00FFFF & ch[3];

			if ((digit1 == 0x6F22) &&   // get original data back?
			    (digit2 == 0x5B57) &&   // get original data back?
			    (digit3 == 0x3000) &&   // get double byte space (0x3000)?
			    (digit4 == 0x3000))     // get double byte space (0x3000)?
			{
			    succeeded();
			}
			else
			    failed("Col3 not padded correctly.  Expected 'AA\u3000\u3000', received " + digit1 + digit2 + digit3 + digit4);
		    }
		    else
			failed("Col2 not padded correctly.  Expected 'AA\u0020\u0020', received " + digit1 + digit2 + digit3 + digit4);
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
    }

/**
setNString() - make sure pad is correct for graphic fields
**/
    public void Var039()
    {
	if (checkJdbc40()) {

	    {                                                                                   //@G1A
		String tableName = JDPSTest.COLLECTION + ".GRAPHIC_PAD";
		try 
		{                              

		    try { statement_.executeUpdate("drop table " + tableName); } catch (Exception e) {}
		    statement_.executeUpdate ("CREATE TABLE "
					      + tableName
					      + " (COL1 CHAR(4) CCSID 1208, "        // padded with single byte space
					      + "  COL2 GRAPHIC(4) CCSID 1200, "    // padded with single byte space
					      + "  COL3 GRAPHIC(4) CCSID 835)");     // padded with double byte space

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + tableName +
									 "(COL1, COL2, COL3) VALUES (?, ?, ?)");
		    JDReflectionUtil.callMethod_V(ps, "setNString", 1, "AA");
		    JDReflectionUtil.callMethod_V(ps, "setNString", 2, "BB");
		    JDReflectionUtil.callMethod_V(ps, "setNString", 3, "\u6f22\u5b57");
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
			    ch = col3.toCharArray();      
			    digit1 = 0x00FFFF & ch[0];
			    digit2 = 0x00FFFF & ch[1];
			    digit3 = 0x00FFFF & ch[2];
			    digit4 = 0x00FFFF & ch[3];

			    if ((digit1 == 0x6F22) &&   // get original data back?
				(digit2 == 0x5B57) &&   // get original data back?
				(digit3 == 0x3000) &&   // get double byte space (0x3000)?
				(digit4 == 0x3000))     // get double byte space (0x3000)?
			    {
				succeeded();
			    }
			    else
				failed("Col3 not padded correctly.  Expected 'AA\u3000\u3000', received " + digit1 + digit2 + digit3 + digit4);
			}
			else
			    failed("Col2 not padded correctly.  Expected 'AA\u0020\u0020', received " + digit1 + digit2 + digit3 + digit4);
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
	}
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
setNString() - make sure UTF-8 parameter can be passed 
**/
    public void Var040() {
	if (checkJdbc40()) {
	     {
		String tableName = JDPSTest.COLLECTION + ".UTF8";
		try  {                              
		    try { statement_.executeUpdate("drop table " + tableName); } catch (Exception e) {}
		    statement_.executeUpdate ("CREATE TABLE " + tableName
					      + " (COL1 VARCHAR(30000) CCSID 1208)");
		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + tableName +
									 "(COL1) VALUES (?)");
		    String insertString = "\u05D0";
		    JDReflectionUtil.callMethod_V(ps, "setNString", 1, insertString );
		    ps.executeUpdate ();
		    ps.close ();

		    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tableName);
		    rs.next ();
		    String col1 = rs.getString (1);
		    rs.close ();

		    assertCondition(col1.equals(insertString), "retrieved value("+formatUnicode(col1)+") != expected("+formatUnicode(insertString)+") -- Added 10/21/03 by native driver");

		} catch (Exception e) {
		    failed (e, "Unexpected Exception -- Added 10/21/03 by native driver");
		} finally {
		    try { statement_.executeUpdate ("DROP TABLE " + tableName); } catch (SQLException e) { }
		}
	    } 
	}
    }


/**
setNString() - make sure EURO symbol parameter can be passed and retrieved
            - This does not work for native driver (pre v5r3). 
**/
    public void Var041() {
	if (checkJdbc40()) {
	    {
		String tableName = JDPSTest.COLLECTION + ".EURO";
		try  {                              
		    try { statement_.executeUpdate("drop table " + tableName); } catch (Exception e) {}
		    statement_.executeUpdate ("CREATE TABLE " + tableName
					      + " (COL1 VARCHAR(30000) CCSID 1140)");
		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + tableName +
									 "(COL1) VALUES (?)");
		    String insertString = "\u20AC";
		    JDReflectionUtil.callMethod_V(ps, "setNString", 1, insertString );
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
	    }
	}
    }

/**
setNString() - make sure MIXED data can be passed to a CLOB
Found by customer in V5R3 in CPS discussion 6FZJF4
Also needs to work for V5R2
**/
    public void Var042() {
	if (checkJdbc40()) {
	     {
		String tableName = JDPSTest.COLLECTION + ".MIXED5035";
		try  {

		//
		// In V5R2, all translations for native JDBC driver
		// go through job CCSID.  Must change CCSId for 5035 for
		// this to work.
		//
		    try { statement_.executeUpdate("drop table " + tableName); } catch (Exception e) {}
		    statement_.executeUpdate ("CREATE TABLE " + tableName
					      + " (COL1 CLOB(30000) CCSID 5035)");
		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + tableName +
									 "(COL1) VALUES (?)");
		    String insertString = "\u3042\u3044\u3046\u3048\u304a";
		    JDReflectionUtil.callMethod_V(ps, "setNString", 1, insertString );
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
	    }
	}


    }




    public void Var043() {
	notApplicable();
    } 
    

/**
setClob() - Set an XML  parameter.
**/
	public void setXML(String tablename, String data, String expected ) {
	    setXML(tablename, data, expected, null); 
	}

	public void setXML(String tablename, String data, String expected, String added ) {
	    if (added == null) added = " -- added by native driver 08/21/2009";
	    if (checkJdbc40()) {
		if (checkXmlSupport ()) {
		    try {
			statement_.executeUpdate ("DELETE FROM " + tablename);

			PreparedStatement ps = connection_.prepareStatement (
									     "INSERT INTO " + tablename
									     + "  VALUES (?)");


			JDReflectionUtil.callMethod_V(ps, "setNString", 1, data);

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

	}

/**
setClob() - Set an XML  parameter using invalid data.
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

			JDReflectionUtil.callMethod_V(ps, "setNString", 1, data);

			ps.executeUpdate ();
			ps.close ();

			ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename);
			rs.next ();
			String check = rs.getString (1);
			rs.close ();
			failed("Didn't throw exception but got "+
			       JDTestUtilities.getMixedString(check)+
			       added); 

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
	public void Var044() { setXML(JDPSTest.PSTEST_SETXML,  "<Test>VAR044\u00a2</Test>",  "<Test>VAR044\u00a2</Test>"); }
	public void Var045() { setXML(JDPSTest.PSTEST_SETXML,  "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR045\u00a2</Test>",  "<Test>VAR045\u00a2</Test>"); }
	public void Var046() { setXML(JDPSTest.PSTEST_SETXML,  "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR046\u0130\u3041\ud800\udf30</Test>",  "<Test>VAR046\u0130\u3041\ud800\udf30</Test>"); }

	public void Var047() { setXML(JDPSTest.PSTEST_SETXML13488, "<Test>VAR047</Test>",  "<Test>VAR047</Test>"); }
	public void Var048() { setXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR048\u00a2</Test>",  "<Test>VAR048\u00a2</Test>"); }
	public void Var049() { setXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR049\u0130\u3041</Test>",  "<Test>VAR049\u0130\u3041</Test>"); }

	public void Var050() { setXML(JDPSTest.PSTEST_SETXML1200, "<Test>VAR050</Test>",  "<Test>VAR050</Test>"); }
	public void Var051() { setXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR051\u00a2</Test>",  "<Test>VAR051\u00a2</Test>"); }
	public void Var052() { setXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR052\u0130\u3041\ud800\udf30</Test>",  "<Test>VAR052\u0130\u3041\ud800\udf30</Test>"); }

	public void Var053() { setXML(JDPSTest.PSTEST_SETXML37, "<Test>VAR053\u00a2</Test>",  "<Test>VAR053\u00a2</Test>"); }
	public void Var054() { setXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR054\u00a2</Test>",  "<Test>VAR054\u00a2</Test>"); }
	public void Var055() { setXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR055\u00a2</Test>",  "<Test>VAR055\u00a2</Test>"); }

	public void Var056() { setXML(JDPSTest.PSTEST_SETXML937, "<Test>VAR056\u672b</Test>",  "<Test>VAR056\u672b</Test>"); }
	public void Var057() { setXML(JDPSTest.PSTEST_SETXML937, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR057\u672b</Test>",  "<Test>VAR057\u672b</Test>"); }
	public void Var058() { setXML(JDPSTest.PSTEST_SETXML937, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR058\u672b</Test>",  "<Test>VAR058\u672b</Test>"); }

	public void Var059() { setXML(JDPSTest.PSTEST_SETXML290, "<Test>VAR059</Test>",  "<Test>VAR059</Test>"); }
	public void Var060() { setXML(JDPSTest.PSTEST_SETXML290, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR060\uff7a</Test>",  "<Test>VAR060\uff7a</Test>"); }
	public void Var061() { setXML(JDPSTest.PSTEST_SETXML290, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR061\uff98</Test>",  "<Test>VAR061\uff98</Test>"); }



	   /* Encoding is stripped for character data since we know is is UTF-16 */ 
	public void Var062() { setXML(JDPSTest.PSTEST_SETXML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>VAR062</Test>",  "<Test>VAR062</Test>"); }
	public void Var063() { setInvalidXML(JDPSTest.PSTEST_SETXML, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR063</Tes>",  "XML parsing failed"); }

	public void Var064() { setXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>VAR064</Test>",  "<Test>VAR064</Test>" ); }
	public void Var065() { setInvalidXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tes>VAR065</Test>",  "XML parsing failed"); }

	public void Var066() { setXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>VAR066</Test>",  "<Test>VAR066</Test>"); }
	public void Var067() { setInvalidXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tes>VAR067</Test>",  "XML parsing failed"); }

	public void Var068() { setXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>VAR068\u00a2</Test>",  "<Test>VAR068\u00a2</Test>"); }
	public void Var069() { setInvalidXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tet>VAR069</Test>",  "XML parsing failed"); }



	/* Run with package cache.  As of 10/3/2011, if the cache is used then the locator in the cached */
	/* statement is -1.  This then causes the write of the XML data to fail */

	public void Var070() {
	    String added = "Added 10/03/2011 -- Run with package cache.  If the cache is used then the locator in the cached statement is -1.  This then causes the write of the XML data to fail "; 
	    try { 
		reconnect ("extended dynamic=true;package=PSTEST"
			   + ";package library=" + JDPSTest.COLLECTION
			   + ";package cache=true");
		setXML(JDPSTest.PSTEST_SETXML,  "<Test>VAR044\u00a2</Test>",  "<Test>VAR044\u00a2</Test>", added );
		reconnect ("");
	    }  catch (Exception e) {
		failed (e, "Unexpected Exception -- "+added);
	    }
	}

	
	
	
  /**
   * setInt() - Set an parameter for a column of a specified type.
   **/
  public void testSetNString(String columnName, String[][] testValues) {
    try {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");

      for (int i = 0; i < testValues.length; i++) {
        String value = testValues[i][0];
        String expectedValue = testValues[i][1];

        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        JDReflectionUtil.callMethod_V(ps, "setNString", 1, value);
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


  public void Var071() {
    if (checkBooleanSupport()) {
      String[][] testValues = { { "1", "1" }, { "0", "0" }, { "1000", "1" },
          { "-100", "1" }, { null, "null" },

      };
      testSetNString("C_BOOLEAN", testValues);
    }
  }

  public void Var072() {
    if (checkBooleanSupport()) {
      String[][] testValues = { { "true", "1" }, { "false", "0" }, { "T", "1" },
          { "F", "0" }, { "yes", "1" }, { "no", "0" }, { null, "null" }, };
      testSetNString("C_BOOLEAN", testValues);
    }
  }




}
