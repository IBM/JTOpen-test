///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDBUPSArray.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDBUPSArray.java
//
// Classes:      JDBUPSArray
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.BatchUpdateException;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDBUPSArray.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDBUPSArray
extends JDTestcase {



    // Private data.
    private Connection          connection_;

    private StringBuffer sb = new StringBuffer(); 
/**
Constructor.
**/
    public JDBUPSArray (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
    {
        super (systemObject, "JDBUPSArray",
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
            // Register the JDBC driver.
            Class.forName("com.ibm.db2.jdbc.app.DB2Driver");

            // Get a global connection - choose how you which
            // to get the connection.
            connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_); 

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
            System.out.println("Caught exception: "); 
            e.printStackTrace();

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
		String expectedException = "not found"; 
		String exInfo = e.toString();
		if (exInfo.indexOf(expectedException) < 0) {
		    System.out.println("Unexpected exception  -- expected "+expectedException); 
		    e.printStackTrace();
		}
            }

            s.executeUpdate("create table " + JDBUTest.BUTEST + 
                            " (col1 int primary key, col2 int, col3 int)");

            s.executeUpdate("insert into " + JDBUTest.BUTEST + 
                            " values(0, 0, 0)");

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
This is just like the netSetupLobs code except that the table has no primary
key because we want to insert *only* lobs into the database.
**/
    public void newSetupLobs2(String connectionParms) 
    {
        Statement s = null;

        try {
            try {
		if (connection_ != null){
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
                            " (col2 BLOB(200), col3 BLOB(200))");

            // NOTE:  It is important here for verification that the first byte is 0.
            byte[] blobBytes = new byte[] { (byte) 0, (byte) -12, (byte) 45, (byte) -33, (byte) 1};

            
            PreparedStatement ps = connection_.prepareStatement("insert into " + JDBUTest.BUTESTLOB + " values(?, ?)");
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


    public PreparedStatement buildBatch(int size,
                                        int failurePos) 
    throws SQLException
    {
        PreparedStatement ps = 
        connection_.prepareStatement("insert into " + JDBUTest.BUTEST + " values(?, ?, ?)");

       sb.append("Creating a batch .... "+"\n");

        if (failurePos < 1) {
            for (int i = 1; i <= size; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i);
                }
                sb.append("Row:  " + i + ", " + i + ", " + i+"\n");
                ps.addBatch();
            }
        } else {

            for (int i = 1; i < failurePos; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i);
                }
                sb.append("Row:  " + i + ", " + i + ", " + i+"\n");
                ps.addBatch();
            }

            // Insert the failure entry
            for (int j = 1; j <= 3; j++) {
                ps.setInt(j, 0);
            }
            sb.append("Row:  " + 0 + ", " + 0 + ", " + 0+"\n");
            ps.addBatch();

            for (int i = failurePos + 1; i <= size; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i);
                }
		sb.append("Row:  " + i + ", " + i + ", " + i+"\n");
                ps.addBatch();
            }


        }

        return ps;
    }



/**
Do the same thing we did for the common case batch above, but make the table 
be full of LOB columns so that we can see that we handle locators right. 

@exception SQLException If a SQL exception occurs.
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
	    sb.append("Building batch input value is " + byteValue+"\n");
            ps.addBatch();
        }

        return ps;
    }



/**
Do the same thing we did for the common case batch above, but make the table 
be full of LOB columns so that we can see that we handle locators right. 

NOTE:  This is just like the buildBatchLobs method but the table contains 
       no primary key (we want to insert *ONLY* lobs in this case).

@exception SQLException If a SQL exception occurs.
**/
    public PreparedStatement buildBatchLobs2(int size) 
    throws SQLException
    {
        PreparedStatement ps = 
        connection_.prepareStatement("insert into " + JDBUTest.BUTESTLOB + " values(?, ?)");


        for (int i = 1; i <= size; i++) {

            byte byteValue = (byte) (i % 100);
            byte[] blobBytes = new byte[] { byteValue, byteValue, byteValue, byteValue, byteValue};

            ps.setBytes(1, blobBytes);
            ps.setBytes(2, blobBytes);
	    sb.append("Building batch input value is " + byteValue+"\n");
            ps.addBatch();
        }

        return ps;
    }


