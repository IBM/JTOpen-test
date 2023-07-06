///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionCommit.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDConnectionCommit.java
 //
 // Classes:      JDConnectionCommit
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;



/**
Testcase JDConnectionCommit.  This tests the following methods
of the JDBC Connection class:

<ul>
<li>commit()
<li>rollback()
<li>setAutoCommit()
<li>getAutoCommit()
</ul>
**/
public class JDConnectionCommit
extends JDTestcase
{



    // Private data.
    private static  String         table_      = JDConnectionTest.COLLECTION + ".JDCCOMMIT";
    private              Connection     connection_;
    private              boolean        remoteConnection_ = false; 



/**
Constructor.
**/
    public JDConnectionCommit (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDConnectionCommit",
            namesAndVars, runMode, fileOutputStream,
            password);
    }



/**
Setup.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
        table_      = JDConnectionTest.COLLECTION + ".JDCCOMMIT";
        // Initialize the connection.
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        connection_.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
        connection_.setAutoCommit (false);

        // Create the table.
        Statement s = connection_.createStatement ();
        s.executeUpdate ("CREATE TABLE " + table_ + " (NAME VARCHAR(10))");
        s.close ();
        connection_.commit ();

	remoteConnection_ = isRemoteConnection(connection_); 


    }



/**
Cleanup.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        // Drop the table.
        Statement s = connection_.createStatement ();
        s.executeUpdate ("DROP TABLE " + table_);
        s.close ();
        connection_.commit ();

        // Close the connection.
        connection_.close ();
    }



/**
Checks that the result set contains a row.

@param name     The name.
@return         true if the result set contains the row, false otherwise.
**/
    private boolean checkForRow (String name)
        throws SQLException
    {
        Statement s = connection_.createStatement ();
        ResultSet rs = s.executeQuery ("SELECT * FROM " + table_);
        boolean found = false;
        while (rs.next ()) {
            if (rs.getString ("NAME").equals (name))
                found = true;
        }
        rs.close ();
        s.close ();
        return found;
    }



