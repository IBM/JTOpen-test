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

package test;


import java.io.FileOutputStream;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.FTP;


/**
  * SetupPgmCall "testcase".
**/
public class SetupPgmCall extends Testcase
{

  AS400 PwrSys;
  String PwrPwd;

  /**
  Constructor.
  **/
  public SetupPgmCall(AS400            systemObject,
                   Vector           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   
                   String           password,
                   AS400            PwrSys,
                   String           PwrPwd)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "SetupPgmCall", 1,
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
      output_.println("Creating master save files ...");
      cmd.setCommand("CRTSAVF QGPL/W95LIB");
      cmd.run();
      cmd.setCommand("CRTSAVF QGPL/JAVASP");
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

      port = asftp.receive("w95lib.savf");
      rsftp.send(port, "w95lib.savf");
      asftp.readResponse();
      asftp.readResponse();

      rsftp.sendCommand("quit");
      asftp.sendCommand("quit");
*/
//@B0A - start

      FTP os400 = new FTP(PwrSys.getSystemName(), PwrSys.getUserId(), PwrPwd);
      os400.cd("QGPL");
      os400.setDataTransferType(FTP.BINARY);

      {
        output_.println("Transferring W95LIB.SAVF ...");
        boolean ok  = os400.put("test/w95lib.savf", "w95lib.savf");
        if (!ok) {
          failed("Unable to ftp test/w95lib.savf to w95lib.savf"); 
          return; 
        }
      }

      {
        output_.println("Transferring JAVASP.SAVF ...");
        boolean ok = os400.put("test/javasp.savf", "javasp.savf");
        if (!ok) {
          failed("Unable to ftp test/javasp.savf to javasp.savf"); 
          return; 
        }
        
      }

      os400.disconnect();
//@B0A - end

      // Restore objects from save files to appropriate locations
      output_.println("Restoring objects and libraries...");
      cmd.setCommand("RSTLIB SAVLIB(W95LIB) DEV(*SAVF) SAVF(QGPL/W95LIB)");
      cmd.run();
      cmd.setCommand("RSTLIB SAVLIB(JAVASP) DEV(*SAVF) SAVF(QGPL/JAVASP)");
      cmd.run();

      // Delete the necessary save files.
      output_.println("Deleting master save files ...");
      cmd.setCommand("DLTF QGPL/W95LIB");
      cmd.run();
      cmd.setCommand("DLTF QGPL/JAVASP");
      cmd.run();

      // Make sure there is enough authority to the PROGS file (needed by SFTestcase Var 22 ) 
      cmd.setCommand("GRTOBJAUT OBJ(W95LIB/PROGS) OBJTYPE(*FILE) USER(*PUBLIC) AUT(*USE)");
      cmd.run(); 
      output_.println("Setup of CmdCall on "+PwrSys.getSystemName()+" is complete.");
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }
}


