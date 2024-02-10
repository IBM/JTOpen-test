///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetUDTs.java
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
// File Name:    JDDMDGetUDTs.java
//
// Classes:      JDDMDGetUDTs
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
// 
//
////////////////////////////////////////////////////////////////////////
//
// The following differences/incompatilies are present.
//
// 1.  localhost does not work for JCC/SYSIBM
// 2.  LUW returns rows for catalog pattern (Var009)
// 3.  LUW throws SQL0727 for null/empty schema pattern (Var011,Var012)
// 4.  LUW/SYSIBM returns rows for empty type array (Var024)
// 5.  LUW returns 3 NUMERIC type (Var030) 
// 6.  LUW/SYSIBM returns BINARY for VARCHAR FBD data type. (Var031) 
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
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDDMDGetUDTs.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getUDTs()
</ul>
**/
public class JDDMDGetUDTs
extends JDTestcase {



    // Private data.
    private String              connectionCatalog_; 
    
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;

    private String errorMessage = ""; 
    private String parameterName = ""; 
    StringBuffer message = new StringBuffer(); 
/**
Constructors.
**/
    public JDDMDGetUDTs (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDDMDGetUDTs",
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
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        connectionCatalog_ = connection_.getCatalog(); 
        if (connectionCatalog_ == null) {
           connectionCatalog_ = getCatalogFromURL(baseURL_); 
           System.out.println("Warning:  connection.getCatalog() returned null setting to "+connectionCatalog_); 
        }
        dmd_ = connection_.getMetaData ();


        // Create the distinct types if on V4R4 or later.
        if (areLobsSupported ()) {
          
            Statement s = connection_.createStatement ();
            
            JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION + ".UDTS");
            JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION + ".UDTS1");
            JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION + ".UDTS2");
            JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION + ".UDTSXX");
            JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION2 + ".UDTS");
            JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION2 + ".UDTS3");
            JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION2 + ".UDTS4");
            JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTIONXX + ".UDTSXX");

	    String withComparisonsClause = "";
	    if (JDTestDriver.isLUW()) {
		// LUW requires the withComparisionsClaus
		withComparisonsClause=" WITH COMPARISONS";
		// Collection names limited to 8 characters on LUW
		if ( JDDMDTest.COLLECTIONXX.length() > 8) {
		    JDDMDTest.COLLECTIONXX = JDDMDTest.COLLECTION.substring(0,7)+"X";
		} 
	    }

            s.executeUpdate ("CREATE DISTINCT TYPE " + JDDMDTest.COLLECTION
                             + ".UDTS AS NUMERIC(10,2)"+withComparisonsClause);
            s.executeUpdate ("CREATE DISTINCT TYPE " + JDDMDTest.COLLECTION
                             + ".UDTS1 AS INTEGER"+withComparisonsClause);
            s.executeUpdate ("CREATE DISTINCT TYPE " + JDDMDTest.COLLECTION
                             + ".UDTS2 AS CHAR(4)"+withComparisonsClause);
            s.executeUpdate ("CREATE DISTINCT TYPE " + JDDMDTest.COLLECTION
                             + ".UDTSXX AS VARCHAR(1024) FOR BIT DATA"+withComparisonsClause);
            s.executeUpdate ("CREATE DISTINCT TYPE " + JDDMDTest.COLLECTION2
                             + ".UDTS AS FLOAT"+withComparisonsClause);
            s.executeUpdate ("CREATE DISTINCT TYPE " + JDDMDTest.COLLECTION2
                             + ".UDTS3 AS SMALLINT"+withComparisonsClause);
            s.executeUpdate ("CREATE DISTINCT TYPE  " + JDDMDTest.COLLECTION2
                             + ".UDTS4 AS DECIMAL(20,15)"+withComparisonsClause);
            s.executeUpdate ("CREATE DISTINCT TYPE " + JDDMDTest.COLLECTIONXX
                             + ".UDTSXX AS VARCHAR(50)"+withComparisonsClause);
            
           
            s.close (); 
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
    if (areLobsSupported()) {
      Statement s = connection_.createStatement();
      JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION + ".UDTS");
      JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION + ".UDTS1");
      JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION + ".UDTS2");
      JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION + ".UDTSXX");
      JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION2 + ".UDTS");
      JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION2 + ".UDTS3");
      JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTION2 + ".UDTS4");
      JDTestDriver.dropDistinctType(s, JDDMDTest.COLLECTIONXX + ".UDTSXX");

     
      s.close();
    }

    connection_.close();

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
getUDTs() - Check the result set format.
**/
    public void Var001()
    {
      message.setLength(0); 
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (null, null, null, null);
                ResultSetMetaData rsmd = rs.getMetaData ();

                String[] expectedNames = { "TYPE_CAT", "TYPE_SCHEM",
                        "TYPE_NAME", "CLASS_NAME",
                        "DATA_TYPE", "REMARKS"};
                int[] expectedTypes = { Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR,
                        Types.SMALLINT, Types.VARCHAR};

                String[] expectedNames30 = { "TYPE_CAT", "TYPE_SCHEM",
                        "TYPE_NAME", "CLASS_NAME",
                        "DATA_TYPE", "REMARKS", "BASE_TYPE"};
                int[] expectedTypes30 = { Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR,
                        Types.SMALLINT, Types.VARCHAR, 
                        Types.SMALLINT};

                int[] expectedTypes30V6R1 = { Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR,
                        Types.INTEGER, Types.VARCHAR, 
                        Types.SMALLINT};


                int[] expectedTypes30V7R1 = { Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR,
                        Types.SMALLINT, Types.VARCHAR, 
                        Types.INTEGER};  /* changed 11/18/2016 */ 

			int[] expectedTypes40V7R1 = { Types.VARCHAR, Types.VARCHAR,
			Types.VARCHAR, Types.VARCHAR,
			Types.SMALLINT, -9, 
			Types.SMALLINT};

                int[] expectedTypes30V7R1Native = { Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR,
                        Types.SMALLINT, Types.VARCHAR, 
                        Types.INTEGER};

                int[] expectedTypes40V7R1Native = { Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR,
                        Types.SMALLINT, -9, 
                        Types.SMALLINT};


                int count = rsmd.getColumnCount ();

                if (getJdbcLevel() <= 2) {
                    boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);
                    boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);
                    rs.close ();
                    assertCondition ((count == expectedNames.length) && (namesCheck) && (typesCheck), message);
                } else {
                    if( getRelease() >= JDTestDriver.RELEASE_V7R1M0 ){
			if (getDriver() == JDTestDriver.DRIVER_NATIVE ){
			    if (isJdbc40()) {
				expectedTypes30 = expectedTypes40V7R1Native;
				message.append("using  expectedTypes40V7R1Native\n");

			    } else { 
				expectedTypes30 = expectedTypes30V7R1Native;
				message.append("using  expectedTypes30V7R1Native\n");
			    } 
			} else {
			    if (isJdbc40()) {
				message.append("using  expectedTypes40V7R1"); 
				expectedTypes30 = expectedTypes40V7R1;
			    } else {
				message.append("using  expectedTypes30V7R1"); 
				expectedTypes30 = expectedTypes30V7R1;
			    }
			}
		    } else  if( getRelease() >= JDTestDriver.RELEASE_V5R5M0 &&
				getDriver() == JDTestDriver.DRIVER_NATIVE ){
			/* SYSIBM changes PTF'd back to V6R1 1/11/2010 */ 
			expectedTypes30 = expectedTypes30V6R1;
		    }

		    message.append("Checking column names\n"); 
                    boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames30, message);
		    message.append("Checking column types against expectedTypes30\n"); 
                    boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes30, message);
                    rs.close ();
                    assertCondition ((count == expectedNames30.length) && (namesCheck) && (typesCheck),
                        "\ncount="+count+" sb "+expectedNames30.length+message);
                }

            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Get a list of those created in this testcase and
