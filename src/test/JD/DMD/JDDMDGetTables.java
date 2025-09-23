///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetTables.java
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
// File Name:    JDDMDGetTables.java
//
// Classes:      JDDMDGetTables
//
////////////////////////////////////////////////////////////////////////
//
// The following differences/incompatilies are present.
//
// 1.  Some versions of LUW name the 9th column SELF_REF_COL_NAME instead
//     of SELF_REFERENCING_COL_NAME (See Var001, Var037)
// 2.  The DB2 stored procedures return null instead of "" in the REMARKS column
//     (See Var002, Var003)
// 3.  The DB2 procedures will return rows when catalog is "".  LUW returns null
//     as the catalog name.  For iSeries, the DB2 SYSIBM procedures will return
//     answers in order to be compatible. (See Var006)
// 4.  LUW doesn't support "SYNONYM" as a means to return ALIASes (See 83+)
// 5.  LUW differs in nullability of columns (see Var040)
//     column 2 was 0 sb 1
//     column 4 was 1 sb 0
//     column 5 was 1 sb 0
// 5.  LUW returns answer when an empty array of types is passed (See Var024)
// 6.  LUW will return the DBNAME from the rsmd.getCatalogName (see Var119)
//     but doesn't return the DBNAME in the column -- (see Var002, Var003, ..)
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.DMD;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDDriverTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

/**
Testcase JDDMDGetTables.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getTables()
</ul>
**/
public class JDDMDGetTables
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetTables";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }


    // Private data.
    private Connection          connection_;
    private String              connectionCatalog_;
    private Connection          closedConnection_;
    private Connection          connectionNoSysibm_; //@128sch
    private DatabaseMetaData    dmdNoSysibm_; //@128sch
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;
    private ResultSet           rsForMD = null;
    private static final String[] justTables = { "TABLE"};
    private String              jddmdCol_;

    private int allRows = 0;
    boolean setupDone = false;
    boolean setupExpectedDone = false;
    String setupVersion = "";
    String setupSysibmVersion = "";
    StringBuffer message = new StringBuffer();
    boolean is400 = true;




/**
Constructor.
**/
    public JDDMDGetTables (AS400 systemObject,
                           Hashtable<String,Vector<String>> namesAndVars,
                           int runMode,
                           FileOutputStream fileOutputStream,
                           
                           String password)
    {
        super (systemObject, "JDDMDGetTables",
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
    protected void setup() throws Exception {
    connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
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


    Statement s = connection_.createStatement();



    // Determine if LUW
    try {
	s.executeQuery("select * from qsys2.qsqptabl");
	is400 =true;
    } catch (Exception e) {
	is400 =false;
    }


    /* Make sure that the schema is valid.  In newer releases, sometimes */
    /* the SYSTABLES view is missing.  */

    validateCollection(connection_, JDDMDTest.COLLECTION);
    validateCollection(connection_, JDDMDTest.COLLECTION2);
    validateCollection(connection_, JDDMDTest.COLLECTIONXX);
    jddmdCol_ = JDDMDTest.COLLECTION+"GT"; 
    validateCollection(connection_,jddmdCol_);
    if (!is400) {
		  // 
      // Table names are limited to 8 characters.. Replace the name
      //
	if (jddmdCol_.length() > 8) {
	    jddmdCol_ = "JX"+jddmdCol_.substring(2,8); 
	} 
    }


    //@128sch toolbox metadata source ROI
    String urlNoSysibm = baseURL_;
    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
        urlNoSysibm += ";metadata source=0"; //roi (nosysibm) - sysibm is default in v7r1
    }
    connectionNoSysibm_ = testDriver_.getConnection(urlNoSysibm, userId_, encryptedPassword_);
    dmdNoSysibm_ = connectionNoSysibm_.getMetaData();

    dmd_ = connection_.getMetaData();

 
    //
    // Make sure it is clean before running
    try {
      s.executeUpdate("DROP VIEW " + JDDMDTest.COLLECTION + ".TABLESV");
    } catch (Exception e) {
    }
    try {
      s.executeUpdate("DROP VIEW " + JDDMDTest.COLLECTION2 + ".TABLESV2");
    } catch (Exception e) {
    }
    try {

      s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION + ".TABLES");
    } catch (Exception e) {
    }
    try {
      s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION + ".TABLES1");
    } catch (Exception e) {
    }
    try {
      s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION + ".TABLES2");
    } catch (Exception e) {
    }
    try {
      s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION + ".TABLESXX");
    } catch (Exception e) {
    }
    try {
      s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION2 + ".TABLES");
    } catch (Exception e) {
    }
    try {
      s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION2 + ".TABLES3");
    } catch (Exception e) {
    }
    try {
      s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION2 + ".TABLES4");
    } catch (Exception e) {
    }
    try {
      s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTIONXX + ".TABLESXX");
    } catch (Exception e) {
    }






        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".TABLES (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".TABLES1 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".TABLES2 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".TABLESXX (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".TABLES (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".TABLES3 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".TABLES4 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTIONXX
                         + ".TABLESXX (NAME INTEGER)");

        s.executeUpdate ("CREATE VIEW " + JDDMDTest.COLLECTION
                         + ".TABLESV AS SELECT * FROM " + JDDMDTest.COLLECTION
                         + ".TABLES");
        s.executeUpdate ("CREATE VIEW " + JDDMDTest.COLLECTION2
                         + ".TABLESV2 AS SELECT * FROM " + JDDMDTest.COLLECTION2
                         + ".TABLES3");

        // @128sch
        if(true)
        {
            String sql = "CREATE TABLE " + JDDMDTest.SCHEMAS_LEN128
                + ".TABLE1 (COL1 CHAR(15) DEFAULT 'DEFAULTVAL',"
                + " COL2 CHAR(15) )";
            try{
                s.executeUpdate(sql);
            }
            catch(Exception e){
                System.out.println("Warning.. create table failed " + sql);
                e.printStackTrace();
            }
        }



	setupObjects(connection_);
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

        s.executeUpdate ("DROP VIEW " + JDDMDTest.COLLECTION
                         + ".TABLESV");
        s.executeUpdate ("DROP VIEW " + JDDMDTest.COLLECTION2
                         + ".TABLESV2");

        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
                         + ".TABLES");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
                         + ".TABLES1");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
                         + ".TABLES2");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
                         + ".TABLESXX");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
                         + ".TABLES");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
                         + ".TABLES3");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
                         + ".TABLES4");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTIONXX
                         + ".TABLESXX");

        if(true) //@128sch
        {
            try{
                s.executeUpdate("DROP TABLE " + JDDMDTest.SCHEMAS_LEN128 + ".TABLE1");
            }
            catch(Exception e){
                System.out.println("Warning.. drop table failed");
                e.printStackTrace();
            }
        }


        s.close ();
        connection_.close ();
    }

  public boolean check(StringBuffer message1, String info, String a, String b) {
    boolean result;
    if (a == null) {
      result = (b == null);
    } else {
      result = a.equals(b);
    }
    if (!result) {
      message1.append(info + "=\"" + a + "\" sb \"" + b + "\" \n");
    }
    return result;
  }



/**
 * getTables() - Check the result set format.
 */
    public void Var001()
    {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            ResultSet rs = dmd_.getTables (null, null, null, null);
            ResultSetMetaData rsmd = rs.getMetaData ();
            String expectedTypesInfo = ""; 
            
            String[] expectedNames = { "TABLE_CAT", "TABLE_SCHEM",
                "TABLE_NAME", "TABLE_TYPE", "REMARKS"};
            int[] expectedTypes20 = { Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR};
            int[] expectedTypes; 
            String[] expectedNames30 = { "TABLE_CAT", "TABLE_SCHEM",
                "TABLE_NAME", "TABLE_TYPE", "REMARKS", "TYPE_CAT",
                "TYPE_SCHEM", "TYPE_NAME", "SELF_REFERENCING_COL_NAME",
                "REF_GENERATION"};

            String[] expectedNames30LUW = { "TABLE_CAT", "TABLE_SCHEM",
                "TABLE_NAME", "TABLE_TYPE", "REMARKS", "TYPE_CAT",
                "TYPE_SCHEM", "TYPE_NAME", "SELF_REF_COL_NAME",
                "REF_GENERATION"};

            int[] expectedTypes30 = { Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR};

            //v7r1 changed col 5 to longvarchar
            //v7r1 10/22/2009 col 5 is now back to VARCHAR (lp08ut23) for native driver
            int[] expectedTypesV7R1 = { Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR};

            // For JDBC50 column 5 is now NVARCHAR  
            int[] expectedTypes40V7R1 = { Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, -9,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR};

            // In V7R1, toolbox has column 5 as LONGVARCHAR
            int[] expectedTypesV7R1toolbox = { Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR};


            int count = rsmd.getColumnCount ();

            if (getJdbcLevel() <= 2) {
                boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, stringBuffer);
                boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes20, stringBuffer);
                rs.close ();
                assertCondition ((count == expectedNames.length) && (namesCheck) && (typesCheck), "getJdbcLevel() is <= 2 (*if* part of if-else), boolean namesCheck = " + namesCheck + " and should be true, boolean typesCheck = " + typesCheck + " and should be true, count =  " + count + " and should be equal to expectedNames.length = " + expectedNames.length+stringBuffer);
                }

            else
               {
                expectedTypes = expectedTypes30;
                expectedTypesInfo =" from expectedTypes30"; 
                if (JDTestDriver.isLUW()) {
                    expectedNames30 = expectedNames30LUW;
                   
                }else if(true ){
                  if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		      if (getJdbcLevel() < 4) {
			  expectedTypes = expectedTypesV7R1toolbox;
			  expectedTypesInfo =" from expectedTypesV7R1toolbox";
		      } else {
			  expectedTypes = expectedTypes40V7R1;
			  expectedTypesInfo =" from expectedTypes40V7R1toolbox";
		      }
                    
                  } else {
                    if (getJdbcLevel() < 4) {
                      expectedTypes = expectedTypesV7R1;
                      expectedTypesInfo =" from expectedTypesV7R1"; 
                    } else {
                      expectedTypes = expectedTypes40V7R1;
                      expectedTypesInfo =" from expectedTypes40V7R1"; 
                    }
                  }
                }

                boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames30, stringBuffer);
                stringBuffer.append("\n");
                boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, stringBuffer);
                stringBuffer.append("\n");
                rs.close ();
                assertCondition ((count == expectedNames30.length) && (namesCheck) && (typesCheck),
                    expectedTypesInfo+" boolean namesCheck = " + namesCheck + " sb true, boolean typesCheck = " + typesCheck + " and should be true, column Count = " + count + " and should be equal to expectedNames.length = " + expectedNames30.length+"\n"+
                    stringBuffer.toString());
            }

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Get a list of those created in this testcase and
verify all columns with system remarks (the default).

SQL400 - the 400 doesn't support the LABEL on values being brought back
         in the remarks field.  We therefore use comment on here to make
         sure that we test returning all the result set columns.
**/
    public void Var002()
    {
	StringBuffer sb = new StringBuffer();
	String operation;
	String retrieved;
	String expected;

  message.setLength(0);
        try {
            // Label the tables.  No label on TABLES, please.
            Statement s = connection_.createStatement ();

            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                s.executeUpdate ("LABEL ON TABLE " + JDDMDTest.COLLECTION
                                 + ".TABLES1 IS '9'");
                s.executeUpdate ("LABEL ON TABLE " + JDDMDTest.COLLECTION
                                 + ".TABLES2 IS 'Ten'");
                s.executeUpdate ("LABEL ON TABLE " + JDDMDTest.COLLECTION
                                 + ".TABLESXX IS 'Client Access/400'");
            } else {
                s.executeUpdate ("COMMENT ON TABLE " + JDDMDTest.COLLECTION
                                 + ".TABLES1 IS '9'");
                s.executeUpdate ("COMMENT ON TABLE " + JDDMDTest.COLLECTION
                                 + ".TABLES2 IS 'Ten'");
                s.executeUpdate ("COMMENT ON TABLE " + JDDMDTest.COLLECTION
                                 + ".TABLESXX IS 'Client Access/400'");
            }

            s.close ();

            ResultSet rs = dmd_.getTables (null, JDDMDTest.COLLECTION,
                                           "TABLES%", justTables);
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                operation= "rs.getString (\"TABLE_CAT\")";
                retrieved = rs.getString ("TABLE_CAT");
                expected  = connectionCatalog_;
                success = check(sb, operation, retrieved, expected) && success;

                operation ="rs.getString (\"TABLE_SCHEM\")";
                retrieved =	rs.getString ("TABLE_SCHEM");
                expected = JDDMDTest.COLLECTION;
                success = check(sb, operation, retrieved, expected) && success;


                operation = "rs.getString(\"TABLE_TYPE\")";
                retrieved = rs.getString ("TABLE_TYPE");
                expected = "TABLE";
                success = check(sb, operation, retrieved, expected) && success;

                String table = rs.getString ("TABLE_NAME");
                String remarks = rs.getString ("REMARKS");
                operation = "rs.getString (\"REMARKS\") for "+table;
                if (table.equals ("TABLES")) {
                    if (JDTestDriver.isLUW() ||
			getDriver() == JDTestDriver.DRIVER_JTOPENLITE || 
                            ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                                    ( isJdbc40() || true)) ||
                            (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                        expected=null;
                    } else {
                        expected = "";
                    }
                    success = check(sb, operation, remarks, expected) && success;
                } else if (table.equals ("TABLES1")) {
                    expected="9";
                    success = check(sb, operation, remarks, expected) && success;
                } else if (table.equals ("TABLES2")) {
                    expected="Ten";
                    success = check(sb, operation, remarks, expected) && success;
                } else if (table.equals ("TABLESXX")) {
                    expected="Client Access/400";
                    success = check(sb, operation, remarks, expected) && success;
                }
            }

            rs.close ();
            message.append(" rows = "+rows+" sb 4");
            assertCondition ((rows == 4) && success,sb);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Get a list of those created in this testcase and
