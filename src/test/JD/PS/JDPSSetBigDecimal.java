///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetBigDecimal.java
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
// File Name:    JDPSSetBigDecimal.java
//
// Classes:      JDPSSetBigDecimal
//
////////////////////////////////////////////////////////////////////////
//
//
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
import test.JVMInfo;
import test.JD.JDSetupPackage;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDPSetBigDecimal.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setBigDecimal()
</ul>
**/
public class JDPSSetBigDecimal
extends JDTestcase {



    // Constants.
    private static final String PACKAGE             = "JDPSSBD";



    // Private data.
    private Connection          connection_;
    private Connection          connectionNoDT_;
    private Statement           statement_;


    int jdk_; 

/**
Constructor.
**/
    public JDPSSetBigDecimal (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password)
    {
        super (systemObject, "JDPSSetBigDecimal",
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

	jdk_ = JVMInfo.getJDK(); 
        String url = baseURL_
                     
                     
                     + ";data truncation=true";
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



/**
Compares a BigDecimal with a double, and allows a little rounding error.
**/
    private boolean compare (BigDecimal bd, double d)
    {
        return(Math.abs (bd.doubleValue () - d) < 0.001);
    }





/**
setBigDecimal() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.close ();
            ps.setBigDecimal (1, new BigDecimal (0.43453));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBigDecimal() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?, ?)");
            ps.setBigDecimal (100, new BigDecimal (4.43233));
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBigDecimal() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?, ?)");
            ps.setBigDecimal (0, new BigDecimal (-22.4538));
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBigDecimal() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?, ?)");
            ps.setBigDecimal (-1, new BigDecimal (22.594));
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBigDecimal() - Should work with a valid parameter index
greater than 1.
**/
    public void Var005()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
            ps.setString (1, "Test");
            ps.setBigDecimal (2, new BigDecimal ("-423.43"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 2);
            rs.close ();

            assertCondition (compare (check, -423.43));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Should set to SQL NULL when null is passed.
**/
    public void Var006()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setBigDecimal (1, null);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition ((check == null) && (wn == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var007()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            ps.setBigDecimal (2, new BigDecimal (22.522));
            ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBigDecimal() - Set a SMALLINT parameter.
**/
    public void Var008()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_SMALLINT) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (264.0));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (check.intValue () == 264);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set a SMALLINT parameter, when there is a decimal part.  This will work 
by silently truncating the data.
**/
    public void Var009()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_SMALLINT) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (12.2222));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (check.intValue () == 12);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set a SMALLINT parameter, when the value is too large.  This
will result in a DataTruncation exception.
**/
    public void Var010()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_SMALLINT) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (12020282.0));
            ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              String exmessage = e.toString(); 
              if (exmessage.indexOf("Invalid data conversion") >= 0) {
                succeeded(); 
              } else {
                e.printStackTrace(); 
                failed("Unexpected exception "+exmessage); 
              }
            } else {

		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V6R1");

            }
        }
    }



/**
setBigDecimal() - Set an INTEGER parameter.
**/
    public void Var011()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (6793.0));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (check.intValue () == 6793, "got "+check.intValue()+" sb 6793");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set an INTEGER parameter, when there is a decimal part.  This will work 
by silently truncating the data.
**/
    public void Var012()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (6.83));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (check.intValue () == 6, "got "+check.intValue()+" sb 6");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set an INTEGER parameter, when the value is too large.  This
will result in a DataTruncation exception.
**/
    public void Var013()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (1484573742020282.0));
            ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            String exmessage = e.toString(); 
            if (exmessage.indexOf("Invalid data conversion") >= 0) {
              succeeded(); 
            } else {
              e.printStackTrace(); 
              failed("Unexpected exception "+exmessage); 
            }
          } else {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

          }
        }
    }



/**
setBigDecimal() - Set a REAL parameter.
**/
    public void Var014()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_REAL) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal ("-92.2497"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 4);
            rs.close ();

            assertCondition (compare (check, -92.2497));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set a FLOAT parameter.
**/
    public void Var015()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_FLOAT) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (0.123));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 3);
            rs.close ();

            assertCondition (compare (check, 0.123));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set an DOUBLE parameter.
**/
    public void Var016()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DOUBLE) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (0.222903441));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 9);
            rs.close ();

            assertCondition (compare (check, 0.222903441));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set an DECIMAL parameter.
**/
    public void Var017()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal ("4001.23421"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 5);
            rs.close ();

            assertCondition (compare (check, 4001.23421));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set an DECIMAL parameter, when the value is too big.
**/
    public void Var018()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_50) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (324512.21));
            ps.execute();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            String expected = "Exception occurred during BigDecimal conversion"; 
            String exmessage = e.toString(); 
            if (exmessage.indexOf(expected ) >= 0) {
              succeeded(); 
            } else {
              // e.printStackTrace(); 
              failed("Unexpected exception '"+exmessage+"' expected '"+expected+"'"); 
            }
          } else {
	      if (e instanceof DataTruncation) { 
		  DataTruncation dt = (DataTruncation)e;
		  assertCondition ((dt.getIndex() == 1)
				   && (dt.getParameter() == true)
				   && (dt.getRead() == false)
				   && (dt.getTransferSize() == 5));
	      } else {
		  // Data type mismatch is now the expected exception for overflow
		  if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox");

		  } else { 
		      
			failed("Unexpected exception '"+e+"' expected data truncation");
		  }
	      }
          }
        }
    }



