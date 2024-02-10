///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SetupRLA.java
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
  * SetupRLA "testcase".
**/
public class SetupRLA extends Testcase
{

  AS400 PwrSys;
  String PwrPwd;

  /**
  Constructor.
  **/
  public SetupRLA(AS400            systemObject,
                   Vector           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   
                   String           password,
                   AS400            PwrSys,
                   String           PwrPwd)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "SetupRLA", 1,
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
      output_.println("Creating master save file...");
      cmd.setCommand("CRTSAVF QGPL/DDMTESTSAV");
      cmd.run();

      // FTP files from CMVC on to the 400
      output_.println("Starting ftp...");

/*@B0D
      // Note: The FTPConnection class is currently located in JDTestUtilities.java.
      String port = null;
      FTPConnection rsftp = new FTPConnection();
      FTPConnection asftp = new FTPConnection();

      rsftp.openConnection(rs6000, rsUserId, rsPassword);
      rsftp.cwd("/as400/v4r5m0t.jacl/cur/cmvc/java.pgm/yjac.jacl/test");
      rsftp.setBinary();

      asftp.openConnection(PwrSys.getSystemName(), PwrSys.getUserId(), PwrPwd);
      asftp.cwd("QGPL");
      asftp.setBinary();

      port = asftp.receive("ddmtestsav.savf");
      rsftp.send(port, "ddmtestsav.savf");
      asftp.readResponse();
      asftp.readResponse();

      rsftp.sendCommand("quit");
      asftp.sendCommand("quit");
*/
//@B0A - start
      FTP os400 = new FTP(PwrSys.getSystemName(), PwrSys.getUserId(), PwrPwd);
      os400.cd("QGPL");
      os400.setDataTransferType(FTP.BINARY);
      boolean ok  = os400.put("test/ddmtestsav.savf", "ddmtestsav.savf");
      if (!ok) { 
        failed("Unable to FtP test/ddmtestsavf.savf to ddmtestsav.savf"); 
      }
      os400.disconnect();
//@B0A - end

      // Restore objects from save files to appropriate locations
      output_.println("Restoring objects and libraries...");
      cmd.setCommand("RSTLIB SAVLIB(DDMTESTSAV) DEV(*SAVF) SAVF(QGPL/DDMTESTSAV)");
      cmd.run();

      // Delete the necessary save files.
      output_.println("Deleting master save file...");
      cmd.setCommand("DLTF QGPL/DDMTESTSAV");
      cmd.run();

      output_.println("Setup of RLATestcase on "+PwrSys.getSystemName()+" is complete.");
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }
}


