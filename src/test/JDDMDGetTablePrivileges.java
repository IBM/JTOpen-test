///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetTablePrivileges.java
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
 // File Name:    JDDMDGetTablePrivileges.java
 //
 // Classes:      JDDMDGetTablePrivileges
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 //
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

import java.sql.Statement;
import java.sql.Types;
import java.sql.SQLException;
import java.util.Hashtable;



/**
Testcase JDDMDGetTablePrivileges.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getTablePrivileges()
</ul>
**/
public class JDDMDGetTablePrivileges
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private Connection          connectionNoSysibm_; //@128sch
    private DatabaseMetaData    dmdNoSysibm_; //@128sch
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;
    StringBuffer message = new StringBuffer();



/**
Constructor.
**/
    public JDDMDGetTablePrivileges (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetTablePrivileges",
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
        //@128sch toolbox metadata source ROI
        String urlNoSysibm = baseURL_;
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
            urlNoSysibm += ";metadata source=0"; //roi (nosysibm) - sysibm is default in v7r1
        }
        connectionNoSysibm_ = testDriver_.getConnection(urlNoSysibm, userId_, encryptedPassword_);
        dmdNoSysibm_ = connectionNoSysibm_.getMetaData();

        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        dmd_ = connection_.getMetaData ();

        Statement s = connection_.createStatement ();
	String[] dropTables = {
	    JDDMDTest.COLLECTION + ".TPRIVS ",
	    JDDMDTest.COLLECTION + ".TPRIVS1 ",
	    JDDMDTest.COLLECTION + ".TPRIVS2 ",
	    JDDMDTest.COLLECTION + ".TPRIVSXX ",
	    JDDMDTest.COLLECTION2 + ".TPRIVS ",
	    JDDMDTest.COLLECTION2 + ".TPRIVS3 ",
	    JDDMDTest.COLLECTION2 + ".TPRIVS4 ",
	    JDDMDTest.COLLECTIONXX + ".TPRIVSXX ",
	};

	for (int i = 0; i < dropTables.length; i++) {
	    try {
		s.executeUpdate("DROP TABLE "+dropTables[i]);
	    } catch (SQLException e) {
		int errorCode = e.getErrorCode();
		if (errorCode == -204) {
		} else {
		    System.out.println("Unexpected Exception errorCode="+errorCode);
		    e.printStackTrace();
		}
	    }
	}
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS1 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS2 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVSXX (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS3 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS4 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTIONXX
            + ".TPRIVSXX (NAME INTEGER)");

        // @128sch
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)
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

        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS1");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS2");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVSXX");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS3");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS4");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTIONXX
            + ".TPRIVSXX");

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



    public boolean check( StringBuffer message1, String info, String a, String b ) {
      boolean result;
      if (a == null) {
          result = ( b == null);
      } else {
          result = a.equals(b);
      }
      if (!result) {
          message1.append(info+"=\""+a+"\" sb \""+b+"\" \n");
      }
      return result;
}




/**
getTablePrivileges() - Check the result set format.
**/
    public void Var001() {
      message.setLength(0);
    try {
      ResultSet rs = dmd_.getTablePrivileges(null, JDDMDTest.COLLECTION, "TPRIVS%");
      ResultSetMetaData rsmd = rs.getMetaData();

      String[] expectedNames = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME",
          "GRANTOR", "GRANTEE", "PRIVILEGE", "IS_GRANTABLE" };
      int[] expectedTypes = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
          Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };

      int count = rsmd.getColumnCount();
      boolean namesCheck = JDDMDTest.checkColumnNames(rsmd, expectedNames, message);
      boolean typesCheck = JDDMDTest.checkColumnTypes(rsmd, expectedTypes, message );

      rs.close();
      assertCondition((count == 7) && (namesCheck) && (typesCheck), "Count = "
          + count + " sb 7 " + message);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }


/**
 * getTablePrivileges() - Get a list of those created in this testcase and
 * verify all columns.
 */
    public void Var002()
    {
      StringBuffer sb = new StringBuffer();
	try {
	    String userName = dmd_.getUserName ();
	    ResultSet rs = dmd_.getTablePrivileges (null, JDDMDTest.COLLECTION, "TPRIVS%");
	    //There will be six privileges for each table meeting the above criteria
		// SELECT, INSERT, DELETE, UPDATE, ALTER, REFERENCES
	    // In May 2012, an additional INDEX privilege was added.
		if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
		{

		    boolean success = true;

		    int rows = 0;
		    while (rs.next ()) {
			++rows;
			success = check(sb, "TABLE_CAT",  rs.getString ("TABLE_CAT"),connection_.getCatalog ()) && success;
			success = success && rs.getString ("TABLE_SCHEM").equals (JDDMDTest.COLLECTION);

			success = success && (rs.getString ("TABLE_NAME").length () > 0);
			success = success && (rs.getString ("GRANTOR") == null) && rs.wasNull ();
			success = success && rs.getString ("GRANTEE").equals (userName);
		    }

		    assertCondition ((rows == 24 || rows == 28 ) && success, "Rows="+rows+" sb 24/28 success="+success+" "+sb.toString());
		}
		else
		{

		    boolean success = true;

		    int rows = 0;
		    while (rs.next ()) {
			++rows;

			success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),connection_.getCatalog ());
			success = success && rs.getString ("TABLE_SCHEM").equals (JDDMDTest.COLLECTION);

			success = success && (rs.getString ("TABLE_NAME").length () > 0);
			success = success && (rs.getString ("GRANTOR") == null) && rs.wasNull ();
			success = success && rs.getString ("GRANTEE").equals (userName);

		// It would be nice to try to force this to be
		// different values, but nobody seems to understand how
		// to do it!
			success = success && rs.getString ("PRIVILEGE").equals ("READ ADD DELETE UPDATE");

			success = success && (rs.getString ("IS_GRANTABLE") == null) && rs.wasNull ();
		    }

		    rs.close ();
		    assertCondition ((rows == 4) && success, "Rows="+rows+" sb 4 success="+success+" "+sb.toString());
		}
	}
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify all null parameters.  Verify that all tables
come back in the library list are returned.

Note:  When calling the SYSIBM procedures, this test now takes a long time
to run (about 20 minutes on V6R1).  Don't run for native driver..

**/
    public void Var003()
    {
	assertCondition(true, "Moved to JDDMDGetTablePrivileges3");
   }



