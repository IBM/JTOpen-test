///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetByte.java
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
 // File Name:    JDPSSetByte.java
 //
 // Classes:      JDPSSetByte
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDPSetByte.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setByte()
</ul>
**/
public class JDPSSetByte
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetByte (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetByte",
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
setByte() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.close ();
            ps.setByte (1, (byte) 5);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setByte() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
            ps.setByte (100, (byte) 3);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setByte() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
            ps.setByte (0, (byte) -5);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setByte() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
            ps.setByte (-1, (byte) 4);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setByte() - Should work with a valid parameter index
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
            ps.setByte (2, (byte) 42);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getByte (1);
            rs.close ();

            assertCondition (check == 42);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setByte() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var006()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC permits setting of an output parameter");
        return; 
      } 
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            ps.setByte (2, (byte) 22);
            ps.setByte (1,(byte)1); 
            ps.setByte (3, (byte) 2); 
            // Note: jcc doesn't check until the execute. 
            ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }






  public void testSetSuccessful(String columnName, int inputByte,
      String outputString) {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
      ps.setByte(1, (byte) inputByte);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT "+columnName+" FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      String check = rs.getString(1);
      rs.close();

      assertCondition(outputString.equals(check),
          "Got '" + check + "' sb '" + outputString+"'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setByte() - Set a SMALLINT parameter.
   **/
  public void Var007() {
    testSetSuccessful("C_SMALLINT", -22, "-22");
  }

  /**
   * setByte() - Set an INTEGER parameter.
   **/
  public void Var008() {
    testSetSuccessful("C_INTEGER", -101, "-101");
  }

  /**
   * setByte() - Set an REAL parameter.
   **/
  public void Var009() {
    testSetSuccessful("C_REAL", 119, "119.0");
  }

  /**
   * setByte() - Set an FLOAT parameter.
   **/
  public void Var010() {
    testSetSuccessful("C_FLOAT", 51, "51.0");
  }

/**
setByte() - Set an DOUBLE parameter.
**/
    public void Var011()
    {
      testSetSuccessful("C_DOUBLE", 11, "11.0");
      }



/**
setByte() - Set an DECIMAL parameter.
**/
    public void Var012()
    {
      testSetSuccessful("C_DECIMAL_105", -100, "-100.00000");
    }



/**
setByte() - Set an NUMERIC parameter.
**/
    public void Var013()
    {
      testSetSuccessful("C_DECIMAL_50", 117, "117");
    }



  /**
   * setByte() - Set an CHAR(50) parameter.
   **/
  public void Var014() {
    testSetSuccessful("C_CHAR_50", 87,
        "87                                                ");
  }

/**
setByte() - Set an CHAR(1) parameter.
**/
    public void Var015()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_1) VALUES (?)");
            ps.setByte (1, (byte) 87);
            ps.executeUpdate(); 
            
            ResultSet rs = statement_.executeQuery("SELECT C_CHAR_1 FROM "+JDPSTest.PSTEST_SET); 
            rs.next(); 
            String value = rs.getString(1); 
            failed ("Didn't throw SQLException but set byte for CHAR parameter got back "+value);
        }
        catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          } else { 
             assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
          }
            
        }
    }



/**
setByte() - Set an VARCHAR parameter.
**/
  public void Var016() {
    testSetSuccessful("C_VARCHAR_50", 77, "77");
  }


/**
setByte() - Set a CLOB parameter.
**/
    public void Var017()     {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_CLOB) VALUES (?)");
                ps.setByte (1, (byte) -2);
                ps.execute(); 
                ps.close ();
                
                ResultSet rs = statement_.executeQuery("SELECT C_CLOB FROM "+JDPSTest.PSTEST_SET); 
                rs.next(); 
                String value = rs.getString(1); 

                failed ("Didn't throw SQLException for setting CLOB value "+value);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setByte() - Set a DBCLOB parameter.
**/
    public void Var018()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DBCLOB) VALUES (?)");
                ps.setByte (1, (byte) -5);
                ps.execute(); 
                ps.close ();
                ResultSet rs = statement_.executeQuery("SELECT C_DBCLOB FROM "+JDPSTest.PSTEST_SET); 
                rs.next(); 
                String value = rs.getString(1); 

                failed ("Didn't throw SQLException for setting DBCLOB value  "+value);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setByte() - Set a BINARY parameter.
**/
    public void Var019()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BINARY_20) VALUES (?)");
            ps.setByte (1, (byte) 4);
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }




