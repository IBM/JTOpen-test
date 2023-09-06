///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASClientRegisters.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: JDASClientRegisters.java
//  Classes:   JDASClientRegisters
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test;


import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Properties;
import java.sql.*;

import com.ibm.as400.access.AS400;

/**
 * Testcase JDASClientRegisters
 **/
public class JDASClientRegisters extends JDASTestcase {

  private String systemName;
  private String password; 

  /**
   * Constructor. This is called from the AS400JDBCEnableCALTest constructor.
   **/
  public JDASClientRegisters(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASClientRegisters", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }

  public void setup() {
    systemName = systemObject_.getSystemName(); 
    password="PasswordLeak.JDASClientRegisters"; 
    password = new String(PasswordVault.decryptPassword(encryptedPassword_)); 

  }

  /**
   * @exception Exception
   *              If an exception occurs.
   **/
  public void cleanup() throws Exception {
  }

  
  private static Properties buildProperties(String propertyString) throws SQLException {
    Properties properties = new Properties();
    int length = propertyString.length();  
    int startIndex = 0; 
    int equalsIndex = propertyString.indexOf('=', startIndex);
    int commaIndex = propertyString.indexOf(',', startIndex);
    while (equalsIndex > 0) {
      if (commaIndex == -1) {
        commaIndex = length; 
      }
      String key = propertyString.substring(startIndex,equalsIndex).trim(); 
      String value = propertyString.substring(equalsIndex+1,commaIndex).trim(); 
      properties.setProperty(key, value); 
      startIndex = commaIndex + 1;
      if (startIndex < length) { 
        equalsIndex = propertyString.indexOf('=', startIndex);
        commaIndex = propertyString.indexOf(',', startIndex);
      } else {
        equalsIndex = -1; 
      }
    }
        
    return properties; 
  }

  /**
   * Test the supplied test script
   **/
  public void testScript(String[][] testScript) {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      try {
        SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort1 = socketProxy1.getPortNumber();
        Thread proxyThread1 = new Thread(socketProxy1);
        proxyThread1.start();

        SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7011, systemName,
            JDASTest.AS_DATABASE);
        int localPort2 = socketProxy2.getPortNumber();
        Thread proxyThread2 = new Thread(socketProxy2);
        proxyThread2.start();

        String url = "jdbc:as400:localhost:"
            + localPort1
            + ";enableClientAffinitiesList=1;"
            + "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
            + localPort2;
        Connection connection = null; 
        Statement stmt = null; 
        long startTime = System.currentTimeMillis(); 
        for (int i = 0; i < testScript.length; i++) {
          String[] entry = testScript[i];
          String command = entry[0].trim();
          double elaspedSeconds = (System.currentTimeMillis() - startTime) / 1000.0; 
          sb.append("Executing at "+elaspedSeconds+": "+command+"\n"); 
          String expected = entry[1];
          if (expected != null) {
            expected = expected.trim(); 
          }
          try {
            // Some kind of command
            if (command.indexOf("!") == 0) {
              if (command.indexOf("!CONNECT") == 0) {
                sb.append("Connecting to " + url + "\n");
                if (url.indexOf("password") < 0) { 
                connection = testDriver_.getConnection(url,
                    systemObject_.getUserId(), encryptedPassword_);
                } else {
                  connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
                }
                stmt = connection.createStatement(); 
              } else if (command.indexOf("!ADDPROPERTY") == 0) {
                String property = command.substring(12).trim(); 
                url = url +";"+property;
              } else if (command.indexOf("!ENABLE") == 0)   {
                int proxy = Integer.parseInt(command.substring(7).trim()); 
                switch(proxy) {
                case 1:
                  socketProxy1.enable(true);
                  break; 
                case 2:
                  socketProxy2.enable(true);
                  break; 
                  default:
                    passed = false; 
                    sb.append("Could not process command "+command+"\n"); 
                }
              } else if (command.indexOf("!DISABLE") == 0)   {
                int proxy = Integer.parseInt(command.substring(8).trim()); 
                switch(proxy) {
                case 1:
                  socketProxy1.enable(false);
                  break; 
                case 2:
                  socketProxy2.enable(false);
                  break; 
                  default:
                    passed = false; 
                    sb.append("Could not process command "+command+"\n"); 
                }
              } else if (command.indexOf("!ENDACTIVECONNECTIONS") == 0)   {
                int proxy = Integer.parseInt(command.substring(21).trim()); 
                switch(proxy) {
                case 1:                
                  socketProxy1.endActiveConnections(); 
                  break; 
                case 2:
                  socketProxy2.endActiveConnections();
                  break; 
                default:
                    passed = false; 
                    sb.append("Could not process command "+command+"\n"); 
                }
              } else if (command.indexOf("!CHECKTABLE") == 0)   {
                String tablename = command.substring(11).trim(); 
                String checkUrl = "jdbc:as400:"+systemName+";query timeout mechanism=cancel"; 
                
                Connection checkConnection = testDriver_.getConnection(checkUrl,
                    systemObject_.getUserId(), encryptedPassword_);
                checkConnection.setAutoCommit(false); 
                checkConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                Statement checkStmt = checkConnection.createStatement(); 
                checkStmt.execute("CALL QSYS2.QCMDEXC('CHGJOB DFTWAIT(2)')"); 
                String result =  null; 
                try {
                  checkStmt.setQueryTimeout(5); 
                  ResultSet rs = checkStmt.executeQuery("SELECT * FROM "+tablename); 
                  if (rs.next()) {
                    result = rs.getString(1); 
                  }
                } catch (SQLException e) { 
                  result=e.toString(); 
                }
                
                if (result == null) {
                   if (expected != null) { 
                     passed = false; 
                     sb.append("ERROR:  Got null sb '"+expected+"'\n"); 
                   }
                } else if (result.indexOf(expected)< 0) {
                  passed = false; 
                  sb.append("ERROR:  Got '"+result+"' sb '"+expected+"'\n"); 
                }
                
                checkConnection.commit(); 
                checkConnection.close(); 
                    
              } else if (command.indexOf("!CALLMETHOD") == 0)   {
                String remaining = command.substring(11).trim(); 
                int spaceIndex = remaining.indexOf(' '); 
                String methodName = null ;
                if (spaceIndex > 0) { 
                  methodName = remaining.substring(0,spaceIndex); 
                  remaining = remaining.substring(spaceIndex+1).trim();
                } else {
                    methodName = remaining; 
                    remaining=""; 
                }
                  
                  Object methodResult = null; 
                  if (methodName.equals("setAutoCommit")) {
                    remaining = remaining.toLowerCase();
                    connection.setAutoCommit(getBoolean(remaining)); 
                  } else if (methodName.equals("commit")) {
                    connection.commit(); 
                  } else if (methodName.equals("setCatalog")) {
                    connection.setCatalog(remaining); 
                  } else if (methodName.equals("setClientInfo")) {
                    // * for client info -- properties are set like key=value and separated by commas
                    // If a comma is used, then the properties version of the method is used. 
                    // Otherwise the single method version is used. 
                    int commaIndex = remaining.indexOf(','); 
                    if (commaIndex < 0) {
                        int equalsIndex = remaining.indexOf('='); 
                        if (equalsIndex > 0) {
                          JDReflectionUtil.callMethod_V(connection,"setClientInfo",remaining.substring(0,equalsIndex).trim(),
                              remaining.substring(equalsIndex+1).trim()); 
                        } else {
                          sb.append("Did not find = in '" + command + "'\n");
                          passed =false; 
                        }
                    } else {
                      JDReflectionUtil.callMethod_V(connection,"setClientInfo",buildProperties(remaining)); 
                    }
                  //  setHoldability(int holdability)
                  } else if (methodName.equals("setHoldability")) {
                    connection.setHoldability(Integer.parseInt(remaining)); 
                     
                  //  setNetworkTimeout(int milliseconds)
                  } else if (methodName.equals("setNetworkTimeout")) {
                    JDReflectionUtil.callMethod_V(connection,"setNetworkTimeout",Integer.parseInt(remaining)); 
                    
                  //  setReadOnly(boolean readOnly)
                  } else if (methodName.equals("setReadOnly")) {
                    connection.setReadOnly(getBoolean(remaining)); 
                    
                  //  setSchema(String schema)
                  } else if (methodName.equals("setSchema")) {
                    JDReflectionUtil.callMethod_V( connection, "setSchema", remaining);

                    
                  //  setTransactionIsolation(int level)
                  } else if (methodName.equals("setTransactionIsolation")) {
                    int transactionIsolation = 0; 
                    if (remaining.equals("READ_UNCOMMITTED")) {
                      transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED; 
                    } else if (remaining.equals("READ_COMMITTED")) {
                        transactionIsolation = Connection.TRANSACTION_READ_COMMITTED; 
                    } else if (remaining.equals("REPEATABLE_READ")) {
                      transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ; 
                    } else if (remaining.equals("SERIALIZABLE")) {
                      transactionIsolation = Connection.TRANSACTION_SERIALIZABLE; 
                    } else {
                      transactionIsolation = Integer.parseInt(remaining); 
                      if (transactionIsolation == 0) {
                        sb.append("Invalid transaction isolation from "+remaining);
                        passed = false; 
                      }
                    }
                    connection.setTransactionIsolation(transactionIsolation); 
                    
                  } else if (methodName.equals("getHoldability")) {
                    methodResult = "" + connection.getHoldability(); 
                  } else if (methodName.equals("isReadOnly")) {
                    methodResult = "" + connection.isReadOnly(); 
                  } else if (methodName.equals("getNetworkTimeout")) {
                    methodResult = "" + JDReflectionUtil.callMethod_I(connection,"getNetworkTimeout"); 
                  } else if (methodName.equals("getTransactionIsolation")) {
                    methodResult = "" + connection.getTransactionIsolation(); 
                    //  setTypeMap(Map map)     
                  } else if (methodName.equals("setTypeMap")) {
                    sb.append("Did not understand '"+methodName+"' in '" + command + "'\n");
                    passed =false; 
                  
                  } else {
                    sb.append("Did not understand '"+methodName+"' in '" + command + "'\n");
                    passed =false; 
                    
                  }
                  // Check the result of the method
                  if (expected == null) { 
                    if (methodResult != null) {
                      passed = false; 
                      sb.append("Expected null but got "+methodResult); 
                    }
                  } else {
                    if (!expected.equals(methodResult)) {
                      passed = false;
                      sb.append("From '" + command + "' got '" + methodResult
                          + "' expected '" + expected + "'\n");
                    } 
                  }
                

              } else {
                passed = false;
                sb.append("Did not understand command '" + command + "'\n");
              }

            } else { /* NOT A COMMAND */

              boolean rsAvailable = stmt.execute(command);
              if (rsAvailable) {
                ResultSet rs = stmt.getResultSet();
                rs.next();
                String s = rs.getString(1);
                if (s != null) {
                  s = s.trim(); 
                }
                rs.close();
                if (expected == null) {
                  passed = false;
                  sb.append("From '" + command + "' got unexpected " + s +"\n");

                } else {
                  if (!expected.equals(s)) {
                    passed = false;
                    sb.append("From '" + command + "' got '" + s
                        + "' expected '" + expected + "'\n");
                  }
                }

              } else {
                if (expected != null) {
                  passed = false;
                  sb.append("For '" + command + "' expected " + expected + "\n");
                }
              }
            }

          } catch (SQLException sqlex) {
            if (expected != null && expected.indexOf("EXCEPTION:") == 0) {
              expected = expected.substring(10).trim();
              String message = sqlex.getMessage();
              if (message.indexOf(expected) < 0) {
                passed = false;
                sb.append("For '" + command + "' expected exception '"
                    + expected + "' but got \n");
                printStackTraceToStringBuffer(sqlex, sb);
              }
            } else {
              passed = false;
              if (expected == null)  {
                  sb.append("For '" + command + "' got unexpected exception\n");
              } else {
                sb.append("For '" + command + "' expected '"+expected+"' but got unexpected exception\n");

              } 
              
              printStackTraceToStringBuffer(sqlex, sb);
            }
          }
        }

        if (connection != null) {
          connection.close();
        }
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    }
  }


  
  /* 
   * Set of tests to ensure that properties are preserved. The following properties are 
   * tested. 
   * 
   *   decfloat rounding mode
   *   naming 
   *   libraries
   *   date format
   *   date separator
   *   decimal separator
   *   time format
   *   time separator
   *       
   */
  

  

