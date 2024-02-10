///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetVersionColumns.java
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
 // File Name:    JDDMDGetVersionColumns.java
 //
 // Classes:      JDDMDGetVersionColumns
 //
 ////////////////////////////////////////////////////////////////////////
 //
 // Note:  GetVersionColumns was completely wrong before native switched
 //        to use the metadata support.
 //        To get an answer we must use the ROWID support on iSeries
 // Note2: In Dec 2008, Mark Anderson fixed the SYSIBM procedure in SI34052.
 //        A row will only be returned if it is a ROW CHANGE TIMESTAMP
 //
 //  To see possible values run the following query.
 //  select * from SYSIBM.SQLSpecialColumns
 //  Note: This query fails on LUW 9.1
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




/**
Testcase JDDMDGetVersionColumns.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getVersionColumns()
</ul>
**/
public class JDDMDGetVersionColumns
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private String              connectionCatalog_; 
    private String changeType = "NOT SET"; 
    StringBuffer message = new StringBuffer(); 


/**
Constructor.
**/
    public JDDMDGetVersionColumns (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetVersionColumns",
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
            + ";libraries=" + JDDMDTest.COLLECTION2 + " "
            + JDDMDTest.COLLECTION, 
            userId_, encryptedPassword_);
        }
        dmd_ = connection_.getMetaData ();
        connectionCatalog_ = connection_.getCatalog(); 
	if (connectionCatalog_ == null ) {
	    if (JDTestDriver.isLUW()) {
	    // Leave connectionCatalog_ as null for LUW
		connectionCatalog_=null; 
	    } else { 
		connectionCatalog_ = getCatalogFromURL(baseURL_);
	    }
	}

        Statement s = connection_.createStatement ();


	// Make sure all the tables are dropped. 

	if (JDTestDriver.isLUW()) {
	    try { s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION + ".VC1 DROP PRIMARY KEY ");   } catch (Exception e) { }
	    try { s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION + ".VC2 DROP PRIMARY KEY ");    } catch (Exception e) { }
	    try { s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2 + ".VC1 DROP PRIMARY KEY ");    } catch (Exception e) { }
	} else { 
	    if (isSysibmMetadata()) {
	      // Nothing to do here. 
	    } else {
		try { 
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".VC1 DROP PRIMARY KEY CASCADE");
		} catch (Exception e) {} 
		try {
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".VC2 DROP PRIMARY KEY CASCADE");
		} catch (Exception e) {} 
		try {
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
				 + ".VC1 DROP PRIMARY KEY CASCADE");
		} catch (Exception e) {} 
	    }
	}

	//@C1A
	if(getRelease() >= JDTestDriver.RELEASE_V5R4M0 && (!JDTestDriver.isLUW())){
	    if (!isSysibmMetadata()) { 
		try {
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".LCNVC2 DROP PRIMARY KEY CASCADE");
		} catch (Exception e) {} 
	    }
	    try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".LCNVC2");
	    } catch (Exception e) {} 

	}

	try { 
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".VC1");
	} catch (Exception e) { }
	try { 
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".VC2");
	} catch (Exception e) { }
	try { 
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
			     + ".VC1");
	} catch (Exception e) { }







        //


	if (JDTestDriver.isLUW()) {

        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".VC1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                         + "CONSTRAINT " 
                         + "VCKEY1 PRIMARY KEY (CUSTID))");
  
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".VC2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                         + "CONSTRAINT " 
                         + "VCKEY2 PRIMARY KEY (NAME))");

        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".VC1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                         + "CONSTRAINT "  
                         + "VCKEY3 PRIMARY KEY (ACCTNBR))");

	} else {

	    if (isSysibmMetadata()) {
		if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		    changeType = "TIMESTAMP FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL"; 
		    s.executeUpdate ( "CREATE TABLE " + JDDMDTest.COLLECTION
				     +".VC1 (CUSTID  TIMESTAMP FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL, "
				     +"NAME CHAR(10) NOT NULL, "
				     +"ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2))");
		    s.executeUpdate ( "CREATE TABLE " + JDDMDTest.COLLECTION
				     + ".VC2 (CUSTID  TIMESTAMP FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL, "
				     +"NAME CHAR(10) NOT NULL, "
				     +"ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2))");
		    s.executeUpdate ( "CREATE TABLE " + JDDMDTest.COLLECTION2
				     +".VC1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
				     +"ACCTNBR  TIMESTAMP FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL, BALANCE DEC (6,2))");
		} else {

		    changeType = "ROWID NOT NULL"; 
		    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
				     + ".VC1 (CUSTID ROWID NOT NULL, NAME CHAR(10) NOT NULL, "
				     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2))");

		    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
				     + ".VC2 (CUSTID ROWID NOT NULL, NAME CHAR(10) NOT NULL, "
				     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2))");

		    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
				     + ".VC1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
				     + "ACCTNBR ROWID NOT NULL, BALANCE DEC (6,2))");
		}

	    } else { 

		s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
				 + ".VC1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
				 + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
				 + "CONSTRAINT " + JDDMDTest.COLLECTION 
				 + ".VCKEY1 PRIMARY KEY (CUSTID))");

		s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
				 + ".VC2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
				 + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
				 + "CONSTRAINT " + JDDMDTest.COLLECTION 
				 + ".VCKEY2 PRIMARY KEY (NAME))");

		s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
				 + ".VC1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
				 + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
				 + "CONSTRAINT " + JDDMDTest.COLLECTION2 
				 + ".VCKEY3 PRIMARY KEY (ACCTNBR))");
	    }
	}
  
        //@C1A
	if (isSysibmMetadata()) {
	    if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {


		s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
				 + ".LCNVC2 (CUSTID INT NOT NULL, " +
				 "THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST TIMESTAMP FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL, "
				 + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2))");

	    } else { 
		s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
				 + ".LCNVC2 (CUSTID INT NOT NULL, " +
				 "THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST ROWID NOT NULL, "
				 + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2))");
	    }

	} else { 
	    if(getRelease() >= JDTestDriver.RELEASE_V5R4M0 && (!JDTestDriver.isLUW())){
		s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
				 + ".LCNVC2 (CUSTID INT NOT NULL, THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST CHAR(10) NOT NULL, "
				 + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
				 + "CONSTRAINT " + JDDMDTest.COLLECTION 
				 + ".LCNVCKEY2 PRIMARY KEY (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST))");
	    }
	}
        s.close ();
	try { 
        connection_.commit(); // for xa
	} catch(Exception e) {} 
        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        closedConnection_.close ();        
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
				 + ".VC1 DROP PRIMARY KEY ");
	    } catch (Exception e) { e.printStackTrace();}
	    try { 
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".VC2 DROP PRIMARY KEY ");
	    } catch (Exception e) { e.printStackTrace();}
	    try { 
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
			     + ".VC1 DROP PRIMARY KEY ");
	    } catch (Exception e) { e.printStackTrace();}
	} else { 
          if (isSysibmMetadata()) {
              // Nothing to do here. 
          } else { 
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".VC1 DROP PRIMARY KEY CASCADE");
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".VC2 DROP PRIMARY KEY CASCADE");
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
			     + ".VC1 DROP PRIMARY KEY CASCADE");
          }
	}
        
        //@C1A
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0 && (!JDTestDriver.isLUW())){
            if (!isSysibmMetadata()) { 
               s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
                             + ".LCNVC2 DROP PRIMARY KEY CASCADE");
            }
            s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
                             + ".LCNVC2");
        }

	try { 
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".VC1");
	} catch (Exception e) { e.printStackTrace();}
	try { 
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".VC2");
	} catch (Exception e) { e.printStackTrace();}
	try { 
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
			     + ".VC1");
	} catch (Exception e) { e.printStackTrace();}


        s.close ();
	try { 
	    connection_.commit(); // for xa
	} catch (Exception e) {
	} 
        connection_.close ();
    }



