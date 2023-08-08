///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetNClob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;

import java.util.Hashtable;



/**
Testcase JDRSGetNClob.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getNClob()
</ul>
**/
public class JDRSGetNClob
extends JDRSGetClob
{





/**
Constructor.
**/
   public JDRSGetNClob (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, "JDRSGetNClob",
             namesAndVars, runMode, fileOutputStream,
             password);

      methodName = "getNClob"; 
   }


   boolean isLevel() {
       return isJdbc40(); 
   }

   boolean checkLevel() {
       if (getDriver() == JDTestDriver.DRIVER_JCC) {
	   notApplicable("jcc does not support getNClob method"); 
	   return false; 
       }
       return checkJdbc40(); 
   } 

/* Everything else inherited */




}



