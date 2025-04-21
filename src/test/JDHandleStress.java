///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDHandleStress.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.*;

import javax.sql.DataSource;

import java.io.PrintStream;
import java.sql.*;



public class JDHandleStress implements Runnable {
    // 1 handle for statement itself, 4 for potentially associated descriptors
    public final static int HANDLES_PER_STATEMENT = 5;

    String threadId_;
    int paramConnections_;
    int paramStatements_;
    int paramPercentStatementOps_;
    int paramPercentAllocOps_;
    int paramRunSeconds_;
    int paramGrowShrinkTime_;
    PrintStream out_ = null;
    Vector<Connection>  connections_;
    Connection currentConnection_;
    Statement  currentStatement_ = null; 
    // Hashtable from connection to Vector of Array[statement, result, statement handle count] on that connection
    Hashtable<Connection, Vector<Object[]>> connectionStatementHashtable_;
    // Hashtable form connection to Integer
    Hashtable<Connection, Integer> connectionStatementMaxCountHashtable_;
    int allocConnectionCount_=0;
    int freeConnectionCount_=0;
    int allocStatementCount_=0;
    int freeStatementCount_=0;
    Random random_ = new Random();
    int exerciseStatementCount_ = 1;
    boolean paused = false;
    boolean failed = false;
    boolean abort = false;
    boolean running = false;
    public JDHandleStress
      (PrintStream out,
       String threadId,
	  int paramConnections,
	  int paramStatements,
	  int paramPercentStatementOps,
	  int paramPercentAllocOps,
	  int paramRunSeconds) throws SQLException {
	out_ = out;
	threadId_ = threadId;
	paramConnections_ = paramConnections;
	paramStatements_ = paramStatements;
	paramPercentStatementOps_ = paramPercentStatementOps;
	paramPercentAllocOps_ =  paramPercentAllocOps;
	paramRunSeconds_ = paramRunSeconds;

	connections_ = new Vector<Connection>();

	connectionStatementHashtable_ = new Hashtable<Connection, Vector<Object[]>>();
	connectionStatementMaxCountHashtable_ = new Hashtable<Connection, Integer>();


    }


    public void run() {
    try {
      out_.println("Thread "+threadId_ +" starting ");
      System.out.println("Thread "+threadId_ +" starting ");
      int choice;
      for (int i = 0; i < paramConnections_; i++) {
        allocConnection();
      }
      long endtime = System.currentTimeMillis() + 1000 * paramRunSeconds_;
      long statusTime = System.currentTimeMillis() + 5000;
      out_.println("Thread " + threadId_ + ": running for " + paramRunSeconds_
          + " seconds");
      long currentTime = System.currentTimeMillis();
      synchronized(this) {
	  running = true;
      }
      boolean localAbort = false;
      while (currentTime < endtime && !localAbort) {
        choice = random_.nextInt(100);
        int percentAllocOps;
        if (isGrowing()) {
          percentAllocOps = paramPercentAllocOps_;
        } else {
          percentAllocOps = 100 - paramPercentAllocOps_;
        }
        if (choice < paramPercentStatementOps_) {
          choice = random_.nextInt(100);
          if (choice < percentAllocOps) {
            allocStatement();
          } else {
            freeStatement();
          }

        } else {
          choice = random_.nextInt(100);
          if (choice < percentAllocOps) {
            allocConnection();
          } else {
            freeConnection();
          }
        }
        exerciseStatements();
        if (System.currentTimeMillis() > statusTime) {
          out_.println("Thread " + threadId_ + ": conn="+(allocConnectionCount_ - freeConnectionCount_ )+" conn+="
              + allocConnectionCount_ + "conn-=" + freeConnectionCount_
              + "stmt+=" + allocStatementCount_ + " stmt-="
              + freeStatementCount_ + " handlecount=" + getHandleCount());
          statusTime = System.currentTimeMillis() + 5000;
        }
        currentTime = System.currentTimeMillis();
        synchronized(this) {
          localAbort = abort;
        }
      } /* while */
    } catch (Exception e) {
      String jobname = "UNKNOWN";
      try {
        jobname = JDReflectionUtil.callMethod_S( currentConnection_,"getServerJobName");

      } catch (Exception e2) {
      }
      boolean localAbort; 
      synchronized (this) {
	  localAbort = abort;
      } 
      if (!localAbort) { 
	  failed = true;
      } else {
	  out_.print("Thread " + threadId_ + ": aborting .. ignoring following exception"); 
      } 

      out_.println("Thread " + threadId_ + ": caught exception current server="
          + jobname);
      e.printStackTrace(out_);

    }
    synchronized(this) {
	running = false;
	notify();
    }

    }

