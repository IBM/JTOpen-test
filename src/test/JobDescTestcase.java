///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JobDescTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:  JobDescTestcase.java
//
// Classes:  JobDescTestcase
//
////////////////////////////////////////////////////////////////////////
package test;


import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.JobDescription;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectList;


/**
  Testcase JobDescTestcase.
**/
public class JobDescTestcase extends Testcase
{

    static final boolean DEBUG = false;

    private static String jobDescLib_   = "JDTSTLIB";
    private static String jobDescName1_ = "JOBDESC1";
    private static String jobQName1_    = "JOBQ1";

    private static CommandCall pwrCmd_;
    // private boolean brief_;

    private static final int VRM520 = AS400.generateVRM(5, 2, 0);
    private static final int VRM530 = AS400.generateVRM(5, 3, 0);
    private static final int VRM540 = AS400.generateVRM(5, 4, 0);
    private int vrm_;  // system version


    // Attribute values.  We declare them static to keep the compiler from optimizing them out.
    static String localName_;
    static String acctCode_;
    static boolean allowMult_;
    static String cymdJobDate_;
    static String devRecAct_;
    static String ddmConv_;
    static int endSev_;
    static boolean hold_;
    static String[] libList_;
    static String inqMsgRep_;
    static String[] groupNames_;
    static String jobDate_;
    static String libName_;
    static String jobLogOutput_;
    static String jobFullMsgQ_;
    static int jobQMaxSize_;
    static String jobQLib_;
    static String jobQName_;
    static int jobQPriority_;
    static byte jobSwitches_;
    static boolean logging_;
    static int msgLogLvl_;
    static int msgLogSev_;
    static String msgLogTxt_;
    static String outQLib_;
    static String outQName_;
    static int outQPriority_;
    static String printDev_;
    static String printTxt_;
    static String routing_;
    static String splfAction_;
    static int syntaxSev_;
    static AS400 system_;
    static String textDesc_;
    static String timeSlice_;
    static String userName_;

    public static void main(String args[]) throws Exception {
      String[] newArgs = new String[args.length+2];
       newArgs[0] = "-tc";
       newArgs[1] = "JobDescTestcase";
       for (int i = 0; i < args.length; i++) {
         newArgs[2+i]=args[i];
       }
       test.JobDescTest.main(newArgs); 
     }

    /**
     Constructor.
     **/
    public JobDescTestcase(AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             AS400    pwrSys,
                             boolean  brief)
    {
        super(systemObject, "JobDescTestcase", namesAndVars, runMode, fileOutputStream);

        if(pwrSys == null || pwrSys.getSystemName().length() == 0 || pwrSys.getUserId().length() == 0)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");

        pwrSys_ = pwrSys;
        // brief_ = brief;
    }

    /**
    Performs setup needed before running variations.

    @exception Exception If an exception occurs.
    **/
    protected void setup ()
    throws Exception
    {
      if (DEBUG) output_.println("Running testcase setup ...");
      try
      {
          jobDescLib_ = super.testLib_; 
        pwrCmd_ = new CommandCall(pwrSys_);
        pwrCmd_.run("QSYS/DLTJOBD JOBD("+jobDescLib_+"/"+jobDescName1_+")");
        deleteLibrary(jobDescLib_);
        createLibrary(jobDescLib_);

        StringBuffer buf = new StringBuffer();
        buf.append("QSYS/CRTJOBD JOBD("+jobDescLib_+"/"+jobDescName1_+") JOBQ("+jobDescLib_+"/"+jobQName1_+") JOBPTY(8) OUTPTY(9) PRTDEV(PRT99) OUTQ(*WRKSTN) TEXT('test job description') USER("+pwrSys_.getUserId()+") PRTTXT('print text') ACGCDE(99999999) RTGDTA(*RQSDTA) RQSDTA('request data') SYNTAX(50) INLLIBL(*NONE) ENDSEV(70) LOG(3 30 *MSG) LOGCLPGM(*YES) INQMSGRPY(*DFT) HOLD(*YES) DATE(*SYSVAL) SWS(01100001) DEVRCYACN(*MSG) TSEPOOL(*BASE) AUT(*CHANGE) JOBMSGQMX(64) JOBMSGQFL(*NOWRAP) ALWMLTTHD(*YES)");
        vrm_ = pwrSys_.getVRM();
        if (vrm_ >= VRM520) {
          // Added in V5R2: spooled file action, and initial ASP group names (not specifiable)
          buf.append(" SPLFACN(*KEEP)");
        }
        if (vrm_ >= VRM530) {
          // Added in V5R3: DDM conversation
          buf.append(" DDMCNV(*DROP)");
        }
        if (vrm_ >= VRM540) {
          // Added in V5R4: job log output
          buf.append(" LOGOUTPUT(*JOBLOGSVR)");
        }
        pwrCmd_.run(buf.toString());
      }
      catch(AS400Exception e) {
        output_.println("Testcase setup failed.");
        e.printStackTrace(output_);
        AS400Message[] msgList = e.getAS400MessageList();
        for (int i = 0; i < msgList.length; ++i) {
          output_.println(msgList[i].getText());
        }
      }
      catch(Exception e) {
        output_.println("Testcase setup failed.");
        e.printStackTrace(output_);
      }
    }

