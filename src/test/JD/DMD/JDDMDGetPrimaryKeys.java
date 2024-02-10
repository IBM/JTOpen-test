///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetPrimaryKeys.java
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
 // File Name:    JDDMDGetPrimaryKeys.java
 //
 // Classes:      JDDMDGetPrimaryKeys
 //
 ////////////////////////////////////////////////////////////////////////
 //
 // LUW differences
 //
 // 1.  empty catalog string returns rows -- Var005
 //
 ////////////////////////////////////////////////////////////////////////
 //
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

import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Random;



/**
Testcase JDDMDGetPrimaryKeys.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getPrimaryKeys()
</ul>
**/
public class JDDMDGetPrimaryKeys
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;


    StringBuffer message = new StringBuffer() ;
    String messageColumnName = "";



/**
Constructor.
**/
    public JDDMDGetPrimaryKeys (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetPrimaryKeys",
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

        Statement s = connection_.createStatement ();

	if (JDTestDriver.isLUW()) {
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".PK1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
			     + "CONSTRAINT "
			     + "PKKEY1 PRIMARY KEY (CUSTID))");

        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".PK2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                         + "CONSTRAINT "
                         + "PKKEY2 PRIMARY KEY (NAME))");

        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".PK1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                         + "CONSTRAINT "
                         + "PKKEY3 PRIMARY KEY (ACCTNBR))");



	} else {
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".PK1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
			     + "CONSTRAINT " + JDDMDTest.COLLECTION
			     + ".PKKEY1 PRIMARY KEY (CUSTID))");
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".PK2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".PKKEY2 PRIMARY KEY (NAME))");
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
			     + ".PK1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
			     + "CONSTRAINT " + JDDMDTest.COLLECTION2
			     + ".PKKEY3 PRIMARY KEY (ACCTNBR))");


	}


        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0 && !JDTestDriver.isLUW()){
            s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION     //@C1A
                         + ".LCNPK1 (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INTEGER NOT NULL PRIMARY KEY, COL2 INTEGER)");
        }
        s.close ();

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

	if (JDTestDriver.isLUW()) {
	    try {
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".PK1 DROP PRIMARY KEY ");
	    } catch (Exception e) {  e.printStackTrace();}
	    try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".PK2 DROP PRIMARY KEY ");
	    } catch (Exception e) {  e.printStackTrace(); }
	    try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
			     + ".PK1 DROP PRIMARY KEY ");
	    } catch (Exception e) {  e.printStackTrace(); }

	} else {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".PK1 DROP PRIMARY KEY CASCADE");
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".PK2 DROP PRIMARY KEY CASCADE");
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
			     + ".PK1 DROP PRIMARY KEY CASCADE");
	}
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".PK1");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".PK2");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
            + ".PK1");
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0){
            s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION //@C1A
                         + ".LCNPK1");
        }

        s.close ();
        connection_.close ();
    }



    public boolean check(int a, int expected, String info) {
	boolean result = (a == expected);
	if (!result) {
	    message.append(info+"="+a+" sb "+expected+" "+messageColumnName+"\n");

	}
	return result;
    }

    public boolean check(String a, String expected, String info) {
	boolean result;
	if (a == null) {
	    result = ( expected == null);
	} else {
	    result = a.equals(expected);
	}
	if (!result) {
	    message.append(info+"=\""+a+"\" sb \""+expected+"\" "+messageColumnName+"\n");
	}
	return result;
    }


