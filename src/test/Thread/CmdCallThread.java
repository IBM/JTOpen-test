///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CmdCallThread.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Thread;

import java.io.PipedInputStream;
import java.io.PrintWriter;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;

import test.ComponentThread;
import test.Testcase;
import test.misc.ThreadedTestcase;

class CmdCallThread extends ComponentThread
{
    // Functions that this thread can perform.  One is specified at object construction.
    public static final int CRTLIB      = 0;
    public static final int GET_ALL     = 1;
    public static final int SET_COMMAND = 2;
    public static final int SET_SYSTEM  = 3;

    // Variables related to CRTLIB.
    private CommandCall  cmdCall          = null;
    private String       lib              = null; // library to create

    // variables for comparison to AS400Message.
    private AS400Message message          = null;
    private String       file             = null;
    private String       help             = null;
    private String       id               = null;
    private String       library          = null;
    private int          severity         = 0;
    private byte[]       substitutionData = null;
    private String       text             = null;
    private int          type             = 0;
    // Variables used for CRT_SYSTEM.
    private AS400        system1          = null;
    private AS400        system2          = null;

    /**
     Constructor.
     **/
    public CmdCallThread(PipedInputStream pipeReader, PrintWriter output, ThreadedTestcase testcase, int function)
    {
        super(pipeReader, output, testcase, function);
    }

    /**
     Constructor for CRTLIB.
     **/
    public CmdCallThread(PipedInputStream pipeReader, PrintWriter output, ThreadedTestcase testcase, int function, CommandCall cc, String uniqueLibName)
    {
        super(pipeReader, output, testcase, function);
        cmdCall = cc;
        lib = uniqueLibName;
    }

    /**
     Constructor for SET_COMMAND.
     **/
    public CmdCallThread(PipedInputStream pipeReader, PrintWriter output, ThreadedTestcase testcase, int function, CommandCall cc)
    {
        super(pipeReader, output, testcase, function);
        cmdCall = cc;
    }

    /**
     Constructor for SET_SYSTEM.
     **/
    public CmdCallThread(PipedInputStream pipeReader, PrintWriter output, ThreadedTestcase testcase, int function, CommandCall cc, AS400 sys1, AS400 sys2)
    {
        super(pipeReader, output, testcase, function);
        cmdCall = cc;
        system1 = sys1;
        system2 = sys2;
    }

    /**
     Constructor for GET_ALL.
     **/
    public CmdCallThread(PipedInputStream pipeReader, PrintWriter output, ThreadedTestcase testcase, int function, AS400Message message, String file, String help, String id, String library, int severity, byte[] substitutionData, String text, int type)
    {
        super(pipeReader, output, testcase, function);
        this.message          = message;
        this.file             = file;
        this.help             = help;
        this.id               = id;
        this.library          = library;
        this.severity         = severity;
        this.substitutionData = substitutionData;
        this.text             = text;
        this.type             = type;
    }

    public void run()
    {
        // Notify testcase that we are ready to start.
        testcase_.ready();

        // Perform the task specified by function, numLoops_ times unless the stop flag is set.
        for (int i = 0; i < numLoops_ && !stop_ ; ++i)
        {
            // Don't be a selfish thread...
            try { sleep(1); } catch (InterruptedException e) {}

            switch (function_)
            {
                case CRTLIB:
                    performCrtLib();
                    break;
                case GET_ALL:
                    performGetAll();
                    break;
                case SET_COMMAND:
                    performSetCommand();
                    break;
                case SET_SYSTEM:
                    //performSetSystem();
                    break;
            }
        }

        // Done: close our end of pipe and stop.
        this.kill();
    }

    /**
     Executes a CRTLIB command on the as400 system and then deletes the same library.
     **/
    private void performCrtLib()
    {
        try
        {
            if (cmdCall.run("CRTLIB " + lib) == false)
            {
                error("CRTLIB failed");
            }
	    cmdCall.run("CHGJOB INQMSGRPY(*SYSRPYL)");

            if (cmdCall.run("DLTLIB " + lib) == false)
            {
                error("DLTLIB failed");
            }
        }
        catch (Exception e)
        {
            error("Unexpected Exception", e);
        }
    }

    /**
     Queries the AS400Message object returned due to a CommandCall being executed.  Ensures that each get method returns the correct info.
     **/
    private void performGetAll()
    {
        try
        {
            String s = null;  // Holds returned string data for comparison.

            // Note that only the first error written to pipe will be displayed.
            s = message.getFileName();
            if (s != null && ! s.equals(file))
            {
                error("Files not equal");
            }

            s = message.getHelp();
            if (s != null && !s.equals(help))
            {
                error("Error Text not equal");
            }

            s = message.getID();
            if (s != null && !s.equals(id))
            {
                error("Ids not equal");
            }

            s = message.getLibraryName();
            if (s != null && !s.equals(library))
            {
                error("Libraries not equal");
            }

            if (message.getSeverity() != severity)
            {
                error("Severities not equal");
            }

            if (!Testcase.isEqual(message.getSubstitutionData(), substitutionData))
            {
                error("Substitution Data not equal");
            }

            s = message.getText();
            if (s != null && !s.equals(text))
            {
                error("Texts not equal");
            }

            if (message.getType() != type)
            {
                error("Types not equal");
            }
        }
        catch (Exception e)
        {
            error("Unexpected Exception", e);
        }
    }

    /**
     Sets the value of the command to one of 3 values.  If the value retrieved doesn't match one of these 3 values the command was corrupted.
     **/
    private void performSetCommand()
    {
        try
        {
            // Any CmdCallThread executing a SET_COMMAND will set the value of system to one of 3 values.  If getCommand() returns any value besides these it was corrupted.
            String c1 = "ALPHA";
            String c2 = "BETA";
            String c3 = "OMEGA";
            cmdCall.setCommand(c1);
            cmdCall.setCommand(c2);
            cmdCall.setCommand(c3);

            String s = cmdCall.getCommand();
            if ((!s.equals(c1)) && (!s.equals(c2)) && (!s.equals(c3)))
            {
                error("System name was corrupted");
            }
        }
        catch (Exception e)
        {
            error("Unexpected Exception", e);
        }
    }

    /**
     Sets the value of the sytem to one of 2 values.  If the value retrieved doesn't match one of these 2 values the system as corrupted.
     **/
    private void performSetSystem()
    {
        try
        {
            cmdCall.setSystem(system1);

            cmdCall.setSystem(system2);

            String s = cmdCall.getSystem().toString();
            if (!s.equals(system1.toString()) || !s.equals(system2.toString()))
            {
                error("System was corrupted");
            }
        }
        catch (Exception e)
        {
            error("Unexpected Exception", e);
        }
    }
}