    /**
    Performs cleanup needed after running variations.

    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
      if (DEBUG) output_.println("Running testcase cleanup ...");
      ///if (!DEBUG)
      {
        pwrCmd_.run("QSYS/DLTJOBD JOBD("+jobDescLib_+"/"+jobDescName1_+")");
	deleteLibrary(pwrCmd_,jobDescLib_);
      }
    }

    static void delete(String libName, String fileName)
    {
      try {
        if (!pwrCmd_.run("QSYS/DLTF FILE(" + libName+"/"+fileName + ")")) {
          ///AS400Message[] msgs = pwrCmd_.getAS400MessageList();
        }
      }
      catch (Exception e) { e.printStackTrace(); }
    }

    private static boolean createLibrary(String libName)
    {
      try { if (!pwrCmd_.run("QSYS/CRTLIB LIB("+libName+")")) return false; }
      catch (Exception e) { e.printStackTrace(); return false; }
      return true;
    }


    boolean display(JobDescription jd)
    {
      return getAttributes(jd,true);
    }

    boolean getAttributes(JobDescription jd, boolean display)
    {
      if (jd == null) {
        if (display) println("\n(null)");
        return false;
      }

      boolean ok = true;
      try
      {
        localName_ = jd.getName();
        acctCode_ = jd.getAccountingCode();
        if (acctCode_ == null) ok = false;  // sanity check
        allowMult_ = jd.isAllowMultipleThreads();
        cymdJobDate_ = jd.getCYMDJobDate();
        devRecAct_ = jd.getDeviceRecoveryAction();
        ddmConv_ = jd.getDDMConversation();
        endSev_ = jd.getEndSeverity();
        hold_ = jd.isHoldOnJobQueue();
        libList_ = jd.getInitialLibraryList();
        inqMsgRep_ = jd.getInquiryMessageReply();
        groupNames_ = jd.getInitialASPGroupNames();
        jobDate_ = jd.getJobDateString();
        libName_ = jd.getLibraryName();
        jobLogOutput_ = jd.getJobLogOutput();
        jobFullMsgQ_ = jd.getJobMessageQueueFullAction();
        jobQMaxSize_ = jd.getJobMessageQueueMaximumSize();
        jobQLib_ = jd.getJobQueueLibraryName();
        jobQName_ = jd.getJobQueueName();
        jobQPriority_ = jd.getJobQueuePriority();
        jobSwitches_ = jd.getJobSwitches();
        logging_ = jd.isLoggingOfCLPrograms();
        msgLogLvl_ = jd.getMessageLoggingLevel();
        msgLogSev_ = jd.getMessageLoggingSeverity();
        msgLogTxt_ = jd.getMessageLoggingText();
        outQLib_ = jd.getOutputQueueLibraryName();
        outQName_ = jd.getOutputQueueName();
        outQPriority_ = jd.getOutputQueuePriority();
        printDev_ = jd.getPrinterDeviceName();
        printTxt_ = jd.getPrintText();
        routing_ = jd.getRoutingData();
        splfAction_ = jd.getSpooledFileAction();
        syntaxSev_ = jd.getSyntaxCheckSeverity();
        system_ = jd.getSystem();
        textDesc_ = jd.getTextDescription();
        timeSlice_ = jd.getTimeSliceEndPool();
        userName_ = jd.getUserName();
        if (display)
        {
          println("\nAttributes of job desc named |" + localName_ + "|");
          println("  AccountingCode: " + acctCode_);
          println("  AllowMultipleThreads: " + allowMult_);
          println("  CYMDJobDate: " + cymdJobDate_);
          println("  DeviceRecoveryAction: " + devRecAct_);
          println("  DDMConversation: " + ddmConv_);
          println("  EndSeverity: " + endSev_);
          println("  HoldOnJobQueue: " + hold_);
          print  ("  InitialLibraryList: ");
          if (libList_ == null) println("null");
          else if (libList_.length == 0) println("none");
          else {
            for (int i=0; i<libList_.length; i++) {
              print(" " + libList_[i]);
            }
            println();
          }
          println("  InquiryMessageReply: " + inqMsgRep_);
          print  ("  IASPNames: ");
          if (groupNames_ == null) println("null");
          else if (groupNames_.length == 0) println("none");
          else {
            for (int i=0; i<groupNames_.length; i++) {
              print(" " + groupNames_[i]);
            }
            println();
          }
          println("  JobDate: " + jobDate_);
          println("  LibraryName: " + libName_);
          println("  JobLogOutput: " + jobLogOutput_);
          println("  JobMessageQueueFullAction: " + jobFullMsgQ_);
          println("  JobMessageQueueMaximumSize: " + jobQMaxSize_);
          println("  JobQueueLibraryName: " + jobQLib_);
          println("  JobQueueName: " + jobQName_);
          println("  JobQueuePriority: " + jobQPriority_);
          println("  JobSwitches: " + jobSwitches_);
          println("  LoggingOfCLPrograms: " + logging_);
          println("  MessageLoggingLevel: " + msgLogLvl_);
          println("  MessageLoggingSeverity: " + msgLogSev_);
          println("  MessageLoggingText: " + msgLogTxt_);
          println("  OutputQueueLibraryName: " + outQLib_);
          println("  OutputQueueName: " + outQName_);
          println("  OutputQueuePriority: " + outQPriority_);
          println("  PrinterDeviceName: " + printDev_);
          println("  PrintText: " + printTxt_);
          println("  RoutingData: " + routing_);
          println("  SpooledFileAction: " + splfAction_);
          println("  SyntaxCheckSeverity: " + syntaxSev_);
          println("  System: " + system_.getSystemName());
          println("  TextDescription: " + textDesc_);
          println("  TimeSliceEndPool: " + timeSlice_);
          println("  UserName: " + userName_);
          println();
        }

        return ok;
      }
      catch (Exception e) { e.printStackTrace(); return false; }
    }


    /**
     Construct a JobDescription instance representing an existing jobDescription, and validate its attributes.
     **/
    public void Var001()
    {
      boolean ok = true;
      try
      {
        JobDescription jd = new JobDescription(pwrSys_, jobDescLib_, jobDescName1_);

        if (DEBUG) display(jd);

        String name = jd.getName();
        if (!name.equals(jobDescName1_)) {
          println("Name incorrect.  Expected " + jobDescName1_ + ", got " + name);
          ok = false;
        }
        String actgCode = jd.getAccountingCode();
        if (!actgCode.equals("99999999")) {
          println("actgCode incorrect.  Expected 99999999, got " + actgCode);
          ok = false;
        }
        boolean allowMT = jd.isAllowMultipleThreads();
        if (allowMT != true) {
          println("allowMT incorrect.  Expected true, got " + allowMT);
          ok = false;
        }
        String cymdJobDate = jd.getCYMDJobDate();
        if (!cymdJobDate.equals("*SYSVAL")) {
          println("cymdJobDate incorrect.  Expected *SYSVAL, got " + cymdJobDate);
          ok = false;
        }
        String recAction = jd.getDeviceRecoveryAction();
        if (!recAction.equals("*MSG")) {
          println("recAction incorrect.  Expected *MSG, got " + recAction);
          ok = false;
        }
        String ddmConv = jd.getDDMConversation();
        if (vrm_ >= VRM530) {
          if (!ddmConv.equals("*DROP")) {
            println("ddmConv incorrect.  Expected *DROP, got " + ddmConv);
            ok = false;
          }
        }
        else if (ddmConv != null) {
          println("ddmConv incorrect.  Expected null, got " + ddmConv);
          ok = false;
        }
        int endSev = jd.getEndSeverity();
        if (endSev != 70) {
          println("endSev incorrect.  Expected 70, got " + endSev);
          ok = false;
        }
        boolean holdQ = jd.isHoldOnJobQueue();
        if (holdQ != true) {
          println("holdQ incorrect.  Expected true, got " + holdQ);
          ok = false;
        }
        String[] libList = jd.getInitialLibraryList();
        if (libList.length != 1) {
          println("libList length incorrect.  Expected 1, got " + libList.length);
          ok = false;
          if (libList.length != 0) {
            for (int i=0; i<libList.length; i++) {
              print(" " + libList[i]);
            }
            println();
          }
        }
        else if (!libList[0].equals("*NONE")) {
          println("libList incorrect.  Expected *NONE, got " + libList[0]);
          ok = false;
        }
        String inqMsg = jd.getInquiryMessageReply();
        if (!inqMsg.equals("*DFT")) {
          println("inqMsg incorrect.  Expected *DFT, got " + inqMsg);
          ok = false;
        }
        String[] iaspNames = jd.getInitialASPGroupNames();
        if (vrm_ >= VRM520) {
          if (iaspNames.length != 0) {
            println("iaspNames length incorrect.  Expected 0, got " + iaspNames.length);
            ok = false;
            for (int i=0; i<iaspNames.length; i++) {
              print(" " + iaspNames[i]);
            }
            println();
          }
        }
        else if (iaspNames != null) {
          println("iaspNames incorrect.  Expected null, got a list of length " + iaspNames.length);
          ok = false;
          for (int i=0; i<iaspNames.length; i++) {
            print(" " + iaspNames[i]);
          }
          println();
        }

        String jobDate = jd.getJobDateString();
        if (!jobDate.equals("*SYSVAL")) {
          println("jobDate incorrect.  Expected *SYSVAL, got " + jobDate);
          ok = false;
        }
        String lib = jd.getLibraryName();
        if (!lib.equals(jobDescLib_)) {
          println("lib incorrect.  Expected "+jobDescLib_+", got " + lib);
          ok = false;
        }
        String logOutput = jd.getJobLogOutput();
        if (vrm_ >= VRM540) {
          if (!logOutput.equals("*JOBLOGSVR")) {
            println("logOutput incorrect.  Expected *JOBLOGSVR, got " + logOutput);
            ok = false;
          }
        }
        else if (logOutput != null) {
          println("logOutput incorrect.  Expected null, got " + logOutput);
          ok = false;
        }

        String msgqFull = jd.getJobMessageQueueFullAction();
        if (!msgqFull.equals("*NOWRAP")) {
          println("msgqFull incorrect.  Expected *NOWRAP, got " + msgqFull);
          ok = false;
        }
        int msgQMaxSize = jd.getJobMessageQueueMaximumSize();
        if (msgQMaxSize != 64) {
          println("msgQMaxSize incorrect.  Expected 64, got " + msgQMaxSize);
          ok = false;
        }
        String jobQLib = jd.getJobQueueLibraryName();
        if (!jobQLib.equals(jobDescLib_)) {
          println("xxx incorrect.  Expected "+jobDescLib_+", got " + jobQLib);
          ok = false;
        }
        String jobQName = jd.getJobQueueName();
        if (!jobQName.equals(jobQName1_)) {
          println("jobQName incorrect.  Expected "+jobQName1_+", got " + jobQName);
          ok = false;
        }
        int jobQPriority = jd.getJobQueuePriority();
        if (jobQPriority != 8) {
          println("jobQPriority incorrect.  Expected 8, got " + jobQPriority);
          ok = false;
        }
        byte switches = jd.getJobSwitches();
        if (switches != (byte)97) {
          println("switches incorrect.  Expected 97, got " + switches);
          ok = false;
        }
        boolean isLogging = jd.isLoggingOfCLPrograms();
        if (isLogging != true) {
          println("isLogging incorrect.  Expected true, got " + isLogging);
          ok = false;
        }
        int logLvl = jd.getMessageLoggingLevel();
        if (logLvl != 3) {
          println("logLvl incorrect.  Expected 3, got " + logLvl);
          ok = false;
        }
        int logSev = jd.getMessageLoggingSeverity();
        if (logSev != 30) {
          println("logSev incorrect.  Expected 30, got " + logSev);
          ok = false;
        }
        String logTxt = jd.getMessageLoggingText();
        if (!logTxt.equals("*MSG")) {
          println("logTxt incorrect.  Expected *MSG, got " + logTxt);
          ok = false;
        }
        String outqLib = jd.getOutputQueueLibraryName();
        if (!outqLib.equals("")) {
          println("outqLib incorrect.  Expected '', got " + outqLib);
          ok = false;
        }
        String outqName = jd.getOutputQueueName();
        if (!outqName.equals("*WRKSTN")) {
          println("outqName incorrect.  Expected *WRKSTN, got " + outqName);
          ok = false;
        }
        int outQPriority = jd.getOutputQueuePriority();
        if (outQPriority != 9) {
          println("outQPriority incorrect.  Expected 9, got " + outQPriority);
          ok = false;
        }
        String printer = jd.getPrinterDeviceName();
        if (!printer.equals("PRT99")) {
          println("printer incorrect.  Expected PRT99, got " + printer);
          ok = false;
        }
        String printTxt = jd.getPrintText();
        if (!printTxt.equals("print text")) {
          println("printTxt incorrect.  Expected 'print text', got " + printTxt);
          ok = false;
        }
        String routing = jd.getRoutingData();
        if (!routing.equals("*RQSDTA")) {
          println("routing incorrect.  Expected *RQSDTA, got " + routing);
          ok = false;
        }
        String splfAction = jd.getSpooledFileAction();
        if (vrm_ >= VRM520) {
          if (!splfAction.equals("*KEEP")) {
            println("splfAction incorrect.  Expected *KEEP, got " + splfAction);
            ok = false;
          }
        }
        else if (splfAction != null) {
          println("splfAction incorrect.  Expected null, got " + splfAction);
          ok = false;
        }
        int syntaxSev = jd.getSyntaxCheckSeverity();
        if (syntaxSev != 50) {
          println("syntaxSev incorrect.  Expected 50, got " + syntaxSev);
          ok = false;
        }
        String sysName = jd.getSystem().getSystemName();
        String expected = pwrSys_.getSystemName();
        if (!sysName.equalsIgnoreCase(expected)) {
          println("sysName incorrect.  Expected "+expected+", got " + sysName);
          ok = false;
        }
        String desc = jd.getTextDescription();
        if (!desc.equals("test job description")) {
          println("desc incorrect.  Expected 'test job description', got " + desc);
          ok = false;
        }
        String timeSlice = jd.getTimeSliceEndPool();
        if (!timeSlice.equals("*BASE")) {
          println("timeSlice incorrect.  Expected *BASE, got " + timeSlice);
          ok = false;
        }
        String user = jd.getUserName();
        expected = pwrSys_.getUserId();
        if (!user.equals(expected)) {
          println("user incorrect.  Expected "+expected+", got " + user);
          ok = false;
        }

        if (ok) succeeded();
        else failed();
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

    /**
     Pass null for first parm.
     A NullPointerException should be thrown.
     **/
    public void Var002()
    {
      try
      {
        JobDescription jbd = new JobDescription(null, jobDescLib_, jobDescName1_);
        failed("No exception"+jbd);
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "system");
      }
    }

    /**
     Pass null for second parm.
     A NullPointerException should be thrown.
     **/
    public void Var003()
    {
      try
      {
        JobDescription jbd = new JobDescription(systemObject_, null, jobDescName1_);
        failed("No exception"+jbd);
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "library");
      }
    }

