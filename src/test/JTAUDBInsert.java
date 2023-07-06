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

public class JTAUDBInsert extends JTAStdInsert {

/**
Constructor.
**/
   public JTAUDBInsert (AS400 systemObject,
                    Hashtable namesAndVars,
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
