///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  QueuedMessageRFTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Message;

import com.ibm.as400.access.QueuedMessage;
import com.ibm.as400.resource.RMessageQueue;
import com.ibm.as400.resource.RQueuedMessage;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.resource.ResourceMetaData;

import test.MessageSandbox;
import test.MessageTest;
import test.Testcase;

import java.util.Date;

/**
 * Testcase QueuedMessageRFTestcase.
 * <p>
 * This tests the following QueuedMessage and Resource methods:
 * <ul>
 * <li>addResourceListener()
 * <li>cancelAttributesChanges()
 * <li>commitAttributeChanges()
 * <li>getAttributeMetaData()
 * <li>getAttributeValue(Object)
 * <li>getDate()
 * <li>getDefaultReply()
 * <li>getFileName()
 * <li>getFromJobName()
 * <li>getFromProgram()
 * <li>getHelp()
 * <li>getID()
 * <li>getKey()
 * <li>getLibraryName()
 * <li>getPath()
 * <li>getPresentation()
 * <li>getQueue()
 * <li>getReplyStatus()
 * <li>getResourceKey()
 * <li>getSeverity()
 * <li>getSubstitutionData()
 * <li>getText()
 * <li>getType()
 * <li>getUser()
 * <li>isAttributeChangePending()
 * <li>refreshAttributeValues()
 * <li>removeResourceListener()
 * <li>toString()
 * </ul>
 **/
public class QueuedMessageRFTestcase extends Testcase {
  private MessageSandbox sandbox_ = null;
  private MessageSandbox sandboxReply_ = null;

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {

    if (baseTestDriver_ != null) {  
        testLib_     = baseTestDriver_.getTestLib();
    }

    sandbox_ = new MessageSandbox(systemObject_, testLib_, "MQT", systemObject_.getUserId());
    sandboxReply_ = new MessageSandbox(systemObject_, testLib_, "MQTREPLY", systemObject_.getUserId());
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    sandbox_.cleanup();
    sandboxReply_.cleanup();
    deleteLibrary(testLib_);
  }

  private static ResourceMetaData findMetaData(
      ResourceMetaData[] metaDataArray, Object attributeID) {
    for (int i = 0; i < metaDataArray.length; ++i) {
      if (metaDataArray[i].getID() == attributeID)
        return metaDataArray[i];
    }
    return null;
  }

