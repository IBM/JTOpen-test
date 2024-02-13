///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDBUPSFunctional.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDBUPSFunctional.java
//
// Classes:      JDBUPSFunctional
//
////////////////////////////////////////////////////////////////////////

package test.JD.BU;

import com.ibm.as400.access.AS400;

import test.JDBUTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Timestamp;
import java.util.Hashtable;



/**
Testcase JDBUPSFunctional.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDBUPSFunctional
extends JDTestcase {



    // Private data.
    private Connection          connection_;


/**
Constructor.
**/
    public JDBUPSFunctional (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
    {
        super (systemObject, "JDBUPSFunctional",
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
        try {

            // Get a global connection - choose how you which
            // to get the connection.
            connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_); 
            Statement s = connection_.createStatement();

            // todo:  butestdata is probably not needed....
            try {
                s.executeUpdate("drop table " + JDBUTest.BUTESTDATA);
            } catch (SQLException e) {
                // Ignore it.
            }

            // Create a table that uses the largest row size the database will
            // allow me to use.
            s.executeUpdate("create table  " + JDBUTest.BUTESTDATA +
                            " (col1 int, col2 int, col3 int)");

            for (int i = 1; i <= 20; i++) {
                s.executeUpdate("insert into " + JDBUTest.BUTESTDATA + 
                                " values(" + i + ", " + i + ", " + i + ")");
            }

            s.close();

        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());

        }
    }



/**
**/
    public void newSetupLobs(String connectionParms) 
    {
        Statement s = null;

        try {
            try {
		if (connection_ != null) {
		    connection_.commit(); 
                    connection_.close();
		}
            } catch (SQLException e) {
                System.out.println("Critical Error - couldn't close connection");
            }

             connection_ = testDriver_.getConnection(baseURL_+";" + connectionParms, userId_, encryptedPassword_); 
            connection_.setAutoCommit(false);              
            s = connection_.createStatement();

            try {
                s.executeUpdate("drop table " + JDBUTest.BUTESTLOB);
            } catch (SQLException e) {
                // Ignore it... 
            }

            s.executeUpdate("create table " + JDBUTest.BUTESTLOB + 
                            " (col1 int primary key, col2 BLOB(200), col3 BLOB(200))");

            // NOTE:  It is important here for verification that the first byte is 0.
            byte[] blobBytes = new byte[] { (byte) 0, (byte) -12, (byte) 45, (byte) -33, (byte) 1};

            
            PreparedStatement ps = connection_.prepareStatement("insert into " + JDBUTest.BUTESTLOB + " values(0, ?, ?)");
            ps.setBytes(1, blobBytes);
            ps.setBytes(2, blobBytes);
            ps.executeUpdate();
            ps.close();

            connection_.commit();

        } catch (Exception e) {
            System.out.println("Caught exception: ");
            e.printStackTrace();
        } finally {
            try {
                if (s != null)
                    s.close();
            } catch (SQLException e) {
                System.out.println("Critical Error closing statement()");
            }
        }
    }



/**
Do the same thing we did for the common case batch above, but make the table 
be full of LOB columns so that we can see that we handle locators right. 
**/
    public PreparedStatement buildBatchLobs(int size) 
    throws SQLException
    {
        PreparedStatement ps = 
        connection_.prepareStatement("insert into " + JDBUTest.BUTESTLOB + " values(?, ?, ?)");


        for (int i = 1; i <= size; i++) {

            byte byteValue = (byte) (i % 100);
            byte[] blobBytes = new byte[] { byteValue, byteValue, byteValue, byteValue, byteValue};

            ps.setInt(1, i);
            ps.setBytes(2, blobBytes);
            ps.setBytes(3, blobBytes);
            System.out.println("Building batch input value is " + byteValue);
            ps.addBatch();
        }

        return ps;
    }



