///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSStress.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.RS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Random;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JobLogUtil;



/**
Testcase JDRSStress.  This tests various combinations of datatypes in a
table.  There is one generic testcase that is run.  Specific testcases
are created for error condition that needed to be fixed.  Otherwise,
the test will run for a specific amount of time to try to detect errors.
**/
public class JDRSStress
extends JDTestcase
{

    // Private data.
    private Connection          connection1_;
    private Connection          connection2_;
    private Connection          connection3_;
    private Statement           statement1_;
    boolean skipCleanup = false; 
    String url_; 
    // private String sql = "";
    private boolean verbose = false;
    protected String connectionProperties=";date format=iso;time format=jis";
    protected boolean usePreparedStatement=false;
    protected boolean useScrollableCursor=false;
    protected boolean directMap = false;
    protected boolean forUpdate = false;
    protected boolean noBlocking = false;
    protected boolean useBatching = false; 
    // 01/05/2016
    // Dropped to 10 seconds in order to get though group test run 
    protected int randomRunTime = 10;
/**
Constructor.
**/
    public JDRSStress (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
	// Note. TestDriver should be set when this testcase is added to the driver.
        super (systemObject, "JDRSStress",
            namesAndVars, runMode, fileOutputStream,
            password);
    }


    public JDRSStress (AS400 systemObject,
		          String testname,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, testname,
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
	if (System.getProperty("test.skipCleanup") != null) {
	    skipCleanup = true; 
	} else {
	    skipCleanup = false; 
	}

	if (areBooleansSupported()) {
	    T_COUNT      =  T_BOOLEAN_COUNT ;
	} else {
	    T_COUNT      =  T_PREBOOLEAN_COUNT ;
	}

        // SQL400 - driver neutral...
        url_ = baseURL_+connectionProperties;
        connection1_ = testDriver_.getConnection (url_,systemObject_.getUserId(),encryptedPassword_,"JDRSStress");
        connection2_ = testDriver_.getConnection (url_,systemObject_.getUserId(),encryptedPassword_,"JDRSStress");
        connection3_ = testDriver_.getConnection (url_,systemObject_.getUserId(),encryptedPassword_,"JDRSStress");
	if (useScrollableCursor) {
	    statement1_ = connection1_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
						      ResultSet.CONCUR_READ_ONLY);
	} else {
	    statement1_ = connection1_.createStatement ();
	}
        // @PDC to match current behavior for toolbox date and time
        if (isToolboxDriver())
        {
            typeInfo[14] = new JDRSSQLTypeInfo("DATE", GETSTRING, dateSet, dateReadToolBox);
            typeInfo[15] = new JDRSSQLTypeInfo("TIME", GETSTRING, timeSet, timeReadToolbox);
        }



	String randomRunTimeProperty = System.getProperty("JDRSStress.randomRunTime");
	if (randomRunTimeProperty != null) {
	    int value = Integer.parseInt(randomRunTimeProperty);
	    if (value > 0) {
		randomRunTime = value;
		System.out.println("randomRunTime override using property JDRSStress.randomRunTime="+value); 
	    } 
	} 


    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
	statement1_.close ();
        connection1_.close ();
        connection2_.close ();
        connection3_.close ();
    }
    
    public void cleanupConnections() throws SQLException { 
      connection1_.close ();
      connection2_.close ();
      connection3_.close ();
    }

    public final static int GETSTRING = 1;
    public final static int GETBYTES  = 2;
    public final static int GETBOOLEAN = 3; 
    public final static String[] shortSet =  {"NULL", "0", "1", "2", "3", "32004", "-32005"};
    public final static String[] shortRead = {null,   "0", "1", "2", "3", "32004", "-32005"};

    public final static String[] intSet =  {"NULL", "0", "1", "2", "3", "2000000004", "-2000000005", "8"};
    public final static String[] intRead = {null,   "0", "1", "2", "3", "2000000004", "-2000000005", "8"};

    public final static String[] bigintSet =  {"NULL", "0", "1", "2", "3", "2000000004", "-2000000005", "8", "9"};
    public final static String[] bigintRead = {null,   "0", "1", "2", "3", "2000000004", "-2000000005", "8", "9"};

    public final static String[] realSet =  {"NULL", "0.0", "1.1", "2.2", "3.3", "2000000004", "-2000000005", "8", "9", "10"};
    public final static String[] realRead = {null,   "0.0", "1.1", "2.2", "3.3", "2.0E9", "-2.0E9", "8.0", "9.0", "10.0"};

    public final static String[] floatSet =  {"NULL", "0",   "1.1", "2.2", "3.3", "2000000004", "-2000000005", "8", "9", "10", "11"};
    public final static String[] floatRead = {null,   "0.0", "1.1", "2.2", "3.3", "2.000000004E9", "-2.000000005E9", "8.0", "9.0", "10.0", "11.0"};


    public final static String[] decimal102Set = {
	"NULL", "0.0", "12345.12", "-12345.12", "5", "6", "7",  "8", "9", "10", "11", "12" };
    public final static String[] decimal102Read = {
	null,   "0.00", "12345.12", "-12345.12", "5.00", "6.00", "7.00",  "8.00", "9.00", "10.00", "11.00", "12.00" };

    public final static String[] decimal152Set = {
	"0.0", "12345.12", "-12345.12", "NULL", "5", "6", "7",  "8", "9", "10", "11", "12", "13"   };
    public final static String[] decimal152Read = {
	"0.00", "12345.12", "-12345.12", null, "5.00", "6.00", "7.00",  "8.00", "9.00", "10.00", "11.00", "12.00", "13.00" };

    public final static String[] decimal105Set = {
	"0.0", "12345.12", "-12345.12", "NULL", "5", "6", "7",  "8", "9", "10", "11", "12", "13","14"   };
    public final static String[] decimal105Read = {
	"0.00000", "12345.12000", "-12345.12000", null, "5.00000", "6.00000", "7.00000",  "8.00000", "9.00000", "10.00000", "11.00000", "12.00000", "13.00000", "14.00000"  };

    public final static String[] decimal155Set = {
	"0.0", "12345.12", "-12345.12", "NULL", "5", "6", "7",  "8", "9", "10", "11", "12", "13","14", "15" };
    public final static String[] decimal155Read = {
	"0.00000", "12345.12000", "-12345.12000", null, "5.00000", "6.00000", "7.00000",  "8.00000", "9.00000", "10.00000", "11.00000", "12.00000", "13.00000", "14.00000", "15.00000"  };

    public final static String[] decimal314Set = {
	"0.0", "123456789012345678901234567.8901", "-12345.12", "NULL", "1", "6", "7",  "8", "9", "10", "11", "12", "13","14", "15" };
    public final static String[] decimal314Read = {
	"0.0000", "123456789012345678901234567.8901", "-12345.1200", null, "1.0000", "6.0000", "7.0000",  "8.0000", "9.0000", "10.0000", "11.0000", "12.0000", "13.0000", "14.0000", "15.0000"  };




    public final static String[] fixedStringSet = {
	"NULL", "''", "'Hello'" };
    public final static String[] fixedStringRead = {
	null,   "                                        ", "Hello                                   " };


    public final static String[] char4Set = {
	"NULL", "''", "'1234'", "'abcd'" };
    public final static String[] char4Read = {
	null,   "    ", "1234", "abcd" };


    public final static String[] varchar4Set = {
	"NULL", "''", "'1234'", "'abcd'" };
    public final static String[] varchar4Read = {
	null,   "", "1234", "abcd" };


    public final static String[] stringSet = {
	"NULL", "''", "'Hello'", "NULL" };
    public final static String[] stringRead = {
	null,   "", "Hello", null };

    // public final static String[] dateSet = {"NULL", "'3/4/2005'", "'5/6/2007'", "'2/3/1999'", "NULL" };

    public final static String[] dateSet =  { "NULL",  "'2005-03-04'", "'2007-05-06'", "'1999-02-03'", "NULL" };

    public final static String[] dateRead = {
	null,   "2005-03-04", "2007-05-06", "1999-02-03", null };

    public final static String[] dateReadToolBox = {
	null,   "2005-03-04", "2007-05-06", "1999-02-03", null };

    /* public final static String[] dateReadToolBox = {
    null,   "03/04/05", "05/06/07", "02/03/99", null };  */

    public final static String[] timeSet = {
	"'12:00:00'", "'01:00:00'", "'03:00:00'", "NULL" };
    public final static String[] timeRead = {
	"12:00:00", "01:00:00", "03:00:00", null };
    public final static String[] timeReadToolbox = {
    "12:00:00", "01:00:00", "03:00:00", null };

    public final static String[] timestampSet = {
	"'1999-10-10-12.12.12.123456'", "'2000-10-10-12.12.12.123456'", "'2005-10-10-12.12.12.123456'", "NULL" };
    public final static String[] timestampRead = {
	"1999-10-10 12:12:12.123456", "2000-10-10 12:12:12.123456", "2005-10-10 12:12:12.123456", null };


    public final static String[] blobSet = {
	"NULL", "BLOB(X'')", "BLOB(X'11223344556677889900')"};
    public final static String[] blobRead = {
	 null,          "",         "11223344556677889900" };


    public final static String[] vcfbSet = {
	"NULL", "X''", "X'11223344556677889900'"};
    public final static String[] vcfbRead = {
	 null,          "",         "11223344556677889900" };

    public final static String[] binarySet = {
  "NULL", "BX''", "BX'11223344556677889900'"};
    public final static String[] binaryRead = {
   null,          "",         "11223344556677889900" };

    public final static String[] fixedBinarySet = {
  "NULL", "BX''", "BX'11223344556677889900'"};
    public final static String[] fixedBinaryRead = {
   null,          
   "00000000000000000000000000000000000000000000000000000000000000000000000000000000",
   "11223344556677889900000000000000000000000000000000000000000000000000000000000000" };



    public final static String[]  booleanSet = {
	"NULL", "'true'", "'false'"}; 
    public final static String[] booleanRead = {
	null,   "true", "false"}; 


    static JDRSSQLTypeInfo typeInfo[] = {
        /* 0 */
	new JDRSSQLTypeInfo("SMALLINT",
			    GETSTRING,
			    shortSet ,
			    shortRead),
        /* 1 */
	new JDRSSQLTypeInfo("INTEGER",
			    GETSTRING,
			    intSet ,
			    intRead),
        /* 2 */
	new JDRSSQLTypeInfo("BIGINT",
			    GETSTRING,
			    bigintSet ,
			    bigintRead),
        /* 3 */
	new JDRSSQLTypeInfo("REAL",
			    GETSTRING,
			    realSet ,
			    realRead),
        /* 4 */
	new JDRSSQLTypeInfo("FLOAT",
			    GETSTRING,
			    floatSet ,
			    floatRead),
        /* 5 */
	new JDRSSQLTypeInfo("DOUBLE",
			    GETSTRING,
			    floatSet ,
			    floatRead),
        /* 6 */
	new JDRSSQLTypeInfo("DECIMAL(10,2)",
			    GETSTRING,
			    decimal102Set ,
			    decimal102Read),
        /* 7 */
	new JDRSSQLTypeInfo("DECIMAL(15,2)",
			    GETSTRING,
			    decimal152Set ,
			    decimal152Read),
        /* 8 */
	new JDRSSQLTypeInfo("DECIMAL(10,5)",
			    GETSTRING,
			    decimal105Set ,
			    decimal105Read),
        /* 9 */
	new JDRSSQLTypeInfo("DECIMAL(15,5)",
			    GETSTRING,
			    decimal155Set ,
			    decimal155Read),

        /* 10 */
	new JDRSSQLTypeInfo("CHAR(40)",
			    GETSTRING,
			    fixedStringSet ,
			    fixedStringRead),
        /* 11 */
	new JDRSSQLTypeInfo("VARCHAR(40)",
			    GETSTRING,
			    stringSet ,
			    stringRead),

        /* 12 */
	new JDRSSQLTypeInfo("GRAPHIC(40) CCSID 13488 ",
			    GETSTRING,
			    fixedStringSet ,
			    fixedStringRead),
        /* 13 */
	new JDRSSQLTypeInfo("VARGRAPHIC(40) CCSID 13488 ",
			    GETSTRING,
			    stringSet ,
			    stringRead),
        /* 14 */
	new JDRSSQLTypeInfo("DATE",
			    GETSTRING,
			    dateSet ,
			    dateRead),
        /* 15 */
	new JDRSSQLTypeInfo("TIME",
			    GETSTRING,
			    timeSet ,
			    timeRead),
        /* 16 */
	new JDRSSQLTypeInfo("TIMESTAMP",
			    GETSTRING,
			    timestampSet ,
			    timestampRead),
        /* 17 */
	new JDRSSQLTypeInfo("CLOB(1000000)",
			    GETSTRING,
			    stringSet ,
			    stringRead),
        /* 18 */
	new JDRSSQLTypeInfo("DBCLOB(1000000) CCSID 13488 ",
			    GETSTRING,
			    stringSet ,
			    stringRead),
	/* 19 */
	new JDRSSQLTypeInfo("BLOB(1000000)",
			    GETBYTES,
			    blobSet ,
			    blobRead),

        /* 20 */
	new JDRSSQLTypeInfo("NUMERIC(10,2)",
			    GETSTRING,
			    decimal102Set ,
			    decimal102Read),
        /* 21 */
	new JDRSSQLTypeInfo("NUMERIC(15,2)",
			    GETSTRING,
			    decimal152Set ,
			    decimal152Read),
        /* 22 */
	new JDRSSQLTypeInfo("NUMERIC(10,5)",
			    GETSTRING,
			    decimal105Set ,
			    decimal105Read),
        /* 23 */
	new JDRSSQLTypeInfo("NUMERIC(15,5)",
			    GETSTRING,
			    decimal155Set ,
			    decimal155Read),

        /* 24 */ 
	new JDRSSQLTypeInfo("VARCHAR(40) FOR BIT DATA",
			    GETBYTES,
			    vcfbSet,
			    vcfbRead),

	        /* 25 */ 
			    new JDRSSQLTypeInfo("VARBINARY(40)",
			            GETBYTES,
			            binarySet,
			            binaryRead),

			            /* 26 */ 
			            new JDRSSQLTypeInfo("BINARY(40)",
			                    GETBYTES,
			                    fixedBinarySet,
			                    fixedBinaryRead),

        /* 27 */
	new JDRSSQLTypeInfo("DECIMAL(31,4)",
			    GETSTRING,
			    decimal314Set ,
			    decimal314Read),

//
// reserved for expansion
//

        /* 28 */ 
	new JDRSSQLTypeInfo("CHAR(4) CCSID 937 ",
			    GETSTRING,
			    char4Set ,
			    char4Read),

        /* 29 */ 
	new JDRSSQLTypeInfo("VARCHAR(4) CCSID 937 ",
			    GETSTRING,
			    varchar4Set ,
			    varchar4Read),

	/* 30 */
	new JDRSSQLTypeInfo("BOOLEAN ",
			    GETBOOLEAN,
			    booleanSet ,
			    booleanRead),



    };
    // these ID's must match the indexes above
    public static final int T_SMALLINT = 0;
    public static final int T_INTEGER  = 1;
    public static final int T_BIGINT   = 2;
    public static final int T_REAL     = 3;
    public static final int T_FLOAT    = 4;
    public static final int T_DOUBLE   = 5;
    public static final int T_DECIMAL102 = 6;
    public static final int T_DECIMAL152 = 7;
    public static final int T_DECIMAL105 = 8;
    public static final int T_DECIMAL155 = 9;
    public static final int T_CHAR       = 10;
    public static final int T_VARCHAR    = 11;
    public static final int T_GRAPHIC    = 12;
    public static final int T_VARGRAPHIC = 13;
    public static final int T_DATE       = 14;
    public static final int T_TIME       = 15;
    public static final int T_TIMESTAMP  = 16;
    public static final int T_CLOB       = 17;
    public static final int T_DBCLOB     = 18;
    public static final int T_BLOB       = 19;
    public static final int T_NUMERIC102 = 20;
    public static final int T_NUMERIC152 = 21;
    public static final int T_NUMERIC105 = 22;
    public static final int T_NUMERIC155 = 23;
    public static final int T_VARCHARFBD = 24;
    public static final int T_VARBINARY  = 25;
    public static final int T_BINARY     = 26;
    public static final int T_DECIMAL314 = 27;
    public static final int T_CHAR4 = 28;
    public static final int T_VARCHAR4 = 29;
    public static final int T_BOOLEAN = 30; 

    public static  int T_MAXCOUNT = 31; 
    public static  int T_COUNT      = 31;
    public static final int T_BOOLEAN_COUNT = 31;
    public static final int T_PREBOOLEAN_COUNT = 30;

    public static int T1 = typeInfo.length; 
    public static int T2 = T1 * T1;
    public static int T3 = T2 * T1;
    public static int T4 = T3 * T1;
    public static int T5 = T4 * T1;

    public static int getId(int a, int b) {
      return  b * T1 + a;
    }

    
    public static int getId(int a, int b, int c) {
	return c * T2 + b * T1 + a;
    }

    public static int getId(int a, int b, int c, int d) {
	return d * T3 + c * T2 + b * T1 + a;
    }

    public static int getId(int a, int b, int c, int d, int e) {
	return e * T4 + d * T3 + c * T2 + b * T1 + a;
    }

    public static int getId(int a, int b, int c, int d, int e, int f) {
       return f * T5 + e * T4 + d * T3 + c * T2 + b * T1 + a;
    }

    public static String byteArrayToHexString(byte[] b) {
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < b.length; i++) {
	    int value = ((int) b[i]) & 0xFF;
	    if (value < 16) {
		sb.append(0);
	    }
	    sb.append(Integer.toHexString(value));
	}
	return sb.toString();
    }

    public int checkResultSet(ResultSet rs, StringBuffer sb, JDRSSQLTypeInfo[] thisTestInfo, int columns, int valuesLimit) throws Exception  {
        int failCount = 0;
	int retrievedCount = 0;
	while (rs.next()) {
	    int divisor = 1; 
	    for (int i = 0; i < columns; i++) {
	      // Determine the read values 
		String[] readValues = thisTestInfo[i].readValues;
		int valuesLength = readValues.length; 
		if (valuesLength > valuesLimit)  {
		  valuesLength = valuesLimit; 
		}
		String expected =  readValues[ (retrievedCount / divisor) % valuesLength];
		divisor = divisor * valuesLength; 

		switch(thisTestInfo[i].accessor) {
		    case GETSTRING: {
			String s = rs.getString(i+1);
			if (s == null) {
			    if (expected != null) {
				failCount++;
				sb.append("["+failCount+"] Mismatch row "+retrievedCount+
				  " column "+i+" expected="+expected+" actual="+s+"\n");
			    }
			} else {
			    if (! s.equals(expected)) {
				failCount++;
				sb.append("["+failCount+"] Mismatch row "+retrievedCount+
				  " column "+i+" expected="+expected+" actual="+s+"\n");
			    }
			}
		    }
			break;
		    case GETBYTES:
			byte[] b = rs.getBytes(i+1);
			if (b == null) {
			    if (expected != null) {
				failCount++;
				sb.append("["+failCount+"] Mismatch row "+retrievedCount+
				  " column "+i+" expected="+expected+" actual=null\n");
			    }
			} else {
			    String b2 = byteArrayToHexString(b);
			    if (! b2.equals(expected)) {
				failCount++;
				sb.append("["+failCount+"] Mismatch row "+retrievedCount+
				  " column "+i+" expected="+expected+" actual="+b2+"\n");
			    }
			}
			break;
        case GETBOOLEAN:
        {

          boolean bool = rs.getBoolean(i + 1);
          String s = null;
          if (!rs.wasNull()) {
            s = "" + bool;
          }

          if (s == null) {
            if (expected != null) {
              failCount++;
              sb.append("[" + failCount + "] Mismatch row " + retrievedCount
                  + " column " + i + " expected=" + expected + " actual=" + s
                  + "\n");
            }
          } else {
            if (!s.equals(expected)) {
              failCount++;
              sb.append("[" + failCount + "] Mismatch row " + retrievedCount
                  + " column " + i + " expected=" + expected + " actual=" + s
                  + "\n");
            }
          }
        }
          break;

		    default:
			failCount++;
			sb.append("["+failCount+"] Undefined accessor "+thisTestInfo[i].accessor+
			  " for type "+thisTestInfo[i].sqlName+"\n");
		}
	    }
	    retrievedCount++;

	} /* while */

	return failCount;
    }

