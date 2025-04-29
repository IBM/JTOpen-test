///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JobLogUtil.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobLog;
import com.ibm.as400.access.QueuedMessage;
import com.ibm.as400.access.list.SpooledFileListItem;
import com.ibm.as400.access.list.SpooledFileOpenList;

public class JobLogUtil {

  
  private static final int MAX_LINES = 10000;

  public static String getSpooledJobLogToFile(String hostname, String userid, char[] password, String serverJob, String directory) throws Exception {
  
    AS400 as400 = new AS400(hostname, userid, password ); 
    String joblog = getSpooledJobLogToFile(as400, serverJob, directory); 
    as400.disconnectAllServices(); 
    return joblog; 
    
  }
  
  
  
  
  
  public static String getSpooledJobLogToFile(AS400 as400, String serverJob, String directory) throws Exception {
    return getSpooledJobLogToFile(as400, serverJob, directory, null); 
  }
  
  public static String getSpooledJobLogToFile(AS400 as400, String serverJob, String directory, String[] ignoreLines) throws Exception {
    String outFile = null; 
      if (serverJob == null) { 
        throw new Exception("serverJob is null"); 
      }
      int firstSlash = serverJob.indexOf("/"); 
      if (firstSlash == -1) { 
        throw new Exception("invalid job name "+serverJob); 
      }
      int secondSlash = serverJob.indexOf("/", firstSlash+1); 
      if (secondSlash == -1) { 
        throw new Exception("invalid job name "+serverJob); 
      }
      String number = serverJob.substring(0,firstSlash); 
      String user = serverJob.substring(firstSlash+1,secondSlash); 
      String name = serverJob.substring(secondSlash+1); 
      
      SpooledFileOpenList list = new SpooledFileOpenList(as400);
      list.setFilterJobInformation(name, user, number);
      list.open();
      Enumeration<?> items = list.getItems();
      int retry = 10; 
      while (items.hasMoreElements() && retry > 0)
      {
        try { 
          SpooledFileListItem item = (SpooledFileListItem)items.nextElement();
          String filename = item.getName(); 
          if (filename.equals("QPJOBLOG")) {
            outFile = cpysplfToFile(as400, item,directory);
          }
        } catch (java.util.NoSuchElementException ex) {
          // Just ignore 
          retry --; 
        }
      }
      list.close();

      as400.disconnectAllServices(); 
      
      
      return outFile;  
      
  }
  
  /* Copy the spooled file to the specified directory and the name of the file */  

  public static String cpysplfToFile(AS400 as400, SpooledFileListItem item, String directory) throws AS400SecurityException, ErrorCompletingRequestException, IOException, InterruptedException, PropertyVetoException {
    return cpysplfToFile(as400,item,directory,null); 
  }
  
