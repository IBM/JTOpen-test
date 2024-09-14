///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTableName.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDTableName.java
 //
 // Classes:      JDTableName
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;


/**
Testcase JDTableName.  This tests the ability of
the driver to handle multiword and single word
table and column names.  This test case should
only work on v5r2 or later systems.
**/
public class JDTableName extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDTableName";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }

    private Connection connection;
    private ResultSet  rs;
    private Statement  stmnt;

/**
Constructor.
**/
    public JDTableName(AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password) {

        super(systemObject, "JDTableName",
              namesAndVars, runMode, fileOutputStream,
              password);
    }



/**
Performs setup needed before running variations.
@exception Exception If an exception occurs.
**/
    protected void setup() throws Exception {

        connection = testDriver_.getConnection(baseURL_ + ";libraries=" + JDStatementTest.COLLECTION,
                                                userId_, encryptedPassword_);

	if (getJdbcLevel() < 2 || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
	    stmnt = connection.createStatement(); 
	} else { 
        stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	} 
	try {
	    stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".\"My Table\""); 
	} catch (Exception e) {
	} 

        // create the "My Table" table with columns named "First Name" and "Last Name"
        stmnt.executeUpdate("CREATE TABLE " + JDStatementTest.COLLECTION + ".\"My Table\"" +
                            " (\"First Name\" CHAR(20), \"Last Name\" CHAR(20))");
        // load the "My Table" table with 10 records with "First Name" = 'First' and "Last Name" = 'Last'
        for (int i=0; i<10; i++) {
            stmnt.executeUpdate("INSERT INTO " + JDStatementTest.COLLECTION + ".\"My Table\"" +
                                " (\"First Name\", \"Last Name\") VALUES ('First', 'Last')");
        }

	try {
	    stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".\"Your Table\""); 
	} catch (Exception e) {
	} 

        // create the "Your Table" table with columns named "FIRST" and "LAST"
        stmnt.executeUpdate("CREATE TABLE " + JDStatementTest.COLLECTION + ".\"Your Table\"" +
                            " (\"FIRST\" CHAR(20), \"LAST\" CHAR(20))");
        // load the "Your Table" table with 10 records with "FIRST" = 'First Name' and "LAST" = 'Last Name'
        for (int i=0; i<10; i++) {
            stmnt.executeUpdate("INSERT INTO " + JDStatementTest.COLLECTION + ".\"Your Table\"" +
                                " (\"FIRST\", \"LAST\") VALUES ('First Name', 'Last Name')");
        }

	try {
	    stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".JDTNTABLE"); 
	} catch (Exception e) {
	} 
        // create the "JDTNTABLE" table with columns named "First Name" and "Last Name"
        stmnt.executeUpdate("CREATE TABLE " + JDStatementTest.COLLECTION + ".JDTNTABLE" +
                            " (\"First Name\" CHAR(20), \"Last Name\" CHAR(20))");
        // load the "JDTNTABLE" table with 10 records with "First Name" = 'First' and "Last Name" = 'Last'
        for (int i=0; i<10; i++) {
            stmnt.executeUpdate("INSERT INTO " + JDStatementTest.COLLECTION + ".\"JDTNTABLE\"" +
                                " (\"First Name\", \"Last Name\") VALUES ('First', 'Last')");
        }

	try {
	    stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".YOURTABLE"); 
	} catch (Exception e) {
	} 

        // create the "YOURTABLE" table with columns named "FIRST" and "LAST"
        stmnt.executeUpdate("CREATE TABLE " + JDStatementTest.COLLECTION + ".YOURTABLE" +
                            " (\"FIRST\" CHAR(20), \"LAST\" CHAR(20))");
        // load the "YOURTABLE" table with 10 records with "FIRST" = 'First Name' and "LAST" = 'Last Name'
        for (int i=0; i<10; i++) {
            stmnt.executeUpdate("INSERT INTO " + JDStatementTest.COLLECTION + ".YOURTABLE" +
                                " (\"FIRST\", \"LAST\") VALUES ('First Name', 'Last Name')");
        }

	try {
	    stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".\"My     Table\""); 
	} catch (Exception e) {
	} 

        // create the "My     Table" table with columns named "First Name" and "LAST"
        stmnt.executeUpdate("CREATE TABLE " + JDStatementTest.COLLECTION + ".\"My     Table\"" +
                            " (\"First Name\" CHAR(20), \"LAST\" CHAR(20))");
        // load the "My     Table" table with 10 records with "First Name" = 'First' and "LAST" = 'Last Name'
        for (int i=0; i<10; i++) {
            stmnt.executeUpdate("INSERT INTO " + JDStatementTest.COLLECTION + ".\"My     Table\"" +
                                " (\"First Name\", \"LAST\") VALUES ('First', 'Last Name')");
        }


	try { 
	    stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".\"My multi word table\"" );
	} catch (Exception e) {
	} 

        // create the "My multi word table" table with columns named "First Name" and "LAST"
        stmnt.executeUpdate("CREATE TABLE " + JDStatementTest.COLLECTION + ".\"My multi word table\"" +
                            " (\"First Name\" CHAR(20), \"LAST\" CHAR(20))");
        // load the "My multi word table" table with 10 records with "First Name" = 'First' and "LAST" = 'Last Name'
        for (int i=0; i<10; i++) {
            stmnt.executeUpdate("INSERT INTO " + JDStatementTest.COLLECTION + ".\"My multi word table\"" +
                                " (\"First Name\", \"LAST\") VALUES ('First', 'Last Name')");
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup() throws Exception {

        stmnt = connection.createStatement();
        stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".\"My Table\"");
        stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".\"Your Table\"");
        stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".JDTNTABLE");
        stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".YOURTABLE");
        stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".\"My     Table\"");
        stmnt.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".\"My multi word table\"");
        stmnt.close();

        connection.close ();
    }


