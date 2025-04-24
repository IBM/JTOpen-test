///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSBatchCompress.java
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
// File Name:    JDPSBatchCompress.java
//
// Classes:      JDPSBatchCompress
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.BinaryConverter;
import com.ibm.as400.access.Trace;

import test.JDTestDriver;
import test.JDTestcase;
import test.JVMInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;



/**
Testcase JDPSBatchCompress.  This tests the default toolbox variable
input compression setting for block update. 
**/
public class JDPSBatchCompress
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSBatchCompress";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }
  
     static int RUN_MINUTES = 1; 
     static {
       try { 
         // System.out.print("Checking env for JDPSBatchCompressMinutes ...");
         String value = System.getenv("JDPSBatchCompressMinutes");
         if (value != null) { 
            RUN_MINUTES = Integer.parseInt(value); 
            if (RUN_MINUTES < 1) RUN_MINUTES = 1;
            System.out.println("RUN_MINUTES set to "+RUN_MINUTES); 
         } else {
           System.out.println(" not found"); 
         }
       } catch (Exception e) { 
         System.out.println("Warning:  exception caught ");
         e.printStackTrace(System.out); 
       } catch (Error e) {
	   int jdk = JVMInfo.getJDK(); 
	       System.out.println("Warning:  exception caught  jdk="+jdk);
	       e.printStackTrace(System.out);
       }
     }
  
      boolean useCompressConnection = true; 
      Connection connection_;
      String formatProperties=";time format=jis;date format=jis";
      String compressionProperties=";variable field compression=all;use block update=true";   
      String nocompressionProperties=";variable field compression=true";   
      Connection connectionNoCompress_; 
      boolean useNulls = true;
      private long executeBatchTime; 
      boolean calculateBytesWritten = false; 
      long bytesWritten = 0;
      boolean showRow = false;
      boolean issue53914exists = false; 

/**
Constructor.
**/
    public JDPSBatchCompress (AS400 systemObject,
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
			      String password,
			      String powerUserID,
			      String powerPassword)
    {
        super (systemObject, "JDPSBatchCompress",
               namesAndVars, runMode, fileOutputStream,
               password,powerUserID, powerPassword);
    }


    public JDPSBatchCompress (AS400 systemObject,
		      String testname, 
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password,
                            String powerUserID,
                            String powerPassword)
    {
        super (systemObject, testname,
               namesAndVars, runMode, fileOutputStream,
               password,powerUserID, powerPassword);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
      if (areBooleansSupported()) {
         TYPE_COUNT = TYPE_BOOLEAN + 1; 
      };
      
        String url = baseURL_;
        connection_ = testDriver_.getConnection (
		url + formatProperties+compressionProperties,
		userId_,
                encryptedPassword_);

        connectionNoCompress_ = testDriver_.getConnection (
            url + formatProperties+nocompressionProperties,
            userId_,
                        encryptedPassword_);
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        connection_.close ();
        connectionNoCompress_.close(); 
    }

    public static final int VERIFY_NONE = 0; 
    public static final int VERIFY_COMPRESSION = 1; 
    public static final int VERIFY_NO_COMPRESSION = 2; 

public static final int TYPE_VARCHAR30 = 0; 
public static final int TYPE_LONGVARCHAR = 1; 
public static final int TYPE_NVARCHAR30 = 2;
public static final int TYPE_VARGRAPHIC30 = 3;
public static final int TYPE_LONGVARGRAPHIC = 4;
public static final int TYPE_INTEGER = 5;
public static final int TYPE_SMALLINT = 6;
public static final int TYPE_BIGINT   = 7;
public static final int TYPE_REAL     = 8;
public static final int TYPE_FLOAT    = 9;
public static final int TYPE_DOUBLE   = 10;
public static final int TYPE_DECIMAL102 = 11;
public static final int TYPE_DECIMAL152 = 12;
public static final int TYPE_DECIMAL105 = 13;
public static final int TYPE_DECIMAL155 = 14;
public static final int TYPE_CHAR30       = 15;
public static final int TYPE_NCHAR      = 16;
public static final int TYPE_GRAPHIC    = 17;
public static final int TYPE_DATE       = 18;
public static final int TYPE_TIME       = 19;
public static final int TYPE_TIMESTAMP  = 20;
public static final int TYPE_CLOB       = 21;
public static final int TYPE_DBCLOB     = 22;
public static final int TYPE_BLOB       = 23;
public static final int TYPE_NUMERIC102 = 24;
public static final int TYPE_NUMERIC152 = 25;
public static final int TYPE_NUMERIC105 = 26;
public static final int TYPE_NUMERIC155 = 27;
public static final int TYPE_INTKEY     = 28;
public static final int TYPE_VARCHAR60  = 29;
public static final int TYPE_VARCHAR1000  = 30; 
public static final int TYPE_VARCHARFBD255 = 31; 
public static final int TYPE_VARBINARY255 = 32; 
public static final int TYPE_VARCHARHEX255 = 33;
public static final int TYPE_CHARFBD25 = 34;
public static final int TYPE_BINARY25 = 35; 
public static final int TYPE_CHARHEX25 = 36;
public static final int TYPE_VARCHAR256 = 37;
public static final int TYPE_VARCHARFBD256 = 38;
public static final int TYPE_VARBINARY256 = 39;
public static final int TYPE_VARCHAR257 = 40;
public static final int TYPE_VARCHARFBD257 = 41;
public static final int TYPE_VARBINARY257 = 42;
public static final int TYPE_VARCHAR32700 = 43;
public static final int TYPE_VARCHARFBD32700 = 44;
public static final int TYPE_VARBINARY32700 = 45;
public static final int TYPE_BOOLEAN   = 46; 

public static int TYPE_COUNT = 46; // Default to no boolean  

public static String[] typeName = {
  "TYPE_VARCHAR30",     /* TYPE_VARCHAR30 = 0 */ 
  "TYPE_LONGVARCHAR",    /* TYPE_LONGVARCHAR = 1 */
  "TYPE_NVARCHAR30",    /* TYPE_NVARCHAR30 = 2 */
  "TYPE_VARGRAPHIC30",    /* TYPE_VARGRAPHIC30 = 3 */
  "TYPE_LONGVARGRAPHIC",    /* TYPE_LONGVARGRAPHIC = 4 */ 
  "TYPE_INTEGER",                        /* TYPE_INTEGER = 5 */ 
  "TYPE_SMALLINT",   /* TYPE_SMALLINT = 6 */
  "TYPE_BIGINT",   /* TYPE_BIGINT   = 7 */
  "TYPE_REAL",   /* TYPE_REAL     = 8 */
  "TYPE_FLOAT",   /* TYPE_FLOAT    = 9 */
  "TYPE_DOUBLE",   /* TYPE_DOUBLE   = 10 */
  "TYPE_DECIMAL102",   /* TYPE_DECIMAL102 = 11 */
  "TYPE_DECIMAL152",   /* TYPE_DECIMAL152 = 12 */
  "TYPE_DECIMAL105",   /* TYPE_DECIMAL105 = 13 */
  "TYPE_DECIMAL155",   /* TYPE_DECIMAL155 = 14 */
  "TYPE_CHAR30",   /* TYPE_CHAR30       = 15 */
  "TYPE_NCHAR",   /* TYPE_NCHAR      = 16 */
  "TYPE_GRAPHIC",   /* TYPE_GRAPHIC    = 17 */
  "TYPE_DATE",   /* TYPE_DATE       = 18 */
  "TYPE_TIME",   /* TYPE_TIME       = 19 */
  "TYPE_TIMESTAMP",   /* TYPE_TIMESTAMP  = 20 */
  "TYPE_CLOB",   /* TYPE_CLOB       = 21 */
  "TYPE_DBCLOB",   /* TYPE_DBCLOB     = 22 */
  "TYPE_BLOB",   /* TYPE_BLOB       = 23 */
  "TYPE_NUMERIC102",   /* TYPE_NUMERIC102 = 24 */
  "TYPE_NUMERIC152",   /* TYPE_NUMERIC152 = 25 */
  "TYPE_NUMERIC105",   /* TYPE_NUMERIC105 = 26 */
  "TYPE_NUMERIC155",   /* TYPE_NUMERIC155 = 27 */
  "TYPE_INTKEY",       /* TYPE_INTKEY     = 28 */ 
  "TYPE_VARCHAR60",     /* TYPE_VARCHAR60 = 29 */
  "TYPE_VARCHAR1000",     /* TYPE_VARCHAR1000 = 30 */
  "TYPE_VARCHARFBD255",   /* TYPE_VARCHARFBD255 = 31 */ 
  "TYPE_VARBINARY255",   /* TYPE_VARBINARY255 = 32 */ 
  "TYPE_VARCHARHEX255",   /* TYPE_VARCHARHEX255 = 33 */
  "TYPE_CHARFBD25",   /* TYPE_CHARFBD25 = 34 */ 
  "TYPE_BINARY25",   /* TYPE_BINARY25 = 35 */ 
  "TYPE_CHARHEX25",   /* TYPE_CHARHEX25 = 36 */
  "TYPE_VARCHAR256", /* TYPE_VARCHAR256 = 37 */ 
  "TYPE_VARCHARFBD256", /* TYPE_VARCHARFBD256 = 38 */ 
  "TYPE_VARBINARY256", /* TYPE_VARBINARY256 = 39 */ 
  "TYPE_VARCHAR257", /* TYPE_VARCHAR257 = 40 */ 
  "TYPE_VARCHARFBD257", /* TYPE_VARCHARFBD257 = 41 */ 
  "TYPE_VARBINARY257", /* TYPE_VARBINARY257 = 42 */ 
  "TYPE_VARCHAR32700", /* TYPE_VARCHAR32700 = 43 */ 
  "TYPE_VARCHARFBD32700", /* TYPE_VARCHARFBD32700 = 44 */ 
  "TYPE_VARBINARY32700", /* TYPE_VARBINARY32700 = 45 */ 
  "TYPE_BOOLEAN",        /* TYPE_BOOLEAN = 46 */ 

};

public static String[] createSyntax = {
    "VARCHAR(30)",     /* TYPE_VARCHAR30 = 0 */ 
    "LONG VARCHAR",    /* TYPE_LONGVARCHAR = 1 */
    "NVARCHAR(30)",    /* TYPE_NVARCHAR30 = 2 */
    "VARGRAPHIC(30) CCSID 1200",    /* TYPE_VARGRAPHIC30 = 3 */
    "LONG VARGRAPHIC CCSID 1200",    /* TYPE_LONGVARGRAPHIC = 4 */ 
    "INTEGER",                        /* TYPE_INTEGER = 5 */
    "SMALLINT",   /* TYPE_SMALLINT = 6 */
    "BIGINT",   /* TYPE_BIGINT   = 7 */
    "REAL",   /* TYPE_REAL     = 8 */
    "FLOAT",   /* TYPE_FLOAT    = 9 */
    "DOUBLE",   /* TYPE_DOUBLE   = 10 */
    "DECIMAL(10,2)",   /* TYPE_DECIMAL102 = 11 */
    "DECIMAL(15,2)",   /* TYPE_DECIMAL152 = 12 */
    "DECIMAL(10,5)",   /* TYPE_DECIMAL105 = 13 */
    "DECIMAL(15,5)",   /* TYPE_DECIMAL155 = 14 */
    "CHAR(30)",   /* TYPE_CHAR30       = 15 */
    "NCHAR(30)",   /* TYPE_NCHAR      = 16 */
    "GRAPHIC(30) CCSID 1200",   /* TYPE_GRAPHIC    = 17 */
    "DATE",   /* TYPE_DATE       = 18 */
    "TIME",   /* TYPE_TIME       = 19 */
    "TIMESTAMP",   /* TYPE_TIMESTAMP  = 20 */
    "CLOB(1M)",   /* TYPE_CLOB       = 21 */
    "DBCLOB(1M) CCSID 1200",   /* TYPE_DBCLOB     = 22 */
    "BLOB(1M)",   /* TYPE_BLOB       = 23 */
    "NUMERIC(10,2)",   /* TYPE_NUMERIC102 = 24 */
    "NUMERIC(15,2)",   /* TYPE_NUMERIC152 = 25 */
    "NUMERIC(10,5)",   /* TYPE_NUMERIC105 = 26 */
    "NUMERIC(15,5)",   /* TYPE_NUMERIC155 = 27 */
    "INTEGER PRIMARY KEY",/* TYPE_INTKEY     = 28 */
    "VARCHAR(60)",     /* TYPE_VARCHAR60 = 29 */
    "VARCHAR(1000)",     /* TYPE_VARCHAR1000 = 30 */
    "VARCHAR(255) FOR BIT DATA ",  /* TYPE VARCHARFBD255 = 31 */ 
    "VARBINARY(255) ",  /* TYPE VARBINARY255 = 32 */
    "VARCHAR(255) CCSID 65535", /* TYPE VARCHARHEX255 = 33 */ 
    "CHAR(25) FOR BIT DATA ",  /* TYPE CHARFBD255 = 34 */ 
    "BINARY(25) ",  /* TYPE BINARY255 = 35 */
    "CHAR(25) CCSID 65535", /* TYPE CHARHEX255 = 36 */
    "VARCHAR(256)", /* TYPE_VARCHAR256 = 37 */ 
    "VARCHAR(256) FOR BIT DATA", /* TYPE_VARCHARFBD256 = 38 */ 
    "VARBINARY(256)", /* TYPE_VARBINARY256 = 39 */ 
    "VARCHAR(257)", /* TYPE_VARCHAR257 = 40 */ 
    "VARCHAR(257) FOR BIT DATA", /* TYPE_VARCHARFBD257 = 41 */ 
    "VARBINARY(257)", /* TYPE_VARBINARY257 = 42 */ 
    "VARCHAR(32700)", /* TYPE_VARCHAR32700 = 43 */ 
    "VARCHAR(32700) FOR BIT DATA", /* TYPE_VARCHARFBD32700 = 44 */ 
    "VARBINARY(32700)", /* TYPE_VARBINARY32700 = 45 */ 
    "BOOLEAN",          /* TYPE_BOOLEAN = 46 */ 
};


