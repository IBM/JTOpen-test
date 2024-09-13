///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSExecute.java
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
// File Name:    JDPSExecute.java
//
// Classes:      JDPSExecute
//
////////////////////////////////////////////////////////////////////////
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDPSExecute.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>executeUpdate()</li>
<li>executeQuery()</li>
<li>execute()</li>
<li>executeLargeUpdate()</li>
</ul>
**/
public class JDPSExecute
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSExecute";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }


    // Private data.
    private static String table_  = JDPSTest.COLLECTION + ".JDPSE";
    private static String table2_  = JDPSTest.COLLECTION + ".JDPSE2";
    private static String table3_  = JDPSTest.COLLECTION + ".JDPSE3";
    private static String table4_  = JDPSTest.COLLECTION + ".JDPSE4";
    private static String tableErr_  = JDPSTest.COLLECTION + ".JDPSERR";
    private Connection      connection_;
    private Connection      connection2_ = null;
    private Connection      connectionNoPrefetch_; 



/**
Constructor.
**/
    public JDPSExecute (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
    {
        super (systemObject, "JDPSExecute",
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
        connection_ = testDriver_.getConnection (baseURL_ + ";errors=full;date format=iso", 
                                                 userId_, encryptedPassword_);


        connectionNoPrefetch_ = testDriver_.getConnection (baseURL_ + ";errors=full;date format=iso;prefetch=false", 
                                                 userId_, encryptedPassword_);

        // Be default the native driver will reuse result set objects.  
        // That means that we need a connection that will not reuse 
        // result set object if we are going to test that the get closed
        // as we do in a couple variations.
        if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
            connection2_ = testDriver_.getConnection (baseURL_ + ";errors=full;reuse objects=false", 
                                                      userId_, encryptedPassword_);
	} else {
	    connection2_ = testDriver_.getConnection (baseURL_ + ";errors=full", 
						      userId_, encryptedPassword_);
	}

            JDSetupProcedure.create (systemObject_,
                                     connection_, JDSetupProcedure.STP_RS0,
                                     supportedFeatures_, collection_);
            JDSetupProcedure.create (systemObject_,
                                     connection_, JDSetupProcedure.STP_RS1,
                                     supportedFeatures_, collection_);
            JDSetupProcedure.create (systemObject_,
                                     connection_, JDSetupProcedure.STP_RS3,
                                     supportedFeatures_, collection_);
            table_  = JDPSTest.COLLECTION + ".JDPSE";
            table2_  = JDPSTest.COLLECTION + ".JDPSE2";
            table3_  = JDPSTest.COLLECTION + ".JDPSE3";
	    table4_  = JDPSTest.COLLECTION + ".JDPSE4";
	    tableErr_  = JDPSTest.COLLECTION + ".JDPSERR";
	    



        Statement s = connection_.createStatement ();
        s.executeUpdate ("CREATE TABLE " + table_
                         + " (NAME VARCHAR(10), ID INT, SCORE INT)");
        s.executeUpdate ("INSERT INTO " + table_
                         + " (NAME, ID) VALUES ('cnock', 1)");
        s.executeUpdate ("INSERT INTO " + table_
                         + " (NAME, ID) VALUES ('murch', 2)");
        s.executeUpdate ("INSERT INTO " + table_
                         + " (NAME, ID) VALUES ('joshvt', 3)");
        s.executeUpdate ("INSERT INTO " + table_
                         + " (NAME, ID) VALUES ('robb', -1)");

	try {
	    s.executeUpdate("DROP FUNCTION "+JDPSTest.COLLECTION+".RET_ERROR2"); 
	} catch (Exception e) {
	} 

	s.executeUpdate("CREATE FUNCTION "+JDPSTest.COLLECTION+".RET_ERROR2 (INDATA VARCHAR(80), INFO VARCHAR(80)) RETURNS VARCHAR(80) LANGUAGE SQL BEGIN DECLARE ALLMESSAGE VARCHAR(80);  IF INDATA='IOERR' THEN SET ALLMESSAGE=CONCAT('ERROR CONDITION FOUND ', INFO);  SIGNAL SQLSTATE 'IOERR' SET MESSAGE_TEXT = ALLMESSAGE ;  END IF;   RETURN INDATA;    END ");

	try {
	    s.executeUpdate("DROP TABLE "+tableErr_); 
	} catch (Exception e) {
	} 
	s.executeUpdate("CREATE TABLE "+tableErr_ +"(INDATA VARCHAR(80), ERROR_MIDDLE INT, ERROR_FIRST INT, ERROR_LAST INT )");
	s.executeUpdate("INSERT INTO "+tableErr_+" VALUES('FIRST' ,1,2,1)");
	s.executeUpdate("INSERT INTO "+tableErr_+" VALUES('SECOND',2,3,2)");
	s.executeUpdate("INSERT INTO "+tableErr_+" VALUES('IOERR' ,3,1,9)");
	s.executeUpdate("INSERT INTO "+tableErr_+" VALUES('FOURTH',4,4,3)");
	s.executeUpdate("INSERT INTO "+tableErr_+" VALUES('FIFTH' ,5,5,4)"); 
        s.close ();

        connection_.commit(); // for xa
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
	if (connection2_ != null) { 
            connection2_.close ();
        }
	connectionNoPrefetch_.close();
    }



/**
executeUpdate() - Execute an update with a closed statement.
Should throw an exception.
**/
    public void Var001()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=5 WHERE ID > 10");
            ps.close ();
            ps.executeUpdate ();
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
    public void Var002()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            int updateCount = ps.executeUpdate ();

               failed ("Didn't throw SQLException updateCount ="+updateCount);
            }
        catch (Exception e) {
              assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Calls a stored procedure that returns a result set.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("CALL "
                                                                 + JDSetupProcedure.STP_RS1);
            ps.executeUpdate();   // @C1C - changed back to executeUpdate() 
                                  //        since that is specifically what 
                                  //        this variation is for.          

               failed ("Didn't throw SQLException");
            }
        catch (Exception e) {
              assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Execute when no parameters are set, and the statement
does not contain any parameter markers.  Should work.
**/
    public void Var004()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=2 WHERE ID > 1");
            ps.executeUpdate ();
            ps.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeUpdate() - Execute when no parameters are set, and the statement
does not contain any parameter markers.
Should throw an exception.
**/
    public void Var005()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=? WHERE ID > 1");
            ps.executeUpdate ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Verify that the update count returned
is correct when no rows are updated.
**/
    public void Var006()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=? WHERE ID > 10");
            ps.setInt (1, 5);
            int updateCount = ps.executeUpdate ();
            ps.close ();
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
    public void Var007()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=? WHERE ID > 1");
            ps.setInt (1, 5);
            int updateCount = ps.executeUpdate ();
            ps.close ();
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
    public void Var008()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("CALL "
                                                                 + JDSetupProcedure.STP_RS0);
            int updateCount = ps.executeUpdate ();
            ps.close ();
            assertCondition (updateCount == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeQuery() - Execute an query with a closed statement.
Should throw an exception.
**/
    public void Var009()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            ps.close ();
            ResultSet rs = ps.executeQuery ();
            failed ("Didn't throw SQLException "+rs);
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
    public void Var010()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=5 WHERE ID > 10");
            ResultSet rs = ps.executeQuery ();
               failed ("Didn't throw SQLException  "+rs);
            }
        catch (Exception e) {
              assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Calls a stored procedure that does not return
a result set.  Should throw an exception.
**/
    public void Var011()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("CALL " + JDSetupProcedure.STP_RS0);
            ps.executeQuery ();
               failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Execute a statement without parameter markers, and no
parameters set. Should work.
**/
    public void Var012()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT WHERE LSTNAM = 'Jones'");
            ps.executeQuery ();
            ps.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeQuery() - Execute a statement without parameter markers, and no
parameters set. Should throw an exception.
**/
    public void Var013()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT WHERE LSTNAM = ?");
            ps.executeQuery ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Verify that the result set is returned.
**/
    public void Var014()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            ResultSet rs = ps.executeQuery ();
            boolean check = rs.next ();
            rs.close ();
            ps.close ();
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
    public void Var015()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("CALL " + JDSetupProcedure.STP_RS1);
            ResultSet rs = ps.executeQuery ();
            boolean check = rs.next ();
            rs.close ();
            ps.close ();
            assertCondition (check);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeQuery() - Verify that the result set returned
is correct when a stored procedure is called that returns multiple
result sets.
**/
    public void Var016()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("CALL " + JDSetupProcedure.STP_RS3);
            ResultSet rs = ps.executeQuery ();
            boolean check = rs.next ();
            rs.close ();
            ps.close ();
            assertCondition (check);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeQuery() - Verify that a previous result set is
closed.

SQL400 - the native JDBC driver defaults to reusing result sets so this
         variation uses a connection that has that default turned off.
**/
    public void Var017()
    {
        try {
            PreparedStatement ps = null;

            if (getDriver () != JDTestDriver.DRIVER_NATIVE)
                ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            else
                ps = connection2_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");

            ResultSet rs = ps.executeQuery ();
            rs.next ();
            ps.executeQuery ();

            boolean success = false;
            try {
                rs.next ();
            }
            catch (SQLException e) {
                success = true;
            }

            ps.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
executeQuery() - Execute an query with an empty result set.  Verify that
the statement is still usable.  This test is here because of a specific
bug that was in a previous release of the Toolbox JDBC driver.
**/
    public void Var018()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT WHERE LSTNAM=?");
            ps.setString (1, "Nock");
            ResultSet rs = ps.executeQuery ();
            boolean check1 = rs.next ();
            ps.setString (1, "Jones");
            ResultSet rs2 = ps.executeQuery ();
            boolean check2 = rs2.next ();
            rs2.close ();
            ps.close ();
            assertCondition ((check1 == false) && (check2 == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Execute an statement with a closed statement.
Should throw an exception.
**/
    public void Var019()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=5 WHERE ID > 10");
            ps.close ();
            ps.execute ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
execute() - Pass a statement without parameter markers, and no
parameters are set.  Should work.
**/
    public void Var020()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=7 WHERE ID > 1");
            ps.execute ();
            ps.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Pass a statement without parameter markers, and no
parameters are set.  Should throw an exception.
**/
    public void Var021()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=? WHERE ID > 1");
            ps.execute ();
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
execute() - Executes a query.  Should return true.
**/
    public void Var022()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT WHERE LSTNAM=?");
            ps.setString (1, "Lee");
            boolean flag = ps.execute ();
            ps.close ();
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
    public void Var023()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("CALL "
                                                                 + JDSetupProcedure.STP_RS1);
            boolean flag = ps.execute ();
            ps.close ();
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
    public void Var024()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("CALL "
                                                                 + JDSetupProcedure.STP_RS3);
            boolean flag = ps.execute ();
            ps.close ();
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
    public void Var025()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=? WHERE ID > 10");
            ps.setInt (1, 4);
            boolean flag = ps.execute ();
            ps.close ();
            assertCondition (flag == false, "Got "+flag+" expected false from ps.execute() when 0 rows updated");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Executes an update where some rows are updated.
Should return false.
**/
    public void Var026()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("UPDATE " + table_
                                                                 + " SET SCORE=? WHERE ID > 1");
            ps.setInt (1, 12);
            boolean flag = ps.execute ();
            ps.close ();
            assertCondition (flag == false, "Got "+flag+" expected false from ps.execute() when 0 rows updated");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Executes a stored procedure that does not
return a result set.  Should return false.
**/
    public void Var027()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement ("CALL "
                                                                 + JDSetupProcedure.STP_RS0);
            boolean flag = ps.execute ();
            ps.close ();
            assertCondition (flag == false, "Got "+flag+" expected false from ps.execute() when CALL returns RS");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Verify that a previous result set is closed.

SQL400 - the native JDBC driver defaults to reusing result sets so this
         variation uses a connection that has that default turned off.
**/
    public void Var028()
    {
        try {
            PreparedStatement ps = null;

            if (getDriver () != JDTestDriver.DRIVER_NATIVE)
                ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            else
                ps = connection2_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");

            ResultSet rs = ps.executeQuery ();
            rs.next ();
            ps.execute ();

            boolean success = false;
            try {
                rs.next ();
            }
            catch (SQLException e) {
                success = true;
            }

            ps.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
executeQuery() - Verify that a previous result set is
not closed when the reuse objects flag is true (the default value).

SQL400 - This is added to test the reuse objects flag.  It mirrors the work
         done in variation 17 but we expect the previous result set object to 
         still be able to be used because it is the same object as the new 
         result set object (the object was reused).
**/
    public void Var029()
    {
        if (checkNative()) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
                ResultSet rs = ps.executeQuery ();
                rs.next ();
                ps.executeQuery ();

                boolean success = true;
                try {
                    rs.next ();
                }
                catch (SQLException e) {
                    success = false;
                }

                ps.close ();
                assertCondition (success);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
execute() - Verify that a previous result set is not closed when
the reuse objects flag is true (the default value).


SQL400 - This is added to test the reuse objects flag.  It mirrors the work
         done in variation 28 but we expect the previous result set object to 
         still be able to be used because it is the same object as the new 
         result set object (the object was reused).
**/
    public void Var030()
    {
        if (checkNative()) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
                ResultSet rs = ps.executeQuery ();
                rs.next ();
                ps.execute ();

                boolean success = true;
                try {
                    rs.next ();
                }
                catch (SQLException e) {
                    success = false;
                }

                ps.close ();
                assertCondition (success);
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
    public void Var031()
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

                    PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + table3_ + " SELECT ONE FROM " + table2_);
                    updateCount = ps.executeUpdate();
                    ps.close();

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
result set row cache didn't get reported properly.  This ensures that problem
is fixed.
**/
    public void Var032()
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

                PreparedStatement ps = connection_.prepareStatement("SELECT * FROM " + table2_);
                ResultSet rs = ps.executeQuery();
                rs.next();
                rs.next();
                int value1 = rs.getInt(1);

                rs = ps.executeQuery();
                rs.next();
                rs.next();
                int value2 = rs.getInt(1);

                ps.close();

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
execute() - Verify that lobs can be inserted, even when the declared width of the columns
            is greater than 3.5 gig.  This is a bug found in V5R2 native driver by SAP
            This will need to be fixed in V5R2. 

**/
    public void Var033()
    {
	if ( (getRelease() <= JDTestDriver.RELEASE_V7R1M0) && (getDriver() == JDTestDriver.DRIVER_NATIVE)) {
	    notApplicable("Native driver V5R2 and beyond test");
	} else {

	    try {

		Statement stmt = connection_.createStatement();

		try {
		    stmt.executeUpdate("drop table "+table4_); 
		} catch (Exception e) {
		 // just ignore 
		} 

		stmt.executeUpdate("create table "+table4_+"(b1 blob(1000M), b2 blob(1000M), b3 blob(1000M), b4 blob(1000M))");

		PreparedStatement pstmt = null;

		byte[] b1 = new byte[10];
		byte[] b2 = new byte[100];
		byte[] b3 = new byte[1000];
		byte[] b4 = new byte[10000];
		String sql = "insert into "+table4_+" values(?,?,?,?)";
		pstmt=connection_.prepareStatement(sql); 
		pstmt.setBytes(1, b1);
		pstmt.setBytes(2, b2);
		pstmt.setBytes(3, b3);
		pstmt.setBytes(4, b4);

	        //
	        // This should work..
	        // 
		pstmt.executeUpdate(); 

	        //
	        // now cleanup
	        //
		stmt.executeUpdate("drop table "+table4_);

		succeeded ();


	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception -- added by native driver 8/18/2003 ");
	    }
	}
    }

/**
execute() - Verify that lobs cannot be inserted even when the width of the inserted columns
            is greater than 3.5 gig.  This is a bug found in V5R2 native driver by SAP
            This will need to be fixed in V5R2. 

**/
    public void Var034() {

	if ( (isToolboxDriver()) || ((getRelease() <= JDTestDriver.RELEASE_V7R1M0)
        && (getDriver() == JDTestDriver.DRIVER_NATIVE))) {
      notApplicable("Native driver V5R3 and beyond test");
    } else {
      if (checkJdbc20()) {
        // Don't try running this on J9
        String vmName = System.getProperty("java.vm.name");
        if (vmName.indexOf("Classic VM") == -1) {
          notApplicable("Cannot run this test in J9 (it has 1 gig lobs)");
        } else {
          try {

            Statement stmt = connection_.createStatement();

            try {
              stmt.executeUpdate("drop table " + table4_);
            } catch (Exception e) {
              // just ignore
            }

            stmt
                .executeUpdate("create table "
                    + table4_
                    + "(b1 blob(1000M), b2 blob(1000M), b3 blob(1000M), b4 blob(1000M))");

            PreparedStatement pstmt = null;

            byte[] b1 = new byte[1000000000];
            byte[] b2 = b1;
            byte[] b3 = b1;
            byte[] b4 = b1;
            String sql = "insert into " + table4_ + " values(?,?,?,?)";
            pstmt = connection_.prepareStatement(sql);
            System.out.println("Done preparing...");
            pstmt.setBytes(1, b1);
            System.out.println("1 done...");
            pstmt.setBytes(2, b2);
            System.out.println("2 done...");
            pstmt.setBytes(3, b3);
            System.out.println("3 done...");
            pstmt.setBytes(4, b4);
            System.out.println("4 done...");

            boolean failed = false;
            try {
              //
              // This should not work..
              // 
              pstmt.executeUpdate();

              failed = false;
            } catch (SQLException sqlEx) {
              failed = true;

              if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
                if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
                  if ((sqlEx.getMessage().indexOf(
                      "Row length exceeds 3.5 gigabytes") >= 0)
                      || (sqlEx.getMessage().indexOf(
                          "ROW LENGTH EXCEEDS 3.5 GIGABYTES") >= 0)) {
                    // we found the right error message
                  } else {
                    System.out
                        .println("Unexpected error message -- looking for SQLDA not valid ");
                    sqlEx.printStackTrace();
                    failed = false;
                  }
                } else {
                  if ((sqlEx.getMessage().indexOf("SQLDA not valid") >= 0)
                      || (sqlEx.getMessage().indexOf("SQLDA NOT VALID") >= 0)) {
                    // we found the right error message
                  } else {
                    System.out
                        .println("Unexpected error message -- looking for SQLDA not valid ");
                    sqlEx.printStackTrace();
                    failed = false;
                  }
                }
              }

            }

            //
            // now cleanup
            //
            stmt.executeUpdate("drop table " + table4_);

            assertCondition(failed);

          } catch (Exception e) {
            failed(e,
                "Unexpected Exception -- added by native driver 8/18/2003 ");
          }
        }
      }
    }

  }

/**
execute() - Verify that we can handle the new array insert syntax -- See issue 43713
**/
    public void Var035() {
      String description = "New Array insert syntax test"; 
    if (checkRelease710()) {
      StringBuffer testInfo = new StringBuffer(); 
      boolean passed = true; 
      String sql = ""; 
      try {
        Statement s = connection_.createStatement();
        String tablename = JDPSTest.COLLECTION+".JDPSEX35"; 
        try {
          s.executeUpdate("drop table "+tablename);  
        } catch (Exception e) { 
        }
        sql = "create table "+tablename+"(c1 int)"; 
        s.executeUpdate(sql); 
        sql = "insert into "+tablename+" values ?,?,?"; 
        PreparedStatement ps =connection_.prepareStatement(sql);
        ps.setInt(1,1); 
        ps.setInt(2,2); 
        ps.setInt(3,3); 
        ps.executeUpdate();
        ps.close(); 
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
    execute() Execute a prepared statement twice, where the first statement encounters an
              error on the combined open/fetch.  See issue 44980 for an example.

    **/
        public void Var036() {
	    String sql = "";
	    String description = " Testing executing a prepared statement twice where the first statement has an error on the combined open/fetch added 10/19/2010"; 
	    try {
		String exception1;
		String exception2;
		String exception3;
		String expectedException = "Value in date, time, or timestamp string not valid";
		sql = "select IBMREQD  from sysibm.sysdummy1 where CURRENT DATE BETWEEN ? and  CAST(CAST(IBMREQD AS VARCHAR(80)) AS DATE)";

		PreparedStatement ps = connection_.prepareStatement(sql);
		try {
		    ps.setString(1,"1997-01-01"); 
		    ResultSet rs = ps.executeQuery();
		    rs.next(); 
		    exception1="Exception did not occur on first execute"; 
		} catch (Exception e) {
		    exception1=e.toString(); 
		}

		try {
		    ps.setString(1,"1997-01-01"); 
		    ResultSet rs = ps.executeQuery();
		    rs.next(); 
		    exception2="Exception did not occur on second execute"; 
		} catch (Exception e) {
		    exception2=e.toString(); 
		}

		try {
		    ps.setString(1,"1997-01-01"); 
		    ResultSet rs = ps.executeQuery();
		    rs.next(); 
		    exception3="Exception did not occur on second execute"; 
		} catch (Exception e) {
		    exception3=e.toString(); 
		}


		ps.close();
		assertCondition(exception1.indexOf(expectedException) >= 0 &&
				exception2.indexOf(expectedException) >= 0 &&
				exception3.indexOf(expectedException) >= 0,
				"exception1 = "+exception1+"\n"+
				"exception2 = "+exception2+"\n"+
				"exception3 = "+exception3+"\n"+
				"expected   = "+expectedException+"\n"+description); 



	    } catch (Exception e) {
		failed(e, "Unexpected exception last sql = " + sql+" "+description);
		return;

	    }

      }

  /**
   * Execute a values clause
   **/
  public void Var037() {
    String description = "Values('abc') execution test -- added 10/23/2012 to detect bug in native JDBC driver";
    if (checkRelease610()) {   /* values only in v6r1 and later */ 
	StringBuffer testInfo = new StringBuffer();
	boolean passed = true;
	String sql = "";
	try {

	    sql = "values('abc')";
	    PreparedStatement ps = connection_.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();
	    if (rs.next()) {
		String outString = rs.getString(1);
		String expected = "abc";
		if (!outString.equals(expected)) {
		    passed = false;
		    testInfo.append("FAILED:  got " + outString + " expected " + expected
				    + "\n");
		}
	    } else {
		passed = false;
		testInfo.append("FAILED No rows returned from query: " + sql + "\n");
	    }
	    ps.close();

	    assertCondition(passed, description + testInfo.toString());
	} catch (Exception e) {
	    failed(e, "Unexpected exception last sql = " + sql + " " + description);
	    return;
	}
    }
  }

  /**
   * Execute a values clause
   **/
  public void Var038() {
    String description = "Values('abc' || ? ) execution test -- added 10/23/2012 to detect bug in native JDBC driver";

    if (checkRelease710()) {
    StringBuffer testInfo = new StringBuffer();
    boolean passed = true;
    String sql = "";
    try {

      sql = "values('abc' || ? )";
      PreparedStatement ps = connection_.prepareStatement(sql);
      ps.setString(1,"xyz"); 
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        String outString = rs.getString(1);
        String expected = "abcxyz";
        if (!outString.equals(expected)) {
          passed = false;
          testInfo.append("FAILED:  got " + outString + " expected " + expected
              + "\n");
        }
      } else {
        passed = false;
        testInfo.append("FAILED No rows returned from query: " + sql + "\n");
      }
      ps.close();

      assertCondition(passed, description + testInfo.toString());
    } catch (Exception e) {
      failed(e, "Unexpected exception last sql = " + sql + " " + description);
      return;
    }
  }
  }




  /**
   * executeLargeUpdate() - Execute an update with a closed statement. Should
   * throw an exception.
   **/
  public void Var039() {
    if (checkJdbc42()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("UPDATE " + table_
            + " SET SCORE=5 WHERE ID > 10");
        ps.close();
        JDReflectionUtil.callMethod_L(ps, "executeLargeUpdate");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * executeLargeUpdate() - Pass a query that returns a result set. Should throw
   * an exception.
   * <P>
   * SQL400 - As of v4r5, the native driver is going to stop throwing exceptions
   * for this behavior and simple let it work. We will test that the ResultSet
   * can be obtained with getResultSet() and that it is valid. As of v5r1 the
   * native driver is going to begin throwing exceptions for this behavior so we
   * are compliant with J2EE.
   **/
  public void Var040() {
    if (checkJdbc42()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
        long updateCount = JDReflectionUtil.callMethod_L(ps,
            "executeLargeUpdate");

        failed("Didn't throw SQLException updateCount =" + updateCount);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * executeLargeUpdate() - Calls a stored procedure that returns a result set.
   * Should throw an exception.
   **/
  public void Var041() {
    if (checkJdbc42()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("CALL "
            + JDSetupProcedure.STP_RS1);
        JDReflectionUtil.callMethod_L(ps, "executeLargeUpdate"); // @C1C -
                                                                 // changed back
                                                                 // to
                                                                 // executeLargeUpdate()
        // since that is specifically what
        // this variation is for.

        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * executeLargeUpdate() - Execute when no parameters are set, and the
   * statement does not contain any parameter markers. Should work.
   **/
  public void Var042() {
    if (checkJdbc42()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("UPDATE " + table_
            + " SET SCORE=2 WHERE ID > 1");
        JDReflectionUtil.callMethod_L(ps, "executeLargeUpdate");
        ps.close();
        succeeded();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * executeLargeUpdate() - Execute when no parameters are set, and the
   * statement does not contain any parameter markers. Should throw an
   * exception.
   **/
  public void Var043() {
    if (checkJdbc42()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("UPDATE " + table_
            + " SET SCORE=? WHERE ID > 1");
        JDReflectionUtil.callMethod_L(ps, "executeLargeUpdate");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * executeLargeUpdate() - Verify that the update count returned is correct
   * when no rows are updated.
   **/
  public void Var044() {
    if (checkJdbc42()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("UPDATE " + table_
            + " SET SCORE=? WHERE ID > 10");
        ps.setInt(1, 5);
        long updateCount = JDReflectionUtil.callMethod_L(ps,
            "executeLargeUpdate");
        ps.close();
        assertCondition(updateCount == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * executeLargeUpdate() - Verify that the update count returned is correct
   * when some rows are updated.
   **/
  public void Var045() {
    if (checkJdbc42()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("UPDATE " + table_
            + " SET SCORE=? WHERE ID > 1");
        ps.setInt(1, 5);
        long updateCount = JDReflectionUtil.callMethod_L(ps,
            "executeLargeUpdate");
        ps.close();
        assertCondition(updateCount == 2);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * executeLargeUpdate() - Verify that the update count returned is correct
   * when a stored procedure is called.
   **/
  public void Var046() {
    if (checkJdbc42()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("CALL "
            + JDSetupProcedure.STP_RS0);
        long updateCount = JDReflectionUtil.callMethod_L(ps,
            "executeLargeUpdate");
        ps.close();
        assertCondition(updateCount == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * executeLargeUpdate() - Verify that updates that use subqueries work
   * correctly.
   * 
   * SQL400 - There was some discussion of whether or not a call to
   * executeLargeUpdate should work with a subselect because no queries are
   * allowed through the executeLargeUpdate method. It is our take that a
   * subselect is not a query but a way of specifying the input values to an
   * update. Therefore, the subselect should work through executeLargeUpdate and
   * no exception should be thrown.
   **/
  public void Var047() {
    if (checkJdbc42()) {
      if (checkNative()) {
        try {
          long updateCount = 0;

          Statement s = connection_.createStatement();

          try {
            s.executeUpdate("DROP TABLE " + table2_);
          } catch (SQLException one) {
            // Ignore it...
          }

          try {
            s.executeUpdate("DROP TABLE " + table3_);
          } catch (SQLException one) {
            // Ignore it...
          }

          try {
            s.executeUpdate("CREATE TABLE " + table2_
                + " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
            s.executeUpdate("INSERT INTO " + table2_ + " VALUES('one')");
            s.executeUpdate("INSERT INTO " + table2_ + " VALUES('two')");
            s.executeUpdate("CREATE TABLE " + table3_
                + " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");

            PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
                + table3_ + " SELECT ONE FROM " + table2_);
            updateCount = JDReflectionUtil.callMethod_L(ps,
                "executeLargeUpdate");
            ps.close();

          } catch (SQLException two) {
            failed(two, "Unexpected Exception");
          }

          s.executeUpdate("DROP TABLE " + table2_);
          s.executeUpdate("DROP TABLE " + table3_);
          s.close();

          assertCondition(updateCount == 2);

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * execute() - Verify that lobs can be inserted, even when the declared width
   * of the columns is greater than 3.5 gig. This is a bug found in V5R2 native
   * driver by SAP This will need to be fixed in V5R2.
   **/
  public void Var048() {
    if (checkJdbc42()) {

      try {

        Statement stmt = connection_.createStatement();

        try {
          stmt.executeUpdate("drop table " + table4_);
        } catch (Exception e) {
          // just ignore
        }

        stmt.executeUpdate("create table "
            + table4_
            + "(b1 blob(1000M), b2 blob(1000M), b3 blob(1000M), b4 blob(1000M))");

        PreparedStatement pstmt = null;

        byte[] b1 = new byte[10];
        byte[] b2 = new byte[100];
        byte[] b3 = new byte[1000];
        byte[] b4 = new byte[10000];
        String sql = "insert into " + table4_ + " values(?,?,?,?)";
        pstmt = connection_.prepareStatement(sql);
        pstmt.setBytes(1, b1);
        pstmt.setBytes(2, b2);
        pstmt.setBytes(3, b3);
        pstmt.setBytes(4, b4);

        //
        // This should work..
        //
        JDReflectionUtil.callMethod_L(pstmt, "executeLargeUpdate");

        //
        // now cleanup
        //
        stmt.executeUpdate("drop table " + table4_);

        succeeded();

      } catch (Exception e) {
        failed(e, "Unexpected Exception -- added by native driver 8/18/2003 ");
      }
    }
  }

  /**
   * execute() - Verify that we can handle the new array insert syntax -- See
   * issue 43713
   **/
  public void Var049() {
    if (checkJdbc42()) {
      String description = "New Array insert syntax test";
      if (checkRelease710()) {
        StringBuffer testInfo = new StringBuffer();
        boolean passed = true;
        String sql = "";
        try {
          Statement s = connection_.createStatement();
          String tablename = JDPSTest.COLLECTION + ".JDPSEX35";
          try {
            s.executeUpdate("drop table " + tablename);
          } catch (Exception e) {
          }
          sql = "create table " + tablename + "(c1 int)";
          s.executeUpdate(sql);
          sql = "insert into " + tablename + " values ?,?,?";
          PreparedStatement ps = connection_.prepareStatement(sql);
          ps.setInt(1, 1);
          ps.setInt(2, 2);
          ps.setInt(3, 3);
          JDReflectionUtil.callMethod_L(ps, "executeLargeUpdate");
          ps.close();
          sql = "select * from " + tablename + " order by c1";
          ResultSet rs = s.executeQuery(sql);
          rs.next();
          int value = rs.getInt(1);
          int expected = 1;
          if (value != expected) {
            testInfo.append("Expected " + expected + " got " + value);
            passed = false;
          }
          rs.next();
          value = rs.getInt(1);
          expected = 2;
          if (value != expected) {
            testInfo.append("Expected " + expected + " got " + value);
            passed = false;
          }
          rs.next();
          value = rs.getInt(1);
          expected = 3;
          if (value != expected) {
            testInfo.append("Expected " + expected + " got " + value);
            passed = false;
          }
          rs.close();
          s.close();
          assertCondition(passed, description + testInfo.toString());
        } catch (Exception e) {
          failed(e, "Unexpected exception last sql = " + sql + " "
              + description);
          return;
        }
      }
    }

  }


  /* Verify that the error are reported on fetch, not execute */
  public void testFetchError(String orderByClause) {
      StringBuffer sb = new StringBuffer(); 
      try {
	  sb.append("STEPS\n");
	  String sql = "SELECT "+JDPSTest.COLLECTION+".RET_ERROR2(INDATA,'"+orderByClause+"') "+
	               " FROM "+tableErr_ + orderByClause;
	  sb.append("Prepare "+sql+"\n"); 
	  PreparedStatement ps = connectionNoPrefetch_.prepareStatement (sql);
	  sb.append("Executing Query\n"); 
	  ResultSet rs = ps.executeQuery();

	  int currentRow = 0; 
	  sb.append("Calling rs.next()\n");

	  boolean rowAvailable = true;
	  boolean tryFetch = true;
	  while (rowAvailable && currentRow < 10 ) {
	      tryFetch = true; 
	      while(tryFetch && currentRow < 10 ) { 
		  try {
		      sb.append("Calling rs.next() after row "+currentRow+"\n");
		      rowAvailable = rs.next();
		      tryFetch = false;
		      currentRow++; 
		  } catch (Exception e) {
		      currentRow++;
		      sb.append("Exception caught on row "+currentRow); 
		      tryFetch = true; 
		  } 
	      } /* while tryFetch */ 
	  } /* rowAvilable */ 
	  rs.close(); 
	  assertCondition (true);
      }
      catch (Exception e) {
	  failed (e, "Unexpected Exception -- Exception should not be thrown until FETCH\n"+sb.toString());
      }

  } 
  
  public void Var050()  { testFetchError(" ORDER BY ERROR_MIDDLE"); }
  public void Var051()  { testFetchError(" ORDER BY ERROR_FIRST"); }
  public void Var052()  { testFetchError(" ORDER BY ERROR_LAST"); } 

  /* Test to make sure SQLCODE is correct when multiple threads running
   * Reported by JTOpen Bug#371 */ 
  public void Var053() {
   StringBuffer sb = new StringBuffer(); 
   try {
       boolean passed = true; 
       String sql;
       Statement stmt = connection_.createStatement(); 
       String tableName = JDPSTest.COLLECTION+".JDPSEX53";
       try {
	   sql =  "DROP TABLE "+tableName;
	   sb.append("Executing "+sql+"\n"); 
	   stmt.executeUpdate(sql); 
       } catch (Exception ex) {
	   sb.append("Drop table failed with \n");
	   
	   printStackTraceToStringBuffer(ex, sb); 
       } 
       sql = "CREATE TABLE "+tableName+" (a int primary key  constraint "+JDPSTest.COLLECTION+".valconst check(a<20), b int)";
       sb.append("Executing "+sql+"\n"); 
       stmt.executeUpdate(sql);
       String sql1 = "insert into "+tableName+" values(1,1)";
       sb.append("Executing "+sql1+"\n"); 
       stmt.executeUpdate(sql1);

       // Make sure the message queue in the jobs wrap
       stmt.execute("CALL QSYS.QCMDEXC('CHGJOB JOBMSGQFL(*WRAP)          ', 0000000030.00000)");

       Statement stmt2 = connection2_.createStatement();
      stmt2.execute("CALL QSYS.QCMDEXC('CHGJOB JOBMSGQFL(*WRAP)          ', 0000000030.00000)");
      stmt2.close();

       // Create the two prepared statements that will fail
       sb.append("Preparing "+sql1+"\n"); 
       PreparedStatement ps1 = connection_.prepareStatement(sql1);
       String sqlState1="23505";

       String sql2 = "insert into "+tableName+" values(99,1)";
       sb.append("Preparing "+sql2+"\n"); 
       PreparedStatement ps2 = connection2_.prepareStatement(sql2);
       String sqlState2="23513"; 
       //
       // Verify that they fail as expected
       //
       try {
	   sb.append("Executing "+sql1+"\n");
	   ps1.execute();
	   sb.append("Exception did not occur\n"); 
	   passed = false; 
       } catch (SQLException e) {
	   String sqlState = e.getSQLState();
	   if (!sqlState.equals(sqlState1)) {
	       sb.append("FAILED -- Got sqlState = "+sqlState+ " sb  "+sqlState1+" from sql1\n");
	       passed = false; 
	       printStackTraceToStringBuffer(e, sb); 
	   } else { 
	       sb.append("Exception on ps1 occured as expected\n");
	   }
       }
       

       try {
	   sb.append("Executing "+sql2+"\n");
	   ps2.execute();
	   sb.append("Exception did not occur\n"); 
	   passed = false; 
       } catch (SQLException e) {
	   String sqlState = e.getSQLState();
	   if (!sqlState.equals(sqlState2)) {
	       sb.append("FAILED -- Got sqlState = "+sqlState+ " sb  "+sqlState2+" from sql2\n");
	       passed = false; 
	       printStackTraceToStringBuffer(e, sb); 
	   } else { 
	       sb.append("Exception on ps2 occured as expected\n");
	   }
       }

     

       long endTime = System.currentTimeMillis() + 20000;
       JDPSExecuteRunnable run1 = new JDPSExecuteRunnable(sql1,ps1,sqlState1,endTime);
       JDPSExecuteRunnable run2 = new JDPSExecuteRunnable(sql2,ps2,sqlState2,endTime);

       Thread thread1 = new Thread(run1);
       Thread thread2 = new Thread(run2);
       thread1.start();
       thread2.start();

       thread1.join();
       thread2.join();

       if (!run1.passed()) {
	   sb.append("Thread 1 failed\n");
	   sb.append(run1.getStatus());
	   passed = false; 
       }
       if (!run2.passed()) {
	   sb.append("Thread 2 failed\n");
	   sb.append(run2.getStatus());
	   passed = false; 
       }

       assertCondition(passed, sb); 
       
   } catch (Exception e) {
	  failed (e, "Unexpected Exception --\n"+sb.toString());

   }
  } 

  /* Test to sure set connection statement works */
  public void Var054() {

    if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
	notApplicable("NATIVE driver does not support connect to statement");
	return; 
    }
   StringBuffer sb = new StringBuffer();
   sb.append("Set connection test:  CPS-AXCLXR -- toolbox duplicate records\n");
   try {
       boolean passed = true; 
       String sql;
       Connection connection =  testDriver_.getConnection (baseURL_ + ";errors=full;date format=iso", 
           userId_, encryptedPassword_);

       Statement stmt = connection.createStatement(); 
       String tableName = JDPSTest.COLLECTION+".JDPSEX54";
       
       
       sql = "connect to mememem";
       sb.append("Executing "+sql+"\n"); 
       stmt.executeUpdate(sql);

       try {
      	 sql =  "DROP TABLE "+tableName;
      	 sb.append("Executing "+sql+"\n"); 
      	 stmt.executeUpdate(sql); 
       	} catch (Exception ex) {
       		sb.append("Drop table failed with \n");
       		printStackTraceToStringBuffer(ex, sb); 
       } 
       sql = "CREATE TABLE "+tableName+" (a int)";
       sb.append("Executing "+sql+"\n"); 
       stmt.executeUpdate(sql);
       sql = "insert into "+tableName+" values(1)";
       sb.append("Executing "+sql+"\n"); 
       stmt.executeUpdate(sql);
       sql = "set connection mememem";
       sb.append("Executing "+sql+"\n"); 
       stmt.executeUpdate(sql);
       sql = "select * from "+tableName; 
       sb.append("Executing "+sql+"\n"); 
       ResultSet rs = stmt.executeQuery(sql);
       if (!rs.next()) {
      	 passed = false; 
      	 sb.append("ERROR: No results\n"); 
       } else {
      	 if (rs.getInt(1) != 1) {
      		 passed = false; 
      		 sb.append("ERROR: Got "+rs.getInt(1)+" sb 1 \n"); 
      	 }
      	 if (rs.next()) {
      		 passed = false; 
      		 sb.append("ERROR:  Got second row\n"); 
      	 }
       }
       rs.close(); 
       
    	 sql =  "DROP TABLE "+tableName;
    	 sb.append("Executing "+sql+"\n"); 
    	 stmt.executeUpdate(sql); 
       connection.close(); 
       
       assertCondition(passed, sb); 


   } catch (Exception e) {
	  failed (e, "Unexpected Exception --\n"+sb.toString());

   }
  } 

  /* Test to sure set connection statement works */
  public void Var055() {
    if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
	notApplicable("NATIVE driver does not support connect to statement");
	return; 
    }

   StringBuffer sb = new StringBuffer();
   sb.append("Set connection test:  CPS-AXCLXR -- toolbox duplicate records -- use prepared statements without reuse \n");
   try {
       boolean passed = true; 
       String sql;
       Connection connection =  testDriver_.getConnection (baseURL_ + ";errors=full;date format=iso", 
           userId_, encryptedPassword_);
       Statement stmt = connection.createStatement(); 
       String tableName = JDPSTest.COLLECTION+".JDPSEX54";
       
       
       sql = "connect to mememem";
       sb.append("Executing "+sql+"\n"); 
       stmt.executeUpdate(sql);

       try {
      	 sql =  "DROP TABLE "+tableName;
      	 sb.append("Executing "+sql+"\n"); 
      	 stmt.executeUpdate(sql); 
       	} catch (Exception ex) {
       		sb.append("Drop table failed with \n");
       		printStackTraceToStringBuffer(ex, sb); 
       } 
       sql = "CREATE TABLE "+tableName+" (a int)";
       sb.append("Executing "+sql+"\n"); 
       stmt.executeUpdate(sql);
       
       sql = "insert into "+tableName+" values(1)";
       sb.append("Executing "+sql+"\n"); 
       PreparedStatement pstmt=connection.prepareStatement(sql);
       pstmt.execute(); 
       sql = "set connection mememem";
       sb.append("Executing "+sql+"\n"); 
       pstmt=connection.prepareStatement(sql);
       pstmt.execute(); 
      
       
       sql = "select * from "+tableName; 
       sb.append("Executing "+sql+"\n"); 
       ResultSet rs = stmt.executeQuery(sql);
       if (!rs.next()) {
      	 passed = false; 
      	 sb.append("ERROR: No results\n"); 
       } else {
      	 if (rs.getInt(1) != 1) {
      		 passed = false; 
      		 sb.append("ERROR: Got "+rs.getInt(1)+" sb 1 \n"); 
      	 }
      	 if (rs.next()) {
      		 passed = false; 
      		 sb.append("ERROR:  Got second row\n"); 
      	 }
       }
       rs.close(); 
       
       
       
       assertCondition(passed, sb); 
    	 sql =  "DROP TABLE "+tableName;
    	 sb.append("Executing "+sql+"\n"); 
    	 stmt.executeUpdate(sql); 
       
   } catch (Exception e) {
	  failed (e, "Unexpected Exception --\n"+sb.toString());

   }
  } 

  /* Test to sure set connection statement works */
  public void Var056() {
    if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
	notApplicable("NATIVE driver does not support connect to statement");
	return; 
    }

   StringBuffer sb = new StringBuffer();
   sb.append("Set connection test:  CPS-AXCLXR -- toolbox duplicate records -- use prepared statements with reuse \n");
   try {
       boolean passed = true; 
       String sql;
       Connection connection =  testDriver_.getConnection (baseURL_ + ";errors=full;date format=iso", 
           userId_, encryptedPassword_);
       Statement stmt = connection.createStatement(); 
       String tableName = JDPSTest.COLLECTION+".JDPSEX54";
       
       
       sql = "connect to mememem";
       sb.append("Executing "+sql+"\n"); 
       stmt.executeUpdate(sql);

       try {
      	 sql =  "DROP TABLE "+tableName;
      	 sb.append("Executing "+sql+"\n"); 
      	 stmt.executeUpdate(sql); 
       	} catch (Exception ex) {
       		sb.append("Drop table failed with \n");
       		printStackTraceToStringBuffer(ex, sb); 
       } 
       sql = "CREATE TABLE "+tableName+" (a int)";
       sb.append("Executing "+sql+"\n"); 
       stmt.executeUpdate(sql);
       
       sql = "insert into "+tableName+" values(1)";
       sb.append("Executing "+sql+"\n"); 
       PreparedStatement pstmt=connection.prepareStatement(sql);
       pstmt.execute(); 
       pstmt.close(); 
       sql = "set connection mememem";
       sb.append("Executing "+sql+"\n"); 
       pstmt=connection.prepareStatement(sql);
       pstmt.execute(); 
      
       
       sql = "select * from "+tableName; 
       sb.append("Executing "+sql+"\n"); 
       ResultSet rs = stmt.executeQuery(sql);
       if (!rs.next()) {
      	 passed = false; 
      	 sb.append("ERROR: No results\n"); 
       } else {
      	 if (rs.getInt(1) != 1) {
      		 passed = false; 
      		 sb.append("ERROR: Got "+rs.getInt(1)+" sb 1 \n"); 
      	 }
      	 if (rs.next()) {
      		 passed = false; 
      		 sb.append("ERROR:  Got second row\n"); 
      	 }
       }
       rs.close(); 
       
       
       
       assertCondition(passed, sb); 
    	 sql =  "DROP TABLE "+tableName;
    	 sb.append("Executing "+sql+"\n"); 
    	 stmt.executeUpdate(sql); 
       
   } catch (Exception e) {
	  failed (e, "Unexpected Exception --\n"+sb.toString());

   }
  } 


  /* Test to sure set connection statement works */
  public void Var057() {
    if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
	notApplicable("NATIVE driver does not support connect to statement");
	return; 
    }

   StringBuffer sb = new StringBuffer();
   sb.append("Set connection test:  CPS-AXCLXR -- make sure switching between connections works \n");
   try {
       boolean passed = true; 
       String sql;
       Connection connection =  testDriver_.getConnection (baseURL_ + ";errors=full;date format=iso", 
           userId_, encryptedPassword_);
       String catalog = connection.getCatalog(); 
       
       PreparedStatement psBase = connection.prepareStatement("SET CONNECTION "+catalog); 
       PreparedStatement psRemote = connection.prepareStatement("SET CONNECTION MEMEMEM"); 
       Statement stmt = connection.createStatement();  
       sql = "connect to mememem";
       sb.append("Executing "+sql+"\n"); 
       stmt.executeUpdate(sql);

       for (int i = 0; passed && i < 100; i++) { 
      	 psBase.execute(); 
      	 String jobName = queryJobName(connection); 
      	 if (jobName.indexOf("QZDASOINIT") < 0) {
      		 passed = false; 
      		 sb.append("jobName is "+jobName+ " should have QZDASOINIT\n"); 
      	 }
      	 if (passed) { 
        	 psRemote.execute(); 
        	 jobName = queryJobName(connection); 
        	 if (jobName.indexOf("QRWT") < 0) {
        		 passed = false; 
        		 sb.append("jobName is "+jobName+ " should have QRWT\n"); 
        	 }
      		 
      	 }
       }
       
       
       stmt.close(); 
       psBase.close(); 
       psRemote.close(); 
       assertCondition(passed, sb); 
       
   } catch (Exception e) {
	  failed (e, "Unexpected Exception --\n"+sb.toString());

   }
  }



	private String queryJobName(Connection connection) throws SQLException {
		String jobName = null; 
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT JOB_NAME FROM TABLE(QSYS2.ACTIVE_JOB_INFO( 'NO' ,  '',  '*' , '')) X");
		rs.next();
		jobName = rs.getString(1); 
		stmt.close(); 
		return jobName; 
  } 


  
  
  
  
  

}





