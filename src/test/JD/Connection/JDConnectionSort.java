///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionSort.java
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
 // File Name:    JDConnectionSort.java
 //
 // Classes:      JDConnectionSort
 //
 ////////////////////////////////////////////////////////////////////////
 ////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Hashtable;
import java.util.Properties;


/**
Testcase JDConnectionSort.  This tests the following methods
of the JDBC Connection class:

<ul>
<li>"sort" property
<li>"sort language" property
<li>"sort table" property
<li>"sort weight" property
</ul>
**/
public class JDConnectionSort
extends JDTestcase
{



/**
Constructor.
**/
    public JDConnectionSort (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDConnectionSort",
            namesAndVars, runMode, fileOutputStream,
            password);
    }



/**
Verifies that the server is sorting.

@exception Exception    If an exception occurs.
**/
    private boolean verifySort (Connection connection)
        throws Exception
    {
        Statement s = connection.createStatement ();       
        ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT ORDER BY LSTNAM");
        int count = 0;
        boolean check = true;
        String previous = null;
        while (rs.next ()) {
            ++count;
            String current = rs.getString ("LSTNAM");
            if (previous != null) {
                if (previous.compareTo (current) > 0)
                    check = false;
            }
            previous = current;
        }
        rs.close ();
        s.close ();
        return (check && (count > 5));
    }


/**
Verifies that the server is sorting.

@exception Exception    If an exception occurs.
**/
    private boolean verifySort (Connection connection, String table, String[] expected )
        throws Exception
    {
	// int i; 
        Statement s = connection.createStatement ();       
        ResultSet rs = s.executeQuery ("SELECT * FROM "+table+" ORDER BY STUFF");
        int count = 0;
        boolean check = true;

        while (rs.next ()) {
            String current = rs.getString (1);
	    if (count < expected.length) {
		if (expected[count].equals(current)) {
		    if (check == false)
			System.out.println("Expected "+expected[count]+ " and found "+current);
		} else {
		    System.out.println("Expected "+expected[count]+ " but found "+current);
		    check = false; 
		} 
	    } else {
		System.out.println("ERROR.. string "+current+" not expected"); 
		check = false;
	    }
            ++count;

        }
        rs.close ();
        s.close ();
        return (check);
    }


/**
 populate a table with data to be sorted.  The data is inserted in reverse order.
**/ 
    public void populateTable(Connection connection, String tableName, int ccsid, String[] sortStrings) throws Exception {
	Statement stmt = connection.createStatement();
	try {
	    stmt.executeUpdate("DROP TABLE "+tableName); 
	} catch (Exception e) {
	}
	if (ccsid == 13488 || ccsid == 1200) {
	    stmt.executeUpdate("CREATE TABLE "+tableName+" (STUFF VARGRAPHIC(4096) CCSID "+ccsid+")"); 
	} else { 
	    stmt.executeUpdate("CREATE TABLE "+tableName+" (STUFF VARCHAR(4096)    CCSID "+ccsid+")"); 
	}
	PreparedStatement pStmt = connection.prepareStatement("INSERT INTO "+tableName+" VALUES(?)"); 
	for (int i = sortStrings.length-1; i >= 0; i--) {
	    pStmt.setString(1, sortStrings[i]);
	    pStmt.executeUpdate(); 
	} 
	pStmt.close();
	stmt.close(); 

    } 

/**
 cleanup the table
 */ 
    public void cleanupTable(Connection connection, String tableName) throws Exception  {
	Statement stmt = connection.createStatement();
	stmt.executeUpdate("DROP TABLE "+tableName); 
	stmt.close(); 
    } 




/**
"sort" property - Specify an invalid value.
**/
    public void Var001()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.1"));
            properties.put ("sort", "invalid");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @A1A
            // @A1D failed(e, "Unexpected Exception");
        }
    }



/**
"sort" property - Specify "hex".
**/
    public void Var002()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.2"));
            properties.put ("sort", "hex");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
"sort" property - Specify "job".
**/
    public void Var003()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.3"));
            properties.put ("sort", "job");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
"sort" property - Specify "language" with no language specified.
**/
    public void Var004()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.4"));
            properties.put ("sort", "language");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @A1A
            // @A1D failed(e, "Unexpected Exception");
        }
    }