/**
This is the place to put all cleanup work for the testcase.
**/   
    public void cleanup() {
        try {

            // Close the global connection opened in setup().
	    connection_.commit(); 
            connection_.close();

        } catch (Exception e) {
            System.out.println("Caught exception: ");

        }
    }



    public void newSetup(String connectionParms) 
    {
        Statement s = null;

        try {
            try {
		if (connection_ != null) {
		    connection_.commit(); 
                    connection_.close();
		}
            } catch (SQLException e) {
                System.out.println("Critical Error - couldn't close connection");
            }

            connection_ = testDriver_.getConnection(baseURL_+";" + connectionParms, userId_, encryptedPassword_); 
            connection_.setAutoCommit(false);              
            s = connection_.createStatement();

            try {
                s.executeUpdate("drop table " + JDBUTest.BUTEST);
            } catch (SQLException e) {
                // Ignore it... 
            }

            s.executeUpdate("create table " + JDBUTest.BUTEST + 
                            " (col1 int primary key, col2 int, col3 int)");

            s.executeUpdate("insert into " + JDBUTest.BUTEST + 
                            " values(0, 0, 0)");

            connection_.commit();

        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        } finally {
            try {
                if (s != null)
                    s.close();
            } catch (SQLException e) {
                System.out.println("Critical Error closing statement()");
            }
        }
    }


    public PreparedStatement buildBatch(int size,
                                        int failurePos) 
    throws SQLException
    {
        PreparedStatement ps = 
        connection_.prepareStatement("insert into " + JDBUTest.BUTEST + " values(?, ?, ?)");

        if (failurePos < 1) {
            for (int i = 1; i <= size; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i);
                }
                ps.addBatch();
            }
        } else {

            for (int i = 1; i < failurePos; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i);
                }
                ps.addBatch();
            }

            // Insert the failure entry
            for (int j = 1; j <= 3; j++) {
                ps.setInt(j, 0);
            }
            ps.addBatch();

            for (int i = failurePos + 1; i <= size; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i);
                }
                ps.addBatch();
            }


        }

        return ps;
    }



    protected int verifyRows() 
    throws SQLException 
    {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDBUTest.BUTEST);

        int count = 0;
        while (rs.next()) {
            count++;
            //if ((rs.getInt(1) != count) ||
            //    (rs.getInt(2) != count) ||
            //    (rs.getInt(3) != count)) {
            //    success = false;
            //    break;
            //}

        }

        s.close();
        return count;
    }



