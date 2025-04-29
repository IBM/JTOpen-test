///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.JTA;

// import java.lang.*;
// import java.sql.*;
import java.sql.Connection;
import java.util.*;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JDTestcase;
import test.JTATest;
import test.JD.JDTestUtilities;

import java.io.*;
import java.net.*;


public class JTACrash2 extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTACrash2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTATest.main(newArgs); 
   }

   private static int WAIT_FOR_CLIENT_MILLIS  = 60000;

   // private String basTbl = JTATest.COLLECTION + ".CRASH2";
   private Connection c;
   // private Connection gc;
   String cshMsg = "Ready to Crash"; // this should be the same as in JTACrash2Vars.java
   ServerSocket serverSocket = null;
   Socket clientSocket = null;
   PrintWriter out = null;
   BufferedReader in = null;
   String xidStr;
   String errorMessage=""; 
   boolean isIasp = false; 
   boolean isNTS = false; 
   boolean validRelease = true; 

   // This file and JTACrash2Vars.java are both needed to run the JTACrash2 testcase
   // JTACrash2Vars.java contains the variations

/**
Constructor.
**/
   public JTACrash2 (AS400 systemObject,
                     Hashtable<String,Vector<String>> namesAndVars,
                     int runMode,
                     FileOutputStream fileOutputStream,
                     
                     String password) {
      super (systemObject, "JTACrash2",
             namesAndVars, runMode, fileOutputStream,
             password);
   }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
   protected void setup ()
   throws Exception
   {
       lockSystem("JTATEST",600);
       // 
       // Only NTS tests are valid in V7R1M0 and later
       // 
       if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	   if (!isNTS) { 
	       validRelease = false;
	   }
       }
       if (validRelease) { 
       // basTbl = JTATest.COLLECTION + ".CRASH2";
	   if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
	       JTATest.verboseOut(baseURL_);
	       c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	       isIasp = JDTestUtilities.isIasp(c); 

	       c.close(); 

	 // c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	   }
       }
   }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
       if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {

      }
       unlockSystem(); 
   }

   /**
    * nested Thread class to listen for the socket
    */ 
   class SocketAcceptThread extends Thread {
       public SocketAcceptThread() {
           // Clear the client socket before we start running
	   clientSocket = null; 
       } 
       public void run() {
	   try {
	       // System.out.println("SocketAcceptThread running"); 
	       clientSocket = serverSocket.accept();
	   } catch (Exception e) {
	       e.printStackTrace();
	   } 
       } 
   }

   void showProcessOutput(Process p) {
       try {
	   System.out.println("\nOutput of process : "+p);
           System.out.println("---------------------------------"); 
	   InputStream iStream = p.getInputStream();
	   int readByte;
	   readByte = iStream.read();
	   while (readByte != -1 ) {
	       System.out.write((char) readByte);
	       readByte = iStream.read();
	   }
           System.out.println("\n---------------------------------"); 
	   System.out.println("ErrorOutput of process : "+p); 
           System.out.println("---------------------------------"); 
	   iStream = p.getErrorStream(); 
	   readByte = iStream.read();
	   while (readByte != -1 ) {
	       System.out.write((char) readByte);
	       readByte = iStream.read();
	   }
           System.out.println("\n---------------------------------"); 

       } catch (Exception e) {
	   e.printStackTrace();
       }
   } 


   protected Process setupSocket() throws Exception {
      JTATest.verboseOut("create the server socket");
      int      serverPort = 0;
      try {
         serverSocket = new ServerSocket(0);
         serverPort = serverSocket.getLocalPort();
      }
      catch (IOException e) {
         System.err.println("Could not create and listen on server socket.");
         throw e;
      }

      // start the variation
      JTATest.verboseOut("Start the client program in a process");
      // Propagate the following JAVA properties to the sub process if present
      //   jta.secondary.system
      //   jta.verbose
      String propSecSys = "";
      if (System.getProperty("jta.secondary.system") != null) {
         propSecSys = " -Djta.secondary.system=" +
                      System.getProperty("jta.secondary.system") + " ";
      }
      String propVerbose = "";
      if (System.getProperty("jta.verbose") != null) {
         propVerbose = " -Djta.verbose=" +
                       System.getProperty("jta.verbose") + " ";
      }
      String propSocket = " -Djta.server.socket=" + serverPort + " ";
      String propClasspath = " -classpath \"" +
                             System.getProperty("java.class.path") + "\" ";
      String[] cmdA = new String[7];
      cmdA[0] = "java";
      cmdA[1] = "-classpath";
      cmdA[2] = System.getProperty("java.class.path");
      cmdA[3] = propSocket.trim();
      cmdA[4] = propSecSys.trim();
      cmdA[5] = propVerbose.trim();
      cmdA[6] = "test.JTACrash2Vars"; 

      String command =
            "java " + propClasspath + propSocket +
            propSecSys + propVerbose +
            " test.JTACrash2Vars";
      JTATest.verboseOut("Start subprocess: " + command);
      Process p = Runtime.getRuntime().exec(cmdA /* command */ );

//       // Propagate the following JAVA properties to the sub process if present
//       // jta.secondary.system
//       // jta.verbose
//       String propSecSys = "";
//       if (System.getProperty("jta.secondary.system") != null) {
//          propSecSys = " -Djta.secondary.system=" +
//                       System.getProperty("jta.secondary.system") + " ";
//       }
//       String propVerbose = "";
//       if (System.getProperty("jta.verbose") != null) {
//          propVerbose = " -Djta.verbose=" +
//                        System.getProperty("jta.verbose") + " ";
//       }
//       String propSocket = " -Djta.server.socket=" + serverPort + " ";
//       String propClasspath = " -classpath \"" +
//                              System.getProperty("java.class.path") + "\" ";
//       String command =
//                        System.getProperty("java.class.path") + " " +
//             propSecSys + propVerbose +
//                        " test.JTACrash2Vars";
//       JTATest.verboseOut("Start subprocess: " + command);
//       Process p = Runtime.getRuntime().exec(command);

      JTATest.verboseOut("wait for the client to connect to the server socket");

      try {
         //
         // Use a separate thread to do the accept.. This will
         // keep this testcase from hanging in the case that the client never
         // connects back
         // 
	 SocketAcceptThread socketAcceptThread = new SocketAcceptThread();
	 socketAcceptThread.start();
	 int waitTime = 0; 
	 while ( clientSocket == null) {
	     if (waitTime > WAIT_FOR_CLIENT_MILLIS) {
		 System.out.println("ERROR:  Client not connecting after "+WAIT_FOR_CLIENT_MILLIS+" milliseconds");
		 System.out.println("Destroying process");
		 p.destroy(); 
                 System.out.println("Command was "+command); 
                 System.out.println("Here is process's output");
                 showProcessOutput(p); 
		 throw new Exception("Client not responding");
      }
	     Thread.sleep(100);
	     waitTime += 100; 
	 } 
      }
      catch (IOException e) {
         System.err.println("Accept failed.");
         throw e;
      }



      JTATest.verboseOut("get the I/O streams to the socket");
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      return p;
   }

   protected void cleanupSocket() throws Exception {
      JTATest.verboseOut("cleanupSocket");
      out.close();
      in.close();
      clientSocket.close();
      serverSocket.close();
   }

   // TO Test:
   // There are 4 points (*) during a transaction where we try to
   // simulate a crash and see the results.
   // | Start (do work)  *    End  * Prepare * Commit/rollback *  |
   // This testcase is similar to JTACrash1.java
   // In this testcase "Crash" scenario is created by abending the job
   // that is running the variation


   // Returns true if variation is run 
   public boolean RunVar(String variation) {

       if (!validRelease) {
	   notApplicable("Not valid V7R1 or later");
	   return false; 
       } 
       if (isIasp && (! isNTS)) {
	   notApplicable("Not applicable for IASP and not NTS");
	   return false; 
       }

      try {
         Process p = setupSocket();

         JTATest.verboseOut("ask the client to run the variation");
         errorMessage +=    "ask the client to run the variation\n";
         // Tell the subprocess which DB its dealing with
         out.println("DBName:" + system_);
         // what collection to use
         JTATest.verboseOut("Collection:" + JTATest.COLLECTION);
	 errorMessage +=    "Collection:" + JTATest.COLLECTION+"\n"; 
         out.println("Collection:" + JTATest.COLLECTION);
         // ask the program to run the variation
         out.println(variation);

         JTATest.verboseOut("wait till the client is ready to be toasted");
	 errorMessage+=     "wait till the client is ready to be toasted\n";
         // now wait till the var program is ready to be abended
         String fromCrash2Vars;
         while ((fromCrash2Vars = in.readLine()) != null) {
             JTATest.verboseOut("fromCrash2Vars: " + fromCrash2Vars);
	    errorMessage+=     "from sub job: " + fromCrash2Vars+"\n";

             if (fromCrash2Vars.startsWith("TestXid : L"))
               xidStr = fromCrash2Vars;
            if (fromCrash2Vars.equals(cshMsg))
               break;
         }

         JTATest.verboseOut("Abend the process running the Variation");
         errorMessage += "Abend the process running the Variation\n"; 
         // Toast the Variation
         p.destroy();


         while ((fromCrash2Vars = in.readLine()) != null) {
             JTATest.verboseOut("fromCrash2Vars: " + fromCrash2Vars);
 	     errorMessage+=     "from sub job: " + fromCrash2Vars+"\n";

         }


         cleanupSocket();
      }
      catch (Exception e) {
         failed(e, "Unexpected Exception");
	 return false; 
      }
      return true; 
   }


   public boolean VfyVar(String variation) {
      try {
         Process p = setupSocket();
         boolean success = false;

         // Tell the subprocess which DB its dealing with
         out.println("DBName:" + system_);
         // what collection to use
         JTATest.verboseOut("Collection:" + JTATest.COLLECTION);
	 errorMessage+="Collection:" + JTATest.COLLECTION+"\n";

         out.println("Collection:" + JTATest.COLLECTION);
         // Tell it which XID its dealing with
         out.println(xidStr);
         // Tell it which variation to run.
         out.println(variation);

         String fromCrash2Vars;
         while ((fromCrash2Vars = in.readLine()) != null) {
             JTATest.verboseOut("fromCrash2Vars: " + fromCrash2Vars);
	    errorMessage+=     "from sub job: " + fromCrash2Vars+"\n";
            if (fromCrash2Vars.equals("SUCCESS")) {
               success = true;
               break;
            }
            if (fromCrash2Vars.equals("FAILURE")) {
               success = false;
               break;
            }
         }

         p.destroy();
         cleanupSocket();
         JTATest.verboseOut("success " + success);
	 errorMessage+=     "success " + success+"\n";
         return success;
      }
      catch (Exception e) {
         if (JTATest.verbose) {
            e.printStackTrace();
         }
         return false;
      }
   }


   public void Var001() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var001")) { 
	      assertCondition(VfyVar("Var101"), "Could not verify variation Var101: "+errorMessage);
	  }
      }
       }
   }

   public void Var002() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var002")) { 
	      assertCondition(VfyVar("Var102"),errorMessage);
	  }
      }
       }
   }

   public void Var003() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var003")) { 
	      assertCondition(VfyVar("Var103"),errorMessage);
	  }
      }
       }
   }

   public void Var004() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var004")) { 
	      assertCondition(VfyVar("Var104"),errorMessage);
	  }
      }
       }
   }

   public void Var005() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var005")) { 
	      assertCondition(VfyVar("Var105"),errorMessage);
	  }
      }
       }
   }

   public void Var006() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var006")) { 
	      assertCondition(VfyVar("Var106"),errorMessage);
	  }
      }
       }
   }

   public void Var007() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var007")) { 
	      assertCondition(VfyVar("Var107"),errorMessage);
	  }
      }
       }
   }

   public void Var008() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var008")){ 
	      assertCondition(VfyVar("Var108"),errorMessage);
	  }
      }
       }
   }

   public void Var009() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var009")) { 
	      assertCondition(VfyVar("Var109"),errorMessage);
	  }
      }
       }
   }

   public void Var010() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var010")) { 
	      assertCondition(VfyVar("Var110"),errorMessage);
	  }
      }
       }
   }

   public void Var011() {
      errorMessage="";
       if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
	  if (RunVar("Var011")) { 
	      assertCondition(VfyVar("Var111"),errorMessage);
	  }
      }
       }
   }


}
