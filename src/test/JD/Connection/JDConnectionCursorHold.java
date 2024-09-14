///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionCursorHold.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionCursorHold.java
//
// Classes:      JDConnectionCursorHold
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;



/**
Testcase JDConnectionCursorHold.  This tests the following
methods of the JDBC Connection class:

<ul>
<li>cursor hold property
<li>CURSORHOLD property()
</ul>
**/
public class JDConnectionCursorHold
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionCursorHold";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.
    private static  String table_      = JDConnectionTest.COLLECTION + ".CURSORHOLD";



/**
Constructor.
**/
    public JDConnectionCursorHold (AS400 systemObject,
                                   Hashtable namesAndVars,
                                   int runMode,
                                   FileOutputStream fileOutputStream,
                                   
                                   String password)
    {
        super (systemObject, "JDConnectionCursorHold",
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
        //if (getDriver() == JDTestDriver.DRIVER_NATIVE) {           // @C1
            // Create the table.
            table_      = JDConnectionTest.COLLECTION + ".CURSORHOLD";
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("CREATE TABLE " + table_
                             + " (NAME CHAR(10), ID INT)");

            // Make sure there are a couple values in the table to fetch.
            s.executeUpdate("INSERT INTO " + table_ + " VALUES('first', 1)");
            s.executeUpdate("INSERT INTO " + table_ + " VALUES('second', 2)");

            s.close ();
            c.close ();
        //}
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        //if (getDriver() == JDTestDriver.DRIVER_NATIVE) {        // @C1
            // Drop the table.
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("DROP TABLE " + table_);
            s.close ();
            c.close ();
        //}
    }

/**
Returns true if running native driver and JDBC level is <= 2
*/
    protected boolean isNative2() {
	return (getDriver() == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2); 
    } 


/**
Checks that a ResultSet object is still usable.

@param  rs      The ResultSet.
@return         true if the result set can still be used.
                false otherwise.
**/
    private boolean cursorOpen (ResultSet rs)
    throws Exception
    {
        boolean success = false;
        try {
            // If a fetch returns true, the cursor is still open.
            if (rs.next())
                success = true;
        }
        catch (SQLException e) {
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)  //@E3
	    {
		String exceptionMessage = "Cursor state not valid.";
	        /* success = ( (exceptionMessage.equals("Cursor state not valid.") &&
				(e instanceof SQLException)); */
		success = !exceptionMessage.equals(e.getMessage());
	        //we already know that e is an instance of SQLException becuase it was caught

		if (success) {                                                            
		/* IF WE GET HERE, WE DIDN'T GET WHAT WE EXPECTED */
		/* DUMP THE EXCEPTION SO WE CAN FIX IT  */
		/* ALL EXCEPTIONS THAT WE EXPECT AS WELL AS THE */
		/* EXPECTED RESULT SHOULD BE EXPLICILY SPECIFIED */
		    System.out.println("cursorOpen encountered unexpected exception "+e);
		    e.printStackTrace();
		}		
	      }
	    else  //toolbox
	      {
	    // This seems like reverse logic, but this will work right.
		  success = !exceptionIs(e, "SQLException", "Cursor state not valid.");  //@E1A
	      }
	}
	return success;
    }



