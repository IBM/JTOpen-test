///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionCharacterTruncation.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionCharacterTruncation.java
//
// Classes:      JDConnectionCharacterTruncation
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDLobTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDTestUtilities;
import test.JDLobTest.JDTestClob;

import java.io.FileOutputStream;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;

/**
 * Testcase JDConnectionCharacterTruncation. this tests the following of the JDBC
 * Connection class:
 * 
 * Property "Translate Hex"
 **/
public class JDConnectionCharacterTruncation extends JDTestcase {

  // Private data.
  private static String schema = JDConnectionTest.COLLECTION;
  private static int tableNumber = 0; 
  private Connection conn_Default;
  private Connection conn_Warning;
  private Connection conn_None;

  /**
   * Constructor.
   **/
  public JDConnectionCharacterTruncation(AS400 systemObject,
      Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, "JDConnectionCharacterTruncation", namesAndVars, runMode,
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
    String urlDefault = baseURL_ +  ";character truncation=default";
    String urlWarning = baseURL_ + ";character truncation=warning";

    String urlNone = baseURL_ +  ";character truncation=none";

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
   * Generic testcase to test truncation on query
   **/

  public void testQueryCharacterTruncation(Connection connection, String query,
      Object parameter, Object expectedResult, String setExceptionString,
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
        if (parameter instanceof String) { 
          ps.setString(1, (String) parameter);
        } else { 
          ps.setObject(1,  parameter); 
        } 
        
        SQLWarning wSet = ps.getWarnings();
        sb.append("Got warning " + wSet+"\n");
        if (setExceptionString != null) {
          if (wSet == null) {
            sb.append("Expected set '" + setExceptionString
                + "' but got no warning\n");
            passed = false;
          } else {
            String exceptionString = wSet.toString();
            if (setExceptionString.indexOf("Warning:") == 0) {
              setExceptionString = setExceptionString.substring(8); 
            }

            if (exceptionString.indexOf(setExceptionString) < 0) {
              sb.append("Got set " + exceptionString + " sb "
                  + setExceptionString + "\n");
              passed = false;
            }
          }
        }
        ps.clearWarnings();
        try {
          sb.append("Running executeQuery\n");
          ResultSet rs = ps.executeQuery();
          SQLWarning wExec = ps.getWarnings();
          sb.append("Got warning " + wExec+"\n");
          if (executeExceptionString != null) {
            if (wExec == null) {
              sb.append("Expected set '" + executeExceptionString
                  + "' but got no warning\n");
              passed = false;
            } else {
              String exceptionString = wExec.toString();
              if (executeExceptionString.indexOf("Warning:") == 0) {
                executeExceptionString = executeExceptionString.substring(8); 
              }

              if (exceptionString.indexOf(executeExceptionString) < 0) {
                sb.append("Got set " + exceptionString + " sb "
                    + executeExceptionString + "\n");
                passed = false;
              }
            }
          }
          rs.next();
          Object value; 
          if (parameter instanceof String) { 
            value = rs.getString(1);
          } else if (parameter instanceof byte[]) {
            value = rs.getBytes(1);
          } else {
            value = rs.getObject(1);
          }
          if (value == null) {
            if (expectedResult != null) {
              sb.append("Got null, should be " + expectedResult + "\n");
              passed = false;
            }
          } else {
            if (value instanceof String) {
              if (!value.equals(expectedResult)) {
                sb.append("getString returned '" + value + "' sb '"
                    + expectedResult + "'\n");
                passed = false;
              }
            } else if (value instanceof byte[]){
                 if (expectedResult instanceof byte[]) { 
                   if (!compare((byte[])value,(byte[])expectedResult, sb)) {
                     passed = false; 
                   }
                 } else {
                   sb.append("Expected type not valid"); 
                   passed = false; 
                 }
            } else if (value instanceof Blob) {
              value = ((Blob)value).getBytes(1, (int) ((Blob)value).length()); 
              if (!compare((byte[])value,(byte[])expectedResult, sb)) {
                passed = false; 
              }
             
            } else if (value instanceof Clob) {
              value = ((Clob)value).getSubString(1, (int) ((Clob)value).length()); 
              if (!value.equals(expectedResult)) {
                sb.append("getObject returned '" + value + "' sb '"
                    + expectedResult + "'\n");
                passed = false;
              }
            } else {
              if (!value.equals(expectedResult)) {
                sb.append("getObject returned '" + value + "' sb '"
                    + expectedResult + "'\n");
                passed = false;
              }
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
    } catch (Exception e) {

      failed(e, "Unexpected Exception : " + sb.toString());
    }

  }

  /**
   * Generic testcase
   **/

  public void testResultSetCharacterTruncation(
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



/*  Test objects */   


  static final String STRING_LARGE_CHAR1 = "12"; 
  static final String STRING_MAX_CHAR1 = "1"; 
  static final String STRING_LARGE2_CHAR1 = "AB"; 
  static final String STRING_MAX2_CHAR1 = "A"; 
  Clob CLOB_LARGE_CHAR1 = new JDLobTest.JDTestClob(STRING_LARGE_CHAR1); 
  Clob CLOB_MAX_CHAR1 = new JDLobTest.JDTestClob(STRING_MAX_CHAR1); 
  Clob CLOB_LARGE2_CHAR1 = new JDLobTest.JDTestClob(STRING_LARGE2_CHAR1); 
  Clob CLOB_MAX2_CHAR1 = new JDLobTest.JDTestClob(STRING_MAX2_CHAR1); 

  static final String STRING_LARGE_VARCHAR1 = "21";
  static final String STRING_MAX_VARCHAR1   = "2"; 
  static final String STRING_LARGE2_VARCHAR1 = "ABCEDFE"; 
  static final String STRING_MAX2_VARCHAR1 = "A"; 
  Clob CLOB_LARGE_VARCHAR1 = new JDLobTest.JDTestClob(STRING_LARGE_VARCHAR1); 
  Clob CLOB_MAX_VARCHAR1 = new JDLobTest.JDTestClob(STRING_MAX_VARCHAR1); 
  Clob CLOB_LARGE2_VARCHAR1 = new JDLobTest.JDTestClob(STRING_LARGE2_VARCHAR1); 
  Clob CLOB_MAX2_VARCHAR1 = new JDLobTest.JDTestClob(STRING_MAX2_VARCHAR1); 


  static final String STRING_LARGE_CLOB10 =  "12345678901"; 
  static final String STRING_MAX_CLOB10 =    "1234567890"; 
  static final String STRING_LARGE2_CLOB10 = "abcdefghijkl"; 
  static final String STRING_MAX2_CLOB10 =   "abcdefghij"; 
  Clob CLOB_LARGE_CLOB10 = new JDLobTest.JDTestClob(STRING_LARGE_CLOB10); 
  Clob CLOB_MAX_CLOB10 = new JDLobTest.JDTestClob(STRING_MAX_CLOB10); 
  Clob CLOB_LARGE2_CLOB10 = new JDLobTest.JDTestClob(STRING_LARGE2_CLOB10); 
  Clob CLOB_MAX2_CLOB10 = new JDLobTest.JDTestClob(STRING_MAX2_CLOB10); 

  
  static final String STRING_LARGE_BINARY1 =  "01020304"; 
  static final String STRING_MAX_BINARY1 =    "01"; 
  static final String STRING_LARGE2_BINARY1 = "f1f2f3f4"; 
  static final String STRING_MAX2_BINARY1 =   "F1"; 
  Clob CLOB_LARGE_BINARY1 = new JDLobTest.JDTestClob(STRING_LARGE_BINARY1); 
  Clob CLOB_MAX_BINARY1 = new JDLobTest.JDTestClob(STRING_MAX_BINARY1); 
  Clob CLOB_LARGE2_BINARY1 = new JDLobTest.JDTestClob(STRING_LARGE2_BINARY1); 
  Clob CLOB_MAX2_BINARY1 = new JDLobTest.JDTestClob(STRING_MAX2_BINARY1); 

  final static String STRING_LARGE_DBCLOB10  = "12345678901"; 
  final static String STRING_MAX_DBCLOB10    = "1234567890"; 
  Clob CLOB_LARGE_DBCLOB10 = new JDLobTest.JDTestClob(STRING_LARGE_DBCLOB10); 
  Clob CLOB_MAX_DBCLOB10 = new JDLobTest.JDTestClob(STRING_MAX_DBCLOB10); 


  final static String STRING_LARGE_GRAPHIC = "1234";
  final static String STRING_MAX_GRAPHIC = "1";
  Clob CLOB_LARGE_GRAPHIC = new JDLobTest.JDTestClob(STRING_LARGE_GRAPHIC); 
  Clob CLOB_MAX_GRAPHIC = new JDLobTest.JDTestClob(STRING_MAX_GRAPHIC); 

  
  final static String STRING_LARGE_VARBINARY1 = "01020304"; 
  final static String STRING_MAX_VARBINARY1 = "01"; 
  final static String STRING_LARGE2_VARBINARY1 = "f1f2f3f4"; 
  final static String STRING_MAX2_VARBINARY1 = "F1"; 
  Clob CLOB_LARGE_VARBINARY1 = new JDLobTest.JDTestClob(STRING_LARGE_VARBINARY1); 
  Clob CLOB_MAX_VARBINARY1 = new JDLobTest.JDTestClob(STRING_MAX_VARBINARY1); 
  Clob CLOB_LARGE2_VARBINARY1 = new JDLobTest.JDTestClob(STRING_LARGE2_VARBINARY1); 
  Clob CLOB_MAX2_VARBINARY1 = new JDLobTest.JDTestClob(STRING_MAX2_VARBINARY1); 
  
  final static String STRING_LARGE_BLOB10 = "01020304050607080910111213"; 
  final static String STRING_MAX_BLOB10 = "01020304050607080910"; 
  Clob CLOB_LARGE_BLOB10 = new JDLobTest.JDTestClob(STRING_LARGE_BLOB10); 
  Clob CLOB_MAX_BLOB10 = new JDLobTest.JDTestClob(STRING_MAX_BLOB10); 

  
  static final byte[] BYTES_LARGE_BINARY1 =  {0x01,0x02,0x03,0x04}; 
  static final byte[] BYTES_MAX_BINARY1 =    {0x01}; 
  static final byte[] BYTES_LARGE2_BINARY1 = {(byte)0xf1,(byte)0xf2,(byte)0xf3,(byte)0xf4}; 
  static final byte[] BYTES_MAX2_BINARY1 =    {(byte)0xf1}; 
  
  final static byte[] BYTES_LARGE_BLOB10 = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x10,0x11,0x12,0x13}; 
  final static byte[] BYTES_MAX_BLOB10 = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x10}; 

  // -----------------------------------------------
  // Summary 
  // -----------------------------------------------
  // Query String tests  #1-42
  // Query bytes tests #43 - 57
  // Query clob tests #58 - 99
  // -----------------------------------------------
  

  // -----------------------------------------------
  // Query String tests  #1-42
  // -----------------------------------------------
  
  /**
   * default:  fail insert large string into CHAR(1) 
   **/
  public void Var001() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as CHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into CHAR(1) 
   **/
  public void Var002() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as CHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into CHAR(1) 
   **/
  public void Var003() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as CHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into NCHAR(1) 
   **/
  public void Var004() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as NCHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into NCHAR(1) 
   **/
  public void Var005() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as NCHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into  NCHAR(1) 
   **/
  public void Var006() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as NCHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
         null, 
         null);

  }


  
  
