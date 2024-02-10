///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetBoolean.java
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
 // File Name:    JDPSSetBoolean.java
 //
 // Classes:      JDPSSetBoolean
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.PS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDPSetBoolean.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setBoolean()
</ul>
**/
public class JDPSSetBoolean
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetBoolean (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetBoolean",
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
        
        ;
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
setBoolean() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            ps.close ();
            ps.setBoolean (1, true);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBoolean() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
            ps.setBoolean (100, false);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBoolean() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
            ps.setBoolean (0, true);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBoolean() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
            ps.setBoolean (-1, false);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBoolean() - Should work with a valid parameter index
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
            ps.setBoolean (2, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Should throw exception when the parameter is
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
            
            ps.setBoolean (2, false);
            ps.setByte    (1, (byte)1); 
            ps.setByte    (3, (byte) 2); 
            // Note: jcc doesn't check until the execute. 
            ps.execute(); 
            ps.executeUpdate(); ps.close ();

            failed ("Didn't throw SQLException when setting an output only parameter");
        }
        catch (Exception e) {
            // e.printStackTrace(System.out); 
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBoolean() - Set a SMALLINT parameter, specifying true.
**/
    public void Var007()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set a SMALLINT parameter, specifying false.
**/
    public void Var008()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an INTEGER parameter, specifying true.
**/
    public void Var009()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an INTEGER parameter, specifying false.
**/
    public void Var010()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an REAL parameter, specifying true.
**/
    public void Var011()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_REAL) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an REAL parameter, specifying false.
**/
    public void Var012()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_REAL) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an FLOAT parameter, specifying true.
**/
    public void Var013()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_FLOAT) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an FLOAT parameter, specifying false.
**/
    public void Var014()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_FLOAT) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an DOUBLE parameter, specifying true.
**/
    public void Var015()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DOUBLE) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an DOUBLE parameter, specifying false.
**/
    public void Var016()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DOUBLE) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an DECIMAL parameter, specifying true.
**/
    public void Var017()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an DECIMAL parameter, specifying false.
**/
    public void Var018()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_50) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an NUMERIC parameter, specifying true.
**/
    public void Var019()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_50) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an NUMERIC parameter, specifying false.
**/
    public void Var020()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_105) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an CHAR parameter, specifying true.
**/
    public void Var021()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_1) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an CHAR parameter, specifying false.
**/
    public void Var022()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_1) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an VARCHAR parameter, specifying true.
**/
    public void Var023()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set an VARCHAR parameter, specifying false.
**/
    public void Var024()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBoolean() - Set a CLOB parameter.
**/
    public void Var025()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_CLOB) VALUES (?)");
                ps.setBoolean (1, true);
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
setBoolean() - Set a DBCLOB parameter.
**/
    public void Var026()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DBCLOB) VALUES (?)");
                ps.setBoolean (1, true);
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
setBoolean() - Set a BINARY parameter.
**/
    public void Var027()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BINARY_20) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }




/**
setBoolean() - Set a VARBINARY parameter.
**/
    public void Var028()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARBINARY_20) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setBoolean() - Set a BLOB parameter.
**/
    public void Var029()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BLOB) VALUES (?)");
            ps.setBoolean (1, true);
            ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setBoolean() - Set a DATE parameter.
**/
    public void Var030()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DATE) VALUES (?)");
            ps.setBoolean (1, true);
            ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBoolean() - Set a TIME parameter.
**/
    public void Var031()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIME) VALUES (?)");
            ps.setBoolean (1, true);
            ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBoolean() - Set a TIMESTAMP parameter.
**/
    public void Var032()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIMESTAMP) VALUES (?)");
            ps.setBoolean (1, true);
            ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
