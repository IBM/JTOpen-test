///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionCreateStatement.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionCreateStatement.java
//
// Classes:      JDConnectionCreateStatement
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JDConnectionCreateStatement.  This tests the following methods
of the JDBC Connection class:

<ul>
<li>createStatement()
<li>prepareStatement()
<li>prepareCall()
</ul>
**/
public class JDConnectionCreateStatement
extends JDTestcase {



   // Private data.
   private              Connection     connection_;
   private              Connection     closedConnection_;
   private static String table_ = JDConnectionTest.COLLECTION + ".JDCCS";

   // If the "user" application doesn't keep a reference to the statements 
   // it creates, the Native JDBC driver will start cleaning them up for 
   // the user automatically, and we will not be able to test bounds conditions.
   private              Vector         statements_;



/**
Constructor.
**/
   public JDConnectionCreateStatement (AS400 systemObject,
                                       Hashtable namesAndVars,
                                       int runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String password)
   {
      super (systemObject, "JDConnectionCreateStatement",
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
      table_ = JDConnectionTest.COLLECTION + ".JDCCS";
      connection_ = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);
      connection_.clearWarnings ();

      closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
      closedConnection_.close ();


      Statement s = connection_.createStatement ();
      try { s.executeUpdate ("DROP TABLE " + table_); } catch (Throwable t) {}
      s.executeUpdate ("CREATE TABLE " + table_ + " (COL1 VARCHAR(15))");
      s.executeUpdate ("INSERT INTO " + table_ + " (COL1) VALUES ('Initial')");
      s.close ();

         JDSetupProcedure.create (systemObject_, connection_,
                                  JDSetupProcedure.STP_CSPARMS,
                                  supportedFeatures_,
				  collection_);
      
      connection_.commit(); // for xa testing


      statements_ = new Vector();
   }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      Statement s = connection_.createStatement ();
      s.executeUpdate ("DROP TABLE " + table_);
      s.close ();

      connection_.commit(); // for xa testing
      connection_.close ();
   }



/**
Checks whether a result set is scrollable.
**/
   private boolean checkScrollable (ResultSet rs)
   {
      try {
         rs.last ();
         rs.previous ();
      } catch (SQLException e) {
         return false;
      }
      return true;
   }



/**
Checks whether a result set is updatable.
**/
   private boolean checkUpdatable (ResultSet rs)
   {
      try {
         rs.next ();
         rs.updateString (1, "Hello");
      } catch (SQLException e) {
         return false;
      }
      return true;
   }



/**
Allocates statements until the connection maximum has been
reached, and returns the number of statements allocated.

@param c    The connection.
@return     The number of statements.
**/
   private int fillConnection (Connection c)
   {
      int numberOfStatements = 0;

      try {
         // Just get more than we used to allow...
         // if we get there we are doing pretty good,
         // but to actually get about 40,000 statements takes too
         // long to be doing all the time.
         for (int i = 0; i < 1005; ++i) {

            // Allocate a different kind of statement
            // each time, just for fun.
            switch (i % 3) {
            case 0:
               statements_.addElement( c.createStatement () );
               break;
            case 1:
               statements_.addElement( c.prepareStatement ("SELECT * FROM QSYS2.SYSTABLES") );
               break;
            case 2:
               statements_.addElement( c.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)") );
               break;
            }

            ++numberOfStatements;
         }
      } catch (SQLException e) {
         // Ignore.  This will happen when the connection is full.
      }

      return numberOfStatements;
   }

/**
@D1A
Checks to see if the 1000th open statement SQLWarning has been
posted for this connection

@param c    The connection.
@return     The number of times the warning has been posted.
**/
   private int checkWarnings (Connection c) throws SQLException
   {
       int sqlWarningCount = 0;
       SQLWarning w = c.getWarnings();
       if(w!=null)
       {
           while(w!=null)
           {
               if(w.getSQLState().equals("01G00") && w.getMessage().equals("1000 open statements on this connection."))
               {
                   sqlWarningCount++;
               }
               w = w.getNextWarning();
            }
       }
       return sqlWarningCount;
   }




/**
createStatement() with 0 parameters - Create a statement in a
connection.  Verify the result set type and concurrency in the
statement.
**/
   public void Var001()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            int resultSetType = s.getResultSetType ();
            int resultSetConcurrency = s.getResultSetConcurrency ();
            s.close ();
            assertCondition ((resultSetType == ResultSet.TYPE_FORWARD_ONLY)
                    && (resultSetConcurrency == ResultSet.CONCUR_READ_ONLY));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
