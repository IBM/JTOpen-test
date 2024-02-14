///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetProcedureColumns.java
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
 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDDMDGetProcedureColumns.java
 //
 // Classes:      JDDMDGetProcedureColumns
 //
 ////////////////////////////////////////////////////////////////////////
 //
 // LUW differences
 // ---------------
 // 1.  Empty string for column pattern returns answers Var023
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;


/**
Testcase JDDMDGetProcedureColumns.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getProcedureColumns()
</ul>
**/
public class JDDMDGetProcedureColumns
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private String              connectionCatalog_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;

    private int expectedRowsAll = 0;
    private int expectedRowsBase = 0;
    private String errorMessage="";
    private String parameterName="";
    StringBuffer message= new StringBuffer();

/**
Constructor.
**/
    public JDDMDGetProcedureColumns (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetProcedureColumns",
            namesAndVars, runMode, fileOutputStream,
            password);
    }



    public boolean checkString(String name, String value, String expected) {
        if (value == null) {
            if (expected == null) {
                return true;
            } else {
                errorMessage+= "\n"+parameterName+":"+name+" = "+value+" sb "+expected;
                return false;
            }
        } else {
            if (value.equals(expected)) {
                return true;
            } else {
                errorMessage+= "\n"+ parameterName+":"+name+" = "+value+" sb "+expected;
                return false;
            }
        }
    }

  public boolean checkInt(String name, int value, int expected) {
          if (value == expected) {
              return true;
          } else {
              errorMessage+= "\n"+parameterName+":"+name+" = "+value+" sb "+expected;
              return false;
          }
  }


  protected String getCatalogFromURL(String url) {
    // System.out.println("BaseURL is "+baseURL_);
    // must be running JCC, set to a valid value.
    // jdbc:db2://y0551p2:446/*LOCAL

    int lastColon = url.indexOf(":446");
    if (lastColon > 0) {
      String part1 = url.substring(0,lastColon);
      int lastSlash = part1.lastIndexOf('/',lastColon);
      if (lastSlash > 0) {
        return part1.substring(lastSlash+1).toUpperCase();

      }
    }
    return null;

  }

/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup() throws Exception {
    String sql = "";
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

    } else {
	String connectUrl = baseURL_   + ";errors=full;libraries=" + JDDMDTest.COLLECTION + ","
						    + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX;

      System.out.println("connecting to URL"); 
      connection_ = testDriver_.getConnection(connectUrl, userId_, encryptedPassword_);
    }

    connectionCatalog_ = connection_.getCatalog();
    if (connectionCatalog_ == null) {
      connectionCatalog_ = getCatalogFromURL(baseURL_);
      System.out
          .println("Warning:  connection.getCatalog() returned null setting to "
              + connectionCatalog_);
    }

    dmd_ = connection_.getMetaData();

    cleanup(true);
    Statement s = connection_.createStatement();
    try {
      sql = "CREATE TABLE " + JDDMDTest.COLLECTION
          + ".PROCCOLT (COL1 INT, COL2 DATE NOT NULL)";
      s.executeUpdate(sql);
      if (JDTestDriver.isLUW()) {
        sql = "CREATE PROCEDURE " + JDDMDTest.COLLECTION
        + ".PROCCOLS1() RESULT SET 1 LANGUAGE SQL READS SQL DATA P1: BEGIN "
        + " DECLARE C1 CURSOR FOR SELECT * FROM " + JDDMDTest.COLLECTION
        + ".PROCCOLT; " + " OPEN C1; END P1";

      } else {
         sql = "CREATE PROCEDURE " + JDDMDTest.COLLECTION
          + ".PROCCOLS1() RESULT SET 1 LANGUAGE SQL READS SQL DATA P1: BEGIN "
          + " DECLARE C1 CURSOR FOR SELECT * FROM " + JDDMDTest.COLLECTION
          + ".PROCCOLT; " + " OPEN C1; SET RESULT SETS CURSOR C1; END P1";
      }
      s.executeUpdate(sql);
      sql = "CREATE PROCEDURE " + JDDMDTest.COLLECTION
          + ".PROCCOLS2 LANGUAGE SQL P1: BEGIN DECLARE DUMMY INTEGER;"
          + " SET DUMMY = 5; END P1";
      s.executeUpdate(sql);
      sql = "CREATE PROCEDURE " + JDDMDTest.COLLECTION2
          + ".PROCCOLS LANGUAGE SQL P1: BEGIN DECLARE DUMMY INTEGER;"
          + " SET DUMMY = 5; END P1";
      s.executeUpdate(sql);
      sql = "CREATE PROCEDURE " + JDDMDTest.COLLECTION
          + ".PROCCOLS (IN PARM INT, OUT PARM1 TIMESTAMP, "
          + " INOUT PARM2 CHAR(10), OUT PARMXX NUMERIC(6,2)) "
          + " LANGUAGE SQL P1: BEGIN DECLARE DUMMY INTEGER;"
          + " SET DUMMY = 5; END P1";
      s.executeUpdate(sql);
      try {
	  s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION2
			   + ".PROCCOLS2");
      } catch (Exception e) { }

      sql = "CREATE PROCEDURE " + JDDMDTest.COLLECTION2
          + ".PROCCOLS2 (IN PARM INT, OUT PARM1 TIMESTAMP, "
          + " INOUT PARM2 CHAR(10), OUT PARMXX NUMERIC(6,2)) "
          + " LANGUAGE SQL P1: BEGIN DECLARE DUMMY INTEGER;"
          + " SET DUMMY = 5; END P1";
      s.executeUpdate(sql);


      expectedRowsBase = 4  ;
      expectedRowsAll  = 8  ;
      // @C1A
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        sql="CREATE PROCEDURE "
          + JDDMDTest.COLLECTION
          + ".PROCCOLSLCN (IN THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INT) "
          + " LANGUAGE SQL P1: BEGIN DECLARE DUMMY INTEGER;"
          + " SET DUMMY = 5; END P1";
        s
            .executeUpdate(sql);
        expectedRowsBase++;
        expectedRowsAll++;
      }
    } catch (Exception e) {
      System.out.println("Exception during setup:  SQL probably = " + sql);
      e.printStackTrace();
    }
        s.close ();
    try {
      connection_.commit(); // for xa
    } catch (SQLException e) {
      // DB2 V9 throws exception .. ignore
    }

        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        dmd2_ = closedConnection_.getMetaData ();
        closedConnection_.close ();
    }


/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup() throws Exception {
    cleanup(false);
    try {
	connection_.commit(); // for xa
    } catch (SQLException e) {
      // DB2 V9 throws exception .. ignore
    }
    connection_.close();

  }

    protected void cleanup(boolean ignore) throws Exception {
        Statement s = connection_.createStatement ();
	try {
	    s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION
			     + ".PROCCOLS");
	} catch (Exception e) { if (!ignore) e.printStackTrace(); }
	try {
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION
            + ".PROCCOLS1");
	} catch (Exception e) { if (!ignore)  e.printStackTrace(); }
	try {
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION
            + ".PROCCOLS2");
	} catch (Exception e) { if (!ignore)  e.printStackTrace(); }
	try {
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION2
            + ".PROCCOLS");
	} catch (Exception e) { if (!ignore)  e.printStackTrace(); }

	try {
        s.executeUpdate ("DROP PROCEDURE " + JDDMDTest.COLLECTION2
          + ".PROCCOLS2");
	} catch (Exception e) { if (!ignore)  e.printStackTrace(); }



	try {

        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".PROCCOLT");
	} catch (Exception e) { if (!ignore)  e.printStackTrace(); }


        //@C1A
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        {
	    try {
            s.executeUpdate("DROP PROCEDURE " + JDDMDTest.COLLECTION
                            + ".PROCCOLSLCN");
	    } catch (Exception e) { if (!ignore)  e.printStackTrace(); }
        }

        s.close ();

    }




/**
getProcedureColumns() - Check the result set format.
**/
    public void Var001()
    {

	assertCondition(true, "This is checked by the new metadata tests at the end of the testcase");

    }



/**
getProcedureColumns() - Get a list of some created in this testcase and
verify all columns.
**/
    public void Var002()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (null, JDDMDTest.COLLECTION,
                "PROCCOLS%", "%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = checkString("PROCEDURE_CAT", rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;
                success = checkString("PROCEDURE_SCHEM", rs.getString ("PROCEDURE_SCHEM"), JDDMDTest.COLLECTION) && success;

                String procedureName    = rs.getString ("PROCEDURE_NAME");
                String columnName       = rs.getString ("COLUMN_NAME");
                short columnType        = rs.getShort ("COLUMN_TYPE");
                short dataType          = rs.getShort ("DATA_TYPE");
                String typeName         = rs.getString ("TYPE_NAME");
                int precision           = rs.getInt ("PRECISION");
                int length              = rs.getInt ("LENGTH");
                short scale             = rs.getShort ("SCALE");
                int radix               = rs.getInt ("RADIX");
                short nullable          = rs.getShort ("NULLABLE");
                String remarks          = rs.getString ("REMARKS");

                // System.out.println (procedureName + ":" + columnName + ":" + columnType + ":");
                // System.out.println (dataType + ":" + typeName + ":" + precision + ":" + length + ":" + scale + ":" + radix + ":");
                // System.out.println (nullable + ":" + remarks + ":");

                if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM")) {
                    success = (checkInt("PROCCOLS.PARM:columnType",columnType,DatabaseMetaData.procedureColumnIn)) && success;
                    success = (checkInt("PROCCOLS.PARM:dataType", dataType, Types.INTEGER)) && success;
                    success = (checkString("PROCCOLS.PARM:typeName", typeName,("INTEGER"))) && success;
                    success = (checkInt("PROCCOLS.PARM:precision", precision,10)) && success;
                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                        success = (checkInt("PROCCOLS.PARM:length", length ,0)) && success;
                        success = (checkString("PROCCOLS.PARM:remarks", remarks, "")) && success;
                   }
                   else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC  ) ||
                   ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                   (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLS.PARM:length", length ,4)) && success;
                      success = (checkString("PROCCOLS.PARM:remarks", remarks, null)) && success;
                    } else {
                      success = (checkInt("PROCCOLS.PARM:length", length ,0)) && success;
                      success = (checkString("PROCCOLS.PARM:remarks", remarks, "")) && success;
                    }
                    success = (checkInt("PROCCOLS.PARM:scale", scale, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM:radix", radix, 10)) && success;
                    success = (checkInt("PROCCOLS.PARM:nullable", nullable, DatabaseMetaData.columnNullable)) && success;
                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM1")) {

                    success = (checkInt("PROCCOLS.PARM1:columnType",columnType,DatabaseMetaData.procedureColumnOut)) && success;
                    success = (checkInt("PROCCOLS.PARM1:dataType", dataType, Types.TIMESTAMP)) && success;
                    success = (checkString("PROCCOLS.PARM1:typeName", typeName,("TIMESTAMP"))) && success;
                    success = (checkInt("PROCCOLS.PARM1:radix", radix, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM1:nullable", nullable, DatabaseMetaData.columnNullable)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	  success = (checkInt("PROCCOLS.PARM1:length", length ,26)) && success;
                          success = (checkInt("PROCCOLS.PARM1:scale", scale, 0)) && success;
                          success = (checkString("PROCCOLS.PARM1:remarks", remarks, "")) && success;
                          success = (checkInt("PROCCOLS.PARM1:precision", precision,0)) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC ) ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (
                            getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLS.PARM1:length", length ,16)) && success;
                      success = (checkInt("PROCCOLS.PARM1:scale", scale, 6)) && success;
                      success = (checkString("PROCCOLS.PARM1:remarks", remarks, null)) && success;
                      success = (checkInt("PROCCOLS.PARM1:precision", precision,26)) && success;
                    } else {
                      success = (checkInt("PROCCOLS.PARM1:length", length ,26)) && success;
                      success = (checkInt("PROCCOLS.PARM1:scale", scale, 0)) && success;
                      success = (checkString("PROCCOLS.PARM1:remarks", remarks, "")) && success;
                      success = (checkInt("PROCCOLS.PARM1:precision", precision,0)) && success;

                    }

                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM2")) {

                  success = (checkInt("PROCCOLS.PARM2:columnType",columnType,DatabaseMetaData.procedureColumnInOut)) && success;
                    success = (checkInt("PROCCOLS.PARM2:dataType", dataType, Types.CHAR)) && success;
                    success = (checkInt("PROCCOLS.PARM2:scale", scale, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM2:radix", radix, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM2:nullable", nullable, DatabaseMetaData.columnNullable)) && success;
                    success = (checkInt("PROCCOLS.PARM2:length", length ,10)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	   success = (checkString("PROCCOLS.PARM2:typeName", typeName,("CHARACTER"))) && success;
                           success = (checkInt("PROCCOLS.PARM2:precision", precision,0)) && success;
                           success = (checkString("PROCCOLS.PARM2:remarks", remarks, "")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC )
                        || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkString("PROCCOLS.PARM2:typeName", typeName,("CHAR"))) && success;
                      success = (checkInt("PROCCOLS.PARM2:precision", precision,10)) && success;
                      success = (checkString("PROCCOLS.PARM2:remarks", remarks, null)) && success;

                    } else {
                      success = (checkString("PROCCOLS.PARM2:typeName", typeName,("CHARACTER"))) && success;
                      success = (checkInt("PROCCOLS.PARM2:precision", precision,0)) && success;
                      success = (checkString("PROCCOLS.PARM2:remarks", remarks, "")) && success;

                    }


                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARMXX")) {
		    success = (checkInt("PROCCOLS.PARMXX:columnType",columnType,DatabaseMetaData.procedureColumnOut)) && success;

		    if (JDTestDriver.isLUW()) {
			success = (checkInt("PROCCOLS.PARMXX:dataType", dataType, Types.DECIMAL)) && success;
			success = (checkString("PROCCOLS.PARMXX:typeName", typeName,("DECIMAL"))) && success;
		    } else {
			success = (checkInt("PROCCOLS.PARMXX:dataType", dataType, Types.NUMERIC)) && success;
			success = (checkString("PROCCOLS.PARMXX:typeName", typeName,("NUMERIC"))) && success;
		    }
                    success = (checkInt("PROCCOLS.PARMXX:precision", precision,6)) && success;
                    success = (checkInt("PROCCOLS.PARMXX:scale", scale, 2)) && success;
                    success = (checkInt("PROCCOLS.PARMXX:radix", radix, 10)) && success;
                    success = (checkInt("PROCCOLS.PARMXX:nullable", nullable, DatabaseMetaData.columnNullable)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	success = (checkInt("PROCCOLS.PARMXX:length", length ,0)) && success;
                        success = (checkString("PROCCOLS.PARMXX:remarks", remarks, "")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC )
                        || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLS.PARMXX:length", length ,8)) && success;
                      success = (checkString("PROCCOLS.PARMXX:remarks", remarks, null)) && success;
                    } else {
                      success = (checkInt("PROCCOLS.PARMXX:length", length ,0)) && success;
                      success = (checkString("PROCCOLS.PARMXX:remarks", remarks, "")) && success;
                    }

                }
                else if (procedureName.equals ("PROCCOLSLCN") && columnName.equals ("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST")) {  //@C1A

                    success = (checkInt("PROCCOLSLCN.THISIS..:columnType",columnType,DatabaseMetaData.procedureColumnIn)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:dataType", dataType, Types.INTEGER)) && success;
                    success = (checkString("PROCCOLSLCN.THISIS..:typeName", typeName,("INTEGER"))) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:precision", precision,10)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:scale", scale, 0)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:radix", radix, 10)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:nullable", nullable, DatabaseMetaData.columnNullable)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	  success = (checkInt("PROCCOLSLCN.THISIS..:length", length ,0)) && success;
                          success = (checkString("PROCCOLSLCN.THISIS..:remarks", remarks, "")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC ) ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLSLCN.THISIS..:length", length ,4)) && success;
                      success = (checkString("PROCCOLSLCN.THISIS..:remarks", remarks, null)) && success;
                    } else {
                      success = (checkInt("PROCCOLSLCN.THISIS..:length", length ,0)) && success;
                      success = (checkString("PROCCOLSLCN.THISIS..:remarks", remarks, "")) && success;

                    }

                }

              //   System.out.println ("Success = " + success);
            }

            // System.out.println ("Rows = " + rows);

            rs.close ();
            if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) //@C1A
                assertCondition((rows == 5) && success, "rows = "+rows+" sb 5 "+ errorMessage);    //@C1A
            else                                            //@C1A
                assertCondition ((rows == 4) && success, "rows = "+rows+" sb 4 "+ errorMessage);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify all null parameters.  Verify that all columns
