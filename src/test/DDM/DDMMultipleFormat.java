///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMMultipleFormat.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.*;

import java.util.Vector;
import java.math.BigDecimal;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.SequentialFile;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.Trace;

import test.Testcase;

import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.AS400Text;


/**
 *Testcase DDMMultipleFormat.  This test class verifies using multiple format logical files:
 *<ul compact>
 *<li>reading
 *<li>writing
 *</ul>
**/
public class DDMMultipleFormat extends Testcase
{
//  AS400 pwrSys_ = DDMTest.PwrSys;
  long start;
  long time;
//  String testLib_ = "DDMTest";
  String testLib_ = null;
  String fileName_;
  Record[] records_ = new Record[100];
  Record[] recordsFmt1_ = new Record[100];
  Record[] recordsFmt2_ = new Record[100];

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
/*  public DDMMultipleFormat(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      )
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMMultipleFormat", 1, variationsToRun, runMode,
          fileOutputStream);
    pwrSys_ = pwrSys;
  }
*/

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMMultipleFormat(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib,
                      AS400 pwrSys)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMMultipleFormat", 1, variationsToRun, runMode,
          fileOutputStream);
    pwrSys_ = pwrSys;
//    if (testLib != null)
//    {
    testLib_ = testLib;
//    }
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
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("1"))
        Var001();
