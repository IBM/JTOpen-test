///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSDSMisc.java
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
// File Name:    JDSDSMisc.java
//
// Classes:      JDSDSMisc
//
////////////////////////////////////////////////////////////////////////
//
//
// Release     Date        Userid    Comments
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.SDS;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.util.Hashtable;
import javax.sql.DataSource;



/**
Testcase JDSDSMisc.  This tests the following methods
of the JDBC Driver class:

<ul>
<li>toString()
</ul>
**/
public class JDSDSMisc
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDSDSMisc";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDSDSTest.main(newArgs); 
   }



   // Private data.
   private DataSource dataSource_;


/**
Constructor.
**/
   public JDSDSMisc (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password)
   {
      super (systemObject, "JDSDSMisc",
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
      if (isJdbc20StdExt ()) {
         dataSource_ = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdDataSource");
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
      if (checkJdbc20StdExt ()) {
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