/* The type size calculations can be found in the SQL Reference
   Manual in the CREATE Table section.  This table uses those values.
   The calculation of size adds bytes for the indicators.
*/ 
public static final int LOB_TYPE_SIZE = 29; 
public static int[] typeSize = {
  32,     /* TYPE_VARCHAR30 = 0 */ 
  8208,    /* TYPE_LONGVARCHAR = 1 */
  62,    /* TYPE_NVARCHAR30 = 2 */
  62,    /* TYPE_VARGRAPHIC30 = 3 */
  16400,    /* TYPE_LONGVARGRAPHIC = 4 */ 
  4,   /* TYPE_INTEGER = 5 */
  2,   /* TYPE_SMALLINT = 6 */
  8,   /* TYPE_BIGINT   = 7 */
  8,   /* TYPE_REAL     = 8 */
  8,   /* TYPE_FLOAT    = 9 */
  8,   /* TYPE_DOUBLE   = 10 */
  6,   /* TYPE_DECIMAL102 = 11 */
  9,   /* TYPE_DECIMAL152 = 12 */
  6,   /* TYPE_DECIMAL105 = 13 */
  9,   /* TYPE_DECIMAL155 = 14 */
  30,   /* TYPE_CHAR30       = 15 */
  60,   /* TYPE_NCHAR      = 16 */
  62,   /* TYPE_GRAPHIC    = 17 */
  10,   /* TYPE_DATE       = 18 */
  8,    /* TYPE_TIME       = 19 */
  26,   /* TYPE_TIMESTAMP  = 20 */
  LOB_TYPE_SIZE,   /* TYPE_CLOB       = 21 */ 
  LOB_TYPE_SIZE,   /* TYPE_DBCLOB     = 22 */
  LOB_TYPE_SIZE,   /* TYPE_BLOB       = 23 */
  10,   /* TYPE_NUMERIC102 = 24 */
  15,   /* TYPE_NUMERIC152 = 25 */
  10,   /* TYPE_NUMERIC105 = 26 */
  15,   /* TYPE_NUMERIC155 = 27 */
  4,/* TYPE_INTKEY     = 28 */
  62,     /* TYPE_VARCHAR60 = 29 */
  1001,     /* TYPE_VARCHAR1000 = 30 */
  257,  /* TYPE VARCHARFBD255 = 31 */ 
  257,  /* TYPE VARBINARY255 = 32 */
  258, /* TYPE VARCHARHEX255 = 33 */ 
  255,  /* TYPE CHARFBD255 = 34 */ 
  255,  /* TYPE BINARY255 = 35 */
  255, /* TYPE CHARHEX255 = 36 */
  258, /* TYPE_VARCHAR256 = 37 */ 
  258, /* TYPE_VARCHARFBD256 = 38 */ 
  258, /* TYPE_VARBINARY256 = 39 */ 
  259, /* TYPE_VARCHAR257 = 40 */ 
  259, /* TYPE_VARCHARFBD257 = 41 */ 
  259, /* TYPE_VARBINARY257 = 42 */ 
  32702, /* TYPE_VARCHAR32700 = 43 */ 
  32702, /* TYPE_VARCHARFBD32700 = 44 */ 
  32702, /* TYPE_VARBINARY32700 = 45 */ 
  1,     /* TYPE_BOOLEAN = 46 */ 

};



public static String[][] sampleValues = {
  { "","A","B","C","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890"}, /* TYPE_VARCHAR30 */ 
  { "","A","B","C","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890","HUNDRED890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"}, /* TYPE_LONGVARCHAR */
  { "","A","\u03B2","\u500c","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890"}, /* TYPE_NVARCHAR30 */
  { "","A","\u03B2","\u500c","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890"}, /* TYPE_VARGRAPHIC30 */ 
  { "","A","\u03B2","\u500c","TEN4567890","TWENTY678901234567890","THIRTY789012345678901234567890"}, /* TYPE_LONGVARGRAPHIC */
  { "0", "1", "2", "4096","16384","2000000000" },        /* TYPE_INTEGER */
  { "0", "1", "2", "4096","16384",},   /* TYPE_SMALLINT = 6 */
  { "0", "1", "2", "4096","16384", },   /* TYPE_BIGINT   = 7 */
  { "0.0", "1.0", "2.0", "4096.0","16384.0", },   /* TYPE_REAL     = 8 */
  { "0.0", "1.0", "2.0", "4096.0","16384.0",},   /* TYPE_FLOAT    = 9 */
  { "0.0", "1.0", "2.0", "4096.0","16384.0",},   /* TYPE_DOUBLE   = 10 */
  { "0.00", "1.00", "2.00", "4096.00","16384.00",},   /* TYPE_DECIMAL102 = 11 */
  { "0.00", "1.00", "2.00", "4096.00","16384.00",},   /* TYPE_DECIMAL152 = 12 */
  { "0.00000", "1.00000", "2.00000", "4096.00000","16384.00000",},   /* TYPE_DECIMAL105 = 13 */
  { "0.00000", "1.00000", "2.00000", "4096.00000","16384.00000",},   /* TYPE_DECIMAL155 = 14 */
  {
      "                              ",
      "A                             ",
      "B                             ",
      "C                             ",
      "TEN4567890                    ",
      "TWENTY78901234567890          ",
      "THIRTY789012345678901234567890"},   /* TYPE_CHAR30       = 15 */
  {
     "                              ",
     "A                             ",
     "\u03B2                             ",
     "\u500c                             ",
     "TEN4567890                    ",
     "TWENTY78901234567890          ",
     "THIRTY789012345678901234567890"
      },   /* TYPE_NCHAR      = 16 */
  {
     "                              ",
     "A                             ",
     "\u03B2                             ",
     "\u500c                             ",
     "TEN4567890                    ",
     "TWENTY78901234567890          ",
     "THIRTY789012345678901234567890"
     },   /* TYPE_GRAPHIC    = 17 */
  { "1987-10-31", "2011-03-16", "2013-12-26", "1984-05-23"},   /* TYPE_DATE       = 18 */
  { "00:00:00", "01:02:03","04:05:06","07:08:09","10:11:12","10:20:30" },   /* TYPE_TIME       = 19 */
  { "2013-12-05 16:04:55.137158",
    "2012-11-04 15:03:44.026047",
    "2010-10-03 14:02:43.915936",
    "2009-09-02 13:01:42.804825",
    "2008-08-01 12:00:41.793714",
    "2007-07-31 11:49:40.682603",
    },   /* TYPE_TIMESTAMP  = 20 */
  {"","A","B","C","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890"},   /* TYPE_CLOB       = 21 */
  {"","A","\u03B2","\u500c","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890"},   /* TYPE_DBCLOB     = 22 */
  {"01","01","02", "03", "04",},   /* TYPE_BLOB       = 23 */
  {"0.00", "1.00", "2.00", "4096.00","16384.00", },   /* TYPE_NUMERIC102 = 24 */
  {"0.00", "1.00", "2.00", "4096.00","16384.00", },   /* TYPE_NUMERIC152 = 25 */
  {"0.00000", "1.00000", "2.00000", "4096.00000","16384.00000", },   /* TYPE_NUMERIC105 = 26 */
  {"0.00000", "1.00000", "2.00000", "4096.00000","16384.00000", },   /* TYPE_NUMERIC155 = 27 */
  {},  /* TYPE_INTKEY     = 28 .. requires special case code*/
  { "","NINE56789","EIGHT678","SEVEN67","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890"}, /* TYPE_VARCHAR60 = 29 */ 
  { "","NINE56789","EIGHT678","SEVEN67","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890"}, /* TYPE_VARCHAR1000 = 30 */ 
  {"01","01","02", "03", "04",},   /* TYPE_VARCHARFBD255       = 31 */
  {"01","01","02", "03", "04",},   /* TYPE_VARBINARY255       = 32 */
  {"01","01","02", "03", "04",},   /* TYPE_VARCHARHEX255       = 33 */
  {"01020304050607080910111213141516171819202122232425",
   "01020304050607080910111213141516171819202122232425",
   "02020304050607080910111213141516171819202122232425", 
   "03020304050607080910111213141516171819202122232425", 
   "04020304050607080910111213141516171819202122232425",},   /* TYPE_CHARFBD25       = 34 */
  {
       "01020304050607080910111213141516171819202122232425",
       "01020304050607080910111213141516171819202122232425",
       "02020304050607080910111213141516171819202122232425", 
       "03020304050607080910111213141516171819202122232425", 
       "04020304050607080910111213141516171819202122232425",
   },   /* TYPE_BINARY25       = 35 */
  {
       "01020304050607080910111213141516171819202122232425",
       "01020304050607080910111213141516171819202122232425",
       "02020304050607080910111213141516171819202122232425", 
       "03020304050607080910111213141516171819202122232425", 
       "04020304050607080910111213141516171819202122232425",
   },   /* TYPE_CHARHEX25       = 36 */


   { "","NINE56789","EIGHT678","SEVEN67","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890"}, /* TYPE_VARCHAR256 = 37 */ 
  {
       "01020304050607080910111213141516171819202122232425",
       "01020304050607080910111213141516171819202122232425",
       "02020304050607080910111213141516171819202122232425", 
       "03020304050607080910111213141516171819202122232425", 
       "04020304050607080910111213141516171819202122232425",
   }, /* TYPE_VARCHARFBD256 = 38 */ 
  {
       "01020304050607080910111213141516171819202122232425",
       "01020304050607080910111213141516171819202122232425",
       "02020304050607080910111213141516171819202122232425", 
       "03020304050607080910111213141516171819202122232425", 
       "04020304050607080910111213141516171819202122232425",
   }, /* TYPE_VARBINARY256 = 39 */ 
  { "","NINE56789","EIGHT678","SEVEN67","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890"}, /* TYPE_VARCHAR257 = 40 */ 
  {
       "01020304050607080910111213141516171819202122232425",
       "01020304050607080910111213141516171819202122232425",
       "02020304050607080910111213141516171819202122232425", 
       "03020304050607080910111213141516171819202122232425", 
       "04020304050607080910111213141516171819202122232425",
   }, /* TYPE_VARCHARFBD257 = 41 */ 
  {
       "01020304050607080910111213141516171819202122232425",
       "01020304050607080910111213141516171819202122232425",
       "02020304050607080910111213141516171819202122232425", 
       "03020304050607080910111213141516171819202122232425", 
       "04020304050607080910111213141516171819202122232425",
   }, /* TYPE_VARBINARY257 = 42 */ 
  { "","NINE56789","EIGHT678","SEVEN67","TEN4567890","TWENTY78901234567890","THIRTY789012345678901234567890"}, /* TYPE_VARCHAR32700 = 43 */ 
  {
       "01020304050607080910111213141516171819202122232425",
       "01020304050607080910111213141516171819202122232425",
       "02020304050607080910111213141516171819202122232425", 
       "03020304050607080910111213141516171819202122232425", 
       "04020304050607080910111213141516171819202122232425",
   }, /* TYPE_VARCHARFBD32700 = 44 */ 
  {
       "01020304050607080910111213141516171819202122232425",
       "01020304050607080910111213141516171819202122232425",
       "02020304050607080910111213141516171819202122232425", 
       "03020304050607080910111213141516171819202122232425", 
       "04020304050607080910111213141516171819202122232425",
   }, /* TYPE_VARBINARY32700 = 45 */ 

  {
       "0",
       "1",
       "1", 
       "0", 
       "1",
   }, /* TYPE_BOOLEAN = 46 */ 





}; 

public static int[] generatedIndex = {
  0,   /* 0 */ 
  0,   /* 1 */
  0,   /* 2 */
  0,   /* 3 */
  0,   /* 4 */
  0,   /* 5 */
  0,   /* TYPE_SMALLINT = 6 */
  0,   /* TYPE_BIGINT   = 7 */
  0,   /* TYPE_REAL     = 8 */
  0,   /* TYPE_FLOAT    = 9 */
  0,   /* TYPE_DOUBLE   = 10 */
  0,   /* TYPE_DECIMAL102 = 11 */
  0,   /* TYPE_DECIMAL152 = 12 */
  0,   /* TYPE_DECIMAL105 = 13 */
  0,   /* TYPE_DECIMAL155 = 14 */
  0,   /* TYPE_CHAR30       = 15 */
  0,   /* TYPE_NCHAR      = 16 */
  0,   /* TYPE_GRAPHIC    = 17 */
  0,   /* TYPE_DATE       = 18 */
  0,   /* TYPE_TIME       = 19 */
  0,   /* TYPE_TIMESTAMP  = 20 */
  0,   /* TYPE_CLOB       = 21 */
  0,   /* TYPE_DBCLOB     = 22 */
  0,   /* TYPE_BLOB       = 23 */
  0,   /* TYPE_NUMERIC102 = 24 */
  0,   /* TYPE_NUMERIC152 = 25 */
  0,   /* TYPE_NUMERIC105 = 26 */
  0,   /* TYPE_NUMERIC155 = 27 */
  0,   /* TYPE_INTKEY     = 28 */
  0,   /* TYPE_VARCHAR60  = 29 */
  0,   /* TYPE_VARCHAR1000  = 30 */
  0,   /* TYPE_VARCHARFBD255 = 31 */ 
  0,   /* TYPE_VARBINARY255 = 32 */ 
  0,   /* TYPE_VARCHARHEX255 = 33 */ 
  0,   /* TYPE_VARCHARFBD255 = 34 */ 
  0,   /* TYPE_VARBINARY255 = 35 */ 
  0,   /* TYPE_VARCHARHEX255 = 36 */
  0,   /* TYPE_VARCHAR256 = 37 */ 
  0,   /* TYPE_VARCHARFBD256 = 38 */ 
  0,   /* TYPE_VARBINARY256 = 39 */ 
  0,   /* TYPE_VARCHAR257 = 40 */ 
  0,   /* TYPE_VARCHARFBD257 = 41 */ 
  0,   /* TYPE_VARBINARY257 = 42 */ 
  0,   /* TYPE_VARCHAR32700 = 43 */ 
  0,   /* TYPE_VARCHARFBD32700 = 44 */ 
  0,   /* TYPE_VARBINARY32700 = 45 */ 
  0,   /* TYPE_BOOLEAN = 46 */ 
};

void resetGeneratedValues(StringBuffer sb) {
  sb.append("resetGeneratedValues()\n"); 
  for (int i = 0; i < generatedIndex.length; i++) {
    generatedIndex[i] = 0; 
  }
}

String getNextValue(int dataType) {
  String nextValue = null; 
  int nextIndex = generatedIndex[dataType];
  if (dataType == TYPE_INTKEY) {
    nextIndex++; 
    generatedIndex[dataType] = nextIndex; 
    nextValue = ""+nextIndex; 
  } else { 
    String[] samples = sampleValues[dataType];
    nextValue = samples[nextIndex];
    nextIndex++; 
    if (nextIndex >= samples.length) nextIndex = 0; 
    generatedIndex[dataType] = nextIndex; 
  }
  return nextValue; 
}


static String  displayDataTypes(int[] dataTypes) {
  StringBuffer sb = new StringBuffer(); 
  for (int i = 0; i < dataTypes.length ; i++) {
    if (i > 0) sb.append(","); 
    sb.append(typeName[dataTypes[i]]); 
  }
  return sb.toString(); 
}



