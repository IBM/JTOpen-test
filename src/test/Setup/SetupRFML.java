///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SetupRFML.java
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

import test.Testcase;


/**
  * SetupRFML "testcase".
**/
public class SetupRFML extends Testcase
{

  AS400 PwrSys;
  String PwrPwd;

  /**
  Constructor.
  **/
  public SetupRFML(AS400            systemObject,
                   Vector           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   
                   String           password,
                   AS400            PwrSys,
                   String           PwrPwd)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "SetupRFML", 1,
          variationsToRun, runMode, fileOutputStream, password);
    this.PwrSys = PwrSys;
    this.PwrPwd = PwrPwd;
  }


/**
  Performs setup operations to prepare a system for Program Call testing.
  **/
  public void Var001()
  {
    CommandCall cmd = new CommandCall(PwrSys);
    try
    {

      // Create the necessary save files in preparation
      // for object restoration.
      output_.println("Creating master save file ...");
      cmd.setCommand("CRTSAVF QGPL/RMLIB");
      cmd.run();

      // FTP files from CMVC on to the 400
      output_.println("Starting ftp...");


      FTP os400 = new FTP(PwrSys.getSystemName(), PwrSys.getUserId(), PwrPwd);
      os400.cd("QGPL");
      os400.setDataTransferType(FTP.BINARY);

      {
        output_.println("Transferring RMLIB.SAVF ...");
        boolean ok = os400.put("test/rmlib.savf", "rmlib.savf");
        if (!ok) {
          failed("Unable to ftp test/rmlib.savf to rmlib.savf"); 
          return; 
        }
      }

      os400.disconnect();

      // Restore objects from save files to appropriate locations
      output_.println("Restoring objects and libraries...");
      cmd.setCommand("RSTLIB SAVLIB(RMLIB) DEV(*SAVF) SAVF(QGPL/RMLIB)");
      cmd.run();

      // Delete the necessary save files.
      output_.println("Deleting master save files ...");
      cmd.setCommand("DLTF QGPL/RMLIB");
      cmd.run();


      output_.println("Setup of RFML on "+PwrSys.getSystemName()+" is complete.");
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }
}


