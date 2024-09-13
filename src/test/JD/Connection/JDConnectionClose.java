///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionClose.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionClose.java
//
// Classes:      JDConnectionClose
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDJSTPTestcase;
import test.JDJobName;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Hashtable;



/**
Testcase JDConnectionClose.  his tests the following methods
of the JDBC Connection class:

<ul>
<li>close()</li>
<li>isClosed()</li>
</ul>
**/
public class JDConnectionClose
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionClose";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.
    private static String         table_      = JDConnectionTest.COLLECTION + ".JDCCLOSE";



/**
Constructor.
**/
    public JDConnectionClose (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              String pwrSysUserID, 
                              String pwrSysPassword)
    {
        super (systemObject, "JDConnectionClose",
               namesAndVars, runMode, fileOutputStream,
               password,
               pwrSysUserID,
               pwrSysPassword);
    }


/**
Constructor.
**/
    public JDConnectionClose (AS400 systemObject,
			      String testname, 
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              String pwrSysUserID, 
                              String pwrSysPassword)
    {
        super (systemObject, testname,
               namesAndVars, runMode, fileOutputStream,
               password,
               pwrSysUserID,
               pwrSysPassword);
    }



/**
Setup.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        // Create the table.
        table_      = JDConnectionTest.COLLECTION + ".JDCCLOSE";
        Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        Statement s = c.createStatement ();
        try { s.executeUpdate ("DROP TABLE " + table_); } catch (Throwable t) {}
        s.executeUpdate ("CREATE TABLE " + table_ + " (COL1 INT)");
        s.close ();

            JDSetupProcedure.create (systemObject_, c, JDSetupProcedure.STP_CSPARMS,
                                     supportedFeatures_, collection_);
       

        c.commit(); // for xa
        c.close ();
    }



/**
Cleanup.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        // Drop the table.
        Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        Statement s = c.createStatement ();
        s.executeUpdate ("DROP TABLE " + table_);
        s.close ();
        c.commit(); // for xa
        c.close ();
    }



/**
Indicates if a result set is closed.
**/
    private boolean isResultSetClosed (ResultSet rs)
    {
        // Try to get the value of the first column of the
        // next row.  An exception will be thrown if the
        // result set is closed.
        try {
            rs.next ();
            rs.getString (1);
            return false;
        }
        catch (SQLException e) {
            return true;
        }
    }



/**
Indicates if a statement is closed.
**/
    private boolean isStatementClosed (Statement s)
    {
        // Try to get the max field size.  An exception will be
        // thrown if the statement is closed.
        try {
            s.getMaxFieldSize ();
            return false;
        }
        catch (SQLException e) {
            return true;
        }
    }



/**
isClosed() - Should return false on an open connection.
**/
    public void Var001 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            boolean closed = c.isClosed ();
            c.close ();
            assertCondition (!closed);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
isClosed() - Should return true on a closed connection.
**/
    public void Var002 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c.close ();
            boolean closed = c.isClosed ();
            assertCondition (closed);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
close() - Should have no effect when the connection is already closed.
**/
    public void Var003 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c.close ();
            c.close ();
            assertCondition (true);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }




