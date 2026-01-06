///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetColumnPrivileges.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDDMDGetColumnPrivileges.java
 //
 // Classes:      JDDMDGetColumnPrivileges
 //
 ////////////////////////////////////////////////////////////////////////
 //
 // Differences between SYSIBM and legacy
 // 1.  SYSIBM will return rows for empty catalog string Var005
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.DMD;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDDMDGetColumnPrivileges.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getColumnPrivileges()
</ul>
**/
public class JDDMDGetColumnPrivileges
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetColumnPrivileges";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }



    // Private data.
    private Connection          connectionTBROI_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_, dmdTBROI_;
    private DatabaseMetaData    dmd2_;

    StringBuffer message = new StringBuffer();


/**
Constructor.
**/
    public JDDMDGetColumnPrivileges (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetColumnPrivileges",
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
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    connectionTBROI_ = testDriver_.getConnection (baseURL_ , userId_, encryptedPassword_);
	} else {
	    connectionTBROI_ = testDriver_.getConnection (baseURL_ + ";metadata source=0", userId_, encryptedPassword_);
	}
        dmd_ = connection_.getMetaData ();
        dmdTBROI_ = connectionTBROI_.getMetaData ();

        Statement s = connection_.createStatement ();
	setupPrivileges(s, getDriver(), getRelease(), output_); 
        s.close ();

        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        dmd2_ = closedConnection_.getMetaData ();
        closedConnection_.close ();

    }

    public static void setupPrivileges(Statement s, int driver, int release, java.io.PrintWriter output_) throws SQLException {


	String[] dropTables = {
	    JDDMDTest.COLLECTION +  ".CPRIVS",
	    JDDMDTest.COLLECTION +  ".CPRIVS1",
	    JDDMDTest.COLLECTION +  ".CPRIVS2",
	    JDDMDTest.COLLECTION2 + ".CPRIVS",
	    JDDMDTest.COLLECTION  + ".LCNPRIVS",
	};

	for (int i = 0; i < dropTables.length; i++) {
	    try {
		s.executeUpdate("DROP TABLE "+dropTables[i]);
	    } catch (SQLException e) {
		int errorCode = e.getErrorCode();
		if (errorCode == -204) {
		} else {
		    output_.println("Unexpected Exception errorCode="+errorCode);
		    e.printStackTrace();
		}
	    }
	}


        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".CPRIVS (COLUMN INT NOT NULL, COLUMN1 TIMESTAMP, "
            + "COLUMN2 CHAR(10) NOT NULL, "
            + "COLUMNXX DECIMAL(6,2))");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".CPRIVS1 (COLUMN1 INT, COLUMN2 CHAR(10))");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".CPRIVS2 (COLUMN1 INT, COLUMNXX CHAR(10))");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
            + ".CPRIVS (COLUMN2 DECIMAL, COLUMNXX CHAR(10))");

        //@E1A
	//@E2C.  When trying to get this to work, it would fail because the SQLCOLPRIVILEGES
        //       view does not work when the table contains only 1 column.  DB Issue 27810
        //
        if((!JDTestDriver.isLUW())  && true){
            s.executeUpdate ("CREATE TABLE "  + JDDMDTest.COLLECTION                         //@E1A
                + ".LCNPRIVS (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INTEGER, C2 INTEGER)");  //@E1A
            s.executeUpdate("GRANT UPDATE(THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST) ON " + JDDMDTest.COLLECTION + ".LCNPRIVS TO QUSER");  /*@E2A*/

        }


        if ((driver == JDTestDriver.DRIVER_NATIVE) ||
            (driver == JDTestDriver.DRIVER_JCC) ||
            (driver == JDTestDriver.DRIVER_TOOLBOX) ||
	    (driver == JDTestDriver.DRIVER_JTOPENLITE) ) //@C0  
        {
            s.executeUpdate("GRANT UPDATE(COLUMN1) ON " + JDDMDTest.COLLECTION + ".CPRIVS TO QUSER");  /*@D1C*/

        }


    }

    public static void cleanupPrivileges(Statement s, int driver, int release ) throws SQLException {
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".CPRIVS");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".CPRIVS1");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".CPRIVS2");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
            + ".CPRIVS");
        //@E1A
        if((!JDTestDriver.isLUW()) && true){
            s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION       //@E1A
                + ".LCNPRIVS");                                         //@E1A
        }

    }
