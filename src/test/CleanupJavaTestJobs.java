///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CleanupJavaTestJobs.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobList;
import com.ibm.as400.access.JobLog;
import com.ibm.as400.access.QueuedMessage;

public class CleanupJavaTestJobs {
	public static void usage() {
		System.out
		    .println("Usage:  java CleanupJavaTestJobs [<SYSTEM> <USERID> <PASSWORD> ]");
		System.out
		    .println("   Ends all the QJVAEXEC and CLIJOBURN jobs that are used by testcases and older than 1 hour");
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
		
		cleanup(system, userid, password);
		} catch (Exception e) { 
		  e.printStackTrace(); 
		}
	}

	public final static int OS_OS400 = 1;
	public final static int OS_WINDOWS = 2;
	public final static int OS_LINUX = 3;

	public static void cleanup(String system, String userid, String password) {
		try {
			int os;
			

			if (JTOpenTestEnvironment.isOS400) {
				os = OS_OS400;
			} else if (JTOpenTestEnvironment.isWindows) {
				os = OS_WINDOWS;
			} else if (JTOpenTestEnvironment.isLinux) {
				os = OS_LINUX;
			} else {
				throw new Exception("Unsupported OS " + JTOpenTestEnvironment.osVersion);
			}
			boolean jobNotFoundStackPrinted = false; 
			if (os == OS_OS400 || os==OS_WINDOWS) {
				AS400 as400;

				if (system == null || "localhost" == system) {
					as400 = new AS400();

				} else {
					as400 = new AS400(system, userid, password);
				}
				Hashtable userSet = new Hashtable();

				for (int i = 0; i < 2; i++) {

				    JobList joblist = new JobList(as400);
				    if (i == 0) { 
					joblist.addJobSelectionCriteria(JobList.SELECTION_JOB_NAME, "QJVAEXEC");
					System.out.println("Searching for QJVAEXEC"); 
				    } else if (i == 1) {
					joblist.addJobSelectionCriteria(JobList.SELECTION_JOB_NAME, "QP0ZSPWT");
					System.out.println("Searching for QP0ZSPWTC"); 
				    }
				    joblist.addJobSelectionCriteria(
								    JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
            joblist.addJobSelectionCriteria(
                    JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ,  Boolean.FALSE);
            joblist.addJobSelectionCriteria(
                    JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ,  Boolean.FALSE);

				    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
				    joblist.load(); 
				    Enumeration enumeration = joblist.getJobs();
				    long currentTimeMillis = System.currentTimeMillis();
				    boolean continueLoop=true;
				    int jobCount = 0; 
				    while (enumeration.hasMoreElements() && continueLoop) {
					Job j = null; 
					try { 
					    j = (Job) enumeration.nextElement();
					    jobCount++; 
					    String functionName = j.getFunctionName(); 
					    String user = j.getStringValue(Job.CURRENT_USER);
					    userSet.put(user, user);
					// System.out.println("Job name is "+j.getNumber()+"/"+j.getUser()+"/"+j.getName()+" user is "+user+" function name is "+functionName);
					    if (user.equalsIgnoreCase("JAVA")
						|| user.equalsIgnoreCase("NEWTONJUDF")
						|| user.equalsIgnoreCase("SQLJTEST")
						|| user.equalsIgnoreCase("JDPWRSYS")) {
						System.out.println("-------------------");
						System.out.println("Job name is " + j.getNumber() + "/"
								   + j.getUser() + "/" + j.getName() + " user is " + user +" function name is "+functionName);
						JobLog joblog = j.getJobLog();
						Enumeration messageEnumeration = joblog.getMessages();
						boolean endJob = false;
						//
						// Look for message of the following form. If the
						// message is older than 1 hour then
						// end the job.
						// Message ID . . . . . . : CPF9898 Severity . . . . . .
						// . : 40
						// Message type . . . . . : Diagnostic
						// Date sent . . . . . . : 01/15/17 Time sent . . . . .
						// . : 19:17:03
						//
						// Message . . . . : Sun Jan 15 19:17:03 CST 2017
						// Running test.JDLobTest -tc
						// JDLobClobLocator -lib JDT7163N -system UT30P58 -uid
						// JDPWRSYS -pwd xxxxxxxx
						// -pwrSys JDPWRSYS,xxxxxxxx -directory / -misc
						// native,v7r1m0 -asp IASP.
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
							if ((text.indexOf("Running test") >= 0)  ||
							    (text.indexOf("starting worker") >= 0)) {
							    connectingJobMessageFound = true;

							    Calendar date = message.getDate();
							    long millis = date.getTimeInMillis();
							    if (currentTimeMillis - millis > 3600000) {
								System.out.println("Message send over an hour ago "
										   + simpleDateFormat.format(date.getTime()));
								if (i == 1) {
								    System.out.println("jobFunction is "+functionName); 
								    if (functionName.indexOf("CLIJOBRUN") >= 0) {
									System.out.println(" .. ending CLIJOBRUN"); 
									endJob = true;
								    }
								} else { 
								    endJob = true;
								}
							    } else {
								System.out.println("New message "
										   + simpleDateFormat.format(date.getTime()));
							    }

							}
						    } else if (message.getID().equals("JVAB302")) {
							Calendar date = message.getDate();
							long millis = date.getTimeInMillis();
							if (currentTimeMillis - millis > 3600000) {
							    System.out.println(i+": JVAB302 Message send over an hour ago "
									       + simpleDateFormat.format(date.getTime()));
							    if (i == 0) {
								if (functionName.indexOf("test.") >= 0) {
								    System.out.println(" .. ending test. job"); 
								endJob = true; 
								}
							    }
							} else {
							    System.out.println(i+": JVAB302 Recent message "
									       + simpleDateFormat.format(date.getTime()));
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
					    }
					} catch (Exception e) {
					    if (e instanceof java.util.NoSuchElementException) {
						System.out.println("Warning: Unexpected NoSuchElementException ");
						e.printStackTrace(System.out); 
					    } else { 
						String message = e.toString();
						if (message.indexOf("CPF3C53") >= 0 ) {
						    /* for now, ignore other cases where job not found */ 
						    if (!jobNotFoundStackPrinted) {
							jobNotFoundStackPrinted = true;
							System.out.println("Warning: Unexpected Job not found  ");
							System.out.println("job was "+j); 
							e.printStackTrace(System.out);
						    }
						} else { 
						    System.out.println("Error : Unexpected exception exiting loop ");
						    e.printStackTrace(System.out); 
						    continueLoop = false;
						}
					    }

					}  
				    }  /* while enumeration.hasMoreElements */
				    System.out.println("jobCount was "+jobCount); 
				} /* for i */
				System.out.println("-----------------------------------------");
				System.out.println("Here are the users found on the system");
				Enumeration userEnumeration = userSet.keys();
				while (userEnumeration.hasMoreElements()) {
					System.out.println(userEnumeration.nextElement());
				}
			
			}  else if (os == OS_WINDOWS) {
				// How to get list of processes on Windows
				String line;
				String command = "wmic  path win32_process get /format:list";
				Process process = Runtime.getRuntime().exec(command);
				BufferedReader input = new BufferedReader(new InputStreamReader(
				    process.getInputStream()));
				String savedCaption = null; 
				String savedCommandLine = null; 
				String savedCreationDate = null; 
				while ((line = input.readLine()) != null) {
					line = line.trim(); 
					if (line.length() > 0) {
							if (line.indexOf("Caption=") == 0 ) {
								savedCaption = line.substring(8); 
							} else if (line.indexOf("CommandLine=") == 0) { 
								savedCommandLine = line.substring(12); 
							} else if (line.indexOf("CreationDate=") == 0) { 
								savedCreationDate = line.substring(13); 
							} else if (line.indexOf("ProcessId=") == 0) {
								String processId = line.substring(10); 
								
								// System.out.println(">"+processId+","+savedCaption+","+savedCreationDate+","+savedCommandLine);
								
								// Check for a testcase process
								if (savedCaption.indexOf("java.exe") >= 0) {
									System.out.println(">"+processId+","+savedCaption+","+savedCreationDate+","+savedCommandLine);
									command = "TASKKILL /PID "+processId+" /F /T"; 
									// TODO:  Working here 
								}

								
								
								
								
								
								savedCaption = "NONE"; 
								savedCommandLine = "NONE"; 
								savedCreationDate = "NONE"; 
							}
					    
					}
				}
				input.close();

			} else if (os == OS_LINUX) {
				throw new Exception("Linux not yet supported");
			} else {
			  throw new Exception("Unsupported OS = "+os);
			  
			}

		} catch (Exception e) {
			e.printStackTrace();
			usage();
		}

	}
}
