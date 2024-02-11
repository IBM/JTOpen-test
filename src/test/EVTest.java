///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  EVTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.ibm.as400.access.EnvironmentVariable;
import com.ibm.as400.access.EnvironmentVariableList;

import test.MiscAH.EVBasicTestcase;
import test.MiscAH.EVBeanInfoTestcase;
import test.MiscAH.EVListBasicTestcase;
import test.MiscAH.EVListBeanInfoTestcase;

/**
 Test driver for the EnvironmentVariable and related classes.
 **/
public class EVTest extends TestDriver
{
    private static final String SERIALIZE_FILENAME = "EVTest.ser";

    /**
     Run the test as an application.  This should be called from the test driver's main().
     @param  args  The command line arguments.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new EVTest(args));
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }
    }

    /**
     Constructs an object for applets.
     @exception  Exception  If an exception occurs.
     **/
    public EVTest() throws Exception
    {
        super();
    }

    /**
     Constructs an object for testing applications.
     @param  args  The command line arguments.
     @exception  Exception  If an exception occurs.
     **/
    public EVTest(String[] args) throws Exception
    {
        super(args);
    }

    /**
     Creates the testcases.
     **/
    public void createTestcases()
    {
        Testcase[] testcases =
        {
            new EVBasicTestcase(),
            new EVBeanInfoTestcase(),
            new EVListBasicTestcase(),
            new EVListBeanInfoTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_,  pwrSysUserID_, pwrSysPassword_);
            addTestcase(testcases[i]);
        }
    }

    /**
     Serializes and deserializes an object.
     @param  object  The object.
     @param  password  The password.
     @return  The deserialized object.
     @exception  Exception  If an exception occurs.
     **/
    public static Object serialize(EnvironmentVariable object, char[] encryptedPassword) throws Exception
    {
        // Serialize.
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(SERIALIZE_FILENAME));
        out.writeObject(object);
        out.flush();

        // Deserialize.
        EnvironmentVariable object2 = null;
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALIZE_FILENAME));
            object2 = (EnvironmentVariable)in.readObject();
        }
        finally
        {
            File f = new File(SERIALIZE_FILENAME);
            f.delete();
        }

        // Set the password.
        char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword);
        object2.getSystem().setPassword(passwordChars);
        PasswordVault.clearPassword(passwordChars);

        return object2;
    }

    /**
     Serializes and deserializes an object.
     @param  object  The object.
     @param  password  The password.
     @return  The deserialized object.
     @exception  Exception  If an exception occurs.
     **/
    public static Object serialize(EnvironmentVariableList object, char[] encryptedPassword) throws Exception
    {
        // Serialize.
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(SERIALIZE_FILENAME));
        out.writeObject(object);
        out.flush();

        // Deserialize.
        EnvironmentVariableList object2 = null;
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALIZE_FILENAME));
            object2 = (EnvironmentVariableList)in.readObject();
        }
        finally
        {
            File f = new File(SERIALIZE_FILENAME);
            f.delete();
        }

        // Set the password.
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword);
        
        object2.getSystem().setPassword(charPassword);
        PasswordVault.clearPassword(charPassword);

        return object2;
    }

    /**
     Sample property change listener.
     **/
    public static class PropertyChangeListener_ implements PropertyChangeListener
    {
        public int eventCount_ = 0;
        public PropertyChangeEvent event_ = null;

        public void propertyChange(PropertyChangeEvent event)
        {
            ++eventCount_;
            event_ = event;
        }
    }
}
