///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CmdOnThreadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Cmd;

import java.util.Properties;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Trace;

import test.Testcase;

/**
 The CmdOnThreadTestcase class tests the following methods of the CommandCall class:
 <ul>
 <li>isThreadSafe()
 <li>setThreadSafe()
 <li>isStayOnThread()
 <li>getSystemThread()
 </ul>
 **/
public class CmdOnThreadTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "CmdOnThreadTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.CmdTest.main(newArgs); 
   }
    // Private data.
    static final boolean DEBUG = false;
    private static final String SYSTEM_PROP_NAME = "com.ibm.as400.access.CommandCall.threadSafe";

    private static final String LIBRARY    = "JT400TH";
    private static final String CRTLIB_CMD  = "CRTLIB " + LIBRARY;


    private static final String THREADSAFE_CMD  = CRTLIB_CMD;
    private static final String QUALIFIED_THREADSAFE_CMD  = "QSYS/"+CRTLIB_CMD;

    private static final String NONTHREADSAFE_CMD  = "CPYLIB FROMLIB(" + LIBRARY + ") TOLIB(TEMPLIBQ) CRTLIB(*NO)";
    private static final String NONEXISTENT_CMD  = "CPYLIBX FROMLIB(" + LIBRARY + ") TOLIB(TEMPLIBQ) CRTLIB(*NO)";

    private static boolean TRACE_INFORMATION_SETTING = Trace.isTraceInformationOn();
    private static boolean TRACE_SETTING = Trace.isTraceOn();
    private static boolean isSystemV5orHigher_; // is the AS/400 version V5 or later

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        clearThreadsafeProperty();
        CommandCall cmd = new CommandCall(systemObject_);
	deleteLibrary(cmd, LIBRARY); 
        if (systemObject_.getVersion() >= 5)
          isSystemV5orHigher_ = true;
        else
          isSystemV5orHigher_ = false;
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        if (DEBUG) System.out.println("CmdOnThreadTestcase.cleanup()");
        CommandCall cmd = new CommandCall(systemObject_);
	deleteLibrary(cmd, LIBRARY); 
    }

    /**
     Dumps the messages for debugging.
     **/
    public void dump(AS400Message[] messageList)
    {
        for (int i = 0; i < messageList.length; ++i)
            System.out.println(i + ". " + messageList[i]);
    }

    private final boolean isRunningNatively()
    {
      if (onAS400_ /*&& isNative_ && isLocal_*/ && systemObject_.canUseNativeOptimizations())
      {
        return true;
      }
      else return false;
    }

    /**
     Validates a message.
     **/
    public boolean checkMessage(AS400Message message, String fileName, String id, int severity, int type)
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
        success = success && (message.getDate() == null);
        success = success && (message.getDefaultReply() == null);
        success = success && (message.getFileName().equals(fileName));
        success = success && (message.getHelp() == null);
        success = success && (message.getID().equals(id));
        success = success && (message.getLibraryName().equals("QSYS"));
        success = success && (message.getPath().equals("/QSYS.LIB/" + fileName + ".MSGF"));
        success = success && (message.getSeverity() == severity);
        success = success && (message.getSubstitutionData() != null);
        success = success && (message.getText() != null);
        success = success && (message.getType() == type);
        success = success && (message.toString() != null);
        return success;
    }

    /**
     Prints the returned messages.
     **/
    public void printMessages(CommandCall cmd)
    {
        AS400Message[] messageList = cmd.getMessageList();
        if (messageList != null) {
            for (int i=0; i<messageList.length; i++)
                System.out.println("Message["+i+"] = " +
                                   messageList[i].getFileName() + ", " +
                                   messageList[i].getID() + ", " +
                                   messageList[i].getText());
        }
    }

    /**
     Display state of command.
     **/
    private void displayCommandValues(CommandCall cmd)
    {
      try
      {
        System.out.println("cmd.getSystem() == " + cmd.getSystem()); 
        System.out.println("cmd.getCommand() == " + cmd.getCommand()); 
        System.out.println("cmd.toString() == " + cmd.toString()); 
        System.out.println("cmd.getSystemThread() == " + cmd.getSystemThread()); 
        System.out.println("cmd.isStayOnThread() == " + cmd.isStayOnThread()); 
        System.out.println("cmd.isThreadSafe() == " + cmd.isThreadSafe());
      }
      catch (Exception e) {
        System.out.println("Error while attempting to display command values:");
        e.printStackTrace();
      }
    }


    /**
     Validate the value reported by getSystemThread().
     **/
