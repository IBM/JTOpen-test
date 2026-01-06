///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetImportedKeys.java
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
 // File Name:    JDDMDGetImportedKeys.java
 //
 // Classes:      JDDMDGetImportedKeys
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
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDDMDGetImportedKeys.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getImportedKeys()
</ul>
**/
public class JDDMDGetImportedKeys
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetImportedKeys";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }



    // Private data.
    private String              catalog_ = null;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    StringBuffer message = new StringBuffer();



/**
Constructor.
**/
    public JDDMDGetImportedKeys (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetImportedKeys",
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
	    if (! JDTestDriver.isLUW()) {
		catalog_ = system_.toUpperCase();
	    }
        }


        Statement s = connection_.createStatement ();

        //  Create primary key.
	if (JDTestDriver.isLUW()) {
	    // LUW doesn't allow constraint to be qualified. -- GETS SQL0108
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".IK1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
			     + "CONSTRAINT IKKEY1 PRIMARY KEY (CUSTID))");


	//  Create foreign key.
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".IK2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, "
			     + "CONSTRAINT IKKEY2 FOREIGN KEY (CUSTID) "
			     + "REFERENCES " + JDDMDTest.COLLECTION
			     + ".IK1 (CUSTID) ON DELETE NO ACTION ON UPDATE NO ACTION)");


        // Create foreign key using restrict update and delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".IK5 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT IKKEY5 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".IK1 (CUSTID) ON DELETE RESTRICT ON UPDATE RESTRICT)");

        // Create foreign key using restrict update and cascade delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".IK6 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT IKKEY6 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".IK1 (CUSTID) ON DELETE CASCADE ON UPDATE RESTRICT)");

        // Create foreign key using restrict update and set default delete rules.
	// LUW doesn't have SET DEFAULT cannot change to SET NULL -- changed to CASCADE
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".IK7 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT IKKEY7 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".IK1 (CUSTID) ON DELETE CASCADE ON UPDATE RESTRICT)");

        // Create primary key which will be used for set null update rule.
	// LUW requires NOT NULL for BALANCE.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                           + ".IK8 (CUSTID INT NOT NULL, BALANCE DEC (6,2) NOT NULL , "
                           + "CONSTRAINT IKKEY8 UNIQUE (BALANCE))");

        // Create foreign key using restrict update and set null delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".IK9 (CUSTID INT NOT NULL, BALANCE DECIMAL(6,2), "
                         + "CONSTRAINT IKKEY9 FOREIGN KEY (BALANCE) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".IK8 (BALANCE) ON DELETE SET NULL ON UPDATE RESTRICT)");

        //  Create primary key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".IK3 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT IKKEY3 PRIMARY KEY (CUSTID))");

        //  Create foreign key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".IK4 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT IKKEY4 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION2
                         + ".IK3 (CUSTID) ON DELETE NO ACTION ON UPDATE NO ACTION)");


	} else {
	    s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
			     + ".IK1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
			     + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
			     + "CONSTRAINT " + JDDMDTest.COLLECTION
			     + ".IKKEY1 PRIMARY KEY (CUSTID))");

        //  Create foreign key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".IK2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".IKKEY2 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".IK1 (CUSTID) ON DELETE NO ACTION ON UPDATE NO ACTION)");


        // Create foreign key using restrict update and delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".IK5 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".IKKEY5 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".IK1 (CUSTID) ON DELETE RESTRICT ON UPDATE RESTRICT)");

        // Create foreign key using restrict update and cascade delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".IK6 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".IKKEY6 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".IK1 (CUSTID) ON DELETE CASCADE ON UPDATE RESTRICT)");

        // Create foreign key using restrict update and set default delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".IK7 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".IKKEY7 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".IK1 (CUSTID) ON DELETE SET DEFAULT ON UPDATE RESTRICT)");

        // Create primary key which will be used for set null update rule.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                           + ".IK8 (CUSTID INT NOT NULL, BALANCE DEC (6,2), "
                           + "CONSTRAINT " + JDDMDTest.COLLECTION
                           + ".IKKEY8 UNIQUE (BALANCE))");

        // Create foreign key using restrict update and set null delete rules.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
                         + ".IK9 (CUSTID INT NOT NULL, BALANCE DECIMAL(6,2), "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION
                         + ".IKKEY9 FOREIGN KEY (BALANCE) "
                         + "REFERENCES " + JDDMDTest.COLLECTION
                         + ".IK8 (BALANCE) ON DELETE SET NULL ON UPDATE RESTRICT)");

        //  Create primary key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".IK3 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".IKKEY3 PRIMARY KEY (CUSTID))");

        //  Create foreign key.
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".IK4 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".IKKEY4 FOREIGN KEY (CUSTID) "
                         + "REFERENCES " + JDDMDTest.COLLECTION2
                         + ".IK3 (CUSTID) ON DELETE NO ACTION ON UPDATE NO ACTION)");


	}




        //@C1A
        if(true){
	    if (JDTestDriver.isLUW()) {
		// LUW doesn't support long names..
	    } else {
            //  Create primary key.                                                 //@C1A
            s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".LCNIK3 (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".LCNIKKEY3 PRIMARY KEY (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST))");

            //  Create foreign key.                                                //@C1A
            s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
                         + ".LCNIK4 (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INT NOT NULL, NAME CHAR(10) NOT NULL, "
                         + "ACCTNBR INT NOT NULL WITH DEFAULT, "
                         + "CONSTRAINT " + JDDMDTest.COLLECTION2
                         + ".LCNIKKEY4 FOREIGN KEY (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST) "
                         + "REFERENCES " + JDDMDTest.COLLECTION2
                         + ".LCNIK3 (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST) ON DELETE NO ACTION ON UPDATE NO ACTION)");
	    }
        }

        s.close ();

        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        closedConnection_.close ();
    }