  public static String cpysplfToFile(AS400 as400, SpooledFileListItem item, String directory, String[] ignoreLines) throws AS400SecurityException, ErrorCompletingRequestException, IOException, InterruptedException, PropertyVetoException {
    // StringBuffer sb = new StringBuffer(); 
    String name = item.getName(); 
    int number = item.getNumber(); 
    String jobName = item.getJobName(); 
    String jobUser = item.getJobUser();
    String jobNumber = item.getJobNumber();
    String filename = directory+"/"+name+"."+number+"."+jobName+"."+jobUser+"."+jobNumber+".txt"; 
    String commandString; 
    CommandCall commandCall = new CommandCall(as400); 
    commandString = "QSH CMD('rm -f "+filename+"2; rm -f "+filename+"; touch -C 819 "+filename+"')";
    commandCall.run(commandString); 
    
    
    commandString = "CPYSPLF FILE("+name+") TOFILE(*TOSTMF) JOB("+jobNumber+"/"+jobUser+"/"+jobName+") SPLNBR("+number+") TOSTMF('"+filename+"2')  STMFOPT(*REPLACE)   ";    
    commandCall.run(commandString);
    
    // TODO:  Remove the ignore messages   
    commandString = "QSH CMD('cat "+filename+"2 > "+filename+"')";
    commandCall.run(commandString); 

    return filename; 
  }

  
  public static String getJobLog(String hostname, String userid, char[] password, String serverJob) throws Exception {
    AS400 as400 = new AS400(hostname, userid, password ); 
    String joblog = getJobLog(as400, serverJob); 
    as400.disconnectAllServices();
    return joblog; 
  }
  
  
  public static String getJobLog(AS400 as400, String serverJob) throws Exception {
    String name;
    String user;
    String number;
    StringBuffer sb = new StringBuffer();
    
    sb.append("Getting joblog for "+serverJob+" on "+as400.getSystemName()+"\n"); 
    if (serverJob == null) { 
      throw new Exception("serverJob is null"); 
    }
    int firstSlash = serverJob.indexOf("/"); 
    if (firstSlash == -1) { 
      throw new Exception("invalid job name "+serverJob); 
    }
    int secondSlash = serverJob.indexOf("/", firstSlash+1); 
    if (secondSlash == -1) { 
      throw new Exception("invalid job name "+serverJob); 
    }
    number=serverJob.substring(0,firstSlash); 
    user = serverJob.substring(firstSlash+1,secondSlash); 
    name = serverJob.substring(secondSlash+1); 
    
    
    JobLog joblog = new JobLog(as400,name,user,number); 
    
    joblog.clearAttributesToRetrieve(); 
    //joblog.addAttributeToRetrieve(JobLog.ALERT_OPTION );
    //joblog.addAttributeToRetrieve(JobLog.CCSID_CONVERSION_STATUS_DATA );
    //joblog.addAttributeToRetrieve(JobLog.CCSID_CONVERSION_STATUS_TEXT); 
    //joblog.addAttributeToRetrieve(JobLog.CCSID_FOR_DATA );
    //joblog.addAttributeToRetrieve(JobLog.CCSID_FOR_TEXT );
    //joblog.addAttributeToRetrieve(JobLog.DEFAULT_REPLY); 
    //joblog.addAttributeToRetrieve(JobLog.MESSAGE );
    // joblog.addAttributeToRetrieve(JobLog.MESSAGE_FILE_LIBRARY_USED); 
    //joblog.addAttributeToRetrieve(JobLog.MESSAGE_HELP );
    //joblog.addAttributeToRetrieve(JobLog.MESSAGE_HELP_WITH_FORMATTING_CHARACTERS); 
    joblog.addAttributeToRetrieve(JobLog.MESSAGE_HELP_WITH_REPLACEMENT_DATA );
    //joblog.addAttributeToRetrieve(JobLog.MESSAGE_HELP_WITH_REPLACEMENT_DATA_AND_FORMATTING_CHARACTERS); 
    joblog.addAttributeToRetrieve(JobLog.MESSAGE_WITH_REPLACEMENT_DATA); 
    joblog.addAttributeToRetrieve(JobLog.RECEIVING_MODULE_NAME );
    joblog.addAttributeToRetrieve(JobLog.RECEIVING_PROCEDURE_NAME );
    joblog.addAttributeToRetrieve(JobLog.RECEIVING_PROGRAM_NAME );
    joblog.addAttributeToRetrieve(JobLog.RECEIVING_STATEMENT_NUMBERS); 
    joblog.addAttributeToRetrieve(JobLog.RECEIVING_TYPE );
    //joblog.addAttributeToRetrieve(JobLog.REPLACEMENT_DATA); 
    //joblog.addAttributeToRetrieve(JobLog.REPLY_STATUS );
    joblog.addAttributeToRetrieve(JobLog.REQUEST_LEVEL );
    joblog.addAttributeToRetrieve(JobLog.REQUEST_STATUS); 
    joblog.addAttributeToRetrieve(JobLog.SENDER_TYPE );
    joblog.addAttributeToRetrieve(JobLog.SENDING_MODULE_NAME );
    joblog.addAttributeToRetrieve(JobLog.SENDING_PROCEDURE_NAME );
    joblog.addAttributeToRetrieve(JobLog.SENDING_PROGRAM_NAME );
    joblog.addAttributeToRetrieve(JobLog.SENDING_STATEMENT_NUMBERS); 
    joblog.addAttributeToRetrieve(JobLog.SENDING_USER_PROFILE);
    
    
    Enumeration<QueuedMessage> messages = joblog.getMessages(); 
    int maxLines = MAX_LINES; 
    while (messages.hasMoreElements() && maxLines > 0 ) {
      maxLines --; 
      try { 
        QueuedMessage message = (QueuedMessage) messages.nextElement(); 
        formatMessage(message, sb);
      } catch (java.util.NoSuchElementException ex) {
        sb.append("\n\n ---- WARNING -- hit "+ex+"\n\n" );
        maxLines = maxLines - 100; 
      }
    }

    if (maxLines == 0) {
      sb.append(" -- HIT maximum line count of "+MAX_LINES+"\n"); 
    }
    sb.append("----------- END OF JOB LOG for "+number+"/"+user+"/"+name+" ---------------------\n");
    // Change the job so it leaves something behind
    try { 
       Job job = new Job(as400, name, user, number);
       job.setLoggingLevel(4); 
       job.setLoggingSeverity(00); 
       job.setLoggingText(Job.LOGGING_TEXT_SECLVL); 
    } catch (Exception e) {
      sb.append("\n\n"); 
      sb.append("Warning:  Changing job failed with the following\n");
      JDTestcase.printStackTraceToStringBuffer(e, sb);
      sb.append("\n"); 
    }
    
    return sb.toString(); 
  }

