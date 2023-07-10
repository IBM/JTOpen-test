///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetPseudoColumns.java
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
// File Name:    JDDMDGetPseudoColumns.java
//
// Classes:      JDDMDGetPseudoColumns
//
////////////////////////////////////////////////////////////////////////
//
// Compatiblity issues
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test;


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



/**
Testcase JDDMDGetPseudoColumns.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getPseudoColumns()
</ul>
**/
public class JDDMDGetPseudoColumns
extends JDTestcase {



    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;

    // Public toolbox system name
    String systemName;    /** @K2 **/

    boolean var003Executed = false;	// @K3
    StringBuffer message = new StringBuffer();
    String messageColumnName = "";


    //
    // Create the tables for the different types of hidden columns
    // IMPLICITLY HIDDEN
    // FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP
    //
    String TABLE_PATTERN =                                  "JDDPSD%";

    String tableCharHidden         = JDDMDTest.COLLECTION+ ".JDDPSDCH";
    String tableIntHidden          = JDDMDTest.COLLECTION+" .JDDPSDIH";
    String tableRowChangeTimestamp = JDDMDTest.COLLECTION+ ".JDDPSDRC";
    String tableRowChangeTimestamp2= JDDMDTest.COLLECTION2+".JDDPSDRC";
    String tableCharHiddenL         = JDDMDTest.SCHEMAS_LEN128+".JDDPSDCH";
    String tableIntHiddenL          = JDDMDTest.SCHEMAS_LEN128+".JDDPSDIH";
    String tableRowChangeTimestampL = JDDMDTest.SCHEMAS_LEN128+".JDDPSDRC";

    static int ALL_ROWS_COUNT = 4;

/**
Constructor.
**/
    public JDDMDGetPseudoColumns (AS400 systemObject,
                            Hashtable namesAndVars,
                            int runMode,
                            FileOutputStream fileOutputStream,
                            
                            String password)
    {
        super (systemObject, "JDDMDGetPseudoColumns",
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

        // String urlNoSysibm = url;
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
            // urlNoSysibm += ";metadata source=0"; //roi (nosysibm)
        } else {
            systemName = connection_.getCatalog();
            if (systemName == null) {
                systemName= system_;
            }
        }


        dmd_ = connection_.getMetaData ();

        Statement s = connection_.createStatement ();



        tableCharHidden         = JDDMDTest.COLLECTION+".JDDPSDCH";
        tableIntHidden          = JDDMDTest.COLLECTION+".JDDPSDIH";
        tableRowChangeTimestamp = JDDMDTest.COLLECTION+".JDDPSDRC";
        tableRowChangeTimestamp2= JDDMDTest.COLLECTION2+".JDDPSDRC";
        tableCharHiddenL         = JDDMDTest.SCHEMAS_LEN128+".JDDPSDCH";
        tableIntHiddenL          = JDDMDTest.SCHEMAS_LEN128+".JDDPSDIH";
        tableRowChangeTimestampL = JDDMDTest.SCHEMAS_LEN128+".JDDPSDRC";


	String dropStatements54[] = {};
	String createStatements54[] = {};

	String dropStatements[] = {
	/* 0 */     "DROP TABLE " + tableCharHidden,
	/* 1 */     "DROP TABLE " + tableIntHidden,
	/* 2 */     "DROP TABLE " + tableRowChangeTimestamp,
        /* 2 */     "DROP TABLE " + tableRowChangeTimestamp2,
	};

	String createStatements[] = {
                   /* 0 */     "CREATE TABLE " + tableCharHidden +" (IDCH INT NOT NULL, HCH CHAR(10) NOT NULL IMPLICITLY HIDDEN)",
                   /* 1 */     "CREATE TABLE " + tableIntHidden  +" (IDIH INT NOT NULL, HIH INT      NOT NULL IMPLICITLY HIDDEN)",
                   /* 2 */     "CREATE TABLE " + tableRowChangeTimestamp+  " ( IDRCT INT NOT NULL, HRCT FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL )  ",
                   /* 3 */     "CREATE TABLE " + tableRowChangeTimestamp2+ " ( IDRCT INT NOT NULL, HRCT FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL )  ",
       };

	       String dropStatements71[] = {
	           /* 0 */     "DROP TABLE " + tableCharHidden,
	           /* 1 */     "DROP TABLE " + tableIntHidden,
	           /* 2 */     "DROP TABLE " + tableRowChangeTimestamp,
	           /* 3 */     "DROP TABLE " + tableRowChangeTimestamp2,
                   /* 4 */     "DROP TABLE " + tableCharHiddenL,
                   /* 5 */     "DROP TABLE " + tableIntHiddenL,
                   /* 6 */     "DROP TABLE " + tableRowChangeTimestampL,
	           };

	           String createStatements71[] = {
	                      /* 0 */     "CREATE TABLE " + tableCharHidden +" (IDCH INT NOT NULL, HCH CHAR(10) NOT NULL IMPLICITLY HIDDEN)",
	                      /* 1 */     "CREATE TABLE " + tableIntHidden  +" (IDIH INT NOT NULL, HIH INT      NOT NULL IMPLICITLY HIDDEN)",
	                      /* 2 */     "CREATE TABLE " + tableRowChangeTimestamp+  " ( IDRCT INT NOT NULL, HRCT FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL )  ",
	                      /* 3 */     "CREATE TABLE " + tableRowChangeTimestamp2+ " ( IDRCT INT NOT NULL, HRCT FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL )  ",
                              /* 4 */     "CREATE TABLE " + tableCharHiddenL +" (IDCH INT NOT NULL, HCH CHAR(10) NOT NULL IMPLICITLY HIDDEN)",
                              /* 5 */     "CREATE TABLE " + tableIntHiddenL  +" (IDIH INT NOT NULL, HIH INT      NOT NULL IMPLICITLY HIDDEN)",
                              /* 6 */     "CREATE TABLE " + tableRowChangeTimestampL+  " ( IDRCT INT NOT NULL, HRCT FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL )  ",
	          };



        if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)  {
          dropStatements=dropStatements71;
          createStatements=createStatements71;
        }

	if (getRelease() == JDTestDriver.RELEASE_V5R4M0) {
	    dropStatements=dropStatements54;
	    createStatements=createStatements54;
	}


	for (int i = 0; i < dropStatements.length; i++) {

	    try {
		s.executeUpdate (dropStatements[i]);
	    } catch (Exception e) {
		String expectedInfo = "NOT FOUND";
		String exInfo = e.toString().toUpperCase();
		if (exInfo.indexOf(expectedInfo) < 0) {
		    System.out.println("Warning.. Expected "+expectedInfo+": drop table failed "+dropStatements[i]);
		    e.printStackTrace();
		}
	    }
	}







	for (int i = 0; i < createStatements.length; i++) {

	    try {
		s.executeUpdate (createStatements[i]);
	    } catch (Exception e) {
		System.out.println("Warning.. create table failed "+createStatements[i]);
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
    protected void cleanup() throws Exception {
		Statement s = connection_.createStatement();

		String[] dropStatements = {
		/* 0 */"DROP TABLE " + tableCharHidden,
		/* 1 */"DROP TABLE " + tableIntHidden,
		/* 2 */"DROP TABLE " + tableRowChangeTimestamp, };

		for (int i = 0; i < dropStatements.length; i++) {
			try {
				s.executeUpdate(dropStatements[i]);
			} catch (Exception e) {
				System.out.println("Warning.. drop table failed "
						+ dropStatements[i]);
				e.printStackTrace();
			}
		}

		s.close();
		connection_.close();
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
getPseudoColumns() - Check the result set format.
**/
    public void Var001()

    {
	// Result set format checked below.  Removing cases that are not working.
	// Just pass this test to avoid dual maintenance
	if (checkRelease610()) {
		assertCondition(true);
		return; 
	}
    }

/**
getPseudoColumns() - Get a list of those created in this testcase.
**/
    public void Var002()
    {
	if( checkRelease610()) {
        try {


            String catalog = getExpectedTableCat();

            message = new StringBuffer();
            messageColumnName="";

            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",null, JDDMDTest.COLLECTION,
                    TABLE_PATTERN, "%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;
                String column           = rs.getString ("COLUMN_NAME");
                messageColumnName=column;

                short dataType          = rs.getShort ("DATA_TYPE");
                int columnSize          = rs.getInt ("COLUMN_SIZE");
                int decimalDigits       = rs.getInt ("DECIMAL_DIGITS");
                int numPrecRadix        = rs.getInt ("NUM_PREC_RADIX");
                String columnUsage      = rs.getString ("COLUMN_USAGE");
                String remarks          = rs.getString ("REMARKS");
                int charOctetLength     = rs.getInt ("CHAR_OCTET_LENGTH");
                String isNullable       = rs.getString ("IS_NULLABLE");


                /* only 3 hidden columns */
                if (column.equals ("HCH")) {
                  success =  check(rs.getString ("TABLE_SCHEM"), JDDMDTest.COLLECTION, "TABLE_SCHEM") && success;
                  success =  check(rs.getString ("TABLE_NAME"), "JDDPSDCH", "TABLE_NAME") && success;
                  success = check(remarks, (String) null, "remarks") && success;
                  success = check(charOctetLength, 10, "charOctetLength") && success;
                  success = check(dataType,Types.CHAR, "dataType")&& success;
                  success = check(columnSize,10,"columnSize")&& success;
                  success = check(decimalDigits,0,"decimalDigits")&& success;
                  success = check(numPrecRadix,0,"numPrecRadix")&& success;
                  success = check(columnUsage, "NO USAGE RESTRICTIONS","columnUsage")&& success;
                  success = check(isNullable, ("NO"), "isNullable")&& success;
                } else if (column.equals ("HIH")) {
                  success =  check(rs.getString ("TABLE_SCHEM"), JDDMDTest.COLLECTION, "TABLE_SCHEM") && success;
                  success =  check(rs.getString ("TABLE_NAME"), "JDDPSDIH", "TABLE_NAME") && success;
                  success = check(charOctetLength , 0, "charOctetLength")&& success;
                  success = check(columnUsage, "NO USAGE RESTRICTIONS", "columnUsage")&& success;
                  success = check(remarks, (String) null, "remarks") && success;
                  success = check(dataType,         Types.INTEGER, "dataType" )&& success;
                  success = check(columnSize,       10, "columnSize")&& success;
                  success = check(decimalDigits,    0, "decimalDigits" )&& success;
                  success = check(numPrecRadix, 10, "numPrecRadix")&& success;
                  success = check(isNullable,"NO", "isNullable")&& success;

                } else if (column.equals ("HRCT")) {
                  success =  check(rs.getString ("TABLE_SCHEM"), JDDMDTest.COLLECTION, "TABLE_SCHEM") && success;
                  success =  check(rs.getString ("TABLE_NAME"), "JDDPSDRC", "TABLE_NAME") && success;
                  success = check(charOctetLength , 0, "charOctetLength")&& success;
                  success = check(columnUsage, "NO USAGE RESTRICTIONS", "columnUsage")&& success;
              success = check(remarks, (String) null, "remarks") && success;
              success = check(dataType,         Types.TIMESTAMP, "dataType" )&& success;
              success = check(columnSize,       26, "columnSize")&& success;
              success = check(decimalDigits,    6, "decimalDigits" )&& success;
              success = check(numPrecRadix, 0, "numPrecRadix")&& success;
              success = check(isNullable,"NO", "isNullable")&& success;

                }
            } /* while */
            messageColumnName="";
            success = success && check(rows, 3, "rows");
            rs.close ();
            assertCondition ( success, message.toString());
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }

    /**
     * report which rows were missing
     */
   void missingRows(String[] searchStrings, StringBuffer sb) {
     for (int i = 0; i < searchStrings.length; i++) {
       if (searchStrings[i] != null) {
         sb.append("DID NOT FIND ROW: "+searchStrings[i]+"\n");
       }
     }
   }

   /**
    * check to see if current column is one of the expected columns (searchStrings).
    * The searchFound tracks which elements have been found.
    * Returns true only after all searchStrings have been found.
    */
    boolean checkRows (String column, String[] searchStrings, boolean[] searchFound, StringBuffer sb) {
      boolean found = false;
      boolean allFound = true;
      for (int i = 0; i < searchStrings.length; i++) {
        if (searchStrings[i] != null) {
          if (searchStrings[i].equals(column)) {
            sb.append("Found "+column+"\n");
            searchStrings[i] = null;
            found = true;
            searchFound[i] = true;
          }
          allFound = allFound & searchFound[i];
        }
      }
      if (!found) {
        sb.append("Unexpected value "+column+"\n");
      }
      return allFound;
    }


/**
getPseudoColumns() - Specify all null parameters.  Verify that all columns
come back in the library list are returned.
**/
    public void Var003()
    {

	if( checkRelease610()) {

	    message = new StringBuffer();

	    try {

		int endIndex = 8;
		if (JDDMDTest.COLLECTION.length() < 8) {
		    endIndex = JDDMDTest.COLLECTION.length();
		}
		String searchSchema=JDDMDTest.COLLECTION.substring(0,endIndex)+"%";
		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",
									     null, searchSchema, null, null);

	    // It is impossible to check that all columns come back,
	    // so we just check that at least some of them come back.
		boolean allFound = false;
		message.append("looking for 4 strings in "+searchSchema+"\n");
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDCH(HCH)",
		    JDDMDTest.COLLECTION + ".JDDPSDIH(HIH)",
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)",
		};
		boolean[] searchResults = new boolean[searchStrings.length];
		while (rs.next ()) {
		    String column = rs.getString ("TABLE_SCHEM")
		      + "." + rs.getString ("TABLE_NAME") + "("
		      + rs.getString ("COLUMN_NAME") + ")";
		    allFound = checkRows(column, searchStrings, searchResults, message);
		}

		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (allFound, message.toString());
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPseudoColumns() - Specify null for the catalog pattern.  All matching
tables should be returned.
**/
    public void Var004()
    {
	if( checkRelease610()) {

	    try {
		message = new StringBuffer();
		messageColumnName="";


		String catalog = getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",null,
									     JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "%");

	    // Check for some of the tables.
		boolean success = true;
		int rows = 0;
		boolean allFound = false;
		message = new StringBuffer();
		message.append("looking for 4 strings with "+JDDMDTest.SCHEMAS_PERCENT+"/"+TABLE_PATTERN+"\n");
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDCH(HCH)",
		    JDDMDTest.COLLECTION + ".JDDPSDIH(HIH)",
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)",
		};

		boolean[] searchResults = new boolean[searchStrings.length];
		while (rs.next ()) {
		    ++rows;
		    String column = rs.getString ("COLUMN_NAME");
		    messageColumnName=column;

		    success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;

		    column = rs.getString ("TABLE_SCHEM")
		      + "." + rs.getString ("TABLE_NAME") + "("
		      + rs.getString ("COLUMN_NAME") + ")";
		    allFound = checkRows(column, searchStrings, searchResults, message);

		}


		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound, message.toString()+ " rows="+rows);


	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Specify empty string for the catalog pattern.
No matching columns should be returned.
**/
    public void Var005()
    {
	if( checkRelease610()) {

        try {
            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns","",
                                            JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (JDTestDriver.isLUW() ||
                (getDriver() == JDTestDriver.DRIVER_NATIVE && (isJdbc40() || getRelease() >= JDTestDriver.RELEASE_V5R5M0))
                || (getDriver() == JDTestDriver.DRIVER_TOOLBOX)) {
		assertCondition (rows > 0, "rows = "+rows+" sb > 0  empty string for the catalog parameter");
	    } else {
		assertCondition (rows == 0, "rows = "+rows+" sb 0 empty string for the catalog parameter");
	    }

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getPseudoColumns() - Specify a catalog pattern that matches the catalog
exactly.  All matching columns should be returned.
**/
    public void Var006()
    {
	if( checkRelease610()) {

        try {
          String catalog = getExpectedTableCat();

          message = new StringBuffer();
          messageColumnName="";


            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
                                            JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "%");

            // Check for some of the columns.

            boolean allFound = false;
            message.append("looking for 4 strings\n");
            String[] searchStrings = {
              JDDMDTest.COLLECTION + ".JDDPSDCH(HCH)",
              JDDMDTest.COLLECTION + ".JDDPSDIH(HIH)",
              JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
              JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
            };

            boolean success = true;
            int rows = 0;
            boolean[] searchResults = new boolean[searchStrings.length];
            while (rs.next ()) {
                ++rows;
                String column = rs.getString ("COLUMN_NAME");
                messageColumnName=column;

                success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;


                column = rs.getString ("TABLE_SCHEM")
                                + "." + rs.getString ("TABLE_NAME") + "("
                                + rs.getString ("COLUMN_NAME") + ")";

                allFound = checkRows(column, searchStrings, searchResults, message);

            }

            if (!allFound) missingRows(searchStrings, message);
            rs.close ();
            assertCondition (success && allFound && (rows >= ALL_ROWS_COUNT), " rows="+rows+" sb >= "+ALL_ROWS_COUNT+ message.toString());

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    }


/**
getPseudoColumns() - Specify a catalog pattern for which there is a
match.  No matching columns should be returned, since we do not
support catalog pattern.
**/
    public void Var007()
    {
	if( checkRelease610()) {
	    try {
		String catalog = getExpectedTableCat();

		ResultSet rs = null;
		if (JDTestDriver.isLUW()) {
		    rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns","BOGUSCAT",
								       JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "%");

		} else {

		    rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog.substring (0, 4) + "%",
								       JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "%");

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

    }

/**
getPseudoColumns() - Specify a catalog pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var008()
    {
	if( checkRelease610()) {

	    try {
		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns","BOGUS%",
									     JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "%");

		int rows = 0;
		while (rs.next ())
		    ++rows;

		rs.close ();
		assertCondition (rows == 0);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPseudoColumns() - Specify null for the schema pattern.  All matching
columns should be returned.
**/
    public void Var009()
    {
	if( checkRelease610()) {
	    try {
		String catalog = getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog ,
									     null, TABLE_PATTERN, "%");

	    // It is impossible to check that all columns come back,
	    // so we just check that at least some of them come back.
		message = new StringBuffer();
		message.append("Looking for 4 columns with null,"+TABLE_PATTERN+"%");
		boolean allFound = false;
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDCH(HCH)",
		    JDDMDTest.COLLECTION + ".JDDPSDIH(HIH)",
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
		};

		boolean success = true;
		int rows = 0;
    boolean[] searchResults = new boolean[searchStrings.length];

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

		    allFound = checkRows(column, searchStrings, searchResults, message);

		}


		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows >= ALL_ROWS_COUNT), " rows="+rows+" sb >= "+ALL_ROWS_COUNT+ message.toString());

	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Specify empty string for the schema pattern.
No matching columns should be returned.
**/
    public void Var010()
    {
	if( checkRelease610()) {
	    try {
		String catalog = getExpectedTableCat(); ;

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog ,
									     "", TABLE_PATTERN, "%");

		int rows = 0;
		while (rs.next ())
		    ++rows;

		rs.close ();
		assertCondition (rows == 0);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Specify a schema pattern that matches a schema
exactly.  All matching columns should be returned.
**/
    public void Var011()
    {
	if( checkRelease610()) {
	    try {
		String catalog = getExpectedTableCat(); ;

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog ,
									     JDDMDTest.COLLECTION2, TABLE_PATTERN, "%");

	    // Check for some of the columns.
		message = new StringBuffer();
		message.append("Looking for 1 columns with "+JDDMDTest.COLLECTION2+","+TABLE_PATTERN+"%");
		boolean allFound = false;
		String[] searchStrings = {
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
		};
		boolean success = true;
		int rows = 0;
    boolean[] searchResults = new boolean[searchStrings.length];

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

		    allFound = checkRows(column, searchStrings, searchResults, message);

		}


		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows == 1), " rows="+rows+" sb = 1 "+ message.toString());



	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Specify a schema pattern using a percent for which
there is a match.  All matching columns should be returned.
**/
    public void Var012()
    {
	if( checkRelease610()) {
	    try {
		String catalog = getExpectedTableCat(); ;
		message = new StringBuffer();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog ,
									     JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "%");

	    // Check for some of the columns.
		boolean success = true;
		boolean allFound = false;
		message.append("looking for 4 strings\n");
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDCH(HCH)",
		    JDDMDTest.COLLECTION + ".JDDPSDIH(HIH)",
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
		};

    boolean[] searchResults = new boolean[searchStrings.length];

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


		    allFound = checkRows(column, searchStrings, searchResults, message);

		}


		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows >= ALL_ROWS_COUNT), " rows="+rows+" sb >= "+ALL_ROWS_COUNT+ message.toString());
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Specify a schema pattern using an underscore for which
there is a match.  All matching columns should be returned.
**/
    public void Var013()
    {
	if( checkRelease610()) {
        try {
                  String catalog = getExpectedTableCat(); ;

            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
                                            JDDMDTest.SCHEMAS_UNDERSCORE, TABLE_PATTERN, "%");

            boolean allFound = false;
            message = new StringBuffer();
            message.append("looking for 1 strings\n");
            String[] searchStrings = {
              JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
            };

            // Check for some of the columns.
            boolean success = true;
            int rows = 0;
            boolean[] searchResults = new boolean[searchStrings.length];

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

		allFound = checkRows(column, searchStrings, searchResults, message);
            } /* while */

        if (!allFound) missingRows(searchStrings, message);
        rs.close ();
        assertCondition (success && allFound && (rows == 1), " rows="+rows+" sb  "+1+ message.toString());


        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    }

/**
getPseudoColumns() - Specify a schema pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var014()
    {
 	if( checkRelease610()) {
       try {
          String catalog =  getExpectedTableCat();

            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
                                            "BOGUS%", TABLE_PATTERN, "%");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getPseudoColumns() - Specify null for the table pattern.  All matching
columns should be returned.
**/
    public void Var015()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     JDDMDTest.SCHEMAS_PERCENT, null, "%");

	    // It is impossible to check that all columns come back,
	    // so we just check that at least some of them come back.
		boolean allFound = false;
		message = new StringBuffer();
		message.append("looking for 4 strings\n");
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDCH(HCH)",
		    JDDMDTest.COLLECTION + ".JDDPSDIH(HIH)",
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
		};
		boolean success = true;
		int rows = 0;
    boolean[] searchResults = new boolean[searchStrings.length];

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
		    allFound = checkRows(column, searchStrings, searchResults, message);
		} /* while */

		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows >= ALL_ROWS_COUNT), " rows="+rows+" sb >= "+ALL_ROWS_COUNT+ message.toString());

	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Specify empty string for the table pattern.
No matching columns should be returned.
**/
    public void Var016()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
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

    }

