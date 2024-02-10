///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionWarnings.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDConnectionWarnings.java
 //
 // Classes:      JDConnectionWarnings
 //
 ////////////////////////////////////////////////////////////////////////
package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDJobName;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.ResultSet; 
import java.util.Hashtable;



/**
Testcase JDConnectionWarnings.  This tests the following methods
of the JDBC Connection class:

<ul>
<li>clearWarnings()
<li>getWarning()
</ul>
**/
public class JDConnectionWarnings
extends JDTestcase
{



    // Private data.
    private static  String         table_ = JDConnectionTest.COLLECTION + ".JDCWARN";
    private static  String         procedure_ = JDConnectionTest.COLLECTION + ".JDCWARNCALL";
    private static  String         emptytable_ = JDConnectionTest.COLLECTION + ".JDCEMPTY";
    private              Connection     connection_;



/**
Constructor.
**/
    public JDConnectionWarnings (AS400 systemObject,
                                    Hashtable<?,?> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDConnectionWarnings",
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
        // Create the table.
        table_ = JDConnectionTest.COLLECTION + ".JDCWARN";
	procedure_ = JDConnectionTest.COLLECTION + ".JDCWARNCALL";
        emptytable_ = JDConnectionTest.COLLECTION + ".JDCEMPTY";
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        Statement s = connection_.createStatement ();
        
        try { s.executeUpdate ("DROP TABLE " + table_); } catch (Exception e) {} 
        try { s.executeUpdate ("DROP TABLE " + emptytable_); } catch (Exception e) {} 
        try { s.executeUpdate ("DROP PROCEDURE "+procedure_);  } catch (Exception e) {} 

        
        
        s.executeUpdate ("CREATE TABLE " + table_ + " (NAME VARCHAR(10))");

	s.executeUpdate ("CREATE PROCEDURE "+ procedure_ +"() RESULT SET 1"+
			 " LANGUAGE SQL "+
			 " BEGIN "+
			 " DECLARE C1 CURSOR FOR  select * from SYSIBM.SYSDUMMY1; "+
			 " OPEN C1; "+
			 " SET RESULT SETS CURSOR C1; "+
			 " END "); 

        s.executeUpdate ("CREATE TABLE " + emptytable_ + " (NAME VARCHAR(10))");


        connection_.commit(); // for xa
        s.close ();
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
        s.executeUpdate ("DROP TABLE " + emptytable_);
        s.executeUpdate ("DROP PROCEDURE "+procedure_); 
        s.close ();
        connection_.commit(); // for xa
        connection_.close ();
    }



/**
Forces a single warning to be posted to the connection.

@param c The connection.

@exception Exception If an exception occurs.
**/
    public void forceWarning (Connection c)
        throws Exception
    {
        c.setAutoCommit (false);
        c.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

        Statement s = c.createStatement ();
        s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES ('MURCH')");
        s.close ();

        // This forces a warning that the transaction was
        // committed.
	//
	// 
	// In V5R5, setting autocommit = true does not cause a warning to be
	// posted to a connection.  This warning was causing problems for
	// SAP and requested that this behavior be removed.  I'll need
	// to find another way to send a warning to the connection.
	//
        c.setAutoCommit (true);

	if ( (getRelease() >= JDTestDriver.RELEASE_V5R5M0) &&
	     (getDriver () == JDTestDriver.DRIVER_NATIVE)) {
	    //
	    // Creating a statement with
            // rsType == DB2ResultSet.TYPE_SCROLL_INSENSITIVE
            // and
            // rsConcurrency == DB2ResultSet.CONCUR_UPDATABLE
            // will force a warning for the native driver.
            //
            c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE); 

	} 
    }