come back in the library list are returned.
**/
    public void Var003()
    {
        StringBuffer sb = new StringBuffer();
        try {
            ResultSet rs = dmd_.getProcedureColumns (null, null, null, null);

            // It is impossible to check that all columns come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;

            while (rs.next ()) {
                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";

                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
                sb.append(column);
                sb.append("\n");
            }

            rs.close ();
            assertCondition (check1 && check2 && check3,
                            "PROCCOLS(PARM)="+check1+" PROCCOLS(PARM2)="+check2+" PROCCOLS(PARMSX)="+check3+"\n columns were "+
                            sb.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify null for the catalog pattern.  All matching
tables should be returned.
**/
    public void Var004()
    {
        try {
            errorMessage="";
            ResultSet rs = dmd_.getProcedureColumns (null,
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", "%");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                String procedureCat =rs.getString ("PROCEDURE_CAT") ;
                success = checkString("PROCEDURE_CAT", procedureCat, connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows == expectedRowsAll),
                    "PROCCOLS(PARM1)="+check1+" PROCCOLS(PARM2)="+
                    check2+" PROCCOLS(PARMXX)="+check3+" rows="+rows+" sb "+expectedRowsAll+ " "+errorMessage);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify empty string for the catalog pattern.
No matching columns should be returned.
**/
    public void Var005()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns ("",
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", "%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (getDriver() == JDTestDriver.DRIVER_JCC || isSysibmMetadata()) {
		assertCondition (rows > 0, "rows should have been returned for empty catalog patttern for JCC/SYSIBM ");
	    } else {
		assertCondition (rows == 0, "No rows should have been returned for empty catalog patttern");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a catalog pattern that matches the catalog
exactly.  All matching columns should be returned.
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT", rs.getString ("PROCEDURE_CAT"), (connectionCatalog_))  && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == expectedRowsAll));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify "localhost" for the catalog pattern.
All matching columns should be returned.
**/
    public void Var007()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4 ||
          ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (
              getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
          (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
        notApplicable("\"localhost\" variation ");
      } else {

        try {
            ResultSet rs = dmd_.getProcedureColumns ("localhost",
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT", rs.getString ("PROCEDURE_CAT"), (connectionCatalog_)) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows == expectedRowsAll),
                "\nsuccess="+success+" PROCCOLS(PARM2)="+check1+" PROCCOLS(PARM1)"+check2+
                " PROCCOLS(PARMXX)="+check3+" rows="+rows+" sb "+expectedRowsAll );
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
      }



/**
getProcedureColumns() - Specify a catalog pattern for which there is a
match.  No matching columns should be returned, since we do not
support catalog pattern.
**/
    public void Var008()
    {
        try {
	    ResultSet rs;
	    if (JDTestDriver.isLUW()) {
		rs = dmd_.getProcedureColumns ("BOGUS%",
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOL%", "%");
	    } else {
		rs = dmd_.getProcedureColumns (connectionCatalog_.substring (0, 4) + "%",
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOL%", "%");
	    }

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
getProcedureColumns() - Specify a catalog pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var009()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns ("BOGUS%",
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", "%");

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
getProcedureColumns() - Specify null for the schema pattern.  All matching
columns should be returned.
**/
    public void Var010()
    {
        try {
	    StringBuffer sb = new StringBuffer();
	    sb.append("\n");
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                                            null, "PROCCOLS%", "%");

            // It is impossible to check that all columns come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
		    ++rows;

		    success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"), (connectionCatalog_)) && success;

		    String column = rs.getString ("PROCEDURE_SCHEM")
		      + "." + rs.getString ("PROCEDURE_NAME") + "("
		      + rs.getString ("COLUMN_NAME") + ")";
		    if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
			check1 = true;
		    else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
			check2 = true;
		    else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
			check3 = true;
		    else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM)")) {
		    } else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLSLCN(THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST)")) {
		    }  else {
			sb.append("Warning: unchecked entry '"+column+"'\n");
		    }
		}

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows >=  expectedRowsAll),
                "success="+success+" check1="+check1+" check2="+check2+"" +
                                " check3="+check3+" rows="+rows+"  sb >=  "+expectedRowsAll+sb.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify empty string for the schema pattern.
No matching columns should be returned.
**/
    public void Var011()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                "", "PROCCOLS%", "%");

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
getProcedureColumns() - Specify a schema pattern that matches a schema
exactly.  All matching columns should be returned.
**/
    public void Var012()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.COLLECTION, "PROCCOLS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT", rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2
                    && (rows == expectedRowsBase));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a schema pattern using a percent for which
there is a match.  All matching columns should be returned.
**/
    public void Var013()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == expectedRowsAll));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a schema pattern using an underscore for which
there is a match.  All matching columns should be returned.
**/
    public void Var014()
    {
	int expectedRows2 = 4;
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_UNDERSCORE, "PROCCOLS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT",rs.getString("PROCEDURE_CAT"),connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION2 + ".PROCCOLS2(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".PROCCOLS2(PARM1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".PROCCOLS2(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == expectedRows2), "success="+success+" check1="+check1+" check2="+check2+" check3="+check3+" rows="+rows+" expectedRows="+expectedRows2);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a schema pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var015()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                "BOGUS%", "PROCCOLS%", "%");

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
getProcedureColumns() - Specify null for the procedure pattern.  All matching
columns should be returned.
**/
    public void Var016()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, null, "%");

            // It is impossible to check that all columns come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"), connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            // SQL400 - We can't check for exactly 4 rows here as too many other values can fit
            //          the query as well (we changed the bucket so that it can be run with a
            //          user specified library - so that multiple people could run at bucket
            //          on the system at one time).  We can check for *at least* four, but not
            //          *exactly* four.
            assertCondition (success && check1 && check2 && check3  && (rows >= 4),
                "success="+success+" check1="+check1+" check2="+check2+"" +
                " check3="+check3+" rows="+rows+"sb > 4");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify empty string for the procedure pattern.
No matching columns should be returned.
**/
    public void Var017()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "", "%");

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
getProcedureColumns() - Specify a procedure pattern that matches a table
exactly.  All matching columns should be returned.
**/
    public void Var018()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS", "%");

            // Check for the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 4));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a procedure pattern using a percent for which
there is a match.  All matching columns should be returned.
**/
    public void Var019()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"), connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == expectedRowsAll));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a procedure pattern using an underscore for which
there is a match.  All matching columns should be returned.
**/
    public void Var020()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOL_", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 4));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a procedure pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var021()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "BOGUS%", "%");

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
getProcedureColumns() - Specify null for the column pattern.  All matching
columns should be returned.
**/
    public void Var022()
    {
        try {
          message.setLength(0);
	    Vector retrievedRows = new Vector();

            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", null);

            // Check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
		retrievedRows.addElement(column);

                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();

	    boolean testPassed = success && check1 && check2 && check3
                    && (rows == expectedRowsAll);
	    if (!testPassed) {
		message.append("...Test failed\n");
		message.append("...success = "+success+" sb true\n");
		message.append("...check1  = "+check1+ " sb true -- "+JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)\n");
		message.append("...check2  = "+check2+ " sb true -- "+JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)\n");
		message.append("...check3  = "+check3+ " sb true -- "+JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)\n");
		message.append("...rows    = "+rows+ " sb 4\n");
		message.append("...rows were \n");
		Enumeration enumeration = retrievedRows.elements();
		while (enumeration.hasMoreElements()) {
		    message.append("..."+enumeration.nextElement()+"\n");
		}
	    }
            assertCondition (testPassed, message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify empty string for the column pattern.
No matching columns should be returned.
**/
    public void Var023()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", "");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (getDriver() == JDTestDriver.DRIVER_JCC || isSysibmMetadata()) {
		assertCondition (rows > 0, "Empty string specified for column pattern -- rows = "+rows+" sb > 0 for LUW/SYSIBM metadata  ");
	    } else {
		assertCondition (rows == 0, "Empty string specified for column pattern -- rows = "+rows+" sb 0");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a column pattern that matches a column
exactly.  All matching columns should be returned.
**/
    public void Var024()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.COLLECTION, "PROCCOLS%", "PARM2");

            // Check for the columns.
            boolean check1 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (success && check1
                    && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a column pattern using a percent for which
there is a match.  All matching columns should be returned.
**/
    public void Var025()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.COLLECTION, "PROCCOLS%", "PARM%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 4));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a table pattern using an underscore for which
there is a match.  All matching columns should be returned.
**/
    public void Var026()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.COLLECTION, "PROCCOLS%", "PARM_");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2
                    && (rows == 2));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProcedureColumns() - Specify a column pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var027()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", "BOGUS%");

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
getProcedureColumns() - Should throw an exception when the connection
is closed.
**/
    public void Var028()
    {
        try {
            ResultSet resultSet = dmd2_.getProcedureColumns (null, null, null, null);
            failed ("Didn't throw SQLException but rs is "+resultSet);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

/**
getProcedureColumns() - Get a list of some created in this testcase and
verify all columns.  Use getString to get the values.. Near duplicate of Var002
Detects native driver bug.
**/

    public void Var029()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (null, JDDMDTest.COLLECTION,
                "PROCCOLS%", "%");
            boolean success = true;
	    errorMessage="";

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = checkString("PROCEDURE_CAT",rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;
                success = rs.getString ("PROCEDURE_SCHEM").equals (JDDMDTest.COLLECTION) && success;

                String procedureName    = rs.getString ("PROCEDURE_NAME");
                String columnName       = rs.getString ("COLUMN_NAME");
                String columnType       = rs.getString ("COLUMN_TYPE");
                String dataType         = rs.getString ("DATA_TYPE");
                String typeName         = rs.getString ("TYPE_NAME");
                String precision        = rs.getString ("PRECISION");
		if (precision == null) precision = "0";
                String length           = rs.getString ("LENGTH");
		if (length == null) length = "0";
                String scale            = rs.getString ("SCALE");
		if (scale == null) scale = "0";
                String radix            = rs.getString ("RADIX");
		if (radix == null) radix = "0";
                String nullable         = rs.getString ("NULLABLE");
                String remarks          = rs.getString ("REMARKS");

                // System.out.println (procedureName + ":" + columnName + ":" + columnType + ":");
                // System.out.println (dataType + ":" + typeName + ":" + precision + ":" + length + ":" + scale + ":" + radix + ":");
                // System.out.println (nullable + ":" + remarks + ":");

                if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM")) {
                    parameterName="PROCCOLS.PARM";
                    success = (checkString("columnType",columnType,""+DatabaseMetaData.procedureColumnIn)) && success;
                    success = (checkString("dataType",dataType,""+Types.INTEGER)) && success;
                    success = checkString("typeName",typeName,("INTEGER")) && success;
                    success = (checkString("precision",precision,""+10)) && success;
                    success = (checkString("scale",scale,""+0)) && success;
                    success = (checkString("radix",radix,""+10)) && success;
                    success = (checkString("nullable",nullable,"1")) && success;
                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	success = (checkString("length",length,""+0)) && success;
                        success = checkString("remarks",remarks,("")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC )
                        || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkString("length",length,""+4)) && success;
                      success = checkString("remarks",remarks,null) && success;
                    } else {
                       success = (checkString("length",length,""+0)) && success;
                       success = checkString("remarks",remarks,("")) && success;
                    }
                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM1")) {
                    parameterName="PROCCOLS.PARM1";
                    success = (checkString("columnType",columnType,""+DatabaseMetaData.procedureColumnOut)) && success;
                    success = (checkString("dataType",dataType,""+Types.TIMESTAMP)) && success;
                    success = checkString("typeName",typeName,("TIMESTAMP")) && success;
                    success = (checkString("radix",radix,""+0)) && success;
                    success = (checkString("nullable",nullable,"1")) && success;
                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	success = (checkString("precision",precision,""+0)) && success;
                        success = (checkString("length",length,""+26)) && success;
                        success = (checkString("scale",scale,""+0)) && success;
                        success = checkString("remarks",remarks,("")) && success;
                    }else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC ) ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkString("precision",precision,""+26)) && success;
                      success = (checkString("length",length,""+16)) && success;
                      success = (checkString("scale",scale,""+6)) && success;
                      success = checkString("remarks",remarks,null) && success;
                    } else {
                      success = (checkString("precision",precision,""+0)) && success;
                      success = (checkString("length",length,""+26)) && success;
                      success = (checkString("scale",scale,""+0)) && success;
                      success = checkString("remarks",remarks,("")) && success;
                    }

                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM2")) {
                    parameterName="PROCCOLS.PARM2";
                    success = (checkString("columnType",columnType,""+DatabaseMetaData.procedureColumnInOut)) && success;
                    success = (checkString("dataType",dataType,""+Types.CHAR)) && success;
                    success = (checkString("length",length,""+10)) && success;
                    success = (checkString("scale",scale,""+0)) && success;
                    success = (checkString("radix",radix,""+0)) && success;
                    success = (checkString("nullable",nullable,"1")) && success;
                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	 success = checkString("typeName",typeName,("CHARACTER")) && success;
                         success = (checkString("precision",precision,""+0)) && success;
                         success = checkString("remarks",remarks,("")) && success;
                    }else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC ) ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = checkString("typeName",typeName,("CHAR")) && success;
                      success = (checkString("precision",precision,""+10)) && success;
                      success = checkString("remarks",remarks,null) && success;
                    } else {
                      success = checkString("typeName",typeName,("CHARACTER")) && success;
                      success = (checkString("precision",precision,""+0)) && success;
                      success = checkString("remarks",remarks,("")) && success;
                    }

                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARMXX")) {
                    parameterName="PROCCOLS.PARMXXX";
                    success = (checkString("columnType",columnType,""+DatabaseMetaData.procedureColumnOut)) && success;
		    if (JDTestDriver.isLUW()) {
			success = (checkString("dataType",dataType,""+Types.DECIMAL)) && success;
			success = checkString("typeName",typeName,("DECIMAL")) && success;
		    } else {
			success = (checkString("dataType",dataType,""+Types.NUMERIC)) && success;
			success = checkString("typeName",typeName,("NUMERIC")) && success;
		    }
                    success = (checkString("precision",precision,""+6)) && success;
                    success = (checkString("scale",scale,""+2)) && success;
                    success = (checkString("radix",radix,""+10)) && success;
                    success = (checkString("nullable",nullable,"1")) && success;
                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	success = (checkString("length",length,""+0)) && success;
                        success = checkString("remarks",remarks,("")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) ||
                        (getDriver() == JDTestDriver.DRIVER_JCC ) ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkString("length",length,""+8)) && success;
                      success = checkString("remarks",remarks,null) && success;

                    } else {
                      success = (checkString("length",length,""+0)) && success;
                      success = checkString("remarks",remarks,("")) && success;

                    }

                }
                else if (procedureName.equals ("PROCCOLSLCN") && columnName.equals ("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST")) {  //@C1A
                    parameterName="PROCCOLSLCN.THISIS...";
                    success = (checkString("columnType",columnType,""+DatabaseMetaData.procedureColumnIn)) && success;
                    success = (checkString("dataType",dataType,""+Types.INTEGER)) && success;
                    success = checkString("typeName",typeName,("INTEGER")) && success;
                    success = (checkString("precision",precision,""+10)) && success;
                    success = (checkString("scale",scale,""+0)) && success;
                    success = (checkString("radix",radix,""+10)) && success;
                    success = (checkString("nullable",nullable,"1")) && success;
                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	success = (checkString("length",length,""+0)) && success;
                        success = checkString("remarks",remarks,("")) && success;
                    } else if ((getJdbcLevel() >= 4) || (
                        getDriver() == JDTestDriver.DRIVER_JCC ) ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkString("length",length,""+4)) && success;
                      success = checkString("remarks",remarks,null) && success;

                    } else {
                      success = (checkString("length",length,""+0)) && success;
                      success = checkString("remarks",remarks,("")) && success;

                    }


                }

              //   System.out.println ("Success = " + success);
            }

            // System.out.println ("Rows = " + rows);

            rs.close ();
            if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) //@C1A
                assertCondition((rows == 5) && success, "rows = "+rows+" "+errorMessage);    //@C1A
            else                                            //@C1A
                assertCondition ((rows == 4) && success, "rows = "+rows+" "+errorMessage+" added by native driver 8/3/05");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception Added by native driver 8/3/05");
        }
    }


    /**
     * Test a datatype in the catalog
     */
    public void testDataType(ExpectedParameterFormat ex) {
      int column = 0;
      errorMessage="";
      parameterName="";
      try {
        int maxColumn = 13;  /* The maximum column for JDBC 3.0 is 13 (remarks) */
        boolean success = true;
        boolean rowFound = false;
        Statement s = connection_.createStatement();
        try {
          s.executeUpdate("DROP PROCEDURE " + JDDMDTest.COLLECTION+ "."+ex.procedureName);
        } catch (Exception e) {
        }
        s.executeUpdate("CREATE PROCEDURE " + JDDMDTest.COLLECTION+"."+ex.procedureName+ex.parametersAndBody);

        ResultSet rs = dmd_.getProcedureColumns (null, JDDMDTest.COLLECTION, ex.procedureName+"%" , "%");
        boolean more = rs.next();
        while (more) {
          String columnName = rs.getString(4);
          if (columnName.equals(ex.columnName)) {
            rowFound = true;
            for (column = 3; column <= maxColumn; column++) {
              success=checkString(ex.getColumnName(column), rs.getString(column), ex.getColumnValue(column)) && success ;
            }
          } else {
            errorMessage+="\nFound unexpected row ";
            for (column = 3; column <= maxColumn; column++) {
              errorMessage+=rs.getString(column)+",";
            }
          }
          more = rs.next();
        }
	rs.close();
        s.executeUpdate("DROP PROCEDURE "   + JDDMDTest.COLLECTION+ "."+ex.procedureName);

        assertCondition(rowFound && success, "rowFound="+rowFound+" Test for "+ex.typeName+" added by native driver 4/27/06 "+errorMessage);

      } catch (Exception e) {
        failed (e, "Unexpected Exception Added by native driver 4/27/06 column="+column);

      }

    }

    /**
     * Test the INT data types as a parameter
    **/

        public void Var030()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDINTPROC",
                             "(C1 INT) LANGUAGE SQL P30: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P30",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.INTEGER,
              /*typeName*/   "INTEGER",
              /*precision*/  "10",
              /*length */    "4",
              /*scale */     "0",
              /*radix */     "10",
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
              );

          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
        	  if ((getDriver() == JDTestDriver.DRIVER_NATIVE )  &&
        			  (getRelease()< JDTestDriver.RELEASE_V7R1M0)) {
        		  ex.length=null;
        		  ex.remarks="";
        	  }
        	  if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
        		  ex.length="0";
        		  ex.remarks="";
        	  }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
    		  ex.length="0";
    		  ex.remarks="";
    	  }
          testDataType(ex);
        }

        /**
         * Test the SMALLINT data types as a parameter
        **/

        public void Var031()
        {

          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDSMINT",
                             "(C1 SMALLINT) LANGUAGE SQL P31: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P31",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.SMALLINT,
              /*typeName*/   "SMALLINT",
              /*precision*/  "5",
              /*length */    "2",
              /*scale */     "0",
              /*radix */     "10",
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE )  &&
                (getRelease()< JDTestDriver.RELEASE_V7R1M0)) {
              ex.length=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.length="0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.length="0";
              ex.remarks="";
            }
          testDataType(ex);
        }

        /**
         * Test the BIGINT data types as a parameter
        **/

        public void Var032()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDBIGINT",
                             "(C1 BIGINT) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.BIGINT,
              /*typeName*/   "BIGINT",
              /*precision*/  "19",
              /*length */    "8",
              /*scale */     "0",
              /*radix */     "10",
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE )  &&
                (getRelease()< JDTestDriver.RELEASE_V7R1M0)) {
              // This is really wrong..
              ex.dataType ="19";
              ex.length=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.length="0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.length="0";
              ex.remarks="";
            }
          testDataType(ex);
        }


        /**
         * Test the DECIMAL data types as a parameter
        **/

        public void Var033()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDDEC",
                             "(C1 DECIMAL(10,2)) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.DECIMAL,
              /*typeName*/   "DECIMAL",
              /*precision*/  "10",
              /*length */    "12",
              /*scale */     "2",
              /*radix */     "10",
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE )  &&
                (getRelease()< JDTestDriver.RELEASE_V7R1M0)) {
              ex.length=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.length="0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.length="0";
              ex.remarks="";
            }
          testDataType(ex);
        }


        /**
         * Test the NUMERIC data types as a parameter
        **/

        public void Var034()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDNUM",
                             "(C1 NUMERIC(10,2)) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.NUMERIC,
              /*typeName*/   "NUMERIC",
              /*precision*/  "10",
              /*length */    "12",
              /*scale */     "2",
              /*radix */     "10",
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          if (JDTestDriver.isLUW() ) {
             // NUMERIC turns into decimal
            ex.dataType = ""+Types.DECIMAL;
            ex.typeName ="DECIMAL";
          }
          // Now fix older releases with the wrong answers
          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
        	  if ((getDriver() == JDTestDriver.DRIVER_NATIVE )) {
        		  if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        			  ex.length=null;
        			  ex.remarks="";
        		  }
        	  }
        	  if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
        		  ex.length="0";
        		  ex.remarks="";
        	  }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
    		  ex.length="0";
    		  ex.remarks="";
    	  }
          testDataType(ex);
        }


        /**
         * Test the DOUBLE data types as a parameter
        **/

        public void Var035()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDDOUB",
                             "(C1 DOUBLE) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.DOUBLE,
              /*typeName*/   "DOUBLE",
              /*precision*/  "53",
              /*length */    "8",
              /*scale */     null,
              /*radix */     "2",
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
        	  if ((getDriver() == JDTestDriver.DRIVER_NATIVE )) {
        		  if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        			  ex.typeName = "DOUBLE PRECISION";
        			  ex.length=null;
        			  ex.remarks="";
        		  }
        	  }
        	  if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
        		  ex.typeName = "DOUBLE PRECISION";
        		  ex.scale="0";
        		  ex.length="0";
        		  ex.remarks="";
        	  }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
    		  ex.typeName = "DOUBLE PRECISION";
    		  ex.scale="0";
    		  ex.length="0";
    		  ex.remarks="";
    	  }
          testDataType(ex);
        }

        /**
         * Test the FLOAT data types as a parameter
        **/

        public void Var036()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDFLOAT",
                             "(C1 FLOAT) LANGUAGE SQL P36: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P36",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.FLOAT,
              /*typeName*/   "FLOAT",
              /*precision*/  "27",
              /*length */    "8",
              /*scale */     null,
              /*radix */     "2",
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          if (JDTestDriver.isLUW() || (getJdbcLevel() >= 4)||
              (getDriver() == JDTestDriver.DRIVER_NATIVE &&
                  getRelease() >= JDTestDriver.RELEASE_V7R1M0) ||
                  (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
            // LUW changes FLOAT to DOUBLE
            // SYSIBM procedures also report this
            ex.dataType = ""+Types.DOUBLE;
            ex.typeName="DOUBLE";
            ex.precision="53";
          }
          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() < JDTestDriver.RELEASE_V7R1M0 )) {
              ex.dataType=""+Types.DOUBLE;
              ex.typeName="DOUBLE PRECISION";
              ex.precision="53";
              ex.length=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.dataType=""+Types.DOUBLE;
              ex.typeName="DOUBLE PRECISION";
              ex.precision="53";
              ex.scale="0";
              ex.length="0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.dataType=""+Types.DOUBLE;
              ex.typeName="DOUBLE PRECISION";
              ex.precision="53";
              ex.scale="0";
              ex.length="0";
              ex.remarks="";
            }
          testDataType(ex);
        }
        /**
         * Test the REAL data types as a parameter
        **/

        public void Var037()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDNUM",
                             "(C1 REAL) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.REAL,
              /*typeName*/   "REAL",
              /*precision*/  "24",
              /*length */    "4",
              /*scale */     null,
              /*radix */     "2",
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
        	  if ((getDriver() == JDTestDriver.DRIVER_NATIVE )) {
        		  if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        			  ex.length=null;
        			  ex.remarks="";
        		  }
        	  }
        	  if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
        		  ex.scale="0";
        		  ex.length="0";
        		  ex.remarks="";
        	  }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
    		  ex.scale="0";
    		  ex.length="0";
    		  ex.remarks="";
    	  }
          testDataType(ex);
        }

        /**
         * Test the DATE data types as a parameter
        **/

        public void Var038()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDDATE",
                             "(C1 DATE) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.DATE,
              /*typeName*/   "DATE",
              /*precision*/  "10",
              /*length */    "6",
              /*scale */     null,
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE  && getRelease() < JDTestDriver.RELEASE_V7R1M0)) {
              ex.precision=null;
              ex.length=""+10;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.precision="0";
              ex.length="10";
              ex.scale="0";
              ex.radix="0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.precision="0";
              ex.length="10";
              ex.scale="0";
              ex.radix="0";
              ex.remarks="";
            }
          testDataType(ex);
        }
        /**
         * Test the TIME data types as a parameter
        **/

        public void Var039()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDTIME",
                             "(C1 TIME) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.TIME,
              /*typeName*/   "TIME",
              /*precision*/  "8",
              /*length */    "6",
              /*scale */     "0",
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE  && getRelease() < JDTestDriver.RELEASE_V7R1M0)) {
              ex.precision=null;
              ex.length="8";
              ex.scale=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.precision = "0";
              ex.length = "8";
              ex.radix="0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.precision = "0";
              ex.length = "8";
              ex.radix="0";
              ex.remarks="";
            }
          testDataType(ex);
        }
        /**
         * Test the TIMESTAMP data types as a parameter
        **/

        public void Var040()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDTS",
                             "(C1 TIMESTAMP) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.TIMESTAMP,
              /*typeName*/   "TIMESTAMP",
              /*precision*/  "26",
              /*length */    "16",
              /*scale */     "6",
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE  && getRelease() < JDTestDriver.RELEASE_V7R1M0)) {
              ex.precision=null;
              ex.length="26";
              ex.scale=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.precision = "0";
              ex.length= "26";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.precision = "0";
              ex.length= "26";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          testDataType(ex);
        }


        /**
         * Test the CHAR data types as a parameter
        **/

        public void Var041()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDCHAR",
                             "(C1 CHAR(80)) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.CHAR,
              /*typeName*/   "CHAR",
              /*precision*/  "80",
              /*length */    "80",
              /*scale */     null,
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE  && getRelease() < JDTestDriver.RELEASE_V7R1M0)) {
              ex.typeName="CHARACTER";
              ex.precision=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.typeName="CHARACTER";
              ex.precision = "0";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.typeName="CHARACTER";
              ex.precision = "0";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          testDataType(ex);
        }
        /**
         * Test the VARCHAR data types as a parameter
        **/

        public void Var042()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDVC",
                             "(C1 VARCHAR(80)) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.VARCHAR,
              /*typeName*/   "VARCHAR",
              /*precision*/  "80",
              /*length */    "80",
              /*scale */     null,
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE  && getRelease() < JDTestDriver.RELEASE_V7R1M0)) {
              ex.typeName="CHARACTER VARYING";
              ex.precision=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.typeName = "CHARACTER VARYING";
              ex.precision = "0";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.typeName = "CHARACTER VARYING";
              ex.precision = "0";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          testDataType(ex);
        }
        /**
         * Test the CLOB data types as a parameter
        **/

        public void Var043()
        {
	    if (getDriver()  == JDTestDriver.DRIVER_NATIVE &&
		getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
		notApplicable("Not working in pree V5R5 native code");
		return;
	    }

          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDCLOB",
                             "(C1 CLOB(100000)) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.CLOB,
              /*typeName*/   "CLOB",
              /*precision*/  "100000",
              /*length */    "100000",
              /*scale */     null,
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
        	  if ((getDriver() == JDTestDriver.DRIVER_NATIVE ) &&
		      (getRelease()< JDTestDriver.RELEASE_V7R1M0)) {
        		  // This is really wrong
        		  if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        			  // Recent CLI PTF fixed this

        		  } else {
        			  ex.dataType = null;
        		  }
        		  ex.typeName="CHARACTER LARGE OBJECT";
        		  ex.precision=null;
        		  ex.remarks="";
        	  }
        	  if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
        		  ex.typeName = "CHARACTER LARGE OBJECT";
        		  ex.precision = "0";
        		  ex.scale = "0";
        		  ex.radix = "0";
        		  ex.remarks="";
        	  }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
    		  ex.typeName = "CHARACTER LARGE OBJECT";
    		  ex.precision = "0";
    		  ex.scale = "0";
    		  ex.radix = "0";
    		  ex.remarks="";
    	  }
          testDataType(ex);
        }

        /**
         * Test the CHAR FOR BIT DATA  data types as a parameter
        **/

        public void Var044()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDBIN",
                             "(C1 CHAR(40) FOR BIT DATA) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.BINARY,
              /*typeName*/   "CHAR () FOR BIT DATA",
              /*precision*/  "40",
              /*length */    "40",
              /*scale */     null,
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE  && getRelease() < JDTestDriver.RELEASE_V7R1M0 )) {
              ex.dataType = ""+Types.CHAR;
              ex.typeName = "CHARACTER";
              ex.precision=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.dataType = ""+Types.CHAR;
              ex.typeName = "CHARACTER";
              ex.precision = "0";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.dataType = ""+Types.CHAR;
              ex.typeName = "CHARACTER";
              ex.precision = "0";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          testDataType(ex);
        }
        /**
         * Test the BLOB data types as a parameter
        **/

        public void Var045()
        {
	    if (getDriver()  == JDTestDriver.DRIVER_NATIVE &&
		getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
		notApplicable("Not working in pre V5R5 native code");
		return;
	    }

          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDBLOB",
                             "(C1 BLOB(100000)) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.BLOB,
              /*typeName*/   "BLOB",
              /*precision*/  "100000",
              /*length */    "100000",
              /*scale */     null,
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE ) &&
		(getRelease() < JDTestDriver.RELEASE_V7R1M0)) {
		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		    // Recent CLI PTF fixed this

		} else {

		    ex.dataType=null;
		}
	      ex.typeName="BINARY LARGE OBJECT";
              ex.precision=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.typeName = "BINARY LARGE OBJECT";
              ex.precision = "0";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.typeName = "BINARY LARGE OBJECT";
              ex.precision = "0";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          testDataType(ex);
        }
        /**
         * Test the DATALINK data types as a parameter
        **/

        public void Var046()
        {

 	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
	    ( (System.getProperty("java.home").indexOf("1.3") > 0) ||
	      (System.getProperty("java.home").indexOf("13") > 0)))
 	{
 	    notApplicable("Datalink not working in JDK 1.3 for toolbox ");
 	    return;
 	}



          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDDL",
                             "(C1 DATALINK) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.DATALINK,
              /*typeName*/   "DATALINK",
              /*precision*/  "200",
              /*length */    "200",
              /*scale */     null,
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          if (JDTestDriver.isLUW()) {
            notApplicable("LUW datalink doesn't work");
          } else {
          // Now fix older releases with the wrong answers
            // Now fix older releases with the wrong answers
            if ((getJdbcLevel() < 4)) {
              if ((getDriver() == JDTestDriver.DRIVER_NATIVE   && getRelease() < JDTestDriver.RELEASE_V7R1M0)) {
		ex.dataType = null;
		ex.precision = null;
                ex.remarks="";
              }
              if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
                ex.precision = "0";
                ex.scale = "0";
                ex.radix = "0";
                ex.remarks="";
              }
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
                ex.precision = "0";
                ex.scale = "0";
                ex.radix = "0";
                ex.remarks="";
              }
          testDataType(ex);
          }
        }
        /**
         * Test the VARCHAR FOR BIT DATA data types as a parameter
        **/

        public void Var047()
        {
          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGPDVB",
                             "(C1 VARCHAR(80) FOR BIT DATA ) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
              /*columnName*/ "C1",
              /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
              /*dataType*/   ""+Types.VARBINARY,
              /*typeName*/   "VARCHAR () FOR BIT DATA",
              /*precision*/  "80",
              /*length */    "80",
              /*scale */     null,
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*columnDef*/  null,
              /*sqlDataType*/ "0",
              /*sqlSub*/      "0",
              /*octetLength*/ null,
              /*ordinalPos*/  "1",
              /*isNullable*/  "",
              /*specificName*/ ""
          );

          // Now fix older releases with the wrong answers
          // Now fix older releases with the wrong answers
          if ((getJdbcLevel() < 4)) {
            if ((getDriver() == JDTestDriver.DRIVER_NATIVE  &&
                   getRelease() < JDTestDriver.RELEASE_V7R1M0 )) {
              ex.dataType=""+Types.VARCHAR;
              ex.typeName="CHARACTER VARYING";
              ex.precision=null;
              ex.remarks="";
            }
            if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.dataType=""+Types.VARCHAR;
              ex.typeName="CHARACTER VARYING";
              ex.precision = "0";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          }
          if (( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata() )) {
              ex.dataType=""+Types.VARCHAR;
              ex.typeName="CHARACTER VARYING";
              ex.precision = "0";
              ex.scale = "0";
              ex.radix = "0";
              ex.remarks="";
            }
          testDataType(ex);
        }

        /**
         * Test the BINARY  data types as a parameter
        **/

        /**
         * Test the VARBINARY data types as a parameter
        **/


  class ExpectedParameterFormat {
    String procedureName; /* 3 */
    String parametersAndBody;
    String columnName; /* 4 */
    String columnType; /* 5 */
    String dataType; /* 6 */
    String typeName; /* 7 */
    String precision; /* 8 */
    String length; /* 9 */
    String scale; /* 10 */
    String radix; /* 11 */
    String nullable; /* 12 */
    String remarks; /* 13 */
    String columnDef; /* 14 */
    String sqlDataType; /* 15 */
    String sqlDataTimeSub; /* 16 */
    String charOctetLength; /* 17 */
    String ordinalPosition; /* 18 */
    String isNullable; /* 19 */
    String specificName; /* 20 */

    public ExpectedParameterFormat(String procedureName,
        String parametersAndBody, String columnName, String columnType,
        String dataType, String typeName, String precision, String length, String scale,
        String radix, String nullable, String remarks, String columnDef,
        String sqlDataType, String sqlDataTimeSub, String charOctetLength,
        String ordinalPosition, String isNullable, String specificName) {
      this.procedureName = procedureName;
      this.parametersAndBody = parametersAndBody;
      this.columnName = columnName;
      this.columnType = columnType;
      this.dataType = dataType;
      this.typeName = typeName;
      this.precision = precision;
      this.length = length;
      this.scale = scale;
      this.radix = radix;
      this.nullable = nullable;
      this.remarks = remarks;
      this.columnDef = columnDef;
      this.sqlDataType = sqlDataType;
      this.sqlDataTimeSub = sqlDataTimeSub;
      this.charOctetLength = charOctetLength;
      this.ordinalPosition = ordinalPosition;
      this.isNullable = isNullable;
      this.specificName = specificName;
    }

    public String getColumnValue(int column) {
      switch (column) {
      case 3:
        return procedureName;

      case 4:
        return columnName;

      case 5:
        return columnType;

      case 6:
        return dataType;

      case 7:
        return typeName;

      case 8:
        return precision;

      case 9:
        return length;

      case 10:
        return scale;

      case 11:
        return radix;

      case 12:
        return nullable;

      case 13:
        return remarks;

      case 14:
        return columnDef;

      case 15:
        return sqlDataType;

      case 16:
        return sqlDataTimeSub;

      case 17:
        return charOctetLength;

      case 18:
        return ordinalPosition;

      case 19:
        return isNullable;

      case 20:
        return specificName;

      default:
        return "UNKNOWN";
      }
    }

    public String getColumnName(int column) {
      switch (column) {
      case 3:
        return "procedureName";

      case 4:
        return "columnName";

      case 5:
        return "columnType";

      case 6:
        return "dataType";

      case 7:
        return "typeName";

      case 8:
        return "precision";

      case 9:
        return "length";

      case 10:
        return "scale";

      case 11:
        return "radix";

      case 12:
        return "nullable";

      case 13:
        return "remarks";

      case 14:
        return "columnDef";

      case 15:
        return "sqlDataType";

      case 16:
        return "sqlDataTimeSub";

      case 17:
        return "charOctetLength";

      case 18:
        return "ordinalPosition";

      case 19:
        return "isNullable";

      case 20:
        return "specificName";

      default:
        return "UNKNOWN";
      }

    }
  }



