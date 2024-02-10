///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetFloat.java
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
// File Name:    JDPSSetFloat.java
//
// Classes:      JDPSSetFloat
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
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDPSetFloat.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setFloat()
</ul>
**/
public class JDPSSetFloat
extends JDTestcase
{



    // Constants.
    private static final String PACKAGE             = "JDPSSF";



    // Private data.
    private Connection          connection_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetFloat (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetFloat",
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
        String url = baseURL_
            
            
            + ";data truncation=true";
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        statement_ = connection_.createStatement ();
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
setFloat() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_FLOAT) VALUES (?)");
            ps.close ();
            ps.setFloat (1, 533.45f);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setFloat() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setFloat (100, 334.45f);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setFloat() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setFloat (0, -385.45f);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setFloat() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setFloat (-1, 943.5f);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setFloat() - Should work with a valid parameter index
greater than 1.
**/
    public void Var005()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_KEY, C_FLOAT) VALUES (?, ?)");
            ps.setString (1, "Test");
            ps.setFloat (2, -43423.456f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -43423.456f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var006()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            ps.setFloat (2, -23322.5f);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setFloat() - Set a SMALLINT parameter.
**/
    public void Var007()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.setFloat (1, 4269f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 4269f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set a SMALLINT parameter, when there is a decimal part.
**/
    public void Var008()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.setFloat (1, 212.243f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 212f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set a SMALLINT parameter, when the value is too big.  This will
cause a data truncation exception.
**/
    public void Var009()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.setFloat (1, -70000.0f);
	    ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		assertSqlException(e, -99999, "07006", "Data type mismatch", " Data type mismatch consistency");
        }
    }



/**
setFloat() - Set an INTEGER parameter.
**/
    public void Var010()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            ps.setFloat (1, 30679f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 30679f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an INTEGER parameter, when there is a decimal part.
**/
    public void Var011()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            ps.setFloat (1, 306.8976f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 306f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an INTEGER parameter, when the value is too big.  This will
cause a data truncation exception.
**/
    public void Var012() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      ps.setFloat(1, 21474836448.0f);
      ps.execute(); 

      failed("Didn't throw SQLException");
    } catch (Exception e) {
        assertSqlException(e, -99999, "07006", "Data type mismatch",
            " Data type mismatch consistency");
    }
  }


/**
setFloat() - Set an REAL parameter.
**/
    public void Var013()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_REAL) VALUES (?)");
            ps.setFloat (1, -1792.249f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -1792.249f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an FLOAT parameter.
**/
    public void Var014()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_FLOAT) VALUES (?)");
            ps.setFloat (1, 0.1243f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check ==  0.1243f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an DOUBLE parameter.
**/
    public void Var015()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DOUBLE) VALUES (?)");
            ps.setFloat (1, 122.290f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 122.290f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an DECIMAL parameter.
**/
    public void Var016()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setFloat (1, 4.0012f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 4.0012f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an DECIMAL parameter, when the value is too big.
**/
    public void Var017()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_50) VALUES (?)");
            ps.setFloat (1, 103212.2134f);
	    ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
		}
        }
    }



/**
setFloat() - Set a DECIMAL parameter, where the float's fraction truncates.
This does not cause a DataTruncation object to be created.
**/
    public void Var018()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_50) VALUES (?)");
            ps.setFloat (1, -99999.2134f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -99999);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an DECIMAL parameter, when the value is very small.
**/
    public void Var019()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setFloat (1, 0.00007f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 0.00007f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an NUMERIC parameter.
**/
    public void Var020()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_105) VALUES (?)");
            ps.setFloat (1, -277.7919f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -277.7919f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an NUMERIC parameter, when the value is too big
**/
    public void Var021()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_50) VALUES (?)");
            ps.setFloat (1, -24345542334.4f);
	    ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt = (DataTruncation)e;
		    assertCondition ((dt.getIndex() == 1)
				     && (dt.getParameter() == true)
				     && (dt.getRead() == false)
				     && (dt.getTransferSize() == 5));
		}
        }
    }



