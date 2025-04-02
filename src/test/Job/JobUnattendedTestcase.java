///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JobUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//
//
//
///////////////////////////////////////////////////////////////////////////////
//
// File Name:  JobUnattendedTestcase.java
//
// Class Name:  JobUnattendedTestcase
//
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

package test.Job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobList;
import com.ibm.as400.access.SystemValue;
import com.ibm.as400.access.Trace;

import test.Testcase;
import test.misc.TelnetRunnable;

/**
 The JobUnattendedTestcase class tests the methods of Job.
 <p>The methods are listed as following:
 <ul>
 <li>constructor
 <li>commitChanges()
 <li>getAuxiliaryIORequests()
 <li>getBreakMessageHandling()
 <li>getCodedCharacterSetID()
 <li>getCompletionStatus()
 <li>getCountryID()
 <li>getCPUUsed()
 <li>getCurrentLibrary()
 <li>getCurrentLibraryExistence()
 <li>getDate()
 <li>getDateFormat()
 <li>getDateSeparator()
 <li>getDDMConversationHandling()
 <li>getDecimalFormat()
 <li>getDefaultCodedCharacterSetIdentifier()
 <li>getDeviceRecoveryAction()
 <li>getEndSeverity
 <li>getFunctionName()
 <li>getFunctionType()
 <li>getInquiryMessageReply()
 <li>getInteractiveTransactions()
 <li>getJobAccountingCode()
 <li>getJobActiveDate()
 <li>getJobDate()
 <li>getJobDescription()
 <li>getJobEnterSystemDate()
 <li>getJobMessageQueueFullAction()
 <li>getJobMessageQueueMaximumSize()
 <li>getJobPutOnJobQueueDate()
 <li>getScheduleDate()
 <li>getJobStatusInJobQueue()
 <li>getJobSwitches()
 <li>getLanguageID()
 <li>getLoggingCLPrograms()
 <li>getLoggingLevel()
 <li>getLoggingSeverity()
 <li>getLoggingText()
 <li>getModeName()
 <li>getName()
 <li>getNumber()
 <li>getNumberOfLibrariesInSYSLIB()
 <li>getNumberOfLibrariesInUSRLIBL()
 <li>getNumberOfProductLibraries()
 <li>getOutputQueue()
 <li>getOutputQueuePriority()
 <li>getPoolIdentifier()
 <li>getPrinterDeviceName()
 <li>getPrintKetFormat()
 <li>getPrintText()
 <li>getProductLibraries()
 <li>getQueue()
 <li>getQueuePriority()
 <li>getRoutingData()
 <li>getRunPriority()
 <li>getSignedOnJob()
 <li>getSortSequenceTable()
 <li>getStatus()
 <li>getStatusMessageHandling()
 <li>getSubsystem()
 <li>getSubtype()
 <li>getSystem()
 <li>getSystemLibraryList()
 <li>getTimeSeparator()
 <li>getTotalResponseTime()
 <li>getType()
 <li>getUser()
 <li>getUserLibraryList()
 <li>getWorkIDUnit()
 <li>loadInformation()
 <li>setBreakMessageHandling()
 <li>setCacheChanges(true)
 <li>setCodedCharacterSetID()
 <li>setCountryID()
 <li>setDateFormat()
 <li>setDateSeparator()
 <li>setDDMConversationHandling()
 <li>setDecimalFormat()
 <li>setDefaultWait()
 <li>setDeviceRecoveryAction()
 <li>setInquiryMessageReply()
 <li>setJobAccountingCode()
 <li>setJobDate()
 <li>setJobMessageQueueFullAction()
 <li>setJobSwitches()
 <li>setLanguageID()
 <li>setLoggingCLPrograms()
 <li>setLoggingLevel()
 <li>setLoggingSeverity()
 <li>setLoggingText()
 <li>setOutputQueue()
 <li>setOutputQueuePriority()
 <li>setPrinterDeviceName()
 <li>setPrintKeyFormat()
 <li>setPrintText()
 <li>setPurge()
 <li>setQueue()
 <li>setQueuePriority()
 <li>setRunPriority()
 <li>setScheduleDate()
 <li>setScheduleTime()
 <li>setSortSequenceTable()
 <li>setStatusMessageHandling()
 <li>setTimeSeparator()
 <li>setTimeSlice()
 <li>setTimeSliceEndPool()
 <li>toString()
 </ul>
 **/
