///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementQueryTimeout.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementQueryTimeout.java
//
// Classes:      JDStatementQueryTimeout
//
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;


/**
Testcase JDStatementQueryTimeout.  This tests the following method
of the JDBC Statement class:

<ul>
<li>getQueryTimeout()</li>
<li>setQueryTimeout()</li>
</ul>
**/
public class JDStatementQueryTimeout
extends JDTestcase
{


    // Private data.
    private              Connection     connection_;
    private              Connection     connection2_;
    private              Connection     connection3_;
    private              Connection     connectionPwrSys_;        //@I3A
    private              String         slow_;



    /**
    Constructor.
    **/
    public JDStatementQueryTimeout (AS400 systemObject,
                            Hashtable namesAndVars,
                            int runMode,
                            FileOutputStream fileOutputStream,
                            
                            String password,
                            String powerUserID,
                            String powerPassword)
    {
        super (systemObject, "JDStatementQueryTimeout",
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
    Sets up an SQL statement that is pretty much guaranteed to take longer 
    than 1 second.
    
    @exception Exception If an exception occurs.
    **/
    private void initializeSlow ()
    throws Exception
    {
        if(slow_ == null)
        {
            Statement s = connection_.createStatement ();

            // Make sure everything is gone
            //
            for(int i = 1; i <= 26; ++i)
            {
                try
                {
                    s.executeUpdate ("DROP TABLE " + JDStatementTest.COLLECTION
                                     + ".SLOW" + i);
                }
                catch(Exception e)
                {
                }
            }


            for(int i = 1; i <= 26; ++i)
            {

                StringBuffer create = new StringBuffer ("CREATE TABLE " 
                                                        + JDStatementTest.COLLECTION
                                                        + ".SLOW" + i + " (");                
                StringBuffer insert = new StringBuffer ("INSERT INTO " 
                                                        + JDStatementTest.COLLECTION
                                                        + ".SLOW" + i + " (");
                for(int j = 1; j <= 26; ++j)
                {
                    create.append ("COL" + j + " INTEGER, ");
                    insert.append ("COL" + j + ", ");
                }
                create.append ("LASTCOL INTEGER)");
                insert.append ("LASTCOL) VALUES(");
                for(int k = 1; k <= 26; ++k)
                    insert.append ("?, ");
                insert.append ("?)");

                s.executeUpdate (create.toString ());

                PreparedStatement ps = connection_.prepareStatement (insert.toString ());
                for(int m = 1; m <= 26; ++m)
                {
                    for(int n = 1; n <= 26; ++n)
                        ps.setInt (n, m+n+i);              
                    ps.setInt (27, -1);
                    ps.executeUpdate ();
                }
                ps.close ();      
            }

            StringBuffer query = new StringBuffer ("SELECT * FROM ");
            for(int p = 1; p < 26; ++p)
                query.append (JDStatementTest.COLLECTION + ".SLOW" + p + ", ");
            query.append (JDStatementTest.COLLECTION + ".SLOW" + 26);        
            slow_ = query.toString ();
        }
    }



    /**
    setQueryTimeout() - Pass 0 and verify that no timeout occurs.
    **/
    public void Var001()
    {
        try
        {
            initializeSlow ();
            Statement s = connection_.createStatement ();
            s.setQueryTimeout (0);
            s.executeQuery (slow_);

            // Make sure and close this statement handle,
            // because it hogs a lot of resources on the
            // server.
            s.close ();

            succeeded ();
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getQueryTimeout() - Verify that the returned value is 0
    by default (unlimited query time).
    **/
    public void Var002()
    {
        try
        {
            Statement s = connection_.createStatement ();
            int queryTimeout = s.getQueryTimeout ();
            s.close ();
            assertCondition (queryTimeout == 0);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getQueryTimeout() - Verify that the returned value is the
    one that was set.
    **/
    public void Var003()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.setQueryTimeout (2531027);
            int queryTimeout = s.getQueryTimeout ();
            s.close ();
            assertCondition (queryTimeout == 2531027);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getQueryTimeout() - Should thrown an exception is the
    statement is closed.
    **/
    public void Var004()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.close ();
            int queryTimeout = s.getQueryTimeout ();
            failed("Didn't throw SQLException but got "+queryTimeout);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setQueryTimeout() - Pass a negative value.  Should throw
    an exception.
    **/
    public void Var005()
    {
        try
        {
            Statement s = connection_.createStatement ();
            s.setQueryTimeout (-5);
            failed("Didn't throw SQLException");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setQueryTimeout() - Pass a valid value and verify that
    timeout does occur.
    
    SQL400 - Today, the native driver will not honor the timeout value.
             This is something that we are looking into supporting in
             the v4r4 timeframe.
    **/
    public void Var006()
    {
        // Clif (01/20/1999) - I have verified that the query timeout
        //                     is indeed working.  However, on fast
        //                     servers, this variation may fail.  We
        //                     need to come up with a slower query.

        // ODBC uses this to test:
        //
        // SELECT * FROM QSYS2.SYSTABLES, QSYS2.SYSVIEWS, QSYS2.SYSCOLUMNS

        try
        {

            long queryStart = 0;
            long queryEnd = 0;
            char letter = 'e'; 

            String query = "SELECT a.table_name FROM QSYS2.SYSTABLES a, QSYS2.SYSVIEWS, QSYS2.SYSCOLUMNS, QSYS2.SYSINDEXES, QSYS2.SYSCOLUMNS b, QSYS2.SYSCOLUMNS c, QSYS2.SYSCOLUMNS d ";    /* @F1C */ 
            initializeSlow ();

            // Need to use the connectionPwrSys_ connection because @I3A
            // setQueryTimeout requires that the user have *JOBCTL  @I3A
            // authority (beginning in V6R1)                        @I3A
            // The DB host server has the JOBCTL reqmt              @I3A
            Statement s = connectionPwrSys_.createStatement ();   //@I3A

            s.setQueryTimeout (1);             /* 1 second timeout */
	    /* Need to prevent a system reply list entry from causing this*/
            /* to work */
	    s.executeUpdate("CALL QSYS.QCMDEXC('CHGJOB INQMSGRPY(*RQD)                ',0000000030.00000 )"); 
            boolean success = false;
            boolean retry = true; 
            while (retry) {
                retry = false; 
                try
                {
                    queryStart = System.currentTimeMillis();   
                    //@C1D s.executeQuery (slow_);
                    //33923 - v5r5 optimizer is smarter...
                    if   (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
                        s.executeQuery (query +  " optimize for all rows"); 
                    } else { 
                        s.executeQuery (query );    //@C1A@F1C
                    }
                    queryEnd = System.currentTimeMillis();

                    // If query took less than a second and a half, make it bigger and retry
                    if ((queryEnd-queryStart) < 1500) {
			// System.out.println("Query too fast : "+query); 
                        query +=" , QSYS2.SYSCOLUMNS "+letter;
                        letter++;
                        retry = true; 
                    } 
                }
                catch(SQLException e)
                {
                    // check for the right information in the query
                    e.printStackTrace(System.out); 
                    String exceptionMessage = e.toString();
                    String[]  expectedMessage = { "TimeoutException", "Operation cancelled", "Processing of the SQL statement ended", "666" } ; //sql number on all versions
                    // Because of the current toolbox implementation, the query will
                    // not even attempt to run, but will get
		    // [SQL0666] SQL query exceeds specified time limit or storage limit.
		    System.out.println("Got exception: '"+exceptionMessage+"' for query '"+query+"'"); 
                    for (int i = 0; i < expectedMessage.length; i++){
                        if (exceptionMessage.indexOf(expectedMessage[i]) >= 0) { 
                            success = true;
                        }
                    }
                    if (!success) { 
                        for (int i = 0; i < expectedMessage.length; i++){

                            System.out.println("Unable to find "+expectedMessage[i]+" in "+exceptionMessage);
                        }
                        System.out.println("Statement was "+query); 
                        e.printStackTrace();
                    }
                }
            }
            // Make sure and close this statement handle,
            // because it hogs a lot of resources on the
            // server.
            s.close ();
            connection2_.close ();

            //
            // Reopen the connection so it can be used by other variations
            //
            connection2_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);



                if(!success)
                {
                    System.out.println("Query timeout did not work, was query too fast?");
                    System.out.println("Query was "+query); 
                    if(queryEnd != 0)
                    {
                        System.out.println("Query took "+(queryEnd-queryStart)+" milliseconds"); 
                    }
                }
                assertCondition (success);
 

        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    setQueryTimeout() - Pass 0 and verify that no timeout occurs.
    **/
    public void Var007()
    {
        try
        {
            initializeSlow ();
            Statement s = connection_.createStatement ();
            s.setQueryTimeout (0);
            s.executeQuery (slow_);

            // Make sure and close this statement handle,
            // because it hogs a lot of resources on the
            // server.
            s.close ();

            succeeded ();
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }




    /**
     Verify that setQueryTimeout 0 works. 
    **/
    public void Var008()
    {

	if (getDriver() == JDTestDriver.DRIVER_NATIVE ) {
	    System.out.println("Var008 is toolbox only test for QRYTIMLMT");
	    assertCondition(true);
	    return; 
	} 
	StringBuffer sb = new StringBuffer(); 
	try
	{
	    String[] table = {
	       "SYSIBM.SQLCOLUMNS",
	       "SYSIBM.SQLFOREIGNKEYS",
	       "SYSIBM.SQLPRIMARYKEYS",
	       "SYSIBM.SQLPROCEDURECOLS", 
	       "SYSIBM.SQLPROCEDURES", 
	       "SYSIBM.SQLSCHEMAS",
	       "SYSIBM.SQLSPECIALCOLUMNS",
	       "SYSIBM.SQLSTATISTICS", 
	       "SYSIBM.SQLTABLEPRIVILEGES", 
	       "SYSIBM.SQLCOLPRIVILEGES",
	       "SYSIBM.SQLTABLES"
	      
	    }; 
	    
	    sb.append("\n");
	    sb.append("Connect using a job with a low query timeout limit value.\n");

	    Connection connection = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	    Statement stmt = connection.createStatement();
	    stmt.executeUpdate("CALL QSYS.QCMDEXC("+
			       "'CHGQRYA QRYTIMLMT(1)                          ',"+
			       " 0000000040.00000 )");

	    // Make sure the job doesn't use the system reply list.  Otherwise an entry for
            // CPA4269 with I will cause this test to fail.
	    stmt.executeUpdate("CALL QSYS.QCMDEXC('CHGJOB INQMSGRPY(*RQD)                ',0000000030.00000 )"); 


	    sb.append(" Verify that a query timeouts\n"); 
	    String queryFirst =  "select * from sysibm.sqltables A\n";
	    String querySecond = "";
	    String query=""; 
	    int nextTable = 0; 
	    char letter='B';
	    boolean queryFailed =  false;
	    while (!queryFailed) {
		queryFirst = queryFirst+", "+table[nextTable % table.length]+" "+letter;
		
		letter++;
		nextTable++; 
		query=queryFirst+querySecond;
		sb.append("Running "+query+"\n");
		try {
		    long startTime = System.currentTimeMillis(); 
		    ResultSet rs = stmt.executeQuery(query);
		    rs.close();
		    long endTime = System.currentTimeMillis();
		    if (endTime - startTime > 60 * 1000) {
			assertCondition(false, "Error. qrytimlmt is set to 1, but query took "+(endTime-startTime)+" milliseconds");
			return; 
		    } 
		} catch (SQLException e) {
		    /* e.printStackTrace(); */ 
		    queryFailed = true; 
		} 
	    } 

	    sb.append("Set the queryTimeout to zero.\n"); 
            stmt.setQueryTimeout(0); 

	    sb.append("Verify that the query now works.\n"); 
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            assertCondition (true);
                
	} catch(Exception e)
            {
                failed(e, "Unexpected Exception.  Added for Toolbox 12/09/2010"+sb.toString());
            }
        
    }

    public static final int TESTTYPE_STATEMENT_EXECUTE_UPDATE = 1; 
    public static final int TESTTYPE_STATEMENT_EXECUTE        = 2; 
    public static final int TESTTYPE_PREPARED_STATEMENT_EXECUTE_UPDATE = 3; 
    public static final int TESTTYPE_PREPARED_STATEMENT_EXECUTE        = 4;
    public static final int TESTTYPE_CALLABLE_STATEMENT_EXECUTE_UPDATE = 5;
    public static final int TESTTYPE_CALLABLE_STATEMENT_EXECUTE        = 6; 
    
    public void doTestWithUpdate(int variation, int testType, String info) {
      StringBuffer sb = new StringBuffer("\n");

      String added = " --- Added 06/14/2011 to test queryTimeout for "+info;
      String sql = ""; 
      Connection lockingConnection = null;
      Connection updateConnection = null; 
      try {
        boolean passed = true; 
        String tablename = JDStatementTest.COLLECTION+".JDSTMTSQ"+variation ; 
         lockingConnection =  testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
         lockingConnection.setAutoCommit(false); 
         lockingConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); 

         Statement lockingStatement = lockingConnection.createStatement(); 
         try {
           sql = "drop table "+tablename; 
           lockingStatement.executeUpdate(sql); 
         } catch (Exception e) {    
         }
         sql = "create table "+tablename+" (C1 INT, C2 INT)"; 
         lockingStatement.executeUpdate(sql);
         sql = "commit"; 
         lockingConnection.commit(); 
         sql = "insert into "+tablename+" values(1,1)"; 
         lockingStatement.executeUpdate(sql);
         

         updateConnection =  testDriver_.getConnection (baseURL_+";query timeout mechanism=cancel", userId_, encryptedPassword_);
         updateConnection.setAutoCommit(false); 
         updateConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); 
         Statement updateStatement = updateConnection.createStatement(); 
         updateStatement.setQueryTimeout(1); 
         sql = "UPDATE "+tablename+" SET C2=2 WHERE C1=1";
         try { 
             switch (testType) {
               case TESTTYPE_STATEMENT_EXECUTE_UPDATE:
                 updateStatement.executeUpdate(sql);
                 break; 
               case TESTTYPE_STATEMENT_EXECUTE:
                 updateStatement.executeUpdate(sql);
                 break; 
               case TESTTYPE_PREPARED_STATEMENT_EXECUTE_UPDATE:
                 {
                   PreparedStatement ps = updateConnection.prepareStatement(sql); 
                   ps.setQueryTimeout(1); 
                   ps.executeUpdate();
		   ps.close(); 
                 }
                 break; 
               case TESTTYPE_PREPARED_STATEMENT_EXECUTE:
                 {
                   PreparedStatement ps = updateConnection.prepareStatement(sql); 
                   ps.setQueryTimeout(1); 
                   ps.execute();
		   ps.close(); 
                 }
                 break; 


               case TESTTYPE_CALLABLE_STATEMENT_EXECUTE_UPDATE:
               case TESTTYPE_CALLABLE_STATEMENT_EXECUTE:
                 {
		       String procedureName = JDStatementTest.COLLECTION+".JDSQTPROC";
		       String createProcSql = "CREATE PROCEDURE "+JDStatementTest.COLLECTION+".JDSQTPROC() LANGUAGE SQL BEGIN "+sql+"; END";
		       sql = "DROP PROCEDURE "+procedureName;
		       try {
			   updateStatement.executeUpdate(sql); 
		       } catch (Exception e) {
		       }
		       sql = createProcSql;
		       updateStatement.executeUpdate(sql);
		       sql = "CALL "+procedureName+"()"; 
                   CallableStatement cs = updateConnection.prepareCall(sql); 
                   cs.setQueryTimeout(1);
		   if (testType == TESTTYPE_CALLABLE_STATEMENT_EXECUTE_UPDATE) {
		       cs.executeUpdate();
		   } else {
		       cs.execute();
		   }
		   cs.close();
		   sql = "DROP PROCEDURE "+procedureName;
		   updateStatement.executeUpdate(sql); 


                 }
                 break; 

             }
             passed = false; 
             sb.append("Did not get exception on update\n"); 
         } catch (Exception e) { 
           String expectedException = "Processing of the SQL statement ended";
           String exceptionInfo = e.toString(); 
           if (exceptionInfo.indexOf(expectedException)>= 0) {
             passed = true; 
           } else { 
             passed = false; 
             sb.append("Got "+exceptionInfo+" expected "+expectedException+"\n");
	     printStackTraceToStringBuffer(e, sb); 
           }
         }
         updateConnection.close(); 
         lockingConnection.commit(); 
         sql = "drop table "+tablename; 
         lockingStatement.executeUpdate(sql); 
         lockingConnection.close(); 
         
         assertCondition(passed,sb.toString()+added ); 

      } catch(Exception e) {
          failed(e, " sql="+sql+sb.toString()+added);
      }

      
    }
    
   /**
     * Set query timeout and verify that it works with executeUpdate
     */
    public void Var009() { doTestWithUpdate(9,TESTTYPE_STATEMENT_EXECUTE_UPDATE, "executeUpdate"); } 

   /**
    * Set query timeout and verify that it works with execute
    */
    public void Var010() { doTestWithUpdate(10,TESTTYPE_STATEMENT_EXECUTE, "execute"); } 
    
    /**
     * Set query timeout and verify that it works with executeUpdate
     */
    public void Var011() { doTestWithUpdate(11,TESTTYPE_PREPARED_STATEMENT_EXECUTE_UPDATE, "PS.executeUpdate"); } 

   /**
    * Set query timeout and verify that it works with execute
    */
    public void Var012() { doTestWithUpdate(12,TESTTYPE_PREPARED_STATEMENT_EXECUTE, "PS.execute"); } 

   /**
    * Copy of var006 for new toolbox JDBC query timeout mechanism property
    */
    /**
    setQueryTimeout() - Pass a valid value and verify that
    timeout does occur.
    
    **/
    public void Var013()
    {
	StringBuffer sb = new StringBuffer(); 

	if ((!isToolboxDriver())) {
	    notApplicable("Toolbox only test");
	    return; 
	} 
        try
        {

            long queryStart = 0;
            long queryEnd = 0;
            char letter = 'e'; 

            String query = "SELECT a.table_name FROM QSYS2.SYSTABLES a, QSYS2.SYSVIEWS, QSYS2.SYSCOLUMNS, QSYS2.SYSINDEXES, QSYS2.SYSCOLUMNS b ";    /* @F1C */ 
            initializeSlow ();

	    Connection queryConnection =  testDriver_.getConnection (baseURL_+";query timeout mechanism=cancel", userId_, encryptedPassword_);

            Statement s = queryConnection.createStatement ();   //@I3A

            s.setQueryTimeout (1);             /* 1 second timeout */ 
            boolean success = false;
            boolean retry = true; 
            while (retry) {
                retry = false; 
                try
                {
                    queryStart = System.currentTimeMillis();   
                    //@C1D s.executeQuery (slow_);
                    //33923 - v5r5 optimizer is smarter...
                    if   (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
                        s.executeQuery (query +  " optimize for all rows"); 
                    } else { 
                        s.executeQuery (query );    //@C1A@F1C
                    }
                    queryEnd = System.currentTimeMillis();

                    // If query took less than a second and a half, make it bigger and retry
                    if ((queryEnd-queryStart) < 1500) {
			// System.out.println("Query too fast : "+query); 
                        query +=" , QSYS2.SYSCOLUMNS "+letter;
                        letter++;
                        retry = true; 
                    } 
                }
                catch(SQLException e)
                {
                    // check for the right information in the query
                    String exceptionMessage = e.toString();
                    e.printStackTrace(System.out); 
                    String[]  expectedMessage = { "TimeoutException", "Operation cancelled", "Processing of the SQL statement ended" } ; 
		    System.out.println("Got exception: '"+exceptionMessage+"' for query '"+query+"'"); 
                    for (int i = 0; i < expectedMessage.length; i++){
                        if (exceptionMessage.indexOf(expectedMessage[i]) >= 0) { 
                            success = true;
                        }
                    }
                    if (!success) { 
                        for (int i = 0; i < expectedMessage.length; i++){

                            System.out.println("Unable to find "+expectedMessage[i]+" in "+exceptionMessage);
                        }
                        System.out.println("Statement was "+query); 
                        e.printStackTrace();
                    }
                }
            }
            // Make sure and close this statement handle,
            // because it hogs a lot of resources on the
            // server.
            s.close ();
            queryConnection.close ();

                if(!success)
                {
                    System.out.println("Query timeout did not work, was query too fast?");
                    System.out.println("Query was "+query); 
                    if(queryEnd != 0)
                    {
                        System.out.println("Query took "+(queryEnd-queryStart)+" milliseconds"); 
                    }
                }
                assertCondition (success,sb);
 

        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

   /**
    setQueryTimeout() - Pass 0 and verify that no timeout occurs with new toolbox property
    **/
    public void Var014()
    {
	if ((!isToolboxDriver())) {
	    notApplicable("Toolbox only test");
	    return; 
	} 
        try
        {
            initializeSlow ();
	    Connection queryConnection =  testDriver_.getConnection (baseURL_+";query timeout mechanism=cancel", userId_, encryptedPassword_);

            Statement s = queryConnection.createStatement ();
            s.setQueryTimeout (0);
            s.executeQuery (slow_);

            // Make sure and close this statement handle,
            // because it hogs a lot of resources on the
            // server.
            s.close ();

	    queryConnection.close(); 
            succeeded ();
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }








    
}