  private static void formatMessage(QueuedMessage message, StringBuffer sb) {
    
  // Sample messages
  //  MSGID      TYPE                    SEV  DATE      TIME             FROM PGM     LIBRARY     INST     TO PGM      LIBRARY     INST
  //  CPF1124    Information             00   01/08/19  15:52:57.228665  QWTPIIPP     QSYS        04CC     *EXT                    *N
  //                                       Message . . . . :   Job 076431/EBERHARD/QPADEV000F started on 01/08/19 at
  //                                         15:52:57 in subsystem QINTER in QSYS. Job entered system on 01/08/19 at
  //                                         15:52:57.
  //  CPF2451    Information             40   01/08/19  15:52:57.267266  QMHCHMSQ     QSYS        0B04     *EXT                    *N
  //                                       Message . . . . :   Message queue EBERHARD is allocated to another job.
  //                                       Cause . . . . . :   If this message was received when trying to sign on, the
  //                                        most probable cause is that you are already signed on to another work
    
    // TODO:  Look at QGYOLJBL API to see why I am not getting program information. 
      String id = message.getID(); 
      if (id != null && id.length() > 0) { 
          sb.append(id);
      } else {
        sb.append("*NONE"); 
      }
      sb.append(" "); 
      sb.append(formatType(message.getType())); 
      sb.append(" "); 
      sb.append(message.getSeverity()); 
      sb.append(" "); 
      sb.append(message.getDate().getTime());
      sb.append(" ");
      String fromProgram= message.getFromProgram();
      fromProgram = terminateAtNull(fromProgram); 
      sb.append(fromProgram);
      
      String sendingModule = message.getSendingModuleName();
      sendingModule = terminateAtNull(sendingModule); 
      if (sendingModule != null && sendingModule.length() > 0) { 
        sb.append("/"); 
        sb.append(sendingModule); 
      }
      String sendingProcedureName = message.getSendingProcedureName();
      sendingProcedureName = terminateAtNull(sendingProcedureName); 
      if (sendingProcedureName != null && sendingProcedureName.length() > 0) {
        sb.append("/"); 
        sb.append(sendingProcedureName); 
      }
      java.lang.String[]  sendingStatementNumbers = message.getSendingStatementNumbers(); 
      if (sendingStatementNumbers != null) { 
        for (int i = 0; i < sendingStatementNumbers.length && i < 10 ; i++) {
          sb.append(" #");
	  String sendingStatementNumber = terminateAtNull(sendingStatementNumbers[i]); 
          sb.append(sendingStatementNumber); 
        }
      }
      
      sb.append(" "); 
      sb.append(terminateAtNull(message.getReceivingProgramName()));
      String receivingModule = terminateAtNull(message.getReceivingModuleName());
      if (receivingModule != null && receivingModule.length() > 0) { 
        sb.append("/"); 
        sb.append(receivingModule); 
      }
      String receivingProcedureName = terminateAtNull( message.getReceivingProcedureName());
      if (receivingProcedureName != null && receivingProcedureName.length() > 0) {
        sb.append("/"); 
        sb.append(receivingProcedureName); 
      }
      sb.append(" "); 
     java.lang.String[]  receiverStatementNumbers = message.getReceiverStatementNumbers(); 
      if (receiverStatementNumbers != null) { 
        for (int i = 0; i < receiverStatementNumbers.length && i < 10; i++) {
          sb.append(" #");
	  String number = terminateAtNull(receiverStatementNumbers[i]);
          sb.append(number); 
        }
      }
      sb.append("\n");
      sb.append("  Message: ");
      sb.append(terminateAtNull(message.getText()));
      sb.append("\n"); 
      String cause = terminateAtNull(message.getMessageHelpReplacement()); 
      if (cause != null && cause.length() > 0) { 
        sb.append(" "); 
        sb.append(cause); 
        sb.append("\n"); 
      }
    
      
  }

