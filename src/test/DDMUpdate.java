///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMUpdate.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.SequentialFile;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Trace;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.CharacterFieldDescription;

import java.io.ByteArrayOutputStream;

/**
 *Testcase DDMUpdate.  This test class verifies valid and invalid usage of
 *the AS400File, KeyedFile and SequentialFile update() methods:
 *<ul compact>
 *<li>AS400File.update(record)
 *<li>KeyedFile.update(key, record)
 *<li>KeyedFile.update(key, record, searchType)
 *<li>SequentialFile.update(recordNumber)
 *</ul>
**/
public class DDMUpdate extends Testcase
{
  long start;
  long time;
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMUpdate(AS400            systemObject,
                   Vector           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   String testLib)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMUpdate", 45,
          variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    // Connect to the AS/400 for record the record level access service
    try
    {
      systemObject_.connectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      System.out.println("Unable to connect to the AS/400");
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


    // Disconnect from the AS/400 for record the record level access service
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
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
  }

  /**
   *Verify valid usage of update(record) on a SequentialFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Position the cursor to the first record
   *<li>Invoke update(record).
   *<li>Invoke read() and verify the update.
   *<li>Invoke readLast().
   *<li>Invoke update(record)
   *<li>Invoke readLast() and verify the update
   *<li>Position the cursor to a record in the middle of the file by
   *record number.
   *<li>Invoke update(record)
   *<li>Invoke read(recordNumber) on the record updated and verify
   *the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The records will be updated
   *<ul compact>
   *<li>The first record will be updated
   *<li>The last record will be updated.
   *<li>The record accessed by record number will be updated.
   *</ul>
   *</ul>
  **/
  public void Var001()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    // Create file for use in this variation
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_,
                                             "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE1.FILE/MBR1.MBR");
      f1.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var001.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 001");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      file.update(rec);
      Record verify = file.read();
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on first record: "+verify.toString()+"\n");
      }

      file.readLast();
      file.update(rec);
      verify = file.readLast();
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on last record: "+verify.toString()+"\n");
      }

      file.positionCursor(4);
      file.update(rec);
      verify = file.read(4);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on middle record: "+verify.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(record) on a KeyedFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Position the cursor to the first record
   *<li>Invoke update(record).
   *<li>Invoke read() and verify the update.
   *<li>Invoke readLast().
   *<li>Invoke update(record)
   *<li>Invoke readLast() and verify the update
   *<li>Position the cursor to a record in the middle of the file by key.
   *<li>Invoke update(record)
   *<li>Invoke read(key) on the record updated and verify
   *the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The records will be updated
   *<ul compact>
   *<li>The first record will be updated
   *<li>The last record will be updated.
   *<li>The record accessed by key will be updated.
   *</ul>
   *</ul>
  **/
  public void Var002()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE2.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var002.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE2.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 002");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      file.update(rec);
      file.close();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record verify = file.readLast();
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on first record: "+verify.toString()+"\n");
      }

      file.readLast();
      file.update(rec);
      verify = file.readLast();
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on last record: "+verify.toString()+"\n");
      }

      Object[] key = new Object[1];
      key[0] = "Record 4  ";
      file.positionCursor(key);
      file.update(rec);
      key[0] = "UPDATE 002";
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on middle record: "+verify.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(record) on a SequentialFile.
   *<ul>
   *<li>Attempt update(record) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   *</ul>
  **/
  public void Var003()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE3.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 003");

      try
      {
        file.update(rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                         ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(record) on a SequentialFile.
   *<ul>
   *<li>Specify null for record
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "record".
   *</ul>
  **/
  public void Var004()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE4.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), null);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = null;
      try
      {
        file.update(rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "record"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(record) on a SequentialFile.
   *<ul>
   *<li>Attempt to update when not positioned on a record.
   *<ul compact>
   *<li>Open the file for READ_WRITE
   *<li>Invoke update(record).
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx.
   *</ul>
  **/
  public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    // Create file for use in this variation
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_,
                                             "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE5.FILE/MBR1.MBR");
      f1.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var005.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE5.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 005");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        file.update(rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF501B: Operation sequence for member MBR1 not valid. (C I)
        if(msg.getID().toUpperCase().indexOf("CPF501B") == -1)
        {
          failMsg.append("CPF501B not thrown in exception\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(record) on a SequentialFile.
   *<ul>
   *<li>Attempt to update after an update with no intervening
   *position or read operation..
   *<ul compact>
   *<li>Open the file for READ_WRITE
   *<li>Position the cursor to the first record.
   *<li>Invoke update(record)
   *<li>Invoke update(record) again
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx.
   *</ul>
  **/
  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    // Create file for use in this variation
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_,
                                         "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE6.FILE/MBR1.MBR");
      f1.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var006.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE6.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 006");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      file.update(rec);
      try
      {
        file.update(rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if(msg.getID().toUpperCase().indexOf("CPF501B") == -1)
        {
          failMsg.append("CPF501B not thrown in exception\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(record) on a KeyedFile.
   *<ul>
   *<li>Attempt update(record) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   *</ul>
  **/
  public void Var007()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE7.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = new DDMChar10KeyFormat(systemObject_).getNewRecord();
      try
      {
        file.update(rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                         ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(record) on a KeyedFile.
   *<ul>
   *<li>Specify null for record
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "record".
   *</ul>
  **/
  public void Var008()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE8.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), null);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = null;
      try
      {
        file.update(rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "record"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(record) on a KeyedFile.
   *<ul>
   *<li>Attempt to update when not positioned on a record.
   *<ul compact>
   *<li>Open the file for READ_WRITE
   *<li>Invoke update(record).
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx.
   *</ul>
  **/
  public void Var009()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE9.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var009.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE9.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 009");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        file.update(rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF501B: Operation sequence for member MBR1 not valid. (C I)
        if(msg.getID().toUpperCase().indexOf("CPF501B") == -1)
        {
          failMsg.append("CPF501B not thrown in exception\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(record) on a KeyedFile.
   *<ul>
   *<li>Attempt to update after an update with no intervening
   *position or read operation..
   *<ul compact>
   *<li>Open the file for READ_WRITE
   *<li>Position the cursor to the first record.
   *<li>Invoke update(record)
   *<li>Invoke update(record) again
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx.
   *</ul>
  **/
  public void Var010()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE10.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var010.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE10.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 010");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      file.update(rec);
      try
      {
        file.update(rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if(msg.getID().toUpperCase().indexOf("CPF501B") == -1)
        {
          failMsg.append("CPF501B not thrown in exception\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke update(key, record).
   *<li>Invoke read(key) and verify the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record will be updated.
   *<ul compact>
   *<li>The record accessed by key will be updated.
   *</ul>
   *</ul>
  **/
  public void Var011()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE11.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var011.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE11.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 011");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Object[] key = new Object[1];
      key[0] = "Record 5  ";
      file.update(key, rec);
      key[0] = "UPDATE 011";
      Record verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(rec, key) failed: "+verify.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Attempt update(key, record) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   *</ul>
  **/
  public void Var012()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE12.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 012");
      Object[] key = new Object[1];
      key[0] = "Record 1  ";

      try
      {
        file.update(key, rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                         ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Specify null for record
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "record".
   *</ul>
  **/
  public void Var013()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE13.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var013.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE13.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = null;
      Object[] key = new Object[1];
      key[0] = "Record 1  ";
      try
      {
        file.update(key, rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "record"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Specify null for key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key".
   *</ul>
  **/
  public void Var014()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE14.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var014.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE14.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 014");
      Object[] key = null;
      try
      {
        file.update(key, rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Specify a key of length 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "key" and
   *a value of LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var015()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE15.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var015.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE15.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 015");
      Object[] key = new Object[0];
      try
      {
        file.update(key, rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                         ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Specify an invalid key length > 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "key" and
   *a value of LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var016()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE16.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var016.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE16.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 016");
      Object[] key = new Object[2];
      key[0] = "Record 1  ";
      key[1] = "MoreFields";
      try
      {
        file.update(key, rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                         ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Specify a non-existent key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF5006.
   *</ul>
  **/
  public void Var017()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE17.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var017.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE17.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 017");
      Object[] key = new Object[1];
      key[0] = "Record 99 ";
      try
      {
        file.update(key, rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if(msg.getID().toUpperCase().indexOf("CPF5006") == -1)
        {
          failMsg.append("CPF5006 not thrown in exception\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke update(key, record, searchType), testing all search types.
   *<li>Invoke read(key) and verify the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record will be updated.
   *<ul compact>
   *<li>The record accessed by key will be updated.
   *</ul>
   *</ul>
  **/
  public void Var018()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE18.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var018.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE18.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE18EQ");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Object[] key = new Object[1];
      key[0] = "Record 3  ";
      file.update(key, rec, KeyedFile.KEY_EQ);
      key[0] = rec.getField(0);
      Record verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_EQ) failed: "+verify.toString()+"\n");
      }

      key[0] = "Record 4  ";
      rec.setField(0, "UPDATE18GE");
      file.update(key, rec, KeyedFile.KEY_GE);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_GE) failed: "+verify.toString()+"\n");
      }

      key[0] = "Record 5  ";
      rec.setField(0, "UPDATE18GT");
      file.update(key, rec, KeyedFile.KEY_GT);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_GT) failed: "+verify.toString()+"\n");
      }

      key[0] = "Record 6  ";
      rec.setField(0, "UPDATE18LE");
      file.update(key, rec, KeyedFile.KEY_LE);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_LE) failed: "+verify.toString()+"\n");
      }

      key[0] = "Record 7  ";
      rec.setField(0, "UPDATE18LT");
      file.update(key, rec, KeyedFile.KEY_LT);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_LT) failed: "+verify.toString()+"\n");
      }

    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Attempt update(key, record, searchType) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   *</ul>
  **/
  public void Var019()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE19.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 019");
      Object[] key = new Object[1];
      key[0] = "Record 1  ";

      try
      {
        file.update(key, rec, KeyedFile.KEY_GE);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                         ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify null for record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "record".
   *</ul>
  **/
  public void Var020()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE20.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var020.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE20.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = null;
      Object[] key = new Object[1];
      key[0] = "Record 1  ";
      try
      {
        file.update(key, rec, KeyedFile.KEY_EQ);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "record"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify null for key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key".
   *</ul>
  **/
  public void Var021()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE21.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var021.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE21.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 021");
      Object[] key = null;
      try
      {
        file.update(key, rec, KeyedFile.KEY_GE);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify a key of length 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "key" and
   *a value of LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var022()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE22.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var022.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE22.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 022");
      Object[] key = new Object[0];
      try
      {
        file.update(key, rec, KeyedFile.KEY_GE);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                         ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify an invalid key length > 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "key" and
   *a value of LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var023()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE23.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var023.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE23.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 023");
      Object[] key = new Object[2];
      key[0] = "Record 1  ";
      key[1] = "MoreFields";
      try
      {
        file.update(key, rec, KeyedFile.KEY_GE);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                         ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify a non-existent key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF5006.
   *</ul>
  **/
  public void Var024()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE24.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var024.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE24.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 024");
      Object[] key = new Object[1];
      key[0] = "Record 99 ";
      try
      {
        file.update(key, rec, KeyedFile.KEY_GE);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if(msg.getID().toUpperCase().indexOf("CPF5006") == -1)
        {
          failMsg.append("CPF5006 not thrown in exception\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify an invalid searchType.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "searchType" and
   *a value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var025()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE25.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var025.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE25.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 025");
      Object[] key = new Object[1];
      key[0] = "Record 99 ";
      try
      {
        file.update(key, rec, -1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "searchType",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(recordNumber, record) on a SequentialFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke update(recordNumber, record).
   *<li>Invoke read(recordNumber) on the record updated and verify
   *the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record will be updated.
   *<ul compact>
   *<li>The record accessed by record number will be updated.
   *</ul>
   *</ul>
  **/
  public void Var026()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    // Create file for use in this variation
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_,
                                             "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE26.FILE/MBR1.MBR");
      f1.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var026.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE26.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 026");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.update(4, rec);
      Record verify = file.read(4);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(4) failed: "+verify.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(recordNumber, record) on a SequentialFile.
   *<ul>
   *<li>Attempt update(recordNumber, record) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   *</ul>
  **/
  public void Var027()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE27.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 027");

      try
      {
        file.update(1, rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                         ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(recordNumber, record) on a SequentialFile.
   *<ul>
   *<li>Specify null for record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "record".
   *</ul>
  **/
  public void Var028()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    // Create file for use in this variation
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_,
                                             "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE28.FILE/MBR1.MBR");
      f1.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var028.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE28.FILE/MBR1.MBR");
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, file.getPath()).retrieveRecordFormat()[0]);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = null;
      try
      {
        file.update(1, rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "record"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(recordNumber, record) on a SequentialFile.
   *<ul>
   *<li>Attempt to update using an invalid recordNumber.
   *<ul compact>
   *<li>Open the file for READ_WRITE
   *<li>Invoke update(recordNumber, record).
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordNumber" and
   *a value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var029()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    // Create file for use in this variation
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_,
                                             "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE29.FILE/MBR1.MBR");
      f1.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var029.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE29.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 005");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        file.update(0, rec);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "recordNumber",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(recordNumber, record) on a SequentialFile.
   *<ul>
   *<li>Attempt to update after an update using the same recordNumber.
   *<ul compact>
   *<li>Open the file for READ_WRITE.
   *<li>Invoke update(recordNumber, record).
   *<li>Invoke update(recordNumber, record) again, using the same recordNumber.
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The specified record will be updated.
   *</ul>
  **/
  public void Var030()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    // Create file for use in this variation
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_,
                                         "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE30.FILE/MBR1.MBR");
      f1.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var030.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE30.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      Record rec2 = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE030a");
      rec2.setField(0, "UPDATE030b");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.update(2, rec);
      file.update(2, rec2);

      Record verify = file.read(2);
      if (!verify.toString().equals(rec2.toString()))
      {
        failMsg.append("update(recordNumber) failed: "+verify.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke update(key, record).
   *<li>Invoke read(key) and verify the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record will be updated.
   *<ul compact>
   *<li>The record accessed by key will be updated.
   *</ul>
   *</ul>
  **/
  public void Var031()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE11.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var031.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE11.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 011");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Object[] key = new Object[1];
//      key[0] = "Record 5  ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 5  "), 0, 10);
      byte[] k = keyAsBytes.toByteArray();

      file.update(k, rec, 1);
      key[0] = "UPDATE 011";
      Record verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(rec, key) failed: "+verify.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Attempt update(key, record) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   *</ul>
  **/
  public void Var032()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE12.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 012");
//      Object[] key = new Object[1];
//      key[0] = "Record 1  ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1  "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      try
      {
        file.update(key, rec, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                         ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Specify null for record
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "record".
   *</ul>
  **/
  public void Var033()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE13.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var013.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE13.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = null;
//      Object[] key = new Object[1];
//      key[0] = "Record 1  ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1  "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      try
      {
        file.update(key, rec, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "record"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Specify null for key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key".
   *</ul>
  **/
  public void Var034()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE14.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var014.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE14.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 014");
//      Object[] key = null;

      byte[] key = null;

      try
      {
        file.update(key, rec, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Specify a key of length 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "key" and
   *a value of LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var035()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE15.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var015.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE15.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 015");
//      Object[] key = new Object[0];
      byte[] key = new byte[0];

      try
      {
        file.update(key, rec, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                         ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Specify an invalid key length > 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "key" and
   *a value of LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var036()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE16.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var016.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE16.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 016");
//      Object[] key = new Object[2];
//      key[0] = "Record 1  ";
//      key[1] = "MoreFields";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1  "), 0, 10);
      keyAsBytes.write(text.toBytes("MoreFields"), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      try
      {
        file.update(key, rec, 2);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "numberOfKeyFields",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Specify a non-existent key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF5006.
   *</ul>
  **/
  public void Var037()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE17.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var017.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE17.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 017");
//      Object[] key = new Object[1];
//      key[0] = "Record 99 ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 99 "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      try
      {
        file.update(key, rec, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if(msg.getID().toUpperCase().indexOf("CPF5006") == -1)
        {
          failMsg.append("CPF5006 not thrown in exception\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke update(key, record, searchType), testing all search types.
   *<li>Invoke read(key) and verify the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record will be updated.
   *<ul compact>
   *<li>The record accessed by key will be updated.
   *</ul>
   *</ul>
  **/
  public void Var038()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE18.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var018.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE18.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE18EQ");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Object[] key = new Object[1];
//      key[0] = "Record 3  ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 3  "), 0, 10);
      byte[] k = keyAsBytes.toByteArray();
      file.update(k, rec, KeyedFile.KEY_EQ, 1);

      key[0] = rec.getField(0);
      Record verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_EQ) failed: "+verify.toString()+"\n");
      }

//      key[0] = "Record 4  ";
      System.arraycopy(text.toBytes("Record 4  "), 0, k, 0, 10);

      rec.setField(0, "UPDATE18GE");
      file.update(k, rec, KeyedFile.KEY_GE, 1);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_GE) failed: "+verify.toString()+"\n");
      }

//      key[0] = "Record 5  ";
      System.arraycopy(text.toBytes("Record 5  "), 0, k, 0, 10);

      rec.setField(0, "UPDATE18GT");
      file.update(k, rec, KeyedFile.KEY_GT, 1);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_GT) failed: "+verify.toString()+"\n");
      }

//      key[0] = "Record 6  ";
      System.arraycopy(text.toBytes("Record 6  "), 0, k, 0, 10);

      rec.setField(0, "UPDATE18LE");
      file.update(k, rec, KeyedFile.KEY_LE, 1);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_LE) failed: "+verify.toString()+"\n");
      }

//      key[0] = "Record 7  ";
      System.arraycopy(text.toBytes("Record 7  "), 0, k, 0, 10);

      rec.setField(0, "UPDATE18LT");
      file.update(k, rec, KeyedFile.KEY_LT, 1);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_LT) failed: "+verify.toString()+"\n");
      }

    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Attempt update(key, record, searchType) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   *</ul>
  **/
  public void Var039()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE19.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 019");
//      Object[] key = new Object[1];
//      key[0] = "Record 1  ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1  "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      try
      {
        file.update(key, rec, KeyedFile.KEY_GE, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                         ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify null for record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "record".
   *</ul>
  **/
  public void Var040()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE20.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var020.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE20.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = null;
//      Object[] key = new Object[1];
//      key[0] = "Record 1  ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1  "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      try
      {
        file.update(key, rec, KeyedFile.KEY_EQ, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "record"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify null for key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key".
   *</ul>
  **/
  public void Var041()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE21.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var021.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE21.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 021");
//      Object[] key = null;
      byte[] key = null;

      try
      {
        file.update(key, rec, KeyedFile.KEY_GE, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify a key of length 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "key" and
   *a value of LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var042()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE22.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var022.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE22.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 022");
//      Object[] key = new Object[0];
      byte[] key = new byte[0];

      try
      {
        file.update(key, rec, KeyedFile.KEY_GE, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                         ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify an invalid key length > 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "key" and
   *a value of LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var043()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE23.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var023.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE23.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 023");
//      Object[] key = new Object[2];
//      key[0] = "Record 1  ";
//      key[1] = "MoreFields";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1  "), 0, 10);
      keyAsBytes.write(text.toBytes("MoreFields"), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      try
      {
        file.update(key, rec, KeyedFile.KEY_GE, 2);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "numberOfKeyFields",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify a non-existent key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF5006.
   *</ul>
  **/
  public void Var044()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE24.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var024.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE24.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 024");
//      Object[] key = new Object[1];
//      key[0] = "Record 99 ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 99 "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      try
      {
        file.update(key, rec, KeyedFile.KEY_GE, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if(msg.getID().toUpperCase().indexOf("CPF5006") == -1)
        {
          failMsg.append("CPF5006 not thrown in exception\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify invalid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Specify an invalid searchType.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "searchType" and
   *a value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var045()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE25.FILE/MBR1.MBR");
      f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Record " + String.valueOf(i)+"  ");
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var025.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/UPDATE25.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE 025");
//      Object[] key = new Object[1];
//      key[0] = "Record 99 ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 99 "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      try
      {
        file.update(key, rec, -1, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "searchType",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

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