/**
setBigDecimal() - Set an DECIMAL parameter, where only the BigDecimal's 
fraction truncates.  This should work.
**/
    public void Var019()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_50) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal ("40013.23421"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (compare (check, 40013), "got "+check.toString()+" sb 40013");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set an NUMERIC parameter.
**/
    public void Var020()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal ("7742"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 4);
            rs.close ();

            assertCondition (compare (check, 7742));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set an NUMERIC parameter, when the value is too big
SQL400 - right now the Native JDBC driver data truncation exceptions report the 
data size and transfer size for the precision - the scale.  This is because the
the scale portion can be of any size and will be ignored.  Also, otherwise you 
can get into situations where the needed size is actually smaller than than 
the available size which could be confusing.  Plus, it was easiest for me to 
do that. :)
**/
    public void Var021()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (2173345334.42));
            ps.execute();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            String expected = "Exception occurred during BigDecimal conversion"; 
            String exmessage = e.toString(); 
            if (exmessage.indexOf(expected) >= 0) {
              succeeded(); 
            } else {
              e.printStackTrace(); 
              failed("Unexpected exception "+exmessage+" expected '"+expected+"'"); 
            }
          } else {
	      if (e instanceof  DataTruncation) { 
		  DataTruncation dt = (DataTruncation)e;
		  if (isToolboxDriver()  || getDriver () == JDTestDriver.DRIVER_JTOPENLITE) {
		      assertCondition ((dt.getIndex() == 1)
				       && (dt.getParameter() == true)
				       && (dt.getRead() == false)
				       && (dt.getTransferSize() == 10),
				       "index/parameter/read/datasize/transfersize="+
				       dt.getIndex()+"/"+
				       dt.getParameter() +"/"+
				       dt.getRead()+"/"+
				       dt.getTransferSize()+
				       "sb 1/true/false/10");
		  }else {
		      if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 49160) {
			  notApplicable("Native driver fix in PTF V7R1 SI49160" );
			  return; 
		      }

			  assertCondition ((dt.getIndex() == 1)
					   && (dt.getParameter() == true)
					   && (dt.getRead() == false)
					   && (dt.getDataSize() == 15)
					   && (dt.getTransferSize() == 10),
					   "index/parameter/read/datasize/transfersize="+
					   dt.getIndex()+"/"+
					   dt.getParameter() +"/"+
					   dt.getRead()+"/"+
					   dt.getDataSize() +"/"+
					   dt.getTransferSize()+
					   "sb 1/true/false/15/10  Updated 1/18/2013 for V7R1 fix");

		  }
	      } else { /* not data truncation */ 
		  if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		      assertSqlException(e, -99999, "07006", "Data type mismatch", "Expected mismatch for jtopenlite");
		  } else {
		      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
			  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox");

		      } else { 

			  failed("Unexpected exception '"+e+"' expected data truncation");
		      }
		  }
	      }

          }
        }
    }



/**
setBigDecimal() - Set a NUMERIC parameter, where only the BigDecimal's 
fraction truncates.  This should work.
**/
    public void Var022()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_50) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal ("-40013.23421"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (compare (check, -40013), "got "+check.toString()+" sb -40013");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set an CHAR(50) parameter.
**/
    public void Var023()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_50) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (-1842.7993322));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 7);
            rs.close ();

            assertCondition (compare (check, -1842.7993322));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set an CHAR(1) parameter.   
**/
    public void Var024()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_1) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal ("8"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (compare (check, 8));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set an CHAR(1) parameter, when the value is too big.
**/
    public void Var025()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_1) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (533.25));
            ps.execute(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            String exmessage = e.toString(); 
            /* SQL0302 = Conversion error on host variable or parameter */ 
            if (exmessage.indexOf("-302") >= 0) {
              succeeded(); 
            } else {
              e.printStackTrace(); 
              failed("Unexpected exception "+exmessage); 
            }
          } else { 
            assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
          }
        }
    }



/**
setBigDecimal() - Set an VARCHAR parameter.
**/
    public void Var026()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal ("0.8766753"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 7);
            rs.close ();

            assertCondition (compare (check,0.8766753));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setBigDecimal() - Set a CLOB parameter.
