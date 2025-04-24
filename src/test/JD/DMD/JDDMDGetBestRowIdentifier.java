///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetBestRowIdentifier.java
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
 // File Name:    JDDMDGetBestRowIdentifier.java
 //
 // Classes:      JDDMDGetBestRowIdentifier
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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDDMDGetBestRowIdentifier.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getBestRowIdentifier()
</ul>
**/
public class JDDMDGetBestRowIdentifier
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetBestRowIdentifier";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }



    // Private data.
    private Connection          connection_;
    private String              catalog_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    StringBuffer message = new StringBuffer();



/**
Constructor.
**/
    public JDDMDGetBestRowIdentifier (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetBestRowIdentifier",
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

        if (getDriver()== JDTestDriver.DRIVER_JCC) {
          url = baseURL_;
        }
        connection_ = testDriver_.getConnection (url,
            userId_, encryptedPassword_);
        dmd_ = connection_.getMetaData ();

        catalog_ = connection_.getCatalog ();
        if (catalog_ == null) {
            catalog_ = system_.toUpperCase();
        }



        Statement s = connection_.createStatement ();
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION+".BR1");
	} catch (Exception e) {}

	if (JDTestDriver.isLUW()) {
	   // LUW requires NO NULL on CUSTID and no schema on the constraint name
	   s.executeUpdate ("SET SCHEMA "+JDDMDTest.COLLECTION);
           s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".BR1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
            + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
            + " CONSTRAINT BRKEY1 UNIQUE (CUSTID))");

	} else {
           s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".BR1 (CUSTID INT, NAME CHAR(10) NOT NULL, "
            + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
            + " CONSTRAINT " + JDDMDTest.COLLECTION + ".BRKEY1 UNIQUE (CUSTID))");
	}


	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION+".BR2");
	} catch (Exception e) {}
	if (JDTestDriver.isLUW()) {
	   // LUW requires  no schema on the constraint name
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".BR2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
			     + " CONSTRAINT BRKEY2 PRIMARY KEY (NAME))");

	} else {
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".BR2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
			     + " CONSTRAINT " + JDDMDTest.COLLECTION + ".BRKEY2 PRIMARY KEY (NAME))");
	}


	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2+".BR1");
	} catch (Exception e) {}
	if (JDTestDriver.isLUW()) {
	   // LUW requires  no schema on the constaint name
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
			     + ".BR1 (CUSTID INT, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
			     + " CONSTRAINT BRKEY3 UNIQUE (ACCTNBR))");
	} else {
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
			     + ".BR1 (CUSTID INT, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
			     + " CONSTRAINT " + JDDMDTest.COLLECTION2 + ".BRKEY3 UNIQUE (ACCTNBR))");
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2+".BR2");
	} catch (Exception e) {}
	if (JDTestDriver.isLUW()) {
	   // LUW requires  no schema on the constaint name and NOT NULL for balance column
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
			     + ".BR2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2) NOT NULL, "
			     + " CONSTRAINT BRKEY4 UNIQUE (BALANCE))");

	} else {
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
			     + ".BR2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
			     + " CONSTRAINT " + JDDMDTest.COLLECTION2 + ".BRKEY4 UNIQUE (BALANCE))");
	}

        //@C1A
        // Note LUW still has 30 column name limit

        if(!JDTestDriver.isLUW() && (getRelease() >= JDTestDriver.RELEASE_V7R1M0))
        {

	    try {
		s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION+".LCNTAB");
	    } catch (Exception e) {}


            s.executeUpdate("CREATE TABLE " + JDDMDTest.COLLECTION  //@C1A
                        + ".LCNTAB (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INTEGER PRIMARY KEY, " //@C1A
                        + " COL2 INTEGER)");  //@C1A
        }

        try {
           connection_.commit(); // for xa
        } catch (SQLException e) {
          // IGNORE.. JCC V9 doesn't permit commit on autocommit connection
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
	try {
	    if (JDTestDriver.isLUW()) {
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".BR1 DROP UNIQUE BRKEY1");

	    } else {
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".BR1 DROP UNIQUE " + JDDMDTest.COLLECTION + ".BRKEY1");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    if (JDTestDriver.isLUW()) {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".BR2 DROP PRIMARY KEY ");
	    } else {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".BR2 DROP PRIMARY KEY CASCADE");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    if (JDTestDriver.isLUW()) {
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
				 + ".BR1 DROP UNIQUE BRKEY3");

	    } else {
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
				 + ".BR1 DROP UNIQUE " + JDDMDTest.COLLECTION2 + ".BRKEY3");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    if (JDTestDriver.isLUW()) {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
			     + ".BR2 DROP UNIQUE BRKEY4");
	    } else {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
			     + ".BR2 DROP UNIQUE " + JDDMDTest.COLLECTION2 + ".BRKEY4");
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".BR1");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".BR2");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
			     + ".BR1");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
			     + ".BR2");
	} catch (Exception e) {
	    e.printStackTrace();
	}

	//@C1A
	if((!JDTestDriver.isLUW()) && getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	    try {
		s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION   //@C1A
				 + ".LCNTAB");                          //@C1A
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

        s.close ();
        connection_.commit(); // for xa
        connection_.close ();
    }



