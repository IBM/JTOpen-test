//////////////////////////////////////////////////////////////////////
//
//
// OCO Source Materials
//
// The Source code for this program is not published or otherwise
// divested of its trade secrets, irrespective of what has been
// deposited with the U.S. Copyright Office
//
// 5770-SS1
// (C) Copyright IBM Corp. 2018,2018
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    SQLWorkPool.java
//
// Classes:      SQLWorkPool.java
//
// Class to create a pool of work that will be run by threads.
// The number of running threads will adapt due to the load on the system. 
// Errors from the requests will be ignored.
// The primary purpose is to be able to delete libraries quicker. 
//
////////////////////////////////////////////////////////////////////////
//
// CHANGE ACTIVITY:
// See changeActivity field below. 
//
//-------------------------------------------------------------

package test.misc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import test.Testcase;

public class SQLWorkPool {
  StringBuffer log = new StringBuffer();
  long minId;
  long currentId = 0;
  String jdbcUrl;
  String userId;
  String password;
  
  Connection c = null; 
  Statement stmt = null; 
  ConcurrentLinkedQueue<SQLWorkPoolEntry> waitingQueue = new ConcurrentLinkedQueue<SQLWorkPoolEntry>();   
  HashSet<SQLWorkPoolEntry> currentWorkSet = new HashSet<SQLWorkPoolEntry>();
  private long minimumActiveId = 0; 
  SQLWorkThread[] threads;
  private boolean shuttingDown ; 
  
  public SQLWorkPool(String jdbcUrl, String userId, String password, int poolSize) throws SQLException {
    this.jdbcUrl = jdbcUrl; 
    this.userId = userId; 
    this.password = password; 
    threads = new SQLWorkThread[poolSize];
    for (int i = 0; i < poolSize; i++) { 
      threads[i] = new SQLWorkThread(this, i, jdbcUrl, userId, password); 
      threads[i].start(); 
    }
  }

  
  public void deleteLibraryFiles(String library) throws SQLException {
    // Delete all the files in the library using the workers
    if (c == null) { 
      c = DriverManager.getConnection(jdbcUrl, userId, password); 
      stmt = c.createStatement(); 
    }
    PreparedStatement ps = c .prepareStatement("select TABLE_SCHEM || '.' || TABLE_NAME from sysibm.sqltables where TABLE_SCHEM=?" );
    ResultSet rs = ps.executeQuery();
    long lastid =  0; 
    while(rs.next()) { 
      lastid = addSQLWork("DROP TABLE "+rs.getString(1)); 
    }
    try {
      waitForComplete(lastid);
    } catch (InterruptedException e) {
    }
  }
  /**
   * Add sql Work to the queue. This returns an ID that can be used to determine
   * if the work has finished
   */

  public long addSQLWork(String sql) {
    long returnId; 
    synchronized(this) {
        returnId = currentId; 
        SQLWorkPoolEntry entry = new SQLWorkPoolEntry(currentId, sql); 
        waitingQueue.add(entry); 
        currentId++;
        notify(); 
    }
    
    return returnId; 
  }

  synchronized public void waitForComplete(long id) throws InterruptedException {
      while( ! idComplete(id)) {
         wait(); 
      }
  }
  
  boolean idComplete(long id) {
    
      if (id < minimumActiveId ) {
        return true; 
      }
    long newMinimumActiveId = Long.MAX_VALUE; 
    Iterator<?> [] iterators = new Iterator<?>[2]; 
    iterators[0] = waitingQueue.iterator(); 
    iterators[1] = currentWorkSet.iterator(); 
    for (int i = 0; i < iterators.length; i++) {
      Iterator<?> looking = iterators[i];
      while (looking.hasNext()) {
        SQLWorkPoolEntry entry = (SQLWorkPoolEntry) looking.next();
        if (entry.id < newMinimumActiveId)
          newMinimumActiveId = entry.id;
        if (entry.id == id)
          return false;
      }
    }
    minimumActiveId = newMinimumActiveId; 
    return true; 
  }
  
  /** get the next entry from the pool.  This will wait until there is work available 
   * @throws InterruptedException */ 
  
  synchronized public SQLWorkPoolEntry getNext() throws InterruptedException { 
    while ((waitingQueue.size() == 0) && (!shuttingDown)) {
      wait(500);
    }
    SQLWorkPoolEntry entry = null;
    if (!shuttingDown) {
      entry = (SQLWorkPoolEntry) waitingQueue.remove();
      currentWorkSet.add(entry);
      notify();
    }
    return entry;
  }
  
  synchronized public void markCompete(SQLWorkPoolEntry entry) { 
     currentWorkSet.remove(entry); 
     notify(); 
  }

  public String getLog() {
    synchronized (log) {
      return log.toString();
    }
  }

  public void clearLog() {
    synchronized (log) {
      log.setLength(0);
    }
  }

  public void logString(String threadId, String s) {
    synchronized (log) {
      log.append(threadId); 
      log.append(":"); 
      log.append(s);
      log.append("\n");
    }
  }

  public void logException(String threadId, SQLException e) {
    synchronized (log) {
      log.append(threadId); 
      log.append(": Exception : ");
      Testcase.printStackTraceToStringBuffer(e, log);
      log.append("\n");
    }
  }
  
  public synchronized void shutdown() throws SQLException {
    if (c != null) c.close(); 
    synchronized(this) {
       shuttingDown  = true; 
       waitingQueue.clear();
    }
    
    for (int i = 0; i < threads.length; i++) { 
      threads[i].setDone(); 
    }

    for (int i = 0; i < threads.length; i++) { 
      try {
        threads[i].join();
      } catch (InterruptedException e) {
      } 
    }

  }

  
  public static void main(String[] args) { 
    try { 
        String library = args[0];
        String jdbcUrl = args[1]; 
        String userid = args[2]; 
        String password = args[3]; 
        SQLWorkPool pool = new SQLWorkPool(jdbcUrl, userid, password, 10); 
        System.out.println("Starting file delete for library "); 
        pool.deleteLibraryFiles(library);
        System.out.println("Done with file delete for library "); 
        System.out.println(pool.getLog()) ;
        
    } catch (Exception e) { 
      e.printStackTrace();
      System.out.println("Usage: java test.SQLWorkPool LIBRARY_TO_DELETE_FILE jdbcUser userid password");
    }
  }
  
  
}
