///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CleanupSockets.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;

public class CleanupSockets {
  public static void usage() { 
    System.out.println("Usage:  java CleanupSockets [System] [userid] [password]");
    System.out.println("   Cleans up sockets on the specified connection.  If the system is not specifed then the local system is used. "); 
  }

  public static void cleanup(PrintWriter printWriter, String system,
      String userid, String password) throws Exception {

    Connection c = null;
    Timestamp ts;
    if (system == null) {
      c = DriverManager.getConnection("jdbc:db2:localhost");
    } else {
      c = DriverManager.getConnection("jdbc:as400:" + system, userid, password);
    }

    Statement queryStatement = c.createStatement();
    CallableStatement qcmdExcStatement = c.prepareCall("CALL QSYS2.QCMDEXC(?)");

    // Restart interface if needed.
    String query = "select 'ENDTCPIFC INTNETADR(''' CONCAT INTERNET_ADDRESS CONCAT ''')' ,INTERNET_ADDRESS, LINE_DESCRIPTION, INTERFACE_STATUS from QSYS2.NETSTAT_INTERFACE_INFO  WHERE CONNECTION_TYPE='IPV4' AND INTERFACE_STATUS <> 'ACTIVE'";
 ts = new Timestamp(System.currentTimeMillis());
    printWriter.println(ts + " : running query " + query + "\n");
    ResultSet rs = queryStatement.executeQuery(query);

    while (rs.next()) {

      String command = rs.getString(1);
      ts = new Timestamp(System.currentTimeMillis());
      printWriter.println(ts + " : exeuting command  " + command + "\n");
      qcmdExcStatement.setString(1, command);
      qcmdExcStatement.execute();
    }

    query = "select 'STRTCPIFC INTNETADR(''' CONCAT INTERNET_ADDRESS CONCAT ''')' ,INTERNET_ADDRESS, LINE_DESCRIPTION, INTERFACE_STATUS from QSYS2.NETSTAT_INTERFACE_INFO  WHERE CONNECTION_TYPE='IPV4' AND INTERFACE_STATUS <> 'ACTIVE'";

    printWriter.println(ts + " : running query " + query + "\n");
    rs = queryStatement.executeQuery(query);

    while (rs.next()) {

      String command = rs.getString(1);
      ts = new Timestamp(System.currentTimeMillis());
      printWriter.println(ts + " : exeuting command  " + command + "\n");
      qcmdExcStatement.setString(1, command);
      qcmdExcStatement.execute();
    }

    // Cleanup ports
    ts = new Timestamp(System.currentTimeMillis());
    query = "select 'ENDTCPCNN PROTOCOL(*TCP) LCLINTNETA('''||LOCAL_ADDRESS||''') LCLPORT('||LOCAL_PORT||') RMTINTNETA('''||REMOTE_ADDRESS||''') RMTPORT('||REMOTE_PORT|| ')    ', "
        + "CONNECTION_TYPE,REMOTE_ADDRESS,REMOTE_PORT,REMOTE_PORT_NAME,LOCAL_ADDRESS,LOCAL_PORT,LOCAL_PORT_NAME,PROTOCOL,TCP_STATE,IDLE_TIME "
        + "from qsys2.netstat_info where IDLE_TIME > 300 and REMOTE_PORT = 25";
    printWriter.println(ts + " : running query " + query + "\n");
    rs = queryStatement.executeQuery(query);

    while (rs.next()) {

      String command = rs.getString(1);
      ts = new Timestamp(System.currentTimeMillis());
      printWriter.println(ts + " : exeuting command  " + command + "\n");
      qcmdExcStatement.setString(1, command);
      qcmdExcStatement.execute();
    } 


    // Cleanup scanner hanging on port 6042

    ts = new Timestamp(System.currentTimeMillis());
    query = "select 'ENDTCPCNN PROTOCOL(*TCP) LCLINTNETA('''||LOCAL_ADDRESS||''') LCLPORT('||LOCAL_PORT||') RMTINTNETA('''||REMOTE_ADDRESS||''') RMTPORT('||REMOTE_PORT|| ')    ', CONNECTION_TYPE,REMOTE_ADDRESS,REMOTE_PORT,REMOTE_PORT_NAME,LOCAL_ADDRESS,LOCAL_PORT,LOCAL_PORT_NAME,PROTOCOL,TCP_STATE,IDLE_TIME from qsys2.netstat_info where LOCAL_PORT=6042 and BYTES_RECEIVED_LOCALLY < 50 and REMOTE_ADDRESS <> '0.0.0.0'";
    printWriter.println(ts + " : running query " + query + "\n");
    rs = queryStatement.executeQuery(query);

    while (rs.next()) {

      String command = rs.getString(1);
      ts = new Timestamp(System.currentTimeMillis());
      printWriter.println(ts + " : exeuting command  " + command + "\n");
      qcmdExcStatement.setString(1, command);
      qcmdExcStatement.execute();
    } 



    c.close(); 
    


  }
  public static void main(String args[]) {
    String system = null ; 
    String userid = null; 
    String password = null; 
    PrintWriter printWriter = null; 
    
    try { 
      printWriter = new PrintWriter(new FileWriter("/tmp/CleanupSockets.out", true));

      if (args.length >= 3) {
        system=args[0]; 
        userid=args[1];
        password=args[2]; 
      }
      cleanup(printWriter, system,userid,password);
      
    } catch (Exception e) { 
      StringBuffer sb = new StringBuffer(); 
      e.printStackTrace(); 
      Testcase.printStackTraceToStringBuffer(e, sb); 
      printWriter.append("Error: "+sb.toString()+"\n"); 
      usage(); 
    }
    if (printWriter != null) { 
      printWriter.close(); 
    }
  }
}
