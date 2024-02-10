///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSStressRunnable.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.RS;

import java.sql.Connection;


public class JDRSStressRunnable implements Runnable {

  JDRSStress baseObject_; 
  private Connection connection_; 
  int columns_;
  int rows_; 
  int testId_; 
  boolean ensureMinimumRows_; 
  int valuesLimit_; 
  boolean go = false;
  private String result_;
  private Exception exception_;
  boolean done_; 
  StringBuffer stringBufferLog_ = new StringBuffer(); 
  String[] lastSql_ = new String[1]; 
  
    // Creates a runnable that is used to run a testcase. 
    // Note:  Two threads should not be sharing the same id. 
    public JDRSStressRunnable(JDRSStress baseObject, Connection connection, int columns, int rows, int testId, boolean ensureMinimumRows, int valuesLimit) {
      baseObject_=baseObject; 
      connection_ = connection; 
      columns_ = columns; 
      rows_ = rows; 
      testId_ = testId; 
      ensureMinimumRows_=ensureMinimumRows; 
      valuesLimit_ = valuesLimit;
      result_ = "Initialized";
      done_ = false; 
    }

    // Returns null if test was successful 
    public String getResult() { 
      return result_; 
    }
    
    public Exception getException() { 
      return exception_; 
    }
    public static void main(String args[]) {

    }
    public void waitForDone() {
      synchronized(this) { 
        while (!done_) { 
          try {
            wait(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          } 
        }
      }
    }
    public synchronized void setGo() {
      go = true; 
      notifyAll(); 
    }
    
    public void run() {
      synchronized(this) {
        while(!go) { 
          try {
            wait(10);
          } catch (InterruptedException e) {
            e.printStackTrace();
          } 
        }
      }
      
      try {
        result_ = baseObject_.runTest2(connection_, columns_, rows_, testId_, ensureMinimumRows_, valuesLimit_, stringBufferLog_, lastSql_);
      } catch (Exception e) {
        result_=e.toString(); 
        exception_ = e; 
      }
      synchronized(this) { 
        done_ = true; 
        notifyAll(); 
      }
    }

    public String getErrorInformation() {
      return stringBufferLog_.toString();
    }
}