verify all columns with system remarks (the default).
**/
    public void Var002()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                // Label the types.  No label on TYPES, please.
                /* @A1D if (areLobsSupported ()) {
                    Statement s = connection_.createStatement ();
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTS1 IS '9'");
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTS2 IS 'Ten'");
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTSXX IS 'Client Access/400'");
                    s.close ();
                } */

                ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                                             "UDTS%", null);
                boolean success = true;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;
                    success = success && rs.getString ("TYPE_SCHEM").equals (JDDMDTest.COLLECTION);
                    success = success && (rs.getShort ("DATA_TYPE") == Types.DISTINCT);

                    String type = rs.getString ("TYPE_NAME");
                    String className = rs.getString ("CLASS_NAME");
                    // String remarks = rs.getString ("REMARKS");
                    if (type.equals ("TYPES")) {
                        success = success && className.equals ("java.math.BigDecimal");
                        // @A1D success = success && remarks.equals ("");
                    } else if (type.equals ("TYPES1")) {
                        success = success && className.equals ("java.lang.Integer");
                        // @A1D success = success && remarks.equals ("9");
                    } else if (type.equals ("TYPES2")) {
                        success = success && className.equals ("java.lang.String");
                        // @A1D success = success && remarks.equals ("Ten");
                    } else if (type.equals ("TYPESXX")) {
                        success = success && className.equals ("[B");
                        // @A1D success = success && remarks.equals ("Client Access/400");
                    }
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition ((rows == 4) && success);
                else
                    assertCondition (rows == 0);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Get a list of those created in this testcase and
verify all columns with sql remarks.
**/
    public void Var003()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
              Connection c; 
              if (getDriver() == JDTestDriver.DRIVER_JCC) { 
                c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
              } else { 
                c = testDriver_.getConnection (baseURL_
                                                          + ";remarks=sql", userId_, encryptedPassword_);
              }

                if (areLobsSupported ()) {
                    // Label the types.  No label on TYPES, please.
                    Statement s = connection_.createStatement ();
                    s.executeUpdate ("COMMENT ON TYPE " + JDDMDTest.COLLECTION
                                     + ".UDTS1 IS '1'");
                    s.executeUpdate ("COMMENT ON TYPE " + JDDMDTest.COLLECTION
                                     + ".UDTS2 IS 'Two'");
                    s.executeUpdate ("COMMENT ON TYPE " + JDDMDTest.COLLECTION
                                     + ".UDTSXX IS 'AS/400 Toolbox for Java'");
                    s.close ();
                }

                DatabaseMetaData dmd = c.getMetaData ();
                ResultSet rs = dmd.getUDTs (null, JDDMDTest.COLLECTION,
                                            "UDTS%", null);
                boolean success = true;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;
                    success = success && rs.getString ("TYPE_SCHEM").equals (JDDMDTest.COLLECTION);
                    success = success && (rs.getShort ("DATA_TYPE") == Types.DISTINCT);

                    String type = rs.getString ("TYPE_NAME");
                    String className = rs.getString ("CLASS_NAME");
                    String remarks = rs.getString ("REMARKS");
                    if (type.equals ("TYPES")) {
                        success = success && className.equals ("java.math.BigDecimal");
                        success = success && remarks.equals ("");
                    } else if (type.equals ("TYPES1")) {
                        success = success && className.equals ("java.lang.Integer");
                        success = success && remarks.equals ("1");
                    } else if (type.equals ("TYPES2")) {
                        success = success && className.equals ("java.lang.String");
                        success = success && remarks.equals ("Two");
                    } else if (type.equals ("TYPESXX")) {
                        success = success && className.equals ("[B");
                        success = success && remarks.equals ("AS/400 Toolbox for Java");
                    }
                }

                rs.close ();
                c.close ();
                if (areLobsSupported ())
                    assertCondition ((rows == 4) && success);
                else
                    assertCondition (rows == 0);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify all null parameters.  Verify that all types