private String[][] batchInsert(String tablename, int[] dataTypes, int rows,
    StringBuffer sb) throws SQLException {
  String[][] insertedValues = new String[rows][];  
      
  String fullTableName = collection_+"."+tablename; 
  StringBuffer insertSql = new StringBuffer(); 
  insertSql.append("INSERT INTO "+fullTableName+" VALUES(");
  for (int i = 0; i < dataTypes.length; i++) {
    if (i > 0) insertSql.append(",");
    // Add spaces to prevent problem with PS cache. 
    for (int j = 0; j < dataTypes[i]; j++) {
	insertSql.append(" "); 
    }
    insertSql.append("?"); 
  }
  insertSql.append(")"); 
  String sql = insertSql.toString(); 
  sb.append("Preparing "+sql+"\n"); 
  PreparedStatement ps;
  if (useCompressConnection) { 
   ps = connection_.prepareStatement(sql);
  } else {
    ps = connectionNoCompress_.prepareStatement(sql);
  }
  for (int i = 0; i < rows; i++) {
      if (showRow) {   
	  sb.append("Adding row: ");
      }
    
    insertedValues[i] = new String[dataTypes.length];
    boolean[] setNull = getNullInfo(i, dataTypes.length);
    if (dataTypes[0] == TYPE_INTKEY) {
      setNull[0] = false; 
    }
    for (int j = 0; j < dataTypes.length; j++) { 
        if (setNull[j]) {
          ps.setString(j + 1, null);
          insertedValues[i][j] = null;
          if (showRow)
            sb.append("null,");
        } else {
          String nextValue = getNextValue(dataTypes[j]);
          insertedValues[i][j] = nextValue;

          switch (dataTypes[j]) {
            case TYPE_BLOB:
            case TYPE_VARBINARY255:
            case TYPE_VARCHARFBD255:
            case TYPE_VARCHARHEX255:
            case TYPE_BINARY25:
            case TYPE_CHARFBD25:
	    case TYPE_CHARHEX25:
	    case TYPE_VARCHARFBD256 :
	    case TYPE_VARBINARY256 :
	    case TYPE_VARCHARFBD257 :
	    case TYPE_VARBINARY257 :
	    case TYPE_VARCHARFBD32700 :
	    case TYPE_VARBINARY32700 :

            {
              byte[] stuff = BinaryConverter.stringToBytes(nextValue);
              ps.setBytes(j + 1, stuff);
            } /* binary case */ 
            break;
            default:
              ps.setString(j + 1, nextValue);
          } /* switch */ 

          if (showRow)
            sb.append(nextValue + ",");
        }
    }
    
    ps.addBatch();
    if (showRow)  sb.append("\n"); 
  }
  sb.append("Executing\n");
  String traceFile = null; 
  if (calculateBytesWritten) {
      System.gc(); 
    traceFile = "/tmp/JDPSBatchCompress.write."+collection_+".trace"; 
    File checkFile = new File(traceFile); 
    if (checkFile.exists()) {
      checkFile.delete(); 
    }
    try { 
    Trace.setFileName(traceFile);
    } catch (Exception e) { } 
    Trace.setTraceDatastreamOn(true);
    Trace.setTraceOn(true); 
  }
  System.gc(); 
  long startTime = System.currentTimeMillis(); 
  ps.executeBatch();
  long finishTime = System.currentTimeMillis();
  if (traceFile != null) {
      bytesWritten = countSentBytes(traceFile);
    
      Trace.setTraceOn(false); 
      Trace.setTraceDatastreamOn(false);
      try { 
      Trace.setFileName(null);
      } catch (Exception e) { } 
      File checkFile = new File(traceFile); 
      if (checkFile.exists()) {
	  checkFile.delete(); 
      }

  }
  executeBatchTime = (finishTime - startTime); 
  
  return insertedValues; 
}



private String[][] batchUpdate(String tablename, int[] dataTypes, int rows,
    StringBuffer sb, String[][] currentValues) throws SQLException {
      
  String fullTableName = collection_+"."+tablename; 
  StringBuffer updateSql = new StringBuffer(); 
  updateSql.append("UPDATE "+fullTableName+" SET ");
  for (int i = 1; i < dataTypes.length; i++) {
    if (i > 1) updateSql.append(",");
    // Add spaces to prevent problem with PS cache. 
    for (int j = 0; j < dataTypes[i]; j++) {
	updateSql.append(" "); 
    }

    updateSql.append("col"+i+" = ");
    updateSql.append("?"); 
  }
  updateSql.append(" WHERE col0 = ?"); 
  
  String sql = updateSql.toString(); 
  sb.append("Preparing "+sql+"\n"); 
  PreparedStatement ps;
  if (useCompressConnection) { 
   ps = connection_.prepareStatement(sql);
  } else {
    ps = connectionNoCompress_.prepareStatement(sql);
  }
  resetGeneratedValues(sb); 
  for (int i = 0; i < currentValues.length; i++) {
    if (showRow)  sb.append("Updating row: "); 
    
    boolean[] setNull = getNullInfo(i, dataTypes.length);
    if (dataTypes[0] == TYPE_INTKEY) {
      setNull[0] = false; 
    }
    for (int j = 1; j < dataTypes.length; j++) { 
       if (setNull[j]) { 
         ps.setString(j, null);
         currentValues[i][j] = null;
         if (showRow)  sb.append("null,");
       } else { 
         String nextValue =getNextValue(dataTypes[j]);  
         currentValues[i][j] = nextValue;

         if (showRow)  sb.append(nextValue+","); 
	 switch (dataTypes[j]) {
	     case  TYPE_BLOB:
	     case TYPE_VARBINARY255:
	     case TYPE_VARCHARFBD255:
	     case TYPE_VARCHARHEX255:
	     case TYPE_BINARY25:
	     case TYPE_CHARFBD25:
	     case TYPE_CHARHEX25:
	    case TYPE_VARCHARFBD256 :
	    case TYPE_VARBINARY256 :
	    case TYPE_VARCHARFBD257 :
	    case TYPE_VARBINARY257 :
	    case TYPE_VARCHARFBD32700 :
	    case TYPE_VARBINARY32700 :

		 {
		     byte[] stuff = BinaryConverter.stringToBytes(nextValue);
		     ps.setBytes(j, stuff); 
		 }
		 break;
	     default:
		 ps.setString(j, nextValue);
	 } /* switch */  
       


       }
    } /* for */ 
    ps.setString(dataTypes.length, currentValues[i][0]);
    sb.append(" where col1="+currentValues[i][0]); 
    ps.addBatch();
    sb.append("\n"); 
  }
  sb.append("Executing\n");
  String traceFile = null; 
  if (calculateBytesWritten) { 
    traceFile = "/tmp/JDPSBatchCompress.write."+collection_+".trace"; 
    File checkFile = new File(traceFile); 
    if (checkFile.exists()) {
      checkFile.delete(); 
    }
    try { 
    Trace.setFileName(traceFile);
    } catch (Exception e) { } 
    Trace.setTraceDatastreamOn(true);
    Trace.setTraceOn(true); 
  }

  long startTime = System.currentTimeMillis(); 
  ps.executeBatch();
  long finishTime = System.currentTimeMillis();
  if (traceFile != null) {
      bytesWritten = countSentBytes(traceFile);
    
      Trace.setTraceOn(false); 
      Trace.setTraceDatastreamOn(false);
      try { 
      Trace.setFileName(null);
      } catch (Exception e) { } 

    File checkFile = new File(traceFile); 
    if (checkFile.exists()) {
      checkFile.delete(); 
    }
  
  }
  executeBatchTime = (finishTime - startTime); 
  
  return currentValues; 
}


private String[][] batchDelete(String tablename, int[] dataTypes, int rows,
    StringBuffer sb, String[][] currentValues) throws SQLException {
      
  String fullTableName = collection_+"."+tablename; 
  StringBuffer updateSql = new StringBuffer(); 
  updateSql.append("DELETE FROM "+fullTableName+" WHERE ");
  for (int i = 0; i < dataTypes.length; i++) {
    if (i > 0) updateSql.append(" AND ");
    // Add spaces to prevent problem with PS cache. 
    for (int j = 0; j < dataTypes[i]; j++) {
	updateSql.append(" "); 
    }

    updateSql.append("col"+i+" = ");
    updateSql.append("?"); 
  }

  
  String sql = updateSql.toString(); 
  sb.append("Preparing "+sql+"\n"); 
  PreparedStatement ps;
  if (useCompressConnection) { 
   ps = connection_.prepareStatement(sql);
  } else {
    ps = connectionNoCompress_.prepareStatement(sql);
  }
  ArrayList<String[]> preservedValues = new ArrayList<String[]>(); 
  for (int i = 0; i < currentValues.length; i++) {
    if (showRow)  sb.append("Deleting row: "); 
    
    for (int j = 0; j < dataTypes.length; j++) { 
      String value = currentValues[i][j]; 
	    if (showRow)  sb.append(value+","); 
	    switch (dataTypes[j]) {
		case TYPE_BLOB: 
		case TYPE_VARBINARY255:
		case TYPE_VARCHARFBD255:
		case TYPE_VARCHARHEX255:
		case TYPE_BINARY25:
		case TYPE_CHARFBD25:
		case TYPE_CHARHEX25:
	    case TYPE_VARCHARFBD256 :
	    case TYPE_VARBINARY256 :
	    case TYPE_VARCHARFBD257 :
	    case TYPE_VARBINARY257 :
	    case TYPE_VARCHARFBD32700 :
	    case TYPE_VARBINARY32700 :
		    {
			byte[] stuff ;
			if (value == null) { 
			    stuff = null; 
			} else { 
			    stuff = BinaryConverter.stringToBytes(currentValues[i][j]);
			}
			ps.setBytes(j+1, stuff); 
		    }
		    break;
		default:  {  
			ps.setString(j+1, value);
		    }
	    } /* switch */ 
    }
    ps.addBatch();
    if (showRow)  sb.append("\n");

    i++;
    if (i < currentValues.length) {
      preservedValues.add(currentValues[i]); 
    }
  }
  sb.append("Executing\n");
  String traceFile = null; 
  if (calculateBytesWritten) { 
    traceFile = "/tmp/JDPSBatchCompress.write."+collection_+".trace"; 
    File checkFile = new File(traceFile); 
    if (checkFile.exists()) {
      checkFile.delete(); 
    }
    try { 
    Trace.setFileName(traceFile);
    } catch (Exception e) { } 
    Trace.setTraceDatastreamOn(true);
    Trace.setTraceOn(true); 
  }

  long startTime = System.currentTimeMillis(); 
  ps.executeBatch();
  long finishTime = System.currentTimeMillis();
  if (traceFile != null) {
      bytesWritten = countSentBytes(traceFile);
    
      Trace.setTraceOn(false); 
      Trace.setTraceDatastreamOn(false);
      try { 
      Trace.setFileName(null);
      } catch (Exception e) { } 
      File checkFile = new File(traceFile); 
      if (checkFile.exists()) {
	  checkFile.delete(); 
      }
  
  }
  executeBatchTime = (finishTime - startTime); 
  Object[] objectValues = preservedValues.toArray();
  String[][] remainingValues = new String[objectValues.length][];
  for (int i = 0; i < objectValues.length; i++) {
    remainingValues[i] = (String[]) objectValues[i]; 
  }
  return remainingValues; 
}


