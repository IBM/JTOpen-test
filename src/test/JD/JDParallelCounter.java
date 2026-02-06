///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRunit.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 2025-2025 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import test.JTOpenTestEnvironment;


/**
 * Class to manage parallel tests running at the same time
 * 
 * At the beginning of setup a ParallelCounter object should be created. 
 * This will prevent the run of setup of cleanup is occurring.  Once it is returned, 
 * entry for the current job is added to the table. 
 * 
 * In cleanup, the doCleanup() method should be called. 
 * If this is the last test using the environment, then true will be returned and
 * cleanup should be done
 * At the end of cleanup close() should be called to remove the entry from the table. 
 * 
 * 
 * So, beginning of setup has
 * 
 *       parallelCounter_ = new JDParallelCounter(connection_, PSTEST_CONCUR); 
 *       
 *       each use of an object should still use JDSerializeFile to ensure
 *       serialized access to the file. 
 *
 *
 * Cleanup has
 * 
 *    if (parallelCounter_.doCleanup()) {
 *    ... cleanup here
 *    }
 *    parallelCounter_.close(); 
 */
public class JDParallelCounter implements AutoCloseable{
  String filename_ = null; 
  String name_; 
  Statement statement_; 
  boolean closeStatement_ = false; 
  JDDataAreaLock dataAreaLock_ ; 
  boolean cleanupSet_ = false; 
  
  /* Name is the name of a file that should be used to keep track of the */
  /* oncurrently running jobs */
  
  public JDParallelCounter(Connection c, String name) throws Exception {
    filename_ = name; 
    name_ = name; 
    statement_ = c.createStatement();
    closeStatement_ = true; 
    name = name.replace("/","X").replace(".","X").replace("\"","X");
    int len = name.length(); 
    if (len > 10) {
      name = name.substring(len-10); 
    }
    if (Character.isDigit(name.charAt(0))) { 
      name='X'+name.substring(1); 
    }
    
    String baseFilename = null; 
    String baseCollection = null; 
    int dotIndex = filename_.indexOf("."); 
    if (dotIndex > 0) { 
      baseFilename = filename_.substring(dotIndex+1); 
      baseCollection = filename_.substring(0,dotIndex); 
    } else {
      baseFilename= filename_; 
      baseCollection= JTOpenTestEnvironment.testInfoSchema;
      filename_ = baseCollection+"."+baseFilename; 
    }
    dataAreaLock_ = new JDDataAreaLock(statement_, name);
    dataAreaLock_.lock("JDparallelCounter", 3600);
    
    /* If the table does not exist, create it and add a row for the counter */ 
    
    /* Wait for a long time if there is much contention */ 
    statement_.executeUpdate("CALL QSYS2.QCMDEXC('CHGJOB DFTWAIT(3600)')"); 
    
    String sql = "SELECT * FROM QSYS2.SYSTABLES where TABLE_NAME='"+baseFilename+"' and TABLE_SCHEMA='"+baseCollection+"' " ;
    ResultSet rs = statement_.executeQuery(sql); 
    if (!rs.next()) { 
      rs.close(); 
      
      sql =  "CREATE OR REPLACE TABLE "+filename_+" (LOCKJOBNAME VARCHAR(80), STARTTIME TIMESTAMP) ON REPLACE PRESERVE ROWS"; 
      System.out.println("Running "+sql); 
      statement_.executeUpdate(sql); 
      c.commit(); 
    } else {
      rs.close(); 
    }

    /* If the jobname is CLEANUP, then cleanup is in progress. */ 
    /* Unlock the data area and wait for it to become positive.  */
    /* Once positive reaquire the data area */
    sql = "SELECT count(*) FROM "+filename_+" WHERE LOCKJOBNAME='CLEANUP'";
    rs = statement_.executeQuery(sql); 
    rs.next(); 
    int count = rs.getInt(1); 
    while (count > 0) {
      dataAreaLock_.unlock("JDSerializeFile", null);
      Thread.sleep(1000); 
      rs.close(); 
      rs = statement_.executeQuery(sql); 
      rs.next(); 
      count = rs.getInt(1); 
      dataAreaLock_.lock("JDSerializeFile", 3600);
    }
    rs.close(); 
    

    
    
    /* Register the job */
    Timestamp startTimestamp = new Timestamp(System.currentTimeMillis()); 
    PreparedStatement ps = c.prepareStatement("INSERT INTO "+filename_+" VALUES(QSYS2.JOB_NAME,?)");
    ps.setTimestamp(1,startTimestamp); 
    ps.executeUpdate(); 
    ps.close(); 
    c.commit(); 
    statement_.executeUpdate("CALL QSYS2.QCMDEXC('CHGJOB DFTWAIT(30)')"); 

    dataAreaLock_.unlock("JDSerializeFile", null);
    
  }

 
  public String toString() { 
    return "JDParalleCounter("+filename_+","+name_+")";
  }

  public String getName() { 
    return name_; 
  }
  