come back in the library list are returned.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                Connection c;
                // SQL400 - native driver gets unhappy when you specify a 
                //          value such as *LIBL in your libraries list.
                if (getDriver() == JDTestDriver.DRIVER_JCC) { 
                  c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
                } else if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
                    c = testDriver_.getConnection (baseURL_
                                                   + ";libraries=*LIBL,QIWS," + JDDMDTest.COLLECTION + ","
                                                   + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                                                   userId_, encryptedPassword_);
                else
                    c = testDriver_.getConnection (baseURL_
                                                   + ";libraries=QIWS," + JDDMDTest.COLLECTION + ","
                                                   + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                                                   userId_, encryptedPassword_);

                DatabaseMetaData dmd = c.getMetaData ();
                ResultSet rs = dmd.getUDTs (null, null, null, null);

                // It is impossible to check that all types come back,
                // so we just check that at least some of them come back.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTIONXX + ".UDTSXX"))
                        check3 = true;
                }

                rs.close ();
                c.close ();
                if (areLobsSupported ())
                    assertCondition (check1 && check2 && check3, JDDMDTest.COLLECTION+".UDTS found="+check1+" "+JDDMDTest.COLLECTION+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTION+".UDTSXX found = "+check3 );
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0 when all null parameters specified" );
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify null for the catalog pattern.  All matching
types should be returned.
**/
    public void Var005()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (null,
                                             JDDMDTest.SCHEMAS_PERCENT, "UDTS%", null);

                // Check for some of the types.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTIONXX + ".UDTSXX"))
                        check3 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && check2 && check3
                            && (rows == 8), 
                            JDDMDTest.COLLECTION+".UDTS found="+check1+" "+
                            JDDMDTest.COLLECTION2+".UDTS3 found = "+check2+" "+
                            JDDMDTest.COLLECTIONXX+".UDTSXX found = "+check3+" rows = "+rows+" sb 8");
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0 when null specified for catalog ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify empty string for the catalog pattern.
No matching types should be returned.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs ("",
                                             JDDMDTest.SCHEMAS_PERCENT, "UDTS%", null);

                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
		if (isSysibmMetadata()) {
		    assertCondition (rows > 0, "Specified empty string for catalog parameter, but got "+rows+" rows");
		} else { 
		    assertCondition (rows == 0, "Specified empty string for catalog parameter, but got "+rows+" rows");
		}
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a catalog pattern that matches the catalog
exactly.  All matching types should be returned.
**/
    public void Var007()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "UDTS%", null);

                // Check for some of the types.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTIONXX + ".UDTSXX"))
                        check3 = true;
                }

                rs.close ();
		if (areLobsSupported ()) {
                    assertCondition (success && check1 && check2 && check3
                            && (rows == 8), JDDMDTest.COLLECTION+".UDTS found="+check1+" "+JDDMDTest.COLLECTION+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTION+".UDTSXX found = "+check3+" rows = "+rows+" sb 8");
		} else {
		    assertCondition (rows == 0, "Rows = "+rows+" sb 0 ");
		}
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify "localhost" for the catalog pattern.  
All matching types should be returned.
**/
    public void Var008()
    {
      StringBuffer sb = new StringBuffer(); 

      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4  ||
              (getDriver() == JDTestDriver.DRIVER_NATIVE  && getRelease() >= JDTestDriver.RELEASE_V5R5M0 ) ||
              (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
        notApplicable("\"localhost\" variation ");
      } else {

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs ("localhost",
                                             JDDMDTest.SCHEMAS_PERCENT, "UDTS%", null);

                // Check for some of the types.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTIONXX + ".UDTSXX"))
                        check3 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && check2 && check3
                            && (rows == 8), JDDMDTest.COLLECTION+".UDTS found="+check1+" "+JDDMDTest.COLLECTION+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTION+".UDTSXX found = "+check3+" rows = "+rows+" sb 8");
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0 ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
      }
    }