/***
 runTest2()  Run the test, but don't report anything to the test framework
 Returns null if successful, otherwise returns an error message or throws
 an exception.
 
 The valuesLimit is used to limit the number of value (and prevent exponential number)
 of rows when ensureMinimumRows is used. 
 * @param stringBufferLog_ 
***/
  public String runTest2(Connection connection, int columns, int rows, long testId, boolean ensureMinimumRows, int valuesLimit, StringBuffer sb, String[] lastSql) throws Exception {
  
    long originalTestId = testId;
    JDRSSQLTypeInfo thisTestInfo[] = new JDRSSQLTypeInfo[columns];
    String label = "";
    String tablename = JDRSTest.COLLECTION + ".JDRS" + columns + "x" + testId;
    String tableDefinition = "(";
    for (int i = 0; i < columns; i++) {
      int typeId = (int) (testId % typeInfo.length);
      thisTestInfo[i] = typeInfo[typeId];
      label += "-" + thisTestInfo[i].sqlName;
      testId = testId / typeInfo.length;
      if (i > 0) {
        tableDefinition += ",";
      }
      tableDefinition += "c" + i + " " + thisTestInfo[i].sqlName;
    }
    tableDefinition += ")";
    
    if (verbose) {
      System.out.println("Testing " + columns + "." + rows + "."
          + originalTestId + " " + label);
    }
    sb.append("Testing " + columns + "." + rows + "."
        + originalTestId + " " + label+"\n"); 
    Statement statement = connection.createStatement(); 
    //
    // Create the table
    //
    
    initTable(statement, tablename, tableDefinition, sb); 
    
    //
    // Insert values into the table
    //
    boolean printed = false;
    PreparedStatement ps = null;
    String lastPreparedStatement = "";
    
    // Determine the minimum number of rows
    if (ensureMinimumRows) {
      int minimumRows = 1;
      for (int i = 0; i < columns; i++) {
        String[] setValues = thisTestInfo[i].setValues;
        int valuesLength = setValues.length;
        if (valuesLength > valuesLimit) {
          valuesLength = valuesLimit; 
        }
        minimumRows = minimumRows * valuesLength;
      }

      if (rows < minimumRows) {
        rows = minimumRows;
        System.out.println("Setting minimum rows to " + rows);
      }
    }
    int batchCount = 0; 
    for (int j = 0; j < rows; j++) {
      String insertSQL = "INSERT INTO " + tablename + " VALUES (";
      String preparedInsertSQL = "INSERT INTO " + tablename + " VALUES (";
      int divisor = 1; 
      for (int i = 0; i < columns; i++) {
        if (i > 0) {
          insertSQL += ",";
          preparedInsertSQL += ",";
        }
        String[] setValues = thisTestInfo[i].setValues;
        int valuesLength = setValues.length; 
        if (valuesLength > valuesLimit) {
          valuesLength = valuesLimit; 
        }
        insertSQL += setValues[(j / divisor ) % valuesLength];
        divisor = divisor * valuesLength; 
        preparedInsertSQL += "?";
      }
      insertSQL += ")";
      preparedInsertSQL += ")";
      lastSql[0] = insertSQL;
      if (usePreparedStatement) {
        String parameters = "";
        try {
          boolean compatible = true;
          if (!preparedInsertSQL.equals(lastPreparedStatement)) {
            if (ps != null) {
		if (useBatching) {
		    if (batchCount > 0) {
			// System.out.println("Executing batch at count="+batchCount); 
			ps.executeBatch();
			batchCount = 0;
		    }
		} 		
              ps.close();
              ps = null;
            }
            sb.append("preparing3 "+preparedInsertSQL+"\n"); 
            ps = connection.prepareStatement(preparedInsertSQL);
            lastPreparedStatement = preparedInsertSQL;
          }
          divisor = 1; 
          for (int i = 0; i < columns; i++) {
            // Calculate the value to insert into the column 
            String[] setValues = thisTestInfo[i].setValues;
            int valuesLength = setValues.length; 
            if (valuesLength > valuesLimit) { 
              valuesLength = valuesLimit; 
            }
            String value = setValues[(j / divisor ) % valuesLength];
            divisor = divisor * valuesLength;
            
            // Fixup value if needed

            if ((value.charAt(0) == '\'')
                && (value.charAt(value.length() - 1) == '\'')) {
              value = value.substring(1, value.length() - 1);
            }
            if (value.equalsIgnoreCase("NULL")) {
              value = null;
            }
            parameters += value + ",";
            if ((value != null) && (value.indexOf("BLOB(") >= 0)) {
              compatible = false;
            } else if ((value != null ) && ((value.indexOf("X'") >= 0) )) {
              int beginIndex = value.indexOf("X'") + 2; 
              int endIndex = value.indexOf("'", beginIndex); 
              if (endIndex > beginIndex) { 
                String hexString = value.substring(beginIndex, endIndex); 
                byte[] bytes = hexAsStringToBytes(hexString);
                if (ps != null) { 
                   ps.setBytes(i+1, bytes);
                }
              } else {
                compatible=false; 
              }
            } else {
              if (ps != null)
                ps.setString(i + 1, value);
            }
          }
          if (compatible) {

	      if (ps != null) {
		  if (useBatching) {
		      ps.addBatch();
		      batchCount++; 
		      if (batchCount >= 100) {
			  // System.out.println("Executing batch at count="+batchCount); 
			  ps.executeBatch();
			  batchCount = 0; 
		      } 
		  } else { 
		      ps.executeUpdate();
		  }
	      }
          } else {

            if (ps != null) {
		if (useBatching) {
		    if (batchCount > 0) {
			// System.out.println("Executing batch at count="+batchCount); 
			ps.executeBatch();
			batchCount = 0;
		    }
		} 		

              ps.close();
              ps = null;
            }
            sb.append("Preparing4 "+lastSql[0]+"\n"); 
            ps = connection.prepareStatement(lastSql[0]);
            lastPreparedStatement = lastSql[0];
            ps.executeUpdate();

          }

        } catch (Exception e) {
          if (!printed) {
            System.out.println("Statement is " + preparedInsertSQL);
            System.out.println("parameters are " + parameters);
            System.out.flush();
            System.err.flush();
            e.printStackTrace();
            System.out.flush();
            System.err.flush();
            printed = true;

          }
          System.out.flush();
          System.err.flush();

          System.out
              .println("Warning inserting into database.. falling back to using "
                  + lastSql[0]);
          System.out.flush();
          System.err.flush();

          if (ps != null) {
            ps.close();
            ps = null;
          }
          sb.append("Preparing5 "+lastSql[0]+"\n");  
          ps = connection.prepareStatement(lastSql[0]);
          lastPreparedStatement = lastSql[0];
          ps.executeUpdate();

        }
      } else {
	 /* Not prepared statement path */ 
        sb.append("Executing5 "+insertSQL+"\n"); 
        statement.executeUpdate(insertSQL);
      }
      lastSql[0] = "";
    } /* for j < rows */ 
    if (ps != null) {
	if (useBatching) {
	    if (batchCount > 0) {
		// System.out.println("Executing batch at count="+batchCount); 
		ps.executeBatch();
		batchCount = 0;
	    }
	} 		

      ps.close();
      ps = null;
    }

    //
    // Now run the query and validate it.
    //
    int failCount = 0;
    String failMessage = "";
    lastSql[0] = "Select * from " + tablename;
    if (forUpdate) {
      lastSql[0] = lastSql[0] + " FOR UPDATE";
    }
    ResultSet rs;
    PreparedStatement pStmt = null;
    StringBuffer sb1 = new StringBuffer();

    if (usePreparedStatement) {
      sb.append("preparing6 "+lastSql[0]+"\n"); 
      if (useScrollableCursor) {
        pStmt = connection.prepareStatement(lastSql[0],
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      } else {
        pStmt = connection.prepareStatement(lastSql[0]);
      }
      rs = pStmt.executeQuery();
      // Cause it to be run twice
      failCount += checkResultSet(rs, sb1, thisTestInfo, columns, valuesLimit);
      failMessage += sb1.toString();
      rs.close();
      rs = pStmt.executeQuery();

    } else {
      sb.append("Executing6 "+lastSql[0]+"\n"); 
      rs = statement.executeQuery(lastSql[0]);
    }

    failCount += checkResultSet(rs, sb1, thisTestInfo, columns, valuesLimit);
    failMessage += sb1.toString();

    rs.close();
    if (usePreparedStatement) {
      if (pStmt != null)
        pStmt.close();
    }

    try {
      connection.commit();
    } catch (Exception e) {
    }

    if (failCount == 0) {
      if (skipCleanup) {
        System.out.println("Skipping cleanup of " + tablename);
      } else {
        lastSql[0] = "Drop table " + tablename;
        sb.append("Executing7 " + lastSql[0] + "\n");
        if (usePreparedStatement) {
          ps = connection.prepareStatement(lastSql[0]);
          lastPreparedStatement = lastSql[0];
          ps.executeUpdate();
          ps.close();
        } else {
          statement.executeUpdate(lastSql[0]);
        }
      }
    }
    lastSql[0] = "";
    statement.close(); 
    if (failCount == 0) {
      return null;
    } else {
      return failMessage + "\n" + tableDefinition;
    }
  }

/**
 runTest() -  Run a test with the specific number of columns and rows
**/
  public void runTest(int columns, int rows, long testId, boolean ensureMinimumRows, int valuesLimit) {
    if (directMap && getDriver() != JDTestDriver.DRIVER_NATIVE) {
      notApplicable("Native DRIVER direct map test");
      return;
    }
    String[] lastSql = new String[1]; 
    try {
      if (directMap && (getRelease() <= JDTestDriver.RELEASE_V5R3M0)) {
        notApplicable("DirectMap test");
      } else {
        StringBuffer sb = new StringBuffer(); 
        String failMessage = runTest2(connection1_, columns, rows, testId, ensureMinimumRows, valuesLimit, sb, lastSql);
        assertCondition(failMessage == null, failMessage+"\n------------\n" 
        + sb.toString()
            + "\n--- testcase added by native 10/26/2006");
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception ("+e.toString()+") running test " + columns + "," + rows
          + "," + testId + " last SQL = " + lastSql[0]
          + "--- testcase added by native 10/26/2006");
    }
  }

  void runThreadedTest(int columns, int rows, int[] testIds, boolean ensureMinimumRows, int valuesLimit) {
    String[] lastSql = new String[1]; 

    if (directMap && getDriver() != JDTestDriver.DRIVER_NATIVE) {
      notApplicable("Native DRIVER direct map test");
      return;
    }

    try { 
      boolean passed = true; 
      StringBuffer sb = new StringBuffer(); 
      int threadCount = testIds.length; 
      JDRSStressRunnable[] runnables = new JDRSStressRunnable[threadCount];
      Connection[] connections = new Connection[threadCount]; 
      Thread[] thread = new Thread[threadCount]; 
      for (int i = 0; i < threadCount; i++) {
        connections[i] = testDriver_.getConnection (url_,"JDRSStressThread"); 
        runnables[i] = new JDRSStressRunnable(this, connections[i], columns, rows, testIds[i], ensureMinimumRows, valuesLimit);
        thread[i]  = new Thread(runnables[i]); 
        thread[i].start(); 
      }
      for (int i = 0; i < threadCount; i++) {
        runnables[i].setGo(); 
      }      
      for (int i = 0; i < threadCount; i++) {
        runnables[i].waitForDone();
        String result = runnables[i].getResult();
        if (result != null) { 
          passed = false; 
          sb.append("runnable["+i+"] failed with "+result+"\n"); 
        }
        Exception e = runnables[i].getException(); 
        if (e != null) { 
          passed = false; 
          sb.append("runnable["+i+"] failed with execption "+e.toString()+"\n"); 
          printStackTraceToStringBuffer(e, sb); 
          sb.append("Error INFORMATION\n"); 
          sb.append(runnables[i].getErrorInformation()); 
          sb.append("----------------------------------------------\n"); 
        }
         
      }      
      assertCondition(passed, sb);
    } catch (Exception e) {

      failed(e, "Unexpected Exception"+e.toString()+" running  test " + columns + "," + rows
          + "," + /* testId + */  " last SQL = " + lastSql[0]+"\n"
          + "--- testcase added by native 10/26/2006");
    }
  }


  
  
    public void runRandomTests(int columns, int rows, int seconds, boolean ensureMinimumRows, int valuesLimit) {
	if (directMap && getDriver() != JDTestDriver.DRIVER_NATIVE) {
	    notApplicable("Native DRIVER direct map test");
	    return;
	}
	if (directMap && (getRelease() <=  JDTestDriver.RELEASE_V5R3M0)) {
	    notApplicable("DirectMap test");
	} else {
    String[] lastSql = new String[1]; 

	    verbose = true;
	    Random random = new Random();
	    long startTime = System.currentTimeMillis();
	    long endTime = startTime + seconds *1000;
	    long testId = 0;
	    StringBuffer sb = new StringBuffer(); 
      try {
        String failMessage = null;
        while ((failMessage == null) && (System.currentTimeMillis() < endTime)) {
          testId = 0;
          for (int i = 0; i < columns; i++) {
            int r = random.nextInt();
            if (r < 0)
              r = -r;
	    int typeId = r % T_MAXCOUNT;
	    while (typeId >= T_COUNT) {
		r = random.nextInt();
		if (r < 0)
		    r = -r;
		typeId = r % T_MAXCOUNT;
	    }

            testId = testId * T1 + (typeId);
	    sb.append("Testing type: "+typeId+" testID now "+testId+"\n"); 	    
          }

          failMessage = runTest2(connection1_, columns, rows, testId,
              ensureMinimumRows, valuesLimit, sb, lastSql);
        }
        assertCondition(failMessage == null,
            failMessage + "\n-----\n" + sb.toString()
                + "\n--- testcase added by native 10/26/2006");
      } catch (Exception e) {
	// Save a copy of the joblog   
	String joblogInfo = JobLogUtil.publishConnectionJoblog(connection1_); 

        failed(e, "Unexpected Exception("+e.toString()+") running test " + columns + "," + rows
            + "," + testId + " last SQL = " + lastSql[0] + "\n" + joblogInfo+"\n"+sb.toString()
            + "\n--- testcase added by native 10/26/2006");
      }
      verbose = false;
    }
  }

  public void runRandomNonLobTests(int columns, int rows, int seconds,
      boolean ensureMinimumRows, int valuesLimit) {
    if (directMap && getDriver() != JDTestDriver.DRIVER_NATIVE) {
      notApplicable("Native DRIVER direct map test");
      return;
    }
    if (directMap && (getRelease() <= JDTestDriver.RELEASE_V5R3M0)) {
      notApplicable("DirectMap test");
    } else {
      String[] lastSql = new String[1]; 

      verbose = true;
      Random random = new Random();
      long startTime = System.currentTimeMillis();
      long endTime = startTime + seconds * 1000;
      long testId = 0;
      StringBuffer sb = new StringBuffer(); 
      try {
        String failMessage = null;
        while ((failMessage == null) && (System.currentTimeMillis() < endTime)) {
          testId = 0;
          for (int i = 0; i < columns; i++) {
            int r = random.nextInt();
            if (r < 0)
              r = -r;
            r = r % T_MAXCOUNT;
            while (r == T_CLOB || r == T_DBCLOB || r == T_BLOB || r >= T_COUNT) {
              r = random.nextInt();
              if (r < 0)
                r = -r;
              r = r % T_MAXCOUNT;
            }
            testId = testId * T1 + (r % T_MAXCOUNT);
          }
          failMessage = runTest2(connection1_, columns, rows, testId,
              ensureMinimumRows, valuesLimit, sb, lastSql);
        }
        assertCondition(failMessage == null, failMessage
            + "\n------\n"+sb.toString()+"\n--- testcase added by native 10/26/2006");
        
      } catch (Exception e) {
        String joblogInfo = JobLogUtil.publishConnectionJoblog(connection1_);
        failed(e, "Unexpected Exception("+e.toString()+") running test " + columns + "," + rows
            + "," + testId + " last SQL = " + lastSql[0] +"\n"+joblogInfo
            +"\n------\n"+sb.toString()+ "--- testcase added by native 10/26/2006");
      }
      verbose = false;
    }
  }

  public void runAllTests(int columns, int rows, boolean ensureMinimumRows,
      int valuesLimit) {
    if (directMap && getDriver() != JDTestDriver.DRIVER_NATIVE) {
      notApplicable("Native DRIVER direct map test");
      return;
    }
    if (directMap && (getRelease() <= JDTestDriver.RELEASE_V5R3M0)) {
      notApplicable("DirectMap test");
    } else {
      Random random = new Random();
      verbose = true;
      int testId = 0;
      StringBuffer sb = new StringBuffer();
      String[] lastSql = new String[1];

      try {
        String failMessage = null;
        int totalVars = 1;
        for (int i = 0; i < columns; i++) {
          totalVars = totalVars * T1;
        }
        boolean[] testRun = new boolean[totalVars];

        long startTime = System.currentTimeMillis();
        int i = 0;
        int passCount = 0;
        int randomMissCount = 0;
        while ((failMessage == null) && (i < totalVars)
            && (randomMissCount < (totalVars / 10))) {
          randomMissCount = 0;
          testId = -1;
          while (testId == -1 && (randomMissCount < (totalVars / 10))) {
            testId = random.nextInt(totalVars);
            if (testRun[testId] == false) {
              sb.setLength(0);
              failMessage = runTest2(connection1_, columns, rows, testId,
                  ensureMinimumRows, valuesLimit, sb, lastSql);
              testRun[testId] = true;
              passCount++;
            } else {
              randomMissCount++;
              testId = -1;
            }
          }
          i++;
          if (i % 10 == 0) {
            long currentTime = System.currentTimeMillis();
            long passedTime = currentTime - startTime;
            long predictedTime = passedTime * totalVars / passCount;
            java.util.Date endTime = new java.util.Date(
                startTime + predictedTime);
            System.out
                .println("Predicting completion of " + totalVars + " tests in "
                    + (predictedTime - passedTime) + " = " + endTime);
          }
        }
        if (failMessage == null) { /* Do final pass */
          i = 0;
          while ((failMessage == null) && (i < totalVars)) {
            if (testRun[testId] == false) {
              sb.setLength(0);
              failMessage = runTest2(connection1_, columns, rows, i,
                  ensureMinimumRows, valuesLimit, sb, lastSql);
              testRun[testId] = true;
              passCount++;
              if (passCount % 10 == 0) {
                long currentTime = System.currentTimeMillis();
                long passedTime = currentTime - startTime;
                long predictedTime = passedTime * totalVars / passCount;
                java.util.Date endTime = new java.util.Date(
                    startTime + predictedTime);
                System.out.println(
                    "Predicting completion of " + totalVars + " tests in "
                        + (predictedTime - passedTime) + " = " + endTime);
              }

            }
            i++;

          }
        }
        sb.append("--- testcase added by native 10/26/2006\n");
        if (failMessage != null) {
          sb.append(failMessage);
        }
        assertCondition(failMessage == null, sb);
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception(" + e.toString() + " running test " + columns
                + "," + rows + "," + testId + " last SQL = " + lastSql[0]
                + "\n------\n" + sb.toString()
                + "--- testcase added by native 10/26/2006");
      }
      verbose = false;
    }
  }

  /**
These first tests verify that each type is correct
**/
    public void Var001()
    {
	runTest(2, 1000, T1 *0,true,20);
    }

    public void Var002()
    {
	runTest(2, 100, T1 *1,true,20);
    }

    public void Var003()
    {
	runTest(2, 100, T1 *2,true,20);
    }

    public void Var004()
    {
	runTest(2, 100, T1 *3,true,20);
    }

    public void Var005()
    {
	runTest(2, 100, T1 *4,true,20);
    }

    public void Var006()
    {
	runTest(2, 100, T1 *5,true,20);
    }

    public void Var007()
    {
	runTest(2, 100, T1 *6,true,20);
    }

    public void Var008()
    {
	runTest(2, 100, T1 *7,true,20);
    }

    public void Var009()
    {
	runTest(2, 100, T1 *8,true,20);
    }

    public void Var010()
    {
	runTest(2, 105, T1 *9,true,20);
    }

    public void Var011()
    {
	runTest(2, 100, T1 *10,true,20);
    }

    public void Var012()
    {
	runTest(2, 100, T1 *11,true,20);
    }

    public void Var013()
    {
	runTest(2, 100, T1 *12,true,20);
    }

    public void Var014()
    {
	runTest(2, 100, T1 *13,true,20);
    }


    public void Var015()
    {
	runTest(2, 100, T1 *14,true,20);
    }


    public void Var016()
    {
	runTest(2, 100, T1 *15,true,20);
    }


    public void Var017()
    {
	runTest(2, 100, T1 *16,true,20);
    }

    public void Var018()
    {
	runTest(2, 100, T1 *T_CLOB,true,20);
    }

    public void Var019()
    {
	runTest(2, 100, T1 *T_DBCLOB,true,20);
    }

    public void Var020()
    {
	runTest(2, 100, T1 *T_BLOB,true,20);
    }

    public void Var021()
    {
	runTest(2, 100, T1 *T_NUMERIC102,true,20);
    }

    public void Var022()
    {
	runTest(2, 100, T1 *T_NUMERIC105,true,20);
    }


    public void Var023()
    {
	runTest(2, 100, T1 *T_NUMERIC152,true,20);
    }


    public void Var024()
    {
	runTest(2, 110, T1 *T_NUMERIC155,true,20);
    }


    /*
     * Test the 3 integer types
     */
    public void Var025()
    {
	runTest(3, 505, getId(T_SMALLINT,T_INTEGER,T_BIGINT),true,20);
    }


    /**
     * Run random tests of 3 columns, 100 rows for randomRunTime seconds
     */
    public void Var026()
    {
	runRandomTests(3, 100, randomRunTime,false,20);
    }

    /**
     * Run random tests of 4 columns, 100 rows for randomRunTime seconds
     */
    public void Var027()
    {
	runRandomTests(4, 100, randomRunTime,false,20);
    }


    /**
     * Run random tests of 5 columns, 100 rows for randomRunTime seconds
     */
    public void Var028()
    {
	runRandomTests(5, 100, randomRunTime,false,20);
    }

    /**
     * Run random tests of 6 columns, 100 rows for randomRunTime seconds
     */
    public void Var029()
    {
	runRandomTests(6, 100, randomRunTime,false,20);
    }


    /**
     *
     * Test combination            9        23               22           11
     * Testing 4.100.287457 -DECIMAL(15,5)-NUMERIC(15,5)-NUMERIC(10,5)-VARCHAR(40)
     */

    public void Var030()
    {
	runTest(4, 100, getId(T_DECIMAL155, T_NUMERIC155,                       T_NUMERIC105, T_VARCHAR),false,20);
    }

    /**
      * Test combination
      * Failed 2/2/2006 on V5R2 19 17 3 14 
      * Testing 4.100.344481 -BLOB(1000000)-CLOB(1000000)-REAL-DATE
      *
      */
    public void Var031()
    {
	    runTest(4, 100, getId(T_BLOB, T_CLOB, T_REAL, T_DATE    ),false,20);
    }


    public void Var032() {
	String runAllTests4 = System.getProperty("runAllTests4");
	if (runAllTests4 == null) {
	    notApplicable("This will run all 4 tests if runAllTests4 is defined");
	} else {
	    runAllTests(4, 100, true,3);
	}
    }


    //
    // 5,100,1320743; 5,100,15111198;  6,100,11073988 froze on 3/26/2007 when running 159P on v5r4
    //

    public void Var033()
    {                         /* 18 12 15 19 18 */ 
	    runTest(5, 1000, getId(T_DBCLOB, T_GRAPHIC, T_TIME, T_BLOB,T_DBCLOB), false,20); 
    }

    public void Var034()
    {                        /* 15111198 b29 = 23 3 17 10 21 */ 
	    runTest(5, 1000, getId(23,3,17,10,21), false,20);   
    }

    public void Var035()
    {
	                   /* 11073988 base 29 = 19,18,1,19,15,0 */
	    runTest(6, 1000, getId(19,18,1,19,15,0), false,20 );
    }



    /**
     * Run random tests of 4 columns, 100 rows for randomRunTime seconds
     */
    public void Var036()
    {
	if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
	    runRandomNonLobTests(4, 100, randomRunTime,false,20);
	} else {
	    notApplicable("JTOPENLITE TEST");
	}
    }


    /**
     * Run random tests of 5 columns, 100 rows for randomRunTime seconds
     */
    public void Var037()
    {
	if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
	runRandomNonLobTests(5, 100, randomRunTime,false,20);
	} else {
	    notApplicable("JTOPENLITE TEST");
	}
    }

    /**
     * Run random tests of 6 columns, 100 rows for randomRunTime seconds
     */
    public void Var038()
    {
	if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
	    runRandomNonLobTests(6, 100, randomRunTime,false,20);
	} else {
	    notApplicable("JTOPENLITE TEST");
	}
    }




    public void Var039()
    {
	/* variation 3.100.10198 SQL0901 running 7163P on 11/14/2012 */
        /* with export -s MALLOCDEBUG=catch_overflow,align:16,allow_overreading */
	/* 10198 - 19,3,12 */ 
	runTest(3, 100, getId(19,3,12),false,20 );
    }

    public void Var040()
    {
	/* variation  4.100.397308 SQL0901 running 7163P on 11/14/2012 */
        /* with export -s MALLOCDEBUG=catch_overflow,align:16,allow_overreading */
	/* 397308 = 8, 12, 8 , 16 */ 
	runTest(4, 100, getId(8,12,8,16),false,20);

    }


  /* Failure on 11/14/2012 */ 
