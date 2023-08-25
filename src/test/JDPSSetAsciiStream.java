///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetAsciiStream.java
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
// File Name:    JDPSSetAsciiStream.java
//
// Classes:      JDPSSetAsciiStream
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

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.sql.Clob;                 // @E2


/**
Testcase JDPSSetAsciiStream.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setAsciiStream()
</ul>
**/
public class JDPSSetAsciiStream
extends JDTestcase {



    // Constants.
    private static final String PACKAGE             = "JDPSSAS";



    // Private data.
    private Connection          connection_;
    private Connection          connectionNoDT_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetAsciiStream (AS400 systemObject,
                               Hashtable namesAndVars,
                               int runMode,
                               FileOutputStream fileOutputStream,
                               
                               String password)
    {
        super (systemObject, "JDPSSetAsciiStream",
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
                     
                     
                     + ";data truncation=true;lob threshold=1000;errors=full";
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        statement_ = connection_.createStatement ();

        url = url + ";data truncation=false";
        connectionNoDT_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
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
        connectionNoDT_.close();
    }

    public void setInvalid(String column, String inputValue)  {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " ("+column+") VALUES (?)");
        InputStream is = new ByteArrayInputStream(inputValue.getBytes("ISO8859_1"));
        ps.setAsciiStream(1, is, is.available());
        ps.close();
        failed("Didn't throw SQLException for column("+column+") inputValue("+inputValue+")");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }


/**
setAsciiStream() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.close ();
            InputStream is = new ByteArrayInputStream ("Canada".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?, ?)");
            InputStream is = new ByteArrayInputStream ("United States".getBytes ("ISO8859_1"));
            ps.setAsciiStream (100, is, is.available ());
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?, ?)");
            InputStream is = new ByteArrayInputStream ("Mexico".getBytes ("ISO8859_1"));
            ps.setAsciiStream (0, is, is.available ());
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?, ?)");
            InputStream is = new ByteArrayInputStream ("Virgin Islands".getBytes ("ISO8859_1"));
            ps.setAsciiStream (0, is, is.available ());
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Should set to SQL NULL when the value is null.
**/
    public void Var005()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setAsciiStream (1, null, 0);
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
setAsciiStream() - Should work with a valid parameter index
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
            InputStream is = new ByteArrayInputStream ("El Salvador".getBytes ("ISO8859_1"));
            ps.setAsciiStream (2, is, is.available ());
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);

            rs.close ();

            assertCondition (check.equals ("El Salvador"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAsciiStream() - Should throw exception when the length is not valid.
**/
    public void Var007()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Banana Republic".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, -1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var008()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            InputStream is = new ByteArrayInputStream ("Honduras".getBytes ("ISO8859_1"));
            ps.setAsciiStream (2,is, is.available ());
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Verify that a data truncation warning is
posted when data is truncated.
**/
    public void Var009()
    {
        int length = 0;
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            String s = "Panama is yet another country that is in Central America";
            InputStream is = new ByteArrayInputStream (s.getBytes ("ISO8859_1"));
            length = s.length ();
            ps.setAsciiStream (1, is, is.available ());
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (DataTruncation dt) {
            assertCondition ((dt.getIndex() == 1)
                    && (dt.getParameter() == true)
                    && (dt.getRead() == false)
                    && (dt.getDataSize() == length)
                    && (dt.getTransferSize() == 50));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }





/**
setAsciiStream() - Set a SMALLINT parameter.
**/
    public void Var010()
    {
      setInvalid("C_SMALLINT","Costa Rica"); 
    }



/**
setAsciiStream() - Set a INTEGER parameter.
**/
    public void Var011()

    {
      setInvalid("C_INTEGER","Cuba"); 
    }



/**
setAsciiStream() - Set a REAL parameter.
**/
    public void Var012()
    {
      setInvalid("C_REAL", "Jamaica"); 
    }



/**
setAsciiStream() - Set a FLOAT parameter.
**/
    public void Var013()
    {
      setInvalid("C_FLOAT", "Haiti"); 
          }

    


/**
setAsciiStream() - Set a DOUBLE parameter.
**/
    public void Var014()
    {
      setInvalid("C_DOUBLE","Trinidad"); 
      
    }



/**
setAsciiStream() - Set a DECIMAL parameter.
**/
    public void Var015()
    {
      setInvalid("C_DECIMAL_105","Ecuador"); 
      
    }



/**
setAsciiStream() - Set a NUMERIC parameter.
**/
    public void Var016()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Brazil".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Set a CHAR(1) parameter.
**/
    public void Var017()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_1) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("U".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("U"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
setAsciiStream() - Set a CHAR(50) parameter.
**/
    public void Var018()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Uruguay".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition ((check.startsWith ("Uruguay")) && (check.length () == 50));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAsciiStream() - Set a VARCHAR(50) parameter, with the length less than
the full stream.
**/
    public void Var019()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Tobago".getBytes ("ISO8859_1"));
            InputStream is2 = new ByteArrayInputStream ("Tob".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is2.available ());
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @E3
		getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @E3
		succeeded();						// @E3
	    else							// @E3
		failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @E3
		getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @E3
		failed(e, "Unexpected Exception.");			// @E3
	    else							// @E3

		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Set a VARCHAR(50) parameter, with the length greater than
the full stream.
**/
    public void Var020()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Long Island".getBytes ("ISO8859_1"));
            InputStream is2 = new ByteArrayInputStream ("Long Island and more".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is2.available () + 5);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Set a VARCHAR(50) parameter, with the length set to 1 character.
**/
    public void Var021()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Yucatan Penisula".getBytes ("ISO8859_1"));
            InputStream is2 = new ByteArrayInputStream ("Y".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is2.available ());
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @E3
		getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @E3
		succeeded();						// @E3
	    else							// @E3
		failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @E3
		getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @E3
		failed(e, "Unexpected Exception.");			// @E3
	    else							// @E3

		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Set a VARCHAR(50) parameter, with the length set to 0.
**/
    public void Var022()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Baja California".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, 0);
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @E3
		getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @E3
		succeeded();						// @E3
	    else							// @E3
		failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
    	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @E3
		getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @E3
		failed(e, "Unexpected Exception.");			// @E3
	    else							// @E3

		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Set a VARCHAR(50) parameter to the empty string.
**/
    public void Var023()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals (""));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAsciiStream() - Set a VARCHAR(50) parameter to a bad input stream.
**/
    public void Var024()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");

            class BadInputStream extends InputStream {
                public BadInputStream () {
                    super ();
                }
                public int available () throws IOException {
                    throw new IOException ();
                };
                public int read () throws IOException {
                    throw new IOException ();
                };
                public int read (byte[] buffer) throws IOException {
                    throw new IOException ();
                };
            }

            InputStream r = new BadInputStream ();
            ps.setAsciiStream (1, r, 2);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setAsciiStream() - Set a CLOB parameter.
**/
    public void Var025()
    {
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_CLOB) VALUES (?)");
                InputStream is = new ByteArrayInputStream ("Argentina".getBytes ("ISO8859_1"));
                ps.setAsciiStream (1, is, 9);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("Argentina"), "check = "+check+" And SB Argentina");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
setAsciiStream() - Set a DBCLOB parameter.
**/
    public void Var026()
    {
        if (checkLobSupport ()) {
            succeeded ();
            /* Need to investigate this variation ...
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DBCLOB) VALUES (?)");
                InputStream is = new ByteArrayInputStream ("Peru".getBytes ("ISO8859_1"));
                ps.setAsciiStream (1, is, is.available ());
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DBCLOB FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("Peru"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
            */
        }
    }



/**
setAsciiStream() - Set a BINARY parameter.
**/
    public void Var027()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BINARY_20) VALUES (?)");

            String expected = null;

            if (isToolboxDriver())
               expected = "436F6C6F6D616960";
            else
               expected = "Colombia";

            InputStream is = new ByteArrayInputStream (expected.getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            // Spaces get translated different, so we kluge this
            // comparison.
            assertCondition (check.startsWith (expected));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
setAsciiStream() - Set a VARBINARY parameter.
**/
    public void Var028()
    {
        try {
            String expected = null;

            if (isToolboxDriver())
               expected = "50756572746F";
            else
               expected = "Puerto Rico";

            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            InputStream is = new ByteArrayInputStream (expected.getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
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
setAsciiStream() - Set a BLOB parameter.
**/
    public void Var029()
    {
        try
        {
            PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");
            InputStream is = new ByteArrayInputStream("FFFF1234".getBytes ("ISO8859_1"));
            ps.setAsciiStream(1, is, is.available());
            ps.close();
	    if(getDriver() == JDTestDriver.DRIVER_NATIVE){		// @E2
		failed("Didn't throw SQLException");			// @E2
	    }								// @E2
	    else							// @E2
		succeeded();
        }
        catch(Exception e)
        {
	    if(getDriver() == JDTestDriver.DRIVER_NATIVE){			// @E2
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");	// @E2
	    }									// @E2
	    else								// @E2
		failed(e, "Unexpected Exception - Toolbox added support for HEX String representation of binary types (01/24/2003).");
        }
    }




/**
setAsciiStream() - Set a DATE parameter.
**/
    public void Var030()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DATE) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Virgin Islands".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Set a TIME parameter.
**/
    public void Var031()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIME) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Bermuda".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setAsciiStream() - Set a TIMESTAMP parameter.
**/
    public void Var032()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIMESTAMP) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Bahamas".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
setAsciiStream() - Set a DATALINK parameter.
**/
    public void Var033()
    {
        if (checkDatalinkSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(40))))");
                String url = "http://java.sun.com/jdbc.html";
                InputStream is = new ByteArrayInputStream (url.getBytes ("ISO8859_1"));
                ps.setAsciiStream (1, is, url.length());
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equalsIgnoreCase(url));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setAsciiStream() - Set a DISTINCT parameter.
**/
    public void Var034()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DISTINCT) VALUES (?)");
                InputStream is = new ByteArrayInputStream ("Strait of Magellan".getBytes ("ISO8859_1"));
                ps.setAsciiStream (1, is, is.available ());
                ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setAsciiStream() - Set a BIGINT parameter.
