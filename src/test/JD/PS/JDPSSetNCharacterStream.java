///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetNCharacterStream.java
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
 // File Name:    JDPSSetNCharacterStream.java
 //
 // Classes:      JDPSSetNCharacterStream
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTOpenTestEnvironment;
import test.JD.JDSetupPackage;
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;
import java.io.FileWriter;
import java.io.FileReader;
import java.sql.Types; 


/**
Testcase JDPSSetNCharacterStream.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setNCharacterStream()
</ul>
**/
public class JDPSSetNCharacterStream
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetNCharacterStream";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Constants.
    private static final String PACKAGE             = "JDPSSCS";



    // Private data.
    private Connection          connection_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetNCharacterStream (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetNCharacterStream",
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
setNCharacterStream() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");
                ps.close ();
                Reader r = new StringReader ("Rochester");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long)9L);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
                Reader r = new StringReader ("Douglas");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",100, r,7L);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
                Reader r = new StringReader ("Pine Island");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",0, r, 11L);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
                Reader r = new StringReader ("Zumbrota");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",0, r, 8L);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Should set to SQL NULL when the value is null.
**/
    public void Var005()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, (Reader) null, 0L);
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
    }



/**
setNCharacterStream() - Should work with a valid parameter index
greater than 1.
**/
    public void Var006()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_KEY, C_VARCHAR_50) VALUES (?, ?)");
                ps.setString (1, "Muchas");
                Reader r = new StringReader ("Cannon Falls");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",2, r, 12L);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);

                rs.close ();

                assertCondition (check.equals ("Cannon Falls"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setNCharacterStream() - Should throw exception when the length is not valid.
**/
    public void Var007()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");
                Reader r = new StringReader ("Hampton");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) -1L);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var008()
    {
        if (checkJdbc40 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC permits setting of an output parameter");
            return; 
          } 
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
                Reader r = new StringReader ("Coates");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",2,r, 6L);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Verify that a data truncation warning is
posted when data is truncated.
**/
    public void Var009()
    {
        if (checkJdbc40 ()) {
            int length = 0;
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");
                String s = "Rosemount is one of the many cities you drive by on the way to Minneapolis from Rochester.";
                Reader r = new StringReader (s);
                length = s.length ();
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) length);
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
    }





/**
setNCharacterStream() - Set a SMALLINT parameter.
**/
    public void Var010()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_SMALLINT) VALUES (?)");
                Reader r = new StringReader ("Inver Grove Heights");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 19);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a INTEGER parameter.
**/
    public void Var011()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_INTEGER) VALUES (?)");
                Reader r = new StringReader ("Eagan");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 5);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a REAL parameter.
**/
    public void Var012()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_REAL) VALUES (?)");
                Reader r = new StringReader ("Mendota Heights");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 15);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a FLOAT parameter.
**/
    public void Var013()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_FLOAT) VALUES (?)");
                Reader r = new StringReader ("Bloomington");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 11);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a DOUBLE parameter.
**/
    public void Var014()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DOUBLE) VALUES (?)");
                Reader r = new StringReader ("Edina");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 5);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a DECIMAL parameter.
**/
    public void Var015()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DECIMAL_105) VALUES (?)");
                Reader r = new StringReader ("Burnsville");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 10);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a NUMERIC parameter.
**/
    public void Var016()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_NUMERIC_50) VALUES (?)");
                Reader r = new StringReader ("Richfield");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r,9L);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a CHAR(1) parameter.
**/
    public void Var017()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_CHAR_1) VALUES (?)");
                Reader r = new StringReader ("T");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 1);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("T"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
setNCharacterStream() - Set a CHAR(50) parameter.
**/
    public void Var018()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_CHAR_50) VALUES (?)");
                Reader r = new StringReader ("Minneapolis");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 11);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition ((check.startsWith ("Minneapolis")) && (check.length () == 50));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setNCharacterStream() - Set a VARCHAR(50) parameter, with the length equal
to the full stream.
**/
    public void Var019()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");
                Reader r = new StringReader ("St. Paul");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 8);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("St. Paul"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setNCharacterStream() - Set a VARCHAR(50) parameter, with the length less than
the full stream.
**/
    public void Var020()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");
                Reader r = new StringReader ("Roseville");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 4);
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @C1
		   true )	// @C1
		    succeeded();					// @C1
		else							// @C1
		    failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a VARCHAR(50) parameter, with the length greater than
the full stream.
**/
    public void Var021()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");
                Reader r = new StringReader ("Blaine");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 10);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a VARCHAR(50) parameter, with the length set to 1 character.
