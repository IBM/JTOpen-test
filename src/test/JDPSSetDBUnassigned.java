///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetDBUnassigned.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.FileOutputStream;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

public class JDPSSetDBUnassigned extends JDPSSetDB2Unassigned {

    public JDPSSetDBUnassigned (AS400 systemObject,
            Hashtable namesAndVars,
            int runMode,
            FileOutputStream fileOutputStream,
            
            String password)
    {
        super (systemObject, "JDPSSetDBUnassigned",
                namesAndVars, runMode, fileOutputStream,
                password);
	methodName = "setDBUnassigned"; 
    }




}
