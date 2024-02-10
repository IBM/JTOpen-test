///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDPerformance.java
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
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDMDPerformance.java
//
// Classes:      JDDMDPerformance
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import java.sql.*;
import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDPerformanceUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
Testcase JDDMDPerformance.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>isPerformanceFor()
<li>unwrap()
</ul>
**/
public class JDDMDPerformance
extends JDTestcase {
    public final static String added = " -- added by native driver 05/03/2006"; 


    // Private data.
    private              Connection     connection_;
    private              DatabaseMetaData dmd_; 
    private              Statement        stmt_; 
    private              String           driverName = "UNKNOWN"; 
    private              int             tableColumnCount = 0;  
    private              int             tableCount=0; 
    private              int             indexCount = 0; 
    private              Vector          tableNames = new Vector(); 

/**
Constructor.
**/
    public JDDMDPerformance (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password) {
        super (systemObject, "JDDMDPerformance",
               namesAndVars, runMode, fileOutputStream,
               password);
    }


   void dropTableSilent(String[][] columnsTable)  {
      try { 
        dropTable(columnsTable);
      } catch (Exception e) { 
        
      }
   }
    
   void dropTable(String[][] columnsTable) throws Exception {
     String sql = "DROP TABLE "+  JDDMDTest.COLLECTION +"." +columnsTable[0][0];
     stmt_.executeUpdate(sql); 
   }

   void dropIndexSilent(String[][] indexTable)  {
     try { 
       dropIndex(indexTable);
     } catch (Exception e) { 
       
     }
  }

   void dropIndex(String[][] indexTable) throws Exception {
     String sql = "DROP Index "+  JDDMDTest.COLLECTION +"." + indexTable[0][0];
     stmt_.executeUpdate(sql); 
   }

   void createTable(String[][] columnsTable) throws Exception {
     String tableName = columnsTable[0][0]; 
     StringBuffer sql = new StringBuffer("CREATE TABLE "); 
     sql.append(JDDMDTest.COLLECTION);
     sql.append("."); 
     sql.append(tableName); 
     tableNames.addElement(tableName); 
     sql.append("(") ; 
     boolean first = true; 
     for (int i = 1; i < columnsTable.length ; i++ ) {
       if (first) {
         first=false;
       } else {
         sql.append(","); 
       }
       sql.append(columnsTable[i][0]);
       sql.append(" "); 
       sql.append(columnsTable[i][1]);
     }
     sql.append(")"); 
     
     stmt_.executeUpdate(sql.toString()); 
     tableColumnCount += columnsTable.length -1; 
     tableCount++; 
   }

   
   void createIndex(String[][] indexTable) throws Exception {
     String indexName = indexTable[0][0]; 
     String tableName = indexTable[0][1]; 
     StringBuffer sql = new StringBuffer("CREATE INDEX "); 
     sql.append(JDDMDTest.COLLECTION);
     sql.append(".");
     sql.append(indexName); 
     sql.append(" ON "); 
     sql.append(JDDMDTest.COLLECTION);
     sql.append(".");
     sql.append(tableName); 
     sql.append("(") ; 
     boolean first = true; 
     for (int i = 1; i < indexTable.length ; i++ ) {
       if (first) {
         first=false;
       } else {
         sql.append(","); 
       }
       sql.append(indexTable[i][0]);
       indexCount++; 
     }
     sql.append(")"); 
     
     stmt_.executeUpdate(sql.toString()); 
   }

   String tableNamePrefix="JDDMDPRF";
   String indexNamePrefix="JDDMDPRI"; 
   String [][] indexTable1 = {
       {indexNamePrefix+"01",tableNamePrefix+"01"},
       {"COLUMN1"}
   };
   String [][] columnsTable1 = {
       {tableNamePrefix+"01"},
       {"COLUMN1",   "SMALLINT"}, 
       {"COLUMN2",   "INTEGER"}, 
       {"COLUMN3",   "BIGINT"}, 
       {"COLUMN4",   "REAL"}, 
       {"COLUMN5",   "FLOAT"}, 
       {"COLUMN6",   "DOUBLE"}, 
       {"COLUMN7",   "DECIMAL(5,2)"}, 
       {"COLUMN8",   "NUMERIC(5,2)"}, 
       {"COLUMN9",   "CHAR(10)"}, 
       {"COLUMN10",  "VARCHAR(10)"}, 
       {"COLUMN11",  "BINARY(10)"}, 
       {"COLUMN12",  "VARBINARY(10)"}, 
       {"COLUMN13",  "GRAPHIC(10) CCSID 13488"}, 
       {"COLUMN14",  "VARGRAPHIC(10) CCSID 13488 "}, 
       {"COLUMN15",  "DATE"}, 
       {"COLUMN16",  "TIME"}, 
       {"COLUMN17",  "TIMESTAMP"}, 
       {"COLUMN18",  "CLOB(100)"}, 
       {"COLUMN19",  "BLOB(100)"}, 
       {"COLUMN20",  "DBCLOB(100) CCSID 13488 "}, 
       {"COLUMN21",  "VARCHAR(10) CCSID 1208"}, 
       {"COLUMN22",  "VARCHAR(10) CCSID 500 "}, 
       {"COLUMN23",  "VARCHAR(10) CCSID 37"}, 
       {"COLUMN24",  "INTEGER"}, 
       {"COLUMN25",  "INTEGER"}, 
       {"COLUMN26",  "INTEGER"}, 
       {"COLUMN27",  "INTEGER"}, 
       {"COLUMN28",  "INTEGER"}, 
       {"COLUMN29",  "INTEGER"}, 
       {"COLUMN30",  "INTEGER"}, 
   };

   String [][] indexTable2 = {
       {indexNamePrefix+"02",tableNamePrefix+"02"},
       {"COLUMN2"}
   };
   String [][] columnsTable2 = {
       {tableNamePrefix+"02"},
       {"COLUMN1",   "SMALLINT"}, 
       {"COLUMN2",   "INTEGER"}, 
       {"COLUMN3",   "BIGINT"}, 
       {"COLUMN4",   "REAL"}, 
       {"COLUMN5",   "FLOAT"}, 
       {"COLUMN6",   "DOUBLE"}, 
       {"COLUMN7",   "DECIMAL(5,2)"}, 
       {"COLUMN8",   "NUMERIC(5,2)"}, 
       {"COLUMN9",   "CHAR(10)"}, 
       {"COLUMN10",  "VARCHAR(10)"}, 
       {"COLUMN11",  "BINARY(10)"}, 
       {"COLUMN12",  "VARBINARY(10)"}, 
       {"COLUMN13",  "GRAPHIC(10) CCSID 13488"}, 
       {"COLUMN14",  "VARGRAPHIC(10) CCSID 13488 "}, 
       {"COLUMN15",  "DATE"}, 
       {"COLUMN16",  "TIME"}, 
       {"COLUMN17",  "TIMESTAMP"}, 
       {"COLUMN18",  "CLOB(100)"}, 
       {"COLUMN19",  "BLOB(100)"}, 
       {"COLUMN20",  "DBCLOB(100) CCSID 13488 "}, 
       {"COLUMN21",  "VARCHAR(10) CCSID 1208"}, 
       {"COLUMN22",  "VARCHAR(10) CCSID 500 "}, 
       {"COLUMN23",  "VARCHAR(10) CCSID 37"}, 
       {"COLUMN24",  "INTEGER"}, 
       {"COLUMN25",  "INTEGER"}, 
       {"COLUMN26",  "INTEGER"}, 
       {"COLUMN27",  "INTEGER"}, 
       {"COLUMN28",  "INTEGER"}, 
       {"COLUMN29",  "INTEGER"}, 
       {"COLUMN30",  "INTEGER"}, 
   };

   String [][] indexTable3 = {
       {indexNamePrefix+"03",tableNamePrefix+"03"},
       {"COLUMN3"}
   };

   String [][] columnsTable3 = {
       {tableNamePrefix+"03"},
       {"COLUMN1",   "SMALLINT"}, 
       {"COLUMN2",   "INTEGER"}, 
       {"COLUMN3",   "BIGINT"}, 
       {"COLUMN4",   "REAL"}, 
       {"COLUMN5",   "FLOAT"}, 
       {"COLUMN6",   "DOUBLE"}, 
       {"COLUMN7",   "DECIMAL(5,2)"}, 
       {"COLUMN8",   "NUMERIC(5,2)"}, 
       {"COLUMN9",   "CHAR(10)"}, 
       {"COLUMN10",  "VARCHAR(10)"}, 
       {"COLUMN11",  "BINARY(10)"}, 
       {"COLUMN12",  "VARBINARY(10)"}, 
       {"COLUMN13",  "GRAPHIC(10) CCSID 13488"}, 
       {"COLUMN14",  "VARGRAPHIC(10) CCSID 13488 "}, 
       {"COLUMN15",  "DATE"}, 
       {"COLUMN16",  "TIME"}, 
       {"COLUMN17",  "TIMESTAMP"}, 
       {"COLUMN18",  "CLOB(100)"}, 
       {"COLUMN19",  "BLOB(100)"}, 
       {"COLUMN20",  "DBCLOB(100) CCSID 13488 "}, 
       {"COLUMN21",  "VARCHAR(10) CCSID 1208"}, 
       {"COLUMN22",  "VARCHAR(10) CCSID 500 "}, 
       {"COLUMN23",  "VARCHAR(10) CCSID 37"}, 
       {"COLUMN24",  "INTEGER"}, 
       {"COLUMN25",  "INTEGER"}, 
       {"COLUMN26",  "INTEGER"}, 
       {"COLUMN27",  "INTEGER"}, 
       {"COLUMN28",  "INTEGER"}, 
       {"COLUMN29",  "INTEGER"}, 
       {"COLUMN30",  "INTEGER"}, 
   };

   String [][] indexTable4 = {
       {indexNamePrefix+"04",tableNamePrefix+"04"},
       {"COLUMN4"},
       {"COLUMN3"}
   };

   String [][] columnsTable4 = {
       {tableNamePrefix+"04"},
       {"COLUMN1",   "SMALLINT"}, 
       {"COLUMN2",   "INTEGER"}, 
       {"COLUMN3",   "BIGINT"}, 
       {"COLUMN4",   "REAL"}, 
       {"COLUMN5",   "FLOAT"}, 
       {"COLUMN6",   "DOUBLE"}, 
       {"COLUMN7",   "DECIMAL(5,2)"}, 
       {"COLUMN8",   "NUMERIC(5,2)"}, 
       {"COLUMN9",   "CHAR(10)"}, 
       {"COLUMN10",  "VARCHAR(10)"}, 
       {"COLUMN11",  "BINARY(10)"}, 
       {"COLUMN12",  "VARBINARY(10)"}, 
       {"COLUMN13",  "GRAPHIC(10) CCSID 13488"}, 
       {"COLUMN14",  "VARGRAPHIC(10) CCSID 13488 "}, 
       {"COLUMN15",  "DATE"}, 
       {"COLUMN16",  "TIME"}, 
       {"COLUMN17",  "TIMESTAMP"}, 
       {"COLUMN18",  "CLOB(100)"}, 
       {"COLUMN19",  "BLOB(100)"}, 
       {"COLUMN20",  "DBCLOB(100) CCSID 13488 "}, 
       {"COLUMN21",  "VARCHAR(10) CCSID 1208"}, 
       {"COLUMN22",  "VARCHAR(10) CCSID 500 "}, 
       {"COLUMN23",  "VARCHAR(10) CCSID 37"}, 
       {"COLUMN24",  "INTEGER"}, 
       {"COLUMN25",  "INTEGER"}, 
       {"COLUMN26",  "INTEGER"}, 
       {"COLUMN27",  "INTEGER"}, 
       {"COLUMN28",  "INTEGER"}, 
       {"COLUMN29",  "INTEGER"}, 
       {"COLUMN30",  "INTEGER"}, 
   };

   String [][] indexTable5 = {
       {indexNamePrefix+"05",tableNamePrefix+"05"},
       {"COLUMN5"}
   };

   String [][] columnsTable5 = {
       {tableNamePrefix+"05"},
       {"COLUMN1",   "SMALLINT"}, 
       {"COLUMN2",   "INTEGER"}, 
       {"COLUMN3",   "BIGINT"}, 
       {"COLUMN4",   "REAL"}, 
       {"COLUMN5",   "FLOAT"}, 
       {"COLUMN6",   "DOUBLE"}, 
       {"COLUMN7",   "DECIMAL(5,2)"}, 
       {"COLUMN8",   "NUMERIC(5,2)"}, 
       {"COLUMN9",   "CHAR(10)"}, 
       {"COLUMN10",  "VARCHAR(10)"}, 
       {"COLUMN11",  "BINARY(10)"}, 
       {"COLUMN12",  "VARBINARY(10)"}, 
       {"COLUMN13",  "GRAPHIC(10) CCSID 13488"}, 
       {"COLUMN14",  "VARGRAPHIC(10) CCSID 13488 "}, 
       {"COLUMN15",  "DATE"}, 
       {"COLUMN16",  "TIME"}, 
       {"COLUMN17",  "TIMESTAMP"}, 
       {"COLUMN18",  "CLOB(100)"}, 
       {"COLUMN19",  "BLOB(100)"}, 
       {"COLUMN20",  "DBCLOB(100) CCSID 13488 "}, 
       {"COLUMN21",  "VARCHAR(10) CCSID 1208"}, 
       {"COLUMN22",  "VARCHAR(10) CCSID 500 "}, 
       {"COLUMN23",  "VARCHAR(10) CCSID 37"}, 
       {"COLUMN24",  "INTEGER"}, 
       {"COLUMN25",  "INTEGER"}, 
       {"COLUMN26",  "INTEGER"}, 
       {"COLUMN27",  "INTEGER"}, 
       {"COLUMN28",  "INTEGER"}, 
       {"COLUMN29",  "INTEGER"}, 
       {"COLUMN30",  "INTEGER"}, 
   };

   String [][] indexTable6 = {
       {indexNamePrefix+"06",tableNamePrefix+"06"},
       {"COLUMN6"}
   };

   String [][] columnsTable6 = {
       {tableNamePrefix+"06"},
       {"COLUMN1",   "SMALLINT"}, 
       {"COLUMN2",   "INTEGER"}, 
       {"COLUMN3",   "BIGINT"}, 
       {"COLUMN4",   "REAL"}, 
       {"COLUMN5",   "FLOAT"}, 
       {"COLUMN6",   "DOUBLE"}, 
       {"COLUMN7",   "DECIMAL(5,2)"}, 
       {"COLUMN8",   "NUMERIC(5,2)"}, 
       {"COLUMN9",   "CHAR(10)"}, 
       {"COLUMN10",  "VARCHAR(10)"}, 
       {"COLUMN11",  "BINARY(10)"}, 
       {"COLUMN12",  "VARBINARY(10)"}, 
       {"COLUMN13",  "GRAPHIC(10) CCSID 13488"}, 
       {"COLUMN14",  "VARGRAPHIC(10) CCSID 13488 "}, 
       {"COLUMN15",  "DATE"}, 
       {"COLUMN16",  "TIME"}, 
       {"COLUMN17",  "TIMESTAMP"}, 
       {"COLUMN18",  "CLOB(100)"}, 
       {"COLUMN19",  "BLOB(100)"}, 
       {"COLUMN20",  "DBCLOB(100) CCSID 13488 "}, 
       {"COLUMN21",  "VARCHAR(10) CCSID 1208"}, 
       {"COLUMN22",  "VARCHAR(10) CCSID 500 "}, 
       {"COLUMN23",  "VARCHAR(10) CCSID 37"}, 
       {"COLUMN24",  "INTEGER"}, 
       {"COLUMN25",  "INTEGER"}, 
       {"COLUMN26",  "INTEGER"}, 
       {"COLUMN27",  "INTEGER"}, 
       {"COLUMN28",  "INTEGER"}, 
       {"COLUMN29",  "INTEGER"}, 
       {"COLUMN30",  "INTEGER"}, 
   };

   String [][] indexTable7 = {
       {indexNamePrefix+"07",tableNamePrefix+"07"},
       {"COLUMN7"}
   };

   String [][] columnsTable7 = {
       {tableNamePrefix+"07"},
       {"COLUMN1",   "SMALLINT"}, 
       {"COLUMN2",   "INTEGER"}, 
       {"COLUMN3",   "BIGINT"}, 
       {"COLUMN4",   "REAL"}, 
       {"COLUMN5",   "FLOAT"}, 
       {"COLUMN6",   "DOUBLE"}, 
       {"COLUMN7",   "DECIMAL(5,2)"}, 
       {"COLUMN8",   "NUMERIC(5,2)"}, 
       {"COLUMN9",   "CHAR(10)"}, 
       {"COLUMN10",  "VARCHAR(10)"}, 
       {"COLUMN11",  "BINARY(10)"}, 
       {"COLUMN12",  "VARBINARY(10)"}, 
       {"COLUMN13",  "GRAPHIC(10) CCSID 13488"}, 
       {"COLUMN14",  "VARGRAPHIC(10) CCSID 13488 "}, 
       {"COLUMN15",  "DATE"}, 
       {"COLUMN16",  "TIME"}, 
       {"COLUMN17",  "TIMESTAMP"}, 
       {"COLUMN18",  "CLOB(100)"}, 
       {"COLUMN19",  "BLOB(100)"}, 
       {"COLUMN20",  "DBCLOB(100) CCSID 13488 "}, 
       {"COLUMN21",  "VARCHAR(10) CCSID 1208"}, 
       {"COLUMN22",  "VARCHAR(10) CCSID 500 "}, 
       {"COLUMN23",  "VARCHAR(10) CCSID 37"}, 
       {"COLUMN24",  "INTEGER"}, 
       {"COLUMN25",  "INTEGER"}, 
       {"COLUMN26",  "INTEGER"}, 
       {"COLUMN27",  "INTEGER"}, 
       {"COLUMN28",  "INTEGER"}, 
       {"COLUMN29",  "INTEGER"}, 
       {"COLUMN30",  "INTEGER"}, 
   };

   String [][] indexTable8 = {
       {indexNamePrefix+"08",tableNamePrefix+"08"},
       {"COLUMN8"}
   };

   String [][] columnsTable8 = {
       {tableNamePrefix+"08"},
       {"COLUMN1",   "SMALLINT"}, 
       {"COLUMN2",   "INTEGER"}, 
       {"COLUMN3",   "BIGINT"}, 
       {"COLUMN4",   "REAL"}, 
       {"COLUMN5",   "FLOAT"}, 
       {"COLUMN6",   "DOUBLE"}, 
       {"COLUMN7",   "DECIMAL(5,2)"}, 
       {"COLUMN8",   "NUMERIC(5,2)"}, 
       {"COLUMN9",   "CHAR(10)"}, 
       {"COLUMN10",  "VARCHAR(10)"}, 
       {"COLUMN11",  "BINARY(10)"}, 
       {"COLUMN12",  "VARBINARY(10)"}, 
       {"COLUMN13",  "GRAPHIC(10) CCSID 13488"}, 
       {"COLUMN14",  "VARGRAPHIC(10) CCSID 13488 "}, 
       {"COLUMN15",  "DATE"}, 
       {"COLUMN16",  "TIME"}, 
       {"COLUMN17",  "TIMESTAMP"}, 
       {"COLUMN18",  "CLOB(100)"}, 
       {"COLUMN19",  "BLOB(100)"}, 
       {"COLUMN20",  "DBCLOB(100) CCSID 13488 "}, 
       {"COLUMN21",  "VARCHAR(10) CCSID 1208"}, 
       {"COLUMN22",  "VARCHAR(10) CCSID 500 "}, 
       {"COLUMN23",  "VARCHAR(10) CCSID 37"}, 
       {"COLUMN24",  "INTEGER"}, 
       {"COLUMN25",  "INTEGER"}, 
       {"COLUMN26",  "INTEGER"}, 
       {"COLUMN27",  "INTEGER"}, 
       {"COLUMN28",  "INTEGER"}, 
       {"COLUMN29",  "INTEGER"}, 
       {"COLUMN30",  "INTEGER"}, 
   };

   String [][] indexTable9 = {
       {indexNamePrefix+"09",tableNamePrefix+"09"},
       {"COLUMN9"}
   };

   String [][] columnsTable9 = {
       {tableNamePrefix+"09"},
       {"COLUMN1",   "SMALLINT"}, 
       {"COLUMN2",   "INTEGER"}, 
       {"COLUMN3",   "BIGINT"}, 
       {"COLUMN4",   "REAL"}, 
       {"COLUMN5",   "FLOAT"}, 
       {"COLUMN6",   "DOUBLE"}, 
       {"COLUMN7",   "DECIMAL(5,2)"}, 
       {"COLUMN8",   "NUMERIC(5,2)"}, 
       {"COLUMN9",   "CHAR(10)"}, 
       {"COLUMN10",  "VARCHAR(10)"}, 
       {"COLUMN11",  "BINARY(10)"}, 
       {"COLUMN12",  "VARBINARY(10)"}, 
       {"COLUMN13",  "GRAPHIC(10) CCSID 13488"}, 
       {"COLUMN14",  "VARGRAPHIC(10) CCSID 13488 "}, 
       {"COLUMN15",  "DATE"}, 
       {"COLUMN16",  "TIME"}, 
       {"COLUMN17",  "TIMESTAMP"}, 
       {"COLUMN18",  "CLOB(100)"}, 
       {"COLUMN19",  "BLOB(100)"}, 
       {"COLUMN20",  "DBCLOB(100) CCSID 13488 "}, 
       {"COLUMN21",  "VARCHAR(10) CCSID 1208"}, 
       {"COLUMN22",  "VARCHAR(10) CCSID 500 "}, 
       {"COLUMN23",  "VARCHAR(10) CCSID 37"}, 
       {"COLUMN24",  "INTEGER"}, 
       {"COLUMN25",  "INTEGER"}, 
       {"COLUMN26",  "INTEGER"}, 
       {"COLUMN27",  "INTEGER"}, 
       {"COLUMN28",  "INTEGER"}, 
       {"COLUMN29",  "INTEGER"}, 
       {"COLUMN30",  "INTEGER"}, 
   };

   String [][] indexTable10 = {
       {indexNamePrefix+"10",tableNamePrefix+"10"},
       {"COLUMN15"}
   };

   String [][] columnsTable10 = {
       {tableNamePrefix+"10"},
       {"COLUMN1",   "SMALLINT"}, 
       {"COLUMN2",   "INTEGER"}, 
       {"COLUMN3",   "BIGINT"}, 
       {"COLUMN4",   "REAL"}, 
       {"COLUMN5",   "FLOAT"}, 
       {"COLUMN6",   "DOUBLE"}, 
       {"COLUMN7",   "DECIMAL(5,2)"}, 
       {"COLUMN8",   "NUMERIC(5,2)"}, 
       {"COLUMN9",   "CHAR(10)"}, 
       {"COLUMN10",  "VARCHAR(10)"}, 
       {"COLUMN11",  "BINARY(10)"}, 
       {"COLUMN12",  "VARBINARY(10)"}, 
       {"COLUMN13",  "GRAPHIC(10) CCSID 13488"}, 
       {"COLUMN14",  "VARGRAPHIC(10) CCSID 13488 "}, 
       {"COLUMN15",  "DATE"}, 
       {"COLUMN16",  "TIME"}, 
       {"COLUMN17",  "TIMESTAMP"}, 
       {"COLUMN18",  "CLOB(100)"}, 
       {"COLUMN19",  "BLOB(100)"}, 
       {"COLUMN20",  "DBCLOB(100) CCSID 13488 "}, 
       {"COLUMN21",  "VARCHAR(10) CCSID 1208"}, 
       {"COLUMN22",  "VARCHAR(10) CCSID 500 "}, 
       {"COLUMN23",  "VARCHAR(10) CCSID 37"}, 
       {"COLUMN24",  "INTEGER"}, 
       {"COLUMN25",  "INTEGER"}, 
       {"COLUMN26",  "INTEGER"}, 
       {"COLUMN27",  "INTEGER"}, 
       {"COLUMN28",  "INTEGER"}, 
       {"COLUMN29",  "INTEGER"}, 
       {"COLUMN30",  "INTEGER"}, 
   };

   
/**
Performs setup needed before running variations.
@exception Exception If an exception occurs.
**/
    protected void setup() throws Exception {
      
      if ((getRelease () >= JDTestDriver.RELEASE_V5R4M0)) {
    connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
    dmd_ = connection_.getMetaData();
    stmt_ = connection_.createStatement();

    dropIndexSilent(indexTable1);
    dropIndexSilent(indexTable2);
    dropIndexSilent(indexTable3);
    dropIndexSilent(indexTable4);
    dropIndexSilent(indexTable5);
    dropIndexSilent(indexTable6);
    dropIndexSilent(indexTable7);
    dropIndexSilent(indexTable8);
    dropIndexSilent(indexTable9);
    dropIndexSilent(indexTable10);

    dropTableSilent(columnsTable1);
    dropTableSilent(columnsTable2);
    dropTableSilent(columnsTable3);
    dropTableSilent(columnsTable4);
    dropTableSilent(columnsTable5);
    dropTableSilent(columnsTable6);
    dropTableSilent(columnsTable7);
    dropTableSilent(columnsTable8);
    dropTableSilent(columnsTable9);
    dropTableSilent(columnsTable10);

    createTable(columnsTable1);
    createTable(columnsTable2);
    createTable(columnsTable3);
    createTable(columnsTable4);
    createTable(columnsTable5);
    createTable(columnsTable6);
    createTable(columnsTable7);
    createTable(columnsTable8);
    createTable(columnsTable9);
    createTable(columnsTable10);

    createIndex(indexTable1);
    createIndex(indexTable2);
    createIndex(indexTable3);
    createIndex(indexTable4);
    createIndex(indexTable5);
    createIndex(indexTable6);
    createIndex(indexTable7);
    createIndex(indexTable8);
    createIndex(indexTable9);
    createIndex(indexTable10);
    
    int driver = getDriver();
    switch (driver) {
      case JDTestDriver.DRIVER_NATIVE:
        driverName = "NATIVE";
        break;
      case JDTestDriver.DRIVER_JCC:
        driverName = "JCC";
        break;
      case JDTestDriver.DRIVER_TOOLBOX:
        driverName = "TOOLBOX";
        break;

    }
      }
  }



/**
 * Performs cleanup needed after running variations.
 * 
 * @exception Exception
 *              If an exception occurs.
 */
    protected void cleanup ()
    throws Exception
    {
if ((getRelease () >= JDTestDriver.RELEASE_V5R4M0)) { 
      dropIndex(indexTable1);
      dropIndex(indexTable2);
      dropIndex(indexTable3);
      dropIndex(indexTable4);
      dropIndex(indexTable5);
      dropIndex(indexTable6);
      dropIndex(indexTable7);
      dropIndex(indexTable8);
      dropIndex(indexTable9);
      dropIndex(indexTable10);

        dropTable(columnsTable1);
        dropTable(columnsTable2);
        dropTable(columnsTable3);
        dropTable(columnsTable4);
        dropTable(columnsTable5);
        dropTable(columnsTable6);
        dropTable(columnsTable7);
        dropTable(columnsTable8);
        dropTable(columnsTable9);
        dropTable(columnsTable10);

        connection_.close ();
    }

    }



  /**
   * Measure the performance of getColumns. Make sure it is within 5% of the
   * last time it was run
   */
  public void Var001() {
    if (getRelease () >= JDTestDriver.RELEASE_V5R4M0) {
    String testname = "JDDMDPerf01SQLCOLUMNS";
    int rowCount = 0; 
    try {
      long startTime = System.currentTimeMillis(); 
      for (int i = 0; i < 10; i++) {
        ResultSet rs = dmd_.getColumns(null, JDDMDTest.COLLECTION,
            tableNamePrefix + "%", "%");
        int count = 0;
        while (rs.next()) {
          count++;
        }
        rowCount=count; 
        rs.close(); 
      }
      long endTime = System.currentTimeMillis();
      Timestamp ts = new Timestamp(System.currentTimeMillis());  
      
      JDPerformanceUtil.recordTestRun(connection_ , 
                                      ts,
                                      testname,
                                      driverName,
                                      (endTime - startTime)
                                      ); 

      JDPerformanceUtil.showRunComparision(connection_, testname, ts); 
      assertCondition(rowCount == tableColumnCount, "columnCount="+rowCount+" sb "+tableColumnCount+ added);
      
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
    } else {
      notApplicable("V5R4 or later variation"); 
    }
  }
  
  /**
   * Measure the performance of getTables. Make sure it is within 5% of the
   * last time it was run
   */
  public void Var002() {
    if (getRelease () >= JDTestDriver.RELEASE_V5R4M0) {
    String testname = "JDDMDPerf02SQLTABLES";
    int rowCount = 0; 
    try {
      long startTime = System.currentTimeMillis(); 
      for (int i = 0; i < 10; i++) {
        ResultSet rs = dmd_.getTables(null, JDDMDTest.COLLECTION,
            tableNamePrefix + "%",null);
        int count = 0;
        while (rs.next()) {
          count++;
        }
        rowCount=count; 
        rs.close(); 
      }
      long endTime = System.currentTimeMillis();
      Timestamp ts = new Timestamp(System.currentTimeMillis());  
      
      JDPerformanceUtil.recordTestRun(connection_ , 
                                      ts,
                                      testname,
                                      driverName,
                                      (endTime - startTime)
                                      ); 

      JDPerformanceUtil.showRunComparision(connection_, testname, ts); 
      assertCondition(rowCount == tableCount, "columnCount="+rowCount+" sb "+tableCount+ added);
      
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  } else {
    notApplicable("V5R4 or later variation"); 
  }
  }
  
  /**
   * Measure the performance of getIndexInfo. Make sure it is within 5% of the
   * last time it was run
   */
  public void Var003() {
    if (getRelease () >= JDTestDriver.RELEASE_V5R4M0) {
    String testname = "JDDMDPerf03SQLSTATISTICS";
    int rowCount = 0; 
    try {
      long startTime = System.currentTimeMillis(); 
      for (int i = 0; i < 10; i++) {
        int count = 0;
        Enumeration enumeration = tableNames.elements(); 
        while (enumeration.hasMoreElements() ) {
          String tableName = (String) enumeration.nextElement(); 
          ResultSet rs = dmd_.getIndexInfo(null, JDDMDTest.COLLECTION,
              tableName,false, false);
          while (rs.next()) {
            count++;
          }
          rs.close(); 
        }
        rowCount=count; 

      }
      long endTime = System.currentTimeMillis();
      Timestamp ts = new Timestamp(System.currentTimeMillis());  
      
      JDPerformanceUtil.recordTestRun(connection_ , 
                                      ts,
                                      testname,
                                      driverName,
                                      (endTime - startTime)
                                      ); 

      JDPerformanceUtil.showRunComparision(connection_, testname, ts); 
      assertCondition(rowCount >=  indexCount, "rows="+rowCount+" sb >= "+indexCount+ added);
      
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  } else {
    notApplicable("V5R4 or later variation"); 
  }
  }
  
 
  
}