    public synchronized void abort() {
       abort = true;
       if (currentStatement_ != null) {
         try { 
           currentStatement_.cancel();
         }   catch (Exception e) { 
           // Just ignore errors from cancel 
         }
       }
    }
    public boolean getFailed() {
      return failed;
    }

    public void allocConnection()
      throws SQLException, InterruptedException {
	synchronized(this) { 
	    while (paused) {
		wait(1000);
	    }
	}
	if (canAllocateConnectionHandles()) {

	    Connection c;
	    if ((allocConnectionCount_ %2) == 0) {
	      c = DriverManager.getConnection("jdbc:db2:*LOCAL;errors=full");
	    } else {
	      c = staticDataSource.getConnection();
	    }
	    synchronized(this) { 
		currentConnection_ = c;
	    }
	    Vector<Object[]> connectionStatements = new Vector<Object[]>();
	    for (int i = 0; i < paramStatements_; i++) {
		allocStatement(c, connectionStatements);
		synchronized(this) { 
		    while (paused) {
			wait(1000);
		    }
		}

	    }
	    synchronized(this) { 
		connections_.addElement(c);
		connectionStatementHashtable_.put(c, connectionStatements);
		allocConnectionCount_++;
	    }

	}
    }

    public void freeConnection() throws SQLException, InterruptedException {
	synchronized(this) { 
	    while (paused) {
		wait(1000);
	    }
	}

	int connectionCount = connections_.size();
	if (connectionCount > 0) {
	    int whichConnection = random_.nextInt(connectionCount);
	    Connection c = null;
	    Vector<?> connectionStatements = null; 
	    synchronized(this) { 
		c = (Connection) connections_.elementAt(whichConnection);
		currentConnection_ = c;
		connections_.removeElementAt(whichConnection);

		connectionStatements = (Vector<?>) connectionStatementHashtable_.get(c);
		connectionStatementHashtable_.remove(c);
	    }
	    c.close();
	    freeConnectionHandles();
	    synchronized(this) { 
		freeConnectionCount_++;
	    }
	    // Need to go through the connectionStatements and determine how many to free
	    //
	    synchronized(this) { 
		Enumeration<?> statementsEnumeration = connectionStatements.elements();
		while (statementsEnumeration.hasMoreElements()) {
		    Object[] statementAndResult = (Object[]) statementsEnumeration.nextElement();
		    int handles = ((Integer)statementAndResult[2]).intValue();
		    freeStatementCount_+= handles ;
		    freeStatementHandles(handles);
		}
	    }
	}


    }

    public void allocStatement() throws SQLException,
      InterruptedException {
	synchronized(this) { 
	    while (paused) {
		wait(1000);
	    }
	}
	Connection c = null; 
	Vector<Object[]> connectionStatements = null; 
	synchronized(this) { 
	    int connectionCount = connections_.size();
	    if (connectionCount > 0) {
		int whichConnection = random_.nextInt(connectionCount);
		c = (Connection) connections_.elementAt(whichConnection);
		currentConnection_ = c;
		connectionStatements = (Vector<Object[]>) connectionStatementHashtable_.get(c);
	
	    }
	}
	if (connectionStatements != null && (!abort)) {
	    allocStatement(c, connectionStatements);
	} 
  }



