///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMDelete.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.Trace;

import test.Testcase;

import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.SequentialFile;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;

import java.io.ByteArrayOutputStream;

/**
  Testcase DDMDelete.  This test class verifies valid and invalid usage of
  the delete(), deleteMember() and deleteCurrentRecord() methods.  This
  test class also verifies valid and invalid usage of SequentialFile.delete(recordNumber)
  and KeyedFile.delete(key).
**/
public class DDMDelete extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMDelete";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  long start;
  long time;
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMDelete(AS400            systemObject,
                   Vector           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   
                   String testLib)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMDelete", 18,
          variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
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


    // Do any necessary cleanup work for the variations
    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      System.out.println("Unable to complete cleanup.");
    }

    // Disconnect from the AS/400 for the record level access service
    systemObject_.disconnectService(AS400.RECORDACCESS);
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

      c.run("CRTLIB LIB(" + testLib_ +") AUT(*ALL)");
      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
      SequentialFile file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
      file.create(new DDMDeleteFormat(systemObject_), "DDMDelete");
      KeyedFile file2 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYFILE.FILE/MBR1.MBR");
      file2.create(new DDMDeleteKeyFormat(systemObject_), "DDMDelete");
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
    SequentialFile file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
    KeyedFile file2 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYFILE.FILE/MBR1.MBR");
    try
    {
      file.delete();
      file2.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      System.out.println("Unable to delete " + testLib_ + "/file");
      throw e;
    }
  }

  /**
   *Verify valid usage of delete() method.
   *<ul compact>
   *<li>Create a physical file with one member.
   *<li>Delete the file.
   *<li>Attempt to open the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The file will be deleted successfully.
   *<li>The attempt to open the file after the delete() will result in
   *an AS400Exception with AS400Message indicating CPF9812.
   *</ul>
  **/
  public void Var001()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DLTFILE.FILE/MBR1.MBR");
      file.create(132, "*DATA", "DDMDelete.Var001()");
      file.delete();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      if (file.isOpen())
      {
        file.close();
        file.delete();
      }
      failed("Exception didn't occur.");
    }
    catch(AS400Exception e)
    {
      AS400Message msg = e.getAS400Message();
      if(msg.getID().toUpperCase().indexOf("CPF9812") == -1)
      {
        e.printStackTrace(output_);
        failed("Wrong exception id.");
      }
      else
      {
        succeeded();
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed(e);
    }
  }


  /**
   *Verify invalid usage of delete().
   *<ul compact>
   *<li>Attempt to delete the file when it is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException should be thrown with
   *return code ExtendedIllegalStateException.OBJECT_CAN_NOT_BE_OPEN.
   *</ul>
  **/
  public void Var002()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.delete();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalStateException", ExtendedIllegalStateException.OBJECT_CAN_NOT_BE_OPEN))
      {
        e.printStackTrace(output_);
        failed("Wrong exception/return code");
      }
      else
      {
        succeeded();
      }
    }

    try
    {
      if (file.isOpen())
      {
        file.close();
      }
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of deleteMember().
   *<ul compact>
   *<li>Create a physical file with two members.
   *<li>Delete one of the members.
   *<li>Attempt to open the file for the member that was
   *deleted.
   *<li>Ensure that the member that was not deleted is still accesible.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>An AS400Exception with AS400Message indicating CPF4102 should be thrown.
   *</ul>
  **/
  public void Var003()
  {
    SequentialFile file = null;
    SequentialFile file2 = null;
    RecordFormat format = new DDMDeleteFormat(systemObject_);
    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
      file2 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR2.MBR");
      // Issue a change physical file command to allow more than one member.
      CommandCall interpreter = new CommandCall(systemObject_);
      interpreter.run("CHGPF FILE(" + testLib_ + "/FILE) MAXMBRS(*NOMAX)");

      file.addPhysicalFileMember("MBR2", "Member 2");
      file2.deleteMember();
      file2.setRecordFormat(format);
      file2.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      failed("Able to open file on member that was just deleted.");
    }
    catch(AS400Exception e)
    {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF4102") != -1)
      {
        // Ensure that MBR1 still exists.
        try
        {
          file.setRecordFormat(format);
          file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
          succeeded();
        }
        catch(Exception e1)
        {
          failed(e1, "Unable to open member that was not deleted");
        }
      }
      else
      {
        failed(e, "Wrong exception id.");
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }

    try
    {
      if (file.isOpen())
      {
        file.close();
      }
    }
    catch(Exception e) {}
  }

  /**
   *Verify invalid usage of deleteMember().
   *<ul compact>
   *<li>Create a physical file.
   *<li>Open the file.
   *<li>Attempt deleteMember().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException should be thrown
   *with return code ExtendedIllegalStateException.OBJECT_CAN_NOT_BE_OPEN.
   *</ul>
  **/
  public void Var004()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.deleteMember();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalStateException", ExtendedIllegalStateException.OBJECT_CAN_NOT_BE_OPEN))
      {
        failed(e, "Wrong exception/return code");
      }
      else
      {
        succeeded();
      }
    }

    try
    {
      if (file.isOpen())
      {
        file.close();
      }
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of SequentialFile.deleteCurrentRecord().
   *<ul compact>
   *<li>Create a physical file, open it, and populate it.
   *<li>Position the cursor and attempt deleteCurrentRecord().
   *Attempt to delete the following records:
   *<ul>
   *<li>First record
   *<li>Last record
   *<li>A record in the middle of the file.
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The records are deleted.
   *</ul>
  **/
  public void Var005()
  {
    SequentialFile file = null;

    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      Record[] records = file.readAll();

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Delete the first record.
      file.positionCursor(1);
      file.deleteCurrentRecord();

      // Ensure that only 'Record1' was deleted.
      file.close();
      records = file.readAll();
      if (records.length == 3 &&
          ((String) records[0].getField(0)).startsWith("Record2") &&
          ((String) records[1].getField(0)).startsWith("Record3") &&
          ((String) records[2].getField(0)).startsWith("Record4"))
      {
        // Delete the middle record.
        file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
        file.positionCursor(3);
        file.deleteCurrentRecord();

        // Ensure that only 'Record3' was deleted.
        file.close();
        records = file.readAll();
        if (records.length == 2 &&
            ((String) records[0].getField(0)).startsWith("Record2") &&
            ((String) records[1].getField(0)).startsWith("Record4"))
        {
          // Delete the last record.
          file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
          file.positionCursor(4);
          file.deleteCurrentRecord();

          // Ensure that only 'Record4' was deleted.
          file.close();
          records = file.readAll();
          assertCondition(records.length == 1 &&
                 ((String) records[0].getField(0)).startsWith("Record2"));
        }
        else
        {
          failed("deleteCurrentRecord() on middle record");
        }
      }
      else
      {
        failed("deleteCurrentRecord() on first record");
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/FILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify invalid usage of deleteCurrentRecord().
   *<ul compact>
   *<li>Attempt deleteCurrentRecord when the file hasn't been opened.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException with return code
   *ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN should be thrown.
   *</ul>
  **/
  public void Var006()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
      file.deleteCurrentRecord();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalStateException", ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
      {
        failed(e, "Wrong exception/returncode");
      }
      else
      {
        succeeded();
      }
    }
  }

  /**
   *Verify invalid usage of deleteCurrentRecord().
   *<ul compact>
   *<li>Attempt to delete a record when the cursor is not positioned
   *on a record.  Test the following positions:
   *<ul>
   *<li>Positioned before the first record.
   *<li>Positioned after the last record.
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>In each case an AS400Exception having AS400Message indicating CPF501B
   *should be thrown.
   *</ul>
  **/
  public void Var007()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.positionCursorBeforeFirst();
      file.deleteCurrentRecord();
      failed("Exception didn't occur.");
    }
    catch(AS400Exception e)
    {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF501B") != -1)
      {
        try
        {
          file.positionCursorAfterLast();
          file.deleteCurrentRecord();
          failed("Exception didn't occur.");
        }
        catch(AS400Exception e1)
        {
          msg = e1.getAS400Message();
          if (msg.getID().toUpperCase().indexOf("CPF501B") == -1)
          {
            failed(e1, "Wrong exception id");
          }
          else
          {
            succeeded();
          }
        }
        catch(Exception e1)
        {
          failed(e1, "Incorrect exception information.");
        }
      }
      else
      {
        failed(e, "Incorrect exception information.");
      }
    }
    catch(Exception e)
    {
      failed(e, "Incorrect exception information.");
    }

    try
    {
      if (file.isOpen())
      {
        file.close();
      }
    }
    catch(Exception e) {}

    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/FILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }

  }

  /**
   *Verify valid usage of SequentialFile.delete(int).
   *<ul compact>
   *<li>Delete record number 2.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Record with record number 2 will be deleted.
   *</ul>
  **/
  public void Var008()
  {
    SequentialFile file = null;

    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      Record[] records = file.readAll();

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Delete record 2.
      file.deleteRecord(2);

      // Ensure that only 'Record2' was deleted.
      file.close();
      records = file.readAll();
      if (records.length == 3 &&
          ((String) records[0].getField(0)).startsWith("Record1") &&
          ((String) records[1].getField(0)).startsWith("Record3") &&
          ((String) records[2].getField(0)).startsWith("Record4"))
      {
        succeeded();
      }
      else
      {
        failed("deleteRecord(2) failed: "+records.length);
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/FILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify valid usage of KeyedFile.delete(Object[]).
   *<ul compact>
   *<li>Delete a record by key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Record matching key will be deleted.
   *</ul>
  **/
  public void Var009()
  {
    KeyedFile file = null;

    try
    {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYFILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      Record[] records = file.readAll();

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Object[] k = new Object[1];
      k[0] = "Record2   ";

      // Delete record with key "Record2".
      file.deleteRecord(k);

      // Ensure that only 'Record2' was deleted.
      file.close();
      records = file.readAll();
      if (records.length == 3 &&
          ((String) records[0].getField(0)).startsWith("Record1") &&
          ((String) records[1].getField(0)).startsWith("Record3") &&
          ((String) records[2].getField(0)).startsWith("Record4"))
      {
        succeeded();
      }
      else
      {
        failed("deleteRecord(2) failed: "+records.length);
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/KEYFILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify invalid usage of SequentialFile.delete(int).
   *<ul compact>
   *<li>Specify a record number of 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException.
   *</ul>
  **/
  public void Var010()
  {
    SequentialFile file = null;

    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      // Delete record 0.
      try
      {
        file.deleteRecord(0);
        failed("Expected exception didn't occur.");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "recordNumber",
            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          e.printStackTrace(output_);
          failed("Wrong exception/return code");
        }
        else
        {
          succeeded();
        }
      }

      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/FILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify invalid usage of SequentialFile.delete(int).
   *<ul compact>
   *<li>Specify a non-existent record number.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception.
   *</ul>
  **/
  public void Var011()
  {
    SequentialFile file = null;

    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/FILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      // Delete record 5.
      try
      {
        file.deleteRecord(5);
        failed("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5006 indicates null cursor position in file, right?
        if(msg.getID().toUpperCase().indexOf("CPF5006") == -1)
        {
          e.printStackTrace(output_);
          failed("Wrong exception id.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failed(e, "Incorrect exception info.");
      }

      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      e.printStackTrace(output_);
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/FILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify invalid usage of KeyedFile.delete(Object[]).
   *<ul compact>
   *<li>Specify null for key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException.
   *</ul>
  **/
  public void Var012()
  {
    KeyedFile file = null;

    try
    {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYFILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Object[] k = null;
      // Delete record with null key.
      try
      {
        file.deleteRecord(k);
        failed("Expected exception didn't occur.");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          e.printStackTrace(output_);
          failed("Wrong exception/return code");
        }
        else
        {
          succeeded();
        }
      }

      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/KEYFILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify invalid usage of KeyedFile.delete(Object[]).
   *<ul compact>
   *<li>Specify array of length 0 for key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedllegalArgumentException.
   *</ul>
  **/
  public void Var013()
  {
    KeyedFile file = null;

    try
    {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYFILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Object[] k = new Object[0];
      // Delete record with empty key.
      try
      {
        file.deleteRecord(k);
        failed("Expected exception didn't occur.");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          e.printStackTrace(output_);
          failed("Wrong exception/return code");
        }
        else
        {
          succeeded();
        }
      }

      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/KEYFILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify invalid usage of KeyedFile.delete(Object[]).
   *<ul compact>
   *<li>Specify key for which there is no matching record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception.
   *</ul>
  **/
  public void Var014()
  {
    KeyedFile file = null;

    try
    {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYFILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Object[] k = new Object[1];
      k[0] = "Record5   ";

      // Delete record with non-existent key.
      try
      {
        file.deleteRecord(k);
        failed("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if(msg.getID().toUpperCase().indexOf("CPF5006") == -1)
        {
          e.printStackTrace(output_);
          failed("Wrong exception id.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failed(e, "Incorrect exception info.");
      }

      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/KEYFILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }


  /**
   *Verify valid usage of KeyedFile.delete(Object[]).
   *<ul compact>
   *<li>Delete a record by key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Record matching key will be deleted.
   *</ul>
  **/
  public void Var015()
  {
    KeyedFile file = null;

    try
    {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYFILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      Record[] records = file.readAll();

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

//      Object[] k = new Object[1];
//      k[0] = "Record2   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record2   "), 0, 10);
      byte[] k = keyAsBytes.toByteArray();

      // Delete record with key "Record2".
      file.deleteRecord(k, 1);

      // Ensure that only 'Record2' was deleted.
      file.close();
      records = file.readAll();
      if (records.length == 3 &&
          ((String) records[0].getField(0)).startsWith("Record1") &&
          ((String) records[1].getField(0)).startsWith("Record3") &&
          ((String) records[2].getField(0)).startsWith("Record4"))
      {
        succeeded();
      }
      else
      {
        failed("deleteRecord(2) failed: "+records.length);
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/KEYFILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }


  /**
   *Verify invalid usage of KeyedFile.delete(Object[]).
   *<ul compact>
   *<li>Specify null for key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException.
   *</ul>
  **/
  public void Var016()
  {
    KeyedFile file = null;

    try
    {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYFILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

//      Object[] k = null;
      byte[] k = null;
      // Delete record with null key.
      try
      {
        file.deleteRecord(k, 1);
        failed("Expected exception didn't occur.");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          e.printStackTrace(output_);
          failed("Wrong exception/return code");
        }
        else
        {
          succeeded();
        }
      }

      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/KEYFILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify invalid usage of KeyedFile.delete(Object[]).
   *<ul compact>
   *<li>Specify array of length 0 for key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedllegalArgumentException.
   *</ul>
  **/
  public void Var017()
  {
    KeyedFile file = null;

    try
    {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYFILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

//      Object[] k = new Object[0];
      byte[] k = new byte[0];
      // Delete record with empty key.
      try
      {
        file.deleteRecord(k, 1);
        failed("Expected exception didn't occur.");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          e.printStackTrace(output_);
          failed("Wrong exception/return code");
        }
        else
        {
          succeeded();
        }
      }

      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/KEYFILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify invalid usage of KeyedFile.delete(Object[]).
   *<ul compact>
   *<li>Specify key for which there is no matching record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception.
   *</ul>
  **/
  public void Var018()
  {
    KeyedFile file = null;

    try
    {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/KEYFILE.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMDeleteKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Write four records to the file.
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

//      Object[] k = new Object[1];
//      k[0] = "Record5   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record5   "), 0, 10);
      byte[] k = keyAsBytes.toByteArray();

      // Delete record with non-existent key.
      try
      {
        file.deleteRecord(k, 1);
        failed("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if(msg.getID().toUpperCase().indexOf("CPF5006") == -1)
        {
          e.printStackTrace(output_);
          failed("Wrong exception id.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failed(e, "Incorrect exception info.");
      }

      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Clear the file
    try
    {
      CommandCall c = new CommandCall(systemObject_, "CLRPFM FILE(" + testLib_ + "/KEYFILE) MBR(MBR1)");
      c.run();
    }
    catch(Exception e)
    {
    }
  }

}