**/
    public void Var027()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_CLOB) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (542.36));
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
setBigDecimal() - Set a DBCLOB parameter.
**/
    public void Var028()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DBCLOB) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (5.4324));
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
setBigDecimal() - Set a BINARY parameter.
**/
    public void Var029()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BINARY_20) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (45.445));
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setBigDecimal() - Set a VARBINARY parameter.
**/
    public void Var030()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (0.94431));
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setBigDecimal() - Set a BLOB parameter.
**/
    public void Var031()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BLOB) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (-54.433));
            ps.executeUpdate(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setBigDecimal() - Set a DATE parameter.
**/
    public void Var032()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DATE) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (-6.54));
            ps.executeUpdate(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBigDecimal() - Set a TIME parameter.
**/
    public void Var033()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIME) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (-0.31));
            ps.executeUpdate(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBigDecimal() - Set a TIMESTAMP parameter.
**/
    public void Var034()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIMESTAMP) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal (-544.0));
            ps.executeUpdate(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
setBigDecimal() - Set a DATALINK parameter.
**/
    public void Var035()
    {
        if ((getDriver() == JDTestDriver.DRIVER_JCC)) {
          notApplicable("Datalink test"); 
          return; 
        }
        if ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
            notApplicable ("Native driver pre-JDBC 3.0");
            return;
        }
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATALINK) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (5.543));
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
setBigDecimal() - Set a DISTINCT parameter.  Age is an integer and we can therefore put the value
into it with silent truncation with the setBigDecimal method.  
**/
    public void Var036()
    {
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DISTINCT) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (-9.813));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1, 0);
                rs.close ();

                assertCondition (check.intValue () == -9);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a BIGINT parameter.
**/
    public void Var037()
    {
        if (checkBigintSupport()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BIGINT) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (-67393.0));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1, 0);
                rs.close ();

                assertCondition (check.longValue () == -67393);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a BIGINT parameter, when there is a decimal part.  This will work 
by silently truncating the data.
**/
    public void Var038()
    {
        if (checkBigintSupport()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BIGINT) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (-6.834));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1, 0);
                rs.close ();

                assertCondition (check.intValue () == -6);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a BIGINT parameter, when the value is too large.  This
will result in a DataTruncation exception.
**/
    public void Var039()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BIGINT) VALUES (?)");
            ps.setBigDecimal (1, new BigDecimal ("9999999999999999999494"));
            ps.executeUpdate(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            String exmessage = e.toString(); 
            if (exmessage.indexOf("Invalid data conversion") >= 0) {
              succeeded(); 
            } else {
              e.printStackTrace(); 
              failed("Unexpected exception "+exmessage); 
            }
          } else { 
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

          }
          
        }
        }
    }



/**
setBigDecimal() - Set a parameter in a statement that comes from the
package cache.
**/
    public void Var040()
    {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("Toolbox testcase"); 
          return; 
        }
        try {
            String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
                            + " (C_DOUBLE) VALUES (?)";

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
            PreparedStatement ps = c2.prepareStatement (insert);
            ps.setBigDecimal (1, new BigDecimal (2.13598));
            ps.executeUpdate ();
            ps.close ();
            c2.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 5);
            rs.close ();            

            assertCondition (compare (check, 2.13598));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
SQL400 - testcase added.
setBigDecimal() - Set a SMALLINT parameter, when there is a decimal part but data trunction errors are 
                  disabled.  The fact that data truncation is turned off is no longer relevant.
                  This test will work in either case because this is not 'significant' truncation.
                  This test can be removed in the future.
**/
    public void Var041()
    {
        if (checkNative()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connectionNoDT_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_SMALLINT) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (12.2222));
                ps.executeUpdate ();
                ps.close ();


                ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1, 0);
                rs.close ();

                assertCondition (compare (check, 12));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
SQL400 - testcase added.
setBigDecimal() - Set an INTEGER parameter, when there is a decimal part,
                  but the connection has decimal truncation errors turned off.
                  The fact that data truncation is turned off is no longer relevant.
                  This test will work in either case because this is not 'significant' truncation.
                  This test can be removed in the future.
**/
    public void Var042()
    {
        if (checkNative()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connectionNoDT_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_INTEGER) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (6.83));
                ps.executeUpdate ();
                ps.close ();


                ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1, 0);
                rs.close ();

                assertCondition (compare (check, 6));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
SQL400 - testcase added.
setBigDecimal() - Set an DECIMAL parameter, when the value is too big,
                  but the connection has decimal truncation errors turned off.
                  The fact that data truncation is turned off is no longer relevant.
                  This test will work in either case because this is not 'significant' truncation.
                  This test can be removed in the future.
**/
    public void Var043()
    {
        if (checkNative()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connectionNoDT_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_DECIMAL_50) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (12.21));
                ps.executeUpdate ();
                ps.close ();


                ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_50 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1, 0);
                rs.close ();

                assertCondition (compare (check, 12));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
SQL400 - testcase added.
setBigDecimal() - Set an NUMERIC parameter, when the value is too big, but the data truncaction
                  flag is turned off.  The fact that data truncation is turned off is no longer 
                  relevant.  This test will always fail because this is 'significant' data 
                  truncation.  The data truncation flag will not allow you to put values into 
                  the database that are just plain wrong.
**/
    public void Var044()
    {
        if (checkNative()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connectionNoDT_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_NUMERIC_105) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (217334.42));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                DataTruncation dt = (DataTruncation)e;
                if (isToolboxDriver())  // yeah, I know the toolbox doesn't
                    assertCondition ((dt.getIndex() == 1)                  // come through this test today, but
                        && (dt.getParameter() == true)            // I just thought I would be a good
                        && (dt.getRead() == false)                // citizen. :)
                        && (dt.getTransferSize() == 10));
		else {
		    if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 49160) {
			notApplicable("Native driver fix in PTF V7R1 SI49160" );
			return; 
		    }
                    assertCondition ((dt.getIndex() == 1)
                        && (dt.getParameter() == true)
                        && (dt.getRead() == false)
                        && (dt.getDataSize() == 11)
                        && (dt.getTransferSize() == 10), "Updated 1/18/2013 for V7R1 fix");
		}
            }
        }
    }


