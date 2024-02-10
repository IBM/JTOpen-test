///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementExecute.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementExecute.java
//
// Classes:      JDStatementExecute
//
////////////////////////////////////////////////////////////////////////
package test.JD.Statement;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDStatementExecute.  This tests the following methods
of the JDBC Statement class:

<ul>
<li>executeUpdate()</li>
<li>executeQuery()</li>
<li>execute()</li>
<li>executeLargeUpdate()</li>
</ul>
**/
public class JDStatementExecute
extends JDTestcase {


    // Private data.
    private static  String table_  = JDStatementTest.COLLECTION + ".JDSE";
    private static  String table2_  = JDStatementTest.COLLECTION + ".JDSE2";
    private static  String table3_  = JDStatementTest.COLLECTION + ".JDSE3";
    private static  String table4_  = JDStatementTest.COLLECTION + ".JDSE4";  // 30 Char column name table

    private Connection      connection_;
    private Connection      connection2_;



/**
Constructor.
**/
    public JDStatementExecute (AS400 systemObject,
                               Hashtable namesAndVars,
                               int runMode,
                               FileOutputStream fileOutputStream,
                               
                               String password)
    {
        super (systemObject, "JDStatementExecute",
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
        connection_ = testDriver_.getConnection (baseURL_ + ";errors=full",
                                                 userId_, encryptedPassword_);

        connection2_ = testDriver_.getConnection (baseURL_ + ";errors=full;reuse objects=false",
                                                  userId_, encryptedPassword_);

        Statement s = connection_.createStatement ();
        table_  = JDStatementTest.COLLECTION + ".JDSE";
        table2_  = JDStatementTest.COLLECTION + ".JDSE2";
        table3_  = JDStatementTest.COLLECTION + ".JDSE3";
        table4_  = JDStatementTest.COLLECTION + ".JDSE4";  // 30 Char column name table

	initTable( s, table_
                         , " (NAME VARCHAR(10), ID INT, SCORE INT)");
        s.executeUpdate ("INSERT INTO " + table_
                         + " (NAME, ID) VALUES ('cnock', 1)");
        s.executeUpdate ("INSERT INTO " + table_
                         + " (NAME, ID) VALUES ('murch', 2)");
        s.executeUpdate ("INSERT INTO " + table_
                         + " (NAME, ID) VALUES ('joshvt', 3)");
        s.executeUpdate ("INSERT INTO " + table_
                         + " (NAME, ID) VALUES ('robb', -1)");

            JDSetupProcedure.create (systemObject_,connection_,
                                     JDSetupProcedure.STP_RS0, supportedFeatures_, collection_);
            JDSetupProcedure.create (systemObject_,connection_,
                                     JDSetupProcedure.STP_RS1, supportedFeatures_, collection_);
            JDSetupProcedure.create (systemObject_,connection_,
                                     JDSetupProcedure.STP_RS3, supportedFeatures_, collection_);
        connection_.commit(); // for xa

        s.close ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        Statement s = connection_.createStatement ();
        s.executeUpdate ("DROP TABLE " + table_);
        s.close ();
        connection_.commit(); // for xa
        connection_.close ();

        connection2_.close ();
    }

/**
Set a warning for the native driver
*/
    private void setNativeWarning(Statement s) {
	// Use reflection to set a Warning
	// Reflection must be used to permit the toolbox to compile
	try {
	    SQLWarning warning = new SQLWarning("This is a warning");

	    Class db2Statement = Class.forName("com.ibm.db2.jdbc.app.DB2Statement");
	    Class[]  args  = new Class[1];
	    args[0] = Class.forName("java.sql.SQLWarning");
	    java.lang.reflect.Method method = db2Statement.getMethod("addWarning", args);
	    Object[] parms = new Object[1];
	    parms[0] = warning;
	    method.invoke(s, parms);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }



/**
executeUpdate() - Pass a null, should throw an exception.
**/
    public void Var001()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate (null);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Pass an empty string, should throw an exception.
**/
    public void Var002()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Pass a blank string, should throw an exception.
**/
    public void Var003()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate (" ");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Execute an update with a closed statement.
Should throw an exception.
**/
    public void Var004()
    {
        try {
            Statement s = connection_.createStatement ();
            s.close ();
            s.executeUpdate ("UPDATE " + table_
                             + " SET SCORE=5 WHERE ID > 10");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Pass a query that returns a result set.
Should throw an exception.
<P>
SQL400 - As of v4r5, the native driver is going to stop throwing
exceptions for this behavior and simple let it work.  We will test
that the ResultSet can be obtained with getResultSet() and that it
is valid.  As of v5r1 the native driver is going to begin throwing
exceptions for this behavior so we are compliant with J2EE.
**/
    public void Var005()
    {
        try {
            Statement s = connection_.createStatement ();
            int updateCount = s.executeUpdate ("SELECT * FROM QIWS.QCUSTCDT");
                failed ("Didn't throw SQLException for executeUpdate on a query. but got "+updateCount);
            }
        catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Calls a stored procedure that returns a result set.
Should throw an exception.

**/
    public void Var006()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("CALL "                           // @C1C - changed back to executeUpdate()
                             + JDSetupProcedure.STP_RS1);      //        since that is specifically what
                                                               //        this variation is for.
                failed ("Didn't throw SQLException for running executeUpdate on a stored procedure that returns result set");
            }
        catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Pass a statement with parameter markers.
Should throw an exception.
**/
    public void Var007()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("UPDATE " + table_
                             + " SET SCORE=? WHERE ID > 1");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    // Check the exception that comes back.
	    // Prior to 8/4/2010, the toolbox driver detected the error and threw
	    // a generic parameter mismatch error.  This causes problems for
	    // users running through ops native -- See CPS discussion 874LTT for more
	    // information.
          int expectedSqlcode=-313;
          String expectedSqlstate="07001";
          String expectedSqlmessage="Number of host variables not valid.";

	  //
	  // For the native driver, the CLI is not properly returning an error for some cases.
	  // This should be fixed in V7R2.
	  //
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() < JDTestDriver.RELEASE_V7R2M0) {

	      if (e instanceof SQLException) {
		  if (((SQLException)e). getErrorCode() == -99999) {
		      expectedSqlcode=-99999;
		      expectedSqlstate="JDBC\u0000";
		      expectedSqlmessage="Internal Error:  CLI signalled an error condition and had no error data available.";
		  }
	      }
	  }
          assertSqlException(e, expectedSqlcode, expectedSqlstate, expectedSqlmessage, "Expected syntax error for parameter marker  -- updated 8/5/2010");
        }
    }



/**
executeUpdate() - Verify that the update count returned
is correct when no rows are updated.
**/
    public void Var008()
    {
        try {
            Statement s = connection_.createStatement ();
            int updateCount = s.executeUpdate ("UPDATE " + table_
                                               + " SET SCORE=5 WHERE ID > 10");
            s.close ();
            assertCondition (updateCount == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeUpdate() - Verify that the update count returned
is correct when some rows are updated.
**/
    public void Var009()
    {
        try {
            Statement s = connection_.createStatement ();
            int updateCount = s.executeUpdate ("UPDATE " + table_
                                               + " SET SCORE=5 WHERE ID > 1");
            s.close ();
            assertCondition (updateCount == 2);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeUpdate() - Verify that the update count returned
is correct when a stored procedure is called.
**/
    public void Var010()
    {
        try {
            Statement s = connection_.createStatement ();
            int updateCount = s.executeUpdate ("CALL "
                                               + JDSetupProcedure.STP_RS0);
            s.close ();
            assertCondition (updateCount == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeUpdate() - Verify that a previous result set is
closed.
**/
    public void Var011()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.next ();
            s.executeUpdate ("UPDATE " + table_
                             + " SET SCORE=5 WHERE ID > 10");

            boolean success = false;
            try {
                rs.next ();
            }
            catch (SQLException e) {
                success = true;
            }

            s.close ();
            assertCondition (success, "Result set was not closed by subsequent update");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeUpdate() - Verify that previous warnings are cleared.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            if (( isToolboxDriver() ||
		  getDriver () == JDTestDriver.DRIVER_JTOPENLITE) &&
                getRelease() >= JDTestDriver.RELEASE_V5R1M0 )
            {
                try {
                    Statement s = connection_.createStatement();
                    s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
                    SQLWarning before = s.getWarnings ();
                    s.executeUpdate ("UPDATE " + table_ + " SET SCORE=5 WHERE ID = -1");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null), "before="+before+" sb !=null after="+after+" sb null");
                }
                catch (Exception e) {
                    failed(e, "Unexpected Exception");
                }
            }
            else if (getDriver () == JDTestDriver.DRIVER_NATIVE &&
                getRelease() >= JDTestDriver.RELEASE_V5R1M0 )
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
		    setNativeWarning(s);
                    SQLWarning before = s.getWarnings ();
                    rs.close ();
                    s.executeUpdate ("UPDATE " + table_ + " SET SCORE=5 WHERE ID = -1");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }

	    }
            else
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                    SQLWarning before = s.getWarnings ();
                    rs.close ();
                    s.executeUpdate ("UPDATE " + table_ + " SET SCORE=5 WHERE ID = -1");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
executeQuery() - Pass a null, should throw an exception.
**/
    public void Var013()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery (null);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Pass an empty string, should throw an exception.
**/
    public void Var014()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Execute an query with a closed statement.
Should throw an exception.
**/
    public void Var015()
    {
        try {
            Statement s = connection_.createStatement ();
            s.close ();
            s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Pass an update that does not return a result set.
Should throw an exception.
<P>
SQL400 - As of v4r5, the native driver is going to stop throwing
exceptions for this behavior and simple let it work.  We will test
that the ResultSet can be obtained with getResultSet() and that it
is valid.  As of v5r1 the native driver is going to begin throwing
exceptions for this behavior so we are compliant with J2EE.
**/
    public void Var016()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("UPDATE " + table_
                            + " SET SCORE=5 WHERE ID > 10");
                failed ("Didn't throw SQLException");
            }
        catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Calls a stored procedure that does not return
a result set.  Should throw an exception.
**/
    public void Var017()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("CALL " + JDSetupProcedure.STP_RS0);
                failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Pass a statement with parameter markers.
Should throw an exception.
**/
    public void Var018()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT LSTNAM = ?");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {

          int expectedSqlcode=-104;
          String expectedSqlstate="42601";
          String expectedSqlmessage="Token = was not valid";
          assertSqlException(e, expectedSqlcode, expectedSqlstate, expectedSqlmessage, "Expected syntax error for parameter marker  -- updated 8/5/2010");
        }
    }



/**
executeQuery() - Verify that the result set is returned.
**/
    public void Var019()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check = rs.next ();
            rs.close ();
            s.close ();
            assertCondition (check);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeQuery() - Verify that the result set returned
is correct when a stored procedure is called that returns 1
result set.
**/
    public void Var020()
    {
	StringBuffer sb = new StringBuffer("Verify that the result set returned is correct when a stored procedure is called that returns 1 result set.");
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("CALL " + JDSetupProcedure.STP_RS1);
            boolean check = rs.next ();
            rs.close ();
            s.close ();
            assertCondition (check, sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+sb);
        }
    }



/**
executeQuery() - Verify that the result set returned
is correct when a stored procedure is called that returns multiple
result sets.
**/
    public void Var021()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("CALL " + JDSetupProcedure.STP_RS3);
            boolean check = rs.next ();
            rs.close ();
            s.close ();
            assertCondition (check);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeQuery() - Verify that a previous result set is
closed.

SQL400 - We added a property for whether or not to use result sets and other
objects.  This property is set to reuse the objects by default.  Therefore,
what is being attempted below should work.  See variation 41 for a test to
check what this test used to.
**/
    public void Var022()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.next ();
            s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");

            boolean success = false;
            try {
                rs.next ();
            }
            catch (SQLException e) {
                success = true;
            }

            s.close ();
            if (getDriver() == JDTestDriver.DRIVER_NATIVE)
                assertCondition (!success);
            else
                assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
executeQuery() - Verify that previous warnings are cleared.
**/
    public void Var023()
    {
        if (checkJdbc20 ()) {
            if (isToolboxDriver() &&
                getRelease() >= JDTestDriver.RELEASE_V5R1M0 )
            {
                try {
                    //TB v7r1 returns warning if statement type does not match cursor type returned from host
                    Statement s = connection_.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
                    SQLWarning before = s.getWarnings ();
                    s.executeQuery ("SELECT * FROM " + table_ + " FOR UPDATE");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed(e, "Unexpected Exception");
                }
            }
            else if (getDriver () == JDTestDriver.DRIVER_NATIVE &&
                getRelease() >= JDTestDriver.RELEASE_V5R1M0 )
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                               ResultSet.CONCUR_UPDATABLE);
                    /* ResultSet rs = */ s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
		    setNativeWarning(s);
                    SQLWarning before = s.getWarnings ();
                    s.executeQuery ("SELECT * FROM " + table_ + " FOR UPDATE");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }

	    }
            else
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                    SQLWarning before = s.getWarnings ();
                    s.executeQuery ("SELECT * FROM " + table_ + " FOR UPDATE");
                    SQLWarning after = s.getWarnings ();
                    rs.close();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
executeQuery() - Execute a read only query using a read only statement.
**/
    public void Var024()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                           ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                int concurrency = rs.getConcurrency ();
                SQLWarning w = s.getWarnings ();
                rs.close ();
                s.close ();
                assertCondition ((w == null) && (concurrency == ResultSet.CONCUR_READ_ONLY));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeQuery() - Execute an updatable query using a read only statement.
**/
    public void Var025()
    {
        if (checkJdbc20 ()) {
            try {

                Statement s = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                           ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT FOR UPDATE");
                int concurrency = rs.getConcurrency ();
                SQLWarning w = s.getWarnings ();
                rs.close ();
                s.close ();

                //TB v7r1 cursor info is actual info from host
                if (isToolboxDriver() &&
                    getRelease() >= JDTestDriver.RELEASE_V7R1M0 )
                    assertCondition ((w == null) && (concurrency == ResultSet.CONCUR_UPDATABLE), "concurrency="+concurrency+" sb CONCUR_UPDATABLE="+ResultSet.CONCUR_UPDATABLE+" Updated for V7R1");
                else
                    assertCondition ((w == null) && (concurrency == ResultSet.CONCUR_READ_ONLY));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeQuery() - Execute a read only query using an updatable statement.
**/
    public void Var026()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                           ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                int concurrency = rs.getConcurrency ();
                SQLWarning w = s.getWarnings ();
                rs.close ();
                s.close ();
                // As of v5r2 or v5r1 with a host server PTF, we no longer switch concurrency.
                // We can actually support what the user passed in.  That means that
                // we also no longer warn that we changed concurrency.
                if ((isToolboxDriver() ||
                     getDriver () == JDTestDriver.DRIVER_NATIVE ) &&
                    getRelease() >= JDTestDriver.RELEASE_V5R1M0 )
                {
                    assertCondition ((w == null) && (concurrency == ResultSet.CONCUR_UPDATABLE));
                }
                else
                    assertCondition ((w != null) && (concurrency == ResultSet.CONCUR_READ_ONLY));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeQuery() - Execute an updatable query using an updatable statement.
**/
    public void Var027()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                           ResultSet.CONCUR_UPDATABLE);
                connection_.commit();
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT FOR UPDATE");
                int concurrency = rs.getConcurrency ();
                SQLWarning w = s.getWarnings ();
                rs.close ();
                s.close ();
                assertCondition ((w == null) && (concurrency == ResultSet.CONCUR_UPDATABLE));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeQuery() - Execute an query with an empty result set.  Verify that
the statement is still usable.  This test is here because of a specific
bug that was in a previous release of the Toolbox JDBC driver.
**/
    public void Var028()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE CUSNUM=-999");
            boolean check1 = rs.next ();
            ResultSet rs2 = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check2 = rs2.next ();
            rs2.close ();
            s.close ();
            assertCondition ((check1 == false) && (check2 == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Pass a null, should throw an exception.
**/
    public void Var029()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute (null);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
execute() - Pass an empty string, should throw an exception.
**/
    public void Var030()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
execute() - Execute an statement with a closed statement.
Should throw an exception.
**/
    public void Var031()
    {
        try {
            Statement s = connection_.createStatement ();
            s.close ();
            s.execute ("UPDATE " + table_
                       + " SET SCORE=5 WHERE ID > 10");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
execute() - Pass a statement with parameter markers.
Should throw an exception.

Updated 05/27/2009 to verify that exception is not a generic exception for CLI

**/
    public void Var032()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("UPDATE " + table_
                       + " SET SCORE=? WHERE ID > 1");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&
		getDriver() == JDTestDriver.DRIVER_NATIVE) {
		String expectedException = "Number of host variables not valid";
		String exceptionMessage = e.toString();
		assertCondition(exceptionMessage.indexOf(expectedException) >= 0,
				"Got "+exceptionMessage+" but expected "+expectedException+" -- modified 5/28/2009 by native driver to detect CLI bug in V7R1");

	    } else {

	          int expectedSqlcode=-313;
	          String expectedSqlstate="07001";
	          String expectedSqlmessage="Number of host variables not valid";


	  //
	  // For the native driver, the CLI is not properly returning an error in earlier releases
          // (see above check).
	  //
		  if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		      expectedSqlcode=-99999;
		      expectedSqlstate="JDBC\u0000";
		      expectedSqlmessage="Internal Error:  CLI signalled an error condition and had no error data available.";
		  }


	          assertSqlException(e, expectedSqlcode, expectedSqlstate, expectedSqlmessage, "Expected syntax error for parameter marker -- updated 8/5/2010");

	    }
        }
    }



/**
execute() - Executes a query.  Should return true.
**/
    public void Var033()
    {
        try {
            Statement s = connection_.createStatement ();
            boolean flag = s.execute ("SELECT * FROM QIWS.QCUSTCDT");
            s.close ();
            assertCondition (flag == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Calls a stored procedure that returns 1
result set.  Should return true.
**/
    public void Var034()
    {
        try {
            Statement s = connection_.createStatement ();
            boolean flag = s.execute ("CALL "
                                      + JDSetupProcedure.STP_RS1);
            s.close ();
            assertCondition (flag == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Calls a stored procedure that returns multiple
result sets.  Should return true.
**/
    public void Var035()
    {
        try {
            Statement s = connection_.createStatement ();
            boolean flag = s.execute ("CALL "
                                      + JDSetupProcedure.STP_RS3);
            s.close ();
            assertCondition (flag == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Executes an update where no rows are updated.
Should return false.
**/
    public void Var036()
    {
        try {
            Statement s = connection_.createStatement ();
            boolean flag = s.execute ("UPDATE " + table_
                                      + " SET SCORE=5 WHERE ID > 10");
            s.close ();
            assertCondition (flag == false, "execute(update) returned true, sb false because no result set was returned ");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Executes an update where some rows are updated.
Should return false.
**/
    public void Var037()
    {
        try {
            Statement s = connection_.createStatement ();
            boolean flag = s.execute ("UPDATE " + table_
                                      + " SET SCORE=5 WHERE ID > 1");
            s.close ();
            assertCondition (flag == false, "execute(update) returned true, sb false because there is not a result set ");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Executes a stored procedure that does not
return a result set.  Should return false.
**/
    public void Var038()
    {
        try {
            Statement s = connection_.createStatement ();
            boolean flag = s.execute ("CALL "
                                      + JDSetupProcedure.STP_RS0);
            s.close ();
            assertCondition (flag == false, "execute(call) returned true, sb false because a result set is not returned ");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Verify that a previous result set is closed.
**/
    public void Var039()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.next ();
            s.execute ("UPDATE " + table_
                       + " SET SCORE=5 WHERE ID > 10");

            boolean success = false;
            try {
                rs.next ();
            }
            catch (SQLException e) {
                success = true;
            }

            s.close ();
            assertCondition (success, "execute(update) previous result set not closed");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Verify that previous warnings are closed.
**/
    public void Var040()
    {
        if (checkJdbc20 ()) {
            if (isToolboxDriver() &&
                getRelease() >= JDTestDriver.RELEASE_V5R1M0 )
            {
                try {
                    Statement s = connection_.createStatement();
                    s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
                    SQLWarning before = s.getWarnings ();
                    s.execute ("UPDATE " + table_ + " SET SCORE=5 WHERE ID = -1");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed(e, "Unexpected Exception");
                }
            }
	    else if (getDriver () == JDTestDriver.DRIVER_NATIVE &&
		     getRelease() >= JDTestDriver.RELEASE_V5R1M0 )
	    {

                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
		    setNativeWarning(s);
                    SQLWarning before = s.getWarnings ();
                    s.execute ("UPDATE " + table_ + " SET SCORE=5 WHERE ID = -1");
                    SQLWarning after = s.getWarnings ();
                    rs.close();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }

	    }
            else
            {

                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                    SQLWarning before = s.getWarnings ();
                    s.execute ("UPDATE " + table_ + " SET SCORE=5 WHERE ID = -1");
                    SQLWarning after = s.getWarnings ();
                    rs.close();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }


/**
executeQuery() - Verify that a previous result set is
closed if we specify the "reuse objects=false" property on the
connection.

SQL400 - This test was added for the native driver.  Reusing the
result sets performs faster, but is technically not exactly how
the spec says things should work.  We have the default turned on
and allow people to turn it off to be compliant.  This may change
in the future.
**/
    public void Var041()
    {
        if (checkNative()) {
            try {
                Statement s = connection2_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                rs.next ();
                s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");

                boolean success = false;
                try {
                    rs.next ();
                }
                catch (SQLException e) {
                    success = true;
                }

                s.close ();
                assertCondition (success);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
executeUpdate() - Verify that the JDBC driver is not masking the
vendor codes from coming back from the database.

SQL400 - This test was added for the native driver.  We changed the
way we interface with the database and, in doing so, caused a problem
with the vendor always being set as if it were a JDBC driver level errror
(-99999) instead of returning the industry standard values when we
had them.
**/
    public void Var042()
    {
        if (checkNative()) {
            try {
                int vendorCode = 0;

                Statement s = connection_.createStatement ();

                try {
                    s.executeUpdate("DROP TABLE " + table2_);
                }
                catch (SQLException one) {
                    // Ignore it...
                }

                try {
                    s.executeUpdate("CREATE TABLE " + table2_ + " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
                }
                catch (SQLException two) {
                    failed (two, "Unexpected Exception");
                }

                try {
                    s.executeUpdate("CREATE TABLE " + table2_ + " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
                }
                catch (SQLException three) {
                    //System.out.println ("*** SQL Exception Caught ***\n");
                    //System.out.println (" SQLState: " + two.getSQLState ());
                    //System.out.println (" Message: " + two.getMessage ());
                    //System.out.println (" Vendor: " + two.getErrorCode ());
                    vendorCode = three.getErrorCode();
                }

                s.executeUpdate("DROP TABLE " + table2_);
                s.close();

                assertCondition(vendorCode == -601);

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeUpdate() - Verify that updates that use subqueries work correctly.

SQL400 - There was some discussion of whether or not a call to executeUpdate
should work with a subselect because no queries are allowed through the
executeUpdate method.  It is our take that a subselect is not a query but a
way of specifying the input values to an update.  Therefore, the subselect
should work through executeUpdate and no exception should be thrown.
**/
    public void Var043()
    {
        if (checkNative()) {
            try {
                int updateCount = 0;

                Statement s = connection_.createStatement ();

                try {
                    s.executeUpdate("DROP TABLE " + table2_);
                }
                catch (SQLException one) {
                    // Ignore it...
                }

                try {
                    s.executeUpdate("DROP TABLE " + table3_);
                }
                catch (SQLException one) {
                    // Ignore it...
                }

                try {
                    s.executeUpdate("CREATE TABLE " + table2_ + " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
                    s.executeUpdate("INSERT INTO " + table2_ + " VALUES('one')");
                    s.executeUpdate("INSERT INTO " + table2_ + " VALUES('two')");
                    s.executeUpdate("CREATE TABLE " + table3_ + " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
                    updateCount = s.executeUpdate("INSERT INTO " + table3_ + " SELECT ONE FROM " + table2_);
                }
                catch (SQLException two) {
                    failed (two, "Unexpected Exception");
                }


                s.executeUpdate("DROP TABLE " + table2_);
                s.executeUpdate("DROP TABLE " + table3_);
                s.close();

                assertCondition(updateCount == 2);

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeQuery() - Verify that blocked result sets get results properly for each
execution.

SQL400 - An internal problem was discovered where a prepared statement could
be executed multiple times and the information about the offsets into the
result set row cache didn't get reported properly.  The problem did not effect
Statement objects, but this testcase is to cover the same situation for Statements
to ensure that there will not be a problem in the future.
**/
    public void Var044()
    {
        if (checkNative()) {
            try {
                Statement s = connection_.createStatement ();

                // Make sure the table doesn't exist.
                try {
                    s.executeUpdate("DROP TABLE " + table2_);
                }
                catch (SQLException one) {
                    // Ignore it...
                }

                // Setup the table.
                try {
                    s.executeUpdate("CREATE TABLE " + table2_ + "  (NUMBER INT)");
                    s.executeUpdate("INSERT INTO " + table2_ + " VALUES(1)");
                    s.executeUpdate("INSERT INTO " + table2_ + " VALUES(2)");
                    s.executeUpdate("INSERT INTO " + table2_ + " VALUES(3)");
                    s.executeUpdate("INSERT INTO " + table2_ + " VALUES(4)");
                    s.executeUpdate("INSERT INTO " + table2_ + " VALUES(5)");

                }
                catch (SQLException two) {
                    failed (two, "Unexpected Exception");
                }

                ResultSet rs = s.executeQuery("SELECT * FROM " + table2_);
                rs.next();
                rs.next();
                int value1 = rs.getInt(1);

                rs = s.executeQuery("SELECT * FROM " + table2_);
                rs.next();
                rs.next();
                int value2 = rs.getInt(1);

                s.executeUpdate("DROP TABLE " + table2_);
                s.close();

                assertCondition((value1 == 2) && (value2 == 2));

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeQuery() - Verify that we can handle a column with a 30 character name correctly.

SQL400 - Because the CLI will return SUCCESS_WITH_INFO messages for these columns, the
Native JDBC driver was getting confused and not completely work on the columns.  This
is in response to a reported WebSphere problem.
**/
    public void Var045()
    {
        if (checkNative()) {
            try {
                String columnName = "THISCOLNAMEIS30CHARACTERSLONG_";

                Statement s = connection_.createStatement();
                try {
                    s.executeUpdate("DROP TABLE " + table4_);
                }
                catch (SQLException e) {
                    // Ignore problems here.
                }

                s.executeUpdate("CREATE TABLE " + table4_ + " ( " +
                                columnName + " INT) ");
                s.executeUpdate("INSERT INTO " + table4_ + " VALUES(1)");

                ResultSet rs = s.executeQuery("SELECT * FROM " + table4_);

                rs.next();

                assertCondition(rs.getInt(columnName)==1);
                s.close();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
executeQuery() - Verify that we can handle parentheses and whitespace at the
beginning and end of a statement. See v5r2m0.jacl PTR 9949621.
**/
    public void Var046()
    {
        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on 'SELECT'.");
            return;
        }

        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("(SELECT * FROM QIWS.QCUSTCDT)");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on '(SELECT)'.");
            return;
        }

        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("((SELECT * FROM QIWS.QCUSTCDT))");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on '((SELECT))'.");
            return;
        }

        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("( SELECT * FROM QIWS.QCUSTCDT )");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on '( SELECT )'.");
            return;
        }

        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("( ( SELECT * FROM QIWS.QCUSTCDT ) )");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on '( ( SELECT ) )'.");
            return;
        }

        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery(" ((( (( (SELECT * FROM QIWS.QCUSTCDT) )) ))) ");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on ' ((( (( (SELECT) )) ))) '.");
            return;
        }
        succeeded();
    }


/**
executeQuery() - Verify that we can handle missing whitespace around "*" after SELECT. See v5r2m0.jacl PTR 9954254.
**/
    public void Var047()
    {
        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT *FROM QIWS.QCUSTCDT");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on 'SELECT'.");
            return;
        }

        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("(SELECT* FROM QIWS.QCUSTCDT)");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on '(SELECT)'.");
            return;
        }

        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("((SELECT*FROM QIWS.QCUSTCDT))");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on '((SELECT))'.");
            return;
        }

        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("( SELECT* FROM QIWS.QCUSTCDT )");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on '( SELECT )'.");
            return;
        }

        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("( ( SELECT *FROM QIWS.QCUSTCDT ) )");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on '( ( SELECT ) )'.");
            return;
        }

        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery(" ((( (( (SELECT*FROM QIWS.QCUSTCDT) )) ))) ");
            rs.next();
            rs.next();
        }
        catch (Exception e)
        {
            failed(e, "Exception on ' ((( (( (SELECT) )) ))) '.");
            return;
        }
        succeeded();
    }

/**

**/
    public void Var048()
    {
            Statement s = null;
            ResultSet rs = null;
            String table = JDStatementTest.COLLECTION + ".DateTest";

            try
            {
                s = connection_.createStatement();
                try { s.executeUpdate("DROP TABLE " + table); } catch (SQLException e) { }

                s.executeUpdate("CREATE TABLE " + table + " ( P1_DATE DATE )");
                s.executeUpdate("INSERT INTO "  + table + " VALUES('01/01/2000')");
                s.executeUpdate("INSERT INTO "  + table + " VALUES('01/01/1898')");
                s.executeUpdate("INSERT INTO "  + table + " VALUES('01/01/2003')");

                rs = s.executeQuery("SELECT * FROM " + table);

                if (rs.next() && rs.next() && rs.next())
                   succeeded();
                else
                   failed("failed to return three rows");
            }
            catch (Exception e)
            {
                failed (e, "Unexpected Exception");
            }

            if (rs != null)
                try { rs.close(); } catch (SQLException e) { }
            if (s != null)
            {
                try { s.executeUpdate("DROP TABLE " + table); } catch (SQLException e) { }
                try { s.close(); } catch (SQLException e) { }
            }
    }
    /**
      executeQuery -> make sure we can override the behavior of
      throwing an exception if a result set is not returned
    */
    public void Var049()
    {
	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    try {
		String url;
		url = baseURL_+";behavior override=1";
		Connection conn = testDriver_.getConnection(url,userId_, encryptedPassword_);

		Statement s = conn.createStatement();

		s.executeQuery ("CALL " + JDSetupProcedure.STP_RS0);

		s.close();
		conn.close();
		assertCondition(true);


	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("Native driver variation");
	}


    }

    /**
      executeQuery -> make sure we can override the behavior of
      throwing an exception if a result set is not returned
    */
    public void Var050()
    {
	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    try {
		String url;
		url = baseURL_+";behavior override=1";
		Connection conn = testDriver_.getConnection(url,userId_, encryptedPassword_);

		PreparedStatement s = conn.prepareStatement("CALL "+JDSetupProcedure.STP_RS0);

		s.executeQuery ();

		s.close();
		conn.close();
		assertCondition(true);


	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("Native driver variation");
	}


    }


/**
executeQuery() - Pass a statement with parameter markers.
Should throw an exception.

Updated 05/27/2009 to verify that exception is not a generic exception for CLI

**/
    public void Var051()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("SELECT * FROM SYSIBM.SYSDUMMY1 WHERE ?=1");
            failed ("Didn't throw SQLException when passing parameter markers on execute");
        }
        catch (Exception e) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&
		getDriver() == JDTestDriver.DRIVER_NATIVE) {
		String expectedException = "Number of host variables not valid";
		String exceptionMessage = e.toString();
		assertCondition(exceptionMessage.indexOf(expectedException) >= 0,
				"Got "+exceptionMessage+" but expected "+expectedException+" -- modified 5/28/2009 by native driver to detect CLI bug in V7R1");

	    } else {
	           int expectedSqlcode=-313;
		   String expectedSqlstate="07001";
		   String expectedSqlmessage="Number of host variables not valid";


	          //
         	  // For the native driver, the CLI is not properly returning an error.
         	  // This should be fixed in V7R1 (See above) .
         	  //
		   if (getDriver() == JDTestDriver.DRIVER_NATIVE ) {
		       expectedSqlcode=-99999;
		       expectedSqlstate="JDBC\u0000";
		       expectedSqlmessage="Internal Error:  CLI signalled an error condition and had no error data available.";
		   }

		   assertSqlException(e, expectedSqlcode, expectedSqlstate, expectedSqlmessage, "Expected syntax error for parameter marker  -- updated 8/5/2010");

	    }
	}
    }



/**
execute() - Verify that we can handle the new array insert syntax -- See issue 43713
**/
    public void Var052() {
      String description = "New Array insert syntax test";
    if (checkRelease710()) {
      StringBuffer testInfo = new StringBuffer();
      boolean passed = true;
      String sql = "";
      try {
        Statement s = connection_.createStatement();
        String tablename = JDStatementTest.COLLECTION+".JDSTMTEX52";
        try {
          s.executeUpdate("drop table "+tablename);
        } catch (Exception e) {
        }
        sql = "create table "+tablename+"(c1 int)";
        s.executeUpdate(sql);
        sql = "insert into "+tablename+" values 1,2,3";
        s.executeUpdate(sql);
        sql = "select * from "+tablename+" order by c1";
        ResultSet rs = s.executeQuery(sql);
        rs.next();
        int value =rs.getInt(1);
        int expected = 1;
        if (value != expected) { testInfo.append("Expected "+expected+" got "+value); passed=false;}
        rs.next();
        value =rs.getInt(1);
        expected = 2;
        if (value != expected) { testInfo.append("Expected "+expected+" got "+value); passed=false;}
        rs.next();
        value =rs.getInt(1);
        expected = 3;
        if (value != expected) { testInfo.append("Expected "+expected+" got "+value); passed=false;}
        rs.close();
        s.close();
        assertCondition(passed, description + testInfo.toString());
      } catch (Exception e) {
        failed(e, "Unexpected exception last sql = " + sql+" "+description);
        return;
      }
    }
  }


    /**
    execute() - Verify that we can handle the new array insert syntax -- See issue 43713
    **/
        public void Var053() {
          String description = "New Array insert syntax test";
        if (checkRelease710()) {
          StringBuffer testInfo = new StringBuffer();
          boolean passed = true;
          String sql = "";
          try {
            Statement s = connection_.createStatement();
            String tablename = JDStatementTest.COLLECTION+".JDSTMTEX53";
            try {
              s.executeUpdate("drop table "+tablename);
            } catch (Exception e) {
            }
            sql = "create table "+tablename+"(c1 int, c2 varchar(80))";
            s.executeUpdate(sql);
            sql = "insert into "+tablename+" values (1,'v1'),(2,'v2'),(3,'v3')";
            s.executeUpdate(sql);
            sql = "select * from "+tablename+" order by c1";
            ResultSet rs = s.executeQuery(sql);
            rs.next();
            int value =rs.getInt(1);
            int expected = 1;
            if (value != expected) { testInfo.append("Expected "+expected+" got "+value); passed=false;}
            rs.next();
            value =rs.getInt(1);
            expected = 2;
            if (value != expected) { testInfo.append("Expected "+expected+" got "+value); passed=false;}
            rs.next();
            value =rs.getInt(1);
            expected = 3;
            if (value != expected) { testInfo.append("Expected "+expected+" got "+value); passed=false;}
            rs.close();
            s.close();
            assertCondition(passed, description + testInfo.toString());
          } catch (Exception e) {
            failed(e, "Unexpected exception last sql = " + sql+" "+description);
            return;
          }
        }
      }



    /**
    executeQuery() - Verify that we reuse the statement -- see issue 44013
    **/
	public void Var054() {
	    String description = "Reuse the statement for a query added 05/21/2010";

	    StringBuffer testInfo = new StringBuffer();
	    boolean passed = true;
	    String sql = "";
	    try {
		ResultSet rs;
		Statement s = connection_.createStatement();

		String expected = "abc";
		sql = "select 'abc' from sysibm.sysdummy1";
		rs = s.executeQuery(sql);
		rs.next();
		String string37 = rs.getString(1);
		if (! expected.equals(string37)) {
		    testInfo.append("Expected "+expected+" got "+string37);
		    passed=false;
		}

		sql = "select cast('abc' as VARCHAR(80) CCSID 290) from sysibm.sysdummy1";
		rs = s.executeQuery(sql);
		rs.next();
		String string290 = rs.getString(1);
		if (! expected.equals(string290)) {
		    testInfo.append("Expected "+expected+" got "+string290);
		    passed=false;
		}


		sql = "select cast('abc' as VARGRAPHIC(80) CCSID 1200) from sysibm.sysdummy1";
		rs = s.executeQuery(sql);
		rs.next();
		String string1200 = rs.getString(1);
		if (! expected.equals(string1200)) {
		    testInfo.append("Expected "+expected+" got "+string1200);
		    passed=false;
		}
		rs.close();
		s.close();
		assertCondition(passed, description + testInfo.toString());

	    } catch (Exception e) {
		failed(e, "Unexpected exception last sql = " + sql+" "+description);
		return;
	    }
	}


    /**
    executeQuery() - Verify that we reuse the statement -- see issue 44013
    **/
	public void Var055() {
	    String description = "Reuse the statement for a query added 05/21/2010";

	    StringBuffer testInfo = new StringBuffer();
	    boolean passed = true;
	    String sql = "";
	    try {
		ResultSet rs;
		Statement s = connection_.createStatement();

		String expected = "abc";
		sql = "select cast('abc' as CHAR(3)) from sysibm.sysdummy1";
		rs = s.executeQuery(sql);
		rs.next();
		String string37 = rs.getString(1);
		if (! expected.equals(string37)) {
		    testInfo.append("Expected "+expected+" got "+string37);
		    passed=false;
		}

		sql = "select cast('abc' as CHAR(3) CCSID 290) from sysibm.sysdummy1";
		rs = s.executeQuery(sql);
		rs.next();
		String string290 = rs.getString(1);
		if (! expected.equals(string290)) {
		    testInfo.append("Expected "+expected+" got "+string290);
		    passed=false;
		}


		sql = "select cast('abc' as GRAPHIC(3) CCSID 1200) from sysibm.sysdummy1";
		rs = s.executeQuery(sql);
		rs.next();
		String string1200 = rs.getString(1);
		if (! expected.equals(string1200)) {
		    testInfo.append("Expected "+expected+" got "+string1200);
		    passed=false;
		}
		rs.close();
		s.close();
		assertCondition(passed, description + testInfo.toString());

	    } catch (Exception e) {
		failed(e, "Unexpected exception last sql = " + sql+" "+description);
		return;
	    }
	}



    /**
    executeQuery() - Handle recursive SQL
    **/
	public void Var056() {
	    String description = "Handle recursive SQL -- sourceforge bug 2986438 -- added 6/1/2010";

	    StringBuffer testInfo = new StringBuffer();
	    boolean passed = true;
	    String sql = "";
	    try {
		ResultSet rs;
		Statement s;
		 s = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

		String expected = "Y";
		sql = " with f1(IBMREQD) as (select IBMREQD from sysibm.sysdummy1) select * from f1";
		rs = s.executeQuery(sql);
		rs.next();
		String string37 = rs.getString(1);
		if (! expected.equals(string37)) {
		    testInfo.append("Expected "+expected+" got "+string37);
		    passed=false;
		}

		rs.close();
		s.close();
		assertCondition(passed, description + testInfo.toString());

	    } catch (Exception e) {
		failed(e, "Unexpected exception last sql = " + sql+" "+description);
		return;
	    }
	}



/**
executeQuery() - Verify casting by qq works.  Fixed in V5R4 by via issue 44209 (SI40069).

**/
    public void Var057()
    {
	StringBuffer sb = new StringBuffer();
	boolean passed = true;
	String[] queries = {
	    "select CAST('X' AS VARCHAR(80) CCSID 1208) CONCAT UX'0020' from sysibm.sysdummy1",
	    "select concat(CAST('X' AS VARCHAR(80) CCSID 1208), UX'0020') from sysibm.sysdummy1",
	    "select coalesce(CAST('X ' AS VARCHAR(80) CCSID 1208), UX'0020') from sysibm.sysdummy1",
	    "select ifnull(CAST('X ' AS VARCHAR(80) CCSID 1208), UX'0020') from sysibm.sysdummy1",
	    "select value(CAST('X ' AS VARCHAR(80) CCSID 1208), UX'0020') from sysibm.sysdummy1",
	    " select CASE WHEN (1=1) THEN CAST('X ' AS VARCHAR(80) CCSID 1208) ELSE UX'0020' END from sysibm.sysdummy1"

	};
	try {
	    Statement s = connection_.createStatement ();


	    for (int i = 0; i < queries.length; i++) {
		String sql = queries[i];
		try {
		    ResultSet rs = s.executeQuery(sql);
		    rs.next();
		    String value = rs.getString(1);
		    if (! "X ".equals(value)) {
			sb.append("Expected 'X ' but got '"+value+"' for "+sql+"\n");
			passed = false;
		    }
		} catch (Exception e) {
		    sb.append("Exception "+e+" processing query "+sql+"\n");
		    passed = false;
		}
	    }
	    s.close();

	    assertCondition(passed, sb.toString()+" -- added 6/9/2010 to test QQ fixes");

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
    }



/**
executeUpdate() - Pass a statement with parameter markers.
Should throw an exception.
**/
    public void Var058()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("UPDATE " + table_
                             + " SET SCORE=? WHERE NAME='X AND NAME='?' AND ID > 1");
            failed ("Didn't throw SQLException");
        }
	catch (Exception e) {
	    int expectedSqlcode =-104;
	    String expectedSqlstate = "42601";
	    String expectedSqlmessage = "Token ? was not valid";
          assertSqlException(e, expectedSqlcode, expectedSqlstate, expectedSqlmessage, "Expected syntax error for parameter marker -- added 8/5/2010");
        }
    }

/**
executeQuery -- make sure we can run a values statement
**/

    public void Var059()
    {
	if (checkRelease610()) { 
	    StringBuffer sb = new StringBuffer();
	    boolean passed = true;
	    String[][] queries = {
		{"values(1)", "1"},
		{"values('1')", "1"},
		{"values(1+2)", "3"},
		{"values(3.14159)","3.14159"},
		{"values(314159470992097079276.428380682068820632362362362362362361231231)", "314159470992097079276.428380682068820632362362362362362361231231"},
		{"values(CAST(314159470992097079276.428380682068820632362362362362362361231231 AS DOUBLE))", "3.141594709920971E20"},

	    };
	    try {
		Statement s = connection_.createStatement ();


		for (int i = 0; i < queries.length; i++) {
		    String sql = queries[i][0];
		    try {
			ResultSet rs = s.executeQuery(sql);
			rs.next();
			String value = rs.getString(1);
			if (! queries[i][1].equals(value)) {
			    sb.append("Expected '"+queries[i][1]+"' but got '"+value+"' for "+sql+"\n");
			    passed = false;
			}
		    } catch (Exception e) {
			sb.append("Exception "+e+" processing query "+sql+"\n");
			passed = false;
		    }
		}
		s.close();

		assertCondition(passed, sb.toString()+" -- added 1/23/2013 to test VALUES statement");

	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception -- added 1/23/2013 to test VALUES statement");
	    }
	}
    }



/**
execute -- make sure we can run a values into statement and get the right update count back 
**/

    public void Var060()
    {
	if (checkRelease710()) { 
	    StringBuffer sb = new StringBuffer();
	    boolean passed = true;
	    String[][] queries = {
		{"set schema "+JDStatementTest.COLLECTION,"0"},
		{"set current path current path,"+JDStatementTest.COLLECTION,"IGNORE"},
		{"drop variable gv1", "IGNORE"},
		{"create variable gv1 integer default 10", "0"},
		{"values gv1 * 2 into gv1", "0"},
		{"             values gv1 * 2 into gv1", "0"},
		{"\n\n\tvalues gv1 * 2 into gv1", "0"},
		{"values gv1 * 2 into gv1", "0"},
		{"valueS gv1 * 2 into gv1", "0"},
		{"valuEs gv1 * 2 into gv1", "0"},
		{"valuES gv1 * 2 into gv1", "0"},
		{"valUes gv1 * 2 into gv1", "0"},
		{"valUeS gv1 * 2 into gv1", "0"},
		{"valUEs gv1 * 2 into gv1", "0"},
		{"valUES gv1 * 2 into gv1", "0"},
		{"vaLues gv1 * 2 into gv1", "0"},
		{"vaLueS gv1 * 2 into gv1", "0"},
		{"vaLuEs gv1 * 2 into gv1", "0"},
		{"vaLuES gv1 * 2 into gv1", "0"},
		{"vaLUes gv1 * 2 into gv1", "0"},
		{"vaLUeS gv1 * 2 into gv1", "0"},
		{"vaLUEs gv1 * 2 into gv1", "0"},
		{"vaLUES gv1 * 2 into gv1", "0"},
		{"vAlues gv1 * 2 into gv1", "0"},
		{"vAlueS gv1 * 2 into gv1", "0"},
		{"vAluEs gv1 * 2 into gv1", "0"},
		{"vAluES gv1 * 2 into gv1", "0"},
		{"vAlUes gv1 * 2 into gv1", "0"},
		{"vAlUeS gv1 * 2 into gv1", "0"},
		{"vAlUEs gv1 * 2 into gv1", "0"},
		{"vAlUES gv1 * 2 into gv1", "0"},
		{"vALues gv1 * 2 into gv1", "0"},
		{"vALueS gv1 * 2 into gv1", "0"},
		{"vALuEs gv1 * 2 into gv1", "0"},
		{"vALuES gv1 * 2 into gv1", "0"},
		{"vALUes gv1 * 2 into gv1", "0"},
		{"vALUeS gv1 * 2 into gv1", "0"},
		{"vALUEs gv1 * 2 into gv1", "0"},
		{"vALUES gv1 * 2 into gv1", "0"},

		{"Values gv1 * 2 into gv1", "0"},
		{"ValueS gv1 * 2 into gv1", "0"},
		{"ValuEs gv1 * 2 into gv1", "0"},
		{"ValuES gv1 * 2 into gv1", "0"},
		{"ValUes gv1 * 2 into gv1", "0"},
		{"ValUeS gv1 * 2 into gv1", "0"},
		{"ValUEs gv1 * 2 into gv1", "0"},
		{"ValUES gv1 * 2 into gv1", "0"},
		{"VaLues gv1 * 2 into gv1", "0"},
		{"VaLueS gv1 * 2 into gv1", "0"},
		{"VaLuEs gv1 * 2 into gv1", "0"},
		{"VaLuES gv1 * 2 into gv1", "0"},
		{"VaLUes gv1 * 2 into gv1", "0"},
		{"VaLUeS gv1 * 2 into gv1", "0"},
		{"VaLUEs gv1 * 2 into gv1", "0"},
		{"VaLUES gv1 * 2 into gv1", "0"},
		{"VAlues gv1 * 2 into gv1", "0"},
		{"VAlueS gv1 * 2 into gv1", "0"},
		{"VAluEs gv1 * 2 into gv1", "0"},
		{"VAluES gv1 * 2 into gv1", "0"},
		{"VAlUes gv1 * 2 into gv1", "0"},
		{"VAlUeS gv1 * 2 into gv1", "0"},
		{"VAlUEs gv1 * 2 into gv1", "0"},
		{"VAlUES gv1 * 2 into gv1", "0"},
		{"VALues gv1 * 2 into gv1", "0"},
		{"VALueS gv1 * 2 into gv1", "0"},
		{"VALuEs gv1 * 2 into gv1", "0"},
		{"VALuES gv1 * 2 into gv1", "0"},
		{"VALUes gv1 * 2 into gv1", "0"},
		{"VALUeS gv1 * 2 into gv1", "0"},
		{"VALUEs gv1 * 2 into gv1", "0"},
		{"VALUES gv1 * 2 into gv1", "0"},

		{"drop variable gv1", "0"},
	    };
	    try {
		Statement s = connection_.createStatement ();


		for (int i = 0; i < queries.length; i++) {
		    String sql = queries[i][0];
		    try {
			s.execute(sql);
			String value = ""+s.getUpdateCount();
			if (queries[i][1].equals("IGNORE")) {
			} else {
			    if (! queries[i][1].equals(value)) {
				sb.append("Expected '"+queries[i][1]+"' but updcateCount='"+value+"' for "+sql+"\n");
				passed = false;
			    }
			}
		    } catch (Exception e) {
			if (queries[i][1].equals("IGNORE")) {
			} else {
			    sb.append("Exception "+e+" processing query "+sql+"\n");
			    passed = false;
			}
		    }
		}
		s.close();

		assertCondition(passed, sb.toString()+" -- added 1/23/2013 to test VALUES statement");

	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception -- added 1/23/2013 to test VALUES statement");
	    }
	}
    }




/**
executeLargeUpdate() - Pass a null, should throw an exception.
**/
    public void Var061()
    {
	
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_L(s,"executeLargeUpdate",(String) null);
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
executeLargeUpdate() - Pass an empty string, should throw an exception.
**/
    public void Var062()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","");
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
executeLargeUpdate() - Pass a blank string, should throw an exception.
**/
    public void Var063()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_L(s,"executeLargeUpdate"," ");
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
executeLargeUpdate() - Execute an update with a closed statement.
Should throw an exception.
**/
    public void Var064()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		s.close ();
		JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + table_
					      + " SET SCORE=5 WHERE ID > 10");
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
executeLargeUpdate() - Pass a query that returns a result set.
Should throw an exception.
<P>
SQL400 - As of v4r5, the native driver is going to stop throwing
exceptions for this behavior and simple let it work.  We will test
that the ResultSet can be obtained with getResultSet() and that it
is valid.  As of v5r1 the native driver is going to begin throwing
exceptions for this behavior so we are compliant with J2EE.
**/
    public void Var065()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		long updateCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","SELECT * FROM QIWS.QCUSTCDT");
		failed ("Didn't throw SQLException for executeLargeUpdate on a query. but got "+updateCount);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
executeLargeUpdate() - Calls a stored procedure that returns a result set.
Should throw an exception.

**/
    public void Var066()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","CALL "                           // @C1C - changed back to executeLargeUpdate()
					      + JDSetupProcedure.STP_RS1);      //        since that is specifically what
							       //        this variation is for.
		failed ("Didn't throw SQLException for running executeLargeUpdate on a stored procedure that returns result set");
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
executeLargeUpdate() - Pass a statement with parameter markers.
Should throw an exception.
**/
    public void Var067()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + table_
					      + " SET SCORE=? WHERE ID > 1");
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
	    // Check the exception that comes back.
	    // Prior to 8/4/2010, the toolbox driver detected the error and threw
	    // a generic parameter mismatch error.  This causes problems for
	    // users running through ops native -- See CPS discussion 874LTT for more
	    // information.
		int expectedSqlcode=-313;
		String expectedSqlstate="07001";
		String expectedSqlmessage="Number of host variables not valid.";

	  //
	  // For the native driver, the CLI is not properly returning an error for some cases.
	  // This should be fixed in V7R2.
	  //
		if (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() < JDTestDriver.RELEASE_V7R2M0) {

		    if (e instanceof SQLException) {
			if (((SQLException)e). getErrorCode() == -99999) {
			    expectedSqlcode=-99999;
			    expectedSqlstate="JDBC\u0000";
			    expectedSqlmessage="Internal Error:  CLI signalled an error condition and had no error data available.";
			}
		    }
		}
		assertSqlException(e, expectedSqlcode, expectedSqlstate, expectedSqlmessage, "Expected syntax error for parameter marker  -- updated 8/5/2010");
	    }
	}
    }



/**
executeLargeUpdate() - Verify that the update count returned
is correct when no rows are updated.
**/
    public void Var068()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		long updateCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + table_
								+ " SET SCORE=5 WHERE ID > 10");
		s.close ();
		assertCondition (updateCount == 0);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
executeLargeUpdate() - Verify that the update count returned
is correct when some rows are updated.
**/
    public void Var069()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		long updateCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + table_
								+ " SET SCORE=5 WHERE ID > 1");
		s.close ();
		assertCondition (updateCount == 2);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
executeLargeUpdate() - Verify that the update count returned
is correct when a stored procedure is called.
**/
    public void Var070()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		long updateCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","CALL "
								+ JDSetupProcedure.STP_RS0);
		s.close ();
		assertCondition (updateCount == 0);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
executeLargeUpdate() - Verify that a previous result set is
closed.
**/
    public void Var071()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
		rs.next ();
		JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + table_
					      + " SET SCORE=5 WHERE ID > 10");

		boolean success = false;
		try {
		    rs.next ();
		}
		catch (SQLException e) {
		    success = true;
		}

		s.close ();
		assertCondition (success, "Result set was not closed by subsequent update");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
executeLargeUpdate() - Verify that previous warnings are cleared.
**/
    public void Var072()
    {
        if (checkJdbc42 ()) {
            if (( isToolboxDriver() ||
		  getDriver () == JDTestDriver.DRIVER_JTOPENLITE) &&
                getRelease() >= JDTestDriver.RELEASE_V5R1M0 )
            {
                try {
                    Statement s = connection_.createStatement();
                    JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","DELETE FROM " + table_ + " WHERE NAME='Susan'");
                    SQLWarning before = s.getWarnings ();
                    JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + table_ + " SET SCORE=5 WHERE ID = -1");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null), "before="+before+" sb !=null after="+after+" sb null");
                }
                catch (Exception e) {
                    failed(e, "Unexpected Exception");
                }
            }
            else if (getDriver () == JDTestDriver.DRIVER_NATIVE &&
                getRelease() >= JDTestDriver.RELEASE_V5R1M0 )
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
		    setNativeWarning(s);
                    SQLWarning before = s.getWarnings ();
                    rs.close ();
                    JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + table_ + " SET SCORE=5 WHERE ID = -1");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }

	    }
            else
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                    SQLWarning before = s.getWarnings ();
                    rs.close ();
                    JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + table_ + " SET SCORE=5 WHERE ID = -1");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }





/**
executeLargeUpdate() - Verify that the JDBC driver is not masking the
vendor codes from coming back from the database.

SQL400 - This test was added for the native driver.  We changed the
way we interface with the database and, in doing so, caused a problem
with the vendor always being set as if it were a JDBC driver level errror
(-99999) instead of returning the industry standard values when we
had them.
**/
    public void Var073()
    {
	if (checkNative()) {
	    if (checkJdbc42()) {
		try {
		    int vendorCode = 0;

		    Statement s = connection_.createStatement ();

		    try {
			JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","DROP TABLE " + table2_);
		    }
		    catch (SQLException one) {
		    // Ignore it...
		    }

		    try {
			JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","CREATE TABLE " + table2_ + " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
		    }
		    catch (SQLException two) {
			failed (two, "Unexpected Exception");
		    }

		    try {
			JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","CREATE TABLE " + table2_ + " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
		    }
		    catch (SQLException three) {
		    //System.out.println ("*** SQL Exception Caught ***\n");
		    //System.out.println (" SQLState: " + two.getSQLState ());
		    //System.out.println (" Message: " + two.getMessage ());
		    //System.out.println (" Vendor: " + two.getErrorCode ());
			vendorCode = three.getErrorCode();
		    }

		    JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","DROP TABLE " + table2_);
		    s.close();

		    assertCondition(vendorCode == -601);

		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }



/**
executeLargeUpdate() - Verify that updates that use subqueries work correctly.

SQL400 - There was some discussion of whether or not a call to executeLargeUpdate
should work with a subselect because no queries are allowed through the
executeLargeUpdate method.  It is our take that a subselect is not a query but a
way of specifying the input values to an update.  Therefore, the subselect
should work through executeLargeUpdate and no exception should be thrown.
**/
    public void Var074()
    {
	if (checkNative()) {
	    if (checkJdbc42()) {
		try {
		    long updateCount = 0;

		    Statement s = connection_.createStatement ();

		    try {
			JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","DROP TABLE " + table2_);
		    }
		    catch (SQLException one) {
		    // Ignore it...
		    }

		    try {
			JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","DROP TABLE " + table3_);
		    }
		    catch (SQLException one) {
		    // Ignore it...
		    }

		    try {
			JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","CREATE TABLE " + table2_ + " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
			JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + table2_ + " VALUES('one')");
			JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + table2_ + " VALUES('two')");
			JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","CREATE TABLE " + table3_ + " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
			updateCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + table3_ + " SELECT ONE FROM " + table2_);
		    }
		    catch (SQLException two) {
			failed (two, "Unexpected Exception");
		    }


		    JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","DROP TABLE " + table2_);
		    JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","DROP TABLE " + table3_);
		    s.close();

		    assertCondition(updateCount == 2);

		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}

    }



/**
executeLargeUpdate() - Pass a statement with parameter markers.
Should throw an exception.
**/
    public void Var075()
    {
	if (checkJdbc42()) {
	    try {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + table_
					      + " SET SCORE=? WHERE NAME='X AND NAME='?' AND ID > 1");
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		int expectedSqlcode =-104;
		String expectedSqlstate = "42601";
		String expectedSqlmessage = "Token ? was not valid";
		assertSqlException(e, expectedSqlcode, expectedSqlstate, expectedSqlmessage, "Expected syntax error for parameter marker -- added 8/5/2010");
	    }
	}

    }




}




