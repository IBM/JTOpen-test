///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NetServerSessionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.NetServer;


import java.io.*;

import com.ibm.as400.access.*;
import com.ibm.as400.resource.*;

import java.util.Hashtable;

import test.JCIFSUtility;
import test.PasswordVault;
import test.Testcase;

/** 
 Testcase NetServerSessionTestcase.
**/
public class NetServerSessionTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NetServerSessionTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.INetServerTest.main(newArgs); 
   }
   ResourceList sessionList_;

   JCIFSUtility jcifs = null; 

    /**
     Constructor.
     **/
    public NetServerSessionTestcase(AS400 systemObject, 
                             Hashtable namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream, 
                             
                             String password, AS400 pwrSys,
				    String pwrUserid,
				    String pwrPassword)   //@A1C
    {
        super(systemObject, "NetServerSessionTestcase", namesAndVars, runMode, fileOutputStream, password);
        pwrSys_ = pwrSys;                                     //@A1A
	pwrSysUserID_ = pwrUserid;
	
	pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrPassword);
    }

    /**
    Performs setup needed before running variations.

    @exception Exception If an exception occurs.
    **/
    protected void setup ()
    throws Exception
    {
      lockSystem("NETSVR", 600);
      super.setup();

	jcifs = new JCIFSUtility(pwrSys_.getSystemName(), pwrSysUserID_, pwrSysEncryptedPassword_); 

       NetServer netser = new NetServer(pwrSys_);
       sessionList_ = netser.listSessions();

       if (sessionList_.getListLength() == 0)
          throw new IllegalStateException("No sessions are active.  To start a session: Signon to any PC and use Network Neighborhood to map a drive to \\\\" + systemObject_.getSystemName() + "\\ROOT , then open any file on the mapped drive.  \nNOTE: If you previously started a session, it may have been ended by NetServerSession.Var011.");
    }

    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {

	jcifs.close(); 
	      super.cleanup();
	      unlockSystem();
    }


  
    /**
    Create a NetServerSession()
    **/
    public void Var001()
    {
        try 
        {   
            NetServerSession session = (NetServerSession)sessionList_.resourceAt(0);
            assertCondition(true, "session = "+session); 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    
    /**
    listConnections()
    **/
    public void Var002()
    {
        try
        {
            NetServerSession session = (NetServerSession)sessionList_.resourceAt(0);
            ResourceList connectionsList = session.listConnections();
            if (connectionsList != null)
               succeeded();
            else
               failed("Null session list.");
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    
    /**
      getAttributeValue(NetServerSession.CONNECTION_COUNT) 
    **/
    public void Var003()
    {
        try
        {
           NetServerSession session = (NetServerSession)sessionList_.resourceAt(0);
           if (((Integer)session.getAttributeValue(NetServerSession.CONNECTION_COUNT)).intValue() == 1)
              succeeded();
           else
              failed("NetServerSession connection count is not valid.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    getName()
    **/
    public void Var004()
    {
        try 
        {
            NetServerSession session = (NetServerSession)sessionList_.resourceAt(0);
            String s = session.getName();

            assertCondition (s != null);
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
      getAttributeValue(NetServerSession.SESSION_TIME) 
    **/
    public void Var005()
    {
        try
        {
           NetServerSession session = (NetServerSession)sessionList_.resourceAt(0);
           if (((Integer)session.getAttributeValue(NetServerSession.SESSION_TIME)).intValue() >= 0)
              succeeded();
           else
              failed("NetServerSession session time is not valid.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      getAttributeValue(NetServerSession.FILES_OPEN_COUNT) 
    **/
    public void Var006()
    {
        try
        {
           NetServerSession session = (NetServerSession)sessionList_.resourceAt(0);
           if (((Integer)session.getAttributeValue(NetServerSession.FILES_OPEN_COUNT)).intValue() >= 1)
              succeeded();
           else
              failed("NetServerSession files open count is not valid.  Make sure you have a drive mapped and a file open on the drive.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      getAttributeValue(NetServerSession.IDLE_TIME) 
    **/
    public void Var007()
    {
        try
        {
           NetServerSession session = (NetServerSession)sessionList_.resourceAt(0);
           if (((Integer)session.getAttributeValue(NetServerSession.IDLE_TIME)).intValue() >= 0)
              succeeded();
           else
              failed("NetServerSession idle time is not valid.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      getAttributeValue(NetServerSession.IS_ENCRYPT_PASSWORD) 
    **/
    public void Var008()
    {
        try
        {
           NetServerSession session = (NetServerSession)sessionList_.resourceAt(0);
           if (((Boolean)session.getAttributeValue(NetServerSession.IS_ENCRYPT_PASSWORD)).booleanValue() == true ||
               ((Boolean)session.getAttributeValue(NetServerSession.IS_ENCRYPT_PASSWORD)).booleanValue() == false)
              succeeded();
           else
              failed("NetServerSession is encrypt password is not valid.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      getAttributeValue(NetServerSession.IS_GUEST) 
    **/
    public void Var009()
    {
        try
        {
           NetServerSession session = (NetServerSession)sessionList_.resourceAt(0);
           if (((Boolean)session.getAttributeValue(NetServerSession.IS_GUEST)).booleanValue() == true ||
               ((Boolean)session.getAttributeValue(NetServerSession.IS_GUEST)).booleanValue() == false)
              succeeded();
           else
              failed("NetServerSession is guest is not valid.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      getAttributeValue(NetServerSession.USER) 
    **/
    public void Var010()
    {
        try
        {
           NetServerSession session = (NetServerSession)sessionList_.resourceAt(0);
           if (((String)session.getAttributeValue(NetServerSession.USER)).length() >= 0)
              succeeded();
           else
              failed("NetServerSession user is not valid.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    end()
     Note: This variation ends the session that the user was instructed to start.
     That's why this variation must be postponed until after the other variations.
     This variation has been copied to NetServerTestcase as an unattended variation, into the final section where the NetServer.end() variations get called.
    **/
    public void Var011(int runMode)
    {
     if (runMode != ATTENDED && runMode != BOTH) {
       notApplicable("Attended testcase");  // In unattended runs, we need to avoid ending the prestarted session, to avoid causing other testcases to fail.
       return;
     }
        try 
        {
          // Create connection with IOSYSCFG auth (i.e. pwrSys_) for QZLSENSS() API
          NetServer netserPwr = new NetServer(pwrSys_);             //@A1A
          ResourceList sessionListPwr = netserPwr.listSessions();   //@A1A

            NetServerSession session = (NetServerSession)sessionListPwr.resourceAt(0);//@A1C
            session.end();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

}
