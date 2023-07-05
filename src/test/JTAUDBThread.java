////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAUDBThread.java
//
// Description:  Same as JTAThread.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBThread
//
////////////////////////////////////////////////////////////////////////
//------------------- Maintenance-Change Activity ------------------
//
//  Flag  Reason     Rel Lvl   Date    PGMR     Comments
//  ---- --------    ------- -------- ------- ---------------------------
//                           06/14/02 JEBER    NEW PART 
////////////////////////////////////////////////////////////////////////
package test;

import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

public class JTAUDBThread extends JTAStdThread {

/**
Constructor.
**/
   public JTAUDBThread (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBThread",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      isNTS = true;             /* Fixed 09/09/2008 */ 
   }


/**
 * All testcases inheritied from JTAStdThread
 */


}
