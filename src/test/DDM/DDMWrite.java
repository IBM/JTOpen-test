///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMWrite.java
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

import test.Testcase;

import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.CharacterFieldDescription;

import com.ibm.as400.access.AS400Date;
import com.ibm.as400.access.AS400Time;
import com.ibm.as400.access.AS400Timestamp;
import com.ibm.as400.access.DateFieldDescription;
import com.ibm.as400.access.TimeFieldDescription;
import com.ibm.as400.access.TimestampFieldDescription;

/**
 *Testcase DDMWrite.  This test class verifies valid and invalid usage of
 *the AS400File write methods:
 *<ul compact>
 *<li>write(Record)
 *<li>write(Record[])
 *</ul>
**/
public class DDMWrite extends Testcase
{
  long start;
  long time;
  int bf_ = 1;
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMWrite(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMWrite", 35,
          variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
  }

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMWrite(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib,
                      int              blockingFactor)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, (blockingFactor == 1)? "DDMWrite" : "DDMWriteCaching" + String.valueOf(blockingFactor), 35,
          variationsToRun, runMode, fileOutputStream);
    bf_ = blockingFactor;
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

    System.out.println("Running " + name_ + " with a blocking factor of " + String.valueOf(bf_));

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
      deleteLibrary( testLib_);
 
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
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
  }

  /**
   *Verify valid usage of write(Record) on a Sequential File.
   *<ul>
   *<li>Open file for WRITE_ONLY.
   *<li>Invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var001()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV1.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Record 001");
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readFirst();

      if (!rec2.toString().equals(rec1.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
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
   *Verify valid usage of write(Record) on a Sequential File.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var002()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV2.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Record 002");
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readFirst();

      if (!rec2.toString().equals(rec1.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Open file for WRITE_ONLY.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  **/
  public void Var003()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV3.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch: "+rec2[i-1].toString()+" != "+recs[i-1].toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  **/
  public void Var004()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV4.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch: "+rec2[i-1].toString()+" != "+recs[i-1].toString()+"\n");
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
   *Verify invalid usage of write(Record) on a Sequential File.
   *<ul>
   *<li>Invoke write(Record) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/

  public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV5.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");

      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Record 005");
      try
      {
        file.write(rec1);
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
   *Verify invalid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Invoke write(Record[]) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/

  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV6.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");

      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
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
   *Verify invalid usage of write(Record) on a Sequential File.
   *<ul>
   *<li>Specify null for record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "record".
   *</ul>
  **/
  public void Var007()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV7.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = null;
      try
      {
        file.write(rec1);
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
   *Verify invalid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Specify null for Record[].
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "records".
   *</ul>
  **/
  public void Var008()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV8.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = null;
      try
      {
        file.write(recs);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "records"))
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
   *Verify invalid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Specify a Record[] of size 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "records" and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var009()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV9.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[0];
      try
      {
        file.write(recs);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "records",
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
   *Verify valid usage of write(Record) on a Sequential File.
   *<ul>
   *<li>Open file, specifying blocking factor > 0.
   *<li>Invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var010()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV10.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 20, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Record 010");
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 20, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readFirst();

      if (!rec2.toString().equals(rec1.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Open file, specifying blocking factor = 1.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  **/
  public void Var011()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV11.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch: "+rec2[i-1].toString()+" != "+recs[i-1].toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Open file, specifying blocking factor > 1 and < record array size.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  **/
  public void Var012()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV12.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 5, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch: "+rec2[i-1].toString()+" != "+recs[i-1].toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Open file, specifying blocking factor > record array size.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  **/
  public void Var013()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV13.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 15, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch: "+rec2[i-1].toString()+" != "+recs[i-1].toString()+"\n");
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
   *Verify invalid usage of write(Record) on a Sequential File.
   *<ul>
   *<li>Create file with record format, then call setRecordFormat()
   *specifying a different record format whose record length does not match
   *the files record length, and attempt to write.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "record" and
   *PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var014()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV14.FILE/MBR1.MBR");
      RecordFormat rf1 = new RecordFormat("FMT1");
      RecordFormat rf2 = new RecordFormat("FMT2");
      rf1.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "field1"));
      rf2.addFieldDescription(new CharacterFieldDescription(new AS400Text(15, systemObject_.getCcsid(), systemObject_), "Field01"));
      rf2.addFieldDescription(new CharacterFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), "Field02"));

      file.create(rf1, "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Record rec1 = rf2.getNewRecord();
      rec1.setField(0, "Record 014");
      try
      {
        file.write(rec1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "record", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception");
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
   *Verify invalid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Create file with record format, then call setRecordFormat()
   *specifying a different record format whose record length does not match
   *the files record length, and attempt to write.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception should occur.
   *</ul>
  **/
  public void Var015()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV15.FILE/MBR1.MBR");
      RecordFormat rf1 = new RecordFormat("FMT1");
      RecordFormat rf2 = new RecordFormat("FMT2");
      rf1.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "field1"));
      rf2.addFieldDescription(new CharacterFieldDescription(new AS400Text(15, systemObject_.getCcsid(), systemObject_), "Field01"));
      rf2.addFieldDescription(new CharacterFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), "Field02"));

      file.create(rf1, "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Record[] recs = new Record[9];
      RecordFormat rf = rf2;
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "record", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception");
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
   *Verify valid usage of write(Record) on a Sequential File.
   *<ul>
   *<li>Open file with pre-existing records, invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var016()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV16.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Populate file
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      file.write(recs);
      file.close();

      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Record 010");
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readLast();

      if (!rec2.toString().equals(rec1.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Open file with pre-existing records, invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var017()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV17.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Populate file
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      file.write(recs);
      file.close();

      // Write extra records
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] extras = new Record[9];
      RecordFormat rf2 = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        extras[i-1] = rf2.getNewRecord();
        extras[i-1].setField(0, "RECORD 00" + Integer.toString(i));
      }
      try
      {
        file.write(extras);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] allrecs = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!allrecs[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch recs: "+allrecs[i-1].toString()+" != "+recs[i-1].toString()+"\n");
        }
        if (!allrecs[i+8].toString().equals(extras[i-1].toString()))
        {
          failMsg.append("mismatch extras: "+allrecs[i+8].toString()+" != "+extras[i-1].toString()+"\n");
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
   *Verify valid usage of write(Record) on a Keyed File.
   *<ul>
   *<li>Open file for WRITE_ONLY.
   *<li>Invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var018()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK1.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Record 001");
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readFirst();

      if (!rec2.toString().equals(rec1.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
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
   *Verify valid usage of write(Record) on a Keyed File.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var019()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK2.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Record 002");
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readFirst();

      if (!rec2.toString().equals(rec1.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Open file for WRITE_ONLY.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  **/
  public void Var020()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK3.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch: "+rec2[i-1].toString()+" != "+recs[i-1].toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  **/
  public void Var021()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK4.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch: "+rec2[i-1].toString()+" != "+recs[i-1].toString()+"\n");
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
   *Verify invalid usage of write(Record) on a Keyed File.
   *<ul>
   *<li>Invoke write(Record) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/

  public void Var022()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK5.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");

      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Record 005");
      try
      {
        file.write(rec1);
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
   *Verify invalid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Invoke write(Record[]) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/

  public void Var023()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK6.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");

      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
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
   *Verify invalid usage of write(Record) on a Keyed File.
   *<ul>
   *<li>Specify null for record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "record".
   *</ul>
  **/
  public void Var024()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK7.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = null;
      try
      {
        file.write(rec1);
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
   *Verify invalid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Specify null for Record[].
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "records".
   *</ul>
  **/
  public void Var025()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK8.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = null;
      try
      {
        file.write(recs);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "records"))
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
   *Verify invalid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Specify a Record[] of size 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "records" and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var026()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK9.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[0];
      try
      {
        file.write(recs);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "records",
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
   *Verify valid usage of write(Record) on a Keyed File.
   *<ul>
   *<li>Open file, specifying blocking factor > 0.
   *<li>Invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var027()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK10.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 20, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Record 010");
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 20, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readFirst();

      if (!rec2.toString().equals(rec1.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Open file, specifying blocking factor = 1.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  **/
  public void Var028()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK11.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch: "+rec2[i-1].toString()+" != "+recs[i-1].toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Open file, specifying blocking factor > 1 and < record array size.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  **/
  public void Var029()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK12.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 5, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch: "+rec2[i-1].toString()+" != "+recs[i-1].toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Open file, specifying blocking factor > record array size.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  **/
  public void Var030()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK13.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 15, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch: "+rec2[i-1].toString()+" != "+recs[i-1].toString()+"\n");
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
   *Verify invalid usage of write(Record) on a Keyed File.
   *<ul>
   *<li>Create file with record format, then call setRecordFormat()
   *specifying a different record format whose record length does not match
   *the files record length, and attempt to write.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception should occur.
   *</ul>
  **/
  public void Var031()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK14.FILE/MBR1.MBR");
      RecordFormat rf1 = new RecordFormat("FMT1");
      RecordFormat rf2 = new RecordFormat("FMT2");
      rf1.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "field1"));
      rf2.addFieldDescription(new CharacterFieldDescription(new AS400Text(15, systemObject_.getCcsid(), systemObject_), "Field01"));
      rf2.addFieldDescription(new CharacterFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), "Field02"));

      file.create(rf1, "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Record rec1 = rf2.getNewRecord();
      rec1.setField(0, "Record 014");
      try
      {
        file.write(rec1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "record", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception");
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
   *Verify invalid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Create file with record format, then call setRecordFormat()
   *specifying a different record format whose record length does not match
   *the files record length, and attempt to write.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception should occur.
   *</ul>
  **/
  public void Var032()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK15.FILE/MBR1.MBR");
      RecordFormat rf1 = new RecordFormat("FMT1");
      RecordFormat rf2 = new RecordFormat("FMT2");
      rf1.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "field1"));
      rf2.addFieldDescription(new CharacterFieldDescription(new AS400Text(15, systemObject_.getCcsid(), systemObject_), "Field01"));
      rf2.addFieldDescription(new CharacterFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), "Field02"));

      file.create(rf1, "*BLANK");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Record[] recs = new Record[9];
      RecordFormat rf = rf2;
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      try
      {
        file.write(recs);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "record", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception");
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
   *Verify valid usage of write(Record) on a Keyed File.
   *<ul>
   *<li>Open file with pre-existing records, invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var033()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK16.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Populate file
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      file.write(recs);
      file.close();

      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Record 010");
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readLast();

      if (!rec2.toString().equals(rec1.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
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
   *Verify valid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Open file with pre-existing records, invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var034()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEK17.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Populate file
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Record   " + Integer.toString(i));
      }
      file.write(recs);
      file.close();

      // Write extra records
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] extras = new Record[9];
      RecordFormat rf2 = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        extras[i-1] = rf2.getNewRecord();
        extras[i-1].setField(0, "RECORD 00" + Integer.toString(i));
      }
      try
      {
        file.write(extras);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] allrecs = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!allrecs[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("mismatch recs: "+allrecs[i-1].toString()+" != "+recs[i-1].toString()+"\n");
        }
        if (!allrecs[i+8].toString().equals(extras[i-1].toString()))
        {
          failMsg.append("mismatch extras: "+allrecs[i+8].toString()+" != "+extras[i-1].toString()+"\n");
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
   *Verify valid usage of write(Record) on a Sequential File, where the record contains 'date', 'time', and 'timestamp' fields.
   *<ul>
   *<li>Open file for WRITE_ONLY.
   *<li>Invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var035()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      DDMFormatDateAndTime recFmt = new DDMFormatDateAndTime(systemObject_);

      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/WRITEV35.FILE/MBR1.MBR");
      file.create(recFmt, "DDMWrite.Var035()");
      file.open(AS400File.WRITE_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, new AS400Date().getDefaultValue());
      rec1.setField(1, new AS400Time().getDefaultValue());
      rec1.setField(2, new AS400Timestamp().getDefaultValue());
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readFirst();

      String rec1String = rec1.toString();
      String rec2String = rec2.toString();
      //System.out.println("rec1.toString(): |" + rec1String + "|");
      //System.out.println("rec2.toString(): |" + rec2String + "|");
      if (!rec2String.equals(rec1String))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1String+"\n");
        failMsg.append("rec2: "+rec2String+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    finally
    {
      ///System.out.println("File: " + file.getPath());
      ///System.out.println ("Press ENTER to continue."); try { System.in.read(); } catch (Exception exc) {};
      if (file != null) {
        try {
          file.close();
          file.delete();
        }
        catch(Exception e) {}
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

  class DDMFormatDateAndTime extends RecordFormat
  {
    DDMFormatDateAndTime(AS400 sys)
    {
      super("DateAndTime");
      AS400Date date0 = new AS400Date(AS400.getDefaultTimeZone(sys));
      AS400Time time0 = new AS400Time(AS400.getDefaultTimeZone(sys));
      AS400Timestamp timestamp0 = new AS400Timestamp(AS400.getDefaultTimeZone(sys));
      addFieldDescription(new DateFieldDescription(date0, "Field1"));
      addFieldDescription(new TimeFieldDescription(time0, "Field2"));
      addFieldDescription(new TimestampFieldDescription(timestamp0, "Field3"));
    }
  }

}
