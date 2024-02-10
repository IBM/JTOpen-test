///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetObjectBase.java
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
// File Name:    JDCSSetObjectBase.java
//
// Tests the setObject method with 3 parameters.. 
//
// Classes:      JDCSSetObjectBase
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

/**
 * Testcase JDCSSetObjectBase. Provide base function for JDCSSetObject3 and
 * JDCSSetObject4.
 **/
public class JDCSSetObjectBase extends JDCSSetTestcase {

  public final static int TYPES_DBCLOB = 104501;
  public final static int TYPES_DECFLOAT16 = 104502;
  public final static int TYPES_DECFLOAT34 = 104503;

  // Private data.
  protected Connection connection_;
  protected Statement statement_;

  protected Connection connectionCommaSeparator_;
  private Hashtable procedureDefinedHashtable = new Hashtable();
  private String distinctTypename_;

  /**
   * Constructor.
   **/
  public JDCSSetObjectBase(AS400 systemObject, String testcasename,
      Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, testcasename, namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    super.setup(); 
    String url = baseURL_  + ";data truncation=true";
    connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    connectionCommaSeparator_ = testDriver_.getConnection(url
        + ";decimal separator=,");
    statement_ = connection_.createStatement();

    String sql = "NONE";
    try {
      distinctTypename_ = collection_ + ".JDCS03DINT";
      sql = "CREATE TYPE " + distinctTypename_ + " AS INT";
      statement_.executeUpdate(sql);
    } catch (Exception e) {
      String expected = "already exists";
      String message = e.toString();
      if (message.indexOf(expected) < 0) {
        System.out.println("Warning:  expected to find exception with "
            + expected);
        e.printStackTrace(System.out);
      }
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    super.cleanup(); 
    statement_.close();
    connection_.close();
    connectionCommaSeparator_.close();
  }

  /**
   * get the procedure name to use for the datatype
   * 
   * @throws SQLException
   **/
  public String getProcedureName(int dataType) throws SQLException {
    switch (dataType) {
    case java.sql.Types.ARRAY:
      return collection_ + ".JDCS03ARRAY";
    case java.sql.Types.BIGINT:
      return collection_ + ".JDCS03BIGINT";
    case java.sql.Types.BINARY:
      return collection_ + ".JDCS03BINARY";
    case java.sql.Types.BIT:
      return collection_ + ".JDCS03BIT";
    case java.sql.Types.BLOB:
      return collection_ + ".JDCS03BLOB";
    case java.sql.Types.BOOLEAN:
      return collection_ + ".JDCS03BOOLEAN";
    case java.sql.Types.CHAR:
      return collection_ + ".JDCS03CHAR";
    case java.sql.Types.CLOB:
      return collection_ + ".JDCS03CLOB";
    case java.sql.Types.DATALINK:
      return collection_ + ".JDCS03DATALINK";
    case java.sql.Types.DATE:
      return collection_ + ".JDCS03DATE";
    case TYPES_DECFLOAT16:
      return collection_ + ".JDCS03DCF16";
    case TYPES_DECFLOAT34:
      return collection_ + ".JDCS03DCF34";

    case java.sql.Types.DECIMAL:
      return collection_ + ".JDCS03DECIMAL";
    case java.sql.Types.DISTINCT:
      return collection_ + ".JDCS03DISTINCT";
    case java.sql.Types.DOUBLE:
      return collection_ + ".JDCS03DOUBLE";
    case java.sql.Types.FLOAT:
      return collection_ + ".JDCS03FLOAT";
    case java.sql.Types.INTEGER:
      return collection_ + ".JDCS03INTEGER";
    case -16: /* java.sql.Types.LONGNVARCHAR */
      return collection_ + ".JDCS03LONGNVARCHAR";
    case java.sql.Types.LONGVARBINARY:
      return collection_ + ".JDCS03LONGVARBINARY";
    case java.sql.Types.LONGVARCHAR:
      return collection_ + ".JDCS03LONGVARCHAR";
    case -15: /* java.sql.Types.NCHAR: */
      return collection_ + ".JDCS03NCHAR";
    case 2011: /* java.sql.Types.NCLOB: */
      return collection_ + ".JDCS03NCLOB";
    case java.sql.Types.NULL:
      return collection_ + ".JDCS03NULL";
    case java.sql.Types.NUMERIC:
      return collection_ + ".JDCS03NUMERIC";
    case -9: /* java.sql.Types.NVARCHAR: */
      return collection_ + ".JDCS03NVARCHAR";
    case java.sql.Types.OTHER:
      return collection_ + ".JDCS03OTHER";
    case java.sql.Types.REAL:
      return collection_ + ".JDCS03REAL";
    case java.sql.Types.REF:
      return collection_ + ".JDCS03REF";
    case -8: /* java.sql.Types.ROWID: */
      return collection_ + ".JDCS03ROWID";
    case java.sql.Types.SMALLINT:
      return collection_ + ".JDCS03SMALLINT";
    case 2009: /* java.sql.Types.SQLXML: */
      return collection_ + ".JDCS03SQLXML";
    case java.sql.Types.STRUCT:
      return collection_ + ".JDCS03STRUCT";
    case java.sql.Types.TIME:
      return collection_ + ".JDCS03TIME";
    case java.sql.Types.TIMESTAMP:
      return collection_ + ".JDCS03TIMESTAMP";
    case java.sql.Types.TINYINT:
      return collection_ + ".JDCS03TINYINT";
    case java.sql.Types.VARBINARY:
      return collection_ + ".JDCS03VARBINARY";
    case java.sql.Types.VARCHAR:
      return collection_ + ".JDCS03VARCHAR";
    case TYPES_DBCLOB:
      return collection_ + ".JDCS03DBCLOB";

    default:
      throw new SQLException("Type " + dataType + " not valid");
    }
  }

  /**
   * get the procedure name to use for the datatype
   * 
   * @throws SQLException
   **/
  public String getProcedureCreateSql(String procedureName, int dataType)
      throws SQLException {
    switch (dataType) {
    case java.sql.Types.ARRAY:
      return "NA YET";
    case java.sql.Types.BIGINT:
      return "Create procedure "
          + procedureName
          + "(IN INPARM BIGINT, OUT OUTPARM BIGINT) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.BINARY:
      return "Create procedure "
          + procedureName
          + "(IN INPARM BINARY(30), OUT OUTPARM BINARY(30)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.BIT:
      return "NA";
    case java.sql.Types.BLOB:
      return "Create procedure "
          + procedureName
          + "(IN INPARM BLOB(30K), OUT OUTPARM BLOB(30K)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.BOOLEAN:
      return "Create procedure "
      + procedureName
      + "(IN INPARM BOOLEAN, OUT OUTPARM BOOLEAN) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
      
    case java.sql.Types.CHAR:
      return "Create procedure "
          + procedureName
          + "(IN INPARM CHAR(30), OUT OUTPARM CHAR(30)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.CLOB:
      return "Create procedure "
          + procedureName
          + "(IN INPARM CLOB(30K), OUT OUTPARM CLOB(30K)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.DATALINK:
      return "Create procedure "
          + procedureName
          + "(IN INPARM DATALINK, OUT OUTPARM DATALINK) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.DATE:
      return "Create procedure "
          + procedureName
          + "(IN INPARM DATE, OUT OUTPARM DATE) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case TYPES_DBCLOB:
      return "Create procedure "
          + procedureName
          + "(IN INPARM DBCLOB(30K) CCSID 1200, OUT OUTPARM DBCLOB(30K) CCSID 1200) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case TYPES_DECFLOAT16:
      return "Create procedure "
          + procedureName
          + "(IN INPARM DECFLOAT(16), OUT OUTPARM DECFLOAT(16)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case TYPES_DECFLOAT34:
      return "Create procedure "
          + procedureName
          + "(IN INPARM DECFLOAT(34), OUT OUTPARM DECFLOAT(34)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.DECIMAL:
      return "Create procedure "
          + procedureName
          + "(IN INPARM DECIMAL(10,2), OUT OUTPARM DECIMAL(10,2)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.DISTINCT:
      return "Create procedure " + procedureName + "(IN INPARM "
          + distinctTypename_ + ", OUT OUTPARM " + distinctTypename_
          + ") LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.DOUBLE:
      return "Create procedure "
          + procedureName
          + "(IN INPARM DOUBLE, OUT OUTPARM DOUBLE) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.FLOAT:
      return "Create procedure "
          + procedureName
          + "(IN INPARM FLOAT, OUT OUTPARM FLOAT) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.INTEGER:
      return "Create procedure "
          + procedureName
          + "(IN INPARM INT, OUT OUTPARM INT) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case -16: /* java.sql.Types.LONGNVARCHAR */
      return "Create procedure "
          + procedureName
          + "(IN INPARM LONG NVARCHAR, OUT OUTPARM LONG NVARCHAR) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.LONGVARBINARY:
      return "Create procedure "
          + procedureName
          + "(IN INPARM LONG VARBINARY, OUT OUTPARM LONG VARBINARY) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.LONGVARCHAR:
      return "Create procedure "
          + procedureName
          + "(IN INPARM LONG VARCHAR, OUT OUTPARM LONG VARCHAR) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case -15: /* java.sql.Types.NCHAR: */
      return "Create procedure "
          + procedureName
          + "(IN INPARM NCHAR(30), OUT OUTPARM NCHAR(30)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case 2011: /* java.sql.Types.NCLOB: */
      return "Create procedure "
          + procedureName
          + "(IN INPARM NCLOB(30K), OUT OUTPARM NCLOB(30K)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.NUMERIC:
      return "Create procedure "
          + procedureName
          + "(IN INPARM NUMERIC(10,2), OUT OUTPARM NUMERIC(10,2)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case -9: /* java.sql.Types.NVARCHAR: */
      return "Create procedure "
          + procedureName
          + "(IN INPARM NVARCHAR(30), OUT OUTPARM NVARCHAR(30)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.OTHER:
      return "NA OTHER";
    case java.sql.Types.REAL:
      return "Create procedure "
          + procedureName
          + "(IN INPARM REAL, OUT OUTPARM REAL) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.REF:
      return "NA REF";

    case -8: /* java.sql.Types.ROWID: */
      return "Create procedure "
          + procedureName
          + "(IN INPARM ROWID, OUT OUTPARM ROWID) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.SMALLINT:
      return "Create procedure "
          + procedureName
          + "(IN INPARM SMALLINT, OUT OUTPARM SMALLINT) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case 2009: /* java.sql.Types.SQLXML: */
      return "Create procedure "
          + procedureName
          + "(IN INPARM SQLXML, OUT OUTPARM SQLXML) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.STRUCT:
      return "NA Struct";
    case java.sql.Types.TIME:
      return "Create procedure "
          + procedureName
          + "(IN INPARM TIME, OUT OUTPARM TIME) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.TIMESTAMP:
      return "Create procedure "
          + procedureName
          + "(IN INPARM TIMESTAMP, OUT OUTPARM TIMESTAMP) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.TINYINT:
      return collection_ + ".JDCS03TINYINT";
    case java.sql.Types.VARBINARY:
      return "Create procedure "
          + procedureName
          + "(IN INPARM VARBINARY(30), OUT OUTPARM VARBINARY(30)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    case java.sql.Types.VARCHAR:
      return "Create procedure "
          + procedureName
          + "(IN INPARM VARCHAR(30), OUT OUTPARM VARCHAR(30)) LANGUAGE SQL BEGIN SET OUTPARM=INPARM; END";
    default:
      throw new SQLException("Type " + dataType + " not valid");
    }
  }

  /**
   * make sure the procedure is available and return the name of the procedure
   * 
   * @throws Exception
   */

  public String assureProcedureIsAvailable(int dataType) throws Exception {
    String procedureName = getProcedureName(dataType);
    Object defined = procedureDefinedHashtable.get(procedureName);
    if (defined == null) {
      Statement s = connection_.createStatement();
      try {
        s.executeUpdate("DROP PROCEDURE " + procedureName);
      } catch (Exception e) {
        String message = e.toString();
        String expected = "not found";
        if (message.indexOf(expected) < 0) {
          System.out.println("Warning did not find exception " + expected);
          e.printStackTrace(System.out);
        }
      }
      String sql = getProcedureCreateSql(procedureName, dataType);
      try {
        s.executeUpdate(sql);
      } catch (Exception e) {
        System.out.println("Exception " + e + " processing '" + sql
            + "' RETHROWING");
        throw e;
      }
      procedureDefinedHashtable.put(procedureName, procedureName);
    }

    return procedureName;
  }

  /**
   * prepare a call for the specified procedure. The first parameter is an input
   * parameter. The second parameter is an output parameter. Both parameters are
   * of the specified type. If needed create the sample procedure.
   * 
   * @throws Exception
   * 
   **/
  public CallableStatement prepareCall(int dataType) throws Exception {
    String procedureName = assureProcedureIsAvailable(dataType);
    return connection_.prepareCall("CALL " + procedureName + "(?,?)");
  }

}