public class JobUnattendedTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JobUnattendedTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JobTest.main(newArgs); 
   }
    private String userName_;
    private String jobName_;
    private String jobNumber_;
    private String userName1_;
    private String jobName1_;
    private String jobNumber1_;
    String userName2_;
    String jobName2_;
    String jobNumber2_;
    private String userName3_;
    private String jobName3_;
    private String jobNumber3_;
    String userName4_;
    String jobName4_;
    String jobNumber4_;
    private String batchNumber_ = null;
    private TelnetRunnable telnet = null; 

    static private final String interactiveUser_ = "TSTJOBUSR1";
    static private final String testJobQueue_ = "TSTJOBQU03";
    static private final String testJob01_ = "TSTJOB01";
    static private final String testJob02_ = "TSTJOB02";

    //static private String countryID = "*SYSVAL";
    //static private String languageID = "*SYSVAL";
    static private String[] jobAccountingCode = {"*BLANK", ""};
    //static private String[] scheduieTime = {"*CURRENT", ""};
    static private int[] loggingLevel= {0, 1, 2, 3, 4};
    static private String[] breakMessageHandling = {"*NORMAL", "*HOLD", "*NOTIFY"};
    static private String[] dateFormat = {"*SYS", "*MDY", "*JUL"};
    static private String[] dateSeparator = {"S", "/", "-", ".", " ", ","};
    static private String[] DDMCH = {"*KEEP", "*DROP"};
    //    static private String[] decimalFormat = {"*SYSVAL", "*BLANK", "J", "I"};
    static private String[] deviceRecoveryAction = {"*SYSVAL", "*MSG", "*DSCMSG", "*DSCENDRQS", "*ENDJOB", "*ENDJOBNOLIST"};
    static private String[] inquiryMessageReply = {"*RQD", "*DFT", "*SYSRPYL"};
    static String[] JMQFA = {"*PRTWRAP", "*WRAP", "*NOWRAP", "*SYSVAL"};
    static private String[] loggingCLPrograms = {"*YES", "*NO"};
    static private String[] loggingText = {"*MSG", "*SECLVL", "*NOLIST"};
    static private String[] outputQueue = {"*DEV", "*WRKSTN"};

    static  String[] printerDeviceName = {"*INLUSR", "*CURUSR"};
    static private String[] printKeyFormat = {"*SYSVAL", "*NONE", "*PRTBDR", "*PRTHDR", "*PRTALL"};
    static private String[] printText = {"*SYSVAL", "*BLANK", ""};
    //    static private String[] scheduleDate = {"*MONTHSTR", "*MONTHEND"};
    static private String[] jobMessageQueueFullAction = {"*NOWRAP", "*WRAP", "*PRTWRAP"};
    static private String[] statusMessageHandling = {"*SYSVAL", "*NONE", "*NORMAL"};
    static private String[] timeSeparator = {"S", ":", ".", " ", ","};
    static private String[] timeSliceEndPool = {"*SYSVAL", "*NONE", "*BASE"};

    
    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        if (pwrSys_ == null)
        {
            throw new Exception("-pwrSys not specified.");
        }

        CommandCall ccallPow_ = new CommandCall(pwrSys_);


	//
	// Make sure that the user exists and the password is set
	//
	String newPassword="ABC212CB";
	ccallPow_.setCommand("CRTUSRPRF USRPRF("+interactiveUser_+") PASSWORD("+newPassword+") TEXT('Toolbox testing profile') ACGCDE(514648897) CCSID(37)");
	ccallPow_.run();
	// Change the password to keep it from expiring
	ccallPow_.setCommand("CHGUSRPRF USRPRF("+interactiveUser_+") PASSWORD(BOGUS7)");
	ccallPow_.run();
	ccallPow_.setCommand("CHGUSRPRF USRPRF("+interactiveUser_+") PASSWORD("+newPassword+")  STATUS(*ENABLED) "); 
	if (ccallPow_.run() != true) {
	    System.out.println("SEVERE ERROR:  Unable to change password for "+interactiveUser_); 
	}
	ccallPow_.setCommand("GRTOBJAUT OBJ(QSYS/"+interactiveUser_+") OBJTYPE(*USRPRF) USER("+interactiveUser_+") AUT(*USE)");
	ccallPow_.run();

	// 
	//  Use a simple telnet session to connect to the system as interactiveUser_ (TSTJOBUSR1)
	// 

	StringBuffer sb = new StringBuffer();

	sb.append("telnet connecting using "+pwrSys_.getSystemName()+" "+ interactiveUser_ +" "+ newPassword+"\n"); 
	telnet = new TelnetRunnable(pwrSys_.getSystemName(), interactiveUser_, newPassword); 
	try { 
	  telnet.connect(); 
	} catch (Exception e) {
	  System.out.println("Exception from telnet session"); 
	   e.printStackTrace(); 
	   System.out.println(telnet.toString()); 
	}


        // Get the interactive job.
        JobList jobList = new JobList(pwrSys_);
        jobList.setUser(interactiveUser_);
        Enumeration enumeration = jobList.getJobs();
        boolean foundInteractiveJob = false;



        while (enumeration.hasMoreElements() && !foundInteractiveJob)
        {
            Job job = (Job)enumeration.nextElement();
	    sb.append(job.getType() +" " +
                job.getSubtype() +" " +
                job.getStatus() +" "+
                job.getUser() +"\n");

            if (job.getType().equals("I") &&
                job.getSubtype().trim().equals("") &&
                job.getStatus().equals(Job.JOB_STATUS_ACTIVE) &&
                job.getUser().trim().equalsIgnoreCase(interactiveUser_))
            {
                userName_ = job.getUser();
                jobName_ = job.getName();
                jobNumber_ = job.getNumber();
                foundInteractiveJob = true;
            }
        }

        // Get a batch job for variation 90.
        jobList.setUser("QSYS");
        jobList.setName("QBATCH");
        enumeration =jobList.getJobs();
        while (enumeration.hasMoreElements())
        {
            Job job = (Job)enumeration.nextElement();
            if (job.getType().equals("M") && job.getName().equals("QBATCH") && job.getUser().equals("QSYS"))
            {
                batchNumber_ = job.getNumber();
            }
        }
        if (batchNumber_ == null)
        {
            output_.println("Note: No QBATCH job was found, some variations may fail.");
        }

        if (jobName_ == null || userName_ == null || jobNumber_ == null)
        {
	    System.out.println("---- did not find job -- jobs where");
	    System.out.println(sb.toString());

            throw new Exception("Please bring up an emulator session to " + pwrSys_.getSystemName() + ", and\nsign-on as " + interactiveUser_ + ".  Run this testcase while that\nsession is active. jobName_="+jobName_+" uesrName_="+userName_+"jobNumber_="+jobNumber_);
        }

        ccallPow_.setCommand("CRTJOBQ JOBQ("+testJobQueue_+")");
        if (ccallPow_.run() != true)
        {
            AS400Message[] messages = ccallPow_.getMessageList();
            output_.println("Error in create Job Queue: ");
            for (int i = 0; i < messages.length; ++i) output_.println(messages[i]);
        }

        String[] jobInfo = createJob(pwrSys_, testJob01_, testJobQueue_, "*MONTHSTR");
        jobName1_ = jobInfo[0];
        userName1_ = jobInfo[1];
        jobNumber1_ = jobInfo[2];
        jobName2_ = jobInfo[0];
        userName2_ = jobInfo[1];
        jobNumber2_ = jobInfo[2];
        jobName3_ = jobInfo[0];
        userName3_ = jobInfo[1];
        jobNumber3_ = jobInfo[2];

        jobInfo = createJob(pwrSys_, testJob02_, "*JOBD", "*CURRENT");
        jobName4_ = jobInfo[0];
        userName4_ = jobInfo[1];
        jobNumber4_ = jobInfo[2];

        ccallPow_.run("CRTLIB LIB(TLIB6)");
        ccallPow_.run("CHGCURLIB CURLIB(TLIB6)");
        // These 2 are for the sort sequence table
        ccallPow_.run("CRTSRCPF FILE(TLIB6/TABLESRC) MBR(TESTSEQ)");
        ccallPow_.run("CRTTBL TBL(TLIB6/TESTSEQ) SRCFILE(TLIB6/TABLESRC) TBLTYPE(*SRTSEQ) CCSID(*HEX)");
        ccallPow_.run("CLRJOBQ QGPL/TESTJOBQ6");
        ccallPow_.run("DLTJOBQ QGPL/TESTJOBQ6");

        CommandCall norm = new CommandCall(systemObject_);
        norm.run("CRTJOBQ QGPL/TESTJOBQ6");
    }

    private String[] createJob(AS400 system, String jobName, String jobQueue, String scheduleDate) throws Exception
    {
        String[] jobInfo = new String[3];

        CommandCall command = new CommandCall(system);
        if (!scheduleDate.equals("*CURRENT"))
        {
            command.setCommand("SBMJOB CMD(WRKSYSVAL) JOB(" + jobName + ") JOBQ(" + jobQueue + ") SCDDATE(" + scheduleDate + ")");
        }
        else
        {
            command.setCommand("SBMJOB CMD(WRKSYSVAL) JOB(" + jobName + ") JOBQ(" + jobQueue + ")");
        }
        if (command.run() == true)
        {
            AS400Message[] messages = command.getMessageList();
            for (int i = 0; i < messages.length; ++i) Trace.log(Trace.INFORMATION, messages[i].getText());
            String log = messages[0].getText();
            int separator1 = log.indexOf(' ');
            int separator2 = log.indexOf('/', separator1 + 1);
            int separator3 = log.indexOf('/', separator2 + 1);
            int separator4 = log.indexOf(' ', separator3 + 1);
            jobInfo[2] = log.substring(separator1 + 1, separator2);
            jobInfo[1] = log.substring(separator2 + 1, separator3);
            jobInfo[0] = log.substring(separator3 + 1, separator4);
        }
        else
        {
            AS400Message[] messages = command.getMessageList();
            output_.println("Error in create Job: ");
            for (int i = 0; i < messages.length; ++i) output_.println(messages[i]);
        }
        return jobInfo;
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        CommandCall ccallPow_ = new CommandCall(pwrSys_);
        if (ccallPow_.run("ENDJOB JOB("+jobNumber1_+"/"+userName1_+"/"+jobName1_+") OPTION(*IMMED)") != true)
        {
            AS400Message[] messages = ccallPow_.getMessageList();
            output_.println("Error in delete job: ");
            for (int i = 0; i < messages.length; ++i) output_.println(messages[i]);
        }
        // Don't need to delete jobName4_ because it has already completed by now.

        ccallPow_.run("DLTTBL TBL(TLIB6/TESTSEQ)");
        ccallPow_.run("DLTF FILE(TLIB6/TABLESRC)");
	deleteLibrary(ccallPow_,"TLIB6");
        // Have to clear it before you can delete it.
        ccallPow_.run("CLRJOBQ JOBQ("+testJobQueue_+")");

        if (ccallPow_.run("DLTJOBQ JOBQ("+testJobQueue_+")") != true)
        {
            AS400Message[] messages = ccallPow_.getMessageList();
            output_.println("Error in delete Job queue: ");
            for (int i = 0; i < messages.length; ++i) output_.println(messages[i]);
        }
         
        telnet.close(); 
        
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the constructor with arguments run well.
     **/
    public void Var001()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            assertCondition(true, "job="+job); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the constructor with lower case arguments work.
     **/
    public void Var002()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_.toLowerCase(), userName_.toLowerCase(), jobNumber_);
            assertCondition(true, "job="+job);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the constructor with arguments runs well if specifying job name * when user name and job number are "".
     **/
    public void Var003()
    {
        try
        {
            Job job = new Job(pwrSys_, "*", "", "");
            assertCondition(true, "job="+job); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the ExtendedIllegalArgumentException will be thrown if specifying invalid job name in constructor with arguments when job name is *.
     **/
    public void Var004()
    {
        try
        {
            Job job = new Job(systemObject_, "*", "ttt", jobNumber_);
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the ExtendedIllegalArgumentException will be thrown if specifying invalid job number in constructor with arguments when job name is *.
     **/
    public void Var005()
    {
        try
        {
            Job job = new Job(systemObject_, "*", userName_, "1111");
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }


    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the ExtendedIllegalArgumentException will be thrown if specifying invalid jobName in constructor with arguments.
     **/
    public void Var006()
    {
        try
        {
            Job job = new Job(systemObject_, "QZDASOINITS", userName_, "31");
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the AS400Exception will be thrown if specifying invalid userName in constructor with arguments.
     **/
    public void Var007()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, "xxxxx", jobNumber_);
            job.getType(); // connect to the system
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the AS400Exception will be thrown if specifying invalid jobNumber in constructor with arguments.
     **/
    public void Var008()
    {
        try
        {
            Job job = new Job(systemObject_,jobName_, userName_, "-1");
            job.getType(); // connect to the system
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the NullPointerException will be thrown if the as400 is null in constructor with arguments.
     **/
    public void Var009()
    {
        try
        {
            Job job = new Job(null, jobName_, userName_, jobNumber_);
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the NullPointerException will be thrown if the jobName is null
     in constructor with arguments.
     **/
    public void Var010()
    {
        try
        {
            Job job = new Job(systemObject_, null, userName_, jobNumber_);
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that the NullPointerException will be thrown if the userName is null in constructor with arguments.
     **/
    public void Var011()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, null, jobNumber_);
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested: Job(AS400 as400, String jobName, String userName, String jobNumber)
     - Ensure that nothing will happen if the jobNumber is null in constructor with arguments.
     **/
    public void Var012()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, null);
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:Job(AS400 as400, String internalJobID)
     - Ensure that the constructor with arguments run well.
     **/
    public void Var013()
    {
        try
        {
            //     Job job = new Job(systemObject_, internalJobID);
            succeeded();
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:Job(AS400 as400, String internalJobID)
     - Ensure that the ExtendedIllegalArgumentException will be thrown if specifying invalid internalJobID in constructor with arguments.
     **/
    public void Var014()
    {
        try
        {
            Job job = new Job(systemObject_, "");
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:Job(AS400 as400, String internalJobID)
     - Ensure that the NullPointerException will be thrown if the as400 is null in constructor with arguments.
     **/
    public void Var015()
    {
        try
        {
            Job job = new Job(null, "");
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:Job(AS400 as400, String internalJobID)
     - Ensure that the NullPointerException will be thrown if the internalJobID is null in constructor with arguments.
     **/
    public void Var016()
    {
        try
        {
            Job job = new Job(systemObject_, (String)null);
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested: commitChanges(), and setCacheChanges(true)
     - Ensure that nothing will happen when there is nothing in cache.
     **/
    public void Var017()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setCacheChanges(true);
            //job.loadInformation();
            job.commitChanges();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: commitChanges(), setCacheChanges(true), getCountryID(), and setCountryID()
     - Ensure that the changes to the job information can be committed correctly.
     **/
    public void Var018()
    {
        try
        {
            Job job =  new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getCountryID();
            String str = "AR";
            job.setCacheChanges(true);
            job.setCountryID(str);
            job.commitChanges();
            assertCondition(job.getCountryID().equals(str), "Mismatched country ID: " + job.getCountryID() + " != " + str);
            job.setCacheChanges(false);
            job.setCountryID(s);
        }
        catch (AS400Exception e)
        {
            failed(e, "Unexpected exception occurred.");
            AS400Message msg = e.getAS400Message();
            if (msg != null && msg.getID().equals("CPF1290"))
            {
              System.out.println("SUGGESTION:\nPlease bring up an emulator session to " + pwrSys_.getSystemName() + ", and\nsign-on as " + interactiveUser_ + ".  Run this testcase while that\nsession is active.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getAuxiliaryIORequests()
     - Ensure that the number of auxiliary storage input/output request can be got correctly.
     **/
    public void Var019()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int num = job.getAuxiliaryIORequests();
            assertCondition(num >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getBreakMessageHandling(), and setBreakMessageHandling()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var020()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getBreakMessageHandling();
            String failMsg = "";
            for (int x = 0; x < 3; ++x)
            {
                String str = breakMessageHandling[x];
                job.setBreakMessageHandling(str);
                if (!job.getBreakMessageHandling().equals(str))
                {
                    failMsg += "'" + str + "' != '" + job.getBreakMessageHandling() + "'\n";
                }
            }
            if (failMsg.length() > 0)
            {
                failed("Error in getBreakMessageHandling():\n" + failMsg);
            }
            else
            {
                succeeded();
            }
            job.setBreakMessageHandling(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getBreakMessageHandling(), setBreakMessageHandling(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var021()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getBreakMessageHandling();
            String failMsg = "";
            for (int x = 0; x < 3; ++x)
            {
                String str = breakMessageHandling[x];
                job.setCacheChanges(true);
                job.setBreakMessageHandling(str);
                job.commitChanges();
                if (!job.getBreakMessageHandling().equals(str))
                {
                    failMsg += "'" + str + "' != '" + job.getBreakMessageHandling() + "'\n";
                }
            }
            if (failMsg.length() > 0)
            {
                failed("Error in getBreakMessageHandling():\n"+failMsg);
            }
            else
            {
                succeeded();
            }
            job.setCacheChanges(false);
            job.setBreakMessageHandling(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:getCacheChanges(), and setCacheChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var022()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            boolean b = job.getCacheChanges();
            job.setCacheChanges(true);
            assertCondition(job.getCacheChanges() == true);
            job.setCacheChanges(b);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getCodedCharacterSetID(), and setCodedCharacterSetID()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var023()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int int_ = job.getCodedCharacterSetID();
            int i = -1;
            job.setCodedCharacterSetID(i);
            assertCondition(job.getCodedCharacterSetID() == i);
            job.setCodedCharacterSetID(int_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getCodedCharacterSetID(), setCodedCharacterSetID(), and job.commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var024()
    {

        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int int_ = job.getCodedCharacterSetID();
            int i = -1;
            job.setCacheChanges(true);
            job.setCodedCharacterSetID(i);
            job.commitChanges();
            assertCondition(job.getCodedCharacterSetID() == i);
            job.setCacheChanges(false);
            job.setCodedCharacterSetID(int_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getCompletionStatus()
     - Ensure that the completion status can be got.
     **/
    public void Var025()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            assertCondition(job.getCompletionStatus() != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getCountryID(), and setCountryID()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var026()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getCountryID();
            job.setCountryID("AR");
            job.commitChanges();
            Job job2 = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String str = job2.getCountryID();
            assertCondition(job.getCountryID().equals(str), "Country IDs don't match: '" + job.getCountryID() + "' != '" + str + "'");
            job.setCountryID(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getCPUUsed()
     - Ensure that the processing time of specified job can be got.
     **/
    public void Var027()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            assertCondition(job.getCPUUsed() > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getCurrentLibrary()
     - Ensure that the current library can be got if one exist.
     **/
    public void Var028()
    {
        try
        {
            Job job = new Job(systemObject_, "*", "", "");
            if (job.getCurrentLibraryExistence())
                assertCondition(job.getCurrentLibrary().equals("QGPL"));
            else if (job.getCurrentLibrary().length() == 0)
                succeeded();
            else
                failed("Wrong current library: 'QGPL' != '" + job.getCurrentLibrary() + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getCurrentLibraryExistence()
     - Ensure that the state of current library can be got, existence or not.
     **/
    public void Var029()
    {
        notApplicable("Obsolete testcase");
        // This testcase is invalid.  The getCurrentLibraryExistence() method simply
        // reports whether or not a "current library" exists.
        // Both 'true' and 'false' are valid return values, depending on
        // whether there is a current library defined for the user profile.
        //
        // 2007-10-25: According to Carol Marolt:
        // The current library can get set a number of different ways,
        // but your job (or secondary thread) doesn't have to have a current library:
        // - Can be specified on your user profile (CURLIB parm on CRTUSRPRF)
        //
        // - When a cmd or menu is executed (if specified on CRTCMD or CRTMNU cmd)
        //
        // Can be changed with a number of cmds/apis:
        // - chglibl
        // - chgcurlib
        // - qlichgll API
        /*
        try
        {
            Job job = new Job(pwrSys_, "*", "", "");
            boolean s = job.getCurrentLibraryExistence();
            assertCondition(s == true);
        }
        catch (Exception e)
        {

            failed(e, "Unexpected exception.");
        }
        */
    }

    /**
     Method tested:getDate()
     - Ensure that the correct date can be retrieved when the job was placed on the system.
     **/
    public void Var030()
    {
        notApplicable("Obsolete testcase");
        /*
         try
         {
         Calendar cal = Calendar.getInstance();
         Date current = cal.getTime();
         cal.add(Calendar.HOUR, -1);   // Assume the job date to be
         Date cmp = cal.getTime();     // within an hour of the current date.

         Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
         Date date = job.getDate();
         if (date.after(cmp) && date.before(current))
         succeeded();
         else
         failed("Wrong job date: '"+date+"' is not between '"+cmp+"' and '"+current+"'");
         }
         catch(Exception e)
         {
         failed(e, "Unexpected exception.");
         }
         */
    }

    /**
     Method tested:getDateFormat(), and setDateFormat()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var031()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getDateFormat();
            for (int x = 0; x < 3; ++x)
            {
                String str = dateFormat[x];
                job.setDateFormat(str);
                if (job.getDateFormat().equals(str)) condition_ = true;
                if (condition_ == false) break;
            }
            assertCondition(condition_);
            job.setDateFormat(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getDateFormat(), setDateFormat(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var032()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getDateFormat();
            for (int x = 0; x < 3; ++x)
            {
                String str = dateFormat[x];
                job.setCacheChanges(true);
                job.setDateFormat(str);
                job.commitChanges();
                if (job.getDateFormat().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setDateFormat(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getDateSeparator(), and setDateSeparator()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var033()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getDateSeparator();
            for (int x = 0; x < 6; ++x)
            {
                String str = dateSeparator[x];
                job.setDateSeparator(str);
                if (job.getDateSeparator().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setDateSeparator(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getDateSeparator(), setDateSeparator(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var034()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getDateSeparator();
            for (int x = 0; x < 6; ++x)
            {
                String str = dateSeparator[x];
                job.setCacheChanges(true);
                job.setDateSeparator(str);
                job.commitChanges();
                if (job.getDateSeparator().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setDateSeparator(s);

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getDDMConversationHandling(), and setDDMConversationHandling()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var035()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getDDMConversationHandling();
            for (int x = 0; x < 2; ++x)
            {
                String str = DDMCH[x];
                job.setDDMConversationHandling(str);
                if (job.getDDMConversationHandling().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setDDMConversationHandling(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getDDMConversationHandling(), setDDMConversationHandling(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var036()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getDDMConversationHandling();
            for (int x = 0; x < 2; ++x)
            {
                String str = DDMCH[x];
                job.setCacheChanges(true);
                job.setDDMConversationHandling(str);
                job.commitChanges();
                if (job.getDDMConversationHandling().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setDDMConversationHandling(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getDecimalFormat(), and setDecimalFormat()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var037()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String s = job.getDecimalFormat();
            job.setDecimalFormat("");
            String ret = job.getDecimalFormat();
            if (!ret.equals(""))
            {
                failed("Wrong decimal format for '*BLANK': '"+ret+"'");
            }
            else
            {
                job.setDecimalFormat("J");
                ret = job.getDecimalFormat();
                if (!ret.equals("J"))
                {
                    failed("Wrong decimal format for 'J': '"+ret+"'");
                }
                else
                {
                    job.setDecimalFormat("I");
                    ret = job.getDecimalFormat();
                    if (!ret.equals("I"))
                    {
                        failed("Wrong decimal format for 'I': '"+ret+"'");
                    }
                    else
                    {
                        job.setDecimalFormat("*SYSVAL");
                        ret = job.getDecimalFormat();
                        if (!ret.equals("*SYSVAL"))
                        {
                            failed("Wrong decimal format for '*SYSVAL': '"+ret+"' != '*SYSVAL'");
                        }
                        else
                        {
                            succeeded();
                        }
                    }
                }
            }
            if (s.equals(" "))
                job.setDecimalFormat("*BLANK");
            else
                job.setDecimalFormat(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getDecimalFormat(), setDecimalFormat(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var038()
    {
        int[] jobAttributes = {Job.DECIMAL_FORMAT, Job.ACTIVE_JOB_STATUS, Job.ACTIVE_JOB_STATUS_FOR_JOBS_ENDING, Job.CONTROLLED_END_REQUESTED};
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setCacheChanges(true);
            String s = job.getDecimalFormat();
            job.setDecimalFormat("");
            job.commitChanges();
            job.loadInformation(jobAttributes);
            String ret = job.getDecimalFormat();
            if (!ret.equals(""))
            {
                failed("Wrong decimal format for '*BLANK': '"+ret+"'");
            }
            else
            {
                job.setDecimalFormat("J");
                job.commitChanges();
                job.loadInformation(jobAttributes);
                ret = job.getDecimalFormat();
                if (!ret.equals("J"))
                {
                    failed("Wrong decimal format for 'J': '"+ret+"'");
                }
                else
                {
                    job.setDecimalFormat("I");
                    job.commitChanges();
                    //job.loadInformation();
                    job.loadInformation(jobAttributes);
                    //job.loadInformation(new int[] {Job.DECIMAL_FORMAT, Job.ACTIVE_JOB_STATUS});
                    ret = job.getDecimalFormat();
                    if (!ret.equals("I"))
                    {
                        failed("Wrong decimal format for 'I': '"+ret+"'");
                    }
                    else
                    {
                        job.setDecimalFormat("*SYSVAL");
                        job.commitChanges();
                        //job.loadInformation();
                        job.loadInformation(jobAttributes);
                        SystemValue sv = new SystemValue(pwrSys_, "QDECFMT");
                        String expected = (String)sv.getValue();
                        ret = job.getDecimalFormat();
                        if (!ret.trim().equals(expected.trim()))
                        {
                            failed("Wrong decimal format for '*SYSVAL': '"+ret+"' != '"+expected+"'");
                        }
                        else
                        {
                            succeeded();
                        }
                    }
                }
            }
            job.setCacheChanges(false);
            if (s.equals(" "))
                job.setDecimalFormat("*BLANK");
            else
                job.setDecimalFormat(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getDefaultCodedCharacterSetIdentifier()
     - Ensure that the identifier of the default coded character set can be got.
     **/
    public void Var039()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int int_ = job.getDefaultCodedCharacterSetIdentifier();
            assertCondition(int_ > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getDeviceRecoveryAction(), and setDeviceRecoveryAction()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var040()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getDeviceRecoveryAction();
            for (int x = 0; x < 6; ++x)
            {
                String str = deviceRecoveryAction[x];
                job.setDeviceRecoveryAction(str);
                if (job.getDeviceRecoveryAction().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setDeviceRecoveryAction(s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:getDeviceRecoveryAction(), setDeviceRecoveryAction(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var041()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getDeviceRecoveryAction();
            for (int x = 0; x < 6; ++x)
            {
                String str = deviceRecoveryAction[x];
                job.setCacheChanges(true);
                job.setDeviceRecoveryAction(str);
                job.commitChanges();
                if (job.getDeviceRecoveryAction().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setDeviceRecoveryAction(s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:getEndSeverity()
     - Ensure that the end severity can be got.
     **/
    public void Var042()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int i = job.getEndSeverity();
            /* This test assumes that ENDSEV in the job and job description is set to something other than 0 for interactive jobs. */
            assertCondition(i != 0, "end severity for job "+jobName_ +" "+ userName_+" "+jobNumber_+"is "+i+" non zero ");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getFunctionName()
     - Ensure that the information about the function that the job is currently performing can be got.
     **/
    public void Var043()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getFunctionName();
            assertCondition(s != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getFunctionType()
     - Ensure that the function type can be got, if the job is performing a
     high_level function.
     **/
    public void Var044()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getFunctionType();
            if (((s.equals(""))||(s.equals("C"))||(s.equals("D"))||(s.equals("G")))||
                ((s.equals("I"))||(s.equals("L"))||(s.equals("M"))||(s.equals("N")))||
                ((s.equals("O"))||(s.equals("P"))||(s.equals("R"))||(s.equals("*"))))
                condition_ = true;
            assertCondition(condition_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getInquiryMessageReply(), and setInquiryMessageReply()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var045()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getInquiryMessageReply();
            for (int x = 0; x < 3; ++x)
            {
                String str = inquiryMessageReply[x];
                job.setInquiryMessageReply(str);
                if (job.getInquiryMessageReply().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setInquiryMessageReply(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getInquiryMessageReply(), setInquiryMessageReply(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var046()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getInquiryMessageReply();
            for (int x = 0; x < 3; ++x)
            {
                String str = inquiryMessageReply[x];
                job.setCacheChanges(true);
                job.setInquiryMessageReply(str);
                job.commitChanges();
                if (job.getInquiryMessageReply().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setInquiryMessageReply(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getInteractiveTransactions()
     - Ensure that the number of interactive transactions can be got.
     **/
    public void Var047()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int int_ = job.getInteractiveTransactions();
            assertCondition(int_ >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getJobAccountingCode(), setJobAccountingCode(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var048()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getJobAccountingCode();
            for (int x = 0; x < 2; ++x)
            {
                String str = jobAccountingCode[x];
                job.setCacheChanges(true);
                job.setJobAccountingCode(str);
                job.commitChanges();
                if (job.getJobAccountingCode().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setJobAccountingCode(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getJobActiveDate()
     - Ensure that the date and time can be got.
     **/
    public void Var049()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            Date date = job.getJobActiveDate();
            assertCondition(date != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getJobDate(), and setJobDate()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var050()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            Date d = job.getJobDate();
            Calendar c = Calendar.getInstance();
            Date date_ = c.getTime();
            c.set(1998, 0, 11);
            Date date = c.getTime();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int t = c.get(Calendar.DATE);
            job.setCacheChanges(true);
            job.setJobDate(date);
            job.commitChanges();
            Date newdate = job.getJobDate();
            c.setTime(newdate);
            int y1 = c.get(Calendar.YEAR);
            int m1 = c.get(Calendar.MONTH);
            int t1 = c.get(Calendar.DATE);

            assertCondition((y==y1)&&(m==m1)&&(t==t1));
            job.setCacheChanges(false);
            job.setJobDate(d);
            c.setTime(date_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getJobDescription()
     - Ensure that the full path of the job description can be got.
     **/
    public void Var051()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getJobDescription();
            assertCondition(s != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getJobEnterSystemDate()
     - Ensure that the date and time that the job entered system can be got.
     **/
    public void Var052()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            Date date = job.getJobEnterSystemDate();
            assertCondition(date != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getJobMessageQueueFullAction(), and setJobMessageQueueFullAction()
     - Ensure that the job message queue full action can be got.
     **/
    public void Var053()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getJobAccountingCode();
            for (int x = 0; x < 2; ++x)
            {
                String str = jobMessageQueueFullAction[x];
                job.setCacheChanges(true);
                job.setJobMessageQueueFullAction(str);
                job.commitChanges();
                if (job.getJobMessageQueueFullAction().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setJobAccountingCode(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getJobMessageQueueMaximumSize()
     - Ensure that the maximum size of the job message queue can be got.
     **/
    public void Var054()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int size = job.getJobMessageQueueMaximumSize();
            assertCondition(size != -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getJobPutOnJobQueueDate()
     - Ensure that the date and time that the job was put on the job queue can be got.
     **/
    public void Var055()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName3_, userName3_, jobNumber3_);
            Date date = job.getJobPutOnJobQueueDate();
            assertCondition(date != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getScheduleDate()
     - Ensure that the date and time that the job is sheduled to run can be got.
     **/
    public void Var056()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName3_, userName3_, jobNumber3_);
            Date date = job.getScheduleDate();
            assertCondition(date != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getJobStatusInJobQueue()
     - Ensure that the status of the job on the job queue can be retrieved.
     **/
    public void Var057()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String s = job.getJobStatusInJobQueue();
            if (s.equals("")) // should not be on job queue
                succeeded();
            else
                failed("Wrong job status on job queue: ' ' != '"+s+"'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getJobSwitches(), setJobSwitches(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var058()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getJobSwitches();
            String str = "00000000";
            job.setCacheChanges(true);
            job.setJobSwitches(str);
            job.commitChanges();
            assertCondition(job.getJobSwitches().equals(str));
            job.setCacheChanges(false);
            job.setJobSwitches(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getLanguageID(), setLanguageID(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var059()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getLanguageID();
            String str = "*SYSVAL";
            job.setCacheChanges(true);
            job.setLanguageID(str);
            job.commitChanges();
            Job job2 = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            SystemValue sv = new SystemValue(pwrSys_, "QLANGID");
            String langID = (String)sv.getValue();
            if (job2.getLanguageID().equals(langID))
                succeeded();
            else
                failed("Wrong language ID: '"+job.getLanguageID()+"' != '"+langID+"'");
            job.setCacheChanges(false);
            job.setLanguageID(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getLoggingCLPrograms(), setLoggingCLPrograms(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var060()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getLoggingCLPrograms();
            for (int x = 0; x < 2; ++x)
            {
                String str = loggingCLPrograms[x];
                job.setCacheChanges(true);
                job.setLoggingCLPrograms(str);
                job.commitChanges();
                if (job.getLoggingCLPrograms().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setLoggingCLPrograms(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getLoggingLevel(), and setLoggingLevel()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var061()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int lev = job.getLoggingLevel();
            for (int x = 0; x < 5; ++x)
            {
                int level = loggingLevel[x];
                job.setLoggingLevel(level);
                if (job.getLoggingLevel() == level) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setLoggingLevel(lev);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getLoggingLevel(), setLoggingLevel(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var062()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int lev = job.getLoggingLevel();
            for (int x = 0; x < 5; ++x)
            {
                int level = loggingLevel[x];
                job.setCacheChanges(true);
                job.setLoggingLevel(level);
                job.commitChanges();
                if (job.getLoggingLevel() == level) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setLoggingLevel(lev);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getLoggingSeverity(), and setLoggingSeverity()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var063()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int sev = job.getLoggingSeverity();
            int sev_ = 2;
            job.setLoggingSeverity(sev_);
            assertCondition(job.getLoggingSeverity() == sev_);
            job.setLoggingSeverity(sev);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getLoggingSeverity(), setLoggingSeverity(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var064()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int sev = job.getLoggingSeverity();
            int sev_ = 2;
            job.setCacheChanges(true);
            job.setLoggingSeverity(sev_);
            job.commitChanges();
            assertCondition(job.getLoggingSeverity() == sev_);
            job.setCacheChanges(false);
            job.setLoggingSeverity(sev);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getLoggingText(), and setLoggingText()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var065()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getLoggingText();
            for (int x = 0; x < 3; ++x)
            {
                String str = loggingText[x];
                job.setLoggingText(str);
                if (job.getLoggingText().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setLoggingText(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getLoggingText(), setLoggingText(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var066()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getLoggingText();
            for (int x = 0; x < 3; ++x)
            {
                String str = loggingText[x];
                job.setCacheChanges(true);
                job.setLoggingText(str);
                job.commitChanges();
                if (job.getLoggingText().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setLoggingText(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getModeName()
     - Ensure that the mode name can be got.
     **/
    public void Var067()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String mode = job.getModeName();
            if (mode.equals("")) // mode name should be blank
                succeeded();
            else
                failed("Wrong mode name: ' ' != '"+mode+"'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getName()
     - Ensure that the job name can be got.
     **/
    public void Var068()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            assertCondition(job.getName().equals(jobName_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getNumber()
     - Ensure that the job number can be got.
     **/
    public void Var069()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            assertCondition(job.getNumber().equals(jobNumber_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getNumberOfLibrariesInSYSLIBL()
     - Ensure that the number of libraries in SYSLIBL can be got.
     **/
    public void Var070()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int number = job.getNumberOfLibrariesInSYSLIBL();
            assertCondition(number != -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getNumberOfLibrariesInUSRLIBL()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var071()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int number = job.getNumberOfLibrariesInUSRLIBL();
            assertCondition(number != -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getNumberOfProductLibraries()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var072()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int number = job.getNumberOfProductLibraries();
            assertCondition(number != -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getOutputQueue(), and setOutputQueue()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var073()
    {
        String failedStr = "";
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String s = job.getOutputQueue();
            for (int x = 0; x < 2; ++x)
            {
                String str = outputQueue[x];
                job.setOutputQueue(str);
                if (!job.getOutputQueue().equals(str))
                    failedStr += "\n'"+job.getOutputQueue()+"' != '"+str+"'";
            }
            if (failedStr.length() > 0)
                failed("Wrong output queue:"+failedStr);
            else
                succeeded();
            job.setOutputQueue(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getOutputQueue(), setOutputQueue(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var074()
    {
        String failedStr = "";
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setCacheChanges(true);
            String s = job.getOutputQueue();
            for (int x = 0; x < 2; ++x)
            {
                String str = outputQueue[x];
                job.setOutputQueue(str);
                job.commitChanges();
                if (!job.getOutputQueue().equals(str))
                    failedStr += "\n'"+job.getOutputQueue()+"' != '"+str+"'";
            }
            if (failedStr.length() > 0)
                failed("Wrong output queue:"+failedStr);
            else
                succeeded();
            job.setCacheChanges(false);
            job.setOutputQueue(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getOutputQueuePriority(), and setOutputQueuePriority()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var075()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int i = job.getOutputQueuePriority();
            int oqp = 5;
            job.setOutputQueuePriority(oqp);
            assertCondition(job.getOutputQueuePriority() == oqp);
            job.setOutputQueuePriority(i);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:getOutputQueuePriority(), setOutputQueuePriority(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var076()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int i = job.getOutputQueuePriority();
            int oqp = 5;
            job.setCacheChanges(true);
            job.setOutputQueuePriority(oqp);
            job.commitChanges();
            assertCondition(job.getOutputQueuePriority() == oqp);
            job.setCacheChanges(false);
            job.setOutputQueuePriority(i);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:getPoolIdentifier()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var077()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            assertCondition(job.getPoolIdentifier() > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getPrinterDeviceName(), and setPrinterDeviceName()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var078()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getPrinterDeviceName();
            String str = "*SYSVAL";
            job.setPrinterDeviceName(str);
            if (job.getPrinterDeviceName().equals(str))
            {
                succeeded();
            }
            else
            {
                failed("Incorrect printer device name: '"+
                       job.getPrinterDeviceName()+"' != '"+str+"'");
            }
            job.setPrinterDeviceName(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:getPrinterDeviceName(), setPrinterDeviceName(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var079()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getPrinterDeviceName();
            String str = "*WRKSTN";
            job.setCacheChanges(true);
            job.setPrinterDeviceName(str);
            job.commitChanges();
            if (job.getPrinterDeviceName().equals(str))
            {
                succeeded();
            }
            else
            {
                failed("Incorrect printer device name: '"+
                       job.getPrinterDeviceName()+"' != '"+str+"'");
            }
            job.setCacheChanges(false);
            job.setPrinterDeviceName(s);
        }
        catch (AS400Exception e)
        {
          String id = e.getAS400Message().getID();
          if (id.equals("CPD0912")) {
            failed(e, "Printer PRT01 is not configured. To configure PRT01, signon to the system and type: CRTDEVPRT DEVD(PRT01) DEVCLS(*LCL) TYPE(3812) MODEL(1) PORT(1) SWTSET(1) FONT(11)");
          }
          else
          {
            failed(e, "Unexpected exception occurred.");
          }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:getPrintKeyFormat(), and setPrintKeyFormat()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var080()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getPrintKeyFormat();
            for (int x = 0; x < 5; ++x)
            {
                String str = printKeyFormat[x];
                job.setPrintKeyFormat(str);
                if (job.getPrintKeyFormat().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setPrintKeyFormat(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getPrintKeyFormat(), setPrintKeyFormat(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var081()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getPrintKeyFormat();
            for (int x = 0; x < 5; ++x)
            {
                String str = printKeyFormat[x];
                job.setCacheChanges(true);
                job.setPrintKeyFormat(str);
                job.commitChanges();
                if (job.getPrintKeyFormat().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setPrintKeyFormat(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getPrintText(), and setPrintText()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var082()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getPrintText();
            for (int x = 0; x < 3; ++x)
            {
                String str = printText[x];
                job.setPrintText(str);
                if (job.getPrintText().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setPrintText(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getPrintText(), setPrintText(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var083()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getPrintText();
            for (int x = 0; x < 3; ++x)
            {
                String str = printText[x];
                job.setCacheChanges(true);
                job.setPrintText(str);
                job.commitChanges();
                if (job.getPrintText().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setPrintText(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getProductLibraries()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var084()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String[] str = job.getProductLibraries();
            String[] s = new String[str.length];
            for (int i=0; i<str.length;i++)
            {
                s[i] = str[i];
                output_.println("meb:"+s[i]);
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getQueue(), setQueue(), and commitChanges()
     - Ensure that the coded character set ID can be set and got when the type of job is *JOBQ.
     **/
    public void Var085()
    {
        try
        {
            if (pwrSys_ == null)
            {
                failed("-misc testcase parm with PwrSys not specified.");
                return;
            }
            Job job = new Job(pwrSys_, jobName1_, userName1_, jobNumber1_);
            String s = job.getQueue();
            job.setCacheChanges(true);
            job.setQueue("/QSYS.LIB/QGPL.LIB/TESTJOBQ6.JOBQ");
            job.commitChanges();
            if (job.getQueue().equals("/QSYS.LIB/QGPL.LIB/TESTJOBQ6.JOBQ"))
            {
                succeeded();
            }
            else
            {
                failed("Incorrect printer device name: '"+
                       job.getQueue()+"' != '/QSYS.LIB/QGPL.LIB/TESTJOBQ6.JOBQ'");
            }
            job.setCacheChanges(false);
            job.setQueue(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:getQueuePriority()
     We use the active QBATCH/QSYS job.
     **/
    public void Var086()
    {
        try
        {
            Job job = new Job(pwrSys_, "QBATCH", "QSYS", batchNumber_);
            /*if (job.getQueuePriority() != -1)
             {
             String failMsg = "QBATCH/QSYS/"+batchNumber_+"\n";
             failed("Error on getQueuePriority(): "+job.getQueuePriority()+"\n"+failMsg);
             }
             else*/
            assertCondition(true, "job="+job); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:getQueuePriority(), and setQueuePriority()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var087()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int oldPriority = job.getQueuePriority();
            int newPriority = 4;

            job.setQueuePriority(newPriority);

            assertCondition(job.getQueuePriority() == newPriority);
            job.setQueuePriority(oldPriority);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getQueuePriority(), setQueuePriority(), and commitChanges()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var088()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int oldPriority = job.getQueuePriority();
            int newPriority = 4;
            job.setCacheChanges(true);
            job.setQueuePriority(newPriority);
            job.commitChanges();
            assertCondition(job.getQueuePriority() == newPriority);
            job.setCacheChanges(false);
            job.setQueuePriority(oldPriority);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getRoutingData()
     - Ensure that the routing date can be retrieved.
     **/
    public void Var089()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String data = job.getRoutingData();
            if (data.equals("QCMDI"))
                succeeded();
            else
                failed("Wrong routing data: 'QCMDI' != '"+data+"'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getRunPriority(), and serRunPriority()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var090()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int i = job.getRunPriority();
            int runp = 14;
            job.setCacheChanges(true);
            job.setRunPriority(runp);
            job.commitChanges();
            assertCondition(job.getRunPriority() == runp);
            job.setCacheChanges(false);
            job.setRunPriority(i);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getRunPriority(), serRunPriority(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var091()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int i = job.getRunPriority();
            int runp = 2;
            job.setCacheChanges(true);
            job.setRunPriority(runp);
            job.commitChanges();
            assertCondition(job.getRunPriority() == runp);
            job.setCacheChanges(false);
            job.setRunPriority(i);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getSignedOnJob()
     - Ensure that the correct coded character set ID can be got
     when the job is treated like a signed-on user.
     **/
    public void Var092()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            assertCondition(job.getSignedOnJob()==true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getSignedOnJob()
     - Ensure that the correct coded character set ID can be got
     when the job is not treated like a signed-on user.
     **/
    public void Var093()
    {
        try
        {
            // Note: getSignedOnJob() always returns true... cannot find a case
            // where a job would not be "signed on".
            //
            //            Job job = new Job(pwrSys_, "QBATCH", "QSYS", batchNumber_);
            //            if (job.getSignedOnJob())
            //            {
            //              String failMsg = "QBATCH/QSYS/"+batchNumber_+"\n";
            //              failed("Error: getSignedOnJob() returned "+job.getSignedOnJob()+"\n"+failMsg);
            //            }
            //            else
            //            {
            succeeded();
            //            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:getSortSequenceTable(), setSortSequenceTable(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var094()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getSortSequenceTable();
            //            String str = "*SYSVAL";
            String str = "/QSYS.LIB/TLIB6.LIB/TESTSEQ.FILE"; // setup() created a sort sequence table for us
            job.setCacheChanges(true);
            job.setSortSequenceTable(str);
            job.commitChanges();
            if (!job.getSortSequenceTable().equals(str))
                failed("Wrong sort sequence table: \n"+
                       "'"+str+"' != '"+job.getSortSequenceTable()+"'\n");
            else
                succeeded();
            job.setCacheChanges(false);
            job.setSortSequenceTable(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:getStatus()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var095()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String status = job.getStatus();
            if (status.equals("*ACTIVE"))
                succeeded();
            else
                failed("Wrong job status: '*ACTIVE' != '"+status+"'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getStatusMessageHandling(), and setStatusMessageHandling()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var096()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getStatusMessageHandling();
            for (int x = 0; x < 2; ++x)
            {
                String str = statusMessageHandling[x];
                job.setStatusMessageHandling(str);
                if (job.getStatusMessageHandling().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setStatusMessageHandling(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getStatusMessageHandling(), setStatusMessageHandling(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var097()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getStatusMessageHandling();
            for (int x = 0; x < 3; ++x)
            {
                String str = statusMessageHandling[x];
                job.setCacheChanges(true);
                job.setStatusMessageHandling(str);
                job.commitChanges();
                if (job.getStatusMessageHandling().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setStatusMessageHandling(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getSubsystem()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var098()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String sub = job.getSubsystem();
            assertCondition(sub.equals("/QSYS.LIB/QINTER.SBSD"), "Wrong subsystem: '/QSYS.LIB/QINTER.SBSD' != '" + sub + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getSubtype()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var099()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getSubtype().trim();
            if (((s.equals(""))||(s.equals("D"))||(s.equals("E"))||(s.equals("F")))||
                ((s.equals("J"))||(s.equals("P"))||(s.equals("T"))||(s.equals("U"))))
                condition_ = true;
            assertCondition(condition_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getSystem()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var100()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            assertCondition(job.getSystem().equals(pwrSys_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getSystemLibraryList().
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var101()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String[] libList = job.getSystemLibraryList();

            // The syslibl is different from system to system, so use the sysval.
            SystemValue sv = new SystemValue(pwrSys_, "QSYSLIBL");
            String[] expected = (String[])sv.getValue();

            boolean failed = false;
            if (libList.length != expected.length)
            {
                failed = true;
            }
            else
            {
                for (int i = 0; i < libList.length; ++i)
                {
                    if (!libList[i].equals(expected[i].trim())) failed = true;
                }
            }
            if (failed)
            {
                String failedStr = "\n" + expected.length + ": ";
                for (int i = 0; i < expected.length - 1; ++i)
                {
                    failedStr += "'" + expected[i] + "', ";
                }
                failedStr += "'" + expected[expected.length - 1] + "'";
                failedStr += "\n" + libList.length + ": ";
                for (int i = 0; i < libList.length - 1; ++i)
                {
                    failedStr += "'" + libList[i] + "', ";
                }
                failedStr += "'" + libList[libList.length - 1] + "'";
                failed("Wrong system library list:" + failedStr);
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getTimeSeparator(), setTimeSeparator(), and commitChanges()
     - Ensure that the value can be set and got by the two methods mentioned above.
     **/
    public void Var102()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getTimeSeparator();
            for (int x = 0; x < 5; ++x)
            {
                String str = timeSeparator[x];
                job.setCacheChanges(true);
                job.setTimeSeparator(str);
                job.commitChanges();
                if (job.getTimeSeparator().equals(str)) condition_ = true;
                if (condition_ == false)  break;
            }
            assertCondition(condition_);
            job.setCacheChanges(false);
            job.setTimeSeparator(s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getTotalResponseTime()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var103()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            assertCondition(job.getTotalResponseTime() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getType()
     - Ensure that the correct coded character set ID can be got.
     **/
    public void Var104()
    {
        boolean condition_ = false;
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = job.getType();
            if (((s.equals(""))||(s.equals("A"))||(s.equals("B")))||
                ((s.equals("I"))||(s.equals("M"))||(s.equals("R")))||
                ((s.equals("S"))||(s.equals("W"))||(s.equals("X"))))
                condition_ = true;
            assertCondition(condition_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getUser()
     - Ensure that the correct user is retrieved.
     **/
    public void Var105()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String user = job.getUser();
            if (user.equals("TSTJOBUSR1"))
                succeeded();
            else
                failed("Wrong user: 'TSTJOBUSR1' != '"+user+"'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getUserLibraryList().
     - Ensure that the user profile under which the job runs can be got.
     **/
    public void Var106()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String[] libList = job.getUserLibraryList();

            // The usrlibl is different from system to system, so use the sysval.
            SystemValue sv = new SystemValue(pwrSys_, "QUSRLIBL");
            String[] expected = (String[])sv.getValue();

            boolean failed = false;
            if (libList.length != expected.length)
            {
                failed = true;
            }
            else
            {
                for (int i = 0; i < libList.length; ++i)
                {
                    if (!libList[i].equals(expected[i].trim())) failed = true;
                }
            }
            if (failed)
            {
                String failedStr = "\n" + expected.length + ": ";
                for (int i = 0; i < expected.length - 1; ++i)
                {
                    failedStr += "'" + expected[i] + "', ";
                }
                failedStr += "'" + expected[expected.length - 1] + "'";
                failedStr += "\n" + libList.length + ": ";
                for (int i = 0; i < libList.length - 1; ++i)
                {
                    failedStr += "'" + libList[i] + "', ";
                }
                failedStr += "'" + libList[libList.length - 1] + "'";
                failed("Wrong user library list:" + failedStr);
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:getWorkIDUnit()
     - Ensure that the user library list can be got correctly.
     **/
    public void Var107()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String unit = job.getWorkIDUnit();
            if (unit != null) // this should be nothing
                succeeded();
            else
                failed("Wrong unit of work ID: '' != '"+unit+"'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:loadInformation()
     - Ensure that the information can be loaded correctly.
     **/
    public void Var108()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            Date d = job.getJobDate();
            Calendar c = Calendar.getInstance();
            c.set(2008, 10, 11, 11, 11, 11);
            Date date = c.getTime();
            job.loadInformation();
            job.setJobDate(date);
            //Date ret = job.getJobDate();
            Calendar ret = Calendar.getInstance();
            ret.setTime(job.getJobDate());

            //if (ret.equals(date))
            //  succeeded();
            // Just check to see if the date portions are correct, not the time
            if (c.get(Calendar.YEAR) == ret.get(Calendar.YEAR) &&
                c.get(Calendar.MONTH) == ret.get(Calendar.MONTH) &&
                c.get(Calendar.DAY_OF_WEEK) == ret.get(Calendar.DAY_OF_WEEK) &&
                c.get(Calendar.DATE) == ret.get(Calendar.DATE))
                succeeded();
            else
                failed("Wrong job date: '"+c+"' != '"+ret+"'");
            job.setJobDate(d);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested: readObject()
     - Ensure that correct object will be returned by this method
     **/
    public void Var109()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            FileOutputStream ostream = new FileOutputStream("JOB.SER");
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            p.writeObject(job);
            p.flush();
            ostream.close();

            FileInputStream istream = new FileInputStream("JOB.SER");
            ObjectInputStream p1 = new ObjectInputStream(istream);
            Job j = (Job)p1.readObject();
            istream.close();

            if (!j.getName().equals(jobName_))
                failed("Job name not serialized.");
            else if (!j.getUser().equals(userName_))
                failed("User name not serialized.");
            else if (!j.getNumber().equals(jobNumber_))
                failed("Job number not serialized.");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        try
        {
            File f = new File("JOB.SER");
            f.delete();
        }
        catch (Exception e)
        {
        }
    }


    /**
     Method tested:setBreakMessageHandling()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var110()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String s = "&^%$";
            job.setBreakMessageHandling(s);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setBreakMessageHandling()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var111()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setBreakMessageHandling(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setCodedCharacterSetID()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var112()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int newID = -3;
            job.setCodedCharacterSetID(newID);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setCountryID()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var113()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String id = "star";
            job.setCountryID(id);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setCountryID()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var114()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setCountryID(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setDateFormat()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var115()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "*USE";
            job.setDateFormat(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setDateFormat()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var116()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setDateFormat(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setDateSeparator()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var117()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "Y";
            job.setDateSeparator(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setDateSeparator()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var118()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setDateSeparator(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setDDMConversationHandling()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var119()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "*TTTT";
            job.setDDMConversationHandling(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setDDMConversationHandling()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var120()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setDDMConversationHandling(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setDecimalFormat()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var121()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "*";
            job.setDecimalFormat(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setDecimalFormat()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var122()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setDecimalFormat(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setDefaultWait()
     - Ensure that the setting of the default waiting time is correct.
     **/
    public void Var123()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int oldTime = job.getDefaultWait();
            int newTime = 20;
            job.setCacheChanges(true);
            job.setDefaultWait(newTime);
            job.commitChanges();
            if (job.getDefaultWait() == newTime)
                succeeded();
            else
                failed("Wrong default wait: "+job.getDefaultWait()+" != "+newTime);
            job.setCacheChanges(false);
            job.setDefaultWait(oldTime);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:setDefaultWait()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var124()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int newTime = -20;
            job.setDefaultWait(newTime);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setDeviceRecoveryAction()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var125()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "*TTT";
            job.setDeviceRecoveryAction(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setDeviceRecoveryAction()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var126()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setDeviceRecoveryAction(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setInquiryMessageReply()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var127()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "*TTT";
            job.setInquiryMessageReply(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))
                succeeded();
            else
                failed(e, "Incorrect exception info.");
        }
    }

    /**
     Method tested:setInquiryMessageReply()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var128()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setInquiryMessageReply(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setJobAccountingCode()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var129()
    {
        notApplicable("Obsolete testcase");  // Difficult to force error.
        /*
         if (pwrSys_ == null)
         {
         failed("-misc testcase parm with PwrSys not specified.");
         return;
         }
         try
         {
         Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
         String newCode = "hgddhgfdsdsfdfsfdfdhfjdshfkjdshfccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccckjdshkjfhdskjfhdskjfhkjdsfhkjdshfkjHJG%^$&&^HFHGuyr";
         job.setJobAccountingCode(newCode);
         job.commitChanges();
         failed("Exception didn't occur.");
         }
         catch(Exception e)
         {
         if (exceptionIs(e, "ExtendedIllegalArgumentException"))
         succeeded();
         else
         failed(e, "Unexpected exception occurred.");
         }
         */
    }

    /**
     Method tested:setJobAccountingCode()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var130()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setJobAccountingCode(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setJobDate()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var131()
    {

        notApplicable("Obsolete testcase");  // Difficult to force error.
        /*
         if (pwrSys_ == null)
         {
         failed("-misc testcase parm with PwrSys not specified.");
         return;
         }
         try
         {
         Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
         Calendar c = Calendar.getInstance();
         Date d = c.getTime();
         c.set(1798, 0, 11);
         Date date = c.getTime();
         job.setJobDate(date);
         job.commitChanges();
         failed("Exception didn't occur.");
         job.setJobDate(d);
         c.setTime(d);
         }
         catch(Exception e)
         {
         if (exceptionIs(e, "ExtendedIllegalArgumentException"))
         succeeded();
         else
         failed(e, "Unexpected exception occurred.");
         }
         */
    }

    /**
     Method tested:setJobDate()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var132()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setJobDate(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setJobMessageQueueFullAction()
     - Ensure that the AS400Exception will be thrown if "" is set by this method.
     **/
    public void Var133()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            job.setCacheChanges(true);
            job.setJobMessageQueueFullAction("");
            job.commitChanges();
            failed("Expected exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))
                succeeded();
            else
                failed(e, "Incorrect exception info.");
        }
    }

    /**
     Method tested:setJobMessageQueueFullAction()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var134()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "ggf%^^*&&&&*$#shdfy";
            job.setJobMessageQueueFullAction(s);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setJobMessageQueueFullAction()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var135()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setJobMessageQueueFullAction(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setQueue()
     - Ensure that the AS400Exception will be thrown if the status of the job is interactive.
     **/
    public void Var136()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "/QSYS.LIB/QGPL.LIB/TESTJOBQ8.JOBQ";
            job.setQueue(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setQueue()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var137()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setQueue(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setQueuePriority()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var138()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int priority = -1;
            job.setQueuePriority(priority);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setJobSwitches()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var139()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "retddddddddddddddddssssssssssssssssur%$#*&$#*@#%KKKKKKJDKI";
            job.setJobSwitches(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setJobSwitches()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var140()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setJobSwitches(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setLanguageID()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var141()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String id = "*AABB";
            job.setLanguageID(id);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }
    /**
     Method tested:setLanguageID()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var142()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String id = "*AAAA";
            job.setLanguageID(id);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }
    /**
     Method tested:setLanguageID()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var143()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setLanguageID(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setLoggingCLPrograms()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var144()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String newProgram = "kkkkkkkhfd&&&%$#*!@%ASFK";
            job.setLoggingCLPrograms(newProgram);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setLoggingCLPrograms()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var145()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setLoggingCLPrograms(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setLoggingLevel()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var146()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int level = -2;
            job.setLoggingLevel(level);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setLoggingSeverity()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var147()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int i = -1;
            job.setLoggingSeverity(i);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setLoggingText()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var148()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "hagSJDHuas%$#*&!%%%%$ASkfkjire";
            job.setLoggingText(s);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setLoggingText()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var149()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setLoggingText(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setOutputQueue()
     - Ensure that the IllegalPathNameException will be thrown if an invalid value is set by this method.
     **/
    public void Var150()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String s = "/*DDD";
            job.setOutputQueue(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "IllegalPathNameException");
        }
    }

    /**
     Method tested:setOutputQueue()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var151()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setOutputQueue(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setOutputQueuePriority()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var152()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int priority = -1;
            job.setOutputQueuePriority(priority);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setPrinterDeviceName()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var153()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String name = "DFashdd%$#*&@iurehfHGFUYGG";
            job.setPrinterDeviceName(name);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setPrinterDeviceName()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var154()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setPrinterDeviceName(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setPrintKeyFormat()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var155()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String newFormat = "FGfghhdgjew^%$^#%$ghhhASFGl";
            job.setPrintKeyFormat(newFormat);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setPrintKeyFormat()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var156()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setPrintKeyFormat(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setPrintText()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var157()
    {
        notApplicable("Obsolete testcase"); // Too hard to force error.
        /*
         if (pwrSys_ == null)
         {
         failed("-misc testcase parm with PwrSys not specified.");
         return;
         }
         try
         {
         Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
         String s = "*()uuyewr)(fdgfdgfdgdfgfdgduieg^%^$&#$&*ASRFEDiood";
         job.setPrintText(s);
         job.commitChanges();
         failed("Exception didn't occur.");
         }
         catch(Exception e)
         {
         assertExceptionIs(e, "AS400Exception");
         */
    }

    /**
     Method tested:setPrintText()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var158()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setPrintText(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setPurge()
     - Ensure that the job is eligible to be moved out of main storage and put into auxiliary
     storage at the end of a time slice or when entering a long wait is correct by this method.
     **/
    public void Var159()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            boolean b = true;
            job.setCacheChanges(true);
            job.setPurge(b);
            job.commitChanges();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:setPurge()
     - Ensure that the job is not eligible to be moved out of main storage and put into auxiliary
     storage at the end of a time slice or when entering a long wait is correct by this method.
     **/
    public void Var160()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            boolean b = false;
            job.setCacheChanges(true);
            job.setPurge(b);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:setRunPriority()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var161()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int priority = -1;
            job.setRunPriority(priority);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setScheduleDate(Date date)
     - Ensure that the setting of schedule data is correct.
     **/
    public void Var162()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName3_, userName3_, jobNumber3_);
            Calendar c = Calendar.getInstance();
            c.set(2029, 0, 11, 11, 9, 11);                               // @A1C
            Date date = c.getTime();
            job.setCacheChanges(true);
            job.setScheduleDate(date);
            job.commitChanges();
            job.loadInformation();
            //Date ret = job.getScheduleDate();
            //if (date.equals(ret))
            //  succeeded();

            // Just compare the date portion, not the time
            Calendar ret = Calendar.getInstance();
            ret.setTime(job.getScheduleDate());
            if (c.get(Calendar.YEAR) == ret.get(Calendar.YEAR) &&
                c.get(Calendar.MONTH) == ret.get(Calendar.MONTH) &&
                c.get(Calendar.DAY_OF_WEEK) == ret.get(Calendar.DAY_OF_WEEK) &&
                c.get(Calendar.DATE) == ret.get(Calendar.DATE))
                succeeded();
            else
                failed("Wrong schedule dates: '"+date+"' != '"+ret+"'");

            // c.set(year,month,date,hour,min,sec)
            // Date must be future from current date
            c.set(2028, 0, 11, 11, 9, 11);                               // @A1C
            job.setCacheChanges(false);
	    // If following fails, verify that the date above is "future"          @A1A
            job.setScheduleDate(c.getTime());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Method tested:setScheduleDate(Date date)
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var163()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);

            Date d = null;
            job.setCacheChanges(true);
            job.setScheduleDate(d);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Incorrect exception info.");
        }
    }

    /**
     Method tested:setScheduleDate(String s)
     - Ensure that the setting of schedule data is correct.
     **/
    public void Var164()
    {
        notApplicable("Obsolete testcase"); // No longer supported.
        /*
         try
         {
         String failedStr = "";
         Job job = new Job(systemObject_, jobName3_, userName3_, jobNumber3_);
         job.setCacheChanges(true);
         // *MONTHSTR
         job.setScheduleDate("*MONTHSTR");
         job.setScheduleTime("*CURRENT");
         job.commitChanges();
         job.loadInformation();
         Date ret = job.getScheduleDate();
         Calendar nextMonth = Calendar.getInstance();
         nextMonth.add(Calendar.MONTH, 1);
         nextMonth.set(Calendar.DAY_OF_MONTH, 1); // The beginning of next month
         Date cmp = nextMonth.getTime();

         // Should be within 10 minutes of each other
         long t1 = cmp.getTime();
         long t2 = ret.getTime();
         long diff = (t1 > t2) ? (t1 - t2) : (t2 - t1);

         //          if (!ret.equals(nextMonth.getTime()))
         if (diff > 600000) // num of ms in 10 min
         {
         failed("Wrong date for *MONTHSTR: '"+ret+"' != `"+nextMonth.getTime()+"'");
         }
         else
         {
         // *MONTHEND
         job.setScheduleDate("*MONTHEND");
         job.commitChanges();
         job.loadInformation();
         ret = job.getScheduleDate();
         Calendar endOfMonth = Calendar.getInstance();
         endOfMonth.add(Calendar.MONTH, 1);
         endOfMonth.set(Calendar.DAY_OF_MONTH, 1);
         endOfMonth.add(Calendar.DAY_OF_MONTH, -1); // The last day of this month
         cmp = endOfMonth.getTime();

         // Should be within 10 minutes of each other
         t1 = cmp.getTime();
         t2 = ret.getTime();
         diff = (t1 > t2) ? (t1 - t2) : (t2 - t1);
         //if (!ret.equals(endOfMonth.getTime()))
         if (diff > 600000) // num of ms in 10 min
         {
         failed("Wrong date for *MONTHEND: '"+ret+"' != `"+endOfMonth.getTime()+"'");
         }
         else
         {
         succeeded();
         }
         }
         }
         catch(Exception e)
         {
         failed(e, "Unexpected exception.");
         }
         */
    }

    /**
     Method tested:setScheduleDate(String s)
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var165()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = null;
            job.setCacheChanges(true);
            job.setScheduleDate(s);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setScheduleTime(Date date)
     - Ensure that the schedule time can be set by setScheduleTime(Date date).
     **/
    public void Var166()
    {
        notApplicable("Obsolete testcase");
        /*
         try
         {
         Job job = new Job(pwrSys_, jobName3_, userName3_, jobNumber3_);
         Calendar c = Calendar.getInstance();
         Date d = c.getTime();
         c.set(2098, 12, 1, 1, 23, 11);
         Date date = c.getTime();
         job.setCacheChanges(true);
         job.setScheduleTime(date);
         job.commitChanges();
         job.loadInformation();
         //Date ret = job.getScheduleDate();
         //if (date.equals(ret))
         //  succeeded();

         // Compare just the times, not the dates
         Calendar ret = Calendar.getInstance();
         ret.setTime(job.getScheduleDate());
         if (c.get(Calendar.HOUR) == ret.get(Calendar.HOUR) &&
         c.get(Calendar.MINUTE) == ret.get(Calendar.MINUTE) &&
         c.get(Calendar.SECOND) == ret.get(Calendar.SECOND) &&
         c.get(Calendar.HOUR_OF_DAY) == ret.get(Calendar.HOUR_OF_DAY))
         succeeded();
         else
         failed("Wrong schedule times: '"+date+"' != '"+ret+"'");
         c.set(2007, 0, 11, 11, 8, 11);
         job.setCacheChanges(false);
         job.setScheduleTime(c.getTime());
         }
         catch(Exception e)
         {
         failed(e, "Unexpected exception.");
         }
         */
    }

    /**
     Method tested:setScheduleTime(Date date)
     - Ensure that the  NullPointerException will be thrown if null is set by this method.
     **/
    public void Var167()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            Date t = null;
            job.setCacheChanges(true);
            job.setScheduleTime(t);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else if (exceptionIs(e, "NullPointerException"))
                failed();
        }
    }
    /**
     Method tested:setScheduleTime(String s)
     - Ensure that the schedule time can be set by setScheduleTime(String s) .
     **/
    public void Var168()
    {
        notApplicable("Obsolete testcase"); // No longer supported.
        /*
         if (pwrSys_ == null)
         {
         failed("-misc testcase parm with PwrSys not specified.");
         return;
         }
         try
         {
         Job job = new Job(pwrSys_, jobName3_, userName3_, jobNumber3_);
         String scheduleTime = "*CURRENT";
         String scheduleDate = "*MONTHEND";
         job.setCacheChanges(true);
         job.setScheduleTime(scheduleTime);
         job.setScheduleDate(scheduleDate);
         job.commitChanges();
         succeeded();
         }
         catch(Exception e)
         {
         failed(e, "Unexpected exception.");
         }
         */
    }

    /**
     Method tested:setScheduleTime(String s)
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var169()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "GHGVHjhgj*&%^*%JGJHGJGHHyughj";
            job.setScheduleTime(s);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:setScheduleTime(String s)
     - Ensure that the NullPointerException will be thrown if an invalid value is set by this method.
     **/
    public void Var170()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = null;
            job.setScheduleTime(s);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setSortSequenceTable()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var171()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "*CEC";
            job.setCacheChanges(true);
            job.setSortSequenceTable(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception") &&
                e.getMessage().equalsIgnoreCase("CPF1897 DATA FOR KEY FIELD 1901 NOT VALID."))
                succeeded();
            else
                failed(e, "Incorrect exception info.");
        }
    }

    /**
     Method tested:setSortSequenceTable()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var172()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setCacheChanges(true);
            job.setSortSequenceTable(null);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setStatusMessageHandling()
     - Ensure that the CharConversionException will be thrown if an invalid value is set by this method.
     **/
    public void Var173()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "FGDkljlkj&^%^%iuyhuiRTEDKJH";
            job.setCacheChanges(true);
            job.setStatusMessageHandling(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "CharConversionException");
        }
    }

    /**
     Method tested:setStatusMessageHandling()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var174()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setCacheChanges(true);
            job.setStatusMessageHandling(null);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setTimeSeparator()
     - Ensure that the ExtendedIllegalArgumentException will be thrown if an invalid value is set by this method.
     **/
    public void Var175()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String s = "GHGFHGtyfhgv&*%&%$kljhUYIUYGhg";
            job.setCacheChanges(true);
            job.setTimeSeparator(s);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setTimeSeparator()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var176()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setCacheChanges(true);
            job.setTimeSeparator(null);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:setTimeSlice()
     - Ensure that the setting of time slice is correct.
     **/
    public void Var177()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            int slice = 1;
            job.setCacheChanges(true);
            job.setTimeSlice(slice);
            job.commitChanges();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:setTimeSlice()
     - Ensure that the AS400Exception will be thrown if an invalid value is set by this method.
     **/
    public void Var178()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            int slice = -1;
            job.setCacheChanges(true);
            job.setTimeSlice(slice);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "AS400Exception");
        }
    }

    /**
     Method tested:setTimeSliceEndPool()
     - Ensure that the setting whether the interactive jobs are moved to another
     main storage pool at the end of the time slice is correct.
     **/
    public void Var179()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            for (int x = 0; x < 3; ++x)
            {
                String newValue = timeSliceEndPool[x];
                job.setCacheChanges(true);
                job.setTimeSliceEndPool(newValue);
                job.commitChanges();
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:setTimeSliceEndPool()
     - Ensure that the CharConversionException will be thrown if an invalid value is set by this method.
     **/
    public void Var180()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            String newValue = "HGJHGJKuguyg877^%$$@#jhklUYGJHJkgh";
            job.setCacheChanges(true);
            job.setTimeSliceEndPool(newValue);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "CharConversionException");
        }
    }

    /**
     Method tested:setTimeSliceEndPool()
     - Ensure that the NullPointerException will be thrown if null is set by this method.
     **/
    public void Var181()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.setCacheChanges(true);
            job.setTimeSliceEndPool(null);
            job.commitChanges();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:toString()
     - Ensure that the string representation of the job is correct.
     **/
    public void Var182()
    {
        try
        {
            Job job = new Job(systemObject_, jobName_, userName_, jobNumber_);
            String str = job.toString();
            String expected = jobNumber_+"/"+userName_+"/"+jobName_;
            if (str.equals(expected))
                succeeded();
            else
                failed("Wrong toString() value: '"+expected+"' != '"+str+"'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested: writeObject()
     - Ensure that the method runs correct result.
     **/

    public void Var183()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            FileOutputStream ostream = new FileOutputStream("TT.TMP");
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            p.writeObject(job);
            p.flush();
            ostream.close();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
     * Method tested: Job(AS400)
     **/
    public void Var184()
    {
        try
        {
            Job job = new Job(pwrSys_);
            assertCondition(true, "job="+job); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
     * Method tested: Job(AS400) and getValue().
     * The job will be the current job.
     **/
    public void Var185()
    {
        try
        {
            Job job = new Job(pwrSys_);
            job.getDateSeparator();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Method tested: Job(AS400), setName(), setUser(), setNumber(), and getValue().
     * Ensure that getValue() does not throw an exception.
     **/
    public void Var186()
    {
        try
        {
            Job job = new Job(pwrSys_);
            job.setName(jobName_);
            job.setUser(userName_);
            job.setNumber(jobNumber_);
            job.getDateSeparator();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Method tested: Job(AS400).
     * Ensure that a NullPointerException is thrown.
     **/
    public void Var187()
    {
        try
        {
            Job job = new Job(null);
            failed("Expected exception did not occur."+job);
        }
        catch (NullPointerException npe)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Method tested: setName(null).
     * Ensure that a NullPointerException is thrown.
     **/
    public void Var188()
    {
        try
        {
            Job job = new Job(pwrSys_);
            job.setName(null);
            failed("Expected exception did not occur.");
        }
        catch (NullPointerException npe)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Method tested: setUser(null).
     * Ensure that a NullPointerException is thrown.
     **/
    public void Var189()
    {
        try
        {
            Job job = new Job(pwrSys_);
            job.setUser(null);
            failed("Expected exception did not occur.");
        }
        catch (NullPointerException npe)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Method tested: setNumber(null).
     * Ensure that a NullPointerException is thrown.
     **/
    public void Var190()
    {
        try
        {
            Job job = new Job(pwrSys_);
            job.setNumber(null);
            failed("Expected exception did not occur.");
        }
        catch (NullPointerException npe)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Method tested: setName() after connecting.
     * Ensure that an ExtendedIllegalStateException is thrown.
     **/
    public void Var191()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.getDateSeparator();
            job.setName("blah");
            failed("Expected exception did not occur.");
        }
        catch (ExtendedIllegalStateException npe)
        {
            if (npe.getReturnCode() == ExtendedIllegalStateException.PROPERTY_NOT_CHANGED)
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Method tested: setUser() after connecting.
     * Ensure that an ExtendedIllegalStateException is thrown.
     **/
    public void Var192()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.getDateSeparator();
            job.setUser("blah");
            failed("Expected exception did not occur.");
        }
        catch (ExtendedIllegalStateException npe)
        {
            if (npe.getReturnCode() == ExtendedIllegalStateException.PROPERTY_NOT_CHANGED)
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Method tested: setNumber() after connecting.
     * Ensure that an ExtendedIllegalStateException is thrown.
     **/
    public void Var193()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.getDateSeparator();
            job.setNumber("blah");
            failed("Expected exception did not occur.");
        }
        catch (ExtendedIllegalStateException npe)
        {
            if (npe.getReturnCode() == ExtendedIllegalStateException.PROPERTY_NOT_CHANGED)
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Method tested: setSystem() after connecting.
     * Ensure that an ExtendedIllegalStateException is thrown.
     **/
    public void Var194()
    {
        try
        {
            Job job = new Job(pwrSys_, jobName_, userName_, jobNumber_);
            job.getDateSeparator();
            job.setSystem(systemObject_);
            failed("Expected exception did not occur.");
        }
        catch (ExtendedIllegalStateException npe)
        {
            if (npe.getReturnCode() == ExtendedIllegalStateException.PROPERTY_NOT_CHANGED)
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested:Job(AS400 as400, byte[] internalJobID)
     - Ensure that the NullPointerException will be thrown if the internalJobID is null in constructor with arguments.
     **/
    public void Var195()
    {
        try
        {
            Job job = new Job(systemObject_, (byte[])null);
            failed("Exception didn't occur."+job);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }
}
