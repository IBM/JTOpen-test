///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLPortMapperTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SSL;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.SecureAS400;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase SSLPortMapperTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>SecureAS400::getServicePort(int)
 <li>SecureAS400::setServicePort(int, int)
 <li>SecureAS400::setServicePortsToDefault()
 </ul>
 **/
public class SSLPortMapperTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SSLPortMapperTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SSLTest.main(newArgs); 
   }
    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.FILE) before the file service has been connected.</dd>
     <dt>Result:</dt><dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
     </dl>
     **/
    public void Var001()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            int service = AS400.FILE;

            int port = system.getServicePort(service);
            assertCondition(port == AS400.USE_PORT_MAPPER, "Port incorrect: " + port);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.PRINT) before the print service has been connected.</dd>
     <dt>Result:</dt><dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
     </dl>
     **/
    public void Var002()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            int service = AS400.PRINT;

            int port = system.getServicePort(service);
            assertCondition(port == AS400.USE_PORT_MAPPER, "Port incorrect: " + port);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.COMMAND) before the command service has been connected.</dd>
     <dt>Result:</dt><dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
     </dl>
     **/
    public void Var003()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            int service = AS400.COMMAND;

            int port = system.getServicePort(service);
            assertCondition(port == AS400.USE_PORT_MAPPER, "Port incorrect: " + port);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.DATAQUEUE) before the data queue service has been connected.</dd>
     <dt>Result:</dt><dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            int service = AS400.DATAQUEUE;

            int port = system.getServicePort(service);
            assertCondition(port == AS400.USE_PORT_MAPPER, "Port incorrect: " + port);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.DATABASE) before the database service has been connected.</dd>
     <dt>Result:</dt><dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
     </dl>
     **/
    public void Var005()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            int service = AS400.DATABASE;

            int port = system.getServicePort(service);
            assertCondition(port == AS400.USE_PORT_MAPPER, "Port incorrect: " + port);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.RECORDACCESS) before the record level access service has been connected.</dd>
     <dt>Result:</dt><dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
     </dl>
     **/
    public void Var006()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            int service = AS400.RECORDACCESS;

            int port = system.getServicePort(service);
            assertCondition(port == 448, "Port incorrect: " + port);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.CENTRAL) before the central service has been connected.</dd>
     <dt>Result:</dt><dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            int service = AS400.CENTRAL;

            int port = system.getServicePort(service);
            assertCondition(port == AS400.USE_PORT_MAPPER, "Port incorrect: " + port);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.SIGNON) before the sign-on service has been connected.</dd>
     <dt>Result:</dt><dd>Verify AS400.USE_PORT_MAPPER is returned.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            int service = AS400.SIGNON;

            int port = system.getServicePort(service);
            assertCondition(port == AS400.USE_PORT_MAPPER, "Port incorrect: " + port);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(int) with the system name not set.</dd>
     <dt>Result:</dt><dd>Verify an ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var009()
    {
        try
        {
            SecureAS400 system = new SecureAS400();
            system.setMustUseSockets(mustUseSockets_);
            try
            {
                int port = system.getServicePort(AS400.COMMAND);
                assertCondition(onAS400_, "No exception.");
            }
            catch (Exception e)
            {
                if (!onAS400_)
                {
                    assertExceptionStartsWith(e, "ExtendedIllegalStateException", "systemName: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
                }
                else
                {
                    failed(e, "Unexpected exception.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(int) with an invalid service number (too low).</dd>
     <dt>Result:</dt><dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
     </dl>
     **/
    public void Var010()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            try
            {
                int port = system.getServicePort(-1);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "service (-1): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(int) with an invalid service number (too high).</dd>
     <dt>Result:</dt><dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
     </dl>
     **/
    public void Var011()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            try
            {
                int port = system.getServicePort(8);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "service (8): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.SIGNON) after the sign-on service has been connected.</dd>
     <dt>Result:</dt><dd>Verify the correct port number is returned.</dd>
     </dl>
     **/
    public void Var012()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.SIGNON;
            int expectedPort = (isNative_ && isLocal_) ? -1 : 9476;

            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.FILE) after the file service has been connected.</dd>
     <dt>Result:</dt><dd>Verify the correct port number is returned.</dd>
     </dl>
     **/
    public void Var013()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.FILE;
            int expectedPort = 9473;

            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.PRINT) after the print service has been connected.</dd>
     <dt>Result:</dt><dd>Verify the correct port number is returned.</dd>
     </dl>
     **/
    public void Var014()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.PRINT;
            int expectedPort = (isNative_ && isLocal_) ? -1 : 9474;

            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.COMMAND) after the command service has been connected.</dd>
     <dt>Result:</dt><dd>Verify the correct port number is returned.</dd>
     </dl>
     **/
    public void Var015()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.COMMAND;
            int expectedPort = (isNative_ && isLocal_) ? -1 : 9475;

            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.DATAQUEUE) after the data queue service has been connected.</dd>
     <dt>Result:</dt><dd>Verify the correct port number is returned.</dd>
     </dl>
     **/
    public void Var016()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.DATAQUEUE;
            int expectedPort = (isNative_ && isLocal_) ? -1 : 9472;

            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.DATABASE) after the database service has been connected.</dd>
     <dt>Result:</dt><dd>Verify the correct port number is returned.</dd>
     </dl>
     **/
    public void Var017()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.DATABASE;
            int expectedPort = 9471;

            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.RECORDACCESS) after the record level access service has been connected.</dd>
     <dt>Result:</dt><dd>Verify the correct port number is returned.</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.RECORDACCESS;
            int expectedPort = 448;

            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::getServicePort(AS400.CENTRAL) after the central service has been connected.</dd>
     <dt>Result:</dt><dd>Verify the correct port number is returned.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.CENTRAL;
            int expectedPort = (isNative_ && isLocal_) ? -1 : 9470;

            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(AS400.FILE, int) with a valid value.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var020()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.FILE;
            int setPort = 9393;

            try
            {
                system.setServicePort(service, setPort);

                int port = system.getServicePort(service);
                assertCondition(port == setPort, "Port incorrect: " + port);
            }
            finally
            {
                system.setServicePort(service, 9473);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(AS400.PRINT, int) with a valid value.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var021()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.PRINT;
            int setPort = 9394;

            try
            {
                system.setServicePort(service, setPort);

                int port = system.getServicePort(service);
                assertCondition(port == setPort, "Port incorrect: " + port);
            }
            finally
            {
                system.setServicePort(service, 9474);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(AS400.COMMAND, int) with a valid value.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var022()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.COMMAND;
            int setPort = 9395;

            try
            {
                system.setServicePort(service, setPort);

                int port = system.getServicePort(service);
                assertCondition(port == setPort, "Port incorrect: " + port);
            }
            finally
            {
                system.setServicePort(service, 9475);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(AS400.DATAQUEUE, int) with a valid value.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var023()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.DATAQUEUE;
            int setPort = 9392;

            try
            {
                system.setServicePort(service, setPort);

                int port = system.getServicePort(service);
                assertCondition(port == setPort, "Port incorrect: " + port);
            }
            finally
            {
                system.setServicePort(service, 9472);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(AS400.DATABASE, int) with a valid value.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.DATABASE;
            int setPort = 9391;

            try
            {
                system.setServicePort(service, setPort);

                int port = system.getServicePort(service);
                assertCondition(port == setPort, "Port incorrect: " + port);
            }
            finally
            {
                system.setServicePort(service, 9471);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(AS400.RECORDACCESS, int) with a valid value.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.RECORDACCESS;
            int setPort = 9397;

            try
            {
                system.setServicePort(service, setPort);

                int port = system.getServicePort(service);
                assertCondition(port == setPort, "Port incorrect: " + port);
            }
            finally
            {
                system.setServicePort(service, 448);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(AS400.CENTRAL, int) with a valid value.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.CENTRAL;
            int setPort = 9390;

            try
            {
                system.setServicePort(service, setPort);

                int port = system.getServicePort(service);
                assertCondition(port == setPort, "Port incorrect: " + port);
            }
            finally
            {
                system.setServicePort(service, 9470);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(AS400.SIGNON, int) with a valid value.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var027()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.SIGNON;
            int setPort = 9396;

            try
            {
                system.setServicePort(service, setPort);

                int port = system.getServicePort(service);
                assertCondition(port == setPort, "Port incorrect: " + port);
            }
            finally
            {
                system.setServicePort(service, 9476);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(int, int) with the system name not set.</dd>
     <dt>Result:</dt><dd>Verify an ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var028()
    {
        try
        {
            SecureAS400 system = new SecureAS400();
            system.setMustUseSockets(mustUseSockets_);
            try
            {
                system.setServicePort(AS400.COMMAND, 9475);
                assertCondition(onAS400_, "No exception.");
            }
            catch (Exception e)
            {
                if (!onAS400_)
                {
                    assertExceptionStartsWith(e, "ExtendedIllegalStateException", "systemName: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
                }
                else
                {
                    failed(e, "Unexpected exception.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(int, int) with an invalid service number (too low).</dd>
     <dt>Result:</dt><dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            try
            {
                system.setServicePort(-1, 9393);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "service (-1): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(int, int) with an invalid service number (too high).</dd>
     <dt>Result:</dt><dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            try
            {
                system.setServicePort(8, 9393);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "service (8): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePort(int, int) with an invalid port number.</dd>
     <dt>Result:</dt><dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            try
            {
                system.setServicePort(AS400.CENTRAL, -2);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "port (-2): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePortsToDefault() then connect to the file service.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var032()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.FILE;
            int expectedPort = 9473;

            system.setServicePortsToDefault();
            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePortsToDefault() then connect to the print service.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var033()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.PRINT;
            int expectedPort = 9474;

            system.setServicePortsToDefault();
            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePortsToDefault() then connect to the command service.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var034()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.COMMAND;
            int expectedPort = 9475;

            system.setServicePortsToDefault();
            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePortsToDefault() then connect to the data queue service.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var035()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.DATAQUEUE;
            int expectedPort = 9472;

            system.setServicePortsToDefault();
            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePortsToDefault() then connect to the database service.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var036()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.DATABASE;
            int expectedPort = 9471;

            system.setServicePortsToDefault();
            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePortsToDefault() then connect to the sign-on service.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var037()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.SIGNON;
            int expectedPort = 9476;

            system.setServicePortsToDefault();
            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePortsToDefault() then connect to the central service.</dd>
     <dt>Result:</dt><dd>Verify with getServicePort(int) the correct port number is returned.</dd>
     </dl>
     **/
    public void Var038()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);

            int service = AS400.CENTRAL;
            int expectedPort = 9470;

            system.setServicePortsToDefault();
            system.connectService(service);
            try
            {
                int port = system.getServicePort(service);
                String failMessage = "";
                if (port != expectedPort) failMessage += "Port incorrect: " + port + "\n";
                if (!system.isConnected(service)) failMessage += "Connect failed.\n";
                assertCondition(failMessage.equals(""), "\n" + failMessage);
            }
            finally
            {
                system.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call AS400::setServicePortsToDefault() with the system name not set.</dd>
     <dt>Result:</dt><dd>Verify an ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var039()
    {
        try
        {
            SecureAS400 system = new SecureAS400();
            system.setMustUseSockets(mustUseSockets_);
            try
            {
                system.setServicePortsToDefault();
                assertCondition(onAS400_, "No exception.");
            }
            catch (Exception e)
            {
                if (!onAS400_)
                {
                    assertExceptionStartsWith(e, "ExtendedIllegalStateException", "systemName: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
                }
                else
                {
                    failed(e, "Unexpected exception.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