/**
getVersionColumns() - Check the result set format.
**/
    public void Var001()
    {
      message.setLength(0); 
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_, 
                                                  JDDMDTest.COLLECTION, "VC1");
            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "SCOPE", "COLUMN_NAME",
                                       "DATA_TYPE", "TYPE_NAME",
                                       "COLUMN_SIZE", "BUFFER_LENGTH",
                                       "DECIMAL_DIGITS", "PSEUDO_COLUMN" };            
            int[] expectedTypes = { Types.SMALLINT, Types.VARCHAR,
                                    Types.SMALLINT, Types.VARCHAR,
                                    Types.INTEGER, Types.INTEGER,
                                    Types.SMALLINT, Types.SMALLINT };

            int[] expectedTypesJCC = { Types.SMALLINT, Types.VARCHAR,
                Types.SMALLINT, Types.LONGVARCHAR,
                Types.INTEGER, Types.INTEGER,
                Types.SMALLINT, Types.SMALLINT };
            
            int[] expectedTypesTBV7R1 = { Types.SMALLINT, Types.VARCHAR,
                    Types.INTEGER, Types.VARCHAR,
                    Types.INTEGER, Types.INTEGER,
                    Types.SMALLINT, Types.SMALLINT };
            
            if (getDriver() == JDTestDriver.DRIVER_JCC ) {
                if (! JDTestDriver.isLUW()) { 
                    expectedTypes = expectedTypesJCC;
                }
	    } else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
                    expectedTypes = expectedTypesJCC;
            }else if( getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
                expectedTypes = expectedTypesTBV7R1;
            } else if (getRelease() >= JDTestDriver.RELEASE_V5R5M0 &&
		       getDriver() == JDTestDriver.DRIVER_NATIVE) {
		// V7R1 changes moved to V6R1 01/11/2010 
                expectedTypes = expectedTypesTBV7R1;
	    }
	    
            int count = rsmd.getColumnCount ();
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);
            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);

            rs.close ();
            assertCondition ((count == 8) && (namesCheck) && (typesCheck), 
                "\ncount="+count+" sb 8" +
                "\nnamesCheck="+namesCheck+" typesCheck="+typesCheck+" message="+message );
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getVersionColumns() - Verify all columns.
**/
    public void Var002()
    {
      message.setLength(0); 
      message.append("Change type is '"+changeType+"'"); 
        try {
	    boolean isTimestamp = (getRelease() >= JDTestDriver.RELEASE_V5R5M0);
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_, 
                                                  JDDMDTest.COLLECTION, "VC1");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;

                short scope           = rs.getShort ("SCOPE");
                String columnName     = rs.getString ("COLUMN_NAME");
                short dataType        = rs.getShort ("DATA_TYPE");
                String typeName       = rs.getString ("TYPE_NAME");
                int columnSize        = rs.getInt ("COLUMN_SIZE");
                int bufferLength      = rs.getInt ("BUFFER_LENGTH");
                short decimalDigits   = rs.getShort ("DECIMAL_DIGITS");
                short pseudoColumn    = rs.getShort ("PSEUDO_COLUMN");

                // System.out.println (scope + ":" + columnName + ":" + dataType + ":" + typeName + ":");
                // System.out.println (columnSize + ":" + bufferLength + ":" + decimalDigits + ":" + pseudoColumn + ":");
                if (! (scope == 0)) {
                  success=false; 
                  message.append("\nscope="+scope+" sb 0"); 
                }
                if (isSysibmMetadata()) { 
                  if (!(pseudoColumn == DatabaseMetaData.versionColumnPseudo)) {
                    success=false;
                    message.append("\npseudoColumn="+pseudoColumn+" sb "+DatabaseMetaData.versionColumnPseudo); 
                  }
                  
                } else { 
                  if (!(pseudoColumn == DatabaseMetaData.versionColumnNotPseudo)) {
                    success=false;
                    message.append("\npseudoColumn="+pseudoColumn+" sb "+DatabaseMetaData.versionColumnNotPseudo); 
                  }
                }

                if (columnName.equals ("CUSTID")) {
		    if (isSysibmMetadata()) {

			if (isTimestamp) {
			    if (!(dataType == 93)) {
				success=false; 
				message.append("\ndataType="+dataType+" sb 1111"); 
			    }
			    if (!(typeName.equals ("TIMESTAMP"))) {
				success=false; 
				message.append("\ntypeName="+typeName+" sb TIMESTAMP"); 
			    }
			    if (!(columnSize == 26)) { 
				success=false; 
				message.append("\ncolumnSize="+columnSize+" sb 26"); 
			    }
			    if (!(bufferLength == 16)) {
				success=false; 
				message.append("\nFor CUSTID/TIMESTAMP bufferLength="+bufferLength+" sb 16"); 
			    }
			    if (! (decimalDigits == 6)) {
				success=false; 
				message.append("\ndecimalDigits="+decimalDigits+" sb 6"); 
			    }

			} else { 
			    if (!(dataType == 1111)) {
				success=false; 
				message.append("\ndataType="+dataType+" sb 111"); 
			    }
			    if (!(typeName.equals ("ROWID"))) {
				success=false; 
				message.append("\ntypeName="+typeName+" sb ROWID"); 
			    }
			    if (!(columnSize == 40)) { 
				success=false; 
				message.append("\ncolumnSize="+columnSize+" sb 40"); 
			    }
			    if (!(bufferLength == 40)) {
				success=false; 
				message.append("\nbufferLength="+bufferLength+" sb 40"); 
			    }
			    if (! (decimalDigits == 0)) {
				success=false; 
				message.append("\ndecimalDigits="+decimalDigits+" sb 0"); 
			    }
			}

		    } else { 
			if (!(dataType == Types.INTEGER)) {
			    success=false; 
			    message.append("\ndataType="+dataType+" sb "+Types.INTEGER); 
			}
			if (!(typeName.equals ("INTEGER"))) {
			    success=false; 
			    message.append("\ntypeName="+typeName+" sb INTEGER"); 
			}
			if (!(columnSize == 10)) { 
			    success=false; 
			    message.append("\ncolumnSize="+columnSize+" sb 10"); 
			}
			if (!(bufferLength == 4)) {
			    success=false; 
			    message.append("\nbufferLength="+bufferLength+" sb 4"); 
			}
			if (! (decimalDigits == 0)) {
			    success=false; 
			    message.append("\ndecimalDigits="+decimalDigits+" sb 0"); 
			}
		    }
                }
                else {
                    success = false;  // Tighten down the testing more.
                    message.append("\nUnexpected column "+columnName+" found"); 
                }
            }

            rs.close ();
	    if (JDTestDriver.isLUW()) {
		assertCondition ((rows == 0) && success, "Rows="+rows+" sb 0 "+message);
	    } else if (getDriver() ==  JDTestDriver.DRIVER_TOOLBOX &&  ( getRelease()== JDTestDriver.RELEASE_V6R1M0 || getRelease()== JDTestDriver.RELEASE_V5R5M0)) {//per issue 40138 for v6r1 non-metadata TB
            assertCondition ((rows == 0) && success, "Rows="+rows+" sb 0 "+message);  
        } else { 
		assertCondition ((rows == 1) && success, "Rows="+rows+" sb 1 "+message);
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }

       
/**
getVersionColumns() - Specify all null parameters.  Verify no rows are returned.
**/
    public void Var003()
    {
        try {
            ResultSet rs; 
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              rs = dmd_.getVersionColumns (null, null, "NOEXIST3143234234234");
                  
            } else {
             rs = dmd_.getVersionColumns (null, null, null);
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
getVersionColumns() - Specify null for the catalog.  
**/
    public void Var004()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (null, JDDMDTest.COLLECTION, 
                                                   "VC2");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (isSysibmMetadata() ) {
                  if (columnName.equals ("CUSTID"))
                    check1 = true;
                } else { 
                  if (columnName.equals ("NAME"))
                    check1 = true;
                }
            }

            rs.close ();
	    if (JDTestDriver.isLUW()) {
		assertCondition ((rows == 0), " rows="+rows+" sb 0");
        } else if (getDriver() ==  JDTestDriver.DRIVER_TOOLBOX && (getRelease()== JDTestDriver.RELEASE_V6R1M0 || getRelease()== JDTestDriver.RELEASE_V5R5M0)) {//per issue 40138 for v6r1 non-metadata TB
            assertCondition ((rows == 0), " rows="+rows+" sb 0");
        } else { 
		assertCondition (check1 && (rows == 1), "NAME found="+check1+" rows="+rows+" sb 1");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getVersionColumns() - Specify empty string for the catalog.
**/
    public void Var005()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns ("", JDDMDTest.COLLECTION, "VC1");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            if (isSysibmMetadata()) {
              assertCondition (rows > 0, "rows = "+rows+" sb > 0 since empty catalog  and sysibm metadata " );
            } else { 
              assertCondition (rows == 0, "rows = "+rows+" sb 0 since empty catalog " );
            }   
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getVersionColumns() - Specify a catalog that matches the catalog
exactly.  
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_, 
                                                 JDDMDTest.COLLECTION2, "VC1");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("ACCTNBR"))
                    check1 = true;
            }

            rs.close ();
	    if (JDTestDriver.isLUW()) {
		assertCondition ((rows == 0), " rows="+rows+" sb 0");
	    } else if (getDriver() ==  JDTestDriver.DRIVER_TOOLBOX && (getRelease()== JDTestDriver.RELEASE_V6R1M0 || getRelease()== JDTestDriver.RELEASE_V5R5M0)) {//per issue 40138 for v6r1 non-metadata TB
	        assertCondition ((rows == 0), " rows="+rows+" sb 0");
	    } else { 
		assertCondition (check1 && (rows == 1), "ACCTNBR found="+check1+" rows="+rows+" sb 1");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getVersionColumns() - Specify "localhost" for the catalog.  
**/
    public void Var007()
    {
        if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4  ||
                ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease()>= JDTestDriver.RELEASE_V5R5M0)) ||
                (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
            notApplicable("\"localhost\" variation ");
        } else {

            try {
                ResultSet rs = dmd_.getVersionColumns ("localhost", 
                        JDDMDTest.COLLECTION, "VC1");

                boolean check1 = false;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    String columnName = rs.getString ("COLUMN_NAME");
                    if (columnName.equals ("CUSTID"))
                        check1 = true;
                }

                rs.close ();
               if (getDriver() ==  JDTestDriver.DRIVER_TOOLBOX && (getRelease()== JDTestDriver.RELEASE_V6R1M0 || getRelease()== JDTestDriver.RELEASE_V5R5M0)) {//per issue 40138 for v6r1 non-metadata TB
                   assertCondition (!check1 && (rows == 0), "CUSTID found="+check1+" rows="+rows+" sb 0");
                }
               else
                   assertCondition (check1 && (rows == 1), "CUSTID found="+check1+" rows="+rows+" sb 1");
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getVersionColumns() - Specify a catalog pattern for which there is a
match.  No matching rows should be returned, since we do not
support catalog pattern.
**/
    public void Var008()
    {
        try {
            ResultSet rs;
	    if (JDTestDriver.isLUW()) {
		rs = dmd_.getVersionColumns ("BOGUS%",
					     JDDMDTest.COLLECTION, "VC1");
	    } else { 
		rs = dmd_.getVersionColumns (connectionCatalog_.substring (0, 4) + "%",
					     JDDMDTest.COLLECTION, "VC1");
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
getVersionColumns() - Specify a catalog for which there is no match.
No matching columns should be returned.
**/
    public void Var009()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns ("BOGUS",
                JDDMDTest.COLLECTION, "VC1");

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
getVersionColumns() - Specify null for the schema.  
**/
    public void Var010()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_, 
                                                 null, "VC1");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("ACCTNBR"))
                    check1 = true;
            }

            rs.close ();
            if (JDTestDriver.isLUW()) {
                assertCondition ( (rows == 0), " rows="+rows+" sb 0");
            }else if (getDriver() ==  JDTestDriver.DRIVER_TOOLBOX && ( getRelease()== JDTestDriver.RELEASE_V6R1M0 || getRelease()== JDTestDriver.RELEASE_V5R5M0)) {//per issue 40138 for v6r1 non-metadata TB
                assertCondition ( (rows == 0), " rows="+rows+" sb 0");
            } else { 
                assertCondition (check1 && (rows >= 1), "ACCTNBR found="+check1+" rows="+rows+" sb >=  1");
            }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
       }



