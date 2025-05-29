///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPerformanceUtil.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/**
 * JDPerformance Utils contains utilities for recording and reporting the
 * performace of testcases. 
 * 
 * The information is stored in the JDPERFRES.RESULTS2 table which has the following
 * format
 * 
 * RUNTIMESTAMP | TEST | CLIENT | CLIENTLOAD | SERVER | SERVERLOAD | JAVAHOME | DRIVER | RUNMICROS
 * 
 *  RUNTIMESTAMP    timestamp indicating when the test completed
 *  TEST            name of the test
 *  CLIENT          system name of the client running the test
 *  CLIENTLOAD      a percentage indicating the approximate load on the client system when the 
 *                  test was run.  This utility provides a native method to get that information.
 *               
 *  SERVER          system name of the server running the test
 *  SERVERLOAD      a percentage indicating the approximate load on the client system when the test 
 *                  was run.  This utility provides a method to get that information. 
 *  JAVAHOME        JAVAHOME of JVM used by test
 *  DRIVER          driver used to run the test could be NATIVE/TOOLBOX/JCC
 *  RUNMICROS       the number of microseconds used by the test 
 *        
 *  Historical Note:  The JDPERFRES.RESULTS table had a JDK column instead of JAVAHOME.  With the
 *  advent of J9, it is better to record the Javahome. 
 */
package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Connection;

import java.sql.*;
import java.util.Vector;

public class JDPerformanceUtil {
  static boolean debug=true; 
  static String clientName = null;
  public static String getJavaHome() {
    String javaHome = System.getProperty("java.home");  

    /* shorten some versions java.home */
    if (javaHome.indexOf("/QIBM/ProdData/Java400") == 0) {
	javaHome = javaHome.substring(15); 
    } else if (javaHome.indexOf("/QOpenSys/QIBM/ProdData/JavaVM/") == 0) {
	javaHome = javaHome.substring(23); 
    }

    return javaHome; 
  }


