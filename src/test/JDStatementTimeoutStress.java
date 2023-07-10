///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementTimeoutStress.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementTimeoutStress.java
//
// Classes:      JDStatementTimeoutStress
//
////////////////////////////////////////////////////////////////////////

package test;

import java.util.List;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

public class JDStatementTimeoutStress implements Runnable {
  String url;
  String userid;
  String password;
  String schema;
  int runSeconds;
  int timeoutSeconds;
  private Connection connection;
  private Statement stmt;
  Hashtable tableToColumns = new Hashtable();
  Hashtable typeToTableColumns = new Hashtable();
  Random random = new Random();
  private Object[] tableArray;
  private int tableCount;
  String threadName = ""; 
  public JDStatementTimeoutStress(String url, String userid, String password,
      String schema, int runSeconds, int timeoutSeconds, String threadName) throws Exception {
    this.url = url;
    this.userid = userid;
    this.password = password;
    this.schema = schema;
    this.runSeconds = runSeconds;
    this.timeoutSeconds = timeoutSeconds;
    this.threadName = threadName; 
    int unknownCount = 0;
    Class.forName("com.ibm.as400.access.AS400JDBCDriver");
    connection = DriverManager.getConnection(
        url + ";query timeout mechanism=cancel", userid, password);
    stmt = connection.createStatement();
    stmt.execute("SET SCHEMA " + schema);
    stmt.setQueryTimeout(timeoutSeconds);
    System.out.println("Query timeout is " + timeoutSeconds);
    // Figure out the metadata for the tables
    DatabaseMetaData dmd = connection.getMetaData();
    ResultSet rs = dmd.getColumns(null, schema, null, null);
    while (rs.next()) {
      String tableName = rs.getString(3);
      String columnName = rs.getString(4);
      int sqltype = rs.getInt(5);
      String datatype = "";
      switch (sqltype) {
      case Types.CHAR:
      case Types.VARCHAR:
      case Types.CLOB:
      case -9:   /* NVARCHAR */ 
      case 2011: /* NCLOB */ 
      case -15:  /* NCHAR */ 
        datatype = "CHAR";
        break;
      case Types.DECIMAL:
      case Types.NUMERIC:
        datatype = "DECIMAL";
        break;
      case Types.INTEGER:
        datatype = "INTEGER";
        break;
      case Types.BIGINT:
        datatype = "BIGINT";
        break;
      case Types.SMALLINT:
        datatype = "SMALLINT";
        break;
      case Types.TIMESTAMP:
        datatype = "TIMESTAMP";
        break;
      case Types.BLOB:
        datatype = "BLOB";
        break;
        
      case Types.REAL:
        datatype = "REAL";
        break;
      case Types.BINARY:
      case Types.VARBINARY:
        datatype = "VARBINARY"; 
        break;
      case Types.OTHER:
        datatype = "OTHER"; 
        break;
      case Types.DATALINK:
        datatype = "DATALINK"; 
        break; 
      case Types.DATE:
        datatype = "DATE";
        break;
      case Types.TIME:
        datatype = "TIME";
        break;
      case Types.DOUBLE:
        datatype = "DOUBLE"; 
        break; 
      case 2009:
        datatype = "SQLXML"; 
        break; 
      default:
        System.out.println("Warning:  type " + sqltype + " unknown");
        datatype = "UNKNOWN";
        unknownCount++;
      }

      List columns = (List) tableToColumns.get(tableName);
      if (columns == null) {
        columns = new ArrayList();
        tableToColumns.put(tableName, columns);
      }
      columns.add(columnName);

      List tableColumns = (List) typeToTableColumns.get(datatype);
      if (tableColumns == null) {
        tableColumns = new ArrayList();
        typeToTableColumns.put(datatype, tableColumns);
      }

      String[] tableColumn = new String[2];
      tableColumn[0] = tableName;
      tableColumn[1] = columnName;
      tableColumns.add(tableColumn);
    }
    rs.close();

    if (unknownCount > 0) {
      throw new Exception("unknownCount=" + unknownCount);
    }
    System.out.println("---------------------------------------");

    Set keySet = tableToColumns.keySet();
    tableArray = keySet.toArray();
    tableCount = tableArray.length;
    System.out.println("There were " + tableCount + " tables found");
    for (int i = 0; i < tableCount; i++) {
      List columns = (List) tableToColumns.get(tableArray[i]);
      System.out.println(
          "Table " + tableArray[i] + " has " + columns.size() + " columns");
    }
    System.out.println("---------------------------------------");

  }