/**
getVersionColumns() - Specify empty string for the schema.
**/
    public void Var011()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_,
                "", "VC1");

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
getVersionColumns() - Specify a schema that matches the schema
exactly.  
**/
    public void Var012()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_, 
                                                 JDDMDTest.COLLECTION, "VC2");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (isSysibmMetadata() ) {
                  if (columnName.equals ("CUSTID"))
                    check1 = true;
                } else { 
                  if (columnName.equals ("NAME"))
                    check1 = true;
                }
            }

            rs.close ();
            if (JDTestDriver.isLUW()) {
                assertCondition ((rows == 0), " rows="+rows+" sb 0");
            }else if (getDriver() ==  JDTestDriver.DRIVER_TOOLBOX && ( getRelease()== JDTestDriver.RELEASE_V6R1M0 || getRelease()== JDTestDriver.RELEASE_V5R5M0)) {//per issue 40138 for v6r1 non-metadata TB
                assertCondition ((rows == 0), " rows="+rows+" sb 0");
            } else { 
                assertCondition (check1 && (rows == 1), "NAME found="+check1+" rows="+rows+" sb 1");
            }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getVersionColumns() - Specify a schema pattern for which there is a
match.  An exception should be thrown, since we do not
support schema pattern.

SQL400 - When you give the native driver a search pattern and they are not
         supported, we are simple going to return 0 rows.  The name isn't 
         found by the CLI and therefore, nothing is returned.