  public static void recordTestRun(Connection connection, 
                                   Timestamp  runTimeStamp,
                                   String     test, 
                                   String     driver,
                                   long       runMicros) {
    try {
      if (clientName == null) { 
         InetAddress localAddress =  InetAddress.getLocalHost();
         clientName  = localAddress.getHostName();
         int rchIndex = clientName.indexOf(JTOpenTestEnvironment.getDefaultClientDomain().toUpperCase()); 
         if (rchIndex > 0) { 
           clientName = clientName.substring(0, rchIndex); 
         }
      }
         
    double clientLoad = getClientCPULoad(); 
    String server; 
    double serverLoad=getServerLoad(connection);
    String javaHome = getJavaHome();


    Statement stmt = connection.createStatement(); 
    ResultSet rs = stmt.executeQuery("SELECT CURRENT SERVER FROM SYSIBM.SYSDUMMY1"); 
    rs.next();
    server = rs.getString(1); 


    
    recordTestRun(connection, 
                  runTimeStamp, 
                  test, 
                  clientName,
                  clientLoad, 
                  server, 
                  serverLoad, 
                  javaHome, 
                  driver,
                  runMicros);
    } catch (Exception e) {
      e.printStackTrace(); 
    }
             
  }
  public static void recordTestRun(Connection connection,
                                   Timestamp runTimeStamp,
                                   String    test,
                                   String    client, 
                                   double    clientLoad,
                                   String    server,
                                   double    serverLoad,
                                   String    javaHome,
                                   String    driver,
                                   long      runMicros) {
    try {
      Statement stmt = connection.createStatement();

      PreparedStatement pstmt = null;
      //if old table still has name of client, delete it
      try{ 
          pstmt = connection.prepareStatement("SELECT * FROM JDPERFRES.RESULTS2");
          ResultSetMetaData rsmd = pstmt.getMetaData();
          //CLIENT exists, so delete table so it gets created fresh below as CLIENTX
          if(rsmd.getColumnName(3).equalsIgnoreCase("CLIENT"))    
              stmt.execute("DROP TABLE JDPERFRES.RESULTS2");
      } catch (Exception e2) {
            //ignore        
          System.out.println(e2);
      }
        try {
          pstmt = connection
              .prepareStatement("INSERT INTO JDPERFRES.RESULTS2 VALUES(?,?,?,?,?,?,?,?,?)");
        } catch (Exception e) {
          // If the file is not found make sure it exists.
          try { 
            stmt.executeUpdate("CREATE SCHEMA JDPERFRES");

	    stmt.executeUpdate("CALL QSYS.QCMDEXC('CHGLIB LIB(JDPERFRES) TEXT(''JDBC perf results .. Do not delete'')                                      ', 000000086.00000) "); 

          } catch (Exception e2) {
            // ignore
          }
          // v6r1, "client" is a reserved keyword.
          stmt.executeUpdate("CREATE TABLE JDPERFRES.RESULTS2("+
              "RUNTIMESTAMP TIMESTAMP, "+
              "TEST         VARCHAR(100),"+
              "CLIENTX      VARCHAR(100), "+
              "CLIENTLOAD   DOUBLE, "+
              "SERVER       VARCHAR(100), "+
              "SERVERLOAD   DOUBLE, "+
              "JAVAHOME     VARCHAR(40), "+
              "DRIVER       VARCHAR(20), "+
              "RUNMICROS    BIGINT)"); 
          pstmt = connection
          .prepareStatement("INSERT INTO JDPERFRES.RESULTS2 VALUES(?,?,?,?,?,?,?,?,?)");
          
        }
        pstmt.setTimestamp(1,runTimeStamp);
        pstmt.setString(2,test); 
        pstmt.setString(3,client); 
        pstmt.setDouble(4,clientLoad); 
        pstmt.setString(5,server); 
        pstmt.setDouble(6,serverLoad); 
        pstmt.setString(7,javaHome); 
        pstmt.setString(8,driver); 
        pstmt.setLong  (9,runMicros);
        pstmt.executeUpdate(); 
        pstmt.close(); 
        stmt.close(); 
    } catch (Exception e) { 
      e.printStackTrace(); 
    }
  }
  
  public static native double cpuuseNative();

  
  static boolean platformKnown = false; 
  static boolean nativeMethodAvailable=false; 
  static String osname = "";