  // If the string contains null, then the output is somehow corrupted. 
  // terminate at null to limit the amount of data that is returned. 
  private static String terminateAtNull(String s) {
    int nullIndex = s.indexOf('\u0000');
    if (nullIndex < 0) { 
      return s; 
    } else  if (nullIndex == 0) {
      return ""; 
    } else {
      return s.substring(0,nullIndex); 
    }
  }


  private static String formatType(int type) {
   switch(type) { 

       case AS400Message.COMPLETION: return "COMPLETION"; 
       case AS400Message.DIAGNOSTIC: return "DIAGNOSTIC"; 
       case AS400Message.INFORMATIONAL: return "INFORMATIONAL"; 
       case AS400Message.INQUIRY: return "INQUIRY"; 
       case AS400Message.SENDERS_COPY: return "SENDERS_COPY"; 
       case AS400Message.REQUEST: return "REQUEST"; 
       case AS400Message.REQUEST_WITH_PROMPTING: return "REQUEST_WITH_PROMPTING"; 
       case AS400Message.NOTIFY: return "NOTIFY"; 
       case AS400Message.ESCAPE: return "ESCAPE"; 
       case AS400Message.REPLY_NOT_VALIDITY_CHECKED: return "REPLY_NOT_VALIDITY_CHECKED"; 
       case AS400Message.REPLY_VALIDITY_CHECKED: return "REPLY_VALIDITY_CHECKED"; 
       case AS400Message.REPLY_MESSAGE_DEFAULT_USED: return "REPLY_MESSAGE_DEFAULT_USED"; 
       case AS400Message.REPLY_SYSTEM_DEFAULT_USED: return "REPLY_SYSTEM_DEFAULT_USED"; 
       case AS400Message.REPLY_FROM_SYSTEM_REPLY_LIST : return "REPLY_FROM_SYSTEM_REPLY_LIST ";
       default: return "UNKNOWN"; 
   
   }
  }