/**
getTablePrivileges() - Specify null for the catalog pattern.  All matching
tables should be returned.
**/
    public void Var004()
    {
        StringBuffer sb = new StringBuffer();
        try {
            ResultSet rs = dmd_.getTablePrivileges (null,
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS%");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TPRIVSXX"))
                    check3 = true;
            }

            rs.close ();
            //@C0
	    if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE ||getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
	    {
		assertCondition (success && check1 && check2 && check3
				 && (rows == 48 || rows == 56 ),
				 "success="+success+" "+
				 JDDMDTest.COLLECTION + ".TPRIVS found="+check1+" "+
				 JDDMDTest.COLLECTION2 + ".TPRIVS3 found="+check2+" "+
				 JDDMDTest.COLLECTIONXX + ".TPRIVSXX found="+check3+" "+
				 "rows="+rows+" sb 48/56 "+ sb.toString());
	    }
	    else
	    {
		assertCondition (success && check1 && check2 && check3
				 && (rows == 8),
				 "success="+success+" "+
				 JDDMDTest.COLLECTION + ".TPRIVS found="+check1+" "+
				 JDDMDTest.COLLECTION2 + ".TPRIVS3 found="+check2+" "+
				 JDDMDTest.COLLECTIONXX + ".TPRIVSXX found="+check3+" "+
				 "rows="+rows+" sb 8 "+ sb.toString());

	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify empty string for the catalog pattern.
No matching tables should be returned.
**/
    public void Var005()
    {
        try {
            ResultSet rs = dmd_.getTablePrivileges ("",
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (isSysibmMetadata()) {
		assertCondition (rows > 0, "Empty string specified for the catalog pattern,  tables should be returned for sysibm metadata rows="+rows );

	    } else {
		assertCondition (rows == 0, "Empty string specified for the catalog pattern, no tables should be returned");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify a catalog pattern that matches the catalog
exactly.  All matching tables should be returned.
**/
    public void Var006()
    {
      StringBuffer sb = new StringBuffer();

        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS%");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TPRIVSXX"))
                    check3 = true;
            }

            rs.close ();

            //@C0
            if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
            {
            assertCondition (success && check1 && check2 && check3
                    && (rows == 48  || rows == 56 ),
			     "success="+success+" "+
			     JDDMDTest.COLLECTION + ".TPRIVS found="+check1+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS3 found="+check2+" "+
			     JDDMDTest.COLLECTIONXX + ".TPRIVSXX found="+check3+" "+
			     "rows="+rows+" sb 48/56 "+ sb.toString());

            }
            else
            {
                assertCondition (success && check1 && check2 && check3
                    && (rows == 8));
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify "localhost" for the catalog pattern.
All matching tables should be returned.
**/
    public void Var007()
    {
      StringBuffer sb = new StringBuffer();

      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4  ||
           (getDriver() == JDTestDriver.DRIVER_NATIVE  && getRelease() >= JDTestDriver.RELEASE_V5R5M0 ) ||
           (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
        notApplicable("\"localhost\" variation ");
      } else {

        try {
            ResultSet rs = dmd_.getTablePrivileges ("localhost",
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS%");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TPRIVSXX"))
                    check3 = true;
            }

            rs.close ();
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_NATIVE  )
            {
            assertCondition (success && check1 && check2 && check3
                    && (rows == 48 || rows == 56 ),
			     "success="+success+" "+
			     JDDMDTest.COLLECTION + ".TPRIVS found="+check1+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS3 found="+check2+" "+
			     JDDMDTest.COLLECTIONXX + ".TPRIVSXX found="+check3+" "+
			     "rows="+rows+" sb 48/56 "+ sb.toString());
            }
            else
            {
                assertCondition (success && check1 && check2 && check3
                    && (rows == 8));
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }




/**
getTablePrivileges() - Specify a catalog pattern for which there is a
match.  No matching tables should be returned, since we do not
support catalog pattern.
**/
    public void Var008()
    {
        try {
          String catalogSearch = connection_.getCatalog();
          if (catalogSearch != null) {
            catalogSearch = catalogSearch.substring (0, 4) + "%";
          }
            ResultSet rs = dmd_.getTablePrivileges (catalogSearch,
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS%");

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
getTablePrivileges() - Specify a catalog pattern for which there is no match.
No matching tables should be returned.
**/
    public void Var009()
    {
        try {
            ResultSet rs = dmd_.getTablePrivileges ("BOGUS%",
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS%");

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
getTablePrivileges() - Specify null for the schema pattern.  All matching
tables should be returned.
**/
    public void Var010()
    {
      StringBuffer sb = new StringBuffer();

        try {
          Connection c;
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            c = testDriver_.getConnection (baseURL_,
                userId_, encryptedPassword_);

          } else {
            c = testDriver_.getConnection (baseURL_
                + ";libraries=*LIBL," + JDDMDTest.COLLECTION + ","
                + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                userId_, encryptedPassword_);
          }
            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getTablePrivileges (c.getCatalog (),
                null, "TPRIVS%");

            // It is impossible to check that all tables come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TPRIVSXX"))
                    check3 = true;
            }

            rs.close ();
            c.close ();
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_NATIVE)
            {
            assertCondition (success && check1 && check2 && check3 && (rows >= 48),
                "\nsuccess="+success+
                " TPRIVS found="+check1+
                " TPRIVS3 found="+check2+
                " TPRIVSXX found="+check3+
                " rows = "+rows+" sb > 48");
            }
            else
            {
                assertCondition (success && check1 && check2 && check3
                    && (rows >= 8));
        }

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify empty string for the schema pattern.
No matching tables should be returned.
**/
    public void Var011()
    {
        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                "", "TPRIVS%");

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
getTablePrivileges() - Specify a schema pattern that matches a schema
exactly.  All matching tables should be returned.
**/
    public void Var012()
    {
      StringBuffer sb = new StringBuffer();

        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                JDDMDTest.COLLECTION2, "TPRIVS%");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS4"))
                    check3 = true;
            }

            rs.close ();
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
            {
            assertCondition (success && check1 && check2 && check3
                    && (rows == 18  || rows == 21 ));
            }
            else
            {
                assertCondition (success && check1 && check2 && check3
                    && (rows == 3));
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify a schema pattern using a percent for which
there is a match.  All matching tables should be returned.
**/
    public void Var013()
    {
      StringBuffer sb = new StringBuffer();

        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS%");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TPRIVSXX"))
                    check3 = true;
            }

            rs.close ();
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
            {
            assertCondition (success && check1 && check2 && check3
                    && (rows == 48  || rows == 56 ),
			     "success="+success+" "+
			     JDDMDTest.COLLECTION + ".TPRIVS found="+check1+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS3 found="+check2+" "+
			     JDDMDTest.COLLECTIONXX + ".TPRIVSXX found="+check3+" "+
			     "rows="+rows+" sb 48/56 "+ sb.toString());

            }
            else
            {
                assertCondition (success && check1 && check2 && check3
                    && (rows == 8));
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify a schema pattern using an underscore for which
there is a match.  All matching tables should be returned.
**/
    public void Var014()
    {
      StringBuffer sb = new StringBuffer();

        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                JDDMDTest.SCHEMAS_UNDERSCORE, "TPRIVS%");

            // Check for some of the tables.
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
		if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check3 = true;
            }

            rs.close ();
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE|| (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
            {
            assertCondition (success && check2 && check3
                    && (rows == 18 || rows == 21),
			     "success="+success+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS found="+check2+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS3 found="+check3+" "+
			     "rows="+rows+" sb 18/21 "+ sb.toString());
            }
            else
            {
                /* Todo:  may need to fix this because of setup change on 8/2/2010 */
                assertCondition (success && check2 && check3
                    && (rows == 3), "success="+success+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS found="+check2+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS3 found="+check3+" "+
			     "rows="+rows+" sb 3 "+ sb.toString() );
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify a schema pattern for which there is no match.
No matching tables should be returned.
**/
    public void Var015()
    {
        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                "BOGUS%", "TPRIVS%");

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
getTablePrivileges() - Specify null for the table pattern.  All matching
tables should be returned.
**/
    public void Var016()
    {
      StringBuffer sb = new StringBuffer();

        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                JDDMDTest.SCHEMAS_PERCENT, null);

            // It is impossible to check that all tables come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TPRIVSXX"))
                    check3 = true;
            }

            rs.close ();
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_NATIVE && !isSysibmMetadata() )
            {
                assertCondition (rows == 0, "null specified for table pattern, nothing should have been returned rows="+rows);
            }
            else
            {
            assertCondition (success && check1 && check2 && check3
                    && (rows >= 8));
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify empty string for the table pattern.
No matching tables should be returned.
**/
    public void Var017()
    {
        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
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
getTablePrivileges() - Specify a table pattern that matches a table
exactly.  All matching tables should be returned.
**/
    public void Var018()
    {
      StringBuffer sb = new StringBuffer();

        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS");

            // Check for the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE|| (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
            {
            assertCondition (success && check1 && check2
                    && (rows == 12 || rows == 14),
			     "success="+success+" "+
			     JDDMDTest.COLLECTION + ".TPRIVS found="+check1+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS found="+check2+" "+
			     "rows="+rows+" sb 12/14 "+ sb.toString());
            }
            else
            {
                assertCondition (success && check1 && check2
                    && (rows == 2));
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify a table pattern using a percent for which
there is a match.  All matching tables should be returned.
**/
    public void Var019()
    {

      StringBuffer sb = new StringBuffer();

        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS%");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TPRIVSXX"))
                    check3 = true;
            }

            rs.close ();
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE|| (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
            {
            assertCondition (success && check1 && check2 && check3
                    && (rows == 48  || rows == 56 ),
			     "success="+success+" "+
			     JDDMDTest.COLLECTION + ".TPRIVS found="+check1+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS found="+check2+" "+
			     JDDMDTest.COLLECTIONXX + ".TPRIVSXX found="+check3+" "+
			     "rows="+rows+" sb 48/56 "+ sb.toString());

            }
            else
            {
                assertCondition (success && check1 && check2 && check3
                    && (rows == 8));
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify a tables pattern using an underscore for which
there is a match.  All matching tables should be returned.
**/
    public void Var020()
    {
      StringBuffer sb = new StringBuffer();

        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS_");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS1"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS4"))
                    check3 = true;
            }

            rs.close ();

            if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_NATIVE|| (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
            {
            assertCondition (success && check1 && check2 && check3
                    && (rows == 24 || rows == 28 ),
			     "success="+success+" "+
			     JDDMDTest.COLLECTION + ".TPRIVS1 found="+check1+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS3 found="+check2+" "+
			     JDDMDTest.COLLECTION2 + ".TPRIVS4 found="+check3+" "+
			     "rows="+rows+" sb 24/28  "+ sb.toString());

            }
            else
            {
                assertCondition (success && check1 && check2 && check3
                    && (rows == 4));
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTablePrivileges() - Specify a table pattern for which there is no match.
No matching tables should be returned.
**/
    public void Var021()
    {
        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
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
getTablePrivileges() - Should throw an exception when the connection
is closed.
**/
    public void Var022()
    {
        try {
            ResultSet resultSet = dmd2_.getTablePrivileges (null, null, null);
            failed ("Didn't throw SQLException but got "+resultSet);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
/**
getTablePrivileges() - make sure we get all the correct privileges back from the native driver
**/
    public void Var023()
    {
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
        {
            notApplicable();
            return;
        }
        try {
            ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                JDDMDTest.COLLECTION, "TPRIVS");

            int rows = 0;
            boolean check1,check2,check3,check4,check5,check6;
            check1 = check2 = check3 = check4 = check5 = check6 =  false;

            while (rs.next ())
            {
                ++rows;
                String privilege = rs.getString("PRIVILEGE");
                if (privilege.equals("SELECT"))
                    check1 = true;
                if (privilege.equals("INSERT"))
                    check2 = true;
                if (privilege.equals("DELETE"))
                    check3 = true;
                if (privilege.equals("UPDATE"))
                    check4 = true;
                if (privilege.equals("ALTER"))
                    check5 = true;
                if (privilege.equals("REFERENCES"))
                    check6 = true;
            }

            rs.close ();
            assertCondition ((rows == 6 || rows == 7 ) && check1 && check2 && check3
                                         && check4 && check5 && check6);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }




    /**
    getTablePrivileges() - long schema- Specify a table pattern that matches a table
    exactly.
    **/
        public void Var024()
        {
            if(getRelease() < JDTestDriver.RELEASE_V7R1M0)
            {
                notApplicable("V7R1 long schema TC.");
                return;
            }

            StringBuffer sb = new StringBuffer();

            try {
		sb.append("Calling getTablePrivileges with "+connection_.getCatalog ()+","+
                    JDDMDTest.SCHEMAS_LEN128.toUpperCase()+", \"TAB%\"");

                ResultSet rs = dmd_.getTablePrivileges (connection_.getCatalog (),
                    JDDMDTest.SCHEMAS_LEN128.toUpperCase(), "TAB%");

                // Check for the tables.
                boolean check1 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                    String table = rs.getString ("TABLE_SCHEM")
                        + "." + rs.getString ("TABLE_NAME");
                    if (table.equals (JDDMDTest.SCHEMAS_LEN128.toUpperCase() + ".TABLE1"))
                        check1 = true;

                }

                rs.close ();

                assertCondition (success && check1 && (rows >= 6), "New Long Schema names testcase success="+success+" check1="+check1+" rows = "+rows+" sb >= "+6);

            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception (need v7r1 host long-schema support)");
            }
        }


        /**
        getTablePrivileges() - long schema- Specify a table pattern that matches a table
        exactly.
        Same as var024 but with toolbox non sysibm md
        **/
            public void Var025()
            {
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    notApplicable("Toolbox only testcase");
		    return;
		}
                if(getRelease() < JDTestDriver.RELEASE_V7R1M0 )
                {
                    notApplicable("V7R1 long schema (non sysibm SP) TC.");
                    return;
                }

                StringBuffer sb = new StringBuffer();

                try {
                    ResultSet rs = dmdNoSysibm_.getTablePrivileges (connection_.getCatalog (),
                        JDDMDTest.SCHEMAS_LEN128, "TAB%");

                    // Check for the tables.
                    boolean check1 = false;
                    boolean success = true;
                    int rows = 0;
                    while (rs.next ()) {
                        ++rows;

                        success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                        String table = rs.getString ("TABLE_SCHEM")
                            + "." + rs.getString ("TABLE_NAME");
                        if (table.equals (JDDMDTest.SCHEMAS_LEN128.toUpperCase() + ".TABLE1"))
                            check1 = true;

                    }

                    rs.close ();


                    assertCondition (success && check1 && (rows == 1), "success="+success+" check1="+check1+" rows="+rows+"  (need v7r1 host long-schema support)");

                }
                catch (Exception e)  {
                    failed (e, "Unexpected Exception  (need v7r1 host long-schema support)");
                }
            }



/**
getTablePrivileges() - Run getTablePrivileges multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var026()
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

		int beginningHandle = JDReflectionUtil.callMethod_I(stmt,"getStatementHandle");

		long endTime = System.currentTimeMillis() + 300000;
		int i = 0;
		int maxLoop = 1000;
		for (i = 0; i < maxLoop && System.currentTimeMillis() < endTime ; i++) {
		    // System.out.println("Calling getTablePrivileges");
		    ResultSet rs =
		      dmd_.getTablePrivileges (connection_.getCatalog (),
					       JDDMDTest.COLLECTION,
					       "TPRIVS");

		    rs.close();
		}

		if (i < maxLoop) {
		    System.out.println("Warning:  Only executed "+i+" of "+maxLoop+" loops");
		}
		Statement stmt2 = connection_.createStatement();

		int endingHandle = JDReflectionUtil.callMethod_I(stmt2,"getStatementHandle");


		for (i = 0; i < fill.length; i++) {
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


  public void Var027() {
	  checkRSMD(false);
  }
  public void Var028() {
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

	/* primed from 546CN */
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
	    {"isNullable","4","1"},
	    {"isSigned","4","false"},
	    {"getColumnDisplaySize","4","128"},
	    {"getColumnLabel","4","GRANTOR"},
	    {"getColumnName","4","GRANTOR"},
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
	    {"isCaseSensitive","5","true"},
	    {"isSearchable","5","true"},
	    {"isCurrency","5","false"},
	    {"isNullable","5","0"},
	    {"isSigned","5","false"},
	    {"getColumnDisplaySize","5","128"},
	    {"getColumnLabel","5","GRANTEE"},
	    {"getColumnName","5","GRANTEE"},
	    {"getPrecision","5","128"},
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
	    {"isNullable","6","0"},
	    {"isSigned","6","false"},
	    {"getColumnDisplaySize","6","10"},
	    {"getColumnLabel","6","PRIVILEGE"},
	    {"getColumnName","6","PRIVILEGE"},
	    {"getPrecision","6","10"},
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
	    {"isNullable","7","0"},
	    {"isSigned","7","false"},
	    {"getColumnDisplaySize","7","3"},
	    {"getColumnLabel","7","IS_GRANTABLE"},
	    {"getColumnName","7","IS_GRANTABLE"},
	    {"getPrecision","7","3"},
	    {"getScale","7","0"},
	    {"getCatalogName","7","LOCALHOST"},
	    {"getColumnType","7","12"},
	    {"getColumnTypeName","7","VARCHAR"},
	    {"isReadOnly","7","true"},
	    {"isWritable","7","false"},
	    {"isDefinitelyWritable","7","false"},
	    {"getColumnClassName","7","java.lang.String"},


	};



	String[][] fixupArray544T = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"getColumnDisplaySize","6","128"},
	    {"getPrecision","6","128"},
	    {"isNullable","7","1"},
	};

	String [][] fixup545NX = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"getColumnDisplaySize","6","128"},
	    {"getPrecision","6","128"},
	    {"isNullable","7","1"},
	    {"getColumnDisplaySize","7","128"},
	    {"getPrecision","7","128"},
	};


	String [][] fixup = {};

	String[][] fixupExtended545N = {
	    {"isSearchable","1","false"},
	    {"isNullable","1","1"},
	    {"isSearchable","2","false"},
	    {"isNullable","2","1"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"getColumnDisplaySize","6","128"},
	    {"getPrecision","6","128"},
	    {"isSearchable","7","false"},
	    {"isNullable","7","1"},
	    {"getColumnDisplaySize","7","128"},
	    {"getPrecision","7","128"},
	};

	String[][] fixupExtended546N = {
      {"isSearchable","1","false"},
      {"isSearchable","2","false"},
      {"isSearchable","3","false"},
      {"isSearchable","4","false"},
      {"isSearchable","5","false"},
      {"isSearchable","6","false"},
      {"isSearchable","7","false"},
	};


	String[][] fixupExtended736N = {
      {"isSearchable","1","false"},
      {"isSearchable","2","false"},
      {"isSearchable","3","false"},
      {"isSearchable","4","false"},
      {"isSearchable","5","false"},
      {"isSearchable","6","false"},
      {"isSearchable","7","false"},
  {"getColumnLabel","1","Table               Cat"},
      {"getColumnLabel","2","Table               Schem"},
      {"getColumnLabel","3","Table               Name"},
      {"getColumnLabel","4","Grantor"},
      {"getColumnLabel","5","Grantee"},
      {"getColumnLabel","6","Privilege"},
      {"getColumnLabel","7","Is                  Grantable"},

	};



	String[][] fixupExtended715T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"getColumnDisplaySize","6","10"},
	    {"getPrecision","6","10"},
	    {"isNullable","7","0"},
	} ; 

	String[][] fixupExtended735T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"getColumnDisplaySize","6","10"},
	    {"getPrecision","6","10"},
	    {"isNullable","7","0"},
	    {"getColumnLabel","1","Table Cat"},
	    {"getColumnLabel","2","Table Schem"},
	    {"getColumnLabel","3","Table Name"},
	    {"getColumnLabel","4","Grantor"},
	    {"getColumnLabel","5","Grantee"},
	    {"getColumnLabel","6","Privilege"},
	    {"getColumnLabel","7","Is Grantable"},
	} ; 



	Object[][] fixupArrayExtended = {
	    {"543T", fixupArray544T},
	    {"544T", fixupArray544T},
	    {"545T", fixupArray544T},
	    {"546T", fixupArray544T},








	    { "714T", fixupExtended715T},
	    { "715T", fixupExtended715T},
	    { "716T", fixupExtended715T},
	    { "717T", fixupExtended715T},
	    { "718T", fixupExtended715T},
	    { "719T", fixupExtended715T},

	    { "726T", fixupExtended715T},
	    { "727T", fixupExtended715T},

	    { "736T", fixupExtended735T},
	    { "737T", fixupExtended735T},
	    { "738T", fixupExtended735T},
	    { "739T", fixupExtended735T},



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
	    {"726N", fixupExtended546N},
	    {"727N", fixupExtended546N},

	    {"736N", fixupExtended736N},
	    {"737N", fixupExtended736N},
	    {"738N", fixupExtended736N},
	    {"739N", fixupExtended736N},


	};





	Object[][] fixupArray = {
	    { "543TX", fixupArray544T},
	    { "544TX", fixupArray544T},
	    { "545TX", fixupArray544T},
	    { "546TX", fixupArray544T},

	    { "614TX", fixupArray544T},
	    { "615TX", fixupArray544T},
	    { "616TX", fixupArray544T},
	    { "617TX", fixupArray544T},
	    { "618TX", fixupArray544T},


	    { "714TX", fixupArray544T},
	    { "715TX", fixupArray544T},
	    { "716TX", fixupArray544T},
	    { "717TX", fixupArray544T},
	    { "718TX", fixupArray544T},
	    { "719TX", fixupArray544T},

	    { "724TX", fixupArray544T},
	    { "725TX", fixupArray544T},
	    { "726TX", fixupArray544T},
	    { "727TX", fixupArray544T},

	    { "544NX", fixup545NX},
	    { "545NX", fixup545NX},

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
		    rsA[j] = dmd.getTablePrivileges (connection_.getCatalog (),
            JDDMDTest.COLLECTION, "TPRIVS");

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
getTablePrivileges() - Use readonly connection 
tables should be returned.
**/
    public void Var029()
    {
        StringBuffer sb = new StringBuffer();
        try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            ResultSet rs = dmd.getTablePrivileges (null,
                JDDMDTest.SCHEMAS_PERCENT, "TPRIVS%");

            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals (JDDMDTest.COLLECTIONXX + ".TPRIVSXX"))
                    check3 = true;
            }

            rs.close ();
	    c.close(); 
            //@C0
	    if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE ||getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
	    {
		assertCondition (success && check1 && check2 && check3
				 && (rows == 48 || rows == 56 ),
				 "success="+success+" "+
				 JDDMDTest.COLLECTION + ".TPRIVS found="+check1+" "+
				 JDDMDTest.COLLECTION2 + ".TPRIVS3 found="+check2+" "+
				 JDDMDTest.COLLECTIONXX + ".TPRIVSXX found="+check3+" "+
				 "rows="+rows+" sb 48/56 "+ sb.toString());
	    }
	    else
	    {
		assertCondition (success && check1 && check2 && check3
				 && (rows == 8),
				 "success="+success+" "+
				 JDDMDTest.COLLECTION + ".TPRIVS found="+check1+" "+
				 JDDMDTest.COLLECTION2 + ".TPRIVS3 found="+check2+" "+
				 JDDMDTest.COLLECTIONXX + ".TPRIVSXX found="+check3+" "+
				 "rows="+rows+" sb 8 "+ sb.toString());

	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }




}





