///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SetupPgmCall.java
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



/**
  * SetupPgmCall "testcase".
**/
public class SetupPgmCall extends SetupLibraryTestcase
{


  /**
  Constructor.
  **/
  public SetupPgmCall(AS400            systemObject,
                   Vector<String>           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   
                   String           password,
                   AS400            PwrSys,
                   String           PwrPwd)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "SetupPgmCall", 1,
          variationsToRun, runMode, fileOutputStream, password, PwrSys.getUserId(), PwrPwd);
  }


/**
  Performs setup operations to prepare a system for Program Call testing.
  **/
  public void Var001()
  {
    CommandCall cmd = new CommandCall(pwrSys_);
    try
    {

      restoreLibrary("w95lib.savf", "W95LIB"); 
      restoreLibrary("javasp.savf", "JAVASP"); 
     

      // Make sure there is enough authority to the PROGS file (needed by SFTestcase Var 22 ) 
      cmd.setCommand("QSYS/GRTOBJAUT OBJ(W95LIB/PROGS) OBJTYPE(*FILE) USER(*PUBLIC) AUT(*USE)");
      cmd.run(); 
      output_.println("Setup of CmdCall on "+pwrSys_.getSystemName()+" is complete.");
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }
}


