///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetExportedKeys.java
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
 // File Name:    JDDMDGetExportedKeys.java
 //
 // Classes:      JDDMDGetExportedKeys
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
Testcase JDDMDGetExportedKeys.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getExportedKeys()
</ul>
**/
public class JDDMDGetExportedKeys
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private String              catalog_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    DatabaseMetaData    dmd2_;
    StringBuffer message = new StringBuffer();



/**
Constructor.
**/
    public JDDMDGetExportedKeys (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetExportedKeys",
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

        catalog_ = connection_.getCatalog();
        if (catalog_ == null) {
          catalog_ = system_.toUpperCase();
        }

        Statement s = connection_.createStatement ();

        //  Create primary key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".EK1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".EKKEY1 PRIMARY KEY (CUSTID))");

        //  Create foreign key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".EK2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".EKKEY2 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".EK1 (CUSTID) ON DELETE NO ACTION ON UPDATE NO ACTION)");

        // Create foreign key using restrict update and delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".EK5 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".EKKEY5 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".EK1 (CUSTID) ON DELETE RESTRICT ON UPDATE RESTRICT)");

        // Create foreign key using restrict update and cascade delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".EK6 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".EKKEY6 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".EK1 (CUSTID) ON DELETE CASCADE ON UPDATE RESTRICT)");

        // Create foreign key using restrict update and set default delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".EK7 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".EKKEY7 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".EK1 (CUSTID) ON DELETE SET DEFAULT ON UPDATE RESTRICT)");

        // Create primary key which will be used for set null update rule.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                           + ".EK8 (CUSTID INT NOT NULL, BALANCE DEC (6,2), "
                           + "CONSTRAINT " + JDDMDTest.COLLECTION
                           + ".EKKEY8 UNIQUE (BALANCE))");

        // Create foreign key using restrict update and set null delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".EK9 (CUSTID INT NOT NULL, BALANCE DECIMAL(6,2), "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".EKKEY9 FOREIGN KEY (BALANCE) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".EK8 (BALANCE) ON DELETE SET NULL ON UPDATE RESTRICT)");

        //  Create primary key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".EK3 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".EKKEY3 PRIMARY KEY (CUSTID))");

        //  Create foreign key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".EK4 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".EKKEY4 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION2
                         + ".EK3 (CUSTID) ON DELETE NO ACTION ON UPDATE NO ACTION)");

        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0) //@C1A
        {
            //  Create primary key.  //@C1A
            s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".LCNEK3 (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".LCNEKKEY3 PRIMARY KEY (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST))");

            //  Create foreign key.  //@C1A
            s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".LCNEK4 (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".LCNEKKEY4 FOREIGN KEY (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST) "
                         + "REFERENCES " + JDDMDTest.COLLECTION2
                         + ".LCNEK3 (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST) ON DELETE NO ACTION ON UPDATE NO ACTION)");
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

        s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
            + ".EK2 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".EKKEY2 CASCADE");
        s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
            + ".EK5 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".EKKEY5 RESTRICT");
        s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
            + ".EK6 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".EKKEY6 CASCADE");
        s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
            + ".EK7 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".EKKEY7 RESTRICT");
        s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
            + ".EK1 DROP PRIMARY KEY CASCADE");
        s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
            + ".EK4 DROP FOREIGN KEY " + JDDMDTest.COLLECTION2 + ".EKKEY4 CASCADE");
        s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2
            + ".EK3 DROP PRIMARY KEY CASCADE");
        s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
            + ".EK9 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".EKKEY9 CASCADE");
        s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION
            + ".EK8 DROP UNIQUE " + JDDMDTest.COLLECTION + ".EKKEY8");

        //@C1A
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0){
            s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2     //@C1A
                + ".LCNEK4 DROP FOREIGN KEY " + JDDMDTest.COLLECTION2 + ".LCNEKKEY4 CASCADE");
            s.executeUpdate ("ALTER TABLE " + JDDMDTest.COLLECTION2     //@C1A
                + ".LCNEK3 DROP PRIMARY KEY CASCADE");
        }

        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".EK1");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".EK2");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".EK5");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".EK6");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".EK7");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".EK8");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".EK9");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
            + ".EK3");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
            + ".EK4");

        //@C1A
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0)
        {
            s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2     //@C1A
                + ".LCNEK3");
            s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2     //@C1A
                + ".LCNEK4");
        }

        s.close ();
        connection_.close ();
    }



