///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PCTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;
import com.ibm.as400.access.SystemProperties;

import test.PC.PCConstructorTestcase;
import test.PC.PCMiscTestcase;
import test.PC.PCTransformTestcase;
import test.PC.PCUsageTestcase;

/**
 Test driver for the PCML component.
 **/
public class PCTest extends TestDriver
{
  
  public static  String COLLECTION     = "JDPCTESTXXX";
    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new PCTest(args));
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }

            System.exit(0);
    }

    /**
     This ctor used for applets.
     @exception  Exception  Initialization errors may cause an exception.
     **/
    public PCTest() throws Exception
    {
        super();
        // Indicate that we want exceptions to be thrown for all parse errors.
        System.setProperty(SystemProperties.THROW_SAX_EXCEPTION_IF_PARSE_ERROR, "true");
    }

    public void setup() {
      if (testLib_ != null) { 
          COLLECTION = testLib_;
      }
    }
    
    /**
     This ctor used for applications.
     @param  args  The array of command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception.
     **/
    public PCTest(String[] args) throws Exception
    {
        super(args);
        // Indicate that we want exceptions to be thrown for all parse errors.
        System.setProperty(SystemProperties.THROW_SAX_EXCEPTION_IF_PARSE_ERROR, "true");
    }

    /**
     Creates the testcases.
     **/
    public void createTestcases()
    {
        Testcase[] testcases =
        {
            new PCMiscTestcase(),
            new PCConstructorTestcase(),
            new PCUsageTestcase(),
	    new PCTransformTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_, pwrSysUserID_, pwrSysPassword_);
            addTestcase(testcases[i]);
        }
        
    }
}
