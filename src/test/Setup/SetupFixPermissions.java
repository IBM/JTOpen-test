///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SetupFixPermissions.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.Setup;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

import test.JDReflectionUtil;

public class SetupFixPermissions implements Runnable {

  public static long lastCheck = 0;
  static Object lock = new Object(); 
  static ArrayBlockingQueue<File> queue = new ArrayBlockingQueue<File>(100000); 
  static int queueCount = 0; 
  static int workingCount = 0;
  
  public static void main(String args[]) {
    System.out.println("Fixing permissions");
    try {

      String lastPermissionFixFilename = ".lastPermissionFix";
      System.out.println("Checking " + lastPermissionFixFilename);
      File lastPermissionFix = new File(lastPermissionFixFilename);
      if (!lastPermissionFix.exists()) {
        lastPermissionFix.createNewFile();
      	lastPermissionFix.setLastModified(0);
        lastCheck = 0;
      } else {
        lastCheck = lastPermissionFix.lastModified();
      }

      File startFile = new File(".");
      queue.add(startFile);
      queueCount++;
      
      // Start the threads
      SetupFixPermissions[]  runnables = new SetupFixPermissions[10];
      Thread[]               threads   = new Thread[runnables.length];
      for (int i = 0; i < runnables.length; i++) {
        boolean filesOnly = ( i < 2 ); 
        runnables[i] = new SetupFixPermissions("Thread"+i, filesOnly); 
        threads[i] = new Thread(runnables[i]); 
        threads[i].start(); 
      }

      // Wait for all the threads to stop working.
      int totalCount = 0; 
      for (int i = 0; i < runnables.length; i++) {
        threads[i].join();
        totalCount += runnables[i].processCount; 
      }


      lastPermissionFix.setLastModified(System.currentTimeMillis());
      System.out.println("Total files processed = "+totalCount); 
    } catch (Exception e) {
      e.printStackTrace(System.out);
      System.exit(1); 
    }
  }

  public static void fixPermission(File startFile, String threadName) throws Exception {

    if (startFile.isDirectory()) {
        File[] files = startFile.listFiles();
        for (int i = 0; i < files.length; i++) {
          boolean retry = true;
          while (retry) {
            try {
              synchronized (lock) {
                queue.add(files[i]);
                queueCount++;
                retry = false;
              }
            } catch (Exception e) {
              System.out.println("Warning: Thread " + threadName
                  + " sleeping on " + e + " for " + files[i]);
              try {
                Thread.sleep(1000);
              } catch (Exception e2) {
              }
            }
          }
        }
      }
    fixFilePermission(startFile, threadName);
  }

  public static void fixFilePermission(File thisFile, String threadName) throws Exception {
    //synchronized (System.out) {
    //  System.out.println("Thread "+threadName+" checking " + thisFile.getPath());
    // }
    if (thisFile.lastModified() > lastCheck) {
      // Note: Methods only available in JDK 1.6
      synchronized (System.out) {
        System.out.println("Thread "+threadName+" fixing " + thisFile.getPath());
      }
      JDReflectionUtil.callMethod_V(thisFile, "setReadable", true, false);
      JDReflectionUtil.callMethod_V(thisFile, "setExecutable", true, false);
    } else {
      // System.out.println("Skipping " + thisFile.getPath());
    }
  }
  
  String threadName="";
  boolean filesOnly;   // Should this thread only process files? 
  public SetupFixPermissions(String threadName, boolean filesOnly) {
    this.threadName = threadName;
    this.filesOnly = filesOnly; 
  }
  int processCount = 0; 
  public void run() {
    boolean running = true; 
    
    while (running) {

      File f = null;
      boolean looking = true;
      while (looking) {
        boolean doSleep = false;
        synchronized (lock) {
          f = (File) queue.poll();
          looking = false;
          if (f != null) {
            if (filesOnly && f.isDirectory()) {
              try {
                // put back on the queue.
                // Should not block because we should be able to
                // put back one what was taken off.
                queue.put(f);
                System.out.println("Thread " + threadName +"processCount="+processCount
                    + " re-adding directory:" + f);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              looking = true;
              // sleep to allow another thread to process
              doSleep = true;
            } else {
              queueCount--;
              workingCount++;
            }
          }
        } /* lock */
        if (doSleep) {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      } /* looking */
      if (f != null) { 
          processCount++; 
          try {
              
            fixPermission(f, threadName);
          } catch (Exception e) {
            synchronized(System.out) { 
              System.out.println("Thread "+threadName+" caught exception"); 
              e.printStackTrace(System.out);
            }
          }
          synchronized(lock) {
            workingCount--;  
          }
        } else {
          int count; 
          synchronized(lock) { 
            count = workingCount + queueCount; 
          }
          if (count == 0) {
            running = false; 
          } else {
            // Wait for work
            try {
              Thread.sleep(100);
            } catch (InterruptedException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } 
          }
        }
      
      
    }
    synchronized(System.out) { 
      System.out.println("Thread "+threadName+" processed "+processCount+" files"); 
    }
  }

}