/**
setAutoCommit()/getAutoCommit() - Verify that autocommit is turned on by default.
**/
  public void Var001()
  {
        try {
            // Create a connection with default autocommit.
            Connection c2 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            boolean autoCommit = c2.getAutoCommit ();
            Statement s2 = c2.createStatement ();
            s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('default')");

            c2.commit(); // for xa testing
            // Verify changes were committed.
            boolean found = checkForRow ("default");

            // Close the connection.
            c2.close ();

            assertCondition (found && autoCommit, "found = "+found+" autoCommit="+autoCommit);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
  }



/**
setAutoCommit()/getAutoCommit() - Turn on autocommit.  Insert a value and then
verify it was committed.
**/
    public void Var002()
    {
        try {
            // Create a connection with autocommit on.
            Connection c2 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c2.setAutoCommit (true);
            boolean autoCommit = c2.getAutoCommit ();
            Statement s2 = c2.createStatement ();
            s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('enabled')");

            // Verify changes were committed.
            boolean found = checkForRow ("enabled");

            // Close the connection.
            c2.close ();

            assertCondition (found && autoCommit);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
setAutoCommit()/getAutoCommit() - Turn off autocommit.  Insert a value and
then verify it was not committed.
**/
    public void Var003()
    {
        try {
            // Create a connection with autocommit off.
            Connection c2 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c2.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            c2.setAutoCommit (false);
            boolean autoCommit = c2.getAutoCommit ();
            Statement s2 = c2.createStatement ();
            s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('disabled')");
            c2.rollback ();

            // Verify changes were committed.
            boolean found = checkForRow ("disabled");

            // Close the connection.
            c2.close ();

            assertCondition (!found  && !autoCommit);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
setAutoCommit() - Verify that setting autocommit on a closed
connection throws an exception.
**/
    public void Var004()
    {
        try {
            Connection c2 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c2.close ();
            c2.setAutoCommit (false);
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getAutoCommit() - Verify that getting autocommit on a closed
connection throws an exception.
**/
    public void Var005()
    {
        try {
            Connection c2 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c2.close ();
            c2.getAutoCommit ();
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
rollback() - Insert twice into a table and then call rollback().
Verify that table exists but previous inserts were rolled back.
**/
    public void Var006()
    {
        try {
            // Perform 2 inserts that we can rollback.
            Statement s = connection_.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('ibm')");
            s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('bigblue')");

            // Verify changes occured before rolling back.
            boolean found1Before = checkForRow ("ibm");
            boolean found2Before = checkForRow ("bigblue");

            // Perform a rollback and verify that insertions have
            // been erased.
            connection_.rollback ();
            boolean found1After = checkForRow ("ibm");
            boolean found2After = checkForRow ("bigblue");

            assertCondition (found1Before && found2Before && !found1After && !found2After,
			     "\n"+
			     "found1Before="+found1Before+" sb true\n"+
			     "found2Before="+found2Before+" sb true\n"+
			     "found1After ="+found1After+" sb false\n"+
			     "found2After ="+found2After+" sb false");
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
commit(), rollback() - Insert into table, call commit().
Insert into table again, call rollback().  Verify that only committed
changes are reflected.
**/
    public void Var007()
    {
        try {
            // Insert into table and commit.
            Statement s = connection_.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('commit')");
            connection_.commit ();

            // Insert again and but don't commit.
            s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('rollback')");

            // Verify changes occured before rolling back.
            boolean found1Before = checkForRow ("commit");
            boolean found2Before = checkForRow ("rollback");

            // Perform a rollback and verify that only one insertion
            // remains.
            connection_.rollback ();
            boolean found1After = checkForRow ("commit");
            boolean found2After = checkForRow ("rollback");

            assertCondition (found1Before && found2Before && found1After && !found2After,
			     "found1Before="+found1Before+" sb true\n"+
			     "found2Before="+found2Before+" sb true\n"+
			     "found1After ="+found1After+" sb true\n"+
			     "found2After ="+found2After+" sb false");

        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
commit(), rollback() - Verify that when a connection is closed
uncommited transactions are rolled back.
**/
    public void Var008()
    {
        try {
            // Turn off auto-commit.
            Connection c2 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c2.setAutoCommit (false);
            c2.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            // Insert a row, but don't commit.
            Statement s = c2.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('josh')");

            // Close the connection.
            c2.close ();

            // Verify changes are rolled back when the connection
            // was closed.
            boolean foundAfter = checkForRow ("josh");

            assertCondition (!foundAfter, " boolean foundAfter = " + foundAfter +" AND SHOULD BE false" );
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
commit()/setAutocommit() - Verify that commit has no effect when autocommit is on.
**/
    public void Var009()
    {
        try {
            // Turn on auto-commit.
            Connection c2 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c2.setAutoCommit (true);

            // Insert a row and commit.
            Statement s2 = connection_.createStatement();
            s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('Hola')");
            c2.commit();

            // Verify that the row is there.
            boolean foundAfter = checkForRow ("Hola");

            assertCondition (foundAfter);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }




/**
rollback()/setAutocommit( - Verify that rollback has no effect when autocommit is on.
**/
    public void Var010()
    {
        try {
            // Turn on auto-commit.
            Connection c2 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c2.setAutoCommit (true);

            // Insert a row and commit.
            Statement s2 = connection_.createStatement();
            s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('Adios')");
            c2.rollback();

            // Verify that the row is there.
            boolean foundAfter = checkForRow ("Adios");

            assertCondition (foundAfter);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
commit() - Verify that committing a closed connection throws an exception.
**/
    public void Var011()
    {
        try {
            Connection c2 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c2.close ();
            c2.commit ();
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
rollback() - Verify that rolling back a closed connection throws an exception.
**/
    public void Var012()
    {
        try {
            Connection c2 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c2.close ();
            c2.rollback ();
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
rollback() - Insert twice into a table using a batch update and then 
call rollback().  Verify that table exists but previous inserts 
were rolled back.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            try {
                // Perform 2 inserts that we can rollback.
                Statement s = connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + " (NAME) VALUES('Helium')");
                s.addBatch ("INSERT INTO " + table_ + " (NAME) VALUES('Oxygen')");
                s.executeBatch ();
    
                // Verify changes occured before rolling back.
                boolean found1Before = checkForRow ("Helium");
                boolean found2Before = checkForRow ("Oxygen");
    
                // Perform a rollback and verify that insertions have
                // been erased.
                connection_.rollback ();
                boolean found1After = checkForRow ("Helium");
                boolean found2After = checkForRow ("Oxygen");
    
                assertCondition (found1Before && found2Before && !found1After && !found2After);
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
rollback() - Insert twice into a table using a result set insert and then 
call rollback().  Verify that table exists but previous inserts 
were rolled back.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            try {
                // Perform 2 inserts that we can rollback.
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                           ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery ("SELECT * FROM " + table_ + " FOR UPDATE");
                rs.moveToInsertRow ();
                rs.updateString ("NAME", "Hydrogen");
                rs.insertRow ();
                rs.updateString ("NAME", "Nitrogen");
                rs.insertRow ();
                rs.close ();
    
                // Verify changes occured before rolling back.
                boolean found1Before = checkForRow ("Hydrogen");
                boolean found2Before = checkForRow ("Nitrogen");
    
                // Perform a rollback and verify that insertions have
                // been erased.
                connection_.rollback ();
                boolean found1After = checkForRow ("Hydrogen");
                boolean found2After = checkForRow ("Nitrogen");

                // SQL400 - We are really covering up an issue here.  When we 
                //          have a result set, we do not close the cursor until
                //          the statement gets closed (unless the user specifies
                //          the property CURSORHOLD=false on the connection).  We
                //          really should close the cursors today.
                if (getDriver() == JDTestDriver.DRIVER_NATIVE)
                   s.close(); // Clean up the statement so that the cursor goes away.
    
                assertCondition (found1Before && found2Before && !found1After && !found2After,
				 "\bfound1Before="+found1Before+" sb true"+
				 "\bfound2Before="+found2Before+" sb true"+
				 "\bfound1After ="+found1After+" sb false" +
				 "\bfound2After ="+found2After+" sb false");
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

    /**
     * commit() - should throw an exception if 'autocommit exception' is true and autocommit is on.
    **/
    public void Var015(){
	if (! (isToolboxDriver())) {
	    notApplicable("TOOLBOX TEST for autocommit exception=true"); 
	} else {
	    try { 
		boolean autocommitExceptionPropertyFound = false; 
		Properties properties = new Properties();
		properties.put("autocommit exception", "true"); 
		Driver driver = DriverManager.getDriver(baseURL_);
		DriverPropertyInfo[] propertyInfo = driver.getPropertyInfo(baseURL_, properties);
		for (int i = 0; i < propertyInfo.length; i++) {
		    if (propertyInfo[i].name.equals("autocommit exception")) {
			autocommitExceptionPropertyFound = true; 
		    }
		} 

		if (!autocommitExceptionPropertyFound) {
		    notApplicable("'autocommit exception' property not found from getPropertyInfo");
		} else { 
		    try {
			Connection c = testDriver_.getConnection (baseURL_ + ";autocommit exception=true", userId_, encryptedPassword_);
			c.commit ();
			failed ("Did not throw exception.  Added by Toolbox 8/23/2007.");
		    }
		    catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Added by Toolbox 8/23/2007.");
		    }
		}
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    } 
	}
    }

    /**
     * rollback() - should throw an exception if 'autocommit exception' is true and autocommit is on.
    **/
    public void Var016(){
	if (! (isToolboxDriver())) {
	    notApplicable("TOOLBOX TEST for autocommit exception=true"); 
	} else {
	    try { 
		boolean autocommitExceptionPropertyFound = false; 
		Properties properties = new Properties();
		properties.put("autocommit exception", "true"); 
		Driver driver = DriverManager.getDriver(baseURL_);
		DriverPropertyInfo[] propertyInfo = driver.getPropertyInfo(baseURL_, properties);
		for (int i = 0; i < propertyInfo.length; i++) {
		    if (propertyInfo[i].name.equals("autocommit exception")) {
			autocommitExceptionPropertyFound = true; 
		    }
		} 

		if (!autocommitExceptionPropertyFound) {
		    notApplicable("'autocommit exception' property not found from getPropertyInfo");
		} else { 
		    try {
			Connection c = testDriver_.getConnection (baseURL_ + ";autocommit exception=true", userId_, encryptedPassword_);
			c.rollback ();
			failed ("Did not throw exception.  Added by Toolbox 2008-03-11.");
		    }
		    catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Added by Toolbox 2008-03-11.");
		    }
		}
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    } 
	}
    }


    /**
     
      setAutoCommit()/getAutoCommit() - Verify that autocommit is turned off by default if property is set.
    **/
      public void Var017()
      {
            String added = " -- Added by toolbox 3/31/2008"; 
            try {
                // Create a connection with default autocommit.
                Connection c2;
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    // For native driver must specify isolation level if turning off auto commit
		    c2 = testDriver_.getConnection (baseURL_ + ";auto commit=false;transaction isolation=read uncommitted", userId_, encryptedPassword_);
		} else { 
		    c2 = testDriver_.getConnection (baseURL_ + ";auto commit=false", userId_, encryptedPassword_);
		}
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    c2.setAutoCommit(false);
		    c2.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED); 
		}
                boolean autoCommit = c2.getAutoCommit ();
                Statement s2 = c2.createStatement ();
                s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('def17')");

                c2.commit(); // for xa testing
                // Verify changes were committed.
                boolean found = checkForRow ("def17");

                // Close the connection.
                c2.close ();

                assertCondition (found && !autoCommit, "found="+found+" sb true autoCommit="+autoCommit+" sb false"+added);
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception"+added);
            }
      }




    /**
    setAutoCommit()/getAutoCommit() - Turn off autocommit with property.  Insert a value and
    then verify it was not committed.
    **/
        public void Var018()
        {
            String added = " -- Added by toolbox 3/31/2008"; 
            try {
                // Create a connection with autocommit off.
		Connection c2; 
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    // For native driver must specify isolation level if turning off auto commit
		    c2 = testDriver_.getConnection (baseURL_ + ";auto commit=false;transaction isolation=read uncommitted", userId_, encryptedPassword_);
		} else { 
		    c2 = testDriver_.getConnection (baseURL_ + ";auto commit=false", userId_, encryptedPassword_);
		}
                c2.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    c2.setAutoCommit(false);
		}

                boolean autoCommit = c2.getAutoCommit ();
                Statement s2 = c2.createStatement ();
                s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('disab18')");
                c2.rollback ();

                // Verify changes were committed.
                boolean found = checkForRow ("disab18");

                // Close the connection.
                c2.close ();

                assertCondition (!found  && !autoCommit, "found="+found+" sb false autoCommit="+autoCommit+" sb false "+added );
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception"+added);
            }
        }

 

     

    /**
    commit(), rollback() - Verify that when a connection is closed
    uncommited transactions are rolled back. (using auto commit property)
    **/
        public void Var019()
        {
            String added = " -- Added by toolbox 3/31/2008"; 
            try {
                // Turn off auto-commit.
		Connection c2; 
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    // For native driver must specify isolation level if turning off auto commit
		    c2 = testDriver_.getConnection (baseURL_ + ";auto commit=false;transaction isolation=read uncommitted", userId_, encryptedPassword_);
		} else { 

		    c2 = testDriver_.getConnection (baseURL_+ ";auto commit=false", userId_, encryptedPassword_);
		}
                c2.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

                // Insert a row, but don't commit.
                Statement s = c2.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('josh19')");

                // Close the connection.
                c2.close ();

                // Verify changes are rolled back when the connection
                // was closed.
                boolean foundAfter = checkForRow ("josh19");

                assertCondition (!foundAfter, " boolean foundAfter = " + foundAfter +" AND SHOULD BE false" +added );
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception"+added);
            }
        }


    /**
    commit() -- Verify that commit does not take significantly longer if
    a larger number of statements are opened.  This is to detect the bug
    in the native JDBC driver that was always setting rs.closed without
    checking that it was already closed.  By making the simple change,
    the native driver performance improved.  Here are the results seen on
    lp18ut12 (V6R1) on 5/20/008.

Before..

For 500 statements 1000 commits took 97 ms  avg = 0.097
For 1000 statements 1000 commits took 122 ms  avg = 0.122
For 1500 statements 1000 commits took 227 ms  avg = 0.227
For 2000 statements 1000 commits took 416 ms  avg = 0.416
For 2500 statements 1000 commits took 599 ms  avg = 0.599
For 3000 statements 1000 commits took 766 ms  avg = 0.766
For 3500 statements 1000 commits took 925 ms  avg = 0.925
For 4000 statements 1000 commits took 1071 ms  avg = 1.071
For 4500 statements 1000 commits took 1216 ms  avg = 1.216
For 5000 statements 1000 commits took 1429 ms  avg = 1.429
For 5500 statements 1000 commits took 1503 ms  avg = 1.503

After


Usage: java CloseCursorCommit maxStatements  statementStep  commitLoops
For 500 statements 1000 commits took 27 ms  avg = 0.027
For 1000 statements 1000 commits took 22 ms  avg = 0.022
For 1500 statements 1000 commits took 22 ms  avg = 0.022
For 2000 statements 1000 commits took 22 ms  avg = 0.022
For 2500 statements 1000 commits took 23 ms  avg = 0.023
For 3000 statements 1000 commits took 22 ms  avg = 0.022
For 3500 statements 1000 commits took 22 ms  avg = 0.022
For 4000 statements 1000 commits took 22 ms  avg = 0.022
For 4500 statements 1000 commits took 22 ms  avg = 0.022
For 5000 statements 1000 commits took 22 ms  avg = 0.022
For 5500 statements 1000 commits took 22 ms  avg = 0.022

    **/
        public void Var020()
        {

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() <= JDTestDriver.RELEASE_V5R3M0) {
		notApplicable("Fix does not improve performance on V5R3");
		return; 
	    }  
	    if (isToolboxDriver())  {
	        notApplicable("N/A on Toolbox");
	        return; 
	    }
	    if (remoteConnection_) {
		notApplicable("Remote connection limited to 500 statements"); 
	    } 

	    boolean success = true; 
	    StringBuffer stringBuffer = new StringBuffer(); 
            String added = " -- Added by native 5/20/2008";

            try {

		// Run a maximum of 10 minutes 
		long endTime = System.currentTimeMillis() + 600000; 
		int maxStatements = 6001; 
		int statementStep = 1000; 
		int commitLoops   = 1000;
		int statementCount = statementStep; 
		Connection conn = null;
		double first = -1.0; 

		while (success && statementCount < maxStatements && System.currentTimeMillis() < endTime ) { 

 	            /* Force garbage collection, so numbers are not thrown off */
		    System.out.println("statementCount="+statementCount); 
		    System.gc(); 

		    conn =    testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
		    conn.setTransactionIsolation( Connection.TRANSACTION_SERIALIZABLE);
		    conn.setAutoCommit(false); 
		    Statement[] stmt = new Statement[statementCount];
		    for (int i = 0;  i < statementCount &&  System.currentTimeMillis() < endTime ; i++) {
			if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
			    stmt[i] = conn.createStatement(); 
			} else { 
			    stmt[i] = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
							   ResultSet.CONCUR_READ_ONLY,
							   ResultSet.CLOSE_CURSORS_AT_COMMIT);
			}
			ResultSet rs = stmt[i].executeQuery("Select * from sysibm.sysdummy1");
			rs.close(); 
		    }
		    conn.commit(); 
		    long startTime = System.currentTimeMillis(); 
		    for (int i = 0 ; i < commitLoops &&  System.currentTimeMillis() < endTime ; i++) {
			conn.commit(); 
		    } 
		    endTime = System.currentTimeMillis();
		    long commitTime = endTime - startTime;
		    double average = ((double) commitTime) / commitLoops;

		    stringBuffer.append("For "+statementCount+" statements "+commitLoops+" commits took "+commitTime+" ms  avg = "+ average+"\n"); 
		    if (first == -1.0) {
			first = average;
		    } else {
			if (average > first * 4.0) {
			    stringBuffer.append("Failed:  avg of "+average+" > "+first+" * 4.0\n");
			    success = false;
			}
		    } 

		    
		    conn.close(); 
		
		    statementCount += statementStep; 
		}
 
		
                assertCondition (success, stringBuffer.toString() + added); 
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception"+added);
            }
	    catch(java.lang.NoSuchMethodError nsme) {
		    failed("Unexpected Exception");
		    nsme.printStackTrace(); 
	    } 
        }
        /**
        commit(), rollback() - Verify that when isolationlevel is *none, that commit and rollback do
        not close cursors.  DB issue 38976.
         **/
        public void Var021()
        {

	    if ( 
		getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		notApplicable("*NONE CURSOR PROBLEM fixed by native/tb in V7R1");
		return; 
	    }

            String added = " commit should not close *NONE cursors -- Added by toolbox (needs v7r1 host support) 12/08/2008"; 
            try {
                // Turn off auto-commit.
                Connection c2; 
                c2 = testDriver_.getConnection (baseURL_+ ";cursor hold=false;auto commit=false;transaction isolation=none", userId_, encryptedPassword_);

		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    // Default is *NONE
		} else {
		    c2.setTransactionIsolation(Connection.TRANSACTION_NONE);
		}

                // Insert a row
                Statement s = c2.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('josh21')");
                c2.commit();
                
                //open cursor
              int i=  c2.getTransactionIsolation();
                s = c2.createStatement ();
                ResultSet rs = s.executeQuery("SELECT * from " + table_ + " where name = 'josh21'");

                // commit the connection.
                c2.commit();

                // Verify RS still open
                rs.next();
		String getStringValue = rs.getString(1);
                rs.close();
                c2.close();

                assertCondition(getStringValue.equals("josh21"), "output string '"+getStringValue+"'not 'josh21' isolation="+i);    
             
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception"+added);
            }
        }
        /**
        commit(), rollback() - Verify that when isolationlevel is *none, that commit and rollback do
        not close cursors.  DB issue 38976.
         **/
        public void Var022()
        {

	    if ( 
		getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		notApplicable("*NONE CURSOR PROBLEM fixed by native/tb in V7R1"); 
		return; 
	    }

            String added = " -- Added by toolbox (needs v7r1 host support) 12/08/2008"; 
            try {
                // Turn off auto-commit.
                Connection c2; 
                c2 = testDriver_.getConnection (baseURL_+ ";cursor hold=false;auto commit=false;transaction isolation=none", userId_, encryptedPassword_);

                c2.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); //override in sql
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    c2.setAutoCommit(false); 
		}

                // Insert a row
                Statement s = c2.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('josh22')");
                c2.commit();
                
                //open cursor
                s = c2.createStatement ();
                ResultSet rs = s.executeQuery("SELECT * from " + table_ + " where name = 'josh22' for update with NC ");

                // commit the connection.
                c2.commit();

                // Verify RS still open
                rs.next();
		String getStringValue = rs.getString(1);
                rs.close();
                c2.close();
                assertCondition(getStringValue.equals("josh22"));    
             
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception"+added);
            }
        }

        /**
         rollback() - Verify that when isolationlevel is *none, that commit and rollback do
        not close cursors.  DB issue 38976.
         **/
        public void Var023()
        {

	    if ( 
		getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		notApplicable("*NONE CURSOR PROBLEM fixed by native/tb in V7R1"); 
		return; 
	    }

            String added = " -- Added by toolbox (needs v7r1 host support) 12/08/2008"; 
            try {
                // Turn off auto-commit.
                Connection c2; 
                c2 = testDriver_.getConnection (baseURL_+ ";cursor hold=false;auto commit=false;transaction isolation=none", userId_, encryptedPassword_);

                c2.setTransactionIsolation(Connection.TRANSACTION_NONE);

                // Insert a row
                Statement s = c2.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('josh23')");
                c2.commit();
                
                //open cursor
                s = c2.createStatement ();
                ResultSet rs = s.executeQuery("SELECT * from " + table_ + " where name = 'josh23'");

                // rollback the connection.
                c2.rollback();

                // Verify RS still open
                rs.next();
                assertCondition(rs.getString(1).equals("josh23"));    
                rs.close();
                c2.close();
             
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception"+added);
            }
        }
        /**
        rollback() - Verify that when isolationlevel is *none, that commit and rollback do
        not close cursors.  DB issue 38976.
         **/
        public void Var024()
        {

	    if (
		getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		notApplicable("*NONE CURSOR PROBLEM fixed by native/tb in V7R1"); 
		return; 
	    }

            String added = " -- Added by toolbox (needs v7r1 host support) 12/08/2008"; 
            try {
                // Turn off auto-commit.
                Connection c2; 
                c2 = testDriver_.getConnection (baseURL_+ ";cursor hold=false;auto commit=false;transaction isolation=none", userId_, encryptedPassword_);

                c2.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); //override in sql

                // Insert a row
                Statement s = c2.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('josh24')");
                c2.commit();
                
                //open cursor
                s = c2.createStatement ();
                ResultSet rs = s.executeQuery("SELECT * from " + table_ + " where name = 'josh24' for update with NC ");

                // rollback the connection.
                c2.rollback();

                // Verify RS still open
                rs.next();
                assertCondition(rs.getString(1).equals("josh24"));    
                rs.close();
                c2.close();
             
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception"+added);
            }
        }


	//
	// Test using getMoreResults after calling commit.
	// This recreates a problem in CPS 896M2P

        public void Var025()
        {


            String added = " -- Added by native 2/11/2011 to test CPS 896M2P"; 
            try {

                Connection c2; 
                c2 = testDriver_.getConnection (baseURL_+ ";cursor hold=false;auto commit=true;sensitivity=asensitive", userId_, encryptedPassword_);

		String tableName = JDConnectionTest.COLLECTION + ".JDCONCOM25";
		Statement s;
		s = c2.createStatement(); 
		try {
		    s.executeUpdate("DROP TABLE "+tableName); 
		} catch (Exception e) {
		}

		s.executeUpdate("CREATE TABLE "+tableName+"( C1 INT)");
		c2.commit();
		for (int i = 1; i <= 20; i++) {
		    s.executeUpdate("insert into "+tableName+" values("+i+")");
		    c2.commit(); 
		} 

		PreparedStatement ps = c2.prepareStatement("select * from sysibm.sysdummy1 a,  sysibm.sysdummy1 b",
							   ResultSet.TYPE_FORWARD_ONLY,
							   ResultSet.CONCUR_READ_ONLY ,
							   ResultSet.CLOSE_CURSORS_AT_COMMIT ); 

		ResultSet rs = ps.executeQuery();
		rs.next();
		rs.next();
		c2.commit();
		rs.close();
		ps.getMoreResults();
		c2.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		c2.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		rs = ps.executeQuery();

                s.executeUpdate("DROP TABLE "+tableName); 
                c2.commit(); 
		c2.close();

		assertCondition(true); 
             
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception"+added);
            }
        }


        
        Statement[] var26statements = null; 
 	/*
   * rollback -- rollback connection while another thread is closing statements. 
   *  Attempt to recreate problem in CPS 8Q9LML
   */

        public void Var026()
        {

	    
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getDriverFixLevel() <= 46209 ) {
		notApplicable("TODO:  Testcases hangs for native JDBC driver -- Fix in PTF after SI46209 -- see issue 47682");
		return; 
	    }
	    
	    System.out.println("driverFixLevel="+getDriverFixLevel());

            String added = " -- Added by toolbox 1/09/2011 to test CPS 8Q9LML";
            Var26Cleaner cleanerThread = null; 
            try {

                Connection c2; 
                c2 = testDriver_.getConnection (baseURL_+ ";transaction isolation=read uncommitted;auto commit=false", userId_, encryptedPassword_);

                c2.setAutoCommit(false);
                
                cleanerThread = new Var26Cleaner(c2); 
                cleanerThread.start(); 

                int seconds = 60; 
                System.out.println("Starting "+seconds+" second test closer thread "); 
                long endTime = System.currentTimeMillis() + seconds * 1000;
                int statementCount = 1000; 
                var26statements = new Statement[statementCount];
                while (System.currentTimeMillis() < endTime) { 
                  for (int i = 0; i < statementCount; i++) {
                      var26statements[i] = c2.createStatement(); 
                      ResultSet rs = var26statements[i].executeQuery("Select * from sysibm.sysdummy1");
                      rs.next();
                  }
                  cleanerThread.setCleanup(true);
                  System.out.println("Calling rollback"); 
                  c2.rollback();
                  System.out.println("Rollback completed"); 
                  cleanerThread.setCleanup(false); 

                }
                
                assertCondition(true); 
             
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception"+added);
            } finally {
              if (cleanerThread != null) { 
                  cleanerThread.setComplete(); 
              }
            }
        }

        class Var26Cleaner extends Thread {
          boolean complete = false;
          boolean cleanup = false;
          Connection c2; 
          Var26Cleaner(Connection c2) {
            this.c2 = c2; 
          }
          public synchronized void setComplete() {
            complete =true;
            notifyAll(); 
          }
          public synchronized void setCleanup(boolean value) {
            cleanup =  value; 
            notifyAll(); 
          }
   public void run() {
      synchronized (this) {
        String jobName = ""; 
        if (c2 instanceof com.ibm.as400.access.AS400JDBCConnection) {
          jobName = ((com.ibm.as400.access.AS400JDBCConnection)c2).getServerJobIdentifier(); 
        }
        setName("Var26Cleaner"+jobName);
        while (!complete) {
          while (cleanup) {
            System.out.println("Cleanup thread running"); 
            for (int i = 0; cleanup && i < var26statements.length; i++) {
              try {
                var26statements[i].close();
              } catch (Exception e) {

              }
            } /*int i */
            cleanup = false; 
            System.out.println("Cleanup thread complete"); 
          } /* while cleanup */
          try {
            wait();
          } catch (InterruptedException ex) {
            ex.printStackTrace();
            complete = true;
          }
        }
      }
    }
  }


}



