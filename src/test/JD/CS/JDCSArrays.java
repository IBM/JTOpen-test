///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSArrays.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Random;

/**
 * Testcase JDCSArrays. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * <ul>
 * <li>Arrays()
 * </ul>
 */
public class JDCSArrays extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSArrays";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

  // Private data.
  Statement stmt_;

  /**
   * Constructor.
   */
  public JDCSArrays(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream,  String password) {
    super(systemObject, "JDCSArrays", namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   */
  protected void setup() throws Exception {

    JDSetupProcedure.resetCollection(collection_);

    connection_ = testDriver_.getConnection(baseURL_ + ";errors=full", userId_, encryptedPassword_);
    stmt_ = connection_.createStatement();

    try {
      String sql = "DROP TYPE " + JDCSTest.COLLECTION + ".JDCSARRAYS";
      stmt_.execute(sql);
    } catch (SQLException e) {
      String message = e.toString();
      if (message.indexOf("not found") < 0) {
        throw e;
      }
    }
    String sql = "CREATE TYPE " + JDCSTest.COLLECTION
        + ".JDCSARRAYS AS CHAR(3) ARRAY[10]";
    stmt_.execute(sql);

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   */
   public  void cleanup() throws Exception {
    
    try {
      String sql = "DROP TYPE " + JDCSTest.COLLECTION + ".JDCSARRAYS";
      stmt_.execute(sql);
      connection_ = cleanupConnection(connection_);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /* Tests an array combination */
  /* Each parameter is represented by 5 bits */
  /* High 2 bit are interpreted as follows */
  /* 0x10 Input parameter */
  /* 0x08 Output parameter */
  /* 0x18 Input/output parameter */
  /* 0x4 Array 0x0 Nonarray */
  /* 0x2 Input non-null 0x0 Null */
  /* 0x1 Output non-null 0x0 Null */
  /* The parameter are given in little-ending order */
  /* Test returns true if the testcase passed */
  /* On failure the stringbuffer contains error information */

  
  public boolean testArrayCombination(long combination, StringBuffer sb) {
    boolean passed = true;
    String sql = "";
    try {
      if (redundantTest(combination)) {
        // System.out.println("Skipping redundant test "+describeTest(combination)); 
      } else {

        sb.append("*************************************************************\n");
        sb.append("Testing combination 0x" + Long.toHexString(combination)
            + "\n");
        sb.append("*************************************************************\n");
        String procedureName = JDCSTest.COLLECTION + ".JDCSA"
            + Long.toHexString(combination);
        try {
          sql = "DROP PROCEDURE " + procedureName;
          stmt_.execute(sql);
        } catch (Exception e) {
          sb.append("Warning:  Error on " + sql + "\n");
          printStackTraceToStringBuffer(e, sb);
        }

        sql = "createArrayCombinationProcedure";
        passed = createArrayCombinationProcedure(procedureName, combination, sb);
        sql = "callArrayCombination";
        passed = callArrayCombination(procedureName, combination, sb) && passed;

        sql = "DROP PROCEDURE " + procedureName;
        stmt_.execute(sql);
      }
    } catch (Exception e) {
      passed = false;
      sb.append("UNEXPECTED EXCEPTION for sql=" + sql + "\n");
      printStackTraceToStringBuffer(e, sb);
    }
    sb.append("*************************************************************\n");
    sb.append("Ending combination " + Long.toHexString(combination) + "\n");
    sb.append("*************************************************************\n");

    return passed;
  }

  // Returns true if the test is redundant
  String describeTest(long combination) {
    int parameterNumber=0;
    StringBuffer sb = new StringBuffer(); 
    while (combination > 0) {
      parameterNumber ++; 
      int thisParameter = (int) (combination & 0x1F);
      boolean inNull;
      boolean outNull;

      inNull = ((thisParameter & 0x2) != 0x2);
      outNull = ((thisParameter & 0x1) != 0x1);

      /* Input only */
      if ((thisParameter & 0x18) == 0x10) {
        sb.append("IN P"+parameterNumber+"(");
      } else if ((thisParameter & 0x18) == 0x08) {
        sb.append("OUT P"+parameterNumber+"(");
      } else { 
        sb.append("INOUT P"+parameterNumber+"(");
      }
      if (inNull) { 
        sb.append("<=NULL ");
      } else {
        sb.append("<=VALUE "); 
      }
      if (outNull) { 
        sb.append("=>NULL ");
      } else {
        sb.append("=>VALUE "); 
      }
        
      
      combination = combination >> 5;
    } /* while combination > 0 */

    return sb.toString(); 
  }

  // Returns true if the test is redundant
  private boolean redundantTest(long combination) {
    while (combination > 0) {
      int thisParameter = (int) (combination & 0x1F);
      boolean inNull;
      boolean outNull;

      inNull = ((thisParameter & 0x2) != 0x2);
      outNull = ((thisParameter & 0x1) != 0x1);

      /* Input only */
      if ((thisParameter & 0x18) == 0x10) {
        if (outNull == true) {
          // not redundant
        } else {
          // redundant with outNull = tru case.
          return true;
        }
      }
      /* Output only */
      if ((thisParameter & 0x18) == 0x08) {
        if (inNull == true) {
          // not redundant

        } else {
          // redunant with inNull == true case.
          return true;
        }
      }
      combination = combination >> 5;
    } /* while combination > 0 */

    return false;
  }

  private boolean callArrayCombination(String procedureName, long combination,
      StringBuffer sb) {
    long startCombination = combination;
    boolean passed = true;
    StringBuffer sqlsb = new StringBuffer();
    sqlsb.append("CALL " + procedureName + "(?");
    while (combination > 0) {
      sqlsb.append(",?");
      combination = combination >> 5;
    }
    sqlsb.append(")");

    try {
      String sql = sqlsb.toString();
      sb.append("Preparing " + sql + "\n");
      CallableStatement cstmt = connection_.prepareCall(sql);
      int parameterNumber = 1;
      // Set / register the parameters
      combination = startCombination;
      StringBuffer expectedSb = new StringBuffer();
      String[] stringArray = { "ABC", "DEF" };
      Array inArray = (Array) JDReflectionUtil.callMethod_OSA(connection_,
          "createArrayOf", "VARCHAR", stringArray); //

      while (combination > 0) {
        int thisParameter = (int) (combination & 0x1F);
        boolean arrayType;
        boolean inNull;
        boolean outNull;

        arrayType = ((thisParameter & 0x4) == 0x4);
        inNull = ((thisParameter & 0x2) != 0x2);
        outNull = ((thisParameter & 0x1) != 0x1);

        /* Input */
        if ((thisParameter & 0x10) == 0x10) {
          if (inNull) {
            expectedSb.append(" P" + parameterNumber + " IS NULL");
            if (arrayType) {
              cstmt.setNull(parameterNumber, Types.ARRAY);
            } else {
              cstmt.setNull(parameterNumber, Types.CHAR);
            }
          } else {
            if (arrayType) {
              expectedSb.append(" P" + parameterNumber + "[1]=ABC");
              cstmt.setArray(parameterNumber, inArray);
            } else {
              expectedSb.append(" P" + parameterNumber + "=ABC     ");
              cstmt.setString(parameterNumber, "ABC");
            }
          }
        }
        /* Output */
        if ((thisParameter & 0x8) == 0x08) {
          if (arrayType) {
            cstmt.registerOutParameter(parameterNumber, Types.ARRAY);
          } else {
            cstmt.registerOutParameter(parameterNumber, Types.CHAR);
          }
        }
        parameterNumber++;
        combination = combination >> 5;
      } /* while combination > 0 */

      cstmt.registerOutParameter(parameterNumber, Types.VARCHAR);

      // Execute it
      sb.append("Executing\n");
      cstmt.execute();

      // Check the parameters
      parameterNumber = 1;
      combination = startCombination;
      while (combination > 0) {
        int thisParameter = (int) (combination & 0x1F);
        boolean arrayType;
        boolean inNull;
        boolean outNull;

        arrayType = ((thisParameter & 0x4) == 0x4);
        inNull = ((thisParameter & 0x2) != 0x2);
        outNull = ((thisParameter & 0x1) != 0x1);

        /* Output */
        if ((thisParameter & 0x8) == 0x08) {
          if (arrayType) {
            Array outArray = cstmt.getArray(parameterNumber);
            if (outNull) {
              if (outArray != null) {
                passed = false;
                sb.append("For P" + parameterNumber + " Expected null but got "
                    + dumpArray(outArray) + "\n");
              }
            } else {
              if (outArray == null) {
                sb.append("For P" + parameterNumber
                    + " Expected XYZ but got null\n");
              } else {
                String outArrayString = dumpArray(outArray);
                String expected = " [0]=XYZ";
                if (!expected.equals(outArrayString)) {
                  passed = false;
                  sb.append("For P" + parameterNumber + " GOT: "
                      + outArrayString + "\n");
                  sb.append("For P" + parameterNumber + "  SB: " + expected
                      + "\n");
                }
              }
            }
          } else {
            String outString = cstmt.getString(parameterNumber);
            if (outNull) {
              if (outString != null) {
                passed = false;
                sb.append("For P" + parameterNumber + " Expected null but got "
                    + outString + "\n");
              }

            } else {
              String expected = "XYZ     ";
              if (!expected.equals(outString)) {
                passed = false;
                sb.append("For P" + parameterNumber + " Expected '" + expected
                    + "'\n");
                sb.append("For P" + parameterNumber + "      got '" + outString
                    + "'\n");
              }
            }
          }

        }
        parameterNumber++;
        combination = combination >> 5;
      } /* while combination > 0 */

      sb.append("Calling getString(" + parameterNumber + ") to get output\n");
      String result = cstmt.getString(parameterNumber);
      String expectedString = expectedSb.toString();
      if (!expectedString.equals(result)) {
        passed = false;
        sb.append(" FOR OUTPARM GOT : " + result + "\n");
        sb.append(" FOR OUTPURM  SB : " + expectedString + "\n");
      }

    } catch (Exception e) {
      passed = false;
      sb.append("Error during callArray \n");
      printStackTraceToStringBuffer(e, sb);
    }
    return passed;
  }

  private String dumpArray(Array outArray) throws SQLException {
    StringBuffer sb = new StringBuffer();
    if (outArray == null) {
      sb.append("null");
    } else {
      String[] array = (String[]) outArray.getArray();
      for (int i = 0; i < array.length; i++) {
        sb.append(" [" + i + "]=" + array[i]);
      }
    }

    return sb.toString();
  }

  private boolean createArrayCombinationProcedure(String procedureName,
      long combination, StringBuffer sb) {
    long startCombination = combination;
    boolean passed = true;
    StringBuffer sqlsb = new StringBuffer();
    sqlsb.append("CREATE OR REPLACE PROCEDURE " + procedureName + "(");
    int parameterNumber = 0;
    while (combination > 0) {
      parameterNumber++;
      int thisParameter = (int) (combination & 0x1F);
      switch (thisParameter & 0x18) {
      case 0x10:
        sqlsb.append(" IN ");
        break;
      case 0x08:
        sqlsb.append(" OUT ");
        break;
      case 0x18:
        sqlsb.append(" INOUT ");
        break;
      default:
        passed = false;
        sb.append("No IN/OUT/INOUT set for p#" + parameterNumber + " for "
            + Integer.toHexString(thisParameter));
      }
      sqlsb.append("P" + parameterNumber + " ");
      if ((thisParameter & 0x4) == 0x4) {
        sqlsb.append(JDCSTest.COLLECTION + ".JDCSARRAYS");
      } else {
        sqlsb.append("CHAR(8)");
      }
      sqlsb.append(",");
      combination = combination >> 5;
    } /* while combination > 0 */
    sqlsb.append(" OUT POUT VARCHAR(16000) ) LANGUAGE SQL BEGIN SET POUT=''; ");
    parameterNumber = 0;
    combination = startCombination;
    while (combination > 0) {
      parameterNumber++;
      int thisParameter = (int) (combination & 0x1F);
      boolean arrayType;
      boolean inNull;
      boolean outNull;

      arrayType = ((thisParameter & 0x4) == 0x4);
      inNull = ((thisParameter & 0x2) != 0x2);
      outNull = ((thisParameter & 0x1) != 0x1);

      /* Input */
      if ((thisParameter & 0x10) == 0x10) {
        if (inNull) {
          sqlsb.append(" IF P" + parameterNumber
              + " IS NULL THEN SET POUT = POUT || ' P" + parameterNumber
              + " IS NULL'; " + "ELSE SET POUT = POUT || ' P" + parameterNumber
              + " IS NOT NULL'; END IF; ");
        } else {
          if (arrayType) {
            sqlsb.append("SET POUT = POUT || ' P" + parameterNumber
                + "[1]=' || P" + parameterNumber + "[1];");
          } else {
            sqlsb.append("SET POUT = POUT || ' P" + parameterNumber + "=' || P"
                + parameterNumber + ";");
          }
        }
      }
      /* Output */
      if ((thisParameter & 0x8) == 0x08) {
        if (outNull) {
          sqlsb.append("SET P" + parameterNumber + "=NULL;");
        } else {
          if (arrayType) {
            sqlsb.append("SET P" + parameterNumber + "= ARRAY['XYZ'] ;");
          } else {
            sqlsb.append("SET P" + parameterNumber + "='XYZ';");
          }

        }

      }

      combination = combination >> 5;
    } /* while combination > 0 */

    sqlsb.append(" END ");

    String sql = sqlsb.toString();
    try {
      sb.append("Executing: " + sql + "\n");
      stmt_.execute(sql);
    } catch (Exception e) {
      passed = false;
      sb.append("ERROR ON " + sql + "\n");
      ;
      printStackTraceToStringBuffer(e, sb);
    }
    return passed;
  }

  /**
   * Arrays() - test all 1 parameter combinations.
   */
  public void Var001() {
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    try {
      for (int i = 0x8; passed && (i <= 0x1f); i++) {

        switch (i) {
        case 0x1d: /* Tested in Var002 */
          break;
        default:

          passed = testArrayCombination(i, sb) && passed;
          if (passed) {
            sb.setLength(0);
          }
        }
      }

      assertCondition(passed, sb);
    } catch (Exception e) {
      failed(connection_, e, sb);
    }

  }

  /**
   * Arrays() - 0x1d combination fails with SQL0901 on sq740
   */
  public void Var002() {
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    try {
      passed = testArrayCombination(0x1d, sb) && passed;

      assertCondition(passed, sb);
    } catch (Exception e) {
      failed(connection_, e, sb);
    }

  }

  boolean SQL0901failure(int combo) {
    switch (combo) {
    case 0x11d:
    case 0x13d:
    case 0x15d:
    case 0x17d:
    case 0x19d:
    case 0x1bd:
    case 0x1dd:
    case 0x1fd:
    case 0x21d:
    case 0x23d:
    case 0x25d:
    case 0x29d:
    case 0x2bd:
    case 0x2dd:
    case 0x2fd:
    case 0x31d:
    case 0x33d:
    case 0x35d:
    case 0x37d: 
    case 0x39d:
    case 0x3a8:
    case 0x3a9:
    case 0x3aa:
    case 0x3ab:
    case 0x3ac:
    case 0x3ad:
    case 0x3ae:
    case 0x3af:
    case 0x3b0:
    case 0x3b1:
    case 0x3b2:
    case 0x3b3:
    case 0x3b4:
    case 0x3b5:
    case 0x3b6:
    case 0x3b7:
    case 0x3b8:
    case 0x3b9:
    case 0x3ba:
    case 0x3bb:
    case 0x3bc:
    case 0x3bd:
    case 0x3be:
    case 0x3bf:
    case 0x3dd:
    case 0x3fd:
      return true;
    default:
      return false;
    }
  }

  boolean nullCharOuputFailure(int combo) {
    switch (combo) {
    case 0x10c:
    case 0x10d:
    case 0x10e:
    case 0x10f:
    case 0x114:
    case 0x115:
    case 0x116:
    case 0x117:
    case 0x11c:
    case 0x11e:
    case 0x11f:
    case 0x14c:
    case 0x14d:
    case 0x14e:
    case 0x14f:
    case 0x154:
    case 0x155:
    case 0x156:
    case 0x157:
    case 0x15c:
    case 0x15d:
    case 0x15e:
    case 0x15f:
    case 0x188:
    case 0x18a:
    case 0x198:
    case 0x19a:
    case 0x1a8:
    case 0x1aa:
    case 0x1b8:
    case 0x1ba:
    case 0x1c8:
    case 0x1ca:
    case 0x1d8:
    case 0x1da:
    case 0x1e8:
    case 0x1ea:
    case 0x1f8:
    case 0x1fa:
    case 0x248:
    case 0x24a:
    case 0x258:
    case 0x25a:
    case 0x268:
    case 0x26a:
    case 0x278:
    case 0x27a:
    case 0x288:
    case 0x28a:
    case 0x298:
    case 0x29a:
    case 0x2a8:
    case 0x2aa:
    case 0x2b8:
    case 0x2ba:
    case 0x2c8:
    case 0x2ca:
    case 0x2d8:
    case 0x2da:
    case 0x2e8:
    case 0x2ea:
    case 0x2f8:
    case 0x2fa:
    case 0x30c:
    case 0x30d:
    case 0x30e:
    case 0x30f:
    case 0x314:
    case 0x315:
    case 0x316:
    case 0x317:
    case 0x31c:
    case 0x31e:
    case 0x31f:
    case 0x348:
    case 0x349:
    case 0x34a:
    case 0x34b:
    case 0x34c:
    case 0x34d:
    case 0x34e:
    case 0x34f:
    case 0x350:
    case 0x351:
    case 0x352:
    case 0x353:
    case 0x354:
    case 0x355:
    case 0x356:
    case 0x357:
    case 0x358:
    case 0x359:
    case 0x35a:
    case 0x35b:
    case 0x35c:
    case 0x35e:
    case 0x35f:
    case 0x368:
    case 0x36a:
    case 0x378:
    case 0x37a:
    case 0x388:
    case 0x38a:
    case 0x398:
    case 0x39a:
    case 0x3aa:
    case 0x3c8:
    case 0x3ca:
    case 0x3d8:
    case 0x3da:
    case 0x3e8:
    case 0x3ea:
    case 0x3f8:
    case 0x3fa:
      return true;
    default:
      return false;
    }
  }

  /**
   * Arrays() - test all 2 parameter combinations.
   */
  public void Var003() {
    StringBuffer finalSb = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    long startTime = System.currentTimeMillis();
    long printTime = startTime + 2000;
    try {
      int count = 0;
      int totalCount = (0x20-0x8) * (0x20 - 0x08);
      for (int i = 0x8; (i < 0x20); i++) { 

        for (int j = 0x8; (j < 0x20); j++) {
          count++;
          int combo = i << 5 | j;
          long currentTime = System.currentTimeMillis();
          if (currentTime > printTime) {
            long elapsedTime = currentTime - startTime;
            double totalEstimatedTime = (double) elapsedTime / (double) count
                * (double) totalCount;
            int secondsLeft = (int) (totalEstimatedTime - elapsedTime) / 1000;
            System.out.println("Currently processing " + count + "/"
                + totalCount + " combo 0x" + Integer.toHexString(combo)
                + " about " + secondsLeft + "/"
                + ((int) (totalEstimatedTime / 1000)) + " seconds left");

            printTime = currentTime + 2000;
          }
          if (SQL0901failure(combo)) {
            /* do nothing */
          } else if (nullCharOuputFailure(combo)) {
            /* do nothing */
          } else {
            boolean thisPassed = testArrayCombination(combo, sb);
            passed = thisPassed & passed;
            if (!thisPassed) {
              finalSb.append(sb);
            }
            sb.setLength(0);
          }
        }
      }
      assertCondition(passed, finalSb);
    } catch (Exception e) {
      failed(connection_, e, sb);
    }

  }

  /**
   * Arrays() - 0x10c, etc combinations fails with because of ZDA bug with NULL
   * normal variable output
   */

  public void Var004() {
    StringBuffer finalSb = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    long startTime = System.currentTimeMillis();
    long printTime = startTime + 2000;
    try {
      int count = 0;
      int totalCount = (0x20-0x8) * (0x20 - 0x08);
      for (int i = 0x8; (i < 0x20); i++) { 

        for (int j = 0x8; (j < 0x20); j++) {
          count++;
          int combo = i << 5 | j;
          long currentTime = System.currentTimeMillis();
          if (currentTime > printTime) {
            long elapsedTime = currentTime - startTime;
            double totalEstimatedTime = (double) elapsedTime / (double) count
                * (double) totalCount;
            int secondsLeft = (int) (totalEstimatedTime - elapsedTime) / 1000;
            System.out.println("Currently processing " + count + "/"
                + totalCount + " combo 0x" + Integer.toHexString(combo)
                + " about " + secondsLeft + "/"
                + ((int) (totalEstimatedTime / 1000)) + " seconds left");

            printTime = currentTime + 2000;
          }
          if (nullCharOuputFailure(combo)) {
            boolean thisPassed = testArrayCombination(combo, sb);
            passed = thisPassed & passed;
            if (!thisPassed) {
              finalSb.append(sb);
            }
            sb.setLength(0);
          }
        }
      }
      assertCondition(passed, finalSb);
    } catch (Exception e) {
      failed(connection_,e, sb);
    }

  }

  
  /**
   * Arrays() - 0x13d, etc combinations fails with because of SQL0901
   */

  public void Var005() {
    StringBuffer finalSb = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    long startTime = System.currentTimeMillis();
    long printTime = startTime + 2000;
    try {
      int count = 0;
      int totalCount = (0x20-0x8) * (0x20 - 0x08);
      for (int i = 0x8; (i < 0x20); i++) { 

        for (int j = 0x8; (j < 0x20); j++) {
          count++;
          int combo = i << 5 | j;
          long currentTime = System.currentTimeMillis();
          if (currentTime > printTime) {
            long elapsedTime = currentTime - startTime;
            double totalEstimatedTime = (double) elapsedTime / (double) count
                * (double) totalCount;
            int secondsLeft = (int) (totalEstimatedTime - elapsedTime) / 1000;
            System.out.println("Currently processing " + count + "/"
                + totalCount + " combo 0x" + Integer.toHexString(combo)
                + " about " + secondsLeft + "/"
                + ((int) (totalEstimatedTime / 1000)) + " seconds left");

            printTime = currentTime + 2000;
          }
          if (SQL0901failure(combo)) {
            boolean thisPassed = testArrayCombination(combo, sb);
            passed = thisPassed & passed;
            if (!thisPassed) {
              finalSb.append(sb);
            }
            sb.setLength(0);
          }
        }
      }
      assertCondition(passed, finalSb);
    } catch (Exception e) {
      failed(connection_,e, sb);
    }

  }

  /* Randomly test 3 parameter combinations */
  
  public void Var006() {

    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    long currentTime = System.currentTimeMillis();

    long startTime = System.currentTimeMillis();
    long printTime = startTime + 2000;
    long totalMillis = 30000; /* run for 30 seconds */ 
    long endTime = currentTime + totalMillis; 
    Random random = new Random(); 
    try {
      
      while (passed && (currentTime < endTime)) { 
          sb.setLength(0);
          long combo = (0x8 + random.nextInt(0x18)) << 10 |
                       (0x8 + random.nextInt(0x18)) << 5 | 
                       (0x8 + random.nextInt(0x18)); 
      
         
          if (currentTime > printTime) {
            long elapsedTime = currentTime - startTime;
            int secondsLeft = (int) (totalMillis - elapsedTime) / 1000;
            System.out.println("Currently processing combo 0x" + Long.toHexString(combo)
                + " about " + secondsLeft + " seconds left");

            printTime = currentTime + 2000;
          }
          
           passed = testArrayCombination(combo, sb);
           currentTime = System.currentTimeMillis();
      }
      assertCondition(passed, sb);
    } catch (Exception e) {
      failed(connection_,e, sb);
    }

  }

  /* Randomly test 4 parameter combinations */
  
  public void Var007() {

    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    long currentTime = System.currentTimeMillis();

    long startTime = System.currentTimeMillis();
    long printTime = startTime + 2000;
    long totalMillis = 30000; /* run for 30 seconds */ 
    long endTime = currentTime + totalMillis; 
    Random random = new Random(); 
    try {
      
      while (passed && (currentTime < endTime)) { 
          sb.setLength(0);
          long combo = (0x8 + random.nextInt(0x18)) << 15|
                       (0x8 + random.nextInt(0x18)) << 10 |
                       (0x8 + random.nextInt(0x18)) << 5 | 
                       (0x8 + random.nextInt(0x18)); 
      
         
          if (currentTime > printTime) {
            long elapsedTime = currentTime - startTime;
            int secondsLeft = (int) (totalMillis - elapsedTime) / 1000;
            System.out.println("Currently processing combo 0x" + Long.toHexString(combo)
                + " about " + secondsLeft + " seconds left");

            printTime = currentTime + 2000;
          }
          
           passed = testArrayCombination(combo, sb);
           currentTime = System.currentTimeMillis();
      }
      assertCondition(passed, sb);
    } catch (Exception e) {
      failed(connection_,e, sb);
    }

  }


  /* Randomly test 5 parameter combinations */
  
  public void Var008() {

    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    long currentTime = System.currentTimeMillis();

    long startTime = System.currentTimeMillis();
    long printTime = startTime + 2000;
    long totalMillis = 30000; /* run for 30 seconds */ 
    long endTime = currentTime + totalMillis; 
    Random random = new Random(); 
    try {
      
      while (passed && (currentTime < endTime)) { 
          sb.setLength(0);
          long combo = (0x8 + random.nextInt(0x18)) << 20|
                       (0x8 + random.nextInt(0x18)) << 15|
                       (0x8 + random.nextInt(0x18)) << 10 |
                       (0x8 + random.nextInt(0x18)) << 5 | 
                       (0x8 + random.nextInt(0x18)); 
      
         
          if (currentTime > printTime) {
            long elapsedTime = currentTime - startTime;
            int secondsLeft = (int) (totalMillis - elapsedTime) / 1000;
            System.out.println("Currently processing combo 0x" + Long.toHexString(combo)
                + " about " + secondsLeft + " seconds left");

            printTime = currentTime + 2000;
          }
          
           passed = testArrayCombination(combo, sb);
           currentTime = System.currentTimeMillis();
      }
      assertCondition(passed, sb);
    } catch (Exception e) {
      failed(connection_,e, sb);
    }

  }


  
  
}
