///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMOpenClose.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;


/**
 *Testcase DDMOpenClose.  Verify valid and invalid usages of open and close
 *for KeyedFile and SequentialFile.  See the individual variations for a
 *description of the tests involved..
**/
public class DDMOpenClose extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMOpenClose";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  CommandCall cmd_;
  Record[] records_;
  String testLib_ = null;
  private boolean precleaning_ = false;
  
  /**
   Constructor.  This is called from the DDMTest constructor.
   **/
public DDMOpenClose(AS400            systemObject,
                    Vector<String> variationsToRun,
                    int              runMode,
                    FileOutputStream fileOutputStream,
                    
                    String testLib,
                    AS400 pwrsys)
{
  // Replace the third parameter with the total number of variations
  // in this testcase.
  super(systemObject, "DDMOpenClose", 48,
        variationsToRun, runMode, fileOutputStream);
  testLib_ = testLib;
  pwrSys_ = pwrsys;
  cmd_ = new CommandCall(pwrSys_);
}

/**
 Runs the variations requested.
 **/
public void run()
{
  boolean allVariations = (variationsToRun_.size() == 0);

  // Connect to the AS/400 for the record level access service
  try
  {
    systemObject_.connectService(AS400.RECORDACCESS);
  }
  catch(Exception e)
  {
    e.printStackTrace();
    return;
  }

    // Do any necessary setup work for the variations
    try
    {
      output_.println("Performing any necessary cleanup from previous run...");
      precleaning_ = true;
      cleanup();
    }
    catch(Exception e)
    {
      output_.println("Warning - cleanup failed.");
    }
    finally
    {
      precleaning_ = false;
    }
    try
    {
      output_.println("Performing setup for DDMOpenClose testcase run...");
      setup();
      output_.println("Setup successful.");
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      output_.println("Unable to setup DDMOpenClose testcase. Variations not run.");
    }

  if (runMode_ != ATTENDED)
  {
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
    if (allVariations || variationsToRun_.contains("23"))
      Var023();
    if (allVariations || variationsToRun_.contains("24"))
      Var024();
    if (allVariations || variationsToRun_.contains("25"))
      Var025();
    if (allVariations || variationsToRun_.contains("26"))
      Var026();
    if (allVariations || variationsToRun_.contains("27"))
      Var027();
    if (allVariations || variationsToRun_.contains("28"))
      Var028();
    if (allVariations || variationsToRun_.contains("29"))
      Var029();
    if (allVariations || variationsToRun_.contains("30"))
      Var030();
    if (allVariations || variationsToRun_.contains("31"))
      Var031();
    if (allVariations || variationsToRun_.contains("32"))
      Var032();
    if (allVariations || variationsToRun_.contains("33"))
      Var033();
    if (allVariations || variationsToRun_.contains("34"))
      Var034();
    if (allVariations || variationsToRun_.contains("35"))
      Var035();
    if (allVariations || variationsToRun_.contains("36"))
      Var036();
    if (allVariations || variationsToRun_.contains("37"))
      Var037();
    if (allVariations || variationsToRun_.contains("38"))
      Var038();
    if (allVariations || variationsToRun_.contains("39"))
      Var039();
    if (allVariations || variationsToRun_.contains("40"))
      Var040();
    if (allVariations || variationsToRun_.contains("41"))
      Var041();
    if (allVariations || variationsToRun_.contains("42"))
      Var042();
    if (allVariations || variationsToRun_.contains("43"))
      Var043();
    if (allVariations || variationsToRun_.contains("44"))
      Var044();
    if (allVariations || variationsToRun_.contains("45"))
      Var045();
    if (allVariations || variationsToRun_.contains("46"))
      Var046();
    if (allVariations || variationsToRun_.contains("47"))
      Var047();
    if (allVariations || variationsToRun_.contains("48"))
      Var048();
  }

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

  try
  {
    // Run the command.
    cmd_.run(command);

    // If there are any messages then save the ones that potentially
    // indicate problems.
    AS400Message[] msgs = cmd_.getMessageList();
    if (msgs.length > 0 && msgs[0].getID().toUpperCase().startsWith("CPF"))
    {
      msg = msgs[0].getID().toUpperCase();
    }
  }
  catch(Exception e)
  {
    msg = e.toString();
    e.printStackTrace(output_);
  }

  return msg;
}
  
  
/**
 @exception  Exception  If an exception occurs.
 **/
protected void setup()
  throws Exception
{
  try
  {
    CommandCall c = new CommandCall(pwrSys_);
    deleteLibrary(c, testLib_); 
    c.run();

    // Create library DDMTEST
    String msg = runCommand("CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
    if (msg != null && !msg.equals("CPF2111"))
    {
      output_.println("Failure executing 'CRTLIB LIB(" + testLib_ + ") AUT(*ALL)'");
      output_.println(msg);
      throw new Exception("");
    }

    // Create the necessary files
    SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
    f1.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");
    KeyedFile f2 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
    f2.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), one key");
    SequentialFile f3 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    f3.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");
    KeyedFile f4 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    f4.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), one key");
    KeyedFile f5 = new KeyedFile(systemObject_, "/QSYS.LIB/QGPL.LIB/KEYSRC.FILE/MBR1.MBR");
    f5.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), one key");
    KeyedFile f6 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC3.FILE/KEYSRC3.MBR");
    f6.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), one key");

    // Create an array of records to write to the files
    records_ = new Record[9];
    RecordFormat rf = f1.getRecordFormat();
    for (short i = 1; i < 10; ++i)
    {
      records_[i-1] = rf.getNewRecord();
      records_[i-1].setField(0, "RECORD " + String.valueOf(i) + "  ");
      // "  " was added to pad out to 10 characters to facilitate any
      // subsequent compares to records_[x]. CRS
    }

    // Populate the files
    f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f2.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f3.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f4.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f5.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f6.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f1.write(records_);
    f2.write(records_);
    f3.write(records_);
    f4.write(records_);
    f5.write(records_);
    f6.write(records_);
    f1.close();
    f2.close();
    f3.close();
    f4.close();
    f5.close();
    f6.close();


    // Create journal receiver and journal if it does not already exist
    msg = runCommand("CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')");
    if (msg != null && !msg.equals("CPF7010")) /* CPF7011 means already exists */ 
    {
      output_.println("Failure executing 'CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')'");
      output_.println(msg);
      throw new Exception("");
    }
    if (msg != null && msg.equals("CPF7010")) /* CPF7011 means already exists */
    {
	runCommand("ENDJRNPF FILE(*ALL) JRN(QGPL/JT4DDMJRN)"); 
        runCommand("DLTJRN QGPL/JT4DDMJRN");
	runCommand("DLTJRNRCV JRNRCV(QGPL/JT4DDMRCV) DLTOPT(*IGNINQMSG)"); 
	// Try again.... 
	msg = runCommand("CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')");
	if (msg != null && !msg.equals("CPF7010")) /* CPF7011 means already exists */ 
	{
	    output_.println("Failure executing 'CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')'");
	    output_.println(msg);
	    throw new Exception("");
	}

    }

    msg = runCommand("CRTJRN JRN(QGPL/JT4DDMJRN) JRNRCV(QGPL/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')");
    if (msg != null && !msg.equals("CPF7010"))
    {
      output_.println("Failure executing 'CRTJRN JRN(QGPL/JT4DDMJRN) JRNRCV(QGPL/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')'");
      output_.println(msg);
      throw new Exception("");
    }

    // Start journaling
    msg = runCommand("STRJRNPF FILE(" + testLib_ + "/F1M1RW) JRN(QGPL/JT4DDMJRN)");
    if (msg != null)
    {
      output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/F1M1RW) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      throw new Exception("");
    }
    msg = runCommand("STRJRNPF FILE(" + testLib_ + "/F2M1RW) JRN(QGPL/JT4DDMJRN)");
    if (msg != null)
    {
      output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/F2M1RW) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      throw new Exception("");
    }
    msg = runCommand("STRJRNPF FILE(" + testLib_ + "/KEYSRC) JRN(QGPL/JT4DDMJRN)");
    if (msg != null)
    {
      output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/KEYSRC) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      throw new Exception("");
    }
    msg = runCommand("STRJRNPF FILE(" + testLib_ + "/KEYSRC2) JRN(QGPL/JT4DDMJRN)");
    if (msg != null)
    {
      output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/KEYSRC2) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      throw new Exception("");
    }
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    throw e;
  }
}

