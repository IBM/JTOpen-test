///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CmdRunTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Cmd;

import java.io.IOException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.Job;

import test.CmdTest;
import test.JTOpenTestEnvironment;
import test.Testcase;


/**
 The CmdRunTestcase class tests the following methods of the CommandCall class:
 <li>run(),
 <li>getMessageList().
 **/
public class CmdRunTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "CmdRunTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.CmdTest.main(newArgs); 
   }
    static final boolean DEBUG = false;
    static boolean dftThreadSafe_ = CmdTest.assumeCommandsThreadSafe_;

    // Private data.
    private static final String LIBRARY_ = "JT400CC";
    private static final String CRTLIB_ = "QSYS/CRTLIB " + LIBRARY_;
    private boolean reportedMissingJobClass_ = false;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
	deleteLibrary(LIBRARY_); 
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
	deleteLibrary(LIBRARY_);
	deleteLibrary("FRED");
    }

    /**
     Dumps the messages for debugging.
     **/
    public void dump(AS400Message[] messageList)
    {
        for (int i = 0; i < messageList.length; ++i)
        {
            System.out.println(i + ". " + messageList[i]);
        }
    }

    /**
     Validates a message.
     * @throws IOException 
     * @throws AS400SecurityException 
     **/
    private boolean checkMessage(AS400Message message, String fileName, String id, int severity, int type, String help, StringBuffer sb) throws AS400SecurityException, IOException
    {
        /*
         System.out.println("Date:                " + message.getDate());
         System.out.println("Default reply:       " + message.getDefaultReply());
         System.out.println("File name:           " + message.getFileName());
         System.out.println("Help:                " + message.getHelp());
         System.out.println("ID:                  " + message.getID());
         System.out.println("Library name:        " + message.getLibraryName());
         System.out.println("Path:                " + message.getPath());
         System.out.println("Severity:            " + message.getSeverity());
         System.out.println("Subsitution data:    " + message.getSubstitutionData());
         System.out.println("Text:                " + message.getText());
         System.out.println("Type:                " + message.getType());
         System.out.println("toString:            " + message.toString());
         */

        boolean success = true;
        if (systemObject_.getVRM() >= 0x00070200) { 
          if ((message.getDate() == null)) {
            sb.append("\nfor expectedId="+id+" messageDate is null prior to V7R2, sb nonNull"); 
            success = false; 
          }
          
        } else { 
          if (!(message.getDate() == null)) {
            sb.append("\nfor expectedId="+id+" messageDate="+message.getDate()+" sb null"); 
            success = false; 
          }
        }
	if ( ! (message.getDefaultReply() != null)) {
	    sb.append("\nfor expectedId="+id+"message.getDefaultReply()="+message.getDefaultReply()+" sb nonNull");
	    success=false;
	}
	if ( ! (message.getFileName().equals(fileName))) {
	  sb.append("\nfor expectedId="+id+"message.getFileName()="+message.getFileName()+" sb "+fileName); 
	    success=false;
	}
	if ( ! (message.getHelp().equals(help))) {
	  sb.append("\nfor expectedId="+id+"message.getHelp()="+message.getHelp()+" sb "+help); 
	    success=false;
	}
	if ( ! (message.getID().equals(id))) {
	  sb.append("\nfor expectedId="+id+"message.getID()="+message.getID()+" sb "+id); 
	    success=false;
	}
	if ( ! (message.getLibraryName().startsWith("QSYS"))) {
	  sb.append("\nfor expectedId="+id+"message.getLibraryName()="+message.getLibraryName()+" sb QSYS");
	    success=false;
	}
	if ( ! (message.getPath().indexOf(fileName) >= 0)) {
	  sb.append("\nfor expectedId="+id+"message.getPath()="+message.getPath()+" should contain "+fileName); 
	    success=false;
	}
	if ( ! (message.getSeverity() == severity)) {
    sb.append("\nfor expectedId="+id+"message.getSeverity()="+message.getSeverity()+" sb "+severity); 
	    success=false;
	}
	if ( ! (message.getSubstitutionData() != null)) {
    sb.append("\nfor expectedId="+id+"message.getSubstitutionData()="+message.getSubstitutionData()+" sb nonNull"); 
	    success=false;
	}
	if ( ! (message.getText() != null && message.getText().trim().length() > 0)) {
    sb.append("\nfor expectedId="+id+"message.getText()="+message.getText()+" should have length > 0"); 
	    success=false;
	}
	if ( ! (message.getType() == type)) {
    sb.append("\nfor expectedId="+id+"message.getType()="+message.getType()+" sb "+type); 
	    success=false;
	}
	if ( ! (message.toString() != null)) {
    sb.append("\nfor expectedId="+id+"message.toString()="+message.toString()+" sb nonNull"); 
	    success=false;
	}
        return success;
    }

    private final boolean isRunningNatively()
    {
      if (JTOpenTestEnvironment.isOS400 /*&& isNative_ && isLocal_*/ && systemObject_.canUseNativeOptimizations())
      {
        return true;
      }
      else return false;
    }

    private final boolean isRunningNativelyAndThreadsafe()
    {
      if (!isRunningNatively()) return false;
      String prop = getCommandCallThreadSafetyProperty(); // we only care about CommandCall, not ProgramCall
      if (prop != null && prop.equals("true"))
      {
        return true;
      }
      else return false;
    }


    /**
     Prints the returned messages.
     **/
    private void printMessages(CommandCall cmd)
    {
        AS400Message[] messageList = cmd.getMessageList();
        if (messageList == null || messageList.length == 0)
          System.out.println("No messages were returned from command " + cmd.getCommand());
        else
        {
            for (int i=0; i<messageList.length; ++i)
            {
                System.out.println("Message["+i+"] = " + messageList[i].getFileName() + ", " + messageList[i].getID() + ", " + messageList[i].getText());
            }
        }
    }

    /**
     run() with no parameters.  Run when neither system nor command is set.
     **/
    public void Var001()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            cmd.run();
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     run() with no parameters.  Run when a system is set but not a command.
     **/
    public void Var002()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            cmd.run();
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     run() with no parameters.  Run when a command is set but not a system.
     **/
    public void Var003()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            cmd.setCommand("PING " + systemObject_.getSystemName());
            cmd.run();
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     run() with no parameters.  Set the command to "" and run that.
     **/
    public void Var004()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "");
            cmd.run();
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     run() with no parameters.  Run with a bad system.
     **/
    public void Var005()
    {
        try
        {
            AS400 system = new AS400("rchas000", "bob", "nelson".toCharArray());
            system.setGuiAvailable(false);
            CommandCall cmd = new CommandCall(system, "PING " + systemObject_.getSystemName());
            cmd.run();
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
                assertExceptionIsInstanceOf(e, "java.net.UnknownHostException");
        }
    }

    /**
     run() with no parameters.  Run a bogus command.
     **/
    public void Var006()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "QSYS/CRTWLDPCE");
            cmd.setThreadSafe(true);
            boolean check = cmd.run();
            AS400Message[] messageList = cmd.getMessageList();
            messageList[0].load();
            messageList[1].load();
            if (systemObject_.getVRM() >= 0x00050500)
            {
                StringBuffer sb = new StringBuffer(); 
		boolean check2 = checkMessage(messageList[0], "QCPFMSG", "CPD0030", 30, 2, messageList[0].getHelp(),sb) 
		              && checkMessage(messageList[1], "QCPFMSG", "CPF0001", 30, 15, messageList[1].getHelp(), sb);
                assertCondition((check == false) && (messageList.length == 2) &&  check2, sb.toString());
            }
            else
            {
              StringBuffer sb = new StringBuffer(); 
              boolean check2 =(check == false) && (messageList.length == 2) && (checkMessage(messageList[0], "QCPFMSG", "CPD0030", 30, 2, messageList[0].getHelp(),sb)) && (checkMessage(messageList[1], "QCPFMSG", "CPF0006", 30, 15, messageList[1].getHelp(),sb)); 
                assertCondition(check2, sb);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with no parameters.  Run a command that we don't have access to.
     <p>Note to tester: If this fails, make sure you are running with a non-SECOFR user id.  A SECOFR will have access to the command and this variation will fail.
     **/
    public void Var007()
    {
	String info = "run() with no parameters.  Run a command that we don't "+
	  " have access to.\n"+
	  "<p>Note to tester: If this fails, make sure you are running with a "+
	  "non-SECOFR user id.  A SECOFR will have access to the command and " +
	  "this variation will fail."; 
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "QSYS/STRCMNTRC");
            cmd.setThreadSafe(true);
            boolean check = cmd.run();
            if (DEBUG) printMessages(cmd);
            AS400Message[] messageList = cmd.getMessageList();
            messageList[0].load();
            messageList[1].load();
            StringBuffer sb = new StringBuffer(info); 
            if (systemObject_.getVRM() >= 0x00050500)
            {
                assertCondition((check == false) &&(messageList.length == 2) && (checkMessage(messageList[0], "QCPFMSG", "CPD0032", 30, 2, messageList[0].getHelp(),sb)) && (checkMessage(messageList[1], "QCPFMSG", "CPF0001", 30, 15, messageList[1].getHelp(),sb)), sb);
            }
            else
            {
                assertCondition((check == false) &&(messageList.length == 2) && (checkMessage(messageList[0], "QCPFMSG", "CPD0032", 30, 2, messageList[0].getHelp(), sb)) && (checkMessage(messageList[1], "QCPFMSG", "CPF0006", 30, 15, messageList[1].getHelp(), sb)), sb);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with no parameters.  Run an interactive command.
     **/
    public void Var008()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "DSPDTAARA DTAARA(*LDA)");
            boolean check = cmd.run();
            if (DEBUG) printMessages(cmd);
            AS400Message[] messageList = cmd.getMessageList();
            int expectedMsgs = 0;
            if (dftThreadSafe_)
            {
                expectedMsgs += 1; // extra warning msg if on-thread
            }
            // If we're running truly natively, and assuming all commands are threadsafe:
            if (isRunningNativelyAndThreadsafe())
            {
              expectedMsgs = 0;
            }
            assertCondition((check == true) && (messageList.length == expectedMsgs));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with no parameters.  Run an valid command that succeeds with no messages.
     **/
    public void Var009()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "SNDMSG MSG('Hi Mom') TOUSR(" + systemObject_.getUserId() + ")");
            boolean check = cmd.run();
            AS400Message[] messageList = cmd.getMessageList();
            assertCondition((check == true) && (messageList.length == 0));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with no parameters.  Run an valid command that succeeds with 1 message.
     **/
    public void Var010()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, CRTLIB_);
            boolean check = cmd.run();
            AS400Message[] messageList = cmd.getMessageList();
            assertCondition((check == true) && (messageList.length == 1));
            cleanup();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with no parameters.  Run an valid command that succeeds with multiple messages.
     **/
    public void Var011()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName() + " NBRPKT(5)");
            boolean check = cmd.run();
            if (DEBUG) printMessages(cmd);
            AS400Message[] messageList = cmd.getMessageList();
            int expectedMsgs = 8;
            if (dftThreadSafe_)
            {
                expectedMsgs += 1; // extra warning msg if on-thread
            }
            // If we're running truly natively, and assuming all commands are threadsafe:
            if (isRunningNativelyAndThreadsafe())
            {
              expectedMsgs = 8;
            }
            assertCondition((check == true) && (messageList.length == expectedMsgs));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with no parameters.  Run an valid command that fails.
     **/
    public void Var012()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING RCHAS000. NBRPKT(5)");
            boolean check = cmd.run();
            if (DEBUG) printMessages(cmd);
            AS400Message[] messageList = cmd.getMessageList();
            int expectedMsgs = 1;
            if (dftThreadSafe_)
            {
                expectedMsgs += 1; // extra warning msg if on-thread
            }
            // If we're running truly natively, and assuming all commands are threadsafe:
            if (isRunningNativelyAndThreadsafe())
            {
              expectedMsgs = 1;
            }
            assertCondition((check == false) && (messageList.length >= expectedMsgs), 
            		"check="+check+" sb false : messageList.length="+messageList.length+" sb >= expectedMsgs="+expectedMsgs);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with 1 parameter.  Pass null and expect an exception.
     **/
    public void Var013()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            cmd.run((String) null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     run() with 1 parameter.  Run when neither system nor command is set.
     **/
    public void Var014()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            cmd.run("PING " + systemObject_.getSystemName());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     run() with 1 parameter.  Run when a system is set but not a command.
     **/
    public void Var015()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            String command = "PING " + systemObject_.getSystemName();
            boolean check = cmd.run(command);
            AS400Message[] messageList = cmd.getMessageList();
            assertCondition((check == true) && (messageList.length > 0) && (cmd.getCommand().equals(command)));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with 1 parameter.  Run when a command is set but not a system.
     **/
    public void Var016()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            String command = "PING " + systemObject_.getSystemName();
            cmd.setCommand(command);
            cmd.run(command);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     run() with 1 parameter.  Set the command to "" and run that.
     **/
    public void Var017()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            cmd.run("");
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     run() with 1 parameter.  Run with a bad system.
     **/
    public void Var018()
    {
        try
        {
            AS400 system = new AS400("rchas000", "bob", "nelson".toCharArray());
            system.setGuiAvailable(false);
            CommandCall cmd = new CommandCall(system);
            cmd.run("PING " + systemObject_.getSystemName());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
                assertExceptionIsInstanceOf(e, "java.net.UnknownHostException");
        }
    }

    /**
     run() with 1 parameter.  Run a bogus command.
     **/
    public void Var019()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            cmd.setThreadSafe(true);
            boolean check = cmd.run("QSYS/CRTWLDPCE");
            AS400Message[] messageList = cmd.getMessageList();
            messageList[0].load();
            messageList[1].load();
            StringBuffer sb = new StringBuffer(); 
            if (systemObject_.getVRM() >= 0x00050500)
            {
                assertCondition((check == false) && (messageList.length == 2) && (checkMessage(messageList[0], "QCPFMSG", "CPD0030", 30, 2, messageList[0].getHelp(), sb)) && (checkMessage(messageList[1], "QCPFMSG", "CPF0001", 30, 15, messageList[1].getHelp(), sb)),sb);
            }
            else
            {
                assertCondition((check == false) && (messageList.length == 2) && (checkMessage(messageList[0], "QCPFMSG", "CPD0030", 30, 2, messageList[0].getHelp(), sb)) && (checkMessage(messageList[1], "QCPFMSG", "CPF0006", 30, 15, messageList[1].getHelp(), sb)),sb);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with 1 parameter.  Run a command that we don't have access to.
     <p>Note to tester: If this fails, make sure you are running with a non-SECOFR user id.  A SECOFR will have access to the command and this variation will fail.
     **/
    public void Var020()
    {
	String info = "run() with 1 parameter.  Run a command that we don't have "+
	  "access to.\n"+
	  "<p>Note to tester: If this fails, make sure you are running with a "+
	  "non-SECOFR user id.  A SECOFR will have access to the command and "+
	  "this variation will fail."; 
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            cmd.setThreadSafe(true);
            boolean check = cmd.run("QSYS/STRCMNTRC");
            if (DEBUG) printMessages(cmd);
            AS400Message[] messageList = cmd.getMessageList();
            messageList[0].load();
            messageList[1].load();
            StringBuffer sb = new StringBuffer(info); 
            if (systemObject_.getVRM() >= 0x00050500)
            {
                assertCondition((check == false) &&(messageList.length == 2) && (checkMessage(messageList[0], "QCPFMSG", "CPD0032", 30, 2, messageList[0].getHelp(), sb)) && (checkMessage(messageList[1], "QCPFMSG", "CPF0001", 30, 15, messageList[1].getHelp(), sb)), sb);
            }
            else
            {
                assertCondition((check == false) && (messageList.length == 2) && (checkMessage(messageList[0], "QCPFMSG", "CPD0032", 30, 2, messageList[0].getHelp(), sb)) && (checkMessage(messageList[1], "QCPFMSG", "CPF0006", 30, 15, messageList[1].getHelp(), sb)), sb);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with 1 parameter.  Run an interactive command.
     **/
    public void Var021()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            boolean check = cmd.run("DSPDTAARA DTAARA(*LDA)");
            if (DEBUG) printMessages(cmd);
            AS400Message[] messageList = cmd.getMessageList();
            int expectedMsgs = 0;
            if (dftThreadSafe_)
            {
                expectedMsgs += 1; // extra warning msg if on-thread
            }
            // If we're running truly natively, and assuming all commands are threadsafe:
            if (isRunningNativelyAndThreadsafe())
            {
              expectedMsgs = 0;
            }
            assertCondition((check == true) && (messageList.length == expectedMsgs));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with 1 parameter.  Run an valid command that succeeds with no messages.
     **/
    public void Var022()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            String command = "SNDMSG MSG('Hi Mom') TOUSR(" + systemObject_.getUserId() + ")";
            boolean check = cmd.run(command);
            AS400Message[] messageList = cmd.getMessageList();
            assertCondition((check == true) && (messageList.length == 0) && (cmd.getCommand().equals(command)));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with 1 parameter.  Run an valid command that succeeds with 1 message.
     **/
    public void Var023()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            boolean check = cmd.run(CRTLIB_);
            AS400Message[] messageList = cmd.getMessageList();
            assertCondition((check == true) && (messageList.length == 1) && (cmd.getCommand().equals(CRTLIB_)));
            cleanup();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with 1 parameter.  Run an valid command that succeeds with multiple messages.
     **/
    public void Var024()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            String command = "PING " + systemObject_.getSystemName() + " NBRPKT(5)";
            boolean check = cmd.run(command);
            if (DEBUG) printMessages(cmd);
            AS400Message[] messageList = cmd.getMessageList();
            int expectedMsgs = 8;
            if (dftThreadSafe_)
            {
                expectedMsgs += 1; // extra warning msg if on-thread
            }
            // If we're running truly natively, and assuming all commands are threadsafe:
            if (isRunningNativelyAndThreadsafe())
            {
              expectedMsgs = 8;
            }
            assertCondition((check == true) && (messageList.length == expectedMsgs) && (cmd.getCommand().equals(command)));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with 1 parameter.  Run an valid command that fails.
     **/
    public void Var025()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            String command = "PING RCHAS000. NBRPKT(5)";
            boolean check = cmd.run(command);
            if (DEBUG) printMessages(cmd);
            AS400Message[] messageList = cmd.getMessageList();
            int expectedMsgs = 1;
            if (dftThreadSafe_)
            {
                expectedMsgs += 1; // extra warning msg if on-thread
            }
            // If we're running truly natively, and assuming all commands are threadsafe:
            if (isRunningNativelyAndThreadsafe())
            {
              expectedMsgs = 1;
            }
            assertCondition((check == false) && (messageList.length >= expectedMsgs) && (cmd.getCommand().equals(command)),
            		"check="+check+" sb false : messageList.length="+messageList.length+" sb >= expectedMsgs="+expectedMsgs+
            		" : cmd.getCommand()="+cmd.getCommand()+" sb command="+command);

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     getMessageList() with no parameters.  When no properties have been set.
     **/
    public void Var026()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            AS400Message[] messageList = cmd.getMessageList();
            assertCondition(messageList.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     getMessageList() with no parameters.  When the command has not been run.
     **/
    public void Var027()
    {
        try
        {
            String command = "PING " + systemObject_.getSystemName() + " NBRPKT(5)";
            CommandCall cmd = new CommandCall(systemObject_, command);
            AS400Message[] messageList = cmd.getMessageList();
            assertCondition(messageList.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     getMessageList() with 1 parameter.  When no properties have been set.
     **/
    public void Var028()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            AS400Message message = cmd.getMessageList(0);
            failed("Did not throw exception."+message);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.ArrayIndexOutOfBoundsException");
        }
    }

    /**
     getMessageList() with 1 parameter.  When the command has not been run.
     **/
    public void Var029()
    {
        try
        {
            String command = "PING " + systemObject_.getSystemName() + " NBRPKT(5)";
            CommandCall cmd = new CommandCall(systemObject_, command);
            AS400Message message = cmd.getMessageList(0);
            failed("Did not throw exception."+message);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.ArrayIndexOutOfBoundsException");
        }
    }

    /**
     run() with 1 parameter.  Pass a -1.
     **/
    public void Var030()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            String command = "PING " + systemObject_.getSystemName() + " NBRPKT(5)";
            boolean check = cmd.run(command);
            AS400Message message = cmd.getMessageList(-1);
            failed("Did not throw exception."+message+" "+check);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.ArrayIndexOutOfBoundsException");
        }
    }

    /**
     run() with 1 parameter.  Pass a 0.
     **/
    public void Var031()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            String command = "PING " + systemObject_.getSystemName() + " NBRPKT(5)";
            boolean check = cmd.run(command);
            if (DEBUG) printMessages(cmd);
            int expectedMsgs = 1;
            if (dftThreadSafe_)
            {
                expectedMsgs += 1; // extra warning msg if on-thread
            }
            // If we're running truly natively, and assuming all commands are threadsafe:
            if (isRunningNativelyAndThreadsafe())
            {
              expectedMsgs = 1;
            }
            AS400Message message = cmd.getMessageList(expectedMsgs-1);
            message.load();
            StringBuffer sb = new StringBuffer("check="+check); 
            assertCondition(checkMessage(message, "QTCPMSG", "TCP3203", 0, 4, message.getHelp(), sb), sb);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with 1 parameter.  Pass the max valid index.
     **/
    public void Var032()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            String command = "PING " + systemObject_.getSystemName() + " NBRPKT(5)";
            boolean check = cmd.run(command);
            if (DEBUG) printMessages(cmd);
            int expectedMsgs = 8;
            if (dftThreadSafe_)
            {
                expectedMsgs += 1; // extra warning msg if on-thread
            }
            // If we're running truly natively, and assuming all commands are threadsafe:
            if (isRunningNativelyAndThreadsafe())
            {
              expectedMsgs = 8;
            }
            AS400Message message = cmd.getMessageList(expectedMsgs-1);
            message.load();
            StringBuffer sb = new StringBuffer("check = "+check); 
            assertCondition(checkMessage(message, "QTCPMSG", "TCP3210", 0, 1, message.getHelp(), sb), sb);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     run() with 1 parameter.  Pass 1 greater than the max valid index.
     **/
    public void Var033()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            String command = "PING " + systemObject_.getSystemName() + " NBRPKT(5)";
            boolean check = cmd.run(command);
            if (DEBUG) printMessages(cmd);
            int expectedMsgs = 8;
            if (dftThreadSafe_)
            {
                expectedMsgs += 1; // extra warning msg if on-thread
            }
            AS400Message message = cmd.getMessageList(expectedMsgs+1);
            failed("Did not throw exception."+check+message);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.ArrayIndexOutOfBoundsException");
        }
    }

    //--------------------------------------------------------------
    //
    // Variations to test getJob()
    //
    //--------------------------------------------------------------

    /**
     Create a command object, without setting system, verify that getJob() returns null.
     **/
    public void Var034()
    {
      notApplicable("obsolete method");  // the getJob() method was eliminated
//        try
//        {
//            CommandCall cmd = new CommandCall();
//            RJob job = cmd.getJob();
//            failed("Did not throw exception.");
//        }
//        catch (Exception e)
//        {
//            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
//        }
//        catch (NoClassDefFoundError e)
//        { // JarMaker may have removed RJob class.
//          if (e.getMessage().indexOf("RJob") != -1) {
//            notApplicable("Class not found: RJob");
//          }
//          else failed(e, "Unexpected exception");
//        }
    }

    /**
     Create a command object, verify that getJob() returns non-null.
     **/
    public void Var035()
    {
      notApplicable("obsolete method");  // the getJob() method was eliminated
//        if (systemObject_.getProxyServer().length() > 0)                    // @B4A
//        {                                                                   // @B4A
//            notApplicable("getJob() with proxy not supported");             // @B4A
//            return;                                                         // @B4A
//        }                                                                   // @B4A
//        try
//        {
//            CommandCall cmd = new CommandCall(systemObject_);
//            RJob job = cmd.getJob();
//            if (job == null)
//            {
//                failed("Null Job returned from getJob()");
//            }
//            else
//            {
//                if (DEBUG)
//                {
//                    String jobID = job.getName() + "/" + job.getUser() + "/" + job.getNumber();
//                    output_.println("DEBUG job name/user/number: |" + jobID + "|");
//                }
//                succeeded();
//            }
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception");
//        }
//        catch (NoClassDefFoundError e)
//        { // JarMaker may have removed RJob class.
//          if (e.getMessage().indexOf("RJob") != -1) {
//            notApplicable("Class not found: RJob");
//          }
//          else failed(e, "Unexpected exception");
//        }
    }

    /**
     If running on an AS/400:  Verify that getJob() returns different values if command is declared threadsafe vs non-threadsafe.  This is a crude way to check that we get the job of the JVM versus the job of the Host Server.
     If *not* running on an AS/400:  Verify that getJob() returns same values if command is declared threadsafe vs non-threadsafe.  This is a crude way to check that we always get the job of the Host Server rather than the JVM.
     **/
    public void Var036()
    {
      notApplicable("obsolete method");  // the getJob() method was eliminated
//        if (systemObject_.getProxyServer().length() > 0)                    // @B4A
//        {                                                                   // @B4A
//            notApplicable("getJob() with proxy not supported");             // @B4A
//            return;                                                         // @B4A
//        }                                                                   // @B4A
//
//        try
//        {
//            CommandCall cmd = new CommandCall(systemObject_);
//            cmd.setThreadSafe(false);
//            RJob job1 = cmd.getJob();
//            if (job1 == null)
//            {
//                failed("Null Job returned from first getJob()");
//                return;
//            }
//            String job1ID = job1.getName() + "/" + job1.getUser() + "/" + job1.getNumber();
//            if (DEBUG) output_.println("DEBUG job1 name/user/number: |" + job1ID + "|");
//
//            cmd.setThreadSafe(true);
//            RJob job2 = cmd.getJob();
//            if (job2 == null)
//            {
//                failed("Null Job returned from second getJob()");
//                return;
//            }
//            String job2ID = job2.getName() + "/" + job2.getUser() + "/" + job2.getNumber();
//            if (DEBUG) output_.println("DEBUG job2 name/user/number: |" + job2ID + "|");
//
//            cmd.setThreadSafe(false);
//            RJob job3 = cmd.getJob();
//            if (job3 == null)
//            {
//                failed("Null Job returned from third getJob()");
//                return;
//            }
//            String job3ID = job3.getName() + "/" + job3.getUser() + "/" + job3.getNumber();
//            if (DEBUG) output_.println("DEBUG job3 name/user/number: |" + job3ID + "|");
//
//            // Note: Threadsafety features became available in V5R1.
//            if (CmdTest.onAS400_ && systemObject_.getVersion() >= 5)
//            {
//                if (DEBUG) output_.println("DEBUG Running on an AS/400 (V5 or later)");
//                assertCondition(!job1ID.equals(job2ID) && job1ID.equals(job3ID));
//            }
//            else
//            {
//                //if (DEBUG) output_.println("DEBUG Not running on an AS/400");
//                assertCondition(job1ID.equals(job2ID) && job1ID.equals(job3ID));
//            }
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception");
//        }
//        catch (NoClassDefFoundError e)
//        { // JarMaker may have removed RJob class.
//          if (e.getMessage().indexOf("RJob") != -1) {
//            notApplicable("Class not found: RJob");
//          }
//          else failed(e, "Unexpected exception");
//        }
    }



    public void Var037()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            byte[] bytes = new byte[0];
            cmd.run(bytes);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "command.length (0): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    public void Var038()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            cmd.run((byte[]) null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    public void Var039()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);

            AS400Text conv = new AS400Text(16, systemObject_);
            byte[] bytes = new byte[16];
            conv.toBytes("QSYS/CRTLIB FRED", bytes);
            cmd.setThreadSafe(false);
            cmd.run(bytes);
            AS400Message[] messageList = cmd.getMessageList();
            if (messageList[0].getID().equals("CPF2111") ||    // fred exists
                messageList[0].getID().equals("CPC2102"))      // fred created
               succeeded();
            else
               failed("wrong message " + messageList[0].getText());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    public void Var040()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            AS400Text conv = new AS400Text(16, systemObject_);
            byte[] bytes = new byte[16];
            conv.toBytes("QSYS/CRTLIB FRED", bytes);
            cmd.setThreadSafe(true);
            cmd.run(bytes);
            AS400Message[] messageList = cmd.getMessageList();
            if (messageList[0].getID().equals("CPF2111") ||    // fred exists
                messageList[0].getID().equals("CPC2102"))      // fred created
               succeeded();
            else
               failed("wrong message " + messageList[0].getText());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    //--------------------------------------------------------------
    //
    // Variations to test getServerJob()
    //
    //--------------------------------------------------------------

    /**
     Create a command object, without setting system, verify that getServerJob() returns null.
     **/
    public void Var041()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            Job job = cmd.getServerJob();
            failed("Did not throw exception."+job);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     Create a command object, verify that getServerJob() returns non-null.
     **/
    public void Var042()
    {
        if (systemObject_.getProxyServer().length() > 0)                    // @B4A
        {                                                                   // @B4A
            notApplicable("getServerJob() with proxy not supported");             // @B4A
            return;                                                         // @B4A
        }                                                                   // @B4A
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            Job job =  cmd.getServerJob();
            if (job == null)
            {
                failed("Null Job returned from getServerJob()");
            }
            else
            {
                if (DEBUG)
                {
                    String jobID = job.getName() + "/" + job.getUser() + "/" + job.getNumber();
                    output_.println("DEBUG job name/user/number: |" + jobID + "|");
                }
                succeeded();
            }
        }
        catch (NoClassDefFoundError e)
        { // JarMaker may have removed Job class.
          String msg = e.getMessage();
          if (msg != null) {
            if (msg.indexOf("Job") != -1 ||
                msg.indexOf("IntegerHashtable") != -1) {
              reportedMissingJobClass_ = true;
              failed("Job-related class not found: " + e.getMessage());
            }
            else failed(e, "Unexpected NoClassDefFoundError");
          }
          else failed(e, "Unexpected NoClassDefFoundError");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If running on an AS/400:  Verify that getServerJob() returns different values if command is declared
     threadsafe vs non-threadsafe.  This is a crude way to check that we get the job of the JVM versus the
     job of the Host Server.
     If *not* running on an AS/400:  Verify that getServerJob() returns same values if command is declared
     threadsafe vs non-threadsafe.  This is a crude way to check that we always get the job of the Host Server
     rather than the JVM.
     **/
    public void Var043()
    {
        if (systemObject_.getProxyServer().length() > 0)                    // @B4A
        {                                                                   // @B4A
            notApplicable("getServerJob() with proxy not supported");             // @B4A
            return;                                                         // @B4A
        }                                                                   // @B4A

        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            cmd.setThreadSafe(false);
            Job job1 = cmd.getServerJob();
            if (job1 == null)
            {
                failed("Null Job returned from first getServerJob()");
                return;
            }
            String job1ID = job1.getName() + "/" + job1.getUser() + "/" + job1.getNumber();
            if (DEBUG) output_.println("DEBUG job1 name/user/number: |" + job1ID + "|");

            cmd.setThreadSafe(true);
            Job job2 = cmd.getServerJob();
            if (job2 == null)
            {
                failed("Null Job returned from second getServerJob()");
                return;
            }
            String job2ID = job2.getName() + "/" + job2.getUser() + "/" + job2.getNumber();
            if (DEBUG) output_.println("DEBUG job2 name/user/number: |" + job2ID + "|");

            cmd.setThreadSafe(false);
            Job job3 = cmd.getServerJob();
            if (job3 == null)
            {
                failed("Null Job returned from third getServerJob()");
                return;
            }
            String job3ID = job3.getName() + "/" + job3.getUser() + "/" + job3.getNumber();
            if (DEBUG) output_.println("DEBUG job3 name/user/number: |" + job3ID + "|");

            // Note: Threadsafety features became available in V5R1.
            if (isRunningNatively() && systemObject_.getVersion() >= 5)
            {
                if (DEBUG) output_.println("DEBUG Running natively on an IBM i system (V5 or later)");
                assertCondition(!job1ID.equals(job2ID) && job1ID.equals(job3ID));
            }
            else
            {
                //if (DEBUG) output_.println("DEBUG Not running on an AS/400");
                assertCondition(job1ID.equals(job2ID) && job1ID.equals(job3ID));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        catch (NoClassDefFoundError e)
        { // JarMaker may have removed Job class.
          if (reportedMissingJobClass_) output_.println("Already reported missing job class.");
          String msg = e.getMessage();
          if (msg != null) {
            if (msg.indexOf("Job") != -1 ||
                msg.indexOf("IntegerHashtable") != -1) {
              failed("Job-related class not found: " + e.getMessage());
            }
            else failed(e, "Unexpected NoClassDefFoundError");
          }
          else failed(e, "Unexpected NoClassDefFoundError");
        }
    }


    /**
	 * Run a wrong CL command and expect message returned w/ date and time.
	 **/
	public void Var044() {
		try {
			if (systemObject_.getVRM() < 0x00070200) {
				notApplicable("Currently this function is suported on 7.2 and later");
				return;
			}
			
			StringBuffer sb = new StringBuffer(); 
		boolean passed = true; 
		  CommandCall cc = new CommandCall(systemObject_);
		boolean isSuccess = cc.run("PING 127.0.0.0");
		if (!isSuccess) {
			// we except error because it's not 127.0.0.1, and we only check the first error message and verify its date is empty or not. 
			AS400Message[] msgs = cc.getMessageList();
			for (int i = 0; i < msgs.length; i++) {
				AS400Message msg = msgs[i];
				if (msg.getDate() == null) { 
				  passed = false;
				  sb.append("\nmessage for i="+i+" is null");
				}
			}
		} else { 
		  sb.append("Ping was successful "); 
		  passed =false; 
		}
    assertCondition(passed, sb );

    } catch (Exception e) {
      failed(e); 
    }
		
	}


}

