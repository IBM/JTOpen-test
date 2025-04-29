///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMDeletedRecords.java
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
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

/**
 *Testcase DDMDeletedRecords.  This test class verifies reading records
 *from a file that contains deleted records.
**/
public class DDMDeletedRecords extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMDeletedRecords";
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
  public DDMDeletedRecords(AS400            systemObject,
                           Vector<String> variationsToRun,
                           int              runMode,
                           FileOutputStream fileOutputStream,
                           
                           String testLib)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMDeletedRecords", 3,
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

    if (runMode_ != ATTENDED)
    {
      // Unattended variations.
      if (allVariations || variationsToRun_.contains("1"))
        Var001();
      if (allVariations || variationsToRun_.contains("2"))
        Var002();
      if (allVariations || variationsToRun_.contains("3"))
        Var003();
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
   *Verify valid usage of readNext() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file.
   *<li>Delete record 3
   *<li>Invoke readPevious().
   *<li>Invoke readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record after the deleted record should be returned.
   *</ul>
  **/
  public void Var001()
  {
    setVariation(1);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/VAR1.FILE/VAR1.MBR");
      file.create(7, "*DATA", "DDMDeletedRecords.Var001");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = file.getRecordFormat().getNewRecord();
      for (int i = 1; i < 10; ++i)
      {
        r.setField(0, "Record" + String.valueOf(i));
        file.write(r);
      }

      file.deleteRecord(3);
      r = file.readPrevious();
      if (!r.toString().equals("Record2"))
      {
        failed("Read previous returned wrong record");
      }
      else
      {
        r = file.readNext();
        if (!r.toString().equals("Record4"))
        {
          failed("Read next returned wrong record");
        }
        else
        {
          r = file.read(3);
          if (r != null)
          {
            failed("Read() of deleted record does not return null");
          }
          else
          {
            succeeded();
          }
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify valid usage of readPrevious() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file.
   *<li>Delete record 3
   *<li>Invoke readNext().
   *<li>Invoke readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record before the deleted record should be returned.
   *</ul>
  **/
  public void Var002()
  {
    setVariation(2);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/VAR2.FILE/VAR2.MBR");
      file.create(7, "*DATA", "DDMDeletedRecords.Var001");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = file.getRecordFormat().getNewRecord();
      for (int i = 1; i < 10; ++i)
      {
        r.setField(0, "Record" + String.valueOf(i));
        file.write(r);
      }

      file.deleteRecord(3);
      r = file.readNext();
      if (!r.toString().equals("Record4"))
      {
        failed("Read next returned wrong record");
      }
      else
      {
        r = file.readPrevious();
        if (!r.toString().equals("Record2"))
        {
          failed("Read previous returned wrong record");
        }
        else
        {
          r = file.read(3);
          if (r != null)
          {
            failed("Read() of deleted record does not return null");
          }
          else
          {
            succeeded();
          }
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e)
    {
    }
  }

  /**
   *Verify that readAll() returns only active records when a file contains
   *deleted records and active records.
   *<ul compact>
   *<li>First record deleted.
   *<li>Last record deleted.
   *<li>First and last records deleted.
   *<li>Intermediate records deleted.
   *<li>All records deleted.
   *<li>An empty file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>For each that the file contains active records, only the active records
   *are returned.
   *<li>For the case in which no active records exist in the file, an empty
   *array is returned.
   *<li>For the case in which the file is empty, an empty
   *array is returned.
   *</ul>
  **/
  @SuppressWarnings("resource")
  public void Var003()
  {
    setVariation(3);
    SequentialFile file = null;
    SequentialFile file2 = null;
    SequentialFile file3 = null;
    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/VAR2.FILE/VAR2.MBR");
      file2 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/VAR2B.FILE/VAR2B.MBR");
      file3 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/VAR2C.FILE/VAR2C.MBR");
      file.create(7, "*DATA", "DDMDeletedRecords.Var001");
      file2.create(7, "*DATA", "DDMDeletedRecords.Var001");
      file3.create(7, "*DATA", "DDMDeletedRecords.Var001");
      RecordFormat rf = file.getRecordFormat();
      RecordFormat rf2 = file2.getRecordFormat();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = rf.getNewRecord();
      for (int i = 1; i < 10; ++i)
      {
        r.setField(0, "Record" + String.valueOf(i));
        file.write(r);
      }
      file2.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r2 = rf2.getNewRecord();
      for (int i = 1; i < 10; ++i)
      {
        r2.setField(0, "Record" + String.valueOf(i));
        file2.write(r2);
      }
      file.close();
      file2.close();
      Record[] before = file.readAll();
      Record[] before2 = file2.readAll();

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      file.deleteCurrentRecord();
      file.close();
      Record[] after = file.readAll();
      for (int i = 0; i < after.length; ++i)
      {
        if (!before[i+1].toString().equals(after[i].toString()))
        {
          failed("readAll not returning correct records");
          file.delete();
          return;
        }
      }
      file2.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.positionCursorToLast();
      file2.deleteCurrentRecord();
      file2.close();
      Record[] after2 = file2.readAll();
      for (int i = 0; i < after2.length; ++i)
      {
        if (!before2[i].toString().equals(after2[i].toString()))
        {
          failed("readAll not returning correct records");
          file2.delete();
          return;
        }
      }
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToLast();
      file.deleteCurrentRecord();
      file.close();
      after = file.readAll();
      for (int i = 0; i < after.length; ++i)
      {
        if (!before[i+1].toString().equals(after[i].toString()))
        {
          failed("readAll not returning correct records");
          file.delete();
          return;
        }
      }
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursor(3);
      file.deleteCurrentRecord();
      file.readNext();
      file.deleteCurrentRecord();
      file.readNext();
      file.deleteCurrentRecord();
      file.close();
      after = file.readAll();
      // Deleted records = 1,3,4,5,9
      if (!(after.length == 4 &&
            after[0].toString().equals(before[1].toString()) &&
            after[1].toString().equals(before[5].toString()) &&
            after[2].toString().equals(before[6].toString()) &&
            after[3].toString().equals(before[7].toString())))
      {
        failed("Wrong records returned on readall with first, last and intermediate records deleted");
        file.delete();
        return;
      }
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursor(2);
      file.deleteCurrentRecord();
      file.positionCursor(6);
      file.deleteCurrentRecord();
      file.positionCursor(7);
      file.deleteCurrentRecord();
      file.positionCursor(8);
      file.deleteCurrentRecord();
      file.close();
      after = file.readAll();
      if (after.length != 0)
      {
        failed("Array of length 0 not returned on readAll from file with all deleted records");
        file.delete();
        return;
      }
      after = file3.readAll();
      if (after.length != 0)
      {
        failed("Array of length 0 not returned on readAll from empty file");
        file3.delete();
        file3.close(); 
        return;
      }
      file3.close(); 
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e)
    {
    }
    try
    {
      file2.close();
      file2.delete();
    }
    catch(Exception e)
    {
    }
    try
    {
      file3.close();
      file3.delete();
    }
    catch(Exception e)
    {
    }
  }
}