/**
getProcedureColumns() - Get a list of some created in this testcase and
verify all columns.  Use getLong to get the values.. Near duplicate of Var002
Detects native driver bug with getLong @D1A
**/


    public void Var048()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (null, JDDMDTest.COLLECTION,
                "PROCCOLS%", "%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = checkString("PROCEDURE_CAT", rs.getString ("PROCEDURE_CAT"),connectionCatalog_) && success;
                success = checkString("PROCEDURE_SCHEM", rs.getString ("PROCEDURE_SCHEM"), JDDMDTest.COLLECTION) && success;

                String procedureName    = rs.getString ("PROCEDURE_NAME");
                String columnName       = rs.getString ("COLUMN_NAME");
                short columnType        = (short) rs.getLong ("COLUMN_TYPE");
                short dataType          = (short) rs.getLong ("DATA_TYPE");
                String typeName         = rs.getString ("TYPE_NAME");
                int precision           = (int) rs.getLong ("PRECISION");
                int length              = (int) rs.getLong ("LENGTH");
                short scale             = (short) rs.getLong ("SCALE");
                int radix               = (int) rs.getLong ("RADIX");
                short nullable          = (short) rs.getLong ("NULLABLE");
                String remarks          = rs.getString ("REMARKS");

                // System.out.println (procedureName + ":" + columnName + ":" + columnType + ":");
                // System.out.println (dataType + ":" + typeName + ":" + precision + ":" + length + ":" + scale + ":" + radix + ":");
                // System.out.println (nullable + ":" + remarks + ":");

                if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM")) {
                    success = (checkInt("PROCCOLS.PARM:columnType",columnType,DatabaseMetaData.procedureColumnIn)) && success;
                    success = (checkInt("PROCCOLS.PARM:dataType", dataType, Types.INTEGER)) && success;
                    success = (checkString("PROCCOLS.PARM:typeName", typeName,("INTEGER"))) && success;
                    success = (checkInt("PROCCOLS.PARM:precision", precision,10)) && success;
                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                        success = (checkInt("PROCCOLS.PARM:length", length ,0)) && success;
                        success = (checkString("PROCCOLS.PARM:remarks", remarks, "")) && success;
                   }
                   else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC  ) ||
                   ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                   (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLS.PARM:length", length ,4)) && success;
                      success = (checkString("PROCCOLS.PARM:remarks", remarks, null)) && success;
                    } else {
                      success = (checkInt("PROCCOLS.PARM:length", length ,0)) && success;
                      success = (checkString("PROCCOLS.PARM:remarks", remarks, "")) && success;
                    }
                    success = (checkInt("PROCCOLS.PARM:scale", scale, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM:radix", radix, 10)) && success;
                    success = (checkInt("PROCCOLS.PARM:nullable", nullable, DatabaseMetaData.columnNullable)) && success;
                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM1")) {

                    success = (checkInt("PROCCOLS.PARM1:columnType",columnType,DatabaseMetaData.procedureColumnOut)) && success;
                    success = (checkInt("PROCCOLS.PARM1:dataType", dataType, Types.TIMESTAMP)) && success;
                    success = (checkString("PROCCOLS.PARM1:typeName", typeName,("TIMESTAMP"))) && success;
                    success = (checkInt("PROCCOLS.PARM1:radix", radix, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM1:nullable", nullable, DatabaseMetaData.columnNullable)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	  success = (checkInt("PROCCOLS.PARM1:length", length ,26)) && success;
                          success = (checkInt("PROCCOLS.PARM1:scale", scale, 0)) && success;
                          success = (checkString("PROCCOLS.PARM1:remarks", remarks, "")) && success;
                          success = (checkInt("PROCCOLS.PARM1:precision", precision,0)) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC ) ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (
                            getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLS.PARM1:length", length ,16)) && success;
                      success = (checkInt("PROCCOLS.PARM1:scale", scale, 6)) && success;
                      success = (checkString("PROCCOLS.PARM1:remarks", remarks, null)) && success;
                      success = (checkInt("PROCCOLS.PARM1:precision", precision,26)) && success;
                    } else {
                      success = (checkInt("PROCCOLS.PARM1:length", length ,26)) && success;
                      success = (checkInt("PROCCOLS.PARM1:scale", scale, 0)) && success;
                      success = (checkString("PROCCOLS.PARM1:remarks", remarks, "")) && success;
                      success = (checkInt("PROCCOLS.PARM1:precision", precision,0)) && success;

                    }

                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM2")) {

                  success = (checkInt("PROCCOLS.PARM2:columnType",columnType,DatabaseMetaData.procedureColumnInOut)) && success;
                    success = (checkInt("PROCCOLS.PARM2:dataType", dataType, Types.CHAR)) && success;
                    success = (checkInt("PROCCOLS.PARM2:scale", scale, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM2:radix", radix, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM2:nullable", nullable, DatabaseMetaData.columnNullable)) && success;
                    success = (checkInt("PROCCOLS.PARM2:length", length ,10)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	   success = (checkString("PROCCOLS.PARM2:typeName", typeName,("CHARACTER"))) && success;
                           success = (checkInt("PROCCOLS.PARM2:precision", precision,0)) && success;
                           success = (checkString("PROCCOLS.PARM2:remarks", remarks, "")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC )
                        || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkString("PROCCOLS.PARM2:typeName", typeName,("CHAR"))) && success;
                      success = (checkInt("PROCCOLS.PARM2:precision", precision,10)) && success;
                      success = (checkString("PROCCOLS.PARM2:remarks", remarks, null)) && success;

                    } else {
                      success = (checkString("PROCCOLS.PARM2:typeName", typeName,("CHARACTER"))) && success;
                      success = (checkInt("PROCCOLS.PARM2:precision", precision,0)) && success;
                      success = (checkString("PROCCOLS.PARM2:remarks", remarks, "")) && success;

                    }


                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARMXX")) {
		    success = (checkInt("PROCCOLS.PARMXX:columnType",columnType,DatabaseMetaData.procedureColumnOut)) && success;

		    if (JDTestDriver.isLUW()) {
			success = (checkInt("PROCCOLS.PARMXX:dataType", dataType, Types.DECIMAL)) && success;
			success = (checkString("PROCCOLS.PARMXX:typeName", typeName,("DECIMAL"))) && success;
		    } else {
			success = (checkInt("PROCCOLS.PARMXX:dataType", dataType, Types.NUMERIC)) && success;
			success = (checkString("PROCCOLS.PARMXX:typeName", typeName,("NUMERIC"))) && success;
		    }
                    success = (checkInt("PROCCOLS.PARMXX:precision", precision,6)) && success;
                    success = (checkInt("PROCCOLS.PARMXX:scale", scale, 2)) && success;
                    success = (checkInt("PROCCOLS.PARMXX:radix", radix, 10)) && success;
                    success = (checkInt("PROCCOLS.PARMXX:nullable", nullable, DatabaseMetaData.columnNullable)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	success = (checkInt("PROCCOLS.PARMXX:length", length ,0)) && success;
                        success = (checkString("PROCCOLS.PARMXX:remarks", remarks, "")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC )
                        || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLS.PARMXX:length", length ,8)) && success;
                      success = (checkString("PROCCOLS.PARMXX:remarks", remarks, null)) && success;
                    } else {
                      success = (checkInt("PROCCOLS.PARMXX:length", length ,0)) && success;
                      success = (checkString("PROCCOLS.PARMXX:remarks", remarks, "")) && success;
                    }

                }
                else if (procedureName.equals ("PROCCOLSLCN") && columnName.equals ("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST")) {  //@C1A

                    success = (checkInt("PROCCOLSLCN.THISIS..:columnType",columnType,DatabaseMetaData.procedureColumnIn)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:dataType", dataType, Types.INTEGER)) && success;
                    success = (checkString("PROCCOLSLCN.THISIS..:typeName", typeName,("INTEGER"))) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:precision", precision,10)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:scale", scale, 0)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:radix", radix, 10)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:nullable", nullable, DatabaseMetaData.columnNullable)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	  success = (checkInt("PROCCOLSLCN.THISIS..:length", length ,0)) && success;
                          success = (checkString("PROCCOLSLCN.THISIS..:remarks", remarks, "")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC ) ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLSLCN.THISIS..:length", length ,4)) && success;
                      success = (checkString("PROCCOLSLCN.THISIS..:remarks", remarks, null)) && success;
                    } else {
                      success = (checkInt("PROCCOLSLCN.THISIS..:length", length ,0)) && success;
                      success = (checkString("PROCCOLSLCN.THISIS..:remarks", remarks, "")) && success;

                    }

                }

              //   System.out.println ("Success = " + success);
            }

            // System.out.println ("Rows = " + rows);

            rs.close ();
            if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) //@C1A
                assertCondition((rows == 5) && success, "rows = "+rows+" sb 5 "+ errorMessage);    //@C1A
            else                                            //@C1A
                assertCondition ((rows == 4) && success, "rows = "+rows+" sb 4 "+ errorMessage);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }

/**
getProcedureColumns() - Get a list of some created in this testcase and
verify all columns.  Use getObject to get the values.. Near duplicate of Var002
Detects native driver bug with getObject @D1A
**/


    public void Var049()
    {
        try {
            ResultSet rs = dmd_.getProcedureColumns (null, JDDMDTest.COLLECTION,
                "PROCCOLS%", "%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = checkString("PROCEDURE_CAT", (String) rs.getObject ("PROCEDURE_CAT"),connectionCatalog_) && success;
                success = checkString("PROCEDURE_SCHEM", (String) rs.getObject ("PROCEDURE_SCHEM"), JDDMDTest.COLLECTION) && success;

                String procedureName    = (String) rs.getObject ("PROCEDURE_NAME");
                String columnName       = (String) rs.getObject ("COLUMN_NAME");
                short columnType        =  ((Integer) rs.getObject ("COLUMN_TYPE")).shortValue();
                short dataType          =  ((Integer) rs.getObject ("DATA_TYPE")).shortValue();
                String typeName         = (String) rs.getObject ("TYPE_NAME");
                int precision           =  ((Integer) rs.getObject ("PRECISION")).intValue();
                int length              =  ((Integer) rs.getObject ("LENGTH")).intValue();
		Object scaleObject      =  rs.getObject ("SCALE");
                short scale;
		if (scaleObject == null) {
		    scale = 0;
		} else {
		    scale =  ((Integer)scaleObject).shortValue();
		}
		Object radixObject      =  rs.getObject ("RADIX");

                int radix;
		if (radixObject == null) {
		    radix = 0;
		} else {
		    radix =  ((Integer) radixObject ).intValue();
		}
                short nullable          =  ((Integer) rs.getObject ("NULLABLE")).shortValue();
                String remarks          = (String) rs.getObject ("REMARKS");

                // System.out.println (procedureName + ":" + columnName + ":" + columnType + ":");
                // System.out.println (dataType + ":" + typeName + ":" + precision + ":" + length + ":" + scale + ":" + radix + ":");
                // System.out.println (nullable + ":" + remarks + ":");

                if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM")) {
                    success = (checkInt("PROCCOLS.PARM:columnType",columnType,DatabaseMetaData.procedureColumnIn)) && success;
                    success = (checkInt("PROCCOLS.PARM:dataType", dataType, Types.INTEGER)) && success;
                    success = (checkString("PROCCOLS.PARM:typeName", typeName,("INTEGER"))) && success;
                    success = (checkInt("PROCCOLS.PARM:precision", precision,10)) && success;
                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                        success = (checkInt("PROCCOLS.PARM:length", length ,0)) && success;
                        success = (checkString("PROCCOLS.PARM:remarks", remarks, "")) && success;
                   }
                   else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC  ) ||
                   ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                   (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLS.PARM:length", length ,4)) && success;
                      success = (checkString("PROCCOLS.PARM:remarks", remarks, null)) && success;
                    } else {
                      success = (checkInt("PROCCOLS.PARM:length", length ,0)) && success;
                      success = (checkString("PROCCOLS.PARM:remarks", remarks, "")) && success;
                    }
                    success = (checkInt("PROCCOLS.PARM:scale", scale, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM:radix", radix, 10)) && success;
                    success = (checkInt("PROCCOLS.PARM:nullable", nullable, DatabaseMetaData.columnNullable)) && success;
                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM1")) {

                    success = (checkInt("PROCCOLS.PARM1:columnType",columnType,DatabaseMetaData.procedureColumnOut)) && success;
                    success = (checkInt("PROCCOLS.PARM1:dataType", dataType, Types.TIMESTAMP)) && success;
                    success = (checkString("PROCCOLS.PARM1:typeName", typeName,("TIMESTAMP"))) && success;
                    success = (checkInt("PROCCOLS.PARM1:radix", radix, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM1:nullable", nullable, DatabaseMetaData.columnNullable)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	  success = (checkInt("PROCCOLS.PARM1:length", length ,26)) && success;
                          success = (checkInt("PROCCOLS.PARM1:scale", scale, 0)) && success;
                          success = (checkString("PROCCOLS.PARM1:remarks", remarks, "")) && success;
                          success = (checkInt("PROCCOLS.PARM1:precision", precision,0)) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC ) ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (
                            getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLS.PARM1:length", length ,16)) && success;
                      success = (checkInt("PROCCOLS.PARM1:scale", scale, 6)) && success;
                      success = (checkString("PROCCOLS.PARM1:remarks", remarks, null)) && success;
                      success = (checkInt("PROCCOLS.PARM1:precision", precision,26)) && success;
                    } else {
                      success = (checkInt("PROCCOLS.PARM1:length", length ,26)) && success;
                      success = (checkInt("PROCCOLS.PARM1:scale", scale, 0)) && success;
                      success = (checkString("PROCCOLS.PARM1:remarks", remarks, "")) && success;
                      success = (checkInt("PROCCOLS.PARM1:precision", precision,0)) && success;

                    }

                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARM2")) {

                  success = (checkInt("PROCCOLS.PARM2:columnType",columnType,DatabaseMetaData.procedureColumnInOut)) && success;
                    success = (checkInt("PROCCOLS.PARM2:dataType", dataType, Types.CHAR)) && success;
                    success = (checkInt("PROCCOLS.PARM2:scale", scale, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM2:radix", radix, 0)) && success;
                    success = (checkInt("PROCCOLS.PARM2:nullable", nullable, DatabaseMetaData.columnNullable)) && success;
                    success = (checkInt("PROCCOLS.PARM2:length", length ,10)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	   success = (checkString("PROCCOLS.PARM2:typeName", typeName,("CHARACTER"))) && success;
                           success = (checkInt("PROCCOLS.PARM2:precision", precision,0)) && success;
                           success = (checkString("PROCCOLS.PARM2:remarks", remarks, "")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC )
                        || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkString("PROCCOLS.PARM2:typeName", typeName,("CHAR"))) && success;
                      success = (checkInt("PROCCOLS.PARM2:precision", precision,10)) && success;
                      success = (checkString("PROCCOLS.PARM2:remarks", remarks, null)) && success;

                    } else {
                      success = (checkString("PROCCOLS.PARM2:typeName", typeName,("CHARACTER"))) && success;
                      success = (checkInt("PROCCOLS.PARM2:precision", precision,0)) && success;
                      success = (checkString("PROCCOLS.PARM2:remarks", remarks, "")) && success;

                    }


                }
                else if (procedureName.equals ("PROCCOLS") && columnName.equals ("PARMXX")) {
		    success = (checkInt("PROCCOLS.PARMXX:columnType",columnType,DatabaseMetaData.procedureColumnOut)) && success;

		    if (JDTestDriver.isLUW()) {
			success = (checkInt("PROCCOLS.PARMXX:dataType", dataType, Types.DECIMAL)) && success;
			success = (checkString("PROCCOLS.PARMXX:typeName", typeName,("DECIMAL"))) && success;
		    } else {
			success = (checkInt("PROCCOLS.PARMXX:dataType", dataType, Types.NUMERIC)) && success;
			success = (checkString("PROCCOLS.PARMXX:typeName", typeName,("NUMERIC"))) && success;
		    }
                    success = (checkInt("PROCCOLS.PARMXX:precision", precision,6)) && success;
                    success = (checkInt("PROCCOLS.PARMXX:scale", scale, 2)) && success;
                    success = (checkInt("PROCCOLS.PARMXX:radix", radix, 10)) && success;
                    success = (checkInt("PROCCOLS.PARMXX:nullable", nullable, DatabaseMetaData.columnNullable)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	success = (checkInt("PROCCOLS.PARMXX:length", length ,0)) && success;
                        success = (checkString("PROCCOLS.PARMXX:remarks", remarks, "")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC )
                        || ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLS.PARMXX:length", length ,8)) && success;
                      success = (checkString("PROCCOLS.PARMXX:remarks", remarks, null)) && success;
                    } else {
                      success = (checkInt("PROCCOLS.PARMXX:length", length ,0)) && success;
                      success = (checkString("PROCCOLS.PARMXX:remarks", remarks, "")) && success;
                    }

                }
                else if (procedureName.equals ("PROCCOLSLCN") && columnName.equals ("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST")) {  //@C1A

                    success = (checkInt("PROCCOLSLCN.THISIS..:columnType",columnType,DatabaseMetaData.procedureColumnIn)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:dataType", dataType, Types.INTEGER)) && success;
                    success = (checkString("PROCCOLSLCN.THISIS..:typeName", typeName,("INTEGER"))) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:precision", precision,10)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:scale", scale, 0)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:radix", radix, 10)) && success;
                    success = (checkInt("PROCCOLSLCN.THISIS..:nullable", nullable, DatabaseMetaData.columnNullable)) && success;

                    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                    	  success = (checkInt("PROCCOLSLCN.THISIS..:length", length ,0)) && success;
                          success = (checkString("PROCCOLSLCN.THISIS..:remarks", remarks, "")) && success;
                    }
                    else if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC ) ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            (getRelease()>= JDTestDriver.RELEASE_V7R1M0)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                      success = (checkInt("PROCCOLSLCN.THISIS..:length", length ,4)) && success;
                      success = (checkString("PROCCOLSLCN.THISIS..:remarks", remarks, null)) && success;
                    } else {
                      success = (checkInt("PROCCOLSLCN.THISIS..:length", length ,0)) && success;
                      success = (checkString("PROCCOLSLCN.THISIS..:remarks", remarks, "")) && success;

                    }

                }

              //   System.out.println ("Success = " + success);
            }

            // System.out.println ("Rows = " + rows);

            rs.close ();
            if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) //@C1A
                assertCondition((rows == 5) && success, "rows = "+rows+" sb 5 "+ errorMessage);    //@C1A
            else                                            //@C1A
                assertCondition ((rows == 4) && success, "rows = "+rows+" sb 4 "+ errorMessage);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     * Test the ROWID data type as a parameter
     **/

    public void Var050()
    {


        if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
            notApplicable("V5R4 or later testcase");
            return;

        }
        ExpectedParameterFormat ex = new ExpectedParameterFormat(
                /* name */     "JDDMDGPDROWID",
                "(C1 ROWID) LANGUAGE SQL P50: BEGIN DECLARE DUMMY ROWID; SET DUMMY = C1; END P50",
                /*columnName*/ "C1",
                /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
                /*dataType*/   "-8",
                /*typeName*/   "ROWID",
                /*precision*/  "40",
                /*length */    "40",
                /*scale */     null,
                /*radix */     null,
                /*nullable*/   "1",
                /*remarks*/    null,
                /*columnDef*/  null,
                /*sqlDataType*/ "0",
                /*sqlSub*/      "0",
                /*octetLength*/ null,
                /*ordinalPos*/  "1",
                /*isNullable*/  "",
                /*specificName*/ ""
        );

        // Now fix older releases with the wrong answers
        // Now fix older releases with the wrong answers
        if ((getJdbcLevel() < 4) || getRelease() < JDTestDriver.RELEASE_V7R1M0) {
            ex.dataType = "1111";
            ex.length = "42";
        }

	// Fixed 06/02/2012
	if (getJdbcLevel() >= 4 && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
            ex.dataType = "-8";
            ex.length = "42";
	}

        // Fix V5R4
        if ((getJdbcLevel() < 4) &&
                ( getRelease() == JDTestDriver.RELEASE_V7R1M0) ) {
            ex.dataType = null;
            ex.precision = null;
            ex.length = "40";
            ex.remarks = "";
        }

        // Fix V5R4 toolbox
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && (getJdbcLevel() >= 4) &&
                ( getRelease() == JDTestDriver.RELEASE_V7R1M0) ) {
            ex.dataType = "-8";
            ex.precision = "0";
            ex.length = "40";
            ex.scale = "0";
            ex.radix = "0";
            ex.remarks = "";


        }

        // Fix V5R5 toolbox

        if ((getJdbcLevel() < 4) &&
                getRelease() < JDTestDriver.RELEASE_V7R1M0 &&
                getDriver() == JDTestDriver.DRIVER_TOOLBOX) {

            ex.dataType = "-2" ;
            ex.precision = "0";
            ex.length = "40";
            ex.scale = "0";
            ex.radix = "0";
            ex.remarks = "";
        }

        if (getJdbcLevel() >= 4 &&
                getRelease() == JDTestDriver.RELEASE_V7R1M0 &&
                getDriver() == JDTestDriver.DRIVER_TOOLBOX) {

            ex.dataType = "-8" ;
            ex.precision = "0";
            ex.length = "40";
            ex.scale = "0";
            ex.radix = "0";
            ex.remarks = "";
        }

        if (getJdbcLevel() >= 4 &&
                getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&
                getDriver() == JDTestDriver.DRIVER_TOOLBOX) {

            ex.dataType = "-8" ;
            ex.precision = "40";
            ex.length = "42";
            ex.scale = null;
            ex.radix = null;
            ex.remarks = null;
        }

        // Fix V7R1 native driver
        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&
                getDriver() == JDTestDriver.DRIVER_NATIVE) {
            ex.length = "42";
        }


        testDataType(ex);

    }

