///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DateTimeConverterTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.Job;
import test.*; 

import com.ibm.as400.resource.RJob; 
import com.ibm.as400.resource.RJobList; 
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.resource.RJobLog;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobLog;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.resource.RQueuedMessage;
import com.ibm.as400.access.QueuedMessage;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.resource.ResourceMetaData;

import test.MessageSandbox;

import com.ibm.as400.resource.Presentation;

import java.util.Date;
import java.util.StringTokenizer;
import java.lang.Thread;

/**
 * Testcase JobLogRFTestcase.
 * 
 * <p>
 * This tests the following BufferedResourceList methods:
 * <ul>
 * <li>close()
 * <li>getSelectionMetaData()
 * <li>getSelectionMetaData(Object)
 * <li>getSelectionValue()
 * <li>getListLength()
 * <li>getPresentation()
 * <li>isComplete()
 * <li>isInError()
 * <li>isOpen()
 * <li>isResourceAvailable()
 * <li>open()
 * <li>refreshContents()
 * <li>refreshStatus()
 * <li>resourceAt()
 * <li>setSelectionValue(Object,Object)
 * <li>toString()
 * <li>getSortMetaData()
 * <li>getSortMetaData(sortID)
 * <li>getSortValue(sortID)
 * <li>setSortOrder(sortID)
 * </ul>
 **/
@SuppressWarnings({"deprecation","unused","resource"})
public class JobLogRFTestcase extends Testcase {

  static final boolean DEBUG = false;

  // Private data.
  static final int variations_ = 109;

  MessageSandbox sandbox_ = null;
  MessageSandbox sandboxReply_ = null;

  private void printErrors(AS400Message[] msgs) {
    for (int i = 0; i < msgs.length; ++i) {
      output_.println(msgs[i]);
    }
  }

  /**
   * Submits the CL program SNDMSGS on the AS/400.
   **/
  private String[] startCLJob() {
    try {
      CommandCall c = new CommandCall(systemObject_);
      // Make sure the program exists
      if (!c.run("CHKOBJ OBJ(JOBLOGTEST/SNDMSGS) OBJTYPE(*PGM)")
          || c.getMessageList().length > 0) {
        output_.println("Program JOBLOGTEST/SNDMSGS does not exist or user");
        output_.println(systemObject_.getUserId()
            + " does not have authority to it");
        output_.println();
        output_
            .println("Run program SetupDriver specifying SetupJobLog for the -tc parameter.");
        return null;
      }

      if (!c
          .run("SBMJOB CMD(CALL PGM(JOBLOGTEST/SNDMSGS)) JOB(JOBLOGTEST) JOBQ(QSYS/QSYSNOMAX)")) {
        printErrors(c.getMessageList());
        return null;
      }
      AS400Message[] msgs = c.getMessageList();
      // Extract the job name, number and user
      String msg = msgs[0].getText();
      int index = msg.indexOf(" SUBMITTED");
      if (index < 0) {
        index = msg.indexOf(" submitted");
        if (index < 0) {
          return null;
        }
      }
      StringTokenizer st = new StringTokenizer(msg.substring(4, index), "/");
      int count = 0;
      String[] s = new String[3];
      while (st.hasMoreTokens()) {
        s[count++] = st.nextToken();
      }
      if (count == 3) {
        // Wait a bit before returning so the job can get off and running
        // long start = System.currentTimeMillis();
        // while (System.currentTimeMillis() - start < 10000)
        // {
        // }
        // Thread.sleep(15000);
        long len = 0;
        while (len == 0) {
          RJobList list = new RJobList(systemObject_);
          list.setSelectionValue(RJobList.JOB_NUMBER, s[0]);
          list.open();
          list.waitForComplete();
          len = list.getListLength();
          list.close();
          Thread.sleep(100);
        }
        return s;
      }
      return null;
    } catch (Exception e) {
      e.printStackTrace(output_);
      return null;
    }
  }

  /**
   * Verifies the messages returned from getMessages() for the submitted job
   * that called SNDMSGS.
   **/

