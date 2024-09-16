///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLConnectTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SSL;

import java.net.InetAddress;
import java.util.Date;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.SecureAS400;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase SSLConnectTestcase.
 **/
public class SSLConnectTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SSLConnectTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SSLTest.main(newArgs); 
   }
    /**
     Try to connect to an invalid service.
     **/
    public void Var001()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            try
            {
                sys.connectService(99);
                failed("exception not generated");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ExtendedIllegalArgumentException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try connecting to file service and disconnect.
     **/
    public void Var002()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.FILE;

            sys.connectService(serv);
            try
            {
                assertCondition(sys.isConnected(serv), "Connect failed");
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, use the default constructor, connect to file.
     Even though this does not use local sockets, it should not prompt for system name, user id, and password.
     **/
    public void Var003()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.FILE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using the system name, connect to file.
     Even though this does not use local sockets, it should not prompt for system name, user id, and password.
     **/
    public void Var004()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(InetAddress.getLocalHost().getHostName());
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.FILE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using "localhost" as the system name, connect to file.
     Even though this does not use local sockets, it should not prompt for system name, user id, and password.
     **/
    public void Var005()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("localhost");
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.FILE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try connecting to the print service and disconnect.
     **/
    public void Var006()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.PRINT;

            sys.connectService(serv);
            try
            {
                assertCondition(sys.isConnected(serv), "Connect failed");
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, use the default constructor, connect to print.
     **/
    public void Var007()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.PRINT;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using the system name, connect to print.
     **/
    public void Var008()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(InetAddress.getLocalHost().getHostName());
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.PRINT;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using "localhost" as the system name, connect to print.
     **/
    public void Var009()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("localhost");
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.PRINT;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try connecting to command and disconnect.
     **/
    public void Var010()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.COMMAND;

            sys.connectService(serv);
            try
            {
                assertCondition(sys.isConnected(serv), "Connect failed");
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, use default constructor connect to command.
     **/
    public void Var011()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.COMMAND;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using the system name, connect to command.
     **/
    public void Var012()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(InetAddress.getLocalHost().getHostName());
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.COMMAND;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using "localhost" as the system name, connect to command.
     **/
    public void Var013()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("localhost");
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.COMMAND;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     setMustUseSockets to true, connect to data queues and disconnect.
     **/
    public void Var014()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(true);
            int serv = AS400.DATAQUEUE;

            sys.connectService(serv);
            try
            {
                assertCondition(sys.isConnected(serv), "Connect failed");
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, use the default constructor, setMustUseSockets to true, connect to dataqueue.
     **/
    public void Var015()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(true);
            int serv = AS400.DATAQUEUE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using the system name, setMustUseSockets to true, connect to dataqueue.
     **/
    public void Var016()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(InetAddress.getLocalHost().getHostName());
            sys.setMustUseSockets(true);
            int serv = AS400.DATAQUEUE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using "localhost" as the system name, setMustUseSockets to true, connect to dataqueue.
     **/
    public void Var017()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("localhost");
            sys.setMustUseSockets(true);
            int serv = AS400.DATAQUEUE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Connect to data queues and disconnect.
     **/
    public void Var018()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(false);
            int serv = AS400.DATAQUEUE;

            sys.connectService(serv);
            try
            {
                assertCondition(sys.isConnected(serv), "Connect failed");
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, use the default constructor, connect to dataqueue.
     **/
    public void Var019()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(false);
            int serv = AS400.DATAQUEUE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using the system name, connect to dataqueue.
     **/
    public void Var020()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(InetAddress.getLocalHost().getHostName());
            sys.setMustUseSockets(false);
            int serv = AS400.DATAQUEUE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using "localhost" as the system name, connect to dataqueue.
     **/
    public void Var021()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("localhost");
            sys.setMustUseSockets(false);
            int serv = AS400.DATAQUEUE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try connecting to database and disconnect.
     **/
    public void Var022()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.DATABASE;

            sys.connectService(serv);
            try
            {
                assertCondition(sys.isConnected(serv), "Connect failed");
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, use the default constructor, connect to database.
     Even though this does not use local sockets, it should not prompt for system name, user id, and password.
     **/
    public void Var023()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.DATABASE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using the system name, connect to database.
     Even though this does not use local sockets, it should not prompt for system name, user id, and password.
     **/
    public void Var024()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(InetAddress.getLocalHost().getHostName());
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.DATABASE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using "localhost" as the system name, connect to database.
     Even though this does not use local sockets, it should not prompt for system name, user id, and password.
     **/
    public void Var025()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("localhost");
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.DATABASE;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     setMustUseSockets to true, connect to record level access and disconnect.
     **/
    public void Var026()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(true);
            int serv = AS400.RECORDACCESS;

            sys.connectService(serv);
            try
            {
                assertCondition(sys.isConnected(serv), "Connect failed");
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, use the default constructor, setMustUseSockets to true, connect to record access.
     **/
    public void Var027()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(true);
            int serv = AS400.RECORDACCESS;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using the system name, setMustUseSockets to true, connect to record access.
     **/
    public void Var028()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(InetAddress.getLocalHost().getHostName());
            sys.setMustUseSockets(true);
            int serv = AS400.RECORDACCESS;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using "localhost" as the system name, setMustUseSockets to true, connect to record access.
     **/
    public void Var029()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("localhost");
            sys.setMustUseSockets(true);
            int serv = AS400.RECORDACCESS;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     connect to record level access and disconnect.
     **/
    public void Var030()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(false);
            int serv = AS400.RECORDACCESS;

            sys.connectService(serv);
            try
            {
                assertCondition(sys.isConnected(serv), "Connect failed");
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, use the default constructor, connect to record access.
     **/
    public void Var031()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(false);
            int serv = AS400.RECORDACCESS;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using the system name, connect to record access.
     **/
    public void Var032()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(InetAddress.getLocalHost().getHostName());
            sys.setMustUseSockets(false);
            int serv = AS400.RECORDACCESS;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, construct using "localhost" as the system name, connect to record access.
     **/
    public void Var033()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("localhost");
            sys.setMustUseSockets(false);
            int serv = AS400.RECORDACCESS;

            if (onAS400_ && isNative_)
            {
                sys.connectService(serv);
                try
                {
                    assertCondition(sys.isConnected(serv), "Connect failed");
                }
                finally
                {
                    sys.disconnectAllServices();
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Connect to all services and disconnect all.
     **/
    public void Var034()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.connectService(AS400.FILE);
                sys.connectService(AS400.PRINT);
                sys.connectService(AS400.DATAQUEUE);
                sys.connectService(AS400.COMMAND);
                sys.connectService(AS400.DATABASE);
                sys.connectService(AS400.RECORDACCESS);

                assertCondition(sys.isConnected(), "Connect failed");
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a default AS400 object and try to signon with an invalid system name.
     An UnknownHostException should be generated.
     **/
    public void Var035()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("BADSYS", "aaa", "bbb");
            sys.setGuiAvailable(false);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.connectService(AS400.COMMAND);
                failed("sign on succeeded without a system name or user ID");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "UnknownHostException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Connect to service without setting the system name.  This should go to the local system, and if we are not running on an AS/400, an exception will be generated.  In some cases this will be a ServerPortMapException.  
     **/
    public void Var036()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400("localhost", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.connectService(AS400.COMMAND);
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
                    failed("Exception not generated");
                }
            }
            catch (Exception e)
            {
                if (onAS400_)
                {
                    failed(e, "Unexpected exception");
                }
                else
                {
                    assertExceptionIs(e, "ConnectException");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try connecting to command, disconnecting, then changing the system name.
     **/
    public void Var037()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.COMMAND;

            sys.connectService(serv);
            sys.disconnectService(serv);
            try
            {
                sys.setSystemName("wontwork");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ExtendedIllegalStateException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try connecting to command, disconnecting, then changing the user ID.
     **/
    public void Var038()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.COMMAND;

            sys.connectService(serv);
            sys.disconnectService(serv);
            try
            {
                sys.setUserId("wontwork");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ExtendedIllegalStateException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try connecting to command, then changing the password.
     **/
    public void Var039()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            int serv = AS400.COMMAND;

            sys.connectService(serv);
            try
            {
                sys.setPassword("wontwork");
                succeeded();
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try add password cacheEntry, connecting to command, run command.
     **/
    public void Var040()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400.addPasswordCacheEntry(systemName_, userId_, charPassword);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.COMMAND);
            try
            {
                CommandCall cmd = new CommandCall(sys);
                cmd.run("DSPLIB FRED");
                succeeded();
            }
            finally
            {
                sys.disconnectAllServices();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Stress test connect.
     **/
    //  public void Var041()
    //  {
    //	try
    //	{
   // char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
    //	    SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   // PasswordVault.clearPassword(charPassword);
    //	    sys.setMustUseSockets(mustUseSockets_);
    //
    //	    Date start = new Date();
    //	    Date now = new Date();
    //
    //	    long starttime = start.getTime();
    //	    long nowtime = now.getTime();
    //	    long elapsed = nowtime - starttime;
    //
    //	    int attempts = 0;
    //	    while (elapsed < 3600000)
    //	    {
    //		sys.connectService(AS400.COMMAND);
    //		now = new Date();
    //		nowtime = now.getTime();
    //		elapsed = nowtime - starttime;
    //		attempts++;
    //		output_.println(elapsed);
    //		output_.println(attempts);
    //		sys.disconnectService(AS400.COMMAND);
    //	    }
    //
    //	    succeeded();
    //	}
    //	catch (Exception e)
    //	{
    //	    failed(e, "Unexpected exception");
    //	}
    //  }
}