  @SuppressWarnings("resource")
  /** determine if cleanup should be called.  
   * If cleanup should be called, the data area is locked until
   * the counter is closed. 
   * 
   * @return
   * @throws SQLException
   */
  public boolean doCleanup() throws SQLException {
    boolean cleanup = false; 

    dataAreaLock_.lock("JDparallelCounter", 3600);

    /* Check the files.  If we are the only job then */
    /* add a CLEANUP entry to prevent new counters from being added */
    
    
    String sql = "SELECT count(*) FROM "+filename_;
    ResultSet rs = statement_.executeQuery(sql); 
    rs.next(); 
    int count = rs.getInt(1); 
    if (count == 1) {
      sql = "INSERT INTO "+filename_+" VALUES('CLEANUP',CURRENT TIMESTAMP)"; 
      statement_.execute(sql); 
      cleanupSet_ = true; 
      cleanup = true; 
    }
    rs.close(); 
    
    if (!cleanupSet_) { 
       dataAreaLock_.unlock("JDSerializeFile", null);
    }
   statement_.getConnection().commit(); 
    
    return cleanup; 
  }
  /*
   * Must be called before the connection is closed  
   */
  @SuppressWarnings("resource")
  public void close() throws SQLException {
    if (!cleanupSet_) { 
      dataAreaLock_.lock("JDparallelCounter", 3600);
    }
    String sql; 
    /* If we were doing cleanup, remove the cleanup entry  */ 
    if (cleanupSet_) { 
      sql = "DELETE FROM "+filename_+" WHERE LOCKJOBNAME='CLEANUP'"; 
      statement_.execute(sql); 
    }
    
    /* Otherwise, remove the entry for the job  */ 
    sql = "DELETE FROM "+filename_+" A WHERE A.LOCKJOBNAME=QSYS2.JOB_NAME"; 
    statement_.execute(sql); 
    
    
    dataAreaLock_.unlock("JDSerializeFile", null);
    statement_.getConnection().commit(); 
    
    
    if (closeStatement_)
      statement_.close(); 

  
  }
  
  
  public static void main(String args[]) { 
     System.out.println("Testing JDParallelCounter  system userid password "); 
     try { 
       String system=args[0]; 
       String userid = args[1]; 
       String password = args[2]; 
       String tablename="QGPL.JDPCNTTST"; 
       int failureCount= 0; 
       System.out.println("Getting connections"); 
       System.out.println("Running to "+system+" using "+userid); 
       Connection c1 = DriverManager.getConnection("jdbc:as400:"+system, userid, password); 
       Connection c2 = DriverManager.getConnection("jdbc:as400:"+system, userid, password); 
       Connection c3 = DriverManager.getConnection("jdbc:as400:"+system, userid, password); 
       
       System.out.println("TEST 1 : Simple"); 
       System.out.println("  creating counter"); 
       JDParallelCounter counter1= new JDParallelCounter(c1, tablename);
       System.out.println("  calling doCleanup"); 
       boolean doCleanup = counter1.doCleanup();
       if (!doCleanup) { 
           System.out.println("Cleanup should have worked"); 
           failureCount++;
       }
       System.out.println("  calling close"); 
       counter1.close(); 
       
       System.out.println("TEST 2 : Simple"); 
       System.out.println("  creating counter"); 
       counter1= new JDParallelCounter(c1, tablename);
       System.out.println("  calling doCleanup"); 
       doCleanup = counter1.doCleanup();
       if (!doCleanup) { 
           System.out.println("Cleanup should have worked"); 
           failureCount++;
       }
       System.out.println("  calling close"); 
       counter1.close(); 
       

       System.out.println("TEST 3 : Concurrent "); 
       System.out.println("  creating counter1"); 
       counter1= new JDParallelCounter(c1, tablename);
       System.out.println("  creating counter2"); 
       JDParallelCounter counter2 = new JDParallelCounter(c2, tablename);
       System.out.println("  calling counter1.doCleanup"); 
       doCleanup = counter1.doCleanup();
       if (doCleanup) { 
           System.out.println("Cleanup have returned false"); 
           failureCount++;
       }
       System.out.println("  calling count1.close"); 
       counter1.close(); 
       System.out.println("  calling counter2.doCleanup"); 
       doCleanup = counter2.doCleanup();
       if (!doCleanup) { 
           System.out.println("Failure Cleanup should have worked"); 
           failureCount++;
       }
       System.out.println("  calling counter2.close"); 
       counter2.close(); 
       

       
       
       
       System.out.println("Closing connections"); 
       c1.close(); 
       c2.close(); 
       c3.close(); 
       if (failureCount == 0) { 
         System.out.println(" TESTCASE SUCCESSFUL"); 
       } else {
         System.out.println("********************************"); 
         System.out.println(" TESTCASE HAD "+failureCount+" FAILURES"); 
         System.out.println("********************************"); 
         
       }
     } catch (Exception e) { 
       System.out.println("********************************"); 
       System.out.println(" TEST FAILED"); 
       System.out.println("********************************"); 
       e.printStackTrace();
     }
     
  }

}
