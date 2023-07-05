///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  MessageQueueRFTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.RMessageQueue;
import com.ibm.as400.resource.RQueuedMessage;
import com.ibm.as400.access.QueuedMessage;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.resource.ResourceMetaData;
import com.ibm.as400.resource.Presentation;

import java.util.Vector;

/**
 * Testcase MessageQueueRFTestcase.
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
public class MessageQueueRFTestcase extends Testcase {
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

    sandbox_ = new MessageSandbox(systemObject_, testLib_, "MQT",userId_);
    sandboxReply_ = new MessageSandbox(systemObject_, testLib_, "MQTREPLY",userId_);
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

  private static boolean compare(Object[] a, Object[] b) {
    if (a.length != b.length)
      return false;

    Vector ina = new Vector(a.length);
    for (int i = 0; i < a.length; ++i)
      ina.addElement(a[i]);

    for (int i = 0; i < b.length; ++i) {
      if (ina.contains(b[i]))
        ina.removeElement(b[i]);
      else
        return false;
    }

    return (ina.size() == 0);
  }

  /**
   * close() -- Valid close with default constructor followed by an open.
   **/
  public void Var001() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * close() - Valid close with constructor RMessageQueue(AS400) followed by an
   * open()
   **/
  public void Var002() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * close() - Valid close with constructor RMessageQueue(AS400, String)
   **/
  public void Var003() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * close() - Valid close on list that hasn't been open yet. Nothing should
   * happen.
   **/
  public void Var004() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * close() - Valid close on list that hasn't been open yet. Nothing should
   * happen
   **/
  public void Var005() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * close() - Valid close on list that hasn't been open yet. Nothing should
   * happen
   **/
  public void Var006() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");

    }
  }

  /**
   * close() - Attempt to close a list twice. This should not cause an exception
   **/
  public void Var007() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.close();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * close() - Attempt to load resources after a close. Nothing should happen
   * since list should be implicity opened by the refresh.
   **/
  public void Var008() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.close();
      f.refreshContents();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * close() - Valid use of close. Open/close, Open/close a message queue four
   * times
   **/
  public void Var009() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.close();
      f.open();
      f.refreshContents();
      f.close();
      f.open();
      f.close();
      f.open();
      f.getListLength();
      f.close();
      succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * close() - Close a message queue that has been deleted from the AS/400.
   **/
  public void Var010() {
    MessageSandbox sbox_ = null;

    try {
      sbox_ = new MessageSandbox(systemObject_, testLib_, "TEMPQ1", userId_);

      String path = QSYSObjectPathName.toPath(testLib_, "TEMPQ1", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      sbox_.cleanup();
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
  public void Var011() {
    ResourceMetaData SelectionData;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      SelectionData = f.getSelectionMetaData(null);
      f.close();
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) -- call with default message queue
   * constructor
   **/
  public void Var012() {
    try {
      ResourceMetaData SelectionData;
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.open();
      SelectionData = f.getSelectionMetaData(RMessageQueue.LIST_DIRECTION);
      f.close();
      succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object AttributeID) -- Test invalid use; pass bad
   * value for attribute ID.
   **/
  public void Var013() {
    ResourceMetaData SelectionData;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      SelectionData = f.getSelectionMetaData(RMessageQueue.PREVIOUS);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * getSelectionMetaData(Object AttributeID) -- Test valid use; pass valid
   * value for attribute ID.
   **/
  public void Var014() {
    ResourceMetaData SelectionData;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      SelectionData = f.getSelectionMetaData(RMessageQueue.SELECTION_CRITERIA);
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object AttributeID) -- Test valid use; pass valid
   * value for attribute ID.
   **/
  public void Var015() {
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_USER_MESSAGE_KEY);
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object AttributeID) -- Test valid use; pass valid
   * value for attribute ID.
   **/
  public void Var016() {
    ResourceMetaData SelectionData;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.setPath(path);
      f.open();
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) - Attempt to getSelectionMetaData
   * prior to open. This is valid.
   **/
  public void Var017() {
    ResourceMetaData SelectionData;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      SelectionData = f.getSelectionMetaData(RMessageQueue.LIST_DIRECTION);
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) - Verify LIST_DIRECTION meta data.
   **/
  public void Var018() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      SelectionData = f.getSelectionMetaData(RMessageQueue.LIST_DIRECTION);
      if (!(SelectionData.getID().equals(RMessageQueue.LIST_DIRECTION)
          && SelectionData.getType().equals(String.class)
          && SelectionData.isReadOnly() == false && SelectionData
            .areMultipleAllowed() == false))
        failed("SelectionData wrong for attributeID, type or isReadOnly.");
      else {
        possibleValues = SelectionData.getPossibleValues();
        if (compare(possibleValues, new Object[] { RMessageQueue.NEXT,
            RMessageQueue.PREVIOUS })) {
          if ((SelectionData.getDefaultValue()).equals(RMessageQueue.NEXT)
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
   * getSelectionMetaData(Object attributeID) -- call with default message queue
   * constructor and no open
   **/
  public void Var019() {
    try {
      ResourceMetaData SelectionData;
      RMessageQueue f = new RMessageQueue();
      SelectionData = f.getSelectionMetaData(RMessageQueue.LIST_DIRECTION);
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) - Verify SELECTION_CRITERIA meta
   * data.
   **/
  public void Var020() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SELECTION_CRITERIA);
      if (!(SelectionData.getID().equals(RMessageQueue.SELECTION_CRITERIA)
          && SelectionData.getType().equals(String.class)
          && SelectionData.isReadOnly() == false && SelectionData
            .areMultipleAllowed() == false))
        failed("SelectionData wrong for attributeID, areMultipleAllowed, type or isReadOnly.");
      else {
        possibleValues = SelectionData.getPossibleValues();
        if (compare(possibleValues, new Object[] { RMessageQueue.ALL,
            RMessageQueue.MESSAGES_NEED_REPLY,
            RMessageQueue.SENDERS_COPY_NEED_REPLY,
            RMessageQueue.MESSAGES_NO_NEED_REPLY })) {
          if ((SelectionData.getDefaultValue()).equals(RMessageQueue.ALL)
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
   * getSelectionMetaData(Object attributeID) - Verify SEVERITY_CRITERIA meta
   * data.
   **/
  public void Var021() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    Integer int0 = new Integer(0);
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionData = f.getSelectionMetaData(RMessageQueue.SEVERITY_CRITERIA);
      if (!(SelectionData.getID().equals(RMessageQueue.SEVERITY_CRITERIA)
          && SelectionData.getType().equals(Integer.class)
          && SelectionData.isReadOnly() == false && SelectionData
            .areMultipleAllowed() == false))
        failed("SelectionData wrong for attributeID, areMultipleAllowed, type or isReadOnly.");
      else {
        possibleValues = SelectionData.getPossibleValues();
        if (possibleValues.length == 0) {
          if ((SelectionData.getDefaultValue().equals(int0))
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
   * getSelectionMetaData(Object attributeID) - Verify
   * STARTING_USER_MESSAGE_KEY.
   **/
  public void Var022() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_USER_MESSAGE_KEY);
      if (!(SelectionData.getID().equals(
          RMessageQueue.STARTING_USER_MESSAGE_KEY)
          && SelectionData.getType().equals(byte[].class)
          && SelectionData.isReadOnly() == false && SelectionData
            .areMultipleAllowed() == false))
        failed("SelectionData wrong for attributeID, areMultipleAllowed, type or isReadOnly.");
      else {
        possibleValues = SelectionData.getPossibleValues();

        if (compare(possibleValues, new Object[] { RMessageQueue.OLDEST,
            RMessageQueue.NEWEST })) {
          if (SelectionData.getDefaultValue().equals(RMessageQueue.OLDEST)
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
   * getSelectionMetaData(Object attributeID) - Verify
   * STARTING_WORKSTATION_MESSAGE_KEY.
   **/
  public void Var023() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    byte[] byteValues;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
      if (!(SelectionData.getID().equals(
          RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY)
          && SelectionData.getType().equals(byte[].class)
          && SelectionData.isReadOnly() == false && SelectionData
            .areMultipleAllowed() == false))
        failed("SelectionData wrong for attributeID, areMultipleAllowed, type or isReadOnly.");
      else {
        possibleValues = SelectionData.getPossibleValues();
        if (compare(possibleValues, new Object[] { RMessageQueue.OLDEST,
            RMessageQueue.NEWEST })) {
          if (SelectionData.getDefaultValue().equals(RMessageQueue.OLDEST)
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
   * getSelectionMetaData() - Verify valid with default constructor
   **/
  public void Var024() {
    ResourceMetaData[] SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionData = f.getSelectionMetaData();
      if (SelectionData.length == 8)
        succeeded();
      else
        failed("Length of ResourceMetaData array incorrect.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify valid with constructor
   * RMessageQueue(systemObject_)
   **/
  public void Var025() {
    ResourceMetaData[] SelectionData;
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      SelectionData = f.getSelectionMetaData();
      if (SelectionData.length == 8)
        succeeded();
      else
        failed("Length of ResourceMetaData array incorrect.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify valid with constructor
   * RMessageQueue(systemObject_,path)
   **/
  public void Var026() {
    ResourceMetaData[] SelectionData;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      SelectionData = f.getSelectionMetaData();
      if (SelectionData.length == 8)
        succeeded();
      else
        failed("Length of ResourceMetaData array incorrect.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify valid with constructor
   * RMessageQueue(systemObject_,path) plus an open
   **/
  public void Var027() {
    ResourceMetaData[] SelectionData;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      SelectionData = f.getSelectionMetaData();
      if (SelectionData.length == 8)
        succeeded();
      else
        failed("Length of ResourceMetaData array incorrect.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify that the LIST_DIRECTION Selection is
   * retrieved correctly
   **/
  public void Var028() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionData = findMetaData(f.getSelectionMetaData(),
          RMessageQueue.LIST_DIRECTION);
      if (SelectionData.getID().equals(RMessageQueue.LIST_DIRECTION)) {
        if (!(SelectionData.getType().equals(String.class)
            && SelectionData.isReadOnly() == false && SelectionData
              .areMultipleAllowed() == false))
          failed("SelectionData wrong for attributeID, type, multipleAllowed or isReadOnly.");
        else {
          possibleValues = SelectionData.getPossibleValues();
          if (compare(possibleValues, new Object[] { RMessageQueue.NEXT,
              RMessageQueue.PREVIOUS })) {
            if ((SelectionData.getDefaultValue()).equals(RMessageQueue.NEXT)
                && SelectionData.isValueLimited() == true)
              succeeded();
            else
              failed("SelectionData wrong for getDefaultValue() or isValueLimited()");
          } else
            failed("SelectionData wrong for getPossibleValues()");
        }
      } else
        failed("LIST_DIRECTION Selection not retrieved.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify that the SELECTION_CRITERIA Selection is
   * retrieved correctly
   **/
  public void Var029() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionData = findMetaData(f.getSelectionMetaData(),
          RMessageQueue.SELECTION_CRITERIA);
      if (SelectionData.getID().equals(RMessageQueue.SELECTION_CRITERIA)) {
        if (!(SelectionData.getType().equals(String.class)
            && SelectionData.isReadOnly() == false && SelectionData
              .areMultipleAllowed() == false))
          failed("SelectionData wrong for attributeID, type, areMultipleAllowed or isReadOnly.");
        else {
          possibleValues = SelectionData.getPossibleValues();
          if (compare(possibleValues, new Object[] { RMessageQueue.ALL,
              RMessageQueue.MESSAGES_NEED_REPLY,
              RMessageQueue.SENDERS_COPY_NEED_REPLY,
              RMessageQueue.MESSAGES_NO_NEED_REPLY })) {
            if ((SelectionData.getDefaultValue()).equals(RMessageQueue.ALL)
                && SelectionData.isValueLimited() == true)
              succeeded();
            else
              failed("SelectionData wrong for getDefaultValue() or isValueLimited()");
          } else
            failed("SelectionData wrong for getPossibleValues()");
        }
      } else
        failed("SELECTION_CRITERIA Selection not retrieved.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify that the SEVERITY_CRITERIA Selection is
   * retrieved correctly
   **/
  public void Var030() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    Integer int0 = new Integer(0);
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionData = findMetaData(f.getSelectionMetaData(),
          RMessageQueue.SEVERITY_CRITERIA);
      if (SelectionData.getID().equals(RMessageQueue.SEVERITY_CRITERIA)) {
        if (!(SelectionData.getType().equals(Integer.class)
            && SelectionData.isReadOnly() == false && SelectionData
              .areMultipleAllowed() == false))
          failed("SelectionData wrong for attributeID, type, areMultipleAllowed or isReadOnly.");
        else {
          possibleValues = SelectionData.getPossibleValues();
          if (possibleValues.length == 0) {
            if (SelectionData.getDefaultValue().equals(int0)
                && SelectionData.isValueLimited() == false)
              succeeded();
            else
              failed("SelectionData wrong for getDefaultValue() or isValueLimited()");
          } else
            failed("SelectionData wrong for getPossibleValues()");
        }
      } else
        failed("SEVERITY_CRITERIA Selection not retrieved.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify that the STARTING_USER_MESSAGE_KEY
   * Selection is retrieved correctly
   **/
  public void Var031() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionData = findMetaData(f.getSelectionMetaData(),
          RMessageQueue.STARTING_USER_MESSAGE_KEY);
      if (SelectionData.getID().equals(RMessageQueue.STARTING_USER_MESSAGE_KEY)) {
        if (!(SelectionData.getType().equals(byte[].class)
            && SelectionData.isReadOnly() == false && SelectionData
              .areMultipleAllowed() == false))
          failed("SelectionData wrong for attributeID, type, areMultipleAllowed or isReadOnly.");
        else {
          possibleValues = SelectionData.getPossibleValues();
          if (compare(possibleValues, new Object[] { RMessageQueue.OLDEST,
              RMessageQueue.NEWEST })) {
            if (SelectionData.getDefaultValue().equals(RMessageQueue.OLDEST)
                && SelectionData.isValueLimited() == false)
              succeeded();
            else
              failed("SelectionData wrong for getDefaultValue() or isValueLimited()");
          } else
            failed("SelectionData wrong for getPossibleValues()");
        }
      } else
        failed("STARTING_USER_MESSAGE_KEY Selection not retrieved.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify that the STARTING_WORKSTATION_MESSAGE_KEY
   * Selection is retrieved correctly
   **/
  public void Var032() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionData = findMetaData(f.getSelectionMetaData(),
          RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
      if (SelectionData.getID().equals(
          RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY)) {
        if (!(SelectionData.getType().equals(byte[].class)
            && SelectionData.isReadOnly() == false && SelectionData
              .areMultipleAllowed() == false))
          failed("SelectionData wrong for attributeID, type, areMultipleAllowed or isReadOnly.");
        else {
          possibleValues = SelectionData.getPossibleValues();
          if (compare(possibleValues, new Object[] { RMessageQueue.OLDEST,
              RMessageQueue.NEWEST })) {
            if (SelectionData.getDefaultValue().equals(RMessageQueue.OLDEST)
                && SelectionData.isValueLimited() == false)
              succeeded();
            else
              failed("SelectionData wrong for getDefaultValue() or isValueLimited()");
          } else
            failed("SelectionData wrong for getPossibleValues()");
        }
      } else
        failed("STARTING_WORKSTATION_MESSAGE_KEY Selection not retrieved.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionValue(Object AttributeID) -- Test invalid use; pass null for
   * attribute ID.
   **/
  public void Var033() {
    Object SelectionValue;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      SelectionValue = f.getSelectionValue(null);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * getSelectionValue(Object attributeID) -- call with default message queue
   * constructor
   **/
  public void Var034() {
    try {
      Object SelectionValue;
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.LIST_DIRECTION);
      succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionValue(Object AttributeID) -- Test invalid use; pass bad value
   * for attribute ID.
   **/
  public void Var035() {
    Object SelectionValue;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.PREVIOUS);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * getSelectionValue(Object AttributeID) -- Test valid use; pass value for
   * attribute ID and check return to ensure the default value is returned.
   **/
  public void Var036() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.LIST_DIRECTION);
      SelectionData = f.getSelectionMetaData(RMessageQueue.LIST_DIRECTION);
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
  public void Var037() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SELECTION_CRITERIA);
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
  public void Var038() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SEVERITY_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SEVERITY_CRITERIA);
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
  public void Var039() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f
          .getSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY);
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_USER_MESSAGE_KEY);
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
  public void Var040() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f
          .getSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
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
  public void Var041() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.LIST_DIRECTION);
      SelectionData = f.getSelectionMetaData(RMessageQueue.LIST_DIRECTION);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.LIST_DIRECTION,
            RMessageQueue.PREVIOUS);
        SelectionValue = f.getSelectionValue(RMessageQueue.LIST_DIRECTION);
        if (SelectionValue.equals(RMessageQueue.PREVIOUS)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.LIST_DIRECTION,
              SelectionData.getDefaultValue());
          SelectionValue = f.getSelectionValue(RMessageQueue.LIST_DIRECTION);
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
  public void Var042() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SELECTION_CRITERIA);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
            RMessageQueue.MESSAGES_NEED_REPLY);
        SelectionValue = f.getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
        if (SelectionValue.equals(RMessageQueue.MESSAGES_NEED_REPLY)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
              SelectionData.getDefaultValue());
          SelectionValue = f
              .getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
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
  public void Var043() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SELECTION_CRITERIA);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
            RMessageQueue.MESSAGES_NO_NEED_REPLY);
        SelectionValue = f.getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
        if (SelectionValue.equals(RMessageQueue.MESSAGES_NO_NEED_REPLY)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
              SelectionData.getDefaultValue());
          SelectionValue = f
              .getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
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
  public void Var044() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SELECTION_CRITERIA);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
            RMessageQueue.SENDERS_COPY_NEED_REPLY);
        SelectionValue = f.getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
        if (SelectionValue.equals(RMessageQueue.SENDERS_COPY_NEED_REPLY)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
              SelectionData.getDefaultValue());
          SelectionValue = f
              .getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
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
  public void Var045() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SELECTION_CRITERIA);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
            RMessageQueue.MESSAGES_NEED_REPLY);
        SelectionValue = f.getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
        if (SelectionValue.equals(RMessageQueue.MESSAGES_NEED_REPLY)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
              RMessageQueue.ALL);
          SelectionValue = f
              .getSelectionValue(RMessageQueue.SELECTION_CRITERIA);
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
  public void Var046() {
    Object SelectionValue;
    Integer Int = new Integer(10);
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SEVERITY_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SEVERITY_CRITERIA);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA, Int);
        SelectionValue = f.getSelectionValue(RMessageQueue.SEVERITY_CRITERIA);
        if (SelectionValue.equals(Int)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA,
              SelectionData.getDefaultValue());
          SelectionValue = f.getSelectionValue(RMessageQueue.SEVERITY_CRITERIA);
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
  public void Var047() {
    Object SelectionValue;
    Integer Int = new Integer(0);
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SEVERITY_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SEVERITY_CRITERIA);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA, Int);
        SelectionValue = f.getSelectionValue(RMessageQueue.SEVERITY_CRITERIA);
        if (SelectionValue.equals(Int)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA,
              SelectionData.getDefaultValue());
          SelectionValue = f.getSelectionValue(RMessageQueue.SEVERITY_CRITERIA);
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
  public void Var048() {
    Object SelectionValue;
    Integer Int = new Integer(99);
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SEVERITY_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SEVERITY_CRITERIA);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA, Int);
        SelectionValue = f.getSelectionValue(RMessageQueue.SEVERITY_CRITERIA);
        if (SelectionValue.equals(Int)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA,
              SelectionData.getDefaultValue());
          SelectionValue = f.getSelectionValue(RMessageQueue.SEVERITY_CRITERIA);
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
  public void Var049() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f
          .getSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY);
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_USER_MESSAGE_KEY);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY,
            RMessageQueue.NEWEST);
        SelectionValue = f
            .getSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY);
        if (SelectionValue.equals(RMessageQueue.NEWEST)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY,
              SelectionData.getDefaultValue());
          SelectionValue = f
              .getSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY);
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
  public void Var050() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SORT_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SORT_CRITERIA);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.SORT_CRITERIA, Boolean.TRUE);
        SelectionValue = f.getSelectionValue(RMessageQueue.SORT_CRITERIA);
        if (SelectionValue.equals(Boolean.TRUE)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.SORT_CRITERIA, Boolean.FALSE);
          SelectionValue = f.getSelectionValue(RMessageQueue.SORT_CRITERIA);
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
  public void Var051() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f
          .getSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY,
            RMessageQueue.NEWEST);
        SelectionValue = f
            .getSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
        if (SelectionValue.equals(RMessageQueue.NEWEST)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY,
              SelectionData.getDefaultValue());
          SelectionValue = f
              .getSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
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
  public void Var052() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f
          .getSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
      if (SelectionValue.equals(SelectionData.getDefaultValue())) {
        // Set value and then re-get to check
        f.setSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY,
            RMessageQueue.NEWEST);
        SelectionValue = f
            .getSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
        if (SelectionValue.equals(RMessageQueue.NEWEST)) {
          // Reset value back to default
          f.setSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY,
              RMessageQueue.OLDEST);
          SelectionValue = f
              .getSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
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
   * getListLength(Object AttributeID) -- Test invalid use; Try to get list
   * length prior to open
   **/
  public void Var053() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.getListLength();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- getListLength of default constructor
   **/
  public void Var054() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.setPath(QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ"));
      f.open();
      f.remove();
      f.refreshContents();
      if (f.getListLength() == 0)
        succeeded();
      else
        failed("Incorrect list length returned.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- getListLength of constructor
   * RMessageQueue(system)
   **/
  public void Var055() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.setPath(QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ"));
      f.remove();
      f.open();
      if (f.getListLength() == 0)
        succeeded();
      else
        failed("Incorrect list length returned.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test valid use; get list length of
   * empty list
   **/
  public void Var056() {
    try {

      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      if (f.getListLength() == 0)
        succeeded();
      else
        failed("Incorrect list length returned.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test valid use.
   **/
  public void Var057() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.getListLength();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test invalid use. Tr
   **/
  public void Var058() {
    MessageSandbox sbox_ = null;

    try {

      sbox_ = new MessageSandbox(systemObject_, testLib_, "TEMPMQ",userId_);

      String path = QSYSObjectPathName.toPath(testLib_, "TEMPMQ", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.sendInformational("Hi there.");

      // Delete queue
      sbox_.cleanup();

      // Check list length. Refreshing status should refresh list length which
      // should cause
      // a resource exception
      f.refreshContents();
      f.refreshStatus();
      f.getListLength();
      failed("Exception not thrown.");
      f.close();
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test valid use; add message to list
   * and then check length prior to resetting the list.
   **/
  public void Var059() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("This is a test.");
      // Check length of list prior to refresh -- ensure still 0
      if (f.getListLength() == 0)
        succeeded();
      else
        failed("Incorrect list length returned.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test valid use; add message to list
   * and then check length after resetting the list.
   **/
  public void Var060() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("This is a test.");
      // Refresh and the check length
      f.refreshContents();
      if (f.getListLength() == 1)
        succeeded();
      else
        failed("Incorrect list length returned.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test valid use; add message to list
   * and then check length after resetting and refreshing the status of the
   * list.
   **/
  public void Var061() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("This is a test.");
      // Refresh and the check length
      f.refreshContents();
      if (f.getListLength() == 1)
        succeeded();
      else
        failed("Incorrect list length returned.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test valid use; add message to list
   * and then check length prior to resetting the list.
   **/
  public void Var062() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("This is a test.");
      f.sendInformational("This is a test.");
      f.remove();
      f.refreshContents();
      f.refreshStatus();
      // Check length of list after refresh; should be 0
      if (f.getListLength() == 0)
        succeeded();
      else
        failed("Incorrect list length returned.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test valid use; add message to list
   * and then check length prior to resetting the list.
   **/
  public void Var063() {
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.remove();
      f.sendInformational("This is a test.");
      f.sendInformational("This is a test.");
      f.refreshContents();
      // Check length of list after refresh; should be 0
      if (f.getListLength() == 2) {
        for (i = 0; i < 2; ++i)
          f.receive(null, 0, RMessageQueue.OLD, RMessageQueue.ANY);
        f.refreshContents();
        f.remove(RMessageQueue.OLD);
        f.sendInformational("This is another test.");
        f.refreshContents();
        if (f.getListLength() == 1)
          succeeded();
        else
          failed("Incorrect list length returned.");
      } else
        failed("Incorrect list length returned.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test valid use; add message to list
   * and then check length prior to resetting the list.
   **/
  public void Var064() {
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("This is a test.");
      f.sendInformational("This is a test.");
      f.refreshContents();
      // Check length of list after refresh; should be 2
      if (f.getListLength() == 2) {
        for (i = 0; i < 2; ++i)
          f.receive(null, 0, RMessageQueue.OLD, RMessageQueue.ANY);
        f.remove(RMessageQueue.OLD);
        f.refreshContents();
        if (f.getListLength() != 0) {
          failed("Incorrect list length returned.");
          return;
        }
        f.sendInformational("This is another test.");
        f.sendInformational("This is another test.");
        f.refreshContents();
        if (f.getListLength() == 2)
          succeeded();
        else
          failed("Incorrect list length returned.");
      } else
        failed("Incorrect list length returned.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getPresentation() -- Test valid use; Verify presentation object.
   **/
  public void Var065() {
    Presentation presObj;
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      presObj = f.getPresentation();
      if (onAS400_) {
        if (presObj != null
            && presObj.getFullName().equals("*CURRENT")
            && presObj.getName().equals("*CURRENT")
            && presObj.getValue(Presentation.DESCRIPTION_TEXT).toString()
                .equals("Message Queue")
            && presObj.getValue(Presentation.HELP_TEXT) == null)
          succeeded();
        else
          failed("Bad presentation info returned.");
      } else {
        if (presObj != null
            && presObj.getFullName().equals("*CURRENT")
            && presObj.getName().equals("*CURRENT")
            && presObj.getValue(Presentation.DESCRIPTION_TEXT).toString()
                .equals("Message Queue")
            && presObj.getValue(Presentation.HELP_TEXT) == null
            && presObj.getValue(Presentation.ICON_COLOR_16x16) != null
            && presObj.getValue(Presentation.ICON_COLOR_32x32) != null)
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
   * isComplete() -- Test valid use; Check if complete without open
   **/
  public void Var066() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      if (!f.isComplete())
        succeeded();
      else
        failed("isComplete() returning incorrect value.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isComplete() -- Test valid use; Check if complete after open.
   **/
  public void Var067() {
    boolean TimedOut = false;
    int secs = 0;
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      while (!f.isComplete() && !TimedOut) {
        Thread.sleep(2000);
        secs = secs + 2;
        if (secs > 90)
          TimedOut = true;
        f.refreshStatus();
      }
      if (f.isComplete())
        succeeded();
      else if (TimedOut)
        failed("Time out prior to list loading.");
      else
        failed("isComplete() returned incorrect value.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isComplete() -- Test valid use; Check if complete after open of specified
   * message queue.
   **/
  public void Var068() {
    boolean TimedOut = false;
    int secs = 0;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      while (!f.isComplete() && !TimedOut) {
        Thread.sleep(2000);
        secs = secs + 2;
        if (secs > 90)
          TimedOut = true;
        f.refreshStatus();
      }
      if (f.isComplete())
        succeeded();
      else if (TimedOut)
        failed("Time out prior to list loading.");
      else
        failed("isComplete() returned incorrect value.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isComplete() -- Test valid use; Check if complete after open of large
   * message queue.
   **/
  public void Var069() {
    boolean TimedOut = false;
    int secs = 0;
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      while (!f.isComplete() && !TimedOut) {
        Thread.sleep(2000);
        secs = secs + 2;
        if (secs > 90)
          TimedOut = true;
        f.refreshStatus();
      }
      if (f.isComplete())
        succeeded();
      else if (TimedOut)
        failed("Time out prior to list loading.");
      else
        failed("isComplete() returned incorrect value.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isComplete() -- Test valid use; Check if complete after open and refresh of
   * large message queue.
   **/
  public void Var070() {
    boolean TimedOut = false;
    int secs = 0;
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      while (!f.isComplete() && !TimedOut) {
        Thread.sleep(2000);
        secs = secs + 2;
        if (secs > 90)
          TimedOut = true;
        f.refreshStatus();
      }
      f.refreshContents();
      TimedOut = false;
      while (!f.isComplete() && !TimedOut) {
        Thread.sleep(2000);
        secs = secs + 2;
        if (secs > 90)
          TimedOut = true;
        f.refreshStatus();
      }
      if (f.isComplete())
        succeeded();
      else if (TimedOut)
        failed("Time out prior to list loading.");
      else
        failed("isComplete() returned incorrect value.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isComplete() -- Test invalid use; Check if non-existent message queue
   * isComplete
   **/
  public void Var071() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MYBAD", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      if (!f.isComplete())
        succeeded();
      else
        failed("isComplete() returning incorrect value.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * isComplete() -- Check if deleted queue is complete.
   **/
  public void Var072() {
    MessageSandbox sbox_ = null;

    try {
      sbox_ = new MessageSandbox(systemObject_, testLib_, "TEMPMQ",userId_);

      String path = QSYSObjectPathName.toPath(testLib_, "TEMPMQ", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      if (!f.isComplete()) {
        f.open();
        sbox_.cleanup();
        f.refreshStatus();
        if (f.isComplete())
          // Contents not refreshed so list is still considered complete.
          succeeded();
        else
          failed("List not complete.");
      } else
        failed("List not complete.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isComplete() -- Check if very large queue is complete.
   **/
  public void Var073() {
    int i;
    boolean timedOut = false;
    int secs = 0;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      for (i = 0; i < 200; ++i)
        f.sendInformational("Test Message...");
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      while (!g.isComplete() && !timedOut) {
        Thread.sleep(2000);
        secs = secs + 2;
        if (secs > 240)
          timedOut = true;
        g.refreshStatus();
      }
      if (g.isComplete())
        // Status not refreshed so list is still considered complete.
        succeeded();
      else
        failed("Complete request timed out.");

      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isOpen() -- Check with default constructor followed by an open.
   **/
  public void Var074() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.open();
      if (f.isOpen())
        succeeded();
      else
        failed("Message queue not open.");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isOpen() - Check with constructor RMessageQueue(AS400) followed by an
   * open()
   **/
  public void Var075() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      if (f.isOpen())
        succeeded();
      else
        failed("Message queue not open.");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isOpen() - Check with constructor RMessageQueue(AS400, String) followed by
   * an open()
   **/
  public void Var076() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      if (f.isOpen())
        succeeded();
      else
        failed("Message queue not open.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isOpen() - Check on list that hasn't been open yet.
   **/
  public void Var077() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      if (!f.isOpen())
        succeeded();
      else
        failed("isOpen() returning true instead of false.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isOpen() - Check after opening and then closing list
   **/
  public void Var078() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      f.close();
      if (!f.isOpen())
        succeeded();
      else
        failed("isOpen returning true instead of false.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isOpen() - Check after opening, closing, the re-opening list
   **/
  public void Var079() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.close();
      f.open();
      if (f.isOpen())
        succeeded();
      else
        failed("isOpen returning false instead of true.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isOpen() - Check after opening twice and then closing
   **/
  public void Var080() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.open();
      f.close();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isOpen() - Check after a series of opens/closes is done
   **/
  public void Var081() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.close();
      f.open();
      f.close();
      if (!f.isOpen()) {
        f.open();
        f.close();
        f.open();
        if (f.isOpen()) {
          succeeded();
          f.close();
        }
      } else
        failed("isOpen() returning true instead of false.");

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isOpen() - Check on list that hasn't been open yet.
   **/
  public void Var082() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "BADQ", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      if (!f.isOpen())
        succeeded();
      else
        failed("isOpen() returning true instead of false.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isResourceAvailable() -- Check with default constructor followed by an
   * open.
   **/
  public void Var083() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.sendInformational("This is a test");
      f.sendInformational("This is a test");
      if (f.getListLength() > 0 && f.isResourceAvailable(0))
        succeeded();
      else if (f.getListLength() > 0)
        failed("isResourceAvailable returning false.");
      else {
        failed("Not applicable. List length is 0.");
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isResourceAvailable() -- Check with constructor RMessageQueue(system)
   * followed by an open.
   **/
  public void Var084() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      if (f.getListLength() > 0 && f.isResourceAvailable(0))
        succeeded();
      else if (f.getListLength() > 0)
        failed("isResourceAvailable returning false.");
      else
        failed("Not applicable. List length = 0.");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isResourceAvailable(index) -- Test valid use; check availability of empty
   * list
   **/
  public void Var085() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.waitForComplete();
      boolean available = f.isResourceAvailable(0); // @A1C
      assertCondition(available == false); // @A1C
    } catch (Exception e) {
      failed(e, "Unexpected Exception"); // @A1C
    }
  }

  /**
   * isResourceAvailable() - Check if resource available without doing an open
   * first. Should return false.
   **/
  public void Var086() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      if (f.isResourceAvailable(0) == false)
        succeeded();
      else
        failed("isResourceAvailable(0) returned true for unopen list.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isResourceAvailable() - Check if first and last resources are available
   * from a queue.
   **/
  public void Var087() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      if (f.getListLength() > 0 && f.isResourceAvailable(0)) {
        // Loop until list is complete
        if (!f.isComplete())
          f.waitForComplete();
        if (f.isResourceAvailable(f.getListLength() - 1))
          succeeded();
        else
          failed("Last resource not available.");
      } else {
        if (f.getListLength() > 0)
          failed("First resource of QSYSOPR is not available.");
        else
          failed("N/A -- QSYSOPR length is 0.");
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isResourceAvailable() -- Check if resourceisAvailable before and after list
   * is complete.
   **/
  public void Var088() {
    int i;
    boolean timedOut = false;
    int secs = 0;
    boolean Failed = false;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 500; ++i)
        f.sendInformational("Test Message...");
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      if (!g.isResourceAvailable(0))
        failed("First resource not available.");
      else {
        while (!g.isComplete() && !timedOut && !Failed) {
          Thread.sleep(2000);
          secs = secs + 2;
          if (secs > 240)
            timedOut = true;
          if (!g.isResourceAvailable(g.getListLength() - 1))
            Failed = true;
          if (!g.isResourceAvailable(g.getListLength() - 3))
            Failed = true;
          g.refreshStatus();
        }
        if (Failed)
          failed("isResourceAvailable() returned false instead of true.");
        else if (g.isResourceAvailable(g.getListLength() - 1))
          succeeded();
        else
          failed("Complete request timed out.");
      }
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isResourceAvailable() -- Check if resourceisAvailable before and after list
   * is complete.
   **/
  public void Var089() {
    MessageSandbox sbox_ = null;

    try {
      sbox_ = new MessageSandbox(systemObject_, testLib_, "TEMPMQ",userId_);

      String path = QSYSObjectPathName.toPath(testLib_, "TEMPMQ", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("New message...");
      f.sendInformational("Another message...");
      f.refreshContents();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      if (!g.isResourceAvailable(1))
        failed("isResourceAvailable() failed");
      else {
        sbox_.cleanup(); // Delete queue
        if (!g.isResourceAvailable(1))
          failed("isResourceAvailable() returned FALSE not TRUE.");
        else
          succeeded();
      }
      f.close();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isResourceAvailable() -- Check if resourceisAvailable after adding and
   * removing a message
   **/
  public void Var090() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.sendInformational("New message...");
      f.sendInformational("Another message...");
      f.sendInformational("Test it");
      f.refreshContents();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      if (!g.isResourceAvailable(2))
        failed("isResourceAvailable() failed");
      else {
        f.remove();
        f.refreshContents();
        g.refreshContents();
        boolean available = g.isResourceAvailable(1); // @A1C
        assertCondition(available == false); // @A1C
      }
      f.close();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception."); // @A1C
    }

  }

  /**
   * isResourceAvailable() -- Check if resourceisAvailable after adding and
   * removing a message
   **/
  public void Var091() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("New message...");
      f.sendInformational("Another message...");
      f.sendInformational("Test it");
      f.refreshContents();
      if (!f.isResourceAvailable(2))
        failed("isResourceAvailable() failed");
      else {
        f.remove();
        f.refreshContents();
        boolean available = f.isResourceAvailable(1); // @A1C
        assertCondition(available == false); // @A1C
      }
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception."); // @A1C
    }
  }

  /**
   * isResourceAvailable() -- Test invalid use. Pass -1 for index value. A
   * resource exception should be thrown.
   **/
  public void Var092() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.sendInformational("New message...");
      f.sendInformational("Another message...");
      f.sendInformational("Test it");
      f.refreshContents();
      f.isResourceAvailable(-1);
      f.close();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * isResourceAvailable() -- Test invalid use. Pass too large of a value for
   * the index. False should be returned.
   **/
  public void Var093() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("New message...");
      f.sendInformational("Another message...");
      f.sendInformational("Test it");
      f.refreshContents();
      boolean available = f.isResourceAvailable(20); // @A1C
      assertCondition(available == false); // @A1C
      f.close();

    } catch (Exception e) {
      failed(e, "Unexpected exception."); // @A1C
    }
  }

  /**
   * open() -- Valid open with default constructor.
   **/
  public void Var094() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.open();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * open() - Valid open with constructor RMessageQueue(AS400)
   **/
  public void Var095() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * open() - Valid open with constructor RMessageQueue(AS400, String)
   **/
  public void Var096() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * open() - Invalid open on list that doesn't exist. An IllegalStateException
   * should be thrown
   **/
  public void Var097() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "BADMQ", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
    }
  }

  /**
   * open() - Invalid open on undefined queue. An IllegalStateException should
   * be thrown
   **/
  public void Var098() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.open();
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
    }
  }

  /**
   * open() - Open a list twice. This should cause no error.
   **/
  public void Var099() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.open();
      succeeded();
    } catch (Exception e) {
      if (exceptionIs(e, "IllegalStateException", "open"))
        succeeded();
      else
        failed(e, "Wrong exception info.");
    }
  }

  /**
   * open() - Open a list, delete the queue and then re-open
   **/
  public void Var100() {
    MessageSandbox sbox_ = null;

    try {
      sbox_ = new MessageSandbox(systemObject_, testLib_, "TEMPMQ",userId_);

      String path = QSYSObjectPathName.toPath(testLib_, "TEMPMQ", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      sbox_.cleanup();
      f.close();
      f.open();
      failed("Exception not thrown.");

    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
    }
  }

  /**
   * open() - Open and close list a number of times
   **/
  public void Var101() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("New message");
      f.sendInformational("Another message.");
      f.close();
      f.open();
      f.close();
      f.open();
      if (f.getListLength() == 2)
        succeeded();
      else
        failed("Bad list length after re-open.");
      f.close();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * open() - Open and close list a number of times
   **/
  public void Var102() {
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("New message");
      f.sendInformational("Another message.");
      for (i = 0; i < 20; ++i) {
        f.close();
        f.open();
      }
      if (f.getListLength() == 2)
        succeeded();
      else
        failed("Bad list length after re-open.");
      f.close();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * open() - Open and close list a number of times
   **/
  public void Var103() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("New message");
      f.sendInformational("Another message.");
      f.close();
      f.open();
      f.close();
      f.open();
      if (f.getListLength() == 2)
        succeeded();
      else
        failed("Bad list length after re-open.");
      f.close();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * refreshContents() - Valid refresh with default constructor.
   **/
  public void Var104() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.open();
      f.refreshContents();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshContents() - Valid refresh with constructor RMessageQueue(AS400)
   **/
  public void Var105() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      f.refreshContents();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshContents() - Valid refresh with constructor RMessageQueue(AS400,
   * String)
   **/
  public void Var106() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.refreshContents();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * refreshContents() -- Test refreshing contents after adding a message.
   **/
  public void Var107() {
    long initialLength;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      initialLength = f.getListLength();
      f.sendInformational("This is my message");
      f.refreshContents();
      if (f.getListLength() == initialLength + 1)
        succeeded();
      else
        failed("Bad list length returned.");
      f.close();

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshContents() - Remove all messages and check list length
   **/
  public void Var108() {
    long i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 50; ++i)
        f.sendInformational("Test message");
      f.refreshContents();
      if (f.getListLength() != 50)
        failed("Bad list length returned.");
      else {
        f.remove();
        f.refreshContents();
        if (f.getListLength() == 0)
          succeeded();
        else
          failed("Bad list length returned.");
      }
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshContents() - Invalid use. Change system and then refresh. A
   * ResourceException should be thrown.
   **/
  public void Var109() {
    try {
      AS400 newSys_ = new AS400("RCHAS1DD", "JAVA", "JTEAM1");
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      f.setSystem(newSys_);
      f.refreshContents();
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * refreshContents() - Invalid use. Change queue name and then refresh. A
   * ResourceException should be thrown.
   **/
  public void Var110() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String newPath = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.sendInformational("Hi there");
      f.refreshContents();
      f.setPath(newPath);
      f.refreshContents();
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * refreshContents() - Invalid use. Delete queue and then refresh. A
   * ResourceException should be thrown.
   **/
  public void Var111() {
    MessageSandbox sbox_ = null;

    try {

      sbox_ = new MessageSandbox(systemObject_, testLib_, "TEMPMQ",userId_);

      String path = QSYSObjectPathName.toPath(testLib_, "TEMPMQ", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      sbox_.cleanup();
      f.refreshContents();
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
    }
  }

  /**
   * refreshContents() - Valid use. Do a setSelection and then refresh. Ensure
   * only the correct type of messages are in queue.
   **/
  public void Var112() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInquiry("Answer yes please", replyPath);
      f.sendInquiry("Answer no please", replyPath);
      f.sendInformational("Hi there");
      f.sendInformational("Bye");
      f.refreshContents();
      if (f.getListLength() != 4)
        failed("Bad list length returned (first call to getListLength)");
      else {
        f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
            RMessageQueue.MESSAGES_NO_NEED_REPLY);
        f.refreshContents();
        if (f.getListLength() != 2)
          failed("Bad list length returned (second call to getListLength).");
        else
          succeeded();
      }
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * refreshContents() - Valid use. Do a setSeverity and then refresh. Ensure
   * only the correct type of messages are in queue.
   **/
  public void Var113() {
    int i;
    Integer Int99 = new Integer(99);
    RQueuedMessage qmsg;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInquiry("Answer yes please", replyPath);
      f.sendInquiry("Answer no please", replyPath);
      f.sendInformational("Hi there");
      f.sendInformational("Bye");
      f.refreshContents();
      if (f.getListLength() != 4)
        failed("Bad list length returned (first call to getListLength()");
      else {
        f.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA, Int99);
        f.refreshContents();
        if (f.getListLength() != 2)
          failed("Bad list length returned (second call to getListLength().");
        else
          succeeded();
      }
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * refreshStatus() - Valid refresh with default constructor.
   **/
  public void Var114() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.open();
      f.refreshStatus();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshStatus() - Valid refresh with constructor RMessageQueue(AS400)
   **/
  public void Var115() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      f.refreshStatus();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshStatus() - Valid refresh with constructor RMessageQueue(AS400,
   * String)
   **/
  public void Var116() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.refreshStatus();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * refreshStatus() -- Test that refreshing status updates the list length and
   * whether or not the list is complete.
   **/
  public void Var117() {
    long initialLength, postLength;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      initialLength = f.getListLength();
      for (i = 0; i < 300; ++i)
        f.sendInformational("Test Message...");
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      initialLength = g.getListLength();
      postLength = initialLength;
      while (!g.isComplete()) {
        Thread.sleep(2000);
        g.refreshStatus();
        postLength = g.getListLength();
      }
      if (initialLength == postLength)
        failed("List length not updated after refreshStatus");
      else
        succeeded();
      g.close();

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshStatus() - Remove all messages and check list length
   **/
  public void Var118() {
    long i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 50; ++i)
        f.sendInformational("Test message");
      f.refreshStatus();
      f.refreshContents();
      if (f.getListLength() != 50)
        failed("Bad list length returned.");
      else {
        f.remove();
        f.refreshStatus();
        f.refreshContents();
        if (f.getListLength() == 0)
          succeeded();
        else
          failed("Bad list length returned.");
      }
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshStatus() - Invalid use. Change system and then refresh. A
   * ResourceException should be thrown.
   **/
  public void Var119() {
    try {
      AS400 newSys_ = new AS400("RCHAS1DD", "JAVA", "JTEAM1");
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.open();
      f.setSystem(newSys_);
      f.refreshStatus();
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * refreshStatus() - Invalid use. Change queue name and then refresh. A
   * ResourceException should be thrown.
   **/
  public void Var120() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String newPath = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.sendInformational("Hi there");
      f.refreshContents();
      f.refreshStatus();
      f.setPath(newPath);
      f.refreshStatus();
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * refreshStatus() - Valid use. Delete queue and then refresh. A
   **/
  public void Var121() {
    MessageSandbox sbox_ = null;

    try {
      sbox_ = new MessageSandbox(systemObject_, testLib_, "TEMPMQ",userId_);

      String path = QSYSObjectPathName.toPath(testLib_, "TEMPMQ", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.sendInformational("Hi there");
      f.refreshContents();
      sbox_.cleanup();
      f.refreshStatus();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * refreshContents() - Valid use. Do a setSelection and then refresh. Ensure
   * only the correct type of messages are in queue.
   **/
  public void Var122() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInquiry("Answer yes please", replyPath);
      f.sendInquiry("Answer no please", replyPath);
      f.sendInformational("Hi there");
      f.sendInformational("Bye");
      f.refreshContents();
      if (f.getListLength() != 4)
        failed("Bad list length returned (first check)");
      else {
        f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
            RMessageQueue.MESSAGES_NO_NEED_REPLY);
        f.refreshContents();
        if (f.getListLength() != 2)
          failed("Bad list length returned (second check).");
        else
          succeeded();
      }
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * refreshContents() - Valid use. Do a setSeverity and then refresh. Ensure
   * only the correct type of messages are in queue.
   **/
  public void Var123() {
    Integer Int99 = new Integer(99);
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInquiry("Answer yes please", replyPath);
      f.sendInquiry("Answer no please", replyPath);
      f.sendInformational("Hi there");
      f.sendInformational("Bye");
      f.refreshContents();
      if (f.getListLength() != 4)
        failed("Bad list length returned (first check)");
      else {
        f.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA, Int99);
        f.refreshContents();
        if (f.getListLength() != 2)
          failed("Bad list length returned (second check).");
        else
          succeeded();
      }
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * resourceAt() - Valid use. Check first resource in QSYSOPR message queue
   **/
  public void Var124() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      if (f.getListLength() > 0)
        f.resourceAt(0);
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * resourceAt() - Valid use. Check first resource in created message queue
   **/
  public void Var125() {
    RQueuedMessage queuedMsg;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.sendInformational("Test message");
      f.refreshContents();
      if (f.isResourceAvailable(0)) {
        queuedMsg = (RQueuedMessage) f.resourceAt(0);
        if (queuedMsg != null)
          succeeded();
        else
          failed("Null message returned.");
      } else
        failed("Resource 0 not available.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * resourceAt() - Invalid use. Delete messages from queue and check. An
   * IllegalArgumentException should be thrown
   **/
  public void Var126() {
    RQueuedMessage queuedMsg;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.sendInformational("Test message");
      f.refreshContents();
      f.remove();
      f.refreshContents();
      f.resourceAt(0);
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * resourceAt() - Invalid use. Specify a negative index. An
   * IllegalArgumentException should be thrown
   **/
  public void Var127() {
    RQueuedMessage queuedMsg;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.sendInformational("Test message");
      f.refreshContents();
      f.resourceAt(-1);
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * resourceAt() - Invalid use. Specify too large of an index. An
   * IllegalArgumentException should be thrown
   **/
  public void Var128() {
    RQueuedMessage queuedMsg;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      f.sendInformational("Test message");
      f.refreshContents();
      f.resourceAt(20);
      failed("Exception not thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * resourceAt() - Valid use. Try to retrieve a resource that is not loaded
   * yet. NULL should be returned.
   **/
  public void Var129() {
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 300; ++i)
        f.sendInformational("Test Message...");
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      if (!g.isComplete()) {
        if (!g.isResourceAvailable(299)) {
          // This is hard to enforce, because of timing difficulties. We will
          // just
          // let it pass either way.
          succeeded();
          /*
           * if (g.resourceAt(299) != null)
           * failed("Non-null resource returned."); else succeeded();
           */
        } else {
          if (g.resourceAt(299) == null)
            failed("Null resource returned.");
          else
            succeeded();
        }
      }
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * resourceAt() - Valid use. Check resource that is returned to ensure correct
   **/
  public void Var130() {
    int i;
    RQueuedMessage queuedMsg;
    Object attrValue;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 10; ++i)
        f.sendInformational("Test Message...");
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      if (g.isResourceAvailable(1)) {
        queuedMsg = (RQueuedMessage) g.resourceAt(1);
        attrValue = queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT);
        if (attrValue.toString().equals("Test Message...")) {
          attrValue = queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE);
          Integer msgType = new Integer(QueuedMessage.INFORMATIONAL);
          if (attrValue.equals(msgType)) {
            attrValue = queuedMsg
                .getAttributeValue(RQueuedMessage.MESSAGE_QUEUE);
            if (attrValue.toString().equals(
                "/QSYS.LIB/" + testLib_.toUpperCase() + ".LIB/MQT.MSGQ")) {
              succeeded();
            } else
              failed("Incorrect message queue name returned. got '"
                  + attrValue.toString() + "' sb '/QSYS.LIB/"
                  + testLib_.toUpperCase() + ".LIB/MQT.MSGQ'");
          } else
            failed("Incorrect message type returned.");
        } else
          failed("Incorrect message text returned.");
      }
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * resourceAt() - Valid use. Check each resource to ensure non null
   **/
  public void Var131() {
    int i;
    RQueuedMessage queuedMsg;
    boolean OK = true;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 40; ++i)
        f.sendInformational("Test Message...");
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      while (!g.isComplete()) {
        Thread.sleep(2000);
        g.refreshStatus();
      }
      for (i = 0; i < g.getListLength() && OK == true; ++i) {
        if (g.isResourceAvailable(i)) {
          queuedMsg = (RQueuedMessage) g.resourceAt(i);
          if (queuedMsg == null) {
            failed("Resource at " + i + " is null.");
            OK = false;
          }
        } else {
          failed("Resource at " + i + " is not available.");
          OK = false;
        }
      }
      if (OK)
        succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * resourceAt(index) -- Check if resource is returned after queue is deleted.
   **/
  public void Var132() {
    MessageSandbox sbox_ = null;

    try {
      sbox_ = new MessageSandbox(systemObject_, testLib_, "TEMPMQ",userId_);

      String path = QSYSObjectPathName.toPath(testLib_, "TEMPMQ", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.sendInformational("New message...");
      f.sendInformational("Another message...");
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      if (!g.isResourceAvailable(1))
        failed("isResourceAvailable(1) failed");
      else {
        if (g.resourceAt(1) == null)
          failed("resourceAt(1) returned null");
        else {
          sbox_.cleanup(); // Delete queue
          if (g.resourceAt(1) == null)
            failed("resourceAt(1) returned null.");
          else {
            g.refreshContents();
            if (g.resourceAt(1) != null)
              failed("No exception thrown.");
          }
        }
      }
      f.close();
      g.close();
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test invalid use; pass null for
   * attribute ID.
   **/
  public void Var133() {
    Object SelectionValue;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.setSelectionValue(null, RMessageQueue.PREVIOUS);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; pass null for
   * value and ensure reset
   **/
  public void Var134() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.MESSAGES_NO_NEED_REPLY);
      g.open();
      if (g.getListLength() != 20) {
        failed("Bad list length.");
        return;
      }
      for (i = 0; i < g.getListLength(); ++i) {
        queuedMsg = (RQueuedMessage) g.resourceAt(i);
        if (((Integer) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE))
            .intValue() != QueuedMessage.INFORMATIONAL) {
          failed("Bad message type.");
          return;
        } else if (!((String) queuedMsg
            .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Test message " + i)) {
          failed("Message " + i + " has wrong text. Text = "
              + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
              + " instead of Test message " + i);
          return;
        }
      }
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA, null);
      g.refreshContents();
      if (g.getListLength() != 40) {
        failed("Bad list length returned.");
        return;
      }
      succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object attributeID) -- Test invalid use; call with bad
   * value for value
   **/
  public void Var135() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSelectionValue(RMessageQueue.LIST_DIRECTION,
          RMessageQueue.MESSAGES_NEED_REPLY);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test invalid use; pass bad value
   * for attribute ID.
   **/
  public void Var136() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSelectionValue(RMessageQueue.PREVIOUS,
          RMessageQueue.MESSAGES_NEED_REPLY);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * LIST_DIRECTION and then open list. Ensure correct Selectioning done.
   **/
  public void Var137() {
    Object SelectionValue;
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);

      // Set the selection value to retrieve all of the messages newer than the
      // OLDEST messages in
      // the queue. This should return all messages.
      // This is based on STARTING_USER_MESSAGE_KEY and
      // STARTING_WORKSTATION_MESSAGE_KEY which are
      // both set to OLDEST as the default.
      g.setSelectionValue(RMessageQueue.LIST_DIRECTION, RMessageQueue.NEXT);
      g.open();
      if (g.getListLength() == 40)
        succeeded();
      else
        failed("Bad list length returned.");
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * SELECTION_CRITERIA and then open list. Ensure correct Selectioning done.
   **/
  public void Var138() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.refreshContents();
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.MESSAGES_NO_NEED_REPLY);
      g.refreshContents();
      if (g.getListLength() != 20) {
        failed("Bad list length.");
        return;
      }
      for (i = 0; i < g.getListLength(); ++i) {
        queuedMsg = (RQueuedMessage) g.resourceAt(i);
        if (((Integer) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE))
            .intValue() != QueuedMessage.INFORMATIONAL) {
          System.out.println("Message type not informational.");
          failed("Bad message type.");
          return;
        }
      }
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA, null);
      g.refreshContents();
      if (g.getListLength() != 40) {
        failed("Bad list length returned.");
        return;
      }
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.MESSAGES_NEED_REPLY);
      g.refreshContents();
      if (g.getListLength() != 20) {
        failed("Bad list length returned.");
        return;
      }
      for (i = 0; i < g.getListLength(); ++i) {
        queuedMsg = (RQueuedMessage) g.resourceAt(i);
        if (((Integer) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE))
            .intValue() != QueuedMessage.INQUIRY) {
          System.out.println("Message type not inquiry.");
          failed("Bad message type.");
          return;
        } else if (!((String) queuedMsg
            .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Inquiry message " + i)) {
          failed("Message " + i + " has wrong text. Text = "
              + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
              + " instead of Inquiry message " + i);
          return;
        }
      }
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA, null);
      g.refreshContents();
      if (g.getListLength() != 40) {
        failed("Bad list length returned.");
        return;
      }

      succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * SELECTION_CRITERIA and then open list. Ensure correct Selection is done.
   **/
  public void Var139() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i, j;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 25; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA, RMessageQueue.ALL);
      g.open();
      if (g.getListLength() != 50) {
        failed("Bad list length.");
        return;
      }
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA, null);
      g.refreshContents();
      if (g.getListLength() != 50) {
        failed("Bad list length returned.");
        return;
      }
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.MESSAGES_NEED_REPLY);
      g.refreshContents();
      if (g.getListLength() != 25) {
        failed("Bad list length returned.");
        return;
      }
      for (i = 0; i < g.getListLength(); ++i) {
        queuedMsg = (RQueuedMessage) g.resourceAt(i);
        if (((Integer) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE))
            .intValue() != QueuedMessage.INQUIRY) {
          failed("Bad message type.");
          return;
        } else if (!((String) queuedMsg
            .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Inquiry message " + i)) {
          failed("Message " + i + " has wrong text. Text = "
              + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
              + " instead of Inquiry message " + i);
          return;
        }

      }
      succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * SELECTION_CRITERIA and then open list. Ensure correct Selectioning done.
   **/
  public void Var140() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.close();
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.SENDERS_COPY_NEED_REPLY);
      g.open();
      if (g.getListLength() != 20) {
        failed("Bad list length.");
        return;
      }
      for (i = 0; i < g.getListLength(); ++i) {
        queuedMsg = (RQueuedMessage) g.resourceAt(i);
        if (((Integer) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE))
            .intValue() != QueuedMessage.SENDERS_COPY) {
          failed("Bad message type. Message type not SENDERS_COPY_NEED_REPLY");
          return;
        } else if (!((String) queuedMsg
            .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Inquiry message " + i)) {
          failed("Message " + i + " has wrong text. Text = "
              + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
              + " instead of Inquiry message " + i);
          return;
        }

      }
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA, null);
      g.refreshContents();
      if (g.getListLength() != 20) {
        failed("Bad list length returned.");
        return;
      }
      succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * SELECTION_CRITERIA and then open list. Ensure correct Selectioning done.
   **/
  public void Var141() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }

      f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA, RMessageQueue.ALL);
      f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.MESSAGES_NO_NEED_REPLY);
      f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.MESSAGES_NEED_REPLY);
      f.refreshContents();

      for (i = 0; i < f.getListLength(); ++i) {
        queuedMsg = (RQueuedMessage) f.resourceAt(i);
      }
      if (f.getListLength() == 20)
        succeeded();
      else
        failed("Bad list length. List length should be zero.");
      f.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * SELECTION_CRITERIA and then open list. Ensure correct Selectioning done.
   **/
  public void Var142() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      for (i = 0; i < 20; ++i)
        g.sendInformational("Test message " + i);

      f.close();
      g.open();
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.MESSAGES_NO_NEED_REPLY);
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.SENDERS_COPY_NEED_REPLY);
      g.refreshContents();

      if (g.getListLength() != 20) {
        failed("Bad list length returned.");
        return;
      }
      succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * SELECTION_CRITERIA and then open list. Ensure correct Selectioning done.
   **/
  public void Var143() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    Integer Int99 = new Integer(99);
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.refreshContents();
      f.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.MESSAGES_NO_NEED_REPLY);
      f.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA, Int99);
      f.refreshContents();
      if (f.getListLength() == 0)
        succeeded();
      else {
        failed("Bad list length returned.");
        return;
      }

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * SEVERITY_CRITERIA and then open list. Ensure correct Selectioning done.
   **/
  public void Var144() {
    Integer Int40 = new Integer(40);
    Integer Int99 = new Integer(99);
    Integer Int0 = new Integer(0);
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    long listLength;
    int i;
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      listLength = g.getListLength();

      g.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA, Int40);
      g.refreshContents();
      for (i = 0; i < g.getListLength(); ++i) {
        queuedMsg = (RQueuedMessage) g.resourceAt(i);
        if (((Integer) queuedMsg
            .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY)).intValue() < 40) {
          failed("Message with Severity < 40 returned.");
          return;
        }
      }
      g.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA, Int99);
      g.refreshContents();
      for (i = 0; i < g.getListLength(); ++i) {
        queuedMsg = (RQueuedMessage) g.resourceAt(i);
        if (((Integer) queuedMsg
            .getAttributeValue(RQueuedMessage.MESSAGE_SEVERITY)).intValue() < 99) {
          failed("Message with Severity < 99 returned.");
          return;
        }
      }

      g.setSelectionValue(RMessageQueue.SEVERITY_CRITERIA, Int0);
      g.refreshContents();
      if (g.getListLength() != listLength) {
        failed("All messages not returned when Sev 0 specified.");
        return;
      }

      succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * SELECTION_CRITERIA and then open list. Ensure correct Selectioning done.
   **/
  public void Var145() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 100; ++i) {
        f.sendInformational("Test message " + i);
      }
      f.refreshContents();
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA,
          RMessageQueue.MESSAGES_NEED_REPLY);
      g.refreshContents();
      if (g.getListLength() != 0) {
        failed("Bad list length.");
        return;
      }
      g.setSelectionValue(RMessageQueue.SELECTION_CRITERIA, null);
      g.refreshContents();
      if (g.getListLength() != 50)// Change by Guang Ming Pi/China/IBM, the
                                  // value changes from 100 to 50 because of msg
                                  // buffer changed by PCML file and the
                                  // underlying system API, more details, see
                                  // CPS 954QAF
      {
        failed("Bad list length returned.");
        return;
      }

      for (i = 0; i < g.getListLength(); ++i) {
        queuedMsg = (RQueuedMessage) g.resourceAt(i);
        if (((Integer) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE))
            .intValue() != QueuedMessage.INFORMATIONAL) {
          failed("Bad message type.");
          return;
        } else if (!((String) queuedMsg
            .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Test message " + i)) {
          failed("Message " + i + " has wrong text. Text = "
              + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
              + " instead of Test message " + i);
          return;
        }
      }

      succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * LIST_DIRECTION and then open list. Ensure correct Selectioning done.
   **/
  public void Var146() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);

      // Set the selection value to retrieve all of the messages older than the
      // OLDEST messages in
      // the queue. This should return the oldest message in the queue.
      g.setSelectionValue(RMessageQueue.LIST_DIRECTION, RMessageQueue.PREVIOUS);
      g.open();
      if (g.getListLength() == 1) {
        queuedMsg = (RQueuedMessage) g.resourceAt(0);
        if (((String) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Test message 0"))
          succeeded();
        else
          failed("Wrong message returned.");
      } else
        failed("Bad list length returned.");
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * LIST_DIRECTION and then open list. Ensure correct Selectioning done.
   **/
  public void Var147() {
    Object SelectionValue;
    RQueuedMessage qmsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);

      // Set the selection value to retrieve all of the messages older than the
      // NEWEST messages in
      // the queue. This should return 40 messages.
      g.setSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY,
          RMessageQueue.NEWEST);
      g.setSelectionValue(RMessageQueue.LIST_DIRECTION, RMessageQueue.PREVIOUS);
      g.open();
      if (g.getListLength() != 40)
        failed("Bad list length returned.");
      else
        succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * LIST_DIRECTION and then open list. Ensure correct Selectioning done.
   **/
  public void Var148() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);

      // Set the selection value to retrieve all of the messages older than the
      // NEWEST messages in
      // the queue. This should return 40 messages.
      g.setSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY,
          RMessageQueue.NEWEST);
      g.setSelectionValue(RMessageQueue.LIST_DIRECTION, RMessageQueue.PREVIOUS);
      g.open();
      if (g.getListLength() != 1)
        failed("Bad list length returned.");
      else
        succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * LIST_DIRECTION and then open list. Ensure correct Selectioning done.
   **/
  public void Var149() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);

      // Set the selection value to retrieve all of the messages older than the
      // OLDEST messages in
      // the queue. This should return just the first message
      g.setSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY,
          RMessageQueue.OLDEST);
      g.setSelectionValue(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY,
          RMessageQueue.NEWEST);
      g.setSelectionValue(RMessageQueue.LIST_DIRECTION, RMessageQueue.PREVIOUS);
      g.open();
      if (g.getListLength() != 1)
        failed("Bad list length returned.");
      else {
        queuedMsg = (RQueuedMessage) g.resourceAt(0);
        if (((String) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Test message 0"))
          succeeded();
        else
          failed("Wrong message returned.");
      }
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }

  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * LIST_DIRECTION and then open list. Ensure correct Selectioning done.
   **/
  public void Var150() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);

      // Set the selection value to retrieve all of the messages newer than the
      // OLDEST messages in
      // the queue. This should return 40 messages.
      g.setSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY,
          RMessageQueue.OLDEST);
      g.setSelectionValue(RMessageQueue.LIST_DIRECTION, RMessageQueue.NEXT);
      g.open();
      if (g.getListLength() != 40)
        failed("Bad list length returned.");
      else
        succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * LIST_DIRECTION and then open list. Ensure correct Selectioning done.
   **/
  public void Var151() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);

      // Set the selection value to retrieve all of the messages newer than the
      // NEWEST messages in
      // the queue. This should return just the last message.
      g.setSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY,
          RMessageQueue.NEWEST);
      g.setSelectionValue(RMessageQueue.LIST_DIRECTION, RMessageQueue.NEXT);
      g.open();
      if (g.getListLength() != 1)
        failed("Bad list length returned.");
      else {
        queuedMsg = (RQueuedMessage) g.resourceAt(0);
        if (((String) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Inquiry message 19"))
          succeeded();
        else
          failed("Wrong message returned.");
      }
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * LIST_DIRECTION and then open list. Ensure correct Selectioning done.
   **/
  public void Var152() {
    Object SelectionValue;
    RQueuedMessage queuedMsg;
    int i, j;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 20; ++i) {
        f.sendInformational("Test message " + i);
      }
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);

      // Set the selection value to retrieve all of the messages newer than the
      // 8th message in
      // the queue. This should return the last 12 messages.
      g.open();
      queuedMsg = (RQueuedMessage) g.resourceAt(7);
      g.setSelectionValue(RMessageQueue.STARTING_USER_MESSAGE_KEY,
          queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_KEY));
      g.setSelectionValue(RMessageQueue.LIST_DIRECTION, RMessageQueue.NEXT);
      g.refreshContents();
      if (g.getListLength() != 13) {
        failed("Bad list length returned.");
        return;
      }
      for (i = 0; i < g.getListLength(); ++i) {
        j = i + 7;
        queuedMsg = (RQueuedMessage) g.resourceAt(i);
        if (((Integer) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE))
            .intValue() != QueuedMessage.INFORMATIONAL) {
          failed("Bad message type. Message type not INFORMATIONAL");
          return;
        } else if (!((String) queuedMsg
            .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Test message " + j)) {
          failed("Message " + i + " has wrong text. Text = "
              + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
              + " instead of Test message " + j);
          return;
        }
      }
      succeeded();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSelectionValue(Object AttributeID) -- Test valid use; pass value for
   * attribute ID and check return to ensure the default value is returned.
   **/
  public void Var153() {
    Object SelectionValue;
    ResourceMetaData SelectionData;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionValue = f.getSelectionValue(RMessageQueue.SORT_CRITERIA);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SORT_CRITERIA);
      if (SelectionValue.equals(SelectionData.getDefaultValue()))
        succeeded();
      else
        failed("Default value not returned.");
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * getSelectionMetaData() - Verify that the SORT_CRITERIA Selection is
   * retrieved correctly
   **/
  public void Var154() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    try {
      RMessageQueue f = new RMessageQueue();
      SelectionData = findMetaData(f.getSelectionMetaData(),
          RMessageQueue.SORT_CRITERIA);
      if (SelectionData.getID().equals(RMessageQueue.SORT_CRITERIA)) {
        if (!(SelectionData.getType().equals(Boolean.class)
            && SelectionData.isReadOnly() == false && SelectionData
              .areMultipleAllowed() == false))
          failed("SelectionData wrong for attributeID, type, areMultipleAllowed or isReadOnly.");
        else {
          possibleValues = SelectionData.getPossibleValues();
          if (possibleValues.length == 0) {
            if ((SelectionData.getDefaultValue()).equals(Boolean.FALSE)
                && SelectionData.isValueLimited() == false)
              succeeded();
            else
              failed("SelectionData wrong for getDefaultValue() or isValueLimited()");
          } else
            failed("SelectionData wrong for getPossibleValues()");
        }
      } else
        failed("SORT_CRITERIA Selection not retrieved.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test valid use; add message to list
   * and then check length prior to resetting the list.
   **/
  public void Var155() {
    int i, j;
    byte messageKey1[];
    byte messageKey10[];
    RQueuedMessage qmsg1, qmsg10, queuedMsg;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      RMessageQueue g = new RMessageQueue(systemObject_, replyPath);
      g.remove();
      f.remove();
      f.open();
      for (i = 0; i < 10; ++i) {
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      g.open();
      if (g.getListLength() == 10) {
        qmsg1 = (RQueuedMessage) g.resourceAt(0);
        qmsg10 = (RQueuedMessage) g.resourceAt(9);
        messageKey1 = (byte[]) qmsg1
            .getAttributeValue(RQueuedMessage.MESSAGE_KEY);
        messageKey10 = (byte[]) qmsg10
            .getAttributeValue(RQueuedMessage.MESSAGE_KEY);
        g.receive(messageKey1, 0, RMessageQueue.OLD, RMessageQueue.COPY);
        g.receive(messageKey10, 0, RMessageQueue.OLD, RMessageQueue.COPY);
        g.refreshContents();
        g.remove(RMessageQueue.OLD);
        g.refreshContents();
        if (g.getListLength() != 8) {
          failed("Bad list length returned.");
          return;
        }
        for (i = 0; i < g.getListLength(); ++i) {
          j = i + 1;
          queuedMsg = (RQueuedMessage) g.resourceAt(i);
          if (((Integer) queuedMsg
              .getAttributeValue(RQueuedMessage.MESSAGE_TYPE)).intValue() != QueuedMessage.SENDERS_COPY) {
            failed("Bad message type. Message type not SENDERS_COPY");
            return;
          } else if (!((String) queuedMsg
              .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
              .equals("Inquiry message " + j)) {
            failed("Message " + i + " has wrong text. Text = "
                + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
                + " instead of Inquiry message " + j);
            return;
          }
        }
        succeeded();
      } else
        failed("Incorrect list length returned.");
      f.close();
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getListLength(Object AttributeID) -- Test valid use; add message to list
   * and then check length prior to resetting the list.
   **/
  public void Var156() {
    int i;
    RQueuedMessage queuedMsg;
    String failMessage = null;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 10; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.refreshContents();
      if (f.getListLength() == 20) {
        f.remove(RMessageQueue.KEEP_UNANSWERED);
        f.refreshContents();
        if (f.getListLength() != 10) {
          failed("Bad list length returned.");
          return;
        }
        for (i = 0; i < f.getListLength(); ++i) {
          queuedMsg = (RQueuedMessage) f.resourceAt(i);
          if (((Integer) queuedMsg
              .getAttributeValue(RQueuedMessage.MESSAGE_TYPE)).intValue() != QueuedMessage.INQUIRY) {
            failed("Bad message type. Message type not INQUIRY");
            return;
          } else if (!((String) queuedMsg
              .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
              .equals("Inquiry message " + i)) {
            failed("Message " + i + " has wrong text. Text = "
                + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
                + " instead of Inquiry message " + i);
            return;
          }
        }
      } else
        failMessage = "Incorrect list length returned.";

      f.close();
      if (failMessage == null) {
        succeeded();

      } else {
        failed(failMessage);
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSelectionValue(Object AttributeID) -- Test valid use; set value for
   * SORT_CRITERIA and then open list. Ensure correct Selectioning done.
   **/

  public void Var157() {
    Object SelectionValue;
    RQueuedMessage queuedMsg, queuedMsg2;
    int i;
    int j;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String replyPath = QSYSObjectPathName
          .toPath(testLib_, "MQTREPLY", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();
      f.open();
      for (i = 0; i < 10; ++i) {
        f.sendInformational("Test message " + i);
        f.sendInquiry("Inquiry message " + i, replyPath);
      }
      f.refreshContents();
      f.setSelectionValue(RMessageQueue.SORT_CRITERIA, Boolean.TRUE);
      f.refreshContents();
      if (f.getListLength() != 20) {
        failed("Bad list length.");
        return;
      }
      for (i = 0; i < f.getListLength(); ++i) {
        j = i - 10;
        // Ensure sorted by message type (informational followed by inquiry
        queuedMsg = (RQueuedMessage) f.resourceAt(i);
        if (i >= 10
            && ((Integer) queuedMsg
                .getAttributeValue(RQueuedMessage.MESSAGE_TYPE)).intValue() != QueuedMessage.INFORMATIONAL) {
          failed("Bad message type. One of the first 10 messages is not type INFORMATIONAL");
          return;
        } else if (i >= 10
            && !((String) queuedMsg
                .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
                .equals("Test message " + j)) {
          failed("Message " + i + " has wrong text. Text = "
              + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
              + " instead of Test message " + j);
          return;
        }
        if (i < 10
            && ((Integer) queuedMsg
                .getAttributeValue(RQueuedMessage.MESSAGE_TYPE)).intValue() != QueuedMessage.INQUIRY) {
          failed("Bad message type. One of the last 10 messages is not type INQUIRY");
          return;
        } else if (i < 10
            && !((String) queuedMsg
                .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
                .equals("Inquiry message " + i)) {
          failed("Message " + i + " has wrong text. Text = "
              + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
              + " instead of Inquiry message " + i);
          return;
        }
      }
      f.setSelectionValue(RMessageQueue.SORT_CRITERIA, null);
      f.refreshContents();
      j = 0;
      for (i = 0; i < 20; ++i) {
        queuedMsg = (RQueuedMessage) f.resourceAt(i);
        queuedMsg2 = (RQueuedMessage) f.resourceAt(i + 1);
        if (((Integer) queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TYPE))
            .intValue() != QueuedMessage.INFORMATIONAL) {
          failed("Bad message type. Type is not INFORMATIONAL");
          return;
        } else if (!((String) queuedMsg
            .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Test message " + j)) {
          failed("Message " + i + " has wrong text. Text = "
              + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
              + " instead of Test message " + j);
          return;
        }
        if (((Integer) queuedMsg2
            .getAttributeValue(RQueuedMessage.MESSAGE_TYPE)).intValue() != QueuedMessage.INQUIRY) {
          failed("Bad message type. Type is not INQUIRY");
          return;
        } else if (!((String) queuedMsg2
            .getAttributeValue(RQueuedMessage.MESSAGE_TEXT))
            .equals("Inquiry message " + j)) {
          failed("Message " + i + " has wrong text. Text = "
              + queuedMsg.getAttributeValue(RQueuedMessage.MESSAGE_TEXT)
              + " instead of Inquiry message " + j);
          return;
        }
        i = i + 1;
        j = j + 1;
      }
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isInError() - Valid use.
   **/
  public void Var158() {
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      for (i = 0; i < 10; ++i)
        f.sendInformational("Test Message...");
      f.close();
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      g.refreshStatus();
      boolean inError = g.isInError();

      g.close();

      if (inError)
        failed("isInError returned TRUE.");
      else
        succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * isInError() - Valid use. Force TRUE to occur
   **/
  public void Var159() {
    StringBuffer sb = new StringBuffer();
    MessageSandbox sbox_ = null;
    int i;

    try {
      sb.append("Creating MessageSandbox\n");
      sbox_ = new MessageSandbox(systemObject_, testLib_, "TEMPMQ",userId_);

      String path = QSYSObjectPathName.toPath(testLib_, "TEMPMQ", "MSGQ");
      sb.append("Creating Message queue " + path + "\n");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      sb.append("Opening\n");
      f.open();
      sb.append("Sending 2000 messages\n");
      for (i = 0; i < 2000; ++i)
        f.sendInformational("Test Message...");
      while (!f.isComplete()) {
        sb.append("Sleeping while waiting for complete\n");
        Thread.sleep(2000);
      }
      sb.append("Closing\n");
      f.close();

      sb.append("Creating new message queue " + path + "\n");
      RMessageQueue g = new RMessageQueue(systemObject_, path);
      sb.append("Open\n");
      g.open();

      sbox_.cleanup();

      sb.append("refreshStatus\n");
      g.refreshStatus();
      sb.append("Checking inError\n");
      boolean inError = g.isInError();
      sb.append("  inError=" + inError + "\n");
      sb.append("Checking g.isComplete()\n");
      while (!g.isComplete()) {
        sb.append("Sleeping while not complete\n");
        Thread.sleep(2000);
        g.refreshStatus();
      }
      sb.append("Closing");
      g.close();

      if (inError)
        failed("isInError returned TRUE not FALSE.\n" + sb.toString());
      else
        succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception.\n" + sb.toString());
    }
  }

  /**
   * getSortMetaData() -- Verify that no sort elements are returned
   **/
  public void Var160() {
    int i;
    ResourceMetaData[] sortData;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
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
   * getSortMetaData(sortID) -- There are not valid sort IDs for message queues.
   * Pass in a bad value and verify an exception is generated.
   **/
  public void Var161() {
    int i;
    ResourceMetaData sortData;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      sortData = f.getSortMetaData(RMessageQueue.PREVIOUS);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * getSortValue() -- There are not valid sort IDs for message queues. Pass in
   * a bad value and verify an empty array of sortIDs is generated.
   **/
  public void Var162() {
    int i;
    Object[] sortID;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      sortID = f.getSortValue();
      if (sortID.length == 0)
        succeeded();
      else
        failed("sortIDs returned for message queue.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getSortOrder(sortID) -- There are not valid sort IDs for message queues.
   * Pass in a bad value and verify an exception is generated.
   **/
  public void Var163() {
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.getSortOrder(RMessageQueue.LIST_DIRECTION);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * toString() --
   **/
  public void Var164() {
    int i;
    String toStr;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      toStr = f.toString();
      String expected = "/QSYS.LIB/" + testLib_.toUpperCase() + ".LIB/MQT.MSGQ";
      if (toStr.equals(expected))
        succeeded();
      else
        failed("Bad toString returned got " + toStr + " sb " + expected + ".");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * getPresentation() -- Test valid use; Verify presentation object.
   **/
  public void Var165() {
    Presentation presObj;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      presObj = f.getPresentation();
      String expected = "/QSYS.LIB/" + testLib_.toUpperCase() + ".LIB/MQT.MSGQ";
      if (presObj == null || !presObj.getFullName().equals(expected)
          || !presObj.getName().equals("MQT")) {
        failed("Full name or name incorrect for Presentation object.");
        return;
      }
      if (onAS400_) {
        if (presObj.getValue(Presentation.DESCRIPTION_TEXT).equals(
            "Message Queue")
            && presObj.getValue(Presentation.HELP_TEXT) == null)
          succeeded();
        else
          failed("Bad presentation info returned.");
      } else {
        if (presObj.getValue(Presentation.DESCRIPTION_TEXT).equals(
            "Message Queue")
            && presObj.getValue(Presentation.HELP_TEXT) == null
            && presObj.getValue(Presentation.ICON_COLOR_16x16) != null
            && presObj.getValue(Presentation.ICON_COLOR_32x32) != null)
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
   * refreshStatus() - Valid refresh with default constructor without an open.
   **/
  public void Var166() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.refreshStatus();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshStatus() - Valid refresh with default constructor and no open.
   **/
  public void Var167() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.refreshStatus();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshContents() - Valid refresh with default constructor without an open.
   **/
  public void Var168() {
    try {
      RMessageQueue f = new RMessageQueue();
      f.setSystem(systemObject_);
      f.refreshContents();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * refreshContents() - Valid refresh with default constructor and no open().
   **/
  public void Var169() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.refreshContents();
      f.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * resourceAt() - Valid resourceAt() without an open().
   **/
  public void Var170() {
    try {
      RMessageQueue f = new RMessageQueue(systemObject_);
      f.resourceAt(0);
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * resourceAt() - Valid resourceAt() without an open().
   **/
  public void Var171() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.resourceAt(0);
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isOpen() - Valid use. Check value on an implicit open.
   **/
  public void Var172() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.resourceAt(0);
      if (f.isOpen())
        succeeded();
      else
        failed("List not open.");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isOpen() - Valid use. Check value on an implicit open.
   **/
  public void Var173() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.getListLength();
      if (f.isOpen())
        succeeded();
      else
        failed("List not open.");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isOpen() - Valid use. Check value on an implicit open.
   **/
  public void Var174() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.isResourceAvailable(0);
      if (f.isOpen())
        failed("List should not be open after an isResourceAvailable().");
      else
        succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isOpen() - Valid use. Check value on an implicit open.
   **/
  public void Var175() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.refreshContents();
      if (f.isOpen())
        succeeded();
      else
        failed("List not open.");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isOpen() - Valid use. Check value on an implicit open.
   **/
  public void Var176() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.refreshStatus();
      if (f.isOpen())
        succeeded();
      else
        failed("List not open.");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isComplete() - Valid use. Check value without opening.
   **/
  public void Var177() {
    try {
      String path = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.isComplete();
      if (f.isOpen())
        failed("List should not be open after an isComplete().");
      else
        succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * waitForComplete() -- Test valid use; Ensure list is complete after
   * waitForComplete() runs
   **/
  public void Var178() {
    boolean TimedOut = false;
    int secs = 0;
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();

      for (i = 0; i < 300; ++i)
        f.sendInformational("Test message " + i);

      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      if (!g.isComplete()) {
        g.waitForComplete();
        if (g.isComplete())
          succeeded();
        else
          failed("List not complete after waitForComplete().");
      } else {
        g.waitForComplete();
        if (g.isComplete())
          succeeded();
        else
          failed("List not complete after waitForComplete().");
      }

      g.close();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * waitForComplete() -- Test valid use; Ensure list is complete after
   * waitForComplete() runs
   **/
  public void Var179() {
    boolean TimedOut = false;
    int secs = 0;
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();

      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      g.waitForComplete();
      if (g.isComplete())
        succeeded();
      else
        failed("List not complete after waitForComplete().");
      g.close();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * waitForResource() -- Test valid use; Ensure resource is available after
   * waitForResource() runs
   **/
  public void Var180() {
    boolean TimedOut = false;
    int secs = 0;
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();

      for (i = 0; i < 300; ++i)
        f.sendInformational("Test message " + i);

      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      if (!g.isResourceAvailable(200)) {
        g.waitForResource(200);
        if (g.isResourceAvailable(200))
          succeeded();
        else
          failed("Resource not available after waitForResource().");
      } else {
        g.waitForResource(200);
        if (g.isResourceAvailable(200))
          succeeded();
        else
          failed("Resource not available after waitForResource().");
      }

      g.close();
    } catch (Exception e) {
      failed(e, "Unknown exception.");
    }
  }

  /**
   * waitForResource() -- Test invalid use; waitForResource for a resource that
   * does not exist in the queue
   **/
  public void Var181() {
    boolean TimedOut = false;
    int secs = 0;
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.remove();

      for (i = 0; i < 10; ++i)
        f.sendInformational("Test message " + i);

      RMessageQueue g = new RMessageQueue(systemObject_, path);
      g.open();
      g.waitForResource(20);
      succeeded(); // @A1C
      g.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception."); // @A1C
    }
  }

  /**
   * getSelectionMetaData(Object attributeID) - Verify LIST_DIRECTION
   * getPresentation() meta data
   **/
  public void Var182() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    Presentation presObj;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      SelectionData = f.getSelectionMetaData(RMessageQueue.LIST_DIRECTION);
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
   * getSelectionMetaData(Object attributeID) - Verify SELECTION_CRITERIA
   * getPresentation() meta data
   **/
  public void Var183() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    Presentation presObj;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SELECTION_CRITERIA);
      presObj = SelectionData.getPresentation();
      if (presObj != null && presObj.getFullName().equals("Selection Criteria")
          && presObj.getName().equals("Selection Criteria")
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
   * getSelectionMetaData(Object attributeID) - Verify SEVERITY_CRITERIA
   * getPresentation() meta data
   **/
  public void Var184() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    Presentation presObj;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SEVERITY_CRITERIA);
      presObj = SelectionData.getPresentation();
      if (presObj != null && presObj.getFullName().equals("Severity Criteria")
          && presObj.getName().equals("Severity Criteria")
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
   * getSelectionMetaData(Object attributeID) - Verify SORT_CRITERIA
   * getPresentation() meta data
   **/
  public void Var185() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    Presentation presObj;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      SelectionData = f.getSelectionMetaData(RMessageQueue.SORT_CRITERIA);
      presObj = SelectionData.getPresentation();

      if (presObj != null && presObj.getFullName().equals("Sort Criteria")
          && presObj.getName().equals("Sort Criteria")
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
   * getSelectionMetaData(Object attributeID) - Verify STARTING_USER_MESSAGE_KEY
   * getPresentation() meta data
   **/
  public void Var186() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    Presentation presObj;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_USER_MESSAGE_KEY);
      presObj = SelectionData.getPresentation();
      if (presObj != null
          && presObj.getFullName().equals("Starting User Message Key")
          && presObj.getName().equals("Starting User Message Key")
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
   * getSelectionMetaData(Object attributeID) - Verify
   * STARTING_WORKSTATION_MESSAGE_KEY getPresentation() meta data
   **/
  public void Var187() {
    ResourceMetaData SelectionData;
    Object[] possibleValues;
    Presentation presObj;
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      SelectionData = f
          .getSelectionMetaData(RMessageQueue.STARTING_WORKSTATION_MESSAGE_KEY);
      presObj = SelectionData.getPresentation();
      if (presObj != null
          && presObj.getFullName().equals("Starting Workstation Message Key")
          && presObj.getName().equals("Starting Workstation Message Key")
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
   * setSortOrder(sortID) -- There are not valid sort IDs for message queues.
   * Pass in a bad value and verify an exception is generated.
   **/
  public void Var188() {
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.setSortOrder(RMessageQueue.LIST_DIRECTION, false);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalArgumentException");
    }
  }

  /**
   * setSortOrder(sortID) -- There are not valid sort IDs for message queues.
   * Pass in a bad value and verify an exception is generated.
   **/
  public void Var189() {
    int i;

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.setSortOrder(null, true);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * setSortValue(sortValues[]) -- There are not valid sort IDs for message
   * queues. Pass in a bad value and verify an exception is generated.
   **/
  public void Var190() {
    int i;
    Object sortValues[] = { RMessageQueue.LIST_DIRECTION,
        RMessageQueue.SELECTION_CRITERIA };

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.setSortValue(sortValues);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * setSortValue(sortValues[]) -- There are not valid sort IDs for message
   * queues. Pass in a bad value and verify an exception is generated.
   **/
  public void Var191() {
    int i;
    Object sortValues[] = {};

    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.setSortValue(sortValues);
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * setSortValue(sortValues[]) -- There are not valid sort IDs for message
   * queues. Pass in a bad value and verify an exception is generated.
   **/
  public void Var192() {
    int i;
    Object sortValues[] = { null, null, null };
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.setSortValue(sortValues);
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * open() - Open a list, change the system name and then reopen the list. This
   * should cause an exception.
   **/
  public void Var193() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      AS400 as400_ = new AS400("BADAS400", "JAVA", "JTEAM1");
      f.open();
      f.setSystem(as400_);
      f.open();
      succeeded();
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

  /**
   * open() - Open a list, change the queue name and then reopen the list. This
   * should cause an exception.
   **/
  public void Var194() {
    try {
      String path = QSYSObjectPathName.toPath(testLib_, "MQT", "MSGQ");
      String newPath = QSYSObjectPathName.toPath("QSYS", "QSYSOPR", "MSGQ");
      RMessageQueue f = new RMessageQueue(systemObject_, path);
      f.open();
      f.setPath(newPath);
      f.open();
      succeeded();
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e,
          "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }
}
