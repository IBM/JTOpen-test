///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASSFCallableStatement.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: AS400JDBCSFStatement.java
//  Classes:   AS400JDBCSFStatement
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test.JD.AS;


import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;


import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.AS400JDBCPreparedStatement;
import com.ibm.as400.access.AS400JDBCStatement;

import test.JDASTest;
import test.JDReflectionUtil;
import test.SocketProxy;
import test.SocketProxy.*;
import test.socketProxy.SocketProxyPair; 


/**
 * Testcase JDASSFPreparedStatement Test for the Statement object covers for
 * Seamless failover
 **/
public class JDASSFCallableStatement extends JDASTestcase {
  static final int RUN_SECONDS = 20;

  private String systemName;

  Connection transactionalConnection;
  Connection autocommitConnection;
  Connection failConnection;
  SocketProxyPair transactionalSocketProxyPair;
  SocketProxyPair autocommitSocketProxyPair;
  SocketProxyPair failSocketProxyPair;
  String collection;

  private String table;
  Vector cleanupSql = new Vector();

  /**
   * Constructor.
   **/
  public JDASSFCallableStatement(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASSFCallableStatement", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }

  public void setup() {
    String sql = "";
    try {
      systemName = systemObject_.getSystemName();

      if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {
        url_ = "jdbc:as400://" + systemObject_.getSystemName() + ";user="
            + systemObject_.getUserId() ;

        String url;

        SocketProxyPair transactionalSocketProxyPair = SocketProxyPair.getSocketProxyPair(8001,
            8010, systemName, JDASTest.AS_DATABASE);

        url = "jdbc:as400:localhost:"
            + transactionalSocketProxyPair.getPortNumber1()
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1;"
            + "retryIntervalForClientReroute=1;"
            + "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
            + transactionalSocketProxyPair.getPortNumber2();

        transactionalConnection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);
        transactionalConnection.setAutoCommit(false);
        transactionalConnection
            .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        autocommitSocketProxyPair = SocketProxyPair.getSocketProxyPair(8021,
            8030, systemName, JDASTest.AS_DATABASE);

        url = "jdbc:as400:localhost:"
            + autocommitSocketProxyPair.getPortNumber1()
            + ";date format=jis;time format=jis;"
            + "enableClientAffinitiesList=1;enableSeamlessFailover=1;"
            + "retryIntervalForClientReroute=1;"
            + "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
            + autocommitSocketProxyPair.getPortNumber2();

        autocommitConnection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);
        autocommitConnection.setAutoCommit(true);

        failSocketProxyPair = SocketProxyPair.getSocketProxyPair(8021, 8030,
            systemName, JDASTest.AS_DATABASE);

        url = "jdbc:as400:localhost:"
            + failSocketProxyPair.getPortNumber1()
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1;"
            + "retryIntervalForClientReroute=1;"
            + "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
            + failSocketProxyPair.getPortNumber2();

        failConnection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        failConnection.setAutoCommit(true);

        collection = JDASTest.COLLECTION;
        table = collection + ".JDASEMLSS";
        Statement stmt = transactionalConnection.createStatement();
        try {
          stmt.executeUpdate("DROP TABLE " + table);
        } catch (Exception e) {

        }
        transactionalConnection.commit();

        stmt.executeUpdate("CREATE TABLE " + table
            + "(C1 INT, GENID INT GENERATED ALWAYS AS IDENTITY)");
        transactionalConnection.commit();

        System.out.println("DONE WITH SETUP");
      }

    } catch (Exception e) {
      System.out.println("Setup error.");
      System.out.println("Last sql statement was the following");
      System.out.println(sql);
      e.printStackTrace(System.out);
    }
  }

  /**
   * @exception Exception
   *              If an exception occurs.
   **/
  public void cleanup() throws Exception {
    try {
      if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {

        Statement stmt = transactionalConnection.createStatement();
        Enumeration e = cleanupSql.elements();
        while (e.hasMoreElements()) {
          try {
            String sql = (String) e.nextElement();
            stmt.execute(sql);
            transactionalConnection.commit();
          } catch (Exception ex) {
            // Ignore any cleanup exceptions
          }
        }

        transactionalConnection.close();
        autocommitConnection.close();
        failConnection.close();
        transactionalSocketProxyPair.endService();
        autocommitSocketProxyPair.endService();
        failSocketProxyPair.endService();

      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public interface SetMethodTest {
    String getProcedureName(); 
    String getTestDescription(); 
    /* The procedure definition should accept one input parameter and */
    /* return a result set containing the input parameter */ 
    String getProcedureDefinition();
    void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception;
    /* call the get method on the result set to check the result.  Return false if incorrect */ 
    boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException; 
  }

  
  
  
  public void runSetMethodTest(SetMethodTest testMethod ) {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      Connection connection = null; 
      Connection checkConnection = null; 
      boolean passed = true;
      String sql = null; 
      String procedure = collection+"."+testMethod.getProcedureName();
      StringBuffer sb = new StringBuffer();
      sb.append("Test "+testMethod.getTestDescription()+"\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        sb.append("Connecting to " + url + "\n");
        checkConnection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);
        ResultSet checkRS = null; 
        
        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        // Setup
        try {
           sql = "DROP PROCEDURE  "+procedure;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.executeUpdate(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE PROCEDURE "+procedure+ testMethod.getProcedureDefinition();
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql);
        
        // Working path
        sql = "call "+procedure+"(?)";
        sb.append("Preparing "+sql+"\n"); 
        CallableStatement cstmt = connection
            .prepareCall(sql );
        sb.append("Calling set method 1\n"); 
        testMethod.doSetMethod(cstmt, sb, 1); 
        sb.append("...execute\n"); 
        cstmt.execute();
        checkRS = cstmt.getResultSet(); 
        checkRS.next(); 
        sb.append("..checking\n"); 
        passed = passed && testMethod.checkResult(checkRS, sb, 1);
        sb.append("..cleanup\n"); 
        connection.commit();
        
        // Seamless path
        sb.append("Ending Active Connections\n"); 
        socketProxy.endActiveConnections();
        sb.append("Calling set method 2\n"); 
        testMethod.doSetMethod(cstmt, sb, 2);
        sb.append("..execute\n"); 
        cstmt.execute();
        sb.append("..checking\n"); 
        checkRS = cstmt.getResultSet(); 
        checkRS.next(); 
        passed = passed && testMethod.checkResult(checkRS, sb, 2);

        connection.commit();

        // Within transaction path
        sb.append("Calling set method 3\n"); 
        testMethod.doSetMethod(cstmt, sb, 3); 
        sb.append("..execute\n"); 
        cstmt.execute();
        sb.append("Ending Active Connections\n"); 
        socketProxy.endActiveConnections();
        try { 
        stmt.executeQuery("Select * from sysibm.sysdummy1");
        } catch (Exception e) {
          // Ignore failure. 
        }
        connection.commit(); 
        stmt.executeQuery("Select * from sysibm.sysdummy1");
        sb.append("Ending Active Connections\n"); 
        socketProxy.endActiveConnections();
        try {
          sb.append("Calling set method 4\n"); 
          testMethod.doSetMethod(cstmt, sb, 4); 
          passed = false;
          sb.append("FAILED: Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("FAILED: Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          } else {
            sb.append("Append:  Passed throwing exception within transaction\n"); 
          }
        }
        connection.commit();

        sb.append("Ending Active Connections\n"); 
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();

        // Get connection in working state
        // Seamless path after previous failure 
        sb.append("Calling set method 5\n"); 
        testMethod.doSetMethod(cstmt, sb, 5); 
        cstmt.execute(); 
        checkRS = cstmt.getResultSet(); 
        checkRS.next(); 
        sb.append("..checking\n"); 
        passed = passed && testMethod.checkResult(checkRS, sb, 5);
        connection.commit();

        sb.append("Ending Active Connections\n"); 
        socketProxy.endActiveConnections();
        connection.commit(); 
        
        // Seamless path after previous failure 
        sb.append("Ending Active Connections\n"); 
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sb.append("Calling set method 6\n"); 
        testMethod.doSetMethod(cstmt, sb, 6); 
        sb.append("..execute\n"); 
        cstmt.execute();
        checkRS = cstmt.getResultSet();
        checkRS.next(); 
        sb.append("..checking\n"); 
        passed = passed && testMethod.checkResult(checkRS, sb, 6);
        connection.commit(); 
        
        
        // Unable to retry path
        sb.append("Setting up unable to retry path\n"); 
        sb.append("Ending Active Connections\n"); 
        socketProxy.endActiveConnections();
        stmt.executeQuery("select * from sysibm.sysdummy1"); 
        connection.commit(); 
        sb.append("Ending Active Connections\n"); 
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sb.append("Calling set method 7\n"); 
        testMethod.doSetMethod(cstmt, sb, 7); 
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();


        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();
        stmt.executeQuery("select * from sysibm.sysdummy1"); 
        connection.commit(); 
        sb.append("Ending Active Connections\n"); 
        socketProxy.endActiveConnections();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 10)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 10, Long.MAX_VALUE);
        
        try {
          sb.append("Testing unable to retry path\n"); 
          sb.append("Calling set method 8\n"); 
          testMethod.doSetMethod(cstmt, sb, 8); 
          passed = false;
          sb.append("FAILED: Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("FAILED: Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }
        socketProxy.endService();

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      } finally {
        try {
          connection.close();
        } catch (SQLException e) {
        }
        try {
          checkConnection.close();
        } catch (SQLException e) {
        } 
      }
    } else {
    }
  }


  /* Test setInt() */
  public abstract class IntProcedureDefinition implements SetMethodTest {
    public String getProcedureDefinition() { return 
        "(P1 INT) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      int answer = rs.getInt(1); 
      if (answer != i) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  public class SetIntMethodTest  extends IntProcedureDefinition {
    
    public String getProcedureName() { return "JDASSFCSMT01";}
    public String getTestDescription() { return "Test setInt()";} 
   
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setInt("P1", i); 
    }
  }

  public void Var001() {
    runSetMethodTest(new SetIntMethodTest()); 
  }

  /* Test setBoolean() */

  
  public class SetBooleanMethodTest extends IntProcedureDefinition {
    
    public String getProcedureName() { return "JDASSFCSMT02";}
    public String getTestDescription() { return "Test setBoolean()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setBoolean("P1", i % 2 == 0); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      boolean expected = i % 2 == 0; 
      boolean answer = rs.getBoolean(1); 
      if (answer != expected) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+expected+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  public void Var002() {
    runSetMethodTest(new SetBooleanMethodTest()); 
  }

  /* Test setByte() */
  public class SetByteMethodTest  extends IntProcedureDefinition {
   
    public String getProcedureName() { return "JDASSFCSMT03";}
    public String getTestDescription() { return "Test setByte()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setByte("P1", (byte) i); 
    }
  }

  public void Var003() {
    runSetMethodTest(new SetByteMethodTest()); 
  }

  /* Test setShort() */
  public class SetShortMethodTest implements SetMethodTest {
   
    public String getProcedureName() { return "JDASSFCSMT04";}
    public String getTestDescription() { return "Test setShort()";} 
    public String getProcedureDefinition() { return 
        "(P1 SMALLINT) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setShort("P1", (short) i); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      int answer = rs.getShort(1); 
      if (answer != (short) i) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  public void Var004() {
    runSetMethodTest(new SetShortMethodTest()); 
  }

  
  /* Test setLong() */
  public class SetLongMethodTest implements SetMethodTest {
   
    public String getProcedureName() { return "JDASSFCSMT05";}
    public String getTestDescription() { return "Test setLong()";} 
    public String getProcedureDefinition() { return 
        "(P1 BIGINT) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setLong("P1",  i); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      long answer = rs.getLong(1); 
      if (answer !=  i) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  public void Var005() {
    runSetMethodTest(new SetLongMethodTest()); 
  }


  /* Test setFloat() */
  public abstract class DoubleProcedureDefinition implements SetMethodTest {
    public String getProcedureDefinition() { return 
        "(P1 DOUBLE) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      double answer = rs.getDouble(1); 
      if (answer !=  i) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
    
  }
  public class SetFloatMethodTest extends DoubleProcedureDefinition {
    public String getProcedureName() { return "JDASSFCSMT06";}
    public String getTestDescription() { return "Test setFloat()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setFloat("P1",  i); 
    }
  }

  public void Var006() {
    runSetMethodTest(new SetFloatMethodTest()); 
  }

  /* Test setDouble() */
  public class SetDoubleMethodTest extends DoubleProcedureDefinition {
   
    public String getProcedureName() { return "JDASSFCSMT07";}
    public String getTestDescription() { return "Test setDouble()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setDouble("P1",  i); 
    }
  }

  public void Var007() {
    runSetMethodTest(new SetDoubleMethodTest()); 
  }

  /* Test setBigDecimal() */
  public abstract class DecimalProcedureDefinition implements SetMethodTest {
    public String getProcedureDefinition() { return 
        "(P1 DECIMAL(10,2)) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      BigDecimal expected = new BigDecimal(i); 
      expected = expected.setScale(2); 
      BigDecimal answer = rs.getBigDecimal(1); 
      if (! expected.equals(answer)) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+expected+"\n"); 
        return false; 
      }
      return true; 
    }
    
  }
  public class SetBigDecimalMethodTest extends DecimalProcedureDefinition {
   
    public String getProcedureName() { return "JDASSFCSMT08";}
    public String getTestDescription() { return "Test setBigDecimal()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setBigDecimal("P1", new BigDecimal(i)); 
    }
  }

  public void Var008() {
    runSetMethodTest(new SetBigDecimalMethodTest()); 
  }

  /* Test setString() */
  public abstract class VarcharProcedureDefinition implements SetMethodTest {
    public String getProcedureDefinition() { return 
        "(P1 VARCHAR(80)) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String expected = "STRING"+i; 
      String answer = rs.getString(1); 
      if (! expected.equals(answer)) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+expected+"\n"); 
        return false; 
      }
      return true; 
    }
    
  }
  public class SetStringMethodTest extends VarcharProcedureDefinition {
   
    public String getProcedureName() { return "JDASSFCSMT9";}
    public String getTestDescription() { return "Test setString()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setString("P1", "STRING"+i); 
    }
  }

  public void Var009() {
    runSetMethodTest(new SetStringMethodTest()); 
  }

  /* Test setObject() */
  public class SetObjectMethodTest extends DecimalProcedureDefinition {
   
    public String getProcedureName() { return "JDASSFCSMT10";}
    public String getTestDescription() { return "Test setObject()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setObject("P1", new BigDecimal(i)); 
    }
  }

  public void Var010() {
    runSetMethodTest(new SetObjectMethodTest()); 
  }

  /* Test setObject(targetType) */
  public class SetObjectTargetTypeMethodTest extends DecimalProcedureDefinition {
   
    public String getProcedureName() { return "JDASSFCSMT11";}
    public String getTestDescription() { return "Test setObject(..,targetType)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setObject("P1", new BigDecimal(i),Types.DECIMAL); 
    }
  }

  public void Var011() {
    runSetMethodTest(new SetObjectTargetTypeMethodTest()); 
  }


  /* Test setObject(targetType, scale) */
  public class SetObjectTargetTypeScaleMethodTest extends DecimalProcedureDefinition {
   
    public String getProcedureName() { return "JDASSFCSMT12";}
    public String getTestDescription() { return "Test setObject(..,targetType, scale)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setObject("P1", new BigDecimal(i),Types.DECIMAL, 2); 
    }
  }

  public void Var012() {
    runSetMethodTest(new SetObjectTargetTypeMethodTest()); 
  }

  
  
  /* Test setBytes() */
  public abstract class VarbinaryProcedureDefinition implements SetMethodTest {
    public String getProcedureDefinition() { return 
        "(P1 VARBINARY(80)) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    
  }
  public class SetBytesMethodTest extends VarbinaryProcedureDefinition {
   
    public String getProcedureName() { return "JDASSFCSMT13";}
    public String getTestDescription() { return "Test setBytes()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      byte [] bytes = new byte[1]; 
      bytes[0] = (byte) i; 
      pstmt.setBytes("P1", bytes); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      byte[] answer = rs.getBytes(1); 
      if ((answer == null) || 
          (answer.length != 1) || 
          (answer[0] != (byte) i)) {
        sb.append("FAILED: checking result: Got "+answer+" sb ["+i+"]\n"); 
        return false; 
      }
      return true; 
    }
  }

  public void Var013() {
    runSetMethodTest(new SetBytesMethodTest()); 
  }

  /* Test setDate() */
  public abstract class DateProcedureDefinition implements SetMethodTest { 
    java.sql.Date date; 
    public String getProcedureDefinition() { return 
        "(P1 DATE) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      Date answer = rs.getDate(1); 
      if (answer == null) { 
        sb.append("FAILED: checking result: Got null \n"); 
        return false; 
      }
      if (answer.equals(date)) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+date +"\n"); 
        return false; 
      }
      return true; 
    }
    
  }
  public class SetDateMethodTest extends DateProcedureDefinition {
    public String getProcedureName() { return "JDASSCPSMT14";}
    public String getTestDescription() { return "Test setDate()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      date = new java.sql.Date(3600000*24*i + System.currentTimeMillis()); 
      pstmt.setDate("P1",  date ); 
    }
  }
  
  public void Var014() {
    runSetMethodTest(new SetDateMethodTest()); 
  }

  public abstract class TimeProcedureDefinition implements SetMethodTest { 
    java.sql.Time time  = null; 
    public String getProcedureDefinition() { return 
        "(P1 TIME) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      Time answer = rs.getTime(1); 
      if (answer == null) { 
        sb.append("FAILED: checking result: Got null \n"); 
        return false; 
      }
      if (answer.equals(time)) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+time +"\n"); 
        return false; 
      }
      return true; 
    }
    
  }
  public class SetTimeMethodTest extends TimeProcedureDefinition {
    public String getProcedureName() { return "JDASSFCSMT15";}
    public String getTestDescription() { return "Test setTime()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      time = new java.sql.Time(36000*i); 
      pstmt.setTime("P1",  time ); 
    }
  }

  
  /* Test setTime() */
  public void Var015() {
    runSetMethodTest(new SetTimeMethodTest()); 
  }

  /* Test setTimestamp() */

  public abstract class TimestampProcedureDefinition implements SetMethodTest { 
    java.sql.Timestamp ts = null; 
    public String getProcedureDefinition() { return 
        "(P1 TIMESTAMP) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      Timestamp answer = rs.getTimestamp(1); 
      if (answer == null) { 
        sb.append("FAILED: checking result: Got null \n"); 
        return false; 
      }
      if (!answer.equals(ts)) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+ts +"\n"); 
        return false; 
      }
      return true; 
    }
    
  }
  public class SetTimestampMethodTest extends TimestampProcedureDefinition {
    public String getProcedureName() { return "JDASSFCSMT16";}
    public String getTestDescription() { return "Test setTimestamp()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      ts = new java.sql.Timestamp(3600000*24*i + System.currentTimeMillis()); 
      pstmt.setTimestamp("P1",  ts ); 
    }
  }

  public void Var016() {
    runSetMethodTest(new SetTimestampMethodTest());
  }


  /* Test setAsciiStreamLength() */

  public class SetAsciiStreamLengthMethodTest extends ClobProcedureDefinition {
    public String getProcedureName() { return "JDASSFCSMT17";}
    public String getTestDescription() { return "Test setAsciiStream(length)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      expected = "12345"; 
      byte[] bytesArray = { 0x31, 0x32, 0x33, 0x34, 0x35 };
      ByteArrayInputStream asciiStream = new ByteArrayInputStream(bytesArray); 
      pstmt.setAsciiStream("P1", asciiStream, 5); 
    }
  }

  public void Var017() {
    runSetMethodTest(new SetAsciiStreamLengthMethodTest());
  }


  /* Test setBinaryStream(length) */

  public class SetBinaryStreamLengthMethodTest implements SetMethodTest {
    byte[] bytesArray = { 0x31, 0x32, 0x33, 0x34, 0x35 };
    public String getProcedureName() { return "JDASSFCSMT18";}
    public String getTestDescription() { return "Test setAsciiStream(length)";} 
    public String getProcedureDefinition() { return 
        "(P1 BLOB) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      
      ByteArrayInputStream binaryStream = new ByteArrayInputStream(bytesArray); 
      pstmt.setBinaryStream("P1", binaryStream, bytesArray.length); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      boolean passed = true; 
      byte[] answer = rs.getBytes(1); 
      if (answer == null) { 
        sb.append("FAILED: checking result:  got null\n"); 
        return false; 
      }
      if (answer.length != bytesArray.length) { 
        sb.append("Error check results: legnth is "+answer.length+" sb "+bytesArray.length+"\n");
        return false; 
      }
      for (int j = 0; j < bytesArray.length; j++) { 
        if (answer[j] != bytesArray[j]) { 
          sb.append("Error check results: answer["+j+"] is "+answer[j]+" sb "+bytesArray[j]+"\n");
          passed = false; 
        }
      }
      return passed; 
    }
    
  }

  public void Var018() {
    runSetMethodTest(new SetBinaryStreamLengthMethodTest());
  }

  
  /* Test setCharacterStream() */
  
  public class SetCharacterStreamLengthMethodTest extends VarcharProcedureDefinition {
    String setString; 
    public String getProcedureName() { return "JDASSFCSMT19";}
    public String getTestDescription() { return "Test setCharacterStream(length)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
      setString = "STRING"+i;     
      StringReader reader = new StringReader(setString); 
      pstmt.setCharacterStream("P1", reader, setString.length()); 
    }
    
  }

  public void Var019() {
    runSetMethodTest(new SetCharacterStreamLengthMethodTest());
  }

  
  /* Test setDate() */
  
  public class SetDateCalMethodTest extends DateProcedureDefinition {
    public String getProcedureName() { return "JDASSCPSMT20";}
    public String getTestDescription() { return "Test setDate(cal)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      date = new java.sql.Date(3600000*24*i + System.currentTimeMillis()); 
      pstmt.setDate("P1",  date, new GregorianCalendar() ); 
    }
  }
  
  public void Var020() {
    runSetMethodTest(new SetDateCalMethodTest()); 
  }

  /* Test setTime() */

  public class SetTimeCalMethodTest extends TimeProcedureDefinition {
    public String getProcedureName() { return "JDASSFCSMT21";}
    public String getTestDescription() { return "Test setTime(cal)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      time = new java.sql.Time(36000*i); 
      pstmt.setTime("P1",  time, new GregorianCalendar() ); 
    }
  }

  
  public void Var021() {
    runSetMethodTest(new SetTimeCalMethodTest()); 
  }

  /* Test setTimestamp() */

  public class SetTimestampCalMethodTest extends TimestampProcedureDefinition {
    public String getProcedureName() { return "JDASSFCSMT22";}
    public String getTestDescription() { return "Test setTimestamp(cal)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      ts = new java.sql.Timestamp(3600000*24*i + System.currentTimeMillis()); 
      pstmt.setTimestamp("P1",  ts, new GregorianCalendar() ); 
    }
  }

  public void Var022() {
    runSetMethodTest(new SetTimestampCalMethodTest());
  }

  /* Test setNull() */

  public class SetNullMethodTest extends VarcharProcedureDefinition {
    public String getProcedureName() { return "JDASSFCSMT23";}
    public String getTestDescription() { return "Test setNull()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setNull("P1", Types.VARCHAR); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String result = rs.getString(1); 
      if (result != null) {
        sb.append("FAILED: checking results: expected null but got "+result); 
        return false; 
      }
      return true;
    }
  }


  public void Var023() {
    runSetMethodTest(new SetNullMethodTest());
  }
        

  /* Test setNull() */

  public class SetNullTypeNameMethodTest extends VarcharProcedureDefinition {
    public String getProcedureName() { return "JDASSFCSMT24";}
    public String getTestDescription() { return "Test setNull(typename)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setNull("P1", Types.VARCHAR, "VARCHAR"); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String result = rs.getString(1); 
      if (result != null) {
        sb.append("FAILED: checking results: expected null but got "+result); 
        return false; 
      }
      return true;
    }
  }


  public void Var024() {
    runSetMethodTest(new SetNullTypeNameMethodTest());
  }
        