/**
setByte() - Set a VARBINARY parameter.
**/
    public void Var020()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARBINARY_20) VALUES (?)");
            ps.setByte (1, (byte) 19);
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setByte() - Set a BLOB parameter.
**/
    public void Var021()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BLOB) VALUES (?)");
            ps.setByte (1, (byte) 4);
            ps.execute(); 
            ps.close ();

            ResultSet rs = statement_.executeQuery("SELECT C_BLOB FROM "+JDPSTest.PSTEST_SET); 
            rs.next(); 
            String value = rs.getString(1); 

            failed ("Didn't throw SQLException for setting BLOB "+value);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setByte() - Set a DATE parameter.
**/
    public void Var022()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DATE) VALUES (?)");
            ps.setByte (1, (byte) 0);
            ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setByte() - Set a TIME parameter.
**/
    public void Var023()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIME) VALUES (?)");
            ps.setByte (1, (byte) 3);
            ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setByte() - Set a TIMESTAMP parameter.
**/
    public void Var024()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIMESTAMP) VALUES (?)");
            ps.setByte (1, (byte) 33);
            ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
setByte() - Set a DATALINK parameter.
**/
    public void Var025()
    {
        if ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
            notApplicable ("Native driver pre-JDBC 3.0");
            return;
        }
        if (checkDatalinkSupport ()) {
            try {
              statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

              PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DATALINK) VALUES (?)");
                ps.setByte (1, (byte) 75);
                ps.execute(); 
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String value = rs.getString (1);
                rs.close ();

                failed ("Didn't throw SQLException but DATALINK contained "+value);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setByte() - Set a DISTINCT parameter.
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
                ps.setByte (1, (byte) -7);
                ps.executeUpdate ();
                ps.close ();
    
                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                short check = rs.getByte (1);
                rs.close ();
    
                assertCondition (check == -7);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    setByte() - Set an BIGINT parameter.
    **/
        public void Var027()
        {
            if (checkBigintSupport()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_BIGINT) VALUES (?)");
                ps.setByte (1, (byte) 98);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                long check = rs.getLong (1);
                rs.close ();

                assertCondition (check == 98);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
            }
        }
        /**
        setByte() - Set a DECFLOAT16 parameter.
        **/
        public void Var028()
        {
        
          if (checkDecFloatSupport()) {
            try {
              statement_.executeUpdate("DELETE FROM "+ JDPSTest.PSTEST_SETDFP16);
              PreparedStatement ps = connection_.prepareStatement (
                  "INSERT INTO " + JDPSTest.PSTEST_SETDFP16               
                  + "  VALUES (?)");
              ps.setByte (1, (byte) 33);
              ps.execute(); 
              ps.close ();
              
              ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
              rs.next ();
              String  value = rs.getString (1);
              rs.close ();

              assertCondition(value.equals("33"), "Expected 33 but got "+value); 
            }
            catch (Exception e) {
              failed (e, "Unexpected Exception -- JCC running to Z/OS returns 33");
            }
          }
        }

        /**
         setByte() - Set a DECFLOAT34 parameter.
         **/
        public void Var029()
        {
          if (checkDecFloatSupport()) { 
            try {
              statement_.executeUpdate("DELETE FROM "+ JDPSTest.PSTEST_SETDFP34);
              PreparedStatement ps = connection_.prepareStatement (
                  "INSERT INTO " + JDPSTest.PSTEST_SETDFP34               
                  + "  VALUES (?)");
              ps.setByte (1, (byte) 33);
              ps.execute(); 
              ps.close ();

              ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
              rs.next ();
              String  value = rs.getString (1);
              rs.close ();
              assertCondition(value.equals("33"), "Expected 33 but got "+value); 
            }
            catch (Exception e) {
              failed (e, "Unexpected Exception -- JCC running to Z/OS:  should return 33");
            }
          }
        }


  /**
   * setByte() - Set an XML parameter.
   **/
  public void Var030() {
    if (checkXmlSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETXML + " VALUES (?)");
        try {
          ps.setByte(1, (byte) 33);
          ps.execute();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(e, "Unexpected exception");
      }
    }
  }
  
  
  
  /**
   * setByte() - Set an BOOLEAN parameter.
   **/
  public void Var031() {
    if (checkBooleanSupport()) { 
      testSetSuccessful("C_BOOLEAN", 1, "1");
    }
  }

  /**
   * setByte() - Set an BOOLEAN parameter.
   **/
  public void Var032() {
    if (checkBooleanSupport()) { 
      testSetSuccessful("C_BOOLEAN", 0, "0");
    }
  }
  /**
   * setByte() - Set an BOOLEAN parameter.
   **/
  public void Var033() {
    if (checkBooleanSupport()) { 
      testSetSuccessful("C_BOOLEAN", 99, "1");
    }
  }

}

