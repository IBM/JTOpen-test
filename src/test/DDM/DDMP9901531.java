///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMP9901531.java
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
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

/**
 *Testcase DDMP9901531.  Verify fix for P9901531. This is to make sure
 * the correlation ids in the datastreams wrap to 0 if they reach 32767.
 * AKA DDM shouldn't hang after doing 32767 datastream sends.
**/
public class DDMP9901531 extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMP9901531";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMP9901531(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMP9901531", 1,
          variationsToRun, runMode, fileOutputStream);
    setTestLib(testLib);
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
   *Verify many DDM operations do not hang the DDM code by running out of correlation ids.
   *Expected results:
   *<ul compact>
   *<li>The variation should return success.
   *</ul>
  **/
  public void Var001()
  {
    if (systemObject_.getProxyServer().length() != 0)
    {
      notApplicable("Running proxified.");
      return;
    }    

    try
    {
      CorrelationIDThread helper = new CorrelationIDThread(systemObject_);
      Thread t = new Thread(helper);
      System.out.println("Starting helper thread...");
      t.start();
      // We wait until t is started
      while (!helper.started_) { Thread.sleep(200); }
      // Give it a chance to do something
      Thread.sleep(10000); // sleep for 10 seconds
      int oldval = 0;
      int newval = helper.counter_;
      int numSleeps = 0;
      while(newval > oldval && newval != helper.counterMax_) // while the helper is still going
      {
        oldval = helper.counter_;
        Thread.sleep(10000); // sleep for 10 seconds
        numSleeps++;
        newval = helper.counter_;
        int secondsLeft = ((helper.counterMax_ - newval) * (numSleeps*10)) / newval;
        System.out.println(((newval*100)/helper.counterMax_)+"% complete. Remaining: "+secondsLeft/60+" min "+secondsLeft % 60+" sec");
      }
      if (newval == helper.counterMax_)
      {
        succeeded();
        t.join(2000); // wait 2 seconds for the thread to die 'cause we're nice
      }
      else
      {
        // The app could be hung, or it could have thrown an exception
        failed("Timed out waiting for SequentialFile access at "+newval+"/"+helper.counterMax_);
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  
  
  class CorrelationIDThread implements Runnable
  {
    private SequentialFile f = null;
    
    public int counter_ = 0;
    public final int counterMax_ = 17000; // this is high enough to hit the 32767 mark since we do 2 ops per iteration
    public boolean started_ = false;

    CorrelationIDThread(AS400 systemObject_) throws Exception
    {
      systemObject_.connectService(AS400.RECORDACCESS);        
      f = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
      f.setRecordFormat();
      f.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    }
    
    public void run()
    {
      try
      {
        f.readNext();

        started_ = true;
        System.out.println("Helper thread started.");

        for (counter_=0; counter_ < counterMax_; ++counter_)
        {
          f.readNext();
          f.readPrevious();
        }
      }
      catch(Exception e) {}
      finally
      {
        try { f.close(); } catch(Exception f) {}
        // Make sure the main thread knows that we started
        started_ = true;
      }
    }
  }  

}
