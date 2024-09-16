///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ServicePgmCallStressTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.ServiceProgram;

import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.ServiceProgramCall;
import com.ibm.as400.access.Trace;

import test.ProxyStressTest;

public class ServicePgmCallStressTestcase extends ProxyStressTest implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ServicePgmCallStressTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ProxyStressTest.main(newArgs); 
   }
    // Private variables.
    private Thread thread_;
    private boolean isRunning_;
    private int curntThread_;
    private String serviceProgramName_ = "/QSYS.LIB/JAVASP.LIB/ENTRYPTS.SRVPGM";

    AS400Message[] msglist_;
    AS400Bin4 bin4_;
    ProgramParameter[] paramList_;

    /**
     Constructor
     **/
    public ServicePgmCallStressTestcase(int n)
    {
        curntThread_ = n;
        thread_ = null;

        paramList_ = new ProgramParameter[7];
        bin4_ = new AS400Bin4();
    }

    /**
     Runs the current thread_
     **/
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

                    for (int j = 0; j < 7; ++j)
                    {
                        byte[] parm = bin4_.toBytes(j + 1);
                        ProgramParameter p = new ProgramParameter(parm);
                        p.setParameterType(ProgramParameter.PASS_BY_VALUE);
                        paramList_[j] = new ProgramParameter(parm);
                    }

                    ServiceProgramCall s = new ServiceProgramCall();

                    System.out.println("     Running ServiceProgramCall..." + "(t" + curntThread_ + ")");

                    if (s.run(sys_, serviceProgramName_, "i_iiiiiii", ServiceProgramCall.RETURN_INTEGER, paramList_))
                    {
                        Integer I = (Integer) bin4_.toObject(s.getReturnValue(), 0);
                        int k = I.intValue();
                        if (k != 28)     // If run successful, but incorrect return data.
                        {
                            System.out.println("     Incorrect data returned.");
                            System.out.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");

                            AS400Message[] messageList = s.getMessageList();
                            for (int msg = 0; msg < messageList.length; ++msg)
                            {
                                if (Trace.isTraceOn()) Trace.log(Trace.ERROR, messageList[msg].toString());
                            }
                        }
                        else
                        {
                            System.out.println("   Loop #" + i + ": Successful" + "(t" + curntThread_ + ")");
                        }
                    }
                    else    // Run failed.
                    {
                        System.out.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");

                        AS400Message[] messageList = s.getMessageList();
                        for (int msg = 0; msg < messageList.length; ++msg)
                        {
                            if (Trace.isTraceOn()) Trace.log(Trace.ERROR, messageList[msg].toString());
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("       Unexpected Exception." + "(t" + curntThread_ + ")");
                    System.out.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");
                    if (Trace.isTraceOn()) Trace.log(Trace.ERROR, e);
                }
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