createStatement() - Create a statement in a closed connection.
Should throw an exception.
**/
   public void Var002()
   {
      try {
         Statement s = closedConnection_.createStatement ();
         failed ("Did not throw exception. s="+s);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
createStatement() with 2 parameters - Create a statement in a
connection.  Verify the result set type and concurrency in the
statement.
**/
   public void Var003()
   {
      if (checkJdbc20 ()) {
         try {
	     boolean passed = true;
	     StringBuffer sb = new StringBuffer(); 
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                       ResultSet.CONCUR_UPDATABLE);
            int resultSetType = s.getResultSetType ();
            int resultSetConcurrency = s.getResultSetConcurrency ();
            SQLWarning w = connection_.getWarnings ();
            s.close ();
	    if (resultSetType != ResultSet.TYPE_SCROLL_SENSITIVE) {
		sb.append("resultSetType is "+resultSetType+" sb TYPE_SCROLL_SENSITIVE\n");
		passed = false; 
	    }
	    if (resultSetConcurrency != ResultSet.CONCUR_UPDATABLE) {
		sb.append("resultSetConcurrency is "+resultSetConcurrency+" sb CONCUR_UPDATABLE\n");
		passed = false;
	    }
	    if (w != null) {
		sb.append("unexpected warning "+w+"\n");
		passed=false; 
	    }
            assertCondition (passed,sb);
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
createStatement() with 2 parameters - Create a statement with
a bogus result set type.
**/
   public void Var004()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement (4378,
                                                       ResultSet.CONCUR_UPDATABLE);
            failed ("Did not throw exception. s="+s);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
createStatement() with 2 parameters - Create a statement with
a bogus result set concurrency.
**/
   public void Var005()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       1027);
            failed ("Did not throw exception."+s);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
createStatement() with 2 parameters - Create a statement in a
closed connection.  Should throw an exception.
**/
   public void Var006()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = closedConnection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                             ResultSet.CONCUR_UPDATABLE);
            failed ("Did not throw exception."+s);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
createStatement() with 2 parameters - Create a statement as forward-only
and read-only.
**/
   public void Var007()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            Statement s = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                       ResultSet.CONCUR_READ_ONLY);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table_);
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_FORWARD_ONLY)
                    && (concurrency == ResultSet.CONCUR_READ_ONLY)
                    && (updatable == false)
                    && (scrollable == false));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
createStatement() with 2 parameters - Create a statement as forward-only
and updatable.
**/
   public void Var008()
   {
       String info = " -- Create a statement as forward-only and updatable."; 
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            Statement s = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                       ResultSet.CONCUR_UPDATABLE);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table_
                                           + " FOR UPDATE");
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_FORWARD_ONLY)
                    && (concurrency == ResultSet.CONCUR_UPDATABLE)
                    && (updatable == true)
                    && (scrollable == false));
         } catch (Exception e) {
            failed(e, "Unexpected Exception"+info);
         }
      }
   }



/**
createStatement() with 2 parameters - Create a statement as scroll insensitive
and read-only.
**/
   public void Var009()
   {
       String info = " Create a statement as scroll insensitive and read-only."; 
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table_);
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
                    && (concurrency == ResultSet.CONCUR_READ_ONLY)
                    && (updatable == false)
                    && (scrollable == true));
         } catch (Exception e) {
            failed(e, "Unexpected Exception"+info);
         }
      }
   }



/**
createStatement() with 2 parameters - Create a statement as scroll insensitive
and updatable.
**/
   public void Var010()
   {
       String info = "Create a statement as scroll insensitive and updatable"; 
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_UPDATABLE);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table_
                                           + " FOR UPDATE");
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w != null)
                    && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
                    && (concurrency == ResultSet.CONCUR_UPDATABLE)
                    && (updatable == true)
                    && (scrollable == true));
         } catch (Exception e) {
            failed(e, "Unexpected Exception "+info);
         }
      }
   }



/**
createStatement() with 2 parameters - Create a statement as scroll sensitive
and read-only.

Native JDBC Note: I believe the only type of cursor we can't create is a scroll
insensitive cursor that is updatable.  This is because the implementation is such
that a scroll insensitive cursor is created by making a temporary table with the 
data in it (so updates to the real table are not seen).  Because the cursor is 
not accessing the real table, updates can't be done.
**/
   public void Var011()
   {
      if (checkJdbc20 ()) {
         try {
             if( (isToolboxDriver()) ){
                 //in v7r1, we get actual cursor type from hostserver.  Need to set property for sensitive, 
                 //or if comes back as asensitive. (when running var 9,11)
                connection_ = testDriver_.getConnection (baseURL_ + ";cursor sensitivity=sensitive", userId_, encryptedPassword_);
             }
            connection_.clearWarnings ();
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table_);
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            if (isToolboxDriver()) {
                assertCondition ((w == null)                            //@D2C
                        && (type == ResultSet.TYPE_SCROLL_SENSITIVE)    //@D2C
                        && (concurrency == ResultSet.CONCUR_READ_ONLY)
                        && (updatable == false)
                        && (scrollable == true));
            } else {
		if ( getRelease() ==  JDTestDriver.RELEASE_V7R1M0) {
		    // V5R1 version 
		    assertCondition ((w != null)
				     && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
				     && (concurrency == ResultSet.CONCUR_READ_ONLY)
				     && (updatable == false)
				     && (scrollable == true));
		} else { 
		    assertCondition ((w == null)
				     && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
				     && (concurrency == ResultSet.CONCUR_READ_ONLY)
				     && (updatable == false)
				     && (scrollable == true));
		}
            }
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
createStatement() with 2 parameters - Create a statement as scroll sensitive
and updatable.
**/
   public void Var012()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                       ResultSet.CONCUR_UPDATABLE);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table_
                                           + " FOR UPDATE");
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
                    && (concurrency == ResultSet.CONCUR_UPDATABLE)
                    && (updatable == true)
                    && (scrollable == true));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareStatement() - Create a prepared statement in a connection.
