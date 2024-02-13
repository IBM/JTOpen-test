///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetUnicodeStream.java
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
 // File Name:    JDPSSetUnicodeStream.java
 //
 // Classes:      JDPSSetUnicodeStream
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;
import test.JD.JDTestUtilities;
import test.JD.JDWeirdInputStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDPSSetUnicodeStream.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setUnicodeStream()
</ul>
**/
public class JDPSSetUnicodeStream
extends JDTestcase
{



    // Constants.
    private static final String PACKAGE             = "JDPSSUS";



    // Private data.
    private Statement           statement_;

   StringBuffer sb = new StringBuffer(); 

/**
Constructor.
**/
    public JDPSSetUnicodeStream (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetUnicodeStream",
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
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        String url = baseURL_; 
        connection_ = testDriver_.getConnection (url, userId_, encryptedPassword_);
        
      } else { 
              String url = baseURL_
            
            
            + ";data truncation=true";
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
      }
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
setUnicodeStream() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            ps.close ();
            InputStream is = new ByteArrayInputStream ("Canada".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            InputStream is = new ByteArrayInputStream ("United States".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (100, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            InputStream is = new ByteArrayInputStream ("Mexico".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (0, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            InputStream is = new ByteArrayInputStream ("Virgin Islands".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (0, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Should set to SQL NULL when the value is null.
**/
    public void Var005()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            ps.setUnicodeStream (1, null, 0);
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
setUnicodeStream() - Should work with a valid parameter index
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
            InputStream is = new ByteArrayInputStream ("El Salvador".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (2, is, is.available ());
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
setUnicodeStream() - Should throw exception when the length is not valid.
**/
    public void Var007()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Banana Republic".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, -1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var008()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC permits setting of an output parameter");
        return; 
      } 
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            InputStream is = new ByteArrayInputStream ("Honduras".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (2,is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Verify that a data truncation warning is
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
            InputStream is = new ByteArrayInputStream (s.getBytes ("UnicodeBigUnmarked")); // @B0C
            length = s.length ();
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
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
setUnicodeStream() - Set a SMALLINT parameter.
**/
    public void Var010()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Costa Rica".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a INTEGER parameter.
**/
    public void Var011()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Cuba".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a REAL parameter.
**/
    public void Var012()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_REAL) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Jamaica".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a FLOAT parameter.
**/
    public void Var013()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_FLOAT) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Haiti".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a DOUBLE parameter.
**/
    public void Var014()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DOUBLE) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Trinidad".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a DECIMAL parameter.
**/
    public void Var015()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_105) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Ecuador".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a NUMERIC parameter.
**/
    public void Var016()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Brazil".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a CHAR(1) parameter.
**/
    public void Var017()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_1) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("U".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
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
setUnicodeStream() - Set a CHAR(50) parameter.
**/
    public void Var018()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Uruguay".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
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
setUnicodeStream() - Set a VARCHAR(50) parameter, with the length equal
to the full stream, an even number (to deal with Unicode issues).
**/
    public void Var019()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Paraguay".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Paraguay"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setUnicodeStream() - Set a VARCHAR(50) parameter, with the length equal
to the full stream, an odd number (to deal with Unicode issues).

@E1 - Native driver doesn't throw an exception if stream contains more characters than needs to be read
**/
    public void Var020()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Paraguay".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available () - 1);
	    if( getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&		// @E1
		getDriver() == JDTestDriver.DRIVER_NATIVE )		// @E1
		succeeded();						// @E1
	    else							// @E1
		failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if( getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&		// @E1
		getDriver() == JDTestDriver.DRIVER_NATIVE )		// @E1
		succeeded();						// @E1
	    else							// @E1
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a VARCHAR(50) parameter, with the length less than
the full stream.

@E1 - Native driver doesn't throw an exception if stream contains more characters than needs to be read
**/
    public void Var021()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Tobago".getBytes ("UnicodeBigUnmarked")); // @B0C
            InputStream is2 = new ByteArrayInputStream ("Tob".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is2.available ());
	    if( getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&		// @E1
		getDriver() == JDTestDriver.DRIVER_NATIVE )		// @E1
		succeeded();						// @E1
	    else							// @E1
		failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
    	    if( getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&		// @E1
		getDriver() == JDTestDriver.DRIVER_NATIVE )		// @E1
		succeeded();						// @E1
	    else							// @E1
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a VARCHAR(50) parameter, with the length greater than
the full stream.
**/
    public void Var022()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Long Island".getBytes ("UnicodeBigUnmarked")); // @B0C
            InputStream is2 = new ByteArrayInputStream ("Long Island and more".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is2.available () + 5);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a VARCHAR(50) parameter, with the length set to 1 character.

@E1 - Native driver doesn't throw an exception if stream contains more characters than needs to be read
**/
    public void Var023()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Yucatan Penisula".getBytes ("UnicodeBigUnmarked")); // @B0C
            InputStream is2 = new ByteArrayInputStream ("Y".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is2.available ());
	    if( getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&		// @E1
		getDriver() == JDTestDriver.DRIVER_NATIVE )		// @E1
		succeeded();						// @E1
	    else							// @E1

		failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if( getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&		// @E1
		getDriver() == JDTestDriver.DRIVER_NATIVE )		// @E1
		succeeded();						// @E1
	    else							// @E1

		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a VARCHAR(50) parameter, with the length set to 0.

@E1 - Native driver doesn't throw an exception if stream contains more characters than needs to be read
**/
    public void Var024()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Baja California".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, 0);
	    if( getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&		// @E1
		getDriver() == JDTestDriver.DRIVER_NATIVE )		// @E1
		succeeded();						// @E1
	    else							// @E1

		failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if( getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&		// @E1
		getDriver() == JDTestDriver.DRIVER_NATIVE )		// @E1
		succeeded();						// @E1
	    else							// @E1

		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a VARCHAR(50) parameter to the empty string.
**/
    public void Var025()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
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
setUnicodeStream() - Set a VARCHAR(50) parameter to a bad input stream.
**/
    public void Var026()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");

            class BadInputStream extends InputStream
            {
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
            ps.setUnicodeStream (1, r, 2);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a CLOB parameter.
**/
    public void Var027()
    {
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_CLOB) VALUES (?)");
                InputStream is = new ByteArrayInputStream ("Argentina".getBytes ("UnicodeBigUnmarked")); // @B0C
                ps.setUnicodeStream (1, is, is.available ());
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("Argentina"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
setUnicodeStream() - Set a DBCLOB parameter.
**/
    public void Var028()
    {
        if (checkLobSupport ()) {
            succeeded ();
            /* Need to investigate this variation ...
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DBCLOB) VALUES (?)");
                InputStream is = new ByteArrayInputStream ("Peru".getBytes ("UnicodeBigUnmarked")); // @B0C
                ps.setUnicodeStream (1, is, is.available ());
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
setUnicodeStream() - Set a BINARY parameter.
**/
    public void Var029()
    {
        try {
            String expected = null;

            if (isToolboxDriver())
               expected = "436F6C6F6D616960";
            else
               expected = "Colombia Heights";

            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BINARY_20) VALUES (?)");

            InputStream is = new ByteArrayInputStream (expected.getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
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
setUnicodeStream() - Set a VARBINARY parameter.
**/
    public void Var030()
    {
        try {
            String expected = null;

             if (isToolboxDriver())
                expected = "507565";
             else
                expected = "Puerto";

            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARBINARY_20) VALUES (?)");

            InputStream is = new ByteArrayInputStream (expected.getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
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
setUnicodeStream() - Set a BLOB parameter.
**/
    public void Var031()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BLOB) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Guam".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setUnicodeStream() - Set a DATE parameter.
**/
    public void Var032()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DATE) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Virgin Islands".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a TIME parameter.
**/
    public void Var033()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIME) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Bermuda".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setUnicodeStream() - Set a TIMESTAMP parameter.
**/
    public void Var034()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIMESTAMP) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Bahamas".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
setUnicodeStream() - Set a DATALINK parameter.
**/
    public void Var035()
    {
        if (checkDatalinkSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(120))))");
                String url = "http://www.falkandislands.com/map.html";
                InputStream is = new ByteArrayInputStream (url.getBytes ("UnicodeBigUnmarked")); // @B0C
                ps.setUnicodeStream (1, is, is.available ());
                ps.executeUpdate();
                ps.close ();

               ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + JDPSTest.PSTEST_SET);
               rs.next ();
               String check = rs.getString (1);
               rs.close ();

               assertCondition (check.equalsIgnoreCase(url), "fetched string("+check+") != url("+url+")");
           }
           catch (Exception e) {
               failed (e, "Unexpected Exception");
           }
       }
   }




/**
setUnicodeStream() - Set a DISTINCT parameter.
**/
    public void Var036()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DISTINCT) VALUES (?)");
                InputStream is = new ByteArrayInputStream ("Strait of Magellan".getBytes ("UnicodeBigUnmarked")); // @B0C
                ps.setUnicodeStream (1, is, is.available ());
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setUnicodeStream() - Set a BIGINT parameter.
**/
    public void Var037()
    {
        if (checkBigintSupport()) {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            InputStream is = new ByteArrayInputStream ("Spain".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
setUnicodeStream() - Set a VARCHAR(50) parameter with package caching.
**/
    public void Var038()
    {
        try {
            String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)";

            if (isToolboxDriver())
               JDSetupPackage.prime (systemObject_, PACKAGE,
                   JDPSTest.COLLECTION, insert);
            else
               JDSetupPackage.prime (systemObject_, encryptedPassword_, PACKAGE,
                   JDPSTest.COLLECTION, insert, "", getDriver());

            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            Connection c2 = testDriver_.getConnection (baseURL_
                + ";extended dynamic=true;package=" + PACKAGE
                + ";package library=" + JDPSTest.COLLECTION
                + ";package cache=true", userId_, encryptedPassword_);
            // 10/11/2006 changed to use c2 as intended 
            PreparedStatement ps = c2.prepareStatement (insert);
            InputStream is = new ByteArrayInputStream ("Antarctica".getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());
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


/**
setUnicodeStream() - Should work with a funky input stream that will return 0 bytes
**/
    public void Var039()
    {
        // Per Toolbox implementation... 
        // The spec says to throw an exception when the
        // actual length does not match the specified length.
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

	    InputStream is = new JDWeirdInputStream("0102030102030", "UNICODE");
	    ps.setUnicodeStream (2, is, 12);
	    ps.executeUpdate ();
	    ps.close ();

	    ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
	    rs.next ();
	    String check = rs.getString (1);

	    rs.close ();
            String expected = " !\"#$%";
	    assertCondition (check.equals (expected), "\nExpected :'"+expected+"'" +
                "\nGot      :'"+check+"'" + added);
	}
	catch (Exception e) {
	    failed (e, "Unexpected Exception");
	}
    }



    /**
    setUnicodeStream() - Should work with a funky input stream that will return 0 bytes 
                         and not all of stream is read. 
    **/
        public void Var040()
        {
            // Per Toolbox implementation... 
            // The spec says to throw an exception when the
            // actual length does not match the specified length.
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

            InputStream is = new JDWeirdInputStream("0102030102030", "UNICODE");
            ps.setUnicodeStream (2, is, 10);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);

            rs.close ();
                String expected = " !\"#$";
            assertCondition (check.equals (expected), "\nExpected :'"+expected+"'" +
                    "\nGot      :'"+check+"'" + added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }



        /**
        setUnicodeStream() - Set a DECFLOAT parameter.
        **/
            public void Var041()
            {
                if (checkDecFloatSupport()) {
                try {
                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                        + "  VALUES (?)");
                    InputStream is = new ByteArrayInputStream ("Spain".getBytes ("UnicodeBigUnmarked")); // @B0C
                    ps.setUnicodeStream (1, is, is.available ());
                    ps.executeUpdate(); ps.close ();
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
                }
            }

            /**
            setUnicodeStream() - Set a DECFLOAT parameter.
            **/
                public void Var042()
                {
                    if (checkDecFloatSupport()) {
                    try {
                        PreparedStatement ps = connection_.prepareStatement (
                            "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                            + "  VALUES (?)");
                        InputStream is = new ByteArrayInputStream ("S pain".getBytes ("UnicodeBigUnmarked")); // @B0C
                        ps.setUnicodeStream (1, is, is.available ());
                        ps.executeUpdate(); ps.close ();
                        failed ("Didn't throw SQLException");
                    }
                    catch (Exception e) {
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    }
                    }
                }



		public void Var043() { notApplicable(); } 


/**
setXML() - Set an XML  parameter.
**/
	   public void setXML(String tablename, String data, String expected) {
	     boolean passed = true; 
	       sb.setLength(0); 
	       sb.append(" -- added by native driver 08/21/2009\n");
	       if (checkXmlSupport ()) {
		   try {
		       String sql = "DELETE FROM " + tablename;
		       sb.append("SQL="+sql+"\n"); 
		       statement_.executeUpdate (sql);

           sql = "INSERT INTO " + tablename
                + "  VALUES (?)"; 
	           sb.append("Preparing "+sql+"\n"); 

		       PreparedStatement ps = connection_.prepareStatement (
									    sql);

		       sb.append("Creating ByteArrayInputStream from "+data+"\n"); 
		       InputStream is = new ByteArrayInputStream (data.getBytes ("UnicodeBigUnmarked")); // @B0C
		       sb.append("Calling ps.setUnicodeStream"); 
		       ps.setUnicodeStream (1, is, is.available ());
		       sb.append("Calling executeUpdate()\n"); 
		       ps.executeUpdate ();
		       ps.close ();

		       ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename);
		       rs.next ();
		       String check = rs.getString (1);
		       rs.close ();
		       passed = check.equals (expected);
		       if (!passed) {
		         sb.append("check = "+
		             JDTestUtilities.getMixedString(check)+
		             " And SB "+
		             JDTestUtilities.getMixedString(expected)+"\n"); 
		       }
		       assertCondition (passed,sb); 
		   }
		   catch (Exception e) {
		       failed (e, "Unexpected Exception"+sb.toString());
		   }
	       }
	   }



/**
setCharacterStream() - Set an XML  parameter using invalid data.
**/
	   public void setInvalidXML(String tablename, String data, String expectedException) {
	    String added = " -- added by native driver 08/21/2009";
	       if (checkXmlSupport ()) {
		   try {
		       statement_.executeUpdate ("DELETE FROM " + tablename);

		       PreparedStatement ps = connection_.prepareStatement (
									    "INSERT INTO " + tablename
									    + "  VALUES (?)");

		       InputStream is = new ByteArrayInputStream (data.getBytes ("UnicodeBigUnmarked")); // @B0C
		       ps.setUnicodeStream (1, is, is.available ());


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

	   /* Note.  For native this goes through binary stream and to
              work, the binary stream must begin with <? xml */ 
	   public void Var044() { setXML(JDPSTest.PSTEST_SETXML,  "<?xml version=\"1.0\"?> <Test>VAR044\u00a2</Test>",  "<Test>VAR044\u00a2</Test>"); }
	   public void Var045() { setXML(JDPSTest.PSTEST_SETXML,  "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR045\u00a2</Test>",  "<Test>VAR045\u00a2</Test>"); }
	   public void Var046() { setXML(JDPSTest.PSTEST_SETXML,  "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR046\u0130\u3041\ud800\udf30</Test>",  "<Test>VAR046\u0130\u3041\ud800\udf30</Test>"); }

	   public void Var047() { setXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\"?> <Test>VAR047</Test>",  "<Test>VAR047</Test>"); }
	   public void Var048() { setXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR048\u00a2</Test>",  "<Test>VAR048\u00a2</Test>"); }
	   public void Var049() { setXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR049\u0130\u3041</Test>",  "<Test>VAR049\u0130\u3041</Test>"); }

	   public void Var050() { setXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\"?> <Test>VAR050</Test>",  "<Test>VAR050</Test>"); }
	   public void Var051() { setXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR051\u00a2</Test>",  "<Test>VAR051\u00a2</Test>"); }
	   public void Var052() { setXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR052\u0130\u3041\ud800\udf30</Test>",  "<Test>VAR052\u0130\u3041\ud800\udf30</Test>"); }

	   public void Var053() { setXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\"?> <Test>VAR053\u00a2</Test>",  "<Test>VAR053\u00a2</Test>"); }
	   public void Var054() { setXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR054\u00a2</Test>",  "<Test>VAR054\u00a2</Test>"); }
	   public void Var055() { setXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR055\u00a2</Test>",  "<Test>VAR055\u00a2</Test>"); }

	   public void Var056() { setXML(JDPSTest.PSTEST_SETXML937, "<?xml version=\"1.0\"?> <Test>VAR056\u672b</Test>",  "<Test>VAR056\u672b</Test>"); }
	   public void Var057() { setXML(JDPSTest.PSTEST_SETXML937, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR057\u672b</Test>",  "<Test>VAR057\u672b</Test>"); }
	   public void Var058() { setXML(JDPSTest.PSTEST_SETXML937, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR058\u672b</Test>",  "<Test>VAR058\u672b</Test>"); }

	   public void Var059() { setXML(JDPSTest.PSTEST_SETXML290, "<?xml version=\"1.0\"?> <Test>VAR059</Test>",  "<Test>VAR059</Test>"); }
	   public void Var060() { setXML(JDPSTest.PSTEST_SETXML290, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR060\uff7a</Test>",  "<Test>VAR060\uff7a</Test>"); }
	   public void Var061() { setXML(JDPSTest.PSTEST_SETXML290, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR061\uff98</Test>",  "<Test>VAR061\uff98</Test>"); }




	   public void Var062() {  
	       if( isToolboxDriver()){
	           setXML(JDPSTest.PSTEST_SETXML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>VAR062</Test>",  "<Test>VAR062</Test>"); 
	           return;
	       }
	       setInvalidXML(JDPSTest.PSTEST_SETXML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>VAR062</Test>",  "XML parsing failed"); }
	   public void Var063() { setInvalidXML(JDPSTest.PSTEST_SETXML, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR063</Tes>",  "XML parsing failed"); }

	   public void Var064() { 
	       if( isToolboxDriver()){
	           setXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>VAR064</Test>",  "<Test>VAR064</Test>" ); 
               return;
           }
	       setInvalidXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>VAR064</Test>",  "XML parsing failed" ); }
	   public void Var065() { setInvalidXML(JDPSTest.PSTEST_SETXML13488, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tes>VAR065</Test>",  "XML parsing failed"); }

	   public void Var066() {
	       if( isToolboxDriver()){
	           setXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>VAR066</Test>",  "<Test>VAR066</Test>"); 
               return;
           }
	       setInvalidXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>VAR066</Test>",  "XML parsing failed"); }
	   public void Var067() { setInvalidXML(JDPSTest.PSTEST_SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tes>VAR067</Test>",  "XML parsing failed"); }

	   public void Var068() { 
	       if( isToolboxDriver()){
	           setXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>VAR068\u00a2</Test>",  "<Test>VAR068\u00a2</Test>");
               return;
           }
	       setInvalidXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>VAR068\u00a2</Test>",  "XML parsing failed"); }
	   public void Var069() { setInvalidXML(JDPSTest.PSTEST_SETXML37, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tet>VAR069</Test>",  "XML parsing failed"); }


	    public void setInvalid(String column, String inputValue, String exceptionInfo)  {
	      StringBuffer sb = new StringBuffer(); 
	      try {
	        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

	        PreparedStatement ps = connection_.prepareStatement(
	            "INSERT INTO " + JDPSTest.PSTEST_SET + " ("+column+") VALUES (?)");


            InputStream is = new ByteArrayInputStream (inputValue.getBytes ("UnicodeBigUnmarked")); // @B0C
            ps.setUnicodeStream (1, is, is.available ());

	        ps.close();
	        failed("Didn't throw SQLException for column("+column+") inputValue("+inputValue+")");
	      } catch (Exception e) {
	        assertExceptionContains(e, exceptionInfo, sb);
	      }
	    }

	    public void Var070() {
		if (checkBooleanSupport()) { 
		    setInvalid("C_BOOLEAN","true","Data type mismatch" );
		}
	    }

	     public void Var071() {
		 if (checkBooleanSupport()) { 
		     setInvalid("C_BOOLEAN","false","Data type mismatch" );
		 }
	     }






}