/**
    execute a statement during cleanup
**/
    protected void executeDuringCleanup(Statement s, String sql) {
	try {
	    s.executeUpdate(sql);
	} catch (Exception e) {
	    output_.println("------------------------------------------------------");
	    output_.println("Exception on "+sql);
	    output_.println("------------------------------------------------------");
	    e.printStackTrace();
	}
    }


/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
	String sql;
        Statement s = connection_.createStatement ();


	if (JDTestDriver.isLUW()) {
	    // LUW doesn't like CASCADE/RESTRICT and SCHEMA name
	    sql = "ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".IK2 DROP FOREIGN KEY IKKEY2 ";
	    executeDuringCleanup(s,sql);

	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK5 DROP FOREIGN KEY IKKEY5");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK6 DROP FOREIGN KEY IKKEY6 ");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK7 DROP FOREIGN KEY IKKEY7");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK1 DROP PRIMARY KEY ");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION2
				 + ".IK4 DROP FOREIGN KEY IKKEY4 ");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION2
				 + ".IK3 DROP PRIMARY KEY ");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK9 DROP FOREIGN KEY IKKEY9");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK8 DROP UNIQUE IKKEY8");


	} else {
	    sql = "ALTER TABLE " + JDDMDTest.COLLECTION
			     + ".IK2 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".IKKEY2 CASCADE";
	    executeDuringCleanup(s,sql);

	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK5 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".IKKEY5 RESTRICT");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK6 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".IKKEY6 CASCADE");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK7 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".IKKEY7 RESTRICT");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK1 DROP PRIMARY KEY CASCADE");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION2
				 + ".IK4 DROP FOREIGN KEY " + JDDMDTest.COLLECTION2 + ".IKKEY4 CASCADE");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION2
				 + ".IK3 DROP PRIMARY KEY CASCADE");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK9 DROP FOREIGN KEY " + JDDMDTest.COLLECTION + ".IKKEY9 CASCADE");
	    executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION
				 + ".IK8 DROP UNIQUE " + JDDMDTest.COLLECTION + ".IKKEY8");

	}

        //@C1A
        if(true)
        {
	    if (JDTestDriver.isLUW()) {
		// LUW doesn't support long names
	    } else {
		executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION2              //@C1A
				     + ".LCNIK4 DROP FOREIGN KEY " + JDDMDTest.COLLECTION2 + ".LCNIKKEY4 CASCADE");
		executeDuringCleanup(s, "ALTER TABLE " + JDDMDTest.COLLECTION2              //@C1A
				     + ".LCNIK3 DROP PRIMARY KEY CASCADE");
	    }
        }

        executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION
            + ".IK1");
        executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION
            + ".IK2");
        executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION
            + ".IK5");
        executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION
            + ".IK6");
        executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION
            + ".IK7");
        executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION
            + ".IK8");
        executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION
            + ".IK9");
        executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION2
            + ".IK3");
        executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION2
            + ".IK4");
        //@C1A
        if(true)
        {
	    if (JDTestDriver.isLUW()) {
		// LUW doesn't support long names
	    } else {
		executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION2  //@C1A
				     + ".LCNIK3");
		executeDuringCleanup(s, "DROP TABLE " + JDDMDTest.COLLECTION2  //@C1A
				     + ".LCNIK4");
	    }
        }

        s.close ();
        connection_.close ();
        connection_ = null; 

    }