/**
getPrimaryKeys() - Check the result set format.
**/
    public void Var001()
    {
        try {
          message.setLength(0);

            ResultSet rs = dmd_.getPrimaryKeys (null, "SYSIBM", "SYSDUMMY1");
            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "TABLE_CAT", "TABLE_SCHEM",
                                       "TABLE_NAME", "COLUMN_NAME",
                                       "KEY_SEQ", "PK_NAME" };
            int[] expectedTypes = { Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.VARCHAR,
                                    Types.SMALLINT, Types.VARCHAR };

            int count = rsmd.getColumnCount ();
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);
            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);

            rs.close ();
            assertCondition ((count == 6) && (namesCheck) && (typesCheck), message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Verify all columns.

SQL400 - The native JDBC driver is going to return the primary key name.  The Toolbox is going to return
         null.
**/
    public void Var002()
    {
        try {

          message.setLength(0);
            messageColumnName="";

            ResultSet rs = dmd_.getPrimaryKeys (null, JDDMDTest.COLLECTION,
                "PK1");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;

                String tableCat       = rs.getString ("TABLE_CAT");
                String tableSchem     = rs.getString ("TABLE_SCHEM");
                String tableName      = rs.getString ("TABLE_NAME");
                String columnName     = rs.getString ("COLUMN_NAME");
                short keySeq          = rs.getShort ("KEY_SEQ");
                String pkName         = rs.getString ("PK_NAME");

		messageColumnName=columnName;
                success = check(tableCat, connection_.getCatalog (), "TABLE_CAT") && success ;
                success = check(tableSchem, JDDMDTest.COLLECTION, "TABLE_SCHEM") && success;
                success = check(tableName, "PK1", "TABLE_NAME") && success;
                success = check(columnName, "CUSTID", "COLUMN_NAME") && success;
                success = check(keySeq,1, "KEY_SEQ") && success;


		// For the group testing on V5R4 done by native driver, this still return the old
		// results.  Updating for now.

		success = check(pkName, "PKKEY1", "PK_NAME") && success ;

            }

            rs.close ();
            assertCondition ((rows == 1) && success, "rows = "+rows+" sb 1 success="+success+" message="+message.toString() );
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Specify all null parameters.  Verify no rows are returned.
**/
    public void Var003()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys (null, null, null);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            if (isSysibmMetadata()) {
              assertCondition (rows > 0, rows+" rows returned when null specified for all parameters using SYSIBM metadata");
            } else {
              assertCondition (rows == 0, rows+" rows returned when null specified for all parameters");
            }
        }
        catch (Exception e)  {
	    if (JDTestDriver.isLUW()) {
		String message1 = e.toString();
		if (message1.indexOf("Invalid getIndexInfo") >=0) {
		    assertCondition(true);
		    return;
		}
	    }

            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Specify null for the catalog.
**/
    public void Var004()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys (null, JDDMDTest.COLLECTION, "PK1");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String tableName = rs.getString ("TABLE_NAME");
                if (tableName.equals ("PK1"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" sb true --  rows="+rows+" sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Specify empty string for the catalog.
**/
    public void Var005()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys ("", JDDMDTest.COLLECTION, "PK1");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (isSysibmMetadata() || getDriver() == JDTestDriver.DRIVER_JCC) {
		assertCondition (rows > 0, "rows = "+rows+" sb > 0 since empty string passed for catalog and jcc driver used ");
	    } else {
		assertCondition (rows == 0, "rows = "+rows+" sb 0 since empty string passed for catalog");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Specify a catalog that matches the catalog
exactly.
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys (connection_.getCatalog (),
                                                 JDDMDTest.COLLECTION, "PK1");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String tableName = rs.getString ("TABLE_NAME");
                if (tableName.equals ("PK1"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" sb true --  rows="+rows+" sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Specify "localhost" for the catalog.
**/
    public void Var007()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4 ||
          (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() >= JDTestDriver.RELEASE_V5R5M0) ||
          (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
        notApplicable("\"localhost\" variation ");
      } else {

        try {
            ResultSet rs = dmd_.getPrimaryKeys ("localhost",
                                                 JDDMDTest.COLLECTION, "PK1");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String tableName = rs.getString ("TABLE_NAME");
                if (tableName.equals ("PK1"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
getPrimaryKeys() - Specify a catalog pattern for which there is a
match.  No matching rows should be returned, since we do not
support catalog pattern.
**/
    public void Var008()
    {
        try {
        String catalog = connection_.getCatalog ();
        String searchString;
        if (catalog == null ) {
          searchString="BOGUS%";
        } else {
          searchString = catalog.substring (0, 4) + "%";
        }
            ResultSet rs = dmd_.getPrimaryKeys (searchString,
                JDDMDTest.COLLECTION, "PK1");

            int rows = 0;
	    while (rs.next ()) {
                ++rows;
	    }

            rs.close ();
            assertCondition (rows == 0, "expected 0 rows but got rows="+rows+" stuff" );
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Specify a catalog for which there is no match.
No matching columns should be returned.
**/
    public void Var009()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys ("BOGUS",
                JDDMDTest.COLLECTION, "PK1");

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
getPrimaryKeys() - Specify null for the schema.
**/
    public void Var010()
    {
        try {
	    String tablenames="";
            ResultSet rs = dmd_.getPrimaryKeys (null,
                                                 null, "PK1");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String tableName = rs.getString ("TABLE_NAME");
                if (tableName.equals ("PK1"))
                    check1 = true;
		tablenames+="\nRow "+rows+" =,"+rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6);
            }

            rs.close ();
            if (JDTestDriver.isLUW() || isSysibmMetadata()) {
              // Two rows are
              // Row 1 =,null,JDTSL12,PK1,ACCTNBR,1,PKKEY3
              // Row 2 =,null,JDTSL14,PK1,CUSTID,1,PKKEY1
              assertCondition (check1 && (rows >= 2), "PK1 found="+check1+" rows = "+rows+" sb >= 2 when null passed for schema "+tablenames);

            } else {
               assertCondition (check1 && (rows >= 1), "PK1 found="+check1+" rows = "+rows+" sb >= 1 when null passed for schema "+tablenames);
            }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Specify empty string for the schema.
**/
    public void Var011()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys (connection_.getCatalog (),
                "", "PK1");

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
getPrimaryKeys() - Specify a schema that matches the schema
exactly.
**/
    public void Var012()
    {
        try {
	    String catalog = connection_.getCatalog ();
            ResultSet rs = dmd_.getPrimaryKeys (catalog,
                                                 JDDMDTest.COLLECTION, "PK2");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String tableName = rs.getString ("TABLE_NAME");
                if (tableName.equals ("PK2"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" rows="+rows+" sb 1 catalog="+catalog);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Specify a schema pattern for which there is a
match.  An exception should be thrown, since we do not
support schema pattern.


SQL400 - The native driver supports search patters for primary keys
         so we just check that something is found.

JDBC40 -- The JDBC spec states that the schema parameter is not a pattern.
       -- The name must match exactly.  The correct behavior is to return no data.
**/
  public void Var013() {
    if ((JDTestDriver.isLUW() || (getJdbcLevel() >= 4 && getDriver() != JDTestDriver.DRIVER_TOOLBOX)) ||    //@C2C Do not execute if Toolbox driver
        (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() >= JDTestDriver.RELEASE_V5R5M0) ||
        (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
      try {
        ResultSet rs = dmd_.getPrimaryKeys(connection_.getCatalog(),
            JDDMDTest.SCHEMAS_PERCENT, "PK1");
        int rows = 0;
        while (rs.next())
          ++rows;

        rs.close();

        assertCondition(rows == 0, "No rows should have been return for schema"
            + JDDMDTest.SCHEMAS_PERCENT);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }

    } else {
      try {
        ResultSet rs = dmd_.getPrimaryKeys(connection_.getCatalog(),
            JDDMDTest.SCHEMAS_PERCENT, "PK1");

        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          failed("Didn't throw SQLException");
        } else {
          int rows = 0;
          while (rs.next())
            ++rows;

          rs.close();

          assertCondition(rows > 0, "Rows = "+rows+" sb > 0" );
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        else
          failed(e, "Unexpected Exception");
      }
    }
  }



/**
 * getPrimaryKeys() - Specify a schema for which there is no match. No rows
 * should be returned.
 */
    public void Var014()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys (connection_.getCatalog (),
                "BOGUS", "PK1");

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
getPrimaryKeys() - Specify null for the table.
No rows should be returned.
**/
    public void Var015()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys (connection_.getCatalog (),
                JDDMDTest.COLLECTION, null);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            if (isSysibmMetadata()) {
              assertCondition (rows > 0, "rows ="+rows+" sb > 0 since null specified for table when using SYSIBM metadata ");

            } else {
            assertCondition (rows == 0, "rows ="+rows+" sb 0 since null specified for table");
            }
        }
        catch (Exception e)  {
	    if (JDTestDriver.isLUW()) {
		String message1 = e.toString();
		if (message1.indexOf("Invalid getIndexInfo") >=0) {
		    assertCondition(true);
		    return;
		}
	    }
            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Specify empty string for the table.
**/
    public void Var016()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys (connection_.getCatalog (),
                JDDMDTest.COLLECTION, "");

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
getPrimaryKeys() - Specify a table that matches the table
exactly.  All matching columns should be returned.
**/
    public void Var017()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys (connection_.getCatalog (),
                JDDMDTest.COLLECTION2, "PK1");

            boolean check4 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String tableName = rs.getString ("TABLE_NAME");
                if (tableName.equals ("PK1"))
                    check4 = true;
            }

            rs.close ();
            assertCondition (check4 && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPrimaryKeys() - Specify a table pattern for which there is a
match.  An exception should be thrown, since we do not
support table pattern.

SQL400 - The native driver supports search patters for primary keys
         so we just check that something is found.
JDBC40 - The javadoc states that the table must match the table name as
         stored in the database.  It is "NOT" a pattern.
**/
  public void Var018() {
    if (JDTestDriver.isLUW() || (getJdbcLevel() >= 4 && getDriver() != JDTestDriver.DRIVER_TOOLBOX) || //@C2C Do not execute if Toolbox driver
        (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() >= JDTestDriver.RELEASE_V5R5M0) ||
        (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
      try {
        ResultSet rs = dmd_.getPrimaryKeys(connection_.getCatalog(),
            JDDMDTest.COLLECTION, "PK%");

        int rows = 0;
        while (rs.next())
          ++rows;

        rs.close();
        assertCondition(rows == 0, "No rows should have been retrieved for PK%");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }

    } else {
      try {
        ResultSet rs = dmd_.getPrimaryKeys(connection_.getCatalog(),
            JDDMDTest.COLLECTION, "PK%");

        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          failed("Didn't throw SQLException");
        } else {
          int rows = 0;
          while (rs.next())
            ++rows;

          rs.close();
          assertCondition(rows > 0,
              "No primay keys found when specifying primary key pattern");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        else
          failed(e, "Unexpected Exception");
      }
    }
  }



/**
 * getPrimaryKeys() - Specify a table for which there is no match. No rows
 * should be returned.
 */
    public void Var019()
    {
        try {
            ResultSet rs = dmd_.getPrimaryKeys (connection_.getCatalog (),
                JDDMDTest.COLLECTION, "BOGUS");

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

//@C1A
/**
getPrimaryKeys() - Verify you can retrieve a 128 byte column name.
**/
    public void Var020()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0 && !JDTestDriver.isLUW()){
            try {
                ResultSet rs = dmd_.getPrimaryKeys (null, JDDMDTest.COLLECTION,
                    "LCNPK1");
                boolean success = true;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    String columnName     = rs.getString ("COLUMN_NAME");

                    success = success && (columnName.equals ("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST"));
                }

                rs.close ();
                assertCondition ((rows == 1) && success, "Added by Toolbox 8/12/2004 to test 128 byte column names");
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception.  Added by Toolbox 8/12/2004 to test 128 byte column names");
            }
        }
        else
            notApplicable("V5R4 or greater variation.");
    }


/**
getPrimaryKeys() - Run getPrimaryKeys multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var021()
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
		    // System.out.println("Calling getPrimaryKeys");
		    ResultSet rs =
		      dmd_.getPrimaryKeys (connection_.getCatalog (),
					   JDDMDTest.COLLECTION2, "PK1");

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


  public void Var022() {
	  checkRSMD(false);
  }
  public void Var023() {
      if (checkNotGroupTest()) { 
	  checkRSMD(true);
      }
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

	/* Primed from 546CN */ 
	String [][] methodTests = {
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
      {"getColumnLabel","5","KEY_SEQ"},
      {"getColumnName","5","KEY_SEQ"},
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
      {"isCaseSensitive","6","true"},
      {"isSearchable","6","true"},
      {"isCurrency","6","false"},
      {"isNullable","6","0"},
      {"isSigned","6","false"},
      {"getColumnDisplaySize","6","128"},
      {"getColumnLabel","6","PK_NAME"},
      {"getColumnName","6","PK_NAME"},
      {"getPrecision","6","128"},
      {"getScale","6","0"},
      {"getCatalogName","6","LOCALHOST"},
      {"getColumnType","6","12"},
      {"getColumnTypeName","6","VARCHAR"},
      {"isReadOnly","6","true"},
      {"isWritable","6","false"},
      {"isDefinitelyWritable","6","false"},
      {"getColumnClassName","6","java.lang.String"},


	};







	String [][] fixup = {};

	String[][] fixup545T = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"isNullable","6","1"},
	};

	String[][] fixup545NX = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"getColumnClassName","5","java.lang.Short"},
	    {"isNullable","6","1"},
	};

	String[][] fixupExtended545N = {
	    {"isSearchable","1","false"},
	    {"isNullable","1","1"},
	    {"isSearchable","2","false"},
	    {"isNullable","2","1"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"getColumnClassName","5","java.lang.Short"},
	    {"isSearchable","6","false"},
	    {"isNullable","6","1"},
	};

	String[][] fixupExtended546N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},


	};

	String[][] fixupExtended726N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"getColumnLabel","1","Table               Cat"},
	    {"getColumnLabel","2","Table               Schem"},
	    {"getColumnLabel","3","Table               Name"},
	    {"getColumnLabel","4","Column              Name"},
	    {"getColumnLabel","5","Key                 Seq"},
	    {"getColumnLabel","6","Pk                  Name"},
	};



	String[][] fixupExtended715T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","6","0"},
	};


	String[][] fixupExtended736T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","6","0"},
	    {"getColumnLabel","1","Table Cat"},
	    {"getColumnLabel","2","Table Schem"},
	    {"getColumnLabel","3","Table Name"},
	    {"getColumnLabel","4","Column Name"},
	    {"getColumnLabel","5","Key Seq"},
	    {"getColumnLabel","6","Pk Name"},
	};


	Object[][] fixupArrayExtended = {

	    {"543T", fixup545T},
	    {"544T", fixup545T},
	    {"545T", fixup545T},
	    {"546T", fixup545T},

	    {"614T", fixup545T},
	    {"615T", fixup545T},
	    {"616T", fixup545T},
	    {"617T", fixup545T},
	    {"618T", fixup545T},

	    {"714T", fixupExtended715T}, 
	    {"715T", fixupExtended715T},
	    {"716T", fixupExtended715T, "primed 10/5/2012"},
	    {"717T", fixupExtended715T, "primed 10/5/2012"},
	    {"718T", fixupExtended715T, "primed 10/5/2012"},
	  

	    {"726T", fixupExtended715T},
	    {"727T", fixupExtended715T},

	    {"736T", fixupExtended736T, "Updated 5/29/2019"}, 
	    {"737T", fixupExtended736T, "Updated 5/29/2019"}, 
	    {"738T", fixupExtended736T, "Updated 5/29/2019"},
	    {"739T", fixupExtended736T, "Updated 5/29/2019"}, 



	    {"544N", fixupExtended545N},
	    {"545N", fixupExtended545N},
	    {"546N", fixupExtended546N},
	    {"614N", fixupExtended546N},
	    {"615N", fixupExtended546N},
	    {"616N", fixupExtended546N},
	    {"714N", fixupExtended546N},
	    {"715N", fixupExtended546N},
	    {"716N", fixupExtended546N},
	    {"717N", fixupExtended546N},
	    {"718N", fixupExtended546N},
	    {"719N", fixupExtended546N},
	    {"725N", fixupExtended546N},
	    {"726N", fixupExtended726N},
	    {"727N", fixupExtended726N},
	    {"728N", fixupExtended726N},
	    {"729N", fixupExtended726N},

	};




	Object[][] fixupArray = {
	    {"543TX", fixup545T},
	    {"544TX", fixup545T},
	    {"545TX", fixup545T},
	    {"546TX", fixup545T},

	    {"614TX", fixup545T},
	    {"615TX", fixup545T},
	    {"616TX", fixup545T},
	    {"617TX", fixup545T},
	    {"618TX", fixup545T},

	    {"714TX", fixup545T},
	    {"715TX", fixup545T},
	    {"716TX", fixup545T},
	    {"717TX", fixup545T},
	    {"718TX", fixup545T},
	    {"719TX", fixup545T},

	    {"724TX", fixup545T},
	    {"725TX", fixup545T},
	    {"726TX", fixup545T},
	    {"727TX", fixup545T},

	    {"544NX", fixup545NX},
	    {"545NX", fixup545NX}, 

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
		    rsA[j] = dmd.getPrimaryKeys(connection_.getCatalog (),
					   JDDMDTest.COLLECTION2, "PK1");

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


/* Test readonly connection */ 
/**
getPrimaryKeys() - Test readonly connection 
**/
    public void Var024()
    {
        try {

	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            ResultSet rs = dmd.getPrimaryKeys (null, JDDMDTest.COLLECTION, "PK1");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String tableName = rs.getString ("TABLE_NAME");
                if (tableName.equals ("PK1"))
                    check1 = true;
            }

            rs.close ();
	    c.close(); 
            assertCondition (check1 && (rows == 1), "check1="+check1+" sb true --  rows="+rows+" sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }

  public void Var025() {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;

      try {
        // Setup
        String collection = JDDMDTest.COLLECTION;
        Connection c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
        Statement s = c.createStatement();
        String[] cleanupSQL = { "drop trigger " + collection + ".countprikey",
            "drop trigger " + collection + ".countprikeyA",
            "drop table " + collection + ".prikey",
            "drop table " + collection + ".prikey2",
            "drop table " + collection + ".prikeyA",
            "drop table " + collection + ".prikeyA2", };
        for (int i = 0; i < cleanupSQL.length; i++) {
          sb.append("Executing " + cleanupSQL[i] + "\n");
          try {
            s.executeUpdate(cleanupSQL[i]);
          } catch (Exception e) {
            sb.append("Warning.. Exception during cleanup ");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        String[] setupSQL = {
            "create table " + collection + ".prikey(c1 int primary key)",
            "create table " + collection + ".prikey2(c1 int)",
            "insert into " + collection + ".prikey2 values(0)",
            "create trigger "
                + collection
                + ".countprikey after insert on " + collection + ".prikey for each row mode db2sql update "
                + collection + ".prikey2 set C1 = C1 + 1",

            "create table " + collection + ".prikeyA(c1 int primary key)",
            "create table " + collection + ".prikeyA2(c1 int)",
            "insert into " + collection + ".prikeyA2 values(0)",
            "create trigger "
                + collection
                + ".countprikeyA after insert on " + collection + ".prikeyA for each row mode db2sql update "
                + collection + ".prikeyA2 set C1 = C1 + 1" };

        for (int i = 0; i < setupSQL.length; i++) {
          sb.append("Executing " + setupSQL[i] + "\n");
          s.executeUpdate(setupSQL[i]);
        }

        for (int i = 0; i < 10; i++) {
          sb.append("Getting new connection");
          Connection cTest = testDriver_.getConnection(baseURL_
              + ";metadata source=0", userId_, encryptedPassword_);
          DatabaseMetaData dmd = cTest.getMetaData();
          Statement sTest = cTest.createStatement(); 
          sb.append("Set schema "+collection+"\n"); 
          sTest.executeUpdate("Set schema "+collection);
          Random random = new Random();
          for (int j = 0; j < 100; j++) {
            ResultSet rs;

            // Randomly execute a method
            switch (random.nextInt(15)) {
            case 0:

              // do the sequence from PMR B2SHXV
              sb.append("Calling dmd.getColumns(null," + collection
                  + ",PRIKEY)\n");
              rs = dmd.getColumns(null, collection, "PRIKEY", null);
              while (rs.next()) {
              }
              rs.close();
              break;
            case 1:
              sb.append("Calling dmd.getPrimaryKeys(null," + collection
                  + ",PRIKEY)\n");
              rs = dmd.getPrimaryKeys(null, collection, "PRIKEY");
              while (rs.next()) {
              }
              rs.close();
              break;
            case 2:

              sb.append("Calling dmd.getExportedKeys(null," + collection
                  + ",PRIKEY)\n");
              rs = dmd.getExportedKeys(null, collection, "PRIKEY");
              while (rs.next()) {
              }
              rs.close();
              break;
            case 3:

              sb.append("Calling dmd.getImportedKeys(null," + collection
                  + ",PRIKEY)\n");
              rs = dmd.getImportedKeys(null, collection, "PRIKEY");
              while (rs.next()) {
              }
              rs.close();
              break;
            case 4:

              sb.append("Calling dmd.getColumns(null," + collection
                  + ",PRIKEYA)\n");
              rs = dmd.getColumns(null, "*USRLIBL", "PRIKEYA", null);
              while (rs.next()) {
              }
              rs.close();
              break;
            case 5:

              sb.append("Calling dmd.getPrimaryKeys(null," + collection
                  + ",PRIKEYA)\n");
              rs = dmd.getPrimaryKeys(null, "*USRLIBL", "PRIKEYA");
              while (rs.next()) {
              }
              rs.close();

              break;
            case 6:

              sb.append("Calling dmd.getPrimaryKeys(null," + collection
                  + ",PRIKEY)\n");
              rs = dmd.getPrimaryKeys(null, collection, "PRIKEY");
              while (rs.next()) {
              }
              rs.close();
              break;
            case 7:
              sb.append("Calling dmd.getPrimaryKeys(null," + collection
                  + ",PRIKEY2)\n");
              rs = dmd.getPrimaryKeys(null, collection, "PRIKEY2");
              while (rs.next()) {
              }
              rs.close();
              break;
            case 8:

              sb.append("Calling dmd.getPrimaryKeys(null," + collection
                  + ",PRIKEYA)\n");
              rs = dmd.getPrimaryKeys(null, collection, "PRIKEYA");
              while (rs.next()) {
              }
              rs.close();
              break;
            case 9:

              sb.append("Calling dmd.getPrimaryKeys(null," + collection
                  + ",PRIKEYA2)\n");
              rs = dmd.getPrimaryKeys(null, collection, "PRIKEYA2");
              while (rs.next()) {
              }
              rs.close();
              break;
              
            case 10:

              sb.append("Calling dmd.getVersionColumns(null," + collection
                  + ",PRIKEY)\n");
              rs = dmd.getVersionColumns(null, collection, "PRIKEY");
              while (rs.next()) {
              }
              rs.close();
              break;

            case 11:

              sb.append("Calling dmd.getBestRowIdentifier(null," + collection
                  + ",PRIKEY)\n");
              rs = dmd.getBestRowIdentifier(null, collection, "PRIKEY", 0, true);
              while (rs.next()) {
              }
              rs.close();
              break;

            case 12:

              sb.append("Calling dmd.getBestRowIdentifier(null," + collection
                  + ",PRIKEY)\n");
              rs = dmd.getBestRowIdentifier(null, collection, "PRIKEY", 1, true);
              while (rs.next()) {
              }
              rs.close();
              break;

            case 13:

              sb.append("Calling dmd.getBestRowIdentifiermns(null," + collection
                  + ",PRIKEY)\n");
              rs = dmd.getBestRowIdentifier(null, collection, "PRIKEY", 2, true);
              while (rs.next()) {
              }
              rs.close();
              break;

            
            case 14:

              sb.append("Calling dmd.getIndexInfo(null," + collection
                  + ",PRIKEY)\n");
              rs = dmd.getIndexInfo(null, collection, "PRIKEY", true, true);
              while (rs.next()) {
              }
              rs.close();
              break;

            
            
            
            

            }
          }

          cTest.close();
        }

        for (int i = 0; i < cleanupSQL.length; i++) {
          sb.append("Executing " + cleanupSQL[i] + "\n");

          s.executeUpdate(cleanupSQL[i]);
        }

        c.close();

        if (passed) {
          assertCondition(passed, "PASSED");
        } else {
          assertCondition(passed,sb);
        }

      } catch (Exception e) {
        failed(e, "Unexpected Exception " + sb.toString());
      }
    } else {
      notApplicable("Toolbox only variation");
    }
  }

}
