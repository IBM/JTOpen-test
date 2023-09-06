///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCleanSplf.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.PrintObjectListEvent;
import com.ibm.as400.access.PrintObjectListListener;
import com.ibm.as400.access.PrintObjectPageInputStream;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.access.SpooledFileList;
import java.sql.Timestamp;

public class JDCleanSplf implements PrintObjectListListener {

	private static final int DEFAULT_TIMEOUT_MILLIS = 15000;
	static int defaultTimeoutMillis = DEFAULT_TIMEOUT_MILLIS;
	public static int DEFAULT_DAYS = 7;
	public static boolean verbose = false;

	public static void usage() {
		System.out.println("Usage:  java -Dverbose=true/false JDCleanSplf <system> <userid> <password> <days> <ALL>");
		System.out.println("        deletes spool files on the system older than <days>");
		System.out.println("        Only deletes spool files from testcases except if <ALL> is specified");
		System.out.println("        minimum value of days is 1");
		System.out.println("        Default days is " + DEFAULT_DAYS);
	}

	static {
		String verboseString = System.getProperty("verbose");
		if (verboseString != null && verboseString.equalsIgnoreCase("true")) {
			verbose = true;
		}
		String debugString = System.getProperty("debug");
		if (debugString != null) {
			verbose = true;
		}
	}
	public static NumberFormat numberFormat = NumberFormat.getIntegerInstance();

	public static void main(String args[]) {
		try {
			String system = "localhost";
			String userid = null;
			String password = null;
			int days = DEFAULT_DAYS;
			boolean deleteAll = false;

			if (args[0].equals("help") || args[0].equals("-help")) {
				usage();
				System.exit(1);
			}
			if (args.length >= 1) {
				system = args[0];
			}
			if (args.length >= 2) {
				userid = args[1];
				if (userid.equals("null")) {
					userid = null;
				}
			}
			if (args.length >= 3) {
				password = args[2];
				if (password.equals("null")) {
					password = null;
				}
			}

			if (args.length >= 4) {
				days = Integer.parseInt(args[3]);
				if (days == 0) {
					days = 1;
				}
				System.out.println("days is " + days);
			}

			if (args.length >= 5) {
				if ("ALL".equalsIgnoreCase(args[4])) {
					System.out.println("deleting all ");
					deleteAll = true;
				}
			}

			Hashtable deleteUserIds = new Hashtable();
			deleteUserIds.put("BOSSJUDF", "BOSSJUDF");
			deleteUserIds.put("JAVA", "JAVA");
			deleteUserIds.put("JAVA290", "JAVA290");
			deleteUserIds.put("JDBBDUS2N", "JDBBDUS2N");
			deleteUserIds.put("JDCON500", "JDCON500");
			deleteUserIds.put("JDCON65535", "JDCON65535");
			deleteUserIds.put("JDPWRSYS", "JDPWRSYS");
			deleteUserIds.put("JDSTJARUSR", "JDSTJARUSR");
			deleteUserIds.put("NEWTONJUDF", "NEWTONJUDF");
			deleteUserIds.put("SQLJTEST", "SQLJTEST");

			AS400 as400;
			if (userid == null) {
				as400 = new AS400(system);
			} else {
				as400 = new AS400(system, userid, password.toCharArray());
			}
			/* endDateFilter is in the form. CYYMMDD */
			/* run the delete for the last year first */

			long endFilterTime = System.currentTimeMillis() - ((days + 1) * 1000L * 3600 * 24);
			long startFilterTime = endFilterTime - 1L * 365 * 24 * 3600000L;

			System.out.println("RUNNING delete (ALL=" + deleteAll + ") for last year");
			JDCleanSplfResults results = deleteSpoolFiles(System.out, as400, "", startFilterTime, endFilterTime,
					deleteAll, deleteUserIds);
			System.out.println("Done:  (last year) deleteCount is " + results.deleteCount);
			System.out.println("Done:  (last year) deleteBytes is " + numberFormat.format(results.deleteBytes));
			System.out.println("Done:  (last year) keepCount is " + results.keepCount);
			System.out.println("Done:  (last year) keepBytes is " + numberFormat.format(results.keepBytes));
			System.out.println("Done:  (last year) processedCount is " + results.processedCount);

			/* Run the delete for last 10 years */
			endFilterTime = endFilterTime - 1L * 365 * 24 * 3600000L;
			startFilterTime = endFilterTime - 10L * 365 * 24 * 3600000L;
			System.out.println("RUNNING delete for last 10 years");
			results = deleteSpoolFiles(System.out, as400, "", startFilterTime, endFilterTime, deleteAll, deleteUserIds);
			System.out.println("Done:  (10 year) deleteCount is " + results.deleteCount);
			System.out.println("Done:  (10 year) deleteBytes is " + numberFormat.format(results.deleteBytes));
			System.out.println("Done:  (10 year) keepCount is " + results.keepCount);
			System.out.println("Done:  (10 year) keepBytes is " + numberFormat.format(results.keepBytes));
			System.out.println("Done:  (10 year) processedCount is " + results.processedCount);

			if (!deleteAll) {
				startFilterTime = endFilterTime - 1L * 365 * 24 * 3600000L;
				System.out.println("Running delete of useless for last year");
				JDCleanSplfResults uselessResults = deleteUselessSpoolFiles(System.out, as400, "", startFilterTime,
						System.currentTimeMillis());
				System.out.println("Done:  deleteCount is    " + results.deleteCount);
				System.out.println("Done:  deleteBytes is " + numberFormat.format(results.deleteBytes));
				System.out.println("Done:  keepCount is    " + results.keepCount);
				System.out.println("Done:  keepBytes is " + numberFormat.format(results.keepBytes));
				System.out.println("Done:  processedCount is " + results.processedCount);
				int hours = (int) (results.processSeconds / 3600);
				int minutes = (int) ((results.processSeconds / 60) % 60);
				int seconds = (int) (results.processSeconds % 60);
				System.out.println("Done:  processTime is    " + hours + ":" + minutes + ":" + seconds);

				System.out.println("Done:  uselessDeleteCount is    " + uselessResults.deleteCount);
				System.out
						.println("Done:  uselessDeleteBytes is    " + numberFormat.format(uselessResults.deleteBytes));
				System.out.println("Done:  uselessKeepCount is    " + uselessResults.keepCount);
				System.out.println("Done:  uselessKeepBytes is    " + numberFormat.format(uselessResults.keepBytes));
				System.out.println("Done:  uselessProcessedCount is " + uselessResults.processedCount);
				hours = (int) (uselessResults.processSeconds / 3600);
				minutes = (int) ((uselessResults.processSeconds / 60) % 60);
				seconds = (int) (uselessResults.processSeconds % 60);
				System.out.println("Done:  uselessProcessTime is    " + hours + ":" + minutes + ":" + seconds);
			}

			System.out.println("Submitting RCLSPLSTG");
			String command = "SBMJOB CMD(RCLSPLSTG DAYS(*NONE))";
			CommandCall cmd = new CommandCall(as400);
			cmd.run(command);
			System.out.println("DONE");
		} catch (Exception e) {
			System.out.println("Exception while running");
			e.printStackTrace(System.out);
			usage();
		}

	}

