///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementMisc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementMisc.java
//
// Classes:      JDStatementMisc
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.math.BigDecimal;


/**
Testcase JDStatementMisc.  This tests the following method
of the JDBC Statement class:

<ul>
<li>getEscapeProcessing()</li>
<li>setEscapeProcessing()</li>
<li>getMaxFieldSize()</li>
<li>setMaxFieldSize()</li>
<li>getMaxRows()</li>
<li>setMaxRows()</li>
<li>getLargeMaxRows()</li>
<li>setLargeMaxRows()</li>

<li>cancel()</li>
<li>toString()</li>
<li>isClosed()</li> 
</ul>
**/
public class JDStatementMisc
extends JDTestcase
{
    private static final boolean DEBUG = false;

    // table name
    private static String LARGE_PRECISION_TABLE = JDStatementTest.COLLECTION + ".LDPREC";
    private static String LARGE_COLUMN_NAME_TABLE = JDStatementTest.COLLECTION + ".LCNTAB";        //@H1A
    private static String QUERY_STORAGE_LIMIT_TABLE = JDStatementTest.COLLECTION + ".QSLTAB";       //@I2A


    // Private data.
    private              Connection     connection_;
    private              Connection     connection2_;
    private              Connection     connection3_;
    private              Connection     connectionPwrSys_;        //@I3A
    private              String         slow_;



    /**
    Constructor.
    **/
    public JDStatementMisc (AS400 systemObject,
                            Hashtable namesAndVars,
                            int runMode,
                            FileOutputStream fileOutputStream,
                            
                            String password,
                            String powerUserID,
                            String powerPassword)
    {
        super (systemObject, "JDStatementMisc",
               namesAndVars, runMode, fileOutputStream,
               password, powerUserID, powerPassword);
    }



    /**
    Performs setup needed before running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void setup ()
    throws Exception
    {

	// reset names after -lib arg is processed.
	LARGE_PRECISION_TABLE = JDStatementTest.COLLECTION + ".LDPREC";
	LARGE_COLUMN_NAME_TABLE = JDStatementTest.COLLECTION + ".LCNTAB";        //@H1A
        QUERY_STORAGE_LIMIT_TABLE = JDStatementTest.COLLECTION + ".QSLTAB";       //@I2A

        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        connection2_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        connection3_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        connectionPwrSys_ = testDriver_.getConnection (baseURL_, pwrSysUserID_, pwrSysEncryptedPassword_);//@I3A

    }



    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
        slow_=null; 
        if(slow_ != null)
        {
            Statement s = connection_.createStatement ();
            for(int i = 1; i <= 26; ++i)
                s.executeUpdate ("DROP TABLE " + JDStatementTest.COLLECTION
                                 + ".SLOW" + i);
            s.close ();
        }

        connection_.close ();
        connection2_.close ();
        connection3_.close();
        connectionPwrSys_.close();                                //@I3A
    }





    /**
    setEscapeProcessing() - Verify that the default is true.
    **/
    public void Var001()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.executeQuery ("SELECT {fn concat(CITY,STATE)} FROM QIWS.QCUSTCDT");
            s.close ();
            assertCondition (true);
        }
        catch(SQLException e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setEscapeProcessing() - Set to true.  Verify that the escape
    syntax is processed.
    **/
    public void Var002()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.setEscapeProcessing (true);
            s.executeQuery ("SELECT {fn concat(CITY,STATE)} FROM QIWS.QCUSTCDT");
            s.close ();
            assertCondition (true);
        }
        catch(SQLException e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    setEscapeProcessing() - Set to false.  Verify that the escape
    syntax is not processed - the bad syntax will cause an exception.
    **/
    public void Var003()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.setEscapeProcessing (false);
            s.executeQuery ("SELECT {fn concat(CITY,STATE)} FROM QIWS.QCUSTCDT");
            failed("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setEscapeProcessing() - Set to false on a closed statement.
    Should throw an exception.
    **/
    public void Var004()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.close ();
            s.setEscapeProcessing (false);
            failed("Didn't throw SQLException");
        }
        catch(Exception e)
        {
	    assertClosedException(e, "");

        }
    }



    /**
    getMaxFieldSize() - Verify that the returned value is 0
    by default (unlimited field size).
    **/
    public void Var005()
    {
        try
        {
            Statement s = connection_.createStatement ();
            int maxFieldSize = s.getMaxFieldSize ();
            s.close ();
            assertCondition (maxFieldSize == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getMaxFieldSize() - Verify that the returned value is the
    one that was set.
    **/
    public void Var006()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (26);
            int maxRows = s.getMaxFieldSize ();
            s.close ();
            assertCondition (maxRows == 26);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getFieldSize() - Should thrown an exception is the
    statement is closed.
    **/
    public void Var007()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.close ();
            int maxFieldSize = s.getMaxFieldSize ();
            failed("Didn't throw SQLException but got "+maxFieldSize);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setFieldSize() - Pass a negative value.  Should throw
    an exception.
    **/
    public void Var008()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (-5);
            failed("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setFieldSize() - Pass a value larger than the maximum
    character literal size.  This should work.  (It used to
    post a warning, but we lifted that restriction, so we are
    testing it here.)
    **/
    public void Var009()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.clearWarnings ();
            DatabaseMetaData dmd = connection_.getMetaData ();
            s.setMaxFieldSize (dmd.getMaxCharLiteralLength() + 100);
            assertCondition (s.getWarnings () == null);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getMaxRows() - Verify that the returned value is 0
    by default (unlimited rows).
    **/
    public void Var010()
    {
        try
        {
            Statement s = connection_.createStatement ();
            int maxRows = s.getMaxRows ();
            s.close ();
            assertCondition (maxRows == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getMaxRows() - Verify that the returned value is the
    one that was set.
    **/
    public void Var011()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.setMaxRows (15);
            int maxRows = s.getMaxRows ();
            s.close ();
            assertCondition (maxRows == 15);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getMaxRows() - Should thrown an exception is the
    statement is closed.
    **/
    public void Var012()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.close ();
            int maxRows = s.getMaxRows ();
            failed("Didn't throw SQLException but got "+maxRows);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setMaxRows() - Pass a negative value.  Should throw
    an exception.
    **/
    public void Var013()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.setMaxRows (-5);
            failed("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setMaxRows() - Pass a valid value and verify that
    max rows is honored.
    **/
    public void Var014()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.setMaxRows (4);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int rows = 0;
            while(rs.next ())
                ++rows;
            rs.close ();
            s.close ();
            assertCondition (rows == 4);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    public void Var015()
    {
	// Moved to JDStatementQueryTimeout
	assertCondition(true); 
    }



    public void Var016()
    {
	// Moved to JDStatementQueryTimeout
	assertCondition(true); 

    }



    public void Var017()
    {
	// Moved to JDStatementQueryTimeout
	assertCondition(true); 
    }



    public void Var018()
    {
	// Moved to JDStatementQueryTimeout
	assertCondition(true); 
    }



    public void Var019()
    {
	// Moved to JDStatementQueryTimeout
	assertCondition(true); 
    }



    public void Var020()
    {
	// Moved to JDStatementQueryTimeout
	assertCondition(true); 
    }



    public void Var021()
    {
	// Moved to JDStatementQueryTimeout
	assertCondition(true); 

    }



    /**
    cancel() - Cancel a statement that has executed nothing.
    **/
    public void Var022()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.cancel ();
            s.close ();
            succeeded ();
        }
        catch(SQLException e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    cancel() - Cancel a statement that has executed an update
    statement.
    **/
    public void Var023()
    {
        try
        {
            Statement s = connection_.createStatement ();
            try
            {
                s.executeUpdate ("CREATE TABLE " + JDStatementTest.COLLECTION + ".NOTATABLE");
            }
            catch(SQLException e)
            {
                // Ignore.
            }
            s.cancel ();
            try
            {
                s.executeUpdate ("DROP TABLE " + JDStatementTest.COLLECTION + ".NOTATABLE");
            }
            catch(SQLException e)
            {
                // Ignore.
            }
            s.close ();
            assertCondition (true);
        }
        catch(SQLException e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    cancel() - Cancel a statement that has executed an query statement
    and closed the cursor.
    **/
    public void Var024()
    {
        try
        {
            Statement s = connection3_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QSYS2.SYSPROCS");
            while(rs.next())
                ;
            s.cancel ();
            s.close ();
            assertCondition (true);

        }
        catch(SQLException e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    cancel() - Cancel a long running statement from another thread.
             - Use a different connection so that it runs slower. 
    **/
    public void Var025()
    {

        if (proxy_ != null && (!proxy_.equals(""))) {
            notApplicable("Do not test cancel with proxy driver");
            return; 
        }


        if (getRelease() <= JDTestDriver.RELEASE_V5R4M0) { 
          notApplicable("Not working in V5R3/V5R4 after testcase fix");
	  return; 
        }
        
        try
        {

            
            
            class AuxThread extends Thread
            {
                private Statement s_;
                private boolean success_ = false;
		private boolean done_ = false;
		private String[]  expected_ ; 
		private String message = ""; 
                private int[] sqlcode_ ;
                private String query_="";
                private long queryTime_ = -1; 
                public AuxThread(Statement s, String[] expected, int[] sqlcode, String query )
                {
                    s_ = s;
		    expected_ = expected;
                    sqlcode_ = sqlcode;
                    query_ = query; 
                }
		public String getMessage() {
		    return message;
		} 
                public boolean getSuccess()
                {
                    return success_;
                }

                public boolean getDone()
                {
                    return done_;
                }

                public long getQueryTime() {
                   return queryTime_; 
                }
                public void run()
                {
                    try
                    {
                      Thread.currentThread().setName("QueryRunThread");
                        queryTime_ = -1;  
			message+="\nrunning query "+query_+"\n";
                        long startTime = System.currentTimeMillis(); 
                        ResultSet rs = s_.executeQuery(query_ /* slow_ */ );
                        long endTime = System.currentTimeMillis(); 

			message+="\nclosing result set\n"; 
                        rs.close();
			message+="\nQuery finished and result set closed for query \n"+query_+"\n"+
                         "in "+(endTime-startTime)+" ms\n";
                        queryTime_ = (endTime-startTime); 
                    }
                    catch(SQLException e)
                    {
			success_ = false;
			for (int i = 0 ; i < expected_.length; i++) { 
			   if (( e.toString().indexOf(expected_[i]) >= 0 ) && 
			       (e.getErrorCode() == sqlcode_[i])) {
			       success_ = true;
			   } else {

			    message += "Caught " +e.toString()+" Expecting '"+expected_[i]+"' sqlcode="+e.getErrorCode()+" sb "+sqlcode_[i]+"\n";
			   }
			} /* for i */
                    }
                    catch(Exception e)
                    {
			success_ = false;
			for (int i = 0 ; i < expected_.length; i++) { 

			    if ( e.toString().indexOf(expected_[i]) >= 0 ) {
				success_ = true;
			    } else { 
				message += "Caught " +e.toString()+" Expecting '"+expected_[i]+"'\n";
			    }
			} /* for */ 
                    }
		    done_ = true; 
                }
            }
            
            Statement s1;
            
            String query = "SELECT a.table_name  "+
            "FROM QSYS2.SYSTABLES a,  "+
            "     QSYS2.SYSVIEWS b,   "+
            "     QSYS2.SYSCOLUMNS c, "+
            "     QSYS2.SYSINDEXES d, " +
            "     QSYS2.SYSTABLES e,  " +
            "     QSYS2.SYSVIEWS f,   " +
            "     QSYS2.SYSCOLUMNS g, " +
            "     QSYS2.SYSCOLUMNS h, " +
            "     QSYS2.SYSCOLUMNS i, " +
            "     QSYS2.SYSCOLUMNS j  ";
            char letter = 'k'; 
            boolean retry = true; 
            boolean done = false; 
            boolean success = false; 
            AuxThread t = null; 
            while (retry) {
              s1 = connection2_.createStatement ();
              retry = false;
	      String[] message = new String[2]; 
              message[0] = "Processing of the SQL statement ended";
	      message[1] = "Operation cancelled"; 
	      int[] sqlcode = new int[2];
              sqlcode[0] = -952;
	      sqlcode[1] = -99999; 
              if   (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
                t = new AuxThread(s1, message, sqlcode, query  +  " optimize for all rows");
              } else { 
                t = new AuxThread(s1, message, sqlcode, query);
              }

              t.start();

              Thread.sleep(1000);    // Give the thread a chance to execute the statement.
              s1.cancel();
	      Thread.sleep(1000);    // Give the thread a chance to end 
              s1.close();
	      done = t.getDone();
	      success = t.getSuccess();
              long queryTime = t.getQueryTime(); 
              if (queryTime>=0 && queryTime <= 2500) { 
                // Query ran too fast.. Retry
                retry = true; 
                query += ", QSYS2.SYSCOLUMNS "+letter; 
                letter++; 
              }
            }
            
            String threadMessage =""; 
            if (t != null) threadMessage = t.getMessage(); 
            assertCondition( done &&  success, 
                "Cancel did not work done = "+ done+
                " success = "+success+
                "\n modified 7/7/05 by native driver to verify that cancel worked message="+
                threadMessage);
            // It would be good to verify that an exception was thrown
            // (i.e. t.getSuccess() == true), but I have not yet found
            // a reliable way to force that.
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    cancel() - Cancel a closed statement.  Should throw an exception.
    **/
    public void Var026()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.close ();
            s.cancel ();
            failed("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    toString() - Returns the name of the statement.
    **/
    public void Var027()
    {
        try
        {
            Statement s = connection_.createStatement ();
            String name = s.toString ();
            s.close ();
            assertCondition (name != null, "Changed 06/21/2006 to actually check name");
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
    toString() - Returns the name of the statement when the statement
    is closed.
    **/
    public void Var028()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.close ();
            String name = s.toString ();
            assertCondition (name != null, "Changed 06/21/2006 to actually check name");
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    SQL statement too long -- RLE cannot compress

    In V5R4 the native driver will process the longer statements.  For V5R4 make the
    statements even larger to cause the error to occur. 
    **/
    public void Var029()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R2M0) {
	    Statement s = null;
	    ResultSet rs = null;
	    try
	    {
		s = connection_.createStatement ();       
		StringBuffer query = new StringBuffer("Select Parm_int, Parm_Date ");
		int maxCount = 32800;
	    // For V5R4M0 make the statement larger than 2 Meg 
		if (getRelease() >=  JDTestDriver.RELEASE_V5R4M0) {
		    maxCount = 2 * 1024 * 1024;
		}
		for(int i=0; i< maxCount; i++)
		    query.append(i%10);

		query.append(" from types1");         

		rs  = s.executeQuery(query.toString());

		rs.close();
		s.close ();     
		s = null;
		rs = null;
		failed("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		e.printStackTrace(); 
	    // @G1 Changes for v5r3 Cli issue the error now
		// AS of 08/04 the CLI will also issue the error in V5R2 
		if((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getRelease() >=  JDTestDriver.RELEASE_V5R2M0))
		{
		    if(e instanceof SQLException)
		    {
			String message = e.getMessage().toUpperCase();
			if(message.indexOf("ERROR OCCURRED IN SQL CALL LEVEL INTERFACE") >= 0)
			    succeeded();
			else if (message.indexOf("HY009") >= 0) {
			    // 
			    // In V5R5 CLI will issue the error if the message is too long
			    // 
			    succeeded(); 
			} 
			else
			    failed("Wrong message.  Received " + message + " expected ERROR OCCURRED IN SQL CALL LEVEL INTERFACE ");
		    }
		    else
			failed(e, "wrong exception");
		}
		else if(getDriver () == JDTestDriver.DRIVER_NATIVE)
		{
		    if(e instanceof SQLException)
		    {
			String message = e.getMessage().toUpperCase();
			if(message.indexOf("TOO LONG") >= 0)	
			    succeeded();
			else
			    failed("Wrong message.  Received " + message + " expected SQL statement too long or complex");
		    }
		    else
			failed(e, "wrong exception");
		}
		else
		{
		    if(exceptionIs(e, "SQLException"))
		    {
			String message = e.getMessage().toUpperCase();
			if(message.indexOf("TOO LONG") >= 0)
			    succeeded();
			else
			    failed("Wrong message.  Received " + message + " expected SQL statement too long or complex");
		    }
		    else
			failed(e, "wrong exception");   
		}
	    }

	    try
	    {
		if(rs != null)
		    rs.close();

		if(s != null)
		    s.close();
	    }
	    catch(Exception e)
	    {
	    }
	} else {
	    notApplicable("V5R2 or later testcase");
	} 
    }


    /**
    SQL statement too long -- will RLE compress
    **/
    public void Var030()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R2M0) {

	    Statement s = null;
	    ResultSet rs = null;
	    try
	    {
		s = connection_.createStatement ();       
		StringBuffer query = new StringBuffer("Select Parm_int, Parm_Date ");

		int maxCount = 32800;
	    // For V5R4M0 make the statement larger than 2 Meg 
		if (getRelease() >=  JDTestDriver.RELEASE_V5R4M0) {
		    maxCount = 2 * 1024 * 1024;
		}

		for(int i=0; i<maxCount; i++)
		    query.append(" ");          

		query.append(" from types1");         

		int queryLength = query.length(); 
		System.out.println("Var030 Running queryLength= "+
				   queryLength+" : " +
				   query.substring(0,30)+"..."+
				   query.substring(queryLength-30)); 
		rs  = s.executeQuery(query.toString());

		rs.close();
		s.close ();     
		s = null;
		rs = null;
		failed("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
	    // @G1 Changes for v5r3 Cli issue the error now
		if ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getRelease() >=  JDTestDriver.RELEASE_V5R3M0))
		{
		    if(e instanceof SQLException)
		    {
			String message = e.getMessage().toUpperCase();
			if(message.indexOf("ERROR OCCURRED IN SQL CALL LEVEL INTERFACE") >= 0)
			    succeeded();
			else if (message.indexOf("HY009") >= 0) {
			    succeeded(); 
			} 
			else
			    failed("Wrong message.  Received " + message + " expected SQL statement too long or complex");
		    }
		    else
			failed(e, "wrong exception");
		}

		else if(getDriver () == JDTestDriver.DRIVER_NATIVE)
		{
		    if(e instanceof SQLException)
		    {
			String message = e.getMessage().toUpperCase();
			if(message.indexOf("TOO LONG") >= 0)
			    succeeded();
			else
			    failed("Wrong message.  Received " + message + " expected SQL statement too long or complex");
		    }
		    else
			failed(e, "wrong exception");
		}
		else
		{
		    if(exceptionIs(e, "SQLException"))
		    {
			String message = e.getMessage().toUpperCase();
			if(message.indexOf("TOO LONG") >= 0)
			    succeeded();
			else
			    failed("Wrong message.  Received " + message + " expected SQL statement too long or complex");
		    }
		    else
			failed(e, "wrong exception");   
		}
	    }
	    try
	    {
		if(rs != null)
		    rs.close();

		if(s != null)
		    s.close();
	    }
	    catch(Exception e)
	    {
	    }
	} else {
	    notApplicable("V5R2 or later testcase");
	} 

    }


    /**
    Parse comments.  Test nested comment fix for Jim Flanagan.  Old behavior was  
    java.sql.SQLException: [SQL0104] TOKEN / WAS NOT VALID. VALID TOKENS: FROM INTO.  
    **/
    public void Var031()
    {
        try
        {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT /* /* NESTED COMMENT */ */ * FROM QIWS.QCUSTCDT");
            // Before this fix, the statement came out as "SELECT     */  * FROM QIWS.QCUSTCDT" 

            int rows = 0;
            while(rs.next ())
                ++rows;
            rs.close ();
            s.close ();
            assertCondition (rows == 12);
        }
        catch(SQLException e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    Parse comments.  Test nested comment fix for Jim Flanagan.  Old behavior was
    java.sql.SQLException: [SQL0104] TOKEN * WAS NOT VALID. VALID TOKENS: ( <IDENTIFIER>. 
    **/
    public void Var032()
    {
        try
        {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM /* /* NESTED COMMENT */ */ QIWS.QCUSTCDT");
            // Before this fix, the statement came out as "SELECT * FROM     */  QIWS.QCUSTCDT" 

            int rows = 0;
            while(rs.next ())
                ++rows;
            rs.close ();
            s.close ();
            assertCondition (rows == 12);
        }
        catch(SQLException e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    Parse comments.  Test nested comment fix for Jim Flanagan.  Old behavior was 
    java.sql.SQLException: [SQL0104] TOKEN / WAS NOT VALID. VALID TOKENS: FROM INTO.
    **/
    public void Var033()
    {
        try
        {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT /* /* NESTED COMMENT */ */ * /* /* NESTED COMMENT */ */ FROM /* /* NESTED COMMENT */ */ QIWS.QCUSTCDT /* /* NESTED COMMENT */ */");
            // Before this fix, the statement came out as "SELECT     */   *     */   FROM     */  QIWS.QCUSTCDT     */  " 

            int rows = 0;
            while(rs.next ())
                ++rows;
            rs.close ();
            s.close ();
            assertCondition (rows == 12);
        }
        catch(SQLException e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    Parse comments.  Test preceding comments to see if result set
    was returned.
    **/
    public void Var034()
    {
        try
        {
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("/* Preceding comment */ SELECT * FROM QIWS.QCUSTCDT");

            if(rs == null)
            {
                failed("ResultSet was null");
            }
            else
            {
                int rows = 0;
                while(rs.next())
                    ++rows;
                rs.close();
                s.close();
                if(rows == 12)
                {
                    succeeded();
                }
                else
                {
                    System.out.println("Row count is "+rows);    /*@F2A*/
                    failed("Row count != 12.. Warning...Earlier version of testcase did not detect failure"); 
                } 
            }
        }
        catch(SQLException e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    Parse comments.  Test nested block comments to see if result set
    was returned.
    **/
    public void Var035()
    {
        try
        {
            Statement s = connection_.createStatement();
            s.execute("SELECT /* nested /*block*/ comment */ * FROM QIWS.QCUSTCDT");
            ResultSet rs = s.getResultSet();

            if(rs == null)
            {
                failed("ResultSet was null");
            }
            else
            {
                int rows = 0;
                while(rs.next())
                    ++rows;
                rs.close();
                s.close();
                if(rows == 12)
                {
                    succeeded();
                }
                else
                {
                    System.out.println("Row count is "+rows);    /*@F2A*/
                    failed("Row count != 12.. Warning...Earlier version of testcase did not detect failure"); 
                } 
            }
        }
        catch(SQLException e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    Parse comments.  Test mixed comments to make sure
    ResultSet is returned.
    **/
    public void Var036()
    {
        try
        {
            Statement s = connection_.createStatement();
            s.execute("/*Preceding comment*/SELECT\n*\nFROM--line comment /*with block comment*/\nQIWS.QCUSTCDT--end of line comment");
            ResultSet rs = s.getResultSet();

            if(rs == null)
            {
                failed("ResultSet was null");
            }
            else
            {
                int rows = 0;
                while(rs.next())
                    ++rows;
                rs.close();
                s.close();
                if(rows == 12)
                {
                    succeeded();
                }
                else
                {
                    System.out.println("Row count is "+rows);    /*@F2A*/
                    failed("Row count != 12.. Warning...Earlier version of testcase did not detect failure"); 
                } 

            }
        }
        catch(SQLException e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    Parse comments.  Test to see if comment inside of literal
    returns a ResultSet.
    @F2C... This will not return any rows since the comment is inside the literal
    **/
    public void Var037()
    {
        try
        {
            Statement s = connection_.createStatement();
            s.execute("SELECT * FROM QIWS.QCUSTCDT WHERE LSTNAM = '/*comment*/Alison'");
            ResultSet rs = s.getResultSet();

            if(rs == null)
            {
                failed("ResultSet was null");
            }
            else
            {
                int rows = 0;
                while(rs.next())
                    ++rows;
                rs.close();
                s.close();
                if(rows == 0)
                {    /*@F2C*/ 
                    succeeded();
                }
                else
                {
                    System.out.println("Row count is "+rows);    /*@F2A*/
                    failed("Row count != 0.. Warning...Earlier version of testcase did not detect failure"); 
                } 


            }
        }
        catch(SQLException e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    Parse comments.  Test nested mixed comments.
    **/
    public void Var038()
    {
        try
        {
            Statement s = connection_.createStatement();
            s.execute("/*preceeding /*nested*/ comment*/SELECT /* mixed block comment --end of line comment\n */ * FROM QIWS.QCUSTCDT");
            ResultSet rs = s.getResultSet();

            if(rs == null)
            {
                failed("ResultSet was null");
            }
            else
            {
                int rows = 0;
                while(rs.next())
                    ++rows;
                rs.close();
                s.close();
                if(rows == 12)
                {
                    succeeded();
                }
                else
                {
                    System.out.println("Row count is "+rows);    /*@F2A*/
                    failed("Row count != 12.. Warning...Earlier version of testcase did not detect failure"); 
                } 
            }
        }
        catch(SQLException e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    public void Var039()
    {
	// Moved to JDStatementQueryTimeout
	assertCondition(true); 

    }

    /**
    create table
    maximum precision = 63
    maximum scale = 63
    **/
    public void Var040()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=63;maximum scale=63", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_6363 NUMERIC(63,63),");
                sql.append("F_NUMERIC_6333 NUMERIC(63,33),");
                sql.append("F_NUMERIC_6331 NUMERIC(63,31),");
                sql.append("F_NUMERIC_632 NUMERIC(63,2),");
                sql.append("F_NUMERIC_630 NUMERIC(63,0),");
                sql.append("F_NUMERIC_3131 NUMERIC(31,31),");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_6363 DECIMAL(63,63),");
                sql.append("F_DECIMAL_6333 DECIMAL(63,33),");
                sql.append("F_DECIMAL_6331 DECIMAL(63,31),");
                sql.append("F_DECIMAL_632 DECIMAL(63,2),");
                sql.append("F_DECIMAL_630 DECIMAL(63,0),");
                sql.append("F_DECIMAL_3131 DECIMAL(31,31),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 63
    maximum scale = 33
    **/
    public void Var041()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=63;maximum scale=33", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_6333 NUMERIC(63,33),");
                sql.append("F_NUMERIC_6331 NUMERIC(63,31),");
                sql.append("F_NUMERIC_632 NUMERIC(63,2),");
                sql.append("F_NUMERIC_630 NUMERIC(63,0),");
                sql.append("F_NUMERIC_3131 NUMERIC(31,31),");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_6333 DECIMAL(63,33),");
                sql.append("F_DECIMAL_6331 DECIMAL(63,31),");
                sql.append("F_DECIMAL_632 DECIMAL(63,2),");
                sql.append("F_DECIMAL_630 DECIMAL(63,0),");
                sql.append("F_DECIMAL_3131 DECIMAL(31,31),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 63
    maximum scale = 31
    **/
    public void Var042()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=63;maximum scale=31", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_6331 NUMERIC(63,31),");
                sql.append("F_NUMERIC_632 NUMERIC(63,2),");
                sql.append("F_NUMERIC_630 NUMERIC(63,0),");
                sql.append("F_NUMERIC_3131 NUMERIC(31,31),");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_6331 DECIMAL(63,31),");
                sql.append("F_DECIMAL_632 DECIMAL(63,2),");
                sql.append("F_DECIMAL_630 DECIMAL(63,0),");
                sql.append("F_DECIMAL_3131 DECIMAL(31,31),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 
                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 63
    maximum scale = 2
    **/
    public void Var043()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=63;maximum scale=2", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_632 NUMERIC(63,2),");
                sql.append("F_NUMERIC_630 NUMERIC(63,0),");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_632 DECIMAL(63,2),");
                sql.append("F_DECIMAL_630 DECIMAL(63,0),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 
                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 63
    maxium scale = 0
    **/
    public void Var044()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=63;maximum scale=0", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_630 NUMERIC(63,0),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_630 DECIMAL(63,0),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 
                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 31
    maximum scale = 63
    **/
    public void Var045()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31;maximum scale=63", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_3131 NUMERIC(31,31),");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_3131 DECIMAL(31,31),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                // should not see an Exception because the driver should change the maximum scale value to 31
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 
                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 31
    maximum scale = 33
    **/
    public void Var046()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31;maximum scale=33", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();
		try {
		    statement.executeUpdate("DROP TABLE "+ LARGE_PRECISION_TABLE); 
		} catch (Exception e) {

		} 
                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_3131 NUMERIC(31,31),");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_3131 DECIMAL(31,31),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                // should not see an Exception because the driver should change maximum scale to 31
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 31
    maximum scale = 31
    **/
    public void Var047()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31;maximum scale=31", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();
		try {
		    statement.executeUpdate("DROP TABLE "+ LARGE_PRECISION_TABLE); 
		} catch (Exception e) {

		} 


                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_3131 NUMERIC(31,31),");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_3131 DECIMAL(31,31),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 
                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 31
    maximum scale = 2
    **/
    public void Var048()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31;maximum scale=2", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 31
    maxium scale = 0
    **/
    public void Var049()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31;maximum scale=0", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 31
    maximum scale = 63
    columns with invalid scale/precision
    **/
    public void Var050()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31;maximum scale=63", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_3163 NUMERIC(31,63),");
                sql.append("F_NUMERIC_3133 NUMERIC(31,33),");
                sql.append("F_NUMERIC_3131 NUMERIC(31,31),");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_3163 DECIMAL(31,63),");
                sql.append("F_DECIMAL_3133 DECIMAL(31,33),");
                sql.append("F_DECIMAL_3131 DECIMAL(31,31),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                failed("Did not throw Exception.");
            }
            catch(SQLException e)
            {
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Wrong type of Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 31
    maximum scale = 33
    columns with invalid scale/precision
    **/
    public void Var051()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31;maximum scale=33", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_3133 NUMERIC(31,33),");
                sql.append("F_NUMERIC_3131 NUMERIC(31,31),");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_3133 DECIMAL(31,33),");
                sql.append("F_DECIMAL_3131 DECIMAL(31,31),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                failed("Did not throw Exception.");
            }
            catch(SQLException e)
            {
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Wrong type of Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 31
    maximum scale = 31
    columns with invalid scale/precision
    **/
    public void Var052()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31;maximum scale=31", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_1531 NUMERIC(15,31),");
                sql.append("F_NUMERIC_1515 NUMERIC(15,15),");
                sql.append("F_DECIMAL_1531 DECIMAL(15,31),");
                sql.append("F_DECIMAL_1515 DECIMAL(15,15)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                failed("Did not throw Exception.");
            }
            catch(SQLException e)
            {
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Wrong type of Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 31
    maximum scale = 2
    columns with invalid scale/precision
    **/
    public void Var053()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31;maximum scale=2", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_315 NUMERIC(31,5),");
                sql.append("F_NUMERIC_312 NUMERIC(31,2),");
                sql.append("F_NUMERIC_310 NUMERIC(31,0),");
                sql.append("F_DECIMAL_315 DECIMAL(31,5),");
                sql.append("F_DECIMAL_312 DECIMAL(31,2),");
                sql.append("F_DECIMAL_310 DECIMAL(31,0)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded(); // should not get an Exception on CREATE TABLE no matter what URL props are set to
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    create table
    maximum precision = 31
    maxium scale = 0
    columns with invalid scale/precision
    **/
    public void Var054()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31;maximum scale=0", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("F_NUMERIC_315 NUMERIC(31,5),");
                sql.append("F_DECIMAL_315 DECIMAL(31,5)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
     * divide scale minimum divide scale = 3 maximum precision = 31 maximum
     * scale = 18
     */
  public void Var055() {
    if (checkLargeDecimalPrecisionSupport()) {
      Connection connection = null;
      try {
        connection = testDriver_.getConnection(baseURL_
            + ";minimum divide scale=0;maximum precision=63;maximum scale=63",
            userId_, encryptedPassword_);
        Statement statement = connection.createStatement();

        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE ");
        sql.append(LARGE_PRECISION_TABLE);
        sql.append(" (");
        sql.append("MARKER CHAR(1),");
        sql.append("COL1 DECIMAL(10,2),");
        sql.append("COL2 NUMERIC(31,10),");
        sql.append("COL3 NUMERIC(63,0),");
        sql.append("COL4 DECIMAL(45,5),");
        sql.append("COL5 DECIMAL(50,20),");
        sql.append("COL6 NUMERIC(63,63)");
        sql.append(")");

        // try to create the table
        statement.execute(sql.toString());

        sql = new StringBuffer();
        sql.append("INSERT INTO ");
        sql.append(LARGE_PRECISION_TABLE);
        sql
            .append(" (MARKER, COL1, COL2, COL3, COL4, COL5, COL6) VALUES (?,?,?,?,?,?,?)");
        PreparedStatement ps = connection.prepareStatement(sql.toString());
        ps.setString(1, "A");
        ps.setString(2, "66666666.66");
        ps.setString(3, "555555555555555555555.5555555555");
        ps.setString(4,
            "444444444444444444444444444444444444444444444444444444444444444");
        ps.setString(5, "3333333333333333333333333333333333333333.33333");
        ps.setString(6, "222222222222222222222222222222.22222222222222222222");
        ps
            .setString(7,
                "0.111111111111111111111111111111111111111111111111111111111111111");
        ps.addBatch();
        ps.setString(1, "B");
        ps.setString(2, "11111111.11");
        ps.setString(3, "222222222222222222222.2222222222");
        ps.setString(4,
            "333333333333333333333333333333333333333333333333333333333333333");
        ps.setString(5, "4444444444444444444444444444444444444444.44444");
        ps.setString(6, "555555555555555555555555555555.55555555555555555555");
        ps
            .setString(7,
                "0.666666666666666666666666666666666666666666666666666666666666666");
        ps.addBatch();
        ps.executeBatch();
        ps.close();

        // reset the connection and statement to use the correct parms
        statement.close();
        connection.close();
        connection = testDriver_.getConnection(baseURL_
            + ";minimum divide scale=3;maximum precision=31;maximum scale=18",
            userId_, encryptedPassword_);
        statement = connection.createStatement();

        // perform test
        ResultSet rs = statement.executeQuery("SELECT COL6 / COL1 FROM "
            + LARGE_PRECISION_TABLE + " WHERE MARKER ='A'");
        boolean success = rs.next();
        if (success) {
          if (rs.getBigDecimal(1).compareTo(
              new BigDecimal("0.000000001666666666")) == 0) {
            succeeded();
          } else {
            failed(rs.getString(1) + " != 0.000000001666666666");
          }
        } else {
          failed("No results returned from query");
        }
        rs.close();
        statement.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        if (connection != null) {
          try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
            statement.close();
          } catch (SQLException sqlx) {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

          }
          try {
            connection.close();
          } catch (SQLException sqlx) {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

          }
        }
      }
    }
  }

    /**
     * divide scale minimum divide scale = 5 maximum precision = 63 maximum
     * scale = 63
     */
    public void Var056()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";minimum divide scale=0;maximum precision=63;maximum scale=63", userId_, encryptedPassword_);
                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("MARKER CHAR(1),");
                sql.append("COL1 DECIMAL(10,2),");
                sql.append("COL2 NUMERIC(31,10),");
                sql.append("COL3 NUMERIC(63,0),");
                sql.append("COL4 DECIMAL(45,5),");
                sql.append("COL5 DECIMAL(50,20),");
                sql.append("COL6 NUMERIC(63,63)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                sql = new StringBuffer();
                sql.append("INSERT INTO ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (MARKER, COL1, COL2, COL3, COL4, COL5, COL6) VALUES (?,?,?,?,?,?,?)");
                PreparedStatement ps = connection.prepareStatement(sql.toString());
                ps.setString(1, "A");
                ps.setString(2, "66666666.66");
                ps.setString(3, "555555555555555555555.5555555555");
                ps.setString(4, "444444444444444444444444444444444444444444444444444444444444444");
                ps.setString(5, "3333333333333333333333333333333333333333.33333");
                ps.setString(6, "222222222222222222222222222222.22222222222222222222");
                ps.setString(7, "0.111111111111111111111111111111111111111111111111111111111111111");
                ps.addBatch();
                ps.setString(1, "B");
                ps.setString(2, "11111111.11");
                ps.setString(3, "222222222222222222222.2222222222");
                ps.setString(4, "333333333333333333333333333333333333333333333333333333333333333");
                ps.setString(5, "4444444444444444444444444444444444444444.44444");
                ps.setString(6, "555555555555555555555555555555.55555555555555555555");
                ps.setString(7, "0.666666666666666666666666666666666666666666666666666666666666666");
                ps.addBatch();
                ps.executeBatch();
                ps.close();

                // reset the connection and statement to use the correct parms
                statement.close();
                connection.close();
                connection = testDriver_.getConnection(baseURL_ + ";minimum divide scale=5;maximum precision=63;maximum scale=63", userId_, encryptedPassword_);
                statement = connection.createStatement();

                // perform test
                ResultSet rs = statement.executeQuery("SELECT COL6 / COL1 FROM " + LARGE_PRECISION_TABLE + " WHERE MARKER ='A'");
                boolean success = rs.next();
                if (success) { 
                if(rs.getBigDecimal(1).compareTo(new BigDecimal("0.0000000016666666668333333333500000000016666666668333333333500")) == 0)
                {
                    succeeded();
                }
                else
                {
                    failed(rs.getString(1) + " != 0.0000000016666666668333333333500000000016666666668333333333500");
                }
                } else {
                  failed("No results returned from query"); 
                }
                rs.close();
                statement.close();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    divide scale
    minimum divide scale = 2
    maximum precision = 75
    maximum scale = 20
    **/
    public void Var057()
    {
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";minimum divide scale=0;maximum precision=63;maximum scale=63", userId_, encryptedPassword_);
                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("MARKER CHAR(1),");
                sql.append("COL1 DECIMAL(10,2),");
                sql.append("COL2 NUMERIC(31,10),");
                sql.append("COL3 NUMERIC(63,0),");
                sql.append("COL4 DECIMAL(45,5),");
                sql.append("COL5 DECIMAL(50,20),");
                sql.append("COL6 NUMERIC(63,63)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                sql = new StringBuffer();
                sql.append("INSERT INTO ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (MARKER, COL1, COL2, COL3, COL4, COL5, COL6) VALUES (?,?,?,?,?,?,?)");
                PreparedStatement ps = connection.prepareStatement(sql.toString());
                ps.setString(1, "A");
                ps.setString(2, "66666666.66");
                ps.setString(3, "555555555555555555555.5555555555");
                ps.setString(4, "444444444444444444444444444444444444444444444444444444444444444");
                ps.setString(5, "3333333333333333333333333333333333333333.33333");
                ps.setString(6, "222222222222222222222222222222.22222222222222222222");
                ps.setString(7, "0.111111111111111111111111111111111111111111111111111111111111111");
                ps.addBatch();
                ps.setString(1, "B");
                ps.setString(2, "11111111.11");
                ps.setString(3, "222222222222222222222.2222222222");
                ps.setString(4, "333333333333333333333333333333333333333333333333333333333333333");
                ps.setString(5, "4444444444444444444444444444444444444444.44444");
                ps.setString(6, "555555555555555555555555555555.55555555555555555555");
                ps.setString(7, "0.666666666666666666666666666666666666666666666666666666666666666");
                ps.addBatch();
                ps.executeBatch();
                ps.close();

                // reset the connection and statement to use the correct parms
                statement.close();
                connection.close();
                connection = testDriver_.getConnection(baseURL_ + ";minimum divide scale=2;maximum precision=75;maximum scale=20", userId_, encryptedPassword_);
                statement = connection.createStatement();

                // perform test
                ResultSet rs = statement.executeQuery("SELECT COL6 / COL1 FROM " + LARGE_PRECISION_TABLE + " WHERE MARKER ='A'");
                boolean success = rs.next();
                if (success) { 
                if(rs.getBigDecimal(1).compareTo(new BigDecimal("0.00000000166666666683")) == 0)
                {
                    succeeded();
                }
                else
                {
                    failed(rs.getString(1) + " != 0.00000000166666666683");
                }
                } else {
                    failed("No results from query"); 
                }
                rs.close();
                statement.close();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    /**
    divide scale
    minimum divide scale = 9
    maximum precision = 31
    maximum scale = 65
    **/
    /*
       @G2 - maximum scale is defined to be something starting from 0 to maximum precision.
       In this case, ms exceeds mp and so it should throw an error
    */
    public void Var058()
    {
	if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	    getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Causes SQL0901 before V7R2 -- see issue 45200");
	    return; 
	} 

        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";minimum divide scale=0;maximum precision=63;maximum scale=63", userId_, encryptedPassword_);
                Statement statement = connection.createStatement();



                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (");
                sql.append("MARKER CHAR(1),");
                sql.append("COL1 DECIMAL(10,2),");
                sql.append("COL2 NUMERIC(31,10),");
                sql.append("COL3 NUMERIC(63,0),");
                sql.append("COL4 DECIMAL(45,5),");
                sql.append("COL5 DECIMAL(50,20),");
                sql.append("COL6 NUMERIC(63,63)");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                sql = new StringBuffer();
                sql.append("INSERT INTO ");
                sql.append(LARGE_PRECISION_TABLE);
                sql.append(" (MARKER, COL1, COL2, COL3, COL4, COL5, COL6) VALUES (?,?,?,?,?,?,?)");
                PreparedStatement ps = connection.prepareStatement(sql.toString());
                ps.setString(1, "A");
                ps.setString(2, "66666666.66");
                ps.setString(3, "555555555555555555555.5555555555");
                ps.setString(4, "444444444444444444444444444444444444444444444444444444444444444");
                ps.setString(5, "3333333333333333333333333333333333333333.33333");
                ps.setString(6, "222222222222222222222222222222.22222222222222222222");
                ps.setString(7, "0.111111111111111111111111111111111111111111111111111111111111111");
                ps.addBatch();
                ps.setString(1, "B");
                ps.setString(2, "11111111.11");
                ps.setString(3, "222222222222222222222.2222222222");
                ps.setString(4, "333333333333333333333333333333333333333333333333333333333333333");
                ps.setString(5, "4444444444444444444444444444444444444444.44444");
                ps.setString(6, "555555555555555555555555555555.55555555555555555555");
                ps.setString(7, "0.666666666666666666666666666666666666666666666666666666666666666");
                ps.addBatch();
                ps.executeBatch();
                ps.close();

                // reset the connection and statement to use the correct parms
                statement.close();
                connection.close();

                // Need to use the pwrSysUserID_ userid because             @I3A
                // CHGQRYA QRYTIMLMT requires that the user have *JOBCTL    @I3A
                // authority (beginning in V6R1)                            @I3A
                // The DB host server has the *JOBCTL reqmt                 @I3A
                connection = testDriver_.getConnection(baseURL_ + ";minimum divide scale=9;maximum precision=31;maximum scale=65", pwrSysUserID_, pwrSysEncryptedPassword_);//@I3C
                statement = connection.createStatement();

		//
		// Set the job so that  QIBM_SQL_SERVICE is not set.  For native, this
                // test causes an SQL0901 which causes group test to hang if QIBM_SQL_SERVICE is set
		//

		try {
		    statement.executeUpdate("CALL QSYS.QCMDEXC("+
                                       "'CHGENVVAR ENVVAR(QIBM_SQL_SERVICE) VALUE(X)            ',"+
                                       " 0000000050.00000 )");
		} catch (Exception e) {
                  if (DEBUG) {
                    System.out.println("EXCEPTION CHANGING ENVIRONMENT VARIABLE");
                    e.printStackTrace();
                  }
		} 

		try {
		    // CHGQRYA must be used to pick up the new settings 
		    statement.executeUpdate("CALL QSYS.QCMDEXC("+
                                       "'CHGQRYA QRYTIMLMT(200000)                                           ',"+
                                       " 0000000040.00000 )");
		} catch (Exception e) {
                     e.printStackTrace(); 
		} 



                // perform test
                ResultSet rs = statement.executeQuery("SELECT COL6, COL1, COL6 / COL1 FROM " + LARGE_PRECISION_TABLE + " WHERE MARKER ='A'");
                boolean success = rs.next();
		String stringAnswer = rs.getString(1)+"/"+rs.getString(2)+"="+rs.getString(3); 


		/* Updated 09/20/2013 for V7R2 -- Native driver returns same values as toolbox driver */ 
		if (success) { 
		    if(rs.getBigDecimal(3).compareTo(new BigDecimal("0.0000000016666666668333333333500")) == 0)
			succeeded();
		    else
			failed(rs.getString(3) + " != 0.00000000166666666683");
		} else {
		    failed("No results from query stringAnswer="+stringAnswer); 
		}


                rs.close();
                statement.close();
            }
            catch(Exception e)
            {
		if(getDriver() == JDTestDriver.DRIVER_NATIVE)		// @G2
		    succeeded();					// @G2
		else							// @G2
		    failed(e, "Unexpected Exception.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_PRECISION_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
    }

    //@H1A
    /**
    create table with a 128 byte column name
    **/
    public void Var059()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0)
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_COLUMN_NAME_TABLE);
                sql.append(" (");
                sql.append("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INTEGER");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.  Added by Toolbox 8/11/2004 to test 128 byte column name.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_COLUMN_NAME_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
        else{
            notApplicable("V5R4 or greater variation.");
        }
    }

    //@H1A
    /**
    create table with more than one 128 byte column name
    **/
    public void Var060()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0)
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_COLUMN_NAME_TABLE);
                sql.append(" (");
                sql.append("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INTEGER, ");
                sql.append("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQTWO INTEGER, ");
                sql.append("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOTHREE INTEGER");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                succeeded();
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.  Added by Toolbox 8/11/2004 TO TEST 128 BYTE COLUMN NAMES.");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_COLUMN_NAME_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
        else{
            notApplicable("V5R4 or greater variation.");
        }
    }

    //@H1A
    /**
    create table with a column name that is more than 128 bytes.  Should throw an exception.
    **/
    public void Var061()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R4M0)
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE ");
                sql.append(LARGE_COLUMN_NAME_TABLE);
                sql.append(" (");
                sql.append("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRSTUVWXYZ INTEGER");
                sql.append(")");

                // try to create the table
                statement.execute(sql.toString());

                statement.close();
                failed("Didn't throw SQLException.  Added by Toolbox 8/11/2004 to test 128 byte column names");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
            finally
            {
                if(connection != null)
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.execute("DROP TABLE " + LARGE_COLUMN_NAME_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                    try
                    {
                        connection.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
        else{
            notApplicable("V5R4 or greater variation.");
        }
    }


    /**
     * test isClosed when statement is open.  Should return true
     *
     * @since 1.6 (JDBC 4.0)
     */ 
    public void Var062()
    {
	if (checkJdbc40()) { 
	    try
	    {
		Statement s = connection_.createStatement ();
		s.executeQuery ("SELECT {fn concat(CITY,STATE)} FROM QIWS.QCUSTCDT");
		boolean closed = JDReflectionUtil.callMethod_B(s,"isClosed"); 
		s.close ();
		assertCondition (closed==false, "isClosed returned true sb true");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
     * test isClosed when statement is closed .  Should return false 
     */ 
    public void Var063()
    {
	if (checkJdbc40()) { 
	    try
	    {
		Statement s = connection_.createStatement ();
		s.executeQuery ("SELECT {fn concat(CITY,STATE)} FROM QIWS.QCUSTCDT");
		s.close ();
		boolean closed = JDReflectionUtil.callMethod_B(s,"isClosed"); 
		assertCondition (closed==true, "isClosed returned false sb true");

	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
     * test isClosed when connection has been closed.  Should return false.
     */ 
    public void Var064()
    {
	if (checkJdbc40()) { 
	    try
	    {
		Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
		  Statement s = c.createStatement ();
		s.executeQuery ("SELECT {fn concat(CITY,STATE)} FROM QIWS.QCUSTCDT");
		c.close();
		boolean closed = JDReflectionUtil.callMethod_B(s,"isClosed"); 
		assertCondition (closed==true, "isClosed returned false sb true after connection is closed");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
     * test isPoolable when not set.  Should return false for statement 
     *
     * @since 1.6 (JDBC 4.0)
     */ 
    public void Var065()
    {
	if (checkJdbc40()) { 
	    try
	    {
		Statement s = connection_.createStatement ();
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable"); 
		s.close ();
		assertCondition (poolable==false, "isPoolable returned true sb false");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    isPoolable() - isPoolable on a closed statement.  Should throw an exception.
    **/
    public void Var066()
    {
	if (checkJdbc40()) { 
	    try
	    {
		Statement s = connection_.createStatement ();
		s.close ();
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable"); 
		failed("Didn't throw SQLException" +poolable );
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

    /**
     * setPoolable() Set true and check 
     *
     * @since 1.6 (JDBC 4.0)
     */ 
    public void Var067()
    {
	if (checkJdbc40()) { 
	    try
	    {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_V(s,"setPoolable", true); 
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable"); 
		s.close ();
		assertCondition (poolable ==true, "isPoolable returned false sb true");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
     * setPoolable() Set false and check 
     *
     * @since 1.6 (JDBC 4.0)
     */ 
    public void Var068()
    {
	if (checkJdbc40()) { 
	    try
	    {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_V(s,"setPoolable", false); 
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable"); 
		s.close ();
		assertCondition (poolable==false, "isPoolable returned true sb false");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    isPoolable() - isPoolable on a closed statement.  Should throw an exception.
    **/
    public void Var069()
    {
	if (checkJdbc40()) {
	    try
	    {
		Statement s = connection_.createStatement ();
		s.close ();
		JDReflectionUtil.callMethod_V(s,"setPoolable", true); 
		failed("Didn't throw SQLException"  );
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

    //@I2A
    /**
     * test that a query that uses temp storage space under the query storage limit can run.
     */
    public void Var070()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R5M0)
        {               
            Connection c = null;
            try{
		// In December 2017 getting intermittent failures when
                // limit = 1 on 7.2 system.  Bumped limit to 2. 
                c = testDriver_.getConnection(baseURL_ + ";query storage limit=2", userId_, encryptedPassword_);
                //Create the table
                Statement s = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                s.execute("CREATE TABLE " + QUERY_STORAGE_LIMIT_TABLE + " (COL1 VARCHAR(32739))");
                StringBuffer buffer = new StringBuffer (32739);
                int actualLength = 32739 - 2;
                for (int i = 1; i <= actualLength; ++i)
                    buffer.append ("&");
                PreparedStatement ps = c.prepareStatement("INSERT INTO " + QUERY_STORAGE_LIMIT_TABLE + " VALUES(?)");
                for (int j=1; j<20; j++)
                {
                    ps.setString(1, buffer.toString());
                    ps.execute();
                }
                ps.close();

                //execute the query
                ResultSet rs = s.executeQuery("SELECT * FROM " + QUERY_STORAGE_LIMIT_TABLE + " ORDER BY COL1");
                rs.close();
                s.close();
                succeeded();
            }
            catch(Exception e)
            {
		try {

		    Statement statement = c.createStatement();
		    statement.execute("CALL QSYS2.QCMDEXC('DSPJOBLOG OUTPUT(*PRINT)')");
		    statement.execute("CALL QSYS2.QCMDEXC(' DSPJOBLOG OUTPUT(*OUTFILE) OUTFILE(QGPL/JDSMVAR70)      ')");
		    
		    statement.close();
		} catch (Exception e1) {
		    System.out.println("Warning:  cleanup exception");
		    e.printStackTrace(System.out); 

		} 
                failed(e, "Unexpected Exception.  Added by Toolbox 6/29/2006 to test query storage limit support Check QGPL/JDSMVAR70 for job log ");
            }
            finally
            {
                if(c != null)
                {
                    try
                    {
                        Statement statement = c.createStatement();
                        statement.execute("DROP TABLE " + QUERY_STORAGE_LIMIT_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
                        System.out.println("EXCEPTION DELETING TABLE");
                        sqlx.printStackTrace();
                    }
                    try
                    {
                        c.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
        else
            notApplicable("V5R5 or higher variation");
    }

    //@I2A
    /**
     * test that a query that uses temp storage space above the query storage limit throws an exception.
     */
    public void Var071()
    {
	if (getDriver() == JDTestDriver.DRIVER_NATIVE ) {
	    notApplicable("TOOLBOX TEST for 'query storage limit' connection property");
	    return; 
	} 
        if(getRelease() >= JDTestDriver.RELEASE_V5R5M0)
        {
            Connection c = null;
            try{
              // Need to use the pwrSysUserID_ userid because             @I3A
              // query storage limit requires that the user have *JOBCTL  @I3A
              // authority (beginning in V6R1)                            @I3A
              // The DB host server has the *JOBCTL reqmt                 @I3A
                c = testDriver_.getConnection(baseURL_ + ";query storage limit=1", pwrSysUserID_, 
                    pwrSysEncryptedPassword_); //@I3C
                //Create the table
                Statement s = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                s.execute("CREATE TABLE " + QUERY_STORAGE_LIMIT_TABLE + " (COL1 VARCHAR(32739))");
                StringBuffer buffer = new StringBuffer (32739);
                int actualLength = 32739 - 2;
                for (int i = 1; i <= actualLength; ++i)
                    buffer.append ("&");
                PreparedStatement ps = c.prepareStatement("INSERT INTO " + QUERY_STORAGE_LIMIT_TABLE + " VALUES(?)");
                for (int j=1; j<50; j++)
                {
                    ps.setString(1, buffer.toString());
                    ps.execute();
                }
                ps.close();

                //execute the query
                ResultSet rs = null;
                //33923 - v5r5 optimizer is smarter...
                if (getRelease() >= JDTestDriver.RELEASE_V5R5M0)
                    rs = s.executeQuery("SELECT * FROM " + QUERY_STORAGE_LIMIT_TABLE + " ORDER BY COL1 optimize for all rows");
                else
                    rs = s.executeQuery("SELECT * FROM " + QUERY_STORAGE_LIMIT_TABLE + " ORDER BY COL1");
                rs.close();
                s.close();
                
                if (getRelease() >= JDTestDriver.RELEASE_V5R5M0 && isToolboxDriver())
                {
                    succeeded(); //in v6r1 now, this does not take up enough space to error out
                    return;
                }
                else
                    failed("Didn't throw SQLException.  Added by Toolbox 6/29/2006 to test query storage limit support");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
            finally
            {
                if(c != null)
                {
                    try
                    {
                        Statement statement = c.createStatement();
                        statement.execute("DROP TABLE " + QUERY_STORAGE_LIMIT_TABLE);
                        statement.close();
                    }
                    catch(SQLException sqlx)
                    {
                        System.out.println("EXCEPTION DELETING TABLE");
                        sqlx.printStackTrace();
                    }
                    try
                    {
                        c.close();
                    }
                    catch(SQLException sqlx)
                    {
			System.out.println("Warning:  error during cleanup"); 
			sqlx.printStackTrace(System.out); 

                    }
                }
            }
        }
        else
            notApplicable("V5R5 or higher variation");
    }

    

    /**
     VALUES sql  - v5r5
    **/
    public void Var072()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R5M0)
        {
            try
            {
                Statement s = connection_.createStatement ();
                
                ResultSet rs = s.executeQuery("values ('a', 'b', 'c')");
                rs.next();
                String val1 = rs.getString(1);
                String val2 = rs.getString(2);
                String val3 = rs.getString(3);
                assertCondition (val1.equals("a") && val2.equals("b") && val3.equals("c"), "VALUES sql returned incorrect values");
                
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.  Added by Toolbox 02/21/2007");
            }
        }
        else{
            notApplicable("V5R5 or greater variation.");
        }
    }



    /**
     VALUES sql  - v5r5 - multiple rows
    **/
    public void Var073()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R5M0)
        {
            try
            {
                Statement s = connection_.createStatement ();
                
                ResultSet rs = s.executeQuery("select * from (VALUES 1,2,3) as vr");
                
                rs.next();
                String val1 = rs.getString(1);
                rs.next();
                String val2 = rs.getString(1);
                rs.next();
                String val3 = rs.getString(1);
                 
                
                assertCondition (val1.equals("1") && val2.equals("2") && val3.equals("3"), "VALUES sql returned incorrect values");
                
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.  Added by Toolbox 02/21/2007");
            }
        }
        else{
            notApplicable("V5R5 or greater variation.");
        }
    }




    /**
     Execute sql statement with lots of text 
    **/
    public void Var074()
    {
	int length = 0;
	String sql = ""; 
	try
	{
	    if (getRelease() <= JDTestDriver.RELEASE_V5R3M0 && isToolboxDriver())
        {
            notApplicable("Too complex for v5r3");
            return;
        }


	if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	    getRelease() <= JDTestDriver.RELEASE_V5R3M0) {
	    notApplicable("Native fails in V5R3, need CLI header fix");
            return; 
	} 

	    String longProc = JDStatementTest.COLLECTION + ".JDSMLONG";
	    try {
		Statement s = connection_.createStatement ();
		sql = "DROP PROCEDURE " + longProc; 
		s.executeUpdate(sql) ;

	    } catch (Exception e) {
	    } 

	    String before = " CREATE PROCEDURE " + longProc + " (IN INDATE DATE, OUT OUTDATE DATE) LANGUAGE SQL BEGIN SET OUTDATE = DATE(DAYS(INDATE)+1) ;"; 
	    String after = " END ";
	    int[]  testLength = { 1000,10000,100000,1000000,1048576  };
	    String char100 = "                                                                                                    "; 
	    for (int i = 0; i < testLength.length; i++) {
		System.out.println("Testing "+testLength[i]); 
		StringBuffer sb = new StringBuffer(0);
		sb.append(before);
		length = before.length() + after.length();
		while ( length < testLength[i]) {
		    if (testLength[i] - length >= 100) {
			sb.append(char100);
			length += 100; 
		    } else {
			while ( length < testLength[i]) {
			    sb.append(" ");
			    length++; 
			}
		    } 
		}
		sb.append(after); 
		

		Statement s = connection_.createStatement ();
		sql = sb.toString();
		s.executeUpdate(sql);

		sql = "DROP PROCEDURE " + longProc; 
		s.executeUpdate(sql) ;
		s.close(); 
		
	    } 

	    assertCondition (true); 

	}
	catch(Exception e)
	{
	    String printSql = sql;
	    if (printSql.length()> 100) {
		printSql=printSql.substring(0,100)+"..."; 
	    } 
	    failed(e, "Unexpected Exception. Length="+length+"/"+sql.length()+" for SQL'"+printSql+"'  Added by Native 6/23/2008");
	}
    }



    /**
     MERGE STATEMENT sql  - v7R1
    **/
    public void Var075()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        {
            try
            {
                String t1 = JDStatementTest.COLLECTION + ".MERGE1";
                String t2 = JDStatementTest.COLLECTION + ".MERGE2";
                
                Statement st = connection_.createStatement();
                try{ st.execute("drop table " + t1); }catch(Exception e){ }
                try{ st.execute("create table "+ t1 + " (col1 varchar(10))");}catch(Exception e){ }
                try{ st.execute("drop table " + t2); }catch(Exception e){ }
                try{ st.execute("create table "+ t2 + " (col1 varchar(10))");}catch(Exception e){ }
               
                
                st.execute("MERGE INTO " +  t1 + " ar " +
                        " USING " + t2 + " ac " +
                        " ON ( ar.col1 = ac.col1 ) " +
                        " WHEN MATCHED THEN " +
                        " UPDATE SET ar.COL1 = 'a'");
               
                assertCondition (true);
                
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.  Added by Toolbox 06/22/2009");
            }
        }
        else{
            notApplicable("V7R1 or greater variation.");
        }
    }



    public void Var076()
    {
	// Moved to JDStatementQueryTimeout
	assertCondition(true); 

        
    }

    /**
    test specifying only maximum precision.
    default scale is 31... 
    Fails in native driver prior to V7R2 -- See issue 45200
    **/
    public void Var077()
    {
	String info = " -- Test specifying only maximum precision.  Defualt scale should be 31"; 
	if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	    getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in native V7R1 and earlier");
	    return; 
	} 

	
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";maximum precision=31", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

		ResultSet rs = statement.executeQuery("select CAST(1 AS DECIMAL(9,2))/3 from sysibm.sysdummy1"); 
		rs.next();
		String value = rs.getString(1);

                statement.close();
		String expected = "0.333333333333333333333333" ; 
		assertCondition(value.equals(expected), "Got "+value+" expected "+expected+info);
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception.");
            }
        }
    }



    /**
    test specifying only minimum divide scale
    Fails in native driver prior to V7R2 -- See issue 45200
    **/
    public void Var078()
    {
	String info = " -- Test specifying only minimum divide scale"; 

	if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	    getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in native V7R1 and earlier");
	    return; 
	} 
        if(checkLargeDecimalPrecisionSupport())
        {
            Connection connection = null;
            try
            {
                connection = testDriver_.getConnection(baseURL_ + ";minimum divide scale=3", userId_, encryptedPassword_);

                Statement statement = connection.createStatement();

		ResultSet rs = statement.executeQuery("select CAST(1 AS DECIMAL(9,2))/3 from sysibm.sysdummy1"); 
		rs.next();
		String value = rs.getString(1);

                statement.close();
		String expected = "0.333333333333333333333333" ; 
		assertCondition(value.equals(expected), "Got "+value+" expected "+expected+info );
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception."+info );
            }
        }
    }


    /**
    test querying binary data after CHAR data.  See issue 46937
    **/
    public void Var079()
    {
	StringBuffer sb = new StringBuffer();
	sb.append(" -- Test querying binary data after char data -- native issue 46937 \n"); ; 
	boolean passed = true; 
	Connection connection = null;
	try   {
	    connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
		
	    Statement statement = connection.createStatement();

	    String sql = "select "+
	      "cast('ABC' as CHAR(10)), "+
	      "cast('ABC' as GRAPHIC(10) CCSID 1200), "+
	      "cast(null as GRAPHIC(10)), "+
	      "cast('ABC' as VARCHAR(10)), "+
	      "cast('ABC' as VARGRAPHIC(10) CCSID 1200), "+
	      "cast(null as VARGRAPHIC(10)) "+
	      "from sysibm.sysdummy1";
	    sb.append("Executing "+sql+"\n"); 
	    ResultSet rs = statement.executeQuery(sql); 
	    rs.next();
	    String[] expected = {
		"",	
		"ABC       ",
		"ABC       ",
		"null", 
		"ABC", 
		"ABC", 
		"null"
	    };
	    for (int i = 1; i <= 6; i++) {
		String value = rs.getString(i);
		if (!expected[i].equals(""+value)) {
		    sb.append("Got "+value+" expected "+expected[i]+"\n");
		    passed =false; 
		}
	    }
	    rs.close(); 
	    

	    sql = "select cast(BX'010203' AS BINARY(10)), "+
	      "cast (BX'010203' AS VARBINARY(20)), "+
	      "cast(null AS BINARY(10)), "+
	      "cast (null AS VARBINARY(20)) "+
	      "from sysibm.sysdummy1";
	    sb.append("Executing "+sql+"\n");
	    try {
		rs = statement.executeQuery(sql);
	    } catch (SQLException ex ) {
		sql = "select cast(X'010203' AS BINARY(10)), "+
		  "cast (X'010203' AS VARBINARY(20)), "+
		  "cast(null AS BINARY(10)), "+
		  "cast (null AS VARBINARY(20)) "+
		  "from sysibm.sysdummy1";
		sb.append("Executing "+sql+"\n");
		rs = statement.executeQuery(sql);
	    } 

	    rs.next();
	    String[] expectedBytes = {
		"",	
		"01020300000000000000",
		"010203",
		"null", 
		"null"
	    };
	    for (int i = 1; i <= 4; i++) {
		byte[] bytesValue = rs.getBytes(i);
		String value = bytesToString(bytesValue); 
		if (!expectedBytes[i].equals(value)) {
		    sb.append("Got "+value+" expected "+expectedBytes[i]+"\n");
		    passed =false; 
		}
	    }

	    rs.close();
	    statement.close();
	    connection.close(); 


	    assertCondition(passed,sb);
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected Exception."+sb.toString() );
	}
    }


    /**
    test querying binary data after CHAR data.  See issue 46937
    **/
    public void Var080()
    {
	StringBuffer sb = new StringBuffer();
	sb.append(" -- Test querying binary data after char data -- native issue 46937 \n"); ; 
	boolean passed = true; 
	Connection connection = null;
	try   {
	    connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
		
	    Statement statement = connection.createStatement();

	    String sql = "select "+
	      "cast('ABC' as CHAR(10) CCSID 1208 ), "+
	      "cast('ABC' as GRAPHIC(10) CCSID 13488), "+
	      "cast(null as GRAPHIC(10)), "+
	      "cast('ABC' as VARCHAR(10) CCSID 1208), "+
	      "cast('ABC' as VARGRAPHIC(10) CCSID 13488), "+
	      "cast(null as VARGRAPHIC(10)) "+
	      "from sysibm.sysdummy1";
	    sb.append("Executing "+sql+"\n"); 
	    ResultSet rs = statement.executeQuery(sql); 
	    rs.next();
	    String[] expected = {
		"",	
		"ABC       ",
		"ABC       ",
		"null", 
		"ABC", 
		"ABC", 
		"null"
	    };
	    for (int i = 1; i <= 6; i++) {
		String value = rs.getString(i);
		if (!expected[i].equals(""+value)) {
		    sb.append("Got "+value+" expected "+expected[i]+"\n");
		    passed =false; 
		}
	    }
	    rs.close(); 
	    

	    sql = "select cast(BX'010203' AS BINARY(10)), "+
	      "cast (BX'010203' AS VARBINARY(20)), "+
	      "cast(null AS BINARY(10)), "+
	      "cast (null AS VARBINARY(20)) "+
	      "from sysibm.sysdummy1";
	    sb.append("Executing "+sql+"\n");
	    try { 
		rs = statement.executeQuery(sql);
	    } catch (SQLException ex) {
		sql = "select cast(X'010203' AS BINARY(10)), "+
		  "cast (X'010203' AS VARBINARY(20)), "+
		  "cast(null AS BINARY(10)), "+
		  "cast (null AS VARBINARY(20)) "+
		  "from sysibm.sysdummy1";
		sb.append("Executing "+sql+"\n");
		rs = statement.executeQuery(sql);
	    } 
	    rs.next();
	    String[] expectedBytes = {
		"",	
		"01020300000000000000",
		"010203",
		"null", 
		"null"
	    };
	    for (int i = 1; i <= 4; i++) {
		byte[] bytesValue = rs.getBytes(i);
		String value = bytesToString(bytesValue); 
		if (!expectedBytes[i].equals(value)) {
		    sb.append("Got "+value+" expected "+expectedBytes[i]+"\n");
		    passed =false; 
		}
	    }

	    rs.close();
	    statement.close();
	    connection.close(); 


	    assertCondition(passed,sb);
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected Exception."+sb.toString() );
	}
    }


    /**
    test fetch error after combined open fetch.  See issue 46618.
    **/
    public void Var081()
    {
	if (checkRelease720()) { 
	    StringBuffer sb = new StringBuffer();
	    sb.append(" -- Test fetch error after combined open fetch -- toolbox  issue 46618 -- need both toolbox fix and fix from john broich \n"); ; 
	    boolean passed = true; 
	    Connection connection = null;
	    try   {
		connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

		Statement statement = connection.createStatement();
		Statement dropStatement = connection.createStatement();
		String tablename = JDStatementTest.COLLECTION+".JDSM81";
		String sql; 

		try {
		    sql = "DROP TABLE "+tablename; 
		    sb.append("Executing "+sql+"\n");
		    statement.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append("Caught exception on drop "+e.toString()+"\n"); 
		    String eMessage = e.toString().toUpperCase();
		    if (eMessage.indexOf("NOT FOUND") < 0) {
			System.out.println("Warning:  unexpected exception on drop");
			e.printStackTrace(); 
		    } 
		} 



		sql = "CREATE TABLE "+tablename+"(doc xml)"; 
		sb.append("Executing "+sql+"\n");
		statement.executeUpdate(sql);


		sql = "INSERT INTO  "+tablename+" values('<string>   <string1>12.567732</string1> <string2>Hello world!</string2> <string3>He said \"Hi!\"</string3> <string4>Another string</string4> <string5>Peter''s house</string5> <string6>The expression 5+4 &lt; 12e+1 but &gt; 2.01. The operator AND is written as &amp;&amp;.</string6> <string7>John Smith''s son has a name called \"Nimo\" but his friends call him ''Shrek''</string7> <string8>Escaping &amp; Or Not Escaping</string8> <string9>?99.50</string9> <string10>euro symbol: ?</string10> <string11>''</string11> <string12>\"</string12> <string13>\"\"</string13> </string>')"; 
		sb.append("Executing "+sql+"\n");
		statement.executeUpdate(sql);
    
		/* Note:  This syntax only works on v7r2 */ 
		sql = "SELECT * FROM "+tablename+" t, XMLTABLE ('$D/string/string11[. = '']' passing doc as D ) AS X";
		sb.append("Executing "+sql+"\n");
		ResultSet rs = null;
		try { 
		    rs = statement.executeQuery(sql);
		    sb.append("Result set returned\n"); 
		    rs.next();
		} catch (Exception e) {
		    sb.append("Exception caught "+e.toString()+"\n"); 
		}
		if (rs != null) {
		    sb.append("Closing result set \n");
		    rs.close();
		} else {
		    sb.append("Warning:  No result set to close\n"); 
		} 


		sql = "DROP TABLE "+tablename; 
		sb.append("Executing "+sql+"\n");
		dropStatement.executeUpdate(sql);

		statement.close();
		dropStatement.close(); 
		connection.close(); 


		assertCondition(passed,sb);
	    }
	    catch(Exception e)
	    {
		failed(e, "Unexpected Exception."+sb.toString() );
	    }

	} /* check release */
    } /* var081 */ 





    /**
    setLargeMaxRows() - Pass a negative value.  Should throw
    an exception.
    **/
    public void Var082()
    {
	if (checkJdbc42()) {
	    try
	    {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_V(s,"setLargeMaxRows", (long) -5);
		failed("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


    /**
    setLargeMaxRows() - Pass a valid value and verify that
    max rows is honored.
    **/
    public void Var083()
    {
	if (checkJdbc42()) {
        try
        {
            Statement s = connection_.createStatement ();
            JDReflectionUtil.callMethod_V(s,"setLargeMaxRows", (long) 4);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int rows = 0;
            while(rs.next ())
                ++rows;
            rs.close ();
            s.close ();
            assertCondition (rows == 4);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }
    }
    /**
    setLargeMaxRows() - Pass a huge value.  Should truncated it to 
    to zero. 
    **/
    public void Var084()
    {
	if (checkJdbc42()) {
	    try
	    {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_V(s,"setLargeMaxRows", 0x100000000L);
    long largeMaxRows = JDReflectionUtil.callMethod_L(s,"getLargeMaxRows");
    int maxRows = s.getMaxRows(); 
    
    s.close ();
    assertCondition (largeMaxRows == 0x100000000L && maxRows == 0, "got maxLargeRows="+Long.toHexString(largeMaxRows)+" sb 0x100000000L"+
    "gotMaxRows="+maxRows+"sb 0");
    
}
catch(Exception e)
{
    failed (e, "Unexpected Exception");
}
	}
    }


    /**
    getLargeMaxRows() - Verify that the returned value is 0
    by default (unlimited rows).
    **/
    public void Var085()
    {
	if (checkJdbc42()) { 
        try
        {
            Statement s = connection_.createStatement ();
            long maxRows = JDReflectionUtil.callMethod_L(s,"getLargeMaxRows");
            s.close ();
            assertCondition (maxRows == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }
    }



    /**
    getLargeMaxRows() - Verify that the returned value is the
    one that was set.
    **/
    public void Var086()
    {
	if (checkJdbc42()) {
        try
        {
            Statement s = connection_.createStatement ();
            s.setMaxRows (15);
            long maxRows = JDReflectionUtil.callMethod_L(s,"getLargeMaxRows");

            s.close ();
            assertCondition (maxRows == 15);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }
    }


    /**
    getLargeMaxRows() - Should thrown an exception is the
    statement is closed.
    **/
    public void Var087()
    {
	if (checkJdbc42()) {
	    try
	    {
		Statement s = connection_.createStatement ();
		s.close ();
		long maxRows = JDReflectionUtil.callMethod_L(s,"getLargeMaxRows");
		failed("Didn't throw SQLException but got "+maxRows);
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }



}
