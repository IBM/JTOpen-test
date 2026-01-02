///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  MessageQueueTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Message;

import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.MessageQueue;
import com.ibm.as400.access.QueuedMessage;

import test.JDTestDriver;
import test.MessageSandbox;
import test.MessageTest;
import test.Testcase;

import com.ibm.as400.access.QSYSObjectPathName;

/**
 * Testcase MessageQueueTestcase.
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
public class MessageQueueTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "MessageQueueTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.MessageTest.main(newArgs); 
   }
  static boolean DEBUG = false;

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
        setTestLib(baseTestDriver_.getTestLib());
    }

    sandbox_ = new MessageSandbox(systemObject_, testLib_, "MQT",userId_,output_);
    sandboxReply_ = new MessageSandbox(systemObject_, testLib_, "MQTREPLY",userId_,output_);
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

  /**
   * ctor - Empty ctor should have default properties.
   **/
  @SuppressWarnings("deprecation")
  public void Var001() {
    try {
      MessageQueue f = new MessageQueue();
      if (DEBUG) {
        output_.println("Path:      " + f.getPath());
        output_.println("Selection: " + f.getSelection());
        output_.println("Severity:  " + f.getSeverity());
        output_.println("System:    " + f.getSystem());
      }
      assertCondition(f.getPath().equals(MessageQueue.CURRENT)
          && f.getSelection().equals(MessageQueue.ALL) && f.getSeverity() == 0
          && f.getSystem() == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * ctor - Passing null for system should throw an exception.
   **/
  public void Var002() {
    try {
      MessageQueue f = new MessageQueue(null);
      failed("Did not throw exception." + f);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * ctor - Passing null for system should throw an exception.
   **/
  public void Var003() {
    try {
      MessageQueue f = new MessageQueue(null, "Apath");
      failed("Did not throw exception." + f);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * ctor() - Passing a valid system should set the system.
   **/
  public void Var004() {
    try {
      MessageQueue f = new MessageQueue(systemObject_);
      assertCondition(f.getSystem() == systemObject_);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * ctor() - Passing a valid system should set the system.
   **/
  public void Var005() {
    try {
      String path = QSYSObjectPathName.toPath("MYLIB", "APATH", "MSGQ");
      MessageQueue f = new MessageQueue(systemObject_, path);
      assertCondition(f.getSystem() == systemObject_);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * ctor - Passing null for path should throw an exception.
   **/
  public void Var006() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, null);
      failed("Did not throw exception." + f);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * ctor() - Passing a valid path should set the path.
   **/
  public void Var007() {
    try {
      String path = QSYSObjectPathName.toPath("MYLIB", "APATH", "MSGQ");
      MessageQueue f = new MessageQueue(systemObject_, path);
      assertCondition(f.getPath().equals(path));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getLength() - Should return null when no properties have been set.
   **/
  public void Var008() {
    try {
      MessageQueue f = new MessageQueue();
      f.getLength();
      failed("No exception thrown.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
    }
  }

  /**
   * getLength() - Should return 0 if getMessages has not been called.
   **/
  public void Var009() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      assertCondition(f.getLength() == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getLength() - Should return the number of messages immediately after
   * getMessages has been called (for a short list).
   **/
  public void Var010() {
    try {
      sandbox_.setNumberOfMessages(3);
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      Enumeration<QueuedMessage> messages = f.getMessages();
      assertCondition(f.getLength() == 3, "messages = "+messages);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getLength() - Should return the number of messages immediately after
   * getMessages has been called (for a long list).
   **/
  public void Var011() {
    try {
      sandbox_.setNumberOfMessages(100);
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      Enumeration<QueuedMessage> messages = f.getMessages();
      assertCondition(f.getLength() == 100, "messages = "+messages );
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getLength() - Should return the number of messages after getMessages has
   * been called and the whole list of messages has been read (for a long list).
   **/
  public void Var012() {
    try {
      sandbox_.setNumberOfMessages(96);
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      Enumeration<QueuedMessage> e = f.getMessages();
      while (e.hasMoreElements()) {
         e.nextElement();
      }

      assertCondition(f.getLength() == 96);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getMesages() - Should throw an exception if the system is not set.
   **/
  public void Var013() {
    try {
      MessageQueue f = new MessageQueue();
      f.getMessages();
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
    }
  }

  /**
   * getMessages() - Should throw an exception if the path is not correct.
   **/
  public void Var014() {
    try {
      MessageQueue f = new MessageQueue(systemObject_,
          QSYSObjectPathName.toPath("MYLIB", "MYDOGSMSGQ", "MSGQ"));
      f.getMessages();
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
    }
  }

  /**
   * getMessages() - Should return an empty enumeration when the message list is
   * empty.
   **/
  public void Var015() {
    try {
      sandbox_.setNumberOfMessages(0);
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      Enumeration<QueuedMessage> e = f.getMessages();
      while ( e.hasMoreElements()) {
         e.nextElement();
      }

      assertCondition(e.hasMoreElements() == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getMessages() - Should return the whole list of messages (for a short
   * list).
   **/
  public void Var016() {
    try {
      sandbox_.setNumberOfMessages(3);
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      Enumeration<QueuedMessage> e = f.getMessages();
      int count;
      for (count = 0; e.hasMoreElements(); ++count) {
         e.nextElement();
      }

      assertCondition(count == 3);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getMessages() - Not setting a path should return the default queue (no
   * exception is expected).
   **/
  public void Var017() {
    try {
      MessageQueue f = new MessageQueue(systemObject_);
      Enumeration<QueuedMessage> e = f.getMessages();

      assertCondition(true, "Got messages "+e); 
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getMessages() - Should return the two whole lists when two different lists
   * are processed at the same time.
   **/
  public void Var018() {
    final int count1 = 107;
    final int count2 = 208;
    try {
      sandbox_.setNumberOfMessages(count1);
      sandboxReply_.setNumberOfMessages(count2);

      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      MessageQueue fReply = new MessageQueue(systemObject_, sandboxReply_
          .getQueue().getPath());

      Enumeration<QueuedMessage> e1 = f.getMessages();
      Enumeration<QueuedMessage> e2 = fReply.getMessages();
      boolean done1 = false;
      boolean done2 = false;
      int counter1 = 0;
      int counter2 = 0;
      QueuedMessage message=null; 
      while (done1 == false || done2 == false) {
        if (e1.hasMoreElements() == false) {
          done1 = true;
        } else {
          message = (QueuedMessage) e1.nextElement();
          counter1++;
        }

        if (e2.hasMoreElements() == false) {
          done2 = true;
        } else {
          message = (QueuedMessage) e2.nextElement();
          counter2++;
        }
      }

      if (DEBUG) {
        output_.println("counter1: " + counter1 + " count1: " + count1);
        output_.println("counter2: " + counter2 + " count2: " + count2);
      }

      assertCondition(counter1 == count1 && counter2 == count2, "lastMessage="+message);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getPath() - Should return "*CURRENT" if the path has not been set.
   **/
  public void Var019() {
    try {
      MessageQueue f = new MessageQueue();
      assertCondition(f.getPath().equals("*CURRENT"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getPath() - Should return the path if it has been set.
   **/
  public void Var020() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      String path = QSYSObjectPathName.toPath("MYLIB", "YOURMSGQ", "MSGQ");
      f.setPath(path);
      assertCondition(f.getPath().equals(path));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getSelection() - Should return MessageQueue.ALL if the selection has not
   * been set.
   **/
  @SuppressWarnings("deprecation")
  public void Var021() {
    try {
      MessageQueue f = new MessageQueue();
      assertCondition(f.getSelection().equals(MessageQueue.ALL));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getSelection() - Should return the selection if it has been set.
   **/
  @SuppressWarnings("deprecation")
  public void Var022() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      String selection = MessageQueue.MESSAGES_NEED_REPLY;
      f.setSelection(selection);
      assertCondition(f.getSelection().equals(selection));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getSeverity() - Should return 0 if the severity has not been set.
   **/
  public void Var023() {
    try {
      MessageQueue f = new MessageQueue();
      assertCondition(f.getSeverity() == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getSeverity() - Should return the severity if it has been set.
   **/
  public void Var024() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      int severity = 82;
      f.setSeverity(severity);
      assertCondition(f.getSeverity() == severity);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getSystem() - Should return null if the system has not been set.
   **/
  public void Var025() {
    try {
      MessageQueue f = new MessageQueue();
      assertCondition(f.getSystem() == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * getSystem() - Should return the system if it has been set.
   **/
  public void Var026() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      assertCondition(f.getSystem() == systemObject_);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * receive() - Not setting a system should throw an invalid state exception.
   **/
  public void Var027() {
    try {
      MessageQueue f = new MessageQueue();
      byte[] key = new byte[4];
      f.receive(key);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
    }
  }

  /**
   * receive() - Receiving a nonexistant message should throw an exception.
   **/
  public void Var028() {
    try {
      if (DEBUG) {
        output_.println("Queue: " + sandbox_.getQueue().getPath());
      }

      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();
      byte[] key = new byte[4];
      f.receive(key);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
    }
  }

  /**
   * receive() - Receiving a message when specifying action *SAME should work
   * successfully more than once.
   **/
  public void Var029() {
    try {
      sandbox_.setNumberOfMessages(1);
      if (DEBUG) {
        output_.println("Queue: " + sandbox_.getQueue().getPath());
      }

      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      Enumeration<QueuedMessage> e = f.getMessages();

      byte[] key = null;

      while ( e.hasMoreElements()) {
        QueuedMessage message = (QueuedMessage) e.nextElement();
        key = message.getKey();

        message = f.receive(key, 0, MessageQueue.SAME, MessageQueue.ANY);
        message = f.receive(key, 0, MessageQueue.SAME, MessageQueue.ANY);
      }

      succeeded();
    } catch (Exception e) {
      failed("Unexpected exception.");
    }
  }

  /**
   * receive() - When the message queue is cleared and a message is sent to it
   * the message can be received successfully and is removed from the message
   * queue.
   **/
  public void Var030() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();

      f.sendInformational("This is test message 1.");
      f.sendInformational("CAE0009", "/QSYS.LIB/QCPFMSG.MSGF");
      f.sendInformational("This is test message 3.");

      Enumeration<QueuedMessage> e = f.getMessages();
      for (int count = 0; e.hasMoreElements(); ++count) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        if (DEBUG) {
          output_.print("count: " + count);
          output_.print(" key:  " + message.getKey());
          output_.println(" -> " + message.getText());
        }

        QueuedMessage testMessage = f.receive(message.getKey());

        if (DEBUG) {
          String messageText = message.getText();
          String testMessageText = testMessage.getText();
          output_.println("key:  " + testMessage.getKey() + " -> "
              + message.getKey());
          output_.println("text: " + testMessageText + " -> " + messageText);
          output_.println("length: " + testMessageText.length() + " -> "
              + messageText.length());
          output_.println("help: " + testMessage.getHelp() + " -> "
              + message.getHelp());
        }

        String t1 = message.getText();
        String t2 = testMessage.getText();
        if (!t1.equals(t2)) {
          failed("Message text not equal: '" + t1 + "' != '" + t2 + "'");
          return;
        }
      }

      Enumeration<QueuedMessage> e2 = f.getMessages();

      if (DEBUG) {
        output_.println("List now contains:");
        while (e2.hasMoreElements()) {
          QueuedMessage message = (QueuedMessage) e2.nextElement();

          output_.print("key:  " + message.getKey());
          output_.println(" -> " + message.getText());
        }
        output_.println("End -- List now contains:");
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * remove() - When the message queue is cleared there are no messages left in
   * it.
   **/
  public void Var031() {
    final int messageCount = 10;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      // String messageList[] = new String[messageCount];

      sandbox_.setNumberOfMessages(messageCount);

      f.remove();
      Enumeration<QueuedMessage> e = f.getMessages();
      assertCondition(f.getLength() == 0, "Messages = "+e);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * remove() - When a particular message is cleared from the queue, it is no
   * longer there.
   **/
  public void Var032() {
    final int messageCount = 10;

    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      f.remove(); // Remove old messages.

      String messageList[] = new String[messageCount];
      for (int count = 0; count < messageCount; ++count) {
        messageList[count] = new String("This is the message I sent as number "
            + count);
        f.sendInformational(messageList[count]);
      }

      Enumeration<QueuedMessage> e = f.getMessages();

      byte[] removeKey = null;
      String removeText = null;

      for (int count = 0; e.hasMoreElements(); ++count) {
        QueuedMessage message = (QueuedMessage) e.nextElement();
        if (count == 4) {
          removeKey = message.getKey();
          removeText = message.getText();
        }

        if (DEBUG) {
          output_.print("Key: " + message.getKey());
          output_.println(" -> " + message.getText());
        }
      }

      if (DEBUG) {
        output_.println("-- end of list 1 --");
        output_.println("");
        output_.println("Remove: Key: " + removeKey + " -> " + removeText);
      }

      f.remove(removeKey);

      Enumeration<QueuedMessage> e2 = f.getMessages();

      if (DEBUG) {
        while (e2.hasMoreElements()) {
          QueuedMessage message = (QueuedMessage) e2.nextElement();
          output_.print("Key: " + message.getKey());
          output_.println(" -> " + message.getText());
        }
        output_.println("-- end of list 2 --");
      }

      if (f.getLength() != messageCount - 1) {
        if (DEBUG) {
          output_.println("Improper length returned after remove");
        }
        failed("Wrong length: " + f.getLength() + " != " + (messageCount - 1));
        return;
      }

      int extra = 0;
      for (int counter = 0; e.hasMoreElements(); ++counter) {
        QueuedMessage message = (QueuedMessage) e.nextElement();
        if (counter == 5) {
          extra = 1;

          if (DEBUG) {
            output_.println("extra: " + extra);
          }
        }

        // Here we need to calculate the actual message element to compare based
        // on the index.
        if (DEBUG) {
          output_.print(message.getText());
          output_.println("  ->  " + messageList[counter + extra]
              + " last chance");
        }

        String t1 = message.getText();
        String t2 = messageList[counter + extra];
        if (!t1.equals(t2)) {
          failed("Message text not equal: '" + t1 + "' != '" + t2 + "'");
          return;
        }
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * remove() - When a particular message type is cleared from the queue, that
   * message is no longer there.
   **/
  public void Var033() {
    final int messageCount = 10;

    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      MessageQueue fReply = new MessageQueue(systemObject_, sandboxReply_
          .getQueue().getPath());

      String messageList[] = new String[messageCount];

      f.remove(); // Remove old messages.

      int tracker = 0;
      for (int count = 0; count < messageCount; ++count) {
        String tempString = new String("This is the message I sent as number "
            + count);
        if (count % 2 == 0) {
          f.sendInformational("Informational " + tempString);
        } else {
          messageList[tracker] = "Inquiry       " + tempString;
          f.sendInquiry(messageList[tracker], sandboxReply_.getQueue()
              .getPath());
          tracker++;
        }
      }

      Enumeration<QueuedMessage> e = f.getMessages();

      if (DEBUG) {
        while (e.hasMoreElements()) {
          QueuedMessage message = (QueuedMessage) e.nextElement();
          output_.print("Key: " + message.getKey());
          output_.println(" -> " + message.getText());
        }
        output_.println("");
      }

      f.remove(MessageQueue.KEEP_UNANSWERED);

      Enumeration<QueuedMessage> e2 = f.getMessages();

      for (int count = tracker - 1; e2.hasMoreElements(); --count) {
        QueuedMessage message = (QueuedMessage) e2.nextElement();
        if (DEBUG) {
          output_.print(message.getText());
          output_.println(" -> " + messageList[(tracker - 1) - count]);
        }

        String t1 = message.getText();
        String t2 = messageList[tracker - 1 - count];
        if (!t1.equals(t2)) {
          failed("Message text not equal: '" + t1 + "' != '" + t2 + "'"+fReply);
          return;
        }
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * remove() - Verify that passing null for type causes an exception.
   **/
  public void Var034() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove((byte[]) null);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * remove() - Verify that passing null for type causes an exception.
   **/
  public void Var035() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove((String) null);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * reply() - Verify that passing null for message causes an exception.
   **/
  public void Var036() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      byte[] key = new byte[4];
      f.reply(key, null);
      failed("Did not throw exception.");
    } catch (Exception e) {
      if (exceptionIs(e, "NullPointerException")) {
        try {
          MessageQueue f2 = new MessageQueue(systemObject_, sandbox_.getQueue()
              .getPath());
          f2.reply(null, "I");
          failed("Did not throw exception 2.");
        } catch (Exception e2) {
          assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
      } else {
        failed(e);
      }
    }
  }

  /**
   * reply() - Replying to a nonexistant message should throw an exception.
   **/
  public void Var037() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();
      byte[] key = new byte[4];
      f.reply(key, "Data in the string");
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
    }
  }

  /**
   * reply() - When a particular message is replied to, it is cleared from the
   * queue and is no longer there. A second reply to the original message throws
   * an exception.
   **/
  public void Var038() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      MessageQueue fReply = new MessageQueue(systemObject_, sandboxReply_
          .getQueue().getPath());

      f.remove(); // Remove old messages.
      fReply.remove(); // Remove old messages.

      f.sendInquiry("This is the message.", sandboxReply_.getQueue().getPath());

      if (DEBUG) {
        Enumeration<QueuedMessage> e = f.getMessages();
        while (e.hasMoreElements()) {
          QueuedMessage message = (QueuedMessage) e.nextElement();

          output_.print("Key: " + message.getKey());
          output_.println(" -> " + message.getText());
        }
        output_.println("");
      }

      Enumeration<QueuedMessage> e = f.getMessages();
      while (e.hasMoreElements()) {
        QueuedMessage message2 = (QueuedMessage) e.nextElement();

        f.reply(message2.getKey(), "This is the reply.");
        f.reply(message2.getKey(), "This is the reply."); // Should exception
                                                          // here.
      }

      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
    }
  }

  /**
   * reply() - When a particular message is replied to, it is cleared from the
   * queue and is no longer there. As a side effect of reply, the messages are
   * located in the reply queue.
   **/
  public void Var039() {
    final int messageCount = 10;
    boolean succeeded = true;

    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      MessageQueue fReply = new MessageQueue(systemObject_, sandboxReply_
          .getQueue().getPath());

      String messageList[] = new String[messageCount];
      String replyList[] = new String[messageCount];

      if (DEBUG)
        output_.println("f.remove()");

      f.remove(); // Remove old messages.
      if (DEBUG)
        output_.println("fReply.remove()");

      fReply.remove(); // Remove old messages.

      int tracker = 0;
      int replyTrack = 0;
      for (int count = 0; count < messageCount; ++count) {
        String tempString = new String("This is the message I sent as number "
            + count);
        if (count % 2 == 0) {
          messageList[tracker] = "Informational " + tempString;
          if (DEBUG) {
            output_.println("message" + tracker + ": "
                + messageList[tracker]);
            output_.println("f.sendInformational(" + messageList[tracker]
                + ");");
          }

          f.sendInformational(messageList[tracker]);
          tracker++;
        } else {
          if (DEBUG) {
            output_.println("queuepath: <"
                + sandboxReply_.getQueue().getPath() + ">");
            output_.println("f.sendInquiry(Inquiry " + tempString + ");");
          }
          f.sendInquiry("Inquiry       " + tempString, sandboxReply_.getQueue()
              .getPath());
        }
      }

      Enumeration<QueuedMessage> e = f.getMessages();
      while ( e.hasMoreElements()) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        if (DEBUG) {
          output_.print("Key: " + message.getKey());
          output_.println(" -> " + message.getText());
        }

        String replyText = "This is a reply message #";
        String messageText = message.getText();
        if (messageText.startsWith("Inquiry")) {
          replyList[replyTrack++] = messageText;
          replyList[replyTrack] = replyText + messageText;

          if (DEBUG) {
            output_.println("(breaks here?) count:  " + replyTrack);
            output_.println("Reply: key: " + message.getKey() + " "
                + replyList[replyTrack]);
          }

          f.reply(message.getKey(), replyList[replyTrack]);
          replyTrack++;
        }
      }
      if (DEBUG) {
        output_.println("");
      }

      Enumeration<QueuedMessage> e2 = f.getMessages();
      for (int count = tracker - 1; e2.hasMoreElements(); count--) {
        QueuedMessage message = (QueuedMessage) e2.nextElement();

        if (DEBUG) {
          output_.print(message.getText());
          output_.println(" -> " + messageList[(tracker - 1) - count]);
        }

        if (!message.getText().equals(messageList[(tracker - 1) - count])) {
          succeeded = false;
          if (DEBUG == false) {
            break;
          }
        }
      }

      if (DEBUG) {
        output_.println("");
        output_.println("Following are reply queue messages");
        output_.println("");
      }

      Enumeration<QueuedMessage> eReply = fReply.getMessages();
      for (int count = 0; eReply.hasMoreElements(); ++count) {
        QueuedMessage message = (QueuedMessage) eReply.nextElement();
        if (DEBUG) {
          output_.println("Last check...");
          output_.println(message.getText() + " -> " + replyList[count]);
          output_.println("");
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
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * sendInformational() - When the message queue is cleared and a message is
   * sent to it, the message arrives successfully.
   **/
  public void Var040() {
    int count = 0;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();
      f.sendInformational("message text");

      Enumeration<QueuedMessage> e = f.getMessages();
      while (e.hasMoreElements()) {
        e.nextElement();
        count++;
      }

      if (DEBUG) {
        output_.println("length:   (1) " + f.getLength());
      }

      assertCondition(f.getLength() == 1 && count == 1);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * sendInformational() - When the message queue is cleared and a message is
   * sent to it, the message text is retrieved successfully.
   **/
  public void Var041() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      String message = "This is the message I sent";
      f.remove();
      f.sendInformational(message);

      QueuedMessage TheMessage = null;
      Enumeration<QueuedMessage> e = f.getMessages();
      while (e.hasMoreElements()) {
        TheMessage = (QueuedMessage) e.nextElement();
      }

      assertCondition(TheMessage.getText().equals(message));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * sendInformational() - When the message queue is cleared and multiple
   * messages are sent to it, the message text of each is retrieved
   * successfully.
   **/
  public void Var042() {
    final int messageCount = 10;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      String messageList[] = new String[messageCount];
      for (int count = 0; count < messageCount; ++count) {
        messageList[count] = new String("This is the message I sent as number "
            + count);
      }
      f.remove();
      for (int count = 0; count < messageList.length; ++count) {
        if (DEBUG) {
          output_.println(messageList[count]);
        }

        f.sendInformational(messageList[count]);
      }

      Enumeration<QueuedMessage> e = f.getMessages();
      boolean succeeded = true;
      for (int counter = messageCount - 1; counter >= 0; --counter) {
        QueuedMessage message = (QueuedMessage) e.nextElement();
        if (DEBUG) {
          output_.println(message.getText() + " --> "
              + messageList[(messageCount - 1) - counter]);
        }

        if (!message.getText()
            .equals(messageList[(messageCount - 1) - counter])) {
          succeeded = false;
          break;
        }
      }
      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * sendInformational() - When the message queue is cleared and a message is
   * sent to it from an AS/400 file, the message arrives successfully. Help text
   * is also set for the message (retrieved via receive).
   **/
  public void Var043() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();
      f.sendInformational("CAE0002", "/QSYS.LIB/QCPFMSG.MSGF");
      f.sendInformational("CAE0005", "/QSYS.LIB/QCPFMSG.MSGF");
      f.sendInformational("CAE0009", "/QSYS.LIB/QCPFMSG.MSGF");

      Enumeration<QueuedMessage> e = f.getMessages();

      if (DEBUG) {
        output_.println("length   (3): " + f.getLength());
      }

      while ( e.hasMoreElements()) {
        QueuedMessage message = (QueuedMessage) e.nextElement();
        if (DEBUG) {
          output_.println("text: " + message.getText());
        }

        QueuedMessage rMessage = f.receive(message.getKey());
        if (DEBUG) {
          output_.println("help: " + rMessage.getHelp());
        }
      }
      int len = f.getLength();
      if (len == 0) {
        succeeded();
      } else {
        failed("Wrong length: " + len + " != 0");
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * sendInformational() - When a message is sent to the message queue with
   * substitution text, the message is delivered successfully.
   **/
  public void Var044() {
    boolean succeeded = true;
    final int messageCount = 10;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();

      String[] substitutionList = new String[messageCount];
      substitutionList[0] = new String("zero");
      substitutionList[1] = new String("one");
      substitutionList[2] = new String("two");
      substitutionList[3] = new String("three");
      substitutionList[4] = new String("four");
      substitutionList[5] = new String("five");
      substitutionList[6] = new String("six");
      substitutionList[7] = new String("seven");
      substitutionList[8] = new String("eight");
      substitutionList[9] = new String("nine");

      for (int count = 0; count < substitutionList.length; ++count) {
        AS400Text converter = new AS400Text(10, systemObject_.getCcsid(),
            systemObject_);
        f.sendInformational("CAE0014", "/QSYS.LIB/QCPFMSG.MSGF",
            converter.toBytes(substitutionList[count]));
      }

      Enumeration<QueuedMessage> e = f.getMessages();
      for (int counter = messageCount - 1; e.hasMoreElements(); --counter) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        if (message.getText().indexOf(
            substitutionList[(messageCount - 1) - counter]) < 0) {
          succeeded = false;
          break;
        }
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * sendInformational() - When a message is sent to the message queue with
   * multiple substitution text strings, the message is delivered successfully.
   **/
  public void Var045() {
    boolean succeeded = true;
    final int messageCount = 10;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();

      String[] substitutionList = new String[messageCount];
      substitutionList[0] = new String("zero    hot dog ");
      substitutionList[1] = new String("one     chili   ");
      substitutionList[2] = new String("two     catsup  ");
      substitutionList[3] = new String("three   tomato  ");
      substitutionList[4] = new String("four    soup    ");
      substitutionList[5] = new String("five    magic   ");
      substitutionList[6] = new String("six     money   ");
      substitutionList[7] = new String("seven   gigantic");
      substitutionList[8] = new String("eight   mondo   ");
      substitutionList[9] = new String("nine    dinosaur");

      for (int count = 0; count < substitutionList.length; ++count) {
        AS400Text converter = new AS400Text(16, systemObject_.getCcsid(),
            systemObject_);
        f.sendInformational("CAE0005", "/QSYS.LIB/QCPFMSG.MSGF",
            converter.toBytes(substitutionList[count]));
      }

      Enumeration<QueuedMessage> e = f.getMessages();
      for (int counter = messageCount - 1; e.hasMoreElements(); --counter) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        String theMessage = message.getText();

        if (DEBUG) {
          output_.println("message: <" + theMessage + ">");
        }

        if (theMessage.indexOf(substitutionList[(messageCount - 1) - counter]
            .substring(0, 7).trim()) < 0
            || theMessage
                .indexOf(substitutionList[(messageCount - 1) - counter]
                    .substring(8).trim()) < 0) {
          succeeded = false;
          break;
        }
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * sendInformational() - Verify that passing null for msg causes an exception.
   **/
  public void Var046() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.sendInformational(null);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInformational() - Verify that passing null for id causes an exception.
   **/
  public void Var047() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.sendInformational(null, "message");
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInformational() - Verify that passing null for file causes an
   * exception.
   **/
  public void Var048() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.sendInformational("id", null);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInformational() - Verify that passing null for id causes an exception.
   **/
  public void Var049() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      byte[] temp = new byte[20];
      f.sendInformational(null, "file", temp);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInformational() - Verify that passing null for file causes an
   * exception.
   **/
  public void Var050() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      byte[] temp = new byte[20];
      f.sendInformational("id", null, temp);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInformational() - Verify that passing null for msg causes an exception.
   **/
  public void Var051() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.sendInformational("CAE0009", "/QSYS.LIB/QCPFMSG.MSGF", null);
      succeeded();
    } catch (Exception e) {
      failed(e, "Unknown exception");
    }
  }

  /**
   * sendInquiry() - Verify that passing null for msg causes an exception.
   **/
  public void Var052() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.sendInquiry(null, "replypath");
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInquiry() - Verify that passing null for replypath causes an exception.
   **/
  public void Var053() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.sendInquiry("msg", null);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInquiry() - Verify that passing null for id causes an exception.
   **/
  public void Var054() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.sendInquiry(null, "file", "replypath");
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInquiry() - Verify that passing null for file causes an exception.
   **/
  public void Var055() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.sendInquiry("id", null, "replypath");
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInquiry() - Verify that passing null for replypath causes an exception.
   **/
  public void Var056() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.sendInquiry("id", "file", null);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInquiry() - Verify that passing null for id causes an exception.
   **/
  public void Var057() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      byte[] temp = new byte[20];
      f.sendInquiry(null, "file", temp, "replypath");
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInquiry() - Verify that passing null for file causes an exception.
   **/
  public void Var058() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      byte[] temp = new byte[20];
      f.sendInquiry("id", null, temp, "replypath");
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInquiry() - Verify that passing null for substitutionData causes an
   * exception.
   **/
  public void Var059() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.sendInquiry("CAE0014", "/QSYS.LIB/QCPFMSG.MSGF", null, sandboxReply_
          .getQueue().getPath());
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * sendInquiry() - Verify that passing null for replypath causes an exception.
   **/
  public void Var060() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      byte[] temp = new byte[20];
      f.sendInquiry("id", "file", temp, null);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * sendInquiry() - When a message is sent to the message queue with
   * substitution text, the message is delivered successfully.
   **/
  public void Var061() {
    boolean succeeded = true;
    final int messageCount = 10;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();

      String[] substitutionList = new String[messageCount];
      substitutionList[0] = new String("zero");
      substitutionList[1] = new String("one");
      substitutionList[2] = new String("two");
      substitutionList[3] = new String("three");
      substitutionList[4] = new String("four");
      substitutionList[5] = new String("five");
      substitutionList[6] = new String("six");
      substitutionList[7] = new String("seven");
      substitutionList[8] = new String("eight");
      substitutionList[9] = new String("nine");

      for (int count = 0; count < substitutionList.length; ++count) {
        AS400Text converter = new AS400Text(10, systemObject_.getCcsid(),
            systemObject_);
        f.sendInquiry("CAE0014", "/QSYS.LIB/QCPFMSG.MSGF", converter
            .toBytes(substitutionList[count]), sandboxReply_.getQueue()
            .getPath());
      }

      Enumeration<QueuedMessage> e = f.getMessages();
      for (int counter = messageCount - 1; e.hasMoreElements(); --counter) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        if (DEBUG) {
          AS400Text converter = new AS400Text(10, systemObject_.getCcsid(),
              systemObject_);
          output_.println(message.getText() + " sub: "
              + converter.toBytes(substitutionList[counter]));
        }

        if (message.getText().indexOf(
            substitutionList[(messageCount - 1) - counter]) < 0) {
          succeeded = false;
          break;
        }
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * sendInquiry() - When a message is sent to the message queue with multiple
   * substitution text strings, the message is delivered successfully.
   **/
  public void Var062() {
    boolean succeeded = true;
    final int messageCount = 10;
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();

      String[] substitutionList = new String[messageCount];
      substitutionList[0] = new String("zero    hot dog ");
      substitutionList[1] = new String("one     chili   ");
      substitutionList[2] = new String("two     catsup  ");
      substitutionList[3] = new String("three   tomato  ");
      substitutionList[4] = new String("four    soup    ");
      substitutionList[5] = new String("five    magic   ");
      substitutionList[6] = new String("six     money   ");
      substitutionList[7] = new String("seven   gigantic");
      substitutionList[8] = new String("eight   mondo   ");
      substitutionList[9] = new String("nine    dinosaur");

      for (int count = 0; count < substitutionList.length; ++count) {
        AS400Text converter = new AS400Text(16, systemObject_.getCcsid(),
            systemObject_);
        f.sendInquiry("CAE0005", "/QSYS.LIB/QCPFMSG.MSGF", converter
            .toBytes(substitutionList[count]), sandboxReply_.getQueue()
            .getPath());
      }

      Enumeration<QueuedMessage> e = f.getMessages();
      for (int counter = messageCount - 1; e.hasMoreElements(); --counter) {
        QueuedMessage message = (QueuedMessage) e.nextElement();

        String theMessage = message.getText();

        if (DEBUG) {
          output_.println("message: <" + theMessage + ">");
        }

        if (theMessage.indexOf(substitutionList[(messageCount - 1) - counter]
            .substring(0, 7).trim()) < 0
            || theMessage
                .indexOf(substitutionList[(messageCount - 1) - counter]
                    .substring(8).trim()) < 0) {
          succeeded = false;
          break;
        }
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * setPath() - Verify that passing null causes an exception.
   **/
  public void Var063() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.setPath(null);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * setPath() - When the path was not previously set, this should set the path.
   **/
  public void Var064() {
    try {
      MessageQueue f = new MessageQueue(systemObject_);
      String path = QSYSObjectPathName.toPath("HISLIB", "HERMSGQ", "MSGQ");
      f.setPath(path);
      assertCondition(f.getPath().equals(path));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * setPath() - When the path was previously set, this should set the path to a
   * new value.
   **/
  public void Var065() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      String path = QSYSObjectPathName.toPath("HERLIB", "HISMSGQ", "MSGQ");
      f.setPath(path);
      assertCondition(f.getPath().equals(path));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * setSelection() - Verify that passing null causes an exception.
   **/
  @SuppressWarnings("deprecation")
  public void Var066() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.setSelection(null);
      failed("No exception was thrown");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * setSelection() - When the selection was not previously set, this should set
   * the selection.
   **/
  @SuppressWarnings("deprecation")
  public void Var067() {
    try {
      MessageQueue f = new MessageQueue();
      String selection = MessageQueue.MESSAGES_NEED_REPLY;
      f.setSelection(selection);
      assertCondition(f.getSelection().equals(selection));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * setSelection() - When the selection was previously set, this should set the
   * selection to a new value.
   **/
  @SuppressWarnings("deprecation")
  public void Var068() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.setSelection(MessageQueue.SENDERS_COPY_NEED_REPLY);
      String selection = MessageQueue.MESSAGES_NEED_REPLY;
      f.setSelection(selection);
      assertCondition(f.getSelection().equals(selection));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * setSeverity() - When the severity was not previously set, this should set
   * the severity.
   **/
  public void Var069() {
    try {
      MessageQueue f = new MessageQueue();
      int severity = 83;
      f.setSeverity(severity);
      assertCondition(f.getSeverity() == severity);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * setSeverity() - When the selection was previously set, this should set the
   * selection to a new value.
   **/
  public void Var070() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.setSeverity(46);
      int severity = 83;
      f.setSeverity(severity);
      assertCondition(f.getSeverity() == severity);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * setSelection() - When an invalid selection is set, this should throw an
   * exception.
   **/
  @SuppressWarnings("deprecation")
  public void Var071() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.setSelection(MessageQueue.KEEP_UNANSWERED);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * setSelection() - When an invalid selection is set, this should throw an
   * exception.
   **/
  @SuppressWarnings("deprecation")
  public void Var072() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.setSelection("Hockey Puck");
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * setSeverity() - When the value specified is out of range an exception
   * should be thrown.
   **/
  public void Var073() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.setSeverity(46);
      int severity = -1;
      f.setSeverity(severity);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * setSeverity() - When the value specified is out of range an exception
   * should be thrown.
   **/
  public void Var074() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.setSeverity(46);
      int severity = 100;
      f.setSeverity(severity);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
    }
  }

  /**
   * setSystem() - Verify that passing null causes an exception.
   **/
  public void Var075() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.setSystem(null);
      failed("Did not throw exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * setSystem() - When the system was not previously set, this should set the
   * system.
   **/
  public void Var076() {
    try {
      MessageQueue f = new MessageQueue();
      AS400 system2 = new AS400();
      f.setSystem(system2);
      assertCondition(f.getSystem() == system2);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * setSystem() - When the system was previously set, this should set the
   * system to a new value.
   **/
  public void Var077() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      AS400 system2 = new AS400();
      f.setSystem(system2);
      assertCondition(f.getSystem() == system2);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Verify QueuedMessage() values are available for getFromJob, etc.
   **/
  public void Var078() {
    StringBuffer sb = new StringBuffer(); 
    try {
      sandbox_.setNumberOfMessages(3);
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());

      boolean succeeded = true;
      Enumeration<QueuedMessage> e = f.getMessages();
      for (int count = 0; e.hasMoreElements(); ++count) {
        QueuedMessage m = (QueuedMessage) e.nextElement();
        if (m.getFromJobName() != null) {
          if (!m.getFromJobName().trim().equals("QZRCSRVS")) {
            // If running on-thread, then tolerate a different job name.
            if (!MessageTest.runningOnThread_)
              sb.append("FAILED job name "+m.getFromJobName().trim()+" is not QZRCSRVS\n");
              succeeded = false;
          }
          
          sb.append("Message " + String.valueOf(count) + ":\n");
          sb.append(" From job name: " + m.getFromJobName()+"\n");
          
        }
        if (m.getFromJobNumber() != null) {
          String aNumber = m.getFromJobNumber().trim();
          try {
            for (int index = 0; index < aNumber.length(); index++) {
              if (!(java.lang.Character.isDigit(aNumber.charAt(index)))) {
                succeeded = false;
                sb.append("FAILED: no digit in job number "+aNumber+"\n"); 
              }
            }
          } catch (Exception xcp) {
            sb.append("FAILED: Got Exception\n"); 
            printStackTraceToStringBuffer(xcp, sb); 
            succeeded = false; 
            
          }
          
            sb.append("Message " + String.valueOf(count) + ":\n");
            sb.append("  From job number: " + m.getFromJobNumber()+"\n");
          
        }
        if (m.getKey() == null) {
          succeeded = false;
       
          sb.append("FAILED Message " + String.valueOf(count) + ":\n");
          sb.append("  Key is null: \n");
          
        }
        if (m.getReplyStatus() != null) {
          if (!m.getReplyStatus().trim().equals("N")) {
            sb.append("FAILED getReplyStatus is not N\n"  );
            succeeded = false;
          }
          
          sb.append("  Reply status: " + m.getReplyStatus()+"\n");
         
        }
        if (m.getUser() != null) {
          String expectedUser = "QUSER"; 
          if (getRelease() > JDTestDriver.RELEASE_V7R5M0) 
            expectedUser="QUSER_NC"; 
          if (!m.getUser().trim().equals(expectedUser)) {
            // If running on-thread, then tolerate a different job name.
            if (!MessageTest.runningOnThread_) {
              succeeded = false;
              sb.append("FAILED User="+m.getUser()+" sb "+expectedUser+"\n"); 
            }
          }
          sb.append("Message " + String.valueOf(count) + ":\n");
          sb.append("  User: " + m.getUser()+"\n");
        }
      }
      assertCondition(succeeded == true, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.\n" + sb.toString());
    }
  }

  /**
   * Verify QueuedMessage() values are available for getHelp, etc.
   **/
  public void Var079() {
    try {
      MessageQueue f = new MessageQueue(systemObject_, sandbox_.getQueue()
          .getPath());
      f.remove();
      f.sendInformational("CAE0002", "/QSYS.LIB/QCPFMSG.MSGF");
      f.sendInformational("CAE0005", "/QSYS.LIB/QCPFMSG.MSGF");
      f.sendInformational("CAE0009", "/QSYS.LIB/QCPFMSG.MSGF");

      boolean succeeded = true;
      Enumeration<QueuedMessage> e = f.getMessages();

      if (DEBUG) {
        output_.println("length   (3): " + f.getLength());
      }
      for (int count = 0; count < f.getLength(); count++) {
        QueuedMessage message = (QueuedMessage) e.nextElement();
        if (DEBUG) {
          output_.println("list text: " + message.getText());
          output_.println("list key:  " + message.getKey());
        }

        QueuedMessage rMessage = f.receive(message.getKey());

        if (DEBUG) {
          output_.println("key: " + rMessage.getKey());
        }
        if (rMessage.getKey() == null) {
          output_.println("getKey returns null");
          succeeded = false;
        }

        if (DEBUG) {
          output_.println("text: " + rMessage.getText());
        }
        if (rMessage.getText() == null) {
          output_.println("getText returns null");
          succeeded = false;
        }

        if (DEBUG) {
          output_.println("help: " + rMessage.getHelp());
        }
        if (rMessage.getHelp() == null) {
          output_.println("getHelp returns null");
          succeeded = false;
        }

        if (DEBUG) {
          output_.println("default reply: " + rMessage.getDefaultReply());
        }
        // if (rMessage.getDefaultReply() == null)
        // {
        // output_.println("getDefaultReply returns null");
        // succeeded = false;
        // }

        if (DEBUG) {
          output_.println("date: " + rMessage.getDate());
        }
        if (rMessage.getDate() == null) {
          output_.println("getDate returns null");
          succeeded = false;
        }

        if (DEBUG) {
          output_.println("filename: " + rMessage.getFileName());
        }
        if (rMessage.getFileName() == null) {
          output_.println("getFileName returns null");
          succeeded = false;
        }

        if (DEBUG) {
          output_.println("ID: " + rMessage.getID());
        }
        if (rMessage.getID() == null) {
          output_.println("getID returns null");
          succeeded = false;
        }

        if (DEBUG) {
          output_.println("library: " + rMessage.getLibraryName());
        }
        if (rMessage.getLibraryName() == null) {
          output_.println("getLibraryName returns null");
          succeeded = false;
        }

        if (DEBUG) {
          output_.println("path: " + rMessage.getPath());
        }
        if (rMessage.getPath() == null) {
          output_.println("getPath returns null");
          succeeded = false;
        }

        if (DEBUG) {
          output_.println("severity: " + rMessage.getSeverity());
        }
        if ((rMessage.getSeverity() < 0) || (rMessage.getSeverity() > 99)) {
          output_.println("getSeverity returns: " + rMessage.getSeverity());
          succeeded = false;
        }

        if (DEBUG) {
          output_.println("substitution data: "
              + rMessage.getSubstitutionData());
        }
        if (rMessage.getSubstitutionData().length != 0) {
          output_.println("getSubstitution returns non-0 length array");
          succeeded = false;
        }

        if (DEBUG) {
          output_.println("type: " + rMessage.getType());
        }
        if (rMessage.getType() < 1 || rMessage.getType() > 25) {
          output_.println("getType returns: " + rMessage.getType());
          succeeded = false;
        }
      }

      assertCondition(succeeded == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }
}