/**
getImportedKeys() - Check the result set format.
**/
    public void Var001()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (null, null, null);
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
getImportedKeys() - Verify all columns.

SQL400 - The native JDBC driver will return the names of the primary and foreign
         keys, whereas the Toolbox will return null for these values.

SQL400 - The native driver reports the update and delete rules in the following
         testcase as being no action where the toolbox driver reports them as
         being restrict.  I think the native driver is giving the right results.
**/
    public void Var002()    {
        message.setLength(0);
        try {
            ResultSet rs = dmd_.getImportedKeys (null, JDDMDTest.COLLECTION,
                "IK2");
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

                // output_.println (pktableCat + ":" + pktableSchem + ":" +
                // pktableName + ":" + pkcolumnName + ":");
                // output_.println (fktableCat + ":" + fktableSchem + ":" +
                // fktableName + ":" + fkcolumnName + ":");

		if (pktableCat == null && catalog_ == null) {
		} else if (pktableCat == null || ( !(pktableCat.equals (catalog_)))) {
		    success=false;
		    message.append("\npktableCat="+pktableCat+" sb "+catalog_);
		}
		if (!(pktableSchem.equals (JDDMDTest.COLLECTION))) {
		    success=false;
		    message.append("\npktableSchem="+pktableSchem+" sb "+JDDMDTest.COLLECTION);
		}

		if (!(pkcolumnName.equals ("CUSTID"))) {
		    success=false;
		    message.append("\npkcolumnName="+pkcolumnName+" sb "+"CUSTID");
		}

		if (fktableCat==null && catalog_ == null) {
		} else {
		    if (fktableCat == null || !(fktableCat.equals (catalog_))) {
			success=false;
			message.append("\nfktableCat="+fktableCat+" sb "+catalog_);
		    }
		}
	    	if (!(fktableSchem.equals (JDDMDTest.COLLECTION))) {
		    success=false;
		    message.append("\nfktableSchem="+fktableSchem+" sb "+JDDMDTest.COLLECTION);
		}
	    	if (!(fktableName.equals ("IK2"))) {
		    success=false;
		    message.append("\nfktableName="+fktableName+" sb "+"IK2");
		}
	    	if (!(fkcolumnName.equals ("CUSTID"))) {
		    success=false;
		    message.append("\nfkcolumnName="+fkcolumnName+" sb "+"CUSTID");
		}

                short keySeq            = rs.getShort ("KEY_SEQ");
                short updateRule        = rs.getShort ("UPDATE_RULE");
                short deleteRule        = rs.getShort ("DELETE_RULE");
                String fkName           = rs.getString ("FK_NAME");
                String pkName           = rs.getString ("PK_NAME");
                short deferrability     = rs.getShort ("DEFERRABILITY");

             // output_.println (keySeq + ":" + updateRule + ":" +
              // deleteRule + ":" + fkName + ":" + pkName + ":" + deferrability
              // + ":");
		if (!(keySeq == 1)) {
		    success=false;
		    message.append("\nkeySeq="+keySeq+" sb 1");
		}
		if (!  (fkName.equals("IKKEY2"))) {
		    success = false;
		    message.append("\nfkName="+fkName+" sb IKKEY2");
		}
		if (!((deferrability == DatabaseMetaData.importedKeyNotDeferrable))) {
		    success = false;
		    message.append( "\ndefferability="+deferrability+" sb " +DatabaseMetaData.importedKeyNotDeferrable);
		}

		if (pktableName.equals ("IK1")) {
		    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())    {
		        success = success && (updateRule == DatabaseMetaData.importedKeyRestrict);
		        success = success && (deleteRule == DatabaseMetaData.importedKeyRestrict);
		        success = success && (pkName.equals("IKKEY1")); 
		    } else  {
		        if (!(updateRule == DatabaseMetaData.importedKeyNoAction)) {
		            success = false;
		            message.append("\nupdateRule="+updateRule+" sb "+DatabaseMetaData.importedKeyNoAction);
		        }
		        if (!(deleteRule == DatabaseMetaData.importedKeyNoAction)) {
		            success=false;
		            message.append("\ndeleteRule="+deleteRule+" sb "+ DatabaseMetaData.importedKeyNoAction);
		        }
		        if ( !(pkName.equals("IKKEY1"))) {
		            success=false;
		            message.append("\npkName="+pkName+" sb IKKEY1");
		        }
		    }
		}
	    } /* while rs.next(); */

             // output_.println ("Rows = " + rows);

            rs.close ();
            assertCondition ((rows == 1) && success, "\nrows="+rows+" sb 1"+ message);

	} catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }


/**
getImportedKeys() - Specify all null parameters.  Verify no rows are returned.
**/
    public void Var003()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (null, null, null);

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
getImportedKeys() - Specify null for the catalog.
**/
    public void Var004()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (null, JDDMDTest.COLLECTION, "IK2");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableName = rs.getString ("PKTABLE_NAME");
                if (pktableName.equals ("IK1"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getImportedKeys() - Specify empty string for the catalog.
**/
    public void Var005()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys ("", JDDMDTest.COLLECTION, "IK2");

            int rows = 0;
            while (rs.next ())
                ++rows;

            rs.close ();
	    if (isSysibmMetadata()) {
		assertCondition (rows > 0, "rows="+rows+" empty string specified for catalog, entries should be returned since sysibm metadata ");
	    } else {
		assertCondition (rows == 0, "rows="+rows+" empty string specified for catalog, no entries should be returned");
	    }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getImportedKeys() - Specify a catalog that matches the catalog
exactly.
**/
    public void Var006()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
                                                 JDDMDTest.COLLECTION, "IK5");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableName = rs.getString ("PKTABLE_NAME");
                if (pktableName.equals ("IK1"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "IK1 found = "+check1+" row count = "+rows+" sb 1 for catalog = "+catalog_);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getImportedKeys() - Specify "localhost" for catalog.
**/
    public void Var007()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC || getJdbcLevel() >= 4 ||
          (getDriver() == JDTestDriver.DRIVER_NATIVE && true) ||
          (getDriver() == JDTestDriver.DRIVER_TOOLBOX && isSysibmMetadata())) {
        notApplicable("\"localhost\" variation ");
      } else {

        try {
            ResultSet rs = dmd_.getImportedKeys ("localhost",
                                                 JDDMDTest.COLLECTION, "IK5");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableName = rs.getString ("PKTABLE_NAME");
                if (pktableName.equals ("IK1"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1),
                  "\nPKTABLE_NAME found="+check1+" rows="+rows+" sb 1");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
      }
    }



/**
getImportedKeys() - Specify a catalog pattern for which there is a
match.  No matching rows should be returned, since we do not
support catalog pattern.
**/
    public void Var008()
    {
	if (JDTestDriver.isLUW()) {
	    notApplicable("LUW uses null catalog names for pattern matches does not make sense");
	} else {
	    try {
		ResultSet rs = dmd_.getImportedKeys (catalog_.substring (0, 4) + "%",
						     JDDMDTest.COLLECTION, "IK2");

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
    }



/**
getImportedKeys() - Specify a catalog for which there is no match.
No matching columns should be returned.
**/
    public void Var009()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys ("BOGUS",
                JDDMDTest.COLLECTION, "IK6");

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
getImportedKeys() - Specify null for the schema.  Should find match..
**/
    public void Var010()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
                                                 null, "IK9");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableName = rs.getString ("PKTABLE_NAME");
                if (pktableName.equals ("IK8"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows >= 1),
                "\nIK8 found="+check1+" rows="+rows+" sb >= 1 when schema=null");

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getImportedKeys() - Specify empty string for the schema.
**/
    public void Var011()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
                "", "IK9");

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
getImportedKeys() - Specify a schema that matches the schema
exactly.
**/
    public void Var012()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
                                                 JDDMDTest.COLLECTION, "IK9");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableName = rs.getString ("PKTABLE_NAME");
                if (pktableName.equals ("IK8"))
                    check1 = true;
            }

            rs.close ();
            assertCondition (check1 && (rows == 1), "IK8 found="+check1+" rows="+rows+" sb 1 when schema matches");
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getImportedKeys() - Specify a schema pattern for which there is a
match.  An exception should be thrown, since we do not
support schema pattern.

SQL400 - for functions that call the SQLForeignKeys() CLI function,
         the native driver can handle the search pattern.  We check
         to make sure that the row gets returned.
