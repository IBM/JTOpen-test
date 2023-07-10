///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetIndexInfo.java
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
 // File Name:    JDDMDGetIndexInfo.java
 //
 // Classes:      JDDMDGetIndexInfo
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 // LUW differences
 // 1. Only 1 index returned (Var004, ..
 //
 // SYSIBM differences
 // 1. Empty catalog returns answers (Var005)
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDDMDGetIndexInfo.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getIndexInfo()
</ul>
**/
public class JDDMDGetIndexInfo
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private String              connectionCatalog_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private StringBuffer message=new StringBuffer();
    private String messageColumnName="";
    private boolean is400 = true;


/**
Constructor.
**/
    public JDDMDGetIndexInfo (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetIndexInfo",
            namesAndVars, runMode, fileOutputStream,
            password);
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
    protected void setup ()
        throws Exception
    {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          connection_ = testDriver_.getConnection (baseURL_,
              userId_, encryptedPassword_);

        } else {
        connection_ = testDriver_.getConnection (baseURL_
            + ";libraries=" + JDDMDTest.COLLECTION + " "
            + JDDMDTest.COLLECTION2 + " " + JDDMDTest.COLLECTIONXX,
            userId_, encryptedPassword_);
        }
        dmd_ = connection_.getMetaData ();
        connectionCatalog_ = connection_.getCatalog();
        if (connectionCatalog_ == null) {
	    if (JDTestDriver.isLUW()) {
		// Leave as null
	    } else {
		connectionCatalog_ = getCatalogFromURL(baseURL_);
		System.out.println("Warning:  connection.getCatalog() returned null setting to "+connectionCatalog_);
	    }
        }

        Statement s = connection_.createStatement ();

	//
	// Determine if destination is a 400
	//
        // Determine if LUW
	try {
	    s.executeQuery("select * from qsys2.qsqptabl");
	    is400 =true;
	} catch (Exception e) {
	    is400 =false;
	}




	//
	// Cleanup old stuff (if needed)
	//
	try { s.executeUpdate ("DROP INDEX "+JDDMDTest.COLLECTION2+".INDEXXX"); } catch (Exception e) {}
        try { s.executeUpdate ("DROP INDEX "+JDDMDTest.COLLECTION2+".INDEX");} catch (Exception e) {}
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
        {
            try { s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2+ ".INDEXTABLEXX");} catch (Exception e) {}
        }
        else
        {
            try { s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2+ ".INDEXTABXX");} catch (Exception e) {}
        }

        try { s.executeUpdate ("DROP INDEX " + JDDMDTest.COLLECTION+ ".INDEX2");} catch (Exception e) {}
        try { s.executeUpdate ("DROP INDEX " + JDDMDTest.COLLECTION+ ".INDEX1");} catch (Exception e) {}
        try { s.executeUpdate ("DROP INDEX " + JDDMDTest.COLLECTION+ ".INDEX");} catch (Exception e) {}
        try { s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION+ ".INDEXTABLE2");} catch (Exception e) {}
        try { s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION+ ".INDEXTABLE");} catch (Exception e) {}
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0){
            try { s.executeUpdate ("DROP INDEX " + JDDMDTest.COLLECTION+".LCNINDEX");} catch (Exception e) {}
            try { s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION+".LCNINDEXTABLE");} catch (Exception e) {}
        }




	//
	// Create new tables.
	//
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".INDEXTABLE (COLUMN INT)");

	if (JDTestDriver.isLUW()) {
	    //
	    // Need to disallow reverse scans so both indexes
	    // will be created.
	    s.executeUpdate ("CREATE INDEX " + JDDMDTest.COLLECTION
			     + ".INDEX ON " + JDDMDTest.COLLECTION
			     + ".INDEXTABLE (COLUMN DESC) DISALLOW REVERSE SCANS");
	    s.executeUpdate ("CREATE INDEX " + JDDMDTest.COLLECTION
			     + ".INDEX1 ON " + JDDMDTest.COLLECTION
			     + ".INDEXTABLE (COLUMN ASC) DISALLOW REVERSE SCANS" );


	} else {
	    s.executeUpdate ("CREATE INDEX " + JDDMDTest.COLLECTION
			     + ".INDEX ON " + JDDMDTest.COLLECTION
			     + ".INDEXTABLE (COLUMN DESC)");
	    s.executeUpdate ("CREATE INDEX " + JDDMDTest.COLLECTION
			     + ".INDEX1 ON " + JDDMDTest.COLLECTION
			     + ".INDEXTABLE (COLUMN ASC)");
	}
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".INDEXTABLE2 (COLUMN INT)");
        s.executeUpdate ("CREATE UNIQUE INDEX " + JDDMDTest.COLLECTION
            + ".INDEX2 ON " + JDDMDTest.COLLECTION
            + ".INDEXTABLE2 (COLUMN DESC)");

        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0 && is400 )
        {
            s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION      //@C1A
                + ".LCNINDEXTABLE (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INT)");
            s.executeUpdate ("CREATE INDEX " + JDDMDTest.COLLECTION      //@C1A
                + ".LCNINDEX ON " + JDDMDTest.COLLECTION
                + ".LCNINDEXTABLE (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST DESC)");
        }

        // SQL400 - SQLStatistics is a funtion for the native driver that
        //          doesn't support long names for the table and collection.
        //          This is a CLI implementation issue.  Here, we give the
        //          table a different shorted name than the Toolbox JDBC
        //          driver uses.
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
        {
            s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                + ".INDEXTABLEXX (COLUMN INT)");
            s.executeUpdate ("CREATE INDEX " + JDDMDTest.COLLECTION2
                + ".INDEX ON " + JDDMDTest.COLLECTION2
                + ".INDEXTABLEXX (COLUMN ASC)");
            s.executeUpdate ("CREATE UNIQUE INDEX " + JDDMDTest.COLLECTION2
                + ".INDEXXX ON " + JDDMDTest.COLLECTION2
                + ".INDEXTABLEXX (COLUMN ASC)");
        }
        else
        {
            s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                + ".INDEXTABXX (COLUMN INT)");
            s.executeUpdate ("CREATE INDEX " + JDDMDTest.COLLECTION2
                + ".INDEX ON " + JDDMDTest.COLLECTION2
                + ".INDEXTABXX (COLUMN ASC)");
            s.executeUpdate ("CREATE UNIQUE INDEX " + JDDMDTest.COLLECTION2
                + ".INDEXXX ON " + JDDMDTest.COLLECTION2
                + ".INDEXTABXX (COLUMN ASC)");
        }
        s.close ();
        try {
        connection_.commit(); // for xa
        } catch (SQLException e) {
          // DB2 V9 throws exception .. ignore
        }

        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
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

        s.executeUpdate ("DROP INDEX " + JDDMDTest.COLLECTION2
            + ".INDEXXX");
        s.executeUpdate ("DROP INDEX " + JDDMDTest.COLLECTION2
            + ".INDEX");

        // SQL400 - Table name length issue (see above)
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
        {
            s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
                + ".INDEXTABLEXX");
        }
        else
        {
            s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
                + ".INDEXTABXX");
        }

        s.executeUpdate ("DROP INDEX " + JDDMDTest.COLLECTION
            + ".INDEX2");
	try {
	    s.executeUpdate ("DROP INDEX " + JDDMDTest.COLLECTION
			     + ".INDEX1");
	} catch (Exception e) {
	    e.printStackTrace();
	}
        s.executeUpdate ("DROP INDEX " + JDDMDTest.COLLECTION
            + ".INDEX");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".INDEXTABLE2");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".INDEXTABLE");
        //@C1A
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0 && is400){
            s.executeUpdate ("DROP INDEX " + JDDMDTest.COLLECTION   //C1A
                + ".LCNINDEX");
            s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION   //@C1A
                + ".LCNINDEXTABLE");
        }


        s.close ();
	try {
	    connection_.commit(); // for xa
	} catch (Exception e) {
	}
        connection_.close ();
    }


    public boolean checkString(String label, String actual, String expected) {
	if (actual == null) {
	    if (expected == null) {
		return true;
	    } else {
		message.append("Column:"+messageColumnName+"For "+label+" actual=("+actual+") sb("+expected+")\n");
		return false;
	    }
	} else {
	    if ( actual.equals(expected)) {
		return true;
	    } else {
		message.append("Column:"+messageColumnName+"For "+label+" actual=("+actual+") sb("+expected+")\n");
		return false;
	    }
	}
    }

    public boolean checkBoolean(String label, boolean actual, boolean expected) {
	if ( actual == expected) {
	    return true;
	} else {
	    message.append("Column:"+messageColumnName+"For "+label+" actual=("+actual+") sb("+expected+")\n");
	    return false;
	}
    }

    public boolean checkInt(String label, int actual, int expected) {
	if ( actual == expected) {
	    return true;
	} else {
	    message.append("Column:"+messageColumnName+"For "+label+" actual=("+actual+") sb("+expected+")\n");
	    return false;
	}
    }


