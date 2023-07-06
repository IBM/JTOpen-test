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
// File Name:    JTAUDBBasic2.java
//
// Description:  Same as JTABasic2.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBBasic2
//
////////////////////////////////////////////////////////////////////////
package test;

import java.lang.*;
import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.XAException;

public class JTAUDBBasic2 extends JTAStdBasic2 {

/**
Constructor.
**/
   public JTAUDBBasic2 (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBBasic2",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      isNTS=true; 
   }


/**
 * All testcases inheritied from JTAStdBasic2
 */


}