/**
getUDTs() - Specify a catalog pattern for which there is a
match.  No matching types should be returned, since we do not
support catalog pattern.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            try {
              String catalogSearch = connection_.getCatalog ();
              if (catalogSearch != null) {
                catalogSearch = catalogSearch.substring (0, 4) + "%";
              }
                ResultSet rs = dmd_.getUDTs (catalogSearch,
                                             JDDMDTest.SCHEMAS_PERCENT, "UDTS%", null);

                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                if (JDTestDriver.isLUW()) {
                  assertCondition (rows > 0, "Rows = "+rows+" sb > 0 for catalog patterm");
                } else { 
                   assertCondition (rows == 0, "Rows = "+rows+" sb 0 for catalog patterm");
                }       
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a catalog pattern for which there is no match.
No matching types should be returned.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs ("BOGUS%",
                                             JDDMDTest.SCHEMAS_PERCENT, "UDTS%", null);

                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                assertCondition (rows == 0, "Rows = "+rows+" sb 0 ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify null for the schema pattern.  All matching
types should be returned.
**/
    public void Var011()
    {
      StringBuffer sb = new StringBuffer(); 
      if (JDTestDriver.isLUW()) {
        notApplicable("LUW procedure throws SQL0727 when schema pattern = null");
        return; 
      }
        if (checkJdbc20 ()) {
            try {
                Connection c;
                // SQL400 - native driver gets unhappy when you specify a 
                //          value such as *LIBL in your libraries list.              Connection c; 
                if (getDriver() == JDTestDriver.DRIVER_JCC) { 
                  c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
                } else if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
                    c = testDriver_.getConnection (baseURL_
                                                   + ";libraries=*LIBL," + JDDMDTest.COLLECTION + ","
                                                   + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                                                   userId_, encryptedPassword_);
                else
                    c = testDriver_.getConnection (baseURL_
                                                   + ";libraries=" + JDDMDTest.COLLECTION + ","
                                                   + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                                                   userId_, encryptedPassword_);

                DatabaseMetaData dmd = c.getMetaData ();
                ResultSet rs = dmd.getUDTs (c.getCatalog (),
                                            null, "UDTS%", null);

                // It is impossible to check that all types come back,
                // so we just check that at least some of them come back.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTIONXX + ".UDTSXX"))
                        check3 = true;
                }

                rs.close ();
                c.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && check2 && check3
                            && (rows >= 8),  JDDMDTest.COLLECTION+".UDTS found="+check1+" "+JDDMDTest.COLLECTION+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTION+".UDTSXX found = "+check3+" rows = "+rows+" sb >= 8" );
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0 ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify empty string for the schema pattern.
No matching types should be returned.
**/
    public void Var012()
    {
      if (JDTestDriver.isLUW()) {
        notApplicable("LUW procedure throws SQL0727 when schema pattern = empty string");
        return; 
      }
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             "", "UDTS%", null);

                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                assertCondition (rows == 0, "Rows = "+rows+" sb 0 ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a schema pattern that matches a schema
exactly.  All matching types should be returned.
**/
    public void Var013()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.COLLECTION2, "UDTS%", null);

                // Check for some of the types.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS4"))
                        check3 = true;
                }

                rs.close ();
                if (areLobsSupported ()) {
                    assertCondition (success && check1 && check2 && check3
                            && (rows == 3),  JDDMDTest.COLLECTION2+".UDTS found="+check1+" "+JDDMDTest.COLLECTION2+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTION2+".UDTS4 found = "+check3+" rows = "+rows+" sb 3");
                } else {
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0 ");
                }
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a schema pattern using a percent for which
there is a match.  All matching types should be returned.
**/
    public void Var014()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "UDTS%", null);

                // Check for some of the types.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTIONXX + ".UDTSXX"))
                        check3 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && check2 && check3
                            && (rows == 8), JDDMDTest.COLLECTION+".UDTS found="+check1+" "+JDDMDTest.COLLECTION+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTION+".UDTSXX found = "+check3+" rows = "+rows+" sb 8");
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0 ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a schema pattern using an underscore for which
there is a match.  All matching types should be returned.
**/
    public void Var015()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                // For some reason, this only matches when we tack on a % at the end...
                // I verified this by typing directly into the green screen...
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_UNDERSCORE + '%', "UDTS%", null);

                // Check for some of the types.
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
		    if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check3 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check2 && check3
                            && (rows == 4), JDDMDTest.COLLECTION2+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTION2+".UDTSXX found = "+check3+" rows = "+rows+" sb 8");
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0 ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a schema pattern for which there is no match.
No matching types should be returned.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             "BOGUS%", "UDTS%", null);

                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                assertCondition (rows == 0, "Rows = "+rows+" sb 0 ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify null for the type name pattern.  All matching
types should be returned.
**/
    public void Var017()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, null, null);

                // It is impossible to check that all types come back,
                // so we just check that at least some of them come back.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTIONXX + ".UDTSXX"))
                        check3 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && check2 && check3
                            && (rows >= 8), JDDMDTest.COLLECTION+".UDTS found="+check1+" "+JDDMDTest.COLLECTION2+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTIONXX+".UDTSXX found = "+check3+" rows = "+rows+" sb > 8");
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0 ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify empty string for the type name pattern.
No matching types should be returned.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "", null);

                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                if (JDTestDriver.isLUW()) { 
                  assertCondition (rows > 0, "Empty string specified for name pattern but "+rows+" rows returned");
                } else {
                  assertCondition (rows == 0, "Empty string specified for name pattern but "+rows+" rows returned");
                
                }
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a type name pattern that matches a type
exactly.  All matching types should be returned.
**/
    public void Var019()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "UDTS", null);

                // Check for the types.
                boolean check1 = false;
                boolean check2 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS"))
                        check2 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && check2
                            && (rows == 2),  JDDMDTest.COLLECTION+".UDTS found="+check1+" "+JDDMDTest.COLLECTION2+".UDTS found = "+check2+" rows = "+rows+" sb 2");
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a type name pattern using a percent for which
there is a match.  All matching types should be returned.
**/
    public void Var020()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "UDTS%", null);

                // Check for some of the types.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTIONXX + ".UDTSXX"))
                        check3 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && check2 && check3
                            && (rows == 8), JDDMDTest.COLLECTION+".UDTS found="+check1+" "+JDDMDTest.COLLECTION+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTION+".UDTSXX found = "+check3+" rows = "+rows+" sb 8");
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a types pattern using an underscore for which
there is a match.  All matching types should be returned.
**/
    public void Var021()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "UDTS_", null);

                // Check for some of the types.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS1"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS4"))
                        check3 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && check2 && check3
                            && (rows == 4), JDDMDTest.COLLECTION+".UDTS1 found="+check1+" "+JDDMDTest.COLLECTION2+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTION2+".UDTS4 found = "+check3+" rows = "+rows+" sb 4");
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a type name pattern for which there is no match.
No matching types should be returned.
**/
    public void Var022()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "BOGUS%", null);

                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getUDTs() - Specify null for the types.  All matching