/**
update records in a table with a multiword name and multiword columns
use the table name and collection name
**/
    public void Var001() {
	if (checkJdbc20()) { 
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", \"Last Name\" FROM " +
                                    JDStatementTest.COLLECTION + ".\"My Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"Last Name\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a multiword name and multiword columns
use the table name only (no collection name)
**/
    public void Var002() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);


            rs = stmnt.executeQuery("SELECT \"First Name\", \"Last Name\" FROM \"My Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"Last Name\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a multiword name and single word columns in quotes
use the table name and collection name
**/
    public void Var003() {
	if (checkJdbc20()) {

        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"FIRST\", \"LAST\" FROM " +
                                    JDStatementTest.COLLECTION + ".\"Your Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"FIRST\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }

    }
/**
update records in a table with a multiword name and single word columns in quotes
use the table name only (no collection name)
**/
    public void Var004() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"FIRST\", \"LAST\" FROM \"Your Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"FIRST\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a multiword name and single word columns w/o quotes
use the table name and collection name
**/
    public void Var005() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT FIRST, LAST FROM " + JDStatementTest.COLLECTION + ".\"Your Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("FIRST", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a multiword name and single word columns w/o quotes
use the table name only (no collection name)
**/
    public void Var006() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT FIRST, LAST FROM \"Your Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("FIRST", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a single word name and multiword columns
use the table name and collection name
**/
    public void Var007() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", \"Last Name\" FROM " +
                                    JDStatementTest.COLLECTION + ".\"JDTNTABLE\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"Last Name\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a single word name and multiword columns
use the table name only (no collection name)
**/
    public void Var008() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", \"Last Name\" FROM \"JDTNTABLE\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"Last Name\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a single word name and single word columns in quotes
use the table name and collection name
**/
    public void Var009() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"FIRST\", \"LAST\" FROM " +
                                    JDStatementTest.COLLECTION + ".\"YOURTABLE\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"FIRST\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a single name and single word columns in quotes
use the table name only (no collection name)
**/
    public void Var010() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"FIRST\", \"LAST\" FROM \"YOURTABLE\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"FIRST\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a single word name and single word columns w/o quotes
use the table name and collection name
**/
    public void Var011() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT FIRST, LAST FROM " +
                                    JDStatementTest.COLLECTION + ".\"YOURTABLE\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("FIRST", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a single word name and single word columns w/o quotes
use the table name only (no collection name)
**/
    public void Var012() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT FIRST, LAST FROM \"YOURTABLE\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("FIRST", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a single word name (w/o quotes) and multiword columns
use the table name and collection name
**/
    public void Var013() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", \"Last Name\" FROM " +
                                    JDStatementTest.COLLECTION + ".JDTNTABLE FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"Last Name\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a single word name (w/o quotes) and multiword columns
use the table name only (no collection name)
**/
    public void Var014() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", \"Last Name\" FROM JDTNTABLE FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"Last Name\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a single word name (w/o quotes) and single word columns in quotes
use the table name and collection name
**/
    public void Var015() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"FIRST\", \"LAST\" FROM " +
                                    JDStatementTest.COLLECTION + ".YOURTABLE FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"FIRST\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a single word name (w/o quotes) and single word columns in quotes
use the table name only (no collection name)
**/
    public void Var016() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"FIRST\", \"LAST\" FROM YOURTABLE FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"FIRST\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a single word name (w/o quotes) and single word columns w/o quotes
use the table name and collection name
**/
    public void Var017() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT FIRST, LAST FROM " + JDStatementTest.COLLECTION + ".YOURTABLE FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("FIRST", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a single word name (w/o quotes) and single word columns w/o quotes
use the table name only (no collection name)
**/
    public void Var018() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT FIRST, LAST FROM YOURTABLE FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("FIRST", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a multiword name and multiword columns
use the table name and collection name in quotes
**/
    public void Var019() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", \"Last Name\" FROM \"" +
                                    JDStatementTest.COLLECTION + "\".\"My Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"Last Name\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a multiword name and single word columns in quotes
use the table name and collection name in quotes
**/
    public void Var020() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"FIRST\", \"LAST\" FROM \"" +
                                    JDStatementTest.COLLECTION + "\".\"Your Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"FIRST\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a multiword name and single word columns w/o quotes
use the table name and collection name in quotes
**/
    public void Var021() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT FIRST, LAST FROM \"" +
                                    JDStatementTest.COLLECTION + "\".\"Your Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("FIRST", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a single word name and multiword columns
use the table name and collection name in quotes
**/
    public void Var022() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", \"Last Name\" FROM \"" +
                                    JDStatementTest.COLLECTION + "\".\"JDTNTABLE\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"Last Name\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a single word name and single word columns in quotes
use the table name and collection name in quotes
**/
    public void Var023() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"FIRST\", \"LAST\" FROM \"" +
                                    JDStatementTest.COLLECTION + "\".\"YOURTABLE\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"FIRST\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a single word name and single word columns w/o quotes
use the table name and collection name in quotes
**/
    public void Var024() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT FIRST, LAST FROM \"" +
                                    JDStatementTest.COLLECTION + "\".\"YOURTABLE\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("FIRST", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a single word name (w/o quotes) and multiword columns
use the table name and collection name in quotes
**/
    public void Var025() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", \"Last Name\" FROM \"" +
                                    JDStatementTest.COLLECTION + "\".JDTNTABLE FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"Last Name\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a single word name (w/o quotes) and single word columns in quotes
use the table name and collection name in quotes
**/
    public void Var026() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"FIRST\", \"LAST\" FROM \"" +
                                    JDStatementTest.COLLECTION + "\".YOURTABLE FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"FIRST\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a single word name (w/o quotes) and single word columns w/o quotes
use the table name and collection name in quotes
**/
    public void Var027() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT FIRST, LAST FROM \"" +
                                    JDStatementTest.COLLECTION + "\".YOURTABLE FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("FIRST", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a multiword name and quoted column names
use the table name and collection name
**/
    public void Var028() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", \"LAST\" FROM " +
                                    JDStatementTest.COLLECTION + ".\"My     Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
update records in a table with a multiword name and quoted column names
use the table name only (no collection name)
**/
    public void Var029() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);


            rs = stmnt.executeQuery("SELECT \"First Name\", \"LAST\" FROM \"My     Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a multiword name and non quoted column name
use the table name and collection name
**/
    public void Var030() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", LAST FROM " +
                                    JDStatementTest.COLLECTION + ".\"My     Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
update records in a table with a multiword name and single word columns in quotes
use the table name only (no collection name)
**/
    public void Var031() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", LAST FROM \"My     Table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a multiword name and quoted column names
use the table name and collection name
**/
    public void Var032() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", \"LAST\" FROM " +
                                    JDStatementTest.COLLECTION + ".\"My multi word table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
update records in a table with a multiword name and quoted column names
use the table name only (no collection name)
**/
    public void Var033() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);


            rs = stmnt.executeQuery("SELECT \"First Name\", \"LAST\" FROM \"My multi word table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("\"LAST\"", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
update records in a table with a multiword name and non quoted column name
use the table name and collection name
**/
    public void Var034() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", LAST FROM " +
                                    JDStatementTest.COLLECTION + ".\"My multi word table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
        }
    }

    }
/**
update records in a table with a multiword name and single word columns in quotes
use the table name only (no collection name)
**/
    public void Var035() {
	if (checkJdbc20()) {
        try {
            stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = stmnt.executeQuery("SELECT \"First Name\", LAST FROM \"My multi word table\" FOR UPDATE");

            int cnt = 0;
            while(rs.next()) {
                rs.updateString("\"First Name\"", "fornavn");
                rs.updateString("LAST", "etternavn");
                rs.updateRow();
                cnt++;
            }

            stmnt.close();
            if (cnt == 0) {
                failed("Did not find records to update");
            } else {
                succeeded();
            }
        }
        catch (SQLException e) {
            try { stmnt.close(); } catch (Exception ce) {}
            failed (e, "Unexpected Exception");
	    }
        }
    }
}



