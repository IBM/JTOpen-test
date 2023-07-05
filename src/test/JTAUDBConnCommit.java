////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAUDBConnCommit.java
//
// Description:  Same as JTAConnCommit.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBConnCommit
//
////////////////////////////////////////////////////////////////////////
//------------------- Maintenance-Change Activity ------------------
//
//  Flag  Reason     Rel Lvl   Date    PGMR     Comments
//  ---- --------    ------- -------- ------- ---------------------------
//                           06/14/02 JEBER    NEW PART 
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

public class JTAUDBConnCommit extends JTAStdConnCommit {

/**
Constructor.
**/
   public JTAUDBConnCommit (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBConnCommit",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      isNTS=true; 
   }


/**
 * All testcases inheritied from JTAStdConnCommit
 */


}