  private boolean verifyCLJobMessages(RJobLog j, String[] job) {
    try {
      int i;
      int count = 0;
      for (i = 0; i < j.getListLength(); ++i) {
        RQueuedMessage m = (RQueuedMessage) j.resourceAt(i);
        if (((String) m.getAttributeValue(RQueuedMessage.SENDER_JOB_NAME))
            .length() != 0) {
          output_.println("Message " + String.valueOf(count) + ":");
          output_.println("From job name not null: "
              + m.getAttributeValue(RQueuedMessage.SENDER_JOB_NAME));
          return false;
        }
        if (((String) m.getAttributeValue(RQueuedMessage.SENDER_JOB_NUMBER))
            .length() != 0) {
          output_.println("Message " + String.valueOf(count) + ":");
          output_.println("From job number not null: "
              + m.getAttributeValue(RQueuedMessage.SENDER_JOB_NUMBER));
          return false;
        }
        if (m.getAttributeValue(RQueuedMessage.MESSAGE_KEY) == null) {
          output_.println("Message " + String.valueOf(count) + ":");
          output_.println("Key is null: ");
          return false;
        }
        if (!((String) m.getAttributeValue(RQueuedMessage.REPLY_STATUS))
            .equals("N")) {
          output_.println("Message " + String.valueOf(count) + ":");
          output_.println("Reply status not null: "
              + m.getAttributeValue(RQueuedMessage.REPLY_STATUS));
          return false;
        }
        if (((String) m.getAttributeValue(RQueuedMessage.SENDER_USER_NAME))
            .length() != 0) {
          output_.println("Message " + String.valueOf(count) + ":");
          output_.println("User not null: "
              + m.getAttributeValue(RQueuedMessage.SENDER_USER_NAME));
          return false;
        }
        String pgm1 = "SNDMSGS";
        String pgm2 = "QWTSCSBJ";
        String pgm3 = "QWTPIIPP";
        String msgTxt1 = "New level of CSP/AE required for application.";
        String msgTxt2 = "Just an old-fashioned message";
        String msgTxt3 = "CALL PGM(JOBLOGTEST/SNDMSGS)";
        String msgTxt4 = "Job " + job[1] + "/" + job[1] + "/" + job[1]
            + " submitted.";
        String msgId1 = "CAE0002";
        String msgId2 = "";
        String msgId3 = "";
        String msgId4 = "CPI1125";

        switch (count) {
        case 4:
          if (!((String) m
              .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME))
              .equals(pgm1)) {
            output_.println("Message " + String.valueOf(count) + ":");
            output_.println("From program incorrect: "
                + m.getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME));
            output_.println(" > Expected: " + pgm1);
            return false;
          }
          if (!((String) m.getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
              .equalsIgnoreCase(msgTxt1)) {
            output_.println("Message " + String.valueOf(count) + ":");
            output_.println("Message text incorrect: "
                + m.getAttributeValue(RQueuedMessage.MESSAGE_TEXT));
            output_.println(" > Expected: " + msgTxt1);
            return false;
          }
          if (!((String) m.getAttributeValue(RQueuedMessage.MESSAGE_ID))
              .equals(msgId1)) {
            output_.println("Message " + String.valueOf(count) + ":");
            output_.println("Message id incorrect: "
                + m.getAttributeValue(RQueuedMessage.MESSAGE_ID));
            output_.println(" > Expected: " + msgId1);
            return false;
          }
          break;
        case 3:
          if (!((String) m
              .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME))
              .equals(pgm1)) {
            output_.println("Message " + String.valueOf(count) + ":");
            output_.println("From program incorrect: "
                + m.getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME));
            output_.println(" > Expected: " + pgm1);
            return false;
          }
          if (!((String) m.getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
              .equalsIgnoreCase(msgTxt2)) {
            output_.println("Message " + String.valueOf(count) + ":");
            output_.println("Message text incorrect: "
                + m.getAttributeValue(RQueuedMessage.MESSAGE_TEXT));
            output_.println(" > Expected: " + msgTxt2);
            return false;
          }
          if (!((String) m.getAttributeValue(RQueuedMessage.MESSAGE_ID))
              .equals(msgId2)) {
            output_.println("Message " + String.valueOf(count) + ":");
            output_.println("Message id incorrect: "
                + m.getAttributeValue(RQueuedMessage.MESSAGE_ID));
            output_.println(" > Expected: " + msgId2);
            return false;
          }
          break;
        case 2:
          if (!((String) m
              .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME))
              .equals(pgm2)) {
            output_.println("Message " + String.valueOf(count) + ":");
            output_.println("From program incorrect: "
                + m.getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME));
            output_.println(" > Expected: " + pgm2);
            return false;
          }
          if (!((String) m.getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
              .equalsIgnoreCase(msgTxt3)) {
            output_.println("Message " + String.valueOf(count) + ":");
            output_.println("Message text incorrect: "
                + m.getAttributeValue(RQueuedMessage.MESSAGE_TEXT));
            output_.println(" > Expected: " + msgTxt3);
            return false;
          }
          if (!((String) m.getAttributeValue(RQueuedMessage.MESSAGE_ID))
              .equals(msgId3)) {
            output_.println("Message " + String.valueOf(count) + ":");
            output_.println("Message id incorrect: "
                + m.getAttributeValue(RQueuedMessage.MESSAGE_ID));
            output_.println(" > Expected: " + msgId3);
            return false;
          }
          break;
        }
        count++;
      }
    } catch (Exception x) {
      x.printStackTrace(output_);
      return false;
    }
    return true;
  }

  /**
   * close() -- Valid close with default constructor followed by an open.
   **/
  public void Var001() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * close() - Valid close with constructor JobLog(AS400)
   **/
  public void Var002() {
    try {
      RJobLog f = new RJobLog(systemObject_);
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * close() - Valid close with constructor JobLog(AS400, String, String,
   * String)
   **/
  public void Var003() {
    try {
      RJobLog f = new RJobLog(systemObject_, "name", "user", "number");
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * close() - Valid close with constructor JobLog(AS400) plus getName()
   **/
  public void Var004() {
    try {
      RJobLog f = new RJobLog(systemObject_);
      f.setName("name");
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * close() - Valid close without first doing an open.
   **/
  public void Var005() {
    try {
      RJobLog f = new RJobLog(systemObject_);
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * close() - Do multiple closes.
   **/
  public void Var006() {
    try {
      RJobLog f = new RJobLog(systemObject_);
      f.close();
      f.close();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object AttributeID) -- Test invalid use; pass null for
   * attribute ID.
   **/
  public void Var007() {
    ResourceMetaData SelectionData;
    try {
      RJobLog f = new RJobLog(systemObject_);
      SelectionData = f.getSelectionMetaData(null);
      f.close();
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) -- call with default job log
   * constructor
   **/
  public void Var008() {
    try {
      ResourceMetaData SelectionData;
      RJobLog f = new RJobLog();
      SelectionData = f.getSelectionMetaData(RJobLog.LIST_DIRECTION);
      succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) -- call with default job log
   * constructor
   **/
  public void Var009() {
    try {
      ResourceMetaData SelectionData;
      RJobLog f = new RJobLog(systemObject_);
      SelectionData = f.getSelectionMetaData(RJobLog.STARTING_MESSAGE_KEY);
      succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object AttributeID) -- Test invalid use; pass bad
   * value for attribute ID.
   **/
  public void Var010() {
    ResourceMetaData SelectionData;
    try {
      RJobLog f = new RJobLog();
      SelectionData = f.getSelectionMetaData(RJobLog.PREVIOUS);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) - Verify LIST_DIRECTION meta data.
   **/
  public void Var011() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    try {
      RJobLog f = new RJobLog();
      SelectionData = f.getSelectionMetaData(RJobLog.LIST_DIRECTION);
      if (!(SelectionData.getID().equals(RJobLog.LIST_DIRECTION)
          && SelectionData.getType().equals(String.class)
          && SelectionData.isReadOnly() == false && SelectionData
            .areMultipleAllowed() == false))
        failed("SelectionData wrong for attributeID, type or isReadOnly.");
      else {
        possibleValues = SelectionData.getPossibleValues();
        if (possibleValues.length == 2
            && ((possibleValues[0].equals(RJobLog.NEXT) && possibleValues[1]
                .equals(RJobLog.PREVIOUS)) || (possibleValues[0]
                .equals(RJobLog.PREVIOUS) && possibleValues[1]
                .equals(RJobLog.NEXT)))) {
          if ((SelectionData.getDefaultValue()).equals(RJobLog.NEXT)
              && SelectionData.isValueLimited() == true)
            succeeded();
          else
            failed("SelectionData wrong for getDefaultValue() or isValueLimited()");
        } else
          failed("SelectionData wrong for getPossibleValues()");
      }

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) - Verify LIST_DIRECTION
   * getPresentation() meta data
   **/
  public void Var012() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    Presentation presObj;
    try {
      RJobLog f = new RJobLog();
      SelectionData = f.getSelectionMetaData(RJobLog.LIST_DIRECTION);
      presObj = SelectionData.getPresentation();
      if (presObj != null && presObj.getFullName().equals("List Direction")
          && presObj.getName().equals("List Direction")
          && (presObj.getValue(Presentation.ICON_COLOR_16x16) == null)
          && (presObj.getValue(Presentation.ICON_COLOR_32x32) == null)
          && (presObj.getValue(Presentation.HELP_TEXT) == null)
          && presObj.getValue(Presentation.DESCRIPTION_TEXT) == null)
        succeeded();
      else
        failed("Bad presentation info returned.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) - Verify STARTING_MESSAGE_KEY.
   **/
  public void Var013() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    try {
      RJobLog f = new RJobLog();
      SelectionData = f.getSelectionMetaData(RJobLog.STARTING_MESSAGE_KEY);
      if (!(SelectionData.getID().equals(RJobLog.STARTING_MESSAGE_KEY)
          && SelectionData.getType().equals(byte[].class)
          && SelectionData.isReadOnly() == false && SelectionData
            .areMultipleAllowed() == false))
        failed("SelectionData wrong for attributeID, areMultipleAllowed, type or isReadOnly.");
      else {
        possibleValues = SelectionData.getPossibleValues();

        if (possibleValues.length == 2
            && (possibleValues[0].equals(RJobLog.OLDEST) || possibleValues[0]
                .equals(RJobLog.NEWEST))
            && (possibleValues[1].equals(RJobLog.NEWEST) || possibleValues[1]
                .equals(RJobLog.OLDEST))) {
          if (SelectionData.getDefaultValue().equals(RJobLog.OLDEST)
              && SelectionData.isValueLimited() == false)
            succeeded();
          else
            failed("SelectionData wrong for getDefaultValue() or isValueLimited()");
        } else
          failed("SelectionData wrong for getPossibleValues()");

      }

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) - Verify STARTING_MESSAGE_KEY
   * getPresentation() meta data
   **/
  public void Var014() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    Presentation presObj;
    try {
      RJobLog f = new RJobLog(systemObject_);
      SelectionData = f.getSelectionMetaData(RJobLog.STARTING_MESSAGE_KEY);
      presObj = SelectionData.getPresentation();
      if (presObj != null
          && presObj.getFullName().equals("Starting Message Key")
          && presObj.getName().equals("Starting Message Key")
          && (presObj.getValue(Presentation.HELP_TEXT) == null)
          && (presObj.getValue(Presentation.ICON_COLOR_16x16) == null)
          && (presObj.getValue(Presentation.ICON_COLOR_32x32) == null)
          && presObj.getValue(Presentation.DESCRIPTION_TEXT) == null)
        succeeded();
      else
        failed("Bad presentation info returned.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify valid with default constructor
   **/
  public void Var015() {
    ResourceMetaData[] SelectionData;
    try {
      RJobLog f = new RJobLog();
      SelectionData = f.getSelectionMetaData();
      if (SelectionData.length == 2)
        succeeded();
      else
        failed("Length of ResourceMetaData array incorrect.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify that the LIST_DIRECTION Selection is
   * retrieved correctly
   **/
  public void Var016() {
    ResourceMetaData[] SelectionData;
    Object[] possibleValues;
    try {
      RJobLog f = new RJobLog();
      SelectionData = f.getSelectionMetaData();
      if (SelectionData[1].getID().equals(RJobLog.LIST_DIRECTION)) {
        if (!(SelectionData[1].getType().equals(String.class)
            && SelectionData[1].isReadOnly() == false && SelectionData[1]
              .areMultipleAllowed() == false))
          failed("SelectionData[0] wrong for attributeID, type, multipleAllowed or isReadOnly.");
        else {
          possibleValues = SelectionData[1].getPossibleValues();
          if (possibleValues.length == 2
              && (possibleValues[0].equals(RJobLog.NEXT) || possibleValues[0]
                  .equals(RJobLog.PREVIOUS))
              && (possibleValues[1].equals(RJobLog.NEXT) || possibleValues[1]
                  .equals(RJobLog.PREVIOUS))) {
            if ((SelectionData[1].getDefaultValue()).equals(RJobLog.NEXT)
                && SelectionData[1].isValueLimited() == true)
              succeeded();
            else
              failed("SelectionData[1] wrong for getDefaultValue() or isValueLimited()");
          } else
            failed("SelectionData[1] wrong for getPossibleValues()");
        }
      } else
        failed("LIST_DIRECTION Selection not retrieved.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify that the STARTING_MESSAGE_KEY Selection is
   * retrieved correctly
   **/
  public void Var017() {
    ResourceMetaData[] SelectionData;
    Object[] possibleValues;
    try {
      RJobLog f = new RJobLog();
      SelectionData = f.getSelectionMetaData();
      if (SelectionData[0].getID().equals(RJobLog.STARTING_MESSAGE_KEY)) {
        if (!(SelectionData[0].getType().equals(byte[].class)
            && SelectionData[0].isReadOnly() == false && SelectionData[0]
              .areMultipleAllowed() == false))
          failed("SelectionData[0] wrong for attributeID, type, areMultipleAllowed or isReadOnly.");
        else {
          possibleValues = SelectionData[0].getPossibleValues();
          if (possibleValues.length == 2
              && (possibleValues[0].equals(RJobLog.OLDEST) || possibleValues[0]
                  .equals(RJobLog.NEWEST))
              && (possibleValues[1].equals(RJobLog.NEWEST) || possibleValues[1]
                  .equals(RJobLog.OLDEST))) {
            if (SelectionData[0].getDefaultValue().equals(RJobLog.OLDEST)
                && SelectionData[0].isValueLimited() == false)
              succeeded();
            else
              failed("SelectionData[0] wrong for getDefaultValue() or isValueLimited()");
          } else
            failed("SelectionData[0] wrong for getPossibleValues()");
        }
      } else
        failed("STARTING_MESSAGE_KEY Selection not retrieved.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionValue(Object AttributeID) -- Test invalid use; pass null for
   * attribute ID.
   **/
  public void Var018() {
    Object SelectionValue;
    try {
      RJobLog f = new RJobLog(systemObject_);
      SelectionValue = f.getSelectionValue(null);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * getSelectionValue(Object attributeID) -- call with default message queue
   * constructor
   **/
  public void Var019() {
    try {
      Object SelectionValue;
      RJobLog f = new RJobLog();
      SelectionValue = f.getSelectionValue(RJobLog.LIST_DIRECTION);
      succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionValue(Object AttributeID) -- Test invalid use; pass bad value
   * for attribute ID.
   **/
  public void Var020() {
    Object SelectionValue;
    try {
      RJobLog f = new RJobLog();
      SelectionValue = f.getSelectionValue(RJobLog.PREVIOUS);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * getSelectionValue(Object AttributeID) -- Test valid use; pass value for
   * attribute ID and check return to ensure the default value is returned.
   **/
  public void Var021() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RJobLog f = new RJobLog();
      SelectionValue = f.getSelectionValue(RJobLog.LIST_DIRECTION);
      SelectionData = f.getSelectionMetaData(RJobLog.LIST_DIRECTION);
      if (SelectionValue.equals(SelectionData.getDefaultValue()))
        succeeded();
      else
        failed("Default value not returned.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * getSelectionValue(Object AttributeID) -- Test valid use; pass value for
   * attribute ID and check return to ensure the default value is returned.
   **/
  public void Var022() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RJobLog f = new RJobLog();
      SelectionValue = f.getSelectionValue(RJobLog.STARTING_MESSAGE_KEY);
      SelectionData = f.getSelectionMetaData(RJobLog.STARTING_MESSAGE_KEY);
      if (SelectionValue.equals(SelectionData.getDefaultValue()))
        succeeded();
      else
        failed("Default value not returned.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * getSelectionValue(Object AttributeID) -- Test valid use; getSelectionValue,
   * reset the value and then get again and ensure the changed value is
   * returned.
   **/
  public void Var023() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RJobLog f = new RJobLog();
      SelectionValue = f.getSelectionValue(RJobLog.LIST_DIRECTION);
      SelectionData = f.getSelectionMetaData(RJobLog.LIST_DIRECTION);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RJobLog.LIST_DIRECTION, RJobLog.PREVIOUS);
        SelectionValue = f.getSelectionValue(RJobLog.LIST_DIRECTION);
        if (SelectionValue.equals(RJobLog.PREVIOUS)) {
          // Reset value back to default
          f.setSelectionValue(RJobLog.LIST_DIRECTION,
              SelectionData.getDefaultValue());
          SelectionValue = f.getSelectionValue(RJobLog.LIST_DIRECTION);
          if (SelectionValue.equals(SelectionData.getDefaultValue()))
            succeeded();
          else
            failed("Default Selection value not returned.");
        } else
          failed("Re-set Selection value not returned.");
      } else
        failed("Default value not returned.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * getSelectionValue(Object AttributeID) -- Test valid use; getSelectionValue,
   * reset the value and then get again and ensure the changed value is
   * returned.
   **/
  public void Var024() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RJobLog f = new RJobLog();
      SelectionValue = f.getSelectionValue(RJobLog.STARTING_MESSAGE_KEY);
      SelectionData = f.getSelectionMetaData(RJobLog.STARTING_MESSAGE_KEY);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RJobLog.STARTING_MESSAGE_KEY, RJobLog.NEWEST);
        SelectionValue = f.getSelectionValue(RJobLog.STARTING_MESSAGE_KEY);
        if (SelectionValue.equals(RJobLog.NEWEST)) {
          // Reset value back to default
          f.setSelectionValue(RJobLog.STARTING_MESSAGE_KEY,
              SelectionData.getDefaultValue());
          SelectionValue = f.getSelectionValue(RJobLog.STARTING_MESSAGE_KEY);
          if (SelectionValue.equals(SelectionData.getDefaultValue()))
            succeeded();
          else
            failed("Default Selection value not returned.");
        } else
          failed("Re-set Selection value not returned.");
      } else
        failed("Default value not returned.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * getPresentation() -- Test valid use; Verify presentation object.
   **/
  public void Var025() {
    Presentation presObj;
    try {
      RJobLog f = new RJobLog();
      presObj = f.getPresentation();
      if (presObj != null
          && presObj.getFullName().equals("//*")
          && // @A1C
          presObj.getName().equals("*")
          && // @A1C
          presObj.getValue(Presentation.DESCRIPTION_TEXT).toString()
              .equals("Job Log")
          && presObj.getValue(Presentation.HELP_TEXT) == null
          && presObj.getValue(Presentation.ICON_COLOR_16x16) != null
          && presObj.getValue(Presentation.ICON_COLOR_32x32) != null)
        succeeded();
      else
        failed("Bad presentation info returned.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getPresentation() -- Test valid use; Verify presentation object.
   **/
  public void Var026() {
    Presentation presObj;
    try {
      RJobLog f = new RJobLog(systemObject_, "name", "user", "number");
      presObj = f.getPresentation();

      if (presObj != null && presObj.getFullName().equals("number/user/name")
          && presObj.getName().equals("name")
          && presObj.getValue(Presentation.DESCRIPTION_TEXT).equals("Job Log")
          && presObj.getValue(Presentation.HELP_TEXT) == null
          && presObj.getValue(Presentation.ICON_COLOR_16x16) != null
          && presObj.getValue(Presentation.ICON_COLOR_32x32) != null)
        succeeded();
      else
        failed("Bad presentation info returned.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getPresentation() -- Test valid use; Verify presentation object.
   **/
  public void Var027() {
    Presentation presObj;
    try {
      RJobLog f = new RJobLog(systemObject_);
      presObj = f.getPresentation();

      if (presObj != null
          && presObj.getFullName().equals("//*")
          && // @A1C
          presObj.getName().equals("*")
          && // @A1C
          presObj.getValue(Presentation.DESCRIPTION_TEXT).toString()
              .equals("Job Log")
          && presObj.getValue(Presentation.HELP_TEXT) == null
          && presObj.getValue(Presentation.ICON_COLOR_16x16) != null
          && presObj.getValue(Presentation.ICON_COLOR_32x32) != null)
        succeeded();
      else
        failed("Bad presentation info returned.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * open() -- Invalid open with default constructor. Should throw
   * IllegalStateException
   **/
  public void Var028() {
    try {
      RJobLog f = new RJobLog();
      f.open();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * open() -- Invalid open with default constructor. Should throw
   * IllegalStateException
   **/
  public void Var029() {
    try {
      RJobLog f = new RJobLog(systemObject_);
      f.open();
      succeeded(); // @A1C
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * open() -- Invalid open with default constructor. Should throw
   * IllegalStateException
   **/
  public void Var030() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.open();
      succeeded(); // @A1C
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * open() -- Invalid open with default constructor. Should throw
   * IllegalStateException
   **/
  public void Var031() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.setUser("java");
      f.setNumber("00000");
      f.open();
      succeeded(); // @A1C
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * open() -- Invalid open with default constructor. Should throw
   * IllegalStateException
   **/
  public void Var032() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.setUser("java");
      f.setName("job");
      f.open();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * open() -- Invalid open with default constructor. Should throw
   * IllegalStateException
   **/
  public void Var033() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.setNumber("00000");
      f.setName("job");
      f.open();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * open() -- Invalid open with bad name, user and number passed in
   **/
  public void Var034() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.setNumber("aNum");
      f.setName("ajob");
      f.setUser("aUser");
      f.open();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");

    }
  }

  /**
   * open() -- Valid open
   **/
  public void Var035() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setPageSize(20);
      jobList.setNumberOfPages(2);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          if (!f.isOpen()) {
            failed("Open failed.");
            return;
          }
          for (i = 0; i < f.getListLength() && found == false; ++i) {
            RQueuedMessage message = (RQueuedMessage) f.resourceAt(i);
            if (message.getAttributeValue(RQueuedMessage.MESSAGE_ID).equals(id))
              found = true;
          }
          f.close();
        }
      }
      jobList.close();
      if (found)
        succeeded();
      else
        failed("Message ID not found.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");

    }
  }

  /**
   * getSortMetaData() -- Verify that no sort elements are returned
   **/
  public void Var036() {
    int i;
    ResourceMetaData[] sortData;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RJobLog f = new RJobLog(systemObject_);
      sortData = f.getSortMetaData();
      if (sortData.length == 0)
        succeeded();
      else
        failed("Sort data returned in error.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSortMetaData(sortID) -- There are no valid sort IDs for job logs. Pass
   * in a bad value and verify an exception is generated.
   **/
  public void Var037() {
    int i;
    ResourceMetaData sortData;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RJobLog f = new RJobLog(systemObject_);
      sortData = f.getSortMetaData(RJobLog.PREVIOUS);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * getSortMetaData(sortID) -- There are no valid sort IDs for job logs. Pass
   * in null and verify an exception is generated
   **/
  public void Var038() {
    int i;
    ResourceMetaData sortData;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RJobLog f = new RJobLog(systemObject_);
      sortData = f.getSortMetaData(null);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");

    }
  }

  /**
   * getSortValue() -- There are not valid sort IDs for job logs. Pass in a bad
   * value and verify an empty array of sortIDs is generated.
   **/
  public void Var039() {
    int i;
    Object[] sortID;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RJobLog f = new RJobLog(systemObject_);
      sortID = f.getSortValue();
      if (sortID.length == 0)
        succeeded();
      else
        failed("sortIDs returned for job log.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSortOrder(sortID) -- There are no valid sort IDs for job logs. Pass in a
   * bad value and verify an exception is generated.
   **/
  public void Var040() {
    int i;

    try {
      RJobLog f = new RJobLog();
      f.getSortOrder(RJobLog.LIST_DIRECTION);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * setSortOrder(sortID) -- There are no valid sort IDs for job logs. Pass in a
   * bad value and verify an exception is generated.
   **/
  public void Var041() {
    int i;

    try {
      RJobLog f = new RJobLog();
      f.setSortOrder(RJobLog.LIST_DIRECTION, false);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * setSortOrder(sortID) -- There are not valid sort IDs for job logs. Pass in
   * a bad value and verify an exception is generated.
   **/
  public void Var042() {
    int i;

    try {
      RJobLog f = new RJobLog(systemObject_);
      f.setSortOrder(null, true);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * setSortValue(sortValues[]) -- There are not valid sort IDs for job logs.
   * Pass in a bad value and verify an exception is generated.
   **/
  public void Var043() {
    int i;
    Object sortValues[] = { RJobLog.LIST_DIRECTION,
        RJobLog.STARTING_MESSAGE_KEY };

    try {
      RJobLog f = new RJobLog(systemObject_);
      f.setSortValue(sortValues);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");

    }
  }

  /**
   * setSortValue(sortValues[]) -- There are not valid sort IDs for job logs.
   * Pass in a bad value and verify an exception is generated.
   **/
  public void Var044() {
    int i;
    Object sortValues[] = {};

    try {
      RJobLog f = new RJobLog(systemObject_);
      f.setSortValue(sortValues);
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSortValue(sortValues[]) -- There are not valid sort IDs for job logs.
   * Pass in a bad value and verify an exception is generated.
   **/
  public void Var045() {
    int i;
    Object sortValues[] = { null, null, null };
    try {
      RJobLog f = new RJobLog(systemObject_);
      f.setSortValue(sortValues);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * open(). Test valid use
   **/
  public void Var046() {
    RQueuedMessage qmsg;
    int i;
    String[] job = startCLJob();
    if (job == null) {
      failed("CL job not submitted.");
      return;
    }
    try {
      RJobLog j = new RJobLog(systemObject_, job[2], job[1], job[0]);

      j.open();
      if (!j.isOpen()) {
        failed("Open failed.");
        return;
      }

      if (j.getListLength() > 0) {
        for (i = 0; i < j.getListLength(); ++i) {
          // Thread.sleep(30000);
          qmsg = (RQueuedMessage) j.resourceAt(i);
        }
        assertCondition(verifyCLJobMessages(j, job));
      } else
        failed("job not found");
    } catch (Exception e) {
      failed(e, "Exception occurred.");
    }
  }

  /**
   * open() -- Invalid open . Should throw IllegalStateException
   **/
  public void Var047() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.setNumber("");
      f.setName("");
      f.setUser("");
      f.open();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * open() -- Valid open. Open a job log twice.
   **/
  public void Var048() {
    int i, x;
    boolean found;
    RJobList jobList = null;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      jobList = new RJobList(pwrSys_);
      jobList.setPageSize(20);
      jobList.setNumberOfPages(2);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if ((((String) j.getAttributeValue(RJob.COMPLETION_STATUS))).length() == 0) {
          RJobLog f = null;
          try {
            f = new RJobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
            f.open();
            f.open();
            f.open();
            if (!f.isOpen()) {
              failed("Open failed.");
              return;
            }

            if (f.resourceAt(0) != null) {
              succeeded();
              return;
            } else {
              failed("No resource returned from job log.");
              return;
            }
          } finally {
            try {
              f.close();
            } catch (Exception e) {
            }
          }

        }
      }
      jobList.close();
      failed("No jobs matched.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");

    } finally {
      try {
        jobList.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * open() -- Invalid open. Open a job log, change the system and then reopen.
   * This should cause an exception.
   **/
  public void Var049() {
    int i, x;
    boolean found;
    RJobList jobList = null;
    try {
      AS400 as400_ = new AS400("BADAS400", "JAVA", "JTEAM1");
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      jobList = new RJobList(pwrSys_);
      jobList.setPageSize(20);
      jobList.setNumberOfPages(2);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if ((((String) j.getAttributeValue(RJob.COMPLETION_STATUS))).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          f.setSystem(as400_);
          f.open();
          f.close();
          failed("Exception not thrown.");
          return;
        }
      }
      jobList.close();
      failed("No jobs matched.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    } finally {
      try {
        jobList.close();
      } catch (Exception e) {
      }
    }

  }

  /**
   * open() -- Invalid open. Open a job log, change the job name and then
   * reopen. This should cause an exception.
   **/
  public void Var050() {
    int i, x;
    boolean found;
    RJobList jobList = null;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      jobList = new RJobList(pwrSys_);
      jobList.setPageSize(20);
      jobList.setNumberOfPages(2);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if ((((String) j.getAttributeValue(RJob.COMPLETION_STATUS))).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          f.setName("newName");
          f.open();
          f.close();
          failed("Exception not thrown.");
          return;
        }
      }
      jobList.close();
      failed("No jobs matched.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    } finally {
      try {
        jobList.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * open() -- Invalid open. Open a job log, change the job user and then
   * reopen. This should cause an exception.
   **/
  public void Var051() {
    int i, x;
    boolean found;
    RJobList jobList = null;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      jobList = new RJobList(pwrSys_);
      jobList.setPageSize(20);
      jobList.setNumberOfPages(2);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if ((((String) j.getAttributeValue(RJob.COMPLETION_STATUS))).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          f.setUser("newUser");
          f.open();
          f.close();
          failed("Exception not thrown.");
          return;
        }
      }
      jobList.close();
      failed("No jobs matched.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    } finally {
      try {
        jobList.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * open() -- Invalid open. Open a job log, change the job number and then
   * reopen. This should cause an exception.
   **/
  public void Var052() {
    int i, x;
    boolean found;
    RJobList jobList = null;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      jobList = new RJobList(pwrSys_);
      jobList.setPageSize(20);
      jobList.setNumberOfPages(2);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if ((((String) j.getAttributeValue(RJob.COMPLETION_STATUS))).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          f.setNumber("newNum");
          f.open();
          f.close();
          failed("Exception not thrown.");
          return;
        }
      }
      jobList.close();
      failed("No jobs matched.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    } finally {
      try {
        jobList.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * open() -- Invalid open; pass null for number and user
   **/
  public void Var053() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.setNumber(null);
      f.setName("aName");
      f.setUser(null);
      f.open();
      failed("No exception thrown.");
    } catch (Exception e) {
      if (exceptionIs(e, "NullPointerException", "number"))
        succeeded();
      else
        failed(e, "Wrong exception info.");
    }

  }

  /**
   * open() -- Valid open. Open and close a job log a number of times.
   **/
  public void Var054() {
    int i, x;
    boolean found;
    RJobList jobList = null;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      jobList = new RJobList(pwrSys_);
      jobList.setPageSize(20);
      jobList.setNumberOfPages(2);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if ((((String) j.getAttributeValue(RJob.COMPLETION_STATUS))).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          for (i = 0; i < 20; ++i) {
            f.open();
            f.close();
          }
          f.open();
          if (!f.isOpen()) {
            failed("Open failed.");
            return;
          }

          if (f.resourceAt(0) == null) {
            failed("Null resource returned.");
            return;
          }
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");

    } finally {
      try {
        jobList.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test invalid use; Try to get list
   * length prior to open with default constructor. This should fail with an
   * IllegalStateException.
   **/
  public void Var055() {
    try {
      RJobLog f = new RJobLog();
      f.getListLength();
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * open() -- Valid getListLength(). Do prior to open.
   **/
  public void Var056() {
    int i, x;
    boolean found;
    long l;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      ProgramCall pc = new ProgramCall(pwrSys_);
      Job j = pc.getServerJob();
      /*
       * RJobList jobList = new RJobList(pwrSys_);
       * jobList.setSelectionValue(RJobList.JOB_NAME,"QZRCSRVS");
       * jobList.open(); jobList.waitForComplete(); for (x=0;x<
       * jobList.getListLength(); ++x) { RJob j = (RJob) jobList.resourceAt(x);
       * if ( ( (String)
       * j.getAttributeValue(RJob.COMPLETION_STATUS)).length()==0 ) {
       */
      RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
      JobLog g = new JobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
      l = f.getListLength();
      if (l > g.getLength()) {
        failed("Bad list length returned.");
        return;
      }
      /*
       * } } jobList.close();
       */
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");

    }
  }

  /**
   * open() -- Valid getListLength(). Do after opening.
   **/
  public void Var057() {
    int i, x;
    boolean found;
    long l;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      ProgramCall pc = new ProgramCall(pwrSys_);
      Job j = pc.getServerJob();
      /*
       * RJobList jobList = new RJobList(pwrSys_);
       * jobList.setSelectionValue(RJobList.JOB_NAME,"QZRCSRVS");
       * jobList.open(); jobList.waitForComplete(); for (x=0;x<
       * jobList.getListLength(); ++x) { RJob j = (RJob) jobList.resourceAt(x);
       * if ( ( (String)
       * j.getAttributeValue(RJob.COMPLETION_STATUS)).length()==0 ) {
       */
      RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
      JobLog g = new JobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
      f.open();
      f.waitForComplete();
      l = f.getListLength();
      if (l > g.getLength()) {
        failed("Bad list length returned.");
        return;
      }
      /*
       * } } jobList.close();
       */
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");

    }
  }

  /**
   * getListLength() -- Valid use.
   **/
  public void Var058() {
    int i, x;
    boolean found;
    RJobList jobList = null;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      jobList = new RJobList(pwrSys_);
      jobList.setPageSize(20);
      jobList.setNumberOfPages(2);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      long u;
      u = jobList.getListLength();
      for (x = 0; x < u; ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          long w;
          w = f.getListLength();
          for (i = 0; i < w; ++i) {
            RQueuedMessage message = (RQueuedMessage) f.resourceAt(i);
            if (message == null) {
              failed("Bad resource returned");
              return;
            }
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");

    } finally {
      try {
        jobList.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * getListLength() -- Invalid use. Try to getListLength of non-active job log
   **/
  public void Var059() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setPageSize(20);
      jobList.setNumberOfPages(2);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() != 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.getListLength();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      // assertExceptionIsInstanceOf(e,
      // "com.ibm.as400.resource.ResourceException");
      failed(e, "Unexpected exception.");

    }
  }

  /**
   * getListLength() -- Valid use. Add a message to job log and check length
   **/
  public void Var060() {
    int i, x;
    boolean found;
    long length1, length2;
    length1 = 0;
    length2 = 0;
    long y, u;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      ProgramCall pc = new ProgramCall(pwrSys_);
      Job j = pc.getServerJob();
      /*
       * RJobList jobList = new RJobList(pwrSys_);
       * jobList.setSelectionValue(RJobList.JOB_NAME,"QZRCSRVS");
       * jobList.open(); jobList.waitForComplete(); y=jobList.getListLength();
       * for (x=0;x< y; ++x) { RJob j = (RJob) jobList.resourceAt(x); if ( (
       * (String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() ==0) {
       */
      RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
      f.waitForComplete();
      u = f.getListLength();
      for (i = 0; i < u && found == false; ++i) {
        RQueuedMessage message = (RQueuedMessage) f.resourceAt(i);
        if (message.getAttributeValue(RQueuedMessage.MESSAGE_ID).equals(id)) {
          length1 = f.getListLength();
          found = true;
        }
      }
      /*
       * } } jobList.close();
       */
      String id2 = "CAE0002";
      RJobLog.writeMessage(pwrSys_, id2, AS400Message.COMPLETION);
      /*
       * RJobList jobList2 = new RJobList(pwrSys_);
       * jobList2.setSelectionValue(RJobList.JOB_NAME,"QZRCSRVS");
       * jobList2.open(); jobList2.waitForComplete();
       * y=jobList2.getListLength(); for (x=0;x< y; ++x) { RJob j1 = (RJob)
       * jobList2.resourceAt(x); if ( ( (String)
       * j1.getAttributeValue(RJob.COMPLETION_STATUS)).length() ==0) {
       */
      RJobLog f1 = new RJobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
      f1.waitForComplete();
      u = f1.getListLength();
      for (i = 0; i < u && found == false; ++i) {
        RQueuedMessage message = (RQueuedMessage) f1.resourceAt(i);
        if (message.getAttributeValue(RQueuedMessage.MESSAGE_ID).equals(id2)) {
          length2 = f1.getListLength();
          found = true;
        }
      }
      /*
       * } } jobList2.close();
       */
      if (found)
        succeeded();
      else
        failed("Message not found.");
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * close() -- Valid close
   **/
  public void Var061() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          if (f.isOpen())
            f.close();
          else {
            failed("Open failed.");
            return;
          }
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * close() -- Valid close
   **/
  public void Var062() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          if (f.isOpen())
            f.close();
          else {
            failed("Open failed.");
            return;
          }
          f.close();
          f.close();
          f.close();
          f.open();
          if (f.isOpen())
            f.close();
          else {
            failed("Second open failed.");
            return;
          }
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isComplete() -- Test valid use; Check if complete without open
   **/
  public void Var063() {
    try {
      RJobLog f = new RJobLog(systemObject_);
      if (!f.isComplete())
        succeeded();
      else
        failed("isComplete() returning incorrect value.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isComplete() -- Valid isComplete(). waitForComplete and then ensure
   * isComplete is true
   **/
  public void Var064() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          f.waitForComplete();
          if (!f.isComplete()) {
            failed("isComplete() failed.");
            return;
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isComplete() -- Valid isComplete(). Keep checking until complete and then
   * ensure isComplete is true
   **/
  public void Var065() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          while (!f.isComplete()) {
            Thread.sleep(100);
            f.refreshStatus();
          }
          if (!f.isComplete()) {
            failed("isComplete() failed.");
            return;
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isInError() -- Valid isInError(). Keep checking to ensure false until
   * complete and then ensure isInError is false
   **/
  public void Var066() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          while (!f.isComplete()) {
            if (f.isInError()) {
              failed("isInError() returned TRUE.");
              return;
            }
            Thread.sleep(100);
            f.refreshStatus();
          }
          if (f.isInError()) {
            failed("isError() is TRUE.");
            return;
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isOpen() -- Valid isOpen(). Check for implicit open
   **/
  public void Var067() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.getListLength();
          if (!f.isOpen()) {
            failed("isOpen() failed.");
            return;
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isOpen() -- Valid isOpen(). check for implicit open
   **/
  public void Var068() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.resourceAt(0);
          if (!f.isOpen()) {
            failed("isOpen() failed.");
            return;
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isOpen() -- Valid isOpen(). Check for implicit open
   **/
  public void Var069() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.refreshStatus();
          if (!f.isOpen()) {
            failed("isOpen() failed.");
            return;
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isOpen() -- Valid isOpen(). Check for implicit open
   **/
  public void Var070() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.refreshContents(); // Exception happening here.
          if (!f.isOpen()) {
            failed("isOpen() failed.");
            return;
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isOpen() -- Valid isOpen(). Check for implicity open
   **/
  public void Var071() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.isResourceAvailable(0);
          if (f.isOpen()) {
            failed("isOpen() failed.");
            return;
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isOpen() -- Valid isOpen(). Check on explicit open
   **/
  public void Var072() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          if (!f.isOpen()) {
            failed("isOpen() failed.");
            return;
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isOpen() -- Valid isOpen(). Check on explicit open after closing
   **/
  public void Var073() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      long u;
      u = jobList.getListLength();
      for (x = 0; x < u; ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          if (!f.isOpen()) {
            failed("isOpen() failed. Should have returned TRUE");
            return;
          }
          f.close();
          if (f.isOpen()) {
            failed("isOpen() failed. Should have returned FALSE");
            return;
          }

        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isOpen() -- Valid isOpen(). Check after series of opens and closes
   **/
  public void Var074() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          if (!f.isOpen()) {
            failed("isOpen() failed. Should have returned TRUE");
            return;
          }
          f.close();
          if (f.isOpen()) {
            failed("isOpen() failed. Should have returned FALSE");
            return;
          }
          f.open();
          f.open();
          if (!f.isOpen()) {
            failed("isOpen() failed. Should have returned TRUE");
            return;
          }
          f.getListLength();
          f.close();
          if (f.isOpen()) {
            failed("isOpen() failed. Should have returned FALSE");
            return;
          }
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isOpen() -- Invalid isOpen with default constructor and not open
   **/
  public void Var075() {
    try {
      RJobLog f = new RJobLog();
      if (!f.isOpen())
        succeeded();
      else
        failed("isOpen failed.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");

    }
  }

  /**
   * isOpen() -- Invalid isOpen with default constructor and not open
   **/
  public void Var076() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      if (!f.isOpen())
        succeeded();
      else
        failed("isOpen failed.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");

    }
  }

  /**
   * isResourceAvailable() -- Do prior to open. Should return false
   **/
  public void Var077() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      if (!f.isResourceAvailable(0))
        succeeded();
      else
        failed("isResourceAvailable() failed.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");

    }
  }

  /**
   * isResourceAvailable() -- Pass in -1. An IllegalArgumentException should be
   * thrown.
   **/
  public void Var078() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.isResourceAvailable(-1);
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");

    }
  }

  /**
   * isResourceAvailable() -- Valid isResourceAvailable(). Pass in too large of
   * an index. Ensure exception is thrown
   **/
  public void Var079() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          boolean checkResource;
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          f.waitForComplete();
          checkResource = f.isResourceAvailable(f.getListLength() + 500);
          assertCondition(checkResource == false);
          return;
        }
      }
      jobList.close();
    } catch (Exception e) {
      failed(e, "Unknown exception thrown");
    }
  }

  /**
   * isResourceAvailable() -- Valid isResourceAvailable(). Check all resources
   * in list to see if available
   **/
  public void Var080() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          for (i = 0; i < f.getListLength(); ++i) {
            if (!f.isResourceAvailable(i)) {
              failed("isResourceAvailable() failed. Should have returned true");
              return;
            }
          }
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isResourceAvailable() -- Valid isResourceAvailable(). Check all resources
   * in list to see if available after an implicit open
   **/
  public void Var081() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.setSelectionValue(RJobList.PRIMARY_JOB_STATUSES,
          new String[] { RJob.JOB_STATUS_ACTIVE });
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          for (i = 0; i < f.getListLength(); ++i) {
            if (!f.isResourceAvailable(i)) {
              failed("isResourceAvailable() failed. Should have returned true");
              return;
            }
          }
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * refreshContents() -- Valid refreshContents(). Do after open
   **/
  public void Var082() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      long y;
      y = jobList.getListLength();
      for (x = 0; x < y; ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          long u;
          u = f.getListLength();
          for (i = 0; i < u; ++i) {
            f.open();
            f.refreshContents();
            f.close();
          }
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * refreshContents() - Invalid refresh with default constructor. An
   * IllegalStateException should be thrown.
   **/
  public void Var083() {
    try {
      RJobLog f = new RJobLog(systemObject_);
      f.refreshContents();
      succeeded(); // @A1C
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * refreshContents() -- Valid use. Add a message to job log and check length
   **/
  public void Var084() {
    int i, x;
    boolean found;
    long length1, length2;
    length1 = 0;
    length2 = 0;
    RJobLog f, f1;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      ProgramCall pc = new ProgramCall(pwrSys_);
      Job j = pc.getServerJob();
      /*
       * RJobList jobList = new RJobList(pwrSys_);
       * jobList.setSelectionValue(RJobList.JOB_NAME,"QZRCSRVS");
       * jobList.open(); jobList.waitForComplete(); long u; u =
       * jobList.getListLength(); for (x=0;x< u; ++x) { RJob j = (RJob)
       * jobList.resourceAt(x); if ( ( (String)
       * j.getAttributeValue(RJob.COMPLETION_STATUS)).length() ==0) {
       */
      f = new RJobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
      long w;
      w = f.getListLength();
      for (i = 0; i < w && found == false; ++i) {
        f.open();
        f.waitForComplete();
        RQueuedMessage message = (RQueuedMessage) f.resourceAt(i);
        if (message.getAttributeValue(RQueuedMessage.MESSAGE_ID).equals(id)) {
          length1 = f.getListLength();
          found = true;
        }
        f.refreshContents();
      }
      /*
       * } } jobList.close();
       */
      String id2 = "CAE0002";
      RJobLog.writeMessage(pwrSys_, id2, AS400Message.COMPLETION);
      /*
       * RJobList jobList2 = new RJobList(pwrSys_);
       * jobList2.setSelectionValue(RJobList.JOB_NAME,"QZRCSRVS");
       * jobList2.open(); jobList2.waitForComplete(); for (x=0;x<
       * jobList2.getListLength(); ++x) { RJob j1 = (RJob)
       * jobList2.resourceAt(x); if ( ( (String)
       * j1.getAttributeValue(RJob.COMPLETION_STATUS)).length() ==0) {
       */
      f1 = new RJobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
      for (i = 0; i < f1.getListLength() && found == false; ++i) {
        f1.open();
        f1.waitForComplete();
        RQueuedMessage message = (RQueuedMessage) f1.resourceAt(i);
        if (message.getAttributeValue(RQueuedMessage.MESSAGE_ID).equals(id2)) {
          length2 = f1.getListLength();
          found = true;
        }
        f1.refreshContents();
      }
      /*
       * } } jobList2.close();
       */
      if (found)
        succeeded();
      else
        failed("Message not found.");
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * refreshContents() -- Invalid refreshContents(). Do after changing system
   **/
  public void Var085() {
    int i, x;
    boolean found;
    try {
      AS400 newSys_ = new AS400("RCHAS1DD", "JAVA", "JTEAM1");
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          for (i = 0; i < f.getListLength(); ++i) {
            f.setSystem(newSys_);
            f.refreshContents();
            f.close();
          }
        }
      }
      jobList.close();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * refreshContents() -- Invalid refreshContents(). Do after changing name
   **/
  public void Var086() {
    int i, x;
    boolean found;
    try {
      AS400 newSys_ = new AS400("RCHAS1DD", "JAVA", "JTEAM1");
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          for (i = 0; i < f.getListLength(); ++i) {
            f.setName("newName");
            f.refreshContents();
            f.close();
          }
        }
      }
      jobList.close();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * refreshContents() -- Invalid refreshContents(). Do after changing user
   **/
  public void Var087() {
    int i, x;
    boolean found;
    try {
      AS400 newSys_ = new AS400("RCHAS1DD", "JAVA", "JTEAM1");
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      long y;
      y = jobList.getListLength();
      for (x = 0; x < y; ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          long u;
          u = f.getListLength();
          for (i = 0; i < u; ++i) {
            f.setUser("JAVACTL");
            f.refreshContents();
            f.close();
          }
        }
      }
      jobList.close();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * refreshContents() -- Valid refreshContents(). setSelectionValue and
   * refresh. Ensure only selected messages are in log
   **/
  public void Var088() {
    int i, x;
    boolean found;
    long len1, len2;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      long y;
      y = jobList.getListLength();
      for (x = 0; x < y; ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          len1 = f.getListLength();
          // Set the selection value to retrieve all of the messages newer than
          // the OLDEST messages in
          // the queue. This should return all messages.
          // This is based on STARTING_MESSAGE_KEY which is
          // set to OLDEST as the default.
          f.setSelectionValue(RJobLog.LIST_DIRECTION, RJobLog.NEXT);
          f.refreshContents();
          if (f.getListLength() != len1) {
            failed("Bad list length returned.");
            return;
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * refreshContents() -- Valid refreshContents(). setSelectionValue and
   * refresh. Ensure only selected messages are in log
   **/
  public void Var089() {
    int i, x;
    boolean found;
    long len1, len2;
    RQueuedMessage qmsg, qmsg2;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          if (f.getListLength() > 0) {
            qmsg = (RQueuedMessage) f.resourceAt(0);
            // Set the selection value to retrieve all of the messages older
            // than the OLDEST messages in
            // the queue. This should return only the first message in the
            // queue.
            // This is based on STARTING_MESSAGE_KEY which is
            // set to OLDEST as the default.
            f.setSelectionValue(RJobLog.LIST_DIRECTION, RJobLog.PREVIOUS);
            f.refreshContents();
            qmsg2 = (RQueuedMessage) f.resourceAt(0);
            if (f.getListLength() != 1 || !(qmsg.equals(qmsg2))) {
              failed("Bad list length or object returned.");
              return;
            }
            f.close();
          }
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * refreshContents(). Test valid use. setSelectionValue and refresh. Ensure
   * correct messages in log
   **/
  public void Var090() {
    RQueuedMessage qmsg, qmsg2;
    long len1;
    int i;
    String[] job = startCLJob();
    if (job == null) {
      failed("CL job not submitted.");
      return;
    }
    try {
      RJobLog j = new RJobLog(systemObject_, job[2], job[1], job[0]);

      j.open();
      if (!j.isOpen()) {
        failed("Open failed.");
        return;
      }

      len1 = j.getListLength();
      if (len1 > 0) {
        // Set the selection value to retrieve all of the messages older than
        // the NEWEST messages in
        // the queue. This should return all the messages in the queue from
        // newest to oldest
        // This is based on STARTING_MESSAGE_KEY which is
        j.setSelectionValue(RJobLog.STARTING_MESSAGE_KEY, RJobLog.NEWEST);
        j.setSelectionValue(RJobLog.LIST_DIRECTION, RJobLog.PREVIOUS);
        j.refreshContents();
        qmsg = (RQueuedMessage) j.resourceAt(0);
        if (j.getListLength() == len1
            && (((String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
                .indexOf("CSP/AE") != -1))
          succeeded();
        else
          failed("Bad list length returned.");
      } else
        failed("job not found");
    } catch (Exception e) {
      failed(e, "Exception occurred.");
    }
  }

  /**
   * refreshContents(). Test valid use. setSelectionValue and refresh. Ensure
   * correct messages in log
   **/
  public void Var091() {
    RQueuedMessage qmsg, qmsg2;
    long len1;
    int i;
    String[] job = startCLJob();
    if (job == null) {
      failed("CL job not submitted.");
      return;
    }
    try {
      RJobLog j = new RJobLog(systemObject_, job[2], job[1], job[0]);

      j.open();
      if (!j.isOpen()) {
        failed("Open failed.");
        return;
      }

      len1 = j.getListLength();
      if (len1 > 0) {
        // Set the selection value to retrieve all of the messages newer than
        // the NEWEST messages in
        // the queue. This should return the last message in the queue = CSP/AE
        // This is based on STARTING_MESSAGE_KEY which is
        j.setSelectionValue(RJobLog.STARTING_MESSAGE_KEY, RJobLog.NEWEST);
        j.setSelectionValue(RJobLog.LIST_DIRECTION, RJobLog.NEXT);
        j.refreshContents();
        qmsg = (RQueuedMessage) j.resourceAt(0);
        if (j.getListLength() == 1
            && ((String) qmsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
                .indexOf("CSP/AE") != -1)
          succeeded();
        else
          failed("Bad list length returned.");
      } else
        failed("job not found");
    } catch (Exception e) {
      failed(e, "Exception occurred.");
    }
  }

  /**
   * refreshContents(). Test valid use. setSelectionValue and refresh. Ensure
   * correct messages in log
   **/
  public void Var092() {
    RQueuedMessage qmsg, qmsg2;
    long len1;
    int i;
    String[] job = startCLJob();
    if (job == null) {
      failed("CL job not submitted.");
      return;
    }
    try {
      RJobLog j = new RJobLog(systemObject_, job[2], job[1], job[0]);

      j.open();
      if (!j.isOpen()) {
        failed("Open failed.");
        return;
      }

      len1 = j.getListLength();
      qmsg = (RQueuedMessage) j.resourceAt(0);
      if (len1 > 0) {
        // Set the selection value to retrieve all of the messages newer than
        // the OLDEST messages in
        // the queue. This should return all of the messages
        // This is based on STARTING_MESSAGE_KEY which is
        j.setSelectionValue(RJobLog.STARTING_MESSAGE_KEY, RJobLog.OLDEST);
        j.setSelectionValue(RJobLog.LIST_DIRECTION, RJobLog.NEXT);
        j.refreshContents();
        qmsg2 = (RQueuedMessage) j.resourceAt(0);
        if (j.getListLength() == len1 && qmsg.equals(qmsg2))
          succeeded();
        else
          failed("Bad list length returned or object returned.");
      } else
        failed("job not found");
    } catch (Exception e) {
      failed(e, "Exception occurred.");
    }
  }

  /**
   * refreshStatus() -- Valid refresh with default constructor and no open.
   * Should try to open and throw an IllegalStateException
   **/
  public void Var093() {
    try {
      RJobLog f = new RJobLog();
      f.refreshStatus();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * refreshStatus() -- Valid refresh with default constructor and no open.
   * Should throw an IllegalStateException
   **/
  public void Var094() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.refreshStatus();
      succeeded(); // @A1C
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * refreshStatus() -- Valid. Do while loading large list
   **/
  public void Var095() {
    int i, x;
    long len1;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          len1 = f.getListLength();
          while (!f.isComplete()) {
            f.refreshStatus();
            if (f.getListLength() < len1) {
              failed("List length not updated after refreshStatus().");
              return;
            }
          }
          f.close();
        }
      }
      succeeded();
      jobList.close();

    } catch (Exception e) {
      failed(e, "Unknown exception.");

    }
  }

  /**
   * refreshStatus() -- Invalid refreshStatus(). Do after changing system
   **/
  public void Var096() {
    int i, x;
    boolean found;
    try {
      AS400 newSys_ = new AS400("RCHAS1DD", "JAVA", "JTEAM1");
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          for (i = 0; i < f.getListLength(); ++i) {
            f.setSystem(newSys_);
            f.refreshStatus();
            f.close();
          }
        }
      }
      jobList.close();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * resourceAt() -- Do prior to setting name. Should open implicitly
   **/

  public void Var097() {
    try {
      RJobLog f = new RJobLog();
      f.setSystem(systemObject_);
      f.resourceAt(0);
      succeeded(); // @A1C
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * resourceAt() -- Invalid resourceAt(). Pass in too large of an index. Ensure
   * an IllegalArgumentException is thrown
   **/
  public void Var098() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          f.waitForComplete();
          f.resourceAt(f.getListLength() + 200);
        }
      }
      jobList.close();
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }

  }

  /**
   * resourceAt() -- Invalid resourceAt(). Pass in -1 for an index. Ensure an
   * IllegalArgumentException is thrown
   **/
  public void Var099() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          f.resourceAt(-1);
        }
      }
      jobList.close();
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }

  }

  /**
   * resourceAt() -- Valid isResourceAvailable(). Check each resource to ensure
   * non null.
   **/
  public void Var100() {
    int i, x;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          for (i = 0; i < f.getListLength(); ++i) {
            RQueuedMessage message = (RQueuedMessage) f.resourceAt(i);
          }
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * resourceAt() -- Valid. Try to get resource that is not loaded yet.
   **/
  public void Var101() {
    int i, x;
    long len1;
    boolean found;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          f.open();
          len1 = f.getListLength();
          while (!f.isComplete()) {
            f.refreshStatus();
            f.resourceAt(f.getListLength() - 1);
          }
          f.close();
        }
      }
      succeeded();
      jobList.close();

    } catch (Exception e) {
      failed(e, "Unknown exception.");

    }
  }

  /**
   * resourceAt() -- Check each attribute of a queued message gotten from a job
   * log to ensure correct
   **/
  public void Var102() {
    int i, x;
    boolean found;
    Date curDate;
    Date msgDate;

    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      ProgramCall pc = new ProgramCall(pwrSys_);
      Job j = pc.getServerJob();
      RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
      f.open();
      long k;
      k = f.getListLength();
      for (i = 0; i < k && found == false; ++i) {
        RQueuedMessage message = (RQueuedMessage) f.resourceAt(i);
        if (message.getAttributeValue(RQueuedMessage.MESSAGE_ID).equals(id)) {
          curDate = new Date();
          msgDate = (Date) message.getAttributeValue(RQueuedMessage.DATE_SENT);
          if (msgDate.getMonth() != curDate.getMonth()
              || msgDate.getYear() != curDate.getYear()
              || msgDate.getDay() != curDate.getDay()
              || msgDate.getHours() != curDate.getHours()) {
            failed("Bad DATE_SENT value returned.");
            return;
          }
          if (!(((String) message
              .getAttributeValue(RQueuedMessage.DEFAULT_REPLY)).equals(""))) {
            failed("Bad DEFAULT_REPLY value returned.");
            return;
          }
          if (!(((String) message
              .getAttributeValue(RQueuedMessage.MESSAGE_FILE))
              .equals("/QSYS.LIB/QCPFMSG.MSGF"))) {
            failed("Bad MESSAGE_FILE value returned.");
            return;
          }
          if (((String) message.getAttributeValue(RQueuedMessage.MESSAGE_HELP))
              .toUpperCase().indexOf(
                  "BECAUSE THE FIELD TO RECEIVE THE NUMBER IS TOO SHORT") == -1) {
            failed("Bad MESSAGE_HELP value returned.");
            return;
          }
          Integer sevInt = (Integer) message
              .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY);
          if (sevInt.intValue() != 40) {
            failed("Bad MESSAGE_SEVERITY value returned.");
            return;
          }
          if (!(((String) message
              .getAttributeValue(RQueuedMessage.MESSAGE_TEXT)).toUpperCase()
              .equals("OVERFLOW OCCURRED. TARGET ITEM IS TOO SHORT."))) {
            failed("Bad MESSAGE_TEXT value returned.");
            return;
          }
          if (!(((String) message
              .getAttributeValue(RQueuedMessage.SENDER_JOB_NAME)).equals(""))) {
            failed("Bad SENDING_JOB_NAME value returned.");
            return;
          }
          if (!(((String) message
              .getAttributeValue(RQueuedMessage.SENDER_JOB_NUMBER)).equals(""))) {
            failed("Bad SENDING_JOB_NUMBER value returned.");
            return;
          }
          if (!(((String) message
              .getAttributeValue(RQueuedMessage.SENDER_USER_NAME)).equals(""))) {
            failed("Bad SENDING_USER_NAME value returned.");
            return;
          }
          if (!(((String) message
              .getAttributeValue(RQueuedMessage.SENDING_PROGRAM_NAME))
              .equals("QZRCSRVS"))) {
            failed("Bad SENDING_PROGRAM_NAME value returned.");
            return;
          }
          if (!(((String) message
              .getAttributeValue(RQueuedMessage.REPLY_STATUS))
              .equals(RQueuedMessage.REPLY_STATUS_NOT_ACCEPT))) {
            failed("Bad REPLY_STATUS value returned.");
            return;
          }
          Integer typeInt = (Integer) message
              .getAttributeValue(RQueuedMessage.MESSAGE_TYPE);
          if (typeInt.intValue() != QueuedMessage.COMPLETION) {
            failed("Bad MESSAGE_TYPE value returned.");
            return;
          }
          if (message.getAttributeValue(RQueuedMessage.MESSAGE_KEY) == null) {
            failed("Bad MESSAGE_KEY value returned.");
            return;
          }
          found = true;
        }
      }
      f.close();
      if (found)
        succeeded();
      else
        failed("Message ID not found.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");

    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test invalid use; pass null for
   * attribute ID.
   **/
  public void Var103() {
    Object SelectionValue;
    try {
      RJobLog f = new RJobLog(systemObject_);
      f.setSelectionValue(null, RJobLog.PREVIOUS);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test invalid use; pass bad value
   * for attribute ID.
   **/
  public void Var104() {
    Object SelectionValue;
    try {
      RJobLog f = new RJobLog(systemObject_);
      f.setSelectionValue(RJobLog.PREVIOUS, RJobLog.PREVIOUS);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test invalid use; pass bad value
   * for value.
   **/
  public void Var105() {
    Object SelectionValue;
    try {
      RJobLog f = new RJobLog(systemObject_);
      f.setSelectionValue(RJobLog.LIST_DIRECTION, RJobLog.STARTING_MESSAGE_KEY);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * setSelectionValue() -- setSelectionValue and then reset by passing null in
   * for value. Ensure the selection id is reset
   **/
  public void Var106() {
    int i, x;
    RQueuedMessage qmsg1, qmsg2;
    boolean found;
    long len1, len2;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          len1 = f.getListLength();
          qmsg1 = (RQueuedMessage) f.resourceAt(0);
          // Set the selection value to retrieve all of the messages older than
          // the OLDEST messages in
          // the queue. This should return just the first message.
          // This is based on STARTING_MESSAGE_KEY which is
          // set to OLDEST as the default.
          f.setSelectionValue(RJobLog.LIST_DIRECTION, RJobLog.PREVIOUS);
          f.refreshContents();
          qmsg2 = (RQueuedMessage) f.resourceAt(0);
          if (f.getListLength() != 1 || !qmsg1.equals(qmsg2)) {
            failed("Bad list length or object returned.");
            return;
          }
          f.setSelectionValue(RJobLog.LIST_DIRECTION, null); // should reset
                                                             // value to NEXT
          f.refreshContents();
          qmsg2 = (RQueuedMessage) f.resourceAt(0);
          if (f.getListLength() != len1 || !qmsg1.equals(qmsg2)) {
            failed("Bad list length or object returned.");
            return;
          }

          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * setSelectionValue() -- setSelectionValue and then reset by passing in a
   * specific key value. Ensure the selection id is reset
   **/
  public void Var107() {
    int i, x;
    RQueuedMessage qmsg0, qmsg1, qmsg2;
    boolean found;
    long len1, len2;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      long y;
      y = jobList.getListLength();
      for (x = 0; x < y; ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          len1 = f.getListLength();
          if (len1 > 1) {
            qmsg0 = (RQueuedMessage) f.resourceAt(0);
            qmsg1 = (RQueuedMessage) f.resourceAt(len1 - 2);
            f.setSelectionValue(RJobLog.STARTING_MESSAGE_KEY,
                qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY));
            f.refreshContents();
            System.out.println("length after setSelectionValue = "
                + f.getListLength());
            qmsg2 = (RQueuedMessage) f.resourceAt(0);
            if (f.getListLength() < 2 || !qmsg1.equals(qmsg2)) {
              failed("Bad list length or object returned.");
              return;
            }
            f.setSelectionValue(RJobLog.STARTING_MESSAGE_KEY, null); // should
                                                                     // reset
                                                                     // value to
                                                                     // NEXT
            f.refreshContents();
            qmsg2 = (RQueuedMessage) f.resourceAt(0);
            System.out.println("length = " + f.getListLength());
            if (!qmsg0.equals(qmsg2)) {
              failed("Message objects unequal after reset.");
              return;
            }
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * setSelectionValue() -- setSelectionValue and then reset by passing in a
   * specific key value. Ensure the selection id is reset
   **/
  public void Var108() {
    int i, x;
    RQueuedMessage qmsg1, qmsg2;
    boolean found;
    long len1, len2;
    try {
      found = false;
      if (pwrSys_ == null) {
        failed("Power user on -misc parameter not specified.");
        return;
      }
      String id = "CAE0009";
      RJobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
      RJobList jobList = new RJobList(pwrSys_);
      jobList.setSelectionValue(RJobList.JOB_NAME, "QZRCSRVS");
      jobList.open();
      jobList.waitForComplete();
      for (x = 0; x < jobList.getListLength(); ++x) {
        RJob j = (RJob) jobList.resourceAt(x);
        if (((String) j.getAttributeValue(RJob.COMPLETION_STATUS)).length() == 0) {
          RJobLog f = new RJobLog(pwrSys_, j.getName(), j.getUser(),
              j.getNumber());
          len1 = f.getListLength();
          if (len1 > 1) {
            qmsg1 = (RQueuedMessage) f.resourceAt(len1 - 2);
            f.setSelectionValue(RJobLog.STARTING_MESSAGE_KEY,
                qmsg1.getAttributeValue(RQueuedMessage.MESSAGE_KEY));
            f.setSelectionValue(RJobLog.LIST_DIRECTION, RJobLog.PREVIOUS);
            f.refreshContents();
            qmsg2 = (RQueuedMessage) f.resourceAt(0);
            if (f.getListLength() != len1 - 1 || !qmsg1.equals(qmsg2)) {
              failed("Bad list length or object returned.");
              return;
            }
            f.setSelectionValue(RJobLog.STARTING_MESSAGE_KEY, null); // should
                                                                     // reset
                                                                     // value to
                                                                     // OLDEST
            f.setSelectionValue(RJobLog.LIST_DIRECTION, null); // set back to
                                                               // next
            f.refreshContents();
            if (f.getListLength() != len1) {
              failed("Bad list length or object returned.");
              return;
            }
          }
          f.close();
        }
      }
      jobList.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * toString() --
   **/
  public void Var109() {
    int i;
    String toStr;

    try {
      RJobLog f = new RJobLog(systemObject_);
      toStr = f.toString();
      if (toStr.equals("//*"))
        succeeded();
      else
        failed("Bad toString returned.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

}