/**
"sort" property - Specify "language" with an invalid language specified.
**/
    public void Var005()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.5"));
            properties.put ("sort", "language");
            properties.put ("sort language", "bad");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @A1A
            // @A1D failed(e, "Unexpected Exception");
        }
    }



/**
"sort" property - Specify "language" with a valid language specified.
**/
    public void Var006()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.6"));
            properties.put ("sort", "language");
            properties.put ("sort language", "enu");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
"sort" property - Specify "language" with an invalid weight.
**/
    public void Var007()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.7"));
            properties.put ("sort", "language");
            properties.put ("sort language", "enu");
            properties.put ("sort weight", "invalid");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
"sort" property - Specify "language" with shared weight.
**/
    public void Var008()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.8"));
            properties.put ("sort", "language");
            properties.put ("sort language", "enu");
            properties.put ("sort weight", "shared");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
"sort" property - Specify "language" with unique weight.
**/
    public void Var009()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.9"));
            properties.put ("sort", "language");
            properties.put ("sort language", "enu");
            properties.put ("sort weight", "unique");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
"sort" property - Specify "table" with no table specified.
**/
    public void Var010()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.10"));
            properties.put ("sort", "table");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @A1A
            // @A1D failed(e, "Unexpected Exception");
        }
    }



/**
"sort" property - Specify "table" with an invalid table specified.
**/
    public void Var011()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.11"));
            properties.put ("sort", "table");
            properties.put ("sort table", "qsys/bogus");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @A1A
            // @A1D failed(e, "Unexpected Exception");
        }
    }


/**
"sort" property - Specify "table" with a valid uppercase slash-separated table specified.
**/
    public void Var012()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.12"));
            properties.put ("sort", "table");
            properties.put ("sort table", "QSYS/QTOCSSTBL");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }


/**
"sort" property - Specify "table" with a valid uppercase dot-separated table specified.
**/
    public void Var013()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.13"));
            properties.put ("sort", "table");
            properties.put ("sort table", "QSYS.QTOCSSTBL");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }


/**
"sort" property - Specify "table" with a valid lowercase table specified.
**/
    public void Var014()
    {
        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.14"));
            properties.put ("sort", "table");
            properties.put ("sort table", "qsys/qtocsstbl");
            Connection connection = testDriver_.getConnection (baseURL_, properties);
            boolean check = verifySort (connection);
            connection.close ();
            assertCondition (check);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }

/**
"sort" property - Test a true hex sort.
Note:  The native JDBC driver default is hex, so this should work in previous releases
**/
    public void Var015()
    {
	int ccsid=37;
	String tableName = JDConnectionTest.COLLECTION+".JDCONNSORT15"; 
	String[] sortStrings={
	    "A",       /* 0xC1 in CCSID 37 */ 
	    "\u00f4",  /* 0xCB in CCSID 37 */
	    "J",       /* 0xD1 in CCSID 37 */
	    "\u00fb",  /* 0xDB in CCSID 37 */ 
	    "S",       /* 0xE2 in CCSID 37 */
            "\u00d4",  /* 0xEB in CCSID 37 */ 
	    "9"
	};


        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.15"));
            properties.put ("sort", "hex");

            Connection connection = testDriver_.getConnection (baseURL_, properties);
	    populateTable(connection, tableName, ccsid, sortStrings);
            boolean check = verifySort (connection, tableName, sortStrings );
	    cleanupTable(connection, tableName); 
            connection.close ();
            assertCondition (check, "New Test added by native JDBC driver 04/21/2004");
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }

/**
"sort" property - Test a job sort. This also appears like a hex sort.
**/
    public void Var016()
    {
	int ccsid=37;
	String tableName = JDConnectionTest.COLLECTION+".JDCONNSORT16"; 
	String[] sortStrings={
	    "a",
	    "j",
	    "s",
	    "z",
	    "A",       /* 0xC1 in CCSID 37 */ 
	    "\u00f4",  /* 0xCB in CCSID 37 */
	    "J",       /* 0xD1 in CCSID 37 */
	    "\u00fb",  /* 0xDB in CCSID 37 */ 
	    "S",       /* 0xE2 in CCSID 37 */
            "\u00d4",  /* 0xEB in CCSID 37 */ 
	    "9"
	};


        try {
            Properties properties = new Properties ();
            properties.put ("user", userId_);
            properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.16"));
            properties.put ("sort", "job");

            Connection connection = testDriver_.getConnection (baseURL_, properties);

	    populateTable(connection, tableName, ccsid, sortStrings);
            boolean check = verifySort (connection, tableName, sortStrings );
	    cleanupTable(connection, tableName); 
            connection.close ();
            assertCondition (check, "New Test added by native JDBC driver 04/21/2004");
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }


/**
"sort" property - Specify "language" with shared weight. 
**/
    public void Var017()
    {
	int ccsid=37;
	String tableName = JDConnectionTest.COLLECTION+".JDCONNSORT16"; 
	String[] sortStrings={
	    "9",
	    "ab",
	    "Ab",       /* 0xC1 in CCSID 37 */ 
	    "ac",
	    "Ac",       /* 0xC1 in CCSID 37 */ 
	    "j",
	    "J",       /* 0xD1 in CCSID 37 */
	    "\u00f4",  /* 0xCB in CCSID 37 */
	    "\u00d4",  /* 0xEB in CCSID 37 */ 
	    "s",
	    "S",       /* 0xE2 in CCSID 37 */
	    "\u00fb",  /* 0xDB in CCSID 37 */ 
	    "z"
	};

	String[] v5r4SortStrings={                            //@E1A
	    "9",
	    "Ab",
	    "ab",
	    "Ac",
	    "ac",
	    "J",
	    "j",
	    "\u00d4",
	    "\u00f4",
	    "S",
	    "s",
	    "\u00fb",
	    "z"
	};

	try {
	    Properties properties = new Properties ();
	    properties.put ("user", userId_);
	    properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.17"));
	    properties.put ("sort", "language");
	    properties.put ("sort language", "enu");
	    properties.put ("sort weight", "shared");

	    Connection connection = testDriver_.getConnection (baseURL_, properties);

	    populateTable(connection, tableName, ccsid, sortStrings);
	    boolean check = false;
	    if(getRelease() <= JDTestDriver.RELEASE_V7R1M0)                 //@E1C things are ordered by order by sort
		check = verifySort (connection, tableName, sortStrings );
	    else                                                            //@E1A Things are implemented by order by index in V54
		check = verifySort (connection, tableName, v5r4SortStrings); //@E1A
	    cleanupTable(connection, tableName); 
	    connection.close ();
	    assertCondition (check, "New Test added by native JDBC driver 04/21/2004");

	}
	catch(Exception e) {
	    failed(e, "Unexpected Exception");
	}
    }



/**
"sort" property - Specify "language" with unique weight. 
**/
    public void Var018()
    {
	    int ccsid=37;
	    String tableName = JDConnectionTest.COLLECTION+".JDCONNSORT16"; 
	    String[] sortStrings={
		"9",
		"ab",
		"ac",
		"Ab",       /* 0xC1 in CCSID 37 */
		"Ac",       /* 0xC1 in CCSID 37 */ 
		"j",
		"J",       /* 0xD1 in CCSID 37 */
		"\u00f4",  /* 0xCB in CCSID 37 */
		"\u00d4",  /* 0xEB in CCSID 37 */ 
		"s",
		"S",       /* 0xE2 in CCSID 37 */
		"\u00fb",  /* 0xDB in CCSID 37 */ 
		"z"
	    };

	    try {
		Properties properties = new Properties ();
		properties.put ("user", userId_);
		properties.put ("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionSort.18"));
		properties.put ("sort", "language");
		properties.put ("sort language", "enu");
		properties.put ("sort weight", "unique");

		Connection connection = testDriver_.getConnection (baseURL_, properties);

		populateTable(connection, tableName, ccsid, sortStrings);
		boolean check = verifySort (connection, tableName, sortStrings );
		cleanupTable(connection, tableName); 
		connection.close ();
		assertCondition (check, "New Test added by native JDBC driver 04/21/2004");

	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }

    }



}