/**
getBestRowIdentifier() - Check the result set format.
**/
    public void Var001()
    {
        try {
          ResultSet rs;
          message.setLength(0);
            // According to the JDBC SPEC, the tableName parameter must match the
            // table name as stored in the database.  This testcase was changed
            // 4/26/2006 to specific the SYSDUMMY1 name as the table name
            // Note:  A test for null as the table name is given below in variation 003
            rs = dmd_.getBestRowIdentifier (null, null, "SYSDUMMY1",
                  DatabaseMetaData.bestRowTemporary, true);
            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "SCOPE", "COLUMN_NAME",
                                       "DATA_TYPE", "TYPE_NAME",
                                       "COLUMN_SIZE", "BUFFER_LENGTH",
                                       "DECIMAL_DIGITS", "PSEUDO_COLUMN" };
            int[] expectedTypesBase = { Types.SMALLINT, Types.VARCHAR,
                                    Types.SMALLINT, Types.VARCHAR,
                                    Types.INTEGER, Types.INTEGER,
                                    Types.SMALLINT, Types.SMALLINT };
            int[] expectedTypesJCC = { Types.SMALLINT, Types.VARCHAR,
                                       Types.SMALLINT, Types.LONGVARCHAR,
                                       Types.INTEGER, Types.INTEGER,
                                       Types.SMALLINT, Types.SMALLINT };

            int[] expectedTypesV7R1 = { Types.SMALLINT, Types.VARCHAR,
                    Types.INTEGER, Types.VARCHAR,
                    Types.INTEGER, Types.INTEGER,
                    Types.SMALLINT, Types.SMALLINT };



            int[] expectedTypes = expectedTypesBase;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              expectedTypes = expectedTypesJCC;
            }


            if( getRelease() >= JDTestDriver.RELEASE_V7R1M0)
                expectedTypes=expectedTypesV7R1;

	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 &&
		getDriver() == JDTestDriver.DRIVER_NATIVE) {
		/* SYSIBM procedures fixed in V6R1 on 1/11/2010 */
                expectedTypes=expectedTypesV7R1;
	    }

            int count = rsmd.getColumnCount ();
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);
            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes,message);

            rs.close ();
            assertCondition ((count == 8) && (namesCheck) && (typesCheck),
                "count="+count+" sb 8 namesCheck="+namesCheck+" typesCheck="+typesCheck+"\n"+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Verify all columns.

SQL400 - the native driver is going to set the buffer length below to 4 and the columnSize
         to 10, the toolbox is going to return the bufferLength as 0 and the columnSize as
         4.
**/
    public void Var002()
    {
      message.setLength(0);
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (null, JDDMDTest.COLLECTION,
                "BR1", DatabaseMetaData.bestRowTemporary, true);
            message.append("Calling getBestRowIdentifier with (null, "+JDDMDTest.COLLECTION+
                     ", BR1, bestRowTemporary, true)");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;

                short scope             = rs.getShort ("SCOPE");
                String columnName       = rs.getString ("COLUMN_NAME");
                short dataType          = rs.getShort ("DATA_TYPE");
                String typeName         = rs.getString ("TYPE_NAME");
                int columnSize          = rs.getInt ("COLUMN_SIZE");
                int bufferLength        = rs.getInt ("BUFFER_LENGTH");
                short decimalDigits     = rs.getShort ("DECIMAL_DIGITS");
                short pseudoColumn      = rs.getShort ("PSEUDO_COLUMN");

                if (!(scope == DatabaseMetaData.bestRowTemporary)) {
                  success=false;
                  message.append("\nscope="+scope+"sb "+DatabaseMetaData.bestRowTemporary);
                }
                if (!("CUSTID".equals(columnName))){
                  success=false;
                  message.append("\ncolumnName="+columnName+" sb CUSTID");
                }

                if (!(dataType == Types.INTEGER)){
                  success=false;
                  message.append("\ndataType="+dataType+" sb "+Types.INTEGER);
                }

                if (!("INTEGER".equals(typeName))){
                  success=false;
                  message.append("\ntypeName="+typeName+" sb INTEGER");
                }


                if ((getDriver () == JDTestDriver.DRIVER_TOOLBOX) &&  isSysibmMetadata()==false) //@mdsp
                {
                    if (!(columnSize == 4)){
                      success=false;
                      message.append("\ncolumnSize="+columnSize+" sb 4");
                    }

                    if (!(bufferLength == 0)){
                      success=false;
                      message.append("\nbufferLength="+bufferLength+"sb 0");
                    }

                }
                else
                {
                    if (!(columnSize == 10)){
                      success=false;
                      message.append("\ncolumnSize="+columnSize+" sb 10");
                    }

                    if (!(bufferLength == 4)){
                      success=false;
                      message.append("\nbufferLength="+bufferLength+"sb 4");
                    }

                }

                if (!(decimalDigits == 0)){
                  success=false;
                  message.append("\ndecimalDigits="+decimalDigits+" sb 0");
                }

                if (!(pseudoColumn == DatabaseMetaData.bestRowNotPseudo)){
                  success=false;
                  message.append("\npseudoColumn="+pseudoColumn+" sb "+DatabaseMetaData.bestRowNotPseudo);
                }

            }

            rs.close ();
            assertCondition ((rows == 1) && success, " rows = " + rows + " AND SHOULD BE 1 and success = " + success + " AND SHOULD BE true "+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify all null parameters.  Verify no rows are returned.
**/
    public void Var003() {
    try {
      ResultSet rs;

      rs = dmd_.getBestRowIdentifier(null, null, null,
            DatabaseMetaData.bestRowTemporary, false);

      int rows = 0;
      while (rs.next())
        ++rows;

      rs.close();
      assertCondition(rows == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- COMPATIBILITY jcc driver throws exception when table name is null ");
    }
  }



/**
 * getBestRowIdentifier() - Specify null for the catalog.
 */
    public void Var004()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (null,
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowTemporary,
                true);

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("CUSTID"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" rows="+rows+"sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify empty string for the catalog.
**/
    public void Var005()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier ("",
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowTemporary,
                true);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (isSysibmMetadata()) {
		assertCondition (rows > 0, "Empty string specified for catalog, rows should have been found ");

	    } else {
		assertCondition (rows == 0, "Empty string specified for catalog, no rows should have been found");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify a catalog_ that matches the catalog
exactly.  All matching rows should be returned.
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (connection_.getCatalog (),
                JDDMDTest.COLLECTION, "BR2", DatabaseMetaData.bestRowTemporary,
                true);

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("NAME"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" rows="+rows+"sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify "localhost" for the catalog.  All
matching rows should be returned.
**/
    public void Var007()
    {
      if ( (getJdbcLevel() >= 4) || (getDriver() == JDTestDriver.DRIVER_JCC) ||
            ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V7R1M0))) {
        notApplicable("COMPATIBILITY localhost variation");
      } else {

        try {
            String catalog = "localhost";
            if(isSysibmMetadata())
                catalog="";
            ResultSet rs = dmd_.getBestRowIdentifier (catalog,
                JDDMDTest.COLLECTION, "BR2", DatabaseMetaData.bestRowTemporary,
                true);

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("NAME"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" rows="+rows+"sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
getBestRowIdentifier() - Specify a catalog pattern for which there is a
match.  No matching rows should be returned, since we do not
support catalog pattern.
**/
    public void Var008()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_.substring (0, 4) + "%",
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowTemporary,
                true);

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
getBestRowIdentifier() - Specify a catalog for which there is no match.
No matching columns should be returned.
**/
    public void Var009()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier ("BOGUS",
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowTemporary,
                true);

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
getBestRowIdentifier() - Specify null for the schema.
**/
    public void Var010()
    {
	if (getDriver()  == JDTestDriver.DRIVER_NATIVE &&
	    getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in pree V5R5 native code");
	    return;
	}
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                null, "BR1", DatabaseMetaData.bestRowTemporary,
                true);
            message.setLength(0);
            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("CUSTID"))
                    check1 = true;
                message.append("\n row: "+rows+" "+
                rs.getString(1)+","+
                rs.getString(2)+","+
                rs.getString(3)+","+
                rs.getString(4)+","+
                rs.getString(5)+","+
                rs.getString(6)+","+
                rs.getString(7)+","+
                rs.getString(8));

            }

            rs.close ();
            if ((getDriver () == JDTestDriver.DRIVER_TOOLBOX) && isSysibmMetadata()==false) //toolbox not changed
                assertCondition (check1 && (rows == 1), "check1="+check1+" rows="+rows+"sb 1 -- When schema = null.  Updated 04/26/2006 to really pass null"+message);
            else
                assertCondition (check1 && (rows >= 2), "check1="+check1+" rows="+rows+"sb >=2 -- When schema = null.  Updated 04/26/2006 to really pass null"+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify empty string for the schema.
**/
    public void Var011()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                "", "BR1", DatabaseMetaData.bestRowTemporary,
                true);

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
getBestRowIdentifier() - Specify a schema that matches the schema
exactly.
**/
    public void Var012()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION2, "BR1", DatabaseMetaData.bestRowTemporary,
                true);

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("ACCTNBR"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" rows="+rows+"sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify a schema pattern for which there is a
match.  An exception should be thrown, since we do not
support schema pattern.

SQL400 - When you give the native driver a search pattern and they are not
         supported, we are simple going to return 0 rows.  The name isn't
         found by the CLI and therefore, nothing is returned.
**/
    public void Var013()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.SCHEMAS_PERCENT, "BR1", DatabaseMetaData.bestRowTemporary,
                true);

            if ((getDriver () == JDTestDriver.DRIVER_TOOLBOX) && isSysibmMetadata()==false)
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
            if ((getDriver () == JDTestDriver.DRIVER_TOOLBOX)&& isSysibmMetadata()==false)
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            else
                failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify a schema for which there is no match.
No rows should be returned.
**/
    public void Var014()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                "BOGUS", "BR1", DatabaseMetaData.bestRowTemporary,
                true);

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
getBestRowIdentifier() - Specify null for the table.
No rows should be returned.
**/
    public void Var015()
    {
        try {
          ResultSet rs;
           rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION, null, DatabaseMetaData.bestRowTemporary,
                true);
            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0);
        }
        catch (Exception e)  {
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              failed (e, "Unexpected Exception");
            } else {
               failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBestRowIdentifier() - Specify empty string for the table.
**/
    public void Var016()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION, "", DatabaseMetaData.bestRowTemporary,
                true);

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
getBestRowIdentifier() - Specify a table that matches the table
exactly.  All matching columns should be returned.
**/
    public void Var017()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION2, "BR2", DatabaseMetaData.bestRowTemporary,
                true);

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("BALANCE"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" rows="+rows+"sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify a table pattern for which there is a
match.  An exception should be thrown, since we do not
support table pattern.

SQL400 - When you give the native driver a search pattern and they are not
         supported, we are simple going to return 0 rows.  The name isn't
         found by the CLI and therefore, nothing is returned.
**/
    public void Var018()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION2, "BR%", DatabaseMetaData.bestRowTemporary,
                true);

            if ((getDriver () == JDTestDriver.DRIVER_TOOLBOX)&& isSysibmMetadata()==false)
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
            if ((getDriver () == JDTestDriver.DRIVER_TOOLBOX)&& isSysibmMetadata()==false)
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            else
                failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify a table for which there is no match.
No rows should be returned.
**/
    public void Var019()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION2, "BOGUS", DatabaseMetaData.bestRowTemporary,
                true);

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
getBestRowIdentifier() - Specify scope = bestRowSession.
No rows should be returned.
**/
    public void Var020()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowSession,
                true);

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
getBestRowIdentifier() - Specify scope = bestRowTemporary.
A single row should be returned.
**/
    public void Var021()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowTemporary,
                true);

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("CUSTID"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" rows="+rows+"sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify scope = bestRowTransaction with
auto commit on.  No rows should be returned.
**/
    public void Var022()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowTransaction,
                true);

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
getBestRowIdentifier() - Specify scope = bestRowTransaction with auto
commit off and transaction isolation set to serializable. A single row
should be returned.
**/
    public void Var023()
    {
        try {
          Connection c2;
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            c2 = testDriver_.getConnection (baseURL_ ,
                userId_, encryptedPassword_);

          } else {
           c2 = testDriver_.getConnection (baseURL_
                + ";libraries=" + JDDMDTest.COLLECTION + " "
                + JDDMDTest.COLLECTION2 + " " + JDDMDTest.COLLECTIONXX,
                userId_, encryptedPassword_);
          }
            c2.setAutoCommit (false);
            c2.setTransactionIsolation (Connection.TRANSACTION_SERIALIZABLE);
            DatabaseMetaData dmd = c2.getMetaData ();
            ResultSet rs = dmd.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowTemporary,
                true);

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("CUSTID"))
                    check1 = true;
            }

            rs.close ();
            c2.commit();
            c2.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" rows="+rows+"sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify nullable = false.
