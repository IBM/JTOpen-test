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
//              v5r1     10/02/2000  wiedrich    Created
//  A1          v6r1     2008-08-12  dprigge     ZLSOLST uses an expanded
//                       workstation name up to 124 chars (due to IPV6).
//                       Previously, workstation name had 15 char limit.
//    
// END CHANGE ACTIVITY
//
////////////////////////////////////////////////////////////////////////
package test;


import java.io.*;
import com.ibm.as400.access.*;
import java.util.Hashtable;

/** 
 Testcase INetServerConnectionTestcase.
**/
public class INetServerConnectionTestcase extends Testcase
{
    
    // Private data.
    ///private VIFSSandbox     sandbox_;
    private ISeriesNetServer netserver_;
    private ISeriesNetServerSession[] sessionList_;

    static final boolean DEBUG = false;

    JCIFSUtility jcifs = null; 

    /**
     Constructor.
     **/
    public INetServerConnectionTestcase(AS400 systemObject, 
                             Hashtable namesAndVars, 
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

	jcifs = new JCIFSUtility(pwrSys_.getSystemName(), pwrSysUserID_, pwrSysEncryptedPassword_); 

       netserver_ = new ISeriesNetServer(systemObject_);
       sessionList_ = netserver_.listSessions();

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
    }
    protected void setup() throws Exception {
      lockSystem("NETSVR", 600);
      super.setup();
    }

    protected void cleanup() throws Exception {
      super.cleanup();
      unlockSystem();

    }


    static void displayAttributeValues(ISeriesNetServerConnection conn)
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

      System.out.println("-----------\n" +
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


    static boolean validateAttributeValues(ISeriesNetServerConnection conn)
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
        System.out.println("Invalid resType: " + resType);
      }

      if (resName.trim().length() == 0) {
        ok = false;
        System.out.println("Zero-length resName.");
      }

      if (resName.charAt(0) == ' ') {
        ok = false;
        System.out.println("resName starts with a blank: |" + resName + "|");
      }

      if ((resType == ISeriesNetServerConnection.WORKSTATION && resName.length() > 124) || //@A1C
          (resType == ISeriesNetServerConnection.SHARE && resName.length() > 12)) {
        ok = false;
        System.out.println("resName is too long: |" + resName +"|");
      }

      if (user.trim().length() == 0 || user.length() > 10) {
        ok = false;
        System.out.println("user name length is invalid: |" + resName +"|");
      }

      if (user.charAt(0) == ' ') {
        ok = false;
        System.out.println("user name starts with a blank: |" + user + "|");
      }

      if (connId < 0) {
        ok = false;
        System.out.println("connId < 1");
      }

      if (sessId < 1) {
        ok = false;
        System.out.println("sessId < 1");
      }

      if (age < 1) {
        ok = false;
        System.out.println("age < 1");
      }
      if (age > 10000) {
        ///ok = false;
        System.out.println("Warning: Connection age is suspect: " + age);
      }

      if (filesOpen < 0) {
        ok = false;
        System.out.println("Invalid number of files open: " + filesOpen);
      }
      if (filesOpen > 100) {
        System.out.println("Warning: Number of files open is questionable: " + filesOpen);
      }

      if (connType != ISeriesNetServerConnection.DISK_DRIVE &&
          connType != ISeriesNetServerConnection.SPOOLED_OUTPUT_QUEUE) {
        ok = false;
        System.out.println("Invalid connType: " + connType);
      }

      if (numUsers < 0) {
        ok = false;
        System.out.println("Num Users < 0");
      }
      if (numUsers > 100) {
        ///ok = false;
        System.out.println("Warning Num Users is questionable: " + numUsers);
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
            System.out.println("-----------------------------------\n" +
                               "CONNECTIONS FOR SESSION " +wsName+" ("+sessID+")\n" +
                               "-----------------------------------\n");
          }
          ISeriesNetServerConnection[] connections = netserver_.listConnectionsForSession(sessID);
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
            System.out.println("-----------------------------------\n" +
                               "CONNECTIONS FOR SESSION " +wsName+" ("+sessID+")\n" +
                               "-----------------------------------\n");
          }
          ISeriesNetServerConnection[] connections = netserver_.listConnectionsForSession(wsName);
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
        ISeriesNetServerShare[] shares = netserver_.listShares();
        for (int i=0; i<shares.length; i++)
        {
          ISeriesNetServerShare share = shares[i];
          String shareName = share.getName();
          String shareType = (share instanceof ISeriesNetServerFileShare ? "FILE" : "PRINT");
          if (DEBUG) {
            System.out.println("-----------------------------------\n" +
                               "CONNECTIONS FOR SHARE " +shareName+" ("+shareType+")\n" +
                               "-----------------------------------\n");
          }
          ISeriesNetServerConnection[] connections = netserver_.listConnectionsForShare(shareName);
          for (int j=0; j<connections.length; j++)
          {
            ISeriesNetServerConnection conn = connections[j];
            if (DEBUG) displayAttributeValues(conn);
            if (!validateAttributeValues(conn)) {
              failed("Connection has invalid attribute value(s).");
              return;
            }
            netserver_.refresh(conn);
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