verify all columns with sql remarks.
**/
    public void Var003()
    {
        try {
	    String operation;
	    String expected;
	    String retrieved;
	    StringBuffer sb  = new StringBuffer();
            Connection c;
            if (getDriver() == JDDriverTest.DRIVER_JCC) {
              c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

            } else {
              c = testDriver_.getConnection (baseURL_
                                                      + ";remarks=sql", userId_, encryptedPassword_);
            }

            // Label the tables.  No label on TABLES, please.
            Statement s = connection_.createStatement ();
            s.executeUpdate ("COMMENT ON TABLE " + JDDMDTest.COLLECTION
                             + ".TABLES1 IS '1'");
            s.executeUpdate ("COMMENT ON TABLE " + JDDMDTest.COLLECTION
                             + ".TABLES2 IS 'Two'");
            s.executeUpdate ("COMMENT ON TABLE " + JDDMDTest.COLLECTION
                             + ".TABLESXX IS 'AS/400 Toolbox for Java'");
            s.close ();

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getTables (null, JDDMDTest.COLLECTION,
                                          "TABLES%", justTables);
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;

                operation= "rs.getString (\"TABLE_CAT\")";
                retrieved = rs.getString ("TABLE_CAT");
                expected  = connectionCatalog_;
                success = check(sb, operation, retrieved, expected) && success;

                operation= "rs.getString (\"TABLE_SCHEM\")";
                retrieved = rs.getString ("TABLE_SCHEM");
                expected  = JDDMDTest.COLLECTION;
                success = check(sb, operation, retrieved, expected) && success;


                success = success && rs.getString ("TABLE_TYPE").equals ("TABLE");

                String table = rs.getString ("TABLE_NAME");
                String remarks = rs.getString ("REMARKS");
                if (table.equals ("TABLES")) {
                    expected="";
                    if (JDTestDriver.isLUW() ||
			getDriver() == JDTestDriver.DRIVER_JTOPENLITE ||
                            ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                                    ( isJdbc40() || true)) ||
                              (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {

                        expected=null;
                    } else {
                        expected="";
                    }
                    success = check(sb, "REMARKS", remarks, expected) && success;

                } else if (table.equals ("TABLES1"))
                    success = check(sb, "REMARKS", remarks, "1") && success;
                else if (table.equals ("TABLES2"))
                    success = check(sb, "REMARKS", remarks, "Two") && success;
                else if (table.equals ("TABLESXX"))
                    success = check(sb, "REMARKS", remarks, "AS/400 Toolbox for Java") && success;
            }

            rs.close ();
            c.close ();
            assertCondition ((rows == 4) && success,sb);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify all null parameters.  Verify that all tables
come back in the library list are returned.
**/
    public void Var004()
    {
        try {
            Connection c;
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
                c = testDriver_.getConnection (baseURL_
                                               + ";libraries=*LIBL,QIWS," + JDDMDTest.COLLECTION + ","
                                               + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                                               userId_, encryptedPassword_);
            } else {
                if (getDriver() == JDTestDriver.DRIVER_JCC) {
                   c = testDriver_.getConnection (baseURL_,
                      userId_, encryptedPassword_);

                } else {
                   c = testDriver_.getConnection (baseURL_
                                               + ";libraries=" + JDDMDTest.COLLECTION,
                                               userId_, encryptedPassword_);
                }
            }

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getTables (null, null, null, null);

            // It is impossible to check that all tables come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
	    if (JDTestDriver.isLUW()) {
		// Don't check for QIWS.QCUSTCDT
		check3=true;
	    }
            while (rs.next ()) {
                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check2 = true;
                else if (table.equals ("QIWS.QCUSTCDT"))
                    check3 = true;
            }

            rs.close ();
            c.close ();
            assertCondition (check1 && check2 && check3,
                "\n"+JDDMDTest.COLLECTION+".TABLES found="+check1+
                "\n"+JDDMDTest.COLLECTION2+".TABLES3 found="+check2+
                "\n QIWS.QCUSTCDT found="+check3);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify null for the catalog pattern.  All matching
tables should be returned.
**/
    public void Var005()
    {
        try {
            StringBuffer sb = new StringBuffer();

            ResultSet rs = dmd_.getTables (null,
                                           JDDMDTest.SCHEMAS_PERCENT, "TABLES%", justTables);

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;

                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TABLESXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 8), sb.toString() );
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify empty string for the catalog pattern.
No matching tables should be returned.
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getTables ("",
                                           JDDMDTest.SCHEMAS_PERCENT, "TABLES%", justTables);

            int rows = 0;
	    StringBuffer sb  = new StringBuffer();
	    while (rs.next ()) {
                ++rows;
		sb.append("ROW ");
		sb.append(rows);
		sb.append(" tableCat=");
		sb.append(rs.getString(1));
		sb.append("\n");
	    }

            rs.close ();
            if (JDTestDriver.isLUW() ||
                    ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                            ( isJdbc40() || true)) ||
                    (getDriver() == JDTestDriver.DRIVER_TOOLBOX) && isSysibmMetadata()) {
                assertCondition (rows > 0, "Expected > zero rows with \"\" catalog, but got "+rows+"\n"+sb.toString());
            } else {
                assertCondition (rows == 0, "Expected zero rows with \"\" catalog, but got "+rows+"\n"+sb.toString());
            }
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a catalog pattern that matches the catalog
exactly.  All matching tables should be returned.
**/
    public void Var007()
    {
      StringBuffer sb = new StringBuffer();

        try {
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "TABLES%", justTables);

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;

                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TABLESXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 8), "rows="+rows+" sb 8 check1="+check1+" check2="+check2+" check2="+check1 );
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify "localhost" for the catalog pattern.
All matching tables should be returned.
**/
    public void Var008()
    {
      StringBuffer sb = new StringBuffer();

      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4 ||
          ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (true)) ||
          (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
        notApplicable("\"localhost\" variation ");
      } else {

        try {
            ResultSet rs = dmd_.getTables ("localhost",
                                           JDDMDTest.SCHEMAS_PERCENT, "TABLES%", justTables);

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;


                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TABLESXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3 && (rows == 8),
                "\n"+JDDMDTest.COLLECTION + ".TABLES found="+check1+
                "\n"+JDDMDTest.COLLECTION2 + ".TABLES3 found = " + check2+
                "\n"+JDDMDTest.COLLECTIONXX + ".TABLESXX found = "+check3+
                "\n rows="+rows+" sb 8");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
getTables() - Specify a catalog pattern for which there is a
match.  No matching tables should be returned, since we do not
support catalog pattern.
**/
    public void Var009()
    {
        try {
	    ResultSet rs;
	    if (connectionCatalog_ != null) {
		rs = dmd_.getTables (connectionCatalog_.substring (0, 4) + "%",
                                           JDDMDTest.SCHEMAS_PERCENT, "TABLES%", justTables);
	    } else {
		rs = dmd_.getTables ("BOGUSCAT",
				       JDDMDTest.SCHEMAS_PERCENT, "TABLES%", justTables);
	    }
            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a catalog pattern for which there is no match.
No matching tables should be returned.
**/
    public void Var010()
    {
        try {
            ResultSet rs = dmd_.getTables ("BOGUS%",
                                           JDDMDTest.SCHEMAS_PERCENT, "TABLES%", justTables);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify null for the schema pattern.  All matching
tables should be returned.
**/
    public void Var011()
    {
        try {
            StringBuffer sb = new StringBuffer();

            Connection c;
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
                c = testDriver_.getConnection (baseURL_
                                               + ";libraries=*LIBL," + JDDMDTest.COLLECTION + ","
                                               + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                                               userId_, encryptedPassword_);
            } else if (getDriver() == JDTestDriver.DRIVER_JCC ) {
              c = testDriver_.getConnection (baseURL_,
                  userId_, encryptedPassword_);

            } else {
                c = testDriver_.getConnection (baseURL_
                                               + ";libraries=" + JDDMDTest.COLLECTION,
                                               userId_, encryptedPassword_);
            }

            DatabaseMetaData dmd = c.getMetaData ();
            String catalog =  c.getCatalog();
            if (catalog == null) {
              getCatalogFromURL(baseURL_);
            }
            ResultSet rs = dmd.getTables (catalog,
                                          null, "TABLES%", justTables);

            // It is impossible to check that all tables come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;


                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TABLESXX"))
                    check3 = true;
            }

            rs.close ();
            c.close ();
            assertCondition (success && check1 && check2 && check3 && (rows >= 8),
                "\n success = " + success +
                "\n"+ JDDMDTest.COLLECTION + ".TABLES found = "+check1+
                "\n"+ JDDMDTest.COLLECTION2+ ".TABLES3 found = "+ check2 +
                "\n"+ JDDMDTest.COLLECTIONXX + ".TABLESXX foudn = " + check3 +
                "\n rows = " +rows+ " AND SHOULD BE greater than 8 "+sb.toString());
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify empty string for the schema pattern.
No matching tables should be returned.
**/
    public void Var012()
    {
        try {
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           "", "TABLES%", justTables);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a schema pattern that matches a schema
exactly.  All matching tables should be returned.
**/
    public void Var013()
    {
        try {
            StringBuffer sb = new StringBuffer();
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.COLLECTION2, "TABLES%", justTables);

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;


                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES4"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 3));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a schema pattern using a percent for which
there is a match.  All matching tables should be returned.
**/
    public void Var014()
    {
        try {
          StringBuffer sb = new StringBuffer();

            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "TABLES%", justTables);

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;

                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TABLESXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 8),
                "\n"+JDDMDTest.COLLECTION + ".TABLES found="+check1+
                "\n"+JDDMDTest.COLLECTION2 + ".TABLES3 found = " + check2+
                "\n"+JDDMDTest.COLLECTIONXX + ".TABLESXX found = "+check3+
                "\n rows="+rows+" sb 8");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a schema pattern using an underscore for which
there is a match.  All matching tables should be returned.
**/
    public void Var015()
    {
        try {
          StringBuffer sb = new StringBuffer();

            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_UNDERSCORE, "TABLES%", justTables);

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;

                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES4"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check3 = true;
		else {
		    sb.append("\n extra table="+table);
		}

		String schema = rs.getString ("TABLE_SCHEM");
		if (!schema.equals(JDDMDTest.COLLECTION) &&
		    !schema.equals(JDDMDTest.COLLECTION2)) {
		    sb.append("\nUnrecognized schema "+schema);
		}


            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
			     && (rows == 3),
			     "success="+success+
			     " check1="+check1+
			     " check2="+check2+
			     " check3="+check3+
			     " rows="+rows+ " " +sb);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a schema pattern for which there is no match.
No matching tables should be returned.
**/
    public void Var016()
    {
        try {
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           "BOGUS%", "TABLES%", justTables);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify null for the table pattern.  All matching
tables should be returned.
**/
    public void Var017()
    {
        try {
          StringBuffer sb = new StringBuffer();

            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, null, justTables);

            // It is impossible to check that all tables come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;

                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TABLESXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows >= 8));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify empty string for the table pattern.
No matching tables should be returned.
**/
    public void Var018()
    {
        try {
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "", justTables);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a table pattern that matches a table
exactly.  All matching tables should be returned.
**/
    public void Var019()
    {
        try {
          StringBuffer sb = new StringBuffer();

            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "TABLES", justTables);

            // Check for the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;

                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2
                    && (rows == 2),
                "\n"+JDDMDTest.COLLECTION + ".TABLES found="+check1+
                "\n"+JDDMDTest.COLLECTION2 + ".TABLES3 found = " + check2+
                "\n rows="+rows+" sb 2");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a table pattern using a percent for which
there is a match.  All matching tables should be returned.
**/
    public void Var020()
    {
      StringBuffer sb = new StringBuffer();

        try {
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "TABLES%", justTables);

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;

                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TABLESXX"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 8),
                "\n"+JDDMDTest.COLLECTION + ".TABLES found="+check1+
                "\n"+JDDMDTest.COLLECTION2 + ".TABLES3 found = " + check2+
                "\n"+JDDMDTest.COLLECTIONXX + ".TABLESXX found = "+check3+
                "\n rows="+rows+" sb 8");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a tables pattern using an underscore for which
there is a match.  All matching tables should be returned.
**/
    public void Var021()
    {
        try {
          StringBuffer sb = new StringBuffer();

            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "TABLES_", justTables);

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;


                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLES1"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES4"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 4),
                "\n"+JDDMDTest.COLLECTION + ".TABLES1 found="+check1+
                "\n"+JDDMDTest.COLLECTION2 + ".TABLES3 found = " + check2+
                "\n"+JDDMDTest.COLLECTION2 + ".TABLES4 found = "+check3+
                "\n rows="+rows+" sb 4");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a table pattern for which there is no match.
No matching tables should be returned.
**/
    public void Var022()
    {
        try {
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "BOGUS%", justTables);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getTables() - Specify null for the types.  All matching
tables should be returned.
**/
    public void Var023()
    {
        try {
          StringBuffer sb = new StringBuffer();

            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "%", null);

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
	    if (JDTestDriver.isLUW()) {
		// We don't need to check for SYSTABLES in the collection
		check3 = true;
	    }
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;

                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                String type = rs.getString ("TABLE_TYPE");

                if ((getDriver () == JDTestDriver.DRIVER_TOOLBOX )
                    || (getDriver() == JDTestDriver.DRIVER_JCC) ||
                    (getJdbcLevel() >= 4) || ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (true)))  {
		    if ((table.equals (JDDMDTest.COLLECTION + ".TABLES"))
			&& (type.equals ("TABLE")))
			check1 = true;
		    else if ((table.equals (JDDMDTest.COLLECTION2 + ".TABLESV2"))
			     && (type.equals ("VIEW")))
			check2 = true;

		    // SYSTEM TABLEs are really just VIEWs that start with
                    // "SYS" or logical or physical files that start with
                    // "QIDCT" or "QADB".  Consequently, the table type
                    // comes back as VIEW.  There is no way to get a table
                    // type back of SYSTEM TABLE.
		    else if ((table.equals (JDDMDTest.COLLECTIONXX + ".SYSTABLES"))
			     && (type.equals ("VIEW")))
			check3 = true;
		} else {
		    if ((table.equals (JDDMDTest.COLLECTION + ".TABLES"))){	// @C1
			if ( type.equals("TABLE") )	{			// @C1
check1 = true;					// @C1
// System.out.println("... check 1  type = "+type+" sb TABLE");
      }
		    }
                    else if ((table.equals (JDDMDTest.COLLECTION2 + ".TABLESV2"))
                             & (type.equals ("VIEW")))
                        check2 = true;
                    else if ((table.equals (JDDMDTest.COLLECTIONXX + ".SYSTABLES"))
			     && (type.equals ("SYSTEM TABLE")))
			// System.out.println(table+" "+type);
			check3 = true;
		}
	    }

	    rs.close ();

            assertCondition (success && check1 && check2 && check3 && (rows >= 10),
                "\nsuccess = "+success+
                "\n check1("+JDDMDTest.COLLECTION + ".TABLES/TABLE) = "+check1+
                "\n check2("+JDDMDTest.COLLECTION2 + ".TABLESV2/VIEW) = "+check2+
                "\n check3("+JDDMDTest.COLLECTIONXX + ".SYSTABLES/VIEW) = "+check3+
                "\n row2 = "+rows+" and SB >= 10");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify empty array for the type.
No matching tables should be returned.
**/
    public void Var024()
    {
        try {
            String[] types = new String[0];
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "%", types);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (JDTestDriver.isLUW()) {
		notApplicable("LUW returns answers for empty array type");
	    } else {
		assertCondition (rows == 0, "Empty array specified for type and "+rows+" rows returned -- sb 0");
	    }
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a 1 element array that matches a type
exactly.  All matching tables should be returned.
**/
    public void Var025()
    {
      StringBuffer sb = new StringBuffer();
      message.setLength(0);
        try {
            String[] types = { "VIEW"};
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "%", types);

            // Check for the tables.
            boolean tablesvFound = false;
            boolean tablesV2found = false;
            boolean logicalFound = false;
            boolean viewFound = false;

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;
/*
		if (! rs.getString ("TABLE_CAT").equals (connectionCatalog_)) {
		    success=false;
		    message+="TABLE_CAT is "+rs.getString("")+" sb "+connectionCatalog_+"\n";
		}
*/
                  String table = rs.getString ("TABLE_NAME");
		if (!rs.getString ("TABLE_TYPE").equals ("VIEW")) {
		    if (table.indexOf("LOGICAL") < 0) { 

			success=false;
			message.append("TABLE_TYPE is "+rs.getString("TABLE_TYPE")+" sb VIEW\n");
		    }
		}


                table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
		if (table.equals (JDDMDTest.COLLECTION + ".TABLESV")) {
                    tablesvFound = true;
		}  else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLESV2")) {
                    tablesV2found = true;
		}  else if (table.equals (jddmdCol_ + ".LOGICAL1")) {
                    logicalFound = true;
		}  else if (table.equals (jddmdCol_ + ".VIEW1")) {
                    viewFound = true;
		} else {
		    if (table.indexOf(".SYS")>=0) {
			// IGNORE the sysibm tables
			rows--;
		    } else {
			message.append("Did not recognize row with "+table+"\n");
		    }
		}
            }

            rs.close ();
            assertCondition (success && tablesvFound && tablesV2found && logicalFound && viewFound , "success="+success+" rows="+rows+" sb 2 tablesvFound="+tablesvFound+" tablesV2found="+tablesV2found+" logicalFound="+logicalFound+" viewFound="+viewFound+" "+message);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a 1 element array that does not match a type.
All matching tables should be returned.
**/
    public void Var026()
    {
        try {
            String[] types = { "BOGUS"};
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "%", types);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a multiple element array that matches all types
exactly.  All matching tables should be returned.
**/
    public void Var027()
    {
      StringBuffer sb = new StringBuffer();

        try {
            String[] types = { "SYSTEM TABLE", "VIEW", "TABLE"};
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "%TABLES%", types);

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
	    if (JDTestDriver.isLUW()) {
		// no SYSTABLES on LUW
		check3=true;
	    }

            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;

                String table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                String type = rs.getString ("TABLE_TYPE");

                if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
                    if ((table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                        && (type.equals ("TABLE")))
                        check1 = true;
                    else if ((table.equals (JDDMDTest.COLLECTION2 + ".TABLESV2"))
                             && (type.equals ("VIEW")))
                        check2 = true;

                    // SYSTEM TABLEs are really just VIEWs that start with
                    // "SYS" or logical or physical files that start with
                    // "QIDCT" or "QADB".  Consequently, the table type
                    // comes back as VIEW.  There is no way to get a table
                    // type back of SYSTEM TABLE.
                    else if ((table.equals (JDDMDTest.COLLECTIONXX + ".SYSTABLES"))
                             && (type.equals ("VIEW")))
                        check3 = true;
                } else {

		    if ((table.equals (JDDMDTest.COLLECTION + ".TABLES"))){	// @C1
			if ( type.equals("TABLE") ) {			// @C1
check1 = true;					// @C1
// System.out.println("... type = "+type+" sb TABLE");
      }
		    }
                    else if ((table.equals (JDDMDTest.COLLECTION2 + ".TABLESV2"))
                             & (type.equals ("VIEW")))
                        check2 = true;
                    else if ((table.equals (JDDMDTest.COLLECTIONXX + ".SYSTABLES"))
                             && (type.equals ("SYSTEM TABLE") || type.equals ("VIEW") ))
                        check3 = true;
		 //   System.out.println(table+" "+type);
                }
            }

            rs.close ();

            assertCondition (success && check1 && check2 && check3
                    && (rows >= 10), "success = "+success+" check1 = "+check1+" check2 = "+check2+" check3 = "+check3+" row = "+rows+" and SB >= 10");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a multiple element array that matches some types
exactly but includes a bogus type.  All matching tables should be returned.
**/
    public void Var028()
    {
      StringBuffer sb = new StringBuffer();

        try {
            String[] types = { "VIEW", "BOGUS"};
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "%", types);

            // Check for the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;
                String table = rs.getString ("TABLE_NAME");
		if (!rs.getString ("TABLE_TYPE").equals ("VIEW")) {
		    if (table.indexOf("LOGICAL") < 0) { 
			success = false;
			sb.append("Table type is "+rs.getString ("TABLE_TYPE"));
		    }
		} 


                table = rs.getString ("TABLE_SCHEM")
                               + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TABLESV"))
                    check1 = true;
		else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLESV2")) {
                    check2 = true;
		}  else if (table.equals (jddmdCol_ + ".LOGICAL1")) {
                    // Ignore 
		}  else if (table.equals (jddmdCol_ + ".VIEW1")) {
                    // Ignore 
		} else if (table.indexOf(".SYS") >= 0) {
		    // IGNORE
		    rows--;
		}
            }

            rs.close ();
            assertCondition (success && check1 && check2 ,
                             "\nsuccess = "+success+
			     "\nrows = "+rows+" sb 2" +
                             "\n"+JDDMDTest.COLLECTION + ".TABLESV found = "+check1+
                             "\n"+JDDMDTest.COLLECTION2 + ".TABLESV2 found ="+check2+
                             "\n rows="+rows);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Specify a multiple element array that does not match
any types.  No matching tables should be returned.
**/
    public void Var029()
    {
        try {
            String[] types = { "THESE", "ARE", "ALL", "BOGUS"};
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "%", types);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTables() - Should throw an exception when the connection
is closed.
**/
    public void Var030()
    {
        try {
            ResultSet resultSet = dmd2_.getTables (null, null, null, null);
            failed ("Didn't throw SQLException for "+resultSet);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getTables() - Use multiple result sets from a metadata object at the same time.
This is a test added after a v4r4 bug was discovered which was limiting JDBC
to one ResultSet per DatabaseMetaData object at a time.
**/
    public void Var031()   // @B1A
    {
        if (checkNative()) {
            try {
                String catalog = null;
                String schema = "JDTESTDMD";
                String table   = "%";
                String column = "%";
                String types[]  = {"TABLE", "VIEW", "SYSTEM TABLE"};
                String columnsTable = null;
                String columnsColumn = null;

                ResultSet rs = dmd_.getTables(catalog, schema, table, types);

                while (rs.next()) {
                    columnsTable = rs.getString(3);

                    ResultSet rs2 = dmd_.getColumns(catalog, schema, columnsTable, column);
                    while (rs2.next()) {
                        // get the column just for the sake of getting it.
                        columnsColumn = rs2.getString(4);
                    }
                }

                // You can't get here without the test working.
                assertCondition(true, "columnsColumn = "+columnsColumn);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

    // to extract ALL types information
    public void Var032()	// @C1
    {
      StringBuffer sb = new StringBuffer();

	try {
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "%", null);
	    boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;


                String table = rs.getString ("TABLE_SCHEM") + "." + rs.getString ("TABLE_NAME");
                String type = rs.getString ("TABLE_TYPE");

		if( !type.equals("TABLE") && !type.equals("SYSTEM TABLE") &&!type.equals("VIEW") &&!type.equals("ALIAS") && !type.equals("MATERIALIZED QUERY TABLE")){
                    System.out.println("type = "+type+" and table="+table);
		    success = false;
		}

	    }

            rs.close ();

	    // System.out.println("ALL: rows = "+rows);

	    assertCondition(rows != 0 && success );
	    allRows = rows;

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
	}
    }

    // to extract TABLE type information only
    public void Var033()		// @C1
    {
      StringBuffer sb = new StringBuffer();

	try {
	    String[] types = {"TABLE"};
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "%", types);
	    boolean success = true;
            int rows = 0;

            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;


                String table = rs.getString ("TABLE_SCHEM") + "." + rs.getString ("TABLE_NAME");
                String type = rs.getString ("TABLE_TYPE");

		if( !type.equals("TABLE") ){
		    System.out.println("type = "+type+" sb TABLE table="+table);
		    success = false;
		    break;
		}

	    }
	    // System.out.println("TABLE only: rows = "+rows);
	    assertCondition(rows != 0 && success);
	    allRows -= rows;

	}catch(Exception e){
	    failed(e, "Unexpected Exception");
	}
    }

    // to extract SYSTEM TABLE type information only
    public void Var034()		// @C1
    {
      StringBuffer sb = new StringBuffer();

      String schema = JDDMDTest.SCHEMAS_PERCENT;
	if(getDriver() == JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Native driver variation only");      //System tables are represented as views
            return;
        }
	if (isSysibmMetadata()) {
	    schema = "SYSIBM";
	}
        try {
	    String[] types = {"SYSTEM TABLE"};
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           schema, "%", types);
	    boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;


                String table = rs.getString ("TABLE_SCHEM") + "." + rs.getString ("TABLE_NAME");
                String type = rs.getString ("TABLE_TYPE");

		if( !type.equals("SYSTEM TABLE") ){
                    System.out.println("Non system table found "+table);
		    success = false;
		    break;
		}
	    }
	    // System.out.println("SYSTEM TABLE: rows = "+rows);
	    assertCondition(rows != 0 && success, "rows="+rows+" > 0 success="+success);
	    allRows -= rows;

	}catch(Exception e){
	    failed(e, "Unexpected Exception");
	}
    }

    // to extract VIEW type information only
    public void Var035()	// @C1
    {
      StringBuffer sb = new StringBuffer();

	try {
	    String[] types = {"VIEW"};
            ResultSet rs = dmd_.getTables (connectionCatalog_,
                                           JDDMDTest.SCHEMAS_PERCENT, "%", types);
	    boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;


                String table = rs.getString ("TABLE_SCHEM") + "." + rs.getString ("TABLE_NAME");
                String type = rs.getString ("TABLE_TYPE");

		if( !type.equals("VIEW") ){
		    if (table.indexOf("LOGICAL") < 0) { 
			System.out.println("type="+type+" table="+table);
			success = false;
		    }
		}
	    }
	    // System.out.println("VIEW: rows = "+rows);
            if(getDriver() == JDTestDriver.DRIVER_TOOLBOX || getDriver() == JDTestDriver.DRIVER_JCC  || isSysibmMetadata() )
                assertCondition(rows != 0 && success, "success="+success+" rows="+rows);
            else
                assertCondition(rows != 0 && success && allRows == rows,
				"success="+success+" allRows="+allRows+" sb "+rows);
                              //this variation is dependent on previous ones - need to fix!
	}catch(Exception e){
	    failed(e, "Unexpected Exception");
	}
    }

/**
getTables() - Specify a 1 element array that matches a type
exactly.  All matching tables should be returned.
This testcase is like Var025, but checks columns 6 - 10 for JDBC 3.0
**/
    boolean checkColumnForNull(ResultSet rs, int column, boolean success) throws Exception {

	String s = rs.getString(column);
	if (s != null) {
	    System.out.println("Column "+column+" is not null but "+s);
	    success = false;
	}

	return success;
    }


    boolean checkColumnForNull(ResultSet rs, String column, boolean success) throws Exception {

	String s = rs.getString(column);
	if (s != null) {
	    System.out.println("Column "+column+" is not null but "+s);
	    success = false;
	}

	return success;
    }


    boolean checkColumnForNull2(ResultSet rs, int column, boolean success) throws Exception {

	String s = rs.getString(column);
	if (s != null) {
	    System.out.println("Column "+column+" is not null but "+s);
	    success = false;
	} else {
	    if ( ! rs.wasNull()) {
		System.out.println("wasNull for column "+column+" returned false instead of true");
		success = false;
	    }
	}

	return success;
    }


    boolean checkColumnForNull2(ResultSet rs, String column, boolean success) throws Exception {

	String s = rs.getString(column);
	if (s != null) {
	    System.out.println("Column "+column+" is not null but "+s);
	    success = false;
	} else {
	    if ( ! rs.wasNull()) {
		System.out.println("wasNull for column "+column+" returned false instead of true");
		success = false;
	    }
	}

	return success;
    }



    public void Var036()
    {
	if (checkJdbc30 ()) {
	    try {
		String[] types = { "VIEW"};
		ResultSet rs = dmd_.getTables (connectionCatalog_,
					       JDDMDTest.SCHEMAS_PERCENT, "%", types);

	        // Check for the tables.
		// boolean check1 = false;
		// boolean check2 = false;
		boolean success = true;
		int rows = 0;
		while (rs.next ()) {
		    ++rows;

		    if (rs.getString("TABLE_NAME").indexOf("SYS") == 0) {
			rows--;
		    } else {

			success = checkColumnForNull(rs, 6, success);
			success = checkColumnForNull(rs, 7, success);
			success = checkColumnForNull(rs, 8, success);
			success = checkColumnForNull(rs, 9, success);
			success = checkColumnForNull(rs, 10, success);
		    }

		}

		rs.close ();
		assertCondition (success
				, "Failed -- added by native 08/03/05 success="+success+" +rows="+rows);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception -- added by native 08/03/05");
	    }
	}
    }

/**
getTables() - check columns 6-10 using the JDBC 3.0 names.
**/

    public void Var037()
    {
	if (checkJdbc30 ()) {
	    try {
		String[] types = { "VIEW"};
		ResultSet rs = dmd_.getTables (connectionCatalog_,
					       JDDMDTest.SCHEMAS_PERCENT, "%", types);

	        // Check for the tables.
		// boolean check1 = false;
		// boolean check2 = false;
		boolean success = true;
		int rows = 0;
		while (rs.next ()) {
		    ++rows;
		    success = checkColumnForNull(rs, "TYPE_CAT", success);
		    success = checkColumnForNull(rs, "TYPE_SCHEM", success);
		    success = checkColumnForNull(rs, "TYPE_NAME", success);
		    if (JDTestDriver.isLUW()) {
			System.out.println("Warning:  LUW has bug and accesses SELF_REF_COL_NAME");
			success = checkColumnForNull(rs, "SELF_REF_COL_NAME", success);
		    } else {
			success = checkColumnForNull(rs, "SELF_REFERENCING_COL_NAME", success);
		    }
		    success = checkColumnForNull(rs, "REF_GENERATION", success);

		    if (rs.getString("TABLE_NAME").indexOf("SYS") >= 0) {
			rows--;
		    }

		}

		rs.close ();
		assertCondition (success
				, "Failed -- added by native 08/03/05 success="+success+" rows = "+rows);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception -- added by native 08/03/05");
	    }
	}
    }


/**
 retrieve the result set metadata used by the next tests.
 Since the tests are only applicable for V5R3, return null
 and set notApplcable if running on a previous release.
**/

ResultSetMetaData getResultSetMetaData() {
  ResultSetMetaData rsmd;
  try {
    String[] types = { "VIEW" };
    rsForMD = dmd_.getTables(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT, "%", types);
    rsmd = rsForMD.getMetaData();

    return rsmd;

  } catch (Exception e) {
    failed(e, "Unexpected exception -- added by native 08/03/05");
    return null;
  }

}

/**
 * getTables() - check the metadata for the RS -- getColumnCount
 **/

    public void Var038()
    {
	ResultSetMetaData rsmd = getResultSetMetaData();
	if (rsmd != null) {
	    try {
		int columnCount = rsmd.getColumnCount();
		int expectedCount;

		//
	        // The number of columns should depend on the JDK level
	        //
		if (getJdbcLevel() < 3) {
		    expectedCount = 5;
		} else {
		    expectedCount = 10;
		}
		assertCondition(columnCount == expectedCount, "columnCount ="+columnCount+" sb "+expectedCount+"  -- added by native 08/03/05");
	    } catch (Exception e) {
		failed(e, "Unexpected exception -- added by native 08/03/05");
	    }

	}
    }

/**
getTables() - check the metadata for the RS -- is CaseSensitive
**/
    public void Var039()
    {
	boolean expected13[] = {false,true,true,true,true,true};
	boolean expected14[] = {false,true,true,true,true,true,true,true,true,true,true};
  message.setLength(0);
	boolean success = true;
	ResultSetMetaData rsmd = getResultSetMetaData();
	if (rsmd != null) {
	    int i = 0;
	    try {
		int columnCount = rsmd.getColumnCount();
		boolean expected[];

		// int expectedCount;

		//
	        // The number of columns should depend on the JDK level
	        //
		if (getJdbcLevel() < 3) {
		    expected = expected13;
		} else {
		    expected = expected14;
		}
		for ( i = 1; i < columnCount; i++) {
		    boolean b = rsmd.isCaseSensitive(i);
		    if (b != expected[i]) {
			message.append("\n column "+i+" was "+b+" sb "+expected[i]);
			success=false;
		    }
		}
		assertCondition(success, message + "  -- added by native 08/03/05");
	    } catch (Exception e) {
		failed(e, "Unexpected exception i="+i+" -- added by native 08/03/05");
	    }

	}
    }
/**
getTables() - check the metadata for the RS -- isNullable
**/
   public void Var040()
    {
       int expected13[] = { 0,
        ResultSetMetaData.columnNullable,
        ResultSetMetaData.columnNullable,
        ResultSetMetaData.columnNoNulls,
        ResultSetMetaData.columnNoNulls,
        ResultSetMetaData.columnNoNulls};

	int expected14[] = { 0,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNoNulls,
	ResultSetMetaData.columnNoNulls,
	ResultSetMetaData.columnNoNulls,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable};


	int expectedLUW[] = { 0,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNoNulls,
	ResultSetMetaData.columnNoNulls,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable};

	int expectedSysibm[] = { 0,
	ResultSetMetaData.columnNoNulls,
	ResultSetMetaData.columnNoNulls,
	ResultSetMetaData.columnNoNulls,
	ResultSetMetaData.columnNoNulls,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable,
	ResultSetMetaData.columnNullable};



  message.setLength(0);
	boolean success = true;
	ResultSetMetaData rsmd = getResultSetMetaData();
	if (rsmd != null) {
	    int i = 0;
	    try {
		int[] expected;
		int columnCount = rsmd.getColumnCount();

		// int expectedCount;

		//
	        // The number of columns should depend on the JDK level
	        //
		if (getJdbcLevel() < 3) {
		    expected = expected13;
		    message.setLength(0);
		    message.append("expected13:");
		} else {
      message.setLength(0);
		    expected = expected14;
		    message.append("expected14:");
		}
		if (isSysibmMetadata()) {
      message.setLength(0);
		    expected = expectedSysibm;
		    message.append("expectedSysibm:");
		}
		if (JDTestDriver.isLUW()) {
      message.setLength(0);
		    expected = expectedLUW;
		    message.append("expectedLUW:");
		}
		for ( i = 1; i < columnCount; i++) {
		    int b = rsmd.isNullable(i);
		    if (b != expected[i]) {
			message.append("\n isNullable("+i+") returned "+b+" sb "+expected[i]);
			success=false;
		    }
		}
		assertCondition(success, message + "  -- added by native 08/03/05");
	    } catch (Exception e) {
		failed(e, "Unexpected exception i="+i+" -- added by native 08/03/05");
	    }

	}
    }

/**
getTables() - check the metadata for the RS -- isSigned
**/
   public void Var041()
    {
	boolean expected13[] = {false,false,false,false,false,false};
	boolean expected14[] = {false,false,false,false,false,false,false,false,false,false,false};
  message.setLength(0);
	boolean success = true;
	ResultSetMetaData rsmd = getResultSetMetaData();
	if (rsmd != null) {
	    int i = 0;
	    try {
		int columnCount = rsmd.getColumnCount();
		boolean expected[];

		// int expectedCount;

		//
	        // The number of columns should depend on the JDK level
	        //
		if (getJdbcLevel() < 3) {
		    expected = expected13;
		} else {
		    expected = expected14;
		}
		for ( i = 1; i < columnCount; i++) {
		    boolean b = rsmd.isSigned(i);
		    if (b != expected[i]) {
			message.append("\n column "+i+" was "+b+" sb "+expected[i]);
			success=false;
		    }
		}
		assertCondition(success, message + "  -- added by native 08/03/05");
	    } catch (Exception e) {
		failed(e, "Unexpected exception i="+i+" -- added by native 08/03/05");
	    }

	}
    }

   public static final int GETCOLUMNDISPLAYSIZE=1;
   public static final int GETCOLUMNLABEL  =2;
   public static final int GETCOLUMNNAME  =3;
   public static final int GETPRECISION  =4;
   public static final int GETSCALE  =5;
   public static final int GETCOLUMNTYPE  =6;
   public static final int GETCOLUMNTYPENAME  =7;
   public static final int GETCOLUMNCLASSNAME  =8;
   public static final int ISAUTOINCREMENT     = 9;
   public static final int ISSEARCHABLE = 10;
   public static final int ISCURRENCY = 11;
   public static final int GETCATALOGNAME = 12;
   public static final int ISREADONLY     = 13;
   public static final int ISWRITABLE     = 14;
   public static final int ISDEFINITELYWRITABLE = 15;

   public void checkExpectedMDStrings(String[] jdk13String, String[]jdk14String, String[] sysibmString, int whichMethod) {
       try {
         message.setLength(0);
	boolean success = true;
	ResultSetMetaData rsmd = getResultSetMetaData();
	if (rsmd != null) {
	    int i = 0;
	    try {
		int columnCount = rsmd.getColumnCount();
		String expected[];

		// int expectedCount;

		//
		// The number of columns should depend on the JDK level
		//
		if (getJdbcLevel() < 3) {
		    expected = jdk13String;
		    message.append("Using jdk13String:");
		} else {
		    expected = jdk14String;
		    message.append("Using jdk14String:");
		}
		if (isSysibmMetadata()) {
		    expected = sysibmString;
		    message.append("Using sysibmString:");
		}
		for ( i = 1; i <= columnCount; i++) {
		    String methodName = "";
		    String s = "";
		    switch(whichMethod) {
			case GETCOLUMNDISPLAYSIZE:
			    methodName="getColumnDisplaySize";
			    s = ""+ rsmd.getColumnDisplaySize(i);
			    break;
			case GETCOLUMNLABEL:
			    methodName="getColumnLabel";
			    s = ""+rsmd.getColumnLabel (i);
			    break;
			case GETCOLUMNNAME:
			    methodName="getColumnName";
			    s = ""+rsmd.getColumnName (i);
			    break;
			case GETPRECISION:
			    methodName="getPrecision";
			    s = ""+rsmd.getPrecision (i);
			    break;
			case GETSCALE:
			    methodName="getScale";
			    s = ""+rsmd.getScale (i);
			    break;
			case GETCOLUMNTYPE:
			    methodName="getColumnType";
			    s = ""+rsmd.getColumnType (i);
			    break;
			case GETCOLUMNTYPENAME:
			    methodName="getColumnTypeName";
			    s = ""+rsmd.getColumnTypeName (i);
			    break;
			case GETCOLUMNCLASSNAME:
			    methodName="getColumnClassName";
			    s = ""+rsmd.getColumnClassName (i);
			    break;

			case  ISAUTOINCREMENT     :
			    methodName="isAutoIncrement";
			    s=""+rsmd.isAutoIncrement(i);
			    break;
			case  ISSEARCHABLE :
			    methodName="isSearchable";
			    s=""+rsmd.isSearchable(i);
			    break;
			case  ISCURRENCY :
			    methodName="isCurrency";
			    s=""+rsmd.isCurrency(i);
			    break;
			case  GETCATALOGNAME :
			    methodName="getCatalogName";
			    s=""+rsmd.getCatalogName(i);
			    break;
			case  ISREADONLY     :
			    methodName="isReadOnly";
			    s=""+rsmd.isReadOnly(i);
			    break;
			case  ISWRITABLE     :
			    methodName="isWritable";
			    s=""+rsmd.isWritable(i);
			    break;
			case  ISDEFINITELYWRITABLE :
			    methodName="isDefinitelyWritable";
			    s=""+rsmd.isDefinitelyWritable(i);
			    break;


		    }


			if (! s.equals(expected[i])) {
			    message.append("\n "+methodName+"("+i+") was "+s+" sb "+expected[i]);
			    success=false;
			}
		}
		assertCondition(success, message + "  -- added by native 08/03/05");
	    } catch (Exception e) {
		failed(e, "Unexpected exception i="+i+" "+message+" -- added by native 08/03/05");
	    }

	}
       } catch (Exception e) {
	   failed(e, "Unexpected exception -- added by native 08/03/05");
       }

    }




/**
getTables() - check the metadata for the RS -- getColumnDisplaySize
**/
   public void Var042()
   {
       String expected13[] = {null, "128","128","128","128","254"};
       String expected14[] = {null, "128","128","128","128","254", "128","128","128","128","128"};
       String expectedSysibm[] = {null, "128","128","128","128","2000", "128","128","128","128","128"};
       /* SysIBM fixed 4/11/2021 */ 
       checkExpectedMDStrings(expected13, expected14, expectedSysibm, GETCOLUMNDISPLAYSIZE);
   }

/**
getTables() - check the metadata for the RS -- getColumnLabel
**/
   public void Var043()
    {
       String expected13[] = {null,  "TABLE_CAT",  "TABLE_SCHEM",  "TABLE_NAME",  "TABLE_TYPE",  "REMARKS"};
	String expected14[] = {null, "TABLE_CAT",  "TABLE_SCHEM",  "TABLE_NAME",  "TABLE_TYPE",  "REMARKS",  "TYPE_CAT",  "TYPE_SCHEM",  "TYPE_NAME",  "SELF_REFERENCING_COL_NAME",  "REF_GENERATION"};
	String expectedSysibm[] = {null, "TABLE_CAT",  "TABLE_SCHEM",  "TABLE_NAME",  "TABLE_TYPE",  "REMARKS",  "TYPE_CAT",  "TYPE_SCHEM",  "TYPE_NAME",  "SELF_REFERENCING_COL_NAME",  "REF_GENERATION"};
	String expected14LUW[] = {null, "TABLE_CAT",  "TABLE_SCHEM",  "TABLE_NAME",  "TABLE_TYPE",  "REMARKS",  "TYPE_CAT",  "TYPE_SCHEM",  "TYPE_NAME",  "SELF_REF_COL_NAME",  "REF_GENERATION"};

	if (JDTestDriver.isLUW()) {
	    System.out.println("Warning:  LUW has bug and accesses SELF_REF_COL_NAME");

	    checkExpectedMDStrings(expected13, expected14LUW, expected14LUW, GETCOLUMNLABEL);
	} else {
	    checkExpectedMDStrings(expected13, expected14, expectedSysibm, GETCOLUMNLABEL);
	}
   }


/**
getTables() - check the metadata for the RS -- getColumnName
**/
   public void Var044()
    {
       String expected13[] = {null,  "TABLE_CAT",  "TABLE_SCHEM",  "TABLE_NAME",  "TABLE_TYPE",  "REMARKS"};
       String expected14[] = {null, "TABLE_CAT",  "TABLE_SCHEM",  "TABLE_NAME",  "TABLE_TYPE",  "REMARKS",  "TYPE_CAT",  "TYPE_SCHEM",  "TYPE_NAME",  "SELF_REFERENCING_COL_NAME",  "REF_GENERATION"};
	if (JDTestDriver.isLUW()) {
	    System.out.println("Warning:  LUW has bug and accesses SELF_REF_COL_NAME");
	    String expected14LUW[] = {null, "TABLE_CAT",  "TABLE_SCHEM",  "TABLE_NAME",  "TABLE_TYPE",  "REMARKS",  "TYPE_CAT",  "TYPE_SCHEM",  "TYPE_NAME",  "SELF_REF_COL_NAME",  "REF_GENERATION"};
	    checkExpectedMDStrings(expected13, expected14LUW, expected14LUW, GETCOLUMNNAME);
	} else {
	    checkExpectedMDStrings(expected13, expected14, expected14, GETCOLUMNNAME);
	}
   }
/**
getTables() - check the metadata for the RS -- getPrecision
**/
   public void Var045()
    {
       String expected13[] = {null, "128","128","128","128","254"};
       String expected14[] = {null, "128","128","128","128","254", "128","128","128","128","128"};
       String expectedSysibm[] = {null, "128","128","128","24","2000", "128","128","128","128","128"};
       String expectedSysibmV7R3[] = {null, "128","128","128","128","2000", "128","128","128","128","128"};
       String expectedSysibmV7R3toolbox[] = {null, "128","128","128","128","2000", "128","128","128","128","128"};

       if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
           expectedSysibm = expectedSysibmV7R3toolbox;
       } else {
           expectedSysibm = expectedSysibmV7R3;
       }
       checkExpectedMDStrings(expected13, expected14, expectedSysibm, GETPRECISION);
   }
/**
getTables() - check the metadata for the RS -- getScale
**/
   public void Var046()
    {
       String expected13[] = {null, "0","0","0","0","0"};
	String expected14[] = {null, "0","0","0","0","0", "0","0","0","0","0"};
	checkExpectedMDStrings(expected13, expected14, expected14, GETSCALE);
   }
/**
getTables() - check the metadata for the RS -- getColumnType
**/
   public void Var047()
    {
       String expected13[] = {null, "12","12","12","12","12"};
	   String expected14[] = {null, "12","12","12","12","12", "12","12","12","12","12"};

           // 10/22/2009 -- size back to 2000 type back to varchar
	   String expected14V7R1[] = {null, "12","12","12","12","12", "12","12","12","12","12"}; //v7r1 longvarchar
	   String expected14V7R1toolbox[] = {null, "12","12","12","12","12", "12","12","12","12","12"}; //v7r1 longvarchar


	   if(true ){
	       if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE || getDriver() == JDTestDriver.DRIVER_TOOLBOX ) {
		   expected14 = expected14V7R1toolbox;
	       } else {
		   expected14 = expected14V7R1;
	       }
	   }
     String[] expectedSysibm = expected14; 

	   if (getJdbcLevel()>= 4) {
	     expectedSysibm[5]="-9"; 
	   }
	checkExpectedMDStrings(expected13, expected14, expectedSysibm, GETCOLUMNTYPE);
   }
/**
getTables() - check the metadata for the RS -- getColumnTypeName
**/
   public void Var048()
    {
       String expected13[] = {null, "VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR"};
	   String expected14[] = {null, "VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR", "VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR"};

	   // 10/22/2009 lp08ut23 -- type now NVARCHAR
	   String expected14V7R1[] = {null, "VARCHAR","VARCHAR","VARCHAR","VARCHAR","NVARCHAR", "VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR"};

	   String expected14V7R1toolbox[] = {null, "VARCHAR","VARCHAR","VARCHAR","VARCHAR","NVARCHAR", "VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR"};

	  if(true ){
	       if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE || getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		   expected14 = expected14V7R1toolbox;
	       } else {
		   expected14 = expected14V7R1;
	       }
      }

	checkExpectedMDStrings(expected13, expected14, expected14, GETCOLUMNTYPENAME);
   }
/**
getTables() - check the metadata for the RS -- getColumnClassName
**/
   public void Var049()
    {
       String expected13[] = {null, "java.lang.String","java.lang.String","java.lang.String","java.lang.String","java.lang.String"};
	String expected14[] = {null, "java.lang.String","java.lang.String","java.lang.String","java.lang.String","java.lang.String", "java.lang.String","java.lang.String","java.lang.String","java.lang.String","java.lang.String"};
	checkExpectedMDStrings(expected13, expected14, expected14, GETCOLUMNCLASSNAME);
   }

   public void Var050()
    {
	if (checkJdbc30 ()) {
	    try {
		String[] types = { "VIEW"};
		ResultSet rs = dmd_.getTables (connectionCatalog_,
					       JDDMDTest.SCHEMAS_PERCENT, "%", types);

	        // Check for the tables.
		// boolean check1 = false;
		// boolean check2 = false;
		boolean success = true;
		int rows = 0;
		String tableNames="";
		while (rs.next ()) {
		    ++rows;

		    success = checkColumnForNull2(rs, 6, success);
		    success = checkColumnForNull2(rs, 7, success);
		    success = checkColumnForNull2(rs, 8, success);
		    success = checkColumnForNull2(rs, 9, success);
		    success = checkColumnForNull2(rs, 10, success);

		    if (rs.getString("TABLE_NAME").indexOf("SYS") == 0) {
			rows--;
		    } else {
			tableNames+=" "+rs.getString("TABLE_NAME");
		    }

		}

		rs.close ();
		assertCondition (success
				 , "Failed -- added by native 08/03/05 success="+success+" sb 2 tableNames="+tableNames+" rows = "+rows );
	    } catch (Exception e) {
		failed (e, "Unexpected Exception -- added by native 08/03/05");
	    }
	}
    }

/**
getTables() - check columns 6-10 using the JDBC 3.0 names.
**/

    public void Var051()
    {
	if (checkJdbc30 ()) {
	    try {
		String[] types = { "VIEW"};
		ResultSet rs = dmd_.getTables (connectionCatalog_,
					       JDDMDTest.SCHEMAS_PERCENT, "%", types);

	        // Check for the tables.
		// boolean check1 = false;
		// boolean check2 = false;
		boolean success = true;
		int rows = 0;
		String tableNames="";
		while (rs.next ()) {
		    ++rows;
		    success = checkColumnForNull2(rs, "TYPE_CAT", success);
		    success = checkColumnForNull2(rs, "TYPE_SCHEM", success);
		    success = checkColumnForNull2(rs, "TYPE_NAME", success);
		    if (JDTestDriver.isLUW()) {
			System.out.println("Warning:  LUW has bug and accesses SELF_REF_COL_NAME");
			success = checkColumnForNull2(rs, "SELF_REF_COL_NAME", success);
		    } else {
			success = checkColumnForNull2(rs, "SELF_REFERENCING_COL_NAME", success);
		    }
		    success = checkColumnForNull2(rs, "REF_GENERATION", success);

		    if (rs.getString("TABLE_NAME").indexOf("SYS") == 0) {
			rows--;
		    } else {
			tableNames+=" "+rs.getString("TABLE_NAME");
		    }

		}

		rs.close ();
		assertCondition (success
				 , "Failed -- added by native 08/03/05 success = "+success+"   tableNames="+tableNames+" rows="+rows);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception -- added by native 08/03/05");
	    }
	}
    }


/**
 * utility functions for the next variations
 */

    public static final int ALIAS=1;
    public static final int TABLE=2;
    public static final int SYSTEMTABLE=4;
    public static final int VIEW=8;
    public static final int MQT=16;
    public static final int SYNONYM=32;
    public static final int ALL=63;
    public static final int MAXTYPECOUNT=6;


    public JDDMDGetTablesExpectedRow expectedSysibmRows[];

    public JDDMDGetTablesExpectedRow expectedSysibmRowsLUW[] = {
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSATTRIBUTES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSBUFFERPOOLNODES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSBUFFERPOOLS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCHECKS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCODEPROPERTIES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLCHECKS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLDIST","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLGROUPDIST","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLGROUPDISTCOUNTS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLGROUPS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLGROUPSCOLS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLOPTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLPROPERTIES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLUMNS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOLUSE","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCOMMENTS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSCONSTDEP","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSDATATYPES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSDBAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSDEPENDENCIES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSEVENTMONITORS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSEVENTS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSEVENTTABLES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSFUNCMAPOPTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSFUNCMAPPARMOPTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSFUNCMAPPINGS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSHIERARCHIES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSINDEXAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSINDEXCOLUSE","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSINDEXES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSINDEXEXPLOITRULES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSINDEXEXTENSIONMETHODS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSINDEXEXTENSIONPARMS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSINDEXEXTENSIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSINDEXOPTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJARCONTENTS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJAROBJECTS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSKEYCOLUSE","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSLIBRARIES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSLIBRARYAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSLIBRARYBINDFILES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSLIBRARYVERSIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSNAMEMAPPINGS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSNODEGROUPDEF","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSNODEGROUPS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSPARTITIONMAPS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSPASSTHRUAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSPLAN","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSPLANAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSPLANDEP","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSPREDICATESPECS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSPROCOPTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSPROCPARMOPTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSRELS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSROUTINEAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSROUTINEPARMS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSROUTINEPROPERTIESJAVA","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSROUTINES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSSCHEMAAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSSCHEMATA","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSSECTION","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSSEQUENCEAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSSEQUENCES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSSERVEROPTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSSERVERS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSSTMT","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTABAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTABCONST","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTABLES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTABLESPACES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTABOPTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTBSPACEAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTRANSFORMS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTRIGGERS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTYPEMAPPINGS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSUSERAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSUSEROPTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSVERSIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSVIEWDEP","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSVIEWS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSWRAPOPTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSWRAPPERS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLOBJECTAUTH","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLOBJECTAUTHPERF","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLOBJECTPROPERTIES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLOBJECTRELDEP","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLOBJECTS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLOBJECTXMLDEP","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLPHYSICALCOLLECTIONS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLQUERIES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLRELATIONSHIPS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLRSPROPERTIES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSXMLSTATS","SYSTEM TABLE"),

	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHECK_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REF_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REFERENTIAL_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLPRIVILEGES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLUMNS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLFOREIGNKEYS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPRIMARYKEYS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURECOLS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSCHEMAS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSPECIALCOLUMNS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSTATISTICS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLEPRIVILEGES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLETYPES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTYPEINFO","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLUDTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSDUMMY1","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSFUNCPARMS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSFUNCTIONS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSPROCEDURES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSPROCPARMS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLE_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","UDT_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","USER_DEFINED_TYPES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","VIEWS","VIEW"),

    };

    public JDDMDGetTablesExpectedRow expectedSysibmRows55[] = {
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CATALOG_NAME","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHECK_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","INFORMATION_SCHEMA_CATALOG_NAME","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REF_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REFERENTIAL_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQL_LANGUAGES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLPRIVILEGES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLFOREIGNKEYS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPRIMARYKEYS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURECOLS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSCHEMAS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSPECIALCOLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSTATISTICS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLEPRIVILEGES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLETYPES","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SQLTYPEINFO","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLUDTS","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSCHARSETS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSDUMMY1","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJARCONTENTS","VIEW"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJAROBJECTS","VIEW"),
        // SYSPRIVILEGES deprecated 5/2012
	// new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSPRIVILEGES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSTABLETYPES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLE_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","UDT_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","USER_DEFINED_TYPES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","VIEWS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","AUTHORIZATIONS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSPARTITIONSTAT","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSTABLESTAT","VIEW"),

	// New rows added 2/20/2013 -- option = true since not on fowgai2 but on rchaptf2
	new JDDMDGetTablesExpectedRow(VIEW,"SYSIBM","CHECKVIEW","VIEW", true),
	new JDDMDGetTablesExpectedRow(VIEW,"SYSIBM","CHECKVIEW2","VIEW", true ),



    };




    public JDDMDGetTablesExpectedRow expectedSysibmRows71[] = {
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CATALOG_NAME","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHECK_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","INFORMATION_SCHEMA_CATALOG_NAME","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REF_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REFERENTIAL_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQL_LANGUAGES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLPRIVILEGES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLFOREIGNKEYS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPRIMARYKEYS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURECOLS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSCHEMAS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSPECIALCOLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSTATISTICS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLEPRIVILEGES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLETYPES","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SQLTYPEINFO","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLUDTS","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSCHARSETS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSDUMMY1","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJARCONTENTS","VIEW"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJAROBJECTS","VIEW"),
        // SYSPRIVILEGES deprecated 5/2012
        // new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSPRIVILEGES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSTABLETYPES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLE_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","UDT_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","USER_DEFINED_TYPES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","VIEWS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","AUTHORIZATIONS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSPARTITIONSTAT","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSTABLESTAT","VIEW"),
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","MQPOLICY_TABLE","TABLE"),
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","MQSERVICE_TABLE","TABLE"),

    };

   public JDDMDGetTablesExpectedRow expectedSysibmRows72[] = {
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CATALOG_NAME","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHECK_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","INFORMATION_SCHEMA_CATALOG_NAME","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REF_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REFERENTIAL_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA_S","VIEW"),

	/* Added Sequences 10/11/2013 */ 
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SEQUENCES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SEQUENCES_S","VIEW"),


        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQL_LANGUAGES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLPRIVILEGES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLFOREIGNKEYS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPRIMARYKEYS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURECOLS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSCHEMAS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSPECIALCOLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSTATISTICS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLEPRIVILEGES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLETYPES","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SQLTYPEINFO","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLUDTS","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSCHARSETS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSDUMMY1","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJARCONTENTS","VIEW"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJAROBJECTS","VIEW"),
        // SYSPRIVILEGES deprecated 5/2012
        // new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSPRIVILEGES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSTABLETYPES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLE_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","UDT_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","USER_DEFINED_TYPES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","VIEWS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","AUTHORIZATIONS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSPARTITIONSTAT","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SYSTABLESTAT","VIEW"),
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","MQPOLICY_TABLE","TABLE"),
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","MQSERVICE_TABLE","TABLE"),

    };



    public JDDMDGetTablesExpectedRow expectedSysibmRows54[] = {
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","AUTHORIZATIONS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CATALOG_NAME","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHECK_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","INFORMATION_SCHEMA_CATALOG_NAME","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REF_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REFERENTIAL_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQL_LANGUAGES_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLPRIVILEGES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLUMNS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLFOREIGNKEYS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPRIMARYKEYS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURECOLS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSCHEMAS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSPECIALCOLUMNS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSTATISTICS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLEPRIVILEGES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLETYPES","VIEW"),
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SQLTYPEINFO","TABLE"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLUDTS","VIEW"),
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSCHARSETS","TABLE"),
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSDUMMY1","TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJARCONTENTS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJAROBJECTS","SYSTEM TABLE"),
        // SYSPRIVILEGES deprecated 5/2012
	// new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSPRIVILEGES","TABLE"),
