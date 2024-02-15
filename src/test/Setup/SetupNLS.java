///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SetupNLS.java
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
import com.ibm.as400.access.FTP;



/**
  * SetupNLS "testcase".
**/
public class SetupNLS extends SetupLibraryTestcase
{


  /**
  Constructor.
  **/
  public SetupNLS(AS400            systemObject,
                   Vector           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   
                   String           password,
                   AS400            PwrSys,
                   String           PwrPwd)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "SetupNLS", 1,
          variationsToRun, runMode, fileOutputStream, password, PwrSys.getUserId(),PwrPwd );
  }


/**
  Performs setup operations to prepare a system for JDBC testing.
  **/
  public void Var001()
  {
    CommandCall cmd = new CommandCall(pwrSys_);
    try
    {
      // Cleanup the necessary libraries
      output_.println("Deleting library JAVANLS...");
      deleteLibrary(cmd, "JAVANLS");
      output_.println("Re-creating library JAVANLS...");
      cmd.setCommand("CRTLIB LIB(JAVANLS) AUT(*ALL)");
      cmd.run();

      output_.println("Deleting library JAVAPRIME...");
      deleteLibrary(cmd,"JAVAPRIME");

      output_.println("Re-creating library JAVAPRIME...");
      cmd.setCommand("CRTLIB LIB(JAVANLS) AUT(*ALL)");
      cmd.run();

      
      restoreLibrary("javaprime.savf", "JAVAPRIME"); 

      output_.println("Copying objects to respective libraries...");
      String str1 = "CRTDUPOBJ OBJ(SMPDBCS*) FROMLIB(JAVAPRIME) OBJTYPE(*ALL) ";
      str1 += "TOLIB(JAVANLS) NEWOBJ(*SAME) DATA(*YES)";
      cmd.setCommand(str1);
      cmd.run();

      output_.println("Setup of NLS on "+pwrSys_.getSystemName()+" is complete.");
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }
}



