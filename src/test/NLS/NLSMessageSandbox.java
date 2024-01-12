///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSMessageSandbox.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NLS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.MessageQueue;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.QSYSObjectPathName;

import test.MessageSandbox;
import test.Testcase;

/**
 * The MessageSandbox class provides some common routines for testing messages
 * in the visual components.
 **/
class NLSMessageSandbox extends MessageSandbox {

  /**
   * Setting this to false can make testing much quicker, but it eliminates
   * cleanup.
   **/
  // public boolean cleanup = true;

  /**
   * Debugging messages.
   **/
  // public boolean debug = false;

  // Private data.
  // private MessageQueue queue_ = null;

  String dbcs_string5 = Testcase.getResource("IFS_DBCS_STRING5");
  String dbcs_string10 = Testcase.getResource("IFS_DBCS_STRING10");
  String dbcs_string50 = Testcase.getResource("IFS_DBCS_STRING50");

  /**
   * Constructor.
   **/
  public NLSMessageSandbox(AS400 system, String library, String queueName, String userId) {
    super(system, library, queueName, userId);
    // Create the library if necessary.
    String libraryName = library;
    CommandCall cmd = new CommandCall(system, "CRTLIB LIB(" + libraryName
        + ") TEXT(\'" + dbcs_string50 + "\')");
    try {
      cmd.run();

      if (debug) {
        AS400Message[] messageList = cmd.getMessageList();
        for (int i = 0; i < messageList.length; ++i)
          System.out.println(messageList[i]);
      }
    } catch (Exception e) {
      if (debug)
        System.out.println("Could not create library  " + libraryName + ".");
    }

    String path = QSYSObjectPathName.toPath(libraryName, queueName, "MSGQ");

    // Create the message queue if necessary.
    cmd = new CommandCall(system, "CRTMSGQ MSGQ(" + libraryName + "/"
        + queueName + ") " + "TEXT(\'" + dbcs_string50 + "\')");
    try {
      cmd.run();

      if (debug) {
        AS400Message[] messageList = cmd.getMessageList();
        for (int i = 0; i < messageList.length; ++i)
          System.out.println(messageList[i]);
      }

      queue_ = new MessageQueue(system, path);
    } catch (Exception e) {
      if (debug)
        System.out.println("Could not create message queue " + path + ".");
    }

  }

  /**
   * Pad (or truncate) a string to desired number of characters (left justify).
   **/
  public String padToLength(String string, int desiredLength) {
    if (string.length() == desiredLength)
      return string;
    else if (string.length() > desiredLength)
      return string.substring(desiredLength);
    else {
      int diff = desiredLength - string.length();
      StringBuffer buf = new StringBuffer(string);
      for (int i = 0; i < diff; i++)
        buf.append(" ");
      return buf.toString();
    }
  }