  private boolean getBoolean(String remaining) {
    String s = remaining.toLowerCase().trim(); 
    if ("true".equals(s)) {
      return true; 
    }
    return false;
  }

  /**
   * Test saving of DECFLOAT rounding mode 
   **/
  public void Var001() {
    String[][] testScript = {
        {"!ADDPROPERTY decfloat rounding mode=ceiling", null}, 
        {"!CONNECT", null}, 
        {"VALUES CURRENT DECFLOAT ROUNDING MODE", "ROUND_CEILING"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT DECFLOAT ROUNDING MODE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DECFLOAT ROUNDING MODE", "ROUND_CEILING"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT DECFLOAT ROUNDING MODE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DECFLOAT ROUNDING MODE", "ROUND_CEILING"},
        {"SET CURRENT DECFLOAT ROUNDING MODE ROUND_HALF_EVEN", null}, 
        {"VALUES CURRENT DECFLOAT ROUNDING MODE", "ROUND_HALF_EVEN"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT DECFLOAT ROUNDING MODE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DECFLOAT ROUNDING MODE", "ROUND_HALF_EVEN"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT DECFLOAT ROUNDING MODE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DECFLOAT ROUNDING MODE", "ROUND_HALF_EVEN"},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }

  
  /**
   * Test saving of NAMING  
   **/
  public void Var002() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"create or replace table "+collection_+".JDASCRG2 (c1 int)",null},
        {"insert into "+collection_+".JDASCRG2 VALUES(99)", null}, 
        {"SELECT * FROM "+collection_+"/JDASCRG2", "EXCEPTION:not valid"},
        {"!ADDPROPERTY naming=system", null}, 
        {"!CONNECT", null}, 
        {"SELECT * FROM "+collection_+"/JDASCRG2", "99"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+"/JDASCRG2", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+"/JDASCRG2", "99"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+"/JDASCRG2", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+"/JDASCRG2", "99"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+"/JDASCRG2", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+"/JDASCRG2", "99"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+"/JDASCRG2", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+"/JDASCRG2", "99"},
        {"drop table "+collection_+".JDASCRG2 ",null},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }

  /**
   * Test saving of LIBRARIES  
   **/
  public void Var003() {
    String[][] testScript = {
        {"!ADDPROPERTY naming=system", null}, 
        {"!ADDPROPERTY libraries=QGPL,"+collection_, null}, 
        {"!CONNECT", null},
        {"create or replace table "+collection_+".JDASCRG3 (c1 int)",null},
        {"insert into "+collection_+".JDASCRG3 VALUES(99)", null}, 
        {"SELECT * FROM "+collection_+"/JDASCRG3", "99"},
        {"SELECT * FROM JDASCRG3", "99"},
        {"VALUES CURRENT SCHEMA", "*LIBL" }, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM JDASCRG3", "EXCEPTION:re-established"},
        {"SELECT * FROM JDASCRG3", "99"},
        {"VALUES CURRENT SCHEMA", "*LIBL" }, 
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM JDASCRG3", "EXCEPTION:re-established"},
        {"SELECT * FROM JDASCRG3", "99"},
        {"VALUES CURRENT SCHEMA", "*LIBL" }, 
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM JDASCRG3", "EXCEPTION:re-established"},
        {"SELECT * FROM JDASCRG3", "99"},
        {"VALUES CURRENT SCHEMA", "*LIBL" }, 
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM JDASCRG3", "EXCEPTION:re-established"},
        {"SELECT * FROM JDASCRG3", "99"},
        {"drop table "+collection_+".JDASCRG3 ",null},
        {"VALUES CURRENT SCHEMA", "*LIBL" }, 
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }

  /**
   * Test saving of DATE FORMAT   
   **/
  public void Var004() {
    String[][] testScript = {
        {"!ADDPROPERTY date format=dmy", null}, 
        {"!CONNECT", null},
        {"create or replace table "+collection_+".JDASCRG4 (c1 date)",null},
        {"insert into "+collection_+".JDASCRG4 VALUES('31/12/17')", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG4", "31/12/17"},
        {"SELECT * FROM "+collection_+".JDASCRG4", "31/12/17"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG4", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG4", "31/12/17"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG4", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG4", "31/12/17"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG4", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG4", "31/12/17"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG4", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG4", "31/12/17"},
        {"drop table "+collection_+".JDASCRG4 ",null},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }

  
  /**
   * Test saving of DATE SEPARATOR   
   **/
  public void Var005() {
    String[][] testScript = {
        {"!ADDPROPERTY date format=dmy", null}, 
        {"!ADDPROPERTY date separator=b", null}, 
        {"!CONNECT", null},
        {"create or replace table "+collection_+".JDASCRG5 (c1 date)",null},
        {"insert into "+collection_+".JDASCRG5 VALUES('31 12 17')", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG5", "31 12 17"},
        {"SELECT * FROM "+collection_+".JDASCRG5", "31 12 17"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG5", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG5", "31 12 17"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG5", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG5", "31 12 17"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG5", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG5", "31 12 17"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG5", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG5", "31 12 17"},
        {"drop table "+collection_+".JDASCRG5 ",null},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }

  /**
   * Test saving of DECIMAL SEPARATOR   
   **/
  public void Var006() {
    String[][] testScript = {
        {"!ADDPROPERTY decimal separator=,", null}, 
        {"!CONNECT", null},
        {"create or replace table "+collection_+".JDASCRG6 (c1 decimal(10, 4))",null},
        {"insert into "+collection_+".JDASCRG6 VALUES(31,1217)", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG6", "31,1217"},
        {"SELECT * FROM "+collection_+".JDASCRG6", "31,1217"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG6", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG6", "31,1217"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG6", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG6", "31,1217"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG6", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG6", "31,1217"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG6", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG6", "31,1217"},
        {"drop table "+collection_+".JDASCRG6 ",null},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }

  /**
   * Test saving of TIME FORMAT   
   **/
  public void Var007() {
    String[][] testScript = {
        {"!ADDPROPERTY time format=usa", null}, 
        {"!CONNECT", null},
        {"create or replace table "+collection_+".JDASCRG7 (c1 time)",null},
        {"insert into "+collection_+".JDASCRG7 VALUES('11:44 PM')", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG7", "11:44 PM"},
        {"SELECT * FROM "+collection_+".JDASCRG7", "11:44 PM"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG7", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG7", "11:44 PM"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG7", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG7", "11:44 PM"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG7", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG7", "11:44 PM"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG7", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG7", "11:44 PM"},
        {"drop table "+collection_+".JDASCRG7 ",null},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }


  /**
   * Test saving of TIME SEPARATOR   
   **/
  public void Var008() {
    String[][] testScript = {
        {"!ADDPROPERTY time format=hms", null}, 
        {"!ADDPROPERTY time separator=b", null}, 
        {"!CONNECT", null},
        {"create or replace table "+collection_+".JDASCRG7 (c1 time)",null},
        {"insert into "+collection_+".JDASCRG7 VALUES('11 44 33')", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG7", "11 44 33"},
        {"SELECT * FROM "+collection_+".JDASCRG7", "11 44 33"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG7", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG7", "11 44 33"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG7", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG7", "11 44 33"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG7", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG7", "11 44 33"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"SELECT * FROM "+collection_+".JDASCRG7", "EXCEPTION:re-established"},
        {"SELECT * FROM "+collection_+".JDASCRG7", "11 44 33"},
        {"drop table "+collection_+".JDASCRG7 ",null},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }



  /* 
   * Set of tests to ensure that registers are preserved. 
   * The following registers are tested. 
   * 
   *  CURRENT CLIENT_ACCTNG      -- changed using SYSPROC.WLM_SET_CLIENT_INFO procedure
   *  CURRENT CLIENT_APPLNAME    -- changed using SYSPROC.WLM_SET_CLIENT_INFO procedure
   *  CURRENT CLIENT_PROGRAMID   -- changed using SYSPROC.WLM_SET_CLIENT_INFO procedure
   *  CURRENT CLIENT_USERID      -- changed using SYSPROC.WLM_SET_CLIENT_INFO procedure 
   *  CURRENT CLIENT_WRKSTNNAME  -- changed using SYSPROC.WLM_SET_CLIENT_INFO procedure 
   *  CURRENT DEBUG MODE         -- SET CURRENT DEBUG MODE DISALLOW |  ALLOW | DISABLE 
   *  CURRENT DECFLOAT ROUNDING MODE -- tested above with JDBC property  
   *  CURRENT DEGREE             -- SET CURRENT DEGREE 1 | n | ANY | NONE | MAX | IO 
   *  CURRENT IMPLICIT XMLPARSE OPTION -- SET ...  = PRESERVE WHITESPACE | STRIP WHITESPACE 
   *  CURRENT PATH               -- SET CURRENT PATH / SET PATH   
   *  CURRENT SCHEMA             -- SET SCHEMA / SET CURRENT SCHEMA  / SET CURRENT_SCHEMA
   *  CURRENT SERVER             -- CONNECT TO ______ 
   *  CURRENT TEMPORAL SYSTEM_TIME -- SET CURRENT ..  
   *  CURRENT USER -- not changable  
   *  SESSION_USER -- SET SESSION AUTHORIZATION 
   *  SYSTEM_USER -- not changable 
   *  USER  -- SET SESSION AUTHORIZATION
   */
  /**
   * Test saving of CURRENT CLIENT_*  
   **/
  public void Var009() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"CALL SYSPROC.WLM_SET_CLIENT_INFO('AUSER','AWRKSTNNAME','APPLNAME','X6782847','WORKLOAD')", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},

        {"CALL SYSPROC.WLM_SET_CLIENT_INFO('BUSER','BWRKSTNNAME','BAPPLNAME','BX6782847','BWORKLOAD')", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "BX6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "BAPPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "BWORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "BUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "BWRKSTNNAME"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "BX6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "BAPPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "BWORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "BUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "BWRKSTNNAME"},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }

  /**
   * Test saving of CURRENT DEBUG MODE  
   **/
  public void Var010() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"SET CURRENT DEBUG MODE ALLOW", null}, 
        {"VALUES CURRENT DEBUG MODE", "ALLOW"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT DEBUG MODE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DEBUG MODE", "ALLOW"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT DEBUG MODE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DEBUG MODE", "ALLOW"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT DEBUG MODE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DEBUG MODE", "ALLOW"},

        {"SET CURRENT DEBUG MODE DISABLE", null}, 
        {"VALUES CURRENT DEBUG MODE", "DISABLE"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT DEBUG MODE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DEBUG MODE", "DISABLE"},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }


  /**
   * Test saving of CURRENT DEGREE  
   **/
  public void Var011() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"SET CURRENT DEGREE = '2'", null}, 
        {"VALUES CURRENT DEGREE", "2"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT DEGREE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DEGREE", "2"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT DEGREE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DEGREE", "2"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT DEGREE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DEGREE", "2"},

        {"SET CURRENT DEGREE='IO'", null}, 
        {"VALUES CURRENT DEGREE", "IO"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT DEGREE", "EXCEPTION:re-established"},
        {"VALUES CURRENT DEGREE", "IO"},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }

  
  /**
   * Test saving of CURRENT IMPLICIT XMLPARSE OPTION  
   **/
  public void Var012() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"SET CURRENT IMPLICIT XMLPARSE OPTION PRESERVE WHITESPACE", null}, 
        {"VALUES CURRENT IMPLICIT XMLPARSE OPTION", "PRESERVE WHITESPACE"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT IMPLICIT XMLPARSE OPTION", "EXCEPTION:re-established"},
        {"VALUES CURRENT IMPLICIT XMLPARSE OPTION", "PRESERVE WHITESPACE"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT IMPLICIT XMLPARSE OPTION", "EXCEPTION:re-established"},
        {"VALUES CURRENT IMPLICIT XMLPARSE OPTION", "PRESERVE WHITESPACE"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT IMPLICIT XMLPARSE OPTION", "EXCEPTION:re-established"},
        {"VALUES CURRENT IMPLICIT XMLPARSE OPTION", "PRESERVE WHITESPACE"},

        {"SET CURRENT IMPLICIT XMLPARSE OPTION STRIP WHITESPACE ", null}, 
        {"VALUES CURRENT IMPLICIT XMLPARSE OPTION", "STRIP WHITESPACE"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT IMPLICIT XMLPARSE OPTION", "EXCEPTION:re-established"},
        {"VALUES CURRENT IMPLICIT XMLPARSE OPTION", "STRIP WHITESPACE"},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }
  

  

  /**
   * Test saving of CURRENT PATH  
   **/
  public void Var013() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"SET CURRENT PATH "+collection_, null}, 
        {"VALUES CURRENT PATH", "\""+collection_+"\""},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT PATH", "EXCEPTION:re-established"},
        {"VALUES CURRENT PATH", "\""+collection_+"\""},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT PATH", "EXCEPTION:re-established"},
        {"VALUES CURRENT PATH", "\""+collection_+"\""},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT PATH", "EXCEPTION:re-established"},
        {"VALUES CURRENT PATH", "\""+collection_+"\""},

        {"SET CURRENT PATH QGPL ", null}, 
        {"VALUES CURRENT PATH", "\"QGPL\""},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT PATH", "EXCEPTION:re-established"},
        {"VALUES CURRENT PATH", "\"QGPL\""},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT PATH", "EXCEPTION:re-established"},
        {"VALUES CURRENT PATH", "\"QGPL\""},

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT PATH", "EXCEPTION:re-established"},
        {"SET PATH QSYS,QSYS2,SYSPROC,SYSIBMADM", null}, 
        {"VALUES CURRENT PATH", "\"QSYS\",\"QSYS2\",\"SYSPROC\",\"SYSIBMADM\""},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT PATH", "EXCEPTION:re-established"},
        {"VALUES CURRENT PATH", "\"QSYS\",\"QSYS2\",\"SYSPROC\",\"SYSIBMADM\""},
        

        
    };
    testScript(testScript);

  }

  
  
  /**
   * Test saving of CURRENT SCHEMA
   **/
  public void Var014() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"SET CURRENT SCHEMA "+collection_, null}, 
        {"VALUES CURRENT SCHEMA", collection_},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", collection_},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", collection_},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", collection_},

        {"SET CURRENT SCHEMA QGPL ", null}, 
        {"VALUES CURRENT SCHEMA", "QGPL"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"SET SCHEMA SYSIBMADM", null}, 
        {"VALUES CURRENT SCHEMA", "SYSIBMADM"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "SYSIBMADM"},
        

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"SET CURRENT_SCHEMA QGPL", null}, 
        {"VALUES CURRENT SCHEMA", "QGPL"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},

        
    };
    testScript(testScript);

  }


  // Is the specified server valid for this test? 
  public boolean validServer(String server) {
    Connection testConnection = null;
    String sql = "" ;
    try {
	sql= "GET CONNECTION to LOCAL SYSTEM";
     testConnection = testDriver_.getConnection("jdbc:as400:"+systemName,systemObject_.getUserId(), encryptedPassword_);
      Statement stmt = testConnection.createStatement();
      sql = "CONNECT TO "+server+" USER "+systemObject_.getUserId()+" USING '"+password+"'";
      stmt.execute(sql);
      sql = "VALUES CURRENT SERVER";
      ResultSet rs = stmt.executeQuery(sql); 
      rs.next(); 
      String currentServer = rs.getString(1);
      if (server.equals(currentServer)) {
        return true; 
      } else {
	  System.out.println("Warning: Invalid server : RDB("+server+") != CURRENT SERVER("+currentServer+")"); 
	      return false;
      }
    } catch (Exception e) {
      System.out.println(" Warning server "+server+" had exception "+e+" sql="+sql); 
      return false; 
    } finally {
      if (testConnection != null) { 
        try { 
          testConnection.close();
        } catch (Exception e) {
        
        }
      }
    }
    
  }
  /**
   * Test saving of CURRENT SERVER
   **/
  public void Var015() {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) { 
    String server1 = null; 
    String server2 = null; 
    String server3 = null;
    String query = "SELECT TRIM(DBXRDBN) FROM qsys.qadbxrdbd  WHERE "+
            "UPPER(TRIM(DBTXRLC)) <> 'LOOPBACK' "+
            "AND UPPER(DBTXRLC) not like '%SVL.IBM.COM' "+
            "AND UPPER(DBTXRLC) not like '%::FFFF%' " +
            "AND UPPER(DBTXRLC) like '%SQ7%' " +
            "AND UPPER(DBTXRLC) not like '%RCHLAND%' ";

    try { 
      // Find two RDBENTRIES that are usable.
      System.out.println("  Var015 getting conection using jdbc:as400:"+systemName+" , " +systemObject_.getUserId()); 
      Connection connection = testDriver_.getConnection("jdbc:as400:"+systemName,systemObject_.getUserId(), encryptedPassword_);
      Statement stmt = connection.createStatement(); 
      System.out.println("  Var015 running query"); 
      ResultSet rs = stmt.executeQuery(query); 
      System.out.println("  Var015 fetching query"); 

     while (rs.next() && (server3 == null)) {
        String server = rs.getString(1); 
        if (validServer(server)) { 
           if (server1 == null) {
             server1 = server;
           } else if (server2 == null) { 
             server2 = server; 
           } else if (server3 == null) { 
             server3 = server; 
           }
        }
      }
      connection.close(); 
    
    } catch (Exception e) {
      failed(e, "setup failed"); 
      return; 
    }
    if (server1 == null) { 
      failed("Unable to find 1 server to use in QADBXRDBD using "+query); 
      return; 
      
    }
    if (server2 == null) { 
      failed("Unable to find 2 servers to use in QADBXRDBD using "+query); 
      return; 
      
    }

    if (server3 == null) { 
      failed("Unable to find 3 servers to use in QADBXRDBD using "+query); 
      return; 
      
    }
    String[][] testScript = {
        {"!CONNECT", null},
        {"CONNECT TO "+server1+" USER "+systemObject_.getUserId()+" USING '"+password+"'", null}, 
        {"VALUES CURRENT SERVER", server1},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SERVER", "EXCEPTION:re-established"},
        {"VALUES CURRENT SERVER", server1},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SERVER", "EXCEPTION:re-established"},
        {"VALUES CURRENT SERVER", server1},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SERVER", "EXCEPTION:re-established"},
        {"VALUES CURRENT SERVER", server1},

        {"CONNECT TO "+server2+" USER "+systemObject_.getUserId()+" USING '"+password+"'", null}, 
        {"VALUES CURRENT SERVER", server2},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SERVER", "EXCEPTION:re-established"},
        {"VALUES CURRENT SERVER", server2},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SERVER", "EXCEPTION:re-established"},
        {"VALUES CURRENT SERVER", server2},

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SERVER", "EXCEPTION:re-established"},
        {"CONNECT TO "+server3+" USER "+systemObject_.getUserId()+" USING '"+password+"'", null}, 
        {"VALUES CURRENT SERVER", server3},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SERVER", "EXCEPTION:re-established"},
        {"VALUES CURRENT SERVER", server3},
        

        
    };
    testScript(testScript);
    }
  }


  
  /**
   * Test saving of CURRENT TEMPORAL SYSTEM_TIME
   **/
  public void Var016() {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) { 
      if (checkRelease730("CURRENT TEMPORAL SYSTEM_TIME")) { 
    String[][] testScript = {
        {"!CONNECT", null},
        {"SET CURRENT TEMPORAL SYSTEM_TIME '2018-01-01'", null}, 
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "2018-01-01 00:00:00.000000000000"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "EXCEPTION:re-established"},
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "2018-01-01 00:00:00.000000000000"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "EXCEPTION:re-established"},
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "2018-01-01 00:00:00.000000000000"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "EXCEPTION:re-established"},
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "2018-01-01 00:00:00.000000000000"},

        {"SET CURRENT TEMPORAL SYSTEM_TIME '2017-01-01' ", null}, 
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "2017-01-01 00:00:00.000000000000"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "EXCEPTION:re-established"},
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "2017-01-01 00:00:00.000000000000"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "EXCEPTION:re-established"},
        {"VALUES CURRENT TEMPORAL SYSTEM_TIME", "2017-01-01 00:00:00.000000000000"},

        
    };
    testScript(testScript);
      }
    }
  }


  
  /**
   * Test saving of CURRENT USER 
   **/
  public void Var017() {
    String user = pwrSysUserID_.toUpperCase(); 
    
    String[][] testScript = {
        {"!ADDPROPERTY user="+user, null}, 
        {"!ADDPROPERTY password="+PasswordVault.decryptPasswordLeak(pwrSysEncryptedPassword_), null }, 
        {"!CONNECT", null},
        {"VALUES USER", user },
        {"VALUES CURRENT USER", user },
        {"VALUES SESSION_USER", user}, 
        {"VALUES SYSTEM_USER", user }, 
        {"SET SESSION AUTHORIZATION JAVA", null}, 
        {"VALUES USER", "JAVA" },
        {"VALUES CURRENT USER","JAVA" },
        {"VALUES SESSION_USER", "JAVA"}, 
        {"VALUES SYSTEM_USER", user}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES USER", "EXCEPTION:re-established"},
        {"VALUES USER", "JAVA" },
        {"VALUES CURRENT USER","JAVA" },
        {"VALUES SESSION_USER", "JAVA"}, 
        {"VALUES SYSTEM_USER", user}, 
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES USER", "EXCEPTION:re-established"},
        {"VALUES USER", "JAVA" },
        {"VALUES CURRENT USER","JAVA" },
        {"VALUES SESSION_USER", "JAVA"}, 
        {"VALUES SYSTEM_USER", user}, 
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES USER", "EXCEPTION:re-established"},
        {"VALUES USER", "JAVA" },
        {"VALUES CURRENT USER","JAVA" },
        {"VALUES SESSION_USER", "JAVA"}, 
        {"VALUES SYSTEM_USER", user}, 

       
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES USER", "EXCEPTION:re-established"},
        {"VALUES USER", "JAVA" },
        {"VALUES CURRENT USER","JAVA" },
        {"VALUES SESSION_USER", "JAVA"}, 
        {"VALUES SYSTEM_USER", user}, 
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES USER", "EXCEPTION:re-established"},
        {"VALUES USER", "JAVA" },
        {"VALUES CURRENT USER","JAVA" },
        {"VALUES SESSION_USER", "JAVA"}, 
        {"VALUES SYSTEM_USER", user}, 

        
    };
    testScript(testScript);

  }


  
  /* 
   * Set of tests to ensure that settings made via methods calls are preserved.
   * The following methods are tested.
   * 
   *  setAutoCommit(boolean autocommit)
   *  setCatalog( String catalog)  -- Does nothing so it is not tested 
   *  setClientInfo(String name, String value)
   *  setClientInfo(Properties properties)
   *  setHoldability(int holdability)
   *  setNetworkTimeout(Executor executor, int milliseconds)
   *  setReadOnly(boolean readOnly)
   *  setSchema(String schema)
   *  setTransactionIsolation(int level)
   *  setTypeMap(Map map)     
   */

  /**
   * Test saving autocommit setting  
   **/
  public void Var018() {
    String[][] testScript = {
    {"!CONNECT", null},
    {"!CALLMETHOD setAutoCommit false", null}, 
    {"!CALLMETHOD setTransactionIsolation SERIALIZABLE", null}, 
    {"create or replace table "+collection_+".JDASCRG18 (c1 int)",null},
    {"!CALLMETHOD commit true", null}, 
    {"CALL QSYS2.QCMDEXC('CHGPF FILE("+collection_+"/JDASCRG18) WAITFILE(2) WAITRCD(2)')", null}, 
    {"!CALLMETHOD commit true", null}, 
    {"!CHECKTABLE "+collection_+".JDASCRG18", null},
    {"insert into "+collection_+".JDASCRG18 VALUES(99)", null}, 
    {"!CHECKTABLE "+collection_+".JDASCRG18", "in use"},
    {"!CALLMETHOD commit true", null}, 
    {"!CHECKTABLE "+collection_+".JDASCRG18", "99"},
    {"delete from "+collection_+".JDASCRG18", null}, 
    {"!CALLMETHOD commit true", null}, 

    {"!DISABLE 1", null}, 
    {"!ENDACTIVECONNECTIONS 1", null}, 
    {"CALL QSYS2.QCMDEXC('CHGJOB DFTWAIT(2)')", "EXCEPTION:re-established"},
    {"insert into "+collection_+".JDASCRG18 VALUES(99)", null},
    {"!CHECKTABLE "+collection_+".JDASCRG18", "in use"},
    {"!CALLMETHOD commit true", null}, 
    {"!CHECKTABLE "+collection_+".JDASCRG18", "99"},
    {"delete from "+collection_+".JDASCRG18", null}, 
    {"!CALLMETHOD commit true", null}, 
    
    {"!ENABLE 1", null}, 
    {"!DISABLE 2", null}, 
    {"!ENDACTIVECONNECTIONS 2", null},
    {"CALL QSYS2.QCMDEXC('CHGJOB DFTWAIT(2)')", "EXCEPTION:re-established"},
    {"insert into "+collection_+".JDASCRG18 VALUES(99)", null},
    {"!CHECKTABLE "+collection_+".JDASCRG18", "in use"},
    {"!CALLMETHOD commit true", null}, 
    {"!CHECKTABLE "+collection_+".JDASCRG18", "99"},
    {"delete from "+collection_+".JDASCRG18", null}, 
    {"!CALLMETHOD commit true", null}, 

    {"!CALLMETHOD setAutoCommit true", null}, 

    {"insert into "+collection_+".JDASCRG18 VALUES(99)", null},
    {"!CHECKTABLE "+collection_+".JDASCRG18", "99"},
    {"!CALLMETHOD commit true", null}, 
    {"!CHECKTABLE "+collection_+".JDASCRG18", "99"},
    {"delete from "+collection_+".JDASCRG18", null}, 
    {"!CALLMETHOD commit true", null}, 

    
    
    
    {"!ENABLE 2", null}, 
    {"!DISABLE 1", null}, 
    {"!ENDACTIVECONNECTIONS 1", null}, 
    {"CALL QSYS2.QCMDEXC('CHGJOB DFTWAIT(2)')", "EXCEPTION:re-established"},
    {"insert into "+collection_+".JDASCRG18 VALUES(99)", null},
    {"!CHECKTABLE "+collection_+".JDASCRG18", "99"},
    {"!CALLMETHOD commit true", null}, 
    {"!CHECKTABLE "+collection_+".JDASCRG18", "99"},
    {"delete from "+collection_+".JDASCRG18", null}, 
    {"!CALLMETHOD commit true", null}, 
    
    
    {"!ENABLE 1", null}, 
    {"!DISABLE 2", null}, 
    {"!ENDACTIVECONNECTIONS 2", null}, 
    {"CALL QSYS2.QCMDEXC('CHGJOB DFTWAIT(2)')", "EXCEPTION:re-established"},
    {"insert into "+collection_+".JDASCRG18 VALUES(99)", null},
    {"!CHECKTABLE "+collection_+".JDASCRG18", "99"},
    {"!CALLMETHOD commit true", null}, 
    {"!CHECKTABLE "+collection_+".JDASCRG18", "99"},
    {"delete from "+collection_+".JDASCRG18", null}, 
    {"!CALLMETHOD commit true", null}, 
    
    
    {"!ENABLE 2", null}, 

    
    }; 
    
    testScript(testScript);

  }

  

  /*  setClientInfo(String name, String value)   */
  public void Var019() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"!CALLMETHOD setClientInfo ApplicationName=APPLNAME",null }, 
        {"!CALLMETHOD setClientInfo ClientUser=AUSER",null }, 
        {"!CALLMETHOD setClientInfo ClientAccounting=X6782847",null }, 
        {"!CALLMETHOD setClientInfo ClientHostname=AWRKSTNNAME",null }, 
        {"!CALLMETHOD setClientInfo ClientProgramID=WORKLOAD",null }, 
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},

        {"!CALLMETHOD setClientInfo ApplicationName=BAPPLNAME,ClientUser=BUSER,ClientAccounting=BX6782847"+
        ",ClientHostname=BWRKSTNNAME,ClientProgramID=BWORKLOAD",null }, 
        {"!CALLMETHOD setClientInfo ApplicationName=BAPPLNAME",null }, 
        {"!CALLMETHOD setClientInfo ClientUser=BUSER",null }, 
        {"!CALLMETHOD setClientInfo ClientAccounting=BX6782847",null }, 
        {"!CALLMETHOD setClientInfo ClientHostname=BWRKSTNNAME",null }, 
        {"!CALLMETHOD setClientInfo ClientProgramID=BWORKLOAD",null }, 
        {"VALUES CURRENT CLIENT_ACCTNG", "BX6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "BAPPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "BWORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "BUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "BWRKSTNNAME"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "BX6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "BAPPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "BWORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "BUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "BWRKSTNNAME"},
        {"!ENABLE 2", null}, 
    };
    testScript(testScript);

  }

  
  
  /*  setClientInfo(Properties properties)   */
  public void Var020() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"!CALLMETHOD setClientInfo ApplicationName=APPLNAME,ClientUser=AUSER,ClientAccounting=X6782847,ClientHostname=AWRKSTNNAME,ClientProgramID=WORKLOAD",null }, 
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "X6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "APPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "WORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "AUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "AWRKSTNNAME"},

        {"!CALLMETHOD setClientInfo ApplicationName=BAPPLNAME,ClientUser=BUSER,ClientAccounting=BX6782847"+
        ",ClientHostname=BWRKSTNNAME,ClientProgramID=BWORKLOAD",null }, 
        {"VALUES CURRENT CLIENT_ACCTNG", "BX6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "BAPPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "BWORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "BUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "BWRKSTNNAME"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "BX6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "BAPPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "BWORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "BUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "BWRKSTNNAME"},
        {"!ENABLE 2", null}, 

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "BX6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "BAPPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", "BWORKLOAD"},
        {"VALUES CURRENT CLIENT_USERID", "BUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "BWRKSTNNAME"},

        
        {"!CALLMETHOD setClientInfo ApplicationName=BAPPLNAME,ClientUser=BUSER,ClientAccounting=BX6782847"+
        ",ClientHostname=BWRKSTNNAME",null }, 
        {"VALUES CURRENT CLIENT_ACCTNG", "BX6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "BAPPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", ""},
        {"VALUES CURRENT CLIENT_USERID", "BUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "BWRKSTNNAME"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT CLIENT_ACCTNG", "EXCEPTION:re-established"},
        {"VALUES CURRENT CLIENT_ACCTNG", "BX6782847"},
        {"VALUES CURRENT CLIENT_APPLNAME", "BAPPLNAME"},
        {"VALUES CURRENT CLIENT_PROGRAMID", ""},
        {"VALUES CURRENT CLIENT_USERID", "BUSER"},
        {"VALUES CURRENT CLIENT_WRKSTNNAME", "BWRKSTNNAME"},
        {"!ENABLE 2", null}, 
        

        
        
    };
    testScript(testScript);

  }

  
  
  
  /*  setHoldability(int holdability)   */
  public void Var021() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"!CALLMETHOD getHoldability", "1"}, 
        {"!CALLMETHOD setHoldability 2", null}, 
        {"!CALLMETHOD getHoldability", "2"}, 

        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getHoldability", "2"}, 

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getHoldability", "2"}, 

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getHoldability", "2"}, 
        {"!CALLMETHOD setHoldability 1", null}, 
        {"!CALLMETHOD getHoldability", "1"}, 

        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getHoldability", "1"}, 

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getHoldability", "1"}, 
        {"!CALLMETHOD setHoldability 2", null}, 
        {"!CALLMETHOD getHoldability", "2"}, 
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getHoldability", "2"}, 
        {"!ENABLE 2", null}, 
        

        
        
    };
    testScript(testScript);

  }

  /*  setReadOnly(boolean readOnly)   */
  public void Var022() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"!CALLMETHOD isReadOnly", "false"}, 
        {"!CALLMETHOD setReadOnly true", null}, 
        {"!CALLMETHOD isReadOnly", "true"}, 

        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD isReadOnly", "true"}, 

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD isReadOnly", "true"}, 

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD isReadOnly", "true"}, 
        {"!CALLMETHOD setReadOnly false", null}, 
        {"!CALLMETHOD isReadOnly", "false"}, 

        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD isReadOnly", "false"}, 

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD isReadOnly", "false"}, 
        {"!CALLMETHOD setReadOnly true", null}, 
        {"!CALLMETHOD isReadOnly", "true"}, 
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD isReadOnly", "true"}, 
        {"!ENABLE 2", null}, 
        

        
        
    };
    testScript(testScript);

  }

    
  /*  setSchema(String schema)   */
  public void Var023() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"SET CURRENT SCHEMA "+collection_, null}, 
        {"VALUES CURRENT SCHEMA", collection_},
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", collection_},

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", collection_},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", collection_},
        {"SET CURRENT SCHEMA QGPL ", null}, 
        {"VALUES CURRENT SCHEMA", "QGPL"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"SET SCHEMA SYSIBMADM", null}, 
        {"VALUES CURRENT SCHEMA", "SYSIBMADM"},
  
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "SYSIBMADM"},
        

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"SET CURRENT_SCHEMA QGPL", null}, 
        {"VALUES CURRENT SCHEMA", "QGPL"},

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},

        // Now test the method

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD setSchema "+collection_, null}, 
        {"VALUES CURRENT SCHEMA", collection_},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", collection_},
        {"!CALLMETHOD setSchema QGPL ", null}, 
        {"VALUES CURRENT SCHEMA", "QGPL"},
        

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},
        {"!CALLMETHOD setSchema QGPL ", null}, 
        {"VALUES CURRENT SCHEMA", "QGPL"},
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD setSchema SYSIBMADM", null}, 
        {"VALUES CURRENT SCHEMA", "SYSIBMADM"},
  
        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "SYSIBMADM"},
        

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD setSchema QGPL", null}, 
        {"VALUES CURRENT SCHEMA", "QGPL"},

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"VALUES CURRENT SCHEMA", "QGPL"},

        
        
    };
    testScript(testScript);

  }

   
  
  
  /*  setTransactionIsolation(int level)   */
  public void Var024() {
    String[][] testScript = {
        {"!CONNECT", null},
        {"!CALLMETHOD getTransactionIsolation", "1"}, 
        {"!CALLMETHOD setTransactionIsolation 2", null}, 
        {"!CALLMETHOD getTransactionIsolation", "2"}, 

        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getTransactionIsolation", "2"}, 

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getTransactionIsolation", "2"}, 

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getTransactionIsolation", "2"}, 
        {"!CALLMETHOD setTransactionIsolation 1", null}, 
        {"!CALLMETHOD getTransactionIsolation", "1"}, 

        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getTransactionIsolation", "1"}, 

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getTransactionIsolation", "1"}, 
        {"!CALLMETHOD setTransactionIsolation 2", null}, 
        {"!CALLMETHOD getTransactionIsolation", "2"}, 
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getTransactionIsolation", "2"},
        

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getTransactionIsolation", "2"}, 
        {"!CALLMETHOD setTransactionIsolation 4", null}, 
        {"!CALLMETHOD getTransactionIsolation", "4"}, 

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getTransactionIsolation", "4"},
        

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getTransactionIsolation", "4"}, 
        {"!CALLMETHOD setTransactionIsolation 8", null}, 
        {"!CALLMETHOD getTransactionIsolation", "8"}, 

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getTransactionIsolation", "8"},
        
        
        {"!ENABLE 2", null}, 
        

        
        
    };
    testScript(testScript);

  }


  
    
  /*  setTypeMap(Map map)   */     
  /* Not supported */ 
  
    
  /*  setNetworkTimeout(int milliseconds)   */

  public void Var025() {
    String[][] testScript = {
        {"!ADDPROPERTY thread used=false", null}, 
        {"!CONNECT", null},
        {"!CALLMETHOD getNetworkTimeout", "0"}, 
        {"!CALLMETHOD setNetworkTimeout 2000", null}, 
        {"!CALLMETHOD getNetworkTimeout", "2000"}, 

        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getNetworkTimeout", "2000"}, 

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getNetworkTimeout", "2000"}, 

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getNetworkTimeout", "2000"}, 
        {"!CALLMETHOD setNetworkTimeout 1000", null}, 
        {"!CALLMETHOD getNetworkTimeout", "1000"}, 

        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getNetworkTimeout", "1000"}, 

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getNetworkTimeout", "1000"}, 
        {"!CALLMETHOD setNetworkTimeout 2000", null}, 
        {"!CALLMETHOD getNetworkTimeout", "2000"}, 
        
        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getNetworkTimeout", "2000"},
        

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getNetworkTimeout", "2000"}, 
        {"!CALLMETHOD setNetworkTimeout 4000", null}, 
        {"!CALLMETHOD getNetworkTimeout", "4000"}, 

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getNetworkTimeout", "4000"},
        

        {"!ENABLE 2", null}, 
        {"!DISABLE 1", null}, 
        {"!ENDACTIVECONNECTIONS 1", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getNetworkTimeout", "4000"}, 
        {"!CALLMETHOD setNetworkTimeout 8000", null}, 
        {"!CALLMETHOD getNetworkTimeout", "8000"}, 

        {"!ENABLE 1", null}, 
        {"!DISABLE 2", null}, 
        {"!ENDACTIVECONNECTIONS 2", null}, 
        {"VALUES CURRENT SCHEMA", "EXCEPTION:re-established"},
        {"!CALLMETHOD getNetworkTimeout", "8000"},
        
        
        {"!ENABLE 2", null}, 
        

        
        
    };
    testScript(testScript);

  }


  
  
  
}