/**
getProcedureColumns() - Run getProcedureColumns multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var051()
    {
	String added = " -- added by 1/31/2011 to test for native statement leak in metadata";
        if (checkNative()) {
            try {

		Statement stmt = connection_.createStatement();

		for (int i = 0; i < 1000; i++) {
		    // System.out.println("Calling getProcedureColumns");
		    ResultSet rs =
		      dmd_.getProcedureColumns (null, JDDMDTest.COLLECTION,
						"PROCCOLS%", "%");
		    rs.close();
		}

		Statement stmt2 = connection_.createStatement();
		int beginningHandle = JDReflectionUtil.callMethod_I(stmt,"getStatementHandle");
		int endingHandle = JDReflectionUtil.callMethod_I(stmt2,"getStatementHandle");


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


  public void Var052() {
	  checkRSMD(false);
  }
  public void Var053() {
	  checkRSMD(true);
  }

    public void checkRSMD(boolean extendedMetadata)
    {


	if (getRelease() == JDTestDriver.RELEASE_V7R1M0 &&
	    getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    notApplicable("Toolbox driver fails in V5R4 on call to isAutoincrement");
	    return;
	}


	Connection connection = connection_;
	DatabaseMetaData dmd = dmd_;
	int TESTSIZE=40;
	StringBuffer prime = new StringBuffer();
	int j=0;
	int col=0;
	String added=" -- Reworked 03/23/2012";
	boolean passed=true;
	message.setLength(0);

	/* primed from 546CN */
	/* columns 11-20 added 6/22/2011 */

	String [][] methodTests = {
      {"isAutoIncrement","1","false"},
      {"isCaseSensitive","1","true"},
      {"isSearchable","1","true"},
      {"isCurrency","1","false"},
      {"isNullable","1","0"},
      {"isSigned","1","false"},
      {"getColumnDisplaySize","1","128"},
      {"getColumnLabel","1","PROCEDURE_CAT"},
      {"getColumnName","1","PROCEDURE_CAT"},
      {"getPrecision","1","128"},
      {"getScale","1","0"},
      {"getCatalogName","1","LOCALHOST"},
      {"getColumnType","1","12"},
      {"getColumnTypeName","1","VARCHAR"},
      {"isReadOnly","1","true"},
      {"isWritable","1","false"},
      {"isDefinitelyWritable","1","false"},
      {"getColumnClassName","1","java.lang.String"},
      {"isAutoIncrement","2","false"},
      {"isCaseSensitive","2","true"},
      {"isSearchable","2","true"},
      {"isCurrency","2","false"},
      {"isNullable","2","0"},
      {"isSigned","2","false"},
      {"getColumnDisplaySize","2","128"},
      {"getColumnLabel","2","PROCEDURE_SCHEM"},
      {"getColumnName","2","PROCEDURE_SCHEM"},
      {"getPrecision","2","128"},
      {"getScale","2","0"},
      {"getCatalogName","2","LOCALHOST"},
      {"getColumnType","2","12"},
      {"getColumnTypeName","2","VARCHAR"},
      {"isReadOnly","2","true"},
      {"isWritable","2","false"},
      {"isDefinitelyWritable","2","false"},
      {"getColumnClassName","2","java.lang.String"},
      {"isAutoIncrement","3","false"},
      {"isCaseSensitive","3","true"},
      {"isSearchable","3","true"},
      {"isCurrency","3","false"},
      {"isNullable","3","0"},
      {"isSigned","3","false"},
      {"getColumnDisplaySize","3","128"},
      {"getColumnLabel","3","PROCEDURE_NAME"},
      {"getColumnName","3","PROCEDURE_NAME"},
      {"getPrecision","3","128"},
      {"getScale","3","0"},
      {"getCatalogName","3","LOCALHOST"},
      {"getColumnType","3","12"},
      {"getColumnTypeName","3","VARCHAR"},
      {"isReadOnly","3","true"},
      {"isWritable","3","false"},
      {"isDefinitelyWritable","3","false"},
      {"getColumnClassName","3","java.lang.String"},
      {"isAutoIncrement","4","false"},
      {"isCaseSensitive","4","true"},
      {"isSearchable","4","true"},
      {"isCurrency","4","false"},
      {"isNullable","4","1"},
      {"isSigned","4","false"},
      {"getColumnDisplaySize","4","128"},
      {"getColumnLabel","4","COLUMN_NAME"},
      {"getColumnName","4","COLUMN_NAME"},
      {"getPrecision","4","128"},
      {"getScale","4","0"},
      {"getCatalogName","4","LOCALHOST"},
      {"getColumnType","4","12"},
      {"getColumnTypeName","4","VARCHAR"},
      {"isReadOnly","4","true"},
      {"isWritable","4","false"},
      {"isDefinitelyWritable","4","false"},
      {"getColumnClassName","4","java.lang.String"},
      {"isAutoIncrement","5","false"},
      {"isCaseSensitive","5","false"},
      {"isSearchable","5","true"},
      {"isCurrency","5","false"},
      {"isNullable","5","0"},
      {"isSigned","5","true"},
      {"getColumnDisplaySize","5","6"},
      {"getColumnLabel","5","COLUMN_TYPE"},
      {"getColumnName","5","COLUMN_TYPE"},
      {"getPrecision","5","5"},
      {"getScale","5","0"},
      {"getCatalogName","5","LOCALHOST"},
      {"getColumnType","5","5"},
      {"getColumnTypeName","5","SMALLINT"},
      {"isReadOnly","5","true"},
      {"isWritable","5","false"},
      {"isDefinitelyWritable","5","false"},
      {"getColumnClassName","5","java.lang.Integer"},
      {"isAutoIncrement","6","false"},
      {"isCaseSensitive","6","false"},
      {"isSearchable","6","true"},
      {"isCurrency","6","false"},
      {"isNullable","6","0"},
      {"isSigned","6","true"},
      {"getColumnDisplaySize","6","11"},
      {"getColumnLabel","6","DATA_TYPE"},
      {"getColumnName","6","DATA_TYPE"},
      {"getPrecision","6","10"},
      {"getScale","6","0"},
      {"getCatalogName","6","LOCALHOST"},
      {"getColumnType","6","4"},
      {"getColumnTypeName","6","INTEGER"},
      {"isReadOnly","6","true"},
      {"isWritable","6","false"},
      {"isDefinitelyWritable","6","false"},
      {"getColumnClassName","6","java.lang.Integer"},
      {"isAutoIncrement","7","false"},
      {"isCaseSensitive","7","true"},
      {"isSearchable","7","true"},
      {"isCurrency","7","false"},
      {"isNullable","7","0"},
      {"isSigned","7","false"},
      {"getColumnDisplaySize","7","261"},
      {"getColumnLabel","7","TYPE_NAME"},
      {"getColumnName","7","TYPE_NAME"},
      {"getPrecision","7","261"},
      {"getScale","7","0"},
      {"getCatalogName","7","LOCALHOST"},
      {"getColumnType","7","12"},
      {"getColumnTypeName","7","VARCHAR"},
      {"isReadOnly","7","true"},
      {"isWritable","7","false"},
      {"isDefinitelyWritable","7","false"},
      {"getColumnClassName","7","java.lang.String"},
      {"isAutoIncrement","8","false"},
      {"isCaseSensitive","8","false"},
      {"isSearchable","8","true"},
      {"isCurrency","8","false"},
      {"isNullable","8","0"},
      {"isSigned","8","true"},
      {"getColumnDisplaySize","8","11"},
      {"getColumnLabel","8","PRECISION"},
      {"getColumnName","8","PRECISION"},
      {"getPrecision","8","10"},
      {"getScale","8","0"},
      {"getCatalogName","8","LOCALHOST"},
      {"getColumnType","8","4"},
      {"getColumnTypeName","8","INTEGER"},
      {"isReadOnly","8","true"},
      {"isWritable","8","false"},
      {"isDefinitelyWritable","8","false"},
      {"getColumnClassName","8","java.lang.Integer"},
      {"isAutoIncrement","9","false"},
      {"isCaseSensitive","9","false"},
      {"isSearchable","9","true"},
      {"isCurrency","9","false"},
      {"isNullable","9","0"},
      {"isSigned","9","true"},
      {"getColumnDisplaySize","9","11"},
      {"getColumnLabel","9","LENGTH"},
      {"getColumnName","9","LENGTH"},
      {"getPrecision","9","10"},
      {"getScale","9","0"},
      {"getCatalogName","9","LOCALHOST"},
      {"getColumnType","9","4"},
      {"getColumnTypeName","9","INTEGER"},
      {"isReadOnly","9","true"},
      {"isWritable","9","false"},
      {"isDefinitelyWritable","9","false"},
      {"getColumnClassName","9","java.lang.Integer"},
      {"isAutoIncrement","10","false"},
      {"isCaseSensitive","10","false"},
      {"isSearchable","10","true"},
      {"isCurrency","10","false"},
      {"isNullable","10","1"},
      {"isSigned","10","true"},
      {"getColumnDisplaySize","10","6"},
      {"getColumnLabel","10","SCALE"},
      {"getColumnName","10","SCALE"},
      {"getPrecision","10","5"},
      {"getScale","10","0"},
      {"getCatalogName","10","LOCALHOST"},
      {"getColumnType","10","5"},
      {"getColumnTypeName","10","SMALLINT"},
      {"isReadOnly","10","true"},
      {"isWritable","10","false"},
      {"isDefinitelyWritable","10","false"},
      {"getColumnClassName","10","java.lang.Integer"},


      {"isAutoIncrement","11","false"},
      {"isCaseSensitive","11","false"},
      {"isSearchable","11","true"},
      {"isCurrency","11","false"},
      {"isNullable","11","1"},
      {"isSigned","11","true"},
      {"getColumnDisplaySize","11","6"},
      {"getColumnLabel","11","RADIX"},
      {"getColumnName","11","RADIX"},
      {"getPrecision","11","5"},
      {"getScale","11","0"},
      {"getCatalogName","11","LOCALHOST"},
      {"getColumnType","11","5"},
      {"getColumnTypeName","11","SMALLINT"},
      {"isReadOnly","11","true"},
      {"isWritable","11","false"},
      {"isDefinitelyWritable","11","false"},
      {"getColumnClassName","11","java.lang.Integer"},
      {"isAutoIncrement","12","false"},
      {"isCaseSensitive","12","false"},
      {"isSearchable","12","true"},
      {"isCurrency","12","false"},
      {"isNullable","12","0"},
      {"isSigned","12","true"},
      {"getColumnDisplaySize","12","6"},
      {"getColumnLabel","12","NULLABLE"},
      {"getColumnName","12","NULLABLE"},
      {"getPrecision","12","5"},
      {"getScale","12","0"},
      {"getCatalogName","12","LOCALHOST"},
      {"getColumnType","12","5"},
      {"getColumnTypeName","12","SMALLINT"},
      {"isReadOnly","12","true"},
      {"isWritable","12","false"},
      {"isDefinitelyWritable","12","false"},
      {"getColumnClassName","12","java.lang.Integer"},

      {"isAutoIncrement","13","false"},
      {"isCaseSensitive","13","true"},
      {"isSearchable","13","true"},
      {"isCurrency","13","false"},
      {"isNullable","13","1"},
      {"isSigned","13","false"},
      {"getColumnDisplaySize","13","2000"},
      {"getColumnLabel","13","REMARKS"},
      {"getColumnName","13","REMARKS"},
      {"getPrecision","13","2000"},
      {"getScale","13","0"},
      {"getCatalogName","13","LOCALHOST"},
      {"getColumnType","13","12"},
      {"getColumnTypeName","13","VARCHAR"},
      {"isReadOnly","13","true"},
      {"isWritable","13","false"},
      {"isDefinitelyWritable","13","false"},
      {"getColumnClassName","13","java.lang.String"},

      /* Column 14 -20  added in JDK 1.6 */
      {"isAutoIncrement","14","false"},
      {"isCaseSensitive","14","true"},
      {"isSearchable","14","true"},
      {"isCurrency","14","false"},
      {"isNullable","14","1"},
      {"isSigned","14","false"},
      {"getColumnDisplaySize","14","1"},
      {"getColumnLabel","14","COLUMN_DEF"},
      {"getColumnName","14","COLUMN_DEF"},
      {"getPrecision","14","1"},
      {"getScale","14","0"},
      {"getCatalogName","14","LOCALHOST"},
      {"getColumnType","14","12"},
      {"getColumnTypeName","14","VARCHAR"},
      {"isReadOnly","14","true"},
      {"isWritable","14","false"},
      {"isDefinitelyWritable","14","false"},
      {"getColumnClassName","14","java.lang.String"},
      {"isAutoIncrement","15","false"},
      {"isCaseSensitive","15","false"},
      {"isSearchable","15","true"},
      {"isCurrency","15","false"},
      {"isNullable","15","0"},
      {"isSigned","15","true"},
      {"getColumnDisplaySize","15","6"},
      {"getColumnLabel","15","SQL_DATA_TYPE"},
      {"getColumnName","15","SQL_DATA_TYPE"},
      {"getPrecision","15","5"},
      {"getScale","15","0"},
      {"getCatalogName","15","LOCALHOST"},
      {"getColumnType","15","5"},
      {"getColumnTypeName","15","SMALLINT"},
      {"isReadOnly","15","true"},
      {"isWritable","15","false"},
      {"isDefinitelyWritable","15","false"},
      {"getColumnClassName","15","java.lang.Integer"},
      {"isAutoIncrement","16","false"},
      {"isCaseSensitive","16","false"},
      {"isSearchable","16","true"},
      {"isCurrency","16","false"},
      {"isNullable","16","1"},
      {"isSigned","16","true"},
      {"getColumnDisplaySize","16","6"},
      {"getColumnLabel","16","SQL_DATETIME_SUB"},
      {"getColumnName","16","SQL_DATETIME_SUB"},
      {"getPrecision","16","5"},
      {"getScale","16","0"},
      {"getCatalogName","16","LOCALHOST"},
      {"getColumnType","16","5"},
      {"getColumnTypeName","16","SMALLINT"},
      {"isReadOnly","16","true"},
      {"isWritable","16","false"},
      {"isDefinitelyWritable","16","false"},
      {"getColumnClassName","16","java.lang.Integer"},
      {"isAutoIncrement","17","false"},
      {"isCaseSensitive","17","false"},
      {"isSearchable","17","true"},
      {"isCurrency","17","false"},
      {"isNullable","17","1"},
      {"isSigned","17","true"},
      {"getColumnDisplaySize","17","11"},
      {"getColumnLabel","17","CHAR_OCTET_LENGTH"},
      {"getColumnName","17","CHAR_OCTET_LENGTH"},
      {"getPrecision","17","10"},
      {"getScale","17","0"},
      {"getCatalogName","17","LOCALHOST"},
      {"getColumnType","17","4"},
      {"getColumnTypeName","17","INTEGER"},
      {"isReadOnly","17","true"},
      {"isWritable","17","false"},
      {"isDefinitelyWritable","17","false"},
      {"getColumnClassName","17","java.lang.Integer"},
      {"isAutoIncrement","18","false"},
      {"isCaseSensitive","18","false"},
      {"isSearchable","18","true"},
      {"isCurrency","18","false"},
      {"isNullable","18","0"},
      {"isSigned","18","true"},
      {"getColumnDisplaySize","18","11"},
      {"getColumnLabel","18","ORDINAL_POSITION"},
      {"getColumnName","18","ORDINAL_POSITION"},
      {"getPrecision","18","10"},
      {"getScale","18","0"},
      {"getCatalogName","18","LOCALHOST"},
      {"getColumnType","18","4"},
      {"getColumnTypeName","18","INTEGER"},
      {"isReadOnly","18","true"},
      {"isWritable","18","false"},
      {"isDefinitelyWritable","18","false"},
      {"getColumnClassName","18","java.lang.Integer"},
      {"isAutoIncrement","19","false"},
      {"isCaseSensitive","19","true"},
      {"isSearchable","19","true"},
      {"isCurrency","19","false"},
      {"isNullable","19","0"},
      {"isSigned","19","false"},
      {"getColumnDisplaySize","19","3"},
      {"getColumnLabel","19","IS_NULLABLE"},
      {"getColumnName","19","IS_NULLABLE"},
      {"getPrecision","19","3"},
      {"getScale","19","0"},
      {"getCatalogName","19","LOCALHOST"},
      {"getColumnType","19","12"},
      {"getColumnTypeName","19","VARCHAR"},
      {"isReadOnly","19","true"},
      {"isWritable","19","false"},
      {"isDefinitelyWritable","19","false"},
      {"getColumnClassName","19","java.lang.String"},
      {"isAutoIncrement","20","false"},
      {"isCaseSensitive","20","true"},
      {"isSearchable","20","true"},
      {"isCurrency","20","false"},
      {"isNullable","20","0"},
      {"isSigned","20","false"},
      {"getColumnDisplaySize","20","128"},
      {"getColumnLabel","20","SPECIFIC_NAME"},
      {"getColumnName","20","SPECIFIC_NAME"},
      {"getPrecision","20","128"},
      {"getScale","20","0"},
      {"getCatalogName","20","LOCALHOST"},
      {"getColumnType","20","12"},
      {"getColumnTypeName","20","VARCHAR"},
      {"isReadOnly","20","true"},
      {"isWritable","20","false"},
      {"isDefinitelyWritable","20","false"},
      {"getColumnClassName","20","java.lang.String"},


	};



	String[][] fixup716L = {
	    {"getColumnType","7","-1"},
	    {"getColumnTypeName","7","LONG VARCHAR"},
	    {"getColumnType","13","-1"},
	    {"getColumnTypeName","13","LONG VARGRAPHIC"},
	} ;

	String[][] fixup726L = {
	    {"getColumnType","7","-1"},
	    {"getColumnTypeName","7","LONG VARCHAR"},
	} ;

	String[][] fixup615T = {
	    /* Primed 7/24/2012 from 615TT JDDMDGetProcedureColumns */ 
	};

	String[][] fixup616T = {
	};

	String[][] fixup544T = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"isNullable","4","0"},
	    {"getColumnDisplaySize","6","6"},
	    {"getPrecision","6","5"},
	    {"getColumnType","6","5"},
	    {"getColumnTypeName","6","SMALLINT"},
	    {"getColumnDisplaySize","7","128"},
	    {"getPrecision","7","128"},
	    {"isNullable","10","0"},
	    {"isNullable","11","0"},
	    {"getColumnDisplaySize","11","11"},
	    {"getPrecision","11","10"},
	    {"getColumnType","11","4"},
	    {"getColumnTypeName","11","INTEGER"},
	    {"isNullable","13","0"},

	};

	String[][] fixup545NX = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"isNullable","4","0"},
	    {"getColumnDisplaySize","5","0"},
	    {"getPrecision","5","6"},
	    {"getColumnClassName","5","java.lang.Short"},
	    {"isSigned","6","false"},
	    {"getColumnDisplaySize","6","0"},
	    {"getPrecision","6","6"},
	    {"getColumnType","6","5"},
	    {"getColumnTypeName","6","SMALLINT"},
	    {"getColumnClassName","6","java.lang.Short"},
	    {"isSigned","7","true"},
	    {"getColumnDisplaySize","7","128"},
	    {"getPrecision","7","128"},
	    {"getColumnDisplaySize","8","0"},
	    {"getPrecision","8","11"},
	    {"getColumnDisplaySize","9","0"},
	    {"getPrecision","9","11"},
	    {"isNullable","10","0"},
	    {"getColumnDisplaySize","10","0"},
	    {"getPrecision","10","6"},
	    {"getColumnClassName","10","java.lang.Short"},
	    {"isNullable","11","0"},
	    {"getColumnDisplaySize","11","0"},
	    {"getPrecision","11","11"},
	    {"getColumnType","11","4"},
	    {"getColumnTypeName","11","INTEGER"},
	    {"getColumnDisplaySize","12","0"},
	    {"getPrecision","12","6"},
	    {"getColumnClassName","12","java.lang.Short"},
	    {"isNullable","13","0"},
	};

	String [][] fixupExtended614T = {
	};

	String [][] fixup715TS = {
	    {"getColumnTypeName","13","NVARCHAR"},
	};


	String [][] fixup716TS = {
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnDisplaySize","14","32768"},
	    {"getPrecision","14","65536"},
	    {"getColumnType","14","2005"},
            {"getColumnType","14","2011"},
	    {"getColumnTypeName","14","NCLOB"},
	    {"getColumnClassName","14","com.ibm.as400.access.AS400JDBCNClobLocator"},
	}; 

	String [][] fixup726TS = {
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnType","13","-9"}, 
	    {"getColumnDisplaySize","14","32768"},
	    {"getPrecision","14","65536"},
		    {"getColumnType","14","2011"},
	    
	    {"getColumnTypeName","14","NCLOB"},
	    {"getColumnClassName","14","com.ibm.as400.access.AS400JDBCNClobLocator"},
	};


	String [][] fixup = {};

	String[][] fixupExtended715T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","4","1"},
	    {"getColumnDisplaySize","6","11"},
	    {"getPrecision","6","10"},
	    {"getColumnType","6","4"},
	    {"getColumnTypeName","6","INTEGER"},
	    {"getColumnDisplaySize","7","261"},
	    {"getPrecision","7","261"},
	    {"isNullable","10","1"},
	    {"getColumnTypeName","13","NVARCHAR"},
	};


	String[][] fixupExtended716T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","4","1"},
	    {"getColumnDisplaySize","6","11"},
	    {"getPrecision","6","10"},
	    {"getColumnType","6","4"},
	    {"getColumnTypeName","6","INTEGER"},
	    {"getColumnDisplaySize","7","261"},
	    {"getPrecision","7","261"},
	    {"isNullable","10","1"},
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnDisplaySize","14","32768"},
	    {"getPrecision","14","65536"},
	    {"getColumnType","14","2005"},
	    {"getColumnType","14","2011"},
	    {"getColumnTypeName","14","NCLOB"},
	    {"getColumnClassName","14","com.ibm.as400.access.AS400JDBCNClobLocator"},

	};


	String[][] fixupExtended736T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","4","1"},
	    {"getColumnDisplaySize","6","11"},
	    {"getPrecision","6","10"},
	    {"getColumnType","6","4"},
	    {"getColumnTypeName","6","INTEGER"},
	    {"getColumnDisplaySize","7","261"},
	    {"getPrecision","7","261"},
	    {"isNullable","10","1"},
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnDisplaySize","14","32768"},
	    {"getPrecision","14","65536"},
	    {"getColumnType","14","2005"},
	    {"getColumnType","14","2011"},
	    {"getColumnTypeName","14","NCLOB"},
	    {"getColumnClassName","14","com.ibm.as400.access.AS400JDBCNClobLocator"},


	    {"getColumnLabel","1","Procedure Cat"},
	    {"getColumnLabel","2","Procedure Schem"},
	    {"getColumnLabel","3","Procedure Name"},
	    {"getColumnLabel","4","Column Name"},
	    {"getColumnLabel","5","Column Type"},
	    {"getColumnLabel","12","Nullable"},
	    {"getColumnLabel","13","Remarks"},
	    {"getColumnLabel","14","Column Def"},
	    {"getColumnLabel","16","SQL Datetime Sub"},
	    {"getColumnLabel","17","Char Octet Length"},
	    {"getColumnLabel","18","Ordinal Position"},
	    {"getColumnLabel","19","Is Nullable"},
	    {"getColumnLabel","20","Specific Name"},
	};



	String [][]  fixupExtended714N  = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	    {"isSearchable","11","false"},
	    {"isSearchable","12","false"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"isSearchable","13","false"},
	    {"isSearchable","14","false"},
	    {"isSearchable","15","false"},
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},
	    {"isSearchable","18","false"},
	    {"isSearchable","19","false"},
	    {"isSearchable","20","false"},
	    {"getColumnTypeName","14","NCLOB"},
	    {"getColumnClassName","14","com.ibm.as400.access.AS400JDBCNClobLocator"},
	};



	String [][]  fixupExtended717N  = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	    {"isSearchable","11","false"},
	    {"isSearchable","12","false"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"isSearchable","13","false"},
	    {"getColumnType","13","-9"},
	    {"getColumnDisplaySize","14","32768"}, /* Nov 2016 7176N */ 
	    {"getColumnType","14","2011"},
	    {"getColumnTypeName","14","NCLOB"}	,
	    {"isSearchable","14","false"},
	    
	    {"getPrecision","14","65536"},
	   
	   
	    {"getColumnClassName","14","com.ibm.db2.jdbc.app.DB2Clob"},


	    {"isSearchable","15","false"},
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},
	    {"isSearchable","18","false"},
	    {"isSearchable","19","false"},
	    {"isSearchable","20","false"},
	};


	String [][]  fixupExtended737N  = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	    {"isSearchable","11","false"},
	    {"isSearchable","12","false"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"isSearchable","13","false"},
	    {"getColumnType","13","-9"},
	    {"getColumnDisplaySize","14","32768"}, /* Nov 2016 7176N */ 
	    {"getColumnType","14","2011"},
	    {"getColumnTypeName","14","NCLOB"}	,
	    {"isSearchable","14","false"},
	    
	    {"getPrecision","14","65536"},
	   
	   
	    {"getColumnClassName","14","com.ibm.db2.jdbc.app.DB2Clob"},


	    {"isSearchable","15","false"},
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},
	    {"isSearchable","18","false"},
	    {"isSearchable","19","false"},
	    {"isSearchable","20","false"},