**/
    public void Var022()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");
                Reader r = new StringReader ("Brooklyn Center");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 1);
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @C1
		   true )	// @C1
		    succeeded();					// @C1
		else							// @C1
		    failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a VARCHAR(50) parameter, with the length set to 0.
**/
    public void Var023()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");
                Reader r = new StringReader ("Brooklyn Park");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 0);
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @C1
		   true )	// @C1
		    succeeded();					// @C1
		else							// @C1
		    failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a VARCHAR(50) parameter to the empty string.
**/
    public void Var024()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");
                Reader r = new StringReader ("");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 0);
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
    }



/**
setNCharacterStream() - Set a VARCHAR(50) parameter to a bad reader.
**/
    public void Var025()
    {
        if (checkJdbc40 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_VARCHAR_50) VALUES (?)");

                class BadReader extends StringReader
                {
  
                    public BadReader () {
                        super ("Hi Mom!");
                    }
                    public int read (char[] buffer) throws IOException {
                         throw new IOException ();
                    };
                }

                Reader r = new BadReader ();
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 2);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a CLOB parameter.
**/
    public void Var026()
    {
        if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
                try {
                    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + JDPSTest.PSTEST_SET
                        + " (C_CLOB) VALUES (?)");
                    Reader r = new StringReader ("St. Louis Park");
                    JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 14);
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
                    rs.next ();
                    String check = rs.getString (1);
                    rs.close ();

                    assertCondition (check.equals ("St. Louis Park"));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }




/**
setNCharacterStream() - Set a DBCLOB parameter.
**/
    public void Var027()
    {
        if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
                succeeded ();
                /* Need to investigate this variation ...
                try {
                    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + JDPSTest.PSTEST_SET
                        + " (C_DBCLOB) VALUES (?)");
                    Reader r = new StringReader ("Minnetonka");
                    JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 10);
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_DBCLOB FROM " + JDPSTest.PSTEST_SET);
                    rs.next ();
                    String check = rs.getString (1);
                    rs.close ();

                    assertCondition (check.equals ("Minnetonka"));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
                                     */
            }
        }
    }



/**
setNCharacterStream() - Set a BINARY parameter.
**/
    public void Var028()
    {
        if (checkJdbc40 ()) {
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
                Reader r = new StringReader (expected);
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 16);
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
    }




/**
setNCharacterStream() - Set a VARBINARY parameter.
**/
    public void Var029()
    {
        if (checkJdbc40 ()) {
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
                Reader r = new StringReader (expected);
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 6);
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
setNCharacterStream() - Set a BLOB parameter.
**/
    public void Var030()
    {
        if (checkJdbc40())
        {
            try
            {
                PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET +
								    " (C_BLOB) VALUES (?)");
                Reader r = new StringReader("FFFF123456");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream", 1, r, (long) 10);
                ps.executeUpdate(); ps.close();
		if(getDriver() == JDTestDriver.DRIVER_NATIVE)   // @C1
		    failed("Didn't throw SQLException");	// @C1: String->Blob Conversion not allowed
		else						// @C1		
		    succeeded();
                // failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&			// @C1
		   true )		// @C1
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");	// @C1
		else	
		    failed(e, "Unexpected Exception - supported added by Toolbox"+
			   " to set HEX String representation of binary type (01/24/2003).");
            }
        }
    }




/**
setNCharacterStream() - Set a DATE parameter.
**/
    public void Var031()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DATE) VALUES (?)");
                Reader r = new StringReader ("Lauderdale");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 10);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a TIME parameter.
**/
    public void Var032()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_TIME) VALUES (?)");
                Reader r = new StringReader ("Golden Valley");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 13);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setNCharacterStream() - Set a TIMESTAMP parameter.
**/
    public void Var033()
    {
        if (checkJdbc40 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_TIMESTAMP) VALUES (?)");
                Reader r = new StringReader ("Little Canada");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 13);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setNCharacterStream() - Set a DATALINK parameter.
**/
    public void Var034()
    {
        if (checkJdbc40 ()) {
            if (checkDatalinkSupport ()) {
                try {
                    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + JDPSTest.PSTEST_SET
                        + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(50))))");
                    String url = "http://www.weststpaul.com/index.html";
                    Reader r = new StringReader (url);
                    JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) url.length());
                    ps.executeUpdate ();
                    ps.close ();

                   ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + JDPSTest.PSTEST_SET);
                   rs.next ();
                   String check = rs.getString (1);
                   rs.close ();

                   assertCondition (url.equalsIgnoreCase(check), "fetched string("+check+") != url("+url+")");
               }
               catch (Exception e) {
                   failed (e, "Unexpected Exception");
               }
           }
       }
    }