/**
setFloat() - Set a NUMERIC parameter, where the float's fraction truncates.
This does not cause a DataTruncation object to be created.
**/
    public void Var022()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_50) VALUES (?)");
            ps.setFloat (1, 99999.2134f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 99999);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an NUMERIC parameter, when the value is very small.
**/
    public void Var023()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_105) VALUES (?)");
            ps.setFloat (1, -0.00003f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -0.00003f, "got "+check+" sb -0.0003f");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an CHAR(50) parameter.
**/
    public void Var024()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_50) VALUES (?)");
            ps.setFloat (1, -1842.799f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -1842.799f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set an CHAR(1) parameter.
**/
    public void Var025()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_1) VALUES (?)");

            // It seems that any float translates into bigger than
            // 1 character, so for a CHAR(1), we will always get a 
            // DataTruncation.
            ps.setFloat (1, 9f);
	    ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
        }
    }



/**
setFloat() - Set an CHAR(1) parameter, when the value is too big.
**/
    public void Var026()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_1) VALUES (?)");
            ps.setFloat (1, -2233.2f);
	    ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
        }
    }



/**
setFloat() - Set an VARCHAR parameter.
**/
    public void Var027()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            ps.setFloat (1, 0.87667f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 0.87667f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setFloat() - Set a CLOB parameter.
**/
    public void Var028()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_CLOB) VALUES (?)");
                ps.setFloat (1, 542.3f);
		ps.execute(); 
                ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setFloat() - Set a DBCLOB parameter.
**/
    public void Var029()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DBCLOB) VALUES (?)");
                ps.setFloat (1, 5.432f);
		ps.execute(); 
                ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFloat() - Set a BINARY parameter.
**/
    public void Var030()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BINARY_20) VALUES (?)");
            ps.setFloat (1, 545.4f);
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }




/**
setFloat() - Set a VARBINARY parameter.
**/
    public void Var031()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARBINARY_20) VALUES (?)");
            ps.setFloat (1, 1.944f);
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setFloat() - Set a BLOB parameter.
**/
    public void Var032()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BLOB) VALUES (?)");
            ps.setFloat (1, -54.4f);
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setFloat() - Set a DATE parameter.
**/
    public void Var033()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DATE) VALUES (?)");
            ps.setFloat (1, -756.5f);
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setFloat() - Set a TIME parameter.
**/
    public void Var034()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIME) VALUES (?)");
            ps.setFloat (1, 0.1f);
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setFloat() - Set a TIMESTAMP parameter.
**/
    public void Var035()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIMESTAMP) VALUES (?)");
            ps.setFloat (1, 454543f);
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
setFloat() - Set a DATALINK parameter.
**/
    public void Var036()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DATALINK) VALUES (?)");
                ps.setFloat (1, -75.5f);
	    ps.execute(); 
                ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFloat() - Set a DISTINCT parameter.
**/
    public void Var037()
    {
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DISTINCT) VALUES (?)");
                ps.setFloat (1, 9.81f);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                float check = rs.getFloat (1);
                rs.close ();

                assertCondition (check == 9f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set a BIGINT parameter.
**/
    public void Var038()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setFloat (1, -20679f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -20679f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
setFloat() - Set a BIGINT parameter, when there is a decimal part.
**/
    public void Var039()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setFloat (1, -306.8976f);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -306f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
setFloat() - Set an BIGINT parameter, when the value is too big.  This will
cause a data truncation exception.
**/
    public void Var040()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setFloat (1, -92233720368547758099.0f);
	    ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertSqlException(e, -99999, "07006", "Data type mismatch",
                " Data type mismatch consistency");
        }
        }
    }



