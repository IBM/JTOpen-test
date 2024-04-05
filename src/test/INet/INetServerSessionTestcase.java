////////////////////////////////////////////////////////////////////////
//
// File Name:  INetServerSessionTestcase.java
//
// Classes:  ISeriesNetServerSession
//
////////////////////////////////////////////////////////////////////////
//
// CHANGE ACTIVITY:
//
// $A0=PTR/DCR  Release  Date        Userid      Comments
//              v5r3     06/19/2003  jlee        Created
//  A1          v6r1     2008-08-12  dprigge     ZLSOLST uses an expanded
//                       workstation name up to 124 chars (due to IPV6).
//                       Previously, workstation name had 15 char limit.
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

import java.util.Hashtable;

/**
 Testcase INetServerSessionTestcase.
**/
public class INetServerSessionTestcase extends Testcase
{
   private ISeriesNetServer netserver_;
   private ISeriesNetServer pwrNetserver_;
   private ISeriesNetServerSession[] sessionList_;

   private static final boolean DEBUG = false;

    JCIFSUtility jcifs = null; 

    /**
     Constructor.
     **/
    public INetServerSessionTestcase(AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password,
                             AS400 pwrSys,
			     String pwrUserid,
			     String pwrPassword)
    {
        super(systemObject, "INetServerSessionTestcase", namesAndVars, runMode, fileOutputStream, password);

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
          throw new IllegalStateException("No sessions are active.  To start a session: Go to a PC and use Network Neighborhood to map a drive to \\\\" + systemObject_.getSystemName() + "\\ROOT , then open any file on the mapped drive (WordPad is better than NotePad).  You might also need to open the mapped drive using Windows Explorer.");
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
    Create a ISeriesNetServerSession()
    **/
    public void Var001()
    {
        try
        {
            ISeriesNetServerSession session = sessionList_[0];
            assertCondition(true, "session="+session); 
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
            ISeriesNetServerSession session = sessionList_[0];
            ///System.out.println("session ID: " + session.getID());
            ///System.out.println ("About to call listConnectionsForSession().  Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {}
            ISeriesNetServerConnection[] connectionsList = pwrNetserver_.listConnectionsForSession(session.getID());
            ///System.out.println ("Called listConnectionsForSession().  Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {}
            if (connectionsList != null) {
              succeeded();
              if (connectionsList.length == 0) System.out.println("No connections found for session " + session.getName());
              else {
                if (DEBUG) {
                  System.out.println("Connections found for session " + session.getName() + ":");
                  for (int i=0; i<connectionsList.length; i++) {
                    System.out.println(connectionsList[i].getUserName() + " connected to " + connectionsList[i].getName());
                  }
                }
              }
            }
            else
               failed("Null session list.");
        }
        catch(Exception e)
        {
            ///System.out.println ("Caught exception.  Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {}
            failed(e, "Unexpected Exception");
        }
    }


    /**
      getNumberOfConnections()
    **/
    public void Var003()
    {
        try
        {
           // Try to find a session with at least one connection.
          boolean found = false;
          for (int i=0; i<sessionList_.length && !found; i++) {
            if (sessionList_[i].getNumberOfConnections() != 0) found = true;
          }
           if (found)
              succeeded();
           else
              succeeded("Warning: Found no session with at least 1 connection.");
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
            ISeriesNetServerSession session = sessionList_[0];
            String s = session.getName();

            assertCondition (s != null && s.length() != 0);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
      getAge()
    **/
    public void Var005()
    {
        try
        {
           ISeriesNetServerSession session = sessionList_[0];
	   long age =  session.getAge(); 
           if (age > 0)
              succeeded();
           else
              failed("ISeriesNetServerSession session time is not valid but is "+age);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      getNumberOfFilesOpen()
    **/
    public void Var006()
    {
        try
        {
           ISeriesNetServerSession session = sessionList_[0];
           if (session.getNumberOfFilesOpen() >= 1)
              succeeded();
           else
              failed("ISeriesNetServerSession files open count is not valid.  Make sure you have a drive mapped and a file open on the drive.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      getIdleTime()
    **/
    public void Var007()
    {
        try
        {
           ISeriesNetServerSession session = sessionList_[0];
           if (session.getIdleTime() >= 0)
              succeeded();
           else
              failed("ISeriesNetServerSession idle time is not valid.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      isPasswordEncrypted()
    **/
    public void Var008()
    {
        try
        {
           ISeriesNetServerSession session = sessionList_[0];
           if (session.isPasswordEncrypted() == true ||
               session.isPasswordEncrypted() == false)
              succeeded();
           else
              failed("ISeriesNetServerSession is encrypt password is not valid.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      isGuest()
    **/
    public void Var009()
    {
        try
        {
           ISeriesNetServerSession session = sessionList_[0];
           if (session.isGuest() == true ||
               session.isGuest() == false)
              succeeded();
           else
              failed("ISeriesNetServerSession is guest is not valid.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      getUserName()
    **/
    public void Var010()
    {
        try
        {
           ISeriesNetServerSession session = sessionList_[0];
           if (session.getUserName().length() > 0)
              succeeded();
           else
              failed("ISeriesNetServerSession user is not valid.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    endSession(), where user doesn't have *IOSYSCFG authority.
     Verify that an AS400SecurityException is thrown.
    **/
    public void Var011()
    {
        try
        {
            ISeriesNetServerSession session = sessionList_[0];
            netserver_.endSession(session.getID());
            failed ("Didn't throw exception.  Try a userid without IOSYSCFG auth.");
        }
        catch(Exception e)
        {
          assertExceptionIsInstanceOf (e, "com.ibm.as400.access.AS400SecurityException");
        }
    }


    static void displayAttributeValues(ISeriesNetServerSession sess)
    {
      String wsName = sess.getName();
      long id = sess.getID();
      int numConns = sess.getNumberOfConnections();
      int age = sess.getAge();
      int filesOpen = sess.getNumberOfFilesOpen();
      int idleTime = sess.getIdleTime();
      boolean encrypted = sess.isPasswordEncrypted();
      boolean guest = sess.isGuest();
      String user = sess.getUserName();
      System.out.println("--------\n" +
                         "SESSION:\n" +
                         "--------\n" +
                         "Workstation: "+wsName+"\n"+
                         "Session ID: "+id+"\n"+
                         "Connections: "+numConns+"\n"+
                         "Connection age: "+age+"\n"+
                         "Files open: "+filesOpen+"\n"+
                         "Idle time: "+idleTime+"\n"+
                         "Is Encrypted: "+encrypted+"\n"+
                         "Is guest: "+guest+"\n"+
                         "User name: "+user
                         );
    }

    private /*static*/ boolean validateAttributeValues(ISeriesNetServerSession sess)
    {
      boolean ok = true;

      String wsName = sess.getName();
      long id = sess.getID();
      int numConns = sess.getNumberOfConnections();
      int age = sess.getAge();
      int filesOpen = sess.getNumberOfFilesOpen();
      int idleTime = sess.getIdleTime();
      boolean encrypted = sess.isPasswordEncrypted();
      boolean guest = sess.isGuest();
      String user = sess.getUserName();

      if (wsName.trim().length() == 0 || wsName.length() > 124) { //@A1C
        ok = false;
        System.out.println("Workstation name has invalid length: " + wsName.length());
      }
      if (wsName.charAt(0) == ' ') {
        ok = false;
        System.out.println("wsName starts with a blank: |" + wsName + "|");
      }

      if (id < 1) {
        ok = false;
        System.out.println("Invalid session ID: " + id);
      }
      if (numConns < 0) {
        ok = false;
        System.out.println("Invalid number of connections: " + numConns);
      }
      if (numConns > 100) {
        System.out.println("Warning: Number of connections is questionable: " + numConns);
      }
      if (age < 0) {
        ok = false;
        System.out.println("Invalid age: " + age);
      }
      if (age > 60*60*48) {  // See if session is older than 48 hours
        ok = false;
        System.out.println("Warning: Session age is questionable: " + age);
      }
      if (filesOpen < 0) {
        ok = false;
        System.out.println("Invalid number of files open: " + filesOpen);
      }
      if (filesOpen > 100) {
        System.out.println("Warning: Number of files open is questionable: " + filesOpen);
      }
      if (idleTime < 0) {
        ok = false;
        System.out.println("Invalid idle time: " + idleTime);
      }
      int idleTimeout = netserver_.getIdleTimeout();
      if (idleTimeout > 0 &&
          idleTime > idleTimeout) {
        ok = false;
        System.out.println("Idle time ("+idleTime+") exceeds NetServer idle timeout ("+idleTimeout+")");
      }

      if (user.trim().length() == 0 || user.length() > 10) {
        ok = false;
        System.out.println("User name has invalid length: " + user.length());
      }
      if (user.charAt(0) == ' ') {
        ok = false;
        System.out.println("user starts with a blank: |" + user + "| guest="+guest+" encrypted="+encrypted);
      }

      return ok;
    }


    /**
    Display and validate all attributes for all sessions.
    **/
    public void Var012()
    {
        try
        {
          for (int i=0; i<sessionList_.length; i++)
          {
            ISeriesNetServerSession sess = sessionList_[i];
            if (DEBUG) displayAttributeValues(sess);
            if (!validateAttributeValues(sess)) {
              failed("Session has invalid attribute value(s).");
              return;
            }
            pwrNetserver_.refresh(sess);
            if (!validateAttributeValues(sess)) {
              failed("Session has invalid attribute value(s) after refresh.");
              return;
            }
          }
          succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


// Note: The following variation is redundant with the setup() method.

///    /**
///    Construct a ISeriesNetServer with system and parm.
///    ListSessions held in ISeriesNetServer.
///    Get the string to verify results.
///
///    ISeriesNetServer(system)
///    ISeriesNetServer::listSessions()
///    **/
///    public void Var013(int runMode)
///    {
///      if (runMode != ATTENDED && runMode != BOTH) return;
///        try
///        {
///          String sysName = pwrSys_.getSystemName();
///          String text = "Establish at least one session.  The easiest way to do this is to map a drive on your PC to " + sysName + " (net use * \\\\"+sysName+"\\qibm), and opening of a file on that drive.\nDo you have a session established?";
///          if (!VTestUtilities.ask(text)) {
///            failed("This variation requires that a drive be mapped.");
///            return;
///          }
///            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
///            ISeriesNetServerSession[] sessionList = netser.listSessions();
///            long listLength = sessionList.length;
///            String[] s = new String[(int)listLength];
///            String st = new String();
///            for(int i=0; i<listLength; i++)
///            {
///                ISeriesNetServerSession session = sessionList[i];
///                s[i] = session.getName() + ": " + session.getUserName();
///                if(i > 0)
///                    st = st + s[i];
///                else
///                    st = s[i];
///            }
///
///            if(st.equals("")) {
///                failed("No sessions were listed.");
///            }
///            else
///                succeeded("The following sessions were listed:\n" + st);
///
///        }
///        catch (Exception e)
///        {
///            failed(e, "Unexpected exception.");
///        }
///    }

}