private String[][] batchMerge(String tablename, int[] dataTypes, int rows,
    StringBuffer sb, String[][] currentValues) throws SQLException {
      
  String fullTableName = collection_+"."+tablename;
  String mergeTableName;
  String shortMergeTableName;
  if (tablename.charAt(0) != 'M') {
      shortMergeTableName = "M"+tablename.substring(1);
  } else {
      shortMergeTableName = "N"+tablename.substring(1);
  }

  mergeTableName = collection_+"."+shortMergeTableName;

  createTable(shortMergeTableName, dataTypes, sb);
  batchInsert(shortMergeTableName, dataTypes, 1, sb);

  StringBuffer updateSql = new StringBuffer(); 
  updateSql.append("MERGE INTO "+fullTableName+" a  ");
  updateSql.append(" USING ( SELECT col0 from "+mergeTableName+" ) b");
  updateSql.append(" ON (a.col0=?)");
  updateSql.append(" WHEN MATCHED THEN ");
  updateSql.append(" UPDATE SET "); 
  for (int i = 1; i < dataTypes.length; i++) {
    if (i > 1) updateSql.append(", ");
    // Add spaces to prevent problem with PS cache. 
    for (int j = 0; j < dataTypes[i]; j++) {
	updateSql.append(" "); 
    }

    updateSql.append("col"+i+" = ");
    updateSql.append("?"); 
  }
  updateSql.append(" WHEN NOT MATCHED THEN INSERT ( ");
  for (int i = 0; i < dataTypes.length; i++) {
    if (i > 0) updateSql.append(", ");
    updateSql.append("col"+i);
  }
  updateSql.append(") VALUES (");
  for (int i = 0; i < dataTypes.length; i++) {
    if (i > 0) updateSql.append(", ");
    // Add spaces to prevent problem with PS cache. 
    for (int j = 0; j < dataTypes[i]; j++) {
	updateSql.append(" "); 
    }

    updateSql.append("?");
  }
  updateSql.append(")");

 
  
  String sql = updateSql.toString(); 
  sb.append("Preparing "+sql+"\n"); 
  PreparedStatement ps;
  if (useCompressConnection) { 
   ps = connection_.prepareStatement(sql);
  } else {
    ps = connectionNoCompress_.prepareStatement(sql);
  }
  ArrayList<String[]> newValuesArrayList = new ArrayList<String[]>();

  for (int i = 0; i < currentValues.length; i++) {
 
    if (showRow)  sb.append("Updating row: "); 
    
    boolean[] setNull = getNullInfo(i, dataTypes.length);
    if (dataTypes[0] == TYPE_INTKEY) {
      setNull[0] = false; 
    }
    ps.setString(1, currentValues[i][0]);
    ps.setString(1+dataTypes.length, currentValues[i][0]);
    // Loop through, alternating update / insert 
    for (int j = 1; j < dataTypes.length; j++) { 
       if (setNull[j]) { 
         ps.setString(j+1, null);
         ps.setString(j+1+dataTypes.length, null);
         currentValues[i][j] = null;
         if (showRow)  sb.append("null,");
       } else { 
	   String nextValue =getNextValue(dataTypes[j]);  
	   currentValues[i][j] = nextValue;

	   if (showRow)  sb.append(nextValue+","); 
	   switch (dataTypes[j]) {
	       case TYPE_BLOB:
	       case TYPE_VARBINARY255:
	       case TYPE_VARCHARFBD255:
	       case TYPE_VARCHARHEX255:
	       case TYPE_BINARY25:
	       case TYPE_CHARFBD25:
	       case TYPE_CHARHEX25:
	    case TYPE_VARCHARFBD256 :
	    case TYPE_VARBINARY256 :
	    case TYPE_VARCHARFBD257 :
	    case TYPE_VARBINARY257 :
	    case TYPE_VARCHARFBD32700 :
	    case TYPE_VARBINARY32700 :

		   {
	       byte[] stuff = BinaryConverter.stringToBytes(nextValue);
	       try { 
	       ps.setBytes(j+1, stuff);
	       ps.setBytes(j+1+dataTypes.length, stuff);
	       } catch (Exception e) { 
	         sb.append("*****\n");
	         sb.append("Exception "+e.toString()+"setting parm #"+(j+1)+" type="+dataTypes[j]+" to "+nextValue); 
	       }
		   }
		   break; 
	       default : {  
		       ps.setString(j+1, nextValue);
		       ps.setString(j+1+dataTypes.length, nextValue);
		   } 
	   } /* switch */ 

       } /* not null */ 
    } /* for j */ 

    if (showRow)  sb.append(" where col1="+currentValues[i][0]); 
    ps.addBatch();
    if (showRow)  sb.append("\n"); 



    for (int j = 0; j < dataTypes.length; j++) { 
	if (currentValues[i][j] == null) {
	    ps.setString(j+1, null);
	    if (showRow)  sb.append("null,"); 
	} else {
	    String insertValue =currentValues[i][j]; 
	    if (showRow)  sb.append(insertValue+",");
	    switch (dataTypes[j]) {
		case TYPE_BLOB:
		case TYPE_VARBINARY255:
		case TYPE_VARCHARFBD255:
		case TYPE_VARCHARHEX255:
		case TYPE_BINARY25:
		case TYPE_CHARFBD25:
		case TYPE_CHARHEX25:
	    case TYPE_VARCHARFBD256 :
	    case TYPE_VARBINARY256 :
	    case TYPE_VARCHARFBD257 :
	    case TYPE_VARBINARY257 :
	    case TYPE_VARCHARFBD32700 :
	    case TYPE_VARBINARY32700 :
		    {
			byte[] stuff;
			if (insertValue == null) { 
			    stuff = null; 
			} else { 
			    stuff = BinaryConverter.stringToBytes(insertValue);
			    ps.setBytes(j+1, stuff);
			}
		    }
		    break;
		default: {  
			ps.setString(j+1, insertValue);
		    }
	    } /* switch */ 
	}
    }
    ps.addBatch();
    if (showRow)  sb.append("\n");

    i++;
    // Now add a new values
    String[] newValues = new String[dataTypes.length];
    setNull = getNullInfo(i, dataTypes.length);
    if (dataTypes[0] == TYPE_INTKEY) {
      setNull[0] = false; 
    }
    sb.append("Inserting "); 
    for (int j = 0; j < dataTypes.length; j++) { 
       if (setNull[j]) { 
         ps.setString(j+1, null);
         ps.setString(j+1+dataTypes.length, null);
         newValues[j] = null;
         sb.append("null,");
       } else { 
         String nextValue =getNextValue(dataTypes[j]);  
         newValues[j] = nextValue;
	 switch (dataTypes[j]) {
	     case TYPE_BLOB:
	     case TYPE_VARBINARY255:
	     case TYPE_VARCHARFBD255:
	     case TYPE_VARCHARHEX255:
	     case TYPE_BINARY25:
	     case TYPE_CHARFBD25:
	     case TYPE_CHARHEX25:
	    case TYPE_VARCHARFBD256 :
	    case TYPE_VARBINARY256 :
	    case TYPE_VARCHARFBD257 :
	    case TYPE_VARBINARY257 :
	    case TYPE_VARCHARFBD32700 :
	    case TYPE_VARBINARY32700 :
		 {
		     byte[] stuff = BinaryConverter.stringToBytes(nextValue);
		     ps.setBytes(j+1, stuff);
		     ps.setBytes(j+1+dataTypes.length, stuff); 
		 }
		 break;
	     default:
		 {  
		     ps.setString(j+1, nextValue);
		     ps.setString(j+1+dataTypes.length, nextValue);
		 } 
	 } /* switch */ 

         sb.append(nextValue+","); 
       }
    }

    newValuesArrayList.add(newValues);
    ps.addBatch();
    sb.append("\n"); 


  }
  sb.append("Executing\n");
  String traceFile = null; 
  if (calculateBytesWritten) { 
    traceFile = "/tmp/JDPSBatchCompress.write."+collection_+".trace"; 
    File checkFile = new File(traceFile); 
    if (checkFile.exists()) {
      checkFile.delete(); 
    }
    try { 
    Trace.setFileName(traceFile);
    } catch (Exception e) { } 
    Trace.setTraceDatastreamOn(true);
    Trace.setTraceOn(true); 
  }

  long startTime = System.currentTimeMillis(); 
  ps.executeBatch();
  long finishTime = System.currentTimeMillis();
  if (traceFile != null) {
      bytesWritten = countSentBytes(traceFile);
    
      Trace.setTraceOn(false); 
      Trace.setTraceDatastreamOn(false);
      try { 
      Trace.setFileName(null);
      } catch (Exception e) { }
      File checkFile = new File(traceFile); 
      if (checkFile.exists()) {
	  checkFile.delete(); 
      }

  
  }
  executeBatchTime = (finishTime - startTime); 


  Object[] newValuesArray = newValuesArrayList.toArray();
  String[][] remainingValues = new String[currentValues.length + newValuesArray.length][];

  for (int i = 0; i < currentValues.length; i++) { 
    remainingValues[i] = currentValues[i]; 
  }
  for (int i = 0; i < newValuesArray.length; i++) {
    remainingValues[i+currentValues.length] = (String[]) newValuesArray[i]; 
  }
  return remainingValues; 

}







/* Returns an indication of which values in the row should be null */ 
  boolean[] getNullInfo(int row, int typeCount) {
    boolean[] setNull = new boolean[typeCount];
    for (int i = 0; i < typeCount; i++) {
      if (useNulls) {
        if ((row & 0x1) == 0) {
          setNull[i] = true;
        } else {
          setNull[i] = false;
        }
        row = row >> 1;
      }
    }
    return setNull;
  }



private void dropTable(String tablename) throws SQLException {
  String fullTableName = collection_+"."+tablename; 
  Statement stmt;
  if (useCompressConnection) { 
    stmt = connection_.createStatement();
  } else { 
    stmt = connectionNoCompress_.createStatement();
  }
  String sql = "DROP TABLE "+fullTableName; 
  stmt.executeUpdate(sql); 
  stmt.close(); 
}

/**
 * Create a table for used by testCompression
 * @param tablename
 * @param dataTypes
 * @param sb
 * @throws SQLException 
 */
void createTable(String tablename, int[] dataTypes, StringBuffer sb) throws SQLException {
  String sql; 
  String fullTableName = collection_+"."+tablename; 

  Statement stmt;
  if (useCompressConnection) { 
    stmt = connection_.createStatement();
  } else {
    stmt = connectionNoCompress_.createStatement();
  }
  sql = "DROP TABLE "+fullTableName; 
  try {
    stmt.executeUpdate(sql); 
  } catch (Exception e) { 
    sb.append("Warning:  drop failure -- "+sql+"\n");
    sb.append(e.toString()+"\n");
  }

  StringBuffer sqlBuffer = new StringBuffer(); 
  sqlBuffer.append("CREATE TABLE "+fullTableName+" ("); 
  for (int i = 0; i < dataTypes.length;i++) {
    if (i > 0) sqlBuffer.append(","); 
    int columnType = dataTypes[i]; 
    sqlBuffer.append(" col"+i+" ");
    sqlBuffer.append(createSyntax[columnType]); 
    
  }
  sqlBuffer.append(")"); 
  sql = sqlBuffer.toString(); 
  sb.append("Executing "+sql+"\n");
  stmt.executeUpdate(sql); 
  stmt.close(); 
}


/**
 * generic method to test insert compression using various types
 * uses the existing connection that is already established. 
 */ 