    public void allocStatement(Connection c,
					    Vector<Object[]> connectionStatements)
      throws SQLException, InterruptedException {
	synchronized (this) { 
	    while (paused) {
		wait(1000);
	    }
	}


	Integer statementMaxCountInteger;
	boolean canAllocate;
	boolean useClob = false;
	int allocCount = 0;
	int statementMaxCount = 0;
	int result; 
	synchronized (this) { 
	    statementMaxCountInteger  = (Integer)connectionStatementMaxCountHashtable_.get(c) ;
	    if (statementMaxCountInteger != null) {
		statementMaxCount = statementMaxCountInteger.intValue();
	    }
	    result = random_.nextInt(100);
	    if (result % 2 == 0 && (connectionStatements.size() < statementMaxCount -1 )) {
		useClob=true;
		allocCount = 2;
	    } else {
		useClob = false;
		allocCount = 1;
	    }
	// Check to see if we can allocate.
	    if (canAllocateStatementHandles(allocCount)) {
		canAllocate = true;
	    } else {
		canAllocate = false;
	    }

	}

	if (canAllocate) {
	    Object[] statementAndResult = new Object[3];
	    result = random_.nextInt(999999);
	    synchronized(this) { 
		currentConnection_ = c;
	    }
	    PreparedStatement ps;
	    /* Randomly choose whether or not use use locator */
	    if (useClob) {
	      ps = c.prepareStatement("select "+result+",cast('abcdefg' as CLOB(1M))  from sysibm.sysdummy1 where IBMREQD=?");
	    } else {
	      ps = c.prepareStatement("select "+result+",'abcdefg' from sysibm.sysdummy1 where IBMREQD=?");
	    }
	    ps.setString(1,"Y");
	    currentStatement_ = ps; 
	    ResultSet rs = ps.executeQuery();
	    currentStatement_ = null; 
	    rs.next();
	    int thisResult = rs.getInt(1);
	    String x = rs.getString(2);
	    if (thisResult != result) {
	      out_.println("Thread "+threadId_+": Result from alloc statement not correct x="+x);
	    }

	    rs.close();
	    statementAndResult[0] = ps;
	    statementAndResult[1] = new Integer(result);
	    statementAndResult[2] = new Integer(allocCount);
	    synchronized(this) { 
		connectionStatements.addElement(statementAndResult);
		if (connectionStatements.size() > statementMaxCount) {
			connectionStatementMaxCountHashtable_.put(c, new Integer(connectionStatements.size()));
		}
		allocStatementCount_+=allocCount;
	    }
	}
    }


    synchronized public void freeStatement()
      throws SQLException, InterruptedException {
      while (paused) {
        wait(1000);
      }

	int connectionsSize = connections_.size();
	if (connectionsSize > 0) {
	int whichConnection = random_.nextInt(connectionsSize);
	Connection c = (Connection) connections_.elementAt(whichConnection);
	currentConnection_ = c;
	Vector<?> connectionStatements = (Vector<?>) connectionStatementHashtable_.get(c);
	int statementCount = connectionStatements.size();
	if (statementCount > 1) {
	    try {
		int whichStatement = random_.nextInt(statementCount);
		Object[] statementAndResult = (Object[]) connectionStatements.elementAt(whichStatement);
		connectionStatements.removeElementAt(whichStatement);
		PreparedStatement ps = (PreparedStatement) statementAndResult[0];
		ps.close();
		// Free handles here instead of when connection closes.
		int allocCount = ((Integer)statementAndResult[2]).intValue();
		freeStatementHandles(allocCount);
		freeStatementCount_+=allocCount;
	    } catch (IllegalArgumentException e) {
		out_.println("Thread "+threadId_+": statementCount was "+statementCount);
		e.printStackTrace(out_);

	    }
	}
	}
    }


    public void exerciseStatement()
throws SQLException, InterruptedException {

	int connectionCount = connections_.size();
	if (connectionCount > 0) {
	int whichConnection = random_.nextInt(connectionCount);
	Connection c = (Connection) connections_.elementAt(whichConnection);
	currentConnection_ = c;
	Vector<?> connectionStatements = (Vector<?>) connectionStatementHashtable_.get(c);
	int statementCount = connectionStatements.size();
	if (statementCount > 0) {
	    int whichStatement = random_.nextInt(statementCount);
	    Object[] statementAndResult = (Object[]) connectionStatements.elementAt(whichStatement);
	    PreparedStatement ps = (PreparedStatement) statementAndResult[0];
	    int result = ((Integer) statementAndResult[1]).intValue();
      currentStatement_ = ps; 
	    ResultSet rs = ps.executeQuery();
      currentStatement_ = null; 
	    rs.next();
	    int thisResult = rs.getInt(1);
	    if (thisResult != result) {
		out_.println("Thread "+threadId_+": Result from exercise statement not correct");
	    }
	    rs.close();
	}
	}

    }

    public void exerciseStatements()
throws SQLException, InterruptedException {
	for (int i = 0; i < exerciseStatementCount_; i++) {
	    exerciseStatement();
	}
    }

      synchronized public void pause() {
        paused = true;
        notifyAll();
      }