/* Not on lp03ut5, rchasbb1 V5R4 as of 7/18/2007 */
/*
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTABLES","SYSTEM TABLE"),
*/
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSTABLETYPES","TABLE"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLE_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","UDT_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","USER_DEFINED_TYPES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","VIEWS","VIEW"),

    };



    public JDDMDGetTablesExpectedRow expectedSysibmRows5440[] = {
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","AUTHORIZATIONS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CATALOG_NAME","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHECK_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","INFORMATION_SCHEMA_CATALOG_NAME","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REF_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REFERENTIAL_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQL_LANGUAGES_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLPRIVILEGES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLUMNS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLFOREIGNKEYS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPRIMARYKEYS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURECOLS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSCHEMAS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSPECIALCOLUMNS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSTATISTICS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLEPRIVILEGES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLETYPES","VIEW"),
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SQLTYPEINFO","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLUDTS","VIEW"),
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSCHARSETS","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSDUMMY1","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJARCONTENTS","VIEW"),
	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJAROBJECTS","VIEW"),
        // SYSPRIVILEGES deprecated 5/2012
// 	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSPRIVILEGES","SYSTEM TABLE"),
/* 	new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSTABLES","SYSTEM TABLE"), */
	new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSTABLETYPES","SYSTEM TABLE"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLE_CONSTRAINTS","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","UDT_S","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","USER_DEFINED_TYPES","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","VIEWS","VIEW"),

    };

    public JDDMDGetTablesExpectedRow expectedSysibmRows53[] = {

        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CATALOG_NAME","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHECK_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","INFORMATION_SCHEMA_CATALOG_NAME","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REF_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REFERENTIAL_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQL_LANGUAGES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLPRIVILEGES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLFOREIGNKEYS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPRIMARYKEYS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURECOLS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSCHEMAS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSPECIALCOLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSTATISTICS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLEPRIVILEGES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLETYPES","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SQLTYPEINFO","TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLUDTS","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSCHARSETS","TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSDUMMY1","TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJARCONTENTS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJAROBJECTS","SYSTEM TABLE"),
        // SYSPRIVILEGES deprecated 5/2012
//         new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSPRIVILEGES","TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSTABLETYPES","TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLE_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","UDT_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","USER_DEFINED_TYPES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","VIEWS","VIEW"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSSEQUENCES","SYSTEM TABLE"),

    };

    public JDDMDGetTablesExpectedRow expectedSysibmRows52[] = {

        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CATALOG_NAME","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHARACTER_SETS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","CHECK_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","COLUMNS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","INFORMATION_SCHEMA_CATALOG_NAME","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","PARAMETERS_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REF_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","REFERENTIAL_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","ROUTINES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SCHEMATA_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQL_LANGUAGES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLPRIVILEGES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLCOLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLFOREIGNKEYS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPRIMARYKEYS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURECOLS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLPROCEDURES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSCHEMAS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSPECIALCOLUMNS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLSTATISTICS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLEPRIVILEGES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLTABLETYPES","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SQLTYPEINFO","BASE TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","SQLUDTS","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSCHARSETS","BASE TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSDUMMY1","BASE TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJARCONTENTS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE, "SYSIBM","SYSJAROBJECTS","SYSTEM TABLE"),
        // SYSPRIVILEGES deprecated 5/2012
