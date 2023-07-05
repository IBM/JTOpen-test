///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NetServerTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
Test driver for the NetServer* classes.  
**/
public class NetServerTest
extends TestDriver
{


    // Private data.
    private static final String serializeFilename_      = "NetServerTest.ser";


/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public static void main(String args[])
        throws Exception
    {
        runApplication(new NetServerTest(args));
    }


/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public NetServerTest()
        throws Exception
    {
        super();
    }


/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public NetServerTest(String[] args)
        throws Exception
    {
        super(args);
    }


/**
Creates the testcases.
**/
    public void createTestcases ()
    {
        if (pwrSys_.getUserId() == null ||
            pwrSys_.getUserId().trim().length() == 0)
        {
          usage();
        }

        // boolean allTestcases = (namesAndVars_.size() == 0);

        // Test the NetServer and NetServerBeanInfo classes.

        addTestcase(new NetServerBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                                  fileOutputStream_));

        addTestcase(new NetServerConnectionTestcase(systemObject_, namesAndVars_, runMode_,
                                                    fileOutputStream_, password_, pwrSys_, pwrSysUserID_, pwrSysPassword_));

        addTestcase(new NetServerFileShareTestcase(systemObject_, namesAndVars_, runMode_,
                                                    fileOutputStream_, password_, pwrSys_));

        addTestcase(new NetServerFileShareBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                                           fileOutputStream_));

        addTestcase(new NetServerPrintShareTestcase(systemObject_, namesAndVars_, runMode_,
                                                    fileOutputStream_, password_, pwrSys_));

        addTestcase(new NetServerPrintShareBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                                           fileOutputStream_));

        // Add pwrSys since QZLSENSS() API requires IOSYSCFG auth                          @A1C
        addTestcase(new NetServerSessionTestcase(systemObject_, namesAndVars_, runMode_, //@A1C
                                                 fileOutputStream_, password_, pwrSys_, pwrSysUserID_, pwrSysPassword_));

        // Run NetServerTestcase last, since it has variations that end and restart the NetServer job.  That terminates the prestarted connection session, and causes some other testcase variations to fail.
        addTestcase(new NetServerTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, pwrSysUserID_, pwrSysPassword_ ));
    }

 /**
Serializes and deserializes an object.

@param  object  The object.
@return         The deserialized object.
 @exception  Exception  If an exception occurs.
**/
    public static Object serialize (Object object)
        throws Exception
    {
	    // Serialize.
	    ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFilename_));
	    out.writeObject (object);
	    out.flush ();

        // Deserialize.
        Object object2 = null;
        try 
        {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFilename_));
            object2 = in.readObject ();
        }
        finally 
        {
           File f = new File (serializeFilename_);
           f.delete();
        }
        
        return object2;
   	}

/**
 * Display usage information for the Testcase.
 * This method never returns.
**/
  public void usage()
  {
    System.out.println ("usage: java test.NetServerTest -system systemName -uid userID -pwd password -pwrSys powerUserID,password");
    System.exit(0);
  }

}


