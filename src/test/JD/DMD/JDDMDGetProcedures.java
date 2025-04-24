///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetProcedures.java
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
 // File Name:    JDDMDGetProcedures.java
 //
 // Classes:      JDDMDGetProcedures
 //
 ////////////////////////////////////////////////////////////////////////
 ////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDDMDGetProcedures.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getProcedures()
</ul>
**/
public class JDDMDGetProcedures
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetProcedures";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }



    // Private data.
    private Connection          connection_;
    private String              connectionCatalog_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;
    StringBuffer message= new StringBuffer();

/**
Constructor.
**/
    public JDDMDGetProcedures (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetProcedures",
            namesAndVars, runMode, fileOutputStream,
            password);
    }


    protected String getCatalogFromURL(String url) {
      // System.out.println("BaseURL is "+baseURL_);
      // must be running JCC, set to a valid value.
      int lastColon;
      if (JDTestDriver.isLUW()) {
	  // For LUW format is jdbc:db2://erebor:50000/CLIDB
	  lastColon = url.lastIndexOf("/");
	  String part1 = url.substring(lastColon+1);
	  int semiIndex = part1.indexOf(";");
	  if (semiIndex >= 0) {
	      return part1.substring(0,semiIndex);
	  } else {
	      return part1;
	  }


      } else {
          // jdbc:db2://y0551p2:446/*LOCAL
	  lastColon = url.indexOf(":446");
	  if (lastColon > 0) {
	      String part1 = url.substring(0,lastColon);
	      int lastSlash = part1.lastIndexOf('/',lastColon);
	      if (lastSlash > 0) {
		  return part1.substring(lastSlash+1).toUpperCase();

	      }
	  }
      }
      return null;

    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        connectionCatalog_ = connection_.getCatalog();
        if (connectionCatalog_ == null) {
	    if (JDTestDriver.isLUW()) {
	    // Leave connectionCatalog_ as null for LUW
		connectionCatalog_=null;
	    } else {
		connectionCatalog_ = getCatalogFromURL(baseURL_);
		System.out.println("Warning:  connection.getCatalog() returned null setting to "+connectionCatalog_);
	    }
        }
        dmd_ = connection_.getMetaData ();
        String QIWS = JDSetupProcedure.setupQIWS(systemObject_, connection_);
        Statement s = connection_.createStatement ();
        s.executeUpdate ("CREATE PROCEDURE " + JDDMDTest.COLLECTION
            + ".PROCS LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
            + " SET DUMMY = 5; END P1");
        if (JDTestDriver.isLUW() ) {
          // LUW doesn't like the SET RESULTS SETS.. Just leave the cursor open
          s.executeUpdate ("CREATE PROCEDURE " + JDDMDTest.COLLECTION
              + ".PROCS1 RESULT SET 1 LANGUAGE SQL READS SQL DATA P1: BEGIN "
              + " DECLARE C1 CURSOR FOR SELECT * FROM "+QIWS+".QCUSTCDT; "
              + " OPEN C1; END P1");
        } else {
          s.executeUpdate ("CREATE PROCEDURE " + JDDMDTest.COLLECTION
            + ".PROCS1 RESULT SET 1 LANGUAGE SQL READS SQL DATA P1: BEGIN "
            + " DECLARE C1 CURSOR FOR SELECT * FROM "+QIWS+".QCUSTCDT; "
            + " OPEN C1; SET RESULT SETS CURSOR C1; END P1");
        }
        s.executeUpdate ("CREATE PROCEDURE " + JDDMDTest.COLLECTION
            + ".PROCS2 LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
            + " SET DUMMY = 5; END P1");
        s.executeUpdate ("CREATE PROCEDURE " + JDDMDTest.COLLECTION
            + ".PROCSXX LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
            + " SET DUMMY = 5; END P1");
        s.executeUpdate ("CREATE PROCEDURE " + JDDMDTest.COLLECTION2
            + ".PROCS LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
            + " SET DUMMY = 5; END P1");
        if (JDTestDriver.isLUW() ) {
          // LUW doesn't like the SET RESULTS SETS.. Just leave the cursor open
          s.executeUpdate ("CREATE PROCEDURE " + JDDMDTest.COLLECTION2
              + ".PROCS3 RESULT SET 1 LANGUAGE SQL READS SQL DATA P1: BEGIN "
              + " DECLARE C1 CURSOR FOR SELECT * FROM "+QIWS+".QCUSTCDT; "
              + " OPEN C1;  END P1");
        } else {
        s.executeUpdate ("CREATE PROCEDURE " + JDDMDTest.COLLECTION2
            + ".PROCS3 RESULT SET 1 LANGUAGE SQL READS SQL DATA P1: BEGIN "
            + " DECLARE C1 CURSOR FOR SELECT * FROM "+QIWS+".QCUSTCDT; "
            + " OPEN C1; SET RESULT SETS CURSOR C1; END P1");
        }
        s.executeUpdate ("CREATE PROCEDURE " + JDDMDTest.COLLECTION2
            + ".PROCS4 LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
            + " SET DUMMY = 5; END P1");
        s.executeUpdate ("CREATE PROCEDURE " + JDDMDTest.COLLECTIONXX
            + ".PROCSXX LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
            + " SET DUMMY = 5; END P1");

        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        dmd2_ = closedConnection_.getMetaData ();
        closedConnection_.close ();



    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        Statement s = connection_.createStatement ();

        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION
            + ".PROCS");
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION
            + ".PROCS1");
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION
            + ".PROCS2");
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION
            + ".PROCSXX");
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION2
            + ".PROCS");
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION2
            + ".PROCS3");
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION2
            + ".PROCS4");
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTIONXX
            + ".PROCSXX");

        s.close ();
        connection_.close ();
    }



  /**
   * getProcedures() - Check the result set format.
   **/
  public void Var001() {
    try {
      message.setLength(0);
      ResultSet rs = dmd_.getProcedures(null, null, null);
      ResultSetMetaData rsmd = rs.getMetaData();
      String usingExpectedTypes = "expectedTypes";

      String[] jccExpectedNames = { "PROCEDURE_CAT", "PROCEDURE_SCHEM",
          "PROCEDURE_NAME", "NUM_INPUT_PARAMS", "NUM_OUTPUT_PARAMS",
          "NUM_RESULT_SETS", "REMARKS", "PROCEDURE_TYPE" };
      
      int[] jccExpectedTypes = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
          Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.LONGVARCHAR,
          Types.SMALLINT };

      int[] luwExpectedTypes = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
          Types.INTEGER, Types.INTEGER, Types.SMALLINT, Types.VARCHAR,
          Types.SMALLINT };

      String[] jdk16ExpectedNames = { "PROCEDURE_CAT", "PROCEDURE_SCHEM",
          "PROCEDURE_NAME", "NUM_INPUT_PARAMS", "NUM_OUTPUT_PARAMS",
          "NUM_RESULT_SETS", "REMARKS", "PROCEDURE_TYPE", "SPECIFIC_NAME" };

      int[] jdk16ExpectedTypes = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
          Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.VARCHAR,
          Types.SMALLINT, Types.VARCHAR };

      // Toolbox is still not sysibm stored proc metadata. follow spec for now.
      String[] jdk16ExpectedNamesTB = { "PROCEDURE_CAT", "PROCEDURE_SCHEM",
          "PROCEDURE_NAME", "RESERVED1", "RESERVED2", "RESERVED3", "REMARKS",
          "PROCEDURE_TYPE", "SPECIFIC_NAME" };

      // Toolbox is still not sysibm stored proc metadata. follow spec for now.
      int[] jdk16ExpectedTypesTB = { Types.VARCHAR, Types.VARCHAR,
          Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER,
          Types.VARCHAR, Types.SMALLINT, Types.VARCHAR };

      int[] jdk16ExpectedTypes55 = { Types.VARCHAR, Types.VARCHAR,
          Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.SMALLINT, -9,
          Types.SMALLINT, Types.VARCHAR };

	  int[] jdk14ExpectedTypes55 = { Types.VARCHAR, Types.VARCHAR,
	  Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.SMALLINT, Types.VARCHAR,
	  Types.SMALLINT, Types.VARCHAR };


      int[] jdk16ExpectedTypes71TB = { Types.VARCHAR, Types.VARCHAR,
          Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.SMALLINT, -9,
          Types.SMALLINT, Types.VARCHAR };

      int[] jdk14ExpectedTypes71TB = { Types.VARCHAR, Types.VARCHAR,
          Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.SMALLINT,
          Types.VARCHAR, Types.SMALLINT, Types.VARCHAR };

      String[] expectedNames = { "PROCEDURE_CAT", "PROCEDURE_SCHEM",
          "PROCEDURE_NAME", "RESERVED1", "RESERVED2", "RESERVED3", "REMARKS",
          "PROCEDURE_TYPE" };
      int[] expectedTypes = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
          Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
          Types.SMALLINT };

      if (JDTestDriver.isLUW()) {
        expectedNames = jccExpectedNames;
        expectedTypes = luwExpectedTypes;
        usingExpectedTypes = "luwExpectedTypes";
      } else if (getDriver() == JDTestDriver.DRIVER_JCC) {
        expectedNames = jccExpectedNames;
        expectedTypes = jccExpectedTypes;
        usingExpectedTypes = "jccExpectedtypes";
      } else if ((getJdbcLevel() >= 4)
          || ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V7R1M0))
          || (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
        expectedNames = jdk16ExpectedNames;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
          // toolbox is not sysibm stored proc metadata yet. follow spec for
          // now, keeping 3 reserved columns
          expectedTypes = jdk16ExpectedTypesTB;
          usingExpectedTypes = "jdk16ExpectedTypesTB";
          expectedNames = jdk16ExpectedNamesTB;
        } else if ((getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
          if (getRelease() >= JDTestDriver.RELEASE_V7R1M0
              && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            if (getJdbcLevel() >= 4) {
              expectedTypes = jdk16ExpectedTypes71TB;
              usingExpectedTypes = "jdk16ExpectedTypes71TB";
            } else {
              expectedTypes = jdk14ExpectedTypes71TB;
              usingExpectedTypes = "jdk14ExpectedTypes71TB";

            }
          } else {
            if (getJdbcLevel() >= 4) {

            expectedTypes = jdk16ExpectedTypes55;
            usingExpectedTypes = "jdk16ExpectedTypes55 for release="
                + getRelease() + " dirver=" + getDriver();
	    } else {
            expectedTypes = jdk14ExpectedTypes55;
            usingExpectedTypes = "jdk14ExpectedTypes55 for release="
                + getRelease() + " dirver=" + getDriver();

	    }
          }
        } else {
          expectedTypes = jdk16ExpectedTypes;
          usingExpectedTypes = "jdk16ExpectedTypes";
        }

      }

            int count = rsmd.getColumnCount ();
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames,message);
            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);

            rs.close ();
            assertCondition ((count == expectedNames.length) && (namesCheck) && (typesCheck),
                "count is "+count+"sb "+expectedNames.length+"\n"+
                "namesCheck = "+namesCheck+"\n"+
                "typesCheck = "+typesCheck+"\n"+
                "expectedTypes="+usingExpectedTypes+"\n"+
                message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Get a list of those created in this testcase and
verify all columns with system remarks (the default).
**/
    public void Var002() {
      message.setLength(0);
    try {
      ResultSet rs = dmd_.getProcedures(null, JDDMDTest.COLLECTION, "PROCS%");
      boolean success = true;

      int rows = 0;
      while (rs.next()) {
        ++rows;
        success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "PROCS%", message, success) ;
        success = JDDMDTest.check(rs.getString("PROCEDURE_SCHEM"), JDDMDTest.COLLECTION, "PROCEDURE_SCHEMA", "PROCS%", message, success);

        //below, toolbox is not yet on metadata from sysibm stored procs
        if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || getDriver() == JDTestDriver.DRIVER_JCC || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0)) || (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
          success = JDDMDTest.check(rs.getString("REMARKS"), null, "REMARKS", "PROCS%", message, success);
        } else {
          success = JDDMDTest.check(rs.getString("REMARKS"), "", "REMARKS", "PROCS%", message, success);
        }

        String procedureName = rs.getString("PROCEDURE_NAME");
        short procedureType = rs.getShort("PROCEDURE_TYPE");

        if (procedureName.equals("PROCS")) {
        	if ((getJdbcLevel() >= 4  && getDriver() != JDTestDriver.DRIVER_TOOLBOX) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0)) || (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
		success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
					"PROCEDURE_TYPE", procedureName, message,success);

	    } else {
		success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureNoResult,
					"PROCEDURE_TYPE", procedureName, message,success);
	    }
        } else if (procedureName.equals("PROCS1")) {
        	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
            success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                "PROCEDURE_TYPE", procedureName, message,success);
          } else  if (JDTestDriver.isLUW()) {
            success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureNoResult,
                "PROCEDURE_TYPE", procedureName, message,success);
          } else {
            success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureReturnsResult,
                "PROCEDURE_TYPE", procedureName, message,success);
          }
        } else if (procedureName.equals("PROCS2")) {
        	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
            success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                "PROCEDURE_TYPE", procedureName, message,success);
          } else {
            success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureNoResult,
                "PROCEDURE_TYPE", procedureName, message,success);
          }
        } else if (procedureName.equals("PROCSXX")) {
        	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
            success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                "PROCEDURE_TYPE", procedureName, message,success);
          } else {
            success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureNoResult,
                "PROCEDURE_TYPE", procedureName, message,success);
          }
        }
      }

      rs.close();
      if (rows != 4)
        message.append("rows = " + rows + " sb 4 ");
      assertCondition((rows == 4) && success, "\n"+message.toString() );
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }



