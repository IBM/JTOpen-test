///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQRunnable.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.DQ;

import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.DataQueueEntry;

public class DQRunnable implements Runnable {

  DataQueue dq_;
  int runSeconds_;
  String operation_;
  Exception savedException_ = null;
  private int opCount_ = 0;

  public DQRunnable(DataQueue dq, String operation, int runSeconds) throws Exception {
    if (operation.equals("READ") || operation.equals("WRITE")) {
      dq_ = dq;
      operation_ = operation;
      runSeconds_ = runSeconds;
    } else {
      throw new Exception("Invalid operation " + operation);
    }

  }

  @Override
  public void run() {
    opCount_ = 0; 
    Thread.currentThread().setName(operation_);
    try {
      long endTime = System.currentTimeMillis() + 1000 * runSeconds_;
      if (operation_.equals("READ")) {
        while (System.currentTimeMillis() < endTime) {
          DataQueueEntry stuff = dq_.read(1);
          if (stuff != null) { 
            opCount_++; 
          }
        }
      } else {
        while (System.currentTimeMillis() < endTime) {
          dq_.write("This is some data");
          opCount_++;
          Thread.sleep(100);
        }

      }
    } catch (Exception e) {
      synchronized (System.out) {
        System.out.println("Thread " + operation_ + " hit error ");
        e.printStackTrace(System.out);
      }
      savedException_ = e;
    }

  }

  public Exception  getException() {
    
    return savedException_;
  }

  public int getOpCount() {
    return opCount_;
  }

}
