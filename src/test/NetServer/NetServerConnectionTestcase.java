///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NetServerConnectionTestcase.java
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

import java.util.Hashtable; import java.util.Vector;

import test.JCIFSUtility;
import test.PasswordVault;
import test.Testcase;

/** 
 Testcase NetServerConnectionTestcase.
**/
@SuppressWarnings("deprecation")

public class NetServerConnectionTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NetServerConnectionTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NetServerTest.main(newArgs); 
   }
    
    // Private data.
    JCIFSUtility jcifs; 
    /**
     Constructor.
     **/
    public NetServerConnectionTestcase(AS400 systemObject, 
                             Hashtable<String,Vector<String>> namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream, 
                             
                             String password,
                             AS400 pwrSys,
			     String pwrUserid,
			     String pwrPassword)
    {
        super(systemObject, "NetServerConnectionTestcase", namesAndVars, runMode, fileOutputStream, password);

        pwrSys_ = pwrSys;
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
	// Establish a connection using jcifs
      lockSystem("NETSVR",600); 

      
	jcifs = new JCIFSUtility(pwrSys_.getSystemName(), pwrSysUserID_, pwrSysEncryptedPassword_); 

	
       NetServer ns = new NetServer(systemObject_);
       
       // Use must have *IOSYSCFG special authority or authority to the
       // QIBM_IOSYSCFG_VIEW function usage identifier.

       
       CommandCall c = new CommandCall(pwrSys_);
       c.run("QSYS/CHGFCNUSG FCNID(QIBM_IOSYSCFG_VIEW) USER("+systemObject_.getUserId()+") USAGE(*ALLOWED)"); 
       
       
       ResourceList connectionList = ns.listSessionConnections();
       connectionList.waitForComplete();
       if (connectionList.getListLength() == 0) {
	       throw new IllegalStateException("No connections are active.  To start a connection: Signon to any PC and use Network Neighborhood to map a drive to \\\\" + systemObject_.getSystemName() + "\\ROOT.   Alternatively jcifs should have established a session");
       }
          super.setup(); 
    }



    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {

	jcifs.close(); 
        unlockSystem(); 
        super.cleanup(); 
    }


    /**
    NetServerConnection()
    **/
    public void Var001()
    {
        try 
        {
            NetServer ns = new NetServer(systemObject_);
            ResourceList connectionList = ns.listSessionConnections();
            connectionList.waitForComplete();
            NetServerConnection connection = null; 
            for (int i = 0; i < connectionList.getListLength(); ++i)
            {
               connection = (NetServerConnection)connectionList.resourceAt(i);
            }
            assertCondition(true, "last connection = "+connection);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeValue(NetServerConnection.CONNECT_TIME) 
     **/
    public void Var002()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           ResourceList connectionList = ns.listSessionConnections();
           connectionList.waitForComplete();
           NetServerConnection connection = (NetServerConnection)connectionList.resourceAt(0);

           if (((Integer)connection.getAttributeValue(NetServerConnection.CONNECT_TIME)).intValue() >= 0)
              succeeded();
            else
              failed("NetServerConnection connect time is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServerConnection.FILES_OPEN_COUNT) 
     **/
    public void Var003()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           ResourceList connectionList = ns.listSessionConnections();
           connectionList.waitForComplete();
           NetServerConnection connection = (NetServerConnection)connectionList.resourceAt(0);

           if (((Integer)connection.getAttributeValue(NetServerConnection.FILES_OPEN_COUNT)).intValue() >= 0)
              succeeded();
            else
              failed("NetServerConnection files open count is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServerConnection.NAME) 
     **/
    public void Var004()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           ResourceList connectionList = ns.listSessionConnections();
           connectionList.waitForComplete();
           NetServerConnection connection = (NetServerConnection)connectionList.resourceAt(0);

           if (((String)connection.getAttributeValue(NetServerConnection.NAME)).length() > 0)
              succeeded();
            else
              failed("NetServerConnection name is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServerConnection.TYPE) 
     **/
    public void Var005()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           ResourceList connectionList = ns.listSessionConnections();
           connectionList.waitForComplete();
           NetServerConnection connection = (NetServerConnection)connectionList.resourceAt(0);

           if (((Integer)connection.getAttributeValue(NetServerConnection.TYPE)).intValue() == NetServerConnection.TYPE_DISK_DRIVE.intValue() ||
               ((Integer)connection.getAttributeValue(NetServerConnection.TYPE)).intValue() == NetServerConnection.TYPE_SPOOLED_OUTPUT_QUEUE.intValue() )
              succeeded();
            else
              failed("NetServer connect time is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServerConnection.USER) 
     **/
    public void Var006()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           ResourceList connectionList = ns.listSessionConnections();
           connectionList.waitForComplete();
           NetServerConnection connection = (NetServerConnection)connectionList.resourceAt(0);
           
           if (((String)connection.getAttributeValue(NetServerConnection.USER)).length() > 0 )
              succeeded();
            else
              failed("NetServerConnection user is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeValue(NetServerConnection.TYPE) 
     **/
    public void Var007()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           ResourceList connectionList = ns.listSessionConnections();
           connectionList.waitForComplete();
           NetServerConnection connection = (NetServerConnection)connectionList.resourceAt(0);

           connection.refreshAttributeValues();
           succeeded();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getID() 
    **/
    public void Var008()
    {
        try 
        {
            NetServer netser = new NetServer(systemObject_);
            ResourceList connectionList = netser.listSessionConnections();
            
            NetServerConnection connection = (NetServerConnection)connectionList.resourceAt(0);
            assertCondition(connection.getID() >= 0);
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

}