/**
The goal here is to build a batch that has update statements in it 
so that we can see that we can get numbers other than 1 though the 
update counts successfully.

@exception SQLException If a SQL exception occurs.
**/
    public PreparedStatement buildBatchUpdates(int size) 
    throws SQLException
    {
        PreparedStatement ps = 
        connection_.prepareStatement("UPDATE " + JDBUTest.BUTEST + " SET COL2 = COL2 + 1, COL3 = COL3 + 1 WHERE COL1 >= ?");

        for (int i = size; i > 0; i--) {
            ps.setInt(1, i);
            ps.addBatch();
        }

        return ps;
    }



/**
The goal here is to build a batch that has update statements in it 
so that we can see that we can get numbers other than 1 though the 
update counts successfully.

@exception SQLException If a SQL exception occurs.
**/
    public PreparedStatement buildBatchSubselect(int size) 
    throws SQLException
    {
        PreparedStatement ps = 
        connection_.prepareStatement("insert into " + JDBUTest.BUTEST + 
                                     " select COL1 + (100 * ?), COL1, COL1 from " + JDBUTest.BUTESTDATA + " where COL1 >= ?");

        for (int i = size; i > 0; i--) {
            ps.setInt(1, i);
            ps.setInt(2, i);
            ps.addBatch();
        }

        return ps;
    }




    protected boolean verifyCounts(int [] actual,
                                   int [] expected) 
    {
	boolean passed = true; 
        if (actual == null) {
            sb.append("actual array was null.\n");
            return false;
        }

        if (expected.length != actual.length) {
            sb.append("expected and actual lengths don't match.  actual.length is " + actual.length+
		      " expected length = "+expected.length+" \n");
            passed = false;
        }

        for (int i = 0; i < expected.length && i < actual.length; i++) {
            if (expected[i] != actual[i]) {
                sb.append("arrays don't match at index " + i + " expected[i] is " + expected[i] + " and actual[i] is " + actual[i]+"\n");
                passed = false;
            }
        }
        return passed;
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
        boolean success = true;
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDBUTest.BUTESTLOB);

        int count = 0;
        while (rs.next()) {

            int index = rs.getInt(1);
            if (index != count) {
		sb.append("Index value was bad - failed verify\n");
                success = false;
                break;
            } else {
		sb.append("Index value is right.\n");
            }

            byte[] blobValue = rs.getBytes(2);
            int compareValue = blobValue[0];
            if (compareValue != index) {
                sb.append("Blob value was incorrect - failed verify\n");
                sb.append("  actual value: " + compareValue + " and expected was " + index+"\n");
                success = false;
                break;
            } else {
                sb.append("Blob value is right. success="+success+"\n");
            }

            count++;
        }

        s.close();
        return count;
    }


/**
This is just like the verifyRowsLobs method but the table is expected to 
have no primary key (Only lobs were inserted into it).

@exception SQLException If a SQL exception occurs.
**/
    protected int verifyRowsLobs2() 
    throws SQLException 
    {
        boolean success = true;
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDBUTest.BUTESTLOB);

        int count = 0;
        while (rs.next()) {

            byte[] blobValue = rs.getBytes(1);
            int compareValue = blobValue[0];
            if (compareValue != count) {
                sb.append("Blob value was incorrect - failed verify]n");
		sb.append("  actual value: " + compareValue + " and expected was " + count+"\n");
                success = false;
                break;
            } else {
                sb.append("Blob value is right. "+success+"\n");
            }

            count++;
        }

        s.close();
        return count;
    }



/**
1) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows 
     - no connection properties specified
     - expected to work with 20 rows of 1s returned
**/
    public void Var001() {
        sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
2) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows 
    - no connection properties specified
    - fails on first entry
    - exception object with update count length 0 returned
**/
    public void Var002() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 1;
        int [] expected = new int[] {};

        try {
            newSetup("");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
3) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows 
    - no connection properties specified
    - fails on 10th entry
    - exception object with update count length 9 returned (all values are 1)
