///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLEventTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SSL;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ConnectionEvent;
import com.ibm.as400.access.ConnectionListener;
import com.ibm.as400.access.SecureAS400;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase SSLEventTestcase.
 **/
public class SSLEventTestcase extends Testcase implements ConnectionListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SSLEventTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SSLTest.main(newArgs); 
   }
    Object connectSrc;
    int connectServ;
    int connectCount;
    Object disconnectSrc;
    int disconnectServ;
    int disconnectCount;

    public void connected(ConnectionEvent e)
    {
        connectSrc = e.getSource();
        connectServ = e.getService();
        connectCount++;
    }

    public void disconnected(ConnectionEvent e)
    {
        disconnectSrc = e.getSource();
        disconnectServ = e.getService();
        disconnectCount++;
    }

    void resetState()
    {
        connectSrc = null;
        connectServ = -1;
        connectCount = 0;
        disconnectSrc = null;
        disconnectServ = -1;
        disconnectCount = 0;
    }

    String verifyConnect(SecureAS400 sys, int serv)
    {
        String failMsg = "";
        if (connectSrc == null)
        {
            failMsg += "connect event not received\n";
        }
        if ((SecureAS400)connectSrc != sys)
        {
            failMsg += "incorrect connect source\n";
        }
        if (connectServ != serv)
        {
            failMsg += "incorrect connect service (expected: " + serv + " received: " + connectServ + ")\n";
        }
        if (connectCount != 1)
        {
            failMsg += "incorrect connect count\n";
        }
        if (disconnectSrc != null)
        {
            failMsg += "disconnect received on connect\n";
        }
        if (disconnectServ != -1)
        {
            failMsg += "disconnect service changed on connect\n";
        }
        if (disconnectCount != 0)
        {
            failMsg += "disconnect count changed on connect\n";
        }
        return failMsg;
    }

    String verifyDisconnect(SecureAS400 sys, int serv)
    {
        String failMsg = "";
        if (disconnectSrc == null)
        {
            failMsg += "disconnect event not received\n";
        }
        if ((SecureAS400)disconnectSrc != sys)
        {
            failMsg += "incorrect disconnect source\n";
        }
        if (disconnectServ != serv)
        {
            failMsg += "incorrect disconnect service (expected: " + serv + " received: " + connectServ + ")\n";
        }
        if (disconnectCount != 1)
        {
            failMsg += "incorrect disconnect count\n";
        }
        if (connectSrc != null)
        {
            failMsg += "connect received on disconnect\n";
        }
        if (connectServ != -1)
        {
            failMsg += "connect service changed on disconnect\n";
        }
        if (connectCount != 0)
        {
            failMsg += "connect count changed on disconnect\n";
        }
        return failMsg;
    }

    String verifyNoEvent()
    {
        String failMsg = "";
        if (connectSrc != null)
        {
            failMsg += "connect event received in no event\n";
        }
        if (connectServ != -1)
        {
            failMsg += "connect service changed in no event\n";
        }
        if (connectCount != 0)
        {
            failMsg += "connect count changed in no event\n";
        }
        if (disconnectSrc != null)
        {
            failMsg += "disconnect event received in no event\n";
        }
        if (disconnectServ != -1)
        {
            failMsg += "disconnect service changed in no event\n";
        }
        if (disconnectCount != 0)
        {
            failMsg += "disconnect count changed in no event\n";
        }
        return failMsg;
    }

    /**
     Register for connection event and connect to file service.
     Verify that a connection event is fired.
     Then disconnect and verify that a connection event is fired.
     **/
    public void Var001()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.SIGNON);
            sys.disconnectService(AS400.SIGNON);

            int serv = AS400.FILE;
            String failMsg = "";

            sys.addConnectionListener(this);
            try
            {
                resetState();
                sys.connectService(serv);
                failMsg += verifyConnect(sys, serv);

                resetState();
                sys.disconnectService(serv);
                failMsg += verifyDisconnect(sys, serv);

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeConnectionListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Register for connection event and connect to print service.
     Verify that a connection event is fired.
     Then disconnect and verify that a connection event is fired.
     **/
    public void Var002()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.SIGNON);
            sys.disconnectService(AS400.SIGNON);

            int serv = AS400.PRINT;
            String failMsg = "";

            sys.addConnectionListener(this);
            try
            {
                resetState();
                sys.connectService(serv);
                failMsg += verifyConnect(sys, serv);

                resetState();
                sys.disconnectService(serv);
                failMsg += verifyDisconnect(sys, serv);

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeConnectionListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Register for connection event and connect to command service.
     Verify that a connection event is fired.
     Then disconnect and verify that a connection event is fired.
     **/
    public void Var003()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.SIGNON);
            sys.disconnectService(AS400.SIGNON);

            int serv = AS400.COMMAND;
            String failMsg = "";

            sys.addConnectionListener(this);
            try
            {
                resetState();
                sys.connectService(serv);
                failMsg += verifyConnect(sys, serv);

                resetState();
                sys.disconnectService(serv);
                failMsg += verifyDisconnect(sys, serv);

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeConnectionListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Register for connection event and connect to data queues service.
     Verify that a connection event is fired.
     Then disconnect and verify that a connection event is fired.
     **/
    public void Var004()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.SIGNON);
            sys.disconnectService(AS400.SIGNON);

            int serv = AS400.DATAQUEUE;
            String failMsg = "";

            sys.addConnectionListener(this);
            try
            {
                resetState();
                sys.connectService(serv);
                failMsg += verifyConnect(sys, serv);

                resetState();
                sys.disconnectService(serv);
                failMsg += verifyDisconnect(sys, serv);

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeConnectionListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Register for connection event and connect to database service.
     Verify that a connection event is fired.
     Then disconnect and verify that a connection event is fired.
     **/
    public void Var005()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.SIGNON);
            sys.disconnectService(AS400.SIGNON);

            int serv = AS400.DATABASE;
            String failMsg = "";

            sys.addConnectionListener(this);
            try
            {
                resetState();
                sys.connectService(serv);
                failMsg += verifyConnect(sys, serv);

                resetState();
                sys.disconnectService(serv);
                failMsg += verifyDisconnect(sys, serv);

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeConnectionListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Register for connection event and connect to record access service.
     Verify that a connection event is fired.
     Then disconnect and verify that a connection event is fired.
     **/
    public void Var006()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.SIGNON);
            sys.disconnectService(AS400.SIGNON);

            int serv = AS400.RECORDACCESS;
            String failMsg = "";

            sys.addConnectionListener(this);
            try
            {
                resetState();
                sys.connectService(serv);
                failMsg += verifyConnect(sys, serv);

                resetState();
                sys.disconnectService(serv);
                failMsg += verifyDisconnect(sys, serv);

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeConnectionListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Register for connection event and connect to all services.
     Call disconnectAll() and verify that connection events are fired for all connected services.
     **/
    public void Var007()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);

            sys.addConnectionListener(this);
            try
            {
                resetState();

                sys.connectService(AS400.FILE);
                sys.connectService(AS400.PRINT);
                sys.connectService(AS400.COMMAND);
                sys.connectService(AS400.DATAQUEUE);
                sys.connectService(AS400.DATABASE);
                sys.connectService(AS400.RECORDACCESS);

                sys.disconnectAllServices();

                if (connectCount == disconnectCount)
                {
                    succeeded();
                }
                else
                {
                    failed("mismatching connect/disconnect events");
                }
            }
            finally
            {
                sys.removeConnectionListener(this);
                sys.close(); 
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Register a connection listener and submit a command.
     Verify that a connection event is received.
     Disconnect and verify that a disconnect event is received.
     **/
    public void Var008()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.SIGNON);
            sys.disconnectService(AS400.SIGNON);

            int serv = AS400.COMMAND;
            String failMsg = "";

            sys.addConnectionListener(this);
            try
            {
                resetState();
                CommandCall cmd = new CommandCall(sys);
                cmd.run("QSYS/crtlib fred");
                if (onAS400_ && isNative_ && isLocal_ && !mustUseSockets_)
                {
                    failMsg += verifyNoEvent();
                }
                else
                {
                    failMsg += verifyConnect(sys, serv);
                }

                resetState();
                sys.disconnectService(serv);
                if (onAS400_ && isNative_ && isLocal_ && !mustUseSockets_)
                {
                    failMsg += verifyNoEvent();
                }
                else
                {
                    failMsg += verifyDisconnect(sys, serv);
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeConnectionListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Register for connection event and connect to the command service.
     Connect again and verify that a connection event is not received.
     Disconnect and verify that a disconnect event is received.
     **/
    public void Var009()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.SIGNON);
            sys.disconnectService(AS400.SIGNON);

            int serv = AS400.COMMAND;
            String failMsg = "";

            sys.addConnectionListener(this);
            try
            {
                resetState();
                sys.connectService(serv);
                failMsg += verifyConnect(sys, serv);

                resetState();
                sys.connectService(serv);
                failMsg += verifyNoEvent();

                resetState();
                sys.disconnectService(serv);
                failMsg += verifyDisconnect(sys, serv);

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeConnectionListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Register for connection event and connect to command service.
     Remove the listener and connect again.
     Verify that a connect event is not received.
     Disconnect and verify a disconnect event is not received.
     **/
    public void Var010()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.SIGNON);
            sys.disconnectService(AS400.SIGNON);

            int serv = AS400.COMMAND;
            String failMsg = "";

            sys.addConnectionListener(this);

            resetState();
            sys.connectService(serv);
            failMsg += verifyConnect(sys, serv);

            sys.removeConnectionListener(this);
            resetState();
            sys.connectService(serv);
            failMsg += verifyNoEvent();

            resetState();
            sys.disconnectService(serv);
            failMsg += verifyNoEvent();

            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
