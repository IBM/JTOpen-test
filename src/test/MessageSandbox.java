///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  MessageSandbox.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.MessageQueue;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.QueuedMessage;
import com.ibm.as400.vaccess.VMessage;
import com.ibm.as400.vaccess.VMessageList;
import com.ibm.as400.vaccess.VMessageQueue;
import com.ibm.as400.vaccess.VQueuedMessage;

/**
 * The MessageSandbox class provides some common routines for testing messages
 * in the visual components.
 **/
public class MessageSandbox {
  /**
   * Setting this to false can make testing much quicker, but it eliminates
   * cleanup.
   **/
  public boolean cleanup = true;

  /**
   * Debugging messages.
   **/
  public boolean debug = false;

  protected MessageQueue queue_ = null;

  /**
   * Constructor.
   **/
  public MessageSandbox(AS400 system, String libraryName, String queueName, String userId) {
    // Create the library if necessary.

    CommandCall cmd = new CommandCall(system, "CRTLIB LIB(" + libraryName
        + ") TEXT(\'AS/400 Toolbox for Java Testing\')");
    try {
      cmd.run();

      if (debug) {
        AS400Message[] messageList = cmd.getMessageList();
        for (int i = 0; i < messageList.length; ++i) {
          System.out.println(messageList[i]);
        }
      }

      cmd = new CommandCall(system, "GRTOBJAUT OBJ(" + libraryName
					+ ") OBJTYPE(*LIB) USER("+userId+") AUT(*ALL)");

      cmd.run(); 

    } catch (Exception e) {
      if (debug) {
        System.out.println("Could not create library  " + libraryName + ".");
        e.printStackTrace();
      }
    }

    String path = QSYSObjectPathName.toPath(libraryName, queueName, "MSGQ");

    // Create the message queue if necessary.
    cmd = new CommandCall(system, "CRTMSGQ MSGQ(" + libraryName + "/"
        + queueName + ") TEXT(\'AS/400 Toolbox for Java Testing\')");
    try {
      cmd.run();

      if (debug) {
        AS400Message[] messageList = cmd.getMessageList();
        for (int i = 0; i < messageList.length; ++i) {
          System.out.println(messageList[i]);
        }
      }

      queue_ = new MessageQueue(system, path);
    } catch (Exception e) {
        System.out.println("Could not create message queue " + path + ".");
        e.printStackTrace();
    }
  }

  /**
   * Cleans up after the variations.
   **/
  public void cleanup() {
    if (cleanup) {
      QSYSObjectPathName pathName = new QSYSObjectPathName(queue_.getPath());
      try {
        queue_.remove();

        CommandCall cmd = new CommandCall(queue_.getSystem(), "DLTMSGQ MSGQ("
            + pathName.getLibraryName() + "/" + pathName.getObjectName() + ")");
        cmd.run();

        if (debug) {
          AS400Message[] messageList = cmd.getMessageList();
          for (int i = 0; i < messageList.length; ++i) {
            System.out.println(messageList[i]);
          }
        }
      } catch (Exception e) {
        System.out.println("Could not delete message queue "
            + pathName.getPath() + ".");
        e.printStackTrace();
      }
    } else {
      System.out.println("Warning: No cleanup of message queue "
          + queue_.getPath() + " was done.");
    }
  }

  /**
   * Generates an AS400Message object.
   * 
   * @param system
   *          The system object.
   **/
  public static AS400Message generateMessage(AS400 system) throws Exception {
    CommandCall cmd = new CommandCall(system, "DLTLIB XXXXXXXXXX");
    cmd.run();
    AS400Message[] messageList = cmd.getMessageList();
    return messageList[0];
  }

  /**
   * Generates an array of AS400Message objects.
   * 
   * @param system
   *          The system object.
   * @param length
   *          The array size.
   **/
  public static AS400Message[] generateMessageList(AS400 system, int length)
      throws Exception {
    CommandCall cmd = new CommandCall(system, "DLTLIB YYYYYYYY");
    cmd.run();
    AS400Message[] messageList = cmd.getMessageList();

    AS400Message[] returnList = new AS400Message[length];
    for (int i = 0; i < length; ++i) {
      returnList[i] = messageList[i % messageList.length];
    }
    return returnList;
  }

  /**
   * Generates an QueuedMessage object.
   **/
  public QueuedMessage generateQueuedMessage() throws Exception {
    queue_.remove();
    queue_.sendInformational("CAE0002",
        QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF"));
    Enumeration enumeration = queue_.getMessages();
    if (enumeration.hasMoreElements())
      return (QueuedMessage) enumeration.nextElement();
    throw new Exception("Could not generate queued message.");
  }

  /**
   * Generates a VMessage object.
   * 
   * @param system
   *          The system object.
   **/
  public static VMessage generateVMessage(AS400 system) throws Exception {
    CommandCall cmd = new CommandCall(system, "DLTLIB XXXXXXXXXX");
    cmd.run();
    AS400Message[] messageList = cmd.getMessageList();
    VMessageList vml = new VMessageList(messageList);
    vml.load();
    return (VMessage) vml.getDetailsChildAt(0);
  }

  /**
   * Generates a VQueuedMessage object.
   **/
  public VQueuedMessage generateVQueuedMessage() throws Exception {
    queue_.remove();
    queue_.sendInformational("CAE0002",
        QSYSObjectPathName.toPath("QSYS", "QCPFMSG", "MSGF"));
    VMessageQueue vmq = new VMessageQueue(queue_.getSystem(), queue_.getPath());
    vmq.load();
    return (VQueuedMessage) vmq.getDetailsChildAt(0);
  }

  /**
   * Returns the message queue.
   **/
  public MessageQueue getQueue() {
    return queue_;
  }

  /**
   * Forces the queue to have a particular number of messages.
   **/
  public void setNumberOfMessages(int count) {
    try {
      queue_.remove();
      for (int i = 0; i < count; ++i) {
        queue_.sendInformational("This is test message " + i + ".");
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
      System.out
          .println("Could not set the number of messages on message queue "
              + queue_.getPath() + ".");
    }
  }
}
