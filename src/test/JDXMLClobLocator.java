///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDXMLClobLocator.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDXMLClob.java
//
// Classes:      JDXMLClob
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.util.Hashtable;




/**
Testcase JDXMLClob.  This tests the following method
of the JDBC XML class when used against a Clob locator column:

<ul>
  <li>free()
  <li>getBinaryStream()
  <li>setBinaryStream() 
  <li>getCharacterStream()
  <li>getString()
  <li>setCharacterStream()
  <li>setString()
  <li>getSource()
  <li>setResult()
</ul>

All testcases are inherited from JDXMLClob
**/
public class JDXMLClobLocator
extends JDXMLClob
{




    /**
    Constructor.
    **/
    public JDXMLClobLocator (AS400 systemObject,
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
    {
        super (systemObject, "JDXMLClobLocator",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



    /**
    Performs setup needed before running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void setup ()
    throws Exception
    {
        lobThreshold = ";lob threshold=1";
        isLocator = true; 
	super.setup(); 
    }


    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
	super.cleanup();
    }



           
    
}
