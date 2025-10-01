///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetObject41.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetObject41.java
//
// Classes:      JDCSGetObject41.java
//
////////////////////////////////////////////////////////////////////////


package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSQL400;
import test.JDTestDriver;
import test.JDTestcase;
import test.JVMInfo;

import java.io.FileOutputStream;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import java.io.*;


/**
Testcase JDCSGetObject41.  This tests the following
method of the JDBC CallableStatement class:

     getObject()

This testcases tests when the values are registered using Types.JAVA_OBJECT.
This tests the new method for JDBC 4.1

**/
public class JDCSGetObject41
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetObject41";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

    // Private data.
    private Connection          connection;

    /* Set this to true if any stored procedure definitions change */ 

    boolean cleanProcedures = true; 
    boolean serverJobLogDumped = false; 
    String parameterName = null; 

    String[][]  typesAndDefinitions = {
	{"ARINT", " AS INT ARRAY[10]"}
    } ;

    String[][]  proceduresAndDefinitions = {
	{"csgoSMLINT", "(INOUT B SMALLINT) LANGUAGE SQL BEGIN DECLARE DUMMY SMALLINT; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY + 10; END IF; END "},
	{"csgoINT","(INOUT B INT) LANGUAGE SQL BEGIN DECLARE DUMMY INT; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY + 10; END IF; END "},
	{"csgoBIGINT","(INOUT B BIGINT) LANGUAGE SQL BEGIN DECLARE DUMMY BIGINT; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY + 10; END IF; END "},
  {"csgoBOOLEAN","(INOUT B BOOLEAN) LANGUAGE SQL BEGIN DECLARE DUMMY BOOLEAN; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY; END IF; END "},
	{"csgoREAL","(INOUT B REAL) LANGUAGE SQL BEGIN DECLARE DUMMY REAL; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY + 10; END IF; END "},
	{"csgoFLOAT","(INOUT B FLOAT) LANGUAGE SQL BEGIN DECLARE DUMMY FLOAT; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY + 10; END IF; END "},
	{"csgoDOUBLE","(INOUT B DOUBLE) LANGUAGE SQL BEGIN DECLARE DUMMY DOUBLE; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY + 10; END IF; END "},
	{"csgoDEC","(INOUT B DECIMAL(10,2)) LANGUAGE SQL BEGIN DECLARE DUMMY DECIMAL(10,2); SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY + 10; END IF; END "},
	{"csgoNUM","(INOUT B NUMERIC(10,2)) LANGUAGE SQL BEGIN DECLARE DUMMY NUMERIC(10,2); SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY + 10; END IF; END "},
	{"csgoCHAR","(INOUT B CHAR(10)) LANGUAGE SQL BEGIN DECLARE DUMMY CHAR(5); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = TRIM(DUMMY) || TRIM(DUMMY); END IF; END "},

	{"csgoGRAPHIC","(INOUT B GRAPHIC(10) CCSID 1200) LANGUAGE SQL BEGIN DECLARE DUMMY GRAPHIC(5) CCSID 1200; SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = TRIM(DUMMY) || TRIM(DUMMY); END IF; END "}, 
	{"csgoVARG","(INOUT B VARGRAPHIC(10) CCSID 1200) LANGUAGE SQL BEGIN DECLARE DUMMY VARGRAPHIC(10) CCSID 1200; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY || DUMMY; END IF; END "},
	{"csgoVARC","(INOUT B VARCHAR(10)) LANGUAGE SQL BEGIN DECLARE DUMMY VARCHAR(10); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY || DUMMY; END IF; END "}, 
	{"csgoBIN", "(INOUT B BINARY(10)) LANGUAGE SQL BEGIN DECLARE DUMMY BINARY(5); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = TRIM(DUMMY) || TRIM(DUMMY); END IF; END "},
	{"csgoVARB","(INOUT B VARBINARY(10)) LANGUAGE SQL BEGIN DECLARE DUMMY VARBINARY(5); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = TRIM(DUMMY) || TRIM(DUMMY); END IF; END "},

	{"csgoDATE","(INOUT B DATE) LANGUAGE SQL BEGIN DECLARE DUMMY DATE; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DATE(DAYS(DUMMY)+1) ; END IF; END "},
	{"csgoTIME", "(INOUT B TIME) LANGUAGE SQL BEGIN DECLARE DUMMY TIME; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY+2 hours ; END IF; END "},
	{"csgoTS","(INOUT B timestamp) LANGUAGE SQL BEGIN DECLARE DUMMY timestamp; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY+2 hours + 2 days ; END IF; END "},


	{"csgoCLOB","(INOUT B CLOB(1M)) LANGUAGE SQL BEGIN DECLARE DUMMY CLOB(1M); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY || DUMMY; END IF; END "},

	{"csgoDBCLOB","(INOUT B dbclob(1M) CCSID 1200) LANGUAGE SQL BEGIN DECLARE DUMMY dbclob(1M) CCSID 1200; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY || DUMMY; END IF; END "}, 

	{"csgoBLOB", "(INOUT B BLOB(1M)) LANGUAGE SQL BEGIN DECLARE DUMMY BLOB(1M); SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY ; END IF; END "},

	{"csgoROWID", "(INOUT B ROWID) LANGUAGE SQL BEGIN DECLARE DUMMY ROWID; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY ; END IF; END "},

 
	{"csgoNCHAR","(INOUT B NCHAR(10)) LANGUAGE SQL BEGIN DECLARE DUMMY NCHAR(5); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = TRIM(DUMMY) || TRIM(DUMMY); END IF; END "},


	{"csgoNVARC","(INOUT B NVARCHAR(80)) LANGUAGE SQL BEGIN DECLARE DUMMY NVARCHAR(40); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY || DUMMY; END IF; END "},

	{"csgoNCLOB","(INOUT B NCLOB(1M)) LANGUAGE SQL BEGIN DECLARE DUMMY NCLOB(1M); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY || DUMMY; END IF; END "},


	/* TODO:  Remove this workaround when issue 46927 resolved */ 
/*
	{"csgoNCHAR","(INOUT B GRAPHIC(10) CCSID 1200) LANGUAGE SQL BEGIN DECLARE DUMMY GRAPHIC(5) CCSID 1200 ; SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = TRIM(DUMMY) || TRIM(DUMMY); END IF; END "},


	{"csgoNVARC","(INOUT B VARGRAPHIC(80) CCSID 1200) LANGUAGE SQL BEGIN DECLARE DUMMY VARGRAPHIC(40) CCSID 1200; SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY || DUMMY; END IF; END "},

	{"csgoNCLOB","(INOUT B DBCLOB(1M) CCSID 1200) LANGUAGE SQL BEGIN DECLARE DUMMY DBCLOB(1M) CCSID 1200 ; SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY || DUMMY; END IF; END "},
*/

	{"csgoXML","(INOUT B XML) LANGUAGE SQL BEGIN DECLARE DUMMY XML; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY ; END IF; END "},



	/* MIRROR character based procedures */ 
	{"csgoCHARM","(INOUT B CHAR(26)) LANGUAGE SQL BEGIN DECLARE DUMMY CHAR(26); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B =DUMMY; END IF; END "},
	{"csgoGRPHCM","(INOUT B GRAPHIC(26) CCSID 1200) LANGUAGE SQL BEGIN DECLARE DUMMY GRAPHIC(26) CCSID 1200; SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY; END IF; END "}, 
	{"csgoVARGM","(INOUT B VARGRAPHIC(26) CCSID 1200) LANGUAGE SQL BEGIN DECLARE DUMMY VARGRAPHIC(26) CCSID 1200; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY; END IF; END "},
	{"csgoVARCM","(INOUT B VARCHAR(80)) LANGUAGE SQL BEGIN DECLARE DUMMY VARCHAR(80); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY ; END IF; END "}, 
	{"csgoCLOBM","(INOUT B CLOB(1M)) LANGUAGE SQL BEGIN DECLARE DUMMY CLOB(1M); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY; END IF; END "},
	{"csgoDBCLBM","(INOUT B dbclob(1M) CCSID 1200) LANGUAGE SQL BEGIN DECLARE DUMMY dbclob(1M) CCSID 1200; SET DUMMY=B; IF DUMMY IS NOT NULL THEN SET B = DUMMY ; END IF; END "},


	
	{"csgoNCHARM","(INOUT B NCHAR(26)) LANGUAGE SQL BEGIN DECLARE DUMMY NCHAR(26); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY; END IF; END "},
	{"csgoNVARCM","(INOUT B NVARCHAR(80)) LANGUAGE SQL BEGIN DECLARE DUMMY NVARCHAR(80); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY; END IF; END "},
	{"csgoNCLOBM","(INOUT B NCLOB(1M)) LANGUAGE SQL BEGIN DECLARE DUMMY NCLOB(1M); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY ; END IF; END "},


/*
	{"csgoNCHARM","(INOUT B CHAR(26) CCSID 1208) LANGUAGE SQL BEGIN DECLARE DUMMY CHAR(26) CCSID 1208 ; SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY; END IF; END "},
	{"csgoNVARCM","(INOUT B VARCHAR(80) CCSID 1208) LANGUAGE SQL BEGIN DECLARE DUMMY VARCHAR(80) CCSID 1208; SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY; END IF; END "},
	{"csgoNCLOBM","(INOUT B CLOB(1M) CCSID 1208) LANGUAGE SQL BEGIN DECLARE DUMMY CLOB(1M) CCSID 1208; SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY ; END IF; END "},
*/

	{"csgoCHAR26","(INOUT B CHAR(26)) LANGUAGE SQL BEGIN DECLARE DUMMY CHAR(26); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B =DUMMY; END IF; END "},
	{"csgoGRPHC26","(INOUT B GRAPHIC(26) CCSID 1200) LANGUAGE SQL BEGIN DECLARE DUMMY GRAPHIC(26) CCSID 1200; SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY; END IF; END "}, 

	{"csgoNCHR26","(INOUT B NCHAR(26)) LANGUAGE SQL BEGIN DECLARE DUMMY NCHAR(26); SET DUMMY=TRIM(B); IF DUMMY IS NOT NULL THEN SET B = DUMMY; END IF; END "},

	{"csgoARINT","(INOUT B ARINT) LANGUAGE SQL BEGIN IF B IS NOT NULL THEN SET B = ARRAY[0,1,2,3,4]; END IF; END "},


    };


    Hashtable<String,String> skipHash = null;

    String[] skipListRelease = {};
    String[] skipListJDK = {};

    String[] skipListV5R4 = {
	"ARINT",
	"csgoNCHAR",
	"csgoNVARC",
	"csgoNCLOB",
	"csgoXML",
	"csgoNCHARM",
	"csgoNVARCM",
	"csgoNCLOBM",
	"csgoNCHR26",
	"csgoARINT",
    } ; 

    String[] skipListJDK15 = {
	"csgoNCHAR",
	"csgoNVARC",
	"csgoNCLOB",
	"csgoXML",
	"csgoNCHARM",
	"csgoNVARCM",
	"csgoNCLOBM",
	"csgoNCHR26",
	"csgoARINT",
	"java.sql.RowId",
	"java.sql.SQLXML",
	"java.sql.NClob",
	"csgoROWID",
    }; 


    private Hashtable<String,String> foundExceptions_ = null; 

/**
Constructor.
**/
    public JDCSGetObject41 (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetObject41",
               namesAndVars, runMode, fileOutputStream,
               password);
    }

    
    public JDCSGetObject41(AS400 systemObject, String testname,
      Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
 password);
  }

/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {

	serverJobLogDumped = false; 

	if (System.getProperty("debug") != null) {
	    debug = true; 
	} 

	


	if (JVMInfo.getJDK() < JVMInfo.JDK_16) { 
		skipListJDK = skipListJDK15; 
	} 

	skipHash  = new Hashtable<String,String>();
	for (int i = 0; i < skipListRelease.length; i++) {
	    skipHash.put(skipListRelease[i], skipListRelease[i]); 
	}
	for (int i = 0; i < skipListJDK.length; i++) {
	    skipHash.put(skipListJDK[i], skipListJDK[i]); 
	}



	
        String url = baseURL_
            
            ;
        if(isToolboxDriver())              //@B1A
            url = url + ";date format=iso";                         //@B1A
        if(getDriver() == JDTestDriver.DRIVER_NATIVE)               //@B1A
            url = url + ";time format=jis;";                         //@B1A
        connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

	Statement stmt = connection.createStatement();

	stmt.executeUpdate("SET SCHEMA "+JDCSTest.COLLECTION); 
	stmt.executeUpdate("SET CURRENT PATH "+JDCSTest.COLLECTION); 
	if (cleanProcedures) {
	    System.out.println("************** cleaning procedures *********"); 
	    for (int i = 0; i < proceduresAndDefinitions.length; i++) {
		String[] thisProc = proceduresAndDefinitions[i];
		if (skipHash.get(thisProc[0]) == null) { 
		    String sql = "DROP PROCEDURE "+JDCSTest.COLLECTION+"."+thisProc[0]; 
		    try {
			stmt.executeUpdate(sql); 
		    } catch (Exception e) {
			String message = e.toString();
			String expected = "not found";
			if (message.indexOf(expected) >= 0) {
		    // ignore expected message
			} else {
			    System.out.println("Exception for : "+sql); 
			    e.printStackTrace(); 
			}

		    }
		}
	    } 

	} 


	System.out.println("************** creating types *********");

	for (int i = 0; i < typesAndDefinitions.length; i++) {
	    String[] thisType = typesAndDefinitions[i];
	    if (skipHash.get(thisType[0]) == null) { 
		String sql = "CREATE TYPE "+JDCSTest.COLLECTION+"."+thisType[0]+thisType[1]; 
		try {
		    if ((!areBooleansSupported()) && (sql.indexOf("BOOLEAN") >= 0)) {
		      // Skip booleans if not supported
		    } else { 
			stmt.executeUpdate(sql);
			System.out.println("Created type "+thisType[0]); 
		    }
		} catch (Exception e) {
		    String message = e.toString();
		    String expected = "already exists";
		    if (message.indexOf(expected) >= 0) {
		    // ignore expected message
			System.out.println("Type exists "+thisType[0]); 
		    } else {
			System.out.println("Exception for : "+sql); 
			e.printStackTrace(); 
		    }
		}
	    }
	} 

	// Create a set of procedures to test the different data types.
	System.out.println("************** creating procedures *********"); 
	for (int i = 0; i < proceduresAndDefinitions.length; i++) {
	    String[] thisProc = proceduresAndDefinitions[i];
	    if (skipHash.get(thisProc[0]) == null) { 
		String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+thisProc[0]+thisProc[1]; 
		try {

		     if ((!areBooleansSupported()) && (sql.indexOf("BOOLEAN") >= 0)) {
          // Skip booleans if not supported

		    } else { 
			stmt.executeUpdate(sql); 
		    }
		} catch (Exception e) {
		    String message = e.toString();
		    String expected = "already exists";
		    if (message.indexOf(expected) >= 0) {
		    // ignore expected message
		    } else {
			System.out.flush();
			System.err.flush(); 
			System.out.println("Exception for : "+sql); 
			e.printStackTrace(); 
		    }

		}
	    }
	} 
	


        
    }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {

	// Don't clean up the procedures. They will be reused on the next run of the test
        connection.close ();
    }



