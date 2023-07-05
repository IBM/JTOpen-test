////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAUDBCrash2Vars.java
//
// Description:  Same as JTACrash2Vars.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBCrash2Vars
//
////////////////////////////////////////////////////////////////////////
//------------------- Maintenance-Change Activity ------------------
//
//  Flag  Reason     Rel Lvl   Date    PGMR     Comments
//  ---- --------    ------- -------- ------- ---------------------------
//                           06/14/02 JEBER    NEW PART 
////////////////////////////////////////////////////////////////////////
package test;


public class JTAUDBCrash2Vars extends JTAStdCrash2Vars {

/**
Constructor.
**/

/**
 * All testcases inheritied from JTAStdCrash2Vars
 */
    public static void main(String args[]) {

	useUDBDataSource = true; 
	try { 
	JTAStdCrash2Vars.main(args); 
        } catch (Exception e) {
          e.printStackTrace(); 
        }
    } 

}
