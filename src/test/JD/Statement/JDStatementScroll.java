///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementScroll.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementScroll.java
//
// Classes:      JDStatementScroll
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDTestcase;

/**
 * Testcase JDStatementScroll. This the use of a scrollable statement
 * for 1048576 times.  This is a test for PTF FW589278 for the
 * native JDBC driver.
 **/
public class JDStatementScroll extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementScroll";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }

  
  /**
   * Constructor.
   **/
  public JDStatementScroll(AS400 systemObject,
      Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, "JDStatementScroll", namesAndVars, runMode,
        fileOutputStream, password);
  }

  
  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    // Initialize private data.
    connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
    
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    connection_.close();
  }

  
  /**
   * Test using a statement 1048576 times. 
   **/
  public void Var001() {
    try {
      long startTime = System.currentTimeMillis(); 
      int endIteration = 1050000;
      for (int i = 0; i < endIteration; i++) {
	  if (i % 100000 == 1001) {
	      long currentTime = System.currentTimeMillis(); 
	      output_.println("i=" + i + " Elapsed="+(currentTime-startTime)+" Time = "
				 + new Timestamp(currentTime));
	      double predictedTimePerIteration = ((double) (currentTime-startTime)) / (i);
	      long  predictedDuration = (long) (predictedTimePerIteration * endIteration);

	      long   predictedTime = startTime + predictedDuration; 

	      output_.println("     Remaining="+(predictedTime-currentTime)+" ms Est="+new Timestamp(predictedTime));

	      if (predictedDuration > 600000) {
		  output_.println("Exiting test because predicted duration ("+predictedDuration +" ms) is more than 10 minutes");
		  assertCondition(true,"Typically this occurs on remote connections. ");
		  return; 
	      } 


	  }
	  Statement s = connection_.createStatement(
						    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

	  ResultSet rs = s.executeQuery("Select * from sysibm.sysdummy1");
	  rs.next();
	  rs.close();
	  s.close();
      } /* for */ 
      assertCondition(true);
    } catch (Throwable e) {
      failed(e, "Unexpected Exception");
    }
  }
  
  
  
}



