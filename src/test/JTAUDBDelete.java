////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAUDBDelete.java
//
// Description:  Same as JTADelete.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBDelete
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