  public static void joblogFileToTable(String filename, String tablename) throws Exception {
    Connection connection = DriverManager.getConnection("jdbc:default:connection");
    PrintStream out = new PrintStream(new FileOutputStream("/tmp/JobLogUtil.txt")); 
    joblogFileToTable(filename,connection,tablename,out); 
  }
  
  
  public static void joblogFileToTable(String filename, Connection connection, String tablename, PrintStream out) {

      String createSql = "CREATE OR REPLACE TABLE "+tablename+" ( "+
	"ORDINAL_POSITION FOR COLUMN ORDIN00001 INTEGER DEFAULT NULL , "+
	"MESSAGE_ID VARCHAR(7) CCSID 37 DEFAULT NULL , "+
	"MESSAGE_TYPE FOR COLUMN MESSA00001 VARCHAR(13) CCSID 37 DEFAULT NULL , "+
	"MESSAGE_SUBTYPE FOR COLUMN MESSA00002 VARCHAR(22) CCSID 37 DEFAULT NULL , "+
	"SEVERITY SMALLINT DEFAULT NULL , "+
	"MESSAGE_TIMESTAMP FOR COLUMN MESSA00003 TIMESTAMP DEFAULT NULL , "+
	"FROM_LIBRARY FOR COLUMN FROM_00001 VARCHAR(10) CCSID 37 DEFAULT NULL , "+
	"FROM_PROGRAM FOR COLUMN FROM_00002 VARCHAR(256) CCSID 37 DEFAULT NULL , "+
	"FROM_MODULE FOR COLUMN FROM_00003 VARCHAR(10) CCSID 37 DEFAULT NULL , "+
	"FROM_PROCEDURE FOR COLUMN FROM_00004 VARCHAR(4096) CCSID 37 DEFAULT NULL , "+
	"FROM_INSTRUCTION FOR COLUMN FROM_00005 VARCHAR(10) CCSID 37 DEFAULT NULL , "+
	"TO_LIBRARY VARCHAR(10) CCSID 37 DEFAULT NULL , "+
	"TO_PROGRAM VARCHAR(10) CCSID 37 DEFAULT NULL , "+
	"TO_MODULE VARCHAR(10) CCSID 37 DEFAULT NULL , "+
	"TO_PROCEDURE FOR COLUMN TO_PR00001 VARCHAR(4096) CCSID 37 DEFAULT NULL , "+
	"TO_INSTRUCTION FOR COLUMN TO_IN00001 VARCHAR(10) CCSID 37 DEFAULT NULL , "+
	"FROM_USER VARCHAR(10) CCSID 37 DEFAULT NULL , "+
	"MESSAGE_LIBRARY FOR COLUMN MESSA00004 VARCHAR(10) CCSID 37 DEFAULT NULL , "+
	"MESSAGE_FILE FOR COLUMN MESSA00005 VARCHAR(10) CCSID 37 DEFAULT NULL , "+
	"MESSAGE_TOKEN_LENGTH FOR COLUMN MESSA00006 SMALLINT DEFAULT NULL , "+
	"MESSAGE_TOKENS FOR COLUMN MESSA00007 VARCHAR(2048) FOR BIT DATA DEFAULT NULL , "+
	"MESSAGE_TEXT FOR COLUMN MESSA00008 DBCLOB(8192) CCSID 1200 DEFAULT NULL , "+
	"MESSAGE_SECOND_LEVEL_TEXT FOR COLUMN MESSA00009 VARGRAPHIC(4096) CCSID 1200 DEFAULT NULL , "+
	"MESSAGE_KEY FOR COLUMN MESSA00010 BINARY(4) DEFAULT NULL," +
	"THREAD VARCHAR(20) DEFAULT NULL  )   "; 
	  

      try {
	  Statement stmt = connection.createStatement();
	  stmt.executeUpdate(createSql);
	  stmt.executeUpdate("delete from "+tablename); 
	  PreparedStatement pstmt = connection.prepareStatement(
	      "INSERT INTO "+tablename+" ("
	          + "MESSAGE_ID,MESSAGE_TYPE,SEVERITY,MESSAGE_TIMESTAMP,"
	          + "FROM_PROGRAM,FROM_LIBRARY,FROM_INSTRUCTION,"
	          + "TO_PROGRAM,TO_LIBRARY,TO_INSTRUCTION, THREAD, MESSAGE_TEXT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"); 

	  File file = new File(filename);

	  if (!file.exists()) {
	      throw new Exception("File "+file+" does not exist"); 
	  }
	  
	  if (file.isDirectory() ) {
	    throw new Exception("File "+file+" is a directory"); 
	  }

	  BufferedReader reader = new BufferedReader(new FileReader(file)); 
	  String line = reader.readLine(); 
	  
	  StringBuffer sb = new StringBuffer(); 
	  String[] currentSectionInfo = new String[11]; 
	  String thread=""; 
	  while (line != null) { 
	   if (!skipJoblogLine(line)) {
	     String[] sectionInfo = getSectionInfo(line); 
	     if (sectionInfo != null ) {
	       if (sb.length() > 0) { 
	         doInsert(pstmt, 
	                  currentSectionInfo[0],
                    currentSectionInfo[1],
                    currentSectionInfo[2],
                    currentSectionInfo[3],
                    currentSectionInfo[4],
                    currentSectionInfo[5],
                    currentSectionInfo[6],
                    currentSectionInfo[7],
                    currentSectionInfo[8],
                    currentSectionInfo[9],
                    thread, 
                    sb.toString() 
	                  ); 
	         sb.setLength(0);
	         thread=""; 
	       }
	       currentSectionInfo = sectionInfo; 
	     } else {
	       int threadIndex = line.indexOf("Thread  . . . . :"); 
	       if (threadIndex > 0) { 
	         thread = line.substring(threadIndex+18).trim(); 
	       } else { 
	         sb.append(" "); 
	         sb.append(line.trim());
	       }
	     }
	     
	   } 
	   line = reader.readLine();  
	  }
	  reader.close(); 
    if (sb.length() > 0) { 
      doInsert(pstmt, 
          currentSectionInfo[0],
          currentSectionInfo[1],
          currentSectionInfo[2],
          currentSectionInfo[3],
          currentSectionInfo[4],
          currentSectionInfo[5],
          currentSectionInfo[6],
          currentSectionInfo[7],
          currentSectionInfo[8],
          currentSectionInfo[9],
          thread,
          sb.toString() 
          ); 
      sb.setLength(0);
      
    }

	  
	  
	  stmt.close(); 
	  pstmt.close(); 
	  
	  
      } catch (Exception ex) {
	  out.println("Exception caught processing "+filename+" to "+tablename);
	  ex.printStackTrace(out); 
      } 
      



  } 