/**
SQL400 - testcase added.
setBigDecimal() - Set a CHAR(1) parameter, when the value is too big, but the data truncation
                  flag is turned off.  Today this test still allows this data truncation because
                  it is going to a character field.  The data truncation flag is in place to 
                  allow character fields to be truncated without error.
**/
    public void Var045()
    {
        if (checkNative()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connectionNoDT_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_CHAR_1) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (533.25));
                ps.executeUpdate ();
                ps.close ();


                ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1, 0);
                rs.close ();

                assertCondition (compare (check, 5));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
     * set a DECIMAL(2,2) field where the data was truncated.
     * this causes problems for the native driver in V5R4, but didn't
     * appear in previous releases
     * The V5R4 PTR is 9A86093
     * 
     * This behavior is wrong.. jcc toolbox throws an error
     * Fixed in V5R5 by the native driver. 
     */

    public void Var046()
    {
        if ((getDriver() == JDTestDriver.DRIVER_NATIVE) && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
            notApplicable("Variation doesn't work in V5R3");
            return; 
        } 
        //toolbox v6r1 has new behavior also
        boolean oldBehavior =  ((isToolboxDriver()  && getRelease() < JDTestDriver.RELEASE_V7R1M0) || 
                ((getDriver() == JDTestDriver.DRIVER_NATIVE) && 
                        (getRelease() < JDTestDriver.RELEASE_V7R1M0) ) );

        if (checkBigintSupport()) {
            try {
                String tableName = JDPSTest.COLLECTION +".JDPSBD046"; 
                try {
                    statement_.executeUpdate("DROP TABLE "+tableName);
                } catch (Exception e) {
                    // e.printStackTrace(); 
                } 
                statement_.executeUpdate("CREATE TABLE "+tableName+" (c1 DECIMAL(2,2))");
                statement_.executeUpdate("INSERT INTO "+tableName+" values(0.11)");

                PreparedStatement ps;
                ps = connection_.prepareStatement ( "select c1 from " + tableName + " where c1 =?");
                ps.setBigDecimal (1, new BigDecimal ("1.11"));
                ResultSet rs = ps.executeQuery();
                String value; 
                if (rs.next()) { 
                    value=rs.getString(1);
                } else {
                    value="NO DATA FOUND"; 
                }
                rs.close(); 
                // statement_.executeUpdate("DROP TABLE "+tableName);
                if (oldBehavior) { 
                    assertCondition("0.11".equals(value), "value("+value+") != 0.11 -- added by native driver 12/15/2005");
                } else {
                    failed("Did not throw exception for setting 1.11 as decimal(2,2)"); 
                }
            } catch (Exception e) {
                if (oldBehavior) { 
                    failed (e, "Unexpected Exception -- added by native driver 12/15/2004 ");
                } else {
                    String exceptionInfo = "Exception occurred during BigDecimal conversion";
                    if (getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()  || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
                        exceptionInfo="Data truncation"; 
                    } 
                    String exToString = e.toString(); 
                    if (exToString.indexOf(exceptionInfo) >= 0) { 
                        assertCondition(true); 
                    } else {
			if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
			    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox");

			} else { 

			    e.printStackTrace(); 
			    failed("Unexpected Exception '"+exToString+"' expected '"+exceptionInfo+"'\n");
			}
                    }
                }
            }
        }
    }

    /**
     * set a DECIMAL field using a BigDecimal which in JDK 1.5 will be formatted
     * with an Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */

    public void Var047()
    {
      String added=" -- added by native driver 2/8/2006"; 

        if (checkBigintSupport()) {
            try {
                String tableName = JDPSTest.COLLECTION +".JDPSBD047"; 
                try {
                    statement_.executeUpdate("DROP TABLE "+tableName);
                } catch (Exception e) {
                    // e.printStackTrace(); 
                } 
                statement_.executeUpdate("CREATE TABLE "+tableName+" (c1 DECIMAL(12,10))");
                statement_.executeUpdate("INSERT INTO "+tableName+" values(0.00000016)");

                PreparedStatement ps;
                ps = connection_.prepareStatement ( "select c1 from " + tableName + " where c1 =?");
                ps.setBigDecimal (1, new BigDecimal ("0.00000016"));
                ResultSet rs = ps.executeQuery();
                rs.next();
                String value=rs.getString(1);
                String expectedValue = "1.600E-7"; 
		
		if(isToolboxDriver() || getDriver () == JDTestDriver.DRIVER_JTOPENLITE )
                    expectedValue = "0.0000001600";
                else if ((jdk_ <= JVMInfo.JDK_142) ||
                        ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V7R1M0 ))  ) {
                    expectedValue = "0.0000001600";
                }
                rs.close(); 
                statement_.executeUpdate("DROP TABLE "+tableName);
                assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);
                
            } catch (Exception e) {
                failed (e, "Unexpected Exception "+added); 
            }
        }
    }

    /**
     * set a DECIMAL field using a BigDecimal which in JDK 1.5 will be formatted
     * with a posite Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */

    public void Var048()
    {
      String added=" -- added by native driver 2/8/2006"; 

        if (checkBigintSupport()) {
            try {

                String tableName = JDPSTest.COLLECTION +".JDPSBD048"; 
                try {
                    statement_.executeUpdate("DROP TABLE "+tableName);
                } catch (Exception e) {
                    // e.printStackTrace(); 
                } 
                statement_.executeUpdate("CREATE TABLE "+tableName+" (c1 DECIMAL(14,2))");
                statement_.executeUpdate("INSERT INTO "+tableName+" values(1600000000)");

                PreparedStatement ps;
                ps = connection_.prepareStatement ( "select c1 from " + tableName + " where c1 =?");
                BigDecimal bd; 
                if (jdk_ <= JVMInfo.JDK_142) {
                  bd = new BigDecimal("1600000000"); 
                } else {
                  bd = new BigDecimal (new BigInteger("16"),-8 ); 
                }
                
                ps.setBigDecimal (1, bd);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String value=rs.getString(1);
                String expectedValue = "1600000000.00"; 
                if (jdk_ <= JVMInfo.JDK_142) {
                  expectedValue = "1600000000.00";
                }
                rs.close(); 
                statement_.executeUpdate("DROP TABLE "+tableName);
                assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);
                
            } catch (Exception e) {
                failed (e, "Unexpected Exception "+added); 
            }
        }
    }

    /**
     * set a NUMERIC field using a BigDecimal which in JDK 1.5 will be formatted
     * with an Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */

    public void Var049()
    {
      String added=" -- added by native driver 2/8/2006"; 

        if (checkBigintSupport()) {
            try {
                String tableName = JDPSTest.COLLECTION +".JDPSBD049"; 
                try {
                    statement_.executeUpdate("DROP TABLE "+tableName);
                } catch (Exception e) {
                    // e.printStackTrace(); 
                } 
                statement_.executeUpdate("CREATE TABLE "+tableName+" (c1 NUMERIC(12,10))");
                statement_.executeUpdate("INSERT INTO "+tableName+" values(0.00000016)");

                PreparedStatement ps;
                ps = connection_.prepareStatement ( "select c1 from " + tableName + " where c1 =?");
                ps.setBigDecimal (1, new BigDecimal ("0.00000016"));
                ResultSet rs = ps.executeQuery();
                rs.next();
                String value=rs.getString(1);
                String expectedValue = "1.600E-7"; 
                if(isToolboxDriver() || getDriver () == JDTestDriver.DRIVER_JTOPENLITE)
                    expectedValue = "0.0000001600";
                else if ((jdk_ <= JVMInfo.JDK_142)  ||
                    ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V7R1M0 ))) {
                  expectedValue = "0.0000001600";
                }
                rs.close(); 
                statement_.executeUpdate("DROP TABLE "+tableName);
                assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);
                
            } catch (Exception e) {
                failed (e, "Unexpected Exception "+added); 
            }
        }
    }

    /**
     * set a NUMERIC field using a BigDecimal which in JDK 1.5 will be formatted
     * with a positive Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */

    public void Var050()
    {
      String added=" -- added by native driver 2/8/2006"; 

        if (checkBigintSupport()) {
            try {

                String tableName = JDPSTest.COLLECTION +".JDPSBD050"; 
                try {
                    statement_.executeUpdate("DROP TABLE "+tableName);
                } catch (Exception e) {
                    // e.printStackTrace(); 
                } 
                statement_.executeUpdate("CREATE TABLE "+tableName+" (c1 NUMERIC(14,2))");
                statement_.executeUpdate("INSERT INTO "+tableName+" values(1600000000)");

                PreparedStatement ps;
                ps = connection_.prepareStatement ( "select c1 from " + tableName + " where c1 =?");
                BigDecimal bd; 
		if (jdk_ <= JVMInfo.JDK_142) {
                  bd = new BigDecimal("1600000000"); 
                } else {
                  bd = new BigDecimal (new BigInteger("16"),-8 ); 
                }
                
                ps.setBigDecimal (1, bd);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String value=rs.getString(1);
                String expectedValue = "1600000000.00"; 
                if (JVMInfo.getJDK() <= JVMInfo.JDK_142) {
                  expectedValue = "1600000000.00";
                }
                rs.close(); 
                statement_.executeUpdate("DROP TABLE "+tableName);
                assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);
                
            } catch (Exception e) {
                failed (e, "Unexpected Exception "+added); 
            }
        }
    }




    public boolean testDfp(StringBuffer sb, String table, String input, String expected, String exceptionInfo) {
      boolean passed = true; 
      try {
        statement_.executeUpdate ("DELETE FROM " + table);
        
        PreparedStatement ps = connection_.prepareStatement (
            "INSERT INTO " + table    + " (C1) VALUES (?)");
        ps.setBigDecimal (1, new BigDecimal (input ));
        ps.executeUpdate ();
        SQLWarning warning = ps.getWarnings();
        if (warning != null) {
          System.out.println("Got warning "+warning+" from "+input); 
        }
        ps.close ();
        
        ResultSet rs = statement_.executeQuery ("SELECT C1 FROM " + table);
        rs.next ();
        String check = rs.getString(1); 
        rs.close ();
        if (exceptionInfo == null) {
          if (expected.equals(check)) {
            passed=true; 
          } else if((isToolboxDriver()) && (expected+"0").equals(check)){
              passed=true; //some jdk14 append a trailing 0
          }
          else { 
            passed = false; 
            sb.append("FAILED: from "+input+" got "+check+" sb "+expected+"\n");
          }
        } else {
          passed = false; 
          sb.append("Exception with info '"+exceptionInfo+"' did not occur for "+input+" instead got "+check+"\n");
        }
      }
      catch (Exception e) {
        if (exceptionInfo == null) {
          passed = false; 
          e.printStackTrace(); 
          sb.append("FAILED:  Unexpected exception "+e.toString()+" from input "+input+"\n"); 
        } else {
          String exToString = e.toString(); 
          if (exToString.indexOf(exceptionInfo) >= 0) { 
            passed=true; 
          } else {
            e.printStackTrace(); 
            passed=false; 
            sb.append("FAILED:  Unexpected Exception "+exToString+" expected "+exceptionInfo+"\n");
          }
        }
      }
      return passed; 
    }
    
    public void testDfp(String table, String input, String expected, String exceptionInfo) {
      StringBuffer sb = new StringBuffer(); 
      if (checkDecFloatSupport()) {
        boolean passed = testDfp(sb, table, input,expected, exceptionInfo);
        assertCondition(passed,sb); 
      }
    }

    public void testDfp(String table, String[][] testValues) {
      if (checkDecFloatSupport()) {
        StringBuffer sb = new StringBuffer(); 
        boolean passed = true; 
        
        for (int i = 0; i < testValues.length; i++) { 
          String input = testValues[i][0]; 
          String expected = testValues[i][1]; 
          String exceptionInfo = testValues[i][2];  
          boolean thisPassed = testDfp(sb, table, input,expected, exceptionInfo);
          if (!thisPassed) passed = false; 
          
        }
        assertCondition(passed,sb); 
      }
    }


    /**
    setBigDecimal() - Set an DFP16  parameter.
    **/
   public void Var051()
   {
     testDfp (JDPSTest.PSTEST_SETDFP16, "40013.23421123456",  "40013.23421123456", null); 
   }

    
    /**
     setBigDecimal() - Set an DFP16  parameter, where only the BigDecimal's 
     fraction rounds.  This should work.
     **/
    public void Var052()
    {
      testDfp (JDPSTest.PSTEST_SETDFP16, "40013.23421123456789",  "40013.23421123457", null); 
    }