  public static double getClientCPULoad() {
    if (!platformKnown) {
      String srvpgm = "CPUUSE"; 
      platformKnown=true; 
      if (JTOpenTestEnvironment.isOS400 && !JTOpenTestEnvironment.isOS400open) {
        // 
        // If on iSeries, determine if native method exists
        // Create native method and use it.
        // 
        File cpuuseSrvpgm = new File ("/QSYS.LIB/QGPL.LIB/CPUUSE.SRVPGM"); 
        if (! cpuuseSrvpgm.exists()) {
          try { 
            Runtime runtime;
            Process process;
            runtime = Runtime.getRuntime();

            String command = "rm -f /tmp/CpuUse.c";
            if (debug) System.out.println("CpuUse.debug: "+command); 
            process = runtime.exec(command );
            if (debug) JDJobName.showProcessOutput(process, null);
            process.waitFor();


            command = "touch -C 819 /tmp/CpuUse.c"; 
            if (debug) System.out.println("CpuUse.debug: "+command); 
            process = runtime.exec(command );
            if (debug) JDJobName.showProcessOutput(process, null);
            process.waitFor();

            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("/tmp/CpuUse.c")));


            for (int i = 0; i < cpuuseSource.length; i++) {
                writer.println(cpuuseSource[i]); 
            }
            writer.close();

            command = "system  CRTCMOD MODULE(QGPL/"+srvpgm+") SRCSTMF('/tmp/CpuUse.c')  DBGVIEW(*ALL) TERASPACE(*YES) STGMDL(*INHERIT) "; 
            if (debug) System.out.println("CpuUse.debug: "+command); 
            process = runtime.exec(command );
            if (debug) JDJobName.showProcessOutput(process, null);
            process.waitFor();


            command = "system  CRTSRVPGM SRVPGM(QGPL/"+srvpgm+") MODULE(QGPL/"+srvpgm+") EXPORT(*ALL) STGMDL(*INHERIT) "; 
            if (debug) System.out.println("CpuUse.debug: "+command); 
            process = runtime.exec(command );
            if (debug) JDJobName.showProcessOutput(process, null);
            process.waitFor();
          } catch (Exception e) {
            e.printStackTrace(); 
          }
          
        }
        System.load("/QSYS.LIB/QGPL.LIB/CPUUSE.SRVPGM");
        nativeMethodAvailable=true; 
      }
      
      
      
    }
    
    if (nativeMethodAvailable) {
       return cpuuseNative(); 
    } else { 
       return 0.0;
    }
  }

  public static String[] cpuuseSource = {
      "",
      "/*",
      " * Compile using",
      " *",
      " *  CRTSQLCI OBJ(QGPL/cpuuse) SRCFILE(QGPL/QCSRC)  ",
      " *  CRTPGM PGM(QGPL/cpuuse) MODULE(QGPL/cpuuse) ACTGRP(*CALLER)  ",
      " * ",
      " * Create using the following SQLStatement",
      " * CREATE PROCEDURE QGPL.cpuuse (OUT USAGE DOUBLE) dynamic result sets 0 ",
      " * EXTERNAL NAME 'QGPL/CPUUSE' LANGUAGE C GENERAL",
      " *",
      " * Then call using",
      " *",
      " * call qgpl.cpuuse(?)",
      " * ",
      " */",
      "",
      "",
      "#include <milib.h>",
      "#include <miproces.h>",
      "#include <mirsc.h>",
      "#include <stdio.h>",
      "#include <stdlib.h>",
      "#include <jni.h>",
     
      "",
      "_MATRMD_Template_T   *RMD_template;    ",
      "char             control[ 8 ];         ",
      "_MI_Time        time_to_wait;          ",
      "int             hours      =  0,       ",
      "                minutes    =  0,       ",
      "                seconds    =  1,       ",
      "                hundredths =  0;       ",
      "",
      "short           wait_option;",
      "double       begin_time;",
      "double       begin_cpu;",
      "double       end_time;",
      "double       end_cpu;",
      "",
      "double t_to_f(_MI_Time x)",
      "{",
      "   double result = 0.0;",
      "   int i;",
      "   for (i = 0; i < sizeof(_MI_Time); i++) {",
      "      result = result * 256.0 + x[i]; ",
      "   }",
      "   return result; ",
      "} ",
      "",
      "double cpuuse() {",
      "    double usage; ",
      "    control[ 0 ] = _MATRMD_PROCESS_DATA ;     ",
      "    RMD_template = ( _MATRMD_Template_T * ) malloc( 24 );",
      "    memset((void *) RMD_template,0,24);",
      "    RMD_template->Template_Size = 24;",

      "",
      "    matrmd ( RMD_template, control );",
      "    begin_time=t_to_f(RMD_template->Time_of_Day);",
      "    begin_cpu =t_to_f(RMD_template->_MATRMD_Data.Process_Data.Time);",
      "",
      "     mitime( &time_to_wait, hours, minutes, seconds, hundredths );",
      "     wait_option = _WAIT_NORMAL;       ",
      "     waittime( &time_to_wait, wait_option );",
      "",
      "    memset((void *) RMD_template,0,24);",
      "    RMD_template->Template_Size = 24;",
      "",
      "    matrmd ( RMD_template, control );",
      "",
      "    end_time=t_to_f(RMD_template->Time_of_Day);",
      "    end_cpu =t_to_f(RMD_template->_MATRMD_Data.Process_Data.Time);",
      "",
      "    usage = (end_cpu - begin_cpu) / (end_time - begin_time) ;",
      "    return usage; ",

      "}", 
      "",
        "jdouble Java_test_JDPerformanceUtil_cpuuseNative(JNIEnv * env)",
        "{",
        "    return (jdouble) cpuuse();",
        "}",

      "int main(int argc, char * argv[]) {",
      "  double usage=0.67;",
      "  double * outUsage; ",
            "",
      "",
      "  outUsage = (double *) argv[1];",
      "",
      "  *outUsage = cpuuse(); ",
      "  ",
      "}",

  };


 public static void stringArrayToSourceFile(Connection connection, String stringArray[], String library, String file) throws Exception {

      //
      // Make sure the procedure exists to call CL commands.
      Statement stmt = connection.createStatement(); 
      try {
	  stmt.executeUpdate("create procedure "+
			     "QGPL.JDCMDEXEC(IN CMDSTR VARCHAR(1024),IN CMDLEN DECIMAL(15,5)) "+
			     "External name QSYS.QCMDEXC LANGUAGE C GENERAL"); 
      } catch (Exception e) {
	  // Just ignore error 
      } 

      CallableStatement cmd = connection.prepareCall("call QGPL.JDCMDEXEC(?,?)"); 

      //
      // Make sure the source file exists 
      //
      String command = "QSYS/CRTSRCPF "+library+"/"+file;
      cmd.setString(1, command );
      cmd.setInt(2, command.length());
      try {
	  cmd.execute(); 
      } catch (Exception e) {
	  // ignore error
      } 

      //
      // Make sure the member exists 
      //
      command = "QSYS/ADDPFM  "+library+"/"+file+" "+file;
      cmd.setString(1, command );
      cmd.setInt(2, command.length());

      try {
	  cmd.execute(); 
      } catch (Exception e) {
	  // ignore error 
      } 

      //
      // Use SQL to clear the member
      //

      stmt.executeUpdate("DELETE FROM "+library+"."+file);

      //
      // Use SQL to add the file to the member
      //

      PreparedStatement ps = connection.prepareStatement("insert into "+library+"."+file+" values (?, 0, ?)");

      for (int i = 0; i < stringArray.length; i++) {
	  ps.setInt(1, i+1);
	  ps.setString(2, stringArray[i]);
	  ps.executeUpdate();
      }

      ps.close(); 
      stmt.close(); 
      cmd.close(); 

  }  

 public static boolean cpuuseExists = false; 
  
  public static double getServerLoad(Connection connection) throws SQLException {
    double answer=0; 
    if (!cpuuseExists) {
      try {

        // 
        // Check if the stored procedure exists
        // 
        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet rs = dmd.getProcedures(null, "QGPL", "CPUUSE");
        if (!rs.next()) {
          // Row not found so procedure must be created

          stringArrayToSourceFile(connection, cpuuseSource, "QGPL", "CPUUSE");


          // compile
          CallableStatement cmd = connection.prepareCall("call QGPL.JDCMDEXEC(?,?)");
          String command = "QSYS/CRTSQLCI OBJ(QGPL/cpuuse) SRCFILE(QGPL/cpuuse)";
          cmd.setString(1, command );
          cmd.setInt(2, command.length());
          cmd.execute();


          command = "QSYS/CRTPGM PGM(QGPL/cpuuse) MODULE(QGPL/cpuuse) ACTGRP(*CALLER)";
          cmd.setString(1, command );
          cmd.setInt(2, command.length());
          cmd.execute();

          String sql = "CREATE PROCEDURE QGPL.cpuuse (OUT USAGE DOUBLE) "+
                       "dynamic result sets 0 EXTERNAL NAME 'QGPL/CPUUSE' "+
                       "LANGUAGE C GENERAL"; 
          
          Statement stmt = connection.createStatement(); 
          try {
              stmt.executeUpdate("drop procedure qgpl.CPUUSE"); 
          } catch (Exception e) {
          } 
          stmt.executeUpdate(sql); 
          
          // create procedures 
          cpuuseExists = true; 
          
        } else {
          cpuuseExists = true;
        }
        rs.close();
      } catch (SQLException e) {
	System.out.println("Warning:  Exception in getServerLoad"); 
        e.printStackTrace();
	throw e;            /* throw exception so it can be handled */ 

      } catch (Exception e) {
	System.out.println("Warning:  Exception in getServerLoad"); 
        e.printStackTrace();
      }

    }
    try {
      // 
      // Call the stored procedure
      // 
      CallableStatement cs = connection.prepareCall("call qgpl.cpuuse(?)");
      cs.registerOutParameter(1, Types.DOUBLE);
      cs.execute();
      answer = cs.getDouble(1); 
    } catch (Exception e) {
      System.out.println("Warning:  Exception in getServerLoad"); 
      e.printStackTrace();
    }
    return answer;
  }
  
  public static void main(String args[]) {
    if (args.length < 3) {
      System.out.println("Usage:  JDPerformanceUtil <URL> <userid> <password> ");
      System.out.println("        JDPerformanceUtil jdbc:db2:y0551p2 user xxxxx "); 
      System.out.println("        JDPerformanceUtil jdbc:as400://y0551p2 user xxxxx "); 
      System.out.println("        JDPerformanceUtil jdbc:db2://y0551p2:446/*LOCAL user xxxxx"); 
      System.out.println("        JDPerformanceUtil jdbc:db2://diroc:50000/SAMPLE user xxxxx"); 
      System.out.println("");
      System.out.println("Current system information");
    } else {
      try {
        String userId = args[1]; 
        String password = args[2]; 
        String driver = ""; 
        String localhost = "" ; 
        String serverhost = "" ; 
        String URL = args[0];
        // Load the right JDBC driver base on the URL
        if (URL.indexOf(":as400:") > 0) {
          Class.forName("com.ibm.as400.access.AS400JDBCDriver");
          driver ="TOOLBOX"; 
        } else {
          if (URL.indexOf(":db2:") > 0) {
             if (URL.indexOf("//")> 0) {
               Class.forName("com.ibm.db2.jcc.DB2Driver");
               driver="JCC"; 
             } else {
               Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
               driver="NATIVE"; 
             }
          } else {
             System.out.println("Unrecognized URL:  "+ URL); 
          }
        }
        long startTime = System.currentTimeMillis(); 
        String javaHome = getJavaHome(); 

        Connection c = DriverManager.getConnection(URL, userId, password);
        Statement stmt = c.createStatement(); 
        ResultSet rs = stmt.executeQuery("SELECT CURRENT SERVER FROM SYSIBM.SYSDUMMY1"); 
        rs.next(); 
        serverhost = rs.getString(1); 
        InetAddress localAddress =  InetAddress.getLocalHost(); 
        localhost = localAddress.getHostName();
        double clientCpu=getClientCPULoad(); 
        double serverCpu=getServerLoad(c); 
        Timestamp ts = new Timestamp(System.currentTimeMillis()); 
        String testname = "PERFUTIL1"; 
        long endTime = System.currentTimeMillis(); 
        long runTime = endTime - startTime; 
        System.out.println("Call recordTestRun with "+ts+","+testname+","+localhost+", "+clientCpu+","+
            serverhost+", "+serverCpu+","+javaHome+","+driver+","+ runTime+")");
        startTime = System.currentTimeMillis() ; 
        recordTestRun(c, ts, testname, localhost, clientCpu, serverhost, serverCpu, javaHome, driver, runTime); 
        endTime = System.currentTimeMillis(); 
        runTime=endTime-startTime ; 
        ts = new Timestamp(System.currentTimeMillis()); 
        System.out.println("Call recordTestRun with "+ts+","+testname+","+driver+","+ runTime+")");
        
        testname="PERFUTIL2"; 
        recordTestRun(c, ts, testname, driver, runTime); 
        
        
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void showRunComparision(Connection connection_, String testname, Timestamp ts) {
    try { 
      PreparedStatement pstmt = connection_.prepareStatement(
          "SELECT TEST, CLIENTX, SERVER, JAVAHOME, DRIVER, '1' AS COUNT, RUNMICROS FROM JDPERFRES.RESULTS2 WHERE RUNTIMESTAMP = ? ");
      pstmt.setTimestamp(1, ts); 
      ResultSet rs = pstmt.executeQuery();
      System.out.println("Last run results"); 
      showResults(rs);
      pstmt.close();

      System.out.println("Best run results by Driver, JAVA_HOME");
      // client is a reserved keyword
      pstmt = connection_.prepareStatement(
          "SELECT TEST, CLIENTX, SERVER, JAVAHOME, DRIVER, COUNT(RUNMICROS) AS COUNT , MIN(RUNMICROS) AS MINRUN, AVG(RUNMICROS) AS AVGRUN FROM JDPERFRES.RESULTS2 WHERE TEST=? GROUP BY TEST, CLIENTX, SERVER, JAVAHOME, DRIVER ORDER BY DRIVER, JAVAHOME");
      pstmt.setString(1, testname); 
      rs = pstmt.executeQuery(); 
      showResults(rs); 
      pstmt.close();


      System.out.println("Best run results by AVGRUN");
      pstmt = connection_.prepareStatement(
          "SELECT TEST, CLIENTX, SERVER, JAVAHOME, DRIVER, COUNT(RUNMICROS) AS COUNT , MIN(RUNMICROS) AS MINRUN, AVG(RUNMICROS) AS AVGRUN FROM JDPERFRES.RESULTS2 WHERE TEST=? GROUP BY TEST, CLIENTX, SERVER, JAVAHOME, DRIVER ORDER BY AVGRUN");
      pstmt.setString(1, testname); 
      rs = pstmt.executeQuery(); 
      showResults(rs); 
      pstmt.close();




    } catch (Exception e) { 
      e.printStackTrace(); 
    }
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static void showResults(ResultSet rs) throws Exception  { 
    ResultSetMetaData rsmd = rs.getMetaData(); 
    int columnCount = rsmd.getColumnCount(); 
    
    int[] columnWidth = new int[columnCount+1]; 
    Vector[] columnContents = new Vector[columnCount+1];
    String[] columnHeader   = new String[columnCount+1]; 
    for (int i = 1; i <= columnCount; i++) { 
      columnHeader[i] = rsmd.getColumnName(i); 
      columnWidth[i]=columnHeader[i].length(); 
      columnContents[i] = new Vector(); 
    }
    
    while (rs.next()) {
      for (int i = 1; i <= columnCount; i++) {
        String s = rs.getString(i);
        if (s.length() > columnWidth[i]) {
          columnWidth[i] = s.length(); 
        }
        columnContents[i].add(s); 
      }
    }
    rs.close(); 
    StringBuffer sb = new StringBuffer(); 
    for (int i = 1; i <= columnCount; i++) {
        columnWidth[i] += 1;
        padOut(sb, columnHeader[i], columnWidth[i]); 
    }
    System.out.println(sb.toString()); 
    for (int i = 0; i < columnContents[1].size(); i++) {
      sb.setLength(0); 
      for (int j = 1; j <= columnCount; j++) { 
        padOut(sb, (String) columnContents[j].elementAt(i), columnWidth[j]); 
      }
      
      System.out.println(sb.toString()); 
    }
  }
  
  public static void padOut(StringBuffer sb, String x, int width) {
    sb.append(x); 
    int padChars = width - x.length(); 
    for (int i = 0; i < padChars; i++) {
      sb.append(' '); 
    }
  }
  
}
