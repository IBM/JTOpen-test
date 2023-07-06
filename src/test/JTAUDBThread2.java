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
// File Name:    JTAUDBThread2.java
//
// Description:  Same as JTAThread2.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBThread2
//
////////////////////////////////////////////////////////////////////////
package test;

import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;


public class JTAUDBThread2 extends JTAStdThread2 {

/**
Constructor.
**/
   public JTAUDBThread2 (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBThread2",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      isNTS = true;             /* Fixed 09/09/2008 */ 
   }


/**
 * All testcases inheritied from JTAStdThread2
 */


}
