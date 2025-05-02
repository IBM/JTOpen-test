///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CmdCallThreadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Thread;


import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;

import test.misc.ThreadedTestcase;

public class CmdCallThreadTestcase extends ThreadedTestcase
{
    private String lib         = "CCTHDTST";        // Library on as400 to conduct tests in.
    // Variables used to create new AS400 objects.
    private String system1     = "rchaslkb";
    private String system2     = "rchas2ba";
    private String uid         = "javatest";
    private String pwd         = "jteam1";

    /**
     Creates a new CmdCallThreadTestcase.  This is called from ThreadTest::createTestcases().
     **/
    public CmdCallThreadTestcase(AS400 systemObject, Vector<String> variationsToRun, int runMode, FileOutputStream fileOutputStream,  String password)
    {
        super(systemObject, "CmdCallThreadTestcase", 5, variationsToRun, runMode, fileOutputStream, password);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        boolean allVariations = (variationsToRun_.size() == 0);

        try
        {
            setup();
        }
        catch (Exception e)
        {
            System.out.println("Setup failed");
            return;
        }

        if ((allVariations || variationsToRun_.contains("1")) && runMode_ != ATTENDED)
        {
            setVariation(1);
            Var001();
        }
        if ((allVariations || variationsToRun_.contains("2")) && runMode_ != ATTENDED)
        {
            setVariation(2);
            Var002();
        }
        if ((allVariations || variationsToRun_.contains("3")) && runMode_ != ATTENDED)
        {
            setVariation(3);
            Var003();
        }
        if ((allVariations || variationsToRun_.contains("4")) && runMode_ != ATTENDED)
        {
            setVariation(4);
            Var004();
        }
        if ((allVariations || variationsToRun_.contains("5")) && runMode_ != ATTENDED)
        {
            setVariation(5);
            Var005();
        }

        try
        {
            cleanup();
        }
        catch (Exception e)
        {
        }
    }

    // Create library.
    protected void setup() throws Exception
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
	    deleteLibrary(cmd, lib);
            if (cmd.run("QSYS/CRTLIB " + lib) == false)
            {
                output_.println("Unable to create library " + cmd.getMessageList()[0].getID() + " " + cmd.getMessageList()[0].getText());
            }
            systemObject_.disconnectService(AS400.COMMAND);
        }
        catch (Exception e)
        {
            output_.println("Unable to do setup: " + e);
            throw e;
        }
    }

    // Delete library.
    protected void cleanup() throws Exception
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
	    deleteLibrary(cmd,lib);
        }
        catch (Exception e)
        {
            output_.println("Cleanup failed: " + e);
            throw e;
        }
    }

    /**
     Multiple Threads retrieving information from a AS400Message object.
     **/
    public void Var001()
    {
        try
        {
            pipeInput_ = new PipedInputStream();
            CommandCall c  = new CommandCall(systemObject_);
            // Run a command to get a AS400Message object.
	    c.run("QSYS/DLTLIB XHB8BOGUS");
            // Use one resulting AS400Message for comparison purposes.
            AS400Message m  = c.getMessageList()[0];
            CmdCallThread t1 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.GET_ALL, m, m.getFileName(), m.getHelp(), m.getID(), m.getLibraryName(), m.getSeverity(), m.getSubstitutionData(), m.getText(), m.getType());
            CmdCallThread t2 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.GET_ALL, m, m.getFileName(), m.getHelp(), m.getID(), m.getLibraryName(), m.getSeverity(), m.getSubstitutionData(), m.getText(), m.getType());
            CmdCallThread t3 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.GET_ALL, m, m.getFileName(), m.getHelp(), m.getID(), m.getLibraryName(), m.getSeverity(), m.getSubstitutionData(), m.getText(), m.getType());
            objectInput_ = new ObjectInputStream(pipeInput_);
            t1.start();
            t2.start();
            t3.start();
            go();
            handleError();
            stopThreads();
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Multiple Threads performing CRTLIB's simultaneously.
     **/
    public void Var002()
    {
        try
        {
            pipeInput_ = new PipedInputStream();
            CommandCall c = new CommandCall(systemObject_);
            CmdCallThread t1 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.CRTLIB, c, "T1LIB");
            CmdCallThread t2 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.CRTLIB, c, "T2LIB");
            CmdCallThread t3 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.CRTLIB, c, "T3LIB");
            objectInput_ = new ObjectInputStream(pipeInput_);
            t1.start();
            t2.start();
            t3.start();
            go();
            handleError();
            stopThreads();
            // Ensure that libraries have been deleted.
            c = new CommandCall(systemObject_);
	    deleteLibrary(c,"T1LIB");
	    deleteLibrary(c,"T2LIB");
	    deleteLibrary(c,"T3LIB");

        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Multiple Threads executing CommandCall.setCommand().
     **/
    public void Var003()
    {
        try
        {
            pipeInput_ = new PipedInputStream();
            CommandCall c = new CommandCall(systemObject_);
            CmdCallThread t1 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.SET_COMMAND, c);
            CmdCallThread t2 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.SET_COMMAND, c);
            CmdCallThread t3 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.SET_COMMAND, c);
            objectInput_ = new ObjectInputStream(pipeInput_);
            t1.start();
            t2.start();
            t3.start();
            go();
            handleError();
            stopThreads();
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Multiple Threads attempting to set the value of the System using CommandCall.setSystem()
     **/
    public void Var004()
    {
        try
        {
            pipeInput_ = new PipedInputStream();
            CommandCall c = new CommandCall(systemObject_);
            AS400 s1 = new AS400(system1, uid, pwd.toCharArray());
            AS400 s2 = new AS400(system2, uid, pwd.toCharArray());
            CmdCallThread t1 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.SET_SYSTEM, c, s1, s2);
            CmdCallThread t2 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.SET_SYSTEM, c, s1, s2);
            CmdCallThread t3 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.SET_SYSTEM, c, s1, s2);
            objectInput_ = new ObjectInputStream(pipeInput_);
            t1.start();
            t2.start();
            t3.start();
            go();
            handleError();
            stopThreads();
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Test.
     **/
    public void Var005()
    {
        try
        {
            pipeInput_ = new PipedInputStream();
            CommandCall c = new CommandCall(systemObject_);
            CmdCallThread t1 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.CRTLIB, c, "T1LIB");
            CmdCallThread t2 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.SET_COMMAND, c);
            AS400 s1 = new AS400(system1, uid, pwd.toCharArray());
            AS400 s2 = new AS400(system2, uid, pwd.toCharArray());
            CmdCallThread t3 = new CmdCallThread(pipeInput_, output_, this, CmdCallThread.SET_SYSTEM, c, s1, s2);
            objectInput_ = new ObjectInputStream(pipeInput_);
            t1.start();
            t2.start();
            t3.start();
            go();
            handleError();
            stopThreads();
            c = new CommandCall(systemObject_);
            c.run("QSYS/DLTLIB T1LIB");
	    
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