**/
    public void Var003() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 10;
        int [] expected = new int[] {1, 1, 1, 1, 1,1, 1, 1, 1};
        int [] expectedToolbox = new int[0]; 

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then nothing is entered
	    expected=expectedToolbox; 
	} 

	sb.setLength(0);     
        try {
            newSetup("");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expected.length + 1),
			    "verifyCountsSucces="+success1+" "+
			    "rowCount = "+rowCount+" sb "+(expected.length + 1)+" "+
			    sb.toString());

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
4) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows 
    - no connection properties specified
    - fails on 20th entry
    - exception object with update count length 19 returned (all values are 1)
**/
    public void Var004() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 20;
        int [] expected = new int[] {1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 
            1, 1, 1, 1};

        int [] expectedToolbox = new int[0]; 

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then nothing is entered
	    expected=expectedToolbox; 
	} 

        try {
            newSetup("");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
5) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows 
    - connection property jdbc 2.0 batch support specified (this is the default)
     - expected to work with 20 rows of 1s returned
**/
    public void Var005() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("batch style=2.0");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
6) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows 
    - connection property jdbc 2.0 batch support specified (this is the default)
    - fails on first entry
    - exception object with update count length 0 returned
**/
    public void Var006() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 1;
        int [] expected = new int[] {};

        try {
            newSetup("batch style=2.0");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
7) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows 
    - connection property jdbc 2.0 batch support specified (this is the default)
    - fails on 10th entry
    - exception object with update count length 9 returned (all values are 1)
**/
    public void Var007() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 10;
        int [] expected = new int[] {1, 1, 1, 1, 1,
            1, 1, 1, 1};

        int [] expectedToolbox = new int[0]; 

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then nothing is entered
	    expected=expectedToolbox; 
	} 

        try {
            newSetup("batch style=2.0");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
8) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows 
    - connection property jdbc 2.0 batch support specified (this is the default)
    - fails on 20th entry
    - exception object with update count length 19 returned (all values are 1)
**/
    public void Var008() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 20;
        int [] expected = new int[] {1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 
            1, 1, 1, 1};

        int [] expectedToolbox = new int[0]; 

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then nothing is entered
	    expected=expectedToolbox; 
	} 

        try {
            newSetup("batch style=2.0");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
9) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows 
     - connection property jdbc 2.1 batch support specified (not the default)
     - expected to work with 20 rows of 1s returned
**/
    public void Var009() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("batch style=2.1");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
10) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows 
     - connection property jdbc 2.1 batch support specified (not the default)
    - fails on first entry
    - exception object with update count length 0 returned
**/
    public void Var010() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 1;
        int [] expected = new int[] { -3, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

	int expectedLength = expected.length - 1 + 1; 
        int [] expectedToolbox = new int[0]; 

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then nothing is entered
	    expected=expectedToolbox;
	    expectedLength = 1; 
	} 

        try {
            newSetup("batch style=2.1");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expectedLength),"success="+success1+" rowCount="+rowCount+" sb "+expectedLength+" "+sb.toString());

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
11) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows 
     - connection property jdbc 2.1 batch support specified (not the default)
    - fails on 10th entry
    - exception object with update count length 9 returned (all values are 1)
**/
    public void Var011() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 10;
        int [] expected = new int[] {  1, 1, 1, 1, 1,
            1, 1, 1, 1, -3,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};
	    
	    int expectedLength=expected.length; 
        int [] expectedToolbox = new int[0]; 



	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then nothing is entered
	    expected=expectedToolbox;
	    expectedLength = 1; 
	} 

        try {
            newSetup("batch style=2.1");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expectedLength),"success="+success1+" rowCount="+rowCount+" sb "+expectedLength+" "+sb.toString());

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
12) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows 
     - connection property jdbc 2.1 batch support specified (not the default)
    - fails on 20th entry
    - exception object with update count length 19 returned (all values are 1)
**/
    public void Var012() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 20;
        int [] expected = new int[] {1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, -3};

	    int expectedLength = expected.length - 1 + 1;  
        int [] expectedToolbox = new int[0]; 

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then nothing is entered
	    expected=expectedToolbox;
	    expectedLength = 1; 
	} 

        try {
            newSetup("batch style=2.1");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expectedLength),"success="+success1+" rowCount="+rowCount+" sb "+expectedLength+" "+sb.toString());

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
13) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows 
     - connection property 'use block insert' specified (not the default)
     - expected to work with 1 row with 1 returned
     - verify that 20 rows were inserted.