setBoolean() - Set a DATALINK parameter.
**/
    public void Var033()
    {
        if ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
            notApplicable ("Native driver pre-JDBC 3.0");
            return;
        }
        if (checkDatalinkSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DATALINK) VALUES (?)");
                ps.setBoolean (1, true);
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
setBoolean() - Set a DISTINCT parameter.
**/
    // @C0C
    public void Var034()
    {
        if (checkLobSupport ()) {         
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DISTINCT) VALUES (?)");
                ps.setBoolean (1, true);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                boolean check = rs.getBoolean (1);
                rs.close ();

                assertCondition (check == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBoolean() - Set a BIGINT parameter, specifying true.
**/
    public void Var035()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
setBoolean() - Set a BIGINT parameter, specifying false.
**/
    public void Var036()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


    /**
    setBoolean() - Set a DECFLOAT parameter, specifying true.
    **/
        public void Var037()
        {
          if (checkDecFloatSupport()) { 
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              notApplicable("JCC does not support setting DECFLOAT from boolean");
              return; 
            } 

            try {
              statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);
              
              PreparedStatement ps = connection_.prepareStatement (
                  "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                  + "  VALUES (?)");
              ps.setBoolean (1, true);
              ps.executeUpdate ();
              ps.close ();
              
              ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
              rs.next ();
              boolean check = rs.getBoolean (1);
              rs.close ();
              
              assertCondition (check == true);
            }
            catch (Exception e) {
              failed (e, "Unexpected Exception");
            }
          }
        }



    /**
    setBoolean() - Set an DECFLOAT parameter, specifying false.
    **/
        public void Var038()
        {
          if (checkDecFloatSupport()) { 
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              notApplicable("JCC does not support setting DECFLOAT from boolean");
              return; 
            } 
            try {
              statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);
              
              PreparedStatement ps = connection_.prepareStatement (
                  "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                  + "  VALUES (?)");
              ps.setBoolean (1, false);
              ps.executeUpdate ();
              ps.close ();
              
              ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
              rs.next ();
              boolean check = rs.getBoolean (1);
              rs.close ();
              
              assertCondition (check == false);
            }
            catch (Exception e) {
              failed (e, "Unexpected Exception");
            }
          }
          
        }

        /**
        setBoolean() - Set a DECFLOAT parameter, specifying true.
        **/
            public void Var039()
            {
              if (checkDecFloatSupport()) { 
                if (getDriver() == JDTestDriver.DRIVER_JCC) {
                  notApplicable("JCC does not support setting DECFLOAT from boolean");
                  return; 
                } 
                try {
                  statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);
                  
                  PreparedStatement ps = connection_.prepareStatement (
                      "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                      + "  VALUES (?)");
                  ps.setBoolean (1, true);
                  ps.executeUpdate ();
                  ps.close ();
                  
                  ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
                  rs.next ();
                  boolean check = rs.getBoolean (1);
                  rs.close ();
                  
                  assertCondition (check == true);
                }
                catch (Exception e) {
                  failed (e, "Unexpected Exception");
                }
              }
            }



        /**
        setBoolean() - Set an DECFLOAT parameter, specifying false.
        **/
            public void Var040() 
            {
              if (checkDecFloatSupport()) { 
                if (getDriver() == JDTestDriver.DRIVER_JCC) {
                  notApplicable("JCC does not support setting DECFLOAT from boolean");
                  return; 
                } 
                try {
                  statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);
                  
                  PreparedStatement ps = connection_.prepareStatement (
                      "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                      + "  VALUES (?)");
                  ps.setBoolean (1, false);
                  ps.executeUpdate ();
                  ps.close ();
                  
                  ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
                  rs.next ();
                  boolean check = rs.getBoolean (1);
                  rs.close ();
                  
                  assertCondition (check == false);
                }
                catch (Exception e) {
                  failed (e, "Unexpected Exception");
                }
              }
              
            }


/**
setBoolean() - Set an XML  parameter.
**/
    public void Var041()
    {
        if (checkXmlSupport ()) {
	    try { 
		PreparedStatement ps =
		  connection_.prepareStatement( 
					       "INSERT INTO " +
						JDPSTest.PSTEST_SETXML
						+ " VALUES (?)");
		try {
		    ps.setBoolean (1, true);
		    ps.execute(); 
		    ps.close ();
		    failed ("Didn't throw SQLException");
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    } catch (Exception e) {
		failed(e, "unexpected exception"); 
	    } 
        }
    }


/**
setBoolean() - Should work on boolean column.
**/
    public void Var042()
    {
	if (checkBooleanSupport()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_KEY, C_BOOLEAN) VALUES (?, ?)");
		ps.setString (1, "Test");
		ps.setBoolean (2, false);
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_BOOLEAN FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		boolean check = rs.getBoolean (1);
		rs.close ();

		assertCondition (check == false);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}


    }


    public void Var043()
    {
	if (checkBooleanSupport()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_KEY, C_BOOLEAN) VALUES (?, ?)");
		ps.setString (1, "Test");
		ps.setBoolean (2, true);
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_BOOLEAN FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		boolean check = rs.getBoolean (1);
		rs.close ();

		assertCondition (check == true);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}


    }
}


