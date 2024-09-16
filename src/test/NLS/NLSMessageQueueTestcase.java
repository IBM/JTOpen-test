///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSMessageQueueTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NLS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.MessageQueue;
import com.ibm.as400.access.QueuedMessage;

import test.Testcase;

import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Testcase NLSMessageQueueTestcase.
 * 
 * <p>
 * This tests the following MessageQueue methods:
 * <ul>
 * <li>ctors
 * <li>getLength()
 * <li>getMessages()
 * <li>getPath()
 * <li>getSelection()
 * <li>getSeverity()
 * <li>getSystem()
 * <li>receive()
 * <li>remove()
 * <li>reply()
 * <li>sendInformational()
 * <li>sendInquiry()
 * <li>setPath()
 * <li>setSelection()
 * <li>setSeverity
 * <li>setSystem()
 * </ul>
 **/
public class NLSMessageQueueTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NLSMessageQueueTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NLSTest.main(newArgs); 
   }

  private static boolean DEBUG = false;

  // Private data.
  private static final int variations_ = 11;
  private boolean DBCS_mode = false;

  private NLSMessageSandbox sandbox_ = null;
  private NLSMessageSandbox sandboxReply_ = null;

  String nl_string2 = getResource("DDM_STRING2");
  String nl_string4 = getResource("DDM_STRING4");
  String nl_string10 = getResource("DDM_STRING10");
  String nl2_string10 = getResource("DQ_DESC10");

  String dbcs_string5 = getResource("IFS_DBCS_STRING5");
  String dbcs_string10 = getResource("IFS_DBCS_STRING10");
  String dbcs_string50 = getResource("IFS_DBCS_STRING50");

  String dbcs_words0 = getResource("DDM_STRING4");
  String dbcs_words1 = getResource("JDBC_DBCS_WORDS1");
  String dbcs_words2 = getResource("JDBC_DBCS_WORDS2");
  String dbcs_words3 = getResource("JDBC_DBCS_WORDS3");
  String dbcs_words4 = getResource("JDBC_DBCS_WORDS4");
  String dbcs_words5 = getResource("JDBC_DBCS_WORDS5");
  String dbcs_words6 = getResource("JDBC_DBCS_WORDS6");
  String dbcs_words7 = getResource("JDBC_DBCS_WORDS7");
  String dbcs_words8 = getResource("JDBC_DBCS_WORDS8");
  String dbcs_words9 = getResource("JDBC_DBCS_WORDS9");
  String dbcs_words10 = getResource("JDBC_DBCS_WORDS10");
  String dbcs_words11 = getResource("JDBC_DBCS_WORDS11");
  String dbcs_words12 = getResource("JDBC_DBCS_WORDS12");
  String dbcs_words13 = getResource("JDBC_DBCS_WORDS13");
  String dbcs_words14 = getResource("JDBC_DBCS_WORDS14");
  String dbcs_words15 = getResource("JDBC_DBCS_WORDS15");
  String dbcs_words16 = getResource("JDBC_DBCS_WORDS16");
  String dbcs_words17 = getResource("JDBC_DBCS_WORDS17");
  String dbcs_words18 = getResource("JDBC_DBCS_WORDS18");
  String dbcs_words19 = getResource("JDBC_DBCS_WORDS19");
  String dbcs_words20 = getResource("JDBC_DBCS_WORDS20");

  /**
   * Constructor.
   **/
  public NLSMessageQueueTestcase(AS400 systemObject, Vector variationsToRun,
      int runMode, FileOutputStream fileOutputStream) {
    super(systemObject, "NLSMessageQueueTestcase", variations_,
        variationsToRun, runMode, fileOutputStream);
  }

  /**
   * Runs the variations.
   **/
  public void run() {
    sandbox_ = new NLSMessageSandbox(systemObject_, testLib_, "MQT", systemObject_.getUserId());
    sandboxReply_ = new NLSMessageSandbox(systemObject_, testLib_, "MQTREPLY", systemObject_.getUserId());

    String specificName = systemObject_.getUserId().substring(4);
    if (!specificName.equals("JAP") && !specificName.equals("KOR")
        && !specificName.equals("TCH") && !specificName.equals("SCH")) { // SBCS
                                                                         // language
                                                                         // here
      DBCS_mode = false;
    } else { // DBCS language here
      DBCS_mode = true;
    }

    boolean allVariations = (variationsToRun_.size() == 0);

    if ((allVariations || variationsToRun_.contains("1"))
        && runMode_ != ATTENDED) {
      setVariation(1);
      Var001();
    }
    if ((allVariations || variationsToRun_.contains("2"))
        && runMode_ != ATTENDED) {
      setVariation(2);
      Var002();
    }

    if ((allVariations || variationsToRun_.contains("3"))
        && runMode_ != ATTENDED) {
      setVariation(3);
      Var003();
    }

    if ((allVariations || variationsToRun_.contains("4"))
        && runMode_ != ATTENDED) {
      setVariation(4);
      Var004();
    }

    if ((allVariations || variationsToRun_.contains("5"))
        && runMode_ != ATTENDED) {
      setVariation(5);
      Var005();
    }

    if ((allVariations || variationsToRun_.contains("6"))
        && runMode_ != ATTENDED) {
      setVariation(6);
      Var006();
    }

    if ((allVariations || variationsToRun_.contains("7"))
        && runMode_ != ATTENDED) {
      setVariation(7);
      Var007();
    }

    if ((allVariations || variationsToRun_.contains("8"))
        && runMode_ != ATTENDED) {
      setVariation(8);
      Var008();
    }

    if ((allVariations || variationsToRun_.contains("9"))
        && runMode_ != ATTENDED) {
      setVariation(9);
      Var009();
    }

    if ((allVariations || variationsToRun_.contains("10"))
        && runMode_ != ATTENDED) {
      setVariation(10);
      Var010();
    }

    if ((allVariations || variationsToRun_.contains("11"))
        && runMode_ != ATTENDED) {
      setVariation(11);
      Var011();
    }

    sandbox_.cleanup();
    sandboxReply_.cleanup();
  }

  /**
   * reply() - Replying to a nonexistant message should throw an exception.
   * Based on MessageQueueTestcase Var037.
   **/
  public void Var001() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();
      byte[] key = new byte[4]; // $$$BTW set value here
      f.reply(key, dbcs_string10);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
    }
  }

  /**
   * reply() - When a particular message is replied to, it is cleared from the
   * queue and is no longer there. A second reply to the original message throws
   * an exception. Based on MessageQueueTestcase Var038.
   **/
  public void Var002() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      MessageQueue fReply = new MessageQueue(systemObject_, sandboxReply_
          .getQueue().getPath());

      f.remove(); // remove old messages
      fReply.remove(); // remove old messages

      String inquiryText = "[Inquiry] " + dbcs_string50 + dbcs_string10;
      String replyText = "[Reply] " + dbcs_string5;

      f.sendInquiry(inquiryText, sandboxReply_.getQueue().getPath());

      if (DEBUG) {
        Enumeration e = f.getMessages();
        while (e.hasMoreElements()) {
          QueuedMessage message = (QueuedMessage) e.nextElement();

          System.out.print("Key: " + message.getKey());
          System.out.println(" -> " + message.getText());
        }
        System.out.println("");
      }

      Enumeration e = f.getMessages();
      while (e.hasMoreElements()) {
        QueuedMessage message2 = (QueuedMessage) e.nextElement();
        // Verify that the message text is correct.
        if (!message2.getText().equals(inquiryText)) {
          failed("Incorrect text in inquiry message: " + message2.getText());
          return;
        }

        String expected;
        if (message2.getID() == null) {
          System.out.println("ID is null");
          expected = inquiryText;
        } else {
          System.out.println("ID is: |" + message2.getID() + "|");
          expected = message2.getID() + " " + inquiryText;
        }
        expected = expected.trim(); // ??? TBD: Should this be necessary?
        if (!message2.toString().equals(expected)) {
          failed("Incorrect result from toString(): \n" + message2.toString()
              + "\n" + "Expected: \n" + expected);
          return;
        }

        f.reply(message2.getKey(), replyText);
        f.reply(message2.getKey(), replyText); // should exception here.
      }
      failed("Did not throw exception.");

      // Verify that the reply message is correct.

      e = fReply.getMessages();
      while (e.hasMoreElements()) {
        QueuedMessage message2 = (QueuedMessage) e.nextElement();
        if (!message2.getText().equals(replyText)) {
          failed("Incorrect text in reply message: " + message2.getText());
          return;
        }
      }
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
    }
  }

  /**
   * reply() - When a particular message is replied to, it is cleared from the
   * queue and is no longer there. As a side effect of reply, the messages are
   * located in the reply queue. Based on MessageQueueTestcase Var039.
   **/
  public void Var003() {
    final int messageCount = 10;
    boolean succeeded = true;

    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      MessageQueue fReply = new MessageQueue(systemObject_, sandboxReply_
          .getQueue().getPath());

      String messageList[] = new String[messageCount];
      String replyList[] = new String[messageCount];

      f.remove(); // remove old messages
      fReply.remove(); // remove old messages

      // Send messages (alternating: Informational and Inquiry)
      int tracker = 0;
      int replyTrack = 0;
      for (int count = 0; count < messageCount; count++) {
        String tempString = new String("message sent as number " + count + "] "
            + dbcs_string50);
        String infoText = "[Informational " + tempString;
        String inquiryText = "[Inquiry       " + tempString;
        if (count % 2 == 0) {
          messageList[tracker] = infoText;
          f.sendInformational(messageList[tracker]);
          tracker++;

        } else {
          f.sendInquiry(inquiryText, sandboxReply_.getQueue().getPath());
        }
      }

      // Check that the messages got sent.
      Enumeration e = f.getMessages();
      for (int count = 0; e.hasMoreElements(); count++) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        if (DEBUG) {
          System.out.print("Key: " + message.getKey());
          System.out.println(" -> " + message.getText());
        }

        String replyText = "[Reply] " + dbcs_string10;
        String messageText = message.getText();
        if (messageText.startsWith("[Inquiry")) {
          replyList[replyTrack++] = messageText;
          replyList[replyTrack] = replyText + messageText;

          if (DEBUG) {
            System.out.println("count:  " + replyTrack);
            System.out.println("Reply: key: " + message.getKey() + " "
                + replyList[replyTrack]);
          }

          f.reply(message.getKey(), replyList[replyTrack]);
          replyTrack++;
        }
      }
      if (DEBUG) {
        System.out.println("");
      }

      Enumeration e2 = f.getMessages();
      for (int count = 0; e2.hasMoreElements(); count++) {
        QueuedMessage message = (QueuedMessage) e2.nextElement();

        if (DEBUG) {
          System.out.print(message.getText());
          System.out.println(" -> " + messageList[count]); // @A1C
        }
        if (!message.getText().equals(messageList[count])) // @A1C
        {
          succeeded = false;
          if (DEBUG == false) {
            break;
          }
        }
      }

      if (DEBUG) {
        System.out.println("");
        System.out.println("Following are reply queue messages");
        System.out.println("");
      }

      Enumeration eReply = fReply.getMessages();
      for (int count = 0; eReply.hasMoreElements(); count++) {
        QueuedMessage message = (QueuedMessage) eReply.nextElement();
        if (DEBUG) {
          System.out.println(message.getText() + " -> " + replyList[count]);
          System.out.println("");
        }

        if (!message.getText().equals(replyList[count])) {
          succeeded = false;
          if (DEBUG == false) {
            break;
          }
        }
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * sendInformational() - When the message queue is cleared and a message is
   * sent to it, the message arrives successfully. Based on MessageQueueTestcase
   * Var040.
   **/
  public void Var004() {
    int count = 0;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();
      String infoText = dbcs_string50;
      f.sendInformational(infoText);

      Enumeration e = f.getMessages();
      QueuedMessage message = null;
      while (e.hasMoreElements()) {
        message = (QueuedMessage) e.nextElement();
        count++;
      }

      if (DEBUG) {
        System.out.println("length:   (1) " + f.getLength());
      }

      assertCondition((f.getLength() == 1) && (count == 1) && (message != null)
          && message.getText().equals(infoText));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * sendInformational() - When the message queue is cleared and a message is
   * sent to it, the message text is retrieved successfully. Based on
   * MessageQueueTestcase Var041.
   **/
  public void Var005() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      String message = "My message: " + dbcs_string50;
      f.remove();
      f.sendInformational(message);

      QueuedMessage TheMessage = null;
      Enumeration e = f.getMessages();
      while (e.hasMoreElements()) {
        TheMessage = (QueuedMessage) e.nextElement();
      }

      assertCondition((TheMessage != null)
          && TheMessage.getText().equals(message));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * sendInformational() - When the message queue is cleared and multiple
   * messages are sent to it, the message text of each is retrieved
   * successfully. Based on MessageQueueTestcase Var042.
   **/
  public void Var006() {
    final int messageCount = 10;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      String messageList[] = new String[messageCount];
      for (int count = 0; count < messageCount; count++) {
        messageList[count] = new String("Info msg # " + count + ": "
            + dbcs_string50);
      }
      f.remove();
      for (int count = 0; count < messageList.length; count++) {
        if (DEBUG) {
          System.out.println(messageList[count]);
        }

        f.sendInformational(messageList[count]);
      }

      Enumeration e = f.getMessages();
      boolean succeeded = true;
      for (int counter = 0; counter < messageCount; counter++) {
        QueuedMessage message = (QueuedMessage) e.nextElement();
        if (DEBUG) {
          System.out.println(message.getText() + " --> "
              + messageList[(messageCount - 1) - counter]);
        }

        if (!message.getText().equals(messageList[counter])) // @A1C
        {
          succeeded = false;
          break;
        }
      }
      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * sendInformational() - When the message queue is cleared and a message is
   * sent to it from an AS/400 file, the message arrives successfully.
   **/
  /*
   * Not relevant to NLS testing. -JPL public void Var043 () { try {
   * MessageQueue f = new MessageQueue (systemObject_, sandbox_.getQueue
   * ().getPath ()); f.remove (); f.sendInformational("CAE0002",
   * "/QSYS.LIB/QCPFMSG.MSGF"); f.sendInformational("CAE0005",
   * "/QSYS.LIB/QCPFMSG.MSGF"); f.sendInformational("CAE0009",
   * "/QSYS.LIB/QCPFMSG.MSGF");
   * 
   * Enumeration e = f.getMessages();
   * 
   * if (DEBUG) { System.out.println ("length   (3): " + f.getLength()); for
   * (int count=0; count<f.getLength();count++) { QueuedMessage message =
   * (QueuedMessage) e.nextElement(); System.out.println ("text: " +
   * message.getText()); } }
   * 
   * assertCondition (f.getLength () == 3); } catch (Exception e) { failed (e,
   * "Unexpected Exception"); } }
   */

  /**
   * sendInformational() - When a message is sent to the message queue with
   * substitution text, the message is delivered successfully. Based on
   * MessageQueueTestcase Var044.
   **/
  public void Var007() {
    boolean succeeded = true;
    int messageCount = 0;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();

      if (DBCS_mode == true) {
        messageCount = 1;
      } else { // SBCS
        messageCount = 10;
      }

      String[] substitutionList = new String[messageCount];
      if (DBCS_mode == true) { // DBCS test
        substitutionList[0] = new String(nl_string4 + "567890");
      } else { // SBCS test

        substitutionList[0] = new String(dbcs_words0);
        substitutionList[1] = new String(dbcs_words1);
        substitutionList[2] = new String(dbcs_words2);
        substitutionList[3] = new String(dbcs_words3);
        substitutionList[4] = new String(dbcs_words4);
        substitutionList[5] = new String(dbcs_words5);
        substitutionList[6] = new String(dbcs_words6);
        substitutionList[7] = new String(dbcs_words7);
        substitutionList[8] = new String(dbcs_words8);
        substitutionList[9] = new String(dbcs_words9);
      }

      for (int count = 0; count < substitutionList.length; count++) {
        AS400Text converter = new AS400Text(10, systemObject_.getCcsid(),
            systemObject_);
        f.sendInformational("CAE0014", "/QSYS.LIB/QCPFMSG.MSGF",
            converter.toBytes(substitutionList[count]));
      }

      Enumeration e = f.getMessages();
      for (int counter = 0; e.hasMoreElements(); counter++) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        if (message.getText().indexOf(substitutionList[counter]) < 0) // @A1C
        {
          succeeded = false;
          break;
        }
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * sendInformational() - When a message is sent to the message queue with
   * substitution text strings, the message is delivered successfully. Based on
   * MessageQueueTestcase Var045.
   **/
  public void Var008() {
    boolean succeeded = true;
    int messageCount = 0;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();

      if (DBCS_mode == true) {
        messageCount = 1;
      } else { // SBCS
        messageCount = 10;
      }

      String[] substitutionList = new String[messageCount];
      if (DBCS_mode == true) { // DBCS test
        substitutionList[0] = new String(nl_string4 + "567890123456");
      } else { // SBCS test
        substitutionList[0] = new String(dbcs_words0 + " " + dbcs_words10);
        substitutionList[1] = new String(dbcs_words1 + " " + dbcs_words11);
        substitutionList[2] = new String(dbcs_words2 + " " + dbcs_words12);
        substitutionList[3] = new String(dbcs_words3 + " " + dbcs_words13);
        substitutionList[4] = new String(dbcs_words4 + " " + dbcs_words14);
        substitutionList[5] = new String(dbcs_words5 + " " + dbcs_words15);
        substitutionList[6] = new String(dbcs_words6 + " " + dbcs_words16);
        substitutionList[7] = new String(dbcs_words7 + " " + dbcs_words17);
        substitutionList[8] = new String(dbcs_words8 + " " + dbcs_words18);
        substitutionList[9] = new String(dbcs_words9 + " " + dbcs_words19);
      }

      for (int count = 0; count < substitutionList.length; count++) {
        if (substitutionList[count].length() > 16)
          substitutionList[count] = substitutionList[count].substring(0, 16);
        AS400Text converter = new AS400Text(16, systemObject_.getCcsid(),
            systemObject_);
        f.sendInformational("CAE0005", "/QSYS.LIB/QCPFMSG.MSGF",
            converter.toBytes(substitutionList[count]));
      }

      Enumeration e = f.getMessages();
      for (int counter = 0; e.hasMoreElements(); counter++) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        String theMessage = message.getText();

        if (DBCS_mode == false) { // SBCS language here

          if (DEBUG) {
            System.out.println("message: <" + theMessage + ">");
            System.out.println("nl_string4 = <" + nl_string4 + ">");
            System.out.println("substitutionList["
                + ((messageCount - 1) - counter)
                + "] (#1 string) = <"
                + substitutionList[((messageCount - 1) - counter)].substring(0,
                    8).trim() + ">");
            System.out.println("substitutionList["
                + ((messageCount - 1) - counter)
                + "] (#2 string) = <"
                + substitutionList[((messageCount - 1) - counter)].substring(8)
                    .trim() + ">");
          }

          if ((theMessage.indexOf(substitutionList[counter].substring(0, 8)
              .trim()) < 0) // @A1C
              || (theMessage.indexOf(substitutionList[counter].substring(8)
                  .trim()) < 0)) // @A1C
          {
            succeeded = false;
            break;
          }
        } else { // DBCS language here

          if (DEBUG) {
            System.out.println("message: <" + theMessage + ">");
            System.out.println("nl_string4 = <" + nl_string4 + ">");
            System.out.println("substitutionList["
                + ((messageCount - 1) - counter)
                + "] (#1 string) = <"
                + substitutionList[((messageCount - 1) - counter)].substring(0,
                    5).trim() + ">");
            System.out.println("substitutionList["
                + ((messageCount - 1) - counter)
                + "] (#2 string) = <"
                + substitutionList[((messageCount - 1) - counter)].substring(5,
                    13).trim() + ">");
          }

          if ((theMessage
              .indexOf(substitutionList[(messageCount - 1) - counter]
                  .substring(0, 5).trim()) < 0)
              || (theMessage.indexOf(substitutionList[(messageCount - 1)
                  - counter].substring(5, 13).trim()) < 0)) {
            succeeded = false;
            break;
          }
        }
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * sendInquiry() - When a message is sent to the message queue with
   * substitution text, the message is delivered successfully. Based on
   * MessageQueueTestcase Var061.
   **/
  public void Var009() {
    boolean succeeded = true;
    int messageCount = 0;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();

      if (DBCS_mode == true) {
        messageCount = 1;
      } else { // SBCS
        messageCount = 10;
      }

      String[] substitutionList = new String[messageCount];
      if (DBCS_mode == true) { // DBCS test
        substitutionList[0] = new String(nl_string4 + "567890");
      } else { // SBCS test

        substitutionList[0] = new String(dbcs_words0);
        substitutionList[1] = new String(dbcs_words1);
        substitutionList[2] = new String(dbcs_words2);
        substitutionList[3] = new String(dbcs_words3);
        substitutionList[4] = new String(dbcs_words4);
        substitutionList[5] = new String(dbcs_words5);
        substitutionList[6] = new String(dbcs_words6);
        substitutionList[7] = new String(dbcs_words7);
        substitutionList[8] = new String(dbcs_words8);
        substitutionList[9] = new String(dbcs_words9);
      }

      for (int count = 0; count < substitutionList.length; count++) {
        AS400Text converter = new AS400Text(10, systemObject_.getCcsid(),
            systemObject_);
        f.sendInquiry("CAE0014", "/QSYS.LIB/QCPFMSG.MSGF", converter
            .toBytes(substitutionList[count]), sandboxReply_.getQueue()
            .getPath());
      }

      Enumeration e = f.getMessages();
      for (int counter = 0; e.hasMoreElements(); counter++) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        if (DEBUG) {
          AS400Text converter = new AS400Text(10, systemObject_.getCcsid(),
              systemObject_);
          System.out.println(message.getText() + " sub: "
              + converter.toBytes(substitutionList[counter])); // $$$BTW This is
                                                               // bad
        }

        if (message.getText().indexOf(substitutionList[counter]) < 0) // @A1C
        {
          succeeded = false;
          break;
        }
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * sendInquiry() - When a message is sent to the message queue with multiple
   * substitution text strings, the message is delivered successfully. Based on
   * MessageQueueTestcase Var062.
   **/
  public void Var010() {
    boolean succeeded = true;
    int messageCount = 0;
    if (DBCS_mode == true) {
      messageCount = 2;
    } else { // SBCS
      messageCount = 10;
    }

    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();

      String[] substitutionList = new String[messageCount];
      if (DBCS_mode == false) { // SBCS
        substitutionList[0] = new String(dbcs_words0 + " " + dbcs_words10);
        substitutionList[1] = new String(dbcs_words1 + " " + dbcs_words11);
        substitutionList[2] = new String(dbcs_words2 + " " + dbcs_words12);
        substitutionList[3] = new String(dbcs_words3 + " " + dbcs_words13);
        substitutionList[4] = new String(dbcs_words4 + " " + dbcs_words14);
        substitutionList[5] = new String(dbcs_words5 + " " + dbcs_words15);
        substitutionList[6] = new String(dbcs_words6 + " " + dbcs_words16);
        substitutionList[7] = new String(dbcs_words7 + " " + dbcs_words17);
        substitutionList[8] = new String(dbcs_words8 + " " + dbcs_words18);
        substitutionList[9] = new String(dbcs_words9 + " " + dbcs_words19);
      } else { // DBCS
        substitutionList[0] = new String(nl_string4 + nl_string4 + nl_string4
            + "12345678");
        substitutionList[1] = new String("12345678" + nl_string4 + nl_string4
            + nl_string4);
      }

      for (int count = 0; count < substitutionList.length; count++) {
        if (substitutionList[count].length() > 16)
          substitutionList[count] = substitutionList[count].substring(0, 16);
        AS400Text converter = new AS400Text(16, systemObject_.getCcsid(),
            systemObject_);
        byte[] substitutionData = converter.toBytes(substitutionList[count]);
        f.sendInquiry("CAE0005", "/QSYS.LIB/QCPFMSG.MSGF", substitutionData,
            sandboxReply_.getQueue().getPath());
      }

      Enumeration e = f.getMessages();
      if (!e.hasMoreElements())
        succeeded = false;
      for (int counter = 0; e.hasMoreElements(); counter++) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        String theMessage = message.getText();

        if (DEBUG) {
          System.out.println("Counter = <" + counter + ">");
          System.out.println("");
          System.out.println("message: <" + theMessage + ">");
          System.out.println("nl_string4 = <" + nl_string4 + ">");
        }

        if (DBCS_mode == false) { // SBCS

          if (DEBUG == true) {
            System.out.println("s0 substitutionList["
                + ((messageCount - 1) - counter)
                + "] (#1 string) = <"
                + substitutionList[((messageCount - 1) - counter)].substring(0,
                    8).trim() + ">");
            System.out.println("s0 substitutionList["
                + ((messageCount - 1) - counter)
                + "] (#2 string) = <"
                + substitutionList[((messageCount - 1) - counter)].substring(8)
                    .trim() + ">");
          }

          if ((theMessage.indexOf(substitutionList[counter].substring(0, 8)
              .trim()) < 0) // @A1C
              || (theMessage.indexOf(substitutionList[counter].substring(8)
                  .trim()) < 0)) // @A1C
          {
            succeeded = false;
            break;
          }
        } else { // DBCS
          if (counter == 0) {

            if (DEBUG) {
              System.out.println("d1 substitutionList["
                  + /* (messageCount - 1) - */counter
                  + "] (#1 string) = <"
                  + substitutionList[/* (messageCount -1) - */counter]
                      .substring(0, 3).trim() + ">");
              System.out.println("d1 substitutionList["
                  + /* (messageCount - 1) - */counter
                  + "] (#2 string) = <"
                  + substitutionList[/* (messageCount -1) - */counter]
                      .substring(3).trim() + ">");
            }

            if ((theMessage
                .indexOf(substitutionList[/* (messageCount - 1) - */counter]
                    .substring(0, 3).trim()) < 0)
                || (theMessage
                    .indexOf(substitutionList[/* (messageCount - 1) - */counter]
                        .substring(3).trim()) < 0)) {
              succeeded = false;
              break;
            }
          } else if (counter == 1) {
            if (DEBUG) {
              System.out.println("d2 substitutionList["
                  + /* (messageCount - 1) - */counter
                  + "] (#1 string) = <"
                  + substitutionList[/* (messageCount -1) - */counter]
                      .substring(0, 8).trim() + ">");
              System.out.println("d2 substitutionList["
                  + /* (messageCount - 1) - */counter
                  + "] (#2 string) = <"
                  + substitutionList[/* (messageCount -1) - */counter]
                      .substring(8).trim() + ">");
            }

            if ((theMessage
                .indexOf(substitutionList[/* (messageCount - 1) - */counter]
                    .substring(0, 8).trim()) < 0)
                || (theMessage
                    .indexOf(substitutionList[/* (messageCount - 1) - */counter]
                        .substring(8).trim()) < 0)) {
              succeeded = false;
              break;
            }
          }
        }
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDefaultReply() - When a program message is sent to the message queue
   * with substitution text, the message is delivered successfully, with correct
   * default reply.
   **/
  public void Var011() {
    boolean succeeded = true;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();

      // sandbox_.sendProgramMessage ("CAE0041", "QCPFMSG",
      // sandboxReply_.getQueue());
      f.sendInformational("CAE0041", "/QSYS.LIB/QCPFMSG.MSGF");
      // Note: CAE0041 in english is: "DIVISION BY ZERO ATTEMPTED."

      // sandbox_.sendProgramMessage ("CPA2087", "QCPFMSG",
      // sandboxReply_.getQueue());
      f.sendInformational("CPA2087", "/QSYS.LIB/QCPFMSG.MSGF");
      // Note: CAE0041 in english is:
      // "LOAD NEXT VOLUME ON THE INSTALLATION DEVICE (C G)."

      // sandbox_.sendProgramMessage ("CAE9058", "QCPFMSG",
      // sandboxReply_.getQueue());
      f.sendInformational("CAE9058", "/QSYS.LIB/QCPFMSG.MSGF");
      // Note: CAE0041 in english is: "TOO MANY FILES AND TABLES OPEN."

      System.out.println("Sent message");
      // /System.out.print("Press ENTER to continue."); try {System.in.read();}
      // catch(Exception e) {}
      Enumeration e = f.getMessages();
      if (!e.hasMoreElements()) {
        System.out.println("No messages received.");
        succeeded = false;
      }
      Vector msgKeys = new Vector();
      for (int counter = 0; e.hasMoreElements(); counter++) {
        QueuedMessage listedMessage = (QueuedMessage) e.nextElement();

        byte[] msgKey = listedMessage.getKey();
        msgKeys.addElement(new MessageKey(msgKey));
      }

      Enumeration e1 = msgKeys.elements();
      for (int counter = 0; e1.hasMoreElements(); counter++) {
        byte[] msgKey = ((MessageKey) e1.nextElement()).getKey();
        QueuedMessage message = f.receive(msgKey);

        System.out.println("Message ID: " + message.getID());
        System.out.println("Message text: " + message.getText());
        System.out.println("Default reply: " + message.getDefaultReply());
        System.out.println("Message help text: " + message.getHelp());
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  class MessageKey {
    private byte[] key_ = null;

    public MessageKey(byte[] key) {
      key_ = key;
    }

    byte[] getKey() {
      return key_;
    }
  }
}
