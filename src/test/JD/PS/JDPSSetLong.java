///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetLong.java
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
// File Name:    JDPSSetLong.java
//
// Classes:      JDPSSetLong
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
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;



/**
Testcase JDPSetLong.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setLong()
</ul>
**/
public class JDPSSetLong
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetLong";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Constants.
    private static final String PACKAGE             = "JDPSSL";



    // Private data.
    private Connection          connection_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetLong (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetLong",
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
setLong() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            ps.close ();
            ps.setLong (1, (long) 533);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setLong() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setLong (100, (long) 334);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setLong() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setLong (0, (long) -385);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setLong() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setLong (-1, (long) 943);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setLong() - Should work with a valid parameter index
greater than 1.
**/
    public void Var005()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_KEY, C_INTEGER) VALUES (?, ?)");
            ps.setString (1, "Test");
            ps.setLong (2, -43423);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == -43423);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Should throw exception when the parameter is
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
            ps.setLong (2, (long) -23322);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setLong() - Set a SMALLINT parameter.
**/
    public void Var007()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.setLong (1, 4265);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == 4265);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Set a SMALLINT parameter, when the integer is too large.
**/
    public void Var008()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_SMALLINT) VALUES (?)");
            ps.setLong (1, 212234243);
            // Note:  jcc doesn't throw the exception until the execute
            ps.executeUpdate(); 
            failed ("Didn't throw SQLException when setting small int to 212234243");
        }
        catch (Exception e) {

		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation for all drivers");


        }
    }



/**
setLong() - Set an INTEGER parameter.
**/
    public void Var009()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            ps.setLong (1, 30613);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == 30613);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Set a INTEGER parameter, when the long is too large.
This should throw a DataTruncation exception.
**/
    public void Var010()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            ps.setLong (1, 1234567890123456L);
            // Note:  jcc doesn't throw the exception until the execute
            ps.executeUpdate(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "JTopenlite Data type mismatch");
        }
    }



/**
setLong() - Set an REAL parameter.
**/
    public void Var011()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_REAL) VALUES (?)");
            ps.setLong (1, -17922);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == -17922, "Got "+check+" sb -17922");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Set an FLOAT parameter.
**/
    public void Var012()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_FLOAT) VALUES (?)");
            ps.setLong (1, 1243);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
	    String s = rs.getString(1); 
            rs.close ();

            assertCondition (check ==  1243, "Got "+check+" for "+s+" sb 1243");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Set an DOUBLE parameter.
**/
    public void Var013()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DOUBLE) VALUES (?)");
            ps.setLong (1, 122290);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
	    String s = rs.getString(1); 
            rs.close ();

            assertCondition (check == 122290, "got "+check+" for "+s+" sb 122290");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Set an DECIMAL parameter.
**/
    public void Var014()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setLong (1, 40012);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == 40012);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Set an DECIMAL parameter, when the value is too big.
**/
    public void Var015()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_50) VALUES (?)");
            ps.setLong (1, 1032122134);
            // Note:  jcc doesn't throw the exception until the execute
            ps.executeUpdate(); 
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
setLong() - Set an NUMERIC parameter.
**/
    public void Var016()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_50) VALUES (?)");
            ps.setLong (1, -2777);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == -2777);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Set an NUMERIC parameter, when the value is too big

SQL400 - the Native JDBC driver reports trunction information based on
the precision - scale size of the column and value.  Not the total size
of the values because scale values will be silently truncated.
**/
    public void Var017()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_105) VALUES (?)");
            ps.setLong (1, -234322421733234L);
            // Note:  jcc doesn't throw the exception until the execute
            ps.executeUpdate(); 
            failed ("Didn't throw SQLException");
	}
	catch (Exception e) {

	    if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    assertSqlException(e, 0, "22001", "Data truncation", "JTopenlite Data truncation");

	    } else { 
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

	    } else { 

		if (e instanceof DataTruncation) { 
		    DataTruncation dt = (DataTruncation)e;
		    if (isToolboxDriver())
			assertCondition ((dt.getIndex() == 1)
					 && (dt.getParameter() == true)
					 && (dt.getRead() == false)
					 && (dt.getTransferSize() == 10));
		    else {
			if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 49160) {
			    notApplicable("Native driver fix in PTF V7R1 SI49160" );
			    return; 
			}

			assertCondition ((dt.getIndex() == 1)
					 && (dt.getParameter() == true)
					 && (dt.getRead() == false)
					 && (dt.getTransferSize() == 10)
					 && (dt.getDataSize() == 20), "Native testcases updated for V7R1 on 1/19/2013. dt.getTransferSize() = "+dt.getTransferSize()+" sb 10 "+
					 "dt.getDataSize()="+dt.getDataSize()+"sb 19" );
		    }
		} else {

		    failed(e, "Expected DataTruncation exception"); 
		}
	    }
	    }
        }
    }



