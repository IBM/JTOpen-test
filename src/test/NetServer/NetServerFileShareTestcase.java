///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NetServerFileShareTestcase.java
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

import test.Testcase;

/** 
 Testcase NetServerFileShareTestcase.
**/
@SuppressWarnings("deprecation")

public class NetServerFileShareTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NetServerFileShareTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.INetServerTest.main(newArgs); 
   }
  private final static boolean DEBUG = false;

    /**
     Constructor.
     **/
    public NetServerFileShareTestcase(AS400 systemObject, 
                             Hashtable<String,Vector<String>> namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream, 
                             
                             String password, AS400 pwrSys)
    {
        super(systemObject, "NetServerFileShareTestcase", namesAndVars, runMode, fileOutputStream, password);
        pwrSys_ = pwrSys;
    }

    protected void setup()
        throws Exception
      {
        lockSystem("NETSVR",600); 
        super.setup(); 
      }
      protected void cleanup() throws Exception
      {
        unlockSystem();
        super.cleanup(); 
      }


    
    /**
     Construct a NetServerFileShare with no parameters.
     
     NetServerFileShare() 
     **/
    public void Var001()
    {
        try
        {
            NetServerFileShare nsfs = new NetServerFileShare();
            succeeded("nsfs="+nsfs);
        }
        catch (Exception e)
        {
           failed(e, "Wrong exception info.");
        }
    }

    /**
    Construct a NetServerFileShare with parameters.
     
    NetServerFileShare(sysem, String) 
    **/
    public void Var002()
    {
        try
        {
            NetServerFileShare nsfs = new NetServerFileShare(systemObject_, "Sam");
            succeeded("nsfs="+nsfs);
        }
        catch (Exception e)
        {
           failed(e, "Wrong exception info.");
        }
    }

    /**
    Construct a NetServerFileShare passing a null for the system parm.
    A NullPointerException should be thrown.

    NetServerFileShare(null, String)
    **/
    public void Var003()
    {
        try
        {
           NetServerFileShare nsfs = new NetServerFileShare(null, "Sam");
           failed("No exception"+nsfs);
        }
        catch (Exception e)
        {
           assertExceptionIs (e, "NullPointerException", "system");
        }
    }

    /**
    Construct a NetServerFileShare passing a null for the request parm.
    A NullPointerException should be thrown.

    NetServerFileShare(system, null)
    **/
    public void Var004()
    {
        try
        {
           NetServerFileShare nsfs = new NetServerFileShare(systemObject_, null);
           failed("No exception"+nsfs);
        }
        catch (Exception e)
        {
           assertExceptionIs (e, "NullPointerException", "name");
        }
    }

    /**
    getAttributeValue(NetServerFlieShare.PATH) - For a NetServerFileShare this should return the integrated
    file system path.
    **/
    public void Var005()
    {
        try 
        {
            NetServer ns = new NetServer(systemObject_);
            ResourceList shareList = ns.listFileShares();
            shareList.waitForComplete();
            if (DEBUG) {
              for (int i=0; i<shareList.getListLength(); i++) {
                NetServerFileShare share = (NetServerFileShare)shareList.resourceAt(i);
                System.out.println("Share path: |" + (String)share.getAttributeValue(NetServerFileShare.PATH));
              }
            }
            NetServerFileShare share = (NetServerFileShare)shareList.resourceAt(0);

            if ((String)share.getAttributeValue(NetServerFileShare.PATH) != null)
               succeeded();
            else
               failed("NetServerFileShare path is null.");
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeValue(NetServerFlieShare.MAXIMUM_USERS) - For a NetServerFileShare this should return the 
    maximum number of users who can concurrently access this share. The default is -1.
    **/
public void Var006()
{
  try
  {
    NetServer ns = new NetServer(systemObject_);
    ResourceList shareList = ns.listFileShares();
    shareList.waitForComplete();
    NetServerFileShare share = (NetServerFileShare)shareList.resourceAt(0);

    int expectedValue;                         //@A1A
    if (share.toString().equals("QDIRSRV"))    //@A1A
      expectedValue = 2147483647;              //@A1A
    else                                       //@A1A
      expectedValue = -1;                      //@A1A

    if (((Integer)share.getAttributeValue(NetServerFileShare.MAXIMUM_USERS)).intValue() != expectedValue)//@A1C
      failed("getAttributeValue(MAXIMUM_USERS) is NOT equal to '"+expectedValue+"'"); //@A1C
    else
      succeeded();
  }
  catch (Exception e)
  {
    failed (e, "Unexpected Exception");
  }
}

    /**
    setAttributeValue(NetServerFlieShare.MAXIMUM_USERS) - For a NetServerFileShare this should return the 
    maximum number of users who can concurrently access this share. 
    **/
    public void Var007()
    {
        try 
        {
            NetServer ns = new NetServer(systemObject_);
            ResourceList shareList = ns.listFileShares();
            shareList.waitForComplete();
            NetServerFileShare share = (NetServerFileShare)shareList.resourceAt(0);
            
            share.setAttributeValue(NetServerFileShare.MAXIMUM_USERS, new Integer(100));
            
            if (((Integer)share.getAttributeValue(NetServerFileShare.MAXIMUM_USERS)).intValue() != 100)
               failed("NetServerFileShare maximum users is invalid.");        
            else
               succeeded();

            share.setAttributeValue(NetServerFileShare.MAXIMUM_USERS, new Integer(-1));
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeValue(NetServerFlieShare.PERMISSION) - For a NetServerFileShare this represents the permission
     for a share. The default is read-write.
    **/
    public void Var008()
    {
        try 
        {
            NetServer ns = new NetServer(systemObject_);
            ResourceList shareList = ns.listFileShares();
            shareList.waitForComplete();
            NetServerFileShare share = (NetServerFileShare)shareList.resourceAt(0);

            if (((Integer)share.getAttributeValue(NetServerFileShare.PERMISSION)).intValue() != (NetServerFileShare.PERMISSION_READ_WRITE).intValue() &&
                ((Integer)share.getAttributeValue(NetServerFileShare.PERMISSION)).intValue() != (NetServerFileShare.PERMISSION_READ_ONLY).intValue() )
               failed("NetServerFileShare Permission is incorect.");        
            else
               succeeded();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAttributeValue(NetServerFlieShare.PERMISSION) - For a NetServerFileShare this represents the permission
     for a share. 
    **/
    public void Var009()
    {
        try 
        {
            NetServer ns = new NetServer(systemObject_);
            ResourceList shareList = ns.listFileShares();
            shareList.waitForComplete();
            NetServerFileShare share = (NetServerFileShare)shareList.resourceAt(0);

            share.setAttributeValue(NetServerFileShare.PERMISSION, NetServerFileShare.PERMISSION_READ_ONLY);

            if ((Integer)share.getAttributeValue(NetServerFileShare.PERMISSION) != NetServerFileShare.PERMISSION_READ_ONLY)
               failed("NetServerFileShare Permission is incorect.");        
            else
               succeeded();

            share.setAttributeValue(NetServerFileShare.PERMISSION, NetServerFileShare.PERMISSION_READ_WRITE);
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    add() - Adds the file server share to the NetServer. 
    **/
    public void Var010()
    {
        NetServerFileShare share = null;
        try 
        {
            share = new NetServerFileShare(pwrSys_, "Toolbox");
            share.setAttributeValue(NetServerFileShare.PATH, "/QIBM");
            share.add();

            succeeded();
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
        finally
        {
          if (share != null) try { share.remove(); } catch (Exception e) {}
        }
    }
}