/**
 @exception  Exception  If an exception occurs.
 **/
protected void cleanup()
  throws Exception
{
  boolean success = true;

  try
  {
    // Stop journaling
    output_.println("  Ending journaling...");
    String msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/F1M1RW) JRN(QGPL/JT4DDMJRN)");
      if (!precleaning_ && msg != null && !msg.startsWith("CPF9803") && !msg.startsWith("CPF9801") && !msg.startsWith("CPF7032")) //7032 because on cleanup, the system might think we are not actually journaling the file.
    {
      output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/F1M1RW) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      success = false;
    }
    msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/F2M1RW) JRN(QGPL/JT4DDMJRN)");
      if (!precleaning_ && msg != null && !msg.startsWith("CPF9803") && !msg.startsWith("CPF9801") && !msg.startsWith("CPF7032")) //7032 because on cleanup, the system might think we are not actually journaling the file.
    {
      output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/F2M1RW) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      success = false;
    }
    msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/KEYSRC) JRN(QGPL/JT4DDMJRN)");
      if (!precleaning_ && msg != null && !msg.startsWith("CPF9803") && !msg.startsWith("CPF9801") && !msg.startsWith("CPF7032")) //7032 because on cleanup, the system might think we are not actually journaling the file.
    {
      output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/KEYSRC) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      success = false;
    }
    msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/KEYSRC2) JRN(QGPL/JT4DDMJRN)");
      if (!precleaning_ && msg != null && !msg.startsWith("CPF9803") && !msg.startsWith("CPF9801") && !msg.startsWith("CPF7032")) //7032 because on cleanup, the system might think we are not actually journaling the file.
    {
      output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/KEYSRC2) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      success = false;
    }

    // Delete the files created during setup()
    String[] filesToDelete = new String[]
    {
      testLib_+"/F1M1RW",
      testLib_+"/KEYSRC",
      testLib_+"/F2M1RW",
      testLib_+"/KEYSRC2",
      "QGPL/KEYSRC",
      testLib_+"/KEYSRC3"
    };

    output_.println("  Deleting files...");
    for (int i=0; i<filesToDelete.length; ++i)
    {
      msg = runCommand("DLTF FILE("+filesToDelete[i]+")");
      if (msg != null && !msg.startsWith("CPF3202") && !msg.startsWith("CPF2110") && !msg.startsWith("CPF2105"))
      {
        output_.println("Error: File "+filesToDelete[i]+" is still locked by a DDM server job.");
        output_.println("       Please signon to "+systemObject_.getSystemName()+" and do a WRKOBJLCK OBJ("+filesToDelete[i]+") OBJTYPE(*FILE).");
        output_.println("       Type a '4' (End job) on the line containing the QRWTSRVR job for user QUSER.");
        output_.println("       After pressing Enter twice, use the F5 key to refresh the screen.");
        output_.println("       Verify that there are no more locks for that file.");
        output_.println("       Repeat the above procedure for WRKOBJLCK OBJ(QGPL/JT4DDMRCV) OBJTYPE(*JRNRCV).");
        output_.println("       Re-run this testcase.");
        throw new Exception("Delete file locks and re-run testcase.");
      }
    }
