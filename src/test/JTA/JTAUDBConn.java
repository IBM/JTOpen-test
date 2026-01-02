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
// File Name:    JTAUDBConn.java
//
// Description:  Same as JTAConn.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBConn
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JTATest;
import test.JTAUDBTest;


public class JTAUDBConn extends JTAStdConn {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAUDBConn";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAUDBTest.main(newArgs); 
   }

/**
Constructor.
**/
   public JTAUDBConn (AS400 systemObject,
                    Hashtable<String,Vector<String>> namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password,
                      String pwrSysUid,
                      String pwrSysPwd) {
      super (systemObject, "JTAUDBConn",
             namesAndVars, runMode, fileOutputStream,
             password, pwrSysUid, pwrSysPwd);
      useUDBDataSource=true;
      //
      // Only override collection if it is a standard one
      // otherwise this was set using the -lib parameter
      //
      output_.println("JTATest.COLLECTION = "+JTATest.COLLECTION); 
      if (JTATest.COLLECTION.equals(JTATest.DEFAULT_COLLECTION)) {
	  JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      } 
      isNTS=true; 
   }


/**
 * All testcases inheritied from JTAStdConn
 */


}
