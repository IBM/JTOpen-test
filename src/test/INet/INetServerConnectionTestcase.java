////////////////////////////////////////////////////////////////////////
//
// File Name:  INetServerConnectionTestcase.java
//
// Classes:  NetServer
//
////////////////////////////////////////////////////////////////////////
//
// CHANGE ACTIVITY:
//
// $A0=PTR/DCR  Release  Date        Userid      Comments
//    
// END CHANGE ACTIVITY
//
////////////////////////////////////////////////////////////////////////
package test.INet;


import java.io.*;
import com.ibm.as400.access.*;

import test.JCIFSUtility;
import test.PasswordVault;
import test.Testcase;

import java.util.Hashtable; import java.util.Vector;

/** 
 Testcase INetServerConnectionTestcase.
**/
public class INetServerConnectionTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "INetServerConnectionTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.INetServerTest.main(newArgs); 
   }
    
    // Private data.
    ///private VIFSSandbox     sandbox_;
    ISeriesNetServer netserver_;
    private ISeriesNetServer pwrNetserver_;
    private ISeriesNetServerSession[] sessionList_;

    static final boolean DEBUG = false;

    JCIFSUtility jcifs = null; 

    /**
     Constructor.
     **/
    public INetServerConnectionTestcase(AS400 systemObject, 
                             Hashtable<String,Vector<String>> namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream, 
                             
                             String password,
                             AS400 pwrSys,
			     String pwrUserid,
			     String pwrPassword)
    {
        super(systemObject, "INetServerConnectionTestcase", namesAndVars, runMode, fileOutputStream, password);

        if(pwrSys == null || pwrSys.getSystemName().length() == 0 || pwrSys.getUserId().length() == 0)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");

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
      lockSystem("NETSVR", 600);
      super.setup();

	jcifs = new JCIFSUtility(pwrSys_.getSystemName(), pwrSysUserID_, pwrSysEncryptedPassword_); 

       netserver_ = new ISeriesNetServer(systemObject_);
       pwrNetserver_ = new ISeriesNetServer(pwrSys_);
       sessionList_ = pwrNetserver_.listSessions();

       if (sessionList_.length == 0)
          throw new IllegalStateException("No sessions are active.  To start a session: Go to a PC and use Network Neighborhood to map a drive to \\\\" + systemObject_.getSystemName() + "\\ROOT , then open any file on the mapped drive.");
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



    void displayAttributeValues(ISeriesNetServerConnection conn)
    {
      String resName = conn.getName();
      int resType = conn.getResourceType();
      String user = conn.getUserName();
      int connId = conn.getID();
      long sessId = conn.getSessionID();
      int age = conn.getAge();
      int filesOpen = conn.getNumberOfFilesOpen();
      int connType = conn.getConnectionType();
      int numUsers = conn.getNumberOfUsers();

      output_.println("-----------\n" +
                         "CONNECTION:\n" +
                         "-----------\n" +
                         "Workstation or share: "+resName+"\n"+
                         "Resource type: "+resType+"\n"+
                         "User name: "+user+"\n"+
                         "Conn ID: "+connId+"\n"+
                         "Sess ID: "+sessId+"\n"+
                         "Conn age: "+age+"\n"+
                         "Files open: "+filesOpen+"\n"+
                         "Conn type: "+connType+"\n"+
                         "Num users: "+numUsers///+"\n"+
                         ///"System: "+system.getSystemName()
                         );
    }


    boolean validateAttributeValues(ISeriesNetServerConnection conn)
    {
      boolean ok = true;

      String resName = conn.getName();
      int resType = conn.getResourceType();
      String user = conn.getUserName();
      int connId = conn.getID();
      long sessId = conn.getSessionID();
      int age = conn.getAge();
      int filesOpen = conn.getNumberOfFilesOpen();
      int connType = conn.getConnectionType();
      int numUsers = conn.getNumberOfUsers();

      if (resType != ISeriesNetServerConnection.WORKSTATION &&
          resType != ISeriesNetServerConnection.SHARE) {
        ok = false;
        output_.println("Invalid resType: " + resType);
      }

      if (resName.trim().length() == 0) {
        ok = false;
        output_.println("Zero-length resName.");
      }

      if (resName.charAt(0) == ' ') {
        ok = false;
        output_.println("resName starts with a blank: |" + resName + "|");
      }

      if ((resType == ISeriesNetServerConnection.WORKSTATION && resName.length() > 124) || //@A1C
          (resType == ISeriesNetServerConnection.SHARE && resName.length() > 12)) {
        ok = false;
        output_.println("resName is too long: |" + resName +"|");
      }

      if (user.trim().length() == 0 || user.length() > 10) {
        ok = false;
        output_.println("user name length is invalid: |" + resName +"|");
      }

      if (user.charAt(0) == ' ') {
        ok = false;
        output_.println("user name starts with a blank: |" + user + "|");
      }

      if (connId < 0) {
        ok = false;
        output_.println("connId < 1");
      }

      if (sessId < 1) {
        ok = false;
        output_.println("sessId < 1");
      }

      if (age < 1) {
        ok = false;
        output_.println("age < 1");
      }
      if (age > 10000) {
        ///ok = false;
        output_.println("Warning: Connection age is suspect: " + age);
      }

      if (filesOpen < 0) {
        ok = false;
        output_.println("Invalid number of files open: " + filesOpen);
      }
      if (filesOpen > 100) {
        output_.println("Warning: Number of files open is questionable: " + filesOpen);
      }

      if (connType != ISeriesNetServerConnection.DISK_DRIVE &&
          connType != ISeriesNetServerConnection.SPOOLED_OUTPUT_QUEUE) {
        ok = false;
        output_.println("Invalid connType: " + connType);
      }

      if (numUsers < 0) {
        ok = false;
        output_.println("Num Users < 0");
      }
      if (numUsers > 100) {
        ///ok = false;
        output_.println("Warning Num Users is questionable: " + numUsers);
      }

      return ok;
    }


    /**
    Display and validate all attributes for all connections, listed via listConnectionsForSession(sessionID).
    **/
    public void Var001()
    {
      try
      {
        for (int i=0; i<sessionList_.length; i++)
        {
          ISeriesNetServerSession sess = sessionList_[i];
          String wsName = sess.getName();
          long sessID = sess.getID();
          if (DEBUG) {
            output_.println("-----------------------------------\n" +
                               "CONNECTIONS FOR SESSION " +wsName+" ("+sessID+")\n" +
                               "-----------------------------------\n");
          }
          ISeriesNetServerConnection[] connections = pwrNetserver_.listConnectionsForSession(sessID);
          for (int j=0; j<connections.length; j++)
          {
            ISeriesNetServerConnection conn = connections[j];
            if (DEBUG) displayAttributeValues(conn);
            if (!validateAttributeValues(conn)) {
              failed("Connection has invalid attribute value(s)");
              return;
            }
          }
        }
        succeeded();
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
    Display and validate all attributes for all connections, listed via listConnectionsForSession(sessionName).
    **/
    public void Var002()
    {
      try
      {
        for (int i=0; i<sessionList_.length; i++)
        {
          ISeriesNetServerSession sess = sessionList_[i];
          String wsName = sess.getName();
          long sessID = sess.getID();
          if (DEBUG) {
            output_.println("-----------------------------------\n" +
                               "CONNECTIONS FOR SESSION " +wsName+" ("+sessID+")\n" +
                               "-----------------------------------\n");
          }
          ISeriesNetServerConnection[] connections = pwrNetserver_.listConnectionsForSession(wsName);
          for (int j=0; j<connections.length; j++)
          {
            ISeriesNetServerConnection conn = connections[j];
            if (DEBUG) displayAttributeValues(conn);
            if (!validateAttributeValues(conn)) {
              failed("Connection has invalid attribute value(s)");
              return;
            }
          }
        }
        succeeded();
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
    Display and validate all attributes for all connections, listed via listConnectionsForShare(shareName).
    **/
    public void Var003()
    {
      try
      {
        // Get list of all shares.
        ISeriesNetServerShare[] shares = pwrNetserver_.listShares();
        for (int i=0; i<shares.length; i++)
        {
          ISeriesNetServerShare share = shares[i];
          String shareName = share.getName();
          String shareType = (share instanceof ISeriesNetServerFileShare ? "FILE" : "PRINT");
          if (DEBUG) {
            output_.println("-----------------------------------\n" +
                               "CONNECTIONS FOR SHARE " +shareName+" ("+shareType+")\n" +
                               "-----------------------------------\n");
          }
          ISeriesNetServerConnection[] connections = pwrNetserver_.listConnectionsForShare(shareName);
          for (int j=0; j<connections.length; j++)
          {
            ISeriesNetServerConnection conn = connections[j];
            if (DEBUG) displayAttributeValues(conn);
            if (!validateAttributeValues(conn)) {
              failed("Connection has invalid attribute value(s).");
              return;
            }
            pwrNetserver_.refresh(conn);
            if (!validateAttributeValues(conn)) {
              failed("Connection has invalid attribute value(s) after refresh.");
              return;
            }
          }
        }
        succeeded();
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

}
