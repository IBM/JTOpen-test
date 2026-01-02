///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMSerialization.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

/**
 *Testcase DDMSerialization.  This testcase verifies the ability
 *to serialize and deserialize AS400File, KeyedFile and
 *SequentialFile objects.
**/
public class DDMSerialization extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMSerialization";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  CommandCall cmd_;
  FileInputStream ris = null;
  ObjectInputStream rin = null;
  String fileName_ = null;

  
  /**
   * Constructor.  This is called from DDMTest::createTestcases().
  **/
  public DDMSerialization(AS400            systemObject,
                          Vector<String> variationsToRun,
                          int              runMode,
                          FileOutputStream fileOutputStream,
                          String testLib)
  {
    super(systemObject, "DDMSerialization", 9,
          variationsToRun, runMode, fileOutputStream);
    cmd_ = new CommandCall(systemObject_);
    setTestLib(testLib);
    fileName_ = "/QSYS.LIB/" + testLib_ + ".LIB/ddmser.FILE/%FILE%.MBR";
  }

  
  /**
   * Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      systemObject_.connectService(AS400.RECORDACCESS);
    }
    catch (Exception e)
    {
      output_.println("Unable to connect to the AS400.");
      e.printStackTrace(output_);
    }

    try
    {
      output_.println("Performing any necessary cleanup from previous run...");
      try { 
	  cleanup();
      } catch (Exception e) {
	  output_.println("Ignoring exception from cleanup");
	  e.printStackTrace();
	  output_.println("--------------------------------");
      } 
      output_.println("Performing setup for DDMSerialization testcase run...");
      setup();
    }
    catch (Exception e)
    {
      output_.println("Unable to run setup because of exception.");
      e.printStackTrace(); 
      systemObject_.disconnectService(AS400.RECORDACCESS);
      return;
    }

    output_.println("Setup successful.");
    
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

    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      output_.println("Cleanup failed.");
      return;
    }
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
  protected void setup()
  throws Exception
  {
    try
    {
      // Create library DDMTEST
      deleteLibrary(cmd_, testLib_);
      
      String msg = runCommand("QSYS/CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
      if (msg != null && !msg.equals("CPF2111"))
      {
        output_.println("Failure executing 'CRTLIB LIB(" + testLib_ + ") AUT(*ALL)'");
        output_.println(msg);
        throw new Exception("");
      }

      // Create the necessary file
      SequentialFile f1 = new SequentialFile(systemObject_, fileName_);
      f1.create(new DDMChar10NoKeyFormat(systemObject_), "DDMSerialization testcase file");

      // Populate the file
      Record[] records = new Record[50];
      RecordFormat rf = f1.getRecordFormat();
      for (short i = 0; i < 50; ++i)
      {
        records[i] = rf.getNewRecord();
        records[i].setField(0, "RECORD " + String.valueOf(i));
      }
      f1.open(AS400File.WRITE_ONLY, 50, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records);
      f1.close();

      // Create journal receiver and journal if it does not already exist
      msg = runCommand("QSYS/CRTJRNRCV JRNRCV("+testLib_+"/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')");
      if (msg != null && !msg.equals("CPF7010"))
      {
        output_.println("Failure executing 'CRTJRNRCV JRNRCV("+testLib_+"/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')'");
        output_.println(msg);
        throw new Exception("");
      }
      msg = runCommand("QSYS/CRTJRN JRN(" + testLib_ + "/JT4DDMJRN) JRNRCV(" + testLib_ + "/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')");
      if (msg != null && !msg.equals("CPF7010"))
      {
        output_.println("Failure executing 'CRTJRN JRN(" + testLib_ + "/JT4DDMJRN) JRNRCV(" + testLib_ + "/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')'");
        output_.println(msg);
        throw new Exception("");
      }

      // Start journaling
      msg = runCommand("QSYS/STRJRNPF FILE(" + testLib_ + "/DDMSER) JRN(" + testLib_ + "/JT4DDMJRN)");
      if (msg != null)
      {
        output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/DDMSER) JRN(" + testLib_ + "/JT4DDMJRN)'");
        output_.println(msg);
        throw new Exception("");
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
  protected void cleanup()
  throws Exception
  {
    boolean success = true;
    try
    {
      try
      {
        File f = new File("ddmser.ser");
        f.delete();
      }
      catch (Exception f)
      {
      }

      // Stop journaling
      output_.println("  Ending journaling...");
      String msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/DDMSER) JRN(" + testLib_ + "/JT4DDMJRN)");
      if (msg != null && !msg.startsWith("CPF9803") && !msg.startsWith("CPF9801") && !msg.startsWith("CPF7032")) //7032 because on cleanup, the system might think we are not actually journaling the file.
      {
        output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/DDMSER) JRN(" + testLib_ + "/JT4DDMJRN)'");
        output_.println(msg);
        success = false;
      }

      // Delete files created
      output_.println("  Deleting file...");
      msg = runCommand("QSYS/DLTF FILE("+testLib_+"/DDMSER)");
      if (msg != null && msg.startsWith("CPF3202"))
      {
        output_.println("Error: File DDMSER in library "+testLib_+" is still locked by a DDM server job.");
        output_.println("       Please signon to "+systemObject_.getSystemName()+" and do a WRKOBJLCK OBJ("+testLib_+"/DDMSER) OBJTYPE(*FILE).");
        output_.println("       Type a '4' (End job) on the line containing the QRWTSRVR job for user QUSER.");
        output_.println("       After pressing Enter twice, use the F5 key to refresh the screen.");
        output_.println("       Verify that there are no more locks for that file.");
        output_.println("       Repeat the above procedure for WRKOBJLCK OBJ(" + testLib_ + "/JT4DDMRCV) OBJTYPE(*JRNRCV).");
        output_.println("       Re-run this testcase.");
        throw new Exception("Delete file locks and re-run testcase.");
      }

      if (msg != null && !msg.startsWith("CPC2191") && !msg.startsWith("CPF2110"))
      {
        output_.println("Failure executing 'DLTF FILE("+testLib_+"/DDMSER)'");
        output_.println(msg);
        success = false;
      }
//      SequentialFile f1 = new SequentialFile(systemObject_, fileName_);
//      f1.delete();

      // Delete the DDMTest library and the journal file.  The journal
      // receive should be automatically deleted due to the way that the
      // journal and receiver were deleted.
      output_.println("  Deleting journal...");
      msg = runCommand("QSYS/DLTJRN " + testLib_ + "/JT4DDMJRN");
      if (msg != null && !msg.startsWith("CPF2105"))
      {
        output_.println("Failure executing 'DLTJRN " + testLib_ + "/JT4DDMJRN'");
        output_.println(msg);
        success = false;
      }
      output_.println("  Deleting receiver...");
      msg = runCommand("QSYS/DLTJRNRCV JRNRCV(" + testLib_ + "/JT4DDMRCV) DLTOPT(*IGNINQMSG)");
      if (msg != null && !msg.startsWith("CPF2105"))
      {
        output_.println("Failure executing 'DLTJRNRCV JRNRCV(" + testLib_ + "/JT4DDMRCV) DLTOPT(*IGNINQMSG)'");
        output_.println(msg);
        success = false;
      }
      output_.println("  Deleting library...");
      msg = deleteLibrary(cmd_, testLib_); 
      if (msg != null && !msg.startsWith("CPF2110"))
      {
        output_.println("Failure executing 'delete library " + testLib_ + "'");
        output_.println(msg);
        success = false;
      }

    }
    catch (Exception e)
    {
      output_.println("Cleanup unsuccessful.  Some files may have been left in " + testLib_ + " and QGPL");
      e.printStackTrace(output_);
      throw e;
    }

    if (!success)
    {
      throw new Exception("Cleanup was unsuccessful");
    }
  }

  /**
   *Verify the ability to serialize and deserialize an AS400File object.
   *<ul compact>
   *<li>When the object was constructed with the null constructor and
   *no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The AS400File object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The path, system and record format can be set for the object.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var001()
  {
    AS400File f = null;
    AS400File deserf = null;
    try
    {
      f = new SequentialFile();
      // Serialize
      FileOutputStream ros = new FileOutputStream("ddmser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();
      // Deserialize
      ris = new FileInputStream("ddmser.ser");
      rin = new ObjectInputStream(ris);
      deserf = (AS400File)rin.readObject();
      rin.close();
      ris.close();
      // Verify state -- Note:: This tests nothing since the static value are loaded by the current class.
      /* 
      if (deserf.READ_ONLY != AS400File.READ_ONLY ||
          deserf.WRITE_ONLY != AS400File.WRITE_ONLY ||
          deserf.READ_WRITE != AS400File.READ_WRITE ||
          deserf.COMMIT_LOCK_LEVEL_NONE != AS400File.COMMIT_LOCK_LEVEL_NONE ||
          deserf.COMMIT_LOCK_LEVEL_ALL != AS400File.COMMIT_LOCK_LEVEL_ALL ||
          deserf.COMMIT_LOCK_LEVEL_CHANGE != AS400File.COMMIT_LOCK_LEVEL_CHANGE ||
          deserf.COMMIT_LOCK_LEVEL_CURSOR_STABILITY != AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY)
      {
        failed("constants not correct for deserialized object");
        f.close();
        deserf.close();
        return;
      }
      */ 
      if (deserf.getBlockingFactor() != 0)
      {
        failed("getBlockingFactor() != 0");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getCommitLockLevel() != -1)
      {
        failed("getCommitLockLevel() != -1");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getExplicitLocks().length != 0)
      {
        failed("getExplicitLocks()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getFileName().equals(""))
      {
        failed("getFileName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getMemberName().equals(""))
      {
        failed("getMemberName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getPath().equals(""))
      {
        failed("getPath()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getRecordFormat() != null)
      {
        failed("getRecordFormat()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getSystem() != null)
      {
        failed("getSystem()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isOpen())
      {
        failed("isOpen()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadOnly())
      {
        failed("isReadOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadWrite())
      {
        failed("isReadWrite()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isWriteOnly())
      {
        failed("isWriteOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadNoUpdate())
      {
        failed("isReadNoUpdate()");
        f.close();
        deserf.close();
        return;
      }
      // Verify usability
      deserf.setPath(fileName_);
      deserf.setSystem(systemObject_);
      deserf.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      deserf.addFileListener(new DDMFileListener());
      deserf.addPropertyChangeListener(new DDMPropertyChangeListener());
      deserf.addVetoableChangeListener(new DDMVetoableChangeListener());
      deserf.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      if (deserf.getExplicitLocks().length != 1 &&
          deserf.getExplicitLocks()[0] != AS400File.READ_ALLOW_SHARED_READ_LOCK)
      {
        failed("Retrieving explicit locks after locking.");
        f.close();
        deserf.close();
        return;
      }
      deserf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = deserf.readNext();
      for (int i = 0; r != null; ++i, r = deserf.readNext())
      {
        if (!r.toString().startsWith("RECORD " + String.valueOf(i)))
        {
          failed("Record read not expected: " + r.toString());
          f.close();
          deserf.close();
          return;
        }
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        deserf.close();
      }
      catch (Exception e1)
      {
      }
      return;
    }
    try
    {
      f.close();
      deserf.close();
    }
    catch (Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an AS400File object.
   *<ul compact>
   *<li>When the object was constructed with the null constructor, the file
   *has not been opened, the system, name and record format have been set and
   *startCommitmentControl has been .
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The AS400File object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization and isCommitmentControlStarted will return false..
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var002()
  {
    AS400File f = null;
    AS400File deserf = null;
    try
    {
      f = new SequentialFile();
      // Set some of the state
      f.setPath(fileName_);
      f.setSystem(systemObject_);
      RecordFormat rfBeforeSerialization = new DDMChar10NoKeyFormat(systemObject_);
      f.setRecordFormat(rfBeforeSerialization);
      f.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);

      // Serialize
      FileOutputStream ros = new FileOutputStream("ddmser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();
      // Deserialize
      ris = new FileInputStream("ddmser.ser");
      rin = new ObjectInputStream(ris);
      deserf = (AS400File)rin.readObject();
      rin.close();
      ris.close();
      // Verify state
      if (deserf.getBlockingFactor() != 0)
      {
        failed("getBlockingFactor() != 0");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getCommitLockLevel() != -1)
      {
        failed("getCommitLockLevel() != -1");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getExplicitLocks().length != 0)
      {
        failed("getExplicitLocks()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getFileName().equals("DDMSER"))
      {
        failed("getFileName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getMemberName().equals("DDMSER"))
      {
        failed("getMemberName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getPath().equals(fileName_))
      {
        failed("getPath()");
        f.close();
        deserf.close();
        return;
      }
      RecordFormat rfAfter = deserf.getRecordFormat();
      if (!rfAfter.getName().equals(rfBeforeSerialization.getName()) ||
          rfAfter.getNumberOfFields() != rfBeforeSerialization.getNumberOfFields())
      {
        failed("getRecordFormat()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getSystem().getSystemName().equals(f.getSystem().getSystemName()) ||
          !deserf.getSystem().getUserId().equals(f.getSystem().getUserId()))
      {
        failed("getSystem()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isOpen())
      {
        failed("isOpen()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadOnly())
      {
        failed("isReadOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadWrite())
      {
        failed("isReadWrite()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isWriteOnly())
      {
        failed("isWriteOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadNoUpdate())
      {
        failed("isReadNoUpdate()");
        f.close();
        deserf.close();
        return;
      }
      // Let's reset the system object so we don't get a signon now that we've verified they're the same
      deserf.setSystem(f.getSystem());
      
      if (!deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted()");
        f.close();
        deserf.close();
        return;
      }

      // Verify usability
      deserf.addFileListener(new DDMFileListener());
      deserf.addPropertyChangeListener(new DDMPropertyChangeListener());
      deserf.addVetoableChangeListener(new DDMVetoableChangeListener());
      deserf.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      if (deserf.getExplicitLocks().length != 1 &&
          deserf.getExplicitLocks()[0] != AS400File.READ_ALLOW_SHARED_READ_LOCK)
      {
        failed("Retrieving explicit locks after locking.");
        f.close();
        deserf.close();
        return;
      }
      deserf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = deserf.readNext();
      for (int i = 0; r != null; ++i, r = deserf.readNext())
      {
        if (!r.toString().startsWith("RECORD " + String.valueOf(i)))
        {
          failed("Record read not expected: " + r.toString());
          f.close();
          deserf.close();
          return;
        }
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        deserf.close();
      }
      catch (Exception e1)
      {
      }
      return;
    }
    try
    {
      f.close();
      deserf.close();
    }
    catch (Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    try
    {
      f.close();
      deserf.close();
      f.endCommitmentControl();
    }
    catch (Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an AS400File object.
   *<ul compact>
   *<li>When the object was constructed with the null constructor and
   *the file has been opened.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The AS400File object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to opening the file.
   *<li>Any listeners added prior to serialization are no longer getting notified of
   *when an event is fired.
   *<li>Listeners can be added to the object.
   *<li>Commitment control can be started on the object.
   *<li>The file can be locked.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var003()
  {
    AS400File f = null;
    AS400File deserf = null;
    try
    {
      DDMFileListener l1 = new DDMFileListener();
      DDMPropertyChangeListener l2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener l3 = new DDMVetoableChangeListener();
      f = new SequentialFile();
      // Set some of the state and open the file
      f.setPath(fileName_);
      f.setSystem(systemObject_);
      f.addFileListener(l1);
      f.addPropertyChangeListener(l2);
      f.addVetoableChangeListener(l3);
      RecordFormat rfBeforeSerialization = new DDMChar10NoKeyFormat(systemObject_);
      f.setRecordFormat(rfBeforeSerialization);
      f.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      f.open(AS400File.READ_WRITE, 20, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      // Serialize
      FileOutputStream ros = new FileOutputStream("ddmser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();
      f.close();
      f.endCommitmentControl();
      // Reset the listener variables to false
      l1.closed = false;
      l1.created = false;
      l1.deleted = false;
      l1.modified = false;
      l1.opened = false;
      l2.propertyChangeFired_ = false;
      l3.vetoableChangeFired_ = false;
      // Deserialize
      ris = new FileInputStream("ddmser.ser");
      rin = new ObjectInputStream(ris);
      deserf = (AS400File)rin.readObject();
      rin.close();
      ris.close();
      // Verify state
      if (deserf.getBlockingFactor() != 0)
      {
        failed("getBlockingFactor() != 0");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getCommitLockLevel() != -1)
      {
        failed("getCommitLockLevel() != -1");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getExplicitLocks().length != 0)
      {
        failed("getExplicitLocks()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getFileName().equals("DDMSER"))
      {
        failed("getFileName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getMemberName().equals("DDMSER"))
      {
        failed("getMemberName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getPath().equals(fileName_))
      {
        failed("getPath()");
        f.close();
        deserf.close();
        return;
      }
      RecordFormat rfAfter = deserf.getRecordFormat();
      if (!rfAfter.getName().equals(rfBeforeSerialization.getName()) ||
          rfAfter.getNumberOfFields() != rfBeforeSerialization.getNumberOfFields())
      {
        failed("getRecordFormat()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getSystem().getSystemName().equals(f.getSystem().getSystemName()) ||
          !deserf.getSystem().getUserId().equals(f.getSystem().getUserId()))
      {
        failed("getSystem()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isOpen())
      {
        failed("isOpen()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadOnly())
      {
        failed("isReadOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadWrite())
      {
        failed("isReadWrite()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isWriteOnly())
      {
        failed("isWriteOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadNoUpdate())
      {
        failed("isReadNoUpdate()");
        f.close();
        deserf.close();
        return;
      }
      // Let's reset the system object so we don't get a signon now that we've verified they're the same
      deserf.setSystem(f.getSystem());
      if (deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted()");
        f.close();
        deserf.close();
        return;
      }


      // Verify usability
      DDMFileListener dl1 = new DDMFileListener();
      DDMPropertyChangeListener dl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener dl3 = new DDMVetoableChangeListener();
      deserf.addFileListener(dl1);
      deserf.addPropertyChangeListener(dl2);
      deserf.addVetoableChangeListener(dl3);
      deserf.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      if (deserf.getExplicitLocks().length != 1 &&
          deserf.getExplicitLocks()[0] != AS400File.READ_ALLOW_SHARED_READ_LOCK)
      {
        failed("Retrieving explicit locks after locking.");
        f.close();
        deserf.close();
        return;
      }
      deserf.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted after deserializing and starting commitment control.");
        f.close();
        deserf.close();
        return;
      }
      f.endCommitmentControl();
      deserf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Verify file listener added after deserialization
      if (!dl1.opened)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify pre-serialized FileListener no longer affected
      if (l1.opened)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
      Record r = deserf.readNext();
      for (int i = 0; r != null; ++i, r = deserf.readNext())
      {
        if (!r.toString().startsWith("RECORD " + String.valueOf(i)))
        {
          failed("Record read not expected: " + r.toString());
          f.close();
          deserf.close();
          return;
        }
      }
      deserf.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      // Verify current listeners
      if (!dl2.propertyChangeFired_)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      if (!dl3.vetoableChangeFired_)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify no one listening from pre-serialization
      if (l2.propertyChangeFired_)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
      if (l3.vetoableChangeFired_)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        deserf.close();
      }
      catch (Exception e1)
      {
      }
      return;
    }
    try
    {
      f.close();
      deserf.close();
    }
    catch (Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an AS400File object.
   *<ul compact>
   *<li>When the object was constructed with the constructor
   *that takes an AS400 and String and no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The AS400File object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var004()
  {
    AS400File f = null;
    AS400File deserf = null;
    try
    {
      f = new SequentialFile(systemObject_, fileName_);
      // Serialize
      FileOutputStream ros = new FileOutputStream("ddmser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();
      // Deserialize
      ris = new FileInputStream("ddmser.ser");
      rin = new ObjectInputStream(ris);
      deserf = (AS400File)rin.readObject();
      rin.close();
      ris.close();
      // Verify state
      if (deserf.getBlockingFactor() != 0)
      {
        failed("getBlockingFactor() != 0");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getCommitLockLevel() != -1)
      {
        failed("getCommitLockLevel() != -1");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getExplicitLocks().length != 0)
      {
        failed("getExplicitLocks()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getFileName().equals("DDMSER"))
      {
        failed("getFileName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getMemberName().equals("DDMSER"))
      {
        failed("getMemberName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getPath().equals(fileName_))
      {
        failed("getPath()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getRecordFormat() != null)
      {
        failed("getRecordFormat()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getSystem().getSystemName().equals(f.getSystem().getSystemName()) ||
          !deserf.getSystem().getUserId().equals(f.getSystem().getUserId()))
      {
        failed("getSystem()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isOpen())
      {
        failed("isOpen()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadOnly())
      {
        failed("isReadOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadWrite())
      {
        failed("isReadWrite()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isWriteOnly())
      {
        failed("isWriteOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadNoUpdate())
      {
        failed("isReadNoUpdate()");
        f.close();
        deserf.close();
        return;
      }
      // Let's reset the system object so we don't get a signon now that we've verified they're the same
      deserf.setSystem(f.getSystem());
      if (deserf.isCommitmentControlStarted() != f.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted()");
        f.close();
        deserf.close();
        return;
      }


      // Verify usability
      deserf.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      deserf.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      if (deserf.getExplicitLocks().length != 1 &&
          deserf.getExplicitLocks()[0] != AS400File.READ_ALLOW_SHARED_READ_LOCK)
      {
        failed("Retrieving explicit locks after locking.");
        f.close();
        deserf.close();
        return;
      }
      deserf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = deserf.readNext();
      for (int i = 0; r != null; ++i, r = deserf.readNext())
      {
        if (!r.toString().startsWith("RECORD " + String.valueOf(i)))
        {
          failed("Record read not expected: " + r.toString());
          f.close();
          deserf.close();
          return;
        }
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        deserf.close();
      }
      catch (Exception e1)
      {
      }
      return;
    }
    try
    {
      f.close();
      deserf.close();
    }
    catch (Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an AS400File object.
   *<ul compact>
   *<li>When the object was constructed with the constructor
   *that takes an AS400 and String and the file has been opened.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The AS400File object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to opening the file.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var005()
  {
    AS400File f = null;
    AS400File deserf = null;
    try
    {
      DDMFileListener l1 = new DDMFileListener();
      DDMPropertyChangeListener l2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener l3 = new DDMVetoableChangeListener();
      f = new SequentialFile(systemObject_, fileName_);
      // Set some of the state and open the file
      f.addFileListener(l1);
      f.addPropertyChangeListener(l2);
      f.addVetoableChangeListener(l3);
      RecordFormat rfBeforeSerialization = new DDMChar10NoKeyFormat(systemObject_);
      f.setRecordFormat(rfBeforeSerialization);
      f.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      f.open(AS400File.READ_WRITE, 20, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      // Serialize
      FileOutputStream ros = new FileOutputStream("ddmser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();
      f.close();
      f.endCommitmentControl();
      // Reset the listener variables to false
      l1.closed = false;
      l1.created = false;
      l1.deleted = false;
      l1.modified = false;
      l1.opened = false;
      l2.propertyChangeFired_ = false;
      l3.vetoableChangeFired_ = false;
      // Deserialize
      ris = new FileInputStream("ddmser.ser");
      rin = new ObjectInputStream(ris);
      deserf = (AS400File)rin.readObject();
      rin.close();
      ris.close();
      // Verify state
      if (deserf.getBlockingFactor() != 0)
      {
        failed("getBlockingFactor() != 0");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getCommitLockLevel() != -1)
      {
        failed("getCommitLockLevel() != -1");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getExplicitLocks().length != 0)
      {
        failed("getExplicitLocks()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getFileName().equals("DDMSER"))
      {
        failed("getFileName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getMemberName().equals("DDMSER"))
      {
        failed("getMemberName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getPath().equals(fileName_))
      {
        failed("getPath()");
        f.close();
        deserf.close();
        return;
      }
      RecordFormat rfAfter = deserf.getRecordFormat();
      if (!rfAfter.getName().equals(rfBeforeSerialization.getName()) ||
          rfAfter.getNumberOfFields() != rfBeforeSerialization.getNumberOfFields())
      {
        failed("getRecordFormat()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getSystem().getSystemName().equals(f.getSystem().getSystemName()) ||
          !deserf.getSystem().getUserId().equals(f.getSystem().getUserId()))
      {
        failed("getSystem()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isOpen())
      {
        failed("isOpen()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadOnly())
      {
        failed("isReadOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadWrite())
      {
        failed("isReadWrite()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isWriteOnly())
      {
        failed("isWriteOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadNoUpdate())
      {
        failed("isReadNoUpdate()");
        f.close();
        deserf.close();
        return;
      }
      // Let's reset the system object so we don't get a signon now that we've verified they're the same
      deserf.setSystem(f.getSystem());
      if (deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted()");
        f.close();
        deserf.close();
        return;
      }


      // Verify usability
      DDMFileListener dl1 = new DDMFileListener();
      DDMPropertyChangeListener dl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener dl3 = new DDMVetoableChangeListener();
      deserf.addFileListener(dl1);
      deserf.addPropertyChangeListener(dl2);
      deserf.addVetoableChangeListener(dl3);
      deserf.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      if (deserf.getExplicitLocks().length != 1 &&
          deserf.getExplicitLocks()[0] != AS400File.READ_ALLOW_SHARED_READ_LOCK)
      {
        failed("Retrieving explicit locks after locking.");
        f.close();
        deserf.close();
        return;
      }
      deserf.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted after deserializing and starting commitment control.");
        f.close();
        deserf.close();
        return;
      }
      f.endCommitmentControl();
      deserf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Verify file listener added after deserialization
      if (!dl1.opened)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify pre-serialized FileListener no longer affected
      if (l1.opened)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
      Record r = deserf.readNext();
      for (int i = 0; r != null; ++i, r = deserf.readNext())
      {
        if (!r.toString().startsWith("RECORD " + String.valueOf(i)))
        {
          failed("Record read not expected: " + r.toString());
          f.close();
          deserf.close();
          return;
        }
      }
      deserf.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      // Verify current listeners
      if (!dl2.propertyChangeFired_)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      if (!dl3.vetoableChangeFired_)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify no one listening from pre-serialization
      if (l2.propertyChangeFired_)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
      if (l3.vetoableChangeFired_)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        deserf.close();
      }
      catch (Exception e1)
      {
      }
      return;
    }
    try
    {
      f.close();
      deserf.close();
    }
    catch (Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a KeyedFile object.
   *<ul compact>
   *<li>When the object has not been opened.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The KeyedFile object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var006()
  {
    KeyedFile f = null;
    KeyedFile deserf = null;
    try
    {
      f = new KeyedFile(systemObject_, fileName_);
      // Serialize
      FileOutputStream ros = new FileOutputStream("ddmser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();
      // Deserialize
      ris = new FileInputStream("ddmser.ser");
      rin = new ObjectInputStream(ris);
      deserf = (KeyedFile)rin.readObject();
      rin.close();
      ris.close();
      // Verify state
      if (deserf.getBlockingFactor() != 0)
      {
        failed("getBlockingFactor() != 0");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getCommitLockLevel() != -1)
      {
        failed("getCommitLockLevel() != -1");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getExplicitLocks().length != 0)
      {
        failed("getExplicitLocks()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getFileName().equals("DDMSER"))
      {
        failed("getFileName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getMemberName().equals("DDMSER"))
      {
        failed("getMemberName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getPath().equals(fileName_))
      {
        failed("getPath()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getRecordFormat() != null)
      {
        failed("getRecordFormat()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getSystem().getSystemName().equals(f.getSystem().getSystemName()) ||
          !deserf.getSystem().getUserId().equals(f.getSystem().getUserId()))
      {
        failed("getSystem()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isOpen())
      {
        failed("isOpen()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadOnly())
      {
        failed("isReadOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadWrite())
      {
        failed("isReadWrite()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isWriteOnly())
      {
        failed("isWriteOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadNoUpdate())
      {
        failed("isReadNoUpdate()");
        f.close();
        deserf.close();
        return;
      }
      // Let's reset the system object so we don't get a signon now that we've verified they're the same
      deserf.setSystem(f.getSystem());
      if (deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted()");
        f.close();
        deserf.close();
        return;
      }


      // Verify usability
      deserf.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      deserf.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      if (deserf.getExplicitLocks().length != 1 &&
          deserf.getExplicitLocks()[0] != AS400File.READ_ALLOW_SHARED_READ_LOCK)
      {
        failed("Retrieving explicit locks after locking.");
        f.close();
        deserf.close();
        return;
      }
      deserf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = deserf.readNext();
      for (int i = 0; r != null; ++i, r = deserf.readNext())
      {
        if (!r.toString().startsWith("RECORD " + String.valueOf(i)))
        {
          failed("Record read not expected: " + r.toString());
          f.close();
          deserf.close();
          return;
        }
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        deserf.close();
      }
      catch (Exception e1)
      {
      }
      return;
    }
    try
    {
      f.close();
      deserf.close();
    }
    catch (Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a KeyedFile object.
   *<ul compact>
   *<li>When the object has been opened.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The KeyedFile object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to opening the file.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var007()
  {
    KeyedFile f = null;
    KeyedFile deserf = null;
    try
    {
      DDMFileListener l1 = new DDMFileListener();
      DDMPropertyChangeListener l2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener l3 = new DDMVetoableChangeListener();
      f = new KeyedFile(systemObject_, fileName_);
      // Set some of the state and open the file
      f.addFileListener(l1);
      f.addPropertyChangeListener(l2);
      f.addVetoableChangeListener(l3);
      RecordFormat rfBeforeSerialization = new DDMChar10NoKeyFormat(systemObject_);
      f.setRecordFormat(rfBeforeSerialization);
      f.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      f.open(AS400File.READ_WRITE, 20, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      // Serialize
      FileOutputStream ros = new FileOutputStream("ddmser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();
      f.close();
      f.endCommitmentControl();
      // Reset the listener variables to false
      l1.closed = false;
      l1.created = false;
      l1.deleted = false;
      l1.modified = false;
      l1.opened = false;
      l2.propertyChangeFired_ = false;
      l3.vetoableChangeFired_ = false;
      // Deserialize
      ris = new FileInputStream("ddmser.ser");
      rin = new ObjectInputStream(ris);
      deserf = (KeyedFile)rin.readObject();
      rin.close();
      ris.close();
      // Verify state
      if (deserf.getBlockingFactor() != 0)
      {
        failed("getBlockingFactor() != 0");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getCommitLockLevel() != -1)
      {
        failed("getCommitLockLevel() != -1");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getExplicitLocks().length != 0)
      {
        failed("getExplicitLocks()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getFileName().equals("DDMSER"))
      {
        failed("getFileName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getMemberName().equals("DDMSER"))
      {
        failed("getMemberName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getPath().equals(fileName_))
      {
        failed("getPath()");
        f.close();
        deserf.close();
        return;
      }
      RecordFormat rfAfter = deserf.getRecordFormat();
      if (!rfAfter.getName().equals(rfBeforeSerialization.getName()) ||
          rfAfter.getNumberOfFields() != rfBeforeSerialization.getNumberOfFields())
      {
        failed("getRecordFormat()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getSystem().getSystemName().equals(f.getSystem().getSystemName()) ||
          !deserf.getSystem().getUserId().equals(f.getSystem().getUserId()))
      {
        failed("getSystem()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isOpen())
      {
        failed("isOpen()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadOnly())
      {
        failed("isReadOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadWrite())
      {
        failed("isReadWrite()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isWriteOnly())
      {
        failed("isWriteOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadNoUpdate())
      {
        failed("isReadNoUpdate()");
        f.close();
        deserf.close();
        return;
      }
      // Let's reset the system object so we don't get a signon now that we've verified they're the same
      deserf.setSystem(f.getSystem());
      if (deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted()");
        f.close();
        deserf.close();
        return;
      }


      // Verify usability
      DDMFileListener dl1 = new DDMFileListener();
      DDMPropertyChangeListener dl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener dl3 = new DDMVetoableChangeListener();
      deserf.addFileListener(dl1);
      deserf.addPropertyChangeListener(dl2);
      deserf.addVetoableChangeListener(dl3);
      deserf.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      if (deserf.getExplicitLocks().length != 1 &&
          deserf.getExplicitLocks()[0] != AS400File.READ_ALLOW_SHARED_READ_LOCK)
      {
        failed("Retrieving explicit locks after locking.");
        f.close();
        deserf.close();
        return;
      }
      deserf.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted after deserializing and starting commitment control.");
        f.close();
        deserf.close();
        return;
      }
      f.endCommitmentControl();
      deserf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Verify file listener added after deserialization
      if (!dl1.opened)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify pre-serialized FileListener no longer affected
      if (l1.opened)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
      Record r = deserf.readNext();
      for (int i = 0; r != null; ++i, r = deserf.readNext())
      {
        if (!r.toString().startsWith("RECORD " + String.valueOf(i)))
        {
          failed("Record read not expected: " + r.toString());
          f.close();
          deserf.close();
          return;
        }
      }
      deserf.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      // Verify current listeners
      if (!dl2.propertyChangeFired_)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      if (!dl3.vetoableChangeFired_)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify no one listening from pre-serialization
      if (l2.propertyChangeFired_)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
      if (l3.vetoableChangeFired_)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        deserf.close();
      }
      catch (Exception e1)
      {
      }
      return;
    }
    try
    {
      f.close();
      deserf.close();
    }
    catch (Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a SequentialFile object.
   *<ul compact>
   *<li>When the object has not been opened.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The SequentialFile object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var008()
  {
    SequentialFile f = null;
    SequentialFile deserf = null;
    try
    {
      f = new SequentialFile();
      // Serialize
      FileOutputStream ros = new FileOutputStream("ddmser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();
      // Deserialize
      ris = new FileInputStream("ddmser.ser");
      rin = new ObjectInputStream(ris);
      deserf = (SequentialFile)rin.readObject();
      rin.close();
      ris.close();
      // Verify state
      if (deserf.getSystem() != null)
      {
        failed("getSystem()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getBlockingFactor() != 0)
      {
        failed("getBlockingFactor() != 0");
        f.close();
        deserf.close();
        return;
      }
      deserf.setSystem(new AS400()); // This is needed to avoid null pointer exception
      if (deserf.getCommitLockLevel() != -1)
      {
        failed("getCommitLockLevel() != -1");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getExplicitLocks().length != 0)
      {
        failed("getExplicitLocks()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getFileName().equals(""))
      {
        failed("getFileName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getMemberName().equals(""))
      {
        failed("getMemberName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getPath().equals(""))
      {
        failed("getPath()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getRecordFormat() != null)
      {
        failed("getRecordFormat()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isOpen())
      {
        failed("isOpen()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadOnly())
      {
        failed("isReadOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadWrite())
      {
        failed("isReadWrite()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isWriteOnly())
      {
        failed("isWriteOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadNoUpdate())
      {
        failed("isReadNoUpdate()");
        f.close();
        deserf.close();
        return;
      }

      // Verify usability
      deserf.setPath(fileName_);
      deserf.setSystem(systemObject_);
      deserf.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      deserf.addFileListener(new DDMFileListener());
      deserf.addPropertyChangeListener(new DDMPropertyChangeListener());
      deserf.addVetoableChangeListener(new DDMVetoableChangeListener());
      deserf.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      if (deserf.getExplicitLocks().length != 1 &&
          deserf.getExplicitLocks()[0] != AS400File.READ_ALLOW_SHARED_READ_LOCK)
      {
        failed("Retrieving explicit locks after locking.");
        f.close();
        deserf.close();
        return;
      }
      deserf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = deserf.readNext();
      for (int i = 0; r != null; ++i, r = deserf.readNext())
      {
        if (!r.toString().startsWith("RECORD " + String.valueOf(i)))
        {
          failed("Record read not expected: " + r.toString());
          f.close();
          deserf.close();
          return;
        }
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        deserf.close();
      }
      catch (Exception e1)
      {
      }
      return;
    }
    try
    {
      f.close();
      deserf.close();
    }
    catch (Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a SequentialFile object.
   *<ul compact>
   *<li>When the object has been opened.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The SequentialFile object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to opening the file.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var009()
  {
    SequentialFile f = null;
    SequentialFile deserf = null;
    try
    {
      DDMFileListener l1 = new DDMFileListener();
      DDMPropertyChangeListener l2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener l3 = new DDMVetoableChangeListener();
      f = new SequentialFile(systemObject_, fileName_);
      // Set some of the state and open the file
      f.addFileListener(l1);
      f.addPropertyChangeListener(l2);
      f.addVetoableChangeListener(l3);
      RecordFormat rfBeforeSerialization = new DDMChar10NoKeyFormat(systemObject_);
      f.setRecordFormat(rfBeforeSerialization);
      f.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      f.open(AS400File.READ_WRITE, 20, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      // Serialize
      FileOutputStream ros = new FileOutputStream("ddmser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();
      f.close();
      f.endCommitmentControl();
      // Reset the listener variables to false
      l1.closed = false;
      l1.created = false;
      l1.deleted = false;
      l1.modified = false;
      l1.opened = false;
      l2.propertyChangeFired_ = false;
      l3.vetoableChangeFired_ = false;
      // Deserialize
      ris = new FileInputStream("ddmser.ser");
      rin = new ObjectInputStream(ris);
      deserf = (SequentialFile)rin.readObject();
      rin.close();
      ris.close();
      // Verify state
      if (deserf.getBlockingFactor() != 0)
      {
        failed("getBlockingFactor() != 0");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getCommitLockLevel() != -1)
      {
        failed("getCommitLockLevel() != -1");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.getExplicitLocks().length != 0)
      {
        failed("getExplicitLocks()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getFileName().equals("DDMSER"))
      {
        failed("getFileName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getMemberName().equals("DDMSER"))
      {
        failed("getMemberName()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getPath().equals(fileName_))
      {
        failed("getPath()");
        f.close();
        deserf.close();
        return;
      }
      RecordFormat rfAfter = deserf.getRecordFormat();
      if (!rfAfter.getName().equals(rfBeforeSerialization.getName()) ||
          rfAfter.getNumberOfFields() != rfBeforeSerialization.getNumberOfFields())
      {
        failed("getRecordFormat()");
        f.close();
        deserf.close();
        return;
      }
      if (!deserf.getSystem().getSystemName().equals(f.getSystem().getSystemName()) ||
          !deserf.getSystem().getUserId().equals(f.getSystem().getUserId()))
      {
        failed("getSystem()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isOpen())
      {
        failed("isOpen()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadOnly())
      {
        failed("isReadOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadWrite())
      {
        failed("isReadWrite()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isWriteOnly())
      {
        failed("isWriteOnly()");
        f.close();
        deserf.close();
        return;
      }
      if (deserf.isReadNoUpdate())
      {
        failed("isReadNoUpdate()");
        f.close();
        deserf.close();
        return;
      }
      // Let's reset the system object so we don't get a signon now that we've verified they're the same
      deserf.setSystem(f.getSystem());
      if (deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted()");
        f.close();
        deserf.close();
        return;
      }

      // Verify usability
      DDMFileListener dl1 = new DDMFileListener();
      DDMPropertyChangeListener dl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener dl3 = new DDMVetoableChangeListener();
      deserf.addFileListener(dl1);
      deserf.addPropertyChangeListener(dl2);
      deserf.addVetoableChangeListener(dl3);
      deserf.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      if (deserf.getExplicitLocks().length != 1 &&
          deserf.getExplicitLocks()[0] != AS400File.READ_ALLOW_SHARED_READ_LOCK)
      {
        failed("Retrieving explicit locks after locking.");
        f.close();
        deserf.close();
        return;
      }
      deserf.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!deserf.isCommitmentControlStarted())
      {
        failed("isCommitmentControlStarted after deserializing and starting commitment control.");
        f.close();
        deserf.close();
        return;
      }
      f.endCommitmentControl();
      deserf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Verify file listener added after deserialization
      if (!dl1.opened)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify pre-serialized FileListener no longer affected
      if (l1.opened)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
      Record r = deserf.readNext();
      for (int i = 0; r != null; ++i, r = deserf.readNext())
      {
        if (!r.toString().startsWith("RECORD " + String.valueOf(i)))
        {
          failed("Record read not expected: " + r.toString());
          f.close();
          deserf.close();
          return;
        }
      }
      deserf.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      // Verify current listeners
      if (!dl2.propertyChangeFired_)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      if (!dl3.vetoableChangeFired_)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify no one listening from pre-serialization
      if (l2.propertyChangeFired_)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
      if (l3.vetoableChangeFired_)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        deserf.close();
      }
      catch (Exception e1)
      {
      }
      return;
    }
    try
    {
      f.close();
      deserf.close();
    }
    catch (Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }
}