/**
setLong() - Set an CHAR(50) parameter.
**/
    public void Var018()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_50) VALUES (?)");
            ps.setLong (1, -18427);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == -18427);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Set an CHAR(1) parameter.
**/
    public void Var019()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_1) VALUES (?)");
            ps.setLong (1, 9);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == 9);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Set an CHAR(1) parameter, when the value is too big.
**/
    public void Var020()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_1) VALUES (?)");
            ps.setLong (1, -2233);
            // Note:  jcc doesn't throw the exception until the execute
            ps.executeUpdate(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
        }
    }



/**
setLong() - Set an VARCHAR parameter.
**/
    public void Var021()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            ps.setLong (1, 87667);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == 87667);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLong() - Set a CLOB parameter.
**/
    public void Var022()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_CLOB) VALUES (?)");
                ps.setLong (1, 5423);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setLong() - Set a DBCLOB parameter.
**/
    public void Var023()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DBCLOB) VALUES (?)");
                ps.setLong (1, 5432);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setLong() - Set a BINARY parameter.
**/
    public void Var024()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BINARY_20) VALUES (?)");
            ps.setLong (1, 5454);
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }




/**
setLong() - Set a VARBINARY parameter.
**/
    public void Var025()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARBINARY_20) VALUES (?)");
            ps.setLong (1, 1944);
            ps.executeUpdate ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setLong() - Set a BLOB parameter.
**/
    public void Var026()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BLOB) VALUES (?)");
            ps.setLong (1, -544);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setLong() - Set a DATE parameter.
**/
    public void Var027()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DATE) VALUES (?)");
            ps.setLong (1, -756);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setLong() - Set a TIME parameter.
**/
    public void Var028()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIME) VALUES (?)");
            ps.setLong (1, 0);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setLong() - Set a TIMESTAMP parameter.
**/
    public void Var029()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIMESTAMP) VALUES (?)");
            ps.setLong (1, 454543);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
setLong() - Set a DATALINK parameter.
**/
    public void Var030()
    {
        if (checkDatalinkSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DATALINK) VALUES (?)");
                ps.setLong (1, -755);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setLong() - Set a DISTINCT parameter.
**/
    // @C0C
    public void Var031()
    {
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DISTINCT) VALUES (?)");
                ps.setLong (1, 9);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                long check = rs.getLong (1);
                rs.close ();

                assertCondition (check == 9);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setLong() - Set a BIGINT parameter.
**/
    public void Var032()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setLong (1, -30361321213L);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == -30361321213L);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
setLong() - Set a parameter in a statement that comes from the
package cache.

SQL400 - JDSetupPackage cache doesn't have enough information to be
         driver neutral today.
**/
    public void Var033()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("package cache test");
        return; 
      }
        try {
            String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)";

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
            ps.setLong (1, 98213);
            ps.executeUpdate ();
            ps.close ();
            c2.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();

            assertCondition (check == 98213);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
// @D1a
setLong() - Set a BIGINT parameter.
// 01/14/2002 -- BigInt changed back.  We now throw an exception.
// 11/13/2002 -- Changed BigInt to match all of our other integer
//               types, by chopping any decimal places.
**/
    public void Var034()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "-303613.1213");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
	    rs.close ();
	    assertCondition (check == -303613);
            // failed("no exception");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception - Toolbox changed testcase 11/13/2002"); 
            // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }
 
  public void Var035()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "1.56E10");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();
	    assertCondition (check == 15600000000L, "native added -- make sure exponent is handled");
            // failed("no exception");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception - Toolbox changed testcase 11/13/2002"); 
            // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }

 public void Var036()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "9223372036854775708");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();
	    assertCondition (check == 9223372036854775708L, "native change -- handle large number correctly ");
            // failed("no exception");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception - Toolbox changed testcase 11/13/2002"); 
            // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }

 public void Var037()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "112.32v3");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();
	    failed("no exception -- native added invalid stuff to right of decimal "+check);
        }
        catch (Exception e) 
        {
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }

 public void Var038()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "9223372036854775708.987");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();
	    assertCondition (check == 9223372036854775708L, "Native change to handle truncating large numbers"); 
            // failed("no exception");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception - Toolbox changed testcase 11/13/2002"); 
            // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }

