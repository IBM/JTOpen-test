///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMLocking.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;

import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.SequentialFile;

import test.DDMTest;
import test.TestDriverStatic;
import test.Testcase;

import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;


/**
 *Testcase DDMLocking.  This test class verifies valid and invalid usage of:
 *<ul compact>
 *<li>lock()
 *<li>getExplicitLocks()
 *<li>releaseExplicitLocks()
 *</ul>
**/
public class DDMLocking extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMLocking";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  BufferedReader inBuf_ = new BufferedReader(new InputStreamReader(System.in));
  long start;
  long time;
  String testLib_ = null;
  String journalLib_ = null; 
  String fileName_ = null;
  private boolean brief_ = TestDriverStatic.brief_;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMLocking(AS400            systemObject,
                    Vector           variationsToRun,
                    int              runMode,
                    FileOutputStream fileOutputStream,
                    
                    String testLib,
                    AS400 pwrsys)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMLocking", 22, variationsToRun, runMode,
          fileOutputStream);
    setupTestLib(); 
    pwrSys_ = pwrsys;
  }

  public void setupTestLib() {
      if (baseTestDriver_ == null) {
	  testLib_ = DDMTest.COLLECTION; 
      }  else { 
	  testLib_ = baseTestDriver_.getTestLib();
      }
      if (testLib_.length() > 8) {
	  testLib_ = testLib_.substring(0,8); 
      }
      journalLib_ = testLib_ + "JN"; 
      testLib_ = testLib_ + "DM";
      
  }
  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);
    setupTestLib();

    // Connect to the AS/400 for the record level access service
    try
    {
      systemObject_.connectService(AS400.RECORDACCESS);
    }
    catch (Exception e)
    {
      System.out.println("Unable to connect to the AS/400");
      return;
    }


    // Do any necessary setup work for the variations
    try
    {
      output_.println("Performing any necessary cleanup from previous run...");
      cleanup();
    }
    catch(Exception e)
    {
      output_.println("Warning - cleanup failed.");
    }
    try
    {
      output_.println("Performing setup for DDMLocking testcase run...");
      setup();
      output_.println("Setup successful.");
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      output_.println("Unable to setup DDMLocking testcase. Variations not run.");
    }

      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("1"))
        Var001();
      if (allVariations || variationsToRun_.contains("2"))
        Var002();
      if (allVariations || variationsToRun_.contains("3"))
        Var003();
      if (allVariations || variationsToRun_.contains("4"))
        Var004();
      if (allVariations || variationsToRun_.contains("5"))
        Var005();
      if (allVariations || variationsToRun_.contains("6"))
        Var006();
      if (allVariations || variationsToRun_.contains("7"))
        Var007();
      if (allVariations || variationsToRun_.contains("8"))
        Var008();
      if (allVariations || variationsToRun_.contains("9"))
        Var009();
      if (allVariations || variationsToRun_.contains("10"))
        Var010();
      if (allVariations || variationsToRun_.contains("11"))
        Var011();
      if (allVariations || variationsToRun_.contains("12"))
        Var012();
      if (allVariations || variationsToRun_.contains("13"))
        Var013();
      if (allVariations || variationsToRun_.contains("14"))
        Var014();
      if (allVariations || variationsToRun_.contains("15"))
        Var015();
      if (allVariations || variationsToRun_.contains("16"))
        Var016();

      if (allVariations || variationsToRun_.contains("17"))
        Var017();
      if (allVariations || variationsToRun_.contains("18"))
        Var018();
      if (allVariations || variationsToRun_.contains("19"))
        Var019();
      if (allVariations || variationsToRun_.contains("20"))
        Var020();
      if (allVariations || variationsToRun_.contains("21"))
        Var021();
      if (allVariations || variationsToRun_.contains("22"))
        Var022();


    // Do any necessary cleanup work for the variations
    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      System.out.println("Unable to complete final cleanup.");
    }

    // Disconnect from the AS/400 for record the record level access service
    systemObject_.disconnectService(AS400.RECORDACCESS);
  }


  String runCommand(String command)
  {
    String msg = null;
    CommandCall cmd_ = new CommandCall();
    try
    {
      cmd_.setSystem(pwrSys_);
      // Run the command.
      cmd_.run(command);

      // If there are any messages then save the ones that potentially
      // indicate problems.
      AS400Message[] msgs = cmd_.getMessageList();
/*
    for (int i = 0; i < msgs.length; ++i)
    {
      output_.println(msgs[i]);
    }
*/
      if (msgs.length > 0 && msgs[0].getID().toUpperCase().startsWith("CPF"))
      {
        msg = msgs[0].getID().toUpperCase();
      }
    }
    catch (Exception e)
    {
      msg = e.toString();
      e.printStackTrace(output_);
    }

    return msg;
  }