/*    private boolean validateSystemThread(CommandCall cmd)
    {
        boolean result;
        if (cmd.isStayOnThread() == true)
            return (cmd.getSystemThread() != null);
        else
            return (cmd.getSystemThread() == null);
    }
*/

    /**
     Set the "CommandCall.threadSafe" system property.
     **/
    private void setThreadsafeProperty(boolean state)
    {
        //Properties sysProps = System.getProperties();
        if (state == true) {
            //sysProps.put(SYSTEM_PROP_NAME, "true");
            System.setProperty(SYSTEM_PROP_NAME, "true");
        }
        else {
            //sysProps.put(SYSTEM_PROP_NAME, "false");
            System.setProperty(SYSTEM_PROP_NAME, "false");
        }
        //System.setProperties(sysProps);
    }


    /**
     Clear the "CommandCall.threadSafe" system property.
     **/
    private void clearThreadsafeProperty()
    {
        Properties sysProps = System.getProperties();
        sysProps.remove(SYSTEM_PROP_NAME);
        System.setProperties(sysProps);
    }



    /**
     Verify DEFAULTS for a threadsafe command.
     <pre>
     - threadSafe property is not set.
     - setThreadSafe() not done.
     - setMustUseSockets() not done.
     - command is designated as thread-safe on the AS/400.
     __________________________________________________________________

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.
     **/
    public void Var001()
    {
        try {
            CommandCall cmd = new CommandCall(systemObject_, QUALIFIED_THREADSAFE_CMD);
            if (DEBUG) {
              displayCommandValues(cmd);
              System.out.println("isRunningNatively() == "+isRunningNatively()+"; "+
                                 "isSystemV5orHigher_ == "+isSystemV5orHigher_+"; "+
                                 "cmd.getSystemThread() == "+cmd.getSystemThread()+"; "+
                                 "cmd.isStayOnThread() == "+cmd.isStayOnThread()+"; "+
                                 "cmd.isThreadSafe() == "+cmd.isThreadSafe());
            }
            if (isRunningNatively())
            {
              if (isSystemV5orHigher_)
              {
                assertCondition((cmd.getSystemThread() != null) &&
                       (cmd.isStayOnThread() == false) &&
                       (cmd.isThreadSafe() == false));
              }
              else  // AS/400 is pre-V5.
              {
                assertCondition((cmd.getSystemThread() == null) &&
                       (cmd.isStayOnThread() == false) &&
                       (cmd.isThreadSafe() == false));
              }
            }
            else { // Not running on an AS/400.
                assertCondition((cmd.getSystemThread() == null) &&
                        (cmd.isStayOnThread() == false) &&
                        (cmd.isThreadSafe() == false));
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Verify DEFAULTS for a non-threadsafe command.
     <pre>
     - threadSafe property is not set
     - setThreadSafe() not done
     - setMustUseSockets() not done
     - command is designated as non-thread-safe on the AS/400.
     __________________________________________________________________

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.
     **/
    public void Var002()
    {
        try {
            CommandCall cmd = new CommandCall(systemObject_, NONTHREADSAFE_CMD);
            if (DEBUG) displayCommandValues(cmd);
            // NOTE TO TESTER: This variation will FAIL if you've
            // set the CommandCall.threadSafe property to "true".
            if (isRunningNatively()) {
                assertCondition((cmd.getSystemThread() != null) &&
                        (cmd.isStayOnThread() == false) &&
                        (cmd.isThreadSafe() == false));
            }
            else { // Not running on an AS/400.
                assertCondition((cmd.getSystemThread() == null) &&
                        (cmd.isStayOnThread() == false) &&
                        (cmd.isThreadSafe() == false));
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Verify effect of setThreadSafe(true).
     <pre>
     - threadSafe property is not set
     - setMustUseSockets() not done
     __________________________________________________________________

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return true.
     - isThreadSafe should return true.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return true.
     **/
    public void Var003()
    {
        try {
            CommandCall cmd = new CommandCall(systemObject_, NONTHREADSAFE_CMD);
            cmd.setThreadSafe(true);

            if (isRunningNatively())
            {
              if (isSystemV5orHigher_)
              {
                assertCondition((cmd.getSystemThread() != null) &&
                       (cmd.isStayOnThread() == true) &&
                       (cmd.isThreadSafe() == true));
              }
              else  // AS/400 is pre-V5.
              {
                assertCondition((cmd.getSystemThread() == null) &&
                       (cmd.isStayOnThread() == false) &&
                       (cmd.isThreadSafe() == true));
              }
            }
            else { // Not running on an AS/400.
                assertCondition((cmd.getSystemThread() == null) &&
                        (cmd.isStayOnThread() == false) &&
                        (cmd.isThreadSafe() == true));
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Verify effect of setThreadSafe(false).
     <pre>
     - threadSafe property is not set
     - setMustUseSockets() not done
     __________________________________________________________________

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.
     **/
    public void Var004()
    {
        try {
            CommandCall cmd = new CommandCall(systemObject_, THREADSAFE_CMD);
            cmd.setThreadSafe(false);
            if (isRunningNatively()) {
                assertCondition((cmd.getSystemThread() != null) &&
                        (cmd.isStayOnThread() == false) &&
                        (cmd.isThreadSafe() == false));
            }
            else { // Not running on an AS/400.
                assertCondition((cmd.getSystemThread() == null) &&
                        (cmd.isStayOnThread() == false) &&
                        (cmd.isThreadSafe() == false));
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Verify effect of threadSafe property set to true.
     <pre>
     - setThreadSafe() not done
     - setMustUseSockets() not done
     __________________________________________________________________

     Programmatically set CommandCall.threadSafe to "true".

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return true.
     - isThreadSafe should return true.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return true.
     **/
    public void Var005()
    {
      if (isApplet_)
        notApplicable("Applets cannot set system properties.");
      else
      {
        try {
          setThreadsafeProperty(true);
          CommandCall cmd = new CommandCall(systemObject_, NONTHREADSAFE_CMD);

          if (isRunningNatively())
          {
            if (isSystemV5orHigher_)
            {
              assertCondition((cmd.getSystemThread() != null) &&
                     (cmd.isStayOnThread() == true) &&
                     (cmd.isThreadSafe() == true));
            }
            else  // AS/400 is pre-V5.
            {
              assertCondition((cmd.getSystemThread() == null) &&
                     (cmd.isStayOnThread() == false) &&
                     (cmd.isThreadSafe() == true));
            }
          }
          else { // Not running on an AS/400.
            assertCondition((cmd.getSystemThread() == null) &&
                   (cmd.isStayOnThread() == false) &&
                   (cmd.isThreadSafe() == true));
          }
        }
        catch (Exception e) {
          failed(e, "Unexpected exception");
        }
        finally {
          clearThreadsafeProperty();
        }
      }
    }


    /**
     Verify effect of threadSafe property set to false.
     <pre>
     - setThreadSafe() not done
     - setMustUseSockets() not done
     __________________________________________________________________

     Programmatically set CommandCall.threadSafe to "false".

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.
     **/
    public void Var006()
    {
        if (isApplet_)
            notApplicable("Applets cannot set system properties.");
        else
        {
            try {
                setThreadsafeProperty(false);
                CommandCall cmd = new CommandCall(systemObject_, THREADSAFE_CMD);

/*
                System.out.println(cmd.getSystemThread() + ", " +
                                   cmd.isStayOnThread() + ", " +
                                   cmd.isThreadSafe());
*/

                if (isRunningNatively()) {
                    assertCondition((cmd.getSystemThread() != null) &&
                            (cmd.isStayOnThread() == false) &&
                            (cmd.isThreadSafe() == false));
                }
                else { // Not running on an AS/400.
                    assertCondition((cmd.getSystemThread() == null) &&
                            (cmd.isStayOnThread() == false) &&
                            (cmd.isThreadSafe() == false));
                }
            }
            catch (Exception e) {
                failed(e, "Unexpected exception");
            }
            finally {
                clearThreadsafeProperty();
            }
        }
    }



    /**
     Verify that setThreadSafe(true) overrides property threadSafe=false.
     <pre>
     __________________________________________________________________

     Programmatically set CommandCall.threadSafe to "false".

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return true.
     - isThreadSafe should return true.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return true.
     **/
    public void Var007()
    {
      if (isApplet_)
        notApplicable("Applets cannot set system properties.");
      else
      {
        try {
          setThreadsafeProperty(false);
          CommandCall cmd = new CommandCall(systemObject_, NONTHREADSAFE_CMD);
          cmd.setThreadSafe(true);

          if (isRunningNatively())
          {
            if (isSystemV5orHigher_)
            {
              assertCondition((cmd.getSystemThread() != null) &&
                     (cmd.isStayOnThread() == true) &&
                     (cmd.isThreadSafe() == true));
            }
            else  // System is pre-V5.
            {
              assertCondition((cmd.getSystemThread() == null) &&
                     (cmd.isStayOnThread() == false) &&
                     (cmd.isThreadSafe() == true));
            }
          }
          else { // Not running on an AS/400.
            assertCondition((cmd.getSystemThread() == null) &&
                   (cmd.isStayOnThread() == false) &&
                   (cmd.isThreadSafe() == true));
          }
        }
        catch (Exception e) {
          failed(e, "Unexpected exception");
        }
        finally {
          clearThreadsafeProperty();
        }
      }
    }



    /**
     Verify that setThreadSafe(false) overrides property threadSafe=true.
     <pre>
     __________________________________________________________________

     Programmatically set CommandCall.threadSafe to "true".

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.
     **/
    public void Var008()
    {
        if (isApplet_)
            notApplicable("Applets cannot set system properties.");
        else
        {
            try {
                setThreadsafeProperty(true);
                CommandCall cmd = new CommandCall(systemObject_, THREADSAFE_CMD);
                cmd.setThreadSafe(false);

                if (isRunningNatively()) {
                    assertCondition((cmd.getSystemThread() != null) &&
                            (cmd.isStayOnThread() == false) &&
                            (cmd.isThreadSafe() == false));
                }
                else { // Not running on an AS/400.
                    assertCondition((cmd.getSystemThread() == null) &&
                            (cmd.isStayOnThread() == false) &&
                            (cmd.isThreadSafe() == false));
                }
            }
            catch (Exception e) {
                failed(e, "Unexpected exception");
            }
            finally {
                clearThreadsafeProperty();
            }
        }
    }



    /**
     Verify that setMustUseSockets(true) overrides setThreadSafe(true).
     <pre>
     __________________________________________________________________

     If on AS/400:
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return true.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return true.
     **/
    public void Var009()
    {
        try {
            AS400 systemObj = new AS400(systemObject_);
            systemObj.setMustUseSockets(true);
            CommandCall cmd = new CommandCall(systemObj, THREADSAFE_CMD);
            cmd.setThreadSafe(true);  // suppress the lookup

            if (isRunningNatively()) {
                assertCondition((cmd.getSystemThread() == null) &&
                        (cmd.isStayOnThread() == false) &&
                        (cmd.isThreadSafe() == true));
            }
            else { // Not running on an AS/400.
                assertCondition((cmd.getSystemThread() == null) &&
                        (cmd.isStayOnThread() == false) &&
                        (cmd.isThreadSafe() == true));
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



    /**
     Verify that when isStayOnThread() returns true,
     the command actually gets run on-thread.
     <pre>
     __________________________________________________________________

     (Do this in an attended testcase.
     Programmatically activate information tracing, and have the
     tester watch for information Trace message
     "running command on-thread".)
     **/
    public void Var010(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) {
        output_.println("attended");
	notApplicable("attended"); 
        return;
      }
      if (!isRunningNatively()) {
        notApplicable("native only");
        return;
      }
      if (!isSystemV5orHigher_) {
        notApplicable("Running on a pre-V5 AS/400");
        return;
      }
      boolean result = false; 
      try {
        CommandCall cmd = new CommandCall(systemObject_, QUALIFIED_THREADSAFE_CMD);
        cmd.setThreadSafe(true);  // Suppress the lookup
        Trace.setTraceInformationOn(true);
        Trace.setTraceOn(true);
        result = cmd.run();
        String text1 = "\nTESTER: Please verify that the following message " +
          "appeared:\n" +
          "        \"Invoking native method.\"";
        String text2 = "        If it did not appear, FAIL this variation.\n";
        output_.println(text1);
        output_.println(text2);
        succeeded();
      }
      catch (Exception e) {
        failed(e, "Unexpected exception result="+result);
      }
      finally {
        Trace.setTraceInformationOn(TRACE_INFORMATION_SETTING);
        Trace.setTraceOn(TRACE_SETTING);
        try { cleanup(); } catch (Exception e) {}
      }
    }



    /**
     Verify that when isStayOnThread() returns false,
     the command does *not* get run on-thread.
     <pre>
     __________________________________________________________________

     (Do this in an attended testcase.
     Programmatically activate information tracing, and have the
     tester watch for information Trace message
     "running command via sockets".)
     **/
    public void Var011(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) {
        output_.println("Variation 11 not run: Attended testcase");
	notApplicable("Variation 11 not run: Attended testcase");
        return;
      }
      if (!isRunningNatively()) {
        notApplicable("native only");
        return;
      }
      if (!isSystemV5orHigher_) {
        notApplicable("Running on a pre-V5 AS/400");
        return;
      }

      try {
        CommandCall cmd = new CommandCall(systemObject_, QUALIFIED_THREADSAFE_CMD);
        cmd.setThreadSafe(false);  // Suppress the lookup
        Trace.setTraceInformationOn(true);
        Trace.setTraceOn(true);
        boolean result = cmd.run();
        String text1 = "\nTESTER: Please verify that the following message " +
          "did *NOT* appear:\n" +
          "        \"Invoking native method.\"";
        String text2 = "        If it *DID* appear, FAIL this variation.\n";
        output_.println(text1);
        output_.println(text2);
        output_.println("result is "+result); 
        succeeded();
      }
      catch (Exception e) {
        failed(e, "Unexpected exception");
      }
      finally {
        Trace.setTraceInformationOn(TRACE_INFORMATION_SETTING);
        Trace.setTraceOn(TRACE_SETTING);
        try { cleanup(); } catch (Exception e) {}
      }
    }


    /**
     Verify DEFAULTS for a non-existent command.
     <pre>
     - threadSafe property is not set
     - setThreadSafe() not done
     - setMustUseSockets() not done
     - command does not exist on the AS/400.
     __________________________________________________________________

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.
     **/
    public void Var012()
    {
        try {
            CommandCall cmd = new CommandCall(systemObject_, NONEXISTENT_CMD);
            if (DEBUG) displayCommandValues(cmd);
            // NOTE TO TESTER: This variation will FAIL if you've
            // set the CommandCall.threadSafe property to "true".
            if (isRunningNatively()) {
                assertCondition((cmd.getSystemThread() != null) &&
                        (cmd.isStayOnThread() == false) &&
                        (cmd.isThreadSafe() == false));
            }
            else { // Not running on an AS/400.
              assertCondition((cmd.getSystemThread() == null) &&
                     (cmd.isStayOnThread() == false) &&
                     (cmd.isThreadSafe() == false));
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }


    /**
     If running on the iSeries JVM, try calling a command simultaneously on multiple threads.
     **/
    public void Var013()
    {
      if (!isRunningNatively()) {
        notApplicable("native only");
        return;
      }
      try {
        CmdMultiThread.initializeStatus();
        CmdMultiThread.main(new String[] {"cmd"});  // run the command across multiple threads
        assertCondition(CmdMultiThread.getStatus() == true);
      }
      catch (Exception e) {
        failed(e, "Unexpected exception");
      }
    }

}
