///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CmdConstructor.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Cmd;


import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;

import test.Testcase;

/**
 The CmdConstructor class tests the following methods of the CommandCall class:
 <li>constructors,
 <li>get/set methods,
 <li>toString().
 **/
public class CmdConstructor extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "CmdConstructor";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.CmdTest.main(newArgs); 
   }
    /**
     Default constructor.  Verify that neither the system or command are set.
     **/
    public void Var001()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            assertCondition((cmd.getSystem() == null) && (cmd.getCommand().length() == 0) && (cmd.toString().startsWith("CommandCall (system: null command: ):")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Constructor with 1 parameter.  Pass null and expect an exception.
     **/
    public void Var002()
    {
        try
        {
            CommandCall cmd = new CommandCall(null);
            failed("Did not throw exception."+cmd);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Constructor with 1 parameter.  Pass a valid system.
     **/
    public void Var003()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            assertCondition((cmd.getSystem() == systemObject_) && (cmd.getCommand().length() == 0) && (cmd.toString().startsWith("CommandCall (system:")) && (cmd.toString().indexOf("command: ):") != -1));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Constructor with 2 parameters.  Pass null for the system and expect an exception.
     **/
    public void Var004()
    {
        try
        {
            CommandCall cmd = new CommandCall(null, "CRTLIB FRED");
            failed("Did not throw exception."+cmd);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Constructor with 2 parameters.  Pass null for the command and expect an exception.
     **/
    public void Var005()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, null);
            failed("Did not throw exception."+cmd);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Constructor with 2 parameters.  Pass a valid system and command.
     **/
    public void Var006()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "CRTLIB FRED");
            assertCondition((cmd.getSystem() == systemObject_) && (cmd.getCommand().equals("CRTLIB FRED")) && (cmd.toString().startsWith("CommandCall (system:")) && (cmd.toString().indexOf("command: CRTLIB FRED):") != -1));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     setCommand().  Pass null and expect an exception.
     **/
    public void Var007()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            cmd.setCommand(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setCommand().  Pass a valid command.
     **/
    public void Var008()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
	    cmd.setCommand("DLTLIB FRED");
            assertCondition((cmd.getSystem() == systemObject_) && (cmd.getCommand().equals("DLTLIB FRED")) && (cmd.toString().startsWith("CommandCall (system:")) && (cmd.toString().indexOf("command: DLTLIB FRED):") != -1));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     setCommand().  Set after a command has run.
     **/
    public void Var009()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            cmd.run();
            cmd.setCommand("DLTLIB FRED");
            assertCondition((cmd.getSystem() == systemObject_) && (cmd.getCommand().equals("DLTLIB FRED")) && (cmd.toString().startsWith("CommandCall (system:")) && (cmd.toString().indexOf("command: DLTLIB FRED):") != -1));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     setSystem().  Pass null and expect an exception.
     **/
    public void Var010()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            cmd.setSystem(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setSystem().  Pass a valid system.
     **/
    public void Var011()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            cmd.setSystem(systemObject_);
            assertCondition((cmd.getSystem() == systemObject_) && (cmd.getCommand().equals("")) && (cmd.toString().startsWith("CommandCall (system:")) && (cmd.toString().indexOf("command: ):") != -1));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     setSystem().  Set after a command has run.
     **/
    public void Var012()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            cmd.run();
            cmd.setSystem(new AS400());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }
}