**/
    public void Var013()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
                JDDMDTest.SCHEMAS_PERCENT, "IK9");

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
                    (getDriver() == JDTestDriver.DRIVER_NATIVE && true) ||
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
getImportedKeys() - Specify a schema for which there is no match.
No rows should be returned.
**/
    public void Var014()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
                "BOGUS", "IK9");

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
getImportedKeys() - Specify null for the table.
No rows should be returned.
**/
    public void Var015()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
                JDDMDTest.COLLECTION2, null);

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
getImportedKeys() - Specify empty string for the table.
**/
    public void Var016()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
                JDDMDTest.COLLECTION2, "");

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
getImportedKeys() - Specify a table that matches the table
exactly.  All matching columns should be returned.
**/
    public void Var017()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
                JDDMDTest.COLLECTION2, "IK4");

            boolean check4 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableName = rs.getString ("PKTABLE_NAME");
                if (pktableName.equals ("IK3"))
                    check4 = true;
            }

            rs.close ();
            assertCondition (check4 && (rows == 1), "IK3 found = "+check4+" rows = "+rows+" sb 1 for ("+catalog_+","+
			     JDDMDTest.COLLECTION2+",IK4)" );
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getImportedKeys() - Specify a table pattern for which there is a
match.  An exception should be thrown, since we do not
support table pattern.

SQL400 - for functions that call the SQLForeignKeys() CLI function,
         the native driver can handle the search pattern.  We check
         to make sure that the row gets returned.
