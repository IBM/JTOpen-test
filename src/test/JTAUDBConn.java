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
package test;

import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;


public class JTAUDBConn extends JTAStdConn {

/**
Constructor.
**/
   public JTAUDBConn (AS400 systemObject,
                    Hashtable namesAndVars,
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
      System.out.println("JTATest.COLLECTION = "+JTATest.COLLECTION); 
      if (JTATest.COLLECTION.equals(JTATest.DEFAULT_COLLECTION)) {
	  JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      } 
      isNTS=true; 
   }


/**
 * All testcases inheritied from JTAStdConn
 */


}
