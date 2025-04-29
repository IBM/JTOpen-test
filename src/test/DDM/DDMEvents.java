///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMEvents.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.beans.PropertyVetoException;
import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

/**
 *Testcase DDMEvents.  This test class verifies valid and invalid
 *usage of:
 *<ul compact>
 *<li>AS400File.addFileListener()
 *<li>AS400File.addPropertyChangeListener()
 *<li>AS400File.addVetoableChangeListener()
 *<li>AS400File.removeFileListener()
 *<li>AS400File.removePropertyChangeListener()
 *<li>AS400File.removeVetoableChangeListener()
 *<li>AS400FileRecordDescription.addAS400FileRecordDescriptionListener()
 *<li>AS400FileRecordDescription.addPropertyChangeListener()
 *<li>AS400FileRecordDescription.addVetoableChangeListener()
 *<li>AS400FileRecordDescription.removeAS400FileRecordDescriptionListener()
 *<li>AS400FileRecordDescription.removePropertyChangeListener()
 *<li>AS400FileRecordDescription.removeVetoableChangeListener()
 *</ul>
 *This test class also verifies that the following events are fired
 *from the specified methods:
 *<ul compact>
 *<li>AS400File.setPath() - PropertyChangeEvent, VetoablePropertyChangeEvent
 *<li>AS400File.setRecordFormat() - PropertyChangeEvent, VetoablePropertyChangeEvent
 *<li>AS400File.setSystem() - PropertyChangeEvent, VetoablePropertyChangeEvent
 *<li>AS400FileRecordDescription.setPath() - PropertyChangeEvent, VetoablePropertyChangeEvent
 *<li>AS400FileRecordDescription.setSystem() - PropertyChangeEvent, VetoablePropertyChangeEvent
 *<li>AS400File.close() - FileEvent.fileClosed()
 *<li>AS400File.create() - FileEvent.fileCreated()
 *<li>AS400File.delete() - FileEvent.fileDeleted()
 *<li>AS400File.deleteCurrentRecord() - FileEvent.fileModified()
 *<li>AS400File.update() - FileEvent.fileModified()
 *<li>AS400File.write() - FileEvent.fileModified()
 *<li>KeyedFile.deleteRecord() - FileEvent.fileModified()
 *<li>SequentialFile.deleteRecord() - FileEvent.fileModified()
 *<li>KeyedFile.open() - FileEvent.fileOpened()
 *<li>SequentialFile.open() - FileEvent.fileOpened()
 *<li>KeyedFile.update() - FileEvent.fileModified()
 *<li>SequentialFile.update() - FileEvent.fileModified()
 *<li>AS400FileRecordDescription.createRecordFormatSource() -
AS400FileRecordDescription.recordFormatSourceCreated()
 *<li>AS400FileRecordDescription.retrieveRecordFormat() -
AS400FileRecordDescription.recordFormatRetrieved()
 *</ul>
**/
public class DDMEvents extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMEvents";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  String testLib_ = null;
  /**
   *Constructor.  This is called from DDMTest::createTestcases().
  **/
  public DDMEvents(AS400            systemObject,
                   Vector<String> variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   
                   String testLib)
  {
    super(systemObject, "DDMEvents", 48,
          variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
  }

/**
  Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    // Do any necessary setup work for the variations
    try
    {
      setup();
    }
    catch (Exception e)
    {
      // Testcase setup did not complete successfully
      System.out.println("Unable to complete setup; variations not run");
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
    if ((allVariations || variationsToRun_.contains("10")) &&
        runMode_ != ATTENDED)
    {
      setVariation(10);
      Var010();
    }
    if ((allVariations || variationsToRun_.contains("11")) &&
        runMode_ != ATTENDED)
    {
      setVariation(11);
      Var011();
    }
    if ((allVariations || variationsToRun_.contains("12")) &&
        runMode_ != ATTENDED)
    {
      setVariation(12);
      Var012();
    }
    if ((allVariations || variationsToRun_.contains("13")) &&
        runMode_ != ATTENDED)
    {
      setVariation(13);
      Var013();
    }
    if ((allVariations || variationsToRun_.contains("14")) &&
        runMode_ != ATTENDED)
    {
      setVariation(14);
      Var014();
    }
    if ((allVariations || variationsToRun_.contains("15")) &&
        runMode_ != ATTENDED)
    {
      setVariation(15);
      Var015();
    }
    if ((allVariations || variationsToRun_.contains("16")) &&
        runMode_ != ATTENDED)
    {
      setVariation(16);
      Var016();
    }
    if ((allVariations || variationsToRun_.contains("17")) &&
        runMode_ != ATTENDED)
    {
      setVariation(17);
      Var017();
    }
    if ((allVariations || variationsToRun_.contains("18")) &&
        runMode_ != ATTENDED)
    {
      setVariation(18);
      Var018();
    }
    if ((allVariations || variationsToRun_.contains("19")) &&
        runMode_ != ATTENDED)
    {
      setVariation(19);
      Var019();
    }
    if ((allVariations || variationsToRun_.contains("20")) &&
        runMode_ != ATTENDED)
    {
      setVariation(20);
      Var020();
    }
    if ((allVariations || variationsToRun_.contains("21")) &&
        runMode_ != ATTENDED)
    {
      setVariation(21);
      Var021();
    }
    if ((allVariations || variationsToRun_.contains("22")) &&
        runMode_ != ATTENDED)
    {
      setVariation(22);
      Var022();
    }
    if ((allVariations || variationsToRun_.contains("23")) &&
        runMode_ != ATTENDED)
    {
      setVariation(23);
      Var023();
    }
    if ((allVariations || variationsToRun_.contains("24")) &&
        runMode_ != ATTENDED)
    {
      setVariation(24);
      Var024();
    }
    if ((allVariations || variationsToRun_.contains("25")) &&
        runMode_ != ATTENDED)
    {
      setVariation(25);
      Var025();
    }
    if ((allVariations || variationsToRun_.contains("26")) &&
        runMode_ != ATTENDED)
    {
      setVariation(26);
      Var026();
    }
    if ((allVariations || variationsToRun_.contains("27")) &&
        runMode_ != ATTENDED)
    {
      setVariation(27);
      Var027();
    }
    if ((allVariations || variationsToRun_.contains("28")) &&
        runMode_ != ATTENDED)
    {
      setVariation(28);
      Var028();
    }
    if ((allVariations || variationsToRun_.contains("29")) &&
        runMode_ != ATTENDED)
    {
      setVariation(29);
      Var029();
    }
    if ((allVariations || variationsToRun_.contains("30")) &&
        runMode_ != ATTENDED)
    {
      setVariation(30);
      Var030();
    }
    if ((allVariations || variationsToRun_.contains("31")) &&
        runMode_ != ATTENDED)
    {
      setVariation(31);
      Var031();
    }
    if ((allVariations || variationsToRun_.contains("32")) &&
        runMode_ != ATTENDED)
    {
      setVariation(32);
      Var032();
    }
    if ((allVariations || variationsToRun_.contains("33")) &&
        runMode_ != ATTENDED)
    {
      setVariation(33);
      Var033();
    }
    if ((allVariations || variationsToRun_.contains("34")) &&
        runMode_ != ATTENDED)
    {
      setVariation(34);
      Var034();
    }
    if ((allVariations || variationsToRun_.contains("35")) &&
        runMode_ != ATTENDED)
    {
      setVariation(35);
      Var035();
    }
    if ((allVariations || variationsToRun_.contains("36")) &&
        runMode_ != ATTENDED)
    {
      setVariation(36);
      Var036();
    }
    if ((allVariations || variationsToRun_.contains("37")) &&
        runMode_ != ATTENDED)
    {
      setVariation(37);
      Var037();
    }
    if ((allVariations || variationsToRun_.contains("38")) &&
        runMode_ != ATTENDED)
    {
      setVariation(38);
      Var038();
    }
    if ((allVariations || variationsToRun_.contains("39")) &&
        runMode_ != ATTENDED)
    {
      setVariation(39);
      Var039();
    }
    if ((allVariations || variationsToRun_.contains("40")) &&
        runMode_ != ATTENDED)
    {
      setVariation(40);
      Var040();
    }
    if ((allVariations || variationsToRun_.contains("41")) &&
        runMode_ != ATTENDED)
    {
      setVariation(41);
      Var041();
    }
    if ((allVariations || variationsToRun_.contains("42")) &&
        runMode_ != ATTENDED)
    {
      setVariation(42);
      Var042();
    }
    if ((allVariations || variationsToRun_.contains("43")) &&
        runMode_ != ATTENDED)
    {
      setVariation(43);
      Var043();
    }
    if ((allVariations || variationsToRun_.contains("44")) &&
        runMode_ != ATTENDED)
    {
      setVariation(44);
      Var044();
    }
    if ((allVariations || variationsToRun_.contains("45")) &&
        runMode_ != ATTENDED)
    {
      setVariation(45);
      Var045();
    }
    if ((allVariations || variationsToRun_.contains("46")) &&
        runMode_ != ATTENDED)
    {
      setVariation(46);
      Var046();
    }
    if ((allVariations || variationsToRun_.contains("47")) &&
        runMode_ != ATTENDED)
    {
      setVariation(47);
      Var047();
    }
    if ((allVariations || variationsToRun_.contains("48")) &&
        runMode_ != ATTENDED)
    {
      setVariation(48);
      Var048();
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
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    try
    {
      // Delete and recreate library DDMTEST
      CommandCall c = new CommandCall(systemObject_);
	deleteLibrary(c, testLib_);

      c.run("CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
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
      SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE/%FILE%.MBR");
      f1.create(new DDMChar10NoKeyFormat(systemObject_), "DDMEvents file");
      KeyedFile f2 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE2.FILE/%FILE%.MBR");
      f2.create(new DDMChar10KeyFormat(systemObject_), "DDMEvents keyed file");
      // Populate the files
      Record[] recs = new Record[9];
      for (int i = 1; i < 10; ++i)
      {
        recs[i-1] = f1.getRecordFormat().getNewRecord();
        recs[i-1].setField(0, "RECORD00" + String.valueOf(i));
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f2.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(recs);
      f2.write(recs);
      f1.close();
      f2.close();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
    try
    {
      // Verify the existence of library DDMTESTSAV on the system
      CommandCall c = new CommandCall(systemObject_, "CHKOBJ OBJ(DDMTESTSAV) OBJTYPE(*LIB)");
      c.run();
      AS400Message[] msgs = c.getMessageList();
      if (msgs.length != 0)
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        System.out.println("Either library DDMTESTSAV does not exist or you");
        System.out.println("do not have authority to it.");
        System.out.println("ftp DDMTESTSAV.SAVF in binary from");
        System.out.println("the test/ directory in GIT");
        System.out.println("to the AS/400 system to which you are running.");
        System.out.println("Use RSTLIB to restore library DDMTESTSAV to the system.");
        throw new Exception("Authority problem or create failure");
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
    try
    {
      // Delete the files created during setup()
      SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");
      f1.close(); 
      f1.delete();
      KeyedFile f2 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE2.FILE");
      f2.close(); 
      f2.delete();
    }
    catch(Exception e)
    {
      System.out.println("Cleanup unsuccessful. Delete files " + testLib_ + "/FILE1 and FILE2 if they exist");
      e.printStackTrace(output_);
      throw e;
    }
  }

  void fireFileOpenedEvent(AS400File f)
    throws Exception
  {
    f.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
    f.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
  }

  void fireRecordFormatRetrievedEvent(AS400FileRecordDescription f)
    throws Exception
  {
    f.retrieveRecordFormat();
  }

  void firePropertyChangeEvent(AS400File f, String path)
    throws PropertyVetoException
  {
    f.setPath(path);
  }

  void firePropertyChangeEvent(AS400FileRecordDescription f, String path)
    throws PropertyVetoException
  {
    f.setPath(path);
  }

  /**
   *Verify valid usage AS400File.addFileListener().
   *<ul compact>
   *<li>For one, two and three FileListeners
   *<ul compact>
   *<li>Add the FileListener(s)
   *<li>Invoke a function which fires a FileEvent
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *</ul>
  **/
  public void Var001()
  {
    AS400File f = null;
    try
    {
      // Create our file listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();
      DDMFileListener rl3 = new DDMFileListener();
      // Create our AS400File object
      f = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");
      // Verify one listener
      f.addFileListener(rl1);
      // Cause a FileEvent to be fired
      fireFileOpenedEvent(f);
      if (!rl1.opened)
      {
        failed("Single listener");
        try
        {
          f.close();
        }
        catch(Exception e){}
        return;
      }
      // Reset listener
      rl1.reset();
      f.close();

      // Verify two listeners
      f.addFileListener(rl2);
      // Cause a FileEvent to be fired
      fireFileOpenedEvent(f);
      if (!rl1.opened || !rl2.opened)
      {
        failed("two listeners");
        try
        {
          f.close();
        }
        catch(Exception e){}
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();
      f.close();

      // Verify three listeners
      f.addFileListener(rl3);
      // Cause a FileEvent to be fired
      fireFileOpenedEvent(f);
      if (!rl1.opened || !rl2.opened || !rl3.opened)
      {
        failed("three listeners");
        try
        {
          f.close();
        }
        catch(Exception e){}
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        if (f!=null) f.close();
      }
      catch(Exception e1){}
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e){}
    succeeded();
  }

  /**
   *Verify valid usage AS400File.addPropertyChangeListener().
   *<ul compact>
   *<li>For one, two and three PropertyChangeListeners
   *<ul compact>
   *<li>Add the PropertyChangeListener(s)
   *<li>Invoke a function which fires a PropertyChangeEvent
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *</ul>
  **/
  public void Var002()
  {
    try
    {
      // Create our property change listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl3 = new DDMPropertyChangeListener();
      // Create our AS400File object
      AS400File f = new SequentialFile();

      // Verify one listener
      f.addPropertyChangeListener(rl1);
      // Cause a PropertyChangeEvent to be fired
      String path1 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF.FILE/NEWM.MBR";
      firePropertyChangeEvent(f, path1);
      if (!rl1.propertyChangeFired_)
      {
        failed("Property change not fired.");
        return;
      }
      if (!rl1.e_.getNewValue().equals(path1))
      {
        failed("New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl1.e_.getOldValue().equals(""))
      {
        failed("Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl1.e_.getPropertyName().equals("path"))
      {
        failed("Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
      // Reset listener
      rl1.reset();

      // Verify two listeners
      f.addPropertyChangeListener(rl2);
      // Cause a PropertyChangeEvent to be fired
      String path2 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF2.FILE/NEWM2.MBR";
      firePropertyChangeEvent(f, path2);
      if (!rl1.propertyChangeFired_)
      {
        failed("2-1: Property change not fired.");
        return;
      }
      if (!rl1.e_.getNewValue().equals(path2))
      {
        failed("2-1: New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl1.e_.getOldValue().equals(path1))
      {
        failed("2-1: Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl1.e_.getPropertyName().equals("path"))
      {
        failed("2-1: Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
      if (!rl2.propertyChangeFired_)
      {
        failed("2-2: Property change not fired.");
        return;
      }
      if (!rl2.e_.getNewValue().equals(path2))
      {
        failed("2-2: New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl2.e_.getOldValue().equals(path1))
      {
        failed("2-2: Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl2.e_.getPropertyName().equals("path"))
      {
        failed("2-2: Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners
      f.addPropertyChangeListener(rl3);
      // Cause a PropertyChangeEvent to be fired
      String path3 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF3.FILE/NEWM3.MBR";
      firePropertyChangeEvent(f, path3);
      if (!rl1.propertyChangeFired_)
      {
        failed("3-1: Property change not fired.");
        return;
      }
      if (!rl1.e_.getNewValue().equals(path3))
      {
        failed("3-1: New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl1.e_.getOldValue().equals(path2))
      {
        failed("3-1: Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl1.e_.getPropertyName().equals("path"))
      {
        failed("3-1: Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
      if (!rl2.propertyChangeFired_)
      {
        failed("3-2: Property change not fired.");
        return;
      }
      if (!rl2.e_.getNewValue().equals(path3))
      {
        failed("3-2: New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl2.e_.getOldValue().equals(path2))
      {
        failed("3-2: Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl2.e_.getPropertyName().equals("path"))
      {
        failed("3-2: Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
      if (!rl3.propertyChangeFired_)
      {
        failed("3-3: Property change not fired.");
        return;
      }
      if (!rl3.e_.getNewValue().equals(path3))
      {
        failed("3-3: New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl3.e_.getOldValue().equals(path2))
      {
        failed("3-3: Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl3.e_.getPropertyName().equals("path"))
      {
        failed("3-3: Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage AS400File.addVetoableChangeListener().
   *<ul compact>
   *<li>For one, two and three VetoableChangeListeners
   *<ul compact>
   *<li>Add the VetoableChangeListener(s)
   *<li>Invoke a function which fires a VetoableChangeEvent
   *<li>Verify that at least one listener can veto the event
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *<li>Vetoed property changes will be changed back to the original value
   *</ul>
  **/
  public void Var003()
  {
    try
    {
      // Create our vetoable change listeners
      DDMVetoableChangeListener rl1 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl2 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      // Create our AS400File object
      AS400File f = new SequentialFile();

      // Verify one listener, no veto
      f.addVetoableChangeListener(rl1);
      // Cause a VetoableChangeEvent to be fired
      String path1 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF.FILE/NEWM.MBR";
      rl1.reset();
      firePropertyChangeEvent(f, path1);
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals(path1) ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("path"))
      {
        failed("Single listener");
        return;
      }
      // Reset listener
      rl1.reset();

      // Verify two listeners, no veto
      f.addVetoableChangeListener(rl2);
      // Cause a VetoableChangeEvent to be fired
      String path2 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF2.FILE/NEWM2.MBR";
      firePropertyChangeEvent(f, path2);
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals(path2) ||
          !rl1.e_.getOldValue().equals(path1) ||
          !rl1.e_.getPropertyName().equals("path") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals(path2) ||
          !rl2.e_.getOldValue().equals(path1) ||
          !rl2.e_.getPropertyName().equals("path"))
      {
        failed("two listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners, no veto
      f.addVetoableChangeListener(rl3);
      // Cause a VetoableChangeEvent to be fired
      String path3 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF3.FILE/NEWM3.MBR";
      firePropertyChangeEvent(f, path3);
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals(path3) ||
          !rl1.e_.getOldValue().equals(path2) ||
          !rl1.e_.getPropertyName().equals("path") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals(path3) ||
          !rl2.e_.getOldValue().equals(path2) ||
          !rl2.e_.getPropertyName().equals("path") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals(path3) ||
          !rl3.e_.getOldValue().equals(path2) ||
          !rl3.e_.getPropertyName().equals("path"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      
      // Verify three listeners, with a veto from rl2
      rl2.vetoTheChange = true;
      
      // Cause a VetoableChangeEvent to be fired
      String path4 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF4.FILE/NEWM4.MBR";
      try
      {
        firePropertyChangeEvent(f, path4);
      }
      catch(PropertyVetoException p)
      {
	  boolean passed = true;
	  StringBuffer sb = new StringBuffer(); 
        // if one listener vetos, then the change doesn't go through
        // for ANY of the listeners.
        if (!rl1.vetoableChangeFired_)
        {
          sb.append("\n3-1: Vetoable change not fired.");
          passed=false;
        }
        if (rl1.vetoed_)
        {
          sb.append("\n3-1: Change was vetoed.");
          passed=false;
        }
        if (!rl1.e_.getNewValue().equals(path3)) // The bean support refires an event to the old value.
        {
          sb.append("\n3-1: New value not correct: "+rl1.e_.getNewValue());
          passed=false;
        }
        if (!rl1.e_.getOldValue().equals(path4)) // The bean support refires an event to the old value.
        {
          sb.append("\n3-1: Old value not correct: "+rl1.e_.getOldValue());
          passed=false;
        }
        if (!rl1.e_.getPropertyName().equals("path"))
        {
          sb.append("\n3-1: Property name not correct: "+rl1.e_.getPropertyName());
          passed=false;
        }
        if (rl2.vetoableChangeFired_)
        {
          sb.append("\n3-2: Change still occurred.");
          passed=false;
        }
        if (!rl2.vetoed_)
        {
          sb.append("\n3-2: Change was not vetoed.");
          passed=false;
        }
        if (rl2.e_ != null)
        {
          sb.append("\n3-2: Event not null: "+rl1);
          passed=false;
        }
	 if (isAtLeastJDK17) {
	     // When rl2 vetoes in V7R1, rl3 does not see it
	     if (rl3.vetoableChangeFired_)
	     {
		 sb.append("\n3-3: Vetoable change fired.");
		 passed=false;
	     }

	 } else { 
	     if (!rl3.vetoableChangeFired_)
	     {
		 sb.append("\n3-3: Vetoable change not fired.");
		 passed=false;
	     }
	     if (rl3.vetoed_)
	     {
		 sb.append("\n3-3: Change was vetoed.");
		 passed=false;
	     }
	     Object newValue = rl3.e_.getNewValue();
	     if ( newValue == null) newValue="null"; 
	     if (!newValue.equals(path3)) // The bean support refires an event to the old value.
	     {
		 sb.append("\n3-3: New value not correct: "+newValue);
		 passed=false;
	     }
	     Object oldValue = rl3.e_.getOldValue();
	     if (oldValue == null) oldValue="null"; 
	     if (!oldValue.equals(path4)) // The bean support refires an event to the old value.
	     {
		 sb.append("\n3-3: Old value not correct: "+oldValue);
		 passed=false;
	     }
	     Object propertyName = rl3.e_.getPropertyName();
	     if (propertyName == null) propertyName="null"; 
	     if (!propertyName.equals("path"))
	     {
		 sb.append("\n3-3: Property name not correct: "+propertyName); 
		 passed=false;
	     }
	 }
	assertCondition(passed, sb.toString());		
          return;

      }
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify invalid usage AS400File.addFileListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var004()
  {
    try
    {
      AS400File f = new SequentialFile();
      f.addFileListener(null);
      f.close(); 
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage AS400File.addPropertyChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var005()
  {
    try
    {
      AS400File f = new SequentialFile();
      f.addPropertyChangeListener(null);
      f.close(); 
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage AS400File.addVetoableChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var006()
  {
    try
    {
      AS400File f = new SequentialFile();
      f.addVetoableChangeListener(null);
      f.close(); 
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage AS400File.removeFileListener().
   *<ul compact>
   *<li>Add a several FileListeners
   *<li>Remove one FileListener
   *<li>Invoke a function which fires a FileEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var007()
  {
    AS400File f = null;
    try
    {
      // Create our file listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();
      DDMFileListener rl3 = new DDMFileListener();

      // Create our AS400File object
      f = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Verify three listeners
      f.addFileListener(rl1);
      f.addFileListener(rl2);
      f.addFileListener(rl3);

      // Cause a FileEvent to be fired
      fireFileOpenedEvent(f);
      if (!rl1.opened || !rl2.opened || !rl3.opened)
      {
        failed("three listeners");
        try
        {
          f.close();
        }
        catch(Exception e) {}
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      f.close();

      // Remove rl2 from the listener list
      f.removeFileListener(rl2);
      // Cause a FileEvent to be fired
      fireFileOpenedEvent(f);
      if (!rl1.opened || rl2.opened || !rl3.opened)
      {
        failed("after removing a listener");
        try
        {
          f.close();
        }
        catch(Exception e) {}
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1) {}
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e) {}
    succeeded();
  }

  /**
   *Verify valid usage AS400File.removePropertyChangeListener().
   *<ul compact>
   *<li>Add a several PropertyChangeListeners
   *<li>Remove one PropertyChangeListener
   *<li>Invoke a function which fires a PropertyChangeEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var008()
  {
    try
    {
      // Create our property change listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl3 = new DDMPropertyChangeListener();
      // Create our AS400File object
      AS400File f = new SequentialFile();

      // Verify three listeners
      f.addPropertyChangeListener(rl1);
      f.addPropertyChangeListener(rl2);
      f.addPropertyChangeListener(rl3);
      // Cause a PropertyChangeEvent to be fired
      String path1 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF.FILE/NEWM.MBR";
      firePropertyChangeEvent(f, path1);
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals(path1) ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("path") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getNewValue().equals(path1) ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getPropertyName().equals("path") ||
          !rl3.propertyChangeFired_ ||
          !rl3.e_.getNewValue().equals(path1) ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getPropertyName().equals("path"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Remove rl2 for list of listeners
      f.removePropertyChangeListener(rl2);
      // Cause a PropertyChangeEvent to be fired
      String path2 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF2.FILE/NEWM2.MBR";
      firePropertyChangeEvent(f, path2);
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals(path2) ||
          !rl1.e_.getOldValue().equals(path1) ||
          !rl1.e_.getPropertyName().equals("path") ||
          rl2.propertyChangeFired_ ||
          rl2.e_ != null ||
          !rl3.propertyChangeFired_ ||
          !rl3.e_.getNewValue().equals(path2) ||
          !rl3.e_.getOldValue().equals(path1) ||
          !rl3.e_.getPropertyName().equals("path"))
      {
        failed("three listeners with one removed");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage AS400File.removeVetoableChangeListener().
   *<ul compact>
   *<li>Add a several VetoableChangeListeners
   *<li>Remove one VetoableChangeListener
   *<li>Invoke a function which fires a VetoableChangeEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var009()
  {
    try
    {
      // Create our vetoable change listeners
      DDMVetoableChangeListener rl1 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl2 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      // Create our AS400File object
      AS400File f = new SequentialFile();

      // Verify three listeners, no veto
      f.addVetoableChangeListener(rl1);
      f.addVetoableChangeListener(rl2);
      f.addVetoableChangeListener(rl3);
      // Cause a VetoableChangeEvent to be fired
      String path1 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF.FILE/NEWM.MBR";
      firePropertyChangeEvent(f, path1);
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals(path1) ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("path") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals(path1) ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getPropertyName().equals("path") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals(path1) ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getPropertyName().equals("path"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Remove listener rl2
      f.removeVetoableChangeListener(rl2);
      // Cause a VetoableChangeEvent to be fired
      String path2 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF2.FILE/NEWM2.MBR";
      firePropertyChangeEvent(f, path2);
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals(path2) ||
          !rl1.e_.getOldValue().equals(path1) ||
          !rl1.e_.getPropertyName().equals("path") ||
          rl2.vetoableChangeFired_ ||
          rl2.e_ != null ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals(path2) ||
          !rl3.e_.getOldValue().equals(path1) ||
          !rl3.e_.getPropertyName().equals("path"))
      {
        failed("three listeners with veto");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage AS400File.removeFileListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var010()
  {
    try
    {
      AS400File f = new SequentialFile();
      f.removeFileListener(null);
      f.close(); 
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage AS400File.removePropertyChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var011()
  {
    try
    {
      AS400File f = new SequentialFile();
      f.removePropertyChangeListener(null);
      f.close(); 
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage AS400File.removeVetoableChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var012()
  {
    try
    {
      AS400File f = new SequentialFile();
      f.removeVetoableChangeListener(null);
      f.close(); 
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage AS400FileRecordDescription.addAS400FileRecordDescriptionListener().
   *<ul compact>
   *<li>For one, two and three AS400FileRecordDescriptionListeners
   *<ul compact>
   *<li>Add the AS400FileRecordDescriptionListener(s)
   *<li>Invoke a function which fires a AS400FileRecordDescriptionEvent
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *</ul>
  **/
  public void Var013()
  {
    try
    {
      // Create our file listeners
      DDMAS400FileRecordDescriptionListener rl1 = new DDMAS400FileRecordDescriptionListener();
      DDMAS400FileRecordDescriptionListener rl2 = new DDMAS400FileRecordDescriptionListener();
      DDMAS400FileRecordDescriptionListener rl3 = new DDMAS400FileRecordDescriptionListener();
      // Create our AS400FileRecordDescription object
      AS400FileRecordDescription f = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");
      // Verify one listener
      f.addAS400FileRecordDescriptionListener(rl1);
      // Cause a AS400FileRecordDescriptionEvent to be fired
      fireRecordFormatRetrievedEvent(f);
      if (!rl1.retrieved)
      {
        failed("Single listener");
        return;
      }
      // Reset listener
      rl1.reset();

      // Verify two listeners
      f.addAS400FileRecordDescriptionListener(rl2);
      // Cause a AS400FileRecordDescriptionEvent to be fired
      fireRecordFormatRetrievedEvent(f);
      if (!rl1.retrieved || !rl2.retrieved)
      {
        failed("two listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners
      f.addAS400FileRecordDescriptionListener(rl3);
      // Cause a AS400FileRecordDescriptionEvent to be fired
      fireRecordFormatRetrievedEvent(f);
      if (!rl1.retrieved || !rl2.retrieved || !rl3.retrieved)
      {
        failed("three listeners");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage AS400FileRecordDescription.addPropertyChangeListener().
   *<ul compact>
   *<li>For one, two and three PropertyChangeListeners
   *<ul compact>
   *<li>Add the PropertyChangeListener(s)
   *<li>Invoke a function which fires a PropertyChangeEvent
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *</ul>
  **/
  public void Var014()
  {
    try
    {
      // Create our property change listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl3 = new DDMPropertyChangeListener();
      // Create our AS400FileRecordDescription object
      AS400FileRecordDescription f = new AS400FileRecordDescription();

      // Verify one listener
      f.addPropertyChangeListener(rl1);
      // Cause a PropertyChangeEvent to be fired
      String path1 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF.FILE/NEWM.MBR";
      firePropertyChangeEvent(f, path1);
      if (!rl1.propertyChangeFired_)
      {
        failed("Property change not fired.");
        return;
      }
      if (!rl1.e_.getNewValue().equals(path1))
      {
        failed("New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl1.e_.getOldValue().equals(""))
      {
        failed("Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl1.e_.getPropertyName().equals("path"))
      {
        failed("Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
      // Reset listener
      rl1.reset();;

      // Verify two listeners
      f.addPropertyChangeListener(rl2);
      // Cause a PropertyChangeEvent to be fired
      String path2 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF2.FILE/NEWM2.MBR";
      firePropertyChangeEvent(f, path2);
      if (!rl1.propertyChangeFired_)
      {
        failed("2-1: Property change not fired.");
        return;
      }
      if (!rl1.e_.getNewValue().equals(path2))
      {
        failed("2-1: New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl1.e_.getOldValue().equals(path1))
      {
        failed("2-1: Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl1.e_.getPropertyName().equals("path"))
      {
        failed("2-1: Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
      if (!rl2.propertyChangeFired_)
      {
        failed("2-2: Property change not fired.");
        return;
      }
      if (!rl2.e_.getNewValue().equals(path2))
      {
        failed("2-2: New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl2.e_.getOldValue().equals(path1))
      {
        failed("2-2: Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl2.e_.getPropertyName().equals("path"))
      {
        failed("2-2: Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners
      f.addPropertyChangeListener(rl3);
      // Cause a PropertyChangeEvent to be fired
      String path3 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF3.FILE/NEWM3.MBR";
      firePropertyChangeEvent(f, path3);
      if (!rl1.propertyChangeFired_)
      {
        failed("3-1: Property change not fired.");
        return;
      }
      if (!rl1.e_.getNewValue().equals(path3))
      {
        failed("3-1: New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl1.e_.getOldValue().equals(path2))
      {
        failed("3-1: Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl1.e_.getPropertyName().equals("path"))
      {
        failed("3-1: Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
      if (!rl2.propertyChangeFired_)
      {
        failed("3-2: Property change not fired.");
        return;
      }
      if (!rl2.e_.getNewValue().equals(path3))
      {
        failed("3-2: New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl2.e_.getOldValue().equals(path2))
      {
        failed("3-2: Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl2.e_.getPropertyName().equals("path"))
      {
        failed("3-2: Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
      if (!rl3.propertyChangeFired_)
      {
        failed("3-3: Property change not fired.");
        return;
      }
      if (!rl3.e_.getNewValue().equals(path3))
      {
        failed("3-3: New value not correct: "+rl1.e_.getNewValue());
        return;
      }
      if (!rl3.e_.getOldValue().equals(path2))
      {
        failed("3-3: Old value not correct: "+rl1.e_.getOldValue());
        return;
      }
      if (!rl3.e_.getPropertyName().equals("path"))
      {
        failed("3-3: Property name not correct: "+rl1.e_.getPropertyName());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage AS400FileRecordDescription.addVetoableChangeListener().
   *<ul compact>
   *<li>For one, two and three VetoableChangeListeners
   *<ul compact>
   *<li>Add the VetoableChangeListener(s)
   *<li>Invoke a function which fires a VetoableChangeEvent
   *<li>Verify that at least one listener can veto the event
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *<li>Vetoed property changes will be changed back to the original value
   *</ul>
  **/
  public void Var015()
  {
    try
    {
      // Create our vetoable change listeners
      DDMVetoableChangeListener rl1 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl2 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      // Create our AS400FileRecordDescription object
      AS400FileRecordDescription f = new AS400FileRecordDescription();

      // Verify one listener, no veto
      f.addVetoableChangeListener(rl1);
      // Cause a VetoableChangeEvent to be fired
      String path1 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF.FILE/NEWM.MBR";
      firePropertyChangeEvent(f, path1);
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals(path1) ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("path"))
      {
        failed("Single listener");
        return;
      }
      // Reset listener
      rl1.reset();

      // Verify two listeners, no veto
      f.addVetoableChangeListener(rl2);
      // Cause a VetoableChangeEvent to be fired
      String path2 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF2.FILE/NEWM2.MBR";
      firePropertyChangeEvent(f, path2);

      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals(path2) ||
          !rl1.e_.getOldValue().equals(path1) ||
          !rl1.e_.getPropertyName().equals("path") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals(path2) ||
          !rl2.e_.getOldValue().equals(path1) ||
          !rl2.e_.getPropertyName().equals("path"))
      {
        failed("two listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners, no veto
      f.addVetoableChangeListener(rl3);
      // Cause a VetoableChangeEvent to be fired
      String path3 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF3.FILE/NEWM3.MBR";
      firePropertyChangeEvent(f, path3);
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals(path3) ||
          !rl1.e_.getOldValue().equals(path2) ||
          !rl1.e_.getPropertyName().equals("path") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals(path3) ||
          !rl2.e_.getOldValue().equals(path2) ||
          !rl2.e_.getPropertyName().equals("path") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals(path3) ||
          !rl3.e_.getOldValue().equals(path2) ||
          !rl3.e_.getPropertyName().equals("path"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Verify three listeners, with a veto from rl2
      rl2.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      String path4 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF4.FILE/NEWM4.MBR";
      try
      {
        firePropertyChangeEvent(f, path4);
      }
      catch(PropertyVetoException p)
      {
        // if one listener vetos, then the change doesn't go through
        // for ANY of the listeners.
	boolean passed = true;
	StringBuffer sb = new StringBuffer();

        if (!rl1.vetoableChangeFired_)
        {
          sb.append("\n15-1: Vetoable change not fired.");
          passed=false;
        }
        if (rl1.vetoed_)
        {
          sb.append("\n15-1: Change was vetoed.");
          passed=false;
        }
        if (!rl1.e_.getNewValue().equals(path3)) // The bean support refires an event to the old value.
        {
          sb.append("\n15-1: New value not correct: "+rl1.e_.getNewValue());
          passed=false;
        }
        if (!rl1.e_.getOldValue().equals(path4)) // The bean support refires an event to the old value.
        {
          sb.append("\n15-1: Old value not correct: "+rl1.e_.getOldValue());
          passed=false;
        }
        if (!rl1.e_.getPropertyName().equals("path"))
        {
          sb.append("\n15-1: Property name not correct: "+rl1.e_.getPropertyName());
          passed=false;
        }
        if (rl2.vetoableChangeFired_)
        {
          sb.append("\n15-2: Change still occurred.");
          passed=false;
        }
        if (!rl2.vetoed_)
        {
          sb.append("\n15-2: Change was not vetoed.");
          passed=false;
        }
        if (rl2.e_ != null)
        {
          sb.append("\n15-2: Event not null: "+rl1);
          passed=false;
        }
	 if (isAtLeastJDK17) {
	     // When rl2 vetoes in V7R1, rl3 does not see it
	     if (rl3.vetoableChangeFired_)
	     {
		 sb.append("\n15-3: Vetoable change fired.");
		 passed=false;
	     }

	 } else { 

	     if (!rl3.vetoableChangeFired_)
	     {
		 sb.append("\n15-3: Vetoable change not fired.");
		 passed=false;
	     }
	     if (rl3.vetoed_)
	     {
		 sb.append("\n15-3: Change was vetoed.");
		 passed=false;
	     }
	     if (!rl3.e_.getNewValue().equals(path3)) // The bean support refires an event to the old value.
	     {
		 sb.append("\n15-3: New value not correct: "+rl1.e_.getNewValue());
		 passed=false;
	     }
	     if (!rl3.e_.getOldValue().equals(path4)) // The bean support refires an event to the old value.
	     {
		 sb.append("\n15-3: Old value not correct: "+rl1.e_.getOldValue());
		 passed=false;
	     }
	     if (!rl3.e_.getPropertyName().equals("path"))
	     {
		 sb.append("\n15-3: Property name not correct: "+rl1.e_.getPropertyName());
		 passed=false;
	     }
	 }
	assertCondition(passed, sb.toString()); 
	  return;

      }
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify invalid usage AS400FileRecordDescription.addAS400FileRecordDescriptionListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var016()
  {
    try
    {
      AS400FileRecordDescription f = new AS400FileRecordDescription();
      f.addAS400FileRecordDescriptionListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage AS400FileRecordDescription.addPropertyChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var017()
  {
    try
    {
      AS400FileRecordDescription f = new AS400FileRecordDescription();
      f.addPropertyChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage AS400FileRecordDescription.addVetoableChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var018()
  {
    try
    {
      AS400FileRecordDescription f = new AS400FileRecordDescription();
      f.addVetoableChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage AS400FileRecordDescription.removeAS400FileRecordDescriptionListener().
   *<ul compact>
   *<li>Add a several AS400FileRecordDescriptionListeners
   *<li>Remove one AS400FileRecordDescriptionListener
   *<li>Invoke a function which fires a AS400FileRecordDescriptionEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var019()
  {
    try
    {
      // Create our file listeners
      DDMAS400FileRecordDescriptionListener rl1 = new DDMAS400FileRecordDescriptionListener();
      DDMAS400FileRecordDescriptionListener rl2 = new DDMAS400FileRecordDescriptionListener();
      DDMAS400FileRecordDescriptionListener rl3 = new DDMAS400FileRecordDescriptionListener();

      // Create our AS400FileRecordDescription object
      AS400FileRecordDescription f = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Verify three listeners
      f.addAS400FileRecordDescriptionListener(rl1);
      f.addAS400FileRecordDescriptionListener(rl2);
      f.addAS400FileRecordDescriptionListener(rl3);

      // Cause a AS400FileRecordDescriptionEvent to be fired
      fireRecordFormatRetrievedEvent(f);
      if (!rl1.retrieved || !rl2.retrieved || !rl3.retrieved)
      {
        failed("three listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();

      // Remove rl2 from the listener list
      f.removeAS400FileRecordDescriptionListener(rl2);
      // Cause a AS400FileRecordDescriptionEvent to be fired
      fireRecordFormatRetrievedEvent(f);
      if (!rl1.retrieved || rl2.retrieved || !rl3.retrieved)
      {
        failed("after removing a listener");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage AS400FileRecordDescription.removePropertyChangeListener().
   *<ul compact>
   *<li>Add a several PropertyChangeListeners
   *<li>Remove one PropertyChangeListener
   *<li>Invoke a function which fires a PropertyChangeEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var020()
  {
    try
    {
      // Create our property change listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl3 = new DDMPropertyChangeListener();
      // Create our AS400FileRecordDescription object
      AS400FileRecordDescription f = new AS400FileRecordDescription();

      // Verify three listeners
      f.addPropertyChangeListener(rl1);
      f.addPropertyChangeListener(rl2);
      f.addPropertyChangeListener(rl3);
      // Cause a PropertyChangeEvent to be fired
      String path1 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF.FILE/NEWM.MBR";
      firePropertyChangeEvent(f, path1);
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals(path1) ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("path") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getNewValue().equals(path1) ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getPropertyName().equals("path") ||
          !rl3.propertyChangeFired_ ||
          !rl3.e_.getNewValue().equals(path1) ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getPropertyName().equals("path"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Remove rl2 for list of listeners
      f.removePropertyChangeListener(rl2);
      // Cause a PropertyChangeEvent to be fired
      String path2 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF2.FILE/NEWM2.MBR";
      firePropertyChangeEvent(f, path2);
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals(path2) ||
          !rl1.e_.getOldValue().equals(path1) ||
          !rl1.e_.getPropertyName().equals("path") ||
          rl2.propertyChangeFired_ ||
          rl2.e_ != null ||
          !rl3.propertyChangeFired_ ||
          !rl3.e_.getNewValue().equals(path2) ||
          !rl3.e_.getOldValue().equals(path1) ||
          !rl3.e_.getPropertyName().equals("path"))
      {
        failed("three listeners with one removed");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage AS400FileRecordDescription.removeVetoableChangeListener().
   *<ul compact>
   *<li>Add a several VetoableChangeListeners
   *<li>Remove one VetoableChangeListener
   *<li>Invoke a function which fires a VetoableChangeEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var021()
  {
    try
    {
      // Create our vetoable change listeners
      DDMVetoableChangeListener rl1 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl2 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      // Create our AS400FileRecordDescription object
      AS400FileRecordDescription f = new AS400FileRecordDescription();

      // Verify three listeners, no veto
      f.addVetoableChangeListener(rl1);
      f.addVetoableChangeListener(rl2);
      f.addVetoableChangeListener(rl3);
      // Cause a VetoableChangeEvent to be fired
      String path1 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF.FILE/NEWM.MBR";
      firePropertyChangeEvent(f, path1);
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals(path1) ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("path") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals(path1) ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getPropertyName().equals("path") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals(path1) ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getPropertyName().equals("path"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Remove listener rl2
      f.removeVetoableChangeListener(rl2);
      // Cause a VetoableChangeEvent to be fired
      String path2 = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF2.FILE/NEWM2.MBR";
      firePropertyChangeEvent(f, path2);
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals(path2) ||
          !rl1.e_.getOldValue().equals(path1) ||
          !rl1.e_.getPropertyName().equals("path") ||
          rl2.vetoableChangeFired_ ||
          rl2.e_ != null ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals(path2) ||
          !rl3.e_.getOldValue().equals(path1) ||
          !rl3.e_.getPropertyName().equals("path"))
      {
        failed("three listeners with veto");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage AS400FileRecordDescription.removeAS400FileRecordDescriptionListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var022()
  {
    try
    {
      AS400FileRecordDescription f = new AS400FileRecordDescription();
      f.removeAS400FileRecordDescriptionListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage AS400FileRecordDescription.removePropertyChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var023()
  {
    try
    {
      AS400FileRecordDescription f = new AS400FileRecordDescription();
      f.removePropertyChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage AS400FileRecordDescription.removeVetoableChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var024()
  {
    try
    {
      AS400FileRecordDescription f = new AS400FileRecordDescription();
      f.removeVetoableChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify that AS400File.setPath() fires a
   *a VetoableChangeEvent and a PropertyChangeEvent invoking
   *the vetoable/propertyChange method on the Vetoable/PropertyChangeListener object.
   *<ul compact>
   *<li>Add listeners and then set the name of the
   *the record format being listened to.
   *Verify ability of listener to veto the change.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The propertyChange() method will be invoked on the
   *the property change listener objects.
   *<li>The vetoableChange() method will be invoked on the
   *the vetoable change listener objects.
   *</ul>
  **/
  public void Var025()
  {
    try
    {
      // Create the listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl4 = new DDMVetoableChangeListener();

      // Create the AS400File object
      AS400File f = new SequentialFile();

      // Add listeners
      f.addPropertyChangeListener(rl1);
      f.addPropertyChangeListener(rl2);
      f.addVetoableChangeListener(rl3);
      f.addVetoableChangeListener(rl4);

      // Set the path
      String path = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF.FILE/NEWM.MBR";
      f.setPath(path);

      // Verify listeners notified of the event
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getNewValue().equals(path) ||
          !rl1.e_.getPropertyName().equals("path") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getNewValue().equals(path) ||
          !rl2.e_.getPropertyName().equals("path") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getNewValue().equals(path) ||
          !rl3.e_.getPropertyName().equals("path") ||
          !rl4.vetoableChangeFired_ ||
          !rl4.e_.getOldValue().equals("") ||
          !rl4.e_.getNewValue().equals(path) ||
          !rl4.e_.getPropertyName().equals("path"))
      {
        failed("Error in notification of property change and vetoable change");
        f.close(); 
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      rl4.reset();
      // Verify listeners, with a veto from rl4
      rl4.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      String pathVetoed = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF4.FILE/NEWM4.MBR";
      try
      {
        f.setPath(pathVetoed);
      }
      catch(PropertyVetoException p)
      {
        if (!rl3.vetoableChangeFired_ ||
            !rl3.e_.getNewValue().equals(path) ||
            !rl3.e_.getOldValue().equals(pathVetoed) ||
            !rl3.e_.getPropertyName().equals("path") ||
            !rl4.vetoed_ ||
            rl4.e_ != null ||
            rl1.propertyChangeFired_ ||
            rl2.propertyChangeFired_)
        {
          failed("listeners with veto");
          f.close(); 
          return;
        }
        else
        {
          succeeded();
          f.close(); 
          return;
        }
      }
      f.close(); 

      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify that AS400File.setRecordFormat() fires a
   *a VetoableChangeEvent and a PropertyChangeEvent invoking
   *the vetoable/propertyChange method on the Vetoable/PropertyChangeListener object.
   *<ul compact>
   *<li>Add listeners and then set the name of the
   *the record format being listened to.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The propertyChange() method will be invoked on the
   *the property change listener objects.
   *<li>The vetoableChange() method will be invoked on the
   *the vetoable change listener objects.
   *</ul>
  **/
  public void Var026()
  {
    try
    {
      // Create the listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl4 = new DDMVetoableChangeListener();

      // Create the AS400File object
      AS400File f = new SequentialFile();

      // Add listeners
      f.addPropertyChangeListener(rl1);
      f.addPropertyChangeListener(rl2);
      f.addVetoableChangeListener(rl3);
      f.addVetoableChangeListener(rl4);

      // Set the record format
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "F"));
      f.setRecordFormat(rf);

      // Verify listeners notified of the event
      if (!rl1.propertyChangeFired_ ||
          rl1.e_.getOldValue() != null ||
          rl1.e_.getNewValue() != rf ||
          !rl1.e_.getPropertyName().equals("recordFormat") ||
          !rl2.propertyChangeFired_ ||
          rl2.e_.getOldValue() != null ||
          rl2.e_.getNewValue() != rf ||
          !rl2.e_.getPropertyName().equals("recordFormat") ||
          !rl3.vetoableChangeFired_ ||
          rl3.e_.getOldValue() != null ||
          rl3.e_.getNewValue() != rf ||
          !rl3.e_.getPropertyName().equals("recordFormat") ||
          !rl4.vetoableChangeFired_ ||
          rl4.e_.getOldValue() != null ||
          rl4.e_.getNewValue() != rf ||
          !rl4.e_.getPropertyName().equals("recordFormat"))
      {
        failed("Error in notification of property change and vetoable change");
        f.close(); 
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      rl4.reset();
      // Verify listeners, with a veto from rl4
      rl4.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "F"));
      try
      {
        f.setRecordFormat(rf2);
      }
      catch(PropertyVetoException p)
      {
        if (!rl3.vetoableChangeFired_ ||
            rl3.e_.getNewValue() != rf ||
            rl3.e_.getOldValue() != rf2 ||
            !rl3.e_.getPropertyName().equals("recordFormat") ||
            !rl4.vetoed_ ||
            rl4.e_ != null ||
            rl1.propertyChangeFired_ ||
            rl2.propertyChangeFired_)
        {
          failed("listeners with veto");
          f.close(); 
          return;
        }
        else
        {
          succeeded();
          f.close(); 
          return;
        }
      }
      failed("No propertyVetoException");
      f.close(); 
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify that AS400File.setSystem() fires a
   *a VetoableChangeEvent and a PropertyChangeEvent invoking
   *the vetoable/propertyChange method on the Vetoable/PropertyChangeListener object.
   *<ul compact>
   *<li>Add listeners and then set the name of the
   *the record format being listened to.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The propertyChange() method will be invoked on the
   *the property change listener objects.
   *<li>The vetoableChange() method will be invoked on the
   *the vetoable change listener objects.
   *</ul>
  **/
  public void Var027()
  {
    try
    {
      // Create the listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl4 = new DDMVetoableChangeListener();

      // Create the AS400File object
      AS400File f = new SequentialFile();

      // Add listeners
      f.addPropertyChangeListener(rl1);
      f.addPropertyChangeListener(rl2);
      f.addVetoableChangeListener(rl3);
      f.addVetoableChangeListener(rl4);

      // Set the system
      f.setSystem(systemObject_);

      // Verify listeners notified of the event
      if (!rl1.propertyChangeFired_ ||
          rl1.e_.getOldValue() != null ||
          rl1.e_.getNewValue() != systemObject_ ||
          !rl1.e_.getPropertyName().equals("system") ||
          !rl2.propertyChangeFired_ ||
          rl2.e_.getOldValue() != null ||
          rl2.e_.getNewValue() != systemObject_ ||
          !rl2.e_.getPropertyName().equals("system") ||
          !rl3.vetoableChangeFired_ ||
          rl3.e_.getOldValue() != null ||
          rl3.e_.getNewValue() != systemObject_ ||
          !rl3.e_.getPropertyName().equals("system") ||
          !rl4.vetoableChangeFired_ ||
          rl4.e_.getOldValue() != null ||
          rl4.e_.getNewValue() != systemObject_ ||
          !rl4.e_.getPropertyName().equals("system"))
      {
        failed("Error in notification of property change and vetoable change");
        f.close(); 
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      rl4.reset();
      // Verify listeners, with a veto from rl4
      rl4.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      AS400 sys2 = new AS400();
      try
      {
        f.setSystem(sys2);
      }
      catch(PropertyVetoException p)
      {
        if (!rl3.vetoableChangeFired_ ||
            rl3.e_.getNewValue() != systemObject_ ||
            rl3.e_.getOldValue() != sys2 ||
            !rl3.e_.getPropertyName().equals("system") ||
            !rl4.vetoed_ ||
            rl4.e_ != null ||
            rl1.propertyChangeFired_ ||
            rl2.propertyChangeFired_)
        {
          failed("listeners with veto");
          f.close(); 
          return;
        }
        else
        {
          succeeded();
          f.close(); 
          return;
        }
      }
      f.close(); 
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify that AS400FileRecordDescription.setPath() fires a
   *a VetoableChangeEvent and a PropertyChangeEvent invoking
   *the vetoable/propertyChange method on the Vetoable/PropertyChangeListener object.
   *<ul compact>
   *<li>Add listeners and then set the name of the
   *the record format being listened to.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The propertyChange() method will be invoked on the
   *the property change listener objects.
   *<li>The vetoableChange() method will be invoked on the
   *the vetoable change listener objects.
   *</ul>
  **/
  public void Var028()
  {
    try
    {
      // Create the listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl4 = new DDMVetoableChangeListener();

      // Create the AS400FileRecordDescription object
      AS400FileRecordDescription f = new AS400FileRecordDescription();

      // Add listeners
      f.addPropertyChangeListener(rl1);
      f.addPropertyChangeListener(rl2);
      f.addVetoableChangeListener(rl3);
      f.addVetoableChangeListener(rl4);

      // Set the path
      String path = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF.FILE/NEWM.MBR";
      f.setPath(path);

      // Verify listeners notified of the event
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getNewValue().equals(path) ||
          !rl1.e_.getPropertyName().equals("path") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getNewValue().equals(path) ||
          !rl2.e_.getPropertyName().equals("path") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getNewValue().equals(path) ||
          !rl3.e_.getPropertyName().equals("path") ||
          !rl4.vetoableChangeFired_ ||
          !rl4.e_.getOldValue().equals("") ||
          !rl4.e_.getNewValue().equals(path) ||
          !rl4.e_.getPropertyName().equals("path"))
      {
        failed("Error in notification of property change and vetoable change");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      rl4.reset();
      // Verify listeners, with a veto from rl4
      rl4.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      String pathVetoed = "/QSYS.LIB/" + testLib_ + ".LIB/NEWF4.FILE/NEWM4.MBR";
      try
      {
        f.setPath(pathVetoed);
      }
      catch(PropertyVetoException p)
      {
        if (!rl3.vetoableChangeFired_ ||
            !rl3.e_.getNewValue().equals(path) ||
            !rl3.e_.getOldValue().equals(pathVetoed) ||
            !rl3.e_.getPropertyName().equals("path") ||
            !rl4.vetoed_ ||
            rl4.e_ != null ||
            rl1.propertyChangeFired_ ||
            rl2.propertyChangeFired_)
        {
          failed("listeners with veto");
          return;
        }
        else
        {
          succeeded();
          return;
        }
      }
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify that AS400FileRecordDescription.setSystem() fires a
   *a VetoableChangeEvent and a PropertyChangeEvent invoking
   *the vetoable/propertyChange method on the Vetoable/PropertyChangeListener object.
   *<ul compact>
   *<li>Add listeners and then set the name of the
   *the record format being listened to.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The propertyChange() method will be invoked on the
   *the property change listener objects.
   *<li>The vetoableChange() method will be invoked on the
   *the vetoable change listener objects.
   *</ul>
  **/
  public void Var029()
  {
    try
    {
      // Create the listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl4 = new DDMVetoableChangeListener();

      // Create the AS400FileRecordDescription object
      AS400FileRecordDescription f = new AS400FileRecordDescription();

      // Add listeners
      f.addPropertyChangeListener(rl1);
      f.addPropertyChangeListener(rl2);
      f.addVetoableChangeListener(rl3);
      f.addVetoableChangeListener(rl4);

      // Set the system
      f.setSystem(systemObject_);

      // Verify listeners notified of the event
      if (!rl1.propertyChangeFired_ ||
          rl1.e_.getOldValue() != null ||
          rl1.e_.getNewValue() != systemObject_ ||
          !rl1.e_.getPropertyName().equals("system") ||
          !rl2.propertyChangeFired_ ||
          rl2.e_.getOldValue() != null ||
          rl2.e_.getNewValue() != systemObject_ ||
          !rl2.e_.getPropertyName().equals("system") ||
          !rl3.vetoableChangeFired_ ||
          rl3.e_.getOldValue() != null ||
          rl3.e_.getNewValue() != systemObject_ ||
          !rl3.e_.getPropertyName().equals("system") ||
          !rl4.vetoableChangeFired_ ||
          rl4.e_.getOldValue() != null ||
          rl4.e_.getNewValue() != systemObject_ ||
          !rl4.e_.getPropertyName().equals("system"))
      {
        failed("Error in notification of property change and vetoable change");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      rl4.reset();
      // Verify listeners, with a veto from rl4
      rl4.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      AS400 sys2 = new AS400();
      try
      {
        f.setSystem(sys2);
      }
      catch(PropertyVetoException p)
      {
        if (!rl3.vetoableChangeFired_ ||
            rl3.e_.getNewValue() != systemObject_ ||
            rl3.e_.getOldValue() != sys2 ||
            !rl3.e_.getPropertyName().equals("system") ||
            !rl4.vetoed_ ||
            rl4.e_ != null ||
            rl1.propertyChangeFired_ ||
            rl2.propertyChangeFired_)
        {
          failed("listeners with veto");
          return;
        }
        else
        {
          succeeded();
          return;
        }
      }
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify that AS400File.close() fires a
   *a FileEvent invoking the fileClosed()
   *method on the FileListener object.
   *<ul compact>
   *<li>Open the file.
   *<li>Add listeners and then close the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileClosed() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var030()
  {
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object, open the file and add the listeners
      AS400File r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");
      r.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      r.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Close the file
      r.close();

      // Verify listeners notified of the event and only that event
      if (!rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          rl1.modified ||
          rl1.opened ||
          !rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileClosed");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify that AS400File.create() fires a
   *a FileEvent invoking the fileCreated()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners and then create the file using
   *create(int, String, String).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileCreated() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var031()
  {
    AS400File r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object, add the listeners
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILEC.FILE/%FILE%.MBR");
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Create the file
      r.create(10, "*DATA", null);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          !rl1.created ||
          rl1.deleted ||
          rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          !rl2.created ||
          rl2.deleted ||
          rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileCreated");
        r.close(); 
        r.delete();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        r.close(); 
        r.delete();
      } catch (Exception e1) {}
      return;
    }
    try
    {
      r.close(); 
      r.delete();
    } catch (Exception e1) {}
    succeeded();
  }

  /**
   *Verify that AS400File.create() fires a
   *a FileEvent invoking the fileCreated()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners and then create the file using
   *create(String, String).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileCreated() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var032()
  {
    AS400File r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object, add the listeners
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILEC.FILE/%FILE%.MBR");
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Create the file
      r.create("/QSYS.LIB/DDMTESTSAV.LIB/QDDSSRC.FILE/ALLFLDS.MBR", null);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          !rl1.created ||
          rl1.deleted ||
          rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          !rl2.created ||
          rl2.deleted ||
          rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileCreated");
        r.close(); 
        r.delete();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        r.close(); 
        r.delete();
      } catch (Exception e1) {}
      return;
    }
    try
    {
      r.close(); 
      r.delete();
    } catch (Exception e1) {}
    succeeded();
  }

  /**
   *Verify that AS400File.create() fires a
   *a FileEvent invoking the fileCreated()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners and then create the file using
   *create(RecordFormat, String).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileCreated() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var033()
  {
    AS400File r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object, add the listeners
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILEC.FILE/%FILE%.MBR");
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Create the file
      r.create(new DDMChar10NoKeyFormat(systemObject_), null);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          !rl1.created ||
          rl1.deleted ||
          rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          !rl2.created ||
          rl2.deleted ||
          rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileCreated");
        r.close(); 
        r.delete();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        r.close(); 
        r.delete();
      } catch (Exception e1) {}
      return;
    }
    try
    {
      r.close(); 
      r.delete();
    } catch (Exception e1) {}
    succeeded();
  }

  /**
   *Verify that AS400File.create() fires a
   *a FileEvent invoking the fileCreated()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners and then create the file using
   *create(RecordFormat, String, String, String, String, String, boolean, String, String).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileCreated() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var034()
  {
    AS400File r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object, add the listeners
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILEC.FILE/%FILE%.MBR");
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Create the file
      r.create(new DDMChar10NoKeyFormat(systemObject_), null, null, null, null, null,
               false, null, null);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          !rl1.created ||
          rl1.deleted ||
          rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          !rl2.created ||
          rl2.deleted ||
          rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileCreated");
        r.close(); 
        r.delete();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        r.close(); 
        r.delete();
      } catch (Exception e1) {}
      return;
    }
    try
    {
      r.close(); 
      r.delete();
    } catch (Exception e1) {}
    succeeded();
  }

  /**
   *Verify that AS400File.delete() fires a
   *a FileEvent invoking the fileDeleted()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners and then delete the file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileDeleted() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var035()
  {
    AS400File r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILEC.FILE/%FILE%.MBR");

      // Create the file
      r.create("/QSYS.LIB/DDMTESTSAV.LIB/QDDSSRC.FILE/ALLFLDS.MBR", null);

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Delete the file
      r.delete();

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          !rl1.deleted ||
          rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          rl2.created ||
          !rl2.deleted ||
          rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileDeleted");
        r.close(); 
        return;
      }
      r.close(); 

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        r.close(); 
        r.delete();
      } catch (Exception e1) {}
      return;
    }
    succeeded();
  }

  /**
   *Verify that AS400File.deleteCurrentRecord() fires a
   *a FileEvent invoking the fileModified()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, position cursor and delete the current record
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var036()
  {
    AS400File r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Open the file and position the cursor
      r.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      r.readLast();

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Delete the current record
      r.deleteCurrentRecord();

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          !rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          !rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileModified");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that AS400File.update() fires a
   *a FileEvent invoking the fileModified()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, position cursor and update the current record
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var037()
  {
    AS400File r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Open the file and position the cursor
      r.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = r.readLast();

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Update
      r.update(rec);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          !rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          !rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileModified");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that AS400File.write(Record) fires a
   *a FileEvent invoking the fileModified()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, write a record
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var038()
  {
    AS400File r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Open the file and position the cursor
      r.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = r.readLast();

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Write
      r.write(rec);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          !rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          !rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileModified");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that AS400File.write(Record[]) fires a
   *a FileEvent invoking the fileModified()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, write records
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var039()
  {
    AS400File r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Open the file and position the cursor
      r.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[2];
      recs[0] = r.readFirst();
      recs[1] = r.readNext();

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Write
      r.write(recs);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          !rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          !rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileModified");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that KeyedFile.deleteRecord() fires a
   *a FileEvent invoking the fileModified()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, delete record by key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var040()
  {
    KeyedFile r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE2.FILE");

      // Open the file
      r.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Delete a record
      Object[] key = new Object[1];
      key[0] = "RECORD001 ";
      r.deleteRecord(key);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          !rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          !rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileModified");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that SequentialFile.deleteRecord() fires a
   *a FileEvent invoking the fileModified()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, delete record by record number
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var041()
  {
    SequentialFile r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Open the file
      r.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Delete a record
      r.deleteRecord(1);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          !rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          !rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileModified");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that KeyedFile.open() fires a
   *a FileEvent invoking the fileOpened()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, open the file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileOpened() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var042()
  {
    KeyedFile r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Open the file
      r.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          rl1.modified ||
          !rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          rl2.modified ||
          !rl2.opened)
      {
        failed("Error in notification of fileOpened");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that SequentialFile.open() fires a
   *a FileEvent invoking the fileOpened()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, open the file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileOpened() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var043()
  {
    SequentialFile r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Open the file
      r.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          rl1.modified ||
          !rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          rl2.modified ||
          !rl2.opened)
      {
        failed("Error in notification of fileOpened");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that KeyedFile.update(Object[], Record) fires a
   *a FileEvent invoking the fileModified()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, update the record by key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var044()
  {
    KeyedFile r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE2.FILE");

      // Open the file and position the cursor
      r.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = r.readLast();

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Update
      Object[] key = new Object[1];
      key[0] = "RECORD003 ";
      r.update(key, rec);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          !rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          !rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileModified");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that KeyedFile.update(Object[], Record, int) fires a
   *a FileEvent invoking the fileModified()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, update the record by key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var045()
  {
    KeyedFile r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE2.FILE");

      // Open the file and position the cursor
      r.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = r.readLast();

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Update
      Object[] key = new Object[1];
      key[0] = "RECORD004 ";
      r.update(key, rec, KeyedFile.KEY_LE);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          !rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          !rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileModified");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that SequentialFile.update(int, Record) fires a
   *a FileEvent invoking the fileModified()
   *method on the FileListener object.
   *<ul compact>
   *<li>Add listeners, update the record by record number
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fileModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var046()
  {
    SequentialFile r = null;
    try
    {
      // Create the listeners
      DDMFileListener rl1 = new DDMFileListener();
      DDMFileListener rl2 = new DDMFileListener();

      // Create the AS400File object
      r = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Open the file and position the cursor
      r.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      r.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = r.readLast();

      // Add listeners
      r.addFileListener(rl1);
      r.addFileListener(rl2);

      // Update
      r.update(3, rec);

      // Verify listeners notified of the event and only that event
      if (rl1.closed ||
          rl1.created ||
          rl1.deleted ||
          !rl1.modified ||
          rl1.opened ||
          rl2.closed ||
          rl2.created ||
          rl2.deleted ||
          !rl2.modified ||
          rl2.opened)
      {
        failed("Error in notification of fileModified");
        r.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try{r.close();} catch(Exception e1){}
      return;
    }
    try{r.close();} catch(Exception e1){}
    succeeded();
  }

  /**
   *Verify that AS400FileRecordDescription.createRecordFormatSource() fires a
   *a AS400FileRecordDescriptionEvent invoking the recordFormatSourceCreated()
   *method on the AS400FileRecordDescriptionListener object.
   *<ul compact>
   *<li>Add listeners, create record format source
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The recordFormatSourceCreated() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var047()
  {
    try
    {
      // Create the listeners
      DDMAS400FileRecordDescriptionListener rl1 = new DDMAS400FileRecordDescriptionListener();
      DDMAS400FileRecordDescriptionListener rl2 = new DDMAS400FileRecordDescriptionListener();

      // Create the AS400FileRecordDescription object
      AS400FileRecordDescription r = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Add listeners
      r.addAS400FileRecordDescriptionListener(rl1);
      r.addAS400FileRecordDescriptionListener(rl2);

      // Create record format source
      r.createRecordFormatSource(null, null);

      // Verify listeners notified of the event and only that event
      if (rl1.retrieved ||
          !rl1.created ||
          rl2.retrieved ||
          !rl2.created)
      {
        failed("Error in notification of recordFormatSourceCreated");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify that AS400FileRecordDescription.retrieveRecordFormat() fires a
   *a AS400FileRecordDescriptionEvent invoking the recordFormatRetrieved()
   *method on the AS400FileRecordDescriptionListener object.
   *<ul compact>
   *<li>Add listeners, create record format source
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The recordFormatRetrieved() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var048()
  {
    try
    {
      // Create the listeners
      DDMAS400FileRecordDescriptionListener rl1 = new DDMAS400FileRecordDescriptionListener();
      DDMAS400FileRecordDescriptionListener rl2 = new DDMAS400FileRecordDescriptionListener();

      // Create the AS400FileRecordDescription object
      AS400FileRecordDescription r = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE1.FILE");

      // Add listeners
      r.addAS400FileRecordDescriptionListener(rl1);
      r.addAS400FileRecordDescriptionListener(rl2);

      // Create record format source
      r.retrieveRecordFormat();

      // Verify listeners notified of the event and only that event
      if (rl1.created ||
          !rl1.retrieved ||
          rl2.created ||
          !rl2.retrieved)
      {
        failed("Error in notification of recordFormatRetrieved");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }
}
