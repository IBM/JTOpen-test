///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.JTA;


import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JDTestcase;
import test.JTATest;


public class JTABasic extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTABasic";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTATest.main(newArgs); 
   }

   private String basTbl = JTATest.COLLECTION + ".CHARTAB";
   private Connection c;

/**
Constructor.
**/
   public JTABasic (AS400 systemObject,
                    Hashtable<String,Vector<String>> namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTABasic",
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
       lockSystem("JTATEST",600);

      basTbl = JTATest.COLLECTION + ".CHARTAB";
      if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
         Statement s = c.createStatement();
         s.execute("CREATE TABLE " + basTbl + " (COL1 CHAR (10 ) NOT NULL WITH DEFAULT)");
         s.close();
      }
   }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
       if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
         Statement s = c.createStatement();
         s.execute("DROP TABLE " + basTbl);
         s.close();
         c.close();
      }
       unlockSystem(); 
   }


   // TO Test:
   // Basic Transactional (JTA) tests

   public void Var001() {             // from ~kulack/JTA/jtatest/JTAEasy.java
      notApplicable("OLD JTA TEST");
      return;
   }


   public void Var002() { // from ~kulack/JTA/jtatest/JTAEnd.java
     notApplicable("OLD JTA TEST");
    return;
   }

   public void Var003() {
     notApplicable("OLD JTA TEST");
    return;
   }



   public void Var004() {
     notApplicable("OLD JTA TEST");
    return;
   }


   public void Var005() {
     notApplicable("OLD JTA TEST");
    return;
   }



}