/**
cursor hold property - Uses default behavior.  ResultSet cursor should be able to
be used after a commit.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var001()
    {
        // if (checkNative()) {           // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_); // @C1C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be true.  ResultSet cursor should be able to
be used after a commit.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var002()
    {
        //if (checkNative()) {                 // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=true", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 1.  ResultSet cursor should be able to
be used after a commit.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var003()
    {
        //if (checkNative()) {                     // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=1", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be false.  ResultSet cursor should get
implicitly close and should not be able to be used after a commit.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var004()
    {
        //if (checkNative()) {                         // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 0.  ResultSet cursor should get
implicitly close and should not be able to be used after a commit.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var005()
    {
        //if (checkNative()) {                    // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify an invalid value for "cursor hold".  The value should get
ignored and the ResultSet cursor should be able to be used after a commit.
<P>
Here we use the value that is not the default for the "CURSORHOLD" value to
ensure that they values can't be cross used as well.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var006()
    {
        //if (checkNative()) {                       // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD - Specify an invalid value for "CURSORHOLD".  The value should get
ignored and the ResultSet cursor should be able to be used after a commit.
<P>
Here we use the value that is not the default for the "cursor hold" value to
ensure that they values can't be cross used as well.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var007()
    {
        //if (checkNative()) {                    // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to true, so the result set should be able to be used after a commit in this case.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var008()
    {
        //if (checkNative()) {                        // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;cursor hold=true", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to false, so the result set should not be able to be used after a commit in this case.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var009()
    {
        //if (checkNative()) {                     // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=true;cursor hold=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Uses default behavior.  ResultSet cursor should be able to
be used after a commit.
**/
    public void Var010()
    {
        //if (checkNative()) {                         // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_); // @C1C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be true.  ResultSet cursor should be able to
be used after a commit.
**/
    public void Var011()
    {
        //if (checkNative()) {                    // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=true", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 1.  ResultSet cursor should be able to
be used after a commit.
**/
    public void Var012()
    {
        //if (checkNative()) {                        // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=1", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be false.  ResultSet cursor should get
implicitly close and should not be able to be used after a commit.
**/
    public void Var013()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 0.  ResultSet cursor should get
implicitly close and should not be able to be used after a commit.
**/
    public void Var014()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify an invalid value for "cursor hold".  The value should get
ignored and the ResultSet cursor should be able to be used after a commit.
<P>
Here we use the value that is not the default for the "CURSORHOLD" value to
ensure that they values can't be cross used as well.
**/
    public void Var015()
    {
        //if (checkNative()) {                    // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD - Specify an invalid value for "CURSORHOLD".  The value should get
ignored and the ResultSet cursor should be able to be used after a commit.
<P>
Here we use the value that is not the default for the "cursor hold" value to
ensure that they values can't be cross used as well.
**/
    public void Var016()
    {
        //if (checkNative()) {                   // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to true, so the result set should be able to be used after a commit in this case.
**/
    public void Var017()
    {
        //if (checkNative()) {                          // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;cursor hold=true", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to false, so the result set should not be able to be used after a commit in this case.
**/
    public void Var018()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=true;cursor hold=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Uses default behavior.  ResultSet cursor should be able to
be used after a rollback.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var019()
    {
        //if (checkNative()) {                          // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_); // @C1C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();
		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be true.  ResultSet cursor should be able to
be used after a rollback.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var020()
    {
        //if (checkNative()) {                            // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=true", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();
		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 

		    assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 1.  ResultSet cursor should be able to
be used after a rollback.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var021()
    {
        //if (checkNative()) {                        // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=1", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
		    assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be false.  ResultSet cursor should get
implicitly close and should not be able to be used after a rollback.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var022()
    {
        //if (checkNative()) {                       // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 0.  ResultSet cursor should get
implicitly close and should not be able to be used after a rollback.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var023()
    {
        //if (checkNative()) {                          // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify an invalid value for "cursor hold".  The value should get
ignored and the ResultSet cursor should be able to be used after a rollback.
<P>
Here we use the value that is not the default for the "CURSORHOLD" value to
ensure that they values can't be cross used as well.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var024()
    {
        //if (checkNative()) {                       // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 

		    assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD - Specify an invalid value for "CURSORHOLD".  The value should get
ignored and the ResultSet cursor should be able to be used after a rollback.
<P>
Here we use the value that is not the default for the "cursor hold" value to
ensure that they values can't be cross used as well.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var025()
    {
        //if (checkNative()) {                          // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 

		    assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to true, so the result set should be able to be used after a rollback in this case.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var026()
    {
        //if (checkNative()) {                           // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
		    assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to false, so the result set should not be able to be used after a rollback in this case.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var027()
    {
        //if (checkNative()) {                        // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=true;cursor hold=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Uses default behavior.  ResultSet cursor should be able to
be used after a rollback.
**/
    public void Var028()
    {
        //if (checkNative()) {                    // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_); // @C1C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
		    assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }


/**
cursor hold property - Specify "cursor hold" to be true.  ResultSet cursor should be able to
be used after a rollback.
**/
    public void Var029()
    {
        //if (checkNative()) {                     // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=true", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }


/**
CURSORHOLD property - Specify "CURSORHOLD" to be 1.  ResultSet cursor should be able to
be used after a rollback.
**/
    public void Var030()
    {
        //if (checkNative()) {                               // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=1", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }


/**
cursor hold property - Specify "cursor hold" to be false.  ResultSet cursor should get
implicitly close and should not be able to be used after a rollback.
**/
    public void Var031()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 0.  ResultSet cursor should get
implicitly close and should not be able to be used after a rollback.
**/
    public void Var032()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify an invalid value for "cursor hold".  The value should get
ignored and the ResultSet cursor should be able to be used after a rollback.
<P>
Here we use the value that is not the default for the "CURSORHOLD" value to
ensure that they values can't be cross used as well.
**/
    public void Var033()
    {
        //if (checkNative()) {                         // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD - Specify an invalid value for "CURSORHOLD".  The value should get
ignored and the ResultSet cursor should be able to be used after a rollback.
<P>
Here we use the value that is not the default for the "cursor hold" value to
ensure that they values can't be cross used as well.
**/
    public void Var034()
    {
        //if (checkNative()) {                       // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to true, so the result set should be able to be used after a rollback in this case.
**/
    public void Var035()
    {
        //if (checkNative()) {                          // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;cursor hold=true", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to false, so the result set should not be able to be used after a rollback in this case.
**/
    public void Var036()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=true;cursor hold=false", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Uses default behavior.  ResultSet cursor should be able to
be used after a commit.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var037()
    {
        // if (checkNative()) {           // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_ + ";block criteria=0", userId_, encryptedPassword_); // @C1C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be true.  ResultSet cursor should be able to
be used after a commit.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var038()
    {
        //if (checkNative()) {                 // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=true;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 1.  ResultSet cursor should be able to
be used after a commit.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var039()
    {
        //if (checkNative()) {                     // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=1;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be false.  ResultSet cursor should get
implicitly close and should not be able to be used after a commit.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var040()
    {
        //if (checkNative()) {                         // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 0.  ResultSet cursor should get
implicitly close and should not be able to be used after a commit.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var041()
    {
        //if (checkNative()) {                    // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=0;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify an invalid value for "cursor hold".  The value should get
ignored and the ResultSet cursor should be able to be used after a commit.
<P>
Here we use the value that is not the default for the "CURSORHOLD" value to
ensure that they values can't be cross used as well.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var042()
    {
        //if (checkNative()) {                       // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=0;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD - Specify an invalid value for "CURSORHOLD".  The value should get
ignored and the ResultSet cursor should be able to be used after a commit.
<P>
Here we use the value that is not the default for the "cursor hold" value to
ensure that they values can't be cross used as well.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var043()
    {
        //if (checkNative()) {                    // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to true, so the result set should be able to be used after a commit in this case.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var044()
    {
        //if (checkNative()) {                        // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;cursor hold=true;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to false, so the result set should not be able to be used after a commit in this case.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var045()
    {
        //if (checkNative()) {                     // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=true;cursor hold=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Uses default behavior.  ResultSet cursor should be able to
be used after a commit.
**/
    public void Var046()
    {
        //if (checkNative()) {                         // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_ + ";block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be true.  ResultSet cursor should be able to
be used after a commit.
**/
    public void Var047()
    {
        //if (checkNative()) {                    // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=true;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 1.  ResultSet cursor should be able to
be used after a commit.
**/
    public void Var048()
    {
        //if (checkNative()) {                        // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=1;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be false.  ResultSet cursor should get
implicitly close and should not be able to be used after a commit.
**/
    public void Var049()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 0.  ResultSet cursor should get
implicitly close and should not be able to be used after a commit.
**/
    public void Var050()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=0;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify an invalid value for "cursor hold".  The value should get
ignored and the ResultSet cursor should be able to be used after a commit.
<P>
Here we use the value that is not the default for the "CURSORHOLD" value to
ensure that they values can't be cross used as well.
**/
    public void Var051()
    {
        //if (checkNative()) {                    // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=0;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD - Specify an invalid value for "CURSORHOLD".  The value should get
ignored and the ResultSet cursor should be able to be used after a commit.
<P>
Here we use the value that is not the default for the "cursor hold" value to
ensure that they values can't be cross used as well.
**/
    public void Var052()
    {
        //if (checkNative()) {                   // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to true, so the result set should be able to be used after a commit in this case.
**/
    public void Var053()
    {
        //if (checkNative()) {                          // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;cursor hold=true;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to false, so the result set should not be able to be used after a commit in this case.
**/
    public void Var054()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=true;cursor hold=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.commit();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Uses default behavior.  ResultSet cursor should be able to
be used after a rollback.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var055()
    {
        //if (checkNative()) {                          // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_ + ";block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be true.  ResultSet cursor should be able to
be used after a rollback.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var056()
    {
        //if (checkNative()) {                            // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=true;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 1.  ResultSet cursor should be able to
be used after a rollback.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var057()
    {
        //if (checkNative()) {                        // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=1;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}	
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify "cursor hold" to be false.  ResultSet cursor should get
implicitly close and should not be able to be used after a rollback.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var058()
    {
        //if (checkNative()) {                       // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 0.  ResultSet cursor should get
implicitly close and should not be able to be used after a rollback.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var059()
    {
        //if (checkNative()) {                          // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=0;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify an invalid value for "cursor hold".  The value should get
ignored and the ResultSet cursor should be able to be used after a rollback.
<P>
Here we use the value that is not the default for the "CURSORHOLD" value to
ensure that they values can't be cross used as well.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var060()
    {
        //if (checkNative()) {                       // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=0;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD - Specify an invalid value for "CURSORHOLD".  The value should get
ignored and the ResultSet cursor should be able to be used after a rollback.
<P>
Here we use the value that is not the default for the "cursor hold" value to
ensure that they values can't be cross used as well.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var061()
    {
        //if (checkNative()) {                          // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to true, so the result set should be able to be used after a rollback in this case.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var062()
    {
        //if (checkNative()) {                           // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;cursor hold=true;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to false, so the result set should not be able to be used after a rollback in this case.
<P>
NOTE: For this test, the "FOR UPDATE" clause is added to our select statement because it
will force the JDBC driver to not use ResultSet blocking.  This might not always
be true in the future.  Separate tests exist to ensure that the right thing is done
when the result set is blocked and SQLExtendedFetch is being used.
**/
    public void Var063()
    {
        //if (checkNative()) {                        // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=true;cursor hold=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Uses default behavior.  ResultSet cursor should be able to
be used after a rollback.
**/
    public void Var064()
    {
        //if (checkNative()) {                    // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_ + ";block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }


/**
cursor hold property - Specify "cursor hold" to be true.  ResultSet cursor should be able to
be used after a rollback.
**/
    public void Var065()
    {
        //if (checkNative()) {                     // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=true;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }


/**
CURSORHOLD property - Specify "CURSORHOLD" to be 1.  ResultSet cursor should be able to
be used after a rollback.
**/
    public void Var066()
    {
        //if (checkNative()) {                               // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=1;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }


/**
cursor hold property - Specify "cursor hold" to be false.  ResultSet cursor should get
implicitly close and should not be able to be used after a rollback.
**/
    public void Var067()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD property - Specify "CURSORHOLD" to be 0.  ResultSet cursor should get
implicitly close and should not be able to be used after a rollback.
**/
    public void Var068()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=0;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
cursor hold property - Specify an invalid value for "cursor hold".  The value should get
ignored and the ResultSet cursor should be able to be used after a rollback.
<P>
Here we use the value that is not the default for the "CURSORHOLD" value to
ensure that they values can't be cross used as well.
**/
    public void Var069()
    {
        //if (checkNative()) {                         // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";cursor hold=0;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD - Specify an invalid value for "CURSORHOLD".  The value should get
ignored and the ResultSet cursor should be able to be used after a rollback.
<P>
Here we use the value that is not the default for the "cursor hold" value to
ensure that they values can't be cross used as well.
**/
    public void Var070()
    {
        //if (checkNative()) {                       // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to true, so the result set should be able to be used after a rollback in this case.
**/
    public void Var071()
    {
        //if (checkNative()) {                          // @C1
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=false;cursor hold=true;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

		if (isNative2()) {
                    assertCondition(cursorOpen(rs));
		} else { 
                assertCondition(!cursorOpen(rs));
		}
                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }



/**
CURSORHOLD and cursor hold - Specify a value for both properties and have them be opposites.
Ensure that the setting for "cursor hold" is the one that takes effect.  "cursor hold" is set
to false, so the result set should not be able to be used after a rollback in this case.
**/
    public void Var072()
    {
        // @C2D if (checkNative()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_
                                                          + ";CURSORHOLD=true;cursor hold=false;block criteria=0", userId_, encryptedPassword_); // @C2C

                c.setAutoCommit (false);
                c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                Statement s1 = c.createStatement();
                Statement s2 = c.createStatement();
                ResultSet rs = s1.executeQuery("SELECT * FROM " + table_);
                rs.next();
                s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

                c.rollback();

                assertCondition(!cursorOpen(rs));

                s1.close();
                s2.close();
                c.close ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        //}
    }


  // Get a connection using properties
  public void Var073() {
    if (checkPasswordLeak()) { 
      StringBuffer sb = new StringBuffer("Get a connection using properties\n"); 
    try {
      Properties properties = new Properties();
      properties.put("CURSORHOLD", "1");
      properties.put("user", userId_);
      properties.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionCursorHold.73"));
      sb.append("Getting connection with URL="+baseURL_+" user="+userId_+"\n"); 
      Connection c = DriverManager.getConnection(baseURL_, properties);

      c.setAutoCommit(false);
      c.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

      Statement s1 = c.createStatement();
      Statement s2 = c.createStatement();
      ResultSet rs = s1.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
      rs.next();
      s2.executeUpdate("INSERT INTO " + table_ + " VALUES('tester', 1)");

      c.commit();

      assertCondition(cursorOpen(rs));

      s1.close();
      s2.close();
      c.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception "+sb.toString());
    }
    // }
    }
  }

  // Get a connection using properties
  public void Var074() {
    if (checkPasswordLeak()) {
      try {
        Properties properties = new Properties();
        properties.put("CURSORHOLD", "0");
        properties.put("user", userId_);
        properties.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionCursorHold.74"));
        Connection c = DriverManager.getConnection(baseURL_, properties);

        c.commit();
        c.close();

        assertCondition(true);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

}




