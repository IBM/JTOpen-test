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
// File Name:    JTAUDBCrash2Vars.java
//
// Description:  Same as JTACrash2Vars.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBCrash2Vars
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

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
