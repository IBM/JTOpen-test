///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ThreadedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.PipedInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;
import com.ibm.as400.access.AS400;


public abstract class ThreadedTestcase
  extends Testcase
{
  public static final boolean DEBUG             = true;

  protected PipedInputStream  pipeInput_        = null; // pipe to recieve messages from threads.
  protected ObjectInputStream objectInput_      = null; // enable objects to be read from pipe.

  private   int               readyThreadCount  = 0;    // # of threads ready to be run.


 /**
  * Constructs a ThreadedTestcase object.
  * @param systemObject the AS400 system
  * @param name the testcase name
  * @param totalVariations the total number of variations in this testcase
  * @param variationsToRun the list of variations to be run
  * @param runMode which testcases to run (attended, unattended, both)
  * @param fileOutputStream the output stream to write results to
  * @param consoleOutputStream the output stream to write results to
  */
  protected ThreadedTestcase(AS400            systemObject,
                             String           name,
                             int              totalVariations,
                             Vector           variationsToRun,
                             int              runMode,
                             FileOutputStream fileOutputStream)
  {
    this(systemObject, name, totalVariations, variationsToRun, runMode,
         fileOutputStream,  null);
  }


 /**
  * Constructs a ThreadedTestcase object.
  * @param systemObject the AS400 system
  * @param name the testcase name
  * @param totalVariations the total number of variations in this testcase
  * @param variationsToRun the list of variations to be run
  * @param runMode which testcases to run (attended, unattended, both)
  * @param fileOutputStream the output stream to write results to
  * @param consoleOutputStream the output stream to write results to
  * @param password the password for the current user ID (if specified via the -pwd argument)
  */
  protected ThreadedTestcase(AS400            systemObject,
                             String           name,
                             int              totalVariations,
                             Vector           variationsToRun,
                             int              runMode,
                             FileOutputStream fileOutputStream,
                             String           password)
  {
    super(systemObject, name, totalVariations, variationsToRun, runMode,
         fileOutputStream,  password);
  }


 /**
  * Handles errors that may have occurred in threads.
  * If the thread posted an error message only, then
  * call testcase.failed(String) using this message.
  * if the thread posted both an error message and the
  * exception that occurred then call testcase.failed(
  * Exception, String).  If there are no objects to
  * retrieve and we have reached EOF then this was a
  * successful execution and Testcase.success() is
  * called.
  */
  public void handleError()
  {
    try
    {
      String message      = null;
      Exception exception = null;

      // Block waiting for data. If threads have no
      // errors they will close their pipes w/o writing
      // and an EOFException will be thrown.
      message = (String) objectInput_.readObject();

      // if more data is available then thread sent an exception.
      if ( objectInput_.available() > 0 )
      {
        exception = (Exception) objectInput_.readObject();
        failed(exception, message);
      }
      else
        // thread wrote no exception info to pipe.
        failed(message);
    }
    catch(EOFException eof)
    {
      // writers closed their end of pipe.  No errors occurred
      succeeded();
    }
    catch(Exception e)
    {
      // Unexpected Exception
      output_.println(e.getMessage());
    }
  }


 /**
  * Should be called at the start of the run() method in
  * a ComponentThread subclass.  Causes the thread to
  * wait until the other threads have been initialized
  * before starting.  The testcase's variation must
  * call ThreadedTestcase.go() to allow the threads to go.
  */
  public synchronized void ready()
  {
    try
    {
      readyThreadCount++;
      wait();
    }
    catch (InterruptedException e) {}
  }


 /**
  * Starts all of the waiting ComponentThreads.  Should
  * be called from the ThreadedTestcase's variation after
  * all the ComponentThreads in the current variation
  * have had their start() methods invoked.
  */
  public synchronized void go()
  {
    // generate a count of all the component threads.
    //  note that a thread is reflected in activeCount() and enumerate()
    //  immeadiately after construction not after start() is called.
    int         componentThreadCount = 0;
    ThreadGroup threadGroup          = Thread.currentThread().getThreadGroup();
    Thread[]    threadArray          = new Thread[threadGroup.activeCount()];
    int         totalThreadCount     = threadGroup.enumerate(threadArray);
    for (int i=0; i<totalThreadCount; i++)
      if ((threadArray[i] instanceof ComponentThread) &&
          (threadArray[i].isAlive()) )
        componentThreadCount++;

    // wait until ComponentThreads in question have wait()ed.
    while (readyThreadCount != componentThreadCount)
      try { wait(25); } catch (InterruptedException e) {}

    // simultaneously start all of the waiting ComponentThreads.
    notifyAll();
    // reset readyThreadCount
    readyThreadCount = 0;
  }


 /**
  * Generates a list of all the threads in the threadgroup.
  * All of the ComponentThreads in this list are killed.
  * This method blocks until all off the ComponentThreads have
  * actually stopped.
  * @param threadGroup All ComponentThreads contained in this
  *                    group will be stopped.
  */
  // this should be "private protected"
  protected void stopThreads(ThreadGroup threadGroup)
  {
output_.println("start of stopThreads");
    Thread[] ctArray = new Thread[threadGroup.activeCount()];
    // enumerate to generate all threads and save actual number of threads.
    int numThreads = threadGroup.enumerate(ctArray);
    // kill all the componentthreads.
    for (int i=0; i<numThreads; i++)
    {
output_.println("in for i="+i+" of stopThreads");
      // only kill ComponentThreads.
      if (ctArray[i] instanceof ComponentThread)
      {
output_.println("killing "+ctArray[i].getName());
        ((ComponentThread) ctArray[i]).stopThread();
      }
    }

output_.println("done sending kills to CT's in stopThreads");

    // wait until threads have actually stopped.
    for (int i=0; i<numThreads; i++)
      // only join the componentthreads just told to stop.
      if (ctArray[i] instanceof ComponentThread)
      {
output_.println("Testcase joining with "+ctArray[i].getName());
        try { ctArray[i].join(); } catch (InterruptedException e) {}
      }


    // simple wait that involves no timer
    // wait until component threads have been destroyed
    // note: this block hangs occasionally when a ct isn't
    // killed by jvm.
    /*    boolean componentThreadExists;
    do
    {
      ctArray = new Thread[threadGroup.activeCount()];
      // enumerate to generate all threads and save actual number of threads.
      numThreads = threadGroup.enumerate(ctArray);

      int ctCount = 0;
      componentThreadExists = false;
      for (int i=0; i<numThreads; i++)
        if (ctArray[i] instanceof ComponentThread)
        {
          componentThreadExists = true;
          ctCount++;
        }
      output_.println("waiting for CTs to die: "+ctCount);
    } while (componentThreadExists);
    */

    // wait until component threads have been destroyed.
    // If a component thread is still around 10 seconds
    // after having it's stop called then call destroy()
    // which kills the thread immeadiately w/o cleanup.
    /*    long startTime = System.currentTimeMillis();
    boolean componentThreadExists;
    do
    {
      ctArray = new Thread[threadGroup.activeCount()];
      // enumerate to generate all threads and save actual number of threads.
      numThreads = threadGroup.enumerate(ctArray);

      int ctCount = 0;
      componentThreadExists = false;
      for (int i=0; i<numThreads; i++)
        if (ctArray[i] instanceof ComponentThread)
        {
          // if thread hasn't stopped w/in 10 secs destroy it
          // note: destroy is bad idea as lock aren't released
          // by the destroyed thread.
          if ( ( System.currentTimeMillis() - startTime ) > 10000)
            ctArray[i].destroy();

          componentThreadExists = true;
          ctCount++;
        }
      output_.println("waiting for CTs to die: "+ctCount);
    } while (componentThreadExists);
    */

output_.println("closing pipe");
    // now that all threads are dead, close pipe
    try
    {
      objectInput_.close();
      pipeInput_.close();
    }
    catch(IOException e)
    {
      output_.println("Unable to close pipe");
    }
output_.println("pipe closed");
  }


 /**
  * Generates a list of all the threads in this the threadgroup.
  * All of the ComponentThreads in this list are killed.
  * This method blocks until all off the ComponentThreads have
  * actually stopped.
  */
  // this should be "private protected" but symantic cafe gacks on that.
  protected void stopThreads()
  {
    stopThreads(Thread.currentThread().getThreadGroup());
  }
}