/**
close() - Should close all its statements, but not those of a different connection.
**/
    public void Var004 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);

            Statement s1 = c.createStatement ();
            PreparedStatement s2 = c.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            CallableStatement s3 = c.prepareCall ("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");

            Connection c2 = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);

            Statement sX = c2.createStatement ();

            boolean s1Before = isStatementClosed (s1);
            boolean s2Before = isStatementClosed (s2);
            boolean s3Before = isStatementClosed (s3);
            boolean sXBefore = isStatementClosed (sX);

            c.close ();

            boolean s1After = isStatementClosed (s1);
            boolean s2After = isStatementClosed (s2);
            boolean s3After = isStatementClosed (s3);
            boolean sXAfter = isStatementClosed (sX);

            c2.close ();

	    assertCondition (!s1Before && !s2Before && !s3Before && !sXBefore
			     && s1After && s2After && s3After && !sXAfter, "The following should be false:  "+
			     "\ns1Before="+s1Before+
			     "\ns2Before="+s2Before+
			     "\ns3Before="+s3Before+
			     "\nsXBefore="+sXBefore+
			     "\nsXAfter="+sXAfter+
			     "\nThe following shoud be true"+
			     "\ns1After="+s1After+
			     "\ns2After="+s2After+
			     "\ns3After="+s3After
			     );
	}
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
close() - Should close all its result sets, but not those of a different connection.
**/
    public void Var005 ()
    {
	String info = "Close should close all its result sets, but not those of a different connection.";
        try {

            if (isToolboxDriver())
            {
                notApplicable("Toolbox todo after dust settles from v5r5");
                return;
            }
            Connection c = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);

            Statement s1 = c.createStatement ();
            PreparedStatement s2 = c.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");

            ResultSet rs1 = s1.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            ResultSet rs2 = s2.executeQuery ();

            Connection c2 = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);

            Statement sX = c2.createStatement ();
            ResultSet rsX = sX.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");

            boolean rs1Before = isResultSetClosed (rs1);
            boolean rs2Before = isResultSetClosed (rs2);
            boolean rsXBefore = isResultSetClosed (rsX);

            c.close ();

            boolean rs1After = isResultSetClosed (rs1);
            boolean rs2After = isResultSetClosed (rs2);
            boolean rsXAfter = isResultSetClosed (rsX);

            c2.close ();

            assertCondition (!rs1Before && !rs2Before && !rsXBefore &&
			     rs1After && rs2After && !rsXAfter,
			     info+"\nsb false: rs1Before="+rs1Before+
			     " rs2Before="+rs2Before+
			     " rsXBefore="+rsXBefore+
			     " rsXAfter="+rsXAfter+
			     "\nsb true: rs1After="+rs1After+
			     " rs2After="+rs2After);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
close() - Should rollback a transaction.
**/
    public void Var006 ()
    {
        try {

	    // added the following lines to ensure that rowsBefore indeed evaluates to 1
	    try{									    // @D1
		Connection con = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);  // @D1
		Statement st= con.createStatement();                                        // @D1
		st.executeUpdate(" DELETE FROM " + table_ );                                // @D1
		con.close();                                                                // @D1
	    }catch(Exception ignore){
	    }							    // @D1

            // Add a row with another connection.
            Connection c = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);
            c.setAutoCommit (false);
            c.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
            Statement s = c.createStatement ();

	    int rowsInitial = 0;

	    {            // Count the number of rows before closing.
		ResultSet rs = s.executeQuery ("SELECT * FROM " + table_);
		while (rs.next ())
		    ++rowsInitial;
	    }

            s.executeUpdate ("INSERT INTO " + table_ + " (COL1) VALUES (98)");

            // Count the number of rows before closing.
            int rowsBefore = 0;
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table_);
            while (rs.next ())
                ++rowsBefore;

	    // Rollback the results 
	    // c.rollback(); 
            // Close the connection without committing.
            c.close ();

            // Count the number of rows after closing.
            Connection c2 = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);
	    c2.setAutoCommit (false);
            c2.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            Statement s2 = c2.createStatement ();
            int rowsAfter = 0;
            ResultSet rs2 = s2.executeQuery ("SELECT * FROM " + table_);
            while (rs2.next ())
                ++rowsAfter;
            c2.close ();

            assertCondition ((rowsBefore == 1) && (rowsAfter == 0),"rowsB4 = "+rowsBefore+" rowsAfter = "+rowsAfter+" rowsInitial="+rowsInitial);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }




/**
close() - Check for handle leak..
**/
    public void Var007 ()
    {
	if (checkNative()) {
	    try {

		System.gc();
	    // Determine the beginning statment number
		Connection con = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);
		Statement st= con.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
		String cursorName = rs.getCursorName();
		// System.out.println("cursor name is "+cursorName);
		int beginHandle = Integer.parseInt(cursorName.substring(10));
		rs.close();
		st.close();
		con.close();

	    // Attempt to connect 100 times with a bad user id
		for (int i = 0; i < 100; i++) {
		    try {
			con = testDriver_.getConnection (baseURL_+";errors=full", "BOGUSID", PasswordVault.getEncryptedPassword("BOGUS PASSWORD"));
			con.close();
		    } catch (Exception e) {
		    // Ignore the exception
		    }
		}

	    // Check cursorname again
		con = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);
		st= con.createStatement();
		rs = st.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
		String afterCursorName = rs.getCursorName();
		// System.out.println("after cursor name is "+afterCursorName);
		int afterHandle = Integer.parseInt(afterCursorName.substring(10));
		rs.close();
		st.close();
		con.close();

		assertCondition (afterHandle- beginHandle < 10,
				 " -- Handle leak occurred -- beforeName="+
				 cursorName+" afterName="+afterCursorName+
				 " -- added by Native 01/18/2007 to test handle leak found by CM");
	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}


    }




    public String getJobName(Connection con) {
	String jobName="";
	try {
	    jobName = JDReflectionUtil.callMethod_S(con, "getServerJobName");
	} catch (Exception nsme )  {
	    try {
		jobName = JDReflectionUtil.callMethod_S(con, "getServerJobIdentifier");
				 // Reformat the job name it comes in as QZDASOINITQUSER     364288
		if (jobName.length() >= 26) {
		    jobName = jobName.substring(20).trim()+"/"+jobName.substring(10,20).trim() +"/"+jobName.substring(0,10).trim();
		}

	    } catch (Exception e) {
		try {

		    JDJSTPTestcase.assureGETJOBNAMEisAvailable(con);
		    Statement s = con.createStatement();
		    ResultSet rs = s.executeQuery("select "+JDJSTPTestcase.envLibrary+".GETJOBNAME() from "+
						  "SYSIBM.SYSDUMMY1");
		    rs.next();
		    jobName = rs.getString(1);

		} catch (Exception e2) {

		    e2.printStackTrace();
		    e.printStackTrace();
		    nsme.printStackTrace();

		}
	    }
	}
	return jobName;
    }


/**
Closing of connection when job ends.  Check that the connection does not hang when the server job is ended.  Written to detect problem in PASE sockets for toolbox when job is ended.  See CPS 8BNRBN: HTTP Web admin socket problem for more information.

**/
    public void Var008 ()
    {
	String info = " -- Closing of connection when job ends.  Check that the connection does not hang when the server job is ended.  Written to detect problem in PASE sockets for toolbox when job is ended.  See CPS 8BNRBN: HTTP Web admin socket problem for more information. ";
	int SLEEP_INTERVAL = 500;
	int DIE_SLEEP_TIME = 120000;
	StringBuffer sb = new StringBuffer();
	    try {
		Connection dieConn = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);

		String jobname = getJobName(dieConn);
		System.out.println("Jobname="+jobname+" for "+dieConn);
		JDConnectionCloseRunnable thread = new JDConnectionCloseRunnable(dieConn, "select * from qsys2.syscolumns", sb);
		thread.setDaemon(true);
		thread.start();

		synchronized(sb) {
		    sb.append(mainMarker()+" Creating controlConn using "+baseURL_+" "+pwrSysUserID_);
		}

		Connection controlConn = testDriver_.getConnection (baseURL_+";errors=full", pwrSysUserID_, pwrSysEncryptedPassword_);
		try {
		    Thread.sleep(5000);
		} catch (Exception e) {
		}

		Statement stmt = controlConn.createStatement();
		synchronized(sb) {
		    sb.append(mainMarker()+" Calling ENDJOB \n");
		}
		stmt.executeUpdate("CALL  QSYS.QCMDEXC('ENDJOB JOB("+jobname+") OPTION(*IMMED)                                              ',  0000000080.00000)");
		System.out.println("Calling ENDJOB on "+jobname); 
		synchronized(sb) {
		    sb.append(mainMarker()+" Back from ENDJOB \n");
		}

		int i = 0;
		while( i < (DIE_SLEEP_TIME / SLEEP_INTERVAL) && !thread.getDone() && !thread.getAborted()) {
		    try {
			Thread.sleep(SLEEP_INTERVAL);
		    } catch (Exception e) {
		    }
		    i++;
		}

		synchronized(sb) {
		    sb.append(mainMarker()+" Done waiting i="+i+" about "+(i * SLEEP_INTERVAL)+" ms\n");
		}


		assertCondition (thread.getDone(), "getDone() = false DIE_SLEEP_TIME="+DIE_SLEEP_TIME+"   "+sb+
		    "\n"+info);
	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception "+info);
	    }


    }

	public String mainMarker() {
	    String output;
	    output = "main "+(new Timestamp(System.currentTimeMillis()))+":";
	    return output;
	}


    public class JDConnectionCloseRunnable extends Thread {
  
	Connection c_;
	String query_;
	boolean done_;
	boolean aborted_;
	StringBuffer info_ = new StringBuffer();
	public JDConnectionCloseRunnable(Connection c, String query, StringBuffer info) {
	    c_=c;
	    query_=query;
	    done_ = false;
	    info_ = info;
	    aborted_ = false;
	}
	public boolean getDone() { return done_; }
	public StringBuffer getInfo() { info_.append("\ngetIteration="+getIteration);  return info_; }
        public boolean getAborted() { return aborted_; }
	public int getIteration = 0;

	public String threadMarker() {
	    String output;
	    output = "thread "+(new Timestamp(System.currentTimeMillis()))+":";
	    return output;
	}
	public void run() {

	    try {
		int count = 0;
		synchronized(info_) {
		    info_.append(threadMarker()+"Query="+query_+"\n");
		}
		PreparedStatement ps = c_.prepareStatement(query_);
		    synchronized(info_) {
			info_.append(threadMarker()+"Prepare complete\n");
		    }

		while (true) {
		    count++;
		    synchronized(info_) {
			info_.append(threadMarker()+"Executing "+count+"\n");
		    }
		    ResultSet rs = ps.executeQuery();
		    synchronized(info_) {
			info_.append(threadMarker()+"Execute complete, fetching\n");
		    }

		    getIteration = 0;
		    while (rs.next()) {
			rs.getString(1);
			getIteration++;
			if ((getIteration % 50000) == 0) {
			    synchronized(info_) {
				info_.append(threadMarker()+"getIteration="+getIteration+"\n");
			    }
			}
		    }
		    rs.close();
		    synchronized(info_) {
			info_.append(threadMarker()+"Execute complete, fetch complete\n");
		    }


		}


	    } catch (Throwable  e) {
		try {
		    String [] expectedException = {
			"Communication link failure",
			"Error occurred in SQL Call Level Interface",
			"Connection to relational database",
			"Communication error",
			"java.io.EOFException",
			"Processing of the SQL statement ended",
		    };
		    String exceptionInfo = e.toString();
		// System.out.println("Exception in thread:" + exceptionInfo);
		    for (int i = 0; i < expectedException.length; i++) {
			if (exceptionInfo.indexOf(expectedException[i]) >= 0) {
			    done_ = true;
			}

		    }

		    if (!done_) {
			synchronized(info_) {
			    info_.append("Did not get expected exception....\n");
			    for (int i = 0; i < expectedException.length; i++) {
				info_.append("'"+expectedException[i]+"'\n" ); 
			    }
			    info_.append("Exception was "+exceptionInfo);
			}
			e.printStackTrace();
			aborted_ = true;
		    }
		    try {
			c_.close();
		    } catch (Exception e2) {
		    }
		} catch (Throwable t) {
		    t.printStackTrace();
		}
	    }

	}


    }



