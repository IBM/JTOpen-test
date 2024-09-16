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
////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAStdCrash2.java
//
// Description:  Same as JTACrash2.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdCrash2
//
////////////////////////////////////////////////////////////////////////
package test.JTA;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

public class JTAStdCrash2 extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAStdCrash2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }
   private static int WAIT_FOR_CLIENT_MILLIS  = 60000;

   private String basTbl = JTATest.COLLECTION + ".CRASH2";
   private Connection conn_;
   // private Connection gc;
   String crashMsg = "Ready to Crash"; // this should be the same as in JTAStdCrash2Vars.java
   
   ServerSocket serverSocket1 = null;
   int          serverPort1   = 0;
   Socket clientSocket1 = null;
   PrintWriter outWriter1 = null;
   BufferedReader inReader1 = null;
   String         readerLabel1 = ""; 
   
   ServerSocket serverSocket2 = null;
   int          serverPort2   = 0;
   Socket clientSocket2 = null;
   PrintWriter outWriter2 = null;
   BufferedReader inReader2 = null;
   String         readerLabel2 = ""; 
   
   
   String xidStr;
   String errorMessage=""; 

   // This file and JTAStdCrash2Vars.java are both needed to run the JTAStdCrash2 testcase
   // JTAStdCrash2Vars.java contains the variations