/**
getProcedures() - Get a list of those created in this testcase and
verify all columns with sql remarks.
**/
    public void Var003()
    {
        message.setLength(0);
        try {
            Connection c;
            if (getDriver() == JDTestDriver.DRIVER_JCC ) {
              c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

            } else {
            c = testDriver_.getConnection (baseURL_
                + ";remarks=sql", userId_, encryptedPassword_);
            }

            // Label the procedures.  No label on PROCS, please.
            Statement s = connection_.createStatement ();
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCS1 IS '5'");
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCS2 IS 'Six'");
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCSXX IS 'The Native JDBC driver is very fast'");
            s.close ();

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getProcedures (null, JDDMDTest.COLLECTION,
                "PROCS%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_,
                    "PROCEDURE_CAT", "PROCS%", message, success) ;
                success = JDDMDTest.check(rs.getString("PROCEDURE_SCHEM"), JDDMDTest.COLLECTION,
                    "PROCEDURE_SCHEMA", "PROCS%", message, success);


                String procedureName = rs.getString ("PROCEDURE_NAME");
                String remarks = rs.getString ("REMARKS");
                short procedureType = rs.getShort ("PROCEDURE_TYPE");

                if (procedureName.equals ("PROCS")) {
                	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || (getDriver()==JDTestDriver.DRIVER_JCC) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success = JDDMDTest.check(remarks, null,
                        "REMARKS", "PROCS", message, success);
                  } else {
                    success = JDDMDTest.check(remarks, "",
                        "REMARKS", "PROCS", message, success);
                  }
                	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCS", message, success);
                  }
                }
                else if (procedureName.equals ("PROCS1")) {
                  success = JDDMDTest.check(remarks, "5",
                      "REMARKS", "PROCS1", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX))  || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else if (JDTestDriver.isLUW()) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureNoResult,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureReturnsResult,
                        "PROCEDURETYPE", "PROCS1", message, success);
                  }
                }
                else if (procedureName.equals ("PROCS2")) {
                  success = JDDMDTest.check(remarks, "Six",
                      "REMARKS", "PROCS2", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCS", message, success);
                  }
                }
                else if (procedureName.equals ("PROCSXX")) {
                  success = JDDMDTest.check(remarks, "The Native JDBC driver is very fast",
                      "REMARKS", "PROCS2", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCSXX", message, success);
                  }
                }
            }

            rs.close ();
            c.close ();
            assertCondition ((rows == 4) && success, "rows = "+rows+" sb 4 "+message.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify all null parameters.  Verify that all procedures
come back in the library list are returned.

SQL400 - The native driver can't handle the list of libraries on the connection method.
**/
    public void Var004()
    {
        try {
            Connection c;
		    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
		    {
                c = testDriver_.getConnection (baseURL_
                    + ";libraries=*LIBL,QIWS," + JDDMDTest.COLLECTION + ","
                    + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                    userId_, encryptedPassword_);
            }
            else if (getDriver() == JDTestDriver.DRIVER_JCC) {
              c = testDriver_.getConnection (baseURL_,
                  userId_, encryptedPassword_);

            } else  {
                c = testDriver_.getConnection (baseURL_
                    + ";libraries=" + JDDMDTest.COLLECTION,
                    userId_, encryptedPassword_);

            }

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getProcedures (null, null, null);

            // It is impossible to check that all procedures come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            while (rs.next ()) {
                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTIONXX + ".PROCSXX"))
                    check3 = true;
            }

            rs.close ();
            c.close ();
            assertCondition (check1 && check2 && check3);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify null for the catalog pattern.  All matching