/*    SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
    f1.delete();
    KeyedFile f2 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
    f2.delete();
    SequentialFile f3 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    f3.delete();
    KeyedFile f4 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    f4.delete();
    KeyedFile f5 = new KeyedFile(systemObject_, "/QSYS.LIB/QGPL.LIB/KEYSRC.FILE/MBR1.MBR");
    f5.delete();
    KeyedFile f6 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC3.FILE/KEYSRC3.MBR");
    f6.delete();
*/

    // Delete the DDMTest library and the journal file.  The journal
    // receive should be automatically deleted due to the way that the
    // journal and receiver were deleted.
    output_.println("  Deleting library...");

    msg = deleteLibrary(cmd_, testLib_); 
    if (msg != null && !msg.startsWith("CPF2110"))
    {
      output_.println("Failure deleting library " + testLib_ );
      output_.println(msg);
      success = false;
    }
    output_.println("  Deleting journal...");
    msg = runCommand("DLTJRN QGPL/JT4DDMJRN");
    if (msg != null && !msg.startsWith("CPF2105"))
    {
      output_.println("Failure executing 'DLTJRN QGPL/JT4DDMJRN'");
      output_.println(msg);
      success = false;
    }
    output_.println("  Deleting receiver...");
    msg = runCommand("DLTJRNRCV JRNRCV(QGPL/JT4DDMRCV) DLTOPT(*IGNINQMSG)");
    if (msg != null && !msg.startsWith("CPF2105"))
    {
      output_.println("Failure executing 'DLTJRNRCV JRNRCV(QGPL/JT4DDMRCV) DLTOPT(*IGNINQMSG)'");
      output_.println(msg);
      success = false;
    }
  }
  catch(Exception e)
  {
    output_.println("Cleanup unsuccessful.  Some files may have been left in " + testLib_ + " and QGPL.");
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
 *Verify the ability to open a SequentialFile for READ_ONLY.
 *<ul compact>
 *<li>Specify READ_ONLY, 1, COMMIT_LOCK_LEVEL_NONE
 *<li>Commitment control has not been started
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>isOpen() returns true
 *<li>isReadOnly() returns true
 *<li>isReadWrite() returns false
 *<li>isWriteOnly() returns false
 *<li>getBlockingFactor() returns 1
 *<li>getCommitLockLevel() returns -1
 *<li>readNext() is successful
 *<li>write() throws AS400Exception indicating CPF5149
 *</ul>
 **/
public void Var001()
{
  setVariation(1);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (!file.isReadOnly())
    {
      failMsg.append("isReadOnly() not returning true.\n");
    }
    if (file.isReadWrite())
    {
      failMsg.append("isReadWrite() returning true.\n");
    }
    if (file.isWriteOnly())
    {
      failMsg.append("isWriteOnly() returning true.\n");
    }
    if (file.getBlockingFactor() != 1)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() not returning -1: " + String.valueOf(file.getCommitLockLevel()) + ".\n");
    }
    try
    {
      // Ensure that we can read a record.
      file.readNext();
    }
    catch(Exception e)
    {
      failMsg.append("readNext() failed\n");
      e.printStackTrace(output_);
    }

    try
    {
      // Attempt to write a record.
      Record record = file.getRecordFormat().getNewRecord();
      file.write(record);
      failMsg.append("Able to write a record\n");
    }
    catch(AS400Exception e)
    {
      AS400Message msg = e.getAS400Message();
      if(msg.getID().toUpperCase().indexOf("CPF5149") == -1)
      {
        failMsg.append("CPF5149 not thrown in exception\n");
        e.printStackTrace(output_);
      }
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify the ability to open a SequentialFile for READ_WRITE.
 *<ul compact>
 *<li>Specify READ_WRITE, 2, COMMIT_LOCK_LEVEL_NONE
 *<li>Commitment control has not been started
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>isOpen() returns true
 *<li>isReadOnly() returns false
 *<li>isReadWrite() returns true
 *<li>isWriteOnly() returns false
 *<li>getBlockingFactor() returns 1
 *<li>getCommitLockLevel() returns -1
 *<li>readFirst() readLast(), readPrevious() are successful
 *<li>write() is successful
 *<li>update() is successful
 *<li>delete() is successful
 *</ul>
 **/
public void Var002()
{
  setVariation(2);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 2, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.isReadOnly())
    {
      failMsg.append("isReadOnly() returning true.\n");
    }
    if (!file.isReadWrite())
    {
      failMsg.append("isReadWrite() not returning true.\n");
    }
    if (file.isWriteOnly())
    {
      failMsg.append("isWriteOnly() returning true.\n");
    }
    if (file.getBlockingFactor() != 1)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() not returning -1: " + String.valueOf(file.getCommitLockLevel()) + ".\n");
    }
    try
    {
      // Ensure that we can write a record.
      Record record = file.getRecordFormat().getNewRecord();
      file.write(record);
    }
    catch(Exception e)
    {
      failMsg.append("Exception writing to file\n");
      e.printStackTrace(output_);
    }
    try
    {
      // Ensure that we can read a record.
      file.readFirst();
      file.readLast();
      file.readPrevious();
    }
    catch(Exception e)
    {
      failMsg.append("Exception reading from file\n");
      e.printStackTrace(output_);
    }
    try
    {
      // Ensure that we can update.
      Record record = file.getRecordFormat().getNewRecord();
      file.update(record);
    }
    catch(Exception e)
    {
      failMsg.append("Exception updating file\n");
      e.printStackTrace(output_);
    }
    try
    {
      // Ensure that we can delete the record.
      file.readLast(); // need this to reposition the cursor
      file.deleteCurrentRecord();
    }
    catch(Exception e)
    {
      failMsg.append("Exception deleting from file\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify the ability to open a SequentialFile for WRITE_ONLY.
 *<ul compact>
 *<li>Specify WRITE_ONLY, 1, COMMIT_LOCK_LEVEL_NONE
 *<li>Commitment control has not been started
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>isOpen() returns true
 *<li>isReadOnly() returns false
 *<li>isReadWrite() returns true
 *<li>isWriteOnly() returns false
 *<li>getBlockingFactor() returns 1
 *<li>getCommitLockLevel() returns -1
 *<li>readPrevious() throws AS400Exception with CPF5149
 *<li>update() throws AS400Exception with CPF5149
 *<li>write() is successful
 *</ul>
 **/
public void Var003()
{
  setVariation(3);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(format);
    file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.isReadOnly())
    {
      failMsg.append("isReadOnly() returning true.\n");
    }
    if (file.isReadWrite())
    {
      failMsg.append("isReadWrite() returning true.\n");
    }
    if (!file.isWriteOnly())
    {
      failMsg.append("isWriteOnly() not returning true.\n");
    }
    if (file.getBlockingFactor() != 1)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() not returning -1: " + String.valueOf(file.getCommitLockLevel()) + ".\n");
    }
    try
    {
      // Ensure that we can write a record.
      Record record = file.getRecordFormat().getNewRecord();
      file.write(record);
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception trying to write to file.\n");
      e.printStackTrace(output_);
    }
    try
    {
      // Attempt to read a record.
      file.readPrevious();
      failMsg.append("No exception trying to read from file.\n");
    }
    catch(Exception e)
    {
    }
    try
    {
      Record record = file.getRecordFormat().getNewRecord();
      file.update(record);
      failMsg.append("No exception trying to update file.\n");
    }
    catch(Exception e2)
    {
    }
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify the ability to open a KeyedFile for READ_ONLY.
 *<ul compact>
 *<li>Specify READ_ONLY, 1, COMMIT_LOCK_LEVEL_NONE
 *<li>Commitment control has not been started
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>isOpen() returns true
 *<li>isReadOnly() returns true
 *<li>isReadWrite() returns false
 *<li>isWriteOnly() returns false
 *<li>getBlockingFactor() returns 1
 *<li>getCommitLockLevel() returns -1
 *<li>readNext() is successful
 *<li>write() throws AS400Exception indicating CPF5149
 *</ul>
 **/
public void Var004()
{
  setVariation(4);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (!file.isReadOnly())
    {
      failMsg.append("isReadOnly() not returning true.\n");
    }
    if (file.isReadWrite())
    {
      failMsg.append("isReadWrite() returning true.\n");
    }
    if (file.isWriteOnly())
    {
      failMsg.append("isWriteOnly() returning true.\n");
    }
    if (file.getBlockingFactor() != 1)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() not returning -1: " + String.valueOf(file.getCommitLockLevel()) + ".\n");
    }
    try
    {
      // Ensure that we can read a record.
      file.readNext();
    }
    catch(Exception e)
    {
      failMsg.append("readNext() failed\n");
      e.printStackTrace(output_);
    }

    try
    {
      // Attempt to write a record.
      Record record = file.getRecordFormat().getNewRecord();
      file.write(record);
      failMsg.append("Able to write a record\n");
    }
    catch(AS400Exception e)
    {
      AS400Message msg = e.getAS400Message();
      if(msg.getID().toUpperCase().indexOf("CPF5149") == -1)
      {
        failMsg.append("CPF5149 not thrown in exception\n");
        e.printStackTrace(output_);
      }
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify the ability to open a KeyedFile for READ_WRITE.
 *<ul compact>
 *<li>Specify READ_WRITE, 2, COMMIT_LOCK_LEVEL_NONE
 *<li>Commitment control has not been started
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>isOpen() returns true
 *<li>isReadOnly() returns false
 *<li>isReadWrite() returns true
 *<li>isWriteOnly() returns false
 *<li>getBlockingFactor() returns 1
 *<li>getCommitLockLevel() returns -1
 *<li>readFirst() readLast(), readPrevious() are successful
 *<li>write() is successful
 *<li>update() is successful
 *<li>delete() is successful
 *</ul>
 **/
public void Var005()
{
  setVariation(5);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 2, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.isReadOnly())
    {
      failMsg.append("isReadOnly() returning true.\n");
    }
    if (!file.isReadWrite())
    {
      failMsg.append("isReadWrite() not returning true.\n");
    }
    if (file.isWriteOnly())
    {
      failMsg.append("isWriteOnly() returning true.\n");
    }
    if (file.getBlockingFactor() != 1)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() not returning -1: " + String.valueOf(file.getCommitLockLevel()) + ".\n");
    }
    try
    {
      // Ensure that we can write a record.
      Record record = file.getRecordFormat().getNewRecord();
      file.write(record);
    }
    catch(Exception e)
    {
      failMsg.append("Exception writing to file\n");
      e.printStackTrace(output_);
    }
    Record r = null; 
    try
    {
      // Ensure that we can read a record.
      r = file.readFirst();
      r = file.readLast();
      r = file.readPrevious();
    }
    catch(Exception e)
    {
      failMsg.append("Exception reading from file\nLastRecord="+r);
      e.printStackTrace(output_);
    }
    try
    {
      // Ensure that we can update.
      Record record = file.getRecordFormat().getNewRecord();
      file.update(record);
    }
    catch(Exception e)
    {
      failMsg.append("Exception updating file\n");
      e.printStackTrace(output_);
    }
    try
    {
      // Ensure that we can delete the record.
      file.readLast(); // need this to reposition the cursor
      file.deleteCurrentRecord();
    }
    catch(Exception e)
    {
      failMsg.append("Exception deleting from file\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify the ability to open a KeyedFile for WRITE_ONLY.
 *<ul compact>
 *<li>Specify WRITE_ONLY, 1, COMMIT_LOCK_LEVEL_NONE
 *<li>Commitment control has not been started
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>isOpen() returns true
 *<li>isReadOnly() returns false
 *<li>isReadWrite() returns true
 *<li>isWriteOnly() returns false
 *<li>getBlockingFactor() returns 1
 *<li>getCommitLockLevel() returns -1
 *<li>readPrevious() throws AS400Exception with CPF5149
 *<li>update() throws AS400Exception with CPF5149
 *<li>write() is successful
 *</ul>
 **/
public void Var006()
{
  setVariation(6);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  RecordFormat format = new DDMChar10KeyFormat(systemObject_);
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
    file.setRecordFormat(format);
    file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.isReadOnly())
    {
      failMsg.append("isReadOnly() returning true.\n");
    }
    if (file.isReadWrite())
    {
      failMsg.append("isReadWrite() returning true.\n");
    }
    if (!file.isWriteOnly())
    {
      failMsg.append("isWriteOnly() not returning true.\n");
    }
    if (file.getBlockingFactor() != 1)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() not returning -1: " + String.valueOf(file.getCommitLockLevel()) + ".\n");
    }
    try
    {
      // Ensure that we can write a record.
      Record record = file.getRecordFormat().getNewRecord();
      file.write(record);
    }
    catch(Exception e)
    {
    }
    try
    {
      // Attempt to read a record.
      file.readPrevious();
      failMsg.append("No exception trying to read from file.\n");
    }
    catch(Exception e)
    {
    }
    try
    {
      Record record = file.getRecordFormat().getNewRecord();
      file.update(record);
      failMsg.append("No exception trying to update file.\n");
    }
    catch(Exception e2)
    {
    }
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify invalid usage of SequentialFile.open().
 *<ul compact>
 *<li>Attempt to open() without setting the record format.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalStateException indicating property not set for record format.
 *<li>isOpen() returns false
 *</ul>
**/
public void Var007()
{
  setVariation(7);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file =
    new SequentialFile(systemObject_,
                       "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
  try
  {
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    file.close(); 
    failMsg.append("No exception for record format not specified.\n");
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "ExtendedIllegalStateException", "recordFormat: Property is not set."))
    {
      failMsg.append("Wrong exception/text for record format not specified.\n");
      e.printStackTrace(output_);
    }
    if (file.isOpen())
    {
      failMsg.append("File is open after failed open.\n");
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
 *Verify invalid usage of SequentialFile.open().
 *<ul compact>
 *<li>Attempt to open() specifying invalid openType.
 *<ul compact>
 *<li>Specify one less than the first valid constant.
 *<li>Specify one more than the last valid constant.
 *</ul>
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating parameter value not valid for
 *openType.
 *<li>isOpen() returns false
 *</ul>
**/
public void Var008()
{
  setVariation(8);
  StringBuffer failMsg = new StringBuffer();
  try
  {
    SequentialFile file =
      new SequentialFile(systemObject_,
                         "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    try
    {
      file.open(AS400File.READ_ONLY - 1, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close(); 
      failMsg.append("No exception for invalid openType: READ_ONLY - 1.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "openType: Parameter value is not valid."))
      {
        failMsg.append("Wrong exception/text for invalid openType: READ_ONLY - 1.\n");
        e.printStackTrace(output_);
      }
      if (file.isOpen())
      {
        failMsg.append("File is open after failed open.\n");
      }
    }
    try
    {
      file.open(AS400File.WRITE_ONLY + 1, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      failMsg.append("No exception for invalid openType: WRITE_ONLY + 1.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "openType: Parameter value is not valid."))
      {
        failMsg.append("Wrong exception/text for invalid openType: WRITE_ONLY + 1.\n");
        e.printStackTrace(output_);
      }
      if (file.isOpen())
      {
        failMsg.append("File is open after failed open.\n");
      }
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
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
 *Verify invalid usage of SequentialFile.open().
 *<ul compact>
 *<li>Attempt to open() specifying negative blockingFactor.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating parameter value not valid for
 *blockingFactor.
 *<li>isOpen() returns false
 *</ul>
**/
public void Var009()
{
  setVariation(9);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file =
    new SequentialFile(systemObject_,
                       "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
  try
  {
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, -1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    file.close(); 
    failMsg.append("No exception for negative blockingFactor.\n");
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "ExtendedIllegalArgumentException", "blockingFactor: Parameter value is not valid."))
    {
      failMsg.append("Wrong exception/text for negative blockingFactor.\n");
      e.printStackTrace(output_);
    }
    if (file.isOpen())
    {
      failMsg.append("File is open after failed open.\n");
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
 *Verify invalid usage of SequentialFile.open().
 *<ul compact>
 *<li>Attempt to open() specifying invalid commitLockLevel.
 *<ul compact>
 *<li>Specify one less than the first valid constant.
 *<li>Specify one more than the last valid constant.
 *</ul>
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating parameter value not valid for
 *commitLockLevel.
 *<li>isOpen() returns false
 *</ul>
**/
public void Var010()
{
  setVariation(10);
  StringBuffer failMsg = new StringBuffer();
  try
  {
    SequentialFile file =
      new SequentialFile(systemObject_,
                         "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    try
    {
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL - 1);
      file.close(); 
      failMsg.append("No exception for invalid commitLockLevel: COMMIT_LOCK_LEVEL_ALL - 1.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "commitLockLevel: Parameter value is not valid."))
      {
        failMsg.append("Wrong exception/text for invalid commitLockLevel: COMMIT_LOCK_LEVEL_ALL - 1.\n");
        e.printStackTrace(output_);
      }
      if (file.isOpen())
      {
        failMsg.append("File is open after failed open.\n");
      }
    }
    try
    {
      file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT + 1);
      failMsg.append("No exception for invalid commitLockLevel: COMMIT_LOCK_LEVEL_DEFAULT + 1.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "commitLockLevel: Parameter value is not valid."))
      {
        failMsg.append("Wrong exception/text for invalid commitLockLevel: COMMIT_LOCK_LEVEL_DEFAULT + 1.\n");
        e.printStackTrace(output_);
      }
      if (file.isOpen())
      {
        failMsg.append("File is open after failed open.\n");
      }
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
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
 *Verify invalid usage of SequentialFile.open().
 *<ul compact>
 *<li>Attempt to open() swhen file is already open.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalStateException indicating object can not be open.
 *<li>isOpen() returns true.
 *</ul>
**/
public void Var011()
{
  setVariation(11);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    failMsg.append("No exception trying to open already opened file.\n");
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "ExtendedIllegalStateException", "Object cannot be in an open state."))
    {
      failMsg.append("Wrong exception/text for open on already opened file.\n");
      e.printStackTrace(output_);
    }
    if (!file.isOpen())
    {
      failMsg.append("File is not open after failed open on already opened file.\n");
    }
  }

  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("File failed to close.\n");
    e.printStackTrace(output_);
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
 *Verify invalid usage of KeyedFile.open().
 *<ul compact>
 *<li>Attempt to open() without setting the record format.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalStateException indicating property not set for record format.
 *<li>isOpen() returns false
 *</ul>
**/
public void Var012()
{
  setVariation(12);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file =
    new KeyedFile(systemObject_,
                       "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
  try
  {
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    failMsg.append("No exception for record format not specified.\n");
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "ExtendedIllegalStateException", "recordFormat: Property is not set."))
    {
      failMsg.append("Wrong exception/text for record format not specified.\n");
      e.printStackTrace(output_);
    }
    if (file.isOpen())
    {
      failMsg.append("File is open after failed open.\n");
    }
  }
  try {
    file.close();
  } catch (Exception e) {
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
 *Verify invalid usage of KeyedFile.open().
 *<ul compact>
 *<li>Attempt to open() specifying invalid openType.
 *<ul compact>
 *<li>Specify one less than the first valid constant.
 *<li>Specify one more than the last valid constant.
 *</ul>
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating parameter value not valid for
 *openType.
 *<li>isOpen() returns false
 *</ul>
**/
public void Var013()
{
  setVariation(13);
  StringBuffer failMsg = new StringBuffer();
  try
  {
    KeyedFile file =
      new KeyedFile(systemObject_,
                         "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    try
    {
      file.open(AS400File.READ_ONLY - 1, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close(); 
      failMsg.append("No exception for invalid openType: READ_ONLY - 1.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "openType: Parameter value is not valid."))
      {
        failMsg.append("Wrong exception/text for invalid openType: READ_ONLY - 1.\n");
        e.printStackTrace(output_);
      }
      if (file.isOpen())
      {
        failMsg.append("File is open after failed open.\n");
      }
    }
    try
    {
      file.open(AS400File.WRITE_ONLY + 1, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      failMsg.append("No exception for invalid openType: WRITE_ONLY + 1.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "openType: Parameter value is not valid."))
      {
        failMsg.append("Wrong exception/text for invalid openType: WRITE_ONLY + 1.\n");
        e.printStackTrace(output_);
      }
      if (file.isOpen())
      {
        failMsg.append("File is open after failed open.\n");
      }
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
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
 *Verify invalid usage of KeyedFile.open().
 *<ul compact>
 *<li>Attempt to open() specifying negative blockingFactor.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating parameter value not valid for
 *blockingFactor.
 *<li>isOpen() returns false
 *</ul>
**/
public void Var014()
{
  setVariation(14);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file =
    new KeyedFile(systemObject_,
                       "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
  try
  {
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, -1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    file.close(); 
    failMsg.append("No exception for negative blockingFactor.\n");
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "ExtendedIllegalArgumentException", "blockingFactor: Parameter value is not valid."))
    {
      failMsg.append("Wrong exception/text for negative blockingFactor.\n");
      e.printStackTrace(output_);
    }
    if (file.isOpen())
    {
      failMsg.append("File is open after failed open.\n");
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
 *Verify invalid usage of KeyedFile.open().
 *<ul compact>
 *<li>Attempt to open() specifying invalid commitLockLevel.
 *<ul compact>
 *<li>Specify one less than the first valid constant.
 *<li>Specify one more than the last valid constant.
 *</ul>
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating parameter value not valid for
 *commitLockLevel.
 *<li>isOpen() returns false
 *</ul>
**/
public void Var015()
{
  setVariation(15);
  StringBuffer failMsg = new StringBuffer();
  try
  {
    KeyedFile file =
      new KeyedFile(systemObject_,
                         "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    try
    {
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL - 1);
      file.close(); 
      failMsg.append("No exception for invalid commitLockLevel: COMMIT_LOCK_LEVEL_ALL - 1.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "commitLockLevel: Parameter value is not valid."))
      {
        failMsg.append("Wrong exception/text for invalid commitLockLevel: COMMIT_LOCK_LEVEL_ALL - 1.\n");
        e.printStackTrace(output_);
      }
      if (file.isOpen())
      {
        failMsg.append("File is open after failed open.\n");
      }
    }
    try
    {
      file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT + 1);
      failMsg.append("No exception for invalid commitLockLevel: COMMIT_LOCK_LEVEL_DEFAULT + 1.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "commitLockLevel: Parameter value is not valid."))
      {
        failMsg.append("Wrong exception/text for invalid commitLockLevel: COMMIT_LOCK_LEVEL_DEFAULT + 1.\n");
        e.printStackTrace(output_);
      }
      if (file.isOpen())
      {
        failMsg.append("File is open after failed open.\n");
      }
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
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
 *Verify invalid usage of KeyedFile.open().
 *<ul compact>
 *<li>Attempt to open() swhen file is already open.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalStateException indicating object can not be open.
 *<li>isOpen() returns true.
 *</ul>
**/
public void Var016()
{
  setVariation(16);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    failMsg.append("No exception trying to open already opened file.\n");
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "ExtendedIllegalStateException", "Object cannot be in an open state."))
    {
      failMsg.append("Wrong exception/text for open on already opened file.\n");
      e.printStackTrace(output_);
    }
    if (!file.isOpen())
    {
      failMsg.append("File is not open after failed open on already opened file.\n");
    }
  }

  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("File failed to close.\n");
    e.printStackTrace(output_);
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
 *Verify valid usage of AS400File.close().
 *<ul compact>
 *<li>Open file, close it and re-open it.
 *<li>Open file, close it and close it again.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>isOpen() returns false after close().
 *<li>getBlockingFactor() returns 0 after close.
 *<li>isReadOnly(), isReadWrite() and isWriteOnly() return false.
 *<li>getCommitLockLevel should return -1 if commitment control has
 *not been started and should return COMMIT_LOCK_LEVEL_NONE if it
 *has been started.
 *<li>open() is successful after close().
 *<li>No exception occurs when closing an un-opened file.
 *</ul>
 **/
public void Var017()
{
  setVariation(17);
  StringBuffer failMsg = new StringBuffer();
  RecordFormat format = new DDMChar10KeyFormat(systemObject_);
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,                   "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
    file.setRecordFormat(format);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    file.close();
    // Verify object state
    if (file.isOpen())
    {
      failMsg.append("isOpen() returned true after close()\n");
    }
    if (file.isReadOnly())
    {
      failMsg.append("isReadOnly() returned true after close()\n");
    }
    if (file.isWriteOnly())
    {
      failMsg.append("isWriteOnly() returned true after close()\n");
    }
    if (file.isReadWrite())
    {
      failMsg.append("isReadWrite() returned true after close()\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() not returning -1 after close()\n");
    }
    // Verify ability to re-open file
    try
    {
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
    }
    catch(Exception e)
    {
      failMsg.append("Exception attempting to re-open file after close().\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_NONE)
      {
        failMsg.append("getCommitLockLevel() not returning COMMIT_LOCK_LEVEL_NONE after close()\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("File failed to close.\n");
      e.printStackTrace(output_);
    }
    // Attempt multiple close()
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failMsg.append("Exception when closing an already closed file.\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
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
 *Verify the SequentialFile.open() specifying a blocking factor of
 *greater than 1.
 *<ul compact>
 *<li>Open for READ_ONLY.
 *</ul>
 *<br>Expected results:
 *<ul compact>
 *<li>The file will be open.
 *<li>getBlockingFactor returns the value specified on the open().
 *<li>readFirst() returns the first record.
 *<li>readNext() returns the next record.
 *<li>read(3) returns record number 3.
 *<li>readLast() returns the last record.
 *</ul>
**/
public void Var018()
{
  setVariation(18);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.READ_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getBlockingFactor() != 9)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
    try
    {
        if (!file.readFirst().toString().equals(records_[0].toString()))
        {
            failMsg.append("readFirst() returned incorrect record: ");
            failMsg.append(file.readFirst().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readFirst() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        if (!file.readNext().toString().equals(records_[1].toString()))
        {
            failMsg.append("readNext() returned incorrect record: ");
            failMsg.append(file.readNext().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readNext() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        if (!file.read(3).toString().equals(records_[2].toString()))
        {
            failMsg.append("read() returned incorrect record: ");
            failMsg.append(file.read(3).toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("read() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        if (!file.readLast().toString().equals(records_[8].toString()))
        {
            failMsg.append("readLast() returned incorrect record: ");
            failMsg.append(file.readLast().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readLast() failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify the SequentialFile.open() specifying a blocking factor of
 *greater than 1.
 *<ul compact>
 *<li>Open for WRITE_ONLY.
 *</ul>
 *<br>Expected results:
 *<ul compact>
 *<li>The file will be open.
 *<li>getBlockingFactor returns the value specified on the open().
 *<li>write(Record) writes one record.
 *<li>write(Record[]) writes the array of records.
 *</ul>
**/
public void Var019()
{
  setVariation(19);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F1M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getBlockingFactor() != 9)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
    try
    {
      Record record = file.getRecordFormat().getNewRecord();
      file.write(record);
    }
    catch(Exception e)
    {
      failMsg.append("write() one record failed\n");
      e.printStackTrace(output_);
    }
    try
    {
      Record[] rec = new Record[3];
      for (short i = 0; i < 3; ++i)
      {
        rec[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(rec);
    }
    catch(Exception e)
    {
      failMsg.append("write() records[] failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify the SequentialFile.open() specifying a blocking factor of
 *greater than 1.
 *<ul compact>
 *<li>Open for READ_WRITE.
 *</ul>
 *<br>Expected results:
 *<ul compact>
 *<li>The file will be open.
 *<li>getBlockingFactor returns 1.
 *<li>readFirst() returns the first record.
 *<li>readNext() returns the next record.
 *<li>read(3) returns record number 3.
 *<li>readLast() returns the last record.
 *<li>write(Record) writes one record.
 *<li>write(Record[]) writes the array of records.
 *</ul>
**/
public void Var020()
{
  setVariation(20);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getBlockingFactor() != 1)
    {
      failMsg.append("getBlockingFactor() failed: ");
      failMsg.append(file.getBlockingFactor());
      failMsg.append("\n");
    }
    try
    {
        if (!file.readFirst().toString().equals(records_[0].toString()))
        {
            failMsg.append("readFirst() returned incorrect record: ");
            failMsg.append(file.readFirst().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readFirst() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        if (!file.readNext().toString().equals(records_[1].toString()))
        {
            failMsg.append("readNext() returned incorrect record: ");
            failMsg.append(file.readNext().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readNext() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        if (!file.read(3).toString().equals(records_[2].toString()))
        {
            failMsg.append("read() returned incorrect record: ");
            failMsg.append(file.read(3).toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("read() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        if (!file.readLast().toString().equals(records_[8].toString()))
        {
            failMsg.append("readLast() returned incorrect record: ");
            failMsg.append(file.readLast().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readLast() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        Record rec;
        RecordFormat rf = file.getRecordFormat();
        rec = rf.getNewRecord();
        rec.setField(0, "RECORD 001");
        file.write(rec);
    }
    catch(Exception e)
    {
      failMsg.append("write() one record failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        file.write(records_);
    }
    catch(Exception e)
    {
      failMsg.append("write() records[] failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify the KeyedFile.open() specifying a blocking factor of
 *greater than 1.
 *<ul compact>
 *<li>Open for READ_ONLY.
 *</ul>
 *<br>Expected results:
 *<ul compact>
 *<li>The file will be open.
 *<li>getBlockingFactor returns 1.
 *<li>readFirst() returns the first record.
 *<li>readNext() returns the next record.
 *<li>read(key) returns record mathing key.
 *<li>readLast() returns the last record.
 *</ul>
**/
public void Var021()
{
  setVariation(21);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getBlockingFactor() != 9)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
    try
    {
        if (!file.readFirst().toString().equals(records_[0].toString()))
        {
            failMsg.append("readFirst() returned incorrect record: ");
            failMsg.append(file.readFirst().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readFirst() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        if (!file.readNext().toString().equals(records_[1].toString()))
        {
            failMsg.append("readNext() returned incorrect record: ");
            failMsg.append(file.readNext().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readNext() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        Object[] k = new Object[1];
        k[0] = "RECORD 3  ";
        if (!file.read(k).toString().equals(records_[2].toString()))
        {
            failMsg.append("read(key) returned incorrect record: ");
            failMsg.append(file.read(k).toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("read() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        if (!file.readLast().toString().equals(records_[8].toString()))
        {
            failMsg.append("readLast() returned incorrect record: ");
            failMsg.append(file.readLast().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readLast() failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify the KeyedFile.open() specifying a blocking factor of
 *greater than 1.
 *<ul compact>
 *<li>Open for WRITE_ONLY.
 *</ul>
 *<br>Expected results:
 *<ul compact>
 *<li>The file will be open.
 *<li>getBlockingFactor returns 1.
 *<li>write(Record) writes one record.
 *<li>write(Record[]) writes the array of records.
 *</ul>
**/
public void Var022()
{
  setVariation(22);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getBlockingFactor() != 9)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
    try
    {
      Record record = file.getRecordFormat().getNewRecord();
      file.write(record);
    }
    catch(Exception e)
    {
      failMsg.append("write() one record failed\n");
      e.printStackTrace(output_);
    }
    try
    {
      Record[] rec = new Record[3];
      for (short i = 0; i < 3; ++i)
      {
        rec[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(rec);
    }
    catch(Exception e)
    {
      failMsg.append("write() records[] failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify the KeyedFile.open() specifying a blocking factor of
 *greater than 1.
 *<ul compact>
 *<li>Open for READ_WRITE.
 *</ul>
 *<br>Expected results:
 *<ul compact>
 *<li>The file will be open.
 *<li>getBlockingFactor returns 1.
 *<li>readFirst() returns the first record.
 *<li>readNext() returns the next record.
 *<li>read(key) returns record mathing key.
 *<li>readLast() returns the last record.
 *<li>write(Record) writes one record.
 *<li>write(Record[]) writes the array of records.
 *</ul>
**/
public void Var023()
{
  setVariation(23);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getBlockingFactor() != 1)
    {
      failMsg.append("getBlockingFactor() failed: ");
      failMsg.append(file.getBlockingFactor());
      failMsg.append("\n");
    }
    try
    {
        if (!file.readFirst().toString().equals(records_[0].toString()))
        {
            failMsg.append("readFirst() returned incorrect record: ");
            failMsg.append(file.readFirst().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readFirst() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        if (!file.readNext().toString().equals(records_[1].toString()))
        {
            failMsg.append("readNext() returned incorrect record: ");
            failMsg.append(file.readNext().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readNext() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        Object[] k = new Object[1];
        k[0] = "RECORD 3  ";
        if (!file.read(k).toString().equals(records_[2].toString()))
        {
            failMsg.append("read(key) returned incorrect record: ");
            failMsg.append(file.read(k).toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("read() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        if (!file.readLast().toString().equals(records_[8].toString()))
        {
            failMsg.append("readLast() returned incorrect record: ");
            failMsg.append(file.readLast().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("readLast() failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        Record rec = file.getRecordFormat().getNewRecord();
        rec.setField(0, "RECORD 001");
        file.write(rec);
    }
    catch(Exception e)
    {
      failMsg.append("write() one record failed\n");
      e.printStackTrace(output_);
    }
    try
    {
        file.write(records_);
    }
    catch(Exception e)
    {
      failMsg.append("write() records[] failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify SequentialFile specifying a blocking factor of 0.
 *<ul compact>
 *<li>Open file for READ_ONLY, blocking factor = 0
 *<li>Open file for READ_WRITE, blocking factor = 0
 *<li>Open file for WRITEE_ONLY, blocking factor = 0
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File will be open.
 *<li>getBlockingFactor returns the integer result of
 *2048/(record length of file + 16) for open of READ_ONLY.
 *<li>getBlockingFactor returns 1 for open of READ_WRITE.
 *<li>getBlockingFactor returns the integer result of
 *2048/(record length of file + 16) for open of WRITE_ONLY.
 *</ul>
**/
public void Var024()
{
  setVariation(24);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    int expectedBlockingFactor = 2048/(file.getRecordFormat().getNewRecord().getRecordLength() + 16);
    // Try with READ_ONLY
    file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getBlockingFactor() != expectedBlockingFactor)
    {
      failMsg.append("getBlockingFactor() failed (READ_ONLY): ");
      failMsg.append(file.getBlockingFactor());
      failMsg.append("\n");
    }
    file.close();

    // Try with WRITE_ONLY
    file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getBlockingFactor() != expectedBlockingFactor)
    {
      failMsg.append("getBlockingFactor() failed (WRITE_ONLY): ");
      failMsg.append(file.getBlockingFactor());
      failMsg.append("\n");
    }
    file.close();

    // Try with READ_WRITE
    file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
    expectedBlockingFactor = 1;
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getBlockingFactor() != expectedBlockingFactor)
    {
      failMsg.append("getBlockingFactor() failed (READ_WRITE): ");
      failMsg.append(file.getBlockingFactor());
      failMsg.append("\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify KeyedFile specifying a blocking factor of 0.
 *<br>
 *Expected results:
 *<ul compact>
 *<li>File will be open.
 *<li>getBlockingFactor returns 1
 *</ul>
**/
public void Var025()
{
  setVariation(25);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    int block = 2048/(file.getRecordFormat().getNewRecord().getRecordLength() + 16);
    if (file.getBlockingFactor() != block)
    {
      failMsg.append("getBlockingFactor() failed\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify SequentialFile.open() specifying COMMIT_LOCK_LEVEL_NONE.
 *<ul compact>
 *<li>Commitment control has been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns COMMIT_LOCK_LEVEL_NONE.
 *</ul>
**/
public void Var026()
{
  setVariation(26);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_NONE)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
    try
    {
      file.endCommitmentControl();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("End commitment control failed.\n");
    }
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("End commitment control failed.\n");
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
 *Verify SequentialFile.open() specifying COMMIT_LOCK_LEVEL_DEFAULT.
 *<ul compact>
 *<li>Commitment control has been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns COMMIT_LOCK_LEVEL_DEFAULT.
 *</ul>
**/
public void Var027()
{
  setVariation(27);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_DEFAULT)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("End commitment control failed.\n");
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
 *Verify SequentialFile.open() specifying COMMIT_LOCK_LEVEL_CURSOR_STABILITY.
 *<ul compact>
 *<li>Commitment control has been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns COMMIT_LOCK_LEVEL_CURSOR_STABILITY.
 *</ul>
**/
public void Var028()
{
  setVariation(28);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("End commitment control failed.\n");
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
 *Verify SequentialFile.open() specifying COMMIT_LOCK_LEVEL_ALL.
 *<ul compact>
 *<li>Commitment control has been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns COMMIT_LOCK_LEVEL_ALL.
 *</ul>
**/
public void Var029()
{
  setVariation(29);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_ALL)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("End commitment control failed.\n");
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
 *Verify SequentialFile.open() specifying COMMIT_LOCK_LEVEL_CHANGE.
 *<ul compact>
 *<li>Commitment control has been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns COMMIT_LOCK_LEVEL_CHANGE.
 *</ul>
**/
public void Var030()
{
  setVariation(30);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_CHANGE)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("End commitment control failed.\n");
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
 *Verify SequentialFile.open() specifying COMMIT_LOCK_LEVEL_DEFAULT.
 *<ul compact>
 *<li>Commitment control has not been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns -1.
 *</ul>
**/
public void Var031()
{
  setVariation(31);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify SequentialFile.open() specifying COMMIT_LOCK_LEVEL_CURSOR_STABILITY.
 *<ul compact>
 *<li>Commitment control has not been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns -1.
 *</ul>
**/
public void Var032()
{
  setVariation(32);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify SequentialFile.open() specifying COMMIT_LOCK_LEVEL_ALL.
 *<ul compact>
 *<li>Commitment control has not been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns -1.
 *</ul>
**/
public void Var033()
{
  setVariation(33);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify SequentialFile.open() specifying COMMIT_LOCK_LEVEL_CHANGE.
 *<ul compact>
 *<li>Commitment control has not been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns -1.
 *</ul>
**/
public void Var034()
{
  setVariation(34);
  StringBuffer failMsg = new StringBuffer();
  SequentialFile file = null;
  try
  {
    file = new SequentialFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/F2M1RW.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify KeyedFile.open() specifying COMMIT_LOCK_LEVEL_NONE.
 *<ul compact>
 *<li>Commitment control has been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns COMMIT_LOCK_LEVEL_NONE.
 *</ul>
**/
public void Var035()
{
  setVariation(35);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_NONE)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("End commitment control failed.\n");
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
 *Verify KeyedFile.open() specifying COMMIT_LOCK_LEVEL_DEFAULT.
 *<ul compact>
 *<li>Commitment control has been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns COMMIT_LOCK_LEVEL_DEFAULT.
 *</ul>
**/
public void Var036()
{
  setVariation(36);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_DEFAULT)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("End commitment control failed.\n");
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
 *Verify KeyedFile.open() specifying COMMIT_LOCK_LEVEL_CURSOR_STABILITY.
 *<ul compact>
 *<li>Commitment control has been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns COMMIT_LOCK_LEVEL_CURSOR_STABILITY.
 *</ul>
**/
public void Var037()
{
  setVariation(37);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("End commitment control failed.\n");
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
 *Verify KeyedFile.open() specifying COMMIT_LOCK_LEVEL_ALL.
 *<ul compact>
 *<li>Commitment control has been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns COMMIT_LOCK_LEVEL_ALL.
 *</ul>
**/
public void Var038()
{
  setVariation(38);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_ALL)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("End commitment control failed.\n");
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
 *Verify KeyedFile.open() specifying COMMIT_LOCK_LEVEL_CHANGE.
 *<ul compact>
 *<li>Commitment control has been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns COMMIT_LOCK_LEVEL_CHANGE.
 *</ul>
**/
public void Var039()
{
  setVariation(39);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_CHANGE)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
  }
  try
  {
    file.endCommitmentControl();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    failMsg.append("End commitment control failed.\n");
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
 *Verify KeyedFile.open() specifying COMMIT_LOCK_LEVEL_DEFAULT.
 *<ul compact>
 *<li>Commitment control has not been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns -1.
 *</ul>
**/
public void Var040()
{
  setVariation(40);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.endCommitmentControl();
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify KeyedFile.open() specifying COMMIT_LOCK_LEVEL_CURSOR_STABILITY.
 *<ul compact>
 *<li>Commitment control has not been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns -1.
 *</ul>
**/
public void Var041()
{
  setVariation(41);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.endCommitmentControl();
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify KeyedFile.open() specifying COMMIT_LOCK_LEVEL_ALL.
 *<ul compact>
 *<li>Commitment control has not been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns -1.
 *</ul>
**/
public void Var042()
{
  setVariation(42);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.endCommitmentControl();
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify KeyedFile.open() specifying COMMIT_LOCK_LEVEL_CHANGE.
 *<ul compact>
 *<li>Commitment control has not been started.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>File is open.
 *<li>getCommitLockLevel returns -1.
 *</ul>
**/
public void Var043()
{
  setVariation(43);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC2.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (file.getCommitLockLevel() != -1)
    {
      failMsg.append("getCommitLockLevel() failed.\n");
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.endCommitmentControl();
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify open() when %LIBL% was specified for the library.
 *<br>
 *Expected results:
 *<ul compact>
 *<li>The file will open and be usable.
 *</ul>
**/
public void Var044()
{
  setVariation(44);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    // QCZLIBFILE is a system file which should exist on most systems
    // that a generic user has authority to use.
    file = new KeyedFile(systemObject_,
                         "/QSYS.LIB/%LIBL%.LIB/QCZLIBFILE.FILE");
    file.setRecordFormat(new AS400FileRecordDescription(systemObject_, file.getPath()).retrieveRecordFormat()[0]);
    file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    try
    {
      file.readFirst();
    }
    catch(Exception e)
    {
      failMsg.append("reading from file failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify open() when %CURLIB% was specified for the library.
 *<br>
 *Expected results:
 *<ul compact>
 *<li>The file will open and be usable.
 *</ul>
**/
public void Var045()
{
  setVariation(45);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                              "/QSYS.LIB/%CURLIB%.LIB/KEYSRC.FILE/MBR1.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    try
    {
        if (!file.readFirst().toString().equals(records_[0].toString()))
        {
            failMsg.append("readFirst() returned incorrect record: ");
            failMsg.append(file.readFirst().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("reading from file failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify open() when %FILE% was specified for the member.
 *<br>
 *Expected results:
 *<ul compact>
 *<li>The file will open and be usable.
 *<li>getMemberName() will return the name of the file.
 *</ul>
**/
public void Var046()
{
  setVariation(46);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                         "/QSYS.LIB/" + testLib_ + ".LIB/KEYSRC3.FILE/%FILE%.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (!file.getMemberName().toString().equals("KEYSRC3"))
    {
      failMsg.append("getMemberName() failed: " + file.getMemberName() + "\n");
    }
    try
    {
        if (!file.readFirst().toString().equals(records_[0].toString()))
        {
            failMsg.append("readFirst() returned incorrect record: ");
            failMsg.append(file.readFirst().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("reading from file failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
    e.printStackTrace(output_);
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify open() when %FIRST% was specified for the member.
 *<br>
 *Expected results:
 *<ul compact>
 *<li>The file will open and be usable.
 *<li>getMemberName() will return the name of the first member in the file.
 *</ul>
**/
public void Var047()
{
  setVariation(47);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                         "/QSYS.LIB/QGPL.LIB/KEYSRC.FILE/%FIRST%.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (!file.getMemberName().toString().equals("MBR1"))
    {
      failMsg.append("getMemberName() failed: " + file.getMemberName() + "\n");
    }
    try
    {
        if (!file.readFirst().toString().equals(records_[0].toString()))
        {
            failMsg.append("readFirst() returned incorrect record: ");
            failMsg.append(file.readFirst().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("reading from file failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
 *Verify open() when %LAST% was specified for the member.
 *<br>
 *Expected results:
 *<ul compact>
 *<li>The file will open and be usable.
 *<li>getMemberName() will return the name of the last member in the file.
 *</ul>
**/
public void Var048()
{
  setVariation(48);
  StringBuffer failMsg = new StringBuffer();
  KeyedFile file = null;
  try
  {
    file = new KeyedFile(systemObject_,
                         "/QSYS.LIB/QGPL.LIB/KEYSRC.FILE/%LAST%.MBR");
    file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
    file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

    // Verify the object state
    if (!file.isOpen())
    {
      failMsg.append("isOpen() not returning true.\n");
    }
    if (!file.getMemberName().toString().equals("MBR1"))
    {
      failMsg.append("getMemberName() failed: " + file.getMemberName() + "\n");
    }
    try
    {
        if (!file.readFirst().toString().equals(records_[0].toString()))
        {
            failMsg.append("readFirst() returned incorrect record: ");
            failMsg.append(file.readFirst().toString());
            failMsg.append("\n");
        }
    }
    catch(Exception e)
    {
      failMsg.append("reading from file failed\n");
      e.printStackTrace(output_);
    }
  }
  catch(Exception e)
  {
    failMsg.append("Unexpected exception.\n");
  }
  // Close the file
  try
  {
    file.close();
  }
  catch(Exception e)
  {
    failMsg.append("Failed to close file.\n");
    e.printStackTrace(output_);
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
}

