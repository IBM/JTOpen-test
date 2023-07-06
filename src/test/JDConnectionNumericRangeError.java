///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionNumericRangeError.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionNumericRangeError.java
//
// Classes:      JDConnectionNumericRangeError
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;

/**
 * Testcase JDConnectionNumericRangeError. this tests the following of the JDBC
 * Connection class:
 * 
 * Property "Translate Hex"
 **/
public class JDConnectionNumericRangeError extends JDTestcase {

  // Private data.
  private static String schema = JDConnectionTest.COLLECTION;
  private Connection conn_Default;
  private Connection conn_Warning;
  private Connection conn_None;

  /**
   * Constructor.
   **/
  public JDConnectionNumericRangeError(AS400 systemObject,
      Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, "JDConnectionNumericRangeError", namesAndVars, runMode,
        fileOutputStream, password);

  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {

    //
    // reset collection related information after the superclass
    // has had a change to process the arguments
    //

    schema = JDConnectionTest.COLLECTION;
    String urlDefault = baseURL_ +  ";numeric range error=default";
    String urlWarning = baseURL_  + ";numeric range error=warning";

    String urlNone = baseURL_ +  ";numeric range error=none";

    conn_Default = testDriver_.getConnection (urlDefault,systemObject_.getUserId(), encryptedPassword_);
    conn_Warning = testDriver_.getConnection (urlWarning,systemObject_.getUserId(), encryptedPassword_);
    conn_None = testDriver_.getConnection (urlNone,systemObject_.getUserId(), encryptedPassword_);

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    conn_Default.close();
    conn_Warning.close();
    conn_None.close();

  }

  /**
   * Generic testcase
   **/

