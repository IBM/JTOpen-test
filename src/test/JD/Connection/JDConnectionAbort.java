///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionAbort.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
// 5770SS11
// (C) Copyright IBM Corp.2011,2011
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionAbort.java
//
// Classes:      JDConnectionAbort
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDHandleDump;
import test.JDReflectionUtil;
import test.JDTestcase;
import test.JD.JDSecurityManagerAllowAbort;
import test.JD.JDSecurityManagerDenyAbort;
import test.JD.JDSecurityManagerDenySQLPermission;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDConnectionAbort.  his tests the following methods
of the JDBC Connection class:

<ul>
<li>abort()</li>
</ul>
**/
public class JDConnectionAbort extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionAbort";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.
  
    private Connection conn_ = null;
    StringBuffer sb = new StringBuffer(); 
    String lockTable = JDConnectionTest.COLLECTION +".JDCABORT"; 
    String lockTableDefinition = "CREATE TABLE "+lockTable+" (KEY INT, VALUE TIMESTAMP )";
    String lockTableInsert     = "INSERT INTO "+lockTable+" VALUES(1,CURRENT TIMESTAMP)"; 
    String lockTableQuery      = "SELECT * FROM "+lockTable; 
    Object timeUnitSeconds1 = null; 

/**
Constructor.
**/
    public JDConnectionAbort (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password)
    {
        super (systemObject, "JDConnectionAbort",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Setup.

@exception Exception If an exception occurs.
**/
    protected void setup () throws Exception  {
      // Get the value for an enum
      // 
      //   "java.util.concurrent.TimeUnit"
       timeUnitSeconds1 = JDReflectionUtil.getStaticField_O("java.util.concurrent.TimeUnit", "SECONDS"); 

        lockTable = JDConnectionTest.COLLECTION +".JDCABORT"; 
        lockTableDefinition = "CREATE TABLE "+lockTable+" (KEY INT, VALUE TIMESTAMP )";
        lockTableInsert     = "INSERT INTO "+lockTable+" VALUES(1,CURRENT TIMESTAMP)"; 
        lockTableQuery      = "SELECT * FROM "+lockTable; 

        conn_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

        Statement stmt = conn_.createStatement(); 
        
        silentlyDropTable(stmt, lockTable); 

        String sql = lockTableDefinition; 
        try { 
          stmt.executeUpdate(sql); 
        } catch (Exception e) {
          System.out.println("Warning:  Exception on "+sql); 
          e.printStackTrace(); 
        }
        
        stmt.close(); 
    }



/**
Cleanup.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
	conn_.close(); 
    }


    void callAbort(Connection connection, Object thisExecutor, StringBuffer sbAbort) throws Exception {
      sbAbort.append("Call abort \n"); 
      {
        Class[] argTypes = new Class[1]; 
        Object[] args = new Object[1]; 
        argTypes[0] = Class.forName("java.util.concurrent.Executor"); 
        args[0] = thisExecutor; 
        JDReflectionUtil.callMethod_V(connection, "abort", argTypes, args);
	sbAbort.append("abort returned \n"); 
      }
    }

    void testExecutor(String executorClassname) {
    if (checkJdbc40()) {
      try {
        sb.setLength(0); 
        
        sb.append("Create " + executorClassname + "\n");

        Object thisExecutor = JDReflectionUtil.createObject(executorClassname);

        testExecutor(thisExecutor, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + sb.toString());
      }

    }
    }

    void testExecutor(Object  thisExecutor, StringBuffer sb1 ) {
      // Both the toolbox and native JDBC drivers support the JDBC4.1 features in JDBC 4.0 
      if (checkJdbc40() ) { 
        try {
            boolean passed = true;
	    int connectionHandle = 0;

            sb1.append("Creating connection at "+System.currentTimeMillis()+"\n"); 
            Connection connection  = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	    if (JDReflectionUtil.instanceOf(connection, "com.ibm.db2.jdbc.app.DB2Connection")) {
		connectionHandle =  JDReflectionUtil.callMethod_I(connection,"getConnectionHandle");
		sb.append("Connection handle is "+connectionHandle+"\n"); 
	    } 
	    sb1.append("Opening second connection at "+System.currentTimeMillis()+"\n"); 
            Connection connection2  = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            
            sb1.append("Update an table and do not commit the transaction at "+System.currentTimeMillis()+"\n"); 
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            Statement stmt = connection.createStatement(); 
            stmt.executeUpdate(lockTableInsert); 
           

            callAbort(connection, thisExecutor, sb1); 

            sb1.append("Verify closed connection at "+System.currentTimeMillis()+"\n"); 
            boolean connectionClosed = connection.isClosed();
            if (!connectionClosed) {
                passed = false; sb1.append("Connection is not closed at "+System.currentTimeMillis()+"\n"); 
            }

            sb1.append("executor.shutdown at "+System.currentTimeMillis()+"\n"); 
            JDReflectionUtil.callMethod_V(thisExecutor, "shutdown");

            //
            sb1.append("executor.awaitTermination at "+System.currentTimeMillis()+"\n");
            {
              Class[] argTypes = new Class[2]; 
              Object[] args = new Object[2];
              argTypes[0] = Long.TYPE; 
              argTypes[1] = Class.forName("java.util.concurrent.TimeUnit");
              args[0] = new Long(600); 
              args[1] = timeUnitSeconds1; 
                
              JDReflectionUtil.callMethod_V(thisExecutor, "awaitTermination",
                                         argTypes, args); 
            }

            //
            // Use toolbox to check that the connection is closed and that the file is not locked. 
            //
            sb1.append("Checking lock at "+System.currentTimeMillis()+"\n"); 
            Statement stmt2 = connection2.createStatement(); 
            ResultSet rs = stmt2.executeQuery(lockTableQuery);
            rs.next(); 
            rs.close(); 
            stmt2.close(); 

	    //
	    // If running with the native JDBC driver, make sure that
            // the connection handle was not leaked. 
	    // 
	    if (connectionHandle > 0) {
		JDHandleDump handleDump = new JDHandleDump(userId_, encryptedPassword_);
		if (handleDump.isConnectionHandleUsed(connectionHandle)) {
		    passed = false;
		    sb.append("ERROR "+connectionHandle+" is present in handle dump\n");
		    handleDump.dumpConnectionHandles(sb); 
		} 

	    } 

            connection2.close(); 

            assertCondition (passed, sb1.toString()); 

        } catch (Exception e) {
            failed(e, "Unexpected Exception " + sb1.toString());
        }
      }
    }
    

/** abort(); 
 * Call abort with ForkJoinPool Executor. 
 * Make sure connection is closed.
 * Wait for executor completes.
 * Make sure physical connection is closed.
 */ 


    public void Var001() {
    if (checkJdbc41()) {
      try {
        sb.setLength(0); 

        String executorClassname = "java.util.concurrent.ForkJoinPool";
        Object thisExecutor = createExecutor(executorClassname, sb); 

        testExecutor(thisExecutor, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + sb.toString());
      }

    }
  }


/** abort()
 * Call abort with ThreadPool Executor. 
 * Make sure connection is closed.
 * Wait for executor completes.
 * Make sure physical connection is closed. 
 */ 
    public void Var002 ()
    {
        if (checkJdbc40()) {
          try {
            sb.setLength(0); 

            String executorClassname = "java.util.concurrent.ThreadPoolExecutor";
            Object thisExecutor = createExecutor(executorClassname, sb); 
            
            testExecutor(thisExecutor, sb);
          } catch (Exception e) {
            failed(e, "Unexpected Exception " + sb.toString());
          }

        }
    }

    
    
/** abort()
 * Call abort with ScheduledThreadPool Executor. 
 * Make sure connection is closed.
 * Wait for executor completes.
 * Make sure physical connection is closed. 
 */ 
    public void Var003 ()
    {
        if (checkJdbc40()) {
          try {
            sb.setLength(0); 

            String executorClassname = "java.util.concurrent.ScheduledThreadPoolExecutor";
            Object thisExecutor = createExecutor(executorClassname, sb); 

            testExecutor(thisExecutor, sb);
          } catch (Exception e) {
            failed(e, "Unexpected Exception " + sb.toString());
          }

        }
    }


/** abort() 
 * Call abort while other thread are using the connection. 
 * Verify that thread will progress to completion or throw an SQLException. 
 */ 
    public void Var004() {
    if (checkJdbc41()) {
      try {

        sb.setLength(0);

        String executorClassname = "java.util.concurrent.ForkJoinPool";
        Object thisExecutor = createExecutor(executorClassname, sb); 

        boolean passed = true;

        sb.append("Creating connection\n");
        Connection connection = testDriver_.getConnection(baseURL_, userId_,
            encryptedPassword_);
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        connection.setAutoCommit(false); 

        conn_.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        conn_.setAutoCommit(false);
        
        Statement stmt = conn_.createStatement();
        stmt.executeUpdate(lockTableInsert);
        sb.append("Update a table using a different connection using "+lockTableInsert+"\n");
        
        sb.append("Starting thread \n"); 

        JDExecuteWhileAbortThread thread = new JDExecuteWhileAbortThread(connection, lockTableQuery, sb); 
        thread.start();
        
        Thread.sleep(2000); 
        
        callAbort(connection, thisExecutor, sb);

        sb.append("Verify closed connection at "+System.currentTimeMillis()+"\n");
        boolean connectionClosed = connection.isClosed();
        if (!connectionClosed) {
          passed = false;
          sb.append("ERROR:  Connection is not closed\n");
	  sb.append("Status from connection\n");
	  sb.append("----------------------------------\n");
	  sb.append(thread.sb1); 
	  sb.append("----------------------------------\n");

        }

        sb.append("executor.shutdown at "+System.currentTimeMillis()+"\n");
        JDReflectionUtil.callMethod_V(thisExecutor, "shutdown");

        sb.append("executor.awaitTermination at "+System.currentTimeMillis()+"\n");
        {
          Class[] argTypes = new Class[2];
          Object[] args = new Object[2];
          argTypes[0] = Long.TYPE;
          argTypes[1] = Class.forName("java.util.concurrent.TimeUnit");
          args[0] = new Long(600);
          args[1] = timeUnitSeconds1;

          JDReflectionUtil.callMethod_V(thisExecutor, "awaitTermination",
              argTypes, args);
        }

        
        // Rollback the insert 
        conn_.rollback(); 

        // Check the exception
        SQLException threadException = thread.getExceptionThrown(); 
        if (threadException == null) {
          passed = false; 
          sb.append("ERROR: Thread query did not throw exception \n");

        } else {
	    
            String exceptionMessage = threadException.toString().toUpperCase(); 
            String expectedMessage = "SQL STATEMENT ENDED";
            String expectedMessage2 = "CONNECTION DOES NOT EXIST"; 
            if ((exceptionMessage.indexOf(expectedMessage ) >= 0 ) ||
		(exceptionMessage.indexOf(expectedMessage2 ) >= 0 )) {
              // found ok 
            } else {
              passed = false; 
              sb.append("Expected to find '"+expectedMessage+"' in "+ exceptionMessage+"\n");
	      printStackTraceToStringBuffer(threadException, sb); 
            }
        }
        assertCondition(passed,sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + sb.toString());
      }

    }
  }
    
/** abort()
 * Call abort on a closed connection. It should be a no-op
 */ 
   public void Var005 () {
     if (checkJdbc41() ) { 
       sb.setLength(0);
       try {
           boolean passed = true;

           sb.append("Creating connection\n"); 
           Connection connection  = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

           sb.append("Closing connection\n"); 
           connection.close(); 
           
           sb.append("Verify closed connection\n"); 
           boolean connectionClosed = connection.isClosed();
           if (!connectionClosed) {
               passed = false; sb.append("Connection is not closed\n"); 
           }
           
           connection.close(); 
           String executorClassname = "java.util.concurrent.ForkJoinPool";
           Object thisExecutor = createExecutor(executorClassname, sb); 

           callAbort(connection, thisExecutor, sb); 

           sb.append("Verify closed connection\n"); 
           connectionClosed = connection.isClosed();
           if (!connectionClosed) {
               passed = false; sb.append("Connection is not closed\n"); 
           }

           assertCondition (passed,sb); 

       } catch (Exception e) {
           failed(e, "Unexpected Exception " + sb.toString());
       }
   }
     
   }
    
/** abort()
 * Call abort on an aborted connection. It should be a no-op
 */ 

   public void Var006 () {
     String executorClassname = "java.util.concurrent.ForkJoinPool";
     
     // Both the toolbox and native JDBC drivers support the JDBC4.1 features in JDBC 4.0 
     if (checkJdbc41() ) { 
       sb.setLength(0);
       try {
           boolean passed = true;

           sb.append("Creating connection\n"); 
           Connection connection  = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
           
           
           sb.append("Update an table and do not commit the transaction\n"); 
           connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
           Statement stmt = connection.createStatement(); 
           stmt.executeUpdate(lockTableInsert); 
           
           Object thisExecutor = createExecutor(executorClassname, sb); 

           callAbort(connection, thisExecutor, sb); 

           sb.append("Verify closed connection\n"); 
           boolean connectionClosed = connection.isClosed();
           if (!connectionClosed) {
               passed = false; sb.append("Connection is not closed\n"); 
           }

           sb.append("executor.shutdown\n"); 
           JDReflectionUtil.callMethod_V(thisExecutor, "shutdown");

           //
           // Get the value for an enum
           // 
           //   "java.util.concurrent.TimeUnit"
           Object timeUnitSeconds11 = JDReflectionUtil.getStaticField_O("java.util.concurrent.TimeUnit", "SECONDS"); 
           sb.append("executor.awaitTermination\n");
           {
             Class[] argTypes = new Class[2]; 
             Object[] args = new Object[2];
             argTypes[0] = Long.TYPE; 
             argTypes[1] = Class.forName("java.util.concurrent.TimeUnit");
             args[0] = new Long(600); 
             args[1] = timeUnitSeconds11; 
               
             JDReflectionUtil.callMethod_V(thisExecutor, "awaitTermination",
                                        argTypes, args); 
           }

           //
           // Use toolbox to check that the connection is closed and that the file is not locked. 
           //
           sb.append("Checking lock \n"); 
           Connection connection2  = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
           Statement stmt2 = connection2.createStatement(); 
           ResultSet rs = stmt2.executeQuery(lockTableQuery);
           rs.next(); 
           rs.close(); 
           stmt2.close(); 
           connection2.close(); 

           sb.append("Calling abort again.. Should be no-op\n"); 
           callAbort(connection, thisExecutor, sb); 
           
           assertCondition (passed,sb); 

       } catch (Exception e) {
           failed(e, "Unexpected Exception " + sb.toString());
       }
   }
     
     
   }
   
/** abort()
 * Call abort when Security Manager exists and checkPermission method allows calling abort. 
 */ 

   public void Var007 () {
     if (checkJdbc41()) {
       
       SecurityManager oldSecurityManager = System.getSecurityManager();

       // Create a security manager. 
       SecurityManager newSecurityManager = new JDSecurityManagerAllowAbort(); 
       System.setSecurityManager(newSecurityManager); 
       
       testExecutor("java.util.concurrent.ForkJoinPool");  

       try { 
       // Restore the old security Manager 
       System.setSecurityManager(oldSecurityManager); 
       } catch (Exception e) { 
         e.printStackTrace(); 
       }
       
     }
   }
   
   
   public void testDenySecurityManager(SecurityManager newSecurityManager) {
     String executorClassname = "java.util.concurrent.ForkJoinPool";

     if (checkJdbc41() ) { 
       sb.setLength(0);
       try {
           boolean passed = true;

           SecurityManager oldSecurityManager = System.getSecurityManager();

           System.setSecurityManager(newSecurityManager); 

           sb.append("Creating connection\n"); 
           Connection connection  = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
           
           
           Object thisExecutor = createExecutor(executorClassname, sb); 

           try { 
             sb.append("Calling abort\n"); 
             callAbort(connection, thisExecutor, sb);
             sb.append("Exception not thrown\n");
             passed = false; 
             
           } catch (SecurityException securityException) {
             sb.append("Exception thrown as expected"); 
           }
           sb.append("Verify closed connection\n"); 
           boolean connectionClosed = connection.isClosed();
           if (connectionClosed) {
               passed = false; sb.append("Connection is closed\n"); 
           }

           
           assertCondition (passed,sb); 

           try { 
             // Restore the old security Manager 
             System.setSecurityManager(oldSecurityManager); 
             } catch (Exception e) { 
               e.printStackTrace(); 
             }

       } catch (Exception e) {
           failed(e, "Unexpected Exception " + sb.toString());
       }
   }
     
     
     
   }
   
/** abort()
 * Call abort when Security Manager exists and checkPermission denies calling abort.  
 * Method throws a java.lang.SecurityException.
 */ 

   public void Var008 () {
     // Create a security manager. 
     SecurityManager newSecurityManager = new JDSecurityManagerDenyAbort(); 
     testDenySecurityManager(newSecurityManager); 
   }
   

   /** abort()
    * Call abort when Security Manager exists and checkPermission denies calling abort.  
    * Method throws a java.lang.SecurityException.
    */ 
      public void Var009 () {
        // Create a security manager. 
        SecurityManager newSecurityManager = new JDSecurityManagerDenySQLPermission(); 
        testDenySecurityManager(newSecurityManager); 
      }
      

   
/** abort()
 * Call abort() when the executor is null.  Should throw SQLException
 */ 
      public void Var010() {
        
        if (checkJdbc40() ) { 
          sb.setLength(0);
          try {
              boolean passed = true;

              sb.append("Creating connection\n"); 
              Connection connection  = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
              
              try { 
              Class[] argTypes = new Class[1]; 
              Object[] args = new Object[1]; 
              argTypes[0] = Class.forName("java.util.concurrent.Executor"); 
              args[0] = null ; 
              JDReflectionUtil.callMethod_V(connection, "abort", argTypes, args);
                sb.append("Did not throw exception for abort with null\n"); 
                passed = false; 
              } catch (Exception e) { 
                String expectedMessage = "PARAMETER TYPE NOT VALID"; 
                String message = e.toString().toUpperCase();
                
                if (message.indexOf(expectedMessage)>=0 ) {
                  sb.append("Expected message found\n"); 
                } else {
                  sb.append("ERROR:  Did not find '"+expectedMessage+"' in "+message+"\n");
                  e.printStackTrace(); 
                  passed=false; 
                }
              }

              assertCondition (passed,sb); 

          } catch (Exception e) {
              failed(e, "Unexpected Exception " + sb.toString());
          }
      }
        
      }



      
      class JDExecuteWhileAbortThread extends Thread {
  
          Connection threadConnection = null; 
          StringBuffer sb1 = null;
          SQLException exceptionThrown = null;
          boolean queryDone = false; 
          PreparedStatement ps; 
          public JDExecuteWhileAbortThread(Connection c, String query, StringBuffer sb) {
            threadConnection = c; 
            this.sb1 = sb;
            try {
               sb.append("JDExecuteWhileAbortThread: Preparing "+query+"\n"); 
               ps = c.prepareStatement(query);
            } catch (SQLException e) {
              exceptionThrown=e; 
            }
	    this.setName("JDExWhileAbort"); 
          }
          
          public void run() {
	      long startTime = System.currentTimeMillis(); 
            try {
              sb1.append("JDExecuteWhileAbortThread: Running query at "+startTime+"\n"); 
              ps.executeQuery();
	      long endTime = System.currentTimeMillis(); 
              sb1.append("JDExecuteWhileAbortThread: Done with query at "+endTime+" \n");
              sb1.append("JDExecuteWhileAbortThread: Query took  "+(endTime-startTime)+" \n"); 
              queryDone = true; 
              
            } catch (SQLException e) {
	      long endTime = System.currentTimeMillis(); 
              sb1.append("JDExecuteWhileAbortThread: Caught exception at "+System.currentTimeMillis()+" took "+(endTime-startTime)+" \n"); 
              exceptionThrown = e; 
            }
          }
          
          public java.sql.SQLException getExceptionThrown() {
              return exceptionThrown;  
          }
      }



}