/**
setFloat() - Set a parameter in a statement that comes from the
package cache.
**/
    public void Var041()
    {
        try {
            String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_FLOAT) VALUES (?)";

            if (isToolboxDriver())
               JDSetupPackage.prime (systemObject_, PACKAGE, 
                   JDPSTest.COLLECTION, insert);
            else
               JDSetupPackage.prime (systemObject_,encryptedPassword_,  PACKAGE, 
                   JDPSTest.COLLECTION, insert, "", getDriver());

            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            Connection c2 = testDriver_.getConnection (baseURL_
                + ";extended dynamic=true;package=" + PACKAGE
                + ";package library=" + JDPSTest.COLLECTION
                + ";package cache=true", userId_, encryptedPassword_);
            PreparedStatement ps = c2.prepareStatement (insert);
            ps.setFloat (1, 98213.5f);
            ps.executeUpdate ();
            ps.close ();
            c2.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();            

            assertCondition (check == 98213.5f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setInt() - Set an DECFLOAT16 parameter.
    **/
    public void Var042()
    {
      if (checkDecFloatSupport()) {
        try {
          statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);
          
          PreparedStatement ps = connection_.prepareStatement (
              "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
              + "  VALUES (?)");
          ps.setFloat (1, -10012);
          ps.executeUpdate ();
          ps.close ();
          
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
          rs.next ();
          float check = rs.getFloat (1);
          rs.close ();
          
          assertCondition (check == -10012);
        }
        catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }

        /**
        setFloat() - Set an DECFLOAT34 parameter.
        **/
    public void Var043()
    {
      if (checkDecFloatSupport()) { 
        try {
          statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);
          
          PreparedStatement ps = connection_.prepareStatement (
              "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
              + "  VALUES (?)");
          ps.setFloat (1, -10012);
          ps.executeUpdate ();
          ps.close ();
          
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
          rs.next ();
          float check = rs.getFloat (1);
          rs.close ();
          
          assertCondition (check == -10012);
        }
        catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
      
      
    }



/**
setFloat() - Set a SQLXML parameter.
**/
    public void Var044()
    {
        if (checkXmlSupport ()) {
	    try {
		PreparedStatement ps =
		  connection_.prepareStatement (
						"INSERT INTO " +
						JDPSTest.PSTEST_SETXML
						+ " VALUES (?)");
		try { 
		    ps.setFloat (1, -75.5f);
	              ps.execute();
		    ps.close ();
		    failed ("Didn't throw SQLException");
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    } catch (Exception e) {
		failed(e, "Unexpected exception"); 
	    } 
        }
    }

    
    
    /**
     * setFloat() - Set an parameter for a column of a specified type.
     **/
    public void testSetFloat(String columnName, float value, String outputValue) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
        ps.setFloat(1, value);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery(
            "SELECT " + columnName + " FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        String check = "" + rs.getString(1);
        rs.close();

        assertCondition(outputValue.equals(check),
            " got " + check + " sb " + outputValue+" from "+value);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

    public void Var045() {
      if (checkBooleanSupport()) {
        testSetFloat("C_BOOLEAN", 1, "1");
      }
    }

    public void Var046() {
      if (checkBooleanSupport()) {
        testSetFloat("C_BOOLEAN", 0, "0");
      }
    }

    public void Var047() {
      if (checkBooleanSupport()) {
        testSetFloat("C_BOOLEAN", 101, "1");
      }
    }

    public void Var048() {
      if (checkBooleanSupport()) {
        testSetFloat("C_BOOLEAN", -1, "1");
      }
    }

    public void Var049() {
      if (checkBooleanSupport()) {
        testSetFloat("C_BOOLEAN", Float.MAX_VALUE, "1");
      }
    }

    public void Var050() {
      if (checkBooleanSupport()) {
        testSetFloat("C_BOOLEAN", Float.MIN_VALUE, "1");
      }
    }

    public void Var051() {
      if (checkBooleanSupport()) {
        testSetFloat("C_BOOLEAN", Float.NEGATIVE_INFINITY, "1");
      }
    }

    public void Var052() {
      if (checkBooleanSupport()) {
        testSetFloat("C_BOOLEAN", Float.POSITIVE_INFINITY, "1");
      }
    }


}