/* April 2019 */
      {"getColumnLabel","1","Procedure           Cat"},
      {"getColumnLabel","2","Procedure           Schem"},
      {"getColumnLabel","3","Procedure           Name"},
      {"getColumnLabel","4","Column              Name"},
      {"getColumnLabel","5","Column              Type"},
      {"getColumnLabel","12","Nullable"},
      {"getColumnLabel","13","Remarks"},
      {"getColumnLabel","14","Column              Def"},
      {"getColumnLabel","16","SQL                 Datetime            Sub"},
      {"getColumnLabel","17","Char                Octet               Length"},
      {"getColumnLabel","18","Ordinal             Position"},
      {"getColumnLabel","19","Is                  Nullable"},
      {"getColumnLabel","20","Specific            Name"},

	};



	String [][] fixupExtended545N = {
	    {"isSearchable","1","false"},
	    {"isNullable","1","1"},
	    {"isSearchable","2","false"},
	    {"isNullable","2","1"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isNullable","4","0"},
	    {"isSearchable","5","false"},
	    {"getColumnDisplaySize","5","0"},
	    {"getPrecision","5","6"},
	    {"getColumnClassName","5","java.lang.Short"},
	    {"isSearchable","6","false"},
	    {"isSigned","6","false"},
	    {"getColumnDisplaySize","6","0"},
	    {"getPrecision","6","6"},
	    {"getColumnType","6","5"},
	    {"getColumnTypeName","6","SMALLINT"},
	    {"getColumnClassName","6","java.lang.Short"},
	    {"isSearchable","7","false"},
	    {"isSigned","7","true"},
	    {"getColumnDisplaySize","7","128"},
	    {"getPrecision","7","128"},
	    {"isSearchable","8","false"},
	    {"getColumnDisplaySize","8","0"},
	    {"getPrecision","8","11"},
	    {"isSearchable","9","false"},
	    {"getColumnDisplaySize","9","0"},
	    {"getPrecision","9","11"},
	    {"isSearchable","10","false"},
	    {"isNullable","10","0"},
	    {"getColumnDisplaySize","10","0"},
	    {"getPrecision","10","6"},
	    {"getColumnClassName","10","java.lang.Short"},
	    {"isSearchable","11","false"},
	    {"isNullable","11","0"},
	    {"getColumnDisplaySize","11","0"},
	    {"getPrecision","11","11"},
	    {"getColumnType","11","4"},
	    {"getColumnTypeName","11","INTEGER"},
	    {"isSearchable","12","false"},
	    {"getColumnDisplaySize","12","0"},
	    {"getPrecision","12","6"},
	    {"getColumnClassName","12","java.lang.Short"},
	    {"isSearchable","13","false"},
	    {"isNullable","13","0"},
	} ;

	String [][] fixupExtended546N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	    {"isSearchable","11","false"},
	    {"isSearchable","12","false"},
	    {"isSearchable","13","false"},
	    {"isSearchable","14","false"},
	    {"isSearchable","15","false"},
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},
	    {"isSearchable","18","false"},
	    {"isSearchable","19","false"},
	    {"isSearchable","20","false"},
	};

	String [][] fixup714N = {
	    {"getColumnTypeName","13","NVARCHAR"},
	}; 


	

	String [][] fixup717N = {
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnType","13","-9"},
	    {"getColumnDisplaySize","14","32768"},  /* Nov 2016 7176N */
	    {"getColumnType","14","2011"},
	    {"getColumnTypeName","14","NCLOB"},
	    {"getPrecision","14","65536"},
	    {"getColumnClassName","14","com.ibm.db2.jdbc.app.DB2Clob"},
	}; 

	String [][] fixup726N = {
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnType","13","-9"},
	    {"getColumnType","14","2011"},
	    {"getColumnTypeName","14","NCLOB"},
	    {"getColumnDisplaySize","14","32768"},
	    {"getPrecision","14","65536"},
	    {"getColumnClassName","14","com.ibm.db2.jdbc.app.DB2Clob"},
	}; 
	
	Object[][] fixupArrayExtended = {
	    { "543T", fixup544T},
	    { "544T", fixup544T},
	    { "545T", fixup544T},
	    { "546T", fixup544T},

	    { "614T", fixupExtended614T},
	    { "615T", fixup615T},
	    { "616T", fixup616T},

	    { "714T", fixupExtended715T},
	    { "715T", fixupExtended715T},
	    { "716T", fixupExtended716T, "09/06/2012 -- primed"},
	    { "717T", fixupExtended716T, "09/06/2012 -- guess from 717T"},
	    { "718T", fixupExtended716T, "09/06/2012 -- guess from 717T"},
	    { "719T", fixupExtended716T, "09/06/2012 -- guess from 717T"},

	    { "726T", fixupExtended716T, "10/01/2013 -- guess"},
	    { "727T", fixupExtended716T, "10/01/2013 -- guess"},


	    { "736T",fixupExtended736T, "updated 5/23/2019"},
	    { "737T",fixupExtended736T, "updated 5/23/2019"},
	    { "738T",fixupExtended736T, "updated 5/23/2019"},
	    { "739T",fixupExtended736T, "updated 5/23/2019"},


	    { "544N", fixupExtended545N},
	    { "545N", fixupExtended545N},
	    { "546N", fixupExtended546N},

	    { "614N", fixupExtended546N},
	    { "615N", fixupExtended546N},
	    { "616N", fixupExtended546N},

	    { "714N", fixupExtended714N},
	    { "715N", fixupExtended714N},
	    { "716N", fixupExtended717N},
	    { "717N", fixupExtended717N},
	    { "718N", fixupExtended717N},
	    { "719N", fixupExtended717N},

	    { "726N", fixupExtended717N},
	    { "736N", fixupExtended737N},
	    { "737N", fixupExtended737N},
	    { "738N", fixupExtended737N},
	    { "739N", fixupExtended737N},



	    {"716L", fixup716L},
	    {"726L", fixup726L},



	};




	Object[][] fixupArray = {
	    { "543TX", fixup544T},
	    { "544TX", fixup544T},
	    { "545TX", fixup544T},
	    { "546TX", fixup544T},

	    { "614TX", fixup544T},
	    { "615TX", fixup544T},
	    { "616TX", fixup616T},

	    { "714TX", fixup544T},
	    { "715TX", fixup544T},
	    { "716TX", fixup544T},
	    { "717TX", fixup544T},
	    { "718TX", fixup544T},
	    { "719TX", fixup544T},

	    { "714TS", fixup715TS},
	    { "715TS", fixup715TS},
	    { "716TS", fixup716TS},
	    { "717TS", fixup716TS},
	    { "718TS", fixup716TS},
	    { "719TS", fixup716TS},
	    { "726TS", fixup726TS}, 
	    { "727TS", fixup726TS}, 

	    { "724TX", fixup544T},
	    { "725TX", fixup544T},
	    { "726TX", fixup544T},
	    { "727TX", fixup544T},

	    { "544NX", fixup545NX},
	    { "545NX", fixup545NX},

	    { "716LS", fixup716L},
	    { "726LS", fixup726L},



	    { "714NS", fixup714N},
	    { "715NS", fixup714N, "Primed 09/09/2012 -- guess"},
	    { "716NS", fixup717N, "Primed 01/21/2013 -- actual"}, 
	    { "717NS", fixup717N, "Primed 10/30/2012 -- actual"},
	    { "718NS", fixup717N, "Primed 10/30/2012 -- actual"},
	    { "719NS", fixup717N, "Primed 10/30/2012 -- actual"},

	    { "726NS", fixup726N, "Primed 11/8/2012 -- actual"},

	};




	if (checkJdbc30()) {
	    try {

		if (extendedMetadata) {
		    String url = baseURL_
		      + ";extended metadata=true";
		    if (getDriver() == JDTestDriver.DRIVER_JCC) {
			url = baseURL_;
		    }
		    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
			getRelease() == JDTestDriver.RELEASE_V7R1M0) {
			url += ";metadata source=1"; 
		    } 
		    connection = testDriver_.getConnection (url,
							     userId_, encryptedPassword_);
		    dmd= connection.getMetaData ();

		    fixup = getFixup(fixupArrayExtended, null, "fixupArrayExtended", message);



		} else {

		    String extra="X";
		    if (isSysibmMetadata()) {
		      extra = "S";
		    }


		    if (isJdbc40() &&
			getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
			getRelease() <= JDTestDriver.RELEASE_V7R1M0) {

			connection = testDriver_.getConnection(baseURL_						    + ";errors=full;metadata source=1;libraries=" + JDDMDTest.COLLECTION + " "
							       + JDDMDTest.COLLECTION2 + " " + JDDMDTest.COLLECTIONXX, userId_, encryptedPassword_);
			dmd= connection.getMetaData ();
			extra="S"; 
		    } 

		    fixup = getFixup(fixupArray, extra, "fixupArray", message);


		}

		if (fixup != null) {

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
		}

		boolean skipColumn14 = false;

		if (!isJdbc40()) {
		    skipColumn14 = true; 
		}
		/*  If not JDBC 4.0 dont test columns 14 and above */
		 if (skipColumn14) {
		     for (int i = 0; i < methodTests.length; i++) {
			 if (methodTests[i] != null) {
			     int column = Integer.parseInt(methodTests[i][1]);
			     if (column >= 14) {
				 methodTests[i]=methodTests[0];
			     }
			 }
		     }
		 }

		String catalog1 = connection.getCatalog();
		if (catalog1 == null) {
		    catalog1 = system_.toUpperCase();
		}

		ResultSet[] rsA =  new ResultSet[TESTSIZE];
		ResultSetMetaData[] rsmdA = new ResultSetMetaData[TESTSIZE];
		for (j = 0; j < TESTSIZE; j++) {
		    rsA[j] = dmd.getProcedureColumns (null, JDDMDTest.COLLECTION,
						"PROCCOLS%", "%");

		    rsmdA[j] = rsA[j].getMetaData ();

		    ResultSetMetaData rsmd = rsmdA[j];
		    passed = verifyRsmd(rsmd, methodTests, catalog1, j, message, prime) && passed;
		    rsA[j].close();

		} /* for j */
		assertCondition(passed, message.toString()+added+"\nPRIME=\n"+prime.toString());

	    } catch (Exception e) {
		failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
	    } catch (Error e ) {
		failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
	    }
	}
    }


        /**
         * Test the different TIMESTAMP data types as a parameter
        **/

        public void Var054()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT0",
			 "(C1 TIMESTAMP(0)) LANGUAGE SQL P54: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P54",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "19",
	      /*length */    "16",
	      /*scale */     "0",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }

        public void Var055()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT1",
			 "(C1 TIMESTAMP(1)) LANGUAGE SQL P55: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P55",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "21",
	      /*length */    "16",
	      /*scale */     "1",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var056()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT2",
			 "(C1 TIMESTAMP(2)) LANGUAGE SQL P56: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P56",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "22",
	      /*length */    "16",
	      /*scale */     "2",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var057()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT3",
			 "(C1 TIMESTAMP(3)) LANGUAGE SQL P57: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P57",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "23",
	      /*length */    "16",
	      /*scale */     "3",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var058()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT4",
			 "(C1 TIMESTAMP(4)) LANGUAGE SQL P58: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P58",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "24",
	      /*length */    "16",
	      /*scale */     "4",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var059()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT5",
			 "(C1 TIMESTAMP(5)) LANGUAGE SQL P59: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P59",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "25",
	      /*length */    "16",
	      /*scale */     "5",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var060()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT6",
			 "(C1 TIMESTAMP(6)) LANGUAGE SQL P60: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P60",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "26",
	      /*length */    "16",
	      /*scale */     "6",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var061()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT7",
			 "(C1 TIMESTAMP(7)) LANGUAGE SQL P61: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P61",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "27",
	      /*length */    "16",
	      /*scale */     "7",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var062()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT8",
			 "(C1 TIMESTAMP(8)) LANGUAGE SQL P62: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P62",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "28",
	      /*length */    "16",
	      /*scale */     "8",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var063()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT9",
			 "(C1 TIMESTAMP(9)) LANGUAGE SQL P63: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P63",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "29",
	      /*length */    "16",
	      /*scale */     "9",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var064()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT10",
			 "(C1 TIMESTAMP(10)) LANGUAGE SQL P64: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P64",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "30",
	      /*length */    "16",
	      /*scale */     "10",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var065()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT11",
			 "(C1 TIMESTAMP(11)) LANGUAGE SQL P65: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P65",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "31",
	      /*length */    "16",
	      /*scale */     "11",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }
        public void Var066()
        {
	    if (checkTimestamp12Support()) {
		ExpectedParameterFormat ex = new ExpectedParameterFormat(
	      /* name */     "JDDMDGPT0",
			 "(C1 TIMESTAMP(12)) LANGUAGE SQL P66: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P66",
	      /*columnName*/ "C1",
	      /*columnType*/ ""+DatabaseMetaData.procedureColumnIn,
	      /*dataType*/   ""+Types.TIMESTAMP,
	      /*typeName*/   "TIMESTAMP",
	      /*precision*/  "32",
	      /*length */    "16",
	      /*scale */     "12",
	      /*radix */     null,
	      /*nullable*/   "1",
	      /*remarks*/    null,
	      /*columnDef*/  null,
	      /*sqlDataType*/ "0",
	      /*sqlSub*/      "0",
	      /*octetLength*/ null,
	      /*ordinalPos*/  "1",
	      /*isNullable*/  "",
	      /*specificName*/ ""
									 );

		testDataType(ex);
	    }
        }



        /**
         * Test the CHAR data types as a parameter
        **/