**/
    public void Var013() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("use block insert=true");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
14) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows (first one causes failure)
     - connection property 'use block insert' specified (not the default)
     - expected to fail with 1 row with -3 returned
     - verify that 0 rows were inserted (because it was done in a secondary commit cycle)
**/
    public void Var014() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 1;
        int [] expected = new int[] {};

        try {
            newSetup("use block insert=true");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
15) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows (10th one causes failure)
     - connection property 'use block insert' specified (not the default)
     - expected to fail with 1 row with -3 returned
     - verify that 0 rows were inserted (because it was done in a secondary commit cycle)
**/
    public void Var015() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 10;
        int [] expected = new int[] {};

        try {
            newSetup("use block insert=true");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
16) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows (last one causes failure)
     - connection property 'use block insert' specified (not the default)
     - expected to fail with 1 row with -3 returned
     - verify that 0 rows were inserted (because it was done in a secondary commit cycle)
**/
    public void Var016() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 20;
        int [] expected = new int[] {};

        try {
            newSetup("use block insert=true");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
17) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows 
     - connection property 'use block insert' specified (not the default)
    - connection property jdbc 2.1 batch support specified (not the default)
     - expected to work with 1 row with 1 returned
     - verify that 20 rows were inserted.
**/
    public void Var017() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("use block insert=true;batch style=2.1");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
18) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows (first one causes failure)
     - connection property 'use block insert' specified (not the default)
    - connection property jdbc 2.1 batch support specified (not the default)
     - expected to fail with 1 row with -3 returned
     - verify that 0 rows were inserted (because it was done in a secondary commit cycle)
**/
    public void Var018() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 1;
        int [] expected = new int[] { -3, -3, -3, -3, -3,
            -3, -3, -3, -3, -3,
            -3, -3, -3, -3, -3,
            -3, -3, -3, -3, -3};

        int [] expectedToolbox = new int[0]; 

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then nothing is entered
	    expected=expectedToolbox; 
	} 

        try {
            newSetup("use block insert=true;batch style=2.1");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
19) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows (10th one causes failure)
     - connection property 'use block insert' specified (not the default)
    - connection property jdbc 2.1 batch support specified (not the default)
     - expected to fail with 1 row with -3 returned
     - verify that 0 rows were inserted (because it was done in a secondary commit cycle)
**/
    public void Var019() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 10;
        int [] expected = new int[] { -3, -3, -3, -3, -3,
            -3, -3, -3, -3, -3,
            -3, -3, -3, -3, -3,
            -3, -3, -3, -3, -3};

        int [] expectedToolbox = new int[0]; 

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then nothing is entered
	    expected=expectedToolbox; 
	} 

        try {
            newSetup("use block insert=true;batch style=2.1");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
20) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows (last one causes failure)
     - connection property 'use block insert' specified (not the default)
    - connection property jdbc 2.1 batch support specified (not the default)
     - expected to fail with 1 row with -3 returned
     - verify that 0 rows were inserted (because it was done in a secondary commit cycle)
**/
    public void Var020() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 20;
        int [] expected = new int[] { -3, -3, -3, -3, -3,
            -3, -3, -3, -3, -3,
            -3, -3, -3, -3, -3,
            -3, -3, -3, -3, -3};

        int [] expectedToolbox = new int[0]; 

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then nothing is entered
	    expected=expectedToolbox; 
	} 

        try {
            newSetup("use block insert=true;batch style=2.1");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
21) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows 
     - connection property 'use block insert' specified (not the default)
     - connection is set to autocommit
     - expected to work with 1 row with 1 returned
     - verify that 20 rows were inserted.
**/
    public void Var021() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 0;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("use block insert=true");
            connection_.setAutoCommit(true);

            PreparedStatement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
22) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows (first one causes failure)
     - connection property 'use block insert' specified (not the default)
     - connection is set to autocommit
    - expected to fail with no update counts returned.
     - verify that 0 rows were inserted (autocommitted is based on the whole operation)
