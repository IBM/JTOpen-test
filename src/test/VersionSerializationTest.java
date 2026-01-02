///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  VersionSerializationTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.SystemProperties;

import test.misc.VersionSerializationMessageTestcase;

/**
 Test driver for the Message components.  Refer to TestDriver for calling syntax.
 **/
public class VersionSerializationTest extends TestDriver
{

  /**
   * 
   */
  static final boolean runningOnThread_;
  static {
    boolean onThread = false;
    if (onAS400_)
    {
      String threadSafe = SystemProperties.getProperty(SystemProperties.PROGRAMCALL_THREADSAFE);
      // Note: The 'Message' component isn't sensitive to CommandCall thread-safety.

      String mustUseSockets = SystemProperties.getProperty(SystemProperties.AS400_MUST_USE_SOCKETS);

      if ((threadSafe != null && threadSafe.equalsIgnoreCase("true")) &&
          (mustUseSockets == null || !mustUseSockets.equalsIgnoreCase("true")))
      {
        // We are running natively and on-thread.
        onThread = true;
      }
    }
    runningOnThread_ = onThread;
  }

    /**
     Main for running standalone application tests.
     @param  args  The command line arguments.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new VersionSerializationTest(args));
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }

            System.exit(0);
    }

    /**
     Constructs an object for applets.
     @exception  Exception  If an exception occurs.
     **/
    public VersionSerializationTest() throws Exception
    {
        super();
    }

    /**
     Constructs an object for testing applications.
     @param  args  The command line arguments.
     @exception  Exception  If an exception occurs.
     **/
    public VersionSerializationTest(String[] args) throws Exception
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
            // MessageFile testcases.
            new VersionSerializationMessageTestcase(),
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_, pwrSysUserID_, pwrSysPassword_);
            testcases[i].setProxy5(proxy5_); 
            addTestcase(testcases[i]);
        }
    }
}