public void Var039()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "1.56e10");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();
	    assertCondition (check == 15600000000L, "native added -- make sure exponent is handled");
            // failed("no exception");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception - Toolbox changed testcase 11/13/2002"); 
            // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }

  public void Var040()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "156E10");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();
	    assertCondition (check == 1560000000000L, "native added -- make sure exponent is handled");
            // failed("no exception");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception - Toolbox changed testcase 11/13/2002"); 
            // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


 public void Var041()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "112e23garb");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();
	    failed("no exception -- native added invalid stuff to right of decimal "+check);
        }
        catch (Exception e) 
        {
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }

 public void Var042()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "112E23garb");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();
	    failed("no exception -- native added invalid stuff to right of decimal "+check);
        }
        catch (Exception e) 
        {
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }

 public void Var043()
    {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "112garb32");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();
	    failed("no exception -- native added invalid stuff to right of decimal "+check);
        }
        catch (Exception e) 
        {
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }

  /**
   * pass a value that is too big to be a valid string
   */

  public void Var044()
  {
        if (checkBigintSupport()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setString (1, "9223372036854775808");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            long check = rs.getLong (1);
            rs.close ();
	    failed("no exception -- a value should not have been returned because 9223372036854775808 cannot be represented as a long -- the value "+check+" was returned -- added by native driver 07/29/2004");
        }
        catch (Exception e) 
        {
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
      
  } 

  /**
  setLong() - Set a DECFLOAT parameter.
  **/
      public void Var045()
      {
        
        if (checkDecFloatSupport()) { 
          try {
              long value = 1234567890123456L;
              statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

              PreparedStatement ps = connection_.prepareStatement (
                  "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                  + "  VALUES (?)");
              ps.setLong (1, value);
              ps.executeUpdate ();
              ps.close ();

              ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
              rs.next ();
              long check = rs.getLong (1);
              rs.close ();

              assertCondition (check == value, "got "+check+" sb "+value);
          }
          catch (Exception e) {
              failed (e, "Unexpected Exception");
          }
      }
      }


  /**
  setLong() - Set an DECFLOAT parameter, when the value is too big
              Note:  This does not throw an error since the value can
              be represented.  Only precision is lost. 
              
              Note:  This passes when using the jcc driver running to 
              Z/OS (04/11/2007) .  
  **/
      public void Var046()
      {
        if (checkDecFloatSupport()) { 
          try {
            long value = -Long.MIN_VALUE;
            // Long value is really -9 223372036854775808 
            String expectedValue = "-9.223372036854776E+18";

            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);
            
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                + "  VALUES (?)");
            ps.setLong (1, value);
            ps.executeUpdate ();
            ps.close ();
            
            ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
            rs.next ();
            String check = rs.getString (1);
            rs.close ();
            
            assertCondition (expectedValue.equals(check), "got "+check+" sb "+expectedValue);
          }
          catch (Exception e) {
            //toolbox behavior
            if( isToolboxDriver())
            {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
                return;
            }
            failed (e, "Unexpected Exception");
          }
        }
      }
      /**
      setLong() - Set a DECFLOAT parameter.
      **/
          public void Var047()
          {
            if (checkDecFloatSupport()) { 
              try {
                long value = Long.MAX_VALUE; 
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);
                
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                    + "  VALUES (?)");
                ps.setLong (1, value);
                ps.executeUpdate ();
                ps.close ();
                
                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
                rs.next ();
                long check = rs.getLong (1);
                rs.close ();
                
                assertCondition (check == value, "got "+check+" sb "+value);
              }
              catch (Exception e) {
                failed (e, "Unexpected Exception");
              }
            }
          }