/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        Statement s = connection_.createStatement ();

	cleanupPrivileges(s, getDriver(), getRelease());

        s.close ();
        connection_.close ();
        connection_ = null; 

        connectionTBROI_.close();
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
getColumnPrivileges() - Check the result set format.
**/
    public void Var001()
    {
        message.setLength(0);

        try {
            ResultSet rs;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              // jcc does not permit null for table or column name
              rs = dmd_.getColumnPrivileges (null, null, "SYSIBM", "%");

            } else {
            rs = dmd_.getColumnPrivileges (null, null, null, null);
            }
            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "TABLE_CAT", "TABLE_SCHEM",
                                       "TABLE_NAME", "COLUMN_NAME",
                                       "GRANTOR", "GRANTEE",
                                       "PRIVILEGE", "IS_GRANTABLE" };
            int[] expectedTypes = { Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.VARCHAR };

            int count = rsmd.getColumnCount ();
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);

            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);

            rs.close ();
            assertCondition ((count == 8) && (namesCheck) && (typesCheck), message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Get a list of those created in this testcase and
verify all columns.
**/
    public void Var002()
    {
        StringBuffer sb =new StringBuffer();
        boolean found = false;
        try {
            String userName = dmd_.getUserName ();
            ResultSet rs;
            // 04/26/2006 changed last parameter to '%' instead of null to match JDBC spec
	    output_.println("Calling getColumnPrivileges(null,"+ JDDMDTest.COLLECTION+", CPRIVS, %)");
            rs = dmd_.getColumnPrivileges (null, JDDMDTest.COLLECTION, "CPRIVS", "%");
            boolean success = true;

            int rows = 0;
            //@C0
            //The result set created by the native driver will only have rows representing the priviliges
            //that have been granted.
            if (getDriver()==JDTestDriver.DRIVER_JTOPENLITE ||getDriver()==JDTestDriver.DRIVER_JCC || getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) 
            {
		int expectedRows = 1;
		int expectedRowsBig = 17;

                while (rs.next ()) {
                    ++rows;
                    success =  check(sb, "TABLECAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success;

                    String schemaName   = rs.getString ("TABLE_SCHEM");
                    String tableName    = rs.getString ("TABLE_NAME");
                    String columnName   = rs.getString ("COLUMN_NAME");

                    String grantor              = rs.getString ("GRANTOR");
                    boolean grantorWasNull      = rs.wasNull ();
                    String grantee              = rs.getString ("GRANTEE");
                    String privilege            = rs.getString ("PRIVILEGE");
                    String isGrantable          = rs.getString ("IS_GRANTABLE");
                    if ("QUSER".equals(grantee)) {
                        success = check(sb, "TABLE_SCHEM", schemaName, (JDDMDTest.COLLECTION)) && success;
                        success = check(sb, "TABLE_NAME",  tableName,  "CPRIVS") && success ;
                        success = check(sb, "COLUMN_NAME", columnName, "COLUMN1");
                        if (! JDTestDriver.isLUW()) {
                            success = success && (grantor == null) && grantorWasNull;
                        }
                        success = success && grantee.equals ("QUSER");  /*@D1C*/
                        success = success && privilege.equals ("UPDATE");
                        success = success && (isGrantable.equals("NO"));
                        found = true;
                    }

                    sb.append(schemaName+"."+tableName+":"+columnName+" "+grantor + ":" + grantorWasNull +
                            ":" + grantee + ":"
                            + privilege + ":" + isGrantable + ":\n");

                }

                rs.close ();
                assertCondition (((rows == expectedRows) || (rows == expectedRowsBig)) && success && found, "rows = "+rows+" sb "+expectedRows+" or "+expectedRowsBig+" success="+success+" found="+found+" message="+sb.toString());
            }
            else
            {
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean check4 = false;
                while (rs.next ()) {
                    ++rows;
                    success = check(sb, "TABLE_CAT",  rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                    String schemaName   = rs.getString ("TABLE_SCHEM");
                    String tableName    = rs.getString ("TABLE_NAME");
                    String columnName   = rs.getString ("COLUMN_NAME");

                    sb.append("ROW: "+schemaName+" "+tableName+" "+columnName+"\n") ;

                    // output_.println (schemaName + ":" + tableName + ":" + columnName + ":");

                    success = success && schemaName.equals (JDDMDTest.COLLECTION);
                    success = success && tableName.equals ("CPRIVS");

                    if (columnName.equals ("COLUMN"))
                        check1 = true;
                    if (columnName.equals ("COLUMN1"))
                        check2 = true;
                    if (columnName.equals ("COLUMN2"))
                        check3 = true;
                    if (columnName.equals ("COLUMNXX"))
                        check4 = true;

                    String grantor              = rs.getString ("GRANTOR");
                    boolean grantorWasNull      = rs.wasNull ();
                    String grantee              = rs.getString ("GRANTEE");
                    String privilege            = rs.getString ("PRIVILEGE");
                    String isGrantable          = rs.getString ("IS_GRANTABLE");
                    boolean isGrantableWasNull  = rs.wasNull ();

                    //  output_.println (grantor + ":" + grantorWasNull + ":" + grantee + ":"
                    //                    + privilege + ":" + isGrantable + ":" + isGrantableWasNull + ":");

                    success = success && (grantor == null) && grantorWasNull;
                    success = success && grantee.equals (userName);

                    // It would be nice to try to force this to be
                    // different values, but nobody seems to understand how
                    // to do it!
                    success = success && privilege.equals ("");

                    success = success && (isGrantable == null) && isGrantableWasNull;
                }

                rs.close ();

                // output_.println ("Rows = " + rows);

                assertCondition ((rows == 4) && check1 && check2 && check3 && check4 && success,
                        "rows="+rows+"sb 4 message:"+sb.toString());
            }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify all null parameters.  No rows
should be returned.
**/
    public void Var003()
    {
        try {
            ResultSet rs ;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              // jcc does not permit null for table or column name
              rs = dmd_.getColumnPrivileges (null, null, "XXXX2323", "%");

            } else {

              rs = dmd_.getColumnPrivileges (null, null, null, null);
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
getColumnPrivileges() - Specify null for the catalog.  All matching
columns should be returned.

**/
    public void Var004()
    {
	boolean found = false;
        StringBuffer sb = new StringBuffer();
        try {
            ResultSet rs ;
            // 04/26/2006 updated testcase to pass "%" as last parameter
            rs = dmd_.getColumnPrivileges (null, JDDMDTest.COLLECTION,
                  "CPRIVS", "%");
            boolean success = true;
            int rows = 0;
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_JCC ||
                getDriver () == JDTestDriver.DRIVER_NATIVE ||
		getDriver () == JDTestDriver.DRIVER_JTOPENLITE ||
               (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) 
            {
                while (rs.next ()) {
                    ++rows;
                    success = check(sb, "TABLE_CAT",  rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                    String schemaName   = rs.getString ("TABLE_SCHEM");
                    String tableName    = rs.getString ("TABLE_NAME");
                    String columnName   = rs.getString ("COLUMN_NAME");

                    String grantor              = rs.getString ("GRANTOR");
                    boolean grantorWasNull      = rs.wasNull ();
                    String grantee              = rs.getString ("GRANTEE");
                    String privilege            = rs.getString ("PRIVILEGE");
                    String isGrantable          = rs.getString ("IS_GRANTABLE");


		    if ("QUSER".equals(grantee)) {
			success = success && schemaName.equals (JDDMDTest.COLLECTION);
			success = success && tableName.equals ("CPRIVS");
			success = success && columnName.equals("COLUMN1");

			sb.append(schemaName + ":" + tableName + ":" + columnName + ":" +
			          grantor + ":" + grantorWasNull + ":" + grantee + ":" +
		                  privilege + ":" + isGrantable + ":\n");
			if (!JDTestDriver.isLUW()) {
			    success = success && (grantor == null) && grantorWasNull;
			}
			success = success && grantee.equals ("QUSER"); /*@D1C*/
			success = success && privilege.equals ("UPDATE");
			success = success && (isGrantable.equals("NO"));
			found = true;
		    }

                }

                rs.close ();
                assertCondition (((rows == 1) || (rows==17)) && success && found, "rows="+rows+" sb 1 or 17  success="+success+" found="+found+" "+sb.toString());
            }
            else
            {
            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT",  rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";

                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMNXX)"))
                    check3 = true;
            }

            rs.close ();

            // output_.println ("rows = " + rows + ":" + success + ":" + check1 + ":" + check2 + ":" + check3 + ":");

            assertCondition (success && check1 && check2 && check3 && (rows == 4),
                "\nsuccess="+success+" check1="+check1+" check2="+check2+" check3="+check3+" rows="+rows+" sb 4");
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify empty string for the catalog.
No matching columns should be returned.
**/
    public void Var005()
    {
        try {
            ResultSet rs = dmd_.getColumnPrivileges ("", JDDMDTest.COLLECTION,
                                                "CPRIVS", null);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if ( isSysibmMetadata()) {
		assertCondition (rows > 0 , rows + " rows returned for empty catalog string");
	    } else {
		assertCondition (rows == 0, rows + " rows returned for empty catalog string");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify a catalog that matches the catalog
exactly.  All matching columns should be returned.

**/
    public void Var006()
    {
        try {
            StringBuffer sb = new StringBuffer();
            ResultSet rs ;
            // Changed 04/26/2006 to have last parameter as "%" instead of null
	    rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
					   JDDMDTest.COLLECTION,
					   "CPRIVS1", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;


                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";

                // output_.println (column + ":");
                sb.append(column+"\n") ;
                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS1(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS1(COLUMN2)"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            //no privileges were granted to the CPRIVS1 table so we do not expect any rows
            //in the result set.
            if (getDriver () == JDTestDriver.DRIVER_NATIVE ||
		getDriver () == JDTestDriver.DRIVER_JTOPENLITE ||
		(getDriver() == JDTestDriver.DRIVER_JCC) || (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) 
            {
                assertCondition (rows == 0 || (rows == 8 && check1 && check2),
                    "rows = "+rows+" sb 0/8 \n"+sb.toString());
            }
            else
            {
            assertCondition (success && check1 && check2 && (rows == 2),
                "success="+success+" check1="+check1+
                    " check2="+check2+" rows="+rows+
                    " sb 2  message="+sb.toString());
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify "localhost" for the catalog.
All matching columns should be returned.

**/
    public void Var007()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4 ) {
        notApplicable("COMPATIBILITY \"localhost\" variation ");
      } else {
        StringBuffer sb = new StringBuffer();
        try {

            ResultSet rs;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              rs = dmd_.getColumnPrivileges ("localhost",
                  JDDMDTest.COLLECTION,
                  "CPRIVS2", "%");
            } else {


            rs = dmd_.getColumnPrivileges ("localhost",
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS2", null);
            }

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                sb.append(column+"\n");
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS2(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS2(COLUMNXX)"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            //no privileges were granted to the CPRIVS2 table so we do not expect any rows
            //in the result set.
            if (getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) 
            {
                assertCondition (rows == 0 || (rows == 8 && check1 & check1), "rows = "+rows+" sb 0/8\n"+sb.toString());
            }
            else
            {
            assertCondition (success && check1 && check2
                    && (rows == 2), sb.toString());
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
getColumnPrivileges() - Specify a catalog pattern for which there is a
match.  No matching columns should be returned, since we do not
support catalog pattern.
**/
    public void Var008()
    {
        try {
            ResultSet rs;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              rs = dmd_.getColumnPrivileges (system_.toUpperCase().substring (0, 4) + "%"
                  , JDDMDTest.COLLECTION,
                  "CPRIVS", null);

            } else {
            rs = dmd_.getColumnPrivileges (connection_.getCatalog ().substring (0, 4) + "%"
                                                     , JDDMDTest.COLLECTION,
                                                     "CPRIVS", null);
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
getColumnPrivileges() - Specify a catalog for which there is no match.
No matching columns should be returned.
**/
    public void Var009()
    {
        try {
            ResultSet rs = dmd_.getColumnPrivileges ("BOGUS%",
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", null);

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
   * getColumnPrivileges() - Specify null for the schema. No rows should be
   * returned.
   */
  public void Var010() {
    try {
      ResultSet rs;
      boolean column1found = false;
      if ( getDriver () == JDTestDriver.DRIVER_JTOPENLITE ||
	   getJdbcLevel() >= 4  || (getDriver() == JDTestDriver.DRIVER_JCC)
              ||  ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && isSysibmMetadata()))  {

        rs = dmd_.getColumnPrivileges(connection_.getCatalog(), null,
          "CPRIVS", "%");
      } else {
        rs = dmd_.getColumnPrivileges(connection_.getCatalog(), null,
            "CPRIVS", null);

      }
      // @C0
      // when null is passed in for the schema we do not use schema name
      // to narrow the search
      // so we want one row to be returned for COLUMN1
      if ((getDriver() == JDTestDriver.DRIVER_JTOPENLITE) ||(getDriver() == JDTestDriver.DRIVER_NATIVE) || (getDriver() == JDTestDriver.DRIVER_JCC )
              ||  ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && isSysibmMetadata()) ) {
	  boolean rowAvailable = false;
        boolean check = rs.next();
	rowAvailable = check;
	StringBuffer sb = new StringBuffer();
	int count = 0;
        String columnName="NOT RETREIVED";
        while (check) {
          columnName = rs.getString("COLUMN_NAME");
	  if (columnName.equals("COLUMN1")) {
	      column1found = true;
	  }
	  count++;
	  sb.append("ROW "+count+" "+rs.getString("COLUMN_NAME")+"\n");

	  check = rs.next();
        }

        assertCondition(rowAvailable && column1found,
            "row available="+rowAvailable+" column1found="+column1found+" sb COLUMN1 columns are \n"+sb.toString());
        rs.close();
      } else {
        int rows = 0;
        while (rs.next())
          ++rows;

        rs.close();
        assertCondition(rows == 0, "Specified null for schema  ");
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }



/**
 * getColumnPrivileges() - Specify empty string for the schema. No matching
 * columns should be returned.
 */
    public void Var011()
    {
        try {
            ResultSet rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     "",
                                                     "CPRIVS", null);
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
getColumnPrivileges() - Specify a schema that matches a schema
exactly.  All matching columns should be returned.

**/
    public void Var012()
    {
        StringBuffer sb = new StringBuffer();
        try {
          ResultSet rs;

          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                JDDMDTest.COLLECTION2,
                "CPRIVS", "%");
          } else {
            rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION2,
                                                     "CPRIVS", null);
          }
            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

		success = check(sb, "TABLE_CAT",rs.getString ("TABLE_CAT"),(connection_.getCatalog ())) && success;

		String column = rs.getString ("TABLE_SCHEM")
		  + "." + rs.getString ("TABLE_NAME") + "("
		  + rs.getString ("COLUMN_NAME") + ")";

		String schemaName   = rs.getString ("TABLE_SCHEM");
		String tableName    = rs.getString ("TABLE_NAME");
		String columnName   = rs.getString ("COLUMN_NAME");

		String grantor              = rs.getString ("GRANTOR");
		boolean grantorWasNull      = rs.wasNull ();
		String grantee              = rs.getString ("GRANTEE");
		String privilege            = rs.getString ("PRIVILEGE");
		String isGrantable          = rs.getString ("IS_GRANTABLE");


		sb.append(schemaName + ":" + tableName + ":" + columnName + ":" +
			          grantor + ":" + grantorWasNull + ":" + grantee + ":" +
		                  privilege + ":" + isGrantable + ":\n");

                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION2 + ".CPRIVS(COLUMN2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".CPRIVS(COLUMNXX)"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            if (		getDriver () == JDTestDriver.DRIVER_JTOPENLITE ||  getDriver() == JDTestDriver.DRIVER_NATIVE || (getDriver() == JDTestDriver.DRIVER_JCC) || (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()))
            {
		assertCondition (rows ==0 || ((rows == 8) && check1 && check2), "rows = "+rows+" sb 0 or 7 \n"+ sb.toString() );
            }
            else
            {
            assertCondition (success && check1 && check2
                    && (rows == 2), sb.toString());
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify a schema pattern using a percent for which
there is a match.
**/
    public void Var013()
    {
        try {
          ResultSet rs;
          if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC) ) {
            rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                JDDMDTest.SCHEMAS_PERCENT,
                "CPRIVS", "%");

          } else {
            rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.SCHEMAS_PERCENT,
                                                     "CPRIVS", null);
          }
            //@C0
    
            //No rows should be returned, since we don't support
            //schema pattern here.
            // JDBC says schema is not a pattern and must match exactly.
           
            {
            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify null for the table.  No rows
should be returned.
**/
    public void Var014()
    {
        try {
            ResultSet rs ;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                  JDDMDTest.COLLECTION,
                  "%X", "%");
            } else {
            rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     null, null);
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
getColumnPrivileges() - Specify empty string for the table.
No matching columns should be returned.
**/
    public void Var015()
    {
        try {
            ResultSet rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "", null);

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
getColumnPrivileges() - Specify a table that matches a table
exactly.  All matching columns should be returned.

**/
    public void Var016()
    {
        StringBuffer sb = new StringBuffer();
        try {
            ResultSet rs;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                  JDDMDTest.COLLECTION,
                  "CPRIVS1", "%");
            } else {
            rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS1", null);
            }
            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";

                sb.append(column+"\n");
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS1(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS1(COLUMN2)"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            if (		getDriver () == JDTestDriver.DRIVER_JTOPENLITE || (getDriver () == JDTestDriver.DRIVER_NATIVE) ||(getDriver () == JDTestDriver.DRIVER_JCC) || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) 
            {
		assertCondition(rows == 0 || (rows==8 && check1 && check2 ) , "rows="+rows+"sb 0 or 8 \n"+sb.toString() );
            }
            else
            {
            assertCondition (success && check1 && check2
                    && (rows == 2), sb.toString());
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify a table pattern using a percent for which
there is a match.
**/
    public void Var017() {
    try {
      ResultSet rs;
      if (getJdbcLevel() >= 4) {
        rs = dmd_.getColumnPrivileges(connection_.getCatalog(),
            JDDMDTest.COLLECTION, "CPRIV%", "%");

      } else {
      rs = dmd_.getColumnPrivileges(connection_.getCatalog(),
          JDDMDTest.COLLECTION, "CPRIV%", null);
      }

      // @C0
     
      // No rows should be returned, since we don't support
      // schema pattern here.
      // JDBC doesn't support table pattern.
      {
        int rows = 0;
        while (rs.next())
          ++rows;

        rs.close();
        assertCondition(rows == 0);
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }



/**
 * getColumnPrivileges() - Specify a table pattern for which there is no match.
 * No matching columns should be returned.
 */
    public void Var018()
    {
        try {
            ResultSet rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "BOGUS%", null);

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
getColumnPrivileges() - Specify null for the column pattern.  All matching
columns should be returned.

**/
    public void Var019()
    {
        StringBuffer sb = new StringBuffer("Rows=\n") ;
        try {
            ResultSet rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS2", null);

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";

                sb.append(column+"\n") ;
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS2(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS2(COLUMNXX)"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            if (		getDriver () == JDTestDriver.DRIVER_JTOPENLITE || (getDriver () == JDTestDriver.DRIVER_NATIVE) || (getDriver () == JDTestDriver.DRIVER_JCC) || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) 
            {
                assertCondition(rows == 0 || (rows == 8 && check1 && check2 ) ,
                    "rows="+rows+" sb 0/8  check1="+check1+" check2="+check2+" \n"+sb.toString());
            }
            else
            {
            assertCondition (success && check1 && check2
                    && (rows == 2), sb.toString() );
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify empty string for the column pattern.
No matching columns should be returned.
**/
    public void Var020()
    {
        try {
            ResultSet rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", "");

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
getColumnPrivileges() - Specify a column pattern that matches a column
exactly.  All matching columns should be returned.

**/
    public void Var021()
    {
      StringBuffer sb  = new StringBuffer("ROWS:\n") ;
        try {
            ResultSet rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", "COLUMN1");

            // Check for some of the columns.
            boolean check1 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT",rs.getString ("TABLE_CAT"),(connection_.getCatalog ())) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                sb.append(column+"\n") ;
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN1)"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (success && check1
                    && ((rows == 1) || (rows == 5)), "success="+success+" check1="+check1+" rows="+rows+" sb 1/5\n" + sb.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify a column pattern using a percent for which
there is a match.  All matching columns should be returned.

**/
    public void Var022()
    {
        try {
            StringBuffer sb = new StringBuffer("ROWS=\n");
            ResultSet rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", "COL%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean check4 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),  (connection_.getCatalog ())) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                sb.append(column+"\n") ;
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN2)"))
                    check3 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMNXX)"))
                    check4 = true;
            }

            // output_.println ("rows = " + rows);

            rs.close ();

            //@C0
            if (		getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_JCC || getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata()) ) 
            {
                assertCondition (success && ((check2 && (rows == 1)) ||
                    (check1 && check2 && check3 && check4
                    && (rows == 17))), "rows = "+rows+" sb 1/8 \n"+sb.toString());
            }
            else
            {
            assertCondition (success && check1 && check2 && check3 && check4
                    && (rows == 4), sb.toString() );
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify a table pattern using an underscore for which
there is a match.  All matching columns should be returned.

**/
    public void Var023()
    {
        StringBuffer sb = new StringBuffer ("ROWS:\n") ;
        try {
            ResultSet rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", "COLUMN_");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), (connection_.getCatalog ())) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                sb.append(column+"\n");
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN2)"))
                    check2 = true;
            }

            // output_.println ("rows = " + rows);

            rs.close ();
            if (		getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_JCC ||  getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) 
            {
                assertCondition ((success && check1 && (rows == 1)) ||
                                  (success && check1 && check2
                                      && (rows == 9)),sb.toString() );
            }
            else
            {
            assertCondition (success && check1 && check2
                    && (rows == 2), sb.toString());
            }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnPrivileges() - Specify a column pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var024()
    {
        try {
            ResultSet rs = dmd_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", "BOGUS%");

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
getColumnPrivileges() - Should throw an exception when the connection
is closed.
**/
    public void Var025()
    {
        try {
            ResultSet rs = dmd2_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", null);
            failed ("Didn't throw SQLException but got "+rs);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
//@E1A
/**
getColumnPrivileges() - Specify null for the catalog.  All matching
columns should be returned.

**/
    public void Var026()
    {
      StringBuffer sb = new StringBuffer();
        if((!JDTestDriver.isLUW()) ){
            try {
                String pattern = null;
                //
                // The JDBC spec says nothing about a null pattern.. For the native and JCC drivers,
                // use a '%'
                //
                int x = getDriver();
                if ((x == JDTestDriver.DRIVER_JCC) || (x == JDTestDriver.DRIVER_NATIVE)) {
                  pattern="%";
                }
                ResultSet rs = dmd_.getColumnPrivileges (null, JDDMDTest.COLLECTION,
                                                         "LCNPRIVS", pattern);
                boolean success = false;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

		    if (rs.getString("COLUMN_NAME").equals("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST")) {
                        success = true;
		    } else {
			sb.append("RETREIVED \""+rs.getString("COLUMN_NAME")+"\"\n");
		    }

		    sb.append("Retrieved "+rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6)+","+rs.getString(7)+","+rs.getString(8)+"\n");
                }

                rs.close ();


                if (		getDriver () == JDTestDriver.DRIVER_JTOPENLITE || getDriver () == JDTestDriver.DRIVER_JCC ||  getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) 
                {
                    assertCondition (success && (rows==1 || rows == 9),
                        "Added by Toolbox 8/11/2004 for 128 byte column names rows = "+rows+" sb 1/9,  success ="+success+" "+sb.toString() );
                }
                else
                {
                    assertCondition (success && rows==2, "Added by Toolbox 8/11/2004 for 128 byte column names rows = "+rows+" sb 2,  success ="+success+" "+sb.toString());
                }
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception, Added by Toolbox 8/11/2004 for 128 byte column names.");
            }
	} else {
	    notApplicable();
	}
    }


    /**
    Toolbox var only with "metadata source"=0 (ROI flow) property same as var001
    getColumnPrivileges() - Check the result set format.
    **/
    public void Var027()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        message.setLength(0);
        try {
            ResultSet rs;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              // jcc does not permit null for table or column name
              rs = dmdTBROI_.getColumnPrivileges (null, null, "SYSIBM", "%");

            } else {
            rs = dmdTBROI_.getColumnPrivileges (null, null, null, null);
            }
            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "TABLE_CAT", "TABLE_SCHEM",
                                       "TABLE_NAME", "COLUMN_NAME",
                                       "GRANTOR", "GRANTEE",
                                       "PRIVILEGE", "IS_GRANTABLE" };
            int[] expectedTypes = { Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.VARCHAR };

            int count = rsmd.getColumnCount ();
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);

            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);

            rs.close ();
            assertCondition ((count == 8) && (namesCheck) && (typesCheck), message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 Toolbox var only with "metadata source"=0 (ROI flow) property same as var002
getColumnPrivileges() - Get a list of those created in this testcase and
verify all columns.
**/
    public void Var028()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        StringBuffer sb =new StringBuffer();
        try {
            String userName = dmdTBROI_.getUserName ();
            ResultSet rs;
            // 04/26/2006 changed last parameter to '%' instead of null to match JDBC spec
            rs = dmdTBROI_.getColumnPrivileges (null, JDDMDTest.COLLECTION, "CPRIVS", "%");
            boolean success = true;

            int rows = 0;
            //@C0
            //The result set created by the native driver will only have rows representing the priviliges
            //that have been granted.
            if (getDriver () == JDTestDriver.DRIVER_NATIVE  )
            {
                while (rs.next ()) {
                    ++rows;
                    success =  check(sb, "TABLECAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success;

                    String schemaName   = rs.getString ("TABLE_SCHEM");
                    String tableName    = rs.getString ("TABLE_NAME");
                    String columnName   = rs.getString ("COLUMN_NAME");
                    sb.append("ROW: "+schemaName+" "+tableName+" "+columnName+"\n");
                    //output_.println (schemaName + ":" + tableName + ":" + columnName + ":");

                    success = check(sb, "TABLE_SCHEM", schemaName, (JDDMDTest.COLLECTION)) && success;
                    success = check(sb, "TABLE_NAME",  tableName,  "CPRIVS") && success ;
                    success = check(sb, "COLUMN_NAME", columnName, "COLUMN1");

                    String grantor              = rs.getString ("GRANTOR");
                    boolean grantorWasNull      = rs.wasNull ();
                    String grantee              = rs.getString ("GRANTEE");
                    String privilege            = rs.getString ("PRIVILEGE");
                    String isGrantable          = rs.getString ("IS_GRANTABLE");

                    //output_.println (grantor + ":" + grantorWasNull + ":" + grantee + ":"
                    //                + privilege + ":" + isGrantable + ":");

                    success = success && (grantor == null) && grantorWasNull;
                    success = success && grantee.equals ("QUSER");  /*@D1C*/
                    success = success && privilege.equals ("UPDATE");
                    success = success && (isGrantable.equals("NO"));
                }

                rs.close ();
                assertCondition ((rows == 1) && success, "rows = "+rows+" success="+success+" message="+sb.toString());
            }
            else
            {
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean check4 = false;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT",  rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String schemaName   = rs.getString ("TABLE_SCHEM");
                String tableName    = rs.getString ("TABLE_NAME");
                String columnName   = rs.getString ("COLUMN_NAME");

                sb.append("ROW: "+schemaName+" "+tableName+" "+columnName+"\n") ;

                // output_.println (schemaName + ":" + tableName + ":" + columnName + ":");

                success = success && schemaName.equals (JDDMDTest.COLLECTION);
                success = success && tableName.equals ("CPRIVS");

                if (columnName.equals ("COLUMN"))
                    check1 = true;
                if (columnName.equals ("COLUMN1"))
                    check2 = true;
                if (columnName.equals ("COLUMN2"))
                    check3 = true;
                if (columnName.equals ("COLUMNXX"))
                    check4 = true;

                String grantor              = rs.getString ("GRANTOR");
                boolean grantorWasNull      = rs.wasNull ();
                String grantee              = rs.getString ("GRANTEE");
                String privilege            = rs.getString ("PRIVILEGE");
                String isGrantable          = rs.getString ("IS_GRANTABLE");
                boolean isGrantableWasNull  = rs.wasNull ();

                //  output_.println (grantor + ":" + grantorWasNull + ":" + grantee + ":"
                //                    + privilege + ":" + isGrantable + ":" + isGrantableWasNull + ":");

                success = success && (grantor == null) && grantorWasNull;
                success = success && grantee.equals (userName);

                // It would be nice to try to force this to be
                // different values, but nobody seems to understand how
                // to do it!
                success = success && privilege.equals ("");

                success = success && (isGrantable == null) && isGrantableWasNull;
            }

            rs.close ();

            // output_.println ("Rows = " + rows);

            assertCondition ((rows == 4) && check1 && check2 && check3 && check4 && success,
                "rows="+rows+"sb 4 message:"+sb.toString());
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**     Toolbox var only with "metadata source"=0 (ROI flow) property same as var003
getColumnPrivileges() - Specify all null parameters.  No rows
should be returned.
**/
    public void Var029()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
            ResultSet rs ;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              // jcc does not permit null for table or column name
              rs = dmdTBROI_.getColumnPrivileges (null, null, "XXXX2323", "%");

            } else {

              rs = dmdTBROI_.getColumnPrivileges (null, null, null, null);
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
 *     Toolbox var only with "metadata source"=0 (ROI flow) property same as var004
getColumnPrivileges() - Specify null for the catalog.  All matching
columns should be returned.

**/
    public void Var030()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }

        StringBuffer sb = new StringBuffer();
        try {
            ResultSet rs ;
            // 04/26/2006 updated testcase to pass "%" as last parameter
            rs = dmdTBROI_.getColumnPrivileges (null, JDDMDTest.COLLECTION,
                  "CPRIVS", "%");
            boolean success = true;
            int rows = 0;
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_NATIVE)
            {
                while (rs.next ()) {
                    ++rows;
                    success = check(sb, "TABLE_CAT",  rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                    String schemaName   = rs.getString ("TABLE_SCHEM");
                    String tableName    = rs.getString ("TABLE_NAME");
                    String columnName   = rs.getString ("COLUMN_NAME");

                    //output_.println (schemaName + ":" + tableName + ":" + columnName + ":");

                    success = success && schemaName.equals (JDDMDTest.COLLECTION);
                    success = success && tableName.equals ("CPRIVS");
                    success = success && columnName.equals("COLUMN1");

                    String grantor              = rs.getString ("GRANTOR");
                    boolean grantorWasNull      = rs.wasNull ();
                    String grantee              = rs.getString ("GRANTEE");
                    String privilege            = rs.getString ("PRIVILEGE");
                    String isGrantable          = rs.getString ("IS_GRANTABLE");

                    //output_.println (grantor + ":" + grantorWasNull + ":" + grantee + ":"
                    //                + privilege + ":" + isGrantable + ":");

                    success = success && (grantor == null) && grantorWasNull;
                    success = success && grantee.equals ("QUSER"); /*@D1C*/
                    success = success && privilege.equals ("UPDATE");
                    success = success && (isGrantable.equals("NO"));
                }

                rs.close ();
                assertCondition ((rows == 1) && success, "rows="+rows+" sb 1   success="+success+" "+sb.toString());
            }
            else
            {
            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT",  rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";

                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMNXX)"))
                    check3 = true;
            }

            rs.close ();

            // output_.println ("rows = " + rows + ":" + success + ":" + check1 + ":" + check2 + ":" + check3 + ":");

            assertCondition (success && check1 && check2 && check3 && (rows == 4),
                "\nsuccess="+success+" check1="+check1+" check2="+check2+" check3="+check3+" rows="+rows+" sb 4");
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 *     Toolbox var only with "metadata source"=0 (ROI flow) property same as var005
getColumnPrivileges() - Specify empty string for the catalog.
No matching columns should be returned.
**/
    public void Var031()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }

        try {
            ResultSet rs = dmdTBROI_.getColumnPrivileges ("", JDDMDTest.COLLECTION,
                                                "CPRIVS", null);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0, rows + " rows returned for empty catalog string");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 *     Toolbox var only with "metadata source"=0 (ROI flow) property same as var006
getColumnPrivileges() - Specify a catalog that matches the catalog
exactly.  All matching columns should be returned.

**/
    public void Var032()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }

        try {
            StringBuffer sb = new StringBuffer();
            ResultSet rs ;
            // Changed 04/26/2006 to have last parameter as "%" instead of null
              rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),

                  JDDMDTest.COLLECTION,
                  "CPRIVS1", "%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;


                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";

                // output_.println (column + ":");
                sb.append(column+"\n") ;
                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS1(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS1(COLUMN2)"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            //no privileges were granted to the CPRIVS1 table so we do not expect any rows
            //in the result set.
            if (getDriver () == JDTestDriver.DRIVER_NATIVE || (getDriver() == JDTestDriver.DRIVER_JCC))
            {
                assertCondition (rows == 0);
            }
            else
            {
            assertCondition (success && check1 && check2 && (rows == 2),
                "success="+success+" check1="+check1+
                    " check2="+check2+" rows="+rows+
                    " sb 2  message="+sb.toString());
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 *     Toolbox var only with "metadata source"=0 (ROI flow) property same as var007
getColumnPrivileges() - Specify "localhost" for the catalog.
All matching columns should be returned.

**/
    public void Var033()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }

      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4 ) {
        notApplicable("COMPATIBILITY \"localhost\" variation ");
      } else {
        StringBuffer sb = new StringBuffer();
        try {

            ResultSet rs;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              rs = dmdTBROI_.getColumnPrivileges ("localhost",
                  JDDMDTest.COLLECTION,
                  "CPRIVS2", "%");
            } else {


            rs = dmdTBROI_.getColumnPrivileges ("localhost",
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS2", null);
            }

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                sb.append(column+"\n");
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS2(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS2(COLUMNXX)"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            //no privileges were granted to the CPRIVS2 table so we do not expect any rows
            //in the result set.
            if (getDriver () == JDTestDriver.DRIVER_NATIVE )
            {
                assertCondition (rows == 0,sb);
            }
            else
            {
            assertCondition (success && check1 && check2
                    && (rows == 2), sb.toString());
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
 *     Toolbox var only with "metadata source"=0 (ROI flow) property same as var008
getColumnPrivileges() - Specify a catalog pattern for which there is a
match.  No matching columns should be returned, since we do not
support catalog pattern.
**/
    public void Var034()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
            ResultSet rs;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              rs = dmdTBROI_.getColumnPrivileges (system_.toUpperCase().substring (0, 4) + "%"
                  , JDDMDTest.COLLECTION,
                  "CPRIVS", null);

            } else {
            rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog ().substring (0, 4) + "%"
                                                     , JDDMDTest.COLLECTION,
                                                     "CPRIVS", null);
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
 *     Toolbox var only with "metadata source"=0 (ROI flow) property same as var009
getColumnPrivileges() - Specify a catalog for which there is no match.
No matching columns should be returned.
**/
    public void Var035()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }

        try {
            ResultSet rs = dmdTBROI_.getColumnPrivileges ("BOGUS%",
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", null);

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
   *     Toolbox var only with "metadata source"=0 (ROI flow) property same as var010
   * getColumnPrivileges() - Specify null for the schema. No rows should be
   * returned.
   */
  public void Var036() {
      if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
          notApplicable("Toolbox var only.  Testing ROI flow metadata.");
          return;
      }
    try {
      ResultSet rs;
      if (getJdbcLevel() >= 4  || (getDriver() == JDTestDriver.DRIVER_JCC)) {

        rs = dmdTBROI_.getColumnPrivileges(connection_.getCatalog(), null,
          "CPRIVS", "%");
      } else {
        rs = dmdTBROI_.getColumnPrivileges(connection_.getCatalog(), null,
            "CPRIVS", null);

      }
      // @C0
      // when null is passed in for the schema we do not use schema name
      // to narrow the search
      // so we want one row to be returned for COLUMN1
      if ((getDriver() == JDTestDriver.DRIVER_NATIVE) || (getDriver() == JDTestDriver.DRIVER_JCC )) {
      boolean rowAvailable = false;
        boolean check = rs.next();
    rowAvailable = check;
    StringBuffer sb = new StringBuffer();
    int count = 0;
        String columnName="NOT RETREIVED";
        if (check) {
          columnName = rs.getString("COLUMN_NAME");
      count++;
      sb.append("ROW "+count+" "+columnName+"\n");
      check = rs.next();
        }
        while (check) {
      count++;
      sb.append("ROW "+count+" "+rs.getString("COLUMN_NAME")+"\n");
      check = rs.next();
        }

        assertCondition(rowAvailable && columnName.equals("COLUMN1"),
            "row available="+rowAvailable+" columnName="+columnName+" sb COLUMN1 columns are \n"+sb.toString());
        rs.close();
      } else {
        int rows = 0;
        while (rs.next())
          ++rows;

        rs.close();
        assertCondition(rows == 0, "Specified null for schema  ");
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }



/**
 *     Toolbox var only with "metadata source"=0 (ROI flow) property same as var011
 * getColumnPrivileges() - Specify empty string for the schema. No matching
 * columns should be returned.
 */
    public void Var037()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
            ResultSet rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     "",
                                                     "CPRIVS", null);
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
 *     Toolbox var only with "metadata source"=0 (ROI flow) property same as var012
getColumnPrivileges() - Specify a schema that matches a schema
exactly.  All matching columns should be returned.

**/
    public void Var038()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        StringBuffer sb = new StringBuffer();
        try {
          ResultSet rs;

          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                JDDMDTest.COLLECTION2,
                "CPRIVS", "%");
          } else {
            rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION2,
                                                     "CPRIVS", null);
          }
            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT",rs.getString ("TABLE_CAT"),(connection_.getCatalog ())) && success;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                sb.append(column+"\n");
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION2 + ".CPRIVS(COLUMN2)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION2 + ".CPRIVS(COLUMNXX)"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            if (getDriver() == JDTestDriver.DRIVER_NATIVE || (getDriver() == JDTestDriver.DRIVER_JCC))
            {
               assertCondition (rows ==0,sb);
            }
            else
            {
            assertCondition (success && check1 && check2
                    && (rows == 2), sb.toString());
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var013
getColumnPrivileges() - Specify a schema pattern using a percent for which
there is a match.
**/
    public void Var039()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
          ResultSet rs;
          if ((getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC) ) {
            rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                JDDMDTest.SCHEMAS_PERCENT,
                "CPRIVS", "%");

          } else {
            rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.SCHEMAS_PERCENT,
                                                     "CPRIVS", null);
          }
            //@C0
            if ((!(getJdbcLevel()>=4)) && getDriver () == JDTestDriver.DRIVER_NATIVE)
            {
                boolean check = rs.next();
                String columnName = "NOT RETRIEVED";
                if (check) {
                  columnName = rs.getString ("COLUMN_NAME");
                }
                assertCondition(check && columnName.equals("COLUMN1"),
                    "check="+check+" columnName="+columnName+" sb COLUMN1");
                rs.close();
            }
            //No rows should be returned, since we don't support
            //schema pattern here.
            // JDBC says schema is not a pattern and must match exactly.
            else
            {
            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var014
getColumnPrivileges() - Specify null for the table.  No rows
should be returned.
**/
    public void Var040()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
            ResultSet rs ;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                  JDDMDTest.COLLECTION,
                  "%X", "%");
            } else {
            rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     null, null);
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
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var015
getColumnPrivileges() - Specify empty string for the table.
No matching columns should be returned.
**/
    public void Var041()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
            ResultSet rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "", null);

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
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var016
getColumnPrivileges() - Specify a table that matches a table
exactly.  All matching columns should be returned.

**/
    public void Var042()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        StringBuffer sb = new StringBuffer();
        try {
            ResultSet rs;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                  JDDMDTest.COLLECTION,
                  "CPRIVS1", "%");
            } else {
            rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS1", null);
            }
            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";

                sb.append(column+"\n");
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS1(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS1(COLUMN2)"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            if ((getDriver () == JDTestDriver.DRIVER_NATIVE) ||(getDriver () == JDTestDriver.DRIVER_JCC))
            {
                assertCondition(rows == 0,sb);
            }
            else
            {
            assertCondition (success && check1 && check2
                    && (rows == 2), sb.toString());
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var017
getColumnPrivileges() - Specify a table pattern using a percent for which
there is a match.
**/
    public void Var043() {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
    try {
      ResultSet rs;
      if (getJdbcLevel() >= 4) {
        rs = dmdTBROI_.getColumnPrivileges(connection_.getCatalog(),
            JDDMDTest.COLLECTION, "CPRIV%", "%");

      } else {
      rs = dmdTBROI_.getColumnPrivileges(connection_.getCatalog(),
          JDDMDTest.COLLECTION, "CPRIV%", null);
      }

      // @C0
      if ((!(getJdbcLevel() >= 4)) && (getDriver() == JDTestDriver.DRIVER_NATIVE)) {
        boolean check = rs.next();
        String columnName="NOT RETRIEVED";
        if (check) {
          columnName = rs.getString("COLUMN_NAME");
        }
        assertCondition(check && columnName.equals("COLUMN1"),
              "check = "+check+" columnName="+columnName+" sb COLUMN1");
        rs.close();
      }
      // No rows should be returned, since we don't support
      // schema pattern here.
      // JDBC doesn't support table pattern.
      else {
        int rows = 0;
        while (rs.next())
          ++rows;

        rs.close();
        assertCondition(rows == 0);
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }



/**
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var018
 * getColumnPrivileges() - Specify a table pattern for which there is no match.
 * No matching columns should be returned.
 */
    public void Var044()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
            ResultSet rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "BOGUS%", null);

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
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var019
getColumnPrivileges() - Specify null for the column pattern.  All matching
columns should be returned.

**/
    public void Var045()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        StringBuffer sb = new StringBuffer("Rows=\n") ;
        try {
            ResultSet rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS2", null);

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";

                sb.append(column+"\n") ;
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS2(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS2(COLUMNXX)"))
                    check2 = true;
            }

            rs.close ();
            //@C0
            if ((getDriver () == JDTestDriver.DRIVER_NATIVE) || (getDriver () == JDTestDriver.DRIVER_JCC) )
            {
                assertCondition(rows == 0);
            }
            else
            {
            assertCondition (success && check1 && check2
                    && (rows == 2), sb.toString() );
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var020
getColumnPrivileges() - Specify empty string for the column pattern.
No matching columns should be returned.
**/
    public void Var046()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
            ResultSet rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", "");

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
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var021
getColumnPrivileges() - Specify a column pattern that matches a column
exactly.  All matching columns should be returned.

**/
    public void Var047()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
      StringBuffer sb  = new StringBuffer("ROWS:\n") ;
        try {
            ResultSet rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", "COLUMN1");

            // Check for some of the columns.
            boolean check1 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT",rs.getString ("TABLE_CAT"),(connection_.getCatalog ())) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                sb.append(column+"\n") ;
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN1)"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (success && check1
                    && (rows == 1), "success="+success+" check1="+check1+" rows="+rows+" sb 1" + sb.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var022
getColumnPrivileges() - Specify a column pattern using a percent for which
there is a match.  All matching columns should be returned.

**/
    public void Var048()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
            StringBuffer sb = new StringBuffer("ROWS=\n");
            ResultSet rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", "COL%");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean check4 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"),  (connection_.getCatalog ())) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                sb.append(column+"\n") ;
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN1)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN2)"))
                    check3 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMNXX)"))
                    check4 = true;
            }

            // output_.println ("rows = " + rows);

            rs.close ();

            //@C0
            if (getDriver () == JDTestDriver.DRIVER_NATIVE )
            {
                assertCondition (success && check2 && (rows == 1),sb);
            }
            else
            {
            assertCondition (success && check1 && check2 && check3 && check4
                    && (rows == 4), sb.toString() );
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var023
getColumnPrivileges() - Specify a table pattern using an underscore for which
there is a match.  All matching columns should be returned.

**/
    public void Var049()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        StringBuffer sb = new StringBuffer ("ROWS:\n") ;
        try {
            ResultSet rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", "COLUMN_");

            // Check for some of the columns.
            boolean check1 = false;
            boolean check2 = false;
            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;

                success = check(sb, "TABLE_CAT", rs.getString ("TABLE_CAT"), (connection_.getCatalog ())) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";
                sb.append(column+"\n");
                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN1)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN2)"))
                    check2 = true;
            }

            // output_.println ("rows = " + rows);

            rs.close ();
            if (getDriver () == JDTestDriver.DRIVER_NATIVE )
            {
                assertCondition (success && check1 && (rows == 1),sb);
            }
            else
            {
            assertCondition (success && check1 && check2
                    && (rows == 2), sb.toString());
            }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var024
getColumnPrivileges() - Specify a column pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var050()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
            ResultSet rs = dmdTBROI_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", "BOGUS%");

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
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var025
getColumnPrivileges() - Should throw an exception when the connection
is closed.
**/
    public void Var051()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
        try {
            ResultSet rs = dmd2_.getColumnPrivileges (connection_.getCatalog (),
                                                     JDDMDTest.COLLECTION,
                                                     "CPRIVS", null);
            failed ("Didn't throw SQLException but got "+rs);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
//@E1A
/**
 *    Toolbox var only with "metadata source"=0 (ROI flow) property same as var026
getColumnPrivileges() - Specify null for the catalog.  All matching
columns should be returned.

**/
    public void Var052()
    {
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX){
            notApplicable("Toolbox var only.  Testing ROI flow metadata.");
            return;
        }
      StringBuffer sb = new StringBuffer();
        if((!JDTestDriver.isLUW()) ){
            try {
                String pattern = null;
                //
                // The JDBC spec says nothing about a null pattern.. For the native and JCC drivers,
                // use a '%'
                //
                int x = getDriver();
                if ((x == JDTestDriver.DRIVER_JCC) || (x == JDTestDriver.DRIVER_NATIVE)) {
                  pattern="%";
                }
                ResultSet rs = dmdTBROI_.getColumnPrivileges (null, JDDMDTest.COLLECTION,
                                                         "LCNPRIVS", pattern);
                boolean success = false;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

            if (rs.getString("COLUMN_NAME").equals("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST")) {
                        success = true;
            } else {
            sb.append("RETREIVED \""+rs.getString("COLUMN_NAME")+"\"\n");
            }

            sb.append("Retrieved "+rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6)+","+rs.getString(7)+","+rs.getString(8)+"\n");
                }

                rs.close ();


                if (getDriver () == JDTestDriver.DRIVER_NATIVE )
                {
                    assertCondition (success && rows==1, "Added by Toolbox 8/11/2004 for 128 byte column names rows = "+rows+" sb 1,  success ="+success+" "+sb.toString() );
                }
                else
                {
                    assertCondition (success && rows==2, "Added by Toolbox 8/11/2004 for 128 byte column names rows = "+rows+" sb 2,  success ="+success+" "+sb.toString());
                }
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception, Added by Toolbox 8/11/2004 for 128 byte column names.");
            }
    } else {
        notApplicable();
    }
    }


/**
getColumnPrivileges() - Run getColumnPrivileges multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var053()   // @B1A
    {
	String added = " -- added by 1/31/2011 to test for native statement leak in metadata";
        if (checkNative()) {
            try {

		System.gc();
		Statement fill[] = new Statement[20];
		for (int i = 0; i < fill.length; i++) {
		    fill[i] = connection_.createStatement();
		}

		// Run for a maximum of 5 minutes (300 seconds)
		long endTime = System.currentTimeMillis() + 300000;
		Statement stmt = connection_.createStatement();

		for (int i = 0; i < 1000 && System.currentTimeMillis() < endTime ; i++) {
		    // output_.println("Calling getColumnPrivileges");
		    ResultSet rs =
		      dmd_.getColumnPrivileges (
						connection_.getCatalog (),
						JDDMDTest.COLLECTION,
						"CPRIVS", "COLUMN_");
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


  public void Var054() {
	  checkRSMD(false);
  }
  public void Var055() {
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
	    /* Primed using 546CN */

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
      {"isCaseSensitive","5","true"},
      {"isSearchable","5","true"},
      {"isCurrency","5","false"},
      {"isNullable","5","1"},
      {"isSigned","5","false"},
      {"getColumnDisplaySize","5","128"},
      {"getColumnLabel","5","GRANTOR"},
      {"getColumnName","5","GRANTOR"},
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
      {"getColumnDisplaySize","6","128"},
      {"getColumnLabel","6","GRANTEE"},
      {"getColumnName","6","GRANTEE"},
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
      {"isNullable","7","0"},
      {"isSigned","7","false"},
      {"getColumnDisplaySize","7","10"},
      {"getColumnLabel","7","PRIVILEGE"},
      {"getColumnName","7","PRIVILEGE"},
      {"getPrecision","7","10"},
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
      {"isNullable","8","0"},
      {"isSigned","8","false"},
      {"getColumnDisplaySize","8","3"},
      {"getColumnLabel","8","IS_GRANTABLE"},
      {"getColumnName","8","IS_GRANTABLE"},
      {"getPrecision","8","3"},
      {"getScale","8","0"},
      {"getCatalogName","8","LOCALHOST"},
      {"getColumnType","8","12"},
      {"getColumnTypeName","8","VARCHAR"},
      {"isReadOnly","8","true"},
      {"isWritable","8","false"},
      {"isDefinitelyWritable","8","false"},
      {"getColumnClassName","8","java.lang.String"},



	};



	String[][] fixupExtended715T = {
	    {"isNullable","4","1"},
	};

	String[][] fixupExtended736T = {
	    {"isNullable","4","1"},
	    {"getColumnLabel","1","Table Cat"},
	    {"getColumnLabel","2","Table Schem"},
	    {"getColumnLabel","3","Table Name"},
	    {"getColumnLabel","4","Column Name"},
	    {"getColumnLabel","5","Grantor"},
	    {"getColumnLabel","6","Grantee"},
	    {"getColumnLabel","7","Privilege"},
	    {"getColumnLabel","8","Is Grantable"},
	};


	/* Primed using 546CN */
	String [][] fixupExtended5440 = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	} ;

	String [][] fixupExtended726N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isNullable","4","1"},


	} ;


	String [][] fixupExtended736N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isNullable","4","1"},
/* Column label change April 2018 */ 
      {"getColumnLabel","1","Table               Cat"},
      {"getColumnLabel","2","Table               Schem"},
      {"getColumnLabel","3","Table               Name"},
      {"getColumnLabel","4","Column              Name"},
      {"getColumnLabel","5","Grantor"},
      {"getColumnLabel","6","Grantee"},
      {"getColumnLabel","7","Privilege"},
      {"getColumnLabel","8","Is                  Grantable"},


	} ;

	String[][] fixupExtended545N = {
	    {"isSearchable","1","false"},
	    {"isNullable","1","1"},
	    {"isSearchable","2","false"},
	    {"isNullable","2","1"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"getColumnDisplaySize","7","128"},
	    {"getPrecision","7","128"},
	    {"isSearchable","8","false"},
	    {"isNullable","8","1"},
	    {"getColumnDisplaySize","8","128"},
	    {"getPrecision","8","128"},
	};

	String [][] fixupExtended615N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    /* Updated 5/9/2012 */
	    {"isNullable","4","1"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	} ;


	String [][] fixup545T = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"getColumnDisplaySize","7","128"},
	    {"getPrecision","7","128"},
	    {"isNullable","8","1"},
	};

	String [][] fixup545NS = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"getColumnDisplaySize","7","128"},
	    {"getPrecision","7","128"},
	    {"isNullable","8","1"},
	    {"getColumnDisplaySize","8","128"},
	    {"getPrecision","8","128"},
	};

	String [][] fixup615NS = {
	    {"isNullable","4","1"},
	};

	String [][] fixup = {};

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

	    {"714T", fixupExtended715T, "Prime 8/21/2012"},
	    {"715T", fixupExtended715T},
	    {"716T", fixupExtended715T, "Guess 8/21/2012 from 714T"},
	    {"717T", fixupExtended715T, "Guess 8/21/2012  from 714T"},
	    {"718T", fixupExtended715T, "Guess 8/21/2012  from 714T"},
	    {"719T", fixupExtended715T, "Guess 8/21/2012  from 714T"},

	    {"726T", fixupExtended715T, "Guess 10/02/2013  from 714T"},
	    {"727T", fixupExtended715T, "Guess 10/02/2013  from 714T"},
	    {"728T", fixupExtended715T, "Guess 10/02/2013  from 714T"},
	    {"729T", fixupExtended715T, "Guess 10/02/2013  from 714T"},



	    {"736T", fixupExtended736T, "updated 5/29/2019"}, 
	    {"737T", fixupExtended736T, "updated 5/29/2019"},
	    {"738T", fixupExtended736T, "updated 5/29/2019"},
	    {"739T", fixupExtended736T, "updated 5/29/2019"},

	    {"544N", fixupExtended545N},
	    {"545N", fixupExtended545N},
	    {"546N", fixupExtended5440},
	    {"614N", fixupExtended615N},
	    {"615N", fixupExtended615N},
	    {"616N", fixupExtended615N},

	    {"714N", fixupExtended615N},
	    {"715N", fixupExtended615N, "08/16/2012 Primed"}, 
	    {"716N", fixupExtended615N},
	    {"717N", fixupExtended615N, "08/09/2012 Primed"},
	    {"718N", fixupExtended615N},
	    {"719N", fixupExtended615N},

	    {"726N", fixupExtended726N},
	    {"727N", fixupExtended726N},
	    {"728N", fixupExtended726N},
	    {"729N", fixupExtended726N},

	    {"736N", fixupExtended736N},
	    {"737N", fixupExtended736N},
	    {"738N", fixupExtended736N},
	    {"739N", fixupExtended736N},

	    { "716L", fixup615NS},


	};



	Object[][] fixupArray = {
	    { "543TX", fixup545T},
	    { "544TX", fixup545T},
	    { "545TX", fixup545T},
	    { "546TX", fixup545T},

	    { "614TX", fixup545T},
	    { "615TX", fixup545T},
	    { "616TX", fixup545T},
	    { "617TX", fixup545T},
	    { "618TX", fixup545T},

	    { "714TX", fixup545T},
	    { "715TX", fixup545T},
	    { "716TX", fixup545T},
	    { "717TX", fixup545T},
	    { "718TX", fixup545T},
	    { "719TX", fixup545T},

	    { "714TS", fixup615NS},
	    { "715TS", fixup615NS},
	    { "716TS", fixup615NS},
	    { "717TS", fixup615NS}, 
	    { "718TS", fixup615NS},
	    { "719TS", fixup615NS}, 

	    { "724TX", fixup545T},
	    { "725TX", fixup545T},
	    { "726TX", fixup545T},
	    { "727TX", fixup545T},
	    { "728TX", fixup545T},
	    { "729TX", fixup545T},

	    { "544NS", fixup545NS},
	    { "544NX", fixup545NS},
	    { "545NS", fixup545NS},
	    { "545NX", fixup545NS},
	    { "614NS", fixup615NS},
	    { "615NS", fixup615NS},
	    { "616NS", fixup615NS},

	    { "714NS", fixup615NS},

	    { "715NS", fixup615NS, "08/09/2012 -- guess from 717NS"},
	    { "716NS", fixup615NS, "08/09/2012 -- guess from 717NS"},
	    { "717NS", fixup615NS, "08/09/2012 -- primed"},
	    { "718NS", fixup615NS, "08/09/2012 -- primed"},
	    { "719NS", fixup615NS, "08/09/2012 -- primed"},


	    { "716LS", fixup615NS},

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
		    rsA[j] = dmd.getColumnPrivileges (
						connection_.getCatalog (),
						JDDMDTest.COLLECTION,
						"CPRIVS", "COLUMN_");

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
getColumnPrivileges() - Use readonly connection.

**/
    public void Var056()
    {
	boolean found = false;
        StringBuffer sb = new StringBuffer();
        try {
            ResultSet rs ;

	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            rs = dmd.getColumnPrivileges (null, JDDMDTest.COLLECTION,
                  "CPRIVS", "%");
            boolean success = true;
            int rows = 0;
            //@C0
            if (getDriver () == JDTestDriver.DRIVER_JCC ||
                getDriver () == JDTestDriver.DRIVER_NATIVE ||
		getDriver () == JDTestDriver.DRIVER_JTOPENLITE ||
               (getDriver () == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) 
            {
                while (rs.next ()) {
                    ++rows;
                    success = check(sb, "TABLE_CAT",  rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                    String schemaName   = rs.getString ("TABLE_SCHEM");
                    String tableName    = rs.getString ("TABLE_NAME");
                    String columnName   = rs.getString ("COLUMN_NAME");

                    String grantor              = rs.getString ("GRANTOR");
                    boolean grantorWasNull      = rs.wasNull ();
                    String grantee              = rs.getString ("GRANTEE");
                    String privilege            = rs.getString ("PRIVILEGE");
                    String isGrantable          = rs.getString ("IS_GRANTABLE");


		    if ("QUSER".equals(grantee)) {
			success = success && schemaName.equals (JDDMDTest.COLLECTION);
			success = success && tableName.equals ("CPRIVS");
			success = success && columnName.equals("COLUMN1");

			sb.append(schemaName + ":" + tableName + ":" + columnName + ":" +
			          grantor + ":" + grantorWasNull + ":" + grantee + ":" +
		                  privilege + ":" + isGrantable + ":\n");
			if (!JDTestDriver.isLUW()) {
			    success = success && (grantor == null) && grantorWasNull;
			}
			success = success && grantee.equals ("QUSER"); /*@D1C*/
			success = success && privilege.equals ("UPDATE");
			success = success && (isGrantable.equals("NO"));
			found = true;
		    }

                }

                rs.close ();
                assertCondition (((rows == 1) || (rows==17)) && success && found, "rows="+rows+" sb 1 or 17  success="+success+" found="+found+" "+sb.toString());
            }
            else
            {
            // Check for some of the tables.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            while (rs.next ()) {
                ++rows;
                success = check(sb, "TABLE_CAT",  rs.getString ("TABLE_CAT"), connection_.getCatalog ()) && success ;

                String column = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME") + "("
                    + rs.getString ("COLUMN_NAME") + ")";

                // output_.println (column + ":");

                if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN)"))
                    check1 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMN2)"))
                    check2 = true;
                else if (column.equals (JDDMDTest.COLLECTION + ".CPRIVS(COLUMNXX)"))
                    check3 = true;
            }

            rs.close ();

            // output_.println ("rows = " + rows + ":" + success + ":" + check1 + ":" + check2 + ":" + check3 + ":");

            assertCondition (success && check1 && check2 && check3 && (rows == 4),
                "\nsuccess="+success+" check1="+check1+" check2="+check2+" check3="+check3+" rows="+rows+" sb 4");
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



}
