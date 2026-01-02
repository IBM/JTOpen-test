///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSNamed.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSNamed.java
//
// Classes:      JDCSNamed
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDTestcase;



/**
Testcase JDCSNamed.  This tests the use of named parameters.
**/
public class JDCSNamed
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSNamed";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }
  public final static int GETSTRING = 1;
  public final static int GETBYTES  = 2;
  public final static int SETSTRING = 1;
  public final static int SETBYTES  = 2;

  static JDCSNamedTypeInfo typeInfo[] = {
    /* 0 */
new JDCSNamedTypeInfo("SMALLINT",
      SETSTRING,
      GETSTRING,
      "1",
      "1",
      "2",
      "2",
      "2",
      "3",
      "3",
      "3"),
    /* 1 */
new JDCSNamedTypeInfo("INTEGER",
      SETSTRING,
      GETSTRING,
      "11",
      "11",
      "'12'",
      "12",
      "12",
      "'13'",
      "13",
      "13"),
    /* 2 */
new JDCSNamedTypeInfo("BIGINT",
    SETSTRING,
    GETSTRING,
    "21",
    "21",
    "22",
    "22",
    "22",
    "23",
    "23",
    "23"),
    /* 3 */
new JDCSNamedTypeInfo("CHAR(2)",
    SETSTRING,
    GETSTRING,
    "31",
    "31",
    "'32'",
    "32",
    "32",
    "'33'",
    "33",
    "33"),
    
    /* 4 */
new JDCSNamedTypeInfo("REAL",
    SETSTRING,
    GETSTRING,
    "41",
    "4.1E1",
    "42",
    "4.2E1",
    "42.0",
    "43",
    "4.3E1",
    "43.0"),
    /* 5 */
new JDCSNamedTypeInfo("FLOAT",
    SETSTRING,
    GETSTRING,
    "51",
    "5.1E1",
    "52",
    "5.2E1",
    "52.0", 
    "53",
    "5.3E1",
    "53.0"),
    /* 6 */
new JDCSNamedTypeInfo("DOUBLE",
    SETSTRING,
    GETSTRING,
    "61",
    "6.1E1",
    "62",
    "6.2E1",
    "62.0",
    "63",
    "6.3E1",
    "63.0"),
    /* 7 */
new JDCSNamedTypeInfo("DECIMAL(10,2)",
      SETSTRING,
      GETSTRING,
      "71",
      "71.00",
      "72",
      "72.00",
      "72.00",
      "73",
      "73.00",
      "73.00"),
    /* 8 */
      new JDCSNamedTypeInfo("VARCHAR(4096)",
          SETSTRING,
          GETSTRING,
          "81",
          "81",
          "82",
          "82",
          "82",
          "83",
          "83",
          "83"),
    /* 9 */
          new JDCSNamedTypeInfo("DECIMAL(15,5)",
              SETSTRING,
              GETSTRING,
              "91",
              "91.00000",
              "92",
              "92.00000",
              "92.00000",
              "93",
              "93.00000",
              "93.00000"),

    /* 10 */
              new JDCSNamedTypeInfo("CHAR(40)",
                  SETSTRING,
                  GETSTRING,
                  "101",
                  "101                                     ",
                  "'102'",
                  "102                                     ",
                  "102                                     ",
                  "'103'",
                  "103                                     ",
                  "103                                     "),
    /* 11 */
                  new JDCSNamedTypeInfo("VARCHAR(40)",
                      SETSTRING,
                      GETSTRING,
                      "111",
                      "111",
                      "112",
                      "112",
                      "112",
                      "113",
                      "113",
                      "113"),

    /* 12 */
//new JDCSNamedTypeInfo("GRAPHIC(40) CCSID 13488 ",
//      GETSTRING,
//      fixedStringSet ,
//      fixedStringRead),
    /* 13 */
      //new JDCSNamedTypeInfo("VARGRAPHIC(40) CCSID 13488 ",
    //      GETSTRING,
    //   stringSet ,
    //  stringRead),
    /* 14 */
      //new JDCSNamedTypeInfo("DATE",
    //GETSTRING,
    //dateSet ,
    //dateRead),
    /* 15 */
      //new JDCSNamedTypeInfo("TIME",
    //GETSTRING,
      //  timeSet ,
      //timeRead),
    /* 16 */
      //new JDCSNamedTypeInfo("TIMESTAMP",
      //GETSTRING,
      //      timestampSet ,
      //timestampRead),
    /* 17 */
      //new JDCSNamedTypeInfo("CLOB(1000000)",
      //GETSTRING,
      //stringSet ,
      //stringRead),
    /* 18 */
      //new JDCSNamedTypeInfo("DBCLOB(1000000) CCSID 13488 ",
      //GETSTRING,
      //stringSet ,
      //stringRead),
/* 19 */
      //new JDCSNamedTypeInfo("BLOB(1000000)",
      //GETBYTES,
      //blobSet ,
      //blobRead),

    /* 20 */
      //new JDCSNamedTypeInfo("NUMERIC(10,2)",
      //GETSTRING,
      //decimal102Set ,
      //decimal102Read),
    /* 21 */
      //new JDCSNamedTypeInfo("NUMERIC(15,2)",
      //GETSTRING,
      //decimal152Set ,
      //decimal152Read),
    /* 22 */
      //new JDCSNamedTypeInfo("NUMERIC(10,5)",
      //GETSTRING,
      //decimal105Set ,
      //decimal105Read),
    /* 23 */
      //new JDCSNamedTypeInfo("NUMERIC(15,5)",
      //GETSTRING,
      //decimal155Set ,
      //decimal155Read),

    /* 24 */ 
      //new JDCSNamedTypeInfo("VARCHAR(40) FOR BIT DATA",
      //GETBYTES,
      //vcfbSet,
      //vcfbRead),

      /* 25 */ 
      //new JDCSNamedTypeInfo("VARBINARY(40)",
      //GETBYTES,
      //      binarySet,
      //      binaryRead),

              /* 26 */ 
      //new JDCSNamedTypeInfo("BINARY(40)",
      //GETBYTES,
      //              fixedBinarySet,
      //              fixedBinaryRead),

    /* 27 */
      //new JDCSNamedTypeInfo("DECIMAL(31,4)",
      //GETSTRING,
      //decimal314Set ,
      //decimal314Read),

//
//reserved for expansion
//

    /* 28 */ 
      //new JDCSNamedTypeInfo("INTEGER",
      //GETSTRING,
      //intSet,
      //intRead),


};
// these ID's must match the indexes above
public static final int T_SMALLINT = 0;
public static final int T_INTEGER  = 1;
public static final int T_BIGINT   = 2;
public static final int T_CHAR2    = 3;
public static final int T_REAL     = 4;
public static final int T_FLOAT    = 5;
public static final int T_DOUBLE   = 6;
public static final int T_DECIMAL102 = 7;
public static final int T_VARCHAR4096 = 8;
public static final int T_DECIMAL155 = 9;
public static final int T_CHAR       = 10;
public static final int T_VARCHAR    = 11;
public static final int T_GRAPHIC    = 12;
public static final int T_VARGRAPHIC = 13;
public static final int T_DATE       = 14;
public static final int T_TIME       = 15;
public static final int T_TIMESTAMP  = 16;
public static final int T_CLOB       = 17;
public static final int T_DBCLOB     = 18;
public static final int T_BLOB       = 19;
public static final int T_NUMERIC102 = 20;
public static final int T_NUMERIC152 = 21;
public static final int T_NUMERIC105 = 22;
public static final int T_NUMERIC155 = 23;
public static final int T_VARCHARFBD = 24;
public static final int T_VARBINARY  = 25;
public static final int T_BINARY     = 26;
public static final int T_DECIMAL314 = 27; 
public static final int T_COUNT      = 28;

