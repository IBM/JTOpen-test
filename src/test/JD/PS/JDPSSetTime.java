///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetTime.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 
package test.JD.PS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.Calendar;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;



/**
Testcase JDPSSetTime.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setTime()
</ul>
**/
public class JDPSSetTime
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetTime";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Constants.
    private static final String PACKAGE             = "JDPSST";



    // Private data.
    private Connection          connection_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetTime (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetTime",
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
            
            + ";date format=iso";
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
setTime() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIME) VALUES (?)");
            ps.close ();
            ps.setTime (1, new Time (System.currentTimeMillis()));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setTime() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setTime (100, new Time(System.currentTimeMillis()));
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setTime() - Should throw exception when index is 0.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setTime (0, new Time(System.currentTimeMillis()));
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setTime() - Should throw exception when index is -1.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setTime (0, new Time(System.currentTimeMillis()));
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setTime() - Should set to SQL NULL when the value is null.
**/
    public void Var005()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIME) VALUES (?)");
            ps.setTime (1, null);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Time check = rs.getTime (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition ((check == null) && (wn == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setTime() - Should work with a valid parameter index
greater than 1.
**/
    public void Var006()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_KEY, C_TIME) VALUES (?, ?)");
            ps.setString (1, "Hola");
            Time t = new Time(System.currentTimeMillis());
            ps.setTime (2, t);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Time check = rs.getTime (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setTime() - Should throw exception when the calendar is null.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_TIME) VALUES (?)");
                ps.setTime (1, new Time(System.currentTimeMillis()), null);
                ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setTime() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var008()
    {
        try {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC permits setting of an output parameter");
            return; 
          } 
            PreparedStatement ps = connection_.prepareStatement (
                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            ps.setTime (2, new Time(System.currentTimeMillis()));
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


    public void testSetFailure(String columnName, Time time) {
	try {
	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + JDPSTest.PSTEST_SET
								 + " ("+columnName+") VALUES (?)");
	    ps.setTime (1, time);
	    ps.execute(); 
	    ps.close ();
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}

    }
/**
setTime() - Set a SMALLINT parameter.
**/
    public void Var009()
    {
testSetFailure("C_SMALLINT",new Time(System.currentTimeMillis()));
    }



/**
setTime() - Set a INTEGER parameter.
**/
    public void Var010()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER) VALUES (?)");
            ps.setTime (1, new Time(System.currentTimeMillis()));
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setTime() - Set a REAL parameter.
**/
    public void Var011()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_REAL) VALUES (?)");
            ps.setTime (1, new Time(System.currentTimeMillis()));
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setTime() - Set a FLOAT parameter.
**/
    public void Var012()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_FLOAT) VALUES (?)");
            ps.setTime (1, new Time(System.currentTimeMillis()));
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setTime() - Set a DOUBLE parameter.
**/
    public void Var013()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DOUBLE) VALUES (?)");
            ps.setTime (1, new Time(System.currentTimeMillis()));
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setTime() - Set a DECIMAL parameter.
**/
    public void Var014()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setTime (1, new Time(System.currentTimeMillis()));
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setTime() - Set a NUMERIC parameter.
**/
    public void Var015()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_NUMERIC_50) VALUES (?)");
            ps.setTime (1, new Time(System.currentTimeMillis()));
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setTime() - Set a CHAR(50) parameter.
**/
    public void Var016()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_CHAR_50) VALUES (?)");
            Time t = new Time(System.currentTimeMillis());
            ps.setTime (1, t);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Time check = rs.getTime (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setTime() - Set a VARCHAR(50) parameter.
**/
    public void Var017()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARCHAR_50) VALUES (?)");
            Time t = new Time(System.currentTimeMillis());
            ps.setTime (1, t);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Time check = rs.getTime (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setTime() - Set a CLOB parameter.
**/
    public void Var018()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_CLOB) VALUES (?)");
                ps.setTime (1, new Time(System.currentTimeMillis()));
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
setTime() - Set a DBCLOB parameter.
**/
    public void Var019()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DBCLOB) VALUES (?)");
                ps.setTime (1, new Time(System.currentTimeMillis()));
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
setTime() - Set a BINARY parameter.
**/
    public void Var020()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BINARY_20) VALUES (?)");
            ps.setTime (1, new Time(System.currentTimeMillis()));
	    ps.execute();
	    ps.close(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setTime() - Set a VARBINARY parameter.
**/
    public void Var021()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_VARBINARY_20) VALUES (?)");
            ps.setTime (1, new Time(System.currentTimeMillis()));
	    ps.execute();
	    ps.close(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setTime() - Set a BLOB parameter.
**/
    public void Var022()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BLOB) VALUES (?)");
            ps.setTime (1, new Time(System.currentTimeMillis()));
	    ps.execute(); 
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
setTime() - Set a DATE parameter.
**/
    public void Var023()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_DATE) VALUES (?)");
            ps.setTime (1, Time.valueOf("22:33:54"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            java.sql.Date check = rs.getDate (1);
            rs.close ();

            assertCondition (check.toString ().equals ("1970-01-01"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setTime() - Set a TIME parameter.
**/
    public void Var024()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIME) VALUES (?)");
            Time t = new Time(System.currentTimeMillis());
            ps.setTime (1, t);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Time check = rs.getTime (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setTime() - Set a TIME parameter, with a calendar specified.
**/
    public void Var025()
    {
        if (checkJdbc20 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);
    
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_TIME) VALUES (?)");
                Time t = new Time(System.currentTimeMillis());
                ps.setTime (1, t, Calendar.getInstance ());
                ps.executeUpdate ();
                ps.close ();
    
                ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                Time check = rs.getTime (1);
                rs.close ();
    
                assertCondition (check.toString ().equals (t.toString ()));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setTime() - Set a TIMESTAMP parameter.
**/
    public void Var026()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIMESTAMP) VALUES (?)");
            ps.setTime (1, Time.valueOf("05:33:44"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIMESTAMP FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            java.sql.Timestamp check = rs.getTimestamp (1);
            rs.close ();

            assertCondition (check.toString ().equals ("1970-01-01 05:33:44.0"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
setTime() - Set a DATALINK parameter.
**/
    public void Var027()
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
                ps.setTime (1, new Time(System.currentTimeMillis()));
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
setTime() - Set a DISTINCT parameter.
**/
    public void Var028()
    {
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DISTINCT) VALUES (?)");
                ps.setTime (1, new Time(System.currentTimeMillis()));
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
setTime() - Set a BIGINT parameter.
**/
    public void Var029()
    {
        if (checkBigintSupport()) {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_BIGINT) VALUES (?)");
            ps.setTime (1, new Time(System.currentTimeMillis()));
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
setTime() - Set a parameter in a statement that comes from the
package cache.
**/
    public void Var030()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_TIME) VALUES (?)";
            
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
            Time t = new Time(System.currentTimeMillis());
            ps.setTime (1, t);
            ps.executeUpdate ();
            ps.close ();
            c2.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            Time check = rs.getTime (1);
            rs.close ();            

            assertCondition (check.toString ().equals (t.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setTime() - Set a DECFLOAT parameter.
    **/
        public void Var031()
        {
            if (checkDecFloatSupport()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                    + " VALUES (?)");
                ps.setTime (1, new Time(System.currentTimeMillis()));
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
        setTime() - Set a DECFLOAT parameter.
        **/
            public void Var032()
            {
                if (checkDecFloatSupport()) {
                try {
                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                        + " VALUES (?)");
                    ps.setTime (1, new Time(System.currentTimeMillis()));
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
        setTime() - Set an xml parameter.
        **/
            public void Var033()
            {
                if (checkXmlSupport()) {
		    try {
			PreparedStatement ps = connection_.prepareStatement (
									     "INSERT INTO " +
									     JDPSTest.PSTEST_SETXML
									     + " VALUES (?)");
			try { 
			    ps.setTime (1, new Time(System.currentTimeMillis()));
			    ps.executeUpdate();
			    ps.close ();
			    if (isToolboxDriver())
			        succeeded();
			    else
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


/**
setTime() - Set a  BOOLEAN parameter.
**/
    public void Var034()
    {
testSetFailure("C_BOOLEAN",new Time(System.currentTimeMillis()));
    }




}