**/
    public void Var022() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 1;
        int [] expected = new int[] {};

        try {
            newSetup("use block insert=true");
            connection_.setAutoCommit(true);

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
23) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows (10th one causes failure)
     - connection property 'use block insert' specified (not the default)
    - connection is set to autocommit
     - expected to fail with no update counts returned.
     - verify that 0 rows were inserted (autocommitted is based on the whole operation)
**/
    public void Var023() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 10;
        int [] expected = new int[] {};

	int [] expectedToolbox = new int[] { -2,-2,-2, -2,-2,-2, -2,-2,-2}; 
	int expectedRowCount = 1; 

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then up to that point is processed because autocommit is on
	    expected=expectedToolbox;
	    expectedRowCount = 10; 
	} 

        try {
            newSetup("use block insert=true");
            connection_.setAutoCommit(true);

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expectedRowCount),"success1="+success1+" rowCount="+rowCount+" "+ sb.toString());

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
24) PreparedStatement batch (able to use blocking) 
    - inserts 20 rows (last one causes failure)
     - connection property 'use block insert' specified (not the default)
    - connection is set to autocommit
    - expected to fail with no update counts returned.
     - verify that 0 rows were inserted (autocommitted is based on the whole operation)
**/
    public void Var024() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 20;
        int [] expected = new int[] {};

	int [] expectedToolbox = new int[] { -2,-2,-2, -2,-2,-2, -2,-2,-2, -2,-2,-2,-2, -2,-2,-2, -2,-2,-2}; 

	int expectedRowCount = 1; 
	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    // For toolbox, if an exception occurs while
            // processing the batch, then upto that point is processed because autocommit is on
	    expected=expectedToolbox;
	    expectedRowCount = 20; 
	} 

        try {
            newSetup("use block insert=true");
            connection_.setAutoCommit(true);

            PreparedStatement ps = buildBatch(arraySize, failurePos);

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

            assertCondition(success1 && (rowCount == expectedRowCount),"success1="+success1+"rowCount="+rowCount+" "+sb.toString());

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
25) PreparedStatement batch (can't use blocking) 
     - updates 20 rows 
     - connection property 'use block insert' specified (not the default)
     - connection is set to autocommit
     - expected to work with 1 row with 1 returned
     - verify that 20 rows were inserted.
**/
    public void Var025() {
	    sb.setLength(0);     
        int arraySize = 20;
        int failurePos = 0;
        int [] expected = new int[] { 1, 2, 3, 4, 5,
            6, 7, 8, 9, 10,
            11, 12, 13, 14, 15,
            16, 17, 18, 19, 20};

        try {
            // create a table that has a significant number of values in it.
            newSetup("use block insert=true");
            PreparedStatement ps = buildBatch(arraySize, failurePos);
            int[] counts = ps.executeBatch();
            connection_.commit();
            ps.close();

            // Build a batch that will do updates instead of inserts.
            ps = buildBatchUpdates(arraySize);
            counts = ps.executeBatch();

            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;
            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
26) PreparedStatement batch (can't use blocking) 
     - inserts rows with subselect
     - connection property 'use block insert' specified (not the default)
     - connection is set to autocommit
     - expected to work with 1 row with 1 returned
     - verify that 20 rows were inserted.
**/
    public void Var026() {
	    sb.setLength(0);     
        int arraySize = 20;
        int [] expected = new int[] { 1, 2, 3, 4, 5,
            6, 7, 8, 9, 10,
            11, 12, 13, 14, 15,
            16, 17, 18, 19, 20};

        try {
            // create a table that has a significant number of values in it.
            newSetup("use block insert=true");

            // Build a batch that will do inserts with subselects.
            PreparedStatement ps = buildBatchSubselect(arraySize);
            int[] counts = ps.executeBatch();

            boolean success1 = verifyCounts(counts, expected);

            int rowCount = 0;
            try {
                // Verify that the rows are in the table.
                rowCount = verifyRows();
            } catch (SQLException e) {
                failed("can't obtain row count.");
            }

            int expectedCount = 1;
            for (int i = 0; i < expected.length; i++)
                expectedCount += expected[i];


            assertCondition(success1 && (rowCount == expectedCount),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
27) PreparedStatement batch (able to use blocking) 
     - inserts 1 row (special case situation)
     - no connection properties specified
     - expected to work with 1 row with 1 returned
**/
    public void Var027() {
	    sb.setLength(0);     
        int arraySize = 1;
        int failurePos = 0;
        int [] expected = new int[] { 1};

        try {
            newSetup("");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
28) PreparedStatement batch (able to use blocking) 
     - inserts 1000 rows (special case situation - rebind needed)
     - no connection properties specified
     - expected to work with 1000 rows with 1 returned
**/
    public void Var028() {
	    sb.setLength(0);     
        int arraySize = 1000;
        int failurePos = 0;
        int [] expected = new int[1000];
        for (int i = 0; i < 1000; i++) {
            expected[i] = 1;
        }

        try {
            newSetup("");

            PreparedStatement ps = buildBatch(arraySize, failurePos);

            int[] counts = ps.executeBatch();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
29) PreparedStatement batch (able to use blocking) 
    - no connection properties specified
     - execute an update
     - inserts 20 rows 
     - execute an update
     - expected to work
**/
    public void Var029() {
	    sb.setLength(0);     
        int arraySize = 20;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("");

            // Execute a statement
            PreparedStatement ps = 
            connection_.prepareStatement("insert into " + JDBUTest.BUTEST + " values(?, ?, ?)");

            ps.setInt(1, 1000);
            ps.setInt(2, 1000);
            ps.setInt(3, 1000);
            boolean success1 = (ps.executeUpdate() == 1);


            // Build and execute a batch
            for (int i = 1; i <= arraySize; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i);
                }
                ps.addBatch();
            }

            int[] counts = ps.executeBatch();
            boolean success2 = verifyCounts(counts, expected);


            // Execute another statement
            ps.setInt(1, 2000);
            ps.setInt(2, 2000);
            ps.setInt(3, 2000);
            boolean success3 = (ps.executeUpdate() == 1);

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && success2 && success3 && (rowCount == expected.length + 3),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
30) PreparedStatement batch (able to use blocking) 
    - no connection properties specified
    - inserts 20 rows 
     - execute an update
    - inserts 20 rows 
     - expected to work
**/
    public void Var030() {
	    sb.setLength(0);     
        int arraySize = 20;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetup("");

            // Execute a statement
            PreparedStatement ps = 
            connection_.prepareStatement("insert into " + JDBUTest.BUTEST + " values(?, ?, ?)");

            // Build and execute a batch
            for (int i = 1; i <= 20; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i);
                }
                ps.addBatch();
            }

            int[] counts = ps.executeBatch();
            boolean success1 = verifyCounts(counts, expected);

            // Execute a statement
            ps.setInt(1, 1000);
            ps.setInt(2, 1000);
            ps.setInt(3, 1000);
            boolean success2 = (ps.executeUpdate() == 1);


            // Build and execute another batch
            for (int i = 1; i <= arraySize; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i + 100);
                }
                ps.addBatch();
            }

            counts = ps.executeBatch();
            boolean success3 = verifyCounts(counts, expected);


            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && success2 && success3 && (rowCount == (expected.length * 2) + 2),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
31) PreparedStatement batch (able to use blocking) 
    - no connection properties specified
     - execute small batch
     - execute big   batch
     - execute small batch
     - expected to work
**/
    public void Var031() {
	    sb.setLength(0);     
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
            PreparedStatement ps = 
            connection_.prepareStatement("insert into " + JDBUTest.BUTEST + " values(?, ?, ?)");

            // Build and execute a small batch
            for (int i = 1; i <= 20; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i);
                }
                ps.addBatch();
            }

            int[] counts = ps.executeBatch();
            boolean success1 = verifyCounts(counts, expectedSmall);


            // Build and execute a big batch
            for (int i = 1; i <= 1000; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i + 100);
                }
                ps.addBatch();
            }

            counts = ps.executeBatch();
            boolean success2 = verifyCounts(counts, expectedBig);


            // Build and execute another small batch
            for (int i = 1; i <= 20; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i + 10000);
                }
                ps.addBatch();
            }

            counts = ps.executeBatch();
            boolean success3 = verifyCounts(counts, expectedSmall);


            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && success2 && success3 && (rowCount == 1041),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
