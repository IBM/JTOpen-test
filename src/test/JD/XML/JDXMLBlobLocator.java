///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDXMLBlobLocator.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDXMLBlob.java
//
// Classes:      JDXMLBlob
//
////////////////////////////////////////////////////////////////////////

package test.JD.XML;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;




/**
Testcase JDXMLBlob.  This tests the following method
of the JDBC XML class when used against a blob locator column:

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

All testcases are inherited from JDXMLBlob
**/
public class JDXMLBlobLocator
extends JDXMLBlob
{




    /**
    Constructor.
    **/
    public JDXMLBlobLocator (AS400 systemObject,
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
    {
        super (systemObject, "JDXMLBlobLocator",
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
      suffix="L";
        lobThreshold = ";lob threshold=1";
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
