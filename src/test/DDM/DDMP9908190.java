///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMP9908190.java
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
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.SequentialFile;

import test.TestDriverStatic;
import test.Testcase;

/**
 *Testcase DDMP9908190. Verify fix for P9908190. This is to make sure that:
 *<OL>
 *<LI>A CPF3130 "Member xxx already in use." does not occur when retrieving record formats across multiple threads.
 *</OL>
**/
public class DDMP9908190 extends Testcase implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMP9908190";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  private boolean brief_;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMP9908190(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMP9908190", 1,
          variationsToRun, runMode, fileOutputStream);
    setTestLib(testLib);
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
      Var001();
    }
    systemObject_.disconnectAllServices();
  }

  /**
   *Verify the AS400Text's ccsid does not change.
   *Expected results:
   *<ul compact>
   *<li>The variation should return success.
   *</ul>
  **/
  public void Var001()
  {
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    try
    {
      output_.println("This variation will take approximately 60 seconds to complete.");
      systemObject_.connectService(AS400.RECORDACCESS);
      RetrieverThread obj1 = new RetrieverThread(systemObject_);
      RetrieverThread obj2 = new RetrieverThread(systemObject_);
      Thread t1 = new Thread(obj1);
      Thread t2 = new Thread(obj2);
      Thread t3 = new Thread(obj1);
      output_.println("Starting thread 1 of 3...");
      t1.start();
      output_.println("Starting thread 2 of 3...");
      t2.start();
      output_.println("Starting thread 3 of 3...");
      t3.start();

      for (int i=0; i<6; ++i)
      {
        Thread.sleep(10000);
        if (!t1.isAlive() && !t2.isAlive() && !t3.isAlive())
        {
          i=6; // quit early
        }
      }

      if (!t1.isAlive() || obj1.alreadyDone_ ||
          !t2.isAlive() || obj2.alreadyDone_ ||
          !t3.isAlive())
      {
        failed("One or more threads have died from exceptions.");
      }
      else
      {
        succeeded();
      } 

      obj1.keepGoing_ = false;
      obj2.keepGoing_ = false;

      output_.println("Waiting for threads to join...");
      t1.join();
      t2.join();
      t3.join();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
       // Cleanup.
       try
       {
         AS400File.endCommitmentControl(systemObject_);
       }
       catch(Exception e)
       {
         output_.println("Unable to end commitment control: "+e.toString());
       }
       try
       {
            SequentialFile f = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
            f.endCommitmentControl();
            f.close(); 
       }
       catch(Exception e)
       {
         output_.println("Unable to end commitment control for QCUSTCDT: "+e.toString());
       }
       try
       {
         systemObject_.disconnectService(AS400.RECORDACCESS);
       }
       catch(Exception e)
       {
         output_.println("Unable to disconnect RLA: "+e.toString());
       }
    }
  }



  static boolean started_ = false;


  class RetrieverThread implements Runnable
  {
    public boolean keepGoing_ = true;
    public boolean alreadyDone_ = false;

    AS400 sys_ = null;

    public RetrieverThread(AS400 sys)
    {
      sys_ = sys;
    }

    public void run()
    {
      try
      {
        while(keepGoing_)
        {  
          AS400FileRecordDescription frd1 = new AS400FileRecordDescription(sys_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
          frd1.retrieveRecordFormat();
          if (!started_)
          {
            started_ = true;
            SequentialFile f = new SequentialFile(sys_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
            f.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
            f.close(); 
          }
        }
      }
      catch(Exception e)
      {
        output_.println("ERROR on "+Thread.currentThread());
        e.printStackTrace();
      }
      alreadyDone_ = true;
    }
  }
   

}
