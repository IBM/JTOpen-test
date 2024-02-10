///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDSMisc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDSMisc.java
//
// Classes:      JDDSMisc
//
////////////////////////////////////////////////////////////////////////

package test.JD.DS;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.sql.*;



/**
Testcase JDDSMisc.  This tests the following methods
of the JDBC Driver class:

<ul>
<li>toString()
</ul>
**/
public class JDDSMisc
extends JDTestcase {



   // Private data.
   private Object dataSource_;


/**
Constructor.
**/
   public JDDSMisc (AS400 systemObject,
                    Hashtable<?,?> namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password)
   {
      super (systemObject, "JDDSMisc",
             namesAndVars, runMode, fileOutputStream,
             password);
   }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
   protected void setup ()
   throws Exception
   {
      if (isJdbc20StdExt () &&  getDriver() == JDTestDriver.DRIVER_NATIVE) {
         dataSource_ = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2DataSource");
      }
   }



   // TO Test:
   // The only thing that I can think of to put in here so far is
   // the toString method.  This is where we should add any interesting,
   // or weird test scenarios in the future.

/**
toString() - There are no testcases written yet.
**/
   public void Var001 ()
   {
      if (checkJdbc20StdExt () && checkNative()) {
         try {
            String s = dataSource_.toString();
            System.out.println(s);
            assertCondition(s.length() > 0); 
         } catch (Exception e) {
            failed(e, "Unexpected exception"); 
         }
      }
   }


}