public static int T1 = typeInfo.length;
public static int T2 = T1 * T1;
public static int T3 = T2 * T1;
public static int T4 = T3 * T1;

  
public static String[] directionString = {
  "IN",
  "OUT",
  "INOUT"
}; 
public static final int D_INPUT = 0; 
public static final int D_OUTPUT = 1;
public static final int D_INPUTOUTPUT = 2;
public static final int D_COUNT = 3; 
public static final int D1 = 3; 
public static final int D2 = D1 * D1; 
public static final int D3 = D2 * D1; 
public static final int D4 = D3 * D1; 
  
  
  
  

    boolean verbose = false;


  /**
   * Constructor.
   **/
  public JDCSNamed(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,  String password) {
    super(systemObject, "JDCSNamed", namesAndVars, runMode, fileOutputStream,
 password);
  }

  public JDCSNamed(AS400 systemObject, String testname, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
 password);
  }

/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.

SQL400 - Removed the package priming code - we won't be using it. B1A
**/
    protected void setup ()
        throws Exception
    {
        connection_ = testDriver_.getConnection (baseURL_
                                                 + ";errors=full", userId_, encryptedPassword_);
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
	if (connection_ != null) { 
	    connection_.close ();
	    connection_ = null; 
	}
    }






/**
Test Jesse Gorzinski's error case 
**/
  public void Var001()
  {
      StringBuffer sb = new StringBuffer();
      String procName = collection_+".JDCSNMD001";
      String sql = null; 
	  try {
	    boolean passed = true; 

	      Statement stmt = connection_.createStatement();
	      sql = "CREATE OR REPLACE PROCEDURE "+procName+" (IN ID INTEGER , IN NAME CHARACTER (10) , IN OPT1 VARCHAR (100) DEFAULT 'xyz' , IN OPT2 VARCHAR (100) DEFAULT 'abc' , OUT OUT_TOTAL INTEGER ) MODIFIES SQL DATA CONCURRENT ACCESS RESOLUTION DEFAULT DYNAMIC RESULT SETS 0 OLD SAVEPOINT LEVEL COMMIT ON RETURN NO DISALLOW DEBUG MODE BEGIN DECLARE VARIABLE1 INTEGER ; SET VARIABLE1 = ID * 5 ;        SET OUT_TOTAL = VARIABLE1 ;         END ";
	      sb.append("Executing "+sql+"\n"); 
	      stmt.executeUpdate(sql);

	      sql = "CALL "+procName+"(?,?,OUT_TOTAL => ?)";
	      sb.append("Preparing "+sql+"\n"); 
	      CallableStatement cs = connection_.prepareCall(sql);
	      cs.setInt(1,10);
	      cs.setString(2,"hi");
	      cs.registerOutParameter(3,Types.INTEGER);
	      sb.append("Executing "+sql+"\n"); 
	      cs.execute();
	      int answer = cs.getInt(3);
	      if (answer != 50) {
		  passed = false;
		  sb.append("Got "+answer+" sb 50\n"); 
	      } 

	      stmt.close();
	      cs.close();

	      assertCondition (passed,sb);	
	  }
	  catch(Exception e)
	  {
	      failed (e, "Unexpected Exception  "+sb.toString());
	  }
  }

 
  public void runTest(int outputPosition, 
      int parameterCount,
      long parameterTypes,
      long parameterDirections,
      String[] checkCombinations) {
      StringBuffer sb = new StringBuffer(); 

      boolean passed = runTestVariation(outputPosition, parameterCount, parameterTypes, parameterDirections, checkCombinations, sb);
      assertCondition(passed, sb); 
  }
  
  public void runTestRS( 
      int parameterCount,
      long parameterTypes,
      long parameterDirections) {
      StringBuffer sb = new StringBuffer(); 

      boolean passed = runTestVariationRS(parameterCount, parameterTypes, parameterDirections, sb);
      assertCondition(passed, sb); 
  }
  
  
  public void runRandomTests(int parameterCount, long runMilliseconds) {
      StringBuffer sb = new StringBuffer(); 
    boolean passed = true; 
    long endTime = System.currentTimeMillis() + runMilliseconds;
    while (passed && (System.currentTimeMillis() < endTime)) {
      sb = new StringBuffer(); 
      passed = runRandomTest(parameterCount, sb); 
    }
    assertCondition(passed, sb); 
  }

  
  
  
  public boolean runRandomTest(int parameterCount, StringBuffer sb) {
     boolean passed; 
     Random random = new Random(System.currentTimeMillis());

     int outputPosition = 1 + random.nextInt(parameterCount+1); 
     int parameterTypes = 0; 
     int parameterDirections = 0; 

     for (int i = 1; i <= parameterCount; i++) { 
       parameterTypes = parameterTypes * T1 + random.nextInt(T1); 
       parameterDirections = parameterDirections * D1 + random.nextInt(D1); 
     }
     
     passed = runTestVariation(outputPosition, parameterCount, parameterTypes, parameterDirections, null, sb);
     
     return passed; 
  }

  public void runRandomTestsRS(int parameterCount, long runMilliseconds) {
    boolean passed = true;
    StringBuffer sb = new StringBuffer(); 
    long endTime = System.currentTimeMillis() + runMilliseconds;
    while (passed && (System.currentTimeMillis() < endTime)) {
      sb = new StringBuffer(); 
      passed = runRandomTestRS(parameterCount, sb); 
    }
    assertCondition(passed, sb); 
  }

  public boolean runRandomTestRS(int parameterCount, StringBuffer sb) {
    boolean passed; 
    Random random = new Random(System.currentTimeMillis());

    int parameterTypes = 0; 
    int parameterDirections = 0; 

    for (int i = 1; i <= parameterCount; i++) { 
      parameterTypes = parameterTypes * T1 + random.nextInt(T1); 
      parameterDirections = parameterDirections * D1 + random.nextInt(D1); 
    }
    
    passed = runTestVariationRS(parameterCount, parameterTypes, parameterDirections, sb);
    
    return passed; 
 }

  
  
  
  public void runAllTypesTest() {
    boolean passed = true;

    StringBuffer failedSb = new StringBuffer(); 
    for (int i = 0;  i < typeInfo.length; i++) {
      int parameterCount = 3;
      int parameterTypes = i + i * T1 + i * T1*T1; 
      int parameterDirections = D_INPUT + D_OUTPUT * D1 + D_INPUTOUTPUT;

      StringBuffer sb = new StringBuffer(); 
      if (! runTestVariation(1,parameterCount,parameterTypes,parameterDirections, null, sb)) {
        passed = false; 
        failedSb.append(sb);
      } 
      sb.setLength(0); 
    }
    assertCondition(passed, failedSb); 
  }

  
  
  public boolean runTestVariation(int outputPosition, 
                      int parameterCount,
                      long parameterTypes,
                      long parameterDirections,
                      String[] checkCombinations, 
                      StringBuffer sb) {
    boolean[] checkCombinationsFound = null;
    if (checkCombinations != null) { 
      checkCombinationsFound = new boolean[checkCombinations.length];
    }
    sb.append("Running runTestVariation("+outputPosition+","+parameterCount+","+parameterTypes+","+parameterDirections+")\n");
    boolean passed = true; 
     try {
       JDCSNamedTypeInfo thisTestInfo[] = new JDCSNamedTypeInfo[parameterCount+2];
       int               thisDirection[]    = new int[parameterCount+2]; 
       
       String label = "";
       String body = ""; 
       String returnExpression = ""; 
       String procedureName = JDCSTest.COLLECTION + ".JDCS" + parameterCount + "x" + parameterTypes+"x"+parameterDirections;
       String createSQL = "CREATE OR REPLACE Procedure " + procedureName + "(";
       long workingTypes = parameterTypes;
       long workingDirections = parameterDirections; 
       for (int i = 1; i <=  parameterCount+1; i++) {
         int typeId;
         int direction; 
         if (i == outputPosition) {
           typeId = T_VARCHAR4096;
           direction = D_OUTPUT; 
         } else {
           typeId = (int) (workingTypes % typeInfo.length);
           workingTypes = workingTypes / typeInfo.length;
           direction = (int) (workingDirections % D_COUNT); 
           workingDirections = workingDirections / D_COUNT; 
               
         }
         thisTestInfo[i] = typeInfo[typeId];
         thisDirection[i] = direction;  
             
         label += "-" + thisTestInfo[i].sqlName;
         
         
         if (i > 1) {
           createSQL += ",";
         }
         createSQL += " "+directionString[direction]+" P" + i + " " + thisTestInfo[i].sqlName;
        if (i != outputPosition) {
          switch (direction) {
          case D_INPUT:
            // Just add the input to the output
            if (returnExpression.length() == 0) {
              returnExpression = "CONCAT('P"+i+"=', P"+i+")"; 
            } else { 
              returnExpression = "CONCAT("+returnExpression+", CONCAT(', P"+i+"=', P"+i+"))"; 
            }
            createSQL+=" DEFAULT "+thisTestInfo[i].setDefaultValue+" "; 
            break;
          case D_OUTPUT:
            // Set the output parameter
            body += " SET P"+i+"="+thisTestInfo[i].setOutputValue+";\n";
            break;
          case D_INPUTOUTPUT:
            // Add the input to the output
            if (returnExpression.length() == 0) {
              returnExpression = "CONCAT('P"+i+"=', P"+i+")"; 
            } else { 
              returnExpression = "CONCAT("+returnExpression+", CONCAT(', P"+i+"=', P"+i+"))"; 
            }
            createSQL+=" DEFAULT "+thisTestInfo[i].setDefaultValue+" "; 
            // Still set the output parameter
            body += " SET P"+i+"="+thisTestInfo[i].setOutputValue+";\n";
            break;

          }
        }
       }
       createSQL += " ) ";
       createSQL += "LANGUAGE SQL BEGIN "+body+"\n";
       if (returnExpression.length() == 0) {
         returnExpression = "'ONLY OUTPUT'"; 
       }
       createSQL += " SET P"+outputPosition+"="+returnExpression+";\n"+"END"; 
       
       
  String info = "Testing " + outputPosition + "." + parameterCount + "."
      + parameterTypes+"."+parameterDirections + " " + label; 
  if (verbose) {
    output_.println(info);
  }
  sb.append(info+"\n"); 
  Statement statement = connection_.createStatement(); 
  //
  // Create the procedure
  //
  try {
    String sql  = "Drop procedure " + procedureName;
    sb.append("Executing2 "+sql+"\n"); 
    statement.executeUpdate(sql);
    
  } catch (Exception e) {
    String exString = e.toString().toUpperCase();
    if (exString.indexOf("NOT FOUND") > 0) {
      // ok
    } else {
      e.printStackTrace();
    }
  }
  
    sb.append("Executing4 "+createSQL+"\n"); 
    statement.executeUpdate(createSQL);

    // Loop and call all variations  / checking results each time. 
    String callSql;
    for (int fixedCount = 0; fixedCount <= parameterCount+1; fixedCount++) {
      boolean firstParm = true; 
      callSql = "CALL "+procedureName+"(";
      // Set up the fixed parameters
      for (int i = 1; i <= fixedCount; i++) {
        if (firstParm) {
          firstParm= false; 
        } else {
          callSql+=","; 
        }
        callSql+="?"; 
      }
      
          
      // TODO:  If the output parameter wasn't fixed, then we need to make
      //        sure it is used. 
      
     int parametersLeft = parameterCount+1-fixedCount;
      
     // Value of index is parameter to use.
     // 0 means to not use a parameter for that position
     // 1 means to use the fixedCount+1 parameter there, etc..
     // 
     int[] possibleParameters = new int[parameterCount+1-fixedCount];
     boolean processing = true; 
     processing = incrementInvalidPossibleParameters(possibleParameters, parametersLeft, thisDirection, fixedCount);
     Hashtable<String,String> processedParameterStrings = new Hashtable<String,String>(); 
     while (processing) {
       // Create the parameter string based on the possible parameters
       String parameterString = ""; 
       boolean namedFirstParm = firstParm; 
       for (int i = 0; i < possibleParameters.length; i++) {
         int parmNumber = possibleParameters[i]+fixedCount; 
         if (parmNumber > fixedCount) {
           if (namedFirstParm) {
             namedFirstParm = false; 
           } else {
             parameterString+=", "; 
           }
           parameterString+= "P"+parmNumber+" => ?"; 
         }
       }
       // Make sure the parameter string is not a duplicate string
       Object found = processedParameterStrings.get(parameterString); 
       if (found == null) { 
           processedParameterStrings.put(parameterString, parameterString); 
           
           // Now we can make the call
           String thisCallSql = callSql + parameterString+")"; 
           sb.append("----------------------------------------------\n"); 
           sb.append("Trying to call "+thisCallSql+"\n");
           if (checkCombinations != null) {
             if (!markCalled(thisCallSql, checkCombinations, checkCombinationsFound, sb)) {
               passed = false; 
             }
           }
           if (verbose ) { 
               output_.println("Trying to call "+thisCallSql);
           }
           
           try { 
              sb.append("Preparing "+thisCallSql+"\n"); 
              CallableStatement cstmt = connection_.prepareCall(thisCallSql); 
              int[] parameterCallMapping = new int[1+cstmt.getParameterMetaData().getParameterCount()];
              
              
              // Handle the fixed parameters
              for (int i = 1; i <= fixedCount; i++) {
                parameterCallMapping[i] = i; 
                if (thisDirection[i] == D_INPUT || thisDirection[i] == D_INPUTOUTPUT) {
                    if (thisTestInfo[i].setMethod == SETSTRING) {
                       sb.append("Calling setString("+i+","+thisTestInfo[i].setInputValue+")\n");
                       cstmt.setString(i, thisTestInfo[i].setInputValue);
                    } 
                    // TODO.. Handle binary values
                }
                
                if (thisDirection[i] == D_OUTPUT || thisDirection[i] == D_INPUTOUTPUT) {
                  if (thisTestInfo[i].getMethod == GETSTRING) {
                      sb.append("Calling registerOutParameter("+i+")\n"); 
                      cstmt.registerOutParameter(i, Types.VARCHAR); 
                  }
                  // TODO.  Handle binary values.
                }
              } /* for i */   
                //
                // Now handle the variable parameters
                // 
                int bindParmNumber = fixedCount; 
              for (int j = 0; j < possibleParameters.length; j++) {
                int parmNumber = possibleParameters[j] + fixedCount;
                if (parmNumber > fixedCount) {
                  bindParmNumber++; 
                  parameterCallMapping[bindParmNumber] = parmNumber; 

                  if (thisDirection[parmNumber] == D_INPUT
                      || thisDirection[parmNumber] == D_INPUTOUTPUT) {
                    if (thisTestInfo[parmNumber].setMethod == SETSTRING) {
                      sb.append("Calling setString(" + bindParmNumber + ","    + thisTestInfo[parmNumber].setInputValue + ")\n");
                      cstmt.setString(bindParmNumber,  thisTestInfo[parmNumber].setInputValue);
                    }
                    // TODO.. Handle binary values
                  } /* if Input */ 
                  if (thisDirection[parmNumber] == D_OUTPUT
                      || thisDirection[parmNumber] == D_INPUTOUTPUT) {
                    if (thisTestInfo[parmNumber].getMethod == GETSTRING) {
                      sb.append("Calling registerOutParameter(" + bindParmNumber  + ",Types.VARCHAR)\n");
                      cstmt.registerOutParameter(bindParmNumber, Types.VARCHAR);
                    }
                    // TODO. Handle binary values.
                  } /* output parameter */ 
                } /* If actual parameter */ 

              } /* for j */ 
                
                
                
                // 
                // Do the call 
                // 
                
                cstmt.execute(); 
                
                // 
                // Check the output parameters
                // 
                
                for (int j = 1; j < parameterCallMapping.length; j++) {
                  int declaredParmNumber = parameterCallMapping[j]; 
                  if (declaredParmNumber != outputPosition) {
                     if (thisDirection[declaredParmNumber] == D_OUTPUT || thisDirection[declaredParmNumber] == D_INPUTOUTPUT) {
                       if (thisTestInfo[declaredParmNumber].getMethod == GETSTRING) { 
                         String value = cstmt.getString(j);
                         if (!thisTestInfo[declaredParmNumber].getJDBCOutputValue.equals(value)) {
                           passed = false; 
                           sb.append("***** cstmt.getString("+j+") got '"+value+"' sb '"+thisTestInfo[declaredParmNumber].getJDBCOutputValue+"'\n"); 
                         }
                       } else { 
                         // TODO.. Used getBytes to get info 
                       }
                       
                     }
                  }
                }
                
                
                
                
                // 
                // Check the generated output parameter
                // 
                String outputString = "NOT SET";
                if ( outputPosition <= fixedCount) {
                  sb.append("Calling getString("+outputPosition+")\n");
                  outputString = cstmt.getString(outputPosition); 
                } else {
                  bindParmNumber=fixedCount; 
                  for (int j = 0; j < possibleParameters.length; j++) {
                    int parmNumber = possibleParameters[j] + fixedCount;
                    if (parmNumber > fixedCount) {
                      bindParmNumber++; 
                      if ( parmNumber == outputPosition) {
                        sb.append("Calling getString("+bindParmNumber+")\n");
                        outputString = cstmt.getString(bindParmNumber); 
                        j = possibleParameters.length; 
                      } /* if match found */ 
                    } /* if parameter used */ 
                  } /* for variable parameters */ 
                } /* look at variable parameters instead of fixed parameters */ 
               
                sb.append(" outputString is "+outputString+"\n"); 
                if (verbose) { 
                  output_.println(" outputString is "+outputString); 
                }
                
                // Generated the predicted output value.  Only the input parameters are traced
                boolean firstValue = true;
                String expectedOutputString=""; 
                for (int i = 1; i <= parameterCount+1; i++) {
                  if (thisDirection[i] == D_INPUT || thisDirection[i] == D_INPUTOUTPUT) { 
                     if (firstValue) {
                       firstValue = false; 
                     } else {
                      expectedOutputString+=", "; 
                     }
                     expectedOutputString+="P"+i+"="; 
                     if (thisDirection[i] == D_INPUTOUTPUT) {
                       expectedOutputString+= thisTestInfo[i].getSQLOutputValue;
                     } else { 
                       if (explicitlySet(i, parameterCallMapping)) {
                         expectedOutputString+= thisTestInfo[i].getSQLInputValue;
                       } else {
                         expectedOutputString+= thisTestInfo[i].getSQLDefaultValue;
                       }
                     }
                  }
                }
                if (expectedOutputString.length() == 0) {
                    expectedOutputString = "ONLY OUTPUT"; 
                }

                if ( ! expectedOutputString.equals(outputString)) {
                  passed = false; 
                  sb.append("**** Got        '"+outputString+"'\n");
                  sb.append("**** Expected   '"+expectedOutputString+"'\n"); 
                }
           
           
              cstmt.close(); 
           } catch ( Exception e) { 
             passed = false; 
             sb.append("*****   Exception encountered\n"); 
             printStackTraceToStringBuffer(e, sb); 
             sb.append("----------------------------------------------\n"); 
             
           }
           
           
           
           
           
       }
       
       
       processing = nextPossibleParameters(possibleParameters, parametersLeft, thisDirection, fixedCount); 
     }
      
      
      
      
    }
    
    
    
  
    // Cleanup 

    String sql  = "Drop procedure " + procedureName;
    sb.append("Executing2 "+sql+"\n"); 
    statement.executeUpdate(sql);

      statement.close(); 
    
      if ((checkCombinations != null) && (checkCombinationsFound != null) ) {
        for (int i = 0; i < checkCombinations.length; i++) { 
          if (!checkCombinationsFound[i]) {
            sb.append("******* Did not find combination "+checkCombinations[i]+"\n"); 
            passed = false; 
          }
        }
      } 
      
      
     } catch (Exception e) {
       sb.append("***************** Unexpected exception *******************"); 
       printStackTraceToStringBuffer(e, sb);
       passed = false; 
     }
     
     return passed; 
  }
  





  private boolean markCalled(String thisCallSql, String[] checkCombinations,
      boolean[] checkCombinationsFound, StringBuffer sb) {
    for (int i = 0; i < checkCombinations.length; i++) {
      if (thisCallSql.indexOf(checkCombinations[i]) >= 0) {
        checkCombinationsFound[i] = true;
        return true; 
      }
    }
    sb.append("***************   SQL call "+thisCallSql+" did not contain an expected call\n"); 
    return false; 
  }





  private boolean explicitlySet(int parm, int[] parameterCallMapping) {
    
    for (int j = 1; j < parameterCallMapping.length; j++) {
      if (parameterCallMapping[j] == parm) {
        return true; 
      }
    }
    return false;
  }





  private boolean incrementInvalidPossibleParameters(int[] possibleParameters, int parametersLeft,  int[] thisDirection, int fixedCount) {
    while( ! containsRequiredParameters(possibleParameters, thisDirection, fixedCount)) {
      boolean keepGoing = incrementPossibleParameters(possibleParameters, parametersLeft) ; 
      if (!keepGoing) return false; 
    }
    return true; 
  }





  private boolean incrementPossibleParameters(int[] possibleParameters,
      int parametersLeft) {
    for (int i = 0; i < possibleParameters.length; i++) {
      possibleParameters[i]++;
      if (possibleParameters[i] <= parametersLeft) {
        if (noDuplicates(possibleParameters))  {
           // output_.println("incrementPossibleParameters returning "+dumpArray(possibleParameters));
           return true; 
        } else {
          // Keep looking because there was a duplicate, but start incrementing at the bottom
          i = -1; 
        }
      } else {
         // We overflowed, increment the next digit by staying in the loop
         // In most cases, we will find something to increment and return using 
         // the code above. 
         possibleParameters[i] = 0; 
      }
    }
    // Everything was set to zero.. Return false to indicate we are done
    return false; 
  }

  String dumpArray(int[] possibleParameters) {

    StringBuffer sb = new StringBuffer(); 
    sb.append(""+possibleParameters[0]); 
    for (int i = 1; i < possibleParameters.length; i++) { 
      sb.append(","+possibleParameters[i]); 
    }
    return sb.toString(); 
  }





  private boolean noDuplicates(int[] possibleParameters) {
    for (int i = 1; i < possibleParameters.length; i++) {
      if (possibleParameters[i] > 0) {
        for (int j = 0; j < i; j++) {
          if (possibleParameters[j] == possibleParameters[i]) {
            return false; 
          }
        }
      }
    }
    return true; 
  }





  private boolean nextPossibleParameters(int[] possibleParameters,
      int parametersLeft, int[] thisDirection, int fixedCount) {
    boolean keepGoing = incrementPossibleParameters(possibleParameters, parametersLeft); 
    if (keepGoing) {
       return incrementInvalidPossibleParameters(possibleParameters, parametersLeft, thisDirection, fixedCount);
    } else {
      return false; 
    }
  }



  /**
   * Does the combination contain all the required parameters.
   * All output only parameters are required. 
   * @param possibleParameters.   These are in relation to fixedCount, indexed at 0 
   * @param thisDirection.  The direction of parameters 1....n, indexed at 1 
   * @param fixCount.  The number of fixed parameters. 
   * @return
   */
  private boolean containsRequiredParameters(int[] possibleParameters,
      int[] thisDirection, int fixedCount) {

    // Only need to check the parameters beyond the fixed count */ 
    for (int i = fixedCount+1; i < thisDirection.length; i++) { 
      if (thisDirection[i] == D_OUTPUT) {
        boolean found = false; 
        for (int j = 0; (!found) && (j < possibleParameters.length); j++ ) {
          if (possibleParameters[j] + fixedCount == i) {
            found = true; 
          }
        }
        if (!found) return false; 
      }
      
    }
  
    return true; 
  
  }