  private static void doInsert(PreparedStatement pstmt, 
      String string1,
      String string2, 
      String string3, 
      String string4, 
      String string5,
      String string6, 
      String string7, 
      String string8, 
      String string9,
      String string10, 
      String string11,
      String string12) throws SQLException {

      pstmt.setString(1, string1);
      pstmt.setString(2, string2);
      pstmt.setString(3, string3);
      pstmt.setString(4, string4);
      pstmt.setString(5, string5);
      pstmt.setString(6, string6);
      pstmt.setString(7, string7);
      pstmt.setString(8, string8);
      pstmt.setString(9, string9);
      pstmt.setString(10, string10);
      pstmt.setString(11, string11);
      pstmt.setString(12, string12);
      pstmt.execute(); 
  }



  private static String[] sectionKeys = {
    "  Diagnostic              ",
    "  Escape                  ",
    "  Information             ",
  }; 
  
  // Get the information from the start of a section. 
  private static String[] getSectionInfo(String line) {
    boolean sectionKeyFound = false; 
    for (int i = 0; i < sectionKeys.length && (!sectionKeyFound); i++) { 
      if (line.indexOf(sectionKeys[i]) >= 0) {
        sectionKeyFound = true; 
      }
    }
    if (sectionKeyFound) { 
      // MSGID      TYPE                    SEV  DATE      TIME             FROM PGM     LIBRARY     INST     TO PGM      LIBRARY     INST
      // 0000000000111111111122222222223333333333444444444455555555556666666666777777777788888888889999999999000000000011111111112222222222
      // 0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
      // *NONE      Information                  01/21/20  05:19:21.651389  QDBRSYNC     QSYS        *STMT    QMRDBESYNC  QSYS        *STMT  
      // SQL7905    Information             20   01/21/20  05:19:39.339553  QSQCRTT      QSYS        *STMT    QSQCRTT     QSYS        *STMT  

      String[] sectionInfo = new String[10]; 
      String msgid = line.substring(0,10).trim();
      String type =  line.substring(11,30).trim(); 
      String sev =   line.substring(35,39).trim(); 
      if (sev.length() == 0) { 
        sev = "0"; 
      }
      String date =  line.substring(40,48).trim(); 
      String time =  line.substring(50,65).trim();
      String fromPgm = line.substring(67,79).trim(); 
      String fromLib = line.substring(80,91).trim();
      String fromInst = line.substring(92,100).trim(); 
      String toPgm = line.substring(101,112).trim(); 
      String toLib = line.substring(113,122).trim();
      String toInst = line.substring(125).trim();

      sectionInfo[0] = msgid;
      sectionInfo[1] = type ;
      sectionInfo[2] = sev;
      sectionInfo[3] = "20"+date.substring(6,8)+"-"+date.substring(0,2)+"-"+date.substring(3,5)+" "+time; 
      sectionInfo[4] = fromPgm;
      sectionInfo[5] = fromLib;
      sectionInfo[6] = fromInst ;
      sectionInfo[7] = toPgm ; 
      sectionInfo[8] = toLib;
      sectionInfo[9] = toInst;
      
      return sectionInfo; 
    } else { 
      return null;
    }
  }




