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
// File Name:    JTAUDBDelete.java
//
// Description:  Same as JTADelete.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBDelete
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JTATest;
import test.JTAUDBTest;


public class JTAUDBDelete extends JTAStdDelete {

/**
Constructor.
**/
   public JTAUDBDelete (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBDelete",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      isNTS = true;             /* Fixed 06/27/2008 */ 

   }


/**
 * All testcases inheritied from JTAStdDelete
 */


}
