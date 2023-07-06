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
// File Name:    JTAUDBResource2.java
//
// Description:  Same as JTAResource2.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBResource2
//
////////////////////////////////////////////////////////////////////////
package test;

import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;


public class JTAUDBResource2 extends JTAResource2 {

/**
Constructor.
**/
   public JTAUDBResource2 (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBResource2",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      isNTS = true;             /* Fixed 06/27/2008 */ 
   }


/**
 * All testcases inheritied from JTAStdResource2
 */


}