/*
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
*/
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
      fileName_ = "/qsys.lib/ddmtestsav.lib/mltfmt.file/%file%.mbr";
      // Verify the existence of library DDMTESTSAV on the system
      CommandCall c = new CommandCall(pwrSys_, "CHKOBJ OBJ(DDMTESTSAV) OBJTYPE(*LIB)");
      boolean ran = c.run();
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
        System.out.println("test/ in GIT");
        System.out.println("to the AS/400 system to which you are running.");
        System.out.println("Use RSTLIB to restore library DDMTESTSAV to the system.");
        throw new Exception("");
      }
      // Delete and re-create the file
      SequentialFile f = new SequentialFile(pwrSys_, fileName_);
      try
      {
        f.delete();
      }
      catch(Exception e) {} // may not exist, so ignore faliures
      c.run("CRTLF FILE(DDMTESTSAV/MLTFMT) SRCFILE(DDMTESTSAV/QDDSSRC)");
      // Populate file simpleseq - this is the file over which mltfmt is built
      // Clear the file first
      c.run("CLRPFM FILE(DDMTESTSAV/SIMPLESEQ)");
      AS400FileRecordDescription rd = new AS400FileRecordDescription(systemObject_, "/qsys.lib/ddmtestsav.lib/simpleseq.file");
      SequentialFile f2 = new SequentialFile(pwrSys_, "/qsys.lib/ddmtestsav.lib/simpleseq.file");
      f2.setRecordFormat(rd.retrieveRecordFormat()[0]);
      f2.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      RecordFormat rf = f2.getRecordFormat();
      String field1 = null;
      for (int i = 0; i < 100; ++i)
      {
        records_[i] = rf.getNewRecord();
        field1 = "Record " + String.valueOf(i);
        for (int j = field1.length(); j < 10; ++j)
        {
          field1 += " ";
        }
        records_[i].setField(0, field1);
        records_[i].setField(1, new Integer(i));
        records_[i].setField(2, new BigDecimal(String.valueOf(i) + "." + String.valueOf(i)));
      }
      f2.write(records_);
      f2.close();
      // Populate the record[]s used for comparison
      KeyedFile k = new KeyedFile(pwrSys_, fileName_);
      rd.setPath(fileName_);
      RecordFormat[] rfs = rd.retrieveRecordFormat();
      k.setRecordFormat(rfs[0]);
      k.open(AS400File.READ_ONLY, 100, AS400File.COMMIT_LOCK_LEVEL_NONE);
      for (int i = 0; i < 100; ++i)
      {
        recordsFmt1_[i] = k.readNext();
      }
      k.close();
      k.setRecordFormat(rfs[1]);
      k.open(AS400File.READ_ONLY, 100, AS400File.COMMIT_LOCK_LEVEL_NONE);
      for (int i = 0; i < 100; ++i)
      {
        recordsFmt2_[i] = k.readNext();
      }
      k.close();
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
  }

  /**
   *Verify multpiple format logical file..
   *<ul compact>
   *<li>Read from the file specifying the different formats associated with the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Record read will have the correct format.
   *</ul>
  **/
  public void Var001()
  {
    setVariation(1);
    KeyedFile file1 = null;
    KeyedFile file2 = null;
    try
    {
      AS400FileRecordDescription rd = new AS400FileRecordDescription(systemObject_, fileName_);
      RecordFormat[] rfs = rd.retrieveRecordFormat();
      if (rfs.length != 2)
      {
        failed("Wrong number of record formats returned");
        return;
      }
      Object [] key = new Object[1];
      key[0] = "Record 1  ";
      file1 =
        new KeyedFile(systemObject_, fileName_);
      file1.setRecordFormat(rfs[0]);
      file1.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2 =
        new KeyedFile(systemObject_, fileName_);
      file2.setRecordFormat(rfs[1]);
      file2.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file1.read(key);
      if (!record.toString().equals(recordsFmt1_[1].toString()))
      {
        failed("Wrong record for first format: " + record.toString() + ".\n" + recordsFmt1_[1].toString() + ".");
        file1.close();
        file2.close();
        return;
      }
      record = file2.read(key);
      if (!record.toString().equals(recordsFmt2_[1].toString()))
      {
        failed("Wrong record for second format: " + record.toString());
        file1.close();
        file2.close();
        return;
      }
      file1.close();
      file2.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file1.close();
        file2.close();
      }
      catch(Exception e1) {}
      return;
    }
    succeeded();
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read, read/write or write.
   *</ul>
   **/
   public void Var002()
   {
     setVariation(2);
     SequentialFile file = null;
     SequentialFile violator = null;
     try
     {
       file = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file.lock(AS400File.READ_EXCLUSIVE_LOCK);
       violator = new SequentialFile(new AS400(systemObject_), file.getPath());
       violator.setRecordFormat(new DDMLockFormat(systemObject_));
       try
       {
         violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         failed("No exception with READ_ONLY");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           failed(e, "Wrong exception id with READ_ONLY.");
         }
         else
         {
           try
           {
             violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
             failed("Exception didn't occur with READ_WRITE.");
           }
           catch(AS400Exception e1)
           {
             msg = e1.getAS400Message();
             if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
             {
               failed(e1, "Wrong exception id with READ_WRITE.");
             }
             else
             {
               try
               {
                 violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
                 failed("Exception didn't occur with WRITE_ONLY.");
               }
               catch(AS400Exception e2)
               {
                 msg = e.getAS400Message();
                 if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
                 {
                   failed(e2, "Wrong exception id with WRITE_ONLY.");
                 }
                 else
                 {
                   succeeded();
                 }
               }
             }
           }
         }
       }
       catch(Exception e)
       {
         failed(e, "Incorrect exception.");
       }
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }
     // Release the lock obtained
     try
     {
       file.releaseExplicitLocks();
     }
     catch(Exception e)
     {
       System.out.println("Failed to release explicit locks for " + file.getPath());
     }
     // Disconnect from the system for violator.  This is to prevent the subsystem
     // from filling up with jobs.
     violator.getSystem().disconnectService(AS400.RECORDACCESS);
   }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read only.
   *</ul>
   **/
   public void Var003()
   {
     setVariation(3);
     SequentialFile file = null;
     SequentialFile violator = null;
     try
     {
       file = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file.setRecordFormat(new DDMLockFormat(systemObject_));
       file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file.lock(AS400File.READ_EXCLUSIVE_LOCK);
       violator =  new SequentialFile(new AS400(systemObject_), file.getPath());
       violator.setRecordFormat(file.getRecordFormat());
       violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       failed("Exception didn't occur.");
     }
     catch(AS400Exception e)
     {
       AS400Message msg = e.getAS400Message();
       if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
       {
         failed(e, "Wrong exception id.");
       }
       else
       {
         succeeded();
       }
     }
     catch(Exception e)
     {
       failed(e, "Incorrect exception.");
     }

     try
     {
       file.close();
     }
     catch(Exception e) {}
     // Disconnect from the system for violator.  This is to prevent the subsystem from filling
     // up with jobs.
     violator.getSystem().disconnectService(AS400.RECORDACCESS);
   }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
   public void Var004()
   {
     setVariation(4);
     SequentialFile file1 = null;
     SequentialFile file2 = null;
     SequentialFile violator = null;
     try
     {
       file1 = new SequentialFile(systemObject_,
                                  "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file1.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
       file2 = new SequentialFile(new AS400(systemObject_), file1.getPath());
       file2.setRecordFormat(new DDMLockFormat(systemObject_));
       file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       violator = new SequentialFile(new AS400(systemObject_), file1.getPath());
       violator.setRecordFormat(file2.getRecordFormat());
       try
       {
         violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         failed("Exception didn't occur.");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           failed(e, "Wrong exception id returned.");
         }
         else
         {
           succeeded();
         }
       }
       catch(Exception e)
       {
         failed(e, "Incorrect exception.");
       }
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception occurred");
     }

     // Release the lock on the file
     try
     {
       file1.releaseExplicitLocks();
     }
     catch(Exception e) {e.printStackTrace();}
     // Disconnect from the other connections
     file2.getSystem().disconnectService(AS400.RECORDACCESS);
     violator.getSystem().disconnectService(AS400.RECORDACCESS);
   }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
   public void Var005()
   {
     setVariation(5);
     SequentialFile file1 = null;
     SequentialFile file2 = null;
     SequentialFile violator = null;
     try
     {
       file1 = new SequentialFile(systemObject_,
                                  "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file1.setRecordFormat(new DDMLockFormat(systemObject_));
       file1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file1.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
       file2 = new SequentialFile(new AS400(systemObject_), file1.getPath());
       file2.setRecordFormat(file1.getRecordFormat());
       file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();

       violator = new SequentialFile(new AS400(systemObject_), file1.getPath());
       violator.setRecordFormat(file1.getRecordFormat());
       try
       {
         violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         failed("Exception didn't occur.");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           failed(e, "Wrong exception id returned.");
         }
         else
         {
           succeeded();
         }
       }
       catch(Exception e)
       {
         failed(e, "Incorrect exception.");
       }
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception occurred");
     }

     // Release the lock on the file
     try
     {
       file1.close();
     }
     catch(Exception e) {e.printStackTrace();}
     // Disconnect from the other connections
     file2.getSystem().disconnectService(AS400.RECORDACCESS);
     violator.getSystem().disconnectService(AS400.RECORDACCESS);
   }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
   **/
   public void Var006()
   {
     setVariation(6);
     SequentialFile file1 = null;
     SequentialFile file2 = null;
     try
     {
       file1 = new SequentialFile(systemObject_,
                                  "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file1.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
       file2 = new SequentialFile(new AS400(systemObject_), file1.getPath());
       file2.setRecordFormat(new DDMLockFormat(systemObject_));
       file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       succeeded();
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }
     // Disconnect from the other connection
     file2.getSystem().disconnectService(AS400.RECORDACCESS);
   }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
   **/
   public void Var007()
   {
     setVariation(7);
     SequentialFile file1 = null;
     SequentialFile file2 = null;
     try
     {
       file1 = new SequentialFile(systemObject_,
                                  "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file1.setRecordFormat(new DDMLockFormat(systemObject_));
       file1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file1.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
       file2 = new SequentialFile(new AS400(systemObject_), file1.getPath());
       file2.setRecordFormat(file1.getRecordFormat());
       file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       succeeded();
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }

     try
     {
       file1.close();
     }
     catch(Exception e) {e.printStackTrace();}
     // Disconnect from the other connection
     file2.getSystem().disconnectService(AS400.RECORDACCESS);
   }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read, read/write or write.
   *</ul>
   **/
   public void Var008()
   {
     setVariation(8);
     StringBuffer failMsg = new StringBuffer();
     SequentialFile file = null;
     SequentialFile violator = null;
     try
     {
       file = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
       violator = new SequentialFile(new AS400(systemObject_), file.getPath());
       violator.setRecordFormat(new DDMLockFormat(systemObject_));
       try
       {
         violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         failMsg.append("Exception didn't occur with READ_ONLY.");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           e.printStackTrace(output_);
           failMsg.append("Wrong exception id with READ_ONLY.");
         }
       }
       catch(Exception e)
       {
         e.printStackTrace(output_);
         failMsg.append("Incorrect exception with READ_ONLY.");
       }
       try
       {
         violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         failMsg.append("Exception didn't occur with READ_WRITE.");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           e.printStackTrace(output_);
           failMsg.append("Wrong exception id with READ_WRITE.");
         }
       }
       catch(Exception e)
       {
         e.printStackTrace(output_);
         failMsg.append("Incorrect exception with READ_WRITE.");
       }
       try
       {
         violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         failMsg.append("Exception didn't occur with WRITE_ONLY.");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           e.printStackTrace(output_);
           failMsg.append("Wrong exception id with WRITE_ONLY.");
         }
       }
       catch(Exception e)
       {
         e.printStackTrace(output_);
         failMsg.append("Incorrect exception with WRITE_ONLY.");
       }
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }
     // Release the lock obtained
     try
     {
       file.releaseExplicitLocks();
     }
     catch(Exception e)
     {
     }
     // Disconnect from the system for violator.  This is to prevent the subsystem from filling
     // up with jobs.
     violator.getSystem().disconnectService(AS400.RECORDACCESS);
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
   *Verify AS400File.lock(int) with a lock type of WRITE_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read, read/write or write.
   *</ul>
   **/
   public void Var009()
   {
     setVariation(9);
     StringBuffer failMsg = new StringBuffer();
     SequentialFile file = null;
     SequentialFile violator = null;
     try
     {
       file = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file.setRecordFormat(new DDMLockFormat(systemObject_));
       file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
       violator = new SequentialFile(new AS400(systemObject_), file.getPath());
       violator.setRecordFormat(new DDMLockFormat(systemObject_));
       try
       {
         violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         failMsg.append("Exception didn't occur with READ_ONLY.");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           e.printStackTrace(output_);
           failMsg.append("Wrong exception id with READ_ONLY.");
         }
       }
       catch(Exception e)
       {
         e.printStackTrace(output_);
         failMsg.append("Incorrect exception with READ_ONLY.");
       }
       try
       {
         violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         failMsg.append("Exception didn't occur with READ_WRITE.");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           e.printStackTrace(output_);
           failMsg.append("Wrong exception id with READ_WRITE.");
         }
       }
       catch(Exception e)
       {
         e.printStackTrace(output_);
         failMsg.append("Incorrect exception with READ_WRITE.");
       }
       try
       {
         violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         failMsg.append("Exception didn't occur with WRITE_ONLY.");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           e.printStackTrace(output_);
           failMsg.append("Wrong exception id with WRITE_ONLY.");
         }
       }
       catch(Exception e)
       {
         e.printStackTrace(output_);
         failMsg.append("Incorrect exception with WRITE_ONLY.");
       }
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }
     // Close the file
     try
     {
       file.close();
     }
     catch(Exception e)
     {
     }
     // Disconnect from the system for violator.  This is to prevent the subsystem from filling
     // up with jobs.
     violator.getSystem().disconnectService(AS400.RECORDACCESS);
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
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
   public void Var010()
   {
     setVariation(10);
     SequentialFile file1 = null;
     SequentialFile file2 = null;
     SequentialFile violator = null;
     try
     {
       file1 = new SequentialFile(systemObject_,
                                  "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file1.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
       file2 = new SequentialFile(new AS400(systemObject_), file1.getPath());
       file2.setRecordFormat(new DDMLockFormat(systemObject_));
       file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       violator = new SequentialFile(new AS400(systemObject_), file1.getPath());
       violator.setRecordFormat(file2.getRecordFormat());
       try
       {
         violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         violator.close();
         failed("Exception didn't occur.");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           failed(e, "Wrong exception id returned");
         }
         else
         {
           succeeded();
         }
       }
       catch(Exception e)
       {
         failed(e, "Incorrect exception.");
       }
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }

     try
     {
       file1.releaseExplicitLocks();
     }
     catch(Exception e) {e.printStackTrace();}
     // Disconnect from the other connections
     file2.getSystem().disconnectService(AS400.RECORDACCESS);
     violator.getSystem().disconnectService(AS400.RECORDACCESS);
   }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
   public void Var011()
   {
     setVariation(11);
     SequentialFile file1 = null;
     SequentialFile file2 = null;
     SequentialFile violator = null;
     try
     {
       file1 = new SequentialFile(systemObject_,
                                  "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file1.setRecordFormat(new DDMLockFormat(systemObject_));
       file1.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file1.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
       file2 = new SequentialFile(new AS400(systemObject_), file1.getPath());
       file2.setRecordFormat(new DDMLockFormat(systemObject_));
       file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       violator = new SequentialFile(new AS400(systemObject_), file1.getPath());
       violator.setRecordFormat(file2.getRecordFormat());
       try
       {
         violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
         violator.close();
         failed("Exception didn't occur.");
       }
       catch(AS400Exception e)
       {
         AS400Message msg = e.getAS400Message();
         if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
         {
           failed(e, "Wrong exception id returned");
         }
         else
         {
           succeeded();
         }
       }
       catch(Exception e)
       {
         failed(e, "Incorrect exception.");
       }
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }

     try
     {
       file1.close();
     }
     catch(Exception e) {e.printStackTrace();}
     // Disconnect from the other connections
     file2.getSystem().disconnectService(AS400.RECORDACCESS);
     violator.getSystem().disconnectService(AS400.RECORDACCESS);
   }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
   **/
   public void Var012()
   {
     setVariation(12);
     SequentialFile file1 = null;
     SequentialFile file2 = null;
     try
     {
       file1 = new SequentialFile(systemObject_,
                                  "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file1.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
       file2 = new SequentialFile(new AS400(systemObject_), file1.getPath());
       file2.setRecordFormat(new DDMLockFormat(systemObject_));
       file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       succeeded();
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }
     // Disconnect from the other connection
     file2.getSystem().disconnectService(AS400.RECORDACCESS);
   }

   /**
    *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_WRITE_LOCK.
    *<ul compact>
    *<li>Set the lock when the file is open.
    *</ul>
    *Expected results:
    *<ul compact>
    *<li>Another connection should be able to open the file for read, read/write or write.
    *<li>No exceptions should occur.
    *</ul>
   **/
   public void Var013()
   {
     setVariation(13);
     SequentialFile file1 = null;
     SequentialFile file2 = null;
     try
     {
       file1 = new SequentialFile(systemObject_,
                                  "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file1.setRecordFormat(new DDMLockFormat(systemObject_));
       file1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file1.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
       file2 = new SequentialFile(new AS400(systemObject_), file1.getPath());
       file2.setRecordFormat(file1.getRecordFormat());
       file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file2.close();
       succeeded();
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }

     try
     {
       file1.close();
     }
     catch(Exception e) {e.printStackTrace();}
     // Disconnect from the other connection
     file2.getSystem().disconnectService(AS400.RECORDACCESS);
   }

   /**
    *Verify AS400File.getExplicitLocks().
    *<ul compact>
    *<li>With no locks set.
    *<li>With one lock set.
    *<li>With all locks set.
    *</ul>
    *Expected results:
    *<ul compact>
    *<li>No locks set will return an int[] of size 0.
    *<li>One lock set will return an int[] of size one containing the lock that was set.
    *<li>All locks set will return an int[] of size six containing the locks that were set.
    *</ul>
    **/
   public void Var014()
   {
     setVariation(14);
     SequentialFile file = null;
     try
     {
       file = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       if (file.getExplicitLocks().length == 0)
       {
         file.lock(AS400File.READ_EXCLUSIVE_LOCK);
         int[] locks = file.getExplicitLocks();
         if (locks.length == 1 && locks[0] == AS400File.READ_EXCLUSIVE_LOCK)
         {
           file.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
           file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
           file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
           file.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
           file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
           locks = file.getExplicitLocks();
           Vector v = new Vector(locks.length);
           for (int i = 0; i < locks.length; i++)
           {
             v.addElement(new Integer(locks[i]));
           }
           assertCondition(locks.length == 6 &&
                  v.contains(new Integer(AS400File.READ_EXCLUSIVE_LOCK)) &&
                  v.contains(new Integer(AS400File.READ_ALLOW_SHARED_READ_LOCK)) &&
                  v.contains(new Integer(AS400File.READ_ALLOW_SHARED_WRITE_LOCK)) &&
                  v.contains(new Integer(AS400File.WRITE_EXCLUSIVE_LOCK)) &&
                  v.contains(new Integer(AS400File.WRITE_ALLOW_SHARED_READ_LOCK)) &&
                  v.contains(new Integer(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK)));
         }
         else
         {
           failed("Incorrect lock information.");
         }
       }
       else
       {
         failed("Incorrect lock information.");
       }
     }
     catch(Exception e)
     {
       failed(e);
     }
   }

   /**
    *Verify AS400File.releaseExplicitLocks().
    *<ul compact>
    *<li>When no locks are set and the file is closed.
    *<li>When no locks are set and the file is open.
    *</ul>
    *Expected results:
    *<ul compact>
    *<li>No exceptions are thrown.
    *</ul>
    **/
   public void Var015()
   {
     setVariation(15);
     SequentialFile file = null;
     try
     {
       file = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file.setRecordFormat(new DDMLockFormat(systemObject_));
       file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file.releaseExplicitLocks();
       file.close();
       file.releaseExplicitLocks();
       succeeded();
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }
   }

   /**
    *Verify AS400File.releaseExplicitLocks().
    *<ul compact>
    *<li>With one lock obtained and the file is closed.
    *<li>With one lock obtained and the file is open.
    *<li>With all locks obtained and the file is closed.
    *<li>With all locks obtained and the file is open.
    **/
   public void Var016()
   {
     setVariation(16);
     SequentialFile file = null;
     try
     {
       file = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/DDMTEST.LIB/DDMLOCK.FILE/MBR1.MBR");
       file.setRecordFormat(new DDMLockFormat(systemObject_));
       file.lock(AS400File.READ_EXCLUSIVE_LOCK);
       file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
       file.releaseExplicitLocks();
       if (file.getExplicitLocks().length == 0)
       {
         file.close();
         file.lock(AS400File.READ_EXCLUSIVE_LOCK);
         file.releaseExplicitLocks();
         if (file.getExplicitLocks().length == 0)
         {
           file.lock(AS400File.READ_EXCLUSIVE_LOCK);
           file.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
           file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
           file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
           file.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
           file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
           file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
           file.releaseExplicitLocks();
           if (file.getExplicitLocks().length == 0)
           {
             file.close();
             file.lock(AS400File.READ_EXCLUSIVE_LOCK);
             file.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
             file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
             file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
             file.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
             file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
             file.releaseExplicitLocks();
             assertCondition(file.getExplicitLocks().length == 0);
           }
           else
           {
             failed("Incorrect lock information.");
           }
         }
         else
         {
           failed("Incorrect lock information.");
         }
       }
       else
       {
         failed("Incorrect lock information.");
       }
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }
   }

  /**
   *Verify record locks when file opened for read_only with
   *commitment control locking of *CS.<br>
   *Note: This is an attended variation.
   *<ul>
   *<li>SequentialFile
   *</ul>
   *Expected results:
   *<ul>
   *<li>A record lock of *READ will be held on a read record until
   *the cursor is moved from that record.
   *</ul>
  **/
  public void Var017()
  {
    setVariation(17);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      // Populate the file
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[5];
      for (int i = 0; i < 5; ++i)
      {
        recs[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(recs);
      file.close();

      // Start commitment control for cursor stability
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.readFirst();
      System.out.println("Does a *READ record lock exist on record number 1 of file DDMLOCK in " + "DDMTest" + "(Y/N)? (DSPRCDLCK)");
      InputStreamReader r = new InputStreamReader(System.in);
      BufferedReader inBuf = new BufferedReader(r);
      String resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.readNext();
      System.out.println("Does a *READ record lock exist on record number 2 of file DDMLOCK in " + "DDMTest" + "(Y/N)? (DSPRCDLCK)");
      resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.commit();
      file.close();
      file.endCommitmentControl();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      catch(Exception e1)
      {}
    }
    succeeded();
  }

  /**
   *Verify record locks when file opened for read_only with
   *commitment control locking of *ALL.<br>
   *Note: This is an attended variation.
   *<ul>
   *<li>SequentialFile
   *</ul>
   *Expected results:
   *<ul>
   *<li>A record lock of *READ will be held on a read record until
   *the cursor is moved from that record.
   *</ul>
  **/
  public void Var018()
  {
    setVariation(18);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      // Populate the file
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[5];
      for (int i = 0; i < 5; ++i)
      {
        recs[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(recs);
      file.close();

      // Start commitment control for cursor stability
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.readFirst();
      System.out.println("Does a *READ record lock exist on record number 1 of file DDMLOCK in " + "DDMTest" + "(Y/N)? (DSPRCDLCK)");
      InputStreamReader r = new InputStreamReader(System.in);
      BufferedReader inBuf = new BufferedReader(r);
      String resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.readNext();
      System.out.println("Does a *READ record lock exist on record number 2 of file DDMLOCK in " + "DDMTest" + "(Y/N)? (DSPRCDLCK)");
      resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.commit();
      file.close();
      file.endCommitmentControl();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      catch(Exception e1)
      {}
    }
    succeeded();
  }

  /**
   *Verify record locks when file opened for read_only with
   *commitment control locking of *CS.<br>
   *Note: This is an attended variation.
   *<ul>
   *<li>KeyedFile
   *</ul>
   *Expected results:
   *<ul>
   *<li>A record lock of *READ will be held on a read record until
   *the cursor is moved from that record.
   *</ul>
  **/
  public void Var019()
  {
    setVariation(19);
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      // Populate the file
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[5];
      for (int i = 0; i < 5; ++i)
      {
        recs[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(recs);
      file.close();

      // Start commitment control for cursor stability
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.readFirst();
      System.out.println("Does a *READ record lock exist on record number 1 of file DDMLOCK in " + "DDMTest" + "(Y/N)? (DSPRCDLCK)");
      InputStreamReader r = new InputStreamReader(System.in);
      BufferedReader inBuf = new BufferedReader(r);
      String resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.readNext();
      System.out.println("Does a *READ record lock exist on record number 2 of file DDMLOCK in " + "DDMTest" + "(Y/N)? (DSPRCDLCK)");
      resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.commit();
      file.close();
      file.endCommitmentControl();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      catch(Exception e1)
      {}
    }
    succeeded();
  }

  /**
   *Verify record locks when file opened for read_only with
   *commitment control locking of *ALL.<br>
   *Note: This is an attended variation.
   *<ul>
   *<li>KeyedFile
   *</ul>
   *Expected results:
   *<ul>
   *<li>A record lock of *READ will be held on a read record until
   *the cursor is moved from that record.
   *</ul>
  **/
  public void Var020()
  {
    setVariation(20);
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      // Populate the file
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[5];
      for (int i = 0; i < 5; ++i)
      {
        recs[i] = file.getRecordFormat().getNewRecord();
      }
      file.write(recs);
      file.close();

      // Start commitment control for cursor stability
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.readFirst();
      System.out.println("Does a *READ record lock exist on record number 1 of file DDMLOCK in " + "DDMTest" + "(Y/N)? (DSPRCDLCK)");
      InputStreamReader r = new InputStreamReader(System.in);
      BufferedReader inBuf = new BufferedReader(r);
      String resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.readNext();
      System.out.println("Does a *READ record lock exist on record number 2 of file DDMLOCK in " + "DDMTest" + "(Y/N)? (DSPRCDLCK)");
      resp = inBuf.readLine();
      if (!resp.equals("y") && !resp.equals("Y"))
      {
        failed("Verification of record lock after readFirst failed");
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      file.commit();
      file.close();
      file.endCommitmentControl();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        file.commit();
        file.close();
        file.endCommitmentControl();
        return;
      }
      catch(Exception e1)
      {}
    }
    succeeded();
  }
}


