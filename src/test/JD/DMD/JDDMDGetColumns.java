///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetColumns.java
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
// File Name:    JDDMDGetColumns.java
//
// Classes:      JDDMDGetColumns
//
////////////////////////////////////////////////////////////////////////
//
// Compatiblity issues
//
// 1.  JDBC spec states names should be SCOPE_CATLOG not SCOPE_CATALOG
//     Var001
// 2.  LUW returns SMALLINT for types of columsn 9,10,11,14,15.  These
//     should be INTS -- Var001
// 3.  LUW returns TABLE_CAT as null -- Var005
// 4.  LUW returns rows for empty string for catalog parameter Var006
//

////////////////////////////////////////////////////////////////////////
//
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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;

import test.JDDMDTest;
import test.JDDriverTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDDMDGetColumns.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getColumns()
</ul>
**/
public class JDDMDGetColumns
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetColumns";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }



    // Private data.
    private Connection          closedConnection_;
    private Connection          connectionNoSysibm_; //@128sch
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;
    private DatabaseMetaData    dmdNoSysibm_; //@128sch

    // Public toolbox system name
    String systemName;    /** @K2 **/

    boolean var003Executed = false;	// @K3
    StringBuffer message = new StringBuffer() ;
    String messageColumnName = "";



/**
Constructor.
**/
    public JDDMDGetColumns (AS400 systemObject,
                            Hashtable namesAndVars,
                            int runMode,
                            FileOutputStream fileOutputStream,
                            
                            String password)
    {
        super (systemObject, "JDDMDGetColumns",
               namesAndVars, runMode, fileOutputStream,
               password);
    }




/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {



      String url = baseURL_
        + ";libraries=" + JDDMDTest.COLLECTION + " "
        + JDDMDTest.COLLECTION2 + " " + JDDMDTest.COLLECTIONXX;
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          url = baseURL_;
        }

        String urlNoSysibm = url;
        connection_ = testDriver_.getConnection (url,
                userId_, encryptedPassword_);

        /** @K2 **/
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
	    if (proxy_ != null && (!proxy_.equals(""))) {

		systemName = ((AS400JDBCConnection)connection_).getSystem().getSystemName();
	    } else {
		systemName = connection_.getCatalog();
		if (systemName == null) {
		    systemName= system_;
		}
	    }
            urlNoSysibm += ";metadata source=0"; //roi (nosysibm)
        } else {
            systemName = connection_.getCatalog();
            if (systemName == null) {
                systemName= system_;
            }
        }

        //get connection that will not use sysibm metadata
        connectionNoSysibm_ = testDriver_.getConnection (urlNoSysibm,
                userId_, encryptedPassword_);

        dmd_ = connection_.getMetaData ();
        dmdNoSysibm_ = connectionNoSysibm_.getMetaData ();

        Statement s = connection_.createStatement ();


	String dropTables[] = {
	/* 0 */    JDDMDTest.COLLECTION  + ".COLUMNS",
	/* 1 */    JDDMDTest.COLLECTION	 + ".COLUMNS1",
	/* 2 */    JDDMDTest.COLLECTION	 + ".COLUMNS2",
	/* 3 */    JDDMDTest.COLLECTION2 + ".COLUMNS",
	/* 4 */    JDDMDTest.COLLECTION	 + ".LOBCOLUMNS",
	/* 5 */    JDDMDTest.COLLECTION	 + ".INTCOLUMNS ",
	/* 6 */    JDDMDTest.COLLECTION	 + ".FLOATCOLUMNS ",
	/* 7 */    JDDMDTest.COLLECTION	 + ".CHARBITCOLUMNS",
	/* 8 */    JDDMDTest.COLLECTION	 + ".GRAPHICCOLUMNS "

	};



	for (int i = 0; i < dropTables.length; i++) {
	  JDTestDriver.dropTable(s,dropTables[i]);
	}




	String createStatements[] = {
	/* 0 */     "CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".COLUMNS (COLUMN INT NOT NULL, COLUMN1 TIMESTAMP, "
			     + "COLUMN2 CHAR(10) NOT NULL, "
			     + "COLUMNXX DECIMAL(6,2))",
	/* 1 */     "CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".COLUMNS1 (COLUMN1 INT, COLUMN2 CHAR(10))",
	/* 2 */     "CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".COLUMNS2 (COLUMN1 INT, COLUMNXX CHAR(10))",
	/* 3 */     "CREATE TABLE " + JDDMDTest.COLLECTION2
			     + ".COLUMNS (COLUMN2 DECIMAL, COLUMNXX CHAR(10))",
	/* 4 */     "CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".LOBCOLUMNS ( clob100 CLOB(100), "
			     + "             dbclob200 DBCLOB(200), "
			     + "             blob400   BLOB(400) )",
	/* 5 */     "CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".INTCOLUMNS (COL1 BIGINT, COL2 SMALLINT, COL3 DATE, COL4 TIME)",
	/* 6 */     "CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".FLOATCOLUMNS (COL1 DOUBLE, COL2 FLOAT(10), COL3 REAL, COL4 NUMERIC(6,2))",
	/* 7 */     "CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".CHARBITCOLUMNS (COL1 CHAR(10) FOR BIT DATA, COL2 VARCHAR(10) FOR BIT DATA, COL3 VARCHAR(10), COL4 DATALINK(200))",
	/* 8 */     "CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".GRAPHICCOLUMNS (COL1 GRAPHIC(10), COL2 VARGRAPHIC(10))"

	};

        if (JDTestDriver.isLUW() ) {
          createStatements[4] = "CREATE TABLE " + JDDMDTest.COLLECTION
             + ".LOBCOLUMNS ( clob100 CLOB(100), "
             + "             dbclob200 CLOB(200), "
             + "             blob400   BLOB(400) )";
	  createStatements[7] = "CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".CHARBITCOLUMNS (COL1 CHAR(10) FOR BIT DATA, COL2 VARCHAR(10) FOR BIT DATA, COL3 VARCHAR(10), COL4 VARCHAR(200))";
          createStatements[8] = "CREATE TABLE " + JDDMDTest.COLLECTION
             + ".GRAPHICCOLUMNS (COL1 CHAR(10), COL2 VARCHAR(10))";

        }


	for (int i = 0; i < createStatements.length; i++) {

	    try {
		s.executeUpdate (createStatements[i]);
	    } catch (Exception e) {
		System.out.println("Warning.. create table failed "+createStatements[i]);
		e.printStackTrace();
	    }
	}
        String sql=null;
	try {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0
          && (!JDTestDriver.isLUW())) {

	  JDTestDriver.dropTable(s,JDDMDTest.COLLECTION    + ".CHARCOLUMNS");
	  JDTestDriver.dropTable(s,JDDMDTest.COLLECTION    + ".CHARUTF8COLUMNS");
	  JDTestDriver.dropTable(s,JDDMDTest.COLLECTION    + ".GRAPHICUTF16COLUMNS");

        sql = "CREATE TABLE "
            + JDDMDTest.COLLECTION
            + ".CHARCOLUMNS (COL1 BINARY(10), COL2 VARBINARY(10), COL3 VARCHAR(10), COL4 DATALINK(200))";
        s.executeUpdate(sql);

        sql = "CREATE TABLE "
            + JDDMDTest.COLLECTION
            + ".CHARUTF8COLUMNS (COL1 CHAR(10) CCSID 1208, COL2 VARCHAR(10) CCSID 1208, COL3 CLOB(1M) CCSID 1208)";
        s.executeUpdate(sql);
        sql = "CREATE TABLE "
            + JDDMDTest.COLLECTION
            + ".GRAPHICUTF16COLUMNS (COL1 GRAPHIC(10) CCSID 1200 , COL2 VARGRAPHIC(10) CCSID 1200, COL3 DBCLOB(1M) CCSID 1200)";
        s.executeUpdate(sql);
      }
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 && (!JDTestDriver.isLUW())) {
          sql = "CREATE TABLE " + JDDMDTest.COLLECTION
              + ".ROWIDCOLUMNS (COL1 ROWID , COL2 VARGRAPHIC(10) CCSID 1200)";
          s.executeUpdate(sql);

      }
    } catch (Exception e) {
      System.out.println("Warning.. create table failed " + sql);
      e.printStackTrace();
    }


        // @L1A
	if(getRelease() >= JDTestDriver.RELEASE_V7R1M0 && (!JDTestDriver.isLUW())){

	  JDTestDriver.dropTable(s,JDDMDTest.COLLECTION    + ".LCNCOLUMNS");
            sql = "CREATE TABLE "  + JDDMDTest.COLLECTION                         //@L1A
                 + ".LCNCOLUMNS (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INTEGER)";
	    try {
		s.executeUpdate (sql);  //@L1A
	    } catch (Exception e) {
		System.out.println("Warning.. create table failed "+sql);
		e.printStackTrace();
	    }
	}

        // @M1A
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        {

	  JDTestDriver.dropTable(s,JDDMDTest.COLLECTION    + ".DEFCOLUMNS");

            sql = "CREATE TABLE " + JDDMDTest.COLLECTION    // @M1A
                + ".DEFCOLUMNS (HASDEFAULT CHAR(15) DEFAULT 'DEFAULTVAL',"
                + " NODEFAULT CHAR(15),"
                + " DEFWITHBLANKS CHAR(15) DEFAULT 'MYDEFAULT VAL', "
                + " INTDEF INTEGER DEFAULT 4,"
                + " INTNODEF INTEGER,"
                + " NULLDEFAULT CHAR(15) DEFAULT NULL)"; // @M1A
            try{
                s.executeUpdate(sql);   // @M1A
            }
            catch(Exception e){
                System.out.println("Warning.. create table failed " + sql); // @M1A
                e.printStackTrace();                                        // @M1A
            }
        }
        // @128sch
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        {
	  JDTestDriver.dropTable(s,JDDMDTest.SCHEMAS_LEN128 + ".TABLE1");

            sql = "CREATE TABLE " + JDDMDTest.SCHEMAS_LEN128
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



        s.close ();

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

	String[] dropStatements = {
	"DROP TABLE " + JDDMDTest.COLLECTION + ".COLUMNS",
	"DROP TABLE " + JDDMDTest.COLLECTION + ".COLUMNS1",
	"DROP TABLE " + JDDMDTest.COLLECTION + ".COLUMNS2",
	"DROP TABLE " + JDDMDTest.COLLECTION2 + ".COLUMNS",
	"DROP TABLE " + JDDMDTest.COLLECTION + ".LOBCOLUMNS",
	"DROP TABLE " + JDDMDTest.COLLECTION + ".INTCOLUMNS",
	"DROP TABLE " + JDDMDTest.COLLECTION + ".FLOATCOLUMNS",
	"DROP TABLE " + JDDMDTest.COLLECTION + ".CHARBITCOLUMNS",
	"DROP TABLE " + JDDMDTest.COLLECTION + ".GRAPHICCOLUMNS"
	};

	for (int i = 0; i < dropStatements.length; i++) {
	    try {
		s.executeUpdate(dropStatements[i]);
	    } catch (Exception e) {
		System.out.println("Warning.. drop table failed "+dropStatements[i]);
		e.printStackTrace();
	    }
	}

	if(getRelease() >= JDTestDriver.RELEASE_V7R1M0 && (! JDTestDriver.isLUW())){     //@K1A
	    try {
		s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
				 + ".CHARCOLUMNS");
	    } catch (Exception e) {
		System.out.println("Warning.. drop table failed");
		e.printStackTrace();
	    }
            try {
              s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION + ".CHARUTF8COLUMNS");
            } catch (Exception e) {
                System.out.println("Warning.. drop table failed");
                e.printStackTrace();
            }
          try {
              s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION + ".GRAPHICUTF16COLUMNS");
            } catch (Exception e) {
                System.out.println("Warning.. drop table failed");
                e.printStackTrace();
            }
          try {
              s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION + ".ROWIDCOLUMNS");
            } catch (Exception e) {
                System.out.println("Warning.. drop table failed");
                e.printStackTrace();
            }



	}

        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0 && (! JDTestDriver.isLUW() )) //@L1A
        {
	try {
            s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION   //@L1A
                         + ".LCNCOLUMNS");
	} catch (Exception e) {
	    System.out.println("Warning.. drop table failed");
	    e.printStackTrace();
	}
        }

        // @M1A
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        {
            try{
                s.executeUpdate("DROP TABLE " + JDDMDTest.COLLECTION + ".DEFCOLUMNS");
            }
            catch(Exception e){
                System.out.println("Warning.. drop table failed");
                e.printStackTrace();
            }
        }

        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) //@128sch
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


    public void cleanupConnections ()
    throws Exception
    {
        connection_.close ();
    }


   /**
    * getColumnCatalog
    */

    private String getExpectedTableCat() throws SQLException {
	String catalog =  connection_.getCatalog ();
	if (catalog == null) {
	    if (JDTestDriver.isLUW()) {
		// leave as null
	    } else {
		catalog = system_.toUpperCase();
	    }
	}
	return catalog;
    }
/**
 * check a condition and add a message if wrong
 */
    public boolean check(boolean t, String info ) {
	if (!t) {
	    message.append(info+"\n");
	}
	return t;
    }


    public boolean check(int a, int expected, String info) {
	boolean result = (a == expected);
	if (!result) {
	    message.append(info+"="+a+" sb "+expected+" "+messageColumnName+"\n");

	}
	return result;
    }

    public boolean checkOr(int a, int b, int c, String info) {
	boolean result = (a == b) || ( a == c);
	if (!result) {
	    message.append(info+"="+a+" sb ("+b+" or "+c+") "+messageColumnName+"\n");

	}
	return result;
    }

    public boolean check(String a, String b, String info) {
	boolean result;
	if (a == null) {
	    result = ( b == null);
	} else {
	    result = a.equals(b);
	}
	if (!result) {
	    message.append(info+"=\""+a+"\" sb \""+b+"\" "+messageColumnName+"\n");
	}
	return result;
    }

/**
getColumns() - Check the result set format.
**/
    public void Var001()
    {
	// Result set format now checked by later variations.
	// Removing this variation to avoid double maintenance

	    assertCondition(true);
	    return;
    }



/**
getColumns() - Get a list of those created in this testcase and
verify all columns with system remarks (the default).

SQL400 - the 400 doesn't support the LABEL on values being brought back
         in the remarks field.  We therefore use comment on here to make
         sure that we test returning all the result set columns.

SQL400 - the CLI seems to return some pretty weird stuff for a timestamp
         field.  I changed the testcase for the native driver to expect
         what the CLI is returning, not because it is right, but because
         there are too many important things to do right now to fix this
         one.  At least this way, it will get flagged if the output changes.
**/
    public void Var002()
    {
        try {


            String catalog = getExpectedTableCat();

            Statement s = null;
            message = new StringBuffer();
            messageColumnName="";
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                // Label the columns.  No label on COLUMN, please.
                s = connection_.createStatement ();
                s.executeUpdate ("LABEL ON COLUMN " + JDDMDTest.COLLECTION
                        + ".COLUMNS (COLUMN1 TEXT IS '8')");
                s.executeUpdate ("LABEL ON COLUMN " + JDDMDTest.COLLECTION
                        + ".COLUMNS (COLUMN2 TEXT IS 'Nine')");
                s.executeUpdate ("LABEL ON COLUMN " + JDDMDTest.COLLECTION
                        + ".COLUMNS (COLUMNXX TEXT IS 'SQL/400')");
            } else {
                // Label the columns.  No label on COLUMN, please.
                s = connection_.createStatement ();

                if (JDTestDriver.isLUW()) {
                    // Syntax is different
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS.COLUMN1 IS '8'");
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS.COLUMN2 IS 'Nine'");
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS.COLUMNXX IS 'SQL/400'");

                } else {
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS (COLUMN1 IS '8')");
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS (COLUMN2 IS 'Nine')");
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS (COLUMNXX IS 'SQL/400')");
                }
            }

            s.close ();

            ResultSet rs = dmd_.getColumns (null, JDDMDTest.COLLECTION,
                    "COLUMNS", "%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String column           = rs.getString ("COLUMN_NAME");
                messageColumnName=column;
                success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;
                success =  check(rs.getString ("TABLE_SCHEM"), JDDMDTest.COLLECTION, "TABLE_SCHEM") && success;
                success =  check(rs.getString ("TABLE_NAME"),"COLUMNS", "TABLE_NAME") && success;

                short dataType          = rs.getShort ("DATA_TYPE");
                String typeName         = rs.getString ("TYPE_NAME");
		if (typeName == "TIMESTAMP") {
		    int stopHere = 0;
		    stopHere++; 
		    if (stopHere > 10000) {
		      System.out.println("stop for timestamp before heree"); 
		    }
		}
                int columnSize          = rs.getInt ("COLUMN_SIZE");
                int bufferLength        = rs.getInt ("BUFFER_LENGTH");
                int decimalDigits       = rs.getInt ("DECIMAL_DIGITS");
                int numPrecRadix        = rs.getInt ("NUM_PREC_RADIX");
                int nullable            = rs.getInt ("NULLABLE");
                String remarks          = rs.getString ("REMARKS");
                String columnDef        = rs.getString ("COLUMN_DEF");
                int sqlDataType         = rs.getInt ("SQL_DATA_TYPE");
                int sqlDateTimeSub      = rs.getInt ("SQL_DATETIME_SUB");
                int charOctetLength     = rs.getInt ("CHAR_OCTET_LENGTH");
                int ordinalPosition     = rs.getInt ("ORDINAL_POSITION");
                String isNullable       = rs.getString ("IS_NULLABLE");

                if (getDriver() == JDTestDriver.DRIVER_JCC  || (getJdbcLevel() >= 4)  ||
                        ((getDriver() == JDTestDriver.DRIVER_NATIVE ) &&
                                (getRelease() >= JDTestDriver.RELEASE_V7R1M0))
                                || ((getDriver() == JDTestDriver.DRIVER_TOOLBOX ) && isSysibmMetadata())) {
                    // Buffer length is "not used"  so don't check JCC
                    // check check below to assure that native and toolbox are consistent
                } else {
                    success =  check(bufferLength, 0, "bufferLength") && success;
                    success =  check(sqlDataType, 0, "sqlDataType") && success;
                    success =  check(sqlDateTimeSub, 0, "sqlDataTimeSub") && success;
                }

                if(getRelease() < JDTestDriver.RELEASE_V7R1M0 || ( !JDTestDriver.isLUW() && getDriver () != JDTestDriver.DRIVER_TOOLBOX ))  // @M1A
                    success =  check(columnDef, (String) null, "columnDef" )&& success;

                if (column.equals ("COLUMN")) {
                    if (getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_NATIVE)
                            || ((getDriver() == JDTestDriver.DRIVER_TOOLBOX ) && isSysibmMetadata())){
                        success = check(remarks, (String) null, "remarks") && success;
                        success = check(ordinalPosition, 1, "ordinalPosition") && success;
                        success = check(charOctetLength, 0, "charOctetLength") && success;
                    } else if (getDriver() == JDTestDriver.DRIVER_JCC) {
                        success = check(remarks, (String) null, "remarks") && success;
                        success = check(ordinalPosition, 1, "ordinalPosition") && success;
                        success = check(charOctetLength, 0, "charOctetLength") && success;

                    } else {
                        success = check(remarks, "","remarks") && success;
                        success = check(((ordinalPosition == -1) || (ordinalPosition == 1)),
                                "ordinalPosition = "+ordinalPosition+" sb -1 or 1") && success; // @C1C
                        success = check(charOctetLength, -1, "charOctetLength ") && success;
                    }

                    if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)   {              // @M1A
                        if (getDriver() == JDTestDriver.DRIVER_NATIVE || JDTestDriver.isLUW()
                                || ((getDriver() == JDTestDriver.DRIVER_TOOLBOX ) && isSysibmMetadata())) {
                            success = check(columnDef, null, "columnDef") && success;
                        } else {
                            success = check(columnDef, "", "columnDef") && success;     // @M1A
                        }
                    }

                    success = check(dataType,Types.INTEGER, "dataType")&& success;
                    success = check(typeName,"INTEGER", "typeName")&& success;
                    success = check(columnSize,10,"columnSize")&& success;
                    success = check(decimalDigits,0,"decimalDigits")&& success;
                    success = check(numPrecRadix,10,"numPrecRadix")&& success;
                    success = check(nullable,DatabaseMetaData.columnNoNulls,"nullable")&& success;
                    success = check(isNullable, ("NO"), "isNullable")&& success;
                } else if (column.equals ("COLUMN1")) {
                    if ((getDriver () == JDTestDriver.DRIVER_NATIVE)
                            || ((getDriver() == JDTestDriver.DRIVER_TOOLBOX ) && isSysibmMetadata())) { // @B1C
                        success = check(ordinalPosition , 2, "ordinalPosition")&& success;
                        success = check(charOctetLength,  0, "charOctetLength")&& success;
                        // A timestamp doesn't really have decimalDigits
                        success = check(nullable, DatabaseMetaData.columnNullable, "nullable")&& success;
                    } else if (getDriver() == JDTestDriver.DRIVER_JCC) { // @B1C
                        success = check(ordinalPosition, 2, "ordinalPosition") && success; // @C1C
                        success = check(charOctetLength, 0, "charOctetLength") && success;
                        success = check(nullable, DatabaseMetaData.columnNullable,"Nullable")&& success;
                    } else {
                        if (ordinalPosition == -1) {
                            // OK..
                        } else {
                            success = check(ordinalPosition, 2, "ordinalPosition")&& success;  // @C1C
                        }
                        success = check(charOctetLength , -1, "charOctetLength")&& success;
                        success = check(nullable, DatabaseMetaData.columnNullable, "Nullable")&& success;
                    }
                    success = check(remarks, "8", "remarks") && success;
                    success = check(dataType,         Types.TIMESTAMP, "dataType" )&& success;
                    success = check(typeName,        "TIMESTAMP","typeName")&& success;
                    success = check(columnSize,       26, "columnSize")&& success;
                    success = check(decimalDigits,    6, "decimalDigits" )&& success;
                    success = check(numPrecRadix, 0, "numPrecRadix")&& success;
                    success = check(isNullable,"YES", "isNullable")&& success;

                    if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)                 // @M1A
                        if (getDriver() == JDTestDriver.DRIVER_NATIVE || JDTestDriver.isLUW()
                                || ((getDriver() == JDTestDriver.DRIVER_TOOLBOX ) && isSysibmMetadata())) {
                            success = check(columnDef, null, "columnDef") && success;
                        } else {
                            success = check(columnDef, "", "columnDef") && success;     // @M1A
                        }
                } else if (column.equals ("COLUMN2")) {
                    if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
                        success = check(ordinalPosition, 3, "ordinalPosition") && success;
                    } else {
                        if (ordinalPosition == -1) {
                            // OK
                        } else {
                            success = check(ordinalPosition, 3, "ordinalPosition") && success; // @C1C
                        }
                    }

                    success = check(remarks, "Nine", "remarks") && success;
                    success = check(dataType, Types.CHAR, "dataType") && success;
                    success = check(typeName, "CHAR", "typeName") && success;
                    success = check(columnSize, 10, "columnSize") && success;
                    success = check(decimalDigits, 0, "decimalDigits") && success;
                    success = check(numPrecRadix, 0, "numPrecRadix") && success;
                    success = check(nullable, DatabaseMetaData.columnNoNulls, "nullable") && success;
                    success = check(charOctetLength,10,"charOctetLength") && success;
                    success = check(isNullable,"NO","isNullable") && success;

                    if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)                 // @M1A
                        if (getDriver() == JDTestDriver.DRIVER_NATIVE || JDTestDriver.isLUW()
                                || ((getDriver() == JDTestDriver.DRIVER_TOOLBOX ) && isSysibmMetadata())) {
                            success = check(columnDef, null, "columnDef") && success;
                        } else {
                            success = check(columnDef, "", "columnDef") && success;     // @M1A
                        }
                } else if (column.equals ("COLUMNXX")) {
                    if ((getDriver () == JDTestDriver.DRIVER_NATIVE) || (getDriver () == JDTestDriver.DRIVER_JCC)
                            || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                        success = check(ordinalPosition, 4, "ordinalPosition") && success;
                        success = check(charOctetLength, 0, "charOctetLength") && success;
                    } else {
                        if (ordinalPosition == -1) {
                            // OK
                        } else {
                            success = check(ordinalPosition, 4, "ordinalPosition") && success; // @C1C
                        }
                        success = (check(charOctetLength , -1, "charOctetLength") && success);
                    }

                    success = check(remarks,"SQL/400", "remarks") && success;
                    success = check(dataType , Types.DECIMAL, "dataType") && success;
                    success = check(typeName,"DECIMAL", "typeName") && success;
                    success = check(columnSize , 6, "columnSize") && success;
                    success = check(decimalDigits , 2, "decimalDigits") && success;
                    success = check(numPrecRadix , 10, "numPrecRadix") && success;
                    success = check(nullable , DatabaseMetaData.columnNullable, "nullable") && success;
                    success = check(isNullable,"YES", "isNullable") && success;

                    if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)                 // @M1A
                        if (getDriver() == JDTestDriver.DRIVER_NATIVE || JDTestDriver.isLUW()
                                || ((getDriver() == JDTestDriver.DRIVER_TOOLBOX ) && isSysibmMetadata())) {
                            success = check(columnDef, null, "columnDef") && success;
                        } else {
                            success = check(columnDef, "", "columnDef") && success;     // @M1A
                        }
                }

            }
            messageColumnName="";
            success = success && check(rows, 4, "rows");
            rs.close ();
            assertCondition ( success, message.toString());
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Get a list of those created in this testcase and
verify the remarks column with sql remarks.

SQL400 - The native driver will return null for a remarks field that
         has no value.  The toolbox will return an empty string.
**/
    public void Var003()
    {
	var003Executed = true;		// @K3
        try {
            message = new StringBuffer();
            messageColumnName="";

            Connection c;
	    if (getDriver() == JDDriverTest.DRIVER_JCC) {
		c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	    } else {
		c = testDriver_.getConnection (baseURL_
					       + ";remarks=sql", userId_, encryptedPassword_);
	    }

            // Label the columns.  No label on COLUMN, please.
            Statement s = connection_.createStatement ();

	    if (JDTestDriver.isLUW()) {
		    // Syntax is different
		s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				 + ".COLUMNS.COLUMN1 IS '2'");
		s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				 + ".COLUMNS.COLUMN2 IS 'Three'");
		s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				 + ".COLUMNS.COLUMNXX IS 'JDBC is fast'");

	    } else {
		s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				 + ".COLUMNS (COLUMN1 IS '2')");
		s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				 + ".COLUMNS (COLUMN2 IS 'Three')");
		s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				 + ".COLUMNS (COLUMNXX IS 'JDBC is fast')");
	    }
            s.close ();

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getColumns (null, JDDMDTest.COLLECTION,
                                           "COLUMNS", "%");
            boolean success = true;

            String catalog = getExpectedTableCat();


            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String column = rs.getString ("COLUMN_NAME");
                messageColumnName=column;

                success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;
                success =  check(rs.getString ("TABLE_SCHEM"), JDDMDTest.COLLECTION, "TABLE_SCHEM") && success;
                success =  check(rs.getString ("TABLE_NAME"),"COLUMNS", "TABLE_NAME") && success;

                String remarks = rs.getString ("REMARKS");
                message.append(column+" "+remarks);
                if (column.equals ("COLUMN")) {
                    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                      success = check(remarks, (String) "", "remarks") && success;

                    } else {
                      success = check(remarks, (String) null, "remarks") && success;
                    }
                } else if (column.equals ("COLUMN1"))
                    success = check(remarks, (String) "2", "remarks") && success;
                else if (column.equals ("COLUMN2"))
                    success = check(remarks, (String) "Three", "remarks") && success;
                else if (column.equals ("COLUMNXX"))
                  success = check(remarks, (String) "JDBC is fast", "remarks") && success;
                else
                    success = false;  // tighten down testing.
            }

            rs.close ();
            c.close ();
            assertCondition ((rows == 4) && success, message.toString());
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify all null parameters.  Verify that all columns
come back in the library list are returned.
**/
    public void Var004()
    {

        try {

	    int endIndex = 8;
	    if (JDDMDTest.COLLECTION.length() < 8) {
		endIndex = JDDMDTest.COLLECTION.length();
	    }
            ResultSet rs = dmd_.getColumns (null, JDDMDTest.COLLECTION.substring(0,endIndex)+"%", null, null);

            // It is impossible to check that all columns come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;

            while (rs.next ()) {
                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";

                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMN)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS2(COLUMNXX)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMNXX)"))
                    check3 = true;

            }

            rs.close ();
            assertCondition (check1 && check2 && check3);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify null for the catalog pattern.  All matching