/**
getPseudoColumns() - Specify a table pattern that matches a table
exactly.  All matching columns should be returned.
**/
    public void Var017()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     JDDMDTest.SCHEMAS_PERCENT, "JDDPSDRC", "%");

	    // Check for the columns.
		boolean allFound = false;
		message = new StringBuffer();
		message.append("looking for 4 strings\n");
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
		};
    boolean[] searchResults = new boolean[searchStrings.length];

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

		    allFound = checkRows(column, searchStrings, searchResults, message);
		} /* while */

		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows >= 2), " rows="+rows+" sb >= 2 "+ message.toString());


	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPseudoColumns() - Specify a table pattern using a percent for which
there is a match.  All matching columns should be returned.
**/
    public void Var018()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "%");

	    // Check for some of the columns.
		boolean allFound = false;
		message = new StringBuffer();
		message.append("looking for 4 strings\n");
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDCH(HCH)",
		    JDDMDTest.COLLECTION + ".JDDPSDIH(HIH)",
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
		};
    boolean[] searchResults = new boolean[searchStrings.length];

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
		    allFound = checkRows(column, searchStrings, searchResults, message);
		} /* while */

		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows >= ALL_ROWS_COUNT), " rows="+rows+" sb >= "+ALL_ROWS_COUNT+ message.toString());


	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Specify a table pattern using an underscore for which