  /**
   * Cause a Program Message to be sent.
   **/
  public void sendProgramMessage(String msgID, String msgFileName,
      MessageQueue replyQueue) {
    try {

      // Create the path to the program.

      QSYSObjectPathName programName = new QSYSObjectPathName("QSYS",
          "QMHSNDM", "PGM");

      // Create the program call object. Assocate the object with the
      // AS400 object that represents the AS/400 we get status from.

      ProgramCall prog1 = new ProgramCall(queue_.getSystem());

      // Create the program parameter list. This program has ten
      // parameters that will be added to this list.

      ProgramParameter[] parmlist = new ProgramParameter[10];

      // Parameter 1 is the Message identifier. It is a string input
      // parameter. Set the string value, convert it to AS/400 format,
      // then add the parameter to the parm list.

      AS400Text text1 = new AS400Text(7);
      byte[] messageID = text1.toBytes(padToLength(msgID.toUpperCase(), 7));
      parmlist[0] = new ProgramParameter(messageID);

      // Parameter 2 is the Qualified message file name. It is a string input
      // parameter. Set the string value, convert it to AS/400 format,
      // then add the parameter to the parm list.

      String msgFile10 = padToLength(msgFileName.toUpperCase(), 10);
      String lib10 = padToLength("*LIBL", 10);
      AS400Text text2 = new AS400Text(20);
      byte[] msgFile = text2.toBytes(msgFile10 + lib10);
      parmlist[1] = new ProgramParameter(msgFile);

      // Parameter 3 is the Message data or immediate text. It is a data input
      // parameter. Set the string value, convert it to AS/400 format,
      // then add the parameter to the parm list.

      byte[] msgData = new byte[32];
      parmlist[2] = new ProgramParameter(msgData, 0);

      // Parameter 4 is the buffer size of parm 3. It is a numeric input
      // parameter. Sets its value to 32, convert it to AS/400 format,
      // then add the parm to the parm list.

      AS400Bin4 bin4 = new AS400Bin4();
      Integer iMsgDataLength = new Integer(32);
      byte[] msgDataLength = bin4.toBytes(iMsgDataLength);
      parmlist[3] = new ProgramParameter(msgDataLength);

      // Parameter 5 is the Message type parameter. It is a string input
      // parameter. Set the string value, convert it to AS/400 format,
      // then add the parameter to the parm list.

      AS400Text text5 = new AS400Text(10);
      byte[] statusFormat = text5.toBytes("*INFO     ");
      parmlist[4] = new ProgramParameter(statusFormat);

      // Parameter 6 is the List of qualified message queue names. It is a
      // string input
      // parameter. Set the string value, convert it to AS/400 format,
      // then add the parameter to the parm list.

      QSYSObjectPathName pathName = new QSYSObjectPathName(queue_.getPath());
      String queueName20 = padToLength(pathName.getObjectName(), 10)
          + padToLength(pathName.getLibraryName(), 10);

      AS400Text text6 = new AS400Text(20);
      byte[] queueName = text6.toBytes(queueName20);
      parmlist[5] = new ProgramParameter(queueName);

      // Parameter 7 is the Number of message queues. It is a numeric input
      // parameter. Sets its value to 1, convert it to AS/400 format,
      // then add the parm to the parm list.

      Integer iNumMsgQueues = new Integer(1);
      byte[] numMsgQueues = bin4.toBytes(iNumMsgQueues);
      parmlist[6] = new ProgramParameter(numMsgQueues);

      // Parameter 8 is the Qualified name of the reply message queue.
      // It is a string input
      // parameter. Set the string value, convert it to AS/400 format,
      // then add the parameter to the parm list.

      QSYSObjectPathName replyPathName = new QSYSObjectPathName(
          replyQueue.getPath());
      String replyQueueName20 = padToLength(replyPathName.getObjectName(), 10)
          + padToLength(replyPathName.getLibraryName(), 10);

      byte[] replyQueueName = text2.toBytes(replyQueueName20);
      parmlist[7] = new ProgramParameter(replyQueueName);

      // The AS/400 program returns data in parameter 9. It is an output
      // parameter. Allocate 4 bytes for this parameter.

      parmlist[8] = new ProgramParameter(4);

      // Parameter 10 is the error info parameter. It is an input/output
      // parameter. Add it to the parm list.

      byte[] errorInfo = new byte[32];
      parmlist[9] = new ProgramParameter(errorInfo, 0);

      // Set the program to call and the parameter list to the program
      // call object.

      prog1.setProgram(programName.getPath(), parmlist);

      // Run the program

      if (prog1.run() != true) {

        // If the program did not run get the list of error messages
        // from the program object and display the messages. The error
        // would be something like program-not-found or not-authorized
        // to the program.

        AS400Message[] msgList = prog1.getMessageList();

        System.out.println("The program did not run.  AS/400 messages:");

        for (int i = 0; i < msgList.length; i++) {
          System.out.println(msgList[i].getText());
        }
      }

      /*
       * String queueName = null; parmlist_ = new ProgramParameter[10];
       * QSYSObjectPathName pathName = new QSYSObjectPathName
       * (queue_.getPath()); queueName = pathName.getLibraryName() + "/" +
       * pathName.getObjectName();
       * 
       * String cmdText = "SNDPGMMSG MSGID(" + msgID + ") " + "MSGF(" +
       * msgFilePath + ") " + "TOMSGQ(" + queueName+ ")";
       * System.out.println(cmdText);
       * 
       * // Send the message. CommandCall cmd = new CommandCall
       * (queue_.getSystem(), cmdText); try { cmd.run ();
       * 
       * if (debug) { AS400Message[] messageList = cmd.getMessageList (); for
       * (int i = 0; i < messageList.length; ++i) System.out.println
       * (messageList[i]); } } catch (Exception e) { if (debug)
       * System.out.println ("Could not create message queue " + queueName +
       * "."); }
       */
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Forces the queue to have a particular number of messages.
   **/
  public void setNumberOfMessages(int count) {
    try {
      queue_.remove();
      for (int i = 0; i < count; ++i)
        queue_.sendInformational(dbcs_string10 + " [" + i + "]");
    } catch (Exception e) {
      System.out.println("Could not set the number of messages on "
          + "message queue " + queue_.getPath() + ".");
    }
  }

}
