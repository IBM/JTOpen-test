///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSCursorScrollSensitiveFromCall.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;



/**
Testcase JDRSCursorScrollSensitiveFromCall.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>relative(), absolute(), previous(), next(), isLast(), ast(), isFirst(), first(), isBeforeFirst(), isBeforeLast(), getRow()
</ul>
**/
public class JDRSCursorScrollSensitiveFromCall
extends JDRSCursorScroll {


/**
Constructor.
**/
    public JDRSCursorScrollSensitiveFromCall (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDRSCursorScrollSensitiveFromCall",
               namesAndVars, runMode, fileOutputStream,
               password);
	cursorFromCall = true;
	cursorSensitive = true; 
    }


    /*
     * All remaining behavior inherited
     */


}