/* Run a test variation that returns the output answer in result set */
  /* This variation does not have a required outputPosition */ 
  
  public boolean runTestVariationRS(int parameterCount, long parameterTypes,
      long parameterDirections, StringBuffer sb) {
    sb.append("Running runTestVariationRS(" + parameterCount + ","
        + parameterTypes + "," + parameterDirections + ")\n");
    boolean passed = true;
    try {
      JDCSNamedTypeInfo thisTestInfo[] = new JDCSNamedTypeInfo[parameterCount + 1];
      int thisDirection[] = new int[parameterCount + 1];

      String label = "";
      String body = "";
      String returnExpression = "";
      String procedureName = JDCSTest.COLLECTION + ".JDCS" + parameterCount
          + "x" + parameterTypes + "x" + parameterDirections;
      String createSQL = "CREATE OR REPLACE Procedure " + procedureName + "(";
      long workingTypes = parameterTypes;
      long workingDirections = parameterDirections;
      for (int i = 1; i <= parameterCount; i++) {
        int typeId;
        int direction;

        typeId = (int) (workingTypes % typeInfo.length);
        workingTypes = workingTypes / typeInfo.length;
        direction = (int) (workingDirections % D_COUNT);
        workingDirections = workingDirections / D_COUNT;
        thisTestInfo[i] = typeInfo[typeId];
        thisDirection[i] = direction;
        label += "-" + thisTestInfo[i].sqlName;

        if (i > 1) {
          createSQL += ",";
        }
        createSQL += " " + directionString[direction] + " P" + i + " "
            + thisTestInfo[i].sqlName;
        switch (direction) {
        case D_INPUT:
          // Just add the input to the output
          if (returnExpression.length() == 0) {
            returnExpression = "CONCAT('P" + i + "=', P" + i + ")";
          } else {
            returnExpression = "CONCAT(" + returnExpression + ", CONCAT(', P"
                + i + "=', P" + i + "))";
          }
          createSQL += " DEFAULT " + thisTestInfo[i].setDefaultValue + " ";
          break;
        case D_OUTPUT:
          // Set the output parameter
          body += " SET P" + i + "=" + thisTestInfo[i].setOutputValue + ";\n";
          break;
        case D_INPUTOUTPUT:
          // Add the input to the output
          if (returnExpression.length() == 0) {
            returnExpression = "CONCAT('P" + i + "=', P" + i + ")";
          } else {
            returnExpression = "CONCAT(" + returnExpression + ", CONCAT(', P"
                + i + "=', P" + i + "))";
          }
          createSQL += " DEFAULT " + thisTestInfo[i].setDefaultValue + " ";
          // Still set the output parameter
          body += " SET P" + i + "=" + thisTestInfo[i].setOutputValue + ";\n";
          break;

        } /* switch */

      }
      
      
      if (returnExpression.length() == 0) {
        returnExpression = "'ONLY OUTPUT'";
      }
      
      createSQL += " ) ";
      createSQL += "RESULT SETS 1 LANGUAGE SQL BEGIN \n";
          createSQL += "DECLARE RESULTC cursor for SELECT " + returnExpression + " FROM SYSIBM.SYSDUMMY1;\n";
      createSQL += body + "\n";
      createSQL += "\n"; 
      createSQL +=  "OPEN RESULTC; SET RESULT SETS CURSOR RESULTC; END";

      String info = "Testing " + parameterCount + "." + parameterTypes + "."
          + parameterDirections + " " + label;
      if (verbose) {
        output_.println(info);
      }
      sb.append(info + "\n");
      Statement statement = connection_.createStatement();
      //
      // Create the procedure
      //
      try {
        String sql = "Drop procedure " + procedureName;
        sb.append("Executing2 " + sql + "\n");
        statement.executeUpdate(sql);

      } catch (Exception e) {
        String exString = e.toString().toUpperCase();
        if (exString.indexOf("NOT FOUND") > 0) {
          // ok
        } else {
          e.printStackTrace();
        }
      }

      sb.append("Executing4 " + createSQL + "\n");
      statement.executeUpdate(createSQL);

      // Loop and call all variations / checking results each time.
      String callSql;
      for (int fixedCount = 0; fixedCount <= parameterCount; fixedCount++) {
        boolean firstParm = true;
        callSql = "CALL " + procedureName + "(";
        // Set up the fixed parameters
        for (int i = 1; i <= fixedCount; i++) {
          if (firstParm) {
            firstParm = false;
          } else {
            callSql += ",";
          }
          callSql += "?";
        }

        int parametersLeft = parameterCount - fixedCount;

        // Value of index is parameter to use.
        // 0 means to not use a parameter for that position
        // 1 means to use the fixedCount+1 parameter there, etc..
        //
        int[] possibleParameters = new int[parameterCount - fixedCount];
        boolean processing = true;
        processing = incrementInvalidPossibleParameters(possibleParameters,
            parametersLeft, thisDirection, fixedCount);
        Hashtable<String,String> processedParameterStrings = new Hashtable<String,String>();
        while (processing) {
          // Create the parameter string based on the possible parameters
          String parameterString = "";
          boolean namedFirstParm = firstParm;
          for (int i = 0; i < possibleParameters.length; i++) {
            int parmNumber = possibleParameters[i] + fixedCount;
            if (parmNumber > fixedCount) {
              if (namedFirstParm) {
                namedFirstParm = false;
              } else {
                parameterString += ", ";
              }
              parameterString += "P" + parmNumber + " => ?";
            }
          }
          // Make sure the parameter string is not a duplicate string
          Object found = processedParameterStrings.get(parameterString);
          if (found == null) {
            processedParameterStrings.put(parameterString, parameterString);

            // Now we can make the call
            String thisCallSql = callSql + parameterString + ")";
            sb.append("----------------------------------------------\n");
            sb.append("Trying to call " + thisCallSql + "\n");
            if (verbose) {
              output_.println("Trying to call " + thisCallSql);
            }

            try {
              sb.append("Preparing " + thisCallSql + "\n");
              CallableStatement cstmt = connection_.prepareCall(thisCallSql);
              int[] parameterCallMapping = new int[1 + cstmt
                  .getParameterMetaData().getParameterCount()];

              // Handle the fixed parameters
              for (int i = 1; i <= fixedCount; i++) {
                parameterCallMapping[i] = i;
                if (thisDirection[i] == D_INPUT
                    || thisDirection[i] == D_INPUTOUTPUT) {
                  if (thisTestInfo[i].setMethod == SETSTRING) {
                    sb.append("Calling setString(" + i + ","
                        + thisTestInfo[i].setInputValue + ")\n");
                    cstmt.setString(i, thisTestInfo[i].setInputValue);
                  }
                  // TODO.. Handle binary values
                }

                if (thisDirection[i] == D_OUTPUT
                    || thisDirection[i] == D_INPUTOUTPUT) {
                  if (thisTestInfo[i].getMethod == GETSTRING) {
                    sb.append("Calling registerOutParameter(" + i + ")\n");
                    cstmt.registerOutParameter(i, Types.VARCHAR);
                  }
                  // TODO. Handle binary values.
                }
              } /* for i */
              //
              // Now handle the variable parameters
              //
              int bindParmNumber = fixedCount;
              for (int j = 0; j < possibleParameters.length; j++) {
                int parmNumber = possibleParameters[j] + fixedCount;
                if (parmNumber > fixedCount) {
                  bindParmNumber++;
                  parameterCallMapping[bindParmNumber] = parmNumber;

                  if (thisDirection[parmNumber] == D_INPUT
                      || thisDirection[parmNumber] == D_INPUTOUTPUT) {
                    if (thisTestInfo[parmNumber].setMethod == SETSTRING) {
                      sb.append("Calling setString(" + bindParmNumber + ","
                          + thisTestInfo[parmNumber].setInputValue + ")\n");
                      cstmt.setString(bindParmNumber,
                          thisTestInfo[parmNumber].setInputValue);
                    }
                    // TODO.. Handle binary values
                  } /* if Input */
                  if (thisDirection[parmNumber] == D_OUTPUT
                      || thisDirection[parmNumber] == D_INPUTOUTPUT) {
                    if (thisTestInfo[parmNumber].getMethod == GETSTRING) {
                      sb.append("Calling registerOutParameter("
                          + bindParmNumber + ",Types.VARCHAR)\n");
                      cstmt.registerOutParameter(bindParmNumber, Types.VARCHAR);
                    }
                    // TODO. Handle binary values.
                  } /* output parameter */
                } /* If actual parameter */

              } /* for j */

              //
              // Do the call
              //

              cstmt.execute();

              ResultSet rs = cstmt.getResultSet();
              //
              // Check the output parameters
              //

              for (int j = 1; j < parameterCallMapping.length; j++) {
                int declaredParmNumber = parameterCallMapping[j];
                if (thisDirection[declaredParmNumber] == D_OUTPUT
                    || thisDirection[declaredParmNumber] == D_INPUTOUTPUT) {
                  if (thisTestInfo[declaredParmNumber].getMethod == GETSTRING) {
                    String value = cstmt.getString(j);
                    if (!thisTestInfo[declaredParmNumber].getJDBCOutputValue
                        .equals(value)) {
                      passed = false;
                      sb.append("***** cstmt.getString(" + j + ") got '"
                          + value + "' sb '"
                          + thisTestInfo[declaredParmNumber].getJDBCOutputValue
                          + "'\n");
                    }
                  } else {
                    // TODO.. Used getBytes to get info
                  }

                } /* if output parm */
              } /* for j */

              //
              // Check the generated output parameter
              //

              String outputString = "NOT SET";
              if (rs.next()) {
                outputString = rs.getString(1);
              }
              rs.close();

              sb.append(" outputString is " + outputString + "\n");
              if (verbose) {
                output_.println(" outputString is " + outputString);
              }

              // Generated the predicted output value. Only the input parameters
              // are traced
              boolean firstValue = true;
              String expectedOutputString = "";
              for (int i = 1; i <= parameterCount; i++) {
                if (thisDirection[i] == D_INPUT
                    || thisDirection[i] == D_INPUTOUTPUT) {
                  if (firstValue) {
                    firstValue = false;
                  } else {
                    expectedOutputString += ", ";
                  }
                  expectedOutputString += "P" + i + "=";
                  if (thisDirection[i] == D_INPUTOUTPUT) {
                    expectedOutputString += thisTestInfo[i].getSQLOutputValue;
                  } else {
                    if (explicitlySet(i, parameterCallMapping)) {
                      expectedOutputString += thisTestInfo[i].getSQLInputValue;
                    } else {
                      expectedOutputString += thisTestInfo[i].getSQLDefaultValue;
                    }
                  }
                }
              }
              if (expectedOutputString.length() == 0) {
                expectedOutputString = "ONLY OUTPUT";
              }

              if (!expectedOutputString.equals(outputString)) {
                passed = false;
                sb.append("**** Got        '" + outputString + "'\n");
                sb.append("**** Expected   '" + expectedOutputString + "'\n");
              }

              cstmt.close();
            } catch (Exception e) {
              passed = false;
              sb.append("*****   Exception encountered\n");
              printStackTraceToStringBuffer(e, sb);
              sb.append("----------------------------------------------\n");

            }

          }

          processing = nextPossibleParameters(possibleParameters, parametersLeft, thisDirection, fixedCount); 
        }
      }

      // Cleanup

      String sql = "Drop procedure " + procedureName;
      sb.append("Executing2 " + sql + "\n");
      statement.executeUpdate(sql);

      statement.close();

    } catch (Exception e) {
      sb.append("***************** Unexpected exception *******************");
      printStackTraceToStringBuffer(e, sb);
      passed = false;
    }

    return passed;
  }

  public void Var002()  {
    String [] checkCombinations = {
       "(P1 => ?)",
       "(P2 => ?, P1 => ?)",
       "(P3 => ?, P1 => ?)",
       "(P1 => ?, P2 => ?)",
       "(P1 => ?, P3 => ?)",
       "(P3 => ?, P2 => ?, P1 => ?)",
       "(P2 => ?, P3 => ?, P1 => ?)",
       "(P3 => ?, P1 => ?, P2 => ?)",
       "(P1 => ?, P3 => ?, P2 => ?)",
       "(P2 => ?, P1 => ?, P3 => ?)",
       "(P1 => ?, P2 => ?, P3 => ?)",
       "(?)",
       "(?, P2 => ?)",
       "(?, P3 => ?)",
       "(?, P3 => ?, P2 => ?)",
       "(?, P2 => ?, P3 => ?)",
       "(?,?)",
       "(?,?, P3 => ?)",
       "(?,?,?)",
    };
    
    runTest(1,
            2,
            T_INTEGER + T_INTEGER*T1, 
            D_INPUT+D_INPUT*D1,
            checkCombinations); 
  }

  public void Var003()  {
    runTest(2,
            2,
            T_INTEGER + T_INTEGER*T1, 
            D_INPUT+D_INPUT*D1,
            null); 
  }

  public void Var004()  {
    runTest(3,
            2,
            T_INTEGER + T_INTEGER*T1, 
            D_INPUT+D_INPUT*D1,
            null); 
  }

  public void Var005()  {
    runTest(1,
            2,
            T_CHAR2 + T_CHAR2*T1, 
            D_INPUT+D_INPUT*D1,
            null); 
  }

  public void Var006()  {
    runTest(2,
            2,
            T_CHAR2 + T_CHAR2*T1, 
            D_INPUT+D_INPUT*D1,
            null); 
  }

  public void Var007()  {
    runTest(3,
            2,
            T_CHAR2 + T_CHAR2*T1, 
            D_INPUT+D_INPUT*D1,
            null); 
  }

  
  public void Var008() { 
    runAllTypesTest(); /*  runMilliseconds */ 
  }

  public void Var009() {
      assertCondition(true);  /* Moved to JDCSNamed9 */ 
  }

  public void Var010() {

            assertCondition(true);  /* Moved to JDCSNamed10 */ 

  }

  public void Var011() {
      assertCondition(true);  /* Moved to JDCSNamed11 */ 
  }

  
  //
  // Repeat using the result set as output
  // 
  
  public void Var012()  {
    runTestRS(
            2,
            T_INTEGER + T_INTEGER*T1, 
            D_INPUT+D_INPUT*D1); 
  }

  
  public void Var013() {
      assertCondition(true);  /* Moved to JDCSNamed13 */ 
  }

  // Run test with only output parameters 
  public void Var014()  {
    runTestRS(
            5,
            T_CHAR + T_INTEGER*T1 + T_REAL*T2 + T_FLOAT*T3 + T_INTEGER*T4, 
            D_OUTPUT + D_OUTPUT*D1 + D_OUTPUT*D2 + D_OUTPUT*D3 + D_OUTPUT*D4); 
  }
  


  
}

class JDCSNamedTypeInfo {
  public String sqlName;
  public int    setMethod;
  public int    getMethod;
  public String setInputValue;
  public String getSQLInputValue;
  public String setOutputValue;
  public String getSQLOutputValue;
  public String getJDBCOutputValue; 
  public String setDefaultValue;
  public String getSQLDefaultValue;
  public String getJDBCDefaultValue;
  
  public JDCSNamedTypeInfo(String sqlName, int setMethod, int getMethod, String setInputValue, String getSQLInputValue, String setOutputValue, String getSQLOutputValue, String getJDBCOutputValue, String setDefaultValue, String getSQLDefaultValue, String getJDBCDefaultValue) {
this.sqlName = sqlName;
this.setMethod = setMethod; 
this.getMethod = getMethod; 
this.setInputValue = setInputValue; 
this.getSQLInputValue = getSQLInputValue;
this.setOutputValue = setOutputValue; 
this.getSQLOutputValue = getSQLOutputValue; 
this.getJDBCOutputValue = getJDBCOutputValue; 
this.setDefaultValue = setDefaultValue; 
this.getSQLDefaultValue = getSQLDefaultValue; 
this.getJDBCDefaultValue = getJDBCDefaultValue; 
  }
}