public void testInsertCompression(String tablename, int[] dataTypes, int rows) {
  testInsertCompression(tablename, dataTypes, rows, VERIFY_NONE); 
}
  public void testInsertCompression(String tablename, int[] dataTypes, int rows, int verifyCompression) {
    StringBuffer sb = new StringBuffer();
    String traceFile = null;

    // If the release does not support compression, still run
    // test, but do not verify compression
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	verifyCompression = VERIFY_NONE; 
    }


    try {
      if ((verifyCompression != VERIFY_NONE) && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {


        traceFile = "/tmp/JDPSBatchCompress."+collection_+".trace"; 
        File checkFile = new File(traceFile); 
        if (checkFile.exists()) {
          checkFile.delete(); 
        }
        try { 
        Trace.setFileName(traceFile);
        } catch (Exception e) { } 
        Trace.setTraceDatastreamOn(true);
        Trace.setTraceOn(true); 
      }
      
      createTable(tablename, dataTypes, sb);
      resetGeneratedValues(sb); 
      String[][] expectedResults = batchInsert(tablename, dataTypes, rows, sb);
      boolean passed = checkRows(tablename, expectedResults, dataTypes, sb);
      dropTable(tablename);
      
      if (traceFile != null)  {
        boolean compressionUsed = verifyCompression(traceFile, sb);
        if (verifyCompression == VERIFY_COMPRESSION) {
          if (!compressionUsed) {
            sb.append("ERROR:  compression not used, but should have been\n"); 
            passed = false;
          }
        } else if (verifyCompression == VERIFY_NO_COMPRESSION) {
          if (compressionUsed) { 
            sb.append("ERROR:  compression used, but should not have been\n"); 
            passed = false; 
          }
        }
	File checkFile = new File(traceFile); 
	if (checkFile.exists()) {
	    checkFile.delete(); 
	}

      }
      
      
      assertCondition(passed, sb);

    } catch (Exception e) {
      if (useCompressConnection) { 
        failed(connection_, e, sb.toString());
      } else { 
        failed(connectionNoCompress_, e, sb.toString());
      }
    } finally { 
      if (traceFile != null) {
        Trace.setTraceOn(false); 
        Trace.setTraceDatastreamOn(false);
        try { 
        Trace.setFileName(null);
        } catch (Exception e) { } 
      }
    }
  }


  /**
   * generic method to test insert compression using various types
   * uses the existing connection that is already established. 
   */ 
  public void testUpdateCompression(String tablename, int[] dataTypes, int rows) {
    testUpdateCompression(tablename, dataTypes, rows, VERIFY_NONE); 
  }
    public void testUpdateCompression(String tablename, int[] dataTypes, int rows, int verifyCompression) {
      StringBuffer sb = new StringBuffer();
      String traceFile = null; 
      try {

	  if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	      verifyCompression = VERIFY_NONE; 
	  }

        createTable(tablename, dataTypes, sb);
        resetGeneratedValues(sb); 
        String[][] expectedResults = batchInsert(tablename, dataTypes, rows, sb);

        
        if ((verifyCompression != VERIFY_NONE) && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          traceFile = "/tmp/JDPSBatchCompress."+collection_+".trace"; 
          File checkFile = new File(traceFile); 
          if (checkFile.exists()) {
            checkFile.delete(); 
          }
          try { 
          Trace.setFileName(traceFile);
          } catch (Exception e) { } 
          Trace.setTraceDatastreamOn(true);
          Trace.setTraceOn(true); 
        }

        expectedResults = batchUpdate(tablename, dataTypes, rows, sb, expectedResults); 

        boolean passed = true; 
        if (traceFile != null)  {
          boolean compressionUsed = verifyCompression(traceFile, sb);
          if (verifyCompression == VERIFY_COMPRESSION) {
            if (!compressionUsed) {
              sb.append("ERROR:  compression not used, but should have been\n"); 
              passed = false;
            }
          } else if (verifyCompression == VERIFY_NO_COMPRESSION) {
            if (compressionUsed) { 
              sb.append("ERROR:  compression used, but should not have been\n"); 
              passed = false; 
            }
          }
	  File checkFile = new File(traceFile); 
	  if (checkFile.exists()) {
	      checkFile.delete(); 
	  }

        }

        
        if (!checkRows(tablename, expectedResults, dataTypes, sb)) {
          passed = false; 
        }
        dropTable(tablename);
        
        
        
        assertCondition(passed, sb);

      } catch (Exception e) {
        if (useCompressConnection) { 
          failed(connection_, e, sb.toString());
        } else { 
          failed(connectionNoCompress_, e, sb.toString());
        }
      } finally { 
        if (traceFile != null) {
          Trace.setTraceOn(false); 
          Trace.setTraceDatastreamOn(false);
          try { 
          Trace.setFileName(null);
          } catch (Exception e) { } 
        }
      }
    }

  public void testDeleteCompression(String tablename, int[] dataTypes, int rows) {
    testDeleteCompression(tablename, dataTypes, rows, VERIFY_NONE); 
  }
    public void testDeleteCompression(String tablename, int[] dataTypes, int rows, int verifyCompression) {
      StringBuffer sb = new StringBuffer();
      String traceFile = null;
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	verifyCompression = VERIFY_NONE; 
    }

      try {
        
        createTable(tablename, dataTypes, sb);
        useNulls = false; 
        resetGeneratedValues(sb); 
        String[][] expectedResults = batchInsert(tablename, dataTypes, rows, sb);
        useNulls = true; 

        
        if ((verifyCompression != VERIFY_NONE) && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          traceFile = "/tmp/JDPSBatchCompress."+collection_+".trace"; 
          File checkFile = new File(traceFile); 
          if (checkFile.exists()) {
            checkFile.delete(); 
          }
          try { 
          Trace.setFileName(traceFile);
          } catch (Exception e) { } 
          Trace.setTraceDatastreamOn(true);
          Trace.setTraceOn(true); 
        }

        expectedResults = batchDelete(tablename, dataTypes, rows, sb, expectedResults); 

        boolean passed = true; 
        if (traceFile != null)  {
          boolean compressionUsed = verifyCompression(traceFile, sb);
          if (verifyCompression == VERIFY_COMPRESSION) {
            if (!compressionUsed) {
              sb.append("ERROR:  compression not used, but should have been\n"); 
              passed = false;
            }
          } else if (verifyCompression == VERIFY_NO_COMPRESSION) {
            if (compressionUsed) { 
              sb.append("ERROR:  compression used, but should not have been\n"); 
              passed = false; 
            }
          }
	  File checkFile = new File(traceFile); 
	  if (checkFile.exists()) {
	      checkFile.delete(); 
	  }

        }

        
        if (!checkRows(tablename, expectedResults, dataTypes, sb)) {
          passed = false; 
        }
        dropTable(tablename);
        
        
        
        assertCondition(passed, sb);

      } catch (Exception e) {
        if (useCompressConnection) { 
          failed(connection_, e, sb.toString());
        } else { 
          failed(connectionNoCompress_, e, sb.toString());
        }
      } finally { 
        if (traceFile != null) {
          Trace.setTraceOn(false); 
          Trace.setTraceDatastreamOn(false);
          try { 
          Trace.setFileName(null);
          } catch (Exception e) { } 
        }
      }
    }


    public void testMergeCompression(String tablename, int[] dataTypes, int rows) {
    testMergeCompression(tablename, dataTypes, rows, VERIFY_NONE); 
  }

    public void testMergeCompression(String tablename, int[] dataTypes, int rows, int verifyCompression ) {
	testMergeCompression(tablename, dataTypes, rows, verifyCompression, ""); 
    }

    public boolean testMergeCompression(String tablename, int[] dataTypes, int rows, int verifyCompression, String comment ) {
      StringBuffer sb = new StringBuffer();
      String traceFile = null;
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	notApplicable("Merge testcase for V7R1 and later");
	return true; 
    }


      try {
  
        createTable(tablename, dataTypes, sb);
        resetGeneratedValues(sb); 
        String[][] expectedResults = batchInsert(tablename, dataTypes, rows, sb);

        
        if ((verifyCompression != VERIFY_NONE) && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          traceFile = "/tmp/JDPSBatchCompress."+collection_+".trace"; 
          File checkFile = new File(traceFile); 
          if (checkFile.exists()) {
            checkFile.delete(); 
          }
          try { 
          Trace.setFileName(traceFile);
          } catch (Exception e) { } 
          Trace.setTraceDatastreamOn(true);
          Trace.setTraceOn(true); 
        }

        expectedResults = batchMerge(tablename, dataTypes, rows, sb, expectedResults); 

        boolean passed = true; 
        if (traceFile != null)  {
          boolean compressionUsed = verifyCompression(traceFile, sb);
          if (verifyCompression == VERIFY_COMPRESSION) {
            if (!compressionUsed) {
              sb.append("ERROR:  compression not used, but should have been\n"); 
              passed = false;
            }
          } else if (verifyCompression == VERIFY_NO_COMPRESSION) {
            if (compressionUsed) { 
              sb.append("ERROR:  compression used, but should not have been\n"); 
              passed = false; 
            }
          }
	  File checkFile = new File(traceFile); 
	  if (checkFile.exists()) {
	      checkFile.delete(); 
	  }

        }

        
        if (!checkRows(tablename, expectedResults, dataTypes, sb)) {
          passed = false; 
        }
        dropTable(tablename);
        
        
        
        assertCondition(passed, comment + sb);
	return passed; 
      } catch (Exception e) {
        if (useCompressConnection) { 
          failed(connection_, e, comment + sb.toString());
        } else { 
          failed(connectionNoCompress_, e, comment + sb.toString());
        }
	return false; 
      } finally { 
        if (traceFile != null) {
          Trace.setTraceOn(false); 
          Trace.setTraceDatastreamOn(false);
          try { 
          Trace.setFileName(null);
          } catch (Exception e) { } 
        }
      }
    }




  private boolean verifyCompression(String traceFile, StringBuffer sb) throws IOException {
    boolean compressionUsed = false; 
    boolean inSent = false;
    int  marker381Ffound= 0; 
    int sentLine = 0; 
    // Read the file and look for the trace information 
    BufferedReader reader = new BufferedReader(new FileReader(traceFile));   
    String line = reader.readLine(); 
    while (line != null) {
	if (line.indexOf("Data stream sent") > 0) {
	    inSent = true;
	    marker381Ffound= 0; 
	    sentLine = 0;
      } else if (line.indexOf("Data stream data received") > 0) {
        inSent = false;
        marker381Ffound = 0;
      } else if (line.indexOf("Data stream before read") > 0) {
        inSent = false;
        marker381Ffound = 0;
      } else {
        if (inSent) {
          // System.out.print("SentLine: " + sentLine + " : ");
          sb.append("sentLine: " + sentLine + ":");
          int markerIndex = line.indexOf("38 1F", 39);
          if (markerIndex == 39) {
            marker381Ffound = 32;
          } else {
            // Check for data without RLL compression set
            markerIndex = line.indexOf("38 1F", 9);
            if (markerIndex == 9) {
              marker381Ffound = 2;
            } else {
              markerIndex = line.indexOf("38 1F", 1);
              if (markerIndex > 0) {
                markerIndex = -1;
              }
            }
          }
          if (markerIndex > 0) {
            // System.out.print("381F at " + markerIndex + " : ");
            sb.append("381F at " + markerIndex + " : ");
          } else {
            if (marker381Ffound > 0) {
              int marker80Index = line.indexOf(" 80 ", marker381Ffound);
              if (marker80Index == marker381Ffound) {
                // System.out.print("80 at " + marker80Index);
                sb.append("80 at " + marker80Index);
                compressionUsed = true;
              }
              marker381Ffound = 0;
            } else {
            }

          }
          sb.append(line + "\n");
          sentLine++;
        }
      }
      // System.out.println(line);
      line = reader.readLine();
    }
    reader.close();

    return compressionUsed;
  }
  
  private long countSentBytes(String traceFile)  {
    long sentBytes = 0;
    try { 
    boolean inSent = false;
    int sentLine = 0;
    // Read the file and look for the trace information
    BufferedReader reader = new BufferedReader(new FileReader(traceFile));
    String line = reader.readLine();
    while (line != null) {
      if (line.indexOf("Data stream sent") > 0) {
        inSent = true;
        sentLine = 0;
      } else if (line.indexOf("Data stream data received") > 0) {
        inSent = false;
      } else if (line.indexOf("Data stream before read") > 0) {
        inSent = false;
      } else {
        if (inSent) {
          int lineLen = line.length(); 
          for (int i = 2; i < lineLen; i+=3) { 
            if (line.charAt(i) == ' ') {
              sentBytes++; 
            } else {
              // System.out.println("Parse error on "+line+" at index "+i); 
            }
          }
          if (debug)  { 
          System.out.println("SentLine: " + sentLine + " : "+line+" : totalBytes="+sentBytes);
          }
          sentLine++;
        }
      }
      // System.out.println(line);
      line = reader.readLine();
    }
    reader.close();
    } catch (Exception e) { 
      e.printStackTrace(); 
    }
    // System.out.println("Count done:  returning "+sentBytes); 
    return sentBytes;
  }

  
  public void testInsertCompressionTimePerformance(String testInfo, String tablename, int[] dataTypes, int rows, double expectedRatio, int runMinutes) {

   if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 

       if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	   StringBuffer sb = new StringBuffer();
	   useNulls = false; 
	   try {
	       int TIME_SAMPLES = 10; 
	       int runCount= 0; 
	       long totalCompressedTime = 0; 
	       long totalUncompressedTime = 0; 
	       long[] compressedTime = new long[TIME_SAMPLES]; 
	       long[] uncompressedTime = new long[TIME_SAMPLES];
	       long testStartTime = System.currentTimeMillis();
	       long testFinishTime = testStartTime +  runMinutes * 60000;
	       boolean passed = true; 
	       String[][] expectedResults;
	       calculateBytesWritten = false; 
	       boolean goalMet = false; 
	       while ((!goalMet) && passed && System.currentTimeMillis() < testFinishTime ) { 
		   useCompressConnection = true; 
		   createTable(tablename, dataTypes, sb);
		   resetGeneratedValues(sb); 
		   expectedResults = batchInsert(tablename, dataTypes, rows, sb);
		   compressedTime[runCount % compressedTime.length] = executeBatchTime; 
		   passed = checkRows(tablename, expectedResults, dataTypes, sb);
		   dropTable(tablename);

		   useCompressConnection = false; 
		   createTable(tablename, dataTypes, sb);
		   resetGeneratedValues(sb); 
		   expectedResults = batchInsert(tablename, dataTypes, rows, sb);
		   uncompressedTime[runCount % uncompressedTime.length] = executeBatchTime;
		   passed = checkRows(tablename, expectedResults, dataTypes, sb);
		   dropTable(tablename);

		   runCount++;
		   if (runCount > compressedTime.length) {
		       totalCompressedTime = 0L; 
		       totalUncompressedTime = 0; 
		       for (int i = 0; i < compressedTime.length; i++) {
			   totalCompressedTime += compressedTime[i]; 
			   totalUncompressedTime += uncompressedTime[i]; 
		       }
		       double ratio = (((double)totalCompressedTime)/((double)totalUncompressedTime));
		       if (ratio <= expectedRatio) {
			   goalMet = true; 
		       }
		       if (runCount % compressedTime.length == 0) { 
			   System.out.println("Count="+runCount+" ratio="+ratio+" expected="+expectedRatio+
					      " compressedTime="+totalCompressedTime+" uncompressedTime="+totalUncompressedTime); 
		       }
		   }
	       } /* while not goal met */ 
	       System.out.println("Test info                 ="+testInfo); 
	       System.out.println("Run milliseconds          ="+(System.currentTimeMillis() - testStartTime));
	       System.out.println("Run count                 ="+runCount); 
	       System.out.println("Compressed milliseconds   ="+totalCompressedTime); 
	       System.out.println("Uncompressed milliseconds ="+totalUncompressedTime);
	       sb.append("Run count                 ="+runCount+"\n"); 
	       sb.append("Compressed milliseconds   ="+totalCompressedTime+"\n"); 
	       sb.append("Uncompressed milliseconds ="+totalUncompressedTime+"\n");
	       double ratio = (((double)totalCompressedTime)/((double)totalUncompressedTime));
	       System.out.println("Ratio                     ="+ ratio);
	       System.out.println("Expected ratio            ="+ expectedRatio);
	       if (ratio > expectedRatio) { 
		   sb.append("FAILED:  expectedRatio="+expectedRatio+" ratio="+ratio+"\n");
		   passed = false; 
	       }
	       assertCondition(passed, sb);

	   } catch (Exception e) {
	       if (useCompressConnection) { 
		   failed(connection_, e, sb.toString());
	       } else { 
		   failed(connectionNoCompress_, e, sb.toString());
	       }
	   } finally { 
	   }
	   useCompressConnection = true; 
	   useNulls = true;
       } else {
	   notApplicable("V7R1 or later release"); 
       } 
   } else { 
     notApplicable("Toolbox only test"); 
   }
  }



  public void testInsertCompressionBytePerformance(String testInfo, String tablename, int[] dataTypes, int rows, double expectedRatio) {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
	if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	    // Make sure everything is release, so the the
            // garbage collector does not generate
            // unnecessary traffice
	    System.gc(); 
	    StringBuffer sb = new StringBuffer();
	    useNulls = false; 
	    calculateBytesWritten= true; 
	    try {
		int runCount= 0; 
		long compressedBytes = 0; 
		long uncompressedBytes = 0;
		boolean passed = true; 
		String[][] expectedResults;
		while (passed && runCount < 10 ) { 
		    useCompressConnection = true; 
		    createTable(tablename, dataTypes, sb);
		    resetGeneratedValues(sb);
		    bytesWritten = 0; 
		    expectedResults = batchInsert(tablename, dataTypes, rows, sb);
		    compressedBytes+= bytesWritten; 
		    passed = checkRows(tablename, expectedResults, dataTypes, sb);
		    dropTable(tablename);

		    useCompressConnection = false; 
		    createTable(tablename, dataTypes, sb);
		    resetGeneratedValues(sb);
		    bytesWritten = 0; 
		    expectedResults = batchInsert(tablename, dataTypes, rows, sb);
		    uncompressedBytes += bytesWritten;
		    passed = checkRows(tablename, expectedResults, dataTypes, sb);
		    dropTable(tablename);


		    runCount++; 
		}
		System.out.println("Test name: "+testInfo); 
		System.out.println("Run count                 ="+runCount); 
		System.out.println("Compressed bytes   ="+compressedBytes); 
		System.out.println("Uncompressed bytes ="+uncompressedBytes);
		double ratio = (((double)compressedBytes)/((double)uncompressedBytes));
		System.out.println("Ratio              ="+ ratio);
		System.out.println("Expected ratio     ="+ expectedRatio);

		if (ratio > expectedRatio) {
		    sb.append("Expected ratio="+expectedRatio+" but got "+ratio+"\n"); 
		    passed = false; 

		}
		assertCondition(passed, sb);

	    } catch (Exception e) {
		if (useCompressConnection) { 
		    failed(connection_, e, sb.toString());
		} else { 
		    failed(connectionNoCompress_, e, sb.toString());
		}
	    } finally { 
	    }
	    calculateBytesWritten= false; 
	    useCompressConnection = true; 
	    useNulls = true;
	} else {
	    notApplicable("V7R1 or later release"); 
	}
    } else { 
      notApplicable("Toolbox only test"); 
    }

    }



  
  
  
  
    public boolean testInsertCompressionCase(String tablename, int[] dataTypes, int rows, StringBuffer sb) {
    try {
	    System.gc(); 
      sb.append("Testing "+tablename+" ");
      sb.append(displayDataTypes(dataTypes)); 
      int rowSize = rowSize(dataTypes); 
      if (rowTooBig(dataTypes)) {
        System.out.println("Warning:  Row for "+tablename+" too big ("+rowSize+"): "+displayDataTypes(dataTypes)); 
        return true; 
      }
      try { 
         createTable(tablename, dataTypes, sb);
      } catch (Exception e) { 
        System.out.println("create table failed:  row size = "+rowSize); 
        sb.append("FAILED:  Exception caught on createTable:  row size = "+rowSize); 
        printStackTraceToStringBuffer(e, sb); 
        return false; 
      }
      resetGeneratedValues(sb); 
      String[][] expectedResults = batchInsert(tablename, dataTypes, rows, sb);
      boolean passed = checkRows(tablename, expectedResults, dataTypes, sb);
      dropTable(tablename);
      return passed; 

    } catch (Exception e) {
      sb.append("FAILED:  Exception caught"); 
      printStackTraceToStringBuffer(e, sb); 
      return false; 
    }
  }

  private static int rowSize(int[] dataTypes) {
    int rowSize = 0;
    
    // Account for indicators (1 byte for every 8 
    rowSize = (dataTypes.length + 7) / 8; 
    for (int i = 0; i < dataTypes.length; i++) {
      int dataType = dataTypes[i];
      int thisTypeSize = typeSize[dataType];
      rowSize += thisTypeSize;
      // Handle lob types (only type with size LOB_TYPE_SIZE ) 
      if (thisTypeSize == LOB_TYPE_SIZE) {
        // Round up to next 16 byte boundary
        int extraByte = rowSize % 16;
        if (extraByte > 0) {
          rowSize += (16 - extraByte);
        }
      }
    }

    
    return rowSize;
  }

  private static boolean rowTooBig(int[] dataTypes) {
      int rowSize = rowSize(dataTypes); 
      if (rowSize >= 32740) { 
        return true; 
      } else { 
        return false ;
      }
    }


    public boolean testUpdateCompressionCase(String tablename, int[] dataTypes, int rows, StringBuffer sb) {
    try {
      sb.append("Testing ");
      int rowSize = rowSize(dataTypes); 
      if (rowTooBig(dataTypes)) {
        System.out.println("Warning:  Row for "+tablename+" too big ("+rowSize+"): "+displayDataTypes(dataTypes)); 
        return true; 
      }

      sb.append(displayDataTypes(dataTypes));
      try { 
      createTable(tablename, dataTypes, sb);
      } catch (Exception e) { 
        System.out.println("create table failed:  row size = "+rowSize); 
        sb.append("FAILED:  Exception caught on createTable:  row size = "+rowSize); 
        printStackTraceToStringBuffer(e, sb); 
        return false; 
      }

      resetGeneratedValues(sb); 
      String[][] expectedResults = batchInsert(tablename, dataTypes, rows, sb);
      expectedResults = batchUpdate(tablename, dataTypes, rows, sb, expectedResults);
      boolean passed = checkRows(tablename, expectedResults, dataTypes, sb);
      dropTable(tablename);
      return passed; 

    } catch (Exception e) {
      sb.append("FAILED:  Exception caught"); 
      printStackTraceToStringBuffer(e, sb); 
      return false; 
    }
  }

    public boolean testDeleteCompressionCase(String tablename, int[] dataTypes, int rows, StringBuffer sb) {
    try {
      sb.append("Testing "); 
      sb.append(displayDataTypes(dataTypes));

      int rowSize = rowSize(dataTypes); 
      if (rowTooBig(dataTypes)) {
	  System.out.println("Warning:  Row for "+tablename+" too big ("+rowSize+"): "+displayDataTypes(dataTypes)); 
	  return true; 
      }
      try { 
        createTable(tablename, dataTypes, sb);
      } catch (Exception e) { 
        System.out.println("create table failed:  row size = "+rowSize); 
        sb.append("FAILED:  Exception caught on createTable:  row size = "+rowSize); 
        printStackTraceToStringBuffer(e, sb); 
        return false; 
      }

      useNulls = false; 
      resetGeneratedValues(sb); 
      String[][] expectedResults = batchInsert(tablename, dataTypes, rows, sb);
      useNulls = true; 
      expectedResults = batchDelete(tablename, dataTypes, rows, sb, expectedResults);
      boolean passed = checkRows(tablename, expectedResults, dataTypes, sb);
      dropTable(tablename);
      return passed; 

    } catch (Exception e) {
      sb.append("FAILED:  Exception caught"); 
      printStackTraceToStringBuffer(e, sb); 
      return false; 
    }
  }


    public boolean testMergeCompressionCase(String tablename, int[] dataTypes, int rows, StringBuffer sb) {
    try {
	 
      sb.append("Testing "); 
      sb.append(displayDataTypes(dataTypes)); 
      int rowSize = rowSize(dataTypes); 
      if (rowTooBig(dataTypes)) {
        System.out.println("Warning:  Row for "+tablename+" too big ("+rowSize+"): "+displayDataTypes(dataTypes)); 
        return true; 
      }
      try { 
      createTable(tablename, dataTypes, sb);

      } catch (Exception e) { 
        System.out.println("create table failed:  row size = "+rowSize); 
        sb.append("FAILED:  Exception caught on createTable:  row size = "+rowSize); 
        printStackTraceToStringBuffer(e, sb); 
        return false; 
      }

      useNulls = false; 
      resetGeneratedValues(sb); 
      String[][] expectedResults = batchInsert(tablename, dataTypes, rows, sb);
      useNulls = true; 
      expectedResults = batchMerge(tablename, dataTypes, rows, sb, expectedResults);
      boolean passed = checkRows(tablename, expectedResults, dataTypes, sb);
      dropTable(tablename);
      return passed; 

    } catch (Exception e) {
      sb.append("FAILED:  Exception caught"); 
      printStackTraceToStringBuffer(e, sb); 
      return false; 
    }
  }




