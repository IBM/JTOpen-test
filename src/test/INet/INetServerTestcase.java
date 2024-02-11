///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  INetServerTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.INet;


import java.io.*;
import com.ibm.as400.access.*;

import test.Testcase;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;


/**
  Testcase INetServerTestcase.
**/
public class INetServerTestcase extends Testcase
{
    private String original_NetServer_Name;
    private ISeriesNetServer netserver_;
    private ISeriesNetServer netserverPwr_;

    static final int STOP_WAIT_SECONDS = 6; // seconds to wait for "stop" to complete
    static final int START_WAIT_SECONDS = 3; // seconds to wait for "start" to complete

    private boolean useKerberos_ = false;

    private boolean okToStopNetServer_ = false;
    private boolean askedIfOkToStopNetServer_ = false;

    static final boolean DEBUG = false;

    /**
     Constructor.
     **/
    public INetServerTestcase(AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password,
                             AS400 pwrSys,
                             boolean useKerberos)
    {
        super(systemObject, "INetServerTestcase", namesAndVars, runMode, fileOutputStream, password);

        if(pwrSys == null || pwrSys.getSystemName().length() == 0 || pwrSys.getUserId().length() == 0)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");

        pwrSys_ = pwrSys;
        useKerberos_ = useKerberos;
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
      netserver_ = new ISeriesNetServer(systemObject_);
      netserverPwr_ = new ISeriesNetServer(pwrSys_);
      original_NetServer_Name = netserver_.getName();
      if (original_NetServer_Name != null) {
        original_NetServer_Name = original_NetServer_Name.toUpperCase();
      }
      else System.out.println("ERROR: Original Netserver name is null.");
///      original_NetServer_NamePending = new String(netserver_.getSystemName().toUpperCase());
      if (DEBUG) {
        System.out.println("Original NetServer name: " + original_NetServer_Name);
///        System.out.println("Original NetServer name (pending): " + original_NetServer_NamePending);
      }

///       ISeriesNetServer server = new ISeriesNetServer(pwrSys_);
///       server.setName(original_NetServer_NamePending);
///       server.commitChanges();
///       stopAndStart(server,true);
    }



    /**
    Performs cleanup needed after running variations.

    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
      if (DEBUG) System.out.println("Performing INetServerTestcase cleanup...");
      if (original_NetServer_Name != null) {
        ISeriesNetServer server = new ISeriesNetServer(pwrSys_);
        server.setName(original_NetServer_Name);
        server.commitChanges();
      }
      CommandCall cmd = new CommandCall(pwrSys_);
      cmd.run("DLTUSRPRF USRPRF(NETSRVTEST)");
      if (okToStopNetServer_) {
        stopAndStart(true);
      }
      super.cleanup();
      unlockSystem();
    }


///    /**
///    Construct a ISeriesNetServer with no parameters.
///
///    ISeriesNetServer()
///    **/
///    public void Var001()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            succeeded();
///        }
///        catch (Exception e)
///        {
///            failed(e, "Wrong exception info.");
///        }
///    }

    private boolean isOkToStopNetServer()
    {
    	okToStopNetServer_=true; 
      return okToStopNetServer_;
    }


    // Validate all of the attribute values of a NetServer object.
    private /*static*/ boolean validateAttributeValues(ISeriesNetServer ns)
    {
      boolean ok = true;

      AS400 system = ns.getSystem();
      boolean allowSysName = ns.isAllowSystemName();
      int authMethod = ns.getAuthenticationMethod();
      boolean autostart;
      try { autostart = ns.isAutoStart(); }
      catch (Exception e) {
        try { autostart = netserverPwr_.isAutoStart(); }
        catch (Exception exc) { exc.printStackTrace(); }
      }
      String desc = ns.getDescription();  // max length: 50
      String domain = ns.getDomainName();  // max length: 15
      String guestUser = ns.getGuestUserProfile();  // max length: 10
      String serverName = ns.getName();   // max length: 15
      String winsPrim = ns.getWINSPrimaryAddress();   // max length: 15
      String winsScope = ns.getWINSScopeID();   // max length: 224
      String winsSec = ns.getWINSSecondaryAddress();   // max length: 15
      int browseInt = ns.getBrowsingInterval();  // 0 - 720000 milliseconds
      int ccsid = ns.getCCSID();  // >= 0
      int idleTimeout = ns.getIdleTimeout();  // -1, or >0
      boolean logonServer = ns.isLogonServer();
      boolean winsServer = ns.isWINSServer();

      if (system.getSystemName().length() == 0) {
        ok = false;
        System.out.println("System has zero-length name");
      }

      if (system.getUserId().length() == 0) {
        ok = false;
        System.out.println("System userid is zero-length");
      }

      if (authMethod < 0 || authMethod > 2) {
        ok = false;
        System.out.println("Auth method is invalid: " + authMethod);
      }
      if (desc.length() > 50) {
        ok = false;
        System.out.println("Description is longer than 50 chars");
      }
      if (domain.length() > 50) {
        ok = false;
        System.out.println("Domain is longer than 15 chars");
      }
      if (guestUser.length() > 10) {
        ok = false;
        System.out.println("Guest user is longer than 10 chars");
      }
      if (serverName.length() > 15) {
        ok = false;
        System.out.println("serverName is longer than 15 chars");
      }
      if (winsPrim.length() > 15) {
        ok = false;
        System.out.println("winsPrim is longer than 15 chars");
      }
      if (winsScope.length() > 224) {
        ok = false;
        System.out.println("winsScope is longer than 224 chars");
      }
      if (winsSec.length() > 15) {
        ok = false;
        System.out.println("winsSec is longer than 15 chars");
      }
      if (browseInt < 0 || browseInt > 720000) {
        ok = false;
        System.out.println("browseInt is invalid: " + browseInt);
      }
      if (ccsid < 0) {
        ok = false;
        System.out.println("ccsid is invalid: " + ccsid);
      }
      if (idleTimeout <= 0 && idleTimeout != ISeriesNetServer.NO_AUTO_DISCONNECT) {
        ok = false;
        System.out.println("idleTimeout is invalid: " + idleTimeout);
      }

      return ok;
    }


    /**
    Construct a ISeriesNetServer with parameters.

    ISeriesNetServer()
    **/
    public void Var001()
    {
        try
        {
            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
            if (validateAttributeValues(netser)) succeeded();
            else failed();
        }
        catch (Exception e)
        {
            failed(e, "Wrong exception info.");
        }
    }

    /**
    Construct a ISeriesNetServer passing a null for the request parm.
    A NullPointerException should be thrown.

    ISeriesNetServer(null)
    **/
    public void Var002()
    {
        try
        {
            ISeriesNetServer netser = new ISeriesNetServer(null);
            failed("No exception, but got "+netser);
        }
        catch (Exception e)
        {
            ///assertExceptionIs (e, "NullPointerException", "system");
            assertExceptionIs (e, "NullPointerException");
        }
    }


    /**
    commitChanges() - When the system is bogus.
    **/
    public void Var003()
    {
        try
        {
            AS400 system = new AS400("BogusToolbox", "is", "cool".toCharArray());
            system.setGuiAvailable(false);
            ISeriesNetServer netser = new ISeriesNetServer(system);
            netser.setName(new String());
            netser.commitChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e)
        {
          assertExceptionIsInstanceOf (e, "com.ibm.as400.access.AS400SecurityException");
        }
    }

    /**
    commitChanges() - When the user does not have sufficient authority (*IOSYSCFG).
    **/
    public void Var004()
    {
        try
        {
            netserver_.refresh();
            netserver_.setName(new String());
            netserver_.commitChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e)
        {
            ///assertExceptionIsInstanceOf (e, "com.ibm.as400.access.AS400SecurityException");
            if (!(e instanceof AS400SecurityException) &&
                !(e instanceof AS400Exception)) {
              failed (e, "Wrong exception");
	    } else {
		assertCondition(true); 
	    }
        }
    }