/**
getProcedureColumns() -test readonly connection

**/
    public void Var067()
    {
        try {
            errorMessage="";
	    Connection c = testDriver_.getConnection (baseURL_
						      + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            ResultSet rs = dmd.getProcedureColumns (null,
                JDDMDTest.SCHEMAS_PERCENT, "PROCCOLS%", "%");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                String procedureCat =rs.getString ("PROCEDURE_CAT") ;
                success = checkString("PROCEDURE_CAT", procedureCat, connectionCatalog_) && success;

                String column = rs.getString ("PROCEDURE_SCHEM")
                    + "." + rs.getString ("PROCEDURE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARM2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".PROCCOLS(PARMXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows == expectedRowsAll),
                    "PROCCOLS(PARM1)="+check1+" PROCCOLS(PARM2)="+
                    check2+" PROCCOLS(PARMXX)="+check3+" rows="+rows+" sb "+expectedRowsAll+ " "+errorMessage);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     * Test the BOOLEAN data types as a parameter
    **/

  public void Var068() {
    if (checkBooleanSupport()) {
      ExpectedParameterFormat ex = new ExpectedParameterFormat(
          /* name */ "JDDMDGPDBOOLEAN",
          "(C1 BOOLEAN) LANGUAGE SQL P32: BEGIN DECLARE DUMMY INTEGER; SET DUMMY = 5; END P32",
          /* columnName */ "C1",
          /* columnType */ "" + DatabaseMetaData.procedureColumnIn,
          /* dataType */ "" + Types.BOOLEAN, /* typeName */ "BOOLEAN",
          /* precision */ "1", /* length */ "1", /* scale */ null,
          /* radix */ null, /* nullable */ "1", /* remarks */ null,
          /* columnDef */ null, /* sqlDataType */ "0", /* sqlSub */ "0",
          /* octetLength */ null, /* ordinalPos */ "1", /* isNullable */ "",
          /* specificName */ "");

      testDataType(ex);
    }
  }


}