Verify the result set type and concurrency in the statement.
**/
   public void Var013()
   {
      if (checkJdbc20 ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            int resultSetType = ps.getResultSetType ();
            int resultSetConcurrency = ps.getResultSetConcurrency ();
            ps.close ();
            assertCondition ((resultSetType == ResultSet.TYPE_FORWARD_ONLY)
                    && (resultSetConcurrency == ResultSet.CONCUR_READ_ONLY));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareStatement() - Pass null for SQL statement.
Should throw an exception.
**/
   public void Var014()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (null);
         failed ("Did not throw exception."+ps);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
prepareStatement() - Pass an empty string for SQL statement.
Should throw an exception.
**/
   public void Var015()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement ("");
         failed ("Did not throw exception."+ps);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
prepareStatement() - Pass a blank string for SQL statement.
Should throw an exception.
**/
   public void Var016()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (" ");
         failed ("Did not throw exception."+ps);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
prepareStatement() - Pass an bogus string for SQL statement.
Should throw an exception.
**/
   public void Var017()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement ("CLIF");
         failed ("Did not throw exception."+ps);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
prepareStatement() - Create a prepared statement in a closed connection.
Should throw an exception.
**/
   public void Var018()
   {
      try {
         PreparedStatement ps = closedConnection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
         failed ("Did not throw exception."+ps);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
prepareStatement() with 3 parameters - Pass null for SQL statement.
Should throw an exception.
**/
   public void Var019()
   {
      if (checkJdbc20 ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement (null,
                                                                 ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            failed ("Did not throw exception."+ps);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareStatement() with 3 parameters - Pass an empty string for
SQL statement.   Should throw an exception.
**/
   public void Var020()
   {
      if (checkJdbc20 ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement ("",
                                                                 ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            failed ("Did not throw exception."+ps);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareStatement() with 3 parameters - Pass a blank string
for SQL statement.  Should throw an exception.
**/
   public void Var021()
   {
      if (checkJdbc20 ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement (" ",
                                                                 ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            failed ("Did not throw exception."+ps);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareStatement() with 3 parameters - Pass a bogus string
for SQL statement.  Should throw an exception.
**/
   public void Var022()
   {
      if (checkJdbc20 ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement ("CLIF",
                                                                 ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            failed ("Did not throw exception."+ps);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareStatement() with 3 parameters - Pass an bad value for
result set type.   Should throw an exception.
**/
   public void Var023()
   {
      if (checkJdbc20 ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT",
                                                                 -574, ResultSet.CONCUR_READ_ONLY);
            failed ("Did not throw exception."+ps);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareStatement() with 3 parameters - Pass an bad value for
result set concurrency.   Should throw an exception.
**/
   public void Var024()
   {
      if (checkJdbc20 ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT",
                                                                 ResultSet.TYPE_SCROLL_INSENSITIVE, -754);
            failed ("Did not throw exception."+ps);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareStatement() with 3 parameters - Create a prepared statement
in a closed connection. Should throw an exception.
**/
   public void Var025()
   {
      if (checkJdbc20 ()) {
         try {
            PreparedStatement ps = closedConnection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT",
                                                                       ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            failed ("Did not throw exception."+ps);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareStatement() with 2 parameters - Prepare a read only query as forward-only
and read-only.
**/
   public void Var026()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareStatement ("SELECT * FROM " + table_,
                                                                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_FORWARD_ONLY)
                    && (concurrency == ResultSet.CONCUR_READ_ONLY)
                    && (updatable == false)
                    && (scrollable == false));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareStatement() with 2 parameters - Prepare a updateable query as forward-only
and read-only.
**/
   public void Var027()
   {
      if (checkJdbc20 ()) {
	  String sql = ""; 
         try {
            connection_.clearWarnings ();
	    sql = "SELECT * FROM " + table_ + " FOR UPDATE"; 
            PreparedStatement s = connection_.prepareStatement (sql,
								ResultSet.TYPE_FORWARD_ONLY,
								ResultSet.CONCUR_READ_ONLY);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            int expectedConcurrency = ResultSet.CONCUR_READ_ONLY;
            if(isToolboxDriver() && getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
                expectedConcurrency = ResultSet.CONCUR_UPDATABLE; //v7r1 actual cursor on host
            
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_FORWARD_ONLY)
                    && (concurrency == expectedConcurrency )
                    && (updatable == false)
                    && (scrollable == false));
         } catch (Exception e) {
            failed(e, "Unexpected Exception sql was "+sql);
         }
      }
   }



/**
prepareStatement() with 2 parameters - Prepare a read-only statement as forward-only
and updatable.

@C3C This should work to a v5r1 server as long as PTF to support updateable cursors
is on there.
**/
   public void Var028()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareStatement ("SELECT * FROM " + table_,
                                                                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();

	    if ( getRelease() ==  JDTestDriver.RELEASE_V7R1M0) {
	        // V5R1 version 
		assertCondition ((w == null)
				 && (type == ResultSet.TYPE_FORWARD_ONLY)
				 && (concurrency == ResultSet.CONCUR_READ_ONLY)  
				 && (updatable == false)  
				 && (scrollable == false));
	    } else { 
		assertCondition ((w == null)
				 && (type == ResultSet.TYPE_FORWARD_ONLY)
				 && (concurrency == ResultSet.CONCUR_UPDATABLE)     //@C3C
				 && (updatable == true)                             //@C3C
				 && (scrollable == false));
	    }
	    
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareStatement() with 2 parameters - Prepare an updateable statement as forward-only
and updatable.
**/
   public void Var029()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareStatement ("SELECT * FROM " + table_ + " FOR UPDATE",
                                                                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_FORWARD_ONLY)
                    && (concurrency == ResultSet.CONCUR_UPDATABLE)
                    && (updatable == true)
                    && (scrollable == false));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareStatement() with 2 parameters - Prepare a statement as scroll insensitive
and read-only.
**/
   public void Var030()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareStatement ("SELECT * FROM " + table_,
                                                                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
                    && (concurrency == ResultSet.CONCUR_READ_ONLY)
                    && (updatable == false)
                    && (scrollable == true));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareStatement() with 2 parameters - Prepare a statement as scroll insensitive
and updatable.
**/
   public void Var031()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareStatement ("SELECT * FROM " + table_ + " FOR UPDATE",
                                                                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w != null)
                    && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
                    && (concurrency == ResultSet.CONCUR_UPDATABLE)
                    && (updatable == true)
                    && (scrollable == true));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareStatement() with 2 parameters - Prepare a statement as scroll sensitive
and read-only.

Native JDBC Note: I believe the only type of cursor we can't create is a scroll
insensitive cursor that is updatable.  This is because the implementation is such
that a scroll insensitive cursor is created by making a temporary table with the 
data in it (so updates to the real table are not seen).  Because the cursor is 
not accessing the real table, updates can't be done.
**/
   public void Var032()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareStatement ("SELECT * FROM " + table_,
                                                                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            if (isToolboxDriver()) {
                assertCondition ((w == null)                             //@D2C
                        && (type == ResultSet.TYPE_SCROLL_SENSITIVE)     //@D2C
                        && (concurrency == ResultSet.CONCUR_READ_ONLY)
                        && (updatable == false)
				 && (scrollable == true));
	    } else {
		if ( getRelease() ==  JDTestDriver.RELEASE_V7R1M0) {
		    assertCondition ((w != null)
			    && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
			    && (concurrency == ResultSet.CONCUR_READ_ONLY)
			    && (updatable == false)
			    && (scrollable == true));
		} else { 
		    assertCondition ((w == null)
				     && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
				     && (concurrency == ResultSet.CONCUR_READ_ONLY)
				     && (updatable == false)
				     && (scrollable == true));
		}
	    }
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareStatement() with 2 parameters - Prepare a statement as scroll sensitive
and updatable.
**/
   public void Var033()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareStatement ("SELECT * FROM " + table_ + " FOR UPDATE",
                                                                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
                    && (concurrency == ResultSet.CONCUR_UPDATABLE)
                    && (updatable == true)
                    && (scrollable == true));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareCall() - Create a callable statement in a connection.
Verify the result set type and concurrency in the statement.
**/
   public void Var034()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = connection_.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
            int resultSetType = cs.getResultSetType ();
            int resultSetConcurrency = cs.getResultSetConcurrency ();
            cs.close ();
            assertCondition ((resultSetType == ResultSet.TYPE_FORWARD_ONLY)
                    && (resultSetConcurrency == ResultSet.CONCUR_READ_ONLY));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareCall() - Pass null.  Should throw an exception.
**/
   public void Var035()
   {
      try {
         CallableStatement cs = connection_.prepareCall (null);
         failed ("Did not throw exception."+cs);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
prepareCall() - Pass an empty string.  Should throw an exception.
**/
   public void Var036()
   {
      try {
         CallableStatement cs = connection_.prepareCall ("");
         failed ("Did not throw exception."+cs);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
prepareCall() - Pass a blank string.  Should throw an exception.
**/
   public void Var037()
   {
      try {
         CallableStatement cs = connection_.prepareCall (" ");
         failed ("Did not throw exception."+cs);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
prepareCall() - Pass a bogus string.  Should throw an exception.
**/
   public void Var038()
   {
      try {
         CallableStatement cs = connection_.prepareCall ("CLIF");
         failed ("Did not throw exception."+cs);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
prepareCall() - Create a callable statement in a closed connection.
Should throw an exception.
**/
   public void Var039()
   {
      try {
         CallableStatement cs = closedConnection_.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
         failed ("Did not throw exception."+cs);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
prepareCall() with 3 parameters  - Create a callable
statement in a connection.
**/
   public void Var040()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = connection_.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)",
                                                            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            boolean success = (cs.getResultSetType () == ResultSet.TYPE_SCROLL_SENSITIVE)
                              && (cs.getResultSetConcurrency () == ResultSet.CONCUR_UPDATABLE);
            cs.close ();
            assertCondition (success);
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareCall() with 3 parameters - Pass null for SQL statement.
Should throw an exception.
**/
   public void Var041()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = connection_.prepareCall (null,
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            failed ("Did not throw exception."+cs);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareCall() with 3 parameters - Pass an empty string for
SQL statement.   Should throw an exception.
**/
   public void Var042()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = connection_.prepareCall ("",
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            failed ("Did not throw exception."+cs);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareCall() with 3 parameters - Pass a blank string
for SQL statement.  Should throw an exception.
**/
   public void Var043()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = connection_.prepareCall (" ",
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            failed ("Did not throw exception."+cs);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareCall() with 3 parameters - Pass a  bogus string
for SQL statement.  Should throw an exception.
**/
   public void Var044()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = connection_.prepareCall ("CLIF",
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            failed ("Did not throw exception."+cs);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareCall() with 3 parameters - Pass an bad value for
result set type.   Should throw an exception.
**/
   public void Var045()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = connection_.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)",
                                                            -574, ResultSet.CONCUR_UPDATABLE);
            failed ("Did not throw exception."+cs);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareCall() with 3 parameters - Pass an bad value for
result set concurrency.   Should throw an exception.
**/
   public void Var046()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = connection_.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)",
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE, -754);
            failed ("Did not throw exception."+cs);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareCall() with 3 parameters - Create a callable statement
in a closed connection. Should throw an exception.
**/
   public void Var047()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = closedConnection_.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)",
                                                                  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            failed ("Did not throw exception."+cs);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
prepareCall() with 2 parameters - Prepare a statement as forward-only
and read-only.
**/
   public void Var048()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareCall ("SELECT * FROM " + table_,
                                                           ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_FORWARD_ONLY)
                    && (concurrency == ResultSet.CONCUR_READ_ONLY)
                    && (updatable == false)
                    && (scrollable == false));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareCall() with 2 parameters - Prepare a statement as forward-only
and updatable.
**/
   public void Var049()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareCall ("SELECT * FROM " + table_ + " FOR UPDATE",
                                                           ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_FORWARD_ONLY)
                    && (concurrency == ResultSet.CONCUR_UPDATABLE)
                    && (updatable == true)
                    && (scrollable == false));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareCall() with 2 parameters - Prepare a statement as scroll insensitive
and read-only.
**/
   public void Var050()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareCall ("SELECT * FROM " + table_,
                                                           ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
                    && (concurrency == ResultSet.CONCUR_READ_ONLY)
                    && (updatable == false)
                    && (scrollable == true));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareCall() with 2 parameters - Prepare a statement as scroll insensitive
and updatable.
**/
   public void Var051()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareCall ("SELECT * FROM " + table_ + " FOR UPDATE",
                                                           ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w != null)
                    && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
                    && (concurrency == ResultSet.CONCUR_UPDATABLE)
                    && (updatable == true)
                    && (scrollable == true));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareCall() with 2 parameters - Prepare a statement as scroll sensitive
and read-only.

Native JDBC Note: I believe the only type of cursor we can't create is a scroll
insensitive cursor that is updatable.  This is because the implementation is such
that a scroll insensitive cursor is created by making a temporary table with the 
data in it (so updates to the real table are not seen).  Because the cursor is 
not accessing the real table, updates can't be done.
**/
   public void Var052()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareCall ("SELECT * FROM " + table_,
                                                           ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            if (isToolboxDriver()) {
                assertCondition ((w == null)                            //@D2C
                        && (type == ResultSet.TYPE_SCROLL_SENSITIVE)    //@D2C
                        && (concurrency == ResultSet.CONCUR_READ_ONLY)
                        && (updatable == false)
                        && (scrollable == true));
            } else {

		if ( getRelease() ==  JDTestDriver.RELEASE_V7R1M0) {
		    assertCondition ((w != null)
                        && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
                        && (concurrency == ResultSet.CONCUR_READ_ONLY)
                        && (updatable == false)
                        && (scrollable == true));
		} else { 
		    assertCondition ((w == null)
				     && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
				     && (concurrency == ResultSet.CONCUR_READ_ONLY)
				     && (updatable == false)
				     && (scrollable == true));
		}
            }
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
prepareCall() with 2 parameters - Prepare a statement as scroll sensitive
and updatable.
**/
   public void Var053()
   {
      if (checkJdbc20 ()) {
         try {
            connection_.clearWarnings ();
            PreparedStatement s = connection_.prepareCall ("SELECT * FROM " + table_ + " FOR UPDATE",
                                                           ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQLWarning w = connection_.getWarnings ();
            ResultSet rs = s.executeQuery ();
            int type = rs.getType ();
            int concurrency = rs.getConcurrency ();
            boolean updatable = checkUpdatable (rs);
            boolean scrollable = checkScrollable (rs);
            rs.close ();
            s.close ();
            assertCondition ((w == null)
                    && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
                    && (concurrency == ResultSet.CONCUR_UPDATABLE)
                    && (updatable == true)
                    && (scrollable == true));
         } catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



/**
createStatement()/prepareStatement()/prepareCall() - Verify
that we can create exactly 255 statements.

SQL400 - The Native JDBC driver handle limit keeps changing.
    V4R4 - 500 : V4R5 - 40,000 : V5R1 - 80,000
    Just test that we can get over 5000
**/
   public void Var054()
   {
      try {
         Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
         int count = fillConnection (c);
         c.close ();
         statements_.removeAllElements();

	 System.out.println("Able to create "+count+" statements "); 
         if (isToolboxDriver())
            assertCondition (count > 500, "Count is only "+count+" should be > 500");   // @C2C
         else
            assertCondition (count > 500, "Count is only "+count+" should be > 500");  // anything larger than 500 is success...

      } catch (Exception e) {
         failed(e, "Unexpected Exception");
      }
   }




/**
createStatement()/prepareStatement()/prepareCall() - Verify
that statement handles get reused.

SQL400 - The Native JDBC driver handle limit keeps changing.
    V4R4 - 500 : V4R5 - 40,000 : V5R1 - 80,000
    Just test that we can get over 5000
**/
   public void Var055()
   {
      try {
         Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

         if (isToolboxDriver() ||  getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {

            // Allocate some statement handles.
            int setA = 100;
            Statement[] s = new Statement[setA];
            for (int i = 0; i < setA; ++i)
               s[i] = c.createStatement ();

            // @C2D // Allocate as many more as we can.
            // @C2D int countBefore = fillConnection (c);

            // Free the some statement handles.
            int setB = 50;
            for (int i = 0; i < setB; ++i) {
               s[i].close ();
               s[i] = null;
            }

            // Allocate as many more as we can - again.
            // @C2D int countAfter = fillConnection (c);

            c.close ();

            // @C2C assertCondition ((countBefore == (255 - setA))
            // @C2C         && (countAfter == setB));
            succeeded();                                    // @C2A

         } else {
            // Create a driver specific statement object.
            Statement s = 
             c.createStatement();

            // Get it's CLI handle
            int beforeHandle = JDReflectionUtil.callMethod_I(s,  "getStatementHandle");

            // Close it.
            s.close();

            // When we create a new one, it should use the same handle over.
            s =  c.createStatement();
            int afterHandle = JDReflectionUtil.callMethod_I(s,  "getStatementHandle");

            c.close();

            assertCondition (beforeHandle == afterHandle);
         }

      } catch (Exception e) {
         failed(e, "Unexpected Exception");
      }
   }

/**
createStatement()/prepareStatement()/prepareCall() - Verify
that when we create 1000 statements, only one SQLWarning is posted.
**/
// Just applies for Toolbox Driver
   public void Var056()                                                                             //@D1A
   {
       if(isToolboxDriver()){
	   try {
	       Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	       try {
		   for (int i = 0; i < 1000; ++i) {

		// Allocate a different kind of statement
		// each time, just for fun.
		       switch (i % 3) {
			   case 0:
			       c.createStatement () ;
			       break;
			   case 1:
			       c.prepareStatement ("SELECT * FROM QSYS2.SYSTABLES");
			       break;
			   case 2:
			       c.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
			       break;
		       }

		   }
	       } 
	       catch (SQLException e) {
	 // Ignore.  This will happen when the connection is full.
	       }

	 //Check to see that an SQLWarning was posted
	       SQLWarning w = c.getWarnings();
	       c.close ();
	       int sqlWarningCount = 0;
	       if(w!=null)
	       {
		   while(w!=null)
		   {
		       if(w.getSQLState().equals("01G00") && w.getMessage().equals("1000 open statements on this connection."))
		       {
			   sqlWarningCount++;
		       }
		       w = w.getNextWarning();
		   }
	       }
	       if(sqlWarningCount == 1)
		   succeeded();
	       else if(sqlWarningCount == 0)
		   failed("Didn't post SQL Warning for 1000th open statement.");
	       else 
		   failed("Posted more than one SQL Warning for 1000th open statement.");

	   } catch (Exception e) {
	       failed(e, "Unexpected Exception:  Added by Toolbox 3/4/03 -> JTOpen testcase only for 1000th Statement Warning");
	   }
       }
       else{ notApplicable("TOOLBOX ONLY VARIATION");}
   }	

/**
createStatement()/prepareStatement()/prepareCall() - Verify
that when we create the 1000 statement, an SQLWarning is posted.

**/
// Just Applies for toolbox driver

 public void Var057()                                                                             //@D1A
 {
     if(isToolboxDriver()){
	 try {
	     Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	     int numWarnings = 0;
	     int warningOccurred = 0;
	     for (int i = 0; i < 1005; ++i) 
	     {
	     // Allocate a different kind of statement
	     // each time, just for fun.
		 switch (i % 3) {
		     case 0:
			 c.createStatement () ;
			 break;
		     case 1:
			 c.prepareStatement ("SELECT * FROM QSYS2.SYSTABLES");
			 break;
		     case 2:
			 c.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
			 break;
		 }
		 numWarnings = checkWarnings(c);
		 if(numWarnings != 0)
		 {
		     if(warningOccurred == 0)
			 warningOccurred = i;
		 }
	     }
	     c.close();
	     if(numWarnings == 1 && warningOccurred == 999)
		 succeeded();
	     else if(numWarnings == 1 && warningOccurred != 999) {

		 failed("Posted warning, but not at the 1000th open statement. numWarnings="+numWarnings+" warningOccurred="+warningOccurred);
	     }
	     else if(numWarnings == 0)
		 failed("Didn't post SQL Warning for 1000th open statement.");
	     else 
		 failed("Posted more than one SQL Warning for 1000th open statement.");

	 } catch (Exception e) {
	     failed(e, "Unexpected Exception:  Added by Toolbox 3/4/03 -> JTOpen testcase only for 1000th Statement Warning");
	 }
     }
     else{ notApplicable("TOOLBOX ONLY VARIATION");}
 }

/**
createStatement()/prepareStatement()/prepareCall() - Verify
that when we create 1000 statement, an SQLWarning is posted and that when we close it and create
another statement another warning isn't posted.

**/
// Just applies for toolbox driver
   public void Var058()                                                                             //@D1A
   {
       if(isToolboxDriver()){
	   try {
	       Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	       int numWarnings = 0;
	       int warningOccurred = 0;
	       for (int i = 0; i < 1001; ++i) 
	       {
             // Allocate a different kind of statement
             // each time, just for fun.
		   switch (i % 3) {
		       case 0:
			   Statement s = c.createStatement () ;
			   if( i == 999)
			       s.close();
			   break;
		       case 1:
			   c.prepareStatement ("SELECT * FROM QSYS2.SYSTABLES");
			   break;
		       case 2:
			   c.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
			   break;
		   }
		   numWarnings = checkWarnings(c);
		   if(numWarnings != 0)
		   {
		       if(warningOccurred == 0)
			   warningOccurred = i;
		   }
	       }
         
	       c.close ();
	       if(numWarnings == 1 && warningOccurred == 999)
		   succeeded();
	       else if(numWarnings == 1 && warningOccurred != 999)
		   failed("Posted warning, but not at the 1000th open statement.");
	       else if(numWarnings == 0)
		   failed("Didn't post SQL Warning for 1000th open statement.");
	       else 
		   failed("Posted more than one SQL Warning for 1000th open statement.");

	   } catch (Exception e) {
	       failed(e, "Unexpected Exception:  Added by Toolbox 3/4/03 -> JTOpen testcase only for 1000th Statement Warning");
	   }
       }
       else{ notApplicable("TOOLBOX ONLY VARIATION");}
   }

/**
createStatement()/prepareStatement()/prepareCall() - Verify
that when we create 1000 statement, an SQLWarning is posted and that when we close it and create
another statement another warning isn't posted.

**/
// Only applies for toolbox
public void Var059()                                                                             //@D1A
   {
    if(isToolboxDriver()){
	try {
	    Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	    int numWarnings = 0;
	    int warningOccurred = 0;
	    for (int i = 0; i < 1005; ++i) 
	    {
	     // Allocate a different kind of statement
             // each time, just for fun.
		switch (i % 3) 
		{
		    case 0:
			Statement s = c.createStatement () ;
			if( i >= 999)
			    s.close();
			break;	
		    case 1:
			PreparedStatement p = c.prepareStatement ("SELECT * FROM QSYS2.SYSTABLES");
			if (i>=999)
			    p.close();
			break;
		    case 2:
			CallableStatement cs = c.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
			if(i>=999)
			    cs.close();
			break;
		}
		numWarnings = checkWarnings(c);
		if(numWarnings != 0)
		{
		    if(warningOccurred == 0)
			warningOccurred = i;
		}

	    }
         
	    c.close ();
	    if(numWarnings == 1 && warningOccurred == 999)
		succeeded();
	    else if(numWarnings == 1 && warningOccurred != 999)
		failed("Posted warning, but not at the 1000th open statement.");
	    else if(numWarnings == 0)
		failed("Didn't post SQL Warning for 1000th open statement.");
	    else 
		failed("Posted more than one SQL Warning for 1000th open statement.");

	} catch (Exception e) {
	    failed(e, "Unexpected Exception:  Added by Toolbox 3/4/03 -> JTOpen testcase only for 1000th Statement Warning");
	}
    }
    else{ notApplicable("TOOLBOX ONLY VARIATION");}
}	

/**
createStatement()/prepareStatement()/prepareCall() - Verify
that when we create over 1000 statements but close the 999th statement
that an SQLWarning is not posted until we create the 1001th statement.

**/
// Toolbox test
   public void Var060()                                                                             //@D1A
   {
       if(isToolboxDriver()){
	   try {
	       Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	       int numWarnings = 0;
	       int warningOccurred = 0;
	       for (int i = 0; i < 1001; ++i) 
	       {
	     // Allocate a different kind of statement
             // each time, just for fun.
		   switch (i % 3) 
		   {
		       case 0:
			   /* Statement s = */ c.createStatement () ;
			   break;
		       case 1:
			   /* PreparedStatement p = */ c.prepareStatement ("SELECT * FROM QSYS2.SYSTABLES");
			   break;
		       case 2:
			   CallableStatement cs = c.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
			   if(i==998)
			       cs.close();
			   break;
		   }
		   numWarnings = checkWarnings(c);
		   if(numWarnings != 0)
		   {
		       if(warningOccurred == 0)
			   warningOccurred = i;
		   }

	       }
         
	       c.close ();
	       if(numWarnings == 1 && warningOccurred == 1000)
		   succeeded();
	       else if(numWarnings == 0)
		   failed("Didn't post SQL Warning for 1000th open statement.");
	       else 
		   failed("Posted more than one SQL Warning for 1000th open statement.");

	   } catch (Exception e) {
	       failed(e, "Unexpected Exception:  Added by Toolbox 3/4/03 -> JTOpen testcase only for 1000th Statement Warning");
	   }
       }
       else{ notApplicable("TOOLBOX ONLY VARIATION");}
   }

/**
createStatement()/prepareStatement()/prepareCall() - Verify
that when we create over 1000 statements but close the 995th - 999th statement
that an SQLWarning is not posted until we create the 1006th statement.

**/
// Toolbox testcase
  public void Var061()                                                                             //@D1A
   {
      if(isToolboxDriver()){
	  try {
	      Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	      int numWarnings = 0;
	      int warningOccurred = 0;
	      for (int i = 0; i < 1006; ++i) 
	      {
             // Allocate a different kind of statement
             // each time, just for fun.
		  switch (i % 3) 
		  {
		      case 0:
			  Statement s = c.createStatement () ;
			  if(i>= 995 && i<1000)
			      s.close();
			  break;
		      case 1:
			  PreparedStatement p = c.prepareStatement ("SELECT * FROM QSYS2.SYSTABLES");
			  if(i>= 995 && i<1000)
			      p.close();
			  break;
		      case 2:
			  CallableStatement cs = c.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
			  if(i>= 995 && i<1000)
			      cs.close();
			  break;
		  }
		  numWarnings = checkWarnings(c);
		  if(numWarnings != 0)
		  {
		      if(warningOccurred == 0)
			  warningOccurred = i;
		  }

	      }
         
	      c.close ();
	      if(numWarnings == 1 && warningOccurred == 1004)
		  succeeded();
	      else if(numWarnings == 1 && warningOccurred != 1004)
		  failed("Posted warning, but not at the 1000th open statement.");
	      else if(numWarnings == 0)
		  failed("Didn't post SQL Warning for 1000th open statement.");
	      else 
		  failed("Posted more than one SQL Warning for 1000th open statement.");

	  } catch (Exception e) {
	      failed(e, "Unexpected Exception:  Added by Toolbox 3/4/03 -> JTOpen testcase only for 1000th Statement Warning");
	  }
      }
      else{ notApplicable("TOOLBOX ONLY VARIATION");}
  }

  //@D3A - Make sure that a prepared statement that throws an exception when it is created, does not
  // take up space in the statements array.
  public void Var062()
  {
      if(isToolboxDriver()){
          int count = 0;
          try{
              Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
              Statement[] s = new Statement[11000];  //make a large statement array to hold at least max statements
              Statement s1 = null;
              Statement s2 = null;
              int exceptionThrown = 0;  //Set to true if exception is thrown 

              //Create 9997 statements
              for(int i=0; i<9997; i++)
              {
                  count++;
                  s[i] = c.createStatement();
              }

              PreparedStatement ps = null;       
              try{                               //Create a prepared statement object that throws an exception
                  ps = c.prepareStatement("SELECT * FROM");
                  count++;
              }
              catch(Exception e1){
                  exceptionThrown += 1;
              }
              finally{
                  try{
                      ps.close();
                  }catch(Exception e2){
                  }
              }
                                                
              try{                            //Should be able to create
                  count++;
                  s1 = c.createStatement();
              }
              catch(Exception e1){
                  exceptionThrown += 2;
              }

              try{
                  count++;
                  s2 = c.createStatement();
              }
              catch(Exception e1){
                  exceptionThrown += 3;
              }

              for(int i=0; i<9997; i++)
              {
                  s[i].close();
              }

              try{
                  if(s1 != null)
                      s1.close();
                  if(s2 != null)
                      s2.close();
              }
              catch(Exception e){

              }
              
              c.close();

              assertCondition((count == 9999) && (exceptionThrown == 4), "Expected count=9999, received " + count + ", expected exceptionThrown =4, received " + exceptionThrown);

        }
        catch(Exception e){
            failed(e, "Unexpected Exception");
        }
      }
      else
          notApplicable("TOOLBOX ONLY VARIATION Added by Toolbox 8/26/3004");
  }

  //@D4A - Make sure that a prepared statement that throws an exception when it is created, does not
  // leak CLI handles 
  public void Var063()
  {
      if(getDriver() == JDTestDriver.DRIVER_NATIVE){
	  try{
	      Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	    // Create a driver specific statement object.
	      Statement s =  c.createStatement();

	    // Get it's CLI handle
	      int beforeHandle = JDReflectionUtil.callMethod_I(s,  "getStatementHandle");;
	      // Close it.
		s.close();

	      // 
	      // Now prepare 1000 statements that fail.  This should not consume CLI handles.
	      // 

	      for (int i = 0; i < 1000; i++ ) {
		  try {
		      /* PreparedStatement ps = */ c.prepareStatement("SELECT * FROM");  
		  } catch (Exception e) {
		      // keep going 
		  } 
	      } 

	      //
	      // Running the garbage collector is a workaround
	      //
	      /* 
	      System.gc();
	      try { 
		  Thread.sleep(5000); 
	      } catch (Exception e) {
		  e.printStackTrace(); 
	      } 
	      */
	      // When we create a new one, it should use the same handle over.
	      s =  c.createStatement();
	      int afterHandle = JDReflectionUtil.callMethod_I(s,  "getStatementHandle");

	      c.close();

	      assertCondition (beforeHandle == afterHandle, "Handle leak!!! beforeHandle = "+beforeHandle+" after handle = "+afterHandle);

	  } catch (Exception e) {
	      failed(e, "Unexpected Exception -- added by native 08/15/2005");
	  } 
      }	  else { 
	  notApplicable("Added by Native 08/15/2005");
      }
  }

  //@D4A - Make sure that a prepared statement that throws an exception when it is created, does not
  // leak CLI handles 
  public void Var064()
  {
      if(getDriver() == JDTestDriver.DRIVER_NATIVE){
	  try{
	      Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	    // Create a driver specific statement object.
	      Statement s =  c.createStatement();

	    // Get it's CLI handle
	      int beforeHandle = JDReflectionUtil.callMethod_I(s,  "getStatementHandle");
	      // Close it.
		s.close();

	      // 
	      // Now prepare 1000 statements that fail.  This should not consume CLI handles.
	      // Changed test in V6R1 to have & in name since bigger than 10 character names
	      // are now supported. 
	      // 

	      for (int i = 0; i < 1000; i++ ) {
		  try {
		      /* PreparedStatement ps = */ c.prepareCall("CALL T&RASHYTRASHY.NOPROC(1,2,3)");  
		  } catch (Exception e) {
		      // keep going 
		  } 
	      } 

	      // When we create a new one, it should use the same handle over.
	      s =  c.createStatement();
	      int afterHandle = JDReflectionUtil.callMethod_I(s,  "getStatementHandle");

	      c.close();

	      assertCondition (beforeHandle == afterHandle, "Handle leak!!! beforeHandle = "+beforeHandle+" after handle = "+afterHandle);

	  } catch (Exception e) {
	      failed(e, "Unexpected Exception -- added by native 08/15/2005");
	  } 
      }	  else { 
	  notApplicable("Added by Native 08/15/2005");
      }
  }



}