//        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSPRIVILEGES","BASE TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE, "SYSIBM","SYSTABLETYPES","BASE TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLE_CONSTRAINTS","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","TABLES_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","UDT_S","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","USER_DEFINED_TYPES","VIEW"),
        new JDDMDGetTablesExpectedRow(VIEW, "SYSIBM","VIEWS","VIEW"),

    };



    public JDDMDGetTablesExpectedRow expectedRows[];

    public JDDMDGetTablesExpectedRow expectedRows53[] = {
	new JDDMDGetTablesExpectedRow(ALIAS | SYNONYM, "JDDMDCOL","ALIAS1","ALIAS"),
        new JDDMDGetTablesExpectedRow(VIEW , "JDDMDCOL","LOGICAL1","VIEW"),
        new JDDMDGetTablesExpectedRow(MQT , "JDDMDCOL","MQT1","MATERIALIZED QUERY TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE , "JDDMDCOL","SRCPF","TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCHKCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCOLUMNS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCSTCOL","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCSTDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSINDEXES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSKEYCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSKEYS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSPACKAGE","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSREFCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTABLEDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTABLES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGCOL","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGGERS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGUPD","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSVIEWDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSVIEWS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE , "JDDMDCOL","TABLE1","TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW , "JDDMDCOL","VIEW1","VIEW")
    };

    public JDDMDGetTablesExpectedRow expectedRows71[] = {
	new JDDMDGetTablesExpectedRow(ALIAS | SYNONYM, "JDDMDCOL","ALIAS1","ALIAS"),
        new JDDMDGetTablesExpectedRow(VIEW , "JDDMDCOL","LOGICAL1","VIEW"),
        new JDDMDGetTablesExpectedRow(MQT , "JDDMDCOL","MQT1","MATERIALIZED QUERY TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE , "JDDMDCOL","SRCPF","TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCHKCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCOLUMNS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCSTCOL","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCSTDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSINDEXES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSKEYCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSKEYS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSPACKAGE","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSREFCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTABLEDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTABLES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGCOL","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGGERS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGUPD","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSVIEWDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSVIEWS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE , "JDDMDCOL","TABLE1","TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW , "JDDMDCOL","VIEW1","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW,  "JDDMDCOL","SYSFIELDS","VIEW")
    };

   public JDDMDGetTablesExpectedRow expectedRows72[] = {
	new JDDMDGetTablesExpectedRow(ALIAS | SYNONYM, "JDDMDCOL","ALIAS1","ALIAS"),
        new JDDMDGetTablesExpectedRow(VIEW , "JDDMDCOL","LOGICAL1","VIEW"),
        new JDDMDGetTablesExpectedRow(MQT , "JDDMDCOL","MQT1","MATERIALIZED QUERY TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE , "JDDMDCOL","SRCPF","TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCHKCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCOLUMNS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCSTCOL","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCSTDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSINDEXES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSKEYCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSKEYS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSPACKAGE","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSREFCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTABLEDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTABLES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGCOL","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGGERS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGUPD","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSVIEWDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSVIEWS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE , "JDDMDCOL","TABLE1","TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW , "JDDMDCOL","VIEW1","VIEW"),
	new JDDMDGetTablesExpectedRow(VIEW,  "JDDMDCOL","SYSFIELDS","VIEW")
    };


    public JDDMDGetTablesExpectedRow expectedRowsLUW[] = {
	new JDDMDGetTablesExpectedRow(ALIAS | SYNONYM, "JDDMDGTA","ALIAS1","ALIAS"),
        new JDDMDGetTablesExpectedRow(MQT , "JDDMDGTA","MQT1","MATERIALIZED QUERY TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE , "JDDMDGTA","TABLE1","TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW , "JDDMDGTA","VIEW1","VIEW")
    };


    public JDDMDGetTablesExpectedRow expectedRowsToolbox53[] = {
                new JDDMDGetTablesExpectedRow(ALIAS | SYNONYM, "JDDMDCOL","ALIAS1","ALIAS"),
                new JDDMDGetTablesExpectedRow(VIEW , "JDDMDCOL","LOGICAL1","TABLE"),
                new JDDMDGetTablesExpectedRow(MQT , "JDDMDCOL","MQT1","MATERIALIZED QUERY TABLE"),
                new JDDMDGetTablesExpectedRow(TABLE , "JDDMDCOL","SRCPF","TABLE"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCHKCST","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCOLUMNS","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCST","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCSTCOL","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCSTDEP","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSINDEXES","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSKEYCST","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSKEYS","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSPACKAGE","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSREFCST","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTABLEDEP","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTABLES","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGCOL","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGDEP","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGGERS","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGUPD","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSVIEWDEP","VIEW"),
                new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSVIEWS","VIEW"),
                new JDDMDGetTablesExpectedRow(TABLE , "JDDMDCOL","TABLE1","TABLE"),
                new JDDMDGetTablesExpectedRow(VIEW , "JDDMDCOL","VIEW1","VIEW")
            };
    public JDDMDGetTablesExpectedRow expectedRows52[] = {
	new JDDMDGetTablesExpectedRow(ALIAS | SYNONYM, "JDDMDCOL","ALIAS1","ALIAS"),
        new JDDMDGetTablesExpectedRow(VIEW , "JDDMDCOL","LOGICAL1","VIEW"),
        new JDDMDGetTablesExpectedRow(TABLE , "JDDMDCOL","SRCPF","BASE TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCHKCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCOLUMNS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCSTCOL","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSCSTDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSINDEXES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSKEYCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSKEYS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSPACKAGE","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSREFCST","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTABLES","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGCOL","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGGERS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSTRIGUPD","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSVIEWDEP","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(SYSTEMTABLE , "JDDMDCOL","SYSVIEWS","SYSTEM TABLE"),
        new JDDMDGetTablesExpectedRow(TABLE , "JDDMDCOL","TABLE1","BASE TABLE"),
        new JDDMDGetTablesExpectedRow(VIEW , "JDDMDCOL","VIEW1","VIEW")
    };





    public boolean checkSysibmResultSet(ResultSet rs) throws Exception  {
	String okRows = "";
	boolean extraRow = false;
	boolean ok = true;
	boolean found[] = new boolean[expectedSysibmRows.length];
	for (int i = 0; i < found.length; i++) {
	    found[i] = false;
	}

	while (rs.next()) {
	    String schema = rs.getString(2);
	    String name=rs.getString(3);
	    String typeName=rs.getString(4);
	    boolean rowFound = false;
	    for (int i = 0; !rowFound && i < found.length; i++) {
		if (schema.equals(expectedSysibmRows[i].schema) &&
		    name.equals(expectedSysibmRows[i].name) &&
		    typeName.equals(expectedSysibmRows[i].typeName)) {
		    found[i] = true;
		    rowFound=true;
		    okRows+="\nOK Row: "+schema+","+name+","+typeName;
		}
	    }
	    if (!rowFound) {
		// Ignore SQLFUNCTION stuff
		// Ignore depricated SYSPRIVILEGES
		if (name.indexOf("SQLFUNCTION") >= 0  ||
		    name.indexOf("SYSDUMMY2") >= 0  ||
		    name.indexOf("SYSPARTITION") >= 0  ||
		    name.indexOf("SQLCOLUMNS2") >= 0  ||
		    name.indexOf("MYALIAS") >= 0  ||
		    name.indexOf("SYSPRIVILEGES") >= 0  ||
		    name.indexOf("SYSDUM0001") >= 0 ) {
		    // ignore this.
		} else {
		    message.append("\nRow not expected in checkSysibmResultSet: "+schema+","+name+","+typeName);
		    extraRow =true;
		}
	    }

	}

	rs.close();

	//
	// Now check
	//
	for (int i = 0; i < expectedSysibmRows.length; i++) {
	    if (found[i] == false) {
		if (expectedSysibmRows[i].optional) {
		    message.append("\nWarning:  Optional row not found in checkSysibmResultSet: "+expectedSysibmRows[i].schema+","+expectedSysibmRows[i].name+","+expectedSysibmRows[i].typeName);
		} else { 
		    message.append("\nError: Row not found in checkSysibmResultSet: "+expectedSysibmRows[i].schema+","+expectedSysibmRows[i].name+","+expectedSysibmRows[i].typeName);
		    ok = false;
		}
	    }
	}

	message.append(okRows);
	if (!ok) {
	    message.append( "\nRows set from "+setupSysibmVersion);
	}

	return ok && (!extraRow);
    }




    public boolean checkResultSet(ResultSet rs, int expectedTypes) throws Exception  {
	String okRows = "";
	boolean extraRow = false;
	boolean ok = true;
	boolean found[] = new boolean[expectedRows.length];
	for (int i = 0; i < found.length; i++) {
	    found[i] = false;
	}

	while (rs.next()) {
	    String schema = rs.getString(2);
	    String name=rs.getString(3);
	    String typeName=rs.getString(4);
	    boolean rowFound = false;
	    for (int i = 0; !rowFound && i < found.length; i++) {
		if (expectedRows[i].schema.equals("JDDMDCOL")) {
		    expectedRows[i].schema = jddmdCol_; 
		} 
		if (schema.equals(expectedRows[i].schema) &&
		    name.equals(expectedRows[i].name) &&
		    typeName.equals(expectedRows[i].typeName) &&
                    ((expectedTypes & expectedRows[i].typeFlag) != 0)) {
		    found[i] = true;
		    rowFound=true;
		    okRows+="\nOK Row: "+schema+","+name+","+typeName;
		}
	    }
	    if (!rowFound) {
		message.append("\nRow not expected in checkResultSet: "+schema+","+name+","+typeName);
		extraRow =true;
	    }

	}

	rs.close();

	//
	// Now check
	//
	for (int i = 0; i < expectedRows.length; i++) {
	    if ((expectedRows[i].typeFlag & expectedTypes) > 0) {
		if (found[i] == false) {
		    if (expectedRows[i].optional) {
			message.append("\nWarning:  Optional row not found in checkSysibmResultSet: "+expectedRows[i].schema+","+expectedRows[i].name+","+expectedRows[i].typeName);
		    } else { 

			message.append("\nRow not found in checkResultSet : "+expectedRows[i].schema+","+expectedRows[i].name+","+expectedRows[i].typeName);
			ok = false;
		    }
		}
	    }
	}

	message.append(okRows);

	if (!ok || extraRow) {
	    message.append("\nexpectedRows set from "+setupVersion);
	}
	return ok && (!extraRow);
    }

    public String setupExpected() {
        if (!setupExpectedDone) {
            expectedSysibmRows = expectedSysibmRows54;
            setupSysibmVersion =      "expectedSysibmRows54";
            if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
                expectedSysibmRows = expectedSysibmRows72;
                setupSysibmVersion =      "expectedSysibmRows72";

            } else {
                expectedSysibmRows = expectedSysibmRows71;
                setupSysibmVersion =      "expectedSysibmRows71";

            }

                        if( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()){
                            for (int i = 0; i < expectedSysibmRows.length; i++) {
                                if (expectedSysibmRows[i].name.equals("SYSJARCONTENTS") ||
                                        expectedSysibmRows[i].name.equals("SYSJAROBJECTS")) {
                                    expectedSysibmRows[i].typeName="VIEW";
                                    expectedSysibmRows[i].typeFlag = VIEW;
                                }
                            }
                        }

        }

	return setupVersion+" "+setupSysibmVersion;
    }

    public void setupObjects(Connection conn) throws Exception  {
	setupExpected();
	if (!setupDone) {
	    expectedSysibmRows = expectedSysibmRows54;
            setupSysibmVersion =      "expectedSysibmRows54";
	    if( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()){
          expectedRows = expectedRowsToolbox53;
  setupVersion ="expectedRowsToolbox53";
      }else  {
          if (! is400 ) {
              expectedRows = expectedRowsLUW;
setupVersion ="expectedRowsLUW";
              expectedSysibmRows =  expectedSysibmRowsLUW;
setupSysibmVersion = "expectedSysibmRowsLUW";
          } else {
expectedRows = expectedRows71;
setupVersion ="expectedRows71";

          }
      }

	    if (isSysibmMetadata()) {
	        // We must change all SYSTEM_TABLES to views in the regular tables
	        for (int i = 0; i < expectedRows.length; i++) {
	            JDDMDGetTablesExpectedRow x = expectedRows[i];
	            if (x.typeFlag==SYSTEMTABLE) {
	                x.typeFlag=VIEW;
	                x.typeName="VIEW";
	            }
	        }
	    }

	    Statement stmt=null;

	    //
            // Here are the statements to execute
            //  I   -- errors can be ignored
            //  3   -- V5R3 and later test
            //  Q   -- ignore errors if not on iSeries
            //  X   -- otherwise
	    String statements[] = {
/* don't drop -- we already did setup before starting the testcase
   In V7R3, for some reason, doing this causes the SYSCOLUMNS view to be missing 
                "I", "CALL QSYS.QCMDEXC('CHGJOB INQMSGRPY(*SYSRPYL)          ',   0000000030.00000)",
		"I", "DROP   SCHEMA JDDMDCOL",
		"I", "CREATE SCHEMA JDDMDCOL",
*/ 
		"I", "DROP ALIAS JDDMDCOL.ALIAS1",
		"I", "DROP VIEW JDDMDCOL.VIEW1",
		"I", "DROP TABLE JDDMDCOL.TABLE1",
		"X", "CREATE TABLE JDDMDCOL.TABLE1(A INT)",
		"X", "CREATE VIEW  JDDMDCOL.VIEW1 AS SELECT A FROM JDDMDCOL.TABLE1",
		"X", "CREATE ALIAS JDDMDCOL.ALIAS1 FOR JDDMDCOL.TABLE1",
/* # Create a physical file */
		"I", "CALL QSYS.QCMDEXC('DLTF     JDDMDCOL/SRCPF          ',   0000000030.00000)",
		"Q", "CALL QSYS.QCMDEXC('CRTSRCPF JDDMDCOL/SRCPF          ',   0000000030.00000)",
		"Q", "CALL QSYS.QCMDEXC('ADDPFM FILE(JDDMDCOL/SRCPF) MBR(LOGICAL1)               ', 0000000050.00000)",
/* # set up journaling so that JCC can use the delete command */
                "I", "CALL QSYS.QCMDEXC('STRJRNPF FILE(JDDMDCOL/SRCPF) JRN(JDDMDCOL/QSQJRN)         ', 0000000060.00000)",
/* Create a logical file */
		"Q", "delete from JDDMDCOL.SRCPF",
		"Q", "insert into JDDMDCOL.SRCPF VALUES(1, 040404, ' A              R RECORD                    PFILE(JDDMDCOL/TABLE1)')",
		"Q", "insert into JDDMDCOL.SRCPF VALUES(1, 040404, ' A                A')",
		"I", "CALL QSYS.QCMDEXC('DLTF JDDMDCOL/LOGICAL1          ',   0000000030.00000)",
		"Q", "CALL QSYS.QCMDEXC('CRTLF FILE(JDDMDCOL/LOGICAL1) SRCFILE(JDDMDCOL/SRCPF) SRCMBR(LOGICAL1) MBR(LOGICAL1)         ',0000000090.00000)",
/* Create an MQT */
		"I", "DROP TABLE JDDMDCOL.MQT1",
		"3", "CREATE TABLE JDDMDCOL.MQT1(A INT)",
		"3", "ALTER TABLE JDDMDCOL.MQT1 ADD MATERIALIZED QUERY ( SELECT DISTINCT A FROM JDDMDCOL.TABLE1) DATA INITIALLY DEFERRED REFRESH DEFERRED MAINTAINED BY USER"
	    };


	    stmt = conn.createStatement();

	    String exceptionAction;
	    String sql;
	    for (int i =0 ; i < statements.length; i+=2) {
		exceptionAction=statements[i];
		sql=statements[i+1];





		    // Update the generic JDDMDCOL with the current collection 
		    sql = sql.replaceAll("JDDMDCOL", jddmdCol_);

		try {

		    stmt.executeUpdate(sql);

		} catch (Exception e) {
		    if (exceptionAction.equals("I")) {
		        // just ignore
		    } else if (!is400 && exceptionAction.equals("Q")) {
			// ignore this too
		    } else {
			System.out.println("Exception action = "+exceptionAction+" Exception "+e+" on "+sql+"\n");
			message.append("Exception "+e+" on "+sql+"\n");
			throw e;
		    }
		}
	    }
	    setupDone=true;

	} /* !setupDone */
    }

    public void checkCombination(int expectedTypes) {
	if (JDTestDriver.isLUW() && ((expectedTypes & SYNONYM) != 0)) {
	    notApplicable("LUW doesn't accept SYNONYM as name for ALIAS");
	    return;
	}
	try {
	  message.setLength(0);

	    message.append("Added by native driver 4/5/05");
	    DatabaseMetaData dmd = connection_.getMetaData();

	    setupObjects(connection_);

	    int typeSize  = 0;
	    String checking="";
	    String types[] = new String[MAXTYPECOUNT];

	    if ((expectedTypes & ALIAS) != 0) {
		types[typeSize]= "ALIAS";
		checking+=      " ALIAS";
		typeSize++;
	    }
	    if ((expectedTypes & TABLE) != 0) {
		types[typeSize]= "TABLE";
		checking+=      " TABLE";
		typeSize++;
	    }
	    if ((expectedTypes & SYSTEMTABLE) != 0) {
		types[typeSize]= "SYSTEM TABLE";
		checking+=      " SYSTEM TABLE";
		typeSize++;
	    }
	    if ((expectedTypes & VIEW) != 0) {
		types[typeSize]= "VIEW";
		checking+=      " VIEW";
		typeSize++;
	    }
	    if ((expectedTypes & MQT) != 0) {
		types[typeSize]= "MATERIALIZED QUERY TABLE";
		checking+=      " MATERIALIZED QUERY TABLE";
		typeSize++;
	    }

	    if ((expectedTypes & SYNONYM) != 0) {
		types[typeSize]= "SYNONYM";
		checking+=      " SYNONYM";
		typeSize++;
	    }

	    message.append( " "+ checking);

	    String seeTypes[] = new String[typeSize];
	    for (int i = 0; i < typeSize; i++) {
		seeTypes[i] = types[i];
	    }
	    String schema = jddmdCol_;

	    ResultSet rs = dmd.getTables(null, schema, null, seeTypes);
	    boolean check = checkResultSet(rs, expectedTypes);
	    assertCondition(check, message);
	} catch (Exception e) {
	    failed(e, message.toString());
	}

    }

/**
 * Variations to test the different combinations of types
 */
   public void Var052() {checkCombination(ALIAS); }
   public void Var053() {checkCombination( TABLE ); }
   public void Var054() {checkCombination( ALIAS | TABLE ); }
   public void Var055() {checkCombination( SYSTEMTABLE ); }
   public void Var056() {
       if( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()){
                notApplicable("NOT APPLICABLE.  Not currently supported in toolbox driver.  Possible TODO later.");
                return;
            }
       checkCombination( ALIAS | SYSTEMTABLE ); }
   public void Var057() {checkCombination( TABLE | SYSTEMTABLE ); }
   public void Var058() {checkCombination( ALIAS | TABLE | SYSTEMTABLE ); }
   public void Var059() {checkCombination( VIEW ); }
   public void Var060() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( ALIAS | VIEW ); }
   public void Var061() {checkCombination( TABLE | VIEW ); }
   public void Var062() {checkCombination( ALIAS | TABLE | VIEW ); }
   public void Var063() {checkCombination( SYSTEMTABLE | VIEW ); }
   public void Var064() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( ALIAS | SYSTEMTABLE | VIEW ); }
   public void Var065() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( TABLE | SYSTEMTABLE | VIEW ); }
   public void Var066() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( ALIAS | TABLE | SYSTEMTABLE | VIEW ); }
   public void Var067() {checkCombination( MQT ); }
   public void Var068() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( ALIAS | MQT ); }
   public void Var069() {checkCombination( TABLE | MQT ); }
   public void Var070() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( ALIAS | TABLE | MQT ); }
   public void Var071() {checkCombination( SYSTEMTABLE | MQT ); }
   public void Var072() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( ALIAS | SYSTEMTABLE | MQT ); }
   public void Var073() {checkCombination( TABLE | SYSTEMTABLE | MQT ); }
   public void Var074() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( ALIAS | TABLE | SYSTEMTABLE | MQT ); }
   public void Var075() {checkCombination( VIEW | MQT ); }
   public void Var076() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( ALIAS | VIEW | MQT ); }
   public void Var077() {checkCombination( TABLE | VIEW | MQT ); }
   public void Var078() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( ALIAS | TABLE | VIEW | MQT ); }
   public void Var079() {checkCombination( SYSTEMTABLE | VIEW | MQT ); }
   public void Var080() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( ALIAS | SYSTEMTABLE | VIEW | MQT ); }
   public void Var081() {checkCombination( TABLE | SYSTEMTABLE | VIEW | MQT ); }
   public void Var082() {checkCombination( ALIAS | TABLE | SYSTEMTABLE | VIEW | MQT ); }

