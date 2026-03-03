///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionVirtualThreads
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 2025-2025 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionVirtualThreads.java
//
// Classes:      JDConnectionVirtualThreads
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDTestcase;
import test.JTOpenTestEnvironment;
import test.TestDriver;

import java.util.Hashtable;
import java.util.Vector;
import java.io.*;
import java.sql.Connection;

/**
 * Testcase JDConnectionVirtualThreads Tests the virtual threads property of a
 * JDBC connection.
 **/
public class JDConnectionVirtualThreads extends JDTestcase  {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "JDConnectionVirtualThreads";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.JDConnectionTest.main(newArgs);
  }

  
  /**
   * Constructor.
   **/
  public JDConnectionVirtualThreads(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,

      String password, String pwrUID, // @H2A
      String pwrPwd) { // @H2A
    super(systemObject, "JDConnectionVirtualThreads", namesAndVars, runMode, fileOutputStream, password, pwrUID,
        pwrPwd);

  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {


  } /* setup */

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
  

  }

  /* make sure extra threads not created for virtual threads */ 
  public void Var001() {
    if (checkToolbox()) {
      try { 
         
        int beginningCount = TestDriver.countAS400ReadDeamon(output_);
        if (beginningCount > 0) output_.println("**Current "+beginningCount+" AS400ReadDaemons listed above"); 
        Connection c1  = testDriver_.getConnection (baseURL_ +";virtual threads=true", userId_ , encryptedPassword_ );
        Connection c2  = testDriver_.getConnection (baseURL_ +";virtual threads=true", userId_ , encryptedPassword_ );
        Connection c3  = testDriver_.getConnection (baseURL_ +";virtual threads=true", userId_ , encryptedPassword_ );
        int endingCount = TestDriver.countAS400ReadDeamon(output_);
        int difference = endingCount - beginningCount;
        int expectedDifference = 0; 
        if (! JTOpenTestEnvironment.isJava21()) {
          expectedDifference = 3;  
        }
        if (endingCount > 0) output_.println(" Ending "+endingCount+" AS400ReadDaemons listed above"); 
        
        c1.close();
        c2.close(); 
        c3.close(); 
        
        
        assertCondition(difference == expectedDifference, "Error got difference="+difference+" sb "+expectedDifference+ "ending job count="+endingCount+" but beginning count="+beginningCount);
        
        
      } catch (Exception e) { 
        failed(e); 
      }
    }
  }

  /* Make sure work can be done on virtual threads without majo problems */ 
  public void Var002() {
    if (checkToolbox()) {
      try { 
         StringBuffer sb = new StringBuffer();
         boolean success = true; 
        int beginningCount = TestDriver.countAS400ReadDeamon(output_);
        if (beginningCount > 0) output_.println("**Current "+beginningCount+" AS400ReadDaemons listed above"); 
        JDConnectionVirtualThreadsRunnable runnable[] = new JDConnectionVirtualThreadsRunnable[10]; 
        for (int i = 0; i < runnable.length; i++) {
           runnable[i]  = new JDConnectionVirtualThreadsRunnable("JDCVTRun"+i, testDriver_,baseURL_ +";virtual threads=true", userId_ , encryptedPassword_ );
        }
        Thread thread[] = new Thread[runnable.length]; 
        for (int i = 0; i < thread.length; i++) {
          thread[i] = new Thread(runnable[i]); 
          thread[i].start(); 
        }


        for (int i = 0; i < thread.length; i++) {
          thread[i].join(); 
          String errorMessage = runnable[i].getErrorMessage(); 
          if (errorMessage  != null) { 
            success = false; 
            sb.append("Thread ["+i+"] returned error message: "+errorMessage+"\n"); 
          }
        }
        
        assertCondition(success, sb);
        
        
      } catch (Exception e) { 
        failed(e); 
      }
    }
  }


  
  
}
