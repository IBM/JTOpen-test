///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMReadRN.java
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
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

/**
 *Testcase DDMReadRN.  This test class verifies valid and invalid usage of
 *the SequentialFile read methods:
 *<ul compact>
 *<li>read(key)
 *<li>read(key, searchType)
 *<li>readAfter(key)
 *<li>readBefore(key)
 *</ul>
 *The read methods will be tested again for consistency in the
 *DDMReadRNCaching(n) testcases which call this test case with a
 *specified blocking factor.
**/
public class DDMReadRN extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMReadRN";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  long start;
  long time;
  Record[] records_;
  int bf_;
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMReadRN(AS400            systemObject,
                      Vector<String> variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMReadRN", 15,
          variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
  }

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMReadRN(AS400            systemObject,
                      Vector<String> variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib,
                      int              blockingFactor)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, (blockingFactor == 1)? "DDMReadRN" : "DDMReadRNCaching" + String.valueOf(blockingFactor), 15,
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

    System.out.println("Blocking factor of " + String.valueOf(bf_) + " was specified for " + name_ + ".");

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

    // Do any necessary cleanup work for the variations
    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      System.out.println("Unable to complete cleanup.");
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
        System.out.println(msgs[i]);
      }
      throw new Exception("");
    }
    // Create the necessary files
    SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
    f1.create(new DDMChar10NoKeyFormat(systemObject_), "One field, CHAR(10), no key");

    // Create an array of records to write to the files
    records_ = new Record[9];
    StringBuffer fieldValue = new StringBuffer();
    for (short i = 1; i < 10; ++i)
    {
      records_[i-1] = f1.getRecordFormat().getNewRecord();
      // Blank pad the field to 10; this is necessary for validating
      // what is returned from the AS/400.
      fieldValue.setLength(0);
      fieldValue.append("RECORD ");
      fieldValue.append(String.valueOf(i));
      for (int j = fieldValue.length(); j < 10; ++j)
      {
        fieldValue.append(" ");
      }
      records_[i-1].setField(0, fieldValue.toString());
    }

    // Populate the files
    f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f1.write(records_);
    f1.close();
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
    SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
    f1.delete();
    f1.close(); 
  }
  catch(Exception e)
  {
    System.out.println("Cleanup unsuccessful. Delete file " + testLib_ + "/READRN if it exists");
    e.printStackTrace(output_);
    throw e;
  }
}


  /**
   *Verify valid usage of read(recordNumber).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke read(recordNumber).
   *<li>Invoke read(recordNumber) again with the same record number.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The record <i>recordNumber</i> will be returned.
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
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      Record rec2 = new Record();
      try
      {
        rec1 = file.read(1);
        rec2 = file.read(1);
      }
      catch(Exception e)
      {
        failMsg.append("read() failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
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
   *Verify valid usage of read(recordNumber).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke read(recordNumber).
   *<li>Invoke read(recordNumber) again with the same record number.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The record <i>recordNumber</i> will be returned.
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
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      Record rec2 = new Record();
      try
      {
        rec1 = file.read(2);
        rec2 = file.read(2);
      }
      catch(Exception e)
      {
        failMsg.append("read() failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
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
   *Verify valid usage of readAfter(recordNumber).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke readAfter(recordNumber).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record after record <i>recordNumber</i> will be returned.
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
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = null;
      Record rec2 = records_[3];
      try
      {
        rec1 = file.readAfter(3);
      }
      catch(Exception e)
      {
        failMsg.append("readAfter() failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+".\n");
        failMsg.append("rec2: "+rec2.toString()+".\n");
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
   *Verify valid usage of readAfter(recordNumber).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke readAfter(recordNumber).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record after record <i>recordNumber</i> will be returned.
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
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      Record rec2 = records_[4];
      try
      {
        rec1 = file.readAfter(4);
      }
      catch(Exception e)
      {
        failMsg.append("readAfter() failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
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
   *Verify valid usage of readBefore(recordNumber).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke readBefore(recordNumber).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record before record <i>recordNumber</i> will be returned.
   *</ul>
   *</ul>
  **/
  public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      Record rec2 = records_[3];
      try
      {
        rec1 = file.readBefore(5);
      }
      catch(Exception e)
      {
        failMsg.append("readBefore() failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
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
   *Verify valid usage of readBefore(recordNumber).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke readBefore(recordNumber).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record before record <i>recordNumber</i> will be returned.
   *</ul>
   *</ul>
  **/
  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      Record rec2 = records_[4];
      try
      {
        rec1 = file.readBefore(6);
      }
      catch(Exception e)
      {
        failMsg.append("readBefore() failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
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
   *Verify invalid usage of read(recordNumber).
   *<ul>
   *<li>Invoke read(recordNumber) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var007()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec1 = new Record();
      try
      {
        rec1 = file.read(2);
        failMsg.append("Expected exception didn't occur.\n"+rec1);
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
   *Verify invalid usage of readAfter(recordNumber).
   *<ul>
   *<li>Invoke readAfter(recordNumber) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var008()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec1 = new Record();
      try
      {
        rec1 = file.readAfter(7);
        failMsg.append("Expected exception didn't occur.\n"+rec1);
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
   *Verify invalid usage of readBefore(recordNumber).
   *<ul>
   *<li>Invoke readBefore(recordNumber) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var009()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      Record rec1 = new Record();
      try
      {
        rec1 = file.readBefore(8);
        failMsg.append("Expected exception didn't occur.\n"+rec1);
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
   *Verify invalid usage of read(recordNumber).
   *<ul>
   *<li>Specify 0 for the record number
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordNumber"  and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var010()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      try
      {
        rec1 = file.read(0);
        failMsg.append("Expected exception didn't occur.\n"+rec1);
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
   *Verify invalid usage of readAfter(recordNumber).
   *<ul>
   *<li>Specify 0 for the record number
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordNumber"  and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var011()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      try
      {
        rec1 = file.readAfter(0);
        failMsg.append("Expected exception didn't occur.\n"+rec1);
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
   *Verify invalid usage of readBefore(recordNumber).
   *<ul>
   *<li>Specify 0 for the record number
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordNumber"  and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var012()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      try
      {
        rec1 = file.readBefore(0);
        failMsg.append("Expected exception didn't occur.\n"+rec1);
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
   *Verify that null is returned when trying to read(recordNumber) with
   *record number that does not exist in the file.
   *<ul compact>
   *<li>Invoke read(recordNumber) with a record number value that does not exist in the file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var013()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      try
      {
        rec1 = file.read(10);
      }
      catch(Exception e)
      {
        failMsg.append("read() failed.\n");
        e.printStackTrace(output_);
      }
      if (rec1 != null)
      {
        failMsg.append("read() failed to return null: "+rec1.toString()+"\n");
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
   *Verify that null is returned when readAfter(recordNumber) is specified
   *and no record exists after record <i>recordNumber</i>.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var014()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      try
      {
        rec1 = file.readAfter(9);
      }
      catch(Exception e)
      {
        failMsg.append("readAfter() failed.\n");
        e.printStackTrace(output_);
      }
      if (rec1 != null)
      {
        failMsg.append("readAfter() failed to return null: "+rec1.toString()+"\n");
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
   *Verify that null is returned when readBefore(recordNumber) is specified and no record
   *exists before record<i>recordNumber</i>.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var015()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      try
      {
        rec1 = file.readBefore(1);
      }
      catch(Exception e)
      {
        failMsg.append("readBefore() failed.\n");
        e.printStackTrace(output_);
      }
      if (rec1 != null)
      {
        failMsg.append("readBefore() failed to return null: "+rec1.toString()+"\n");
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