// Additional synonym checkes
   public void Var083() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM); }
   public void Var084() {checkCombination( SYNONYM | ALIAS); }
   public void Var085() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | TABLE ); }
   public void Var086() {checkCombination( SYNONYM | ALIAS | TABLE ); }
   public void Var087() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | SYSTEMTABLE ); }
   public void Var088() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | SYSTEMTABLE ); }
   public void Var089() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | TABLE | SYSTEMTABLE ); }
   public void Var090() {checkCombination( SYNONYM | ALIAS | TABLE | SYSTEMTABLE ); }
   public void Var091() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | VIEW ); }
   public void Var092() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | VIEW ); }
   public void Var093() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | TABLE | VIEW ); }
   public void Var094() {checkCombination( SYNONYM | ALIAS | TABLE | VIEW ); }
   public void Var095() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | SYSTEMTABLE | VIEW ); }
   public void Var096() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | SYSTEMTABLE | VIEW ); }
   public void Var097() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | TABLE | SYSTEMTABLE | VIEW ); }
   public void Var098() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | TABLE | SYSTEMTABLE | VIEW ); }
   public void Var099() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | MQT ); }
   public void Var100() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | MQT ); }
   public void Var101() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | TABLE | MQT ); }
   public void Var102() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | TABLE | MQT ); }
   public void Var103() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | SYSTEMTABLE | MQT ); }
   public void Var104() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | SYSTEMTABLE | MQT ); }
   public void Var105() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | TABLE | SYSTEMTABLE | MQT ); }
   public void Var106() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | TABLE | SYSTEMTABLE | MQT ); }
   public void Var107() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | VIEW | MQT ); }
   public void Var108() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | VIEW | MQT ); }
   public void Var109() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | TABLE | VIEW | MQT ); }
   public void Var110() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | TABLE | VIEW | MQT ); }
   public void Var111() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | SYSTEMTABLE | VIEW | MQT ); }
   public void Var112() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | ALIAS | SYSTEMTABLE | VIEW | MQT ); }
   public void Var113() {
       if (ignoreIfToolbox() == true) return;  //@PDA
       checkCombination( SYNONYM | TABLE | SYSTEMTABLE | VIEW | MQT ); }
   public void Var114() {checkCombination( SYNONYM | ALIAS | TABLE | SYSTEMTABLE | VIEW | MQT ); }

   /**
    * Look at the contents of SYSIBM
    */
   public void Var115() {
	try {
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX )
	    {
		notApplicable("Not toolbox test.  TODO. Look into this later.");
		return;
	    }

	    if (JDTestDriver.isLUW())
	    {
		notApplicable("Not LUW test.  TODO. Look into this later.");
		return;
	    }

	    String setupInfo = setupExpected();
      message.setLength(0);
	    message.append(setupInfo + "Added by native driver 4/25/06");
	    DatabaseMetaData dmd = connection_.getMetaData();

	    String types[] = { "ALIAS", "TABLE",  "SYSTEM TABLE", "VIEW", "MATERIALIZED QUERY TABLE", "SYNONYM" };
	    String schema = "SYSIBM";
	    ResultSet rs = dmd.getTables(null, schema, null, types);
	    boolean check = checkSysibmResultSet(rs);
	    assertCondition(check, message);
	} catch (Exception e) {
	    failed(e, message.toString());
	}


   }

   private boolean ignoreIfToolbox() {
       if( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()){
           notApplicable("NOT APPLICABLE.  Not currently supported in toolbox driver.  Possible TODO later.");
           return true;
       }
       return false;
   }