32) PreparedStatement batch (able to use blocking) 
    - no connection properties specified
     - execute big   batch
     - execute small batch
     - execute big   batch
     - expected to work
**/
    public void Var032() {
	    sb.setLength(0);     
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
            PreparedStatement ps = 
            connection_.prepareStatement("insert into " + JDBUTest.BUTEST + " values(?, ?, ?)");

            // Build and execute a big batch
            for (int i = 1; i <= 1000; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i);
                }
                ps.addBatch();
            }

            int[] counts = ps.executeBatch();
            boolean success1 = verifyCounts(counts, expectedBig);


            // Build and execute a small batch
            for (int i = 1; i <= 20; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i + 1001);
                }
                ps.addBatch();
            }

            counts = ps.executeBatch();
            boolean success2 = verifyCounts(counts, expectedSmall);



            // Build and execute another big batch
            for (int i = 1; i <= 1000; i++) {
                for (int j = 1; j <= 3; j++) {
                    ps.setInt(j, i + 10000);
                }
                ps.addBatch();
            }

            counts = ps.executeBatch();
            boolean success3 = verifyCounts(counts, expectedBig);


            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && success2 && success3 && (rowCount == 2021),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
33) PreparedStatement batch (able to use blocking) 
    - no connection properties specified
     - execute small batch
     - execute big   batch
     - execute small batch
     - expected to work