**/
    public void Var018()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
                JDDMDTest.COLLECTION, "IK%");

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
                    (getDriver() == JDTestDriver.DRIVER_NATIVE && true) ||
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
getImportedKeys() - Specify a table for which there is no match.
No rows should be returned.
**/
    public void Var019()
    {
        try {
            ResultSet rs = dmd_.getImportedKeys (catalog_,
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
getImportedKeys() - Specify a table that matches the table
exactly.  All matching columns should be returned.
**/
    public void Var020()
    {
        if(true && !JDTestDriver.isLUW())
        {
            try {
                ResultSet rs = dmd_.getImportedKeys (catalog_,
                    JDDMDTest.COLLECTION2, "LCNIK4");

                boolean check4 = false;
                int rows = 0;
                while (rs.next ()) {
                    ++rows;
                    String pktableName = rs.getString ("PKTABLE_NAME");
                    if (pktableName.equals ("LCNIK3"))
                        check4 = true;
                    String pkcolumnName = rs.getString("PKCOLUMN_NAME");
                    check4 = check4 && pkcolumnName.equals("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST");
                    String fktableName = rs.getString("FKTABLE_NAME");
                    check4 = check4 && fktableName.equals("LCNIK4");
                    String fkcolumnName = rs.getString("FKCOLUMN_NAME");
                    check4 = check4 && fkcolumnName.equals("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST");
                }

                rs.close ();
                assertCondition (check4 && (rows == 1), "Added by Toolbox 8/11/2004 for 128 byte column names.");
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception.  Added by Toolbox 8/11/2004 for 128 byte column names.");
            }
        }
        else
            notApplicable("V5R4 and greater variation.");
    }


/**
getImportedKeys() - Run getImportedKeys multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var021()
    {
	String added = " -- added by 1/31/2011 to test for native statement leak in metadata";
        if (checkNative()) {
            try {

		Connection connection = testDriver_.getConnection (baseURL_
            + ";prefetch=false;libraries=" + JDDMDTest.COLLECTION + " "
            + JDDMDTest.COLLECTION2 + " " + JDDMDTest.COLLECTIONXX,
            userId_, encryptedPassword_);


		Statement fill[] = new Statement[20];
		for (int i = 0; i < fill.length; i++) {
		    fill[i] = connection.createStatement();
		}

		Statement stmt = connection.createStatement();
		DatabaseMetaData dmd = connection.getMetaData ();

		for (int i = 0; i < 1000; i++) {
		    // output_.println("Calling getImportedKeys");
		    ResultSet rs = dmd.getImportedKeys (catalog_,
							 JDDMDTest.COLLECTION2, "LCNIK4");
		    rs.close();
		}

		Statement stmt2 = connection.createStatement();
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

	/* PRIMED from 546CN */ 
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





	String[][] fixup545T={
	    {"isNullable","1","1"},
	    {"isNullable","2","1"},
	    {"isNullable","5","1"},
	    {"isNullable","6","1"},
	    {"isNullable","12","1"},
	    {"isNullable","13","1"},
	}; 

	String[][] fixup545NX = {
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

	String [][] fixup616NS={
	    {"isNullable","13","1"},
	};

	String [][] fixupExtended716T = {
	    {"getColumnLabel","2","LIBRARY NAME"},
	    {"getColumnLabel","3","LONG FILE NAME"},
	    {"isNullable","13","1"},
	};


	String [][] fixupExtended737T = {
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

	};

	String [][] fixupExtended715T = {
	    {"isNullable","1","0"},
	    {"isNullable","2","0"},
	    {"getColumnLabel","2","LIBRARY NAME"},
	    {"getColumnLabel","3","LONG FILE NAME"},
	    {"isNullable","5","0"},
	    {"isNullable","6","0"},
	    {"isNullable","12","0"},
	    {"isNullable","13","1"},
	};

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

/* New labels April 2019 */
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


	    {"716L", fixup616NS},
	    {"726L", fixup616NS},

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
	    {"716T", fixupExtended716T, "09/06/2012 -- Primed from 7166U"},
	    {"717T", fixupExtended716T, "09/06/2012 -- Guess from 717T"},
	    {"718T", fixupExtended716T, "09/06/2012 -- Guess from 717T"},
	    {"719T", fixupExtended716T, "09/06/2012 -- Guess from 717T"},


	    {"726T", fixupExtended716T, "10/02/2013 -- Guess from 717T"},
	    {"727T", fixupExtended716T, "10/02/2013 -- Guess from 717T"},
	    {"728T", fixupExtended716T, "10/02/2013 -- Guess from 717T"},
	    {"729T", fixupExtended716T, "10/02/2013 -- Guess from 717T"},


	    {"737T", fixupExtended737T, "5/20/2019 -- update labels"}, 
	    {"738T", fixupExtended737T, "5/20/2019 -- update labels"}, 
	    {"739T", fixupExtended737T, "5/20/2019 -- update labels"}, 

	    {"544N", fixupExtended545N},
	    {"545N", fixupExtended545N},
	    {"614N", fixupExtended616N},
	    {"615N", fixupExtended616N},
	    {"616N", fixupExtended616N},
/* 
	    {"714N", fixupExtended616N},
	    {"715N", fixupExtended616N},
	    {"716N", fixupExtended616N},
*/ 
	    {"717N", fixupExtended616N},
	    {"718N", fixupExtended616N},
	    {"719N", fixupExtended616N},


	    {"737N", fixupExtended736N},
	    {"738N", fixupExtended736N},
	    {"739N", fixupExtended736N},

/*
should search for previous release 
	    {"725N", fixupExtended616N},
	    {"726N", fixupExtended616N},
	    {"727N", fixupExtended616N},
*/ 

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

	    {"714TS", fixup616NS},
	    {"715TS", fixup616NS},
	    {"716TS", fixup616NS},
	    {"717TS", fixup616NS},
	    {"718TS", fixup616NS},
	    {"719TS", fixup616NS},

/* 
	    {"724TX", fixup545T},
	    {"725TX", fixup545T},
	    {"726TX", fixup545T},
	    {"727TX", fixup545T},
*/

	    {"544NX", fixup545NX},
	    {"545NX", fixup545NX}, 

	    {"614NS", fixup616NS},
	    {"615NS", fixup616NS},
	    {"616NS", fixup616NS}, 
	    {"714NS", fixup616NS},
	    {"715NS", fixup616NS},
	    {"716NS", fixup616NS},
	    {"717NS", fixup616NS},
	    {"718NS", fixup616NS},
	    {"719NS", fixup616NS},
	    {"7215NS", fixup616NS},
	    {"726NS", fixup616NS},
	    {"727NS", fixup616NS}, 

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
		    rsA[j] = dmd.getImportedKeys(catalog_,
							 JDDMDTest.COLLECTION2, "LCNIK4");

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


/* Test a readonly connection */ 
  public void Var024() {

       try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 


            ResultSet rs = dmd.getImportedKeys (null, JDDMDTest.COLLECTION, "IK2");

            boolean check1 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String pktableName = rs.getString ("PKTABLE_NAME");
                if (pktableName.equals ("IK1"))
                    check1 = true;
            }

            rs.close ();
	    c.close(); 
            assertCondition (check1 && (rows == 1));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
 
  }

    

}
