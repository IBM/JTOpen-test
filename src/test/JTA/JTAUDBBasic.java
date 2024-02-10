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
// File Name:    JTAUDBBasic.java
//
// Description:  Same as JTABasic.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBBasic
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.lang.*;
import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JTATest;
import test.JTAUDBTest;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.XAException;

public class JTAUDBBasic extends JTAStdBasic {

/**
Constructor.
**/
   public JTAUDBBasic (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBBasic",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      isNTS = true; 
   }


/**
 * All testcases inheritied from JTAStdBasic
 */


}