**/
    public void Var033() {
	    sb.setLength(0);     
        int [] expectedBig = new int[1000];
        for (int i = 0; i < 1000; i++) {
            expectedBig[i] = 1;
        }

        try {
            newSetup("");

            // Execute a statement
            PreparedStatement ps = 
            connection_.prepareStatement("insert into " + JDBUTest.BUTEST + " values(?, ?, ?)");

            // Build and execute a batch with null values.
            for (int i = 1; i <= 1000; i++) {
                ps.setInt(1, i);
                if ((i % 2) == 0) {
                    ps.setNull(2, Types.INTEGER);
                    ps.setNull(3, Types.INTEGER);
                } else {
                    ps.setInt(2, i);
                    ps.setInt(3, i);
                }
                ps.addBatch();
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
                            System.out.println("Failed on row " + count+" "+first+","+second+","+third);
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


            assertCondition(success1 && success2 && (count == 1001),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
34) PreparedStatement batch (able to use blocking) 
    - no connection properties specified
     - execute an empty batch 
     - expected to work
**/
    public void Var034() {
	    sb.setLength(0);     
        int [] expected = new int[] { };

        try {
            newSetup("");

            // Execute a statement
            PreparedStatement ps = 
            connection_.prepareStatement("insert into " + JDBUTest.BUTEST + " values(?, ?, ?)");

            // Set all the parameters.
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ps.setInt(3, 1);

            // NOTE:  Not added to the batch here.
            int[] counts = ps.executeBatch();
            boolean success1 = verifyCounts(counts, expected);

            ps.close();

            // Verify that the rows are in the table.
            int rowCount = verifyRows();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
35) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows of LOB data
     - no connection properties specified
     - expected to work with 20 rows of 1s returned
**/
    public void Var035() {
	    sb.setLength(0);     
        int arraySize = 20;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetupLobs("");

            PreparedStatement ps = buildBatchLobs(arraySize);

            int[] counts = ps.executeBatch();
            connection_.commit();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRowsLobs();

            assertCondition(success1 && (rowCount == expected.length + 1), "success1 = " + success1 + " AND SHOULD BE true, rowCount = " + rowCount + " AND SHOULD equal (expected.length + 1) = " + (expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
36) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows of LOB data
     - set the lob threshold to something large enough that the lobs
       should get inlined.
     - expected to work with 20 rows of 1s returned
**/
    public void Var036() {
	    sb.setLength(0);     
        int arraySize = 20;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetupLobs("lob threshold=5000");

            PreparedStatement ps = buildBatchLobs(arraySize);

            int[] counts = ps.executeBatch();
            connection_.commit();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRowsLobs();

            assertCondition(success1 && (rowCount == expected.length + 1), "success1 = " + success1 + " AND SHOULD BE true, rowCount = " + rowCount + " AND SHOULD equal (expected.length + 1) = " + (expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }






/**
37) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows of LOB data (no parms except lobs)
     - no connection properties specified
     - expected to work with 20 rows of 1s returned
**/
    public void Var037() {
	    sb.setLength(0);     
        int arraySize = 20;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetupLobs2("");

            PreparedStatement ps = buildBatchLobs2(arraySize);

            int[] counts = ps.executeBatch();
            connection_.commit();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRowsLobs2();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
38) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows of LOB data (no parms except lobs)
     - set the lob threshold to something large enough that the lobs
       should get inlined.
     - expected to work with 20 rows of 1s returned
**/
    public void Var038() {
	    sb.setLength(0);     
        int arraySize = 20;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetupLobs2("lob threshold=5000");

            PreparedStatement ps = buildBatchLobs2(arraySize);

            int[] counts = ps.executeBatch();
            connection_.commit();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRowsLobs2();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
39) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows of LOB data
     - no connection properties specified
     - expected to work with 20 rows of 1s returned
     - and the number of lobs is large....
    - NOTE:  This test is not working today.     
**/
    public void Var039() {
	    sb.setLength(0);     
        int arraySize = 32;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1};

        try {
            newSetupLobs("");

            PreparedStatement ps = buildBatchLobs(arraySize);

            int[] counts = ps.executeBatch();
            connection_.commit();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRowsLobs();

            assertCondition(success1 && (rowCount == expected.length + 1), "success1 = " + success1 + " AND SHOULD BE true, rowCount = " + rowCount + " AND SHOULD equal (expected.length + 1) = " + (expected.length + 1));

            ps.close();

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
40) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows of LOB data
     - set the lob threshold to something large enough that the lobs
       should get inlined.
     - expected to work with 20 rows of 1s returned
    - and the number of lobs is large....
    - NOTE:  This test is not working today.     
**/
    public void Var040() {
	    sb.setLength(0);     
        int arraySize = 20;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetupLobs("lob threshold=5000");

            PreparedStatement ps = buildBatchLobs(arraySize);

            int[] counts = ps.executeBatch();
            connection_.commit();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRowsLobs();

            assertCondition(success1 && (rowCount == expected.length + 1), "success1 = " + success1 + " AND SHOULD BE true, rowCount = " + rowCount + " AND SHOULD equal (expected.length + 1) = " + (expected.length + 1));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }






/**
41) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows of LOB data (no parms except lobs)
     - no connection properties specified
     - expected to work with 20 rows of 1s returned
     - NOTE:  This test is not working today.     
    - and the number of lobs is large....
    - NOTE:  This test is not working today.     
**/
    public void Var041() {
	    sb.setLength(0);     
        int arraySize = 20;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetupLobs2("");

            PreparedStatement ps = buildBatchLobs2(arraySize);

            int[] counts = ps.executeBatch();
            connection_.commit();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRowsLobs2();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    }



/**
42) PreparedStatement batch (able to use blocking) 
     - inserts 20 rows of LOB data (no parms except lobs)
     - set the lob threshold to something large enough that the lobs
       should get inlined.
     - expected to work with 20 rows of 1s returned
     - NOTE:  This test is not working today.     
    - and the number of lobs is large....
    - NOTE:  This test is not working today.     
**/
    public void Var042() {
	    sb.setLength(0);     
        int arraySize = 20;
        int [] expected = new int[] { 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1};

        try {
            newSetupLobs2("lob threshold=5000");

            PreparedStatement ps = buildBatchLobs2(arraySize);

            int[] counts = ps.executeBatch();
            connection_.commit();

            // Test the update counts.
            boolean success1 = verifyCounts(counts, expected);

            // Verify that the rows are in the table.
            int rowCount = verifyRowsLobs2();

            assertCondition(success1 && (rowCount == expected.length + 1),sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception"+sb.toString());
        } 

    }


/*
Additional tests needed:
    - batch over a stored procedure call.

- Use lobs for a batch then just use the statement to do an insert
- Use the statement then try to do a large batch
- Do small batch, large batch, small batch and the reverse

*/

}