/**
setLong() - Set a SQLXML parameter.
**/
    public void Var048()
    {
        if (checkXmlSupport ()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " +
								     JDPSTest.PSTEST_SETXML
								     + " VALUES (?)");
		try { 
		    ps.setLong (1, -755);
		    ps.executeUpdate(); ps.close ();
		    failed ("Didn't throw SQLException");
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    } catch (Exception e) {
		failed(e, "Unexpected Exception"); 
	    }
        }
    }

    /* Check more string values that should work */ 
    public void Var049()
    {
	String[][] values = {
	    {"-303613.1213", "-303613"},
	    {"9007199254740992.7", "9007199254740992"},
	    {"9007199254740993.7", "9007199254740993"},
	    {"9007199254740994.7", "9007199254740994"},
	    {"9007199254740995.7", "9007199254740995"},
	    {"9007199254740996.7", "9007199254740996"},
	    {"9007199254740997.7", "9007199254740997"},
	    {"9007199254740998.7", "9007199254740998"}, 
	    {"-9007199254740992.7", "-9007199254740992"},
	    {"-9007199254740993.7", "-9007199254740993"},
	    {"-9007199254740994.7", "-9007199254740994"},
	    {"-9007199254740995.7", "-9007199254740995"},
	    {"-9007199254740996.7", "-9007199254740996"},
	    {"-9007199254740997.7", "-9007199254740997"},
	    {"-9007199254740998.7", "-9007199254740998"}, 
	    {"229007199254740992.7", "229007199254740992"},
	    {"229007199254740993.7", "229007199254740993"},
	    {"229007199254740994.7", "229007199254740994"},
	    {"229007199254740995.7", "229007199254740995"},
	    {"229007199254740996.7", "229007199254740996"},
	    {"229007199254740997.7", "229007199254740997"},
	    {"229007199254740998.7", "229007199254740998"}, 
	    {"-229007199254740992.7", "-229007199254740992"},
	    {"-229007199254740993.7", "-229007199254740993"},
	    {"-229007199254740994.7", "-229007199254740994"},
	    {"-229007199254740995.7", "-229007199254740995"},
	    {"-229007199254740996.7", "-229007199254740996"},
	    {"-229007199254740997.7", "-229007199254740997"},
	    {"-229007199254740998.7", "-229007199254740998"},
/* Largest possible value */
	    {""+Long.MAX_VALUE, ""+Long.MAX_VALUE,},
	    {""+Long.MIN_VALUE, ""+Long.MIN_VALUE,},
	    {""+Long.MAX_VALUE+".234234", ""+Long.MAX_VALUE,},
	    {""+Long.MIN_VALUE+".234234", ""+Long.MIN_VALUE,},
	};
	StringBuffer sb = new StringBuffer();
	boolean passed = true;

        if (checkBigintSupport()) {
        try {
	    for (int i = 0; i < values.length; i++) {

		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BIGINT) VALUES (?)");
		ps.setString (1, values[i][0]);
		ps.executeUpdate ();
		ps.close ();


		ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		long check = rs.getLong (1);
		if (values[i][1].equals(""+check)) {
		    sb.append("From "+values[i][0]+" got "+values[i][1]+"\n");

		} else {
		    sb.append("ERROR:  From "+values[i][0]+" got "+check+" sb "+values[i][1]+"\n");
		    passed = false; 
		} 
		
		rs.close ();
	    }


	    assertCondition (passed, "New testcase 05/10/2012 "+sb.toString());
            // failed("no exception");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception - New testcase 05/10/2012\n"+sb.toString()); 
            // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


    /**
     * setInt() - Set an parameter for a column of a specified type.
     **/
    public void testSetLong(String columnName, long value, String outputValue) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
        ps.setLong(1, value);
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

    public void Var050() {
      if (checkBooleanSupport()) {
        testSetLong("C_BOOLEAN", 1, "1");
      }
    }

    public void Var051() {
      if (checkBooleanSupport()) {
        testSetLong("C_BOOLEAN", 0, "0");
      }
    }

    public void Var052() {
      if (checkBooleanSupport()) {
        testSetLong("C_BOOLEAN", 101, "1");
      }
    }

    public void Var053() {
      if (checkBooleanSupport()) {
        testSetLong("C_BOOLEAN", -1, "1");
      }
    }

    public void Var054() {
      if (checkBooleanSupport()) {
        testSetLong("C_BOOLEAN", Long.MAX_VALUE, "1");
      }
    }

    public void Var055() {
      if (checkBooleanSupport()) {
        testSetLong("C_BOOLEAN", Long.MIN_VALUE, "1");
      }
    }




}
