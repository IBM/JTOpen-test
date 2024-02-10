///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetCrossReference.java
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
 // File Name:    JDDMDGetCrossReference.java
 //
 // Classes:      JDDMDGetCrossReference
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
Testcase JDDMDGetCrossReference.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getCrossReference()
</ul>
**/
public class JDDMDGetCrossReference
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private String              catalog;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    DatabaseMetaData    dmd2_;

    StringBuffer message = new StringBuffer();

/**
Constructor.
**/
    public JDDMDGetCrossReference (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetCrossReference",
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
        if (getDriver()== JDTestDriver.DRIVER_JCC) {
          connection_ = testDriver_.getConnection (baseURL_,
              userId_, encryptedPassword_);

        } else {
        connection_ = testDriver_.getConnection (baseURL_
            + ";libraries=" + JDDMDTest.COLLECTION + " "
            + JDDMDTest.COLLECTION2 + " " + JDDMDTest.COLLECTIONXX,
            userId_, encryptedPassword_);
        }

        dmd_ = connection_.getMetaData ();
        catalog = connection_.getCatalog();
        if (catalog == null) {
          catalog = system_.toUpperCase();
        }

	try {
	    cleanup(true);
	} catch (Exception e) {
	    System.out.println("---------------- Warning.. cleanup failed --------------");
	}

        if (getDriver()== JDTestDriver.DRIVER_JCC) {
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

	if (! JDTestDriver.isLUW()) {

        //  Create primary key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".CR1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".CRKEY1 PRIMARY KEY (CUSTID))");

        //  Create foreign key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".CR2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".CRKEY2 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".CR1 (CUSTID) ON DELETE NO ACTION ON UPDATE NO ACTION)");

        // Create foreign key using restrict update and delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".CR5 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".CRKEY5 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".CR1 (CUSTID) ON DELETE RESTRICT ON UPDATE RESTRICT)");

        // Create foreign key using restrict update and cascade delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".CR6 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".CRKEY6 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".CR1 (CUSTID) ON DELETE CASCADE ON UPDATE RESTRICT)");

        // Create foreign key using restrict update and set default delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".CR7 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".CRKEY7 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".CR1 (CUSTID) ON DELETE SET DEFAULT ON UPDATE RESTRICT)");

        // Create primary key which will be used for set null update rule.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                           + ".CR8 (CUSTID INT NOT NULL, BALANCE DEC (6,2), "
                           + "CONSTRAINT " + JDDMDTest.COLLECTION
                           + ".CRKEY8 UNIQUE (BALANCE))");

        // Create foreign key using restrict update and set null delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".CR9 (CUSTID INT NOT NULL, BALANCE DECIMAL(6,2), "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".CRKEY9 FOREIGN KEY (BALANCE) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".CR8 (BALANCE) ON DELETE SET NULL ON UPDATE RESTRICT)");

        //  Create primary key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".CR3 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".CRKEY3 PRIMARY KEY (CUSTID))");

        //  Create foreign key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".CR4 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".CRKEY4 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION2
                         + ".CR3 (CUSTID) ON DELETE NO ACTION ON UPDATE NO ACTION)");


	} else {
	//  Create primary key.
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".CR1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
			     + "CONSTRAINT "
			     + "CRKEY1 PRIMARY KEY (CUSTID))");

	//  Create foreign key.
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".CR2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, "
			     + "CONSTRAINT "
			     + "CRKEY2 FOREIGN KEY (CUSTID) "
			     + "REFERENCES "
			     + "CR1 (CUSTID) ON DELETE NO ACTION ON UPDATE NO ACTION)");

	// Create foreign key using restrict update and delete rules.
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".CR5 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, "
			     + "CONSTRAINT "
			     + "CRKEY5 FOREIGN KEY (CUSTID) "
			     + "REFERENCES "
			     + "CR1 (CUSTID) ON DELETE RESTRICT ON UPDATE RESTRICT)");

	// Create foreign key using restrict update and cascade delete rules.
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".CR6 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, "
			     + "CONSTRAINT "
			     + "CRKEY6 FOREIGN KEY (CUSTID) "
			     + "REFERENCES "
			     + "CR1 (CUSTID) ON DELETE CASCADE ON UPDATE RESTRICT)");

	// Create foreign key using restrict update and set default delete rules.
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".CR7 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, "
			     + "CONSTRAINT "
			     + "CRKEY7 FOREIGN KEY (CUSTID) "
			     + "REFERENCES "
			     + "CR1 (CUSTID) ON DELETE SET DEFAULT ON UPDATE RESTRICT)");

	// Create primary key which will be used for set null update rule.
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".CR8 (CUSTID INT NOT NULL, BALANCE DEC (6,2), "
			     + "CONSTRAINT "
			     + "CRKEY8 UNIQUE (BALANCE))");

	// Create foreign key using restrict update and set null delete rules.
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".CR9 (CUSTID INT NOT NULL, BALANCE DECIMAL(6,2), "
			     + "CONSTRAINT "
			     + "CRKEY9 FOREIGN KEY (BALANCE) "
			     + "REFERENCES "
			     + "CR8 (BALANCE) ON DELETE SET NULL ON UPDATE RESTRICT)");

	//  Create primary key.
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
			     + ".CR3 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, "
			     + "CONSTRAINT "
			     + "CRKEY3 PRIMARY KEY (CUSTID))");

	//  Create foreign key.
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
			     + ".CR4 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, "
			     + "CONSTRAINT "
			     + "CRKEY4 FOREIGN KEY (CUSTID) "
			     + "REFERENCES "
			     + "CR3 (CUSTID) ON DELETE NO ACTION ON UPDATE NO ACTION)");


	}
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0 && (!JDTestDriver.isLUW())) //@C1A
        {
            //  Create primary key.  //@C1A
            s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".LCNCR3 (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".LCNCRKEY3 PRIMARY KEY (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST))");

            //  Create foreign key. //@C1A
            s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".LCNCR4 (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".LCNCRKEY4 FOREIGN KEY (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST) "
                         + "REFERENCES " + JDDMDTest.COLLECTION2
                         + ".LCNCR3 (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST) ON DELETE NO ACTION ON UPDATE NO ACTION)");
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
	cleanup(false);
    }
    protected void cleanup (boolean ignoreErrors)
      throws Exception
    {
	Statement s = connection_.createStatement ();
	try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".CR2 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".CRKEY2 CASCADE");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}

	try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".CR5 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".CRKEY5 RESTRICT");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}

	try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".CR6 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".CRKEY6 CASCADE");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".CR7 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".CRKEY7 RESTRICT");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".CR1 DROP PRIMARY KEY CASCADE");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
			     + ".CR4 DROP FOREIGN KEY " + JDDMDTest.COLLECTION2 + ".CRKEY4 CASCADE");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
			     + ".CR3 DROP PRIMARY KEY CASCADE");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".CR9 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".CRKEY9 CASCADE");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".CR8 DROP UNIQUE " + JDDMDTest.COLLECTION + ".CRKEY8");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	if(getRelease() >= JDTestDriver.RELEASE_V5R4M0) //@C1A
	{
	    try {
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2                   //@C1A
				 + ".LCNCR4 DROP FOREIGN KEY " + JDDMDTest.COLLECTION2 + ".LCNCRKEY4 CASCADE");
	    } catch (Exception e) {
		if (!ignoreErrors) e.printStackTrace();
	    }
	    try {
		s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2                   //@C1A
				 + ".LCNCR3 DROP PRIMARY KEY CASCADE");
	    } catch (Exception e) {
		if (!ignoreErrors) e.printStackTrace();
	    }
	}

	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".CR1");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".CR2");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".CR5");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".CR6");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".CR7");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".CR8");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
			     + ".CR9");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
			     + ".CR3");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	try {
	    s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
			     + ".CR4");
	} catch (Exception e) {
	    if (!ignoreErrors) e.printStackTrace();
	}
	if(getRelease() >= JDTestDriver.RELEASE_V5R4M0) //@C1A
	{
	    try {
		s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2        //@C1A
				 + ".LCNCR3");
	    } catch (Exception e) {
		if (!ignoreErrors) e.printStackTrace();
	    }
	    try {
		s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2        //@C1A
				 + ".LCNCR4");
	    } catch (Exception e) {
		if (!ignoreErrors) e.printStackTrace();
	    }
	}

	s.close ();
	connection_.close ();
    }