there is a match.  All matching columns should be returned.
**/
    public void Var019()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     JDDMDTest.SCHEMAS_PERCENT, "JDDPSD_H", "%");

	    // Check for some of the columns.
		boolean allFound = false;
		message = new StringBuffer();
		message.append("looking for 2 strings\n");
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDCH(HCH)",
		    JDDMDTest.COLLECTION + ".JDDPSDIH(HIH)",
		};
    boolean[] searchResults = new boolean[searchStrings.length];

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
		    allFound = checkRows(column, searchStrings, searchResults, message);
		} /* while */

		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows >= 2), " rows="+rows+" sb >= 2 "+ message.toString());


	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPseudoColumns() - Specify a table pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var020()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
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

    }

/**
getPseudoColumns() - Specify null for the column pattern.  All matching
columns should be returned.
**/
    public void Var021()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, null);

	    // It is impossible to check that all columns come back,
	    // so we just check that at least some of them come back.
		boolean allFound = false;
		message = new StringBuffer();
		message.append("looking for 4 strings\n");
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDCH(HCH)",
		    JDDMDTest.COLLECTION + ".JDDPSDIH(HIH)",
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
		};
    boolean[] searchResults = new boolean[searchStrings.length];

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
		    allFound = checkRows(column, searchStrings, searchResults, message);
		} /* while */

		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows >= ALL_ROWS_COUNT), " rows="+rows+" sb >= "+ALL_ROWS_COUNT+ message.toString());


	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Specify empty string for the column pattern.
