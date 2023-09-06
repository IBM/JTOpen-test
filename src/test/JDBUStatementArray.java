///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDBUStatementArray.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDBUStatementArray.java
//
// Classes:      JDBUStatementArray
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.BatchUpdateException;
import java.util.Hashtable;



/**
Testcase JDBUStatementArray.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDBUStatementArray
extends JDTestcase {



    // Private data.
    private Connection          connection_;
    // private Statement           statement_;
    // private String              properties_;
    private String              url; 
    private StringBuffer        sb = new StringBuffer(); 
/**
Constructor.
**/
    public JDBUStatementArray (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
    {
        super (systemObject, "JDBUStatementArray",
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


	    JDBUTest.BUTESTDATA = "BUSATSTDTA";
	    JDBUTest.BUTEST     = "BUSATEST";
	    JDBUTest.BUTESTLOB  = "BUSATSTLOB"; 
	    /* 
            // Register the JDBC driver.
            Class.forName("com.ibm.db2.jdbc.app.DB2Driver");

            // Get a global connection - choose how you which
            // to get the connection.
            connection_ = DriverManager.getConnection("jdbc:db2:*local");
            */
	    url = baseURL_;
	    connection_ = testDriver_.getConnection (url, systemObject_.getUserId(), encryptedPassword_);

            Statement s = connection_.createStatement();

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
	    connection_.commit();

        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());

        }
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
	sb.setLength(0); 
        try {
            try {
                if (connection_ != null)
                    connection_.close();
            } catch (SQLException e) {
                System.out.println("Critical Error - couldn't close connection");
            }

	    String nowUrl = url+";"+connectionParms;
	    connection_ = testDriver_.getConnection (nowUrl);
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



    public Statement buildBatch(int size,
                                int failurePos) 
    throws SQLException
    {
        Statement ps = 
        connection_.createStatement();
        
        if (failurePos < 1) {
            for (int i = 1; i <= size; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + i + ", " + i + ", " + i + ")");
            }
        } else {

            for (int i = 1; i < failurePos; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + i + ", " + i + ", " + i + ")");
            }

            // Insert the failure entry
            ps.addBatch("insert into " + JDBUTest.BUTEST + " values(0, 0, 0)");

            for (int i = failurePos + 1; i <= size; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + i + ", " + i + ", " + i + ")");
            }


        }

        return ps;
    }



    protected boolean verifyCounts(int [] actual,
                                   int [] expected) 
    {
        if (actual == null) {
            sb.append("actual array was null.\n");
            return false;
        }

        if (expected.length != actual.length) {
            sb.append("expected and actual lengths don't match.  actual.length is " + actual.length+"\n");
            return false;
        }

        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
		sb.append("arrays don't match at index " + i + " expected[i] is " + expected[i] + " and actual[i] is " + actual[i]+"\n");
                return false;
            }
        }
        return true;
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



    protected int verifyRowsLobs() 
    throws SQLException 
    {
        // boolean success = true;
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDBUTest.BUTESTLOB);

        int count = 0;
        while (rs.next()) {

            int index = rs.getInt(1);
            if (index != count) {
                System.out.println("Index value was bad - failed verify");
                // success = false;
                break;
            } else {
                System.out.println("Index value is right.");
            }

            byte[] blobValue = rs.getBytes(2);
            int compareValue = blobValue[0];
            if (compareValue != index) {
                System.out.println("Blob value was incorrect - failed verify");
                // success = false;
                break;
            } else {
                System.out.println("Blob value is right.");
            }

            count++;
        }

        s.close();
        return count;
    }



/**
1) Statement batch (able to use blocking) 
     - inserts 20 rows 
     - no connection properties specified
     - expected to work with 20 rows of 1s returned
**/
    public void Var001() {
        int arraySize = 20;
        int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};
	    
        try {
	    
            newSetup("");

            Statement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1), "success1 = "+ success1 + " AND SHOULD BE true, rowCount = " +rowCount+ " AND SHOULD = (expected.length + 1) which equals " + (expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
2) Statement batch (able to use blocking) 
    - inserts 20 rows 
    - no connection properties specified
    - fails on first entry
    - exception object with update count length 0 returned