/**
getIndexInfo() - Check the result set format.

Note:  In V6R1 the SYSIBM procedures changed the datatype by columns COLUMN_NAME, CARDINALITY, PAGES, and FILTER_CONDITION.  See issu 36275 for more details.
Note:  In December 2008 for V6R1, the SYSIBM procedure changed the datatype for column 13 from 2005 (CLOB) to 12 (VARCHAR).
**/
    public void Var001()
    {

      message.setLength(0);
        try {

            ResultSet rs = dmd_.getIndexInfo (null, null, "INDEXTABLE", true, true);
            ResultSetMetaData rsmd = rs.getMetaData ();
	    String info = ""; 
            String[] expectedNames = { "TABLE_CAT", "TABLE_SCHEM",
                                       "TABLE_NAME", "NON_UNIQUE",
                                       "INDEX_QUALIFIER", "INDEX_NAME",
                                       "TYPE", "ORDINAL_POSITION",
                                       "COLUMN_NAME", "ASC_OR_DESC",
                                       "CARDINALITY", "PAGES",
                                       "FILTER_CONDITION" };
            int[] expectedTypes = { Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.SMALLINT,
                                    Types.VARCHAR, Types.VARCHAR,
                                    Types.SMALLINT, Types.SMALLINT,
                                    Types.VARCHAR, Types.VARCHAR,
                                    Types.INTEGER, Types.INTEGER,
                                    Types.VARCHAR };

            int[] expectedTypes16 = { Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.SMALLINT,
                Types.VARCHAR, Types.VARCHAR,
                Types.SMALLINT, Types.SMALLINT,
                Types.VARCHAR, Types.VARCHAR,
                Types.INTEGER, Types.INTEGER,
                Types.VARCHAR };

            int[] expectedTypesV6R1 = { Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.SMALLINT,
                    Types.VARCHAR, Types.VARCHAR,
                    Types.SMALLINT, Types.SMALLINT,
                    Types.CLOB, Types.CHAR,
                    Types.BIGINT, Types.BIGINT,
                    Types.VARCHAR };   /* changed Dec 2008 */

            int[] expectedTypes40V6R1 = { Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.SMALLINT,
                    Types.VARCHAR, Types.VARCHAR,
                    Types.SMALLINT, Types.SMALLINT,
                    2011, Types.CHAR,
                    Types.BIGINT, Types.BIGINT,
                    -9 };   /* changed Dec 2008 */


          /*  int[] expectedTypesV6R1 = { Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.SMALLINT,
                    Types.VARCHAR, Types.VARCHAR,
                    Types.SMALLINT, Types.SMALLINT,
                    Types.CLOB, Types.CLOB,
                    Types.CHAR, Types.CLOB,
                    Types.CLOB };*/

            if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX ||
                    getDriver() == JDTestDriver.DRIVER_NATIVE)
                    &&
                    (getRelease() >= JDTestDriver.RELEASE_V5R5M0)) {
		if (getJdbcLevel() >= 4) {
		    expectedTypes = expectedTypes40V6R1;
		    info = "using expectedTypes40V6R1";
		} else { 
		    expectedTypes = expectedTypesV6R1;
		    info = "using expectedTypesV6R1";
		}
            }
            else if ((getDriver() == JDTestDriver.DRIVER_JCC) || (getJdbcLevel() >= 4 )
                    ||
                    (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() >= JDTestDriver.RELEASE_V5R5M0)) {
                expectedTypes = expectedTypes16;
		info = "using expectedTypes16"; 
            }

            if ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
                    (getRelease() == JDTestDriver.RELEASE_V5R4M0) &&
                    (getJdbcLevel() == 4)) {
                /* column 10 = 1 */
                /* Changed March 2009 */
                expectedTypes[9] = 1;
		info += " change1"; 

            }
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
                    getRelease() >= JDTestDriver.RELEASE_V7R1M0)
            {
                expectedTypes[12] = 12; // don't use long varchar
		if (getJdbcLevel() >= 4) {
		    expectedTypes[12] = -9; // use NVARCHAR
		    expectedTypes[8] = 2011; // Use NCLOB if JDBC 40 and later
		    info += " change2"; 
		} else {
		    expectedTypes[8] = 2005; // Use NCLOB if JDBC 40 and later
		    info += " change3"; 
		} 

            }else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
                   ( getRelease() == JDTestDriver.RELEASE_V6R1M0 || getRelease() == JDTestDriver.RELEASE_V5R5M0 ) )
            {
                //expectedTypes[12] = 2005; //sysibm returns clob
                //this week it is back to -1
                expectedTypes[12] = -1;
		info += " change4"; 
            }

            int count = rsmd.getColumnCount ();
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);
            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);


            rs.close ();
            assertCondition ((count == 13) && (namesCheck) && (typesCheck),
                           "count="+count+" sb 13 "+message+" jdbcLevel is "+getJdbcLevel()+" info="+info);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Get a list of those created in this testcase and
verify all columns.

SQL400 - The native driver returns the cardinality and the pages as 0
         and the toolbox driver is returning them as -1.  I am note sure
         what is right but it didn't seem to be worth changing the driver
         code when I could change the testcase code.
