///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NetServerTestcase.java
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

import test.JCIFSUtility;
import test.NetServerTest;
import test.PasswordVault;
import test.Testcase;

import java.util.Hashtable;
import java.util.StringTokenizer;            //@A2A


/** 
  Testcase NetServerTestcase.
**/
public class NetServerTestcase extends Testcase
{
     
    private String original_NetServer_Name;
    private String original_NetServer_NamePending;

    JCIFSUtility jcifs = null; 
    /**
     Constructor.
     **/
    public NetServerTestcase(AS400 systemObject, 
                             Hashtable namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream, 
                             
                             String password,
                             AS400 pwrSys,
			     String pwrUserid,
			     String pwrPassword)
    {
        super(systemObject, "NetServerTestcase", namesAndVars, runMode, fileOutputStream, password);
        pwrSys_ = pwrSys;
	pwrSysUserID_ = pwrUserid;
	
  pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrPassword);
        if(pwrSys == null)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");


        //System.out.println("namesAndVars keys:");
        //for (Enumeration enum = namesAndVars.keys(); enum.hasMoreElements(); ) {
        //  System.out.println((String)enum.nextElement());
        //}
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
	
       jcifs = new JCIFSUtility(pwrSys_.getSystemName().toUpperCase(), pwrSysUserID_, pwrSysEncryptedPassword_); 
	