/**
getTables() - check the metadata for the RS -- isAutoIncrement
**/
   public void Var116()
    {
       String expected13[] = {null, "false","false","false","false","false"};
       String expected14[] = {null, "false","false","false","false","false", "false","false","false","false","false"};
       checkExpectedMDStrings(expected13, expected14, expected14, ISAUTOINCREMENT);
   }




/**
getTables() - check the metadata for the RS -- isSearchable
**/
   public void Var117()
    {
       String expected13[] = {null, "true","true","true","true","true"};
       String expected14[] = {null, "true","true","true","true","true", "true","true","true","true","true"};
       checkExpectedMDStrings(expected13, expected14, expected14, ISSEARCHABLE);
   }

/**
getTables() - check the metadata for the RS -- isCurrency
**/
   public void Var118()
   {
       String expected13[] = {null, "false","false","false","false","false"};
       String expected14[] = {null, "false","false","false","false","false", "false","false","false","false","false"};
       checkExpectedMDStrings(expected13, expected14,expected14,  ISCURRENCY);
   }

/**
getTables() - check the metadata for the RS -- getCatalogName
**/
   public void Var119()
   {
       String catalog = "UNKNOWN";
       try {
           catalog =  connection_.getCatalog();
           if (catalog == null) {
         catalog = getCatalogFromURL(baseURL_);
         System.out.println("Retrieved "+catalog+" from URL:"+baseURL_);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }

       String expected13[] = {null, catalog,catalog,catalog,catalog,catalog};
       String expected14[] = {null, catalog,catalog,catalog,catalog,catalog, catalog,catalog,catalog,catalog,catalog};
       checkExpectedMDStrings(expected13, expected14, expected14, GETCATALOGNAME);

   }
/**
getTables() - check the metadata for the RS -- isReadOnly
**/
   public void Var120()
   {
       String expected13[] = {null, "true","true","true","true","true"};
       String expected14[] = {null, "true","true","true","true","true", "true","true","true","true","true"};
       checkExpectedMDStrings(expected13, expected14, expected14, ISREADONLY);
   }

/**
getTables() - check the metadata for the RS -- isWritable
**/
   public void Var121()
   {
       String expected13[] = {null, "false","false","false","false","false"};
       String expected14[] = {null, "false","false","false","false","false", "false","false","false","false","false"};
       checkExpectedMDStrings(expected13, expected14, expected14, ISWRITABLE);
   }

/**
getTables() - check the metadata for the RS -- isDefinitelyWritable
**/
   public void Var122()
   {
       String expected13[] = {null, "false","false","false","false","false"};
       String expected14[] = {null, "false","false","false","false","false", "false","false","false","false","false"};
       checkExpectedMDStrings(expected13, expected14, expected14, ISDEFINITELYWRITABLE);
   }




   /**
   getTables()- Long schemas - Specify a schema pattern that matches a schema
   exactly.  All matching tables should be returned.
   **/
       public void Var123()
       {
           try {
               StringBuffer sb = new StringBuffer();
               ResultSet rs = dmd_.getTables (connectionCatalog_,
                                              JDDMDTest.SCHEMAS_LEN128.toUpperCase(), "TABLE%", justTables);

               // Check for some of the tables.
               boolean check1 = false;
               boolean success = true;
               int rows = 0;
               while (rs.next ()) {
                   ++rows;
                   success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;


                   String table = rs.getString ("TABLE_SCHEM")
                                  + "." + rs.getString ("TABLE_NAME");
                   if (table.equals (JDDMDTest.SCHEMAS_LEN128.toUpperCase() + ".TABLE1"))
                       check1 = true;

               }

               rs.close ();
               assertCondition (success && check1 && (rows == 1),
				"success = "+success+
				" check1 = "+check1+
				" rows = "  + rows + " - v7r1 long schema" );
           } catch (Exception e) {
               failed (e, "Unexpected Exception - v7r1 long schema");
           }
       }


       /**
       getTables()- Long schemas - same as 123 but toolbox going to ROI instead of sysibm
       **/
           public void Var124()
           {
               if(false || getDriver() != JDTestDriver.DRIVER_TOOLBOX)
               {
                   notApplicable("V7R1 long schema (non sysibm SP) TC.");
                   return;
               }
               try {
		   String expectedTable = JDDMDTest.SCHEMAS_LEN128.toUpperCase() + ".TABLE1";
                   StringBuffer sb = new StringBuffer();
                   ResultSet rs = dmdNoSysibm_.getTables (connectionCatalog_,
                                                  JDDMDTest.SCHEMAS_LEN128, "TABLE%", justTables);

                   // Check for some of the tables.
                   boolean check1 = false;
                   boolean success = true;
                   int rows = 0;
                   while (rs.next ()) {
                       ++rows;
                       success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_) && success;


                       String table = rs.getString ("TABLE_SCHEM")
                                      + "." + rs.getString ("TABLE_NAME");
		       if (table.equals (expectedTable)) {
			   check1 = true;
		       } else {
			   sb.append("\n'"+table+"' != '"+expectedTable+"'\n");
		       }

                   }

                   rs.close ();
                   assertCondition (success && check1 && (rows == 1),
				    "success="+success+
				    " check1(for '"+expectedTable+"')="+check1+
				    " rows="+rows+" - v7r1 long schema "+sb.toString());
               } catch (Exception e) {
                   failed (e, "Unexpected Exception - v7r1 long schema");
               }
           }