    /**
     Pass null for third parm.
     A NullPointerException should be thrown.
     **/
    public void Var004()
    {
      try
      {
        JobDescription jbd = new JobDescription(systemObject_, jobDescLib_, null);
        failed("No exception"+jbd);
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "name");
      }
    }

    /**
     Get attributes of the first 30 job descriptions on the library list.
     **/
    public void Var005()
    {
      boolean ok = true;
      try
      {
        ObjectList olist = new ObjectList(pwrSys_, ObjectList.LIBRARY_LIST, ObjectList.ALL, "*JOBD");
        olist.load();
        int numObjs = olist.getLength();
        if (numObjs == 0) {
          failed("No JOBDs returned by ObjectList.");
          olist.close(); 
          return;
        }
        if (DEBUG) println("Number of JOBD's: " + numObjs);
        int numToInspect = Math.min(30, numObjs);
        ObjectDescription[] descList = olist.getObjects(0, numToInspect);
        for (int i=0; i<numToInspect; i++) {
          ObjectDescription objDesc = descList[i];
          if (DEBUG) println("DEBUG ObjDesc name/type: " + objDesc.getName() + ", " + objDesc.getType());
          if (!getAttributes(new JobDescription(pwrSys_, objDesc.getLibrary(), objDesc.getName()), DEBUG)) {  // some job descriptions have restricted access
            ok = false;
          }
        }
        olist.close(); 
        if (ok) succeeded();
        else failed();
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

}
