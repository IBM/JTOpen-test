///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: AS400JDBCEnableCALTestcase.java
//  Classes:   AS400JDBCEnableCALTestcase
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test.JD.AS;


import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.sql.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;

import test.JDASTest;
import test.JDReflectionUtil;
import test.JDTestcase;
import test.PasswordVault;
import test.SocketProxy;

/**
 * Testcase AS400JDBCEnableCALTestcase.
 **/
public class JDASTestcase extends JDTestcase {
  protected long TOOLBOX_FIX_DATE=20200101; 
  
  protected int MINIMUM_TRANSACTION_MILLISECONDS = 300;
  protected int RUN_TRANSACTIONS = 1000;

  protected String url_;

  public int killCount;

  
  protected boolean seamless_;

  protected static String[] setupSql = {
      "create or replace variable COLLECTION.GVCALTCINT INT",
      "create or replace table    COLLECTION.GVCALTCT1(C1 INT,C2 BIGINT)",
      "insert into                COLLECTION.GVCALTCT1 VALUES(2,0)",
      "create or replace table    COLLECTION.GVCALTCT2(C1 INT,C2 BIGINT)",
      "create or replace table    COLLECTION.GVCALTCI7(C1 INT,C2 INT, C3 INT, C4 INT, C5 INT, C6 INT, C7 INT)",
      "create or replace table    COLLECTION.GVCALTCSI7(C1 SMALLINT,C2 SMALLINT, C3 SMALLINT, C4 SMALLINT, C5 SMALLINT, C6 SMALLINT, C7 SMALLINT)",
      "create or replace table    COLLECTION.GVCALTCBI7(C1 BIGINT,C2 BIGINT, C3 BIGINT, C4 BIGINT, C5 BIGINT, C6 BIGINT, C7 BIGINT)",
      "create or replace table    COLLECTION.GVCALTCRL7(C1 REAL ,C2 REAL , C3 REAL , C4 REAL , C5 REAL , C6 REAL , C7 REAL )",
      "create or replace table    COLLECTION.GVCALTCFT7(C1 FLOAT,C2 FLOAT, C3 FLOAT, C4 FLOAT, C5 FLOAT, C6 FLOAT, C7 FLOAT)",
      "create or replace table    COLLECTION.GVCALTCDL7(C1 DOUBLE,C2 DOUBLE, C3 DOUBLE, C4 DOUBLE, C5 DOUBLE, C6 DOUBLE, C7 DOUBLE)",
      "create or replace table    COLLECTION.GVCALTCDC7(C1 DECIMAL(12,2),C2 DECIMAL(12,2), C3 DECIMAL(12,2), C4 DECIMAL(12,2), C5 DECIMAL(12,2), C6 DECIMAL(12,2), C7 DECIMAL(12,2))",
      "create or replace table    COLLECTION.GVCALTCNM7(C1 NUMERIC(12,2),C2 NUMERIC(12,2), C3 NUMERIC(12,2), C4 NUMERIC(12,2), C5 NUMERIC(12,2), C6 NUMERIC(12,2), C7 NUMERIC(12,2))",
      "create or replace table    COLLECTION.GVCALTCDF7(C1 DECFLOAT,C2 DECFLOAT, C3 DECFLOAT, C4 DECFLOAT, C5 DECFLOAT, C6 DECFLOAT, C7 DECFLOAT)",
      "create or replace table    COLLECTION.GVCALTCCH7(C1 CHAR(4),C2 CHAR(4), C3 CHAR(4), C4 CHAR(4), C5 CHAR(4), C6 CHAR(4), C7 CHAR(4))",
      "create or replace table    COLLECTION.GVCALTCVC7(C1 VARCHAR(4),C2 VARCHAR(4), C3 VARCHAR(4), C4 VARCHAR(4), C5 VARCHAR(4), C6 VARCHAR(4), C7 VARCHAR(4))",
      "create or replace table    COLLECTION.GVCALTCGR7(C1 GRAPHIC(4) CCSID 1200,C2 GRAPHIC(4) CCSID 1200, C3 GRAPHIC(4) CCSID 1200, C4 GRAPHIC(4) CCSID 1200, C5 GRAPHIC(4) CCSID 1200, C6 GRAPHIC(4) CCSID 1200, C7 GRAPHIC(4) CCSID 1200)",
      "create or replace table    COLLECTION.GVCALTCVG7(C1 VARGRAPHIC(4) CCSID 1200,C2 VARGRAPHIC(4) CCSID 1200, C3 VARGRAPHIC(4) CCSID 1200, C4 VARGRAPHIC(4) CCSID 1200, C5 VARGRAPHIC(4) CCSID 1200, C6 VARGRAPHIC(4) CCSID 1200, C7 VARGRAPHIC(4) CCSID 1200)",
      "create or replace table    COLLECTION.GVCALTCCL7(C1 CLOB(1M) ,C2 CLOB(1M) , C3 CLOB(1M) , C4 CLOB(1M) , C5 CLOB(1M) , C6 CLOB(1M) , C7 CLOB(1M) )",
      "create or replace table    COLLECTION.GVCALTCDB7(C1 DBCLOB(1M) CCSID 1200,C2 DBCLOB(1M) CCSID 1200, C3 DBCLOB(1M) CCSID 1200, C4 DBCLOB(1M) CCSID 1200, C5 DBCLOB(1M) CCSID 1200, C6 DBCLOB(1M) CCSID 1200, C7 DBCLOB(1M) CCSID 1200 )",
      "create or replace table    COLLECTION.GVCALTCBN7(C1 BINARY(4),C2 BINARY(4), C3 BINARY(4), C4 BINARY(4), C5 BINARY(4), C6 BINARY(4), C7 BINARY(4) )",
      "create or replace table    COLLECTION.GVCALTCVB7(C1 VARBINARY(4),C2 VARBINARY(4), C3 VARBINARY(4), C4 VARBINARY(4), C5 VARBINARY(4), C6 VARBINARY(4), C7 VARBINARY(4) )",
      "create or replace table    COLLECTION.GVCALTCBL7(C1 BLOB(1M) ,C2 BLOB(1M) , C3 BLOB(1M) , C4 BLOB(1M) , C5 BLOB(1M) , C6 BLOB(1M) , C7 BLOB(1M) )",
      "create or replace table    COLLECTION.GVCALTCDT7(C1 DATE,C2 DATE, C3 DATE, C4 DATE, C5 DATE, C6 DATE, C7 DATE )",
      "create or replace table    COLLECTION.GVCALTCTM7(C1 TIME,C2 TIME, C3 TIME, C4 TIME, C5 TIME, C6 TIME, C7 TIME )",
      "create or replace table    COLLECTION.GVCALTCTS7(C1 TIMESTAMP,C2 TIMESTAMP, C3 TIMESTAMP, C4 TIMESTAMP, C5 TIMESTAMP, C6 TIMESTAMP, C7 TIMESTAMP )",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCTM() RESULT SETS 1 "
          + "LANGUAGE SQL BEGIN  DECLARE c1 CURSOR FOR "
          + "SELECT CURRENT TIMESTAMP from SYSIBM.SYSDUMMY1;  "
          + "OPEN c1 ;  SET RESULT SETS CURSOR c1;  END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCP1(P1 VARCHAR(80) ) RESULT SETS 1 "
          + "LANGUAGE SQL BEGIN  DECLARE c1 CURSOR FOR "
          + "SELECT P1 from SYSIBM.SYSDUMMY1;  "
          + "OPEN c1 ;  SET RESULT SETS CURSOR c1;  END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCI3 (IN P1 INT, INOUT P2 INT, OUT P3 INT) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 *2, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 * 2;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCI6"
          + " (IN P1 INT, IN P2 INT,  INOUT P3 INT, INOUT P4 INT, OUT P5 INT, OUT P6 INT) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 *2, P4 *2, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 * 2; " + "SET P4 = P4 * 2;"
          + "SET P5 = P1; " + "SET P6 = P2; " + "SET RESULT SETS CURSOR c1;  "
          + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCSI3 (IN P1 SMALLINT, INOUT P2 SMALLINT, OUT P3 SMALLINT) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 *2, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 * 2;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCSI6"
          + " (IN P1 SMALLINT, IN P2 SMALLINT,  INOUT P3 SMALLINT, INOUT P4 SMALLINT, OUT P5 SMALLINT, OUT P6 SMALLINT) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 *2, P4 *2, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 * 2; " + "SET P4 = P4 * 2;"
          + "SET P5 = P1; " + "SET P6 = P2; " + "SET RESULT SETS CURSOR c1;  "
          + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCBI3 (IN P1 BIGINT, INOUT P2 BIGINT, OUT P3 BIGINT) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 *2, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 * 2;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCBI6"
          + " (IN P1 BIGINT, IN P2 BIGINT,  INOUT P3 BIGINT, INOUT P4 BIGINT, OUT P5 BIGINT, OUT P6 BIGINT) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 *2, P4 *2, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 * 2; " + "SET P4 = P4 * 2;"
          + "SET P5 = P1; " + "SET P6 = P2; " + "SET RESULT SETS CURSOR c1;  "
          + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCRL3 (IN P1 REAL, INOUT P2 REAL, OUT P3 REAL) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 *2, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 * 2;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCRL6"
          + " (IN P1 REAL, IN P2 REAL,  INOUT P3 REAL, INOUT P4 REAL, OUT P5 REAL, OUT P6 REAL) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 *2, P4 *2, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 * 2; " + "SET P4 = P4 * 2;"
          + "SET P5 = P1; " + "SET P6 = P2; " + "SET RESULT SETS CURSOR c1;  "
          + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCFT3 (IN P1 FLOAT, INOUT P2 FLOAT, OUT P3 FLOAT) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 *2, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 * 2;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCFT6"
          + " (IN P1 FLOAT, IN P2 FLOAT,  INOUT P3 FLOAT, INOUT P4 FLOAT, OUT P5 FLOAT, OUT P6 FLOAT) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 *2, P4 *2, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 * 2; " + "SET P4 = P4 * 2;"
          + "SET P5 = P1; " + "SET P6 = P2; " + "SET RESULT SETS CURSOR c1;  "
          + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCDL3 (IN P1 DOUBLE, INOUT P2 DOUBLE, OUT P3 DOUBLE) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 *2, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 * 2;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCDL6"
          + " (IN P1 DOUBLE, IN P2 DOUBLE,  INOUT P3 DOUBLE, INOUT P4 DOUBLE, OUT P5 DOUBLE, OUT P6 DOUBLE) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 *2, P4 *2, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 * 2; " + "SET P4 = P4 * 2;"
          + "SET P5 = P1; " + "SET P6 = P2; " + "SET RESULT SETS CURSOR c1;  "
          + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCDC3 (IN P1 DECIMAL(12,2), INOUT P2 DECIMAL(12,2), OUT P3 DECIMAL(12,2)) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 *2, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 * 2;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCDC6"
          + " (IN P1 DECIMAL(12,2), IN P2 DECIMAL(12,2),  INOUT P3 DECIMAL(12,2), INOUT P4 DECIMAL(12,2), OUT P5 DECIMAL(12,2), OUT P6 DECIMAL(12,2)) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 *2, P4 *2, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 * 2; " + "SET P4 = P4 * 2;"
          + "SET P5 = P1; " + "SET P6 = P2; " + "SET RESULT SETS CURSOR c1;  "
          + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCNM3 (IN P1 NUMERIC(12,2), INOUT P2 NUMERIC(12,2), OUT P3 NUMERIC(12,2)) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 *2, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 * 2;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCNM6"
          + " (IN P1 NUMERIC(12,2), IN P2 NUMERIC(12,2),  INOUT P3 NUMERIC(12,2), INOUT P4 NUMERIC(12,2), OUT P5 NUMERIC(12,2), OUT P6 NUMERIC(12,2)) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 *2, P4 *2, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 * 2; " + "SET P4 = P4 * 2;"
          + "SET P5 = P1; " + "SET P6 = P2; " + "SET RESULT SETS CURSOR c1;  "
          + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCDF3 (IN P1 DECFLOAT, INOUT P2 DECFLOAT, OUT P3 DECFLOAT) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 *2, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 * 2;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCDF6"
          + " (IN P1 DECFLOAT, IN P2 DECFLOAT,  INOUT P3 DECFLOAT, INOUT P4 DECFLOAT, OUT P5 DECFLOAT, OUT P6 DECFLOAT) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 *2, P4 *2, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 * 2; " + "SET P4 = P4 * 2;"
          + "SET P5 = P1; " + "SET P6 = P2; " + "SET RESULT SETS CURSOR c1;  "
          + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCCH3 (IN P1 CHAR(8), INOUT P2 CHAR(8), OUT P3 CHAR(8)) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, CAST(CONCAT(TRIM(P2),P2) AS CHAR(8)), P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = CONCAT(TRIM(P2),P2);"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCCH6"
          + " (IN P1 CHAR(8), IN P2 CHAR(8),  INOUT P3 CHAR(8), INOUT P4 CHAR(8), OUT P5 CHAR(8), OUT P6 CHAR(8)) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, CAST(CONCAT(TRIM(P3),P3) AS CHAR(8)), CAST(CONCAT(TRIM(P4),P4) AS CHAR(8)), P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = CONCAT(TRIM(P3),P3); "
          + "SET P4 = CONCAT(TRIM(P4),P4);" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCVC3 (IN P1 VARCHAR(8), INOUT P2 VARCHAR(8), OUT P3 VARCHAR(8)) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, CONCAT(P2,P2), P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = CONCAT(P2,P2);"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCVC6"
          + " (IN P1 VARCHAR(8), IN P2 VARCHAR(8),  INOUT P3 VARCHAR(8), INOUT P4 VARCHAR(8), OUT P5 VARCHAR(8), OUT P6 VARCHAR(8)) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, CONCAT(P3,P3), CONCAT(P4,P4), P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = CONCAT(P3,P3); "
          + "SET P4 = CONCAT(P4,P4);" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCGR3 (IN P1 GRAPHIC(8) CCSID 1200, INOUT P2 GRAPHIC(8) CCSID 1200, OUT P3 GRAPHIC(8) CCSID 1200) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, CAST(CONCAT(TRIM(P2),P2) AS GRAPHIC(8) CCSID 1200), P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = CONCAT(TRIM(P2),P2);"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCGR6"
          + " (IN P1 GRAPHIC(8) CCSID 1200, IN P2 GRAPHIC(8) CCSID 1200,  INOUT P3 GRAPHIC(8) CCSID 1200, INOUT P4 GRAPHIC(8) CCSID 1200, OUT P5 GRAPHIC(8) CCSID 1200, OUT P6 GRAPHIC(8) CCSID 1200) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, CAST (CONCAT(TRIM(P3),P3)AS GRAPHIC(8) CCSID 1200), CAST(CONCAT(TRIM(P4),P4)AS GRAPHIC(8) CCSID 1200), P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = CONCAT(TRIM(P3),P3); "
          + "SET P4 = CONCAT(TRIM(P4),P4);" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCVG3 (IN P1 VARGRAPHIC(8) CCSID 1200, INOUT P2 VARGRAPHIC(8) CCSID 1200, OUT P3 VARGRAPHIC(8) CCSID 1200) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, CONCAT(P2,P2), P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = CONCAT(P2,P2);"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCVG6"
          + " (IN P1 VARGRAPHIC(8) CCSID 1200, IN P2 VARGRAPHIC(8) CCSID 1200,  INOUT P3 VARGRAPHIC(8) CCSID 1200, INOUT P4 VARGRAPHIC(8) CCSID 1200, OUT P5 VARGRAPHIC(8) CCSID 1200, OUT P6 VARGRAPHIC(8) CCSID 1200) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, CONCAT(P3,P3), CONCAT(P4,P4), P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = CONCAT(P3,P3); "
          + "SET P4 = CONCAT(P4,P4);" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCCL3 (IN P1 CLOB(1M) , INOUT P2 CLOB(1M) , OUT P3 CLOB(1M) ) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, CONCAT(P2,P2), P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = CONCAT(P2,P2);"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCCL6"
          + " (IN P1 CLOB(1M) , IN P2 CLOB(1M) ,  INOUT P3 CLOB(1M) , INOUT P4 CLOB(1M) , OUT P5 CLOB(1M) , OUT P6 CLOB(1M) ) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, CONCAT(P3,P3), CONCAT(P4,P4), P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = CONCAT(P3,P3); "
          + "SET P4 = CONCAT(P4,P4);" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCDB3 (IN P1 DBCLOB(1M) CCSID 1200 , INOUT P2 DBCLOB(1M) CCSID 1200 , OUT P3 DBCLOB(1M) CCSID 1200 ) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, CONCAT(P2,P2), P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = CONCAT(P2,P2);"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCDB6"
          + " (IN P1 DBCLOB(1M) CCSID 1200 , IN P2 DBCLOB(1M) CCSID 1200 ,  INOUT P3 DBCLOB(1M) CCSID 1200 , INOUT P4 DBCLOB(1M) CCSID 1200 , OUT P5 DBCLOB(1M) CCSID 1200 , OUT P6 DBCLOB(1M) CCSID 1200 ) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, CONCAT(P3,P3), CONCAT(P4,P4), P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = CONCAT(P3,P3); "
          + "SET P4 = CONCAT(P4,P4);" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCBN3 (IN P1 BINARY(8) , INOUT P2 BINARY(8) , OUT P3 BINARY(8) ) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, CAST(CONCAT(TRIM(P2),P2) AS BINARY(8)), P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = CONCAT(TRIM(P2),P2);"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCBN6"
          + " (IN P1 BINARY(8) , IN P2 BINARY(8) ,  INOUT P3 BINARY(8) , INOUT P4 BINARY(8) , OUT P5 BINARY(8) , OUT P6 BINARY(8) ) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, CAST(CONCAT(TRIM(P3),P3)  AS BINARY(8)), CAST(CONCAT(TRIM(P4),P4) AS BINARY(8)), P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = CONCAT(TRIM(P3),P3); "
          + "SET P4 = CONCAT(TRIM(P4),P4);" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCVB3 (IN P1 VARBINARY(8) , INOUT P2 VARBINARY(8) , OUT P3 VARBINARY(8) ) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, CONCAT(P2,P2), P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = CONCAT(P2,P2);"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCVB6"
          + " (IN P1 VARBINARY(8) , IN P2 VARBINARY(8) ,  INOUT P3 VARBINARY(8) , INOUT P4 VARBINARY(8) , OUT P5 VARBINARY(8) , OUT P6 VARBINARY(8) ) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, CONCAT(P3,P3), CONCAT(P4,P4), P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = CONCAT(P3,P3); "
          + "SET P4 = CONCAT(P4,P4);" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCBL3 (IN P1 BLOB(1M) , INOUT P2 BLOB(1M) , OUT P3 BLOB(1M) ) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, CONCAT(P2,P2), P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = CONCAT(P2,P2);"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCBL6"
          + " (IN P1 BLOB(1M) , IN P2 BLOB(1M) ,  INOUT P3 BLOB(1M) , INOUT P4 BLOB(1M) , OUT P5 BLOB(1M) , OUT P6 BLOB(1M) ) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, CONCAT(P3,P3), CONCAT(P4,P4), P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = CONCAT(P3,P3); "
          + "SET P4 = CONCAT(P4,P4);" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCDT3 (IN P1 DATE, INOUT P2 DATE, OUT P3 DATE) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 + 1 MONTH, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 + 1 MONTH;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCDT6"
          + " (IN P1 DATE, IN P2 DATE,  INOUT P3 DATE, INOUT P4 DATE, OUT P5 DATE, OUT P6 DATE) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 + 1 MONTH, P4 + 1 MONTH, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 + 1 MONTH; "
          + "SET P4 = P4 + 1 MONTH;" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCTM3 (IN P1 TIME, INOUT P2 TIME, OUT P3 TIME) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 + 1 MINUTE, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 + 1 MINUTE;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCTM6"
          + " (IN P1 TIME, IN P2 TIME,  INOUT P3 TIME, INOUT P4 TIME, OUT P5 TIME, OUT P6 TIME) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 + 1 MINUTE, P4 + 1 MINUTE, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 + 1 MINUTE; "
          + "SET P4 = P4 + 1 MINUTE;" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCTS3 (IN P1 TIMESTAMP, INOUT P2 TIMESTAMP, OUT P3 TIMESTAMP) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2 + 1 MONTH, P1 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  "
          + "SET P2 = P2 + 1 MONTH;"
          + "SET P3 = P1; "
          + "SET RESULT SETS CURSOR c1;" + "END",

      "CREATE OR REPLACE PROCEDURE COLLECTION.GVCALTCTS6"
          + " (IN P1 TIMESTAMP, IN P2 TIMESTAMP,  INOUT P3 TIMESTAMP, INOUT P4 TIMESTAMP, OUT P5 TIMESTAMP, OUT P6 TIMESTAMP) "
          + "RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 CURSOR FOR SELECT P1, P2, P3 + 1 MONTH, P4 + 1 MONTH, P1, P2 from SYSIBM.SYSDUMMY1; "
          + "OPEN c1 ;  " + "SET P3 = P3 + 1 MONTH; "
          + "SET P4 = P4 + 1 MONTH;" + "SET P5 = P1; " + "SET P6 = P2; "
          + "SET RESULT SETS CURSOR c1;  " + "END",

      "CREATE OR REPLACE FUNCTION COLLECTION.GVCALTCVRMT() RETURNS VARCHAR(80) "
          + "LANGUAGE SQL BEGIN  DECLARE ANSWER VARCHAR(80);   "
          + "GET DIAGNOSTICS CONDITION 1 ANSWER=DB2_PRODUCT_ID;   "
          + "RETURN ANSWER;  END", };
  protected static String[] statementSql = { "values 1",
      "select * from sysibm.sqlcolumns fetch first 1000 rows only",
      "set COLLECTION.GVCALTCINT=7",
      "insert into COLLECTION.GVCALTCT1 VALUES(1,0)",
      "delete from COLLECTION.GVCALTCT1 WHERE C1=1",
      "select * from COLLECTION.GVCALTCT1",
      "update COLLECTION.GVCALTCT1 SET C2=C2+1 WHERE C1=2",
      "call COLLECTION.GVCALTCTM()",
      "create or replace table  COLLECTION.GVCALTCT2(C1 INT,C2 BIGINT)",
      "values COLLECTION.GVCALTCVRMT()",

  };

