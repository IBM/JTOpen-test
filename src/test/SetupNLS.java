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

package test;


import java.io.FileOutputStream;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.FTP;


/**
  * SetupNLS "testcase".
**/
public class SetupNLS extends Testcase
{

  AS400 PwrSys;
  String PwrPwd;

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
          variationsToRun, runMode, fileOutputStream, password);
    this.PwrSys = PwrSys;
    this.PwrPwd = PwrPwd;
  }


/**
  Performs setup operations to prepare a system for JDBC testing.
  **/
  public void Var001()
  {
    CommandCall cmd = new CommandCall(PwrSys);
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

      // Create the necessary save files in preparation
      // for object restoration.
      output_.println("Creating master save file...");
      cmd.setCommand("CRTSAVF QGPL/JAVAPRIME");
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

      port = asftp.receive("javaprime.savf");
      rsftp.send(port, "javaprime.savf");
      asftp.readResponse();
      asftp.readResponse();

      rsftp.sendCommand("quit");
      asftp.sendCommand("quit");
*/
//@B0A - start
      FTP os400 = new FTP(PwrSys.getSystemName(), PwrSys.getUserId(), PwrPwd);
      os400.cd("QGPL");
      os400.setDataTransferType(FTP.BINARY);
      boolean ok =  os400.put("test/javaprime.savf", "javaprime.savf");
      if (!ok) {
        failed("FTP of test/javaprime.savf to javaprime.savf failed"); 
        return; 
      }
      os400.disconnect();
//@B0A - end

      // Restore objects from save files to appropriate locations
      output_.println("Restoring objects and libraries...");
      cmd.setCommand("RSTLIB SAVLIB(JAVAPRIME) DEV(*SAVF) SAVF(QGPL/JAVAPRIME)");
      cmd.run();

      output_.println("Copying objects to respective libraries...");
      String str1 = "CRTDUPOBJ OBJ(SMPDBCS*) FROMLIB(JAVAPRIME) OBJTYPE(*ALL) ";
      str1 += "TOLIB(JAVANLS) NEWOBJ(*SAME) DATA(*YES)";
      cmd.setCommand(str1);
      cmd.run();

      output_.println("Setup of NLS on "+PwrSys.getSystemName()+" is complete.");
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }
}



