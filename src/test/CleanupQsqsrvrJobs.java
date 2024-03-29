///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CleanupQsqsrvrJobs.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobList;
import com.ibm.as400.access.JobLog;
import com.ibm.as400.access.QueuedMessage;

public class CleanupQsqsrvrJobs {
  public static void usage() {
    System.out.println(
        "Usage:  java CleanupQsqsrvrjobs [<SYSTEM> <USERID> <PASSWORD> ]");
    System.out.println(
        "   Ends all the QSQSRVR jobs that are used by testcases and older than 1 hour");
  }

  public static void cleanup(String system, String userid, String password) throws Exception {
    AS400 as400;

    if (system == null) {
      as400 = new AS400();
    } else {
      as400 = new AS400(system, userid, password.toCharArray());
    }

    JobList joblist = new JobList(as400);
    joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE,
        Boolean.TRUE);
    joblist.addJobSelectionCriteria(JobList.SELECTION_JOB_NAME, "QSQSRVR");
    Hashtable<String, String> userSet = new Hashtable<String, String>();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    Enumeration<?> enumeration = joblist.getJobs();
    long currentTimeMillis = System.currentTimeMillis();
    while (enumeration.hasMoreElements()) {
      Job j = (Job) enumeration.nextElement();
      String user = j.getStringValue(Job.CURRENT_USER);
      userSet.put(user, user);
      // System.out.println("Job name is
      // "+j.getNumber()+"/"+j.getUser()+"/"+j.getName()+" user is "+user);
      if (user.equalsIgnoreCase("JAVA") || user.equalsIgnoreCase("NEWTONJUDF")
          || user.equalsIgnoreCase("SQLJTEST")
          || user.equalsIgnoreCase("JDPWRSYS")) {
        System.out.println("-------------------");
        System.out.println("Job name is " + j.getNumber() + "/" + j.getUser()
            + "/" + j.getName() + " user is " + user);
        JobLog joblog = j.getJobLog();
        Enumeration<?> messageEnumeration = joblog.getMessages();
        boolean endJob = false;
        //
        // Look for message of the following form. If the message is older than
        // 1 hour then
        // end the job.
        // Message ID . . . . . . : CPF9898 Severity . . . . . . . : 40
        // Message type . . . . . : Completion
        // Date sent . . . . . . : 12/16/14 Time sent . . . . . . : 08:01:35
        //
        // Message . . . . : SERVER MODE CONNECTING JOB IS 631220/JAVA/QJVAEXEC.
        // Cause . . . . . : This message is used by application programs as a
        // general
        // escape message.
        //
        boolean connectingJobMessageFound = false;
        while (!connectingJobMessageFound
            && messageEnumeration.hasMoreElements()) {
          QueuedMessage message = (QueuedMessage) messageEnumeration
              .nextElement();
          if (message.getID().equals("CPF9898")) {
            // System.out.println("CPF9898 found");
            String text = "Text is " + message.getText();
            System.out.println(text);
            if (text.indexOf("SERVER MODE CONNECTING JOB") >= 0) {
              connectingJobMessageFound = true;

              Calendar date = message.getDate();
              long millis = date.getTimeInMillis();
              if (currentTimeMillis - millis > 3600000) {
                System.out.println("Message send over an hour ago "
                    + simpleDateFormat.format(date.getTime()));
                endJob = true;
              } else {
                System.out.println(
                    "New message " + simpleDateFormat.format(date.getTime()));
              }

            }
          }
        }
        if (endJob) {
          System.out.println("Ending job");
          try {
            j.end(0);
          } catch (Exception e) {
            String message = e.toString();
            if (message.indexOf("not allowed") >= 0) {
              System.out.println("Job already ending");
            } else {
              e.printStackTrace();
            }
          }
        }
      } else {
        System.out.println("-------------------");
        System.out.println("Skipping job for " + user);
      }
    }

    System.out.println("-----------------------------------------");
    System.out.println("Here are the users found on the system");
    Enumeration<String> userEnumeration = userSet.keys();
    while (userEnumeration.hasMoreElements()) {
      System.out.println(userEnumeration.nextElement());
    }

  }

  public static void main(String args[]) {
    String system = null;
    String userid = null;
    String password = null;

    try {
      if (args.length >= 3) {
        system = args[0];
        userid = args[1];
        password = args[2];
      }
       cleanup(system,userid,password);

    } catch (Exception e) {
      e.printStackTrace();
      usage();
    }

  }
}
