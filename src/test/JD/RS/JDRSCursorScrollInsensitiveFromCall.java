///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSCursorScrollInsensitiveFromCall.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;



/**
Testcase JDRSCursorScrollInsensitiveFromCall.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>relative(), absolute(), previous(), next(), isLast(), ast(), isFirst(), first(), isBeforeFirst(), isBeforeLast(), getRow()
</ul>
**/
public class JDRSCursorScrollInsensitiveFromCall
extends JDRSCursorScroll {


/**
Constructor.
**/
    public JDRSCursorScrollInsensitiveFromCall (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDRSCursorScrollInsensitiveFromCall",
               namesAndVars, runMode, fileOutputStream,
               password);
	cursorFromCall = true;
	cursorSensitive = false; 
    }


    /*
     * All remaining behavior inherited
     */


}



