///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  INetServerTest.java
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

import test.INet.INetServerConnectionTestcase;
import test.INet.INetServerFileShareTestcase;
import test.INet.INetServerPrintShareTestcase;
import test.INet.INetServerSessionTestcase;
import test.INet.INetServerTestcase;

/**
Test driver for the ISeriesNetServer* classes.  
**/
public class INetServerTest
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
        runApplication(new INetServerTest(args));
    }


/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public INetServerTest()
        throws Exception
    {
        super();
    }


/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public INetServerTest(String[] args)
        throws Exception
    {
        super(args);
    }


/**
Creates the testcases.
**/
    public void createTestcases ()
    {
        // boolean allTestcases = (namesAndVars_.size() == 0);

        // Test the ISeriesNetServer classes.

        addTestcase(new INetServerTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, useKerberos_));

        addTestcase(new INetServerSessionTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, pwrSysUserID_, pwrSysPassword_));

        addTestcase(new INetServerConnectionTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, pwrSysUserID_, pwrSysPassword_));

        addTestcase(new INetServerFileShareTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));

        addTestcase(new INetServerPrintShareTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));

/*
        addTestcase(new NetServerBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                                  fileOutputStream_));

        addTestcase(new NetServerConnectionTestcase(systemObject_, namesAndVars_, runMode_,
                                                    fileOutputStream_, password_));

        addTestcase(new NetServerFileShareTestcase(systemObject_, namesAndVars_, runMode_,
                                                    fileOutputStream_, password_));

        addTestcase(new NetServerFileShareBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                                           fileOutputStream_));

        addTestcase(new NetServerPrintShareTestcase(systemObject_, namesAndVars_, runMode_,
                                                    fileOutputStream_, password_, pwrSys_));

        addTestcase(new NetServerPrintShareBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                                           fileOutputStream_));

        addTestcase(new NetServerSessionTestcase(systemObject_, namesAndVars_, runMode_,
                                                    fileOutputStream_, password_));
*/
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
	    out.close(); 
        // Deserialize.
        Object object2 = null;
        try 
        {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFilename_));
            object2 = in.readObject ();
            in.close(); 
        }
        finally 
        {
           File f = new File (serializeFilename_);
           f.delete();
        }
        
        return object2;
   	}
}