  private static String[] skipLines = {
      "5770SS1 V",
      "Job name . . . . . . . . . . :   ",
      "Job description  . . . . . . :   ",
      "MSGID      TYPE                    SEV  DATE      TIME             FROM PGM     LIBRARY     INST     TO PGM      LIBRARY     INST",   
      "From module . . . . . . . . :  ",                                                        
      "From procedure  . . . . . . :  ",                                                  
      "Statement . . . . . . . . . :  ",                                                   
      "To module . . . . . . . . . :  ",                                                 
      "To procedure  . . . . . . . :  ",                                               
      "Statement . . . . . . . . . :  ",                                             
      
  }; 
  
  private static boolean skipJoblogLine(String line) {
    for (int i = 0; i < skipLines.length; i++) { 
      if (line.indexOf(skipLines[i])>= 0 ) return true; 
    }

    return false;
  }

  /**
   * Save the joblog as a file that be accessed with the web page
   * Returns a string with a message indicating where the joblog can be found
   * @throws Exception 
   */

  public static String publishJoblog(String jobname, String joblog)    throws Exception {
    if (joblog == null) {
      return "joblog is null and cannot be saved";
    }

    File dir = new File(JTOpenTestEnvironment.testcaseHomeDirectory+"/ct/debugdata");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    String filename = jobname.replace('/', '.') + ".txt";
    File outfile = new File(JTOpenTestEnvironment.testcaseHomeDirectory+"/ct/debugdata/" + filename);
    int count = 0;
    while (outfile.exists()) {
      count++;
      filename = jobname.replace('/', '.') + "." + count + ".txt";
      outfile = new File(JTOpenTestEnvironment.testcaseHomeDirectory+"/ct/debugdata/" + filename);
    }

    PrintWriter writer = new PrintWriter(new FileWriter(outfile));

    writer.println(joblog);
    writer.close();
    InetAddress localHost = InetAddress.getLocalHost();
    String hostname =localHost.getHostName();
    if (hostname.indexOf(".") < 0 ) { 
      hostname = hostname+"."+JTOpenTestEnvironment.getDefaultClientDomain();  
    }
    return "...Joblog for " + jobname + " available at http://"
        + hostname + ":6050/debugdata/" + filename;
  }