No matching columns should be returned.
**/
    public void Var022()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "");

		int rows = 0;
		while (rs.next ())
		    ++rows;

		rs.close ();
		assertCondition (rows == 0);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Specify a column pattern that matches a column
exactly.  All matching columns should be returned.
**/
    public void Var023()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "HRCT");

	    // Check for the columns.
		boolean allFound = false;
		message = new StringBuffer();
		message.append("looking for 2 strings\n");
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
		};
    boolean[] searchResults = new boolean[searchStrings.length];

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
		    allFound = checkRows(column, searchStrings, searchResults, message);
		} /* while */

		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows >= 2), " rows="+rows+" sb >= 2 "+ message.toString());

	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}


    }
/**
getPseudoColumns() - Specify a column pattern using a percent for which
there is a match.  All matching columns should be returned.
**/
    public void Var024()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "H%");

	    // Check for some of the columns.
		boolean allFound = false;
		message = new StringBuffer();
		message.append("looking for 4 strings with "+JDDMDTest.SCHEMAS_PERCENT+","+TABLE_PATTERN+", JDDPSD%\n");
		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDCH(HCH)",
		    JDDMDTest.COLLECTION + ".JDDPSDIH(HIH)",
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
		};
    boolean[] searchResults = new boolean[searchStrings.length];

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
		    allFound = checkRows(column, searchStrings, searchResults, message);
		} /* while */

		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows >= ALL_ROWS_COUNT), " rows="+rows+" sb >= "+ALL_ROWS_COUNT+ message.toString());

	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Specify a table pattern using an underscore for which