boolean checkRows(String tablename, String[][] expectedResults, int[] dataTypes,
    StringBuffer sb) throws SQLException {
  boolean passed = true; 
  Statement stmt;
  if (useCompressConnection) { 
  stmt = connection_.createStatement();
  } else { 
    stmt = connectionNoCompress_.createStatement();
  }
  String fullTableName = collection_+"."+tablename; 
  String sql = "select * from "+ fullTableName+" A ORDER BY RRN(A)";
  sb.append("Running query : "+sql); 
  ResultSet rs = stmt.executeQuery(sql); 
  int i = 0;
  StringBuffer thisRowBuffer = new StringBuffer(); 
  StringBuffer thisRowExpectedBuffer = new StringBuffer(); 

  for ( i = 0; i < expectedResults.length && rs.next(); i++) {
      boolean thisRowPassed = true; 
      thisRowBuffer.setLength(0);
      thisRowExpectedBuffer.setLength(0);
      String[] expectedRow = expectedResults[i]; 
      for (int j = 0; j < expectedRow.length; j++) {
        String expected;
        expected = expectedRow[j];
        thisRowExpectedBuffer.append(expected+","); 
        
        String result;
	switch (dataTypes[j]) {
	    case TYPE_BLOB:
	    case TYPE_VARBINARY255:
	    case TYPE_VARCHARFBD255:
	    case TYPE_VARCHARHEX255:
	    case TYPE_BINARY25:
	    case TYPE_CHARFBD25:
	    case TYPE_CHARHEX25:
	    case TYPE_VARCHARFBD256 :
	    case TYPE_VARBINARY256 :
	    case TYPE_VARCHARFBD257 :
	    case TYPE_VARBINARY257 :
	    case TYPE_VARCHARFBD32700 :
	    case TYPE_VARBINARY32700 :
		{
		    byte[] stuff = rs.getBytes(j+1);
		    if (stuff != null) { 
			result = BinaryConverter.bytesToHexString(stuff);
		    } else {
			result = null; 
		    }

		}
		break;
	    default:  { 
		    result = rs.getString(j+1);
		}
	} /* switch */ 
        thisRowBuffer.append(result+","); 
        if (result == null) {
          if (expected == null) {
            /* good */ 
          } else { 
            sb.append("FAILED: For "+i+"/"+j+" expected null but got "+result+"\n");
            passed = false;
            thisRowPassed = false; 
          }
        } else {
          if (result.equals(expected)) {
            /* good */ 
          } else { 
            sb.append("FAILED: For "+i+"/"+j+" expected "+expected+" but got "+result+"\n");
            passed = false; 
            thisRowPassed = false; 
          }
        } /* result check */
      } /* for j */ 
      if (!thisRowPassed) {
        sb.append("Row "+i+"  =  "+thisRowBuffer.toString()+"\n"); 
        sb.append("Row "+i+" exp "+thisRowExpectedBuffer.toString()+"\n"); 
      }
  }
  if (i != expectedResults.length ) {
    sb.append("FAILED:  Got "+i+" rows, expected "+expectedResults.length+"\n" );
    passed = false; 
  }
  rs.close(); 
  stmt.close(); 
  return passed; 
}


/**
 * Testing only VARCHAR30 types  
**/
  public void Var001() {
    int[] dataTypes = { TYPE_VARCHAR30, TYPE_VARCHAR30, TYPE_VARCHAR30,
        TYPE_VARCHAR30, TYPE_VARCHAR30, };
    testInsertCompression("JDPSBC001", dataTypes, 256, VERIFY_COMPRESSION );
  }

  /**
   * Testing only LONGVARCHAR30 types  
  **/
    public void Var002() {
      int[] dataTypes = { TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR,
          TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, };
      testInsertCompression("JDPSBC002", dataTypes, 256, VERIFY_COMPRESSION);
    }

/**
 * Testing only NVARCHAR30 types  
**/
  public void Var003() {
    int[] dataTypes = { TYPE_NVARCHAR30, TYPE_NVARCHAR30, TYPE_NVARCHAR30,
        TYPE_NVARCHAR30, TYPE_NVARCHAR30, };
    testInsertCompression("JDPSBC003", dataTypes, 256, VERIFY_COMPRESSION);
  }


/**
 * Testing only VARGRAPHIC30 types  
**/
  public void Var004() {
    int[] dataTypes = { TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30,
        TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30, };
    testInsertCompression("JDPSBC004", dataTypes, 256, VERIFY_COMPRESSION);
  }


  /**
   * Testing only LONGVARGRAPHIC types
   **/
  public void Var005() {
    int[] dataTypes = { TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC,
        TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC, };
    testInsertCompression("JDPSBC005", dataTypes, 256, VERIFY_COMPRESSION);
  }

  /**
   * Testing INTEGER, INTEGER, INTEGER, INTEGER
   **/
  public void Var006() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_INTEGER, TYPE_INTEGER, TYPE_INTEGER };
    testInsertCompression("JDPSBC006", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

  /**
   * Testing VARCHAR30, INTEGER, INTEGER, INTEGER
   **/
  public void Var007() {
    int[] dataTypes = { TYPE_VARCHAR30, TYPE_INTEGER, TYPE_INTEGER,
        TYPE_INTEGER, };
    testInsertCompression("JDPSBC007", dataTypes, 256, VERIFY_COMPRESSION);
  }

  /**
   * Testing INTEGER, VARCHAR30,INTEGER, INTEGER
   **/
  public void Var008() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_VARCHAR30, TYPE_INTEGER,
        TYPE_INTEGER, };
    testInsertCompression("JDPSBC008", dataTypes, 256, VERIFY_COMPRESSION);
  }

  /**
   * Testing INTEGER, INTEGER, VARCHAR30,INTEGER
   **/
  public void Var009() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_INTEGER, TYPE_VARCHAR30,
        TYPE_INTEGER, };
    testInsertCompression("JDPSBC009", dataTypes, 256, VERIFY_COMPRESSION);
  }

  /**
   * Testing INTEGER, INTEGER, INTEGER, VARCHAR30
   **/
  public void Var010() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_INTEGER, TYPE_INTEGER,
        TYPE_VARCHAR30, };
    testInsertCompression("JDPSBC001", dataTypes, 256, VERIFY_COMPRESSION);
  }

  
