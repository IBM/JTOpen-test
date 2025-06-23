///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecConnectTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.net.InetAddress;
import com.ibm.as400.access.*;

import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

/**
 * Testcase SecConnectTestcase.
 **/
public class SecConnectTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "SecConnectTestcase";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.SecTest.main(newArgs);
  }

  /**
   * Try to connect to an invalid service.
   **/
  public void Var001() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      try {
        sys.connectService(99);
        sys.close();
        failed("exception not generated");
      } catch (Exception e) {
        sys.close();
        assertExceptionIs(e, "ExtendedIllegalArgumentException");
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try connecting to file service and disconnect.
   **/
  public void Var002() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.FILE;

      sys.connectService(serv);
      try {
        assertCondition(sys.isConnected(serv), "Connect failed");
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, use the default constructor, connect to file. Even though
   * this does not use local sockets, it should not prompt for system name, user
   * id, and password.
   **/
  public void Var003() {
    try {
      AS400 sys = new AS400();
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.FILE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using the system name, connect to file. Even
   * though this does not use local sockets, it should not prompt for system name,
   * user id, and password.
   **/
  public void Var004() {
    try {
      AS400 sys = new AS400(InetAddress.getLocalHost().getHostName());
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.FILE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using "localhost" as the system name, connect to
   * file. Even though this does not use local sockets, it should not prompt for
   * system name, user id, and password.
   **/
  public void Var005() {
    try {
      AS400 sys = new AS400("localhost");
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.FILE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try connecting to the print service and disconnect.
   **/
  public void Var006() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.PRINT;

      sys.connectService(serv);
      try {
        assertCondition(sys.isConnected(serv), "Connect failed");
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, use the default constructor, connect to print.
   **/
  public void Var007() {
    try {
      AS400 sys = new AS400();
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.PRINT;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using the system name, connect to print.
   **/
  public void Var008() {
    try {
      AS400 sys = new AS400(InetAddress.getLocalHost().getHostName());
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.PRINT;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using "localhost" as the system name, connect to
   * print.
   **/
  public void Var009() {
    try {
      AS400 sys = new AS400("localhost");
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.PRINT;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try connecting to command and disconnect.
   **/
  public void Var010() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.COMMAND;

      sys.connectService(serv);
      try {
        assertCondition(sys.isConnected(serv), "Connect failed");
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, use default constructor connect to command.
   **/
  public void Var011() {
    try {
      AS400 sys = new AS400();
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.COMMAND;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using the system name, connect to command.
   **/
  public void Var012() {
    try {
      AS400 sys = new AS400(InetAddress.getLocalHost().getHostName());
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.COMMAND;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using "localhost" as the system name, connect to
   * command.
   **/
  public void Var013() {
    try {
      AS400 sys = new AS400("localhost");
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.COMMAND;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * setMustUseSockets to true, connect to data queues and disconnect.
   **/
  public void Var014() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(true);
      int serv = AS400.DATAQUEUE;

      sys.connectService(serv);
      try {
        assertCondition(sys.isConnected(serv), "Connect failed");
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, use the default constructor, setMustUseSockets to true,
   * connect to dataqueue.
   **/
  public void Var015() {
    try {
      AS400 sys = new AS400();
      sys.setMustUseSockets(true);
      int serv = AS400.DATAQUEUE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using the system name, setMustUseSockets to true,
   * connect to dataqueue.
   **/
  public void Var016() {
    try {
      AS400 sys = new AS400(InetAddress.getLocalHost().getHostName());
      sys.setMustUseSockets(true);
      int serv = AS400.DATAQUEUE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using "localhost" as the system name,
   * setMustUseSockets to true, connect to dataqueue.
   **/
  public void Var017() {
    try {
      AS400 sys = new AS400("localhost");
      sys.setMustUseSockets(true);
      int serv = AS400.DATAQUEUE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Connect to data queues and disconnect.
   **/
  public void Var018() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(false);
      int serv = AS400.DATAQUEUE;

      sys.connectService(serv);
      try {
        assertCondition(sys.isConnected(serv), "Connect failed");
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, use the default constructor, connect to dataqueue.
   **/
  public void Var019() {
    try {
      AS400 sys = new AS400();
      sys.setMustUseSockets(false);
      int serv = AS400.DATAQUEUE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using the system name, connect to dataqueue.
   **/
  public void Var020() {
    try {
      AS400 sys = new AS400(InetAddress.getLocalHost().getHostName());
      sys.setMustUseSockets(false);
      int serv = AS400.DATAQUEUE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using "localhost" as the system name, connect to
   * dataqueue.
   **/
  public void Var021() {
    try {
      AS400 sys = new AS400("localhost");
      sys.setMustUseSockets(false);
      int serv = AS400.DATAQUEUE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try connecting to database and disconnect.
   **/
  public void Var022() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.DATABASE;

      sys.connectService(serv);
      try {
        assertCondition(sys.isConnected(serv), "Connect failed");
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, use the default constructor, connect to database. Even
   * though this does not use local sockets, it should not prompt for system name,
   * user id, and password.
   **/
  public void Var023() {
    try {
      AS400 sys = new AS400();
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.DATABASE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using the system name, connect to database. Even
   * though this does not use local sockets, it should not prompt for system name,
   * user id, and password.
   **/
  public void Var024() {
    try {
      AS400 sys = new AS400(InetAddress.getLocalHost().getHostName());
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.DATABASE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using "localhost" as the system name, connect to
   * database. Even though this does not use local sockets, it should not prompt
   * for system name, user id, and password.
   **/
  public void Var025() {
    try {
      AS400 sys = new AS400("localhost");
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.DATABASE;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * setMustUseSockets to true, connect to record level access and disconnect.
   **/
  public void Var026() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(true);
      int serv = AS400.RECORDACCESS;

      sys.connectService(serv);
      try {
        assertCondition(sys.isConnected(serv), "Connect failed");
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, use the default constructor, setMustUseSockets to true,
   * connect to record access.
   **/
  public void Var027() {
    try {
      AS400 sys = new AS400();
      sys.setMustUseSockets(true);
      int serv = AS400.RECORDACCESS;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using the system name, setMustUseSockets to true,
   * connect to record access.
   **/
  public void Var028() {
    try {
      AS400 sys = new AS400(InetAddress.getLocalHost().getHostName());
      sys.setMustUseSockets(true);
      int serv = AS400.RECORDACCESS;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using "localhost" as the system name,
   * setMustUseSockets to true, connect to record access.
   **/
  public void Var029() {
    try {
      AS400 sys = new AS400("localhost");
      sys.setMustUseSockets(true);
      int serv = AS400.RECORDACCESS;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * connect to record level access and disconnect.
   **/
  public void Var030() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(false);
      int serv = AS400.RECORDACCESS;

      sys.connectService(serv);
      try {
        assertCondition(sys.isConnected(serv), "Connect failed");
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, use the default constructor, connect to record access.
   **/
  public void Var031() {
    try {
      AS400 sys = new AS400();
      sys.setMustUseSockets(false);
      int serv = AS400.RECORDACCESS;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using the system name, connect to record access.
   **/
  public void Var032() {
    try {
      AS400 sys = new AS400(InetAddress.getLocalHost().getHostName());
      sys.setMustUseSockets(false);
      int serv = AS400.RECORDACCESS;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * If on the AS400, construct using "localhost" as the system name, connect to
   * record access.
   **/
  public void Var033() {
    try {
      AS400 sys = new AS400("localhost");
      sys.setMustUseSockets(false);
      int serv = AS400.RECORDACCESS;

      if (JTOpenTestEnvironment.isOS400 && isNative_) {
        sys.connectService(serv);
        try {
          assertCondition(sys.isConnected(serv), "Connect failed");
        } finally {
          sys.disconnectAllServices();
          sys.close();
        }
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Connect to all services and disconnect all.
   **/
  public void Var034() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.connectService(AS400.FILE);
        sys.connectService(AS400.PRINT);
        sys.connectService(AS400.DATAQUEUE);
        sys.connectService(AS400.COMMAND);
        sys.connectService(AS400.DATABASE);
        sys.connectService(AS400.RECORDACCESS);

        assertCondition(sys.isConnected(), "Connect failed");
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create a default AS400 object and try to signon with an invalid system name.
   * An UnknownHostException should be generated.
   **/
  public void Var035() {
    try {
      AS400 sys = new AS400("BADSYS", "aaa", "bbb".toCharArray());
      sys.setGuiAvailable(false);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.connectService(AS400.COMMAND);
        sys.close(); 
        failed("sign on succeeded without a system name or user ID");
      } catch (Exception e) {
        sys.close(); 
        assertExceptionIs(e, "UnknownHostException");
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Connect to service without setting the system name. This should go to the
   * local system, and if we are not running on an AS/400, an exception will be
   * generated. In some cases this will be a ServerPortMapException.
   **/
  public void Var036() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400("localhost", userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.connectService(AS400.COMMAND);
        if (JTOpenTestEnvironment.isOS400) {
          sys.close(); 
          succeeded();
        } else {
          sys.close(); 
          failed("Exception not generated");
        }
      } catch (Exception e) {
        if (JTOpenTestEnvironment.isOS400) {
          sys.close(); 
          failed(e, "Unexpected exception");
        } else {
          sys.close(); 
          assertExceptionIs(e, "ConnectException");
        }
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try connecting to command, disconnecting, then changing the system name.
   **/
  public void Var037() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.COMMAND;

      sys.connectService(serv);
      sys.disconnectService(serv);
      try {
        sys.setSystemName("wontwork");
        sys.close(); 
        failed("No exception.");
      } catch (Exception e) {
        sys.close(); 
        assertExceptionIs(e, "ExtendedIllegalStateException");
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try connecting to command, disconnecting, then changing the user ID.
   **/
  public void Var038() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.COMMAND;

      sys.connectService(serv);
      sys.disconnectService(serv);
      try {
        sys.setUserId("wontwork");
        sys.close(); 
        failed("No exception.");
      } catch (Exception e) {
        sys.close(); 
        assertExceptionIs(e, "ExtendedIllegalStateException");
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try connecting to command, then changing the password.
   **/
  public void Var039() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      int serv = AS400.COMMAND;

      sys.connectService(serv);
      try {
        sys.setPassword("wontwork".toCharArray());
        succeeded();
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try add password cacheEntry, connecting to command, run command.
   **/
  public void Var040() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400.addPasswordCacheEntry(systemName_, userId_, charPassword);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      sys.connectService(AS400.COMMAND);
      try {
        CommandCall cmd = new CommandCall(sys);
        cmd.run("DSPLIB FRED");
        succeeded();
      } finally {
        sys.disconnectAllServices();
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Stress test connect.
   **/
  // public void Var041()
  // {
  // try
  // {
  // char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
  // AS400 sys = new AS400(systemName_, userId_, charPassword);
  // PasswordVault.clearPassword(charPassword);
  // sys.setMustUseSockets(mustUseSockets_);
  //
  // Date start = new Date();
  // Date now = new Date();
  //
  // long starttime = start.getTime();
  // long nowtime = now.getTime();
  // long elapsed = nowtime - starttime;
  //
  // int attempts = 0;
  // while (elapsed < 3600000)
  // {
  // sys.connectService(AS400.COMMAND);
  // now = new Date();
  // nowtime = now.getTime();
  // elapsed = nowtime - starttime;
  // attempts++;
  // output_.println(elapsed);
  // output_.println(attempts);
  // sys.disconnectService(AS400.COMMAND);
  // }
  //
  // succeeded();
  // }
  // catch (Exception e)
  // {
  // failed(e, "Unexpected exception");
  // }
  // }

  /**
   * Connect to a service and call isConnectionAlive().
   **/
  public void Var041() {
    boolean succeeded = true;
    AS400 sys = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);

      boolean result;

      result = testConnection(sys, AS400.FILE);
      if (!result)
        succeeded = false;

      result = testConnection(sys, AS400.PRINT);
      if (!result)
        succeeded = false;

      result = testConnection(sys, AS400.COMMAND);
      if (!result)
        succeeded = false;

      result = testConnection(sys, AS400.DATAQUEUE);
      if (!result)
        succeeded = false;

      result = testConnection(sys, AS400.DATABASE);
      if (!result)
        succeeded = false;

      result = testConnection(sys, AS400.RECORDACCESS);
      if (!result)
        succeeded = false;

      result = testConnection(sys, AS400.CENTRAL);
      if (!result)
        succeeded = false;

      result = testConnection(sys, AS400.SIGNON);
      if (!result)
        succeeded = false;

      assertCondition(succeeded);
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    } finally {
      if (sys != null) {
        sys.disconnectAllServices();
        sys.close();
      }
    }
  }

  /**
   * Connect to a service and call isConnectionAlive(service).
   **/
  public void Var042() {
    boolean succeeded = true;
    AS400 sys = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);

      boolean result;

      result = testConnectionToService(sys, AS400.FILE);
      if (!result)
        succeeded = false;

      result = testConnectionToService(sys, AS400.PRINT);
      if (!result)
        succeeded = false;

      result = testConnectionToService(sys, AS400.COMMAND);
      if (!result)
        succeeded = false;

      result = testConnectionToService(sys, AS400.DATAQUEUE);
      if (!result)
        succeeded = false;

      result = testConnectionToService(sys, AS400.DATABASE);
      if (!result)
        succeeded = false;

      result = testConnectionToService(sys, AS400.RECORDACCESS);
      if (!result)
        succeeded = false;

      result = testConnectionToService(sys, AS400.CENTRAL);
      if (!result)
        succeeded = false;

      result = testConnectionToService(sys, AS400.SIGNON);
      if (!result)
        succeeded = false;

      assertCondition(succeeded);
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    } finally {
      if (sys != null) {
        sys.disconnectAllServices();
        sys.close();
      }
    }
  }

  private final boolean testConnection(AS400 sys, int service) {
    boolean succeeded = true;
    try {
      boolean result;
      // System.out.println("calling isConnectionAlive ...");
      result = sys.isConnectionAlive();
      // System.out.println("isConnectionAlive() returned " + result);
      if (result == true) {
        System.out.println("UNEXPECTED RESULT (1)");
        succeeded = false;
      }

      // System.out.println("connecting to "+nameForService(service)+" service ...");
      sys.connectService(service);

      // System.out.println("calling isConnectionAlive ...");
      result = sys.isConnectionAlive();
      // System.out.println("isConnectionAlive() returned " + result);
      if (result == false) {
        System.out.println("UNEXPECTED RESULT (2)");
        succeeded = false;
      }

      // System.out.println("calling isConnectionAlive again ...");
      result = sys.isConnectionAlive();
      // System.out.println("isConnectionAlive() returned " + result);
      if (result == false) {
        System.out.println("UNEXPECTED RESULT (3)");
        succeeded = false;
      }

      // System.out.println("disconnecting "+nameForService(service)+" service ...");
      sys.disconnectService(service);

      // System.out.println("calling isConnectionAlive ...");
      result = sys.isConnectionAlive();
      // System.out.println("isConnectionAlive() returned " + result);
      if (result == true) {
        System.out.println("UNEXPECTED RESULT (4)");
        succeeded = false;
      }
    } catch (Throwable t) {
      System.out.println("UNEXPECTED EXCEPTION");
      t.printStackTrace();
      succeeded = false;
    } finally {
      try {
        sys.disconnectAllServices();
        sys.close();
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
    return succeeded;
  }

  private final boolean testConnectionToService(AS400 sys, int service) {
    boolean succeeded = true;
    try {
      boolean result;
      // System.out.println("calling isConnectionAlive ...");
      result = sys.isConnectionAlive(service);
      // System.out.println("isConnectionAlive("+nameForService(service)+") returned "
      // + result);
      if (result == true) {
        System.out.println("UNEXPECTED RESULT (5)");
        succeeded = false;
      }

      // System.out.println("connecting to "+nameForService(service)+" service ...");
      sys.connectService(service);

      // System.out.println("calling isConnectionAlive ...");
      result = sys.isConnectionAlive(service);
      // System.out.println("isConnectionAlive("+nameForService(service)+") returned "
      // + result);
      if (result == false) {
        System.out.println("UNEXPECTED RESULT (6)");
        succeeded = false;
      }

      // System.out.println("calling isConnectionAlive again ...");
      result = sys.isConnectionAlive(service);
      // System.out.println("isConnectionAlive("+nameForService(service)+") returned "
      // + result);
      if (result == false) {
        System.out.println("UNEXPECTED RESULT (7)");
        succeeded = false;
      }

      /*
       * If attended: if (service == AS400.DATABASE) { System.out.println
       * ("Signon to the system and end the job QUSRWRK/QZDASOINIT under user \"" +
       * userId_ + "\".  Press ENTER to continue."); try { System.in.read (); } catch
       * (Exception exc) {};
       * 
       * System.out.println("calling isConnectionAlive ..."); result =
       * sys.isConnectionAlive(); System.out.println("isConnectionAlive() returned " +
       * result); if (result == true) { System.out.println("UNEXPECTED RESULT (X)");
       * succeeded = false; } }
       */

      // System.out.println("disconnecting "+nameForService(service)+" service ...");
      sys.disconnectService(service);

      // System.out.println("calling isConnectionAlive ...");
      result = sys.isConnectionAlive(service);
      // System.out.println("isConnectionAlive("+nameForService(service)+") returned "
      // + result);
      if (result == true) {
        System.out.println("UNEXPECTED RESULT (8)");
        succeeded = false;
      }
    } catch (Throwable t) {
      System.out.println("UNEXPECTED EXCEPTION");
      t.printStackTrace();
      succeeded = false;
    } finally {
      try {
        sys.disconnectAllServices();
        sys.close();
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
    return succeeded;
  }

}