**/
    public void Var035()
    {
	if (checkBigintSupport()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BIGINT) VALUES (?)");
		InputStream is = new ByteArrayInputStream ("Cuba".getBytes ("ISO8859_1"));
		ps.setAsciiStream (1, is, is.available ());
		ps.close ();
		if( getDriver() == JDTestDriver.DRIVER_NATIVE &&
		    getRelease() <= JDTestDriver.RELEASE_V5R2M0 )
		    assertCondition(true);
		else
		    failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }




/**
setAsciiStream() - Set a VARCHAR(50) parameter with package caching.
**/
    public void Var036()
    {
        try {
            String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
                            + " (C_VARCHAR_50) VALUES (?)";

            if (isToolboxDriver())
                JDSetupPackage.prime (systemObject_, PACKAGE,
                                      JDPSTest.COLLECTION, insert);
            else
                JDSetupPackage.prime (systemObject_, encryptedPassword_, PACKAGE,
                                      JDPSTest.COLLECTION, insert, "" , getDriver());

            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            Connection c2 = testDriver_.getConnection (baseURL_
                                                       + ";extended dynamic=true;package=" + PACKAGE
                                                       + ";package library=" + JDPSTest.COLLECTION
                                                       + ";package cache=true", userId_, encryptedPassword_);
            // Updated 10/11/2006 to use c2 instead of connection_ as documented 
            PreparedStatement ps = c2.prepareStatement (insert);
            InputStream is = new ByteArrayInputStream ("Antarctica".getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, is, is.available ());
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Antarctica"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/** D1A
SQL400 - testcase added.
setAsciiStream() - Verify that no data truncation warning is
posted when data is truncated but the data truncation flag is
turned off.
**/
    public void Var037()
    {
        if (checkNative()) {
            // int length = 0;
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connectionNoDT_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_VARCHAR_50) VALUES (?)");
                String s = "Panama is yet another country that is in Central America";
                InputStream is = new ByteArrayInputStream (s.getBytes ("ISO8859_1"));
                // length = s.length ();
                ps.setAsciiStream (1, is, is.available ());
                ps.close ();
                assertCondition (true);
            }
            catch (DataTruncation dt) {
                failed (dt, "Unexpected Data Truncation Exception");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/********* IT WOULD BE GOOD TO RUN THE FOLLOWING 2 VARIATIONS WITH jdbc.db2.use.job.conversion = true
*********/

    public void Var038()	// Added this var - @E2
    {

	if(checkNative()) {
	    String tableName = JDPSTest.COLLECTION+".T1"; 
	    // only run for JDK 1.2 or newer


		    try{

			try{
			    statement_.executeUpdate("DROP TABLE "+tableName);
			}catch(Exception e){
			}

			int maxSize = 50000;
//		int maxSize = 1073741823; // 1G - 1 = (2^30) - 1
//		int maxSize = 2147483647; // this is max Value of int

			String url = baseURL_
			  
			  
			  + ";data truncation=true;lob threshold="+(maxSize+1)+";errors=full";
			connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
			statement_ = connection_.createStatement ();

			statement_.executeUpdate("CREATE TABLE "+tableName+" ( C1 CLOB("+maxSize+") )");

			PreparedStatement ps = connectionNoDT_.prepareStatement("INSERT INTO "+tableName+" VALUES (?)");

/* METHOD 1
		String s = "A";
		for(long l=1; l<maxSize ; l*=2)
		    s += s;
		if(s.length() > maxSize)
		    s = s.substring(0, (int)maxSize);
*/
/* METHOD 2
		byte[] arr = new byte[maxSize];
		int b=0;
		for(int l=1; l<maxSize; l++, b = (b+1)%26)
		    arr[l] = (byte)b;
		String s = new String(arr);
*/
// METHOD 3
			String shortValue = "abcdefghijklmnopqrstuvwxy";

			StringBuffer largeValueSB = new StringBuffer(shortValue);

			while (largeValueSB.length() < maxSize) {
			    largeValueSB.append(shortValue);
			}

			String s = largeValueSB.substring(0, maxSize);

			InputStream is = new ByteArrayInputStream (s.getBytes ("ISO8859_1"));
			ps.setAsciiStream (1, is, is.available ());
			ps.executeUpdate();
			ps.close();
			ResultSet rs = statement_.executeQuery("SELECT * FROM "+tableName+"");
			boolean check = rs.next();
			Clob clobR = rs.getClob(1);

//		String res = clobR.getSubString(1, clobR.length());
/*	Because Java docs are weird, I can't extract any long # of characters from clob in the
	above method... and so using the following... :-( which is very time consuming !! */

			int blockSize = 99999;
			long count = maxSize;
			String res = "";
			int nextRead;
			long startIndex = 1;
			while(count > 0){
			    nextRead = (count>blockSize)? blockSize: (int)count;
			    res += clobR.getSubString( startIndex, nextRead);
			    count -= nextRead;
			    startIndex += nextRead;
			}

			assertCondition(check && s.equals(res), "res = "+res+" SB "+s+
					" \n check = "+check+" SB true");
		    }catch(SQLException e){
			failed(e, "New variation added by native driver");
		    }catch(Exception e){
			failed(e, "New variation added by Native driver: nonSQL Exception thrown.");
		    }

	}

    }

    public void Var039()	// Added this var - @E2
    {
	if(checkNative()) {
	    String tableName = JDPSTest.COLLECTION+".T1VAR039"; 

	    try{

		try{
		    statement_.executeUpdate("DROP TABLE "+tableName);
		}catch(Exception e){
		}

		long maxSize = 499998;

		String url = baseURL_
		  
		  
		  + ";data truncation=true;lob threshold="+(maxSize-1)+";errors=full";
		connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
		statement_ = connection_.createStatement ();

		try{
		    statement_.executeUpdate("DROP TABLE "+tableName);
		}catch(Exception e){
		}

		statement_.executeUpdate("CREATE TABLE "+tableName+" ( C1 CLOB("+maxSize+") )");

		PreparedStatement ps = connectionNoDT_.prepareStatement("INSERT INTO "+tableName+" VALUES (?)");
		String s = "A";
		for(long l=1; l<maxSize ; l*=2)
		    s += s;
		if(s.length() > maxSize)
		    s = s.substring(0, (int)maxSize);

		InputStream is = new ByteArrayInputStream (s.getBytes ("ISO8859_1"));
		ps.setAsciiStream (1, is, is.available ());
		ps.executeUpdate();
		ps.close();
		ResultSet rs = statement_.executeQuery("SELECT * FROM "+tableName);
		boolean check = rs.next();
		Clob clobR = rs.getClob(1);

//		String res = clobR.getSubString(1,clobR.length());
/*	Because Java docs are weird, I can't extract any long # of characters from clob in the
	above method... and so using the following... :-( */

		int blockSize = 16384;
		long count = maxSize;
		String res = "";
		int nextRead;
		long startIndex = 1;
		while(count > 0){
		    nextRead = (count>blockSize)? blockSize: (int)count;
		    res += clobR.getSubString( startIndex, nextRead);
		    count -= nextRead;
		    startIndex += nextRead;
		}

		assertCondition(check && s.equals(res), "res = "+res+" SB "+s+
				" \n check = "+check+" SB true");
	    }catch(SQLException e){
		failed(e, "New variation added by native driver");
	    }catch(Exception e){
		failed(e, "New variation added by Native driver.. nonSQL Exception thrown !!");
	    }

	}


    }
    
    /**
    setAsciiStream() - Should work with an input stream that occasionally returns
                       0 bytes from the read method. 
    **/
        public void Var040()
        {
            // Per Toolbox implementation... 
            // The spec says to throw an exception when the
            // actual length does not match the specified length.
            // I think this is strange since this means the length
            // parameter is essentially not needed.  I.e., we always
            // read the exact number of bytes in the stream.
            if( isToolboxDriver()){
                notApplicable("Toolbox possible todo later");
                return;
            }
            String added = "Added by native driver 10/11/2006 to test input stream that sometimes returns 0 bytes "; 
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_KEY, C_VARCHAR_50) VALUES (?, ?)");
                ps.setString (1, "Muchas");
                InputStream is = new JDWeirdInputStream("0102030");
                ps.setAsciiStream (2, is, 6 );
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);

                rs.close ();
                String expected = " !\"#$%";
                assertCondition (check.equals (expected), "\nExpected :'"+expected+"'" +
                                                          "\nGot      :'"+check+"'" + added );
                
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception "+added);
            }
        }

        /**
        setAsciiStream() - Should work with an input stream that occasionally returns
                           0 bytes from the read method and uses a portion of the last
                           block returned 
        **/
            public void Var041()
            {
                // Per Toolbox implementation... 
                // The spec says to throw an exception when the
                // actual length does not match the specified length.
                // I think this is strange since this means the length
                // parameter is essentially not needed.  I.e., we always
                // read the exact number of bytes in the stream.
                if( isToolboxDriver()){
                    notApplicable("Toolbox possible todo later");
                    return;
                }
                String added = "Added by native driver 10/11/2006 to test input stream that sometimes returns 0 bytes "; 
                try {
                    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                    PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_KEY, C_VARCHAR_50) VALUES (?, ?)");
                    ps.setString (1, "Muchas");
                    InputStream is = new JDWeirdInputStream("0102030");
                    ps.setAsciiStream (2, is, 5 );
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
                    rs.next ();
                    String check = rs.getString (1);

                    rs.close ();
                    String expected = " !\"#$";
                    assertCondition (check.equals (expected), "\nExpected :'"+expected+"'" +
                                                              "\nGot      :'"+check+"'" + added );
                    
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception "+added);
                }
            }

            /**
             setAsciiStream() - Set a DECFLOAT16 parameter.
             **/
            public void Var042()
            {
                // Per Toolbox implementation... 
                // The spec says to throw an exception when the
                // actual length does not match the specified length.
                // I think this is strange since this means the length
                // parameter is essentially not needed.  I.e., we always
                // read the exact number of bytes in the stream.
                if( isToolboxDriver()){
                    notApplicable("Toolbox possible todo later");
                    return;
                }
              if (checkDecFloatSupport()) {
                try {
                  PreparedStatement ps = connection_.prepareStatement (
                      "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                      + " VALUES (?)");
                  InputStream is = new ByteArrayInputStream ("Cuba".getBytes ("ISO8859_1"));
                  ps.setAsciiStream (1, is, is.available ());
                  ps.close ();
                  if( getDriver() == JDTestDriver.DRIVER_NATIVE &&
                      getRelease() <= JDTestDriver.RELEASE_V5R2M0 )
                    assertCondition(true);
                  else
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
              }
            }
            
            /**
            setAsciiStream() - Set a DECFLOAT34 parameter.
            **/
           public void Var043()
           {
             if (checkDecFloatSupport()) {
               try {
                 PreparedStatement ps = connection_.prepareStatement (
                     "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                     + " VALUES (?)");
                 InputStream is = new ByteArrayInputStream ("Cuba".getBytes ("ISO8859_1"));
                 ps.setAsciiStream (1, is, is.available ());
                 ps.close ();
                 if( getDriver() == JDTestDriver.DRIVER_NATIVE &&
                     getRelease() <= JDTestDriver.RELEASE_V5R2M0 )
                   assertCondition(true);
                 else
                   failed ("Didn't throw SQLException");
               }
               catch (Exception e) {
                 assertExceptionIsInstanceOf (e, "java.sql.SQLException");
               }
             }
           }
           


/**
setAsciiStream() - Set an XML  parameter.
**/
	   public void setXML(String tablename, String byteEncoding, String data, String expected) {
	       String added = " -- added by native driver 08/21/2009"; 

	       
	       if (checkXmlSupport ()) {
		   try {
		       statement_.executeUpdate ("DELETE FROM " + tablename);

		       PreparedStatement ps = connection_.prepareStatement (
									    "INSERT INTO " + tablename
									    + "  VALUES (?)");
		       byte[] bytes = data.getBytes (byteEncoding); 
		       InputStream is = new ByteArrayInputStream (bytes);
		       ps.setAsciiStream (1, is, bytes.length);
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
					added );
		   }
		   catch (Exception e) {
		       failed (e, "Unexpected Exception"+added);
		   }
	       }
	   }



/**
setAsciiStream() - Set an XML  parameter using invalid data.
**/
	   public void setInvalidXML(String tablename, String byteEncoding, String data, String expectedException) {
	       String added = " -- added by native driver 08/21/2009"; 

	       if (checkXmlSupport ()) {
		   try {
		       statement_.executeUpdate ("DELETE FROM " + tablename);

		       PreparedStatement ps = connection_.prepareStatement (
									    "INSERT INTO " + tablename
									    + "  VALUES (?)");
		       byte[] bytes = data.getBytes (byteEncoding); 
		       InputStream is = new ByteArrayInputStream (bytes);
		       ps.setAsciiStream (1, is, bytes.length);
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

	   /* If no declaration, encoding is assumed to be UTF-8 not ISO8859 */ 
	   public void Var044() { 
	       if( isToolboxDriver()){
               notApplicable("utf-8 as ascii");
               return;
           }
	       setXML(JDPSTest.PSTEST_SETXML, "UTF-8", "<Test>VAR044\u00a2</Test>",  "<Test>VAR044\u00a2</Test>"); }
	   public void Var045() { setXML(JDPSTest.PSTEST_SETXML, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR045\u00a2</Test>",  "<Test>VAR045\u00a2</Test>"); }
	   public void Var046() { 
           if( isToolboxDriver()){
               notApplicable("utf-8 as ascii");
               return;
           }
           setXML(JDPSTest.PSTEST_SETXML, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR046\u0130\u3041\ud800\udf30</Test>",  "<Test>VAR046\u0130\u3041\ud800\udf30</Test>"); }

	   public void Var047() { setXML(JDPSTest.PSTEST_SETXML13488, "UTF-8", "<Test>VAR047</Test>",  "<Test>VAR047</Test>"); }
	   public void Var048() { setXML(JDPSTest.PSTEST_SETXML13488, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR048\u00a2</Test>",  "<Test>VAR048\u00a2</Test>"); }
	   public void Var049() { 
           if( isToolboxDriver()){
               notApplicable("utf-8 as ascii");
               return;
           }
           setXML(JDPSTest.PSTEST_SETXML13488, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR049\u0130\u3041</Test>",  "<Test>VAR049\u0130\u3041</Test>"); }

	   public void Var050() { setXML(JDPSTest.PSTEST_SETXML1200, "UTF-8", "<Test>VAR050</Test>",  "<Test>VAR050</Test>"); }
	   public void Var051() { setXML(JDPSTest.PSTEST_SETXML1200, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR051\u00a2</Test>",  "<Test>VAR051\u00a2</Test>"); }
	   public void Var052() { 
           if( isToolboxDriver()){
               notApplicable("utf-8 as ascii");
               return;
           }
           setXML(JDPSTest.PSTEST_SETXML1200, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR052\u0130\u3041\ud800\udf30</Test>",  "<Test>VAR052\u0130\u3041\ud800\udf30</Test>"); }

	   public void Var053() { 
           if( isToolboxDriver()){
               notApplicable("utf-8 as ascii");
               return;
           }
           setXML(JDPSTest.PSTEST_SETXML37, "UTF-8", "<Test>VAR053\u00a2</Test>",  "<Test>VAR053\u00a2</Test>"); }
	   public void Var054() { setXML(JDPSTest.PSTEST_SETXML37, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR054\u00a2</Test>",  "<Test>VAR054\u00a2</Test>"); }
	   public void Var055() { 
           if( isToolboxDriver()){
               notApplicable("utf-8 as ascii");
               return;
           }
           setXML(JDPSTest.PSTEST_SETXML37, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR055\u00a2</Test>",  "<Test>VAR055\u00a2</Test>"); }

	   public void Var056() { 
           if( isToolboxDriver()){
               notApplicable("utf-8 as ascii");
               return;
           }
           setXML(JDPSTest.PSTEST_SETXML937, "UTF-8", "<Test>VAR056\u00a2</Test>",  "<Test>VAR056\u00a2</Test>"); }
	   public void Var057() { setXML(JDPSTest.PSTEST_SETXML937, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR057\u00a2</Test>",  "<Test>VAR057\u00a2</Test>"); }
	   public void Var058() { 
           if( isToolboxDriver()){
               notApplicable("utf-8 as ascii");
               return;
           }
           setXML(JDPSTest.PSTEST_SETXML937, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR058\u00a2</Test>",  "<Test>VAR058\u00a2</Test>"); }

	   public void Var059() { 
           if( isToolboxDriver()){
               notApplicable("utf-8 as ascii");
               return;
           }
           setXML(JDPSTest.PSTEST_SETXML290, "UTF-8", "<Test>VAR059\u00a2</Test>",  "<Test>VAR059\u00a2</Test>"); }
	   public void Var060() { setXML(JDPSTest.PSTEST_SETXML290, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR060\u00a2</Test>",  "<Test>VAR060\u00a2</Test>"); }
	   public void Var061() {
           if( isToolboxDriver()){
               notApplicable("utf-8 as ascii");
               return;
           }
           setXML(JDPSTest.PSTEST_SETXML290, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR061\uff98</Test>",  "<Test>VAR061\uff98</Test>"); }




	   public void Var062() { 
           if( isToolboxDriver()){
               setXML(JDPSTest.PSTEST_SETXML, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"UTF-16\"?><Test>VAR062</Test>",  "<Test>VAR062</Test>"); 
               return;
           }
           setInvalidXML(JDPSTest.PSTEST_SETXML, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"UTF-16\"?><Test>VAR062</Test>",  "XML parsing failed"); 
       }
	   public void Var063() { setInvalidXML(JDPSTest.PSTEST_SETXML, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR063</Tes>",  "XML parsing failed"); }

	   public void Var064() {
	       if( isToolboxDriver()){
	           setXML(JDPSTest.PSTEST_SETXML13488, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR064</Test>",  "<Test>VAR064</Test>" ); 
               return;
           }
	       setInvalidXML(JDPSTest.PSTEST_SETXML13488, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR064</Test>",  "data not properly formed" ); }
	   public void Var065() { setInvalidXML(JDPSTest.PSTEST_SETXML13488, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Tes>VAR065</Test>",  "XML parsing failed"); }

	   public void Var066() { 
	       if( isToolboxDriver()){
	           setXML(JDPSTest.PSTEST_SETXML1200, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"UTF-16\"?><Test>VAR066</Test>",  "<Test>VAR066</Test>");
               return;
           }
	       setInvalidXML(JDPSTest.PSTEST_SETXML1200, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"UTF-16\"?><Test>VAR066</Test>",  "XML parsing failed"); }
	   public void Var067() { setInvalidXML(JDPSTest.PSTEST_SETXML1200, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Tes>VAR067</Test>",  "XML parsing failed"); }

	   public void Var068() { 
	       if( isToolboxDriver()){
	           setXML(JDPSTest.PSTEST_SETXML37, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"IBM037\"?><Test>VAR068</Test>",  "<Test>VAR068</Test>");
               return;
           }
	       setInvalidXML(JDPSTest.PSTEST_SETXML37, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"IBM037\"?><Test>VAR068\u0130\u3041\ud800\udf30</Test>",  "XML parsing failed"); }
	   public void Var069() { setInvalidXML(JDPSTest.PSTEST_SETXML37, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Tet>VAR069</Test>",  "XML parsing failed"); }



  public void setValidTest(String column, String inputValue,
      String outputValue) {
    try {

      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (" + column + ") VALUES (?)");
      InputStream is = new ByteArrayInputStream(
          inputValue.getBytes("ISO8859_1"));
      ps.setAsciiStream(1, is, is.available());
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT " + column + " FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      String check = rs.getString(1);
      rs.close();

      assertCondition(outputValue.equals(check),
          " check was " + check + " sb " + outputValue);

    } catch (Exception e) {
      failed(e, "Unexpected Exception ");

    }

  }

  /**
   * setAsciiStream() - Set a Boolean parameter.  Should fail. 
   **/
  public void Var070() {
    if (checkBooleanSupport()) {
      setInvalid("C_BOOLEAN","1"); 
    }
  }

  /**
   * setAsciiStream() - Set a Boolean parameter.
   **/
  public void Var071() {
    if (checkBooleanSupport()) {
      setInvalid("C_BOOLEAN","0"); 
    }
  }

  /**
   * setAsciiStream() - Set a Boolean parameter.
   **/
  public void Var072() {
    if (checkBooleanSupport()) {
      setInvalid("C_BOOLEAN","true"); 
    }
  }

  /**
   * setAsciiStream() - Set a Boolean parameter.
   **/
  public void Var073() {
    if (checkBooleanSupport()) {
      setInvalid("C_BOOLEAN","false"); 
    }
  }

  

/**
setAsciiStream() - Set a BOOLEAN parameter.
**/
  public void Var074() {

    if (checkBooleanSupport()) {
      setInvalid("C_BOOLEAN","Cuba");
    }
  }

}