      synchronized public void resume() {
        paused = false;
        notifyAll();
      }

	    public static void usage() {
		System.out.println("java JDHandleStress <threads> <connectionsPerThread> <statementsPerConnection> <percentStatementOps> <percentAllocOps> <runSeconds> <growTime> <shrinkTime>");
	    }


	    /* Cleanup the connection.  Must be called after abor and will wait until */
	    /* the thread quits to run. */
	    public void cleanup() throws InterruptedException {
		synchronized(this) {
		    while (running) {
			wait(1000);
		    }
		}
	      int connectionCount;
	      synchronized(this) { 
		  connectionCount = connections_.size();
	      }
	      int i = 0; 
	      while ( i < connectionCount) {
		  Connection c = null; 
	        try {
		    synchronized(this) {
			if (connections_.size() > 0) { 
			    c = (Connection) connections_.elementAt(0);
			    connections_.removeElementAt(0);
			}
			connectionCount = connections_.size();
		    }
		    if (c != null) 
			c.close();
	        } catch(Exception e) {
	          out_.println("Exception during cleanup");
		  out_.println("Connection is "+c); 
	          e.printStackTrace(out_);
		  JDJobName.setJobLogOption();
		  out_.println("Job is "+JDJobName.getJobName()); 
	        }

	      } /* for i */
	    }

/*
 * STATIC methods
 */
static Object lockObject = new Object();
// Make a little lower so that dump handle can run
// static int CLI_NEWMAX=160000;
static int CLI_NEWMAX=159990;
static int maximumCliHandles=160000;
static int handleCount = 0;
static Object growLock = new Object();
static boolean growing = true;
static boolean staticFailed = false;

private static DataSource staticDataSource;

/* Returns true if a statement can be allocated without exceeding the handle limit */
/* Assumes the statement is allocated if true is returned */

/* Can x handles be locator.  With the addition of locators used by statements, a values of two should be passed in  */
public static boolean canAllocateStatementHandles(int handles ) {
    synchronized(lockObject) {
	if (handleCount+ handles*HANDLES_PER_STATEMENT < maximumCliHandles) {
	    handleCount += handles*HANDLES_PER_STATEMENT;
	    return true;
	} else {
	    return false;
	}
    }
}

public static boolean canAllocateConnectionHandles() {
    synchronized(lockObject) {
	if (handleCount+1 < maximumCliHandles) {
	    handleCount += 1;
	    return true;
	} else {
	    return false;
	}
    }
}


public static void freeStatementHandles(int count) {
    synchronized(lockObject) {
	handleCount -= count * HANDLES_PER_STATEMENT;
    }
}

public static void freeConnectionHandles() {
    synchronized(lockObject) {
	handleCount -= 1;
    }
}

public static int getHandleCount() {
    synchronized(lockObject) {
	return handleCount;
    }
}


public static void setGrowing(boolean newGrowing) {
  synchronized (growLock) {
    growing = newGrowing;
  }
}

public static boolean isGrowing() {
  synchronized (growLock) {
    return growing;
  }
}

/*
 * main method
 */

/*
 * Run the test.. This is also called by JDConnectionStress, so any
 * changes here must also be made there.
 */
public static void run(PrintStream out,
		       int threadCount,
		       int paramConnections,
		       int paramStatements,
		       int paramPercentStatementOps,
		       int paramPercentAllocOps,
		       int paramRunSeconds,
		       int paramGrowTime,
		       int paramShrinkTime
		       ) throws Exception {


    String jobName = JDJobName.getJobName();
    String[] dumpHandleArgs = new String[1];
    int slashIndex = jobName.indexOf("/");
    dumpHandleArgs[0] = jobName.substring(0, slashIndex);

    staticDataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdDataSource");
    JDReflectionUtil.callMethod_V(staticDataSource,  "setFullErrors",true); 
    try {
      JDHandleDumpAnalyzer.processDump(out, dumpHandleArgs[0]);
        } catch (Exception e) {
      out.println("Error looking at dump");
      e.printStackTrace(out);
        }
    int beginningHandles = JDHandleDumpAnalyzer.getLastAllocatedHandles();
    out.println("beginningHandles="+beginningHandles);
    maximumCliHandles = CLI_NEWMAX - beginningHandles;
    out.println("Maximum CLI handles set to "+maximumCliHandles);
    /* reset variables */
    handleCount = 0;
    growing = true;
    staticFailed = false;






    // Connection dummyConnection = DriverManager.getConnection("jdbc:db2:*LOCAL");
    // Statement dummyStatement = dummyConnection.createStatement();
    // dummyStatement.executeQuery("Select * from sysibm.sysdummy1");

    if (threadCount  <= 0)  throw new Exception("threads should be > 0");
    if (paramConnections < 5) throw new Exception("connections should be >= 5");
    if (paramStatements  < 10) throw new Exception("statementsPerConnection should be >= 10");
    if (paramPercentStatementOps > 100 ) throw new Exception("percentStatementOps should be <= 100");
    if (paramPercentStatementOps < 0)  throw new Exception("percentStatementOps should be >= 0");
    if (paramPercentAllocOps > 100 ) throw new Exception("percentAllocOps should be <= 100");
    if (paramPercentAllocOps < 0)  throw new Exception("percentAllocOps should be >= 0");
    if (paramGrowTime <= 0)  throw new Exception("growTime should be > 0");
    if (paramShrinkTime <= 0)  throw new Exception("shrinkTime should be > 0");

    out.println("Running test.JDHandleStress in job "+jobName+" at "+(new Timestamp(System.currentTimeMillis())));


    JDHandleStress[] runners = new JDHandleStress[threadCount];
    Thread[] threads = new Thread[threadCount];
    out.println("Creating threads"); 
    for (int i = 0; i < threadCount; i++) {
	runners[i] = new JDHandleStress(out, "T"+i, paramConnections, paramStatements, paramPercentStatementOps, paramPercentAllocOps, paramRunSeconds);
	threads[i] = new Thread(runners[i]);
    }
    out.println("Starting threads"); 
    for (int i = 0; i < threadCount; i++) {
	threads[i].start();
    }
    out.println("Threads started "); 


    long endtime = System.currentTimeMillis() + 1000 * paramRunSeconds;
    long currentTime = System.currentTimeMillis();
    long dumpTime = currentTime + 30000;
    long growTime = currentTime+3600000;
    long shrinkTime = currentTime+1000*paramGrowTime+1000;
    setGrowing(true);
    while (currentTime < endtime && !staticFailed ) {
	Thread.sleep(333);
	currentTime = System.currentTimeMillis();
	if (currentTime > shrinkTime) {
	    out.println("Shrinking...."+ (new Timestamp(currentTime)));
	    setGrowing(false);
	    growTime = currentTime+1000*paramShrinkTime;
	    shrinkTime = currentTime+3600000;
	} else if (currentTime > growTime) {
	    out.println("Growing......"+ (new Timestamp(currentTime)));
	    setGrowing(true);
	    shrinkTime = currentTime+1000*paramGrowTime;
	    growTime = currentTime+3600000;
	}
	if (currentTime > dumpTime) {
	    dumpTime = System.currentTimeMillis() + 30000;
	    int connections = 0;
	    int statements = 0;
	    out.println("Pausing runners for dump..."+ (new Timestamp(System.currentTimeMillis()))); 
	    for (int i = 0; i < threadCount; i++) {
		out.println("Pausing runner "+i+" of "+threadCount+" for dump..."+ (new Timestamp(System.currentTimeMillis()))); 
		runners[i].pause();
	    }
	    out.println("Runners paused..."+ (new Timestamp(System.currentTimeMillis()))); 
	    for (int i = 0; i < threadCount; i++) {
		connections += runners[i].allocConnectionCount_
		  - runners[i].freeConnectionCount_;
		statements += runners[i].allocStatementCount_
		  - runners[i].freeStatementCount_;
	    }

	  /* Free up handles from dummy connection so that */
	  /* the dump can run. */
	    /*
	    if (dummyConnection != null) {
		if (dummyStatement != null) {
		    dummyStatement.close();
		    dummyStatement = null;
		}
		dummyConnection.close();
		dummyConnection = null;
	    }
            */
	    out.println("Dumping handles at "
			       + (new Timestamp(System.currentTimeMillis())));
	    try {
		JDHandleDumpAnalyzer.processDump(out, dumpHandleArgs[0]);
	    } catch (Exception e) {
		out.println("Error looking at dump");
		e.printStackTrace(out);
	    }

	    out.println("Test allocated connections      = " + connections);
	    out.println("Test allocated statements       = " + (statements));
	    out.println("Test allocated handles estimate = " + getHandleCount());
	    out.println("Finished dumping handles at "
			       + (new Timestamp(System.currentTimeMillis())));

	  /* Refresh dummy stuff */
          /*
	    try {
		dummyConnection = DriverManager.getConnection("jdbc:db2:*LOCAL");
		dummyStatement = dummyConnection.createStatement();
		dummyStatement.executeQuery("Select * from sysibm.sysdummy1");
	    } catch (Exception e) {
		out.println("Exception from dummy statements\n");
		e.printStackTrace(out);
	    }
          */
	    out.println("Resuming runners ..." + (new Timestamp(System.currentTimeMillis()))); 
	    for (int i = 0; i < threadCount; i++) {
		if (runners[i].getFailed()) {
		    staticFailed = true;
		}
		runners[i].resume();

	    }
	    out.println("Runners resumed ....."+ (new Timestamp(System.currentTimeMillis())));
	    out.println(" .. end time will be "+ (new Timestamp(endtime))); 
	} /* dumpTime */
	currentTime = System.currentTimeMillis();
    } /* while before end time */


    for (int i = 0; i < threadCount; i++) {
        /* if they haven't stopped already, tell them to stop */
        out.println("Aborting thread "+i+" of "+threadCount+" ... "+ (new Timestamp(System.currentTimeMillis())));
        runners[i].abort();
    }
    for (int i = 0; i < threadCount; i++) {
        out.println("Cleanup thread "+i+" of "+threadCount+" ... "+ (new Timestamp(System.currentTimeMillis())));
        runners[i].cleanup();
        out.println("End cleanup thread "+i+" of "+threadCount+" ... "+ (new Timestamp(System.currentTimeMillis())));
        if (runners[i].getFailed()) {
          staticFailed = true;
        }
    }
    for (int i = 0; i < threadCount; i++) {

        out.println("Joining thread "+i+" of "+threadCount+" ... "+ (new Timestamp(System.currentTimeMillis())));
	threads[i].join(5000);
	while(threads[i].isAlive()) {
	    out.println("Thread  "+i+" of "+threadCount+" is still alive "+ (new Timestamp(System.currentTimeMillis())));
	    try {
		out.println("Attempting to dump the stack "); 
	
		// Using JDK 1.5 methods to dump the stack
		Object[] traceElements = (Object []) JDReflectionUtil.callMethod_O(threads[i], "getStackTrace") ;
		  for (int j = 0; j < traceElements.length; j++) { 
		    out.println("... "+traceElements[j].toString()); 
		  }
	    }  catch (Exception e) {
		// If NoSuchMethodException, just ignore
		out.println("Failed to dump the stack");
		if (e.toString().indexOf("NoSuchMethodExeption") == -1 ) { 
		    e.printStackTrace(out);
		}
	    } 
      threads[i].join(5000);
	} 	


    }
    out.println("Joining complete "+" ... "+ (new Timestamp(System.currentTimeMillis())));

    if (staticFailed) {
      throw new Exception("Test failed");
    }


}