    /**
    commitChanges() - Should do nothing when no change has been made.
    **/
    public void Var005()
    {
        try
        {
            String name = netserverPwr_.getName();
            netserverPwr_.commitChanges();
            String name2 = netserverPwr_.getName();
            assertCondition(name2.equals(name));
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    commitChanges() - Should have no effect on reported attributes prior to stop/restart.
    **/
    public void Var006()
    {
        try
        {
            String nameOrig = netserverPwr_.getName();
            netserverPwr_.setName("TBOXTEST");
            netserverPwr_.commitChanges();
            String name2 = netserverPwr_.getName();
            assertCondition(name2.equals(nameOrig));

            netserverPwr_.setName(nameOrig);
            netserverPwr_.commitChanges();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    private boolean startIfStopped(boolean reset)
      throws Exception
    {
      if (netserverPwr_.isStarted()) {
        ///if (DEBUG) System.out.println("NetServer is already running.");
        return true;
      }
      netserverPwr_.start(reset);
      if (DEBUG) System.out.println("Waiting " + START_WAIT_SECONDS + " seconds for NetServer to start ...");
      Thread.sleep(START_WAIT_SECONDS*1000);  // Sometimes it takes awhile to end.
      if (!netserverPwr_.isStarted()) {
        System.out.println("NetServer is not restarted yet.  Waiting " + 3*START_WAIT_SECONDS + " more seconds ...");
        Thread.sleep(3*START_WAIT_SECONDS*1000);
        if (!netserverPwr_.isStarted()) {
          System.out.println("ERROR: NetServer is still not restarted, " + 4*START_WAIT_SECONDS + " seconds after end().");
          return false;
        }
      }
      return true;
    }

    private boolean stopAndStart(boolean reset)
      throws Exception
    {
      netserverPwr_.end();
      if (DEBUG) System.out.println("Waiting " + STOP_WAIT_SECONDS + " seconds for NetServer to end ...");
      Thread.sleep(STOP_WAIT_SECONDS*1000);  // Sometimes it takes awhile to end.
      if (netserverPwr_.isStarted()) {
        System.out.println("NetServer is still running.  Waiting " + 3*STOP_WAIT_SECONDS + " more seconds ...");
        Thread.sleep(3*STOP_WAIT_SECONDS*1000);
        if (netserverPwr_.isStarted()) {
          System.out.println("ERROR: NetServer is still running, " + 4*STOP_WAIT_SECONDS + " seconds after end().");
          return false;
        }
      }
      netserverPwr_.start(reset);
      if (DEBUG) System.out.println("Waiting " + START_WAIT_SECONDS + " seconds for NetServer to start ...");
      Thread.sleep(START_WAIT_SECONDS*1000);  // Sometimes it takes awhile to end.
      if (!netserverPwr_.isStarted()) {
        System.out.println("NetServer is not restarted yet.  Waiting " + 3*START_WAIT_SECONDS + " more seconds ...");
        Thread.sleep(3*START_WAIT_SECONDS*1000);
        if (!netserverPwr_.isStarted()) {
          System.out.println("ERROR: NetServer is still not restarted, " + 4*START_WAIT_SECONDS + " seconds after end().  Issuing another start()...");
          netserverPwr_.start(reset);
          Thread.sleep(START_WAIT_SECONDS*1000);
          if (!netserverPwr_.isStarted()) {
            System.out.println("NetServer did not restart.");
            return false;
          }
        }
      }
      return true;
    }

    /**
    commitChanges() - Should commit the change when 2 changes have been made.
    **/
    public void Var007(int runMode)
    {
	if (runMode != ATTENDED && runMode != BOTH) {
	    notApplicable("Attended Testcase"); 
	    return;
	}
      if (!isOkToStopNetServer()) {
        failed("This variation needs to stop and restart the NetServer process.");
        return;
      }
        try
        {
            String name1 = netserverPwr_.getName();
            boolean allowSysName1 = netserverPwr_.isAllowSystemName();
            boolean allowSysNameNew = (allowSysName1 ? false : true);

            String newName = new String("TOOLBOX");
            netserverPwr_.setName(newName);
            netserverPwr_.setAllowSystemName(allowSysNameNew);
            netserverPwr_.commitChanges();

            String name2 = netserverPwr_.getName();
            boolean allowSysName2 = netserverPwr_.isAllowSystemName();

            if (!stopAndStart(false)) {
              failed();
              ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception exc) {};
              return;
            }

            netserverPwr_.refresh();
            String name3 = netserverPwr_.getName();
            boolean allowSysName3 = netserverPwr_.isAllowSystemName();
            if (DEBUG) {
              System.out.println(name2 + ", " + name1 + ", " + allowSysName2 + ", " + allowSysName1);
              System.out.println(name3 + ", " + newName + ", " + allowSysName3 + ", true" );
            }

            assertCondition((name2.equals(name1)) && (allowSysName2 == allowSysName1) &&
                            (name3.equals(newName)) && (allowSysName3 == allowSysNameNew));
            ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception exc) {};

        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { cleanup(); } catch (Exception exc) { exc.printStackTrace(); }
        }
    }

///    /**
///    getAttributeUnchangedValue() - When there is no server specified.
///    **/
///    public void Var009()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            Object value = netser.getName();
///            failed ("Didn't throw exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
///        }
///    }

///    /**
///    getAttributeUnchangedValue() - Pass null.
///    **/
///    public void Var010()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
///            Object value = netser.getAttributeUnchangedValue(null);
///            failed ("Didn't throw exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///    getAttributeUnchangedValue() - Pass an invalid attribute ID.
///    **/
///    public void Var011()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
///            Object value = netser.getAttributeUnchangedValue(new AS400());
///            failed ("Didn't throw exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
///        }
///    }

    /**
    getAttributeUnchangedValue() - Pass an attribute ID, whose value has not been referenced.
    **/
    public void Var008()
    {
        try
        {
            netserver_.refresh();
            String value = netserver_.getName();

            ///System.out.println(value + ", " + original_NetServer_NamePending);
            assertCondition(value.equalsIgnoreCase(original_NetServer_Name));
            ///assertCondition(value.equals(original_NetServer_Name));
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeUnchangedValue() - Pass an attribute ID, whose value has been referenced, but not changed.
    **/
    public void Var009()
    {
        try
        {
            netserver_.refresh();
            netserver_.getName();
            String value = netserver_.getName();

            assertCondition(value.equalsIgnoreCase(original_NetServer_Name));
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed, but not committed.
    **/
    public void Var010()
    {
        try
        {
            netserver_.setName("TOOLBOX");
            String value = netserver_.getName();

            assertCondition(value.equalsIgnoreCase(original_NetServer_Name));
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
    public void Var011()
    {
        try
        {
            netserver_.setName("TOOLBOX");
            netserver_.refresh();

            String value = netserver_.getName();

            assertCondition(value.equalsIgnoreCase(original_NetServer_Name));
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

///    /**
///    getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed and committed.
///    **/
///    public void Var011()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer(pwrSys_);
///            netser.setName("TOOLBOX");
///            netser.commitChanges();
///            String value = netser.getName();
///
///            assertCondition(value.equals("TOOLBOX"));
///
///            cleanup();
///
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }


///    /**
///    end() - Call when the server has never been started.
///    **/
///    public void Var012(int runMode)
///    {
///      if (runMode != ATTENDED && runMode != BOTH) return;
///      if (!isOkToStopNetServer()) {
///        failed("This variation needs to stop and restart the NetServer process.");
///        return;
///      }
///        try
///        {
///            netserverPwr_.end();
///            Thread.sleep(STOP_WAIT_SECONDS*1000);
///            assertCondition(netserverPwr_.isStarted() == false);
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }

///    /**
///    end() - Call when the server has been started.
///    **/
///    public void Var013(int runMode)
///    {
///      if (runMode != ATTENDED && runMode != BOTH) return;
///      if (!isOkToStopNetServer()) {
///        failed("This variation needs to stop and restart the NetServer process.");
///        return;
///      }
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer(pwrSys_);
///            startIfStopped(false);
///            netser.end();
///            Thread.sleep(STOP_WAIT_SECONDS*1000);
///            assertCondition(netser.isStarted() == false);
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }
///
///    /**
///    end() - Call when the server has already been ended.
///    **/
///    public void Var014(int runMode)
///    {
///      if (runMode != ATTENDED && runMode != BOTH) return;
///      if (!isOkToStopNetServer()) {
///        failed("This variation needs to stop and restart the NetServer process.");
///        return;
///      }
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer(pwrSys_);
///            startIfStopped(false);
///            netser.end();
///            Thread.sleep(STOP_WAIT_SECONDS*1000);
///            netser.end();
///            assertCondition(netser.isStarted() == false);
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }
///
///    /**
///    isStarted() - Call when the server has never been started and not enough
///    information is available to start it.
///    **/
///    public void Var015(int runMode)
///    {
///      if (runMode != ATTENDED && runMode != BOTH) return;
///      if (!isOkToStopNetServer()) {
///        failed("This variation needs to stop and restart the NetServer process.");
///        return;
///      }
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
///            assertCondition(netser.isStarted() == false);
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }
///
///    /**
///    isStarted() - Call when the server has never been started.
///    **/
///    public void Var016(int runMode)
///    {
///      if (runMode != ATTENDED && runMode != BOTH) return;
///      if (!isOkToStopNetServer()) {
///        failed("This variation needs to stop and restart the NetServer process.");
///        return;
///      }
///        try
///        {
///            ISeriesNetServer netser= new ISeriesNetServer(pwrSys_);
///            if (netser.isStarted())
///               netser.end();
///            assertCondition(netser.isStarted() == false);
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }
///
///    /**
///    isStarted() - Call immediately after starting the server.
///    **/
///    public void Var017(int runMode)
///    {
///      if (runMode != ATTENDED && runMode != BOTH) return;
///      if (!isOkToStopNetServer()) {
///        failed("This variation needs to stop and restart the NetServer process.");
///        return;
///      }
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer(pwrSys_);
///            netser.start();
///            boolean started = netser.isStarted();
///            netser.end();
///            assertCondition(started == true);
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }
///
///    /**
///    isStarted() - Call when the server has started and ended successively.
///    **/
///    public void Var018(int runMode)
///    {
///      if (runMode != ATTENDED && runMode != BOTH) return;
///      if (!isOkToStopNetServer()) {
///        failed("This variation needs to stop and restart the NetServer process.");
///        return;
///      }
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer(pwrSys_);
///            startIfStopped(false);
///            netser.end();
///            Thread.sleep(STOP_WAIT_SECONDS*1000);
///            assertCondition(netser.isStarted() == false);
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }

///    /**
///    start() - Call when there is not enough information to start the server.
///    **/
///    public void Var024()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            netser.end();
///            netser.start();
///            failed ("Didn't throw exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
///        }
///    }

    /**
    start() - Call when a bogus system is specified.
    **/
    public void Var012()
    {
        try
        {
            AS400 bogus = new AS400("bogusToolbox", "bogus", "bogus".toCharArray());
            bogus.setGuiAvailable(false);
            ISeriesNetServer netser = new ISeriesNetServer(bogus);
            netser.start();
            failed ("Didn't throw exception");
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.AS400SecurityException");
        }
    }


    /**
    endSession(), where user has *IOSYSCFG authority.
     Verify that session is ended.
    **/
    public void Var013(int runMode)
    {
	if (runMode != ATTENDED && runMode != BOTH) {
	    notApplicable("Attended Testcase");
	    return;
	}
      if (!isOkToStopNetServer()) {
        failed("This variation ends a session.");
        return;
      }
        try
        {
            ISeriesNetServerSession[] sessionList = netserver_.listSessions();
            if (sessionList.length == 0) {
              failed("No sessions are active");
              return;
            }
            ISeriesNetServerSession sess = sessionList[0];
            long id = sess.getID();
            String name = sess.getName();
            String user = sess.getUserName();
            System.out.println("Ending NetServer session: Workstation: " + name + ", User: "  + user + " ...");
            netserverPwr_.endSession(id);
            // Give it a few seconds to complete.
            Thread.sleep(3*1000);

            // See if the session is still there.
            sessionList = netserver_.listSessions();
            boolean found = false;
            for (int i=0; i<sessionList.length && !found; i++) {
              if (sessionList[i].getID() == id) found = true;
            }

            if (!found) succeeded();
            else failed("Session was not ended.");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    endSessionsForWorkstation(), where user has *IOSYSCFG authority.
     Verify that the sessions are ended.
    **/
    public void Var014(int runMode)
    {
	if (runMode != ATTENDED && runMode != BOTH) {
	    notApplicable("Attended Testcase");
	    return;
	}
      if (!isOkToStopNetServer()) {
        failed("This variation ends all sessions for a workstation.");
        return;
      }
        try
        {
            ISeriesNetServerSession[] sessionList = netserver_.listSessions();
            if (sessionList.length == 0) {
              failed("No sessions are active.  Start a session and rerun this variation.  To start a session: Go to a PC and use Network Neighborhood to map a drive to \\\\" + pwrSys_.getSystemName() + "\\ROOT , then open any file on the mapped drive.  WordPad works better than Notepad.");
              return;
            }
            String name = sessionList[0].getName();
            System.out.println("Ending all NetServer sessions for workstation " + name + " ...");
            netserverPwr_.endSessionsForWorkstation(name);
            // Give it a few seconds to complete.
            Thread.sleep(3*1000);

            // See if the session is still there.
            sessionList = netserver_.listSessions();
            boolean found = false;
            for (int i=0; i<sessionList.length && !found; i++) {
              if (sessionList[i].getName().equals(name)) found = true;
            }

            if (!found) succeeded();
            else failed("Sessions were not ended.");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    start() - Call when there is not enough information to start the server.
///    **/
///    public void Var026()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            netser.start();
///            failed ("Didn't throw exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
///        }
///    }


///    /**
///    refresh() - Call when there is not enough information to implicitly start the server.
///    **/
///    public void Var027()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            netser.refresh();
///            failed ("Didn't throw exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
///        }
///    }

    /**
    refresh() - Call when the server has been started before, but nothing done to it.
    **/
    public void Var015()
    {
        try
        {
            ISeriesNetServer netser = new ISeriesNetServer(pwrSys_);
            netser.start();
            Thread.sleep(STOP_WAIT_SECONDS*1000);
            netser.refresh();
            succeeded();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    refresh() - Call when the server has been started, and an attribute was set.
    **/
    public void Var016()
    {
        try
        {
            ISeriesNetServer netser = new ISeriesNetServer(pwrSys_);
            startIfStopped(false);
            String name = new String("Toolbox");
            netser.setName(name);
            netser.commitChanges();
            netser.refresh();
            succeeded();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    refresh() - Call when the server has been started and ended before, nothing else.
    **/
    public void Var017(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) { notApplicable("Attended TC"); return; }
      if (!isOkToStopNetServer()) {
        failed("This variation needs to stop and restart the NetServer process.");
        return;
      }
        try
        {
            ISeriesNetServer netser = new ISeriesNetServer(pwrSys_);
            startIfStopped(false);
            netser.end();
            Thread.sleep(STOP_WAIT_SECONDS*1000);
            netser.refresh();
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
    public void Var018()
    {
        try
        {
            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
            String asString = netser.toString();
            ///System.out.println("DEBUG: toString() returned: |" + asString + "|");
            String expected = "ISeriesNetServer (system: "+systemObject_.getSystemName() +"; name: ";
            ///System.out.println("DEBUG: expected: |" + expected +"|");
            assertCondition(asString.startsWith(expected));
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

///    /**
///    toString() - For a ISeriesNetServer whose properties have not been set, this should return the default toString.
///    **/
///    public void Var032()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            assertCondition(netser.toString().length() > 0);
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }

    /**
    toString() - For a ISeriesNetServer whose properties have been set and used, this should return the NetServer name.
    **/
    public void Var019()
    {
        try
        {
            startIfStopped(false);
            netserver_.refresh();
            String asString = netserver_.toString();
            ///System.out.println("DEBUG: toString() returned: |" + asString + "|");
            String expected = "ISeriesNetServer (system: "+systemObject_.getSystemName()+"; name: "+netserver_.getName()+"):";
            ///System.out.println("DEBUG: expected: |" + expected +"|");
            assertCondition(asString.startsWith(expected));
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

///    /**
///    listFileShares()
///    **/
///    public void Var034()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            ResourceList fileSharesList = netser.listFileShares();
///            failed("No exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
///        }
///    }

    /**
    Construct a ISeriesNetServer with system and parm.
    ListFileShares held in ISeriesNetServer.
    Get the string to verify results.

    ISeriesNetServer(system)
    ISeriesNetServer::listFileShares()
    **/
    public void Var020()
    {
        try
        {
            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
            ISeriesNetServerFileShare[] fileSharesList = netser.listFileShares();
            long listLength = fileSharesList.length;
            String[] s = new String[(int)listLength];
            StringBuffer st = new StringBuffer();
            for(int i=0; i<listLength; i++)
            {
                ISeriesNetServerFileShare share = fileSharesList[i];
                s[i] = share.getName() + ": " + share.getPath() + ": " +
                       share.getDescription();
                if(i > 0)
                    st.append("\n"+s[i]);
                else
                    st.append(s[i]);
            }

	    System.out.println("Shares were "+st.toString());

            ///if(st.indexOf("QIBM: /QIBM: IBM Product DirectoriesROOT: /:") != -1)
            if(st.toString().indexOf("QIBM: /QIBM: ") != -1)
                succeeded();
            else if(st.toString().indexOf("ROOT: /: ") != -1)
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
    Construct a ISeriesNetServer with system and parm.
    ListFileShares held in ISeriesNetServer.
    Get the string to verify results.

    ISeriesNetServer(system)
    ISeriesNetServer::listFileShares(shareName)
    **/
    public void Var021()
    {
        try
        {

            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);

	    // 04/05/2022 list all 
            ISeriesNetServerFileShare[] fileSharesList = netser.listFileShares("*ALL" /* "QIBM" */);
            long listLength = fileSharesList.length;
            if (listLength == 0) { 
              fileSharesList = netser.listFileShares("QDIRSRV");
              listLength = fileSharesList.length;
            }
            String[] s = new String[(int)listLength];
            StringBuffer st = new StringBuffer();
            for(int i=0; i<listLength; i++)
            {
                ISeriesNetServerFileShare share = fileSharesList[i];
                s[i] = share.getName() + ": " + share.getPath() + ": " +
                       share.getDescription();
                if(i > 0)
                    st.append("\n"+s[i]);
                else
                    st.append(s[i]);
            }

            ///if(st.equals("QIBM: /QIBM: IBM Product Directories"))
	    if(st.toString().startsWith("QIBM: /QIBM: ")) {
		succeeded();
	    } else if(st.toString().startsWith("QDIRSRV: /QIBM/ProdData/OS400/DirSrv")) {
              succeeded();
	    } else if(st.toString().indexOf("ROOT: /: ") != -1) {
                succeeded();
	    } else { 
                failed("Incorrect string returned. '"+st+"' listLength="+listLength+" String should have been list of shares");
	    }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

///    /**
///    listPrintShares()
///    **/
///    public void Var037()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            ResourceList printSharesList = netser.listPrintShares();
///            failed("No exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
///        }
///    }

    /**
    Construct a ISeriesNetServer with system and parm.
    ListPrintShares held in ISeriesNetServer.
    Get the string to verify results.

    ISeriesNetServer(system)
    ISeriesNetServer::listPrintShares()
    **/
    public void Var022()
    {
        try
        {
            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
            ISeriesNetServerPrintShare[] printSharesList = netser.listPrintShares();
            long listLength = printSharesList.length;
            String[] s = new String[(int)listLength];
            String st = new String();
            for(int i=0; i<listLength; i++)
            {
                ISeriesNetServerPrintShare share = printSharesList[i];
                s[i] = share.getName() + ": " + share.getOutputQueueLibrary()+"/"+share.getOutputQueueName() + ": " +
                       share.getDescription();
                if(i > 0)
                    st = st + s[i];
                else
                    st = s[i];
            }

            if(st.length() == 0)
                succeeded("No print shares were listed.");
            else
                succeeded();
                ///failed("Incorrect string returned.\n" + st);

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify that listShares() returns the union of listFileShares() and listPrintShares().
     **/
    public void Var023()
    {
      try
      {
        ISeriesNetServerShare[] allShares = netserver_.listShares();
        ISeriesNetServerFileShare[] fileShares = netserver_.listFileShares();
        ISeriesNetServerPrintShare[] printShares = netserver_.listPrintShares();
        if (DEBUG) {
          System.out.println("Total shares: " + allShares.length +"; file shares: " + fileShares.length+"; print shares: " + printShares.length);
        }
        if (allShares.length != (fileShares.length + printShares.length)) {
          failed("List lengths mismatch: " + allShares.length +"/"+ fileShares.length +"/"+ printShares.length);
          return;
        }
        // For each element of allShares, verify that it's in exactly one of the other lists.
        Vector<String> allShareNames = new Vector<String>(allShares.length);
        Vector<String> fileShareNames = new Vector<String>(allShares.length);
        Vector<String> printShareNames = new Vector<String>(allShares.length);
        boolean ok = true;
        for (int i=0; i<allShares.length; i++) {
          allShareNames.addElement(allShares[i].getName());
        }
        for (int i=0; i<fileShares.length; i++) {
          fileShareNames.addElement(fileShares[i].getName());
        }
        for (int i=0; i<printShares.length; i++) {
          printShareNames.addElement(printShares[i].getName());
        }
        Enumeration<String> enumeration = allShareNames.elements();
        while (enumeration.hasMoreElements()) {
          String shareName = (String)enumeration.nextElement();
          if (fileShareNames.contains(shareName)) {
            if (printShareNames.contains(shareName)) {
              System.out.println("Share " + shareName + " was reported by both listFileShares() or listPrintShares().");
              ok = false;
            }
          }
          else if (printShareNames.contains(shareName)) {
            if (fileShareNames.contains(shareName)) {
              System.out.println("Share " + shareName + " was reported by both listFileShares() or listPrintShares().");
              ok = false;
            }
          }
          else {
            System.out.println("Share " + shareName + " was reported by listShares() but not by either listFileShares() or listPrintShares().");
            ok = false;
          }
        }

        if (ok) succeeded();
        else failed();
      }
      catch (Exception e) {
        failed(e, "Unexpected exception.");
      }
    }



    /**
    Construct a ISeriesNetServer with system and parm.
    ListPrintShares held in ISeriesNetServer.
    Get the string to verify results.

    ISeriesNetServer(system)
    ISeriesNetServer::listPrintShares(shareName)
    **/
    public void Var024()
    {
        try
        {
            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
            ISeriesNetServerPrintShare[] printSharesList = netser.listPrintShares("BOGUSTOOLBOX");
            long listLength = printSharesList.length;
            String[] s = new String[(int)listLength];
            String st = new String();
            for(int i=0; i<listLength; i++)
            {
                ISeriesNetServerPrintShare share = printSharesList[i];
                s[i] = share.getName() + ": " + share.getOutputQueueLibrary()+"/"+share.getOutputQueueName() + ": " +
                       share.getDescription();
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


///    /**
///    listSessionConnections()
///    **/
///    public void Var040()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            ResourceList sessionConnectionsList = netser.listSessionConnections();
///            failed("No exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
///        }
///    }

// TBD:
///    /**
///    Construct a ISeriesNetServer with system and parm.
///    ListSessionConnections held in ISeriesNetServer.
///    Get the string to verify results.
///
///    ISeriesNetServer(system)
///    ISeriesNetServer::listSessionConnections()
///    **/
///    public void Var029()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
///            ResourceList sessionConnectionsList = netser.listSessionConnections();
///            long listLength = sessionConnectionsList.getListLength();
///            String[] s = new String[(int)listLength];
///            String st = new String();
///            for(int i=0; i<listLength; i++)
///            {
///                ISeriesNetServerConnection connection = sessionConnectionsList[i];
///                s[i] = connection.getID() + ": " + (String)connection.getAttributeValue(ISeriesNetServerConnection.NAME) + ": " +
///                       (String)connection.getAttributeValue(ISeriesNetServerConnection.USER);
///                if(i > 0)
///                    st = st + s[i];
///                else
///                    st = s[i];
///            }
///
///            if(st.equals(""))
///                succeeded();
///            else
///                failed("Incorrect string returned.\n" + st);
///        }
///        catch (Exception e)
///        {
///            failed(e, "Unexpected exception.");
///        }
///    }

///    /**
///    listSessions()
///    **/
///    public void Var042()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            ResourceList sessionsList = netser.listSessions();
///            failed("No exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
///        }
///    }


/// Note: The following variation was moved to INetServerSessionTestcase:

///    /**
///    Construct a ISeriesNetServer with system and parm.
///    ListSessions held in ISeriesNetServer.
///    Get the string to verify results.
///
///    ISeriesNetServer(system)
///    ISeriesNetServer::listSessions()
///    **/
///    public void Var029(int runMode)
///    {
///      if (runMode != ATTENDED && runMode != BOTH) { notApplicable("Attended TC"); return; }
///        try
///        {
///          String sysName = pwrSys_.getSystemName();
///          String text = "Establish at least one session.  The easiest way to do this is to map a drive on your PC to " + sysName + " (net use * \\\\"+sysName+"\\qibm), and opening of a file on that drive.\nDo you have a session established?";
///          if (!VTestUtilities.ask(text)) {
///            failed("This variation requires that a drive be mapped.");
///            return;
///          }
///            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
//////            ISeriesNetServer netser = new ISeriesNetServer(pwrSys_);
///            ///System.out.println ("About to call listSessions().  Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};
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
///                ///failed("Incorrect string returned.\n" + st);
///            ///System.out.println ("Called listSessions().  Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};
///
///        }
///        catch (Exception e)
///        {
///            failed(e, "Unexpected exception.");
///        }
///    }

///    /**
///    listShareConnections()
///    **/
///    public void Var044()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer();
///            ResourceList shareConnectionsList = netser.listShareConnections();
///            failed("No exception");
///        }
///        catch(Exception e)
///        {
///            assertExceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set.");
///        }
///    }


// TBD:
///    /**
///    Construct a ISeriesNetServer with system and parm.
///    ListShareConnections held in ISeriesNetServer.
///    Get the string to verify results.
///
///    ISeriesNetServer(system)
///    ISeriesNetServer::listShareConnections()
///    **/
///    public void Var031()
///    {
///        try
///        {
///            ISeriesNetServer netser = new ISeriesNetServer(systemObject_);
///            ResourceList shareConnectionsList = netser.listShareConnections();
///            long listLength = shareConnectionsList.getListLength();
///            String[] s = new String[(int)listLength];
///            String st = new String();
///            for(int i=0; i<listLength; i++)
///            {
///                ISeriesNetServerConnection connection = shareConnectionsList[i];
///                s[i] = connection.getID() + ": " + (String)connection.getAttributeValue(ISeriesNetServerConnection.NAME) + ": " +
///                       (String)connection.getAttributeValue(ISeriesNetServerConnection.USER);
///                if(i > 0)
///                    st = st + s[i];
///                else
///                    st = s[i];
///            }
///
///            if(st.equals(""))
///                succeeded();
///            else
///                failed("Incorrect string returned.\n" + st);
///
///        }
///        catch (Exception e)
///        {
///            failed(e, "Unexpected exception.");
///        }
///    }

///    /**
///    isAllowSystemName()
///     **/
///    public void Var029()
///    {
///        try
///        {
///           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);
///           if (ns.isAllowSystemName() != true &&
///               ns.isAllowSystemName() != false )
///               failed("ISeriesNetServer allow system name is not valid.");
///            else
///               succeeded();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }


    /**
    isAllowSystemName(), setAllowSystemName()
    **/
    public void Var025(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) { notApplicable("Attended TC"); return; }
      if (!isOkToStopNetServer()) {
        failed("This variation needs to stop and restart the NetServer process.");
        return;
      }
        try
        {
            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
            boolean oldVal = ns.isAllowSystemName();
            boolean newVal = (oldVal ? false : true);
            System.out.println("Allow System Name: old: " + oldVal + "; new: " + newVal);
            ns.setAllowSystemName(newVal);
            ///System.out.println ("About to commit changes.  Press ENTER to continue"); try { System.in.read (); } catch (Exception exc) {};
            ns.commitChanges();
            ///System.out.println ("Committed changes.  Press ENTER to continue"); try { System.in.read (); } catch (Exception exc) {};
            if (!stopAndStart(false)) {
              failed();
              return;
            }

            ///System.out.println ("Stopped/restarted.  About to refresh. Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};

            ns.refresh();
            ///System.out.println ("Refreshed.  Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};

            if (ns.isAllowSystemName() != newVal)
               failed("ISeriesNetServer allow system name was not changed.");
            else {
               succeeded();
               ns.setAllowSystemName(oldVal);
               ns.commitChanges();
            }
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    isAutoStart(), where user doesn't have *IOSYSCFG special authority.
    Verify that exception is thrown.
     **/
    public void Var026()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);
           boolean val = ns.isAutoStart();
           failed ("Didn't throw exception.  Try a userid without IOSYSCFG auth. got "+val);
        }
        catch(Exception e)
        {
          assertExceptionIsInstanceOf (e, "com.ibm.as400.access.AS400SecurityException");
        }
    }


    /**
    isAutoStart(), where user has *IOSYSCFG special authority.
     **/
    public void Var027()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
           if ( ns.isAutoStart() != true &&
                ns.isAutoStart() != false )
               failed("ISeriesNetServer auto start is not valid.");
            else
               succeeded();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setAutoStart()
    **/
    public void Var028()
    {
        try
        {
            boolean orig = netserverPwr_.isAutoStart();
            boolean newVal = (orig ? false : true);
            ///System.out.println("AutoStart: original=="+orig+"; new=="+newVal);
            netserverPwr_.setAutoStart(newVal);
            netserverPwr_.commitChanges();
            // Note: setAutoStart() doesn't require a restart in order to take effect.
            Thread.sleep(3000);  // Wait a few seconds for it to take effect.

            if (netserverPwr_.isAutoStart() != newVal)
               failed("ISeriesNetServer autostart is invalid.");
            else
               succeeded();
            netserverPwr_.setAutoStart(orig);
            netserverPwr_.commitChanges();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getBrowsingInterval()
     **/
    public void Var029()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);
           if (ns.getBrowsingInterval() >= 0  )
              succeeded();
            else
              failed("ISeriesNetServer browsing interval is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setBrowsingInterval
///    **/
///    public void Var034()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            ns.setBrowsingInterval(20000);
///            ns.commitChanges();
///
///            if (ns.getBrowsingInterval() != 20000)
///               failed("ISeriesNetServer browsing interval pending is invalid.");
///            else
///               succeeded();
///
///            ns.setBrowsingInterval(0);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }
///

    /**
    getCCSID()
     **/
    public void Var030()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
           ///ns.end();
           ///Thread.sleep(STOP_WAIT_SECONDS*1000);
           ///ns.start();
           ///Thread.sleep(STOP_WAIT_SECONDS*1000);

           if (ns.getCCSID() >= 0 )
              succeeded();
            else
               failed("ISeriesNetServer ccsid is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setCCSID
///    **/
///    public void Var036()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            ns.setCCSID(424);
///            ns.commitChanges();
///
///            if (ns.getCCSID() != 424)
///               failed("ISeriesNetServer ccsid pending is invalid.");
///            else
///               succeeded();
///
///            ns.setCCSID(0);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }


    /**
    getDescription()
     **/
    public void Var031()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);

           if (ns.getDescription().length() > 0  )
              succeeded();
            else
              failed("ISeriesNetServer description length is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setDescription()
///    **/
///    public void Var038()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            String orig = ns.getDescription();
///            ns.setDescription("Toolbox ISeriesNetServer Description");
///            ns.commitChanges();
///
///            if (ns.getDescription().equals("Toolbox ISeriesNetServer Description"))
///               succeeded();
///            else
///               failed("ISeriesNetServer description pending is invalid.");
///
///            ns.setDescription(orig);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }


    /**
    getDomainName()
     **/
    public void Var032()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);
           if (ns.getDomainName().length() > 0)
              succeeded();
            else
              failed("ISeriesNetServer domainName is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setDomainName()
///    **/
///    public void Var040()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            String orig = ns.getDomainName();
///            String newVal = systemObject_.getSystemName();
///            System.out.println("Domain name: Original==|"+orig+"|, new==|"+newVal+"|");
///            ns.setDomainName(newVal);
///            ns.commitChanges();
///            stopAndStart(false);
///            ns.refresh();
///
///            if (ns.getDomainName().equalsIgnoreCase(newVal))
///               succeeded();
///            else
///               failed("ISeriesNetServer domainName pending is invalid.");
///
///            ns.setDomainName(orig);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }


    /**
    getGuestUserProfile()
     **/
    public void Var033()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);

           if (ns.getGuestUserProfile().length() >= 0  )
              succeeded();
            else
              failed("ISeriesNetServer guest user profile length is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setGuestUserProfile()
    **/
    public void Var034(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) { notApplicable("Attended TC"); return; }
      if (!isOkToStopNetServer()) {
        failed("This variation needs to stop and restart the NetServer process.");
        return;
      }
        try
        {
            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);

            CommandCall cmd = new CommandCall(pwrSys_);
            if (!cmd.run("CRTUSRPRF USRPRF(NETSRVTEST) PASSWORD(JTEAM1) TEXT('Toolbox test profile ')")) {
              failed("Setup for this variation failed.");
              AS400Message[] messagelist = cmd.getMessageList();
              for (int i = 0; i < messagelist.length; ++i)
              {
                // Show each message.
                  System.out.println(messagelist[i].getText());
              }
              return;
            }

            String orig = ns.getGuestUserProfile();

            System.out.println("Original guest user profile: |" + orig + "|");

            ns.setGuestUserProfile("NETSRVTEST");
            ns.commitChanges();
            stopAndStart(true);
            ns.refresh();

            System.out.println("Updated guest user profile: |" + ns.getGuestUserProfile() + "|");
            ///System.out.println ("Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};

            if (ns.getGuestUserProfile().equals("NETSRVTEST"))
               succeeded();
            else
               failed("ISeriesNetServer guest user profile pending is invalid.");

            ns.setGuestUserProfile(orig);
            ns.commitChanges();
            stopAndStart(true);

            if (!cmd.run("DLTUSRPRF USRPRF(NETSRVTEST)"))
               System.out.println("DLTUSRPRF USRPRF(NETSRVTEST) - failed.  Manually delete profile on AS/400");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getIdleTimeout()
     **/
    public void Var035()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);

           if (ns.getIdleTimeout() > 0 ||
               ns.getIdleTimeout() == -1)
              succeeded();
            else
              failed("ISeriesNetServer idle timeout is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setAttributeValue(ISeriesNetServer.IDLE_TIMEOUT_PENDING)
///    **/
///    public void Var044()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            int orig = ns.getIdleTimeout();
///            ns.setIdleTimeout(20000);
///            ns.commitChanges();
///
///            if (ns.getIdleTimeout() == 20000)
///               succeeded();
///            else
///               failed("ISeriesNetServer idle timeout pending is invalid.");
///
///            ns.setIdleTimeout(orig);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }


    /**
    isLogonServer()
     **/
    public void Var036()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);

           if (ns.isLogonServer() == true ||
               ns.isLogonServer() == false )
              succeeded();
            else
              failed("ISeriesNetServer logon support is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setLogonServer()
///    **/
///    public void Var046()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            boolean orig = ns.isLogonServer();
///            if (orig == true)
///               ns.setLogonServer(false);
///            else
///               ns.setLogonServer(true);
///
///            ns.commitChanges();
///
///            if (orig == true)
///            {
///               if (ns.isLogonServer() == false)
///                  succeeded();
///               else
///                  failed("ISeriesNetServer logon support pending is invalid.");
///            }
///            else
///            {
///               if (ns.isLogonServer() == true)
///                  succeeded();
///               else
///                  failed("ISeriesNetServer logon support pending is invalid.");
///            }
///
///            ns.setLogonServer(orig);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }



    /**
    getName()
     **/
    public void Var037()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);

           if (ns.getName().length() > 0 )
              succeeded();
            else
              failed("ISeriesNetServer name length is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setName()
///    **/
///    public void Var048()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            String orig = ns.getName();
///            ns.setName("TOOLBOXPENDING");
///            ns.commitChanges();
///
///            if (ns.getName().equals("TOOLBOXPENDING"))
///               succeeded();
///            else
///               failed("ISeriesNetServer name pending is invalid.");
///
///            ns.setName(orig);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }


    /**
    isWINSServer()
     **/
    public void Var038()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);

           if (ns.isWINSServer() == true ||
               ns.isWINSServer() == false )
              succeeded();
            else
              failed("ISeriesNetServer wins enablement is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setWINSServer()
///    **/
///    public void Var050()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            boolean orig = ns.isWINSServer();
///
///            if (orig == true)
///               ns.setWINSServer(false);
///            else
///               ns.setWINSServer(true);
///
///            ns.commitChanges();
///
///            if (orig == true)
///            {
///               if (ns.isWINSServer() == false)
///                  succeeded();
///               else
///                  failed("ISeriesNetServer wins enablement pending is invalid.");
///            }
///            else
///            {
///               if (ns.isWINSServer() == true)
///                  succeeded();
///               else
///                  failed("ISeriesNetServer wins enablement pending is invalid.");
///            }
///
///            ns.setWINSServer(orig);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }


    /**
    getWINSPrimaryAddress()
     **/
    public void Var039()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);

           if (ns.getWINSPrimaryAddress().length() >= 0 )
              succeeded();
            else
              failed("ISeriesNetServer wins primary address is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setWINSPrimaryAddress()
///    **/
///    public void Var052()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            String orig = ns.getWINSPrimaryAddress();
///
///            ns.setWINSPrimaryAddress("9.5.100.77");
///            ns.commitChanges();
///
///            if (ns.getWINSPrimaryAddress().equals("9.5.100.77"))
///               succeeded();
///            else
///               failed("ISeriesNetServer wins primary address pending is invalid.");
///
///
///            ns.setWINSPrimaryAddress(orig);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }

    /**
    getWINSScopeID()
     **/
    public void Var040()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);

           if (ns.getWINSScopeID().length() >= 0 )
              succeeded();
            else
              failed("ISeriesNetServer wins scope id is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setWINSScopeID()
///    **/
///    public void Var054()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            String orig = ns.getWINSScopeID();
///
///            ns.setWINSScopeID("9.5.100.79");
///            ns.commitChanges();
///
///            if (ns.getWINSScopeID().equals("9.5.100.79"))
///               succeeded();
///            else
///               failed("ISeriesNetServer wins scope id pending is invalid.");
///
///
///            ns.setWINSScopeID(orig);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }


    /**
    getWINSSecondaryAddress()
     **/
    public void Var041()
    {
        try
        {
           ISeriesNetServer ns = new ISeriesNetServer(systemObject_);

           if (ns.getWINSSecondaryAddress().length() >= 0 )
              succeeded();
            else
              failed("ISeriesNetServer wins secondary address is not valid.");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


///    /**
///    setWINSSecondaryAddress()
///    **/
///    public void Var056()
///    {
///        try
///        {
///            ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
///            String orig = ns.getWINSSecondaryAddress();
///
///            ns.setWINSSecondaryAddress("9.5.100.77");
///            ns.commitChanges();
///
///            if (ns.getWINSSecondaryAddress().equals("9.5.100.77"))
///               succeeded();
///            else
///               failed("ISeriesNetServer wins primary address pending is invalid.");
///
///
///            ns.setWINSSecondaryAddress(orig);
///            ns.commitChanges();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }

///    /**
///     Serialization.  Verify that the object is properly serialized
///     when the GUEST_USER_PROFILE attributes are not set.
///
///     ISeriesNetServer
///    **/
///   public void Var046()
///   {
///      try
///      {
///         ISeriesNetServer ns = new ISeriesNetServer(systemObject_);
///         ISeriesNetServer ns2 = (ISeriesNetServer) NetServerTest.serialize (ns);
///         assertCondition (ns2.getGuestUserProfile().equals(""));
///      }
///      catch (Exception e)
///      {
///         failed(e, "Unexpected exception");
///      }
///   }

  /**
   Ensure that ISeriesNetServer will correctly serialize and deserialize itself.
   Verify that system name and autostart are preserved.
   Note that the user will be prompted for the iSeries password.
   **/
  public void Var042(int runMode)
  {
    if (runMode != ATTENDED && runMode != BOTH) { notApplicable("Attended TC"); return; }
    if (!isOkToStopNetServer()) {
      failed("This variation needs to stop and restart the NetServer process.");
      return;
    }
    StringBuffer failMsg = new StringBuffer();
    try
    {
      ISeriesNetServer ns1 = new ISeriesNetServer(systemObject_);
      FileOutputStream fos =
        new FileOutputStream("INetServerTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(ns1);
      fos.close();
      FileInputStream fis =
        new FileInputStream("INetServerTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      ISeriesNetServer ns2 = (ISeriesNetServer) ois.readObject();
      fis.close();
      String systemName1 = ns1.getSystem().getSystemName();
      String systemName2 = ns2.getSystem().getSystemName();
      String serverName1 = ns1.getName();
      String serverName2 = ns2.getName();

      if (!systemName1.equals(systemName2))
        failMsg.append("System name not preserved. " +
                       "Expected " + systemName1 +
                       ", got " + systemName2 + ".\n");
      if (!serverName1.equals(serverName2))
        failMsg.append("Server name not preserved. " +
                       "Expected " + serverName1 +
                       ", got " + serverName2 + ".\n");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    finally
    {
      File file = new File("INetServerTestcase.ser");
      try { file.delete(); } catch (Exception e) {e.printStackTrace();}
    }
    if (failMsg.length() == 0)
      succeeded();
    else
      failed(failMsg.toString());
  }


    /**
    getAuthenticationMethod()
     **/
    public void Var043()
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

        ISeriesNetServer ns = new ISeriesNetServer(systemObject_);
        int authMethod = ns.getAuthenticationMethod();
        assertCondition(authMethod >= ISeriesNetServerFileShare.NOT_ENABLED &&
                        authMethod  <= ISeriesNetServerFileShare.ENABLED_AND_MIXED,
                        "The reported attribute value is invalid: " + authMethod);
      }
      catch (Exception e)
      {
        failed (e, "Unexpected Exception");
      }
    }


    /**
    setAuthenticationMethod()
    **/
    public void Var044(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) { notApplicable("Attended TC"); return; }
      if (!isOkToStopNetServer()) {
        failed("This variation needs to stop and restart the NetServer process.");
        return;
      }
      try
      {
        if (systemObject_.getVersion() < 5 ||
            (systemObject_.getVersion() == 5 &&
             systemObject_.getRelease() < 2))
        {
          notApplicable("Server is pre-V5R2.");
          return;
        }

        ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
        int orig = ns.getAuthenticationMethod();

        int newVal;
        if (orig == 0) newVal = 1;
        else           newVal = 0;
        ns.setAuthenticationMethod(newVal);

        // Note: NetServer will let us reset the "authentication method" attribute
        // only if Kerberos is configured on the system.
        if (useKerberos_)
        {
          ns.commitChanges();
          stopAndStart(false);
          ns.refresh();

          if (ns.getAuthenticationMethod() == newVal)
            succeeded();
          else
            failed("ISeriesNetServer authentication method pending is invalid.");

          ///System.out.println ("Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};
        }
        else  // Kerberos is not configured
        {
          try {
            ns.commitChanges();
            System.out.println("Exception was not thrown.  Kerberos might be configured on the system.");
            stopAndStart(false);
            ns.refresh();
            if (ns.getAuthenticationMethod() == newVal)
              succeeded();
            else
              failed("ISeriesNetServer authentication method pending is invalid.");
          }
          catch (Exception exc) {
            assertExceptionIsInstanceOf (exc, "com.ibm.as400.access.AS400Exception");
            // Error text should be "Error configuring iSeries Support for Windows Network Neighborhood".
          }
        }

        ns.setAuthenticationMethod(orig);
        ns.commitChanges();
        stopAndStart(false);
        return;
      }
      catch (Exception e)
      {
        failed (e, "Unexpected Exception");
      }
    }


/////////////////////// Message Authentication

    /**
    getMessageAuthentication()
     **/
    public void Var045()
    {
      try
      {
        ISeriesNetServer ns = new ISeriesNetServer(systemObject_);
        int attrVal = ns.getMessageAuthentication();
        assertCondition(attrVal >= 0 && attrVal <= 2, "The reported attribute value is invalid: " + attrVal);
      }
      catch (Exception e)
      {
        failed (e, "Unexpected Exception");
      }
    }


    /**
    setMessageAuthentication()
    **/
    public void Var046(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) { notApplicable("Attended TC"); return; }
      if (!isOkToStopNetServer()) {
        failed("This variation needs to stop and restart the NetServer process.");
        return;
      }
      try
      {
        ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
        int orig = ns.getMessageAuthentication();

        int newVal;
        if (orig == 0) newVal = 1;
        else           newVal = 0;
        ns.setMessageAuthentication(newVal);

        ns.commitChanges();
        stopAndStart(false);
        ns.refresh();

        if (ns.getMessageAuthentication() == newVal)
          succeeded();
        else {
          if (getSystemVRM() >= 0x00050400)
          { // new attribute added in V5R4
            ns.setMessageAuthentication(orig);
            ns.commitChanges();
            stopAndStart(false);
            failed("ISeriesNetServer messageAuthentication pending is invalid.");
          }
          else notApplicable("Server is pre-V5R4.");
        }
      }
      catch (Exception e)
      {
        failed (e, "Unexpected Exception");
      }
    }


/////////////////////// Minimum Message Severity

    /**
    getMinimumMessageSeverity()
     **/
    public void Var047()
    {
      try
      {
        ISeriesNetServer ns = new ISeriesNetServer(systemObject_);
        int attrVal = ns.getMinimumMessageSeverity();
        assertCondition(attrVal >= -1, "The reported attribute value is invalid: " + attrVal);
      }
      catch (Exception e)
      {
        failed (e, "Unexpected Exception");
      }
    }


    /**
    setMinimumMessageSeverity()
    **/
    public void Var048(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) { notApplicable("Attended TC"); return; }
      if (!isOkToStopNetServer()) {
        failed("This variation needs to stop and restart the NetServer process.");
        return;
      }
      try
      {
        ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
        int orig = ns.getMinimumMessageSeverity();

        int newVal;
        if (orig == -1) newVal = 0;
        else           newVal = -1;
        ns.setMinimumMessageSeverity(newVal);

        ns.commitChanges();
        stopAndStart(false);
        ns.refresh();

        if (ns.getMinimumMessageSeverity() == newVal)
          succeeded();
        else {
          if (getSystemVRM() >= 0x00050400)
          { // new attribute added in V5R4
            ns.setMinimumMessageSeverity(orig);
            ns.commitChanges();
            stopAndStart(false);
            failed("ISeriesNetServer minimumMessageSeverity pending is invalid.");
          }
          else notApplicable("Server is pre-V5R4.");
        }

        ///System.out.println ("Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};

        return;
      }
      catch (Exception e)
      {
        failed (e, "Unexpected Exception");
      }
    }


/////////////////////// LAN Manager Authentication

    /**
    getLANManagerAuthentication()
     **/
    public void Var049()
    {
      try
      {
        ISeriesNetServer ns = new ISeriesNetServer(systemObject_);
        int attrVal = ns.getLANManagerAuthentication();
        assertCondition(attrVal == 0 || attrVal == 1, "The reported attribute value is invalid: " + attrVal);
      }
      catch (Exception e)
      {
        failed (e, "Unexpected Exception");
      }
    }


    /**
    setLANManagerAuthentication()
    **/
    public void Var050(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) { notApplicable("Attended TC"); return; }
      if (!isOkToStopNetServer()) {
        failed("This variation needs to stop and restart the NetServer process.");
        return;
      }
      try
      {
        ISeriesNetServer ns = new ISeriesNetServer(pwrSys_);
        int orig = ns.getLANManagerAuthentication();

        int newVal;
        if (orig == 0) newVal = 1;
        else           newVal = 0;
        ns.setLANManagerAuthentication(newVal);

        ns.commitChanges();
        stopAndStart(false);
        ns.refresh();

        if (ns.getLANManagerAuthentication() == newVal)
          succeeded();
        else {
          if (getSystemVRM() >= 0x00050400)
          { // new attribute added in V5R4
            ns.setLANManagerAuthentication(orig);
            ns.commitChanges();
            stopAndStart(false);
            failed("ISeriesNetServer LANManagerAuthentication pending is invalid.");
          }
          else notApplicable("Server is pre-V5R4.");
        }
      }
      catch (Exception e)
      {
        failed (e, "Unexpected Exception");
      }
    }

///////////////////////



    /**
     Exercise all getters and setters.
     Verify that reported attributes are not changed until commit() and end()/start().
     **/
    public void Var051(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) { notApplicable("Attended TC"); return; }
      if (!isOkToStopNetServer()) {
        failed("This variation needs to stop and restart the NetServer process.");
        return;
      }
      final int NUM_ATTRS = 18;
      final int NUM_BLOCKS = 7;

      boolean [][] results = new boolean[NUM_BLOCKS][NUM_ATTRS];
      for (int blockNum=0; blockNum<NUM_BLOCKS; blockNum++) {
        for (int attrNum=0; attrNum<NUM_ATTRS; attrNum++) {
          results[blockNum][attrNum] = true;
        }
      }

      ISeriesNetServer ns = new ISeriesNetServer(systemObject_);
      ISeriesNetServer nsPower = new ISeriesNetServer(pwrSys_);

      boolean committedChanges = false;

      boolean allowSysName_orig = false;
      int authMethod_orig = 0;
      boolean autostart_orig = false;
      String desc_orig = "";
      String domain_orig = "";
      String guestUser_orig = "";
      String serverName_orig = "";
      String winsPrim_orig = "";
      String winsScope_orig = "";
      String winsSec_orig = "";
      int browseInt_orig = 0;
      int ccsid_orig = 0;
      int idleTimeout_orig = 0;
      boolean logonServer_orig = false;
      boolean winsServer_orig = false;
      int messageAuthentication_orig = 0;
      int minimumMessageSeverity_orig = 0;
      int lanManagerAuthentication_orig = 0;

      try
      {
        CommandCall cmd = new CommandCall(pwrSys_);
        if (!cmd.run("CRTUSRPRF USRPRF(NETSRVTEST) PASSWORD(JTEAM1) TEXT('Toolbox test profile ')")) {
          System.out.println("Setup for this variation failed.");
          AS400Message[] messagelist = cmd.getMessageList();
          for (int i = 0; i < messagelist.length; ++i)
          {
            // Show each message.
            System.out.println(messagelist[i].getText());
          }
          failed("Unable to setup test Guest User Profile.");
          return;
        }

        // Get current values of all attributes.
        allowSysName_orig = nsPower.isAllowSystemName();
        authMethod_orig = nsPower.getAuthenticationMethod();
        autostart_orig = nsPower.isAutoStart();
        desc_orig = nsPower.getDescription();
        domain_orig = nsPower.getDomainName();
        guestUser_orig = nsPower.getGuestUserProfile();
        serverName_orig = nsPower.getName();
        winsPrim_orig = nsPower.getWINSPrimaryAddress();
        winsScope_orig = nsPower.getWINSScopeID();
        winsSec_orig = nsPower.getWINSSecondaryAddress();
        browseInt_orig = nsPower.getBrowsingInterval();
        ccsid_orig = nsPower.getCCSID();
        idleTimeout_orig = nsPower.getIdleTimeout();
        logonServer_orig = nsPower.isLogonServer();
        winsServer_orig = nsPower.isWINSServer();
        messageAuthentication_orig = nsPower.getMessageAuthentication();
        minimumMessageSeverity_orig = nsPower.getMinimumMessageSeverity();
        lanManagerAuthentication_orig = nsPower.getLANManagerAuthentication();

        System.out.println("Original settings:");
        System.out.println(
        allowSysName_orig + ", " +
        authMethod_orig + ", " +
        autostart_orig + ", " +
        desc_orig + ", " +
        domain_orig + ", " +
        guestUser_orig + ", " +
        serverName_orig + ", " +
        winsPrim_orig + ", " +
        winsScope_orig + ", " +
        winsSec_orig + ", " +
        browseInt_orig + ", " +
        ccsid_orig + ", " +
        idleTimeout_orig + ", " +
        logonServer_orig + ", " +
        winsServer_orig + ", " +
        messageAuthentication_orig + ", " +
        minimumMessageSeverity_orig + ", " +
        lanManagerAuthentication_orig
                           );


        // Set up new values that are different.
        boolean allowSysName_new = (allowSysName_orig ? false : true);
        int authMethod_new = (authMethod_orig == 0 ? 1 : 0);
        if (!useKerberos_) authMethod_new = 0;  // only works if using Kerberos
        boolean autostart_new = (autostart_orig ? false : true);
        String desc_new = new String(desc_orig + "New");
        String domain_new = new String(domain_orig + "NEW");
        String guestUser_new = "NETSRVTEST";
        String serverName_new = "SRVR9999";
        String winsPrim_new = "9.5.100.91";
        String winsScope_new = "9.5.100.92";
        String winsSec_new = "9.5.100.93";
        int browseInt_new = (browseInt_orig == 999 ? 888 : 999);
        int ccsid_new = (ccsid_orig == 424 ? 437 : 424);
        int idleTimeout_new = (idleTimeout_orig == 10 ? 15 : 10);
        boolean logonServer_new = (logonServer_orig ? false : true);
        boolean winsServer_new = (winsServer_orig ? false : true);
        int messageAuthentication_new = (messageAuthentication_orig == 0 ? 1 : 0);
        int minimumMessageSeverity_new = (minimumMessageSeverity_orig == -1 ? 0 : -1);
        int lanManagerAuthentication_new = (lanManagerAuthentication_orig == 0 ? 1 : 0);

        System.out.println("New settings:");
        System.out.println(
        allowSysName_new + ", " +
        authMethod_new + ", " +
        autostart_new + ", " +
        desc_new + ", " +
        domain_new + ", " +
        guestUser_new + ", " +
        serverName_new + ", " +
        winsPrim_new + ", " +
        winsScope_new + ", " +
        winsSec_new + ", " +
        browseInt_new + ", " +
        ccsid_new + ", " +
        idleTimeout_new + ", " +
        logonServer_new + ", " +
        winsServer_new + ", " +
        messageAuthentication_new + ", " +
        minimumMessageSeverity_new + ", " +
        lanManagerAuthentication_new
                           );

        // Call the setters to set the new values.
        nsPower.setAllowSystemName(allowSysName_new);
        nsPower.setAuthenticationMethod(authMethod_new);
        nsPower.setAutoStart(autostart_new);
        nsPower.setDescription(desc_new);
        nsPower.setDomainName(domain_new);
        nsPower.setGuestUserProfile(guestUser_new);
        nsPower.setName(serverName_new);
        nsPower.setWINSPrimaryAddress(winsPrim_new);
        nsPower.setWINSScopeID(winsScope_new);
        nsPower.setWINSSecondaryAddress(winsSec_new);
        nsPower.setBrowsingInterval(browseInt_new);
        nsPower.setCCSID(ccsid_new);
        nsPower.setIdleTimeout(idleTimeout_new);
        nsPower.setLogonServer(logonServer_new);
        nsPower.setWINSServer(winsServer_new);
        nsPower.setMessageAuthentication(messageAuthentication_new);
        nsPower.setMinimumMessageSeverity(minimumMessageSeverity_new);
        nsPower.setLANManagerAuthentication(lanManagerAuthentication_new);

        // BLOCK 0:
        // Verify that the getters still report the original values.  (Changes shouldn't take effect until restart.)
        System.out.println("DEBUG: BLOCK 0");
        if (ns.isAllowSystemName() != allowSysName_orig) results[0][0] = false;
        if (ns.getAuthenticationMethod() != authMethod_orig) results[0][1] = false;
        ///if (ns.isAutoStart() != false) results[0][2] = false;  // If no *IOSYSCFG, isAutoStart() throws exception.
        if (!ns.getDescription().equals(desc_orig)) results[0][3] = false;
        if (!ns.getDomainName().equals(domain_orig)) results[0][4] = false;
        if (!ns.getGuestUserProfile().equals(guestUser_orig)) results[0][5] = false;
        if (!ns.getName().equals(serverName_orig)) results[0][6] = false;
        if (!ns.getWINSPrimaryAddress().equals(winsPrim_orig)) results[0][7] = false;
        if (!ns.getWINSScopeID().equals(winsScope_orig)) results[0][8] = false;
        if (!ns.getWINSSecondaryAddress().equals(winsSec_orig)) results[0][9] = false;
        if (ns.getBrowsingInterval() != browseInt_orig) results[0][10] = false;
        if (ns.getCCSID() != ccsid_orig) results[0][11] = false;
        if (ns.getIdleTimeout() != idleTimeout_orig) results[0][12] = false;
        if (ns.isLogonServer() != logonServer_orig) results[0][13] = false;
        if (ns.isWINSServer() != winsServer_orig) results[0][14] = false;
        if (ns.getMessageAuthentication() != messageAuthentication_orig) results[0][15] = false;
        if (ns.getMinimumMessageSeverity() != minimumMessageSeverity_orig) results[0][16] = false;
        if (ns.getLANManagerAuthentication() != lanManagerAuthentication_orig) results[0][17] = false;

        // Refresh the attributes.
        ///ns.refresh();
        ///nsPower.refresh();

        // BLOCK 1:
        // Verify that the getters still report the original values.  (Changes shouldn't take effect until restart.)
        System.out.println("DEBUG: BLOCK 1");
        if (ns.isAllowSystemName() != allowSysName_orig) results[1][0] = false;
        if (ns.getAuthenticationMethod() != authMethod_orig) results[1][1] = false;
        ///if (ns.isAutoStart() != autostart_orig) {} results[1][2] = false;  // If no *IOSYSCFG, isAutoStart() throws exception.
        if (!ns.getDescription().equals(desc_orig)) results[1][3] = false;
        if (!ns.getDomainName().equals(domain_orig)) results[1][4] = false;
        if (!ns.getGuestUserProfile().equals(guestUser_orig)) results[1][5] = false;
        if (!ns.getName().equals(serverName_orig)) results[1][6] = false;
        if (!ns.getWINSPrimaryAddress().equals(winsPrim_orig)) results[1][7] = false;
        if (!ns.getWINSScopeID().equals(winsScope_orig)) results[1][8] = false;
        if (!ns.getWINSSecondaryAddress().equals(winsSec_orig)) results[1][9] = false;
        if (ns.getBrowsingInterval() != browseInt_orig) results[1][10] = false;
        if (ns.getCCSID() != ccsid_orig) results[1][11] = false;
        if (ns.getIdleTimeout() != idleTimeout_orig) results[1][12] = false;
        if (ns.isLogonServer() != logonServer_orig) results[1][13] = false;
        if (ns.isWINSServer() != winsServer_orig) results[1][14] = false;
        if (ns.getMessageAuthentication() != messageAuthentication_orig) results[1][15] = false;
        if (ns.getMinimumMessageSeverity() != minimumMessageSeverity_orig) results[1][16] = false;
        if (ns.getLANManagerAuthentication() != lanManagerAuthentication_orig) results[1][17] = false;

        // Commit the changes.
        committedChanges = true;
        nsPower.commitChanges();

        // BLOCK 2:
        // Verify that the getters still report the original values.  (Changes shouldn't take effect until restart.)
        System.out.println("DEBUG: BLOCK 2");
        if (ns.isAllowSystemName() != allowSysName_orig) results[2][0] = false;
        if (ns.getAuthenticationMethod() != authMethod_orig) results[2][1] = false;
        ///if (ns.isAutoStart() != autostart_orig) {} results[2][2] = false;  // If no *IOSYSCFG, isAutoStart() throws exception.
        if (!ns.getDescription().equals(desc_orig)) results[2][3] = false;
        if (!ns.getDomainName().equals(domain_orig)) results[2][4] = false;
        if (!ns.getGuestUserProfile().equals(guestUser_orig)) results[2][5] = false;
        if (!ns.getName().equals(serverName_orig)) results[2][6] = false;
        if (!ns.getWINSPrimaryAddress().equals(winsPrim_orig)) results[2][7] = false;
        if (!ns.getWINSScopeID().equals(winsScope_orig)) results[2][8] = false;
        if (!ns.getWINSSecondaryAddress().equals(winsSec_orig)) results[2][9] = false;
        if (ns.getBrowsingInterval() != browseInt_orig) results[2][10] = false;
        if (ns.getCCSID() != ccsid_orig) results[2][11] = false;
        if (ns.getIdleTimeout() != idleTimeout_orig) results[2][12] = false;
        if (ns.isLogonServer() != logonServer_orig) results[2][13] = false;
        if (ns.isWINSServer() != winsServer_orig) results[2][14] = false;
        if (ns.getMessageAuthentication() != messageAuthentication_orig) results[2][15] = false;
        if (ns.getMinimumMessageSeverity() != minimumMessageSeverity_orig) results[2][16] = false;
        if (ns.getLANManagerAuthentication() != lanManagerAuthentication_orig) results[2][17] = false;

        // BLOCK 3:
        // Verify that the getters still report the original values.  (Changes shouldn't take effect until restart.)
        System.out.println("DEBUG: BLOCK 3");
        if (nsPower.isAllowSystemName() != allowSysName_orig) results[3][0] = false;
        if (nsPower.getAuthenticationMethod() != authMethod_orig) results[3][1] = false;
        // Note: Changes in autostart attribute take effect upon commitChanges().
        if (nsPower.isAutoStart() != autostart_new) results[3][2] = false;
        if (!nsPower.getDescription().equals(desc_orig)) results[3][3] = false;
        if (!nsPower.getDomainName().equals(domain_orig)) results[3][4] = false;
        if (!nsPower.getGuestUserProfile().equals(guestUser_orig)) results[3][5] = false;
        if (!nsPower.getName().equals(serverName_orig)) results[3][6] = false;
        if (!nsPower.getWINSPrimaryAddress().equals(winsPrim_orig)) results[3][7] = false;
        if (!nsPower.getWINSScopeID().equals(winsScope_orig)) results[3][8] = false;
        if (!nsPower.getWINSSecondaryAddress().equals(winsSec_orig)) results[3][9] = false;
        if (nsPower.getBrowsingInterval() != browseInt_orig) results[3][10] = false;
        if (nsPower.getCCSID() != ccsid_orig) results[3][11] = false;
        if (nsPower.getIdleTimeout() != idleTimeout_orig) results[3][12] = false;
        if (nsPower.isLogonServer() != logonServer_orig) results[3][13] = false;
        if (nsPower.isWINSServer() != winsServer_orig) results[3][14] = false;
        if (nsPower.getMessageAuthentication() != messageAuthentication_orig) results[3][15] = false;
        if (nsPower.getMinimumMessageSeverity() != minimumMessageSeverity_orig) results[3][16] = false;
        if (nsPower.getLANManagerAuthentication() != lanManagerAuthentication_orig) results[3][17] = false;

        // Stop and restart the server.
        ///stopAndStart(false);
        stopAndStart(false);

        // Refresh the attributes.
        ns.refresh();
        nsPower.refresh();

        // BLOCK 4:
        // Verify that the getters now report the new values.
        System.out.println("DEBUG: BLOCK 4");
        if (ns.isAllowSystemName() != allowSysName_new) results[4][0] = false;
        if (ns.getAuthenticationMethod() != authMethod_new) results[4][1] = false;
        ///if (ns.isAutoStart() != autostart_new) {} results[4][2] = false;  // If no *IOSYSCFG, isAutoStart() throws exception.
        if (!ns.getDescription().equals(desc_new)) results[4][3] = false;
        if (!ns.getDomainName().equals(domain_new)) results[4][4] = false;
        if (!ns.getGuestUserProfile().equals(guestUser_new)) results[4][5] = false;
        if (!ns.getName().equals(serverName_new)) results[4][6] = false;
        if (!ns.getWINSPrimaryAddress().equals(winsPrim_new)) results[4][7] = false;
        if (!ns.getWINSScopeID().equals(winsScope_new)) results[4][8] = false;
        if (!ns.getWINSSecondaryAddress().equals(winsSec_new)) results[4][9] = false;
        if (ns.getBrowsingInterval() != browseInt_new) results[4][10] = false;
        if (ns.getCCSID() != ccsid_new) results[4][11] = false;
        if (ns.getIdleTimeout() != idleTimeout_new) results[4][12] = false;
        if (ns.isLogonServer() != logonServer_new) results[4][13] = false;
        if (ns.isWINSServer() != winsServer_new) results[4][14] = false;
        if (getSystemVRM() >= 0x00050400)
        { // new attributes added in V5R4
          if (ns.getMessageAuthentication() != messageAuthentication_new) results[4][15] = false;
          if (ns.getMinimumMessageSeverity() != minimumMessageSeverity_new) results[4][16] = false;
          if (ns.getLANManagerAuthentication() != lanManagerAuthentication_new) results[4][17] = false;
        }

        // BLOCK 5:
        // Verify that the getters now report the new values.
        System.out.println("DEBUG: BLOCK 5");
        if (nsPower.isAllowSystemName() != allowSysName_new) results[5][0] = false;
        if (nsPower.getAuthenticationMethod() != authMethod_new) results[5][1] = false;
        if (nsPower.isAutoStart() != autostart_new) results[5][2] = false;
        if (!nsPower.getDescription().equals(desc_new)) results[5][3] = false;
        if (!nsPower.getDomainName().equals(domain_new)) results[5][4] = false;
        if (!nsPower.getGuestUserProfile().equals(guestUser_new)) results[5][5] = false;
        if (!nsPower.getName().equals(serverName_new)) results[5][6] = false;
        if (!nsPower.getWINSPrimaryAddress().equals(winsPrim_new)) results[5][7] = false;
        if (!nsPower.getWINSScopeID().equals(winsScope_new)) results[5][8] = false;
        if (!nsPower.getWINSSecondaryAddress().equals(winsSec_new)) results[5][9] = false;
        if (nsPower.getBrowsingInterval() != browseInt_new) results[5][10] = false;
        if (nsPower.getCCSID() != ccsid_new) results[5][11] = false;
        if (nsPower.getIdleTimeout() != idleTimeout_new) results[5][12] = false;
        if (nsPower.isLogonServer() != logonServer_new) results[5][13] = false;
        if (nsPower.isWINSServer() != winsServer_new) results[5][14] = false;
        if (getSystemVRM() >= 0x00050400)
        { // new attribute added in V5R4
          if (nsPower.getMessageAuthentication() != messageAuthentication_new) results[5][15] = false;
          if (nsPower.getMinimumMessageSeverity() != minimumMessageSeverity_new) results[5][16] = false;
          if (nsPower.getLANManagerAuthentication() != lanManagerAuthentication_new) results[5][17] = false;
        }

        ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception exc) {};

        // Restore the original values.
        nsPower.setAllowSystemName(allowSysName_orig);
        nsPower.setAuthenticationMethod(authMethod_orig);
        nsPower.setAutoStart(autostart_orig);
        nsPower.setDescription(desc_orig);
        nsPower.setDomainName(domain_orig);
        nsPower.setGuestUserProfile(guestUser_orig);
        nsPower.setName(serverName_orig);
        nsPower.setWINSPrimaryAddress(winsPrim_orig);
        nsPower.setWINSScopeID(winsScope_orig);
        nsPower.setWINSSecondaryAddress(winsSec_orig);
        nsPower.setBrowsingInterval(browseInt_orig);
        nsPower.setCCSID(ccsid_orig);
        nsPower.setIdleTimeout(idleTimeout_orig);
        nsPower.setLogonServer(logonServer_orig);
        nsPower.setWINSServer(winsServer_orig);
        nsPower.setMessageAuthentication(messageAuthentication_orig);
        nsPower.setMinimumMessageSeverity(minimumMessageSeverity_orig);
        nsPower.setLANManagerAuthentication(lanManagerAuthentication_orig);

        nsPower.commitChanges();
        stopAndStart(true);
        ns.refresh();

        // BLOCK 6:
        // Verify that the getters now report the original values.
        System.out.println("DEBUG: BLOCK 6");
        if (ns.isAllowSystemName() != allowSysName_orig) results[6][0] = false;
        if (ns.getAuthenticationMethod() != authMethod_orig) results[6][1] = false;
        ///if (ns.isAutoStart() != autostart_orig) {} results[6][2] = false;  // If no *IOSYSCFG, isAutoStart() throws exception.
        if (!ns.getDescription().equals(desc_orig)) results[6][3] = false;
        if (!ns.getDomainName().equals(domain_orig)) results[6][4] = false;
        if (!ns.getGuestUserProfile().equals(guestUser_orig)) results[6][5] = false;
        if (!ns.getName().equals(serverName_orig)) results[6][6] = false;
        if (!ns.getWINSPrimaryAddress().equals(winsPrim_orig)) results[6][7] = false;
        if (!ns.getWINSScopeID().equals(winsScope_orig)) results[6][8] = false;
        if (!ns.getWINSSecondaryAddress().equals(winsSec_orig)) results[6][9] = false;
        if (ns.getBrowsingInterval() != browseInt_orig) results[6][10] = false;
        if (ns.getCCSID() != ccsid_orig) results[6][11] = false;
        if (ns.getIdleTimeout() != idleTimeout_orig) results[6][12] = false;
        if (ns.isLogonServer() != logonServer_orig) results[6][13] = false;
        if (ns.isWINSServer() != winsServer_orig) results[6][14] = false;
        if (ns.getMessageAuthentication() != messageAuthentication_orig) results[6][15] = false;
        if (ns.getMinimumMessageSeverity() != minimumMessageSeverity_orig) results[6][16] = false;
        if (ns.getLANManagerAuthentication() != lanManagerAuthentication_orig) results[6][17] = false;

        if (!cmd.run("DLTUSRPRF USRPRF(NETSRVTEST)"))
          System.out.println("DLTUSRPRF USRPRF(NETSRVTEST) - failed.  Manually delete profile on AS/400");

        boolean ok=true;
        for (int i=0; i<NUM_BLOCKS; i++) {
          for (int j=0; j<NUM_ATTRS; j++) {
            if (!results[i][j]) ok = false;
          }
        }
        if (ok) {
          if (validateAttributeValues(ns) &&
              validateAttributeValues(nsPower))
            succeeded();
          else failed();
        }
        else {
          for (int i=0; i<NUM_BLOCKS; i++) {
            System.out.print("BLOCK " + i + ":");
            for (int j=0; j<NUM_ATTRS; j++) {
              System.out.print(" " + results[i][j]);
            }
            System.out.println();
          }
          failed();
        }
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
      finally
      { // Ensure that all attribute are restored to original values.
        if (committedChanges)
        {
          nsPower.setAllowSystemName(allowSysName_orig);
          nsPower.setAuthenticationMethod(authMethod_orig);
          nsPower.setAutoStart(autostart_orig);
          nsPower.setDescription(desc_orig);
          nsPower.setDomainName(domain_orig);
          nsPower.setGuestUserProfile(guestUser_orig);
          nsPower.setName(serverName_orig);
          nsPower.setWINSPrimaryAddress(winsPrim_orig);
          nsPower.setWINSScopeID(winsScope_orig);
          nsPower.setWINSSecondaryAddress(winsSec_orig);
          nsPower.setBrowsingInterval(browseInt_orig);
          nsPower.setCCSID(ccsid_orig);
          nsPower.setIdleTimeout(idleTimeout_orig);
          nsPower.setLogonServer(logonServer_orig);
          nsPower.setWINSServer(winsServer_orig);
          nsPower.setMessageAuthentication(messageAuthentication_orig);
          nsPower.setMinimumMessageSeverity(minimumMessageSeverity_orig);
          nsPower.setLANManagerAuthentication(lanManagerAuthentication_orig);

          try { nsPower.commitChanges(); } catch (Exception e) { e.printStackTrace(); }
        }
      }

    }


}