/**
setBigDecimal() - Set an DFP16 parameter -- maximum value .
**/
    public void Var053()
    {
	String expected="9.999999999999999E+384";
	if (getJDK() <= JVMInfo.JDK_142) {
	    expected = "9999999999999999000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"; 
	}
	testDfp(JDPSTest.PSTEST_SETDFP16, "9.999999999999999E384", expected, null); 
    }
    
    
/**
setBigDecimal() - Set an DFP16 parameter -- minimum positive value .
**/
    public void Var054()
    {
	String expected="1.0E-383";
	if (getJDK() <=  JVMInfo.JDK_142) {
              expected="0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010";
	}


        testDfp(JDPSTest.PSTEST_SETDFP16, "1.0E-383",  expected, null ); 
    }
    
    /**
    setBigDecimal() - Set an DFP16 parameter -- too large  .
    **/
        public void Var055()
        {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            testDfp(JDPSTest.PSTEST_SETDFP16, 
                "9.999999999999999E385", 
                "Exception", 
            "Infinity or -Infinity received for DECFLOAT(16)");
          } else { 
            testDfp(JDPSTest.PSTEST_SETDFP16, "9.999999999999999E385", "Exception", "Data type mismatch");
          }
        }
        
        
    /**
    setBigDecimal() - Set an DFP16 parameter -- too small .
    **/
        public void Var056()
        {
            testDfp(JDPSTest.PSTEST_SETDFP16, "1.0E-399",  "Exception", "Data type mismatch" ); 
        }
        
    /**
     * setBigDecimal() -- test a variety of valid values for DFP16 parameters
     */
        public void Var057() {
          String[][] testValues = {
              /* input  expected exception comment */ 
              {"0.01234567890123456E102",      "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"0.1234567890123456E101",       "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"1.234567890123456E100",        "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"12.34567890123456E99",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"123.4567890123456E98",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"1234.567890123456E97",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"12345.67890123456E96",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"123456.7890123456E95",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"1234567.890123456E94",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"12345678.90123456E93",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"123456789.0123456E92",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"1234567890.123456E91",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"12345678901.23456E90",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"123456789012.3456E89",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"1234567890123.456E88",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"12345678901234.56E87",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"123456789012345.6E86",         "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"1234567890123456.0E85",        "1.234567890123456E+100", null, "Various decimal shifts " }, 
              {"1234567890123456.00000001E85", "1.234567890123456E+100", null, "Various decimal shifts " },
              {"-123.4567890123456",           "-123.4567890123456",  null, "Negative number"}, 
              {"-123.4567890123456E-2",        "-1.234567890123456",     null, "Negative exponent"}, 
              {"-0000123.4567890123456",       "-123.4567890123456",  null, "Leading zeros"},
              {"-123.4567890123456E-002",      "-1.234567890123456",     null, "Exponent leading zeros"},
              {"1234567890123456789012345678901234567890123456789012345678901234",
                                               "1.234567890123457E+63",  null, "More than 63 coef digits"},
              {"1.234567890123456789012345678901234567890123456789012345678901234",
                                               "1.234567890123457",      null, "More than 63 coef digits with decimal point "},
              {"1234567890123456",             "1234567890123456",       null, "Without E"},              
              {"1.234567890123456",            "1.234567890123456",      null, "Without E"},              
              {"123456789012345.6",            "123456789012345.6",      null, "Without E"},              
              {"1234567890123456.0000000",     "1234567890123456",       null, "Without E"},              
          };
	  if (JVMInfo.getJDK() <=JVMInfo.JDK_142) {
	      notApplicable("JDK 1.5 and later test"); 
	  } else { 
	      testDfp(JDPSTest.PSTEST_SETDFP16, testValues);
	  }
        }

    

    /**
    setBigDecimal() - Set an DFP34  parameter. 
    **/
   public void Var058()
   { 
     testDfp (JDPSTest.PSTEST_SETDFP34, "40013.18181818181818181823421123456",  "40013.18181818181818181823421123456", null); 
   }

    
    /**
     setBigDecimal() - Set an DFP34  parameter, where only the BigDecimal's 
     fraction rounds.  This should work.
     **/
    public void Var059()
    {
      testDfp (JDPSTest.PSTEST_SETDFP34, "40013.18181818181818181823421123456789",  "40013.18181818181818181823421123457", null); 
    }