  /**
   * default:  fail insert large string into VARCHAR(1) 
   **/
  public void Var007() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as VARCHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARCHAR(1) 
   **/
  public void Var008() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as VARCHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARCHAR(1) 
   **/
  public void Var009() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as VARCHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into NVARCHAR(1) 
   **/
  public void Var010() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as NVARCHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into NVARCHAR(1) 
   **/
  public void Var011() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as NVARCHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into NVARCHAR(1) 
   **/
  public void Var012() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as NVARCHAR(1)) from sysibm.sysdummy1", 
         STRING_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into CLOB(10) 
   **/
  public void Var013() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as CLOB(10)) from sysibm.sysdummy1", 
         STRING_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into CLOB(10) 
   **/
  public void Var014() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as CLOB(10)) from sysibm.sysdummy1", 
         STRING_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into CLOB(10) 
   **/
  public void Var015() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as CLOB(10)) from sysibm.sysdummy1", 
         STRING_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into NCLOB(10) 
   **/
  public void Var016() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as NCLOB(10)) from sysibm.sysdummy1", 
         STRING_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into NCLOB(10) 
   **/
  public void Var017() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as NCLOB(10)) from sysibm.sysdummy1", 
         STRING_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into NCLOB(10) 
   **/
  public void Var018() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as NCLOB(10)) from sysibm.sysdummy1", 
         STRING_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
         null, 
         null);

  }

  
  /**
   * default:  fail insert large string into GRAPHIC(1) CCSID 1200 
   **/
  public void Var019() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as GRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into GRAPHIC(1) CCSID 1200 
   **/
  public void Var020() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as GRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into GRAPHIC(1) CCSID 1200 
   **/
  public void Var021() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as GRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
         null, 
         null);

  }


 


  /**
   * default:  fail insert large string into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var022() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as VARGRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var023(){
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as VARGRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var024() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as VARGRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
         null, 
         null);

  }




  /**
   * default:  fail insert large string into DBCLOB(10) CCSID 1200 
   **/
  public void Var025() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as DBCLOB(10) CCSID 1200) from sysibm.sysdummy1", 
         STRING_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10, 
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into DBCLOB(10) CCSID 1200 
   **/
  public void Var026() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as DBCLOB(10) CCSID 1200) from sysibm.sysdummy1", 
         STRING_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into DBCLOB(10) CCSID 1200 
   **/
  public void Var027() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as DBCLOB(10) CCSID 1200) from sysibm.sysdummy1", 
         STRING_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large string into binary(1) 
   **/
  public void Var028() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
         STRING_LARGE_BINARY1, 
         STRING_MAX_BINARY1,
        "Warning:Data truncation", 
        null);
  }
 
  /**
   * warning:  warning insert large string into binary(1) 
   **/
  public void Var029() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
         STRING_LARGE_BINARY1, 
         STRING_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into binary(1) 
   **/
  public void Var030() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
         STRING_LARGE_BINARY1, 
         STRING_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into char(1) for bit data
   **/
  public void Var031() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as char(1) for bit data) from sysibm.sysdummy1", 
         STRING_LARGE2_BINARY1, 
         STRING_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into char(1) for bit data
   **/
  public void Var032() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as char(1) for bit data) from sysibm.sysdummy1", 
         STRING_LARGE2_BINARY1, 
         STRING_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into char(1) for bit data 
   **/
  public void Var033() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
         STRING_LARGE2_BINARY1, 
         STRING_MAX2_BINARY1,
         null, 
         null);

  }

  
  /**
   * default:  fail insert large string into VARBINARY(1) 
   **/
  public void Var034() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
         STRING_LARGE_VARBINARY1, 
         STRING_MAX_VARBINARY1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARBINARY(1) 
   **/
  public void Var035() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
         STRING_LARGE_VARBINARY1, 
         STRING_MAX_VARBINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARBINARY(1) 
   **/
  public void Var036() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
         STRING_LARGE_VARBINARY1, 
         STRING_MAX_VARBINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into VARCHAR(1) FOR BIT DATA 
   **/
  public void Var037() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as VARCHAR(1) for bit data) from sysibm.sysdummy1", 
         STRING_LARGE2_VARBINARY1, 
         STRING_MAX2_VARBINARY1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARchar(1) for bit data 
   **/
  public void Var038() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as VARchar(1) for bit data) from sysibm.sysdummy1", 
         STRING_LARGE2_VARBINARY1, 
         STRING_MAX2_VARBINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARchar(1) for bit data
   **/
  public void Var039() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as VARchar(1) for bit data) from sysibm.sysdummy1", 
         STRING_LARGE2_VARBINARY1, 
         STRING_MAX2_VARBINARY1,
         null, 
         null);

  }


  
  
  /**
   * default:  fail insert large string into BLOB(10) 
   **/
  public void Var040() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
         STRING_LARGE_BLOB10, 
         STRING_MAX_BLOB10,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into BLOB(10) 
   **/
  public void Var041() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
         STRING_LARGE_BLOB10, 
         STRING_MAX_BLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into BLOB(10) 
   **/
  public void Var042() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
         STRING_LARGE_BLOB10, 
         STRING_MAX_BLOB10,
         null, 
         null);

  }

  // -----------------------------------------------
  // Query bytes tests #43 - 57
  // -----------------------------------------------

  
  /**
   * default:  fail insert large byte array into binary(1) 
   **/
  public void Var043() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
         BYTES_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);
  }
 
  /**
   * warning:  warning insert large byte array into binary(1) 
   **/
  public void Var044() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
        BYTES_LARGE_BINARY1, 
        BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large byte array into binary(1) 
   **/
  public void Var045() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
        BYTES_LARGE_BINARY1, 
        BYTES_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large byte array into char(1) for bit data 
   **/
  public void Var046() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as char(1) for bit data) from sysibm.sysdummy1", 
        BYTES_LARGE2_BINARY1, 
        BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large byte array into char(1) for bit data 
   **/
  public void Var047() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as char(1) for bit data) from sysibm.sysdummy1", 
        BYTES_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large byte array into char(1) for bit data 
   **/
  public void Var048() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as char(1) for bit data) from sysibm.sysdummy1", 
        BYTES_LARGE2_BINARY1, 
        BYTES_MAX2_BINARY1,
         null, 
         null);

  }

  /**
   * default:  fail insert large byte array into VARBINARY(1) 
   **/
  public void Var049() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
        BYTES_LARGE_BINARY1, 
        BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large byte array into VARBINARY(1) 
   **/
  public void Var050() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
        BYTES_LARGE_BINARY1, 
        BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large byte array into VARBINARY(1) 
   **/
  public void Var051() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
        BYTES_LARGE_BINARY1, 
        BYTES_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large byte array into VARchar(1) for bit data 
   **/
  public void Var052() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as VARchar(1) for bit data) from sysibm.sysdummy1", 
        BYTES_LARGE2_BINARY1, 
        BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large byte array into VARchar(1) for bit data 
   **/
  public void Var053() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as VARchar(1) for bit data) from sysibm.sysdummy1", 
        BYTES_LARGE2_BINARY1, 
        BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large byte array into VARchar(1) for bit data 
   **/
  public void Var054() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as VARchar(1) for bit data) from sysibm.sysdummy1", 
        BYTES_LARGE2_BINARY1, 
        BYTES_MAX2_BINARY1,
         null, 
         null);

  }


  
  
  /**
   * default:  fail insert large byte array into BLOB(10) 
   **/
  public void Var055() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
        BYTES_LARGE_BLOB10, 
        BYTES_MAX_BLOB10,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large byte array into BLOB(10) 
   **/
  public void Var056() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
        BYTES_LARGE_BLOB10, 
        BYTES_MAX_BLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large byte array into BLOB(10) 
   **/
  public void Var057() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
        BYTES_LARGE_BLOB10, 
        BYTES_MAX_BLOB10,
         null, 
         null);

  }

  public static Clob createClob(String string) {
    return  new JDLobTest.JDTestClob (string); 
 
  }
  
  //-------------------------------------------------
  // Query clob tests #58 - 99
  //-------------------------------------------------

  /**
   * default:  fail insert large CLOB into CHAR(1) 
   **/
  public void Var058() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as CHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into CHAR(1) 
   **/
  public void Var059() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as CHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into CHAR(1) 
   **/
  public void Var060() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as CHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large CLOB into NCHAR(1) 
   **/
  public void Var061() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as NCHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into NCHAR(1) 
   **/
  public void Var062() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as NCHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into  NCHAR(1) 
   **/
  public void Var063() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as NCHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
         null, 
         null);

  }

  /**
   * default:  fail insert large CLOB into VARCHAR(1) 
   **/
  public void Var064() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as VARCHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into VARCHAR(1) 
   **/
  public void Var065() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as VARCHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into VARCHAR(1) 
   **/
  public void Var066() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as VARCHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large CLOB into NVARCHAR(1) 
   **/
  public void Var067() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as NVARCHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into NVARCHAR(1) 
   **/
  public void Var068() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as NVARCHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into NVARCHAR(1) 
   **/
  public void Var069() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as NVARCHAR(1)) from sysibm.sysdummy1", 
         CLOB_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large CLOB into CLOB(10) 
   **/
  public void Var070() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as CLOB(10)) from sysibm.sysdummy1", 
         CLOB_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
         null,
        "Warning:Data truncation");

  }

  
  /**
   * warning:  warning insert large CLOB into CLOB(10) 
   **/
  public void Var071() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as CLOB(10)) from sysibm.sysdummy1", 
         CLOB_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
         null,
        "Warning:Data truncation"); 
       

  }

  /**
   * none:  no warning insert large CLOB into CLOB(10) 
   **/
  public void Var072() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as CLOB(10)) from sysibm.sysdummy1", 
         CLOB_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
         null, 
         null);

  }


  /**
   * default:  fail insert large CLOB into NCLOB(10) 
   **/
  public void Var073() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as NCLOB(10)) from sysibm.sysdummy1", 
         CLOB_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
         null, 
        "Warning:Data truncation");
    
  }

  
  /**
   * warning:  warning insert large CLOB into NCLOB(10) 
   **/
  public void Var074() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as NCLOB(10)) from sysibm.sysdummy1", 
         CLOB_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
         null,
        "Warning:Data truncation");

  }

  /**
   * none:  no warning insert large CLOB into NCLOB(10) 
   **/
  public void Var075() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as NCLOB(10)) from sysibm.sysdummy1", 
         CLOB_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large CLOB into GRAPHIC(1) CCSID 1200 
   **/
  public void Var076() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as GRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into GRAPHIC(1) CCSID 1200 
   **/
  public void Var077() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as GRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into GRAPHIC(1) CCSID 1200 
   **/
  public void Var078() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as GRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
         null, 
         null);

  }


 


  /**
   * default:  fail insert large CLOB into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var079() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as VARGRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var080(){
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as VARGRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var081() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as VARGRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
         null, 
         null);

  }




  /**
   * default:  fail insert large CLOB into DBCLOB(10) CCSID 1200 
   **/
  public void Var082() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as DBCLOB(10) CCSID 1200) from sysibm.sysdummy1", 
         CLOB_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10, 
         null,
        "Warning:Data truncation");

  }

  
  /**
   * warning:  warning insert large CLOB into DBCLOB(10) CCSID 1200 
   **/
  public void Var083() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as DBCLOB(10) CCSID 1200) from sysibm.sysdummy1", 
         CLOB_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10,
         null,
        "Warning:Data truncation");

  }

  /**
   * none:  no warning insert large CLOB into DBCLOB(10) CCSID 1200 
   **/
  public void Var084() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as DBCLOB(10) CCSID 1200) from sysibm.sysdummy1", 
         CLOB_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large CLOB into binary(1) 
   **/
  public void Var085() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);
  }
 
  /**
   * warning:  warning insert large CLOB into binary(1) 
   **/
  public void Var086() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into binary(1) 
   **/
  public void Var087() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large CLOB into char(1) for bit data
   **/
  public void Var088() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as char(1) for bit data) from sysibm.sysdummy1", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into char(1) for bit data
   **/
  public void Var089() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as char(1) for bit data) from sysibm.sysdummy1", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into char(1) for bit data 
   **/
  public void Var090() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as binary(1)) from sysibm.sysdummy1", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
         null, 
         null);

  }

  
  /**
   * default:  fail insert large CLOB into VARBINARY(1) 
   **/
  public void Var091() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into VARBINARY(1) 
   **/
  public void Var092() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into VARBINARY(1) 
   **/
  public void Var093() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large CLOB into VARCHAR(1) FOR BIT DATA 
   **/
  public void Var094() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as VARCHAR(1) for bit data) from sysibm.sysdummy1", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into VARchar(1) for bit data 
   **/
  public void Var095() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as VARchar(1) for bit data) from sysibm.sysdummy1", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into VARchar(1) for bit data
   **/
  public void Var096() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as VARchar(1) for bit data) from sysibm.sysdummy1", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
         null, 
         null);

  }


  
  
  /**
   * default:  fail insert large CLOB into BLOB(10) 
   **/
  public void Var097() {
    testQueryCharacterTruncation(conn_Default,
        "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
         CLOB_LARGE_BLOB10, 
         BYTES_MAX_BLOB10,
        "Warning:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into BLOB(10) 
   **/
  public void Var098() {
    testQueryCharacterTruncation(conn_Warning,
        "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
         CLOB_LARGE_BLOB10, 
         BYTES_MAX_BLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into BLOB(10) 
   **/
  public void Var099() {
    testQueryCharacterTruncation(conn_None,
        "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
         CLOB_LARGE_BLOB10, 
         BYTES_MAX_BLOB10,
         null, 
         null);

  }

  
    public void Var100() { notApplicable(); }
  
    // -----------------------------------------------
    // Query Reader  tests  #101-142
    // -----------------------------------------------
    
    /**
     * default:  fail insert large string into CHAR(1) 
     **/
    public void Var101() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as CHAR(1)) from sysibm.sysdummy1", 
           getReader(STRING_LARGE_CHAR1), 
           STRING_MAX_CHAR1,
          "Warning:Data truncation", 
          null);

    }

    
    private Object getReader(String string) {
      return new StringReader(string); 
    }

    /**
     * warning:  warning insert large string into CHAR(1) 
     **/
    public void Var102() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as CHAR(1)) from sysibm.sysdummy1", 
           getReader(STRING_LARGE_CHAR1), 
           STRING_MAX_CHAR1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into CHAR(1) 
     **/
    public void Var103() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as CHAR(1)) from sysibm.sysdummy1", 
           getReader(STRING_LARGE_CHAR1), 
           STRING_MAX_CHAR1,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into NCHAR(1) 
     **/
    public void Var104() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as NCHAR(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_CHAR1), 
           STRING_MAX2_CHAR1,
          "Warning:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into NCHAR(1) 
     **/
    public void Var105() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as NCHAR(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_CHAR1), 
           STRING_MAX2_CHAR1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into  NCHAR(1) 
     **/
    public void Var106() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as NCHAR(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_CHAR1), 
           STRING_MAX2_CHAR1,
           null, 
           null);

    }


    
    
    /**
     * default:  fail insert large string into VARCHAR(1) 
     **/
    public void Var107() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as VARCHAR(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_VARCHAR1), 
           STRING_MAX_VARCHAR1,
          "Warning:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into VARCHAR(1) 
     **/
    public void Var108() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as VARCHAR(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_VARCHAR1), 
           STRING_MAX_VARCHAR1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into VARCHAR(1) 
     **/
    public void Var109() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as VARCHAR(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_VARCHAR1), 
           STRING_MAX_VARCHAR1,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into NVARCHAR(1) 
     **/
    public void Var110() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as NVARCHAR(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_VARCHAR1), 
           STRING_MAX2_VARCHAR1,
          "Warning:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into NVARCHAR(1) 
     **/
    public void Var111() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as NVARCHAR(1)) from sysibm.sysdummy1", 
          getReader( STRING_LARGE2_VARCHAR1), 
           STRING_MAX2_VARCHAR1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into NVARCHAR(1) 
     **/
    public void Var112() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as NVARCHAR(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_VARCHAR1), 
           STRING_MAX2_VARCHAR1,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into CLOB(10) 
     **/
    public void Var113() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as CLOB(10)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_CLOB10), 
           STRING_MAX_CLOB10,
           null,
          "Warning:Data truncation");

    }

    
    /**
     * warning:  warning insert large string into CLOB(10) 
     **/
    public void Var114() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as CLOB(10)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_CLOB10), 
           STRING_MAX_CLOB10,
           null,
          "Warning:Data truncation");

    }

    /**
     * none:  no warning insert large string into CLOB(10) 
     **/
    public void Var115() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as CLOB(10)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_CLOB10), 
           STRING_MAX_CLOB10,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into NCLOB(10) 
     **/
    public void Var116() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as NCLOB(10)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_CLOB10), 
           STRING_MAX2_CLOB10,
           null,
          "Warning:Data truncation");

    }

    
    /**
     * warning:  warning insert large string into NCLOB(10) 
     **/
    public void Var117() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as NCLOB(10)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_CLOB10), 
           STRING_MAX2_CLOB10,
           null,
          "Warning:Data truncation");

    }

    /**
     * none:  no warning insert large string into NCLOB(10) 
     **/
    public void Var118() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as NCLOB(10)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_CLOB10), 
           STRING_MAX2_CLOB10,
           null, 
           null);

    }

    
    /**
     * default:  fail insert large string into GRAPHIC(1) CCSID 1200 
     **/
    public void Var119() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as GRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
          "Warning:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into GRAPHIC(1) CCSID 1200 
     **/
    public void Var120() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as GRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into GRAPHIC(1) CCSID 1200 
     **/
    public void Var121() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as GRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
           null, 
           null);

    }


   


    /**
     * default:  fail insert large string into VARGRAPHIC(1) CCSID 1200 
     **/
    public void Var122() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as VARGRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
          "Warning:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into VARGRAPHIC(1) CCSID 1200 
     **/
    public void Var123(){
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as VARGRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into VARGRAPHIC(1) CCSID 1200 
     **/
    public void Var124() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as VARGRAPHIC(1) CCSID 1200) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
           null, 
           null);

    }




    /**
     * default:  fail insert large string into DBCLOB(10) CCSID 1200 
     **/
    public void Var125() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as DBCLOB(10) CCSID 1200) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_DBCLOB10), 
           STRING_MAX_DBCLOB10, 
           null,
          "Warning:Data truncation");

    }

    
    /**
     * warning:  warning insert large string into DBCLOB(10) CCSID 1200 
     **/
    public void Var126() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as DBCLOB(10) CCSID 1200) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_DBCLOB10), 
           STRING_MAX_DBCLOB10,
           null,
          "Warning:Data truncation");

    }

    /**
     * none:  no warning insert large string into DBCLOB(10) CCSID 1200 
     **/
    public void Var127() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as DBCLOB(10) CCSID 1200) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_DBCLOB10), 
           STRING_MAX_DBCLOB10,
           null, 
           null);

    }


    
    /**
     * default:  fail insert large string into binary(1) 
     **/
    public void Var128() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as binary(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
          "Warning:Data truncation", 
          null);
    }
   
    /**
     * warning:  warning insert large string into binary(1) 
     **/
    public void Var129() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as binary(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into binary(1) 
     **/
    public void Var130() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as binary(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into char(1) for bit data
     **/
    public void Var131() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as char(1) for bit data) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into char(1) for bit data
     **/
    public void Var132() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as char(1) for bit data) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into char(1) for bit data 
     **/
    public void Var133() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as binary(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
           null, 
           null);

    }

    
    /**
     * default:  fail insert large string into VARBINARY(1) 
     **/
    public void Var134() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into VARBINARY(1) 
     **/
    public void Var135() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into VARBINARY(1) 
     **/
    public void Var136() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as VARBINARY(1)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into VARCHAR(1) FOR BIT DATA 
     **/
    public void Var137() {
      testQueryCharacterTruncation(conn_Default,
          "select cast(? as VARCHAR(1) for bit data) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into VARchar(1) for bit data 
     **/
    public void Var138() {
      testQueryCharacterTruncation(conn_Warning,
          "select cast(? as VARchar(1) for bit data) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into VARchar(1) for bit data
     **/
    public void Var139() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as VARchar(1) for bit data) from sysibm.sysdummy1", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
           null, 
           null);

    }


    
    
    /**
     * default:  fail insert large string into BLOB(10) 
     **/
    public void Var140() {
	if (checkNotGroupTest()) { 
	    testQueryCharacterTruncation(conn_Default,
					 "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
					 getReader(STRING_LARGE_BLOB10), 
					 BYTES_MAX_BLOB10,
					 "Warning:Data truncation", 
					 null);
	}
    }

    
    /**
     * warning:  warning insert large string into BLOB(10) 
     **/
    public void Var141() {
	if (checkNotGroupTest()) { 
	    testQueryCharacterTruncation(conn_Warning,
					 "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
					 getReader(STRING_LARGE_BLOB10), 
					 BYTES_MAX_BLOB10,
					 "Warning:Data truncation", 
					 null);
	}

    }

    /**
     * none:  no warning insert large string into BLOB(10) 
     **/
    public void Var142() {
      testQueryCharacterTruncation(conn_None,
          "select cast(? as BLOB(10)) from sysibm.sysdummy1", 
          getReader(STRING_LARGE_BLOB10), 
           BYTES_MAX_BLOB10,
           null, 
           null);

    }


    public void Var143() { notApplicable(); }
    public void Var144() { notApplicable(); }
    public void Var145() { notApplicable(); }
    public void Var146() { notApplicable(); }
    public void Var147() { notApplicable(); }
    public void Var148() { notApplicable(); }
    public void Var149() { notApplicable(); }
    public void Var150() { notApplicable(); }
    public void Var151() { notApplicable(); }
    public void Var152() { notApplicable(); }
    public void Var153() { notApplicable(); }
    public void Var154() { notApplicable(); }
    public void Var155() { notApplicable(); }
    public void Var156() { notApplicable(); }
    public void Var157() { notApplicable(); }
    public void Var158() { notApplicable(); }
    public void Var159() { notApplicable(); }
    public void Var160() { notApplicable(); }
    public void Var161() { notApplicable(); }
    public void Var162() { notApplicable(); }
    public void Var163() { notApplicable(); }
    public void Var164() { notApplicable(); }
    public void Var165() { notApplicable(); }
    public void Var166() { notApplicable(); }
    public void Var167() { notApplicable(); }
    public void Var168() { notApplicable(); }
    public void Var169() { notApplicable(); }
    public void Var170() { notApplicable(); }
    public void Var171() { notApplicable(); }
    public void Var172() { notApplicable(); }
    public void Var173() { notApplicable(); }
    public void Var174() { notApplicable(); }
    public void Var175() { notApplicable(); }
    public void Var176() { notApplicable(); }
    public void Var177() { notApplicable(); }
    public void Var178() { notApplicable(); }
    public void Var179() { notApplicable(); }
    public void Var180() { notApplicable(); }
    public void Var181() { notApplicable(); }
    public void Var182() { notApplicable(); }
    public void Var183() { notApplicable(); }
    public void Var184() { notApplicable(); }
    public void Var185() { notApplicable(); }
    public void Var186() { notApplicable(); }
    public void Var187() { notApplicable(); }
    public void Var188() { notApplicable(); }
    public void Var189() { notApplicable(); }
    public void Var190() { notApplicable(); }
    public void Var191() { notApplicable(); }
    public void Var192() { notApplicable(); }
    public void Var193() { notApplicable(); }
    public void Var194() { notApplicable(); }
    public void Var195() { notApplicable(); }
    public void Var196() { notApplicable(); }
    public void Var197() { notApplicable(); }
    public void Var198() { notApplicable(); }
    public void Var199() { notApplicable(); }
    public void Var200() { notApplicable(); }
    
    
    
    
    
    
    
    
    
    
    

  //-------------------------------------------
  // Do ResultSet Update Tests
  //-------------------------------------------
  /**
   * default:  fail insert large string into char(1)  
   **/
  public void Var201() {
    testResultSetCharacterTruncation(conn_Default,
			    schema+".JDCRE259",
			    "CHAR(1)",
			    "1",
			    STRING_LARGE_CHAR1, 
			    null,
			    "Exception:Data truncation", 
			    null);
  }

  
  /**
   * warning:  warning insert large string into CHAR(1) 
   **/
  public void Var202() {
    testResultSetCharacterTruncation(conn_Warning,
			    schema+".JDCRE260",
			    "CHAR(1)",
			    "1",
         STRING_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into CHAR(1) 
   **/
  public void Var203() {
    testResultSetCharacterTruncation(conn_None,
			    schema+".JDCRE260",
			    "CHAR(1)",
			    "1",
         STRING_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into NCHAR(1) 
   **/
  public void Var204() {
    testResultSetCharacterTruncation(conn_Default,
  			    schema+".JDCRE262",
			    "NCHAR(1)",
			    "1",
       STRING_LARGE2_CHAR1, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into NCHAR(1) 
   **/
  public void Var205() {
    testResultSetCharacterTruncation(conn_Warning,
			    schema+".JDCRE263",
			    "NCHAR(1)",
			    "1",
         STRING_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into NCHAR(1) 
   **/
  public void Var206() {
    testResultSetCharacterTruncation(conn_None,
			    schema+".JDCRE264",
			    "NCHAR(1)",
			    "1",
         STRING_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
         null, 
         null);

  }

  /**
   * default:  fail insert large string into VARCHAR(1) 
   **/
  public void Var207() {
    testResultSetCharacterTruncation(conn_Default,
			    schema+".JDCRE265",
			    "VARCHAR(1)",
			    "1",
         STRING_LARGE_VARCHAR1, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARCHAR(1) 
   **/
  public void Var208() {
    testResultSetCharacterTruncation(conn_Warning,

   			    schema+".JDCRE265",
			    "VARCHAR(1)",
			    "1",
      STRING_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARCHAR(1) 
   **/
  public void Var209() {
    testResultSetCharacterTruncation(conn_None,
			    schema+".JDCRE267",
			    "VARCHAR(1)",
			    "1",
         STRING_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into NVARCHAR(1) 
   **/
  public void Var210() {
    testResultSetCharacterTruncation(conn_Default,
			    schema+".JDCRE268",
			    "NVARCHAR(1)",
			    "1",
         STRING_LARGE2_VARCHAR1, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into NVARCHAR(1) 
   **/
  public void Var211() {
    testResultSetCharacterTruncation(conn_Warning,
			    schema+".JDCRE269",
			    "NVARCHAR(1)",
			    "1",
         STRING_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into NVARCHAR(1) 
   **/
  public void Var212() {
    testResultSetCharacterTruncation(conn_None,
			    schema+".JDCRE270",
			    "NVARCHAR(1)",
			    "1",
         STRING_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into CLOB(10) 
   **/
  public void Var213() {
    testResultSetCharacterTruncation(conn_Default,
        schema+".JDCRE271","CLOB(10)","1", 
         STRING_LARGE_CLOB10, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into CLOB(10) 
   **/
  public void Var214() {
    testResultSetCharacterTruncation(conn_Warning,
        schema+".JDCRE272","CLOB(10)","1", 
         STRING_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into CLOB(10) 
   **/
  public void Var215() {
    testResultSetCharacterTruncation(conn_None,
        schema+".JDCRE273","CLOB(10)","1", 
         STRING_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into NCLOB(10) 
   **/
  public void Var216() {
    testResultSetCharacterTruncation(conn_Default,
        schema+".JDCRE274","NCLOB(10)","1", 
         STRING_LARGE2_CLOB10, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into NCLOB(10) 
   **/
  public void Var217() {
    testResultSetCharacterTruncation(conn_Warning,
        schema+".JDCRE275","NCLOB(10)","1", 
         STRING_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into NCLOB(10) 
   **/
  public void Var218() {
    testResultSetCharacterTruncation(conn_None,
        schema+".JDCRE276","NCLOB(10)","1", 
         STRING_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
         null, 
         null);

  }
  
  /**
   * default:  fail insert large string into GRAPHIC(1) CCSID 1200 
   **/
  public void Var219() {
    testResultSetCharacterTruncation(conn_Default,
        schema+".JDCRE277","GRAPHIC(1) CCSID 1200","1", 
         STRING_LARGE_GRAPHIC, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into GRAPHIC(1) CCSID 1200 
   **/
  public void Var220() {
    testResultSetCharacterTruncation(conn_Warning,
        schema+".JDCRE278","GRAPHIC(1) CCSID 1200","1", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into GRAPHIC(1) CCSID 1200 
   **/
  public void Var221() {
    testResultSetCharacterTruncation(conn_None,
        schema+".JDCRE279","GRAPHIC(1) CCSID 1200","1", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
         null, 
         null);

  }





  /**
   * default:  fail insert large string into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var222() {
    testResultSetCharacterTruncation(conn_Default,
        schema+".JDCRE283","VARGRAPHIC(1) CCSID 1200","1", 
         STRING_LARGE_GRAPHIC, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var223() {
    testResultSetCharacterTruncation(conn_Warning,
        schema+".JDCRE284","VARGRAPHIC(1) CCSID 1200","1", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var224() {
    testResultSetCharacterTruncation(conn_None,
        schema+".JDCRE285","VARGRAPHIC(1) CCSID 1200","1", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into DBCLOB(10) CCSID 1200 
   **/
  public void Var225() {
    testResultSetCharacterTruncation(conn_Default,
        schema+".JDCRE289","DBCLOB(10) CCSID 1200","1", 
         STRING_LARGE_DBCLOB10, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into DBCLOB(10) CCSID 1200 
   **/
  public void Var226() {
    testResultSetCharacterTruncation(conn_Warning,
        schema+".JDCRE290","DBCLOB(10) CCSID 1200","1", 
         STRING_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into DBCLOB(10) CCSID 1200 
   **/
  public void Var227() {
    testResultSetCharacterTruncation(conn_None,
        schema+".JDCRE291","DBCLOB(10) CCSID 1200","1", 
         STRING_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into binary(1) 
   **/
  public void Var228() {
    testResultSetCharacterTruncation(conn_Default,
        schema+".JDCRE295","binary(1)","1", 
         STRING_LARGE_BINARY1, 
          null,
        "Exception:Data truncation", 
        null);
  }
 
  /**
   * warning:  warning insert large string into binary(1) 
   **/
  public void Var229() {
    testResultSetCharacterTruncation(conn_Warning,
        schema+".JDCRE296","binary(1)","1", 
         STRING_LARGE_BINARY1, 
         STRING_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into binary(1) 
   **/
  public void Var230() {
    testResultSetCharacterTruncation(conn_None,
        schema+".JDCRE297","binary(1)","1", 
         STRING_LARGE_BINARY1, 
         STRING_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into char(1) for bit data 
   **/
  public void Var231() {
    testResultSetCharacterTruncation(conn_Default,
        schema+".JDCRE298","char(1) for bit data","1", 
         STRING_LARGE2_BINARY1, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into char(1) for bit data 
   **/
  public void Var232() {
    testResultSetCharacterTruncation(conn_Warning,
        schema+".JDCRE299","char(1) for bit data","1", 
         STRING_LARGE2_BINARY1, 
         STRING_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into char (1) for bit data 
   **/
  public void Var233() {
    testResultSetCharacterTruncation(conn_None,
        schema+".JDCRE300","char (1) for bit data","1", 
         STRING_LARGE2_BINARY1, 
         STRING_MAX2_BINARY1,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large string into VARBINARY(1) 
   **/
  public void Var234() {
    testResultSetCharacterTruncation(conn_Default,
        schema+".JDCRE303","VARBINARY(1)","1", 
         STRING_LARGE_VARBINARY1, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARBINARY(1) 
   **/
  public void Var235() {
    testResultSetCharacterTruncation(conn_Warning,
        schema+".JDCRE304","VARBINARY(1)","1", 
         STRING_LARGE_VARBINARY1, 
         STRING_MAX_VARBINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARBINARY(1) 
   **/
  public void Var236() {
    testResultSetCharacterTruncation(conn_None,
        schema+".JDCRE305","VARBINARY(1)","1", 
         STRING_LARGE_VARBINARY1, 
         STRING_MAX_VARBINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into VARchar(1)  for bit data
   **/
  public void Var237() {
    testResultSetCharacterTruncation(conn_Default,
        schema+".JDCRE306","VArchar(1) for bit data","1", 
         STRING_LARGE2_VARBINARY1, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARchar(1) for bit data 
   **/
  public void Var238() {
    testResultSetCharacterTruncation(conn_Warning,
        schema+".JDCRE307","VARchar(1) for bit data","1", 
         STRING_LARGE2_VARBINARY1, 
         STRING_MAX2_VARBINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARchar(1) for bit data 
   **/
  public void Var239() {
    testResultSetCharacterTruncation(conn_None,
        schema+".JDCRE308","VARchar(1) for bit data","1", 
         STRING_LARGE2_VARBINARY1, 
         STRING_MAX2_VARBINARY1,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large string into BLOB(10) 
   **/
  public void Var240() {
    testResultSetCharacterTruncation(conn_Default,
        schema+".JDCRE309","BLOB(10)","1", 
         STRING_LARGE_BLOB10, 
          null,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into BLOB(10) 
   **/
  public void Var241() {
    testResultSetCharacterTruncation(conn_Warning,
        schema+".JDCRE310","BLOB(10)","1", 
         STRING_LARGE_BLOB10, 
         STRING_MAX_BLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into BLOB(10) 
   **/
  public void Var242() {
    testResultSetCharacterTruncation(conn_None,
        schema+".JDCRE311","BLOB(10)","1", 
         STRING_LARGE_BLOB10, 
         STRING_MAX_BLOB10,
         null, 
         null);

  }



   public void Var243() { notApplicable(); }
    public void Var244() { notApplicable(); }
    public void Var245() { notApplicable(); }
    public void Var246() { notApplicable(); }
    public void Var247() { notApplicable(); }
    public void Var248() { notApplicable(); }
    public void Var249() { notApplicable(); }
    public void Var250() { notApplicable(); }
    public void Var251() { notApplicable(); }
    public void Var252() { notApplicable(); }
    public void Var253() { notApplicable(); }
    public void Var254() { notApplicable(); }
    public void Var255() { notApplicable(); }
    public void Var256() { notApplicable(); }
    public void Var257() { notApplicable(); }
    public void Var258() { notApplicable(); }
    public void Var259() { notApplicable(); }
    public void Var260() { notApplicable(); }
    public void Var261() { notApplicable(); }
    public void Var262() { notApplicable(); }
    public void Var263() { notApplicable(); }
    public void Var264() { notApplicable(); }
    public void Var265() { notApplicable(); }
    public void Var266() { notApplicable(); }
    public void Var267() { notApplicable(); }
    public void Var268() { notApplicable(); }
    public void Var269() { notApplicable(); }
    public void Var270() { notApplicable(); }
    public void Var271() { notApplicable(); }
    public void Var272() { notApplicable(); }
    public void Var273() { notApplicable(); }
    public void Var274() { notApplicable(); }
    public void Var275() { notApplicable(); }
    public void Var276() { notApplicable(); }
    public void Var277() { notApplicable(); }
    public void Var278() { notApplicable(); }
    public void Var279() { notApplicable(); }
    public void Var280() { notApplicable(); }
    public void Var281() { notApplicable(); }
    public void Var282() { notApplicable(); }
    public void Var283() { notApplicable(); }
    public void Var284() { notApplicable(); }
    public void Var285() { notApplicable(); }
    public void Var286() { notApplicable(); }
    public void Var287() { notApplicable(); }
    public void Var288() { notApplicable(); }
    public void Var289() { notApplicable(); }
    public void Var290() { notApplicable(); }
    public void Var291() { notApplicable(); }
    public void Var292() { notApplicable(); }
    public void Var293() { notApplicable(); }
    public void Var294() { notApplicable(); }
    public void Var295() { notApplicable(); }
    public void Var296() { notApplicable(); }
    public void Var297() { notApplicable(); }
    public void Var298() { notApplicable(); }
    public void Var299() { notApplicable(); }
    public void Var300() { notApplicable(); }
   
  //-------------------------------------------
  // Do insert tests 
  //-------------------------------------------


 /**
   * Generic testcase to test truncation on insert
   **/

  public void testInsertCharacterTruncation(Connection connection, String dataType,
      Object parameter, Object expectedResult, String setExceptionString,
      String executeExceptionString) {
    if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
      notApplicable("Toolbox only test"); 
      return; 
    }
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    try {
      String tableName=schema+".JDCCT"+tableNumber;
      Statement s = connection.createStatement();
      String sql = "DROP TABLE "+tableName;
      try {
	  sb.append("Executing "+sql+"\n");
	  s.executeUpdate(sql); 
      } catch (Exception e) {
	  sb.append("Info: "+sql+" failed" ); 
      } 

      sql = "CREATE TABLE "+tableName+" ( C1 "+dataType+")";
      sb.append("Executing "+sql+"\n");
      s.executeUpdate(sql); 

      sql = "INSERT INTO "+tableName+" VALUES(?)";

      sb.append("Preparing " + sql + "\n");
      PreparedStatement ps = connection.prepareStatement(sql);
      try {
        sb.append("Calling setString(" + parameter + ")\n");
        if (parameter instanceof String) { 
          ps.setString(1, (String) parameter);
        } else { 
          ps.setObject(1,  parameter); 
        } 
        
        SQLWarning wSet = ps.getWarnings();
        sb.append("Got warning " + wSet+"\n");
        if (setExceptionString != null) {
          if (wSet == null) {
            sb.append("Expected set '" + setExceptionString
                + "' but got no warning\n");
            passed = false;
          } else {
            String exceptionString = wSet.toString();
            if (setExceptionString.indexOf("Warning:") == 0) {
              setExceptionString = setExceptionString.substring(8); 
            }

            if (exceptionString.indexOf(setExceptionString) < 0) {
              sb.append("Got set " + exceptionString + " sb "
                  + setExceptionString + "\n");
              passed = false;
            }
          }
        }
        ps.clearWarnings();
        try {
          sb.append("Running execute\n");
          ps.execute();
          SQLWarning wExec = ps.getWarnings();
          sb.append("Got warning " + wExec+"\n");
          if (executeExceptionString != null) {
            if (wExec == null) {
              sb.append("Expected set '" + executeExceptionString
                  + "' but got no warning\n");
              passed = false;
            } else {
              String exceptionString = wExec.toString();
              if (executeExceptionString.indexOf("Warning:") == 0) {
                executeExceptionString = executeExceptionString.substring(8); 
              }

              if (exceptionString.indexOf(executeExceptionString) < 0) {
                sb.append("Got set " + exceptionString + " sb "
                    + executeExceptionString + "\n");
                passed = false;
              }
            }
          }
	  sql = "select * from "+tableName;
	  sb.append("Running "+sql); 
	  ResultSet rs= s.executeQuery(sql); 
          rs.next();
          Object value; 
          if (parameter instanceof String) { 
            value = rs.getString(1);
          } else if (parameter instanceof byte[]) {
            value = rs.getBytes(1);
          } else {
            value = rs.getObject(1);
          }
          if (value == null) {
            if (expectedResult != null) {
              sb.append("Got null, should be " + expectedResult + "\n");
              passed = false;
            }
          } else {
            if (value instanceof String) {
              if (!value.equals(expectedResult)) {
                sb.append("getString returned '" + value + "' sb '"
                    + expectedResult + "'\n");
                passed = false;
              }
            } else if (value instanceof byte[]){
                 if (expectedResult instanceof byte[]) { 
                   if (!compare((byte[])value,(byte[])expectedResult, sb)) {
                     passed = false; 
                   }
                 } else {
                   sb.append("Expected type not valid"); 
                   passed = false; 
                 }
            } else if (value instanceof Blob) {
              value = ((Blob)value).getBytes(1, (int) ((Blob)value).length()); 
              if (!compare((byte[])value,(byte[])expectedResult, sb)) {
                passed = false; 
              }
             
            } else if (value instanceof Clob) {
              value = ((Clob)value).getSubString(1, (int) ((Clob)value).length()); 
              if (!value.equals(expectedResult)) {
                sb.append("getObject returned '" + value + "' sb '"
                    + expectedResult + "'\n");
                passed = false;
              }
            } else {
              if (!value.equals(expectedResult)) {
                sb.append("getObject returned '" + value + "' sb '"
                    + expectedResult + "'\n");
                passed = false;
              }
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
      if (s != null) s.close();
      if (ps != null) ps.close(); 
      assertCondition(passed,sb);
    } catch (SQLException e) {

      failed(e, "Unexpected Exception : " + sb.toString());
    }

  }





 /**
   * default:  fail insert large string into CHAR(1) 
   **/
  public void Var301() {
      tableNumber=301; 
    testInsertCharacterTruncation(conn_Default,
        "CHAR(1)", 
         STRING_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into CHAR(1) 
   **/
  public void Var302() {
      tableNumber=302; 
    testInsertCharacterTruncation(conn_Warning,
        "CHAR(1)", 
         STRING_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into CHAR(1) 
   **/
  public void Var303() {
      tableNumber=303; 
    testInsertCharacterTruncation(conn_None,
        "CHAR(1)", 
         STRING_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into NCHAR(1) 
   **/
  public void Var304() {
      tableNumber=304; 
    testInsertCharacterTruncation(conn_Default,
        "NCHAR(1)", 
         STRING_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into NCHAR(1) 
   **/
  public void Var305() {
      tableNumber=305; 
    testInsertCharacterTruncation(conn_Warning,
        "NCHAR(1)", 
         STRING_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into  NCHAR(1) 
   **/
  public void Var306() {
      tableNumber=306; 
    testInsertCharacterTruncation(conn_None,
        "NCHAR(1)", 
         STRING_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
         null, 
         null);

  }


  
  
  /**
   * default:  fail insert large string into VARCHAR(1) 
   **/
  public void Var307() {
      tableNumber=307; 
    testInsertCharacterTruncation(conn_Default,
        "VARCHAR(1)", 
         STRING_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARCHAR(1) 
   **/
  public void Var308() {
      tableNumber=308; 
    testInsertCharacterTruncation(conn_Warning,
        "VARCHAR(1)", 
         STRING_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARCHAR(1) 
   **/
  public void Var309() {
      tableNumber=309; 
    testInsertCharacterTruncation(conn_None,
        "VARCHAR(1)", 
         STRING_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into NVARCHAR(1) 
   **/
  public void Var310() {
      tableNumber=310; 
    testInsertCharacterTruncation(conn_Default,
        "NVARCHAR(1)", 
         STRING_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into NVARCHAR(1) 
   **/
  public void Var311() {
      tableNumber=311; 
    testInsertCharacterTruncation(conn_Warning,
        "NVARCHAR(1)", 
         STRING_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into NVARCHAR(1) 
   **/
  public void Var312() {
      tableNumber=312; 
    testInsertCharacterTruncation(conn_None,
        "NVARCHAR(1)", 
         STRING_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into CLOB(10) 
   **/
  public void Var313() {
      tableNumber=313; 
    testInsertCharacterTruncation(conn_Default,
        "CLOB(10)", 
         STRING_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into CLOB(10) 
   **/
  public void Var314() {
      tableNumber=314; 
    testInsertCharacterTruncation(conn_Warning,
        "CLOB(10)", 
         STRING_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into CLOB(10) 
   **/
  public void Var315() {
      tableNumber=315; 
    testInsertCharacterTruncation(conn_None,
        "CLOB(10)", 
         STRING_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into NCLOB(10) 
   **/
  public void Var316() {
      tableNumber=316; 
    testInsertCharacterTruncation(conn_Default,
        "NCLOB(10)", 
         STRING_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into NCLOB(10) 
   **/
  public void Var317() {
      tableNumber=317; 
    testInsertCharacterTruncation(conn_Warning,
        "NCLOB(10)", 
         STRING_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into NCLOB(10) 
   **/
  public void Var318() {
      tableNumber=318; 
    testInsertCharacterTruncation(conn_None,
        "NCLOB(10)", 
         STRING_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
         null, 
         null);

  }

  
  /**
   * default:  fail insert large string into GRAPHIC(1) CCSID 1200 
   **/
  public void Var319() {
      tableNumber=319; 
    testInsertCharacterTruncation(conn_Default,
        "GRAPHIC(1) CCSID 1200", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into GRAPHIC(1) CCSID 1200 
   **/
  public void Var320() {
      tableNumber=320; 
    testInsertCharacterTruncation(conn_Warning,
        "GRAPHIC(1) CCSID 1200", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into GRAPHIC(1) CCSID 1200 
   **/
  public void Var321() {
      tableNumber=321; 
    testInsertCharacterTruncation(conn_None,
        "GRAPHIC(1) CCSID 1200", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
         null, 
         null);

  }


 


  /**
   * default:  fail insert large string into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var322() {
      tableNumber=322; 
    testInsertCharacterTruncation(conn_Default,
        "VARGRAPHIC(1) CCSID 1200", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var323(){
      tableNumber=323; 
    testInsertCharacterTruncation(conn_Warning,
        "VARGRAPHIC(1) CCSID 1200", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var324() {
      tableNumber=324; 
    testInsertCharacterTruncation(conn_None,
        "VARGRAPHIC(1) CCSID 1200", 
         STRING_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
         null, 
         null);

  }




  /**
   * default:  fail insert large string into DBCLOB(10) CCSID 1200 
   **/
  public void Var325() {
      tableNumber=325; 
    testInsertCharacterTruncation(conn_Default,
        "DBCLOB(10) CCSID 1200", 
         STRING_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10, 
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into DBCLOB(10) CCSID 1200 
   **/
  public void Var326() {
      tableNumber=326; 
    testInsertCharacterTruncation(conn_Warning,
        "DBCLOB(10) CCSID 1200", 
         STRING_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into DBCLOB(10) CCSID 1200 
   **/
  public void Var327() {
      tableNumber=327; 
    testInsertCharacterTruncation(conn_None,
        "DBCLOB(10) CCSID 1200", 
         STRING_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large string into binary(1) 
   **/
  public void Var328() {
      tableNumber=328; 
    testInsertCharacterTruncation(conn_Default,
        "binary(1)", 
         STRING_LARGE_BINARY1, 
         STRING_MAX_BINARY1,
        "Exception:Data truncation", 
        null);
  }
 
  /**
   * warning:  warning insert large string into binary(1) 
   **/
  public void Var329() {
      tableNumber=329; 
    testInsertCharacterTruncation(conn_Warning,
        "binary(1)", 
         STRING_LARGE_BINARY1, 
         STRING_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into binary(1) 
   **/
  public void Var330() {
      tableNumber=330; 
    testInsertCharacterTruncation(conn_None,
        "binary(1)", 
         STRING_LARGE_BINARY1, 
         STRING_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into char(1) for bit data
   **/
  public void Var331() {
      tableNumber=331; 
    testInsertCharacterTruncation(conn_Default,
        "char(1) for bit data", 
         STRING_LARGE2_BINARY1, 
         STRING_MAX2_BINARY1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into char(1) for bit data
   **/
  public void Var332() {
      tableNumber=332; 
    testInsertCharacterTruncation(conn_Warning,
        "char(1) for bit data", 
         STRING_LARGE2_BINARY1, 
         STRING_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into char(1) for bit data 
   **/
  public void Var333() {
      tableNumber=333; 
    testInsertCharacterTruncation(conn_None,
        "binary(1)", 
         STRING_LARGE2_BINARY1, 
         STRING_MAX2_BINARY1,
         null, 
         null);

  }

  
  /**
   * default:  fail insert large string into VARBINARY(1) 
   **/
  public void Var334() {
      tableNumber=334; 
    testInsertCharacterTruncation(conn_Default,
        "VARBINARY(1)", 
         STRING_LARGE_VARBINARY1, 
         STRING_MAX_VARBINARY1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARBINARY(1) 
   **/
  public void Var335() {
      tableNumber=335; 
    testInsertCharacterTruncation(conn_Warning,
        "VARBINARY(1)", 
         STRING_LARGE_VARBINARY1, 
         STRING_MAX_VARBINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARBINARY(1) 
   **/
  public void Var336() {
      tableNumber=336; 
    testInsertCharacterTruncation(conn_None,
        "VARBINARY(1)", 
         STRING_LARGE_VARBINARY1, 
         STRING_MAX_VARBINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large string into VARCHAR(1) FOR BIT DATA 
   **/
  public void Var337() {
      tableNumber=337; 
    testInsertCharacterTruncation(conn_Default,
        "VARCHAR(1) for bit data", 
         STRING_LARGE2_VARBINARY1, 
         STRING_MAX2_VARBINARY1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into VARchar(1) for bit data 
   **/
  public void Var338() {
      tableNumber=338; 
    testInsertCharacterTruncation(conn_Warning,
        "VARchar(1) for bit data", 
         STRING_LARGE2_VARBINARY1, 
         STRING_MAX2_VARBINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into VARchar(1) for bit data
   **/
  public void Var339() {
      tableNumber=339; 
    testInsertCharacterTruncation(conn_None,
        "VARchar(1) for bit data", 
         STRING_LARGE2_VARBINARY1, 
         STRING_MAX2_VARBINARY1,
         null, 
         null);

  }


  
  
  /**
   * default:  fail insert large string into BLOB(10) 
   **/
  public void Var340() {
      tableNumber=340; 
    testInsertCharacterTruncation(conn_Default,
        "BLOB(10)", 
         STRING_LARGE_BLOB10, 
         STRING_MAX_BLOB10,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large string into BLOB(10) 
   **/
  public void Var341() {
      tableNumber=341; 
    testInsertCharacterTruncation(conn_Warning,
        "BLOB(10)", 
         STRING_LARGE_BLOB10, 
         STRING_MAX_BLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large string into BLOB(10) 
   **/
  public void Var342() {
      tableNumber=342; 
    testInsertCharacterTruncation(conn_None,
        "BLOB(10)", 
         STRING_LARGE_BLOB10, 
         STRING_MAX_BLOB10,
         null, 
         null);

  }

  // -----------------------------------------------
  // Query bytes tests #43 - 57
  // -----------------------------------------------

  
  /**
   * default:  fail insert large byte array into binary(1) 
   **/
  public void Var343() {
      tableNumber=343; 
    testInsertCharacterTruncation(conn_Default,
        "binary(1)", 
         BYTES_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
        "Exception:Data truncation", 
        null);
  }
 
  /**
   * warning:  warning insert large byte array into binary(1) 
   **/
  public void Var344() {
      tableNumber=344; 
    testInsertCharacterTruncation(conn_Warning,
        "binary(1)", 
        BYTES_LARGE_BINARY1, 
        BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large byte array into binary(1) 
   **/
  public void Var345() {
      tableNumber=345; 
    testInsertCharacterTruncation(conn_None,
        "binary(1)", 
        BYTES_LARGE_BINARY1, 
        BYTES_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large byte array into char(1) for bit data 
   **/
  public void Var346() {
      tableNumber=346; 
    testInsertCharacterTruncation(conn_Default,
        "char(1) for bit data", 
        BYTES_LARGE2_BINARY1, 
        BYTES_MAX2_BINARY1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large byte array into char(1) for bit data 
   **/
  public void Var347() {
      tableNumber=347; 
    testInsertCharacterTruncation(conn_Warning,
        "char(1) for bit data", 
        BYTES_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large byte array into char(1) for bit data 
   **/
  public void Var348() {
      tableNumber=348; 
    testInsertCharacterTruncation(conn_None,
        "char(1) for bit data", 
        BYTES_LARGE2_BINARY1, 
        BYTES_MAX2_BINARY1,
         null, 
         null);

  }

  /**
   * default:  fail insert large byte array into VARBINARY(1) 
   **/
  public void Var349() {
      tableNumber=349; 
    testInsertCharacterTruncation(conn_Default,
        "VARBINARY(1)", 
        BYTES_LARGE_BINARY1, 
        BYTES_MAX_BINARY1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large byte array into VARBINARY(1) 
   **/
  public void Var350() {
      tableNumber=350; 
    testInsertCharacterTruncation(conn_Warning,
        "VARBINARY(1)", 
        BYTES_LARGE_BINARY1, 
        BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large byte array into VARBINARY(1) 
   **/
  public void Var351() {
      tableNumber=351; 
    testInsertCharacterTruncation(conn_None,
        "VARBINARY(1)", 
        BYTES_LARGE_BINARY1, 
        BYTES_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large byte array into VARchar(1) for bit data 
   **/
  public void Var352() {
      tableNumber=352; 
    testInsertCharacterTruncation(conn_Default,
        "VARchar(1) for bit data", 
        BYTES_LARGE2_BINARY1, 
        BYTES_MAX2_BINARY1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large byte array into VARchar(1) for bit data 
   **/
  public void Var353() {
      tableNumber=353; 
    testInsertCharacterTruncation(conn_Warning,
        "VARchar(1) for bit data", 
        BYTES_LARGE2_BINARY1, 
        BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large byte array into VARchar(1) for bit data 
   **/
  public void Var354() {
      tableNumber=354; 
    testInsertCharacterTruncation(conn_None,
        "VARchar(1) for bit data", 
        BYTES_LARGE2_BINARY1, 
        BYTES_MAX2_BINARY1,
         null, 
         null);

  }


  
  
  /**
   * default:  fail insert large byte array into BLOB(10) 
   **/
  public void Var355() {
      tableNumber=355; 
    testInsertCharacterTruncation(conn_Default,
        "BLOB(10)", 
        BYTES_LARGE_BLOB10, 
        BYTES_MAX_BLOB10,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large byte array into BLOB(10) 
   **/
  public void Var356() {
      tableNumber=356; 
    testInsertCharacterTruncation(conn_Warning,
        "BLOB(10)", 
        BYTES_LARGE_BLOB10, 
        BYTES_MAX_BLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large byte array into BLOB(10) 
   **/
  public void Var357() {
      tableNumber=357; 
    testInsertCharacterTruncation(conn_None,
        "BLOB(10)", 
        BYTES_LARGE_BLOB10, 
        BYTES_MAX_BLOB10,
         null, 
         null);

  }

  
  //-------------------------------------------------
  // Query clob tests #58 - 99
  //-------------------------------------------------

  /**
   * default:  fail insert large CLOB into CHAR(1) 
   **/
  public void Var358() {
      tableNumber=358; 
    testInsertCharacterTruncation(conn_Default,
        "CHAR(1)", 
         CLOB_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into CHAR(1) 
   **/
  public void Var359() {
      tableNumber=359; 
    testInsertCharacterTruncation(conn_Warning,
        "CHAR(1)", 
         CLOB_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into CHAR(1) 
   **/
  public void Var360() {
      tableNumber=360; 
    testInsertCharacterTruncation(conn_None,
        "CHAR(1)", 
         CLOB_LARGE_CHAR1, 
         STRING_MAX_CHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large CLOB into NCHAR(1) 
   **/
  public void Var361() {
      tableNumber=361; 
    testInsertCharacterTruncation(conn_Default,
        "NCHAR(1)", 
         CLOB_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into NCHAR(1) 
   **/
  public void Var362() {
      tableNumber=362; 
    testInsertCharacterTruncation(conn_Warning,
        "NCHAR(1)", 
         CLOB_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into  NCHAR(1) 
   **/
  public void Var363() {
      tableNumber=363; 
    testInsertCharacterTruncation(conn_None,
        "NCHAR(1)", 
         CLOB_LARGE2_CHAR1, 
         STRING_MAX2_CHAR1,
         null, 
         null);

  }

  /**
   * default:  fail insert large CLOB into VARCHAR(1) 
   **/
  public void Var364() {
      tableNumber=364; 
    testInsertCharacterTruncation(conn_Default,
        "VARCHAR(1)", 
         CLOB_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into VARCHAR(1) 
   **/
  public void Var365() {
      tableNumber=365; 
    testInsertCharacterTruncation(conn_Warning,
        "VARCHAR(1)", 
         CLOB_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into VARCHAR(1) 
   **/
  public void Var366() {
      tableNumber=366; 
    testInsertCharacterTruncation(conn_None,
        "VARCHAR(1)", 
         CLOB_LARGE_VARCHAR1, 
         STRING_MAX_VARCHAR1,
         null, 
         null);

  }


  /**
   * default:  fail insert large CLOB into NVARCHAR(1) 
   **/
  public void Var367() {
      tableNumber=367; 
    testInsertCharacterTruncation(conn_Default,
        "NVARCHAR(1)", 
         CLOB_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into NVARCHAR(1) 
   **/
  public void Var368() {
      tableNumber=368; 
    testInsertCharacterTruncation(conn_Warning,
        "NVARCHAR(1)", 
         CLOB_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into NVARCHAR(1) 
   **/
  public void Var369() {
      tableNumber=369; 
    testInsertCharacterTruncation(conn_None,
        "NVARCHAR(1)", 
         CLOB_LARGE2_VARCHAR1, 
         STRING_MAX2_VARCHAR1,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large CLOB into CLOB(10) 
   **/
  public void Var370() {
      tableNumber=370; 
    testInsertCharacterTruncation(conn_Default,
        "CLOB(10)", 
         CLOB_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
         null,
        "Exception:Data truncation");

  }

  
  /**
   * warning:  warning insert large CLOB into CLOB(10) 
   **/
  public void Var371() {
      tableNumber=371; 
    testInsertCharacterTruncation(conn_Warning,
        "CLOB(10)", 
         CLOB_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
         null,
        "Warning:Data truncation"); 
       

  }

  /**
   * none:  no warning insert large CLOB into CLOB(10) 
   **/
  public void Var372() {
      tableNumber=372; 
    testInsertCharacterTruncation(conn_None,
        "CLOB(10)", 
         CLOB_LARGE_CLOB10, 
         STRING_MAX_CLOB10,
         null, 
         null);

  }


  /**
   * default:  fail insert large CLOB into NCLOB(10) 
   **/
  public void Var373() {
      tableNumber=373; 
    testInsertCharacterTruncation(conn_Default,
        "NCLOB(10)", 
         CLOB_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
         null, 
        "Exception:Data truncation");
    
  }

  
  /**
   * warning:  warning insert large CLOB into NCLOB(10) 
   **/
  public void Var374() {
      tableNumber=374; 
    testInsertCharacterTruncation(conn_Warning,
        "NCLOB(10)", 
         CLOB_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
         null,
        "Warning:Data truncation");

  }

  /**
   * none:  no warning insert large CLOB into NCLOB(10) 
   **/
  public void Var375() {
      tableNumber=375; 
    testInsertCharacterTruncation(conn_None,
        "NCLOB(10)", 
         CLOB_LARGE2_CLOB10, 
         STRING_MAX2_CLOB10,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large CLOB into GRAPHIC(1) CCSID 1200 
   **/
  public void Var376() {
      tableNumber=376; 
    testInsertCharacterTruncation(conn_Default,
        "GRAPHIC(1) CCSID 1200", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into GRAPHIC(1) CCSID 1200 
   **/
  public void Var377() {
      tableNumber=377; 
    testInsertCharacterTruncation(conn_Warning,
        "GRAPHIC(1) CCSID 1200", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into GRAPHIC(1) CCSID 1200 
   **/
  public void Var378() {
      tableNumber=378; 
    testInsertCharacterTruncation(conn_None,
        "GRAPHIC(1) CCSID 1200", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
         null, 
         null);

  }


 


  /**
   * default:  fail insert large CLOB into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var379() {
      tableNumber=379; 
    testInsertCharacterTruncation(conn_Default,
        "VARGRAPHIC(1) CCSID 1200", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var380(){
      tableNumber=380; 
    testInsertCharacterTruncation(conn_Warning,
        "VARGRAPHIC(1) CCSID 1200", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into VARGRAPHIC(1) CCSID 1200 
   **/
  public void Var381() {
      tableNumber=381; 
    testInsertCharacterTruncation(conn_None,
        "VARGRAPHIC(1) CCSID 1200", 
         CLOB_LARGE_GRAPHIC, 
         STRING_MAX_GRAPHIC,
         null, 
         null);

  }




  /**
   * default:  fail insert large CLOB into DBCLOB(10) CCSID 1200 
   **/
  public void Var382() {
      tableNumber=382; 
    testInsertCharacterTruncation(conn_Default,
        "DBCLOB(10) CCSID 1200", 
         CLOB_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10, 
         null,
        "Exception:Data truncation");

  }

  
  /**
   * warning:  warning insert large CLOB into DBCLOB(10) CCSID 1200 
   **/
  public void Var383() {
      tableNumber=383; 
    testInsertCharacterTruncation(conn_Warning,
        "DBCLOB(10) CCSID 1200", 
         CLOB_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10,
         null,
        "Warning:Data truncation");

  }

  /**
   * none:  no warning insert large CLOB into DBCLOB(10) CCSID 1200 
   **/
  public void Var384() {
      tableNumber=384; 
    testInsertCharacterTruncation(conn_None,
        "DBCLOB(10) CCSID 1200", 
         CLOB_LARGE_DBCLOB10, 
         STRING_MAX_DBCLOB10,
         null, 
         null);

  }


  
  /**
   * default:  fail insert large CLOB into binary(1) 
   **/
  public void Var385() {
      tableNumber=385; 
    testInsertCharacterTruncation(conn_Default,
        "binary(1)", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
        "Exception:Data truncation", 
        null);
  }
 
  /**
   * warning:  warning insert large CLOB into binary(1) 
   **/
  public void Var386() {
      tableNumber=386; 
    testInsertCharacterTruncation(conn_Warning,
        "binary(1)", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into binary(1) 
   **/
  public void Var387() {
      tableNumber=387; 
    testInsertCharacterTruncation(conn_None,
        "binary(1)", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large CLOB into char(1) for bit data
   **/
  public void Var388() {
      tableNumber=388; 
    testInsertCharacterTruncation(conn_Default,
        "char(1) for bit data", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into char(1) for bit data
   **/
  public void Var389() {
      tableNumber=389; 
    testInsertCharacterTruncation(conn_Warning,
        "char(1) for bit data", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into char(1) for bit data 
   **/
  public void Var390() {
      tableNumber=390; 
    testInsertCharacterTruncation(conn_None,
        "binary(1)", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
         null, 
         null);

  }

  
  /**
   * default:  fail insert large CLOB into VARBINARY(1) 
   **/
  public void Var391() {
      tableNumber=391; 
    testInsertCharacterTruncation(conn_Default,
        "VARBINARY(1)", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into VARBINARY(1) 
   **/
  public void Var392() {
      tableNumber=392; 
    testInsertCharacterTruncation(conn_Warning,
        "VARBINARY(1)", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into VARBINARY(1) 
   **/
  public void Var393() {
      tableNumber=393; 
    testInsertCharacterTruncation(conn_None,
        "VARBINARY(1)", 
         CLOB_LARGE_BINARY1, 
         BYTES_MAX_BINARY1,
         null, 
         null);

  }


  /**
   * default:  fail insert large CLOB into VARCHAR(1) FOR BIT DATA 
   **/
  public void Var394() {
      tableNumber=394; 
    testInsertCharacterTruncation(conn_Default,
        "VARCHAR(1) for bit data", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into VARchar(1) for bit data 
   **/
  public void Var395() {
      tableNumber=395; 
    testInsertCharacterTruncation(conn_Warning,
        "VARchar(1) for bit data", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into VARchar(1) for bit data
   **/
  public void Var396() {
      tableNumber=396; 
    testInsertCharacterTruncation(conn_None,
        "VARchar(1) for bit data", 
         CLOB_LARGE2_BINARY1, 
         BYTES_MAX2_BINARY1,
         null, 
         null);

  }


  
  
  /**
   * default:  fail insert large CLOB into BLOB(10) 
   **/
  public void Var397() {
      tableNumber=397; 
    testInsertCharacterTruncation(conn_Default,
        "BLOB(10)", 
         CLOB_LARGE_BLOB10, 
         BYTES_MAX_BLOB10,
        "Exception:Data truncation", 
        null);

  }

  
  /**
   * warning:  warning insert large CLOB into BLOB(10) 
   **/
  public void Var398() {
      tableNumber=398; 
    testInsertCharacterTruncation(conn_Warning,
        "BLOB(10)", 
         CLOB_LARGE_BLOB10, 
         BYTES_MAX_BLOB10,
        "Warning:Data truncation", 
        null);

  }

  /**
   * none:  no warning insert large CLOB into BLOB(10) 
   **/
  public void Var399() {
      tableNumber=399; 
    testInsertCharacterTruncation(conn_None,
        "BLOB(10)", 
         CLOB_LARGE_BLOB10, 
         BYTES_MAX_BLOB10,
         null, 
         null);

  }

  
    public void Var400() { notApplicable(); }
  
    // -----------------------------------------------
    // Query Reader  tests  #101-142
    // -----------------------------------------------
    
    /**
     * default:  fail insert large string into CHAR(1) 
     **/
    public void Var401() {
	tableNumber=401; 
      testInsertCharacterTruncation(conn_Default,
          "CHAR(1)", 
           getReader(STRING_LARGE_CHAR1), 
           STRING_MAX_CHAR1,
          "Exception:Data truncation", 
          null);

    }


    /**
     * warning:  warning insert large string into CHAR(1) 
     **/
    public void Var402() {
	tableNumber=402; 
      testInsertCharacterTruncation(conn_Warning,
          "CHAR(1)", 
           getReader(STRING_LARGE_CHAR1), 
           STRING_MAX_CHAR1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into CHAR(1) 
     **/
    public void Var403() {
	tableNumber=403; 
      testInsertCharacterTruncation(conn_None,
          "CHAR(1)", 
           getReader(STRING_LARGE_CHAR1), 
           STRING_MAX_CHAR1,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into NCHAR(1) 
     **/
    public void Var404() {
	tableNumber=404; 
      testInsertCharacterTruncation(conn_Default,
          "NCHAR(1)", 
          getReader(STRING_LARGE2_CHAR1), 
           STRING_MAX2_CHAR1,
          "Exception:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into NCHAR(1) 
     **/
    public void Var405() {
	tableNumber=405; 
      testInsertCharacterTruncation(conn_Warning,
          "NCHAR(1)", 
          getReader(STRING_LARGE2_CHAR1), 
           STRING_MAX2_CHAR1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into  NCHAR(1) 
     **/
    public void Var406() {
	tableNumber=406; 
      testInsertCharacterTruncation(conn_None,
          "NCHAR(1)", 
          getReader(STRING_LARGE2_CHAR1), 
           STRING_MAX2_CHAR1,
           null, 
           null);

    }


    
    
    /**
     * default:  fail insert large string into VARCHAR(1) 
     **/
    public void Var407() {
	tableNumber=407; 
      testInsertCharacterTruncation(conn_Default,
          "VARCHAR(1)", 
          getReader(STRING_LARGE_VARCHAR1), 
           STRING_MAX_VARCHAR1,
          "Exception:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into VARCHAR(1) 
     **/
    public void Var408() {
	tableNumber=408; 
      testInsertCharacterTruncation(conn_Warning,
          "VARCHAR(1)", 
          getReader(STRING_LARGE_VARCHAR1), 
           STRING_MAX_VARCHAR1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into VARCHAR(1) 
     **/
    public void Var409() {
	tableNumber=409; 
      testInsertCharacterTruncation(conn_None,
          "VARCHAR(1)", 
          getReader(STRING_LARGE_VARCHAR1), 
           STRING_MAX_VARCHAR1,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into NVARCHAR(1) 
     **/
    public void Var410() {
	tableNumber=410; 
      testInsertCharacterTruncation(conn_Default,
          "NVARCHAR(1)", 
          getReader(STRING_LARGE2_VARCHAR1), 
           STRING_MAX2_VARCHAR1,
          "Exception:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into NVARCHAR(1) 
     **/
    public void Var411() {
	tableNumber=411; 
      testInsertCharacterTruncation(conn_Warning,
          "NVARCHAR(1)", 
          getReader( STRING_LARGE2_VARCHAR1), 
           STRING_MAX2_VARCHAR1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into NVARCHAR(1) 
     **/
    public void Var412() {
	tableNumber=412; 
      testInsertCharacterTruncation(conn_None,
          "NVARCHAR(1)", 
          getReader(STRING_LARGE2_VARCHAR1), 
           STRING_MAX2_VARCHAR1,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into CLOB(10) 
     **/
    public void Var413() {
	tableNumber=413; 
      testInsertCharacterTruncation(conn_Default,
          "CLOB(10)", 
          getReader(STRING_LARGE_CLOB10), 
           STRING_MAX_CLOB10,
           null,
          "Exception:Data truncation");

    }

    
    /**
     * warning:  warning insert large string into CLOB(10) 
     **/
    public void Var414() {
	tableNumber=414; 
      testInsertCharacterTruncation(conn_Warning,
          "CLOB(10)", 
          getReader(STRING_LARGE_CLOB10), 
           STRING_MAX_CLOB10,
           null,
          "Warning:Data truncation");

    }

    /**
     * none:  no warning insert large string into CLOB(10) 
     **/
    public void Var415() {
	tableNumber=415; 
      testInsertCharacterTruncation(conn_None,
          "CLOB(10)", 
          getReader(STRING_LARGE_CLOB10), 
           STRING_MAX_CLOB10,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into NCLOB(10) 
     **/
    public void Var416() {
	tableNumber=416; 
      testInsertCharacterTruncation(conn_Default,
          "NCLOB(10)", 
          getReader(STRING_LARGE2_CLOB10), 
           STRING_MAX2_CLOB10,
           null,
          "Exception:Data truncation");

    }

    
    /**
     * warning:  warning insert large string into NCLOB(10) 
     **/
    public void Var417() {
	tableNumber=417; 
      testInsertCharacterTruncation(conn_Warning,
          "NCLOB(10)", 
          getReader(STRING_LARGE2_CLOB10), 
           STRING_MAX2_CLOB10,
           null,
          "Warning:Data truncation");

    }

    /**
     * none:  no warning insert large string into NCLOB(10) 
     **/
    public void Var418() {
	tableNumber=418; 
      testInsertCharacterTruncation(conn_None,
          "NCLOB(10)", 
          getReader(STRING_LARGE2_CLOB10), 
           STRING_MAX2_CLOB10,
           null, 
           null);

    }

    
    /**
     * default:  fail insert large string into GRAPHIC(1) CCSID 1200 
     **/
    public void Var419() {
	tableNumber=419; 
      testInsertCharacterTruncation(conn_Default,
          "GRAPHIC(1) CCSID 1200", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
          "Exception:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into GRAPHIC(1) CCSID 1200 
     **/
    public void Var420() {
	tableNumber=420; 
      testInsertCharacterTruncation(conn_Warning,
          "GRAPHIC(1) CCSID 1200", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into GRAPHIC(1) CCSID 1200 
     **/
    public void Var421() {
	tableNumber=421; 
      testInsertCharacterTruncation(conn_None,
          "GRAPHIC(1) CCSID 1200", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
           null, 
           null);

    }


   


    /**
     * default:  fail insert large string into VARGRAPHIC(1) CCSID 1200 
     **/
    public void Var422() {
	tableNumber=422; 
      testInsertCharacterTruncation(conn_Default,
          "VARGRAPHIC(1) CCSID 1200", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
          "Exception:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into VARGRAPHIC(1) CCSID 1200 
     **/
    public void Var423(){
	tableNumber=423; 
      testInsertCharacterTruncation(conn_Warning,
          "VARGRAPHIC(1) CCSID 1200", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into VARGRAPHIC(1) CCSID 1200 
     **/
    public void Var424() {
	tableNumber=424; 
      testInsertCharacterTruncation(conn_None,
          "VARGRAPHIC(1) CCSID 1200", 
          getReader(STRING_LARGE_GRAPHIC), 
           STRING_MAX_GRAPHIC,
           null, 
           null);

    }




    /**
     * default:  fail insert large string into DBCLOB(10) CCSID 1200 
     **/
    public void Var425() {
	tableNumber=425; 
      testInsertCharacterTruncation(conn_Default,
          "DBCLOB(10) CCSID 1200", 
          getReader(STRING_LARGE_DBCLOB10), 
           STRING_MAX_DBCLOB10, 
           null,
          "Exception:Data truncation");

    }

    
    /**
     * warning:  warning insert large string into DBCLOB(10) CCSID 1200 
     **/
    public void Var426() {
	tableNumber=426; 
      testInsertCharacterTruncation(conn_Warning,
          "DBCLOB(10) CCSID 1200", 
          getReader(STRING_LARGE_DBCLOB10), 
           STRING_MAX_DBCLOB10,
           null,
          "Warning:Data truncation");

    }

    /**
     * none:  no warning insert large string into DBCLOB(10) CCSID 1200 
     **/
    public void Var427() {
	tableNumber=427; 
      testInsertCharacterTruncation(conn_None,
          "DBCLOB(10) CCSID 1200", 
          getReader(STRING_LARGE_DBCLOB10), 
           STRING_MAX_DBCLOB10,
           null, 
           null);

    }


    
    /**
     * default:  fail insert large string into binary(1) 
     **/
    public void Var428() {
	tableNumber=428; 
      testInsertCharacterTruncation(conn_Default,
          "binary(1)", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
          "Exception:Data truncation", 
          null);
    }
   
    /**
     * warning:  warning insert large string into binary(1) 
     **/
    public void Var429() {
	tableNumber=429; 
      testInsertCharacterTruncation(conn_Warning,
          "binary(1)", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into binary(1) 
     **/
    public void Var430() {
	tableNumber=430; 
      testInsertCharacterTruncation(conn_None,
          "binary(1)", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into char(1) for bit data
     **/
    public void Var431() {
	tableNumber=431; 
      testInsertCharacterTruncation(conn_Default,
          "char(1) for bit data", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
          "Exception:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into char(1) for bit data
     **/
    public void Var432() {
	tableNumber=432; 
      testInsertCharacterTruncation(conn_Warning,
          "char(1) for bit data", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into char(1) for bit data 
     **/
    public void Var433() {
	tableNumber=433; 
      testInsertCharacterTruncation(conn_None,
          "binary(1)", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
           null, 
           null);

    }

    
    /**
     * default:  fail insert large string into VARBINARY(1) 
     **/
    public void Var434() {
	tableNumber=434; 
      testInsertCharacterTruncation(conn_Default,
          "VARBINARY(1)", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
          "Exception:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into VARBINARY(1) 
     **/
    public void Var435() {
	tableNumber=435; 
      testInsertCharacterTruncation(conn_Warning,
          "VARBINARY(1)", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into VARBINARY(1) 
     **/
    public void Var436() {
	tableNumber=436; 
      testInsertCharacterTruncation(conn_None,
          "VARBINARY(1)", 
          getReader(STRING_LARGE_BINARY1), 
           BYTES_MAX_BINARY1,
           null, 
           null);

    }


    /**
     * default:  fail insert large string into VARCHAR(1) FOR BIT DATA 
     **/
    public void Var437() {
	tableNumber=437; 
      testInsertCharacterTruncation(conn_Default,
          "VARCHAR(1) for bit data", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
          "Exception:Data truncation", 
          null);

    }

    
    /**
     * warning:  warning insert large string into VARchar(1) for bit data 
     **/
    public void Var438() {
	tableNumber=438; 
      testInsertCharacterTruncation(conn_Warning,
          "VARchar(1) for bit data", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
          "Warning:Data truncation", 
          null);

    }

    /**
     * none:  no warning insert large string into VARchar(1) for bit data
     **/
    public void Var439() {
	tableNumber=439; 
      testInsertCharacterTruncation(conn_None,
          "VARchar(1) for bit data", 
          getReader(STRING_LARGE2_BINARY1), 
           BYTES_MAX2_BINARY1,
           null, 
           null);

    }


    
    
    /**
     * default:  fail insert large string into BLOB(10) 
     **/
    public void Var440() {
	if (checkNotGroupTest()) { 
	tableNumber=440; 
      testInsertCharacterTruncation(conn_Default,
          "BLOB(10)", 
          getReader(STRING_LARGE_BLOB10), 
           BYTES_MAX_BLOB10,
          "Exception:Data truncation", 
          null);
	}
    }

    
    /**
     * Exception:  warning insert large string into BLOB(10) 
     **/
    public void Var441() {
	if (checkNotGroupTest()) { 
	tableNumber=441; 
      testInsertCharacterTruncation(conn_Warning,
          "BLOB(10)", 
          getReader(STRING_LARGE_BLOB10), 
           BYTES_MAX_BLOB10,
          "Warning:Data truncation", 
          null);
	}

    }

    /**
     * none:  no warning insert large string into BLOB(10) 
     **/
    public void Var442() {
	tableNumber=442; 
      testInsertCharacterTruncation(conn_None,
          "BLOB(10)", 
          getReader(STRING_LARGE_BLOB10), 
           BYTES_MAX_BLOB10,
           null, 
           null);

    }


    
  /**
   * Test mixed CCSID truncation
   */
  public void Var443() {
    if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
      notApplicable("Toolbox only test");
      return;
    }

    int ccsid = 5035;
    String data = "AA\uff38\uff38B\uff38\uff38CC\uff38DDD";
    String table = collection_ + ".JDCCT443";
    
    testTruncationLengths(ccsid, data,table); 
  }

  
  /**
   * Test UTF8 CCSID truncation
   */
  public void Var444() {
    if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
      notApplicable("Toolbox only test");
      return;
    }

    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
    notApplicable("System does not truncate 1208 correctly"); 
    return;
    }

    int ccsid = 1208;
    String data = "AA\uff38\uff38B\uff38\uff38CC\uff38DDD";
    String table = collection_ + ".JDCCT444";
    
    testTruncationLengths(ccsid, data,table); 
  }


  /**
   * Test UTF8 CCSID truncation
   */
  public void Var445() {
    if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
      notApplicable("Toolbox only test");
      return;
    }

   if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
    notApplicable("System does not truncate 1208 correctly"); 
    return;
   }
    int ccsid = 1208;
    String data = "A\u00c7\u00d7\u07Fa\u0800\u500c\uffee\uD856\udeff\ud869\uded6DD";
    String table = collection_ + ".JDCCT444";
    
    testTruncationLengths(ccsid, data,table); 
  }

  
  
  
  /**
   * Compare when the database and the driver silently truncate data. 
   * Does it stay valid? 
   * @param ccsid
   * @param data
   * @param table
   */
  public void testTruncationLengths(int ccsid, String data, String table) {   
    StringBuffer sb = new StringBuffer();
    sb.append("Testing truncation differences between system and driver\n");
    boolean passed = true;
    String sql;
    try {
      Statement stmt = conn_None.createStatement();
      
      try {
        sql = "drop table " + table;
        sb.append("Executing " + sql + "\n");

        stmt.executeUpdate(sql);
      } catch (Exception e) {

      }

      sql = "create table " + table + "(C1 VARCHAR(" + (data.length() * 3)
          + ") CCSID " + ccsid + ")";
      sb.append("Executing " + sql + "\n");
      stmt.executeUpdate(sql);
      ;
      sql = "insert into " + table + " values(?)";
      sb.append("Preparing " + sql + "\n");
      PreparedStatement psInit = conn_None.prepareStatement(sql);
      psInit.setString(1, data);
      psInit.executeUpdate();
      psInit.close();

      // Minimum size of mixed from SQL0604:  
      // If the FOR MIXED DATA clause or a mixed CCSID is specified, the length
      // cannot be less than 4
      for (int i = data.length() * 3; i > 3; i--) {

        sql = "SELECT CAST(?  AS  VARCHAR(" + i + ") CCSID " + ccsid + "),  "
        		+ " HEX(CAST(?  AS  VARCHAR(" + i + ") CCSID " + ccsid + ")), "
            + " CAST( C1 AS  VARCHAR(" + i + ") CCSID " + ccsid + " ),  "
            + " HEX(CAST( C1 AS  VARCHAR(" + i + ") CCSID " + ccsid + " ))"
            + " FROM " + table;
        sb.append("Preparing " + sql + "\n");
        PreparedStatement ps = conn_None.prepareStatement(sql);
        ps.setString(1, data);
        ps.setString(2, data);
        try { 
          ResultSet rs = ps.executeQuery();
          rs.next();
          sb.append(".. get TB  hex as " + rs.getString(2) + "\n");
          sb.append(".. get SYS hex as " + rs.getString(4) + "\n");
          String toolboxData = rs.getString(1);
          String systemData = rs.getString(3);
          if (!toolboxData.equals(systemData)) {
            passed = false;
            sb.append("*** ERROR ** " + "\ntoolboxData='"
                + JDTestUtilities.getMixedString(toolboxData) + "'"
                + "\nsystemData ='" + JDTestUtilities.getMixedString(systemData)
                + "'\n");
          }
        } catch (SQLException sqlex) { 
          passed = false; 
          sb.append("*** EXCEPTION ****"+sqlex.toString()+"\n"); 
          if (sqlex.getErrorCode() == -302) {
            // We know the toolbox truncation is wrong.. check the system truncation
            sql = "SELECT  "
                + " CAST( C1 AS  VARCHAR(" + i + ") CCSID " + ccsid + " ),  "
                + " HEX(CAST( C1 AS  VARCHAR(" + i + ") CCSID " + ccsid + " ))"
                + " FROM " + table;
            sb.append("Running "+sql+"\n"); 
            ps = conn_None.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String hexResult = rs.getString(2); 
            sb.append(".. get hex as '" + hexResult + "' byte len= "+(hexResult.length()/2)+"\n");
            
          } else {
            printStackTraceToStringBuffer(sqlex, sb); 
          }
          sb.append("*** ----------------------------------------------------------------- ****\n"); 
          
        }
        
        ps.close();
      }
      stmt.close();
      assertCondition(passed, sb);
    } catch (Exception e) {
      failed(e, sb.toString());
    }

  }
  
}
