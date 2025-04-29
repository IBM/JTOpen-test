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
////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAUDBInsert.java
//
// Description:  Same as JTAInsert.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBInsert
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JTATest;
import test.JTAUDBTest;

public class JTAUDBInsert extends JTAStdInsert {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAUDBInsert";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAUDBTest.main(newArgs); 
   }

/**
Constructor.
**/
   public JTAUDBInsert (AS400 systemObject,
                    Hashtable<String,Vector<String>> namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBInsert",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      isNTS = true; 
   }


/**
 * All testcases inheritied from JTAStdInsert
 */


}
