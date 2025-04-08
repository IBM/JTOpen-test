///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecPortMapperTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;

import test.JDReflectionUtil;
import test.PasswordVault;
import test.TestDriver;
import test.Testcase;

/**
 * Testcase SecPortMapperTestcase.
 * <p>
 * Test variations for the methods:
 * <ul>
 * <li>AS400::getServicePort(int)
 * <li>AS400::setServicePort(int, int)
 * <li>AS400::setServicePortsToDefault()
 * </ul>
 **/
public class SecPortMapperTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SecPortMapperTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SecAuthTest.main(newArgs); 
   }

  class SecPortClassLoader extends URLClassLoader {
    ClassLoader fallbackLoader; 
    public SecPortClassLoader(URL[] urls) {
      super(urls, null);
      fallbackLoader = URLClassLoader.newInstance(urls); 
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      try { 
      return super.findClass(name);
      } catch (java.lang.ClassNotFoundException ex) {
        System.out.println("Loading "+name); 
        return fallbackLoader.loadClass(name); 
      }
    }
  }

  ClassLoader secPortClassLoader;
  protected void setup() throws Exception {
    // Use a new classloader to load the classes and test 

    File toolboxJar = TestDriver.getLoadSource("com.ibm.as400.access.AS400");

    ClassLoader originalClassLoader = AS400.class.getClassLoader();

    String absolutePath = toolboxJar.getAbsolutePath(); 
    URL[] urls = new URL[1]; 
    if (absolutePath.endsWith(".jar")) { 
      urls[0] =  new URL("jar:file:" + absolutePath +"!/") ; 
    } else { 
      urls[0] =  new URL("file:" + absolutePath +"/") ; 
    }

    secPortClassLoader = new SecPortClassLoader(urls);

  }
  int invalidServiceNumber = 9;
  
  
  public void testService(int service) { 
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      
      Class<?> as400Class = secPortClassLoader.loadClass("com.ibm.as400.access.AS400"); 
      
      
      Class<?>[] parameterTypes = new Class[3];
      parameterTypes[0] = String.class; 
      parameterTypes[1] = String.class; 
      parameterTypes[2] = charPassword.getClass();  
      
      Constructor<?> constructor = as400Class.getConstructor(parameterTypes); 
     
      Object[] parameters = new Object[3]; 
      parameters[0] = systemName_;
      parameters[1] = userId_; 
      parameters[2] = charPassword; 
      
      Object as400Copy =  constructor.newInstance(parameters);

     
      PasswordVault.clearPassword(charPassword);
      JDReflectionUtil.callMethod_V(as400Copy, "setMustUseSockets", mustUseSockets_);

      int port = JDReflectionUtil.callMethod_I(as400Copy,"getServicePort",service);
      JDReflectionUtil.callMethod_V(as400Copy,"close"); 
      assertCondition(port == AS400.USE_PORT_MAPPER, "Port incorrect: " + port);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
 
  }
  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.FILE) before the file service has been
   * connected by any AS400 object.</dd>
   * <dt>Result:</dt>
   * <dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
   * </dl>
   **/
  public void Var001() { testService(AS400.FILE); } 

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.PRINT) before the print service has been
   * connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
   * </dl>
   **/
  public void Var002() { testService(AS400.PRINT); } 

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.COMMAND) before the command service has
   * been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
   * </dl>
   **/
  public void Var003() { testService(AS400.COMMAND); } 
    

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.DATAQUEUE) before the data queue service
   * has been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
   * </dl>
   **/
  public void Var004() {testService(AS400.DATAQUEUE); } 
 
  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.DATABASE) before the database service
   * has been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
   * </dl>
   **/
  public void Var005() {testService(AS400.DATABASE); } 

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.RECORDACCESS) before the record level
   * access service has been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
   * </dl>
   **/
  public void Var006() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      int service = AS400.RECORDACCESS;

      int port = system.getServicePort(service);
      system.close();
      assertCondition(port == 446, "Port incorrect: " + port);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.CENTRAL) before the central service has
   * been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
   * </dl>
   **/
  public void Var007() {testService(AS400.CENTRAL);}

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.SIGNON) before the sign-on service has
   * been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
   * </dl>
   **/
  public void Var008() {testService(AS400.SIGNON);}
   

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(int) with the system name not set.</dd>
   * <dt>Result:</dt>
   * <dd>Verify an ExtendedIllegalStateException is thrown.</dd>
   * </dl>
   **/
  public void Var009() {
    try {
      AS400 system = new AS400();
      system.setMustUseSockets(mustUseSockets_);
      try {
        int port = system.getServicePort(AS400.COMMAND);
        assertCondition(onAS400_, "No exception. but got port " + port);
      } catch (Exception e) {
        if (!onAS400_) {
          assertExceptionStartsWith(e, "ExtendedIllegalStateException", "systemName: ",
              ExtendedIllegalStateException.PROPERTY_NOT_SET);
        } else {
          failed(e, "Unexpected exception.");
        }
      } finally {
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(int) with an invalid service number (too
   * low).</dd>
   * <dt>Result:</dt>
   * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
   * </dl>
   **/
  public void Var010() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      try {
        int port = system.getServicePort(-1);
        failed("No exception." + port);
      } catch (Exception e) {
        assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "service (-1): ",
            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
      } finally {
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(int) with an invalid service number (too
   * high).</dd>
   * <dt>Result:</dt>
   * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
   * </dl>
   **/
  public void Var011() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      try {
        int port = system.getServicePort(invalidServiceNumber);
        failed("No exception. port=" + port + " for service " + invalidServiceNumber);
      } catch (Exception e) {
        assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "service (" + invalidServiceNumber + "): ",
            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
      } finally {
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.SIGNON) after the sign-on service has
   * been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var012() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.SIGNON;
      int expectedPort = (isNative_ && isLocal_) ? -1 : 8476;

      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.FILE) after the file service has been
   * connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var013() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.FILE;
      int expectedPort = 8473;

      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.PRINT) after the print service has been
   * connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var014() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.PRINT;
      int expectedPort = (isNative_ && isLocal_) ? -1 : 8474;

      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.COMMAND) after the command service has
   * been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var015() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.COMMAND;
      int expectedPort = (isNative_ && isLocal_) ? -1 : 8475;

      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.DATAQUEUE) after the data queue service
   * has been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var016() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.DATAQUEUE;
      int expectedPort = (isNative_ && isLocal_) ? -1 : 8472;

      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.DATABASE) after the database service has
   * been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var017() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.DATABASE;
      int expectedPort = (isNative_ && isLocal_ && system.getVRM() >= 0x00050500) ? -1 : 8471;

      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.RECORDACCESS) after the record level
   * access service has been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var018() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.RECORDACCESS;
      int expectedPort = 446;

      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::getServicePort(AS400.CENTRAL) after the central service has
   * been connected.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var019() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.CENTRAL;
      int expectedPort = (isNative_ && isLocal_) ? -1 : 8470;

      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(AS400.FILE, int) with a valid value.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var020() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.FILE;
      int setPort = 9393;

      try {
        system.setServicePort(service, setPort);

        int port = system.getServicePort(service);
        assertCondition(port == setPort, "Port incorrect: " + port + " sb " + setPort);
      } finally {
        system.setServicePort(service, 8473);
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(AS400.PRINT, int) with a valid value.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var021() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.PRINT;
      int setPort = 9394;

      try {
        system.setServicePort(service, setPort);

        int port = system.getServicePort(service);
        assertCondition(port == setPort, "Port incorrect: " + port + " sb " + setPort);
      } finally {
        system.setServicePort(service, 8474);
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(AS400.COMMAND, int) with a valid value.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var022() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.COMMAND;
      int setPort = 9395;

      try {
        system.setServicePort(service, setPort);

        int port = system.getServicePort(service);
        assertCondition(port == setPort, "Port incorrect: " + port + " sb " + setPort);
      } finally {
        system.setServicePort(service, 8475);
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(AS400.DATAQUEUE, int) with a valid value.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var023() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.DATAQUEUE;
      int setPort = 9392;

      try {
        system.setServicePort(service, setPort);

        int port = system.getServicePort(service);
        assertCondition(port == setPort, "Port incorrect: " + port + " sb " + setPort);
      } finally {
        system.setServicePort(service, 8472);
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(AS400.DATABASE, int) with a valid value.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var024() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.DATABASE;
      int setPort = 9391;

      try {
        system.setServicePort(service, setPort);

        int port = system.getServicePort(service);
        assertCondition(port == setPort, "Port incorrect: " + port + " sb " + setPort);
      } finally {
        system.setServicePort(service, 8471);
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(AS400.RECORDACCESS, int) with a valid
   * value.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var025() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.RECORDACCESS;
      int setPort = 9397;

      try {
        system.setServicePort(service, setPort);

        int port = system.getServicePort(service);
        assertCondition(port == setPort, "Port incorrect: " + port + " sb " + setPort);
      } finally {
        system.setServicePort(service, 446);
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(AS400.CENTRAL, int) with a valid value.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var026() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.CENTRAL;
      int setPort = 9390;

      try {
        system.setServicePort(service, setPort);

        int port = system.getServicePort(service);
        assertCondition(port == setPort, "Port incorrect: " + port + " sb " + setPort);
      } finally {
        system.setServicePort(service, 8470);
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(AS400.SIGNON, int) with a valid value.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var027() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.SIGNON;
      int setPort = 9396;

      try {
        system.setServicePort(service, setPort);

        int port = system.getServicePort(service);
        assertCondition(port == setPort, "Port incorrect: " + port + " sb " + setPort);
      } finally {
        system.setServicePort(service, 8476);
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(int, int) with the system name not set.</dd>
   * <dt>Result:</dt>
   * <dd>Verify an ExtendedIllegalStateException is thrown.</dd>
   * </dl>
   **/
  public void Var028() {
    try {
      AS400 system = new AS400();
      system.setMustUseSockets(mustUseSockets_);
      try {
        system.setServicePort(AS400.COMMAND, 8475);
        assertCondition(onAS400_, "No exception.");
      } catch (Exception e) {
        if (!onAS400_) {
          assertExceptionStartsWith(e, "ExtendedIllegalStateException", "systemName: ",
              ExtendedIllegalStateException.PROPERTY_NOT_SET);
        } else {
          failed(e, "Unexpected exception.");
        }
      } finally {
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(int, int) with an invalid service number (too
   * low).</dd>
   * <dt>Result:</dt>
   * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
   * </dl>
   **/
  public void Var029() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      try {
        system.setServicePort(-1, 9393);
        failed("No exception.");
      } catch (Exception e) {
        assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "service (-1): ",
            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
      } finally {
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(int, int) with an invalid service number (too
   * high).</dd>
   * <dt>Result:</dt>
   * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
   * </dl>
   **/
  public void Var030() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      try {
        system.setServicePort(invalidServiceNumber, 9393);
        failed("No exception for serviceNumber="+invalidServiceNumber);
      } catch (Exception e) {
        assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "service ("+invalidServiceNumber+"): ",
            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
      } finally {
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePort(int, int) with an invalid port number.</dd>
   * <dt>Result:</dt>
   * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
   * </dl>
   **/
  public void Var031() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      try {
        system.setServicePort(AS400.CENTRAL, -2);
        failed("No exception.");
      } catch (Exception e) {
        assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "port (-2): ",
            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
      } finally {
        system.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePortsToDefault() then connect to the file
   * service.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var032() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.FILE;
      int expectedPort = 8473;

      system.setServicePortsToDefault();
      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close(); 
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePortsToDefault() then connect to the print
   * service.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var033() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.PRINT;
      int expectedPort = 8474;

      system.setServicePortsToDefault();
      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close(); 
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePortsToDefault() then connect to the command
   * service.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var034() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.COMMAND;
      int expectedPort = 8475;

      system.setServicePortsToDefault();
      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close(); 
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePortsToDefault() then connect to the data queue
   * service.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var035() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.DATAQUEUE;
      int expectedPort = 8472;

      system.setServicePortsToDefault();
      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close(); 
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePortsToDefault() then connect to the database
   * service.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var036() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.DATABASE;
      int expectedPort = 8471;

      system.setServicePortsToDefault();
      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close(); 
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePortsToDefault() then connect to the sign-on
   * service.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var037() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.SIGNON;
      int expectedPort = 8476;

      system.setServicePortsToDefault();
      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close(); 
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePortsToDefault() then connect to the central
   * service.</dd>
   * <dt>Result:</dt>
   * <dd>Verify with getServicePort(int) the correct port number is returned.</dd>
   * </dl>
   **/
  public void Var038() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);

      int service = AS400.CENTRAL;
      int expectedPort = 8470;

      system.setServicePortsToDefault();
      system.connectService(service);
      try {
        int port = system.getServicePort(service);
        String failMessage = "";
        if (port != expectedPort)
          failMessage += "Port incorrect: " + port + " sb " + expectedPort + "\n";
        if (!system.isConnected(service))
          failMessage += "Connect failed.\n";
        assertCondition(failMessage.equals(""), "\n" + failMessage);
      } finally {
        system.disconnectAllServices();
        system.close(); 
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::setServicePortsToDefault() with the system name not set.</dd>
   * <dt>Result:</dt>
   * <dd>Verify an ExtendedIllegalStateException is thrown.</dd>
   * </dl>
   **/
  public void Var039() {
    try {
      AS400 system = new AS400();
      system.setMustUseSockets(mustUseSockets_);
      try {
        system.setServicePortsToDefault();
        assertCondition(onAS400_, "No exception.");
      } catch (Exception e) {
        if (!onAS400_) {
          assertExceptionStartsWith(e, "ExtendedIllegalStateException", "systemName: ",
              ExtendedIllegalStateException.PROPERTY_NOT_SET);
        } else {
          failed(e, "Unexpected exception.");
        }
      } finally {
        system.close(); 
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }
}