/**
getCrossReference() - Check the result set format.
**/
    public void Var001()
    {
      message.setLength(0);

        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");
            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "PKTABLE_CAT", "PKTABLE_SCHEM",
                                       "PKTABLE_NAME", "PKCOLUMN_NAME",
                                       "FKTABLE_CAT", "FKTABLE_SCHEM",
                                       "FKTABLE_NAME", "FKCOLUMN_NAME",
                                       "KEY_SEQ", "UPDATE_RULE", "DELETE_RULE",
                                       "FK_NAME", "PK_NAME", "DEFERRABILITY" };
            int[] expectedTypes = { Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.VARCHAR,
                                    Types.VARCHAR, Types.VARCHAR,
                                    Types.SMALLINT, Types.SMALLINT,
                                    Types.SMALLINT, Types.VARCHAR,
                                    Types.VARCHAR, Types.SMALLINT };

            int count = rsmd.getColumnCount ();
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);
            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);

            rs.close ();
            assertCondition ((count == 14) && (namesCheck) && (typesCheck), message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Verify all columns.

SQL400 - The native JDBC driver will return the names of the primary and foreign
         keys, whereas the Toolbox will return null for these values.

SQL400 - The native driver reports the update and delete rules in the following
         testcase as being no action where the toolbox driver reports them as
         being restrict.  I think the native driver is giving the right results.
**/
    public void Var002()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;

                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (pktableName.equals ("CR1"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (fkcolumnName.equals ("CUSTID"));

                short keySeq            = rs.getShort ("KEY_SEQ");
                short updateRule        = rs.getShort ("UPDATE_RULE");
                short deleteRule        = rs.getShort ("DELETE_RULE");
                String fkName           = rs.getString ("FK_NAME");
                String pkName           = rs.getString ("PK_NAME");
                short deferrability     = rs.getShort ("DEFERRABILITY");

                // System.out.println (keySeq + ":" + updateRule + ":" + deleteRule + ":" + fkName + ":" + pkName + ":" + deferrability + ":");

                success = success && (keySeq == 1);

                if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
                    success = success && (pkName == null);
                else
                    success = success && (pkName.equals("CRKEY1"));

                success = success && (deferrability == DatabaseMetaData.importedKeyNotDeferrable);

                if (fktableName.equals ("CR2"))
                {
                    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
                    {
                        success = success && (updateRule == DatabaseMetaData.importedKeyRestrict);
                        success = success && (deleteRule == DatabaseMetaData.importedKeyRestrict);
                        success = success && (fkName == null);
                    }
                    else
                    {
                        success = success && (updateRule == DatabaseMetaData.importedKeyNoAction);
                        success = success && (deleteRule == DatabaseMetaData.importedKeyNoAction);
                        success = success && (fkName.equals("CRKEY2"));
                    }
                }
            }

            // System.out.println ("Rows = " + rows);

            rs.close ();
            assertCondition ((rows == 1) && success);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify all null parameters.  Verify no rows are returned.
**/
    public void Var003()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (null, null, null,
                                                   null, null, null);

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
getCrossReference() - Specify null for the primary catalog.
**/
    public void Var004()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (null,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (pktableName.equals ("CR1"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (fktableName.equals ("CR2"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify empty string for the primary catalog.
**/
    public void Var005()
    {
        try {
            ResultSet rs = dmd_.getCrossReference ("",
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (isSysibmMetadata()) {
		assertCondition (rows > 0, "Using SYSIBM procedures.  Empty string specified for catalog but 0 rows returned");
	    } else {
		assertCondition (rows == 0, "Empty string specified for catalog but rows returned");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify a primary catalog that matches the catalog
exactly.
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR3",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (pktableName.equals ("CR3"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (fktableName.equals ("CR4"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify "localhost" for the primary catalog.
**/
    public void Var007()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4 ||
          ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V5R5M0)) ||
          (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
        notApplicable("\"localhost\" variation ");
      } else {

        try {
            ResultSet rs = dmd_.getCrossReference ("localhost",
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (pktableName.equals ("CR1"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (fktableName.equals ("CR2"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
getCrossReference() - Specify a primary catalog pattern for which there is a
match.  No matching rows should be returned, since we do not
support catalog pattern.
**/
    public void Var008()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog.substring (0, 4) + "%",
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

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
getCrossReference() - Specify a primary catalog for which there is no match.
No matching columns should be returned.
**/
    public void Var009()
    {
        try {
            ResultSet rs = dmd_.getCrossReference ("BOGUS%",
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

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
getCrossReference() - Specify null for the foreign catalog.
**/
    public void Var010()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   null,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (pktableName.equals ("CR1"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (fktableName.equals ("CR2"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify empty string for the foreign catalog.
**/
    public void Var011()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   "",
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (isSysibmMetadata()) {
		assertCondition (rows > 0, "Using sysibm metdata:  empty string specified for foreign catalog, but 0 rows returned ");
	    } else {
		assertCondition (rows == 0, "empty string specified for foreign catalog, but rows returned ");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify a foreign catalog that matches the catalog
exactly.
**/
    public void Var012()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR3",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (pktableName.equals ("CR3"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (fktableName.equals ("CR4"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify "localhost" for the foreign  catalog.
**/
    public void Var013()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4 ||
          ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V5R5M0)) ||
          (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
        notApplicable("\"localhost\" variation ");
      } else {

        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   "localhost",
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (pktableName.equals ("CR1"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (fktableName.equals ("CR2"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
getCrossReference() - Specify a foreign catalog pattern for which there is a
match.  No matching rows should be returned, since we do not
support catalog pattern.
**/
    public void Var014()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog.substring (0, 4) + "%",
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

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
getCrossReference() - Specify a foreign catalog for which there is no match.
No matching columns should be returned.
**/
    public void Var015()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   "BOGUS%",
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

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
getCrossReference() - Specify null for the primary schema.
**/
    public void Var016()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   null,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (pktableName.equals ("CR1"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (fktableName.equals ("CR2"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify empty string for the primary schema.
**/
    public void Var017()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   "",
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

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
getCrossReference() - Specify a primary schema that matches the schema
exactly.
**/
    public void Var018()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR3",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (pktableName.equals ("CR3"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (fktableName.equals ("CR4"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify a primary schema for which there is a
match.  Should throw an exception, since we do not
support schema pattern.

SQL400 - for functions that call the SQLForeignKeys() CLI function,
         the native driver can handle the search pattern.  We check
         to make sure that the row gets returned.
**/
    public void Var019()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.SCHEMAS_PERCENT,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
            {
                failed("Didn't throw SQLException");
            }
            else
            {
                // The JDBC spec doesn't support matching patterns in schema.
                // if JCC or JDK 1.6, we should get now rows back
                int rows = 0;
                while (rs.next ())
                    ++rows;

                rs.close ();
                if ((getDriver() == JDTestDriver.DRIVER_JCC) || (getJdbcLevel() >= 4 ) ||
                    ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V5R5M0)) ||
                    (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                  assertCondition (rows == 0, "Expected 0 rows when schema had a pattern  ");
                } else {
                   assertCondition (rows > 0);
                }
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
getCrossReference() - Specify a primary schema for which there is no match.
No rows should be returned.
**/
    public void Var020()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   "BOGUS",
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

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
getCrossReference() - Specify null for the foreign schema.
**/
    public void Var021()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   null,
                                                   "CR2");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (pktableName.equals ("CR1"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (fktableName.equals ("CR2"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify empty string for the foreign schema.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   "",
                                                   "CR2");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0, "empty string specified for foreign schema but "+rows+" rows returned");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify a foreign schema that matches the schema
exactly.
**/
    public void Var023()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR3",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (pktableName.equals ("CR3"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (fktableName.equals ("CR4"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify a foreign schema for which there is a
match.  Should throw an exception, since we do not
support schema pattern.

SQL400 - for functions that call the SQLForeignKeys() CLI function,
         the native driver can handle the search pattern.  We check
         to make sure that the row gets returned.
**/
    public void Var024()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.SCHEMAS_PERCENT,
                                                   "CR2");

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

                if ((getDriver() == JDTestDriver.DRIVER_JCC) || (getJdbcLevel() >= 4 ) ||
                    ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V5R5M0)) ||
                    (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                  assertCondition (rows == 0, "Expected 0 rows when schema had a pattern  ");
                } else {
                  assertCondition (rows > 0);
                }
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
getCrossReference() - Specify a primary schema for which there is no match.
No rows should be returned.
**/
    public void Var025()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   "BOGUS",
                                                   "CR2");

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
getCrossReference() - Specify null for the primary table.
No rows should be returned.
**/
    public void Var026()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   null,
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0, "null specified for primary table and "+rows+" rows returned");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify empty string for the primary table.
**/
    public void Var027()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0, "Found "+rows+" rows, expected zero since primary table is empty string");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify a primary table that matches the table
exactly.  All matching rows should be returned.
**/
    public void Var028()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR3",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (pktableName.equals ("CR3"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (fktableName.equals ("CR4"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify a primary table pattern for which there is a
match.  An exception should be thrown, since we do not
support table pattern.

SQL400 - for functions that call the SQLForeignKeys() CLI function,
         the native driver can handle the search pattern.  We check
         to make sure that the row gets returned.
**/
    public void Var029()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR%",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");

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
                if ((getDriver() == JDTestDriver.DRIVER_JCC) || (getJdbcLevel() >= 4 ) ||
                    ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V5R5M0)) ||
                    (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                  assertCondition (rows == 0, "Expected 0 rows when table had a pattern  ");
                } else {
                  assertCondition (rows > 0);
                }
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
getCrossReference() - Specify a foreign table for which there is no match.
No rows should be returned.
**/
    public void Var030()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "BOGUS",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");

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
getCrossReference() - Specify null for the foreign table.
No rows should be returned.
**/
    public void Var031()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR3",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   null);

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0, "rows = "+rows+" sb 0 since foreign table = null ");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify empty string for the foreign table.
**/
    public void Var032()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR3",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
            assertCondition (rows == 0, "rows = "+rows+" sb 0 since foreign table = ''");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify a foreign table that matches the table
exactly.  All matching rows should be returned.
**/
    public void Var033()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR3",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (pktableName.equals ("CR3"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION2));
                success = success && (fktableName.equals ("CR4"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCrossReference() - Specify a foreign table pattern for which there is a
match.  An exception should be thrown, since we do not
support table pattern.

SQL400 - for functions that call the SQLForeignKeys() CLI function,
         the native driver can handle the search pattern.  We check
         to make sure that the row gets returned.
**/
    public void Var034()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR3",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR%");

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
                if ((getDriver() == JDTestDriver.DRIVER_JCC) || (getJdbcLevel() >= 4 ) ||
                    ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V5R5M0)) ||
                    (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
                  assertCondition (rows == 0, "Expected 0 rows when table had a pattern  ");
                } else {
                  assertCondition (rows > 0);
                }
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
getCrossReference() - Specify a foreign table for which there is no match.
No rows should be returned.
**/
    public void Var035()
    {
        try {
            ResultSet rs = dmd_.getCrossReference (catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4",
                                                   catalog,
                                                   JDDMDTest.COLLECTION2,
                                                   "BOGUS");

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
getCrossReference() - Specify a primary catalog that matches the catalog
exactly.   Check that we can get back 128 byte column names.
**/
    public void Var036()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0)
        {
            try {
                ResultSet rs = dmd_.getCrossReference (catalog,
                                                       JDDMDTest.COLLECTION2,
                                                       "LCNCR3",
                                                       catalog,
                                                       JDDMDTest.COLLECTION2,
                                                       "LCNCR4");

                boolean success = true;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                    String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                    success = success && (pkcolumnName.equals ("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST"));
                    success = success && (fkcolumnName.equals ("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST"));

                }

                rs.close ();
                assertCondition (success && (rows == 1), "Added by Toolbox 8/11/2004 to test 128 byte column names.");
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception.  Added by Toolbox 8/11/2004 to test 128 byte column names.");
            }
        }
        else
            notApplicable("V5R4 and greater variation.");
    }


/**
getCrossReference() - Check all the RSMD results when using JDBC 3.0.
**/

  public void Var037() {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 24355 && getRelease() < JDTestDriver.RELEASE_V5R5M0 ) {
	  notApplicable("Native Driver and SI24355 testing current level = "+getDriverFixLevel());
      } else {

	  checkRSMD(false);
      }
  }
  public void Var038() {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 24355 && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
	  notApplicable("Native Driver and SI24355 testing current level = "+getDriverFixLevel());
      } else {

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
	String [][] methodTests = {
	    {"isAutoIncrement","1","false"},
	    {"isCaseSensitive","1","true"},
	    {"isSearchable","1","true"},
	    {"isCurrency","1","false"},
	    {"isNullable","1","1"},
	    {"isSigned","1","false"},
	    {"getColumnDisplaySize","1","128"},
	    {"getColumnLabel","1","PKTABLE_CAT"},
	    {"getColumnName","1","PKTABLE_CAT"},
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
	    {"isNullable","2","1"},
	    {"isSigned","2","false"},
	    {"getColumnDisplaySize","2","128"},
	    {"getColumnLabel","2","PKTABLE_SCHEM"},
	    {"getColumnName","2","PKTABLE_SCHEM"},
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
	    {"getColumnLabel","3","PKTABLE_NAME"},
	    {"getColumnName","3","PKTABLE_NAME"},
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
	    {"getColumnLabel","4","PKCOLUMN_NAME"},
	    {"getColumnName","4","PKCOLUMN_NAME"},
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
	    {"getColumnLabel","5","FKTABLE_CAT"},
	    {"getColumnName","5","FKTABLE_CAT"},
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
	    {"isNullable","6","1"},
	    {"isSigned","6","false"},
	    {"getColumnDisplaySize","6","128"},
	    {"getColumnLabel","6","FKTABLE_SCHEM"},
	    {"getColumnName","6","FKTABLE_SCHEM"},
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
	    {"getColumnDisplaySize","7","128"},
	    {"getColumnLabel","7","FKTABLE_NAME"},
	    {"getColumnName","7","FKTABLE_NAME"},
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
	    {"isNullable","8","0"},
	    {"isSigned","8","false"},
	    {"getColumnDisplaySize","8","128"},
	    {"getColumnLabel","8","FKCOLUMN_NAME"},
	    {"getColumnName","8","FKCOLUMN_NAME"},
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
	    {"isCaseSensitive","9","false"},
	    {"isSearchable","9","true"},
	    {"isCurrency","9","false"},
	    {"isNullable","9","0"},
	    {"isSigned","9","true"},
	    {"getColumnDisplaySize","9","6"},
	    {"getColumnLabel","9","KEY_SEQ"},
	    {"getColumnName","9","KEY_SEQ"},
	    {"getPrecision","9","5"},
	    {"getScale","9","0"},
	    {"getCatalogName","9","LOCALHOST"},
	    {"getColumnType","9","5"},
	    {"getColumnTypeName","9","SMALLINT"},
	    {"isReadOnly","9","true"},
	    {"isWritable","9","false"},
	    {"isDefinitelyWritable","9","false"},
	    {"getColumnClassName","9","java.lang.Short"},

	    {"isAutoIncrement","10","false"},
	    {"isCaseSensitive","10","false"},
	    {"isSearchable","10","true"},
	    {"isCurrency","10","false"},
	    {"isNullable","10","0"},
	    {"isSigned","10","true"},
	    {"getColumnDisplaySize","10","6"},
	    {"getColumnLabel","10","UPDATE_RULE"},
	    {"getColumnName","10","UPDATE_RULE"},
	    {"getPrecision","10","5"},
	    {"getScale","10","0"},
	    {"getCatalogName","10","LOCALHOST"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"isReadOnly","10","true"},
	    {"isWritable","10","false"},
	    {"isDefinitelyWritable","10","false"},
	    {"getColumnClassName","10","java.lang.Short"},

	    {"isAutoIncrement","11","false"},
	    {"isCaseSensitive","11","false"},
	    {"isSearchable","11","true"},
	    {"isCurrency","11","false"},
	    {"isNullable","11","0"},
	    {"isSigned","11","true"},
	    {"getColumnDisplaySize","11","6"},
	    {"getColumnLabel","11","DELETE_RULE"},
	    {"getColumnName","11","DELETE_RULE"},
	    {"getPrecision","11","5"},
	    {"getScale","11","0"},
	    {"getCatalogName","11","LOCALHOST"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"isReadOnly","11","true"},
	    {"isWritable","11","false"},
	    {"isDefinitelyWritable","11","false"},
	    {"getColumnClassName","11","java.lang.Short"},

	    {"isAutoIncrement","12","false"},
	    {"isCaseSensitive","12","true"},
	    {"isSearchable","12","true"},
	    {"isCurrency","12","false"},
	    {"isNullable","12","1"},
	    {"isSigned","12","false"},
	    {"getColumnDisplaySize","12","128"},
	    {"getColumnLabel","12","FK_NAME"},
	    {"getColumnName","12","FK_NAME"},
	    {"getPrecision","12","128"},
	    {"getScale","12","0"},
	    {"getCatalogName","12","LOCALHOST"},
	    {"getColumnType","12","12"},
	    {"getColumnTypeName","12","VARCHAR"},
	    {"isReadOnly","12","true"},
	    {"isWritable","12","false"},
	    {"isDefinitelyWritable","12","false"},
	    {"getColumnClassName","12","java.lang.String"},

	    {"isAutoIncrement","13","false"},
	    {"isCaseSensitive","13","true"},
	    {"isSearchable","13","true"},
	    {"isCurrency","13","false"},
	    {"isNullable","13","1"},
	    {"isSigned","13","false"},
	    {"getColumnDisplaySize","13","128"},
	    {"getColumnLabel","13","PK_NAME"},
	    {"getColumnName","13","PK_NAME"},
	    {"getPrecision","13","128"},
	    {"getScale","13","0"},
	    {"getCatalogName","13","LOCALHOST"},
	    {"getColumnType","13","12"},
	    {"getColumnTypeName","13","VARCHAR"},
	    {"isReadOnly","13","true"},
	    {"isWritable","13","false"},
	    {"isDefinitelyWritable","13","false"},
	    {"getColumnClassName","13","java.lang.String"},

	    {"isAutoIncrement","14","false"},
	    {"isCaseSensitive","14","false"},
	    {"isSearchable","14","true"},
	    {"isCurrency","14","false"},
	    {"isNullable","14","0"},
	    {"isSigned","14","true"},
	    {"getColumnDisplaySize","14","6"},
	    {"getColumnLabel","14","DEFERRABILITY"},
	    {"getColumnName","14","DEFERRABILITY"},
	    {"getPrecision","14","5"},
	    {"getScale","14","0"},
	    {"getCatalogName","14","LOCALHOST"},
	    {"getColumnType","14","5"},
	    {"getColumnTypeName","14","SMALLINT"},
	    {"isReadOnly","14","true"},
	    {"isWritable","14","false"},
	    {"isDefinitelyWritable","14","false"},
	    {"getColumnClassName","14","java.lang.Short"},

	};


	String[][] fixup544T = {
            {"getColumnClassName","9","java.lang.Integer"},
            {"getColumnClassName","10","java.lang.Integer"},
            {"getColumnClassName","11","java.lang.Integer"},
            {"getColumnClassName","14","java.lang.Integer"}
	};


	String[][] fixup614TS = {
	    {"isNullable","12","1"},

            {"getColumnClassName","9","java.lang.Integer"},
            {"getColumnClassName","10","java.lang.Integer"},
            {"getColumnClassName","11","java.lang.Integer"},
            {"getColumnClassName","14","java.lang.Integer"},

            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","5","0" },
            {      "isNullable","6","0" },
            {      "getColumnClassName","9","java.lang.Integer" },
            {      "getColumnClassName","10","java.lang.Integer" },
            {      "getColumnClassName","11","java.lang.Integer" },
            {      "isNullable","12","0" },

            {      "getColumnClassName","14","java.lang.Integer" },

	};


	String[][] fixup725TS = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"getColumnLabel","2","PKTABLE_SCHEM"},
	    {"getColumnLabel","3","PKTABLE_NAME"},
	    {"isNullable","5","1"},
	    {"isNullable","6","1"},
	    {"isNullable","12","1"},

            {"getColumnClassName","9","java.lang.Integer"},
            {"getColumnClassName","10","java.lang.Integer"},
            {"getColumnClassName","11","java.lang.Integer"},
            {"getColumnClassName","14","java.lang.Integer"},

	    {      "isNullable","1","0" },
	    {      "isNullable","2","0" },
	    {      "isNullable","5","0" },
	    {      "isNullable","6","0" },
	    {      "getColumnClassName","9","java.lang.Integer" },
	    {      "getColumnClassName","10","java.lang.Integer" },
	    {      "getColumnClassName","11","java.lang.Integer" },
	    {      "isNullable","12","0" },

	    {      "getColumnClassName","14","java.lang.Integer" },


	};

	String [][] nativeExtendedDifferences546N = {
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

	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isNullable","12","0"},
	    {"isNullable","13","0"},
	    {"isSearchable","14","false"},
	    {"getColumnClassName","14","java.lang.Integer"},
	};


	String [][] nativeExtendedDifferences545N = {
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

	};


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

	    /* Primed 3/27/2012 from rchaptf 6156N JDDMDGetCrossReference */
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"getColumnLabel","2","LIBRARY             NAME"},
	    {"getColumnLabel","3","LONG                FILE                NAME"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isNullable","12","0"},
	    {"isSearchable","14","false"},
	    {"getColumnClassName","14","java.lang.Integer"},
	};



        String [][] nativeSysibmExtendedDifferences61 = {
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

	    // Updated 3/23/2012 to new behavior on rchaptf2
	    {"getColumnLabel","2","LIBRARY             NAME"},
	    {"getColumnLabel","3","LONG                FILE                NAME"},
	    // Updated 4/3/2012 for 6166N on rchaptf2
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isNullable","12","0"},
	    {"getColumnClassName","14","java.lang.Integer"},

        };






	String[][] sysibmDifferences710extendedToolbox = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
//
// set 02/13/2012 for 7166T on lp01ut18
	    {      "getColumnLabel","2","LIBRARY NAME" },
	    {      "getColumnLabel","3","LONG FILE NAME" },
            {      "isNullable","5","0" },
            {      "isNullable","6","0" },
            {      "getColumnClassName","9","java.lang.Integer" },
            {      "getColumnClassName","10","java.lang.Integer" },
            {      "getColumnClassName","11","java.lang.Integer" },
            {      "isNullable","12","0" },
            /* On  4/23/2007 on lp13ut16 for JDK 1.6 returns 1 for V6R1 */
            /* On  2/18/2009 on lp13ut16 for JDK 1.4 returns 1 for V6R1 */
            /* On  2/18/2009 on lp13ut16 for JDK 1.5 returns 1 for V6R1 */
            /* On  2/18/2009 on lp13ut16 for JDK 1.6 returns 1 for V6R1 */

            {      "getColumnClassName","14","java.lang.Integer" },
        };


	String[][] sysibmDifferences730extendedToolbox = {
            {      "isNullable","1","0" },
            {      "isNullable","2","0" },
            {      "isNullable","5","0" },
            {      "isNullable","6","0" },
            {      "getColumnClassName","9","java.lang.Integer" },
            {      "getColumnClassName","10","java.lang.Integer" },
            {      "getColumnClassName","11","java.lang.Integer" },
            {      "isNullable","12","0" },
            {      "getColumnClassName","14","java.lang.Integer" },
	    {"getColumnLabel","1","Pktable Cat"},
	    {"getColumnLabel","2","Pktable Schem"},
	    {"getColumnLabel","3","Pktable Name"},
	    {"getColumnLabel","4","Pkcolumn Name"},
	    {"getColumnLabel","5","Fktable Cat"},
	    {"getColumnLabel","6","Fktable Schem"},
	    {"getColumnLabel","7","Fktable Name"},
	    {"getColumnLabel","8","Fkcolumn Name"},
	    {"getColumnLabel","9","Key Seq"},
	    {"getColumnLabel","10","Update Rule"},
	    {"getColumnLabel","11","Delete Rule"},
	    {"getColumnLabel","12","Fk Name"},
	    {"getColumnLabel","13","Pk Name"},
	    {"getColumnLabel","14","Deferrability"},
        };


	String[][]fixup546N ={
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isNullable","12","0"},
	    {"isNullable","13","0"},
	    {"getColumnClassName","14","java.lang.Integer"},
	};


	String[][] fixup546L = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isNullable","12","0"},
	    {"isNullable","13","0"},
	    {"getColumnClassName","14","java.lang.Integer"},

	};

	String[][] fixup614N = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isNullable","12","0"},
	    {"getColumnClassName","14","java.lang.Integer"},
	};

	String[][] fixup725N = {
	    {      "isNullable","1","0" },
	    {      "isNullable","2","0" },
	    {      "isNullable","5","0" },
	    {      "isNullable","6","0" },
	    {      "getColumnClassName","9","java.lang.Integer" },
	    {      "getColumnClassName","10","java.lang.Integer" },
	    {      "getColumnClassName","11","java.lang.Integer" },
	    {      "isNullable","12","0" },

	    {      "getColumnClassName","14","java.lang.Integer" },

	    /* Updated on 08/03/2011 for V7R2 */
	    /* Removed on 09/26/2012 for lp71ut27 7253N */
	    /* 
	    {       "getColumnLabel","2","LIBRARY             NAME"},
	    {       "getColumnLabel","3","LONG                FILE                NAME"},

*/
        };


	String[][] fixup726N = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isNullable","12","0"},
	    {"getColumnClassName","14","java.lang.Integer"},
	};

	String[][] extendedDifferences715N = {
	    {"isSearchable","1","false"},
	    {"isNullable","1","0"},
	    {"isSearchable","2","false"},
	    {"isNullable","2","0"},
	    {"getColumnLabel","2","LIBRARY             NAME"},
	    {"isSearchable","3","false"},
	    {"getColumnLabel","3","LONG                FILE                NAME"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isNullable","5","0"},
	    {"isSearchable","6","false"},
	    {"isNullable","6","0"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"isSearchable","10","false"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"isSearchable","11","false"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isSearchable","12","false"},
	    {"isNullable","12","0"},
	    {"isSearchable","13","false"},
	    {"isSearchable","14","false"},
	    {"getColumnClassName","14","java.lang.Integer"},
	}; 

	String[][] fixupExtended715T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"getColumnLabel","2","LIBRARY NAME"},
	    {"getColumnLabel","3","LONG FILE NAME"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isNullable","12","0"},
	    {"getColumnClassName","14","java.lang.Integer"},
	};
	String [][] fixupExtended726N = {
	    {"isSearchable","1","false"},
	    {"isNullable","1","0"},
	    {"isSearchable","2","false"},
	    {"isNullable","2","0"},
	    {"getColumnLabel","2","LIBRARY             NAME"},
	    {"isSearchable","3","false"},
	    {"getColumnLabel","3","LONG                FILE                NAME"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isNullable","5","0"},
	    {"isSearchable","6","false"},
	    {"isNullable","6","0"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"isSearchable","10","false"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"isSearchable","11","false"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isSearchable","12","false"},
	    {"isNullable","12","0"},
	    {"isSearchable","13","false"},
	    {"isSearchable","14","false"},
	    {"getColumnClassName","14","java.lang.Integer"},
  
	};

	String [][] fixupExtended736N = {
	    {"isSearchable","1","false"},
	    {"isNullable","1","0"},
	    {"isSearchable","2","false"},
	    {"isNullable","2","0"},
	    {"getColumnLabel","2","LIBRARY             NAME"},
	    {"isSearchable","3","false"},
	    {"getColumnLabel","3","LONG                FILE                NAME"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isNullable","5","0"},
	    {"isSearchable","6","false"},
	    {"isNullable","6","0"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"isSearchable","10","false"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"isSearchable","11","false"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isSearchable","12","false"},
	    {"isNullable","12","0"},
	    {"isSearchable","13","false"},
	    {"isSearchable","14","false"},
	    {"getColumnClassName","14","java.lang.Integer"},
      {"getColumnLabel","1","Pktable             Cat"},
      {"getColumnLabel","2","Pktable             Schem"},
      {"getColumnLabel","3","Pktable             Name"},
      {"getColumnLabel","4","Pkcolumn            Name"},
      {"getColumnLabel","5","Fktable             Cat"},
      {"getColumnLabel","6","Fktable             Schem"},
      {"getColumnLabel","7","Fktable             Name"},
      {"getColumnLabel","8","Fkcolumn            Name"},
      {"getColumnLabel","9","Key                 Seq"},
      {"getColumnLabel","10","Update              Rule"},
      {"getColumnLabel","11","Delete              Rule"},
      {"getColumnLabel","12","Fk                  Name"},
      {"getColumnLabel","13","Pk                  Name"},
      {"getColumnLabel","14","Deferrability"},
  
	};



	String [][] fixup = {};

	Object[][] fixupArrayExtended = {
	    { "543T", fixup544T},
	    { "544T", fixup544T},
	    { "545T", fixup544T},
	    { "546T", fixup544T},

	    { "614T", fixup544T},
	    { "615T", fixup544T},
	    { "616T", fixup544T},
	    { "617T", fixup544T},
	    { "618T", fixup544T},
	    { "714T", fixupExtended715T, "Prime 8/21/2012"},
	    { "715T", fixupExtended715T},
	    { "716T", sysibmDifferences710extendedToolbox},
	    { "717T", sysibmDifferences710extendedToolbox},
	    { "718T", sysibmDifferences710extendedToolbox},
	    { "719T", sysibmDifferences710extendedToolbox},


	    { "737T", sysibmDifferences730extendedToolbox},
	    { "738T", sysibmDifferences730extendedToolbox},
	    { "739T", sysibmDifferences730extendedToolbox},


	    { "543N", nativeExtendedDifferences},
	    { "544N", nativeExtendedDifferences545N, "Guess 8/31/2012"},
	    { "545N", nativeExtendedDifferences545N},
	    { "546N", nativeExtendedDifferences546N},
	    { "614N", nativeExtendedDifferences},
	    { "615N", nativeExtendedDifferences},
	    { "616N", nativeSysibmExtendedDifferences61},

	    { "714N", nativeExtendedDifferences},
	    { "715N", extendedDifferences715N}, 
	    { "716N", extendedDifferences715N, "Primed 8/23/2012"},
	    { "717N", nativeExtendedDifferences},  /* checking 8/9/2012 */
	    { "718N", nativeExtendedDifferences},  /* checking 8/9/2012 */
	    { "719N", nativeExtendedDifferences},  /* checking 8/9/2012 */  
	    { "726N", fixupExtended726N, "Primed 11/8/2012"},
	    { "727N", fixupExtended726N, "updated 9/17/2013"},
	    { "728N", fixupExtended726N, "updated 9/17/2013"},
	    { "729N", fixupExtended726N, "updated 9/17/2013"},

	    { "736N", fixupExtended736N, "updated 4/23/2019"},
	    { "737N", fixupExtended736N, "updated 4/23/2019"},
	    { "738N", fixupExtended736N, "updated 4/23/2019"},
	    { "739N", fixupExtended736N, "updated 4/23/2019"},


	    { "546L", fixup546L }, 
	    { "716L", fixup614N },
	};




	Object[][] fixupArray = {
	    { "544TX", fixup544T},
	    { "545TX", fixup544T},
	    { "546TX", fixup544T},
	    { "614TX", fixup544T},
	    { "615TX", fixup544T},
	    { "616TX", fixup544T},
	    { "617TX", fixup544T},
	    { "618TX", fixup544T},
	    { "714TX", fixup544T},
	    { "715TX", fixup544T},
	    { "716TX", fixup544T},
	    { "717TX", fixup544T},
	    { "718TX", fixup544T},
	    { "719TX", fixup544T},
	    { "724TX", fixup544T},
	    { "725TX", fixup544T},
	    { "726TX", fixup544T},
	    { "727TX", fixup544T},
	    { "728TX", fixup544T},
	    { "729TX", fixup544T},


	    { "544TS", fixup544T},
	    { "545TS", fixup544T},
	    { "546TS", fixup544T},
	    { "614TS", fixup614TS},
	    { "615TS", fixup614TS},
	    { "616TS", fixup614TS},
	    { "617TS", fixup614TS},
	    { "618TS", fixup614TS},
	    { "714TS", fixup614TS},
	    { "715TS", fixup614TS},
	    { "716TS", fixup614TS},
	    { "717TS", fixup614TS},
	    { "718TS", fixup614TS},
	    { "719TS", fixup614TS},

	    { "726TS", fixup725TS},
	    { "727TS", fixup725TS},
	    { "728TS", fixup725TS},
	    { "729TS", fixup725TS},

	    {"546NS",  fixup546N},

	    { "614NS", fixup614N},
	    { "615NS", fixup614N},
	    { "616NS", fixup614N},

	    { "714NS", fixup614N },
	    { "715NS",  fixup614N },
	    { "716NS",  fixup614N },
	    { "717NS",  fixup614N },
	    { "718NS",  fixup614N },
	    { "719NS",  fixup614N },

	    { "725NS", fixup725N},
      { "726NS", fixup726N, "Primed 11/8/2012"},
      { "727NS", fixup725N},
      { "725NX", fixup725N},
      { "726NX", fixup725N},
      { "727NX", fixup725N},
      { "728NX", fixup725N},
      { "729NX", fixup725N},



	    { "546LX", fixup546L },
	    { "546LS", fixup546L }, 
	    { "716LX", fixup614N },
	    { "716LS", fixup614N },
	    { "726LX", fixup614N },
	    { "726LS", fixup614N },



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
		    rsA[j] = dmd.getCrossReference ( catalog1,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR3",
                                                   catalog1,
                                                   JDDMDTest.COLLECTION2,
                                                   "CR4");
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
getCrossReference() - Run getCrossReference multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var039()
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
		    // System.out.println("Calling getCrossReference");
		    ResultSet rs = dmd_.getCrossReference (catalog,
							   JDDMDTest.COLLECTION2,
							   "LCNCR3",
							   catalog,
							   JDDMDTest.COLLECTION2,
							   "LCNCR4");

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
getCrossReference() - Readonly connection
**/
    public void Var040()
    {
        try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            ResultSet rs = dmd.getCrossReference (null,
                                                   JDDMDTest.COLLECTION,
                                                   "CR1",
                                                   catalog,
                                                   JDDMDTest.COLLECTION,
                                                   "CR2");

            boolean success = true;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableCat       = rs.getString ("PKTABLE_CAT");
                String pktableSchem     = rs.getString ("PKTABLE_SCHEM");
                String pktableName      = rs.getString ("PKTABLE_NAME");
                String pkcolumnName     = rs.getString ("PKCOLUMN_NAME");
                String fktableCat       = rs.getString ("FKTABLE_CAT");
                String fktableSchem     = rs.getString ("FKTABLE_SCHEM");
                String fktableName      = rs.getString ("FKTABLE_NAME");
                String fkcolumnName     = rs.getString ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (pktableName.equals ("CR1"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (fktableName.equals ("CR2"));
                success = success && (fkcolumnName.equals ("CUSTID"));

            }

            rs.close ();
	    c.close(); 
            assertCondition (success && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }





}