       NetServer server = new NetServer(pwrSys_);   //@A1C
       String systemName = systemObject_.getSystemName().toUpperCase();
       int dotIndex = systemName.indexOf('.'); 
       if (dotIndex > 0) {
         systemName = systemName.substring(0, dotIndex); 
       }
       original_NetServer_Name = new String(systemName);
       original_NetServer_NamePending = new String(systemName);
       if (original_NetServer_Name.equals("LOCALHOST"))     //Start @A2A
       {
	   // The AS400.getSystemName() method returns "localhost" when the client
	   // is on the same system as the server (e.g. running the testcase on i5)
	   // But "localhost" is not valid for a NetServer name in CHGNSVA.
	   // Therefore, we need to extract the real system name.
	   String localSysName = java.net.InetAddress.getLocalHost().getHostName();
	   // The above localSysName is of the format "SYSTEM.DOMAIN.COM"
	   // so we need to extract the system name as the first token of the string
	   StringTokenizer st = new StringTokenizer(localSysName, ".");
	   original_NetServer_Name = st.nextToken().toUpperCase();
	   original_NetServer_NamePending = original_NetServer_Name;  
       }                                                      //End @A2A
       server.setAttributeValue(NetServer.NAME_PENDING, original_NetServer_NamePending);
       server.commitAttributeChanges();
       server.start(true);
       waitForStart(server); 
    }


    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
       NetServer server = new NetServer(pwrSys_);   //@A1C
       server.setAttributeValue(NetServer.NAME_PENDING, original_NetServer_NamePending);
       server.commitAttributeChanges();
       server.start(true);
       waitForStart(server); 

       jcifs.close(); 
       super.cleanup();
       unlockSystem();
    }

    
    /**
    Construct a NetServer with no parameters.
     
    NetServer() 
    **/
    public void Var001()
    {
        try
        {
            NetServer netser = new NetServer();
            assertCondition(true, "netser="+netser); 
            
        }
        catch (Exception e)
        {
            failed(e, "Wrong exception info.");
        }
    }

    /**
    Construct a NetServer with parameters.
    
    NetServer() 
    **/
    public void Var002()
    {
        try
        {
            NetServer netser = new NetServer(systemObject_);
            assertCondition(true, "netser="+netser); 
            
        }
        catch (Exception e)
        {
            failed(e, "Wrong exception info.");
        }
    }

    /**
    Construct a NetServer passing a null for the request parm.
    A NullPointerException should be thrown.

    NetServer(null)
    **/
    public void Var003()
    {
        try
        {
            NetServer netser = new NetServer(null);
            failed("No exception"+netser);
        }
        catch (Exception e)
        {
            assertExceptionIs (e, "NullPointerException", "system");
        }
    }

    /**
    commitAttributeChanges() - Should fail when there is no system or user set.
    **/
    public void Var004()
    {
        try 
        {
            NetServer netser = new NetServer(); 
            netser.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) 
        {
            assertExceptionIsInstanceOf (e, "java.lang.Exception");
        }
    }

    /**
    commitAttributeChanges() - When the system is bogus.
    **/
    public void Var005()
    {
        try 
        {
            AS400 system = new AS400("Toolbox", "is", "cool");
            system.setGuiAvailable(false);
            NetServer netser = new NetServer(system);
            netser.setAttributeValue(NetServer.NAME_PENDING, new String());
            netser.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) 
        {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }
    
    /**
    commitAttributeChanges() - When the user does not exist.
    **/
    public void Var006()
    {
        try 
        {
            NetServer netser = new NetServer(systemObject_);
            netser.setAttributeValue(NetServer.NAME_PENDING, new String());
            netser.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) 
        {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
    commitAttributeChanges() - Should do nothing when no change has been made.
    **/
    public void Var007()
    {
        try 
        {
            NetServer netser = new NetServer(systemObject_);
            String name = (String)netser.getAttributeValue(NetServer.NAME);
            netser.commitAttributeChanges();
            String name2 = (String)netser.getAttributeValue(NetServer.NAME);
            assertCondition(name2.equals(name));
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    commitAttributeChanges() - Should commit the change when 2 changes have been made.
    **/    
    public void Var008()
    {
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            
            String newName = new String("TOOLBOX");
            netser.setAttributeValue(NetServer.NAME_PENDING, newName);
            netser.setAttributeValue(NetServer.ALLOW_SYSTEM_NAME_PENDING, Boolean.TRUE);
            netser.commitAttributeChanges();
            
            NetServer netser2 = new NetServer(systemObject_);
            String name2 = (String)netser.getAttributeValue(NetServer.NAME_PENDING);
            Boolean allowSysName2 = (Boolean)netser.getAttributeValue(NetServer.ALLOW_SYSTEM_NAME_PENDING);
            
            assertCondition((name2.equals(newName)) && (allowSysName2.equals(Boolean.TRUE)), "netser2="+netser2);

            cleanup();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }
 
    /**
    getAttributeUnchangedValue() - When there is no server specified.
    **/
    public void Var009()
    {
        try 
        {
            NetServer netser = new NetServer();
            Object value = netser.getAttributeUnchangedValue(NetServer.NAME_PENDING);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) 
        {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }

    /**
    getAttributeUnchangedValue() - Pass null.
    **/
    public void Var010()
    {
        try 
        {
            NetServer netser = new NetServer(systemObject_);
            Object value = netser.getAttributeUnchangedValue(null);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) 
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
    getAttributeUnchangedValue() - Pass an invalid attribute ID.
    **/
    public void Var011()
    {
        try 
        {
            NetServer netser = new NetServer(systemObject_);
            Object value = netser.getAttributeUnchangedValue(new AS400());
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) 
        {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }

    /**
    getAttributeUnchangedValue() - Pass an attribute ID, whose value has not been referenced.
    **/
    public void Var012()
    {
        try 
        {
            NetServer netser2 = new NetServer(systemObject_);
            
            String value = (String)netser2.getAttributeUnchangedValue(NetServer.NAME_PENDING);
            
            assertCondition(value.equals(original_NetServer_NamePending));
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeUnchangedValue() - Pass an attribute ID, whose value has been referenced, but not changed.
    **/
    public void Var013()
    {
        try 
        {
            NetServer netser = new NetServer(systemObject_);
            netser.getAttributeValue(NetServer.NAME_PENDING);
            String value = (String)netser.getAttributeUnchangedValue(NetServer.NAME_PENDING);
            
            assertCondition(value.equals(original_NetServer_NamePending));
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed, but not committed.
    **/
    public void Var014()
    {
        try 
        {
            NetServer netser = new NetServer(systemObject_);
            netser.setAttributeValue(NetServer.NAME_PENDING, new String("TOOLBOX"));
            String value = (String)netser.getAttributeUnchangedValue(NetServer.NAME_PENDING);
            
            assertCondition(value.equals(original_NetServer_NamePending));
            cleanup();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed, not committed, but refreshed.
    **/
    public void Var015()
    {
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            netser.setAttributeValue(NetServer.NAME_PENDING, new String("TOOLBOX"));
            netser.refreshAttributeValues();

            String value = (String)netser.getAttributeUnchangedValue(NetServer.NAME);

            assertCondition(value.equals(original_NetServer_Name));
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed and committed.
    **/
    public void Var016()
    {
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            netser.setAttributeValue(NetServer.NAME_PENDING, new String("TOOLBOX"));
            netser.commitAttributeChanges();
            String value = (String)netser.getAttributeUnchangedValue(NetServer.NAME_PENDING);
            
            assertCondition(value.equals("TOOLBOX"));

            cleanup();
            
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }
 
    /**
    isStarted() - Call when the server has never been started and not enough
    information is available to start it.
    **/
    public void Var017()
    {
        try 
        {
            NetServer netser = new NetServer();
            boolean started = netser.isStarted();
            failed ("Didn't throw exception"+started);
        }
        catch(Exception e) 
        {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }

    /**
    start() - Call when there is not enough information to start the server.
    **/
    public void Var018()
    {
        try 
        {
            NetServer netser = new NetServer();
            netser.start();
            failed ("Didn't throw exception");
        }
        catch(Exception e) 
        {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }

    /**
    start() - Call when a bogus system is specified.
    **/
    public void Var019()
    {
        try 
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            bogus.setGuiAvailable(false);
            NetServer netser = new NetServer(bogus);
            netser.start();
            failed ("Didn't throw exception");
        }
        catch(Exception e) 
        {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }

    
    /**
    start() - Call when there is not enough information to start the server.
    **/
    public void Var020()
    {
        try 
        {
            NetServer netser = new NetServer();
            netser.start();

            failed ("Didn't throw exception");
        }
        catch(Exception e) 
        {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }

    
    /**
    refreshAttributeValues() - Call when there is not enough information to implicitly start the server.
    **/
    public void Var021()
    {
        try 
        {
            NetServer netser = new NetServer();
            netser.refreshAttributeValues();
            failed ("Didn't throw exception");
        }
        catch(Exception e) 
        {
            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
        }
    }

    /**
    refreshAttributeValues() - Call when the server has been started before, but nothing done to it.
    **/
    public void Var022()
    {
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            netser.start();
	    waitForStart(netser); 
     
            netser.refreshAttributeValues();
            succeeded();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    refreshAttributeValues() - Call when the server has been started, and an attribute was set.
    **/
    public void Var023()
    {
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            if(!netser.isStarted())
            {
               netser.start();
	       waitForStart(netser); 
              
            }
            String name = new String("Toolbox");
            netser.setAttributeValue(NetServer.NAME_PENDING, name);
            netser.commitAttributeChanges();
            netser.refreshAttributeValues();
            succeeded();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    
    /**
    toString()
    **/
    public void Var024()
    {
        try 
        {
            cleanup();

            NetServer netser = new NetServer(pwrSys_);   //@A1C
            if(!netser.isStarted())
            {
               netser.start();
	       waitForStart(netser); 

            }
            Object name = netser.getAttributeValue(NetServer.NAME_PENDING);
            assertCondition((name.toString()).equalsIgnoreCase(original_NetServer_NamePending)); //@A2C
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    toString() - For a NetServer whose properties have not been set, this should return the default toString.
    **/
    public void Var025()
    {
        try 
        {
            NetServer netser = new NetServer();  
            assertCondition(netser.toString().length() > 0);
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    listFileShares()
    **/
    public void Var026()
    {
        try
        {
            NetServer netser = new NetServer();
            ResourceList fileSharesList = netser.listFileShares();
            failed("No exception"+fileSharesList);
        }
        catch(Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
        }
    }

    /**
    Construct a NetServer with system and parm.
    ListFileShares held in NetServer.
    Get the string to verify results.
   
    NetServer(system)
    NetServer::listFileShares()
    **/
    public void Var027()
    {
        try
        {
            NetServer netser = new NetServer(systemObject_);
            ResourceList fileSharesList = netser.listFileShares();
            long listLength = fileSharesList.getListLength();
            String[] s = new String[(int)listLength];
            StringBuffer st = new StringBuffer();
            for(int i=0; i<listLength; i++)
            {
                NetServerFileShare share = (NetServerFileShare)fileSharesList.resourceAt(i);
                s[i] = share.getName() + ": " + (String)share.getAttributeValue(NetServerFileShare.PATH) + ": " +
                       (String)share.getAttributeValue(NetServerFileShare.DESCRIPTION);
                if(i > 0)
                    st.append("\n"+s[i]);
                else
                    st.append(s[i]);
            }
           
	    if(st.toString().indexOf("ROOT: /: ") != -1) {
                succeeded();
	    } else {
		// If root is not mapped look for  QIBM: /QIBM
		if(st.toString().indexOf("QIBM: /QIBM: ") != -1) {
		    succeeded();

		} else {
		    failed("Looking for 'ROOT: /: '  or 'QIBM: /QIBM' -- Incorrect string returned.\n" + st);
		}
	    }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    Construct a NetServer with system and parm.
    ListFileShares held in NetServer.
    Get the string to verify results.
   
    NetServer(system)
    NetServer::listFileShares(shareName)
    **/
    public void Var028()
    {
        try
        {
            NetServer netser = new NetServer(systemObject_);
            ResourceList fileSharesList = netser.listFileShares("ROOT");
            long listLength = fileSharesList.getListLength();
            String[] s = new String[(int)listLength];
            StringBuffer st = new StringBuffer();
            for(int i=0; i<listLength; i++)
            {
                NetServerFileShare share = (NetServerFileShare)fileSharesList.resourceAt(i);
                s[i] = share.getName() + ": " + (String)share.getAttributeValue(NetServerFileShare.PATH) + ": " +
                       (String)share.getAttributeValue(NetServerFileShare.DESCRIPTION);
                if(i > 0)
                    st.append("\n"+s[i]);
                else
                    st.append(s[i]);
            }
           
	    if(st.toString().startsWith("ROOT: /: ")) {
                succeeded();
	    } else {
		// If root is not mapped look for  QIBM: /QIBM
		if(st.toString().indexOf("QIBM: /QIBM: ") != -1) {
		    succeeded();

		} else {
		    failed("Looking for 'ROOT: /: '  or 'QIBM: /QIBM' -- Incorrect string returned.\n" + st);
		}
	    }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    listPrintShares()
    **/
    public void Var029()
    {
        try
        {
            NetServer netser = new NetServer();
            ResourceList printSharesList = netser.listPrintShares();
            failed("No exception"+printSharesList );
        }
        catch(Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
        }
    }

    /**
    Construct a NetServer with system and parm.
    ListPrintShares held in NetServer.
    Get the string to verify results.
    
    NetServer(system)
    NetServer::listPrintShares()
    **/
    public void Var030()
    {
        try
        {
            NetServer netser = new NetServer(systemObject_);
            ResourceList printSharesList = netser.listPrintShares();
            long listLength = printSharesList.getListLength();
            String[] s = new String[(int)listLength];
            String st = new String();
            for(int i=0; i<listLength; i++)
            {
                NetServerPrintShare share = (NetServerPrintShare)printSharesList.resourceAt(i);
                s[i] = share.getName() + ": " + (String)share.getAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_NAME) + ": " + (String)share.getAttributeValue(NetServerPrintShare.DESCRIPTION);
                if(i > 0)
                    st = st + s[i];
                else
                    st = s[i];
            }
           
            if(st.length() >= 0)
                succeeded();
            else
                failed("Incorrect string returned.\n" + st);
                
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    Construct a NetServer with system and parm.
    ListPrintShares held in NetServer.
    Get the string to verify results.
    
    NetServer(system)
    NetServer::listPrintShares(shareName)
    **/
    public void Var031()
    {
        try
        {
            NetServer netser = new NetServer(systemObject_);
            ResourceList printSharesList = netser.listPrintShares("BOGUS");
            long listLength = printSharesList.getListLength();
            String[] s = new String[(int)listLength];
            String st = new String();
            for(int i=0; i<listLength; i++)
            {
                NetServerPrintShare share = (NetServerPrintShare)printSharesList.resourceAt(i);
                s[i] = share.getName() + ": " + (String)share.getAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_NAME) + ": " + (String)share.getAttributeValue(NetServerPrintShare.DESCRIPTION);
                if(i > 0)
                    st = st + s[i];
                else
                    st = s[i];
            }
           
            if(st.equals(""))
                succeeded();
            else
                failed("Incorrect string returned.\n" + st);
                
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    listSessionConnections()
    **/
    public void Var032()
    {
        try
        {
            NetServer netser = new NetServer();
            ResourceList sessionConnectionsList = netser.listSessionConnections();
            failed("No exception"+sessionConnectionsList);
        }
        catch(Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
        }
    }

    /**
    Construct a NetServer with system and parm.
    ListSessionConnections held in NetServer.
    Get the string to verify results.
   
    NetServer(system)
    NetServer::listSessionConnections()
    **/
    public void Var033()
    {
        try
        {
            NetServer netser = new NetServer(systemObject_);
            ResourceList sessionConnectionsList = netser.listSessionConnections();
            long listLength = sessionConnectionsList.getListLength();
            String[] s = new String[(int)listLength];
            StringBuffer st = new StringBuffer();
            for(int i=0; i<listLength; i++)
            {
                NetServerConnection connection = (NetServerConnection)sessionConnectionsList.resourceAt(i);
                s[i] = connection.getID() + ": " + (String)connection.getAttributeValue(NetServerConnection.NAME) + ": " +
                       (String)connection.getAttributeValue(NetServerConnection.USER);
                st.append(s[i]);
                //if(i > 0)
                //    st = st + s[i];
                //else
                //    st = s[i];
            }

            String result = st.toString().trim();
            if(result.length() > 0)
                succeeded();
            else
                failed("listSessionConnections() returned no information.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    listSessions()
    **/
    public void Var034()
    {
        try
        {
            NetServer netser = new NetServer();
            ResourceList sessionsList = netser.listSessions();
            failed("No exception"+sessionsList);
        }
        catch(Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
        }
    }


    /**
    Construct a NetServer with system and parm.
    ListSessions held in NetServer.
    Get the string to verify results.
   
    NetServer(system)
    NetServer::listSessions()
    **/
    public void Var035()
    {
        try
        {
            NetServer netser = new NetServer(systemObject_);
            ResourceList sessionList = netser.listSessions();
            long listLength = sessionList.getListLength();
            String[] s = new String[(int)listLength];
            StringBuffer st = new StringBuffer();
            for(int i=0; i<listLength; i++)
            {
                NetServerSession session = (NetServerSession)sessionList.resourceAt(i);
                s[i] = session.getName() + ": " + (String)session.getAttributeValue(NetServerSession.USER);
                st.append(s[i]);
            }
           
            String result = st.toString().trim();
            if(result.length() > 0)
                succeeded();
            else
                failed("listSessions() returned no information.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    listShareConnections()
    **/
    public void Var036()
    {
        try
        {
            NetServer netser = new NetServer();
            ResourceList shareConnectionsList = netser.listShareConnections();
            failed("No exception"+shareConnectionsList);
        }
        catch(Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
        }
    }


    /**
    Construct a NetServer with system and parm.
    ListShareConnections held in NetServer.
    Get the string to verify results.
   
    NetServer(system)
    NetServer::listShareConnections()
    **/
    public void Var037()
    {
        try
        {
            NetServer netser = new NetServer(systemObject_);
            ResourceList shareConnectionsList = netser.listShareConnections();
            long listLength = shareConnectionsList.getListLength();
            String[] s = new String[(int)listLength];
            StringBuffer st = new StringBuffer();
            for(int i=0; i<listLength; i++)
            {
                NetServerConnection connection = (NetServerConnection)shareConnectionsList.resourceAt(i);
                s[i] = connection.getID() + ": " + (String)connection.getAttributeValue(NetServerConnection.NAME) + ": " +
                       (String)connection.getAttributeValue(NetServerConnection.USER);
                st.append(s[i]);
            }
           
            String result = st.toString().trim();
            if(result.length() > 0)
                succeeded();
            else
                failed("listShareConnections() returned no information.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    getAttributeValue(NetServer.ALLOW_SYSTEM_NAME) 
     **/
    public void Var038()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           if (((Boolean)ns.getAttributeValue(NetServer.ALLOW_SYSTEM_NAME)).booleanValue() != true &&
               ((Boolean)ns.getAttributeValue(NetServer.ALLOW_SYSTEM_NAME)).booleanValue() != false )
               failed("NetServer allow system name is not valid.");        
            else
               succeeded();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.ALLOW_SYSTEM_NAME_PENDING)  
    **/
    public void Var039()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            ns.setAttributeValue(NetServer.ALLOW_SYSTEM_NAME_PENDING, new Boolean(true));
            ns.commitAttributeChanges();
            
            if (((Boolean)ns.getAttributeValue(NetServer.ALLOW_SYSTEM_NAME_PENDING)).booleanValue() != true)
               failed("NetServer allow system name pending is invalid.");        
            else
               succeeded();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    
    /**
    getAttributeValue(NetServer.AUTOSTART) 
     **/
    public void Var040()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           if (((Boolean)ns.getAttributeValue(NetServer.AUTOSTART)).booleanValue() != true &&
               ((Boolean)ns.getAttributeValue(NetServer.AUTOSTART)).booleanValue() != false )
               failed("NetServer auto start is not valid.");        
            else
               succeeded();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.AUTOSTART)  
    **/
    public void Var041()
    {
        try 
        {
            NetServer ns = new NetServer(systemObject_);
            ns.setAttributeValue(NetServer.AUTOSTART, new Boolean(true));
            ns.commitAttributeChanges();
            
            if (((Boolean)ns.getAttributeValue(NetServer.AUTOSTART)).booleanValue() != true)
               failed("NetServer autostart is invalid.");        
            else
               succeeded();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServer.BROWSING_INTERVAL) 
     **/
    public void Var042()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           if (((Integer)ns.getAttributeValue(NetServer.BROWSING_INTERVAL)).intValue() >= 0  )
              succeeded();
            else
              failed("NetServer browsing interval is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.BROWSING_INTERVAL_PENDING)  
    **/
    public void Var043()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            ns.setAttributeValue(NetServer.BROWSING_INTERVAL_PENDING, new Integer(20000));
            ns.commitAttributeChanges();
            
            if (((Integer)ns.getAttributeValue(NetServer.BROWSING_INTERVAL_PENDING)).intValue() != 20000)
               failed("NetServer browsing interval pending is invalid.");        
            else
               succeeded();

            ns.setAttributeValue(NetServer.BROWSING_INTERVAL_PENDING, new Integer(0));
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.CCSID_PENDING)  
    **/
    public void Var044()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            ns.setAttributeValue(NetServer.CCSID_PENDING, new Integer(424));
            ns.commitAttributeChanges();
            
            if (((Integer)ns.getAttributeValue(NetServer.CCSID_PENDING)).intValue() != 424)
               failed("NetServer ccsid pending is invalid.");        
            else
               succeeded();

            ns.setAttributeValue(NetServer.CCSID_PENDING, new Integer(0));
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServer.DESCRIPTION) 
     **/
    public void Var045()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           
           if (((String)ns.getAttributeValue(NetServer.DESCRIPTION)).length() > 0  )
              succeeded();
            else
              failed("NetServer description length is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.DESCRIPTION_PENDING)  
    **/
    public void Var046()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            String orig = (String)ns.getAttributeValue(NetServer.DESCRIPTION_PENDING);
            ns.setAttributeValue(NetServer.DESCRIPTION_PENDING, new String("Toolbox NetServer Description"));
            ns.commitAttributeChanges();
            
            if (((String)ns.getAttributeValue(NetServer.DESCRIPTION_PENDING)).equals("Toolbox NetServer Description"))
               succeeded();
            else
               failed("NetServer description pending is invalid.");        

            ns.setAttributeValue(NetServer.DESCRIPTION_PENDING, orig);
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServer.DOMAIN) 
     **/
    public void Var047()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           if (((String)ns.getAttributeValue(NetServer.DOMAIN)).length() > 0)
              succeeded();
            else
              failed("NetServer domain is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.DOMAIN_PENDING)  
    **/
    public void Var048()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            String orig = (String)ns.getAttributeValue(NetServer.DOMAIN_PENDING);
            ns.setAttributeValue(NetServer.DOMAIN_PENDING, systemObject_.getSystemName());
            ns.commitAttributeChanges();
            
            if (((String)ns.getAttributeValue(NetServer.DOMAIN_PENDING)).equals(systemObject_.getSystemName()))
               succeeded();
            else
               failed("NetServer domain pending is invalid.");        

            ns.setAttributeValue(NetServer.DOMAIN_PENDING, orig);
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServer.GUEST_USER_PROFILE) 
     **/
    public void Var049()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           
           if (((String)ns.getAttributeValue(NetServer.GUEST_USER_PROFILE)).length() >= 0  )
              succeeded();
            else
              failed("NetServer guest user profile length is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.GUEST_USER_PROFILE_PENDING)  
    **/
    public void Var050()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);

            CommandCall cmd = new CommandCall(pwrSys_);
            if (!cmd.run("CRTUSRPRF USRPRF(NETSRVTEST) PASSWORD(JTEAM1) TEXT('Toolbox test profile - Robb Wiedrich 3-3856')"))
               failed("Setup for this variation failed.");

            String orig = (String)ns.getAttributeValue(NetServer.GUEST_USER_PROFILE_PENDING);
            
            ns.setAttributeValue(NetServer.GUEST_USER_PROFILE_PENDING, "NETSRVTEST");
            ns.commitAttributeChanges();
            
            if (((String)ns.getAttributeValue(NetServer.GUEST_USER_PROFILE_PENDING)).equals("NETSRVTEST"))
               succeeded();
            else
               failed("NetServer guest user profile pending is invalid.");        

            ns.setAttributeValue(NetServer.GUEST_USER_PROFILE_PENDING, orig);
            ns.commitAttributeChanges();

            if (!cmd.run("DLTUSRPRF USRPRF(NETSRVTEST)"))
               System.out.println("DLTUSRPRF USRPRF(NETSRVTETS) - failed.  Manually delete profile on AS/400");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServer.IDLE_TIMEOUT) 
     **/
    public void Var051()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           
           if (((Integer)ns.getAttributeValue(NetServer.IDLE_TIMEOUT)).intValue() > 0 || 
              ((Integer)ns.getAttributeValue(NetServer.IDLE_TIMEOUT)).intValue() == -1)
              succeeded();
            else
              failed("NetServer idle timeout is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.IDLE_TIMEOUT_PENDING)  
    **/
    public void Var052()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            Integer orig = (Integer)ns.getAttributeValue(NetServer.IDLE_TIMEOUT_PENDING);
            ns.setAttributeValue(NetServer.IDLE_TIMEOUT_PENDING, new Integer(20000));
            ns.commitAttributeChanges();
            
            if (((Integer)ns.getAttributeValue(NetServer.IDLE_TIMEOUT_PENDING)).intValue() == 20000)
               succeeded();
            else
               failed("NetServer idle timeout pending is invalid.");        

            ns.setAttributeValue(NetServer.IDLE_TIMEOUT_PENDING, orig);
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServer.LOGON_SUPPORT) 
     **/
    public void Var053()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           
           if (((Boolean)ns.getAttributeValue(NetServer.LOGON_SUPPORT)).booleanValue() == true ||
               ((Boolean)ns.getAttributeValue(NetServer.LOGON_SUPPORT)).booleanValue() == false )
              succeeded();
            else
              failed("NetServer logon support is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.LOGON_SUPPORT_PENDING)  
    **/
    public void Var054()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            Boolean orig = (Boolean)ns.getAttributeValue(NetServer.LOGON_SUPPORT_PENDING);
            if (orig.booleanValue() == true)
               ns.setAttributeValue(NetServer.LOGON_SUPPORT_PENDING, new Boolean(false));
            else
               ns.setAttributeValue(NetServer.LOGON_SUPPORT_PENDING, new Boolean(true));

            ns.commitAttributeChanges();
            
            if (orig.booleanValue() == true)
            {
               if (((Boolean)ns.getAttributeValue(NetServer.LOGON_SUPPORT_PENDING)).booleanValue() == false)
                  succeeded();
               else
                  failed("NetServer logon support pending is invalid.");        
            }
            else
            {
               if (((Boolean)ns.getAttributeValue(NetServer.LOGON_SUPPORT_PENDING)).booleanValue() == true)
                  succeeded();
               else
                  failed("NetServer logon support pending is invalid.");        
            }

            ns.setAttributeValue(NetServer.LOGON_SUPPORT_PENDING, orig);
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getAttributeValue(NetServer.NAME) 
     **/
    public void Var055()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           
           if (((String)ns.getAttributeValue(NetServer.NAME)).length() > 0 )
              succeeded();
            else
              failed("NetServer name length is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.NAME_PENDING)  
    **/
    public void Var056()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            String orig = (String)ns.getAttributeValue(NetServer.NAME_PENDING);
            ns.setAttributeValue(NetServer.NAME_PENDING, "TOOLBOXPENDING");
            ns.commitAttributeChanges();
            
            if (((String)ns.getAttributeValue(NetServer.NAME_PENDING)).equals("TOOLBOXPENDING"))
               succeeded();
            else
               failed("NetServer name pending is invalid.");        

            ns.setAttributeValue(NetServer.NAME_PENDING, orig);
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServer.WINS_ENABLEMENT) 
     **/
    public void Var057()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           
           if (((Boolean)ns.getAttributeValue(NetServer.WINS_ENABLEMENT)).booleanValue() == true || 
               ((Boolean)ns.getAttributeValue(NetServer.WINS_ENABLEMENT)).booleanValue() == false )
              succeeded();
            else
              failed("NetServer wins enablement is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.WINS_ENABLEMENT_PENDING)  
    **/
    public void Var058()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            Boolean orig = (Boolean)ns.getAttributeValue(NetServer.WINS_ENABLEMENT_PENDING);
            
            if (orig.booleanValue() == true)
               ns.setAttributeValue(NetServer.WINS_ENABLEMENT_PENDING, new Boolean(false));
            else
               ns.setAttributeValue(NetServer.WINS_ENABLEMENT_PENDING, new Boolean(true));

            ns.commitAttributeChanges();
            
            if (orig.booleanValue() == true)
            {
               if (((Boolean)ns.getAttributeValue(NetServer.WINS_ENABLEMENT_PENDING)).booleanValue() == false)
                  succeeded();
               else
                  failed("NetServer wins enablement pending is invalid.");        
            }
            else
            {
               if (((Boolean)ns.getAttributeValue(NetServer.WINS_ENABLEMENT_PENDING)).booleanValue() == true)
                  succeeded();
               else
                  failed("NetServer wins enablement pending is invalid.");        
            }

            ns.setAttributeValue(NetServer.WINS_ENABLEMENT_PENDING, orig);
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServer.WINS_PRIMARY_ADDRESS) 
     **/
    public void Var059()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           
           if (((String)ns.getAttributeValue(NetServer.WINS_PRIMARY_ADDRESS)).length() >= 0 )
              succeeded();
            else
              failed("NetServer wins primary address is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.WINS_PRIMARY_ADDRESS_PENDING)  
    **/
    public void Var060()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            String orig = (String)ns.getAttributeValue(NetServer.WINS_PRIMARY_ADDRESS_PENDING);
            
            ns.setAttributeValue(NetServer.WINS_PRIMARY_ADDRESS_PENDING, "9.5.100.77");
            ns.commitAttributeChanges();
            
            if (((String)ns.getAttributeValue(NetServer.WINS_PRIMARY_ADDRESS_PENDING)).equals("9.5.100.77"))
               succeeded();
            else
               failed("NetServer wins primary address pending is invalid.");        
            

            ns.setAttributeValue(NetServer.WINS_PRIMARY_ADDRESS_PENDING, orig);
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeValue(NetServer.WINS_SCOPE_ID) 
     **/
    public void Var061()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           
           if (((String)ns.getAttributeValue(NetServer.WINS_SCOPE_ID)).length() >= 0 )
              succeeded();
            else
              failed("NetServer wins scope id is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.WINS_SCOPE_ID_PENDING)  
    **/
    public void Var062()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            String orig = (String)ns.getAttributeValue(NetServer.WINS_SCOPE_ID_PENDING);
            
            ns.setAttributeValue(NetServer.WINS_SCOPE_ID_PENDING, "9.5.100.79");
            ns.commitAttributeChanges();
            
            if (((String)ns.getAttributeValue(NetServer.WINS_SCOPE_ID_PENDING)).equals("9.5.100.79"))
               succeeded();
            else
               failed("NetServer wins scope id pending is invalid.");        
            

            ns.setAttributeValue(NetServer.WINS_SCOPE_ID_PENDING, orig);
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServer.WINS_SECONDARY_ADDRESS) 
     **/
    public void Var063()
    {
        try 
        {
           NetServer ns = new NetServer(systemObject_);
           
           if (((String)ns.getAttributeValue(NetServer.WINS_SECONDARY_ADDRESS)).length() >= 0 )
              succeeded();
            else
              failed("NetServer wins secondary address is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServer.WINS_SECONDARY_ADDRESS_PENDING)  
    **/
    public void Var064()
    {
        try 
        {
            NetServer ns = new NetServer(pwrSys_);   //@A1C
            String orig = (String)ns.getAttributeValue(NetServer.WINS_SECONDARY_ADDRESS_PENDING);
            
            ns.setAttributeValue(NetServer.WINS_SECONDARY_ADDRESS_PENDING, "9.5.100.77");
            ns.commitAttributeChanges();
            
            if (((String)ns.getAttributeValue(NetServer.WINS_SECONDARY_ADDRESS_PENDING)).equals("9.5.100.77"))
               succeeded();
            else
               failed("NetServer wins primary address pending is invalid.");        
            

            ns.setAttributeValue(NetServer.WINS_SECONDARY_ADDRESS_PENDING, orig);
            ns.commitAttributeChanges();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     Serialization.  Verify that the object is properly serialized
     when the GUEST_USER_PROFILE attributes are not set.
    
     NetServer
    **/
   public void Var065(int runMode)
   {
     if (runMode != ATTENDED && runMode != BOTH) {
       notApplicable("Attended testcase");  // The serialize/deserialize forgets the password.
       return;
     }
      try
      {
         NetServer ns = new NetServer(systemObject_);
         NetServer ns2 = (NetServer) NetServerTest.serialize (ns);
         assertCondition (((String)ns2.getAttributeValue(NetServer.GUEST_USER_PROFILE)).equals(""));
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception");
      }
   }


    /**
    getAttributeValue(NetServer.AUTHENTICATION_METHOD) 
     **/
    public void Var066()
    {
      try 
      {
        if (systemObject_.getVersion() < 5 ||
            (systemObject_.getVersion() == 5 &&
             systemObject_.getRelease() < 2))
        {
          notApplicable("Server is pre-V5R2.");
          return;
        }

        NetServer ns = new NetServer(systemObject_);
        Integer authMethod = (Integer)(ns.getAttributeValue(NetServer.AUTHENTICATION_METHOD));
        if (authMethod == null) {
          failed("The reported attribute is null.");
          return;
        }
        int authMethodInt = authMethod.intValue();
        assertCondition(authMethodInt == 0 || authMethodInt == 1 || authMethodInt == 2, "The reported attribute value is invalid: " + authMethodInt);
      }
      catch (Exception e) 
      {
        failed (e, "Unexpected Exception");
      }
    }


    /**
    setAttributeValue(NetServer.AUTHENTICATION_METHOD_PENDING)  
    **/
    public void Var067()
    {
      NetServer ns = null;
      Integer orig = null;
      Integer newVal = null;;
      final Integer AUTH_PWD      = new Integer(0);  // encrypted passwords
      final Integer AUTH_KERBEROS = new Integer(1);  // Kerberos v5 tokens
      try 
      {
        if (systemObject_.getVersion() < 5 ||
            (systemObject_.getVersion() == 5 &&
             systemObject_.getRelease() < 2))
        {
          notApplicable("Server is pre-V5R2.");
          return;
        }

        ns = new NetServer(pwrSys_);   //@A1C
        orig = (Integer)(ns.getAttributeValue(NetServer.AUTHENTICATION_METHOD_PENDING));

        if (orig.equals(AUTH_PWD)) newVal = AUTH_KERBEROS;
        else                       newVal = AUTH_PWD;
        ns.setAttributeValue(NetServer.AUTHENTICATION_METHOD_PENDING, newVal);
        try
        {
          ns.commitAttributeChanges();

          if (((Integer)ns.getAttributeValue(NetServer.AUTHENTICATION_METHOD_PENDING)).equals(newVal))
            succeeded();
          else
            failed("NetServer authentication method pending is invalid.");
        }
        catch (ResourceException e)
        {
          AS400Message msg = e.getMessageList()[0];
          String msgID = msg.getID();
          if (newVal == AUTH_KERBEROS &&
              msgID.equals("CPFB686")) // "Error configuring IBM i Support for Windows Network Neighborhood (IBM i NetServer)"
          {
            succeeded("Assuming " + msgID + " merely indicates that Kerberos isn't configured on the system.");
          }
          else failed (e, "Unexpected ResourceException: " + msgID + ": " + msg.getText());  
        }
      }
      catch (Exception e) 
      {
        failed (e, "Unexpected Exception");
      }
      finally
      {
        if (ns != null && orig != null)
        {
          try {
            ns.setAttributeValue(NetServer.AUTHENTICATION_METHOD_PENDING, orig);
            ns.commitAttributeChanges();
          }
          catch (Exception e) { e.printStackTrace(); }
        }
      }
    }


    // Variations that end sessions and/or end the NetServer job on the system.
    // These variations must be postponed until the end of the variation sequence,
    // since they will kill the prestarted connection session that some other variations assume are active.


    /**
    NetServerSession.end()
     Note: This variation ends the session that the user was instructed to start.
     This variation was moved to this testcase from NetServerSessionTestcase.
    **/
    public void Var068()
    {
      JCIFSUtility jcifs2 = null; 
        try 
        {
          
          // Make sure a session exists 
          jcifs2 = new JCIFSUtility(pwrSys_.getSystemName().toUpperCase(), pwrSysUserID_, pwrSysEncryptedPassword_); 

          
          // Create connection with IOSYSCFG auth (i.e. pwrSys_) for QZLSENSS() API
          NetServer netserPwr = new NetServer(pwrSys_);             //@A1A
          ResourceList sessionListPwr = netserPwr.listSessions();   //@A1A
          

            NetServerSession session = (NetServerSession)sessionListPwr.resourceAt(0);//@A1C
            session.end();
            jcifs2.close(); 
            jcifs2 = null; 
            succeeded();

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        } finally {
          if (jcifs2 != null) {
            try {
              jcifs2.close(); 
              jcifs2 = null; 
            } catch (Exception e) { 
              e.printStackTrace(); 
            }
          }
        }
    }

    public void waitForEnd(NetServer netser) throws Exception { 


	    // Allow up to 90 seconds to end
	long startTime = System.currentTimeMillis();
	long endTime = startTime + 70000; 
	Thread.sleep(1000);

	boolean isStarted = netser.isStarted();
	while (isStarted && System.currentTimeMillis() < endTime) {
	    Thread.sleep(1000);
	    isStarted = netser.isStarted();
	}
	System.out.println("netserver took "+(System.currentTimeMillis() - startTime)+ "ms to end"); 

    }

    /**
    end() - Call when the server has never been started.
    **/
    public void Var069()
    {
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            netser.end();
           waitForEnd(netser);

            assertCondition(netser.isStarted() == false, "netser.isStarted returned true sb false");

	    
	    try {
		netser.start();
		waitForStart(netser); 
	    } catch (Exception e) {
		System.out.println("Warning:  Var069 restart failed");
		e.printStackTrace(System.out); 
	    }
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    public void waitForStart(NetServer netser) throws Exception {
   
	    // Allow up to 90 seconds to start
	    long startTime = System.currentTimeMillis();
	    long endTime = startTime + 90000; 
            Thread.sleep(1000);

	    boolean isStarted = netser.isStarted();
	    while (!isStarted && System.currentTimeMillis() < endTime) {
		Thread.sleep(1000);
		isStarted = netser.isStarted();
	    }
	    if (isStarted) {
		System.out.println("netserver took "+(System.currentTimeMillis() - startTime)+ " ms to start");
	    } else {
		System.out.println("netserver did not start in "+(System.currentTimeMillis() - startTime)+ " ms");
	    }

    }

    /**
    end() - Call when the server has been started.
    **/
    public void Var070()
    {
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            if (!netser.isStarted())
            {
               netser.start();
	       waitForStart(netser);
     
            }
            netser.end();
	    waitForEnd(netser);

            assertCondition(netser.isStarted() == false, "isStarted=true should be false.  Unable to end netserver within 20 seconds.  Updated 3/10/2011");

	    
	    try {
		netser.start();
		waitForStart(netser); 
	    } catch (Exception e) {
		System.out.println("Warning:  Var070 restart failed");
		e.printStackTrace(System.out); 
	    }

        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    end() - Call when the server has already been ended.
    **/
    public void Var071()
    {
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
	    if (netser.isStarted()) {
		netser.end();
		waitForEnd(netser);

	    }
            netser.end();
	    waitForEnd(netser);
	

            assertCondition(netser.isStarted() == false);

	    
	    try {
		netser.start();
		waitForStart(netser); 
	    } catch (Exception e) {
		System.out.println("Warning:  Var071 restart failed");
		e.printStackTrace(System.out); 
	    }

        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    isStarted() - Call when the server has never been started.
    **/
    public void Var072()
    {
        try 
        {
            NetServer netser= new NetServer(pwrSys_);   //@A1C
	    if (netser.isStarted()) {
		netser.end();
		waitForEnd(netser);
	    }
            assertCondition(netser.isStarted() == false);

	    
	    try {
		netser.start();
		waitForStart(netser); 
	    } catch (Exception e) {
		System.out.println("Warning:  Var072 restart failed");
		e.printStackTrace(System.out); 
	    }

        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    isStarted() - Call immediately after starting the server.
    **/
    public void Var073()
    {
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            netser.start();
	    waitForStart(netser);
  
	    boolean started = netser.isStarted(); 

            netser.end();
	    waitForEnd(netser);

            assertCondition(started == true, "NetServer did not start");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    isStarted() - Call when the server has started and ended successively.
    **/
    public void Var074()
    {
	if (checkNotGroupTest()) 
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            if (!netser.isStarted())
            {
               netser.start();
	    waitForStart(netser);
          
            }
            netser.end();
	    waitForEnd(netser);
            assertCondition(netser.isStarted() == false, "netser.isStarted() = true sb false");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    refreshAttributeValues() - Call when the server has been started and ended before, nothing else.
    **/
    public void Var075()
    {
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            if(!netser.isStarted())
            {
               netser.start();
	       waitForStart(netser);

            }
            netser.end();
	    waitForEnd(netser);

 
            netser.refreshAttributeValues();
            succeeded();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    toString() - For a NetServer whose properties have been set and used, this should return the user name.
    **/
    public void Var076()
    {
	if (checkNotGroupTest()) 
        try 
        {
            NetServer netser = new NetServer(pwrSys_);   //@A1C
            netser.start();
	    waitForStart(netser);

            String asString = netser.toString();
            netser.end();
	    waitForEnd(netser);

            assertCondition(asString.length() > 0);
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getAttributeValue(NetServer.CCSID) 
     **/
    public void Var077()
    {
        try 
        {
           NetServer ns = new NetServer(pwrSys_);   //@A1C
           ns.end();
	   waitForEnd(ns);


           ns.start();
	       waitForStart(ns);

           
           if (((Integer)ns.getAttributeValue(NetServer.CCSID)).intValue() >= 0 )
              succeeded();
            else
               failed("NetServer ccsid is not valid.");        
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


}
