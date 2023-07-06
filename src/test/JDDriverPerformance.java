///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDriverPerformance.java
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
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDriverPerformance.java
//
// Classes:      JDDriverPerformance
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

/**
 * Testcase JDDriverPerformance. This tests the performance of the JDBC driver.
 **/
public class JDDriverPerformance extends JDTestcase {

  private static final int PROBABILITY = 3;

  private static final int AGE = 0;

  static int TABLEROWS = 100000;

  // Private data.
  private Connection connection_;

  private static Hashtable insertSetupDone = new Hashtable();

  private String driverName = "UNKNOWN";

  /**
   * Constructor.
   **/
  public JDDriverPerformance(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String powerUserID, String powerPassword) {
    super(systemObject, "JDDriverPerformance", namesAndVars, runMode,
        fileOutputStream,  password);
  }

  /**
   * Performs setup needed before running variations.
   *
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {

      connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

      int driver = getDriver();
      switch (driver) {
	  case JDTestDriver.DRIVER_NATIVE:
	      driverName = "NATIVE";
	      break;
	  case JDTestDriver.DRIVER_JCC:
	      driverName = "JCC";
	      break;
	  case JDTestDriver.DRIVER_TOOLBOX:
	      if (getSubDriver() == JDTestDriver.SUBDRIVER_JT400ANDROID) {
	        driverName = "JT400ANDROID";
	      } else { 
	        driverName = "TOOLBOX";
	      }
	      break;
	  case JDTestDriver.DRIVER_JTOPENLITE:
	      driverName = "JTOPENLITE";
	      break;

	  default:
	      driverName = "UNKNOWN=" + driver;
	      break;

      }

  }

  /**
   * Performs cleanup needed after running variations.
   *
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {

    // Make sure the tables used by the testcase are gone.
      try { 

	  Statement s = connection_.createStatement();
	  String sql = "";
	  String [] tables =  {
	      JDDriverTest.COLLECTION + ".JDDPerfV1",
	      JDDriverTest.COLLECTION + ".JDDPerfV3",
	      JDDriverTest.COLLECTION + ".JDDPerfV5",
	      JDDriverTest.COLLECTION + ".JDDPerfV10",
	  };
	  for (int i = 0 ; i < tables.length; i++) { 
	      try {
		  sql = "DROP TABLE "+tables[i];
		  s.executeUpdate(sql); 
	      } catch (Exception e) {
		  System.out.println("Warning:  error on "+sql);
		  e.printStackTrace(System.out); 
	      }
	  }
	  connection_.close();
      } catch (Exception e) {
	  System.out.println("Error during cleanup");
	  e.printStackTrace(System.out); 
      } 

  }

  public static String lastSql;
  public static long staticInsertSetup(Connection connection,  String testname, String testSchema, String testTable,  String columnDefinition, String insertPattern, int driver) throws Exception {

      long testTime;
    if (insertSetupDone.get(testTable) != null ) {
      return 1;
    }
    String insertString = "";


      Statement stmt = connection.createStatement();

      // Make sure the collection exists
      JDSetupCollection.create(connection, testSchema, false);

      // Make sure the table is gone
      try {
        stmt.executeUpdate("drop table " + testTable);
      } catch (Exception e) {
      }

      // Create the table
      lastSql = "Create Table  " + testTable + " ("+columnDefinition+")";
      stmt.executeUpdate(lastSql);

      // Now run the benchmark
      long startTime = System.currentTimeMillis();
      lastSql = "Insert into " + testTable + " values (?)";
      PreparedStatement pstmt = connection.prepareStatement(lastSql);
      int    insertPatternLength = insertPattern.length();
      // Create the insertion pattern based on the number of digits
      String insertPattern1 = insertPattern.substring(0,insertPatternLength-1);
      String insertPattern2 = insertPattern.substring(0,insertPatternLength-2);
      String insertPattern3 = insertPattern.substring(0,insertPatternLength-3);
      String insertPattern4 = insertPattern.substring(0,insertPatternLength-4);
      String insertPattern5 = insertPattern.substring(0,insertPatternLength-5);
      String insertPattern6 = insertPattern.substring(0,insertPatternLength-6);

      boolean batchSupport = true;
      if (driver == JDTestDriver.DRIVER_JTOPENLITE) {
	  batchSupport = false;
      }
      for (int i = 0; i < TABLEROWS; i++) {
        if (i > 99999) {
          insertString = insertPattern6 + i;
        } else if (i > 9999) {
          insertString = insertPattern5 + i;
        } else if (i > 999) {
          insertString = insertPattern4 + i;
        } else if (i > 99) {
          insertString = insertPattern3 + i;
        } else if (i > 9) {
          insertString = insertPattern2 + i;
        } else {
          insertString = insertPattern1 + i;
        }
        pstmt.setString(1, insertString);
	if (batchSupport) {
	    pstmt.addBatch();
	} else {
	    pstmt.executeUpdate();
	}
      }
      if (batchSupport) {
	  pstmt.executeBatch();
      }

      long endTime = System.currentTimeMillis();

      testTime =  endTime - startTime;
      insertSetupDone.put(testTable, testTable);

      return testTime;


  }


  public boolean insertSetup(String testname, String testTable, boolean recordResults, String columnDefinition, String insertPattern)
      throws Exception {

      try {
      long runtime  = staticInsertSetup(connection_, testname, JDDriverTest.COLLECTION, testTable,  columnDefinition, insertPattern, getDriver());



      if (recordResults) {

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        JDPerformanceUtil.recordTestRun(connection_, ts, testname, driverName,
            (runtime));

        JDPerformanceUtil.showRunComparision(connection_, testname, ts);
        insertSetupDone.put(testTable, testTable);
        assertCondition(true);
        return true;
      }

    } catch (Exception e) {
      if (recordResults) {
        failed(e, "Unexpected Exception lastSql = " + lastSql);
        return false;
      } else {
        throw e;
      }
    }
    return false;
  }

  public static long staticQueryTest(Connection connection, String testname, String collection, String tablename, int iterations) throws Exception {

      String testTable = collection + "."+tablename;

      Statement stmt = connection.createStatement();

      // Now run the benchmark
      long startTime = System.currentTimeMillis();
      while (iterations > 0) {
        lastSql = "select * from " + testTable ;
        ResultSet rs = stmt.executeQuery(lastSql);
        while (rs.next()) {
            rs.getString(1);
        }
        rs.close();
        iterations --;
      }
      long endTime = System.currentTimeMillis();
      stmt.close();

      return endTime-startTime;
    }

  public boolean queryTest(String testname, String tablename) throws Exception {
    try {
      long runTime  = staticQueryTest(connection_, testname,JDDriverTest.COLLECTION,tablename, 1);


      Timestamp ts = new Timestamp(System.currentTimeMillis());

        JDPerformanceUtil.recordTestRun(connection_, ts, testname, driverName,
            runTime);

        JDPerformanceUtil.showRunComparision(connection_, testname, ts);
        assertCondition(true);
        return true;
    } catch (Exception e) {
        failed(e, "Unexpected Exception ");
        return false;
    }

  }

  /**
   * Test the insertion of 100,000 (TABLEROWS) varchar(10/20) records (1
   * megabyte of data)
   **/
  public void Var001() {
    try {
      if ( ! checkLongRunning("JDDriverPerformance", 1, PROBABILITY, AGE)) {
        return;
      }
    System.out.println("Running Var001 -- Test the insertion of 100,000 (TABLEROWS) varchar(10/20) records (1 megabytes of data_");
      boolean passed = insertSetup("JDDPerf001", JDDriverTest.COLLECTION + ".JDDPerfV1", true, "C1 VARCHAR(20)", "ROW0000000");
      finishLongRunning("JDDriverPerformance", 1, passed );

    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 1, false );
      failed(e);
    }
  }

  /**
   * Test the query of 100,000 (TABLEROWS) varchar(10/20) records (1 megabyte of
   * data)
   **/
  public void Var002() {
    if ( ! checkLongRunning("JDDriverPerformance", 2, PROBABILITY, AGE)) {
      return;
    }
    System.out.println("Running Var002 -- Test the query of 100,000 (TABLEROWS) varchar(10/20) records (1 megabyte of data ");
    try {
      insertSetup("JDDPerf002", JDDriverTest.COLLECTION + ".JDDPerfV1", false, "C1 VARCHAR(20)", "ROW0000000");
      boolean passed = queryTest("JDDPerf002", "JDDPerfV1");
      finishLongRunning("JDDriverPerformance", 2, passed );
    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 2, false );
      failed(e);
    }
  }

  /**
   * Test the insertion of 100,000 (TABLEROWS) varchar(40/80) records
   **/
  public void Var003() {
    if ( ! checkLongRunning("JDDriverPerformance", 3, PROBABILITY, AGE)) {
      return;
    }
    System.out.println("Running Var003 -- Test the insertion of 100,000 (TABLEROWS) varchar(40/80) records ");
    try {
      boolean passed = insertSetup("JDDPerf003", JDDriverTest.COLLECTION + ".JDDPerfV3", true, "C1 VARCHAR(80)", "123456789801234567898012345678980ROW0000000");
      finishLongRunning("JDDriverPerformance", 3, passed );

    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 3, false );
      failed(e);
    }
  }

  /**
   * Test the query of 100,000 (TABLEROWS) varchar(40/80) records
   **/
  public void Var004() {
/*
    if ( ! checkLongRunning("JDDriverPerformance", 1, PROBABILITY, AGE)) {
      return;
    }
*/
   System.out.println("Running Var004 -- Test the query of 100,000 (TABLEROWS) varchar(40/80) records ");
    try {
      insertSetup("JDDPerf004", JDDriverTest.COLLECTION + ".JDDPerfV3", false, "C1 VARCHAR(80)", "123456789801234567898012345678980ROW0000000");
      boolean passed =queryTest("JDDPerf004", "JDDPerfV3");
      finishLongRunning("JDDriverPerformance", 4, passed );
    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 4, false );
      failed(e);
    }
  }

  /**
   * Test the insertion of 100,000 (TABLEROWS) vargraphic(40/80) records
   **/
  public void Var005() {
/*
    if ( ! checkLongRunning("JDDriverPerformance", 1, PROBABILITY, AGE)) {
      return;
    }
*/

  System.out.println("Running Var005 -- Test the insertion of 100,000 (TABLEROWS) vargraphic(40/80) records");
    try {
      boolean passed =insertSetup("JDDPerf005", JDDriverTest.COLLECTION + ".JDDPerfV5", true, "C1 VARGRAPHIC(80) CCSID 1200", "123456789801234567898012345678980ROW0000000");
      finishLongRunning("JDDriverPerformance", 5, passed );
    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 5, false );
      failed(e);
    }
  }

  /**
   * Test the query of 100,000 (TABLEROWS) vargraphic (40/80) records
   **/
  public void Var006() {
/*
    if ( ! checkLongRunning("JDDriverPerformance", 1, PROBABILITY, AGE)) {
      return;
    }
*/
  System.out.println("Running Var006 -- Test the query of 100,000 (TABLEROWS) vargraphic (40/80) records ");

    try {
      insertSetup("JDDPerf005", JDDriverTest.COLLECTION + ".JDDPerfV5", false, "C1 VARGRAPHIC(80) CCSID 1200", "123456789801234567898012345678980ROW0000000");
      boolean passed =queryTest("JDDPerf006", "JDDPerfV5");
      finishLongRunning("JDDriverPerformance", 6, passed );
    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 6, false );
      failed(e);
    }
  }



  /**
   * Test the query of 100,000 (TABLEROWS) vargraphic (40/80) records with 512 block size
   **/
  public void Var007() {
      Connection savedConnection = connection_;
      connection_ = null;
  System.out.println("Running Var007 -- Test the query of 100,000 (TABLEROWS) vargraphic (40/80) records with 512 block size ");
    try {

	int driver = getDriver();
	switch (driver) {
	    case JDTestDriver.DRIVER_NATIVE:
		connection_ = testDriver_.getConnection(baseURL_+";block size=512", userId_, encryptedPassword_);
		break;
	    case JDTestDriver.DRIVER_JCC:
	    case JDTestDriver.DRIVER_JTOPENLITE:
		connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
		break;
	    case JDTestDriver.DRIVER_TOOLBOX:
		connection_ = testDriver_.getConnection(baseURL_+";block size=512", userId_, encryptedPassword_);
		break;
	    default:
		throw new Exception("unknown driver");
	}

	System.out.println("Starting insert");
	long insertStart = System.currentTimeMillis();

	insertSetup("JDDPerf005", JDDriverTest.COLLECTION + ".JDDPerfV5", false, "C1 VARGRAPHIC(80) CCSID 1200", "123456789801234567898012345678980ROW0000000");
	long insertFinish = System.currentTimeMillis();
	System.out.println("Insert done:  Took "+(insertFinish - insertStart)+" milliseconds");


      boolean passed =queryTest("JDDPerf007", "JDDPerfV5");
      finishLongRunning("JDDriverPerformance", 7, passed );
    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 7, false );
      failed(e);
    }
    if (connection_ != null) {
	try {
	    connection_.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    connection_ = savedConnection;
  }



  /**
   * Test the query of 100,000 (TABLEROWS) vargraphic (40/80) records with 256 block size
   **/
  public void Var008() {
      Connection savedConnection = connection_;
      connection_ = null;

  System.out.println("Running Var008 -- Test the query of 100,000 (TABLEROWS) vargraphic (40/80) records with 256 block size ");

    try {

	int driver = getDriver();
	switch (driver) {
	    case JDTestDriver.DRIVER_NATIVE:
		connection_ = testDriver_.getConnection(baseURL_+";block size=256", userId_, encryptedPassword_);
		break;
	    case JDTestDriver.DRIVER_JCC:
	  case JDTestDriver.DRIVER_JTOPENLITE:
		connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
		break;
	    case JDTestDriver.DRIVER_TOOLBOX:
		connection_ = testDriver_.getConnection(baseURL_+";block size=256", userId_, encryptedPassword_);
		break;
	    default:
		throw new Exception("unknown driver");
	}

	System.out.println("Starting insert");
	long insertStart = System.currentTimeMillis();

	insertSetup("JDDPerf005", JDDriverTest.COLLECTION + ".JDDPerfV5", false, "C1 VARGRAPHIC(80) CCSID 1200", "123456789801234567898012345678980ROW0000000");
	long insertFinish = System.currentTimeMillis();
	System.out.println("Insert done:  Took "+(insertFinish - insertStart)+" milliseconds");


      boolean passed =queryTest("JDDPerf008", "JDDPerfV5");
      finishLongRunning("JDDriverPerformance", 8, passed );
    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 8, false );
      failed(e);
    }
    if (connection_ != null) {
	try {
	    connection_.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    connection_ = savedConnection;
  }



    /**
   * Test the query of 100,000 (TABLEROWS) vargraphic (40/80) records with 1024 block size
   **/
  public void Var009() {
      Connection savedConnection = connection_;
      connection_ = null;
    try {

	System.out.println("Running Var009 -- Test the query of 100,000 (TABLEROWS) vargraphic (40/80) records with 1024 block size ");
	int driver = getDriver();
	switch (driver) {
	    case JDTestDriver.DRIVER_NATIVE:
		connection_ = testDriver_.getConnection(baseURL_+";block size=1024", userId_, encryptedPassword_);
		break;
	    case JDTestDriver.DRIVER_JCC:
	    case JDTestDriver.DRIVER_JTOPENLITE:
	      connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
		break;
	    case JDTestDriver.DRIVER_TOOLBOX:
		connection_ = testDriver_.getConnection(baseURL_+";block size=1024", userId_, encryptedPassword_);
		break;
	    default:
		throw new Exception("unknown driver");
	}

	System.out.println("Starting insert");
	long insertStart = System.currentTimeMillis();

	insertSetup("JDDPerf005", JDDriverTest.COLLECTION + ".JDDPerfV5", false, "C1 VARGRAPHIC(80) CCSID 1200", "123456789801234567898012345678980ROW0000000");
	long insertFinish = System.currentTimeMillis();
	System.out.println("Insert done:  Took "+(insertFinish - insertStart)+" milliseconds");


      boolean passed =queryTest("JDDPerf009", "JDDPerfV5");
      finishLongRunning("JDDriverPerformance", 9, passed );
    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 9, false );
      failed(e);
    }
    if (connection_ != null) {
	try {
	    connection_.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    connection_ = savedConnection;
  }





  /**
   * Test the insertion of 100,000 (TABLEROWS) char(80) records
   **/
  public void Var010() {
    if ( ! checkLongRunning("JDDriverPerformance", 3, PROBABILITY, AGE)) {
      return;
    }
    System.out.println("Running Var010 -- Test the insertion of 100,000 (TABLEROWS) CHAR (40/80) records ");
    try {
      boolean passed = insertSetup("JDDPerf010", JDDriverTest.COLLECTION + ".JDDPerfV10", true, "C1 CHAR(80)", "123456789801234567898012345678980ROW0000000");
      finishLongRunning("JDDriverPerformance", 10, passed );

    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 10, false );
      failed(e);
    }
  }

  /**
   * Test the query of 100,000 (TABLEROWS) char(40/80) records
   **/
  public void Var011() {
/*
    if ( ! checkLongRunning("JDDriverPerformance", 1, PROBABILITY, AGE)) {
      return;
    }
*/
   System.out.println("Running Var011 -- Test the query of 100,000 (TABLEROWS) char(80) records ");
    try {
      insertSetup("JDDPerf011", JDDriverTest.COLLECTION + ".JDDPerfV10", false, "C1 CHAR(80)", "123456789801234567898012345678980ROW0000000");
      boolean passed =queryTest("JDDPerf011", "JDDPerfV10");
      finishLongRunning("JDDriverPerformance", 11, passed );
    } catch (Exception e) {
      finishLongRunning("JDDriverPerformance", 11, false );
      failed(e);
    }
  }



  static String [][] testcaseInfo = {
      /* testcase     testTable    columnDefinition   insertPattern */
  {   "JDDPerf004",   "JDDPerfV3", "C1 VARCHAR(80)",   "123456789801234567898012345678980ROW0000000" },
  {   "JDDPerf006",   "JDDPerfV5", "C1 VARGRAPHIC(80) CCSID 1200", "123456789801234567898012345678980ROW0000000"},
  {   "JDDPerf011",   "JDDPerfV10", "C1 CHAR(80)", "123456789801234567898012345678980ROW0000000"},


  };

  public static void main(String[] args) {
      if (args.length < 5) {
	  System.out.println("Usage:  java test.JDDriverPerformance <URL> <userid> <password> <CREATE|RUN> <testcase> [<iterations>]");
      } else {
	  try {
	      String schema = "JDPERFDATA";
	      String url = args[0];
	      String userid = args[1];
	      String password = args[2];
	      String option = args[3];
	      String testcase = args[4];
	      int iterations = 1;
	      if (args.length>= 6) {
	        iterations = Integer.parseInt(args[5]);
	      }
        String testTable = null;
        String columnDefinition = null ;
        String insertPattern = null ;
        int driver = 0;
        boolean testcaseFound = false;

        for (int i = 0; !testcaseFound && i < testcaseInfo.length; i++ ) {
          if (testcaseInfo[i][0].equals(testcase)) {
            testcaseFound = true;
            testTable = testcaseInfo[i][1];
            columnDefinition = testcaseInfo[i][2];
            insertPattern = testcaseInfo[i][3];
          }
        }

        if (!testcaseFound) {
          System.out.println("Error testcase "+testcase+" not found.  Valid testcases....");
          for (int i = 0; !testcaseFound && i < testcaseInfo.length; i++ ) {
            System.out.println(testcaseInfo[i][0]);
          }
        } else {

          if (url.indexOf(":jtopenlite:") > 0) {
            driver = JDTestDriver.DRIVER_JTOPENLITE;
            Class.forName("com.ibm.jtopenlite.database.jdbc.JDBCDriver");
          } else if (url.indexOf(":as400:") > 0) {
            driver = JDTestDriver.DRIVER_TOOLBOX;
            Class.forName("com.ibm.as400.access.AS400JDBCDriver");
          }
          Connection connection = DriverManager.getConnection(url, userid, password);

          if (option.equalsIgnoreCase("CREATE")) {
            System.out.println("Running CREATE for "+testcase);
            long time = staticInsertSetup(connection,  testcase, schema, schema+"."+testTable,  columnDefinition, insertPattern, driver);
            System.out.println("CREATE took "+time+" ms");
          } else if (option.equalsIgnoreCase("RUN")) {
            System.out.println("Running RUN for "+testcase);
            long time = staticQueryTest(connection, testcase, schema, testTable, iterations);
            System.out.println("RUN "+url+" "+testcase+" took "+time+" ms");
          } else {
            System.out.println("option '"+option+"' not recognized, should be CREATE or RUN");
          }


	      connection.close();
        }
	  } catch (Exception e) {
	      e.printStackTrace();
	      System.out.println("Classpath is "+System.getProperty("java.class.path"));
	  }
      }

  }


}
