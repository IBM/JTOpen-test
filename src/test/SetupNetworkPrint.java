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

package test;


import java.io.FileOutputStream;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.FTP;


/**
  * SetupNetworkPrint "testcase".
**/
public class SetupNetworkPrint extends Testcase
{

  AS400 PwrSys;
  String PwrPwd;

  /**
  Constructor.
  **/
  public SetupNetworkPrint(AS400            systemObject,
                   Vector           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   
                   String           password,
                   AS400            PwrSys,
                   String           PwrPwd)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "SetupNetworkPrint", 1,
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
      cmd.setCommand("CRTSAVF QGPL/NPJAVA");
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

      port = asftp.receive("npjava.savf");
      rsftp.send(port, "npjava.savf");
      asftp.readResponse();
      asftp.readResponse();

      rsftp.sendCommand("quit");
      asftp.sendCommand("quit");
*/
//@B0A - start
      FTP os400 = new FTP(PwrSys.getSystemName(), PwrSys.getUserId(), PwrPwd);
      os400.cd("QGPL");
      os400.setDataTransferType(FTP.BINARY);
      boolean ok = os400.put("test/npjava.savf", "npjava.savf");
      if (!ok) { 
        failed("ftp of test/npjava.savf to npjava.savf failed"); 
        return; 
      }
      os400.disconnect();
//@B0A - end

      // Restore objects from save files to appropriate locations
      output_.println("Restoring objects and libraries...");
      cmd.setCommand("RSTLIB SAVLIB(NPJAVA) DEV(*SAVF) SAVF(QGPL/NPJAVA)");
      cmd.run();

      // Delete the necessary save files.
      output_.println("Deleting master save file...");
      cmd.setCommand("DLTF QGPL/NPJAVA");
      cmd.run();

      // Create printer device description PRT01 if it doesn't already exist.
      ObjectDescription objDesc = new ObjectDescription(PwrSys, "/QSYS.LIB/PRT01.DEVD");
      if (!objDesc.exists())
      {
        output_.println("Defining printer device description PRT01 ...");
        cmd.setCommand("CRTDEVPRT DEVD(PRT01) DEVCLS(*LCL) TYPE(3812) MODEL(1) PORT(1) SWTSET(1) FONT(11)");
        cmd.run();
      }



      output_.println("Setup of NetworkPrint on "+PwrSys.getSystemName()+" is complete.");
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }
}