/**
clearWarnings() - Has no effect when there are no
warnings.
**/
    public void Var001()
    {
        try {
            connection_.clearWarnings ();
            connection_.clearWarnings ();
            SQLWarning w = connection_.getWarnings ();
            assertCondition (w == null);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
clearWarnings() - Clears warnings after 1 has been posted.
**/
    public void Var002()
    {
        try {
            forceWarning (connection_);
            SQLWarning w1 = connection_.getWarnings ();
            connection_.clearWarnings ();
            SQLWarning w2 = connection_.getWarnings ();
            assertCondition ((w1 != null) && (w2 == null), "w1 = "+w1+" sb not null  && w2 = "+w2+" sb null");
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
clearWarnings() - Clears warnings when a connection is
closed.
**/
    public void Var003()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            forceWarning (c);
            c.close ();
            SQLWarning w1 = c.getWarnings ();
            c.clearWarnings ();
            SQLWarning w2 = c.getWarnings ();
            assertCondition ((w1 != null) && (w2 == null), "w1 = "+w1+" sb not null  && w2 = "+w2+" sb null");
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
getWarning() - Returns null if no warnings have been
reported.
**/
    public void Var004()
    {
        try {
            connection_.clearWarnings ();
            SQLWarning w = connection_.getWarnings();
            assertCondition (w == null);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
getWarning() - Returns the first warning when 1 warning has been
reported.
**/
    public void Var005()
    {
        try {
            connection_.clearWarnings ();
            forceWarning (connection_);
            SQLWarning w1 = connection_.getWarnings ();
            SQLWarning w2 =null;
	    if (w1 != null) w2 = w1.getNextWarning ();
            assertCondition ((w1 != null) && (w2 == null));
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
getWarning() - Returns the first warning when 2 warnings have been
reported.
**/
    public void Var006()
    {
        try {
            connection_.clearWarnings ();
            forceWarning (connection_);
            forceWarning (connection_);
            SQLWarning w1 = connection_.getWarnings ();
            SQLWarning w2 = null;
	    if (w1 != null) w2 = w1.getNextWarning ();
            SQLWarning w3 = null;
	    if (w2 != null) w3= w2.getNextWarning ();
            assertCondition ((w1 != null) && (w2 != null) && (w3 == null));
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }




/**
getWarning() - Returns the first warning even after the connection
is closed.
**/
    public void Var007()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            forceWarning (c);
            c.close ();
            SQLWarning w = c.getWarnings ();
            assertCondition (w != null);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
    }


    public String executeCallAndGetWarnings(Connection c, String sql) throws Exception {
        StringBuffer sb = new StringBuffer(); 
        
	CallableStatement cstmt = c.prepareCall(sql);
	
        cstmt.execute(); 
        SQLWarning warning = cstmt.getWarnings(); 
        if (warning == null) {
          sb.append("NONE"); 
        } else {
          int count = 1; 
          while (warning != null && count <= 10 ) {
            sb.append(" ["+count+"] "+warning.getSQLState()+":"+warning.getErrorCode()+":"+warning.getMessage()); 
            warning = warning.getNextWarning(); 
            count++; 
          }
        }
        cstmt.close(); 
        return sb.toString(); 
    } 

    
    public String executeUpdateAndGetWarnings(Connection c, String sql) throws Exception {
      StringBuffer sb = new StringBuffer(); 
      
      Statement stmt = c.createStatement();        
      stmt.executeUpdate(sql); 
      SQLWarning warning = stmt.getWarnings(); 
      if (warning == null) {
        sb.append("NONE"); 
      } else {
        int count = 1; 
        while (warning != null && count <= 10 ) {
          sb.append(" ["+count+"] "+warning.getSQLState()+":"+warning.getErrorCode()+":"+warning.getMessage()); 
          warning = warning.getNextWarning(); 
          count++; 
        }
      }
      stmt.close(); 
      return sb.toString(); 
  } 

    public String executeQueryAndGetWarnings(Connection c, String sql) throws Exception {
      StringBuffer sb = new StringBuffer(); 
      
      Statement stmt = c.createStatement();        
      ResultSet rs = stmt.executeQuery(sql); 
      SQLWarning warning = stmt.getWarnings(); 
      if (warning != null) {
        int count = 1; 
        while (warning != null && count <= 10 ) {
          sb.append(" ["+count+"] "+warning.getSQLState()+":"+warning.getErrorCode()+":"+warning.getMessage()); 
          warning = warning.getNextWarning(); 
          count++; 
        }
      }
      while (rs.next()) {
        warning = rs.getWarnings(); 
        if (warning != null) {
          int count = 1; 
          while (warning != null && count <= 10 ) {
            sb.append(" ["+count+"] "+warning.getSQLState()+":"+warning.getErrorCode()+":"+warning.getMessage()); 
            warning = warning.getNextWarning(); 
            count++; 
          }
          rs.clearWarnings();
        }
        
      }
      rs.close(); 
      stmt.close(); 
      if (sb.length() == 0) {
        sb.append("NONE"); 
      }
      return sb.toString(); 
  } 

/**
 * Make sure we can get the warning back for a standard connection 
 */
    public void Var008() {
	try {
            String expected = "[1] 0100C:466:1 result sets are available from procedure JDCWARNCALL";
	    if (isToolboxDriver()) {
		expected = "[1] 0100C:466:[SQL0466] 1 result sets are available from procedure JDCWARNCALL"; 
	    } 
	    Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	    String warning = executeCallAndGetWarnings(c, "CALL "+procedure_);
	    assertCondition(warning.indexOf(expected) >= 0 , "Expected to find '"+expected+"' got '"+warning+"'");
	    c. close(); 
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
	
    } 

    /**
     * Make sure we can get the warning back for a standard connection 
     */
        public void Var009() {
        try {
                String expected = "[1] 02000:100:Row not found";

	    if (isToolboxDriver()) {
		expected = "[1] 02000:100:[SQL0100] Row not found for DELETE."; 
	    } 

            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

            String warning = executeUpdateAndGetWarnings(c, "DELETE FROM "+emptytable_);
            assertCondition(warning.indexOf(expected) >= 0 , "Expected to find '"+expected+"' got '"+warning+"'");
            c. close(); 
            }
            catch(Exception e) {
                failed(e, "Unexpected Exception");
            }
        
        } 
        
        /**
         * Make sure we can get the warning back for a standard connection 
         */
            public void Var010() {
            try {
                    String expected = "[1] 01564:802:Data conversion or data mapping error";

	    if (isToolboxDriver()) {
		expected = "[1] 01564:802:[SQL0802] Data conversion or data mapping error"; 
	    } 

                Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

                String warning = executeQueryAndGetWarnings(c, "select 8/0 from sysibm.sysdummy1");
                assertCondition(warning.indexOf(expected) >= 0 , "Expected to find '"+expected+"' got '"+warning+"'");
                c. close(); 
                }
                catch(Exception e) {
                    failed(e, "Unexpected Exception");
                }
            
            } 
            

        
            /**
             * Verify that the warning is ignored when using "ignore warnings" property
             */
            public void Var011() {
                /* Added toolbox 12/7/2015 */ 
                try {
                  String expected = "NONE"; 
                  Connection c = testDriver_.getConnection (baseURL_+";ignore warnings=0100C", userId_, encryptedPassword_);
                  
                  String warning = executeCallAndGetWarnings(c, "CALL "+procedure_);
                  assertCondition(expected.equals(warning), "Expected '"+expected+"' got '"+warning+"'");
                  c. close(); 
                }
                catch(Exception e) {
                  failed(e, "Unexpected Exception");
                }
            } 
            
            
        /**
         * Verify that the warning is ignored when using "ignore warnings" with 0100C at beginning of list
         */
            public void Var012() {
              /* Added toolbox 12/7/2015 */ 
                try {
                  String expected = "NONE"; 
                  Connection c = testDriver_.getConnection (baseURL_+";ignore warnings=0100C,02000,01564", userId_, encryptedPassword_);
                  
                  String warning = executeCallAndGetWarnings(c, "CALL "+procedure_);
                  assertCondition(expected.equals(warning), "Expected '"+expected+"' got '"+warning+"'");
                  c. close(); 
                }
                catch(Exception e) {
                  failed(e, "Unexpected Exception");
                }
              
            } 
            
            /**
             * Verify that the warning is ignored when using "ignore warnings" with 02000 at end of list
             */
            public void Var013() {
              /* Added toolbox 12/7/2015 */ 
                try {
                  String expected = "NONE"; 
                  Connection c = testDriver_.getConnection (baseURL_+";ignore warnings=0100C,01564,02000", userId_, encryptedPassword_);
                  
                  String warning = executeUpdateAndGetWarnings(c, "DELETE FROM "+emptytable_);
                  assertCondition(expected.equals(warning), "Expected '"+expected+"' got '"+warning+"'");
                  c. close(); 
                }
                catch(Exception e) {
                  failed(e, "Unexpected Exception");
                }
            } 
            
            
            /**
             * Verify that the warning is ignored when using "ignore warnings" with 01564 in middle of list
             */
            public void Var014() {
              /* Added toolbox 12/7/2015 */ 
                  try {
                  String expected = "NONE"; 
                  Connection c = testDriver_.getConnection (baseURL_+";ignore warnings=0100C,01564,02000", userId_, encryptedPassword_);
                  
                  String warning = executeQueryAndGetWarnings(c, "select 8/0 from sysibm.sysdummy1");
                  assertCondition(expected.equals(warning), "Expected '"+expected+"' got '"+warning+"'");
                  c. close(); 
                }
                catch(Exception e) {
                  failed(e, "Unexpected Exception");
                }
              
            } 
            

            /**
             * Verify that all types of warnings are ignored 
             */
            public void Var015() {
              /* Added toolbox 12/7/2015 */ 
                              try {
                  String expected = "NONE"; 
                  Connection c = testDriver_.getConnection (baseURL_+";ignore warnings=0100C,01564,02000", userId_, encryptedPassword_);
                  
                  String warning1 = executeQueryAndGetWarnings(c, "select 8/0 from sysibm.sysdummy1");
                  String warning2 = executeUpdateAndGetWarnings(c, "DELETE FROM "+emptytable_);
                  String warning3 = executeCallAndGetWarnings(c, "CALL "+procedure_);
                  
                  assertCondition(expected.equals(warning1) &&
                      expected.equals(warning2) &&
                      expected.equals(warning3)
                      , "Expected '"+expected+"' got '"+warning1+"' '"+warning2+"' '"+warning3+"' ");
                  c. close(); 
                }
                catch(Exception e) {
                  failed(e, "Unexpected Exception");
                }
            } 
            
            
            /**
             * Verify that ignore warning from older connection not used
             */
            public void Var016() {
              /* Added toolbox 12/7/2015 */ 
                              try {
                  String expected = "[1] 01564:802:Data conversion or data mapping error"; 
                  if (isToolboxDriver()) {
                         expected = "[1] 01564:802:[SQL0802] Data conversion or data mapping error"; 
                  } 
 
                  Connection c1 = testDriver_.getConnection (baseURL_+";ignore warnings=0100C,01564,02000", userId_, encryptedPassword_);
                  Connection c2 = testDriver_.getConnection (baseURL_+";ignore warnings=0100C,01564,02000", userId_, encryptedPassword_);
                  Connection c3 = testDriver_.getConnection (baseURL_+";ignore warnings=0100C,01564,02000", userId_, encryptedPassword_);
                  c1.close(); 
                  c2.close(); 
                  c3.close(); 
                  
                  Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

                  String warning = executeQueryAndGetWarnings(c, "select 8/0 from sysibm.sysdummy1");

                  assertCondition(warning.indexOf(expected) >= 0 , "Expected to find '"+expected+"' got '"+warning+"'");

                  c. close(); 
                }
                catch(Exception e) {
                  failed(e, "Unexpected Exception");
                }
            
            } 
            


        /**
         * Verify that no memory leak occurs
         */
            public void Var017() {
              if (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() >= JDTestDriver.RELEASE_V5R4M0) { 
                try {
                  String expected = "NONE"; 
                  Connection c = testDriver_.getConnection (baseURL_+";ignore warnings=0100C,02000,01564", userId_, encryptedPassword_);
                  String warning = "";
                  warning = executeCallAndGetWarnings(c, "CALL "+procedure_);
		  int beginningMemory = JDJobName.getJobMem();
		  int loopsize = 4096; 
		  for (int i = 0; i < loopsize; i++) {
		      warning = executeCallAndGetWarnings(c, "CALL "+procedure_);
		      if (i % 400 == 0) {
			  System.gc(); 
		      } 
		  } 
		  int endingMemory = JDJobName.getJobMem();
		  int memoryDifference = endingMemory-beginningMemory; 
                  assertCondition(expected.equals(warning) && memoryDifference <= 16384, "Expected '"+expected+"' got '"+warning+"' endingMemory="+endingMemory+" beginningMemory="+beginningMemory+" memory difference is "+memoryDifference+" for "+loopsize+" calls");
                  c. close(); 
                }
                catch(Exception e) {
                  failed(e, "Unexpected Exception");
                }
              } else {
                notApplicable("Native driver test for 'ignore warnings'"); 
              }
            } 

            
            


  public void forceConnectionWarning(Connection c, String warning, StringBuffer sb)
      throws SQLException {

    if (warning.equals("01H30")) {
      sb.append("Attempting to create 01H30\n");
      sb.append("setAutoCommit(false)\n");
      c.setAutoCommit(false);
      sb.append("running query\n");
      Statement s = c.createStatement();
      s.executeQuery("select * from sysibm.sysdummy1");
      s.close();
      sb.append("setAutoCommit(true)\n");
      c.setAutoCommit(true);
    } else if (warning.equals("01G00")) {
      sb.append("Attempting to create 01H30\n");
      Statement statements[] = new Statement[1002];
      for (int i = 0; i < statements.length; i++) {
        statements[i] = c.createStatement();
      }

      for (int i = 0; i < statements.length; i++) {
        statements[i].close();
      }
    }
  }

	    public void verifyIgnoredConnectionWarning( String ignoredWarnings, String forcedWarning ) {
		StringBuffer sb = new StringBuffer(); 
		    try {
			boolean passed = true;

			sb.append("Verifying that warning occurs\n");

			String testURL = baseURL_; 
			if (forcedWarning.equals("01H20")) testURL +=";bogus property=charles"; 
			Connection c0 = testDriver_.getConnection (testURL,  userId_, encryptedPassword_);
			forceConnectionWarning(c0, forcedWarning, sb);
			SQLWarning warning0 = c0.getWarnings();
			if (warning0 == null) {
			    sb.append("  Error. No warning occurred\n");
			    passed = false; 
			} else {
			    String sqlState = warning0.getSQLState();
			    if (sqlState == null) {
				sb.append("  Error. SQLState does not exist on warning\n");
				passed = false; 

			    } else {
				if (!sqlState.equals(forcedWarning)) {
				    sb.append(" Error, got SQLSTATE="+sqlState+" sb "+forcedWarning);
				    passed = false; 
				} 
			    } 
			} 

			c0 .close();

			Connection c1 = testDriver_.getConnection (testURL+";ignore warnings=" +ignoredWarnings,  userId_, encryptedPassword_);
      forceConnectionWarning(c1, forcedWarning, sb);
      SQLWarning warning1 = c1.getWarnings();
      if (warning1 != null) { 
        sb.append("Error.  Got warning "+warning1+"\n");
	sb.append("  warning.sqlState = "+ warning1.getSQLState()+"\n");
	printStackTraceToStringBuffer(warning1, sb); 
        passed = false; 
      }
		

			c1.close();

			assertCondition(passed,sb); 


		    }
		    catch(Exception e) {
			failed(e, "Unexpected Exception "+sb.toString());
		    }
	    } 


      public void Var018() {
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      notApplicable("Native does not issue warning on setAutoCommit(true)");
	  } else {
	      verifyIgnoredConnectionWarning("01H30","01H30");
	  }
      }
            

      public void Var019() {
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      notApplicable("Native does not issue warning on more than 1000 statements");
	  } else {
	      verifyIgnoredConnectionWarning("01G00","01G00");
	  }
      }

      
      public void Var020() {
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      notApplicable("Native does prevent warning on bad property on URL -- chicken / egg scenario"); 
	  } else {

	      verifyIgnoredConnectionWarning("01H20","01H20");
	  }
      }

      /**
       * Verify that warning objects are not created 
       */
     public void Var021() {
   	  if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      notApplicable("Not written yet"); 
	  } else {
      	PrintStream savedOut = System.out; 
          try {
        	boolean passed = true; 
        	StringBuffer sb = new StringBuffer(); 

        	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
        	System.setOut(new PrintStream(outputStream)); 
       	
        			
            String expected = "NONE"; 
            Connection c = testDriver_.getConnection (baseURL_+";ignore warnings=0100C;toolbox trace=all", userId_, encryptedPassword_);
            
            String warning = executeCallAndGetWarnings(c, "CALL "+procedure_);
            if (!expected.equals(warning)) {
            	passed = false; 
            	sb.append("\nExpected '"+expected+"' got '"+warning+"'"); 
            }
            c. close(); 
            c = testDriver_.getConnection (baseURL_+";ignore warnings=0100C;toolbox trace=none", userId_, encryptedPassword_);
            c.close(); 
            System.setOut(savedOut);
            savedOut = null; 
            
            String traceOut = new String(outputStream.toByteArray()); 
            // System.out.println("----------------------Trace begin ----------------"); 
            // System.out.println(traceOut); 
            // System.out.println("----------------------Trace end ----------------"); 
            if (traceOut.length() < 100) { 
            	passed = false; 
            	sb.append("\nTrace is too small.  Update jt400.jar"); 
            	sb.append(traceOut); 
            }
            int traceIndex = traceOut.indexOf("com.ibm.as400.access.JDError.getSQLWarning");
            if (traceIndex > 0) { 
            	passed=false; 
            	sb.append("\nError Warning was constructed!!! Trace has "); 
            	sb.append("\n"); 
            	int endIndex = traceIndex-365+4000; 
            	if (traceOut.length() > endIndex) { 
            		endIndex = traceOut.length(); 
            	}
            	sb.append(traceOut.substring(traceIndex-365,endIndex)); 
            }
            assertCondition(passed, sb );
          }
          catch(Exception e) {
            failed(e, "Unexpected Exception");
          } finally {
        	  if (savedOut != null) { 
        		  System.setOut(savedOut);
        	  }
          }
      } 
     }
      
     /**
      * Verify that the warning is ignored when using "ignore warnings" property
      */
     public void Var022() {
       if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
         notApplicable("Native does not support ALL"); 
       } else { 
         try {
           String expected = "NONE"; 
           Connection c = testDriver_.getConnection (baseURL_+";ignore warnings=all", userId_, encryptedPassword_);
           
           String warning = executeCallAndGetWarnings(c, "CALL "+procedure_);
           assertCondition(expected.equals(warning), "Expected '"+expected+"' got '"+warning+"'");
           c. close(); 
         }
         catch(Exception e) {
           failed(e, "Unexpected Exception");
         }
     } 
     }

      
      
}



