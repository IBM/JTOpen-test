///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CleanupPsrwJobs.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobList;
import com.ibm.as400.access.ObjectDoesNotExistException;

public class CleanupPsrwJobs {
  public static void usage() { 
      System.out.println("Usage:  java CleanupPsrwJobs [<SYSTEM> <USERID> <PASSWORD> ]");
    System.out.println("   Ends all the PSRW database jobs"); 
  }

  
  public static void main(String args[]) {
    String system = null ; 
    String userid = null; 
    String password = null; 
  
    try { 
      if (args.length >= 3) {
        system=args[0]; 
        userid=args[1];
        password=args[2]; 
      }

      PrintWriter writer = new PrintWriter(System.out) ;
      clean(system, userid, password, writer);
    } catch (Exception e) { 
	e.printStackTrace(); 
	usage(); 
    }

  }

  public static void clean(String system, String userid, String password, PrintWriter out ) throws PropertyVetoException, AS400SecurityException, ErrorCompletingRequestException, InterruptedException, IOException, ObjectDoesNotExistException { 
      AS400 as400; 
      
      if (system == null) { 
        as400 = new AS400(); 
      } else { 
        as400 = new AS400(system, userid, password.toCharArray()); 
      }
      
      JobList joblist = new JobList(as400); 
      joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
      joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
      joblist.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS, Job.ACTIVE_JOB_STATUS_WAIT_PRESTART);

      Enumeration<Job> enumeration = joblist.getJobs(); 
      StringBuffer endedJobInfo = new StringBuffer(); 
      
      while (enumeration.hasMoreElements()) {
      Job j =  enumeration.nextElement();
      
      String status = j.getStatus(); 
      if (status.equals(Job.JOB_STATUS_ACTIVE)) { 
      boolean endjob = false;
      String name = j.getName(); 
      if (name.equals("QDBMSRVR")) { 
        endjob = true; 
      } else if (name.equals("QZDASOINIT")) { 
        endjob = true; 
      } else if (name.equals("QSQSRVR")) { 
        endjob = true; 
      } else if (name.equals("QRWTSRVR")) { 
        endjob = true;
      } else if (name.equals("QP0ZSPWP")) { 
        endjob = true; 
      } else if (name.equals("QP0ZSPWT")) { 
        endjob = true; 
      } else if (name.equals("QZSOSIGN")) { 
        endjob = true; 
      } else if (name.equals("QZRCSRVS")) { 
        endjob = true; 
      } else if (name.equals("QNPSERVS")) { 
        endjob = true; 
      } else { 
        out.println("Not ending job "+ j.toString()) ;
      }
      
      if (endjob) {
        endedJobInfo.append("Ending job " + j.toString()+"\n");
        try {
          j.end(0);
        } catch (Exception e) {
          String message = e.toString();
          if (message.indexOf("not allowed") >= 0) {
            System.out.println("Job already ending");
          } else {
            e.printStackTrace();
          }
        } /* catch */
      } /* endjob */ 
      }
    }
      /* while */
     out.println(endedJobInfo.toString()) ;
     out.flush(); 

  }
}