  /**
   * getAttributeValue(attributeID, value) -- Invalid use. Pass bad value for id
   **/
  public void Var001() {
    RQueuedMessage qmsg;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      qmsg.getAttributeValue(RQueuedMessage.REPLY_STATUS_NOT_ACCEPT);
      failed("Exception not thrown.");

    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }

  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * ALERT_OPTION
   **/
  public void Var002() {
    RQueuedMessage qmsg, qmsgrcv;
    String alertOpt;
    byte msgKey0[];
    String value;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      alertOpt = (String) qmsg.getAttributeValue(RQueuedMessage.ALERT_OPTION);
      if (alertOpt != null) {
        failed("Bad alert option value returned.");
        return;
      }
      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      value = (String) qmsgrcv.getAttributeValue(RQueuedMessage.ALERT_OPTION);
      if (value.equals(""))
        succeeded();
      else
        failed("Bad alert option value returned on receive.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for DATE_SENT
   **/
  public void Var003() {
    RQueuedMessage qmsg, qmsgrcv;
    Date curDate;
    Date msgDate;
    Date value;
    byte[] msgKey0;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      curDate = new Date();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      msgDate = (Date) qmsg.getAttributeValue(RQueuedMessage.DATE_SENT);
      if (msgDate.getMonth() != curDate.getMonth()
          || msgDate.getYear() != curDate.getYear()
          || msgDate.getDay() != curDate.getDay()
          || msgDate.getHours() != curDate.getHours()) {
        System.out.println(msgDate.getMonth() + " <-> " + curDate.getMonth());
        System.out.println(msgDate.getYear() + " <-> " + curDate.getYear());
        System.out.println(msgDate.getDay() + " <-> " + curDate.getDay());
        System.out.println(msgDate.getHours() + " <-> " + curDate.getHours());
        failed("Bad DATE_SENT value returned.");
        return;
      }
      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      value = (Date) qmsgrcv.getAttributeValue(RQueuedMessage.DATE_SENT);
      if (value.getMonth() != curDate.getMonth()
          || value.getYear() != curDate.getYear()
          || value.getDay() != curDate.getDay()
          || value.getHours() != curDate.getHours())
        failed("Bad DATE_SENT value received.");
      else
        succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * DEFAULT_REPLY for an INFORMATIONAL message
   **/
  public void Var004() {
    RQueuedMessage qmsg, qmsgrcv;
    String reply;
    String value;
    byte[] msgKey0;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      reply = (String) qmsg.getAttributeValue(RQueuedMessage.DEFAULT_REPLY);
      if (!reply.equals("")) {
        failed("Non empty reply sent back for INFORMATIONAL message.");
        return;
      }
      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      value = (String) qmsgrcv.getAttributeValue(RQueuedMessage.DEFAULT_REPLY);
      if (value == null)
        succeeded();
      else
        failed("Non empty reply sent back for received INFORMATIONAL message.");

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * DEFAULT_REPLY for an INQUIRY message
   **/
  public void Var005() {
    RQueuedMessage qmsg;
    String reply;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInquiry("Answer yes please", replyPath);
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      reply = (String) qmsg.getAttributeValue(RQueuedMessage.DEFAULT_REPLY);
      if (reply.length() == 0)
        succeeded();
      else
        failed("Non blank reply sent back for INFORMATIONAL message.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * DEFAULT_REPLY for an INQUIRY message that has a default reply
   **/
  public void Var006() {
    RQueuedMessage qmsg, qmsgrcv;
    String reply;
    String value;
    byte[] msgKey0;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      reply = (String) qmsg.getAttributeValue(RQueuedMessage.DEFAULT_REPLY);
      if (!reply.equals("G")) {
        failed("Bad default reply to CPA1E01. Reply = " + reply);
        return;
      }
      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      value = (String) qmsgrcv.getAttributeValue(RQueuedMessage.DEFAULT_REPLY);
      if (value == null)
        succeeded();
      else
        failed("Bad reply sent back for received INFORMATIONAL message.");

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * MESSAGE_FILE for a message that has no message file
   **/
  public void Var007() {
    RQueuedMessage qmsg;
    String value;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Hi there");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      value = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_FILE);
      if (value != null && value.equals(""))
        succeeded();
      else
        failed("Message file value not empty string. Value = |" + value + "|");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * MESSAGE_FILE for a message that is in a message file
   **/
  public void Var008() {
    RQueuedMessage qmsg, qmsgrcv;
    byte msgKey0[];
    String value;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      value = (String) qmsgrcv.getAttributeValue(RQueuedMessage.MESSAGE_FILE);
      if (value.equals("/QSYS.LIB/QCPFMSG.MSGF"))
        succeeded();
      else
        failed("Bad message file value. Value = " + value);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * MESSAGE_QUEUE
   **/
  public void Var009() {
    RQueuedMessage qmsg;
    String value;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Hi there");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      value = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_QUEUE);
      if (value.equals("/QSYS.LIB/" + testLib_ + ".LIB/MQT.MSGQ"))
        succeeded();
      else
        failed("Message file value not correct. Value = " + value);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * MESSAGE_QUEUE for a message that is in a message file
   **/
  public void Var010() {
    RQueuedMessage qmsg, qmsgrcv;
    byte msgKey0[];
    String value, value1;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      value1 = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_QUEUE);
      if (!value1.equals("/QSYS.LIB/" + testLib_ + ".LIB/MQT.MSGQ")) {
        failed("Bad MESSAGE_QUEUE value returned. Value1 = " + value1);
        return;
      }

      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      value = (String) qmsgrcv.getAttributeValue(RQueuedMessage.MESSAGE_QUEUE);
      if (value == null)
        succeeded();
      else
        failed("Bad message queue value. Value = " + value);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(object) -- Valid use. Get value for MESSAGE_QUEUE for a
   * senders copy message that has been received
   **/
  public void Var011() {
    RQueuedMessage qmsg, qmsgrcv, qmsgrcv1, qmsg1;
    byte msgKey0[], msgKey1[];
    String value, value1;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInquiry("Answer yes please", replyPath);
      f.open();
      g.open();
      qmsg = (RQueuedMessage) g.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(0);
      value1 = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_QUEUE);
      if (!value1.equals("/QSYS.LIB/" + testLib_ + ".LIB/MQTREPLY.MSGQ")) {
        failed("Bad getQueue() value returned. Value = " + value1);
        return;
      }
      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = g.receive(msgKey0, 0, RMessageQueue.SAME, RMessageQueue.COPY);
      value = (String) qmsgrcv.getAttributeValue(RQueuedMessage.MESSAGE_QUEUE);
      if (value == null)
        succeeded();
      else
        failed("Bad message queue value. Value = " + value);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * MESSAGE_FILE for an informational message that has been received
   **/
  public void Var012() {
    RQueuedMessage qmsg, qmsgrcv;
    byte msgKey0[];
    String value;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Hi there");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      value = (String) qmsgrcv.getAttributeValue(RQueuedMessage.MESSAGE_FILE);
      if (value.length() == 0)
        succeeded();
      else
        failed("Bad message queue value. Value = " + value);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue() -- Valid use. Get value for MESSAGE_HELP for an
   * informational message that has been received
   **/
  public void Var013() {
    RQueuedMessage qmsg, qmsg2, qmsg1, qmsg3, qmsgrcv1, qmsgrcv, qmsgrcv2, qmsgrcv3;
    byte msgKey0[], msgKey1[], msgKey2[], msgKey3[];
    String value, value1, value2, value3, value4, value5, value6, value7;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInformational("Hi there");
      f.sendInformational("CAE0002", msgfPath);
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      g.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      qmsg2 = (RQueuedMessage) g.resourceAt(0);
      qmsg3 = (RQueuedMessage) f.resourceAt(2);
      value = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_HELP);
      value1 = (String) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_HELP);
      value2 = (String) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_HELP);
      value3 = (String) qmsg3.getAttributeValue(RQueuedMessage.MESSAGE_HELP);
      if (!value.equals("Hi there")) {
        failed("Bad getHelp() value received. Value = " + value);
        return;
      }
      if (value2.toUpperCase().indexOf("DO ONE OF THE FOLLOWING") == -1) {
        failed("Help text not found for message CPA1E01 (value2).");
        return;
      }
      if (value1.toUpperCase().indexOf(
          "APPLICATION RUNNING REQUIRES A LATER VERSION OF CSP/AE") == -1) {
        failed("Help text not found for message CAE0002 (value1).");
        return;
      }
      if (value3.toUpperCase().indexOf("DO ONE OF THE FOLLOWING") == -1) {
        failed("Help text not found for message CPA1E01 (value3).");
        return;
      }

      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey3 = (byte[]) qmsg3.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      qmsgrcv1 = f.receive(msgKey1);
      qmsgrcv3 = f.receive(msgKey3);
      qmsgrcv2 = g.receive(msgKey2);

      value4 = (String) qmsgrcv.getAttributeValue(RQueuedMessage.MESSAGE_HELP);
      value6 = (String) qmsgrcv2.getAttributeValue(RQueuedMessage.MESSAGE_HELP);
      value5 = (String) qmsgrcv1.getAttributeValue(RQueuedMessage.MESSAGE_HELP);
      value7 = (String) qmsgrcv3.getAttributeValue(RQueuedMessage.MESSAGE_HELP);
      if (value5.toUpperCase().indexOf(
          "APPLICATION RUNNING REQUIRES A LATER VERSION OF CSP/AE") == -1) {
        failed("Help text not found for received informational message CAE0002 (value5).");
        return;
      }
      if (value6.length() != 0) {
        failed("Non blank help text returned for received SENDERS_COPY message.");
        return;
      }
      if (value7.toUpperCase().indexOf("DO ONE OF THE FOLLOWING") == -1) {
        failed("Help text not found for received inquiry message CPA1E01 (value7).");
        return;
      }
      if (value4.length() == 0)
        succeeded();
      else
        failed("Non blank help text returned for informational message with no help");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(object) -- Valid use. Get value for MESSAGE_ID for an
   * informational message
   **/
  public void Var014() {
    RQueuedMessage qmsg, qmsg1, qmsg2, qmsgrcv, qmsgrcv1, qmsgrcv2, qmsg3, qmsgrcv3;
    byte msgKey0[], msgKey1[], msgKey2[], msgKey3[];
    String value, value1, value2, value3, value4, value5, value6, value7;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.sendInformational("Message without an ID");
      f.sendInformational("CAE0002", msgfPath);
      f.open();
      g.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      qmsg2 = (RQueuedMessage) f.resourceAt(2);
      qmsg3 = (RQueuedMessage) g.resourceAt(0);

      value = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value1 = (String) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value2 = (String) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value3 = (String) qmsg3.getAttributeValue(RQueuedMessage.MESSAGE_ID);

      if (!value.equals("CPA1E01")) {
        failed("Bad message ID value. Value should be CPA1E01. Value = "
            + value);
        return;
      }
      if (value1.length() != 0) {
        failed("Non blank ID returned for msg with no ID. Value = " + value1);
        return;
      }
      if (!value2.equals("CAE0002")) {
        failed("Bad message ID value. Value should be CAE0002. Value2 = "
            + value2);
        return;
      }
      if (!value3.equals("CPA1E01")) {
        failed("Bad message ID value. Value should be CPA1E01. Value3 = "
            + value3);
        return;
      }

      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey3 = (byte[]) qmsg3.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      qmsgrcv1 = f.receive(msgKey1);
      qmsgrcv2 = f.receive(msgKey2);
      qmsgrcv3 = g.receive(msgKey3, 0, RMessageQueue.SAME, RMessageQueue.COPY);
      value4 = (String) qmsgrcv.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value5 = (String) qmsgrcv1.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value6 = (String) qmsgrcv2.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value7 = (String) qmsgrcv3.getAttributeValue(RQueuedMessage.MESSAGE_ID);

      if (!value4.equals("CPA1E01")) {
        failed("Bad message ID value. Value should be CPA1E01. Value4 = "
            + value4);
        return;
      }
      if (value5.length() != 0) {
        failed("Non blank ID returned for msg with no ID. Value5 = " + value5);
        return;
      }
      if (!value6.equals("CAE0002")) {
        failed("Bad message ID value. Value should be CAE0002. Value6 = "
            + value6);
        return;
      }
      if (!value7.equals("CPA1E01")) {
        failed("Bad message ID value. Value should be CPA1E01. Value7 = "
            + value7);
        return;
      }
      succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * MESSAGE_KEY for an informational message
   **/
  public void Var015() {
    RQueuedMessage qmsg, qmsgrcv;
    byte msgKey0[];
    byte value[], value1[];

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Hi there");

      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      value = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      if (value == null
          || !value.equals((byte[]) qmsg
              .getAttributeValue(RQueuedMessage.MESSAGE_KEY))) {
        failed("Bad message KEY value. Value = " + value);
        return;
      }
      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      value1 = (byte[]) qmsgrcv.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      if (value1 != null
          && value1.equals(qmsgrcv
              .getAttributeValue(RQueuedMessage.MESSAGE_KEY)))
        succeeded();
      else
        failed("Bad message KEY value. Value = " + value1);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * MESSAGE_SEVERITY for an informational message
   **/
  public void Var016() {
    RQueuedMessage qmsg0, qmsgrcv0, qmsg1, qmsgrcv1;
    byte msgKey0[], msgKey1[];
    Integer value0, value1, value2, value3;
    Integer Int80 = new Integer(80);
    Integer Int99 = new Integer(99);
    Integer Int0 = new Integer(0);

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Hi there.");
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      qmsg0 = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      value0 = (Integer) qmsg0
          .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY);
      value1 = (Integer) qmsg1
          .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY);
      if (!value0.equals(Int80)
          || !value1.equals(Int99)
          || !value0.equals(qmsg0
              .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY))
          || !value1.equals(qmsg1
              .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY))) {
        failed("Bad message SEVERITY value.");
        return;
      }
      msgKey0 = (byte[]) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv0 = f.receive(msgKey0);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv1 = f.receive(msgKey1);

      value2 = (Integer) qmsgrcv0
          .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY);
      value3 = (Integer) qmsgrcv1
          .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY);

      if (value2.equals(Int0)
          && value3.equals(Int99)
          && value2.equals(qmsgrcv0
              .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY))
          && value3.equals(qmsgrcv1
              .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY)))
        succeeded();
      else
        failed("Bad message SEVERITY value.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * MESSAGE_TEXT for an informational message
   **/
  public void Var017() {
    RQueuedMessage qmsg0, qmsgrcv0, qmsg1, qmsgrcv1;
    byte msgKey0[], msgKey1[];
    String value0, value1, value2, value3;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("CAE0002", msgfPath);
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      qmsg0 = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      value0 = (String) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_TEXT);
      value1 = (String) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_TEXT);
      if (value0.toUpperCase().indexOf(
          "NEW LEVEL OF CSP/AE REQUIRED FOR APPLICATION") == -1
          || value1.toUpperCase()
              .indexOf("SYSTEM IS SCHEDULED TO POWER OFF AT") == -1) {
        failed("Bad message TEXT value.");
        return;
      }
      msgKey0 = (byte[]) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv0 = f.receive(msgKey0);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv1 = f.receive(msgKey1);

      value2 = (String) qmsgrcv0.getAttributeValue(RQueuedMessage.MESSAGE_TEXT);
      value3 = (String) qmsgrcv1.getAttributeValue(RQueuedMessage.MESSAGE_TEXT);
      if (value2.toUpperCase().indexOf(
          "NEW LEVEL OF CSP/AE REQUIRED FOR APPLICATION") == -1
          || value3.toUpperCase()
              .indexOf("SYSTEM IS SCHEDULED TO POWER OFF AT") == -1)
        failed("Bad message TEXT value.");
      else
        succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * MESSAGE_TYPE for an informational message , inquiry message and a senders
   * copy message
   **/
  public void Var018() {
    RQueuedMessage qmsg0, qmsgrcv0, qmsg1, qmsgrcv1, qmsg2, qmsgrcv2;
    byte msgKey0[], msgKey1[], msgKey2[];
    Integer value0, value1, value2, value3, value4, value5;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInformational("Hi there.");
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      g.open();
      qmsg0 = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      qmsg2 = (RQueuedMessage) g.resourceAt(0);
      value0 = (Integer) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_TYPE);
      value1 = (Integer) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_TYPE);
      value2 = (Integer) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_TYPE);
      if (value0.intValue() != QueuedMessage.INFORMATIONAL
          || value1.intValue() != QueuedMessage.INQUIRY
          || value2.intValue() != QueuedMessage.SENDERS_COPY) {
        failed("Bad message TYPE value.");
        return;
      }
      msgKey0 = (byte[]) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv0 = f.receive(msgKey0);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv1 = f.receive(msgKey1);
      msgKey2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv2 = g.receive(msgKey2);

      value3 = (Integer) qmsgrcv0
          .getAttributeValue(RQueuedMessage.MESSAGE_TYPE);
      value4 = (Integer) qmsgrcv1
          .getAttributeValue(RQueuedMessage.MESSAGE_TYPE);
      value5 = (Integer) qmsgrcv2
          .getAttributeValue(RQueuedMessage.MESSAGE_TYPE);
      if (value3.intValue() == QueuedMessage.INFORMATIONAL
          && value4.intValue() == QueuedMessage.INQUIRY
          && value5.intValue() == QueuedMessage.REPLY_MESSAGE_DEFAULT_USED)
        succeeded();
      else
        failed("Bad message TYPE value.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * REPLY_STATUS for an informational message , inquiry message and a senders
   * copy message
   **/
  public void Var019() {
    RQueuedMessage qmsg0, qmsgrcv0, qmsg1, qmsgrcv1, qmsg2, qmsgrcv2;
    byte msgKey0[], msgKey1[], msgKey2[];
    String value0, value1, value2, value3, value4, value5;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInformational("Hi there.");
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      g.open();
      qmsg0 = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      qmsg2 = (RQueuedMessage) g.resourceAt(0);
      value0 = (String) qmsg0.getAttributeValue(RQueuedMessage.REPLY_STATUS);
      value1 = (String) qmsg1.getAttributeValue(RQueuedMessage.REPLY_STATUS);
      value2 = (String) qmsg2.getAttributeValue(RQueuedMessage.REPLY_STATUS);
      if (!(value0.equals(RQueuedMessage.REPLY_STATUS_NOT_ACCEPT))
          || !(value1.equals(RQueuedMessage.REPLY_STATUS_ACCEPTS_NOT_SENT))
          || !(value2.equals(RQueuedMessage.REPLY_STATUS_ACCEPTS_NOT_SENT))) {
        failed("Bad message REPLY_STATUS value.");
        return;
      }
      msgKey0 = (byte[]) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv0 = f.receive(msgKey0);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv1 = f.receive(msgKey1);
      msgKey2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv2 = g.receive(msgKey2);

      value3 = (String) qmsgrcv0.getAttributeValue(RQueuedMessage.REPLY_STATUS);
      value4 = (String) qmsgrcv1.getAttributeValue(RQueuedMessage.REPLY_STATUS);
      value5 = (String) qmsgrcv2.getAttributeValue(RQueuedMessage.REPLY_STATUS);
      if (value3 == null && value4 == null && value5 == null)
        succeeded();
      else
        failed("Bad message REPLY_STATUS value.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * SENDER_JOB_NAME for an informational message , inquiry message and a
   * senders copy message
   **/
  public void Var020() {
    RQueuedMessage qmsg0, qmsgrcv0, qmsg1, qmsgrcv1, qmsg2, qmsgrcv2;
    byte msgKey0[], msgKey1[], msgKey2[];
    String value0, value1, value2, value3, value4, value5;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInformational("Hi there.");
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      g.open();
      qmsg0 = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      qmsg2 = (RQueuedMessage) g.resourceAt(0);
      value0 = (String) qmsg0.getAttributeValue(RQueuedMessage.SENDER_JOB_NAME);
      value1 = (String) qmsg1.getAttributeValue(RQueuedMessage.SENDER_JOB_NAME);
      value2 = (String) qmsg2.getAttributeValue(RQueuedMessage.SENDER_JOB_NAME);
      if (!(value0.equals("QZRCSRVS")) || !(value1.equals("QZRCSRVS"))
          || !(value2.equals("QZRCSRVS"))) {
        // If running on-thread, then tolerate a different name.
        if (!MessageTest.runningOnThread_) {
          failed("Bad message SENDER_JOB_NAME value.");
          return;
        }
      }
      msgKey0 = (byte[]) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv0 = f.receive(msgKey0);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv1 = f.receive(msgKey1);
      msgKey2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv2 = g.receive(msgKey2);

      value3 = (String) qmsgrcv0
          .getAttributeValue(RQueuedMessage.SENDER_JOB_NAME);
      value4 = (String) qmsgrcv1
          .getAttributeValue(RQueuedMessage.SENDER_JOB_NAME);
      value5 = (String) qmsgrcv2
          .getAttributeValue(RQueuedMessage.SENDER_JOB_NAME);
      if (value3.equals("QZRCSRVS") && value4.equals("QZRCSRVS")
          && value5.equals("QZRCSRVS"))
        succeeded();
      else {
        // If running on-thread, then tolerate a different name.
        if (MessageTest.runningOnThread_)
          succeeded();
        else {
          failed("Bad message SENDER_JOB_NAME value.");
        }
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * SENDER_USER_NAME for an informational message , inquiry message and a
   * senders copy message
   **/
  public void Var021() {
    RQueuedMessage qmsg0, qmsgrcv0, qmsg1, qmsgrcv1, qmsg2, qmsgrcv2;
    byte msgKey0[], msgKey1[], msgKey2[];
    String value0, value1, value2, value3, value4, value5;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInformational("Hi there.");
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      g.open();
      qmsg0 = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      qmsg2 = (RQueuedMessage) g.resourceAt(0);
      value0 = (String) qmsg0
          .getAttributeValue(RQueuedMessage.SENDER_USER_NAME);
      value1 = (String) qmsg1
          .getAttributeValue(RQueuedMessage.SENDER_USER_NAME);
      value2 = (String) qmsg2
          .getAttributeValue(RQueuedMessage.SENDER_USER_NAME);
      if (!(value0.equals("QUSER")) || !(value1.equals("QUSER"))
          || !(value2.equals("QUSER"))) {
        // If running on-thread, then tolerate a different name.
        if (!MessageTest.runningOnThread_) {
          failed("Bad message SENDER_USER_NAME value.");
          return;
        }
      }
      msgKey0 = (byte[]) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv0 = f.receive(msgKey0);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv1 = f.receive(msgKey1);
      msgKey2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv2 = g.receive(msgKey2);

      value3 = (String) qmsgrcv0
          .getAttributeValue(RQueuedMessage.SENDER_USER_NAME);
      value4 = (String) qmsgrcv1
          .getAttributeValue(RQueuedMessage.SENDER_USER_NAME);
      value5 = (String) qmsgrcv2
          .getAttributeValue(RQueuedMessage.SENDER_USER_NAME);
      if (value3.equals("QUSER") && value4.equals("QUSER")
          && value5.equals("QUSER"))
        succeeded();
      else {
        // If running on-thread, then tolerate a different name.
        if (MessageTest.runningOnThread_)
          succeeded();
        else {
          failed("Bad message SENDER_USER_NAME value.");
        }
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * SENDER_JOB_NUMBER for an informational message , inquiry message and a
   * senders copy message
   **/
  public void Var022() {
    RQueuedMessage qmsg0, qmsgrcv0, qmsg1, qmsgrcv1, qmsg2, qmsgrcv2;
    byte msgKey0[], msgKey1[], msgKey2[];
    String value0, value1, value2, value3, value4, value5;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInformational("Hi there.");
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      g.open();
      qmsg0 = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      qmsg2 = (RQueuedMessage) g.resourceAt(0);
      value0 = (String) qmsg0
          .getAttributeValue(RQueuedMessage.SENDER_JOB_NUMBER);
      value1 = (String) qmsg1
          .getAttributeValue(RQueuedMessage.SENDER_JOB_NUMBER);
      value2 = (String) qmsg2
          .getAttributeValue(RQueuedMessage.SENDER_JOB_NUMBER);
      if (!(value0.equals(value1)) || !(value0.equals(value2))
          || (value0 == null) || (value0.length() == 0)) {
        failed("Bad message SENDER_JOB_NUMBER value.");
        return;
      }
      msgKey0 = (byte[]) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv0 = f.receive(msgKey0);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv1 = f.receive(msgKey1);
      msgKey2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv2 = g.receive(msgKey2);

      value3 = (String) qmsgrcv0
          .getAttributeValue(RQueuedMessage.SENDER_JOB_NUMBER);
      value4 = (String) qmsgrcv1
          .getAttributeValue(RQueuedMessage.SENDER_JOB_NUMBER);
      value5 = (String) qmsgrcv2
          .getAttributeValue(RQueuedMessage.SENDER_JOB_NUMBER);
      if (value3.equals(value4) && value3.equals(value5) && value3 != null
          && value3.length() != 0)
        succeeded();
      else
        failed("Bad message SENDER_JOB_NUMBER value.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * SENDING_PROGRAM_NAME for an informational message , inquiry message and a
   * senders copy message
   **/
  public void Var023() {
    RQueuedMessage qmsg0, qmsgrcv0, qmsg1, qmsgrcv1, qmsg2, qmsgrcv2;
    byte msgKey0[], msgKey1[], msgKey2[];
    String value0, value1, value2, value3, value4, value5;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInformational("Hi there.");
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      g.open();
      qmsg0 = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      qmsg2 = (RQueuedMessage) g.resourceAt(0);
      value0 = (String) qmsg0
          .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME);
      value1 = (String) qmsg1
          .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME);
      value2 = (String) qmsg2
          .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME);
      if (!(value0.trim().equals("QZRCSRVS"))
          || !(value1.trim().equals("QZRCSRVS"))
          || !(value2.trim().equals("QZRCSRVS"))) {
        // If running on-thread, then tolerate a different name.
        if (!MessageTest.runningOnThread_) {
          failed("Bad message SENDING_PROGRAM_NAME value.");
          return;
        }
      }
      msgKey0 = (byte[]) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv0 = f.receive(msgKey0);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv1 = f.receive(msgKey1);
      msgKey2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv2 = g.receive(msgKey2);

      value3 = (String) qmsgrcv0
          .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME);
      value4 = (String) qmsgrcv1
          .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME);
      value5 = (String) qmsgrcv2
          .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME);
      if (value3.equals("QZRCSRVS") && value4.equals("QZRCSRVS")
          && value5.equals("QMHRCVM"))
        succeeded();
      else {
        // If running on-thread, then tolerate a different name.
        if (MessageTest.runningOnThread_)
          succeeded();
        else {
          failed("Bad message SENDING_PROGRAM_NAME value.");
        }
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue(attributeID, value) -- Valid use. Get value for
   * SUBSTITUTION_DATA for an message
   **/
  public void Var024() {
    RQueuedMessage qmsg0, qmsgrcv0;
    byte msgKey0[];
    byte[] value0, value1;
    byte[] subData = new byte[] { (byte) 0x00 };

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("CAE0024", msgfPath, subData);
      f.open();
      qmsg0 = (RQueuedMessage) f.resourceAt(0);
      value0 = (byte[]) qmsg0
          .getAttributeValue(RQueuedMessage.SUBSTITUTION_DATA);
      if (value0 != null) {
        failed("Bad message SUBSTITUTION_DATA value.");
        return;
      }
      msgKey0 = (byte[]) qmsg0.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv0 = f.receive(msgKey0);
      value1 = (byte[]) qmsgrcv0
          .getAttributeValue(RQueuedMessage.SUBSTITUTION_DATA);
      if (value1 != null)
        succeeded();
      else
        failed("Bad message SUBSTITUTION_DATA value.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * ALERT_OPTION
   **/
  public void Var025() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.ALERT_OPTION);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for ALERT_OPTION.");
        return;
      } else {
        if (!presObj.getName().equals("Alert Option")
            || !presObj.getFullName().equals("Alert Option")) {
          failed("Bad presentation object name returned for ALERT_OPTION");
          return;
        }
      }
      if (presObj.getValue(Presentation.ICON_COLOR_16x16) != null
          || presObj.getValue(Presentation.ICON_COLOR_32x32) != null) {
        failed("FALSE returned for ICON_COLOR == null");
        return;
      }

      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute ALERT_OPTION");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute ALERT_OPTION");
        return;
      }
      if (metaData.isValueLimited() != true) {
        failed("FALSE returned for isValueLimited for attribute ALERT_OPTION");
        return;
      }
      if (!metaData.getID().equals("ALERT_OPTION")) {
        failed("ALERT_OPTION not returned for getID for attribute ALERT_OPTION");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute ALERT_OPTION");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute ALERT_OPTION");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 5) {
        failed("Possible values length bad for attribute ALERT_OPTION.");
        return;
      }
      if (possibleValues[0].equals(RQueuedMessage.ALERT_OPTION_DEFER) && // @B1C
                                                                         // changed
                                                                         // index
                                                                         // from
                                                                         // 3 t0
                                                                         // 0
                                                                         // due
                                                                         // to
                                                                         // hash
                                                                         // table
                                                                         // mapping
          possibleValues[1].equals(RQueuedMessage.ALERT_OPTION_IMMEDIATE) && // @B1C
                                                                             // changed
                                                                             // index
                                                                             // from
                                                                             // 2
                                                                             // to
                                                                             // 1
                                                                             // due
                                                                             // to
                                                                             // hash
                                                                             // table
                                                                             // mapping
          possibleValues[3].equals(RQueuedMessage.ALERT_OPTION_NO) && // @B1C
                                                                      // changed
                                                                      // index
                                                                      // from 1
                                                                      // to 3
                                                                      // due to
                                                                      // hash
                                                                      // table
                                                                      // mapping
          possibleValues[2].equals(RQueuedMessage.ALERT_OPTION_UNATTENDED)) // @B1C
                                                                            // changed
                                                                            // index
                                                                            // from
                                                                            // 0
                                                                            // to
                                                                            // 2
                                                                            // due
                                                                            // to
                                                                            // hash
                                                                            // table
                                                                            // mapping
        succeeded();
      else
        failed("Bad possible value returned for attribute ALERT_OPTION.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for DATE_SENT
   **/
  public void Var026() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.DATE_SENT);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for DATE_SENT.");
        return;
      } else {
        if (!presObj.getName().equals("Date Sent")
            || !presObj.getFullName().equals("Date Sent")) {
          failed("Bad presentation object name returned for DATE_SENT");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute DATE_SENT");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute DATE_SENT");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute DATE_SENT");
        return;
      }
      if (!metaData.getID().equals("DATE_SENT")) {
        failed("DATE_SENT not returned for getID for attribute DATE_SENT");
        return;
      }
      if (metaData.getType() != Date.class) {
        failed("Bad class value returned for getType for attribute DATE_SENT");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute DATE_SENT");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute DATE_SENT.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * DEFAULT_REPLY
   **/
  public void Var027() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.DEFAULT_REPLY);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for DEFAULT_REPLY.");
        return;
      } else {
        if (!presObj.getName().equals("Default Reply")
            || !presObj.getFullName().equals("Default Reply")) {
          failed("Bad presentation object name returned for DEFAULT_REPLY");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute DEFAULT_REPLY");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute DEFAULT_REPLY");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute DEFAULT_REPLY");
        return;
      }
      if (!metaData.getID().equals("DEFAULT_REPLY")) {
        failed("DEFAULT_REPLY not returned for getID for attribute DEFAULT_REPLY");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute DEFAULT_REPLY");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute DEFAULT_REPLY");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute DEFAULT_REPLY.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_FILE
   **/
  public void Var028() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.MESSAGE_FILE);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_FILE.");
        return;
      } else {
        if (!presObj.getName().equals("Message File")
            || !presObj.getFullName().equals("Message File")) {
          failed("Bad presentation object name returned for MESSAGE_FILE");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_FILE");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_FILE");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_FILE");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_FILE")) {
        failed("MESSAGE_FILE not returned for getID for attribute MESSAGE_FILE");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_FILE");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_FILE");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_FILE.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }

  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_HELP
   **/
  public void Var029() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.MESSAGE_HELP);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_HELP.");
        return;
      } else {
        if (!presObj.getName().equals("Message Help")
            || !presObj.getFullName().equals("Message Help")) {
          failed("Bad presentation object name returned for MESSAGE_HELP");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_HELP");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_HELP");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_HELP");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_HELP")) {
        failed("MESSAGE_HELP not returned for getID for attribute MESSAGE_HELP");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_HELP");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_HELP");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_HELP.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_ID
   **/
  public void Var030() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.MESSAGE_ID);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_ID.");
        return;
      } else {
        if (!presObj.getName().equals("Message ID")
            || !presObj.getFullName().equals("Message ID")) {
          failed("Bad presentation object name returned for MESSAGE_ID");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_ID");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_ID");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_ID");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_ID")) {
        failed("MESSAGE_ID not returned for getID for attribute MESSAGE_ID");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_ID");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_ID");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_ID.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_KEY
   **/
  public void Var031() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.MESSAGE_KEY);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_KEY.");
        return;
      } else {
        if (!presObj.getName().equals("Message Key")
            || !presObj.getFullName().equals("Message Key")) {
          failed("Bad presentation object name returned for MESSAGE_KEY");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_KEY");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_KEY");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_KEY");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_KEY")) {
        failed("MESSAGE_KEY not returned for getID for attribute MESSAGE_KEY");
        return;
      }
      if (metaData.getType() != byte[].class) {
        failed("Bad class value returned for getType for attribute MESSAGE_KEY");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_KEY");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_KEY.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_QUEUE
   **/
  public void Var032() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.MESSAGE_QUEUE);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_QUEUE.");
        return;
      } else {
        if (!presObj.getName().equals("Message Queue")
            || !presObj.getFullName().equals("Message Queue")) {
          failed("Bad presentation object name returned for MESSAGE_QUEUE");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_QUEUE");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_QUEUE");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_QUEUE");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_QUEUE")) {
        failed("MESSAGE_QUEUE not returned for getID for attribute MESSAGE_QUEUE");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_QUEUE");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_QUEUE");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_QUEUE.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_SEVERITY
   **/
  public void Var033() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.MESSAGE_SEVERITY);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_SEVERITY.");
        return;
      } else {
        if (!presObj.getName().equals("Message Severity")
            || !presObj.getFullName().equals("Message Severity")) {
          failed("Bad presentation object name returned for MESSAGE_SEVERITY");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_SEVERITY");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_SEVERITY");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_SEVERITY");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_SEVERITY")) {
        failed("MESSAGE_SEVERITY not returned for getID for attribute MESSAGE_SEVERITY");
        return;
      }
      if (metaData.getType() != Integer.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_SEVERITY");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_SEVERITY");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_SEVERITY.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_TEXT
   **/
  public void Var034() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.MESSAGE_TEXT);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_TEXT.");
        return;
      } else {
        if (!presObj.getName().equals("Message Text")
            || !presObj.getFullName().equals("Message Text")) {
          failed("Bad presentation object name returned for MESSAGE_TEXT");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_TEXT");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_TEXT");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_TEXT");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_TEXT")) {
        failed("MESSAGE_TEXT not returned for getID for attribute MESSAGE_TEXT");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_TEXT");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_TEXT");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_TEXT.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_TYPE
   **/
  public void Var035() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;
    Integer intValue;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.MESSAGE_TYPE);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_TYPE.");
        return;
      } else {
        if (!presObj.getName().equals("Message Type")
            || !presObj.getFullName().equals("Message Type")) {
          failed("Bad presentation object name returned for MESSAGE_TYPE");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_TYPE");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_TYPE");
        return;
      }
      if (metaData.isValueLimited() != true) {
        failed("FALSE returned for isValueLimited for attribute MESSAGE_TYPE");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_TYPE")) {
        failed("MESSAGE_TYPE not returned for getID for attribute MESSAGE_TYPE");
        return;
      }
      if (metaData.getType() != Integer.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_TYPE");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_TYPE");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 16) {
        failed("Possible values length bad for attribute MESSAGE_TYPE.");
        return;
      }
      intValue = (Integer) possibleValues[0];
      if (intValue.intValue() != QueuedMessage.REPLY_VALIDITY_CHECKED) // @B1C
                                                                       // Changed
                                                                       // from
                                                                       // REPLY_FROM_SYSTEM_REPLY_LIST
                                                                       // due to
                                                                       // hash
                                                                       // table
                                                                       // mapping
      {
        failed("Possible value[0] incorrect.");
        return;
      }
      intValue = (Integer) possibleValues[13];
      // if (intValue.intValue() != QueuedMessage.COMPLETION)
      // @B1D if (intValue.intValue() != QueuedMessage.INFORMATIONAL) // @A2c
      if (intValue.intValue() != QueuedMessage.REPLY_SYSTEM_DEFAULT_USED) // @B1A
      {
        failed("Possible value[13] incorrect.");
        return;
      }

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * REPLY_STATUS
   **/
  public void Var036() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.REPLY_STATUS);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for REPLY_STATUS.");
        return;
      } else {
        if (!presObj.getName().equals("Reply Status")
            || !presObj.getFullName().equals("Reply Status")) {
          failed("Bad presentation object name returned for REPLY_STATUS");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute REPLY_STATUS");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute REPLY_STATUS");
        return;
      }
      if (metaData.isValueLimited() != true) {
        failed("FALSE returned for isValueLimited for attribute REPLY_STATUS");
        return;
      }
      if (!metaData.getID().equals("REPLY_STATUS")) {
        failed("REPLY_STATUS not returned for getID for attribute REPLY_STATUS");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute REPLY_STATUS");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute REPLY_STATUS");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      int i;
      if (possibleValues.length != 3) {
        failed("Possible values length bad for attribute REPLY_STATUS.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * SENDER_JOB_NAME
   **/
  public void Var037() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.SENDER_JOB_NAME);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for SENDER_JOB_NAME.");
        return;
      } else {
        if (!presObj.getName().equals("Sender Job Name")
            || !presObj.getFullName().equals("Sender Job Name")) {
          failed("Bad presentation object name returned for SENDER_JOB_NAME");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute SENDER_JOB_NAME");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute SENDER_JOB_NAME");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute SENDER_JOB_NAME");
        return;
      }
      if (!metaData.getID().equals("SENDER_JOB_NAME")) {
        failed("SENDER_JOB_NAME not returned for getID for attribute SENDER_JOB_NAME");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute SENDER_JOB_NAME");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute SENDER_JOB_NAME");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute SENDER_JOB_NAME.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * SENDER_USER_NAME
   **/
  public void Var038() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.SENDER_USER_NAME);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for SENDER_USER_NAME.");
        return;
      } else {
        if (!presObj.getName().equals("Sender User Name")
            || !presObj.getFullName().equals("Sender User Name")) {
          failed("Bad presentation object name returned for SENDER_USER_NAME");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute SENDER_USER_NAME");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute SENDER_USER_NAME");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute SENDER_USER_NAME");
        return;
      }
      if (!metaData.getID().equals("SENDER_USER_NAME")) {
        failed("SENDER_USER_NAME not returned for getID for attribute SENDER_USER_NAME");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute SENDER_USER_NAME");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute SENDER_USER_NAME");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute SENDER_USER_NAME.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * SENDER_JOB_NUMBER
   **/
  public void Var039() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.SENDER_JOB_NUMBER);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for SENDER_JOB_NUMBER.");
        return;
      } else {
        if (!presObj.getName().equals("Sender Job Number")
            || !presObj.getFullName().equals("Sender Job Number")) {
          failed("Bad presentation object name returned for SENDER_JOB_NUMBER");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute SENDER_JOB_NUMBER");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute SENDER_JOB_NUMBER");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute SENDER_JOB_NUMBER");
        return;
      }
      if (!metaData.getID().equals("SENDER_JOB_NUMBER")) {
        failed("SENDER_JOB_NUMBER not returned for getID for attribute SENDER_JOB_NUMBER");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute SENDER_JOB_NUMBER");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute SENDER_JOB_NUMBER");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute SENDER_JOB_NUMBER.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * SENDING_PROGRAM_NAME
   **/
  public void Var040() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.SENDING_PROGRAM_NAME);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for SENDING_PROGRAM_NAME.");
        return;
      } else {
        if (!presObj.getName().equals("Sending Program Name")
            || !presObj.getFullName().equals("Sending Program Name")) {
          failed("Bad presentation object name returned for SENDING_PROGRAM_NAME");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute SENDING_PROGRAM_NAME");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute SENDING_PROGRAM_NAME");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute SENDING_PROGRAM_NAME");
        return;
      }
      if (!metaData.getID().equals("SENDING_PROGRAM_NAME")) {
        failed("SENDING_PROGRAM_NAME not returned for getID for attribute SENDING_PROGRAM_NAME");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute SENDING_PROGRAM_NAME");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute SENDING_PROGRAM_NAME");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute SENDING_PROGRAM_NAME.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * SUBSTITUTION_DATA
   **/
  public void Var041() {
    RQueuedMessage qmsg;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = (ResourceMetaData) qmsg
          .getAttributeMetaData(RQueuedMessage.SUBSTITUTION_DATA);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for SUBSTITUTION_DATA.");
        return;
      } else {
        if (!presObj.getName().equals("Substitution Data")
            || !presObj.getFullName().equals("Substitution Data")) {
          failed("Bad presentation object name returned for SUBSTITUTION_DATA");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute SUBSTITUTION_DATA");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute SUBSTITUTION_DATA");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute SUBSTITUTION_DATA");
        return;
      }
      if (!metaData.getID().equals("SUBSTITUTION_DATA")) {
        failed("SUBSTITUTION_DATA not returned for getID for attribute SUBSTITUTION_DATA");
        return;
      }
      if (metaData.getType() != byte[].class) {
        failed("Bad class value returned for getType for attribute SUBSTITUTION_DATA");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute SUBSTITUTION_DATA");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute SUBSTITUTION_DATA.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeMetaData(Object AttributeID) -- Test invalid use; pass null for
   * attribute ID.
   **/
  public void Var042() {
    ResourceMetaData metaData;
    RQueuedMessage qmsg;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = qmsg.getAttributeMetaData(null);
      f.close();
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * getAttributeMetaData(Object AttributeID) -- Test invalid use; pass bad
   * value for attribute ID.
   **/
  public void Var043() {
    ResourceMetaData metaData;
    RQueuedMessage qmsg;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = qmsg.getAttributeMetaData(RQueuedMessage.ALERT_OPTION_DEFER);
      f.close();
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * getAttributeMetaData() - Verify valid with default constructor
   **/
  public void Var044() {
    ResourceMetaData[] metaData;
    RQueuedMessage qmsg;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaData = qmsg.getAttributeMetaData();
      if (metaData.length == 17)
        succeeded();
      else
        failed("Length of ResourceMetaData array incorrect.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeMetaData() -- Valid use. Get meta data for ALERT_OPTION
   **/
  public void Var045() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.ALERT_OPTION);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for ALERT_OPTION.");
        return;
      } else {
        if (!presObj.getName().equals("Alert Option")
            || !presObj.getFullName().equals("Alert Option")) {
          failed("Bad presentation object name returned for ALERT_OPTION");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute ALERT_OPTION");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute ALERT_OPTION");
        return;
      }
      if (metaData.isValueLimited() != true) {
        failed("FALSE returned for isValueLimited for attribute ALERT_OPTION");
        return;
      }
      if (!metaData.getID().equals("ALERT_OPTION")) {
        failed("ALERT_OPTION not returned for getID for attribute ALERT_OPTION");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute ALERT_OPTION");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute ALERT_OPTION");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 5) {
        failed("Possible values length bad for attribute ALERT_OPTION.");
        return;
      }
      if (possibleValues[0].equals(RQueuedMessage.ALERT_OPTION_DEFER) && // @B1C
                                                                         // changed
                                                                         // index
                                                                         // from
                                                                         // 3 t0
                                                                         // 0
                                                                         // due
                                                                         // to
                                                                         // hash
                                                                         // table
                                                                         // mapping
          possibleValues[1].equals(RQueuedMessage.ALERT_OPTION_IMMEDIATE) && // @B1C
                                                                             // changed
                                                                             // index
                                                                             // from
                                                                             // 2
                                                                             // to
                                                                             // 1
                                                                             // due
                                                                             // to
                                                                             // hash
                                                                             // table
                                                                             // mapping
          possibleValues[3].equals(RQueuedMessage.ALERT_OPTION_NO) && // @B1C
                                                                      // changed
                                                                      // index
                                                                      // from 1
                                                                      // to 3
                                                                      // due to
                                                                      // hash
                                                                      // table
                                                                      // mapping
          possibleValues[2].equals(RQueuedMessage.ALERT_OPTION_UNATTENDED)) // @B1C
                                                                            // changed
                                                                            // index
                                                                            // from
                                                                            // 0
                                                                            // to
                                                                            // 2
                                                                            // due
                                                                            // to
                                                                            // hash
                                                                            // table
                                                                            // mapping
        succeeded();
      else
        failed("Bad possible value returned for attribute ALERT_OPTION.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for DATE_SENT
   **/
  public void Var046() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.DATE_SENT);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for DATE_SENT.");
        return;
      } else {
        if (!presObj.getName().equals("Date Sent")
            || !presObj.getFullName().equals("Date Sent")) {
          failed("Bad presentation object name returned for DATE_SENT");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute DATE_SENT");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute DATE_SENT");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute DATE_SENT");
        return;
      }
      if (!metaData.getID().equals("DATE_SENT")) {
        failed("DATE_SENT not returned for getID for attribute DATE_SENT");
        return;
      }
      if (metaData.getType() != Date.class) {
        failed("Bad class value returned for getType for attribute DATE_SENT");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute DATE_SENT");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute DATE_SENT.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * DEFAULT_REPLY
   **/
  public void Var047() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.DEFAULT_REPLY);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for DEFAULT_REPLY.");
        return;
      } else {
        if (!presObj.getName().equals("Default Reply")
            || !presObj.getFullName().equals("Default Reply")) {
          failed("Bad presentation object name returned for DEFAULT_REPLY");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute DEFAULT_REPLY");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute DEFAULT_REPLY");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute DEFAULT_REPLY");
        return;
      }
      if (!metaData.getID().equals("DEFAULT_REPLY")) {
        failed("DEFAULT_REPLY not returned for getID for attribute DEFAULT_REPLY");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute DEFAULT_REPLY");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute DEFAULT_REPLY");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute DEFAULT_REPLY.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_FILE
   **/
  public void Var048() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.MESSAGE_FILE);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_FILE.");
        return;
      } else {
        if (!presObj.getName().equals("Message File")
            || !presObj.getFullName().equals("Message File")) {
          failed("Bad presentation object name returned for MESSAGE_FILE");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_FILE");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_FILE");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_FILE");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_FILE")) {
        failed("MESSAGE_FILE not returned for getID for attribute MESSAGE_FILE");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_FILE");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_FILE");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_FILE.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_HELP
   **/
  public void Var049() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.MESSAGE_HELP);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_HELP.");
        return;
      } else {
        if (!presObj.getName().equals("Message Help")
            || !presObj.getFullName().equals("Message Help")) {
          failed("Bad presentation object name returned for MESSAGE_HELP");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_HELP");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_HELP");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_HELP");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_HELP")) {
        failed("MESSAGE_HELP not returned for getID for attribute MESSAGE_HELP");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_HELP");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_HELP");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_HELP.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_ID
   **/
  public void Var050() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.MESSAGE_ID);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_ID.");
        return;
      } else {
        if (!presObj.getName().equals("Message ID")
            || !presObj.getFullName().equals("Message ID")) {
          failed("Bad presentation object name returned for MESSAGE_ID");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_ID");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_ID");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_ID");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_ID")) {
        failed("MESSAGE_ID not returned for getID for attribute MESSAGE_ID");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_ID");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_ID");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_ID.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_KEY
   **/
  public void Var051() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.MESSAGE_KEY);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_KEY.");
        return;
      } else {
        if (!presObj.getName().equals("Message Key")
            || !presObj.getFullName().equals("Message Key")) {
          failed("Bad presentation object name returned for MESSAGE_KEY");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_KEY");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_KEY");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_KEY");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_KEY")) {
        failed("MESSAGE_KEY not returned for getID for attribute MESSAGE_KEY");
        return;
      }
      if (metaData.getType() != byte[].class) {
        failed("Bad class value returned for getType for attribute MESSAGE_KEY");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_KEY");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_KEY.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_QUEUE
   **/
  public void Var052() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.MESSAGE_QUEUE);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_QUEUE.");
        return;
      } else {
        if (!presObj.getName().equals("Message Queue")
            || !presObj.getFullName().equals("Message Queue")) {
          failed("Bad presentation object name returned for MESSAGE_QUEUE");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_QUEUE");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_QUEUE");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_QUEUE");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_QUEUE")) {
        failed("MESSAGE_QUEUE not returned for getID for attribute MESSAGE_QUEUE");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_QUEUE");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_QUEUE");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_QUEUE.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_SEVERITY
   **/
  public void Var053() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.MESSAGE_SEVERITY);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_SEVERITY.");
        return;
      } else {
        if (!presObj.getName().equals("Message Severity")
            || !presObj.getFullName().equals("Message Severity")) {
          failed("Bad presentation object name returned for MESSAGE_SEVERITY");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_SEVERITY");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_SEVERITY");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_SEVERITY");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_SEVERITY")) {
        failed("MESSAGE_SEVERITY not returned for getID for attribute MESSAGE_SEVERITY");
        return;
      }
      if (metaData.getType() != Integer.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_SEVERITY");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_SEVERITY");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_SEVERITY.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_TEXT
   **/
  public void Var054() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.MESSAGE_TEXT);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_TEXT.");
        return;
      } else {
        if (!presObj.getName().equals("Message Text")
            || !presObj.getFullName().equals("Message Text")) {
          failed("Bad presentation object name returned for MESSAGE_TEXT");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_TEXT");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_TEXT");
        return;
      }

      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute MESSAGE_TEXT");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_TEXT")) {
        failed("MESSAGE_TEXT not returned for getID for attribute MESSAGE_TEXT");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_TEXT");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_TEXT");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute MESSAGE_TEXT.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * MESSAGE_TYPE
   **/
  public void Var055() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;
    Integer intValue;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.MESSAGE_TYPE);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for MESSAGE_TYPE.");
        return;
      } else {
        if (!presObj.getName().equals("Message Type")
            || !presObj.getFullName().equals("Message Type")) {
          failed("Bad presentation object name returned for MESSAGE_TYPE");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute MESSAGE_TYPE");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute MESSAGE_TYPE");
        return;
      }
      if (metaData.isValueLimited() != true) {
        failed("FALSE returned for isValueLimited for attribute MESSAGE_TYPE");
        return;
      }
      if (!metaData.getID().equals("MESSAGE_TYPE")) {
        failed("MESSAGE_TYPE not returned for getID for attribute MESSAGE_TYPE");
        return;
      }
      if (metaData.getType() != Integer.class) {
        failed("Bad class value returned for getType for attribute MESSAGE_TYPE");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute MESSAGE_TYPE");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 16) {
        failed("Possible values length bad for attribute MESSAGE_TYPE.");
        return;
      }
      intValue = (Integer) possibleValues[0];
      // @B1D if (intValue.intValue() !=
      // QueuedMessage.REPLY_FROM_SYSTEM_REPLY_LIST)
      if (intValue.intValue() != QueuedMessage.REPLY_VALIDITY_CHECKED) // @B1A
      {
        failed("Possible value[0] incorrect.");
        return;
      }
      intValue = (Integer) possibleValues[13];
      // if (intValue.intValue() != QueuedMessage.COMPLETION)
      // @B1D if (intValue.intValue() != QueuedMessage.INFORMATIONAL) // @A2c
      if (intValue.intValue() != QueuedMessage.REPLY_SYSTEM_DEFAULT_USED) // @B1A
      {
        failed("Possible value[13] incorrect.");
        return;
      }

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * REPLY_STATUS
   **/
  public void Var056() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.REPLY_STATUS);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for REPLY_STATUS.");
        return;
      } else {
        if (!presObj.getName().equals("Reply Status")
            || !presObj.getFullName().equals("Reply Status")) {
          failed("Bad presentation object name returned for REPLY_STATUS");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute REPLY_STATUS");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute REPLY_STATUS");
        return;
      }
      if (metaData.isValueLimited() != true) {
        failed("FALSE returned for isValueLimited for attribute REPLY_STATUS");
        return;
      }
      if (!metaData.getID().equals("REPLY_STATUS")) {
        failed("REPLY_STATUS not returned for getID for attribute REPLY_STATUS");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute REPLY_STATUS");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute REPLY_STATUS");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 3) {
        failed("Possible values length bad for attribute REPLY_STATUS.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * SENDER_JOB_NAME
   **/
  public void Var057() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.SENDER_JOB_NAME);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for SENDER_JOB_NAME.");
        return;
      } else {
        if (!presObj.getName().equals("Sender Job Name")
            || !presObj.getFullName().equals("Sender Job Name")) {
          failed("Bad presentation object name returned for SENDER_JOB_NAME");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute SENDER_JOB_NAME");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute SENDER_JOB_NAME");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute SENDER_JOB_NAME");
        return;
      }
      if (!metaData.getID().equals("SENDER_JOB_NAME")) {
        failed("SENDER_JOB_NAME not returned for getID for attribute SENDER_JOB_NAME");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute SENDER_JOB_NAME");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute SENDER_JOB_NAME");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute SENDER_JOB_NAME.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * SENDER_USER_NAME
   **/
  public void Var058() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.SENDER_USER_NAME);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for SENDER_USER_NAME.");
        return;
      } else {
        if (!presObj.getName().equals("Sender User Name")
            || !presObj.getFullName().equals("Sender User Name")) {
          failed("Bad presentation object name returned for SENDER_USER_NAME");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute SENDER_USER_NAME");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute SENDER_USER_NAME");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute SENDER_USER_NAME");
        return;
      }
      if (!metaData.getID().equals("SENDER_USER_NAME")) {
        failed("SENDER_USER_NAME not returned for getID for attribute SENDER_USER_NAME");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute SENDER_USER_NAME");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute SENDER_USER_NAME");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute SENDER_USER_NAME.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * SENDER_JOB_NUMBER
   **/
  public void Var059() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.SENDER_JOB_NUMBER);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for SENDER_JOB_NUMBER.");
        return;
      } else {
        if (!presObj.getName().equals("Sender Job Number")
            || !presObj.getFullName().equals("Sender Job Number")) {
          failed("Bad presentation object name returned for SENDER_JOB_NUMBER");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute SENDER_JOB_NUMBER");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute SENDER_JOB_NUMBER");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute SENDER_JOB_NUMBER");
        return;
      }
      if (!metaData.getID().equals("SENDER_JOB_NUMBER")) {
        failed("SENDER_JOB_NUMBER not returned for getID for attribute SENDER_JOB_NUMBER");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute SENDER_JOB_NUMBER");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute SENDER_JOB_NUMBER");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute SENDER_JOB_NUMBER.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * SENDING_PROGRAM_NAME
   **/
  public void Var060() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray,
          RQueuedMessage.SENDING_PROGRAM_NAME);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for SENDING_PROGRAM_NAME.");
        return;
      } else {
        if (!presObj.getName().equals("Sending Program Name")
            || !presObj.getFullName().equals("Sending Program Name")) {
          failed("Bad presentation object name returned for SENDING_PROGRAM_NAME");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute SENDING_PROGRAM_NAME");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute SENDING_PROGRAM_NAME");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute SENDING_PROGRAM_NAME");
        return;
      }
      if (!metaData.getID().equals("SENDING_PROGRAM_NAME")) {
        failed("SENDING_PROGRAM_NAME not returned for getID for attribute SENDING_PROGRAM_NAME");
        return;
      }
      if (metaData.getType() != String.class) {
        failed("Bad class value returned for getType for attribute SENDING_PROGRAM_NAME");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute SENDING_PROGRAM_NAME");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute SENDING_PROGRAM_NAME.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /***
   * getAttributeMetaData(attributeID) -- Valid use. Get meta data for
   * SUBSTITUTION_DATA
   **/
  public void Var061() {
    RQueuedMessage qmsg;
    ResourceMetaData[] metaDataArray;
    ResourceMetaData metaData;
    Presentation presObj;
    Object[] possibleValues;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      metaDataArray = (ResourceMetaData[]) qmsg.getAttributeMetaData();
      metaData = findMetaData(metaDataArray, RQueuedMessage.SUBSTITUTION_DATA);
      // Check all meta data values
      presObj = metaData.getPresentation();
      if (presObj == null) {
        failed("Null presentation object returned for SUBSTITUTION_DATA.");
        return;
      } else {
        if (!presObj.getName().equals("Substitution Data")
            || !presObj.getFullName().equals("Substitution Data")) {
          failed("Bad presentation object name returned for SUBSTITUTION_DATA");
          return;
        }
      }
      if (metaData.areMultipleAllowed() != false) {
        failed("TRUE returned for areMultipleAllowed for attribute SUBSTITUTION_DATA");
        return;
      }
      if (metaData.isReadOnly() != true) {
        failed("FALSE returned for isReadOnly for attribute SUBSTITUTION_DATA");
        return;
      }
      if (metaData.isValueLimited() != false) {
        failed("TRUE returned for isValueLimited for attribute SUBSTITUTION_DATA");
        return;
      }
      if (!metaData.getID().equals("SUBSTITUTION_DATA")) {
        failed("SUBSTITUTION_DATA not returned for getID for attribute SUBSTITUTION_DATA");
        return;
      }
      if (metaData.getType() != byte[].class) {
        failed("Bad class value returned for getType for attribute SUBSTITUTION_DATA");
        return;
      }
      if (metaData.getDefaultValue() != null) {
        failed("Null not returned for getDefaultValue for attribute SUBSTITUTION_DATA");
        return;
      }
      possibleValues = metaData.getPossibleValues();
      if (possibleValues.length != 0) {
        failed("Possible values length bad for attribute SUBSTITUTION_DATA.");
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getPresentation() -- Test valid use; Get presentation object of queued
   * message.
   **/
  public void Var062() {
    Presentation presObj;
    RQueuedMessage qmsg;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      presObj = qmsg.getPresentation();
      if (onAS400_) {
        if (presObj != null
            && presObj.getFullName().equals(
                "/QSYS.LIB/" + testLib_ + ".LIB/MQT.MSGQ-(0x00000180)")
            && presObj.getName().equals("(0x00000180)")
            && presObj.getValue(Presentation.DESCRIPTION_TEXT).toString()
                .equals("Test message"))
          succeeded();
        else
          failed("Bad presentation info returned.");
      } else {
        if (presObj != null
            && presObj.getFullName().equals(
                "/QSYS.LIB/" + testLib_ + ".LIB/MQT.MSGQ-(0x00000180)")
            && presObj.getName().equals("(0x00000180)")
            && (presObj.getValue(Presentation.ICON_COLOR_16x16) != null)
            && (presObj.getValue(Presentation.ICON_COLOR_32x32) != null)
            && presObj.getValue(Presentation.DESCRIPTION_TEXT).toString()
                .equals("Test message"))
          succeeded();
        else
          failed("Bad presentation info returned.");
      }
    } catch (Exception e) {
      if (exceptionIs(e, "IllegalStateException", "open"))
        succeeded();
      else
        failed(e, "Wrong exception info.");
    }
  }

  /**
   * getResourceKey() -- Test valid use; Get resource key of queued message and
   * make sure each is unique.
   **/
  public void Var063() {
    Object resKey;
    RQueuedMessage qmsg;
    int i;
    Integer intResKey;
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      if (qmsg.getResourceKey() != null)
        succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * receive() -- Test receiving a SENDERS_COPY message prior to its
   * corresponding INQUIRY message
   **/
  public void Var064() {
    RQueuedMessage qmsg, qmsg2, qmsgrcv, qmsgrcv2;
    byte msgKey0[], msgKey1[];
    String value, value1, value2, value3;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInformational("Hi there");
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.open();
      g.open();
      qmsg = (RQueuedMessage) g.resourceAt(0);
      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = g.receive(msgKey0);
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getID() -- Valid use. Get value for MESSAGE_ID for an informational message
   **/
  public void Var065() {
    RQueuedMessage qmsg, qmsg1, qmsg2, qmsgrcv, qmsgrcv1, qmsgrcv2, qmsg3, qmsgrcv3;
    byte msgKey0[], msgKey1[], msgKey2[], msgKey3[];
    String value, value1, value2, value3, value4, value5, value6, value7;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.sendInformational("Message without an ID");
      f.sendInformational("CAE0002", msgfPath);
      f.open();
      g.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      qmsg2 = (RQueuedMessage) f.resourceAt(2);
      qmsg3 = (RQueuedMessage) g.resourceAt(0);

      value = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value1 = (String) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value2 = (String) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value3 = (String) qmsg3.getAttributeValue(RQueuedMessage.MESSAGE_ID);

      if (!value.equals("CPA1E01")) {
        failed("Bad message ID value. Value should be CPA1E01. Value = "
            + value);
        return;
      }
      if (value1.length() != 0) {
        failed("Non blank ID returned for msg with no ID. Value = " + value1);
        return;
      }
      if (!value2.equals("CAE0002")) {
        failed("Bad message ID value. Value should be CAE0002. Value2 = "
            + value2);
        return;
      }
      if (!value3.equals("CPA1E01")) {
        failed("Bad message ID value. Value should be CPA1E01. Value3 = "
            + value3);
        return;
      }

      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey3 = (byte[]) qmsg3.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      qmsgrcv1 = f.receive(msgKey1);
      qmsgrcv2 = f.receive(msgKey2);
      qmsgrcv3 = g.receive(msgKey3, 0, RMessageQueue.SAME, RMessageQueue.COPY);

      value4 = (String) qmsgrcv.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value5 = (String) qmsgrcv1.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value6 = (String) qmsgrcv2.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      value7 = (String) qmsgrcv3.getAttributeValue(RQueuedMessage.MESSAGE_ID);

      if (!value4.equals("CPA1E01")) {
        failed("Bad message ID value. Value should be CPA1E01. Value4 = "
            + value4);
        return;
      }
      if (value5.length() != 0) {
        failed("Non blank ID returned for msg with no ID. Value5 = " + value5);
        return;
      }
      if (!value6.equals("CAE0002")) {
        failed("Bad message ID value. Value should be CAE0002. Value6 = "
            + value6);
        return;
      }
      if (!value7.equals("CPA1E01")) {
        failed("Bad message ID value. Value should be CPA1E01. Value7 = "
            + value7);
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getKey() -- Valid use. Get value for MESSAGE_KEY for an informational,
   * inquiry and a senders copy message
   **/
  public void Var066() {
    RQueuedMessage qmsg, qmsg1, qmsg2, qmsgrcv, qmsgrcv1, qmsgrcv2, qmsg3, qmsgrcv3;
    byte msgKey0[], msgKey1[], msgKey2[], msgKey3[];
    byte[] value, value1, value2, value3, value4, value5, value6, value7;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.sendInquiry("CPA1E01", msgfPath, replyPath);
      f.sendInformational("Message without an ID");
      f.sendInformational("CAE0002", msgfPath);
      f.open();
      g.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      qmsg1 = (RQueuedMessage) f.resourceAt(1);
      qmsg2 = (RQueuedMessage) f.resourceAt(2);
      qmsg3 = (RQueuedMessage) g.resourceAt(0);

      value = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      value1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      value2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      value3 = (byte[]) qmsg3.getAttributeValue(RQueuedMessage.MESSAGE_KEY);

      if (value == null
          || !value.equals(qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY))) {
        failed("Bad message KEY value. Value = " + value);
        return;
      }
      if (value1 == null
          || !value1
              .equals(qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY))) {
        failed("Bad message KEY value. Value1 = " + value1);
        return;
      }
      if (value2 == null
          || !value2
              .equals(qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY))) {
        failed("Bad message KEY value. Value2 = " + value2);
        return;
      }
      if (value3 == null
          || !value3
              .equals(qmsg3.getAttributeValue(RQueuedMessage.MESSAGE_KEY))) {
        failed("Bad message KEY value. Value3 = " + value3);
        return;
      }

      msgKey0 = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey1 = (byte[]) qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey2 = (byte[]) qmsg2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      msgKey3 = (byte[]) qmsg3.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey0);
      qmsgrcv1 = f.receive(msgKey1);
      qmsgrcv2 = f.receive(msgKey2);
      qmsgrcv3 = g.receive(msgKey3);
      value4 = (byte[]) qmsgrcv.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      value5 = (byte[]) qmsgrcv1.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      value6 = (byte[]) qmsgrcv2.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      value7 = (byte[]) qmsgrcv3.getAttributeValue(RQueuedMessage.MESSAGE_KEY);

      if (value4 == null
          || !value4.equals(qmsgrcv
              .getAttributeValue(RQueuedMessage.MESSAGE_KEY))) {
        failed("Bad message KEY value. Value4 = " + value4);
        return;
      }
      if (value5 == null
          || !value5.equals(qmsgrcv1
              .getAttributeValue(RQueuedMessage.MESSAGE_KEY))) {
        failed("Bad message KEY value. Value5 = " + value5);
        return;
      }
      if (value6 == null
          || !value6.equals(qmsgrcv2
              .getAttributeValue(RQueuedMessage.MESSAGE_KEY))) {
        failed("Bad message KEY value. Value6 = " + value6);
        return;
      }
      if (value7 == null
          || !value7.equals(qmsgrcv3
              .getAttributeValue(RQueuedMessage.MESSAGE_KEY))) {
        failed("Bad message KEY value. Value7 = " + value7);
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * toString() -- Valid use.
   **/
  public void Var067() {
    RQueuedMessage qmsg;
    String toStr;
    Presentation presObj;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("Test message");
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      toStr = qmsg.toString();
      presObj = qmsg.getPresentation();
      if (toStr.equals(presObj.getFullName()))
        succeeded();
      else
        failed("Bad toString() value returned.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue() -- Verify all attribute values for a predefined message
   * in a file
   **/
  public void Var068() {
    RQueuedMessage qmsg, qmsgrcv;
    byte msgKey[];
    String tempStr;
    Integer sev, msgType;
    Presentation presObj;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("CAE0002", msgfPath);
      f.open();
      qmsg = (RQueuedMessage) f.resourceAt(0);
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      if (!tempStr.toUpperCase().equals("CAE0002")) {
        failed("Bad message ID returned for CAE0002. Text = " + tempStr);
        return;
      }
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT);
      if (!tempStr.toUpperCase().equals(
          "NEW LEVEL OF CSP/AE REQUIRED FOR APPLICATION.")) {
        failed("Bad message text returned for CAE0002. Text = "
            + qmsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT));
        return;
      }
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_QUEUE);
      if (!tempStr.toUpperCase().equals(
          "/QSYS.LIB/" + testLib_ + ".LIB/MQT.MSGQ")) {
        failed("Bad message queue returned for CAE0002. Text = " + tempStr);
        return;
      }
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.DEFAULT_REPLY);
      if (!tempStr.toUpperCase().equals("")) {
        failed("Bad default reply returned for CAE0002. Text = " + tempStr);
        return;
      }
      tempStr = (String) qmsg
          .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME);
      if (!tempStr.toUpperCase().equals("QZRCSRVS")) {
        // If running on-thread, then tolerate a different name.
        if (!MessageTest.runningOnThread_) {
          failed("Bad SENDING_PROGRAM_NAME returned for CAE0002. Text = "
              + tempStr);
          return;
        }
      }
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.ALERT_OPTION);
      if (tempStr != null) {
        failed("Bad alert option returned for CAE0002. Text = " + tempStr);
        return;
      }
      sev = (Integer) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY);
      if (sev.intValue() != 40) {
        failed("Bad severity returned for CAE0002. Text = " + sev.intValue());
        return;
      }
      msgType = (Integer) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE);
      if (msgType.intValue() != QueuedMessage.INFORMATIONAL) {
        failed("Bad message type returned for CAE0002. Text = "
            + msgType.intValue());
        return;
      }
      msgKey = (byte[]) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY);
      qmsgrcv = f.receive(msgKey);
      tempStr = (String) qmsgrcv.getAttributeValue(RQueuedMessage.MESSAGE_FILE);
      if (!tempStr.toUpperCase().equals("/QSYS.LIB/QCPFMSG.MSGF")) {
        failed("Bad message file option returned for CAE0002. Text = "
            + tempStr);
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getAttributeValue() -- Verify all attribute values for a predefined message
   * in a file
   **/
  public void Var069() {
    RQueuedMessage qmsg, qmsgrcv;
    byte msgKey[];
    String tempStr;
    Integer sev, msgType;
    Presentation presObj;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String msgfPath = QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF");

      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("CAE0002", msgfPath);
      f.open();
      qmsg = f.receive(null, 0, RMessageQueue.SAME, RMessageQueue.ANY);
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_ID);
      if (!tempStr.toUpperCase().equals("CAE0002")) {
        failed("Bad message ID returned for CAE0002. Text = " + tempStr);
        return;
      }
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT);
      if (!tempStr.toUpperCase().equals(
          "NEW LEVEL OF CSP/AE REQUIRED FOR APPLICATION.")) {
        failed("Bad message text returned for CAE0002. Text = "
            + qmsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT));
        return;
      }
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_QUEUE);
      if (tempStr != null) {
        failed("Bad message queue returned for CAE0002. Text = " + tempStr);
        return;
      }
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.DEFAULT_REPLY);
      if (tempStr != null) {
        failed("Bad default reply returned for CAE0002. Text = " + tempStr);
        return;
      }
      tempStr = (String) qmsg
          .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME);
      if (!tempStr.toUpperCase().equals("QZRCSRVS")) {
        // If running on-thread, then tolerate a different name.
        if (!MessageTest.runningOnThread_) {
          failed("Bad SENDING_PROGRAM_NAME returned for CAE0002. Text = "
              + tempStr);
          return;
        }
      }
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.ALERT_OPTION);
      if (!tempStr.equals("*NO")) {
        failed("Bad alert option returned for CAE0002. Text = " + tempStr);
        return;
      }
      sev = (Integer) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY);
      if (sev.intValue() != 40) {
        failed("Bad severity returned for CAE0002. Text = " + sev.intValue());
        return;
      }
      msgType = (Integer) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE);
      if (msgType.intValue() != QueuedMessage.INFORMATIONAL) {
        failed("Bad message type returned for CAE0002. Text = "
            + msgType.intValue());
        return;
      }
      tempStr = (String) qmsg.getAttributeValue(RQueuedMessage.REPLY_STATUS);
      if (tempStr != null) {
        failed("Bad reply status for received message. Text = " + tempStr);
        return;
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }
}