  public void testRangeError(Connection connection, String query,
      String parameter, String expectedResult, String setExceptionString,
      String executeExceptionString) {
    if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
      notApplicable("Toolbox only test"); 
      return; 
    }
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    try {
      sb.append("Preparing " + query + "\n");
      PreparedStatement ps = connection.prepareStatement(query);
      try {
        sb.append("Calling setString(" + parameter + ")\n");
        ps.setString(1, parameter);
        SQLWarning w = ps.getWarnings();
        sb.append("Got warning " + w+"\n");
        if (setExceptionString != null) {
          if (w == null) {
            sb.append("Expected exception '" + setExceptionString
                + "' but got no warning\n");
            passed = false;
          } else {
            String exceptionString = w.toString();
            if (setExceptionString.indexOf("Warning:") == 0) {
              setExceptionString = setExceptionString.substring(8); 
            }

            if (exceptionString.indexOf(setExceptionString) < 0) {
              sb.append("Got exceptin " + exceptionString + " sb "
                  + setExceptionString + "\n");
              passed = false;
            }
          }
        }
        ps.clearWarnings();
        try {
          sb.append("Running executeQuery\n");
          ResultSet rs = ps.executeQuery();
          rs.next();
          String value = rs.getString(1);
          if (value == null) {
            if (expectedResult != null) {
              sb.append("Got null, should be " + expectedResult + "\n");
              passed = false;
            }
          } else {
            if (!value.equals(expectedResult)) {
              sb.append("getString returned '" + value + "' sb '"
                  + expectedResult + "'\n");
              passed = false; 
            }
          }

        } catch (Exception e) {
          if (executeExceptionString == null) {
            sb.append("Unexpected exception e");
            printStackTraceToStringBuffer(e, sb);
            passed = false;
          } else {
            String exceptionString = e.toString();
            if (executeExceptionString.indexOf("Exception:") == 0) {
              executeExceptionString = executeExceptionString.substring(10); 
            }
            if (exceptionString.indexOf(executeExceptionString) < 0) {
              sb.append("Got exception " + exceptionString + " sb "
                  + executeExceptionString + "\n");
              printStackTraceToStringBuffer(e, sb);
              passed = false;
            }
          }

        }
      } catch (Exception e) {
        if (setExceptionString == null) {
          sb.append("Unexpected exception "+e);
          printStackTraceToStringBuffer(e, sb);
          passed = false;
        } else {
          String exceptionString = e.toString();
          if (setExceptionString.indexOf("Exception:") == 0) {
            setExceptionString = setExceptionString.substring(10); 
          }
          if (exceptionString.indexOf(setExceptionString) < 0) {
            sb.append("Got exception " + exceptionString + " sb "
                + setExceptionString + "\n");
            printStackTraceToStringBuffer(e, sb);
            passed = false;
          }
        }
      }

      assertCondition(passed,sb);
    } catch (SQLException e) {

      failed(e, "Unexpected Exception : " + sb.toString());
    }

  }

  /**
   * Generic testcase
   **/

  public void testResultSetRangeError(
				      Connection connection,
				      String tablename,
				      String datatype,
				      String beginValue,
				      String parameter,
				      String expectedResult,
				      String setExceptionString,
				      String executeExceptionString) {
    if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
      notApplicable("Toolbox only test"); 
      return; 
    }
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    String sql; 
    try {
      Statement s = connection.createStatement(); 
      sql = "DROP TABLE "+tablename;
      try {
	  sb.append("Executing "+sql+"\n"); 
	  s.executeUpdate(sql); 
      } catch (Exception e) {
	  sb.append("Caught exception on drop");
	  printStackTraceToStringBuffer(e, sb); 
      }
      sql = "CREATE TABLE "+tablename+"(C1 "+datatype+")";
      sb.append("Executing "+sql+"\n"); 
      s.executeUpdate(sql); 
      sql = "INSERT INTO "+tablename+" VALUES(?)";
      sb.append("Preparing "+sql+"\n"); 
      PreparedStatement ps = connection.prepareStatement(sql);
      sb.append("Setting column to "+beginValue+"\n");
      ps.setString(1,beginValue);
      ps.execute();

      sql = "select C1 from "+tablename;
      Statement upStmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE); 
      sb.append("Executing "+sql+"\n"); 
      ResultSet rs = upStmt.executeQuery(sql);
      rs.next(); 
      try {
        sb.append("Calling setString(" + parameter + ")\n");
        rs.updateString(1, parameter);
        SQLWarning w = rs.getWarnings();
        sb.append("Got warning " + w+"\n");
        if (setExceptionString != null) {
          if (w == null) {
            sb.append("Expected exception '" + setExceptionString
                + "' but got no warning\n");
            passed = false;
          } else {
            String exceptionString = w.toString();
            if (setExceptionString.indexOf("Warning:") == 0) {
              setExceptionString = setExceptionString.substring(8); 
            }

            if (exceptionString.indexOf(setExceptionString) < 0) {
              sb.append("Got exceptin " + exceptionString + " sb "
                  + setExceptionString + "\n");
              passed = false;
            }
          }
        }
        rs.clearWarnings();
        try {
          sb.append("Running updateRow\n");
	  rs.updateRow();
	  rs.close();
	  upStmt.close(); 
	  sb.append("Executing "+sql+"\n"); 
	  rs = s.executeQuery(sql);
          rs.next();
          String value = rs.getString(1);
          if (value == null) {
            if (expectedResult != null) {
              sb.append("Got null, should be " + expectedResult + "\n");
              passed = false;
            }
          } else {
            if (!value.equals(expectedResult)) {
              sb.append("getString returned '" + value + "' sb '"
                  + expectedResult + "'\n");
              passed = false; 
            }
          }

        } catch (Exception e) {
          if (executeExceptionString == null) {
            sb.append("Unexpected exception e");
            printStackTraceToStringBuffer(e, sb);
            passed = false;
          } else {
            String exceptionString = e.toString();
            if (executeExceptionString.indexOf("Exception:") == 0) {
              executeExceptionString = executeExceptionString.substring(10); 
            }
            if (exceptionString.indexOf(executeExceptionString) < 0) {
              sb.append("Got exception " + exceptionString + " sb "
                  + executeExceptionString + "\n");
              printStackTraceToStringBuffer(e, sb);
              passed = false;
            }
          }

        }
      } catch (Exception e) {
        if (setExceptionString == null) {
          sb.append("Unexpected exception "+e);
          printStackTraceToStringBuffer(e, sb);
          passed = false;
        } else {
          String exceptionString = e.toString();
          if (setExceptionString.indexOf("Exception:") == 0) {
            setExceptionString = setExceptionString.substring(10); 
          }
          if (exceptionString.indexOf(setExceptionString) < 0) {
            sb.append("Got exception " + exceptionString + " sb "
                + setExceptionString + "\n");
            printStackTraceToStringBuffer(e, sb);
            passed = false;
          }
        }
      }

      // Cleanup
      rs.close(); 
      ps.close();
      
      sql = "DROP TABLE "+tablename;
      s.executeUpdate(sql); 
      s.close(); 
      
      assertCondition(passed,sb);
    } catch (SQLException e) {

      failed(e, "Unexpected Exception : " + sb.toString());
    }

  }


  
  public void testRangeErrorBigDecimal(Connection connection, String query,
      String parameter, String expectedResult, String setExceptionString,
      String executeExceptionString) {
    if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
      notApplicable("Toolbox only test"); 
      return; 
    }
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    try {
      sb.append("Preparing " + query + "\n");
      PreparedStatement ps = connection.prepareStatement(query);
      try {
        sb.append("Calling setBigDecimal(" + parameter + ")\n");
        ps.setBigDecimal(1, new BigDecimal(parameter));
        SQLWarning w = ps.getWarnings();
        sb.append("Got warning " + w+"\n");
        if (setExceptionString != null) {
          if (w == null) {
            sb.append("Expected exception '" + setExceptionString
                + "' but got no warning\n");
            passed = false;
          } else {
            String exceptionString = w.toString();
            if (setExceptionString.indexOf("Warning:") == 0) {
              setExceptionString = setExceptionString.substring(8); 
            }

            if (exceptionString.indexOf(setExceptionString) < 0) {
              sb.append("Got exceptin " + exceptionString + " sb "
                  + setExceptionString + "\n");
              passed = false;
            }
          }
        }
        ps.clearWarnings();
        try {
          sb.append("Running executeQuery\n");
          ResultSet rs = ps.executeQuery();
          rs.next();
          String value = rs.getString(1);
          if (value == null) {
            if (expectedResult != null) {
              sb.append("Got null, should be " + expectedResult + "\n");
              passed = false;
            }
          } else {
            if (!value.equals(expectedResult)) {
              sb.append("getString returned '" + value + "' sb '"
                  + expectedResult + "'\n");
              passed = false; 
            }
          }

        } catch (Exception e) {
          if (executeExceptionString == null) {
            sb.append("Unexpected exception e");
            printStackTraceToStringBuffer(e, sb);
            passed = false;
          } else {
            String exceptionString = e.toString();
            if (executeExceptionString.indexOf("Exception:") == 0) {
              executeExceptionString = executeExceptionString.substring(10); 
            }
            if (exceptionString.indexOf(executeExceptionString) < 0) {
              sb.append("Got exception " + exceptionString + " sb "
                  + executeExceptionString + "\n");
              printStackTraceToStringBuffer(e, sb);
              passed = false;
            }
          }

        }
      } catch (Exception e) {
        if (setExceptionString == null) {
          sb.append("Unexpected exception "+e);
          printStackTraceToStringBuffer(e, sb);
          passed = false;
        } else {
          String exceptionString = e.toString();
          if (setExceptionString.indexOf("Exception:") == 0) {
            setExceptionString = setExceptionString.substring(10); 
          }
          if (exceptionString.indexOf(setExceptionString) < 0) {
            sb.append("Got exception " + exceptionString + " sb "
                + setExceptionString + "\n");
            printStackTraceToStringBuffer(e, sb);
            passed = false;
          }
        }
      }

      assertCondition(passed,sb);
    } catch (SQLException e) {

      failed(e, "Unexpected Exception : " + sb.toString());
    }

  }


  
  /**
   * Test default settings
   **/

  
  static final String LARGE_SMALLINT = "32768"; 
  static final String MAX_SMALLINT = "32767"; 
  static final String SMALL_SMALLINT = "-32769"; 
  static final String MIN_SMALLINT = "-32768"; 
  /**
   * default:  fail insert large value into smallint 
   **/
  public void Var001() {
    testRangeError(conn_Default,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         LARGE_SMALLINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into smallint 
   **/
  public void Var002() {
    testRangeError(conn_Warning,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         LARGE_SMALLINT, 
         MAX_SMALLINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into smallint 
   **/
  public void Var003() {
    testRangeError(conn_None,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         LARGE_SMALLINT, 
         MAX_SMALLINT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into smallint 
   **/
  public void Var004() {
    testRangeError(conn_Default,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         SMALL_SMALLINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into smallint 
   **/
  public void Var005() {
    testRangeError(conn_Warning,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         SMALL_SMALLINT, 
         MIN_SMALLINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into smallint 
   **/
  public void Var006() {
    testRangeError(conn_None,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         SMALL_SMALLINT, 
         MIN_SMALLINT,
         null, 
         null);

  }


  static final String LARGE_INTEGER = "2147483648";
  static final String MAX_INTEGER   = "2147483647"; 
  static final String SMALL_INTEGER = "-2147483649"; 
  static final String MIN_INTEGER = "-2147483648"; 
  /**
   * default:  fail insert large value into INTEGER 
   **/
  public void Var007() {
    testRangeError(conn_Default,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         LARGE_INTEGER, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into INTEGER 
   **/
  public void Var008() {
    testRangeError(conn_Warning,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         LARGE_INTEGER, 
         MAX_INTEGER,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into INTEGER 
   **/
  public void Var009() {
    testRangeError(conn_None,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         LARGE_INTEGER, 
         MAX_INTEGER,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into INTEGER 
   **/
  public void Var010() {
    testRangeError(conn_Default,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         SMALL_INTEGER, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into INTEGER 
   **/
  public void Var011() {
    testRangeError(conn_Warning,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         SMALL_INTEGER, 
         MIN_INTEGER,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into INTEGER 
   **/
  public void Var012() {
    testRangeError(conn_None,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         SMALL_INTEGER, 
         MIN_INTEGER,
         null, 
         null);

  }


  static final String LARGE_BIGINT =  "9223372036854775808";
  static final String MAX_BIGINT =    "9223372036854775807";
  static final String SMALL_BIGINT = "-9223372036854775809";
  static final String MIN_BIGINT =   "-9223372036854775808";

  /**
   * default:  fail insert large value into BIGINT 
   **/
  public void Var013() {
    testRangeError(conn_Default,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         LARGE_BIGINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into BIGINT 
   **/
  public void Var014() {
    testRangeError(conn_Warning,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         LARGE_BIGINT, 
         MAX_BIGINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into BIGINT 
   **/
  public void Var015() {
    testRangeError(conn_None,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         LARGE_BIGINT, 
         MAX_BIGINT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into BIGINT 
   **/
  public void Var016() {
    testRangeError(conn_Default,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         SMALL_BIGINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into BIGINT 
   **/
  public void Var017() {
    testRangeError(conn_Warning,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         SMALL_BIGINT, 
         MIN_BIGINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into BIGINT 
   **/
  public void Var018() {
    testRangeError(conn_None,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         SMALL_BIGINT, 
         MIN_BIGINT,
         null, 
         null);

  }

  final static String LARGE_NUMERIC52 = "1234.56";
  final static String MAX_NUMERIC52 = "999.99";
  final static String SMALL_NUMERIC52 = "-1234.56";
  final static String MIN_NUMERIC52 = "-999.99";
  
  /**
   * default:  fail insert large value into NUMERIC(5,2) 
   **/
  public void Var019() {
    testRangeError(conn_Default,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into NUMERIC(5,2) 
   **/
  public void Var020() {
    testRangeError(conn_Warning,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into NUMERIC(5,2) 
   **/
  public void Var021() {
    testRangeError(conn_None,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into NUMERIC(5,2) 
   **/
  public void Var022() {
    testRangeError(conn_Default,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into NUMERIC(5,2) 
   **/
  public void Var023() {
    testRangeError(conn_Warning,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into NUMERIC(5,2) 
   **/
  public void Var024() {
    testRangeError(conn_None,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
         null, 
         null);

  }



  /**
   * default:  fail insert large value into DECIMAL(5,2) 
   **/
  public void Var025() {
    testRangeError(conn_Default,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into DECIMAL(5,2) 
   **/
  public void Var026() {
    testRangeError(conn_Warning,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into DECIMAL(5,2) 
   **/
  public void Var027() {
    testRangeError(conn_None,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into DECIMAL(5,2) 
   **/
  public void Var028() {
    testRangeError(conn_Default,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into DECIMAL(5,2) 
   **/
  public void Var029() {
    testRangeError(conn_Warning,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into DECIMAL(5,2) 
   **/
  public void Var030() {
    testRangeError(conn_None,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
         null, 
         null);

  }

  final static String LARGE_DECFLOAT16 = "1.0E385"; 
  final static String MAX_DECFLOAT16 = "9.999999999999999E+384"; 
  final static String SMALL_DECFLOAT16 = "-1.0E385"; 
  final static String MIN_DECFLOAT16 = "-9.999999999999999E+384"; 
  /**
   * default:  fail insert large value into decfloat(16) 
   **/
  public void Var031() {
    testRangeError(conn_Default,
        "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
         LARGE_DECFLOAT16, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into decfloat(16) 
   **/
  public void Var032() {
      if (checkJdbc40()) { 
	  testRangeError(conn_Warning,
			 "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
			 LARGE_DECFLOAT16, 
			 MAX_DECFLOAT16,
			 "Warning:Data type mismatch", 
			 null);
      }

  }

  /**
   * none:  no warning insert large value into decfloat(16) 
   **/
  public void Var033() {
      if (checkJdbc40()) { 
	  testRangeError(conn_None,
			 "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
			 LARGE_DECFLOAT16, 
			 MAX_DECFLOAT16,
			 null, 
			 null);
      }

  }


  /**
   * default:  fail insert small value into decfloat(16) 
   **/
  public void Var034() {
    testRangeError(conn_Default,
        "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
         SMALL_DECFLOAT16, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into decfloat(16) 
   **/
  public void Var035() {
      if (checkJdbc40()) { 

	  testRangeError(conn_Warning,
			 "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
			 SMALL_DECFLOAT16, 
			 MIN_DECFLOAT16,
			 "Warning:Data type mismatch", 
			 null);
      }

  }

  /**
   * none:  no warning insert small value into decfloat(16) 
   **/
  public void Var036() {
      if (checkJdbc40()) { 

	  testRangeError(conn_None,
			 "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
			 SMALL_DECFLOAT16, 
			 MIN_DECFLOAT16,
			 null, 
			 null);
      }

  }

  static final String LARGE_DECFLOAT34 = "1.0E6145"; 
  static final String MAX_DECFLOAT34 = "9.999999999999999999999999999999999E+6144"; 
  static final String SMALL_DECFLOAT34 = "-1.0E6145"; 
  static final String MIN_DECFLOAT34 = "-9.999999999999999999999999999999999E+6144"; 
  /**
   * default:  fail insert large value into decfloat(34) 
   **/
  public void Var037() {
    testRangeError(conn_Default,
        "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
         LARGE_DECFLOAT34, 
          null,
        "Exception:Data type mismatch", 
        null);
  }
 
  /**
   * warning:  warning insert large value into decfloat(34) 
   **/
  public void Var038() {
      if (checkJdbc40()) { 

	  testRangeError(conn_Warning,
			 "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
			 LARGE_DECFLOAT34, 
			 MAX_DECFLOAT34,
			 "Warning:Data type mismatch", 
			 null);

      }
  }

  /**
   * none:  no warning insert large value into decfloat(34) 
   **/
  public void Var039() {
      if (checkJdbc40()) { 

	  testRangeError(conn_None,
			 "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
			 LARGE_DECFLOAT34, 
			 MAX_DECFLOAT34,
			 null, 
			 null);
      }

  }


  /**
   * default:  fail insert small value into decfloat(34) 
   **/
  public void Var040() {
    testRangeError(conn_Default,
        "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
         SMALL_DECFLOAT34, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into decfloat(34) 
   **/
  public void Var041() {
      if (checkJdbc40()) { 

	  testRangeError(conn_Warning,
			 "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
			 SMALL_DECFLOAT34, 
			 MIN_DECFLOAT34,
			 "Warning:Data type mismatch", 
			 null);
      }

  }

  /**
   * none:  no warning insert small value into decfloat(34) 
   **/
  public void Var042() {
      if (checkJdbc40()) { 

	  testRangeError(conn_None,
			 "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
			 SMALL_DECFLOAT34, 
			 MIN_DECFLOAT34,
			 null, 
			 null);
      }

  }

  
  final static String LARGE_REAL = "3.41e38";
  final static String MAX_REAL = "3.4028235E38";
  final static String SMALL_REAL = "-3.5e38";
  final static String MIN_REAL = "-3.4028235E38"; 
  /**
   * default:  fail insert large value into REAL 
   **/
  public void Var043() {
    testRangeError(conn_Default,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         LARGE_REAL, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into REAL 
   **/
  public void Var044() {
    testRangeError(conn_Warning,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         LARGE_REAL, 
         MAX_REAL,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into REAL 
   **/
  public void Var045() {
    testRangeError(conn_None,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         LARGE_REAL, 
         MAX_REAL,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into REAL 
   **/
  public void Var046() {
    testRangeError(conn_Default,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         SMALL_REAL, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into REAL 
   **/
  public void Var047() {
    testRangeError(conn_Warning,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         SMALL_REAL, 
         MIN_REAL,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into REAL 
   **/
  public void Var048() {
    testRangeError(conn_None,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         SMALL_REAL, 
         MIN_REAL,
         null, 
         null);

  }


  
  
  final static String LARGE_FLOAT = "1.8e308";
  final static String MAX_FLOAT = "1.7976931348623157E308";
  final static String SMALL_FLOAT = "-1.8e308";
  final static String MIN_FLOAT = "-1.7976931348623157E308"; 
  /**
   * default:  fail insert large value into FLOAT 
   **/
  public void Var049() {
    testRangeError(conn_Default,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         LARGE_FLOAT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into FLOAT 
   **/
  public void Var050() {
    testRangeError(conn_Warning,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         LARGE_FLOAT, 
         MAX_FLOAT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into FLOAT 
   **/
  public void Var051() {
    testRangeError(conn_None,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         LARGE_FLOAT, 
         MAX_FLOAT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into FLOAT 
   **/
  public void Var052() {
    testRangeError(conn_Default,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         SMALL_FLOAT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into FLOAT 
   **/
  public void Var053() {
    testRangeError(conn_Warning,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         SMALL_FLOAT, 
         MIN_FLOAT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into FLOAT 
   **/
  public void Var054() {
    testRangeError(conn_None,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         SMALL_FLOAT, 
         MIN_FLOAT,
         null, 
         null);

  }


  
  final static String LARGE_DOUBLE = "1.8e308";
  final static String MAX_DOUBLE = "1.7976931348623157E308";
  final static String SMALL_DOUBLE = "-1.8e308";
  final static String MIN_DOUBLE = "-1.7976931348623157E308"; 
  /**
   * default:  fail insert large value into DOUBLE 
   **/
  public void Var055() {
    testRangeError(conn_Default,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         LARGE_DOUBLE, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into DOUBLE 
   **/
  public void Var056() {
    testRangeError(conn_Warning,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         LARGE_DOUBLE, 
         MAX_DOUBLE,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into DOUBLE 
   **/
  public void Var057() {
    testRangeError(conn_None,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         LARGE_DOUBLE, 
         MAX_DOUBLE,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into DOUBLE 
   **/
  public void Var058() {
    testRangeError(conn_Default,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         SMALL_DOUBLE, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into DOUBLE 
   **/
  public void Var059() {
    testRangeError(conn_Warning,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         SMALL_DOUBLE, 
         MIN_DOUBLE,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into DOUBLE 
   **/
  public void Var060() {
    testRangeError(conn_None,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         SMALL_DOUBLE, 
         MIN_DOUBLE,
         null, 
         null);

  }


  static final String LARGE_EXP_SMALLINT = "3.2768E4"; 
  static final String SMALL_EXP_SMALLINT = "-3.2769E4"; 
  /**
   * default:  fail insert large value into smallint 
   **/
  public void Var061() {
    testRangeError(conn_Default,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         LARGE_EXP_SMALLINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into smallint 
   **/
  public void Var062() {
    testRangeError(conn_Warning,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         LARGE_EXP_SMALLINT, 
         MAX_SMALLINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into smallint 
   **/
  public void Var063() {
    testRangeError(conn_None,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         LARGE_EXP_SMALLINT, 
         MAX_SMALLINT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into smallint 
   **/
  public void Var064() {
    testRangeError(conn_Default,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         SMALL_EXP_SMALLINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into smallint 
   **/
  public void Var065() {
    testRangeError(conn_Warning,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         SMALL_EXP_SMALLINT, 
         MIN_SMALLINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into smallint 
   **/
  public void Var066() {
    testRangeError(conn_None,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         SMALL_EXP_SMALLINT, 
         MIN_SMALLINT,
         null, 
         null);

  }


  static final String LARGE_EXP_INTEGER = "2.147483648E9";
  static final String SMALL_EXP_INTEGER = "-2.147483649E9"; 
  /**
   * default:  fail insert large value into INTEGER 
   **/
  public void Var067() {
    testRangeError(conn_Default,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         LARGE_EXP_INTEGER, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into INTEGER 
   **/
  public void Var068() {
    testRangeError(conn_Warning,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         LARGE_EXP_INTEGER, 
         MAX_INTEGER,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into INTEGER 
   **/
  public void Var069() {
    testRangeError(conn_None,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         LARGE_EXP_INTEGER, 
         MAX_INTEGER,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into INTEGER 
   **/
  public void Var070() {
    testRangeError(conn_Default,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         SMALL_EXP_INTEGER, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into INTEGER 
   **/
  public void Var071() {
    testRangeError(conn_Warning,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         SMALL_EXP_INTEGER, 
         MIN_INTEGER,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into INTEGER 
   **/
  public void Var072() {
    testRangeError(conn_None,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         SMALL_EXP_INTEGER, 
         MIN_INTEGER,
         null, 
         null);

  }


  static final String LARGE_EXP_BIGINT =  "9.223372036854775808E18";
  static final String SMALL_EXP_BIGINT = "-9.223372036854775809E18";

  /**
   * default:  fail insert large value into BIGINT 
   **/
  public void Var073() {
    testRangeError(conn_Default,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         LARGE_EXP_BIGINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into BIGINT 
   **/
  public void Var074() {
    testRangeError(conn_Warning,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         LARGE_EXP_BIGINT, 
         MAX_BIGINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into BIGINT 
   **/
  public void Var075() {
    testRangeError(conn_None,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         LARGE_EXP_BIGINT, 
         MAX_BIGINT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into BIGINT 
   **/
  public void Var076() {
    testRangeError(conn_Default,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         SMALL_EXP_BIGINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into BIGINT 
   **/
  public void Var077() {
    testRangeError(conn_Warning,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         SMALL_EXP_BIGINT, 
         MIN_BIGINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into BIGINT 
   **/
  public void Var078() {
    testRangeError(conn_None,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         SMALL_EXP_BIGINT, 
         MIN_BIGINT,
         null, 
         null);

  }

  final static String LARGE_EXP_NUMERIC = "123456E-2";
  final static String SMALL_EXP_NUMERIC = "-123456E-2";
  
  /**
   * default:  fail insert large value into NUMERIC(5,2) 
   **/
  public void Var079() {
    testRangeError(conn_Default,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         LARGE_EXP_NUMERIC, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into NUMERIC(5,2) 
   **/
  public void Var080() {
    testRangeError(conn_Warning,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         LARGE_EXP_NUMERIC, 
         MAX_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into NUMERIC(5,2) 
   **/
  public void Var081() {
    testRangeError(conn_None,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         LARGE_EXP_NUMERIC, 
         MAX_NUMERIC52,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into NUMERIC(5,2) 
   **/
  public void Var082() {
    testRangeError(conn_Default,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         SMALL_EXP_NUMERIC, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into NUMERIC(5,2) 
   **/
  public void Var083() {
    testRangeError(conn_Warning,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         SMALL_EXP_NUMERIC, 
         MIN_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into NUMERIC(5,2) 
   **/
  public void Var084() {
    testRangeError(conn_None,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         SMALL_EXP_NUMERIC, 
         MIN_NUMERIC52,
         null, 
         null);

  }



  /**
   * default:  fail insert large value into DECIMAL(5,2) 
   **/
  public void Var085() {
    testRangeError(conn_Default,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         LARGE_EXP_NUMERIC, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into DECIMAL(5,2) 
   **/
  public void Var086() {
    testRangeError(conn_Warning,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         LARGE_EXP_NUMERIC, 
         MAX_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into DECIMAL(5,2) 
   **/
  public void Var087() {
    testRangeError(conn_None,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         LARGE_EXP_NUMERIC, 
         MAX_NUMERIC52,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into DECIMAL(5,2) 
   **/
  public void Var088() {
    testRangeError(conn_Default,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         SMALL_EXP_NUMERIC, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into DECIMAL(5,2) 
   **/
  public void Var089() {
    testRangeError(conn_Warning,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         SMALL_EXP_NUMERIC, 
         MIN_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into DECIMAL(5,2) 
   **/
  public void Var090() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         SMALL_EXP_NUMERIC, 
         MIN_NUMERIC52,
         null, 
         null);

  }

  /**
   * default:  fail insert large value into smallint 
   **/
  public void Var091() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         LARGE_SMALLINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into smallint 
   **/
  public void Var092() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         LARGE_SMALLINT, 
         MAX_SMALLINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into smallint 
   **/
  public void Var093() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         LARGE_SMALLINT, 
         MAX_SMALLINT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into smallint 
   **/
  public void Var094() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         SMALL_SMALLINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into smallint 
   **/
  public void Var095() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         SMALL_SMALLINT, 
         MIN_SMALLINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into smallint 
   **/
  public void Var096() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as SMALLINT) from sysibm.sysdummy1", 
         SMALL_SMALLINT, 
         MIN_SMALLINT,
         null, 
         null);

  }


  /**
   * default:  fail insert large value into INTEGER 
   **/
  public void Var097() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         LARGE_INTEGER, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into INTEGER 
   **/
  public void Var098() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         LARGE_INTEGER, 
         MAX_INTEGER,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into INTEGER 
   **/
  public void Var099() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         LARGE_INTEGER, 
         MAX_INTEGER,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into INTEGER 
   **/
  public void Var100() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         SMALL_INTEGER, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into INTEGER 
   **/
  public void Var101() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         SMALL_INTEGER, 
         MIN_INTEGER,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into INTEGER 
   **/
  public void Var102() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as INTEGER) from sysibm.sysdummy1", 
         SMALL_INTEGER, 
         MIN_INTEGER,
         null, 
         null);

  }



  /**
   * default:  fail insert large value into BIGINT 
   **/
  public void Var103() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         LARGE_BIGINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into BIGINT 
   **/
  public void Var104() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         LARGE_BIGINT, 
         MAX_BIGINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into BIGINT 
   **/
  public void Var105() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         LARGE_BIGINT, 
         MAX_BIGINT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into BIGINT 
   **/
  public void Var106() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         SMALL_BIGINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into BIGINT 
   **/
  public void Var107() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         SMALL_BIGINT, 
         MIN_BIGINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into BIGINT 
   **/
  public void Var108() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as BIGINT) from sysibm.sysdummy1", 
         SMALL_BIGINT, 
         MIN_BIGINT,
         null, 
         null);

  }

  
  /**
   * default:  fail insert large value into NUMERIC(5,2) 
   **/
  public void Var109() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into NUMERIC(5,2) 
   **/
  public void Var110() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into NUMERIC(5,2) 
   **/
  public void Var111() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into NUMERIC(5,2) 
   **/
  public void Var112() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into NUMERIC(5,2) 
   **/
  public void Var113() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into NUMERIC(5,2) 
   **/
  public void Var114() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as NUMERIC(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
         null, 
         null);

  }



  /**
   * default:  fail insert large value into DECIMAL(5,2) 
   **/
  public void Var115() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into DECIMAL(5,2) 
   **/
  public void Var116() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into DECIMAL(5,2) 
   **/
  public void Var117() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into DECIMAL(5,2) 
   **/
  public void Var118() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into DECIMAL(5,2) 
   **/
  public void Var119() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into DECIMAL(5,2) 
   **/
  public void Var120() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as DECIMAL(5,2)) from sysibm.sysdummy1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
         null, 
         null);

  }

  /**
   * default:  fail insert large value into decfloat(16) 
   **/
  public void Var121() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
         LARGE_DECFLOAT16, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into decfloat(16) 
   **/
  public void Var122() {
      if (checkJdbc40()) { 

	  testRangeErrorBigDecimal(conn_Warning,
				   "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
				   LARGE_DECFLOAT16, 
				   MAX_DECFLOAT16,
				   "Warning:Data type mismatch", 
				   null);
      }

  }

  /**
   * none:  no warning insert large value into decfloat(16) 
   **/
  public void Var123() {
      if (checkJdbc40()) { 

	  testRangeErrorBigDecimal(conn_None,
				   "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
				   LARGE_DECFLOAT16, 
				   MAX_DECFLOAT16,
				   null, 
				   null);
      }
  }


  /**
   * default:  fail insert small value into decfloat(16) 
   **/
  public void Var124() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
         SMALL_DECFLOAT16, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into decfloat(16) 
   **/
  public void Var125() {
      if (checkJdbc40()) { 

	  testRangeErrorBigDecimal(conn_Warning,
				   "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
				   SMALL_DECFLOAT16, 
				   MIN_DECFLOAT16,
				   "Warning:Data type mismatch", 
				   null);
      }

  }

  /**
   * none:  no warning insert small value into decfloat(16) 
   **/
  public void Var126() {
      if (checkJdbc40()) { 

	  testRangeErrorBigDecimal(conn_None,
				   "select cast(? as decfloat(16)) from sysibm.sysdummy1", 
				   SMALL_DECFLOAT16, 
				   MIN_DECFLOAT16,
				   null, 
				   null);
      }

  }

  /**
   * default:  fail insert large value into decfloat(34) 
   **/
  public void Var127() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
         LARGE_DECFLOAT34, 
          null,
        "Exception:Data type mismatch", 
        null);
  }
 
  /**
   * warning:  warning insert large value into decfloat(34) 
   **/
  public void Var128() {
      if (checkJdbc40()) { 

	  testRangeErrorBigDecimal(conn_Warning,
				   "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
				   LARGE_DECFLOAT34, 
				   MAX_DECFLOAT34,
				   "Warning:Data type mismatch", 
				   null);
      }

  }

  /**
   * none:  no warning insert large value into decfloat(34) 
   **/
  public void Var129() {
      if (checkJdbc40()) { 

	  testRangeErrorBigDecimal(conn_None,
				   "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
				   LARGE_DECFLOAT34, 
				   MAX_DECFLOAT34,
				   null, 
				   null);
      }

  }


  /**
   * default:  fail insert small value into decfloat(34) 
   **/
  public void Var130() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
         SMALL_DECFLOAT34, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into decfloat(34) 
   **/
  public void Var131() {
      if (checkJdbc40()) { 

	  testRangeErrorBigDecimal(conn_Warning,
				   "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
				   SMALL_DECFLOAT34, 
				   MIN_DECFLOAT34,
				   "Warning:Data type mismatch", 
				   null);
      }

  }

  /**
   * none:  no warning insert small value into decfloat(34) 
   **/
  public void Var132() {
      if (checkJdbc40()) { 

	  testRangeErrorBigDecimal(conn_None,
				   "select cast(? as decfloat(34)) from sysibm.sysdummy1", 
				   SMALL_DECFLOAT34, 
				   MIN_DECFLOAT34,
				   null, 
				   null);
      }

  }

  
  /**
   * default:  fail insert large value into REAL 
   **/
  public void Var133() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         LARGE_REAL, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into REAL 
   **/
  public void Var134() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         LARGE_REAL, 
         MAX_REAL,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into REAL 
   **/
  public void Var135() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         LARGE_REAL, 
         MAX_REAL,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into REAL 
   **/
  public void Var136() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         SMALL_REAL, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into REAL 
   **/
  public void Var137() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         SMALL_REAL, 
         MIN_REAL,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into REAL 
   **/
  public void Var138() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as REAL) from sysibm.sysdummy1", 
         SMALL_REAL, 
         MIN_REAL,
         null, 
         null);

  }


  
  
  /**
   * default:  fail insert large value into FLOAT 
   **/
  public void Var139() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         LARGE_FLOAT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into FLOAT 
   **/
  public void Var140() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         LARGE_FLOAT, 
         MAX_FLOAT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into FLOAT 
   **/
  public void Var141() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         LARGE_FLOAT, 
         MAX_FLOAT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into FLOAT 
   **/
  public void Var142() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         SMALL_FLOAT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into FLOAT 
   **/
  public void Var143() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         SMALL_FLOAT, 
         MIN_FLOAT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into FLOAT 
   **/
  public void Var144() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as FLOAT) from sysibm.sysdummy1", 
         SMALL_FLOAT, 
         MIN_FLOAT,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large value into DOUBLE 
   **/
  public void Var145() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         LARGE_DOUBLE, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into DOUBLE 
   **/
  public void Var146() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         LARGE_DOUBLE, 
         MAX_DOUBLE,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into DOUBLE 
   **/
  public void Var147() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         LARGE_DOUBLE, 
         MAX_DOUBLE,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into DOUBLE 
   **/
  public void Var148() {
    testRangeErrorBigDecimal(conn_Default,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         SMALL_DOUBLE, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into DOUBLE 
   **/
  public void Var149() {
    testRangeErrorBigDecimal(conn_Warning,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         SMALL_DOUBLE, 
         MIN_DOUBLE,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into DOUBLE 
   **/
  public void Var150() {
    testRangeErrorBigDecimal(conn_None,
        "select cast(? as DOUBLE) from sysibm.sysdummy1", 
         SMALL_DOUBLE, 
         MIN_DOUBLE,
         null, 
         null);

  }


  /**
   * Check various combinations of NUMERIC/DECIMAL
   */
  final static String LARGE_NUMERIC52170 = "123456789012345678";
  final static String MAX_NUMERIC52170 = "99999999999999999";
  final static String SMALL_NUMERIC52170 = "-123456789012345678";
  final static String MIN_NUMERIC52170 = "-99999999999999999";;
  final static String DECLARE_NUMERIC170 = "17,0"; 

  public void Var151() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC170 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52170,
        null, "Exception:Data type mismatch", null);
  }

  public void Var152() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC170 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52170,
        MAX_NUMERIC52170, "Warning:Data type mismatch", null);
  }

  public void Var153() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC170
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52170, MAX_NUMERIC52170, null,
        null);
  }

  public void Var154() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC170 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52170,
        null, "Exception:Data type mismatch", null);
  }

  public void Var155() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC170 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52170,
        MIN_NUMERIC52170, "Warning:Data type mismatch", null);
  }

  public void Var156() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC170
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52170, MIN_NUMERIC52170, null,
        null);
  }

  public void Var157() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC170 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52170,
        null, "Exception:Data type mismatch", null);
  }

  public void Var158() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC170 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52170,
        MAX_NUMERIC52170, "Warning:Data type mismatch", null);
  }

  public void Var159() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC170
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52170, MAX_NUMERIC52170, null,
        null);
  }

  public void Var160() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC170 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52170,
        null, "Exception:Data type mismatch", null);
  }

  public void Var161() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC170 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52170,
        MIN_NUMERIC52170, "Warning:Data type mismatch", null);
  }

  public void Var162() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC170
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52170, MIN_NUMERIC52170, null,
        null);
  }

  
  final static String LARGE_NUMERIC52172 = "1234567890123456.78";
  final static String MAX_NUMERIC52172 = "999999999999999.99";
  final static String SMALL_NUMERIC52172 = "-1234567890123456.78";
  final static String MIN_NUMERIC52172 = "-999999999999999.99";;
  final static String DECLARE_NUMERIC172 = "17,2"; 

  public void Var163() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC172 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52172,
        null, "Exception:Data type mismatch", null);
  }

  public void Var164() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC172 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52172,
        MAX_NUMERIC52172, "Warning:Data type mismatch", null);
  }

  public void Var165() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC172
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52172, MAX_NUMERIC52172, null,
        null);
  }

  public void Var166() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC172 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52172,
        null, "Exception:Data type mismatch", null);
  }

  public void Var167() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC172 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52172,
        MIN_NUMERIC52172, "Warning:Data type mismatch", null);
  }

  public void Var168() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC172
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52172, MIN_NUMERIC52172, null,
        null);
  }

  public void Var169() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC172 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52172,
        null, "Exception:Data type mismatch", null);
  }

  public void Var170() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC172 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52172,
        MAX_NUMERIC52172, "Warning:Data type mismatch", null);
  }

  public void Var171() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC172
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52172, MAX_NUMERIC52172, null,
        null);
  }

  public void Var172() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC172 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52172,
        null, "Exception:Data type mismatch", null);
  }

  public void Var173() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC172 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52172,
        MIN_NUMERIC52172, "Warning:Data type mismatch", null);
  }

  public void Var174() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC172
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52172, MIN_NUMERIC52172, null,
        null);
  }

  
  final static String LARGE_NUMERIC521717 = "1.23456789012345678";
  final static String MAX_NUMERIC521717 = "0.99999999999999999";
  final static String SMALL_NUMERIC521717 = "-1.23456789012345678";
  final static String MIN_NUMERIC521717 = "-0.99999999999999999";;
  final static String DECLARE_NUMERIC1717 = "17,17"; 

  public void Var175() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1717 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521717,
        null, "Exception:Data type mismatch", null);
  }

  public void Var176() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1717 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521717,
        MAX_NUMERIC521717, "Warning:Data type mismatch", null);
  }

  public void Var177() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC1717
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC521717, MAX_NUMERIC521717, null,
        null);
  }

  public void Var178() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1717 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521717,
        null, "Exception:Data type mismatch", null);
  }

  public void Var179() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1717 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521717,
        MIN_NUMERIC521717, "Warning:Data type mismatch", null);
  }

  public void Var180() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC1717
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC521717, MIN_NUMERIC521717, null,
        null);
  }

  public void Var181() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1717 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521717,
        null, "Exception:Data type mismatch", null);
  }

  public void Var182() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1717 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521717,
        MAX_NUMERIC521717, "Warning:Data type mismatch", null);
  }

  public void Var183() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC1717
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC521717, MAX_NUMERIC521717, null,
        null);
  }

  public void Var184() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1717 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521717,
        null, "Exception:Data type mismatch", null);
  }

  public void Var185() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1717 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521717,
        MIN_NUMERIC521717, "Warning:Data type mismatch", null);
  }

  public void Var186() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC1717
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC521717, MIN_NUMERIC521717, null,
        null);
  }

  
  
  /**
   * Check various combinations of NUMERIC/DECIMAL
   */
  final static String LARGE_NUMERIC52180 = "1234567890123456789";
  final static String MAX_NUMERIC52180 = "999999999999999999";
  final static String SMALL_NUMERIC52180 = "-1234567890123456789";
  final static String MIN_NUMERIC52180 = "-999999999999999999";;
  final static String DECLARE_NUMERIC180 = "18,0"; 

  public void Var187() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC180 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52180,
        null, "Exception:Data type mismatch", null);
  }

  public void Var188() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC180 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52180,
        MAX_NUMERIC52180, "Warning:Data type mismatch", null);
  }

  public void Var189() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC180
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52180, MAX_NUMERIC52180, null,
        null);
  }

  public void Var190() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC180 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52180,
        null, "Exception:Data type mismatch", null);
  }

  public void Var191() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC180 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52180,
        MIN_NUMERIC52180, "Warning:Data type mismatch", null);
  }

  public void Var192() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC180
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52180, MIN_NUMERIC52180, null,
        null);
  }

  public void Var193() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC180 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52180,
        null, "Exception:Data type mismatch", null);
  }

  public void Var194() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC180 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52180,
        MAX_NUMERIC52180, "Warning:Data type mismatch", null);
  }

  public void Var195() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC180
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52180, MAX_NUMERIC52180, null,
        null);
  }

  public void Var196() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC180 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52180,
        null, "Exception:Data type mismatch", null);
  }

  public void Var197() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC180 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52180,
        MIN_NUMERIC52180, "Warning:Data type mismatch", null);
  }

  public void Var198() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC180
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52180, MIN_NUMERIC52180, null,
        null);
  }

  
  final static String LARGE_NUMERIC52182 = "12345678901293456.78";
  final static String MAX_NUMERIC52182 = "9999999999999999.99";
  final static String SMALL_NUMERIC52182 = "-12345678901293456.78";
  final static String MIN_NUMERIC52182 = "-9999999999999999.99";;
  final static String DECLARE_NUMERIC182 = "18,2"; 

  public void Var199() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC182 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52182,
        null, "Exception:Data type mismatch", null);
  }

  public void Var200() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC182 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52182,
        MAX_NUMERIC52182, "Warning:Data type mismatch", null);
  }

  public void Var201() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC182
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52182, MAX_NUMERIC52182, null,
        null);
  }

  public void Var202() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC182 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52182,
        null, "Exception:Data type mismatch", null);
  }

  public void Var203() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC182 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52182,
        MIN_NUMERIC52182, "Warning:Data type mismatch", null);
  }

  public void Var204() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC182
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52182, MIN_NUMERIC52182, null,
        null);
  }

  public void Var205() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC182 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52182,
        null, "Exception:Data type mismatch", null);
  }

  public void Var206() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC182 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52182,
        MAX_NUMERIC52182, "Warning:Data type mismatch", null);
  }

  public void Var207() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC182
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52182, MAX_NUMERIC52182, null,
        null);
  }

  public void Var208() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC182 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52182,
        null, "Exception:Data type mismatch", null);
  }

  public void Var209() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC182 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52182,
        MIN_NUMERIC52182, "Warning:Data type mismatch", null);
  }

  public void Var210() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC182
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52182, MIN_NUMERIC52182, null,
        null);
  }

  
  final static String LARGE_NUMERIC521818 = "1.234567890123456789";
  final static String MAX_NUMERIC521818 = "0.999999999999999999";
  final static String SMALL_NUMERIC521818 = "-1.234567890123456789";
  final static String MIN_NUMERIC521818 = "-0.999999999999999999";;
  final static String DECLARE_NUMERIC1818 = "18,18"; 

  public void Var211() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1818 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521818,
        null, "Exception:Data type mismatch", null);
  }

  public void Var212() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1818 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521818,
        MAX_NUMERIC521818, "Warning:Data type mismatch", null);
  }

  public void Var213() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC1818
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC521818, MAX_NUMERIC521818, null,
        null);
  }

  public void Var214() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1818 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521818,
        null, "Exception:Data type mismatch", null);
  }

  public void Var215() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1818 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521818,
        MIN_NUMERIC521818, "Warning:Data type mismatch", null);
  }

  public void Var216() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC1818
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC521818, MIN_NUMERIC521818, null,
        null);
  }

  public void Var217() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1818 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521818,
        null, "Exception:Data type mismatch", null);
  }

  public void Var218() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1818 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521818,
        MAX_NUMERIC521818, "Warning:Data type mismatch", null);
  }

  public void Var219() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC1818
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC521818, MAX_NUMERIC521818, null,
        null);
  }

  public void Var220() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1818 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521818,
        null, "Exception:Data type mismatch", null);
  }

  public void Var221() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1818 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521818,
        MIN_NUMERIC521818, "Warning:Data type mismatch", null);
  }

  public void Var222() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC1818
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC521818, MIN_NUMERIC521818, null,
        null);
  }

  
  /**
   * Check various combinations of NUMERIC/DECIMAL
   */
  final static String LARGE_NUMERIC52190 = "12345678901234567890";
  final static String MAX_NUMERIC52190 = "9999999999999999999";
  final static String SMALL_NUMERIC52190 = "-12345678901234567890";
  final static String MIN_NUMERIC52190 = "-9999999999999999999";;
  final static String DECLARE_NUMERIC190 = "19,0"; 

  public void Var223() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC190 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52190,
        null, "Exception:Data type mismatch", null);
  }

  public void Var224() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC190 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52190,
        MAX_NUMERIC52190, "Warning:Data type mismatch", null);
  }

  public void Var225() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC190
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52190, MAX_NUMERIC52190, null,
        null);
  }

  public void Var226() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC190 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52190,
        null, "Exception:Data type mismatch", null);
  }

  public void Var227() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC190 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52190,
        MIN_NUMERIC52190, "Warning:Data type mismatch", null);
  }

  public void Var228() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC190
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52190, MIN_NUMERIC52190, null,
        null);
  }

  public void Var229() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC190 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52190,
        null, "Exception:Data type mismatch", null);
  }

  public void Var230() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC190 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52190,
        MAX_NUMERIC52190, "Warning:Data type mismatch", null);
  }

  public void Var231() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC190
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52190, MAX_NUMERIC52190, null,
        null);
  }

  public void Var232() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC190 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52190,
        null, "Exception:Data type mismatch", null);
  }

  public void Var233() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC190 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52190,
        MIN_NUMERIC52190, "Warning:Data type mismatch", null);
  }

  public void Var234() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC190
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52190, MIN_NUMERIC52190, null,
        null);
  }

  
  final static String LARGE_NUMERIC52192 = "123456789012934569.78";
  final static String MAX_NUMERIC52192 = "99999999999999999.99";
  final static String SMALL_NUMERIC52192 = "-123456789012939456.78";
  final static String MIN_NUMERIC52192 = "-99999999999999999.99";;
  final static String DECLARE_NUMERIC192 = "19,2"; 

  public void Var235() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC192 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52192,
        null, "Exception:Data type mismatch", null);
  }

  public void Var236() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC192 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52192,
        MAX_NUMERIC52192, "Warning:Data type mismatch", null);
  }

  public void Var237() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC192
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52192, MAX_NUMERIC52192, null,
        null);
  }

  public void Var238() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC192 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52192,
        null, "Exception:Data type mismatch", null);
  }

  public void Var239() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC192 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52192,
        MIN_NUMERIC52192, "Warning:Data type mismatch", null);
  }

  public void Var240() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC192
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52192, MIN_NUMERIC52192, null,
        null);
  }

  public void Var241() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC192 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52192,
        null, "Exception:Data type mismatch", null);
  }

  public void Var242() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC192 + ")) from sysibm.sysdummy1", LARGE_NUMERIC52192,
        MAX_NUMERIC52192, "Warning:Data type mismatch", null);
  }

  public void Var243() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC192
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC52192, MAX_NUMERIC52192, null,
        null);
  }

  public void Var244() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC192 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52192,
        null, "Exception:Data type mismatch", null);
  }

  public void Var245() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC192 + ")) from sysibm.sysdummy1", SMALL_NUMERIC52192,
        MIN_NUMERIC52192, "Warning:Data type mismatch", null);
  }

  public void Var246() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC192
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC52192, MIN_NUMERIC52192, null,
        null);
  }

  
  final static String LARGE_NUMERIC521919 = "1.9234567890123456789";
  final static String MAX_NUMERIC521919 = "0.9999999999999999999";
  final static String SMALL_NUMERIC521919 = "-1.2934567890123456789";
  final static String MIN_NUMERIC521919 = "-0.9999999999999999999";;
  final static String DECLARE_NUMERIC1919 = "19,19"; 

  public void Var247() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1919 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521919,
        null, "Exception:Data type mismatch", null);
  }

  public void Var248() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1919 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521919,
        MAX_NUMERIC521919, "Warning:Data type mismatch", null);
  }

  public void Var249() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC1919
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC521919, MAX_NUMERIC521919, null,
        null);
  }

  public void Var250() {
    testRangeError(conn_Default, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1919 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521919,
        null, "Exception:Data type mismatch", null);
  }

  public void Var251() {
    testRangeError(conn_Warning, "select cast(? as NUMERIC("
        + DECLARE_NUMERIC1919 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521919,
        MIN_NUMERIC521919, "Warning:Data type mismatch", null);
  }

  public void Var252() {
    testRangeError(conn_None, "select cast(? as NUMERIC(" + DECLARE_NUMERIC1919
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC521919, MIN_NUMERIC521919, null,
        null);
  }

  public void Var253() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1919 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521919,
        null, "Exception:Data type mismatch", null);
  }

  public void Var254() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1919 + ")) from sysibm.sysdummy1", LARGE_NUMERIC521919,
        MAX_NUMERIC521919, "Warning:Data type mismatch", null);
  }

  public void Var255() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC1919
        + ")) from sysibm.sysdummy1", LARGE_NUMERIC521919, MAX_NUMERIC521919, null,
        null);
  }

  public void Var256() {
    testRangeError(conn_Default, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1919 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521919,
        null, "Exception:Data type mismatch", null);
  }

  public void Var257() {
    testRangeError(conn_Warning, "select cast(? as DECIMAL("
        + DECLARE_NUMERIC1919 + ")) from sysibm.sysdummy1", SMALL_NUMERIC521919,
        MIN_NUMERIC521919, "Warning:Data type mismatch", null);
  }

  public void Var258() {
    testRangeError(conn_None, "select cast(? as DECIMAL(" + DECLARE_NUMERIC1919
        + ")) from sysibm.sysdummy1", SMALL_NUMERIC521919, MIN_NUMERIC521919, null,
        null);
  }


  /**
   * default:  fail insert large value into smallint 
   **/
  public void Var259() {
    testResultSetRangeError(conn_Default,
			    schema+".JDCRE259",
			    "SMALLINT",
			    "1",
			    LARGE_SMALLINT, 
			    null,
			    "Exception:Data type mismatch", 
			    null);
  }

  
  /**
   * warning:  warning insert large value into smallint 
   **/
  public void Var260() {
    testResultSetRangeError(conn_Warning,
			    schema+".JDCRE260",
			    "SMALLINT",
			    "1",
         LARGE_SMALLINT, 
         MAX_SMALLINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into smallint 
   **/
  public void Var261() {
    testResultSetRangeError(conn_None,
			    schema+".JDCRE260",
			    "SMALLINT",
			    "1",
         LARGE_SMALLINT, 
         MAX_SMALLINT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into smallint 
   **/
  public void Var262() {
    testResultSetRangeError(conn_Default,
  			    schema+".JDCRE262",
			    "SMALLINT",
			    "1",
       SMALL_SMALLINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into smallint 
   **/
  public void Var263() {
    testResultSetRangeError(conn_Warning,
			    schema+".JDCRE263",
			    "SMALLINT",
			    "1",
         SMALL_SMALLINT, 
         MIN_SMALLINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into smallint 
   **/
  public void Var264() {
    testResultSetRangeError(conn_None,
			    schema+".JDCRE264",
			    "SMALLINT",
			    "1",
         SMALL_SMALLINT, 
         MIN_SMALLINT,
         null, 
         null);

  }

  /**
   * default:  fail insert large value into INTEGER 
   **/
  public void Var265() {
    testResultSetRangeError(conn_Default,
			    schema+".JDCRE265",
			    "INTEGER",
			    "1",
         LARGE_INTEGER, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into INTEGER 
   **/
  public void Var266() {
    testResultSetRangeError(conn_Warning,

   			    schema+".JDCRE265",
			    "INTEGER",
			    "1",
      LARGE_INTEGER, 
         MAX_INTEGER,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into INTEGER 
   **/
  public void Var267() {
    testResultSetRangeError(conn_None,
			    schema+".JDCRE267",
			    "INTEGER",
			    "1",
         LARGE_INTEGER, 
         MAX_INTEGER,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into INTEGER 
   **/
  public void Var268() {
    testResultSetRangeError(conn_Default,
			    schema+".JDCRE268",
			    "INTEGER",
			    "1",
         SMALL_INTEGER, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into INTEGER 
   **/
  public void Var269() {
    testResultSetRangeError(conn_Warning,
			    schema+".JDCRE269",
			    "INTEGER",
			    "1",
         SMALL_INTEGER, 
         MIN_INTEGER,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into INTEGER 
   **/
  public void Var270() {
    testResultSetRangeError(conn_None,
			    schema+".JDCRE270",
			    "INTEGER",
			    "1",
         SMALL_INTEGER, 
         MIN_INTEGER,
         null, 
         null);

  }


  /**
   * default:  fail insert large value into BIGINT 
   **/
  public void Var271() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE271","BIGINT","1", 
         LARGE_BIGINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into BIGINT 
   **/
  public void Var272() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE272","BIGINT","1", 
         LARGE_BIGINT, 
         MAX_BIGINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into BIGINT 
   **/
  public void Var273() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE273","BIGINT","1", 
         LARGE_BIGINT, 
         MAX_BIGINT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into BIGINT 
   **/
  public void Var274() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE274","BIGINT","1", 
         SMALL_BIGINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into BIGINT 
   **/
  public void Var275() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE275","BIGINT","1", 
         SMALL_BIGINT, 
         MIN_BIGINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into BIGINT 
   **/
  public void Var276() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE276","BIGINT","1", 
         SMALL_BIGINT, 
         MIN_BIGINT,
         null, 
         null);

  }
  
  /**
   * default:  fail insert large value into NUMERIC(5,2) 
   **/
  public void Var277() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE277","NUMERIC(5,2)","1", 
         LARGE_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into NUMERIC(5,2) 
   **/
  public void Var278() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE278","NUMERIC(5,2)","1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into NUMERIC(5,2) 
   **/
  public void Var279() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE279","NUMERIC(5,2)","1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into NUMERIC(5,2) 
   **/
  public void Var280() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE280","NUMERIC(5,2)","1", 
         SMALL_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into NUMERIC(5,2) 
   **/
  public void Var281() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE281","NUMERIC(5,2)","1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into NUMERIC(5,2) 
   **/
  public void Var282() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE282","NUMERIC(5,2)","1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
         null, 
         null);

  }



  /**
   * default:  fail insert large value into DECIMAL(5,2) 
   **/
  public void Var283() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE283","DECIMAL(5,2)","1", 
         LARGE_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into DECIMAL(5,2) 
   **/
  public void Var284() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE284","DECIMAL(5,2)","1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into DECIMAL(5,2) 
   **/
  public void Var285() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE285","DECIMAL(5,2)","1", 
         LARGE_NUMERIC52, 
         MAX_NUMERIC52,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into DECIMAL(5,2) 
   **/
  public void Var286() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE286","DECIMAL(5,2)","1", 
         SMALL_NUMERIC52, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into DECIMAL(5,2) 
   **/
  public void Var287() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE287","DECIMAL(5,2)","1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into DECIMAL(5,2) 
   **/
  public void Var288() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE288","DECIMAL(5,2)","1", 
         SMALL_NUMERIC52, 
         MIN_NUMERIC52,
         null, 
         null);

  }

  /**
   * default:  fail insert large value into decfloat(16) 
   **/
  public void Var289() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE289","decfloat(16)","1", 
         LARGE_DECFLOAT16, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into decfloat(16) 
   **/
  public void Var290() {
      if (checkJdbc40()) { 

	  testResultSetRangeError(conn_Warning,
				  schema+".JDCRE290","decfloat(16)","1", 
				  LARGE_DECFLOAT16, 
				  MAX_DECFLOAT16,
				  "Warning:Data type mismatch", 
				  null);
      }

  }

  /**
   * none:  no warning insert large value into decfloat(16) 
   **/
  public void Var291() {
      if (checkJdbc40()) { 

	  testResultSetRangeError(conn_None,
				  schema+".JDCRE291","decfloat(16)","1", 
				  LARGE_DECFLOAT16, 
				  MAX_DECFLOAT16,
				  null, 
				  null);
      }

  }


  /**
   * default:  fail insert small value into decfloat(16) 
   **/
  public void Var292() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE292","decfloat(16)","1", 
         SMALL_DECFLOAT16, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into decfloat(16) 
   **/
  public void Var293() {
      if (checkJdbc40()) { 

    testResultSetRangeError(conn_Warning,
        schema+".JDCRE293","decfloat(16)","1", 
         SMALL_DECFLOAT16, 
         MIN_DECFLOAT16,
        "Warning:Data type mismatch", 
        null);
      }

  }

  /**
   * none:  no warning insert small value into decfloat(16) 
   **/
  public void Var294() {
      if (checkJdbc40()) { 

	  testResultSetRangeError(conn_None,
				  schema+".JDCRE294","decfloat(16)","1", 
				  SMALL_DECFLOAT16, 
				  MIN_DECFLOAT16,
				  null, 
				  null);
      }

  }

  /**
   * default:  fail insert large value into decfloat(34) 
   **/
  public void Var295() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE295","decfloat(34)","1", 
         LARGE_DECFLOAT34, 
          null,
        "Exception:Data type mismatch", 
        null);
  }
 
  /**
   * warning:  warning insert large value into decfloat(34) 
   **/
  public void Var296() {
      if (checkJdbc40()) { 

    testResultSetRangeError(conn_Warning,
        schema+".JDCRE296","decfloat(34)","1", 
         LARGE_DECFLOAT34, 
         MAX_DECFLOAT34,
        "Warning:Data type mismatch", 
        null);
      }

  }

  /**
   * none:  no warning insert large value into decfloat(34) 
   **/
  public void Var297() {
      if (checkJdbc40()) { 
    testResultSetRangeError(conn_None,
        schema+".JDCRE297","decfloat(34)","1", 
         LARGE_DECFLOAT34, 
         MAX_DECFLOAT34,
         null, 
         null);
      }

  }


  /**
   * default:  fail insert small value into decfloat(34) 
   **/
  public void Var298() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE298","decfloat(34)","1", 
         SMALL_DECFLOAT34, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into decfloat(34) 
   **/
  public void Var299() {
      if (checkJdbc40()) { 

	  testResultSetRangeError(conn_Warning,
				  schema+".JDCRE299","decfloat(34)","1", 
				  SMALL_DECFLOAT34, 
				  MIN_DECFLOAT34,
				  "Warning:Data type mismatch", 
				  null);
      }
  }

  /**
   * none:  no warning insert small value into decfloat(34) 
   **/
  public void Var300() {
      if (checkJdbc40()) { 

	  testResultSetRangeError(conn_None,
				  schema+".JDCRE300","decfloat(34)","1", 
				  SMALL_DECFLOAT34, 
				  MIN_DECFLOAT34,
				  null, 
				  null);
      }

  }

  public void Var301() { notApplicable(); }
  public void Var302() { notApplicable(); }

  /**
   * default:  fail insert large value into REAL 
   **/
  public void Var303() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE303","REAL","1", 
         LARGE_REAL, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into REAL 
   **/
  public void Var304() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE304","REAL","1", 
         LARGE_REAL, 
         MAX_REAL,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into REAL 
   **/
  public void Var305() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE305","REAL","1", 
         LARGE_REAL, 
         MAX_REAL,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into REAL 
   **/
  public void Var306() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE306","REAL","1", 
         SMALL_REAL, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into REAL 
   **/
  public void Var307() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE307","REAL","1", 
         SMALL_REAL, 
         MIN_REAL,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into REAL 
   **/
  public void Var308() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE308","REAL","1", 
         SMALL_REAL, 
         MIN_REAL,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large value into FLOAT 
   **/
  public void Var309() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE309","FLOAT","1", 
         LARGE_FLOAT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into FLOAT 
   **/
  public void Var310() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE310","FLOAT","1", 
         LARGE_FLOAT, 
         MAX_FLOAT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into FLOAT 
   **/
  public void Var311() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE311","FLOAT","1", 
         LARGE_FLOAT, 
         MAX_FLOAT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into FLOAT 
   **/
  public void Var312() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE312","FLOAT","1", 
         SMALL_FLOAT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into FLOAT 
   **/
  public void Var313() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE313","FLOAT","1", 
         SMALL_FLOAT, 
         MIN_FLOAT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into FLOAT 
   **/
  public void Var314() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE314","FLOAT","1", 
         SMALL_FLOAT, 
         MIN_FLOAT,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large value into DOUBLE 
   **/
  public void Var315() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE315","DOUBLE","1", 
         LARGE_DOUBLE, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into DOUBLE 
   **/
  public void Var316() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE316","DOUBLE","1", 
         LARGE_DOUBLE, 
         MAX_DOUBLE,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into DOUBLE 
   **/
  public void Var317() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE317","DOUBLE","1", 
         LARGE_DOUBLE, 
         MAX_DOUBLE,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into DOUBLE 
   **/
  public void Var318() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE318","DOUBLE","1", 
         SMALL_DOUBLE, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into DOUBLE 
   **/
  public void Var319() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE319","DOUBLE","1", 
         SMALL_DOUBLE, 
         MIN_DOUBLE,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into DOUBLE 
   **/
  public void Var320() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE320","DOUBLE","1", 
         SMALL_DOUBLE, 
         MIN_DOUBLE,
         null, 
         null);

  }

  /**
   * default:  fail insert large value into smallint 
   **/
  public void Var321() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE321","SMALLINT","1", 
         LARGE_EXP_SMALLINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into smallint 
   **/
  public void Var322() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE322","SMALLINT","1", 
         LARGE_EXP_SMALLINT, 
         MAX_SMALLINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into smallint 
   **/
  public void Var323() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE323","SMALLINT","1", 
         LARGE_EXP_SMALLINT, 
         MAX_SMALLINT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into smallint 
   **/
  public void Var324() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE324","SMALLINT","1", 
         SMALL_EXP_SMALLINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into smallint 
   **/
  public void Var325() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE325","SMALLINT","1", 
         SMALL_EXP_SMALLINT, 
         MIN_SMALLINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into smallint 
   **/
  public void Var326() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE326","SMALLINT","1", 
         SMALL_EXP_SMALLINT, 
         MIN_SMALLINT,
         null, 
         null);

  }

  /**
   * default:  fail insert large value into INTEGER 
   **/
  public void Var327() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE327","INTEGER","1", 
         LARGE_EXP_INTEGER, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into INTEGER 
   **/
  public void Var328() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE328","INTEGER","1", 
         LARGE_EXP_INTEGER, 
         MAX_INTEGER,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into INTEGER 
   **/
  public void Var329() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE329","INTEGER","1", 
         LARGE_EXP_INTEGER, 
         MAX_INTEGER,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into INTEGER 
   **/
  public void Var330() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE330","INTEGER","1", 
         SMALL_EXP_INTEGER, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into INTEGER 
   **/
  public void Var331() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE331","INTEGER","1", 
         SMALL_EXP_INTEGER, 
         MIN_INTEGER,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into INTEGER 
   **/
  public void Var332() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE332","INTEGER","1", 
         SMALL_EXP_INTEGER, 
         MIN_INTEGER,
         null, 
         null);

  }

  /**
   * default:  fail insert large value into BIGINT 
   **/
  public void Var333() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE333","BIGINT","1", 
         LARGE_EXP_BIGINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into BIGINT 
   **/
  public void Var334() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE334","BIGINT","1", 
         LARGE_EXP_BIGINT, 
         MAX_BIGINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into BIGINT 
   **/
  public void Var335() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE335","BIGINT","1", 
         LARGE_EXP_BIGINT, 
         MAX_BIGINT,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into BIGINT 
   **/
  public void Var336() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE336","BIGINT","1", 
         SMALL_EXP_BIGINT, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into BIGINT 
   **/
  public void Var337() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE337","BIGINT","1", 
         SMALL_EXP_BIGINT, 
         MIN_BIGINT,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into BIGINT 
   **/
  public void Var338() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE338","BIGINT","1", 
         SMALL_EXP_BIGINT, 
         MIN_BIGINT,
         null, 
         null);

  }
  
  /**
   * default:  fail insert large value into NUMERIC(5,2) 
   **/
  public void Var339() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE339","NUMERIC(5,2)","1", 
         LARGE_EXP_NUMERIC, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into NUMERIC(5,2) 
   **/
  public void Var340() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE340","NUMERIC(5,2)","1", 
         LARGE_EXP_NUMERIC, 
         MAX_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into NUMERIC(5,2) 
   **/
  public void Var341() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE341","NUMERIC(5,2)","1", 
         LARGE_EXP_NUMERIC, 
         MAX_NUMERIC52,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into NUMERIC(5,2) 
   **/
  public void Var342() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE342","NUMERIC(5,2)","1", 
         SMALL_EXP_NUMERIC, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into NUMERIC(5,2) 
   **/
  public void Var343() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE343","NUMERIC(5,2)","1", 
         SMALL_EXP_NUMERIC, 
         MIN_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert small value into NUMERIC(5,2) 
   **/
  public void Var344() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE344","NUMERIC(5,2)","1", 
         SMALL_EXP_NUMERIC, 
         MIN_NUMERIC52,
         null, 
         null);

  }



  /**
   * default:  fail insert large value into DECIMAL(5,2) 
   **/
  public void Var345() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE345","DECIMAL(5,2)","1", 
         LARGE_EXP_NUMERIC, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert large value into DECIMAL(5,2) 
   **/
  public void Var346() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE346","DECIMAL(5,2)","1", 
         LARGE_EXP_NUMERIC, 
         MAX_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  /**
   * none:  no warning insert large value into DECIMAL(5,2) 
   **/
  public void Var347() {
    testResultSetRangeError(conn_None,
        schema+".JDCRE347","DECIMAL(5,2)","1", 
         LARGE_EXP_NUMERIC, 
         MAX_NUMERIC52,
         null, 
         null);

  }


  /**
   * default:  fail insert small value into DECIMAL(5,2) 
   **/
  public void Var348() {
    testResultSetRangeError(conn_Default,
        schema+".JDCRE348","DECIMAL(5,2)","1", 
         SMALL_EXP_NUMERIC, 
          null,
        "Exception:Data type mismatch", 
        null);

  }

  
  /**
   * warning:  warning insert small value into DECIMAL(5,2) 
   **/
  public void Var349() {
    testResultSetRangeError(conn_Warning,
        schema+".JDCRE349","DECIMAL(5,2)","1", 
         SMALL_EXP_NUMERIC, 
         MIN_NUMERIC52,
        "Warning:Data type mismatch", 
        null);

  }

  
  
  
}