**/
    public void Var013()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_,
                JDDMDTest.SCHEMAS_PERCENT, "VC1");

            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
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
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            else
                failed (e, "Unexpected Exception");
        }
    }



/**
getVersionColumns() - Specify a schema for which there is no match.
No rows should be returned.
**/
    public void Var014()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_,
                "BOGUS", "VC1");

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
getVersionColumns() - Specify null for the table.  
No rows should be returned.
**/
    public void Var015()
    {
        try {
          ResultSet rs;
          if (getDriver() == JDTestDriver.DRIVER_JCC ) {
            // JCC throws an exception if null specified 
            rs = dmd_.getVersionColumns (connectionCatalog_,
                JDDMDTest.COLLECTION, "NOTEXISTTABLE2342342");
          
          } else {
           rs = dmd_.getVersionColumns (connectionCatalog_,
                JDDMDTest.COLLECTION, null);
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
getVersionColumns() - Specify empty string for the table.
**/
    public void Var016()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_,
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
getVersionColumns() - Specify a table that matches the table
exactly.  All matching columns should be returned.
**/
    public void Var017()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_,
                JDDMDTest.COLLECTION2, "VC1");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("ACCTNBR"))
                    check1 = true;
            }

            rs.close ();
            if (JDTestDriver.isLUW()) {
                assertCondition ((rows == 0), " rows="+rows+" sb 0");
            }else if (getDriver() ==  JDTestDriver.DRIVER_TOOLBOX && (getRelease()== JDTestDriver.RELEASE_V6R1M0 || getRelease()== JDTestDriver.RELEASE_V5R5M0)) {//per issue 40138 for v6r1 non-metadata TB
                assertCondition ((rows == 0), " rows="+rows+" sb 0");
            } else { 
                assertCondition (check1 && (rows == 1), "ACCTNBR found="+check1+" rows="+rows+" sb 1");
            }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getVersionColumns() - Specify a table pattern for which there is a