/**
setNCharacterStream() - Set a DISTINCT parameter.
**/
    public void Var035()
    {
        if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
                try {
                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + JDPSTest.PSTEST_SET
                        + " (C_DISTINCT) VALUES (?)");
                    Reader r = new StringReader ("Lilydale");
                    JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 8);
                    ps.executeUpdate(); ps.close ();
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
setNCharacterStream() - Set a BIGINT parameter.
**/
    public void Var036()
    {
        if (checkJdbc40 ()) {
        if (checkBigintSupport()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_BIGINT) VALUES (?)");
                Reader r = new StringReader ("Maplewood");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long)9L);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
setNCharacterStream() - Set a VARCHAR(50) parameter with package caching.
**/
    public void Var037()
    {
        if (checkJdbc40 ()) {
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

                PreparedStatement ps = connection_.prepareStatement (insert);
                Reader r = new StringReader ("Falcon Heights");
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) 14);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("Falcon Heights"), "fetched string("+check+") != (FalconHeights)");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setNCharacterStream() - Set a large CLOB parameter.
**/
    public void Var038()
    {

	// Testcase fails in V6R1 for U testcases
	String initials = "";
	int l = JDPSTest.COLLECTION.length();
	if (l > 5) {
	    initials =  JDPSTest.COLLECTION.substring(l - 5); 
	}
	System.out.println("initials are "+initials); 
	if (initials.equals("614CU") ||
	    initials.equals("615CU") ||
	    initials.equals("616CU")    ) {
	    notApplicable("Fails with out of memory error for "+initials);
	    return; 
	}
	int length=17000000;
	String filename="/tmp/JDPSSetNCharacterStream.var038"; 
        if(isToolboxDriver())  //Determine OS so we know what type of file location to use
        {
            if (JTOpenTestEnvironment.isWindows)
            {
                filename="c:\\temp\\JDPSSetNCharacterStream.var038";
            }
        }
	String table = JDPSTest.COLLECTION + ".JDPSSCS038";
	String clobString=null; 
        if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
                try {
		    FileWriter f = null;

		    StringBuffer sb = new StringBuffer(); 
		    f = new FileWriter (filename);
		    String block="1234567890123456789012345678901234567890";
                    int    blockLength=block.length(); 
		    for (int i=0; i<length; i+=blockLength) {
			sb.append(block); 
		    }
		    clobString = sb.toString(); 
		    f.write(clobString);
		    f.flush();
		    f.close();


		    try {
			statement_.executeUpdate("DROP TABLE "+table); 
		    } catch (Exception e) {
		    }
		    statement_.executeUpdate ("CREATE TABLE "+table+"  (id INTEGER, str DBCLOB(30000000) CCSID 13488)");

                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + table +" VALUES(?,?)"); 

		    ps.setInt(1,1); 

                    Reader r = new FileReader(filename); 
                    JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",2, r, (long) length);
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT str FROM " + table); 
                    rs.next ();
                    String check = rs.getString (1);
                    rs.close ();

		    statement_.executeUpdate("DROP TABLE "+table); 

                    assertCondition (check.equals (clobString), "Testcase added 06/02/04 by native driver to check 17 meg blobs");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Testcase added 06/02/04 by native driver to check 17 meg blobs -- this will fail for native in V5R2 with a malloc error ");
                }
            }
        }
    }


