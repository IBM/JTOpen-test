///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMReadSeq.java
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
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

/**
 *Testcase DDMReadSeq.  This test class verifies valid and invalid usage of
 *the AS400File read methods:
 *<ul compact>
 *<li>read()
 *<li>readFirst()
 *<li>readNext()
 *<li>readPrevious()
 *<li>readLast()
 *</ul>
 *See DDMReadKey.java and DDMReadRN.java for variations testing read by
 *key or by record number respectively.
 *The read methods will be tested again for consistency in the
 *DDMReadRNCaching(n) testcases which call this test case with a
 *specified blocking factor.
**/
public class DDMReadSeq extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMReadSeq";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  // Blocking factor to be used for opens.  This is used to check
  // that caching acts the same as when we always retrieve from the system.
  // Default indicate to not use caching
  int bf_ = 1;

  long start;
  long time;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMReadSeq(AS400            systemObject,
                    Vector<String> variationsToRun,
                    int              runMode,
                    FileOutputStream fileOutputStream,
                    
                    String testLib)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMReadSeq", 50,
          variationsToRun, runMode, fileOutputStream);
    setTestLib(testLib);
  }

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMReadSeq(AS400            systemObject,
                    Vector<String> variationsToRun,
                    int              runMode,
                    FileOutputStream fileOutputStream,
                    
                    String testLib,
                    int              blockingFactor)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, (blockingFactor == 1)? "DDMReadSeq" : "DDMReadSeqCaching" + String.valueOf(blockingFactor), 50,
          variationsToRun, runMode, fileOutputStream);
    bf_ = blockingFactor;
    setTestLib(testLib);
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
      output_.println("Unable to connect to the AS/400");
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
      output_.println("Unable to complete setup; variations not run");
      return;
    }

    output_.println("Blocking factor of " + String.valueOf(bf_) + " was specified for " + name_ + ".");

    if (runMode_ != ATTENDED)
    {
      // Unattended variations.
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
      if (allVariations || variationsToRun_.contains("49"))
        Var049();
      if (allVariations || variationsToRun_.contains("50"))
        Var050();
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

      c.run("QSYS/CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          output_.println(msgs[i]);
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
   *Verify valid usage of read() using a SequentialFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Position cursor to the first record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to the last record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to record number 3 and invoke read().
   *<li>Invoke read() again.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>First record
   *<li>Last record
   *<li>Last record
   *<li>Record number three
   *<li>Record number three
   *</ul>
   *</ul>
  **/
  public void Var001()
  {
    setVariation(1);
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V1.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        record = file.read();
        if (((String) record.getField(0)).startsWith("Record1"))
        {
          file.positionCursorToLast();
          record = file.read();
          if (((String) record.getField(0)).startsWith("Record4"))
          {
            record = file.read();
            if (((String) record.getField(0)).startsWith("Record4"))
            {
              file.positionCursor(3);
              record = file.read();
              if (((String) record.getField(0)).startsWith("Record3"))
              {
                record = file.read();
                assertCondition(((String) record.getField(0)).startsWith("Record3"));
              }
            }
            else
            {
              failed("Incorrect record read from file.");
            }
          }
          else
          {
            failed("Incorrect record read from file.");
          }
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

  }

  /**
   *Verify valid usage of read() using a SequentialFile object.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Position cursor to the first record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to the last record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to record number 3 and invoke read().
   *<li>Invoke read() again.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>First record
   *<li>Last record
   *<li>Last record
   *<li>Record number three
   *<li>Record number three
   *</ul>
   *</ul>
  **/
  public void Var002()
  {
    setVariation(2);
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V2.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        record = file.read();
        if (((String) record.getField(0)).startsWith("Record1"))
        {
          file.positionCursorToLast();
          record = file.read();
          if (((String) record.getField(0)).startsWith("Record4"))
          {
            record = file.read();
            if (((String) record.getField(0)).startsWith("Record4"))
            {
              file.positionCursor(3);
              record = file.read();
              if (((String) record.getField(0)).startsWith("Record3"))
              {
                record = file.read();
                assertCondition(((String) record.getField(0)).startsWith("Record3"));
              }
            }
            else
            {
              failed("Incorrect record read from file.");
            }
          }
          else
          {
            failed("Incorrect record read from file.");
          }
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

  }

  /**
   *Verify valid usage of read() using a KeyedFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Position cursor to the first record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to the last record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to record by key and invoke read().
   *<li>Invoke read() again.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>First record
   *<li>Last record
   *<li>Last record
   *<li>Record matching the key
   *<li>Same record
   *</ul>
   *</ul>
  **/
  public void Var003()
  {
    setVariation(3);
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V3.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var003()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        record = file.read();
        if (((String) record.getField(0)).startsWith("Record1"))
        {
          file.positionCursorToLast();
          record = file.read();
          if (((String) record.getField(0)).startsWith("Record4"))
          {
            record = file.read();
            if (((String) record.getField(0)).startsWith("Record4"))
            {
              String[] key = new String[1];
              key[0] = "Record3   ";
              file.positionCursor(key);
              record = file.read();
              if (((String) record.getField(0)).startsWith("Record3"))
              {
                record = file.read();
                assertCondition(((String) record.getField(0)).startsWith("Record3"));
              }
            }
            else
            {
              failed("Incorrect record read from file.");
            }
          }
          else
          {
            failed("Incorrect record read from file.");
          }
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of read() using a KeyedFile object.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Position cursor to the first record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to the last record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to record by key and invoke read().
   *<li>Invoke read() again.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>First record
   *<li>Last record
   *<li>Last record
   *<li>Record matching the key
   *<li>Same record
   *</ul>
   *</ul>
  **/
  public void Var004()
  {
    setVariation(4);
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V4.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var004()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        record = file.read();
        if (((String) record.getField(0)).startsWith("Record1"))
        {
          file.positionCursorToLast();
          record = file.read();
          if (((String) record.getField(0)).startsWith("Record4"))
          {
            record = file.read();
            if (((String) record.getField(0)).startsWith("Record4"))
            {
              String[] key = new String[1];
              key[0] = "Record3   ";
              file.positionCursor(key);
              record = file.read();
              if (((String) record.getField(0)).startsWith("Record3"))
              {
                record = file.read();
                assertCondition(((String) record.getField(0)).startsWith("Record3"));
              }
            }
            else
            {
              failed("Incorrect record read from file.");
            }
          }
          else
          {
            failed("Incorrect record read from file.");
          }
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readNext() using a SequentialFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<liInvoke readNext().
   *<li>Invoke readNext() again.
   *<li>Position cursor to record number 3 and invoke readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>Second record
   *<li>Fourth record
   *</ul>
   *</ul>
  **/
  public void Var005()
  {
    setVariation(5);
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V5.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readNext();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        record = file.readNext();
        if (((String) record.getField(0)).startsWith("Record2"))
        {
          file.positionCursor(3);
          record = file.readNext();
          assertCondition(((String) record.getField(0)).startsWith("Record4"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readNext() using a SequentialFile object.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<liInvoke readNext().
   *<li>Invoke readNext() again.
   *<li>Position cursor to record number 3 and invoke readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>Second record
   *<li>Fourth record
   *</ul>
   *</ul>
  **/
  public void Var006()
  {
    setVariation(6);
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V6.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readNext();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        record = file.readNext();
        if (((String) record.getField(0)).startsWith("Record2"))
        {
          file.positionCursor(3);
          record = file.readNext();
          assertCondition(((String) record.getField(0)).startsWith("Record4"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readNext() using a KeyedFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<liInvoke readNext().
   *<li>Invoke readNext() again.
   *<li>Position cursor to a record by key and invoke readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>Second record
   *<li>Record after the record matching the key
   *</ul>
   *</ul>
  **/
  public void Var007()
  {
    setVariation(7);
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V7.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var007)");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readNext();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        record = file.readNext();
        if (((String) record.getField(0)).startsWith("Record2"))
        {
          String[] key = new String[1];
          key[0] = "Record3   ";
          file.positionCursor(key);
          record = file.readNext();
          assertCondition(((String) record.getField(0)).startsWith("Record4"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readNext() using a KeyedFile object.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<liInvoke readNext().
   *<li>Invoke readNext() again.
   *<li>Position cursor to a record by key and invoke readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>Second record
   *<li>Record after the record matching the key
   *</ul>
   *</ul>
  **/
  public void Var008()
  {
    setVariation(8);
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V8.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var008()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readNext();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        record = file.readNext();
        if (((String) record.getField(0)).startsWith("Record2"))
        {
          String[] key = new String[1];
          key[0] = "Record3   ";
          file.positionCursor(key);
          record = file.readNext();
          assertCondition(((String) record.getField(0)).startsWith("Record4"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readPrevious() using a SequentialFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Position the cursor to after the last record.
   *<liInvoke readPrevious().
   *<li>Invoke readPrevious() again.
   *<li>Position cursor to record number 3 and invoke readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>Last record
   *<li>Second to last record
   *<li>Second record
   *</ul>
   *</ul>
  **/
  public void Var009()
  {
    setVariation(9);
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V9.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      record = file.readPrevious();
      if (((String) record.getField(0)).startsWith("Record4"))
      {
        record = file.readPrevious();
        if (((String) record.getField(0)).startsWith("Record3"))
        {
          file.positionCursor(3);
          record = file.readPrevious();
          assertCondition(((String) record.getField(0)).startsWith("Record2"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readPrevious() using a SequentialFile object.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Position the cursor to after the last record.
   *<liInvoke readPrevious().
   *<li>Invoke readPrevious() again.
   *<li>Position cursor to record number 3 and invoke readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>Last record
   *<li>Second to last record
   *<li>Second record
   *</ul>
   *</ul>
  **/
  public void Var010()
  {
    setVariation(10);
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V10.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      record = file.readPrevious();
      if (((String) record.getField(0)).startsWith("Record4"))
      {
        record = file.readPrevious();
        if (((String) record.getField(0)).startsWith("Record3"))
        {
          file.positionCursor(3);
          record = file.readPrevious();
          assertCondition(((String) record.getField(0)).startsWith("Record2"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readPrevious() using a KeyedFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Position the cursor to after the last record.
   *<liInvoke readPrevious().
   *<li>Invoke readPrevious() again.
   *<li>Position cursor to a record by key and invoke readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>Last record
   *<li>Second to last record
   *<li>Record before the record matching the key
   *</ul>
   *</ul>
  **/
  public void Var011()
  {
    setVariation(11);
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V11.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var011()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      record = file.readPrevious();
      if (((String) record.getField(0)).startsWith("Record4"))
      {
        record = file.readPrevious();
        if (((String) record.getField(0)).startsWith("Record3"))
        {
          String[] key = new String[1];
          key[0] = "Record3   ";
          file.positionCursor(key);
          record = file.readPrevious();
          if (!((String) record.getField(0)).startsWith("Record2"))
          {
            failed("Incorrect record read from file s/b Record2. " + record.toString() + ".");
          }
          else
          {
            succeeded();
          }
//        assertCondition(((String) record.getField(0)).startsWith("Record2"));
        }
        else
        {
          failed("Incorrect record read from file s/b Record3. " + record.toString() + ".");
        }
      }
      else
      {
        failed("Incorrect record read from file s/b Record4. " + record.toString() + ".");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readPrevious() using a KeyedFile object.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Position the cursor to after the last record.
   *<liInvoke readPrevious().
   *<li>Invoke readPrevious() again.
   *<li>Position cursor to a record by key and invoke readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>Last record
   *<li>Second to last record
   *<li>Record before the record matching the key
   *</ul>
   *</ul>
  **/
  public void Var012()
  {
    setVariation(12);
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V11.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var011()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      record = file.readPrevious();
      if (((String) record.getField(0)).startsWith("Record4"))
      {
        record = file.readPrevious();
        if (((String) record.getField(0)).startsWith("Record3"))
        {
          String[] key = new String[1];
          key[0] = "Record3   ";
          file.positionCursor(key);
          record = file.readPrevious();
          assertCondition(((String) record.getField(0)).startsWith("Record2"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readFirst() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file for READ_ONLY.
   *<li>Invoke readFirst().
   *<li>Position the cursor to the last record.
   *<li>Invoke readFirst().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The first record will be returned.
   *<li>The first record will be returned.
   *</ul>
  **/
  public void Var013()
  {
    setVariation(13);
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V10.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readFirst();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        file.positionCursorToLast();
        record = file.readFirst();
        assertCondition(((String) record.getField(0)).startsWith("Record1"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readFirst() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file for READ_WRITE.
   *<li>Invoke readFirst().
   *<li>Position the cursor to the last record.
   *<li>Invoke readFirst().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The first record will be returned.
   *<li>The first record will be returned.
   *</ul>
  **/
  public void Var014()
  {
    setVariation(14);
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V10.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readFirst();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        file.positionCursorToLast();
        record = file.readFirst();
        assertCondition(((String) record.getField(0)).startsWith("Record1"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readFirst() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file for READ_ONLY.
   *<li>Invoke readFirst().
   *<li>Position the cursor to the last record.
   *<li>Invoke readFirst().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The first record will be returned.
   *<li>The first record will be returned.
   *</ul>
  **/
  public void Var015()
  {
    setVariation(15);
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V15.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var015()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readFirst();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        file.positionCursorToLast();
        record = file.readFirst();
        assertCondition(((String) record.getField(0)).startsWith("Record1"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readFirst() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file for READ_WRITE.
   *<li>Invoke readFirst().
   *<li>Position the cursor to the last record.
   *<li>Invoke readFirst().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The first record will be returned.
   *<li>The first record will be returned.
   *</ul>
  **/
  public void Var016()
  {
    setVariation(16);
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V16.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var016()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readFirst();
      if (((String) record.getField(0)).startsWith("Record1"))
      {
        file.positionCursorToLast();
        record = file.readFirst();
        assertCondition(((String) record.getField(0)).startsWith("Record1"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readLast() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file for READ_ONLY.
   *<li>Invoke readLast().
   *<li>Position the cursor to the first record.
   *<li>Invoke readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The last record will be returned.
   *<li>The last record will be returned.
   *</ul>
  **/
  public void Var017()
  {
    setVariation(17);
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V17.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readLast();
      if (((String) record.getField(0)).startsWith("Record4"))
      {
        file.positionCursorToFirst();
        record = file.readLast();
        assertCondition(((String) record.getField(0)).startsWith("Record4"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readLast() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file for READ_WRITE.
   *<li>Invoke readLast().
   *<li>Position the cursor to the first record.
   *<li>Invoke readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The last record will be returned.
   *<li>The last record will be returned.
   *</ul>
  **/
  public void Var018()
  {
    setVariation(18);
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V17.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readLast();
      if (((String) record.getField(0)).startsWith("Record4"))
      {
        file.positionCursorToFirst();
        record = file.readLast();
        assertCondition(((String) record.getField(0)).startsWith("Record4"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readLast() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file for READ_ONLY.
   *<li>Invoke readLast().
   *<li>Position the cursor to the first record.
   *<li>Invoke readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The last record will be returned.
   *<li>The last record will be returned.
   *</ul>
  **/
  public void Var019()
  {
    setVariation(19);
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V19.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var019()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readLast();
      if (((String) record.getField(0)).startsWith("Record4"))
      {
        file.positionCursorToFirst();
        record = file.readLast();
        assertCondition(((String) record.getField(0)).startsWith("Record4"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readLast() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file for READ_WRITE.
   *<li>Invoke readLast().
   *<li>Position the cursor to the first record.
   *<li>Invoke readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The last record will be returned.
   *<li>The last record will be returned.
   *</ul>
  **/
  public void Var020()
  {
    setVariation(20);
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V20.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var020()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readLast();
      if (((String) record.getField(0)).startsWith("Record4"))
      {
        file.positionCursorToFirst();
        record = file.readLast();
        assertCondition(((String) record.getField(0)).startsWith("Record4"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify invalid usage of read() using a SequentialFile object.
   *<ul compact>
   *<li>Invoke read() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var021()
  {
    setVariation(21);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V21.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var021()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      try
      {
        record = file.read(1);
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
   *Verify invalid usage of read() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file.
   *<li>Invoke read() prior to positioning the cursor (i.e we are not
   *positioned on a record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var022()
  {
    setVariation(22);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var022()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        record = file.read();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5147: Operation sequence for member MBR1 not valid.
        if(msg.getID().toUpperCase().indexOf("CPF5147") == -1)
        {
          failMsg.append("CPF5147 not thrown in exception\n");
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
   *Verify invalid usage of read() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file.
   *<li>Delete record 3
   *<li>Invoke read().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var023()
  {
    setVariation(23);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V23.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var023()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.deleteRecord(3);
      try
      {
        record = file.read();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5007: Record number 3 deleted.
        if(msg.getID().toUpperCase().indexOf("CPF5007") == -1)
        {
          failMsg.append("CPF5007 not thrown in exception\n");
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
   *Verify invalid usage of read() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file.
   *<li>Position the cursor to after the last record.
   *<li>Invoke read().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var024()
  {
    setVariation(24);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V24.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var024()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      try
      {
        record = file.read();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5147: Operation sequence for member MBR1 not valid.
        if(msg.getID().toUpperCase().indexOf("CPF5147") == -1)
        {
          failMsg.append("CPF5147 not thrown in exception\n");
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
   *Verify invalid usage of read() using a KeyedFile object.
   *<ul compact>
   *<li>Invoke read() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var025()
  {
    setVariation(25);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V25.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var025()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      try
      {
        record = file.read();
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
   *Verify invalid usage of read() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file.
   *<li>Invoke read() prior to positioning the cursor (i.e we are not
   *positioned on a record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var026()
  {
    setVariation(26);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V26.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var026()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        record = file.read();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5147: Operation sequence for member MBR1 not valid.
        if(msg.getID().toUpperCase().indexOf("CPF5147") == -1)
        {
          failMsg.append("CPF5147 not thrown in exception\n");
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
   *Verify invalid usage of read() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file.
   *<li>Delete record 3
   *<li>Invoke read().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var027()
  {
    setVariation(27);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V27.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var027()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = new Object[1];
      key[0] = "Record3   ";
      file.deleteRecord(key);
      try
      {
        record = file.read();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5007: Record number 3 deleted.
        if(msg.getID().toUpperCase().indexOf("CPF5007") == -1)
        {
          failMsg.append("CPF5007 not thrown in exception\n");
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
   *Verify invalid usage of read() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file.
   *<li>Position the cursor to after the last record.
   *<li>Invoke read().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var028()
  {
    setVariation(28);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V28.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var028()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      try
      {
        record = file.read();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5147: Operation sequence for member MBR1 not valid.
        if(msg.getID().toUpperCase().indexOf("CPF5147") == -1)
        {
          failMsg.append("CPF5147 not thrown in exception\n");
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
   *Verify invalid usage of readNext() using a SequentialFile object.
   *<ul compact>
   *<li>Invoke readNext() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var029()
  {
    setVariation(29);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V29.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var029()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      try
      {
        record = file.readNext();
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
   *Verify invalid usage of readNext() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file.
   *Position the cursor to the last record.
   *<li>Invoke readNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var030()
  {
    setVariation(30);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V30.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var030()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToLast();
      try
      {
        record = file.readNext();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if(msg.getID().toUpperCase().indexOf("CPF5147") == -1)
        {
          failMsg.append("CPF5147 not thrown in exception\n");
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
   *Verify invalid usage of readNext() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file.
   *Position the cursor to after the last record.
   *<li>Invoke readNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var031()
  {
    setVariation(31);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V31.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var031()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      try
      {
        record = file.readNext();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5025: null
        if(msg.getID().toUpperCase().indexOf("CPF5025") == -1)
        {
          failMsg.append("CPF5147 not thrown in exception\n");
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
   *Verify invalid usage of readNext() using a KeyedFile object.
   *<ul compact>
   *<li>Invoke readNext() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var032()
  {
    setVariation(32);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V32.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var032()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      try
      {
        record = file.readNext();
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
   *Verify invalid usage of readNext() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file.
   *Position the cursor to the last record.
   *<li>Invoke readNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var033()
  {
    setVariation(33);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V33.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var033()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToLast();
      try
      {
        record = file.readNext();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if(msg.getID().toUpperCase().indexOf("CPF5147") == -1)
        {
          failMsg.append("CPF5147 not thrown in exception\n");
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
   *Verify invalid usage of readNext() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file.
   *Position the cursor to after the last record.
   *<li>Invoke readNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var034()
  {
    setVariation(34);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V34.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var034()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      try
      {
        record = file.readNext();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5025: null
        if(msg.getID().toUpperCase().indexOf("CPF5025") == -1)
        {
          failMsg.append("CPF5025 not thrown in exception\n");
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
   *Verify invalid usage of readPrevious() using a SequentialFile object.
   *<ul compact>
   *<li>Invoke readPrevious() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var035()
  {
    setVariation(35);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V35.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var035()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      try
      {
        record = file.readPrevious();
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
   *Verify invalid usage of readPrevious() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file.
   *Position the cursor to the first record.
   *<li>Invoke readPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var036()
  {
    setVariation(36);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V36.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var036()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      try
      {
        record = file.readPrevious();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5025: null
        if(msg.getID().toUpperCase().indexOf("CPF5025") == -1)
        {
          failMsg.append("CPF5025 not thrown in exception\n");
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
   *Verify invalid usage of readPrevious() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file.
   *<li>Invoke readPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var037()
  {
    setVariation(37);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V37.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var037()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        record = file.readPrevious();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5025: null
        if(msg.getID().toUpperCase().indexOf("CPF5025") == -1)
        {
          failMsg.append("CPF5025 not thrown in exception\n");
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
   *Verify invalid usage of readPrevious() using a KeyedFile object.
   *<ul compact>
   *<li>Invoke readPrevious() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var038()
  {
    setVariation(38);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V38.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var038()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      try
      {
        record = file.readPrevious();
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
   *Verify invalid usage of readPrevious() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file.
   *Position the cursor to the first record.
   *<li>Invoke readPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var039()
  {
    setVariation(39);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V39.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var039()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      try
      {
        record = file.readPrevious();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5025: null
        if(msg.getID().toUpperCase().indexOf("CPF5025") == -1)
        {
          failMsg.append("CPF5025 not thrown in exception\n");
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
   *Verify invalid usage of readPrevious() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file.
   *<li>Invoke readPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var040()
  {
    setVariation(40);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V40.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var040()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        record = file.readPrevious();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5025: null
        if(msg.getID().toUpperCase().indexOf("CPF5025") == -1)
        {
          failMsg.append("CPF5025 not thrown in exception\n");
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
   *Verify invalid usage of readFirst() using a SequentialFile object.
   *<ul compact>
   *<li>Invoke readFirst() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var041()
  {
    setVariation(41);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V41.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var041()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      try
      {
        record = file.readFirst();
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
   *Verify invalid usage of readFirst() using a SequentialFile object.
   *<ul compact>
   *<li>Open an empty file.
   *<li>Invoke readFirst().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var042()
  {
    setVariation(42);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having no records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V42.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var042()");
      @SuppressWarnings("unused")
      Record record = new Record();
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        record = file.readFirst();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5025: null
        if(msg.getID().toUpperCase().indexOf("CPF5025") == -1)
        {
          failMsg.append("CPF5025 not thrown in exception\n");
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
   *Verify invalid usage of readFirst() using a KeyedFile object.
   *<ul compact>
   *<li>Invoke readFirst() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var043()
  {
    setVariation(43);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V43.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var043()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      try
      {
        record = file.readFirst();
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
   *Verify invalid usage of readFirst() using a KeyedFile object.
   *<ul compact>
   *<li>Open an empty file.
   *<li>Invoke readFirst().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var044()
  {
    setVariation(44);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having no records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V44.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var044()");
      @SuppressWarnings("unused")
      Record record = new Record();
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        record = file.readFirst();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5025: null
        if(msg.getID().toUpperCase().indexOf("CPF5025") == -1)
        {
          failMsg.append("CPF5025 not thrown in exception\n");
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
   *Verify invalid usage of readLast() using a SequentialFile object.
   *<ul compact>
   *<li>Invoke readLast() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var045()
  {
    setVariation(45);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V45.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var045()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      try
      {
        record = file.readLast();
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
   *Verify invalid usage of readLast() using a SequentialFile object.
   *<ul compact>
   *<li>Open an empty file.
   *<li>Invoke readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var046()
  {
    setVariation(46);
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      // Create a file having no records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V46.FILE/MBR1.MBR");
      RecordFormat format = new DDMChar10NoKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var046()");
      @SuppressWarnings("unused")
      Record record = new Record();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        record = file.readLast();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5025: null
        if(msg.getID().toUpperCase().indexOf("CPF5025") == -1)
        {
          failMsg.append("CPF5025 not thrown in exception\n");
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
   *Verify invalid usage of readLast() using a KeyedFile object.
   *<ul compact>
   *<li>Invoke readLast() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var047()
  {
    setVariation(47);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V47.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var047()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      try
      {
        record = file.readLast();
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
   *Verify invalid usage of readLast() using a KeyedFile object.
   *<ul compact>
   *<li>Open an empty file.
   *<li>Invoke readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPFxxxx
   *</ul>
  **/
  public void Var048()
  {
    setVariation(48);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having no records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V48.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var048()");
      @SuppressWarnings("unused")
      Record record = new Record();
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        record = file.readLast();
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        // CPF5025: null
        if(msg.getID().toUpperCase().indexOf("CPF5025") == -1)
        {
          failMsg.append("CPF5025 not thrown in exception\n");
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
   *Verify that null is returned when we read past the end of file.
   *<ul compact>
   *<li>Open the file.
   *<li>Set the cursor position to the last record.
   *<li>Invoke readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var049()
  {
    setVariation(49);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V49.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var049()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToLast();
      try
      {
        record = file.readNext();
      }
      catch(Exception e)
      {
        failMsg.append("readNext() failed.\n");
        e.printStackTrace(output_);
      }
      if (record != null)
      {
        failMsg.append("readNext() didn't return null: "+record.toString()+"\n");
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
   *Verify that null is returned when we read past the beginning of file.
   *<ul compact>
   *<li>Open the file.
   *<li>Set the cursor position to the first record.
   *<li>Invoke readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var050()
  {
    setVariation(50);
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V50.FILE/MBR1.MBR");
      RecordFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var050()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      try
      {
        record = file.readPrevious();
      }
      catch(Exception e)
      {
        failMsg.append("readPrevious() failed.\n");
        e.printStackTrace(output_);
      }
      if (record != null)
      {
        failMsg.append("readPrevious() didn't return null: "+record.toString()+"\n");
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