match.  An exception should be thrown, since we do not
support table pattern.

SQL400 - When you give the native driver a search pattern and they are not
         supported, we are simple going to return 0 rows.  The name isn't 
         found by the CLI and therefore, nothing is returned.
**/
    public void Var018()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_,
                JDDMDTest.COLLECTION, "VC%");

            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
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
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            else
                failed (e, "Unexpected Exception");
        }
    }



/**
getVersionColumns() - Specify a table for which there is no match.
No rows should be returned.
**/
    public void Var019()
    {
        try {
            ResultSet rs = dmd_.getVersionColumns (connectionCatalog_,
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
getVersionColumns() - Specify null for the catalog.  retrieve a 128 byte column name.  
**/
    public void Var020()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0 && (!JDTestDriver.isLUW())){
            try {
                ResultSet rs = dmd_.getVersionColumns (null, JDDMDTest.COLLECTION, 
                                                       "LCNVC2");
                String columnName="NOT SET"; 
                boolean check1 = false;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    columnName = rs.getString ("COLUMN_NAME");
                    if (columnName.equals ("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST"))
                        check1 = true;
                }

                rs.close ();
                if (getDriver() ==  JDTestDriver.DRIVER_TOOLBOX && (getRelease()== JDTestDriver.RELEASE_V6R1M0 || getRelease()== JDTestDriver.RELEASE_V5R5M0)) {//per issue 40138 for v6r1 non-metadata TB
                    assertCondition (!check1 && (rows == 0), "should return 0");
                }
                else
                { 
                    assertCondition (check1 && (rows == 1), 
                    "columnName="+columnName+" sb THISIS... rows="+rows+" sb 1 " +
                    "Added by Toolbox 8/12/2004 to test 128 byte column names.");
                }
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception. Added by Toolbox 8/12/2004 to test 128 byte column names.");
            }
        }
        else
            notApplicable("V5R4 or greater variation.");
    }