/**
 @exception  Exception  If an exception occurs.
 **/
  public void setup() throws Exception
  {
    try
    {
      // Delete and recreate library DDMTEST
      CommandCall c = new CommandCall(pwrSys_);
      deleteLibrary(c, testLib_);
      deleteLibrary(c, journalLib_);

      c.run("CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
      c.run("CRTLIB LIB(" + journalLib_ + ") AUT(*ALL)");

      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
      // Create the necessary files for this testcase
      fileName_ = "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR";
      SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      f1.create(new DDMLockFormat(systemObject_), "DDMLocking file");

      // Create journal receiver and journal if it does not already exist
      String msg = runCommand("CRTJRNRCV JRNRCV("+journalLib_+"/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')");
      if (msg != null && !msg.equals("CPF7010"))
      {
        output_.println("Failure executing 'CRTJRNRCV JRNRCV("+journalLib_+"/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')'");
        output_.println(msg);
        throw new Exception("");
      }
      msg = runCommand("CRTJRN JRN("+journalLib_+"/JT4DDMJRN) JRNRCV("+journalLib_+"/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')");
      if (msg != null && !msg.equals("CPF7010"))
      {
        output_.println("Failure executing 'CRTJRN JRN("+journalLib_+"/JT4DDMJRN) JRNRCV("+journalLib_+"/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')'");
        output_.println(msg);
        throw new Exception("");
      }

      // Start journaling
      msg = runCommand("STRJRNPF FILE("+testLib_+"/DDMLOCK) JRN("+journalLib_+"/JT4DDMJRN)");
      if (msg != null)
      {
        output_.println("Failure executing 'STRJRNPF FILE("+testLib_+"/DDMLOCK) JRN("+journalLib_+"/JT4DDMJRN)'");
        output_.println(msg);
        throw new Exception("Failure executing 'STRJRNPF FILE("+testLib_+"/DDMLOCK) JRN("+journalLib_+"/JT4DDMJRN)'");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
  }


  /**
   @exception  Exception  If an exception occurs.
   **/
  public void cleanup() throws Exception
  {
    boolean success = true;
    try
    {
      // Stop journaling
      output_.println("  Ending journaling...");
      String msg = runCommand("ENDJRNPF FILE("+testLib_+"/DDMLOCK) JRN("+journalLib_+"/JT4DDMJRN)");
      if (msg != null && !msg.startsWith("CPF9803") && !msg.startsWith("CPF9801") && !msg.startsWith("CPF7032")) //7032 because on cleanup, the system might think we are not actually journaling the file.
      {
        output_.println("Failure executing 'ENDJRNPF FILE("+testLib_+"/DDMLOCK) JRN("+journalLib_+"/JT4DDMJRN)'");
        output_.println(msg);
        success = false;
      }

      // Delete the files created during setup()
      output_.println("  Deleting file...");
      msg = runCommand("DLTF FILE("+testLib_+"/DDMLOCK)");
      if (msg != null && msg.startsWith("CPF3202"))
      {
        output_.println("Error: File DDMLOCK in library "+testLib_+" is still locked by a DDM server job.");
        output_.println("       Please signon to "+systemObject_.getSystemName()+" and do a WRKOBJLCK OBJ("+testLib_+"/DDMLOCK) OBJTYPE(*FILE).");
        output_.println("       Type a '4' (End job) on the line containing the QRWTSRVR job for user QUSER.");
        output_.println("       After pressing Enter twice, use the F5 key to refresh the screen.");
        output_.println("       Verify that there are no more locks for that file.");
        output_.println("       Repeat the above procedure for WRKOBJLCK OBJ("+journalLib_+"/JT4DDMRCV) OBJTYPE(*JRNRCV).");
        output_.println("       Re-run this testcase.");
        throw new Exception("Delete file locks and re-run testcase.");
      }

      if (msg != null && !msg.startsWith("CPC2191") && !msg.startsWith("CPF2110") && !msg.startsWith("CPF2105"))
      {
        output_.println("Failure executing 'DLTF FILE("+testLib_+"/DDMLOCK)'");
        output_.println(msg);
        success = false;
      }
//      SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
//      f1.delete();

      // Delete the DDMTest library and the journal file and receiver.
      output_.println("  Deleting library...");
      CommandCall cmd_ = new CommandCall();
      cmd_.setSystem(pwrSys_);
      msg = deleteLibrary(cmd_, testLib_); 
      if (msg != null && !msg.startsWith("CPF2110"))
      {
        output_.println("Failure executing 'delete library " + testLib_ + "'");
        output_.println(msg);
        success = false;
      }
      output_.println("  Deleting journal...");
      msg = runCommand("DLTJRN "+journalLib_+"/JT4DDMJRN");
      if (msg != null && !msg.startsWith("CPF2105"))
      {
        output_.println("Failure executing 'DLTJRN "+journalLib_+"/JT4DDMJRN'");
        output_.println(msg);
        success = false;
      }
      output_.println("  Deleting receiver...");
      msg = runCommand("DLTJRNRCV JRNRCV("+journalLib_+"/JT4DDMRCV) DLTOPT(*IGNINQMSG)");
      if (msg != null && !msg.startsWith("CPF2105"))
      {
        output_.println("Failure executing 'DLTJRNRCV JRNRCV("+journalLib_+"/JT4DDMRCV) DLTOPT(*IGNINQMSG)'");
        output_.println(msg);
        success = false;
      }
    }
    catch (Exception e)
    {
      System.out.println("Cleanup unsuccessful. Delete file " + testLib_ + "/DDMLOCK if it exists.");
      e.printStackTrace(output_);
      throw e;
    }
    if (!success)
    {
      throw new Exception("Error: Cleanup was unsuccessful");
    }
    else
    {
      output_.println("Cleanup successful.");
    }
  }


  /**
   *Verify invalid use of AS400File.lock(int).
   *<ul compact>
   *<li>Specify an invalid lock level parameter.
   *<ul compact>
   *<li>Specify one less than the minimum lock value allowed.
   *<li>Specify one more than the maximum lock value allowed.
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating parameter value is not valid.
   *</ul>
  **/
  public void Var001()
  {
    setVariation(1);
    try
    {
      SequentialFile file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK - 1);
      failed("Exception didn't occur when specifying READ_ALLOWED_SHARED_WRITE_LOCK - 1.");
    }
    catch (Exception e)
    {
      if (exceptionIs(e, "ExtendedIllegalArgumentException", "lockToObtain: Parameter value is not valid."))
      {
        try
        {
          SequentialFile file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
          file.lock(AS400File.WRITE_EXCLUSIVE_LOCK + 1);
          failed("Exception didn't occur when specifying WRITE_EXCLUSIVE_LOCK + 1.");
        }
        catch (Exception e1)
        {
          if (!exceptionIs(e, "ExtendedIllegalArgumentException", "lockToObtain: Parameter value is not valid."))
          {
            failed(e1, "Wrong exception/text returned");
          }
          else
          {
            succeeded();
          }
        }
      }
      else
      {
        failed(e, "Wrong exception/text returned");
      }
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read, read/write or write.
   *</ul>
   **/
  public void Var002()
  {
    setVariation(2);
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    SequentialFile file = null;
    SequentialFile violator = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.lock(AS400File.READ_EXCLUSIVE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);

      violator = new SequentialFile(newsys, file.getPath());
      violator.setRecordFormat(new DDMLockFormat(systemObject_));
      try
      {
        violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failed("No exception with READ_ONLY");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          failed(e, "Wrong exception id with READ_ONLY.");
        }
        else
        {
          try
          {
            violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
            failed("Exception didn't occur with READ_WRITE.");
          }
          catch (AS400Exception e1)
          {
            msg = e1.getAS400Message();
            if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
            {
              failed(e1, "Wrong exception id with READ_WRITE.");
            }
            else
            {
              try
              {
                violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
                failed("Exception didn't occur with WRITE_ONLY.");
              }
              catch (AS400Exception e2)
              {
                msg = e.getAS400Message();
                if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
                {
                  failed(e2, "Wrong exception id with WRITE_ONLY.");
                }
                else
                {
                  succeeded();
                }
              }
            }
          }
        }
      }
      catch (Exception e)
      {
        failed(e, "Incorrect exception.");
      }
      // Disconnect from the system for violator.  This is to prevent the subsystem
      // from filling up with jobs.
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
    }
    catch (Exception e)
    {
	System.out.println("Unexpected error after test"); 
      e.printStackTrace(System.out); 
    }
    // Release the lock obtained
    try
    {
      file.releaseExplicitLocks();
    }
    catch (Exception e)
    {
      System.out.println("Failed to release explicit locks for " + file.getPath());
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read only.
   *</ul>
   **/
  public void Var003()
  {
    setVariation(3);
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    SequentialFile file = null;
    SequentialFile violator = null;
    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.lock(AS400File.READ_EXCLUSIVE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      violator = new SequentialFile(newsys, file.getPath());
      violator.setRecordFormat(file.getRecordFormat());
      violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      failed("Exception didn't occur.");
    }
    catch (AS400Exception e)
    {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
      {
        failed(e, "Wrong exception id.");
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Incorrect exception.");
    }

    try
    {
      file.close();
    }
    catch (Exception e)
    {
    }
    // Disconnect from the system for violator.  This is to prevent the subsystem from filling
    // up with jobs.
    if (violator != null)
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
  public void Var004()
  {
    setVariation(4);
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    SequentialFile violator = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);

      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(new DDMLockFormat(systemObject_));
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      AS400 vSys = new AS400(systemObject_); //@A1A
      vSys.setMustUseSockets(true); //@A1A
      violator = new SequentialFile(vSys, file1.getPath()); //@A1C
      violator.setRecordFormat(file2.getRecordFormat());
      try
      {
        violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failed("Exception didn't occur.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          failed(e, "Wrong exception id returned.");
        }
        else
        {
          succeeded();
        }
      }
      catch (Exception e)
      {
        failed(e, "Incorrect exception.");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception occurred");
    }

    // Release the lock on the file
    try
    {
      file1.releaseExplicitLocks();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connections
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
    if (violator != null)
    {
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
  public void Var005()
  {
    setVariation(5);
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    SequentialFile violator = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.setRecordFormat(new DDMLockFormat(systemObject_));
      file1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file1.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);

      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(file1.getRecordFormat());
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();

      AS400 vSys = new AS400(systemObject_); //@A1A
      vSys.setMustUseSockets(true); //@A1A
      violator = new SequentialFile(vSys, file1.getPath()); //@A1C
      violator.setRecordFormat(file1.getRecordFormat());
      try
      {
        violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failed("Exception didn't occur.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          failed(e, "Wrong exception id returned.");
        }
        else
        {
          succeeded();
        }
      }
      catch (Exception e)
      {
        failed(e, "Incorrect exception.");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception occurred");
    }

    // Release the lock on the file
    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connections
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
    if (violator != null)
    {
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
   **/
  public void Var006()
  {
    setVariation(6);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(new DDMLockFormat(systemObject_));
      file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      succeeded();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
    // Release the lock on the file
    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connection
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
   **/
  public void Var007()
  {
    setVariation(7);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.setRecordFormat(new DDMLockFormat(systemObject_));
      file1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file1.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(file1.getRecordFormat());
      file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      succeeded();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }

    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connection
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read, read/write or write.
   *</ul>
   **/
  public void Var008()
  {
    setVariation(8);
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    SequentialFile violator = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      violator = new SequentialFile(newsys, file.getPath());
      violator.setRecordFormat(new DDMLockFormat(systemObject_));
      try
      {
        violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failMsg.append("Exception didn't occur with READ_ONLY.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception id with READ_ONLY.");
        }
      }
      catch (Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Incorrect exception with READ_ONLY.");
      }
      try
      {
        violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failMsg.append("Exception didn't occur with READ_WRITE.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception id with READ_WRITE.");
        }
      }
      catch (Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Incorrect exception with READ_WRITE.");
      }
      try
      {
        violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failMsg.append("Exception didn't occur with WRITE_ONLY.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception id with WRITE_ONLY.");
        }
      }
      catch (Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Incorrect exception with WRITE_ONLY.");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace(System.out);
      failMsg.append("Unexpected exception."+e);
      return; 
    }
    // Release the lock obtained
    try
    {
      file.releaseExplicitLocks();
    }
    catch (Exception e)
    {
	e.printStackTrace(); 
    }
    // Disconnect from the system for violator.  This is to prevent the subsystem from filling
    // up with jobs.
    if (violator != null)
    {
	try { 
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
	} catch (Exception e) {
	    e.printStackTrace(); 
	} 
    }
    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read, read/write or write.
   *</ul>
   **/
  public void Var009()
  {
    setVariation(9);
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    SequentialFile violator = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      violator = new SequentialFile(newsys, file.getPath());
      violator.setRecordFormat(new DDMLockFormat(systemObject_));
      try
      {
        violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failMsg.append("Exception didn't occur with READ_ONLY.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception id with READ_ONLY.");
        }
      }
      catch (Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Incorrect exception with READ_ONLY.");
      }
      try
      {
        violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failMsg.append("Exception didn't occur with READ_WRITE.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception id with READ_WRITE.");
        }
      }
      catch (Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Incorrect exception with READ_WRITE.");
      }
      try
      {
        violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failMsg.append("Exception didn't occur with WRITE_ONLY.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception id with WRITE_ONLY.");
        }
      }
      catch (Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Incorrect exception with WRITE_ONLY.");
      }
    }
    catch (Exception e)
    {
	e.printStackTrace(System.out);
	failMsg.append("Unexpected exception."+e);
    }
    // Close the file
    try
    {
      file.releaseExplicitLocks();
      file.close();
    }
    catch (Exception e)
    {
    }
    // Disconnect from the system for violator.  This is to prevent the subsystem from filling
    // up with jobs.
    if (violator != null)
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
  public void Var010()
  {
    setVariation(10);
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    SequentialFile violator = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(new DDMLockFormat(systemObject_));
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      AS400 vSys = new AS400(systemObject_); //@A1A
      vSys.setMustUseSockets(true); //@A1A
      violator = new SequentialFile(vSys, file1.getPath()); //@A1C
      violator.setRecordFormat(file2.getRecordFormat());
      try
      {
        violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        violator.close();
        failed("Exception didn't occur.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          failed(e, "Wrong exception id returned");
        }
        else
        {
          succeeded();
        }
      }
      catch (Exception e)
      {
        failed(e, "Incorrect exception.");
	return; 

      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
      return; 
    }

    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connections
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
    if (violator != null)
    {
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
  public void Var011()
  {
    setVariation(11);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    SequentialFile violator = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.setRecordFormat(new DDMLockFormat(systemObject_));
      file1.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file1.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(new DDMLockFormat(systemObject_));
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      AS400 vSys = new AS400(systemObject_); //@A1A
      vSys.setMustUseSockets(true); //@A1A
      violator = new SequentialFile(vSys, file1.getPath()); //@A1C
      violator.setRecordFormat(file2.getRecordFormat());
      try
      {
        violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        violator.close();
        failed("Exception didn't occur.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          failed(e, "Wrong exception id returned");
        }
        else
        {
          succeeded();
        }
      }
      catch (Exception e)
      {
        failed(e, "Incorrect exception.");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }

    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connections
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
    if (violator != null)
    {
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
   **/
  public void Var012()
  {
    setVariation(12);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(new DDMLockFormat(systemObject_));
      file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      succeeded();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connection
    if (file2 != null)
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
  **/
  public void Var013()
  {
    setVariation(13);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.setRecordFormat(new DDMLockFormat(systemObject_));
      file1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file1.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(file1.getRecordFormat());
      file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      succeeded();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }

    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connection
    if (file2 != null)
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
  }

  /**
   *Verify AS400File.getExplicitLocks().
   *<ul compact>
   *<li>With no locks set.
   *<li>With one lock set.
   *<li>With all locks set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No locks set will return an int[] of size 0.
   *<li>One lock set will return an int[] of size one containing the lock that was set.
   *<li>All locks set will return an int[] of size six containing the locks that were set.
   *</ul>
   **/
  public void Var014()
  {
    setVariation(14);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      if (file.getExplicitLocks().length == 0)
      {
        file.lock(AS400File.READ_EXCLUSIVE_LOCK);
        int[] locks = file.getExplicitLocks();
        if (locks.length == 1 && locks[0] == AS400File.READ_EXCLUSIVE_LOCK)
        {
          file.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
          file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
          file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
          file.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
          file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
          locks = file.getExplicitLocks();
          Vector v = new Vector(locks.length);
          for (int i = 0; i < locks.length; i++)
          {
            v.addElement(new Integer(locks[i]));
          }
          assertCondition(locks.length == 6 &&
                 v.contains(new Integer(AS400File.READ_EXCLUSIVE_LOCK)) &&
                 v.contains(new Integer(AS400File.READ_ALLOW_SHARED_READ_LOCK)) &&
                 v.contains(new Integer(AS400File.READ_ALLOW_SHARED_WRITE_LOCK)) &&
                 v.contains(new Integer(AS400File.WRITE_EXCLUSIVE_LOCK)) &&
                 v.contains(new Integer(AS400File.WRITE_ALLOW_SHARED_READ_LOCK)) &&
                 v.contains(new Integer(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK)));
        }
        else
        {
          failed("Incorrect lock information.");
        }
      }
      else
      {
        failed("Incorrect lock information.");
      }
    }
    catch (Exception e)
    {
      failed(e);
    }
    try
    {
      file.releaseExplicitLocks();
      file.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   *Verify AS400File.releaseExplicitLocks().
   *<ul compact>
   *<li>When no locks are set and the file is closed.
   *<li>When no locks are set and the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exceptions are thrown.
   *</ul>
   **/
  public void Var015()
  {
    setVariation(15);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.releaseExplicitLocks();
      file.close();
      file.releaseExplicitLocks();
      succeeded();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   *Verify AS400File.releaseExplicitLocks().
   *<ul compact>
   *<li>With one lock obtained and the file is closed.
   *<li>With one lock obtained and the file is open.
   *<li>With all locks obtained and the file is closed.
   *<li>With all locks obtained and the file is open.
   **/
  public void Var016()
  {
    setVariation(16);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      file.lock(AS400File.READ_EXCLUSIVE_LOCK);
      file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.releaseExplicitLocks();
      if (file.getExplicitLocks().length == 0)
      {
        file.close();
        file.lock(AS400File.READ_EXCLUSIVE_LOCK);
        file.releaseExplicitLocks();
        if (file.getExplicitLocks().length == 0)
        {
          file.lock(AS400File.READ_EXCLUSIVE_LOCK);
          file.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
          file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
          file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
          file.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
          file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
          file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
          file.releaseExplicitLocks();
          if (file.getExplicitLocks().length == 0)
          {
            file.close();
            file.lock(AS400File.READ_EXCLUSIVE_LOCK);
            file.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
            file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
            file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
            file.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
            file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
            file.releaseExplicitLocks();
            assertCondition(file.getExplicitLocks().length == 0);
          }
          else
          {
            failed("Incorrect lock information.");
          }
        }
        else
        {
          failed("Incorrect lock information.");
        }
      }
      else
      {
        failed("Incorrect lock information.");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   *Verify record locks when file opened for read_only with
   *commitment control locking of *CS.<br>
   *Note: This is an attended variation.
   *<ul>
   *<li>SequentialFile
   *</ul>
   *Expected results:
   *<ul>
   *<li>A record lock of *READ will be held on a read record until
   *the cursor is moved from that record.
   *</ul>
  **/
  public void Var017()
  {

      if (runMode_ == UNATTENDED) {
	  notApplicable("Attended Testcase");
	  return; 
      } 

    setVariation(17);
    notApplicable("DDM bug results in *CS and read only not obtaining record lock");
/*
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      // Populate the file
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[5];
      for (int i = 0; i < 5; ++i)
      {
        recs[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(recs);
      file.close();

      // Start commitment control for cursor stability
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.readFirst();
      System.out.println("Does a *READ record lock exist on record number 1 of file DDMLOCK in " + testLib_ + "(Y/N)? (DSPRCDLCK)");
      InputStreamReader r = new InputStreamReader(System.in);
      BufferedReader inBuf = new BufferedReader(r);
      String resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.readNext();
      System.out.println("Does a *READ record lock exist on record number 2 of file DDMLOCK in " + testLib_ + "(Y/N)? (DSPRCDLCK)");
      resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.commit();
      file.close();
      file.endCommitmentControl();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      catch(Exception e1)
      {}
    }
    succeeded();
*/
  }

  /**
   *Verify record locks when file opened for read_only with
   *commitment control locking of *ALL.<br>
   *Note: This is an attended variation.
   *<ul>
   *<li>SequentialFile
   *</ul>
   *Expected results:
   *<ul>
   *<li>A record lock of *READ will be held on a read record until
   *the cursor is moved from that record.
   *</ul>
  **/
  public void Var018()
  {
      if (runMode_ == UNATTENDED) {
	  notApplicable("Attended Testcase");
	  return; 
      } 
    setVariation(18);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      // Populate the file
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[5];
      for (int i = 0; i < 5; ++i)
      {
        recs[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(recs);
      file.close();

      // Start commitment control for cursor stability
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.readFirst();
      if (!ask("Does a *READ record lock exist on record number 1 of file DDMLOCK\n in " + testLib_ + " (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.readNext();
      if (!ask("Does a *READ record lock exist on record number 2 of file DDMLOCK\n in " + testLib_ + " (Do DSPRCDLCK on AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.commit();
      file.close();
      file.endCommitmentControl();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      catch (Exception e1)
      {
      }
    }
    succeeded();
  }

  private boolean ask(String string) {
	  System.out.println("ERROR:  Interactive tests that needs updating:"+string); 
	return false;
  }

/**
   *Verify record locks when file opened for read_only with
   *commitment control locking of *CS.<br>
   *Note: This is an attended variation.
   *<ul>
   *<li>KeyedFile
   *</ul>
   *Expected results:
   *<ul>
   *<li>A record lock of *READ will be held on a read record until
   *the cursor is moved from that record.
   *</ul>
  **/
  public void Var019()
  {
      if (runMode_ == UNATTENDED) {
	  notApplicable("Attended Testcase");
	  return; 
      } 
    setVariation(19);
    notApplicable("DDM bug results in *CS and read only not obtaining record lock");
/*
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      // Populate the file
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[5];
      for (int i = 0; i < 5; ++i)
      {
        recs[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(recs);
      file.close();

      // Start commitment control for cursor stability
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.readFirst();
      System.out.println("Does a *READ record lock exist on record number 1 of file DDMLOCK in " + testLib_ + "(Y/N)? (DSPRCDLCK)");
      InputStreamReader r = new InputStreamReader(System.in);
      BufferedReader inBuf = new BufferedReader(r);
      String resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.readNext();
      System.out.println("Does a *READ record lock exist on record number 2 of file DDMLOCK in " + testLib_ + "(Y/N)? (DSPRCDLCK)");
      resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.commit();
      file.close();
      file.endCommitmentControl();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      catch(Exception e1)
      {}
    }
    succeeded();
*/
  }

  /**
   *Verify record locks when file opened for read_only with
   *commitment control locking of *ALL.<br>
   *Note: This is an attended variation.
   *<ul>
   *<li>KeyedFile
   *</ul>
   *Expected results:
   *<ul>
   *<li>A record lock of *READ will be held on a read record until
   *the cursor is moved from that record.
   *</ul>
  **/
  public void Var020()
  {
      if (runMode_ == UNATTENDED) {
	  notApplicable("Attended Testcase");
	  return; 
      } 
    setVariation(20);
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      // Populate the file
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[5];
      for (int i = 0; i < 5; ++i)
      {
        recs[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(recs);
      file.close();

      // Start commitment control for cursor stability
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.readFirst();
      if (!ask("Does a *READ record lock exist on record number 1 of file DDMLOCK\n in " + testLib_ + " (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.readNext();
      if (!ask("Does a *READ record lock exist on record number 2 of file DDMLOCK in\n " + testLib_ + " (Do DSPRCDLCK on AS/400 specifying "+testLib_+"/DDMLOCK)?"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.commit();
      file.close();
      file.endCommitmentControl();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      catch (Exception e1)
      {
      }
    }
    succeeded();
  }


  /**
   *Verify record locks when file opened for read_write with
   *readNoUpdate_ set to true & false.
   *Note: This is an attended variation.
   *<ul>
   *<li>SequentialFile
   *</ul>
   *Expected results:
   *<ul>
   *<li>A record lock of *UPDATE will be held on a read record when
   *readNoUpdate_ is false; if readNoUpdate_ is true, there shouldn't
   *be any record locks at all.
   *</ul>
  **/
  public void Var021()
  {
      if (runMode_ == UNATTENDED) {
	  notApplicable("Attended Testcase");
	  return; 
      } 
    setVariation(21);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
//      file.setRecordFormat(new DDMLockFormat(systemObject_));
      file.delete();
      file.create(new DDMLockFormat(systemObject_), "DDMLocking file");

      // Populate the file
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[5];
      for (int i = 0; i < 5; ++i)
      {
        recs[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(recs);
      file.close();

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.setReadNoUpdate(true);

      // positionCursorToFirst() should hit positionCursorAt(int type) in AS400FileImplRemote & AS400FileImplNative.
      file.positionCursorToFirst();
      if (ask("Does record number 1 of file DDMLOCK in " + testLib_ + " have a record lock? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after positionCursorToFirst failed");
        file.close();
        return;
      }
      file.positionCursor(2);
      if (ask("Does record number 2 of file DDMLOCK in " + testLib_ + " have a record lock? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after positionCursorToIndex failed");
        file.close();
        return;
      }
      file.read(3);
      if (ask("Does record number 3 of file DDMLOCK in " + testLib_ + " have a record lock? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after read failed");
        file.close();
        return;
      }
      // readLast() should hit readRecord() in AS400FileImplRemote & AS400FileImplNative.
      file.readLast();
      if (ask("Does record number 5 of file DDMLOCK in " + testLib_ + " have a record lock? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after readLast failed");
        file.close();
        return;
      }

      file.setReadNoUpdate(false);

      // positionCursorToFirst() should hit positionCursorAt(int type) in AS400FileImplRemote & AS400FileImplNative.
      file.positionCursorToFirst();
      if (!ask("Does record number 1 of file DDMLOCK in " + testLib_ + " have a record lock of type *UPDATE? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after positionCursorToFirst failed");
        file.close();
        return;
      }
      file.positionCursor(2);
      if (!ask("Does record number 2 of file DDMLOCK in " + testLib_ + " have a record lock of type *UPDATE? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after positionCursorToIndex failed");
        file.close();
        return;
      }
      file.read(3);
      if (!ask("Does record number 3 of file DDMLOCK in " + testLib_ + " have a record lock of type *UPDATE? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after read failed");
        file.close();
        return;
      }
      // readLast() should hit readRecord() in AS400FileImplRemote & AS400FileImplNative.
      file.readLast();
      if (!ask("Does record number 5 of file DDMLOCK in " + testLib_ + " have a record lock of type *UPDATE? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after readLast failed");
        file.close();
        return;
      }

      file.close();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file.close();
        return;
      }
      catch (Exception e1)
      {
      }
    }
    succeeded();
  }



  /**
   *Verify record locks when file opened for read_write with
   *readNoUpdate_ set to true & false.
   *Note: This is an attended variation.
   *<ul>
   *<li>KeyedFile
   *</ul>
   *Expected results:
   *<ul>
   *<li>A record lock of *UPDATE will be held on a read record when
   *readNoUpdate_ is false; if readNoUpdate_ is true, there shouldn't
   *be any record locks at all.
   *</ul>
  **/
  public void Var022()
  {
      if (runMode_ == UNATTENDED) {
	  notApplicable("Attended Testcase");
	  return; 
      } 
    setVariation(22);
    KeyedFile file = null;
    Object[] key = new Object[1];
    Record[] records = null;

    try
    {
      SequentialFile f = new SequentialFile(systemObject_, fileName_);
      try
      {
        f.delete();
      }
      catch (Exception e)
      {
      }

      // Create the file and populate it first.
      file = new KeyedFile(systemObject_, fileName_);
      file.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), one key");

      // Create an array of records to write to the f1
      records = new Record[5];
      RecordFormat rf = file.getRecordFormat();
      for (short i = 1; i < 6; ++i)
      {
        records[i-1] = rf.getNewRecord();
        records[i-1].setField(0, "RECORD " + String.valueOf(i) + "  ");
      }

      // Populate f1
      file.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.write(records);
      file.close();

      // Now open the file
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.setReadNoUpdate(true);

      // positionCursorToFirst() should hit positionCursorAt(int type) in AS400FileImplRemote & AS400FileImplNative.
      file.positionCursorToFirst();
      if (ask("Does record number 1 of file DDMLOCK in " + testLib_ + " have a record lock? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after positionCursorToFirst failed");
        file.close();
        return;
      }

      key[0] = "RECORD 2  ";
      file.positionCursor(key);
      if (ask("Does record number 2 of file DDMLOCK in " + testLib_ + " have a record lock? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after positionCursorToIndex failed");
        file.close();
        return;
      }

      key[0] = "RECORD 3  ";
      file.read(key);
      if (ask("Does record number 3 of file DDMLOCK in " + testLib_ + " have a record lock? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after read failed");
        file.close();
        return;
      }

      // readLast() should hit readRecord() in AS400FileImplRemote & AS400FileImplNative.
      file.readLast();
      if (ask("Does record number 5 of file DDMLOCK in " + testLib_ + " have a record lock? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after readLast failed");
        file.close();
        return;
      }

      file.setReadNoUpdate(false);

      // positionCursorToFirst() should hit positionCursorAt(int type) in AS400FileImplRemote & AS400FileImplNative.
      file.positionCursorToFirst();
      if (!ask("Does record number 1 of file DDMLOCK in " + testLib_ + " have a record lock of type *UPDATE? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after positionCursorToFirst failed");
        file.close();
        return;
      }

      key[0] = "RECORD 2  ";
      file.positionCursor(key);
      if (!ask("Does record number 2 of file DDMLOCK in " + testLib_ + " have a record lock of type *UPDATE? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after positionCursorToIndex failed");
        file.close();
        return;
      }

      key[0] = "RECORD 3  ";
      file.read(key);
      if (!ask("Does record number 3 of file DDMLOCK in " + testLib_ + " have a record lock of type *UPDATE? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after read failed");
        file.close();
        return;
      }

      // readLast() should hit readRecord() in AS400FileImplRemote & AS400FileImplNative.
      file.readLast();
      if (!ask("Does record number 5 of file DDMLOCK in " + testLib_ + " have a record lock of type *UPDATE? (Do DSPRCDLCK on the AS/400 specifying "+testLib_+"/DDMLOCK)? "))
      {
        failed("Verification of record lock after readLast failed");
        file.close();
        return;
      }

      file.close();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file.close();
        return;
      }
      catch (Exception e1)
      {
      }
    }
    succeeded();
  }


}