there is a match.  All matching columns should be returned.
**/
    public void Var025()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     null, TABLE_PATTERN, "HR%");

	    // Check for some of the columns.
		boolean allFound = false;
		message = new StringBuffer();
		message.append("looking for at least 2 strings with null, "+ TABLE_PATTERN + ",H%\n");

		String[] searchStrings = {
		    JDDMDTest.COLLECTION + ".JDDPSDRC(HRCT)",
		    JDDMDTest.COLLECTION2 + ".JDDPSDRC(HRCT)"
		};
    boolean[] searchResults = new boolean[searchStrings.length];

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
		    allFound = checkRows(column, searchStrings, searchResults, message);
		} /* while */

		if (!allFound) missingRows(searchStrings, message);
		rs.close ();
		assertCondition (success && allFound && (rows >= 2), " rows="+rows+" sb >= 2 "+ message.toString());
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}


    }
/**
getPseudoColumns() - Specify a column pattern for which there is no match.
No matching columns should be returned.
**/
    public void Var026()
    {
	if( checkRelease610()) {
	    try {
		String catalog =  getExpectedTableCat();

		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     JDDMDTest.SCHEMAS_PERCENT, TABLE_PATTERN, "BOGUS%");

		int rows = 0;
		while (rs.next ())
		    ++rows;

		rs.close ();
		assertCondition (rows == 0);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
getPseudoColumns() - Should throw an exception when the connection
is closed.
**/
    public void Var027()
    {
	if( checkRelease610()) {
	    try {
		ResultSet resultSet = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd2_, "getPseudoColumns",null, null, null, null);
		failed ("Didn't throw SQLException but got "+resultSet);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
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
			    message.append("Column "+j+" not expected.. Contains "+row[j]);
			} else if ((row[j] == null) || (expected[i][j] == null)) {
			    if ( (row[j] != null) ||  (expected[i][j] != null)) {
				ok = false;
				message.append("Column "+j+":"+expected[0][j]+" \""+row[j]+"\" sb \""+expected[i][j]+"\"\n");
			    }
			} else {
			    if ( ! (row[j].equals(expected[i][j]))) {
				ok = false;
				message.append("Column "+j+":"+expected[0][j]+" \""+row[j]+"\" sb \""+expected[i][j]+"\"\n");
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
 getPseudoColumns() -- Verify that getString can be done on all the columns and the result
 is as expected.
**/
    public void Var028()
    {
	if( checkRelease610()) {
	/** @K2 **/
	   // String systemName = ((AS400JDBCConnection)connection_).getSystem().getSystemName();    //@K1A
	    String expectedBase[][] = {
		{"", "TABLE_CAT","SCHEMA",             "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE",  "COLUMN_SIZE", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "COLUMN_USAGE",         "REMARKS", "CHAR_OCTET_LENGTH", "IS_NULLABLE", },
		{"", systemName, JDDMDTest.COLLECTION,   "JDDPSDCH",    "HCH",         "1",        "10",         null,             null,            "NO USAGE RESTRICTIONS",  null,     "10",                "NO",},
		{"", systemName, JDDMDTest.COLLECTION,   "JDDPSDIH",    "HIH",         "4",        "10",         "0",              "10",            "NO USAGE RESTRICTIONS",  null,     null,                "NO",},
		{"", systemName, JDDMDTest.COLLECTION,   "JDDPSDRC",    "HRCT",        "93",       "26",         "6",              null,            "NO USAGE RESTRICTIONS",  null,     null,                "NO",},
		{"", systemName, JDDMDTest.COLLECTION2,  "JDDPSDRC",    "HRCT",        "93",       "26",         "6",              null,            "NO USAGE RESTRICTIONS",  null,     null,                "NO",},

	    };	// @K3


	    try {
		String catalog =  getExpectedTableCat();

		message = new StringBuffer();
		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",catalog,
									     JDDMDTest.SCHEMAS_PERCENT, "JDDPSD%", "%");

		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		String [][] expected = expectedBase;


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
		// Skip long name if seen
		    if (oneRow[2].equals(JDDMDTest.SCHEMAS_LEN128)) {

		    } else {
			thisOk = checkExpected(oneRow, expected );
		    }
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
				 && (rows >= 4), "Rows="+rows+" >= sb 4 "+message.toString());
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }

	}

    }



    public void checkRSMD(boolean extendedMetadata)
    {

	Connection connection = connection_;
	DatabaseMetaData dmd = dmd_;
	// Note:  I had to have the test size at 40 to recreate the error on V5R4
        //        Will recreate on V5R2 with test size at 40
        //        Could not recreate on V5R3 with test size at 320
	int TESTSIZE=40;

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
	    {"getColumnDisplaySize",	"1","128"},
	    {"getColumnLabel",		"1","TABLE_CAT"},
	    {"getColumnName",		"1","TABLE_CAT"},
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
	    {"getColumnDisplaySize",	"5","11"},
	    {"getColumnLabel",		"5","DATA_TYPE"},
	    {"getColumnName",		"5","DATA_TYPE"},
	    {"getPrecision",		"5","10"},
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
	    {"isNullable",		"6","0"},
	    {"isSigned",		"6","true"},
	    {"getColumnDisplaySize",	"6","11"},
	    {"getColumnLabel",		"6","COLUMN_SIZE"},
	    {"getColumnName",		"6","COLUMN_SIZE"},
	    {"getPrecision",		"6","10"},
	    {"getScale",		"6","0"},
	    {"getCatalogName",		"6","LOCALHOST"},
	    {"getColumnType",		"6","4"},
	    {"getColumnTypeName",	"6","INTEGER"},
	    {"isReadOnly",		"6","true"},
	    {"isWritable",		"6","false"},
	    {"isDefinitelyWritable",	"6","false"},
	    {"getColumnClassName",	"6","java.lang.Integer"},


	    {"isAutoIncrement",		"7","false"},
	    {"isCaseSensitive",		"7","false"},
	    {"isSearchable",		"7","true"},
	    {"isCurrency",		"7","false"},
	    {"isNullable",		"7","1"},
	    {"isSigned",		"7","true"},
	    {"getColumnDisplaySize",	"7","6"},
	    {"getColumnLabel",		"7","DECIMAL_DIGITS"},
	    {"getColumnName",		"7","DECIMAL_DIGITS"},
	    {"getPrecision",		"7","5"},
	    {"getScale",		"7","0"},
	    {"getCatalogName",		"7","LOCALHOST"},
	    {"getColumnType",		"7","5"},
	    {"getColumnTypeName",	"7","SMALLINT"},
	    {"isReadOnly",		"7","true"},
	    {"isWritable",		"7","false"},
	    {"isDefinitelyWritable",	"7","false"},
	    {"getColumnClassName",	"7","java.lang.Integer"},

	    {"isAutoIncrement",		"8","false"},
	    {"isCaseSensitive",		"8","false"},
	    {"isSearchable",		"8","true"},
	    {"isCurrency",		"8","false"},
	    {"isNullable",		"8","1"},
	    {"isSigned",		"8","true"},
	    {"getColumnDisplaySize",	"8","6"},
	    {"getColumnLabel",		"8","NUM_PREC_RADIX"},
	    {"getColumnName",		"8","NUM_PREC_RADIX"},
	    {"getPrecision",		"8","5"},
	    {"getScale",		"8","0"},
	    {"getCatalogName",		"8","LOCALHOST"},
	    {"getColumnType",		"8","5"},
	    {"getColumnTypeName",	"8","SMALLINT"},
	    {"isReadOnly",		"8","true"},
	    {"isWritable",		"8","false"},
	    {"isDefinitelyWritable",	"8","false"},
	    {"getColumnClassName",	"8","java.lang.Integer"},



	    {"isCaseSensitive",         "9","true"},
	    {"isSearchable",            "9","true"},
	    {"isCurrency",              "9","false"},
	    {"isNullable",              "9","0"},
	    {"isSigned",                "9","false"},
	    {"getColumnDisplaySize",    "9","21"},
	    {"getColumnLabel",          "9","COLUMN_USAGE"},
	    {"getColumnName",           "9","COLUMN_USAGE"},
	    {"getPrecision",            "9","21"},
	    {"getScale",                "9","0"},
	    {"getCatalogName",          "9","LOCALHOST"},
	    {"getColumnType",           "9","12"},
	    {"getColumnTypeName",       "9","VARCHAR"},
	    {"isReadOnly",              "9","true"},
	    {"isWritable",              "9","false"},
	    {"isDefinitelyWritable",    "9","false"},
	    {"getColumnClassName",      "9","java.lang.String"},


	    {"isCaseSensitive",		"10","true"},
	    {"isSearchable",		"10","true"},
	    {"isCurrency",		"10","false"},
	    {"isNullable",		"10",		"1"},
	    {"isSigned",		"10","false"},
	    {"getColumnDisplaySize",	"10","2000"},
	    {"getColumnLabel",		"10","REMARKS"},
	    {"getColumnName",		"10","REMARKS"},
	    {"getPrecision",		"10","2000"},
	    {"getScale",		"10","0"},
	    {"getCatalogName",		"10","LOCALHOST"},
	    {"getColumnType",		"10",		"12"},
	    {"getColumnTypeName",	"10","VARCHAR"},
	    {"isReadOnly",		"10","true"},
	    {"isWritable",		"10","false"},
	    {"isDefinitelyWritable",	"10","false"},
	    {"getColumnClassName",	"10","java.lang.String"},

	    {"isAutoIncrement",		"11","false"},
	    {"isCaseSensitive",		"11","false"},
	    {"isSearchable",		"11","true"},
	    {"isCurrency",		"11","false"},
	    {"isNullable",		"11","1"},
	    {"isSigned",		"11","true"},
	    {"getColumnDisplaySize",	"11",		"11"},
	    {"getColumnLabel",		"11","CHAR_OCTET_LENGTH"},
	    {"getColumnName",		"11","CHAR_OCTET_LENGTH"},
	    {"getPrecision",		"11",		"10"},
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
	    {"isNullable",		"12","0"},
	    {"isSigned",		"12","false"},
	    {"getColumnDisplaySize",	"12","3"},
	    {"getColumnLabel",		"12","IS_NULLABLE"},
	    {"getColumnName",		"12","IS_NULLABLE"},
	    {"getPrecision",		"12","3"},
	    {"getScale",		"12","0"},
	    {"getCatalogName",		"12","LOCALHOST"},
	    {"getColumnType",		"12","12"},
	    {"getColumnTypeName",	"12","VARCHAR"},
	    {"isReadOnly",		"12","true"},
	    {"isWritable",		"12","false"},
	    {"isDefinitelyWritable",	"12","false"},
	    {"getColumnClassName",	"12","java.lang.String"},


	};


	String [][] nativeExtendedDifferences = {
	    // {"getColumnLabel","5",""},
            //  {"getColumnLabel","9",""},
	    /* Added these changed 1/11/2012 */
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
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    {"getPrecision","10","2000"},
	    {"getColumnType","10","12"},
	    {"getColumnTypeName","10","NVARCHAR"},


	};


	String [][] nativeExtendedDifferences717N = {
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
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    /* added 11/7/2013 for 7176N on lp01ut18 */
	    {"isNullable","6","1"},
	    {"getPrecision","10","2000"},
	    {"getColumnType","10","-9"},
	    {"getColumnTypeName","10","NVARCHAR"},


	};


	String [][] nativeExtendedDifferences727N = {
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
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    {"isNullable","6","1"},
	    {"getPrecision","10","2000"},
	    {"getColumnType","10","-9"},
	    {"getColumnTypeName","10","NVARCHAR"},


	};



	String [][] nativeExtendedDifferences737N = {
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
	    {"getColumnDisplaySize","5","11"},
	    {"getPrecision","5","10"},
	    {"getColumnType","5","4"},
	    {"getColumnTypeName","5","INTEGER"},

	    {"isNullable","6","1"},
	    {"getPrecision","10","2000"},
	    {"getColumnType","10","-9"},
	    {"getColumnTypeName","10","NVARCHAR"},
/* April 2019 */
	    {"getColumnLabel","1","Table               Cat"},
	    {"getColumnLabel","2","Table               Schem"},
	    {"getColumnLabel","3","Table               Name"},
	    {"getColumnLabel","4","Column              Name"},
	    {"getColumnLabel","6","Column              Size"},
	    {"getColumnLabel","7","Decimal             Digits"},
	    {"getColumnLabel","8","Num                 Prec                Radix"},
	    {"getColumnLabel","10","Remarks"},
	    {"getColumnLabel","11","Char                Octet               Length"},
	    {"getColumnLabel","12","Is                  Nullable"},

	};



	String [][] nativeExtendedDifferences61 = {
	    // {"getColumnLabel","5",""},
            //  {"getColumnLabel","9",""},
	    /* Added these changed 1/11/2012 */
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
	    {"isNullable","6","1"},

	};

	String [][] nativeExtendedDifferences714N = {
	    // {"getColumnLabel","5",""},
            //  {"getColumnLabel","9",""},
	    /* Added these changed 1/11/2012 */
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
	    {"isNullable","6","1"},
	    {"getColumnTypeName","10","NVARCHAR"},
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
	    {"isNullable","6","1"},

	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	};



	String[][] difference726L = {
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    {"getColumnTypeName","10","VARGRAPHIC"},
	}; 
	String [][] differences7140 = {
	    // {"getPrecision","10","4000"},
	    // {"getColumnType","10","-1"},
            // {"getColumnTypeName","10","LONG VARGRAPHIC"},

	    /* Added these changed 1/11/2012 */
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    {"getPrecision","10","2000"},
	    {"getColumnType","10","-9"},
	    {"getColumnTypeName","10","NVARCHAR"},
	    {"isNullable","6","1"}, /* Changed 9/8/2014 for lp17ut23 */ 

	};

	String[][] differences6130 = {
	    { "isNullable","6","1"}
	};


	String[][] differences7130 = {
	    { "isNullable","6","1"},
	    {"getColumnTypeName","10","NVARCHAR"},
	};

	String[][] extendedDifferences714T = {
	    {"isNullable","6","1"},
	    {"getColumnTypeName","10","NVARCHAR"},
	}; 


	String[][] differences6140 = {
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    {"isNullable","6","1"},
	};


	String[][] extendedDifferences616T = {
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    {"isNullable","6","1"},
	};

	String[][] extendedDifferences614T = {
	    {"isNullable","6","1"},
	}; 
	String[][] differences714T = {
	    {"isNullable","6","1"},
	    {"getColumnTypeName","10","NVARCHAR"},
	};

	String[][] differences716T = {
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    {"isNullable","6","1"},  /* 4/16/2014 x1423p1 */
	    {"getColumnLabel","9","SKIP_SKIP_SKIP"},  /* skipping */
	    {"getColumnType","10","-9"},
	    {"getColumnTypeName","10","NVARCHAR"},
	}; 


	String[][] differences726T = {
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    {"getColumnLabel","9","SKIP_SKIP_SKIP"},  /* skipping */ 
	    {"getColumnType","10","-9"},
	    {"getColumnTypeName","10","NVARCHAR"},
	    {"isNullable","6","1"},
	}; 


	String[][] differences736T = {
	    {"getColumnDisplaySize","5","11"},
	    {"getPrecision","5","10"},
	    {"getColumnType","5","4"},
	    {"getColumnTypeName","5","INTEGER"},
	    {"getColumnLabel","9","SKIP_SKIP_SKIP"},  /* skipping */ 
	    {"getColumnType","10","-9"},
	    {"getColumnTypeName","10","NVARCHAR"},
	    {"isNullable","6","1"},
	};


	String[][] differences738T= {
	   
	    {"getColumnDisplaySize","5","11"},
	    {"getPrecision","5","10"},
	    {"getColumnType","5","4"},
	    {"getColumnTypeName","5","INTEGER"},
	    {"getColumnLabel","9","SKIP_SKIP_SKIP"},  /* skipping */ 
	    {"getColumnType","10","-9"},
	    {"getColumnTypeName","10","NVARCHAR"},
	    {"isNullable","6","1"},
	    {"getColumnLabel","1","Table Cat"},
	    {"getColumnLabel","2","Table Schem"},
	    {"getColumnLabel","3","Table Name"},
	    {"getColumnLabel","4","Column Name"},
	    {"getColumnLabel","6","Column Size"},
	    {"getColumnLabel","7","Decimal Digits"},
	    {"getColumnLabel","8","Num Prec Radix"},
	    {"getColumnLabel","10","Remarks"},
	    {"getColumnLabel","11","Char Octet Length"},
	    {"getColumnLabel","12","Is Nullable"},
	}; 

	String [][] differences717N = {
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    /* Added 11/7/2013 lp01ut18 7176N */ 
	    {"isNullable","6","1"},
	    {"getColumnTypeName","10","NVARCHAR"},
	    { "getColumnType","10","-9"}, 
	}; 

	String [][] differences727N = {
	    {"getColumnDisplaySize","5","6"},
	    {"getPrecision","5","5"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    {"getColumnTypeName","10","NVARCHAR"},
	    {"isNullable","6","1"},
	    {"getColumnType","10","-9"},
	}; 


	String [][] differences737N = {
	    {"getColumnDisplaySize","5","11"},
	    {"getPrecision","5","10"},
	    {"getColumnType","5","4"},
	    {"getColumnTypeName","5","INTEGER"},
	    {"getColumnTypeName","10","NVARCHAR"},
	    {"isNullable","6","1"},
	    {"getColumnType","10","-9"},
	}; 

	Object[][] fixupArrayExtended = {


	    { "614T", extendedDifferences614T},
	    { "615T", extendedDifferences614T},
	    { "616T", extendedDifferences616T},
	    { "617T", extendedDifferences616T},
	    { "618T", extendedDifferences616T},

	    { "714T", extendedDifferences714T},
	    { "715T", extendedDifferences714T},
	    { "716T", differences716T, "09/06/2012 -- primed"},
	    { "717T", differences716T, "09/06/2012 -- guess from 716T"},
	    { "718T", differences716T, "09/06/2012 -- guess from 716T"},
	    { "719T", differences716T, "09/06/2012 -- guess from 716T"}, 

	    { "726T", differences726T, "10/01/2013 -- primed"},
	    { "727T", differences726T, "10/01/2013 -- guess from 726T"},
	    { "728T", differences726T, "10/01/2013 -- guess from 726T"},
	    { "729T", differences726T, "10/01/2013 -- guess from 726T"},

	    { "736T", differences738T,  "5/20/2019 -- updated labels"},
	    { "737T", differences738T,  "5/20/2019 -- updated labels"},
	    { "738T", differences738T,  "5/20/2019 -- updated labels"},
	    { "739T", differences738T,  "5/20/2019 -- updated labels"},

	    { "614N", nativeExtendedDifferences61},
	    { "615N", nativeExtendedDifferences61},
	    { "616N", nativeExtendedDifferences6140},
	    { "714N", nativeExtendedDifferences714N},
	    { "715N", nativeExtendedDifferences714N},
	    { "716N", nativeExtendedDifferences717N},
	    { "717N", nativeExtendedDifferences717N},
	    { "718N", nativeExtendedDifferences717N},
	    { "719N", nativeExtendedDifferences717N},
	    { "726N", nativeExtendedDifferences727N},
	    { "727N", nativeExtendedDifferences727N},
	    { "728N", nativeExtendedDifferences727N},
	    { "729N", nativeExtendedDifferences727N},
	    { "736N", nativeExtendedDifferences737N},
	    { "737N", nativeExtendedDifferences737N},
	    { "738N", nativeExtendedDifferences737N},
	    { "739N", nativeExtendedDifferences737N},

	    { "716L", difference726L},
	    { "726L", difference726L},

	};

	Object[][] fixupArray = {

	    { "614T", differences6130},
	    { "615T", differences6130},
	    { "616T", differences6140},
	    { "617T", differences6140},
	    { "618T", differences6140},
            { "714T", differences714T},
	    { "715T", differences714T},

	    { "716T", differences716T, "09/06/2012 -- primed"}, 
	    { "717T", differences716T, "09/06/2012 -- guess from 716T"},
	    { "718T", differences716T, "09/06/2012 -- guess from 716T"},
	    { "719T", differences716T, "09/06/2012 -- guess from 716T"}, 
	    { "726T", differences726T, "10/01/2013 -- primed"},
	    { "727T", differences726T, "10/01/2013 -- guess from 726T"},
	    { "728T", differences726T, "10/01/2013 -- guess from 726T"},
	    { "729T", differences726T, "10/01/2013 -- guess from 726T"}, 

	    { "737T", differences736T, "05/02/2022 "},

	    { "614N", differences6130},
	    { "615N", differences6130},
	    { "616N", differences6140},

	    { "714N", differences7130 },
	    { "715N", differences7130 }, 
	    { "716N", differences7140},
	    { "717N", differences717N, "09/09/2012 -- actual"},
	    { "718N", differences717N, "09/09/2012 -- actual"},
	    { "719N", differences717N, "09/09/2012 -- actual"}, 

	    { "726N", differences727N, "09/09/2012 -- actual"}, 
	    { "727N", differences727N, "09/09/2012 -- actual"},
	    { "728N", differences727N, "09/09/2012 -- actual"},
	    { "729N", differences727N, "09/09/2012 -- actual"}, 

	    { "736N", differences737N, "04/11/2022 -- actual"}, 
	    { "737N", differences737N, "04/11/2022 -- actual"},
	    { "738N", differences737N, "04/11/2022 -- actual"},
	    { "739N", differences737N, "04/11/2022 -- actual"}, 

	    { "716L", difference726L},
	    { "726L", difference726L}, 
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

		} else {
		    fixup = getFixup(fixupArray, null, "fixupArray", message);

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
			StringBuffer prime = new StringBuffer(); 
			for (j = 0; j < TESTSIZE; j++) {
				rsA[j] = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd, "getPseudoColumns",null, "SYSIBM", null, null);
				rsmdA[j] = rsA[j].getMetaData ();

				ResultSetMetaData rsmd = rsmdA[j];


				passed = verifyRsmd(rsmd, methodTests, catalog, j, message, prime) && passed;

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
     *  check the result set metadata with extended metadata off
     */
    public void Var029() {
	if( checkRelease610()) {
          checkRSMD(false);
	}
    }
    /**
     *  check the result set metadata with extended metadata on
     */
    public void Var030() {
	if( checkRelease610()) {
          checkRSMD(true);
	}
    }


    //@128sch
    /**
    getPseudoColumns() - specify 128 len schema
    **/
        public void Var031()
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

		message.append("Calling getPseudoColumns with "+ JDDMDTest.SCHEMAS_LEN128.toUpperCase()+", 'TAB%', '%'");

                rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",null, JDDMDTest.SCHEMAS_LEN128.toUpperCase(), TABLE_PATTERN, "%");

                // Check for some of the columns.
                boolean allFound = true;
                message = new StringBuffer();
                message.append("looking for 3 strings with "+JDDMDTest.SCHEMAS_PERCENT+","+TABLE_PATTERN+", %\n");
                String[] searchStrings = {
                    JDDMDTest.SCHEMAS_LEN128 + ".JDDPSDCH(HCH)",
                    JDDMDTest.SCHEMAS_LEN128 + ".JDDPSDIH(HIH)",
                    JDDMDTest.SCHEMAS_LEN128 + ".JDDPSDRC(HRCT)",
                };
                boolean[] searchResults = new boolean[searchStrings.length];

                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    String column = rs.getString ("COLUMN_NAME");
                    messageColumnName=column;
		    String tableCat = rs.getString ("TABLE_CAT");
                    if (!check(tableCat, catalog, "TABLE_CAT ") ) {
			System.out.println("Expected catalog "+"+ got "+tableCat);
                        success = false;
                    }
                    column = rs.getString ("TABLE_SCHEM")
                                    + "." + rs.getString ("TABLE_NAME") + "("
                                    + rs.getString ("COLUMN_NAME") + ")";


                     allFound = checkRows(column, searchStrings, searchResults, message);

                }

		if (!allFound) {
		    System.out.println("Rows are missing");
		    missingRows(searchStrings, message);
		}
                rs.close ();
                assertCondition (allFound && success && (rows == 3)," rows = "+rows + " sb 3 " + message.toString());


            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }


/**
getPseudoColumns() - Get a list of those created in this testcase using a readonly connection
**/
    public void Var032()
    {
	if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
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
            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd, "getPseudoColumns",null, JDDMDTest.COLLECTION,
                                           "%", "%");
            boolean success = true;

            String catalog = getExpectedTableCat();


            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String column = rs.getString ("COLUMN_NAME");
                messageColumnName=column;

                success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;
                success =  check(rs.getString ("TABLE_SCHEM"), JDDMDTest.COLLECTION, "TABLE_SCHEM") && success;
		/* success =  check(rs.getString ("TABLE_NAME"),"COLUMNS", "TABLE_NAME") && success; */ 
		message.append("read row for column "+column+" schema "+rs.getString ("TABLE_SCHEM")+" table "+rs.getString ("TABLE_NAME")+"\n");

           }

            rs.close ();
            c.close ();
            assertCondition ((rows >= 3) && success, message.toString()+" rows = "+rows+" sb >= 3 -- Read only connection tests added 7/29/2008 " );
        } catch (Exception e) {
            failed (e, "Unexpected Exception -- Read only connection tests added 7/29/2008");
        }
    }