tables should be returned.
**/
    public void Var005()
    {
        try {
            message = new StringBuffer();
            messageColumnName="";


            String catalog = getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (null,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "%");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String column = rs.getString ("COLUMN_NAME");
                messageColumnName=column;

                success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;

                column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMN)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS1(COLUMN2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMNXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 10), "success="+success+" check1="+check1+" check2="+check2+" check3="+check3+" rows="+rows+" sb 10 "+message.toString());
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify empty string for the catalog pattern.
No matching columns should be returned.
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getColumns ("",
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE || JDTestDriver.isLUW() ||
                (getDriver() == JDTestDriver.DRIVER_NATIVE && (isJdbc40() || getRelease() >= JDTestDriver.RELEASE_V7R1M0))
                || (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
		assertCondition (rows > 0, "rows = "+rows+" sb > 0  empty string for the catalog parameter");
	    } else {
		assertCondition (rows == 0, "rows = "+rows+" sb 0 empty string for the catalog parameter");
	    }

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify a catalog pattern that matches the catalog
exactly.  All matching columns should be returned.
**/
    public void Var007()
    {
        try {
          String catalog = getExpectedTableCat();

          message = new StringBuffer();
          messageColumnName="";


            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String column = rs.getString ("COLUMN_NAME");
                messageColumnName=column;

                success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;


                column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS1(COLUMN2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS2(COLUMN1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMNXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 10),"success="+success+" check1="+check1+" check2="+check2+" check3="+check3+" rows="+rows+" sb 10 "+message.toString());
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify "localhost" for the catalog pattern.
All matching columns should be returned.
**/
    public void Var008()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4  ||
          ((getRelease() >= JDTestDriver.RELEASE_V7R1M0) &&
              (getDriver() == JDTestDriver.DRIVER_NATIVE) )
              || (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())
             ) {
        notApplicable("COMPATIBILITY \"localhost\" variation ");
      } else {

        try {
          String catalog = getExpectedTableCat();

          message = new StringBuffer();
          messageColumnName="";

            ResultSet rs = dmd_.getColumns ("localhost",
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String column = rs.getString ("COLUMN_NAME");
                messageColumnName=column;

                success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;


                column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS1(COLUMN2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS2(COLUMN1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMNXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 10), "success="+success+" check1="+check1+" check2="+check2+" check3="+check3+" rows="+rows+" sb 10 "+message.toString() );
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
getColumns() - Specify a catalog pattern for which there is a
match.  No matching columns should be returned, since we do not
support catalog pattern.
**/
    public void Var009()
    {
        try {
          String catalog = getExpectedTableCat();

	  ResultSet rs = null;
	  if (JDTestDriver.isLUW()) {
	      rs = dmd_.getColumns ("BOGUSCAT",
				    JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "%");

	  } else {

	     rs = dmd_.getColumns (catalog.substring (0, 4) + "%",
					      JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "%");

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
getColumns() - Specify a catalog pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var010()
    {
        try {
            ResultSet rs = dmd_.getColumns ("BOGUS%",
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "%");

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
getColumns() - Specify null for the schema pattern.  All matching
columns should be returned.
**/
    public void Var011()
    {
        try {
          String catalog = getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog ,
                                            null, "COLUMNS%", "%");

            // It is impossible to check that all columns come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog );
		}

                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS2(COLUMNXX)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMNXX)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMN2)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows >= 10));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify empty string for the schema pattern.
No matching columns should be returned.
**/
    public void Var012()
    {
        try {
          String catalog = getExpectedTableCat(); ;

            ResultSet rs = dmd_.getColumns (catalog ,
                                            "", "COLUMNS%", "%");

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
getColumns() - Specify a schema pattern that matches a schema
exactly.  All matching columns should be returned.
**/
    public void Var013()
    {
        try {
          String catalog = getExpectedTableCat(); ;

            ResultSet rs = dmd_.getColumns (catalog ,
                                            JDDMDTest.COLLECTION2, "COLUMNS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog);
		}

                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMN2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMNXX)"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2
                    && (rows == 2));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify a schema pattern using a percent for which
there is a match.  All matching columns should be returned.
**/
    public void Var014()
    {
        try {
          String catalog = getExpectedTableCat(); ;

            ResultSet rs = dmd_.getColumns (catalog ,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;


		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog );
		}
                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS2(COLUMNXX)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS1(COLUMN1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMN2)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 10));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify a schema pattern using an underscore for which
there is a match.  All matching columns should be returned.
**/
    public void Var015()
    {
        try {
	    String retrievedColumns="";
                  String catalog = getExpectedTableCat(); ;

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_UNDERSCORE, "COLUMNS%", "%");

            // Check for some of the columns.
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog);
		}
                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
		retrievedColumns+=column+"\n";
		if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMN2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMNXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check2 && check3
                    && (rows == 2), "Failed rows="+rows+" sb 2"+
			     " check2 for COLUMNS(COLUMN2)="+check2+
			     " check3 for COLUMNS(COLUMNXX)="+check3+
			     "\nRetrieved colums for "+JDDMDTest.SCHEMAS_UNDERSCORE+" were "+retrievedColumns);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify a schema pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var016()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            "BOGUS%", "COLUMNS%", "%");

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
getColumns() - Specify null for the table pattern.  All matching
columns should be returned.
**/
    public void Var017()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
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

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog);
		}
                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMN)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS1(COLUMN2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMNXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows >= 10));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify empty string for the table pattern.
No matching columns should be returned.
**/
    public void Var018()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "", "%");

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
getColumns() - Specify a table pattern that matches a table
exactly.  All matching columns should be returned.
**/
    public void Var019()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS", "%");

            // Check for the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog);
		}
                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMN2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMN)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMNXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 6));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify a table pattern using a percent for which
there is a match.  All matching columns should be returned.
**/
    public void Var020()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog);
		}
                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMNXX)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS2(COLUMN1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMN2)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 10));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify a table pattern using an underscore for which
there is a match.  All matching columns should be returned.
**/
    public void Var021()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS_", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog);
		}
                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS1(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS2(COLUMNXX)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS2(COLUMN1)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 4));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify a table pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var022()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "BOGUS%", "%");

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
getColumns() - Specify null for the column pattern.  All matching
columns should be returned.
**/
    public void Var023()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", null);

            // It is impossible to check that all columns come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog);
		}

                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMN)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS1(COLUMN2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMNXX)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 10));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify empty string for the column pattern.
