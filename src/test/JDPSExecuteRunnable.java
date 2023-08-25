///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSExecuteRunnable.java
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
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSExecuteRunnable.java
//
// Classes:      JDPSExecuteRunnable
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test;

import java.sql.PreparedStatement;
import java.sql.SQLException;




public class JDPSExecuteRunnable implements Runnable {
    String name; 
    PreparedStatement ps;
    String            expectedSQLState;
    long              endTime; 
    boolean           passed = false; 
    StringBuffer     sb = new StringBuffer(); 


/**
Constructor.
**/
    public JDPSExecuteRunnable(String name, 
          PreparedStatement ps,
			       String expectedSQLState,
			       long endTime) {
        this.name = name; 
	this.ps = ps;
	this.expectedSQLState = expectedSQLState;
	this.endTime = endTime;
	sb.append("TESTCASE:  "+name+"\n"); 
    }



public void run() {
  try { 
    passed = true;
    // Keep running for the specified time or until we get a severe exception 
  while ( passed && (System.currentTimeMillis() < endTime)) {
    try {
      ps.execute(); 
      passed = false; 
      sb.append("Execute was successful but should have failed\n "); 
    } catch (SQLException e) {
      String sqlState  = e.getSQLState(); 
      if (!sqlState.equals(expectedSQLState)) {
        passed = false; 
        sb.append("Got sqlstate="+sqlState+" sb "+expectedSQLState+"\n"); 
        JDTestcase.printStackTraceToStringBuffer(e,sb); 
      }
    }
    
  }
  } catch (Throwable t) {
    passed = false; 
    sb.append("Hit unexpected throwable\n"); 
    JDTestcase.printStackTraceToStringBuffer(t, sb);
  }
  
  
}
    
public boolean passed() { return passed; }



public String getStatus() {
  return sb.toString(); 
  
}



}





