///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ComponentThread.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;


import java.io.PipedOutputStream;
import java.io.PipedInputStream;
import java.io.PrintWriter;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.Thread;


public abstract class ComponentThread
  extends Thread
{

  protected PipedOutputStream   pipeOutput_    = null;  // pipe to send messages from threads.
  protected ObjectOutputStream  objectOutput_  = null;  // enables object serialization on pipe.
  protected int                 numLoops_      = 10;    // times to perform function.
  protected int                 function_      = 0;     // function to perform.
  protected boolean             stop_          = false; // loop sentinal.
  protected ThreadedTestcase    testcase_      = null;  // allows for simultaneous thread starting.
  protected PrintWriter         output_        = null;  // for output that can't be sent through pipe.


 /**
  * Constructs a new ComponentThread.
  * @param pipeReader stream that will read what this thread
  *                   writes to the pipe.
  * @param output     any messages that can't be written to pipe
  *                   are written here.
  * @param funt       function that this thread should perform.
  * @param tc         Testcase that created this thread.  It can
  *                   be used to sync thread starting.
  */
  public ComponentThread(PipedInputStream pipeReader, PrintWriter output,
                        ThreadedTestcase tc, int func)
  {
    try
    {
      // create a new PipeOutputStream connected to pipeReader.
      pipeOutput_   = new PipedOutputStream(pipeReader);
      // create an ObjectOutput stream for serialization
      objectOutput_ = new ObjectOutputStream(pipeOutput_);
      function_     = func;
      testcase_     = tc;
      output_       = output;
    }
    catch(IOException e)
    {
      // not connected to pipe. Signal error to stdout.
      output_.print("Pipe Construction failed");
    }
  }


 /**
  * Sets the # of Loops that the function_ will be performed for.
  * @param numLoops times to execute the task specified by function_
  */
  public void setNumLoops(int numLoops)
  {
    if (numLoops >= 0)
      numLoops_ = numLoops;
  }


 /**
  * Writes an error message to the pipe and then
  * sets the stop_ flag.
  * @param errorMessage error sring to be written to the pipe.
  */
  public void error(String errorMessage)
  {
    try
    {
      objectOutput_.writeObject(errorMessage);
      // thread stops after error has occurred.
      stop_ = true;
    }
    catch(IOException e)
    {
      // if we failed it means we can't communicate thru pipe
      // attempt to print message to stdout.
      output_.println("write to pipe failed due to IOException:");
      output_.print(e.getMessage());
      output_.println("Original error was:");
      output_.print(errorMessage);
    }
  }


 /**
  * Writes an error message and exception to the pipe and then
  * sets the stop_ flag.
  * @param errorMessage error string to be written to the pipe.
  * @param exception    exception to write to pipe.
  */
  public void error(String errorMessage, Exception exception)
  {
    try
    {
      // while each writeObject() is atomic we need both this
      // message and the exception to be written in sequence
      // else the reader may have mismatched message/exceptions.
      synchronized(objectOutput_)
      {
        objectOutput_.writeObject(errorMessage);
        objectOutput_.writeObject(exception);
      }
    }
    catch(IOException e)
    {
      // if we failed it means we can't communicate thru pipe
      // attempt to print message to stdout.
      output_.println("write to pipe failed due to IOException:");
      output_.print(e.getMessage());
      output_.println("Original error was:");
      output_.print(errorMessage);
    }
    finally
    {
      // thread stops after error has occurred.
      stop_ = true;
    }

  }



 /**
  * Used by the testcase to stop threads execution.
  * Sets the stop sentinal for the thread to stop 
  * looping and have it call it's kill method.  
  */
  public void stopThread()
  {
	  stop_ = true;
  }


 /**
  * Closes all streams in use and stops this threads execution.
  */
  protected void kill()
  {
    try
    {
output_.println(getName()+" in kill");
      // close all streams.
      objectOutput_.close();
output_.println(getName()+" objectOutput_ closed in kill");
      pipeOutput_.close();
output_.println(getName()+" pipeOutput_ closed in kill");
    }
    catch(IOException e)
    {
      output_.println("Couldn't close pipeOutput:");
      output_.println(e.getMessage());
    }
    finally
    {
      // halt execution.
///output_.println(getName()+" calling stop in kill");
      ///stop();
    }
  }
}
