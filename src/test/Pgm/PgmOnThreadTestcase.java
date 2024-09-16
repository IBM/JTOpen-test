///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PgmOnThreadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Pgm;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.BinaryConverter;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.Trace;

import test.Testcase;
import test.Cmd.CmdMultiThread;

import java.util.Properties;

/**
 The PgmOnThreadTestcase class tests the following methods of the ProgramCall class.
 <ul>
 <li>isThreadSafe()
 <li>setThreadSafe()
 <li>isStayOnThread()
 <li>getSystemThread()
 </ul>
 **/
public class PgmOnThreadTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PgmOnThreadTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PgmTest.main(newArgs); 
   }
    String goodPgm_ = "/QSYS.LIB/W95LIB.LIB/PROG3.PGM";
    ProgramParameter[] parmlist_;
    byte[] data0;
    byte[] data1;
    ProgramParameter parm0, parm1, parm2;

    static final boolean DEBUG = false;

    private static final String SYSTEM_PROP_NAME = "com.ibm.as400.access.ProgramCall.threadSafe";
    private static boolean TRACE_INFORMATION_SETTING = Trace.isTraceInformationOn();
    private static boolean TRACE_SETTING = Trace.isTraceOn();
    private static boolean isSystemV5_; // is the AS/400 version V5 or later

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        clearThreadsafeProperty();
        if (systemObject_.getVersion() >= 5)
            isSystemV5_ = true;
        else
            isSystemV5_ = false;
    }

    private final boolean isRunningNatively()
    {
      if (onAS400_ /*&& isNative_ && isLocal_*/ && systemObject_.canUseNativeOptimizations())
      {
        return true;
      }
      else return false;
    }

    ProgramParameter[] buildParms()
    {
        parmlist_ = new ProgramParameter[3];

        data0 = new byte[5];
        data0[0] = 7;
        data0[1] = 6;
        parmlist_[0] = parm0 = new ProgramParameter(data0, 500);

        data1 = new byte[2];
        data1[0] = (byte)4;
        data1[1] = (byte)2;
        parmlist_[1] = parm1 = new ProgramParameter(data1);
        parmlist_[2] = parm2 = new ProgramParameter(2);

        return parmlist_;
    }

    /**
     Display state of program.
     **/
    private void displayProgramValues (ProgramCall pgm)
    {
        /*      System.out.println("pgm.getSystem () == " + pgm.getSystem ()); 
         System.out.println("pgm.getProgram () == " + pgm.getProgram ()); 
         System.out.println("pgm.toString () == " + pgm.toString ()); 
         System.out.println("pgm.getSystemThread () == " + pgm.getSystemThread ()); 
         System.out.println("pgm.isStayOnThread () == " + pgm.isStayOnThread ()); 
         System.out.println("pgm.isThreadSafe () == " + pgm.isThreadSafe ()); 
         */    }

    /**
     Validate the value reported by getSystemThread().
     **/
    /*    private boolean validateSystemThread (ProgramCall pgm)
     {
     boolean result;
     if (pgm.isStayOnThread() == true)
     return (pgm.getSystemThread() != null);
     else
     return (pgm.getSystemThread() == null);
     }
     */

    /**
     Set the "ProgramCall.threadSafe" system property.
     **/
    private void setThreadsafeProperty (boolean state)
    {
        Properties sysProps = System.getProperties();
        if (state == true)
            sysProps.put(SYSTEM_PROP_NAME, "true");
        else
            sysProps.put(SYSTEM_PROP_NAME, "false");
        System.setProperties(sysProps);
    }

    /**
     Clear the "ProgramCall.threadSafe" system property.
     **/
    private void clearThreadsafeProperty ()
    {
        Properties sysProps = System.getProperties();
        sysProps.remove(SYSTEM_PROP_NAME);
        System.setProperties(sysProps);
    }

    /**
     Verify DEFAULTS.
     <pre>
     - threadSafe property is not set.
     - setThreadSafe() not done.
     - setMustUseSockets() not done.
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
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
            if (isRunningNatively()) {
                assertCondition ((pgm.getSystemThread () != null) &&
                                 (pgm.isStayOnThread () == false) &&
                                 (pgm.isThreadSafe () == false));
            }
            else { // Not running on an AS/400.
                assertCondition ((pgm.getSystemThread () == null) &&
                                 (pgm.isStayOnThread () == false) &&
                                 (pgm.isThreadSafe () == false));
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
    public void Var002()
    {
        try {
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
            pgm.setThreadSafe(true);

            if (isRunningNatively())
            {
                if (isSystemV5_)
                {
                    assertCondition ((pgm.getSystemThread () != null) &&
                                     (pgm.isStayOnThread () == true) &&
                                     (pgm.isThreadSafe () == true));
                }
                else
                {
                    assertCondition ((pgm.getSystemThread () == null) &&
                                     (pgm.isStayOnThread () == false) &&
                                     (pgm.isThreadSafe () == true));
                }
            }
            else { // Not running on an AS/400.
                assertCondition ((pgm.getSystemThread () == null) &&
                                 (pgm.isStayOnThread () == false) &&
                                 (pgm.isThreadSafe () == true));
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
    public void Var003()
    {
        try {
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
            pgm.setThreadSafe(false);
            if (isRunningNatively()) {
                assertCondition ((pgm.getSystemThread () != null) &&
                                 (pgm.isStayOnThread () == false) &&
                                 (pgm.isThreadSafe () == false));
            }
            else { // Not running on an AS/400.
                assertCondition ((pgm.getSystemThread () == null) &&
                                 (pgm.isStayOnThread () == false) &&
                                 (pgm.isThreadSafe () == false));
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

     Programmatically set ProgramCall.threadSafe to "true".

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return true.
     - isThreadSafe should return true.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return true.
     **/
    public void Var004()
    {
        if (isApplet_)
            notApplicable("Applets cannot set system properties.");
        else
        {
            try {
                setThreadsafeProperty(true);
                ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );

                if (isRunningNatively())
                {
                    if (isSystemV5_)
                    {
                        assertCondition ((pgm.getSystemThread () != null) &&
                                         (pgm.isStayOnThread () == true) &&
                                         (pgm.isThreadSafe () == true));
                    }
                    else
                    {
                        assertCondition ((pgm.getSystemThread () == null) &&
                                         (pgm.isStayOnThread () == false) &&
                                         (pgm.isThreadSafe () == true));
                    }
                }
                else { // Not running on an AS/400.
                    assertCondition ((pgm.getSystemThread () == null) &&
                                     (pgm.isStayOnThread () == false) &&
                                     (pgm.isThreadSafe () == true));
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

     Programmatically set ProgramCall.threadSafe to "false".

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.
     **/
    public void Var005()
    {
        if (isApplet_)
            notApplicable("Applets cannot set system properties.");
        else
        {
            try {
                setThreadsafeProperty(false);
                ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );

                if (isRunningNatively()) {
                    assertCondition ((pgm.getSystemThread () != null) &&
                                     (pgm.isStayOnThread () == false) &&
                                     (pgm.isThreadSafe () == false));
                }
                else { // Not running on an AS/400.
                    assertCondition ((pgm.getSystemThread () == null) &&
                                     (pgm.isStayOnThread () == false) &&
                                     (pgm.isThreadSafe () == false));
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

     Programmatically set ProgramCall.threadSafe to "false".

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return true.
     - isThreadSafe should return true.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return true.
     **/
    public void Var006()
    {
        if (isApplet_)
            notApplicable("Applets cannot set system properties.");
        else
        {
            try {
                setThreadsafeProperty(false);
                ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
                pgm.setThreadSafe(true);

                if (isRunningNatively())
                {
                    if (isSystemV5_)
                    {
                        assertCondition ((pgm.getSystemThread () != null) &&
                                         (pgm.isStayOnThread () == true) &&
                                         (pgm.isThreadSafe () == true));
                    }
                    else
                    {
                        assertCondition ((pgm.getSystemThread () == null) &&
                                         (pgm.isStayOnThread () == false) &&
                                         (pgm.isThreadSafe () == true));
                    }
                }
                else { // Not running on an AS/400.
                    assertCondition ((pgm.getSystemThread () == null) &&
                                     (pgm.isStayOnThread () == false) &&
                                     (pgm.isThreadSafe () == true));
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

     Programmatically set ProgramCall.threadSafe to "true".

     If on AS/400:
     - getSystemThread should return non-null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.

     Otherwise (if not running on AS/400):
     - getSystemThread should return null.
     - isStayOnThread should return false.
     - isThreadSafe should return false.
     **/
    public void Var007()
    {
        if (isApplet_)
            notApplicable("Applets cannot set system properties.");
        else
        {
            try {
                setThreadsafeProperty(true);
                ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
                pgm.setThreadSafe(false);

                if (isRunningNatively()) {
                    assertCondition ((pgm.getSystemThread () != null) &&
                                     (pgm.isStayOnThread () == false) &&
                                     (pgm.isThreadSafe () == false));
                }
                else { // Not running on an AS/400.
                    assertCondition ((pgm.getSystemThread () == null) &&
                                     (pgm.isStayOnThread () == false) &&
                                     (pgm.isThreadSafe () == false));
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
    public void Var008()
    {
        try {
            AS400 systemObj = new AS400(systemObject_);
            systemObj.setMustUseSockets(true);
            ProgramCall pgm = new ProgramCall(systemObj, goodPgm_, buildParms() );
            pgm.setThreadSafe(true);

            if (DEBUG) displayProgramValues(pgm);

            if (isRunningNatively()) {
                assertCondition ((pgm.getSystemThread () == null) &&
                                 (pgm.isStayOnThread () == false) &&
                                 (pgm.isThreadSafe () == true));
            }
            else { // Not running on an AS/400.
                assertCondition ((pgm.getSystemThread () == null) &&
                                 (pgm.isStayOnThread () == false) &&
                                 (pgm.isThreadSafe () == true));
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify that when isStayOnThread() returns true,
     the program actually gets run on-thread.
     <pre>
     __________________________________________________________________

     (Do this in an attended testcase.
     Programmatically activate information tracing, and have the
     tester watch for information Trace message
     "running program on-thread".)
     **/
    public void Var009(int runMode)
    {
        if (runMode != ATTENDED && runMode != BOTH) {
            notApplicable("Variation 10 not run: Attended testcase");
            return;
        }
        if (!isRunningNatively()) {
            notApplicable("native only");
            return;
        }
        if (!isSystemV5_) {
            notApplicable("Running on a pre-V5 AS/400");
            return;
        }

        try {
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
            pgm.setThreadSafe(true);  // Suppress the lookup
            Trace.setTraceInformationOn(true);
            Trace.setTraceOn(true);
            boolean result = pgm.run();
            String text1 = "\nTESTER: Please verify that the following message " +
              "appeared:\n" +
              "        \"Invoking native method.\"";
            String text2 = "        If it did not appear, FAIL this variation.\n";
            output_.println(text1);
            output_.println(text2);
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
     Verify that when isStayOnThread() returns false,
     the program does *not* get run on-thread.
     <pre>
     __________________________________________________________________

     (Do this in an attended testcase.
     Programmatically activate information tracing, and have the
     tester watch for information Trace message
     "running program via sockets".)
     **/
    public void Var010(int runMode)
    {
        if (runMode != ATTENDED && runMode != BOTH) {
            notApplicable("Variation 10 not run: Attended testcase");
            return;
        }
        if (!isRunningNatively()) {
            notApplicable("native only");
            return;
        }
        if (!isSystemV5_) {
            notApplicable("Running on a pre-V5 AS/400");
            return;
        }

        try {
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
            pgm.setThreadSafe(false);  // Suppress the lookup
            Trace.setTraceInformationOn(true);
            Trace.setTraceOn(true);
            boolean result = pgm.run();
            String text1 = "\nTESTER: Please verify that the following message " +
              "did *NOT* appear:\n" +
              "        \"Invoking native method.\"";
            String text2 = "        If it *DID* appear, FAIL this variation.\n";
            output_.println(text1);
            output_.println(text2);
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
     If running on the iSeries JVM, try calling a program simultaneously on multiple threads.
     **/
    public void Var011()
    {
        if (!isRunningNatively()) {
            notApplicable("native only");
            return;
        }
        try {
            CmdMultiThread.initializeStatus();
            CmdMultiThread.main(new String[] {"pgm"});  // run the program across multiple threads
            assertCondition(CmdMultiThread.getStatus() == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }

    /**
     * If running on the iSeries JVM, try calling a program that throws a CPF9801
     * to make sure that the run() method still returns.
     **/
    public void Var012()
    {
        if (!isRunningNatively())
        {
            notApplicable("native only");
            return;
        }
        try
        {
            // Use an existing library with a non-existent object name to generate a CPF9801.
            ProgramParameter[] parms = new ProgramParameter[5];
            parms[0] = new ProgramParameter(90); // output
            parms[1] = new ProgramParameter(BinaryConverter.intToByteArray(90)); // length
            parms[2] = new ProgramParameter(CharConverter.stringToByteArray(37, systemObject_, "OBJD0100")); // format name
            parms[3] = new ProgramParameter(CharConverter.stringToByteArray(37, systemObject_, "OBJNAME890QSYS      ")); // object/library name
            parms[4] = new ProgramParameter(CharConverter.stringToByteArray(37, systemObject_, "*USRPRF   ")); // object type

            ProgramCall pc = new ProgramCall(systemObject_, "/QSYS.LIB/QUSROBJD.PGM", parms);
            pc.setThreadSafe(true); // Run on-thread

            try
            {
                pc.run();
                AS400Message[] msgs = pc.getMessageList();
                // for (int i=0; i<msgs.length; ++i) System.out.println(msgs[i]);
                succeeded();
            }
            catch (ObjectDoesNotExistException odnee)
            {
                failed(odnee, "ObjectDoesNotExistException was still thrown.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     * If running on the iSeries JVM, try calling a program that throws a CPF9810
     * to make sure that the run() method still returns.
     **/
    public void Var013()
    {
        if (!isRunningNatively())
        {
            notApplicable("native only");
            return;
        }
        try
        {
            // Use a non-existent library to generate a CPF9810.
            ProgramParameter[] parms = new ProgramParameter[5];
            parms[0] = new ProgramParameter(90); // output
            parms[1] = new ProgramParameter(BinaryConverter.intToByteArray(90)); // length
            parms[2] = new ProgramParameter(CharConverter.stringToByteArray(37, systemObject_, "OBJD0100")); // format name
            parms[3] = new ProgramParameter(CharConverter.stringToByteArray(37, systemObject_, "QCUSTCDT  LIBNAME890")); // object/library name
            parms[4] = new ProgramParameter(CharConverter.stringToByteArray(37, systemObject_, "*USRPRF   ")); // object type

            ProgramCall pc = new ProgramCall(systemObject_, "/QSYS.LIB/QUSROBJD.PGM", parms);
            pc.setThreadSafe(true); // Run on-thread

            try
            {
                pc.run();
                AS400Message[] msgs = pc.getMessageList();
                // for (int i=0; i<msgs.length; ++i) System.out.println(msgs[i]);
                succeeded();
            }
            catch (ObjectDoesNotExistException odnee)
            {
                failed(odnee, "ObjectDoesNotExistException was still thrown.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
