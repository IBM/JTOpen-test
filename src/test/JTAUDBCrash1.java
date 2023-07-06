////////////////////////////////////////////////////////////////////////
//
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
// File Name:    JTAUDBCrash1.java
//
// Description:  Same as JTACrash1.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBCrash1
//
////////////////////////////////////////////////////////////////////////
package test;

import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;


public class JTAUDBCrash1 extends JTAStdCrash1 {

/**
Constructor.
**/
   public JTAUDBCrash1 (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBCrash1",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      isNTS = true;             /* Fixed 06/27/2008 */ 
   }


/**
 * All testcases inheritied from JTAStdCrash1
 */


}
