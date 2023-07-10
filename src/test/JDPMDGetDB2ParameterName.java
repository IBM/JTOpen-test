///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPMDGetDB2ParameterName.java
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
// File Name:    JDPMDGetDB2ParameterName.java
//
// Classes:      JDPMDGetDB2ParameterName
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCParameterMetaData;


import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.util.Hashtable;

/**
 * Testcase JDPSetInt. This tests the following method of the JDBC
 * ParameterMetaData class:
 * 
 * <ul>
 * <li>GetDB2ParameterName()
 * </ul>
 **/
public class JDPMDGetDB2ParameterName extends JDTestcase {

  // Private data.
  private Connection connection_;
  private PreparedStatement ps;
  private ParameterMetaData pmd;

  /**
   * Constructor.
   **/
  public JDPMDGetDB2ParameterName(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPMDGetDB2ParameterName", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    String url = baseURL_ + ";data truncation=true";
    connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    connection_.close();
  }

  /**
   * GetDB2ParameterName() - Should throw exception if the statement gets
   * closed.
   **/
  public void Var001() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        ps.close();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(1);

          failed("Didn't throw SQLException " + name);
        } else {
          notApplicable("Toolbox test");
        }
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should throw exception if the index is too small.
   **/
  public void Var002() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {

          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(0);

          failed("Didn't throw SQLException " + name);
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should throw exception if the index is too large.
   **/
  public void Var003() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {

          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(100);

          failed("Didn't throw SQLException" + name);
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work for a statement after the statement has
   * been executed.
   **/
  public void Var004() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        CallableStatement cs = (CallableStatement) JDPMDTest.getStatement(
            JDPMDTest.DIRECTIONS_CALLSTMT, connection_, supportedFeatures_);
        cs.setInt(1, 1);
        cs.setInt(3, 1);
        cs.registerOutParameter(2, java.sql.Types.INTEGER);
        cs.registerOutParameter(3, java.sql.Types.INTEGER);

        cs.executeUpdate();
        pmd = cs.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(1);
          cs.close();
          assertEqualStrings(name, "P1");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work for a stored procedure call with an
   * output parameter.
   **/
  public void Var005() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      StringBuffer sb = new StringBuffer();
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.RETURN_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          try {
            String name = ((AS400JDBCParameterMetaData) pmd)
                .getDB2ParameterName(1);
            failed(
                "Should have thrown exception trying to access return parameter "
                    + name);
          } catch (Exception e) {
            assertExceptionContains(e, "index not valid", sb);
          }
          try {
            ps.close();
          } catch (Exception e) {
          }

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of smallint.
   **/
  public void Var006() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(1);
          ps.close();
          assertEqualStrings(name, "00001");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of integer.
   **/
  public void Var007() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(2);
          ps.close();
          assertEqualStrings(name, "00002");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of real.
   **/
  public void Var008() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(3);
          ps.close();
          assertEqualStrings(name, "00003");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of float.
   **/
  public void Var009() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(4);
          ps.close();
          assertEqualStrings(name, "00004");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of double.
   **/
  public void Var010() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(5);
          ps.close();
          assertEqualStrings(name, "00005");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of decimal.
   **/
  public void Var011() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(6);
          ps.close();
          assertEqualStrings(name, "00006");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of numeric.
   **/
  public void Var012() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(7);
          ps.close();
          assertEqualStrings(name, "00007");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of char.
   **/
  public void Var013() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(8);
          ps.close();
          assertEqualStrings(name, "00008");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of varchar.
   **/
  public void Var014() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(9);
          ps.close();
          assertEqualStrings(name, "00009");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of binary.
   **/
  public void Var015() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(10);
          ps.close();
          assertEqualStrings(name, "00010");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of varbinary.
   **/
  public void Var016() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(11);
          ps.close();
          assertEqualStrings(name, "00011");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of date.
   **/
  public void Var017() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(12);
          ps.close();
          assertEqualStrings(name, "00012");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of time.
   **/
  public void Var018() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(13);
          ps.close();
          assertEqualStrings(name, "00013");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of timestamp.
   **/
  public void Var019() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(14);
          ps.close();
          assertEqualStrings(name, "00014");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of blob.
   **/
  public void Var020() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(15);
          ps.close();

          assertEqualStrings(name, "00015");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of clob.
   **/
  public void Var021() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(16);
          ps.close();
          assertEqualStrings(name, "00016");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of dbclob.
   **/
  public void Var022() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(17);
          ps.close();

          assertEqualStrings(name, "00017");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of datalink.
   **/
  public void Var023() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(18);
          ps.close();

          // The correct type for this is (I feel) java.net.URL per the
          // JDBC 3.0 specification. Before that release, it should
          // be treated as a String.

          assertEqualStrings(name, "00018");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for an insert
   * for data type of bigint.
   **/
  public void Var024() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(19);
          ps.close();
          assertEqualStrings(name, "00019");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of smallint.
   **/
  public void Var025() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(1);
          ps.close();
          assertEqualStrings(name, "00001");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of integer.
   **/
  public void Var026() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(2);
          ps.close();
          assertEqualStrings(name, "00002");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of real.
   **/
  public void Var027() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(3);
          ps.close();
          assertEqualStrings(name, "00003");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of float.
   **/
  public void Var028() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(4);
          ps.close();
          assertEqualStrings(name, "00004");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of double.
   **/
  public void Var029() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(5);
          ps.close();
          assertEqualStrings(name, "00005");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of decimal.
   **/
  public void Var030() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(6);
          ps.close();
          assertEqualStrings(name, "00006");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of numeric.
   **/
  public void Var031() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(7);
          ps.close();
          assertEqualStrings(name, "00007");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of char.
   **/
  public void Var032() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(8);
          ps.close();
          assertEqualStrings(name, "00008");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of varchar.
   **/
  public void Var033() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(9);
          ps.close();
          assertEqualStrings(name, "00009");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of binary.
   **/
  public void Var034() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(10);
          ps.close();
          assertEqualStrings(name, "00010");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of varbinary.
   **/
  public void Var035() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(11);
          ps.close();
          assertEqualStrings(name, "00011");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of date.
   **/
  public void Var036() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(12);
          ps.close();
          assertEqualStrings(name, "00012");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of time.
   **/
  public void Var037() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(13);
          ps.close();
          assertEqualStrings(name, "00013");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of timestamp.
   **/
  public void Var038() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(14);
          ps.close();
          assertEqualStrings(name, "00014");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of blob.
   **/
  public void Var039() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(15);
          ps.close();
          assertEqualStrings(name, "00015");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of clob.
   **/
  public void Var040() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(16);
          ps.close();

          assertEqualStrings(name, "00016");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of dbclob.
   **/
  public void Var041() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(17);
          ps.close();

          assertEqualStrings(name, "00017");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a prepared statement for a query
   * for data type of bigint.
   **/
  public void Var042() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(18);
          ps.close();
          assertEqualStrings(name, "00018");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of smallint.
   **/
  public void Var043() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(1);
          ps.close();

          assertEqualStrings(name, "P_SMALLINT");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of integer.
   **/
  public void Var044() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(2);
          ps.close();
          assertEqualStrings(name, "P_INTEGER");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of real.
   **/
  public void Var045() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(3);
          ps.close();
          assertEqualStrings(name, "P_REAL");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of float.
   **/
  public void Var046() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(4);
          ps.close();
          assertEqualStrings(name, "P_FLOAT");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of double.
   **/
  public void Var047() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(5);
          ps.close();
          assertEqualStrings(name, "P_DOUBLE");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of decimal.
   **/
  public void Var048() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(6);
          ps.close();
          assertEqualStrings(name, "P_DECIMAL_50");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of numeric.
   **/
  public void Var049() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(8);
          ps.close();
          assertEqualStrings(name, "P_NUMERIC_50");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of char.
   **/
  public void Var050() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(10);
          ps.close();
          assertEqualStrings(name, "P_CHAR_1");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of varchar.
   **/
  public void Var051() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(12);
          ps.close();
          assertEqualStrings(name, "P_VARCHAR_50");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of binary.
   **/
  public void Var052() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(13);
          ps.close();
          assertEqualStrings(name, "P_BINARY_20");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of varbinary.
   **/
  public void Var053() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(14);
          ps.close();
          assertEqualStrings(name, "P_VARBINARY_20");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of date.
   **/
  public void Var054() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(15);
          ps.close();
          assertEqualStrings(name, "P_DATE");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of time.
   **/
  public void Var055() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(16);
          ps.close();
          assertEqualStrings(name, "P_TIME");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of timestamp.
   **/
  public void Var056() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(17);
          ps.close();
          assertEqualStrings(name, "P_TIMESTAMP");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of datalink.
   **/
  public void Var057() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(18);
          ps.close();
          // Handle datalinks...
          assertEqualStrings(name, "P_DATALINK"); // @A1C
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of blob.
   **/
  public void Var058() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(19);
          ps.close();

          assertEqualStrings(name, "P_BLOB");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of clob.
   **/
  public void Var059() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(20);
          ps.close();

          assertEqualStrings(name, "P_CLOB");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of dbclob.
   **/
  public void Var060() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(21);
          ps.close();

          assertEqualStrings(name, "P_DBCLOB");

        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of bigint.
   **/
  public void Var061() {
    if (checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(22);
          ps.close();
          assertEqualStrings(name, "P_BIGINT");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


      /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of decfloat.
   **/
  public void Var062() {
    if (checkBooleanSupport()) 
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(23);
          ps.close();
          assertEqualStrings(name, "P_DECFLOAT16");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  
    /**
   * GetDB2ParameterName() - Should work with a callable statement for a
   * procedure call for data type of boolean.
   **/
  public void Var063() {
    if (checkBooleanSupport()) 
    {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        if (pmd instanceof AS400JDBCParameterMetaData) {
          String name = ((AS400JDBCParameterMetaData) pmd)
              .getDB2ParameterName(25);
          ps.close();
          assertEqualStrings(name, "P_BOOLEAN");
        } else {
          notApplicable("toolbox test");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


  
  
}
