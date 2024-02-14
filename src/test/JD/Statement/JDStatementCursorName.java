///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementCursorName.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDStatementCursorName.java
 //
 // Classes:      JDStatementCursorName
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDStatementCursorName.  This tests the following method
of the JDBC Statement class:

<ul>
<li>setCursorName()</li>
<li>ResultSet.getCursorName()</li>
</ul>
**/
public class JDStatementCursorName
extends JDTestcase
{



    // Private data.
    private static String table_  = JDStatementTest.COLLECTION + ".JDSCN";

    private              Connection     connection_;




/**
Constructor.
**/
    public JDStatementCursorName (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDStatementCursorName",
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
        connection_ = testDriver_.getConnection (baseURL_ + ";errors=full", userId_, encryptedPassword_);
        table_  = JDStatementTest.COLLECTION + ".JDSCN";
        Statement s = connection_.createStatement ();
        initTable(s,  table_ , " (NAME VARCHAR(10), ID INT)");
        s.executeUpdate ("INSERT INTO " + table_
            + " (NAME, ID) VALUES ('alpha', 1)");
        s.executeUpdate ("INSERT INTO " + table_
            + " (NAME, ID) VALUES ('beta', 2)");
        s.executeUpdate ("INSERT INTO " + table_
            + " (NAME, ID) VALUES ('omega', 3)");
        s.close ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        connection_.close ();

        // Drop the table using another connection
        // because if some variations fail, they leave the
        // table locked.  This way all locks are dropped first.
        Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        Statement s = c.createStatement ();
        s.executeUpdate ("DROP TABLE " + table_);
        s.close ();
        c.close ();
    }



/**
Verifies that the cursor can be referenced.

@param cursorName   The cursor name.
@param oldId        The old value of the id.
@return             true if the cursor was referenced.
**/
    private boolean verifyCursor (String cursorName, int oldId)
        throws Exception
    {
        // Update the row from another statement using
        // the cursor name.
        Statement s = connection_.createStatement ();
        s.executeUpdate ("UPDATE " + table_ + " SET ID = "
                + (oldId + 1) + " WHERE CURRENT OF " + cursorName);

        // Verify that the row changed.
        ResultSet rs = s.executeQuery ("SELECT * FROM " + table_
            + " WHERE NAME = 'omega'");
        rs.next ();
	int actualValue = rs.getInt ("ID");
	int expectedValue = oldId + 1;
//        boolean success = (rs.getInt ("ID") == (oldId + 1));

	boolean success = (actualValue == expectedValue);

	if(!success)
	    System.out.println(actualValue+" sb "+expectedValue);

        // Cleanup.
        rs.close ();
        s.close ();
        return success;
    }



/**
setCursorName()/getCursorName() - Pass a valid cursor name,
should assign a default name.
**/
    public void Var001 ()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setCursorName ("GATITO");
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean success = rs.getCursorName ().equals ("GATITO");
            rs.close ();
            s.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
setCursorName()/getCursorName() - Pass null, should assign a
default name.
**/
    public void Var002 ()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setCursorName (null);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean success = rs.getCursorName ().length () > 0;
            rs.close ();
            s.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
setCursorName() - Pass an empty string, should throw an exception
when we try to use it.
**/
    public void Var003 ()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setCursorName ("");
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            failed("Didn't throw SQLException"+rs);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setCursorName() - Set the cursor name on a closed statement,
should throw an exception.
**/
    public void Var004 ()
    {
        try {
            Statement s = connection_.createStatement ();
            s.close ();
            s.setCursorName ("HOLA");
            failed("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertClosedException(e, ""); 
        }
    }



/**
setCursorName() - Set the cursor name.  Verify that we can
reference the cursor using another statement.  This uses
FOR UPDATE OF.
**/
    public void Var005()
    {
        try {
            Statement s1 = connection_.createStatement ();
            s1.setCursorName ("ADIOS");
            ResultSet rs1 = s1.executeQuery ("SELECT * FROM "
                + table_ + " FOR UPDATE OF ID");
            while (rs1.next ()) {
                if (rs1.getString ("NAME").equals ("omega"))
                    break;
            }

            boolean success = verifyCursor ("ADIOS", rs1.getInt ("ID"));
            rs1.close ();
            s1.close ();

            assertCondition (success);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
setCursorName() - Set the cursor name.  Verify that we can
reference the cursor using another statement.  This uses
FOR UPDATE.
**/
    public void Var006()
    {
        try {
            Statement s1 = connection_.createStatement ();
            s1.setCursorName ("AMIGOS");
            ResultSet rs1 = s1.executeQuery ("SELECT * FROM "
                + table_ + " FOR UPDATE");
            while (rs1.next ())
                if (rs1.getString ("NAME").equals ("omega"))
                    break;

            boolean success = verifyCursor ("AMIGOS", rs1.getInt ("ID"));
            rs1.close ();
            s1.close ();

            assertCondition (success);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
setCursorName() - Set the cursor name with a mixed case
name. Verify that we can reference the cursor using another
statement.

SQL400 - The native JDBC driver can't support mixed case cursor 
         names.  This is a restriction that is being imposed 
         because the DB has problems with it, not for anything
         within the JDBC driver.  What is done with any cursor
         name that is input is that it is changed to uppercase
         before the DB set method is called.
**/
    public void Var007()
    {
        try {
            Statement s1 = connection_.createStatement ();
            s1.setCursorName ("DiNeRo");
            ResultSet rs1 = s1.executeQuery ("SELECT * FROM "
                + table_ + " FOR UPDATE OF ID");
            while (rs1.next ())
                if (rs1.getString ("NAME").equals ("omega"))
                    break;

            // Note: Double quotes are needed to preserve case.
            boolean success;
            if (isToolboxDriver())
                success = verifyCursor ("\"DiNeRo\"", rs1.getInt ("ID"));
            else
                success = verifyCursor ("DINERO", rs1.getInt ("ID"));

            rs1.close ();
            s1.close ();

            assertCondition (success);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
setCursorName() - Set the same cursor name in 2 different
statement from the same connection.  Should throw an exception.

In V5R5, the error won't be detected unless SQ has the
two cursors open at the same time. 
**/
    public void Var008()
    {
        try {
            Statement s1 = connection_.createStatement();
            s1.setCursorName ("COCINA");
            Statement s2 = connection_.createStatement();
            s2.setCursorName ("COCINA");

	    s1.executeQuery("SELECT * FROM SYSIBM.SQLTABLES");
	    s2.executeQuery("SELECT * FROM SYSIBM.SQLTABLES"); 

            failed("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setCursorName() - Set the cursor while a result set is open.
Should throw an exception.
**/
    public void Var009()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.next ();
            s.setCursorName ("ZAPATOS");
            failed("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
setCursorName()/getCursorName() - Set a 128 character cursor name
**/
    public void Var010 ()
    {
	String added = " -- Added 06/21/06 by native driver to test for longer cursor names"; 
	if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    String longCursorName="LONGNAME101234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"; 
	    try {
		Statement s = connection_.createStatement ();
		s.setCursorName (longCursorName);
		ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
		boolean success = rs.getCursorName ().equals (longCursorName);
		rs.close ();
		s.close ();
		assertCondition (success, added);
	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception"+added);
	    }
	} else {
	    notApplicable("V5R5 longer cursor name test"); 
	} 
    }



/**
setCursorName() - Set a long cursor name.  Verify that we can
reference the cursor using another statement.  This uses
FOR UPDATE OF.
**/
    public void Var011()
    {
	String added = " -- Added 06/21/06 by native driver to test for longer cursor names"; 
	if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    String longCursorName="LONGNAME111234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"; 

	    try {
		Statement s1 = connection_.createStatement ();
		s1.setCursorName (longCursorName);
		ResultSet rs1 = s1.executeQuery ("SELECT * FROM "
						 + table_ + " FOR UPDATE OF ID");
		while (rs1.next ()) {
		    if (rs1.getString ("NAME").equals ("omega"))
			break;
		}

		boolean success = verifyCursor (longCursorName, rs1.getInt ("ID"));
		rs1.close ();
		s1.close ();

		assertCondition (success, added);
	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception "+added);
	    }
	} else {
	    notApplicable("V5R5 longer cursor name test"); 
	} 

    }



/**
setCursorName() - Set the long cursor name.  Verify that we can
reference the cursor using another statement.  This uses
FOR UPDATE.
**/
    public void Var012()
    {
	String added = " -- Added 06/21/06 by native driver to test for longer cursor names"; 
	if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    String longCursorName="LONGNAME121234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"; 

	    try {
		Statement s1 = connection_.createStatement ();
		s1.setCursorName (longCursorName);
		ResultSet rs1 = s1.executeQuery ("SELECT * FROM "
						 + table_ + " FOR UPDATE");
		while (rs1.next ())
		    if (rs1.getString ("NAME").equals ("omega"))
			break;

		boolean success = verifyCursor (longCursorName, rs1.getInt ("ID"));
		rs1.close ();
		s1.close ();

		assertCondition (success);
	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception"+added);
	    }
	} else {
	    notApplicable("V5R5 longer cursor name test"); 
	} 

    }




/**
setCursorName() - Set the same long  cursor name in 2 different
statement from the same connection.  Should throw an exception.
For the native JDBC driver, the exception will not occur unless
SQ attempts to open the cursor at the same time.
**/
    public void Var013()
    {
	String added = " -- Added 06/21/06 by native driver to test for longer cursor names"; 
	if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    String longCursorName="LONGNAME131234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"; 

	    try {
		Statement s1 = connection_.createStatement();
		s1.setCursorName (longCursorName);
		Statement s2 = connection_.createStatement();
		s2.setCursorName (longCursorName);
		s1.executeQuery("SELECT * FROM SYSIBM.SQLTABLES");
		s2.executeQuery("SELECT * FROM SYSIBM.SQLTABLES"); 
		failed("Didn't throw SQLException for setting same name for two different statements in same connection "+added);
	    }
	    catch(Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    String message = e.toString().toUpperCase();
		    boolean found = message.indexOf("ALREADY OPEN") >= 0 ;
		    if (found) {
			assertCondition(true); 
		    } else {
			e.printStackTrace(); 
			assertCondition(false, "'ALREADY OPEN' not found in '"+message+"'"); 
		    }

		} else { 
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException", added);
		}
	    }
	} else {
	    notApplicable("V5R5 longer cursor name test"); 
	} 

    }

//@C1A
/**
* setCursorName() - Set the name for a cursor to a value larger than the max size for a cursor name.
* Should throw an exception.
**/
    public void Var014()
    {
	String added = " -- Added 08/31/06 by toolbox driver to test for longer cursor names"; 
	if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    String longCursorName="LONGNAME1312345678921234567893123456789412345678951234567896123456789712345678981234567899123456789012345678911234567892123456789000"; 

	    try {
		Statement s1 = connection_.createStatement();
		s1.setCursorName (longCursorName);
		failed("Didn't throw SQLException for setting cursor name larger than max length "+added);
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException", added);
	    }
	} else {
	    notApplicable("V5R5 longer cursor name test"); 
	} 

    }

    long testSetCursorNameTime(Statement stmt, String name) throws SQLException {
	long startTime = System.currentTimeMillis();
	for (int i = 0; i < 1000; i++) {
	    stmt.setCursorName(name); 
	} 
	long endTime = System.currentTimeMillis();
	return endTime - startTime; 
    } 


    long testSetCursorNameTimeChange(Statement stmt, String base) throws SQLException {
	String name; 
	long startTime = System.currentTimeMillis();
	for (int i = 0; i < 1000; i++) {
	    name = base+i; 
	    stmt.setCursorName(name); 
	} 
	long endTime = System.currentTimeMillis();
	return endTime - startTime; 
    } 


/**
 * setCursorName.  Check the performance of set cursor name when going from 100 prepared statements on a different connect to 1000 prepared statements on a different connection
 */
    public void Var015() {

	if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V6R1 and latest testcase");
	    return;
	}

      String added = " -- added 11/10/2011 to test performance of setCursorName"; 
	try {
	    Statement s1 = connection_.createStatement ();
	    long firstTime = testSetCursorNameTime(s1, null);

	    Connection conn = testDriver_.getConnection (baseURL_ + ";errors=full", userId_, encryptedPassword_);

	    PreparedStatement [] ps = new PreparedStatement[1000];
	    for (int i = 0; i < 100; i++) {
		ps[i] = conn.prepareStatement("SELECT * FROM SYSIBM.SYSDUMMY1"); 
	    } 

	    long after100time = testSetCursorNameTime(s1, null);

	    for (int i = 100; i < 1000; i++) {
		ps[i] = conn.prepareStatement("SELECT * FROM SYSIBM.SYSDUMMY1"); 
	    } 

	    long after1000time = testSetCursorNameTime(s1, null);

	    System.out.println("firstTime     ="+firstTime);
	    System.out.println("After 100 PS  ="+after100time);
	    System.out.println("After 1000 PS ="+after1000time);
	    conn.close(); 
	    s1.close();
	    String maxTimeInfo; 
	    long maxTime;
	    maxTime = 2 * firstTime; maxTimeInfo = "2 * first time";
	    if (maxTime < firstTime + after100time) {
		maxTime = firstTime + after100time; maxTimeInfo = "firstTime + after100time";
	    } 
	    
	    assertCondition (after1000time <= maxTime, "after1000 time is not less than "+maxTimeInfo );
	}
	catch (Exception e) {
	    failed(e, "Unexpected Exception"+added);
	}


    } 


    public void Var016() {

	if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V6R1 and latest testcase");
	    return;
	}

      String added = " -- added 11/10/2011 to test performance of setCursorName"; 
	try {
	    Statement s1 = connection_.createStatement ();
	    long firstTime = testSetCursorNameTimeChange(s1, "JDSCN15");

	    Connection conn = testDriver_.getConnection (baseURL_ + ";errors=full", userId_, encryptedPassword_);

	    PreparedStatement [] ps = new PreparedStatement[1000];
	    for (int i = 0; i < 100; i++) {
		ps[i] = conn.prepareStatement("SELECT * FROM SYSIBM.SYSDUMMY1"); 
	    } 

	    long after100time = testSetCursorNameTimeChange(s1, "JDSCN15");

	    for (int i = 100; i < 1000; i++) {
		ps[i] = conn.prepareStatement("SELECT * FROM SYSIBM.SYSDUMMY1"); 
	    } 

	    long after1000time = testSetCursorNameTimeChange(s1, "JDSCN15");

	    System.out.println("firstTime     ="+firstTime);
	    System.out.println("After 100 PS  ="+after100time);
	    System.out.println("After 1000 PS ="+after1000time);
	    conn.close(); 
	    s1.close(); 
	    assertCondition (after1000time <= 3.5 * firstTime , "after1000time is not less than 3.5 * firstTime");
	}
	catch (Exception e) {
	    failed(e, "Unexpected Exception"+added);
	}


    } 



}