**/
    public void Var002() {
        int arraySize = 20;
        int failurePos = 1;
        int [] expected = new int[] {};

        try {
            newSetup("");

            Statement ps = buildBatch(arraySize, failurePos);

            ps.executeBatch();

            failed("Didn't throw a BatchUpdateException");

        } catch (BatchUpdateException bue) {
            int [] counts = bue.getUpdateCounts();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;

            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            assertCondition(success1 && (rowCount == expected.length + 1), " success1 = " + success1 + " AND SHOULD BE true, rowCount = " + rowCount + " AND SHOULD equal (expected.length + 1) = " + (expected.length +1) );

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
3) Statement batch (able to use blocking) 
    - inserts 20 rows 
    - no connection properties specified
    - fails on 10th entry
    - exception object with update count length 9 returned (all values are 1)
**/
    public void Var003() {
        int arraySize = 20;
        int failurePos = 10;
        int [] expected = new int[] {1, 1, 1, 1, 1,
            1, 1, 1, 1};

        try {
            newSetup("");

            Statement ps = buildBatch(arraySize, failurePos);

            ps.executeBatch();

            failed("Didn't throw a BatchUpdateException");

        } catch (BatchUpdateException bue) {
            int [] counts = bue.getUpdateCounts();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;

            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            assertCondition(success1 && (rowCount == expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
4) Statement batch (able to use blocking) 
    - inserts 20 rows 
    - no connection properties specified
    - fails on 20th entry
    - exception object with update count length 19 returned (all values are 1)
**/
    public void Var004() {
        int arraySize = 20;
        int failurePos = 20;
        int [] expected = new int[] {1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 
            1, 1, 1, 1};

        try {
            newSetup("");

            Statement ps = buildBatch(arraySize, failurePos);

            ps.executeBatch();

            failed("Didn't throw a BatchUpdateException");

        } catch (BatchUpdateException bue) {
            int [] counts = bue.getUpdateCounts();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;

            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            assertCondition(success1 && (rowCount == expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
5) Statement batch (able to use blocking) 
     - inserts 20 rows 
    - connection property jdbc 2.0 batch support specified (this is the default)
     - expected to work with 20 rows of 1s returned
**/
    public void Var005() {
        int arraySize = 20;
        int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("batch style=2.0");

            Statement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1), "success1 = "+ success1 + " AND SHOULD BE true, rowCount = " +rowCount+ " AND SHOULD = (expected.length + 1) which equals " + (expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
6) Statement batch (able to use blocking) 
    - inserts 20 rows 
    - connection property jdbc 2.0 batch support specified (this is the default)
    - fails on first entry
    - exception object with update count length 0 returned
**/
    public void Var006() {
        int arraySize = 20;
        int failurePos = 1;
        int [] expected = new int[] {};

        try {
            newSetup("batch style=2.0");

            Statement ps = buildBatch(arraySize, failurePos);

            ps.executeBatch();

            failed("Didn't throw a BatchUpdateException");

        } catch (BatchUpdateException bue) {
            int [] counts = bue.getUpdateCounts();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;

            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            assertCondition(success1 && (rowCount == expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
7) Statement batch (able to use blocking) 
    - inserts 20 rows 
    - connection property jdbc 2.0 batch support specified (this is the default)
    - fails on 10th entry
    - exception object with update count length 9 returned (all values are 1)
**/
    public void Var007() {
        int arraySize = 20;
        int failurePos = 10;
        int [] expected = new int[] {1, 1, 1, 1, 1,
            1, 1, 1, 1};

        try {
            newSetup("batch style=2.0");

            Statement ps = buildBatch(arraySize, failurePos);

            ps.executeBatch();

            failed("Didn't throw a BatchUpdateException");

        } catch (BatchUpdateException bue) {
            int [] counts = bue.getUpdateCounts();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;

            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            assertCondition(success1 && (rowCount == expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
8) Statement batch (able to use blocking) 
    - inserts 20 rows 
    - connection property jdbc 2.0 batch support specified (this is the default)
    - fails on 20th entry
    - exception object with update count length 19 returned (all values are 1)
**/
    public void Var008() {
        int arraySize = 20;
        int failurePos = 20;
        int [] expected = new int[] {1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 
            1, 1, 1, 1};

        try {
            newSetup("batch style=2.0");

            Statement ps = buildBatch(arraySize, failurePos);

            ps.executeBatch();

            failed("Didn't throw a BatchUpdateException");

        } catch (BatchUpdateException bue) {
            int [] counts = bue.getUpdateCounts();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;

            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            assertCondition(success1 && (rowCount == expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
9) Statement batch (able to use blocking) 
     - inserts 20 rows 
     - connection property jdbc 2.1 batch support specified (not the default)
     - expected to work with 20 rows of 1s returned
**/
    public void Var009() {
        int arraySize = 20;
        int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("batch style=2.1");

            Statement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1), "success1 = "+ success1 + " AND SHOULD BE true, rowCount = " +rowCount+ " AND SHOULD = (expected.length + 1) which equals " + (expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
10) Statement batch (able to use blocking) 
    - inserts 20 rows 
     - connection property jdbc 2.1 batch support specified (not the default)
    - fails on first entry
    - exception object with update count length 0 returned
**/
    public void Var010() {
        int arraySize = 20;
        int failurePos = 1;
        int [] expected = new int[] { -3, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};
	int expectedRowCount =	0; 

        try {

	    expectedRowCount  = expected.length - 1 + 1; 
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		// 
		// Toolbox updates to the failure position then stops
		//
		expected = new int[0];
		expectedRowCount = 1; 
	    } 
            newSetup("batch style=2.1");

            Statement ps = buildBatch(arraySize, failurePos);

            ps.executeBatch();

            failed("Didn't throw a BatchUpdateException");

        } catch (BatchUpdateException bue) {
            int [] counts = bue.getUpdateCounts();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;

            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            assertCondition(success1 && (rowCount == expectedRowCount),
			    "success1="+success1+" rowCount="+rowCount+" "+sb.toString());

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
11) Statement batch (able to use blocking) 
    - inserts 20 rows 
     - connection property jdbc 2.1 batch support specified (not the default)
    - fails on 10th entry
    - exception object with update count length 9 returned (all values are 1)
**/
    public void Var011() {
        int arraySize = 20;
        int failurePos = 10;
        int [] expected = new int[] {  1, 1, 1, 1, 1,
            1, 1, 1, 1, -3,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        int [] expectedToolbox = new int[] {  1, 1, 1, 1, 1,
            1, 1, 1, 1};


        int expectedRowCount = expected.length - 1 + 1; 
        try {

	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		// 
		// Toolbox updates to the failure position then stops
		// 
		expected = expectedToolbox;
		expectedRowCount = 10; 
	    } 

            newSetup("batch style=2.1");

            Statement ps = buildBatch(arraySize, failurePos);

            ps.executeBatch();

            failed("Didn't throw a BatchUpdateException");

        } catch (BatchUpdateException bue) {
            int [] counts = bue.getUpdateCounts();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;

            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            assertCondition(success1 && (rowCount == expectedRowCount), "success1="+success1+" rowCount="+rowCount+" "+sb.toString());

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
12) Statement batch (able to use blocking) 
    - inserts 20 rows 
     - connection property jdbc 2.1 batch support specified (not the default)
    - fails on 20th entry
    - exception object with update count length 19 returned (all values are 1)
**/
    public void Var012() {
        int arraySize = 20;
        int failurePos = 20;
        int [] expected = new int[] {1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, -3};

	int [] expectedToolbox = new int[] {1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 
            1, 1, 1, 1 };

	int expectedRowCount = 0; 
        try {

	    expectedRowCount = expected.length - 1 + 1 ; 
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		// 
		// Toolbox updates to the failure position then stops
		// 
		expected = expectedToolbox;
		expectedRowCount = 20; 
	    } 

	    
            newSetup("batch style=2.1");

            Statement ps = buildBatch(arraySize, failurePos);

            ps.executeBatch();

            failed("Didn't throw a BatchUpdateException");

        } catch (BatchUpdateException bue) {
            int [] counts = bue.getUpdateCounts();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;

            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            assertCondition(success1 && (rowCount == expectedRowCount), "success1="+success1+" rowCount="+rowCount+" "+sb.toString());

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
13) Statement batch (able to use blocking) 
     - inserts 1 row (special case situation)
     - no connection properties specified
     - expected to work with 1 row with 1 returned
**/
    public void Var013() {
        int arraySize = 1;
        int failurePos = 0;
        int [] expected = new int[] { 1};

        try {
            newSetup("");

            Statement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1), "success1 = "+ success1 + " AND SHOULD BE true, rowCount = " +rowCount+ " AND SHOULD = (expected.length + 1) which equals " + (expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
14) Statement batch (able to use blocking) 
     - inserts 1000 rows (special case situation - rebind needed)
     - no connection properties specified
     - expected to work with 1000 rows with 1 returned
**/
    public void Var014() {
        int arraySize = 1000;
        int failurePos = 0;
        int [] expected = new int[1000];
        for (int i = 0; i < 1000; i++) {
            expected[i] = 1;
        }

        try {
            newSetup("");

            Statement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1), "success1 = "+ success1 + " AND SHOULD BE true, rowCount = " +rowCount+ " AND SHOULD = (expected.length + 1) which equals " + (expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
15) Statement batch (able to use blocking) 
    - no connection properties specified
     - execute an update
     - inserts 20 rows 
     - execute an update
     - expected to work
**/
    public void Var015() {
        int arraySize = 20;
        // int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("");

            // Execute a statement
            Statement ps = 
            connection_.createStatement();

            boolean success1 = (ps.executeUpdate("insert into " + JDBUTest.BUTEST + " values(1000, 1000, 1000)") == 1);


            // Build and execute a batch
            for (int i = 1; i <= arraySize; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + i + ", " + i + ", " + i + ")");
            }

            int[] counts = ps.executeBatch();
            boolean success2 = verifyCounts(counts, expected);

            boolean success3 = (ps.executeUpdate("insert into " + JDBUTest.BUTEST + " values(2000, 2000, 2000)") == 1);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && success2 && success3 && (rowCount == expected.length + 3), "success1 = " +success1+ " AND SHOULD BE true, success2 = " +success2+ " AND SHOULD BE true, success3 = " +success3+ " AND SHOULD BE true, rowCount = " +rowCount+ " AND SHOULD BE (expected.length + 3) = "+ (expected.length + 3));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
16) Statement batch (able to use blocking) 
    - no connection properties specified
    - inserts 20 rows 
     - execute an update
    - inserts 20 rows 
     - expected to work
**/
    public void Var016() {
        int arraySize = 20;
        // int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("");

            // Execute a statement
            Statement ps = 
            connection_.createStatement();

            // Build and execute a batch
            for (int i = 1; i <= 20; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + i + ", " + i + ", " + i + ")");
            }

            int[] counts = ps.executeBatch();
            boolean success1 = verifyCounts(counts, expected);

            // Execute a statement
            boolean success2 = (ps.executeUpdate("insert into " + JDBUTest.BUTEST + " values(1000, 1000, 1000)") == 1);


            // Build and execute another batch
            for (int i = 1; i <= arraySize; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + (i + 100) + ", " + (i + 100) + ", " + (i + 100) + ")");
            }

            counts = ps.executeBatch();
            boolean success3 = verifyCounts(counts, expected);


            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && success2 && success3 && (rowCount == (expected.length * 2) + 2), "success1 = " +success1+ " AND SHOULD BE true, success2 = " +success2+ " AND SHOULD BE true, success3 = " +success3+ " AND SHOULD BE true, rowCount = " +rowCount+ " AND SHOULD BE ((expected.length * 2) + 2) = "+ ((expected.length * 2) + 2));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
17) Statement batch (able to use blocking) 
    - no connection properties specified
     - execute small batch
     - execute big   batch
     - execute small batch
     - expected to work
**/
    public void Var017() {
        int [] expectedSmall = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        int [] expectedBig = new int[1000];
        for (int i = 0; i < 1000; i++) {
            expectedBig[i] = 1;
        }

        try {
            newSetup("");

            // Execute a statement
            Statement ps = 
            connection_.createStatement();

            // Build and execute a small batch
            for (int i = 1; i <= 20; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + i + ", " + i + ", " + i + ")");
            }

            int[] counts = ps.executeBatch();
            boolean success1 = verifyCounts(counts, expectedSmall);


            // Build and execute a big batch
            for (int i = 1; i <= 1000; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + (i + 100) + ", " + (i + 100) + ", " + (i + 100) + ")");
            }

            counts = ps.executeBatch();
            boolean success2 = verifyCounts(counts, expectedBig);


            // Build and execute another small batch
            for (int i = 1; i <= 20; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + (i + 10000) + ", " + (i + 10000) + ", " + (i + 10000) + ")");
            }

            counts = ps.executeBatch();
            boolean success3 = verifyCounts(counts, expectedSmall);


            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && success2 && success3 && (rowCount == 1041), "success1 = " +success1+ " AND SHOULD BE true, success2 = " +success2+ " AND SHOULD BE true, success3 = " +success3+ " AND SHOULD BE true, rowCount = " +rowCount+ " AND SHOULD BE 1041");

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
18) Statement batch (able to use blocking) 
    - no connection properties specified
     - execute big   batch
     - execute small batch
     - execute big   batch
     - expected to work
**/
    public void Var018() {
        int [] expectedSmall = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        int [] expectedBig = new int[1000];
        for (int i = 0; i < 1000; i++) {
            expectedBig[i] = 1;
        }

        try {
            newSetup("");

            // Execute a statement
            Statement ps = 
            connection_.createStatement();

            // Build and execute a big batch
            for (int i = 1; i <= 1000; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + i + ", " + i + ", " + i + ")");
            }

            int[] counts = ps.executeBatch();
            boolean success1 = verifyCounts(counts, expectedBig);


            // Build and execute a small batch
            for (int i = 1; i <= 20; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + (i + 1001) + ", " + (i + 1001) + ", " + (i + 1001) + ")");
            }

            counts = ps.executeBatch();
            boolean success2 = verifyCounts(counts, expectedSmall);



            // Build and execute another big batch
            for (int i = 1; i <= 1000; i++) {
                ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + (i + 10000) + ", " + (i + 10000) + ", " + (i + 10000) + ")");
            }

            counts = ps.executeBatch();
            boolean success3 = verifyCounts(counts, expectedBig);


            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && success2 && success3 && (rowCount == 2021), "success1 = " +success1+ " AND SHOULD BE true, success2 = " +success2+ " AND SHOULD BE true, success3 = " +success3+ " AND SHOULD BE true, rowCount = " +rowCount+ " AND SHOULD BE 2021");

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
19) Statement batch (able to use blocking) 
    - no connection properties specified
     - execute small batch
     - execute big   batch
     - execute small batch
     - expected to work
**/
    public void Var019() {
        int [] expectedBig = new int[1000];
        for (int i = 0; i < 1000; i++) {
            expectedBig[i] = 1;
        }

        try {
            newSetup("");

            // Execute a statement
            Statement ps = 
            connection_.createStatement();

            // Build and execute a batch with null values.
            for (int i = 1; i <= 1000; i++) {
                if ((i % 2) == 0) {
                    ps.addBatch("insert into " + JDBUTest.BUTEST + " (COL1) values(" + i + ")");
                } else {
                    ps.addBatch("insert into " + JDBUTest.BUTEST + " values(" + i + ", " + i + ", " + i + ")");
                }
            }

            int[] counts = ps.executeBatch();
            boolean success1 = verifyCounts(counts, expectedBig);


            ps.close();


            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM " + JDBUTest.BUTEST);
            boolean success2 = true;

            // handle the first row...
            rs.next();
            if ((rs.getInt(1) != 0 ) ||(rs.getInt(2) != 0 )||(rs.getInt(3) != 0 )) {
                System.out.println("Failed on first row.");
                success2 = false;
            }

            int count = 1;

            if (success2) {
                while (rs.next()) {
                    if ((count % 2) == 0) {
                        int first  = rs.getInt(1);
                        int second = rs.getInt(2);
                        boolean secondNull = rs.wasNull();
                        int third  = rs.getInt(3);
                        boolean thirdNull = rs.wasNull();
                        
                        if ((rs.getInt(1) != count ) || (secondNull != true)||(thirdNull != true )) {
                            System.out.println("Failed on row " + count+"first="+first+" second="+second+" third="+third);
                            success2 = false;
                            break;
                        }
                    } else {
                        if ((rs.getInt(1) != count ) ||(rs.getInt(2) != count )||(rs.getInt(3) != count )) {
                            System.out.println("Failed on row " + count);
                            success2 = false;
                            break;
                        }
                    }

                    count++;

                }
            }


            assertCondition(success1 && success2 && (count == 1001), "success1 = " +success1+ " AND SHOULD BE true, success2 = " +success2+ " AND SHOULD BE true, count = " +count+ " AND SHOULD BE 1001");

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
20) Statement batch (able to use blocking) 
    - no connection properties specified
     - execute an empty batch 
     - expected to work
**/
    public void Var020() {
        int [] expected = new int[] { };

        try {
            newSetup("");

            // Execute a statement
            Statement ps = 
            connection_.createStatement();

            // NOTE:  Not added to the batch here.
            int[] counts = ps.executeBatch();
            boolean success1 = verifyCounts(counts, expected);

            ps.close();

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1), "success1 = " + success1 + " AND SHOULD BE true, rowCount = " + rowCount+ " AND SHOULD equal (expected.length + 1) = " +(expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }

}