types should be returned.
**/
    public void Var023()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "%", null);

                // Check for some of the types.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String typeName = rs.getString ("TYPE_SCHEM")
                                      + "." + rs.getString ("TYPE_NAME");
                    short dataType = rs.getShort ("DATA_TYPE");
                    if ((typeName.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        && (dataType == Types.DISTINCT))
                        check1 = true;
                    else if ((typeName.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                             && (dataType == Types.DISTINCT))
                        check2 = true;
                    else if ((typeName.equals (JDDMDTest.COLLECTIONXX + ".UDTSXX"))
                             && (dataType == Types.DISTINCT))
                        check3 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && check2 && check3
                            && (rows >= 8), JDDMDTest.COLLECTION+".UDTS found="+check1+" "+JDDMDTest.COLLECTION2+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTIONXX+".UDTSXX found = "+check3+" rows = "+rows+" sb >= 8");
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify empty array for the type.
No matching types should be returned.
**/
    public void Var024()
    {
        if (checkJdbc20 ()) {
            try {
                int[] types = new int[0];
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "%", types);

                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                if (JDTestDriver.isLUW() || isSysibmMetadata()){ 
                  assertCondition (rows > 0, "Specified empty array for type and "+rows+" rows returned");
                } else { 
                  assertCondition (rows == 0, "Specified empty array for type and "+rows+" rows returned");
                }
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a 1 element array that matches a type
exactly.  All matching types should be returned.
**/
    public void Var025()
    {
        StringBuffer sb = new StringBuffer(); 
        if (checkJdbc20 ()) {
            try {
                int[] types = { Types.DISTINCT};
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "%", types);

                // Check for the types.
                boolean check1 = false;
                boolean check2 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;

                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && check2 && (rows > 2), 
                        JDDMDTest.COLLECTION+".UDTS found="+check1+" "+
                        JDDMDTest.COLLECTION2+".UDTS3 found = "+check2+
                        " rows = "+rows+" sb > 2");
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a 1 element array that does not match a type.
All matching types should be returned.
**/
    public void Var026()
    {
        if (checkJdbc20 ()) {
            try {
                int[] types = { Types.JAVA_OBJECT};
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "%", types);

                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a multiple element array that matches some types
exactly but includes a bogus type.  All matching types should be
returned.
**/
    public void Var027()
    {
      message.setLength(0); 
        if (checkJdbc20 ()) {
            try {
                int[] types = { Types.JAVA_OBJECT, Types.DISTINCT, Types.STRUCT};
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "%UDTS%", types);

                // Check for some of the types.
                boolean check1 = false;
                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    
                    success=JDDMDTest.check( rs.getString("TYPE_CAT"),   connectionCatalog_,
                        "TYPE_CAT", "", message, success); 

                    String typeName = rs.getString ("TYPE_SCHEM")
                                      + "." + rs.getString ("TYPE_NAME");
                    short dataType = rs.getShort ("DATA_TYPE");
                    if ((typeName.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        && (dataType == Types.DISTINCT))
                        check1 = true;
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition (success && check1 && (rows >= 8), 
                        "\nWhen bogus type in list... success="+success+" check1="+check1+" rows="+rows+" sb >= 8 "+message.toString());
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Specify a multiple element array that does not match
any types.  No matching types should be returned.
**/
    public void Var028()
    {
        if (checkJdbc20 ()) {
            try {
                int[] types = { Types.JAVA_OBJECT, Types.STRUCT, Types.INTEGER};
                ResultSet rs = dmd_.getUDTs (connection_.getCatalog (),
                                             JDDMDTest.SCHEMAS_PERCENT, "%", types);

                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getUDTs() - Should throw an exception when the connection
is closed.
**/
    public void Var029()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet resultSet = dmd2_.getUDTs (null, null, null, null);
                failed ("Didn't throw SQLException but got "+resultSet);
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getUDTs() - Test new JDBC 3.0 field named BASE_TYPE.
**/
    public void Var030()
    {
        StringBuffer sb = new StringBuffer(); 
        if (checkJdbc30 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                                             "UDTS%", null);

                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean check4 = false;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    String baseType = rs.getString ("BASE_TYPE");
                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS")) {
                        // LUW returns 3 -- DECIMAL
                        check1 = baseType.equals("2") || baseType.equals("3");
                        if (!check1) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                             ".UDTS expected 2\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTS1")) {
                        check2 = baseType.equals("4");
                        if (!check2) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTS1 expected 4\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTS2")) {
                        check3 = baseType.equals("1");
                        if (!check3) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTS2 expected 1\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTSXX")) {
                        // LUW returns -3 (VARBINARY) 
                        check4 = baseType.equals("12") || baseType.equals("-3");
                        if (!check1) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTSXX expected 12\n"); 
                    }
                }

                rs.close ();
		if (areLobsSupported ()) {
                    assertCondition (check1 && check2 && check3 && check4 ,
                                 " check1="+check1+
                                 " check2="+check2+
                                 " check3="+check3+
                                 " check4="+check4 + sb.toString());
		}  else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getUDTs() - Get a list of those created in this testcase and
verify all columns with system remarks (the default) using getInt
**/
    public void Var031()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                // Label the types.  No label on TYPES, please.
                /* @A1D if (areLobsSupported ()) {
                    Statement s = connection_.createStatement ();
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTS1 IS '9'");
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTS2 IS 'Ten'");
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTSXX IS 'Client Access/400'");
                    s.close ();
                } */

                ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                                             "UDTS%", null);
                boolean success = true;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;
                    success = success && rs.getString ("TYPE_SCHEM").equals (JDDMDTest.COLLECTION);
                    success = success && (rs.getInt ("DATA_TYPE") == Types.DISTINCT);

                    String type = rs.getString ("TYPE_NAME");
                    String className = rs.getString ("CLASS_NAME");
                    // String remarks = rs.getString ("REMARKS");
                    if (type.equals ("TYPES")) {
                        success = success && className.equals ("java.math.BigDecimal");
                        // @A1D success = success && remarks.equals ("");
                    } else if (type.equals ("TYPES1")) {
                        success = success && className.equals ("java.lang.Integer");
                        // @A1D success = success && remarks.equals ("9");
                    } else if (type.equals ("TYPES2")) {
                        success = success && className.equals ("java.lang.String");
                        // @A1D success = success && remarks.equals ("Ten");
                    } else if (type.equals ("TYPESXX")) {
                        success = success && className.equals ("[B");
                        // @A1D success = success && remarks.equals ("Client Access/400");
                    }
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition ((rows == 4) && success);
                else
                    assertCondition (rows == 0);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getUDTs() - Get a list of those created in this testcase and
verify all columns with system remarks (the default) using getLong
**/
    public void Var032()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                // Label the types.  No label on TYPES, please.
                /* @A1D if (areLobsSupported ()) {
                    Statement s = connection_.createStatement ();
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTS1 IS '9'");
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTS2 IS 'Ten'");
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTSXX IS 'Client Access/400'");
                    s.close ();
                } */

                ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                                             "UDTS%", null);
                boolean success = true;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    success = check(sb, "TABLE_CAT", rs.getString ("TYPE_CAT"), connection_.getCatalog ()) && success;
                    success = success && rs.getString ("TYPE_SCHEM").equals (JDDMDTest.COLLECTION);
                    success = success && (rs.getLong ("DATA_TYPE") == Types.DISTINCT);

                    String type = rs.getString ("TYPE_NAME");
                    String className = rs.getString ("CLASS_NAME");
                    // String remarks = rs.getString ("REMARKS");
                    if (type.equals ("TYPES")) {
                        success = success && className.equals ("java.math.BigDecimal");
                        // @A1D success = success && remarks.equals ("");
                    } else if (type.equals ("TYPES1")) {
                        success = success && className.equals ("java.lang.Integer");
                        // @A1D success = success && remarks.equals ("9");
                    } else if (type.equals ("TYPES2")) {
                        success = success && className.equals ("java.lang.String");
                        // @A1D success = success && remarks.equals ("Ten");
                    } else if (type.equals ("TYPESXX")) {
                        success = success && className.equals ("[B");
                        // @A1D success = success && remarks.equals ("Client Access/400");
                    }
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition ((rows == 4) && success);
                else
                    assertCondition (rows == 0);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getUDTs() - Get a list of those created in this testcase and
verify all columns with system remarks (the default) using getObject
**/
    public void Var033()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                // Label the types.  No label on TYPES, please.
                /* @A1D if (areLobsSupported ()) {
                    Statement s = connection_.createStatement ();
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTS1 IS '9'");
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTS2 IS 'Ten'");
                    s.executeUpdate ("LABEL ON TYPE " + JDDMDTest.COLLECTION
                        + ".UDTSXX IS 'Client Access/400'");
                    s.close ();
                } */

                ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                                             "UDTS%", null);
                boolean success = true;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    success = check(sb, "TABLE_CAT", (String) rs.getObject ("TYPE_CAT"), connection_.getCatalog ()) && success;
                    success = success && ((String) rs.getObject ("TYPE_SCHEM")).equals (JDDMDTest.COLLECTION);
                    success = success && (((Integer)rs.getObject ("DATA_TYPE")).intValue() == Types.DISTINCT);

                    String type = (String) rs.getObject ("TYPE_NAME");
                    String className = (String) rs.getObject ("CLASS_NAME");
                    // String remarks = (String) rs.getObject ("REMARKS");
                    if (type.equals ("TYPES")) {
                        success = success && className.equals ("java.math.BigDecimal");
                        // @A1D success = success && remarks.equals ("");
                    } else if (type.equals ("TYPES1")) {
                        success = success && className.equals ("java.lang.Integer");
                        // @A1D success = success && remarks.equals ("9");
                    } else if (type.equals ("TYPES2")) {
                        success = success && className.equals ("java.lang.String");
                        // @A1D success = success && remarks.equals ("Ten");
                    } else if (type.equals ("TYPESXX")) {
                        success = success && className.equals ("[B");
                        // @A1D success = success && remarks.equals ("Client Access/400");
                    }
                }

                rs.close ();
                if (areLobsSupported ())
                    assertCondition ((rows == 4) && success);
                else
                    assertCondition (rows == 0);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getUDTs() - Test new JDBC 3.0 field named BASE_TYPE using getShort
**/
    public void Var034()
    {
        StringBuffer sb = new StringBuffer(); 
        if (checkJdbc30 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                                             "UDTS%", null);

                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean check4 = false;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    short baseType = rs.getShort ("BASE_TYPE");
                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS")) {
                        // LUW returns 3 -- DECIMAL
                        check1 = ( baseType == 2) || (baseType == 3);
                        if (!check1) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                             ".UDTS expected 2\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTS1")) {
                        check2 = (baseType == 4); 
                        if (!check2) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTS1 expected 4\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTS2")) {
                        check3 = (baseType == 1); 
                        if (!check3) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTS2 expected 1\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTSXX")) {
                        // LUW returns -3 (VARBINARY) 
                        check4 = ( baseType == 12 ) || (baseType == -3);
                        if (!check1) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTSXX expected 12\n"); 
                    }
                }

                rs.close ();
		if (areLobsSupported ()) {
                    assertCondition (check1 && check2 && check3 && check4 ,
                                 " check1="+check1+
                                 " check2="+check2+
                                 " check3="+check3+
                                 " check4="+check4 + sb.toString());
		}  else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getUDTs() - Test new JDBC 3.0 field named BASE_TYPE using getInt
**/
    public void Var035()
    {
        StringBuffer sb = new StringBuffer(); 
        if (checkJdbc30 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                                             "UDTS%", null);

                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean check4 = false;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    int baseType = rs.getInt ("BASE_TYPE");
                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS")) {
                        // LUW returns 3 -- DECIMAL
                        check1 = ( baseType == 2) || (baseType == 3);
                        if (!check1) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                             ".UDTS expected 2\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTS1")) {
                        check2 = (baseType == 4); 
                        if (!check2) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTS1 expected 4\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTS2")) {
                        check3 = (baseType == 1); 
                        if (!check3) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTS2 expected 1\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTSXX")) {
                        // LUW returns -3 (VARBINARY) 
                        check4 = ( baseType == 12 ) || (baseType == -3);
                        if (!check1) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTSXX expected 12\n"); 
                    }
                }

                rs.close ();
		if (areLobsSupported ()) {
                    assertCondition (check1 && check2 && check3 && check4 ,
                                 " check1="+check1+
                                 " check2="+check2+
                                 " check3="+check3+
                                 " check4="+check4 + sb.toString());
		}  else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getUDTs() - Test new JDBC 3.0 field named BASE_TYPE using getLong
**/
    public void Var036()
    {
        StringBuffer sb = new StringBuffer(); 
        if (checkJdbc30 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                                             "UDTS%", null);

                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean check4 = false;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    short baseType = (short) rs.getLong ("BASE_TYPE");
                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS")) {
                        // LUW returns 3 -- DECIMAL
                        check1 = ( baseType == 2) || (baseType == 3);
                        if (!check1) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                             ".UDTS expected 2\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTS1")) {
                        check2 = (baseType == 4); 
                        if (!check2) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTS1 expected 4\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTS2")) {
                        check3 = (baseType == 1); 
                        if (!check3) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTS2 expected 1\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTSXX")) {
                        // LUW returns -3 (VARBINARY) 
                        check4 = ( baseType == 12 ) || (baseType == -3);
                        if (!check1) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTSXX expected 12\n"); 
                    }
                }

                rs.close ();
		if (areLobsSupported ()) {
                    assertCondition (check1 && check2 && check3 && check4 ,
                                 " check1="+check1+
                                 " check2="+check2+
                                 " check3="+check3+
                                 " check4="+check4 + sb.toString());
		}  else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getUDTs() - Test new JDBC 3.0 field named BASE_TYPE using getObject
