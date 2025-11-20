///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementBatch.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementBatch.java
//
// Classes:      JDStatementBatch
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDStatementBatch.  This tests the following method
of the JDBC Statement class:

<ul>
<li>addBatch()</li>
<li>clearBatch()</li>
<li>executeBatch()</li>
<li>executeLargeBatch()</li>
</ul>
**/
public class JDStatementBatch
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementBatch";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }


    // Private data. (These values must be reset in setup because
    // JDStatementTest.COLLECTION could change). 
    private static String table_  = JDStatementTest.COLLECTION + ".JDSB";
    private static String table2_  = JDStatementTest.COLLECTION + ".JDSB2";




/**
Constructor.
**/
    public JDStatementBatch (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
    {
        super (systemObject, "JDStatementBatch",
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
        table_  = JDStatementTest.COLLECTION + ".JDSB";
	table2_  = JDStatementTest.COLLECTION + ".JDSB2";

        String url = baseURL_;
        connection_ = testDriver_.getConnection (url, userId_, encryptedPassword_);

        Statement s = connection_.createStatement ();
	initTable(s,  table_ , " (NAME VARCHAR(10), ID INTEGER)");
        



	initTable(s,  table2_
                , " (NAME VARCHAR(10), ID INTEGER)");

        s.close ();
        connection_.commit(); // for xa
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        Statement s = connection_.createStatement ();
        cleanupTable(s,  table_);
        cleanupTable(s,  table2_);
 
        s.close ();
        connection_.commit(); // for xa
        connection_.close ();
        connection_ = null; 

    }



/**
Counts the rows that match a pattern.
**/
    private int countRows (String pattern)
    throws SQLException
    {
        int count = 0;
        Statement s = connection_.createStatement ();
        ResultSet rs = s.executeQuery ("SELECT * FROM " + table_
                                       + " WHERE NAME LIKE '" + pattern + "'");
        while (rs.next ())
            ++count;
        rs.close ();
        s.close ();
        return count;
    }



/**
Determines if the batch is clear.
**/
    private boolean isBatchCleared (Statement s)
    throws SQLException
    {
        // Verify that the batch was clear by counting the rows
        // and executing the batch again.
        int before = countRows ("%");
        s.executeBatch ();
        int after = countRows ("%");
        return(before == after);
    }


/**
Set a warning for the native driver
*/
    private void setNativeWarning(Statement s) {
	// Use reflection to set a Warning
	// Reflection must be used to permit the toolbox to compile
	try {	
	    SQLWarning warning = new SQLWarning("This is a warning");

	    Class<?> db2Statement = Class.forName("com.ibm.db2.jdbc.app.DB2Statement");
	    Class<?>[]  args  = new Class[1];
	    args[0] = Class.forName("java.sql.SQLWarning");
	    java.lang.reflect.Method method = db2Statement.getMethod("addWarning", args);
	    Object[] parms = new Object[1];
	    parms[0] = warning; 
	    method.invoke(s, parms); 
	} catch (Exception e) {
	    e.printStackTrace();
	} 
    }

/**
addBatch() - Add a statement to the batch after the statement
has been closed.  Should throw an exception.
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.close ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('ANGELA')");
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
addBatch() - Add a null statement to the batch.  Should
throw an exception.
**/
    public void Var002 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch (null);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
addBatch() - Add an empty statement to the batch.  Should
throw an exception.
**/
    public void Var003 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("");
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
addBatch() - Add a blank statement to the batch.  Should
throw an exception.
**/
    public void Var004 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch (" ");
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
addBatch() - Add a statement that contains parameters to the batch.
Should throw an exception.
**/
    public void Var005 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES (?)");
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
addBatch() - Add a statement to the batch.  Verify that it does
not get executed at that time.
**/
    public void Var006 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('JIM')");
                int rows = countRows ("JIM");
                s.close ();
                assertCondition (rows == 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
addBatch() - Verify that addBatch() does NOT close the
current result set.
**/
    public void Var007 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                rs.next ();

                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('KEITH')");

                boolean resultSetClosed = false;
                try {
                    rs.getString (1);
                }
                catch (SQLException e1) {
                    resultSetClosed = true;
                }

                s.close ();
                assertCondition (resultSetClosed == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
addBatch() - Verify that addBatch() does NOT clear the warnings.
**/
    public void Var008 ()
    {
        if (checkJdbc20 ()) {
            if (isToolboxDriver() && 
                true )
            {
                try {
                    Statement s = connection_.createStatement();
                    s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");                
                    SQLWarning before = s.getWarnings ();
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('CHRISTOPHER')");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after != null));
                }
                catch (Exception e) {
                    failed(e, "Unexpected Exception");
                }
            }
            else if ( getDriver () == JDTestDriver.DRIVER_NATIVE  && 
                true )
	    {
		try {
		    Statement s = connection_.createStatement ();
		    setNativeWarning(s);
		    SQLWarning before = s.getWarnings ();
		    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('CHRISTOPHER')");
		    SQLWarning after = s.getWarnings ();
		    s.close ();
		    assertCondition ((before != null) && (after != null));
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}

	    }
            else
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE, 
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                    SQLWarning before = s.getWarnings ();
                    rs.close(); 
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('CHRISTOPHER')");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after != null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
executeBatch() - Execute the batch when the statement is closed.
Should throw an exception.
**/
    public void Var009 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('HOLA')");
                s.close ();
                s.executeBatch ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
executeBatch() - Execute the batch when no statements have been
added to the batch.
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                int before = countRows ("%");
                int[] updateCounts = s.executeBatch ();
                int after = countRows ("%");
                s.close ();
                assertCondition ((updateCounts.length == 0) && (before == after));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
addBatch()/executeBatch() - Execute the batch when 1 statement
has been added to the batch.
**/
    public void Var011 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('AIR')");
                int[] updateCounts = s.executeBatch ();
                int rows = countRows ("AIR");
                s.close ();
                assertCondition ((updateCounts.length == 1)
                                 && (updateCounts[0] == 1)
                                 && (rows == 1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when 2 statements
have been added to the batch.
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('MOON1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('MOON2')");
                int[] updateCounts = s.executeBatch ();
                int rows = countRows ("MOON%");
                s.close ();
                assertCondition ((updateCounts.length == 2)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (rows == 2));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when 5 statements
have been added to the batch.
**/
    public void Var013 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI5')");
                int[] updateCounts = s.executeBatch ();
                int rows = countRows ("SAFARI%");
                s.close ();
                assertCondition ((updateCounts.length == 5)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (updateCounts[2] == 1)
                                 && (updateCounts[3] == 1)
                                 && (updateCounts[4] == 1)
                                 && (rows == 5));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the statements
have update counts other than 1.
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                for (int i = 1; i <= 10; ++i)
                    s.executeUpdate ("INSERT INTO " + table_ + "(NAME) VALUES ('BERNARD" + i + "')");
                for (int i = 1; i <= 3; ++i)
                    s.executeUpdate ("INSERT INTO " + table_ + "(NAME) VALUES ('HOWARD" + i + "')");

                s.addBatch ("UPDATE " + table_ + " SET ID=200 WHERE NAME LIKE 'BERNARD%'");
                s.addBatch ("UPDATE " + table_ + " SET ID=400 WHERE NAME LIKE 'HOWARD%'");
                int[] updateCounts = s.executeBatch ();
                s.close ();
                assertCondition ((updateCounts.length == 2)
                                 && (updateCounts[0] == 10)
                                 && (updateCounts[1] == 3));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the first of 5
statements results in an error.
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES ('STAR1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('STAR2')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('STAR3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('STAR4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('STAR5')");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("STAR%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                                 && (updateCounts.length == 0)
                                 && (rows == 0));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the third of 5
statements results in an error.
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('KELLY1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('KELLY2')");
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES ('KELLY3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('KELLY4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('KELLY5')");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("KELLY%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                                 && (updateCounts.length == 2)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (rows == 2));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the last of 5
statements results in an error.
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('WATCH1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('WATCH2')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('WATCH3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('WATCH4')");
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES ('WATCH5')");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("WATCH%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                                 && (updateCounts.length == 4)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (updateCounts[2] == 1)
                                 && (updateCounts[3] == 1)
                                 && (rows == 4));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
addBatch()/executeBatch() - Execute the batch when one of the statements
contains a query.  Should throw an exception.
<P>
SQL400 - the native driver changed in v4r5 to not throw an exception when
the user passes a query through executeUpdate.  That means that queries can
be passed through a batch as well.  The update count for a query is always
-1, but the rest of the batch is expected to complete successfully.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('EASY1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('EASY2')");
                s.addBatch ("SELECT * FROM QIWS.QCUSTCDT");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('EASY4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('EASY5')");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("EASY%");
                s.close ();

                if (isToolboxDriver() ||
                    getDriver () == JDTestDriver.DRIVER_NATIVE )
                    assertCondition ((exceptionCaught == true)
                                     && (updateCounts.length == 2)
                                     && (updateCounts[0] == 1)
                                     && (updateCounts[1] == 1)
                                     && (rows == 2));
                else
                    assertCondition ((exceptionCaught == false)
                                     && (updateCounts.length == 5)
                                     && (updateCounts[0] == 1)
                                     && (updateCounts[1] == 1)
                                     && (updateCounts[2] == -1)
                                     && (updateCounts[3] == 1)
                                     && (updateCounts[4] == 1)
                                     && (rows == 4));

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeBatch() - Verify that executeBatch() clears the batch
after executing it (when no errors occurred).
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('HOA1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('HOA2')");
                int[] updateCounts = s.executeBatch ();
                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true && updateCounts.length==2);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeBatch() - Verify that executeBatch() clears the batch
after executing it (when errors occurred).
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES ('TRAN1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('TRAN2')");
                try {
                    s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    // Ignore.
                }

                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeBatch() - Verify that executeBatch() closes the
current result set.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                rs.next ();

                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('HOA1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('HOA2')");
                int[] updateCounts = s.executeBatch ();

                boolean resultSetClosed = false;
                try {
                    rs.getString (1);
                }
                catch (SQLException e1) {
                    resultSetClosed = true;
                }

                s.close ();
                assertCondition (resultSetClosed == true && updateCounts.length==2);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeBatch() - Verify that addBatch() clears the warnings.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
            if (isToolboxDriver() && 
                true )
            {
                try {
                Statement s = connection_.createStatement();
                s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
                SQLWarning before = s.getWarnings ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('BERNARD')");
                s.executeBatch ();
                SQLWarning after = s.getWarnings ();
                s.close ();
                assertCondition ((before != null) && (after == null));
                }
                catch (Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
            else if ( getDriver () == JDTestDriver.DRIVER_NATIVE  && 
                true )
	    {
		try {
		    Statement s = connection_.createStatement ();
		    setNativeWarning(s);

                    SQLWarning before = s.getWarnings ();
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('BERNARD')");
                    s.executeBatch ();
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));

		} catch (Exception e) {
		    e.printStackTrace();
		} 
	    }
            else
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE, 
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                    SQLWarning before = s.getWarnings ();
                    rs.close(); 
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('BERNARD')");
                    s.executeBatch ();
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
clearBatch() - Clear the batch after the statement
has been closed.  Should throw an exception.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.close ();
                s.clearBatch ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
clearBatch() - Verify that this has no effect when the batch
is already empty.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.clearBatch ();
                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
clearBatch() - Verify that this works when the batch
is not empty.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('DIFFICULT1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('DIFFICULT2')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('DIFFICULT3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('DIFFICULT4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('DIFFICULT5')");
                s.clearBatch ();
                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
executeBatch() -- Make sure we can execute statements with question marks that are not parameter markers.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('QUESTION1?')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('QUESTION2?')");
		s.executeBatch();
                s.close ();
                assertCondition (true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
addBatch() -- make sure we fail when we execute add statement with parameter markers
*/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('QUESTION1?')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES (?)");
		s.executeBatch();
                s.close ();
                failed("Didn't throw exception"); 
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    addBatch()/executeBatch() - Execute the batch of UPDATE STATEMENT v7r1
    **/
        public void Var028 ()
        {
            if (checkJdbc20 ()) {
                try {
                    Statement s = connection_.createStatement ();
                    s.execute("DELETE FROM " + table_);
                    
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI3')");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI4')");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI5')");
                    s.executeBatch();
            
                    s.addBatch ("UPDATE " + table_ + " SET NAME = 'SAFARI1a' where NAME = 'SAFARI1'");
                    s.addBatch ("UPDATE " + table_ + " SET NAME = 'SAFARI2a' where NAME = 'SAFARI2'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI3a' where NAME = 'SAFARI3'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI4a' where NAME = 'SAFARI4'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI5a' where NAME = 'SAFARI5'");
                    int[] updateCounts = s.executeBatch ();
                    int rows = countRows ("SAFARI%a");
                    s.close ();
                    assertCondition ((updateCounts.length == 5)
                                     && (updateCounts[0] == 1)
                                     && (updateCounts[1] == 1)
                                     && (updateCounts[2] == 1)
                                     && (updateCounts[3] == 1)
                                     && (updateCounts[4] == 1)
                                     && (rows == 5));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -added by TB 6/22/09");
                }
            }
        }


        /**
        addBatch()/executeBatch() - Execute the batch of DELETE STATEMENTS v7r1
        **/
            public void Var029 ()
            {
                if (checkJdbc20 ()) {
                    try {
                        Statement s = connection_.createStatement ();
                        s.execute("DELETE FROM " + table_);
                        
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI3')");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI4')");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI5')");
                        s.executeBatch();
                
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI1'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI2'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI3'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI4'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI5'");
                        int[] updateCounts = s.executeBatch ();
                        int rows = countRows ("SAFARI%");
                        s.close ();
                        assertCondition ((updateCounts.length == 5)
                                         && (updateCounts[0] == 1)
                                         && (updateCounts[1] == 1)
                                         && (updateCounts[2] == 1)
                                         && (updateCounts[3] == 1)
                                         && (updateCounts[4] == 1)
                                         && (rows == 0));
                    }
                    catch (Exception e) {
                        failed (e, "Unexpected Exception -added by TB 6/22/09");
                    }
                }
            }

            /**
            addBatch()/executeBatch() - Execute the batch of MERGE STATEMENTS v7r1
            **/
                public void Var030 ()
                {
                    if (checkJdbc20 ()) {
                        try {
                            Statement s = connection_.createStatement ();
                            s.execute("DELETE FROM " + table_);
                            s.execute("DELETE FROM " + table2_);
                            
                            s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                            s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                            s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES ('SAFARI1')");
                            s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES ('SAFARI2')");
                            s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES ('SAFARI3')");
                            s.executeBatch();
                    
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf a'"); //tab updated
                            s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')"); //insert new SAFARI1 after updated above
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf b'"); //1 updated from new row inserted above
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf c'"); // no matches
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf d'"); //no matches
                            
                            int[] updateCounts = s.executeBatch ();
                            int rows = countRows ("saf a%"); //sb 2 from first merge 
                            int rows2 = countRows ("saf b%"); //sb 1 from second merge 
                            s.close ();
                            assertCondition ((updateCounts.length == 5)
                                             && (updateCounts[0] == 2)
                                             && (updateCounts[1] == 1)
                                             && (updateCounts[2] == 1)
                                             && (updateCounts[3] == 0)
                                             && (updateCounts[4] == 0)
                                             && (rows == 2)
                                             && (rows2 == 1),
					     "updateCounts.length="+updateCounts.length+" sb  5\n"+
					     "updateCounts[0]="+updateCounts[0]+" sb 2\n"+
                                             "updateCounts[1]="+updateCounts[1]+" sb 1\n" +
					     "updateCounts[2]="+updateCounts[2]+" sb 1\n" +
					     "updateCounts[3]="+updateCounts[3]+" sb 0\n" +
					     "updateCounts[4]="+updateCounts[4]+" sb 0\n" +
					     "rows="+rows+" sb  2\n" +
					     "rows2="+rows2+" sb 1"); 
                        }
                        catch (Exception e) {
                            failed (e, "Unexpected Exception -added by TB 6/22/09");
                        }
                    }
                }



/**
addBatch() - Add a statement to the batch after the statement
has been closed.  Should throw an exception.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("-- added 9/23/2013 to test values with no parenthesis"); 
            try {
                Statement s = connection_.createStatement ();
                s.close ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'ANGELA'");
                failed ("Didn't throw SQLException " +sb.toString()); 
            }
            catch (Exception e) {
		assertClosedException(e, sb);
            }
        }
    }





/**
addBatch() - Add a statement that contains parameters to the batch.
Should throw an exception.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ?");
                failed ("Didn't throw SQLException"+sb.toString());
            }
            catch (Exception e) {
                assertExceptionContains (e, "does not match the number of parameters", sb);
            }
        }
    }



/**
addBatch() - Add a statement to the batch.  Verify that it does
not get executed at that time.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 
            try {

                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'JIM'");
                int rows = countRows ("JIM");
                s.close ();
                assertCondition (rows == 0, "rows="+rows+" sb 0 "+sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
addBatch() - Verify that addBatch() does NOT close the
current result set.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {

                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                rs.next ();

                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'KEITH'");

                boolean resultSetClosed = false;
                try {
                    rs.getString (1);
                }
                catch (SQLException e1) {
                    resultSetClosed = true;
                }

                s.close ();
                assertCondition (resultSetClosed == false,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
addBatch() - Verify that addBatch() does NOT clear the warnings.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            if (isToolboxDriver() && 
                true )
            {
                try {
                    Statement s = connection_.createStatement();
                    s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");                
                    SQLWarning before = s.getWarnings ();
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'CHRISTOPHER'");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after != null),sb);
                }
                catch (Exception e) {
                    failed(e, "Unexpected Exception"+sb.toString());
                }
            }
            else if ( getDriver () == JDTestDriver.DRIVER_NATIVE  && 
                true )
	    {
		try {
		    Statement s = connection_.createStatement ();
		    setNativeWarning(s);
		    SQLWarning before = s.getWarnings ();
		    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'CHRISTOPHER'");
		    SQLWarning after = s.getWarnings ();
		    s.close ();
		    assertCondition ((before != null) && (after != null),sb);
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception"+sb.toString());
		}

	    }
            else
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE, 
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                    SQLWarning before = s.getWarnings ();
                    rs.close(); 
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'CHRISTOPHER'");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after != null),sb);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception"+sb.toString());
                }
            }
        }
    }



/**
executeBatch() - Execute the batch when the statement is closed.
Should throw an exception.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'HOLA'");
                s.close ();
                s.executeBatch ();
                failed ("Didn't throw SQLException"+sb.toString());
            }
            catch (Exception e) {
		assertClosedException(e, sb);
            }
        }
    }







/**
addBatch()/executeBatch() - Execute the batch when 1 statement
has been added to the batch.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'AIR'");
                int[] updateCounts = s.executeBatch ();
                int rows = countRows ("AIR");
                s.close ();
                assertCondition ((updateCounts.length == 1)
                                 && (updateCounts[0] == 1)
                                 && (rows == 1), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when 2 statements
have been added to the batch.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'MOON1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'MOON2'");
                int[] updateCounts = s.executeBatch ();
                int rows = countRows ("MOON%");
                s.close ();
                assertCondition ((updateCounts.length == 2)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (rows == 2), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when 5 statements
have been added to the batch.
**/
    public void Var039 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI2'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI3'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI4'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI5'");
                int[] updateCounts = s.executeBatch ();
                int rows = countRows ("SAFARI%");
                s.close ();
                assertCondition ((updateCounts.length == 5)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (updateCounts[2] == 1)
                                 && (updateCounts[3] == 1)
                                 && (updateCounts[4] == 1)
                                 && (rows == 5), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+ sb.toString());
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the statements
have update counts other than 1.
**/
    public void Var040 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                for (int i = 1; i <= 10; ++i)
                    s.executeUpdate ("INSERT INTO " + table_ + "(NAME) VALUES 'BERNARD" + i + "'");
                for (int i = 1; i <= 3; ++i)
                    s.executeUpdate ("INSERT INTO " + table_ + "(NAME) VALUES 'HOWARD" + i + "'");

                s.addBatch ("UPDATE " + table_ + " SET ID=200 WHERE NAME LIKE 'BERNARD%'");
                s.addBatch ("UPDATE " + table_ + " SET ID=400 WHERE NAME LIKE 'HOWARD%'");
                int[] updateCounts = s.executeBatch ();
                s.close ();
                assertCondition ((updateCounts.length == 2)
                                 && (updateCounts[0] == 10)
                                 && (updateCounts[1] == 3), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the first of 5
statements results in an error.
**/
    public void Var041 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES 'STAR1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'STAR2'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'STAR3'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'STAR4'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'STAR5'");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("STAR%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                                 && (updateCounts.length == 0)
                                 && (rows == 0), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the third of 5
statements results in an error.
**/
    public void Var042 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'KELLY1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'KELLY2'");
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES 'KELLY3'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'KELLY4'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'KELLY5'");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("KELLY%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                                 && (updateCounts.length == 2)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (rows == 2), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the last of 5
statements results in an error.
**/
    public void Var043 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'WATCH1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'WATCH2'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'WATCH3'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'WATCH4'");
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES 'WATCH5'");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("WATCH%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                                 && (updateCounts.length == 4)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (updateCounts[2] == 1)
                                 && (updateCounts[3] == 1)
                                 && (rows == 4), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
addBatch()/executeBatch() - Execute the batch when one of the statements
contains a query.  Should throw an exception.
<P>
SQL400 - the native driver changed in v4r5 to not throw an exception when
the user passes a query through executeUpdate.  That means that queries can
be passed through a batch as well.  The update count for a query is always
-1, but the rest of the batch is expected to complete successfully.
**/
    public void Var044 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'EASY1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'EASY2'");
                s.addBatch ("SELECT * FROM QIWS.QCUSTCDT");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'EASY4'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'EASY5'");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("EASY%");
                s.close ();

                if (isToolboxDriver() ||
                    getDriver () == JDTestDriver.DRIVER_NATIVE )
                    assertCondition ((exceptionCaught == true)
                                     && (updateCounts.length == 2)
                                     && (updateCounts[0] == 1)
                                     && (updateCounts[1] == 1)
                                     && (rows == 2), sb.toString());
                else
                    assertCondition ((exceptionCaught == false)
                                     && (updateCounts.length == 5)
                                     && (updateCounts[0] == 1)
                                     && (updateCounts[1] == 1)
                                     && (updateCounts[2] == -1)
                                     && (updateCounts[3] == 1)
                                     && (updateCounts[4] == 1)
                                     && (rows == 4), sb.toString());

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
executeBatch() - Verify that executeBatch() clears the batch
after executing it (when no errors occurred).
**/
    public void Var045 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'HOA1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'HOA2'");
                int[] updateCounts = s.executeBatch ();
                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true && updateCounts.length==2,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
executeBatch() - Verify that executeBatch() clears the batch
after executing it (when errors occurred).
**/
    public void Var046 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES 'TRAN1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'TRAN2'");
                try {
                    s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    // Ignore.
                }

                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
executeBatch() - Verify that executeBatch() closes the
current result set.
**/
    public void Var047 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                rs.next ();

                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'HOA1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'HOA2'");
                int[] updateCounts = s.executeBatch ();

                boolean resultSetClosed = false;
                try {
                    rs.getString (1);
                }
                catch (SQLException e1) {
                    resultSetClosed = true;
                }

                s.close ();
                assertCondition (resultSetClosed == true && updateCounts.length==2,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
executeBatch() - Verify that addBatch() clears the warnings.
**/
    public void Var048 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            if (isToolboxDriver() && 
                true )
            {
                try {
                Statement s = connection_.createStatement();
                s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
                SQLWarning before = s.getWarnings ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'BERNARD'");
                s.executeBatch ();
                SQLWarning after = s.getWarnings ();
                s.close ();
                assertCondition ((before != null) && (after == null),sb);
                }
                catch (Exception e)
                {
                    failed (e, "Unexpected Exception"+ sb.toString());
                }
            }
            else if ( getDriver () == JDTestDriver.DRIVER_NATIVE  && 
                true )
	    {
		try {
		    Statement s = connection_.createStatement ();
		    setNativeWarning(s);

                    SQLWarning before = s.getWarnings ();
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'BERNARD'");
                    s.executeBatch ();
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null),sb);

		} catch (Exception e) {
                    failed (e, "Unexpected Exception"+ sb.toString());
		} 
	    }
            else
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE, 
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                    SQLWarning before = s.getWarnings ();
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('BERNARD')");
                    s.executeBatch ();
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null), "rs="+rs+" "+sb.toString());
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception"+sb.toString());
                }
            }
        }
    }






/**
clearBatch() - Verify that this works when the batch
is not empty.
**/
    public void Var049 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'DIFFICULT1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'DIFFICULT2'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'DIFFICULT3'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'DIFFICULT4'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'DIFFICULT5'");
                s.clearBatch ();
                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }


/**
executeBatch() -- Make sure we can execute statements with question marks that are not parameter markers.
**/
    public void Var050 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'QUESTION1?'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'QUESTION2?'");
		s.executeBatch();
                s.close ();
                assertCondition (true,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }

/**
addBatch() -- make sure we fail when we execute add statement with parameter markers
*/
    public void Var051 ()
    {
        if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'QUESTION1?'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ?");
		s.executeBatch();
                s.close ();
                failed("Didn't throw exception"+ sb.toString()); 
            }
            catch (Exception e) {
                assertExceptionContains (e, "does not match the number", sb);
            }
        }
    }



    /**
    addBatch()/executeBatch() - Execute the batch of UPDATE STATEMENT v7r1
    **/
        public void Var052 ()
        {
            if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

                try {
                    Statement s = connection_.createStatement ();
                    s.execute("DELETE FROM " + table_);
                    
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI1'");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI2'");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI3'");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI4'");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI5'");
                    s.executeBatch();
            
                    s.addBatch ("UPDATE " + table_ + " SET NAME = 'SAFARI1a' where NAME = 'SAFARI1'");
                    s.addBatch ("UPDATE " + table_ + " SET NAME = 'SAFARI2a' where NAME = 'SAFARI2'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI3a' where NAME = 'SAFARI3'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI4a' where NAME = 'SAFARI4'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI5a' where NAME = 'SAFARI5'");
                    int[] updateCounts = s.executeBatch ();
                    int rows = countRows ("SAFARI%a");
                    s.close ();
                    assertCondition ((updateCounts.length == 5)
                                     && (updateCounts[0] == 1)
                                     && (updateCounts[1] == 1)
                                     && (updateCounts[2] == 1)
                                     && (updateCounts[3] == 1)
                                     && (updateCounts[4] == 1)
                                     && (rows == 5), sb.toString());
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception "+sb.toString());
                }
            }
        }


        /**
        addBatch()/executeBatch() - Execute the batch of DELETE STATEMENTS v7r1
        **/
            public void Var053 ()
            {
                if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

                    try {
                        Statement s = connection_.createStatement ();
                        s.execute("DELETE FROM " + table_);
                        
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI1'");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI2'");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI3'");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI4'");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI5'");
                        s.executeBatch();
                
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI1'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI2'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI3'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI4'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI5'");
                        int[] updateCounts = s.executeBatch ();
                        int rows = countRows ("SAFARI%");
                        s.close ();
                        assertCondition ((updateCounts.length == 5)
                                         && (updateCounts[0] == 1)
                                         && (updateCounts[1] == 1)
                                         && (updateCounts[2] == 1)
                                         && (updateCounts[3] == 1)
                                         && (updateCounts[4] == 1)
                                         && (rows == 0), sb.toString());
                    }
                    catch (Exception e) {
                        failed (e, "Unexpected Exception "+sb.toString());
                    }
                }
            }

            /**
            addBatch()/executeBatch() - Execute the batch of MERGE STATEMENTS v7r1
            **/
                public void Var054 ()
                {
                    if (checkJdbc20 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

                        try {
                            Statement s = connection_.createStatement ();
                            s.execute("DELETE FROM " + table_);
                            s.execute("DELETE FROM " + table2_);
                            
                            s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI1'");
                            s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI2'");
                            s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES 'SAFARI1'");
                            s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES 'SAFARI2'");
                            s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES 'SAFARI3'");
                            s.executeBatch();
                    
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf a'"); //tab updated
                            s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')"); //insert new SAFARI1 after updated above
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf b'"); //1 updated from new row inserted above
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf c'"); // no matches
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf d'"); //no matches
                            
                            int[] updateCounts = s.executeBatch ();
                            int rows = countRows ("saf a%"); //sb 2 from first merge 
                            int rows2 = countRows ("saf b%"); //sb 1 from second merge 
                            s.close ();
                            assertCondition ((updateCounts.length == 5)
                                             && (updateCounts[0] == 2)
                                             && (updateCounts[1] == 1)
                                             && (updateCounts[2] == 1)
                                             && (updateCounts[3] == 0)
                                             && (updateCounts[4] == 0)
                                             && (rows == 2)
                                             && (rows2 == 1),
					     "updateCounts.length="+updateCounts.length+" sb  5\n"+
					     "updateCounts[0]="+updateCounts[0]+" sb 2\n"+
                                             "updateCounts[1]="+updateCounts[1]+" sb 1\n" +
					     "updateCounts[2]="+updateCounts[2]+" sb 1\n" +
					     "updateCounts[3]="+updateCounts[3]+" sb 0\n" +
					     "updateCounts[4]="+updateCounts[4]+" sb 0\n" +
					     "rows="+rows+" sb  2\n" +
					     "rows2="+rows2+" sb 1"+ sb.toString()); 
                        }
                        catch (Exception e) {
                            failed (e, "Unexpected Exception"+sb.toString());
                        }
                    }
                }








/**
executeLargeBatch() - Execute the batch when the statement is closed.
Should throw an exception.
**/
    public void Var055 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('HOLA')");
                s.close ();
                JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
executeLargeBatch() - Execute the batch when no statements have been
added to the batch.
**/
    public void Var056 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                int before = countRows ("%");
                long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                int after = countRows ("%");
                s.close ();
                assertCondition ((updateCounts.length == 0) && (before == after));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
addBatch()/executeLargeBatch() - Execute the batch when 1 statement
has been added to the batch.
**/
    public void Var057 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('AIR')");
                long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                int rows = countRows ("AIR");
                s.close ();
                assertCondition ((updateCounts.length == 1)
                                 && (updateCounts[0] == 1)
                                 && (rows == 1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when 2 statements
have been added to the batch.
**/
    public void Var058 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('MOON1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('MOON2')");
                long[] updateCounts = (long []) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                int rows = countRows ("MOON%");
                s.close ();
                assertCondition ((updateCounts.length == 2)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (rows == 2));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when 5 statements
have been added to the batch.
**/
    public void Var059 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI5')");
                long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                int rows = countRows ("SAFARI%");
                s.close ();
                assertCondition ((updateCounts.length == 5)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (updateCounts[2] == 1)
                                 && (updateCounts[3] == 1)
                                 && (updateCounts[4] == 1)
                                 && (rows == 5));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when the statements
have update counts other than 1.
**/
    public void Var060 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                for (int i = 1; i <= 10; ++i)
                    s.executeUpdate ("INSERT INTO " + table_ + "(NAME) VALUES ('BERNARD" + i + "')");
                for (int i = 1; i <= 3; ++i)
                    s.executeUpdate ("INSERT INTO " + table_ + "(NAME) VALUES ('HOWARD" + i + "')");

                s.addBatch ("UPDATE " + table_ + " SET ID=200 WHERE NAME LIKE 'BERNARD%'");
                s.addBatch ("UPDATE " + table_ + " SET ID=400 WHERE NAME LIKE 'HOWARD%'");
                long[] updateCounts = (long[])  JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                s.close ();
                assertCondition ((updateCounts.length == 2)
                                 && (updateCounts[0] == 10)
                                 && (updateCounts[1] == 3));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when the first of 5
statements results in an error.
**/
    public void Var061 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES ('STAR1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('STAR2')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('STAR3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('STAR4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('STAR5')");

                long[] longUpdateCounts = null;
                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    longUpdateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("STAR%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                                 && updateCounts != null 
                                 && (updateCounts.length == 0)
                                 && (rows == 0), 
                                  "Error exceptionCaught = false longUpdateCounts="+longUpdateCounts);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when the third of 5
statements results in an error.
**/
    public void Var062 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('KELLY1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('KELLY2')");
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES ('KELLY3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('KELLY4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('KELLY5')");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("KELLY%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                                 && (updateCounts != null)
                                 && (updateCounts.length == 2)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (rows == 2));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when the last of 5
statements results in an error.
**/
    public void Var063 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('WATCH1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('WATCH2')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('WATCH3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('WATCH4')");
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES ('WATCH5')");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("WATCH%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                    && (updateCounts != null) 
                                 && (updateCounts.length == 4)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (updateCounts[2] == 1)
                                 && (updateCounts[3] == 1)
                                 && (rows == 4));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
addBatch()/executeLargeBatch() - Execute the batch when one of the statements
contains a query.  Should throw an exception.
<P>
SQL400 - the native driver changed in v4r5 to not throw an exception when
the user passes a query through executeUpdate.  That means that queries can
be passed through a batch as well.  The update count for a query is always
-1, but the rest of the batch is expected to complete successfully.
**/
    public void Var064 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('EASY1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('EASY2')");
                s.addBatch ("SELECT * FROM QIWS.QCUSTCDT");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('EASY4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('EASY5')");

                long[] longUpdateCounts = null;
                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    longUpdateCounts = (long []) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("EASY%");
                s.close ();

                if (isToolboxDriver() ||
                    getDriver () == JDTestDriver.DRIVER_NATIVE )
                    assertCondition ((exceptionCaught == true)
                                     && (updateCounts != null) 
                                     && (updateCounts.length == 2)
                                     && (updateCounts[0] == 1)
                                     && (updateCounts[1] == 1)
                                     && (rows == 2));
                else
                    assertCondition ((exceptionCaught == false)
                                     && (longUpdateCounts != null) 
                                     && (longUpdateCounts.length == 5)
                                     && (longUpdateCounts[0] == 1)
                                     && (longUpdateCounts[1] == 1)
                                     && (longUpdateCounts[2] == -1)
                                     && (longUpdateCounts[3] == 1)
                                     && (longUpdateCounts[4] == 1)
                                     && (rows == 4));

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeLargeBatch() - Verify that executeLargeBatch() clears the batch
after executing it (when no errors occurred).
**/
    public void Var065 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('HOA1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('HOA2')");
                long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true && updateCounts.length==2);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeLargeBatch() - Verify that executeLargeBatch() clears the batch
after executing it (when errors occurred).
**/
    public void Var066 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES ('TRAN1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('TRAN2')");
                try {
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                }
                catch (BatchUpdateException bue) {
                    // Ignore.
                }

                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeLargeBatch() - Verify that executeLargeBatch() closes the
current result set.
**/
    public void Var067 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                rs.next ();

                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('HOA1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('HOA2')");
                long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");

                boolean resultSetClosed = false;
                try {
                    rs.getString (1);
                }
                catch (SQLException e1) {
                    resultSetClosed = true;
                }

                s.close ();
                assertCondition (resultSetClosed == true && updateCounts.length==2);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeLargeBatch() - Verify that addBatch() clears the warnings.
**/
    public void Var068 ()
    {
        if (checkJdbc42 ()) {
            if (isToolboxDriver() && 
                true )
            {
                try {
                Statement s = connection_.createStatement();
                s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
                SQLWarning before = s.getWarnings ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('BERNARD')");
                JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                SQLWarning after = s.getWarnings ();
                s.close ();
                assertCondition ((before != null) && (after == null));
                }
                catch (Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
            else if ( getDriver () == JDTestDriver.DRIVER_NATIVE  && 
                true )
	    {
		try {
		    Statement s = connection_.createStatement ();
		    setNativeWarning(s);

                    SQLWarning before = s.getWarnings ();
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('BERNARD')");
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));

		} catch (Exception e) {
		    e.printStackTrace();
		} 
	    }
            else
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE, 
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                    SQLWarning before = s.getWarnings ();
                    rs.close(); 
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('BERNARD')");
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }





/**
executeLargeBatch() -- Make sure we can execute statements with question marks that are not parameter markers.
**/
    public void Var069 ()
    {
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('QUESTION1?')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('QUESTION2?')");
		JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                s.close ();
                assertCondition (true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    addBatch()/executeLargeBatch() - Execute the batch of UPDATE STATEMENT v7r1
    **/
        public void Var070 ()
        {
            if (checkJdbc42 ()) {
                try {
                    Statement s = connection_.createStatement ();
                    s.execute("DELETE FROM " + table_);
                    
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI3')");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI4')");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI5')");
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
            
                    s.addBatch ("UPDATE " + table_ + " SET NAME = 'SAFARI1a' where NAME = 'SAFARI1'");
                    s.addBatch ("UPDATE " + table_ + " SET NAME = 'SAFARI2a' where NAME = 'SAFARI2'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI3a' where NAME = 'SAFARI3'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI4a' where NAME = 'SAFARI4'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI5a' where NAME = 'SAFARI5'");
                    long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                    int rows = countRows ("SAFARI%a");
                    s.close ();
                    assertCondition ((updateCounts.length == 5)
                                     && (updateCounts[0] == 1)
                                     && (updateCounts[1] == 1)
                                     && (updateCounts[2] == 1)
                                     && (updateCounts[3] == 1)
                                     && (updateCounts[4] == 1)
                                     && (rows == 5));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -added by TB 6/22/09");
                }
            }
        }


        /**
        addBatch()/executeLargeBatch() - Execute the batch of DELETE STATEMENTS v7r1
        **/
            public void Var071 ()
            {
                if (checkJdbc42 ()) {
                    try {
                        Statement s = connection_.createStatement ();
                        s.execute("DELETE FROM " + table_);
                        
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI3')");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI4')");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI5')");
                        JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI1'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI2'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI3'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI4'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI5'");
                        long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                        int rows = countRows ("SAFARI%");
                        s.close ();
                        assertCondition ((updateCounts.length == 5)
                                         && (updateCounts[0] == 1)
                                         && (updateCounts[1] == 1)
                                         && (updateCounts[2] == 1)
                                         && (updateCounts[3] == 1)
                                         && (updateCounts[4] == 1)
                                         && (rows == 0));
                    }
                    catch (Exception e) {
                        failed (e, "Unexpected Exception -added by TB 6/22/09");
                    }
                }
            }

            /**
            addBatch()/executeLargeBatch() - Execute the batch of MERGE STATEMENTS v7r1
            **/
                public void Var072 ()
                {
                    if (checkJdbc42 ()) {
                        try {
                            Statement s = connection_.createStatement ();
                            s.execute("DELETE FROM " + table_);
                            s.execute("DELETE FROM " + table2_);
                            
                            s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                            s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                            s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES ('SAFARI1')");
                            s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES ('SAFARI2')");
                            s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES ('SAFARI3')");
                            JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                    
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf a'"); //tab updated
                            s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')"); //insert new SAFARI1 after updated above
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf b'"); //1 updated from new row inserted above
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf c'"); // no matches
                            s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                    " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf d'"); //no matches
                            
                            long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                            int rows = countRows ("saf a%"); //sb 2 from first merge 
                            int rows2 = countRows ("saf b%"); //sb 1 from second merge 
                            s.close ();
                            assertCondition ((updateCounts.length == 5)
                                             && (updateCounts[0] == 2)
                                             && (updateCounts[1] == 1)
                                             && (updateCounts[2] == 1)
                                             && (updateCounts[3] == 0)
                                             && (updateCounts[4] == 0)
                                             && (rows == 2)
                                             && (rows2 == 1),
					     "updateCounts.length="+updateCounts.length+" sb  5\n"+
					     "updateCounts[0]="+updateCounts[0]+" sb 2\n"+
                                             "updateCounts[1]="+updateCounts[1]+" sb 1\n" +
					     "updateCounts[2]="+updateCounts[2]+" sb 1\n" +
					     "updateCounts[3]="+updateCounts[3]+" sb 0\n" +
					     "updateCounts[4]="+updateCounts[4]+" sb 0\n" +
					     "rows="+rows+" sb  2\n" +
					     "rows2="+rows2+" sb 1"); 
                        }
                        catch (Exception e) {
                            failed (e, "Unexpected Exception -added by TB 6/22/09");
                        }
                    }
                }





/**
executeLargeBatch() - Execute the batch when the statement is closed.
Should throw an exception.
**/
    public void Var073 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'HOLA'");
                s.close ();
                JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                failed ("Didn't throw SQLException"+sb.toString());
            }
            catch (Exception e) {
		assertClosedException(e, sb);
            }
        }
    }







/**
addBatch()/executeLargeBatch() - Execute the batch when 1 statement
has been added to the batch.
**/
    public void Var074 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'AIR'");
                long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                int rows = countRows ("AIR");
                s.close ();
                assertCondition ((updateCounts.length == 1)
                                 && (updateCounts[0] == 1)
                                 && (rows == 1), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when 2 statements
have been added to the batch.
**/
    public void Var075 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'MOON1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'MOON2'");
                long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                int rows = countRows ("MOON%");
                s.close ();
                assertCondition ((updateCounts.length == 2)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (rows == 2), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when 5 statements
have been added to the batch.
**/
    public void Var076 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI2'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI3'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI4'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI5'");
                long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                int rows = countRows ("SAFARI%");
                s.close ();
                assertCondition ((updateCounts.length == 5)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (updateCounts[2] == 1)
                                 && (updateCounts[3] == 1)
                                 && (updateCounts[4] == 1)
                                 && (rows == 5), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+ sb.toString());
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when the statements
have update counts other than 1.
**/
    public void Var077 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                for (int i = 1; i <= 10; ++i)
                    s.executeUpdate ("INSERT INTO " + table_ + "(NAME) VALUES 'BERNARD" + i + "'");
                for (int i = 1; i <= 3; ++i)
                    s.executeUpdate ("INSERT INTO " + table_ + "(NAME) VALUES 'HOWARD" + i + "'");

                s.addBatch ("UPDATE " + table_ + " SET ID=200 WHERE NAME LIKE 'BERNARD%'");
                s.addBatch ("UPDATE " + table_ + " SET ID=400 WHERE NAME LIKE 'HOWARD%'");
                long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                s.close ();
                assertCondition ((updateCounts.length == 2)
                                 && (updateCounts[0] == 10)
                                 && (updateCounts[1] == 3), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when the first of 5
statements results in an error.
**/
    public void Var078 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES 'STAR1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'STAR2'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'STAR3'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'STAR4'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'STAR5'");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("STAR%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                    && (updateCounts != null) 
                                 && (updateCounts.length == 0)
                                 && (rows == 0), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when the third of 5
statements results in an error.
**/
    public void Var079 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'KELLY1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'KELLY2'");
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES 'KELLY3'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'KELLY4'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'KELLY5'");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                     JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("KELLY%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                    && (updateCounts != null)
                                 && (updateCounts.length == 2)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (rows == 2), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }




/**
addBatch()/executeLargeBatch() - Execute the batch when the last of 5
statements results in an error.
**/
    public void Var080 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'WATCH1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'WATCH2'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'WATCH3'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'WATCH4'");
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES 'WATCH5'");

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("WATCH%");
                s.close ();
                assertCondition ((exceptionCaught == true)
                    &&  (updateCounts != null)
                                 && (updateCounts.length == 4)
                                 && (updateCounts[0] == 1)
                                 && (updateCounts[1] == 1)
                                 && (updateCounts[2] == 1)
                                 && (updateCounts[3] == 1)
                                 && (rows == 4), sb.toString());
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
addBatch()/executeLargeBatch() - Execute the batch when one of the statements
contains a query.  Should throw an exception.
<P>
SQL400 - the native driver changed in v4r5 to not throw an exception when
the user passes a query through executeUpdate.  That means that queries can
be passed through a batch as well.  The update count for a query is always
-1, but the rest of the batch is expected to complete successfully.
**/
    public void Var081 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'EASY1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'EASY2'");
                s.addBatch ("SELECT * FROM QIWS.QCUSTCDT");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'EASY4'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'EASY5'");

                int[] updateCounts = null;
                long[] longUpdateCounts = null;
                boolean exceptionCaught = false;
                try {
                    longUpdateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("EASY%");
                s.close ();

                if (isToolboxDriver() ||
                    getDriver () == JDTestDriver.DRIVER_NATIVE )
                    assertCondition ((exceptionCaught == true)
                                     && (updateCounts != null) 
                                     && (updateCounts.length == 2)
                                     && (updateCounts[0] == 1)
                                     && (updateCounts[1] == 1)
                                     && (rows == 2), sb.toString());
                else
                    assertCondition ((exceptionCaught == false)
                        && (longUpdateCounts != null)
                                     && (longUpdateCounts.length == 5)
                                     && (longUpdateCounts[0] == 1)
                                     && (longUpdateCounts[1] == 1)
                                     && (longUpdateCounts[2] == -1)
                                     && (longUpdateCounts[3] == 1)
                                     && (longUpdateCounts[4] == 1)
                                     && (rows == 4), sb.toString());

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
executeLargeBatch() - Verify that executeLargeBatch() clears the batch
after executing it (when no errors occurred).
**/
    public void Var082 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'HOA1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'HOA2'");
                long[] updateCounts = (long []) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true && updateCounts.length==2,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
executeLargeBatch() - Verify that executeLargeBatch() clears the batch
after executing it (when errors occurred).
**/
    public void Var083 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("ZZZZZZ INTO " + table_ + "(NAME) VALUES 'TRAN1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'TRAN2'");
                try {
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                }
                catch (BatchUpdateException bue) {
                    // Ignore.
                }

                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
executeLargeBatch() - Verify that executeLargeBatch() closes the
current result set.
**/
    public void Var084 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                rs.next ();

                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'HOA1'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'HOA2'");
                long[] updateCounts = (long[])  JDReflectionUtil.callMethod_O(s,"executeLargeBatch");

                boolean resultSetClosed = false;
                try {
                    rs.getString (1);
                }
                catch (SQLException e1) {
                    resultSetClosed = true;
                }

                s.close ();
                assertCondition (resultSetClosed == true && updateCounts.length==2,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }



/**
executeLargeBatch() - Verify that addBatch() clears the warnings.
**/
    public void Var085 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            if (isToolboxDriver() && 
                true )
            {
                try {
                Statement s = connection_.createStatement();
                s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
                SQLWarning before = s.getWarnings ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'BERNARD'");
                JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                SQLWarning after = s.getWarnings ();
                s.close ();
                assertCondition ((before != null) && (after == null),sb);
                }
                catch (Exception e)
                {
                    failed (e, "Unexpected Exception"+ sb.toString());
                }
            }
            else if ( getDriver () == JDTestDriver.DRIVER_NATIVE  && 
                true )
	    {
		try {
		    Statement s = connection_.createStatement ();
		    setNativeWarning(s);

                    SQLWarning before = s.getWarnings ();
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'BERNARD'");
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null),sb);

		} catch (Exception e) {
                    failed (e, "Unexpected Exception"+ sb.toString());
		} 
	    }
            else
            {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE, 
                                                               ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                    SQLWarning before = s.getWarnings ();
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('BERNARD')");
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                    SQLWarning after = s.getWarnings ();
                    s.close ();
                    assertCondition ((before != null) && (after == null), "rs="+rs+" "+sb.toString());
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception"+sb.toString());
                }
            }
        }
    }







/**
executeLargeBatch() -- Make sure we can execute statements with question marks that are not parameter markers.
**/
    public void Var086 ()
    {
        if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

            try {
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'QUESTION1?'");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'QUESTION2?'");
		JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                s.close ();
                assertCondition (true,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+sb.toString());
            }
        }
    }


    /**
    addBatch()/executeLargeBatch() - Execute the batch of UPDATE STATEMENT v7r1
    **/
        public void Var087 ()
        {
            if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

                try {
                    Statement s = connection_.createStatement ();
                    s.execute("DELETE FROM " + table_);
                    
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI1'");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI2'");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI3'");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI4'");
                    s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI5'");
                    JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
            
                    s.addBatch ("UPDATE " + table_ + " SET NAME = 'SAFARI1a' where NAME = 'SAFARI1'");
                    s.addBatch ("UPDATE " + table_ + " SET NAME = 'SAFARI2a' where NAME = 'SAFARI2'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI3a' where NAME = 'SAFARI3'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI4a' where NAME = 'SAFARI4'");
                    s.addBatch ("UPDAtE " + table_ + " SET NAME = 'SAFARI5a' where NAME = 'SAFARI5'");
                    long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                    int rows = countRows ("SAFARI%a");
                    s.close ();
                    assertCondition ((updateCounts.length == 5)
                                     && (updateCounts[0] == 1)
                                     && (updateCounts[1] == 1)
                                     && (updateCounts[2] == 1)
                                     && (updateCounts[3] == 1)
                                     && (updateCounts[4] == 1)
                                     && (rows == 5), sb.toString());
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception "+sb.toString());
                }
            }
        }


        /**
        addBatch()/executeLargeBatch() - Execute the batch of DELETE STATEMENTS v7r1
        **/
            public void Var088 ()
            {
                if (checkJdbc42 ()) {
		StringBuffer sb = new StringBuffer();
		sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

                    try {
                        Statement s = connection_.createStatement ();
                        s.execute("DELETE FROM " + table_);
                        
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI1'");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI2'");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI3'");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI4'");
                        s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI5'");
                        JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI1'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI2'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI3'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI4'");
                        s.addBatch ("DELETE FROM " + table_ + " where NAME = 'SAFARI5'");
                        long[] updateCounts = (long[]) JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
                        int rows = countRows ("SAFARI%");
                        s.close ();
                        assertCondition ((updateCounts.length == 5)
                                         && (updateCounts[0] == 1)
                                         && (updateCounts[1] == 1)
                                         && (updateCounts[2] == 1)
                                         && (updateCounts[3] == 1)
                                         && (updateCounts[4] == 1)
                                         && (rows == 0), sb.toString());
                    }
                    catch (Exception e) {
                        failed (e, "Unexpected Exception "+sb.toString());
                    }
                }
            }

            /**
            addBatch()/executeLargeBatch() - Execute the batch of MERGE STATEMENTS v7r1
            **/

	    public void Var089 () 		{
		if (checkJdbc42 ()) {
		    StringBuffer sb = new StringBuffer();
		    sb.append("-- added 9/23/2013 to test values with no parenthesis"); 

		    try {
			Statement s = connection_.createStatement ();
			s.execute("DELETE FROM " + table_);
			s.execute("DELETE FROM " + table2_);

			s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI1'");
			s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES 'SAFARI2'");
			s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES 'SAFARI1'");
			s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES 'SAFARI2'");
			s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES 'SAFARI3'");
			JDReflectionUtil.callMethod_O(s,"executeLargeBatch");

			s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
				   " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf a'"); //tab updated
			s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')"); //insert new SAFARI1 after updated above
			s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
				   " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf b'"); //1 updated from new row inserted above
			s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
				   " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf c'"); // no matches
			s.addBatch("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
				   " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = 'saf d'"); //no matches

			long[] updateCounts = (long[])  JDReflectionUtil.callMethod_O(s,"executeLargeBatch");
			int rows = countRows ("saf a%"); //sb 2 from first merge 
			int rows2 = countRows ("saf b%"); //sb 1 from second merge 
			s.close ();
			assertCondition ((updateCounts.length == 5)
					 && (updateCounts[0] == 2)
					 && (updateCounts[1] == 1)
					 && (updateCounts[2] == 1)
					 && (updateCounts[3] == 0)
					 && (updateCounts[4] == 0)
					 && (rows == 2)
					 && (rows2 == 1),
					 "updateCounts.length="+updateCounts.length+" sb  5\n"+
					 "updateCounts[0]="+updateCounts[0]+" sb 2\n"+
					 "updateCounts[1]="+updateCounts[1]+" sb 1\n" +
					 "updateCounts[2]="+updateCounts[2]+" sb 1\n" +
					     "updateCounts[3]="+updateCounts[3]+" sb 0\n" +
					     "updateCounts[4]="+updateCounts[4]+" sb 0\n" +
					     "rows="+rows+" sb  2\n" +
					     "rows2="+rows2+" sb 1"+ sb.toString()); 
			}
			catch (Exception e) {
			    failed (e, "Unexpected Exception"+sb.toString());
			}
		    }
		}













}