procedures should be returned.
**/
    public void Var005()
    {
      message.setLength(0);
        try {
            ResultSet rs = dmd_.getProcedures (null,
                JDDMDTest.SCHEMAS_PERCENT, "PROCS%");

            // Check for some of the procedures.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "ALL", message, success) ;

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTIONXX + ".PROCSXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows == 8),
                "success="+success+
                " PROCS found="+check1+
                " PROCS3 found="+check2+
                " PROCSXX found="+check3+
                " rows="+rows+" sb 8 "+message.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify empty string for the catalog pattern.
No matching procedures should be returned.
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getProcedures ("",
                JDDMDTest.SCHEMAS_PERCENT, "PROCS%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();

        if ((getDriver()==JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())){ //toolbox not yet on sysibm storedproc metadata in jdbc40
        	assertCondition (rows == 0, "Empty string specified for catalog, so results should have NOT been returned rows="+rows);
        } else if ((getJdbcLevel() >= 4) || (getDriver()==JDTestDriver.DRIVER_JCC) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0)) || (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
		assertCondition (rows >0 , "Empty string specified for catalog, so results should have been returned rows="+rows+"  Running with JDBC 4.0 or JCC or Native >= V5R5 ");
	    } else {
		assertCondition (rows == 0, "Empty string specified for catalog, so results should have NOT been returned rows="+rows);
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a catalog pattern that matches the catalog
exactly.  All matching procedures should be returned.
**/
    public void Var007()
    {
      message.setLength(0);

        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_ ,
                JDDMDTest.SCHEMAS_PERCENT, "PROCS%");

            // Check for some of the procedures.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "%", message, success) ;

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTIONXX + ".PROCSXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows == 8),
                "success="+success+
                " PROCS found="+check1+
                " PROCS3 found="+check2+
                " PROCSXX found="+check3+
                " rows="+rows+" sb 8 "+message.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify "localhost" for the catalog pattern.
All matching procedures should be returned.
**/
    public void Var008()
    {
       if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
         notApplicable("localhost variation");
       } else {
        try {
            ResultSet rs = dmd_.getProcedures ("localhost",
                JDDMDTest.SCHEMAS_PERCENT, "PROCS%");

            // Check for some of the procedures.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                if (! rs.getString ("PROCEDURE_CAT").equals (connectionCatalog_ )) {
                  success = false;

                }

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTIONXX + ".PROCSXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 8));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
       }
    }



/**
getProcedures() - Specify a catalog pattern for which there is a
match.  No matching procedures should be returned, since we do not
support catalog pattern.
**/
    public void Var009()
    {
      if (JDTestDriver.isLUW()) {
        notApplicable("LUW does not have catalogs so catalog pattern test is senseless");
      } else {
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_.substring (0, 4) + "%",
                JDDMDTest.SCHEMAS_PERCENT, "PROCS%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
getProcedures() - Specify a catalog pattern for which there is no match.
No matching procedures should be returned.
**/
    public void Var010()
    {
        try {
            ResultSet rs = dmd_.getProcedures ("BOGUS%",
                JDDMDTest.SCHEMAS_PERCENT, "PROCS%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify null for the schema pattern.  All matching
procedures should be returned.

SQL400 - The native driver can't handle the list of libraries on the connection method.
**/
    public void Var011()
    {
      message.setLength(0);

        try {
            Connection c;
            if (JDTestDriver.isLUW()) {
              c = testDriver_.getConnection (baseURL_,
                  userId_, encryptedPassword_);
              Statement stmt = c.createStatement();
              stmt.executeUpdate("set schema "+JDDMDTest.COLLECTION);

            } else {
             if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
             {
                c = testDriver_.getConnection (baseURL_
                    + ";libraries=*LIBL," + JDDMDTest.COLLECTION + ","
                    + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                    userId_, encryptedPassword_);
             }
             else if (getDriver() == JDTestDriver.DRIVER_JCC) {
              c = testDriver_.getConnection (baseURL_
                  + "/" + JDDMDTest.COLLECTION,
                  userId_, encryptedPassword_);

             } else
             {
                c = testDriver_.getConnection (baseURL_
                    + ";libraries=" + JDDMDTest.COLLECTION,
                    userId_, encryptedPassword_);
             }
            }

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getProcedures (c.getCatalog (),
                null, "PROCS%");

            // It is impossible to check that all procedures come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "PROCS%", message, success) ;

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTIONXX + ".PROCSXX"))
                    check3 = true;
            }

            rs.close ();
            c.close ();
            assertCondition (success && check1 && check2 && check3 && (rows >= 8),
                    "success="+success+
                    " PROCS found="+check1+
                    " PROCS3 found="+check2+
                    " PROCSXX found="+check3+
                    " rows="+rows+" sb 8 "+message.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify empty string for the schema pattern.
No matching procedures should be returned.
**/
    public void Var012()
    {
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_ ,
                "", "PROCS%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a schema pattern that matches a schema
exactly.  All matching procedures should be returned.
**/
    public void Var013()
    {
      message.setLength(0);
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_ ,
                JDDMDTest.COLLECTION2, "PROCS%");

            // Check for some of the procedures.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "PROCS%", message, success) ;

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS4"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows == 3),
                "success="+success+
                " PROCS found="+check1+
                " PROCS3 found="+check2+
                " PROCS4 found="+check3+
                " rows="+rows+" sb 3 "+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a schema pattern using a percent for which
there is a match.  All matching procedures should be returned.
**/
    public void Var014()
    {
      message.setLength(0);
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_ ,
                JDDMDTest.SCHEMAS_PERCENT, "PROCS%");

            // Check for some of the procedures.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "PROCS%", message, success) ;

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTIONXX + ".PROCSXX"))
                    check3 = true;
            }

            rs.close ();

            assertCondition (success && check1 && check2 && check3 && (rows == 8),
                "success="+success+
                " PROCS found="+check1+
                " PROCS3 found="+check2+
                " PROCSXX found="+check3+
                " rows="+rows+" sb 8 "+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a schema pattern using an underscore for which
there is a match.  All matching procedures should be returned.
**/
    public void Var015()
    {
      message.setLength(0);
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                JDDMDTest.SCHEMAS_UNDERSCORE, "PROCS%");

            // Check for some of the procedures.
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "PROCS%", message, success) ;

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
		if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check2 && check3 && (rows == 3),
                "success="+success+
                " PROCS found="+check2+
                " PROCS3 found="+check3+
                " rows="+rows+" sb 3 "+message);

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a schema pattern for which there is no match.
No matching procedures should be returned.
**/
    public void Var016()
    {
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                "BOGUS%", "PROCS%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0, "Expected 0 rows but got "+rows);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a schema pattern which contains an escape
character followed by a valid search character.
**/
    public void Var017()
    {
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                "PROC\\%S%", "PROCS%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();

            assertCondition (rows == 0, "Expected 0 rows but got "+rows);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a schema pattern which contains an escape
character followed by an invalid search character.   An exception should
be thrown.

SQL400 - For some reason the Toolbox wants to throw an exception on this
but I don't see why it shouldn't work just like var017 above.
@pd looks like var017 is "PROC\\%S%" and var018 is "PROC\\S%"...different
**/
    public void Var018()  // @B1C
    {
      if ((getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) ||
          (getDriver () == JDTestDriver.DRIVER_JCC) ||
          (getJdbcLevel() >= 4 && getDriver () != JDTestDriver.DRIVER_TOOLBOX) ||
	  ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))) {
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                "PROC\\S%", "PROCS%");
            failed ("Did not throw exception but got "+rs);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      } else {
         try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                                               "PROC\\S%", "PROCS%");

            int rows = 0;
            while (rs.next ())
               ++rows;

            rs.close ();
            assertCondition (rows == 0, "expected 0 rows but got "+rows );
         } catch (Exception e) {
             if(getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())
                 assertExceptionIsInstanceOf (e, "java.sql.SQLException");
             else
                 failed (e, "Unexpected Exception");
         }
        }
    }



/**
getProcedures() - Specify null for the procedure pattern.  All matching
procedures should be returned.
**/
    public void Var019()
    {
      message.setLength(0);

        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, null);

            // It is impossible to check that all procedures come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "PROCS%", message, success) ;

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTIONXX + ".PROCSXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows >= 8),
                "success="+success+
                " PROCS found="+check1+
                " PROCS3 found="+check2+
                " PROCSXX found="+check3+
                " rows="+rows+" sb 8 "+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify empty string for the procedure pattern.
No matching procedures should be returned.
**/
    public void Var020()
    {
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a procedure pattern that matches a procedure
exactly.  All matching procedures should be returned.
**/
    public void Var021()
    {
      message.setLength(0);
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCS");

            // Check for the procedures.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "PROCS%", message, success) ;

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && (rows == 2),
                "success="+success+
                " PROCS found="+check1+
                " PROCS found="+check2+
                " rows="+rows+" sb 2 "+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a procedure pattern using a percent for which
there is a match.  All matching procedures should be returned.
**/
    public void Var022()
    {
      message.setLength(0);

        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCS%");

            // Check for some of the procedures.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "PROCS%", message, success) ;

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTIONXX + ".PROCSXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows == 8),
                "success="+success+
                " PROCS found="+check1+
                " PROCS3 found="+check2+
                " PROCSXX found="+check3+
                " rows="+rows+" sb 8 "+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a procedures pattern using an underscore for which
there is a match.  All matching procedures should be returned.
**/
    public void Var023()
    {
      message.setLength(0);

        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCS_");

            // Check for some of the procedures.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_, "PROCEDURE_CAT", "PROCS%", message, success) ;

                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS1"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS4"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows == 4),
                    "success="+success+
                    " PROCS1 found="+check1+
                    " PROCS3 found="+check2+
                    " PROCS4 found="+check3+
                    " rows="+rows+" sb 4 "+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Specify a procedure pattern for which there is no match.
No matching procedures should be returned.
**/
    public void Var024()
    {
        try {
            ResultSet rs = dmd_.getProcedures (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "BOGUS%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Should throw an exception when the connection
is closed.
**/
    public void Var025()
    {
        try {
            ResultSet resultSet = dmd2_.getProcedures (null, null, null);
            failed ("Didn't throw SQLException but got "+resultSet);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
getProcedures() - Check all the RSMD results when using JDBC 3.0.
**/

  public void Var026() {
      if (checkNotGroupTest()) { 
	  checkRSMD(false);
      }
  }
  public void Var027() {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	      notApplicable("Native Driver and > V5R3 test");
      } else {
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 24355 && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	      notApplicable("Native Driver and SI24355 testing currentLevel="+getDriverFixLevel());
	  } else {
	      checkRSMD(true);
	  }
      }
  }

    public void checkRSMD(boolean extendedMetadata)
    {

	Connection connection = connection_;
	DatabaseMetaData dmd = dmd_;
	int TESTSIZE=40;

	int j=0;
	int col=0;
	String added=" -- Added 06/05/06 by native JDBC driver";
	boolean passed=true;
  message.setLength(0);
  StringBuffer prime = new StringBuffer();
	String [][] methodTests = {

	    {"isAutoIncrement",		"1","false"},
	    {"isCaseSensitive",		"1","true"},
	    {"isSearchable",		"1","true"},
	    {"isCurrency",		"1","false"},
	    {"isNullable",		"1","1"},
	    {"isSigned",		"1","false"},
	    {"getColumnDisplaySize",	"1","128"},
	    {"getColumnLabel",		"1","PROCEDURE_CAT"},
	    {"getColumnName",		"1","PROCEDURE_CAT"},
	    {"getPrecision",		"1","128"},
	    {"getScale",		"1","0"},
	    {"getCatalogName",		"1","LOCALHOST"},
	    {"getColumnType",		"1","12"},
	    {"getColumnTypeName",	"1","VARCHAR"},
	    {"isReadOnly",		"1","true"},
	    {"isWritable",		"1","false"},
	    {"isDefinitelyWritable",	"1","false"},
	    {"getColumnClassName",	"1","java.lang.String"},

	    {"isAutoIncrement",		"2","false"},
	    {"isCaseSensitive",		"2","true"},
	    {"isSearchable",		"2","true"},
	    {"isCurrency",		"2","false"},
	    {"isNullable",		"2","1"},
	    {"isSigned",		"2","false"},
	    {"getColumnDisplaySize",	"2","128"},
	    {"getColumnLabel",		"2","PROCEDURE_SCHEM"},
	    {"getColumnName",		"2","PROCEDURE_SCHEM"},
	    {"getPrecision",		"2","128"},
	    {"getScale",		"2","0"},
	    {"getCatalogName",		"2","LOCALHOST"},
	    {"getColumnType",		"2","12"},
	    {"getColumnTypeName",	"2","VARCHAR"},
	    {"isReadOnly",		"2","true"},
	    {"isWritable",		"2","false"},
	    {"isDefinitelyWritable",	"2","false"},
	    {"getColumnClassName",	"2","java.lang.String"},

	    {"isAutoIncrement",		"3","false"},
	    {"isCaseSensitive",		"3","true"},
	    {"isSearchable",		"3","true"},
	    {"isCurrency",		"3","false"},
	    {"isNullable",		"3","0"},
	    {"isSigned",		"3","false"},
	    {"getColumnDisplaySize",	"3","128"},
	    {"getColumnLabel",		"3","PROCEDURE_NAME"},
	    {"getColumnName",		"3","PROCEDURE_NAME"},
	    {"getPrecision",		"3","128"},
	    {"getScale",		"3","0"},
	    {"getCatalogName",		"3","LOCALHOST"},
	    {"getColumnType",		"3","12"},
	    {"getColumnTypeName",	"3","VARCHAR"},
	    {"isReadOnly",		"3","true"},
	    {"isWritable",		"3","false"},
	    {"isDefinitelyWritable",	"3","false"},
	    {"getColumnClassName",	"3","java.lang.String"},

	    {"isAutoIncrement",		"4","false"},
	    {"isCaseSensitive",		"4","false"},
	    {"isSearchable",		"4","true"},
	    {"isCurrency",		"4","false"},
	    {"isNullable",		"4","1"},
	    {"isSigned",		"4","false"},
	    {"getColumnDisplaySize",	"4","0"},
	    {"getColumnLabel",		"4","RESERVED1"},
	    {"getColumnName",		"4","RESERVED1"},
	    {"getPrecision",		"4","0"},
	    {"getScale",		"4","0"},
	    {"getCatalogName",		"4","LOCALHOST"},
	    {"getColumnType",		"4","4"},
	    {"getColumnTypeName",	"4","INTEGER"},
	    {"isReadOnly",		"4","true"},
	    {"isWritable",		"4","false"},
	    {"isDefinitelyWritable",	"4","false"},
	    {"getColumnClassName",	"4","java.lang.Integer"},

	    {"isAutoIncrement",		"5","false"},
	    {"isCaseSensitive",		"5","false"},
	    {"isSearchable",		"5","true"},
	    {"isCurrency",		"5","false"},
	    {"isNullable",		"5","1"},
	    {"isSigned",		"5","false"},
	    {"getColumnDisplaySize",	"5","0"},
	    {"getColumnLabel",		"5","RESERVED2"},
	    {"getColumnName",		"5","RESERVED2"},
	    {"getPrecision",		"5","0"},
	    {"getScale",		"5","0"},
	    {"getCatalogName",		"5","LOCALHOST"},
	    {"getColumnType",		"5","4"},
	    {"getColumnTypeName",	"5","INTEGER"},
	    {"isReadOnly",		"5","true"},
	    {"isWritable",		"5","false"},
	    {"isDefinitelyWritable",	"5","false"},
	    {"getColumnClassName",	"5","java.lang.Integer"},

	    {"isAutoIncrement",		"6","false"},
	    {"isCaseSensitive",		"6","false"},
	    {"isSearchable",		"6","true"},
	    {"isCurrency",		"6","false"},
	    {"isNullable",		"6","1"},
	    {"isSigned",		"6","false"},
	    {"getColumnDisplaySize",	"6","0"},
	    {"getColumnLabel",		"6","RESERVED3"},
	    {"getColumnName",		"6","RESERVED3"},
	    {"getPrecision",		"6","0"},
	    {"getScale",		"6","0"},
	    {"getCatalogName",		"6","LOCALHOST"},
	    {"getColumnType",		"6","4"},
	    {"getColumnTypeName",	"6","INTEGER"},
	    {"isReadOnly",		"6","true"},
	    {"isWritable",		"6","false"},
	    {"isDefinitelyWritable",	"6","false"},
	    {"getColumnClassName",	"6","java.lang.Integer"},

	    {"isAutoIncrement",		"7","false"},
	    {"isCaseSensitive",		"7","true"},
	    {"isSearchable",		"7","true"},
	    {"isCurrency",		"7","false"},
	    {"isNullable",		"7","1"},
	    {"isSigned",		"7","false"},
	    {"getColumnDisplaySize",	"7","256"},
	    {"getColumnLabel",		"7","REMARKS"},
	    {"getColumnName",		"7","REMARKS"},
	    {"getPrecision",		"7","256"},
	    {"getScale",		"7","0"},
	    {"getCatalogName",		"7","LOCALHOST"},
	    {"getColumnType",		"7","12"},
	    {"getColumnTypeName",	"7","VARCHAR"},
	    {"isReadOnly",		"7","true"},
	    {"isWritable",		"7","false"},
	    {"isDefinitelyWritable",	"7","false"},
	    {"getColumnClassName",	"7","java.lang.String"},


	    {"isAutoIncrement",		"8","false"},
	    {"isCaseSensitive",		"8","false"},
	    {"isSearchable",		"8","true"},
	    {"isCurrency",		"8","false"},
	    {"isNullable",		"8","0"},
	    {"isSigned",		"8","true"},
	    {"getColumnDisplaySize",	"8","6"},
	    {"getColumnLabel",		"8","PROCEDURE_TYPE"},
	    {"getColumnName",		"8","PROCEDURE_TYPE"},
	    {"getPrecision",		"8","5"},
	    {"getScale",		"8","0"},
	    {"getCatalogName",		"8","LOCALHOST"},
	    {"getColumnType",		"8","5"},
	    {"getColumnTypeName",	"8","SMALLINT"},
	    {"isReadOnly",		"8","true"},
	    {"isWritable",		"8","false"},
	    {"isDefinitelyWritable",	"8","false"},
	    {"getColumnClassName",	"8","java.lang.Short"},


	};


	String[][] toolboxDifferences = {
	    {"isSigned","4","true"},
	    {"getColumnDisplaySize","4","11"},
	    {"getPrecision","4","10"},
	    {"isSigned","5","true"},
	    {"getColumnDisplaySize","5","11"},
	    {"getPrecision","5","10"},
	    {"isSigned","6","true"},
	    {"getColumnDisplaySize","6","11"},
	    {"getPrecision","6","10"},
	    {"isNullable","7","0"},
	    {"getColumnDisplaySize","7","2000"},
	    {"getPrecision","7","2000"},
	    {"getColumnClassName","8","java.lang.Integer"},
	};

	String [][] nativeExtendedDifferences = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","true"},
	};
        String [][] nativeExtendedSysibmDifferences = {
            {"isSearchable","1","false"},
            {"isSearchable","2","false"},
            {"isSearchable","3","false"},
            {"isSearchable","4","false"},
            {"isSearchable","5","false"},
            {"isSearchable","6","false"},
            {"isSearchable","7","false"},
            {"isSearchable","8","false"},
        };

        String [][] nativeExtendedSysibmDifferences55 = {
            {"isSearchable","1","false"},
            {"isSearchable","2","false"},
            {"isSearchable","3","false"},
            {"isSearchable","4","false"},
            {"isSearchable","5","false"},
            {"isSearchable","6","false"},
            {"isSearchable","7","false"},
            {"isSearchable","8","false"},
        };


	String [][] luwDifferences = {
	    {"isNullable", "2", "0"},
	    {"isNullable","2","0"},
	    {"isNullable","4","0"},
	    {"isSigned","4","true"},
	    {"getColumnDisplaySize","4","11"},
	    {"getColumnLabel","4","NUM_INPUT_PARAMS"},
	    {"getColumnName","4","NUM_INPUT_PARAMS"},
	    {"getPrecision","4","10"},
	    {"isNullable","5","0"},
	    {"isSigned","5","true"},
	    {"getColumnDisplaySize","5","11"},
	    {"getColumnLabel","5","NUM_OUTPUT_PARAMS"},
	    {"getColumnName","5","NUM_OUTPUT_PARAMS"},
	    {"getPrecision","5","10"},
	    {"isNullable","6","0"},
	    {"isSigned","6","true"},
	    {"getColumnDisplaySize","6","6"},
	    {"getColumnLabel","6","NUM_RESULT_SETS"},
	    {"getColumnName","6","NUM_RESULT_SETS"},
	    {"getPrecision","6","5"},
	    {"getColumnType","6","5"},
	    {"getColumnTypeName","6","SMALLINT"},
	    {"getColumnDisplaySize","7","254"},
	    {"getPrecision","7","254"},
	    {"getColumnClassName","8","java.lang.Integer"},

	} ;


        String[][] sysibmDifferences = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","4","0" },
            {      "isSigned","4","true" },
            {      "getColumnDisplaySize","4","6" },
            {      "getColumnLabel","4","NUM_INPUT_PARAMS" },
            {      "getColumnName","4","NUM_INPUT_PARAMS" },
            {      "getPrecision","4","5" },
	    {      "getColumnType","4","5" },
	    {      "getColumnTypeName","4","SMALLINT" },


            {      "isNullable","5","0" },
            {      "isSigned","5","true" },
            {      "getColumnDisplaySize","5","6" },

            {      "getColumnLabel","5","NUM_OUTPUT_PARAMS" },
            {      "getColumnName","5","NUM_OUTPUT_PARAMS" },
            {      "getPrecision","5","5" },
	    {      "getColumnType","5","5"},
	    {      "getColumnTypeName","5","SMALLINT"},
            {      "isNullable","6","0" },
            {      "isSigned","6","true" },
            {      "getColumnDisplaySize","6","6" },
            {      "getColumnLabel","6","NUM_RESULT_SETS" },
            {      "getColumnName","6","NUM_RESULT_SETS" },
            {      "getPrecision","6","5" },
            {      "getColumnType","6","5" },
            {      "getColumnTypeName","6","SMALLINT" },
            {      "getColumnDisplaySize","7","2000" },
            {      "getPrecision","7","2000" },
            {      "getColumnClassName","8","java.lang.Integer" },
        };

        String[][] sysibmDifferences55 = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","4","0" },
            {      "isSigned","4","true" },
	    {      "getColumnDisplaySize","4","11"},
            {      "getColumnLabel","4","NUM_INPUT_PARAMS" },
            {      "getColumnName","4","NUM_INPUT_PARAMS" },
            {      "getPrecision","4","10" },
            {      "isNullable","5","0" },
            {      "isSigned","5","true" },
            {      "getColumnDisplaySize","5","11" },
            {      "getColumnLabel","5","NUM_OUTPUT_PARAMS" },
            {      "getColumnName","5","NUM_OUTPUT_PARAMS" },
            {      "getPrecision","5","10" },
            {      "isNullable","6","0" },
            {      "isSigned","6","true" },
            {      "getColumnDisplaySize","6","6" },
            {      "getColumnLabel","6","NUM_RESULT_SETS" },
            {      "getColumnName","6","NUM_RESULT_SETS" },
            {      "getPrecision","6","5" },
            {      "getColumnType","6","5" },
            {      "getColumnTypeName","6","SMALLINT" },
            {      "getColumnDisplaySize","7","2000" },
            {      "getPrecision","7","2000" },
            {      "getColumnClassName","8","java.lang.Integer" },



        };


        String[][] sysibmDifferences71 = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","4","0" },
            {      "isSigned","4","true" },
	    {      "getColumnDisplaySize","4","11"},
            {      "getColumnLabel","4","NUM_INPUT_PARAMS" },
            {      "getColumnName","4","NUM_INPUT_PARAMS" },
            {      "getPrecision","4","10" },
            {      "isNullable","5","0" },
            {      "isSigned","5","true" },
            {      "getColumnDisplaySize","5","11" },
            {      "getColumnLabel","5","NUM_OUTPUT_PARAMS" },
            {      "getColumnName","5","NUM_OUTPUT_PARAMS" },
            {      "getPrecision","5","10" },
            {      "isNullable","6","0" },
            {      "isSigned","6","true" },
            {      "getColumnDisplaySize","6","6" },
            {      "getColumnLabel","6","NUM_RESULT_SETS" },
            {      "getColumnName","6","NUM_RESULT_SETS" },
            {      "getPrecision","6","5" },
            {      "getColumnType","6","5" },
            {      "getColumnTypeName","6","SMALLINT" },
            {      "getColumnDisplaySize","7","2000" },
            {      "getPrecision","7","2000" },
	    {      "getColumnTypeName","7","NVARCHAR"},
            {      "getColumnClassName","8","java.lang.Integer" },



        };

       String[][] sysibmDifferences7140 = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","4","0" },
            {      "isSigned","4","true" },
	    {      "getColumnDisplaySize","4","11"},
            {      "getColumnLabel","4","NUM_INPUT_PARAMS" },
            {      "getColumnName","4","NUM_INPUT_PARAMS" },
            {      "getPrecision","4","10" },
            {      "isNullable","5","0" },
            {      "isSigned","5","true" },
            {      "getColumnDisplaySize","5","11" },
            {      "getColumnLabel","5","NUM_OUTPUT_PARAMS" },
            {      "getColumnName","5","NUM_OUTPUT_PARAMS" },
            {      "getPrecision","5","10" },
            {      "isNullable","6","0" },
            {      "isSigned","6","true" },
            {      "getColumnDisplaySize","6","6" },
            {      "getColumnLabel","6","NUM_RESULT_SETS" },
            {      "getColumnName","6","NUM_RESULT_SETS" },
            {      "getPrecision","6","5" },
            {      "getColumnType","6","5" },
            {      "getColumnTypeName","6","SMALLINT" },
            {      "getColumnDisplaySize","7","2000" },
            {      "getPrecision","7","2000" },
	    {      "getColumnTypeName","7","NVARCHAR"},
	    {      "getColumnType","7","-9"},
            {      "getColumnClassName","8","java.lang.Integer" },



        };


       String[][] sysibmDifferences7340 = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","4","0" },
            {      "isSigned","4","true" },
	    {      "getColumnDisplaySize","4","11"},
            {      "getColumnLabel","4","NUM_INPUT_PARAMS" },
            {      "getColumnName","4","NUM_INPUT_PARAMS" },
            {      "getPrecision","4","10" },
            {      "isNullable","5","0" },
            {      "isSigned","5","true" },
            {      "getColumnDisplaySize","5","11" },
            {      "getColumnLabel","5","NUM_OUTPUT_PARAMS" },
            {      "getColumnName","5","NUM_OUTPUT_PARAMS" },
            {      "getPrecision","5","10" },
            {      "isNullable","6","0" },
            {      "isSigned","6","true" },
            {      "getColumnDisplaySize","6","6" },
            {      "getColumnLabel","6","NUM_RESULT_SETS" },
            {      "getColumnName","6","NUM_RESULT_SETS" },
            {      "getPrecision","6","5" },
            {      "getColumnType","6","5" },
            {      "getColumnTypeName","6","SMALLINT" },
            {      "getColumnDisplaySize","7","2000" },
            {      "getPrecision","7","2000" },
	    {      "getColumnTypeName","7","NVARCHAR"},
	    {      "getColumnType","7","-9"},
            {      "getColumnClassName","8","java.lang.Integer" },

	    {"getColumnLabel","1","Procedure           Cat"},
	    {"getColumnLabel","2","Procedure           Schem"},
	    {"getColumnLabel","3","Procedure           Name"},
	    {"getColumnLabel","4","Num                 Input               Params"},
	    {"getColumnLabel","5","Num                 Output              Params"},
	    {"getColumnLabel","6","Num                 Result              Sets"},
	    {"getColumnLabel","7","Remarks"},
	    {"getColumnLabel","8","Procedure           Type"},

        };


        String[][] sysibmDifferences71TB = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","4","0" },
            {      "isSigned","4","true" },
	    {      "getColumnDisplaySize","4","11"},
            {      "getColumnLabel","4","NUM_INPUT_PARAMS" },
            {      "getColumnName","4","NUM_INPUT_PARAMS" },
            {      "getPrecision","4","10" },
            {      "isNullable","5","0" },
            {      "isSigned","5","true" },
            {      "getColumnDisplaySize","5","11" },
            {      "getColumnLabel","5","NUM_OUTPUT_PARAMS" },
            {      "getColumnName","5","NUM_OUTPUT_PARAMS" },
            {      "getPrecision","5","10" },
            {      "isNullable","6","0" },
            {      "isSigned","6","true" },
            {      "getColumnDisplaySize","6","6" },
            {      "getColumnLabel","6","NUM_RESULT_SETS" },
            {      "getColumnName","6","NUM_RESULT_SETS" },
            {      "getPrecision","6","5" },
            {      "getColumnType","6","5" },
            {      "getColumnTypeName","6","SMALLINT" },

            {      "getColumnDisplaySize","7","2000" },

	    {       "getPrecision","7","2000"},   /* Changed 8/21/2012 */ 
	    {       "getColumnTypeName","7","NVARCHAR"},

            {      "getColumnClassName","8","java.lang.Integer" },



        };


        String[][] sysibmDifferences7140TB = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","4","0" },
            {      "isSigned","4","true" },
	    {      "getColumnDisplaySize","4","11"},
            {      "getColumnLabel","4","NUM_INPUT_PARAMS" },
            {      "getColumnName","4","NUM_INPUT_PARAMS" },
            {      "getPrecision","4","10" },
            {      "isNullable","5","0" },
            {      "isSigned","5","true" },
            {      "getColumnDisplaySize","5","11" },
            {      "getColumnLabel","5","NUM_OUTPUT_PARAMS" },
            {      "getColumnName","5","NUM_OUTPUT_PARAMS" },
            {      "getPrecision","5","10" },
            {      "isNullable","6","0" },
            {      "isSigned","6","true" },
            {      "getColumnDisplaySize","6","6" },
            {      "getColumnLabel","6","NUM_RESULT_SETS" },
            {      "getColumnName","6","NUM_RESULT_SETS" },
            {      "getPrecision","6","5" },
            {      "getColumnType","6","5" },
            {      "getColumnTypeName","6","SMALLINT" },

            {      "getColumnDisplaySize","7","2000" },

	    {       "getPrecision","7","2000"},   /* Changed 8/21/2012 */ 
	    {"getColumnType","7","-9"},
	    {       "getColumnTypeName","7","NVARCHAR"},

            {      "getColumnClassName","8","java.lang.Integer" },



        };


        String[][] sysibmDifferences7340TBE = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","4","0" },
            {      "isSigned","4","true" },
	    {      "getColumnDisplaySize","4","11"},
            {      "getColumnName","4","NUM_INPUT_PARAMS" },
            {      "getPrecision","4","10" },
            {      "isNullable","5","0" },
            {      "isSigned","5","true" },
            {      "getColumnDisplaySize","5","11" },
            {      "getColumnName","5","NUM_OUTPUT_PARAMS" },
            {      "getPrecision","5","10" },
            {      "isNullable","6","0" },
            {      "isSigned","6","true" },
            {      "getColumnDisplaySize","6","6" },
            {      "getColumnName","6","NUM_RESULT_SETS" },
            {      "getPrecision","6","5" },
            {      "getColumnType","6","5" },
            {      "getColumnTypeName","6","SMALLINT" },

            {      "getColumnDisplaySize","7","2000" },

	    {       "getPrecision","7","2000"},   /* Changed 8/21/2012 */ 
	    {"getColumnType","7","-9"},
	    {       "getColumnTypeName","7","NVARCHAR"},

            {      "getColumnClassName","8","java.lang.Integer" },


	    {"getColumnLabel","1","Procedure Cat"},
	    {"getColumnLabel","2","Procedure Schem"},
	    {"getColumnLabel","3","Procedure Name"},
	    {"getColumnLabel","4","Num Input Params"},
	    {"getColumnLabel","5","Num Output Params"},
	    {"getColumnLabel","6","Num Result Sets"},
	    {"getColumnLabel","7","Remarks"},
	    {"getColumnLabel","8","Procedure Type"},

        };


        String[][] sysibmDifferences7340TB = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","4","0" },
            {      "isSigned","4","true" },
	    {      "getColumnDisplaySize","4","11"},
            {      "getColumnName","4","NUM_INPUT_PARAMS" },
            {      "getPrecision","4","10" },
            {      "isNullable","5","0" },
            {      "isSigned","5","true" },
            {      "getColumnDisplaySize","5","11" },
            {      "getColumnName","5","NUM_OUTPUT_PARAMS" },
            {      "getPrecision","5","10" },
            {      "isNullable","6","0" },
            {      "isSigned","6","true" },
            {      "getColumnDisplaySize","6","6" },
            {      "getColumnName","6","NUM_RESULT_SETS" },
            {      "getPrecision","6","5" },
            {      "getColumnType","6","5" },
            {      "getColumnTypeName","6","SMALLINT" },

            {      "getColumnDisplaySize","7","2000" },

	    {       "getPrecision","7","2000"},   /* Changed 8/21/2012 */ 
	    {"getColumnType","7","-9"},
	    {       "getColumnTypeName","7","NVARCHAR"},

            {      "getColumnClassName","8","java.lang.Integer" },

	    {    "getColumnLabel","4","NUM_INPUT_PARAMS" }, 
	    {    "getColumnLabel","5","NUM_OUTPUT_PARAMS" }, 
	    {    "getColumnLabel","6","NUM_RESULT_SETS" }, 

	}; 

        String[][] sysibmDifferences71L = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","4","0" },
            {      "isSigned","4","true" },
	    {      "getColumnDisplaySize","4","11"},
            {      "getColumnLabel","4","NUM_INPUT_PARAMS" },
            {      "getColumnName","4","NUM_INPUT_PARAMS" },
            {      "getPrecision","4","10" },
            {      "isNullable","5","0" },
            {      "isSigned","5","true" },
            {      "getColumnDisplaySize","5","11" },
            {      "getColumnLabel","5","NUM_OUTPUT_PARAMS" },
            {      "getColumnName","5","NUM_OUTPUT_PARAMS" },
            {      "getPrecision","5","10" },
            {      "isNullable","6","0" },
            {      "isSigned","6","true" },
            {      "getColumnDisplaySize","6","6" },
            {      "getColumnLabel","6","NUM_RESULT_SETS" },
            {      "getColumnName","6","NUM_RESULT_SETS" },
            {      "getPrecision","6","5" },
            {      "getColumnType","6","5" },
            {      "getColumnTypeName","6","SMALLINT" },
            {      "getColumnDisplaySize","7","2000" },
            {      "getPrecision","7","2000" },
	    {      "getColumnTypeName","7","VARGRAPHIC"}, 
            {      "getColumnClassName","8","java.lang.Integer" },

        };




        String[][] fixup = {};
        String[][] fixup2 = {};

        if (checkJdbc30()) {
	    try {

	        if (extendedMetadata) {
	            String url = baseURL_
	            + ";extended metadata=true";
	            if (getDriver() == JDTestDriver.DRIVER_JCC) {
	                url = baseURL_;
	            }
	            connection = testDriver_.getConnection (url,
	                    userId_, encryptedPassword_);
	            dmd= connection.getMetaData ();



	            if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	                if (isSysibmMetadata()) {
			    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	                        message.append("fixup = nativeExtendedSysibmDifferences\n");
	                        fixup = nativeExtendedSysibmDifferences55;

	                    } else {
	                        message.append("fixup = nativeExtendedSysibmDifferences\n");
	                        fixup = nativeExtendedSysibmDifferences;
	                    }
	                } else {
	                    message.append("fixup = nativeExtendedDifferences\n");
	                    fixup = nativeExtendedDifferences;
	                }
	            }

	        }

		if (JDTestDriver.isLUW()) {
		    fixup = luwDifferences;
		    message.append("fixup = luwDifferences\n");

		}
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
		    fixup = toolboxDifferences;
		    message.append("fixup = toolboxDifferences\n");
		}

		if (isSysibmMetadata()) {
		    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
			    if (getJdbcLevel() >= 4) {
				if (getRelease() >= JDTestDriver.RELEASE_V7R3M0) {
				    if (extendedMetadata) { 
					fixup2 = sysibmDifferences7340TBE;
					message.append("fixup2 = sysibmDifferences7340TBE\n");
				    } else {
					fixup2 = sysibmDifferences7340TB;
					message.append("fixup2 = sysibmDifferences7340TB\n");

				    } 

				} else { 
				    fixup2 = sysibmDifferences7140TB;
				    message.append("fixup2 = sysibmDifferences7140TB\n");
				}
			    } else {
				fixup2 = sysibmDifferences71TB;
				message.append("fixup2 = sysibmDifferences71TB\n");
			    }

			} else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
			    fixup2 = sysibmDifferences71L;
			    message.append("fixup2 = sysibmDifferences71L\n");
			} else {
			    if (getJdbcLevel() >= 4) {
				if (getRelease() >= JDTestDriver.RELEASE_V7R3M0) {
				    if (extendedMetadata) { 
					fixup2 = sysibmDifferences7340;
					message.append("fixup2 = sysibmDifferences7340\n");
				    } else {
					fixup2 = sysibmDifferences7140;
					message.append("fixup2 = sysibmDifferences7140\n");

				    }
				} else { 
				fixup2 = sysibmDifferences7140;
				message.append("fixup2 = sysibmDifferences7140\n");
				}
			    } else {
				fixup2 = sysibmDifferences71;
				message.append("fixup2 = sysibmDifferences71\n");
			    }
			}
		    } else  if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		        fixup2 = sysibmDifferences55;
		        message.append("fixup2 = sysibmDifferences55\n");

		    } else {
		        fixup2 = sysibmDifferences;
		        message.append("fixup2 = sysibmDifferences\n");
		    }

		}
		for (j = 0; j < fixup.length; j++) {
			boolean found = false;
			for (int k = 0; !found &&  k < methodTests.length; k++) {
			    if (fixup[j][0].equals(methodTests[k][0]) &&
                                fixup[j][1].equals(methodTests[k][1])) {
				methodTests[k] = fixup[j];
				found = true;
			    }
			}

		}
                for (j = 0; j < fixup2.length; j++) {
                        boolean found = false;
                        for (int k = 0; !found &&  k < methodTests.length; k++) {
                            if (fixup2[j][0].equals(methodTests[k][0]) &&
                          fixup2[j][1].equals(methodTests[k][1])) {
                                methodTests[k] = fixup2[j];
                                found = true;
                            }
                        }

                }

		String catalog = connection.getCatalog();
		if (catalog == null) {
		    catalog = getCatalogFromURL(baseURL_);
		}

		ResultSet[] rsA =  new ResultSet[TESTSIZE];
		ResultSetMetaData[] rsmdA = new ResultSetMetaData[TESTSIZE];
		for (j = 0; j < TESTSIZE; j++) {
		    rsA[j] = dmd.getProcedures (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCS_");
		    rsmdA[j] = rsA[j].getMetaData ();

		    ResultSetMetaData rsmd = rsmdA[j];


		    passed = verifyRsmd(rsmd, methodTests, catalog, j, message, prime) && passed;

		} /* for j */
		assertCondition(passed, message.toString()+added+"\nPRIME=\n"+prime);

	    } catch (Exception e) {
		failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
	    } catch (Error e ) {
		failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
	    }
	}
    }