  protected static String[] callableStatementSql = { "call COLLECTION.GVCALTCTM()",
      "call COLLECTION.GVCALTCP1('sally')",
      "call COLLECTION.GVCALTCP1('harry')",
      "call COLLECTION.GVCALTCP1('lewis')", };

  /* List of possible transactions */
  protected static String[][] psIntTransactions = {
      /* Transaction 0 */
      { "delete from COLLECTION.GVCALTCI7",
          "INSERT INTO COLLECTION.GVCALTCI7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCI7", },
      /* Transaction 1 */
      {
          "delete from COLLECTION.GVCALTCI7",
          "INSERT INTO COLLECTION.GVCALTCI7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE COLLECTION.GVCALTCI7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCI7", },

  };

  protected static String[][][][] psIntParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1", "2", "3", "4", "5", "6", "7" },
              { "1", "2", "3", "4", "5", "6", "7" }, },
          /* T0 Test 1 */
          { {}, { "7", "6", "5", "4", "3", "2", "1" },
              { "7", "6", "5", "4", "3", "2", "1" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1", "2", "3", "4", "5", "6", "7" },
              { "7", "6", "5", "4", "3", "2", "1" },
              { "7", "6", "5", "4", "3", "2", "1" }, },
          /* T1 Test 1 */
          { {}, { "7", "6", "5", "4", "3", "2", "1" },
              { "1", "2", "3", "4", "5", "6", "7" },
              { "1", "2", "3", "4", "5", "6", "7" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psSmallintTransactions = {
      /* Transaction 0 */
      { "delete from COLLECTION.GVCALTCSI7",
          "INSERT INTO COLLECTION.GVCALTCSI7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCSI7", },
      /* Transaction 1 */
      {
          "delete from COLLECTION.GVCALTCSI7",
          "INSERT INTO COLLECTION.GVCALTCSI7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE COLLECTION.GVCALTCSI7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCSI7", },

  };

  protected static String[][][][] psSmallintParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1", "2", "3", "4", "5", "6", "7" },
              { "1", "2", "3", "4", "5", "6", "7" }, },
          /* T0 Test 1 */
          { {}, { "7", "6", "5", "4", "3", "2", "1" },
              { "7", "6", "5", "4", "3", "2", "1" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1", "2", "3", "4", "5", "6", "7" },
              { "7", "6", "5", "4", "3", "2", "1" },
              { "7", "6", "5", "4", "3", "2", "1" }, },
          /* T1 Test 1 */
          { {}, { "7", "6", "5", "4", "3", "2", "1" },
              { "1", "2", "3", "4", "5", "6", "7" },
              { "1", "2", "3", "4", "5", "6", "7" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psBigintTransactions = {
      /* Transaction 0 */
      { "delete from COLLECTION.GVCALTCBI7",
          "INSERT INTO COLLECTION.GVCALTCBI7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCBI7", },
      /* Transaction 1 */
      {
          "delete from COLLECTION.GVCALTCBI7",
          "INSERT INTO COLLECTION.GVCALTCBI7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE COLLECTION.GVCALTCBI7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCBI7", },

  };

  protected static String[][][][] psBigintParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1", "2", "3", "4", "5", "6", "7" },
              { "1", "2", "3", "4", "5", "6", "7" }, },
          /* T0 Test 1 */
          { {}, { "7", "6", "5", "4", "3", "2", "1" },
              { "7", "6", "5", "4", "3", "2", "1" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1", "2", "3", "4", "5", "6", "7" },
              { "7", "6", "5", "4", "3", "2", "1" },
              { "7", "6", "5", "4", "3", "2", "1" }, },
          /* T1 Test 1 */
          { {}, { "7", "6", "5", "4", "3", "2", "1" },
              { "1", "2", "3", "4", "5", "6", "7" },
              { "1", "2", "3", "4", "5", "6", "7" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psRealTransactions = {
      /* Transaction 0 */
      { "delete from COLLECTION.GVCALTCRL7",
          "INSERT INTO COLLECTION.GVCALTCRL7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCRL7", },
      /* Transaction 1 */
      {
          "delete from COLLECTION.GVCALTCRL7",
          "INSERT INTO COLLECTION.GVCALTCRL7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE COLLECTION.GVCALTCRL7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCRL7", },

  };

  protected static String[][][][] psRealParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" },
              { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" }, },
          /* T0 Test 1 */
          { {}, { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" },
              { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" },
              { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" },
              { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" }, },
          /* T1 Test 1 */
          { {}, { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" },
              { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" },
              { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psFloatTransactions = {
      /* Transaction 0 */
      { "delete from COLLECTION.GVCALTCFT7",
          "INSERT INTO COLLECTION.GVCALTCFT7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCFT7", },
      /* Transaction 1 */
      {
          "delete from COLLECTION.GVCALTCFT7",
          "INSERT INTO COLLECTION.GVCALTCFT7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE COLLECTION.GVCALTCFT7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCFT7", },

  };

  protected static String[][][][] psFloatParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" },
              { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" }, },
          /* T0 Test 1 */
          { {}, { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" },
              { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" },
              { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" },
              { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" }, },
          /* T1 Test 1 */
          { {}, { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" },
              { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" },
              { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psDoubleTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCDL7",
          "INSERT INTO   COLLECTION.GVCALTCDL7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCDL7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCDL7",
          "INSERT INTO   COLLECTION.GVCALTCDL7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCDL7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCDL7", },

  };

  protected static String[][][][] psDoubleParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" },
              { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" }, },
          /* T0 Test 1 */
          { {}, { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" },
              { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" },
              { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" },
              { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" }, },
          /* T1 Test 1 */
          { {}, { "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0" },
              { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" },
              { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psDecimalTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCDC7",
          "INSERT INTO   COLLECTION.GVCALTCDC7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCDC7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCDC7",
          "INSERT INTO   COLLECTION.GVCALTCDC7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCDC7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCDC7", },

  };

  protected static String[][][][] psDecimalParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              {},
              { ("1.00"), ("2.00"), ("3.00"), ("4.00"), ("5.00"), ("6.00"),
                  ("7.00") },
              { ("1.00"), ("2.00"), ("3.00"), ("4.00"), ("5.00"), ("6.00"),
                  ("7.00") }, },
          /* T0 Test 1 */
          {
              {},
              { ("7.00"), ("6.00"), ("5.00"), ("4.00"), ("3.00"), ("2.00"),
                  ("1.00") },
              { ("7.00"), ("6.00"), ("5.00"), ("4.00"), ("3.00"), ("2.00"),
                  ("1.00") }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              {},
              { ("1.00"), ("2.00"), ("3.00"), ("4.00"), ("5.00"), ("6.00"),
                  ("7.00") },
              { ("7.00"), ("6.00"), ("5.00"), ("4.00"), ("3.00"), ("2.00"),
                  ("1.00") },
              { ("7.00"), ("6.00"), ("5.00"), ("4.00"), ("3.00"), ("2.00"),
                  ("1.00") }, },
          /* T1 Test 1 */
          {
              {},
              { ("7.00"), ("6.00"), ("5.00"), ("4.00"), ("3.00"), ("2.00"),
                  ("1.00") },
              { ("1.00"), ("2.00"), ("3.00"), ("4.00"), ("5.00"), ("6.00"),
                  ("7.00") },
              { ("1.00"), ("2.00"), ("3.00"), ("4.00"), ("5.00"), ("6.00"),
                  ("7.00") }, }

      } };

  /* List of possible transactions */
  protected static String[][] psNumericTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCNM7",
          "INSERT INTO   COLLECTION.GVCALTCNM7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCNM7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCNM7",
          "INSERT INTO   COLLECTION.GVCALTCNM7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCNM7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCNM7", },

  };

  protected static String[][][][] psNumericParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              {},
              { ("1.00"), ("2.00"), ("3.00"), ("4.00"), ("5.00"), ("6.00"),
                  ("7.00") },
              { ("1.00"), ("2.00"), ("3.00"), ("4.00"), ("5.00"), ("6.00"),
                  ("7.00") }, },
          /* T0 Test 1 */
          {
              {},
              { ("7.00"), ("6.00"), ("5.00"), ("4.00"), ("3.00"), ("2.00"),
                  ("1.00") },
              { ("7.00"), ("6.00"), ("5.00"), ("4.00"), ("3.00"), ("2.00"),
                  ("1.00") }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              {},
              { ("1.00"), ("2.00"), ("3.00"), ("4.00"), ("5.00"), ("6.00"),
                  ("7.00") },
              { ("7.00"), ("6.00"), ("5.00"), ("4.00"), ("3.00"), ("2.00"),
                  ("1.00") },
              { ("7.00"), ("6.00"), ("5.00"), ("4.00"), ("3.00"), ("2.00"),
                  ("1.00") }, },
          /* T1 Test 1 */
          {
              {},
              { ("7.00"), ("6.00"), ("5.00"), ("4.00"), ("3.00"), ("2.00"),
                  ("1.00") },
              { ("1.00"), ("2.00"), ("3.00"), ("4.00"), ("5.00"), ("6.00"),
                  ("7.00") },
              { ("1.00"), ("2.00"), ("3.00"), ("4.00"), ("5.00"), ("6.00"),
                  ("7.00") }, }

      } };

  /* List of possible transactions */
  protected static String[][] psDecfloatTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCDF7",
          "INSERT INTO   COLLECTION.GVCALTCDF7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCDF7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCDF7",
          "INSERT INTO   COLLECTION.GVCALTCDF7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCDF7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCDF7", },

  };

  protected static String[][][][] psDecfloatParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              {},
              { ("1.01"), ("2.01"), ("3.01"), ("4.01"), ("5.01"), ("6.01"),
                  ("7.01") },
              { ("1.01"), ("2.01"), ("3.01"), ("4.01"), ("5.01"), ("6.01"),
                  ("7.01") }, },
          /* T0 Test 1 */
          {
              {},
              { ("7.01"), ("6.01"), ("5.01"), ("4.01"), ("3.01"), ("2.01"),
                  ("1.01") },
              { ("7.01"), ("6.01"), ("5.01"), ("4.01"), ("3.01"), ("2.01"),
                  ("1.01") }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              {},
              { ("1.01"), ("2.01"), ("3.01"), ("4.01"), ("5.01"), ("6.01"),
                  ("7.01") },
              { ("7.01"), ("6.01"), ("5.01"), ("4.01"), ("3.01"), ("2.01"),
                  ("1.01") },
              { ("7.01"), ("6.01"), ("5.01"), ("4.01"), ("3.01"), ("2.01"),
                  ("1.01") }, },
          /* T1 Test 1 */
          {
              {},
              { ("7.01"), ("6.01"), ("5.01"), ("4.01"), ("3.01"), ("2.01"),
                  ("1.01") },
              { ("1.01"), ("2.01"), ("3.01"), ("4.01"), ("5.01"), ("6.01"),
                  ("7.01") },
              { ("1.01"), ("2.01"), ("3.01"), ("4.01"), ("5.01"), ("6.01"),
                  ("7.01") }, }

      } };

  /* List of possible transactions */
  protected static String[][] psCharTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCCH7",
          "INSERT INTO   COLLECTION.GVCALTCCH7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCCH7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCCH7",
          "INSERT INTO   COLLECTION.GVCALTCCH7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCCH7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCCH7", },

  };

  protected static String[][][][] psCharParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, },
          /* T0 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, },
          /* T1 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psVarcharTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCVC7",
          "INSERT INTO   COLLECTION.GVCALTCVC7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCVC7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCVC7",
          "INSERT INTO   COLLECTION.GVCALTCVC7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCVC7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCVC7", },

  };

  protected static String[][][][] psVarcharParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, },
          /* T0 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, },
          /* T1 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psGraphicTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCGR7",
          "INSERT INTO   COLLECTION.GVCALTCGR7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCGR7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCGR7",
          "INSERT INTO   COLLECTION.GVCALTCGR7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCGR7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCGR7", },

  };

  protected static String[][][][] psGraphicParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, },
          /* T0 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, },
          /* T1 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psVargraphicTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCVG7",
          "INSERT INTO   COLLECTION.GVCALTCVG7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCVG7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCVG7",
          "INSERT INTO   COLLECTION.GVCALTCVG7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCVG7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCVG7", },

  };

  protected static String[][][][] psVargraphicParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, },
          /* T0 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, },
          /* T1 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psClobTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCCL7",
          "INSERT INTO   COLLECTION.GVCALTCCL7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCCL7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCCL7",
          "INSERT INTO   COLLECTION.GVCALTCCL7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCCL7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCCL7", },

  };

  protected static String[][][][] psClobParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, },
          /* T0 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, },
          /* T1 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psDBClobTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCDB7",
          "INSERT INTO   COLLECTION.GVCALTCDB7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCDB7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCDB7",
          "INSERT INTO   COLLECTION.GVCALTCDB7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCDB7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCDB7", },

  };

  protected static String[][][][] psDBClobParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, },
          /* T0 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { {}, { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" }, },
          /* T1 Test 1 */
          { {}, { "7.01", "6.01", "5.01", "4.01", "3.01", "2.01", "1.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" },
              { "1.01", "2.01", "3.01", "4.01", "5.01", "6.01", "7.01" }, }

      } };

  /* List of possible transactions */
  protected static String[][] psBinaryTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCBN7",
          "INSERT INTO   COLLECTION.GVCALTCBN7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCBN7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCBN7",
          "INSERT INTO   COLLECTION.GVCALTCBN7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCBN7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCBN7", },

  };

  protected static String[][][][] psBinaryParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              {},
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" },
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" }, },
          /* T0 Test 1 */
          {
              {},
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },

          }, },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              {},
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" },
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" }, },
          /* T1 Test 1 */
          {
              {},
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" },
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" }, }

      }

  };

  /* List of possible transactions */
  protected static String[][] psVarbinaryTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCVB7",
          "INSERT INTO   COLLECTION.GVCALTCVB7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCVB7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCVB7",
          "INSERT INTO   COLLECTION.GVCALTCVB7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCVB7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCVB7", },

  };

  protected static String[][][][] psVarbinaryParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              {},
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" },
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" }, },
          /* T0 Test 1 */
          {
              {},
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },

          }, },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              {},
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" },
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" }, },
          /* T1 Test 1 */
          {
              {},
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" },
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" }, }

      }

  };

  /* List of possible transactions */
  protected static String[][] psBlobTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCBL7",
          "INSERT INTO   COLLECTION.GVCALTCBL7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCBL7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCBL7",
          "INSERT INTO   COLLECTION.GVCALTCBL7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCBL7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCBL7", },

  };

  protected static String[][][][] psBlobParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              {},
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" },
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" }, },
          /* T0 Test 1 */
          {
              {},
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },

          }, },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              {},
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" },
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" }, },
          /* T1 Test 1 */
          {
              {},
              { "77665544", "66554433", "55443322", "44332211", "33221100",
                  "22110099", "11009988" },
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" },
              { "11223344", "22334455", "33445566", "44556677", "55667788",
                  "66778899", "77889900" }, }

      }

  };

  /* List of possible transactions */
  protected static String[][] psDateTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCDT7",
          "INSERT INTO   COLLECTION.GVCALTCDT7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCDT7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCDT7",
          "INSERT INTO   COLLECTION.GVCALTCDT7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCDT7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCDT7", },

  };

  protected static String[][][][] psDateParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              {},
              { "2001-01-01", "2002-02-02", "2003-03-03", "2004-04-04",
                  "2005-05-05", "2006-06-06", "2007-07-07" },
              { "2001-01-01", "2002-02-02", "2003-03-03", "2004-04-04",
                  "2005-05-05", "2006-06-06", "2007-07-07" }, },
          /* T0 Test 1 */
          {
              {},
              { "2017-07-07", "2016-06-06", "2015-05-05", "2014-04-04",
                  "2013-03-03", "2012-02-02", "2011-01-01" },
              { "2017-07-07", "2016-06-06", "2015-05-05", "2014-04-04",
                  "2013-03-03", "2012-02-02", "2011-01-01" },

          }, },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              {},
              { "2001-01-01", "2002-02-02", "2003-03-03", "2004-04-04",
                  "2005-05-05", "2006-06-06", "2007-07-07" },
              { "2017-07-07", "2016-06-06", "2015-05-05", "2014-04-04",
                  "2013-03-03", "2012-02-02", "2011-01-01" },
              { "2017-07-07", "2016-06-06", "2015-05-05", "2014-04-04",
                  "2013-03-03", "2012-02-02", "2011-01-01" }, },
          /* T1 Test 1 */
          {
              {},
              { "2017-07-07", "2016-06-06", "2015-05-05", "2014-04-04",
                  "2013-03-03", "2012-02-02", "2011-01-01" },
              { "2001-01-01", "2002-02-02", "2003-03-03", "2004-04-04",
                  "2005-05-05", "2006-06-06", "2007-07-07" },
              { "2001-01-01", "2002-02-02", "2003-03-03", "2004-04-04",
                  "2005-05-05", "2006-06-06", "2007-07-07" }, }

      }

  };

  /* List of possible transactions */
  protected static String[][] psTimeTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCTM7",
          "INSERT INTO   COLLECTION.GVCALTCTM7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCTM7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCTM7",
          "INSERT INTO   COLLECTION.GVCALTCTM7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCTM7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCTM7", },

  };

  protected static String[][][][] psTimeParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              {},
              { "01:01:01", "02:02:02", "03:03:03", "04:04:04", "05:05:05",
                  "06:06:06", "07:07:07" },
              { "01:01:01", "02:02:02", "03:03:03", "04:04:04", "05:05:05",
                  "06:06:06", "07:07:07" }, },
          /* T0 Test 1 */
          {
              {},
              { "17:07:07", "16:06:06", "15:05:05", "14:04:04", "13:03:03",
                  "12:02:02", "11:01:01" },
              { "17:07:07", "16:06:06", "15:05:05", "14:04:04", "13:03:03",
                  "12:02:02", "11:01:01" },

          }, },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              {},
              { "01:01:01", "02:02:02", "03:03:03", "04:04:04", "05:05:05",
                  "06:06:06", "07:07:07" },
              { "17:07:07", "16:06:06", "15:05:05", "14:04:04", "13:03:03",
                  "12:02:02", "11:01:01" },
              { "17:07:07", "16:06:06", "15:05:05", "14:04:04", "13:03:03",
                  "12:02:02", "11:01:01" }, },
          /* T1 Test 1 */
          {
              {},
              { "17:07:07", "16:06:06", "15:05:05", "14:04:04", "13:03:03",
                  "12:02:02", "11:01:01" },
              { "01:01:01", "02:02:02", "03:03:03", "04:04:04", "05:05:05",
                  "06:06:06", "07:07:07" },
              { "01:01:01", "02:02:02", "03:03:03", "04:04:04", "05:05:05",
                  "06:06:06", "07:07:07" }, }

      }

  };

  /* List of possible transactions */
  protected static String[][] psTimestampTransactions = {
      /* Transaction 0 */
      { "delete from   COLLECTION.GVCALTCTS7",
          "INSERT INTO   COLLECTION.GVCALTCTS7 VALUES(?,?,?,?,?,?,?)",
          "SELECT * FROM COLLECTION.GVCALTCTS7", },
      /* Transaction 1 */
      {
          "delete from   COLLECTION.GVCALTCTS7",
          "INSERT INTO   COLLECTION.GVCALTCTS7 VALUES(?,?,?,?,?,?,?)",
          "UPDATE        COLLECTION.GVCALTCTS7 SET C1=?, C2=?, C3=?,C4=?,C5=?,C6=?,C7=?",
          "SELECT * FROM COLLECTION.GVCALTCTS7", },

  };

  protected static String[][][][] psTimestampParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              {},
              { "2001-01-01 20:11:10.123456", "2002-02-02 20:11:10.123456",
                  "2003-03-03 20:11:10.123456", "2004-04-04 20:11:10.123456",
                  "2005-05-05 20:11:10.123456", "2006-06-06 20:11:10.123456",
                  "2007-07-07 20:11:10.123456", },
              { "2001-01-01 20:11:10.123456", "2002-02-02 20:11:10.123456",
                  "2003-03-03 20:11:10.123456", "2004-04-04 20:11:10.123456",
                  "2005-05-05 20:11:10.123456", "2006-06-06 20:11:10.123456",
                  "2007-07-07 20:11:10.123456", }, },
          /* T0 Test 1 */
          {
              {},
              { "2017-07-07 20:11:10.123456", "2016-06-06 20:11:10.123456",
                  "2015-05-05 20:11:10.123456", "2014-04-04 20:11:10.123456",
                  "2013-03-03 20:11:10.123456", "2012-02-02 20:11:10.123456",
                  "2011-01-01 20:11:10.123456", },
              { "2017-07-07 20:11:10.123456", "2016-06-06 20:11:10.123456",
                  "2015-05-05 20:11:10.123456", "2014-04-04 20:11:10.123456",
                  "2013-03-03 20:11:10.123456", "2012-02-02 20:11:10.123456",
                  "2011-01-01 20:11:10.123456", },

          }, },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              {},
              { "2001-01-01 20:11:10.123456", "2002-02-02 20:11:10.123456",
                  "2003-03-03 20:11:10.123456", "2004-04-04 20:11:10.123456",
                  "2005-05-05 20:11:10.123456", "2006-06-06 20:11:10.123456",
                  "2007-07-07 20:11:10.123456", },
              { "2017-07-07 20:11:10.123456", "2016-06-06 20:11:10.123456",
                  "2015-05-05 20:11:10.123456", "2014-04-04 20:11:10.123456",
                  "2013-03-03 20:11:10.123456", "2012-02-02 20:11:10.123456",
                  "2011-01-01 20:11:10.123456", },
              { "2017-07-07 20:11:10.123456", "2016-06-06 20:11:10.123456",
                  "2015-05-05 20:11:10.123456", "2014-04-04 20:11:10.123456",
                  "2013-03-03 20:11:10.123456", "2012-02-02 20:11:10.123456",
                  "2011-01-01 20:11:10.123456", }, },
          /* T1 Test 1 */
          {
              {},
              { "2017-07-07 20:11:10.123456", "2016-06-06 20:11:10.123456",
                  "2015-05-05 20:11:10.123456", "2014-04-04 20:11:10.123456",
                  "2013-03-03 20:11:10.123456", "2012-02-02 20:11:10.123456",
                  "2011-01-01 20:11:10.123456", },
              { "2001-01-01 20:11:10.123456", "2002-02-02 20:11:10.123456",
                  "2003-03-03 20:11:10.123456", "2004-04-04 20:11:10.123456",
                  "2005-05-05 20:11:10.123456", "2006-06-06 20:11:10.123456",
                  "2007-07-07 20:11:10.123456", },
              { "2001-01-01 20:11:10.123456", "2002-02-02 20:11:10.123456",
                  "2003-03-03 20:11:10.123456", "2004-04-04 20:11:10.123456",
                  "2005-05-05 20:11:10.123456", "2006-06-06 20:11:10.123456",
                  "2007-07-07 20:11:10.123456", }, }

      }

  };

  /* Callable statement test data */
  /* List of possible transactions */
  protected static String[][] csIntTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCI3(?,?,?)",
          "CALL COLLECTION.GVCALTCI6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCI3(?,?,?)",
          "CALL COLLECTION.GVCALTCI6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csIntParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1", "2", null }, { null, "4", "1" }, { "1", "4", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "4", "42", "1", "11" },
              { "1", "11", "4", "42", "1", "11" }, },
          /* T0 Test 1 */
          { { "-1", "-2", null }, { null, "-4", "-1" }, { "-1", "-4", "-1" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4", "-42", "-1", "-11" },
              { "-1", "-11", "-4", "-42", "-1", "-11" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1", "2", null }, { null, "4", "1" }, { "1", "4", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "4", "42", "1", "11" },
              { "1", "11", "4", "42", "1", "11" }, },
          /* T1 Test 1 */
          { { "-1", "-2", null }, { null, "-4", "-1" }, { "-1", "-4", "-1" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4", "-42", "-1", "-11" },
              { "-1", "-11", "-4", "-42", "-1", "-11" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csSmallintTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCSI3(?,?,?)",
          "CALL COLLECTION.GVCALTCSI6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCSI3(?,?,?)",
          "CALL COLLECTION.GVCALTCSI6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csSmallintParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1", "2", null }, { null, "4", "1" }, { "1", "4", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "4", "42", "1", "11" },
              { "1", "11", "4", "42", "1", "11" }, },
          /* T0 Test 1 */
          { { "-1", "-2", null }, { null, "-4", "-1" }, { "-1", "-4", "-1" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4", "-42", "-1", "-11" },
              { "-1", "-11", "-4", "-42", "-1", "-11" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1", "2", null }, { null, "4", "1" }, { "1", "4", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "4", "42", "1", "11" },
              { "1", "11", "4", "42", "1", "11" }, },
          /* T1 Test 1 */
          { { "-1", "-2", null }, { null, "-4", "-1" }, { "-1", "-4", "-1" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4", "-42", "-1", "-11" },
              { "-1", "-11", "-4", "-42", "-1", "-11" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csBigintTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCBI3(?,?,?)",
          "CALL COLLECTION.GVCALTCBI6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCBI3(?,?,?)",
          "CALL COLLECTION.GVCALTCBI6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csBigintParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1", "2", null }, { null, "4", "1" }, { "1", "4", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "4", "42", "1", "11" },
              { "1", "11", "4", "42", "1", "11" }, },
          /* T0 Test 1 */
          { { "-1", "-2", null }, { null, "-4", "-1" }, { "-1", "-4", "-1" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4", "-42", "-1", "-11" },
              { "-1", "-11", "-4", "-42", "-1", "-11" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1", "2", null }, { null, "4", "1" }, { "1", "4", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "4", "42", "1", "11" },
              { "1", "11", "4", "42", "1", "11" }, },
          /* T1 Test 1 */
          { { "-1", "-2", null }, { null, "-4", "-1" }, { "-1", "-4", "-1" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4", "-42", "-1", "-11" },
              { "-1", "-11", "-4", "-42", "-1", "-11" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csRealTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCRL3(?,?,?)",
          "CALL COLLECTION.GVCALTCRL6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCRL3(?,?,?)",
          "CALL COLLECTION.GVCALTCRL6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csRealParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1", "2", null }, { null, "4.0", "1.0" },
              { "1.0", "4.0", "1.0" }, { "1", "11", "2", "21", null, null },
              { null, null, "4.0", "42.0", "1.0", "11.0" },
              { "1.0", "11.0", "4.0", "42.0", "1.0", "11.0" }, },
          /* T0 Test 1 */
          { { "-1", "-2", null }, { null, "-4.0", "-1.0" },
              { "-1.0", "-4.0", "-1.0" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4.0", "-42.0", "-1.0", "-11.0" },
              { "-1.0", "-11.0", "-4.0", "-42.0", "-1.0", "-11.0" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1", "2", null }, { null, "4.0", "1.0" },
              { "1.0", "4.0", "1.0" }, { "1", "11", "2", "21", null, null },
              { null, null, "4.0", "42.0", "1.0", "11.0" },
              { "1.0", "11.0", "4.0", "42.0", "1.0", "11.0" }, },
          /* T1 Test 1 */
          { { "-1", "-2", null }, { null, "-4.0", "-1.0" },
              { "-1.0", "-4.0", "-1.0" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4.0", "-42.0", "-1.0", "-11.0" },
              { "-1.0", "-11.0", "-4.0", "-42.0", "-1.0", "-11.0" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csFloatTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCFT3(?,?,?)",
          "CALL COLLECTION.GVCALTCFT6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCFT3(?,?,?)",
          "CALL COLLECTION.GVCALTCFT6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csFloatParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1", "2", null }, { null, "4.0", "1.0" },
              { "1.0", "4.0", "1.0" }, { "1", "11", "2", "21", null, null },
              { null, null, "4.0", "42.0", "1.0", "11.0" },
              { "1.0", "11.0", "4.0", "42.0", "1.0", "11.0" }, },
          /* T0 Test 1 */
          { { "-1", "-2", null }, { null, "-4.0", "-1.0" },
              { "-1.0", "-4.0", "-1.0" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4.0", "-42.0", "-1.0", "-11.0" },
              { "-1.0", "-11.0", "-4.0", "-42.0", "-1.0", "-11.0" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1", "2", null }, { null, "4.0", "1.0" },
              { "1.0", "4.0", "1.0" }, { "1", "11", "2", "21", null, null },
              { null, null, "4.0", "42.0", "1.0", "11.0" },
              { "1.0", "11.0", "4.0", "42.0", "1.0", "11.0" }, },
          /* T1 Test 1 */
          { { "-1", "-2", null }, { null, "-4.0", "-1.0" },
              { "-1.0", "-4.0", "-1.0" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4.0", "-42.0", "-1.0", "-11.0" },
              { "-1.0", "-11.0", "-4.0", "-42.0", "-1.0", "-11.0" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csDoubleTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCDL3(?,?,?)",
          "CALL COLLECTION.GVCALTCDL6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCDL3(?,?,?)",
          "CALL COLLECTION.GVCALTCDL6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csDoubleParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1", "2", null }, { null, "4.0", "1.0" },
              { "1.0", "4.0", "1.0" }, { "1", "11", "2", "21", null, null },
              { null, null, "4.0", "42.0", "1.0", "11.0" },
              { "1.0", "11.0", "4.0", "42.0", "1.0", "11.0" }, },
          /* T0 Test 1 */
          { { "-1", "-2", null }, { null, "-4.0", "-1.0" },
              { "-1.0", "-4.0", "-1.0" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4.0", "-42.0", "-1.0", "-11.0" },
              { "-1.0", "-11.0", "-4.0", "-42.0", "-1.0", "-11.0" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1", "2", null }, { null, "4.0", "1.0" },
              { "1.0", "4.0", "1.0" }, { "1", "11", "2", "21", null, null },
              { null, null, "4.0", "42.0", "1.0", "11.0" },
              { "1.0", "11.0", "4.0", "42.0", "1.0", "11.0" }, },
          /* T1 Test 1 */
          { { "-1", "-2", null }, { null, "-4.0", "-1.0" },
              { "-1.0", "-4.0", "-1.0" },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-4.0", "-42.0", "-1.0", "-11.0" },
              { "-1.0", "-11.0", "-4.0", "-42.0", "-1.0", "-11.0" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csDecimalTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCDC3(?,?,?)",
          "CALL COLLECTION.GVCALTCDC6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCDC3(?,?,?)",
          "CALL COLLECTION.GVCALTCDC6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csDecimalParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1.00", "2.00", null }, { null, "4.00", "1.00" },
              { "1.00", "4.00", "1.00" },
              { "1.00", "11.00", "2.00", "21.00", null, null },
              { null, null, "4.00", "42.00", "1.00", "11.00" },
              { "1.00", "11.00", "4.00", "42.00", "1.00", "11.00" }, },
          /* T0 Test 1 */
          { { "-1.00", "-2.00", null }, { null, "-4.00", "-1.00" },
              { "-1.00", "-4.00", "-1.00" },
              { "-1.00", "-11.00", "-2.00", "-21.00", null, null },
              { null, null, "-4.00", "-42.00", "-1.00", "-11.00" },
              { "-1.00", "-11.00", "-4.00", "-42.00", "-1.00", "-11.00" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1.00", "2.00", null }, { null, "4.00", "1.00" },
              { "1.00", "4.00", "1.00" },
              { "1.00", "11.00", "2.00", "21.00", null, null },
              { null, null, "4.00", "42.00", "1.00", "11.00" },
              { "1.00", "11.00", "4.00", "42.00", "1.00", "11.00" }, },
          /* T1 Test 1 */
          { { "-1.00", "-2.00", null }, { null, "-4.00", "-1.00" },
              { "-1.00", "-4.00", "-1.00" },
              { "-1.00", "-11.00", "-2.00", "-21.00", null, null },
              { null, null, "-4.00", "-42.00", "-1.00", "-11.00" },
              { "-1.00", "-11.00", "-4.00", "-42.00", "-1.00", "-11.00" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csNumericTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCNM3(?,?,?)",
          "CALL COLLECTION.GVCALTCNM6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCNM3(?,?,?)",
          "CALL COLLECTION.GVCALTCNM6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csNumericParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1.00", "2.00", null }, { null, "4.00", "1.00" },
              { "1.00", "4.00", "1.00" },
              { "1.00", "11.00", "2.00", "21.00", null, null },
              { null, null, "4.00", "42.00", "1.00", "11.00" },
              { "1.00", "11.00", "4.00", "42.00", "1.00", "11.00" }, },
          /* T0 Test 1 */
          { { "-1.00", "-2.00", null }, { null, "-4.00", "-1.00" },
              { "-1.00", "-4.00", "-1.00" },
              { "-1.00", "-11.00", "-2.00", "-21.00", null, null },
              { null, null, "-4.00", "-42.00", "-1.00", "-11.00" },
              { "-1.00", "-11.00", "-4.00", "-42.00", "-1.00", "-11.00" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1.00", "2.00", null }, { null, "4.00", "1.00" },
              { "1.00", "4.00", "1.00" },
              { "1.00", "11.00", "2.00", "21.00", null, null },
              { null, null, "4.00", "42.00", "1.00", "11.00" },
              { "1.00", "11.00", "4.00", "42.00", "1.00", "11.00" }, },
          /* T1 Test 1 */
          { { "-1.00", "-2.00", null }, { null, "-4.00", "-1.00" },
              { "-1.00", "-4.00", "-1.00" },
              { "-1.00", "-11.00", "-2.00", "-21.00", null, null },
              { null, null, "-4.00", "-42.00", "-1.00", "-11.00" },
              { "-1.00", "-11.00", "-4.00", "-42.00", "-1.00", "-11.00" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csDecfloatTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCDF3(?,?,?)",
          "CALL COLLECTION.GVCALTCDF6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCDF3(?,?,?)",
          "CALL COLLECTION.GVCALTCDF6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csDecfloatParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1.00", "2.00", null }, { null, "4.00", "1.00" },
              { "1.00", "4.00", "1.00" },
              { "1.00", "11.00", "2.00", "21.00", null, null },
              { null, null, "4.00", "42.00", "1.00", "11.00" },
              { "1.00", "11.00", "4.00", "42.00", "1.00", "11.00" }, },
          /* T0 Test 1 */
          { { "-1.00", "-2.00", null }, { null, "-4.00", "-1.00" },
              { "-1.00", "-4.00", "-1.00" },
              { "-1.00", "-11.00", "-2.00", "-21.00", null, null },
              { null, null, "-4.00", "-42.00", "-1.00", "-11.00" },
              { "-1.00", "-11.00", "-4.00", "-42.00", "-1.00", "-11.00" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1.00", "2.00", null }, { null, "4.00", "1.00" },
              { "1.00", "4.00", "1.00" },
              { "1.00", "11.00", "2.00", "21.00", null, null },
              { null, null, "4.00", "42.00", "1.00", "11.00" },
              { "1.00", "11.00", "4.00", "42.00", "1.00", "11.00" }, },
          /* T1 Test 1 */
          { { "-1.00", "-2.00", null }, { null, "-4.00", "-1.00" },
              { "-1.00", "-4.00", "-1.00" },
              { "-1.00", "-11.00", "-2.00", "-21.00", null, null },
              { null, null, "-4.00", "-42.00", "-1.00", "-11.00" },
              { "-1.00", "-11.00", "-4.00", "-42.00", "-1.00", "-11.00" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csCharTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCCH3(?,?,?)",
          "CALL COLLECTION.GVCALTCCH6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCCH3(?,?,?)",
          "CALL COLLECTION.GVCALTCCH6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csCharParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              { "1", "2", null },
              { null, "22      ", "1       " },
              { "1       ", "22      ", "1       " },
              { "1", "11", "2", "21", null, null },
              { null, null, "22      ", "2121    ", "1       ", "11      " },
              { "1       ", "11      ", "22      ", "2121    ", "1       ",
                  "11      " }, },
          /* T0 Test 1 */
          {
              { "-1", "ab", null },
              { null, "abab    ", "-1      " },
              { "-1      ", "abab    ", "-1      " },
              { "-1", "-11", "az", "za", null, null },
              { null, null, "azaz    ", "zaza    ", "-1      ", "-11     " },
              { "-1      ", "-11     ", "azaz    ", "zaza    ", "-1      ",
                  "-11     " }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              { "1", "2", null },
              { null, "22      ", "1       " },
              { "1       ", "22      ", "1       " },
              { "1", "11", "2", "21", null, null },
              { null, null, "22      ", "2121    ", "1       ", "11      " },
              { "1       ", "11      ", "22      ", "2121    ", "1       ",
                  "11      " }, },
          /* T1 Test 1 */
          {
              { "-1", "-2", null },
              { null, "-2-2    ", "-1      " },
              { "-1      ", "-2-2    ", "-1      " },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-2-2    ", "-21-21  ", "-1      ", "-11     " },
              { "-1      ", "-11     ", "-2-2    ", "-21-21  ", "-1      ",
                  "-11     " }, }

      } };

  /* List of possible transactions */
  protected static String[][] csVarcharTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCVC3(?,?,?)",
          "CALL COLLECTION.GVCALTCVC6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCVC3(?,?,?)",
          "CALL COLLECTION.GVCALTCVC6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csVarcharParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1", "2", null }, { null, "22", "1" }, { "1", "22", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "22", "2121", "1", "11" },
              { "1", "11", "22", "2121", "1", "11" }, },
          /* T0 Test 1 */
          { { "-1", "ab", null }, { null, "abab", "-1" },
              { "-1", "abab", "-1" }, { "-1", "-11", "az", "za", null, null },
              { null, null, "azaz", "zaza", "-1", "-11" },
              { "-1", "-11", "azaz", "zaza", "-1", "-11" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1", "2", null }, { null, "22", "1" }, { "1", "22", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "22", "2121", "1", "11" },
              { "1", "11", "22", "2121", "1", "11" }, },
          /* T1 Test 1 */
          { { "-1", "-2", null }, { null, "-2-2", "-1" },
              { "-1", "-2-2", "-1" }, { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-2-2", "-21-21", "-1", "-11" },
              { "-1", "-11", "-2-2", "-21-21", "-1", "-11" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csGraphicTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCGR3(?,?,?)",
          "CALL COLLECTION.GVCALTCGR6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCGR3(?,?,?)",
          "CALL COLLECTION.GVCALTCGR6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csGraphicParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              { "1", "2", null },
              { null, "22      ", "1       " },
              { "1       ", "22      ", "1       " },
              { "1", "11", "2", "21", null, null },
              { null, null, "22      ", "2121    ", "1       ", "11      " },
              { "1       ", "11      ", "22      ", "2121    ", "1       ",
                  "11      " }, },
          /* T0 Test 1 */
          {
              { "-1", "ab", null },
              { null, "abab    ", "-1      " },
              { "-1      ", "abab    ", "-1      " },
              { "-1", "-11", "az", "za", null, null },
              { null, null, "azaz    ", "zaza    ", "-1      ", "-11     " },
              { "-1      ", "-11     ", "azaz    ", "zaza    ", "-1      ",
                  "-11     " }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              { "1", "2", null },
              { null, "22      ", "1       " },
              { "1       ", "22      ", "1       " },
              { "1", "11", "2", "21", null, null },
              { null, null, "22      ", "2121    ", "1       ", "11      " },
              { "1       ", "11      ", "22      ", "2121    ", "1       ",
                  "11      " }, },
          /* T1 Test 1 */
          {
              { "-1", "-2", null },
              { null, "-2-2    ", "-1      " },
              { "-1      ", "-2-2    ", "-1      " },
              { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-2-2    ", "-21-21  ", "-1      ", "-11     " },
              { "-1      ", "-11     ", "-2-2    ", "-21-21  ", "-1      ",
                  "-11     " }, }

      } };

  /* List of possible transactions */
  protected static String[][] csVargraphicTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCVG3(?,?,?)",
          "CALL COLLECTION.GVCALTCVG6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCVG3(?,?,?)",
          "CALL COLLECTION.GVCALTCVG6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csVargraphicParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1", "2", null }, { null, "22", "1" }, { "1", "22", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "22", "2121", "1", "11" },
              { "1", "11", "22", "2121", "1", "11" }, },
          /* T0 Test 1 */
          { { "-1", "ab", null }, { null, "abab", "-1" },
              { "-1", "abab", "-1" }, { "-1", "-11", "az", "za", null, null },
              { null, null, "azaz", "zaza", "-1", "-11" },
              { "-1", "-11", "azaz", "zaza", "-1", "-11" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1", "2", null }, { null, "22", "1" }, { "1", "22", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "22", "2121", "1", "11" },
              { "1", "11", "22", "2121", "1", "11" }, },
          /* T1 Test 1 */
          { { "-1", "-2", null }, { null, "-2-2", "-1" },
              { "-1", "-2-2", "-1" }, { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-2-2", "-21-21", "-1", "-11" },
              { "-1", "-11", "-2-2", "-21-21", "-1", "-11" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csClobTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCCL3(?,?,?)",
          "CALL COLLECTION.GVCALTCCL6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCCL3(?,?,?)",
          "CALL COLLECTION.GVCALTCCL6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csClobParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1", "2", null }, { null, "22", "1" }, { "1", "22", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "22", "2121", "1", "11" },
              { "1", "11", "22", "2121", "1", "11" }, },
          /* T0 Test 1 */
          { { "-1", "ab", null }, { null, "abab", "-1" },
              { "-1", "abab", "-1" }, { "-1", "-11", "az", "za", null, null },
              { null, null, "azaz", "zaza", "-1", "-11" },
              { "-1", "-11", "azaz", "zaza", "-1", "-11" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1", "2", null }, { null, "22", "1" }, { "1", "22", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "22", "2121", "1", "11" },
              { "1", "11", "22", "2121", "1", "11" }, },
          /* T1 Test 1 */
          { { "-1", "-2", null }, { null, "-2-2", "-1" },
              { "-1", "-2-2", "-1" }, { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-2-2", "-21-21", "-1", "-11" },
              { "-1", "-11", "-2-2", "-21-21", "-1", "-11" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csDBClobTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCDB3(?,?,?)",
          "CALL COLLECTION.GVCALTCDB6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCDB3(?,?,?)",
          "CALL COLLECTION.GVCALTCDB6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csDBClobParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "1", "2", null }, { null, "22", "1" }, { "1", "22", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "22", "2121", "1", "11" },
              { "1", "11", "22", "2121", "1", "11" }, },
          /* T0 Test 1 */
          { { "-1", "ab", null }, { null, "abab", "-1" },
              { "-1", "abab", "-1" }, { "-1", "-11", "az", "za", null, null },
              { null, null, "azaz", "zaza", "-1", "-11" },
              { "-1", "-11", "azaz", "zaza", "-1", "-11" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "1", "2", null }, { null, "22", "1" }, { "1", "22", "1" },
              { "1", "11", "2", "21", null, null },
              { null, null, "22", "2121", "1", "11" },
              { "1", "11", "22", "2121", "1", "11" }, },
          /* T1 Test 1 */
          { { "-1", "-2", null }, { null, "-2-2", "-1" },
              { "-1", "-2-2", "-1" }, { "-1", "-11", "-2", "-21", null, null },
              { null, null, "-2-2", "-21-21", "-1", "-11" },
              { "-1", "-11", "-2-2", "-21-21", "-1", "-11" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csBinaryTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCBN3(?,?,?)",
          "CALL COLLECTION.GVCALTCBN6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCBN3(?,?,?)",
          "CALL COLLECTION.GVCALTCBN6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csBinaryParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              { "11", "22", null },
              { null, "2222000000000000", "1100000000000000" },
              { "1100000000000000", "2222000000000000", "1100000000000000" },
              { "11", "11", "22", "21", null, null },
              { null, null, "2222000000000000", "2121000000000000",
                  "1100000000000000", "1100000000000000" },
              { "1100000000000000", "1100000000000000", "2222000000000000",
                  "2121000000000000", "1100000000000000", "1100000000000000" }, },
          /* T0 Test 1 */
          {
              { "0011", "ab", null },
              { null, "ABAB000000000000", "0011000000000000" },
              { "0011000000000000", "ABAB000000000000", "0011000000000000" },
              { "0011000000000000", "0011000000000000", "ab00000000000000",
                  "ba00000000000000", null, null },
              { null, null, "ABAB000000000000", "BABA000000000000",
                  "0011000000000000", "0011000000000000" },
              { "0011000000000000", "0011000000000000", "ABAB000000000000",
                  "BABA000000000000", "0011000000000000", "0011000000000000" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              { "11", "22", null },
              { null, "2222000000000000", "1100000000000000" },
              { "1100000000000000", "2222000000000000", "1100000000000000" },
              { "11", "11", "22", "21", null, null },
              { null, null, "2222000000000000", "2121000000000000",
                  "1100000000000000", "1100000000000000" },
              { "1100000000000000", "1100000000000000", "2222000000000000",
                  "2121000000000000", "1100000000000000", "1100000000000000" }, },
          /* T1 Test 1 */
          {
              { "0011", "ab", null },
              { null, "ABAB000000000000", "0011000000000000" },
              { "0011000000000000", "ABAB000000000000", "0011000000000000" },
              { "0011", "0011", "ab", "ba", null, null },
              { null, null, "ABAB000000000000", "BABA000000000000",
                  "0011000000000000", "0011000000000000" },
              { "0011000000000000", "0011000000000000", "ABAB000000000000",
                  "BABA000000000000", "0011000000000000", "0011000000000000" }, }

      } };

  /* List of possible transactions */
  protected static String[][] csVarbinaryTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCVB3(?,?,?)",
          "CALL COLLECTION.GVCALTCVB6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCVB3(?,?,?)",
          "CALL COLLECTION.GVCALTCVB6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csVarbinaryParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "11", "22", null }, { null, "2222", "11" },
              { "11", "2222", "11" }, { "11", "11", "22", "21", null, null },
              { null, null, "2222", "2121", "11", "11" },
              { "11", "11", "2222", "2121", "11", "11" }, },
          /* T0 Test 1 */
          { { "0011", "ab", null }, { null, "ABAB", "0011" },
              { "0011", "ABAB", "0011" },
              { "0011", "0011", "ab", "ba", null, null },
              { null, null, "ABAB", "BABA", "0011", "0011" },
              { "0011", "0011", "ABAB", "BABA", "0011", "0011" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "11", "22", null }, { null, "2222", "11" },
              { "11", "2222", "11" }, { "11", "11", "22", "21", null, null },
              { null, null, "2222", "2121", "11", "11" },
              { "11", "11", "2222", "2121", "11", "11" }, },
          /* T1 Test 1 */
          { { "0011", "ab", null }, { null, "ABAB", "0011" },
              { "0011", "ABAB", "0011" },
              { "0011", "0011", "ab", "ba", null, null },
              { null, null, "ABAB", "BABA", "0011", "0011" },
              { "0011", "0011", "ABAB", "BABA", "0011", "0011" }, }

      }

  };

  /* List of possible transactions */
  protected static String[][] csBlobTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCBL3(?,?,?)",
          "CALL COLLECTION.GVCALTCBL6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCBL3(?,?,?)",
          "CALL COLLECTION.GVCALTCBL6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csBlobParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          { { "11", "22", null }, { null, "2222", "11" },
              { "11", "2222", "11" }, { "11", "11", "22", "21", null, null },
              { null, null, "2222", "2121", "11", "11" },
              { "11", "11", "2222", "2121", "11", "11" }, },
          /* T0 Test 1 */
          { { "0011", "ab", null }, { null, "ABAB", "0011" },
              { "0011", "ABAB", "0011" },
              { "0011", "0011", "ab", "ba", null, null },
              { null, null, "ABAB", "BABA", "0011", "0011" },
              { "0011", "0011", "ABAB", "BABA", "0011", "0011" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          { { "11", "22", null }, { null, "2222", "11" },
              { "11", "2222", "11" }, { "11", "11", "22", "21", null, null },
              { null, null, "2222", "2121", "11", "11" },
              { "11", "11", "2222", "2121", "11", "11" }, },
          /* T1 Test 1 */
          { { "0011", "ab", null }, { null, "ABAB", "0011" },
              { "0011", "ABAB", "0011" },
              { "0011", "0011", "ab", "ba", null, null },
              { null, null, "ABAB", "BABA", "0011", "0011" },
              { "0011", "0011", "ABAB", "BABA", "0011", "0011" }, }

      }

  };

  /* List of possible transactions */
  protected static String[][] csDateTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCDT3(?,?,?)",
          "CALL COLLECTION.GVCALTCDT6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCDT3(?,?,?)",
          "CALL COLLECTION.GVCALTCDT6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csDateParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              { "2011-11-11", "2022-02-22", null },
              { null, "2022-03-22", "2011-11-11" },
              { "2011-11-11", "2022-03-22", "2011-11-11" },
              { "2011-11-11", "2011-11-11", "2022-02-22", "2022-02-21", null,
                  null },
              { null, null, "2022-03-22", "2022-03-21", "2011-11-11",
                  "2011-11-11" },
              { "2011-11-11", "2011-11-11", "2022-03-22", "2022-03-21",
                  "2011-11-11", "2011-11-11" }, },
          /* T0 Test 1 */
          {
              { "2014-11-11", "2022-02-22", null },
              { null, "2022-03-22", "2014-11-11" },
              { "2014-11-11", "2022-03-22", "2014-11-11" },
              { "2011-11-11", "2011-11-11", "2022-02-22", "2022-02-21", null,
                  null },
              { null, null, "2022-03-22", "2022-03-21", "2011-11-11",
                  "2011-11-11" },
              { "2011-11-11", "2011-11-11", "2022-03-22", "2022-03-21",
                  "2011-11-11", "2011-11-11" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              { "2011-11-11", "2022-02-22", null },
              { null, "2022-03-22", "2011-11-11" },
              { "2011-11-11", "2022-03-22", "2011-11-11" },
              { "2011-11-11", "2011-11-11", "2022-02-22", "2022-02-21", null,
                  null },
              { null, null, "2022-03-22", "2022-03-21", "2011-11-11",
                  "2011-11-11" },
              { "2011-11-11", "2011-11-11", "2022-03-22", "2022-03-21",
                  "2011-11-11", "2011-11-11" }, },
          /* T1 Test 1 */
          {
              { "2014-11-11", "2022-02-22", null },
              { null, "2022-03-22", "2014-11-11" },
              { "2014-11-11", "2022-03-22", "2014-11-11" },
              { "2011-11-11", "2011-11-11", "2022-02-22", "2022-02-21", null,
                  null },
              { null, null, "2022-03-22", "2022-03-21", "2011-11-11",
                  "2011-11-11" },
              { "2011-11-11", "2011-11-11", "2022-03-22", "2022-03-21",
                  "2011-11-11", "2011-11-11" }, }

      }

  };

  /* List of possible transactions */
  protected static String[][] csTimeTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCTM3(?,?,?)",
          "CALL COLLECTION.GVCALTCTM6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCTM3(?,?,?)",
          "CALL COLLECTION.GVCALTCTM6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csTimeParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              { "11:11:11", "12:34:56", null },
              { null, "12:35:56", "11:11:11" },
              { "11:11:11", "12:35:56", "11:11:11" },
              { "11:11:11", "11:11:11", "13:02:22", "14:02:21", null, null },
              { null, null, "13:03:22", "14:03:21", "11:11:11", "11:11:11" },
              { "11:11:11", "11:11:11", "13:03:22", "14:03:21", "11:11:11",
                  "11:11:11" }, },
          /* T0 Test 1 */
          {
              { "14:11:11", "01:02:22", null },
              { null, "01:03:22", "14:11:11" },
              { "14:11:11", "01:03:22", "14:11:11" },
              { "11:11:11", "11:11:11", "22:02:22", "22:02:21", null, null },
              { null, null, "22:03:22", "22:03:21", "11:11:11", "11:11:11" },
              { "11:11:11", "11:11:11", "22:03:22", "22:03:21", "11:11:11",
                  "11:11:11" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              { "11:11:11", "22:02:22", null },
              { null, "22:03:22", "11:11:11" },
              { "11:11:11", "22:03:22", "11:11:11" },
              { "11:11:11", "11:11:11", "22:02:22", "22:02:21", null, null },
              { null, null, "22:03:22", "22:03:21", "11:11:11", "11:11:11" },
              { "11:11:11", "11:11:11", "22:03:22", "22:03:21", "11:11:11",
                  "11:11:11" }, },
          /* T1 Test 1 */
          {
              { "14:11:11", "22:02:22", null },
              { null, "22:03:22", "14:11:11" },
              { "14:11:11", "22:03:22", "14:11:11" },
              { "11:11:11", "11:11:11", "22:02:22", "22:02:21", null, null },
              { null, null, "22:03:22", "22:03:21", "11:11:11", "11:11:11" },
              { "11:11:11", "11:11:11", "22:03:22", "22:03:21", "11:11:11",
                  "11:11:11" }, }

      }

  };

  /* List of possible transactions */
  protected static String[][] csTimestampTransactions = {
      /* Transaction 0 */
      { "CALL COLLECTION.GVCALTCTS3(?,?,?)",
          "CALL COLLECTION.GVCALTCTS6(?,?,?,?,?,?)", },
      /* Transaction 1 */
      { "CALL COLLECTION.GVCALTCTS3(?,?,?)",
          "CALL COLLECTION.GVCALTCTS6(?,?,?,?,?,?)", },

  };

  protected static String[][][][] csTimestampParms = {
      /* Transaction 0 */
      {
          /* T0 Test 0 */
          {
              { "2011-11-11 11:22:33.012345", "2022-02-22 11:22:33.012345",
                  null },
              { null, "2022-03-22 11:22:33.012345",
                  "2011-11-11 11:22:33.012345" },
              { "2011-11-11 11:22:33.012345", "2022-03-22 11:22:33.012345",
                  "2011-11-11 11:22:33.012345" },
              { "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2022-02-22 11:22:33.012345", "2022-02-21 11:22:33.012345",
                  null, null },
              { null, null, "2022-03-22 11:22:33.012345",
                  "2022-03-21 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2011-11-11 11:22:33.012345" },
              { "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2022-03-22 11:22:33.012345", "2022-03-21 11:22:33.012345",
                  "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345" }, },
          /* T0 Test 1 */
          {
              { "2014-11-11 11:22:33.012345", "2022-02-22 11:22:33.012345",
                  null },
              { null, "2022-03-22 11:22:33.012345",
                  "2014-11-11 11:22:33.012345" },
              { "2014-11-11 11:22:33.012345", "2022-03-22 11:22:33.012345",
                  "2014-11-11 11:22:33.012345" },
              { "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2022-02-22 11:22:33.012345", "2022-02-21 11:22:33.012345",
                  null, null },
              { null, null, "2022-03-22 11:22:33.012345",
                  "2022-03-21 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2011-11-11 11:22:33.012345" },
              { "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2022-03-22 11:22:33.012345", "2022-03-21 11:22:33.012345",
                  "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345" }, }

      },
      /* Transaction 1 */
      {
          /* T1 Test 0 */
          {
              { "2011-11-11 11:22:33.012345", "2022-02-22 11:22:33.012345",
                  null },
              { null, "2022-03-22 11:22:33.012345",
                  "2011-11-11 11:22:33.012345" },
              { "2011-11-11 11:22:33.012345", "2022-03-22 11:22:33.012345",
                  "2011-11-11 11:22:33.012345" },
              { "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2022-02-22 11:22:33.012345", "2022-02-21 11:22:33.012345",
                  null, null },
              { null, null, "2022-03-22 11:22:33.012345",
                  "2022-03-21 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2011-11-11 11:22:33.012345" },
              { "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2022-03-22 11:22:33.012345", "2022-03-21 11:22:33.012345",
                  "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345" }, },
          /* T1 Test 1 */
          {
              { "2014-11-11 11:22:33.012345", "2022-02-22 11:22:33.012345",
                  null },
              { null, "2022-03-22 11:22:33.012345",
                  "2014-11-11 11:22:33.012345" },
              { "2014-11-11 11:22:33.012345", "2022-03-22 11:22:33.012345",
                  "2014-11-11 11:22:33.012345" },
              { "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2022-02-22 11:22:33.012345", "2022-02-21 11:22:33.012345",
                  null, null },
              { null, null, "2022-03-22 11:22:33.012345",
                  "2022-03-21 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2011-11-11 11:22:33.012345" },
              { "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345",
                  "2022-03-22 11:22:33.012345", "2022-03-21 11:22:33.012345",
                  "2011-11-11 11:22:33.012345", "2011-11-11 11:22:33.012345" }, }

      }

  };

  protected static String[] cleanupSql = { "drop variable COLLECTION.GVCALTCINT",
      "drop table  COLLECTION.GVCALTCT1", "drop table  COLLECTION.GVCALTCT2",
      "drop table  COLLECTION.GVCALTCI7", "drop table  COLLECTION.GVCALTCSI7",
      "drop table  COLLECTION.GVCALTCBI7", "drop table  COLLECTION.GVCALTCRL7",
      "drop table  COLLECTION.GVCALTCFT7", "drop table  COLLECTION.GVCALTCDL7",
      "drop table  COLLECTION.GVCALTCDC7", "drop table  COLLECTION.GVCALTCNM7",
      "drop table  COLLECTION.GVCALTCDF7", "drop table  COLLECTION.GVCALTCCH7",
      "drop table  COLLECTION.GVCALTCVC7", "drop table  COLLECTION.GVCALTCGR7",
      "drop table  COLLECTION.GVCALTCVG7", "drop table  COLLECTION.GVCALTCCL7",
      "drop table  COLLECTION.GVCALTCDB7", "drop table  COLLECTION.GVCALTCBN7",
      "drop table  COLLECTION.GVCALTCVB7", "drop table  COLLECTION.GVCALTCBL7",
      "drop table  COLLECTION.GVCALTCDT7", "drop table  COLLECTION.GVCALTCTM7",
      "drop table  COLLECTION.GVCALTCTS7",

      "drop procedure COLLECTION.GVCALTCTM",
      "drop procedure COLLECTION.GVCALTCP1",
      "drop PROCEDURE COLLECTION.GVCALTCI3",
      "drop PROCEDURE COLLECTION.GVCALTCSI3",
      "drop PROCEDURE COLLECTION.GVCALTCBI3",

      "drop procedure  COLLECTION.GVCALTCRL3",
      "drop procedure  COLLECTION.GVCALTCFT3",
      "drop procedure  COLLECTION.GVCALTCDL3",
      "drop procedure  COLLECTION.GVCALTCDC3",
      "drop procedure  COLLECTION.GVCALTCNM3",
      "drop procedure  COLLECTION.GVCALTCDF3",
      "drop procedure  COLLECTION.GVCALTCCH3",
      "drop procedure  COLLECTION.GVCALTCVC3",
      "drop procedure  COLLECTION.GVCALTCGR3",
      "drop procedure  COLLECTION.GVCALTCVG3",
      "drop procedure  COLLECTION.GVCALTCCL3",
      "drop procedure  COLLECTION.GVCALTCDB3",
      "drop procedure  COLLECTION.GVCALTCBN3",
      "drop procedure  COLLECTION.GVCALTCVB3",
      "drop procedure  COLLECTION.GVCALTCBL3",
      "drop procedure  COLLECTION.GVCALTCDT3",
      "drop procedure  COLLECTION.GVCALTCTM3",
      "drop procedure  COLLECTION.GVCALTCTS3",

      "drop PROCEDURE COLLECTION.GVCALTCI6",
      "drop PROCEDURE COLLECTION.GVCALTCSI6",
      "drop PROCEDURE COLLECTION.GVCALTCBI6",
      "drop procedure  COLLECTION.GVCALTCRL6",
      "drop procedure  COLLECTION.GVCALTCFT6",
      "drop procedure  COLLECTION.GVCALTCDL6",
      "drop procedure  COLLECTION.GVCALTCDC6",
      "drop procedure  COLLECTION.GVCALTCNM6",
      "drop procedure  COLLECTION.GVCALTCDF6",
      "drop procedure  COLLECTION.GVCALTCCH6",
      "drop procedure  COLLECTION.GVCALTCVC6",
      "drop procedure  COLLECTION.GVCALTCGR6",
      "drop procedure  COLLECTION.GVCALTCVG6",
      "drop procedure  COLLECTION.GVCALTCCL6",
      "drop procedure  COLLECTION.GVCALTCDB6",
      "drop procedure  COLLECTION.GVCALTCBN6",
      "drop procedure  COLLECTION.GVCALTCVB6",
      "drop procedure  COLLECTION.GVCALTCBL6",
      "drop procedure  COLLECTION.GVCALTCDT6",
      "drop procedure  COLLECTION.GVCALTCTM6",
      "drop procedure  COLLECTION.GVCALTCTS6",

      "drop function  COLLECTION.GVCALTCVRMT", };

  /**
   * Constructor. This is called from the AS400JDBCEnableCALTest constructor.
   **/
  public JDASTestcase(AS400 systemObject, String testname,
      Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password, String pwrSysUserID,
      String pwrSysPassword) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
 password);
    pwrSysUserID_ = pwrSysUserID;
    pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrSysPassword); 
  }

  protected void fixupSql(String[] sqlArray) {
    int len = sqlArray.length;
    for (int i = 0; i < len; i++) {
      sqlArray[i] = fixupSql(sqlArray[i]);
    }

  }

  protected String fixupSql(String sql) {
    sql = sql.replace("COLLECTION", JDASTest.COLLECTION);
    return sql;
  }

  protected static final int JAVA_STRING = 1;
  protected static final int JAVA_BIGDECIMAL = 2;
  protected static final int JAVA_SHORT = 3;
  protected static final int JAVA_INT = 4;
  protected static final int JAVA_LONG = 5;
  protected static final int JAVA_FLOAT = 6;
  protected static final int JAVA_DOUBLE = 7;
  protected static final int JAVA_BYTEARRAY = 8;
  protected static final int JAVA_DATE = 9;
  protected static final int JAVA_TIME = 10;
  protected static final int JAVA_TIMESTAMP = 11;

  /* Run prepared statement tests with different types as parameters */
  public void testPSTypeParameters(String[][] psTypeTransactions,
      String[][][][] psTypeParms, int javaType, String url,
      KillThread killThread, int runseconds) {
    StringBuffer sb = new StringBuffer();
    testPSTypeParameters(psTypeTransactions, psTypeParms, javaType, url,
        killThread, runseconds, sb);
  }

  public void testPSTypeParameters(String[][] psTypeTransactions,
      String[][][][] psTypeParms, int javaType, String url,
      KillThread killThread, int runSeconds, StringBuffer sb) {
    try {
      if (sb == null) sb = new StringBuffer(); 
      Connection killerConnection = testDriver_.getConnection(url_,
          systemObject_.getUserId(), encryptedPassword_);
      infoAppend(sb,"Connecting to " + url + "\n");
      Connection connection = testDriver_.getConnection(url,
          systemObject_.getUserId(), encryptedPassword_);
      
      testPSTypeParameters(psTypeTransactions, psTypeParms, javaType,
          connection, killerConnection, killThread, runSeconds, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception\n" + sb.toString());
    }

  }

  
  public void testDSPSTypeParameters(String[][] psTypeTransactions,
      String[][][][] psTypeParms, int javaType, String url,
      KillThread killThread, int runSeconds, StringBuffer sb) {
    try {
      if (sb == null) sb = new StringBuffer(); 
      Connection killerConnection = testDriver_.getConnection(url_,
          systemObject_.getUserId(), encryptedPassword_); 
      infoAppend(sb,"Connecting to " + url + "using datasource \n");
      
      Connection connection = createDSConnectionFromURL(url); 

      testPSTypeParameters(psTypeTransactions, psTypeParms, javaType,
          connection, killerConnection, killThread, runSeconds, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception\n" + sb.toString());
    }

  }

  public Connection createDSConnectionFromURL(String url) throws Exception {
   
   Hashtable h = getPropertiesFromUrl(url);
   String system = (String) h.get("system"); 
   
   AS400JDBCDataSource ds = new AS400JDBCDataSource(system, systemObject_.getUserId(), 
       PasswordVault.decryptPassword(encryptedPassword_));
   
   Enumeration e = h.keys(); 
   while (e.hasMoreElements()) { 
     String property = (String) e.nextElement(); 
     String value = (String) h.get(property); 
     if (property.equals("port")) {
       ds.setPortNumber(Integer.parseInt(value));
     } else if (property.equals("system")) {
       // alread processed 
     } else if (property.equals("user")) {
       ds.setUser(value);
     } else if (property.equals("password")) { 
       ds.setPassword(value);; 
     } else if (property.equals("enableClientAffinitiesList")) {
       ds.setEnableClientAffinitiesList(Integer.parseInt(value));
     } else if (property.equals("transaction isolation")) { 
       ds.setTransactionIsolation(value);; 
     } else {
       throw new Exception("Unable to processing URL property : "+property+"="+value); 
     }
     if (url.indexOf("enableSeamlessFailover=1") > 0) {
       seamless_ = true; 
     } else {
       seamless_ = false; 
     }
   }
   
    
    
   return ds.getConnection(); 
  }

  /* Create a hashtable of properties from the URL */ 
  /* This must return a "system" property */ 
  private Hashtable getPropertiesFromUrl(String url) throws Exception {
    Hashtable properties = new Hashtable(); 
    int startIndex = url.indexOf("jdbc:as400:") ; 
    
    if (startIndex < 0) { 
      throw new Exception("Unable to find jdbc:as400: in URL:"+url);
    }
    startIndex = startIndex + 11; 
    int portIndex = 0; 
    int endIndex = url.indexOf(':',startIndex); 
    if (endIndex < 0) {
      endIndex = url.indexOf(';', startIndex);
    } else {
      portIndex = endIndex + 1;
    }
    while (url.charAt(startIndex) == '/') {
      startIndex++; 
    }
    if (endIndex < 0) {
      properties.put("system", url.substring(startIndex));
    } else {
      
      properties.put("system", url.substring(startIndex, endIndex));

      if (portIndex > 0) {
        startIndex = portIndex;
        endIndex = url.indexOf(';', startIndex);
        if (endIndex < 0) {
          properties.put("port", url.substring(startIndex));
        } else {
          properties.put("port", url.substring(startIndex, endIndex));
        }
      }
      if (endIndex > 0) {
        startIndex = endIndex+1; 
        endIndex = url.indexOf(';',startIndex);
        while (endIndex > 0) { 
          addURLProperty(properties, url, startIndex, endIndex); 
          startIndex = endIndex+1; 
          endIndex = url.indexOf(';',startIndex);
        }
        addURLProperty(properties, url, startIndex, endIndex); 
      }

    }
    return properties;
  }

  private void addURLProperty(Hashtable properties, String url, int startIndex,
      int endIndex) throws Exception {
    if (endIndex < 0) {
      endIndex = url.length(); 
    }
    int equalsIndex = url.indexOf('=',startIndex); 
    if (startIndex > endIndex) { 
      throw new Exception("For '"+url+"' no = between "+startIndex+" and "+endIndex); 
    }
    String property = url.substring(startIndex,equalsIndex); 
    String value = url.substring(equalsIndex+1, endIndex); 
    properties.put(property, value); 
    
  }

  public void testPSTypeParameters(String[][] psTypeTransactions,
      String[][][][] psTypeParms, int javaType, Connection connection, Connection killerConnection,
      KillThread killThread, int runSeconds, StringBuffer sb) {

    Random random = new Random(1);
    killCount = 0;
    int killTime =  MINIMUM_TRANSACTION_MILLISECONDS; 
    int minimumTransactionMilliseconds = MINIMUM_TRANSACTION_MILLISECONDS;
    boolean passed = true;
    infoAppend(sb, "testPSTypeParmeters");; 
    infoAppend(sb,"Test a connection with enableClientAffinitiesList\n");
    infoAppend(sb,"Make sure the connection is re-established after it drops\n");
    infoAppend(sb,"It will be dropped randomly\n");
    infoAppend(sb,"This test uses prepared statements with type parameter markers\n");
    infoAppend(sb,"killThread is "+killThread+"\n");
    try {

      if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
        PreparedStatement[][] psTransactions = new PreparedStatement[psTypeTransactions.length][];
        for      (int i = 0; i < psTypeTransactions.length; i++) {
          String[] sqlInTransaction = psTypeTransactions[i];
          PreparedStatement[] psInTransaction = new PreparedStatement[sqlInTransaction.length];
          psTransactions[i] = psInTransaction;
          for (int j = 0; j < sqlInTransaction.length; j++) {
            psInTransaction[j] = connection
                .prepareStatement(fixupSql(sqlInTransaction[j]));
            ;
          }

        }
        // Start a thread to kill the connection
        String serverJobName = getServerJobName(connection);
        if (killThread == null) {
          // Default behavior is to use kill thread that kills the jobs
          killTime =  minimumTransactionMilliseconds + random.nextInt(500);
          killThread = new KillThread(killerConnection, serverJobName,
             killTime, sb);
        } else {
          killTime = killThread.sleepMilliseconds_; 
        }
        long endMillis = System.currentTimeMillis() + runSeconds * 1000;
        killThread.start();
        killThread.waitUntilReady(); 
        int transactionCount = 0;
        int transactionsAttempted = 0;
        int serverLocalPort = 0; 
        while (System.currentTimeMillis() < endMillis && passed
            && (transactionCount < RUN_TRANSACTIONS)) {
          boolean retry = true;
          int t = random.nextInt(psTransactions.length);
          while (retry && (System.currentTimeMillis() < endMillis && passed)) {
            retry = false;
            try {

              synchronized (sb) {
                infoAppend(sb,"TC=" + transactionCount + " T=" + t + "\n");
                Statement s = connection.createStatement(); 
                ResultSet rs = s.executeQuery("select REMOTE_PORT from qsys2.netstat_job_info a where a.JOB_NAME=qsys2.JOB_NAME and LOCAL_PORT=8471"); 
                if (rs.next()) {
                  int currentServerLocalPort = rs.getInt(1);
                  if (currentServerLocalPort != serverLocalPort) {
                      serverLocalPort = currentServerLocalPort;
                  infoAppend(sb, "Server connection is "+serverLocalPort+"/8471\n");
                  }
                }
              }
              PreparedStatement[] psInT = psTransactions[t];
              String[][][] parameterSets = psTypeParms[t];
              int parameterSetIndex = random.nextInt(parameterSets.length);
              String[][] parameterSet = parameterSets[parameterSetIndex];
              transactionsAttempted++;
              for (int j = 0; j < psInT.length; j++) {
                infoAppend(sb," T=" + t + " S=" + j + "\n");
                int parameterCount = psInT[j].getParameterMetaData()
                    .getParameterCount();
                if (parameterCount > 0) {
                  for (int k = 0; k < parameterCount; k++) {
                    switch (javaType) {
                    case JAVA_STRING:
                      psInT[j].setString((k + 1), parameterSet[j][k]);
                      break;
                    case JAVA_BIGDECIMAL:
                      psInT[j].setBigDecimal((k + 1), new BigDecimal(
                          parameterSet[j][k]));
                      break;
                    case JAVA_FLOAT:
                      psInT[j].setFloat((k + 1),
                          Float.parseFloat(parameterSet[j][k]));
                      break;
                    case JAVA_DOUBLE:
                      psInT[j].setDouble((k + 1),
                          Double.parseDouble(parameterSet[j][k]));
                      break;
                    case JAVA_LONG:
                      psInT[j].setLong((k + 1),
                          Long.parseLong(parameterSet[j][k]));
                      break;
                    case JAVA_INT:
                      psInT[j].setInt((k + 1),
                          Integer.parseInt(parameterSet[j][k]));
                      break;
                    case JAVA_SHORT:
                      psInT[j].setShort((k + 1),
                          Short.parseShort(parameterSet[j][k]));
                      break;
                    case JAVA_BYTEARRAY:
                      byte[] stuff = hexAsStringToBytes(parameterSet[j][k]);
                      psInT[j].setBytes(k + 1, stuff);
                      break;
                    case JAVA_DATE:
                      Date dateStuff = Date.valueOf(parameterSet[j][k]);
                      psInT[j].setDate(k + 1, dateStuff);
                      break;
                    case JAVA_TIME:
                      Time timeStuff = Time.valueOf(parameterSet[j][k]);
                      psInT[j].setTime(k + 1, timeStuff);
                      break;
                    case JAVA_TIMESTAMP:
                      Timestamp timestampStuff = Timestamp
                          .valueOf(parameterSet[j][k]);
                      psInT[j].setTimestamp(k + 1, timestampStuff);
                      break;

                    default:
                      throw new Exception("Unknown type" + javaType);
                    }
                  }
                }
                boolean resultSetAvailable = psInT[j].execute();

                if (resultSetAvailable) {
                  ResultSet rs = psInT[j].getResultSet();
                  if (rs.next()) {
                    String[] outputValues = parameterSet[j];
                    for (int k = 0; k < outputValues.length; k++) {
                      String result;
                      switch (javaType) {
                      case JAVA_STRING:
                        result = rs.getString((k + 1));
                        break;
                      case JAVA_BIGDECIMAL:
                        result = rs.getBigDecimal(k + 1).toString();
                        break;
                      case JAVA_FLOAT:
                        result = "" + rs.getFloat(k + 1);
                        break;
                      case JAVA_DOUBLE:
                        result = "" + rs.getDouble(k + 1);
                        break;
                      case JAVA_LONG:
                        result = "" + rs.getLong(k + 1);
                        break;
                      case JAVA_INT:
                        result = "" + rs.getInt(k + 1);
                        break;
                      case JAVA_SHORT:
                        result = "" + rs.getShort(k + 1);
                        break;
                      case JAVA_BYTEARRAY:
                        byte[] stuff = rs.getBytes(k + 1);
                        result = bytesToString(stuff);
                        break;
                      case JAVA_DATE:
                        result = rs.getDate(k + 1).toString();
                        break;
                      case JAVA_TIME:
                        result = rs.getTime(k + 1).toString();
                        break;
                      case JAVA_TIMESTAMP:
                        result = rs.getTimestamp(k + 1).toString();
                        break;
                      default:
                        throw new Exception("Unknown type" + javaType);
                      }

                      if (!result.equals(outputValues[k])) {
                        passed = false;
                        infoAppend(sb," For column " + (k + 1) + " got " + result
                            + " sb " + outputValues[k] + "\n");
                      }
                    }
                  } else {
                    infoAppend(sb,"Failed:  empty result set\n");
                    passed = false;
                  }
                  rs.close();
                } /* no result set available */

              } /* looping through transaction elements */
              connection.commit();
              transactionCount++;
              minimumTransactionMilliseconds = MINIMUM_TRANSACTION_MILLISECONDS;
            } catch (SQLException e) {
              // System.out.println("-----------------");
              // e.printStackTrace(System.out);
              int sqlcode = e.getErrorCode();
              killCount++;
              if (sqlcode != -4498) {
                synchronized (sb) {
                  infoAppend(sb,"Bad exception received\n");
                  printStackTraceToStringBuffer(e, sb);
                }
                throw e;
              } else {
                synchronized (sb) {
                  infoAppend(sb,"Good exception received\n");
                  printStackTraceToStringBuffer(e, sb);
                }
                // restart the transaction
                retry = true;
                // Increase the minimum transaction on failure if we haven't
                // completed any transactions
                if (transactionCount == 0) {

                  minimumTransactionMilliseconds += killTime;
                  infoAppend(sb,"Minimum transaction milliseconds increased to "
                      + minimumTransactionMilliseconds + "\n");
                }
                // Start a thread to kill the connection
                serverJobName = getServerJobName(connection);
                killThread.setName("Killer-" + killCount);
                killTime = minimumTransactionMilliseconds + random.nextInt(500); 
                killThread.reset(
                    killTime,
                    serverJobName);

              }
            } /* catch */

          } /* while retry */

        } /* while running */
        killThread.shutdown();
        killThread.join();
        infoAppend(sb,"Final killCount =" + killCount + "\n");
        // System.out.println("Testcase done: killCount="+killCount);
        if (transactionCount == 0) {
          infoAppend(sb,"Transactions attempted = " + transactionsAttempted + "\n");
          infoAppend(sb,"Error -- no transactions completed\n");
          passed = false;
        } else {
          System.out.println("Transactions attempted = "
              + transactionsAttempted + " transactions completed="
              + transactionCount);
        }
        connection.close();
        killerConnection.close();
        if (killCount == 0 && !seamless_ ) {
          sb.append("FAILED because kill count is zero\n");
          passed = false; 
        }
        assertCondition(passed, sb);

      }

    } catch (Exception e) {
      failed(e, "Unexpected Exception\n" + sb.toString());
    }
  }


  public void testDSCSTypeParameters(String[][] csTypeTransactions,
      String[][][][] csTypeParms, int javaType, String url,
      KillThread killThread, int runSeconds, StringBuffer sb) {
    
    Connection connection;
    Connection killerConnection;
    try {
      if (sb == null) { 
        sb = new StringBuffer(); 
      }
     killerConnection = testDriver_.getConnection(url_,
        systemObject_.getUserId(), encryptedPassword_);
    infoAppend(sb,"Connecting to " + url + "\n");
     connection = createDSConnectionFromURL(url);
    } catch (Exception e) {
      failed(e, "Unexpected Exception\n" + sb.toString());
      return; 
    }
    testCSTypeParameters(csTypeTransactions, csTypeParms, javaType, connection, killerConnection, killThread, runSeconds, sb);
  }

  
  public void testCSTypeParameters(String[][] csTypeTransactions,
      String[][][][] csTypeParms, int javaType, String url,
      KillThread killThread, int runSeconds, StringBuffer sb) {
    
    Connection connection;
    Connection killerConnection;
    try {
      if (sb == null) { 
        sb = new StringBuffer(); 
      }
     killerConnection = testDriver_.getConnection(url_,
        systemObject_.getUserId(), encryptedPassword_);
    infoAppend(sb,"Connecting to " + url + "\n");
     connection = testDriver_.getConnection(url,
        systemObject_.getUserId(), encryptedPassword_);
    } catch (Exception e) {
      failed(e, "Unexpected Exception\n" + sb.toString());
      return; 
    }
    testCSTypeParameters(csTypeTransactions, csTypeParms, javaType, connection, killerConnection, killThread, runSeconds, sb);
  }
  
  public static boolean printInfo = false; 
  public static boolean propertyChecked = false; 
  public static void infoAppend(StringBuffer sb, String info) { 
    synchronized(sb) {
    if (!propertyChecked) { 
      String prop = System.getProperty("com.ibm.as400.access.Trace.category"); 
      if (prop != null ) {
        printInfo = true; 
      }
      propertyChecked=true; 
    }
    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis()); 
    info = currentTimestamp.toString()+":"+info; 
    sb.append(info); 
    if (printInfo) {
      System.out.print(info);
      System.out.flush(); 
    }
    }
  }
  
  public void testCSTypeParameters(String[][] csTypeTransactions,
      String[][][][] csTypeParms, int javaType, Connection connection, Connection killerConnection,
      KillThread killThread, int runSeconds, StringBuffer sb) {
    Random random = new Random();
    killCount = 0;
    int killTime = 0; 
    boolean passed = true;
    infoAppend(sb, "testCSTypeParameters\n"); 
    infoAppend(sb,"Test a connection with enableClientAffinitiesList\n");
    infoAppend(sb,"Make sure the connection is re-established after it drops\n");
    infoAppend(sb,"It will be dropped randomly\n");
    infoAppend(sb,"This test uses prepared statements with type parameter markers\n");
    int minimumTransactionMilliseconds = MINIMUM_TRANSACTION_MILLISECONDS;

    try {

      if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
        CallableStatement[][] csTransactions = new CallableStatement[csTypeTransactions.length][];
        for (int i = 0; i < csTypeTransactions.length; i++) {
          String[] sqlInTransaction = csTypeTransactions[i];
          CallableStatement[] csInTransaction = new CallableStatement[sqlInTransaction.length];
          csTransactions[i] = csInTransaction;
          for (int j = 0; j < sqlInTransaction.length; j++) {
            csInTransaction[j] = connection
                .prepareCall(fixupSql(sqlInTransaction[j]));
            ;
          }

        }
        // Start a thread to kill the connection
        String serverJobName = getServerJobName(connection);
        if (killThread == null) {
          killTime =  minimumTransactionMilliseconds + random.nextInt(500); 
          killThread = new KillThread(killerConnection, serverJobName,
             killTime, sb);
        } else {
          killTime = killThread.sleepMilliseconds_; 
        }
        long endMillis = System.currentTimeMillis() + runSeconds * 1000;
        killThread.start();
        int transactionCount = 0;
        int transactionsAttempted = 0;
        while (System.currentTimeMillis() < endMillis && passed
            && (transactionCount < RUN_TRANSACTIONS)) {
          boolean retry = true;
          int t = random.nextInt(csTransactions.length);
          while (retry && (System.currentTimeMillis() < endMillis) && passed) {
            retry = false;
            try {

              synchronized (sb) {
                infoAppend(sb,"TC=" + transactionCount + " T=" + t + "\n");
              }
              CallableStatement[] csInT = csTransactions[t];
              String[][][] parameterSets = csTypeParms[t];
              int parameterSetIndex = random.nextInt(parameterSets.length);
              String[][] parameterSet = parameterSets[parameterSetIndex];
              transactionsAttempted++;
              for (int j = 0; j < csInT.length; j++) {
                infoAppend(sb," T=" + t + " S=" + j + "\n");
                ParameterMetaData pmd = csInT[j].getParameterMetaData();
                int parameterCount = pmd.getParameterCount();
                if (parameterCount > 0) {
                  for (int k = 0; k < parameterCount; k++) {
                    String parameterValue = parameterSet[3 * j][k];
                    int parameterMode = pmd.getParameterMode(k + 1);
                    if ((parameterMode == ParameterMetaData.parameterModeInOut)
                        || (parameterMode == ParameterMetaData.parameterModeOut)) {

                      // Todo: parameter is output value
                      switch (javaType) {
                      case JAVA_STRING:
                        csInT[j].registerOutParameter((k + 1),
                            Types.LONGVARCHAR);
                        break;
                      case JAVA_BIGDECIMAL:
                        csInT[j].registerOutParameter((k + 1), Types.DECIMAL);
                        break;
                      case JAVA_FLOAT:
                        csInT[j].registerOutParameter((k + 1), Types.FLOAT);
                        break;
                      case JAVA_DOUBLE:
                        csInT[j].registerOutParameter((k + 1), Types.DOUBLE);
                        break;
                      case JAVA_LONG:
                        csInT[j].registerOutParameter((k + 1), Types.BIGINT);
                        break;
                      case JAVA_INT:
                        csInT[j].registerOutParameter((k + 1), Types.INTEGER);
                        break;
                      case JAVA_SHORT:
                        csInT[j].registerOutParameter((k + 1), Types.SMALLINT);
                        break;
                      case JAVA_BYTEARRAY:
                        csInT[j].registerOutParameter((k + 1), Types.BINARY);
                        break;
                      case JAVA_DATE:
                        csInT[j].registerOutParameter((k + 1), Types.DATE);
                        break;
                      case JAVA_TIME:
                        csInT[j].registerOutParameter((k + 1), Types.TIME);
                        break;
                      case JAVA_TIMESTAMP:
                        csInT[j].registerOutParameter((k + 1), Types.TIMESTAMP);
                        break;

                      default:
                        throw new Exception("Unknown type" + javaType);
                      }

                    }
                    /* If the value is not null then there is a parameter to set */
                    if (parameterValue != null) {
                      switch (javaType) {
                      case JAVA_STRING:
                        infoAppend(sb,"  setString("+(k+1)+")="+parameterValue+"\n"); 
                        csInT[j].setString((k + 1), parameterValue);
                        break;
                      case JAVA_BIGDECIMAL:
                        infoAppend(sb,"  setBigDecimal("+(k+1)+")="+parameterValue+"\n"); 
                        csInT[j].setBigDecimal((k + 1), new BigDecimal(
                            parameterValue));
                        break;
                      case JAVA_FLOAT:
                        infoAppend(sb,"  setFloat("+(k+1)+")="+parameterValue+"\n"); 
                        csInT[j].setFloat((k + 1),
                            Float.parseFloat(parameterValue));
                        break;
                      case JAVA_DOUBLE:
                        infoAppend(sb,"  setDouble("+(k+1)+")="+parameterValue+"\n"); 
                        csInT[j].setDouble((k + 1),
                            Double.parseDouble(parameterValue));
                        break;
                      case JAVA_LONG:
                        infoAppend(sb,"  setLong("+(k+1)+")="+parameterValue+"\n"); 
                        csInT[j].setLong((k + 1),
                            Long.parseLong(parameterValue));
                        break;
                      case JAVA_INT:
                        infoAppend(sb,"  setInt("+(k+1)+")="+parameterValue+"\n"); 
                        csInT[j].setInt((k + 1),
                            Integer.parseInt(parameterValue));
                        break;
                      case JAVA_SHORT:
                        infoAppend(sb,"  setShort("+(k+1)+")="+parameterValue+"\n"); 
                        csInT[j].setShort((k + 1),
                            Short.parseShort(parameterValue));
                        break;
                      case JAVA_BYTEARRAY:
                        infoAppend(sb,"  setByteArray("+(k+1)+")="+parameterValue+"\n"); 
                        byte[] stuff = hexAsStringToBytes(parameterValue);
                        csInT[j].setBytes(k + 1, stuff);
                        break;
                      case JAVA_DATE:
                        infoAppend(sb,"  setDate("+(k+1)+")="+parameterValue+"\n"); 
                        Date dateStuff = Date.valueOf(parameterValue);
                        csInT[j].setDate(k + 1, dateStuff);
                        break;
                      case JAVA_TIME:
                        infoAppend(sb,"  setTime("+(k+1)+")="+parameterValue+"\n"); 
                        Time timeStuff = Time.valueOf(parameterValue);
                        csInT[j].setTime(k + 1, timeStuff);
                        break;
                      case JAVA_TIMESTAMP:
                        infoAppend(sb,"  setTimestamp("+(k+1)+")="+parameterValue+"\n"); 
                        Timestamp timestampStuff = Timestamp
                            .valueOf(parameterValue);
                        csInT[j].setTimestamp(k + 1, timestampStuff);
                        break;

                      default:
                        throw new Exception("Unknown type" + javaType);
                      }
                    }
                  }
                }
                infoAppend(sb,"Executing:" + fixupSql(csTypeTransactions[t][j]) + "\n");
                boolean resultSetAvailable = csInT[j].execute();

                // Process output parameters
                if (parameterCount > 0) {
                  for (int k = 0; k < parameterCount; k++) {
                    String parameterValue = parameterSet[3 * j + 1][k];
                    if (parameterValue == null) {
                      // Parameter is input value

                    } else {
                      // Parameter is output -- read the value and compare
                      String outputParameter = null;

                      switch (javaType) {
                      case JAVA_STRING:
                        outputParameter = csInT[j].getString((k + 1));
                        break;
                      case JAVA_BIGDECIMAL:
                        outputParameter = csInT[j].getBigDecimal((k + 1))
                            .toString();
                        break;
                      case JAVA_FLOAT:
                        outputParameter = "" + csInT[j].getFloat((k + 1));
                        break;
                      case JAVA_DOUBLE:
                        outputParameter = "" + csInT[j].getDouble((k + 1));
                        break;
                      case JAVA_LONG:
                        outputParameter = "" + csInT[j].getLong((k + 1));
                        break;
                      case JAVA_INT:
                        outputParameter = "" + csInT[j].getInt((k + 1));
                        break;
                      case JAVA_SHORT:
                        outputParameter = "" + csInT[j].getShort((k + 1));
                        break;
                      case JAVA_BYTEARRAY:
                        byte[] stuff = csInT[j].getBytes(k + 1);
                        outputParameter = bytesToString(stuff);
                        break;
                      case JAVA_DATE:
                        outputParameter = csInT[j].getDate(k + 1).toString();
                        break;
                      case JAVA_TIME:
                        outputParameter = csInT[j].getTime(k + 1).toString();
                        break;
                      case JAVA_TIMESTAMP:
                        outputParameter = csInT[j].getTimestamp(k + 1)
                            .toString();
                        break;

                      default:
                        throw new Exception("Unknown type" + javaType);
                      }
                      if (!parameterValue.equals(outputParameter)) {
                        passed = false;
                        infoAppend(sb," For parameter " + (k + 1) + " got '"
                            + outputParameter + "'\n"
                            + "                          sb '" + parameterValue
                            + "'\n");
                      }

                    }
                  }
                }

                if (resultSetAvailable) {
                  ResultSet rs = csInT[j].getResultSet();
                  if (rs.next()) {
                    String[] outputValues = parameterSet[3 * j + 2];
                    for (int k = 0; k < outputValues.length; k++) {
                      String result;
                      switch (javaType) {
                      case JAVA_STRING:
                        result = rs.getString((k + 1));
                        break;
                      case JAVA_BIGDECIMAL:
                        result = rs.getBigDecimal(k + 1).toString();
                        break;
                      case JAVA_FLOAT:
                        result = "" + rs.getFloat(k + 1);
                        break;
                      case JAVA_DOUBLE:
                        result = "" + rs.getDouble(k + 1);
                        break;
                      case JAVA_LONG:
                        result = "" + rs.getLong(k + 1);
                        break;
                      case JAVA_INT:
                        result = "" + rs.getInt(k + 1);
                        break;
                      case JAVA_SHORT:
                        result = "" + rs.getShort(k + 1);
                        break;
                      case JAVA_BYTEARRAY:
                        byte[] stuff = rs.getBytes(k + 1);
                        result = bytesToString(stuff);
                        break;
                      case JAVA_DATE:
                        result = rs.getDate(k + 1).toString();
                        break;
                      case JAVA_TIME:
                        result = rs.getTime(k + 1).toString();
                        break;
                      case JAVA_TIMESTAMP:
                        result = rs.getTimestamp(k + 1).toString();
                        break;
                      default:
                        throw new Exception("Unknown type" + javaType);
                      }

                      if (!result.equals(outputValues[k])) {
                        passed = false;
                        infoAppend(sb," For column " + (k + 1) + " got '" + result
                            + "'\n" + "                       sb '"
                            + outputValues[k] + "'\n");
                      }
                    }
                  } else {
                    infoAppend(sb,"Failed:  empty result set\n");
                    passed = false;
                  }
                  rs.close();
                } /* no result set available */

              } /* looping through transaction elements */
              connection.commit();
              transactionCount++;
              minimumTransactionMilliseconds = MINIMUM_TRANSACTION_MILLISECONDS;
            } catch (SQLException e) {
              // System.out.println("-----------------");
              // e.printStackTrace(System.out);
              int sqlcode = e.getErrorCode();
              killCount++;
              if (sqlcode != -4498) {
                synchronized (sb) {
                  infoAppend(sb,"Bad exception received\n");
                  infoAppend(sb, "sqlcode = "+sqlcode);
                  infoAppend(sb, "sqlstate = "+e.getSQLState()); 
                  printStackTraceToStringBuffer(e, sb);
                }
                throw e;
              } else {
                synchronized (sb) {
                  infoAppend(sb,"Good exception received\n");
                  printStackTraceToStringBuffer(e, sb);
                }
                // restart the transaction
                retry = true;
                // Increase the minimum transaction on failure if we haven't
                // completed any transactions
                if (transactionCount == 0) {
                  minimumTransactionMilliseconds += killTime;
                }

                // Start a thread to kill the connection
                serverJobName = getServerJobName(connection);
                killTime = minimumTransactionMilliseconds + random.nextInt(500); 
                killThread.reset(
                    killTime,
                    serverJobName);
                killThread.setName("Killer-" + killCount);

              }
            } /* catch */

          } /* while retry */

        } /* while running */
        killThread.shutdown();
        killThread.join();
        infoAppend(sb,"Final killCount =" + killCount + "\n");
        // System.out.println("Testcase done: killCount="+killCount);
        if (transactionCount == 0) {
          infoAppend(sb,"Transactions attempted = " + transactionsAttempted + "\n");
          infoAppend(sb,"Error -- no transactions completed\n");
          passed = false;
        } else {
          System.out.println("Transactions attempted = "
              + transactionsAttempted + " transactions completed="
              + transactionCount);
          if (debug) {
        	  System.out.println(sb.toString()); 
          }
        }
        /* Close the connection */
        connection.close();
        killerConnection.close();

        assertCondition(passed, sb);

      }

    } catch (Exception e) {
      failed(e, "Unexpected Exception\n" + sb.toString());
    }
  }

  private String getServerJobName(Connection connection) throws Exception {
    String rawJob = JDReflectionUtil.callMethod_S(connection,
        "getServerJobIdentifier");
    return rawJob.substring(20, 26) + "/" + rawJob.substring(10, 20).trim()
        + "/" + rawJob.substring(0, 10).trim();

  }

  public class SwitchKillThread extends KillThread {
    SocketProxy[] proxies_;
    Random random_;

    public SwitchKillThread(Connection c, String killjob,
        int sleepMilliseconds, StringBuffer sb, SocketProxy[] proxies) {
      super(c, killjob, sleepMilliseconds, sb);
      proxies_ = proxies;
      random_ = new Random();
    }

    public void run() {
      // See if only one proxy is in use.  If so, then use that one.
      int pick = -1; 
      for (int i = 0; i < proxies_.length && pick != -2; i++) {
        int count =  proxies_[i].getConnectionCount(); 
        if (count > 0) {
          if (pick == -1) {
            pick = i;        /* Chose this if none selected */ 
          } else {
            pick = -2;      /* -2 indicates we found multiple */ 
          }
        }
      }
      // Randomly pick one if none or many found
      if (pick < 0) { 
        pick = random_.nextInt(proxies_.length);
        sb_.append("SwitchKillThread: Chose random proxy\n"); 
      } else {
        sb_.append("SwitchKillThread: Using in-use  proxy\n"); 
      }
      
      synchronized (sb_) {
        sb_.append("SwitchKillThread: Enabling proxy at index " + pick  + " port= " +proxies_[pick].getPortNumber()+"/"+proxies_[pick].getServerLocalPort()+" c="+proxies_[pick].getConnectionCount()+ " \n");
      }
      proxies_[pick].enable(true);

      // Make sure the rest are disabled
      for (int i = 0; i < proxies_.length; i++) {
        if (i != pick) {
          proxies_[i].enable(false);
          sb_.append("SwitchKillThread: disabling proxy at index " + i  + " port= " +proxies_[i].getPortNumber()+"/"+proxies_[i].getServerLocalPort()+" c="+proxies_[i].getConnectionCount()+ " \n");
        }
      }
      ready_ = true; 
      while (running_) {
        synchronized (sb_) {
          
          sb_.append("SwitchKillThread: sleeping for " + sleepMilliseconds_
              + "\n");
        }
        try {
          Thread.sleep(sleepMilliseconds_);
        } catch (InterruptedException e) {
        }
        synchronized (sb_) {
          sb_.append("SwitchKillThread: Ending proxy \n");
        }

        int newPick = random_.nextInt(proxies_.length);
        // Make sure we do not pick the same one.
        while (newPick == pick) {
          newPick = random_.nextInt(proxies_.length);
        }
        synchronized (sb_) {
          sb_.append("SwitchKillthread: Enabling proxy at index " + newPick + " port= " +proxies_[newPick].getPortNumber()+"/"+proxies_[newPick].getServerLocalPort()+" c="+proxies_[newPick].getConnectionCount()+  " \n");
        }
        proxies_[newPick].enable(true);
        // Allow time for the proxy to become active
        try {
          Thread.sleep(1);
        } catch (Exception e) {

        }
        synchronized (sb_) {
          sb_.append("SwitchKillthread: Ending proxy at index " + pick  + " port= " +proxies_[pick].getPortNumber()+"/"+proxies_[pick].getServerLocalPort()+" c="+proxies_[pick].getConnectionCount()+ "\n");
        }

        proxies_[pick].enable(false);

        proxies_[pick].endActiveConnections();

        pick = newPick;
        waiting_ = true;

        // Now wait for reset before continuing
        // sometimes the reset doesn't work. In that case, we keep going. 
        synchronized (this) {
          long timeoutTime = System.currentTimeMillis() + 10000; /* ten seconds should be long enough */ 
          while (waiting_ && running_ & System.currentTimeMillis() < timeoutTime) {
            try {
              this.wait(250);
            } catch (Exception e) {

            }
          }
          if (waiting_ == true && running_ == true) {   
             sb_.append("SwitchKillThread:  continuing because reset not found within 10 seconds\n");
             waiting_ = false; 
          } else {
            sb_.append("SwitchKillThread:  loop bottom\n"); 
          }
        }

      }
      synchronized (sb_) {
        sb_.append("SwitchKillThread done\n");
      }

    }

  }

  class KillThread extends Thread {
    
    private Connection c_;
    String killjob_;
    int sleepMilliseconds_;
    StringBuffer sb_;
    boolean running_ = true;
    boolean waiting_ = false;
    boolean ready_ = false; 
    
    public KillThread(Connection c, String killjob, int sleepMilliseconds,
        StringBuffer sb) {
      c_ = c;
      killjob_ = killjob;
      sleepMilliseconds_ = sleepMilliseconds;
      sb_ = sb;
    }
    public void waitUntilReady() throws InterruptedException {
      infoAppend(sb_,  "waiting for KillThread to be ready\n");
      boolean ready = false; 
      synchronized(this) {
        ready = ready_; 
      }
      while (!ready) {
          sleep(100);
        synchronized(this) {
          ready = ready_; 
        }
      }
      
      infoAppend(sb_,  "waiting for KillThread complete\n");
      
    }
    public String toString() { 
      return(this.getClass().toString()+" sleepMilliseconds_ = "+sleepMilliseconds_+" killjob_ = "+killjob_+"\n"); 
    }
    public synchronized void reset(int sleepMilliseconds, String killjob) {
      killjob_ = killjob;
      sleepMilliseconds_ = sleepMilliseconds;
      waiting_ = false;
      notifyAll();
    }

    public synchronized void shutdown() {
      waiting_ = false;
      running_ = false;
      notifyAll();
    }

    public void run() {
      while (running_) {
        infoAppend(sb_, "KillThread: sleeping for " + sleepMilliseconds_ + "\n");
        ready_ = true; 
        try {
          Thread.sleep(sleepMilliseconds_);
        } catch (InterruptedException e) {
        }
        String sql = "call qsys2.qcmdexc('endjob job(" + killjob_
            + ") option(*immed)')";
        infoAppend(sb_,"KillThread: Killing job  using " + sql + "\n");
        try {
          Statement s = c_.createStatement();
          s.executeUpdate(sql);
          s.close();
        } catch (SQLException e) {
          synchronized (sb_) {
            infoAppend(sb_, "KillThread: hit exception\n");
            printStackTraceToStringBuffer(e, sb_);
          }
        }
        waiting_ = true;

        // Now wait for reset before continuing
        synchronized (this) {
          while (waiting_ && running_) {
            try {
              this.wait(250);
            } catch (Exception e) {

            }
          }
        }

      }
      synchronized (sb_) {
        infoAppend(sb_, "KillThread: done\n");
      }

    }
  }

}