/**
setNCharacterStream() - Set a CLOB parameter, with the length set to 0.
**/
    public void Var039()
    {
        if (checkJdbc40 ()) {
            try {

		//
		// Insert a different value first.  For the native driver, this will
                // change the prepared statement area.  The native driver had a bug
                // where setNCharacterStream did not do a bind if the length was zero
                // Since a bind was not done, the value that happened to already be there
                // was used. 
                // 

                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_REAL) VALUES (?)");

                ps.setFloat(1, (float) 2.0);
		ps.executeUpdate();
		ps.close();

		//
		// Now do the test
                // 

                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);



		ps = connection_.prepareStatement ("INSERT INTO " + JDPSTest.PSTEST_SET +
					/*  1 */   "(C_SMALLINT, "+
					/*  2 */   "C_CLOB, "+
					/*  3 */   "C_INTEGER, "+
					/*  4 */   "C_REAL, "+
					/*  5 */   "C_FLOAT, "+
					/*  6 */   "C_DOUBLE, "+
					/*  7 */   "C_DECIMAL_50, "+
					/*  8 */   "C_DECIMAL_105, "+
					/*  9 */   "C_NUMERIC_50, "+
					/* 10 */   "C_NUMERIC_105, "+
					/* 11 */   "C_CHAR_1, "+
					/* 12 */   "C_CHAR_50, "+
					/* 13 */   "C_VARCHAR_50, "+
					/* 14 */   "C_BINARY_1, "+
					/* 15 */   "C_BINARY_20, "+
					/* 16 */   "C_VARBINARY_20, "+
					/* 17 */   "C_DATE, "+
					/* 18 */   "C_TIME, "+
					/* 19 */   "C_TIMESTAMP, "+
					/* 20 */   "C_BLOB, "+
					/* 21 */   "C_DATALINK, "+
					/* 22 */   "C_DISTINCT, "+
					/* 23 */   "C_BIGINT) "+
						   "VALUES (?,?,?,?,?,"+
                                                           "?,?,?,?,?,"+
                                                           "?,?,?,?,?,"+
                                                           "?,?,?,?,?,"+
                                                           "?,?,?)");

                Reader r = new StringReader ("");
		ps.setNull(1, Types.SMALLINT); 
                JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",2, r, (long) 0);
		ps.setNull(3, Types.INTEGER);
		ps.setNull(4, Types.REAL);
		ps.setNull(5, Types.FLOAT);
		ps.setNull(6, Types.DOUBLE);
		ps.setNull(7, Types.DECIMAL);
		ps.setNull(8, Types.DECIMAL);
		ps.setNull(9, Types.NUMERIC);
		ps.setNull(10, Types.NUMERIC);
		ps.setNull(11, Types.CHAR);
		ps.setNull(12, Types.CHAR); 
		ps.setNull(13, Types.VARCHAR); 
		ps.setNull(14, Types.BINARY); 
		ps.setNull(15, Types.BINARY); 
		ps.setNull(16, Types.VARBINARY); 
		ps.setNull(17, Types.DATE); 
		ps.setNull(18, Types.TIME);
		ps.setNull(19, Types.TIMESTAMP);
		ps.setNull(20, Types.BLOB);
		ps.setNull(21, Types.DATALINK);
		ps.setNull(22, Types.DISTINCT);
		ps.setNull(23, Types.BIGINT); 



		ps.executeUpdate();
		ps.close ();


		ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		String check = rs.getString (1);
		rs.close ();

		assertCondition (check.equals (""), "Check failed -- Test CLOB with 0 length -- Added 7/1/2004 by native driver " );
            }
            catch (Exception e) {
                failed(e, "Unexpected exception -- Test CLOB with 0 length -- Added 7/1/2004 by native driver "); 
            }
        }
    }

    /**
    setNCharacterStream() - Set a DECFLOAT parameter.
    **/
        public void Var040()
        {
            if (checkJdbc40 ()) {
            if (checkDecFloatSupport()) {
                try {
                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                        + "  VALUES (?)");
                    Reader r = new StringReader ("Maplewood");
                    JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long)9L);
                    ps.executeUpdate(); ps.close ();
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
            }
        }


        /**
        setNCharacterStream() - Set a DECFLOAT parameter.
        **/
            public void Var041()
            {
                if (checkJdbc40 ()) {
                if (checkDecFloatSupport()) {
                    try {
                        PreparedStatement ps = connection_.prepareStatement (
                            "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                            + "  VALUES (?)");
                        Reader r = new StringReader ("Maplewood");
                        JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long)9L);
                        ps.executeUpdate(); ps.close ();
                        failed ("Didn't throw SQLException");
                    }
                    catch (Exception e) {
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    }
                }
                }
            }


            public void Var042() { notApplicable(); }
            public void Var043() { notApplicable(); }


    /**
    setNCharacterStream() - Set an XML  parameter.
    **/
            public void setXML(String tablename, String data, String expected) {
	       String added = " -- added by native driver 08/21/2009";
                if (checkJdbc40()) {
                    if (checkXmlSupport ()) {
                        try {
                            statement_.executeUpdate ("DELETE FROM " + tablename);

                            PreparedStatement ps = connection_.prepareStatement (
                                                                                 "INSERT INTO " + tablename
                                                                                 + "  VALUES (?)");



                            Reader r = new StringReader (data);
                            JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) data.length());


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
                                             JDTestUtilities.getMixedString(expected) +
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

                            Reader r = new StringReader (data);
                            JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) data.length());

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
                                failed (e, "Unexpected Exception.  Expected "+expectedException+
					added);
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




	public void setInvalid(String column, String inputValue, String exceptionInfo)  {
	    StringBuffer sb = new StringBuffer(); 
	    try {
		statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement(
								    "INSERT INTO " + JDPSTest.PSTEST_SET + " ("+column+") VALUES (?)");


		Reader r = new StringReader (inputValue);
		JDReflectionUtil.callMethod_V(ps,"setNCharacterStream",1, r, (long) inputValue.length());

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