  public static String publishConnectionJoblog(Connection connection) {
      String sql = "";
      try { 
	  Statement stmt=connection.createStatement();
	  sql =  "VALUES JOB_NAME"; 
	  ResultSet rs = stmt.executeQuery(sql);
	  rs.next();
	  String jobName =  rs.getString(1);
	  String jobNumber = jobName.substring(0,6);

	  sql = "CALL QSYS2.QCMDEXC('QSH CMD(''system \"dspjoblog job("+jobName+")\" > /tmp/joblog."+jobNumber+"'')')";
	  stmt.execute(sql);

	  sql = "VALUES GET_CLOB_FROM_FILE('/tmp/joblog."+jobNumber+"')";
	  rs =  stmt.executeQuery(sql);
	  rs.next();
	  String joblog = rs.getString(1);
	  stmt.close(); 
	  return publishJoblog(jobName, joblog); 


      } catch (Exception e) {
	  System.out.println("Error on sql="+sql);
	  e.printStackTrace(System.out); 
	     return "ERROR "+e; 
      } 


  } 
  


  public static void main (String[] args) {
    try { 
      try { 
          Class.forName("com.ibm.as400.access.AS400JDBCDriver"); 
      } catch (Exception e) { 
          // Ignore
      }
    
      String hostname = args[0];
      if (hostname.equals("JOBLOGTOFILE")) { 
        String filename = args[1]; 
        hostname = args[2]; 
        String userid = args[3]; 
        String password = args[4];
        String tableName = args[5]; 
        
        Class.forName("com.ibm.as400.access.AS400JDBCDriver"); 
        System.out.println("Getting connection to "+hostname); 
        Connection connection = DriverManager.getConnection("jdbc:as400:"+hostname,userid,password);
        System.out.println("Converting "+filename+" to "+tableName); 
        joblogFileToTable(filename, connection, tableName, System.out) ; 

        System.out.println("DONE"); 
        
      } else if (hostname.equals("PUBLISHCONNECTIONJOBLOG")) {
	  String url = args[1];
	  String userid = args[2]; 
	  String password = args[3];
	  try {

	      Connection c = DriverManager.getConnection(url,userid,password);
	      String answer = publishConnectionJoblog(c);
	      System.out.println(answer);

	  } catch (Exception e) {
	      System.out.println("Usage:  test.JobLogUtil PUBLISHCONNECTIONJOBLOG url userid password"); 
	      e.printStackTrace(); 
	  } 
        
      } else { 

        String userid = args[1]; 
        char[] password = args[2].toCharArray();
        String serverJob = args[3]; 

        
        String filename = getSpooledJobLogToFile(hostname, userid, password, serverJob, "/tmp");
      System.out.println("Job log dumped to "+filename);
      
      BufferedReader br = new BufferedReader(new FileReader(filename)); 
      String line = br.readLine(); 
      System.out.println("---- Begin joblog --------------");
      while (line != null) { 
        System.out.println(line); 
        line = br.readLine(); 
      }
      System.out.println("---- End joblog --------------");
      br.close(); 
      }
    } catch (Exception e) { 
      e.printStackTrace(); 
      System.out.println("Usage:  java test.JobLogUtil <hostname> <userid> <password> <serverjob>");
      System.out.println("        java test.JobLogUtil JOBLOGTOFILE <filename> <hostname> <userid> <password> <tablename>"); 
    }
    
  }

}
