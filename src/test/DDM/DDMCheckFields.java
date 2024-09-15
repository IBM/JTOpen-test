///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMCheckFields.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400JDBCDriver;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.SequentialFile;
import com.ibm.as400.access.SystemValue;

import test.PasswordVault;
import test.Testcase;

public class DDMCheckFields extends Testcase implements Runnable {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMCheckFields";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }

  String testLib_ = "DDMCHKFLD";
  boolean qigc_ = true; // Is DBCS installed on the 400? See setup().

  SequentialFile f = null;
  SequentialFile f2 = null;

  /**
   * Constructor. This is called from the DDMTest constructor.
   **/
  public DDMCheckFields(AS400 systemObject, Vector variationsToRun,
      int runMode, FileOutputStream fileOutputStream, 
      String testLib, String password, AS400 pwrSys) {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMCheckFields", 2, variationsToRun, runMode,
        fileOutputStream, password);
    pwrSys_ = pwrSys;

  }

  /**
   * Runs the variations requested.
   **/

  public void run() {

    boolean allVariations = (variationsToRun_.size() == 0);

    try {
      setup();
    } catch (Exception e) {
      // Testcase setup did not complete successfully
      System.out.println("Unable to complete setup; variations not run");
      return;
    }

    // Connect to the AS/400 for the record level access service
    try {
      systemObject_.connectService(AS400.RECORDACCESS);
    } catch (Exception e) {
      System.out.println("Unable to connect to the AS/400");
      return;
    }

    if ((allVariations || variationsToRun_.contains("1"))
        && runMode_ != ATTENDED) {
      Var001();
    }

    if ((allVariations || variationsToRun_.contains("2"))
        && runMode_ != ATTENDED) {

      Var002();
    }

    // Do any necessary cleanup work for the variations
    try {
      cleanup();
    } catch (Exception e) {
      System.out.println("Unable to complete cleanup.");
    }

    systemObject_.disconnectAllServices();

  }

  // Do any necessary setup work for the variations

  protected void setup() throws Exception {

    try {
      boolean proxy = false;

      try {
        Class.forName("com.ibm.as400.access.SystemValue");
      } catch (Throwable e) {
        output_
            .println("DDMCheckFields: could not load SystemValue (running proxy?).  Will run test anyway. ");
        proxy = true;
        qigc_ = true;
      }

      if (!proxy) {
        // First check to see if DBCS is installed on the system;
        // if not, we might as well not even run this testcase.
        SystemValue sv = new SystemValue(systemObject_, "QIGC");
        String val = ((String) sv.getValue()).trim();
        if (val.equals("0")) {
          qigc_ = false;
          output_
              .println("DDMCheckFields:  QIGC indicates DBCS not installed on "
                  + systemObject_.getSystemName() + ". Changing system.");

          CommandCall cmd = new CommandCall(systemObject_);

          /* SetIGC */
          try {

            cmd.run("SETIGC CHANGE(*BOTH) VALUE(*YES)");
          } catch (Exception e) {
            System.out.println("Could not change SETIGC to *YES ");
            e.printStackTrace();
          }

        }

      }
    } catch (Exception e) {
      System.out.println("Unexpected Exception");

    }

    try {
      CommandCall cmd = new CommandCall(systemObject_);
      cmd.run("CRTLIB LIB(" + testLib_ + ")");

    } catch (Exception e) {
      System.out.println("Could not create library or table " + testLib_);
    }

    try {

      f = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/DDMCHKFLD1.FILE/%FILE%.MBR");
    } catch (Exception e) {
      System.out.println("Could not create sequential file DDMCHKFLD1.");
    }

    try {
      f2 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/DDMCHKFLD2.FILE/%FILE%.MBR");

    } catch (Exception e) {
      System.out.println("Could not create sequential file DDMCHKFLD2.");
    }

  }

  // Do any necessary cleanup

  protected void cleanup() throws Exception {

    CommandCall cmd = new CommandCall(systemObject_);

    // Delete the library and file
    try {

      deleteLibrary(cmd, testLib_);

    } catch (Exception e) {
      System.out.println("Could not delete library " + testLib_);
      e.printStackTrace();
    }

    /* Restore value back */
    if (qigc_ == false) {
      try {

        cmd.run("SETIGC CHANGE(*BOTH) VALUE(*NO)");
      } catch (Exception e) {
        System.out.println("Could not change SETIGC back to *NO ");
        e.printStackTrace();
      }

    }

  }

  /**
   * Var001 verify that CHAR and VARCHAR fields behave correctly.
   **/
  public void Var001() {

    setVariation(1);

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
    AS400 as400 = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

    try {
      AS400JDBCDriver driver = new  AS400JDBCDriver();
      Connection connection_ = driver.connect(as400); 
   

      Statement s = connection_.createStatement();

      s.executeUpdate("CREATE TABLE " + testLib_ + "." + "DDMCHKFLD1"
          + " (CHAR10 CHAR (10) NOT NULL, VARCHAR10 VARCHAR (10) NOT NULL)");
      s.close();
      connection_.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      boolean status = false;
      boolean status2 = false;

      f.setRecordFormat();
      f.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      Record record = new Record(f.getRecordFormat());

      record.setField(0, "\uff21\uff21\uff21\uff21");
      record.setField(1, "\uff21\uff21\uff21\uff21");
      f.write(record);

      if (record.getField(0) == "\uff21\uff21\uff21\uff21")
        status = true;

      if (record.getField(1) == "\uff21\uff21\uff21\uff21")
        status2 = true;

      if (!status || !status2)
        failed("The two fields are different and they should be equal. AS400="
            + as400);

      else
        succeeded();

      f.close();
      f.delete();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Var002 verify that GRAPHIC and VARGRAPHIC fields behave correctly.
   **/

  public void Var002() {
    setVariation(2);
    boolean status = false;
    boolean status2 = false;

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
    AS400 as400 = new AS400(systemObject_.getSystemName(),
        systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

    try {

      AS400JDBCDriver driver = new AS400JDBCDriver();
      Connection connection_ = driver.connect(as400); 
      
      Statement s = connection_.createStatement();

      s.executeUpdate("CREATE TABLE "
          + testLib_
          + "."
          + "DDMCHKFLD2"
          + " (GRA10 GRAPHIC(10) CCSID 1200 NOT NULL, VARGRA10 VARGRAPHIC(10) CCSID 1200 NOT NULL)");

      s.close();
      connection_.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    try {

      f2.setRecordFormat();
      f2.open(AS400File.READ_WRITE, 4, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      Record record = new Record(f2.getRecordFormat());

      record.setField(0, "\uff21\uff21\uff21\uff21");
      record.setField(1, "\uff21\uff21\uff21\uff21");
      f2.write(record);

      if (record.getField(0) == "\uff21\uff21\uff21\uff21")
        status = true;

      if (record.getField(1) == "\uff21\uff21\uff21\uff21")
        status2 = true;

      if (!status || !status2)
        failed("The two fields are different and they should be equal. AS400="
            + as400);

      else
        succeeded();

      f2.close();
      f2.delete();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