/**
setBigDecimal() - Set an DFP34 parameter -- maximum value .
**/
    public void Var060()
    {
	  if (getJDK() <= JVMInfo.JDK_142) {
	      notApplicable("JDK 1.5 and later test"); 
	  } else { 
	      String expected = "9.999999999999999000000000000000000E+6144";

	      testDfp(JDPSTest.PSTEST_SETDFP34, "9.999999999999999E6144", expected, null);
	  }
    }
    
    
/**
setBigDecimal() - Set an DFP34 parameter -- minimum positive value .
**/
    public void Var061()
    {
	  if (getJDK() <= JVMInfo.JDK_142) {
	      notApplicable("JDK 1.5 and later test"); 
	  } else {
	      String expected = "1.0E-6143";

	      testDfp(JDPSTest.PSTEST_SETDFP34, "1.0E-6143",  expected, null );
	  }
    }
    
    /**
    setBigDecimal() - Set an DFP34 parameter -- too large  .
    **/
        public void Var062()
        {
          testDfp(JDPSTest.PSTEST_SETDFP34, "9.999999999999999E6145", "Exception", "Data type mismatch"); 
        }
        
        
    /**
    setBigDecimal() - Set an DFP34 parameter -- too small .
    **/
        public void Var063()
        {
            testDfp(JDPSTest.PSTEST_SETDFP34, "1.0E-6177",  "Exception", "Data type mismatch" ); 
        }
        
    /**
     * setBigDecimal() -- test a variety of valid values for DFP34 parameters
     */
        public void Var064() {
          String[][] testValues = {
              /* input  expected exception comment */ 
              {"0.01234567890123456181818181818181818E102",      "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"0.1234567890123456181818181818181818E101",       "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1.234567890123456181818181818181818E100",        "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12.34567890123456181818181818181818E99",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123.4567890123456181818181818181818E98",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234.567890123456181818181818181818E97",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345.67890123456181818181818181818E96",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123456.7890123456181818181818181818E95",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234567.890123456181818181818181818E94",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345678.90123456181818181818181818E93",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123456789.0123456181818181818181818E92",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234567890.123456181818181818181818E91",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345678901.23456181818181818181818E90",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123456789012.3456181818181818181818E89",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234567890123.456181818181818181818E88",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345678901234.56181818181818181818E87",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123456789012345.6181818181818181818E86",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234567890123456.181818181818181818E85",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345678901234561.81818181818181818E84",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123456789012345618.1818181818181818E83",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234567890123456181.818181818181818E82",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345678901234561818.18181818181818E81",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123456789012345618181.8181818181818E80",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234567890123456181818.181818181818E79",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345678901234561818181.81818181818E78",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123456789012345618181818.1818181818E77",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234567890123456181818181.818181818E76",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345678901234561818181818.18181818E75",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123456789012345618181818181.8181818E74",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234567890123456181818181818.181818E73",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345678901234561818181818181.81818E72",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123456789012345618181818181818.1818E71",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234567890123456181818181818181.818E70",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345678901234561818181818181818.18E69",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"123456789012345618181818181818181.8E68",         "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"1234567890123456181818181818181818.0E67",        "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " }, 
              {"12345678901234561818181818181818180.0000001E66", "1.234567890123456181818181818181818E+100", null, "Various decimal shifts " },
              {"-123.4567890123456",           "-123.4567890123456",  null, "Negative number"}, 
              {"-123.4567890123456E-2",        "-1.234567890123456",     null, "Negative exponent"}, 
              {"-0000123.4567890123456",       "-123.4567890123456",  null, "Leading zeros"},
              {"-123.4567890123456E-002",      "-1.234567890123456",     null, "Exponent leading zeros"},
              {"1234567890123456789012345678901234567890123456789012345678901234",
                                               "1.234567890123456789012345678901235E+63",  null, "More than 63 coef digits"},
              {"1.234567890123456789012345678901234567890123456789012345678901234",
                                               "1.234567890123456789012345678901235",      null, "More than 63 coef digits with decimal point "},
              {"1234567890123456",             "1234567890123456",       null, "Without E"},              
              {"1.234567890123456",            "1.234567890123456",      null, "Without E"},              
              {"123456789012345.6",            "123456789012345.6",      null, "Without E"},              
              {"1234567890123456.0000000",     "1234567890123456.0000000",       null, "Without E"},              
          };


	  if (getJDK() <= JVMInfo.JDK_142) {
	      notApplicable("JDK 1.5 and later test"); 
	  } else {

	      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 && getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      } 


	      testDfp(JDPSTest.PSTEST_SETDFP34, testValues);
	  }
        }


   /**
     * NUMERIC version of Var046.
     *
     * set a NUMERIC(2,2) field where the data was truncated.
     * this causes problems for the native driver in V5R4, but didn't
     * appear in previous releases
     * The V5R4 PTR is 9A86093
     * 
     * This behavior is wrong.. jcc toolbox throws an error
     * Fixed in V5R5 by the native driver. 
     */

        public void Var065()
        {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE) && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
                notApplicable("Variation doesn't work in V5R3");
                return; 
            } 
            boolean oldBehavior =  ((isToolboxDriver() && getRelease() < JDTestDriver.RELEASE_V7R1M0)|| 
                    ((getDriver() == JDTestDriver.DRIVER_NATIVE) && 
                            (getRelease() < JDTestDriver.RELEASE_V7R1M0) ) );

            if (checkBigintSupport()) {
                try {
                    String tableName = JDPSTest.COLLECTION +".JDPSBD046"; 
                    try {
                        statement_.executeUpdate("DROP TABLE "+tableName);
                    } catch (Exception e) {
                        // e.printStackTrace(); 
                    } 
                    statement_.executeUpdate("CREATE TABLE "+tableName+" (c1 NUMERIC(2,2))");
                    statement_.executeUpdate("INSERT INTO "+tableName+" values(0.11)");

                    PreparedStatement ps;
                    ps = connection_.prepareStatement ( "select c1 from " + tableName + " where c1 =?");
                    ps.setBigDecimal (1, new BigDecimal ("1.11"));
                    ResultSet rs = ps.executeQuery();
                    String value; 
                    if (rs.next()) { 
                        value=rs.getString(1);
                    } else {
                        value="NO DATA FOUND"; 
                    }
                    rs.close(); 
                    // statement_.executeUpdate("DROP TABLE "+tableName);
                    if (oldBehavior) { 
                        assertCondition("0.11".equals(value), "value("+value+") != 0.11 -- added by native driver 12/15/2005");
                    } else {
                        failed("Did not throw exception for setting 1.11 as NUMERIC(2,2)"); 
                    }
                } catch (Exception e) {
                    if (oldBehavior) { 
                        failed (e, "Unexpected Exception -- added by native driver 12/15/2004 ");
                    } else {
                        String exceptionInfo = "Exception occurred during BigDecimal conversion";
                        if (getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver() || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
                            exceptionInfo="Data truncation"; 
                        } 
                        String exToString = e.toString(); 
                        if (exToString.indexOf(exceptionInfo) >= 0) { 
                            assertCondition(true); 
                        } else {
			    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
				assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox");

			    } else { 

				e.printStackTrace(); 
				failed("Unexpected Exception '"+exToString+"' expected '"+exceptionInfo+"'\n");
			    }
                        }

                    }
                }
            }
        }