/* SetURL */ 
  public class SetURLMethodTest extends VarcharProcedureDefinition {
    URL url = null; 
    public String getProcedureName() { return "JDASSFCSMT25";}
    public String getTestDescription() { return "Test setURL()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      try {
        url = new URL("http://www.ibm.com/index"+i+".html");
      } catch (Exception e) {
        SQLException sqlex = new SQLException(e.toString()); 
        sqlex.initCause(e);
        throw sqlex; 
      }
      pstmt.setURL("P1", url); 
    }
    
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      boolean passed = true; 
      URL answer = rs.getURL(1);
      if (answer == null) {
        sb.append("FAILED: checking results:  got null\n"); 
        return false; 
      }
      if (!answer.equals(url)) { 
        sb.append("FAILED: checking results:  answer = "+answer+" sb "+url+"\n"); 
        return false; 
      }
      return passed; 
    }
    
  }

  public void Var025() {
    runSetMethodTest(new SetURLMethodTest());
  }

  /* Test setAsciiStream() */
  public abstract class ClobProcedureDefinition implements SetMethodTest { 
     String expected = null; 
     public String getProcedureDefinition() { return 
         "(P1 CLOB) RESULT SETS 1 LANGUAGE SQL BEGIN " +
         "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
         "open c1; set result sets cursor c1; end";}
     public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
       String answer = rs.getString(1); 
       if (!expected.equals(answer)) { 
         sb.append("FAILED: checking results:  got '"+answer+"' sb '"+expected+"'\n");
         
         return false;
       }
       return true; 
     }
     
  }
  public class SetAsciiStreamMethodTest extends ClobProcedureDefinition {
    public String getProcedureName() { return "JDASSFCSMT26";}
    public String getTestDescription() { return "Test setAsciiStream()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      byte[] bytesArray = { 0x31, 0x32, 0x33, 0x34, 0x35 };
      expected="12345"; 
      ByteArrayInputStream asciiStream = new ByteArrayInputStream(bytesArray); 
      try {
        JDReflectionUtil.callMethod_V(pstmt, "setAsciiStream", "P1", asciiStream);
      } catch (Exception e) {
        SQLException sqlex =  new SQLException(e.toString());
        sqlex.initCause(e); 
        throw sqlex; 
      } 
    }
    
  }

  public void Var026() {
    runSetMethodTest(new SetAsciiStreamMethodTest());
  }


  /* Test setAsciiStreamLength(long length) */

  public class SetAsciiStreamLongLengthMethodTest extends ClobProcedureDefinition {
    public String getProcedureName() { return "JDASSFCSMT27";}
    public String getTestDescription() { return "Test setAsciiStream(longLength)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      byte[] bytesArray = { 0x31, 0x32, 0x33, 0x34, 0x35 };
      expected = "12345"; 
      ByteArrayInputStream asciiStream = new ByteArrayInputStream(bytesArray); 
      try {
        JDReflectionUtil.callMethod_V(pstmt, "setAsciiStream", "P1", asciiStream, (long) 5);
      } catch (Exception e) {
        SQLException sqlex =  new SQLException(e.toString());
        sqlex.initCause(e); 
        throw sqlex; 
      } 
    }
  }

  public void Var027() {
    runSetMethodTest(new SetAsciiStreamLongLengthMethodTest());
  }


  /* Test setBinaryStream() */
  public abstract class BlobProcedureDefinition implements SetMethodTest {
    byte[] expected = null; 
  
    public String getProcedureDefinition() { return 
        "(P1 BLOB) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      boolean passed = true; 
      byte[] answer = rs.getBytes(1); 
      if (answer == null) { 
        sb.append("FAILED: checking result:  got null\n"); 
        return false; 
      }
      if (answer.length != expected.length) { 
        sb.append("Error check results: legnth is "+answer.length+" sb "+expected.length+"\n");
        return false; 
      }
      for (int j = 0; j < expected.length; j++) { 
        if (answer[j] != expected[j]) { 
          sb.append("Error check results: answer["+j+"] is "+answer[j]+" sb "+expected[j]+"\n");
          passed = false; 
        }
      }
      return passed; 
    }
    
  }
  public class SetBinaryStreamMethodTest extends BlobProcedureDefinition {
    byte[] bytesArray = { 0x31, 0x32, 0x33, 0x34, 0x35 };
    public String getProcedureName() { return "JDASSFCSMT28";}
    public String getTestDescription() { return "Test setAsciiStream()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      expected = bytesArray; 
      ByteArrayInputStream binaryStream = new ByteArrayInputStream(bytesArray); 
      try {
        JDReflectionUtil.callMethod_V(pstmt, "setBinaryStream", "P1", binaryStream);
      } catch (Exception e) {
        SQLException sqlex =  new SQLException(e.toString());
        sqlex.initCause(e); 
        throw sqlex; 
      } 
    }
    
  }

  public void Var028() {
    runSetMethodTest(new SetBinaryStreamMethodTest());
  }

  
  /* Test setBinaryStream(long length) */

  public class SetBinaryStreamLongLengthMethodTest extends BlobProcedureDefinition {
    byte[] bytesArray = { 0x31, 0x32, 0x33, 0x34, 0x35 };
    public String getProcedureName() { return "JDASSFCSMT29";}
    public String getTestDescription() { return "Test setAsciiStream(long length)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      expected = bytesArray; 
      ByteArrayInputStream binaryStream = new ByteArrayInputStream(bytesArray); 
      try {
        JDReflectionUtil.callMethod_V(pstmt, "setBinaryStream", "P1", binaryStream, (long) bytesArray.length);
      } catch (Exception e) {
        SQLException sqlex =  new SQLException(e.toString());
        sqlex.initCause(e); 
        throw sqlex; 
      } 
    }
    
  }

  public void Var029() {
    runSetMethodTest(new SetBinaryStreamLongLengthMethodTest());
  }

  
  /* Test setBlob() */

  public class SetBlobMethodTest extends BlobProcedureDefinition {
    Blob blob = null; 
    byte[] blobBytes = null; 
    public String getProcedureName() { return "JDASSFCSMT30";}
    public String getTestDescription() { return "Test setBlob()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      blobBytes = new byte[5]; 
      for (int j = 0; j < blobBytes.length; j++) {
        blobBytes[j] = (byte)( i + j); 
      }
      expected = blobBytes; 
      blob = ((AS400JDBCConnection)pstmt.getConnection()).createBlob();
      blob.setBytes(1,blobBytes); 
      try {
        JDReflectionUtil.callMethod_V(pstmt,"setBlob", "P1", blob); 
      } catch (Exception e) {
        SQLException sqlex =  new SQLException(e.toString());
        sqlex.initCause(e); 
        throw sqlex; 
      } 
    }
    
  }

  public void Var030() {
    runSetMethodTest(new SetBlobMethodTest());
  }

  /* Test setBlob(inputStream) */

  public class SetBlobInputStreamMethodTest extends SetBlobMethodTest {
    public String getProcedureName() { return "JDASSFCSMT31";}
    public String getTestDescription() { return "Test setBlob(inputStream)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws Exception {
      blobBytes = new byte[5]; 
      for (int j = 0; j < blobBytes.length; j++) {
        blobBytes[j] = (byte)( i + j); 
      }
      JDReflectionUtil.callMethod_V(pstmt,"setBlob","P1", new ByteArrayInputStream(blobBytes)); 
    }
  }

  public void Var031() {
    runSetMethodTest(new SetBlobInputStreamMethodTest());
  }

  /* Test setBlob(inputStream) */

  public class SetBlobInputStreamLengthMethodTest extends SetBlobMethodTest {
    public String getProcedureName() { return "JDASSFCSMT32";}
    public String getTestDescription() { return "Test setBlob(inputStream, long length)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws Exception {
      blobBytes = new byte[5]; 
      for (int j = 0; j < blobBytes.length; j++) {
        blobBytes[j] = (byte)( i + j); 
      }
      JDReflectionUtil.callMethod_V(pstmt,"setBlob","P1", new ByteArrayInputStream(blobBytes), (long) blobBytes.length); 
    }
  }

  public void Var032() {
    runSetMethodTest(new SetBlobInputStreamLengthMethodTest());
  }

  
  /* Test setCharacterStream() */
  
  public class SetCharacterStreamMethodTest extends SetCharacterStreamLengthMethodTest {
    public String getProcedureName() { return "JDASSFCSMT33";}
    public String getTestDescription() { return "Test setCharacterStream()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
      setString = "STRING"+i; 
      StringReader reader = new StringReader(setString); 
      JDReflectionUtil.callMethod_V(pstmt,"setCharacterStream", "P1", reader); 
    }
  }

  public void Var033() {
    runSetMethodTest(new SetCharacterStreamMethodTest());
  }


  /* Test setCharacterStream(longLength) */
  
  public class SetCharacterStreamLongLengthMethodTest extends SetCharacterStreamLengthMethodTest {
    public String getProcedureName() { return "JDASSFCSMT34";}
    public String getTestDescription() { return "Test setCharacterStream(long Length)";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
	sb.append("..Calling setCharacterStream with 12345\n"); 
	    setString = "STRING"+i;    
      StringReader reader = new StringReader(setString); 
      JDReflectionUtil.callMethod_V(pstmt,"setCharacterStream", "P1", reader, (long) setString.length()); 
    }
  }

  public void Var034() {
    runSetMethodTest(new SetCharacterStreamLongLengthMethodTest());
  }

  public class SetClobMethodTest extends ClobProcedureDefinition {
    Clob clob = null; 
    String clobString = null; 
    public String getProcedureName() { return "JDASSFCSMT35";}
    public String getTestDescription() { return "Test setClob()";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      if (clob == null) {
          clob = ((AS400JDBCConnection)pstmt.getConnection()).createClob();
          clobString ="12345";
          clob.setString(1, clobString );  
      }
      expected = clobString; 
      pstmt.setClob(1, clob); 
    }
  }
    public void Var035() {
      runSetMethodTest(new SetClobMethodTest());
    }

    public class SetClobReaderMethodTest extends ClobProcedureDefinition {
      public String getProcedureName() { return "JDASSFCSMT36";}
      public String getTestDescription() { return "Test setClob(reader)";} 
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
        expected = "12345"; 
        JDReflectionUtil.callMethod_V(pstmt, "setClob", 1, new StringReader(expected)); 
      }
    }

    public void Var036() {
      runSetMethodTest(new SetClobReaderMethodTest());
    }

    public class SetClobReaderLengthMethodTest extends ClobProcedureDefinition {
      public String getProcedureName() { return "JDASSFCSMT37";}
      public String getTestDescription() { return "Test setClob(reader)";} 
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
        expected = "12345"; 
        JDReflectionUtil.callMethod_V(pstmt, "setClob", 1, new StringReader(expected), (long) expected.length()); 
      }
    }

    public void Var037() {
      runSetMethodTest(new SetClobReaderLengthMethodTest());
    }
  

    
    
    public class SetNCharacterStreamReaderMethodTest extends ClobProcedureDefinition {
      public String getProcedureName() { return "JDASSFCSMT38";}
      public String getTestDescription() { return "Test setNCharacterStream(reader)";} 
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
        expected = "12345"; 
        JDReflectionUtil.callMethod_V(pstmt, "setNCharacterStream", 1, new StringReader(expected)); 
      }
    }

    public void Var038() {
      runSetMethodTest(new SetNCharacterStreamReaderMethodTest());
    }

    public class SetNCharacterStreamReaderLengthMethodTest extends ClobProcedureDefinition {
      public String getProcedureName() { return "JDASSFCSMT39";}
      public String getTestDescription() { return "Test setNCharacterStream(reader, length)";} 
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
        expected = "12345"; 
        JDReflectionUtil.callMethod_V(pstmt, "setNCharacterStream", 1, new StringReader(expected), (long) expected.length()); 
      }
    }

    public void Var039() {
      runSetMethodTest(new SetNCharacterStreamReaderLengthMethodTest());
    }

    

    public class SetNClobReaderMethodTest extends ClobProcedureDefinition {
      public String getProcedureName() { return "JDASSFCSMT40";}
      public String getTestDescription() { return "Test setNClob(reader)";} 
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
        expected = "12345"; 
        JDReflectionUtil.callMethod_V(pstmt, "setNClob", 1, new StringReader(expected)); 
      }
    }

    public void Var040() {
      runSetMethodTest(new SetNClobReaderMethodTest());
    }

    public class SetNClobReaderLengthMethodTest extends ClobProcedureDefinition {
      public String getProcedureName() { return "JDASSFCSMT41";}
      public String getTestDescription() { return "Test setNClob(reader, length)";} 
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
        expected = "12345"; 
        JDReflectionUtil.callMethod_V(pstmt, "setNClob", 1, new StringReader(expected), (long) expected.length()); 
      }
    }

    public void Var041() {
      runSetMethodTest(new SetNClobReaderLengthMethodTest());
    }
  

    public class SetNStringMethodTest extends VarcharProcedureDefinition {
      
      public String getProcedureName() { return "JDASSFCSMT42";}
      public String getTestDescription() { return "Test setString()";} 
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
        pstmt.setString("P1", "STRING"+i); 
      }
    }

    public void Var042() {
      runSetMethodTest(new SetNStringMethodTest()); 
    }

    public void Var043() { notApplicable(); } 
    public void Var044() { notApplicable(); } 
    public void Var045() { notApplicable(); } 
    public void Var046() { notApplicable(); } 
    public void Var047() { notApplicable(); } 
    public void Var048() { notApplicable(); } 
    public void Var049() { notApplicable(); } 
    public void Var050() { notApplicable(); } 
    
    
    
    
    public interface GetMethodTest {
      String getProcedureName(); 
      String getTestDescription(); 
      /* The procedure definition should accept one input parameter and */
      /* return a result set containing the input parameter */ 
      String getProcedureDefinition();
      String getCallSyntax(); 
      public void doRegisterMethod(CallableStatement pstmt, StringBuffer sb) throws SQLException; 
      void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception;
      /* call the get method on the result set to check the result.  Return false if incorrect */ 
      boolean doGetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException;
      /* returns true if the get user the network */
      /* If the get does not use the network, then the get method will not fail after a disconnect */ 
      boolean getUsesNetwork(); 
    }

    
    
    
    public void runGetMethodTest(GetMethodTest testMethod ) {

      if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
        Connection connection = null; 
        boolean passed = true;
        String sql = null; 
        String procedure = collection+"."+testMethod.getProcedureName();
        StringBuffer sb = new StringBuffer();
        sb.append("Test "+testMethod.getTestDescription()+"\n");
        try {
          SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
              JDASTest.AS_DATABASE);
          int localPort = socketProxy.getPortNumber();
          Thread proxyThread = new Thread(socketProxy);
          proxyThread.start();

          String url = "jdbc:as400:localhost:" + localPort
              + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
              + ";retryIntervalForClientReroute=1";

          sb.append("Connecting to " + url + "\n");
          connection = testDriver_.getConnection(url,
              systemObject_.getUserId(), encryptedPassword_);

          sb.append("Connecting to " + url + "\n");
          
          connection.setAutoCommit(false);
          AS400JDBCStatement stmt = (AS400JDBCStatement) connection
              .createStatement();
          // Setup
          try {
             sql = "DROP PROCEDURE  "+procedure;
             cleanupSql.add(sql); 
             sb.append("Executing "+sql+"\n"); 
             stmt.executeUpdate(sql); 
          } catch (Exception e) { 
             sb.append("Ignored exception caught on "+sql); 
             printStackTraceToStringBuffer(e, sb);
          }
          sql="CREATE PROCEDURE "+procedure+ testMethod.getProcedureDefinition();
          sb.append("Executing "+sql+"\n"); 
          stmt.executeUpdate(sql);
          
          // Working path
          sql = "call "+procedure+ testMethod.getCallSyntax();
          sb.append("Preparing "+sql+"\n"); 
          CallableStatement cstmt = connection
              .prepareCall(sql );
          sb.append("Registering parameters \n"); 
          testMethod.doRegisterMethod(cstmt, sb);
          
          sb.append("Calling set method 1\n"); 
          testMethod.doSetMethod(cstmt, sb, 1); 
          sb.append("...execute\n"); 
          cstmt.execute();
          sb.append("..Calling get method 1\n"); 
          passed = passed && testMethod.doGetMethod(cstmt, sb, 1);
          sb.append("..cleanup\n"); 
          connection.commit();
          
          // Seamless path
          sb.append("Calling set method 2\n"); 
          testMethod.doSetMethod(cstmt, sb, 2);
          sb.append("..execute\n"); 
          cstmt.execute();
          sb.append("Ending Active Connections\n"); 
          socketProxy.endActiveConnections();
          // At this point, the transaction is active.  If the getMethod does network work, it should fail 
          // since there is no way to recover. 
          sb.append("..calling get method 2\n"); 
          try {
             passed = passed && testMethod.doGetMethod(cstmt, sb, 2);
             if (testMethod.getUsesNetwork()) {
               sb.append("Network call should have failed\n"); 
               passed = false; 
             }
          } catch (Exception e) { 
            String eString = e.toString();
            if (testMethod.getUsesNetwork()) {
              String expected = "connection failed but has been re-established";
              if (eString.indexOf(expected) < 0) {
                sb.append("FAILED: Did not find '" + expected + "' in '" + eString + "'");
                passed = false;
              } else {
                sb.append("Append:  Passed throwing exception within transaction\n"); 
              }
            } else { 
              sb.append("FAILED: Hit unexpected exception "+ eString + "'");
              printStackTraceToStringBuffer(e, sb); 
            }
          }
          try {
              sb.append("...calling commit to get the connection in a clean state\n");
              connection.commit();
          } catch (Exception e) { 
              sb.append("...Warning. The commit failed because the disconnection occurred\n"); 
              printStackTraceToStringBuffer(e, sb); 
          }
          // Within transaction path
          sb.append("Calling set method 3\n"); 
          testMethod.doSetMethod(cstmt, sb, 3); 
          sb.append("..execute\n"); 
          cstmt.execute();
          sb.append("..calling get method 3\n"); 
          passed = passed && testMethod.doGetMethod(cstmt, sb, 3);
          
          sb.append("Ending Active Connections\n"); 
          socketProxy.endActiveConnections();
          try { 
          stmt.executeQuery("Select * from sysibm.sysdummy1");
          } catch (Exception e) {
            // Ignore failure. 
          }
          connection.commit(); 
          stmt.executeQuery("Select * from sysibm.sysdummy1");
          sb.append("Calling set method 4\n"); 
          testMethod.doSetMethod(cstmt, sb, 4);
          sb.append("..execute\n"); 
          cstmt.execute(); 
          sb.append("...Ending Active Connections\n"); 
          socketProxy.endActiveConnections();
          try {
            sb.append("..calling get method 4\n"); 
            passed = passed && testMethod.doGetMethod(cstmt, sb, 4);
            if (testMethod.getUsesNetwork()) {
              passed = false;
              sb.append("FAILED: Did not throw exception within transaction\n");
            }
        } catch (Exception e) {
          if (testMethod.getUsesNetwork()) {
            String expected = "connection failed but has been re-established";
            String eString = e.toString();
            if (eString.indexOf(expected) < 0) {
              sb.append("FAILED: Did not find '" + expected + "' in '"
                  + eString + "'");
              passed = false;
            } else {
              sb.append("Append:  Passed throwing exception within transaction\n");
            }
          } else {
            passed = false;
            sb.append("Should not get exception\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }
          try { 
            connection.commit();
          } catch (Exception e) { 
            sb.append("Ignoring exception from commit after disconnect\n");
            printStackTraceToStringBuffer(e, sb); 
          }
          sb.append("Ending Active Connections\n"); 
          socketProxy.endActiveConnections();
          socketProxy.resetCounts();

          // Get connection in working state
          // Seamless path after previous failure 
          sb.append("Calling set method 5\n"); 
          testMethod.doSetMethod(cstmt, sb, 5); 
          sb.append("..execute\n"); 
          cstmt.execute(); 
          sb.append("..calling get method 5\n"); 
          passed = passed && testMethod.doGetMethod(cstmt, sb, 5);
          connection.commit();

          sb.append("Ending Active Connections\n"); 
          socketProxy.endActiveConnections();
          connection.commit(); 
          
          // Seamless path after previous failure 
          sb.append("Ending Active Connections\n"); 
          socketProxy.endActiveConnections();
          socketProxy.resetCounts();
          sb.append("Calling set method 6\n"); 
          testMethod.doSetMethod(cstmt, sb, 6); 
          sb.append("..execute\n"); 
          cstmt.execute();
          sb.append("..calling get method 6\n"); 
          passed = passed && testMethod.doGetMethod(cstmt, sb, 6);
          connection.commit(); 
          
          // We cannot retry the get, so retrying the gets will not fail
          socketProxy.endService();

          assertCondition(passed, sb);

        } catch (Exception e) {
          failed(e, "Unexpected Exception\n" + sb.toString());
        } finally {
          try {
            connection.close();
          } catch (SQLException e) {
          }
        }
      } else {
      }
    }

    public abstract class GetOutputOnlyMethodTest implements GetMethodTest {
      public String getCallSyntax() {  return "(?,?)";}
    }
    
    public abstract class IntOutputOnlyDefinition extends GetOutputOnlyMethodTest {
      public String getProcedureDefinition() {
        return "(IN P1 INT, OUT P2 INT) LANGUAGE SQL BEGIN SET P2 = P1; END "; 
      }
      public void doRegisterMethod(CallableStatement pstmt, StringBuffer sb) throws SQLException {
        pstmt.registerOutParameter(2, Types.INTEGER); 
      }
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
        pstmt.setInt(1, i); 
      }
      public boolean getUsesNetwork() {  return false;   }
      
    }
    public class GetIntMethodTest extends IntOutputOnlyDefinition { 
      public String getProcedureName() {  return "JDASSFCSMT51"; } 
      public String getTestDescription() {  return "test getInt()"; }  
      public boolean doGetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
        boolean passed = true; 
        int result = pstmt.getInt(2); 
        if (result != i) { 
          passed = false;
          sb.append("Got "+result+" sb "+i+"\n"); 
        }
        return passed; 
      }
    }
    
    public void Var051() {
      runGetMethodTest(new GetIntMethodTest()); 
    }

    public abstract class GetInputOutputMethodTest implements GetMethodTest {
      public String getCallSyntax() {  return "(?)";}
    }

    public abstract class IntInputOutputDefinition extends GetInputOutputMethodTest {
      public String getProcedureDefinition() {
        return "(INOUT P1 INT) LANGUAGE SQL BEGIN SET P1 = P1 + P1; END "; 
      }
      public void doRegisterMethod(CallableStatement pstmt, StringBuffer sb) throws SQLException {
        pstmt.registerOutParameter(1, Types.INTEGER); 
      }
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
        pstmt.setInt(1, i); 
      }
      public boolean getUsesNetwork() {  return false;   }
      
    }
    public class GetIntIOMethodTest extends IntInputOutputDefinition { 
      public String getProcedureName() {  return "JDASSFCSMT52"; } 
      public String getTestDescription() {  return "test getInt()"; }  
      public boolean doGetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
        boolean passed = true; 
        int result = pstmt.getInt(1); 
        if (result != 2*i) { 
          passed = false;
          sb.append("Got "+result+" sb "+(2*i)+"\n"); 
        }
        return passed; 
      }
    }
    
    public void Var052() {
      runGetMethodTest(new GetIntIOMethodTest()); 
    }

    /* getBoolean */ 
    public class GetBooleanMethodTest extends IntOutputOnlyDefinition { 
      public String getProcedureName() {  return "JDASSFCSMT53"; } 
      public String getTestDescription() {  return "test getBoolean()"; }
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
        pstmt.setBoolean(1, (i % 2 == 0 )); 
      }

      public boolean doGetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
        boolean passed = true; 
        boolean expected =  (i % 2 == 0 ); 
        boolean result = pstmt.getBoolean(2); 
        if (result != expected) { 
          passed = false;
          sb.append("Got "+result+" sb "+expected+"\n"); 
        }
        return passed; 
      }
    }
    
    public void Var053() {
      runGetMethodTest(new GetBooleanMethodTest()); 
    }

    /* getBoolean */ 
    public class GetBooleanIOMethodTest extends IntInputOutputDefinition { 
      public String getProcedureName() {  return "JDASSFCSMT54"; } 
      public String getTestDescription() {  return "test getBoolean()"; }
      public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws Exception {
        pstmt.setBoolean(1, (i % 2 == 0 )); 
      }

      public boolean doGetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
        boolean passed = true; 
        boolean expected =  (i % 2 == 0 ); 
        boolean result = pstmt.getBoolean(1); 
        if (result != expected) { 
          passed = false;
          sb.append("Got "+result+" sb "+expected+"\n"); 
        }
        return passed; 
      }
    }
    
    public void Var054() {
      runGetMethodTest(new GetBooleanIOMethodTest()); 
    }

    /* GetByte */ 
    public class GetByteMethodTest extends IntOutputOnlyDefinition { 
      public String getProcedureName() {  return "JDASSFCSMT55"; } 
      public String getTestDescription() {  return "test getByte()"; }  
      public boolean doGetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
        boolean passed = true; 
        int result = pstmt.getByte(2); 
        if (result != i) { 
          passed = false;
          sb.append("Got "+result+" sb "+i+"\n"); 
        }
        return passed; 
      }
    }
    
    public void Var055() {
      runGetMethodTest(new GetByteMethodTest()); 
    }

    public class GetByteIOMethodTest extends IntInputOutputDefinition { 
      public String getProcedureName() {  return "JDASSFCSMT56"; } 
      public String getTestDescription() {  return "test getByte()"; }  
      public boolean doGetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
        boolean passed = true; 
        int result = pstmt.getByte(1); 
        if (result != 2*i) { 
          passed = false;
          sb.append("Got "+result+" sb "+(2*i)+"\n"); 
        }
        return passed; 
      }
    }
    
    public void Var056() {
      runGetMethodTest(new GetByteIOMethodTest()); 
    }


  /* Test setBoolean() to boolean */

      public abstract class BooleanProcedureDefinition implements SetMethodTest {
    public String getProcedureDefinition() { return 
        "(P1 BOOLEAN) RESULT SETS 1 LANGUAGE SQL BEGIN " +
        "DECLARE c1 cursor for select P1 from sysibm.sysdummy1; " +
        "open c1; set result sets cursor c1; end";}
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      boolean answer = rs.getBoolean(1);
      boolean expected = i %2 == 0;  
      if (answer != expected) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+expected+" from ("+i+")\n"); 
        return false; 
      }
      return true; 
    }
  }

  
  public class SetBooleanBooleanMethodTest extends BooleanProcedureDefinition {
    
    public String getProcedureName() { return "JDASSFCSMT57";}
    public String getTestDescription() { return "Test setBoolean() to Boolean";} 
    public void doSetMethod(CallableStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setBoolean("P1", i % 2 == 0); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      boolean expected = i % 2 == 0; 
      boolean answer = rs.getBoolean(1); 
      if (answer != expected) {
        sb.append("FAILED: checking result: Got "+answer+" sb "+expected+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  public void Var057() {
    if (checkBooleanSupport()) { 
      runSetMethodTest(new SetBooleanBooleanMethodTest()); 
    }
  }

    
    
    
  /* Methods left to test */
  /*

  public byte getByte(int parameterIndex) throws SQLException {
  public short getShort(int parameterIndex) throws SQLException {
  public long getLong(int parameterIndex) throws SQLException {
  public String getString(int parameterIndex) throws SQLException {
  public float getFloat(int parameterIndex) throws SQLException {
  public double getDouble(int parameterIndex) throws SQLException {
  public BigDecimal getBigDecimal(int parameterIndex, int scale)
  public byte[] getBytes(int parameterIndex) throws SQLException {
  public Date getDate(int parameterIndex) throws SQLException {
  public Time getTime(int parameterIndex) throws SQLException {
  public Timestamp getTimestamp(int parameterIndex) throws SQLException {
  public Object getObject(int parameterIndex) throws SQLException {
  public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
  public Object getObject(int parameterIndex, Map map) throws SQLException {
  public Ref getRef(int parameterIndex) throws SQLException {
  public Blob getBlob(int parameterIndex) throws SQLException {
  public Clob getClob(int parameterIndex) throws SQLException {
  public Array getArray(int parameterIndex) throws SQLException {
  public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
  public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
  public Timestamp getTimestamp(int parameterIndex, Calendar cal)
  public void registerOutParameter(int paramIndex, int sqlType, String typeName)
  public void registerOutParameter(String parameterName, int sqlType)
  public void registerOutParameter(String parameterName, int sqlType, int scale)
  public void registerOutParameter(String parameterName, int sqlType,
  public URL getURL(int parameterIndex) throws SQLException {
  public String getString(String parameterName) throws SQLException {
  public boolean getBoolean(String parameterName) throws SQLException {
  public byte getByte(String parameterName) throws SQLException {
  public short getShort(String parameterName) throws SQLException {
  public int getInt(String parameterName) throws SQLException {
  public long getLong(String parameterName) throws SQLException {
  public float getFloat(String parameterName) throws SQLException {
  public double getDouble(String parameterName) throws SQLException {
  public byte[] getBytes(String parameterName) throws SQLException {
  public Date getDate(String parameterName) throws SQLException {
  public Time getTime(String parameterName) throws SQLException {
  public Timestamp getTimestamp(String parameterName) throws SQLException {
  public Object getObject(String parameterName) throws SQLException {
  public BigDecimal getBigDecimal(String parameterName) throws SQLException {
  public Object getObject(String parameterName, Map map) throws SQLException {
  public Ref getRef(String parameterName) throws SQLException {
  public Blob getBlob(String parameterName) throws SQLException {
  public Clob getClob(String parameterName) throws SQLException {
  public Array getArray(String parameterName) throws SQLException {
  public Date getDate(String parameterName, Calendar cal) throws SQLException {
  public Time getTime(String parameterName, Calendar cal) throws SQLException {
  public Timestamp getTimestamp(String parameterName, Calendar cal)
  public URL getURL(String parameterName) throws SQLException {
  public Reader getCharacterStream(int parameterIndex) throws SQLException {
  public Reader getCharacterStream(String parameterName) throws SQLException {
  public Reader getNCharacterStream(int parameterIndex) throws SQLException {
  public Reader getNCharacterStream(String parameterName) throws SQLException {
  public String getNString(int parameterIndex) throws SQLException {
  public String getNString(String parameterName) throws SQLException {
  public Object getObject(String parameterName, Class type)
  public Object getObject(int parameter, Class type)

  public void registerOutParameter(int parameterIndex, int sqlType)
  public void registerOutParameter(int parameterIndex, int sqlType, int scale)
  public boolean wasNull() throws SQLException {



JDBC40       
  public void setNClob(String parameterName, NClob clob) throws SQLException {
  public void setRowId(String parameterName, RowId x) throws SQLException {
  public void setSQLXML(String parameterName, SQLXML xml) throws SQLException {
    public NClob getNClob(int parameterIndex) throws SQLException {
  public NClob getNClob(String parameterName) throws SQLException {
  public RowId getRowId(int parameterIndex) throws SQLException {
  public RowId getRowId(String parameterName) throws SQLException {
  public SQLXML getSQLXML(int parameterIndex) throws SQLException {
  public SQLXML getSQLXML(String parameterName) throws SQLException {

JDBC 42 
  public void setObject(String parameterName,
                         Object x,
                         SQLType  
                         targetSqlType,
                         int scaleOrLength)                    throws SQLException

 public void setObject(String parameterName,
                         Object x,
                         SQLType  
                         targetSqlType)
                  throws SQLException


  public void registerOutParameter(int parameterIndex,
      SQLType  
                                    sqlType)
                             throws SQLException{
  
  public void registerOutParameter(int parameterIndex,
      SQLType  
                                    sqlType,
                                    int scale)
                             throws SQLException {

  public void registerOutParameter(int parameterIndex,
      SQLType  
                                    sqlType,
                                    String typeName)
                             throws SQLException {
 public void registerOutParameter(String parameterName,
     SQLType  
                                    sqlType)

public void registerOutParameter(String parameterName,
    SQLType  
                                    sqlType,
                                    int scale)
                             throws SQLException {

public void registerOutParameter(String parameterName,
    SQLType  
                                    sqlType,
                                    String typeName)
                             throws SQLException

   
   
   */

}