/**
getExportedKeys() - Check the result set format.
**/
    public void Var001()
    {
        try {
          message.setLength(0);
            ResultSet rs = dmd_.getExportedKeys (null, null, null);
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
getExportedKeys() - Verify all columns.

SQL400 - The native JDBC driver will return the names of the primary and foreign
         keys, whereas the Toolbox will return null for these values.

SQL400 - The native driver reports the update and delete rules in the following
         testcase as being no action where the toolbox driver reports them as
         being restrict.  I think the native driver is giving the right results.

SQL400 - Another difference in the rules:  The native driver reports EK7s update
         rule as being importedKeySetDefault and the toolbox reports it as importedKeySetNull.
         I don't really know who is right on that one.
**/
    public void Var002()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (null, JDDMDTest.COLLECTION,
                "EK1");
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

                success = success && (pktableCat.equals (catalog_ ));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (pktableName.equals ("EK1"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog_));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (fkcolumnName.equals ("CUSTID"));

                short keySeq            = rs.getShort ("KEY_SEQ");
                short updateRule        = rs.getShort ("UPDATE_RULE");
                short deleteRule        = rs.getShort ("DELETE_RULE");
                String fkName           = rs.getString ("FK_NAME");
                String pkName           = rs.getString ("PK_NAME");
                short deferrability     = rs.getShort ("DEFERRABILITY");


		int   intKeySeq         = rs.getInt ("KEY_SEQ");
                int   intUpdateRule     = rs.getInt ("UPDATE_RULE");
                int   intDeleteRule     = rs.getInt ("DELETE_RULE");
                int   intDeferrability  = rs.getInt ("DEFERRABILITY");

		long   longKeySeq         = rs.getLong ("KEY_SEQ");
                long   longUpdateRule     = rs.getLong ("UPDATE_RULE");
                long   longDeleteRule     = rs.getLong ("DELETE_RULE");
                long   longDeferrability  = rs.getLong ("DEFERRABILITY");

		String   stringKeySeq         = rs.getString ("KEY_SEQ");
                String   stringUpdateRule     = rs.getString ("UPDATE_RULE");
                String   stringDeleteRule     = rs.getString ("DELETE_RULE");
                String   stringDeferrability  = rs.getString ("DEFERRABILITY");


                // System.out.println (keySeq + ":" + updateRule + ":" + deleteRule + ":" + fkName + ":" + pkName + ":" + deferrability + ":");

                success = success && (keySeq == 1);

                success = success && (pkName.equals("EKKEY1"));

                success = success && (deferrability == DatabaseMetaData.importedKeyNotDeferrable);

		if (intKeySeq != 1) {
		    System.out.println("intKeySeq is "+intKeySeq+" sb 1");
		    success = false;
		}

		if (intDeferrability != DatabaseMetaData.importedKeyNotDeferrable ) {
		    System.out.println("intDeferrability = "+intDeferrability+" sb " +DatabaseMetaData.importedKeyNotDeferrable);
		    success = false;
		}

		if (longKeySeq != 1) {
		    System.out.println("longKeySeq is "+longKeySeq+" sb 1");
		    success = false;
		}

		if (longDeferrability != DatabaseMetaData.importedKeyNotDeferrable ) {
		    System.out.println("longDeferrability = "+longDeferrability+" sb " +DatabaseMetaData.importedKeyNotDeferrable);
		    success = false;
		}

		if (!stringKeySeq.equals("1")) {
		    System.out.println("stringKeySeq is "+stringKeySeq+" sb 1");
		    success = false;
		}

		if (!stringDeferrability.equals(""+DatabaseMetaData.importedKeyNotDeferrable) ) {
		    System.out.println("stringDeferrability = "+stringDeferrability+" sb " +DatabaseMetaData.importedKeyNotDeferrable);
		    success = false;
		}

                if (fktableName.equals ("EK2"))
                {
                    success = success && (fkName.equals("EKKEY2"));
                    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
                    {
                        success = success && (updateRule == DatabaseMetaData.importedKeyRestrict);
                        success = success && (deleteRule == DatabaseMetaData.importedKeyRestrict);
                    }
                    else
                    {
                        success = success && (updateRule == DatabaseMetaData.importedKeyNoAction);
                        success = success && (deleteRule == DatabaseMetaData.importedKeyNoAction);
			if (intUpdateRule != DatabaseMetaData.importedKeyNoAction) {
			    System.out.println("intUpdateRule is "+intUpdateRule+" sb "+DatabaseMetaData.importedKeyNoAction);
			    success=false;
			}
			if (intDeleteRule != DatabaseMetaData.importedKeyNoAction) {
			    System.out.println("intDeleteRule is "+intDeleteRule+" sb "+DatabaseMetaData.importedKeyNoAction);
			    success=false;
			}


			if (longUpdateRule != DatabaseMetaData.importedKeyNoAction) {
			    System.out.println("longUpdateRule is "+longUpdateRule+" sb "+DatabaseMetaData.importedKeyNoAction);
			    success=false;
			}
			if (longDeleteRule != DatabaseMetaData.importedKeyNoAction) {
			    System.out.println("longDeleteRule is "+longDeleteRule+" sb "+DatabaseMetaData.importedKeyNoAction);
			    success=false;
			}

			if (! stringUpdateRule.equals(""+ DatabaseMetaData.importedKeyNoAction)) {
			    System.out.println("stringUpdateRule is "+stringUpdateRule+" sb "+DatabaseMetaData.importedKeyNoAction);
			    success=false;
			}
			if (! stringDeleteRule.equals(""+ DatabaseMetaData.importedKeyNoAction)) {
			    System.out.println("stringDeleteRule is "+stringDeleteRule+" sb "+DatabaseMetaData.importedKeyNoAction);
			    success=false;
			}

                    }
                }
                else if (fktableName.equals ("EK5"))
                {
                    success = success && (updateRule == DatabaseMetaData.importedKeyRestrict);
                    success = success && (deleteRule == DatabaseMetaData.importedKeyRestrict);
                    success = success && (fkName.equals("EKKEY5"));
			if (intUpdateRule != DatabaseMetaData.importedKeyRestrict) {
			    System.out.println("intUpdateRule is "+intUpdateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}
			if (intDeleteRule != DatabaseMetaData.importedKeyRestrict) {
			    System.out.println("intDeleteRule is "+intDeleteRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}

			if (longUpdateRule != DatabaseMetaData.importedKeyRestrict) {
			    System.out.println("longUpdateRule is "+longUpdateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}
			if (longDeleteRule != DatabaseMetaData.importedKeyRestrict) {
			    System.out.println("longDeleteRule is "+longDeleteRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}

			if (!stringUpdateRule.equals(""+DatabaseMetaData.importedKeyRestrict)) {
			    System.out.println("stringUpdateRule is "+stringUpdateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}
			if (!stringDeleteRule.equals(""+DatabaseMetaData.importedKeyRestrict)) {
			    System.out.println("stringDeleteRule is "+stringDeleteRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}


                }
                else if (fktableName.equals ("EK6"))
                {
                    success = success && (updateRule == DatabaseMetaData.importedKeyRestrict);
                    success = success && (deleteRule == DatabaseMetaData.importedKeyCascade);
                    success = success && (fkName.equals("EKKEY6"));
			if (intUpdateRule != DatabaseMetaData.importedKeyRestrict) {
			    System.out.println("intUpdateRule is "+intUpdateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}
			if (intDeleteRule != DatabaseMetaData.importedKeyCascade) {
			    System.out.println("intDeleteRule is "+intDeleteRule+" sb "+DatabaseMetaData.importedKeyCascade);
			    success=false;
			}

			if (longUpdateRule != DatabaseMetaData.importedKeyRestrict) {
			    System.out.println("longUpdateRule is "+longUpdateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}
			if (longDeleteRule != DatabaseMetaData.importedKeyCascade) {
			    System.out.println("longDeleteRule is "+longDeleteRule+" sb "+DatabaseMetaData.importedKeyCascade);
			    success=false;
			}
			if (!stringUpdateRule.equals(""+ DatabaseMetaData.importedKeyRestrict)) {
			    System.out.println("stringUpdateRule is "+stringUpdateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}
			if (!stringDeleteRule.equals(""+ DatabaseMetaData.importedKeyCascade)) {
			    System.out.println("stringDeleteRule is "+stringDeleteRule+" sb "+DatabaseMetaData.importedKeyCascade);
			    success=false;
			}


                }
                else if (fktableName.equals ("EK7"))
                {
                    success = success && (updateRule == DatabaseMetaData.importedKeyRestrict);
                    success = success && (fkName.equals("EKKEY7"));
                    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
                    {
                        success = success && (deleteRule == DatabaseMetaData.importedKeySetNull);
                    }
                    else
                    {
                        success = success && (deleteRule == DatabaseMetaData.importedKeySetDefault);


			if (intDeleteRule != DatabaseMetaData.importedKeySetDefault) {
			    System.out.println("intDeleteRule is "+intDeleteRule+" sb "+DatabaseMetaData.importedKeySetDefault);
			    success=false;
			}

			if (longDeleteRule != DatabaseMetaData.importedKeySetDefault) {
			    System.out.println("longDeleteRule is "+longDeleteRule+" sb "+DatabaseMetaData.importedKeySetDefault);
			    success=false;
			}

			if (!stringDeleteRule.equals(""+ DatabaseMetaData.importedKeySetDefault)) {
			    System.out.println("stringDeleteRule is "+stringDeleteRule+" sb "+DatabaseMetaData.importedKeySetDefault);
			    success=false;
			}

                    }

		    if (intUpdateRule != DatabaseMetaData.importedKeyRestrict) {
			System.out.println("intUpdateRule is "+intUpdateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			success=false;
		    }
		    if (longUpdateRule != DatabaseMetaData.importedKeyRestrict) {
			System.out.println("longUpdateRule is "+longUpdateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			success=false;
		    }
		    if (!stringUpdateRule.equals(""+DatabaseMetaData.importedKeyRestrict)) {
			System.out.println("stringUpdateRule is "+stringUpdateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			success=false;
		    }

                }
            }

            // System.out.println ("Rows = " + rows);

            rs.close ();
            assertCondition ((rows == 4) && success);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }


/**
getExportedKeys() - Specify all null parameters.  Verify no rows are returned.
**/
    public void Var003()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (null, null, null);

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
getExportedKeys() - Specify null for the catalog.
**/
    public void Var004()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (null, JDDMDTest.COLLECTION, "EK1");

            boolean check2 = false;
            boolean check5 = false;
            boolean check6 = false;
            boolean check7 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String fktableName = rs.getString ("FKTABLE_NAME");
                if (fktableName.equals ("EK2"))
                    check2 = true;
                else if (fktableName.equals ("EK5"))
                    check5 = true;
                else if (fktableName.equals ("EK6"))
                    check6 = true;
                else if (fktableName.equals ("EK7"))
                    check7 = true;
            }

            rs.close ();
            assertCondition (check2 && check5 && check6 && check7 && (rows == 4));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getExportedKeys() - Specify empty string for the catalog.
**/
    public void Var005()
    {
        String queryRows="";
        try {
            ResultSet rs = dmd_.getExportedKeys ("", JDDMDTest.COLLECTION, "EK1");

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                queryRows+=rows+":"+ rs.getString(1)+" | "+
                                     rs.getString(2)+" | "+
                                     rs.getString(3)+" | "+
                                     rs.getString(4)+" | "+
                                     rs.getString(5)+" | "+
                                     rs.getString(6)+" | "+
                                     rs.getString(7)+" | "+
                                     rs.getString(8)+" | "+
                                     rs.getString(9)+" | "+
                                     rs.getString(10)+" | "+
                                     rs.getString(11)+" | "+
                                     rs.getString(12)+" | "+
                                     rs.getString(13)+" | "+
                                     rs.getString(14)+"\n";

            }

            rs.close ();
	    if (JDTestDriver.isLUW() || isSysibmMetadata()) {
		assertCondition (rows > 0, "Empty string specified for catalog "+
				 "  > 0  rows should be found rows="+rows+" since running LUW or sysibm metadata");

	    } else {
		assertCondition (rows == 0, "Empty string specified for catalog "+
				 "  zero rows should be found rows="+rows+" "+
				 queryRows); ;
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getExportedKeys() - Specify a catalog that matches the catalog
exactly.
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
                                                 JDDMDTest.COLLECTION, "EK1");

            boolean check2 = false;
            boolean check5 = false;
            boolean check6 = false;
            boolean check7 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String fktableName = rs.getString ("FKTABLE_NAME");
                if (fktableName.equals ("EK2"))
                    check2 = true;
                else if (fktableName.equals ("EK5"))
                    check5 = true;
                else if (fktableName.equals ("EK6"))
                    check6 = true;
                else if (fktableName.equals ("EK7"))
                    check7 = true;
            }

            rs.close ();
            assertCondition (check2 && check5 && check6 && check7 && (rows == 4));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getExportedKeys() - Specify "localhost" for the catalog.
**/
  public void Var007() {
    if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4 ||
        ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getRelease() >= JDTestDriver.RELEASE_V5R5M0))
        ||(getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
      notApplicable("\"localhost\" variation ");
    } else {
      try {
        ResultSet rs = dmd_.getExportedKeys("localhost", JDDMDTest.COLLECTION,
            "EK1");

        boolean check2 = false;
        boolean check5 = false;
        boolean check6 = false;
        boolean check7 = false;
        int rows = 0;
        while (rs.next()) {
          ++rows;
          String fktableName = rs.getString("FKTABLE_NAME");
          if (fktableName.equals("EK2"))
            check2 = true;
          else if (fktableName.equals("EK5"))
            check5 = true;
          else if (fktableName.equals("EK6"))
            check6 = true;
          else if (fktableName.equals("EK7"))
            check7 = true;
        }

        rs.close();
        assertCondition(check2 && check5 && check6 && check7 && (rows == 4),
            " check2=" + check2 + " check5=" + check5 + " check6=" + check6
                + " check7=" + check7 + " rows=" + rows + " sb 4");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }



/**
 * getExportedKeys() - Specify a catalog pattern for which there is a match. No
 * matching rows should be returned, since we do not support catalog pattern.
 */
    public void Var008()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_.substring (0, 4) + "%",
                JDDMDTest.COLLECTION, "EK1");

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
getExportedKeys() - Specify a catalog for which there is no match.
No matching columns should be returned.
**/
    public void Var009()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys ("BOGUS",
                JDDMDTest.COLLECTION, "EK1");

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
getExportedKeys() - Specify null for the schema.
**/
    public void Var010()
    {
	StringBuffer sb = new StringBuffer(" -- \n");
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
                                                 null, "EK1");

            boolean check2 = false;
            boolean check5 = false;
            boolean check6 = false;
            boolean check7 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
		String fkTableSchem = rs.getString ("FKTABLE_SCHEM");
                String fktableName = rs.getString ("FKTABLE_NAME");
		if (fkTableSchem.equals(JDDMDTest.COLLECTION) && fktableName.equals ("EK2")) {
		    sb.append("Found expected    fktableName="+fkTableSchem+"/"+fktableName+"\n");
                    check2 = true;
		} else if (fkTableSchem.equals(JDDMDTest.COLLECTION) &&fktableName.equals ("EK5")) {
		    sb.append("Found expected    fktableName="+fkTableSchem+"/"+fktableName+"\n");
                    check5 = true;
		} else if (fkTableSchem.equals(JDDMDTest.COLLECTION) &&fktableName.equals ("EK6")) {
		    sb.append("Found expected    fktableName="+fkTableSchem+"/"+fktableName+"\n");
                    check6 = true;
		} else if (fkTableSchem.equals(JDDMDTest.COLLECTION) &&fktableName.equals ("EK7")) {
		    sb.append("Found expected    fktableName="+fkTableSchem+"/"+fktableName+"\n");
                    check7 = true;
		} else {
		    sb.append("Did not check for fktableName="+fkTableSchem+"/"+fktableName+"\n");
		}
            }

            rs.close ();
            assertCondition (check2 && check5 && check6 && check7 && (rows >= 4), "check2="+check2+" check5="+check5+" check6="+check6+" check7="+check7+" rows="+rows+" sb >= 4 "+sb.toString());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getExportedKeys() - Specify empty string for the schema.
**/
    public void Var011()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
                "", "EK1");

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
getExportedKeys() - Specify a schema that matches the schema
exactly.
**/
    public void Var012()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
                                                 JDDMDTest.COLLECTION, "EK1");

            boolean check2 = false;
            boolean check5 = false;
            boolean check6 = false;
            boolean check7 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String fktableName = rs.getString ("FKTABLE_NAME");
                if (fktableName.equals ("EK2"))
                    check2 = true;
                else if (fktableName.equals ("EK5"))
                    check5 = true;
                else if (fktableName.equals ("EK6"))
                    check6 = true;
                else if (fktableName.equals ("EK7"))
                    check7 = true;
            }

            rs.close ();
            assertCondition (check2 && check5 && check6 && check7 && (rows == 4));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getExportedKeys() - Specify a schema pattern for which there is a
match.  An exception should be thrown, since we do not
support schema pattern.

SQL400 - for functions that call the SQLForeignKeys() CLI function,
         the native driver can handle the search pattern.  We check
         to make sure that the row gets returned.
**/
    public void Var013()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
                JDDMDTest.SCHEMAS_PERCENT, "EK1");

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
		if ((getDriver () == JDTestDriver.DRIVER_NATIVE &&
                    (getRelease() < JDTestDriver.RELEASE_V5R5M0 &&
                       getJdbcLevel() < 4)) ) {
                assertCondition (rows > 0, "rows="+rows+" sb > 0");
		} else {
                assertCondition (rows == 0, "rows="+rows+" sb 0 pattern specified for schema ");
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
getExportedKeys() - Specify a schema for which there is no match.
No rows should be returned.
**/
    public void Var014()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
                "BOGUS", "EK1");

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
getExportedKeys() - Specify null for the table.
No rows should be returned.
**/
    public void Var015()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
                JDDMDTest.COLLECTION, null);

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
getExportedKeys() - Specify empty string for the table.
**/
    public void Var016()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
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
getExportedKeys() - Specify a table that matches the table
exactly.  All matching columns should be returned.
**/
    public void Var017()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
                JDDMDTest.COLLECTION2, "EK3");

            boolean check4 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String fktableName = rs.getString ("FKTABLE_NAME");
                if (fktableName.equals ("EK4"))
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
getExportedKeys() - Specify a table pattern for which there is a
match.  An exception should be thrown, since we do not
support table pattern.

SQL400 - for functions that call the SQLForeignKeys() CLI function,
         the native driver can handle the search pattern.  We check
         to make sure that the row gets returned.
**/
    public void Var018()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
                JDDMDTest.COLLECTION, "EK%");

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
                if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
                    (getRelease() < JDTestDriver.RELEASE_V5R5M0 &&
                     getJdbcLevel() < 4 )) {
                  assertCondition (rows > 0, "rows="+rows+" sb > 0" );
                } else {
                  // Nothing should be found since the API doesn't support matching
                  assertCondition (rows == 0, "rows="+rows+" sb = 0 table pattern specified" );

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
getExportedKeys() - Specify a table for which there is no match.
No rows should be returned.
**/
    public void Var019()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (catalog_,
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
getExportedKeys() - Specify a table that matches the table
exactly.  All matching columns should be returned.   Test for 128 byte column names
**/
    public void Var020()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0)
        {
            try {
                ResultSet rs = dmd_.getExportedKeys (catalog_,
                    JDDMDTest.COLLECTION2, "LCNEK3");

                boolean success = false;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    String fktableName = rs.getString ("FKTABLE_NAME");
                    if (fktableName.equals ("LCNEK4"))
                        success = true;
                    String pktableName = rs.getString("PKTABLE_NAME");
                    success = success && pktableName.equals("LCNEK3");
                    String pkcolumnName = rs.getString("PKCOLUMN_NAME");
                    success = success && pkcolumnName.equals("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST");
                    String fkcolumnName = rs.getString("FKCOLUMN_NAME");
                    success = success && fkcolumnName.equals("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST");
                }

                rs.close ();
                assertCondition (success && (rows == 1), "Added by Toolbox 8/11/2004 for testing 128 byte column names.");
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception.  Added by Toolbox 8/11/2004 for testing 128 byte column names.");
            }
        }
        else
            notApplicable("V5R4 and greater variation");
    }

    /* use getObject -- version of Var002 added by native 5/2/32008 */

    public void Var021()
    {
        try {
            ResultSet rs = dmd_.getExportedKeys (null, JDDMDTest.COLLECTION,
                "EK1");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;

                String pktableCat       = (String) rs.getString ("PKTABLE_CAT");
                String pktableSchem     = (String) rs.getObject ("PKTABLE_SCHEM");
                String pktableName      = (String) rs.getObject ("PKTABLE_NAME");
                String pkcolumnName     = (String) rs.getObject ("PKCOLUMN_NAME");
                String fktableCat       = (String) rs.getObject ("FKTABLE_CAT");
                String fktableSchem     = (String) rs.getObject ("FKTABLE_SCHEM");
                String fktableName      = (String) rs.getObject ("FKTABLE_NAME");
                String fkcolumnName     = (String) rs.getObject ("FKCOLUMN_NAME");

                // System.out.println (pktableCat + ":" + pktableSchem + ":" + pktableName + ":" + pkcolumnName + ":");
                // System.out.println (fktableCat + ":" + fktableSchem + ":" + fktableName + ":" + fkcolumnName + ":");

                success = success && (pktableCat.equals (catalog_ ));
                success = success && (pktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (pktableName.equals ("EK1"));
                success = success && (pkcolumnName.equals ("CUSTID"));
                success = success && (fktableCat.equals (catalog_));
                success = success && (fktableSchem.equals (JDDMDTest.COLLECTION));
                success = success && (fkcolumnName.equals ("CUSTID"));

                Integer keySeq            = (Integer) rs.getObject ("KEY_SEQ");
                Integer updateRule        = (Integer) rs.getObject ("UPDATE_RULE");
                Integer deleteRule        = (Integer) rs.getObject ("DELETE_RULE");
                String fkName           = (String) rs.getObject ("FK_NAME");
                String pkName           = (String) rs.getObject ("PK_NAME");
                Integer deferrability     = (Integer) rs.getObject ("DEFERRABILITY");



                // System.out.println (keySeq + ":" + updateRule + ":" + deleteRule + ":" + fkName + ":" + pkName + ":" + deferrability + ":");


                success = success && (pkName.equals("EKKEY1"));



		if (keySeq.intValue() != 1) {
		    System.out.println("keySeq is "+keySeq+" sb 1");
		    success = false;
		}

		if (deferrability.intValue() != DatabaseMetaData.importedKeyNotDeferrable ) {
		    System.out.println("deferrability = "+deferrability+" sb " +DatabaseMetaData.importedKeyNotDeferrable);
		    success = false;
		}


                if (fktableName.equals ("EK2"))
                {
                    success = success && (fkName.equals("EKKEY2"));
                    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
                    {
                        success = success && (updateRule.intValue() == DatabaseMetaData.importedKeyRestrict);
                        success = success && (deleteRule.intValue() == DatabaseMetaData.importedKeyRestrict);
                    }
                    else
                    {


			if (updateRule.intValue() != DatabaseMetaData.importedKeyNoAction) {
			    System.out.println("updateRule is "+updateRule+" sb "+DatabaseMetaData.importedKeyNoAction);
			    success=false;
			}
			if (deleteRule.intValue() != DatabaseMetaData.importedKeyNoAction) {
			    System.out.println("deleteRule is "+deleteRule+" sb "+DatabaseMetaData.importedKeyNoAction);
			    success=false;
			}

                    }
                }
                else if (fktableName.equals ("EK5"))
                {


                    success = success && (fkName.equals("EKKEY5"));
			if (updateRule.intValue() != DatabaseMetaData.importedKeyRestrict) {
			    System.out.println("updateRule is "+updateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}
			if (deleteRule.intValue() != DatabaseMetaData.importedKeyRestrict) {
			    System.out.println("deleteRule is "+deleteRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}



                }
                else if (fktableName.equals ("EK6"))
                {


                    success = success && (fkName.equals("EKKEY6"));
			if (updateRule.intValue() != DatabaseMetaData.importedKeyRestrict) {
			    System.out.println("updateRule is "+updateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			    success=false;
			}
			if (deleteRule.intValue() != DatabaseMetaData.importedKeyCascade) {
			    System.out.println("deleteRule is "+deleteRule+" sb "+DatabaseMetaData.importedKeyCascade);
			    success=false;
			}
                }
                else if (fktableName.equals ("EK7"))
                {

                    success = success && (fkName.equals("EKKEY7"));
                    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
                    {
                        success = success && (deleteRule.intValue() == DatabaseMetaData.importedKeySetNull);
                    }
                    else
                    {



			if (deleteRule.intValue() != DatabaseMetaData.importedKeySetDefault) {
			    System.out.println("deleteRule is "+deleteRule+" sb "+DatabaseMetaData.importedKeySetDefault);
			    success=false;
			}


                    }

		    if (updateRule.intValue() != DatabaseMetaData.importedKeyRestrict) {
			System.out.println("updateRule is "+updateRule+" sb "+DatabaseMetaData.importedKeyRestrict);
			success=false;
		    }
                }
            }

            // System.out.println ("Rows = " + rows);

            rs.close ();
            assertCondition ((rows == 4) && success);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }

/**
getExportedKeys() - Run getExportedKeys multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var022()
    {
	String added = " -- added by 1/31/2011 to test for native statement leak in metadata";
        if (checkNative()) {
            try {

		Connection connection = testDriver_.getConnection (baseURL_
							 + ";prefetch=false;libraries=" + JDDMDTest.COLLECTION + " "
							 + JDDMDTest.COLLECTION2 + " " + JDDMDTest.COLLECTIONXX,
							 userId_, encryptedPassword_);

		System.gc();
		Statement fill[] = new Statement[20];
		for (int i = 0; i < fill.length; i++) {
		    fill[i] = connection.createStatement();
		}

		Statement stmt = connection.createStatement();

		DatabaseMetaData dmd = connection.getMetaData ();
		for (int i = 0; i < 1000; i++) {
		    // System.out.println("Calling getExportedKeys");
		    ResultSet rs = dmd.getExportedKeys (null, JDDMDTest.COLLECTION,
							 "EK1");
		    rs.close();
		}

		Statement stmt2 = connection.createStatement();
		int beginningHandle = JDReflectionUtil.callMethod_I(stmt,"getStatementHandle");
		int endingHandle = JDReflectionUtil.callMethod_I(stmt2,"getStatementHandle");


		for (int i = 0; i < fill.length; i++) {
		    fill[i].close();
		}

		connection.close();
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




  public void Var023() {
	  checkRSMD(false);
  }
  public void Var024() {
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

	/* Primed from 546CN */ 
	String [][] methodTests = {
      {"isAutoIncrement","1","false"},
      {"isCaseSensitive","1","true"},
      {"isSearchable","1","true"},
      {"isCurrency","1","false"},
      {"isNullable","1","0"},
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
      {"isNullable","2","0"},
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
      {"isNullable","5","0"},
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
      {"isNullable","6","0"},
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
      {"getColumnClassName","9","java.lang.Integer"},
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
      {"getColumnClassName","10","java.lang.Integer"},
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
      {"getColumnClassName","11","java.lang.Integer"},
      {"isAutoIncrement","12","false"},
      {"isCaseSensitive","12","true"},
      {"isSearchable","12","true"},
      {"isCurrency","12","false"},
      {"isNullable","12","0"},
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
      {"isNullable","13","0"},
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
      {"getColumnClassName","14","java.lang.Integer"},


	};







	String [][] fixup = {};
	String [][] fixupExtended545N = {
	    {"isSearchable","1","false"},
	    {"isNullable","1","1"},
	    {"isSearchable","2","false"},
	    {"isNullable","2","1"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isNullable","5","1"},
	    {"isSearchable","6","false"},
	    {"isNullable","6","1"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"getColumnClassName","9","java.lang.Short"},
	    {"isSearchable","10","false"},
	    {"getColumnClassName","10","java.lang.Short"},
	    {"isSearchable","11","false"},
	    {"getColumnClassName","11","java.lang.Short"},
	    {"isSearchable","12","false"},
	    {"isNullable","12","1"},
	    {"isSearchable","13","false"},
	    {"isNullable","13","1"},
	    {"getColumnClassName","14","java.lang.Short"},
	};

	String [][] fixupExtended5440 = {
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
	};



	String[][] fixupExtended616N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"getColumnLabel","2","LIBRARY             NAME"},
	    {"isSearchable","3","false"},
	    {"getColumnLabel","3","LONG                FILE                NAME"},
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
	    {"isNullable","13","1"},
	    {"isSearchable","14","false"},


	}; 


	String[][] fixupExtended736N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"getColumnLabel","2","LIBRARY             NAME"},
	    {"isSearchable","3","false"},
	    {"getColumnLabel","3","LONG                FILE                NAME"},
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
	    {"isNullable","13","1"},
	    {"isSearchable","14","false"},

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

	String[][] fixupExtended715T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"getColumnLabel","2","LIBRARY NAME"},
	    {"getColumnLabel","3","LONG FILE NAME"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"isNullable","12","0"},
	    {"isNullable","13","1"},
	} ;


	String[][] fixupExtended736T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"isNullable","12","0"},
	    {"isNullable","13","1"},
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
	} ; 

	String [][] fixup545T={
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"isNullable","5","1"},
	    {"isNullable","6","1"},
	    {"isNullable","12","1"},
	    {"isNullable","13","1"},
	};

	String[][] fixup715TS = {
	    {"isNullable","13","1"},
	}; 

	String [][] fixup545NX = {
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"isNullable","5","1"},
	    {"isNullable","6","1"},
	    {"getColumnClassName","9","java.lang.Short"},
	    {"getColumnClassName","10","java.lang.Short"},
	    {"getColumnClassName","11","java.lang.Short"},
	    {"isNullable","12","1"},
	    {"isNullable","13","1"},
	    {"getColumnClassName","14","java.lang.Short"},
	}; 

	String[][] fixup616NS = {
	    {"isNullable","13","1"},
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
	    {"716T", fixupExtended715T},
	    {"717T", fixupExtended715T},
	    {"718T", fixupExtended715T},
	    {"719T", fixupExtended715T},


	    {"726T", fixupExtended715T},
	    {"727T", fixupExtended715T},
	    {"728T", fixupExtended715T},
	    {"729T", fixupExtended715T},

	    {"736T", fixupExtended736T},
	    {"737T", fixupExtended736T},
	    {"738T", fixupExtended736T},
	    {"739T", fixupExtended736T},

	    {"544N", fixupExtended545N},
	    {"545N", fixupExtended545N}, 
	    {"546N", fixupExtended5440},
	    {"614N", fixupExtended616N},
	    {"615N", fixupExtended616N},
	    {"616N", fixupExtended616N},
	    {"714N", fixupExtended616N},
	    {"715N", fixupExtended616N},
	    {"716N", fixupExtended616N},
	    {"717N", fixupExtended616N, "08/09/2012 -- Primed" },
	    {"718N", fixupExtended616N, "08/09/2012 -- Primed" },
	    {"719N", fixupExtended616N, "08/09/2012 -- Primed" },
	    

	    {"725N", fixupExtended616N},
	    {"726N", fixupExtended616N},
	    {"727N", fixupExtended616N},
	    {"728N", fixupExtended616N},
	    {"729N", fixupExtended616N},


	    {"736N", fixupExtended736N},
	    {"737N", fixupExtended736N},
	    {"738N", fixupExtended736N},
	    {"739N", fixupExtended736N},

	    {"716L", fixup616NS},
	    {"726L", fixup616NS}, 
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

	    {"714TS", fixup715TS, "Prime 08/21/2012"},
	    {"715TS", fixup715TS}, 
	    {"716TS", fixup715TS, "Guess 08/21/2012 from 714TS"},
	    {"717TS", fixup715TS, "Guess 08/21/2012 from 714TS"},
	    {"718TS", fixup715TS, "Guess 08/21/2012 from 714TS"},
	    {"719TS", fixup715TS, "Guess 08/21/2012 from 714TS"},

 	    {"724TX", fixup545T},
	    {"725TX", fixup545T},
	    {"726TX", fixup545T},
	    {"727TX", fixup545T},
	    {"728TX", fixup545T},
	    {"729TX", fixup545T},

	    {"736TX", fixup545T},
	    {"737TX", fixup545T},
	    {"738TX", fixup545T},
	    {"739TX", fixup545T},

	    {"544NX", fixup545NX},
	    {"545NX", fixup545NX}, 
	    {"614NS", fixup616NS},
	    {"615NS", fixup616NS},
	    {"616NS", fixup616NS}, 
	    {"714NS", fixup616NS},
	    {"715NS", fixup616NS},
	    {"716NS", fixup616NS},
	    {"717NS", fixup616NS, "08/09/2012 -- primed"},
	    {"718NS", fixup616NS, "08/09/2012 -- primed"},
	    {"719NS", fixup616NS, "08/09/2012 -- primed"},

	    {"725NS", fixup616NS},
	    {"726NS", fixup616NS},
	    {"727NS", fixup616NS},
	    {"728NS", fixup616NS},
	    {"729NS", fixup616NS},

	    {"736NS", fixup616NS},
	    {"737NS", fixup616NS},
	    {"738NS", fixup616NS},
	    {"739NS", fixup616NS},

	    {"716LS", fixup616NS},
	    {"726LS", fixup616NS}, 
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
		    rsA[j] = dmd.getExportedKeys (null, JDDMDTest.COLLECTION,
							 "EK1");

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
getExportedKeys() - Use readonly connection
**/
    public void Var025()
    {
        try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            ResultSet rs = dmd.getExportedKeys (null, JDDMDTest.COLLECTION, "EK1");

            boolean check2 = false;
            boolean check5 = false;
            boolean check6 = false;
            boolean check7 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String fktableName = rs.getString ("FKTABLE_NAME");
                if (fktableName.equals ("EK2"))
                    check2 = true;
                else if (fktableName.equals ("EK5"))
                    check5 = true;
                else if (fktableName.equals ("EK6"))
                    check6 = true;
                else if (fktableName.equals ("EK7"))
                    check7 = true;
            }

            rs.close ();
	    c.close(); 
            assertCondition (check2 && check5 && check6 && check7 && (rows == 4));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }




}
