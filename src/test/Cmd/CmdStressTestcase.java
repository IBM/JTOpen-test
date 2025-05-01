///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CmdStressTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.Cmd;

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Trace;

import test.ProxyStressTest;
import test.TestDriver;

public class CmdStressTestcase extends ProxyStressTest implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "CmdStressTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ProxyStressTest.main(newArgs); 
   }
    // Private variables
    private Thread thread_;
    private boolean isRunning_;
    private int curntThread_;

    AS400Message[] msglist_;

    CommandCall crt_;
    CommandCall dlt_;

    /**
     Constructor.
     **/
    public CmdStressTestcase(int n)
    {
        curntThread_ = n;
        thread_ = null;
        crt_ = new CommandCall(sys_, "crtlib fred" + n);
        dlt_ = new CommandCall(sys_);
    }

    /**
     Cleanup:  deletes the libraries created on the AS/400.
     **/
    public void cleanup()
    {
        try
        {
            System.out.println("     Deleting CmdCall libraries..." + "(t" + curntThread_ + ")");
	    String deleteResult = TestDriver.deleteLibrary(dlt_,"fred"+curntThread_); 
	    if (deleteResult != null )
            {
                msglist_ = dlt_.getMessageList();
                System.out.println("       Cleanup Failed."  + "(t" + curntThread_ + ")");
                Trace.log(Trace.ERROR, msglist_[0].getID() + " " + msglist_[0].getText());
            }
        }
        catch(Exception e)
        {
            System.out.println("      Exception during cleanup." + "(t" + curntThread_ + ")");
            Trace.log(Trace.ERROR, e);
        }
    }

    /**
     Runs the current thread_
     **/
    @SuppressWarnings("deprecation")
    public void run()
    {
        int i;
        // While thread_ is still running.
        while (isRunning_)
        {
            // Loop until user specified maxIterations is reached.
            for (i = 1; i <= maxIterations_; ++i)
            {
                try
                {
                    System.out.println("\n   Loop #: " + i + " (current thread: " + curntThread_ + ")");
                    System.out.println("     Creating CmdCall libraries..." + "(t" + curntThread_ + ")");
                    if (crt_.run() != true)
                    {
                        msglist_ = crt_.getMessageList();
                        if (msglist_[0].getID().equals("CPF2111"))
                        {    // Do nothing if the library already exists.
                        }
                        else
                        {
                            System.out.println("       Create Failed." + "(t" + curntThread_ + ")");
                            System.out.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");
                            Trace.log(Trace.ERROR, "     CurrentThread: " + curntThread_ + " Iteration: " + i);
                            Trace.log(Trace.ERROR, "     Message: " + msglist_[0].getID() + " " + msglist_[0].getText() );
                        }
                    }
                    else
                    {
                        // Get the messages to ensure correct return code.
                        msglist_ = crt_.getMessageList();
                        if (msglist_[0].getID().equals("CPC2102"))
                        {
                            if (!msglist_[0].getText().equalsIgnoreCase("Library FRED" + curntThread_ + " created."))
                            {
                                System.out.println("         Unexpected Message." + "(t" + curntThread_ + ")");
                                System.out.println("   Loop #" + i + ": FAILED" + " (t" + curntThread_ + ")");

                                Trace.log(Trace.ERROR, msglist_[0].getID() + " " + msglist_[0].getText() );
                            }
                            else
                            {
                                System.out.println("   Loop #" + i + ":  Successful"+ " (t" + curntThread_ + ")");
                            }
                        }
                        else
                        {
                            System.out.println("       Unexpected Message." + "(t" + curntThread_ + ")");
                            System.out.println("   Loop #" + i + ": FAILED");
                            Trace.log(Trace.ERROR, msglist_[0].getID() + " " + msglist_[0].getText() );
                        }
                    }
                }
                catch(Exception e)
                {
                    System.out.println("       Unexpected Exception." + "(t" + curntThread_ + ")");
                    System.out.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");
                    if (Trace.isTraceOn())
                        Trace.log(Trace.ERROR, e);
                }
                cleanup();
                if (i == maxIterations_)
                {
                    thread_.stop();
                }
            }
        }
    }

    /**
     Starts the current thread_
     **/
    public void start()
    {
        if (thread_ == null)
        {
            thread_ = new Thread(this);
        }
        isRunning_ = true;
        thread_.start();
    }

    /**
     Stops the current thread_
     **/
    @SuppressWarnings("deprecation")
    public void stop()
    {
        if ((thread_ != null) && thread_.isAlive())
        {
            thread_.stop();
        }
        thread_ = null;
        isRunning_ = false;
    }
}