**/
    public void Var037()
    {
        StringBuffer sb = new StringBuffer(); 
        if (checkJdbc30 ()) {
            try {
                ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                                             "UDTS%", null);

                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean check4 = false;

                int rows = 0;
                while (rs.next ()) {
                    ++rows;

                    short baseType = ((Integer) rs.getObject ("BASE_TYPE")).shortValue();
                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS")) {
                        // LUW returns 3 -- DECIMAL
                        check1 = ( baseType == 2) || (baseType == 3);
                        if (!check1) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                             ".UDTS expected 2\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTS1")) {
                        check2 = (baseType == 4); 
                        if (!check2) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTS1 expected 4\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTS2")) {
                        check3 = (baseType == 1); 
                        if (!check3) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTS2 expected 1\n"); 
                    } else if (type.equals (JDDMDTest.COLLECTION + ".UDTSXX")) {
                        // LUW returns -3 (VARBINARY) 
                        check4 = ( baseType == 12 ) || (baseType == -3);
                        if (!check1) sb.append("Got "+baseType+" for "+JDDMDTest.COLLECTION+
                        ".UDTSXX expected 12\n"); 
                    }
                }

                rs.close ();
		if (areLobsSupported ()) {
                    assertCondition (check1 && check2 && check3 && check4 ,
                                 " check1="+check1+
                                 " check2="+check2+
                                 " check3="+check3+
                                 " check4="+check4 + sb.toString());
		}  else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0  ");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


     /*
      * Test a data type
      */


  class ExpectedTypeFormat {
    String typeName; /* 3 */ 
    String declaration;
    String className; /* 4 */
    String dataType; /* 5 */
    String remarks; /* 6 */
    String baseType; /* 7 */

    public ExpectedTypeFormat(String typeName,
			      String declaration,
			      String className,
			      String dataType,
			      String remarks,
			      String baseType) {
      this.typeName = typeName;
      this.declaration = declaration;
      this.className = className; 
      this.dataType = dataType;
      this.remarks = remarks;
      this.baseType = baseType;
    }


    public String getColumnValue(int column) {
      switch (column) {
      case 3:
        return typeName;

      case 4:
        return className;

      case 5:
        return dataType;

      case 6:
        return remarks;

      case 7:
        return baseType;

      default:
        return "UNKNOWN";
      }
    }

    public String getColumnName(int column) {
      switch (column) {
      case 3:
        return "typeName";

      case 4:
        return "className";

      case 5:
        return "dataType";

      case 6:
        return "remarks";

      case 7:
        return "baseType";
      default:
        return "UNKNOWN";
      }
      
    }
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


    /**
     * Test a datatype in the catalog 
     */

    public void testDataType(ExpectedTypeFormat ex, String added ) {
    
        int column = 0; 
        errorMessage=""; 
        parameterName=""; 
        String sql=""; 
        try {
          int maxColumn = 7; 
          boolean success = true;
          boolean rowFound = false;  
          Statement s = connection_.createStatement();
          try {
            s.executeUpdate("DROP DISTINCT TYPE" + JDDMDTest.COLLECTION+ "."+ex.typeName);
          } catch (Exception e) { 
          }
          sql="CREATE DISTINCT TYPE " + JDDMDTest.COLLECTION+"."+ex.typeName+" "+ex.declaration;
          s.executeUpdate(sql); 
          sql="getUDTS";
	  ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                                             ex.typeName, null);
	        sql="rs.next()";
          boolean more = rs.next(); 
          while (more) {
            sql="getString";
            String typeName = rs.getString(3); 
            if (ex.typeName.equals(typeName)) {
              rowFound = true;
              for (column = 4; column <= maxColumn; column++) {
                sql="checkString";
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
	  if (success) { 
	      sql="DROP DISTINCT TYPE "   + JDDMDTest.COLLECTION+ "."+ex.typeName;
	      s.executeUpdate(sql);
	  }
          assertCondition(rowFound && success, "rowFound="+rowFound+" Test for "+ex.typeName+added+errorMessage+ "\n     =====> type not dropped: "+JDDMDTest.COLLECTION+ "."+ex.typeName); 
          
        } catch (Exception e) { 
          failed (e, added +" column="+column+" sql="+sql);
          
        }
        
      
    }


        /**
         * Test the ROWID data type 
        **/

       public void Var038()
        {
         if (checkJdbc40()) { 
	   if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	       notApplicable("V7R1 test for ROWID");
	       return; 
	   }

          ExpectedTypeFormat ex = new ExpectedTypeFormat(
              /* name */     "JDDMDGUROWID",
                             " AS ROWID ",
              /*className*/ "byte[]",
              /*dataType*/   "2001",  /*Types.distinct  updated 9/19/2013 for v7r2 */ 
              /*remarks*/    null,
              /*baseType*/   "-3"   /* VARBINARY */ 
          ); 
          
	 

	  testDataType(ex, " -- Added 01/28/09 by native driver");

         }
        }




        /**
         * Test the XML data type 
        **/

       public void Var039()
        {

	   if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	       notApplicable("V7R1 test for XML");
	       return; 
	   }

          ExpectedTypeFormat ex = new ExpectedTypeFormat(
              /* name */     "JDDMDGUXML",
                             " AS XML ",
              /*className*/ "java.sql.SQLXML",
              /*dataType*/   "2001",  /* UDT*/ 
              /*remarks*/    null,
              /*baseType*/   "2009"  /*SQLXML */ 
          ); 
          

	  testDataType(ex, " -- Added 01/28/09 by native driver");

        }



/**
getUDTs() - Run getUDTs multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE. 

**/
    public void Var040()  
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
		    // System.out.println("Calling getUDTs");
                    ResultSet rs = dmd_.getUDTs (null, JDDMDTest.COLLECTION,
                            "UDTS%", null);

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
getUDTs() - Check readonly conneciton 
**/
    public void Var041()
    {
        if (checkJdbc20 ()) {
            try {
                Connection c;
                // SQL400 - native driver gets unhappy when you specify a 
                //          value such as *LIBL in your libraries list.
                if (getDriver() == JDTestDriver.DRIVER_JCC) { 
                  c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
                } else if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
                    c = testDriver_.getConnection (baseURL_
                                                   + ";access=read only;libraries=*LIBL,QIWS," + JDDMDTest.COLLECTION + ","
                                                   + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                                                   userId_, encryptedPassword_);
                else
                    c = testDriver_.getConnection (baseURL_
                                                   + ";access=read only;libraries=QIWS," + JDDMDTest.COLLECTION + ","
                                                   + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                                                   userId_, encryptedPassword_);

                DatabaseMetaData dmd = c.getMetaData ();
                ResultSet rs = dmd.getUDTs (null, null, null, null);

                // It is impossible to check that all types come back,
                // so we just check that at least some of them come back.
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    String type = rs.getString ("TYPE_SCHEM")
                                  + "." + rs.getString ("TYPE_NAME");
                    if (type.equals (JDDMDTest.COLLECTION + ".UDTS"))
                        check1 = true;
                    else if (type.equals (JDDMDTest.COLLECTION2 + ".UDTS3"))
                        check2 = true;
                    else if (type.equals (JDDMDTest.COLLECTIONXX + ".UDTSXX"))
                        check3 = true;
                }

                rs.close ();
                c.close ();
                if (areLobsSupported ())
                    assertCondition (check1 && check2 && check3, JDDMDTest.COLLECTION+".UDTS found="+check1+" "+JDDMDTest.COLLECTION+".UDTS3 found = "+check2+" "+JDDMDTest.COLLECTION+".UDTSXX found = "+check3 );
                else
                    assertCondition (rows == 0, "Rows = "+rows+" sb 0 when all null parameters specified" );
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
     * Test the BOOLEAN data type 
    **/

   public void Var042()
    {

     if (checkBooleanSupport()) {


      ExpectedTypeFormat ex = new ExpectedTypeFormat(
          /* name */     "JDDMDGUBOL",
                         " AS BOOLEAN ",
          /*className*/ "java.lang.Boolean",
          /*dataType*/   "2001",  /* UDT */ 
          /*remarks*/    null,
          /*baseType*/   "16"  /*BOOLEAN */ 
      ); 
      

testDataType(ex, " -- Added 01/08/21 ");
     }

    }





}