No rows should be returned because CUSTID of BR1 is nullable.
**/
    public void Var024()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowTemporary,
                false);
            message.setLength(0);
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                message.append("\n row: "+rows+" "+
                rs.getString(1)+","+
                rs.getString(2)+","+
                rs.getString(3)+","+
                rs.getString(4)+","+
                rs.getString(5)+","+
                rs.getString(6)+","+
                rs.getString(7)+","+
                rs.getString(8));
            }

            rs.close ();
            assertCondition (rows == 0, "No rows should have been returned when nullable is false"+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBestRowIdentifier() - Specify scope = true.
A single row should be returned.
**/
    public void Var025()
    {
        try {
            ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowTemporary,
                true);

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String columnName = rs.getString ("COLUMN_NAME");
                if (columnName.equals ("CUSTID"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "check1="+check1+" rows="+rows+"sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }

//@C1A
/**
getBestRowIdentifier() - Specify a table that matches the table
exactly.  All matching columns should be returned.
**/
    public void Var026()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0 && (!JDTestDriver.isLUW()))
        {
            try {
                ResultSet rs = dmd_.getBestRowIdentifier (connection_.getCatalog (),
                    JDDMDTest.COLLECTION, "LCNTAB", DatabaseMetaData.bestRowTemporary,
                    true);

                boolean check1 = false;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    String columnName = rs.getString ("COLUMN_NAME");
                    if (columnName.equals ("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST"))
                        check1 = true;
                }

                rs.close ();
                assertCondition (check1 && (rows == 1), "Added by Toolbox 8/11/2004 to test 128 byte column names. check1="+check1+" rows="+rows+"sb 1");
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception.  Added by Toolbox 8/11/2004 to test 128 byte column names.");
            }
        }
        else
            notApplicable("V5R4 or greater variation.  Added by Toolbox 8/11/2004 to test 128 byte column names.");
    }

    /**
    getBestRowIdentifier() - Specify all null parameters, except for table name.  Verify no rows are returned.
    **/
        public void Var027() {
        try {
          ResultSet rs;

            rs = dmd_.getBestRowIdentifier(null, null, "",
                DatabaseMetaData.bestRowTemporary, false);

          int rows = 0;
          while (rs.next())
            ++rows;

          rs.close();
          assertCondition(rows == 0);
        } catch (Exception e) {
          failed(e, "Unexpected Exception -- added by native driver 04/26/2006 ");
        }
      }

        /**
        getBestRowIdentifier() - Specify nullable = false.
        1 rows should be returned because CUSTID of BR2 is not nullable.
        **/
            public void Var028()
            {
		if (getDriver()  == JDTestDriver.DRIVER_NATIVE &&
		    getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
		    notApplicable("Not working in pree V5R5 native code");
		    return;
		}

                try {
                    ResultSet rs = dmd_.getBestRowIdentifier (catalog_,
                        JDDMDTest.COLLECTION, "BR2", DatabaseMetaData.bestRowTemporary,
                        false);
                    message.setLength(0);
                    int rows = 0;
                    while (rs.next ()) {
                        ++rows;
                        message.append("\n row: "+rows+" "+
                        rs.getString(1)+","+
                        rs.getString(2)+","+
                        rs.getString(3)+","+
                        rs.getString(4)+","+
                        rs.getString(5)+","+
                        rs.getString(6)+","+
                        rs.getString(7)+","+
                        rs.getString(8));
                    }

                    rs.close ();
                    assertCondition (rows == 1, "One row should have been returned when nullable is false and table is BR2"+message+"  added 04/2006 by native JDBC driver ");
                }
                catch (Exception e)  {
                    failed (e, "Unexpected Exception");
                }
            }




/**
getBestRowIdentifier() - Run getBestRowIdentifier multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var029()   // @B1A
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
		    // System.out.println("Calling getBestRowIdentifier");
		      ResultSet rs = dmd_.getBestRowIdentifier (
						 catalog_,
						 JDDMDTest.COLLECTION,
						 "BR2", DatabaseMetaData.bestRowTemporary,
						 false);

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



  public void Var030() {
	  checkRSMD(false);
  }
  public void Var031() {
	  checkRSMD(true);
  }

    public void checkRSMD(boolean extendedMetadata)
    {

	if (getRelease() == JDTestDriver.RELEASE_V7R1M0 &&
	    getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    notApplicable("Native driver fails in V5R4 on call is isNullable");
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

	String [][] methodTests = {

      {"isAutoIncrement","1","false"},
      {"isCaseSensitive","1","false"},
      {"isSearchable","1","true"},
      {"isCurrency","1","false"},
      {"isNullable","1","0"},
      {"isSigned","1","true"},
      {"getColumnDisplaySize","1","6"},
      {"getColumnLabel","1","SCOPE"},
      {"getColumnName","1","SCOPE"},
      {"getPrecision","1","5"},
      {"getScale","1","0"},
      {"getCatalogName","1","LOCALHOST"},
      {"getColumnType","1","5"},
      {"getColumnTypeName","1","SMALLINT"},
      {"isReadOnly","1","true"},
      {"isWritable","1","false"},
      {"isDefinitelyWritable","1","false"},
      {"getColumnClassName","1","java.lang.Integer"},
      {"isAutoIncrement","2","false"},
      {"isCaseSensitive","2","true"},
      {"isSearchable","2","true"},
      {"isCurrency","2","false"},
      {"isNullable","2","0"},
      {"isSigned","2","false"},
      {"getColumnDisplaySize","2","128"},
      {"getColumnLabel","2","COLUMN_NAME"},
      {"getColumnName","2","COLUMN_NAME"},
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
      {"isCaseSensitive","3","false"},
      {"isSearchable","3","true"},
      {"isCurrency","3","false"},
      {"isNullable","3","0"},
      {"isSigned","3","true"},
      {"getColumnDisplaySize","3","6"},
      {"getColumnLabel","3","DATA_TYPE"},
      {"getColumnName","3","DATA_TYPE"},
      {"getPrecision","3","5"},
      {"getScale","3","0"},
      {"getCatalogName","3","LOCALHOST"},
      {"getColumnType","3","5"},
      {"getColumnTypeName","3","SMALLINT"},
      {"isReadOnly","3","true"},
      {"isWritable","3","false"},
      {"isDefinitelyWritable","3","false"},
      {"getColumnClassName","3","java.lang.Integer"},
      {"isAutoIncrement","4","false"},
      {"isCaseSensitive","4","true"},
      {"isSearchable","4","true"},
      {"isCurrency","4","false"},
      {"isNullable","4","0"},
      {"isSigned","4","false"},
      {"getColumnDisplaySize","4","261"},
      {"getColumnLabel","4","TYPE_NAME"},
      {"getColumnName","4","TYPE_NAME"},
      {"getPrecision","4","261"},
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
      {"getColumnDisplaySize","5","11"},
      {"getColumnLabel","5","COLUMN_SIZE"},
      {"getColumnName","5","COLUMN_SIZE"},
      {"getPrecision","5","10"},
      {"getScale","5","0"},
      {"getCatalogName","5","LOCALHOST"},
      {"getColumnType","5","4"},
      {"getColumnTypeName","5","INTEGER"},
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
      {"getColumnLabel","6","BUFFER_LENGTH"},
      {"getColumnName","6","BUFFER_LENGTH"},
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
      {"isCaseSensitive","7","false"},
      {"isSearchable","7","true"},
      {"isCurrency","7","false"},
      {"isNullable","7","1"},
      {"isSigned","7","true"},
      {"getColumnDisplaySize","7","6"},
      {"getColumnLabel","7","DECIMAL_DIGITS"},
      {"getColumnName","7","DECIMAL_DIGITS"},
      {"getPrecision","7","5"},
      {"getScale","7","0"},
      {"getCatalogName","7","LOCALHOST"},
      {"getColumnType","7","5"},
      {"getColumnTypeName","7","SMALLINT"},
      {"isReadOnly","7","true"},
      {"isWritable","7","false"},
      {"isDefinitelyWritable","7","false"},
      {"getColumnClassName","7","java.lang.Integer"},
      {"isAutoIncrement","8","false"},
      {"isCaseSensitive","8","false"},
      {"isSearchable","8","true"},
      {"isCurrency","8","false"},
      {"isNullable","8","0"},
      {"isSigned","8","true"},
      {"getColumnDisplaySize","8","6"},
      {"getColumnLabel","8","PSEUDO_COLUMN"},
      {"getColumnName","8","PSEUDO_COLUMN"},
      {"getPrecision","8","5"},
      {"getScale","8","0"},
      {"getCatalogName","8","LOCALHOST"},
      {"getColumnType","8","5"},
      {"getColumnTypeName","8","SMALLINT"},
      {"isReadOnly","8","true"},
      {"isWritable","8","false"},
      {"isDefinitelyWritable","8","false"},
      {"getColumnClassName","8","java.lang.Integer"},



	};





	String[][] fixup545TX = {
	    {"getColumnDisplaySize","4","128"},
	    {"getPrecision","4","128"},
	    {"isNullable","7","0"},
	};

	String [][] fixup614NS = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"isNullable","5","1"},
	};


	String[][] fixup714TS = {
      {"getColumnDisplaySize","3","11"},
      {"getPrecision","3","10"},
      {"getColumnType","3","4"},
      {"getColumnTypeName","3","INTEGER"},
      {"isNullable","5","1"},
	};

	String[][] fixup715TS = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"isNullable","5","1"}, /* Updated 4/30/2014 from lp01ut18 */ 
	};

	String[][] fixup726TS = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"isNullable","5","1"},

	};

	String[][] fixup714NS = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    /* Updated 10/14/2013 7146N run on lp01it18 */ 
	    {"isNullable","5","1"}
	};


	String[][] fixup726NS = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"isNullable","5","1"},
	    {"isNullable","6","0"},  /* Changed to 0 on 9/19/2013 */ 
	};



	String [][] fixup = {};


	String[][] fixupExtended = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	} ;

	String[][] fixupExtended614N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnDisplaySize","3","11"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isNullable","5","1"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},

	};

	String[][] fixupExtended616N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},   /* Updated 6/7/2012 */
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isNullable","5","1"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	};

	String[][] fixupExtended714T = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"getColumnDisplaySize","4","261"},
	    {"getPrecision","4","261"},
	    {"isNullable","5","1"},
	    {"isNullable","7","1"},
	};


	String[][] fixupExtended715T = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"getColumnDisplaySize","4","261"},
	    {"getPrecision","4","261"},
	    {"isNullable","5","1"},  /* 4/30/2014 lp01ut18 */ 
	    {"isNullable","7","1"},
	};

	String[][] fixupExtended726T = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"getColumnDisplaySize","4","261"},
	    {"getPrecision","4","261"},
	    {"isNullable","7","1"},
	    {"isNullable","5","1"},
	};


	String[][] fixupExtended736T = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"getColumnDisplaySize","4","261"},
	    {"getPrecision","4","261"},
	    {"isNullable","7","1"},
	    {"isNullable","5","1"},
	    {"getColumnLabel","1","Scope"},
	    {"getColumnLabel","2","Column Name"},
	    {"getColumnLabel","5","Column Size"},
	    {"getColumnLabel","6","Buffer Length"},
	    {"getColumnLabel","7","Decimal Digits"},
	    {"getColumnLabel","8","Pseudo Column"},
	};


	String[][] fixupExtended714N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    /* Updated 10/14/2013 from run on lp01ut18 */ 
	    {"isNullable","5","1"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	};

	String[][] fixupExtended726N = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"isNullable","5","1"},
	    {"isNullable","6","0"},  /* Changed 9/19/2013 for z1014p14 */ 
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},

	};

	String[][] fixupExtended736N = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"isNullable","5","1"},
	    {"isNullable","6","0"},  /* Changed 9/19/2013 for z1014p14 */ 
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
       /* Label change 4/23/2019 */ 
      {"getColumnLabel","1","Scope"},
      {"getColumnLabel","2","Column              Name"},
      {"getColumnLabel","5","Column              Size"},
      {"getColumnLabel","6","Buffer              Length"},
      {"getColumnLabel","7","Decimal             Digits"},
      {"getColumnLabel","8","Pseudo              Column"},

	};



	String[][] fixup726L = {
	    {"getColumnDisplaySize","3","11"},
	    {"getPrecision","3","10"},
	    {"getColumnType","3","4"},
	    {"getColumnTypeName","3","INTEGER"},
	    {"getColumnType","4","-1"},
	    {"getColumnTypeName","4","LONG VARCHAR"},
	};


	String[][] fixup546L = {
	    {"getColumnType","4","-1"},
	    {"getColumnTypeName","4","LONG VARCHAR"},
	}; 

	Object[][] fixupArrayExtended = {
	    {"544T", fixup545TX},
	    {"545T", fixup545TX},
	    {"546T", fixup545TX},
	    {"614T", fixup545TX},
	    {"615T", fixup545TX},
	    {"616T", fixup545TX},
	    {"617T", fixup545TX},
	    {"618T", fixup545TX},
	    {"714T", fixupExtended714T},
	    {"715T", fixupExtended715T},
	    {"716T", fixupExtended715T, "09/06/2012 Guess based on 715T"},
	    {"717T", fixupExtended715T, "09/06/2012 Guess based on 715T"},
	    {"718T", fixupExtended715T, "09/06/2012 Guess based on 715T"},
	    {"719T", fixupExtended715T, "09/06/2012 Guess based on 715T"},
	    {"726T", fixupExtended726T, "09/06/2012 Guess based on 715T"},
	    {"727T", fixupExtended726T, "09/06/2012 Guess based on 715T"},
	    {"728T", fixupExtended726T, "09/06/2012 Guess based on 715T"},
	    {"729T", fixupExtended726T, "09/06/2012 Guess based on 715T"},

	    {"736T", fixupExtended736T, "Updated 5/29/2019"}, 
	    {"737T", fixupExtended736T, "Updated 5/29/2019"}, 
	    {"738T", fixupExtended736T, "Updated 5/29/2019"}, 
	    {"739T", fixupExtended736T, "Updated 5/29/2019"}, 


	    {"546N", fixupExtended},
	    {"614N", fixupExtended614N},
	    {"615N", fixupExtended614N},
	    {"616N", fixupExtended616N},
	    {"714N", fixupExtended714N},
	    {"715N", fixupExtended714N, "08/09/2012 -- guess from 717N"},
	    {"716N", fixupExtended714N, "08/09/2012 -- guess from 717N"},
	    {"717N", fixupExtended714N, "08/09/2012 -- primed"},
	    {"718N", fixupExtended714N, "08/09/2012 -- primed"},
	    {"719N", fixupExtended714N, "08/09/2012 -- primed"},
	    {"726N", fixupExtended726N},
	    {"727N", fixupExtended726N},
	    {"728N", fixupExtended726N},
	    {"729N", fixupExtended726N},

	    {"736N", fixupExtended736N},
	    {"737N", fixupExtended736N},
	    {"738N", fixupExtended736N},
	    {"739N", fixupExtended736N},




	    {"546L", fixup546L},
	    {"716L", fixup726L}, 

	};



	Object[][] fixupArray = {
	    {"544TX", fixup545TX},
	    {"545TX", fixup545TX},
	    {"546TX", fixup545TX},
	    {"614TX", fixup545TX},
	    {"615TX", fixup545TX},
	    {"616TX", fixup545TX},
	    {"617TX", fixup545TX},
	    {"618TX", fixup545TX},
	    {"619TX", fixup545TX},

	    {"714TX", fixup545TX},
	    {"715TX", fixup545TX},
	    {"716TX", fixup545TX},
	    {"717TX", fixup545TX},
	    {"714TS", fixup714TS},
	    {"715TS", fixup715TS},
	    {"716TS", fixup715TS, "09/06/2012 -- primed"},
	    {"717TS", fixup715TS, "09/06/2012 -- guess from 716TS"},
	    {"718TS", fixup715TS, "09/06/2012 -- guess from 716TS"},
	    {"719TS", fixup715TS, "09/06/2012 -- guess from 716TS"},

	    {"726TS", fixup726TS, "10/01/2013 -- primed"},
	    {"727TS", fixup726TS, "10/01/2013 -- guess from 716TS"},
	    {"728TS", fixup726TS, "10/01/2013 -- guess from 716TS"},
	    {"729TS", fixup726TS, "10/01/2013 -- guess from 716TS"},

	    {"724TX", fixup545TX},
	    {"725TX", fixup545TX},
	    {"726TX", fixup545TX},
	    {"727TX", fixup545TX},
	    {"728TX", fixup545TX},
	    {"729TX", fixup545TX},

	    {"614NS", fixup614NS},
	    {"615NS", fixup614NS},
	    {"616NS", fixup614NS},
	    {"714NS", fixup714NS},
	    {"715NS", fixup714NS, "08/09/2012 -- guess from 717NS"},
	    {"716NS", fixup714NS, "08/09/2012 -- guess from 717NS"},
	    {"717NS", fixup714NS, "08/09/2012 -- Primed"},
	    {"718NS", fixup714NS, "08/09/2012 -- Primed"},
	    {"719NS", fixup714NS, "08/09/2012 -- Primed"},
	    {"726NS", fixup726NS, "09/16/2013 -- Primed"},
	    {"727NS", fixup726NS, "09/16/2013 -- Primed"},
	    {"72iNS", fixup726NS, "09/16/2013 -- Primed"},


	    {"546LS", fixup546L},
	    {"716LS", fixup726L}, 

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
		    rsA[j] = dmd.getBestRowIdentifier (
						 catalog_,
						 JDDMDTest.COLLECTION,
						 "BR2", DatabaseMetaData.bestRowTemporary,
						 false);

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
getBestRowIdentifier() - Use a readonly connection
**/
    public void Var032()
    {
        try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 
            ResultSet rs = dmd.getBestRowIdentifier (null,
                JDDMDTest.COLLECTION, "BR1", DatabaseMetaData.bestRowTemporary,
                true);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    c.close(); 
	    assertCondition (rows > 0, "Rows should have been found ");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }





}