No matching columns should be returned.
**/
    public void Var024()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "");

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
getColumns() - Specify a column pattern that matches a column
exactly.  All matching columns should be returned.
**/
    public void Var025()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "COLUMN2");

            // Check for the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog);
		}
                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMN2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS1(COLUMN2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMN2)"))
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
getColumns() - Specify a column pattern using a percent for which
there is a match.  All matching columns should be returned.
**/
    public void Var026()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "COLUMN%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog);
		}
                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMNXX)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS2(COLUMN1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".COLUMNS(COLUMN2)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 10));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify a table pattern using an underscore for which
there is a match.  All matching columns should be returned.
**/
    public void Var027()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "COLUMN_");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getString ("TABLE_CAT").equals (catalog);
		}
                String column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";
                if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS1(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS2(COLUMN1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".COLUMNS(COLUMN2)"))
                    check3 = true;
            }

            rs.close ();
            assertCondition (success && check1 && check2 && check3
                    && (rows == 6));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumns() - Specify a column pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var028()
    {
        try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "COLUMNS%", "BOGUS%");

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
getColumns() - Should throw an exception when the connection
is closed.
**/
    public void Var029()
    {
        try {
            ResultSet resultSet = dmd2_.getColumns (null, null, null, null);
            failed ("Didn't throw SQLException but got "+resultSet);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


    boolean checkExpected(String row[], String [][] expected) {
	for (int i = 1; i < expected.length; i++) {
	    if ( row[2].equals(expected[i][2]) &&
		 row[4].equals(expected[i][4])) {
		// We've found the right row.. it now must match
/*		System.out.println("\nRECEIVED \n"+
				     "*********");
		for(int j = 2; j< row.length; j++)
		    System.out.print(row[j]+": ");
		System.out.println("\nEXPECTED \n"+
				     "*********");
		for(int j = 2; j< row.length; j++)
		    System.out.print(expected[i][j]+": ");
*/
        boolean ok = true;
        for (int j = 2; j < row.length; j++) {
          if (j >= expected[i].length) {
            ok = false;
            message
                .append("Column " + j + " not expected.. Contains " + row[j]);
          } else if ((row[j] == null) || (expected[i][j] == null)) {
            // Check for null using comparison.
            // Note: FindBugs will think this is an error
            if ((row[j] != null) || (null != expected[i][j])) {
              ok = false;
              message.append("Column " + j + ":" + expected[0][j] + " \""
                  + row[j] + "\" sb \"" + expected[i][j] + "\"\n");
            }
          } else {
            if (!(row[j].equals(expected[i][j]))) {
              ok = false;
              message.append("Column " + j + ":" + expected[0][j] + " \""
                  + row[j] + "\" sb \"" + expected[i][j] + "\"\n");
            }
          }
        }
    		return ok;
	    }
	}
	message.append("Row not found for Column2="+row[2]+" column4="+row[4]);
	return false;
    }




/**
 getColumns() -- Verify that getString can be done on all the columns and the result
 is as expected.
**/
    public void Var030()
    {
	/** @K2 **/
	   // String systemName = ((AS400JDBCConnection)connection_).getSystem().getSystemName();    //@K1A
	String nativeExpected30[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN",      "4",         "INTEGER",   "10",             "0",             "0",              "10",              "0",         null,           null,         "0",             "0",                "0",                 "1",                "NO", null, null, null, null},
	    {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN1",     "93",        "TIMESTAMP", "26",             "0",		"6"/*"0"*/,	   "0",		    "1",          null/*"2"*/,  null,         "0",             "0",                "0",                 "2",                "YES", null, null, null, null },
	    {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN2",     "1",         "CHAR",      "10",             "0",             "0",               "0",             "0",         null/*"Three"*/,null,	      "0",             "0",                "10",                "3",                 "NO", null, null, null, null  },
	    {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMNXX",    "3",         "DECIMAL",   "6",              "0",             "2",              "10",            "1",          null/*"JDBC is fast"*/,null,"0",             "0",                "0",                 "4",                 "YES", null, null, null, null  },
	    {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMN2",     "3",         "DECIMAL",   "5",              "0",             "0",              "10",            "1",           null,         null,         "0",             "0",                "0",                 "1",                 "YES", null, null, null, null  },
	    {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMNXX",    "1",          "CHAR",     "10",             "0",             "0",               "0",             "1",           null,         null,         "0",             "0",                "10",                "2",                  "YES", null, null, null, null  }
	};	// @K3

     String nativeExpected3016[][] = {
        {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
        {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN",      "4",         "INTEGER",   "10",             "4",             "0",              "10",              "0",         null,           null,       "4",            null,                null,               "1",                "NO", null, null, null, null, "NO"},
        {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN1",     "93",        "TIMESTAMP", "26",             "16",            "6"/*"0"*/,        null,             "1",          null/*"2"*/,  null,         "9",            "3",                 null,                "2",                "YES", null, null, null, null, "NO" },
        {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN2",     "1",         "CHAR",      "10",             "10",            null,              null,            "0",         null/*"Three"*/,null,        "1",            null,               "10",                "3",                 "NO", null, null, null, null, "NO"  },
        {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMNXX",    "3",         "DECIMAL",   "6",              "8",             "2",              "10",            "1",          null/*"JDBC is fast"*/,null, "3",            null,                null,               "4",                "YES", null, null, null, null, "NO"  },
        {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMN2",     "3",         "DECIMAL",   "5",              "7",             "0",              "10",            "1",           null,         null,         "3",            null,                null,               "1",                "YES", null, null, null, null, "NO"  },
        {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMNXX",    "1",          "CHAR",     "10",            "10",             null,             null,            "1",           null,         null,         "1",            null,                "10",                "2",                "YES", null, null, null, null, "NO"  }


     };

     
     String nativeExpectedVar30Jdk17[][] = {
         {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN"},
         {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN",      "4",         "INTEGER",   "10",             "4",             "0",              "10",              "0",         null,           null,       "4",            null,                null,               "1",                "NO", null, null, null, null, "NO", "NO"},
         {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN1",     "93",        "TIMESTAMP", "26",             "16",            "6"/*"0"*/,        null,             "1",          null/*"2"*/,  null,         "9",            "3",                 null,                "2",                "YES", null, null, null, null, "NO", "NO" },
         {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN2",     "1",         "CHAR",      "10",             "10",            null,              null,            "0",         null/*"Three"*/,null,        "1",            null,               "10",                "3",                 "NO", null, null, null, null, "NO", "NO"  },
         {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMNXX",    "3",         "DECIMAL",   "6",              "8",             "2",              "10",            "1",          null/*"JDBC is fast"*/,null, "3",            null,                null,               "4",                "YES", null, null, null, null, "NO", "NO"  },
         {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMN2",     "3",         "DECIMAL",   "5",              "7",             "0",              "10",            "1",           null,         null,         "3",            null,                null,               "1",                "YES", null, null, null, null, "NO", "NO"  },
         {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMNXX",    "1",          "CHAR",     "10",            "10",             null,             null,            "1",           null,         null,         "1",            null,                "10",                "2",                "YES", null, null, null, null, "NO", "NO"  }


      };



        String jccExpected30[][] = {
        {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
        {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN",      "4",         "INTEGER",   "10",          "4",             "0",              "10",              "0",         null,           null,         "4",             null,                null,                 "1",                "NO", null, null, null, null},
        {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN1",     "93",        "TIMESTAMP", "26",          "0",          "6"/*"0"*/,        "0",             "1",          null/*"2"*/,  null,         "0",             "0",                "0",                 "2",                "YES", null, null, null, null },
        {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN2",     "1",         "CHAR",      "10",             "0",             "0",               "0",             "0",         null/*"Three"*/,null,          "0",             "0",                "10",                "3",                 "NO", null, null, null, null  },
        {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMNXX",    "3",         "DECIMAL",   "6",              "0",             "2",              "10",            "1",          null/*"JDBC is fast"*/,null,"0",             "0",                "0",                 "4",                 "YES", null, null, null, null  },
        {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMN2",     "3",         "DECIMAL",   "5",              "0",             "0",              "10",            "1",           null,         null,         "0",             "0",                "0",                 "1",                 "YES", null, null, null, null  },
        {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMNXX",    "1",          "CHAR",     "10",             "0",             "0",               "0",             "1",           null,         null,         "0",             "0",                "10",                "2",                  "YES", null, null, null, null  }
                };      // @K3



	String toolboxExpected30[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION,          "COLUMNS",    "COLUMN",      "4",         "INTEGER",   "10",          "0",             "0",              "10",              "0",         "",           null,         "0",             "0",                "-1",                 "1",                "NO", null, null, null, "0"},
	    {"", systemName, JDDMDTest.COLLECTION,          "COLUMNS",    "COLUMN1",     "93",        "TIMESTAMP", "26",          "0",             "6",               "0",             "1",          "8",           null,         "0",             "0",                "-1",                 "2",                "YES", null, null, null, "0" },
	    {"", systemName, JDDMDTest.COLLECTION,          "COLUMNS",    "COLUMN2",     "1",         "CHAR",      "10",          "0",             "0",               "0",             "0",          "Nine",           null,         "0",             "0",                "10",                 "3",                 "NO", null, null, null, "0"  },
	    {"", systemName, JDDMDTest.COLLECTION,          "COLUMNS",    "COLUMNXX",    "3",         "DECIMAL",   "6",           "0",             "2",               "10",            "1",          "SQL/400",           null,         "0",             "0",                "-1",                 "4",                 "YES", null, null, null, "0"  },
	    {"", systemName, JDDMDTest.COLLECTION2,          "COLUMNS",    "COLUMN2",     "3",         "DECIMAL",   "5",           "0",             "0",               "10",            "1",           "",          null,         "0",             "0",                "-1",                 "1",                 "YES", null, null, null, "0"  },
	    {"", systemName, JDDMDTest.COLLECTION2,          "COLUMNS",    "COLUMNXX",    "1",          "CHAR",     "10",          "0",             "0",               "0",             "1",           "",          null,         "0",             "0",                "10",                 "2",                  "YES", null, null, null, "0"  }
	};                      //@K1A

        String toolboxExpectedV5R5[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
            {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN",      "4",         "INTEGER",   "10",          "0",             "0",              "10",             "0",           "",          "",         "0",             "0",                "-1",                 "1",                "NO", null, null, null, "0"},
            {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN1",     "93",        "TIMESTAMP", "26",          "0",             "6",               "0",             "1",          "8",          "",         "0",             "0",                "-1",                 "2",                "YES", null, null, null, "0" },
            {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN2",     "1",         "CHAR",      "10",          "0",             "0",               "0",             "0",       "Nine",          "",         "0",             "0",                "10",                 "3",                 "NO", null, null, null, "0"  },
            {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMNXX",    "3",         "DECIMAL",   "6",           "0",             "2",               "10",            "1",    "SQL/400",          "",         "0",             "0",                "-1",                 "4",                 "YES", null, null, null, "0"  },
            {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMN2",     "3",         "DECIMAL",   "5",           "0",             "0",               "10",            "1",           "",          "",         "0",             "0",                "-1",                 "1",                 "YES", null, null, null, "0"  },
            {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMNXX",    "1",          "CHAR",     "10",          "0",             "0",               "0",             "1",           "",          "",         "0",             "0",                "10",                 "2",                  "YES", null, null, null, "0"  }
        };                      //@M1A

        String toolboxJDBC40Expected[][] = {
                {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN",      "4",         "INTEGER",   "10",          "0",             "0",              "10",             "0",           "",          null,         "0",             "0",                "-1",                 "1",                "NO", null, null, null, "0", ""},
                {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN1",     "93",        "TIMESTAMP", "26",          "0",             "6",               "0",             "1",          "8",          null,         "0",             "0",                "-1",                 "2",                "YES", null, null, null, "0", "" },
                {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN2",     "1",         "CHAR",      "10",          "0",             "0",               "0",             "0",       "Nine",          null,         "0",             "0",                "10",                 "3",                 "NO", null, null, null, "0", ""  },
                {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMNXX",    "3",         "DECIMAL",   "6",           "0",             "2",               "10",            "1",    "SQL/400",          null,         "0",             "0",                "-1",                 "4",                 "YES", null, null, null, "0", ""  },
                {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMN2",     "3",         "DECIMAL",   "5",           "0",             "0",               "10",            "1",           "",          null,         "0",             "0",                "-1",                 "1",                 "YES", null, null, null, "0", ""  },
                {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMNXX",    "1",          "CHAR",     "10",          "0",             "0",               "0",             "1",           "",          null,         "0",             "0",                "10",                 "2",                  "YES", null, null, null, "0", ""  }
            };   //@pda

        String toolboxJDBC40ExpectedV5R5[][] = {
                {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN",      "4",         "INTEGER",   "10",          "0",             "0",              "10",             "0",           "",          "",         "0",             "0",                "-1",                 "1",                "NO", null, null, null, "0", ""},
                {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN1",     "93",        "TIMESTAMP", "26",          "0",             "6",               "0",             "1",          "8",          "",         "0",             "0",                "-1",                 "2",                "YES", null, null, null, "0", "" },
                {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMN2",     "1",         "CHAR",      "10",          "0",             "0",               "0",             "0",       "Nine",          "",         "0",             "0",                "10",                 "3",                 "NO", null, null, null, "0", ""  },
                {"", systemName, JDDMDTest.COLLECTION,    "COLUMNS",    "COLUMNXX",    "3",         "DECIMAL",   "6",           "0",             "2",               "10",            "1",    "SQL/400",          "",         "0",             "0",                "-1",                 "4",                 "YES", null, null, null, "0", ""  },
                {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMN2",     "3",         "DECIMAL",   "5",           "0",             "0",               "10",            "1",           "",          "",         "0",             "0",                "-1",                 "1",                 "YES", null, null, null, "0", ""  },
                {"", systemName, JDDMDTest.COLLECTION2,   "COLUMNS",    "COLUMNXX",    "1",          "CHAR",     "10",          "0",             "0",               "0",             "1",           "",          "",         "0",             "0",                "10",                 "2",                  "YES", null, null, null, "0", ""  }
            };   //@pda


	try {
          String catalog =  getExpectedTableCat();

	    message = new StringBuffer();
	    ResultSet rs = dmd_.getColumns (catalog,
					    JDDMDTest.SCHEMAS_PERCENT, "COLUMNS", "%");

	    ResultSetMetaData rsmd = rs.getMetaData();
	    int count = rsmd.getColumnCount();
	    String [][] expected;
	    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {     //@K1A
	    	if((getJdbcLevel() >= 4)){ //@pda
	    		if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	    			message.append("Expected = toolboxJDBC40Expected\n");//@pda
	    			expected = toolboxJDBC40Expected;  //@pda
	    		}else{
	    			message.append("Expected = toolboxJDBC40ExpectedV5R5\n");//@pda
	    			expected = toolboxJDBC40ExpectedV5R5;  //@pda
	    		} //@pda
	    	} //@pda
                else if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {   // @M1A
                    message.append("Expected = toolboxExpected30\n");
                    expected = toolboxExpected30;  //@K1a
                } else  {                                        // @M1A
                  message.append("Expected = toolboxExpectedV5R5\n");
                    expected = toolboxExpectedV5R5;              // @M1A
                }
            } else                                                //@K1A
	    {						// @K3
		if ((getRelease() >= JDTestDriver.RELEASE_V7R1M0) || JDTestDriver.isLUW() ||
		    (getJdbcLevel() >= 4) ) {
		    if (getJdbcMajorMinorLevel () >= 41) { 
	        message.append("Expected = nativeExpectedVar30Jdk17\n");
	        expected = nativeExpectedVar30Jdk17;
		    } else { 		    
		    message.append("Expected = nativeExpected3016\n");
		    expected = nativeExpected3016;
		    }
		} else if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    message.append("Expected = jccExpected30\n");
		    expected = jccExpected30;
		}
		else {
		    expected = nativeExpected30;
		}

		if(var003Executed){			// @K3
		    expected[2][12] = "2";		// @K3
		    expected[3][12] = "Three";		// @K3
		    expected[4][12] = "JDBC is fast";	// @K3
		}					// @K3
	    }


	    String [] oneRow = new String[count+1];

	    boolean success = true;
	    int rows = 0;

	    while (rs.next ()) {
		++rows;

		//
                // For now print the content of the row -- we'll have to verify this once it is working
                //

		for(int i=1; i <= count; i++)
		{
		    // System.out.println("Getting "+i);
		    oneRow[i]=rs.getString(i);
		}

                boolean thisOk =  true;
		thisOk = checkExpected(oneRow, expected );
		if (!thisOk) {
		    message.append("Line did not verify. Column count is "+count+"\n");
		    for (int i=1; i <= count; i++) {
			message.append("\"" + oneRow[i] + "\",  ");
		    }
                    message.append("\n");

		    success=false;

		}
	    }
	    if (!success) {
		// Dump the expected information

	    }
	    rs.close ();
	    assertCondition (success
			     && (rows == 6), "Native driver updated in V5R3 to return correct information.. If needed toolbox can create expected array \n"+message.toString());
	} catch (Exception e) {
	    failed (e, "Unexpected Exception");
	}

    }





/**
 getColumns() -- Verify the results for LOB columns.
**/
    public void Var031()
    {
	/** @K2 **/
        //String systemName = ((AS400JDBCConnection)connection_).getSystem().getSystemName();    //@K1A
        String jdbc40Expected31[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "CLOB100",     "2005",      "CLOB",      "100",         "100",             null,             null,             "1",             null,         null,         "-99",             null,               "100",               "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "DBCLOB200",   "2005",      "DBCLOB",    "200",         "400",             null,             null,             "1",             null,         null,         "-350",             null,               "400",               "2",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "BLOB400",     "2004",      "BLOB",      "400",         "400",             null,             null,             "1",             null,         null,         "-98",              null,                "400",               "3",                "YES",          null,       null,        null,      null, "NO"},
        };

        String jdbc41Expected31[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN"},
            {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "CLOB100",     "2005",      "CLOB",      "100",         "100",             null,             null,             "1",             null,         null,         "-99",             null,               "100",               "1",                "YES",          null,       null,        null,      null, "NO", "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "DBCLOB200",   "2005",      "DBCLOB",    "200",         "400",             null,             null,             "1",             null,         null,         "-350",             null,               "400",               "2",                "YES",          null,       null,        null,      null, "NO", "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "BLOB400",     "2004",      "BLOB",      "400",         "400",             null,             null,             "1",             null,         null,         "-98",              null,                "400",               "3",                "YES",          null,       null,        null,      null, "NO", "NO"},
        };


        String luwExpected31[][] = { /*DIfferences marked by D*/
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "CLOB100",     "2005",      "CLOB",      "100",         "100",             null,             null,             "1",             null,         null,         "-99",             null,               "100",               "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "DBCLOB200",   "2005",      "CLOB",      "200",         "200",             null,             null,             "1",             null,         null,         "-99",             null,               "200",               "2",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "BLOB400",     "2004",      "BLOB",      "400",         "400",             null,             null,             "1",             null,         null,         "-98",              null,                "400",               "3",                "YES",          null,       null,        null,      null, "NO"},
        };


      String nativeExpected31[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "CLOB100",     "2005",      "CLOB",      "100",         "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "100",               "1",                "YES",          null,       null,        null,      null},
	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "DBCLOB200",   "2005",      "DBCLOB",    "200",         "0",             "0",              "0",              "1",             null,           null,         "0",             "0",                "400",               "2",                "YES",          null,       null,        null,      null },
	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "BLOB400",     "2004",      "BLOB",      "400",         "0",             "0",              "0",              "1",             null,           null,         "0",             "0",                "400",               "3",                "YES",          null,       null,        null,      null  },
	};

        String toolboxExpected31[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "CLOB100",     "2005",      "CLOB",      "100",         "0",             "0",              "0",              "1",             "",         null,         "0",             "0",                "100",               "1",                "YES",          null,       null,        null,      "0"},
	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "DBCLOB200",   "2005",      "DBCLOB",    "200",         "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "200",               "2",                "YES",          null,       null,        null,      "0" },
	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "BLOB400",     "2004",      "BLOB",      "400",         "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "400",               "3",                "YES",          null,       null,        null,      "0"  },
	};  //@K1A

        String toolboxExpectedV5R5[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "CLOB100",     "2005",      "CLOB",      "100",         "0",             "0",              "0",              "1",             "",         "",         "0",             "0",                "100",               "1",                "YES",          null,       null,        null,      "0"},
	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "DBCLOB200",   "2005",      "DBCLOB",    "200",         "0",             "0",              "0",              "1",             "",         "",         "0",             "0",                "200",               "2",                "YES",          null,       null,        null,      "0" },
	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "BLOB400",     "2004",      "BLOB",      "400",         "0",             "0",              "0",              "1",             "",         "",         "0",             "0",                "400",               "3",                "YES",          null,       null,        null,      "0"  },
	};  //@M1A

        String toolboxJDBC40Expected[][] = {
        	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
        	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "CLOB100",     "2005",      "CLOB",      "100",         "0",             "0",              "0",              "1",             "",         null,         "0",             "0",                "100",               "1",                "YES",          null,       null,        null,      "0", ""},
        	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "DBCLOB200",   "2005",      "DBCLOB",    "200",         "0",             "0",              "0",              "1",             "",         null,         "0",             "0",                "200",               "2",                "YES",          null,       null,        null,      "0", "" },
        	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "BLOB400",     "2004",      "BLOB",      "400",         "0",             "0",              "0",              "1",             "",         null,         "0",             "0",                "400",               "3",                "YES",          null,       null,        null,      "0", "" },
        	}; //@pda

        String toolboxJDBC40ExpectedV5R5[][] = {
        	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
        	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "CLOB100",     "2005",      "CLOB",      "100",         "0",             "0",              "0",              "1",             "",         "",         "0",             "0",                "100",               "1",                "YES",          null,       null,        null,      "0", ""},
        	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "DBCLOB200",   "2005",      "DBCLOB",    "200",         "0",             "0",              "0",              "1",             "",         "",         "0",             "0",                "200",               "2",                "YES",          null,       null,        null,      "0", "" },
        	    {"", systemName, JDDMDTest.COLLECTION, "LOBCOLUMNS", "BLOB400",     "2004",      "BLOB",      "400",         "0",             "0",              "0",              "1",             "",         "",         "0",             "0",                "400",               "3",                "YES",          null,       null,        null,      "0", "" },
        	}; //@pda

        String[][] expected31 = nativeExpected31;
	message =  new StringBuffer();

	if (  JDTestDriver.isLUW()) {
	    message.append("Expected = luwExpected31\n");
	    expected31=luwExpected31;

	} else if ( (getJdbcLevel() >= 4   && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) ||   //@N1C
	        ( (getDriver() == JDTestDriver.DRIVER_NATIVE) &&
	                (getRelease() >= JDTestDriver.RELEASE_V7R1M0) )
	                || (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()) ) {
	    if (getJdbcMajorMinorLevel() >= 41) { 
	      message.append("Expected = jdbc41Expected31\n");
	      expected31=jdbc41Expected31;
	      
	    } else { 
	      message.append("Expected = jdbc40Expected31\n");
	      expected31=jdbc40Expected31;
	    }
	} else {
	    if ( getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
	        if((getJdbcLevel() >= 4)){ //@pda
	            if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	                message.append("Expected = toolboxJDBC40Expected\n");//@pda
	                expected31 = toolboxJDBC40Expected;  //@pda
	            }else{
	                message.append("Expected = toolboxJDBC40ExpectedV5R5\n");//@pda
	                expected31 = toolboxJDBC40ExpectedV5R5;  //@pda
	            } //@pda
	        } //@pda
	        else if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {    // @M1A
	            message.append("Expected = toolboxExpected31\n");
	            expected31=toolboxExpected31;
	        } else {                                              // @M1A
	            message.append("Expected = toolboxExpectedV5R5\n");
	            expected31 = toolboxExpectedV5R5;             // @M1A
	        }
	    } else {
	        message.append("Expected = nativeExpected31\n");

	    }
	}
	try {
          String catalog =  getExpectedTableCat();

	    ResultSet rs = dmd_.getColumns (catalog,
					    JDDMDTest.SCHEMAS_PERCENT, "LOBCOLUMNS", "%");

	    ResultSetMetaData rsmd = rs.getMetaData();
	    int count = rsmd.getColumnCount();
	    String [] oneRow = new String[count+1];

	    boolean success = true;
	    int rows = 0;
	    while (rs.next ()) {
		++rows;

		//
		// Get the row contents
                //
		for(int i=1; i <= count; i++)
		{
		    // System.out.println("Getting "+i);
		    oneRow[i]=rs.getString(i);
		}
		//
		// Check for a matching row
                //
                boolean thisOk =  true;
                thisOk = checkExpected(oneRow, expected31);
		if (!thisOk) {
		    message.append("Line did not verify. Column count is "+count+"\n");
		    for (int i=1; i <= count; i++) {
			message.append("\"" + oneRow[i] + "\",  ");
		    }
		    message.append("\n");
		    success=false;
		}
	    }

	    rs.close ();
	    assertCondition (success
			     && (rows == 3), "rows="+rows+" sb 3 Native Driver added V5R3 to check column information for Lobs -- If toolbox different, create a toolbox expected array\n"+message.toString());
	} catch (Exception e) {
	    failed (e, "Unexpected Exception "+message.toString());
	}

    }

/**
 getColumns() -- Verify the results for BIGINT/SMALLINT/DATE/TIME columns.  @E2
**/
    public void Var032()
    {
	/** @K2 **/
        //String systemName = ((AS400JDBCConnection)connection_).getSystem().getSystemName();
        String jdbc40Expected32[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTO_INCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL1",     "-5",      "BIGINT",        "19",         "8",             "0",              "10",              "1",             null,         null,         "-5",             null,                null,               "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL2",    "5",    "SMALLINT",           "5",         "2",             "0",              "10",              "1",             null,           null,       "5",             null,               null,              "2",                "YES",          null,       null,        null,      null , "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL3",     "91",      "DATE",          "10",         "6",             "0",              null,             "1",             null,           null,         "9",             "1",                null,             "3",                "YES",          null,       null,        null,      null , "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL4",     "92",      "TIME",           "8",         "6",             "0",              null,             "1",             null,           null,         "9",             "2",                null,               "4",                "YES",          null,       null,        null,      null , "NO" },
        };

        String jdbc41Expected32[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTO_INCREMENT", "IS_GENERATEDCOLUMN"},
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL1",     "-5",      "BIGINT",        "19",         "8",             "0",              "10",              "1",             null,         null,         "-5",             null,                null,               "1",                "YES",          null,       null,        null,      null, "NO", "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL2",    "5",    "SMALLINT",           "5",         "2",             "0",              "10",              "1",             null,           null,       "5",             null,               null,              "2",                "YES",          null,       null,        null,      null , "NO", "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL3",     "91",      "DATE",          "10",         "6",             "0",              null,             "1",             null,           null,         "9",             "1",                null,             "3",                "YES",          null,       null,        null,      null , "NO", "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL4",     "92",      "TIME",           "8",         "6",             "0",              null,             "1",             null,           null,         "9",             "2",                null,               "4",                "YES",          null,       null,        null,      null , "NO", "NO" },
        };


        String luwExpected32[][] = {  /*Difference is D*/
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTO_INCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL1",     "-5",      "BIGINT",        "19",         "8",             "0",              "10",              "1",             null,         null,         "-5",             null,                null,               "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL2",    "5",    "SMALLINT",           "5",         "2",             "0",              "10",              "1",             null,           null,       "5",             null,               null,              "2",                "YES",          null,       null,        null,      null , "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL3",     "91",      "DATE",          "10",         "6",             /*D*/ null,        null,             "1",             null,           null,         "9",             "1",                null,             "3",                "YES",          null,       null,        null,      null , "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL4",     "92",      "TIME",           "8",         "6",             "0",              null,             "1",             null,           null,         "9",             "2",                null,               "4",                "YES",          null,       null,        null,      null , "NO" },
        };


	String nativeExpected32[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL1",     "-5",      "BIGINT",        "19",         "0",             "0",              "10",              "1",             null,         null,         "0",             "0",                "0",               "1",                "YES",          null,       null,        null,      null},
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL2",    "5",    "SMALLINT",           "5",         "0",             "0",              "10",              "1",             null,           null,         "0",             "0",                "0",               "2",                "YES",          null,       null,        null,      null },
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL3",     "91",      "DATE",          "10",         "0",             "0",              "0",              "1",             null,           null,         "0",             "0",                "0",               "3",                "YES",          null,       null,        null,      null  },
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL4",     "92",      "TIME",           "8",         "0",             "0",              "0",              "1",             null,           null,         "0",             "0",                "0",               "4",                "YES",          null,       null,        null,      null  },
	};

        String toolboxExpected32[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL1",     "-5",      "BIGINT",        "19",         "0",             "0",              "10",              "1",             "",         null,         "0",             "0",                "-1",               "1",                "YES",          null,       null,        null,      "0"},
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL2",    "5",    "SMALLINT",           "5",         "0",             "0",              "10",              "1",             "",           null,         "0",             "0",                "-1",               "2",                "YES",          null,       null,        null,      "0" },
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL3",     "91",      "DATE",          "10",         "0",             "0",              "0",              "1",              "",           null,         "0",             "0",                "-1",               "3",                "YES",          null,       null,        null,      "0"  },
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL4",     "92",      "TIME",           "8",         "0",             "0",              "0",              "1",              "",           null,         "0",             "0",                "-1",               "4",                "YES",          null,       null,        null,      "0"  },
	};

        String toolboxExpectedV5R5[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL1",     "-5",      "BIGINT",        "19",         "0",             "0",              "10",              "1",             "",         "",         "0",             "0",                "-1",               "1",                "YES",          null,       null,        null,      "0"},
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL2",    "5",    "SMALLINT",           "5",         "0",             "0",              "10",              "1",             "",           "",         "0",             "0",                "-1",               "2",                "YES",          null,       null,        null,      "0" },
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL3",     "91",      "DATE",          "10",         "0",             "0",              "0",              "1",              "",           "",         "0",             "0",                "-1",               "3",                "YES",          null,       null,        null,      "0"  },
	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL4",     "92",      "TIME",           "8",         "0",             "0",              "0",              "1",              "",           "",         "0",             "0",                "-1",               "4",                "YES",          null,       null,        null,      "0"  },
	};       // @M1A

        String toolboxJDBC40Expected[][] = {
        	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTO_INCREMENT"},
        	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL1",     "-5",      "BIGINT",        "19",         "0",             "0",              "10",              "1",             "",         null,         "0",             "0",                "-1",               "1",                "YES",          null,       null,        null,      "0", ""},
        	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL2",    "5",    "SMALLINT",           "5",         "0",             "0",              "10",              "1",             "",           null,         "0",             "0",                "-1",               "2",                "YES",          null,       null,        null,      "0", "" },
        	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL3",     "91",      "DATE",          "10",         "0",             "0",              "0",              "1",              "",           null,         "0",             "0",                "-1",               "3",                "YES",          null,       null,        null,      "0", ""  },
        	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL4",     "92",      "TIME",           "8",         "0",             "0",              "0",              "1",              "",           null,         "0",             "0",                "-1",               "4",                "YES",          null,       null,        null,      "0", ""  },
        	};   //@pda

        String toolboxJDBC40ExpectedV5R5[][] = {
        	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTO_INCREMENT"},
        	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL1",     "-5",      "BIGINT",        "19",         "0",             "0",              "10",              "1",             "",         "",         "0",             "0",                "-1",               "1",                "YES",          null,       null,        null,      "0", ""},
        	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL2",    "5",    "SMALLINT",           "5",         "0",             "0",              "10",              "1",             "",           "",         "0",             "0",                "-1",               "2",                "YES",          null,       null,        null,      "0", "" },
        	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL3",     "91",      "DATE",          "10",         "0",             "0",              "0",              "1",              "",           "",         "0",             "0",                "-1",               "3",                "YES",          null,       null,        null,      "0", ""  },
        	    {"", systemName, JDDMDTest.COLLECTION, "INTCOLUMNS", "COL4",     "92",      "TIME",           "8",         "0",             "0",              "0",              "1",              "",           "",         "0",             "0",                "-1",               "4",                "YES",          null,       null,        null,      "0", ""  },
        	};   //@pda

	message = new StringBuffer();

        String[][] expected32 = nativeExpected32;
	if ( JDTestDriver.isLUW()) {
          message.append("Expected = luwExpected32\n");
          expected32=luwExpected32;

	} else if ( (getJdbcLevel() >= 4   && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) ||   //@N1C
                ( (getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                (getRelease() >= JDTestDriver.RELEASE_V7R1M0) )
               || ( (getDriver() == JDTestDriver.DRIVER_TOOLBOX) && isSysibmMetadata()) ) {
	    if (getJdbcMajorMinorLevel() >= 41) {
        message.append("Expected = jdbc40Expected32\n");
        expected32=jdbc41Expected32;
	      
	    } else { 
          message.append("Expected = jdbc40Expected32\n");
          expected32=jdbc40Expected32;
	    }
        } else {
          if ( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        	  if((getJdbcLevel() >= 4)){ //@pda
        		  if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {
  	    			message.append("Expected = toolboxJDBC40Expected\n");//@pda
  	    			expected32 = toolboxJDBC40Expected;  //@pda
  	    		}else{
  	    			message.append("Expected = toolboxJDBC40ExpectedV5R5\n");//@pda
  	    			expected32 = toolboxJDBC40ExpectedV5R5;  //@pda
  	    		} //@pda
              } //@pda
              else if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {    // @M1A
                  expected32=toolboxExpected32;
              } else {                                               // @M1A
                  expected32 = toolboxExpectedV5R5;             // @M1A
              }
          }
        }

	try {
          String catalog =  getExpectedTableCat();

	    ResultSet rs = dmd_.getColumns (catalog,
					    JDDMDTest.SCHEMAS_PERCENT, "INTCOLUMNS", "%");

	    ResultSetMetaData rsmd = rs.getMetaData();
	    int count = rsmd.getColumnCount();
	    String [] oneRow = new String[count+1];

	    boolean success = true;
	    int rows = 0;
	    while (rs.next ()) {
		++rows;

		//
		// Get the row contents
                //
		for(int i=1; i <= count; i++)
		{
		   //System.out.println("Getting "+i);
		    oneRow[i]=rs.getString(i);
		}
		//
		// Check for a matching row
                //
                boolean thisOk =  true;
                thisOk = checkExpected(oneRow, expected32);
		if (!thisOk) {
		    message.append("Line did not verify. Column count is "+count+"\n");
		    for (int i=1; i <= count; i++) {
			message.append("\"" + oneRow[i] + "\",  ");
		    }
		    message.append("\n");
		    success=false;
		}
	    }

	    rs.close ();
	    assertCondition (success
			     && (rows == 4), "rows="+rows+" sb 4 Native Driver added V5R3 to check column information for INTS -- If toolbox different, create a toolbox expected array\n "+message.toString());
	} catch (Exception e) {
	    failed (e, "Unexpected Exception");
	}

    }

/**
 getColumns() -- Verify the results for DOUBLE/FLOAT/REAL/NUMERIC columns.  @E2
**/
    public void Var033()
    {
	/** @K2 **/
        //String systemName = ((AS400JDBCConnection)connection_).getSystem().getSystemName();    //@K1A
        String jdbc40Expected33[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL1",     "8",          "DOUBLE",      "52",         "8",             null,              "2",              "1",             null,         null,          "8",             null,               null,              "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL2",     "7",          "REAL",        "13",         "4",             null,              "2",              "1",             null,           null,        "7",             null,               null,              "2",                "YES",          null,       null,        null,      null, "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL3",     "7",          "REAL",        "24",         "4",             null,             "2",              "1",             null,           null,         "7",             null,                null,              "3",                "YES",          null,       null,        null,      null, "NO"  },
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL4",     "2",          "NUMERIC",      "6",         "8",             "2",              "10",              "1",             null,           null,        "2",             null,               null,              "4",                "YES",          null,       null,        null,      null, "NO"  },
        };

        String jdbc41Expected33[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN"},
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL1",     "8",          "DOUBLE",      "52",         "8",             null,              "2",              "1",             null,         null,          "8",             null,               null,              "1",                "YES",          null,       null,        null,      null, "NO", "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL2",     "7",          "REAL",        "13",         "4",             null,              "2",              "1",             null,           null,        "7",             null,               null,              "2",                "YES",          null,       null,        null,      null, "NO", "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL3",     "7",          "REAL",        "24",         "4",             null,             "2",              "1",             null,           null,         "7",             null,                null,              "3",                "YES",          null,       null,        null,      null, "NO" , "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL4",     "2",          "NUMERIC",      "6",         "8",             "2",              "10",              "1",             null,           null,        "2",             null,               null,              "4",                "YES",          null,       null,        null,      null, "NO" , "NO" },
        };


        String luwExpected33[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL1",     "8",          "DOUBLE",   /*D*/ "53",         "8",             null,              "2",              "1",             null,         null,          "8",             null,               null,              "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL2",     "7",          "REAL",    /*D*/  "24",         "4",             null,              "2",              "1",             null,           null,        "7",             null,               null,              "2",                "YES",          null,       null,        null,      null, "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL3",     "7",          "REAL",        "24",         "4",             null,             "2",              "1",             null,           null,         "7",             null,                null,              "3",                "YES",          null,       null,        null,      null, "NO"  },
            {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL4",     "3",          "DECIMAL",      "6",         "8",             "2",              "10",              "1",             null,           null,        "3",             null,               null,              "4",                "YES",          null,       null,        null,      null, "NO"  },
        };



	String nativeExpected33[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL1",     "8",      "FLOAT",       "53",         "0",             "0",               "2",              "1",             null,         null,         "0",             "0",                "0",               "1",                "YES",          null,       null,        null,      null},
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL2",    "8",    "FLOAT",          "53",         "0",             "0",               "2",              "1",             null,           null,         "0",             "0",                "0",               "2",                "YES",          null,       null,        null,      null },
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL3",     "7",      "REAL",         "24",         "0",             "0",              "2",              "1",             null,           null,         "0",             "0",                "0",               "3",                "YES",          null,       null,        null,      null  },
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL4",     "2",      "NUMERIC",         "6",         "0",             "2",              "10",              "1",             null,           null,         "0",             "0",                "0",               "4",                "YES",          null,       null,        null,      null  },
	};

        String toolboxExpected33[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL1",     "8",      "FLOAT",       "53",         "0",             "0",              "2",              "1",             "",         null,         "0",             "0",                "-1",               "1",                "YES",          null,       null,        null,      "0"},
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL2",     "7",      "REAL",          "24",         "0",             "0",              "2",              "1",              "",           null,         "0",             "0",                "-1",               "2",                "YES",          null,       null,        null,      "0" },
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL3",     "7",      "REAL",         "24",         "0",             "0",              "2",              "1",             "",           null,         "0",             "0",                "-1",               "3",                "YES",          null,       null,        null,      "0"  },
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL4",     "2",      "NUMERIC",         "6",         "0",             "2",              "10",              "1",          "",           null,         "0",             "0",                "-1",               "4",                "YES",          null,       null,        null,      "0"  },
	};  //@K1A

        String toolboxExpectedV5R5[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL1",     "8",      "FLOAT",       "53",         "0",             "0",              "2",              "1",             "",         "",         "0",             "0",                "-1",               "1",                "YES",          null,       null,        null,      "0"},
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL2",     "7",      "REAL",          "24",         "0",             "0",              "2",              "1",              "",           "",         "0",             "0",                "-1",               "2",                "YES",          null,       null,        null,      "0" },
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL3",     "7",      "REAL",         "24",         "0",             "0",              "2",              "1",             "",           "",         "0",             "0",                "-1",               "3",                "YES",          null,       null,        null,      "0"  },
	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL4",     "2",      "NUMERIC",         "6",         "0",             "2",              "10",              "1",          "",           "",         "0",             "0",                "-1",               "4",                "YES",          null,       null,        null,      "0"  },
	};  //@M1A

        String toolboxJDBC40Expected[][] = {
        	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
        	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL1",     "8",      "FLOAT",       "53",         "0",             "0",              "2",              "1",             "",         null,         "0",             "0",                "-1",               "1",                "YES",          null,       null,        null,      "0", ""},
        	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL2",     "7",      "REAL",          "24",         "0",             "0",              "2",              "1",              "",           null,         "0",             "0",                "-1",               "2",                "YES",          null,       null,        null,      "0" , ""},
        	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL3",     "7",      "REAL",         "24",         "0",             "0",              "2",              "1",             "",           null,         "0",             "0",                "-1",               "3",                "YES",          null,       null,        null,      "0", ""  },
        	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL4",     "2",      "NUMERIC",         "6",         "0",             "2",              "10",              "1",          "",           null,         "0",             "0",                "-1",               "4",                "YES",          null,       null,        null,      "0" , "" },
        	};

        String toolboxJDBC40ExpectedV5R5[][] = {
        	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
        	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL1",     "8",      "FLOAT",       "53",         "0",             "0",              "2",              "1",             "",         "",         "0",             "0",                "-1",               "1",                "YES",          null,       null,        null,      "0", ""},
        	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL2",     "7",      "REAL",          "24",         "0",             "0",              "2",              "1",              "",           "",         "0",             "0",                "-1",               "2",                "YES",          null,       null,        null,      "0" , ""},
        	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL3",     "7",      "REAL",         "24",         "0",             "0",              "2",              "1",             "",           "",         "0",             "0",                "-1",               "3",                "YES",          null,       null,        null,      "0", ""  },
        	    {"", systemName, JDDMDTest.COLLECTION, "FLOATCOLUMNS", "COL4",     "2",      "NUMERIC",         "6",         "0",             "2",              "10",              "1",          "",           "",         "0",             "0",                "-1",               "4",                "YES",          null,       null,        null,      "0" , "" },
        	};

	message = new StringBuffer();

        String[][] expected33 = nativeExpected33;
	if ( JDTestDriver.isLUW()) {
          message.append("Expected = luwExpected33\n");
          expected33=luwExpected33;

        } else if ( (getJdbcLevel() >= 4   && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) ||   //@N1C
                ( (getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                (getRelease() >= JDTestDriver.RELEASE_V7R1M0) )
                ||( (getDriver() == JDTestDriver.DRIVER_TOOLBOX) && isSysibmMetadata())) {
          if (getJdbcMajorMinorLevel() >= 41) { 
            message.append("Expected = jdbc41Expected33\n");
            expected33=jdbc41Expected33;
            
          } else { 
            message.append("Expected = jdbc40Expected33\n");
            expected33=jdbc40Expected33;
          } 
        } else {
          if ( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
              if((getJdbcLevel() >= 4)){ //@pda
            	  if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {
  	    			message.append("Expected = toolboxJDBC40Expected\n");//@pda
  	    			expected33 = toolboxJDBC40Expected;  //@pda
  	    		}else{
  	    			message.append("Expected = toolboxJDBC40ExpectedV5R5\n");//@pda
  	    			expected33 = toolboxJDBC40ExpectedV5R5;  //@pda
  	    		} //@pda
            } //@pda
              else if(getRelease() < JDTestDriver.RELEASE_V7R1M0)    // @M1A
                  expected33=toolboxExpected33;
              else                                              // @M1A
                  expected33 = toolboxExpectedV5R5;             // @M1A

          }
        }

	try {
          String catalog =  getExpectedTableCat();

	    ResultSet rs = dmd_.getColumns (catalog,
					    JDDMDTest.SCHEMAS_PERCENT, "FLOATCOLUMNS", "%");

	    ResultSetMetaData rsmd = rs.getMetaData();
	    int count = rsmd.getColumnCount();
	    String [] oneRow = new String[count+1];

	    boolean success = true;
	    int rows = 0;
	    while (rs.next ()) {
		++rows;

		//
		// Get the row contents
                //
		for(int i=1; i <= count; i++)
		{
		    oneRow[i]=rs.getString(i);
		}
		//
		// Check for a matching row
                //
                boolean thisOk =  true;
                thisOk = checkExpected(oneRow, expected33);
		if (!thisOk) {
		    message.append("Line did not verify. Column count is "+count+"\n");
		    for (int i=1; i <= count; i++) {
			message.append("\"" + oneRow[i] + "\",  ");
		    }
		    success=false;
		    message.append("\n");
		}
	    }

	    rs.close ();
	    assertCondition (success
			     && (rows == 4), "Native Driver added V5R3 to check column information for FLOATS -- If toolbox different, create a toolbox expected array\n "+message.toString());
	} catch (Exception e) {
	    failed (e, "Unexpected Exception");
	}

    }


/**
 getColumns() -- Verify the results for BINARY/VARBINARY/VARCHAR/DATALINK columns.  @E2
**/
    public void Var034() {

    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && System.getProperty("java.home").indexOf("13") > 0) {
      notApplicable("Datalink not working in JDK 1.3 for toolbox ");
      return;
    }

    if ( (JDTestDriver.isLUW())) {
       notApplicable("not LUW test");
    }
      /** @K2 **/
      String jdbc40Expected34[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMA_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE",
              "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL1", "-2",
              "BINARY", "10", "10", null, null, "1", null, null, "-2", null,
              "10", "1", "YES", null, null, null, null, "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10", "10", null, null, "1", null, null, "-3", null,
              "10", "2", "YES", null, null, null, null, "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10", "10", null, null, "1", null, null, "12", null,
              "10", "3", "YES", null, null, null, null, "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200", "200", null, null, "1", null, null, "70",
              null, "202", "4", "YES", null, null, null, null, "NO" }, };

      String jdbc40Expected34r72[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMA_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE",
              "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL1", "-2",
              "BINARY", "10", "10", null, null, "1", null, null, "-2", null,
              "10", "1", "YES", null, null, null, null, "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10", "10", null, null, "1", null, null, "-3", null,
              "10", "2", "YES", null, null, null, null, "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10", "10", null, null, "1", null, null, "12", null,
              "10", "3", "YES", null, null, null, null, "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200", "200", null, null, "1", null, null, "70",
              null, "200", "4", "YES", null, null, null, null, "NO" }, };


      String jdbc41Expected34r72[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMA_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE",
              "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL1", "-2",
              "BINARY", "10", "10", null, null, "1", null, null, "-2", null,
              "10", "1", "YES", null, null, null, null, "NO", "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10", "10", null, null, "1", null, null, "-3", null,
              "10", "2", "YES", null, null, null, null, "NO", "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10", "10", null, null, "1", null, null, "12", null,
              "10", "3", "YES", null, null, null, null, "NO", "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200", "200", null, null, "1", null, null, "70",
              null, "200", "4", "YES", null, null, null, null, "NO", "NO" }, };


      String nativeExpected34[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL1", "-2",
              "BINARY", "10", "0", "0", "0", "1", null, null, "0", "0", "10",
              "1", "YES", null, null, null, null },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10", "0", "0", "0", "1", null, null, "0", "0",
              "10", "2", "YES", null, null, null, null },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10", "0", "0", "0", "1", null, null, "0", "0", "10",
              "3", "YES", null, null, null, null },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200", "0", "0", "0", "1", null, null, "0", "0",
              "202", "4", "YES", null, null, null, null }, };

      String toolboxExpected34[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL1", "-2",
              "BINARY", "10", "0", "0", "0", "1", "", null, "0", "0", "10",
              "1", "YES", null, null, null, "0" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10", "0", "0", "0", "1", "", null, "0", "0", "10",
              "2", "YES", null, null, null, "0" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10", "0", "0", "0", "1", "", null, "0", "0", "10",
              "3", "YES", null, null, null, "0" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200", "0", "0", "0", "1", "", null, "0", "0", "200",
              "4", "YES", null, null, null, "0" }, }; // @K1A

      String toolboxExpectedV7R134[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE","???" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL1", "-2",
              "BINARY", "10", "10", null, null, "1", null, null, "-2", null, "10", "1",
              "YES", null, null, null, null,"NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10", "10", null, null, "1", null, null, "-3", null, "10",
              "2", "YES", null, null, null, null,"NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10", "10", null, null, "1", null, null, "12", null, "10", "3",
              "YES", null, null, null, null, "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200", "200", null, null, "1", null, null, "70", null, "202",
              "4", "YES", null, null, null, null, "NO" }, }; // @M1A


      String toolboxExpectedV6R134[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL1", "-2",
              "BINARY", "10", "0", "0", "0", "1", "", "", "0", "0", "10", "1",
              "YES", null, null, null, "0" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10", "0", "0", "0", "1", "", "", "0", "0", "10",
              "2", "YES", null, null, null, "0" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10", "0", "0", "0", "1", "", "", "0", "0", "10", "3",
              "YES", null, null, null, "0" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200", "0", "0", "0", "1", "", "", "0", "0", "200",
              "4", "YES", null, null, null, "0" }, }; // @M1A

      String toolboxJDBC40Expected34[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE",
              "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL1", "-2",
              "BINARY", "10", "0", "0", "0", "1", "", null, "0", "0", "10",
              "1", "YES", null, null, null, "0", "" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10", "0", "0", "0", "1", "", null, "0", "0", "10",
              "2", "YES", null, null, null, "0", "" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10", "0", "0", "0", "1", "", null, "0", "0", "10",
              "3", "YES", null, null, null, "0", "" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200", "0", "0", "0", "1", "", null, "0", "0", "200",
              "4", "YES", null, null, null, "0", "" }, }; // @pda

      String toolboxJDBC40Expected34V5R5[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE",
              "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL1", "-2",
              "BINARY", "10", "0", "0", "0", "1", "", "", "0", "0", "10", "1",
              "YES", null, null, null, "0", "" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10", "0", "0", "0", "1", "", "", "0", "0", "10",
              "2", "YES", null, null, null, "0", "" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10", "0", "0", "0", "1", "", "", "0", "0", "10", "3",
              "YES", null, null, null, "0", "" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200", "0", "0", "0", "1", "", "", "0", "0", "200",
              "4", "YES", null, null, null, "0", "" }, }; // @pda



      String toolboxJDBC40Expected34V7R1[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE",
              "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL1", "-2",
              "BINARY", "10", "10", null, null, "1", null, null, "-2", null, "10", "1",
              "YES", null, null, null, null, "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10", "10", null, null, "1", null, null, "-3", null, "10",
              "2", "YES", null, null, null, null, "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10", "10", null, null, "1", null, null, "12", null, "10", "3",
              "YES", null, null, null, null, "NO" },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200", "200", null, null, "1", null, null, "70", null, "202",
              "4", "YES", null, null, null, null, "NO" }, }; // @pda




      String toolboxJDBC40Expected34V7R2[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE",
              "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT" },
          { "", systemName, JDDMDTest.COLLECTION,  "CHARCOLUMNS",  "COL1",  "-2",  "BINARY",  "10",  "10",  null,  null,  "1",  null,  null,  "-2",  null,  "10",  "1",  "YES",  null,  null,  null,  null,  "NO", },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10",  "10",  null,  null,  "1",  null,  null,  "-3",  null,  "10",  "2",  "YES",  null,  null,  null,  null,  "NO",  },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10",  "10",  null,  null,  "1",  null,  null,  "12",  null,  "10",  "3",  "YES",  null,  null,  null,  null,  "NO",   },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200",  "200",  null,  null,  "1",  null,  null,  "70",  null,  "200",  "4",  "YES",  null,  null,  null,  null,  "NO" }, }; // @pda

      String toolboxJDBC41Expected34V7R2[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE",
              "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT","IS_GENERATEDCOLUMN" },
          { "", systemName, JDDMDTest.COLLECTION,  "CHARCOLUMNS",  "COL1",  "-2",  "BINARY",  "10",  "10",  null,  null,  "1",  null,  null,  "-2",  null,  "10",  "1",  "YES",  null,  null,  null,  null,  "NO", "NO", },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10",  "10",  null,  null,  "1",  null,  null,  "-3",  null,  "10",  "2",  "YES",  null,  null,  null,  null,  "NO",  "NO", },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10",  "10",  null,  null,  "1",  null,  null,  "12",  null,  "10",  "3",  "YES",  null,  null,  null,  null,  "NO",   "NO", },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200",  "200",  null,  null,  "1",  null,  null,  "70",  null,  "200",  "4",  "YES",  null,  null,  null,  null,  "NO",  "NO", }, }; // @pda



      String toolboxJDBC41Expected34V7R1[][] = {
          { "", "TABLE_CAT", "SCHEMA", "TABLE_NAME", "COLUMN_NAME",
              "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH",
              "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS",
              "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
              "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
              "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE",
              "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT","IS_GENERATEDCOLUMN" },
          { "", systemName, JDDMDTest.COLLECTION,  "CHARCOLUMNS",  "COL1",  "-2",  "BINARY",  "10",  "10",  null,  null,  "1",  null,  null,  "-2",  null,  "10",  "1",  "YES",  null,  null,  null,  null,  "NO", "NO", },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL2", "-3",
              "VARBINARY", "10",  "10",  null,  null,  "1",  null,  null,  "-3",  null,  "10",  "2",  "YES",  null,  null,  null,  null,  "NO",  "NO", },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL3", "12",
              "VARCHAR", "10",  "10",  null,  null,  "1",  null,  null,  "12",  null,  "10",  "3",  "YES",  null,  null,  null,  null,  "NO",   "NO", },
          { "", systemName, JDDMDTest.COLLECTION, "CHARCOLUMNS", "COL4", "70",
              "DATALINK", "200",  "200",  null,  null,  "1",  null,  null,  "70",  null,  "202",  "4",  "YES",  null,  null,  null,  null,  "NO",  "NO", }, }; // @pda





      message = new StringBuffer();

      String[][] expected34 = nativeExpected34;
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
          || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
        if ((getJdbcLevel() >= 4)) { // @pda
          if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
            message.append("Expected = toolboxJDBC40Expected34\n");// @pda
            expected34 = toolboxJDBC40Expected34; // @pda
          } else {
	      if (getRelease() < JDTestDriver.RELEASE_V7R1M0) { 
		  message.append("Expected = toolboxJDBC40Expected34V5R5\n");// @pda
		  expected34 = toolboxJDBC40Expected34V5R5; // @pda
	      } else  if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
		  if (getJdbcMajorMinorLevel() >= 41 ) { 
		      message.append("Expected = toolboxJDBC41Expected34V7R1\n");// @pda
		      expected34 = toolboxJDBC41Expected34V7R1; // @pda

		  } else { 
		      message.append("Expected = toolboxJDBC40Expected34V7R1\n");// @pda
		      expected34 = toolboxJDBC40Expected34V7R1; // @pda
		  }


	      } else {
	        if (getJdbcMajorMinorLevel() >= 41 ) { 
	          message.append("Expected = toolboxJDBC41Expected34V7R2\n");// @pda
	          expected34 = toolboxJDBC41Expected34V7R2; // @pda
	          
	        } else { 
		  message.append("Expected = toolboxJDBC40Expected34V7R2\n");// @pda
		  expected34 = toolboxJDBC40Expected34V7R2; // @pda
	        }
	      } 
          } // @pda
        } // @pda
        else {
          if (getRelease() < JDTestDriver.RELEASE_V7R1M0) { // @M1A
            expected34 = toolboxExpected34;
            message.append("Expected = toolboxExpected34\n");
          } else if (getRelease() < JDTestDriver.RELEASE_V7R1M0){ // @M1A
            expected34 = toolboxExpectedV6R134; // @M1A
            message.append("Expected = toolboxExpectedV6R134\n");
	  } else {
            expected34 = toolboxExpectedV7R134; // @M1A
            message.append("Expected = toolboxExpectedV7R134\n");

	  }
        }
      } else if ((getJdbcLevel() >= 4 && getDriver() != JDTestDriver.DRIVER_TOOLBOX)
          || // @N1C
          ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V7R1M0))
          || ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && isSysibmMetadata())) {
	  if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
	    if (getJdbcMajorMinorLevel() >= 41) { 
        message.append("Expected = jdbc41Expected34r72\n");
        expected34 = jdbc41Expected34r72;
	    } else { 
	      message.append("Expected = jdbc40Expected34r72\n");
	      expected34 = jdbc40Expected34r72;
	    }
	  } else { 
	      message.append("Expected = jdbc40Expected34\n");
	      expected34 = jdbc40Expected34;
	  }
      }

      try {
        String catalog = getExpectedTableCat();

        ResultSet rs = dmd_.getColumns(catalog, JDDMDTest.SCHEMAS_PERCENT,
            "CHARCOLUMNS", "%");

        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        String[] oneRow = new String[count + 1];

        boolean success = true;
        int rows = 0;
        while (rs.next()) {
          ++rows;

          //
          // Get the row contents
          //
          for (int i = 1; i <= count; i++) {
            oneRow[i] = rs.getString(i);
          }
          //
          // Check for a matching row
          //
          boolean thisOk = true;
          thisOk = checkExpected(oneRow, expected34);
          if (!thisOk) {
            message.append("Line did not verify. Column count is " + count
                + "\n");
            for (int i = 1; i <= count; i++) {
              message.append("\"" + oneRow[i] + "\",  ");
            }
            message.append("\n");
            success = false;
            // System.out.println(sb.toString());
          }
        }

        rs.close();
        assertCondition(
            success && (rows == 4),
            "rows="
                + rows
                + " sb 4 Native Driver added V5R3 to check column information for CHAR -- If toolbox different, create a toolbox expected array\n "
                + message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }

    }



/**
 getColumns() -- Verify the results for GRAPHIC/VARGRAPHIC columns.  @E2
**/
    public void Var035()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0 && (! JDTestDriver.isLUW()) ) {
     /** @K2 **/
     //String systemName = ((AS400JDBCConnection)connection_).getSystem().getSystemName();    //@K1A
        String jdbc40Expected35[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL1",     "1",    "GRAPHIC",       "10",         "20",             null,              null,            "1",             null,         null,         "-95",            null,                "20",               "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL2",    "12",    "VARGRAPHIC",    "10",         "20",             null,             null,             "1",             null,         null,         "-96",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO" },
        };

        String jdbc41Expected35[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN"},
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL1",     "1",    "GRAPHIC",       "10",         "20",             null,              null,            "1",             null,         null,         "-95",            null,                "20",               "1",                "YES",          null,       null,        null,      null, "NO", "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL2",    "12",    "VARGRAPHIC",    "10",         "20",             null,             null,             "1",             null,         null,         "-96",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO", "NO" },
        };


	String nativeExpected35[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL1",     "1",      "GRAPHIC",       "10",         "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "20",               "1",                "YES",          null,       null,        null,      null},
	    {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL2",    "12",    "VARG",          "10",         "0",             "0",              "0",              "1",             null,           null,         "0",             "0",                "20",               "2",                "YES",          null,       null,        null,      null },
	};

        String toolboxExpected35[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL1",     "1",      "GRAPHIC",       "10",         "0",             "0",              "0",              "1",          "",         null,         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0"},
	    {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL2",    "12",    "VARGRAPHIC",          "10",         "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" },
	};            //@K1A

        String toolboxExpected35V5R5[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL1",     "1",      "GRAPHIC",       "10",         "0",             "0",              "0",              "1",          "",         "",         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0"},
	    {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL2",    "12",    "VARGRAPHIC",          "10",         "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" },
	};            //@M1A

        String toolboxJDBC40Expected35[][] = {
        	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
        	    {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL1",     "1",      "GRAPHIC",       "10",         "0",             "0",              "0",              "1",          "",         null,         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0", ""},
        	    {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL2",    "12",    "VARGRAPHIC",          "10",         "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0", "" },
        	};  //@pda

        

        
        String toolboxJDBC40Expected35V5R5[][] = {
        	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
        	    {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL1",     "1",      "GRAPHIC",       "10",         "0",             "0",              "0",              "1",          "",         "",         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0", ""},
        	    {"", systemName, JDDMDTest.COLLECTION, "GRAPHICCOLUMNS", "COL2",    "12",    "VARGRAPHIC",          "10",         "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0", "" },
        	};  //@pda

	    message = new StringBuffer();
        String[][] expected35 = nativeExpected35;
        if ( (getJdbcLevel() >= 4 && getDriver() != JDTestDriver.DRIVER_TOOLBOX) || //@N1C
            ( (getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                (getRelease() >= JDTestDriver.RELEASE_V7R1M0) )
                || ( (getDriver() == JDTestDriver.DRIVER_TOOLBOX) && isSysibmMetadata()) ) {
          if (getJdbcMajorMinorLevel() >= 41) {
            message.append("Expected = jdbc41Expected35\n");
            expected35=jdbc41Expected35;
          } else { 
          message.append("Expected = jdbc40Expected35\n");
          expected35=jdbc40Expected35;
          }
        } else {
          if ( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        	  if((getJdbcLevel() >= 4)){ //@pda
        		  if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {
  	    			message.append("Expected = toolboxJDBC40Expected35\n");//@pda
  	    			expected35 = toolboxJDBC40Expected35;  //@pda
  	    		}else{
  	    			message.append("Expected = toolboxJDBC40Expected35V5R5\n");//@pda
  	    			expected35 = toolboxJDBC40Expected35V5R5;  //@pda
  	    		} //@pda
              } //@pda
        	  else if(getRelease() < JDTestDriver.RELEASE_V7R1M0)    // @M1A
                  expected35=toolboxExpected35;
              else                                              // @M1A
                  expected35=toolboxExpected35V5R5;               // @M1A
          }
        }


	try {
          String catalog =  getExpectedTableCat();

	    ResultSet rs = dmd_.getColumns (catalog,
					    JDDMDTest.SCHEMAS_PERCENT, "GRAPHICCOLUMNS", "%");

	    ResultSetMetaData rsmd = rs.getMetaData();
	    int count = rsmd.getColumnCount();
	    String [] oneRow = new String[count+1];

	    boolean success = true;
	    int rows = 0;
	    while (rs.next ()) {
		++rows;

		//
		// Get the row contents
                //
		for(int i=1; i <= count; i++)
		{
		   // System.out.println("Getting "+i);
		    oneRow[i]=rs.getString(i);
		}
		//
		// Check for a matching row
                //
                boolean thisOk =  true;
                thisOk = checkExpected(oneRow, expected35);
		if (!thisOk) {
		    message.append("Line did not verify. Column count is "+count+"\n");
		    for (int i=1; i <= count; i++) {
			message.append("\"" + oneRow[i] + "\",  ");
		    }
		    message.append("\n");
		    success=false;
		//    System.out.println(sb.toString());
		}
	    }

	    rs.close ();
	    assertCondition (success
			     && (rows == 2), "Native Driver added V5R3 to check column information for GRAPHIC -- If toolbox different, create a toolbox expected array\n "+message.toString());
	} catch (Exception e) {
	    failed (e, "Unexpected Exception");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

    /**
getColumns() - Get a list of those created in this testcase and
verify all columns with system remarks (the default).  Use *** getObject ****  to get the columns

SQL400 - the 400 doesn't support the LABEL on values being brought back
         in the remarks field.  We therefore use comment on here to make
         sure that we test returning all the result set columns.

SQL400 - the CLI seems to return some pretty weird stuff for a timestamp
         field.  I changed the testcase for the native driver to expect
         what the CLI is returning, not because it is right, but because
         there are too many important things to do right now to fix this
         one.  At least this way, it will get flagged if the output changes.

         Changed 04/26/2006 to ignore BUFFER_LENGTH/SQL_DATA_TYPE/SQL_DATETIME_SUB
         columns since the JDBC spec says these are not used.
     **/
    public void Var036()
    {
        try {
            Statement s = null;
            message = new StringBuffer();
            messageColumnName="";
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
                // Label the columns.  No label on COLUMN, please.
                s = connection_.createStatement ();
                s.executeUpdate ("LABEL ON COLUMN " + JDDMDTest.COLLECTION
                        + ".COLUMNS (COLUMN1 TEXT IS '8')");
                s.executeUpdate ("LABEL ON COLUMN " + JDDMDTest.COLLECTION
                        + ".COLUMNS (COLUMN2 TEXT IS 'Nine')");
                s.executeUpdate ("LABEL ON COLUMN " + JDDMDTest.COLLECTION
                        + ".COLUMNS (COLUMNXX TEXT IS 'SQL/400')");
            } else {
                // Label the columns.  No label on COLUMN, please.
                s = connection_.createStatement ();

                if (JDTestDriver.isLUW()) {
                    // Syntax is different
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS.COLUMN1 IS '8'");
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS.COLUMN2 IS 'Nine'");
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS.COLUMNXX IS 'SQL/400'");

                } else {
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS (COLUMN1 IS '8')");
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS (COLUMN2 IS 'Nine')");
                    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
                            + ".COLUMNS (COLUMNXX IS 'SQL/400')");
                }
            }

            s.close ();

            String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (null, JDDMDTest.COLLECTION,
                    "COLUMNS", "%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                if (catalog == null) {
                    String outCatalog = rs.getString("TABLE_CAT");
                    if (outCatalog == null) {
                    } else {
                        System.out.println("Expected null, got "+outCatalog);
                        success=false;
                    }
                } else {

                    success = check((String) rs.getObject ("TABLE_CAT"),catalog,"TABLE_CAT") && success;
                }
                success = check((String) rs.getObject ("TABLE_SCHEM"),JDDMDTest.COLLECTION, "TABLE_SCHEM") && success ;
                success = check((String) rs.getObject ("TABLE_NAME"), "COLUMNS", "TABLE_NAME") && success;

                String  column           = (String)  rs.getObject ("COLUMN_NAME");
                Integer dataType         = (Integer) rs.getObject ("DATA_TYPE");
                String  typeName         = (String)  rs.getObject ("TYPE_NAME");
                Integer columnSize       = (Integer) rs.getObject ("COLUMN_SIZE");
                /* Integer bufferLength     = (Integer) rs.getObject ("BUFFER_LENGTH"); */
                Integer decimalDigits    = (Integer) rs.getObject ("DECIMAL_DIGITS");
                Integer numPrecRadix     = (Integer) rs.getObject ("NUM_PREC_RADIX");
                Integer nullable         = (Integer) rs.getObject ("NULLABLE");
                String  remarks          = (String)  rs.getObject ("REMARKS");
                String  columnDef        = (String)  rs.getObject ("COLUMN_DEF");
                /* Integer sqlDataType      = (Integer) rs.getObject ("SQL_DATA_TYPE"); */
                /* Integer sqlDateTimeSub   = (Integer) rs.getObject ("SQL_DATETIME_SUB"); */
                Integer charOctetLength  = (Integer) rs.getObject ("CHAR_OCTET_LENGTH");
                Integer ordinalPosition  = (Integer) rs.getObject ("ORDINAL_POSITION");
                String isNullable        = (String)  rs.getObject ("IS_NULLABLE");

                messageColumnName=column;

                /* success = check(bufferLength.intValue(),0,"bufferLength") && success ; */
                if(getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getRelease() < JDTestDriver.RELEASE_V7R1M0 || getDriver() == JDTestDriver.DRIVER_NATIVE || JDTestDriver.isLUW() || (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))  // @M1A
                    success = check(columnDef,(String) null, "columnDef") && success ;
                else                                            // @M1A
                    success = check(columnDef, "", "columnDef") && success; // @M1A
                /* success = check(sqlDataType.intValue(),0, "sqlDataType") && success ; */
                /* success = check(sqlDateTimeSub.intValue(),0, "sqlDateTimeSub") && success ; */

                if (column.equals ("COLUMN")) {
                    messageColumnName="COLUMN";
                    if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE ||getDriver () == JDTestDriver.DRIVER_NATIVE || ( getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata() )){
                        success = check(remarks,(String)null,"remarks") && success ;
                        success = check(ordinalPosition.intValue(),1,"ordinalPosition") && success ;
                        if (charOctetLength == null) {
                            success = check(null,(String) null,"charOctetLength") && success ;
                        } else {
                            success = check(charOctetLength.intValue(),0,"charOctetLength") && success ;
                        }
                    } else {
                        if (JDTestDriver.isLUW()) {
                            success = check(remarks,(String) null,"remarks") && success ;
                        } else {
                            success = check(remarks,"","remarks") && success ;
                        }
                        success = checkOr(ordinalPosition.intValue(), -1, 1, "ordinalPosition") && success ; // @C1C
                        if (charOctetLength == null) {
                            success = check(null,(String) null,"charOctetLength") && success ;
                        } else {
                            success = check(charOctetLength.intValue(),-1,"charOctetLength") && success ;
                        }
                    }

                    success = check(dataType.intValue(), Types.INTEGER, "dataType") && success ;
                    success = check(typeName,"INTEGER", "typeName") && success ;
                    success = check(columnSize.intValue(), 10, "columnSize") && success ;
                    success = check(decimalDigits.intValue(), 0, "decimalDigits") && success ;
                    success = check(numPrecRadix.intValue(), 10, "numPrecRadix") && success ;
                    success = check(nullable.intValue(), DatabaseMetaData.columnNoNulls, "nullable") && success ;
                    success = check(isNullable,"NO", "isNullable") && success ;
                } else if (column.equals ("COLUMN1")) {
                    messageColumnName="COLUMN1";
                    if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) { // @B1C
                        success = check(ordinalPosition.intValue(),2,"ordinalPosition") && success ;
                        if (charOctetLength == null) {
                            success = check(null,(String) null,"charOctetLength") && success ;
                        } else {
                            success = check(charOctetLength.intValue(),0,"charOctetLength") && success ;
                        }
                        success = check(remarks,"8", "remarks") && success ;
                        success = check(dataType.intValue(), Types.TIMESTAMP, "dataType" ) && success ;
                        success = check(typeName,"TIMESTAMP","typeName") && success ;
                        success = check(columnSize.intValue(), 26, "columnSize") && success ;
                        // A timestamp doesn't really have decimalDigits
                        success = check(decimalDigits.intValue(),6, "decimalDigits" ) && success ;	// @K3: changed 0 to 6
                        if (numPrecRadix == null) {
                            success = check(null,(String) null,"numPrecRadix") && success ;
                        } else {
                            success = check(numPrecRadix.intValue(),0,"numPrecRadix") && success ;
                        }
                        success = check(nullable.intValue(), DatabaseMetaData.columnNullable,"nullable") && success ;
                        success = check(isNullable,"YES", "isNullable") && success ;
                    } else {
                        success = checkOr(ordinalPosition.intValue(), -1, 2, "ordinalPosition") && success ; // @C1C
                        if (charOctetLength == null) {
                            success = check(null,(String) null,"charOctetLength") && success ;
                        } else {
                            success = check(charOctetLength.intValue(), -1, "charOctetLength") && success ;
                        }
                        success = check(remarks, "8", "remarks") && success ;
                        success = check(dataType.intValue(),Types.TIMESTAMP,"dataType") && success ;
                        success = check(typeName, "TIMESTAMP", "typeName") && success ;
                        success = check(columnSize.intValue(), 26, "columnSize") && success ;
                        success = check(decimalDigits.intValue(), 6, "decimalDigits") && success ;
                        if (numPrecRadix == null) {
                            success = check(null,(String) null,"numPrecRadix") && success ;
                        } else {
                            success = check(numPrecRadix.intValue(),0,"numPrecRadix") && success ;
                        }
                        success = check(nullable.intValue(),DatabaseMetaData.columnNullable, "nullable") && success ;
                        success = check(isNullable,"YES","isNullable") && success ;
                    }


                } else if (column.equals ("COLUMN2")) {
                    messageColumnName="COLUMN2";
                    if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                        success = check(ordinalPosition.intValue(), 3, "ordinalPosition") && success ;
                    } else {
                        success = checkOr(ordinalPosition.intValue(), -1, 3, "ordinalPosition") && success ; // @C1C
                    }

                    success = check(remarks,"Nine", "remarks") && success ;
                    success = check(dataType.intValue(),Types.CHAR, "dataType") && success ;
                    success = check(typeName, "CHAR", "typeName") && success ;
                    success = check(columnSize.intValue(), 10, "columnSize") && success ;
                    if (decimalDigits == null) {
                        success = check(null, (String) null, "decimalDigits") && success ;
                    } else {
                        success = check(decimalDigits.intValue(), 0, "decimalDigits") && success ;
                    }
                    if (numPrecRadix == null) {
                        success = check(null, (String) null,"numPrecRadix") && success ;
                    } else {
                        success = check(numPrecRadix.intValue(),0,"numPrecRadix") && success ;
                    }
                    success = check(nullable.intValue(),DatabaseMetaData.columnNoNulls, "nullable") && success ;
                    if (charOctetLength == null) {
                        success = check(null,(String) null,"charOctetLength") && success ;
                    } else {
                        success = check(charOctetLength.intValue(),10,"charOctetLength") && success ;
                    }
                    success = check(isNullable,"NO","isNullable") && success ;
                } else if (column.equals ("COLUMNXX")) {
                    messageColumnName="COLUMNXX";
                    if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                        success = check(ordinalPosition.intValue(),4,"ordinalPosition") && success ;
                        if (charOctetLength == null) {
                            success = check(null,(String) null,"charOctetLength") && success ;
                        } else {
                            success = check(charOctetLength.intValue(),0,"charOctetLength") && success ;
                        }
                    } else {
                        success = checkOr(ordinalPosition.intValue(),-1,4,"ordinalPosition") && success ; // @C1C
                        if (charOctetLength == null) {
                            success = check(null,(String) null,"charOctetLength") && success ;
                        } else {
                            success = check(charOctetLength.intValue(),-1,"charOctetLength") && success ;
                        }
                    }

                    success = check(remarks,"SQL/400","remarks") && success ;
                    success = check(dataType.intValue(),Types.DECIMAL,"dataType") && success ;
                    success = check(typeName,"DECIMAL","typeName") && success ;
                    success = check(columnSize.intValue(),6,"columnSize") && success ;
                    success = check(decimalDigits.intValue(),2,"decimalDigits") && success ;
                    success = check(numPrecRadix.intValue(),10,"numPrecRadix") && success ;
                    success = check(nullable.intValue(),DatabaseMetaData.columnNullable, "nullable") && success ;
                    success = check(isNullable,"YES","isNullable") && success ;
                }

            }
            messageColumnName="";
            rs.close ();
            assertCondition ((rows == 4) && success, " Added by native 04/20/2004 rows="+rows+".."+message.toString());
        } catch (Exception e) {
            failed (e, "Unexpected Exception - Added by native 04/20/2004 ");
        }
    }



/**
getColumns() - Get a list of those created in this testcase and
verify the remarks column with sql remarks.  Use getObject

SQL400 - The native driver will return null for a remarks field that
         has no value.  The toolbox will return an empty string.
**/
    public void Var037()
    {
	var003Executed = true;		// @K3
        try {
          String catalog =  getExpectedTableCat();

	  Connection c;
	    if (getDriver() == JDDriverTest.DRIVER_JCC) {
		c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	    } else {

		c = testDriver_.getConnection (baseURL_
							  + ";remarks=sql", userId_, encryptedPassword_);
	    }

            // Label the columns.  No label on COLUMN, please.
            Statement s = connection_.createStatement ();
		if (JDTestDriver.isLUW()) {
		    // Syntax is different
		    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				     + ".COLUMNS.COLUMN1 IS '2'");
		    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				     + ".COLUMNS.COLUMN2 IS 'Three'");
		    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				     + ".COLUMNS.COLUMNXX IS 'JDBC is fast'");

		} else {

		    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				     + ".COLUMNS (COLUMN1 IS '2')");
		    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				     + ".COLUMNS (COLUMN2 IS 'Three')");
		    s.executeUpdate ("COMMENT ON COLUMN " + JDDMDTest.COLLECTION
				     + ".COLUMNS (COLUMNXX IS 'JDBC is fast')");
		}
            s.close ();

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getColumns (null, JDDMDTest.COLLECTION,
                                           "COLUMNS", "%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;

		if (catalog == null) {
		    String outCatalog = rs.getString("TABLE_CAT");
		    if (outCatalog == null) {
		    } else {
			System.out.println("Expected null, got "+outCatalog);
			success=false;
		    }
		} else {
		    success = success && rs.getObject ("TABLE_CAT").equals (catalog);
		}
                success = success && rs.getObject ("TABLE_SCHEM").equals (JDDMDTest.COLLECTION);
                success = success && rs.getObject ("TABLE_NAME").equals ("COLUMNS");

                String column = (String) rs.getObject ("COLUMN_NAME");
                String remarks = (String)  rs.getObject ("REMARKS");

                if (column.equals ("COLUMN")) {
                    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
                        success = success && remarks.equals ("");
                    else
                        success = success && (remarks == null);
                } else if (column.equals ("COLUMN1"))
                    success = success && remarks.equals ("2");
                else if (column.equals ("COLUMN2"))
                    success = success && remarks.equals ("Three");
                else if (column.equals ("COLUMNXX"))
                    success = success && remarks.equals ("JDBC is fast");
                else
                    success = false;  // tighten down testing.
            }

            rs.close ();
            c.close ();
            assertCondition ((rows == 4) && success, " Added by native 04/20/2004 ");
        } catch (Exception e) {
            failed (e, "Unexpected Exception   Added by native 04/20/2004 ");
        }
    }

    //@L1A
    /*
    getColumns() - Test to see if we can retrieve a 128 byte column name
    */
    public void Var038()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0 && (! JDTestDriver.isLUW())){
            try{
                ResultSet rs = dmd_.getColumns(null, JDDMDTest.COLLECTION, "LCNCOLUMNS", "%");
                boolean success = false;
                int rows = 0;
                while(rs.next())
                {
                    rows++;
                    if(rs.getString("COLUMN_NAME").equals("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST"))
                        success = true;
                }
                rs.close();
                assertCondition(success && rows == 1, "Added by Toolbox 8/11/2004 for testing 128 byte column names.");
            }
            catch(Exception e){
                failed(e, "Unexpected exception.  Added by Toolbox 8/11/2004 for testing 128 byte column names.");
            }
        }
        else
            notApplicable("V5R4 or greater variation.");
    }


    /**
     * Test the metatdata for a FOR BIT data columns
     *  Should be the same as native
     */
    public void Var039()
    {

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && System.getProperty("java.home").indexOf("13") > 0 )
	{
	    notApplicable("Datalink not working in JDK 1.3 for toolbox ");
	    return;
	}

	    /** @K2 **/
       // String systemName = ((AS400JDBCConnection)connection_).getSystem().getSystemName();    //@K1A
        String jdbc40Expected39[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL1",     "-2",      "CHAR () FOR BIT DATA",   "10",          "10",            null,             null,              "1",             null,         null,         "-2",            null,                "10",               "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL2",    "-3",       "VARCHAR () FOR BIT DATA","10",          "10",            null,             null,              "1",             null,         null,         "-3",            null,                "10",               "2",                "YES",          null,       null,        null,      null, "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL3",     "12",      "VARCHAR",                "10",          "10",            null,             null,              "1",             null,         null,         "12",            null,                "10",               "3",                "YES",          null,       null,        null,      null, "NO"  },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL4",     "70",      "DATALINK",               "200",         "200",           null,             null,              "1",             null,         null,         "70",            null,                "202",              "4",                "YES",          null,       null,        null,      null, "NO"  },
        };

        String jdbc41Expected39[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL1",     "-2",      "CHAR () FOR BIT DATA",   "10",          "10",            null,             null,              "1",             null,         null,         "-2",            null,                "10",               "1",                "YES",          null,       null,        null,      null, "NO", "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL2",    "-3",       "VARCHAR () FOR BIT DATA","10",          "10",            null,             null,              "1",             null,         null,         "-3",            null,                "10",               "2",                "YES",          null,       null,        null,      null, "NO", "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL3",     "12",      "VARCHAR",                "10",          "10",            null,             null,              "1",             null,         null,         "12",            null,                "10",               "3",                "YES",          null,       null,        null,      null, "NO", "NO"  },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL4",     "70",      "DATALINK",               "200",         "200",           null,             null,              "1",             null,         null,         "70",            null,                "202",              "4",                "YES",          null,       null,        null,      null, "NO", "NO"  },
        };

        
        String jdbc40Expected39r72[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL1",     "-2",      "CHAR () FOR BIT DATA",   "10",          "10",            null,             null,              "1",             null,         null,         "-2",            null,                "10",               "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL2",    "-3",       "VARCHAR () FOR BIT DATA","10",          "10",            null,             null,              "1",             null,         null,         "-3",            null,                "10",               "2",                "YES",          null,       null,        null,      null, "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL3",     "12",      "VARCHAR",                "10",          "10",            null,             null,              "1",             null,         null,         "12",            null,                "10",               "3",                "YES",          null,       null,        null,      null, "NO"  },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL4",     "70",      "DATALINK",               "200",         "200",           null,             null,              "1",             null,         null,         "70",            null,                "200",              "4",                "YES",          null,       null,        null,      null, "NO"  },
        };

        String jdbc41Expected39r72[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL1",     "-2",      "CHAR () FOR BIT DATA",   "10",          "10",            null,             null,              "1",             null,         null,         "-2",            null,                "10",               "1",                "YES",          null,       null,        null,      null, "NO", "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL2",    "-3",       "VARCHAR () FOR BIT DATA","10",          "10",            null,             null,              "1",             null,         null,         "-3",            null,                "10",               "2",                "YES",          null,       null,        null,      null, "NO", "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL3",     "12",      "VARCHAR",                "10",          "10",            null,             null,              "1",             null,         null,         "12",            null,                "10",               "3",                "YES",          null,       null,        null,      null, "NO", "NO"  },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL4",     "70",      "DATALINK",               "200",         "200",           null,             null,              "1",             null,         null,         "70",            null,                "200",              "4",                "YES",          null,       null,        null,      null, "NO", "NO"  },
        };



        String luwExpected39[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL1",     "-2",      "CHAR () FOR BIT DATA",   "10",          "10",            null,             null,              "1",             null,         null,         "-2",            null,                "10",               "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL2",    "-3",       "VARCHAR () FOR BIT DATA","10",          "10",            null,             null,              "1",             null,         null,         "-3",            null,                "10",               "2",                "YES",          null,       null,        null,      null, "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL3",     "12",      "VARCHAR",                "10",          "10",            null,             null,              "1",             null,         null,         "12",            null,                "10",               "3",                "YES",          null,       null,        null,      null, "NO"  },
	    {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL4",     "12",      "VARCHAR",                "200",         "200",  null,             null,              "1",             null,         null,         "12",            null,              "200",              "4",                "YES",        null,      null,  null,  null, "NO"},
        };



        String nativeExpected39[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL1",     "-2",      "CHAR () FOR BIT DATA",   "10",          "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      null},
	    {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL2",    "-3",       "VARCHAR () FOR BIT DATA","10",          "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      null },
	    {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL3",     "12",      "VARCHAR",                "10",          "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "10",               "3",                "YES",          null,       null,        null,      null  },
	    {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL4",     "70",      "DATALINK",               "200",         "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "202",              "4",                "YES",          null,       null,        null,      null  },
	};

        String toolboxExpected39[][] = {
	    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	    {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL1",     "-2",      "CHAR() FOR BIT DATA",    "10",          "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0"},
	    {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL2",    "-3",       "VARCHAR() FOR BIT DATA", "10",          "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" },
	    {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL3",     "12",      "VARCHAR",                "10",          "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "3",                "YES",          null,       null,        null,      "0"  },
	    {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL4",     "70",      "DATALINK",               "200",         "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "200",              "4",                "YES",          null,       null,        null,      "0"  },
	};  //@K1A

        String toolboxExpected39V5R5[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL1",     "-2",      "CHAR() FOR BIT DATA",    "10",          "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL2",    "-3",       "VARCHAR() FOR BIT DATA", "10",          "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL3",     "12",      "VARCHAR",                "10",          "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "3",                "YES",          null,       null,        null,      "0"  },
            {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL4",     "70",      "DATALINK",               "200",         "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "200",              "4",                "YES",          null,       null,        null,      "0"  },
        };  //@M1A

        String toolboxJDBC40Expected39[][] =  {
                {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL1",     "-2",      "CHAR() FOR BIT DATA",    "10",          "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0", ""},
                {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL2",    "-3",       "VARCHAR() FOR BIT DATA", "10",          "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0", "" },
                {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL3",     "12",      "VARCHAR",                "10",          "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "3",                "YES",          null,       null,        null,      "0", ""  },
                {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL4",     "70",      "DATALINK",               "200",         "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "200",              "4",                "YES",          null,       null,        null,      "0", "" },
            }; //@pda

        String toolboxJDBC40Expected39V5R5[][] =  {
                {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL1",     "-2",      "CHAR() FOR BIT DATA",    "10",          "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0", ""},
                {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL2",    "-3",       "VARCHAR() FOR BIT DATA", "10",          "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0", "" },
                {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL3",     "12",      "VARCHAR",                "10",          "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "3",                "YES",          null,       null,        null,      "0", ""  },
                {"", systemName, JDDMDTest.COLLECTION, "CHARBITCOLUMNS", "COL4",     "70",      "DATALINK",               "200",         "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "200",              "4",                "YES",          null,       null,        null,      "0", "" },
            }; //@pda

	    message = new StringBuffer();

        String[][] expected39 = nativeExpected39;
	if ( JDTestDriver.isLUW() ) {
	    expected39=luwExpected39;
	} else if (  getDriver () == JDTestDriver.DRIVER_JTOPENLITE ) {
	    if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		message.append("Expected = toolboxJDBC40Expected39\n");//@pda
		expected39 = toolboxJDBC40Expected39;  //@pda
	    }else{
		message.append("Expected = toolboxJDBC40Expected39V5R5\n");//@pda
		expected39 = toolboxJDBC40Expected39V5R5;  //@pda
	    } //@pda

	} else if (  getDriver () == JDTestDriver.DRIVER_JTOPENLITE ||  getDriver () == JDTestDriver.DRIVER_NATIVE || (getJdbcLevel() >= 4   && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) ||   //@N1C
                ( (getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                (getRelease() >= JDTestDriver.RELEASE_V7R1M0))
                ||  ( (getDriver() == JDTestDriver.DRIVER_TOOLBOX) &&
                        isSysibmMetadata())) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
	      if (getJdbcMajorMinorLevel() >= 41 ) { 
	        message.append("Expected = jdbc41Expected39r72\n");
	        expected39=jdbc41Expected39r72;
	      
	      } else { 
		message.append("Expected = jdbc40Expected39r72\n");
		expected39=jdbc40Expected39r72;
	      }
	    } else { 
	      if (getJdbcMajorMinorLevel() >= 41) { 
	        message.append("Expected = jdbc41Expected39\n");
	        expected39=jdbc41Expected39;
	        
	      } else { 
		message.append("Expected = jdbc40Expected39\n");
		expected39=jdbc40Expected39;
	      }
	    }
        } else {
          if ( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        	  if((getJdbcLevel() >= 4)){ //@pda
        		  if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {
  	    			message.append("Expected = toolboxJDBC40Expected39\n");//@pda
  	    			expected39 = toolboxJDBC40Expected39;  //@pda
  	    		}else{
  	    			message.append("Expected = toolboxJDBC40Expected39V5R5\n");//@pda
  	    			expected39 = toolboxJDBC40Expected39V5R5;  //@pda
  	    		} //@pda
              } //@pda
        	   else if(getRelease() < JDTestDriver.RELEASE_V7R1M0)    // @M1A
                  expected39=toolboxExpected39;
              else                                      // @M1A
                  expected39 = toolboxExpected39V5R5;     // @M1A
          }
        }

	try {
          String catalog =  getExpectedTableCat();


	    ResultSet rs = dmd_.getColumns (catalog,
					    JDDMDTest.SCHEMAS_PERCENT, "CHARBITCOLUMNS", "%");

	    ResultSetMetaData rsmd = rs.getMetaData();
	    int count = rsmd.getColumnCount();
	    String [] oneRow = new String[count+1];

	    boolean success = true;
	    int rows = 0;
	    while (rs.next ()) {
		++rows;

		//
		// Get the row contents
                //
		for(int i=1; i <= count; i++)
		{
		    oneRow[i]=rs.getString(i);
		}
		//
		// Check for a matching row
                //
                boolean thisOk =  true;
                thisOk = checkExpected(oneRow, expected39);
		if (!thisOk) {
		    message.append("Line did not verify. Column count is "+count+"\n");
		    for (int i=1; i <= count; i++) {
			message.append("\"" + oneRow[i] + "\",  ");
		    }
		    message.append("\n");
		    success=false;
		    //System.out.println(sb.toString());
		}
	    }

	    rs.close ();
	    assertCondition (success
			     && (rows == 4), "Native Driver added (08/03/05) to check column information for CHAR FOR BIT DATA -- If toolbox different, create a toolbox expected array\n "+message.toString());
	} catch (Exception e) {
	    failed (e, "Unexpected Exception -- testcase added 08/03/05 by native driver ");
	}

    }

    /**
     * Test the metatdata for UTF-8 columns
     *  Should be the same as native
     */
    public void Var040()
    {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 && (! JDTestDriver.isLUW())) {
        String added = " -- Added by native driver ";
            /** @K2 **/
       // String systemName = ((AS400JDBCConnection)connection_).getSystem().getSystemName();    //@K1A
        String jdbc40Expected40[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL1",     "1",       "CHAR",                   "10",          "10",             null,              null,              "1",             null,         null,         "1",            null,                "10",               "1",                "YES",          null,       null,        null,      null, "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL2",     "12",      "VARCHAR",                "10",          "10",            null,             null,              "1",             null,         null,         "12",             null,                "10",               "2",                "YES",          null,       null,        null,      null, "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL3",     "2005",    "CLOB",                   "1048576",     "1048576",       null,             null,              "1",             null,         null,         "-99",          null,                "1048576",               "3",                "YES",          null,       null,        null,      null, "NO"  },
        };

        String jdbc41Expected40[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL1",     "1",       "CHAR",                   "10",          "10",             null,              null,              "1",             null,         null,         "1",            null,                "10",               "1",                "YES",          null,       null,        null,      null, "NO", "NO"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL2",     "12",      "VARCHAR",                "10",          "10",            null,             null,              "1",             null,         null,         "12",             null,                "10",               "2",                "YES",          null,       null,        null,      null, "NO", "NO" },
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL3",     "2005",    "CLOB",                   "1048576",     "1048576",       null,             null,              "1",             null,         null,         "-99",          null,                "1048576",               "3",                "YES",          null,       null,        null,      null, "NO", "NO"  },
        };



        String nativeExpected40[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL1",     "1",      "CHAR"                ,   "10",          "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      null},
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL2",     "12",     "VARCHAR",               "10",          "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      null },
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL3",     "2005",   "CLOB",                  "1048576",     "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "1048576",               "3",                "YES",          null,       null,        null,      null  },
        };

        String toolboxExpected40[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL1",     "1",       "CHAR",                   "10",          "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL2",     "12",      "VARCHAR",                "10",          "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" },
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL3",     "2005",    "CLOB",                   "1048576",      "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "1048576",               "3",                "YES",          null,       null,        null,      "0"  },
        };  //@K1A

        String toolboxExpected40V5R5[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL1",     "1",       "CHAR",                   "10",          "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0"},
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL2",     "12",      "VARCHAR",                "10",          "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" },
            {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL3",     "2005",    "CLOB",                   "1048576",      "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "1048576",               "3",                "YES",          null,       null,        null,      "0"  },
        };  //@M1A

        String toolboxJDBC40Expected40[][] =  {
                {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL1",     "1",       "CHAR",                   "10",          "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0", ""},
                {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL2",     "12",      "VARCHAR",                "10",          "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" , ""},
                {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL3",     "2005",    "CLOB",                   "1048576",      "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "1048576",               "3",                "YES",          null,       null,        null,      "0" , "" },
            };  //@pda

        String toolboxJDBC40Expected40V5R5[][] =  {
                {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL1",     "1",       "CHAR",                   "10",          "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0", ""},
                {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL2",     "12",      "VARCHAR",                "10",          "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" , ""},
                {"", systemName, JDDMDTest.COLLECTION, "CHARUTF8COLUMNS", "COL3",     "2005",    "CLOB",                   "1048576",      "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "1048576",               "3",                "YES",          null,       null,        null,      "0" , "" },
            };  //@pda


            message = new StringBuffer();

        String[][] expected40 = nativeExpected40;
        if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE ||  (getJdbcLevel() >= 4 && getDriver() != JDTestDriver.DRIVER_TOOLBOX) || //@N1C
            ( (getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                (getRelease() >= JDTestDriver.RELEASE_V7R1M0))
                ||( (getDriver() == JDTestDriver.DRIVER_TOOLBOX) &&
                        (isSysibmMetadata())) ) {
          if (getJdbcMajorMinorLevel() >= 41) { 
            message.append("Expected = jdbc41Expected40\n");
            expected40=jdbc41Expected40;
            
          } else { 
          message.append("Expected = jdbc40Expected40\n");
          expected40=jdbc40Expected40;
          }
        } else {
          if ( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        	  if((getJdbcLevel() >= 4)){ //@pda
        		  if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {
  	    			message.append("Expected = toolboxJDBC40Expected40\n");//@pda
  	    			expected40 = toolboxJDBC40Expected40;  //@pda
  	    		}else{
  	    			message.append("Expected = toolboxJDBC40Expected40V5R5\n");//@pda
  	    			expected40 = toolboxJDBC40Expected40V5R5;  //@pda
  	    		} //@pda
              } //@pda
        	   else if(getRelease() < JDTestDriver.RELEASE_V7R1M0)    // @M1A
                  expected40=toolboxExpected40;
              else                                              // @M1A
                  expected40=toolboxExpected40V5R5;               // @M1A
          }
        }


        try {
          String catalog =  getExpectedTableCat();


            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "CHARUTF8COLUMNS", "%");

            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            String [] oneRow = new String[count+1];

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                //
                // Get the row contents
                //
                for(int i=1; i <= count; i++)
                {
                    oneRow[i]=rs.getString(i);
                }
                //
                // Check for a matching row
                //
                boolean thisOk =  true;
                thisOk = checkExpected(oneRow, expected40);
                if (!thisOk) {
                    message.append("Line did not verify. Column count is "+count+"\n");
                    for (int i=1; i <= count; i++) {
                        message.append("\"" + oneRow[i] + "\",  ");
                    }
                    message.append("\n");
                    success=false;
                    //System.out.println(sb.toString());
                }
            }

            rs.close ();
            assertCondition (success
                             && (rows == 3), "Rows = "+rows+" sb 3 Check column information for CHAR FOR UTF-8 "+added+" -- If toolbox different, create a toolbox expected array\n "+message.toString());
        } catch (Exception e) {
            failed (e, "Unexpected Exception "+added );
        }
      } else {
        notApplicable("V5R3 or later variation");
      }
    }

    /**
    getColumns() -- Verify the results for GRAPHIC/VARGRAPHIC UTF-16 columns.
   **/
       public void Var041()
       {

           if(getRelease() >= JDTestDriver.RELEASE_V7R1M0 && (! JDTestDriver.isLUW())) {

             String jdbc40Expected41[][] = {
                 {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL1",     "1",      "GRAPHIC",       "10",       "20",            null,              null,              "1",             null,         null,        "-95",           null,                "20",               "1",                "YES",          null,       null,        null,      null, "NO"},
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL2",    "12",    "VARGRAPHIC",      "10",         "20",             null,             null,              "1",             null,         null,        "-96",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO" },
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL3",     "2005", "DBCLOB",      "1048576",     "2097152",        null,             null,              "1",             null,         null,       "-350",           null,                "2097152",               "3",                "YES",          null,       null,        null,      null, "NO"  },
             };



             String jdbc30Expected4155[][] = {
                 {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL1",     "1",      "NCHAR",       "10",       "20",            null,              null,              "1",             null,         null,        "-8",           null,                "20",               "1",                "YES",          null,       null,        null,      null, "NO"},
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL2",    "12",    "NVARCHAR",      "10",         "20",             null,             null,              "1",             null,         null,        "-9",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO" },
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL3",     "2005", "NCLOB",      "1048576",     "2097152",        null,             null,              "1",             null,         null,       "-10",           null,                "2097152",               "3",                "YES",          null,       null,        null,      null, "NO"  },

             };



             String jdbc40Expected4155[][] = {
                 {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL1",     "-15",      "NCHAR",       "10",       "20",            null,              null,              "1",             null,         null,        "-8",           null,                "20",               "1",                "YES",          null,       null,        null,      null, "NO"},
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL2",    "-9",    "NVARCHAR",      "10",         "20",             null,             null,              "1",             null,         null,        "-9",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO" },
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL3",     "2011", "NCLOB",      "1048576",     "2097152",        null,             null,              "1",             null,         null,       "-10",           null,                "2097152",               "3",                "YES",          null,       null,        null,      null, "NO"  },

             };

             String jdbc41Expected4155[][] = {
                 {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN"},
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL1",     "-15",      "NCHAR",       "10",       "20",            null,              null,              "1",             null,         null,        "-8",           null,                "20",               "1",                "YES",          null,       null,        null,      null, "NO", "NO"},
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL2",    "-9",    "NVARCHAR",      "10",         "20",             null,             null,              "1",             null,         null,        "-9",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO", "NO" },
                 {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL3",     "2011", "NCLOB",      "1048576",     "2097152",        null,             null,              "1",             null,         null,       "-10",           null,                "2097152",               "3",                "YES",          null,       null,        null,      null, "NO", "NO"  },

             };


             String nativeExpected41[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL1",     "1",    "GRAPHIC",       "10",         "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "20",               "1",                "YES",          null,       null,        null,      null},
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL2",    "12",    "VARG",          "10",         "0",             "0",              "0",              "1",             null,           null,         "0",             "0",                "20",               "2",                "YES",          null,       null,        null,      null },
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL3",     "2005", "DBCLOB",      "1048576",     "0",             "0",              "0",              "1",             null,         null,         "0",             "0",                "2097152",               "3",                "YES",          null,       null,        null,      null  },
        };

           String toolboxExpected41[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL1",     "1",      "GRAPHIC",       "10",         "0",             "0",              "0",              "1",          "",         null,         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0"},
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL2",    "12",    "VARGRAPHIC",      "10",         "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" },
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL3",     "2005", "DBCLOB",      "1048576",     "0",             "0",              "0",              "1",             "",         null,         "0",             "0",                "1048576",               "3",                "YES",          null,       null,        null,      "0"  },

        };            //@K1A

        String toolboxExpected41V5R5[][] = {
            {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL1",     "1",      "GRAPHIC",       "10",         "0",             "0",              "0",              "1",          "",         "",         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0"},
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL2",    "12",    "VARGRAPHIC",      "10",         "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" },
            {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL3",     "2005", "DBCLOB",      "1048576",     "0",             "0",              "0",              "1",             "",         "",         "0",             "0",                "1048576",               "3",                "YES",          null,       null,        null,      "0"  },

        };            //@M1A

        String toolboxJDBC40Expected41[][] =  {
                {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL1",     "1",      "GRAPHIC",       "10",         "0",             "0",              "0",              "1",          "",         null,         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0", ""},
                {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL2",    "12",    "VARGRAPHIC",      "10",         "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0", "" },
                {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL3",     "2005", "DBCLOB",      "1048576",     "0",             "0",              "0",              "1",             "",         null,         "0",             "0",                "1048576",               "3",                "YES",          null,       null,        null,      "0", ""  },

            };      //@pda

        String toolboxJDBC40Expected41V5R5[][] =  {
                {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
                {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL1",     "1",      "GRAPHIC",       "10",         "0",             "0",              "0",              "1",          "",         "",         "0",             "0",                "10",               "1",                "YES",          null,       null,        null,      "0", ""},
                {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL2",    "12",    "VARGRAPHIC",      "10",         "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0", "" },
                {"", systemName, JDDMDTest.COLLECTION, "GRAPHICUTF16COLUMNS", "COL3",     "2005", "DBCLOB",      "1048576",     "0",             "0",              "0",              "1",             "",         "",         "0",             "0",                "1048576",               "3",                "YES",          null,       null,        null,      "0", ""  },

            };      //@pda


            message = new StringBuffer();

           String[][] expected41 = nativeExpected41;
	   if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
	       message.append("Expected = toolboxJDBC40Expected41V5R5\n");//@pda
	       expected41 = toolboxJDBC40Expected41V5R5;  //@pda
	   } else  if ( getJdbcLevel() >= 4 && getRelease() == JDTestDriver.RELEASE_V7R1M0 && getDriver() != JDTestDriver.DRIVER_TOOLBOX) {//@N1C
             message.append("Expected = jdbc40Expected41\n");
             expected41=jdbc40Expected41;
	   } else if ( (getDriver() == JDTestDriver.DRIVER_NATIVE) &&
		       (getRelease() >= JDTestDriver.RELEASE_V7R1M0)
		       || ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) &&
			   (isSysibmMetadata()))  || getDriver () == JDTestDriver.DRIVER_NATIVE  ) {
	      if (getJdbcMajorMinorLevel() >= 41) {
	         message.append("Expected = jdbc41Expected4155\n");
	         expected41=jdbc41Expected4155;
	        
	      } else {
		  if((getJdbcLevel() >= 4)) {
		      message.append("Expected = jdbc40Expected4155\n");
		      expected41=jdbc40Expected4155;
		  } else {
		      message.append("Expected = jdbc30Expected4155\n");
		      expected41=jdbc30Expected4155;

		  }
	      }
           } else {
             if ( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            	 if((getJdbcLevel() >= 4)){ //@pda
            		 if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {
     	    			message.append("Expected = toolboxJDBC40Expected41\n");//@pda
     	    			expected41 = toolboxJDBC40Expected41;  //@pda
     	    		}else{
     	    			message.append("Expected = toolboxJDBC40Expected41V5R5\n");//@pda
     	    			expected41 = toolboxJDBC40Expected41V5R5;  //@pda
     	    		} //@pda
                 } //@pda
            	   else if(getRelease() < JDTestDriver.RELEASE_V7R1M0) // @M1A
                     expected41=toolboxExpected41;
                 else                                           // @M1A
                     expected41=toolboxExpected41V5R5;            // @M1A
             }
           }



        try {
             String catalog =  getExpectedTableCat();

            ResultSet rs = dmd_.getColumns (catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, "GRAPHICUTF16COLUMNS", "%");

            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            String [] oneRow = new String[count+1];

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                //
                // Get the row contents
                   //
                for(int i=1; i <= count; i++)
                {
                   // System.out.println("Getting "+i);
                    oneRow[i]=rs.getString(i);
                }
                //
                // Check for a matching row
                   //
                   boolean thisOk =  true;
                       thisOk = checkExpected(oneRow, expected41);
                if (!thisOk) {
                    message.append("Line did not verify. Column count is "+count+"\n");
                    for (int i=1; i <= count; i++) {
                        message.append("\"" + oneRow[i] + "\", ");
                    }
                    message.append("\n");
                    success=false;
                //    System.out.println(sb.toString());
                }
            }

            rs.close ();
            assertCondition (success
                             && (rows == 3), "Rows="+rows+"sb 3 Native Driver added V5R3 to check column information for GRAPHIC UTF-16-- If toolbox different, create a toolbox expected array\n "+message.toString());
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
           }
           else
               notApplicable("V5R3 or greater variation");
       }

  /**
   * getColumns() -- Verify the results for ROWID columns.
   * Note:  LUW does not support rowid.
   */
  public void Var042() {

    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0  && (! JDTestDriver.isLUW())) {

	       String jdbc40Expected42[][] = {
		   {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL1",     "1111",      "ROWID",        "40",         "40",           null,              null,              "0",             null,         null,        "-100",              null,                "40",               "1",                "NO",          null,       null,        null,      null, "YES"},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL2",    "12",    "VARGRAPHIC",    "10",        "20",           null,             null,              "1",             null,           null,         "-96",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO" },
	       };


	       String expected4255[][] = {
		   {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL1",     "1111",      "ROWID",        "40",         "40",           null,              null,              "0",             null,         null,        "-100",              null,                "40",               "1",                "NO",          null,       null,        null,      null, "NO"},    /* IS_AUTOINCREMENT changed to NO May 2011 -- new results from sysibm procedures */
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL2",    "12",    "NVARCHAR",    "10",        "20",           null,             null,              "1",             null,           null,         "-9",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO" },
	       };


	       String jdbc40Expected4255[][] = {
		   {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
		   /* Changed to -8 4/13/2012 for */

		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL1",     "-8",      "ROWID",        "40",         "40",           null,              null,              "0",             null,         null,        "-100",              null,                "40",               "1",                "NO",          null,       null,        null,      null, "NO"},    /* IS_AUTOINCREMENT changed to NO May 2011 -- new results from sysibm procedures */
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL2",    "12",    "NVARCHAR",    "10",        "20",           null,             null,              "1",             null,           null,         "-9",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO" },
	       };


	       String jdbc40Expected4271[][] = {
		   {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL1",     "-8",      "ROWID",        "40",         "40",           null,              null,              "0",             null,         null,        "-100",              null,                "40",               "1",                "NO",          null,       null,        null,      null, "NO"},  /* IS_AUTOINCREMENT changed to NO May 2011 -- new results from sysibm procedures */
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL2",    "-9",    "NVARCHAR",    "10",        "20",           null,             null,              "1",             null,           null,         "-9",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO" },
	       };

         String jdbc41Expected4271[][] = {
       {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATED_COLUMN"},
       {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL1",     "-8",      "ROWID",        "40",         "40",           null,              null,              "0",             null,         null,        "-100",              null,                "40",               "1",                "NO",          null,       null,        null,      null, "NO", "YES" },  /* IS_AUTOINCREMENT changed to NO May 2011 -- new results from sysibm procedures */
       {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL2",    "-9",    "NVARCHAR",    "10",        "20",           null,             null,              "1",             null,           null,         "-9",            null,                "20",               "2",                "YES",          null,       null,        null,      null, "NO", "NO"  },
         };


	       String nativeExpected42[][] = {
		   {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL1",     "0",      "ROWID",        "0",         "0",             "0",              "0",              "0",             null,         null,         "0",             "0",                "40",               "1",                "NO",          null,       null,        null,      null},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL2",    "12",    "VARG",          "10",         "0",             "0",              "0",              "1",             null,           null,         "0",             "0",                "20",               "2",                "YES",          null,       null,        null,      null },
	       };

	       String toolboxExpected42[][] = {
		   {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL1",     "-2",      "ROWID",        "40",         "0",             "0",              "0",              "0",            "",         null,         "0",             "0",                "40",               "1",                "NO",          null,       null,        null,      "0"},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL2",    "12",    "VARGRAPHIC",          "10",         "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" },
	       };            //@K1A

               String toolboxExpected42V5R5[][] = {
		   {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL1",     "-2",      "ROWID",        "40",         "0",             "0",              "0",              "0",            "",         "",         "0",             "0",                "40",               "1",                "NO",          null,       null,        null,      "0"},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL2",    "12",    "VARGRAPHIC",          "10",         "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" },
	       };            //@M1A


               String toolboxJDBC40Expected42[][] = {
		   {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL1",     "-8",      "ROWID",        "40",         "0",             "0",              "0",              "0",            "",         null,         "0",             "0",                "40",               "1",                "NO",          null,       null,        null,      "0", ""},
		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL2",    "12",    "VARGRAPHIC",          "10",         "0",             "0",              "0",              "1",             "",           null,         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0","" },
	       };

               String toolboxJDBC40Expected42V5R5[][] =  {
            		   {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT"},
            		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL1",     "-8",      "ROWID",        "40",         "0",             "0",              "0",              "0",            "",         "",         "0",             "0",                "40",               "1",                "NO",          null,       null,        null,      "0", ""},
            		   {"", systemName, JDDMDTest.COLLECTION, "ROWIDCOLUMNS", "COL2",    "12",    "VARGRAPHIC",          "10",         "0",             "0",              "0",              "1",             "",           "",         "0",             "0",                "10",               "2",                "YES",          null,       null,        null,      "0" , ""},
               };//@pda

        message = new StringBuffer();

	String[][] expected42 = nativeExpected42;
	if ((getJdbcLevel() >= 4 && getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {  //@N1C
	    message.append("Expected = jdbc40Expected42\n");
	    expected42 = jdbc40Expected42;
	    if ((getDriver() == JDTestDriver.DRIVER_NATIVE || getDriver() == JDTestDriver.DRIVER_JTOPENLITE ) &&
		(getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
	      if (getJdbcMajorMinorLevel() >= 41) { 
	        message.append("Expected = jdbc41Expected4271\n");
	        expected42 = jdbc41Expected4271;
	        
	      } else { 
		message.append("Expected = jdbc40Expected4271\n");
		expected42 = jdbc40Expected4271;
	      }
	    } else if ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
		(getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {

		message.append("Expected = jdbc40Expected4255\n");
		expected42 = jdbc40Expected4255;
	    }

	}  else if (((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
	        (getRelease() >= JDTestDriver.RELEASE_V7R1M0))
	        || ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) &&
	                (isSysibmMetadata()))) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 && getDriver() == JDTestDriver.DRIVER_TOOLBOX && isJdbc40()) {
	      if (getJdbcMajorMinorLevel() >= 41) { 
          message.append("Expected = jdbc41Expected4271\n");
          expected42 = jdbc41Expected4271;
	        
	      } else { 
	        message.append("Expected = jdbc40Expected4271\n");
	        expected42 = jdbc40Expected4271;
	      }
	    } else if ( /* getRelease() == JDTestDriver.RELEASE_V6R1M0 && */ getJdbcLevel() < 4) {
		message.append("Expected = expected4255\n");
		expected42 = expected4255;

	    } else if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	        message.append("Expected = jdbc40Expected4255\n");
	        expected42 = jdbc40Expected4255;
	    } else {
	        message.append("Expected = jdbc40Expected4255\n");
	        expected42 = jdbc40Expected4255;
	    }
	} else {
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		if(getJdbcLevel() >= 4)                         //@N1A
		    if(getRelease() < JDTestDriver.RELEASE_V7R1M0) { //@N1A
			message.append("Expected = toolboxJDBC40Expected42\n");
			expected42 = toolboxJDBC40Expected42;       //@N1A  //@pdc
		    } else  {                                          //@N1A
			message.append("Expected = toolboxJDBC40Expected42V5R5\n");
			expected42 = toolboxJDBC40Expected42V5R5;     //@N1A //@pdc
		    }
		else if(getRelease() < JDTestDriver.RELEASE_V7R1M0) { // @M1A @N1C
		    message.append("Expected = toolboxJDBC42\n");
		    expected42 = toolboxExpected42;
		} else {                                             // @M1A
		    message.append("Expected = toolboxExpected42V5R5\n");
		    expected42 = toolboxExpected42V5R5;           // @M1A
		}
	    }
	}


      try {
        String catalog = getExpectedTableCat();

        ResultSet rs = dmd_.getColumns(catalog, JDDMDTest.SCHEMAS_PERCENT,
            "ROWIDCOLUMNS", "%");

        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        String[] oneRow = new String[count + 1];

        boolean success = true;
        int rows = 0;
        while (rs.next()) {
          ++rows;

          //
          // Get the row contents
          //
          for (int i = 1; i <= count; i++) {
            // System.out.println("Getting "+i);
            oneRow[i] = rs.getString(i);
          }
          //
          // Check for a matching row
          //
          boolean thisOk = true;
          thisOk = checkExpected(oneRow, expected42);
          if (!thisOk) {
            message.append("Line did not verify. Column count is " + count
                + "\n");
            for (int i = 1; i <= count; i++) {
              message.append("\"" + oneRow[i] + "\", ");
            }
            message.append("\n");
            success = false;
            // System.out.println(sb.toString());
          }
        }

        rs.close();
        assertCondition(
            success && (rows == 2),
            "Rows="
                + rows
                + " sb 2 Native Driver added V5R3 to check column information for ROWID-- If toolbox different, create a toolbox expected array\n "
                + message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    } else
      notApplicable("V5R3 or greater variation");
  }


/**
getColumns() - Check all the RSMD results when using JDBC 3.0.  In CPS discussion 6QGKX5, SPSS
encountered an java.lang.UnknownError when call getNullable for the 22 column.  This testcase
will attempt to recreate the error.
**/

  public void Var043() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("SQLCODE: -443, SQLSTATE: 38553, SQLERRMC: SYSIBM.SQLCOLUMNS;COLUMNS;SYSIBM:CLI:-727");
	  return;
      }


	    String jvmName = System.getProperty("java.vm.name");
	    if (jvmName.indexOf("Classic") >=  0) { 
		String classpath = System.getProperty("java.class.path");
		if (classpath.indexOf("jt400native.jar") >= 0) {
		    notApplicable("Fails with OOM error for Classic JVM and jt400native.jar");
		    return; 
		} 
	    }

      if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 24355 && getRelease() < JDTestDriver.RELEASE_V7R1M0 ) {
	  notApplicable("Native Driver and SI24355 testing");
      } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && getRelease() == JDTestDriver.RELEASE_V7R1M0 ) {
	  notApplicable("Not tested for toolbox and V5R4");
      } else {
	  checkRSMD(false);
      }
  }
  public void Var044() {

	    String jvmName = System.getProperty("java.vm.name");
	    if (jvmName.indexOf("Classic") >=  0) { 
		String classpath = System.getProperty("java.class.path");
		if (classpath.indexOf("jt400native.jar") >= 0) {
		    notApplicable("Fails with OOM error for Classic JVM and jt400native.jar");
		    return; 
		} 
	    }

      if (JDTestDriver.isLUW()) {
	  notApplicable("DB2 SQL error: SQLCODE: -954, SQLSTATE: 57011, SQLERRMC: null");
	  return;
      }


      if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 24355 && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	  notApplicable("Native Driver and SI24355 testing");
      } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && getRelease() == JDTestDriver.RELEASE_V7R1M0 ) {
	  notApplicable("Not tested for toolbox and V5R4");
      } else {

	  checkRSMD(true);
      }
  }


  // @M1A added to check column default value support in V5R5
  public void Var045()
  {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW faling with com.ibm.db2.jcc.am.SqlException: DB2 SQL error: SQLCODE: -443, SQLSTATE: 38553, SQLERRMC: SYSIBM.SQLCOLUMNS;COLUMNS;SYSIBM:CLI:-727");
	  return;
      }
      if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)
      {
          message = new StringBuffer();
          ResultSet rs = null;
          try{
              rs = dmd_.getColumns(null, JDDMDTest.COLLECTION, "DEFCOLUMNS", "%");
              int rows = 0;
              String expected = "";
              boolean success = true;
              while(rs.next()){
                  ++rows;
                  switch(rows){
                  case 1: expected = "\'DEFAULTVAL\'";
                      break;
                  case 3: expected = "\'MYDEFAULT VAL\'";
                      break;
                  case 4: expected = "4";
                      break;
                  case 6: expected = "NULL";
                      break;
                  default:
		      /* The SQL reference manual for the SQLCOLUMNS catalog */
                      /* says that COLUMN_DEF should be the null value if */
                      /* there is no default value */
		      /* Changed 8/10/2007 by native */
                      expected = null;
                      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
                    	  expected = "";  //keep prev value till we move to sysibm sp
                  }
                  success = check(rs.getString("COLUMN_DEF"), expected, "columnDef" + rows) && success;
              }
              assertCondition(success && (rows==6), "Added by toolbox 11/28/06 to test column default value. rows="+rows+" sb 6  " + message.toString());
          }catch(Exception e){
              failed(e, "Unexpected Exception.  Added 11/28/06 by toolbox to test column default value.");
          }
          finally{
              try{
                  if (rs != null) rs.close();
              }catch(Exception e){
              }
          }

      }
      else
          notApplicable("V5R5 or greater variation");
  }

    public void checkRSMD(boolean extendedMetadata)
    {

	Connection connection = connection_;
	DatabaseMetaData dmd = dmd_;
	// Note:  I had to have the test size at 40 to recreate the error on V5R4
        //        Will recreate on V5R2 with test size at 40
        //        Could not recreate on V5R3 with test size at 320
	int TESTSIZE=40;
	StringBuffer prime = new StringBuffer();

	int j=0;
	int col=0;
	String added=" -- Added 06/05/06 by native JDBC driver";
	boolean passed=true;
	message.setLength(0);
	String [][] methodTests = {

	    {"isAutoIncrement",		"1","false"},
	    {"isCaseSensitive",		"1","true"},
	    {"isSearchable",		"1","true"},
	    {"isCurrency",		"1","false"},
	    {"isNullable",		"1","0"},
	    {"isSigned",		"1","false"},
	    {"getColumnDisplaySize",	"1","18"},
	    {"getColumnLabel",		"1","TABLE_CAT"},
	    {"getColumnName",		"1","TABLE_CAT"},
	    {"getPrecision",		"1","18"},
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
	    {"isNullable",		"2","0"},
	    {"isSigned",		"2","false"},
	    {"getColumnDisplaySize",	"2","128"},
	    {"getColumnLabel",		"2","TABLE_SCHEM"},
	    {"getColumnName",		"2","TABLE_SCHEM"},
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
	    {"getColumnLabel",		"3","TABLE_NAME"},
	    {"getColumnName",		"3","TABLE_NAME"},
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
	    {"isCaseSensitive",		"4","true"},
	    {"isSearchable",		"4","true"},
	    {"isCurrency",		"4","false"},
	    {"isNullable",		"4","0"},
	    {"isSigned",		"4","false"},
	    {"getColumnDisplaySize",	"4","128"},
	    {"getColumnLabel",		"4","COLUMN_NAME"},
	    {"getColumnName",		"4","COLUMN_NAME"},
	    {"getPrecision",		"4","128"},
	    {"getScale",		"4","0"},
	    {"getCatalogName",		"4","LOCALHOST"},
	    {"getColumnType",		"4","12"},
	    {"getColumnTypeName",	"4","VARCHAR"},
	    {"isReadOnly",		"4","true"},
	    {"isWritable",		"4","false"},
	    {"isDefinitelyWritable",	"4","false"},
	    {"getColumnClassName",	"4","java.lang.String"},

	    {"isAutoIncrement",		"5","false"},
	    {"isCaseSensitive",		"5","false"},
	    {"isSearchable",		"5","true"},
	    {"isCurrency",		"5","false"},
	    {"isNullable",		"5","0"},
	    {"isSigned",		"5","true"},
	    {"getColumnDisplaySize",	"5","6"},
	    {"getColumnLabel",		"5","DATA_TYPE"},
	    {"getColumnName",		"5","DATA_TYPE"},
	    {"getPrecision",		"5","5"},
	    {"getScale",		"5","0"},
	    {"getCatalogName",		"5","LOCALHOST"},
	    {"getColumnType",		"5","5"},
	    {"getColumnTypeName",	"5","SMALLINT"},
	    {"isReadOnly",		"5","true"},
	    {"isWritable",		"5","false"},
	    {"isDefinitelyWritable",	"5","false"},
	    {"getColumnClassName",	"5","java.lang.Short"},

	    {"isAutoIncrement",		"6","false"},
	    {"isCaseSensitive",		"6","true"},
	    {"isSearchable",		"6","true"},
	    {"isCurrency",		"6","false"},
	    {"isNullable",		"6","0"},
	    {"isSigned",		"6","false"},
	    {"getColumnDisplaySize",	"6","23"},
	    {"getColumnLabel",		"6","TYPE_NAME"},
	    {"getColumnName",		"6","TYPE_NAME"},
	    {"getPrecision",		"6","23"},
	    {"getScale",		"6","0"},
	    {"getCatalogName",		"6","LOCALHOST"},
	    {"getColumnType",		"6","12"},
	    {"getColumnTypeName",	"6","VARCHAR"},
	    {"isReadOnly",		"6","true"},
	    {"isWritable",		"6","false"},
	    {"isDefinitelyWritable",	"6","false"},
	    {"getColumnClassName",	"6","java.lang.String"},

	    {"isAutoIncrement",		"7","false"},
	    {"isCaseSensitive",		"7","false"},
	    {"isSearchable",		"7","true"},
	    {"isCurrency",		"7","false"},
	    {"isNullable",		"7","1"},
	    {"isSigned",		"7","true"},
	    {"getColumnDisplaySize",	"7","11"},
	    {"getColumnLabel",		"7","COLUMN_SIZE"},
	    {"getColumnName",		"7","COLUMN_SIZE"},
	    {"getPrecision",		"7","10"},
	    {"getScale",		"7","0"},
	    {"getCatalogName",		"7","LOCALHOST"},
	    {"getColumnType",		"7","4"},
	    {"getColumnTypeName",	"7","INTEGER"},
	    {"isReadOnly",		"7","true"},
	    {"isWritable",		"7","false"},
	    {"isDefinitelyWritable",	"7","false"},
	    {"getColumnClassName",	"7","java.lang.Integer"},

	    {"isAutoIncrement",		"8","false"},
	    {"isCaseSensitive",		"8","false"},
	    {"isSearchable",		"8","true"},
	    {"isCurrency",		"8","false"},
	    {"isNullable",		"8","0"},
	    {"isSigned",		"8","true"},
	    {"getColumnDisplaySize",	"8","11"},
	    {"getColumnLabel",		"8","BUFFER_LENGTH"},
	    {"getColumnName",		"8","BUFFER_LENGTH"},
	    {"getPrecision",		"8","10"},
	    {"getScale",		"8","0"},
	    {"getCatalogName",		"8","LOCALHOST"},
	    {"getColumnType",		"8","4"},
	    {"getColumnTypeName",	"8","INTEGER"},
	    {"isReadOnly",		"8","true"},
	    {"isWritable",		"8","false"},
	    {"isDefinitelyWritable",	"8","false"},
	    {"getColumnClassName",	"8","java.lang.Integer"},

	    {"isAutoIncrement",		"9","false"},
	    {"isCaseSensitive",		"9","false"},
	    {"isSearchable",		"9","true"},
	    {"isCurrency",		"9","false"},
	    {"isNullable",		"9","1"},
	    {"isSigned",		"9","true"},
	    {"getColumnDisplaySize",	"9","11"},
	    {"getColumnLabel",		"9","DECIMAL_DIGITS"},
	    {"getColumnName",		"9","DECIMAL_DIGITS"},
	    {"getPrecision",		"9","10"},
	    {"getScale",		"9","0"},
	    {"getCatalogName",		"9","LOCALHOST"},
	    {"getColumnType",		"9","4"},
	    {"getColumnTypeName",	"9","INTEGER"},
	    {"isReadOnly",		"9","true"},
	    {"isWritable",		"9","false"},
	    {"isDefinitelyWritable",	"9","false"},
	    {"getColumnClassName",	"9","java.lang.Integer"},

	    {"isAutoIncrement",		"10","false"},
	    {"isCaseSensitive",		"10","false"},
	    {"isSearchable",		"10","true"},
	    {"isCurrency",		"10","false"},
	    {"isNullable",		"10","1"},
	    {"isSigned",		"10","true"},
	    {"getColumnDisplaySize",	"10","11"},
	    {"getColumnLabel",		"10","NUM_PREC_RADIX"},
	    {"getColumnName",		"10","NUM_PREC_RADIX"},
	    {"getPrecision",		"10","10"},
	    {"getScale",		"10","0"},
	    {"getCatalogName",		"10","LOCALHOST"},
	    {"getColumnType",		"10","4"},
	    {"getColumnTypeName",	"10","INTEGER"},
	    {"isReadOnly",		"10","true"},
	    {"isWritable",		"10","false"},
	    {"isDefinitelyWritable",	"10","false"},
	    {"getColumnClassName",	"10","java.lang.Integer"},

	    {"isAutoIncrement",		"11","false"},
	    {"isCaseSensitive",		"11","false"},
	    {"isSearchable",		"11","true"},
	    {"isCurrency",		"11","false"},
	    {"isNullable",		"11","0"},
	    {"isSigned",		"11","true"},
	    {"getColumnDisplaySize",	"11","11"},
	    {"getColumnLabel",		"11","NULLABLE"},
	    {"getColumnName",		"11","NULLABLE"},
	    {"getPrecision",		"11","10"},
	    {"getScale",		"11","0"},
	    {"getCatalogName",		"11","LOCALHOST"},
	    {"getColumnType",		"11","4"},
	    {"getColumnTypeName",	"11","INTEGER"},
	    {"isReadOnly",		"11","true"},
	    {"isWritable",		"11","false"},
	    {"isDefinitelyWritable",	"11","false"},
	    {"getColumnClassName",	"11","java.lang.Integer"},
	    {"isAutoIncrement",		"12","false"},

	    {"isCaseSensitive",		"12","true"},
	    {"isSearchable",		"12","true"},
	    {"isCurrency",		"12","false"},
	    {"isNullable",		"12",		"1"},
	    {"isSigned",		"12","false"},
	    {"getColumnDisplaySize",	"12","254"},
	    {"getColumnLabel",		"12","REMARKS"},
	    {"getColumnName",		"12","REMARKS"},
	    {"getPrecision",		"12","254"},
	    {"getScale",		"12","0"},
	    {"getCatalogName",		"12","LOCALHOST"},
	    {"getColumnType",		"12",		"12"},
	    {"getColumnTypeName",	"12","VARCHAR"},
	    {"isReadOnly",		"12","true"},
	    {"isWritable",		"12","false"},
	    {"isDefinitelyWritable",	"12","false"},
	    {"getColumnClassName",	"12","java.lang.String"},

	    {"isAutoIncrement",		"13","false"},
	    {"isCaseSensitive",		"13","true"},
	    {"isSearchable",		"13","true"},
	    {"isCurrency",		"13","false"},
	    {"isNullable",		"13",		"1"},
	    {"isSigned",		"13","false"},
	    {"getColumnDisplaySize",	"13","254"},
	    {"getColumnLabel",		"13","COLUMN_DEF"},
	    {"getColumnName",		"13","COLUMN_DEF"},
	    {"getPrecision",		"13","254"},
	    {"getScale",		"13","0"},
	    {"getCatalogName",		"13","LOCALHOST"},
	    {"getColumnType",		"13",		"12"},
	    {"getColumnTypeName",	"13","VARCHAR"},
	    {"isReadOnly",		"13","true"},
	    {"isWritable",		"13","false"},
	    {"isDefinitelyWritable",	"13","false"},
	    {"getColumnClassName",	"13","java.lang.String"},

	    {"isAutoIncrement",		"14","false"},
	    {"isCaseSensitive",		"14","false"},
	    {"isSearchable",		"14","true"},
	    {"isCurrency",		"14","false"},
	    {"isNullable",		"14",		"1"},
	    {"isSigned",		"14","true"},
	    {"getColumnDisplaySize",	"14",		"11"},
	    {"getColumnLabel",		"14","SQL_DATA_TYPE"},
	    {"getColumnName",		"14","SQL_DATA_TYPE"},
	    {"getPrecision",		"14",		"10"},
	    {"getScale",		"14","0"},
	    {"getCatalogName",		"14","LOCALHOST"},
	    {"getColumnType",		"14","4"},
	    {"getColumnTypeName",	"14","INTEGER"},
	    {"isReadOnly",		"14","true"},
	    {"isWritable",		"14","false"},
	    {"isDefinitelyWritable",	"14","false"},
	    {"getColumnClassName",	"14","java.lang.Integer"},

	    {"isAutoIncrement",		"15","false"},
	    {"isCaseSensitive",		"15","false"},
	    {"isSearchable",		"15","true"},
	    {"isCurrency",		"15","false"},
	    {"isNullable",		"15",		"1"},
	    {"isSigned",		"15","true"},
	    {"getColumnDisplaySize",	"15",		"11"},
	    {"getColumnLabel",		"15","SQL_DATETIME_SUB"},
	    {"getColumnName",		"15","SQL_DATETIME_SUB"},
	    {"getPrecision",		"15",		"10"},
	    {"getScale",		"15","0"},
	    {"getCatalogName",		"15","LOCALHOST"},
	    {"getColumnType",		"15","4"},
	    {"getColumnTypeName",	"15","INTEGER"},
	    {"isReadOnly",		"15","true"},
	    {"isWritable",		"15","false"},
	    {"isDefinitelyWritable",	"15","false"},
	    {"getColumnClassName",	"15","java.lang.Integer"},

	    {"isAutoIncrement",		"16","false"},
	    {"isCaseSensitive",		"16","false"},
	    {"isSearchable",		"16","true"},
	    {"isCurrency",		"16","false"},
	    {"isNullable",		"16","1"},
	    {"isSigned",		"16","true"},
	    {"getColumnDisplaySize",	"16",		"11"},
	    {"getColumnLabel",		"16","CHAR_OCTET_LENGTH"},
	    {"getColumnName",		"16","CHAR_OCTET_LENGTH"},
	    {"getPrecision",		"16",		"10"},
	    {"getScale",		"16","0"},
	    {"getCatalogName",		"16","LOCALHOST"},
	    {"getColumnType",		"16","4"},
	    {"getColumnTypeName",	"16","INTEGER"},
	    {"isReadOnly",		"16","true"},
	    {"isWritable",		"16","false"},
	    {"isDefinitelyWritable",	"16","false"},
	    {"getColumnClassName",	"16","java.lang.Integer"},

	    {"isAutoIncrement",		"17","false"},
	    {"isCaseSensitive",		"17","false"},
	    {"isSearchable",		"17","true"},
	    {"isCurrency",		"17","false"},
	    {"isNullable",		"17","0"},
	    {"isSigned",		"17","true"},
	    {"getColumnDisplaySize",	"17",		"11"},
	    {"getColumnLabel",		"17","ORDINAL_POSITION"},
	    {"getColumnName",		"17","ORDINAL_POSITION"},
	    {"getPrecision",		"17",		"10"},
	    {"getScale",		"17","0"},
	    {"getCatalogName",		"17","LOCALHOST"},
	    {"getColumnType",		"17","4"},
	    {"getColumnTypeName",	"17","INTEGER"},
	    {"isReadOnly",		"17","true"},
	    {"isWritable",		"17","false"},
	    {"isDefinitelyWritable",	"17","false"},
	    {"getColumnClassName",	"17","java.lang.Integer"},

	    {"isAutoIncrement",		"18","false"},
	    {"isCaseSensitive",		"18","true"},
	    {"isSearchable",		"18","true"},
	    {"isCurrency",		"18","false"},
	    {"isNullable",		"18","0"},
	    {"isSigned",		"18","false"},
	    {"getColumnDisplaySize",	"18","254"},
	    {"getColumnLabel",		"18","IS_NULLABLE"},
	    {"getColumnName",		"18","IS_NULLABLE"},
	    {"getPrecision",		"18","254"},
	    {"getScale",		"18","0"},
	    {"getCatalogName",		"18","LOCALHOST"},
	    {"getColumnType",		"18",		"12"},
	    {"getColumnTypeName",	"18","VARCHAR"},
	    {"isReadOnly",		"18","true"},
	    {"isWritable",		"18","false"},
	    {"isDefinitelyWritable",	"18","false"},
	    {"getColumnClassName",	"18","java.lang.String"},

	    {"isAutoIncrement",		"19","false"},
	    {"isCaseSensitive",		"19","true"},
	    {"isSearchable",		"19","true"},
	    {"isCurrency",		"19","false"},
	    {"isNullable",		"19","1"},
	    {"isSigned",		"19","false"},
	    {"getColumnDisplaySize",	"19","128"},
	    {"getColumnLabel",		"19","SCOPE_CATALOG"},
	    {"getColumnName",		"19","SCOPE_CATALOG"},
	    {"getPrecision",		"19","128"},
	    {"getScale",		"19","0"},
	    {"getCatalogName",		"19","LOCALHOST"},
	    {"getColumnType",		"19",		"12"},
	    {"getColumnTypeName",	"19","VARCHAR"},
	    {"isReadOnly",		"19","true"},
	    {"isWritable",		"19","false"},
	    {"isDefinitelyWritable",	"19","false"},
	    {"getColumnClassName",	"19","java.lang.String"},

	    {"isAutoIncrement",		"20","false"},
	    {"isCaseSensitive",		"20","true"},
	    {"isSearchable",		"20","true"},
	    {"isCurrency",		"20","false"},
	    {"isNullable",		"20","1"},
	    {"isSigned",		"20","false"},
	    {"getColumnDisplaySize",	"20","128"},
	    {"getColumnLabel",		"20","SCOPE_SCHEMA"},
	    {"getColumnName",		"20","SCOPE_SCHEMA"},
	    {"getPrecision",		"20","128"},
	    {"getScale",		"20","0"},
	    {"getCatalogName",		"20","LOCALHOST"},
	    {"getColumnType",		"20","12"},
	    {"getColumnTypeName",	"20","VARCHAR"},
	    {"isReadOnly",		"20","true"},
	    {"isWritable",		"20","false"},
	    {"isDefinitelyWritable",	"20","false"},
	    {"getColumnClassName",	"20","java.lang.String"},

	    {"isAutoIncrement",		"21","false"},
	    {"isCaseSensitive",		"21","true"},
	    {"isSearchable",		"21","true"},
	    {"isCurrency",		"21","false"},
	    {"isNullable",		"21","1"},
	    {"isSigned",		"21","false"},
	    {"getColumnDisplaySize",	"21","128"},
	    {"getColumnLabel",		"21","SCOPE_TABLE"},
	    {"getColumnName",		"21","SCOPE_TABLE"},
	    {"getPrecision",		"21","128"},
	    {"getScale",		"21","0"},
	    {"getCatalogName",		"21","LOCALHOST"},
	    {"getColumnType",		"21","12"},
	    {"getColumnTypeName",	"21","VARCHAR"},
	    {"isReadOnly",		"21","true"},
	    {"isWritable",		"21","false"},
	    {"isDefinitelyWritable",	"21","false"},
	    {"getColumnClassName",	"21","java.lang.String"},

	    {"isAutoIncrement",		"22","false"},
	    {"isCaseSensitive",		"22","false"},
	    {"isSearchable",		"22","true"},
	    {"isCurrency",		"22","false"},
	    {"isNullable",		"22","1"},
	    {"isSigned",		"22","true"},
	    {"getColumnDisplaySize",	"22","6"},
	    {"getColumnLabel",		"22","SOURCE_DATA_TYPE"},
	    {"getColumnName",		"22","SOURCE_DATA_TYPE"},
	    {"getPrecision",		"22","5"},
	    {"getScale",		"22","0"},
	    {"getCatalogName",		"22","LOCALHOST"},
	    {"getColumnType",		"22","5"},
	    {"getColumnTypeName",	"22","SMALLINT"},
	    {"isReadOnly",		"22","true"},
	    {"isWritable",		"22","false"},
	    {"isDefinitelyWritable",	"22","false"},
	    {"getColumnClassName",	"22","java.lang.Short"},

	};


	String[][] toolboxDifferences = {
	    {"isNullable",		"1","1"},
	    {"getColumnDisplaySize",	"1","128"},
	    {"getPrecision",		"1","128"},
	    {"isNullable",		"2","1"},
	    {"getColumnClassName",	"5","java.lang.Integer"},
	    {"getColumnDisplaySize",	"6","128"},
	    {"getPrecision",		"6","128"},
	    {"isNullable",		"7","0"},
	    {"isNullable",		"9","0"},
	    {"isNullable",		"10","0"},
	    {"isNullable",		"14","0"},
	    {"isNullable",		"15","0"},
	    {"isNullable",		"16","0"},
	    {"getColumnClassName",	"22","java.lang.Integer"},
	};

        String[][] toolboxDifferences61 = {
	    {"isNullable",		"1","1"},
	    {"getColumnDisplaySize",	"1","128"},
	    {"getPrecision",		"1","128"},
	    {"isNullable",		"2","1"},
	    {"getColumnClassName",	"5","java.lang.Integer"},
	    {"getColumnDisplaySize",	"6","128"},
	    {"getPrecision",		"6","128"},
	    {"isNullable",		"7","0"},
	    {"isNullable",		"9","0"},
	    {"isNullable",		"10","0"},
            {"getColumnDisplaySize",    "13","2000"},
	    {"getPrecision",		"13","2000"},
	    {"isNullable",		"14","0"},
	    {"isNullable",		"15","0"},
	    {"isNullable",		"16","0"},
	    {"getColumnClassName",	"22","java.lang.Integer"},
	};    // @M1A

	String [][] nativeExtendedDifferences = {
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
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},
	    {"isSearchable","18","false"},
	};

	String [][] nativeExtendedDifferences5440 = {
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
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},
	    {"isSearchable","18","false"},

	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
	    {"isNullable","7","0"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isSearchable","14","false"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"isSearchable","15","false"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"isSearchable","19","false"},
	    {"getColumnLabel","19","SCOPE_CATLOG"},
	    {"getColumnName","19","SCOPE_CATLOG"},
	    {"isSearchable","20","false"},
	    {"isSearchable","21","false"},
	    {"isSearchable","22","false"},
	    {"getColumnClassName","22","java.lang.Integer"},
	};

	String [][] nativeExtendedDifferences61 = {
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
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},
	    {"isSearchable","18","false"},

	    // Updated 3/20/2018 .. running on fowgai2
	    {"isSearchable","14","false"},
	    {"isSearchable","15","false"},
	    {"isSearchable","19","false"},
	    {"isSearchable","20","false"},
	    {"isSearchable","21","false"},
	    {"isSearchable","22","false"},

	    // From native40differences55
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
	    {"getColumnDisplaySize","5","11"},   /* Changed 01/2010 by SYSIBM PTF */
	    {"getPrecision","5","10"},           /* Changed 01/2010 by SYSIBM PTF */
	    {"getColumnType","5","4"},           /* Changed 01/2010 by SYSIBM PTF */
	    {"getColumnTypeName","5","INTEGER"}, /* Changed 01/2010 by SYSIBM PTF */
	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
            /* Changed 4/22/2008 for V6R1 */
	    {"isNullable","6","0"},
	    /* isNullable 7 changed for V6R1 3/20/2012 */
	    {"isNullable","7","1"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATLOG"},
	    {"getColumnName","19","SCOPE_CATLOG"},
	    {"getColumnClassName","22","java.lang.Integer"}


	};


	String [][] nativeExtendedDifferences6140 = {

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
	    {"isSearchable","21","false"},
	    {"isSearchable","22","false"},

	    /// from native40differences71
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
            /* 09/21/2010 Back to smallint on lp08ut23 */
            /* 02/22/2011 Back to int      on lp08ut23 for java test.JDRunit 7146N JDDMDGetColumns */
            /* 01/11/2012 Back to smallint on lp01ut18 for java test.JDRunit 7173D JDDMDGetColumns */
	    // {"getColumnDisplaySize","5","11"},
	    // {"getPrecision","5","10"},
	    // {"getColumnType","5","4"},
	    // {"getColumnTypeName","5","INTEGER"},

	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},

	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
            /* Changed 4/22/2008 for V6R1 */
	    {"isNullable","6","0"},
	    /* Change 4/22/2011 for V6R1 */
	    {"isNullable","7","1"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnTypeName","12","VARCHAR"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnTypeName","13","VARCHAR"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATLOG"},
	    {"getColumnName","19","SCOPE_CATLOG"},
	    {"getColumnClassName","22","java.lang.Integer"}




        };

	    //same as nativeExtendedDifferences6140, but one label changed to ""
	    String [][] toolboxExtendedDifferencesv7130 = {
		{"getPrecision","12","2000"},
		{"getPrecision","13","2000"},

		// from jdbc30differences 71
	        {"getColumnDisplaySize","1","128"},
	        {"getPrecision","1","128"},
	        {"getColumnClassName","5","java.lang.Integer"},
	        {"getColumnDisplaySize", "5","11"},
	        {"getPrecision","5","10"},
	        {"getColumnType","5","4"},
	        {"getColumnTypeName","5","INTEGER"},
	        {"getColumnDisplaySize","6","261"},
	        {"getPrecision","6","261"},
	            /* Changed 4/22/2008 for V6R1 */
	        {"isNullable","6","0"},
	        {"isNullable","7","1"},
	        {"getColumnDisplaySize","9","6"},
	        {"getPrecision","9","5"},
	        {"getColumnType","9","5"},
	        {"getColumnTypeName","9","SMALLINT"},
	        {"getColumnDisplaySize","10","6"},
	        {"getPrecision","10","5"},
	        {"getColumnType","10","5"},
	        {"getColumnTypeName","10","SMALLINT"},
	        {"getColumnDisplaySize","11","6"},
	        {"getPrecision","11","5"},
	        {"getColumnType","11","5"},
	        {"getColumnTypeName","11","SMALLINT"},
	        {"getColumnDisplaySize","12","2000"},
	        {"getPrecision","12","2000"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"getPrecision","13","2000"},
	        {"getColumnDisplaySize","13","2000"},
	        {"isNullable","14","0"},
	        {"getColumnDisplaySize","14","6"},
	        {"getPrecision","14","5"},
	        {"getColumnType","14","5"},
	        {"getColumnTypeName","14","SMALLINT"},
	        {"getColumnDisplaySize","15","6"},
	        {"getPrecision","15","5"},
	        {"getColumnType","15","5"},
	        {"getColumnTypeName","15","SMALLINT"},
	        {"getColumnDisplaySize","18","3"},
	        {"getPrecision","18","3"},
	        {"getColumnLabel","19","SCOPE_CATLOG"},
	        {"getColumnName","19","SCOPE_CATLOG"},
	        {"getColumnClassName","22","java.lang.Integer"},

		// From native Differences 5440
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"getColumnClassName","22","java.lang.Integer"}



	    };



	    String [][] toolboxExtendedDifferencesv726T = {
		{"getColumnDisplaySize","1","128"},  /* Added 6/16/2014 */ 
		{"getPrecision","1","128"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnType","12","-9"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnType","13","-9"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"getColumnClassName","22","java.lang.Integer"},

	    };


	    String [][] toolboxExtendedDifferencesv727T = {
		{"getColumnDisplaySize","1","128"},  /* Added 6/16/2014 */ 
		{"getPrecision","1","128"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnType","12","-9"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnType","13","-9"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnLabel","19","SCOPE_CATALOG"},
		{"getColumnName","19","SCOPE_CATALOG"},
		{"getColumnClassName","22","java.lang.Integer"},

	    };



	     /* Updated March 2022 when DATA_TYPE correct to INTEGER */ 
	    String [][] toolboxExtendedDifferencesv737T = {
		{"getColumnDisplaySize","1","128"},  /* Added 6/16/2014 */ 
		{"getPrecision","1","128"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","5","11"},
		{"getPrecision","5","10"},
		{"getColumnType","5","4"},
		{"getColumnTypeName","5","INTEGER"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnType","12","-9"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnType","13","-9"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnName","19","SCOPE_CATALOG"},
		{"getColumnClassName","22","java.lang.Integer"},

		{"getColumnLabel","1","Table Cat"},
		{"getColumnLabel","2","Table Schem"},
		{"getColumnLabel","3","Table Name"},
		{"getColumnLabel","4","Column Name"},
		{"getColumnLabel","7","Column Size"},
		{"getColumnLabel","8","Buffer Length"},
		{"getColumnLabel","9","Decimal Digits"},
		{"getColumnLabel","10","Num Prec Radix"},
		{"getColumnLabel","11","Nullable"},
		{"getColumnLabel","12","Remarks"},
		{"getColumnLabel","13","Column Def"},
		{"getColumnLabel","15","SQL Datetime Sub"},
		{"getColumnLabel","16","Char Octet Length"},
		{"getColumnLabel","17","Ordinal Position"},
		{"getColumnLabel","18","Is Nullable"},
		{"getColumnLabel","19","Scope Catalog"},
		{"getColumnLabel","20","Scope Schema"},
		{"getColumnLabel","21","Scope Table"},
		{"getColumnLabel","22","Source Data Type"},
	    };



	    String [][] jtopenliteDifferences726L = {
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"getColumnType","6","-1"},
		{"getColumnTypeName","6","LONG VARCHAR"},
		{"isNullable","7","0"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"getColumnClassName","22","java.lang.Integer"}
	    };
	String [][] nativeDifferences5440 = {
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
	    {"isNullable","7","0"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATLOG"},
	    {"getColumnName","19","SCOPE_CATLOG"},
	    {"getColumnClassName","22","java.lang.Integer"}
	};

	String [][] nativeDifferences6130 = {

   {"getColumnDisplaySize","1","128"},
      {"getPrecision","1","128"},
      {"getColumnDisplaySize","5","11"},
      {"getPrecision","5","10"},
      {"getColumnType","5","4"},
      {"getColumnTypeName","5","INTEGER"},
      {"getColumnClassName","5","java.lang.Integer"},
      {"getColumnDisplaySize","6","261"},
      {"getPrecision","6","261"},
      {"getColumnDisplaySize","9","6"},
      {"getPrecision","9","5"},
      {"getColumnType","9","5"},
      {"getColumnTypeName","9","SMALLINT"},
      {"getColumnDisplaySize","10","6"},
      {"getPrecision","10","5"},
      {"getColumnType","10","5"},
      {"getColumnTypeName","10","SMALLINT"},
      {"getColumnDisplaySize","11","6"},
      {"getPrecision","11","5"},
      {"getColumnType","11","5"},
      {"getColumnTypeName","11","SMALLINT"},
      {"getColumnDisplaySize","12","2000"},
      {"getPrecision","12","2000"},
      {"getColumnDisplaySize","13","2000"},
      {"getPrecision","13","2000"},
      {"isNullable","14","0"},
      {"getColumnDisplaySize","14","6"},
      {"getPrecision","14","5"},
      {"getColumnType","14","5"},
      {"getColumnTypeName","14","SMALLINT"},
      {"getColumnDisplaySize","15","6"},
      {"getPrecision","15","5"},
      {"getColumnType","15","5"},
      {"getColumnTypeName","15","SMALLINT"},
      {"getColumnDisplaySize","18","3"},
      {"getPrecision","18","3"},
      {"getColumnLabel","19","SCOPE_CATLOG"},
      {"getColumnName","19","SCOPE_CATLOG"},
      {"getColumnClassName","22","java.lang.Integer"},
	};

	String [][] nativeDifferences6140 = {
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
            /* Changed 4/22/2008 for V6R1 */
	    {"isNullable","6","0"},
	    /* isNullable 7 changed for V6R1 3/20/2012 */
	    {"isNullable","7","1"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATLOG"},
	    {"getColumnName","19","SCOPE_CATLOG"},
	    {"getColumnClassName","22","java.lang.Integer"}
	};

	String [][] toolboxDifferences7140 = {
	    {"getColumnDisplaySize","1","128"},    	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
	    {"getColumnDisplaySize","5","6"},       	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},           	    {"getColumnTypeName","5","SMALLINT"},
	    {"getColumnDisplaySize","6","261"}, 	    {"getPrecision","6","261"},
	    {"isNullable","6","0"},
	    {"isNullable","7","1"}, 
	    {"getColumnDisplaySize","9","6"},	            {"getPrecision","9","5"},
	    {"getColumnType","9","5"},                     {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},		    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},                    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},     	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},                     {"getColumnTypeName","11","SMALLINT"},
  	    {"getColumnDisplaySize","12","2000"}, 	    {"getPrecision","12","2000"},
	    {"getColumnType","12","-9"},                    {"getColumnTypeName","12","NVARCHAR"},
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},   	    {"getColumnDisplaySize","13","2000"},
            {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},    	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},                     {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},    	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},          	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},    	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATLOG"}, 	    {"getColumnName","19","SCOPE_CATLOG"},
	    {"getColumnClassName","22","java.lang.Integer"}
	};

  String [][] toolboxDifferences7142 = {
      {"getColumnDisplaySize","1","128"},      {"getPrecision","1","128"},
      {"getColumnClassName","5","java.lang.Integer"},
      {"getColumnDisplaySize","5","6"},         {"getPrecision","5","5"},
      {"getColumnType","5","5"},                {"getColumnTypeName","5","SMALLINT"},
      {"getColumnDisplaySize","6","261"},       {"getPrecision","6","261"},
      {"isNullable","6","0"},
      {"isNullable","7","1"},  
      {"getColumnDisplaySize","9","6"},       {"getPrecision","9","5"},
      {"getColumnType","9","5"},      {"getColumnTypeName","9","SMALLINT"},
      {"getColumnDisplaySize","10","6"},       {"getPrecision","10","5"},
      {"getColumnType","10","5"},      {"getColumnTypeName","10","SMALLINT"},
      {"getColumnDisplaySize","11","6"},      {"getPrecision","11","5"},
      {"getColumnType","11","5"},
      {"getColumnTypeName","11","SMALLINT"},
      {"getColumnDisplaySize","12","2000"},
      {"getPrecision","12","2000"},
      {"getColumnType","12","-9"},                    
      {"getColumnTypeName","12","NVARCHAR"},
      {"getColumnType","13","-9"},
	    
      {"getColumnTypeName","13","NVARCHAR"},
      {"getColumnDisplaySize","13","2000"},
      {"getPrecision","13","2000"},
      {"isNullable","14","0"},
      {"getColumnDisplaySize","14","6"},
      {"getPrecision","14","5"},
      {"getColumnType","14","5"},
      {"getColumnTypeName","14","SMALLINT"},
      {"getColumnDisplaySize","15","6"},
      {"getPrecision","15","5"},
      {"getColumnType","15","5"},
      {"getColumnTypeName","15","SMALLINT"},
      {"getColumnDisplaySize","18","3"},
      {"getPrecision","18","3"},
      {"getColumnLabel","19","SCOPE_CATALOG"},
      {"getColumnName","19","SCOPE_CATALOG"},
      {"getColumnClassName","22","java.lang.Integer"}
  };






	String [][] nativeDifferences7140 = {
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
            /* 09/21/2010 Back to smallint on lp08ut23 */
            /* 02/22/2011 Back to int      on lp08ut23 for java test.JDRunit 7146N JDDMDGetColumns */
            /* 01/11/2012 Back to smallint on lp01ut18 for java test.JDRunit 7173D JDDMDGetColumns */
	    // {"getColumnDisplaySize","5","11"},
	    // {"getPrecision","5","10"},
	    // {"getColumnType","5","4"},
	    // {"getColumnTypeName","5","INTEGER"},

	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},

	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
            /* Changed 4/22/2008 for V6R1 */
	    {"isNullable","6","0"},
	    /* Changed to "7","1" on 11/7/2013 for lp01ut18 717N */ 
	    {"isNullable","7","1"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnTypeName","12","NVARCHAR"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnType","12","-9"},
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATLOG"},
	    {"getColumnName","19","SCOPE_CATLOG"},
	    {"getColumnClassName","22","java.lang.Integer"}
	};





	String [][] nativeDifferences726NX = {
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},

	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
	    {"isNullable","6","0"},
	    {"isNullable","7","1"},
	    {"isNullable","8","0"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnTypeName","12","NVARCHAR"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnType","12","-9"},
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATLOG"},
	    {"getColumnName","19","SCOPE_CATLOG"},
	    {"getColumnClassName","22","java.lang.Integer"}
	};



	String [][] nativeDifferences727NX = {
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},

	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
	    {"isNullable","6","0"},
	    {"isNullable","7","1"},
	    {"isNullable","8","0"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnTypeName","12","NVARCHAR"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnType","12","-9"},
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATALOG"},
	    {"getColumnName","19","SCOPE_CATALOG"},
	    {"getColumnClassName","22","java.lang.Integer"}
	};


	String [][] nativeDifferences737NX = {
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
	    {"getColumnDisplaySize","5","6"},

	     {"getColumnDisplaySize","5","11"},
	    {"getPrecision","5","10"},
	    {"getColumnType","5","4"},
	    {"getColumnTypeName","5","INTEGER"},

	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
	    {"isNullable","6","0"},
	    {"isNullable","7","1"},
	    {"isNullable","8","0"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnTypeName","12","NVARCHAR"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnType","12","-9"},
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATALOG"},
	    {"getColumnName","19","SCOPE_CATALOG"},
	    {"getColumnClassName","22","java.lang.Integer"}
	};




	String [][] nativeDifferences726N = {
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
 
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},

	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
            /* Changed 4/22/2008 for V6R1 */
	    {"isNullable","6","0"},
	    {"isNullable","7","0"},
	    {"isNullable","8","1"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnTypeName","12","NVARCHAR"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnType","12","-9"},
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATLOG"},
	    {"getColumnName","19","SCOPE_CATLOG"},
	    {"getColumnClassName","22","java.lang.Integer"}
	};


	String [][] nativeDifferences727N = {
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
 
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},

	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
            /* Changed 4/22/2008 for V6R1 */
	    {"isNullable","6","0"},
	    {"isNullable","7","0"},
	    {"isNullable","8","1"},
	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnTypeName","12","NVARCHAR"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATALOG"},
	    {"getColumnName","19","SCOPE_CATALOG"},
	    {"getColumnClassName","22","java.lang.Integer"}
	};


	
	 String [][] nativeDifferences737N = {
	      {"getColumnDisplaySize","1","128"},
	      {"getPrecision","1","128"},
	      {"getColumnClassName","5","java.lang.Integer"},
	 
	      {"getColumnDisplaySize","5","6"},
	      {"getPrecision","5","5"},
	      {"getColumnType","5","5"},
	      {"getColumnTypeName","5","SMALLINT"},

	      {"getColumnDisplaySize","6","261"},
	      {"getPrecision","6","261"},
	            /* Changed 4/22/2008 for V6R1 */
	      {"isNullable","6","0"},
	      {"isNullable","7","0"},
	      {"isNullable","8","1"},
	      {"getColumnDisplaySize","9","6"},
	      {"getPrecision","9","5"},
	      {"getColumnType","9","5"},
	      {"getColumnTypeName","9","SMALLINT"},
	      {"getColumnDisplaySize","10","6"},
	      {"getPrecision","10","5"},
	      {"getColumnType","10","5"},
	      {"getColumnTypeName","10","SMALLINT"},
	      {"getColumnDisplaySize","11","6"},
	      {"getPrecision","11","5"},
	      {"getColumnType","11","5"},
	      {"getColumnTypeName","11","SMALLINT"},
	      {"getColumnTypeName","12","NVARCHAR"},
	      {"getColumnDisplaySize","12","2000"},
	      {"getPrecision","12","2000"},
	      {"getColumnTypeName","13","NVARCHAR"},
	      {"getColumnDisplaySize","13","2000"},
	      {"getPrecision","13","2000"},
	      {"isNullable","14","0"},
	      {"getColumnDisplaySize","14","6"},
	      {"getPrecision","14","5"},
	      {"getColumnType","14","5"},
	      {"getColumnTypeName","14","SMALLINT"},
	      {"getColumnDisplaySize","15","6"},
	      {"getPrecision","15","5"},
	      {"getColumnType","15","5"},
	      {"getColumnTypeName","15","SMALLINT"},
	      {"getColumnDisplaySize","18","3"},
	      {"getPrecision","18","3"},
	      {"getColumnName","19","SCOPE_CATALOG"},
	      {"getColumnClassName","22","java.lang.Integer"},

	/* Fix 4/20/2019 for V7R3 */ 
	      {"getColumnLabel","1","Table               Cat"},
	      {"getColumnLabel","2","Table               Schem"},
	      {"getColumnLabel","3","Table               Name"},
	      {"getColumnLabel","4","Column              Name"},
	      {"getColumnLabel","7","Column              Size"},
	      {"getColumnLabel","8","Buffer              Length"},
	      {"getColumnLabel","9","Decimal             Digits"},
	      {"getColumnLabel","10","Num                 Prec                Radix"},
	      {"getColumnLabel","11","Nullable"},
	      {"getColumnLabel","12","Remarks"},
	      {"getColumnLabel","13","Column              Def"},
	      {"getColumnLabel","15","SQL                 Datetime            Sub"},
	      {"getColumnLabel","16","Char                Octet               Length"},
	      {"getColumnLabel","17","Ordinal             Position"},
	      {"getColumnLabel","18","Is                  Nullable"},
	      {"getColumnLabel","19","Scope               Catalog"},
	      {"getColumnLabel","20","Scope               Schema"},
	      {"getColumnLabel","21","Scope               Table"},
	      {"getColumnLabel","22","Source              Data                Type"},
	  };


	
	
	String [][] nativeDifferences7130 = {
	    {"getColumnDisplaySize","1","128"},
	    {"getPrecision","1","128"},
	    {"getColumnClassName","5","java.lang.Integer"},
            /* 02/22/2011 Back to int      on lp08ut23 for java test.JDRunit 7146N JDDMDGetColumns */
	     {"getColumnDisplaySize","5","11"},
	     {"getPrecision","5","10"},
	     {"getColumnType","5","4"},
	     {"getColumnTypeName","5","INTEGER"},


	    {"getColumnDisplaySize","6","261"},
	    {"getPrecision","6","261"},
            /* Changed 4/22/2008 for V6R1 */
	    {"isNullable","6","0"},
	    /* Changed 10/14/2013 for V7R1 */ 
	    {"isNullable","7","1"},

	    {"getColumnDisplaySize","9","6"},
	    {"getPrecision","9","5"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"getColumnDisplaySize","10","6"},
	    {"getPrecision","10","5"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"getColumnDisplaySize","11","6"},
	    {"getPrecision","11","5"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"getColumnTypeName","12","NVARCHAR"},
	    {"getColumnDisplaySize","12","2000"},
	    {"getPrecision","12","2000"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getPrecision","13","2000"},
	    {"isNullable","14","0"},
	    {"getColumnDisplaySize","14","6"},
	    {"getPrecision","14","5"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"getColumnDisplaySize","15","6"},
	    {"getPrecision","15","5"},
	    {"getColumnType","15","5"},
	    {"getColumnTypeName","15","SMALLINT"},
	    {"getColumnDisplaySize","18","3"},
	    {"getPrecision","18","3"},
	    {"getColumnLabel","19","SCOPE_CATLOG"},
	    {"getColumnName","19","SCOPE_CATLOG"},
	    {"getColumnClassName","22","java.lang.Integer"}
	};


	String [][] fixup546TX = {
			{"isNullable",		"1","1"},
		    {"getColumnDisplaySize",	"1","128"},
		    {"getPrecision",		"1","128"},
		    {"isNullable",		"2","1"},
		    {"getColumnClassName",	"5","java.lang.Integer"},
		    {"getColumnDisplaySize",	"6","128"},
		    {"getPrecision",		"6","128"},
		    {"isNullable",		"7","0"},
		    {"isNullable",		"9","0"},
		    {"getColumnDisplaySize",    "9","6"},
		    {"getColumnTypeName","9", "SMALLINT"},
		    {"getColumnType","9","5"},
		    {"getPrecision",		"9","5"},
		    {"isNullable",		"10","0"},
		    {"getColumnDisplaySize","10","6"},
		    {"getColumnType","10","5"},
		    {"getColumnTypeName","10","SMALLINT"},
		    {"getPrecision","10","5"},
		    {"getColumnDisplaySize","11","6"},
		    {"getPrecision","11","5"},
		    {"getColumnType","11","5"},
		    {"getColumnTypeName","11","SMALLINT"},
	            {"getColumnDisplaySize",    "13","254"},
		    {"getPrecision",		"13","254"},
		    {"getColumnDisplaySize","14","6"},
		    {"getPrecision","14","5"},
		    {"getColumnType","14","5"},
		    {"getColumnTypeName","14","SMALLINT"},
		    {"isNullable",		"14","0"},
		    {"isNullable",		"15","0"},
		    {"getColumnDisplaySize","15","6"},
		    {"getPrecision","15","5"},
		    {"getColumnType","15","5"},
		    {"getColumnTypeName","15","SMALLINT"},
		    {"isNullable",		"16","0"},
		    {"getColumnLabel","19","SCOPE_CATLOG"},
		    {"getColumnName","19","SCOPE_CATLOG"},
		    {"getColumnClassName",	"22","java.lang.Integer"},
		};

	String [][] toolboxDifferences6140 = {
			{"isNullable",		"1","1"},
		    {"getColumnDisplaySize",	"1","128"},
		    {"getPrecision",		"1","128"},
		    {"isNullable",		"2","1"},
		    {"getColumnClassName",	"5","java.lang.Integer"},
		    {"getColumnDisplaySize",	"6","128"},
		    {"getPrecision",		"6","128"},
		    {"isNullable",		"7","0"},
		    {"isNullable",		"9","0"},
		    {"getColumnDisplaySize",    "9","6"},
		    {"getColumnTypeName","9", "SMALLINT"},
		    {"getColumnType","9","5"},
		    {"getPrecision",		"9","5"},
		    {"isNullable",		"10","0"},
		    {"getColumnDisplaySize","10","6"},
		    {"getColumnType","10","5"},
		    {"getColumnTypeName","10","SMALLINT"},
		    {"getPrecision","10","5"},
		    {"getColumnDisplaySize","11","6"},
		    {"getPrecision","11","5"},
		    {"getColumnType","11","5"},
		    {"getColumnTypeName","11","SMALLINT"},
	            {"getColumnDisplaySize",    "13","2000"},
		    {"getPrecision",		"13","2000"},
		    {"getColumnDisplaySize","14","6"},
		    {"getPrecision","14","5"},
		    {"getColumnType","14","5"},
		    {"getColumnTypeName","14","SMALLINT"},
		    {"isNullable",		"14","0"},
		    {"isNullable",		"15","0"},
		    {"getColumnDisplaySize","15","6"},
		    {"getPrecision","15","5"},
		    {"getColumnType","15","5"},
		    {"getColumnTypeName","15","SMALLINT"},
		    {"isNullable",		"16","0"},
		    {"getColumnLabel","19","SCOPE_CATLOG"},
		    {"getColumnName","19","SCOPE_CATLOG"},
		    {"getColumnClassName",	"22","java.lang.Integer"},
		};
        String [][] jdbcDifferences7130 = {
	        {"getColumnDisplaySize","1","128"},
	        {"getPrecision","1","128"},
	        {"getColumnDisplaySize", "5","11"},
	        {"getPrecision","5","10"},
	        {"getColumnType","5","4"},
	        {"getColumnTypeName","5","INTEGER"},
	        {"getColumnClassName","5","java.lang.Integer"},
	        {"getColumnDisplaySize","6","261"},
	        {"getPrecision","6","261"},
	            /* Changed 4/22/2008 for V6R1 */
	        {"isNullable","6","0"},
	        {"isNullable","7","1"},  /*change 4/15/2014 for 71 x1423p1 */
	        {"getColumnDisplaySize","9","6"},
	        {"getPrecision","9","5"},
	        {"getColumnType","9","5"},
	        {"getColumnTypeName","9","SMALLINT"},
	        {"getColumnDisplaySize","10","6"},
	        {"getPrecision","10","5"},
	        {"getColumnType","10","5"},
	        {"getColumnTypeName","10","SMALLINT"},
	        {"getColumnDisplaySize","11","6"},
	        {"getPrecision","11","5"},
	        {"getColumnType","11","5"},
	        {"getColumnTypeName","11","SMALLINT"},
	        {"getColumnDisplaySize","12","2000"},
	        {"getPrecision","12","2000"},
		{"getPrecision","13","2000"},
	        {"getColumnDisplaySize","13","2000"},

	        {"isNullable","14","0"},
	        {"getColumnDisplaySize","14","6"},
	        {"getPrecision","14","5"},
	        {"getColumnType","14","5"},
	        {"getColumnTypeName","14","SMALLINT"},
	        {"getColumnDisplaySize","15","6"},
	        {"getPrecision","15","5"},
	        {"getColumnType","15","5"},
	        {"getColumnTypeName","15","SMALLINT"},
	        {"getColumnDisplaySize","18","3"},
	        {"getPrecision","18","3"},
	        {"getColumnLabel","19","SCOPE_CATLOG"},
	        {"getColumnName","19","SCOPE_CATLOG"},
	        {"getColumnClassName","22","java.lang.Integer"}
	    };


	    String[][] fixup715TS = {
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"getColumnDisplaySize","5","11"},
		{"getPrecision","5","10"},
		{"getColumnType","5","4"},
		{"getColumnTypeName","5","INTEGER"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isNullable","7","1"},  /* Updated 4/30/2014 lp01ut18 */ 
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };


	    String[][] fixup716TS = {
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isNullable","7","1"},  /* 4/16/2014 x1423p1 */
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnType","12","-9"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnType","13","-9"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };


	    String[][] fixup717TS = {
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isNullable","7","1"},  /* 4/16/2014 x1423p1 */
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{ "getColumnTypeName","12","NVARCHAR"},
		{"getColumnType","12","-9"},
		{"getColumnType","13","-9"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnLabel","19","SCOPE_CATALOG"},
		{"getColumnName","19","SCOPE_CATALOG"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };



	    String[][] fixup726TS = {
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isNullable","7","1"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnType","12","-9"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnType","13","-9"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };

	     String[][] fixup728TS = {
	         {"getColumnDisplaySize","1","128"},
	         {"getPrecision","1","128"},
	         {"getColumnClassName","5","java.lang.Integer"},
	         {"getColumnDisplaySize","6","261"},
	         {"getPrecision","6","261"},
	         {"isNullable","7","1"},
	         {"getColumnDisplaySize","9","6"},
	         {"getPrecision","9","5"},
	         {"getColumnType","9","5"},
	         {"getColumnTypeName","9","SMALLINT"},
	         {"getColumnDisplaySize","10","6"},
	         {"getPrecision","10","5"},
	         {"getColumnType","10","5"},
	         {"getColumnTypeName","10","SMALLINT"},
	         {"getColumnDisplaySize","11","6"},
	         {"getPrecision","11","5"},
	         {"getColumnType","11","5"},
	         {"getColumnTypeName","11","SMALLINT"},
	         {"getColumnDisplaySize","12","2000"},
	         {"getPrecision","12","2000"},
		 {"getColumnType","12","-9"},
		 {"getColumnTypeName","12","NVARCHAR"},
		 {"getColumnType","13","-9"},
		 {"getColumnTypeName","13","NVARCHAR"},
	         {"getColumnDisplaySize","13","2000"},
	         {"getPrecision","13","2000"},
	         {"isNullable","14","0"},
	         {"getColumnDisplaySize","14","6"},
	         {"getPrecision","14","5"},
	         {"getColumnType","14","5"},
	         {"getColumnTypeName","14","SMALLINT"},
	         {"getColumnDisplaySize","15","6"},
	         {"getPrecision","15","5"},
	         {"getColumnType","15","5"},
	         {"getColumnTypeName","15","SMALLINT"},
	         {"getColumnDisplaySize","18","3"},
	         {"getPrecision","18","3"},
	         {"getColumnLabel","19","SCOPE_CATALOG"},
	         {"getColumnName","19","SCOPE_CATALOG"},
	         {"getColumnClassName","22","java.lang.Integer"},
	           };


             /* DATA_TYPE column changed to int March 2022 */ 
	     String[][] fixup738TS = {
	         {"getColumnDisplaySize","1","128"},
	         {"getPrecision","1","128"},
	         {"getColumnClassName","5","java.lang.Integer"},
		 {"getColumnDisplaySize","5","11"},
		 {"getPrecision","5","10"},
		 {"getColumnType","5","4"},
		 {"getColumnTypeName","5","INTEGER"},
	         {"getColumnDisplaySize","6","261"},
	         {"getPrecision","6","261"},
	         {"isNullable","7","1"},
	         {"getColumnDisplaySize","9","6"},
	         {"getPrecision","9","5"},
	         {"getColumnType","9","5"},
	         {"getColumnTypeName","9","SMALLINT"},
	         {"getColumnDisplaySize","10","6"},
	         {"getPrecision","10","5"},
	         {"getColumnType","10","5"},
	         {"getColumnTypeName","10","SMALLINT"},
	         {"getColumnDisplaySize","11","6"},
	         {"getPrecision","11","5"},
	         {"getColumnType","11","5"},
	         {"getColumnTypeName","11","SMALLINT"},
	         {"getColumnDisplaySize","12","2000"},
	         {"getPrecision","12","2000"},
		 {"getColumnType","12","-9"},
		 {"getColumnTypeName","12","NVARCHAR"},
		 {"getColumnType","13","-9"},
		 {"getColumnTypeName","13","NVARCHAR"},
	         {"getColumnDisplaySize","13","2000"},
	         {"getPrecision","13","2000"},
	         {"isNullable","14","0"},
	         {"getColumnDisplaySize","14","6"},
	         {"getPrecision","14","5"},
	         {"getColumnType","14","5"},
	         {"getColumnTypeName","14","SMALLINT"},
	         {"getColumnDisplaySize","15","6"},
	         {"getPrecision","15","5"},
	         {"getColumnType","15","5"},
	         {"getColumnTypeName","15","SMALLINT"},
	         {"getColumnDisplaySize","18","3"},
	         {"getPrecision","18","3"},
	         {"getColumnLabel","19","SCOPE_CATALOG"},
	         {"getColumnName","19","SCOPE_CATALOG"},
	         {"getColumnClassName","22","java.lang.Integer"},
	           };

	    
	    String[][] toolboxDifferences714TS = {

		{"getPrecision","1","128"},
		{"getColumnDisplaySize","1","128"},
		{"getColumnDisplaySize","5","11"},
		{"getPrecision","5","10"},
		{"getColumnType","5","4"},
		{"getColumnTypeName","5","INTEGER"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isNullable","7","1"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"getColumnClassName","22","java.lang.Integer"},
	    }; 

	    String[][] nativeExtendedDifferences7230 =  {
		{"isSearchable","1","false"},
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"isSearchable","2","false"},
		{"isSearchable","3","false"},
		{"isSearchable","4","false"},
		{"isSearchable","5","false"},
		{"getColumnDisplaySize","5","11"},
		{"getPrecision","5","10"},
		{"getColumnType","5","4"},
		{"getColumnTypeName","5","INTEGER"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"isSearchable","6","false"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isSearchable","7","false"},
		{"isNullable","7","0"},
		{"isSearchable","8","false"},
		{"isSearchable","9","false"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"isSearchable","10","false"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"isSearchable","11","false"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"isSearchable","12","false"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"isSearchable","13","false"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"isSearchable","14","false"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"isSearchable","15","false"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"isSearchable","16","false"},
		{"isSearchable","17","false"},
		{"isSearchable","18","false"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"isSearchable","19","false"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"isSearchable","20","false"},
		{"isSearchable","21","false"},
		{"isSearchable","22","false"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };

	    Object[][] fixupArray = {
		{ "546TX", fixup546TX},
		{ "543TX", toolboxDifferences},
		{ "544TX", toolboxDifferences},
		{ "545TX", toolboxDifferences},

		{ "613TX", toolboxDifferences61},
		{ "614TX", toolboxDifferences61},
		{ "615TX", toolboxDifferences61},
		{ "616TX", toolboxDifferences61},
		{ "617TX", toolboxDifferences61},
		{ "618TX", toolboxDifferences61},
		{ "713TX", toolboxDifferences61},
		{ "714TX", toolboxDifferences61},
		{ "715TX", toolboxDifferences61},
		{ "714TS", toolboxDifferences714TS},
		{ "723TX", toolboxDifferences61},
		{ "724TX", toolboxDifferences61},
		{ "725TX", toolboxDifferences61},

		{ "616TX", toolboxDifferences6140},
		{ "617TX", toolboxDifferences6140},
		{ "617TX", toolboxDifferences6140},
		{ "716TX", toolboxDifferences6140},
		{ "717TX", toolboxDifferences6140},
		{ "718TX", toolboxDifferences6140},
		{ "719TX", toolboxDifferences6140},
		{ "726TX", toolboxDifferences6140},
		{ "727TX", toolboxDifferences6140},

		{ "715TS", fixup715TS},
		{ "716TS", fixup716TS},
		{ "717TS", fixup717TS},
		{ "718TS", fixup717TS},
		{ "719TS", fixup717TS},
		{ "726TS", fixup726TS},
		{ "727TS", fixup728TS},
		{ "728TS", fixup728TS},

		{ "729TS", fixup728TS},

		{ "737TS", fixup738TS},
		{ "738TS", fixup738TS},


		{"714T", jdbcDifferences7130},
		{"715T", jdbcDifferences7130},
		{"716T", toolboxDifferences7140},
		{"717T", toolboxDifferences7140},
		{"718T", toolboxDifferences7140},
		{"719T", toolboxDifferences7140},

		{"546N", nativeDifferences5440},

		{"614N", nativeDifferences6130},
		{"615N", nativeDifferences6130},
		{"616N", nativeDifferences6140},
		{"617N", nativeDifferences6140},

		{"714N", nativeDifferences7130},
		{"715N", nativeDifferences7130},
		{"716N", nativeDifferences7140},
		{"717N", nativeDifferences7140},
		{"718N", nativeDifferences7140},
		{"719N", nativeDifferences7140},

		{"725N", nativeDifferences7130},
		{"726N", nativeDifferences726N},
		{"727N", nativeDifferences727N},
		{"728N", nativeDifferences727N},
		{"729N", nativeDifferences727N},

		
    {"736N", nativeDifferences737N},
    {"737N", nativeDifferences737N},
    {"738N", nativeDifferences737N},
    {"739N", nativeDifferences737N},


		{"726NX", nativeDifferences726NX},
		{"727NX", nativeDifferences727NX},
		{"728NX", nativeDifferences727NX},
		{"729NX", nativeDifferences727NX},

		{"736NX", nativeDifferences737NX},
		{"737NX", nativeDifferences737NX},
		{"738NX", nativeDifferences737NX},
		{"739NX", nativeDifferences737NX},



		{"714NX", nativeDifferences7130},
		{"715NX", nativeDifferences7130},
		{"716NX", nativeDifferences7140},
		{"717NX", nativeDifferences7140},
		{"718NX", nativeDifferences7140},
		{"719NX", nativeDifferences7140},
		{"614NX", nativeDifferences6130},
		{"615NX", nativeDifferences6130},
		{"616NX", nativeDifferences6140},
		{"617NX", nativeDifferences6140},

		{"546NX", nativeDifferences5440},

		{"726LX", jtopenliteDifferences726L},
	    };



	    String[][] fixupExtended715T = {
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"getColumnDisplaySize","5","11"},
		{"getPrecision","5","10"},
		{"getColumnType","5","4"},
		{"getColumnTypeName","5","INTEGER"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isNullable","7","1"},  /* Updated 4/30/2014 lp01ut18 */ 
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"getPrecision","12","2000"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","13","2000"},
		{"getColumnTypeName","13","VARGRAPHIC"},
		{"getColumnDisplaySize","13","2000"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };

	    String[][] nativeExtendedDifferences714N = {
		{"isSearchable","1","false"},
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"isSearchable","2","false"},
		{"isSearchable","3","false"},
		{"isSearchable","4","false"},
		{"isSearchable","5","false"},
		{"getColumnClassName","5","java.lang.Integer"},
		{"getColumnDisplaySize","5","11"},
		{"getPrecision","5","10"},
		{"getColumnType","5","4"},
		{"getColumnTypeName","5","INTEGER"},
		{"isSearchable","6","false"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isSearchable","7","false"},
		/* Changed 10/14/2013 for lp01ut18 */ 
		{"isNullable","7","1"},
		{"isSearchable","8","false"},
		{"isSearchable","9","false"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"isSearchable","10","false"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"isSearchable","11","false"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"isSearchable","12","false"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"isSearchable","13","false"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isSearchable","14","false"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"isSearchable","15","false"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"isSearchable","16","false"},
		{"isSearchable","17","false"},
		{"isSearchable","18","false"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"isSearchable","19","false"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"isSearchable","20","false"},
		{"isSearchable","21","false"},
		{"isSearchable","22","false"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };




	    String[][] nativeExtendedDifferences715N = {
		{"isSearchable","1","false"},
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"isSearchable","2","false"},
		{"isSearchable","3","false"},
		{"isSearchable","4","false"},
		{"isSearchable","5","false"},
		{"getColumnClassName","5","java.lang.Integer"},

		/* Primed 08/09/2012 */ 
		//{"getPrecision","5","5"},
		//{"getColumnType","5","5"},
		//{"getColumnTypeName","5","SMALLINT"},
		//{"getColumnDisplaySize","5","6"}

		/* Primed 09/06/2012 -- 7153N */ 
		{"getColumnDisplaySize","5","11"},
		{"getPrecision","5","10"},
		{"getColumnType","5","4"},
		{"getColumnTypeName","5","INTEGER"},

		{"isSearchable","6","false"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isSearchable","7","false"},
		{"isNullable","7","1"}, /* chnage 9/3/2014 for lp17ut23 */ 
		{"isSearchable","8","false"},
		{"isSearchable","9","false"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"isSearchable","10","false"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"isSearchable","11","false"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"isSearchable","12","false"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"isSearchable","13","false"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isSearchable","14","false"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"isSearchable","15","false"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"isSearchable","16","false"},
		{"isSearchable","17","false"},
		{"isSearchable","18","false"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"isSearchable","19","false"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"isSearchable","20","false"},
		{"isSearchable","21","false"},
		{"isSearchable","22","false"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };



	    String[][] nativeExtendedDifferences717N = {
		{"isSearchable","1","false"},
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"isSearchable","2","false"},
		{"isSearchable","3","false"},
		{"isSearchable","4","false"},
		{"isSearchable","5","false"},
		{"getColumnClassName","5","java.lang.Integer"},

		/* Primed 08/09/2012 */ 
		{"getPrecision","5","5"},
		{"getColumnType","5","5"},
		{"getColumnTypeName","5","SMALLINT"},
		{"getColumnDisplaySize","5","6"},

		{"isSearchable","6","false"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isSearchable","7","false"},
		/* Changed 11/7/2013 lp01ut18 717N */ 
		{"isNullable","7","1"},
		{"isSearchable","8","false"},
		{"isSearchable","9","false"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"isSearchable","10","false"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"isSearchable","11","false"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"isSearchable","12","false"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnType","12","-9"},
		{"getColumnType","13","-9"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"isSearchable","13","false"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isSearchable","14","false"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"isSearchable","15","false"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"isSearchable","16","false"},
		{"isSearchable","17","false"},
		{"isSearchable","18","false"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"isSearchable","19","false"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"isSearchable","20","false"},
		{"isSearchable","21","false"},
		{"isSearchable","22","false"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };



	    String[][] nativeExtendedDifferences726N = {
		{"isSearchable","1","false"},
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"isSearchable","2","false"},
		{"isSearchable","3","false"},
		{"isSearchable","4","false"},
		{"isSearchable","5","false"},
		{"getColumnClassName","5","java.lang.Integer"},

		/* Primed 08/09/2012 */ 
		{"getPrecision","5","5"},
		{"getColumnType","5","5"},
		{"getColumnTypeName","5","SMALLINT"},
		{"getColumnDisplaySize","5","6"},

		{"isSearchable","6","false"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isSearchable","7","false"},
		{"isNullable","7","1"},
		{"isSearchable","8","false"},
		{"isSearchable","9","false"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"isSearchable","10","false"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"isSearchable","11","false"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"isSearchable","12","false"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnType","12","-9"},
		{"getColumnType","13","-9"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"isSearchable","13","false"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isSearchable","14","false"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"isSearchable","15","false"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"isSearchable","16","false"},
		{"isSearchable","17","false"},
		{"isSearchable","18","false"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"isSearchable","19","false"},
		{"getColumnLabel","19","SCOPE_CATLOG"},
		{"getColumnName","19","SCOPE_CATLOG"},
		{"isSearchable","20","false"},
		{"isSearchable","21","false"},
		{"isSearchable","22","false"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };


	    String[][] nativeExtendedDifferences727N = {
		{"isSearchable","1","false"},
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"isSearchable","2","false"},
		{"isSearchable","3","false"},
		{"isSearchable","4","false"},
		{"isSearchable","5","false"},
		{"getColumnClassName","5","java.lang.Integer"},

		/* Primed 08/09/2012 */ 
		{"getPrecision","5","5"},
		{"getColumnType","5","5"},
		{"getColumnTypeName","5","SMALLINT"},
		{"getColumnDisplaySize","5","6"},

		{"isSearchable","6","false"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isSearchable","7","false"},
		{"isNullable","7","1"},
		{"isSearchable","8","false"},
		{"isSearchable","9","false"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"isSearchable","10","false"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"isSearchable","11","false"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"isSearchable","12","false"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnType","12","-9"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"getColumnType","13","-9"},
		{"isSearchable","13","false"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isSearchable","14","false"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"isSearchable","15","false"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"isSearchable","16","false"},
		{"isSearchable","17","false"},
		{"isSearchable","18","false"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"isSearchable","19","false"},
		{"getColumnLabel","19","SCOPE_CATALOG"},
		{"getColumnName","19","SCOPE_CATALOG"},
		{"isSearchable","20","false"},
		{"isSearchable","21","false"},
		{"isSearchable","22","false"},
		{"getColumnClassName","22","java.lang.Integer"},
	    };


	    String[][] nativeExtendedDifferences737N = {
		{"isSearchable","1","false"},
		{"getColumnDisplaySize","1","128"},
		{"getPrecision","1","128"},
		{"isSearchable","2","false"},
		{"isSearchable","3","false"},
		{"isSearchable","4","false"},
		{"isSearchable","5","false"},
		{"getColumnClassName","5","java.lang.Integer"},

		/* Primed 04/12/2022 */ 
		{"getColumnDisplaySize","5","11"},
		{"getPrecision","5","10"},
		{"getColumnType","5","4"},
		{"getColumnTypeName","5","INTEGER"},

		{"isSearchable","6","false"},
		{"getColumnDisplaySize","6","261"},
		{"getPrecision","6","261"},
		{"isSearchable","7","false"},
		{"isNullable","7","1"},
		{"isSearchable","8","false"},
		{"isSearchable","9","false"},
		{"getColumnDisplaySize","9","6"},
		{"getPrecision","9","5"},
		{"getColumnType","9","5"},
		{"getColumnTypeName","9","SMALLINT"},
		{"isSearchable","10","false"},
		{"getColumnDisplaySize","10","6"},
		{"getPrecision","10","5"},
		{"getColumnType","10","5"},
		{"getColumnTypeName","10","SMALLINT"},
		{"isSearchable","11","false"},
		{"getColumnDisplaySize","11","6"},
		{"getPrecision","11","5"},
		{"getColumnType","11","5"},
		{"getColumnTypeName","11","SMALLINT"},
		{"isSearchable","12","false"},
		{"getColumnDisplaySize","12","2000"},
		{"getPrecision","12","2000"},
		{"getColumnType","12","-9"},
		{"getColumnTypeName","12","NVARCHAR"},
		{"getColumnTypeName","13","NVARCHAR"},
		{"getColumnType","13","-9"},
		{"isSearchable","13","false"},
		{"getColumnDisplaySize","13","2000"},
		{"getPrecision","13","2000"},
		{"isSearchable","14","false"},
		{"isNullable","14","0"},
		{"getColumnDisplaySize","14","6"},
		{"getPrecision","14","5"},
		{"getColumnType","14","5"},
		{"getColumnTypeName","14","SMALLINT"},
		{"isSearchable","15","false"},
		{"getColumnDisplaySize","15","6"},
		{"getPrecision","15","5"},
		{"getColumnType","15","5"},
		{"getColumnTypeName","15","SMALLINT"},
		{"isSearchable","16","false"},
		{"isSearchable","17","false"},
		{"isSearchable","18","false"},
		{"getColumnDisplaySize","18","3"},
		{"getPrecision","18","3"},
		{"isSearchable","19","false"},
		{"getColumnName","19","SCOPE_CATALOG"},
		{"isSearchable","20","false"},
		{"isSearchable","21","false"},
		{"isSearchable","22","false"},
		{"getColumnClassName","22","java.lang.Integer"},
		/* New for April 2019 */
		{"getColumnLabel","1","Table               Cat"},
		{"getColumnLabel","2","Table               Schem"},
		{"getColumnLabel","3","Table               Name"},
		{"getColumnLabel","4","Column              Name"},
		{"getColumnLabel","7","Column              Size"},
		{"getColumnLabel","8","Buffer              Length"},
		{"getColumnLabel","9","Decimal             Digits"},
		{"getColumnLabel","10","Num                 Prec                Radix"},
		{"getColumnLabel","11","Nullable"},
		{"getColumnLabel","12","Remarks"},
		{"getColumnLabel","13","Column              Def"},
		{"getColumnLabel","15","SQL                 Datetime            Sub"},
		{"getColumnLabel","16","Char                Octet               Length"},
		{"getColumnLabel","17","Ordinal             Position"},
		{"getColumnLabel","18","Is                  Nullable"},
		{"getColumnLabel","19","Scope               Catalog"},
		{"getColumnLabel","20","Scope               Schema"},
		{"getColumnLabel","21","Scope               Table"},
		{"getColumnLabel","22","Source              Data                Type"},
	    };



	    Object[][] fixupArrayExtended = {

		{ "714T", toolboxExtendedDifferencesv7130},
		{ "715T", toolboxExtendedDifferencesv7130 },
		/* { "715T", fixupExtended715T}, */ 
		{ "716T", toolboxDifferences7140},
		{ "717T", toolboxDifferences7142},
		{ "718T", toolboxDifferences7142},
		{ "719T", toolboxDifferences7142},
		{ "726T", toolboxExtendedDifferencesv726T},
		{ "727T", toolboxExtendedDifferencesv727T},
		{ "728T", toolboxExtendedDifferencesv727T},
		{ "729T", toolboxExtendedDifferencesv727T},
		{ "736T", toolboxExtendedDifferencesv737T},
		{ "737T", toolboxExtendedDifferencesv737T},
		{ "738T", toolboxExtendedDifferencesv737T},
		{ "739T", toolboxExtendedDifferencesv737T},


		{ "543N", nativeExtendedDifferences},
		{ "544N", nativeExtendedDifferences},
		{ "545N", nativeExtendedDifferences},
		{ "546N", nativeExtendedDifferences5440},
		{ "614N", nativeExtendedDifferences61},
		{ "615N", nativeExtendedDifferences61},
		{ "616N", nativeExtendedDifferences6140},
		{ "617N", nativeExtendedDifferences6140},
		{ "714N", nativeExtendedDifferences714N},
		{ "715N", nativeExtendedDifferences715N},
		{ "716N", nativeExtendedDifferences717N},
		{ "717N", nativeExtendedDifferences717N, "Set 08/09/2012 -- actual results"},
		{ "718N", nativeExtendedDifferences717N, "Set 08/09/2012 -- actual results"},
		{ "719N", nativeExtendedDifferences717N, "Set 08/09/2012 -- actual results"},
		{ "725N", nativeExtendedDifferences7230},
		{ "726N", nativeExtendedDifferences726N},
		{ "727N", nativeExtendedDifferences727N},
		{ "728N", nativeExtendedDifferences727N},
		{ "729N", nativeExtendedDifferences727N},

		{ "736N", nativeExtendedDifferences737N},
		{ "737N", nativeExtendedDifferences737N},
		{ "738N", nativeExtendedDifferences737N},
		{ "739N", nativeExtendedDifferences737N},

		{"616N", nativeDifferences6140},
		{"617N", nativeDifferences6140},

		{"726LX", jtopenliteDifferences726L},
	    };




	    String[][] fixup = null;

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
		    }
		    String extra = "X";
		    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()) extra="S";
		    if (fixup == null) {
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




			String catalog = getExpectedTableCat();

			ResultSet[] rsA =  new ResultSet[TESTSIZE];
			ResultSetMetaData[] rsmdA = new ResultSetMetaData[TESTSIZE];
			for (j = 0; j < TESTSIZE; j++) {
				rsA[j] = dmd.getColumns (null, "SYSIBM", null, null);
				boolean answerAvailable = rsA[j].next();
				if (!answerAvailable) {
				    message.append("ERROR: No data returned from dmd.getColumns (null, \"SYSIBM\", null, null);\n");
				    passed = false;
				}
				rsmdA[j] = rsA[j].getMetaData ();

				ResultSetMetaData rsmd = rsmdA[j];

				passed = verifyRsmd(rsmd, methodTests, catalog, j, message, prime) && passed;
			} /* for j */
			assertCondition(passed, message.toString()+added+prime.toString());

		} catch (Exception e) {
			failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
		} catch (Error e ) {
			failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
		}
	}
    }


    //@128sch
    /**
    getColumns() - specify 128 len schema
    **/
        public void Var046()
        {
            if(getRelease() < JDTestDriver.RELEASE_V7R1M0)
            {
                notApplicable("V7R1 long schema TC.");
                return;
            }
            try {
                message = new StringBuffer();
                messageColumnName="";


                String catalog = getExpectedTableCat();

                ResultSet rs = null;

		message.append("Calling getColumns with "+ JDDMDTest.SCHEMAS_LEN128.toUpperCase()+", 'TAB%', '%'");

                rs = dmd_.getColumns (null, JDDMDTest.SCHEMAS_LEN128.toUpperCase(), "TAB%", "%");

                // Check for some of the columns.
                boolean check1 = false;
                boolean check2 = false;

                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    String column = rs.getString ("COLUMN_NAME");
                    messageColumnName=column;

                    success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;

                    column = rs.getString ("TABLE_SCHEM")
                                    + "." + rs.getString ("TABLE_NAME") + "("
                                    + rs.getString ("COLUMN_NAME") + ")";
		    if (column.equalsIgnoreCase (JDDMDTest.SCHEMAS_LEN128 + ".TABLE1(COL1)")) {
                        check1 = true;
		    } else if (column.equalsIgnoreCase(JDDMDTest.SCHEMAS_LEN128 + ".TABLE1(COL2)")) {
                        check2 = true;
		    } else {
			message.append("\nFound nonmatching entry "+column);
		    }

                }

                rs.close ();
                assertCondition (success && check1 && check2
                        && (rows == 2), "success="+success+" check1="+check1+" check2="+check2+ " rows="+rows+" sb 2 "+message.toString() + " V7R1 Long-Schema TC");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }

        //@128sch
        /**
        getColumns() same as var046 but with non sysibm sp metadata
        **/
            public void Var047()
            {
                if(getRelease() < JDTestDriver.RELEASE_V7R1M0 || getDriver() != JDTestDriver.DRIVER_TOOLBOX)
                {
                    notApplicable("V7R1 long schema (non sysibm SP) TC.");
                    return;
                }
                try {
                    message = new StringBuffer();
                    messageColumnName="";


                    String catalog = getExpectedTableCat();

                    ResultSet rs = null;

                    rs = dmdNoSysibm_.getColumns (null, JDDMDTest.SCHEMAS_LEN128, "TAB%", "%");

                    // Check for some of the columns.
                    boolean check1 = false;
                    boolean check2 = false;

                    boolean success = true;
                    int rows = 0;
                    while (rs.next ()) {
                        ++rows;
                        String column = rs.getString ("COLUMN_NAME");
                        messageColumnName=column;

                        success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;

                        column = rs.getString ("TABLE_SCHEM")
                                        + "." + rs.getString ("TABLE_NAME") + "("
                                        + rs.getString ("COLUMN_NAME") + ")";
                        if (column.equalsIgnoreCase (JDDMDTest.SCHEMAS_LEN128 + ".TABLE1(COL1)"))
                            check1 = true;
                        else if (column.equalsIgnoreCase (JDDMDTest.SCHEMAS_LEN128 + ".TABLE1(COL2)"))
                            check2 = true;

                    }

                    rs.close ();
                    assertCondition (success && check1 && check2
                            && (rows == 2), "success="+success+" check1="+check1+" check2="+check2+ " rows="+rows+" sb 2 "+message.toString() + " V7R1 Long-Schema TC - issue 41580 opened for v7r1 roi");
                } catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }




/**
getColumns() - Get a list of those created in this testcase using a readonly connection
**/
    public void Var048()
    {

	if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V7R1 or later test for readonly connection ");
	    return;
	}
        try {
            message = new StringBuffer();
            messageColumnName="";

            Connection c;

	    if (getDriver() == JDDriverTest.DRIVER_JCC) {
		c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	    } else {
		c = testDriver_.getConnection (baseURL_
					       + ";access=read only;remarks=sql", userId_, encryptedPassword_);
	    }

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getColumns (null, JDDMDTest.COLLECTION,
                                           "COLUMNS", "%");
            boolean success = true;

            String catalog = getExpectedTableCat();


            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String column = rs.getString ("COLUMN_NAME");
                messageColumnName=column;

                success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;
                success =  check(rs.getString ("TABLE_SCHEM"), JDDMDTest.COLLECTION, "TABLE_SCHEM") && success;
                success =  check(rs.getString ("TABLE_NAME"),"COLUMNS", "TABLE_NAME") && success;

           }

            rs.close ();
            c.close ();
            assertCondition ((rows == 4) && success, message.toString()+" -- Read only connection tests added 7/29/2008 does not work V7R1 or earlier" );
        } catch (Exception e) {
            failed (e, "Unexpected Exception -- Read only connection tests added 7/29/2008 does not work V7R1 or earlier ");
        }
    }


/**
getColumns() - Get a list of those created in this testcase using a read call  connection
**/
    public void Var049()
    {
        try {
            message = new StringBuffer();
            messageColumnName="";

            Connection c;

	    if (getDriver() == JDDriverTest.DRIVER_JCC) {
		c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	    } else {
		c = testDriver_.getConnection (baseURL_
					       + ";access=read call;remarks=sql", userId_, encryptedPassword_);
	    }

            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getColumns (null, JDDMDTest.COLLECTION,
                                           "COLUMNS", "%");
            boolean success = true;

            String catalog = getExpectedTableCat();


            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String column = rs.getString ("COLUMN_NAME");
                messageColumnName=column;

                success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;
                success =  check(rs.getString ("TABLE_SCHEM"), JDDMDTest.COLLECTION, "TABLE_SCHEM") && success;
                success =  check(rs.getString ("TABLE_NAME"),"COLUMNS", "TABLE_NAME") && success;

           }

            rs.close ();
            c.close ();
            assertCondition ((rows == 4) && success, message.toString()+" -- Read call connection tests added 7/29/2008" );
        } catch (Exception e) {
            failed (e, "Unexpected Exception -- Read only connection tests added 7/29/2008");
        }
    }

/**
getColumns() - Run getColumns multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var050()
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
		    // System.out.println("Calling getColumns");
		    ResultSet rs = dmd_.getColumns (null, JDDMDTest.COLLECTION,
						    "COLUMNS", "%");
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
     * Test the metatdata for TIMESTAMP(0..12) columns
     *  Should be the same as native
     */
    public void Var051()
    {
	if (checkTimestamp12Support()) {
	    String added = " -- Added 4/24/2013  ";


	    String toolboxExpected42[][] = {

	        {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
	        {"", systemName, JDDMDTest.COLLECTION, "TS12COLUMNS", "TS0",        "93",       "TIMESTAMP",               "19",          "16",             "0",              null,              "1",             null,           null,         "9",             "3",               null,              "1",                "YES",          null,       null,        null,      null, "NO"},

	        {"", systemName, JDDMDTest.COLLECTION, "TS12COLUMNS", "TS1",        "93",       "TIMESTAMP",                   "21",          "16",             "1",              null,              "1",             null,           null,         "9",             "3",               null,               "2",                "YES",          null,       null,        null,      null, "NO"  },

	        {"", systemName, JDDMDTest.COLLECTION, "TS12COLUMNS",  "TS2",       "93",        "TIMESTAMP",             "22",         "16",              "2",              null ,             "1",              null,           null,        "9",              "3",               null,               "3",                 "YES",  null,  null,  null,  null,  "NO"}, 
	        {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS3",  "93",  "TIMESTAMP",  "23",  "16",  "3",  null,  "1",  null,  null,  "9",  "3",  null,  "4",  "YES",  null,  null,  null,  null,  "NO"},  
	        {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS4",  "93",  "TIMESTAMP",  "24",  "16",  "4",  null,  "1",  null,  null,  "9",  "3",  null,  "5",  "YES",  null,  null,  null,  null,  "NO"},  
	        {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS5",  "93",  "TIMESTAMP",  "25",  "16",  "5",  null,  "1",  null,  null,  "9",  "3",  null,  "6",  "YES",  null,  null,  null,  null,  "NO"},  
	        {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS6",  "93",  "TIMESTAMP",  "26",  "16",  "6",  null,  "1",  null,  null,  "9",  "3",  null,  "7",  "YES",  null,  null,  null,  null,  "NO"},  
	        {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS7",  "93",  "TIMESTAMP",  "27",  "16",  "7",  null,  "1",  null,  null,  "9",  "3",  null,  "8",  "YES",  null,  null,  null,  null,  "NO"},  
	        {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS8",  "93",  "TIMESTAMP",  "28",  "16",  "8",  null,  "1",  null,  null,  "9",  "3",  null,  "9",  "YES",  null,  null,  null,  null,  "NO"},  
	        {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS9",  "93",  "TIMESTAMP",  "29",  "16",  "9",  null,  "1",  null,  null,  "9",  "3",  null,  "10",  "YES",  null,  null,  null,  null,  "NO"},  
	        {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS10",  "93",  "TIMESTAMP",  "30",  "16",  "10",  null,  "1",  null,  null,  "9",  "3",  null,  "11",  "YES",  null,  null,  null,  null,  "NO"},  
	        {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS11",  "93",  "TIMESTAMP",  "31",  "16",  "11",  null,  "1",  null,  null,  "9",  "3",  null,  "12",  "YES",  null,  null,  null,  null,  "NO"},  
	        {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS12",  "93",  "TIMESTAMP",  "32",  "16",  "12",  null,  "1",  null,  null,  "9",  "3",  null,  "13",  "YES",  null,  null,  null,  null,  "NO"},  


	    };  //@K1A


      String toolboxJdk41Expected42[][] = {

    {"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",             "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE"},
    {"", systemName, JDDMDTest.COLLECTION, "TS12COLUMNS", "TS0",        "93",       "TIMESTAMP",               "19",          "16",             "0",              null,              "1",             null,           null,         "9",             "3",               null,              "1",                "YES",          null,       null,        null,      null, "NO", "NO"},

    {"", systemName, JDDMDTest.COLLECTION, "TS12COLUMNS", "TS1",        "93",       "TIMESTAMP",                   "21",          "16",             "1",              null,              "1",             null,           null,         "9",             "3",               null,               "2",                "YES",          null,       null,        null,      null, "NO" , "NO" },

    {"", systemName, JDDMDTest.COLLECTION, "TS12COLUMNS",  "TS2",       "93",        "TIMESTAMP",             "22",         "16",              "2",              null ,             "1",              null,           null,        "9",              "3",               null,               "3",                 "YES",  null,  null,  null,  null,  "NO", "NO"}, 
    {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS3",  "93",  "TIMESTAMP",  "23",  "16",  "3",  null,  "1",  null,  null,  "9",  "3",  null,  "4",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS4",  "93",  "TIMESTAMP",  "24",  "16",  "4",  null,  "1",  null,  null,  "9",  "3",  null,  "5",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS5",  "93",  "TIMESTAMP",  "25",  "16",  "5",  null,  "1",  null,  null,  "9",  "3",  null,  "6",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS6",  "93",  "TIMESTAMP",  "26",  "16",  "6",  null,  "1",  null,  null,  "9",  "3",  null,  "7",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS7",  "93",  "TIMESTAMP",  "27",  "16",  "7",  null,  "1",  null,  null,  "9",  "3",  null,  "8",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS8",  "93",  "TIMESTAMP",  "28",  "16",  "8",  null,  "1",  null,  null,  "9",  "3",  null,  "9",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS9",  "93",  "TIMESTAMP",  "29",  "16",  "9",  null,  "1",  null,  null,  "9",  "3",  null,  "10",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS10",  "93",  "TIMESTAMP",  "30",  "16",  "10",  null,  "1",  null,  null,  "9",  "3",  null,  "11",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS11",  "93",  "TIMESTAMP",  "31",  "16",  "11",  null,  "1",  null,  null,  "9",  "3",  null,  "12",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "TS12COLUMNS",  "TS12",  "93",  "TIMESTAMP",  "32",  "16",  "12",  null,  "1",  null,  null,  "9",  "3",  null,  "13",  "YES",  null,  null,  null,  null,  "NO", "NO"},  


      };  //@K1A



	    String[][] expected42 = toolboxExpected42;
	    if (getJdbcMajorMinorLevel() >= 41) { 
	      expected42 = toolboxJdk41Expected42; 
	    }
	    try {
	      
	      Statement stmt = connection_.createStatement(); 
	      String sql =  "DROP TABLE " + JDDMDTest.COLLECTION + ".TS12COLUMNS";
	      try {
	    stmt.executeUpdate(sql); 
	      } catch (Exception e) {
	      } 
	      sql = "CREATE TABLE "
	        + JDDMDTest.COLLECTION
	        + ".TS12COLUMNS (TS0 TIMESTAMP(0)," +
	                       " TS1 TIMESTAMP(1)," +
	                       " TS2 TIMESTAMP(2)," +
	                       " TS3 TIMESTAMP(3)," +
	                       " TS4 TIMESTAMP(4)," +
	                       " TS5 TIMESTAMP(5)," +
	                       " TS6 TIMESTAMP(6)," +
	                       " TS7 TIMESTAMP(7)," +
	                       " TS8 TIMESTAMP(8)," +
	                       " TS9 TIMESTAMP(9)," +
	                       " TS10 TIMESTAMP(10)," +
	                       " TS11 TIMESTAMP(11)," +
	                       " TS12 TIMESTAMP(12))"; 
	      stmt.executeUpdate(sql);
	      
	      
	      
	      
		String catalog =  getExpectedTableCat();


		ResultSet rs = dmd_.getColumns (catalog,
						JDDMDTest.SCHEMAS_PERCENT, "TS12COLUMNS", "%");

		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		String [] oneRow = new String[count+1];

		boolean success = true;
		int rows = 0;
		while (rs.next ()) {
		    ++rows;

		//
		// Get the row contents
		//
		    for(int i=1; i <= count; i++)
		    {
			oneRow[i]=rs.getString(i);
		    }
		//
		// Check for a matching row
		//
		    boolean thisOk =  true;
		    thisOk = checkExpected(oneRow, expected42);
		    if (!thisOk) {
			message.append("Line did not verify. Column count is "+count+"\n");
			for (int i=1; i <= count; i++) {
			    message.append("\"" + oneRow[i] + "\",  ");
			}
			message.append("\n");
			success=false;
		    //System.out.println(sb.toString());
		    }
		}

		rs.close ();


		sql =  "DROP TABLE " + JDDMDTest.COLLECTION + ".TS12COLUMNS";
		stmt.executeUpdate(sql); 
		stmt.close();

		assertCondition (success
				 && (rows == 13), "Rows = "+rows+" sb 13 Check column information for TIMESTAMP(0..12) "+added+"  "+message.toString());
	    } catch (Exception e) {
		failed (e, "Unexpected Exception "+added );
	    }
	}
    }

    /**
     * Test the metatdata for BOOLEAN columns
     *  Should be the same as native
     */
    public void Var052()
    {
	if (checkBooleanSupport()) {
	    String added = " -- Added 4/16/2021  ";


      String toolboxJdk41Expected42[][] = {

    {"", "TABLE_CAT","SCHEMA", "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT","IS_GENERATEDCOLUMN"},
    {"", systemName, JDDMDTest.COLLECTION, "BOOLCOLUMNS", "BOOL0",    "16",   "BOOLEAN", "1", "1",   null,  null,  "1",  null, null,  "16",  null,  null, "1",   "YES",  null, null,  null,    null, "NO", "NO"},
    {"", systemName, JDDMDTest.COLLECTION, "BOOLCOLUMNS", "BOOL1",    "16",   "BOOLEAN", "1", "1",  null,  null,  "1",  null,  null, "16",   null,  null, "2",   "YES",  null,  null,  null,  null,"NO", "NO" },
    {"", systemName, JDDMDTest.COLLECTION, "BOOLCOLUMNS",  "BOOL2",   "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "3",  "YES",  null,  null,  null,  null,  "NO", "NO"}, 
    {"", systemName, JDDMDTest.COLLECTION,  "BOOLCOLUMNS",  "BOOL3",  "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "4",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "BOOLCOLUMNS",  "BOOL4",  "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "5",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "BOOLCOLUMNS",  "BOOL5",  "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "6",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "BOOLCOLUMNS",  "BOOL6",  "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "7",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "BOOLCOLUMNS",  "BOOL7",  "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "8",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "BOOLCOLUMNS",  "BOOL8",  "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "9",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "BOOLCOLUMNS",  "BOOL9",  "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "10",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "BOOLCOLUMNS",  "BOOL10", "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "11",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "BOOLCOLUMNS",  "BOOL11", "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "12",  "YES",  null,  null,  null,  null,  "NO", "NO"},  
    {"", systemName, JDDMDTest.COLLECTION,  "BOOLCOLUMNS",  "BOOL12", "16",  "BOOLEAN",  "1", "1",  null,  null,  "1",  null,  null,  "16",  null,  null,  "13",  "YES",  null,  null,  null,  null,  "NO", "NO"},  


      };  //@K1A



	    String[][] expected42 =  toolboxJdk41Expected42; 

	    try {
	      
	      Statement stmt = connection_.createStatement(); 
	      String sql =  "DROP TABLE " + JDDMDTest.COLLECTION + ".BOOLCOLUMNS";
	      try {
	    stmt.executeUpdate(sql); 
	      } catch (Exception e) {
	      } 
	      sql = "CREATE TABLE "
	        + JDDMDTest.COLLECTION
	        + ".BOOLCOLUMNS (BOOL0 BOOLEAN," +
	                       " BOOL1 BOOLEAN," +
	                       " BOOL2 BOOLEAN," +
	                       " BOOL3 BOOLEAN," +
	                       " BOOL4 BOOLEAN," +
	                       " BOOL5 BOOLEAN," +
	                       " BOOL6 BOOLEAN," +
	                       " BOOL7 BOOLEAN," +
	                       " BOOL8 BOOLEAN," +
	                       " BOOL9 BOOLEAN," +
	                       " BOOL10 BOOLEAN," +
	                       " BOOL11 BOOLEAN," +
	                       " BOOL12 BOOLEAN)"; 
	      stmt.executeUpdate(sql);
	      
	      
	      
	      
		String catalog =  getExpectedTableCat();


		ResultSet rs = dmd_.getColumns (catalog,
						JDDMDTest.SCHEMAS_PERCENT, "BOOLCOLUMNS", "%");

		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		String [] oneRow = new String[count+1];

		boolean success = true;
		int rows = 0;
		while (rs.next ()) {
		    ++rows;

		//
		// Get the row contents
		//
		    for(int i=1; i <= count; i++)
		    {
			oneRow[i]=rs.getString(i);
		    }
		//
		// Check for a matching row
		//
		    boolean thisOk =  true;
		    thisOk = checkExpected(oneRow, expected42);
		    if (!thisOk) {
			message.append("Line did not verify. Column count is "+count+"\n");
			for (int i=1; i <= count; i++) {
			    message.append("\"" + oneRow[i] + "\",  ");
			}
			message.append("\n");
			success=false;
		    //System.out.println(sb.toString());
		    }
		}

		rs.close ();


		sql =  "DROP TABLE " + JDDMDTest.COLLECTION + ".BOOLCOLUMNS";
		stmt.executeUpdate(sql); 
		stmt.close();

		assertCondition (success
				 && (rows == 13), "Rows = "+rows+" sb 13 Check column information for BOOLEAN "+added+"  "+message.toString());
	    } catch (Exception e) {
		failed (e, "Unexpected Exception "+added );
	    }
	}
    }





}// END MAIN