	public static String filterTimeToString(long filterTime) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(filterTime);
		String year = "" + (cal.get(Calendar.YEAR));
		String month = "" + (cal.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = "" + (cal.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1) {
			day = "0" + day;
		}

		String hour = "" + (cal.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		String minute = "" + (cal.get(Calendar.MINUTE));
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		String second = "" + (cal.get(Calendar.SECOND));
		if (second.length() == 1) {
			second = "0" + second;
		}

		return " " + month + "/" + day + "/" + year + "_" + hour + ":" + minute + ":" + second;
	}

	public static JDCleanSplfResults deleteSpoolFiles(PrintStream out, AS400 as400, String nestLevel,
			long startFilterTime, long endFilterTime, boolean deleteAll, Hashtable deleteUserIds) throws Exception {

		long startTime = System.currentTimeMillis();
		String startDateFilter = getDateFilter(startFilterTime);
		String startTimeFilter = getTimeFilter(startFilterTime);
		String endDateFilter = getDateFilter(endFilterTime);
		String endTimeFilter = getTimeFilter(endFilterTime);
		out.println(nestLevel + "Attempting deleteSpoolFiles " + startDateFilter + ":" + startTimeFilter + " - "
				+ endDateFilter + ":" + endTimeFilter);
		if (startDateFilter.equals(endDateFilter) && startTimeFilter.equals(endTimeFilter)) {
			out.println(nestLevel + "**** Warning -- aborting since time filters are the same");
			return new JDCleanSplfResults(0, 0, 0, 0, 0, 0);
		}

		try {

			return deleteSpoolFilesAttempt(out, as400, nestLevel, startFilterTime, endFilterTime, deleteAll,
					deleteUserIds);
		} catch (Exception e) {
			long failTime = System.currentTimeMillis();
			String message = e.toString();
			/* CPF34C4 List is too large for user space QNPSLIST */
			/* or connection dropped exception */
			if ((message.indexOf("CPF34C4") >= 0) || (message.indexOf("ConnectionDropped") >= 0)) {
				if (message.indexOf("CPF34C4") >= 0) {
					out.println(nestLevel + "CPF34C4 List is too large for user space QNPSLIST caught ");
				} else {
					out.println(nestLevel + "Connection Dropped caught ");
					as400.disconnectAllServices();
				}
				long seconds = (failTime - startTime) / 1000;
				out.println(nestLevel + "Error after " + seconds + " seconds for " + startDateFilter + ":"
						+ startTimeFilter + " - " + endDateFilter + ":" + endTimeFilter);

				if (verbose) {
					e.printStackTrace(out);
				}
				// Split the time in quarters and call recursively
				long quarterTime = (endFilterTime - startFilterTime) / 4;
				if (quarterTime < 1000) {
					out.println(" **** Warning *** quarter time too small . aborting");
					return new JDCleanSplfResults(0, 0, 0, 0, 0, 0);
				}

				JDCleanSplfResults firstResults = deleteSpoolFiles(out, as400, nestLevel + "a ", startFilterTime,
						startFilterTime + quarterTime, deleteAll, deleteUserIds);
				JDCleanSplfResults secondResults = deleteSpoolFiles(out, as400, nestLevel + "b ",
						startFilterTime + quarterTime, startFilterTime + 2 * quarterTime, deleteAll, deleteUserIds);
				JDCleanSplfResults thirdResults = deleteSpoolFiles(out, as400, nestLevel + "c ",
						startFilterTime + 2 * quarterTime, startFilterTime + 3 * quarterTime, deleteAll, deleteUserIds);
				JDCleanSplfResults fourthResults = deleteSpoolFiles(out, as400, nestLevel + "d ",
						startFilterTime + 3 * quarterTime, endFilterTime, deleteAll, deleteUserIds);

				JDCleanSplfResults newResults = new JDCleanSplfResults(firstResults, secondResults, thirdResults,
						fourthResults);
				newResults.processSeconds += seconds; /* include the timeout time */

				long hours = newResults.processSeconds / 3600;
				long minutes = (newResults.processSeconds / 60) % 60;
				seconds = newResults.processSeconds % 60;

				out.println(nestLevel + "DONE         for " + startDateFilter + ":" + startTimeFilter + " - "
						+ endDateFilter + ":" + endTimeFilter + " deleteCount=" + newResults.deleteCount
						+ " deleteBytes=" + numberFormat.format(newResults.deleteBytes) + " keepCount="
						+ newResults.keepCount + " keepBytes=" + numberFormat.format(newResults.keepBytes)
						+ " processCount=" + newResults.processedCount + " time=" + hours + ":" + minutes + ":"
						+ seconds);

				return newResults;
			} else {
				throw e;
			}
		}
	}

	public static JDCleanSplfResults deleteUselessSpoolFiles(PrintStream out, AS400 as400, String nestLevel,
			long startFilterTime, long endFilterTime) throws Exception {

		long startTime = System.currentTimeMillis();
		String startDateFilter = getDateFilter(startFilterTime);
		String startTimeFilter = getTimeFilter(startFilterTime);
		String endDateFilter = getDateFilter(endFilterTime);
		String endTimeFilter = getTimeFilter(endFilterTime);
		out.println(nestLevel + "Attempting deleteUselessSpoolFile " + startDateFilter + ":" + startTimeFilter + " - "
				+ endDateFilter + ":" + endTimeFilter);

		try {

			return deleteUselessSpoolFilesAttempt(out, as400, nestLevel, startFilterTime, endFilterTime);
		} catch (Exception e) {
			long failTime = System.currentTimeMillis();
			String message = e.toString();
			/* CPF34C4 List is too large for user space QNPSLIST */
			/* or connection dropped exception */
			if ((message.indexOf("CPF34C4") >= 0) || (message.indexOf("ConnectionDropped") >= 0)) {
				if (message.indexOf("CPF34C4") >= 0) {
					out.println(nestLevel + "CPF34C4 List is too large for user space QNPSLIST caught ");
				} else {
					out.println(nestLevel + "Connection Dropped caught ");
					as400.disconnectAllServices();
				}
				long seconds = (failTime - startTime) / 1000;
				out.println(nestLevel + "Error after " + seconds + " seconds for " + startDateFilter + ":"
						+ startTimeFilter + " - " + endDateFilter + ":" + endTimeFilter);

				if (verbose) {
					e.printStackTrace(out);
				}

				// Split the time in half and call recursively
				/* try a smaller sizes first starting at 512 days */
				long quarterTime = (endFilterTime - startFilterTime) / 4;
				if (quarterTime < 1000) {
					out.println(" **** Warning *** quarter time too small . aborting");
					return new JDCleanSplfResults(0, 0, 0, 0, 0, 0);

				} else {
					JDCleanSplfResults resultsA = deleteUselessSpoolFiles(out, as400, nestLevel + "a ", startFilterTime,
							startFilterTime + quarterTime);
					JDCleanSplfResults resultsB = deleteUselessSpoolFiles(out, as400, nestLevel + "b ",
							startFilterTime + quarterTime, startFilterTime + 2 * quarterTime);
					JDCleanSplfResults resultsC = deleteUselessSpoolFiles(out, as400, nestLevel + "c ",
							startFilterTime + 2 * quarterTime, startFilterTime + 3 * quarterTime);
					JDCleanSplfResults resultsD = deleteUselessSpoolFiles(out, as400, nestLevel + "d ",
							startFilterTime + 3 * quarterTime, endFilterTime);
					JDCleanSplfResults newResults = new JDCleanSplfResults(resultsA, resultsB, resultsC, resultsD);
					newResults.processSeconds += seconds; /* Be sure to add the timeout time */
					long hours = newResults.processSeconds / 3600;
					long minutes = (newResults.processSeconds / 60) % 60;
					seconds = newResults.processSeconds % 60;

					out.println(nestLevel + "DONE         for " + startDateFilter + ":" + startTimeFilter + " - "
							+ endDateFilter + ":" + endTimeFilter + " deleteCount=" + newResults.deleteCount
							+ " deleteBytes=" + numberFormat.format(newResults.deleteBytes) + " keepCount="
							+ newResults.keepCount + " keepBytes=" + numberFormat.format(newResults.keepBytes)
							+ " processCount=" + newResults.processedCount + " time=" + hours + ":" + minutes + ":"
							+ seconds);
					return newResults;
				}
			} else {
				throw e;
			}
		}
	}

	public static String getDateFilter(long filterTime) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(filterTime);
		String year = "" + (cal.get(Calendar.YEAR) - 2000);
		if (year.length() == 1) {
			year = "0" + year;
		}
		String month = "" + (cal.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = "" + (cal.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1) {
			day = "0" + day;
		}
		return "1" + year + month + day;

	}

	public static String getTimeFilter(long filterTime) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(filterTime);
		String hour = "" + (cal.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		String minute = "" + (cal.get(Calendar.MINUTE));
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		String second = "" + (cal.get(Calendar.SECOND));
		if (second.length() == 1) {
			second = "0" + second;
		}
		return hour + minute + second;

	}

	public static JDCleanSplfResults deleteSpoolFilesAttempt(PrintStream out, AS400 as400, String nestLevel,
			long startFilterTime, long endFilterTime, boolean deleteAll, Hashtable deleteUserIds) throws Exception {
		SpooledFileList spooledFileList = new SpooledFileList(as400);
		spooledFileList.setUserFilter("*ALL");

		String startDateFilter = getDateFilter(startFilterTime);
		String startTimeFilter = getTimeFilter(startFilterTime);
		String endDateFilter = getDateFilter(endFilterTime);
		String endTimeFilter = getTimeFilter(endFilterTime);

		if (verbose)
			out.println(nestLevel + "Getting list for " + startDateFilter + ":" + startTimeFilter + " - "
					+ endDateFilter + ":" + endTimeFilter);
		spooledFileList.setStartDateFilter(startDateFilter);
		spooledFileList.setStartTimeFilter(startTimeFilter);
		spooledFileList.setEndDateFilter(endDateFilter);
		spooledFileList.setEndTimeFilter(endTimeFilter);

		long startMillis = System.currentTimeMillis();

		JDCleanSplf listener = new JDCleanSplf(out, as400, nestLevel, deleteAll, deleteUserIds);

		spooledFileList.addPrintObjectListListener(listener);
		spooledFileList.openAsynchronously();
		/*
		 * Enumeration objects = spooledFileList.getObjects(); while
		 * (objects.hasMoreElements()) { Object o = objects.nextElement(); if (o
		 * instanceof SpooledFile) { boolean doDelete = false; SpooledFile spooledFile =
		 * (SpooledFile) o; if (deleteAll) { doDelete = true; } else { if
		 * (deleteUserIds.get(spooledFile.getJobUser()) != null) { doDelete = true; } }
		 * if (doDelete) { System.out.println(nestLevel+"Deleting "+
		 * spooledFile.getCreateDate() + " " + spooledFile.getCreateTime() + " " +
		 * spooledFile.getName() + " " + spooledFile.getNumber() + " " +
		 * spooledFile.getJobName() + " " + spooledFile.getJobUser() + " " +
		 * spooledFile.getJobNumber()); spooledFile.delete(); } else {
		 * System.out.println(nestLevel+"Keeping  "+ spooledFile.getCreateDate() + " " +
		 * spooledFile.getCreateTime() + " " + spooledFile.getName() + " " +
		 * spooledFile.getNumber() + " " + spooledFile.getJobName() + " " +
		 * spooledFile.getJobUser() + " " + spooledFile.getJobNumber());
		 * 
		 * }
		 * 
		 * } else { System.out.println(nestLevel+"Object is "+o); }
		 * 
		 * 
		 * } // while moreElements
		 */
		// Poll for list to complete

		waitForComplete(out, as400, nestLevel, spooledFileList, listener, endFilterTime);

		long deleteCount = listener.getDeleteCount();
		long deleteBytes = listener.getDeleteBytes();
		long keepCount = listener.getKeepCount();
		long keepBytes = listener.getKeepBytes();
		long processCount = listener.getProcessCount();

		int processSeconds = (int) (System.currentTimeMillis() - startMillis) / 1000;
		int hours = processSeconds / 3600;
		int minutes = (processSeconds / 60) % 60;
		int seconds = processSeconds % 60;

		// Make sure the default timeout is at least twice as large as the time it takes
		// to
		// process zero rows. So on a slow system, the timeout will gradually increase.
		if (processCount == 0) {
			if ((processSeconds * 2 * 1000) > defaultTimeoutMillis) {
				defaultTimeoutMillis = processSeconds * 2 * 1000;
			}
		}

		out.println(nestLevel + "DONE         for " + startDateFilter + ":" + startTimeFilter + " - " + endDateFilter
				+ ":" + endTimeFilter + " deleteCount=" + deleteCount + " deleteBytes="
				+ numberFormat.format(deleteBytes) + " keepCount=" + keepCount + " keepBytes="
				+ numberFormat.format(keepBytes) + " processCount=" + processCount + " time=" + hours + ":" + minutes
				+ ":" + seconds);
		return new JDCleanSplfResults(deleteCount, deleteBytes, keepCount, keepBytes, processCount, processSeconds);
	}

	public static JDCleanSplfResults deleteUselessSpoolFilesAttempt(PrintStream out, AS400 as400, String nestLevel,
			long startFilterTime, long endFilterTime) throws Exception {
		SpooledFileList spooledFileList = new SpooledFileList(as400);
		spooledFileList.setUserFilter("*ALL");

		String startDateFilter = getDateFilter(startFilterTime);
		String startTimeFilter = getTimeFilter(startFilterTime);
		String endDateFilter = getDateFilter(endFilterTime);
		String endTimeFilter = getTimeFilter(endFilterTime);

		if (verbose)
			out.println(nestLevel + "Getting list for " + startDateFilter + ":" + startTimeFilter + " - "
					+ endDateFilter + ":" + endTimeFilter);
		spooledFileList.setStartDateFilter(startDateFilter);
		spooledFileList.setStartTimeFilter(startTimeFilter);
		spooledFileList.setEndDateFilter(endDateFilter);
		spooledFileList.setEndTimeFilter(endTimeFilter);

		long startMillis = System.currentTimeMillis();

		JDCleanSplf listener = new JDCleanSplf(out, as400, nestLevel);

		spooledFileList.addPrintObjectListListener(listener);
		spooledFileList.openAsynchronously();
		/*
		 * Enumeration objects = spooledFileList.getObjects(); while
		 * (objects.hasMoreElements()) { Object o = objects.nextElement(); if (o
		 * instanceof SpooledFile) { boolean doDelete = false; SpooledFile spooledFile =
		 * (SpooledFile) o; if (deleteAll) { doDelete = true; } else { if
		 * (deleteUserIds.get(spooledFile.getJobUser()) != null) { doDelete = true; } }
		 * if (doDelete) { out.println(nestLevel+"Deleting "+
		 * spooledFile.getCreateDate() + " " + spooledFile.getCreateTime() + " " +
		 * spooledFile.getName() + " " + spooledFile.getNumber() + " " +
		 * spooledFile.getJobName() + " " + spooledFile.getJobUser() + " " +
		 * spooledFile.getJobNumber()); spooledFile.delete(); } else {
		 * out.println(nestLevel+"Keeping  "+ spooledFile.getCreateDate() + " " +
		 * spooledFile.getCreateTime() + " " + spooledFile.getName() + " " +
		 * spooledFile.getNumber() + " " + spooledFile.getJobName() + " " +
		 * spooledFile.getJobUser() + " " + spooledFile.getJobNumber());
		 * 
		 * }
		 * 
		 * } else { out.println(nestLevel+"Object is "+o); }
		 * 
		 * 
		 * } // while moreElements
		 */

		waitForComplete(out, as400, nestLevel, spooledFileList, listener, endFilterTime);

		long deleteCount = listener.getDeleteCount();
		long deleteBytes = listener.getDeleteBytes();
		long keepCount = listener.getKeepCount();
		long keepBytes = listener.getKeepBytes();

		long processCount = listener.getProcessCount();

		int processSeconds = (int) (System.currentTimeMillis() - startMillis) / 1000;
		int hours = processSeconds / 3600;
		int minutes = (processSeconds / 60) % 60;
		int seconds = processSeconds % 60;

		// Make sure the default timeout is at least twice as large as the time it takes
		// to
		// process zero rows. So on a slow system, the timeout will gradually increase.
		if (processCount == 0) {
			if ((processSeconds * 2 * 1000) > defaultTimeoutMillis) {
				defaultTimeoutMillis = processSeconds * 2 * 1000;
			}
		}

		out.println(nestLevel + "DONE         for " + startDateFilter + ":" + startTimeFilter + " - " + endDateFilter
				+ ":" + endTimeFilter + " deleteCount=" + deleteCount + " keepCount=" + keepCount + " processCount="
				+ processCount + " time=" + hours + ":" + minutes + ":" + seconds);
		return new JDCleanSplfResults(deleteCount, deleteBytes, keepCount, keepBytes, processCount, processSeconds);
	}

	static void waitForComplete(PrintStream out, AS400 as400, String nestLevel, SpooledFileList spooledFileList,
			JDCleanSplf listener, long endFilterTime) throws Exception {

// Poll for list to complete
		long endTime = System.currentTimeMillis() + defaultTimeoutMillis;
// Set the end time for longer if the endFilterTime is more than a year old
		if (endFilterTime < System.currentTimeMillis() - 365 * 24 * 60 * 60000) {
			endTime = System.currentTimeMillis() + 2 * defaultTimeoutMillis;
		}

		while (!spooledFileList.isCompleted() && System.currentTimeMillis() < endTime) {
			Thread.sleep(250);
		}

		if (!spooledFileList.isCompleted()) {
			if (listener.processCount_ == 0) {
				// No response yet received. Kill connection
				out.println(nestLevel + "Killing connection");
				Job jobs[] = as400.getJobs(AS400.PRINT);
				for (int i = 0; i < jobs.length; i++) {
					Job thisJob = jobs[i];
					out.println(nestLevel + "*** Ending job " + thisJob);
					thisJob.end(0);
				}
				as400.disconnectService(AS400.PRINT);
				throw new Exception("ConnectionDropped TIMEOUT");
			} else {
				spooledFileList.waitForListToComplete();
			}
		}
	}

	String nestLevel_ = "";
	boolean deleteAll_ = false;
	boolean deleteUseless_ = false;
	Hashtable deleteUserIds_ = null;
	long deleteCount_ = 0;
	long deleteBytes_ = 0;
	long keepCount_ = 0;
	long keepBytes_ = 0;
	long processCount_ = 0;
	CharConverter charConverter_ = null;
	AS400 as400_;
	private PrintStream out;

	public JDCleanSplf(PrintStream out, AS400 as400, String nestLevel, boolean deleteAll, Hashtable deleteUserIds) {
		this.as400_ = as400;
		this.nestLevel_ = nestLevel;
		this.deleteAll_ = deleteAll;
		deleteUserIds_ = deleteUserIds;
		deleteCount_ = 0;
		deleteBytes_ = 0;
		keepCount_ = 0;
		keepBytes_ = 0;
		processCount_ = 0;
		this.out = out;
		try {
			charConverter_ = new CharConverter(37);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JDCleanSplf(PrintStream out, AS400 as400, String nestLevel) {
		this.as400_ = as400;
		deleteUseless_ = true;
		this.nestLevel_ = nestLevel;
		this.deleteAll_ = false;
		deleteUserIds_ = new Hashtable();
		deleteCount_ = 0;
		deleteBytes_ = 0;
		keepCount_ = 0;
		keepBytes_ = 0;
		processCount_ = 0;
		this.out = out;
		try {
			charConverter_ = new CharConverter(37);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listClosed(PrintObjectListEvent e) {
		out.println(nestLevel_ + "listClosed:" + e);
	}

	public long getDeleteCount() {
		return deleteCount_;
	}

	public long getDeleteBytes() {
		return deleteBytes_;
	}

	public long getKeepCount() {
		return keepCount_;
	}

	public long getKeepBytes() {
		return keepBytes_;
	}

	public long getProcessCount() {
		return processCount_;
	}

	public void listCompleted(PrintObjectListEvent e) {
		out.println(nestLevel_ + "listCompleted:" + e);
	}

	public void listErrorOccurred(PrintObjectListEvent e) {
		out.println(nestLevel_ + "listErrorOccurred:" + e);
	}

	public void listOpened(PrintObjectListEvent e) {
		out.println(nestLevel_ + "listOpened:" + e);

	}

	StringBuffer keepReason = new StringBuffer();

	public void listObjectAdded(PrintObjectListEvent e) {
		if (verbose) {
			out.println(nestLevel_ + "listObjectAdded:" + e);
		}
		keepReason.setLength(0);
		processCount_++;
		if (processCount_ % 1000 == 0) {
			out.println(nestLevel_ + "  processCount=" + processCount_ + " deleteCount=" + deleteCount_ + " keepCount="
					+ keepCount_);
			/* Check the number of jobs and delete extra if needed */

			Job jobs[] = as400_.getJobs(AS400.PRINT);
			if (jobs.length > 10) {
				out.println(nestLevel_ + "ending too many jobs=" + jobs.length);
				for (int i = 0; i < jobs.length; i++) {
					Job thisJob = jobs[i];
					out.println(nestLevel_ + "Ending to many " + i + " of " + jobs.length + "  " + thisJob);
					try {
						thisJob.end(0);
					} catch (Exception ex2) {
						ex2.printStackTrace();
					}
				}
				as400_.disconnectService(AS400.PRINT);
				out.println(nestLevel_ + "jobs ended");
			}
		}
		PrintObject printObject = e.getObject();
		if (printObject instanceof SpooledFile) {

			boolean doDelete = false;
			SpooledFile spooledFile = (SpooledFile) printObject;
			if (deleteAll_) {
				doDelete = true;
			} else {

				String jobUser = spooledFile.getJobUser();
				if (jobUser == null) {
					out.println("Error:  jobUser is null");
				} else if (deleteUserIds_.get(jobUser) != null) {
					doDelete = true;
				} else if (deleteUseless_) {
					PrintObjectPageInputStream pageInputStream = null;
					try {
						int pageCount = spooledFile.getSingleIntegerAttribute(PrintObject.ATTR_PAGES).intValue();
						String fileName = spooledFile.getName();
						if (pageCount <= 100) {
							pageInputStream = spooledFile.getPageInputStream(null);
							byte[] buffer = new byte[8192];
							int dataLength = pageInputStream.read(buffer);
							// Need to translate from EBCDIC
							String data = charConverter_.byteArrayToString(buffer, 0, dataLength);
							if (fileName.equals("QPJOBLOG")) {

								// Check for messages indicating a useless spool file
								// CPF2523 No job log information.
								if (data.indexOf("CPF2523") >= 0) {
									doDelete = true;
								} else if (onlyNoiseMessages(data, keepReason)) {
									doDelete = true;
								}
								pageInputStream.close();
								pageInputStream = null;
							} else {
								/* Not a job log */
								if (fileName.equals("QPRINT")) {
									if (hasQprintNoise(data)) {
										doDelete = true;
									} else {
										int substringLen = data.length();
										if (substringLen > 80) {
											substringLen = 80;
										}
										keepReason.append("No QPRINT noise: "
												+ data.substring(0, substringLen).replace('\u0000', ' '));
									}
								} else {
									if (hasNoiseFileName(fileName)) {
										doDelete = true;
									} else {
										keepReason.append("Not noise file name: " + fileName);
									}
								}
							}
						} else {
							keepReason.append("Page count is " + pageCount);
						} /* not lest than 100 */
					} catch (Exception e1) {
						synchronized (out) {
							out.println("Warning: Exception caught ");
							e1.printStackTrace(out);
						}
						if (pageInputStream != null) {
							try {
								pageInputStream.close();
							} catch (IOException e2) {
								synchronized (out) {
									out.println("Error closing pageInputStream");
									e2.printStackTrace(out);
								}
							}
						}
					}
				}
			}
			if (doDelete) {
				if (verbose) {
					out.println(nestLevel_ + "Deleting " + spooledFile.getCreateDate() + " "
							+ spooledFile.getCreateTime() + " " + spooledFile.getName() + " " + spooledFile.getNumber()
							+ " " + spooledFile.getJobName() + " " + spooledFile.getJobUser() + " "
							+ spooledFile.getJobNumber());
				}
				try {

					deleteBytes_ += spooledFile.getIntegerAttribute(PrintObject.ATTR_SPLF_SIZE).intValue()
							* spooledFile.getIntegerAttribute(PrintObject.ATTR_SPLF_SIZE_MULT).intValue();
					spooledFile.delete();
					deleteCount_++;

				} catch (Exception e1) {
					Timestamp ts = new Timestamp(System.currentTimeMillis());
					String message = e1.toString();
					if ((message.indexOf("no longer in the system") > 0) || (message.indexOf("CPF334") > 0)) {
						/* Just ignore these messages */
					} else {
						synchronized (out) {
							out.println(nestLevel_ + " " + ts.toString() + " Delete failed");
							e1.printStackTrace(out);
						}
					}
				}
			} else {
				try {
					keepBytes_ += spooledFile.getIntegerAttribute(PrintObject.ATTR_SPLF_SIZE).intValue()
							* spooledFile.getIntegerAttribute(PrintObject.ATTR_SPLF_SIZE_MULT).intValue();
				} catch (Exception e1) {
					synchronized (out) {
						out.println(nestLevel_ + " Size query failed");
						e1.printStackTrace(out);
					}
				}
				keepCount_++;

				if (verbose) {
					out.println(
							nestLevel_ + "Keeping  " + spooledFile.getCreateDate() + " " + spooledFile.getCreateTime()
									+ " FILE(" + spooledFile.getName() + ")" + " SPLNBR(" + spooledFile.getNumber()
									+ ") " + " JOB(" + spooledFile.getJobNumber() + "/" + spooledFile.getJobUser() + "/"
									+ spooledFile.getJobName() + ")  REASON:" + keepReason.toString());
				}
			}

		} else {
			out.println(nestLevel_ + "Object is " + printObject);
		}

	}

	String[] noiseFileNames = { "ALLTYPES", "ARRAYRS", "CACHING", "CACHING2", "CKEYCUST", "CMPLXKEY", "CUST",
			"DBCSTEST", "DDMCC1", "DDMCC2", "DDMCC3", "DDMCCK1", "DDMCCK2", "DDMFIX", "DDMLOCK", "DDMSER", "DECTEST",
			"F1M1RW", "F2M1RW", "FEWROWS", "FILE1", "FILE2", "FILEC", "GCLL", "GETRF", "GMNAME", "JDCSC11PGM",
			"JDCSC26PGM", "JDCSC27PGM", "JDCSC28PGM", "JDCSC29PGM", "JDCSC30PGM", "JDCSC31PGM", "JDCSCBLPGM",
			"JDCSCLPGM", "JDCSCP2PGM", "JDCSCPGM", "JDCSCPPPGM", "JDCSRPGDAT", "JDCSRPGPG2", "JDCSRPGPG3", "JDCSRPGPG4",
			"JDCSRPGPGM", "JDCSRPGTIM", "JOBSQL",

			"KEYFILE", "KEYSRC", "KEYSRC2", "KEYSRC3", "LOGICAL1", "LONGFIL", "MLTFMT", "MYCUSTCDT", "NODATA", "NOKEY",
			"P9936798", "P9946152", "P9946152B", "PDZ410F1", "PDZ410F2", "QPRTLIBL", "QPDSPJOB", "READKEY1", "READKEY2",
			"READKEY3", "READRN", "RTEST", "SETRF", "SHORTCOL", "SHORTFILE", "SKEYCUST", "SMPLKEY", "TEST", "TEST7036",
			"TESTBIN8", "THDTST0", "THDTST1", "THDTST2", "THDTST3", "UPDATE1", "UPDATE10", "UPDATE11", "UPDATE13",
			"UPDATE14", "UPDATE15", "UPDATE16", "UPDATE17", "UPDATE18", "UPDATE2", "UPDATE20", "UPDATE21", "UPDATE22",
			"UPDATE23", "UPDATE24", "UPDATE25", "UPDATE26", "UPDATE28", "UPDATE29", "UPDATE30", "UPDATE4", "UPDATE5",
			"UPDATE6", "UPDATE8", "UPDATE9", "V107", "V11", "V12", "V15", "V16", "V17", "V19", "V20", "V21", "V22",
			"V23", "V24", "V25", "V26", "V27", "V28", "V29", "V3", "V30", "V31", "V32", "V33", "V34", "V35", "V36",
			"V37", "V38", "V39", "V4", "V40", "V41", "V42", "V43", "V44", "V45", "V46", "V47", "V48", "V49", "V50",
			"V51", "V52", "V53", "V54", "V55", "V56", "V57", "V58", "V59", "V60", "V61", "V62", "V63", "V65", "V7",
			"V8", "VSQLBUT", "WRITEK14", "WRITEK15", "WRITEV14", "WRITEV15", "WRITEV35",

	};

	boolean hasNoiseFileName(String fileName) {
		for (int i = 0; i < noiseFileNames.length; i++) {
			if (fileName.equals(noiseFileNames[i])) {
				return true;
			}
		}

		return false;
	}

	private boolean hasQprintNoise(String data) {
		String[] qprintNoise = { "Listening for transport dt_socket", "added manifest", " A normal program ",
				" loopdead loopdead loopdead ", " start to sleep 10s done",
				" Internal spool control file not accessible.", "THE SQL COMMAND COMPLETED SUCCESSFULLY",
				"Starting Deployment", "Starting JobQueue", "submitted to job queue", "Error found creating directory",
				" Error found opening file", "Output queue changed to QPRINT", "CPF2110:  Library",
				"CPF7020:  Journal receivers", "CPF2125:  No objects deleted.", "CZS0607:  Module",
				"CPC2201:  Object authority granted.",

		};

		for (int i = 0; i < qprintNoise.length; i++) {
			if (data.indexOf(qprintNoise[i]) >= 0) {
				return true;
			}
		}

		return false;
	}

// Does the data only contain noise messages
	static String[][] prefixesAndNoise = { { "MCH", }, { "SQL", "SQL0104", /* token not valid */
			"SQL0117", /* wrong number of values */
			"SQL0189", /* CCSID not valid */
			"SQL0204", /* not found */
			"SQL0301", /* input variable not valid */
			"SQL0332", /* character conversion not valid */
			"SQL0335", /* conversion resulted in substition */
			"SQL0387", /* no additional result sets */
			"SQL0440", /* Routine not found */
			"SQL0443", /* trigger program detected an error */
			"SQL0595", /* commit level escalated */
			"SQL0601", /* already exists */
			"SQL0843", /* connection to rdb does not exist */
			"SQL0950", /*
						 * database not in RDBDIRE" /* SQL0952 processing of statement ended
						 */
			"SQL5016", /* qualified object name not valid */
			"SQL7022", /* user not same as current user */
			"SQL7908", "SQL792A", /* schema not created */

			}, { "CPF", "CPF0001", /* error found on command */
					"CPF0920", /* prestart jobs ending */
					"CPF1124", /* Job entered system */
					"CPF1164", "CPF1175", /* subsystem cannot autostart job */
					"CPF1275", /* Subsystem &1 cannot allocate device &2. */
					"CPF1301", /* ACGDTA not journaled */
					"CPF1302", "CPF1303", "CPF2103", /* library already exists */
					"CPF2105", /* object not found */
					"CPF2111", /* library already exists */
					"CPF2130", /* Objects duplicated */
					"CPF2204", /* user profile not found */
					"CPF22E2", /* password not correct */
					/* "CPF22E3", user profile is disabled */
					/* "CPF24A3", value fo call stack parameter not valid */
					"CPF3202", /* file in use */
					"CPF3423", /* job queue not release */
					"CPF3485", "CPF338C", /* Internal spool control file not accessible. */
					"CPF3596", /* PTF numbers in select/omit list not permitted */
					"CPF3635", /* PTF superceeded */
					/* CPF3698 dump output directed to spool file */
					/* CPF4404 file already closed */
					"CPF5009", /* duplicate record key */
					/* "CPF5034" Duplicate key on access PATH */
					"CPF5035", /* data mapping error */
					"CPF9162", /* cannot establish DDM connection */
					"CPF9190", /* authority failure on DDM connection attempt */
					"CPF9861", /* Output file &1 created in library &2. */
					"CPF9862", /* member added */
					"CPF9898", /* General escape message */
					"CPFB9C6", /* pase ended */

			}, { "CPC", "CPC0905", /* Subsystem &3 prestart job entry not active */
					"CPI0982", /* changed but may be adjusted */
					"CPC1129", /* job changed */
					"CPC1134", /* shared pool changed */
					"CPC1165", /* sigterm signal sent */
					/* "CPC1166", time limit reached for SIGTERM handler */
					"CPC1207", /* subsystem ending immediately */
					"CPC1221", /* Job &3/&2/&1 submitted */
					/* "CPC1224" Job Ended abnormally */
					"CPC1234", /* ended from job queue */
					"CPC1602", /* Subsystem description changed */
					"CPC2103", /* object changed */
					"CPC2130", /* objects duplicated */
					"CPC2191", /* file deleted */
					"CPC2196", /* library added to library list */
					"CPC2197", /* Library removed from library list */
					"CPC2198", /* Current library changed */
					"CPC221B", /* object changed */
					"CPC2605", /* varyon completed */
					"CPC2609", /* varyon completed */
					"CPC2957", /* no records copied */
					"CPC2958", /* All records copied */
					"CPC2983", /* data in member reorganized */
					"CPI6609", /* Alert processing ended */
					"CPC7301", /* file created */
					"CPC7305", /* member added */
					"CPC9801", /* object created */
					"CPCA083", /* Directory created */
					"CPCA087", /* objects copied */
					"CPCA980", "CPCA981", "CPCA984", /* trace option changed" */
					"CPCA986", /* user trace dumped */
			}, { "CPI", "CPI0952", /* Start of prestart jobs in progress */

					"CPI1125", /* job submitted */
					"CPI2101", /* object create */
					"CPI2218", /* Authority revoked */
					"CPI32E8", /* Trigger was changed */
					"CPI7BC4", /* Alert processing started on &1 at &2. */
					"CPI8911", /* Target display station pass-through */
					"CPI8912", /* Target display station pass-through ser */
					"CPI907F", /* shadow controller not started */
					"CPI9160", /* Database connection started over TCP/IP. */
					"CPI9161", /* Database connection ended over TCP/IP. */
					"CPI9162", /* Target job assigned to handle DDM connection */
					"CPIAD08", /* Host servers communication error */

			},

			{ "JVA", "JVAB302", "JVAB578", /* JVM properties loaded from file */
			}, { "QSH", "QSH0005", /* Command ended normally */
			}, { "*NO", "*NONE", },

	};

	boolean onlyNoiseMessages(String data, StringBuffer keepReason1) {
		// Make sure it is a JOBLOG
		for (int i = 0; i < prefixesAndNoise.length; i++) {
			String prefix = prefixesAndNoise[i][0];

			int dataLength = data.length();
			int searchOffset = 0;
			while (searchOffset >= 0) {
				searchOffset = data.indexOf(prefix, searchOffset);
				if (searchOffset > 0) {
					if (searchOffset + 8 < dataLength) {
						String message = extractMessage(data, searchOffset);
						if (message != null) {
							boolean foundMessage = false;
							for (int j = 1; (!foundMessage) && (j < prefixesAndNoise[i].length); j++) {
								if (message.equals(prefixesAndNoise[i][j])) {
									foundMessage = true;
								}
							}
							if (!foundMessage) {
								// We found a message that was not noise -- bail out right away
								if (verbose) {
									keepReason1.append("Message " + message + " was not noise");
									out.println("Message " + message + " was not noise");
									int lastOffset = searchOffset + 800;
									if (lastOffset > data.length())
										lastOffset = data.length();
									String printString = data.substring(searchOffset, lastOffset).replace('\u0001',
											'\n');
									out.println("        " + printString);
								}
								return false;
							}
						}
						searchOffset = searchOffset + 8;
					} else {
						searchOffset = -1;
					}
				}
			}
		}
		return true;
	}

	private String extractMessage(String data, int searchOffset) {
		char prefix = data.charAt(searchOffset - 1);
		if (prefix == ' ' || prefix == 0x0001) {
			if ((data.charAt(searchOffset + 7) == ' ') && isMessageDigit(data.charAt(searchOffset + 3))
					&& isMessageDigit(data.charAt(searchOffset + 4)) && isMessageDigit(data.charAt(searchOffset + 5))
					&& isMessageDigit(data.charAt(searchOffset + 6))) {
				return data.substring(searchOffset, searchOffset + 7);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private boolean isMessageDigit(char charAt) {
		if (charAt >= '0' && charAt <= '9')
			return true;
		if (charAt >= 'A' && charAt <= 'Z')
			return true;
		return false;
	}

}

class JDCleanSplfResults {
	public long deleteCount;
	public long deleteBytes;
	public long keepCount;
	public long keepBytes;

	public long processedCount;
	public long processSeconds;

	public JDCleanSplfResults(long deleteCount, long deleteBytes, long keepCount, long keepBytes, long processedCount,
			long processSeconds) {
		this.deleteCount = deleteCount;
		this.deleteBytes = deleteBytes;
		this.keepCount = keepCount;
		this.keepBytes = keepBytes;
		this.processedCount = processedCount;
		this.processSeconds = processSeconds;
	}

	public JDCleanSplfResults(JDCleanSplfResults a, JDCleanSplfResults b) {
		this.deleteCount = a.deleteCount + b.deleteCount;
		this.deleteBytes = a.deleteBytes + b.deleteBytes;
		this.keepCount = a.keepCount + b.keepCount;
		this.keepBytes = a.keepBytes + b.keepBytes;
		this.processedCount = a.processedCount + b.processedCount;
		this.processSeconds = a.processSeconds + b.processSeconds;
	}

	public JDCleanSplfResults(JDCleanSplfResults a, JDCleanSplfResults b, JDCleanSplfResults c, JDCleanSplfResults d) {
		this.deleteCount = a.deleteCount + b.deleteCount + c.deleteCount + d.deleteCount;
		this.deleteBytes = a.deleteBytes + b.deleteBytes + c.deleteBytes + d.deleteBytes;
		this.keepCount = a.keepCount + b.keepCount + c.keepCount + d.keepCount;
		this.keepBytes = a.keepBytes + b.keepBytes + c.keepBytes + d.keepBytes;
		this.processedCount = a.processedCount + b.processedCount + c.processedCount + d.processedCount;
		this.processSeconds = a.processSeconds + b.processSeconds + c.processSeconds + d.processSeconds;
	}
}
