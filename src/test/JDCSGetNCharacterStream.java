///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetNCharacterStream.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetNCharacterStream.java
//
// Classes:      JDCSGetNCharacterStream
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.io.FileOutputStream;

import java.util.Hashtable;



/**
Testcase JDCSGetNCharacterStream.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getNCharacterStream()
</ul>
**/
public class JDCSGetNCharacterStream
extends JDCSGetCharacterStream
{


/**
Constructor.
**/
   public JDCSGetNCharacterStream (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, "JDCSGetNCharacterStream",
             namesAndVars, runMode, fileOutputStream,
             password);
      getMethodName = "getNCharacterStream"; 
   }


/*
 * Everything else inherited
 */


}
