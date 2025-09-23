///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetArray.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetArray.java
//
// Classes:      JDCSGetArray
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;

/**
 * Testcase JDCSGetArray. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * <ul>
 * <li>getArray()
 * </ul>
 **/
public class JDCSGetArray extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetArray";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

  // Private data.
  private Connection connection_;
  private CallableStatement csTypes_;

  /**
   * Constructor.
   **/
  public JDCSGetArray(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDCSGetArray", namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    try {
      JDSetupProcedure.resetCollection(collection_);
      connection_ = testDriver_.getConnection(baseURL_ + ";errors=full",
          userId_, encryptedPassword_);

      csTypes_ = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
      JDSetupProcedure.register(csTypes_, JDSetupProcedure.STP_CSTYPESOUT,
          supportedFeatures_, getDriver());
      csTypes_.execute();

      //// v7r1 array SPs
      if (supportedFeatures_.arraySupport) {
        Statement st = connection_.createStatement();
        try {
          st.execute(
              "drop type " + JDSetupProcedure.COLLECTION + ".JDINTARRAY ");
        } catch (Exception e) {
        }
        try {
          st.execute("create type " + JDSetupProcedure.COLLECTION
              + ".JDINTARRAY as integer array[100] ");

        } catch (Exception e) {
        }
        st.close();

        JDSetupProcedure.create(systemObject_,  connection_,
            JDSetupProcedure.STP_CSARRSUM, supportedFeatures_);

        JDSetupProcedure.create(systemObject_, connection_,
            JDSetupProcedure.STP_CSARRINT, supportedFeatures_);
      }
    } catch (Exception e) {
      System.out.println("WARNING:  Uncaught exception in setup()");
      e.printStackTrace();
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    //// v7r1 array SPs
      Statement st = connection_.createStatement();
      try {
        st.execute(
            "drop type  " + JDSetupProcedure.COLLECTION + ".JDINTARRAY ");
      } catch (Exception e) {
      }

      st.close();

      JDSetupProcedure.dropProcedure(connection_,
          JDSetupProcedure.STP_CSARRSUM);
      JDSetupProcedure.dropProcedure(connection_,
          JDSetupProcedure.STP_CSARRINT);
    

    connection_.close();
  }

  /**
   * getArray() - Get parameter -1.
   **/
  public void Var001() {
    if (checkJdbc20()) {
      try {
        csTypes_.execute();
        Array p = csTypes_.getArray(-1);
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get parameter 0.
   **/
  public void Var002() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(0);
        failed("Didn't throw SQLException" + p + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Use a parameter that is too big.
   **/
  public void Var003() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(35);
        failed("Didn't throw SQLException" + p + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a parameter when there are no parameters.
   **/
  public void Var004() {
    if (checkJdbc20()) {
      // I created a whole new Connection object here to work
      // around a server bug.
      Connection c = null;
      try {
        c = testDriver_.getConnection(baseURL_ + ";errors=full", userId_, encryptedPassword_);
        CallableStatement cs = c
            .prepareCall("CALL " + JDSetupProcedure.STP_CS0);
        cs.execute();
        Array p = cs.getArray(1);
        failed("Didn't throw SQLException" + p + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
        try {
          c.close();
        } catch (SQLException e) {
          // Ignore.
        }
      }
    }
  }

  /**
   * getArray() - Get a parameter that was not registered.
   **/
  public void Var005() {
    if (checkJdbc20()) {
      try {
        CallableStatement cs = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
        cs.execute();
        Array p = cs.getArray(12);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a parameter when the statement has not been executed.
   **/
  public void Var006() {
    if (checkJdbc20()) {
      try {
        CallableStatement cs = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
        cs.registerOutParameter(12, Types.ARRAY);
        Array p = cs.getArray(12);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a parameter when the statement is closed.
   **/
  public void Var007() {
    if (checkJdbc20()) {
      try {
        CallableStatement cs = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
        cs.registerOutParameter(12, Types.ARRAY);
        cs.execute();
        cs.close();
        Array p = cs.getArray(12);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get an IN parameter that was correctly registered.
   **/
  public void Var008() {
    if (checkJdbc20()) {
      try {
        CallableStatement cs = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSTYPESIN,
            supportedFeatures_);
        cs.registerOutParameter(12, Types.ARRAY);
        cs.execute();
        Array p = cs.getArray(12);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a SMALLINT.
   **/
  public void Var009() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(1);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an INTEGER.
   **/
  public void Var010() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(2);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an REAL.
   **/
  public void Var011() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(3);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an FLOAT.
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(4);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an DOUBLE.
   **/
  public void Var013() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(5);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an DECIMAL.
   **/
  public void Var014() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(6);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an NUMERIC.
   **/
  public void Var015() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(8);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a CHAR(1).
   **/
  public void Var016() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(10);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a CHAR(50).
   **/
  public void Var017() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(11);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a VARCHAR(50).
   **/
  public void Var018() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(12);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a BINARY.
   **/
  public void Var019() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(13);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a VARBINARY.
   **/
  public void Var020() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(14);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a DATE.
   **/
  public void Var021() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(15);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a TIME.
   **/
  public void Var022() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(16);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a TIMESTAMP.
   **/
  public void Var023() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(17);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an OTHER.
   **/
  public void Var024() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(18);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a BLOB.
   **/
  public void Var025() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(19);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a CLOB.
   **/
  public void Var026() {
    if (checkJdbc20()) {
      try {
        Array p = csTypes_.getArray(20);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a BIGINT.
   **/
  public void Var027() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          Array p = csTypes_.getArray(22);
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  // - named paremeters
  /**
   * getArray() - Get a type that was registered as a SMALLINT.
   **/
  public void Var028() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_SMALLINT");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an INTEGER.
   **/
  public void Var029() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_INTEGER");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an REAL.
   **/
  public void Var030() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_REAL");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an FLOAT.
   **/
  public void Var031() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_FLOAT");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an DOUBLE.
   **/
  public void Var032() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_DOUBLE");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an DECIMAL.
   **/
  public void Var033() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_DECIMAL_50");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an DECIMAL.
   **/
  public void Var034() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_DECIMAL_105");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an NUMERIC.
   **/
  public void Var035() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_NUMERIC_50");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an NUMERIC.
   **/
  public void Var036() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_NUMERIC_105");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a CHAR(1).
   **/
  public void Var037() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_CHAR_1");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a CHAR(50).
   **/
  public void Var038() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_CHAR_50");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a VARCHAR(50).
   **/
  public void Var039() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_VARCHAR_50");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a BINARY.
   **/
  public void Var040() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_BINARY_20");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a VARBINARY.
   **/
  public void Var041() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_VARBINARY_20");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a DATE.
   **/
  public void Var042() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_DATE");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a TIME.
   **/
  public void Var043() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_TIME");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a TIMESTAMP.
   **/
  public void Var044() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_TIMESTAMP");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as an OTHER.
   **/
  public void Var045() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_DATALINK");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a BLOB.
   **/
  public void Var046() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_BLOB");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a CLOB.
   **/
  public void Var047() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        try {
          Array p = csTypes_.getArray("P_CLOB");
          failed("Didn't throw SQLException" + p);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a BIGINT.
   **/
  public void Var048() {
    if (checkNamedParametersSupport()) {
      if (checkJdbc20()) {
        if (checkBigintSupport()) {
          try {
            Array p = csTypes_.getArray("P_BIGINT");
            failed("Didn't throw SQLException" + p);
          } catch (Exception e) {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          }
        }
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a DECFLOAT(16).
   **/
  public void Var049() {
    if (checkDecFloatSupport()) {
      try {
        Array p = csTypes_.getArray("P_DECFLOAT16");
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get a type that was registered as a DECFLOAT(16).
   **/
  public void Var050() {
    if (checkDecFloatSupport()) {
      try {
        Array p = csTypes_.getArray("P_DECFLOAT34");
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get an ARRAY of ints
   **/
  public void Var051() {
    try {

      //////// create array and set/get data
      // check local copy of array and arrayResultSet
      boolean check1 = true;
      Integer[] iaCheck = new Integer[6]; // 1,22,23,4,5,6
      iaCheck[0] = Integer.valueOf(1);
      iaCheck[1] = Integer.valueOf(22);
      iaCheck[2] = Integer.valueOf(23);
      iaCheck[3] = Integer.valueOf(4);
      iaCheck[4] = Integer.valueOf(5);
      iaCheck[5] = Integer.valueOf(6);

      CallableStatement cs = connection_
          .prepareCall("call " + JDSetupProcedure.STP_CSARRINT + " ( ?, ?) ");
      cs.registerOutParameter(1, java.sql.Types.INTEGER);
      cs.registerOutParameter(2, java.sql.Types.ARRAY);
      cs.execute();

      Array b = (Array) cs.getObject(2);
      boolean check2 = false;
      System.out.println(((Array) b).getBaseTypeName());
      if (((Array) b).getBaseTypeName().equals("INTEGER"))
        check2 = true;

      // int[] i = (int[])b.getArray();

      /*
       * iaCheck = (Integer[])b.getArray(); for(int x=0;x<iaCheck.length;x++) {
       * // iaCheck[x].toString(); //System.out.println(i[x].toString()); }
       */

      boolean check3 = true;
      ResultSet retRS = b.getResultSet();
      int count = 0;
      while (retRS.next()) {
        count++;

        // check column in resultset is index into array
        if (iaCheck[retRS.getInt(1) - 1].intValue() != retRS.getInt(2))
          check3 = false;
      }

      assertCondition(check1 && check2 && check3 && count == 6);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getArray() - Get an ARRAY of ints
   **/
  public void Var052() {
   
    try {

      //////// create array and set/get data
      // check local copy of array and arrayResultSet
      boolean check1 = true;
      Integer[] ia = new Integer[6]; // 1,22,23,4,5,6
      ia[0] = Integer.valueOf(1);
      ia[1] = Integer.valueOf(22);
      ia[2] = Integer.valueOf(23);
      ia[3] = Integer.valueOf(4);
      ia[4] = Integer.valueOf(5);
      ia[5] = Integer.valueOf(6);
      java.sql.Array a;
      if (isJdbc40() || isToolboxDriver() // TB put createArrayOf into JDBC30
                                          // for testing
          || getDriver() == JDTestDriver.DRIVER_NATIVE) /* call createArrayOf */
      {
        a = (Array) JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "INTEGER", ia); // c.createArrayOf("INTEGER", ia);
      } else {
        // get array the old-fashioned way
        a = null;// add code
      }

      java.sql.ResultSet retRS = a.getResultSet();
      Integer[] iaCheck = (Integer[]) a.getArray();
      // iaCheck and ia should contian same here

      // do a couple of pre-sanity checks to see if input array values are still
      // same out output from Array.getResultSet values
      while (retRS.next()) {
        // first column in resultset is index into array
        if (ia[retRS.getInt(1) - 1].intValue() != iaCheck[retRS.getInt(1) - 1]
            .intValue())
          check1 = false;

        if (iaCheck[retRS.getInt(1) - 1].intValue() != retRS.getInt(2)) // column
                                                                        // 1 is
                                                                        // index
                                                                        // (1-based)
                                                                        // and
                                                                        // column
                                                                        // 2 of
                                                                        // RS
                                                                        // contains
                                                                        // actual
                                                                        // data
          check1 = false;
      }

      CallableStatement cs = connection_
          .prepareCall("call " + JDSetupProcedure.STP_CSARRINT + " ( ?, ?) ");
      cs.registerOutParameter(1, java.sql.Types.INTEGER);
      cs.registerOutParameter(2, java.sql.Types.ARRAY);
      cs.execute();

      Array b = (Array) cs.getObject(2);
      boolean check2 = false;
      System.out.println(((Array) b).getBaseTypeName());
      if (((Array) b).getBaseTypeName().equals("INTEGER"))
        check2 = true;

      // int[] i = (int[])b.getArray();

      /*
       * iaCheck = (Integer[])b.getArray(); for(int x=0;x<iaCheck.length;x++) {
       * // iaCheck[x].toString(); //System.out.println(i[x].toString()); }
       */

      boolean check3 = true;
      retRS = b.getResultSet();
      int count = 0;
      while (retRS.next()) {
        count++;
        int c1 = retRS.getInt(1);
        int c2 = retRS.getInt(2);

        // check column in resultset is index into array
        if (iaCheck[c1 - 1].intValue() != c2)
          check3 = false;
      }

      assertCondition(check1 && check2 && check3 && count == 6);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  public String stringFromArrayElement(Object o) throws SQLException {
    if (o == null) {
      return null;
    } else {
      if (o instanceof Clob) {
        Clob c = (Clob) o;
        long length = c.length();
        return c.getSubString(1, (int) length);
      } else {
        return o.toString();
      }
    }
  }

  public void testOutputArray(String typeName, String typeDefinition,
      String procedureName, String procedureDefinition, String expected) {
    

    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      String sql = "";
      typeName = collection_ + "." + typeName;
      procedureName = collection_ + "." + procedureName;
      try {

        sql = "drop procedure " + procedureName;
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
      }

      try {
        sql = "drop type " + typeName;
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
      }

      sql = "CREATE TYPE " + typeName + " " + typeDefinition;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "CREATE PROCEDURE " + procedureName + "(out numList " + typeName
          + ")" + procedureDefinition;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "call " + procedureName + " ( ? ) ";
      sb.append("Executing " + sql + "\n");
      CallableStatement cs = connection_.prepareCall(sql);
      cs.registerOutParameter(1, java.sql.Types.ARRAY);
      cs.execute();

      Array b = (Array) cs.getObject(1);
      StringBuffer arrayContents = new StringBuffer();
      if (b == null) {
        arrayContents.append("null");
      } else {
        Object[] array = (Object[]) b.getArray();
        arrayContents.append("[");
        int arrayLength = array.length;
        for (int i = 0; i < arrayLength; i++) {
          if (i > 0)
            arrayContents.append(",");
          arrayContents.append(stringFromArrayElement(array[i]));
        }
        arrayContents.append("]");
      }

      String arrayContentsString = arrayContents.toString();
      passed = arrayContentsString.equals(expected);
      if (!passed) {
        sb.append("Got '" + arrayContentsString + "' sb '" + expected + "'\n");
      }

      sql = "drop procedure " + procedureName;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "drop type " + typeName;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString());
    }

  }

  /**
   * getArray() - Get an ARRAY of smallints
   **/
  public void Var053() {

    testOutputArray("JDCSSMI", "as smallint array[100]", "JDCSSMIO",
        "begin   set numList = ARRAY[1,2,3,4,5,6,7];  end", "[1,2,3,4,5,6,7]");

  }

  /**
   * getArray() - Get an ARRAY of smallints with null entries
   **/
  public void Var054() {

    testOutputArray("JDCSSMI", "as smallint array[100]", "JDCSSMIO",
        "begin   set numList = ARRAY[null,2,3,null,5,6,null];  end",
        "[null,2,3,null,5,6,null]");

  }

  /**
   * getArray() - Get a null ARRAY of smallints
   **/
  public void Var055() {

    testOutputArray("JDCSSMI", "as smallint array[100]", "JDCSSMIO",
        "begin   set numList = null;  end", "null");

  }

  /**
   * getArray() - Get an ARRAY of BIGINTs
   **/
  public void Var056() {
    testOutputArray("JDCSBII", "as BIGINT array[100]", "JDCSSMIO",
        "begin   set numList = ARRAY[1,2,3,4,5,6,7];  end", "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of BIGINTs with null entries
   **/
  public void Var057() {

    testOutputArray("JDCSBII", "as BIGINT array[100]", "JDCSSMIO",
        "begin   set numList = ARRAY[null,2,3,null,5,6,null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of BIGINTs
   **/
  public void Var058() {

    testOutputArray("JDCSGA58", "as BIGINT array[100]", "JDCSGA58O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of DECIMALs
   **/
  public void Var059() {
    testOutputArray("JDCSGA59", "as DECIMAL(10,2) array[100]", "JDCSGA59O",
        "begin   set numList = ARRAY[1.00,2.00,3.00,4.00,5.00,6.00,7.00];  end",
        "[1.00,2.00,3.00,4.00,5.00,6.00,7.00]");
  }

  /**
   * getArray() - Get an ARRAY of DECIMALs with null entries
   **/
  public void Var060() {

    testOutputArray("JDCSGA60", "as DECIMAL(10,2) array[100]", "JDCSSGA60O",
        "begin   set numList = ARRAY[null,2.00,3.00,null,5.00,6.00,null];  end",
        "[null,2.00,3.00,null,5.00,6.00,null]");
  }

  /**
   * getArray() - Get a null ARRAY of DECIMALs
   **/
  public void Var061() {

    testOutputArray("JDCSGA61", "as DECIMAL(10,2) array[100]", "JDCSGA61O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of NUMERICs
   **/
  public void Var062() {
    testOutputArray("JDCSGA6", "as NUMERIC(10,2) array[100]", "JDCSGA62O",
        "begin   set numList = ARRAY[1,2,3,4,5,6,7];  end",
        "[1.00,2.00,3.00,4.00,5.00,6.00,7.00]");
  }

  /**
   * getArray() - Get an ARRAY of NUMERICs with null entries
   **/
  public void Var063() {

    testOutputArray("JDCSGA63", "as NUMERIC(10,2) array[100]", "JDCSGA63O",
        "begin   set numList = ARRAY[null,2,3,null,5,6,null];  end",
        "[null,2.00,3.00,null,5.00,6.00,null]");
  }

  /**
   * getArray() - Get a null ARRAY of NUMERICs
   **/
  public void Var064() {

    testOutputArray("JDCSGA64", "as NUMERIC(10,2) array[100]", "JDCSGA64O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of REALs
   **/
  public void Var065() {
    testOutputArray("JDCSGA65", "as REAL array[100]", "JDCSGA65O",
        "begin   set numList = ARRAY[1,2,3,4,5,6,7];  end",
        "[1.0,2.0,3.0,4.0,5.0,6.0,7.0]");
  }

  /**
   * getArray() - Get an ARRAY of REALs with null entries
   **/
  public void Var066() {

    testOutputArray("JDCSGA66", "as REAL array[100]", "JDCSGA66O",
        "begin   set numList = ARRAY[null,2,3,null,5,6,null];  end",
        "[null,2.0,3.0,null,5.0,6.0,null]");
  }

  /**
   * getArray() - Get a null ARRAY of REALs
   **/
  public void Var067() {

    testOutputArray("JDCSGA67", "as REAL array[100]", "JDCSGA67O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of DOUBLEs
   **/
  public void Var068() {
    testOutputArray("JDCSGA68", "as DOUBLE array[100]", "JDCSGA68O",
        "begin   set numList = ARRAY[1,2,3,4,5,6,7];  end",
        "[1.0,2.0,3.0,4.0,5.0,6.0,7.0]");
  }

  /**
   * getArray() - Get an ARRAY of DOUBLEs with null entries
   **/
  public void Var069() {

    testOutputArray("JDCSGA69", "as DOUBLE array[100]", "JDCSGA69O",
        "begin   set numList = ARRAY[null,2,3,null,5,6,null];  end",
        "[null,2.0,3.0,null,5.0,6.0,null]");
  }

  /**
   * getArray() - Get a null ARRAY of DOUBLEs
   **/
  public void Var070() {

    testOutputArray("JDCSGA70", "as DOUBLE array[100]", "JDCSGA70O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of DECFLOATs
   **/
  public void Var071() {
    testOutputArray("JDCSGA71", "as DECFLOAT array[100]", "JDCSGA71O",
        "begin   set numList = ARRAY[1,2,3,4,5,6,7];  end", "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of DECFLOATs with null entries
   **/
  public void Var072() {

    testOutputArray("JDCSGA72", "as DECFLOAT array[100]", "JDCSGA72O",
        "begin   set numList = ARRAY[null,2,3,null,5,6,null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of DECFLOATs
   **/
  public void Var073() {

    testOutputArray("JDCSGA73", "as DECFLOAT array[100]", "JDCSGA73O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of TIMEs
   **/
  public void Var074() {
    testOutputArray("JDCSGA74", "as TIME array[100]", "JDCSGA74O",
        "begin   set numList = ARRAY['1:01:01','2:01:01','3:01:01','4:01:01','5:01:01','6:01:01','7:01:01'];  end",
        "[01:01:01,02:01:01,03:01:01,04:01:01,05:01:01,06:01:01,07:01:01]");
  }

  /**
   * getArray() - Get an ARRAY of TIMEs with null entries
   **/
  public void Var075() {

    testOutputArray("JDCSGA75", "as TIME array[100]", "JDCSGA75O",
        "begin   set numList = ARRAY[null,'2:01:01','3:01:01',null,'5:01:01','6:01:01',null];  end",
        "[null,02:01:01,03:01:01,null,05:01:01,06:01:01,null]");
  }

  /**
   * getArray() - Get a null ARRAY of TIMEs
   **/
  public void Var076() {

    testOutputArray("JDCSGA76", "as TIME array[100]", "JDCSGA76O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of TIMESTAMPs
   **/
  public void Var077() {
    testOutputArray("JDCSGA77", "as TIMESTAMP array[100]", "JDCSGA77O",
        "begin   set numList = ARRAY['2011-01-01-01.01.01.0000','2011-02-01-01.01.01.0000','2011-03-01-01.01.01.0000','2011-04-01-01.01.01.0000','2011-05-01-01.01.01.0000','2011-06-01-01.01.01.0000','2011-07-01-01.01.01.0000'];  end",
        "[2011-01-01 01:01:01.0,2011-02-01 01:01:01.0,2011-03-01 01:01:01.0,2011-04-01 01:01:01.0,2011-05-01 01:01:01.0,2011-06-01 01:01:01.0,2011-07-01 01:01:01.0]");
  }

  /**
   * getArray() - Get an ARRAY of TIMESTAMPs with null entries
   **/
  public void Var078() {

    testOutputArray("JDCSGA78", "as TIMESTAMP array[100]", "JDCSGA78O",
        "begin   set numList = ARRAY[null,'2011-02-01-01.01.01.0000','2011-03-01-01.01.01.0000',null,'2011-05-01-01.01.01.0000','2011-06-01-01.01.01.0000',null];  end",
        "[null,2011-02-01 01:01:01.0,2011-03-01 01:01:01.0,null,2011-05-01 01:01:01.0,2011-06-01 01:01:01.0,null]");
  }

  /**
   * getArray() - Get a null ARRAY of TIMESTAMPs
   **/
  public void Var079() {

    testOutputArray("JDCSGA79", "as TIMESTAMP array[100]", "JDCSGA79O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of DATEs
   **/
  public void Var080() {
    testOutputArray("JDCSGA80", "as DATE array[100]", "JDCSGA80O",
        "begin   set numList = ARRAY['2011-01-01','2011-02-01','2011-03-01','2011-04-01','2011-05-01','2011-06-01','2011-07-01'];  end",
        "[2011-01-01,2011-02-01,2011-03-01,2011-04-01,2011-05-01,2011-06-01,2011-07-01]");
  }

  /**
   * getArray() - Get an ARRAY of DATEs with null entries
   **/
  public void Var081() {

    testOutputArray("JDCSGA81", "as DATE array[100]", "JDCSGA81O",
        "begin   set numList = ARRAY[null,'2011-02-01','2011-03-01',null,'2011-05-01','2011-06-01',null];  end",
        "[null,2011-02-01,2011-03-01,null,2011-05-01,2011-06-01,null]");
  }

  /**
   * getArray() - Get a null ARRAY of DATEs
   **/
  public void Var082() {
    testOutputArray("JDCSGA82", "as DATE array[100]", "JDCSGA82O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of CHARs
   **/
  public void Var083() {
    testOutputArray("JDCSGA83", "as CHAR(1) array[100]", "JDCSGA83O",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of CHARs with null entries
   **/
  public void Var084() {

    testOutputArray("JDCSGA84", "as CHAR(1) array[100]", "JDCSGA84O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of CHARs
   **/
  public void Var085() {

    testOutputArray("JDCSGA85", "as CHAR(1) array[100]", "JDCSGA85O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of CHARs
   **/
  public void Var086() {
    testOutputArray("JDCSGA86", "as CHAR(1) CCSID 1208 array[100]", "JDCSGA86O",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of CHARs with null entries
   **/
  public void Var087() {

    testOutputArray("JDCSGA87", "as CHAR(1) CCSID 1208 array[100]", "JDCSGA87O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of CHARs
   **/
  public void Var088() {

    testOutputArray("JDCSGA85", "as CHAR(1) CCSID 1208 array[100]", "JDCSGA88O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of VARCHARs
   **/
  public void Var089() {
    testOutputArray("JDCSGA89", "as VARCHAR(1) array[100]", "JDCSGA89O",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of VARCHARs with null entries
   **/
  public void Var090() {

    testOutputArray("JDCSGA90", "as VARCHAR(1) array[100]", "JDCSGA90O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of VARCHARs
   **/
  public void Var091() {

    testOutputArray("JDCSGA91", "as VARCHAR(1) array[100]", "JDCSGA91O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of VARCHARs
   **/
  public void Var092() {
    testOutputArray("JDCSGA92", "as VARCHAR(1) CCSID 1208 array[100]",
        "JDCSGA92O",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of VARCHARs with null entries
   **/
  public void Var093() {

    testOutputArray("JDCSGA93", "as VARCHAR(1) CCSID 1208 array[100]",
        "JDCSGA93O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of VARCHARs
   **/
  public void Var094() {

    testOutputArray("JDCSGA94", "as VARCHAR(1) CCSID 1208 array[100]",
        "JDCSGA94O", "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of GRAPHICs
   **/
  public void Var095() {
    testOutputArray("JDCSGA95", "as GRAPHIC(1) CCSID 13488  array[100]",
        "JDCSGA95O",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of GRAPHICs with null entries
   **/
  public void Var096() {

    testOutputArray("JDCSGA96", "as GRAPHIC(1) CCSID 13488 array[100]",
        "JDCSGA96O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of GRAPHICs
   **/
  public void Var097() {

    testOutputArray("JDCSGA97", "as GRAPHIC(1) CCSID 13488 array[100]",
        "JDCSGA97O", "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of GRAPHICs
   **/
  public void Var098() {
    testOutputArray("JDCSGA98", "as GRAPHIC(1) CCSID 1200 array[100]",
        "JDCSGA98O",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of GRAPHICs with null entries
   **/
  public void Var099() {

    testOutputArray("JDCSGA99", "as GRAPHIC(1) CCSID 1200 array[100]",
        "JDCSGA99O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of GRAPHICs
   **/
  public void Var100() {

    testOutputArray("JDCSGAA0", "as GRAPHIC(1) CCSID 1200 array[100]",
        "JDCSGAA0O", "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of VARGRAPHICs
   **/
  public void Var101() {
    testOutputArray("JDCSGAA1", "as VARGRAPHIC(1) CCSID 13488 array[100]",
        "JDCSGAA1O",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of VARGRAPHICs with null entries
   **/
  public void Var102() {

    testOutputArray("JDCSGAA2", "as VARGRAPHIC(1) CCSID 13488 array[100]",
        "JDCSGAA2O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of VARGRAPHICs
   **/
  public void Var103() {

    testOutputArray("JDCSGAA3", "as VARGRAPHIC(1) CCSID 13488 array[100]",
        "JDCSGAA3O", "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of VARGRAPHICs
   **/
  public void Var104() {
    testOutputArray("JDCSGAA4", "as VARGRAPHIC(1) CCSID 1200 array[100]",
        "JDCSGAA4O",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of VARGRAPHICs with null entries
   **/
  public void Var105() {

    testOutputArray("JDCSGAA6", "as VARGRAPHIC(1) CCSID 1200 array[100]",
        "JDCSGAA5O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of VARGRAPHICs
   **/
  public void Var106() {

    testOutputArray("JDCSGAA6", "as VARGRAPHIC(1) CCSID 1200 array[100]",
        "JDCSGAA6O", "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of VARGRAPHICs
   **/
  public void Var107() {
    testOutputArray("JDCSGAA7", "as VARGRAPHIC(20) CCSID 1200 array[100]",
        "JDCSGAAA70",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of VARGRAPHICs with null entries
   **/
  public void Var108() {

    testOutputArray("JDCSGAA8", "as VARGRAPHIC(20) CCSID 1200 array[100]",
        "JDCSGAAA8O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of VARGRAPHICs
   **/
  public void Var109() {

    testOutputArray("JDCSGAA9", "as VARGRAPHIC(20) CCSID 1200 array[100]",
        "JDCSGAAA9O", "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of CLOBs
   **/
  public void Var110() {
    testOutputArray("JDCSGAB0", "as CLOB(20) CCSID 1208 array[100]",
        "JDCSGAAB00",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of CLOBs with null entries
   **/
  public void Var111() {

    testOutputArray("JDCSGAB1", "as CLOB(20) CCSID 1208 array[100]",
        "JDCSGAAB1O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of CLOBs
   **/
  public void Var112() {

    testOutputArray("JDCSGAB2", "as CLOB(20) CCSID 1208 array[100]",
        "JDCSGAAB2O", "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of CLOBs
   **/
  public void Var113() {
    testOutputArray("JDCSGAB3", "as CLOB(20) CCSID 37 array[100]", "JDCSGAAB30",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of CLOBs with null entries
   **/
  public void Var114() {

    testOutputArray("JDCSGAB4", "as CLOB(20) CCSID 37 array[100]", "JDCSGAAB4O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of CLOBs
   **/
  public void Var115() {

    testOutputArray("JDCSGAB5", "as CLOB(20) CCSID 37 array[100]", "JDCSGAAB5O",
        "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get an ARRAY of DBCLOBs
   **/
  public void Var116() {
    testOutputArray("JDCSGAB6", "as DBCLOB(20) CCSID 1200 array[100]",
        "JDCSGAAB60",
        "begin   set numList = ARRAY['1','2','3','4','5','6','7'];  end",
        "[1,2,3,4,5,6,7]");
  }

  /**
   * getArray() - Get an ARRAY of DBCLOBs with null entries
   **/
  public void Var117() {

    testOutputArray("JDCSGAB7", "as DBCLOB(20) CCSID 1200 array[100]",
        "JDCSGAAB7O",
        "begin   set numList = ARRAY[null,'2','3',null,'5','6',null];  end",
        "[null,2,3,null,5,6,null]");
  }

  /**
   * getArray() - Get a null ARRAY of DBCLOBs
   **/
  public void Var118() {

    testOutputArray("JDCSGAB8", "as DBCLOB(20) CCSID 1200 array[100]",
        "JDCSGAAB8O", "begin   set numList = null;  end", "null");
  }

  /**
   * getArray() - Get a type that was registered as a BOOLEAN.
   **/
  public void Var119() {
    if (checkBooleanSupport()) {
      try {
        Array p = csTypes_.getArray(25);
        failed("Didn't throw SQLException" + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getArray() - Get an ARRAY of Booleans
   **/
  public void Var120() {
    if (checkBooleanSupport()) {
      testOutputArray("JDCSGAB9", "as BOOLEAN array[100]", "JDCSGAAB90",
          "begin   set numList = ARRAY[true,false,true,false, false, true, false];  end",
                                     "[true,false,true,false,false,true,false]");
    }
  }

  /**
   * getArray() - Get an ARRAY of Booleans with null entries
   **/
  public void Var121() {
    if (checkBooleanSupport()) {
      testOutputArray("JDCSGABA", "as BOOLEAN array[100]", "JDCSGAABAO",
          "begin   set numList = ARRAY[null,true,false,null,false,true,null];  end",
          "[null,true,false,null,false,true,null]");
    }
  }

  /**
   * getArray() - Get a null ARRAY of Booleans
   **/
  public void Var122() {
    if (checkBooleanSupport()) {
      testOutputArray("JDCSGABB", "as BOOLEAN array[100]", "JDCSGAABBO",
          "begin   set numList = null;  end", "null");
    }
  }

}
