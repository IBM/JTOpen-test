///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTestDriverTimeoutThread.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.sql.Timestamp;

/**
 * Thread to timeout the testcase
 */

public class TestDriverTimeoutThread extends java.lang.Thread {
  long timeout;
  boolean running;
  long doneTime;

  public TestDriverTimeoutThread(long timeout) {
    this.timeout = timeout;
  }

  public void reset() {
    long currentTime = System.currentTimeMillis();
    doneTime = currentTime + timeout;
  }

  public void run() {
    Thread myThread = Thread.currentThread(); 
    myThread.setName("TestDriverTimeoutThread.starting");
    System.out.println("TestDriverTimeoutThread.starting");
    reset();
    long leftTime = 1000;
    running = true;
    synchronized (this) {
      while (running && (leftTime > 0)) {
        long currentTime = System.currentTimeMillis();
        leftTime = doneTime - currentTime;
        try {
          myThread.setName("TestDriverTimeoutThread."+leftTime);
          if (leftTime > 1000) {
            wait(1000);
          } else if (leftTime > 0) {
            wait(leftTime);
          }
        } catch (InterruptedException e) {
          System.out
              .println("Interrupted exception caught.. Timeout thread exiting");
          running = false;
        } catch (Exception e) {
          System.out.println("TestDriverTimeoutThread Exception caught"); 
          e.printStackTrace(System.out); 
          running = false; 
        }
      }
    }
    if (leftTime <= 0) {
      Timestamp ts = new Timestamp(System.currentTimeMillis()); 
      System.out.println(ts.toString()+": Timed out.. Killing job");
      System.out.println("java.lang.Unknown job being killed");
      System.exit(1);
    }
    System.out.println("TestDriverTimeoutThread.done");
    myThread.setName("TestDriverTimeoutThread.done");
    
  }

  public void markDone() {
    synchronized (this) {
      running = false;
      notifyAll();
    }
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