/**
setBigDecimal() - Set an SQLXML parameter.
**/
	public void Var066()
	{
	    if (checkXmlSupport()) {
		try { 
		    PreparedStatement ps =
		      connection_.prepareStatement (
						    "INSERT INTO " +
						    JDPSTest.PSTEST_SETXML
						    + " VALUES (?)");
		    try {
			ps.setBigDecimal (1, new BigDecimal (-6.54));
			ps.executeUpdate(); 
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
setBigDecimal() - Set a boolean parameter.
**/
    public void Var067()
    {
        if (checkBooleanSupport()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BOOLEAN) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (1.0));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BOOLEAN FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

                assertCondition (check.longValue () == 1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setBigDecimal() - Set a boolean parameter.
**/
    public void Var068()
    {
        if (checkBooleanSupport()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BOOLEAN) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal (0.0));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BOOLEAN FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

                assertCondition (check.longValue () == 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a boolean parameter.
**/
    public void Var069()
    {
        if (checkBooleanSupport()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BOOLEAN) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal(2378.9));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BOOLEAN FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

                assertCondition (check.longValue () == 1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setBigDecimal() - Set a boolean parameter.
**/
    public void Var070()
    {
        if (checkBooleanSupport()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BOOLEAN) VALUES (?)");
                ps.setBigDecimal (1, new BigDecimal(-2378.9));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BOOLEAN FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

                assertCondition (check.longValue () == 1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


}
