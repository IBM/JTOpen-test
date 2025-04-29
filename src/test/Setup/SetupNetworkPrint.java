///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SetupNetworkPrint.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Setup;


import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ObjectDescription;


/**
  * SetupNetworkPrint "testcase".
**/
public class SetupNetworkPrint extends SetupLibraryTestcase
{


  /**
  Constructor.
  **/
  public SetupNetworkPrint(AS400            systemObject,
                   Vector<String>           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   
                   String           password,
                   AS400            PwrSys,
                   String           PwrPwd)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "SetupNetworkPrint", 1,
          variationsToRun, runMode, fileOutputStream, password, PwrSys.getUserId(), PwrPwd);
  }


/**
  Performs setup operations to prepare a system for Program Call testing.
  **/
  public void Var001()
  {
    try { 
      restoreLibrary("npjava.savf", "NPJAVA"); 

      // Create printer device description PRT01 if it doesn't already exist.
      ObjectDescription objDesc = new ObjectDescription(pwrSys_, "/QSYS.LIB/PRT01.DEVD");
      if (!objDesc.exists())
      {
        CommandCall cmd = new CommandCall(pwrSys_);

        output_.println("Defining printer device description PRT01 ...");
        cmd.setCommand("CRTDEVPRT DEVD(PRT01) DEVCLS(*LCL) TYPE(3812) MODEL(1) PORT(1) SWTSET(1) FONT(11)");
        cmd.run();
      }

      output_.println("Setup of NetworkPrint on "+pwrSys_.getSystemName()+" is complete.");
      succeeded();
  }
    catch(Exception e)
    {
      failed(e);
    }
  }
}