/**
getObject() - getObject should throw exception when statement is closed. 
**/
    public void Var001()
    {
    if (checkJdbc41()) {
      try {

        CallableStatement cstmt = connection.prepareCall("{call "
            + JDCSTest.COLLECTION + ".csgoSMLINT (?)}");
        cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
        cstmt.setInt(1, 20);
        cstmt.execute();
        cstmt.close();
        try {
          if (parameterName == null) {
            JDReflectionUtil.callMethod_O(cstmt, "getObject", 1, String.class);
          } else {
            JDReflectionUtil.callMethod_O(cstmt, "getObject", parameterName,
                String.class);
          }
          failed("Did not throw exception on closed statement");
        } catch (Exception sqlex) {
	    assertClosedException(sqlex, "");
        }
      } catch (SQLException e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


    public void testInvalidIndex(int index) {
      if (checkJdbc41()) try{
	   
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".csgoSMLINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setInt(1, 20);
	    cstmt.execute();
	    try {
              if (parameterName == null) {
		JDReflectionUtil.callMethod_O(cstmt,"getObject", index, String.class);
              } else {
                JDReflectionUtil.callMethod_O(cstmt,"getObject", parameterName+index, String.class);
              }
		failed("Did not throw exception on invalid index "+index); 
	    } catch (Exception sqlex) {
		String message = sqlex.toString();
		String expected = "index not valid";
                if (parameterName != null) {
                  expected = "undefined column name"; 
                }
		assertCondition(message.indexOf(expected) >= 0, " Got expection "+message+" expected "+expected); 
	    }
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }
/**
getObject() - Should throw an exception when the parameter
is an invalid index.
**/

    public void Var002()  {  testInvalidIndex(2);  }


/**
getObject() - Should throw an exception when the parameter
is 0.
**/
    public void Var003()  {  testInvalidIndex(0);  }

/**
getObject() - Should throw an exception when the parameter
is -1.
**/
    public void Var004()  {  testInvalidIndex(-1);  }

/**
 * Pad variations
 */

    public void Var005()  {  notApplicable("Reserved5 for future use"); }
    public void Var006()  {  notApplicable("Reserved6 for future use"); }
    public void Var007()  {  notApplicable("Reserved7 for future use"); }
    public void Var008()  {  notApplicable("Reserved8 for future use"); }
    public void Var009()  {  notApplicable("Reserved9 for future use"); } 



    Class<?> xmlClass = null;
  Class<?> rowidClass = null;

  /*
   * get getObject using the following testSpec, where the elements of the array
   * are as follows testSpec[0] className testSpec[1] statement to execute
   * testSpec[2] input parameter,expected output parameter testSpec[3] input
   * parameter,expected output parameter ....
   */
  public void testGetObject(String[] testSpec, String parmName) {
    if (checkJdbc41()) {
	String objectDetailString = null; 
      StringBuffer sb = new StringBuffer();
      sb
          .append("Note:  NCHAR/NVARCHAR/NCLOB types fail because of issue 46678 -- 11/16/2011 \n"
              + "Note:  some null inparameter parameters fails issue 46685: MCH3601 T/F QSQCALLSP/MAP with Null array parameter -- var 35,65,95,125,155,185,215,245,275,305,335,365,395,425,455,485,515,545,575,605,635,665,695,,, ");
      boolean passed = true;
      try {
        String className = testSpec[0];
        String callStatement = testSpec[1];
        int testCount = testSpec.length - 2;
        sb.append("Testing class " + className + "\n");

        for (int i = 0; i < skipListRelease.length; i++) {
          if (callStatement.indexOf(skipListRelease[i]) >= 0) {
            notApplicable("Skipping:  found " + skipListRelease[i] + " in "
                + callStatement);
            return;
          }
        }

        for (int i = 0; i < skipListJDK.length; i++) {
          if (callStatement.indexOf(skipListJDK[i]) >= 0) {
            notApplicable("Skipping:  found " + skipListJDK[i] + " in "
                + callStatement);
            return;
          }

          if (className.indexOf(skipListJDK[i]) >= 0) {
            notApplicable("Skipping:  found " + skipListJDK[i] + " in "
                + className);
            return;
          }

        }

        Class<?> lookupClass = Class.forName(className);

        if (xmlClass == null) {
          try {
            xmlClass = Class.forName("java.sql.SQLXML");
          } catch (Exception e) {
          }
        }
        if (rowidClass == null) {

          try {
            rowidClass = Class.forName("java.sql.RowId");
          } catch (Exception e) {
          }
        }

        sb.append("Preparing callStatement " + callStatement + "\n");
        CallableStatement cstmt = connection.prepareCall(callStatement);
        cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
        for (int i = 1; i <= testCount; i++) {
          String testLine = testSpec[i + 1];
          String input = null;
          String expected = null;
          int commaIndex = testLine.indexOf(",");
          if (commaIndex < 0) {
            sb.append("***** Unable to determine test from line " + testLine
                + "\n");
            passed = false;
          } else {
            input = testLine.substring(0, commaIndex);
            expected = testLine.substring(commaIndex + 1);
            sb.append("Setting input parameter to " + input + "\n");
            if (input.equalsIgnoreCase("null")) {
              cstmt.setString(1, null);
            } else if (input.indexOf("BYTEARRAY=") == 0) {
              String remaining = input.substring(10);
              byte[] stuff = stringToBytes(remaining);
              cstmt.setBytes(1, stuff);
            } else if (input.indexOf("SQLARRAY=[") == 0) {
              String remaining = input.substring(10);
              Object arrayObject = JDSQL400.getNativeOrSqlArray(connection,
                  remaining, true);
              cstmt.setArray(1, (Array) arrayObject);
            } else {
              cstmt.setString(1, input);
            }

            sb.append("Executing\n");

            try {
              cstmt.execute();

              Object o;
              if (parmName == null) {
                o = JDReflectionUtil.callMethod_O(cstmt, "getObject", 1,
                    lookupClass);
              } else {
                o = JDReflectionUtil.callMethod_O(cstmt, "getObject", parmName,
                    lookupClass);
              }
              String objectString;
              String objectClassName;
              Class<?> objectClass = null;
              if (o == null) {
                objectString = "null";
                objectClass = lookupClass;
                objectClassName = lookupClass.getName();
              } else {
                objectClass = o.getClass();
                objectClassName = objectClass.getName();

                if (objectClassName.equals("[B")) {

                  objectString = "BYTEARRAY=" + bytesToString((byte[]) o);
                } else if (o instanceof java.io.InputStream) {
                  objectString = "INPUTSTREAM="
                      + inputStreamToString((java.io.InputStream) o);
                } else if (o instanceof java.io.Reader) {
                  objectString = "READER=" + readerToString((java.io.Reader) o);
                } else if (o instanceof java.sql.Blob) {
                  InputStream in = ((java.sql.Blob) o).getBinaryStream();
                  objectString = "BLOB=" + inputStreamToString(in);
                } else if (o instanceof java.sql.Clob) {
                  Reader in = ((java.sql.Clob) o).getCharacterStream();
                  objectString = "CLOB=" + readerToString(in);
                } else if (rowidClass != null
                    && rowidClass.isAssignableFrom(objectClass)) {
                  byte[] stuff = (byte[]) JDReflectionUtil.callMethod_O(o,
                      "getBytes");
                  objectString = "ROWID=" + bytesToString(stuff);
                } else if (xmlClass != null
                    && xmlClass.isAssignableFrom(objectClass)) {
                  Reader in = (Reader) JDReflectionUtil.callMethod_O(o,
                      "getCharacterStream");
                  objectString = "SQLXML=" + readerToString(in);
		  objectDetailString="SQLXML.class="+o.getClass().getName(); 
                } else if (o instanceof java.sql.Array) {
                  StringWriter writer = new StringWriter();
                  PrintWriter printWriter = new PrintWriter(writer);
                  JDSQL400.printArray(printWriter, (java.sql.Array) o);
                  printWriter.flush();
                  objectString = "SQLARRAY=" + writer.toString();
                } else {
                  objectString = o.toString();
                }
              }
              if (expected.equals(objectString)) {
                // Object is good
              } else {
                sb.append("***** For test " + i + " got " + objectString);
		if (objectDetailString !=null) {
		    sb.append(" detail="+objectDetailString); 
		}
		sb.append(" sb " + expected + "\n");
                sb.append("\"" + input + "," + objectString + "\",\n");
                passed = false;
              }

              if (objectClassName.equals(className)) {
                // Class is good
              } else {
                if (lookupClass.isAssignableFrom(objectClass)) {
                  // Still good
                } else {
                  sb.append("***** For test " + i + " got class="
                      + objectClassName + " sb class=" + className + "\n");
                  passed = false;
                }
              }

            } catch (Exception e) {
              String exceptionString = e.toString();
              if (exceptionString == null) {
                exceptionString = e.getClass().getName() + ":null";
              }
              if (exceptionString.indexOf(expected) >= 0) {
                // Exception is good
              } else {
		  if (!serverJobLogDumped) {
		      dumpServerJobLog(connection);
		      serverJobLogDumped = true; 
		  }
                sb.append("***** For test " + i + " got exception "
                    + exceptionString + " sb " + expected + "\n");
                sb.append("\"" + input + "," + exceptionString + "\",\n");
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);

                // Only dump the exception once
                e.printStackTrace(printWriter);
                String exception = stringWriter.toString();
                String checkException = exception;
                int getObjectIndex = checkException.indexOf("JDCSGetObject41");
                if (getObjectIndex >= 0) {
                  checkException = checkException.substring(0, getObjectIndex);
                }

                if (foundExceptions_ == null) {
                  foundExceptions_ = new Hashtable<String,String>();
                }
                if (foundExceptions_.get(checkException) == null) {
                  foundExceptions_.put(checkException, checkException);
                  sb.append(exception);
                } else {
                  // Skip
                }

                passed = false;
              }
            }
          }
        }
        cstmt.close();
        assertCondition(passed, sb.toString() + "\n");
      } catch (Exception e) {
        failed(connection, e, "Unexpected Exception " + sb.toString() + "\n");
      }
    }
  }



    /* Test string 1X with SMALLINT X0 */ 
    public void Var010()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with INT X1 */ 
    public void Var011()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test string 1X with BIGINT X2 */ 
    public void Var012()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with REAL X3 */ 
    public void Var013()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with FLOAT X4 */ 
    public void Var014()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test string 1X with DOUBLE X5 */ 
    public void Var015()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with DECIMAL X6 */ 
    public void Var016()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with NUMERIC X7 */ 
    public void Var017()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with CHAR X8 */ 
    public void Var018()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "ABCD,ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with GRAPHIC X9 */ 
    public void Var019()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "ABCD,ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with VARCHAR X0 */ 
    public void Var020()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with VARGRAPHIC X1 */ 
    public void Var021()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test string 1X with BINARY X2 */
    /* Changed to be 7 bit ascii */
    public void Var022()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,ABCDABCD000000000000",
              "null,null"};

           String[] testArrayNative = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=5678,\u0056\u0078\u0056\u0078\u0000\u0000\u0000\u0000\u0000\u0000",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with VARBINARY X3 */ 
    public void Var023()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,ABCDABCD",
              "null,null"};


           String[] testArrayNative = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=5678,\u0056\u0078\u0056\u0078",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with DATE X4 */ 
    public void Var024()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,2011-09-29",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with TIME X5 */ 
    public void Var025()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,12:11:12",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test string 1X with TIMESTAMP X6 */ 
    public void Var026()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,1990-03-04 10:30:00.010000",
              "null,null"};


           String[] testArrayNative = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,1990-03-04-10.30.00.010000",
              "null,null"};


	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test string 1X with CLOB X7 */ 
    public void Var027()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test string 1X with DBCLOB X8 */ 
    public void Var028()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test string 1X with BLOB X9 */ 
    public void Var029()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,ABCD",
              "null,null"};


           String[] testArrayNative = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=5678,\u0056\u0078",
              "null,null"};


	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test string 1X with ROWID X0 */ 
    public void Var030()  {
	String[] testArray = {
	    "java.lang.String",
	    "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	    "BYTEARRAY=ABCD,ABCD",
	    "null,null"};

	String[] testArrayNative = {
	    "java.lang.String",
	    "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	    "BYTEARRAY=5678,\u0056\u0078",
	    "null,null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

	    testGetObject(testArray,parameterName);
    }



    /* Test string 1X with NCHAR X1 */ 
    public void Var031()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCD,ABCDABCD  ",
              "null,null"};

           testGetObject(testArray,parameterName);
    }

 
    /* Test string 1X with NVARCHAR X2 */ 
    public void Var032()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test string 1X with NCLOB X3 */ 
    public void Var033()  {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test string 1X with XML X4 */ 
    public void Var034()  {
	if (checkRelease710()) {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,<h>h</h>",
              "null,null"};
           testGetObject(testArray,parameterName);
	}
    }

    /* Test string 1X with ARRAY X5 */ 
    public void Var035()  {
	String[] testArray = {
	    "java.lang.String",
	    "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
	    "SQLARRAY=[Integer:1:2:3],Data type mismatch",
	    "null,null"};
	if (checkRelease710()) 
	    testGetObject(testArray,parameterName);
    }

    /* Test string 1X with BOOLEAN X2 */ 
    public void Var036()  { 
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.lang.String",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,1",
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }

    /* Test string 1X padd */
    public void Var037()  {  notApplicable("Reserved7 for future use"); }
    public void Var038()  {  notApplicable("Reserved8 for future use"); }
    public void Var039()  {  notApplicable("Reserved9 for future use"); } 


    /* Test java.lang.Byte 4X*/

    /* Test java.lang.Byte 4X with SMALLINT X0 */ 
    public void Var040()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with INT X1 */ 
    public void Var041()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.lang.Byte 4X with BIGINT X2 */ 
    public void Var042()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with REAL X3 */ 
    public void Var043()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with FLOAT X4 */ 
    public void Var044()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Byte 4X with DOUBLE X5 */ 
    public void Var045()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with DECIMAL X6 */ 
    public void Var046()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with NUMERIC X7 */ 
    public void Var047()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with CHAR X8 */ 
    public void Var048()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with GRAPHIC X9 */ 
    public void Var049()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
	      "Bogus,Data type mismatch", 
              "null,null"};


           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with VARCHAR X0 */ 
    public void Var050()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with VARGRAPHIC X1 */ 
    public void Var051()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "1,11",
	      "259,Data type mismatch",     /* added 11/11/2011*/ 
	      "Bogus,Data type mismatch", 
              "null,null"};


           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Byte 4X with BINARY X2 */ 
    public void Var052()  {

           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};


           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with VARBINARY X3 */ 
    public void Var053()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with DATE X4 */ 
    public void Var054()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with TIME X5 */ 
    public void Var055()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);



    }


    /* Test java.lang.Byte 4X with TIMESTAMP X6 */ 
    public void Var056()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Byte 4X with CLOB X7 */ 
    public void Var057()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Byte 4X with DBCLOB X8 */ 
    public void Var058()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with BLOB X9 */ 
    public void Var059()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Byte 4X with ROWID X0 */ 
    public void Var060()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.lang.Byte 4X with NCHAR X1 */ 
    public void Var061()  {

	String[] testArray = {
	    "java.lang.Byte",
	    "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	    "ABCD,Data type mismatch",
	    "null,null"};

	    if (checkRelease710())  
		testGetObject(testArray,parameterName);
	    
    }

 
    /* Test java.lang.Byte 4X with NVARCHAR X2 */ 
    public void Var062()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Byte 4X with NCLOB X3 */ 
    public void Var063()  {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Byte 4X with XML X4 */ 
    public void Var064()  {

           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Byte 4X with ARRAY X5 */ 
    public void Var065()  {
        String[] testArray = {
            "java.lang.Byte",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Byte 4X with BOOLEAN X2 */ 
    public void Var066()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.lang.Byte",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,1",
        "0,0",    
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }

    /* Test java.lang.Byte 4X pad */
    public void Var067()  {  notApplicable("Reserved7 for future use"); }
    public void Var068()  {  notApplicable("Reserved8 for future use"); }
    public void Var069()  {  notApplicable("Reserved9 for future use"); } 


    /* Test java.lang.Integer 7X*/

    /* Test java.lang.Integer 7X with SMALLINT X0 */ 
    public void Var070()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with INT X1 */ 
    public void Var071()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.lang.Integer 7X with BIGINT X2 */ 
    public void Var072()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,11",
	      "3000000000,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with REAL X3 */ 
    public void Var073()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,11",
	      "3000000000,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with FLOAT X4 */ 
    public void Var074()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,11",
	      "3000000000,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Integer 7X with DOUBLE X5 */ 
    public void Var075()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,11",
	      "3000000000,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with DECIMAL X6 */ 
    public void Var076()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with NUMERIC X7 */ 
    public void Var077()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with CHAR X8 */ 
    public void Var078()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "1,11",
	      "30000,Data type mismatch", 
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with GRAPHIC X9 */ 
    public void Var079()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "1,11",
	      "30000,Data type mismatch",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with VARCHAR X0 */ 
    public void Var080()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "1,11",
	      "30000,Data type mismatch",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with VARGRAPHIC X1 */ 
    public void Var081()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "1,11",
	      "30000,Data type mismatch",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Integer 7X with BINARY X2 */ 
    public void Var082()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with VARBINARY X3 */ 
    public void Var083()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with DATE X4 */ 
    public void Var084()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with TIME X5 */ 
    public void Var085()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Integer 7X with TIMESTAMP X6 */ 
    public void Var086()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
	      /* Note:  null will come back because type registered as JAVA_OBJECT */ 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Integer 7X with CLOB X7 */ 
    public void Var087()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "1,Data type mismatch",
	      "30000,Data type mismatch",
	      "bogus,Data type mismatch",
	      "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Integer 7X with DBCLOB X8 */ 
    public void Var088()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "1,Data type mismatch",
	      "30000,Data type mismatch",
	      "bogus,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with BLOB X9 */ 
    public void Var089()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Integer 7X with ROWID X0 */ 
    public void Var090()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.lang.Integer 7X with NCHAR X1 */ 
    public void Var091()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "1,11",
	      "30000,Data type mismatch",
	      "bogus,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }

 
    /* Test java.lang.Integer 7X with NVARCHAR X2 */ 
    public void Var092()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "1,11",
	      "30000,Data type mismatch",
	      "bogus,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Integer 7X with NCLOB X3 */ 
    public void Var093()  {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "1,Data type mismatch",
	      "30000,Data type mismatch",
	      "bogus,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Integer 7X with XML X4 */ 
    public void Var094()  {

           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
 	if (checkRelease710()) 
          testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with ARRAY X5 */ 
    public void Var095()  {
        String[] testArray = {
            "java.lang.Integer",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Integer 7X with BIGINT X2 */ 
    public void Var096()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.lang.Integer",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,1",
        "0,0", 
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }


    /* Test java.lang.Integer 7X padd */
    public void Var097()  {  notApplicable("Reserved7 for future use"); }
    public void Var098()  {  notApplicable("Reserved8 for future use"); }
    public void Var099()  {  notApplicable("Reserved9 for future use"); } 





    /* Test java.lang.Short 10X*/

    /* Test java.lang.Short 10X with SMALLINT X0 */ 
    public void Var100()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with INT X1 */ 
    public void Var101()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,11",
	      "65535,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.lang.Short 10X with BIGINT X2 */ 
    public void Var102()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,11",
	      "65535,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with REAL X3 */ 
    public void Var103()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,11",
	      "65535,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with FLOAT X4 */ 
    public void Var104()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,11",
	      "65535,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Short 10X with DOUBLE X5 */ 
    public void Var105()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,11",
	      "65535,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with DECIMAL X6 */ 
    public void Var106()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,11",
	      "65535,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with NUMERIC X7 */ 
    public void Var107()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,11",
	      "65535,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with CHAR X8 */ 
    public void Var108()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "1,11",
	      "Bogus,Data type mismatch", 
	      "655,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with GRAPHIC X9 */ 
    public void Var109()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "1,11",
	      "Bogus,Data type mismatch", 
	      "655,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with VARCHAR X0 */ 
    public void Var110()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "1,11",
	      "Bogus,Data type mismatch", 
	      "655,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with VARGRAPHIC X1 */ 
    public void Var111()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "1,11",
	      "655,Data type mismatch",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Short 10X with BINARY X2 */ 
    public void Var112()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with VARBINARY X3 */ 
    public void Var113()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with DATE X4 */ 
    public void Var114()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with TIME X5 */ 
    public void Var115()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Short 10X with TIMESTAMP X6 */ 
    public void Var116()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Short 10X with CLOB X7 */ 
    public void Var117()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Short 10X with DBCLOB X8 */ 
    public void Var118()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Short 10X with BLOB X9 */ 
    public void Var119()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Short 10X with ROWID X0 */ 
    public void Var120()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.lang.Short 10X with NCHAR X1 */ 
    public void Var121()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "1,11",
	      "655,Data type mismatch",
	      "bogus,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.lang.Short 10X with NVARCHAR X2 */ 
    public void Var122()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "1,11",
	      "655,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Short 10X with NCLOB X3 */ 
    public void Var123()  {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Short 10X with XML X4 */ 
    public void Var124()  {

           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
 	if (checkRelease710()) 
          testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Short 10X with ARRAY X5 */ 
    public void Var125()  {
        String[] testArray = {
            "java.lang.Short",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    
    /* Test java.lang.Short 10X with BOOLEAN X2 */ 
    public void Var126()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.lang.Short",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,1",
        "0,0", 
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }

    /* Test java.lang.Short 10X padd */
    public void Var127()  {  notApplicable("Reserved7 for future use"); }
    public void Var128()  {  notApplicable("Reserved8 for future use"); }
    public void Var129()  {  notApplicable("Reserved9 for future use"); } 


    /* Test java.lang.Long 13X*/


    /* Test java.lang.Long 13X with SMALLINT X0 */ 
    public void Var130()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with INT X1 */ 
    public void Var131()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.lang.Long 13X with BIGINT X2 */ 
    public void Var132()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with REAL X3 */ 
    public void Var133()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,11",
	      "655,665",
	      "1.9E19,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with FLOAT X4 */ 
    public void Var134()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,11",
	      "1.9E19,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Long 13X with DOUBLE X5 */ 
    public void Var135()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,11",
	      "1.9E19,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with DECIMAL X6 */ 
    public void Var136()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with NUMERIC X7 */ 
    public void Var137()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with CHAR X8 */ 
    public void Var138()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "1,11",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with GRAPHIC X9 */ 
    public void Var139()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "1,11",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with VARCHAR X0 */ 
    public void Var140()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoVARCM (?)",
	      "11,11",
	      "3000000000000000000000030000000000000000000000,Data type mismatch",
	      "Bogus,Data type mismatch", 
              "null,null"};


           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with VARGRAPHIC X1 */ 
    public void Var141()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "1,11",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Long 13X with BINARY X2 */ 
    public void Var142()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with VARBINARY X3 */ 
    public void Var143()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with DATE X4 */ 
    public void Var144()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with TIME X5 */ 
    public void Var145()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Long 13X with TIMESTAMP X6 */ 
    public void Var146()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Long 13X with CLOB X7 */ 
    public void Var147()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Long 13X with DBCLOB X8 */ 
    public void Var148()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Long 13X with BLOB X9 */ 
    public void Var149()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Long 13X with ROWID X0 */ 
    public void Var150()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.lang.Long 13X with NCHAR X1 */ 
    public void Var151()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.lang.Long 13X with NVARCHAR X2 */ 
    public void Var152()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoNVARCM (?)",
	      "11,11",
	      "3000000000000000000000030000000000000000000000,Data type mismatch",
	      "bogus,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Long 13X with NCLOB X3 */ 
    public void Var153()  {
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Long 13X with XML X4 */ 
    public void Var154()  {

           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Long 13X with ARRAY X5 */ 
    public void Var155()  {
        String[] testArray = {
            "java.lang.Long",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    
    /* Test java.lang.Long 13X with BOOLEAN X2 */ 
    public void Var156()  {
      if (checkBooleanSupport()) { 
           String[] testArray = {
              "java.lang.Long",
              "call "+JDCSTest.COLLECTION+".csgoBoolean (?)",
        "1,1",
        "0,0",
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }

    /* Test java.lang.Long 13X padd */
    public void Var157()  {  notApplicable("Reserved7 for future use"); }
    public void Var158()  {  notApplicable("Reserved8 for future use"); }
    public void Var159()  {  notApplicable("Reserved9 for future use"); } 


    /* Test java.lang.Float 16X*/

    /* Test java.lang.Float 16X with SMALLINT X0 */ 
    public void Var160()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with INT X1 */ 
    public void Var161()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.lang.Float 16X with BIGINT X2 */ 
    public void Var162()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with REAL X3 */ 
    public void Var163()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with FLOAT X4 */ 
    public void Var164()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,11.0",
	      "3.4E38,3.4E38",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Float 16X with DOUBLE X5 */ 
    public void Var165()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,11.0",
	      "4.0E40,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with DECIMAL X6 */ 
    public void Var166()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with NUMERIC X7 */ 
    public void Var167()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with CHAR X8 */ 
    public void Var168()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "1,11.0",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with GRAPHIC X9 */ 
    public void Var169()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "1,11.0",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with VARCHAR X0 */ 
    public void Var170()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "1,11.0",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with VARGRAPHIC X1 */ 
    public void Var171()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "1,11.0",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Float 16X with BINARY X2 */ 
    public void Var172()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with VARBINARY X3 */ 
    public void Var173()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with DATE X4 */ 
    public void Var174()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with TIME X5 */ 
    public void Var175()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Float 16X with TIMESTAMP X6 */ 
    public void Var176()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Float 16X with CLOB X7 */ 
    public void Var177()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Float 16X with DBCLOB X8 */ 
    public void Var178()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with BLOB X9 */ 
    public void Var179()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Float 16X with ROWID X0 */ 
    public void Var180()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.lang.Float 16X with NCHAR X1 */ 
    public void Var181()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.lang.Float 16X with NVARCHAR X2 */ 
    public void Var182()  {

	   /* 11/5/2014 Changed from old to native behavior for all */ 
           /* String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoNVARCM (?)",
	      "11,11.0",
	      "40000000000000000000004000000000000000000000,Data type mismatch",
	      "bogus,Data type mismatch",
              "null,null"};
	      */ 

           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoNVARCM (?)",
	      "11,11.0",
	      "40000000000000000000004000000000000000000000,Infinity",
	      "bogus,Data type mismatch",
              "null,null"};

/* 
	      if (getDriver()== JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      }
*/

	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Float 16X with NCLOB X3 */ 
    public void Var183()  {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Float 16X with XML X4 */ 
    public void Var184()  {

           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Float 16X with ARRAY X5 */ 
    public void Var185()  {
        String[] testArray = {
            "java.lang.Float",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Float 16X with BOOLEAN X2 */ 
    public void Var186()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.lang.Float",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
              "1,1.0",
              "0,0.0",
              "7,1.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    }

    /* Test java.lang.Float 16X padd */
    public void Var187()  {  notApplicable("Reserved7 for future use"); }
    public void Var188()  {  notApplicable("Reserved8 for future use"); }
    public void Var189()  {  notApplicable("Reserved9 for future use"); } 

    /* Test java.lang.Double 19X*/

    /* Test java.lang.Double 19X with SMALLINT X0 */ 
    public void Var190()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with INT X1 */ 
    public void Var191()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.lang.Double 19X with BIGINT X2 */ 
    public void Var192()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with REAL X3 */ 
    public void Var193()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with FLOAT X4 */ 
    public void Var194()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Double 19X with DOUBLE X5 */ 
    public void Var195()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with DECIMAL X6 */ 
    public void Var196()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with NUMERIC X7 */ 
    public void Var197()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with CHAR X8 */ 
    public void Var198()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "1,11.0",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with GRAPHIC X9 */ 
    public void Var199()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "1,11.0",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with VARCHAR X0 */ 
    public void Var200()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "1,11.0",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with VARGRAPHIC X1 */ 
    public void Var201()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "1,11.0",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Double 19X with BINARY X2 */ 
    public void Var202()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with VARBINARY X3 */ 
    public void Var203()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with DATE X4 */ 
    public void Var204()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with TIME X5 */ 
    public void Var205()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Double 19X with TIMESTAMP X6 */ 
    public void Var206()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Double 19X with CLOB X7 */ 
    public void Var207()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Double 19X with DBCLOB X8 */ 
    public void Var208()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with BLOB X9 */ 
    public void Var209()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Double 19X with ROWID X0 */ 
    public void Var210()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.lang.Double 19X with NCHAR X1 */ 
    public void Var211()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.lang.Double 19X with NVARCHAR X2 */ 
    public void Var212()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "1,11.0",
	      "bogus,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Double 19X with NCLOB X3 */ 
    public void Var213()  {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Double 19X with XML X4 */ 
    public void Var214()  {

           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Double 19X with ARRAY X5 */ 
    public void Var215()  {
        String[] testArray = {
            "java.lang.Double",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Double 19X with BOOLEAN X1 */ 
    public void Var216()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.lang.Double",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,1.0",
        "0,0.0",
                     "null,null"};
           testGetObject(testArray,parameterName);
    }
    }

    /* Test java.lang.Double 19X padd */
    public void Var217()  {  notApplicable("Reserved7 for future use"); }
    public void Var218()  {  notApplicable("Reserved8 for future use"); }
    public void Var219()  {  notApplicable("Reserved9 for future use"); } 




    /* Test java.math.BigDecimal 22X*/
    /* Test java.math.BigDecimal 22X with SMALLINT X0 */ 
    public void Var220()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with INT X1 */ 
    public void Var221()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.math.BigDecimal 22X with BIGINT X2 */ 
    public void Var222()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with REAL X3 */ 
    public void Var223()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with FLOAT X4 */ 
    public void Var224()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.math.BigDecimal 22X with DOUBLE X5 */ 
    public void Var225()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with DECIMAL X6 */ 
    public void Var226()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with NUMERIC X7 */ 
    public void Var227()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with CHAR X8 */ 
    public void Var228()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "1,11",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with GRAPHIC X9 */ 
    public void Var229()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "1,11",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with VARCHAR X0 */ 
    public void Var230()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "1,11",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with VARGRAPHIC X1 */ 
    public void Var231()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "1,11",
	      "Bogus,Data type mismatch", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.math.BigDecimal 22X with BINARY X2 */ 
    public void Var232()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with VARBINARY X3 */ 
    public void Var233()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with DATE X4 */ 
    public void Var234()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with TIME X5 */ 
    public void Var235()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.math.BigDecimal 22X with TIMESTAMP X6 */ 
    public void Var236()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.math.BigDecimal 22X with CLOB X7 */ 
    public void Var237()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.math.BigDecimal 22X with DBCLOB X8 */ 
    public void Var238()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with BLOB X9 */ 
    public void Var239()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.math.BigDecimal 22X with ROWID X0 */ 
    public void Var240()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.math.BigDecimal 22X with NCHAR X1 */ 
    public void Var241()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "1,11",
	      "bogus,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }

 
    /* Test java.math.BigDecimal 22X with NVARCHAR X2 */ 
    public void Var242()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "1,11",
	      "bogus,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.math.BigDecimal 22X with NCLOB X3 */ 
    public void Var243()  {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.math.BigDecimal 22X with XML X4 */ 
    public void Var244()  {

           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with ARRAY X5 */ 
    public void Var245()  {
        String[] testArray = {
            "java.math.BigDecimal",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    /* Test java.math.BigDecimal 22X with BOOLEAN X2 */ 
    public void Var246()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.math.BigDecimal",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,1",
        "0,0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    }

    /* Test java.math.BigDecimal 22X padd */
    public void Var247()  {  notApplicable("Reserved7 for future use"); }
    public void Var248()  {  notApplicable("Reserved8 for future use"); }
    public void Var249()  {  notApplicable("Reserved9 for future use"); } 


    /* Test java.lang.Boolean 25X*/

    /* Test java.lang.Boolean 25X with SMALLINT X0 */ 
    public void Var250()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "-10,false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with INT X1 */ 
    public void Var251()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "-10,false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.lang.Boolean 25X with BIGINT X2 */ 
    public void Var252()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "-10,false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with REAL X3 */ 
    public void Var253()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "-10,false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with FLOAT X4 */ 
    public void Var254()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "-10,false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Boolean 25X with DOUBLE X5 */ 
    public void Var255()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "-10,false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with DECIMAL X6 */ 
    public void Var256()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "-10,false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with NUMERIC X7 */ 
    public void Var257()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "-10,false", 
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with CHAR X8 */ 
    public void Var258()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      ",false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with GRAPHIC X9 */ 
    public void Var259()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      ",false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with VARCHAR X0 */ 
    public void Var260()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      ",false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with VARGRAPHIC X1 */ 
    public void Var261()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      ",false",
	      "1,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Boolean 25X with BINARY X2 */ 
    public void Var262()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with VARBINARY X3 */ 
    public void Var263()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=01,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with DATE X4 */ 
    public void Var264()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with TIME X5 */ 
    public void Var265()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Boolean 25X with TIMESTAMP X6 */ 
    public void Var266()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Boolean 25X with CLOB X7 */ 
    public void Var267()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Boolean 25X with DBCLOB X8 */ 
    public void Var268()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with BLOB X9 */ 
    public void Var269()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Boolean 25X with ROWID X0 */ 
    public void Var270()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.lang.Boolean 25X with NCHAR X1 */ 
    public void Var271()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoNCHARM (?)",
	      "1,true",
	      "0,false",
	      "bogus,true",
	      ",false",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }

 
    /* Test java.lang.Boolean 25X with NVARCHAR X2 */ 
    public void Var272()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoNVARCM (?)",
	      "1,true",
	      "0,false",
	      "bogus,true",
	      ",false",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Boolean 25X with NCLOB X3 */ 
    public void Var273()  {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "1,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Boolean 25X with XML X4 */ 
    public void Var274()  {

           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with ARRAY X5 */ 
    public void Var275()  {
        String[] testArray = {
            "java.lang.Boolean",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Boolean 25X with BOOLEAN X2 */ 
    public void Var276()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.lang.Boolean",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "0,false",
        "1,true",
        "false,false",
        "true,true",
        "no,false",
        "yes,true",
        "f,false",
        "t,true",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    }

    /* Test java.lang.Boolean 25X padd */
    public void Var277()  {  notApplicable("Reserved7 for future use"); }
    public void Var278()  {  notApplicable("Reserved8 for future use"); }
    public void Var279()  {  notApplicable("Reserved9 for future use"); } 

    /* Test [B 28X*/
    /* Test [B 28X with SMALLINT X0 */ 
    public void Var280()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with INT X1 */ 
    public void Var281()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test [B 28X with BIGINT X2 */ 
    public void Var282()  {


           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,null"};


           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with REAL X3 */ 
    public void Var283()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with FLOAT X4 */ 
    public void Var284()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test [B 28X with DOUBLE X5 */ 
    public void Var285()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with DECIMAL X6 */ 
    public void Var286()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with NUMERIC X7 */ 
    public void Var287()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with CHAR X8 */ 
    public void Var288()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "12345,BYTEARRAY=1234512345", 
	      "ABCD,Data type mismatch",
              "null,null"};

           String[] testArrayNative = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "12345,Data type mismatch",
	      "ABCD,Data type mismatch",
              "null,null"};

	      if (getDriver()== JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with GRAPHIC X9 */ 
    public void Var289()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "12345,BYTEARRAY=1234512345", 
	      "ABCD,Data type mismatch",
	      "Bogus,Data type mismatch", 
              "null,null"};

           String[] testArrayNative = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "12345,Data type mismatch",
	      "ABCD,Data type mismatch",
	      "Bogus,Data type mismatch", 
              "null,null"};

	      if (getDriver()== JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with VARCHAR X0 */ 
    public void Var290()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,BYTEARRAY=ABCDABCD",
	      "Bogus,Data type mismatch", 
              "null,null"};


           String[] testArrayNative = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with VARGRAPHIC X1 */ 
    public void Var291()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,BYTEARRAY=ABCDABCD",
	      "Bogus,Data type mismatch", 
              "null,null"};



           String[] testArrayNative = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,Data type mismatch",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test [B 28X with BINARY X2 */ 
    public void Var292()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,BYTEARRAY=ABCDABCD000000000000",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with VARBINARY X3 */ 
    public void Var293()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,BYTEARRAY=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with DATE X4 */ 
    public void Var294()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with TIME X5 */ 
    public void Var295()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test [B 28X with TIMESTAMP X6 */ 
    public void Var296()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test [B 28X with CLOB X7 */ 
    public void Var297()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,BYTEARRAY=ABCDABCD",
              "null,null"};

           String[] testArrayNative = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test [B 28X with DBCLOB X8 */ 
    public void Var298()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,BYTEARRAY=ABCDABCD",
              "null,null"};

           String[] testArrayNative = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};


	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with BLOB X9 */ 
    public void Var299()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,BYTEARRAY=ABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test [B 28X with ROWID X0 */ 
    public void Var300()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,BYTEARRAY=ABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test [B 28X with NCHAR X1 */ 
    public void Var301()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCDE,BYTEARRAY=ABCDEABCDE",
	      "bogus,Data type mismatch",
              "null,null"};

           String[] testArrayNative = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCDE,Data type mismatch",
	      "bogus,Data type mismatch",
              "null,null"};


	      if (getDriver()== JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }

 
    /* Test [B 28X with NVARCHAR X2 */ 
    public void Var302()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,BYTEARRAY=ABCDABCD",
	      "bogus,Data type mismatch",
              "null,null"};


           String[] testArrayNative = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,Data type mismatch",
	      "bogus,Data type mismatch",
              "null,null"};


	      if (getDriver()== JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test [B 28X with NCLOB X3 */ 
    public void Var303()  {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,BYTEARRAY=ABCDABCD",
	      "bogus,Data type mismatch",
              "null,null"};

           String[] testArrayNative = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "bogus,Data type mismatch",
	      "ABCD,Data type mismatch", 
              "null,null"};

	      if (getDriver()== JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test [B 28X with XML X4 */ 
    public void Var304()  {

           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,BYTEARRAY=3C683E683C2F683E",
              "null,null"};


	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with ARRAY X5 */ 
    public void Var305()  {
        String[] testArray = {
            "[B",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    /* Test [B 28X with BOOLEAN X2 */ 
    public void Var306()  {

if (checkBooleanSupport()) {
           String[] testArray = {
              "[B",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};


           testGetObject(testArray,parameterName);
}
    }

    /* Test [B 28X padd */
    public void Var307()  {  notApplicable("Reserved7 for future use"); }
    public void Var308()  {  notApplicable("Reserved8 for future use"); }
    public void Var309()  {  notApplicable("Reserved9 for future use"); } 

    /* Test java.sql.Date 31X*/


    /* Test java.sql.Date 31X with SMALLINT X0 */ 
    public void Var310()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with INT X1 */ 
    public void Var311()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.sql.Date 31X with BIGINT X2 */ 
    public void Var312()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with REAL X3 */ 
    public void Var313()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with FLOAT X4 */ 
    public void Var314()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Date 31X with DOUBLE X5 */ 
    public void Var315()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with DECIMAL X6 */ 
    public void Var316()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with NUMERIC X7 */ 
    public void Var317()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with CHAR X8 */ 
    public void Var318()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoCHARM (?)",
	      "2011-09-01,2011-09-01",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with GRAPHIC X9 */ 
    public void Var319()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoGRPHCM (?)",
	      "Bogus,Data type mismatch", 
	      "2011-09-01,2011-09-01",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with VARCHAR X0 */ 
    public void Var320()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoVARCM (?)",
	      "Bogus,Data type mismatch", 
	      "2011-09-01,2011-09-01",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with VARGRAPHIC X1 */ 
    public void Var321()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoVARGM (?)",
	      "Bogus,Data type mismatch", 
	      "2011-09-01,2011-09-01",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Date 31X with BINARY X2 */ 
    public void Var322()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with VARBINARY X3 */ 
    public void Var323()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with DATE X4 */ 
    public void Var324()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,2011-09-29",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with TIME X5 */ 
    public void Var325()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Date 31X with TIMESTAMP X6 */ 
    public void Var326()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,1990-03-04",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Date 31X with CLOB X7 */ 
    public void Var327()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoCLOBM (?)",
	      "Bogus,Data type mismatch", 
	      "1990-03-02,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Date 31X with DBCLOB X8 */ 
    public void Var328()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoDBCLBM (?)",
	      "Bogus,Data type mismatch", 
	      "1990-03-02,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with BLOB X9 */ 
    public void Var329()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Date 31X with ROWID X0 */ 
    public void Var330()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with NCHAR X1 */ 
    public void Var331()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoNCHARM (?)",
	      "Bogus,Data type mismatch", 
	      "1990-03-02,1990-03-02",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.sql.Date 31X with NVARCHAR X2 */ 
    public void Var332()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoNVARCM (?)",
	      "Bogus,Data type mismatch", 
	      "1990-03-02,1990-03-02",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Date 31X with NCLOB X3 */ 
    public void Var333()  {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoNCLOBM (?)",
	      "Bogus,Data type mismatch", 
	      "1990-03-02,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Date 31X with XML X4 */ 
    public void Var334()  {

           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with ARRAY X5 */ 
    public void Var335()  {
        String[] testArray = {
            "java.sql.Date",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Date 31X with BIGINT X2 */ 
    public void Var336()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.sql.Date",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    }

    /* Test java.sql.Date 31X padd */
    public void Var337()  {  notApplicable("Reserved7 for future use"); }
    public void Var338()  {  notApplicable("Reserved8 for future use"); }
    public void Var339()  {  notApplicable("Reserved9 for future use"); } 



    /* Test java.sql.Time 34X*/


    /* Test java.sql.Time 34X with SMALLINT X0 */ 
    public void Var340()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with INT X1 */ 
    public void Var341()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.sql.Time 34X with BIGINT X2 */ 
    public void Var342()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with REAL X3 */ 
    public void Var343()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with FLOAT X4 */ 
    public void Var344()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Time 34X with DOUBLE X5 */ 
    public void Var345()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with DECIMAL X6 */ 
    public void Var346()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with NUMERIC X7 */ 
    public void Var347()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with CHAR X8 */ 
    public void Var348()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoCHARM (?)",
	      "Bogus,Data type mismatch", 
	      "11:22:33,11:22:33",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with GRAPHIC X9 */ 
    public void Var349()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoGRPHCM (?)",
	      "Bogus,Data type mismatch", 
	      "11:22:33,11:22:33",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with VARCHAR X0 */ 
    public void Var350()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoVARCM (?)",
	      "Bogus,Data type mismatch", 
	      "11:22:33,11:22:33",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with VARGRAPHIC X1 */ 
    public void Var351()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoVARGM (?)",
	      "Bogus,Data type mismatch", 
	      "11:22:33,11:22:33",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Time 34X with BINARY X2 */ 
    public void Var352()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with VARBINARY X3 */ 
    public void Var353()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with DATE X4 */ 
    public void Var354()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with TIME X5 */ 
    public void Var355()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,12:11:12",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Time 34X with TIMESTAMP X6 */ 
    public void Var356()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,10:30:00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Time 34X with CLOB X7 */ 
    public void Var357()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoCLOBM (?)",
	      "Bogus,Data type mismatch", 
	      "11:22:33,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Time 34X with DBCLOB X8 */ 
    public void Var358()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoDBCLBM (?)",
	      "Bogus,Data type mismatch", 
	      "11:22:33,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with BLOB X9 */ 
    public void Var359()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Time 34X with ROWID X0 */ 
    public void Var360()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Time 34X with NCHAR X1 */ 
    public void Var361()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoNCHARM (?)",
	      "Bogus,Data type mismatch", 
	      "11:22:33,11:22:33",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.sql.Time 34X with NVARCHAR X2 */ 
    public void Var362()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoNVARCM (?)",
	      "Bogus,Data type mismatch", 
	      "11:22:33,11:22:33",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Time 34X with NCLOB X3 */ 
    public void Var363()  {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoNCLOBM (?)",
	      "Bogus,Data type mismatch", 
	      "11:22:33,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Time 34X with XML4 */ 
    public void Var364()  {

           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};

	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Time 34X with ARRAY X5 */ 
    public void Var365()  {
        String[] testArray = {
            "java.sql.Time",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }
    
    /* Test java.sql.Time 34X with BOOLEAN X2 */ 
    public void Var366()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.sql.Time",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }


    /* Test java.sql.Time 34X padd */
    public void Var367()  {  notApplicable("Reserved7 for future use"); }
    public void Var368()  {  notApplicable("Reserved8 for future use"); }
    public void Var369()  {  notApplicable("Reserved9 for future use"); } 

    /* Test java.sql.Timestamp 37X*/
    /* Test java.sql.Timestamp 37X with SMALLINT X0 */ 
    public void Var370()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with INT X1 */ 
    public void Var371()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.sql.Timestamp 37X with BIGINT X2 */ 
    public void Var372()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with REAL X3 */ 
    public void Var373()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with FLOAT X4 */ 
    public void Var374()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Timestamp 37X with DOUBLE X5 */ 
    public void Var375()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with DECIMAL X6 */ 
    public void Var376()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with NUMERIC X7 */ 
    public void Var377()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with CHAR X8 */ 
    public void Var378()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoCHAR26 (?)",
	      "2011-09-01 11:22:01.012345,2011-09-01 11:22:01.012345",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with GRAPHIC X9 */ 
    public void Var379()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoGRPHC26 (?)",
	      "Bogus,Data type mismatch",
	      "2011-09-01 11:22:01.012345,2011-09-01 11:22:01.012345",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with VARCHAR X0 */ 
    public void Var380()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoVARCM (?)",
	      "Bogus,Data type mismatch",
	      "2011-09-01 11:22:01.012345,2011-09-01 11:22:01.012345",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with VARGRAPHIC X1 */ 
    public void Var381()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoVARGM (?)",
	      "Bogus,Data type mismatch", 
	      "2011-09-01 11:22:01.012345,2011-09-01 11:22:01.012345",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Timestamp 37X with BINARY X2 */ 
    public void Var382()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with VARBINARY X3 */ 
    public void Var383()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with DATE X4 */ 
    public void Var384()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-01,2011-09-02 00:00:00.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with TIME X5 */ 
    public void Var385()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,1970-01-01 12:11:12.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Timestamp 37X with TIMESTAMP X6 */ 
    public void Var386()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,1990-03-04 10:30:00.01",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Timestamp 37X with CLOB X7 */ 
    public void Var387()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoCLOBM (?)",
	      "Bogus,Data type mismatch", 
	      "1990-03-02,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Timestamp 37X with DBCLOB X8 */ 
    public void Var388()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoDBCLBM (?)",
	      "Bogus,Data type mismatch", 
	      "1990-03-02,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with BLOB X9 */ 
    public void Var389()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Timestamp 37X with ROWID X0 */ 
    public void Var390()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with NCHAR X1 */ 
    public void Var391()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoNCHARM (?)",
	      "Bogus,Data type mismatch", 
	      "2011-09-01 11:22:01.012345,2011-09-01 11:22:01.012345",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }

 
    /* Test java.sql.Timestamp 37X with NVARCHAR X2 */ 
    public void Var392()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoNVARCM (?)",
	      "Bogus,Data type mismatch", 
	      "2011-09-01 11:22:01.012345,2011-09-01 11:22:01.012345",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Timestamp 37X with NCLOB X3 */ 
    public void Var393()  {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoNCLOBM (?)",
	      "Bogus,Data type mismatch", 
	      "2011-09-01 11:22:01.012345,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Timestamp 37X with XML X4 */ 
    public void Var394()  {

           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Timestamp 37X with ARRAY X5 */ 
    public void Var395()  {
        String[] testArray = {
            "java.sql.Timestamp",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    
    /* Test java.sql.Timestamp 37X with BOOLEAN X2 */ 
    public void Var396()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.sql.Timestamp",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    }

    /* Test java.sql.Timestamp 37X padd */
    public void Var397()  {  notApplicable("Reserved7 for future use"); }
    public void Var398()  {  notApplicable("Reserved8 for future use"); }
    public void Var399()  {  notApplicable("Reserved9 for future use"); } 

    /* Test java.io.InputStream 40X*/
    /* Test java.io.InputStream 40X with SMALLINT X0 */ 
    public void Var400()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with INT X1 */ 
    public void Var401()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.io.InputStream 40X with BIGINT X2 */ 
    public void Var402()  {
   

           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,null"};




           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with REAL X3 */ 
    public void Var403()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with FLOAT X4 */ 
    public void Var404()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.InputStream 40X with DOUBLE X5 */ 
    public void Var405()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with DECIMAL X6 */ 
    public void Var406()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with NUMERIC X7 */ 
    public void Var407()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with CHAR X8 */ 
    public void Var408()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with GRAPHIC X9 */ 
    public void Var409()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with VARCHAR X0 */ 
    public void Var410()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,INPUTSTREAM=ABCDABCD",
              "null,null"};


           String[] testArrayNative = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with VARGRAPHIC X1 */ 
    public void Var411()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,INPUTSTREAM=ABCDABCD",
              "null,null"};
           String[] testArrayNative = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,Data type mismatch",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 




           testGetObject(testArray,parameterName);
    }


    /* Test java.io.InputStream 40X with BINARY X2 */ 
    public void Var412()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,INPUTSTREAM=ABCDABCD000000000000",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with VARBINARY X3 */ 
    public void Var413()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,INPUTSTREAM=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with DATE X4 */ 
    public void Var414()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with TIME X5 */ 
    public void Var415()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.InputStream 40X with TIMESTAMP X6 */ 
    public void Var416()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.InputStream 40X with CLOB X7 */ 
    public void Var417()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,INPUTSTREAM=ABCDABCD",
              "null,null"};

           String[] testArrayNative = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test java.io.InputStream 40X with DBCLOB X8 */ 
    public void Var418()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,INPUTSTREAM=ABCDABCD",
              "null,null"};


           String[] testArrayNative = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};


	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with BLOB X9 */ 
    public void Var419()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,INPUTSTREAM=ABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.InputStream 40X with ROWID X0 */ 
    public void Var420()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,INPUTSTREAM=ABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.io.InputStream 40X with NCHAR X1 */ 
    public void Var421()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCDE,INPUTSTREAM=ABCDEABCDE",
              "null,null"};

           String[] testArrayNative = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCDE,Data type mismatch",
              "null,null"};

	      if (getDriver()== JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }

 
    /* Test java.io.InputStream 40X with NVARCHAR X2 */ 
    public void Var422()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,INPUTSTREAM=ABCDABCD",
              "null,null"};
           
           String[] testArrayNative = {
               "java.io.InputStream",
               "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
         "ABCD,Data type mismatch",
               "null,null"};
           
	      if (getDriver()== JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.io.InputStream 40X with NCLOB X3 */ 
    public void Var423()  {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,INPUTSTREAM=ABCDABCD",
              "null,null"};

           String[] testArrayNative = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};

	      if (getDriver()== JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.io.InputStream 40X with XML X4 */ 
    public void Var424()  {

	String[] testArray = {
	    "java.io.InputStream",
	    "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	    "<h>h</h>,Data type mismatch",
	    "null,null"};

	    if (isToolboxDriver()) {

		String[] testArrayToolbox = {
		    "java.io.InputStream",
		    "call "+JDCSTest.COLLECTION+".csgoXML (?)",
		    "<h>h</h>,INPUTSTREAM=3C683E683C2F683E",
		    "null,null"};
		    testArray =testArrayToolbox;
	    }


	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with ARRAY X5 */ 
    public void Var425()  {
        String[] testArray = {
            "java.io.InputStream",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    /* Test java.io.InputStream 40X with BIGINT X2 */ 
    public void Var426()  {
   
if (checkBooleanSupport()) {
           String[] testArray = {
              "java.io.InputStream",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};




           testGetObject(testArray,parameterName);
}
    }


    /* Test java.io.InputStream 40X padd */
    public void Var427()  {  notApplicable("Reserved7 for future use"); }
    public void Var428()  {  notApplicable("Reserved8 for future use"); }
    public void Var429()  {  notApplicable("Reserved9 for future use"); } 

    /* Test java.io.Reader 43X*/

    /* Test java.io.Reader 43X with SMALLINT X0 */ 
    public void Var430()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,READER=11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with INT X1 */ 
    public void Var431()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,READER=11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.io.Reader 43X with BIGINT X2 */ 
    public void Var432()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,READER=11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with REAL X3 */ 
    public void Var433()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,READER=11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with FLOAT X4 */ 
    public void Var434()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,READER=11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.Reader 43X with DOUBLE X5 */ 
    public void Var435()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,READER=11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with DECIMAL X6 */ 
    public void Var436()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,READER=11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with NUMERIC X7 */ 
    public void Var437()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,READER=11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with CHAR X8 */ 
    public void Var438()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "ABCD,READER=ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with GRAPHIC X9 */ 
    public void Var439()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "ABCD,READER=ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with VARCHAR X0 */ 
    public void Var440()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,READER=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with VARGRAPHIC X1 */ 
    public void Var441()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,READER=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.Reader 43X with BINARY X2 */ 
    public void Var442()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,READER=ABCDABCD000000000000",
              "null,null"};

           String[] testArrayNative = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=5678,READER=\u0056\u0078\u0056\u0078\u0000\u0000\u0000\u0000\u0000\u0000",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with VARBINARY X3 */ 
    public void Var443()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,READER=ABCDABCD",
              "null,null"};

           String[] testArrayNative = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=5678,READER=\u0056\u0078\u0056\u0078",
              "null,null"};


	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with DATE X4 */ 
    public void Var444()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,READER=2011-09-29",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with TIME X5 */ 
    public void Var445()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,READER=12:11:12",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.Reader 43X with TIMESTAMP X6 */ 
    public void Var446()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,READER=1990-03-04 10:30:00.010000",
	      "null,null"};

           String[] testArrayNative = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,READER=1990-03-04-10.30.00.010000",
	      "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test java.io.Reader 43X with CLOB X7 */ 
    public void Var447()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,READER=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.Reader 43X with DBCLOB X8 */ 
    public void Var448()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,READER=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with BLOB X9 */ 
    public void Var449()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,READER=ABCD",
              "null,null"};

           String[] testArrayNative= {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=5678,READER=\u0056\u0078",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test java.io.Reader 43X with ROWID X0 */ 
    public void Var450()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,READER=ABCD",
              "null,null"};


           String[] testArrayNative = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=6162,READER=ab",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }



    /* Test java.io.Reader 43X with NCHAR X1 */ 
    public void Var451()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCD,READER=ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.io.Reader 43X with NVARCHAR X2 */ 
    public void Var452()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,READER=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.Reader 43X with NCLOB X3 */ 
    public void Var453()  {
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,READER=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.Reader 43X with XML X4 */ 
    public void Var454()  {

           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,READER=<h>h</h>",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.io.Reader 43X with ARRAY X5 */ 
    public void Var455()  {
        String[] testArray = {
            "java.io.Reader",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    /* Test java.io.Reader 43X with BOOLEAN X2 */ 
    public void Var456()  {
      if (checkBooleanSupport()) { 
           String[] testArray = {
              "java.io.Reader",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,READER=1",
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }

    /* Test java.io.Reader 43X padd */
    public void Var457()  {  notApplicable("Reserved7 for future use"); }
    public void Var458()  {  notApplicable("Reserved8 for future use"); }
    public void Var459()  {  notApplicable("Reserved9 for future use"); } 

    /* Test java.sql.Clob 46X*/

    /* Test java.sql.Clob 46X with SMALLINT X0 */ 
    public void Var460()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,CLOB=11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with INT X1 */ 
    public void Var461()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,CLOB=11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.sql.Clob 46X with BIGINT X2 */ 
    public void Var462()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,CLOB=11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with REAL X3 */ 
    public void Var463()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,CLOB=11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with FLOAT X4 */ 
    public void Var464()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,CLOB=11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Clob 46X with DOUBLE X5 */ 
    public void Var465()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,CLOB=11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with DECIMAL X6 */ 
    public void Var466()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,CLOB=11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with NUMERIC X7 */ 
    public void Var467()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,CLOB=11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with CHAR X8 */ 
    public void Var468()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "ABCD,CLOB=ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with GRAPHIC X9 */ 
    public void Var469()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "ABCD,CLOB=ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with VARCHAR X0 */ 
    public void Var470()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with VARGRAPHIC X1 */ 
    public void Var471()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Clob 46X with BINARY X2 */ 
    public void Var472()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,CLOB=ABCDABCD000000000000",
              "null,null"};


           String[] testArrayNative = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=5678,CLOB=\u0056\u0078\u0056\u0078\u0000\u0000\u0000\u0000\u0000\u0000",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with VARBINARY X3 */ 
    public void Var473()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,CLOB=ABCDABCD",
              "null,null"};

           String[] testArrayNative = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=5678,CLOB=\u0056\u0078\u0056\u0078",
              "null,null"};


	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with DATE X4 */ 
    public void Var474()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,CLOB=2011-09-29",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with TIME X5 */ 
    public void Var475()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,CLOB=12:11:12",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Clob 46X with TIMESTAMP X6 */ 
    public void Var476()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,CLOB=1990-03-04 10:30:00.010000",
              "null,null"};


           String[] testArrayNative = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,CLOB=1990-03-04-10.30.00.010000",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Clob 46X with CLOB X7 */ 
    public void Var477()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Clob 46X with DBCLOB X8 */ 
    public void Var478()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Clob 46X with BLOB X9 */ 
    public void Var479()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,CLOB=ABCD",
              "null,null"};

           String[] testArrayNative = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=5678,CLOB=\u0056\u0078",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Clob 46X with ROWID X0 */ 
    public void Var480()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,CLOB=ABCD",
              "null,null"};

           String[] testArrayNative = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=6162,CLOB=ab",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }



    /* Test java.sql.Clob 46X with NCHAR X1 */ 
    public void Var481()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCD,CLOB=ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.sql.Clob 46X with NVARCHAR X2 */ 
    public void Var482()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Clob 46X with NCLOB X3 */ 
    public void Var483()  {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Clob 46X with XML X4 */ 
    public void Var484()  {

           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,CLOB=<h>h</h>",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Clob 56X with ARRAY X5 */ 
    public void Var485()  {
        String[] testArray = {
            "java.sql.Clob",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    
    /* Test java.sql.Clob 46X with BOOLEN X2 */ 
    public void Var486()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.sql.Clob",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    }

    /* Test java.sql.Clob 46X padd */
    public void Var487()  {  notApplicable("Reserved7 for future use"); }
    public void Var488()  {  notApplicable("Reserved8 for future use"); }
    public void Var489()  {  notApplicable("Reserved9 for future use"); } 

    /* Test java.sql.Blob 49X*/
    /* Test java.sql.Blob 49X with SMALLINT X0 */ 
    public void Var490()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with INT X1 */ 
    public void Var491()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.sql.Blob 49X with BIGINT X2 */ 
    public void Var492()  {
 
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,null"};


           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with REAL X3 */ 
    public void Var493()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with FLOAT X4 */ 
    public void Var494()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Blob 49X with DOUBLE X5 */ 
    public void Var495()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with DECIMAL X6 */ 
    public void Var496()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with NUMERIC X7 */ 
    public void Var497()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with CHAR X8 */ 
    public void Var498()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with GRAPHIC X9 */ 
    public void Var499()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with VARCHAR X0 */ 
    public void Var500()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,BLOB=ABCDABCD",
	      "Hello,Data type mismatch",
              "null,null"};

           String[] testArrayNative = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with VARGRAPHIC X1 */ 
    public void Var501()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,BLOB=ABCDABCD",
	      "Hello,Data type mismatch",
              "null,null"};

           String[] testArrayNative = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,Data type mismatch",
	      "Hello,Data type mismatch",
              "null,null"};


	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Blob 49X with BINARY X2 */ 
    public void Var502()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,BLOB=ABCDABCD000000000000",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with VARBINARY X3 */ 
    public void Var503()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,BLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with DATE X4 */ 
    public void Var504()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with TIME X5 */ 
    public void Var505()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Blob 49X with TIMESTAMP X6 */ 
    public void Var506()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Blob 49X with CLOB X7 */ 
    public void Var507()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,BLOB=ABCDABCD",
	      "Hello,Data type mismatch",
              "null,null"};


           String[] testArrayNative = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,Data type mismatch",
	      "Hello,Data type mismatch",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Blob 49X with DBCLOB X8 */ 
    public void Var508()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,BLOB=ABCDABCD",
	      "Hello,Data type mismatch",
              "null,null"};



           String[] testArrayNative = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,Data type mismatch",
	      "Hello,Data type mismatch",
              "null,null"};


	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Blob 49X with BLOB X9 */ 
    public void Var509()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,BLOB=ABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Blob 49X with ROWID X0 */ 
    public void Var510()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,BLOB=ABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.sql.Blob 49X with NCHAR X1 */ 
    public void Var511()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCDD,BLOB=ABCDDABCDD",
              "null,null"};

           
           String[] testArrayNative = {
               "java.sql.Blob",
               "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
         "ABCDD,Data type mismatch",
               "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }

 
    /* Test java.sql.Blob 49X with NVARCHAR X2 */ 
    public void Var512()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,BLOB=ABCDABCD",
              "null,null"};

           String[] testArrayNative = {
               "java.sql.Blob",
               "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
         "ABCD,Data type mismatch",
               "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Blob 49X with NCLOB X3 */ 
    public void Var513()  {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,BLOB=ABCDABCD",
              "null,null"};

           String[] testArrayNative = {
               "java.sql.Blob",
               "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
         "ABCD,Data type mismatch",
               "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Blob 49X with XML X4 */ 
    public void Var514()  {

           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	    if (isToolboxDriver()) {
		String[] testArrayToolbox = {
		    "java.sql.Blob",
		    "call "+JDCSTest.COLLECTION+".csgoXML (?)",
		    "<h>h</h>,BLOB=3C683E683C2F683E",
		    "null,null"};


		    testArray =testArrayToolbox;
	    }

	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Blob 49X with ARRAY X5 */ 
    public void Var515()  {
        String[] testArray = {
            "java.sql.Blob",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    
    /* Test java.sql.Blob 49X with BOOLEAN X2 */ 
    public void Var516()  {
 if (checkBooleanSupport()) {
           String[] testArray = {
              "java.sql.Blob",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};


           testGetObject(testArray,parameterName);
    }
    }

    /* Test java.sql.Blob 49X padd */
    public void Var517()  {  notApplicable("Reserved7 for future use"); }
    public void Var518()  {  notApplicable("Reserved8 for future use"); }
    public void Var519()  {  notApplicable("Reserved9 for future use"); } 

    /* Test java.sql.NClob 52X*/


    /* Test java.sql.NClob 52X with SMALLINT X0 */ 
    public void Var520()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,CLOB=11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with INT X1 */ 
    public void Var521()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,CLOB=11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.sql.NClob 52X with BIGINT X2 */ 
    public void Var522()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,CLOB=11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with REAL X3 */ 
    public void Var523()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,CLOB=11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with FLOAT X4 */ 
    public void Var524()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,CLOB=11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.NClob 52X with DOUBLE X5 */ 
    public void Var525()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,CLOB=11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with DECIMAL X6 */ 
    public void Var526()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,CLOB=11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with NUMERIC X7 */ 
    public void Var527()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,CLOB=11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with CHAR X8 */ 
    public void Var528()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "ABCD,CLOB=ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with GRAPHIC X9 */ 
    public void Var529()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "ABCD,CLOB=ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with VARCHAR X0 */ 
    public void Var530()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with VARGRAPHIC X1 */ 
    public void Var531()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.NClob 52X with BINARY X2 */ 
    public void Var532()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,CLOB=ABCDABCD000000000000",
              "null,null"};
           String[] testArrayNative = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=6162,CLOB=abab\u0000\u0000\u0000\u0000\u0000\u0000",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with VARBINARY X3 */ 
    public void Var533()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,CLOB=ABCDABCD",
              "null,null"};

           String[] testArrayNative = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=5678,CLOB=\u0056\u0078\u0056\u0078",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with DATE X4 */ 
    public void Var534()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,CLOB=2011-09-29",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with TIME X5 */ 
    public void Var535()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,CLOB=12:11:12",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.NClob 52X with TIMESTAMP X6 */ 
    public void Var536()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,CLOB=1990-03-04 10:30:00.010000",
              "null,null"};

           String[] testArrayNative = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,CLOB=1990-03-04-10.30.00.010000",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.NClob 52X with CLOB X7 */ 
    public void Var537()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.NClob 52X with DBCLOB X8 */ 
    public void Var538()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with BLOB X9 */ 
    public void Var539()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,CLOB=ABCD",
              "null,null"};
           
           String[] testArrayNative = {
               "java.sql.NClob",
               "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
         "BYTEARRAY=5678,CLOB=\u0056\u0078",
               "null,null"};
           
           
	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.NClob 52X with ROWID X0 */ 
    public void Var540()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,CLOB=ABCD",
              "null,null"};

           String[] testArrayNative = {
               "java.sql.NClob",
               "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
         "BYTEARRAY=5678,CLOB=\u0056\u0078",
               "null,null"};
            

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }



    /* Test java.sql.NClob 52X with NCHAR X1 */ 
    public void Var541()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCD,CLOB=ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.sql.NClob 52X with NVARCHAR X2 */ 
    public void Var542()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.NClob 52X with NCLOB X3 */ 
    public void Var543()  {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.NClob 52X with XML X4 */ 
    public void Var544()  {

           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,CLOB=<h>h</h>",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.NClob 52X with ARRAY X5 */ 
    public void Var545()  {
        String[] testArray = {
            "java.sql.NClob",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }


    
    /* Test java.sql.NClob 52X with BOOLEAN X2 */ 
    public void Var546()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.sql.NClob",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    }

    /* Test java.sql.NClob 52X padd */
    public void Var547()  {  notApplicable("Reserved7 for future use"); }
    public void Var548()  {  notApplicable("Reserved8 for future use"); }
    public void Var549()  {  notApplicable("Reserved9 for future use"); } 

    /* Test java.sql.Array 55X*/

    /* Test java.sql.Array 55X with SMALLINT X0 */ 
    public void Var550()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with INT X1 */ 
    public void Var551()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.sql.Array 55X with BIGINT X2 */ 
    public void Var552()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with REAL X3 */ 
    public void Var553()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with FLOAT X4 */ 
    public void Var554()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Array 55X with DOUBLE X5 */ 
    public void Var555()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with DECIMAL X6 */ 
    public void Var556()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with NUMERIC X7 */ 
    public void Var557()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with CHAR X8 */ 
    public void Var558()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with GRAPHIC X9 */ 
    public void Var559()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with VARCHAR X0 */ 
    public void Var560()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with VARGRAPHIC X1 */ 
    public void Var561()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Array 55X with BINARY X2 */ 
    public void Var562()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with VARBINARY X3 */ 
    public void Var563()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with DATE X4 */ 
    public void Var564()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with TIME X5 */ 
    public void Var565()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Array 55X with TIMESTAMP X6 */ 
    public void Var566()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Array 55X with CLOB X7 */ 
    public void Var567()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Array 55X with DBCLOB X8 */ 
    public void Var568()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with BLOB X9 */ 
    public void Var569()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Array 55X with ROWID X0 */ 
    public void Var570()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.sql.Array 55X with NCHAR X1 */ 
    public void Var571()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.sql.Array 55X with NVARCHAR X2 */ 
    public void Var572()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Array 55X with NCLOB X3 */ 
    public void Var573()  {
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Array 55X with XML X4 */ 
    public void Var574()  {

           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Array 55X with ARRAY X5 */ 
    public void Var575()  {
        String[] testArray = {
            "java.sql.Array",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],SQLARRAY=java.lang.Integer[5]=[0,1,2,3,4]",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    
    /* Test java.sql.Array 55X with BOOLEAN X2 */ 
    public void Var576()  {
      if (checkBooleanSupport()) { 
           String[] testArray = {
              "java.sql.Array",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }

    /* Test java.sql.Array 55X padd */
    public void Var577()  {  notApplicable("Reserved7 for future use"); }
    public void Var578()  {  notApplicable("Reserved8 for future use"); }
    public void Var579()  {  notApplicable("Reserved9 for future use"); } 


    /* Test java.sql.Ref 58X*/
    /* Test java.sql.Ref 58X with SMALLINT X0 */ 
    public void Var580()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with INT X1 */ 
    public void Var581()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.sql.Ref 58X with BIGINT X2 */ 
    public void Var582()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with REAL X3 */ 
    public void Var583()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with FLOAT X4 */ 
    public void Var584()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Ref 58X with DOUBLE X5 */ 
    public void Var585()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with DECIMAL X6 */ 
    public void Var586()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with NUMERIC X7 */ 
    public void Var587()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with CHAR X8 */ 
    public void Var588()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with GRAPHIC X9 */ 
    public void Var589()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with VARCHAR X0 */ 
    public void Var590()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with VARGRAPHIC X1 */ 
    public void Var591()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Ref 58X with BINARY X2 */ 
    public void Var592()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with VARBINARY X3 */ 
    public void Var593()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with DATE X4 */ 
    public void Var594()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with TIME X5 */ 
    public void Var595()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Ref 58X with TIMESTAMP X6 */ 
    public void Var596()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Ref 58X with CLOB X7 */ 
    public void Var597()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Ref 58X with DBCLOB X8 */ 
    public void Var598()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.Ref 58X with BLOB X9 */ 
    public void Var599()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Ref 58X with ROWID X0 */ 
    public void Var600()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.sql.Ref 58X with NCHAR X1 */ 
    public void Var601()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.sql.Ref 58X with NVARCHAR X2 */ 
    public void Var602()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Ref 58X with NCLOB X3 */ 
    public void Var603()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Ref 58X with XML X4 */ 
    public void Var604()  {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,Data type mismatch"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.Ref 58X with ARRAY X5 */ 
    public void Var605()  {
        String[] testArray = {
            "java.sql.Ref",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,Data type mismatch"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    
    /* Test java.sql.Ref 58X with BOOLEAN X2 */ 
    public void Var606()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.sql.Ref",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,Data type mismatch"};
           testGetObject(testArray,parameterName);
    }
    }

    /* Test java.sql.Ref 58X padd */
    public void Var607()  {  notApplicable("Reserved7 for future use"); }
    public void Var608()  {  notApplicable("Reserved8 for future use"); }
    public void Var609()  {  notApplicable("Reserved9 for future use"); } 


    /* Test java.net.URL 61X*/
    /* Test java.net.URL 61X with SMALLINT X0 */ 
    public void Var610()  {

           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,null"};


           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with INT X1 */ 
    public void Var611()  {

           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,null"};


           testGetObject(testArray,parameterName);
    }
    /* Test java.net.URL 61X with BIGINT X2 */ 
    public void Var612()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with REAL X3 */ 
    public void Var613()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with FLOAT X4 */ 
    public void Var614()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,null"};


           testGetObject(testArray,parameterName);
    }


    /* Test java.net.URL 61X with DOUBLE X5 */ 
    public void Var615()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with DECIMAL X6 */ 
    public void Var616()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with NUMERIC X7 */ 
    public void Var617()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,null"};


           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with CHAR X8 */ 
    public void Var618()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoCHARM (?)",
	      "http://a.b,http://a.b",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with GRAPHIC X9 */ 
    public void Var619()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoGRPHCM (?)",
	      "http://a.b,http://a.b",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with VARCHAR X0 */ 
    public void Var620()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoVARCM (?)",
	      "http://a.b,http://a.b",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with VARGRAPHIC X1 */ 
    public void Var621()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoVARGM (?)",
	      "http://a.b,http://a.b",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.net.URL 61X with BINARY X2 */ 
    public void Var622()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};


           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with VARBINARY X3 */ 
    public void Var623()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with DATE X4 */ 
    public void Var624()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with TIME X5 */ 
    public void Var625()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }


    /* Test java.net.URL 61X with TIMESTAMP X6 */ 
    public void Var626()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }


    /* Test java.net.URL 61X with CLOB X7 */ 
    public void Var627()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoCLOBM (?)",
	      "http://a.b,http://a.b",
	      "ABCD,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }


    /* Test java.net.URL 61X with DBCLOB X8 */ 
    public void Var628()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoDBCLBM (?)",
	      "http://a.b,http://a.b",
	      "ABCD,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }

    /* Test java.net.URL 61X with BLOB X9 */ 
    public void Var629()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
    }


    /* Test java.net.URL 61X with ROWID X0 */ 
    public void Var630()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      // Updated 01/24/2012 for  5363T
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};

           String[] testArrayNative = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }



    /* Test java.net.URL 61X with NCHAR X1 */ 
    public void Var631()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoNCHARM (?)",
	      "http://a.b,http://a.b",
	      "ABCD,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }

 
    /* Test java.net.URL 61X with NVARCHAR X2 */ 
    public void Var632()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoNVARCM (?)",
	      "http://a.b,http://a.b",
	      "ABCD,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.net.URL 61X with NCLOB X3 */ 
    public void Var633()  {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoNCLOBM (?)",
	      "http://a.b,http://a.b",
	      "ABCD,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.net.URL 61X with XML X4 */ 
    public void Var634()  {

           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};


           String[] testArrayNative = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 


	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.net.URL 61X with ARRAY X5 */ 
    public void Var635()  {
        String[] testArray = {
            "java.net.URL",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    
    /* Test java.net.URL 61X with BOOLEAN X2 */ 
    public void Var636()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.net.URL",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};

           testGetObject(testArray,parameterName);
      }
    }

    /* Test java.net.URL 61X padd */
    public void Var637()  {  notApplicable("Reserved7 for future use"); }
    public void Var638()  {  notApplicable("Reserved8 for future use"); }
    public void Var639()  {  notApplicable("Reserved9 for future use"); } 

    /* Test object 64X*/

    /* Test java.lang.Object 64X with SMALLINT X0 */ 
    public void Var640()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with INT X1 */ 
    public void Var641()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.lang.Object 64X with BIGINT X2 */ 
    public void Var642()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,11",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with REAL X3 */ 
    public void Var643()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with FLOAT X4 */ 
    public void Var644()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Object 64X with DOUBLE X5 */ 
    public void Var645()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,11.0",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with DECIMAL X6 */ 
    public void Var646()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with NUMERIC X7 */ 
    public void Var647()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,11.00",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with CHAR X8 */ 
    public void Var648()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "ABCD,ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with GRAPHIC X9 */ 
    public void Var649()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "ABCD,ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with VARCHAR X0 */ 
    public void Var650()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with VARGRAPHIC X1 */ 
    public void Var651()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Object 64X with BINARY X2 */ 
    public void Var652()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,BYTEARRAY=ABCDABCD000000000000",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with VARBINARY X3 */ 
    public void Var653()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,BYTEARRAY=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with DATE X4 */ 
    public void Var654()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,2011-09-29",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with TIME X5 */ 
    public void Var655()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,12:11:12",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Object 64X with TIMESTAMP X6 */ 
    public void Var656()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,1990-03-04 10:30:00.01",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Object 64X with CLOB X7 */ 
    public void Var657()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Object 64X with DBCLOB X8 */ 
    public void Var658()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with BLOB X9 */ 
    public void Var659()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,BLOB=ABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Object 64X with ROWID X0 */ 
    public void Var660()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,ROWID=ABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.lang.Object 64X with NCHAR X1 */ 
    public void Var661()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCD,ABCDABCD  ",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

 
    /* Test java.lang.Object 64X with NVARCHAR X2 */ 
    public void Var662()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Object 64X with NCLOB X3 */ 
    public void Var663()  {
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,CLOB=ABCDABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.lang.Object 64X with XML X4 */ 
    public void Var664()  {

	/* Changed 3/10/2017 */
	/* Result for toolbox with JDK 8 */ 
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,SQLXML=<h>h</h>",
              "null,null"};


	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }

    /* Test java.lang.Object 64X with ARRAY X5 */ 
    public void Var665()  {
        String[] testArray = {
            "java.lang.Object",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],SQLARRAY=java.lang.Integer[5]=[0,1,2,3,4]",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }
    
    
    /* Test java.lang.Object 64X with BOOLEAN X2 */ 
    public void Var666()  {
      if (checkBooleanSupport()) { 
           String[] testArray = {
              "java.lang.Object",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,true",
        "0,false",
        "true,true",
        "false,false",
        "T,true",
        "F,false",
        "Yes,true",
        "No,false",
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }


    /* Test java.lang.Object 64X padd */
     public void Var667()  {  notApplicable("Reserved7 for future use"); }
    public void Var668()  {  notApplicable("Reserved8 for future use"); }
    public void Var669()  {  notApplicable("Reserved9 for future use"); } 


    /* Test java.sql.RowId 67X*/

    /* Test java.sql.RowId 67X with SMALLINT X0 */ 
    public void Var670()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with INT X1 */ 
    public void Var671()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.sql.RowId 67X with BIGINT X2 */ 
    public void Var672()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with REAL X3 */ 
    public void Var673()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with FLOAT X4 */ 
    public void Var674()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.RowId 67X with DOUBLE X5 */ 
    public void Var675()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with DECIMAL X6 */ 
    public void Var676()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with NUMERIC X7 */ 
    public void Var677()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with CHAR X8 */ 
    public void Var678()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoCHAR (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with GRAPHIC X9 */ 
    public void Var679()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoGRAPHIC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with VARCHAR X0 */ 
    public void Var680()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoVARC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with VARGRAPHIC X1 */ 
    public void Var681()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoVARG (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.RowId 67X with BINARY X2 */ 
    public void Var682()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           

           String[] testArrayNative = {
               "java.sql.RowId",
               "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
               "BYTEARRAY=ABCD,ROWID=ABCDABCD000000000000",
               "null,null"};
            

           
	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with VARBINARY X3 */ 
    public void Var683()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};

           String[] testArrayNative = {
               "java.sql.RowId",
               "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
               "BYTEARRAY=ABCD,ROWID=ABCDABCD",
               "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with DATE X4 */ 
    public void Var684()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with TIME X5 */ 
    public void Var685()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.RowId 67X with TIMESTAMP X6 */ 
    public void Var686()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.RowId 67X with CLOB X7 */ 
    public void Var687()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.RowId 67X with DBCLOB X8 */ 
    public void Var688()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoDBCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.RowId 67X with BLOB X9 */ 
    public void Var689()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};

           String[] testArrayNative = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,ROWID=ABCD",
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.RowId 67X with ROWID X0 */ 
    public void Var690()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,ROWID=ABCD",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.sql.RowId 67X with NCHAR X1 */ 
    public void Var691()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoNCHAR (?)",
	      "ABCDE,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }

 
    /* Test java.sql.RowId 67X with NVARCHAR X2 */ 
    public void Var692()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoNVARC (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.sql.RowId 67X with NCLOB X3 */ 
    public void Var693()  {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoNCLOB (?)",
	      "ABCD,Data type mismatch",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.sql.RowId 67X with XML X4 */ 
    public void Var694()  {

           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,Data type mismatch",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.RowId 67X with ARRAY X5 */ 
    public void Var695()  {
        String[] testArray = {
            "java.sql.RowId",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }
    
    
    /* Test java.sql.RowId 67X with BOOLEAN X2 */ 
    public void Var696()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.sql.RowId",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }


    /* Test java.sql.RowId 67X padd */
    public void Var697()  {  notApplicable("Reserved7 for future use"); }
    public void Var698()  {  notApplicable("Reserved8 for future use"); }
    public void Var699()  {  notApplicable("Reserved9 for future use"); } 

    /* Test java.sql.SQLXML 70X*/
    /* Test java.sql.SQLXML 70X with SMALLINT X0 */ 
    public void Var700()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoSMLINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with INT X1 */ 
    public void Var701()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }
    /* Test java.sql.SQLXML 70X with BIGINT X2 */ 
    public void Var702()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoBIGINT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with REAL X3 */ 
    public void Var703()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoREAL (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with FLOAT X4 */ 
    public void Var704()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoFLOAT (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.SQLXML 70X with DOUBLE X5 */ 
    public void Var705()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoDOUBLE (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with DECIMAL X6 */ 
    public void Var706()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoDEC (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with NUMERIC X7 */ 
    public void Var707()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoNUM (?)",
	      "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with CHAR X8 */ 
    public void Var708()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoCHARM (?)",
	      "ABCD,Data type mismatch",
	      // Updated 1/24/2012 for 5463T (trimmed spaces) 
	      "<a>a</a>,SQLXML=<a>a</a>", 
              "null,null"};


           String[] testArrayNative = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoCHARM (?)",
	      "ABCD,Data type mismatch",
	      "<a>a</a>,SQLXML=<a>a</a>", 
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 


           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with GRAPHIC X9 */ 
    public void Var709()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoGRPHCM (?)",
	      "ABCD,Data type mismatch",
	      // Updated 1/24/2012 for 5463T (trimmed spaces) 
	      "<a>a</a>,SQLXML=<a>a</a>", 
              "null,null"};

           String[] testArrayNative = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoGRPHCM (?)",
	      "ABCD,Data type mismatch",
	      "<a>a</a>,SQLXML=<a>a</a>", 
              "null,null"};

	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  testArray = testArrayNative; 
	      } 

           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with VARCHAR X0 */ 
    public void Var710()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoVARCM (?)",
	      "ABCD,Data type mismatch",
	      "<a>a</a>,SQLXML=<a>a</a>", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with VARGRAPHIC X1 */ 
    public void Var711()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoVARGM (?)",
	      "ABCD,Data type mismatch",
	      "<a>a</a>,SQLXML=<a>a</a>", 
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.SQLXML 70X with BINARY X2 */ 
    public void Var712()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoBIN (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with VARBINARY X3 */ 
    public void Var713()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoVARB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with DATE X4 */ 
    public void Var714()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoDATE (?)",
	      "2011-09-28,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with TIME X5 */ 
    public void Var715()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoTIME (?)",
	      "10:11:12,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.SQLXML 70X with TIMESTAMP X6 */ 
    public void Var716()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoTS (?)",
	      "1990-03-02 08:30:00.010000,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.SQLXML 70X with CLOB X7 */ 
    public void Var717()  {


           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoCLOBM (?)",
/* 	      "ABCD,Data type mismatch", */
/* native throws mismatch, toolbox doesn't */

	      "<h>h</h>,SQLXML=<h>h</h>",
              "null,null"};


           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.SQLXML 70X with DBCLOB X8 */ 
    public void Var718()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoDBCLBM (?)",
	      /* "ABCD,Data type mismatch", */ 
	      "<h>h</h>,SQLXML=<h>h</h>",
              "null,null"};
           testGetObject(testArray,parameterName);
    }

    /* Test java.sql.SQLXML 70X with BLOB X9 */ 
    public void Var719()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoBLOB (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.SQLXML 70X with ROWID X0 */ 
    public void Var720()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoROWID (?)",
	      "BYTEARRAY=ABCD,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
    }



    /* Test java.sql.SQLXML 70X with NCHAR X1 */ 
    public void Var721()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoNCHARM (?)",
	      "ABCD,Data type mismatch",
	      "<h>h</h>,SQLXML=<h>h</h>",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }

 
    /* Test java.sql.SQLXML 70X with NVARCHAR X2 */ 
    public void Var722()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoNVARCM (?)",
	      "ABCD,Data type mismatch",
	      "<h>h</h>,SQLXML=<h>h</h>",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.sql.SQLXML 70X with NCLOB X3 */ 
    public void Var723()  {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoNCLOBM (?)",
/* 	      "ABCD,Data type mismatch", */
	      "<h>h</h>,SQLXML=<h>h</h>",
              "null,null"};
	    if (checkRelease710())             testGetObject(testArray,parameterName);
    }


    /* Test java.sql.SQLXML 70X with XML X4 */ 
    public void Var724()  {

           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoXML (?)",
	      "<h>h</h>,SQLXML=<h>h</h>",
              "null,null"};
	if (checkRelease710()) 
           testGetObject(testArray,parameterName);
    }


    /* Test java.sql.SQLXML 70X with ARRAY X5 */ 
    public void Var725()  {
        String[] testArray = {
            "java.sql.SQLXML",
            "call "+JDCSTest.COLLECTION+".csgoARINT (?)",
            "SQLARRAY=[Integer:1:2:3],Data type mismatch",
            "null,null"};
	if (checkRelease710()) 
            testGetObject(testArray,parameterName);
    }

    
    /* Test java.sql.SQLXML 70X with BOOLEAN X2 */ 
    public void Var726()  {
      if (checkBooleanSupport()) {
           String[] testArray = {
              "java.sql.SQLXML",
              "call "+JDCSTest.COLLECTION+".csgoBOOLEAN (?)",
        "1,Data type mismatch",
              "null,null"};
           testGetObject(testArray,parameterName);
      }
    }


    /* Test java.sql.SQLXML 70X padd */
    public void Var727()  {  notApplicable("Reserved7 for future use"); }
    public void Var728()  {  notApplicable("Reserved8 for future use"); }
    public void Var729()  {  notApplicable("Reserved9 for future use"); } 

   /* Test java.time.LocalDate 71X*/

  /* Test java.time.LocalDate 71X with SMALLINT X0 */
  public void Var730() {
    if (checkJdbc42()) {


      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoSMLINT (?)",
          "1,Data type mismatch", "null,null" };


      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with INT X1 */
  public void Var731() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoINT (?)",
          "1,Data type mismatch", "null,null" };

      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with BIGINT X2 */
  public void Var732() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoBIGINT (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with REAL X3 */
  public void Var733() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoREAL (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with FLOAT X4 */
  public void Var734() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoFLOAT (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with DOUBLE X5 */
  public void Var735() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoDOUBLE (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with DECIMAL X6 */
  public void Var736() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoDEC (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with NUMERIC X7 */
  public void Var737() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoNUM (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with CHAR X8 */
  public void Var738() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoCHARM (?)",
          "2011-09-01,2011-09-01", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with GRAPHIC X9 */
  public void Var739() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoGRPHCM (?)",
          "Bogus,Data type mismatch", "2011-09-01,2011-09-01", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with VARCHAR X0 */
  public void Var740() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoVARCM (?)",
          "Bogus,Data type mismatch", "2011-09-01,2011-09-01", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with VARGRAPHIC X1 */
  public void Var741() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoVARGM (?)",
          "Bogus,Data type mismatch", "2011-09-01,2011-09-01", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with BINARY X2 */
  public void Var742() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoBIN (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with VARBINARY X3 */
  public void Var743() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoVARB (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with DATE X4 */
  public void Var744() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoDATE (?)",
          "2011-09-28,2011-09-29", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with TIME X5 */
  public void Var745() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoTIME (?)",
          "10:11:12,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with TIMESTAMP X6 */
  public void Var746() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoTS (?)",
          "1990-03-02 08:30:00.010000,1990-03-04", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with CLOB X7 */
  public void Var747() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoCLOBM (?)",
          "Bogus,Data type mismatch", "1990-03-02,Data type mismatch",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with DBCLOB X8 */
  public void Var748() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoDBCLBM (?)",
          "Bogus,Data type mismatch", "1990-03-02,Data type mismatch",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with BLOB X9 */
  public void Var749() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoBLOB (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with ROWID X0 */
  public void Var750() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoROWID (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with NCHAR X1 */
  public void Var751() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoNCHARM (?)",
          "Bogus,Data type mismatch", "1990-03-02,1990-03-02", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with NVARCHAR X2 */
  public void Var752() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoNVARCM (?)",
          "Bogus,Data type mismatch", "1990-03-02,1990-03-02", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with NCLOB X3 */
  public void Var753() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoNCLOBM (?)",
          "Bogus,Data type mismatch", "1990-03-02,Data type mismatch",
          "null,null" };
      if (checkRelease710())
        testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with XML X4 */
  public void Var754() {
    if (checkJdbc42()) {

      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoXML (?)",
          "<h>h</h>,Data type mismatch", "null,null" };
      if (checkRelease710())
        testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with ARRAY X5 */
  public void Var755() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDate",
          "call " + JDCSTest.COLLECTION + ".csgoARINT (?)",
          "SQLARRAY=[Integer:1:2:3],Data type mismatch", "null,null" };
      if (checkRelease710())
        testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDate 71X with BIGINT X2 */
  public void Var756() {
    if (checkJdbc42()) {
      if (checkBooleanSupport()) {
        String[] testArray = { "java.time.LocalDate",
            "call " + JDCSTest.COLLECTION + ".csgoBOOLEAN (?)",
            "1,Data type mismatch", "null,null" };
        testGetObject(testArray, parameterName);
      }
    }
  }

  /* Test java.time.LocalDate 71X padd */
  public void Var757() {
    notApplicable("Reserved7 for future use");
  }

  public void Var758() {
    notApplicable("Reserved8 for future use");
  }

  public void Var759() {
    notApplicable("Reserved9 for future use");
  }

  /* Test java.time.LocalTime 72X */

  /* Test java.time.LocalTime 72X with SMALLINT X0 */
  public void Var760() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoSMLINT (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with INT X1 */
  public void Var761() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoINT (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with BIGINT X2 */
  public void Var762() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoBIGINT (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with REAL X3 */
  public void Var763() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoREAL (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with FLOAT X4 */
  public void Var764() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoFLOAT (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with DOUBLE X5 */
  public void Var765() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoDOUBLE (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with DECIMAL X6 */
  public void Var766() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoDEC (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with NUMERIC X7 */
  public void Var767() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoNUM (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with CHAR X8 */
  public void Var768() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoCHARM (?)",
          "Bogus,Data type mismatch", "11:22:33,11:22:33", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with GRAPHIC X9 */
  public void Var769() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoGRPHCM (?)",
          "Bogus,Data type mismatch", "11:22:33,11:22:33", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with VARCHAR X0 */
  public void Var770() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoVARCM (?)",
          "Bogus,Data type mismatch", "11:22:33,11:22:33", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with VARGRAPHIC X1 */
  public void Var771() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoVARGM (?)",
          "Bogus,Data type mismatch", "11:22:33,11:22:33", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with BINARY X2 */
  public void Var772() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoBIN (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with VARBINARY X3 */
  public void Var773() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoVARB (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with DATE X4 */
  public void Var774() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoDATE (?)",
          "2011-09-28,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with TIME X5 */
  public void Var775() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoTIME (?)", "10:11:12,12:11:12",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with TIMESTAMP X6 */
  public void Var776() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoTS (?)",
          "1990-03-02 08:30:01.010000,10:30:01", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with CLOB X7 */
  public void Var777() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoCLOBM (?)",
          "Bogus,Data type mismatch", "11:22:33,Data type mismatch",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with DBCLOB X8 */
  public void Var778() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoDBCLBM (?)",
          "Bogus,Data type mismatch", "11:22:33,Data type mismatch",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with BLOB X9 */
  public void Var779() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoBLOB (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with ROWID X0 */
  public void Var780() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoROWID (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with NCHAR X1 */
  public void Var781() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoNCHARM (?)",
          "Bogus,Data type mismatch", "11:22:33,11:22:33", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with NVARCHAR X2 */
  public void Var782() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoNVARCM (?)",
          "Bogus,Data type mismatch", "11:22:33,11:22:33", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with NCLOB X3 */
  public void Var783() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoNCLOBM (?)",
          "Bogus,Data type mismatch", "11:22:33,Data type mismatch",
          "null,null" };
      if (checkRelease710())
        testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with XML4 */
  public void Var784() {

    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoXML (?)",
          "<h>h</h>,Data type mismatch", "null,null" };

      if (checkRelease710())
        testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with ARRAY X5 */
  public void Var785() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalTime",
          "call " + JDCSTest.COLLECTION + ".csgoARINT (?)",
          "SQLARRAY=[Integer:1:2:3],Data type mismatch", "null,null" };
      if (checkRelease710())
        testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalTime 72X with BOOLEAN X2 */
  public void Var786() {
    if (checkBooleanSupport()) {
      if (checkJdbc42()) {
        String[] testArray = { "java.time.LocalTime",
            "call " + JDCSTest.COLLECTION + ".csgoBOOLEAN (?)",
            "1,Data type mismatch", "null,null" };
        testGetObject(testArray, parameterName);
      }
    }
  }

  /* Test java.time.LocalTime 72X padd */
  public void Var787() {
    notApplicable("Reserved7 for future use");
  }

  public void Var788() {
    notApplicable("Reserved8 for future use");
  }

  public void Var789() {
    notApplicable("Reserved9 for future use");
  }

  /* Test java.time.LocalDateTime 73X */
  /* Test java.time.LocalDateTime 73X with SMALLINT X0 */
  public void Var790() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoSMLINT (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with INT X1 */
  public void Var791() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoINT (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with BIGINT X2 */
  public void Var792() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoBIGINT (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with REAL X3 */
  public void Var793() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoREAL (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with FLOAT X4 */
  public void Var794() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoFLOAT (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with DOUBLE X5 */
  public void Var795() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoDOUBLE (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with DECIMAL X6 */
  public void Var796() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoDEC (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with NUMERIC X7 */
  public void Var797() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoNUM (?)",
          "1,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with CHAR X8 */
  public void Var798() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoCHAR26 (?)",
          "2011-09-01 11:22:01.012345,2011-09-01T11:22:01.012345",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with GRAPHIC X9 */
  public void Var799() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoGRPHC26 (?)",
          "Bogus,Data type mismatch",
          "2011-09-01 11:22:01.012345,2011-09-01T11:22:01.012345",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with VARCHAR X0 */
  public void Var800() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoVARCM (?)",
          "Bogus,Data type mismatch",
          "2011-09-01 11:22:01.012345,2011-09-01T11:22:01.012345",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with VARGRAPHIC X1 */
  public void Var801() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoVARGM (?)",
          "Bogus,Data type mismatch",
          "2011-09-01 11:22:01.012345,2011-09-01T11:22:01.012345",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with BINARY X2 */
  public void Var802() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoBIN (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with VARBINARY X3 */
  public void Var803() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoVARB (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with DATE X4 */
  public void Var804() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoDATE (?)",
          "2011-09-01,2011-09-02T00:00", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with TIME X5 */
  public void Var805() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoTIME (?)",
          "10:11:12,1970-01-01T12:11:12", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with TIMESTAMP X6 */
  public void Var806() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoTS (?)",
          "1990-03-02 08:30:00.010000,1990-03-04T10:30:00.010", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with CLOB X7 */
  public void Var807() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoCLOBM (?)",
          "Bogus,Data type mismatch", "1990-03-02,Data type mismatch",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with DBCLOB X8 */
  public void Var808() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoDBCLBM (?)",
          "Bogus,Data type mismatch", "1990-03-02,Data type mismatch",
          "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with BLOB X9 */
  public void Var809() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoBLOB (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with ROWID X0 */
  public void Var810() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoROWID (?)",
          "BYTEARRAY=ABCD,Data type mismatch", "null,null" };
      testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with NCHAR X1 */
  public void Var811() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoNCHARM (?)",
          "Bogus,Data type mismatch",
          "2011-09-01 11:22:01.012345,2011-09-01T11:22:01.012345",
          "null,null" };
	  if (checkRelease710()) {
	      testGetObject(testArray, parameterName);
	  }
    }
  }

  /* Test java.time.LocalDateTime 73X with NVARCHAR X2 */
  public void Var812() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoNVARCM (?)",
          "Bogus,Data type mismatch",
          "2011-09-01 11:22:01.012345,2011-09-01T11:22:01.012345",
          "null,null" };
      if (checkRelease710())
        testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with NCLOB X3 */
  public void Var813() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoNCLOBM (?)",
          "Bogus,Data type mismatch",
          "2011-09-01 11:22:01.012345,Data type mismatch", "null,null" };
      if (checkRelease710())
        testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with XML X4 */
  public void Var814() {

    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoXML (?)",
          "<h>h</h>,Data type mismatch", "null,null" };
      if (checkRelease710())
        testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with ARRAY X5 */
  public void Var815() {
    if (checkJdbc42()) {
      String[] testArray = { "java.time.LocalDateTime",
          "call " + JDCSTest.COLLECTION + ".csgoARINT (?)",
          "SQLARRAY=[Integer:1:2:3],Data type mismatch", "null,null" };
      if (checkRelease710())
        testGetObject(testArray, parameterName);
    }
  }

  /* Test java.time.LocalDateTime 73X with BOOLEAN X2 */
  public void Var816() {
    if (checkBooleanSupport()) {
      if (checkJdbc42()) {
        String[] testArray = { "java.time.LocalDateTime",
            "call " + JDCSTest.COLLECTION + ".csgoBOOLEAN (?)",
            "1,Data type mismatch", "null,null" };
        testGetObject(testArray, parameterName);
      }
    }
  }

    /* Test java.time.LocalDateTime 73X padd */
    public void Var817()  {  notApplicable("Reserved7 for future use"); }
    public void Var818()  {  notApplicable("Reserved8 for future use"); }
    public void Var819()  {  notApplicable("Reserved9 for future use"); } 



}
