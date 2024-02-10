///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetObject4.java
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
// File Name:    JDPSSetObject4.java
//
// Classes:      JDPSSetObject4
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JDLobTest.JDTestBlob;
import test.JDLobTest.JDTestClob;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDPSSetObject4.  This tests the following method
of the JDBC PreparedStatement class (4 parameters):

<ul>
<li>setObject(int,Object,int,int)
</ul>
**/
public class JDPSSetObject4
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private Statement           statement_;

    private Connection          connectionCommaSeparator_;


    /**
    Constructor.
    **/
    public JDPSSetObject4 (AS400 systemObject,
                           Hashtable namesAndVars,
                           int runMode,
                           FileOutputStream fileOutputStream,
                           
                           String password)
    {
        super (systemObject, "JDPSSetObject4",
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
        connectionCommaSeparator_ = testDriver_.getConnection (url+";decimal separator=,");
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
        connectionCommaSeparator_.close ();
    }



    /**
    setObject() - Should throw exception when the prepared
    statement is closed.
    **/
    public void Var001()
    {
        try
        {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.close ();
            ps.setObject (1, new BigDecimal (2.3), Types.NUMERIC, 1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setObject() - Should throw exception when an invalid index is
    specified.
    **/
    public void Var002()
    {
        try
        {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setObject (100, new Integer (4), Types.INTEGER, 0);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setObject() - Should throw exception when index is 0.
    **/
    public void Var003()
    {
        try
        {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setObject (0, "Hi Mom", Types.VARCHAR, 0);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setObject() - Should throw exception when index is -1.
    **/
    public void Var004()
    {
        try
        {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setObject (-1, "Yo Dad", Types.VARCHAR, 0);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setObject() - Should work with a valid parameter index
    greater than 1.
    **/
    public void Var005()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
            ps.setString (1, "Test");
            ps.setObject (2, new BigDecimal ("4.3"), Types.NUMERIC, 1);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 1);
            rs.close ();

            assertCondition (check.doubleValue() == 4.3);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Should set to SQL NULL when the object is null.
    **/
    public void Var006()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, null, Types.NUMERIC, 1);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition ((check == null) && (wn == true));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Should throw exception when the type is invalid.
    **/
    public void Var007()
    {
        if (isToolboxDriver()) {
            //toolbox     
            notApplicable("Toolbox does not use col types");
            return;
        }
        try
        {
	    if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
		notApplicable("testcase didn't work in earlier releases ");
	    } else {
		notApplicable("Native doesn't use col types ");
/* 
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
		ps.setObject (1, new Integer (4), 4848484, 0);
		failed ("Didn't throw SQLException");
*/ 
	    }
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setObject() - Should throw exception when the scale is invalid.
    **/
    public void Var008()
    {
        try
        {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setObject (1, new Integer (4), Types.INTEGER, -1);
            failed ("Didn't throw SQLException for invalid scale");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setObject() - Should throw exception when the parameter is
    not an input parameter.
    **/
    public void Var009()
    {
        try
        {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            ps.setObject (2, new Integer (3), Types.INTEGER, 0);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setObject() - Should throw exception when the parameter is
    not anything close to being a JDBC-style type.
    **/
    public void Var010()
    {
        try
        {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_SMALLINT) VALUES (?)");
            ps.setObject (1, new Hashtable (), Types.SMALLINT, 0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setObject() - Set a SMALLINT parameter.
    **/
    public void Var011()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_SMALLINT) VALUES (?)");
            ps.setObject (1, new Short ((short) -33), Types.SMALLINT, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == -33);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set a SMALLINT parameter with a nonzero scale specified.
    **/
    public void Var012()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_SMALLINT) VALUES (?)");
            ps.setObject (1, new Short ((short) 76), Types.SMALLINT, 2);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == 76);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an INTEGER parameter.
    **/
    public void Var013()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER) VALUES (?)");
            ps.setObject (1, new Integer (-595), Types.INTEGER, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            int check = rs.getInt (1);
            rs.close ();

            assertCondition (check == -595);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an INTEGER parameter with a nonzero scale.
    **/
    public void Var014()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER) VALUES (?)");
            ps.setObject (1, new Integer (-595), Types.INTEGER, 4);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            int check = rs.getInt (1);
            rs.close ();

            assertCondition (check == -595);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an REAL parameter.
    **/
    public void Var015()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_REAL) VALUES (?)");
            ps.setObject (1, new Float (-4.385), Types.REAL, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -4.385f);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an REAL parameter with nonzero scale.
    **/
    public void Var016()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_REAL) VALUES (?)");
            ps.setObject (1, new Float (-4.3235), Types.REAL, 3);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -4.3235f);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an FLOAT parameter.
    **/
    public void Var017()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_FLOAT) VALUES (?)");
            ps.setObject (1, new Float (-3.42), Types.DOUBLE, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -3.42f);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an FLOAT parameter with a nonzero scale.
    **/
    public void Var018()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_FLOAT) VALUES (?)");
            ps.setObject (1, new Float (3.43212), Types.DOUBLE, 3);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 3.43212f);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an DOUBLE parameter.
    **/
    public void Var019()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DOUBLE) VALUES (?)");
            ps.setObject (1, new Double (-314.159), Types.DOUBLE, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -314.159);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an DOUBLE parameter with a nonzero scale.
    **/
    public void Var020()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DOUBLE) VALUES (?)");
            ps.setObject (1, new Double (3159.343), Types.DOUBLE, 2);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == 3159.343);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an DECIMAL parameter with scale 0.
    **/
    public void Var021()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("32.234"), Types.DECIMAL, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 3);
            rs.close ();

            assertCondition (check.doubleValue () == 32, "got "+check+" sb 32");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an DECIMAL parameter with scale less than the number.
    **/
    public void Var022()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("-32.2319"), Types.DECIMAL, 2);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 2);
            rs.close ();

            assertCondition (check.doubleValue () == -32.23);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an DECIMAL parameter with scale equal to the number.
    **/
    public void Var023()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("822.3187"), Types.DECIMAL, 4);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 4);
            rs.close ();

            assertCondition (check.doubleValue () == 822.3187);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an DECIMAL parameter with scale greater than the number.
    **/
    public void Var024()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("-12.87"), Types.DECIMAL, 4);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 2);
            rs.close ();

            assertCondition (check.doubleValue () == -12.87);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an NUMERIC parameter with scale 0.
    **/
    public void Var025()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("-432.34"), Types.NUMERIC, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (check.doubleValue () == -432);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an NUMERIC parameter with scale less than the number.
    **/
    public void Var026()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("52.219"), Types.NUMERIC, 1);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 1);
            rs.close ();

            assertCondition (check.doubleValue () == 52.2);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an NUMERIC parameter with scale equal to the number.
    **/
    public void Var027()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("82.31387"), Types.NUMERIC, 5);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 5);
            rs.close ();

            assertCondition (check.doubleValue () == 82.31387);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an NUMERIC parameter with scale greater than the number.
    **/
    public void Var028()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("32.87"), Types.NUMERIC, 4);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 2);
            rs.close ();

            assertCondition (check.doubleValue () == 32.87);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set a CHAR(50) parameter.
    **/
    public void Var029()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_50) VALUES (?)");
            ps.setObject (1, "Dell Rapids", Types.CHAR, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Dell Rapids                                       "));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set a CHAR(50) parameter with a nonzero scale.
    **/
    public void Var030()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_50) VALUES (?)");
            ps.setObject (1, "Harrisburg", Types.CHAR, 4);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Harrisburg                                        "));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setObject() - Set an VARCHAR parameter.
    **/
    public void Var031()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setObject (1, "Aberdeen", Types.VARCHAR, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Aberdeen"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set an VARCHAR parameter with a nonzero scale.
    **/
    public void Var032()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setObject (1, "Desmet", Types.VARCHAR, 4);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Desmet"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set a CLOB parameter.
    **/
    public void Var033()
    {
        if(checkJdbc20 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                    PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_CLOB) VALUES (?)");
                    ps.setObject (1, new JDLobTest.JDTestClob ("Milbank"), Types.CLOB, 0);
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
                    rs.next ();
                    Clob check = rs.getClob (1);

                    assertCondition (check.getSubString (1, (int) check.length ()).equals ("Milbank")); // @D1C
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }




    /**
    setObject() - Set a CLOB parameter with a nonzero scale.
    **/
    public void Var034()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport())
            {
                try
                {
                    statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

                    PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");
                    ps.setObject(1, new JDLobTest.JDTestClob("Garretson"), Types.CLOB, 4);
                    ps.executeUpdate();
                    ps.close();

                    ResultSet rs = statement_.executeQuery("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
                    rs.next();
                    Clob check = rs.getClob(1);

                    String string = check.getSubString(1, (int)check.length());
                    if(string.equals("Garretson"))
                        succeeded();
                    else
                        failed("Strings did not match. Garretson != " + string);
                }
                catch(Exception e)
                {
                    failed(e, "Unexpected Exception");
                }
            }
        }
    }




    /**
    setObject() - Set a DBCLOB parameter.
    **/
    public void Var035()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport())
            {
                try
                {
                    statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

                    PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");
                    ps.setObject(1, new JDLobTest.JDTestClob("Tea"), Types.CLOB, 0);
                    ps.executeUpdate();
                    ps.close();

                    ResultSet rs = statement_.executeQuery("SELECT C_DBCLOB FROM " + JDPSTest.PSTEST_SET);
                    rs.next();
                    Clob check = rs.getClob(1);

                    String string = check.getSubString(1, (int)check.length());
                    if(string.equals("Tea"))
                        succeeded();
                    else
                        failed("Strings did not match. Tea != " + string);

                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    setObject() - Set a DBCLOB parameter with a nonzero scale.
    **/
    public void Var036()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport())
            {
                try {
                    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);
        
                    PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");
                    ps.setObject(1, new JDLobTest.JDTestClob("Lennox"), Types.CLOB, 20);
                    ps.executeUpdate();
                    ps.close();
        
                    ResultSet rs = statement_.executeQuery("SELECT C_DBCLOB FROM " + JDPSTest.PSTEST_SET);
                    rs.next();
                    Clob check = rs.getClob(1);
        
                    String string = check.getSubString(1, (int)check.length());
                    if(string.equals("Lennox"))
                        succeeded();
                    else
                        failed("Strings did not match. Lennox != " + string);

                }
                catch (Exception e) {
                    failed(e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    setObject() - Set a BINARY parameter.
    **/
    public void Var037()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BINARY_20) VALUES (?)");
            byte[] b = { (byte) 32, (byte) 9, (byte) -1, (byte) -11, (byte) 12};
            ps.setObject (1, b, Types.BINARY, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            byte[] check = rs.getBytes (1);
            rs.close ();

            byte[] b2 = new byte[20];
            System.arraycopy (b, 0, b2, 0, b.length);
            assertCondition (isEqual (check, b2));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }




    /**
    setObject() - Set a BINARY parameter with a nonzero scale.
    **/
    public void Var038()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BINARY_20) VALUES (?)");
            byte[] b = { (byte) 9, (byte) -1, (byte) -11, (byte) 12};
            ps.setObject (1, b, Types.BINARY, 7);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            byte[] check = rs.getBytes (1);
            rs.close ();

            byte[] b2 = new byte[20];
            System.arraycopy (b, 0, b2, 0, b.length);
            assertCondition (isEqual (check, b2));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }




    /**
    setObject() - Set a VARBINARY parameter.
    **/
    public void Var039()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = { (byte) -13, (byte) 32, (byte) 0, (byte) -1, (byte) -11, (byte) 99};
            ps.setObject (1, b, Types.VARBINARY, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            byte[] check = rs.getBytes (1);
            rs.close ();

            assertCondition (isEqual (check, b));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }




    /**
    setObject() - Set a VARBINARY parameter with a nonzero scale.
    **/
    public void Var040()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = { (byte) -13, (byte) 32, (byte) 100, (byte) -1, (byte) -11, (byte) 99};
            ps.setObject (1, b, Types.VARBINARY, 3);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            byte[] check = rs.getBytes (1);
            rs.close ();

            assertCondition (isEqual (check, b));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }




    /**
    setObject() - Set a BLOB parameter.
    **/
    public void Var041()
    {
        if(checkJdbc20 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                    PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_BLOB) VALUES (?)");
                    byte[] b = new byte[] { (byte) 12, (byte) 94, (byte) -1, (byte) 0};
                    ps.setObject (1, new JDLobTest.JDTestBlob (b), Types.BLOB, 0);
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
                    rs.next ();
                    Blob check = rs.getBlob (1);
                    // rs.close ();                                                            // @F1D

                    assertCondition (isEqual (check.getBytes (1, (int) check.length ()), b));  // @D1C
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    setObject() - Set a BLOB parameter with a nonzero scale.
    **/
    public void Var042()
    {
        if(checkJdbc20 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                    PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_BLOB) VALUES (?)");
                    byte[] b = new byte[] { (byte) 94, (byte) -1, (byte) 0};
                    ps.setObject (1, new JDLobTest.JDTestBlob (b), Types.BLOB, 4);
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
                    rs.next ();
                    Blob check = rs.getBlob (1);
                    // rs.close ();                                                            // @F1D

                    assertCondition (isEqual (check.getBytes (1, (int) check.length ()), b));  // @D1C
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    setObject() - Set a DATE parameter.
    **/
    public void Var043()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DATE) VALUES (?)");
            Date d = new Date (3423499);
            ps.setObject (1, d, Types.DATE, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Date check = rs.getDate (1);
            rs.close ();

            assertCondition (d.toString ().equals (check.toString ()));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set a DATE parameter with nonzero scale.
    **/
    public void Var044()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DATE) VALUES (?)");
            Date d = new Date (142333499);
            ps.setObject (1, d, Types.DATE, 4);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Date check = rs.getDate (1);
            rs.close ();

            assertCondition (d.toString ().equals (check.toString ()));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set a TIME parameter.
    **/
    public void Var045()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIME) VALUES (?)");
            Time t = new Time (345333);
            ps.setObject (1, t, Types.TIME, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Time check = rs.getTime (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set a TIME parameter with a nonzero scale.
    **/
    public void Var046()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIME) VALUES (?)");
            Time t = new Time (344311133);
            ps.setObject (1, t, Types.TIME,8);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Time check = rs.getTime (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setObject() - Set a TIMESTAMP parameter.
    **/
    public void Var047()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIMESTAMP) VALUES (?)");              
            Timestamp ts = new Timestamp (3434230);
            ps.setObject (1, ts, Types.TIMESTAMP, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIMESTAMP FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Timestamp check = rs.getTimestamp (1);
            rs.close ();

            assertCondition (check.toString ().equals (ts.toString ()));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setObject() - Set a TIMESTAMP parameter with a nonzero scale.
    **/
    public void Var048()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIMESTAMP) VALUES (?)");              
            Timestamp ts = new Timestamp (934230);
            ps.setObject (1, ts, Types.TIMESTAMP, 3);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIMESTAMP FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Timestamp check = rs.getTimestamp (1);
            rs.close ();

            assertCondition (check.toString ().equals (ts.toString ()));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setObject() - Set a DATALINK parameter.
    **/
    public void Var049()
    {
        if(checkLobSupport ())
        {
            try
            {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(40))))");
                ps.setObject (1, "http://www.yahoo.com/lists.html", Types.VARCHAR, 0);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equalsIgnoreCase("http://www.yahoo.com/lists.html"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    setObject() - Set a DATALINK parameter with a nonzero scale.
    **/
    public void Var050()
    {
        if(checkLobSupport ())
        {
            try
            {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(35))))");
                ps.setObject (1, "http://java.sun.com/jdbc.html", Types.VARCHAR, 7);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equalsIgnoreCase("http://java.sun.com/jdbc.html"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    setObject() - Set a DISTINCT parameter.
    **/
    public void Var051()
    {
        if(checkLobSupport ())
        {
            try
            {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DISTINCT) VALUES (?)");
                ps.setObject (1, new Integer (-92), Types.INTEGER, 0);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                int check = rs.getInt (1);
                rs.close ();

                assertCondition (check == -92);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    setObject() - Set a DISTINCT parameter with a nonzero scale.
    **/
    public void Var052()
    {
        if(checkLobSupport ())
        {
            try
            {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DISTINCT) VALUES (?)");
                ps.setObject (1, new Integer (12), Types.INTEGER, 2);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                int check = rs.getInt (1);
                rs.close ();

                assertCondition (check == 12);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    setObject() - Set a BIGINT parameter.
    **/
    public void Var053()
    {
        if(checkBigintSupport())
        {
            try
            {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BIGINT) VALUES (?)");
                ps.setObject (1, new Long (5323495), Types.BIGINT, 0);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                long check = rs.getLong (1);
                rs.close ();

                assertCondition (check == 5323495);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    setObject() - Set a BIGINT parameter with a nonzero scale.
    **/
    public void Var054()
    {
        if(checkBigintSupport())
        {
            try
            {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BIGINT) VALUES (?)");
                ps.setObject (1, new Long (-59322445665l), Types.BIGINT, 4);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                long check = rs.getLong (1);
                rs.close ();

                assertCondition (check == -59322445665l);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    setObject() - Set an VARCHAR parameter using Types.LONGVARCHAR.  This is by user request.
    
    SQL400 - Not run through the Toolbox as I don't think they make this work.
    **/
    public void Var055()
    {
        if(getDriver () == JDTestDriver.DRIVER_NATIVE)
        {
            try
            {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_VARCHAR_50) VALUES (?)");
                ps.setObject (1, "Aberdeen", Types.LONGVARCHAR, 0);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("Aberdeen"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
        else
        {
            notApplicable();
        }
    }



    /**
    setObject() - Set an VARCHAR parameter with a nonzero scale using Types.LONGVARCHAR.  This is by user request.
    
    SQL400 - Not run through the Toolbox as I don't think they make this work.
    **/
    public void Var056()
    {
        if(getDriver () == JDTestDriver.DRIVER_NATIVE)
        {
            try
            {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_VARCHAR_50) VALUES (?)");
                ps.setObject (1, "Desmet", Types.LONGVARCHAR, 4);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equals ("Desmet"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
        else
        {
            notApplicable();
        }
    }



    /**
    setObject() - Set a VARBINARY parameter using Types.LONGVARBINARY.  This is by user request.
    
    SQL400 - Not run through the Toolbox as I don't think they make this work.
    **/
    public void Var057()
    {
        if(getDriver () == JDTestDriver.DRIVER_NATIVE)
        {
            try
            {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_VARBINARY_20) VALUES (?)");
                byte[] b = { (byte) -13, (byte) 32, (byte) 0, (byte) -1, (byte) -11, (byte) 99};
                ps.setObject (1, b, Types.LONGVARBINARY, 0);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                byte[] check = rs.getBytes (1);
                rs.close ();

                assertCondition (isEqual (check, b));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
        else
        {
            notApplicable();
        }
    }




    /**
    setObject() - Set a VARBINARY parameter with a nonzero scale using Types.LONGVARBINARY.  This is by user request.
    
    SQL400 - Not run through the Toolbox as I don't think they make this work.
    **/
    public void Var058()
    {
        if(getDriver () == JDTestDriver.DRIVER_NATIVE)
        {
            try
            {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_VARBINARY_20) VALUES (?)");
                byte[] b = { (byte) -13, (byte) 32, (byte) 100, (byte) -1, (byte) -11, (byte) 99};
                ps.setObject (1, b, Types.LONGVARBINARY, 3);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                byte[] check = rs.getBytes (1);
                rs.close ();

                assertCondition (isEqual (check, b));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
        else
        {
            notApplicable();
        }
    }


    /**
    setObject() - Set an DECIMAL parameter using exponential notation 
    **/
    public void Var059()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, "8.223187E+02");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 4);
            rs.close ();

            assertCondition (check.doubleValue () == 822.3187);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    setObject() - Set an DECIMAL parameter using exponential notation with 0 in the exponent 
    **/
    public void Var060()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, "8.22E+00");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 4);
            rs.close ();

            assertCondition (check.doubleValue () == 8.22);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    setObject() - Set an DECIMAL parameter using exponential notation with 0 as the value 
    **/
    public void Var061()
    {
        try
        {
            statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject(1, "0E+01");
            ps.executeUpdate();
            ps.close();

            ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next();
            BigDecimal check = rs.getBigDecimal(1, 4);
            rs.close();

            assertCondition(check.doubleValue() == 0.0);
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    setObject() - Set an DECIMAL parameter using exponential notation with 0 as the value and 0 as the exponent
    **/
    public void Var062()
    {
        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, "0E+00");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 4);
            rs.close ();

            assertCondition (check.doubleValue () == 0.0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    setObject() -- Set a timestamp parameter to a char field and get the timestamp back out
    */
    public void Var063()
    {
        try
        {
            String col = JDPSTest.COLLECTION;
            Statement stmt = statement_; 
            try
            {
                stmt.executeUpdate("drop table "+col+".CHAR_TAB");
            }
            catch(Exception e)
            {
            }
            stmt.executeUpdate("create table "+col+".CHAR_TAB (COFFEE_NAME CHAR(30), NULL_VAL CHAR(30) )");

            stmt.executeUpdate("delete from "+col+".CHAR_TAB");
            stmt.executeUpdate("insert into "+col+".CHAR_TAB values('Test Coffee', null)");

            String sPrepStmt = "update "+col+".CHAR_TAB set NULL_VAL=?";

            String maxStringVal = "1999-12-31 12:59:59.000000";
            Timestamp maxTimestampVal = java.sql.Timestamp.valueOf(maxStringVal);

            java.sql.PreparedStatement pstmt = connection_.prepareStatement(sPrepStmt);
            pstmt.setObject(1,maxTimestampVal,java.sql.Types.CHAR);
            pstmt.executeUpdate();

            //to query from the database to check the call of pstmt.executeUpdate
            String Null_Val_Query = "Select NULL_VAL from "+col+".CHAR_TAB";
            ResultSet rs = stmt.executeQuery(Null_Val_Query);
            rs.next();

            String rStringVal = (String)rs.getObject(1);
            rStringVal = rStringVal.trim();
            Timestamp rTimestampVal = java.sql.Timestamp.valueOf(rStringVal);

            rs.close();

            stmt.executeUpdate("drop table "+col+".CHAR_TAB");


            boolean condition = rTimestampVal.equals(maxTimestampVal);
            if(!condition)
            {
                System.out.println("Returned String Value after Updation: " + rStringVal);
                System.out.println("Returned Timestamp Value after Updation: " + rTimestampVal);
            }
            assertCondition(condition);


        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }

    }



/**
setObject() - Set an DECFLOAT(16) parameter.
**/
    public void Var064()
    {

	String added = " -- DECFLOAT(16) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 


	    try {


		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
								     + " VALUES (?)");
		ps.setObject (1, new BigDecimal ("322.344"), Types.OTHER, 0);
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1);
		rs.close ();

		assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception "+added);
	    }
	}
    }



/**
setObject() - Set an DECFLOAT(16) parameter, when the data gets truncated.
**/
    public void Var065()
    {
	String added = " -- DECFLOAT(16) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 

	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
								     + " VALUES (?)");

		ps.setObject (1, new BigDecimal ("9.999999999999999E800"), Types.OTHER, 0);
                ps.executeUpdate(); 
                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();
                
		failed ("Didn't throw SQLException but got "+check+ added );
	    }
	    catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1)
				 && (dt.getParameter() == true)
				 && (dt.getRead() == false)
				 && (dt.getTransferSize() == 5), added);

	    } catch (SQLException e) {
		// The native driver will throw a Data Type mismatch since this cannot be expressed
		boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		if (success) { 
		    assertCondition(true); 
		} else { 
		    failed(e, "Expected data type mismatch (for native driver)"+added ); 
		} 


	    } catch (Exception e) {
		failed (e, "Unexpected Exception "+added);

	    }
	}

    }

/**
setObject() - Set an DECFLOAT(16) parameter, when the fraction gets truncated.  This
does not cause a data truncation exception.
**/
    public void Var066()
    {
	String added = " -- DECFLOAT(16) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
								     + " VALUES (?)");
		ps.setObject (1, new BigDecimal ("322.34412345678901234567890"), Types.OTHER, 0);
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1);
		rs.close ();

		assertCondition (check.toString().equals("322.3441234567890") || check.toString().equals("322.344123456789"), "Got "+check +" sb 322.3441234567890 "+added);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception"+added);
	    }

	}
    }


/**
setObject() - Set a DECFLOAT(16) parameter, when the object is the wrong type.
**/
    public void Var067()
    {
	String added = " -- DECFLOAT(16) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
								     + " VALUES (?)");
		ps.setObject (1, "Friends", Types.OTHER, 0);
		failed ("Didn't throw SQLException"+added);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

 
    /**
    setObject() - Set an DECFLOAT(34) parameter.
    **/
        public void Var068()
        {

        String added = " -- DECFLOAT(34) test added by native driver 05/09/2007"; 
        if (checkDecFloatSupport()) { 


            try {


                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

                PreparedStatement ps = connection_.prepareStatement (
                                                                     "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                                                                     + " VALUES (?)");
                ps.setObject (1, new BigDecimal ("322.344"), Types.OTHER, 0);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

                assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception "+added);
            }
        }
        }



    /**
    setObject() - Set an DECFLOAT(34) parameter, when the data gets truncated.
    **/
        public void Var069()
        {
        String added = " -- DECFLOAT(34) test added by native driver 05/09/2007"; 
        if (checkDecFloatSupport()) { 

            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

                PreparedStatement ps = connection_.prepareStatement (
                                                                     "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                                                                     + " VALUES (?)");

                ps.setObject (1, new BigDecimal ("9.999999999999999E56642"), Types.OTHER, 0);
                ps.executeUpdate(); 

                
                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

                failed ("Didn't throw SQLException got "+check+ added );
            }
            catch (DataTruncation dt) {
                assertCondition ((dt.getIndex() == 1)
                                 && (dt.getParameter() == true)
                                 && (dt.getRead() == false)
                                 && (dt.getTransferSize() == 5), added);

	    } catch (SQLException e) {
		// The native driver will throw a Data Type mismatch since this cannot be expressed
		boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		if (success) { 
		    assertCondition(true); 
		} else { 
		    failed(e, "Expected data type mismatch (for native driver)"+added ); 
		} 


            } catch (Exception e) {
                failed (e, "Unexpected Exception "+added);

            }
        }

        }

    /**
    setObject() - Set an DECFLOAT(34) parameter, when the fraction gets rounded.  This
    does not cause a data truncation exception.
    **/
        public void Var070()
        {
        String added = " -- DECFLOAT(34) test added by native driver 05/09/2007"; 
        if (checkDecFloatSupport()) { 
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

                PreparedStatement ps = connection_.prepareStatement (
                                                                     "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                                                                     + " VALUES (?)");
                ps.setObject (1, new BigDecimal ("322.344123456789012345678901234567890123456789088323"), Types.OTHER, 0);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

		String expected = "322.3441234567890123456789012345679"; 
                assertCondition (check.toString().equals(expected), "Got "+check +" sb "+expected+added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }

        }
        }


    /**
    setObject() - Set a DECFLOAT(34) parameter, when the object is the wrong type.
    **/
        public void Var071()
        {
        String added = " -- DECFLOAT(34) test added by native driver 05/09/2007"; 
        if (checkDecFloatSupport()) { 
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

                PreparedStatement ps = connection_.prepareStatement (
                                                                     "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                                                                     + " VALUES (?)");
                ps.setObject (1, "Friends", Types.OTHER, 0);
                failed ("Didn't throw SQLException"+added);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }



    /**
    setObject() - Should work with a valid parameter index
    greater than 1.
    **/
    public void Var072()
    {

	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
            ps.setString (1, "Test");
            ps.setObject (2, new BigDecimal ("4.3"), Types.NUMERIC, 1);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 1);
            rs.close ();

            assertCondition (check.doubleValue() == 4.3, added);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception" + added);
        }
    }


   /**
    setObject() - Set an DECIMAL parameter with scale 0.
    **/
    public void Var073()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("32.234"), Types.DECIMAL, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 3);
            rs.close ();

            assertCondition (check.doubleValue () == 32, "got "+check+" sb 32" + added);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception"+added);
        }
    }


   /**
    setObject() - Set an DECIMAL parameter with scale greater than the number.
    **/
    public void Var074()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("-12.87"), Types.DECIMAL, 4);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 2);
            rs.close ();

            assertCondition (check.doubleValue () == -12.87, added);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception"+added);
        }
    }



    /**
    setObject() - Set an NUMERIC parameter with scale 0.
    **/
    public void Var075()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("-432.34"), Types.NUMERIC, 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (check.doubleValue () == -432, added);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception"+added);
        }
    }



    /**
    setObject() - Set an NUMERIC parameter with scale less than the number.
    **/
    public void Var076()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("52.219"), Types.NUMERIC, 1);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 1);
            rs.close ();

            assertCondition (check.doubleValue () == 52.2, added);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception"+added);
        }
    }


    /**
    setObject() - Set an DECIMAL parameter using exponential notation 
    **/
    public void Var077()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

        try
        {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, "8.223187E+02");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 4);
            rs.close ();

            assertCondition (check.doubleValue () == 822.3187, added );
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception"+added);
        }
    }




