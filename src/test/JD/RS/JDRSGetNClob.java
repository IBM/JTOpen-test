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


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;

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
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetNClob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }





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



