///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMThreadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.Thread;


import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

import test.DDM.DDMChar10KeyFormat;
import test.DDM.DDMChar10NoKeyFormat;
import test.misc.ThreadedTestcase;


public class DDMThreadTestcase extends ThreadedTestcase
{
  CommandCall cmd_;
  private String lib = "DDMTT";


 /**
  * Creates a new DDMThreadTestcase.
  * This is called from ThreadTest::createTestcases().
  */
  public DDMThreadTestcase (AS400 systemObject,
                             Vector<String> variationsToRun,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
  {
    super (systemObject, "DDMThreadTestcase", 9, variationsToRun,
           runMode, fileOutputStream, password);
    cmd_ = new CommandCall(systemObject_);
  }


 /**
  * Runs the variations requested.
  */
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
    setup();
  }
  catch (Exception e)
  {
    // Testcase setup did not complete successfully
    System.out.println("Unable to complete setup; variations not run");
    // Run cleanup in case some of the setup work got done
    try
    {
      cleanup();
    }
    catch (Exception e2){}
    return;
  }


    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }
    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      Var002();
    }
    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }
    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }
    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }
    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }
    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
    {
      setVariation(7);
      Var007();
    }
    if ((allVariations || variationsToRun_.contains("8")) &&
        runMode_ != ATTENDED)
    {
      setVariation(8);
      Var008();
    }
    if ((allVariations || variationsToRun_.contains("9")) &&
        runMode_ != ATTENDED)
    {
      setVariation(9);
      Var009();
    }

  // Do any necessary cleanup work for the variations
  try
  {
    cleanup();
  }
  catch (Exception e)
  {
    System.out.println("Unable to complete cleanup.");
  }

  // Disconnect from the AS/400
  systemObject_.disconnectService(AS400.RECORDACCESS);

  }

  boolean startJournal(String fileToJournal)
  {
    // Start journaling
    String cmdStr = "QSYS/STRJRNPF FILE("+lib+"/"+fileToJournal+") ";
    cmdStr += "JRN(QGPL/DDMTTJRN)";
    String msg = runCommand(cmdStr);
    if (msg != null)
    {
      output_.println("Failure executing '"+cmdStr+"'");
      output_.println(msg);
      return false;
    }
    return true;
  }

  boolean endJournal(String fileToJournal)
  {
    // Stop journaling
    String cmdStr = "QSYS/ENDJRNPF FILE("+lib+"/"+fileToJournal+") ";
    cmdStr += "JRN(QGPL/DDMTTJRN)";
    String msg = runCommand(cmdStr);
    if (msg != null)
    {
      output_.println("Failure executing '"+cmdStr+"'");
      output_.println(msg);
      return false;
    }
    return true;
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


  protected void setup()
    throws Exception
  {
    try
    {
      // Create library DDMTEST
      String msg = runCommand("QSYS/CRTLIB LIB(" + lib + ") AUT(*ALL)");
      if (msg != null && !msg.equals("CPF2111"))
      {
        output_.println("Failure executing 'CRTLIB LIB(" + lib + ") AUT(*ALL)'");
        output_.println(msg);
        throw new Exception("");
      }

    // Create journal receiver and journal if it does not already exist
    msg = runCommand("QSYS/CRTJRNRCV JRNRCV(QGPL/DDMTTRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM Thread test cases')");
    if (msg != null)
    {
      output_.println("Failure executing 'CRTJRNRCV JRNRCV(QGPL/DDMTTRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM Thread test cases')'");
      output_.println(msg);
      throw new Exception("");
    }
    msg = runCommand("QSYS/CRTJRN JRN(QGPL/DDMTTJRN) JRNRCV(QGPL/DDMTTRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM Thread test case journal')");
    if (msg != null)
    {
      output_.println("Failure executing 'CRTJRN JRN(QGPL/DDMTTJRN) JRNRCV(QGPL/DDMTTRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM Thread test case journal')'");
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


  protected void cleanup()
    throws Exception
  {
    boolean success = true;

    try
    {
    // Delete the DDMTest library and the journal file.  The journal
    // receive should be automatically deleted due to the way that the
    // journal and receiver were deleted.
	
    String msg = deleteLibrary(cmd_, lib);
    if (msg != null)
    {
      output_.println("Failure executing 'delete library " + lib + "'");
      output_.println(msg);
      success = false;
    }
    msg = runCommand("QSYS/DLTJRN QGPL/DDMTTJRN");
    if (msg != null)
    {
      output_.println("Failure executing 'DLTJRN QGPL/DDMTTJRN'");
      output_.println(msg);
      success = false;
    }
    }
    catch(Exception e)
    {
      System.out.println("Cleanup unsuccessful.  Some files may have been left in DDMTEST and QGPL");
      e.printStackTrace(output_);
      throw e;
    }

    if (!success)
    {
      throw new Exception("Cleanup was unsuccessful");
    }
  }


 /**
  * 2 threads attemting to open the same Keyed File both using
  * different commit levels, blockingFactor, and openTypes.  Verify that
  * state of each threads local KeyedFile is consistent with whether it
  * was able to open the file.
  */
  public void Var001()
  {
    try
    {
      pipeInput_    = new PipedInputStream();
      String     p1 = "/QSYS.LIB/" + lib + ".LIB" +"/OPEN1.FILE/THEMBR.MBR";
output_.println("here1");
      KeyedFile  f1 = new KeyedFile(systemObject_, p1);
output_.println("here2");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), one key");
output_.println("here3");
      f1.close();

      if (!startJournal("OPEN1"))
      {
        output_.println("Could not start journaling.");
      }

      DDMThread  t1 = new DDMThread(pipeInput_, output_, this, DDMThread.OPEN_KEYED,
                                    systemObject_, p1, AS400File.READ_ONLY, 5,
                                      AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      DDMThread  t2 = new DDMThread(pipeInput_, output_, this, DDMThread.OPEN_KEYED,
                                    systemObject_, p1, AS400File.WRITE_ONLY, 3,
                                      AS400File.COMMIT_LOCK_LEVEL_NONE);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();

      if (!endJournal("OPEN1"))
      {
        output_.println("Could not end journaling.");
      }

output_.println("here5");
      f1.delete();
output_.println("here6");

    }
    catch(Exception e)
    {
      output_.println("Unexpected exception:");
      e.printStackTrace(output_);
    }
  }


 /**
  * 2 threads attemting to open the same Sequential File both using different
  * different commit levels, blockingFactor, and openTypes.  Verify that
  * state of each threads local SequentialFile is consistant with wether it
  * was able to open the file.
  */
  public void Var002()
  {
    try
    {
      pipeInput_    = new PipedInputStream();
      String     p1 = "/QSYS.LIB/" + lib + ".LIB" +"/OPEN2.FILE/THEMBR.MBR";
output_.println("here1");
      SequentialFile  f1 = new SequentialFile(systemObject_, p1);
output_.println("here2");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), no key");
output_.println("here3");
      f1.close();

      if (!startJournal("OPEN2"))
      {
        output_.println("Could not start journaling.");
      }

output_.println("here4");
      DDMThread  t1 = new DDMThread(pipeInput_, output_, this, DDMThread.OPEN_SEQ,
                                    systemObject_, p1, AS400File.READ_WRITE, 3,
                                      AS400File.COMMIT_LOCK_LEVEL_NONE);
      DDMThread  t2 = new DDMThread(pipeInput_, output_, this, DDMThread.OPEN_SEQ,
                                    systemObject_, p1, AS400File.WRITE_ONLY, 5,
                                      AS400File.COMMIT_LOCK_LEVEL_NONE);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();

      if (!endJournal("OPEN2"))
      {
        output_.println("Could not end journaling.");
      }

output_.println("here5");
      f1.delete();
output_.println("here6");

    }
    catch(Exception e)
    {
      output_.println("Unexpected exception:");
      e.printStackTrace(output_);
    }
  }


 /**
  * One thread trying to open file using SequentialFile another
  * using KeyedFile. Verify that state of each threads local
  * SequentialFile is consistant with wether it was able
  * to open the file.
  */
  public void Var003()
  {
    try
    {
      pipeInput_    = new PipedInputStream();
      String     p1 = "/QSYS.LIB/" + lib + ".LIB" +"/OPEN3.FILE/THEMBR.MBR";
output_.println("here1");
      KeyedFile  f1 = new KeyedFile(systemObject_, p1);
output_.println("here2");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), one key");
output_.println("here3");
      f1.close();

      if (!startJournal("OPEN3"))
      {
        output_.println("Could not start journaling.");
      }

output_.println("here4");
      DDMThread  t1 = new DDMThread(pipeInput_, output_, this, DDMThread.OPEN_KEYED,
                                    systemObject_, p1, AS400File.READ_ONLY, 3,
                                      AS400File.COMMIT_LOCK_LEVEL_NONE);
      DDMThread  t2 = new DDMThread(pipeInput_, output_, this, DDMThread.OPEN_SEQ,
                                    systemObject_, p1, AS400File.WRITE_ONLY, 5,
                                      AS400File.COMMIT_LOCK_LEVEL_NONE);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();

      if (!endJournal("OPEN3"))
      {
        output_.println("Could not end journaling.");
      }

output_.println("here5");
      f1.delete();
output_.println("here6");

    }
    catch(Exception e)
    {
      output_.println("Unexpected exception:");
      e.printStackTrace(output_);
    }
  }


 /**
  * Producer thread writes a record to the file if it's empty.
  * Consumer thread reads/verifies and then deletes the first record.
  * Both threads share a SequentialFile object to access the file
  */
  public void Var004()
  {
    try
    {
      pipeInput_    = new PipedInputStream();
      String     p1 = "/QSYS.LIB/" + lib + ".LIB" +"/CP.FILE/MBR.MBR";
      SequentialFile  f1 = new SequentialFile(systemObject_, p1);
      f1.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");

      if (!startJournal("CP"))
      {
        output_.println("Could not start journaling.");
      }

      f1.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      f1.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      f1.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);

      DDMThread  t1 = new DDMThread(pipeInput_, output_, this,
                            DDMThread.PRODUCER_SEQ, f1);
      DDMThread  t2 = new DDMThread(pipeInput_, output_, this,
                            DDMThread.CONSUMER_SEQ, f1);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.setNumLoops(25);
      t2.setNumLoops(25);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();

      // clean-up file
      f1.commit();
      f1.close();
      f1.endCommitmentControl();
      if (!endJournal("CP"))
      {
        output_.println("Could not end journaling.");
      }
      f1.delete();

    }
    catch(Exception e)
    {
      output_.println("Unexpected exception:");
      e.printStackTrace(output_);
    }
  }


 /**
  * Producer thread writes a keyed record to the file if it's empty.
  * Consumer thread reads/verifies and then deletes the keyed record.
  * Both threads share a KeyedFile object to access the file
  */
  public void Var005()
  {
    try
    {
      pipeInput_    = new PipedInputStream();
      String     p1 = "/QSYS.LIB/" + lib + ".LIB" +"/CP.FILE/MBR.MBR";
      KeyedFile  f1 = new KeyedFile(systemObject_, p1);
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), one key");

      if (!startJournal("CP"))
      {
        output_.println("Could not start journaling.");
      }

      f1.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      f1.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      f1.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);

      DDMThread  t1 = new DDMThread(pipeInput_, output_, this,
                            DDMThread.PRODUCER_KEYED, f1);
      DDMThread  t2 = new DDMThread(pipeInput_, output_, this,
                            DDMThread.CONSUMER_KEYED, f1);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.setNumLoops(25);
      t2.setNumLoops(25);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();

      // clean-up file
      f1.commit();
      f1.close();
      f1.endCommitmentControl();
      if (!endJournal("CP"))
      {
        output_.println("Could not end journaling.");
      }
      f1.delete();

    }
    catch(Exception e)
    {
      output_.println("Unexpected exception:");
      e.printStackTrace(output_);
    }
  }


 /**
  * 2 Threads both attempting to open the same KeyedFile using
  * different arguments to AS400File.open(...).  The Thread
  * that is able to open the file verifies that the state of
  * the KeyedFile is correct.
  */
  public void Var006()
  {
    try
    {
      int   commit   = AS400File.COMMIT_LOCK_LEVEL_NONE;
      int   block1 = 2;
      int   openType1 = AS400File.READ_ONLY;
      int   block2 = 3;
      int   openType2 = AS400File.WRITE_ONLY;
      RecordFormat rf = new DDMChar10KeyFormat(systemObject_);
      pipeInput_      = new PipedInputStream();
      String       p1 = "/QSYS.LIB/" + lib + ".LIB" +"/OPEN.FILE/MBR.MBR";
      KeyedFile    f1 = new KeyedFile(systemObject_, p1);
      f1.create(rf, "One field, CHAR(10), one key");

      if (!startJournal("OPEN"))
      {
        output_.println("Could not start journaling.");
      }

      f1.setRecordFormat(rf);
      //f1.startCommitmentControl(commit);

      DDMThread  t1 = new DDMThread(pipeInput_, output_, this,
                      DDMThread.OPEN_KEYED_SHARED, openType1, block1, commit, f1);
      DDMThread  t2 = new DDMThread(pipeInput_, output_, this,
                      DDMThread.OPEN_KEYED_SHARED, openType2, block2, commit, f1);
      objectInput_  = new ObjectInputStream(pipeInput_);
output_.println(t1.getName()+" blocking factor="+block1);
output_.println(t2.getName()+" blocking factor="+block2);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();

      // clean-up file
output_.println("b4 commit");
      f1.commit();
output_.println("b4 close");
      f1.close();
output_.println("b4 end commit");
      f1.endCommitmentControl();
output_.println("b4 delete");
      if (!endJournal("OPEN"))
      {
        output_.println("Could not end journaling.");
      }
      f1.delete();
output_.println("b4 delete");

    }
    catch(Exception e)
    {
      output_.println("Unexpected exception:");
      e.printStackTrace(output_);
    }
  }


 /**
  * 2 Threads both attempting to open the same SequentialFile using
  * different arguments to AS400File.open(...).  The Thread
  * that is able to open the file verifies that the state of
  * the SequentialFile is correct.
  */
  public void Var007()
  {
    try
    {
      int   commit    = AS400File.COMMIT_LOCK_LEVEL_NONE;
      int   block1    = 2;
      int   openType1 = AS400File.READ_ONLY;
      int   block2    = 3;
      int   openType2 = AS400File.WRITE_ONLY;
      RecordFormat rf = new DDMChar10KeyFormat(systemObject_);
      pipeInput_      = new PipedInputStream();
      String       p1 = "/QSYS.LIB/" + lib + ".LIB" +"/OPEN.FILE/MBR.MBR";
      SequentialFile f1 = new SequentialFile(systemObject_, p1);
      f1.create(rf, "One field, CHAR(10), one key");

      if (!startJournal("OPEN"))
      {
        output_.println("Could not start journaling.");
      }

      f1.setRecordFormat(rf);
      //f1.startCommitmentControl(commit);

      DDMThread  t1 = new DDMThread(pipeInput_, output_, this,
                      DDMThread.OPEN_SEQ_SHARED, openType1, block1, commit, f1);
      DDMThread  t2 = new DDMThread(pipeInput_, output_, this,
                      DDMThread.OPEN_SEQ_SHARED, openType2, block2, commit, f1);
      objectInput_  = new ObjectInputStream(pipeInput_);
output_.println(t1.getName()+" blocking factor="+block1);
output_.println(t2.getName()+" blocking factor="+block2);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();

      // clean-up file
output_.println("b4 commit");
      f1.commit();
output_.println("b4 close");
      f1.close();
output_.println("b4 end commit");
      f1.endCommitmentControl();
output_.println("b4 delete");
      if (!endJournal("OPEN"))
      {
        output_.println("Could not end journaling.");
      }
      f1.delete();
output_.println("b4 delete");

    }
    catch(Exception e)
    {
      output_.println("Unexpected exception:");
      e.printStackTrace(output_);
    }
  }


 /**
  * 2 Threads performing a readAll on a shared SequentialFile.
  * The order and contents of the records are verified.
  */
  public void Var008()
  {
    try
    {
      int   commitLevel         = AS400File.COMMIT_LOCK_LEVEL_NONE;
      int   blockingFactor      = 1;
      int   openType            = AS400File.WRITE_ONLY;
      RecordFormat recordFormat = new DDMChar10NoKeyFormat(systemObject_);
      pipeInput_                = new PipedInputStream();
      String       p1 = "/QSYS.LIB/" + lib + ".LIB" + "/READALL.FILE/MBR.MBR";
      SequentialFile f1 = new SequentialFile(systemObject_, p1);
      f1.create(recordFormat, "One field, CHAR(10), no key");

      if (!startJournal("READALL"))
      {
        output_.println("Could not start journaling.");
      }

      // populate the file
      f1.setRecordFormat(recordFormat);
      f1.open(openType, blockingFactor, commitLevel);
      Record[] records = new Record[9];
      for (short i = 1; i <= 9; i++)
      {
        records[i-1] = recordFormat.getNewRecord();
        records[i-1].setField(0, "RECORD " + String.valueOf(i) + "  ");
      }
      f1.write(records);
      f1.close();

      DDMThread  t1 = new DDMThread(pipeInput_, output_, this,
                      DDMThread.READ_ALL_SEQ, f1, records);
      DDMThread  t2 = new DDMThread(pipeInput_, output_, this,
                      DDMThread.READ_ALL_SEQ, f1, records);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();

      // clean-up file
      if (!endJournal("READALL"))
      {
        output_.println("Could not end journaling.");
      }
      f1.delete();

    }
    catch(Exception e)
    {
      output_.println("Unexpected exception:");
      e.printStackTrace(output_);
    }
  }


 /**
  * 2 Threads performing a readAll on a shared KeyedFile.
  * The order and contents of the records are verified.
  */
  public void Var009()
  {
    try
    {
      int   commitLevel         = AS400File.COMMIT_LOCK_LEVEL_NONE;
      int   blockingFactor      = 1;
      int   openType            = AS400File.WRITE_ONLY;
      RecordFormat recordFormat = new DDMChar10NoKeyFormat(systemObject_);
      pipeInput_                = new PipedInputStream();
      String       p1 = "/QSYS.LIB/" + lib + ".LIB" + "/READALL.FILE/MBR.MBR";
      KeyedFile f1 = new KeyedFile(systemObject_, p1);
      f1.create(recordFormat, "One field, CHAR(10), one key");

      if (!startJournal("READALL"))
      {
        output_.println("Could not start journaling.");
      }

      // populate the file
      f1.setRecordFormat(recordFormat);
      f1.open(openType, blockingFactor, commitLevel);
      Record[] records = new Record[9];
      for (short i = 1; i <= 9; i++)
      {
        records[i-1] = recordFormat.getNewRecord();
        records[i-1].setField(0, "RECORD " + String.valueOf(i) + "  ");
      }
      f1.write(records);
      f1.close();

      DDMThread  t1 = new DDMThread(pipeInput_, output_, this,
                      DDMThread.READ_ALL_KEYED, f1, records);
      DDMThread  t2 = new DDMThread(pipeInput_, output_, this,
                      DDMThread.READ_ALL_KEYED, f1, records);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();

      // clean-up file
      if (!endJournal("READALL"))
      {
        output_.println("Could not end journaling.");
      }
      f1.delete();

    }
    catch(Exception e)
    {
      output_.println("Unexpected exception:");
      e.printStackTrace(output_);
    }
  }

}