**/


    public void Var002()
    {
        try {
	    int expectedRows = 2;
	    if (isSysibmMetadata()) {
		    expectedRows = 3;
	    } else if((getDriver () == JDTestDriver.DRIVER_TOOLBOX) && (getRelease() >= JDTestDriver.RELEASE_V5R5M0))
	    	expectedRows = 3;

      message.setLength(0);
            ResultSet rs = dmd_.getIndexInfo (null, JDDMDTest.COLLECTION,
                "INDEXTABLE", false, true);
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName       = rs.getString ("COLUMN_NAME");
                messageColumnName = columnName;
                success = checkString("TABLE_CAT", rs.getString ("TABLE_CAT"), connectionCatalog_) && success;
                success = checkString("TABLE_SCHEM", rs.getString ("TABLE_SCHEM"), JDDMDTest.COLLECTION) && success;
                success = checkString("TABLE_NAME", rs.getString("TABLE_NAME"), "INDEXTABLE") && success;

                boolean nonUnique       = rs.getBoolean ("NON_UNIQUE");
                String indexQualifier   = rs.getString ("INDEX_QUALIFIER");
                String indexName        = rs.getString ("INDEX_NAME");
                short type              = rs.getShort ("TYPE");
                short ordinalPosition   = rs.getShort ("ORDINAL_POSITION");
                String ascOrDesc        = rs.getString ("ASC_OR_DESC");
                int cardinality         = rs.getInt ("CARDINALITY");
                int pages               = rs.getInt ("PAGES");
                String filterCondition  = rs.getString ("FILTER_CONDITION");

		if (columnName==null) {
		    success = checkBoolean("NON_UNIQUE", nonUnique, false) && success;
		    success = checkInt("TYPE", type, DatabaseMetaData.tableIndexStatistic) && success;
		    success = checkInt("ORDINAL_POSITION", ordinalPosition, 0) && success;

		} else {

		    success = checkBoolean("NON_UNIQUE", nonUnique, true) && success;
		    success = checkInt("TYPE", type, DatabaseMetaData.tableIndexOther) && success;
		    success = checkInt("ORDINAL_POSITION", ordinalPosition, 1) && success;

		}


                if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
                {
                    if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
                    {
                        success = success && (cardinality == -1);
                        success = success && (pages == -1);
                    }
                    else
                    {
                    	  success = checkInt("CARDINALITY", cardinality, 0) && success;
                    	  if(type == DatabaseMetaData.tableIndexStatistic)
                              success = checkInt("PAGES", pages, 1) && success; //index pages
                    	  else
                    		  success = checkInt("PAGES", pages, 0) && success; //table pages
                    }
                }
                else
                {
                    success = checkInt("CARDINALITY", cardinality, 0) && success;
		    if(type == DatabaseMetaData.tableIndexStatistic && getRelease() >= JDTestDriver.RELEASE_V5R5M0)
			success = checkInt("PAGES", pages, 1) && success; //index pages
		    else
			success = checkInt("PAGES", pages, 0) && success;
                }

                success = checkString("FILTER_CONDITION", filterCondition,null ) && success;

                if ("INDEX".equals( indexName)) {
                    success = checkString("INDEX_QUALIFIER", indexQualifier, JDDMDTest.COLLECTION) && success;
                    success = checkString("ASC_OR_DESC", ascOrDesc, "D") && success;
                }
                else if ("INDEX1".equals(indexName)) {
                    success = checkString("INDEX_QUALIFIER", indexQualifier, JDDMDTest.COLLECTION) && success;
                    success = checkString("ASC_OR_DESC", ascOrDesc, "A") && success;

                }
		else if (indexName == null) {
		    success = checkString("INDEX_QUALIFIER", indexQualifier, null) && success;
		    success = checkString("ASC_OR_DESC", ascOrDesc, null) && success;
		}
                else {
		    message.append("UNRECOGNIZED INDEX FOUND "+indexName+"\n");
                    success = false;  // just tightening down the testcase a little.
                }
            }
	    if (rows != expectedRows) {
		success=false;
		message.append("Number of rows is "+rows+" instead of "+expectedRows);
	    }
            rs.close ();
            assertCondition (success, message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify all null parameters.  Verify no rows are returned.
**/
    public void Var003()
    {
        try {
            ResultSet rs;
            rs = dmd_.getIndexInfo (null, null, "", false, false);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0, "all null parameters -- no rows should be returned rows="+rows);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify null for the catalog.  All matching
indices should be returned.
**/
    public void Var004()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (null,
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);

            boolean check1 = false;
            boolean check2 = false;

            while (rs.next ()) {
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                if (index.equals (JDDMDTest.COLLECTION + ".INDEX1"))
                    check1 = true;
                else if (index.equals (JDDMDTest.COLLECTION + ".INDEX"))
                    check2 = true;
		else {
		    System.out.println("Warning:  found "+index);
		}
            }

            rs.close ();
            assertCondition (check1 && check2, "both indexes not found check1="+check1+" check2="+check2);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify empty string for the catalog.
No matching columns should be returned.
**/
    public void Var005()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo ("",
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if ( isSysibmMetadata()) {
		assertCondition (rows > 0, "rows="+rows+" sb > 0 when empty string specified for catalog and LUW or JDBC4.0 or NATIVE & V5R5");
	    } else if ( (getDriver () == JDTestDriver.DRIVER_TOOLBOX) && (getRelease() >= JDTestDriver.RELEASE_V5R5M0))
	    {
	    	assertCondition (rows > 0, "rows="+rows+" sb > 0 when empty string specified for catalog and LUW or JDBC4.0 or NATIVE & V5R5");
	    }
	    else {
		assertCondition (rows == 0, "rows="+rows+" sb 0 since empty string specified for catalog");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify a catalog that matches the catalog
exactly.  All matching rows should be returned.
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);

            boolean check1 = false;
            boolean check2 = false;

            while (rs.next ()) {
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                if (index.equals (JDDMDTest.COLLECTION + ".INDEX1"))
                    check1 = true;
                else if (index.equals (JDDMDTest.COLLECTION + ".INDEX"))
                    check2 = true;
		else {
		    System.out.println("Warning:  found "+index);
		}
            }

            rs.close ();
            assertCondition (check1 && check2,"both indexes not found check1="+check1+" check2="+check2);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify "localhost" for the catalog.
All matching rows should be returned.
**/
    public void Var007()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4 ||
          (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() >= JDTestDriver.RELEASE_V5R5M0) ||
          (getDriver() == JDTestDriver.DRIVER_TOOLBOX && getRelease() >= JDTestDriver.RELEASE_V5R5M0)) {
        notApplicable("\"localhost\" variation ");
      } else {

        try {
            ResultSet rs = dmd_.getIndexInfo ("localhost",
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);

            boolean check1 = false;
            boolean check2 = false;
            while (rs.next ()) {
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                if (index.equals (JDDMDTest.COLLECTION + ".INDEX1"))
                    check1 = true;
                else if (index.equals (JDDMDTest.COLLECTION + ".INDEX"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (check1 && check2,"both indexes not found check1="+check1+" check2="+check2);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
getIndexInfo() - Specify a catalog pattern for which there is a
match.  No matching rows should be returned, since we do not
support catalog pattern.
**/
    public void Var008()
    {
        try {
            ResultSet rs;
	    if (JDTestDriver.isLUW()) {
	      rs = dmd_.getIndexInfo ("_%",
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);
	    } else {
	      rs = dmd_.getIndexInfo (connectionCatalog_.substring (0, 4) + "%",
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);
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
getIndexInfo() - Specify a catalog for which there is no match.
No matching columns should be returned.
**/
    public void Var009()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo ("BOGUS",
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);

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
getIndexInfo() - Specify null for the schema.
All matching rows should be returned.
**/
    public void Var010()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                null, "INDEXTABLE", false, false);

            boolean check1 = false;
            boolean check2 = false;

            while (rs.next ()) {
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                if (index.equals (JDDMDTest.COLLECTION + ".INDEX1"))
                    check1 = true;
                else if (index.equals (JDDMDTest.COLLECTION + ".INDEX"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (check1 && check2,"both indexes not found check1="+check1+" check2="+check2);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify empty string for the schema.
No matching columns should be returned.
**/
    public void Var011()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                "", "INDEXTABLE", false, false);

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
getIndexInfo() - Specify a schema that matches the schema
exactly.  All matching columns should be returned.
**/
    public void Var012()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);

            boolean check1 = false;
            boolean check2 = false;

            while (rs.next ()) {
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                if (index.equals (JDDMDTest.COLLECTION + ".INDEX1"))
                    check1 = true;
                else if (index.equals (JDDMDTest.COLLECTION + ".INDEX"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (check1 && check2,"both indexes not found check1="+check1+" check2="+check2);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify a schema pattern for which there is a
match.  An exception should be thrown, since we do not
support schema pattern.

SQL400 - When you give the native driver a search pattern and they are not
         supported, we are simple going to return 0 rows.  The name isn't
         found by the CLI and therefore, nothing is returned.
**/
    public void Var013()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "INDEXTABLE", false, false);

            if ((getRelease() < JDTestDriver.RELEASE_V5R5M0) && (getDriver () == JDTestDriver.DRIVER_TOOLBOX))
            {
                failed("Didn't throw SQLException");
            }
            else
            {
                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                assertCondition (rows == 0);
            }
        }
        catch (Exception e)
        {
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            else
                failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify a schema for which there is no match.
No rows should be returned.
**/
    public void Var014()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                "BOGUS", "INDEXTABLE", false, false);

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
getIndexInfo() - Specify null for the table.
No rows should be returned.
**/
    public void Var015()
    {
        try {
          ResultSet rs;
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // jcc driver does not permit null as table name.  Use ""
            rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.COLLECTION, "", false, false);

          } else {
            rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.COLLECTION, null, false, false);
          }


            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (isSysibmMetadata()
	    		|| (getRelease() >= JDTestDriver.RELEASE_V5R5M0) && (getDriver () == JDTestDriver.DRIVER_TOOLBOX)) {

	    			assertCondition (rows > 0, "rows="+rows+" sb > 0 Rows should be returned when null is specified for the table and sysibm metadata is used. ");

	    } else {
		assertCondition (rows == 0, "rows="+rows+" sb 0 No rows should be returned when null is specified for the table");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify empty string for the table.
No rows should be returned.
**/
    public void Var016()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.COLLECTION, "", false, false);

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
getIndexInfo() - Specify a table that matches the table
exactly.  All matching columns should be returned.
**/
    public void Var017()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);

            boolean check1 = false;
            boolean check2 = false;

            while (rs.next ()) {
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                if (index.equals (JDDMDTest.COLLECTION + ".INDEX1"))
                    check1 = true;
                else if (index.equals (JDDMDTest.COLLECTION + ".INDEX"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (check1 && check2,"both indexes not found check1="+check1+" check2="+check2);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify a table pattern for which there is a
match.  An exception should be thrown, since we do not
support table pattern.

SQL400 - When you give the native driver a search pattern and they are not
         supported, we are simple going to return 0 rows.  The name isn't
         found by the CLI and therefore, nothing is returned.
**/
    public void Var018()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.COLLECTION, "INDEXTAB%", false, false);

            if ((getRelease() < JDTestDriver.RELEASE_V5R5M0) &&  (getDriver () == JDTestDriver.DRIVER_TOOLBOX))
            {
                failed("Didn't throw SQLException");
            }
            else
            {
                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                assertCondition (rows == 0);
            }
        }
        catch (Exception e)
        {
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            else
                failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify a table for which there is no match.
No rows should be returned.
**/
    public void Var019()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.COLLECTION, "BOGUS", false, false);

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
getIndexInfo() - Specify unique = true.
**/
    public void Var020()
    {
        try {
	    int expectedRows=1;
      message.setLength(0);

	    if (isSysibmMetadata()) {
		expectedRows = 2;
	    } else if ((getRelease() >= JDTestDriver.RELEASE_V5R5M0) && (getDriver () == JDTestDriver.DRIVER_TOOLBOX))
	    {
	    	expectedRows = 2;
	    }
            ResultSet rs = null;

            // SQL400 - Table name length issue (see above).
            if ( (getDriver () == JDTestDriver.DRIVER_TOOLBOX))
            {
                rs = dmd_.getIndexInfo (connectionCatalog_,
                    JDDMDTest.COLLECTION2, "INDEXTABLEXX", true, false);
            }
            else
            {
                rs = dmd_.getIndexInfo (connectionCatalog_,
                    JDDMDTest.COLLECTION2, "INDEXTABXX", true, false);
            }

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                message.append("\nFound index "+index);
                if (index.equals (JDDMDTest.COLLECTION2 + ".INDEXXX"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == expectedRows),"unique=true specified, check1="+check1+" or rows=("+rows+") sb "+expectedRows+" "+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify unique = false.
**/
    public void Var021()
    {
	int expectedRows = 2;

	if (isSysibmMetadata()) {
	    expectedRows = 3;
	} else if ((getRelease() >= JDTestDriver.RELEASE_V5R5M0) && (getDriver () == JDTestDriver.DRIVER_TOOLBOX))
    {
	    expectedRows = 3;
    }

	message.setLength(0);
        try {
            ResultSet rs = null;

            // SQL400 - Table name length issue (see above).
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            {
                rs = dmd_.getIndexInfo (connectionCatalog_,
                    JDDMDTest.COLLECTION2, "INDEXTABLEXX", false, false);
            }
            else
            {
                rs = dmd_.getIndexInfo (connectionCatalog_,
                    JDDMDTest.COLLECTION2, "INDEXTABXX", false, false);
            }

            boolean check1 = false;
            boolean check2 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                message.append("\nfound INDEX="+index);
                if (index.equals (JDDMDTest.COLLECTION2 + ".INDEX"))
                    check1 = true;
                if (index.equals (JDDMDTest.COLLECTION2 + ".INDEXXX"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (check1 && check2 && (rows == expectedRows), "unique = false indexes not found check1="+check1+" check2="+check2+" or rows=("+rows+") sb "+expectedRows+" "+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }




/**
getIndexInfo() - Specify approximate = true.
**/
    public void Var022()
    {
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.COLLECTION, "INDEXTABLE", false, true);

            boolean check1 = false;
            boolean check2 = false;




            while (rs.next ()) {
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                if (index.equals (JDDMDTest.COLLECTION + ".INDEX1"))
                    check1 = true;
                else if (index.equals (JDDMDTest.COLLECTION + ".INDEX"))
                    check2 = true;
            }

            rs.close ();
            assertCondition (check1 && check2, "indexes  not found check1="+check1+" check2="+check2);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIndexInfo() - Specify approximate = false.
**/
    public void Var023()
    {
	StringBuffer sb = new StringBuffer("Indexes are \n");
        try {
            ResultSet rs = dmd_.getIndexInfo (connectionCatalog_,
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);

            boolean check1 = false;
            boolean check2 = false;


            while (rs.next ()) {
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                if (index.equals (JDDMDTest.COLLECTION + ".INDEX1"))
                    check1 = true;
                else if (index.equals (JDDMDTest.COLLECTION + ".INDEX"))
                    check2 = true;
		sb.append(index+"\n");

            }
	    sb.append("indexes  not found check1="+check1+" check2="+check2+" "); 
            rs.close ();
            assertCondition (check1 && check2, sb);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }

//@C1A
/**
getIndexInfo() - Get a list of those created in this testcase and
verify the column name. Test for 128 byte column name.

**/
    public void Var024()
    {
      message.setLength(0);
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0 && is400 )
        {
            try {
		int expectedRows = 1;

		if (isSysibmMetadata()) {
		    expectedRows = 2;
		}
		 else if ((getRelease() >= JDTestDriver.RELEASE_V5R5M0) && (getDriver () == JDTestDriver.DRIVER_TOOLBOX))
	    {
		  	expectedRows = 2;
	    }

                ResultSet rs = dmd_.getIndexInfo (null, JDDMDTest.COLLECTION,
                    "LCNINDEXTABLE", false, true);
                boolean success = true;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    String indexName        = rs.getString ("INDEX_NAME");
                    String columnName       = rs.getString ("COLUMN_NAME");

		    if (indexName != null) {
			success = success && checkString("TABLE_CAT", rs.getString ("TABLE_CAT"),connectionCatalog_);
			success = success && checkString("TABLE_SCHEM", rs.getString ("TABLE_SCHEM"),JDDMDTest.COLLECTION);
			success = success && checkString("TABLE_NAME", rs.getString ("TABLE_NAME"),"LCNINDEXTABLE");



			success = success && checkString("indexName", indexName, "LCNINDEX");
			success = success && checkString("columnName", columnName,"THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST");
		    }
                }

                rs.close ();
                assertCondition ((rows == expectedRows) && success, "Added by Toolbox 8/11/2004 to test 128 byte column names. rows="+rows+" sb "+expectedRows+" success = "+success+" "+message);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception.  Added by Toolbox 8/11/2004 to test 128 byte column names.");
            }
        }
        else
            notApplicable("V5R4 and greater variation");
    }







/**
getIndexInfo() - Check all the RSMD results when using JDBC 3.0.
**/

  public void Var025() {
	  if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
	  {
		  notApplicable("Not toolbox test");
		  return;
	  }
	  if (JDTestDriver.isLUW()) {
	      notApplicable("DB2 SQL error: SQLCODE: -954, SQLSTATE: 57011, SQLERRMC: null");
	      return;
	  }

      if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 24355) {
	  notApplicable("Native Driver and SI24355 testing");
      } else {
	  checkRSMD(false);
      }
  }

  public void Var026() {
	  if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
	  {
		  notApplicable("Not toolbox test");
		  return;
	  }
	  if (JDTestDriver.isLUW()) {
	      notApplicable("LUW fails with  DB2 SQL error: SQLCODE: -954, SQLSTATE: 57011, SQLERRMC: null");
	      return;
	  }
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 24355) {
	  notApplicable("Native Driver and SI24355 testing");
      } else {
	  if (checkNotGroupTest()) { 
	      checkRSMD(true);
	  }
      }
  }

    public void checkRSMD(boolean extendedMetadata) {

    Connection connection = connection_;
    DatabaseMetaData dmd = dmd_;
    int TESTSIZE = 10;         /* Changed to 10 */

    int j = 0;
    int col = 0;
    String added = " -- Added 06/06/06 by native JDBC driver";
    boolean passed = true;
    message.setLength(0);
    String[][] methodTests = {
       { "isAutoIncrement", "1", "false" },
       { "isCaseSensitive", "1", "true" },
       { "isSearchable", "1", "true" },
       { "isCurrency", "1", "false" },
       { "isNullable", "1", "1" },
       { "isSigned", "1", "false" },
        { "getColumnDisplaySize", "1", "128" },
        { "getColumnLabel", "1", "TABLE_CAT" },
        { "getColumnName", "1", "TABLE_CAT" },
        { "getPrecision", "1", "128" },
        { "getScale", "1", "0" },
        { "getCatalogName", "1", "Y0551P2" },
        { "getColumnType", "1", "12" },
        { "getColumnTypeName", "1", "VARCHAR" },
        { "isReadOnly", "1", "true" },
        { "isWritable", "1", "false" },
        { "isDefinitelyWritable", "1", "false" },
        { "getColumnClassName", "1", "java.lang.String" },
        { "isAutoIncrement", "2", "false" },
        { "isCaseSensitive", "2", "true" },
        { "isSearchable", "2", "true" },
        { "isCurrency", "2", "false" },
        { "isNullable", "2", "1" },
        { "isSigned", "2", "false" },
        { "getColumnDisplaySize", "2", "128" },
        { "getColumnLabel", "2", "TABLE_SCHEM" },
        { "getColumnName", "2", "TABLE_SCHEM" },
        { "getPrecision", "2", "128" },
        { "getScale", "2", "0" },
        { "getCatalogName", "2", "Y0551P2" },
        { "getColumnType", "2", "12" },
        { "getColumnTypeName", "2", "VARCHAR" },
        { "isReadOnly", "2", "true" },
        { "isWritable", "2", "false" },
        { "isDefinitelyWritable", "2", "false" },
        { "getColumnClassName", "2", "java.lang.String" },
        { "isAutoIncrement", "3", "false" },
        { "isCaseSensitive", "3", "true" },
        { "isSearchable", "3", "true" },
        { "isCurrency", "3", "false" },
        { "isNullable", "3", "0" },
        { "isSigned", "3", "false" },
        { "getColumnDisplaySize", "3", "128" },
        { "getColumnLabel", "3", "TABLE_NAME" },
        { "getColumnName", "3", "TABLE_NAME" },
        { "getPrecision", "3", "128" },
        { "getScale", "3", "0" },
        { "getCatalogName", "3", "Y0551P2" },
        { "getColumnType", "3", "12" },
        { "getColumnTypeName", "3", "VARCHAR" },
        { "isReadOnly", "3", "true" },
        { "isWritable", "3", "false" },
        { "isDefinitelyWritable", "3", "false" },
        { "getColumnClassName", "3", "java.lang.String" },
        { "isAutoIncrement", "4", "false" },
        { "isCaseSensitive", "4", "true" },
        { "isSearchable", "4", "true" },
        { "isCurrency", "4", "false" },
        { "isNullable", "4", "0" },
        { "isSigned", "4", "false" },
        { "getColumnDisplaySize", "4", "6" },
        { "getColumnLabel", "4", "NON_UNIQUE" },
        { "getColumnName", "4", "NON_UNIQUE" },
        { "getPrecision", "4", "128" },
        { "getScale", "4", "0" },
        { "getCatalogName", "4", "Y0551P2" },
        { "getColumnType", "4", "5" },
        { "getColumnTypeName", "4", "SMALLINT" },
        { "isReadOnly", "4", "true" },
        { "isWritable", "4", "false" },
        { "isDefinitelyWritable", "4", "false" },
        { "getColumnClassName", "4", "java.lang.Boolean" },
        { "isAutoIncrement", "5", "false" },
        { "isCaseSensitive", "5", "true" },
        { "isSearchable", "5", "true" },
        { "isCurrency", "5", "false" },
        { "isNullable", "5", "1" },
        { "isSigned", "5", "false" },
        { "getColumnDisplaySize", "5", "128" },
        { "getColumnLabel", "5", "INDEX_QUALIFIER" },
        { "getColumnName", "5", "INDEX_QUALIFIER" },
        { "getPrecision", "5", "128" },
        { "getScale", "5", "0" },
        { "getCatalogName", "5", "Y0551P2" },
        { "getColumnType", "5", "12" },
        { "getColumnTypeName", "5", "VARCHAR" },
        { "isReadOnly", "5", "true" },
        { "isWritable", "5", "false" },
        { "isDefinitelyWritable", "5", "false" },
        { "getColumnClassName", "5", "java.lang.String" },
        { "isAutoIncrement", "6", "false" },
        { "isCaseSensitive", "6", "true" },
        { "isSearchable", "6", "true" },
        { "isCurrency", "6", "false" },
        { "isNullable", "6", "1" },
        { "isSigned", "6", "false" },
        { "getColumnDisplaySize", "6", "128" },
        { "getColumnLabel", "6", "INDEX_NAME" },
        { "getColumnName", "6", "INDEX_NAME" },
        { "getPrecision", "6", "128" },
        { "getScale", "6", "0" },
        { "getCatalogName", "6", "Y0551P2" },
        { "getColumnType", "6", "12" },
        { "getColumnTypeName", "6", "VARCHAR" },
        { "isReadOnly", "6", "true" },
        { "isWritable", "6", "false" },
        { "isDefinitelyWritable", "6", "false" },
        { "getColumnClassName", "6", "java.lang.String" },
        { "isAutoIncrement", "7", "false" },
        { "isCaseSensitive", "7", "false" },
        { "isSearchable", "7", "true" },
        { "isCurrency", "7", "false" },
        { "isNullable", "7", "0" },
        { "isSigned", "7", "true" }, { "getColumnDisplaySize", "7", "6" },
        { "getColumnLabel", "7", "TYPE" }, { "getColumnName", "7", "TYPE" },
        { "getPrecision", "7", "5" }, { "getScale", "7", "0" },
        { "getCatalogName", "7", "Y0551P2" }, { "getColumnType", "7", "5" },
        { "getColumnTypeName", "7", "SMALLINT" },
        { "isReadOnly", "7", "true" }, { "isWritable", "7", "false" },
        { "isDefinitelyWritable", "7", "false" },
        { "getColumnClassName", "7", "java.lang.Short" },
        { "isAutoIncrement", "8", "false" },
        { "isCaseSensitive", "8", "false" }, { "isSearchable", "8", "true" },
        { "isCurrency", "8", "false" }, { "isNullable", "8", "0" },
        { "isSigned", "8", "true" }, { "getColumnDisplaySize", "8", "6" },
        { "getColumnLabel", "8", "ORDINAL_POSITION" },
        { "getColumnName", "8", "ORDINAL_POSITION" },
        { "getPrecision", "8", "5" }, { "getScale", "8", "0" },
        { "getCatalogName", "8", "Y0551P2" }, { "getColumnType", "8", "5" },
        { "getColumnTypeName", "8", "SMALLINT" },
        { "isReadOnly", "8", "true" }, { "isWritable", "8", "false" },
        { "isDefinitelyWritable", "8", "false" },
        { "getColumnClassName", "8", "java.lang.Short" },
        { "isAutoIncrement", "9", "false" },
        { "isCaseSensitive", "9", "true" }, { "isSearchable", "9", "true" },
        { "isCurrency", "9", "false" }, { "isNullable", "9", "1" },
        { "isSigned", "9", "false" }, { "getColumnDisplaySize", "9", "128" },
        { "getColumnLabel", "9", "COLUMN_NAME" },
        { "getColumnName", "9", "COLUMN_NAME" },
        { "getPrecision", "9", "128" }, { "getScale", "9", "0" },
        { "getCatalogName", "9", "Y0551P2" }, { "getColumnType", "9", "12" },
        { "getColumnTypeName", "9", "VARCHAR" }, { "isReadOnly", "9", "true" },
        { "isWritable", "9", "false" },
        { "isDefinitelyWritable", "9", "false" },
        { "getColumnClassName", "9", "java.lang.String" },
        { "isAutoIncrement", "10", "false" },
        { "isCaseSensitive", "10", "true" },
        { "isSearchable", "10", "true" },
        { "isCurrency", "10", "false" },
        { "isNullable", "10", "0" },
        { "isSigned", "10", "false" },
        { "getColumnDisplaySize", "10", "1" },
        { "getColumnLabel", "10", "ASC_OR_DESC" },
        { "getColumnName", "10", "ASC_OR_DESC" },
        { "getPrecision", "10", "1" }, { "getScale", "10", "0" },
        { "getCatalogName", "10", "Y0551P2" },
        { "getColumnType", "10", "12" },
        { "getColumnTypeName", "10", "VARCHAR" },
        { "isReadOnly", "10", "true" },
        { "isWritable", "10", "false" },
        { "isDefinitelyWritable", "10", "false" },
        { "getColumnClassName", "10", "java.lang.String" },
        { "isAutoIncrement", "11", "false" },
        { "isCaseSensitive", "11", "false" },
        { "isSearchable", "11", "true" },
        { "isCurrency", "11", "false" },
        { "isNullable", "11", "0" },
        { "isSigned", "11", "true" },
        { "getColumnDisplaySize", "11", "11" },
        { "getColumnLabel", "11", "CARDINALITY" },
        { "getColumnName", "11", "CARDINALITY" },
        { "getPrecision", "11", "10" },
        { "getScale", "11", "0" },
        { "getCatalogName", "11", "Y0551P2" },
        { "getColumnType", "11", "4" },
        { "getColumnTypeName", "11", "INTEGER" },
        { "isReadOnly", "11", "true" },
        { "isWritable", "11", "false" },
        { "isDefinitelyWritable", "11", "false" },
        { "getColumnClassName", "11", "java.lang.Integer" },
        { "isAutoIncrement", "12", "false" },
        { "isCaseSensitive", "12", "false" }, { "isSearchable", "12", "true" },
        { "isCurrency", "12", "false" },
        { "isNullable", "12", "0" },
        { "isSigned", "12", "true" }, { "getColumnDisplaySize", "12", "11" },
        { "getColumnLabel", "12", "PAGES" },
        { "getColumnName", "12", "PAGES" }, { "getPrecision", "12", "10" },
        { "getScale", "12", "0" }, { "getCatalogName", "12", "Y0551P2" },
        { "getColumnType", "12", "4" },
        { "getColumnTypeName", "12", "INTEGER" },
        { "isReadOnly", "12", "true" }, { "isWritable", "12", "false" },
        { "isDefinitelyWritable", "12", "false" },
        { "getColumnClassName", "12", "java.lang.Integer" },
        { "isAutoIncrement", "13", "false" },
        { "isCaseSensitive", "13", "true" }, { "isSearchable", "13", "true" },
        { "isCurrency", "13", "false" }, { "isNullable", "13", "1" },
        { "isSigned", "13", "false" }, { "getColumnDisplaySize", "13", "128" },
        { "getColumnLabel", "13", "FILTER_CONDITION" },
        { "getColumnName", "13", "FILTER_CONDITION" },
        { "getPrecision", "13", "128" }, { "getScale", "13", "0" },
        { "getCatalogName", "13", "Y0551P2" }, { "getColumnType", "13", "12" },
        { "getColumnTypeName", "13", "VARCHAR" },
        { "isReadOnly", "13", "true" }, { "isWritable", "13", "false" },
        { "isDefinitelyWritable", "13", "false" },
        { "getColumnClassName", "13", "java.lang.String" },

    };


    String[][] v5r3NativeDifferences = {
        // Bugs in implementation
        {  "getColumnTypeName","11","VARCHAR"},
        {  "getColumnClassName","11","java.lang.String"},
        {  "getColumnTypeName","13","INTEGER"},
        {  "getColumnClassName","13","java.lang.Integer"},

    };


    String[][] v5r3NativeDifferencesExtended = {
        // Bugs in implementation
        {  "getColumnTypeName","11","VARCHAR"},
        {  "getColumnClassName","11","java.lang.String"},
        {  "getColumnTypeName","13","INTEGER"},
        {  "getColumnClassName","13","java.lang.Integer"},
        { "isSearchable", "1", "false" },
        { "isSearchable", "2", "false" },
        { "isSearchable", "3", "false" },
        { "isSearchable", "4", "false" },
        { "isSearchable", "5", "false" },
        { "isSearchable", "6", "false" },
        { "isSearchable", "7", "false" },
        { "isSearchable", "8", "false" },
        { "isSearchable", "9", "false" },
        { "isSearchable", "10", "false" },
        { "isSearchable", "11", "false" },
        { "isSearchable", "12", "false" },
    };


    String[][] v5r4NativeDifferences = {
        // Bugs in implementation
        {  "getColumnTypeName","11","VARCHAR"},
        {  "getColumnClassName","11","java.lang.String"},
        {  "getColumnTypeName","13","INTEGER"},
        {  "getColumnClassName","13","java.lang.Integer"},

    };


    String[][] v5r4NativeDifferencesExtended = {
        // Bugs in implementation
        {  "getColumnTypeName","11","VARCHAR"},
        {  "getColumnClassName","11","java.lang.String"},
        {  "getColumnTypeName","13","INTEGER"},
        {  "getColumnClassName","13","java.lang.Integer"},
        { "isSearchable", "1", "false" },
        { "isSearchable", "2", "false" },
        { "isSearchable", "3", "false" },
        { "isSearchable", "4", "false" },
        { "isSearchable", "5", "false" },
        { "isSearchable", "6", "false" },
        { "isSearchable", "7", "false" },
        { "isSearchable", "8", "false" },
        { "isSearchable", "9", "false" },
        { "isSearchable", "10", "false" },
        { "isSearchable", "11", "false" },
        { "isSearchable", "12", "false" },
    };

    String[][] sysibmDifferences = {
	{  "isNullable","1","0" },
	{  "isNullable","2","0" },
	{  "isCaseSensitive","4","false" },
	{  "isNullable","4","1" },
	{  "isSigned","4","true" },
	{  "getPrecision","4","5" },
	{  "getColumnClassName","4","java.lang.Integer" },
	{  "getColumnClassName","7","java.lang.Integer" },
	{  "isNullable","8","1" },
	{  "getColumnClassName","8","java.lang.Integer" },
	{  "isNullable","10","1" },
	{  "getColumnType","10","1" },
	{  "getColumnTypeName","10","CHAR" },
	{  "isNullable","11","1" },
	{  "getColumnTypeName","11","INTEGER" },
	{  "getColumnClassName","11","java.lang.Integer" },
	{  "isNullable","12","1" },
	{  "getColumnTypeName","13","VARCHAR" },
	{  "getColumnClassName","13","java.lang.String" },
	{  "getColumnDisplaySize","13","2000"},  /* Changed Mar 2009 */
	{  "getPrecision","13","2000" }          /* Changed Mar 2009 */
    };



    String[][] sysibmDifferences55 = {
	{  "isNullable","1","0" },
	{  "isNullable","2","0" },
	{  "isCaseSensitive","4","false" },
	{  "isNullable","4","1" },
	{  "isSigned","4","true" },
	{  "getPrecision","4","5" },
	{  "getColumnClassName","4","java.lang.Integer" },
	{  "getColumnClassName","7","java.lang.Integer" },
	{  "isNullable","8","1" },
	{  "getColumnClassName","8","java.lang.Integer" },
	{ "getColumnDisplaySize","9","1048576" },
	{ "getPrecision","9","2097152" },
	{ "getColumnType","9","2005" },
	{ "getColumnTypeName","9","NCLOB" },
	{ "getColumnClassName","9","com.ibm.db2.jdbc.app.DB2Clob" },
	{  "isNullable","10","1" },
	{  "getColumnType","10","1" },
	{  "getColumnTypeName","10","CHAR" },
	{  "isNullable","11","1" },
	{ "getColumnDisplaySize","11","20" },
	{ "getPrecision","11","19" },
	{ "getColumnType","11","-5" },
	{ "getColumnTypeName","11","BIGINT" },
	{ "getColumnClassName","11","java.lang.Long" },
	{  "isNullable","12","1" },
	{ "getColumnDisplaySize","12","20" },
	{ "getPrecision","12","19" },
	{ "getColumnType","12","-5" },
	{ "getColumnTypeName","12","BIGINT" },
	{ "getColumnClassName","12","java.lang.Long" },
	{ "getColumnDisplaySize","13","2000" },            /* Changed Dec 2008 */
	{ "getPrecision","13","2000" },                    /* Changed Dec 2008 */
	{ "getColumnType","13","12" },                     /* Changed Dec 2008 */
	{ "getColumnTypeName","13","NVARCHAR" },           /* Changed Dec 2008 */
	{ "getColumnClassName","13","java.lang.String" },  /* Changed Dec 2008 */



    };

   String[][] sysibmDifferences5540 = {
	{  "isNullable","1","0" },
	{  "isNullable","2","0" },
	{  "isCaseSensitive","4","false" },
	{  "isNullable","4","1" },
	{  "isSigned","4","true" },
	{  "getPrecision","4","5" },
	{  "getColumnClassName","4","java.lang.Integer" },
	{  "getColumnClassName","7","java.lang.Integer" },
	{  "isNullable","8","1" },
	{  "getColumnClassName","8","java.lang.Integer" },
	{ "getColumnDisplaySize","9","1048576"},  /* changed 11/10/2016 */
	{ "getPrecision","9","2097152" },
	{ "getColumnType","9","2011" },
	{ "getColumnTypeName","9","NCLOB" },
	{ "getColumnClassName","9","com.ibm.db2.jdbc.app.DB2Clob" },
	{  "isNullable","10","1" },
	{  "getColumnType","10","1" },
	{  "getColumnTypeName","10","CHAR" },
	{  "isNullable","11","1" },
	{ "getColumnDisplaySize","11","20" },
	{ "getPrecision","11","19" },
	{ "getColumnType","11","-5" },
	{ "getColumnTypeName","11","BIGINT" },
	{ "getColumnClassName","11","java.lang.Long" },
	{  "isNullable","12","1" },
	{ "getColumnDisplaySize","12","20" },
	{ "getPrecision","12","19" },
	{ "getColumnType","12","-5" },
	{ "getColumnTypeName","12","BIGINT" },
	{ "getColumnClassName","12","java.lang.Long" },
	{ "getColumnDisplaySize","13","2000" },            /* Changed Dec 2008 */
	{ "getPrecision","13","2000" },                    /* Changed Dec 2008 */
	{ "getColumnType","13","-9" },                     /* Changed Dec 2008 */
	{ "getColumnTypeName","13","NVARCHAR" },           /* Changed Dec 2008 */
	{ "getColumnClassName","13","java.lang.String" },  /* Changed Dec 2008 */



    };

    String[][] sysibmDifferences71 = {
	{  "isNullable","1","0" },
	{  "isNullable","2","0" },
	{  "isCaseSensitive","4","false" },
	{  "isNullable","4","1" },
	{  "isSigned","4","true" },
	{  "getPrecision","4","5" },
	{  "getColumnClassName","4","java.lang.Integer" },
	{  "getColumnClassName","7","java.lang.Integer" },
	{  "isNullable","8","1" },
	{  "getColumnClassName","8","java.lang.Integer" },
	{ "getColumnDisplaySize","9","1048576" },
	{ "getPrecision","9","2097152" },
	{ "getColumnType","9","2005" },
	{ "getColumnTypeName","9","NCLOB" },
	{ "getColumnClassName","9","com.ibm.db2.jdbc.app.DB2Clob" },
	{  "isNullable","10","1" },
	{  "getColumnType","10","1" },
	{  "getColumnTypeName","10","CHAR" },
	{  "isNullable","11","1" },
	{ "getColumnDisplaySize","11","20" },
	{ "getPrecision","11","19" },
	{ "getColumnType","11","-5" },
	{ "getColumnTypeName","11","BIGINT" },
	{ "getColumnClassName","11","java.lang.Long" },
	{  "isNullable","12","1" },
	{ "getColumnDisplaySize","12","20" },
	{ "getPrecision","12","19" },
	{ "getColumnType","12","-5" },
	{ "getColumnTypeName","12","BIGINT" },
	{ "getColumnClassName","12","java.lang.Long" },
	{ "getColumnDisplaySize","13","2000" },            /* Changed Dec 2008 */
	{ "getPrecision","13","2000" },                    /* Changed Dec 2008 */
	{ "getColumnType","13","12" },                     /* Changed Dec 2008 */
	{ "getColumnTypeName","13","NVARCHAR" },           /* Changed Dec 2008 */
	{ "getColumnClassName","13","java.lang.String" },  /* Changed Dec 2008 */
	{ "getColumnLabel","9","LONG                FIELD               NAME" }, /* Changed Sept 011 */


    };


    String[][] sysibmDifferences7140 = {
	{  "isNullable","1","0" },
	{  "isNullable","2","0" },
	{  "isCaseSensitive","4","false" },
	{  "isNullable","4","1" },
	{  "isSigned","4","true" },
	{  "getPrecision","4","5" },
	{  "getColumnClassName","4","java.lang.Integer" },
	{  "getColumnClassName","7","java.lang.Integer" },
	{  "isNullable","8","1" },
	{  "getColumnClassName","8","java.lang.Integer" },
	{ "getPrecision","9","2097152" },
	{ "getColumnDisplaySize","9","1048576"},            /* Nov 2016 */ 
	{ "getColumnType","9","2011"},                      /* OCT 2016 */ 
	{ "getColumnTypeName","9","NCLOB"},                 /* OCT 2016 */ 
	{ "getColumnClassName","9","com.ibm.db2.jdbc.app.DB2Clob" },
	{  "isNullable","10","1" },
	{  "getColumnType","10","1" },
	{  "getColumnTypeName","10","CHAR" },
	{  "isNullable","11","1" },
	{ "getColumnDisplaySize","11","20" },
	{ "getPrecision","11","19" },
	{ "getColumnType","11","-5" },
	{ "getColumnTypeName","11","BIGINT" },
	{ "getColumnClassName","11","java.lang.Long" },
	{  "isNullable","12","1" },
	{ "getColumnDisplaySize","12","20" },
	{ "getPrecision","12","19" },
	{ "getColumnType","12","-5" },
	{ "getColumnTypeName","12","BIGINT" },
	{ "getColumnClassName","12","java.lang.Long" },
	{ "getColumnDisplaySize","13","2000" },            /* Changed Dec 2008 */
	{ "getPrecision","13","2000" },                    /* Changed Dec 2008 */
	{ "getColumnType","13","-9"},                      /* Changed OCT 2016 */ 
	{ "getColumnTypeName","13","NVARCHAR" },           /* Changed Dec 2008 */
	{ "getColumnClassName","13","java.lang.String" },  /* Changed Dec 2008 */
	{ "getColumnLabel","9","LONG                FIELD               NAME" }, /* Changed Sept 011 */


    };



    String[][] sysibmDifferences72 = {
	{  "isNullable","1","0" },
	{  "isNullable","2","0" },
	{  "isCaseSensitive","4","false" },
	{  "isNullable","4","1" },
	{  "isSigned","4","true" },
	{  "getPrecision","4","5" },
	{  "getColumnClassName","4","java.lang.Integer" },
	{  "getColumnClassName","7","java.lang.Integer" },
	{  "isNullable","8","1" },
	{  "getColumnClassName","8","java.lang.Integer" },
	{ "getColumnDisplaySize","9","1048576" },
	{ "getPrecision","9","2097152" },
	{ "getColumnType","9","2011" },
	{ "getColumnTypeName","9","NCLOB" },
	{ "getColumnClassName","9","com.ibm.db2.jdbc.app.DB2Clob" },
	{  "isNullable","10","1" },
	{  "getColumnType","10","1" },
	{  "getColumnTypeName","10","CHAR" },
	{  "isNullable","11","1" },
	{ "getColumnDisplaySize","11","20" },
	{ "getPrecision","11","19" },
	{ "getColumnType","11","-5" },
	{ "getColumnTypeName","11","BIGINT" },
	{ "getColumnClassName","11","java.lang.Long" },
	{  "isNullable","12","1" },
	{ "getColumnDisplaySize","12","20" },
	{ "getPrecision","12","19" },
	{ "getColumnType","12","-5" },
	{ "getColumnTypeName","12","BIGINT" },
	{ "getColumnClassName","12","java.lang.Long" },
	{ "getColumnDisplaySize","13","2000" },            /* Changed Dec 2008 */
	{ "getPrecision","13","2000" },                    /* Changed Dec 2008 */
	{ "getColumnType","13","-9" },                     /* Changed Dec 2008 */
	{ "getColumnTypeName","13","NVARCHAR" },           /* Changed Dec 2008 */
	{ "getColumnClassName","13","java.lang.String" },  /* Changed Dec 2008 */
	/* Added Aug 2011 for V7R2 */
	{ "getColumnLabel","9","LONG                FIELD               NAME"},
        


    };



    String[][] sysibmDifferences73 = {
	{  "isNullable","1","0" },
	{  "isNullable","2","0" },
	{  "isCaseSensitive","4","false" },
	{  "isNullable","4","1" },
	{  "isSigned","4","true" },
	{  "getPrecision","4","5" },
	{  "getColumnClassName","4","java.lang.Integer" },
	{  "getColumnClassName","7","java.lang.Integer" },
	{  "isNullable","8","1" },
	{  "getColumnClassName","8","java.lang.Integer" },
	{ "getColumnDisplaySize","9","1048576" },
	{ "getPrecision","9","2097152" },
	{ "getColumnType","9","2011" },
	{ "getColumnTypeName","9","NCLOB" },
	{ "getColumnClassName","9","com.ibm.db2.jdbc.app.DB2Clob" },
	{  "isNullable","10","1" },
	{  "getColumnType","10","1" },
	{  "getColumnTypeName","10","CHAR" },
	{  "isNullable","11","1" },
	{ "getColumnDisplaySize","11","20" },
	{ "getPrecision","11","19" },
	{ "getColumnType","11","-5" },
	{ "getColumnTypeName","11","BIGINT" },
	{ "getColumnClassName","11","java.lang.Long" },
	{  "isNullable","12","1" },
	{ "getColumnDisplaySize","12","20" },
	{ "getPrecision","12","19" },
	{ "getColumnType","12","-5" },
	{ "getColumnTypeName","12","BIGINT" },
	{ "getColumnClassName","12","java.lang.Long" },
	{ "getColumnDisplaySize","13","2000" },            /* Changed Dec 2008 */
	{ "getPrecision","13","2000" },                    /* Changed Dec 2008 */
	{ "getColumnType","13","-9" },                     /* Changed Dec 2008 */
	{ "getColumnTypeName","13","NVARCHAR" },           /* Changed Dec 2008 */
	{ "getColumnClassName","13","java.lang.String" },  /* Changed Dec 2008 */
	/* Added Aug 2011 for V7R2 */
	{"getColumnLabel","1","Table               Cat"},
{"getColumnLabel","2","Table               Schem"},
{"getColumnLabel","3","Table               Name"},
{"getColumnLabel","4","Non                 Unique"},
{"getColumnLabel","5","Index               Qualifier"},
{"getColumnLabel","6","Index               Name"},
{"getColumnLabel","7","Type"},
{"getColumnLabel","8","Ordinal             Position"},
{"getColumnLabel","9","Column              Name"},
{"getColumnLabel","11","Cardinality"},
{"getColumnLabel","12","Pages"},
{"getColumnLabel","13","Filter              Condition"},
        
	/* Updated April 2019 for V7R3 */ 

    };



    String[][] toolboxDifferences = {};

    String[][] nativeExtendedDifferences = {

    { "isSearchable", "1", "false" }, { "isSearchable", "2", "false" },
        { "isSearchable", "3", "false" }, { "isSearchable", "4", "false" },
        { "isSearchable", "5", "false" }, { "isSearchable", "6", "false" },
        { "isSearchable", "7", "false" }, { "isSearchable", "8", "false" },
        { "isSearchable", "9", "false" }, { "isSearchable", "10", "false" },
        { "isSearchable", "11", "false" }, { "isSearchable", "12", "false" },
        { "isSearchable", "13", "false" }, };


	String[][] nativeExtendedDifferences40 = {

	    { "isSearchable", "1", "false" },
	    { "isSearchable", "2", "false" },
	    { "isSearchable", "3", "false" },
	    { "isSearchable", "4", "false" },
	    { "isSearchable", "5", "false" },
	    { "isSearchable", "6", "false" },
	    { "isSearchable", "7", "false" },
	    { "isSearchable", "8", "false" },
	    { "isSearchable", "9", "false" },
	    { "getColumnDisplaySize","9","2097152"},
	    { "getColumnType","9","2011"},
	    { "getColumnTypeName","9","NCLOB"},
	    { "getColumnType","13","-9"},

	    { "isSearchable", "10", "false" },
	    { "isSearchable", "11", "false" },
	    { "isSearchable", "12", "false" },
	    { "isSearchable", "13", "false" }, };




    String[][] fixup = {};
    String[][] fixup2 = {};

    if (checkJdbc30()) {
      try {

        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          fixup = toolboxDifferences;
	  message.append("Fixup = toolboxDifferences\n");
        }

        if (extendedMetadata) {
          String url = baseURL_ + ";extended metadata=true";
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            url = baseURL_;
          }
          connection = testDriver_.getConnection(url, userId_, encryptedPassword_);
          dmd = connection.getMetaData();

          if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            if (getRelease() == JDTestDriver.RELEASE_V5R4M0 && ! isSysibmMetadata()) {
              fixup = v5r4NativeDifferencesExtended;
	      message.append("Fixup = v5r4NativeDifferencesExtended\n");
            } else {
		if ((getRelease() == JDTestDriver.RELEASE_V5R3M0) ||
                     (getRelease() == JDTestDriver.RELEASE_V5R2M0)) {
		    fixup = v5r3NativeDifferencesExtended;
		    message.append("Fixup = v5r3NativeDifferencesExtended\n");
		} else {
		    if(getJdbcLevel() >= 4) {
			fixup = nativeExtendedDifferences40;
			message.append("Fixup = nativeExtendedDifferences40\n");
		    } else { 
			fixup = nativeExtendedDifferences;
			message.append("Fixup = nativeExtendedDifferences\n");
		    }
		}
            }
          }

        } else {

          if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            if (getRelease() == JDTestDriver.RELEASE_V5R4M0) {
              fixup = v5r4NativeDifferences;
	      message.append("Fixup = v5r4NativeDifferences\n");
	    } else {
		if ((getRelease() == JDTestDriver.RELEASE_V5R3M0) ||
                     (getRelease() == JDTestDriver.RELEASE_V5R2M0)) {
		    fixup = v5r3NativeDifferences;
		    message.append("Fixup = v5r3NativeDifferences\n");
		}
	    }
          }
        }

	if (isSysibmMetadata()) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
		if (extendedMetadata) {
		    fixup2 = sysibmDifferences73;
		    message.append("Fixup2 = sysibmDifferences73 extended MD\n");
		} else {
		    if (getJdbcLevel() >= 4) {
			fixup2 = sysibmDifferences5540;
			message.append("Fixup2 = sysibmDifferences5540 no extended MD\n");
		    } else { 
			fixup2 = sysibmDifferences55;
			message.append("Fixup2 = sysibmDifferences55 no extended MD\n");
		    }
		}
	    } else if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
		if (extendedMetadata) {
		    fixup2 = sysibmDifferences72;
		    message.append("Fixup2 = sysibmDifferences72 extended MD\n");
		} else {
		    if (getJdbcLevel() >= 4) {
			fixup2 = sysibmDifferences5540;
			message.append("Fixup2 = sysibmDifferences5540 no extended MD\n");
		    } else { 
			fixup2 = sysibmDifferences55;
			message.append("Fixup2 = sysibmDifferences55 no extended MD\n");
		    }
		}

	    } else if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {

		if (extendedMetadata) {
		    if (getJdbcLevel() >= 4) {
			fixup2 = sysibmDifferences7140;
			message.append("Fixup2 = sysibmDifferences7140 extended MD\n");
		    } else { 
			fixup2 = sysibmDifferences71;
			message.append("Fixup2 = sysibmDifferences71 extended MD\n");
		    }
		} else {
		    if (getJdbcLevel() >= 4) {
			fixup2 = sysibmDifferences5540;
			message.append("Fixup2 = sysibmDifferences5540 no extended MD\n");
		    } else { 

			fixup2 = sysibmDifferences55;
			message.append("Fixup2 = sysibmDifferences55 no extended MD\n");
		    }
		}

	    } else if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		    if (getJdbcLevel() >= 4) {
			fixup2 = sysibmDifferences5540;
			message.append("Fixup2 = sysibmDifferences5540 no extended MD\n");
		    } else { 

			fixup2 = sysibmDifferences55;
			message.append("Fixup2 = sysibmDifferences55\n");
		    }
	    } else {
		fixup2 = sysibmDifferences;
		message.append("Fixup2 = sysibmDifferences\n");
	    }
	}

        for (j = 0; j < fixup.length; j++) {
          boolean found = false;
          for (int k = 0; !found && k < methodTests.length; k++) {
            if (fixup[j][0].equals(methodTests[k][0])
                && fixup[j][1].equals(methodTests[k][1])) {
              methodTests[k] = fixup[j];
              found = true;
            }
          }
        }


        for (j = 0; j < fixup2.length; j++) {
          boolean found = false;
          for (int k = 0; !found && k < methodTests.length; k++) {
            if (fixup2[j][0].equals(methodTests[k][0])
                && fixup2[j][1].equals(methodTests[k][1])) {
              methodTests[k] = fixup2[j];
              found = true;
            }
          }
        }

        String catalog = connection.getCatalog();
        if (catalog == null) {
          catalog = system_.toUpperCase();
        }

        ResultSet[] rsA = new ResultSet[TESTSIZE];
        ResultSetMetaData[] rsmdA = new ResultSetMetaData[TESTSIZE];
        for (j = 0; j < TESTSIZE; j++) {
          rsA[j] = dmd.getIndexInfo(connectionCatalog_, JDDMDTest.COLLECTION,
              "INDEXTABLE", false, false);
          rsmdA[j] = rsA[j].getMetaData();

          ResultSetMetaData rsmd = rsmdA[j];
          for (int i = 0; i < methodTests.length; i++) {
            String answer = "NOT SET";
            String method = methodTests[i][0];
            col = Integer.parseInt(methodTests[i][1]);
            String expected = methodTests[i][2];
            if (expected.equals("Y0551P2")) {
              expected = catalog;
            }
            if (method.equals("isAutoIncrement")) {
              answer = "" + rsmd.isAutoIncrement(col);
            }
            if (method.equals("isCaseSensitive")) {
              answer = "" + rsmd.isCaseSensitive(col);
            }
            if (method.equals("isSearchable")) {
              answer = "" + rsmd.isSearchable(col);
            }
            if (method.equals("isCurrency")) {
              answer = "" + rsmd.isCurrency(col);
            }
            if (method.equals("isNullable")) {
              answer = "" + rsmd.isNullable(col);
            }
            if (method.equals("isSigned")) {
              answer = "" + rsmd.isSigned(col);
            }
            if (method.equals("getColumnDisplaySize")) {
              answer = "" + rsmd.getColumnDisplaySize(col);
            }
            if (method.equals("getColumnLabel")) {
              answer = "" + rsmd.getColumnLabel(col);
            }
            if (method.equals("getColumnName")) {
              answer = "" + rsmd.getColumnName(col);
            }
            if (method.equals("getSchemaName")) {
              answer = "" + rsmd.getSchemaName(col);
            }
            if (method.equals("getPrecision")) {
              answer = "" + rsmd.getPrecision(col);
            }
            if (method.equals("getScale")) {
              answer = "" + rsmd.getScale(col);
            }
            if (method.equals("getTableName")) {
              answer = "" + rsmd.getTableName(col);
            }
            if (method.equals("getCatalogName")) {
              answer = "" + rsmd.getCatalogName(col);
            }
            if (method.equals("getColumnType")) {
              answer = "" + rsmd.getColumnType(col);
            }
            if (method.equals("getColumnTypeName")) {
              answer = "" + rsmd.getColumnTypeName(col);
            }
            if (method.equals("isReadOnly")) {
              answer = "" + rsmd.isReadOnly(col);
            }
            if (method.equals("isWritable")) {
              answer = "" + rsmd.isWritable(col);
            }
            if (method.equals("isDefinitelyWritable")) {
              answer = "" + rsmd.isDefinitelyWritable(col);
            }
            if (method.equals("getColumnClassName")) {
              answer = "" + rsmd.getColumnClassName(col);
            }

            if (answer.equals(expected)) {
              // ok
            } else {
              passed = false;
              if (j == 0) {
                message.append("Expected: \"" + method + "\",\"" + col
                    + "\",\"" + expected + "\"\n" + "{\"" + method
                    + "\",\"" + col + "\",\"" + answer + "\"\n");
              }
            }
          } /* for i */
        } /* for j */
        assertCondition(passed, message.toString() + added);

      } catch (Exception e) {
        failed(e, "Unexpected Error on loop " + j + " col = " + col + " "
            + added);
      } catch (Error e) {
        failed(e, "Unexpected Error on loop " + j + " col = " + col + " "
            + added);
      }
    }
  }

/**
getIndexInfo() - Run getIndexInfo multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var027()
    {
	String added = " -- added by 1/31/2011 to test for native statement leak in metadata";
        if (checkNative()) {
            try {

		// Tie up a bunch of statement handles to make sure we don't get a weird behavior
		Statement[] extraStatements = new Statement[1000];
		for (int i = 0; i < extraStatements.length; i++) {
		    extraStatements[i] = connection_.createStatement(); 
		} 


		Statement stmt = connection_.createStatement();
		// Do not run for more than 10 minutes
		long endTime = System.currentTimeMillis() + 600000;
		for (int i = 0; i < 1000 && System.currentTimeMillis() < endTime ; i++) {
		    // System.out.println("Calling getIndexInfo");
		    if (i % 5 == 0) {
		      JDJobName.sendProgramMessage("On loop "+i+" of 1000");
		    }
		    ResultSet rs = dmd_.getIndexInfo (null, JDDMDTest.COLLECTION,
                    "LCNINDEXTABLE", false, true);


		    rs.close();
		}

		Statement stmt2 = connection_.createStatement();
		int beginningHandle = JDReflectionUtil.callMethod_I(stmt,"getStatementHandle");
		int endingHandle = JDReflectionUtil.callMethod_I(stmt2,"getStatementHandle");

		for (int i = 0; i < extraStatements.length; i++) {
		    extraStatements[i].close(); 
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


/* Test a readonly connection */


    public void Var028()
    {


        try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            ResultSet rs = dmd.getIndexInfo (null,
                JDDMDTest.COLLECTION, "INDEXTABLE", false, false);

            boolean check1 = false;
            boolean check2 = false;

            while (rs.next ()) {
                String index = rs.getString ("INDEX_QUALIFIER")
                    + "." + rs.getString ("INDEX_NAME");
                if (index.equals (JDDMDTest.COLLECTION + ".INDEX1"))
                    check1 = true;
                else if (index.equals (JDDMDTest.COLLECTION + ".INDEX"))
                    check2 = true;
		else {
		    System.out.println("Warning:  found "+index);
		}
            }

            rs.close ();
	    c.close(); 
            assertCondition (check1 && check2, "both indexes not found check1="+check1+" check2="+check2);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }

    }
}