/**
Verify that you do not have to bind all the parameters after the first 
row has been handled.

It works like this:  When you are using the execute methods of the Prepared
Statement class, you must set all the parameters before the statement can 
be executed, but after that, any parameters that you do not change maintain
the same value.  The same applies to parameters set for batch processing.
Once all the values are set, you only need to set the parameters that you 
wish to change for each subsequent set.

Note:  JDBC for UDB on NT does not work this way.  They require you to set 
all the parameters for each run.  The SQLException will be thrown when you
attempt to do the executeBatch() request (not at the time that the addBatch()
request happens).

Scenario:
prepareStatement("stmt ?, ?")
setint(1)
setint(2)
addbatch()
setint(1)
addbatch() <-- works - all the parameters have been set.
**/
    public void Var001() {
        try {
            newSetup("");

            // Build a batch with one value in it.
            PreparedStatement ps = buildBatch(1, 0);

            ps.setInt(1, 1000);
            ps.setInt(2, 2000);
            // Don't set the final parameter.  This should work.

            ps.addBatch();
            int[] counts = ps.executeBatch();

            // Verify that the rows are in the table.
            assertCondition(verifyRows() == 3, "verifyRows did not return 3 counts.length="+counts.length);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
Verify that you do have to bind all the parameters before adding the first
row to the batch.

It works like this:  When you are using the execute methods of the Prepared
Statement class, you must set all the parameters before the statement can 
be executed.  The same applies to adding a statement to the batch.  If all 
the parametes are not know, the batch can't be created.

Note:  JDBC for UDB on NT does not work this way.  They require you to set 
all the parameters for each run but will allow you to do the addBatch() call.
They will wait until the call to execute to throw the SQLException.

Scenario:
prepareStatement("stmt ?, ?")
setint(1)
addbatch() <-- fails - parm 2 not set.
**/
    public void Var002() {
        try {
            newSetup("");

            // Build a batch with one value in it.
            PreparedStatement ps = buildBatch(0, 0);

            ps.setInt(1, 1000);
            ps.setInt(2, 2000);
            // Note:  we don't set the final batch parm here...

            try {
                ps.addBatch();
                failed("Didn't throw SQLException");
            } catch (Exception expected) {
                 assertExceptionIsInstanceOf (expected, "java.sql.SQLException");
            } 
            
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }




/**
Verify that all the parameters are forced to be set for each execution.
When the user clears the parameters for a batch row, verify that we 
keep track of the fact that none of them are set again and throw 
throw an SQLException.

setint(1)
setint(2)
addbatch()
clearParameters()
addbatch() <-- fails - parm not set.
**/
    public void Var003() {
        try {
            newSetup("");

            // Build a batch with one value in it.
            PreparedStatement ps = buildBatch(1, 0);

            // All parms are set for the new row and then
            // cleared...
            ps.setInt(1, 1000);
            ps.setInt(2, 2000);
            ps.setInt(3, 3000);
            ps.clearParameters();

            try {
                ps.addBatch();
                failed("Didn't throw SQLException");
            } catch (Exception expected) {
                 assertExceptionIsInstanceOf (expected, "java.sql.SQLException");
            } 
            
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
Verify that clearParameters can be called before executeBatch.  It has no 
effect because the parameters that are cleared are never added to the batch.

setint(1)
setint(2)
addbatch()
clearParameters()
executebatch() <-- works - clear had no effect
**/
    public void Var004() {
	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
	    getDriverFixLevel() <= 704) {
	    System.out.println("WARNING:  New Testcase not working in JTOpen 7.4");
	    assertCondition(true);

	    return; 
	}

	String description = "clearParameters after addbatch.. clear should have no effect.  Note:  This does not work in JTOpen 7.4 driverFixLevel="+getDriverFixLevel(); 
        try {
            newSetup("");

            // Build a batch with two value in it.
            PreparedStatement ps = buildBatch(2, 0);
            ps.clearParameters();  // Note: clearParameters to have not effect at this point.

            int[] counts = ps.executeBatch();

            // Verify that the rows are in the table.
            assertCondition(verifyRows() == 3, "verifyRows != 3 counts.length="+counts.length+" "+description);

        } catch (Exception e) {
            failed (e, "Unexpected Exception "+description);
        }
    }


/**
Verify that clearParameters can be called before executeBatch when the row
is full of parameters.  This just has the effect of cancelling those sets
which are never added to the batch anyway.

setint(1)
setint(2)
addbatch()
setInt(1)
setInt(2)
clearParameters()
executebatch() <-- works - the set parameter is cleared
**/
    public void Var005() {
	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
	    getDriverFixLevel() <= 704) {
	    System.out.println("New Testcase not working in JTOpen 7.4");
	    assertCondition(true); 
	    return; 
	}

	String description = "clearParameters after addbatch, setInt.. clear should have no effect.  Does not work in JTOpen 7.4"; 
        try {
            newSetup("");

            // Build a batch with two value in it.
            PreparedStatement ps = buildBatch(2, 0);
            ps.setInt(1, 1000);
            ps.setInt(2, 1000);
            ps.setInt(3, 1000);
            ps.clearParameters();  // Note: clearParameters undoes all
                                   // the last parms but the batch is 
                                   // left alone.

            int[] counts = ps.executeBatch();

            // Verify that the rows are in the table.
            assertCondition(verifyRows() == 3, "verifyRows != 3 counts.length="+counts.length+description);

        } catch (Exception e) {
            failed (e, "Unexpected Exception "+description);
        }
    }



/**
Verify that additional sets can be done but not added to the batch without 
effecting the batch's ability to be executed.  The additional sets are just
thrown away.

setint(1)
setint(2)
addbatch()
setInt(1)
executebatch() <-- works - the extra parameter is ignored 
               because it was never added to the batch
**/
    public void Var006() {
        try {
            newSetup("");

            // Build a batch with two value in it.
            PreparedStatement ps = buildBatch(2, 0);
            ps.setInt(1, 1000);
            ps.setInt(2, 1000);
            ps.setInt(3, 1000);

            int[] counts = ps.executeBatch();

            // Verify that the rows are in the table.
            assertCondition(verifyRows() == 3, "verifyRows != 3 counts.length="+counts.length);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
Verify that the data type of all sets are checked.  In the past, we did
not catch this type of error.

setint(1)
setint(2)
addbatch()
setTimestamp(1)  <-- fails - parameter types are varified
**/
    public void Var007() {
        try {
            newSetup("");

            // Build a batch with one value in it.
            PreparedStatement ps = buildBatch(1, 0);

            try {
                ps.setTimestamp(1, new Timestamp (9789874));
                failed("Didn't throw SQLException");
            } catch (Exception expected) {
                 assertExceptionIsInstanceOf (expected, "java.sql.SQLException");
            } 
            
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
Verify that the index of all sets are checked (value too low).  In the past, we did
not catch this type of error.

setint(1)
setint(2)
addbatch()
setInt(-1)  <-- fails - bad parameter index
**/
    public void Var008() {
        try {
            newSetup("");

            // Build a batch with one value in it.
            PreparedStatement ps = buildBatch(1, 0);

            try {
                ps.setInt(0, 10000);
                failed("Didn't throw SQLException");
            } catch (Exception expected) {
                 assertExceptionIsInstanceOf (expected, "java.sql.SQLException");
            } 
            
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
Verify that the index of all sets are checked (value too high).  In the past, we did
not catch this type of error.

setint(1)
setint(2)
addbatch()
setInt(100)  <-- fails - bad parameter index
**/
    public void Var009() {
        try {
            newSetup("");

            // Build a batch with one value in it.
            PreparedStatement ps = buildBatch(1, 0);

            try {
                ps.setInt(4, 10000);
                failed("Didn't throw SQLException");
            } catch (Exception expected) {
                 assertExceptionIsInstanceOf (expected, "java.sql.SQLException");
            } 
            
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
Verify that the closed flag is checked on all sets.  In the past, we did
not catch this type of error.

setint(1)
setint(2)
addbatch()
close()
setInt(1)  <-- fails - statement closed
**/
    public void Var010() {
        try {
            newSetup("");

            // Build a batch with one value in it.
            PreparedStatement ps = buildBatch(1, 0);
            ps.close();

            try {
                ps.setInt(1, 1000);
                failed("Didn't throw SQLException");
            } catch (Exception expected) {
                 assertExceptionIsInstanceOf (expected, "java.sql.SQLException");
            } 
            
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
Verify that an exception is thrown when you mix the executeUpdate 
method with the creation of a batch queue.

setint(1)
setint(2)
addbatch()
setint(1)
setint(2)
addbatch()
executeUpdate() <-- Works - The parameters have been bound and the 
                    values from the first entry in the batch array
                    are used.  Doing this execute method causes the
                    rest of the batch to be thrown away.
**/
    public void Var011() {
        try {
            newSetup("");

            // Build a batch with two rows in it.
            PreparedStatement ps = buildBatch(2, 0);
            
            // This will work.  The parameters have been bound
            // and the initial binds are valid.  In other words,
            // what you should expect to happen here is that the 
            // values that were set for the first row in the 
            // batch are what will get inserted into the database.
            // Running the execute methods also 'destroys' the 
            // existing batch.
            ps.executeUpdate();

            // assertCondition that there are 2 rows in the database - one 
            // is the initial one from the newSetup and the other
            // is what was the first row of the batch that never
            // got executed.
            assertCondition(verifyRows() == 2);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
Verify that calling executeBatch with nothing in the batch works.  Even 
though the parameters are set, no values should get put to the database 
bacause the batch is empty.

setint(1)
setint(2)
executeBatch()  <-- works.  0 rows inserted into the database.
**/
    public void Var012() {
        try {
            newSetup("");

            // Build a batch with two value in it.
            PreparedStatement ps = buildBatch(0, 0);
            ps.setInt(1, 1000);
            ps.setInt(2, 1000);
            ps.setInt(3, 1000);

            int[] counts = ps.executeBatch();

            // Verify that the rows are in the table.
            assertCondition((verifyRows() == 1) && (counts.length == 0));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
Verify that you do not have to bind all the LOB parameters after the first 
row has been handled.  This is to ensure that we correctly handle the case 
where we have set a lob in one row, but don't set it for other rows.  Make 
sure that the previous row values show up in the database correctly.

It works like this:  When you are using the execute methods of the Prepared
Statement class, you must set all the parameters before the statement can 
be executed, but after that, any parameters that you do not change maintain
the same value.  The same applies to parameters set for batch processing.
Once all the values are set, you only need to set the parameters that you 
wish to change for each subsequent set.

Note:  JDBC for UDB on NT does not work this way.  They require you to set 
all the parameters for each run.  The SQLException will be thrown when you
attempt to do the executeBatch() request (not at the time that the addBatch()
request happens).

Scenario:
prepareStatement("stmt ?, ?")
setBlob(1)
setBlob(2)
addbatch()
setBlob(1)
addbatch() <-- works - all the parameters have been set.
**/
    public void Var013() {
	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
	    getDriverFixLevel() <= 704) {
	    System.out.println("New Testcase not working in JTOpen 7.4");
	    assertCondition(true); 
	    return; 
	}

        try {
	    StringBuffer sb = new StringBuffer();
	    sb.append("\n"); 
            // int [] expected = new int[] { 1, 1, 1, 1};

	    // Creates row 0 
            newSetupLobs("");

            // Build a batch with one value in it.
	    // Creates row 1 
            PreparedStatement ps = buildBatchLobs(1);

	    // Create Row 2 
            ps.setInt(1, 2);
            ps.addBatch();

            byte byteValue = 3;
            byte[] blobBytes3 = new byte[] { byteValue, byteValue, byteValue, byteValue, byteValue};

	    // Create Row 3 -- 3,{3,3,3,3,3},{3,3,3,3,3} 
            ps.setInt(1, 3);
            ps.setBytes(2, blobBytes3);
            ps.setBytes(3, blobBytes3);
            ps.addBatch();


	    // Make another copy.  If we don't make a copy, then the bytes will
            // overwritten and different values will be written into the database

            byte[] blobBytes4 = new byte[] { byteValue, byteValue, byteValue, byteValue, byteValue};

	    // Create Row 4 
            blobBytes4[0] = 4;
            ps.setInt(1, 4);
            ps.setBytes(3, blobBytes4);
            ps.addBatch();

            int[] counts = ps.executeBatch();
            connection_.commit();

            // Verify that all the counts are 1
            boolean rowCountsOK = true;
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] != 1) 
                    rowCountsOK = false;
            }


            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM " + JDBUTest.BUTESTLOB + " ORDER BY COL1 ");
            boolean dataOK = true;

            // Test row 1.  0, 0<blob>, 0<blob>
            rs.next();
	    if (rs.getInt(1) != 0) {
		sb.append("Row 1 getInt(1) = "+rs.getInt(1)+" sb 0\n"); 
                dataOK = false;
	    }

            byte[] b = null;
            b = rs.getBytes(2);
	    if (b[0] != 0) {
		sb.append("Row 1 getBytes(2) = "+b[0]+" sb 0\n"); 
                dataOK = false;
	    }

            b = rs.getBytes(3);
	    if (b[0] != 0) {
		sb.append("Row 1 getBytes(3) = "+b[0]+" sb 0\n"); 
                dataOK = false;
	    }
            // Test row 2.  1, 1<blob>, 1<blob>
            rs.next();
	    if (rs.getInt(1) != 1) {
		sb.append("Row 2 getInt(1) = "+rs.getInt(1)+" sb 1\n"); 
                dataOK = false;
	    }
            b = rs.getBytes(2);
	    if (b[0] != 1)  {
		sb.append("Row 2 getBytes(2) = "+b[0]+" sb 1\n"); 
                dataOK = false;
	    }

            b = rs.getBytes(3);
	    if (b[0] != 1) {
		sb.append("Row 2 getBytes(3) = "+b[0]+" sb 1\n"); 
                dataOK = false;
	    }

            // Test row 3.  2, 1<blob>, 1<blob>
            // Note:  only the int was set for this row.
            rs.next();
	    if (rs.getInt(1) != 2) {
		sb.append("Row 3 getInt(1) = "+rs.getInt(1)+" sb 2\n"); 
                dataOK = false;
	    }

            b = rs.getBytes(2);
	    if (b[0] != 1)  {
		sb.append("Row 3 getBytes(2) = "+b[0]+" sb 1\n");
                dataOK = false;
	    }

            b = rs.getBytes(3);
	    if (b[0] != 1)  {
		sb.append("Row 3 getBytes(3) = "+b[0]+" sb 1\n");
                dataOK = false;
	    }
            // Test row 4.  3, 3<blob>, 3<blob>
            // Note:  All the rows were again set.
            rs.next();
	    if (rs.getInt(1) != 3) {
		sb.append("Row 4 getInt(1) = "+rs.getInt(1)+" sb 3\n"); 
		dataOK = false;
	    }

            b = rs.getBytes(2);
	    if (b[0] != 3)  {
		sb.append("Row 4 getBytes(2) = "+b[0]+" sb 3\n");
                dataOK = false;
	    }

            b = rs.getBytes(3);
	    if (b[0] != 3)  {
		sb.append("Row 4 getBytes(3) = "+b[0]+" sb 3\n");
		dataOK = false;
	    }

            // Test row 5.  4, 3<blob>, 4<blob>
            // Note:  Only the int and the second lob was set.
            rs.next();
	    if (rs.getInt(1) != 4)  {
		sb.append("Row 5 getInt(1) = "+rs.getInt(1)+" sb 5\n"); 

		dataOK = false;
	    }
            b = rs.getBytes(2);
	    if (b[0] != 3)  {
		sb.append("Row 5 getBytes(2) = "+b[0]+" sb 3\n");
                dataOK = false;
	    }

            b = rs.getBytes(3);
	    if (b[0] != 4)  {
		sb.append("Row 5 getBytes(3) = "+b[0]+" sb 4\n");
                dataOK = false;
	    }

	    sb.append("rowCountsOK = " + rowCountsOK+"\n");
	    sb.append("dataOK = " + dataOK+"\n");

            assertCondition(rowCountsOK && dataOK,sb);


        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }

}