/* 
Testing 4.100.155107 -TIME-GRAPHIC(40) CCSID 13488 -CHAR(40)-DECIMAL(10,2)
JDRSStressScrollDirectMap  27   FAILED Unexpected Exception running test 4,100,155107 last SQL = Select * from JDT7163P.JDRS4x155107
com.ibm.db2.jdbc.app.DB2DBException: Distributed Relational Database Architecture (DRDA) protocol error.
*/
    /* 155107 = 15,12,10,6 */ 
    public void Var041() {runTest(4, 100, getId(15,12,10,6 ),false,20);    }


    /* Failure on 8/18/2014 on V7R2
 * Testing 5.100.6161203 -DECIMAL(10,5)-INTEGER-DBCLOB(1000000) CCSID 13488 -NUMERIC(10,2)-DECIMAL(10,5)
 * JDRSStressNoBlockPSDirectMap  28   FAILED UXDetail=[1] Mismatch row 42 column 2 expected=Hello actual=UX'00000000000000000000'
 * [1] Mismatch row 42 column 2 expected=Hello actual=UX'00000000000000000000'
 * [1] Mismatch row 42 column 2 expected=Hello actual=UX'00000000000000000000'
     */

    public void Var042()
    {
	/* 6161203 = 8,1,18,20,8 */ 
	runTest(5, 200, getId(8,1,18,20,8),false,20);
    }


    /* Test newly added VARCHAR FBD type */
    public void Var043()
    {
	runTest(2, 1000, getId(0,T_VARCHARFBD),true,20);
    }

    /* Test newly added VARCHAR FBD type */
    public void Var044()
    {
	runTest(2, 1000, getId(T_VARCHARFBD,0),true,20);
    }

    /* Test newly added VARBINARY type */
    public void Var045()
    {
  runTest(2, 1000, getId(0,T_VARBINARY),true,20);
    }

    /* Test newly added VARBINARY type */
    public void Var046()
    {
  runTest(2, 1000, getId(T_VARBINARY,0),true,20);
    }

    /* Test newly added BINARY type */
    public void Var047()
    {
  runTest(2, 1000, getId(0,T_BINARY),true,20);
    }

    /* Test newly added BINARY type */
    public void Var048()
    {
  runTest(2, 1000, getId(T_BINARY,0),true,20);
    }

    
    /**
     * Run random tests of 3 columns, 100 rows for randomRunTime seconds using all row combinations
     */
    public void Var049()
    {
  runRandomTests(3, 100, randomRunTime,true,3);
    }

    /**
     * Run random tests of 4 columns, 100 rows for randomRunTime seconds using all row combinations
     */
    public void Var050()
    {
  runRandomTests(4, 100, randomRunTime,true,3);
    }


    /**
     * Run random tests of 5 columns, 100 rows for randomRunTime seconds using all row combinations
     */
    public void Var051()
    {
      runRandomTests(5, 
          250, randomRunTime,true,3);
    }

    /**
     * Run random tests of 6 columns, 100 rows for randomRunTime seconds using all row combinations
     */
    public void Var052()
    {
  runRandomTests(6, 729, randomRunTime,true,3);
    }


    public void Var053()
    {
	runTest(2, /* columns */ 
	    110,   /* rows */ 
	    T1 *T_DECIMAL314, /* types */ 
	    true,  /* ensure minimum rows */ 
	    20);   /* values limit */ 
    }

    public void Var054() 
    {
      int[] testIds = {
          getId( T_DECIMAL314, T_DECIMAL314), 
          getId( T_DECIMAL155, T_DECIMAL155),
      };
    
      runThreadedTest(2, 20000,  testIds, true, /* ensureMinimumRows*/ 
          5); /* values limit */ 
      
    
    }

    public void Var055() 
    {
      int[] testIds = {
          getId( T_DECIMAL314, T_DECIMAL314), 
          getId( T_DECIMAL155, T_DECIMAL155),
          getId( T_DECIMAL314, T_DECIMAL155),
          getId( T_DECIMAL155, T_DECIMAL314),

      };
    
      runThreadedTest(2, 20000,  testIds, true, /* ensureMinimumRows*/ 
          5); /* values limit */ 
      
    
    }
    public void Var056() 
    {
      int[] testIds = {
          getId( T_DECIMAL102, T_DECIMAL102, T_DECIMAL102), 
          getId( T_DECIMAL102, T_DECIMAL102, T_DECIMAL105), 
          getId( T_DECIMAL102, T_DECIMAL102, T_DECIMAL152), 
          getId( T_DECIMAL102, T_DECIMAL102, T_DECIMAL155), 
          getId( T_DECIMAL102, T_DECIMAL102, T_DECIMAL314), 
          getId( T_DECIMAL105, T_DECIMAL105, T_DECIMAL102), 
          getId( T_DECIMAL105, T_DECIMAL105, T_DECIMAL105), 
          getId( T_DECIMAL105, T_DECIMAL105, T_DECIMAL152), 
          getId( T_DECIMAL105, T_DECIMAL105, T_DECIMAL155), 
          getId( T_DECIMAL105, T_DECIMAL105, T_DECIMAL314), 

      };
    
      runThreadedTest(3, 20000,  testIds, true, /* ensureMinimumRows*/ 
          5); /* values limit */ 
      
    
    }

    public void Var057()
    {
	runTest(2, /* columns */ 
	    110,   /* rows */ 
	    T1 * T_CHAR4, /* types */ 
	    true,  /* ensure minimum rows */ 
	    20);   /* values limit */ 
    }

    public void Var058()
    {
	/* Added to detect CCSID 937 problem with fixed data */ 
	runTest(2, /* columns */ 
	    110,   /* rows */ 
	    T_CHAR4, /* types */ 
	    true,  /* ensure minimum rows */ 
	    20);   /* values limit */ 
    }

    public void Var059()
    {
	runTest(2, /* columns */ 
	    110,   /* rows */ 
	    T1 * T_VARCHAR4, /* types */ 
	    true,  /* ensure minimum rows */ 
	    20);   /* values limit */ 
    }

    public void Var060()
    {
	/* Added to detect CCSID 937 problem with fixed data */ 
	runTest(2, /* columns */ 
	    110,   /* rows */ 
	    T_VARCHAR4, /* types */ 
	    true,  /* ensure minimum rows */ 
	    20);   /* values limit */ 
    }


    /* Test newly added BOOLEAN type */
    public void Var061()
    {
	verbose = true;
	if (checkBooleanSupport()) { 
	    runTest(2, 1000, getId(0,T_BOOLEAN),true,20);
	}
    }

    /* Test newly added BINARY type */
    public void Var062()
    {
	verbose = true; 
	if (checkBooleanSupport()) { 
	    runTest(2, 1000, getId(T_BOOLEAN,0),true,20);
	}
    }



    /* Test 4.100.912175 -SMALLINT-DECIMAL(10,2)-BLOB(1000000)-BOOLEAN  -- Issue 66704*/ 
    public void Var063()
    {
	verbose = true; 
	if (checkBooleanSupport()) { 
	    runTest(4, 100, getId(T_SMALLINT, T_DECIMAL102, T_BLOB, T_BOOLEAN),true,20);
	}
    }

    /* Test 4.100.833390 */ 

    public void Var064()
    {
	verbose = true; 
	if (checkBooleanSupport()) { 
	    runTest(4, 100, 833390,true,20);
	}
    }


    public static void Main(String args) {
	System.out.println("java test.JDRSStress running");

    } 


}



class JDRSSQLTypeInfo {
    public String sqlName;
    public int    accessor;
    public String[] setValues;
    public String[] readValues;
    public JDRSSQLTypeInfo(String sqlName, int accessor, String[] setValues, String[] readValues) {
	this.sqlName = sqlName;
	this.accessor=accessor;
	this.setValues = setValues;
	this.readValues = readValues;
    }
}