/**
setObject() - Set an DECFLOAT(16) parameter.
**/
    public void Var078()
    {

	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 


	if (checkDecFloatSupport()) { 


	    try {


		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
								     + " VALUES (?)");
		ps.setObject (1, new BigDecimal ("322.344"), Types.OTHER, 0);
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1);
		rs.close ();

		assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception "+added);
	    }
	}
    }


    /**
    setObject() - Set an DECFLOAT(34) parameter.
    **/
        public void Var079()
        {

	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 


        if (checkDecFloatSupport()) { 


            try {


                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

                PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                     "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                                                                     + " VALUES (?)");
                ps.setObject (1, new BigDecimal ("322.344"), Types.OTHER, 0);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

                assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception "+added);
            }
        }
        }

        
                /**
setParameterTest () - Set the specified parameter using an object. 
         * @param setType 
**/
    public void setParameterTest(String columnName, Object setObject, int setType, int scale,  String expectedString)
    {
         try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " ("+columnName+") VALUES (?)");
            ps.setObject (1, setObject, setType, scale);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT "+columnName+" FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (expectedString.equals(check)," got "+check+" sb " + expectedString);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
   } 

  public void Var080() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", new Boolean(true), Types.BOOLEAN, 0, "1");
    }
  }

  public void Var081() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", new Boolean(false),Types.BOOLEAN, 0, "0");
    }
  }

  public void Var082() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", "true", Types.BOOLEAN, 0,"1");
    }
  }

  public void Var083() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", "false", Types.BOOLEAN, 0, "0");
    }
  }

  public void Var084() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", new Integer(100),Types.BOOLEAN, 0, "1");
    }
  }

  public void Var085() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", new Integer(0),Types.BOOLEAN, 0, "0");
    }
  }






}