/**
 * Testing TYPE_SMALLINT,TYPE_SMALLINT,TYPE_SMALLINT,TYPE_SMALLINT
 **/
  public void Var011() {
      int[] dataTypes = {TYPE_SMALLINT, TYPE_SMALLINT, TYPE_SMALLINT, TYPE_SMALLINT,};
      testInsertCompression("JDPSBC011", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_BIGINT,TYPE_BIGINT,TYPE_BIGINT,TYPE_BIGINT
 **/
  public void Var012() {
      int[] dataTypes = {TYPE_BIGINT, TYPE_BIGINT, TYPE_BIGINT, TYPE_BIGINT,};
      testInsertCompression("JDPSBC012", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_REAL,TYPE_REAL,TYPE_REAL,TYPE_REAL
 **/
  public void Var013() {
      int[] dataTypes = {TYPE_REAL, TYPE_REAL, TYPE_REAL, TYPE_REAL,};
      testInsertCompression("JDPSBC013", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_FLOAT,TYPE_FLOAT,TYPE_FLOAT,TYPE_FLOAT
 **/
  public void Var014() {
      int[] dataTypes = {TYPE_FLOAT, TYPE_FLOAT, TYPE_FLOAT, TYPE_FLOAT,};
      testInsertCompression("JDPSBC014", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_DOUBLE,TYPE_DOUBLE,TYPE_DOUBLE,TYPE_DOUBLE
 **/
  public void Var015() {
      int[] dataTypes = {TYPE_DOUBLE, TYPE_DOUBLE, TYPE_DOUBLE, TYPE_DOUBLE,};
      testInsertCompression("JDPSBC015", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_DECIMAL102,TYPE_DECIMAL102,TYPE_DECIMAL102,TYPE_DECIMAL102
 **/
  public void Var016() {
      int[] dataTypes = {TYPE_DECIMAL102, TYPE_DECIMAL102, TYPE_DECIMAL102, TYPE_DECIMAL102,};
      testInsertCompression("JDPSBC016", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_DECIMAL152,TYPE_DECIMAL152,TYPE_DECIMAL152,TYPE_DECIMAL152
 **/
  public void Var017() {
      int[] dataTypes = {TYPE_DECIMAL152, TYPE_DECIMAL152, TYPE_DECIMAL152, TYPE_DECIMAL152,};
      testInsertCompression("JDPSBC017", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_DECIMAL105,TYPE_DECIMAL105,TYPE_DECIMAL105,TYPE_DECIMAL105
 **/
  public void Var018() {
      int[] dataTypes = {TYPE_DECIMAL105, TYPE_DECIMAL105, TYPE_DECIMAL105, TYPE_DECIMAL105,};
      testInsertCompression("JDPSBC018", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_DECIMAL155,TYPE_DECIMAL155,TYPE_DECIMAL155,TYPE_DECIMAL155
 **/
  public void Var019() {
      int[] dataTypes = {TYPE_DECIMAL155, TYPE_DECIMAL155, TYPE_DECIMAL155, TYPE_DECIMAL155,};
      testInsertCompression("JDPSBC019", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_CHAR30,TYPE_CHAR30,TYPE_CHAR30,TYPE_CHAR30
 **/
  public void Var020() {
      int[] dataTypes = {TYPE_CHAR30, TYPE_CHAR30, TYPE_CHAR30, TYPE_CHAR30,};
      testInsertCompression("JDPSBC020", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_NCHAR,TYPE_NCHAR,TYPE_NCHAR,TYPE_NCHAR
 **/
  public void Var021() {
      int[] dataTypes = {TYPE_NCHAR, TYPE_NCHAR, TYPE_NCHAR, TYPE_NCHAR,};
      testInsertCompression("JDPSBC021", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_GRAPHIC,TYPE_GRAPHIC,TYPE_GRAPHIC,TYPE_GRAPHIC
 **/
  public void Var022() {
      int[] dataTypes = {TYPE_GRAPHIC, TYPE_GRAPHIC, TYPE_GRAPHIC, TYPE_GRAPHIC,};
      testInsertCompression("JDPSBC022", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_DATE,TYPE_DATE,TYPE_DATE,TYPE_DATE
 **/
  public void Var023() {
      int[] dataTypes = {TYPE_DATE, TYPE_DATE, TYPE_DATE, TYPE_DATE,};
      testInsertCompression("JDPSBC023", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_TIME,TYPE_TIME,TYPE_TIME,TYPE_TIME
 **/
  public void Var024() {
      int[] dataTypes = {TYPE_TIME, TYPE_TIME, TYPE_TIME, TYPE_TIME,};
      testInsertCompression("JDPSBC024", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_TIMESTAMP,TYPE_TIMESTAMP,TYPE_TIMESTAMP,TYPE_TIMESTAMP
 **/
  public void Var025() {
      int[] dataTypes = {TYPE_TIMESTAMP, TYPE_TIMESTAMP, TYPE_TIMESTAMP, TYPE_TIMESTAMP,};
      testInsertCompression("JDPSBC025", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_CLOB,TYPE_CLOB,TYPE_CLOB,TYPE_CLOB
 **/
  public void Var026() {
      int[] dataTypes = {TYPE_CLOB, TYPE_CLOB, TYPE_CLOB, TYPE_CLOB,};
      testInsertCompression("JDPSBC026", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_DBCLOB,TYPE_DBCLOB,TYPE_DBCLOB,TYPE_DBCLOB
 **/
  public void Var027() {
      int[] dataTypes = {TYPE_DBCLOB, TYPE_DBCLOB, TYPE_DBCLOB, TYPE_DBCLOB,};
      testInsertCompression("JDPSBC027", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_BLOB,TYPE_BLOB,TYPE_BLOB,TYPE_BLOB
 **/
  public void Var028() {
      int[] dataTypes = {TYPE_BLOB, TYPE_BLOB, TYPE_BLOB, TYPE_BLOB,};
      testInsertCompression("JDPSBC028", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_NUMERIC102,TYPE_NUMERIC102,TYPE_NUMERIC102,TYPE_NUMERIC102
 **/
  public void Var029() {
      int[] dataTypes = {TYPE_NUMERIC102, TYPE_NUMERIC102, TYPE_NUMERIC102, TYPE_NUMERIC102,};
      testInsertCompression("JDPSBC029", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_NUMERIC152,TYPE_NUMERIC152,TYPE_NUMERIC152,TYPE_NUMERIC152
 **/
  public void Var030() {
      int[] dataTypes = {TYPE_NUMERIC152, TYPE_NUMERIC152, TYPE_NUMERIC152, TYPE_NUMERIC152,};
      testInsertCompression("JDPSBC030", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_NUMERIC105,TYPE_NUMERIC105,TYPE_NUMERIC105,TYPE_NUMERIC105
 **/
  public void Var031() {
      int[] dataTypes = {TYPE_NUMERIC105, TYPE_NUMERIC105, TYPE_NUMERIC105, TYPE_NUMERIC105,};
      testInsertCompression("JDPSBC031", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

/**
 * Testing TYPE_NUMERIC155,TYPE_NUMERIC155,TYPE_NUMERIC155,TYPE_NUMERIC155
 **/
  public void Var032() {
      int[] dataTypes = {TYPE_NUMERIC155, TYPE_NUMERIC155, TYPE_NUMERIC155, TYPE_NUMERIC155,};
      testInsertCompression("JDPSBC032", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }

 
  
  
  /*** 
   * Randomly test combinations for 1 minutes
   */
  public void Var033() {
      if (toolboxNative) {
	  notApplicable("Toolbox native fails with Out of Memory");
	  return; 
      } 

    System.out.println("Var033 is testing combinations for "+RUN_MINUTES+" minutes"); 
    Random random = new Random(); 
    long endTime = System.currentTimeMillis()+ RUN_MINUTES * 60000; 
    boolean passed = true; 
    StringBuffer sb = new StringBuffer(); 
    int tableCount = 1; 
    while (passed && System.currentTimeMillis() < endTime) {
       // Pick number of columns 2 - 10
       int columns = 2 + random.nextInt(8); 
       int[] dataTypes = new int[columns]; 
       for (int i = 0; i < columns; i++) {
         dataTypes[i] = random.nextInt(TYPE_COUNT); 
         while (dataTypes[i] == TYPE_INTKEY) { 
           dataTypes[i] = random.nextInt(TYPE_COUNT);
         }

       }
       int rowsCount = 100 + random.nextInt(1000);
       System.out.println("Test "+tableCount+": Testing "+rowsCount+" rows of : "+displayDataTypes(dataTypes));
       passed = testInsertCompressionCase("JDPSBC"+tableCount, dataTypes, rowsCount, sb);
       tableCount++; 
    }
    
    assertCondition(passed, sb); 
  }
  
  
  /**
   * Testing only VARCHAR30 types  
  **/
    public void Var034() {
	notApplicable("Performance test, see JDPSBatchCompressPerformance");
/*
      int[] dataTypes = { TYPE_VARCHAR30, TYPE_VARCHAR30, TYPE_VARCHAR30,
          TYPE_VARCHAR30, TYPE_VARCHAR30, };
      testInsertCompressionTimePerformance("JDPSBC034", dataTypes, 500, 0.96 );
*/
    }

    
    /**
     * Testing only LONGVARCHAR types  
    **/
      public void Var035() {
	notApplicable("Performance test, see JDPSBatchCompressPerformance");
/*
        int[] dataTypes = { TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR,
            TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, };
        testInsertCompressionTimePerformance("JDPSBC035", dataTypes, 500, 0.60 );
*/
      }

    /**
     * Testing only VARCHAR30 types with no compression  
    **/
      public void Var036() {
        int[] dataTypes = { TYPE_VARCHAR30, TYPE_VARCHAR30, TYPE_VARCHAR30,
            TYPE_VARCHAR30, TYPE_VARCHAR30, };
        useCompressConnection = false; 
        testInsertCompression("JDPSBC036", dataTypes, 1024, VERIFY_NO_COMPRESSION );
        useCompressConnection = true; 
      }

      public void Var037() {
	notApplicable("Performance test, see JDPSBatchCompressPerformance");
/*
        int[] dataTypes = { TYPE_VARCHAR30, TYPE_VARCHAR30, TYPE_VARCHAR30,
            TYPE_VARCHAR30, TYPE_VARCHAR30, };
        testInsertCompressionBytePerformance("JDPSBC037", dataTypes, 500, 0.80);
*/
      }

      public void Var038() {
	notApplicable("Performance test, see JDPSBatchCompressPerformance");
/* 
        int[] dataTypes = { TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR,
            TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, };
        testInsertCompressionBytePerformance("JDPSBC038", dataTypes, 500, 0.87);
*/ 
      }

    
      /*** 
       * Randomly test combinations for 1 minutes
       */
      public void Var039() {
	if (toolboxNative) {
	    notApplicable("Toolbox native fails with Out of Memory");
	    return; 
	} 

        System.out.println("Var039 is testing combinations including compressions for "+RUN_MINUTES+" minutes"); 
        Random random = new Random(); 
        long endTime = System.currentTimeMillis()+ RUN_MINUTES * 60000; 
        boolean passed = true; 
        StringBuffer sb = new StringBuffer(); 
        int tableCount = 1;
        int[] dataTypes = null; 
        while (passed && System.currentTimeMillis() < endTime) {
          boolean picking = true; 
          while (picking) { 
           // Pick number of columns 2 - 10
           int columns = 2 + random.nextInt(8); 
           dataTypes = new int[columns]; 
           for (int i = 0; i < columns; i++) {
             dataTypes[i] = random.nextInt(TYPE_COUNT);
             while (dataTypes[i] == TYPE_INTKEY) { 
               dataTypes[i] = random.nextInt(TYPE_COUNT);
             }
             switch(dataTypes[i]) {
             case TYPE_VARCHAR30: 
             case TYPE_LONGVARCHAR: 
             case TYPE_NVARCHAR30:
             case TYPE_VARGRAPHIC30:
             case TYPE_LONGVARGRAPHIC:
             case TYPE_VARCHAR60:
             case TYPE_VARCHAR1000: 
               picking = false; 
             }
           }
           
          }
           int rowsCount = 100 + random.nextInt(1000);
           System.out.println("Test "+tableCount+": Testing "+rowsCount+" rows of : "+displayDataTypes(dataTypes));
           passed = testInsertCompressionCase("JDPSBC"+tableCount, dataTypes, rowsCount, sb);
           tableCount++; 
        }
        
        assertCondition(passed, sb); 
      }
      
      /**
       * Testing TYPE_VARCHAR30, others should compress
       * because 1/3 is VARCHAR. 
       **/
        public void Var040() {
            int[] dataTypes = {TYPE_INTKEY, TYPE_VARCHAR30, TYPE_CHAR30, TYPE_BIGINT, TYPE_BIGINT, TYPE_BIGINT, TYPE_SMALLINT};
            testUpdateCompression("JDPSBC040", dataTypes, 256, VERIFY_COMPRESSION);
        }


        /**
         * Testing TYPE_VARCHAR30, TYPE_CHAR30, TYPE_CHAR30, TYPE_CHAR30 should not compress
         **/
          public void Var041() {
              int[] dataTypes = {TYPE_INTKEY, TYPE_VARCHAR30, TYPE_CHAR30, TYPE_CHAR30, TYPE_CHAR30};
              testUpdateCompression("JDPSBC041", dataTypes, 256, VERIFY_NO_COMPRESSION);
          }

        
          /**
           * Testing only VARCHAR30 types for update  
          **/
            public void Var042() {
              int[] dataTypes = {TYPE_INTKEY,  TYPE_VARCHAR30, TYPE_VARCHAR30, TYPE_VARCHAR30,
                  TYPE_VARCHAR30, TYPE_VARCHAR30, };
              testUpdateCompression("JDPSBC042", dataTypes, 256, VERIFY_COMPRESSION );
            }

            /**
             * Testing only LONGVARCHAR30 types for update  
            **/
              public void Var043() {
                int[] dataTypes = { TYPE_INTKEY, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR,
                    TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, };
                testUpdateCompression("JDPSBC043", dataTypes, 256, VERIFY_COMPRESSION);
              }

          /**
           * Testing only NVARCHAR30 types for update  
          **/
            public void Var044() { 
              int[] dataTypes = { TYPE_INTKEY, TYPE_NVARCHAR30, TYPE_NVARCHAR30, TYPE_NVARCHAR30,
                  TYPE_NVARCHAR30, TYPE_NVARCHAR30, };
              testUpdateCompression("JDPSBC044", dataTypes, 256, VERIFY_COMPRESSION);
            }


          /**
           * Testing only VARGRAPHIC30 types for update  
          **/
            public void Var045() {
              int[] dataTypes = { TYPE_INTKEY, TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30,
                  TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30, };
              testUpdateCompression("JDPSBC045", dataTypes, 256, VERIFY_COMPRESSION);
            }


            /**
             * Testing only LONGVARGRAPHIC types for update
             **/
            public void Var046() {
              int[] dataTypes = { TYPE_INTKEY, TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC,
                  TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC, };
              testUpdateCompression("JDPSBC046", dataTypes, 256, VERIFY_COMPRESSION);
            }

            /**
             * Testing INTEGER, INTEGER, INTEGER, INTEGER for update
             **/
            public void Var047() {
              int[] dataTypes = { TYPE_INTKEY, TYPE_INTEGER, TYPE_INTEGER, TYPE_INTEGER, TYPE_INTEGER };
              testUpdateCompression("JDPSBC047", dataTypes, 256, VERIFY_NO_COMPRESSION);
            }

            /*** 
             * Randomly test combinations for 1 minutes
             */
            public void Var048() {
	if (toolboxNative) {
	    notApplicable("Toolbox native fails with Out of Memory");
	    return; 
	} 

              System.out.println("Var048 is testing update combinations for "+RUN_MINUTES+" minutes"); 
              Random random = new Random(); 
              long endTime = System.currentTimeMillis()+ RUN_MINUTES * 60000; 
              boolean passed = true; 
              StringBuffer sb = new StringBuffer(); 
              int tableCount = 1; 
              while (passed && System.currentTimeMillis() < endTime) {
                 // Pick number of columns 2 - 10
                 int columns = 2 + random.nextInt(8); 
                 int[] dataTypes = new int[columns];
                 dataTypes[0] = TYPE_INTKEY; 
                 for (int i = 1; i < columns; i++) {
                   dataTypes[i] = random.nextInt(TYPE_COUNT); 
                   while (dataTypes[i] == TYPE_INTKEY) { 
                     dataTypes[i] = random.nextInt(TYPE_COUNT);
                   }
                 }
                 
                 int rowsCount = 100 + random.nextInt(1000);
                 System.out.println("Test "+tableCount+": Testing "+rowsCount+" rows of : "+displayDataTypes(dataTypes));
                 passed = testUpdateCompressionCase("JDPSBC"+tableCount, dataTypes, rowsCount, sb);
                 tableCount++; 
              }
              
              assertCondition(passed, sb); 
            }


          /**
           * Testing only VARCHAR30 types for delete  
          **/
            public void Var049() {
              int[] dataTypes = {TYPE_INTKEY,  TYPE_VARCHAR30, TYPE_VARCHAR30, TYPE_VARCHAR30,
                  TYPE_VARCHAR30, TYPE_VARCHAR30, };
              testDeleteCompression("JDPSBC049", dataTypes, 256, VERIFY_COMPRESSION );
            }

            /**
             * Testing only LONGVARCHAR30 types for delete
            **/
              public void Var050() {
                int[] dataTypes = { TYPE_INTKEY, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR,
                    TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, };
                testDeleteCompression("JDPSBC050", dataTypes, 256, VERIFY_COMPRESSION);
              }

          /**
           * Testing only NVARCHAR30 types for delete
          **/
            public void Var051() { 
              int[] dataTypes = { TYPE_INTKEY, TYPE_NVARCHAR30, TYPE_NVARCHAR30, TYPE_NVARCHAR30,
                  TYPE_NVARCHAR30, TYPE_NVARCHAR30, };
              testDeleteCompression("JDPSBC051", dataTypes, 256, VERIFY_COMPRESSION);
            }


          /**
           * Testing only VARGRAPHIC30 types for delete
          **/
            public void Var052() {
              int[] dataTypes = { TYPE_INTKEY, TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30,
                  TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30, };
              testDeleteCompression("JDPSBC052", dataTypes, 256, VERIFY_COMPRESSION);
            }


            /**
             * Testing only LONGVARGRAPHIC types for delete
             **/
            public void Var053() {
              int[] dataTypes = { TYPE_INTKEY, TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC,
                  TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC, };
              testDeleteCompression("JDPSBC053", dataTypes, 256, VERIFY_COMPRESSION);
            }

            /**
             * Testing INTEGER, INTEGER, INTEGER, INTEGER for delete
             **/
            public void Var054() {
              int[] dataTypes = { TYPE_INTKEY, TYPE_INTEGER, TYPE_INTEGER, TYPE_INTEGER, TYPE_INTEGER };
              testDeleteCompression("JDPSBC054", dataTypes, 256, VERIFY_NO_COMPRESSION);
            }

            /*** 
             * Randomly test combinations for 1 minutes
             */
            public void Var055() {


	    String jvmName = System.getProperty("java.vm.name");
	    if (jvmName.indexOf("Classic") >=  0) { 
		String classpath = System.getProperty("java.class.path");
		if (classpath.indexOf("jt400native.jar") >= 0) {
		    notApplicable("Fails with OOM error for Classic JVM and jt400native.jar");
		    return; 
		} 
	    }

              System.out.println("Var055 is testing update combinations for "+RUN_MINUTES+" minutes"); 
              Random random = new Random(); 
              long endTime = System.currentTimeMillis()+ RUN_MINUTES * 60000; 
              boolean passed = true; 
              StringBuffer sb = new StringBuffer(); 
              int tableCount = 1; 
              while (passed && System.currentTimeMillis() < endTime) {
                 // Pick number of columns 2 - 10
                 int columns = 2 + random.nextInt(8); 
                 int[] dataTypes = new int[columns];
                 dataTypes[0] = TYPE_INTKEY; 
                 for (int i = 1; i < columns; i++) {
                   dataTypes[i] = random.nextInt(TYPE_COUNT); 
                   while (dataTypes[i] == TYPE_INTKEY) { 
                     dataTypes[i] = random.nextInt(TYPE_COUNT);
                   }
                 }
                 
                 int rowsCount = 100 + random.nextInt(1000);
                 System.out.println("Test "+tableCount+": Testing "+rowsCount+" rows of : "+displayDataTypes(dataTypes));
                 passed = testDeleteCompressionCase("JDPSBC"+tableCount, dataTypes, rowsCount, sb);
                 tableCount++; 
              }
              
              assertCondition(passed, sb); 
            }

          
           /**
           * Testing only VARCHAR30 types for merge
          **/
            public void Var056() {
              int[] dataTypes = {TYPE_INTKEY,  TYPE_VARCHAR30, TYPE_VARCHAR30, TYPE_VARCHAR30,
                  TYPE_VARCHAR30, TYPE_VARCHAR30, };
              testMergeCompression("JDPSBC056", dataTypes, 256, VERIFY_COMPRESSION );
            }

            /**
             * Testing only LONGVARCHAR30 types for merge
             **/
              public void Var057() {
                int[] dataTypes = { TYPE_INTKEY, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR,
                    TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, };
                testMergeCompression("JDPSBC057", dataTypes, 256, VERIFY_COMPRESSION);
              }

          /**
           * Testing only NVARCHAR30 types for merge
          **/
            public void Var058() { 
              int[] dataTypes = { TYPE_INTKEY, TYPE_NVARCHAR30, TYPE_NVARCHAR30, TYPE_NVARCHAR30,
                  TYPE_NVARCHAR30, TYPE_NVARCHAR30, };
              testMergeCompression("JDPSBC058", dataTypes, 256, VERIFY_COMPRESSION);
            }


          /**
           * Testing only VARGRAPHIC30 types for merge
          **/
            public void Var059() {
              int[] dataTypes = { TYPE_INTKEY, TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30,
                  TYPE_VARGRAPHIC30, TYPE_VARGRAPHIC30, };
              testMergeCompression("JDPSBC059", dataTypes, 256, VERIFY_COMPRESSION);
            }


            /**
             * Testing only LONGVARGRAPHIC types for merge
             **/
            public void Var060() {
              int[] dataTypes = { TYPE_INTKEY, TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC,
                  TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC, TYPE_LONGVARGRAPHIC, };
              testMergeCompression("JDPSBC060", dataTypes, 256, VERIFY_COMPRESSION);
            }

            /**
             * Testing INTEGER, INTEGER, INTEGER, INTEGER for merge
             **/
            public void Var061() {
              int[] dataTypes = { TYPE_INTKEY, TYPE_INTEGER, TYPE_INTEGER, TYPE_INTEGER, TYPE_INTEGER };
              testMergeCompression("JDPSBC061", dataTypes, 256, VERIFY_NO_COMPRESSION);
            }


           /* Issue 53914 -- Merge with CHAR CCSID 65535 fails */ 
            public void Var062() {
              int[] dataTypes = {
		  TYPE_INTKEY,
		  TYPE_DATE,
		  TYPE_SMALLINT,
		  TYPE_DECIMAL102,
		  TYPE_CHARHEX25,
		  TYPE_VARCHARFBD255,
		  TYPE_CHARFBD25,
		  TYPE_BIGINT,
		  TYPE_VARCHARFBD255,
	      };
	      issue53914exists  = ! testMergeCompression("JDPSBC073", dataTypes, 256, VERIFY_NONE, "Issue 53914 -- Merge with CHAR CCSI 65535 failed");
	    }





	    public void Var063() {
		notApplicable("PERFORMANCE VARIATION");
	    }            
            
	    public void Var064() {
		notApplicable("PERFORMANCE VARIATION");
	    }            
            
	    public void Var065() {
		notApplicable("PERFORMANCE VARIATION");
	    }            
            


  /**
   * Testing INTEGER, VARCHAR1000,VARCHARFBD255,BLOB
   **/
	    public void Var066() {
		int[] dataTypes = { TYPE_INTEGER,
		TYPE_VARCHAR1000,
		TYPE_VARCHARFBD255,
		TYPE_BLOB,
		};
		testInsertCompression("JDPSBC066", dataTypes, 256, VERIFY_COMPRESSION);
	    }

	    /**
	     * Testing INTEGER, VARCHAR1000,VARBINARY255,BLOB
	     **/
	        public void Var067() {
	      int[] dataTypes = { TYPE_INTEGER,
	      TYPE_VARCHAR1000,
	      TYPE_VARBINARY255,
	      TYPE_BLOB,
	      };
	      testInsertCompression("JDPSBC067", dataTypes, 256, VERIFY_COMPRESSION);
	        }


	        /**
	         * Testing INTEGER, VARCHAR1000,VARCHARHEX255,BLOB
	         **/
  public void Var068() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARCHARHEX255,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_COMPRESSION);
  }



  /**
   * Testing INTEGER, VARCHAR1000,CHARFBD25,BLOB
   **/
	    public void Var069() {
		int[] dataTypes = { TYPE_INTEGER,
		TYPE_VARCHAR1000,
		TYPE_CHARFBD25,
		TYPE_BLOB,
		};
		testInsertCompression("JDPSBC069", dataTypes, 256, VERIFY_COMPRESSION);
	    }

	    /**
	     * Testing INTEGER, VARCHAR1000,BINARY25,BLOB
	     **/
	        public void Var070() {
	      int[] dataTypes = { TYPE_INTEGER,
	      TYPE_VARCHAR1000,
	      TYPE_BINARY25,
	      TYPE_BLOB,
	      };
	      testInsertCompression("JDPSBC070", dataTypes, 256, VERIFY_COMPRESSION);
	        }


	        /**
	         * Testing INTEGER, VARCHAR1000,CHARHEX25,BLOB
	         **/
  public void Var071() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_CHARHEX25,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_COMPRESSION);
  }
        



            /**
             * Testing INTEGER, INTEGER, INTEGER, INTEGER for merge
             **/
            public void Var072() {
              int[] dataTypes = {
		  TYPE_INTKEY,       /* col0 INTEGER PRIMARY KEY, */ 
		  TYPE_DOUBLE,       /* col1 DOUBLE, */ 
		  TYPE_VARCHAR1000,  /* col2 VARCHAR(1000), */ 
		  TYPE_VARCHAR30,    /* col3 VARCHAR(60), */ 
		  TYPE_VARCHAR30,    /* col4 VARCHAR(60), */ 
		  TYPE_NUMERIC105,   /* col5 NUMERIC(10,5), */ 
		  TYPE_VARGRAPHIC30, /* col6 VARGRAPHIC(30) CCSID 1200, */ 
		  TYPE_VARCHAR30,    /* col7 VARCHAR(60), */ 
		  TYPE_CHARHEX25,  /* col8 CHAR(25) CCSID 65535 */ 
	      };
              testMergeCompression("JDPSBC072", dataTypes, 256, VERIFY_COMPRESSION);
            }

            /*** 
             * Randomly test combinations for 1 minutes
             */
            public void Var073() {
		if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		    notApplicable("Merge testcase for V7R1 and later");
		    return; 
		}
              System.out.println("Var062 is testing merge combinations for "+RUN_MINUTES+" minutes"); 
              Random random = new Random(); 
              long endTime = System.currentTimeMillis()+ RUN_MINUTES * 60000; 
              boolean passed = true; 
              StringBuffer sb = new StringBuffer(); 
              int tableCount = 1; 
              while (passed && System.currentTimeMillis() < endTime) {
                 // Pick number of columns 2 - 10
                 int columns = 2 + random.nextInt(8); 
                 int[] dataTypes = new int[columns];
                 dataTypes[0] = TYPE_INTKEY; 
                 for (int i = 1; i < columns; i++) {
                   dataTypes[i] = random.nextInt(TYPE_COUNT); 
                   while (dataTypes[i] == TYPE_INTKEY ||
			  // If issue53914exists do not test the bit data types
			  // Add the types as errors are found. 
			  (issue53914exists &&
			   ((dataTypes[i] == TYPE_CHARHEX25) ||
                            (dataTypes[i] == TYPE_VARCHARHEX255) || 
			   (dataTypes[i] == TYPE_VARCHARFBD255 ) ||
			    (dataTypes[i] == TYPE_VARCHARFBD257) ||
			    (dataTypes[i] == TYPE_VARCHARFBD32700) || 
			   (dataTypes[i] == TYPE_CHARFBD25) ||
			   (dataTypes[i] ==  TYPE_VARCHARFBD255)) )) { 
                     dataTypes[i] = random.nextInt(TYPE_COUNT);
                   }
                 }
                 
                 int rowsCount = 100 + random.nextInt(1000);
                 System.out.println("Test "+tableCount+": Testing "+rowsCount+" rows of : "+displayDataTypes(dataTypes));
                 passed = testMergeCompressionCase("JDPSBC"+tableCount, dataTypes, rowsCount, sb);
                 tableCount++; 
              }
              
              assertCondition(passed, sb); 
            }






	        /**
	         * Testing INTEGER, VARCHAR1000,VARCHAR256,BLOB
	         **/
  public void Var074() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARCHAR256,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_COMPRESSION);
  }
        


	        /**
	         * Testing INTEGER, VARCHAR1000,VARCHARFBD256,BLOB
	         **/
  public void Var075() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARCHARFBD256,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_COMPRESSION);
  }

	        /**
	         * Testing INTEGER, VARCHAR1000,VARBINARY256,BLOB
	         **/
  public void Var076() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARBINARY256,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_COMPRESSION);
  }

	        /**
	         * Testing INTEGER, VARCHAR1000,VARCHAR257,BLOB
	         **/
  public void Var077() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARCHAR257,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_COMPRESSION);
  }


 	        /**
	         * Testing INTEGER, VARCHAR1000,VARCHARFBD257,BLOB
	         **/
  public void Var078() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARCHARFBD257,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_COMPRESSION);
  }

 	        /**
	         * Testing INTEGER, VARCHAR1000,VARBINARY257,BLOB
	         **/
  public void Var079() {
    int[] dataTypes = { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARBINARY257,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_COMPRESSION);
  }


 	        /**
	         * Testing VARCHAR32700,BLOB
	         **/
  public void Var080() {
    int[] dataTypes = { TYPE_VARCHAR32700,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_COMPRESSION);
  }

 	        /**
	         * Testing VARCHARFBD32700,BLOB
	         **/
  public void Var081() {
    int[] dataTypes = { TYPE_VARCHARFBD32700,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_COMPRESSION);
  }

	        /**
	         * Testing VARBINARY32700,BLOB
	         * Compression does not occur with VARBINARY types 
	         **/
  public void Var082() {
    int[] dataTypes = { TYPE_VARBINARY32700,
        TYPE_BLOB, };
    testInsertCompression("JDPSBC068", dataTypes, 256, VERIFY_NO_COMPRESSION);
  }



  /**
   * Testing failed var
  **/
    public void Var083() {

	int rowsCount = 412; 
	int[] dataTypes = { TYPE_NUMERIC152, TYPE_VARCHARHEX255, TYPE_TIMESTAMP, };
  
	StringBuffer sb = new StringBuffer(); 
       System.out.println("Testing "+rowsCount+" rows of : "+displayDataTypes(dataTypes));
       boolean passed = testInsertCompressionCase("JDPSBC83", dataTypes, rowsCount, sb);

       assertCondition(passed, sb); 

    }

              /**
           * Testing VARCHAR32700,BOOLEAN
           **/
  public void Var084() {
    if (checkBooleanSupport()) { 
    int[] dataTypes = { TYPE_VARCHAR32700,
        TYPE_BOOLEAN, };
    testInsertCompression("JDPSBC084", dataTypes, 256, VERIFY_COMPRESSION);
    }
  }

    
    
    

  public static void oldMain(String[] args) {
      int[][] workingDataTypes = {
	  { TYPE_VARCHARFBD32700, TYPE_BLOB },
	  { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARCHAR256,  TYPE_BLOB, },
	  { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARCHARFBD256,TYPE_BLOB, },
	  { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARBINARY256, TYPE_BLOB, },
	  { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARCHAR257,  TYPE_BLOB, },
	  { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARCHARFBD257, TYPE_BLOB, },
	  { TYPE_INTEGER, TYPE_VARCHAR1000, TYPE_VARBINARY257,     TYPE_BLOB, },
	  { TYPE_VARCHAR32700,        TYPE_BLOB, },
	  { TYPE_VARCHARFBD32700,        TYPE_BLOB, },
	  { TYPE_VARBINARY32700,        TYPE_BLOB, },
    { TYPE_SMALLINT, TYPE_DBCLOB, TYPE_VARCHARFBD32700 },


      };

      for (int i = 0; i < workingDataTypes.length; i++) {
	  System.out.println("Size="+rowSize(workingDataTypes[i])+" rowTooBig="+rowTooBig(workingDataTypes[i])+" "+displayDataTypes(workingDataTypes[i]));
	  if (rowTooBig(workingDataTypes[i])) {
	      System.out.print("... ERROR -- row should not be too big "); 
	  } 
      } 



      int[][] failingDataTypes = {
	  { TYPE_INTEGER, TYPE_DBCLOB, TYPE_VARCHARFBD32700 },
	  { TYPE_INTKEY, TYPE_VARCHAR32700, TYPE_VARCHAR60 }, 
	  { TYPE_NCHAR, TYPE_VARCHAR32700}, 
	  { TYPE_VARCHAR32700, TYPE_NVARCHAR30}, 
      };

      for  (int i = 0; i < failingDataTypes.length; i++) {
	  System.out.println("Size="+rowSize(failingDataTypes[i])+
	      " rowTooBig="+rowTooBig(failingDataTypes[i])+
	      " "+displayDataTypes(failingDataTypes[i])); 

	  if (!rowTooBig(failingDataTypes[i])) {
	      System.out.print("... ERROR -- row should too big "); 
	  } 

      } 



  } 

}