/**
getTables() - Run getTables multiple times.  Make sure there is not a handle leak.
Created 1/27/2011 for CPS 8DHTTE.

**/
    public void Var125()   // @B1A
    {
	String added = " -- added by 1/27/2011 to test for native statement leak in metadata";
        if (checkNative()) {
            try {
                String catalog = null;
                String schema = "JDTESTDMD";
                String table   = "%";
                String types[]  = {"TABLE", "VIEW", "SYSTEM TABLE"};


		Statement stmt = connection_.createStatement();

		for (int i = 0; i < 1000; i++) {
		    // System.out.println("Calling getTables");
		    ResultSet rs = dmd_.getTables(catalog, schema, table, types);

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








  public void Var126() {
	  checkRSMD(false);
  }
  public void Var127() {
	  checkRSMD(true);
  }

    public void checkRSMD(boolean extendedMetadata)
    {

	Connection connection = connection_;
	DatabaseMetaData dmd = dmd_;
	int TESTSIZE=40;
	StringBuffer prime = new StringBuffer();
	int j=0;
	int col=0;
	String added=" -- Reworked 03/23/2012";
	boolean passed=true;
	message.setLength(0);

	String [][] methodTests = {
	    /* Primed by 546CN */
      {"isAutoIncrement","1","false"},
      {"isCaseSensitive","1","true"},
      {"isSearchable","1","true"},
      {"isCurrency","1","false"},
      {"isNullable","1","0"},
      {"isSigned","1","false"},
      {"getColumnDisplaySize","1","128"},
      {"getColumnLabel","1","TABLE_CAT"},
      {"getColumnName","1","TABLE_CAT"},
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
      {"getColumnLabel","2","TABLE_SCHEM"},
      {"getColumnName","2","TABLE_SCHEM"},
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
      {"getColumnLabel","3","TABLE_NAME"},
      {"getColumnName","3","TABLE_NAME"},
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
      {"isNullable","4","0"},
      {"isSigned","4","false"},
      {"getColumnDisplaySize","4","24"},
      {"getColumnLabel","4","TABLE_TYPE"},
      {"getColumnName","4","TABLE_TYPE"},
      {"getPrecision","4","24"},
      {"getScale","4","0"},
      {"getCatalogName","4","LOCALHOST"},
      {"getColumnType","4","12"},
      {"getColumnTypeName","4","VARCHAR"},
      {"isReadOnly","4","true"},
      {"isWritable","4","false"},
      {"isDefinitelyWritable","4","false"},
      {"getColumnClassName","4","java.lang.String"},
      {"isAutoIncrement","5","false"},
      {"isCaseSensitive","5","true"},
      {"isSearchable","5","true"},
      {"isCurrency","5","false"},
      {"isNullable","5","1"},
      {"isSigned","5","false"},
      {"getColumnDisplaySize","5","2000"},
      {"getColumnLabel","5","REMARKS"},
      {"getColumnName","5","REMARKS"},
      {"getPrecision","5","2000"},
      {"getScale","5","0"},
      {"getCatalogName","5","LOCALHOST"},
      {"getColumnType","5","12"},
      {"getColumnTypeName","5","VARCHAR"},
      {"isReadOnly","5","true"},
      {"isWritable","5","false"},
      {"isDefinitelyWritable","5","false"},
      {"getColumnClassName","5","java.lang.String"},
      {"isAutoIncrement","6","false"},
      {"isCaseSensitive","6","true"},
      {"isSearchable","6","true"},
      {"isCurrency","6","false"},
      {"isNullable","6","1"},
      {"isSigned","6","false"},
      {"getColumnDisplaySize","6","128"},
      {"getColumnLabel","6","TYPE_CAT"},
      {"getColumnName","6","TYPE_CAT"},
      {"getPrecision","6","128"},
      {"getScale","6","0"},
      {"getCatalogName","6","LOCALHOST"},
      {"getColumnType","6","12"},
      {"getColumnTypeName","6","VARCHAR"},
      {"isReadOnly","6","true"},
      {"isWritable","6","false"},
      {"isDefinitelyWritable","6","false"},
      {"getColumnClassName","6","java.lang.String"},
      {"isAutoIncrement","7","false"},
      {"isCaseSensitive","7","true"},
      {"isSearchable","7","true"},
      {"isCurrency","7","false"},
      {"isNullable","7","1"},
      {"isSigned","7","false"},
      {"getColumnDisplaySize","7","128"},
      {"getColumnLabel","7","TYPE_SCHEM"},
      {"getColumnName","7","TYPE_SCHEM"},
      {"getPrecision","7","128"},
      {"getScale","7","0"},
      {"getCatalogName","7","LOCALHOST"},
      {"getColumnType","7","12"},
      {"getColumnTypeName","7","VARCHAR"},
      {"isReadOnly","7","true"},
      {"isWritable","7","false"},
      {"isDefinitelyWritable","7","false"},
      {"getColumnClassName","7","java.lang.String"},
      {"isAutoIncrement","8","false"},
      {"isCaseSensitive","8","true"},
      {"isSearchable","8","true"},
      {"isCurrency","8","false"},
      {"isNullable","8","1"},
      {"isSigned","8","false"},
      {"getColumnDisplaySize","8","128"},
      {"getColumnLabel","8","TYPE_NAME"},
      {"getColumnName","8","TYPE_NAME"},
      {"getPrecision","8","128"},
      {"getScale","8","0"},
      {"getCatalogName","8","LOCALHOST"},
      {"getColumnType","8","12"},
      {"getColumnTypeName","8","VARCHAR"},
      {"isReadOnly","8","true"},
      {"isWritable","8","false"},
      {"isDefinitelyWritable","8","false"},
      {"getColumnClassName","8","java.lang.String"},
      {"isAutoIncrement","9","false"},
      {"isCaseSensitive","9","true"},
      {"isSearchable","9","true"},
      {"isCurrency","9","false"},
      {"isNullable","9","1"},
      {"isSigned","9","false"},
      {"getColumnDisplaySize","9","128"},
      {"getColumnLabel","9","SELF_REFERENCING_COL_NAME"},
      {"getColumnName","9","SELF_REFERENCING_COL_NAME"},
      {"getPrecision","9","128"},
      {"getScale","9","0"},
      {"getCatalogName","9","LOCALHOST"},
      {"getColumnType","9","12"},
      {"getColumnTypeName","9","VARCHAR"},
      {"isReadOnly","9","true"},
      {"isWritable","9","false"},
      {"isDefinitelyWritable","9","false"},
      {"getColumnClassName","9","java.lang.String"},
      {"isAutoIncrement","10","false"},
      {"isCaseSensitive","10","true"},
      {"isSearchable","10","true"},
      {"isCurrency","10","false"},
      {"isNullable","10","1"},
      {"isSigned","10","false"},
      {"getColumnDisplaySize","10","128"},
      {"getColumnLabel","10","REF_GENERATION"},
      {"getColumnName","10","REF_GENERATION"},
      {"getPrecision","10","128"},
      {"getScale","10","0"},
      {"getCatalogName","10","LOCALHOST"},
      {"getColumnType","10","12"},
      {"getColumnTypeName","10","VARCHAR"},
      {"isReadOnly","10","true"},
      {"isWritable","10","false"},
      {"isDefinitelyWritable","10","false"},
      {"getColumnClassName","10","java.lang.String"},



	};


	String [][] fixup614T = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"getColumnDisplaySize","4","128"},
	    {"getPrecision","4","128"},
	    {"isNullable","5","0"},
	    {"getColumnDisplaySize","5","254"},
	    {"getPrecision","5","254"},
	};




	String [][] fixup716T = {
	    {"getColumnType","5","-9"},
	    {"getColumnTypeName","5","NVARCHAR"},
	    {"getColumnLabel","9","SELF_REFERENCING_COL _NAME"},
	}; 


	String [][] fixup738T = {
	    {"getColumnType","5","-9"},
	    {"getColumnTypeName","5","NVARCHAR"},
	    {"getColumnLabel","1","Table Cat"},
	    {"getColumnLabel","2","Table Schem"},
	    {"getColumnLabel","3","Table Name"},
	    {"getColumnLabel","5","Remarks"},
	    {"getColumnLabel","6","Type Cat"},
	    {"getColumnLabel","7","Type Schem"},
	    {"getColumnLabel","8","Type Name"},
	    {"getColumnLabel","9","Self Referencing Col Name"},
	    {"getColumnLabel","10","Ref Generation"},
	    {"getColumnDisplaySize","4","128"},
	    {"getPrecision","4","128"},
	}; 


	String [][] fixup715TS = {
	    {"getColumnTypeName","5","NVARCHAR"},
	};

	String [][] fixup716TS = {
	    {"getColumnType","5","-9"},
	    {"getColumnTypeName","5","NVARCHAR"},
	}; 


	String [][] fixup736TS = {
	    {"getColumnType","5","-9"},
	    {"getColumnTypeName","5","NVARCHAR"},
	    {"getColumnDisplaySize","4","128"},
	    {"getPrecision","4","128"},
	};	

	String [][] fixup715T= {
	    {"getColumnTypeName","5","NVARCHAR"},
	    {"getColumnLabel","9","SELF_REFERENCING_COL _NAME"},

	};

	String [][]  fixup726L = {
	    {"getColumnTypeName","5","VARGRAPHIC"},
	};

	String [][] fixup714N = {
	    {"getColumnTypeName","5","NVARCHAR"},
	};
	String [][] fixup716N = {
	    {"getColumnTypeName","5","NVARCHAR"},
	    {"getColumnType","5","-9"},
	};

	String [][] fixup736N = {
	    {"getColumnTypeName","5","NVARCHAR"},
	    {"getColumnType","5","-9"},
	    {"getColumnDisplaySize","4","128"},
	    {"getPrecision","4","128"},

	};

	String[][] fixupExtended714N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"getColumnTypeName","5","NVARCHAR"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	};

	String[][] fixupExtended716N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"getColumnTypeName","5","NVARCHAR"},
	    {"getColumnType","5","-9"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	};


	String[][] fixupExtended736N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"getColumnTypeName","5","NVARCHAR"},
	    {"getColumnType","5","-9"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	    {"getColumnLabel","1","Table               Cat"},
	    {"getColumnLabel","2","Table               Schem"},
	    {"getColumnLabel","3","Table               Name"},
	    {"getColumnLabel","5","Remarks"},
	    {"getColumnLabel","6","Type                Cat"},
	    {"getColumnLabel","7","Type                Schem"},
	    {"getColumnLabel","8","Type                Name"},
	    {"getColumnLabel","9","Self Referencing ColName"},
	    {"getColumnLabel","10","Ref                 Generation"},
	    {"getColumnDisplaySize","4","128"},
	    {"getPrecision","4","128"},

	};



	String [][] fixup = {};

	String [][] fixupExtended546N={
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
	} ;



	Object[][] fixupArrayExtended = {


	    { "543T", fixup614T},
	    { "544T", fixup614T},
	    { "545T", fixup614T},
	    { "546T", fixup614T},

	    { "614T", fixup614T},
	    { "615T", fixup614T},
	    { "616T", fixup614T},
	    { "617T", fixup614T},
	    { "618T", fixup614T},

	    { "714T", fixup715T},
	    { "715T", fixup715T},
	    { "716T", fixup716T, "09/06/2012 primed from 7166U "},
	    { "717T", fixup716T, "09/06/2012 guess from 716T "},
	    { "718T", fixup716T, "09/06/2012 guess from 716T "},
	    { "719T", fixup716T, "09/06/2012 guess from 716T "},


	    { "726T", fixup716T, "09/06/2012 guess from 716T "},
	    { "727T", fixup716T, "09/06/2012 guess from 716T "}, 

	    {  "736T", fixup738T, "5/20/2109 updated"},
	    {  "737T", fixup738T, "5/20/2109 updated"},
	    {  "738T", fixup738T, "5/20/2109 updated"},
	    {  "739T", fixup738T, "5/20/2109 updated"},

	    { "546N", fixupExtended546N},
	    { "614N", fixupExtended546N},
	    { "615N", fixupExtended546N},
	    { "616N", fixupExtended546N},
	    { "714N", fixupExtended714N},
	    { "715N", fixupExtended714N}, 
	    { "716N", fixupExtended716N},
	    { "717N", fixupExtended716N},
	    { "718N", fixupExtended716N},
	    { "719N", fixupExtended716N},

	    { "736N", fixupExtended736N},
	    { "737N", fixupExtended736N},
	    { "738N", fixupExtended736N},
	    { "739N", fixupExtended736N},


	    { "716L", fixup726L},
	    { "726L", fixup726L}, 
	};

	


	Object[][] fixupArray = {

	    { "543TX", fixup614T},
	    { "544TX", fixup614T},
	    { "545TX", fixup614T},
	    { "546TX", fixup614T},

	    { "614TX", fixup614T},
	    { "615TX", fixup614T},
	    { "616TX", fixup614T},
	    { "617TX", fixup614T},
	    { "618TX", fixup614T},

	    { "714TX", fixup614T},
	    { "714TS", fixup715TS}, 
	    { "715TS", fixup715TS},
	    { "716TS", fixup716TS, "11/17/2016 Primed 7166% "},
	    { "717TS", fixup716TS, "09/06/2012 guess from 717TS"},
	    { "718TS", fixup716TS, "09/06/2012 guess from 717TS"},
	    { "719TS", fixup716TS, "09/06/2012 guess from 717TS"},


	    { "737TS", fixup736TS, "05/02/2022 updated "},

	    { "715TX", fixup614T},
	    { "716TX", fixup614T},
	    { "717TX", fixup614T},
	    { "718TX", fixup614T},
	    { "719TX", fixup614T},

	    { "724TX", fixup614T},
	    { "725TX", fixup614T},
	    { "726TX", fixup614T},
	    { "727TX", fixup614T},


	    { "714NS",  fixup714N},
	    { "715NS",  fixup714N},
	    { "716NS",  fixup716N},
	    { "717NS",  fixup716N},
	    { "718NS",  fixup716N},
	    { "719NS",  fixup716N},
	    { "736NS",  fixup736N},
	    { "737NS",  fixup736N},
	    { "738NS",  fixup736N},
	    { "739NS",  fixup736N},


	    { "716LS", fixup726L},
	    { "726LS", fixup726L}, 

	};



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

		    fixup = getFixup(fixupArrayExtended, null, "fixupArrayExtended", message);



		} else {
		    String extra="X";
		    if (isSysibmMetadata()) {
		      extra = "S";
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



		String catalog1 = connection.getCatalog();
		if (catalog1 == null) {
		    catalog1 = system_.toUpperCase();
		}

		ResultSet[] rsA =  new ResultSet[TESTSIZE];
		ResultSetMetaData[] rsmdA = new ResultSetMetaData[TESTSIZE];
		for (j = 0; j < TESTSIZE; j++) {
		    rsA[j] = dmd.getTables (null, JDDMDTest.COLLECTION,
						  "TABLES%", justTables);

		    rsmdA[j] = rsA[j].getMetaData ();

		    ResultSetMetaData rsmd = rsmdA[j];
		    passed = verifyRsmd(rsmd, methodTests, catalog1, j, message, prime) && passed;

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
    getTables() - Check reaonly connection 
    **/ 
        public void Var128()
        {
            try {
                Connection c;
                if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
                    c = testDriver_.getConnection (baseURL_
                                                   + ";access=read only;libraries=*LIBL,QIWS," + JDDMDTest.COLLECTION + ","
                                                   + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                                                   userId_, encryptedPassword_);
                } else {
                    if (getDriver() == JDTestDriver.DRIVER_JCC) {
                       c = testDriver_.getConnection (baseURL_,
                          userId_, encryptedPassword_);

                    } else {
                       c = testDriver_.getConnection (baseURL_
                                                   + ";access=read only;libraries=" + JDDMDTest.COLLECTION,
                                                   userId_, encryptedPassword_);
                    }
                }

                DatabaseMetaData dmd = c.getMetaData ();
                ResultSet rs = dmd.getTables (null, null, null, null);

                // It is impossible to check that all tables come back,
                // so we just check that at least some of them come back.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
          if (JDTestDriver.isLUW()) {
        // Don't check for QIWS.QCUSTCDT
        check3=true;
          }
                while (rs.next ()) {
                    String table = rs.getString ("TABLE_SCHEM")
                                   + "." + rs.getString ("TABLE_NAME");
                    if (table.equals (JDDMDTest.COLLECTION + ".TABLES"))
                        check1 = true;
                    else if (table.equals (JDDMDTest.COLLECTION2 + ".TABLES3"))
                        check2 = true;
                    else if (table.equals ("QIWS.QCUSTCDT"))
                        check3 = true;
                }

                rs.close ();
                c.close ();
                assertCondition (check1 && check2 && check3,
                    "\n"+JDDMDTest.COLLECTION+".TABLES found="+check1+
                    "\n"+JDDMDTest.COLLECTION2+".TABLES3 found="+check2+
                    "\n QIWS.QCUSTCDT found="+check3);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }


}


class JDDMDGetTablesExpectedRow {
    public int typeFlag;
    public String schema;
    public String name;
    public String typeName;
    public boolean optional; 
    public JDDMDGetTablesExpectedRow(int typeFlag, String schema, String name, String typeName) {
	this.typeFlag = typeFlag;
	this.schema = schema;
	this.name = name;
	this.typeName = typeName;
	this.optional = false; 
    }

    public JDDMDGetTablesExpectedRow(int typeFlag, String schema, String name, String typeName, boolean optional) {
	this.typeFlag = typeFlag;
	this.schema = schema;
	this.name = name;
	this.typeName = typeName;
	this.optional = optional; 
    }






}