/**
getVersionColumns() - Run getVersionColumns multiple times.  Make sure there is not a handle leak.
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
		    // System.out.println("Calling getVersionColumns");
		    ResultSet rs = dmd_.getVersionColumns (connectionCatalog_,
							   JDDMDTest.COLLECTION2, "VC1");


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
getVersionColumns() - Readonly conneciton
**/
    public void Var022()
    {
        try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            ResultSet rs = dmd.getVersionColumns (null, JDDMDTest.COLLECTION, 
                                                   "VC2");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (isSysibmMetadata() ) {
                  if (columnName.equals ("CUSTID"))
                    check1 = true;
                } else { 
                  if (columnName.equals ("NAME"))
                    check1 = true;
                }
            }

            rs.close ();
	    c.close(); 
	    if (JDTestDriver.isLUW()) {
		assertCondition ((rows == 0), " rows="+rows+" sb 0");
        } else if (getDriver() ==  JDTestDriver.DRIVER_TOOLBOX && (getRelease()== JDTestDriver.RELEASE_V6R1M0 || getRelease()== JDTestDriver.RELEASE_V5R5M0)) {//per issue 40138 for v6r1 non-metadata TB
            assertCondition ((rows == 0), " rows="+rows+" sb 0");
        } else { 
		assertCondition (check1 && (rows == 1), "NAME found="+check1+" rows="+rows+" sb 1");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }




/**
getVersionColumns() - CPS AECP2A -- MCH using metadata source=0
**/
    public void Var023()
    {
	try {
	    Connection c = testDriver_.getConnection (baseURL_
						      + ";metadata source=0", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

	    ResultSet rs = dmd.getVersionColumns (null, JDDMDTest.COLLECTION, 
						  "VC2");
	    while(rs.next());
	    rs.close(); 
	    rs = dmd.getVersionColumns(null,"QSYS2","SYSTEXTINDEXES");
	    while(rs.next());
	    rs.close(); 
	    rs = dmd.getVersionColumns(null,"QSYS2","XSROBJECTS");
	    while (rs.next());
	    rs.close(); 
	    rs = dmd.getVersionColumns(null,"QSYS2","SYSTEXTINDEXES");
	    while (rs.next());
	    rs.close(); 
	    assertCondition (true, "");

	}
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }






}
