///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CmdMultiThread.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.Cmd;

import com.ibm.as400.access.*;
import com.ibm.as400.security.auth.*;
import com.ibm.as400.resource.*;
import java.net.*;
import java.io.*;
import java.util.*;

// Note: This class is used by CmdOnThreadTestcase and PgmOnThreadTestcase.
public class CmdMultiThread extends Object implements Runnable
{
   ///static AS400 systemProperty;
   private static boolean succeeded_ = true;
   private static boolean DEBUG = false;
   private final static String COMMAND_CALL = "command";
   private final static String PROGRAM_CALL = "program";
   private static String programOrCommand_;


///   CmdMultiThread(AS400 system_)
///   {
///      systemProperty = system_;
///   }

   CmdMultiThread()
   {
   }

   public static boolean getStatus()
   {
     return succeeded_;
   }

   public static void initializeStatus()
   {
     succeeded_ = true;
   }

   public static void main(String[] args)  // we expect "cmd" or "pgm" for arg.
   {
     if (args.length != 1) {
       System.err.println("Wrong number of arguments: " + args.length);
       return;
     }
     String arg0 = args[0].toLowerCase();
     if (arg0.startsWith("pgm") ||
         arg0.startsWith("program")) {
       programOrCommand_ = PROGRAM_CALL;
     }
     else if (arg0.startsWith("cmd") ||
              arg0.startsWith("command")) {
       programOrCommand_ = COMMAND_CALL;
     }
     else {
       System.out.println("ERROR: Argument not valid: " + args[0]);
       succeeded_ = false;
       return;
     }

     if (DEBUG) System.out.println("starting CmdMultiThread");
     ///CmdMultiThread sp = new CmdMultiThread();
     ///sp.Main(args);

     try
     {
       ///AS400 s = new AS400();
       ///s.connectService(AS400.COMMAND);
       for (int i = 0; i < 100; i++)
       {
         Runnable sp = new CmdMultiThread(/*s*/);
         new Thread(sp).start();
       }
     }
     catch (Exception e) {
       succeeded_ = false;
       e.printStackTrace();
     }

     if (DEBUG) System.out.println("CmdMultiThread main thread ending");
     ///if (succeeded_) System.out.println("Succeeded!");
     ///else System.out.println("Failed!");
     ///System.exit(0);
   }

///                    // This method is private so it can be called only
///                    // other methods in CmdMultiThread.  Java is case
///                    // sensative so methods can have the same name
///                    // differing only by case.  This is not recommended
///                    // except for main, however.
///   private void Main(String[] args)
///   {
///      MainWithThreads2(args);
///   }
///
///
///                    // This method is private so it can be called only
///                    // other methods in CmdMultiThread.  Java is case
///                    // sensative so methods can have the same name
///                    // differing only by case.  This is not recommended
///                    // except for main, however.
///   private void MainWithThreads2(String[] args)
///   {
///      try
///      {
///         AS400 s = new AS400();
///         s.connectService(AS400.COMMAND);
///         for (int i = 0; i < 100; i++)
///         {
///           Runnable sp = new CmdMultiThread(s);
///           new Thread(sp).start();
///         }
///      }
///      catch (Exception e) {
///        succeeded_ = false;
///        e.printStackTrace();
///      }
///   }


   public void run()
   {
      try
      {
         if (DEBUG) System.out.println("calling " + programOrCommand_ + " " + Thread.currentThread().toString());
         if (programOrCommand_ == PROGRAM_CALL) {
           pgm3();
         }
         else {
           cmd2();
         }
         if (DEBUG) System.out.println("called " + programOrCommand_ + " " + Thread.currentThread().toString());
      }
      catch (Exception e) {
         succeeded_ = false;
         e.printStackTrace();
      }
   }


   void pgm3()
   {
         try
         {
             String msgId, msgText;

             AS400 as400System = new AS400();
             // AS400 as400System = systemProperty;

             AS400Bin4 bin4   = new AS400Bin4();
             AS400Text char6  = new AS400Text(6,  as400System);
             AS400Text char7  = new AS400Text(7,  as400System);
             AS400Text char8  = new AS400Text(8,  as400System);
             AS400Text char10 = new AS400Text(10, as400System);

             System.setErr(System.out);

             ProgramCall pc = new ProgramCall(as400System);
             pc.setThreadSafe(true);
             pc.setProgram("/QSYS.LIB/QSYRUSRI.PGM");

             ProgramParameter[] parms = new ProgramParameter[5];

             // First parm is the output area that contains the result
             parms[0] = new ProgramParameter(100);

             // Second parm is the size of the output area
             // (100 bytes in our case)
             parms[1] = new ProgramParameter(bin4.toBytes(100));

             // Third parm is the output format to use
             parms[2] = new ProgramParameter(char8.toBytes("USRI0100"), 5);

             // Fourth parm is the user profile
             parms[3] = new ProgramParameter(char10.toBytes("*CURRENT"));

             // Fifth parm is the error area
             byte[] errorArea = new byte[32];
             parms[4] = new ProgramParameter(errorArea, 32);

             // System.out.println("Beginning ProgramCall Example..");
             // System.out.println("    Setting input parameters...");

             pc.setParameterList(parms);

             // If return code is false, we received messages from the AS/400
             if (pc.run() == false)
             {
                 // Retrieve list of AS/400 messages
                 AS400Message[] msgs = pc.getMessageList();

                 // Iterate through messages and write them to standard output
                 for (int m = 0; m < msgs.length; m++)
                 {
                     msgId = msgs[m].getID();
                     msgText = msgs[m].getText();
                     System.out.println("    " + msgId + " - " + msgText);
                 }
                 System.out.println("** Call to QSYRUSRI failed. See messages above **");
             }
             else
             {
                 byte[] data = parms[0].getOutputData();

                 int value   = ((Integer) bin4.toObject(data)).intValue();
                 if (DEBUG) System.out.println("        Bytes returned:      " + value);

                 value   = ((Integer) bin4.toObject(data, 4)).intValue();
                 if (DEBUG) System.out.println("        Bytes available:     " + value);

                 String strValue = (String) char10.toObject(data, 8);
                 if (DEBUG) System.out.println("        Profile name:        " + strValue);

                 strValue = (String) char7.toObject(data, 18);
                 if (DEBUG) System.out.println("        Previous signon date:" + strValue);

                 strValue = (String) char6.toObject(data, 25);
                 if (DEBUG) System.out.println("        Previous signon time:" + strValue);
             }
         }
         catch (Exception e) {
             succeeded_ = false;
             System.out.println("Unexpected Exception ");
             e.printStackTrace();
             System.out.println("*** Call to QSYRUSRI failed. ***");
         }

   }


   void cmd2()
   {
      try
      {
         AS400 system = new AS400();
         CommandCall cc = new CommandCall(system);
         // cc.run("QDEVELOP/DSPDRVLVL");
         cc.setThreadSafe(true);
         cc.run("CRTLIB FRED");
         if (DEBUG)
         {
           AS400Message[] ml = cc.getMessageList();
           for (int i=0; i<ml.length; i++)
           {
             System.out.println(ml[i]);
             System.out.println(ml[i].getPath());
           }
         }

      }
      catch (Exception e) {
        succeeded_ = false;
        e.printStackTrace();
      }
   }



}