/**
getProcedures() - Get a list of those created in this testcase and
verify all columns with system remarks (the default) using getInt().
Almost identical to Var003.
**/
    public void Var028()
    {
      message.setLength(0);
        try {
            Connection c;
            if (getDriver() == JDTestDriver.DRIVER_JCC ) {
              c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

            } else {
            c = testDriver_.getConnection (baseURL_
                + ";remarks=sql", userId_, encryptedPassword_);
            }

            // Label the procedures.  No label on PROCS, please.
            Statement s = connection_.createStatement ();
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCS1 IS '5'");
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCS2 IS 'Six'");
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCSXX IS 'The Native JDBC driver is very fast'");
            s.close ();

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getProcedures (null, JDDMDTest.COLLECTION,
                "PROCS%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_,
                    "PROCEDURE_CAT", "PROCS%", message, success) ;
                success = JDDMDTest.check(rs.getString("PROCEDURE_SCHEM"), JDDMDTest.COLLECTION,
                    "PROCEDURE_SCHEMA", "PROCS%", message, success);


                String procedureName = rs.getString ("PROCEDURE_NAME");
                String remarks = rs.getString ("REMARKS");
                short procedureType = (short) rs.getInt ("PROCEDURE_TYPE");

                if (procedureName.equals ("PROCS")) {
                	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || (getDriver()==JDTestDriver.DRIVER_JCC) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success = JDDMDTest.check(remarks, null,
                        "REMARKS", "PROCS", message, success);
                  } else {
                    success = JDDMDTest.check(remarks, "",
                        "REMARKS", "PROCS", message, success);
                  }
                	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCS", message, success);
                  }
                }
                else if (procedureName.equals ("PROCS1")) {
                  success = JDDMDTest.check(remarks, "5",
                      "REMARKS", "PROCS1", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX))  || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else if (JDTestDriver.isLUW()) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureNoResult,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureReturnsResult,
                        "PROCEDURETYPE", "PROCS1", message, success);
                  }
                }
                else if (procedureName.equals ("PROCS2")) {
                  success = JDDMDTest.check(remarks, "Six",
                      "REMARKS", "PROCS2", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCS", message, success);
                  }
                }
                else if (procedureName.equals ("PROCSXX")) {
                  success = JDDMDTest.check(remarks, "The Native JDBC driver is very fast",
                      "REMARKS", "PROCS2", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCSXX", message, success);
                  }
                }
            }

            rs.close ();
            c.close ();
            assertCondition ((rows == 4) && success, "rows = "+rows+" sb 4 "+message.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedures() - Get a list of those created in this testcase and
verify all columns with system remarks (the default) using getLong().
Almost identical to Var003.
**/


    public void Var029()
    {
      message.setLength(0);
        try {
            Connection c;
            if (getDriver() == JDTestDriver.DRIVER_JCC ) {
              c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

            } else {
            c = testDriver_.getConnection (baseURL_
                + ";remarks=sql", userId_, encryptedPassword_);
            }

            // Label the procedures.  No label on PROCS, please.
            Statement s = connection_.createStatement ();
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCS1 IS '5'");
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCS2 IS 'Six'");
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCSXX IS 'The Native JDBC driver is very fast'");
            s.close ();

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getProcedures (null, JDDMDTest.COLLECTION,
                "PROCS%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = JDDMDTest.check(rs.getString("PROCEDURE_CAT"), connectionCatalog_,
                    "PROCEDURE_CAT", "PROCS%", message, success) ;
                success = JDDMDTest.check(rs.getString("PROCEDURE_SCHEM"), JDDMDTest.COLLECTION,
                    "PROCEDURE_SCHEMA", "PROCS%", message, success);


                String procedureName = rs.getString ("PROCEDURE_NAME");
                String remarks = rs.getString ("REMARKS");
                short procedureType = (short) rs.getLong ("PROCEDURE_TYPE");

                if (procedureName.equals ("PROCS")) {
                	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || (getDriver()==JDTestDriver.DRIVER_JCC) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success = JDDMDTest.check(remarks, null,
                        "REMARKS", "PROCS", message, success);
                  } else {
                    success = JDDMDTest.check(remarks, "",
                        "REMARKS", "PROCS", message, success);
                  }
                	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCS", message, success);
                  }
                }
                else if (procedureName.equals ("PROCS1")) {
                  success = JDDMDTest.check(remarks, "5",
                      "REMARKS", "PROCS1", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX))  || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else if (JDTestDriver.isLUW()) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureNoResult,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureReturnsResult,
                        "PROCEDURETYPE", "PROCS1", message, success);
                  }
                }
                else if (procedureName.equals ("PROCS2")) {
                  success = JDDMDTest.check(remarks, "Six",
                      "REMARKS", "PROCS2", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCS", message, success);
                  }
                }
                else if (procedureName.equals ("PROCSXX")) {
                  success = JDDMDTest.check(remarks, "The Native JDBC driver is very fast",
                      "REMARKS", "PROCS2", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCSXX", message, success);
                  }
                }
            }

            rs.close ();
            c.close ();
            assertCondition ((rows == 4) && success, "rows = "+rows+" sb 4 "+message.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }





/**
getProcedures() - Get a list of those created in this testcase and
verify all columns with system remarks (the default) using getObject().
Almost identical to Var003.
**/

    public void Var030()
    {
      message.setLength(0);
        try {
            Connection c;
            if (getDriver() == JDTestDriver.DRIVER_JCC ) {
              c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

            } else {
            c = testDriver_.getConnection (baseURL_
                + ";remarks=sql", userId_, encryptedPassword_);
            }

            // Label the procedures.  No label on PROCS, please.
            Statement s = connection_.createStatement ();
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCS1 IS '5'");
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCS2 IS 'Six'");
            s.executeUpdate ("COMMENT ON PROCEDURE " + JDDMDTest.COLLECTION
                + ".PROCSXX IS 'The Native JDBC driver is very fast'");
            s.close ();

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getProcedures (null, JDDMDTest.COLLECTION,
                "PROCS%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = JDDMDTest.check((String) rs.getObject("PROCEDURE_CAT"), connectionCatalog_,
                    "PROCEDURE_CAT", "PROCS%", message, success) ;
                success = JDDMDTest.check((String) rs.getObject("PROCEDURE_SCHEM"), JDDMDTest.COLLECTION,
                    "PROCEDURE_SCHEMA", "PROCS%", message, success);


                String procedureName = (String) rs.getObject ("PROCEDURE_NAME");
                String remarks = (String) rs.getObject ("REMARKS");
                short procedureType = (short) ((Integer)rs.getObject ("PROCEDURE_TYPE")).intValue();

                if (procedureName.equals ("PROCS")) {
                	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || (getDriver()==JDTestDriver.DRIVER_JCC) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success = JDDMDTest.check(remarks, null,
                        "REMARKS", "PROCS", message, success);
                  } else {
                    success = JDDMDTest.check(remarks, "",
                        "REMARKS", "PROCS", message, success);
                  }
                	if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCS", message, success);
                  }
                }
                else if (procedureName.equals ("PROCS1")) {
                  success = JDDMDTest.check(remarks, "5",
                      "REMARKS", "PROCS1", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX))  || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else if (JDTestDriver.isLUW()) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureNoResult,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureReturnsResult,
                        "PROCEDURETYPE", "PROCS1", message, success);
                  }
                }
                else if (procedureName.equals ("PROCS2")) {
                  success = JDDMDTest.check(remarks, "Six",
                      "REMARKS", "PROCS2", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCS", message, success);
                  }
                }
                else if (procedureName.equals ("PROCSXX")) {
                  success = JDDMDTest.check(remarks, "The Native JDBC driver is very fast",
                      "REMARKS", "PROCS2", message, success);
                  if (((getJdbcLevel() >= 4) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&  (getRelease() >=  JDTestDriver.RELEASE_V7R1M0))|| (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                    success=JDDMDTest.check(procedureType,DatabaseMetaData.procedureResultUnknown,
                        "PROCEDURE_TYPE", procedureName, message,success);
                  } else {
                    success = JDDMDTest.check(procedureType, DatabaseMetaData.procedureNoResult,
                        "PROCEDURETYPE", "PROCSXX", message, success);
                  }
                }
            }

            rs.close ();
            c.close ();
            assertCondition ((rows == 4) && success, "rows = "+rows+" sb 4 "+message.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }




/**
getProcedures() - Run getProcedures multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var031()
    {
	String added = " -- added by 1/31/2011 to test for native statement leak in metadata";
        if (checkNative()) {
            try {

		System.gc();
		Statement fill[] = new Statement[20];
		for (int i = 0; i < fill.length; i++) {
		    fill[i] = connection_.createStatement();
		}

		Statement stmt = connection_.createStatement();

		for (int i = 0; i < 1000; i++) {
		    // System.out.println("Calling getProcedures");
		    ResultSet rs = dmd_.getProcedures (connectionCatalog_,
						       JDDMDTest.SCHEMAS_PERCENT, "PROCS_");
		    rs.close();
		}

		Statement stmt2 = connection_.createStatement();
		int beginningHandle = JDReflectionUtil.callMethod_I(stmt,"getStatementHandle");
		int endingHandle = JDReflectionUtil.callMethod_I(stmt2,"getStatementHandle");


		for (int i = 0; i < fill.length; i++) {
		    fill[i].close();
		}

                assertCondition((endingHandle - beginningHandle) < 10 ,
				" endingHandle = "+endingHandle+
				" beginningHandle = "+beginningHandle+
				" endingHandle = "+endingHandle+
				added);
            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


/**
getProcedures() - Use readonly connection 
**/
    public void Var032()
    {
        try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            ResultSet rs = dmd.getProcedures (null, null, null);

            // It is impossible to check that all procedures come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            while (rs.next ()) {
                String procedure = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME");
                if (procedure.equals (JDDMDTest.COLLECTION + ".PROCS"))
                    check1 = true;
                else if (procedure.equals (JDDMDTest.COLLECTION2 + ".PROCS3"))
                    check2 = true;
                else if (procedure.equals (JDDMDTest.COLLECTIONXX + ".PROCSXX"))
                    check3 = true;
            }

            rs.close ();
            c.close ();
            assertCondition (check1 && check2 && check3);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }






}