/**
Testing closing of connection and use of statements.  See CPS 8D9VP3 and 84PLLH.
Prior to a fix, the garbage collector could be in the process of closing connections while
the main connection is close.  This can lead to all sorts of weird errors.
The solution is to update the closing of the connection to force the finalizers to run.
This testcase should detect this problem.
**/
    public void Var009 ()
    {
	boolean passed = true;

	int CONNECTIONS_TO_TEST = 300;
	int STATEMENTS_PER_CONNECTION = 300;
	if (isToolboxDriver()) {
	    CONNECTIONS_TO_TEST = 100;
	    STATEMENTS_PER_CONNECTION = 100;
	}
	STATEMENTS_PER_CONNECTION = overrideFromProperty("test.JDConnectionClose.STATEMENTS_PER_CONNECTION", STATEMENTS_PER_CONNECTION); 
  CONNECTIONS_TO_TEST = overrideFromProperty("test.JDConnectionClose.CONNECTIONS_TO_TEST", CONNECTIONS_TO_TEST); 

	StringBuffer sb = new StringBuffer();
	try {

	    long lastTime = System.currentTimeMillis() + 120000;
            long nextMessage = System.currentTimeMillis() + 15000;

	    for (int i =  0; i < CONNECTIONS_TO_TEST && System.currentTimeMillis() < lastTime; i++) {
              if (System.currentTimeMillis() > nextMessage) {
                long timeleft = ( lastTime - System.currentTimeMillis()) / 1000;
                String message = "JDConnectionClose.Var009 loop="+i+"/"+CONNECTIONS_TO_TEST+
                " or timeLeft = "+timeleft+"s";
                JDJobName.sendProgramMessage(message );
                System.out.println(message);
                nextMessage = System.currentTimeMillis() + 15000;
              }
		sb.append("Create connection loop "+i+"\n");
		// System.out.println("Create connection loop "+i+"\n");
		Connection conn = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);
		for (int j = 0; j < STATEMENTS_PER_CONNECTION  && System.currentTimeMillis() < lastTime; j++) {
		    PreparedStatement ps = conn.prepareStatement("Select * from sysibm.sysdummy1");
		    ps.executeQuery();
		}
		sb.append("Closing connection\n");
		conn.close();
	    }


	    // We pass if we complete without any exceptions.
	    assertCondition (passed);
	}
	catch (Throwable e) {
	    failed(e, "Unexpected Exception\n "+sb.toString());
	}

    }


   public void leakConnection(StringBuffer sb, int i, int STATEMENTS_PER_CONNECTION ) throws Exception {
	sb.append("Create connection loop "+i+"\n");
		// System.out.println("Create connection loop "+i+"\n");

	Connection conn = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);
	PreparedStatement[] ps = new PreparedStatement[STATEMENTS_PER_CONNECTION];
	for (int j = 0; j < STATEMENTS_PER_CONNECTION; j++) {
	    try { 
		ps[j] = conn.prepareStatement("Select * from sysibm.sysdummy1");
	    } catch (Exception e) {
		String message = e.toString();
		if ((message.indexOf("Error occurred in SQL Call Level Interface") >=  0) &&
		    (message.indexOf("The error code is 14") >= 0 )) {
		    System.out.println("Warning.. out of handles, forcing gc");
		    System.gc();
		    j = STATEMENTS_PER_CONNECTION; 
		    } 
	    } 
	}
	for (int j = 0; j < STATEMENTS_PER_CONNECTION; j++) {
	    try {
		if (ps[j] != null) { 
		    ps[j].executeQuery();
		}
	    } catch (Exception e) {

	    } 
	}
        // Just leak the connection
        // sb.append("Closing connection\n");
        // conn.close();

   }

   public void leakConnectionAndResultSets(StringBuffer sb, int i, int STATEMENTS_PER_CONNECTION ) throws Exception {
	sb.append("Create connection loop "+i+"\n");
		// System.out.println("Create connection loop "+i+"\n");

	Connection conn = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);
	PreparedStatement[] ps = new PreparedStatement[STATEMENTS_PER_CONNECTION];
	ResultSet[] rs = new ResultSet[STATEMENTS_PER_CONNECTION];
	for (int j = 0; j < STATEMENTS_PER_CONNECTION; j++) {
	    try { 
		ps[j] = conn.prepareStatement("Select * from sysibm.sysdummy1");
		rs[j] = ps[j].executeQuery();
		rs[j].next();
		rs[j].next(); 
	    } catch (Exception e) {
		String message = e.toString();
		if ((message.indexOf("Error occurred in SQL Call Level Interface") >=  0) &&
		    (message.indexOf("The error code is 14") >= 0 )) {
		    System.out.println("Warning.. out of handles, forcing gc");
		    System.gc();
		    j = STATEMENTS_PER_CONNECTION; 
		    } 
	    } 
	}
	for (int j = 0; j < STATEMENTS_PER_CONNECTION; j++) {
	    try {
		if (ps[j] != null) { 
		    ps[j].executeQuery();
		}
	    } catch (Exception e) {

	    } 
	}
        // Just leak the connection
        // sb.append("Closing connection\n");
        // conn.close();

   }



    public int overrideFromProperty(String property, int STATEMENTS_PER_CONNECTION) {
	String override = System.getProperty(property);
	if (override != null) {
	    STATEMENTS_PER_CONNECTION = Integer.parseInt(override); 
	    System.out.println("Value "+property+" overridden with "+override); 
	} 
	return STATEMENTS_PER_CONNECTION; 
    } 
    

    // These are now in separate classes since they take longer to run
    public void Var010 () { assertCondition(true,"now in JDConnectionClose10"); }
    // These are now in separate classes since they take longer to run
    public void Var011 () { assertCondition(true,"now in JDConnectionClose11"); }
    // These are now in separate classes since they take longer to run
    public void Var012 () { assertCondition(true,"now in JDConnectionClose12"); }


    // These are now in separate classes since they take longer to run
    public void Var013 () { assertCondition(true,"now in JDConnectionClose13"); }






}



