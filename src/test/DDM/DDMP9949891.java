///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMP9949891.java
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
import com.ibm.as400.access.*;

import test.TestDriverStatic;
import test.Testcase;

import java.sql.*;

/**
 *Testcase DDMP9949891. Verify the fix in P9949891. This is to make sure that:
 *<OL>
 *<LI>Test to make sure that multiple threads sharing an AS400 object do not
 *    crash each other when they try to call positionCursorBeforeFirst(),
 *    positionCursorAfterLast(), update(), and write().
 *</OL>
**/
public class DDMP9949891 extends Testcase implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMP9949891";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  String testLib_ = null;
  static final int numThreads_ = 4;

  private boolean brief_ = false; 
  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMP9949891(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib,
                         String password,
                         AS400 pwrSys)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMP9949891", 4,
          variationsToRun, runMode, fileOutputStream, password);
    if (testLib.length()>9) {
	testLib = testLib.substring(0,9); 
    }
    testLib+="Z";

    testLib_ = testLib;

    pwrSys_ = pwrSys;

    brief_ = TestDriverStatic.brief_; 
  }


  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      runTest(RepeatDDMOperation.WRITE);
    }
    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      runTest(RepeatDDMOperation.BEFOREFIRST);
    }
    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      runTest(RepeatDDMOperation.AFTERLAST);
    }
    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      runTest(RepeatDDMOperation.UPDATE);
    }
    systemObject_.disconnectAllServices();
  }


  static RecordFormat format_ = new RecordFormat("MYRF");
  static
  {
    format_.addFieldDescription(new CharacterFieldDescription(new AS400Text(1000, 37), "FIELD1"));
  }

  static class RepeatDDMOperation implements Runnable
  {
    boolean keepRunning_ = true;
    int operation_;
    AS400 sys_ = null;
    SequentialFile file_ = null;
    Throwable savedException_ = null;

    static final int WRITE = 0x1776;
    static final int BEFOREFIRST = 0x1492;
    static final int UPDATE = 0x1867;
    static final int AFTERLAST = 0x2001;

    public RepeatDDMOperation(AS400 sys, SequentialFile file, int operation)
    {
      sys_ = sys;
      file_ = file;
      operation_ = operation;
    }

    public void run()
    {
      //System.out.println("DDMOperation thread running ("+filename_+").");
      SequentialFile sf = file_;
      try
      {
        Record r = null;
        switch(operation_)
        {
          case WRITE:
            while (keepRunning_)
            {
              r = format_.getNewRecord();
              r.setField(0, "Test write(Record).");
              file_.write(r);
              Thread.sleep(20);
            }
            break;
          case BEFOREFIRST:
            r = format_.getNewRecord();
            r.setField(0, "Test positionCursorBeforeFirst().");
            file_.write(r);
            while (keepRunning_)
            {
              sf.positionCursorBeforeFirst();
              Thread.sleep(20);
            }
            break;
          case AFTERLAST:
            r = format_.getNewRecord();
            r.setField(0, "Test positionCursorAfterLast().");
            file_.write(r);
            while (keepRunning_)
            {
              sf.positionCursorAfterLast();
              Thread.sleep(20);
            }
            break;
          case UPDATE:
            r = format_.getNewRecord();
            r.setField(0, "Test update(Record).");
            file_.write(r);
            file_.write(r);
            r.setField(0, "Test update(Record) - 2nd record.");
            while (keepRunning_)
            {
              file_.positionCursorToFirst();
              file_.readNext();
              file_.update(r);
              Thread.sleep(20);
            }
            break;
          default:
            throw new Exception("Unknown operation -- "+operation_);
        }
      }
      catch(Throwable t)
      {
        savedException_ = t;
      }
      finally
      {
        try
        {
          file_.close();
        }
        catch(Exception e) {}
      }
    }
  }

          
  /**
   *Verify no exceptions are thrown for multi-threaded write().
   *Expected results:
   *<ul compact>
   *<li>The variation should return success.
   *</ul>
  **/
  public void runTest(int operation)
  {

      if (brief_) {
	  notApplicable("brief_ specified");
	  return; 
      } 
 
    try
    {


      if (pwrSys_ == null)
      {
        output_.println("pwrSys not specified.");
        return;
      }
      output_.println("This variation will take approximately 30+ seconds to complete.");
      CommandCall cc = new CommandCall(pwrSys_);
      deleteLibrary(cc, testLib_);
      cc = new CommandCall(systemObject_);
      cc.run("CRTLIB "+testLib_);

      // Test using 8 threads (yes, I just picked a number)
      RepeatDDMOperation[] objects = new RepeatDDMOperation[numThreads_];
      Thread[] threads = new Thread[numThreads_];
      for (int i=0; i<numThreads_; ++i)
      {
        String name = "/QSYS.LIB/"+testLib_+".LIB/THDTST"+i+".FILE/THDTST"+i+".MBR";
        SequentialFile sf = new SequentialFile(systemObject_, name);
        try { sf.delete(); } catch(Exception e) {}
        sf.setRecordFormat(format_);
        sf.create(format_, "Test file for DDMP9949891.");
        sf.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
        objects[i] = new RepeatDDMOperation(systemObject_, sf, operation);
      }
      output_.println("Starting "+numThreads_+" threads.");
      for (int i=0; i<numThreads_; ++i)
      {
        threads[i] = new Thread(objects[i]);
        threads[i].setDaemon(true); // Just in case
        threads[i].start();
      }
      Thread timerThread = new Thread(new Runnable()
      {
        public void run()
        {
          //output_.println("Timer thread running.");
          try { Thread.sleep(30000); } catch(Exception e) {} // Sleep for 30 seconds
          synchronized(DDMP9949891.this)
          {
            //output_.println("Notifying...");
            DDMP9949891.this.notifyAll();
          }
        }
      });
      timerThread.setDaemon(true); // Just in case
      timerThread.start();
      synchronized(this)
      {
        this.wait(); // We'll wake up when the timer notifies us
      }
      //output_.println("Awake.");
      String failStr = "";
      for (int i=0; i<numThreads_; ++i)
      {
        objects[i].keepRunning_ = false;
        threads[i].join(1000);
        if (objects[i].savedException_ != null)
        {
          failStr += "Thread "+i+" caught "+objects[i].savedException_.toString()+"\n";
          objects[i].savedException_.printStackTrace();
        }
      }
      if (failStr.length() > 0)
      {
        failed("Multi-threaded test failed:\n"+failStr);
      }
      else
      {
        succeeded();
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
}