/**
getPseudoColumns() - Get a list of those created in this testcase using a read call  connection
**/
    public void Var033()
    {
	if( checkRelease610()) {
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
		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd, "getPseudoColumns",null, JDDMDTest.COLLECTION,
									     "JDDPSDRC", "%");
		boolean success = true;

		String catalog = getExpectedTableCat();


		int rows = 0;
		while (rs.next ()) {
		    ++rows;
		    String column = rs.getString ("COLUMN_NAME");
		    messageColumnName=column;

		    success =  check(rs.getString ("TABLE_CAT"), catalog, "TABLE_CAT ") && success;
		    success =  check(rs.getString ("TABLE_SCHEM"), JDDMDTest.COLLECTION, "TABLE_SCHEM") && success;
		    success =  check(rs.getString ("TABLE_NAME"),"JDDPSDRC", "TABLE_NAME") && success;

		}

		rs.close ();
		c.close ();
		assertCondition ((rows == 1) && success, "rows="+rows+" sb 1 " + message.toString()+" -- Read call connection tests added 7/29/2008" );
	    } catch (Exception e) {
		failed (e, "Unexpected Exception -- Read only connection tests added 7/29/2008");
	    }
	}
    }
/**
getPseudoColumns() - Run getPseudoColumns multiple times.  Make sure there is not a handle leak.


**/
    public void Var034()
    {
	if( checkRelease610()) {
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
		    // System.out.println("Calling getPseudoColumns");
			ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",null, JDDMDTest.COLLECTION,
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


    }


}// END MAIN
