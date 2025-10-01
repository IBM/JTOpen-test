///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetShort.java
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
 // File Name:    JDPSSetShort.java
 //
 // Classes:      JDPSSetShort
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

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDPSetShort.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setShort()
</ul>
**/
public class JDPSSetShort
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetShort";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Private data.
    private Connection          connection_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetShort (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetShort",
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
setShort() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.close ();
            ps.setShort (1, (short) 5);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setShort() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setShort (100, (short) 333);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setShort() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setShort (0, (short) -85);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setShort() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setShort (-1, (short) 94);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setShort() - Should work with a valid parameter index
greater than 1.
**/
    public void Var005()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_KEY, C_SMALLINT) VALUES (?, ?)");
            ps.setString (1, "Test");
            ps.setShort (2, (short) 442);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == 442);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setShort() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var006()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            ps.setShort (2, (short) 222);
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setShort() - Set a SMALLINT parameter.
**/
    public void Var007()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.setShort (1, (short) -2232);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == -2232);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setShort() - Set an INTEGER parameter.
**/
    public void Var008()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            ps.setShort (1, (short) -1061);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == -1061);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setShort() - Set an REAL parameter.
**/
    public void Var009()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_REAL) VALUES (?)");
            ps.setShort (1, (short) 179);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == 179);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setShort() - Set an FLOAT parameter.
**/
    public void Var010()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_FLOAT) VALUES (?)");
            ps.setShort (1, (short) 1);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check ==  1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setShort() - Set an DOUBLE parameter.
**/
    public void Var011()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DOUBLE) VALUES (?)");
            ps.setShort (1, (short) 18);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == 18);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setShort() - Set an DECIMAL parameter.
**/
    public void Var012()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setShort (1, (short) -1001);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == -1001);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setShort() - Set an NUMERIC parameter.
**/
    public void Var013()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_50) VALUES (?)");
            ps.setShort (1, (short) 1777);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == 1777);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setShort() - Set an CHAR(50) parameter.
**/
    public void Var014()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_50) VALUES (?)");
            ps.setShort (1, (short) 187);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == 187);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setShort() - Set an CHAR(1) parameter.
**/
    public void Var015()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_1) VALUES (?)");
            ps.setShort (1, (short) -83);
	    ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
        }
    }



/**
setShort() - Set an VARCHAR parameter.
**/
    public void Var016()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            ps.setShort (1, (short) -767);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == -767);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setShort() - Set a CLOB parameter.
**/
    public void Var017()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_CLOB) VALUES (?)");
                ps.setShort (1, (short) -22);
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
setShort() - Set a DBCLOB parameter.
**/
    public void Var018()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DBCLOB) VALUES (?)");
                ps.setShort (1, (short) -5);
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
setShort() - Set a BINARY parameter.
**/
    public void Var019()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BINARY_20) VALUES (?)");
            ps.setShort (1, (short) 4);
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }




/**
setShort() - Set a VARBINARY parameter.
**/
    public void Var020()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARBINARY_20) VALUES (?)");
            ps.setShort (1, (short) 19);
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setShort() - Set a BLOB parameter.
**/
    public void Var021()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BLOB) VALUES (?)");
            ps.setShort (1, (short) 4);
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setShort() - Set a DATE parameter.
**/
    public void Var022()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DATE) VALUES (?)");
            ps.setShort (1, (short) 0);
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setShort() - Set a TIME parameter.
**/
    public void Var023()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIME) VALUES (?)");
            ps.setShort (1, (short) 3);
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setShort() - Set a TIMESTAMP parameter.
**/
    public void Var024()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIMESTAMP) VALUES (?)");
            ps.setShort (1, (short) -3443);
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
setShort() - Set a DATALINK parameter.
**/
    public void Var025()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DATALINK) VALUES (?)");
                ps.setShort (1, (short) 75);
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
setShort() - Set a DISTINCT parameter.
**/
    // @C0C
    public void Var026()
    {
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DISTINCT) VALUES (?)");
                ps.setShort (1, (short) -17);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                short check = rs.getShort (1);
                rs.close ();

                assertCondition (check == -17);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setShort() - Set a BIGINT parameter.
**/
    public void Var027()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setShort (1, (short) 4031);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == 4031);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
// @D1a
setShort() - Set an CHAR(1) parameter.
**/
    public void Var028()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.setString (1, "123456789012345");
	    ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            // In V5R5 native will throw a datatype mismatch
            // In V5R5 toolbox also will throw a datatype mismatch
            if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE || ((isToolboxDriver() || getDriver() == JDTestDriver.DRIVER_NATIVE) && true)) {
                boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
                if (success) { 
                    assertCondition(true); 
                } else { 
                    failed(e); 
                } 

            } else { 
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }


/**
// @D1a
setShort() - Set a SMALLINT parameter.
**/
    public void Var029()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.setString(1, "2232.2");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == 2232);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setShort() - Set an DECFLOAT16 parameter.
    **/
    public void Var030()
    {
      if (checkDecFloatSupport()) {
        try {
          statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);
          
          PreparedStatement ps = connection_.prepareStatement (
              "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
              + "  VALUES (?)");
          ps.setShort (1, (short) -100);
          ps.executeUpdate ();
          ps.close ();
          
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
          rs.next ();
          int check = rs.getInt (1);
          rs.close ();
          
          assertCondition (check == -100);
        }
        catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }

        /**
        setShort() - Set an DECFLOAT34 parameter.
        **/
    public void Var031()
    {
      if (checkDecFloatSupport()) { 
        try {
          statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);
          
          PreparedStatement ps = connection_.prepareStatement (
              "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
              + "  VALUES (?)");
          ps.setFloat (1, (short) -100);
          ps.executeUpdate ();
          ps.close ();
          
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
          rs.next ();
          int check = rs.getInt (1);
          rs.close ();
          
          assertCondition (check == -100);
        }
        catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
      
      
    }




/**
setShort() - Set an XML parameter.
**/
    public void Var032()
    {
        if (checkXmlSupport ()) {
	    try {
		PreparedStatement ps =
		  connection_.prepareStatement (
						"INSERT INTO " +
						JDPSTest.PSTEST_SETXML
						+ " VALUES (?)");
		try { 
		    ps.setShort (1, (short) 75);
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
     * setInt() - Set an parameter for a column of a specified type.
     **/
    public void testSetShort(String columnName, short value, String outputValue) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
        ps.setShort(1, value);
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

    public void Var033() {
      if (checkBooleanSupport()) {
        testSetShort("C_BOOLEAN", (short) 1, "1");
      }
    }

    public void Var034() {
      if (checkBooleanSupport()) {
        testSetShort("C_BOOLEAN", (short)0, "0");
      }
    }

    public void Var035() {
      if (checkBooleanSupport()) {
        testSetShort("C_BOOLEAN", (short)101, "1");
      }
    }

    public void Var036() {
      if (checkBooleanSupport()) {
        testSetShort("C_BOOLEAN", (short)-1, "1");
      }
    }
    
    public void Var037() {
      if (checkBooleanSupport()) {
        testSetShort("C_BOOLEAN", Short.MAX_VALUE, "1");
      }
    }

    public void Var038() {
      if (checkBooleanSupport()) {
        testSetShort("C_BOOLEAN", Short.MIN_VALUE, "1");
      }
    }






}