  public void run() {
    try {
      long endTime = System.currentTimeMillis() + runSeconds * 1000;
      String noComplex = System.getProperty("noComplex");
      String statementLeak = System.getProperty("statementLeak");
      if (statementLeak != null) {
	  System.out.println(threadName+":Runnning with statement leak"); 
      } 
      while (System.currentTimeMillis() < endTime) {
	  if (statementLeak != null) {
	      stmt = connection.createStatement();
	      stmt.execute("SET SCHEMA " + schema);
	      stmt.setQueryTimeout(timeoutSeconds);
	  } 
        int tableIndex1 = random.nextInt(tableCount);
        int tableIndex2 = random.nextInt(tableCount);
        int tableIndex3 = random.nextInt(tableCount);
        int tableIndex4 = random.nextInt(tableCount);
        String table1 = (String) tableArray[tableIndex1];
        String table2 = (String) tableArray[tableIndex2];
        String table3 = (String) tableArray[tableIndex3];
        String table4 = (String) tableArray[tableIndex4];

        StringBuffer sqlsb = new StringBuffer();

	if (noComplex == null) { 
        sqlsb.append("SELECT T1." + pickColumn(table1) + ",");
        sqlsb.append("T1." + pickColumn(table1) + ",");
        sqlsb.append("T2." + pickColumn(table2) + ",");
        sqlsb.append("T2." + pickColumn(table2) + ",");
        sqlsb.append("T3." + pickColumn(table3) + ",");
        sqlsb.append("T3." + pickColumn(table3) + ",");
        sqlsb.append("T4." + pickColumn(table4) + ",");
        sqlsb.append("T4." + pickColumn(table4) + " FROM ");
        sqlsb.append(table1 + " T1 ," + table2 + " T2," + table3 + " T3,"
            + table4 + " T4");
	} else {
	    sqlsb.append("SELECT T1." + pickColumn(table1) + ",");
	    sqlsb.append("T1." + pickColumn(table1) + " FROM ");
	    sqlsb.append(table1 + " T1");

	} 
        System.out.println(threadName+":Query is : " + sqlsb.toString());
        long queryStartTime = System.currentTimeMillis();
        long queryEndTime = 0;
        try {
          int count = 0;
          ResultSet rs = stmt.executeQuery(sqlsb.toString());
          queryEndTime = System.currentTimeMillis();
          while (rs.next() && count < 100) {
            count++;
          }
          System.out
              .println(threadName+":Query took " + (queryEndTime - queryStartTime) + " ms");
        } catch (Exception e) {
	    queryEndTime = System.currentTimeMillis();
          System.out.println(threadName+":Exception caught");
          e.printStackTrace(System.out);

          System.out
              .println(threadName+":Query took " + (queryEndTime - queryStartTime) + " ms");

        }


      }

    } catch (Exception e) {
      System.out.println(threadName+":Error found: Thread aborting");
      e.printStackTrace();
    }

  }

  private String pickColumn(String table) {
    List columns = (List) tableToColumns.get(table);
    int size = columns.size();

    return (String) columns.get(random.nextInt(size));
  }

  public static void usage() {
    System.out.println(
        "Usage:  java [-DnoComplex=true] [-DstatementLeak=true] test.JDStatementTimeoutStress URL userid password SCHEMA runSeconds timeoutSeconds  threads");
    System.out.println(
        "        Uses thread threads to execute (with timeout) complex quieres against the tables in SCHEMA");
  }

  public static void main(String[] args) {
    if (args.length < 7) {
      usage();
      return;
    }
    System.out.println("java.home is "+System.getProperty("java.home")); 
    try {
      String url = args[0];
      String userid = args[1];
      String password = args[2];
      String schema = args[3];
      int runSeconds = Integer.parseInt(args[4]);
      if (runSeconds <= 0) {
        System.out.println("invalid runSeconds=" + runSeconds);
        usage();
        return;
      }

      int timeoutSeconds = Integer.parseInt(args[5]);
      if (timeoutSeconds <= 0) {
        System.out.println("invalid timeout=" + timeoutSeconds);
        usage();
        return;
      }
      int threadCount = Integer.parseInt(args[6]);
      if (threadCount <= 0) {
        System.out.println("invalid threads=" + threadCount);
        usage();
        return;

      }

      System.out.println("Creating threads"); 
      JDStatementTimeoutStress runnables[] = new JDStatementTimeoutStress[threadCount];
      Thread threads[] = new Thread[threadCount]; 
      for (int i = 0; i < threadCount; i++) { 
        runnables[i] =  new JDStatementTimeoutStress(url, userid,
          password, schema, runSeconds, timeoutSeconds, "Thread"+(i+1)+"-of-"+threadCount);
        threads[i] = new Thread(runnables[i]); 
      }
      System.out.println("Starting threads"); 
      for (int i = 0; i < threadCount; i++) { 
        threads[i].start();  
      }
      System.out.println("Waiting for threads"); 
      for (int i = 0; i < threadCount; i++) {
        while (threads[i].isAlive()) {
            threads[i].join(2000);
            System.gc(); 
        }
      }
      System.out.println("Done with test"); 
      
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
