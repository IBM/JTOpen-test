///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JobListTestcase.java
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
// File Name:  JobListTestcase.java
//
// Class Name:  JobListTestcase
//
///////////////////////////////////////////////////////////////////////////////
//
//
//
///////////////////////////////////////////////////////////////////////////////

package test.Job;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobList;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.SequentialFile;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase JobListTestcase.
 <p>This tests the following JobList methods:
 <ul>
 <li>ctors
 <li>getName
 <li>getNumber
 <li>getLength
 <li>getSystem
 <li>getUser
 <li>getJobs
 <li>setName
 <li>setNumber
 <li>setSystem
 <li>setUser
 <li>addJobAttributeToRetrieve
 <li>addJobAttributeToSortOn
 <li>addJobSelectionCriteria
 <li>addPropertyChangeListener
 <li>addVetoableChangeListener
 <li>clearJobAttributesToRetrieve
 <li>clearJobAttributesToSortOn
 <li>clearJobSelectionCriteria
 <li>close
 <li>load
 </ul>
 **/
public class JobListTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JobListTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JobTest.main(newArgs); 
   }
    private CommandCall c = null;
    private Hashtable sysJobs_ = null;  // Contains jobs retrieved via WRKSBSJOB.
    private Hashtable jobs_ = null;  // Contains jobs retrieved via getJobs().

    private String getKeyForHashtable(String data)
    {
        String s = data.substring(0, 37).trim();
        StringBuffer buf = new StringBuffer();
        StringTokenizer st = new StringTokenizer(s);
        String[] tokens = new String[3];
        int count = 0;
        while (st.hasMoreTokens() && count < 3)
        {
            tokens[count++] = st.nextToken();
        }
        buf.append(tokens[2]);
        buf.append("/");
        buf.append(tokens[1]);
        buf.append("/");
        buf.append(tokens[0]);
        return buf.toString();
    }

    private void outputErrors()
    {
        AS400Message[] msgs = c.getMessageList();
        for (int i = 0; i < msgs.length; ++i)
        {
            output_.println(msgs[i]);
        }
    }

     boolean retrieveJobs()
    {
        try
        {
            if (!c.run("CRTLIB JOBLTEST"))
            {
                if (c.getMessageList()[0].getID().equals("CPF2111"))
                {
                    if (!c.run("CLRLIB JOBLTEST"))
                    {
                        outputErrors();
                        return false;
                    }
                }
                else
                {
                    outputErrors();
                    return false;
                }
            }
            SequentialFile f = new SequentialFile(systemObject_, "/QSYS.LIB/JOBLTEST.LIB/WRKSBSJOB.FILE/%FILE%.MBR");
            f.create(132, "*DATA", null);
            if (!c.run("CHGPF FILE(JOBLTEST/WRKSBSJOB) SIZE(*NOMAX)"))
            {
                outputErrors();
                return false;
            }
            SequentialFile f2 = new SequentialFile(systemObject_, "/QSYS.LIB/JOBLTEST.LIB/WRKACTJOB.FILE/%FILE%.MBR");
            f2.create(132, "*DATA", null);
            if (!c.run("CHGPF FILE(JOBLTEST/WRKACTJOB) SIZE(*NOMAX)"))
            {
                outputErrors();
                return false;
            }
            if (!c.run("WRKSBSJOB OUTPUT(*PRINT)"))
            {
                outputErrors();
                return false;
            }
            if (!c.run("CPYSPLF FILE(QPDSPSBJ) TOFILE(JOBLTEST/WRKSBSJOB) JOB(" + systemObject_.getUserId().trim() + "/QPRTJOB) SPLNBR(*LAST) MBROPT(*REPLACE)"))
            {
                outputErrors();
                return false;
            }
            if (!c.run("WRKACTJOB OUTPUT(*PRINT)"))
            {
                outputErrors();
                return false;
            }
            if (!c.run("CPYSPLF FILE(QPDSPAJB) TOFILE(JOBLTEST/WRKACTJOB) JOB(" + systemObject_.getUserId().trim() + "/QPRTJOB) SPLNBR(*LAST) MBROPT(*REPLACE)"))
            {
                outputErrors();
                return false;
            }
            // Process the WRKSBSJOB output.
            // Get rid of extraneous information (first 5 lines of file and last line).
            f.open(AS400File.READ_WRITE, 0, 1);
            for (int i = 1; i <= 5; ++i)
            {
                f.deleteRecord(i);
            }
            f.positionCursorToLast();
            f.deleteCurrentRecord();
            f.close();
            f.open(AS400File.READ_ONLY, 100, 1);
            sysJobs_ = new Hashtable();
            Record r = f.readNext();
            String key= null;
            String line = null;
            while (r != null)
            {
                line = r.toString();
                // Only look at lines that are valid job lines.
                if (line.startsWith("   ") && !line.startsWith("    ") && !(line.indexOf("JOB NAME") > 0) && !(line.indexOf("Job Name") > 0) && !(line.indexOf("OUTQ") > 0))
                {
                    // Extract key for hash table.
                    key = getKeyForHashtable(r.toString());
                    sysJobs_.put(key, r.toString());
                }
                r = f.readNext();
            }
            f.close();

            // Process the WRKACTJOB output.
            // Get rid of extraneous information (first 5 lines of file and last line).
            f2.open(AS400File.READ_WRITE, 0, 1);
            for (int i = 1; i <= 10; ++i)
            {
                f2.deleteRecord(i);
            }
            f2.positionCursorToLast();
            f2.deleteCurrentRecord();
            f2.close();
            f2.open(AS400File.READ_ONLY, 100, 1);
            r = f2.readNext();
            key= null;
            line = null;
            while (r != null)
            {
                line = r.toString();
                // Only look at lines that are valid subsystem job or system job lines.
                if (line.indexOf("SBS") > 35 || line.lastIndexOf("SYS") > 35)
                {
                    // Extract key for hash table.
                    key = getKeyForHashtable(r.toString());
                    sysJobs_.put(key, r.toString());
                }
                r = f2.readNext();
            }
            f2.close();
        }
        catch (Exception e)
        {
            e.printStackTrace(output_);
            return false;
        }
        return true;
    }

    boolean verifyJobs(JobList j)
    {
        try
        {
            jobs_ = new Hashtable();
            Enumeration jobs = j.getJobs();
            while (jobs.hasMoreElements())
            {
                Job jb = (Job)jobs.nextElement();
                jobs_.put(jb.toString(), jb.toString());
            }
            Enumeration e = jobs_.keys();
            while (e.hasMoreElements())
            {
                String key = (String)e.nextElement();
                if (!sysJobs_.containsKey(key))
                {
                    output_.println("Job: " + key + " not found in WRKSBSJOB output.");
                    outputLists();
                    return false;
                }
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace(output_);
            return false;
        }
    }

    private void outputLists()
    {
        Enumeration jkeys = jobs_.keys();
        Enumeration sjkeys = sysJobs_.keys();
        output_.println();
        output_.println("   WRKSBSJOB jobs                JobList jobs:");
        String jlJob = null;
        String ajJob = null;
        while (sjkeys.hasMoreElements())
        {
            ajJob = (String)sjkeys.nextElement();
            output_.print(ajJob);
            for (int i = 0; i < (30 - ajJob.length()); ++i)
                output_.print(" ");
            if (jkeys.hasMoreElements())
            {
                jlJob = (String)jkeys.nextElement();
                output_.println(jlJob);
            }
        }
    }

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        c = new CommandCall(systemObject_);
    }

    /**
     Verify null constructor.
     **/
    public void Var001()
    {
        try
        {
            JobList j = new JobList();
            assertCondition(true, j.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify 1-parm ctor.
     **/
    public void Var002()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            assertCondition(true, j.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getName() before it has been set.
     **/
    public void Var003()
    {
        try
        {
            JobList j = new JobList();
            assertCondition(j.getName().equals("*ALL"), "Job name: " + j.getName());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getName() after it has been set via setName.
     **/
    public void Var004()
    {
        try
        {
            JobList j = new JobList();
            String n = "NAME";
            j.setName(n);
            assertCondition(j.getName().equals(n), "Job name: " + j.getName());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getName() after a call to getJobs().
     **/
    public void Var005()
    {
        try
        {
            JobList j = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            j.getJobs();
            if (!j.getName().equals("*ALL"))
            {
                failed("Job name: " + j.getName());
            }
            else
            {
                j.setName("BLAH");
                j.getJobs();
                assertCondition(j.getName().equals("BLAH"), "Job name: " + j.getName());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getNumber() before it has been set.
     **/
    public void Var006()
    {
        try
        {
            JobList j = new JobList();
            assertCondition(j.getNumber().equals("*ALL"), "Job number: " + j.getNumber());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getNumber() after it has been set via setNumber.
     **/
    public void Var007()
    {
        try
        {
            JobList j = new JobList();
            String n = "123456";
            j.setNumber(n);
            assertCondition(j.getNumber().equals(n), "Job number: " + j.getNumber());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getNumber() after a call to getJobs().
     **/
    public void Var008()
    {
        try
        {
            JobList j = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            j.getJobs();
            if (!j.getNumber().equals("*ALL"))
            {
                failed("Job number: " + j.getNumber());
            }
            else
            {
                j.setNumber("12345");
                j.getJobs();
                assertCondition(j.getNumber().equals("12345"), "Job number: " + j.getNumber());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getLength() prior to calling getJobs().
     **/
    public void Var009()
    {
        try
        {
            JobList j = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            // getLength() does an open now and gets initial block of jobs. 
            assertCondition(j.getLength() > 0, "length: " + String.valueOf(j.getLength()));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getLength() after calling getJobs().
     **/
    public void Var010()
    {
        try
        {
            JobList j = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            j.getJobs();
            assertCondition(j.getLength() > 0, "length: " + String.valueOf(j.getLength()));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getSystem() when the system has not been set.
     **/
    public void Var011()
    {
        try
        {
            JobList j = new JobList();
            assertCondition(j.getSystem() == null, "System object not null.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getSystem() when the system has been set via the constructor.
     **/
    public void Var012()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            assertCondition(j.getSystem() == systemObject_, "System object.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getSystem() when the system has been set via setSystem().
     **/
    public void Var013()
    {
        try
        {
            JobList j = new JobList();
            j.setSystem(systemObject_);
            assertCondition(j.getSystem() == systemObject_, "System object");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getUser() before it has been set.
     **/
    public void Var014()
    {
        try
        {
            JobList j = new JobList();
            assertCondition(j.getUser().equals("*ALL"), "user name: " + j.getUser());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getUser() after it has been set via setUser().
     **/
    public void Var015()
    {
        try
        {
            JobList j = new JobList();
            String u = "BLAH";
            j.setUser(u);
            assertCondition(j.getUser().equals(u), "User name: " + j.getUser());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getUser() after a call to getJobs().
     **/
    public void Var016()
    {
        try
        {
            JobList j = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            j.getJobs();
            if (!j.getUser().equals("*ALL"))
            {
                failed("user name: " + j.getUser());
            }
            else
            {
                String u = "BLAH";
                j.setUser(u);
                j.getJobs();
                assertCondition(j.getUser().equals(u), "User name: " + j.getUser());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() when no system object has been set.
     **/
    public void Var017()
    {
        try
        {
            JobList j = new JobList();

            j.getJobs();
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
        }
    }

    /**
     Verify getJobs() when no job name, number or user name have been set.
     **/
    public void Var018()
    {
        try
        {
            JobList j = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            Enumeration e = j.getJobs();
            assertCondition(e.hasMoreElements());
            /*
             if (!retrieveJobs())
             {
             failed();
             return;
             }
             assertCondition(verifyJobs(j));
             */
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() when only the job name has been set explicitly and job match exists on the server.
     **/
    public void Var019()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            JobList j2 = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            Enumeration jobs = j.getJobs();
            if (jobs.hasMoreElements())
            {
                String name = ((Job)jobs.nextElement()).getName();
                j2.setName(name);
                Enumeration jobs2 = j2.getJobs();
                while (jobs2.hasMoreElements())
                {
                    if (!((Job)jobs2.nextElement()).getName().equals(name))
                    {
                        failed("Wrong job name.");
                        return;
                    }
                }
                succeeded();
            }
            else
            {
                failed("Setup failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() when only the job number has been set explicitly and job match exists on the server.
     **/
    public void Var020()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            JobList j2 = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            Enumeration jobs = j.getJobs();
            if (jobs.hasMoreElements())
            {
                String num = ((Job)jobs.nextElement()).getNumber();
                j2.setNumber(num);
                Enumeration jobs2 = j2.getJobs();
                if (jobs2.hasMoreElements())
                {
                    if (((Job)jobs2.nextElement()).getNumber().equals(num))
                    {
                        assertCondition(!jobs2.hasMoreElements());
                    }
                    else
                    {
                        failed("Wrong job number.");
                    }
                }
                else
                {
                    failed("No jobs matching job num " + num);
                }
            }
            else
            {
                failed("Setup failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() when only the user name has been set explicitly and job match exists on the server.
     **/
    public void Var021()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            JobList j2 = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            Enumeration jobs = j.getJobs();
            if (jobs.hasMoreElements())
            {
                String user = ((Job)jobs.nextElement()).getUser();
                j2.setUser(user);
                Enumeration jobs2 = j2.getJobs();
                while (jobs2.hasMoreElements())
                {
                    if (!((Job)jobs2.nextElement()).getUser().equals(user))
                    {
                        failed("Wrong job user.");
                        return;
                    }
                }
                succeeded();
            }
            else
            {
                failed("Setup failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() with job name and user name set and job match exists on the server.
     **/
    public void Var022()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            JobList j2 = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            Enumeration jobs = j.getJobs();
            if (jobs.hasMoreElements())
            {
                Job aJob = (Job)jobs.nextElement();
                String user = aJob.getUser();
                String name = aJob.getName();
                j2.setUser(user);
                j2.setName(name);
                Enumeration jobs2 = j2.getJobs();
                if (jobs2.hasMoreElements())
                {
                    Job jb = (Job)jobs2.nextElement();
                    if (jb.getName().equals(name) &&
                        jb.getUser().equals(user))
                    {
                        succeeded();
                    }
                    else
                    {
                        failed("Wrong job.");
                    }
                }
                else
                {
                    failed("No jobs matching criteria.");
                }
            }
            else
            {
                failed("Setup failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() with job name and job number set and job match exists on the server.
     **/
    public void Var023()
    {
        try
        {
            JobList j = new JobList(systemObject_);

            JobList j2 = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            Enumeration jobs = j.getJobs();
            if (jobs.hasMoreElements())
            {
                Job aJob = (Job)jobs.nextElement();
                String num = aJob.getNumber();
                String name = aJob.getName();
                j2.setNumber(num);
                j2.setName(name);
                Enumeration jobs2 = j2.getJobs();
                if (jobs2.hasMoreElements())
                {
                    Job jb = (Job)jobs2.nextElement();
                    if (jb.getName().equals(name) &&
                        jb.getNumber().equals(num))
                    {
                        assertCondition(!jobs2.hasMoreElements());
                    }
                    else
                    {
                        failed("Wrong job.");
                    }
                }
                else
                {
                    output_.println("Seeking job number " + num + ", job name " + name + ".");
                    failed("No jobs matching criteria.");
                }
            }
            else
            {
                failed("Setup failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() with user name and job number set and job match exists on the server.
     **/
    public void Var024()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            JobList j2 = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            Enumeration jobs = j.getJobs();
            if (jobs.hasMoreElements())
            {
                Job aJob = (Job)jobs.nextElement();
                String num = aJob.getNumber();
                String user = aJob.getUser();
                j2.setNumber(num);
                j2.setUser(user);
                Enumeration jobs2 = j2.getJobs();
                if (jobs2.hasMoreElements())
                {
                    Job jb = (Job)jobs2.nextElement();
                    if (jb.getUser().equals(user) && jb.getNumber().equals(num))
                    {
                        assertCondition(!jobs2.hasMoreElements());
                    }
                    else
                    {
                        failed("Wrong job.");
                    }
                }
                else
                {
                    failed("No jobs matching criteria.");
                }
            }
            else
            {
                failed("Setup failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() with job name, user name and job number set and job match exists on the server.
     **/
    public void Var025()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            JobList j2 = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            Enumeration jobs = j.getJobs();
            if (jobs.hasMoreElements())
            {
                Job aJob = (Job)jobs.nextElement();
                String num = aJob.getNumber();
                String user = aJob.getUser();
                String name = aJob.getName();
                j2.setNumber(num);
                j2.setUser(user);
                j2.setName(name);
                Enumeration jobs2 = j2.getJobs();
                if (jobs2.hasMoreElements())
                {
                    Job jb = (Job)jobs2.nextElement();
                    if (jb.getUser().equals(user) && jb.getNumber().equals(num) && jb.getName().equals(name))
                    {
                        assertCondition(!jobs2.hasMoreElements());
                    }
                    else
                    {
                        failed("Wrong job.");
                    }
                }
                else
                {
                    failed("No jobs matching criteria.");
                }
            }
            else
            {
                failed("Setup failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() when no matching jobs are found.
     **/
    public void Var026()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            j.setUser("NOEXIST");
            Enumeration jobs = j.getJobs();
            assertCondition(!jobs.hasMoreElements(), "Unexpected Jobs Returned");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() when an invalid system has been specified.
     **/
    public void Var027()
    {
        try
        {
            AS400 system = new AS400("NOEXIST", "BLAH", "BLAH");
            system.setGuiAvailable(false);
            JobList j = new JobList(system);
            j.getJobs();
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.net.UnknownHostException");
        }
    }

    /**
     Verify setName() passing null.
     **/
    public void Var028()
    {
        try
        {
            JobList j = new JobList();
            j.setName(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "name");
        }
    }

    /**
     Verify setName() passing a non-null string.
     **/
    public void Var029()
    {
        try
        {
            JobList j = new JobList();
            j.setName("NAME");
            assertCondition(j.getName().equals("NAME"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify setNumber() passing null.
     **/
    public void Var030()
    {
        try
        {
            JobList j = new JobList();
            j.setNumber(null);
            failed("No Exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "number");
        }
    }

    /**
     Verify setNumber() passing a non-null string.
     **/
    public void Var031()
    {
        try
        {
            JobList j = new JobList();
            j.setNumber("12345");
            assertCondition(j.getNumber().equals("12345"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify setSystem() passing null.
     **/
    public void Var032()
    {
        try
        {
            JobList j = new JobList();
            j.setSystem(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     Verify setSystem() passing a valid system object.
     **/
    public void Var033()
    {
        try
        {
            JobList j = new JobList();
            j.setSystem(systemObject_);
            assertCondition(j.getSystem() == systemObject_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify setUser() passing null.
     **/
    public void Var034()
    {
        try
        {
            JobList j = new JobList();
            j.setUser(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "user");
        }
    }

    /**
     Verify setUser() passing a non-null string.
     **/
    public void Var035()
    {
        try
        {
            JobList j = new JobList();
            j.setUser("USER");
            assertCondition(j.getUser().equals("USER"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() when the job name is lower case.
     **/
    public void Var036()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            JobList j2 = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

            Enumeration jobs = j.getJobs();

            if (jobs.hasMoreElements())
            {
                String name = ((Job)jobs.nextElement()).getName().toLowerCase();
                j2.setName(name);
                Enumeration jobs2 = j2.getJobs();

                if (! jobs2.hasMoreElements())
                {
                    failed("No jobs returned.");
                }
                else
                {
                    while (jobs2.hasMoreElements())
                    {
                        if (!((Job)jobs2.nextElement()).getName().equalsIgnoreCase(name))
                        {
                            failed("Wrong job name.");
                            return;
                        }
                    }
                    succeeded();
                }
            }
            else
            {
                failed("Setup failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify getJobs() when the user name is lower case.
     **/
    public void Var037()
    {
        try
        {
            JobList j = new JobList(systemObject_);
            JobList j2 = new JobList(systemObject_);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

	    j2.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j2.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j2.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);


            Enumeration jobs = j.getJobs();

            if (jobs.hasMoreElements())
            {
                String user = ((Job)jobs.nextElement()).getUser().toLowerCase();

                while (user.startsWith("qsys"))
                {
                    user = ((Job)jobs.nextElement()).getUser().toLowerCase();
                }

                j2.setUser(user);
                Enumeration jobs2 = j2.getJobs();

                if (! jobs2.hasMoreElements())
                {
                    failed("No jobs returned.");
                }
                else
                {
                    while (jobs2.hasMoreElements())
                    {
                        if (!((Job)jobs2.nextElement()).getUser().equalsIgnoreCase(user))
                        {
                            failed("Wrong job user.");
                            return;
                        }
                    }
                    succeeded();
                }
            }
            else
            {
                failed("Setup failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    



    /**
       Verify addJobAttributeToRetrieve(). Possible values are all job attributes contained in 
       the Job class, excluding some listed in the Javadoc. Variable 1/4.
       **/

       public void Var038()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
       		j.addJobAttributeToRetrieve(Job.ACCOUNTING_CODE);
       		j.addJobAttributeToRetrieve(Job.ACTIVE_JOB_STATUS);
       		j.addJobAttributeToRetrieve(Job.ACTIVE_JOB_STATUS_FOR_JOBS_ENDING);
       		j.addJobAttributeToRetrieve(Job.ALLOW_MULTIPLE_THREADS);
       		j.addJobAttributeToRetrieve(Job.AUXILIARY_IO_REQUESTS);
       		j.addJobAttributeToRetrieve(Job.AUXILIARY_IO_REQUESTS_LARGE);
       		j.addJobAttributeToRetrieve(Job.BREAK_MESSAGE_HANDLING);
       		j.addJobAttributeToRetrieve(Job.CCSID);
       		j.addJobAttributeToRetrieve(Job.CHARACTER_ID_CONTROL);
       		j.addJobAttributeToRetrieve(Job.COMPLETION_STATUS);
       		j.addJobAttributeToRetrieve(Job.CONTROLLED_END_REQUESTED);
       		j.addJobAttributeToRetrieve(Job.COUNTRY_ID);
       		j.addJobAttributeToRetrieve(Job.CPU_TIME_USED);
       		j.addJobAttributeToRetrieve(Job.CPU_TIME_USED_FOR_DATABASE);
       		j.addJobAttributeToRetrieve(Job.CPU_TIME_USED_LARGE);
       		j.addJobAttributeToRetrieve(Job.CURRENT_SYSTEM_POOL_ID);
       		j.addJobAttributeToRetrieve(Job.CURRENT_USER);
       		j.addJobAttributeToRetrieve(Job.DATE_ENDED);
       		j.addJobAttributeToRetrieve(Job.DATE_ENTERED_SYSTEM);
       		j.addJobAttributeToRetrieve(Job.DATE_FORMAT);
       		j.addJobAttributeToRetrieve(Job.DATE_SEPARATOR);
       		j.addJobAttributeToRetrieve(Job.DATE_STARTED);
       		j.addJobAttributeToRetrieve(Job.DBCS_CAPABLE);
       		j.addJobAttributeToRetrieve(Job.DECIMAL_FORMAT);
       		j.addJobAttributeToRetrieve(Job.DEFAULT_CCSID);
       		j.addJobAttributeToRetrieve(Job.DEFAULT_WAIT_TIME);
    		
       			succeeded();	
       
       			j.clearJobAttributesToRetrieve();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using addJobAttributeToRetrieve().");
       	}
       	
       }

       /**
       Verify addJobAttributeToRetrieve(). Possible values are all job attributes contained in 
       the Job class, excluding some listed in the Javadoc. Variable 2/4.
       **/

       public void Var039()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);			   
       		
       		j.addJobAttributeToRetrieve(Job.DEVICE_RECOVERY_ACTION);
       		j.addJobAttributeToRetrieve(Job.ELIGIBLE_FOR_PURGE);
       		j.addJobAttributeToRetrieve(Job.END_SEVERITY);
       		j.addJobAttributeToRetrieve(Job.FUNCTION_NAME);
       		j.addJobAttributeToRetrieve(Job.FUNCTION_TYPE);
       		j.addJobAttributeToRetrieve(Job.INQUIRY_MESSAGE_REPLY);
       		j.addJobAttributeToRetrieve(Job.INSTANCE);
       		j.addJobAttributeToRetrieve(Job.INTERACTIVE_TRANSACTIONS);
       		j.addJobAttributeToRetrieve(Job.INTERNAL_JOB_IDENTIFIER);
       		j.addJobAttributeToRetrieve(Job.JOB_DATE);
       		j.addJobAttributeToRetrieve(Job.JOB_DESCRIPTION);
       		j.addJobAttributeToRetrieve(Job.JOB_END_REASON);
       		j.addJobAttributeToRetrieve(Job.JOB_LOG_OUTPUT);
       		j.addJobAttributeToRetrieve(Job.JOB_LOG_PENDING);
       		j.addJobAttributeToRetrieve(Job.JOB_NAME);
       		j.addJobAttributeToRetrieve(Job.JOB_NUMBER);
       		j.addJobAttributeToRetrieve(Job.JOB_QUEUE);
       		j.addJobAttributeToRetrieve(Job.JOB_QUEUE_DATE);
       		j.addJobAttributeToRetrieve(Job.JOB_QUEUE_PRIORITY);
       		j.addJobAttributeToRetrieve(Job.JOB_QUEUE_STATUS);
       		j.addJobAttributeToRetrieve(Job.JOB_STATUS);
       		j.addJobAttributeToRetrieve(Job.JOB_SUBTYPE);
       		j.addJobAttributeToRetrieve(Job.JOB_SWITCHES);
       		j.addJobAttributeToRetrieve(Job.JOB_TYPE);
       		j.addJobAttributeToRetrieve(Job.JOB_TYPE_ENHANCED);
       		
       			succeeded();	
       
       			j.clearJobAttributesToRetrieve();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using addJobAttributeToRetrieve().");
       	}
       	
       }
       
       /**
       Verify addJobAttributeToRetrieve(). Possible values are all job attributes contained in 
       the Job class, excluding some listed in the Javadoc. Variable 3/4.
       **/

       public void Var040()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);			   
       		
       		j.addJobAttributeToRetrieve(Job.JOB_USER_IDENTITY);
       		j.addJobAttributeToRetrieve(Job.JOB_USER_IDENTITY_SETTING);
       		j.addJobAttributeToRetrieve(Job.KEEP_DDM_CONNECTIONS_ACTIVE);
       		j.addJobAttributeToRetrieve(Job.LOCATION_NAME);
       		j.addJobAttributeToRetrieve(Job.LANGUAGE_ID);
       		j.addJobAttributeToRetrieve(Job.LOG_CL_PROGRAMS);
       		j.addJobAttributeToRetrieve(Job.LOGGING_LEVEL);
       		j.addJobAttributeToRetrieve(Job.LOGGING_SEVERITY);
       		j.addJobAttributeToRetrieve(Job.LOGGING_TEXT);
       		j.addJobAttributeToRetrieve(Job.MAX_CPU_TIME);
       		j.addJobAttributeToRetrieve(Job.MAX_TEMP_STORAGE);
       		j.addJobAttributeToRetrieve(Job.MAX_TEMP_STORAGE_LARGE);
       		j.addJobAttributeToRetrieve(Job.MAX_THREADS);
       		j.addJobAttributeToRetrieve(Job.MEMORY_POOL);
       		j.addJobAttributeToRetrieve(Job.MESSAGE_QUEUE_ACTION);
       		j.addJobAttributeToRetrieve(Job.MESSAGE_QUEUE_MAX_SIZE);
       		j.addJobAttributeToRetrieve(Job.MESSAGE_REPLY);
       		j.addJobAttributeToRetrieve(Job.MODE);
       		j.addJobAttributeToRetrieve(Job.NETWORK_ID);
       		j.addJobAttributeToRetrieve(Job.OUTPUT_QUEUE);
       		j.addJobAttributeToRetrieve(Job.OUTPUT_QUEUE_PRIORITY);
       		j.addJobAttributeToRetrieve(Job.PRINT_KEY_FORMAT);
       		j.addJobAttributeToRetrieve(Job.PRINT_TEXT);
       		j.addJobAttributeToRetrieve(Job.PRINTER_DEVICE_NAME);
       
       		
       			succeeded();	
       
       			j.clearJobAttributesToRetrieve();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using addJobAttributeToRetrieve().");
       	}
       	
       }
       
       /**
       Verify addJobAttributeToRetrieve(). Possible values are all job attributes contained in 
       the Job class, excluding some listed in the Javadoc. Variable 4/4.
       **/

       public void Var041()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);			   
       		
       		j.addJobAttributeToRetrieve(Job.PRODUCT_RETURN_CODE);
       		j.addJobAttributeToRetrieve(Job.ROUTING_DATA);
       		j.addJobAttributeToRetrieve(Job.RUN_PRIORITY);
       		j.addJobAttributeToRetrieve(Job.SCHEDULE_DATE);
       		j.addJobAttributeToRetrieve(Job.SCHEDULE_TIME);
       		j.addJobAttributeToRetrieve(Job.SEQUENCE_NUMBER);
       		j.addJobAttributeToRetrieve(Job.SERVER_TYPE);
       		j.addJobAttributeToRetrieve(Job.SIGNED_ON_JOB);
       		j.addJobAttributeToRetrieve(Job.SORT_SEQUENCE_TABLE);
       		j.addJobAttributeToRetrieve(Job.SPECIAL_ENVIRONMENT);
       		j.addJobAttributeToRetrieve(Job.SPOOLED_FILE_ACTION);
       		j.addJobAttributeToRetrieve(Job.STATUS_MESSAGE_HANDLING);
       		j.addJobAttributeToRetrieve(Job.SUBMITTED_BY_JOB_NAME);
       		j.addJobAttributeToRetrieve(Job.SUBSYSTEM);
       		j.addJobAttributeToRetrieve(Job.SYSTEM_POOL_ID);
       		j.addJobAttributeToRetrieve(Job.TEMP_STORAGE_USED);
       		j.addJobAttributeToRetrieve(Job.THREAD_COUNT);
       		j.addJobAttributeToRetrieve(Job.TIME_SEPARATOR);
       		j.addJobAttributeToRetrieve(Job.TIME_SLICE);
       		j.addJobAttributeToRetrieve(Job.TIME_SLICE_END_POOL);
       		j.addJobAttributeToRetrieve(Job.TOTAL_RESPONSE_TIME);
       		j.addJobAttributeToRetrieve(Job.UNIT_OF_WORK_ID);
       		j.addJobAttributeToRetrieve(Job.USER_NAME);
       		j.addJobAttributeToRetrieve(Job.USER_RETURN_CODE );
       		
      	
       			succeeded();	
       
       			j.clearJobAttributesToRetrieve();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using addJobAttributeToRetrieve().");
       	}
       	
       }
       
       /**
       Verify addJobAttributeToRetrieve() using one of the values that is not permitted.
       **/
       
       public void Var042()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);			   
       		
       		j.addJobAttributeToRetrieve(Job.CURRENT_LIBRARY);

       		j.clearJobAttributesToRetrieve();
       		
       	}
       	catch(Exception e)
       	{
       		 assertExceptionIs(e, "ExtendedIllegalArgumentException", "attribute: Parameter value is not valid.");
       	}	 	
       }
       
       /**
       Verify addJobAttributeToSortOn() using sortOrder 'true' to sort ascending. Possible values 
       are all job attributes contained in the Job class, excluding some listed in the Javadoc. 
       Variable 1/4.
       **/

       public void Var043()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);		
       		

       		j.addJobAttributeToSortOn(Job.ACCOUNTING_CODE,true);
       		j.addJobAttributeToSortOn(Job.ACTIVE_JOB_STATUS,true);
       		j.addJobAttributeToSortOn(Job.ACTIVE_JOB_STATUS_FOR_JOBS_ENDING,true);
       		j.addJobAttributeToSortOn(Job.ALLOW_MULTIPLE_THREADS,true);
       		j.addJobAttributeToSortOn(Job.AUXILIARY_IO_REQUESTS,true);
       		j.addJobAttributeToSortOn(Job.AUXILIARY_IO_REQUESTS_LARGE,true);
       		j.addJobAttributeToSortOn(Job.BREAK_MESSAGE_HANDLING,true);
       		j.addJobAttributeToSortOn(Job.CCSID,true);
       		j.addJobAttributeToSortOn(Job.CHARACTER_ID_CONTROL,true);
       		j.addJobAttributeToSortOn(Job.COMPLETION_STATUS,true);
       		j.addJobAttributeToSortOn(Job.CONTROLLED_END_REQUESTED,true);
       		j.addJobAttributeToSortOn(Job.COUNTRY_ID,true);
       		j.addJobAttributeToSortOn(Job.CPU_TIME_USED,true);
       		j.addJobAttributeToSortOn(Job.CPU_TIME_USED_FOR_DATABASE,true);
       		j.addJobAttributeToSortOn(Job.CPU_TIME_USED_LARGE,true);
       		j.addJobAttributeToSortOn(Job.CURRENT_SYSTEM_POOL_ID,true);
       		j.addJobAttributeToSortOn(Job.CURRENT_USER,true);
       		j.addJobAttributeToSortOn(Job.DATE_ENDED,true);
       		j.addJobAttributeToSortOn(Job.DATE_ENTERED_SYSTEM,true);
       		j.addJobAttributeToSortOn(Job.DATE_FORMAT,true);
       		j.addJobAttributeToSortOn(Job.DATE_SEPARATOR,true);
       		j.addJobAttributeToSortOn(Job.DATE_STARTED,true);
       		j.addJobAttributeToSortOn(Job.DBCS_CAPABLE,true);
       		j.addJobAttributeToSortOn(Job.DECIMAL_FORMAT,true);
       		j.addJobAttributeToSortOn(Job.DEFAULT_CCSID,true);
       		j.addJobAttributeToSortOn(Job.DEFAULT_WAIT_TIME,true);
      	
       			succeeded();	
       
       			j.clearJobAttributesToSortOn();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using clearJobAttributesToSortOn().");
       	}
       	
       }
       
       /**
       Verify addJobAttributeToSortOn() using sortOrder 'true' to sort ascending. Possible values
       are all job attributes contained in the Job class, excluding some listed in the Javadoc. 
       Variable 2/4.
       **/

       public void Var044()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);		
       		

       		j.addJobAttributeToSortOn(Job.DEVICE_RECOVERY_ACTION,true);
       		j.addJobAttributeToSortOn(Job.ELIGIBLE_FOR_PURGE,true);
       		j.addJobAttributeToSortOn(Job.END_SEVERITY,true);
       		j.addJobAttributeToSortOn(Job.FUNCTION_NAME,true);
       		j.addJobAttributeToSortOn(Job.FUNCTION_TYPE,true);
       		j.addJobAttributeToSortOn(Job.INQUIRY_MESSAGE_REPLY,true);
       		j.addJobAttributeToSortOn(Job.INSTANCE,true);
       		j.addJobAttributeToSortOn(Job.INTERACTIVE_TRANSACTIONS,true);
       		j.addJobAttributeToSortOn(Job.INTERNAL_JOB_IDENTIFIER,true);
       		j.addJobAttributeToSortOn(Job.JOB_DATE,true);
       		j.addJobAttributeToSortOn(Job.JOB_DESCRIPTION,true);
       		j.addJobAttributeToSortOn(Job.JOB_END_REASON,true);
       		j.addJobAttributeToSortOn(Job.JOB_LOG_OUTPUT,true);
       		j.addJobAttributeToSortOn(Job.JOB_LOG_PENDING,true);
       		j.addJobAttributeToSortOn(Job.JOB_NAME,true);
       		j.addJobAttributeToSortOn(Job.JOB_NUMBER,true);
       		j.addJobAttributeToSortOn(Job.JOB_QUEUE,true);
       		j.addJobAttributeToSortOn(Job.JOB_QUEUE_DATE,true);
       		j.addJobAttributeToSortOn(Job.JOB_QUEUE_PRIORITY,true);
       		j.addJobAttributeToSortOn(Job.JOB_QUEUE_STATUS,true);
       		j.addJobAttributeToSortOn(Job.JOB_STATUS,true);
       		j.addJobAttributeToSortOn(Job.JOB_SUBTYPE,true);
       		j.addJobAttributeToSortOn(Job.JOB_SWITCHES,true);
       		j.addJobAttributeToSortOn(Job.JOB_TYPE,true);
       		j.addJobAttributeToSortOn(Job.JOB_TYPE_ENHANCED,true);
      	
       			succeeded();	
       
       			j.clearJobAttributesToSortOn();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using clearJobAttributesToSortOn().");
       	}
       	
       }
       
       /**
       Verify addJobAttributeToSortOn() using sortOrder 'true' to sort ascending. Possible values 
       are all job attributes contained in the Job class, excluding some listed in the Javadoc. 
       Variable 3/4.
       **/

       public void Var045()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);		
       		
          		j.addJobAttributeToSortOn(Job.JOB_USER_IDENTITY,true);
       		j.addJobAttributeToSortOn(Job.JOB_USER_IDENTITY_SETTING,true);
       		j.addJobAttributeToSortOn(Job.KEEP_DDM_CONNECTIONS_ACTIVE,true);
       		j.addJobAttributeToSortOn(Job.LOCATION_NAME,true);
       		j.addJobAttributeToSortOn(Job.LANGUAGE_ID,true);
       		j.addJobAttributeToSortOn(Job.LOG_CL_PROGRAMS,true);
       		j.addJobAttributeToSortOn(Job.LOGGING_LEVEL,true);
       		j.addJobAttributeToSortOn(Job.LOGGING_SEVERITY,true);
       		j.addJobAttributeToSortOn(Job.LOGGING_TEXT,true);
       		j.addJobAttributeToSortOn(Job.MAX_CPU_TIME,true);
       		j.addJobAttributeToSortOn(Job.MAX_TEMP_STORAGE,true);
       		j.addJobAttributeToSortOn(Job.MAX_TEMP_STORAGE_LARGE,true);
       		j.addJobAttributeToSortOn(Job.MAX_THREADS,true);
       		j.addJobAttributeToSortOn(Job.MEMORY_POOL,true);
       		j.addJobAttributeToSortOn(Job.MESSAGE_QUEUE_ACTION,true);
       		j.addJobAttributeToSortOn(Job.MESSAGE_QUEUE_MAX_SIZE,true);
       		j.addJobAttributeToSortOn(Job.MESSAGE_REPLY,true);
       		j.addJobAttributeToSortOn(Job.MODE,true);
       		j.addJobAttributeToSortOn(Job.NETWORK_ID,true);
       		j.addJobAttributeToSortOn(Job.OUTPUT_QUEUE,true);
       		j.addJobAttributeToSortOn(Job.OUTPUT_QUEUE_PRIORITY,true);
       		j.addJobAttributeToSortOn(Job.PRINT_KEY_FORMAT,true);
       		j.addJobAttributeToSortOn(Job.PRINT_TEXT,true);
       		j.addJobAttributeToSortOn(Job.PRINTER_DEVICE_NAME,true);
      
      	
       			succeeded();	
       
       			j.clearJobAttributesToRetrieve();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using clearJobAttributesToSortOn().");
       	}
       	
       }
        
       /**
       Verify addJobAttributeToSortOn() using sortOrder 'true' to sort ascending. Possible values 
       are all job attributes contained in the Job class, excluding some listed in the Javadoc. 
       Variable 4/4.
       **/

       public void Var046()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);		
       		
       		j.addJobAttributeToSortOn(Job.PRODUCT_RETURN_CODE,true);
       		j.addJobAttributeToSortOn(Job.ROUTING_DATA,true);
       		j.addJobAttributeToSortOn(Job.RUN_PRIORITY,true);
       		j.addJobAttributeToSortOn(Job.SCHEDULE_DATE,true);
       		j.addJobAttributeToSortOn(Job.SCHEDULE_TIME,true);
       		j.addJobAttributeToSortOn(Job.SEQUENCE_NUMBER,true);
       		j.addJobAttributeToSortOn(Job.SERVER_TYPE,true);
       		j.addJobAttributeToSortOn(Job.SIGNED_ON_JOB,true);
       		j.addJobAttributeToSortOn(Job.SORT_SEQUENCE_TABLE,true);
       		j.addJobAttributeToSortOn(Job.SPECIAL_ENVIRONMENT,true);
       		j.addJobAttributeToSortOn(Job.SPOOLED_FILE_ACTION,true);
       		j.addJobAttributeToSortOn(Job.STATUS_MESSAGE_HANDLING,true);
       		j.addJobAttributeToSortOn(Job.SUBMITTED_BY_JOB_NAME,true);
       		j.addJobAttributeToSortOn(Job.SUBSYSTEM,true);
       		j.addJobAttributeToSortOn(Job.SYSTEM_POOL_ID,true);
       		j.addJobAttributeToSortOn(Job.TEMP_STORAGE_USED,true);
       		j.addJobAttributeToSortOn(Job.THREAD_COUNT,true);
       		j.addJobAttributeToSortOn(Job.TIME_SEPARATOR,true);
       		j.addJobAttributeToSortOn(Job.TIME_SLICE,true);
       		j.addJobAttributeToSortOn(Job.TIME_SLICE_END_POOL,true);
       		j.addJobAttributeToSortOn(Job.TOTAL_RESPONSE_TIME,true);
       		j.addJobAttributeToSortOn(Job.UNIT_OF_WORK_ID,true);
       		j.addJobAttributeToSortOn(Job.USER_NAME,true);
       		j.addJobAttributeToSortOn(Job.USER_RETURN_CODE,true);
      	
       			succeeded();	
       
       			j.clearJobAttributesToSortOn();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using clearJobAttributesToSortOn().");
       	}
       	
       }
        
       /**
       Verify addJobAttributeToSortOn() using sortOrder 'false' to sort ascending. Possible values 
       are all job attributes contained in the Job class, excluding some listed in the Javadoc. Variable 1/4.
       **/

       public void Var047()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);		
       		

       		j.addJobAttributeToSortOn(Job.ACCOUNTING_CODE,false);
       		j.addJobAttributeToSortOn(Job.ACTIVE_JOB_STATUS,false);
       		j.addJobAttributeToSortOn(Job.ACTIVE_JOB_STATUS_FOR_JOBS_ENDING,false);
       		j.addJobAttributeToSortOn(Job.ALLOW_MULTIPLE_THREADS,false);
       		j.addJobAttributeToSortOn(Job.AUXILIARY_IO_REQUESTS,false);
       		j.addJobAttributeToSortOn(Job.AUXILIARY_IO_REQUESTS_LARGE,false);
       		j.addJobAttributeToSortOn(Job.BREAK_MESSAGE_HANDLING,false);
       		j.addJobAttributeToSortOn(Job.CCSID,false);
       		j.addJobAttributeToSortOn(Job.CHARACTER_ID_CONTROL,false);
       		j.addJobAttributeToSortOn(Job.COMPLETION_STATUS,false);
       		j.addJobAttributeToSortOn(Job.CONTROLLED_END_REQUESTED,false);
       		j.addJobAttributeToSortOn(Job.COUNTRY_ID,false);
       		j.addJobAttributeToSortOn(Job.CPU_TIME_USED,false);
       		j.addJobAttributeToSortOn(Job.CPU_TIME_USED_FOR_DATABASE,false);
       		j.addJobAttributeToSortOn(Job.CPU_TIME_USED_LARGE,false);
       		j.addJobAttributeToSortOn(Job.CURRENT_SYSTEM_POOL_ID,false);
       		j.addJobAttributeToSortOn(Job.CURRENT_USER,false);
       		j.addJobAttributeToSortOn(Job.DATE_ENDED,false);
       		j.addJobAttributeToSortOn(Job.DATE_ENTERED_SYSTEM,false);
       		j.addJobAttributeToSortOn(Job.DATE_FORMAT,false);
       		j.addJobAttributeToSortOn(Job.DATE_SEPARATOR,false);
       		j.addJobAttributeToSortOn(Job.DATE_STARTED,false);
       		j.addJobAttributeToSortOn(Job.DBCS_CAPABLE,false);
       		j.addJobAttributeToSortOn(Job.DECIMAL_FORMAT,false);
       		j.addJobAttributeToSortOn(Job.DEFAULT_CCSID,false);
       		j.addJobAttributeToSortOn(Job.DEFAULT_WAIT_TIME,false);
      	
       			succeeded();	
       
       			j.clearJobAttributesToSortOn();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using clearJobAttributesToSortOn().");
       	}
       	
       }

     /**
       Verify addJobAttributeToSortOn() using sortOrder 'false' to sort ascending. Possible values 
       are all job attributes contained in the Job class, excluding some listed in the Javadoc. 
       Variable 2/4.
       **/

       public void Var048()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);		
       		

       		j.addJobAttributeToSortOn(Job.DEVICE_RECOVERY_ACTION,false);
       		j.addJobAttributeToSortOn(Job.ELIGIBLE_FOR_PURGE,false);
       		j.addJobAttributeToSortOn(Job.END_SEVERITY,false);
       		j.addJobAttributeToSortOn(Job.FUNCTION_NAME,false);
       		j.addJobAttributeToSortOn(Job.FUNCTION_TYPE,false);
       		j.addJobAttributeToSortOn(Job.INQUIRY_MESSAGE_REPLY,false);
       		j.addJobAttributeToSortOn(Job.INSTANCE,false);
       		j.addJobAttributeToSortOn(Job.INTERACTIVE_TRANSACTIONS,false);
       		j.addJobAttributeToSortOn(Job.INTERNAL_JOB_IDENTIFIER,false);
       		j.addJobAttributeToSortOn(Job.JOB_DATE,false);
       		j.addJobAttributeToSortOn(Job.JOB_DESCRIPTION,false);
       		j.addJobAttributeToSortOn(Job.JOB_END_REASON,false);
       		j.addJobAttributeToSortOn(Job.JOB_LOG_OUTPUT,false);
       		j.addJobAttributeToSortOn(Job.JOB_LOG_PENDING,false);
       		j.addJobAttributeToSortOn(Job.JOB_NAME,false);
       		j.addJobAttributeToSortOn(Job.JOB_NUMBER,false);
       		j.addJobAttributeToSortOn(Job.JOB_QUEUE,false);
       		j.addJobAttributeToSortOn(Job.JOB_QUEUE_DATE,false);
       		j.addJobAttributeToSortOn(Job.JOB_QUEUE_PRIORITY,false);
       		j.addJobAttributeToSortOn(Job.JOB_QUEUE_STATUS,false);
       		j.addJobAttributeToSortOn(Job.JOB_STATUS,false);
       		j.addJobAttributeToSortOn(Job.JOB_SUBTYPE,false);
       		j.addJobAttributeToSortOn(Job.JOB_SWITCHES,false);
       		j.addJobAttributeToSortOn(Job.JOB_TYPE,false);
       		j.addJobAttributeToSortOn(Job.JOB_TYPE_ENHANCED,false);
      	
       			succeeded();	
       
       			j.clearJobAttributesToSortOn();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using clearJobAttributesToSortOn().");
       	}
       	
       }
        
       /**
       Verify addJobAttributeToSortOn() using sortOrder 'false' to sort ascending. Possible values 
       are all job attributes contained in the Job class, excluding some listed in the Javadoc. 
       Variable 3/4.
       **/

       public void Var049()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);		
       		
          		j.addJobAttributeToSortOn(Job.JOB_USER_IDENTITY,false);
       		j.addJobAttributeToSortOn(Job.JOB_USER_IDENTITY_SETTING,false);
       		j.addJobAttributeToSortOn(Job.KEEP_DDM_CONNECTIONS_ACTIVE,false);
       		j.addJobAttributeToSortOn(Job.LOCATION_NAME,false);
       		j.addJobAttributeToSortOn(Job.LANGUAGE_ID,false);
       		j.addJobAttributeToSortOn(Job.LOG_CL_PROGRAMS,false);
       		j.addJobAttributeToSortOn(Job.LOGGING_LEVEL,false);
       		j.addJobAttributeToSortOn(Job.LOGGING_SEVERITY,false);
       		j.addJobAttributeToSortOn(Job.LOGGING_TEXT,false);
       		j.addJobAttributeToSortOn(Job.MAX_CPU_TIME,false);
       		j.addJobAttributeToSortOn(Job.MAX_TEMP_STORAGE,false);
       		j.addJobAttributeToSortOn(Job.MAX_TEMP_STORAGE_LARGE,false);
       		j.addJobAttributeToSortOn(Job.MAX_THREADS,false);
       		j.addJobAttributeToSortOn(Job.MEMORY_POOL,false);
       		j.addJobAttributeToSortOn(Job.MESSAGE_QUEUE_ACTION,false);
       		j.addJobAttributeToSortOn(Job.MESSAGE_QUEUE_MAX_SIZE,false);
       		j.addJobAttributeToSortOn(Job.MESSAGE_REPLY,false);
       		j.addJobAttributeToSortOn(Job.MODE,false);
       		j.addJobAttributeToSortOn(Job.NETWORK_ID,false);
       		j.addJobAttributeToSortOn(Job.OUTPUT_QUEUE,false);
       		j.addJobAttributeToSortOn(Job.OUTPUT_QUEUE_PRIORITY,false);
       		j.addJobAttributeToSortOn(Job.PRINT_KEY_FORMAT,false);
       		j.addJobAttributeToSortOn(Job.PRINT_TEXT,false);
       		j.addJobAttributeToSortOn(Job.PRINTER_DEVICE_NAME,false);
      
      	
       			succeeded();	
       
       			j.clearJobAttributesToRetrieve();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using clearJobAttributesToSortOn().");
       	}
       	
       }
        
       /**
       Verify addJobAttributeToSortOn() using sortOrder 'false' to sort ascending. Possible 
       values are all job attributes contained in the Job class, excluding some listed in the 
       Javadoc. Variable 4/4.
       **/

       public void Var050()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);		
       		
       		j.addJobAttributeToSortOn(Job.PRODUCT_RETURN_CODE,false);
       		j.addJobAttributeToSortOn(Job.ROUTING_DATA,false);
       		j.addJobAttributeToSortOn(Job.RUN_PRIORITY,false);
       		j.addJobAttributeToSortOn(Job.SCHEDULE_DATE,false);
       		j.addJobAttributeToSortOn(Job.SCHEDULE_TIME,false);
       		j.addJobAttributeToSortOn(Job.SEQUENCE_NUMBER,false);
       		j.addJobAttributeToSortOn(Job.SERVER_TYPE,false);
       		j.addJobAttributeToSortOn(Job.SIGNED_ON_JOB,false);
       		j.addJobAttributeToSortOn(Job.SORT_SEQUENCE_TABLE,false);
       		j.addJobAttributeToSortOn(Job.SPECIAL_ENVIRONMENT,false);
       		j.addJobAttributeToSortOn(Job.SPOOLED_FILE_ACTION,false);
       		j.addJobAttributeToSortOn(Job.STATUS_MESSAGE_HANDLING,false);
       		j.addJobAttributeToSortOn(Job.SUBMITTED_BY_JOB_NAME,false);
       		j.addJobAttributeToSortOn(Job.SUBSYSTEM,false);
       		j.addJobAttributeToSortOn(Job.SYSTEM_POOL_ID,false);
       		j.addJobAttributeToSortOn(Job.TEMP_STORAGE_USED,false);
       		j.addJobAttributeToSortOn(Job.THREAD_COUNT,false);
       		j.addJobAttributeToSortOn(Job.TIME_SEPARATOR,false);
       		j.addJobAttributeToSortOn(Job.TIME_SLICE,false);
       		j.addJobAttributeToSortOn(Job.TIME_SLICE_END_POOL,false);
       		j.addJobAttributeToSortOn(Job.TOTAL_RESPONSE_TIME,false);
       		j.addJobAttributeToSortOn(Job.UNIT_OF_WORK_ID,false);
       		j.addJobAttributeToSortOn(Job.USER_NAME,false);
       		j.addJobAttributeToSortOn(Job.USER_RETURN_CODE,false);
      	
       	    succeeded();	
       
       		j.clearJobAttributesToSortOn();
       		
       	}
       	catch(Exception e)
       	{
       		 failed(e, "Unexpected exception using clearJobAttributesToSortOn().");
       	}
       	
       }
         
       /**
       Verify addJobAttributeToSortOn() using some of the values that are not permitted.
       **/
       
       public void Var051()
       {
       	
       	try
       	{
       		JobList j = new JobList(systemObject_);			   
       		
       		j.addJobAttributeToSortOn(Job.CURRENT_LIBRARY_EXISTENCE, true);
       		j.clearJobAttributesToRetrieve();
       		
       	}
       	catch(Exception e)
       	{
       		 assertExceptionIs(e, "ExtendedIllegalArgumentException", "attribute: Parameter value is not valid.");
       	}	
       }
        
       /**
       Verify addJobSelectionCriteria() retrieving a list of only active jobs.
       **/
       
       public void Var052()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		   
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       
		if (j.getLength() == 0)
		    System.out.println("There are no jobs that match this selection. ");

		succeeded();
		j.clearJobSelectionCriteria();
       		     
       		    
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_NAME  with 
       SELECTION_JOB_NAME_ALL.
       **/
       
       public void Var053()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_NAME,JobList.SELECTION_JOB_NAME_ALL);
       		    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	} 
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_NAME with 
       SELECTION_JOB_NAME_CURRENT .
       **/
       
       public void Var054()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_NAME,JobList.SELECTION_JOB_NAME_CURRENT);
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();

       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }

       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_NAME with 
       SELECTION_JOB_NAME_ONLY.
       **/
       
       public void Var055()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		   
       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_NAME,JobList.SELECTION_JOB_NAME_ONLY);	     

			 j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			 j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			 j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       		  
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }

       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_NAME with an 
       specific job name.
       **/
       
       public void Var056()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       		
       			 //We take the first job to make that our specific job name
       		
		String job = (j.getJobs(0,0)).toString();
       			   
		j.addJobSelectionCriteria(JobList.SELECTION_JOB_NAME, job);
       		     
   	    			
		if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

		succeeded();
		j.clearJobSelectionCriteria();
       			 
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_NAME with an 
       specific job name
       that doesn't exists.
       **/
       
       public void Var057()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       			   
       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_NAME, "FakeJob");
			 j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			 j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			 j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			 if (j.getLength() == 0) {
       				System.out.println("There are no jobs that match this selection. ");
				succeeded();
			 } else {
			     failed("FakeJob found"); 
			 } 
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_USER_NAME with 
       SELECTION_USER_NAME_ALL.
       **/
       
       public void Var058()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			   
       			 j.addJobSelectionCriteria(JobList.SELECTION_USER_NAME,JobList.SELECTION_USER_NAME_ALL);
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_USER_NAME with 
       SELECTION_USER_NAME_CURRENT.
       **/
       
       public void Var059()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
   
       			 j.addJobSelectionCriteria(JobList.SELECTION_USER_NAME,JobList.SELECTION_USER_NAME_CURRENT);
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }

       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_USER_NAME with an 
       specific user name.
       **/
       
       public void Var060()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			 j.addJobSelectionCriteria(JobList.SELECTION_USER_NAME,"java");
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       } 

       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_NUMBER with 
       SELECTION_JOB_NUMBER_ALL.
       **/
       
       public void Var061()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);


       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_NUMBER,JobList.SELECTION_JOB_NUMBER_ALL);
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }

       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_NUMBER with an 
       specific job number.
       **/
       
       public void Var062()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

   			 //We take the first job to make that our specific job
       		
   		     String job = (j.getJobs(0,0)).toString();
       			   
       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_NUMBER,job);
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE with 
       SELECTION_JOB_TYPE_ALL.
       **/
       
       public void Var063()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE,JobList.SELECTION_JOB_TYPE_ALL);
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }
    
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE with 
       JOB_TYPE_AUTOSTART.
       **/
       
       public void Var064()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			   
       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE,Job.JOB_TYPE_AUTOSTART);
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}	
       	
       }

       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE with 
       JOB_TYPE_BATCH.
       **/
       
       public void Var065()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);


       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE,Job.JOB_TYPE_BATCH);
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}		
       }

       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE with 
       JOB_TYPE_INTERACTIVE.
       **/
       
       public void Var066()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
    
       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE,Job.JOB_TYPE_INTERACTIVE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");
     		
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE with 
       JOB_TYPE_SUBSYSTEM_MONITOR.
       **/
       
       public void Var067()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       			   
       			 j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE,Job.JOB_TYPE_SUBSYSTEM_MONITOR);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
   	    			
       			 if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			 succeeded();
       			 j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
      
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE with 
       JOB_TYPE_SPOOLED_READER.
       **/
       
       public void Var068()
       {
       	try
       	{
	    JobList j = new JobList(systemObject_);	

	    j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE,Job.JOB_TYPE_SPOOLED_READER);

	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

	    if (j.getLength() == 0)
		System.out.println("There are no jobs that match this selection. ");

	    succeeded();
	    j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE with 
       JOB_TYPE_SYSTEM.
       **/
       
       public void Var069()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
   				j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE,Job.JOB_TYPE_SYSTEM);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       		     		
   				if (j.getLength() == 0)
   					System.out.println("There are no jobs that match this selection. ");

   				succeeded();
   				j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
      
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE with 
       JOB_TYPE_SPOOLED_WRITER.
       **/
       
       public void Var070()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE,Job.JOB_TYPE_SPOOLED_WRITER);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
   	    	
       			if (j.getLength() == 0)
   					System.out.println("There are no jobs that match this selection. ");

   				succeeded();
   				j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE with 
       JOB_TYPE_SCPF_SYSTEM.
       **/
       
       public void Var071()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE,Job.JOB_TYPE_SCPF_SYSTEM);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
   		    	
       			if (j.getLength() == 0)
   					System.out.println("There are no jobs that match this selection. ");

   				succeeded();
   				j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_NONE.
       **/
       
       public void Var072()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       			
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_NONE);
   	    	
       			if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			succeeded();
       			j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_BIN_SYNCH_DEVICE_AND_ACTIVE.
       **/
       
       public void Var073()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_BIN_SYNCH_DEVICE_AND_ACTIVE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

   	    	
		if (j.getLength() == 0)
		    System.out.println("There are no jobs that match this selection. ");
		
		succeeded();
		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_BIN_SYNCH_DEVICE.
       **/
       
       public void Var074()
       {
	   try
	   {
	       JobList j = new JobList(systemObject_);	

			//This value is only used when the value for 
			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	       j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_BIN_SYNCH_DEVICE);
	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);


	       if (j.getLength() == 0)
		   System.out.println("There are no jobs that match this selection. ");

	       succeeded();
	       j.clearJobSelectionCriteria();
	   }
	   catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_COMM_DEVICE_AND_ACTIVE.
       **/
       
       public void Var075()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_COMM_DEVICE_AND_ACTIVE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_COMM_DEVICE.
       **/
       
       public void Var076()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_COMM_DEVICE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_CHECKPOINT.
       **/
       
       public void Var077()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);		
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_CHECKPOINT);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with
       ACTIVE_JOB_STATUS_WAIT_CONDITION.
       **/
       
       public void Var078()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_CONDITION);
   	    	
       			if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			succeeded();
       			j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_CPI_COMM.
       **/
       
       public void Var079()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_CPI_COMM);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_DEQUEUE.
       **/
       
       public void Var080()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_DEQUEUE);
       		         	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_DEQUEUE_AND_ACTIVE.
       **/
       
       public void Var081()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_DEQUEUE_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_DISKETTE_AND_ACTIVE.
       **/
       
       public void Var082()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       		
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_DISKETTE_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_DISKETTE.
       **/
       
       public void Var083()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_DISKETTE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_DELAYED.
       **/
       
       public void Var084()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_DELAYED);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_DISCONNECTED.
       **/
       
       public void Var085()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
   	    		j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_DISCONNECTED);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_DISPLAY_AND_ACTIVE.
       **/
       
       public void Var086()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_DISPLAY_AND_ACTIVE);
       	    	
		if (j.getLength() == 0)
		    System.out.println("There are no jobs that match this selection. ");

		succeeded();
		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_DISPLAY .
       **/
       
       public void Var087()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_DISPLAY);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_ENDED.
       **/
       
       public void Var088()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_ENDED);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_DATABASE_EOF_AND_ACTIVE.
       **/
       
       public void Var089()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_DATABASE_EOF_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_DATABASE_EOF.
       **/
       
       public void Var090()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

       		
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_DATABASE_EOF);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_ENDING.
       **/
       
       public void Var091()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_ENDING);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_EVENT.
       **/
       
       public void Var092()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_EVENT);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_SUSPENDED.
       **/
       
       public void Var093()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_SUSPENDED);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_HELD.
       **/
       
       public void Var094()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_HELD);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
      ACTIVE_JOB_STATUS_HELD_THREAD.
       **/
       
       public void Var095()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_HELD_THREAD);
       			
       			if (j.getLength() == 0)
       				System.out.println("There are no jobs that match this selection. ");

       			succeeded();
       			j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_ICF_FILE_AND_ACTIVE .
       **/
       
       public void Var096()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_ICF_FILE_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_ICF_FILE.
       **/
       
       public void Var097()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_ICF_FILE);	     
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_INELIGIBLE.
       **/
       
       public void Var098()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_INELIGIBLE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_JAVA_AND_ACTIVE.
       **/
       
       public void Var099()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_JAVA_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_JAVA.
       **/
       
       public void Var100()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_JAVA);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_LOCK.
       **/
       
       public void Var101()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_LOCK);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_LOCK_SPACE_AND_ACTIVE.
       **/
       
       public void Var102()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_LOCK_SPACE_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_LOCK_SPACE.
       **/
       
       public void Var103()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 		
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_LOCK_SPACE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_MULTIPLE_FILES_AND_ACTIVE.
       **/
       
       public void Var104()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_MULTIPLE_FILES_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_MULTIPLE_FILES.
       **/
       
       public void Var105()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_MULTIPLE_FILES);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_MESSAGE.
       **/
       
       public void Var106()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_MESSAGE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_MUTEX.
       **/
       
       public void Var107()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_MUTEX);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_MIXED_DEVICE_FILE.
       **/
       
       public void Var108()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_MIXED_DEVICE_FILE);
       		        	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_OPTICAL_DEVICE_AND_ACTIVE.
       **/
       
       public void Var109()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_OPTICAL_DEVICE_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_OPTICAL_DEVICE.
       **/
       
       public void Var110()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_OPTICAL_DEVICE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_OSI.
       **/
       
       public void Var111()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_OSI);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_PRINT_AND_ACTIVE.
       **/
       
       public void Var112()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_PRINT_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_PRINT.
       **/
       
       public void Var113()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       		//This value is only used when the value for 
       		//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       		j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_PRINT);
   	    	
       		if (j.getLength() == 0)
       			System.out.println("There are no jobs that match this selection. ");

       		succeeded();
       		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
      
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_PRESTART.
       **/
       
       public void Var114()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_PRESTART);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_RUNNING.
       **/
       
       public void Var115()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_RUNNING);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_SELECTION.
       **/
       
       public void Var116()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_SELECTION);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_SEMAPHORE.
       **/
       
       public void Var117()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_SEMAPHORE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_STOPPED.
       **/
       
       public void Var118()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_STOPPED);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_SIGNAL.
       **/
       
       public void Var119()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_SIGNAL);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_SUSPENDED_SYSTEM_REQUEST.
       **/
       
       public void Var120()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_SUSPENDED_SYSTEM_REQUEST);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_SAVE_FILE_AND_ACTIVE.
       **/
       
       public void Var121()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_SAVE_FILE_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_SAVE_FILE.
       **/
       
       public void Var122()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_SAVE_FILE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
          
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_TAPE_DEVICE_AND_ACTIVE.
       **/
       
       public void Var123()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_TAPE_DEVICE_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_TAPE_DEVICE.
       **/
       
       public void Var124()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_TAPE_DEVICE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();;
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_THREAD.
       **/
       
       public void Var125()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_THREAD);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_TIME_INTERVAL_AND_ACTIVE.
       **/
       
       public void Var126()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_TIME_INTERVAL_AND_ACTIVE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_ACTIVE_JOB_STATUS with 
       ACTIVE_JOB_STATUS_WAIT_TIME_INTERVAL.
       **/
       
       public void Var127()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for 
       			//SELECTION_PRIMARY_JOB_STATUS_ACTIVE is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE); 
       			j.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,Job.ACTIVE_JOB_STATUS_WAIT_TIME_INTERVAL);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_QUEUE_STATUS_SCHEDULE using
       true and false as selectionValue.
       **/
       
       public void Var128()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       		    //This value is only used when the value for SELECTION_PRIMARY_JOB_STATUS_JOBQ
       			//is true. 
       		    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_QUEUE_STATUS_SCHEDULE,Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_QUEUE_STATUS_SCHEDULE,Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_QUEUE_STATUS_HELD using
       true and false as selectionValue.
       **/
       
       public void Var129()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
     	
       			
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
   		    	//This value is only used when the value for SELECTION_PRIMARY_JOB_STATUS_JOBQ
   				//is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.TRUE);
   		    	j.addJobSelectionCriteria(JobList.SELECTION_JOB_QUEUE_STATUS_HELD,Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_QUEUE_STATUS_HELD,Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_QUEUE_STATUS_READY using
       true and false as selectionValue.
       **/
       
       public void Var130()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
     	
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for SELECTION_PRIMARY_JOB_STATUS_JOBQ
   				//is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_QUEUE_STATUS_READY,Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_QUEUE_STATUS_READY,Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_QUEUE.
       **/
       
       public void Var131()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
       		QSYSObjectPathName obj = new QSYSObjectPathName("QUSRSYS","QSECOFR","MSGQ");
     	
		// Add active job selection criteria to allow job to run faster. 
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       			//This value is only used when the value for SELECTION_PRIMARY_JOB_STATUS_JOBQ
   				//is true. 
       			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.TRUE);
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_QUEUE,obj.getPath());
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_QUEUE,obj.getPath());
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_INITIAL_USER using qsecofr
       as the user.
       **/
       
       public void Var132()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

       			j.addJobSelectionCriteria(JobList.SELECTION_INITIAL_USER,"QSECOFR");
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_SERVER_TYPE with '1911'
       **/
       
       public void Var133()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

       			//1911 is the value you get from Job.SERVER_TYPE
       			j.addJobSelectionCriteria(JobList.SELECTION_SERVER_TYPE,"1911");

	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_SERVER_TYPE with SELECTION_SERVER_TYPE_ALL 
       **/
       
       public void Var134()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	

       			j.addJobSelectionCriteria(JobList.SELECTION_SERVER_TYPE,JobList.SELECTION_SERVER_TYPE_ALL);

	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_SERVER_TYPE with 
       SELECTION_SERVER_TYPE_BLANK.
       **/
       
       public void Var135()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      

       			j.addJobSelectionCriteria(JobList.SELECTION_SERVER_TYPE,JobList.SELECTION_SERVER_TYPE_BLANK);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	    j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
          
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_SERVER_TYPE with 
       a generic value.
       **/
       
       public void Var136() {
	   try   {
	       JobList j = new JobList(systemObject_);	

	       j.addJobSelectionCriteria(JobList.SELECTION_SERVER_TYPE,"1910");
	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

	       if (j.getLength() == 0)
		   System.out.println("There are no jobs that match this selection. ");

	       succeeded();
	       j.clearJobSelectionCriteria();
	   }
	   catch(Exception e)
	   {
	       failed(e, "Unexpected exception using addJobSelectionCriteria().");	
	   }		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       SELECTION_JOB_TYPE_ENHANCED_ALL_BATCH.
       **/
       
       public void Var137()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,JobList.SELECTION_JOB_TYPE_ENHANCED_ALL_BATCH);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       SELECTION_JOB_TYPE_ENHANCED_ALL_INTERACTIVE.
       **/
       
       public void Var138()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,JobList.SELECTION_JOB_TYPE_ENHANCED_ALL_INTERACTIVE );

			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       SELECTION_JOB_TYPE_ENHANCED_ALL_PRESTART.
       **/
       
       public void Var139()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,JobList.SELECTION_JOB_TYPE_ENHANCED_ALL_PRESTART);

	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
        
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_AUTOSTART.
       **/
       
       public void Var140()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_AUTOSTART );

			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
          
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_AUTOSTART.
       **/
       
       public void Var141()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_AUTOSTART );
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_BATCH.
       **/
       
       public void Var142()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_BATCH);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_BATCH_IMMEDIATE.
       **/
       
       public void Var143()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_BATCH_IMMEDIATE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_BATCH_MRT.
       **/
       
       public void Var144()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_BATCH_MRT);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_BATCH_ALTERNATE_SPOOL_USER.
       **/
       
       public void Var145()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_BATCH_ALTERNATE_SPOOL_USER);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_COMM_PROCEDURE_START_REQUEST.
       **/
       
       public void Var146()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_COMM_PROCEDURE_START_REQUEST);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_INTERACTIVE.
       **/
       
       public void Var147()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_INTERACTIVE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
        
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_INTERACTIVE_GROUP.
       **/
       
       public void Var148()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_INTERACTIVE_GROUP);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_INTERACTIVE_SYSREQ.
       **/
       
       public void Var149()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_INTERACTIVE_SYSREQ);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_INTERACTIVE_SYSREQ_AND_GROUP.
       **/
       
       public void Var150()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_INTERACTIVE_SYSREQ_AND_GROUP);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_PRESTART.
       **/
       
       public void Var151()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_PRESTART);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_PRESTART_BATCH.
       **/
       
       public void Var152()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_PRESTART_BATCH);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_PRESTART_COMM.
       **/
       
       public void Var153()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_PRESTART_COMM);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_READER.
       **/
       
       public void Var154()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_READER);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_SUBSYSTEM.
       **/
       
       public void Var155()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_SUBSYSTEM);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_SYSTEM.
       **/
       
       public void Var156()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_SYSTEM);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
       Verify addJobSelectionCriteria() retrieving a list by SELECTION_JOB_TYPE_ENHANCED with 
       Job.JOB_TYPE_ENHANCED_WRITER.
       **/
       
       public void Var157()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      
       			j.addJobSelectionCriteria(JobList.SELECTION_JOB_TYPE_ENHANCED,Job.JOB_TYPE_ENHANCED_WRITER);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       	    	
           		if (j.getLength() == 0)
           			System.out.println("There are no jobs that match this selection. ");

           		succeeded();
           		j.clearJobSelectionCriteria();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using addJobSelectionCriteria().");	
       	}		
       }
       
       /**
        Verify that clearJobAttributesToRetrieve() works.
       **/
       
       public void Var158()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
      			
       			j.addJobAttributeToRetrieve(Job.CPU_TIME_USED);
           		j.addJobAttributeToRetrieve(Job.ACTIVE_JOB_STATUS);
           		j.clearJobAttributesToRetrieve();
           		
           		succeeded();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using clearJobAttributesToRetrieve().");	
       	}		
       }
       
       /**
       Verify that addJobAttributeToSortOn() works.
      **/
      
       public void Var159()
       {
       	try
      	    {
       		JobList j = new JobList(systemObject_);	

		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

      				j.addJobAttributeToSortOn(Job.ACTIVE_JOB_STATUS,true);
      				j.addJobAttributeToSortOn(Job.JOB_DATE,true);
      				j.addJobAttributeToSortOn(Job.USER_NAME,true);
      				j.clearJobAttributesToSortOn();
          		
      				succeeded();
      	    }
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using clearJobAttributesToSortOn().");	
       	}		
     	}
      
       /**
      	Verify that clearJobSelectionCriteria() works.
        **/
     
       public void Var160()
       {
	   try
	   {
	       JobList j = new JobList(systemObject_);	

	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	       j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
	       j.clearJobSelectionCriteria();

	       succeeded();
	   }
	   catch(Exception e)
	   {
	       failed(e, "Unexpected exception using clearJobSelectionCriteria().");	
	   }		
       }
     
     
       /**
     	Verify that close() works.
        **/
    
       public void Var161()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
   			
       			j.addJobAttributeToRetrieve(Job.ACTIVE_JOB_STATUS);
       			j.getJobs();
       			j.close();
       		   		
    				succeeded();
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using close().");	
       	}		
       }

       private final boolean isRunningNativelyAndThreadsafe()
       {
         String prop = getProgramCallThreadSafetyProperty(); // we only care about ProgramCall, not CommandCall
         if (onAS400_ && isNative_ && isLocal_ &&
             prop != null && prop.equals("true"))
         {
           return true;
         }
         else return false;
       }
       
       /**
     	Verify that close() works and that AS400SecurityException is thrown using 
     	a wrong password.
        **/
    
       public void Var162()
       {
	   StringBuffer sb = new StringBuffer(); 
         try
         {

	   setupTestProfile("JAVAPASS","GOODPASS", sb);

	  
           AS400 system = new AS400(systemObject_.getSystemName(),"JAVAPASS", "WRONGPASS");
           JobList j = new JobList(system);	

           system.setGuiAvailable(false);
           j.addJobAttributeToRetrieve(Job.ACTIVE_JOB_STATUS);
	   j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
	   j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	   j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           j.getJobs();
           j.close();

           // If we're running natively and assuming all program calls are threadsafe, we won't use the supplied uid/pwd.
           if (onAS400_ && isNative_ && isLocal_ && isRunningNativelyAndThreadsafe())
           {
             System.out.println("Tolerating lack of exception.");
             succeeded();
           }
           else failed("Exception didn't occur.");
         }
         catch(Exception e)
         {
           assertExceptionIs(e, "AS400SecurityException");
         }
	 cleanupTestProfile("JAVAPASS");
		
       }
       
      

      /**
        * Updated by ZZ, Toolbox handle the situation that connection has been disconnected before job list close from JTOPen 9.8. 
     	verify close() works and no exception occur
        * So, we don't need to Verify that close() works and that AS400Exception is thrown using ErrorCompletingRequestException.
        **/
    
       public void Var163()
       {
         try
         {
           AS400 system = systemObject_;
           JobList j = new JobList(system);	

	   j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	   j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           system.setGuiAvailable(false);
           j.addJobAttributeToRetrieve(Job.ACTIVE_JOB_STATUS);
	
           j.load();
           system.disconnectAllServices();
           j.close();

           // If we're running natively and assuming all program calls are threadsafe, we won't use the supplied uid/pwd.
           if (onAS400_ && isNative_ && isLocal_ && isRunningNativelyAndThreadsafe())
           {
             System.out.println("Tolerating lack of exception.");
             //succeeded();
           }
           succeeded();
           //else failed("Exception didn't occur.");
         }
         catch(Exception e)
         {
        	 failed(e, "Unexpected exception using JobList close()");
           //assertExceptionStartsWith(e, "AS400Exception", "GUI0001", ErrorCompletingRequestException.AS400_ERROR);	
         }		
       }
        
       
       /**
     	Verify that load() works.
        **/
    
       public void Var164()
       {
       	try
       	{
       		JobList j = new JobList(systemObject_);	
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
       		
       			j.load();
       			System.out.println("Job List length: "+ j.getLength());
       			j.close();
       			succeeded();			
       	}
       	catch(Exception e)
       	{
       		failed(e, "Unexpected exception using load().");	
       	}		
       }
       
       /**
     	Verify that load() works and that AS400SecurityException is thrown using 
     	a wrong password.
        **/
    
       public void Var165()
       {
         StringBuffer sb = new StringBuffer(); 
         try
         {

	   setupTestProfile("JAVAPASS","GOODPASS", sb);
           AS400 system = new AS400(systemObject_.getSystemName(),"JAVAPASS", "WRONGPASS");
           JobList j = new JobList(system);	
	   j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
	   j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

           system.setGuiAvailable(false);
           j.load();
           // If we're running natively and assuming all program calls are threadsafe, we won't use the supplied uid/pwd.
           if (onAS400_ && isNative_ && isLocal_ && isRunningNativelyAndThreadsafe())
           {
             System.out.println("Tolerating lack of exception.");
             succeeded();
           }
           else failed("Exception didn't occur." + sb.toString());
         }
         catch(Exception e)
         {
           assertExceptionIs(e, "AS400SecurityException", sb);	
         }
	 cleanupTestProfile("JAVAPASS");
       }
       
       /**
     	Verify that load() works and that ExtendedIllegalStateException is thrown when the system
     	parameter is not set.
        **/
    
       public void Var166()
       {
       	try
       	{
       		
       		JobList j = new JobList();
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		j.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
	
       		j.load();
       		failed("Exception didn't occur.");	
       		
       	}
       	catch(Exception e)
       	{
       		assertExceptionIs(e, "ExtendedIllegalStateException");	
       	}		
       }

  public void setupTestProfile(String userid, String password, StringBuffer sb)
      throws Exception {

    sb.append("Checking pwrSysUserID_, pwrSysPassword_\n");
    if (pwrSysUserID_ == null)
      throw new Exception("pwrSysUserID_ is null");
    if (pwrSysEncryptedPassword_ == null)
      throw new Exception("pwrSysEncryptedPassword_ is null");

    sb.append("Creating pwrSystem\n");
    char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 pwrSystem = new AS400(systemObject_.getSystemName(), pwrSysUserID_,
        charPassword);
    PasswordVault.clearPassword(charPassword);

    // Create a profile for this test.
    CommandCall cmdCall = new CommandCall(pwrSystem);
    String command = "CRTUSRPRF " + userid + " PASSWORD(" + password + ")";
    sb.append("Running " + command + "\n");
    boolean results = cmdCall.run(command);
    if (!results) {
      dumpFailingCommand(sb, cmdCall);
      sb.append(" Trying CHGUSRPF because failed ");
      command = "CHGUSRPRF " + userid + " PASSWORD(" + password + ")";
      sb.append("Running " + command + "\n");
      results = cmdCall.run(command);
    }

    pwrSystem.disconnectAllServices(); 
  }
    
  private void dumpFailingCommand(StringBuffer sb, CommandCall cmdCall) {
    sb.append("Command failed\n");
    AS400Message[] messageList = cmdCall.getMessageList();
    if (messageList == null) {
      sb.append("  Message list is null \n");
    } else if (messageList.length == 0) {
      sb.append("  Message list is empty \n");
    } else {
      for (int i = 0; i < messageList.length; i++) {
        sb.append("  Message[" + i + "] = " + messageList[i]);
      }
    }

  }

  private void cleanupTestProfile(String userid) {
    try { 
 
        char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 pwrSystem = new AS400(systemObject_.getSystemName(), pwrSysUserID_,
        charPassword);
    PasswordVault.clearPassword(charPassword);


    CommandCall cmdCall = new CommandCall(pwrSystem);

    String command = "DLTUSRPRF " + userid;
    boolean results = cmdCall.run(command);
    if (!results) {
      System.out.println("Warning : " + command + " failed");
      StringBuffer sb = new StringBuffer();
      dumpFailingCommand(sb, cmdCall);
      System.out.println(sb);
    }
    pwrSystem.disconnectAllServices();
    } catch (Exception e) { 
      System.out.println("Warning.  exception caught in cleanupTestProfile"); 
      e.printStackTrace(System.out) ;
    }

  }
}