/**
Constructor.
**/
   public JTAStdCrash2 (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password) {
      super (systemObject, "JTAStdCrash2",
             namesAndVars, runMode, fileOutputStream,
             password);
   }

   public JTAStdCrash2 (AS400 systemObject,
			String testname, 
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password) {
      super (systemObject, testname, 
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
      basTbl = JTATest.COLLECTION + ".CRASH2";
      if (isJdbc20StdExt()) {
         conn_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
         JTATest.verboseOut(baseURL_+" at " + basTbl+" "+conn_);
	 isIasp = JDTestUtilities.isIasp(conn_); 
      }
   }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      if (isJdbc20StdExt()) {
      }
      unlockSystem(); 
       if (conn_ != null) try { conn_.close(); } catch (Throwable t) {}
       
   }

   /**
    * nested Thread class to listen for the socket
    */ 
   class SocketAcceptThread1 extends Thread {
       
       public SocketAcceptThread1() {
           // Clear the client socket before we start running
	   clientSocket1 = null; 
       } 
       public void run() {
	   try {
	       // System.out.println("SocketAcceptThread running"); 
	       clientSocket1 = serverSocket1.accept();
	   } catch (Exception e) {
	       e.printStackTrace();
	   } 
       } 
   }

   class SocketAcceptThread2 extends Thread {
     
     public SocketAcceptThread2() {
         // Clear the client socket before we start running
   clientSocket2 = null; 
     } 
     public void run() {
   try {
       // System.out.println("SocketAcceptThread running"); 
       clientSocket2 = serverSocket2.accept();
   } catch (Exception e) {
       e.printStackTrace();
   } 
     } 
 }



   void showProcessOutput(Process p) {
    try {
      System.out.println("\nOutput of process : " + p);
      System.out.println("---------------------------------");
      InputStream iStream = p.getInputStream();
      int readByte;
      try {
        readByte = iStream.read();
        while (readByte != -1) {
          System.out.write((char) readByte);
          readByte = iStream.read();
        }
      } catch (java.io.IOException ioex) {
      }
      System.out.println("\n---------------------------------");
      System.out.println("ErrorOutput of process : " + p);
      System.out.println("---------------------------------");
      try {

        iStream = p.getErrorStream();
        readByte = iStream.read();
        while (readByte != -1) {
          System.out.write((char) readByte);
          readByte = iStream.read();
        }
      } catch (java.io.IOException ioex) {
      }

      System.out.println("\n---------------------------------");

    } catch (Exception e) {
      e.printStackTrace();
    }
  } 

   protected Process setupSocket1(String label) throws Exception {
      JTATest.verboseOut("create the server socket 1");
      try {
         serverSocket1 = new ServerSocket(0);
         serverPort1 = serverSocket1.getLocalPort();
      }
      catch (IOException e) {
         System.err.println("Could not create and listen on server socket.");
         throw e;
      }

      // start the variation
      JTATest.verboseOut("Start the client program in a new process");
      // Propagate the following JAVA properties to the sub process if present
      //   jta.secondary.system
      //   jta.verbose
      String propSecSys = "";
      if (System.getProperty("jta.secondary.system") != null) {
         propSecSys = " -Djta.secondary.system=" +
                      System.getProperty("jta.secondary.system") + " ";
      }
      String propVerbose = "-Djta.verbose.not.set ";
      if (System.getProperty("jta.verbose") != null) {
         propVerbose = " -Djta.verbose=" +
                       System.getProperty("jta.verbose") + " ";
      }
      String propTraceCategory = "-Dtrace.category.not.set ";
      String propTraceFile     = "-Dtrace.file.not.set "; 
      String traceCategory = System.getProperty("com.ibm.as400.access.Trace.category");  
      if (traceCategory != null) { 
        propTraceCategory= " -Dcom.ibm.as400.access.Trace.category="+traceCategory+" ";
        
        String traceFile = System.getProperty("com.ibm.as400.access.Trace.file");
        if (traceFile != null) { 
          propTraceFile = " -Dcom.ibm.as400.access.Trace.file="+traceFile+"."+label+" ";
        }
        
        
      }
      String propSocket = " -Djta.server.socket=" + serverPort1 + " ";

      //
      // 
      String propClasspath = " -classpath \"" +
                             System.getProperty("java.class.path") +
	                     System.getProperty("path.separator") +
	                     System.getProperty("user.dir")+ "\" ";
      

      String command;
      if (useUDBDataSource) { 
        command =
          "java " + propClasspath + propSocket +
          propSecSys + propVerbose + propTraceCategory + propTraceFile +
          " test.JTAUDBCrash2Vars";
        
      } else { 
        command =
            "java " + propClasspath + propSocket +
            propSecSys + propVerbose + propTraceCategory + propTraceFile +
            " test.JTAStdCrash2Vars";
      }

      String cmdA[] = new String[9];
      cmdA[0]="java";
      cmdA[1]=propSocket.trim();
      cmdA[2]=propSecSys.trim();
      cmdA[3]=propVerbose.trim();
      cmdA[4]=propTraceCategory.trim(); 
      cmdA[5]=propTraceFile.trim(); 
      cmdA[6]="-classpath";
      cmdA[7]=System.getProperty("java.class.path") + System.getProperty("path.separator") + System.getProperty("user.dir");
      if (useUDBDataSource) { 
        cmdA[8]="test.JTAUDBCrash2Vars";
      } else { 
         cmdA[8]="test.JTAStdCrash2Vars";
      }

      JTATest.verboseOut("Start subprocess1: " + command);
      Process p = Runtime.getRuntime().exec(cmdA);

      JTATest.verboseOut("wait for the client to connect to the server socket");
      try {
         //
         // Use a separate thread to do the accept.. This will
         // keep this testcase from hanging in the case that the client never
         // connects back
         // 
	 SocketAcceptThread1 socketAcceptThread = new SocketAcceptThread1();
	 socketAcceptThread.start();
	 int waitTime = 0; 
	 while ( clientSocket1 == null) {
	     if (waitTime > WAIT_FOR_CLIENT_MILLIS ) {
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
      outWriter1 = new PrintWriter(clientSocket1.getOutputStream(), true);
      inReader1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
      readerLabel1 = label; 
      return p;
   }

   protected void cleanupSocket1() throws Exception {
      JTATest.verboseOut("cleanupSocket");
      outWriter1.close();
      inReader1.close();
      clientSocket1.close();
      serverSocket1.close();
   }

   
   protected Process setupSocket2(String label) throws Exception {
     JTATest.verboseOut("create the server socket 2");
     try {
        serverSocket2 = new ServerSocket(0);
        serverPort2 = serverSocket2.getLocalPort();
     }
     catch (IOException e) {
        System.err.println("Could not create and listen on server socket.");
        throw e;
     }

     // start the variation
     JTATest.verboseOut("Start the client program2 in a new process, server port is "+serverPort2);
     // Propagate the following JAVA properties to the sub process if present
     //   jta.secondary.system
     //   jta.verbose
     String propSecSys = "";
     if (System.getProperty("jta.secondary.system") != null) {
        propSecSys = " -Djta.secondary.system=" +
                     System.getProperty("jta.secondary.system") + " ";
     }
     String propVerbose = "-Djta.verbose.not.set ";
     if (System.getProperty("jta.verbose") != null) {
        propVerbose = " -Djta.verbose=" +
                      System.getProperty("jta.verbose") + " ";
     }
     String propSocket = " -Djta.server.socket=" + serverPort2 + " ";

     //
     // 
     String propClasspath = " -classpath \"" +
                            System.getProperty("java.class.path") +
                      System.getProperty("path.separator") +
                      System.getProperty("user.dir")+ "\" ";
     

     String command;
     if (useUDBDataSource) { 
       command =
         "java " + propClasspath + propSocket +
         propSecSys + propVerbose +
         " test.JTAUDBCrash2Vars";
       
     } else { 
       command =
           "java " + propClasspath + propSocket +
           propSecSys + propVerbose +
           " test.JTAStdCrash2Vars";
     }

     String cmdA[] = new String[7];
     cmdA[0]="java";
     cmdA[1]=propSocket.trim();
     cmdA[2]=propSecSys.trim();
     cmdA[3]=propVerbose.trim();
     cmdA[4]="-classpath";
     cmdA[5]=System.getProperty("java.class.path") + System.getProperty("path.separator") + System.getProperty("user.dir");
     if (useUDBDataSource) { 
       cmdA[6]="test.JTAUDBCrash2Vars";
     } else { 
         cmdA[6]="test.JTAStdCrash2Vars";
     }

     JTATest.verboseOut("Start subprocess 2 : " + command);
     Process p = Runtime.getRuntime().exec(cmdA);

     JTATest.verboseOut("wait for the client 2 to connect to the server socket");
     try {
        //
        // Use a separate thread to do the accept.. This will
        // keep this testcase from hanging in the case that the client never
        // connects back
        // 
  SocketAcceptThread2 socketAcceptThread = new SocketAcceptThread2();
  socketAcceptThread.start();
  int waitTime = 0; 
  while ( clientSocket2 == null) {
      if (waitTime > WAIT_FOR_CLIENT_MILLIS ) {
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
     outWriter2 = new PrintWriter(clientSocket2.getOutputStream(), true);
     inReader2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
     readerLabel2 = label; 
     return p;
  }

  protected void cleanupSocket2() throws Exception {
     JTATest.verboseOut("cleanupSocket2");
     outWriter2.close();
     inReader2.close();
     clientSocket2.close();
     serverSocket2.close();
  }

   
   
   // TO Test:
   // There are 4 points (*) during a transaction where we try to
   // simulate a crash and see the results.
   // | Start (do work)  *    End  * Prepare * Commit/rollback *  |
   // This testcase is similar to JTAStdCrash1.java
   // In this testcase "Crash" scenario is created by abending the job
   // that is running the variation



   public void RunVar(String variation) throws Exception {
      try {
         
         Process p = setupSocket1("client1."+variation);

         JTATest.verboseOut("ask the client to run the variation");
         errorMessage +=    "ask the client to run the variation\n";
         // Tell the subprocess which DB its dealing with
         outWriter1.println("DBName:" + system_);
         // what collection to use
         JTATest.verboseOut("Collection:" + JTATest.COLLECTION);
	 errorMessage +=    "Collection:" + JTATest.COLLECTION+"\n"; 
         outWriter1.println("Collection:" + JTATest.COLLECTION);
         outWriter1.println("User:" + userId_);
	 outWriter1.println("Password:" + PasswordVault.decryptPasswordLeak(encryptedPassword_,"JTAStdCrash2"));
         if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
            outWriter1.println("Driver:native");
         }
         else {
            outWriter1.println("Driver:toolbox");
         }

         // and always LAST,
         // ask the program to run the variation
         outWriter1.println(variation);

         JTATest.verboseOut("wait till the client is ready to be toasted");
	 errorMessage+=     "wait till the client is ready to be toasted\n";
         // now wait till the var program is ready to be abended
         String fromCrash2Vars;
         while ((fromCrash2Vars = inReader1.readLine()) != null) {
            JTATest.verboseOut("from sub job "+readerLabel1+": " + fromCrash2Vars);
            errorMessage+=     "from sub job "+readerLabel1+": " + fromCrash2Vars+"\n";
            if (fromCrash2Vars.startsWith("TestXid : L"))
               xidStr = fromCrash2Vars;
            if (fromCrash2Vars.equals(crashMsg))
               break;
         }

         JTATest.verboseOut("Abend the process running the Variation");
         errorMessage += "Abend the process running the Variation\n"; 
         // Toast the Variation
         p.destroy();
         cleanupSocket1();
      }
      catch (Exception e) {
         try {
            cleanupSocket1();
         }
         catch (Exception e2) {
         }
         throw e;
      }
   }


   public boolean VfyVar(String variation) {
       try { 
         Process p = setupSocket2("verifyClient");
         boolean success = false;

         // Tell the subprocess which DB its dealing with
         outWriter2.println("DBName:" + system_);
         // what collection to use
         JTATest.verboseOut("Collection:" + JTATest.COLLECTION);
	 errorMessage+="Collection:" + JTATest.COLLECTION+"\n";

         outWriter2.println("Collection:" + JTATest.COLLECTION);
         outWriter2.println("User:" + userId_);
	 outWriter2.println("Password:" + PasswordVault.decryptPasswordLeak(encryptedPassword_,"JTAStdCrash2"));
         if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
            outWriter2.println("Driver:native");
         }
         else {
            outWriter2.println("Driver:toolbox");
         }
         // Tell it which XID its dealing with
         outWriter2.println(xidStr);

         // And always LAST,
         // Tell it which variation to verify.
         outWriter2.println(variation);

         String fromCrash2Vars;
         while ((fromCrash2Vars = inReader2.readLine()) != null) {
            JTATest.verboseOut("from sub job "+readerLabel2+": " + fromCrash2Vars);
	    errorMessage+=     "from sub job "+readerLabel2+": " + fromCrash2Vars+"\n";
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
         cleanupSocket2();
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
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
        try { 
         RunVar("Var001");
         if (VfyVar("Var101")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
        } catch (Exception e) {
          failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
        }
      }
   }

   public void Var002() {
      errorMessage=""; 
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
        try { 
         RunVar("Var002");
         if (VfyVar("Var102")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
        } catch (Exception e) {
          failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
        }
      }
   }

   public void Var003() {
       errorMessage="";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
     if (checkJdbc20StdExt()) {
       try { 
         RunVar("Var003");
         if (VfyVar("Var103")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
       } catch (Exception e) {
         failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
       }
      }
   }

   public void Var004() {
      errorMessage="";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
        try { 
         RunVar("Var004");
         if (VfyVar("Var104")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
        } catch (Exception e) {
          failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
        }
      }
   }

   public void Var005() {
      errorMessage="";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
        try { 
         RunVar("Var005");
         if (VfyVar("Var105")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
        } catch (Exception e) {
          failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
        }
      }
   }

   public void Var006() {
      errorMessage="";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
        try { 
         RunVar("Var006");
         if (VfyVar("Var106")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
        } catch (Exception e) {
          failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
        }
      }
   }

   public void Var007() {
      errorMessage="";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
        try { 
         RunVar("Var007");
         if (VfyVar("Var107")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
        } catch (Exception e) {
          failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
        }
      }
   }

   public void Var008() {
      errorMessage="";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
        try { 
         RunVar("Var008");
         if (VfyVar("Var108")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
        } catch (Exception e) {
          failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
        }
      }
   }

   public void Var009() {
      errorMessage="";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
        try { 
         RunVar("Var009");
         if (VfyVar("Var109")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
        } catch (Exception e) {
          failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
        }
      }
   }

   public void Var010() {
      errorMessage="";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
        try { 
         RunVar("Var010");
         if (VfyVar("Var110")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
        } catch (Exception e) {
          failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
        }
      }
   }

   public void Var011() {
      errorMessage="";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
        try { 
         RunVar("Var011");
         if (VfyVar("Var111")) {
            assertCondition(true);
         }
         else {
            failed("Debug with -Djta.verbose=true "+errorMessage);
         }
        } catch (Exception e) {
          failed(e, "Unexpected Exception - debug with -Djta.verbose=true"); 
        }
      }
   }
}