    public static void main(String[] args)   {

	if (args.length < 5) {
	    usage();
	    System.exit(1 );
	}


	try
	{

	    int threadCount = Integer.parseInt(args[0]);
	    int paramConnections = Integer.parseInt(args[1]);
	    int paramStatements  = Integer.parseInt(args[2]);
	    int paramPercentStatementOps = Integer.parseInt(args[3]);
	    int paramPercentAllocOps     = Integer.parseInt(args[4]);
	    int paramRunSeconds  =  Integer.parseInt(args[5]);
	    int paramGrowTime = Integer.parseInt(args[6]);
	    int paramShrinkTime = Integer.parseInt(args[7]);

	    run(System.out, threadCount, paramConnections, paramStatements,
		paramPercentStatementOps, paramPercentAllocOps,
		paramRunSeconds, paramGrowTime, paramShrinkTime);



	     }
        catch(Exception e)
        {
                System.out.println("Unexpected Exception: " + e.getMessage());
		usage();
                e.printStackTrace();

                String jobName = JDJobName.getJobName();

		String[] dumpHandleArgs = new String[1];
		int slashIndex = jobName.indexOf("/");
		dumpHandleArgs[0] = jobName.substring(0,slashIndex);

		JDHandleDumpAnalyzer.main(dumpHandleArgs);


        }

    }

}

