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
// Class to create a poool of work that will be run by threads.
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


package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLWorkThread extends Thread {

  int id; 
  Connection c = null;
  Statement stmt; 
  SQLWorkPool sqlWorkPool; 
  boolean running = true; 
  public SQLWorkThread(SQLWorkPool sqlWorkPool, int id, String jdbcUrl, String userId,  String password) throws SQLException {
      c = DriverManager.getConnection(jdbcUrl,userId,password); 
      stmt = c.createStatement(); 
      this.sqlWorkPool = sqlWorkPool; 
      this.id = id; 
  }

  
  
  public void run() {
    String threadId = "SQLWorkThread"+id;
    Thread.currentThread().setName(threadId); 
    while (running) { 
      try {
        SQLWorkPoolEntry entry = sqlWorkPool.getNext();
        if (entry != null) { 
          try {
            sqlWorkPool.logString(threadId, " running "+entry.sql);
            stmt.execute(entry.sql);
            sqlWorkPool.logString(threadId, " completed "+entry.sql);

          } catch (SQLException e) {
            sqlWorkPool.logString(threadId, "failed running "+entry.sql);
            sqlWorkPool.logException(threadId, e);
          }
          sqlWorkPool.markCompete(entry); 
        }
      } catch (InterruptedException e) {
        running = false; 
      } 
    }
    try {
      c.close();
    } catch (SQLException e) {
    } 
    
  }
  
  public void setDone() { 
    running = false; 
  }
  
}
