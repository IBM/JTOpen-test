///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400ConnectionPoolTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400ConnectionPool;
import com.ibm.as400.access.AS400Message;  //@A3A
import com.ibm.as400.access.ConnectionPoolEvent;
import com.ibm.as400.access.ConnectionPoolException;
import com.ibm.as400.access.ConnectionPoolListener;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.Log;
import com.ibm.as400.access.EventLog;
import com.ibm.as400.access.SecureAS400;
import com.ibm.as400.access.SocketProperties;
import com.ibm.as400.security.auth.DefaultProfileTokenProvider;
import com.ibm.as400.security.auth.ProfileTokenCredential;

import test.JDReflectionUtil;
import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

import java.util.Enumeration;              //@A8A
import java.util.Locale;                   //@B1A
import java.util.Vector;

/**
Testcase AS400ConnectionPoolTestcase.

<p>This tests the following AS400ConnectionPool methods:
<ul>
<li>ctor
<li>close()
<li>fill(String, String, String, int, int)
<li>getActiveConnectionCount(String, String)
<li>getAvailableConnectionCount(String, String)
<li>getConnection(String, String, String, int)
<li>getConnection(String, String, String)
<li>getConnection(String, String, int)
<li>getConnection(String, String)
<li>getSecureConnection(String, String, String, int)
<li>getSecureConnection(String, String, String)
<li>getSecureConnection(String, String, int)
<li>getSecureConnection(String, String)
<li>returnConnectionToPool(AS400)
<li>setLog(Log)
</ul>

It also tests ConnectionPoolEventSupport.
**/
public class AS400ConnectionPoolTestcase
extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "AS400ConnectionPoolTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.AS400ConnectionPoolTest.main(newArgs); 
   }

    // Private data.
    static final int variations_ = 148;  //variation count
    private static final boolean DEBUG = false;

    private String operatingSystem_;
    private boolean DOS_;
    private boolean runningNatively_;  // running on i5/OS

    // UserID/password that is target of profile tokens.
    private String swapToUserID_;
    private char[] encryptedSwapToPassword_;

    private CommandCall DLTLIB_FRED;

    /**
     @exception  Exception  If an exception occurs.
     **/
    public void setup()
    throws Exception
    {
        if (pwrSys_ == null || pwrSys_.getSystemName().length() == 0 || pwrSys_.getUserId().length() == 0)
            throw new IllegalStateException("ERROR: Please specify a SECADM uid/pwd via -pwrSys.");

        // Determine operating system we're running under
        operatingSystem_ = System.getProperty("os.name");
        if (JTOpenTestEnvironment.isWindows)
        {
            DOS_ = true;
        }
        else
        {
            DOS_ = false;
        }

        if (operatingSystem_.indexOf("OS/400") >= 0)
        {
          runningNatively_ = true;
        }
        else
        {
          runningNatively_ = false;
        }

        // If running natively, we must swap to a profile different than *CURRENT.
        // This is a limitation imposed on us by the Signon Host Server.

        if (runningNatively_) // swap to a profile other than *CURRENT
        {
          String currentUser = System.getProperty("user.name");
          if (DEBUG) {
            System.out.println("user.name     == |" + currentUser + "|");
            System.out.println("userId_       == |" + userId_ + "|");
          }
          if (currentUser == null || currentUser.length() == 0) {
            currentUser = systemObject_.getUserId();
          }
          swapToUserID_   = (currentUser.equalsIgnoreCase(userId_) ? pwrSysUserID_ : userId_);
           
           
                      
          if (currentUser.equalsIgnoreCase(userId_)) {
            encryptedSwapToPassword_=  pwrSysEncryptedPassword_;
            
          } else {
            encryptedSwapToPassword_=   encryptedPassword_;
          }
        
        }
        else  // running remotely, so just swap to the profile specified on -uid
        {
          swapToUserID_   = userId_;
          
          encryptedSwapToPassword_= encryptedPassword_;
        }
        if (DEBUG)
        {
          System.out.println("swapToUserID_ == |" + swapToUserID_ + "|");

          output_.println("Running under: " + operatingSystem_);
          output_.println("DOS-based file structure: " + DOS_);
       }

        //Get password in cache for later tests.
        //@B2D systemObject_.addPasswordCacheEntry(systemObject_.getSystemName(),          //@A6A
        //@B2D                                    systemObject_.getUserId(), passwordChars);  //@A6A
	CommandCall cc = new CommandCall(pwrSys_);
	cc.run("QSYS/CHGJOB INQMSGRPY(*SYSRPYL)");

        DLTLIB_FRED = new CommandCall(pwrSys_, "QSYS/DLTLIB FRED");
        try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
    }


    public void failed(Exception e, String info) {
	if (e instanceof ConnectionPoolException) {
	    ConnectionPoolException cpe = (ConnectionPoolException) e;
	    Exception nextException = cpe.getException();
	    System.out.println("ConnectionPoolException caught.  Next exception is "); 
	    nextException.printStackTrace(); 
	} 
	super.failed(e, info); 
    } 

    /**
    ctor() - Default ctor should not throw an exception.
    **/
    public void Var001()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            succeeded();
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    ctor() - Default ctor should initialize default properties the same each time.
    **/
    public void Var002()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            AS400ConnectionPool p2 = new AS400ConnectionPool();
            assertCondition(p.getCleanupInterval() == p2.getCleanupInterval() 
                            && p.getMaxConnections() == p2.getMaxConnections()
                            && p.getMaxInactivity() == p2.getMaxInactivity()
                            && p.getMaxLifetime() == p2.getMaxLifetime()
                            && p.getMaxUseCount() == p2.getMaxUseCount()
                            && p.getMaxUseTime() == p2.getMaxUseTime()
                            && p.isRunMaintenance() == p2.isRunMaintenance()
                            && p.isThreadUsed() == p2.isThreadUsed());
            p.close();
            p2.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    ctor() - Default ctor should not create any connections.
    **/
    public void Var003()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            assertCondition(p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 &&
                            p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    close() - Should complete successfully with an empty pool.
    **/
    public void Var004()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.close();
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    close() - Should complete successfully with connections filled in pool.
    **/
    public void Var005()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            int service = AS400.PRINT;
            int numOfConnections = 3;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service,
                   numOfConnections);
            PasswordVault.clearPassword(passwordChars);
            p.close();
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    close() - Should complete successfully with a connection in the pool.
    **/
    public void Var006()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@A5C
             //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
             PasswordVault.clearPassword(passwordChars);
           p.close();
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0, "check = "+check);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
    close() - Should be able to reopen pool after it has been closed.
    **/
    public void Var007()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
          
            p.close();
            int service = AS400.COMMAND;
            AS400 c = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);
            assertCondition(c instanceof AS400 && c.isConnected(service)
                            && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
            p.close();
	    PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    close() - Should be able to close pool after it has been reopened.
    **/
    public void Var008()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.close();
            int service = AS400.COMMAND;
            AS400 c = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);
            p.close();
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0, "Connection="+c);
         PasswordVault.clearPassword(passwordChars);
}
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    close() - Should be able to close an already closed pool.
    **/
    public void Var009()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.PRINT;
            int numOfConnections = 3;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.close();
            p.close();
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
         PasswordVault.clearPassword(passwordChars);
}
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    close() - Should be able to close pool while maintenance thread running.
    **/
    public void Var010()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setCleanupInterval(100);
            p.setRunMaintenance(true);
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            Thread.sleep(200);
            p.close();
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0, "AS400="+o);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    close() - Should be able to close pool while maintenance thread running cleanup.
    **/
    public void Var011()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setCleanupInterval(100);
            p.setRunMaintenance(true);
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            p.setMaxConnections(2);
            Thread.sleep(500);
            p.close();
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    fill(String, String, String, int, int) - Should complete successfully.
    **/
    public void Var012()
    {  
        try
        { 
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.PRINT;
            int numOfConnections = 1;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, 
                   numOfConnections);      
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == numOfConnections); 
             PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");      
        }
    }


    /**
    fill(String, String, String, int, int) - Should complete successfully.
    **/
    public void Var013()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.FILE;
            int numOfConnections = 1;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, 
                   service, numOfConnections);
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                          systemObject_.getUserId(), passwordChars, service);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                          systemObject_.getUserId(), passwordChars, service);
            assertCondition(conn1.isConnected(service) && conn2.isConnected(service) && 
                            p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    fill(String, String, String, int, int) - Should complete successfully.
    **/
    public void Var014()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.FILE;
            int numOfConnections = 3;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                          systemObject_.getUserId(), passwordChars, service);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                          systemObject_.getUserId(), passwordChars, service);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, 
                   service, numOfConnections);
             PasswordVault.clearPassword(passwordChars);

            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == numOfConnections
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 2, 
                            "connections are "+conn1+"  " +conn2);
            conn1.close();
            conn2.close(); 
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    fill(String, String, String, int, int) - Should complete successfully (fill will use the already 
    created connection which is not in use-- it just makes sure that there are the correct number of 
    connections to that service).
    **/
    public void Var015()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.FILE;
            int numOfConnections = 3;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);
            p.returnConnectionToPool(o);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, 
                   service, numOfConnections);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == numOfConnections++
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
            
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    fill(String, String, String, int, int) - Should complete successfully.
    **/
    public void Var016()
    {
        try
        {
            int defaultCcsid = systemObject_.getCcsid();
            int newCcsid = (defaultCcsid == 37 ? 1200 : 37);
            if (DEBUG) System.out.println("defaultCcsid: "+defaultCcsid+"; newCcsid: "+newCcsid);
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setCCSID(newCcsid);
            int service = AS400.FILE;
            int numOfConnections = 3;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                          systemObject_.getUserId(), passwordChars, service);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                          systemObject_.getUserId(), passwordChars, service);
            int conn1_ccsid = conn1.getCcsid();
            int conn2_ccsid = conn2.getCcsid();
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, 
                   service, numOfConnections);
            p.returnConnectionToPool(conn1);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 4 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1 &&
                            conn1_ccsid == newCcsid && conn2_ccsid == newCcsid);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    fill(String, String, String, int, int) - Should throw a connection pool exception if
    nonexistant userID.
    **/
    public void Var017()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            String uid = "me";
            char[]  passwordChars = "happy".toCharArray();
            int service = AS400.FILE;
            int numOfConnections = 1;
            p.fill(systemObject_.getSystemName(), uid, passwordChars, service, numOfConnections);      
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");
            p.close();
        }
    }


    /**
    fill(String, String, String, int, int) - Should throw an exception if nonexistant 
    system name.
    **/
    public void Var018()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
           char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        try
        {
            String systemName = "badSystemName";
            int service = AS400.COMMAND;
            int numOfConnections = 1;
             p.fill(systemName, systemObject_.getUserId(), passwordChars, service, numOfConnections);      
            failed("Did not throw exception.");
          }
        catch (Exception e)
        {
            succeeded();  //@A1C
            p.close();
        }
       PasswordVault.clearPassword(passwordChars);

    }


    /**
    fill(String, String, String, int, int) - Should throw an extended illegal argument exception if 
    numOfConnections < 1.
    **/
    public void Var019()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
	char[] passwordChars =  null; 
             passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
            int service = AS400.COMMAND;
            int numOfConnections = 0;
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, 
                   numOfConnections);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
            p.close();
        }
           PasswordVault.clearPassword(passwordChars);
  }


    /**
    fill(String, String, String, int, int) - Should throw an exception if requested # of 
    connections > maxConnections.
    **/
    public void Var020()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
           char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
            int service = AS400.COMMAND;
            int numOfConnections = 2;
            p.setMaxConnections(1);       
              p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, 
                   numOfConnections);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");    //@A2C
            p.close();
        }
	    PasswordVault.clearPassword(passwordChars);

    }


    /**
    fill(String, String, String, int, int) - Should throw an exception if requested # of 
    connections + currently allocated > maxConnections.
    **/
    public void Var021()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
	char[] passwordChars = null; 
           passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
            p.setMaxConnections(2);       
            int service = AS400.COMMAND;
            int numOfConnections = 2;
              AS400 newConn1 = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                             passwordChars, service);    
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, 
                   numOfConnections);
            failed("Did not throw exception."+newConn1);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");    //@A2C
            p.close();
        }
	    PasswordVault.clearPassword(passwordChars);

    }


    /**
    getActiveConnectionCount(String, String) - Should complete successfully with no connection in pool.
    **/
    public void Var022()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            assertCondition(p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getActiveConnectionCount(String, String) - Should complete successfully with one connection in pool.
    **/
    public void Var023()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            assertCondition(p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getActiveConnectionCount(String, String) - Should complete successfully with some filled connections in pool.
    **/
    public void Var024()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
            int numOfConnections = 5;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
            assertCondition(p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
	    PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getActiveConnectionCount(String, String) - Should complete successfully with some filled connections in pool.
    **/
    public void Var025()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
            int numOfConnections = 5;
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
            assertCondition(p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getActiveConnectionCount(String, String) - Should complete successfully with a returned connection in pool.
    **/
    public void Var026()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);
            p.returnConnectionToPool(o);
            assertCondition(p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
	    PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getActiveConnectionCount(String, String) - Should complete successfully with some filled connections in pool.
    **/
    public void Var027()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
            int numOfConnections = 5;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
            p.returnConnectionToPool(o);
            assertCondition(p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getActiveConnectionCount(String, String) - Should throw exception with null system.
    **/
    public void Var028()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            p.getActiveConnectionCount((String)null, systemObject_.getUserId());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
    }


    /**
    getActiveConnectionCount(String, String) - Should throw exception with null userId.
    **/
    public void Var029()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            p.getActiveConnectionCount(systemObject_.getSystemName(), (String)null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
    } 


    /**
    getActiveConnectionCount(String, String) - Should return 0 if a connection list does not exist
    for that system.
    **/
    public void Var030()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            assertCondition(p.getActiveConnectionCount("jweibn", systemObject_.getUserId()) == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
    getActiveConnectionCount(String, String) - Should return 0 if a connection list does not
    exist for that userId.
    **/
    public void Var031()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            assertCondition(p.getActiveConnectionCount(systemObject_.getSystemName(), "happy") == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    } 


    /**
    getAvailableConnectionCount(String, String) - Should return 0 if a connection list does not exist
    for that system.
    **/
    public void Var032()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            assertCondition(p.getAvailableConnectionCount("jweibn", systemObject_.getUserId()) == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
    getAvailableConnectionCount(String, String) - Should return 0 if a connection list does not
    exist for that userId.
    **/
    public void Var033()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), "happy") == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
    getAvailableConnectionCount(String, String) - Should complete successfully with no connections in pool.
    **/
    public void Var034()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
    getAvailableConnectionCount(String, String) - Should complete successfully with one connection in pool.
    **/
    public void Var035()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
    getAvailableConnectionCount(String, String) - Should complete successfully with some filled connections in pool.
    **/
    public void Var036()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
            int numOfConnections = 5;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == numOfConnections);
	    PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getAvailableConnectionCount(String, String) - Should complete successfully with some filled connection in pool.
    **/
    public void Var037()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
            int numOfConnections = 5;
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
            p.returnConnectionToPool(o);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 6);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getAvailableConnectionCount(String, String) - Should throw exception with null system.
    **/
    public void Var038()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            p.getAvailableConnectionCount((String)null, systemObject_.getUserId());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
    }


    /**
    getAvailableConnectionCount(String, String) - Should throw exception with null userId.
    **/
    public void Var039()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            p.getAvailableConnectionCount(systemObject_.getSystemName(), (String)null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
    }


    /**
    getConnection(String, String) - Should complete successfully.
    **/
    @SuppressWarnings("deprecation")
    public void Var040()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId());
            //@B2D assertCondition(o instanceof AS400 && !(o instanceof SecureAS400) && !(o.isConnected())
            //@B2D	   && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
            //@B2D	   && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
            //@B2Dp.close();
            assertCondition(false, "Did not throw exception but got "+o); 
        }
        catch (Exception e)
        {
            //@B2Dfailed(e, "Unexpected Exception");
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");  //@B2C
        }
    }  


    /**
    getConnection(String, String) - Check that functioning connection was returned.
    **/
    public void Var041()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            CommandCall cmd = new CommandCall(o);
            cmd.run("QSYS/CRTLIB FRED");
            succeeded();
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "UnexpectedException");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }  


    /**
    getConnection(String, String, int) - Should complete successfully.
    **/
    @SuppressWarnings("deprecation")
    public void Var042()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                      service);
            //@B2D assertCondition(o instanceof AS400 && !(o instanceof SecureAS400) && o.isConnected(service)
            //@B2D	   && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
            //@B2D	   && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
            //@B2D p.close();
            assertCondition(false, "Did not throw exception but got "+o); 
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");  //@B2C
        }
    }


    /**
    getConnection(String, String, String) - Should complete successfully.
    **/
    public void Var043()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                      passwordChars);
            assertCondition(o instanceof AS400 && !(o instanceof SecureAS400) && !(o.isConnected())
                            && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getConnection(String, String, String, int) - Should complete successfully.
    **/
    public void Var044()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                      passwordChars, service);
	    PasswordVault.clearPassword(passwordChars);
            assertCondition(o instanceof AS400 && !(o instanceof SecureAS400) && o.isConnected(service)
                            && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);      
           PasswordVault.clearPassword(passwordChars);
          p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getConnection(String, String, String, int) - Should not create a new connection if an appropriate
    one already exists.
    **/
    public void Var045()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            JDReflectionUtil.callMethod_V(p, "setPretestConnections", true); 
            /* p.setPretestConnections(true); */ 
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                      passwordChars);
            if (
                JDReflectionUtil.callMethod_B(p, "isPretestConnections")
                /* p.isPretestConnections() */ 
            ) o.connectService(AS400.SIGNON); // We need at least 1 service connected, in order to pass the "pretest" on the next getConnection().
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                            passwordChars);
            int availableConnectionCount = p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) ;
            int activeConnectionCount = p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()); 
            assertCondition(availableConnectionCount == 0 
                            && activeConnectionCount == 1, "activeConnectionCount="+activeConnectionCount+" sb 1 "
                                + " availableConnectionCount="+availableConnectionCount+" sb 0");      
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    getConnection(String, String, String, int) - Should not create a new connection if an appropriate
    one already exists.
    **/
    public void Var046()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                      passwordChars, service);
            if (
                JDReflectionUtil.callMethod_B(p, "isPretestConnections")
                /* p.isPretestConnections() */ 
                ) o.connectService(service); // We need at least 1 service connected, in order to pass the "pretest" on the next getConnection().
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                            passwordChars, service);
            int availableConnectionCount = p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) ;
            int activeConnectionCount = p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()); 
            assertCondition(availableConnectionCount == 0 
                            &&  activeConnectionCount == 1, "activeConnectionCount="+activeConnectionCount+" sb 1 "
                                + " availableConnectionCount="+availableConnectionCount+" sb 0");      
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getConnection(String, String, String, int) - Should not create a new connection if an appropriate
    one already exists.
    **/
    public void Var047()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                      passwordChars);
            if (
                JDReflectionUtil.callMethod_B(p, "isPretestConnections")
                /* p.isPretestConnections() */ 
                ) o.connectService(AS400.SIGNON); // We need at least 1 service connected, in order to pass the "pretest" on the next getConnection().
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                            passwordChars, service);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);      
	    PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getConnection(String, String, String, int) - Should not create a new connection if an appropriate
    one already exists.
    **/
    public void Var048()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                      passwordChars, service);
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                            passwordChars);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);      
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    getConnection(String, String) - Should throw an exception if system name is null.
    **/
    public void Var049()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            /* p.getConnection((String)null, systemObject_.getUserId()); */ 
            JDReflectionUtil.callMethod_OSS(p, "getConnection", null, systemObject_.getUserId());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");  //@B2C
            p.close();
        }
    }


    /**
    getConnection(String, String, int) - Should throw an exception if system name is null.
    **/
    @SuppressWarnings("deprecation")
    public void Var050()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            int service = AS400.COMMAND;
            p.getConnection((String)null, systemObject_.getUserId(), service);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");  //@B2C
            p.close();
        }
    }


    /**
    getConnection(String, String, String) - Should throw an exception if system name is null.
    **/
    public void Var051()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        try
        {
          p.getConnection((String)null, systemObject_.getUserId(), passwordChars);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
	    PasswordVault.clearPassword(passwordChars);

    }


    /**
    getConnection(String, String, String, int) - Should throw an exception if system name is null.
    **/
    public void Var052()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
char[] passwordChars = null; 
            passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
            int service = AS400.COMMAND;
             p.getConnection((String)null, systemObject_.getUserId(), passwordChars, service);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
       PasswordVault.clearPassword(passwordChars);

    }


    /**
    getConnection(String, String) - Should throw an exception if userId is null.
    **/
    public void Var053()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            JDReflectionUtil.callMethod_OSS(p, "getConnection", systemObject_.getSystemName(), (String)null);
            /* p.getConnection(systemObject_.getSystemName(), (String)null); */ 
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");  //@B2C
            p.close();
        }
    }


    /**
    getConnection(String, String, int) - Should throw an exception if userId is null.
    **/
    @SuppressWarnings("deprecation")
    public void Var054()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            int service = AS400.COMMAND;
            p.getConnection(systemObject_.getSystemName(), (String)null, service);
           failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");  //@B2C
            p.close();
        }
    }


    /**
    getConnection(String, String, String) - Should throw an exception if userId is null.
    **/
    public void Var055()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
             p.getConnection(systemObject_.getSystemName(), (String)null, passwordChars);
            failed("Did not throw exception.");
          }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
       PasswordVault.clearPassword(passwordChars);

    }


    /**
    getConnection(String, String, String, int) - Should throw an exception if userId is null.
    **/
    public void Var056()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
	char[] passwordChars =null; 
           passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
      try
        {
            int service = AS400.COMMAND;
               p.getConnection(systemObject_.getSystemName(), (String)null, passwordChars, service);

            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
         assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
        PasswordVault.clearPassword(passwordChars);

    }


    /**
    getConnection(String, String, String) - Should throw an exception if password is null.
    **/
    public void Var057()
    {
        notApplicable("Obsolete testcase");  // no longer throws an exception

//        AS400ConnectionPool p = new AS400ConnectionPool();
//        try
//        {
//            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
//                            (String)null);
//            failed("Did not throw exception.");
//        }
//        catch (Exception e)
//        {
//            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
//            p.close();
//        }
    }


    /**
    getConnection(String, String, String, int) - Should throw an exception if password is null.
    **/
    public void Var058()
    { 
        notApplicable("Obsolete testcase");  // no longer throws an exception

//        AS400ConnectionPool p = new AS400ConnectionPool();
//        try
//        {
//            int service = AS400.COMMAND;
//            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
//                            (String)null, service);
//            failed("Did not throw exception.");
//        }
//        catch (Exception e)
//        {
//            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
//            p.close();
//        }
    }


    /**
    getConnection(String, String, int) - Should throw an exception if service number < 0.
    **/
    @SuppressWarnings("deprecation")
    public void Var059()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            int service = -1;
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), service);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");  //@B2C
            p.close();
        }
    }


    /**
    getConnection(String, String, String, int) - Should throw an exception if service number < 0.
    **/
    public void Var060()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
            int service = -1;
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, 
                            service);
            failed("Did not throw exception.");
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
            p.close();
        }
        PasswordVault.clearPassword(passwordChars);

    }


    /**
    getConnection(String, String, int) - Should throw an exception if service number > 7.
    **/
    @SuppressWarnings("deprecation")
    public void Var061()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            int service = 8;
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), service);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");  //@B2C
            p.close();
        }
    }


    /**
    getConnection(String, String, String, int) - Should throw an exception if service number > 7.
    **/
    public void Var062()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
            int service = 8;
               p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, 
                            service);
            failed("Did not throw exception.");
         }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
            p.close();
        }
       PasswordVault.clearPassword(passwordChars);

    }


    /**
    getConnection(String, String) - Will fail with incorrect uid.
    **/
    public void Var063()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            JDReflectionUtil.callMethod_OSS(p,"getConnection",systemObject_.getSystemName(), "george" ); 
            /* p.getConnection(systemObject_.getSystemName(), "george"); */ 
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");  
            p.close();
        }
    }


    /**
    getConnection(String, String, int) - Will fail with incorrect uid.
    **/
    @SuppressWarnings("deprecation")
    public void Var064()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            int service = AS400.COMMAND;
            p.getConnection(systemObject_.getSystemName(), "george", service);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");
            p.close();
        }
    }


    /**
    getConnection(String, String, String) - Will fail with incorrect uid.
    **/
    public void Var065()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
               p.getConnection(systemObject_.getSystemName(), "george", passwordChars);
            failed("Did not throw exception.");
          }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");
            p.close();
        }
	    PasswordVault.clearPassword(passwordChars);

    }


    /**
    getConnection(String, String, String, int) - Will fail with incorrect uid.
    **/
    public void Var066()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
	char[] passwordChars = null; 
               passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
            int service = AS400.COMMAND;
          p.getConnection(systemObject_.getSystemName(), "george", passwordChars, service);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {

            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");
            p.close();
        }
         PasswordVault.clearPassword(passwordChars);

    }


    /**
    getConnection(String, String) - Should fail because password has not been cached.
    **/
    public void Var067()
    {
        //@B2D // if on the 400, do not run
        //@B2D if (runningNatively_)     //@A4A
        //@B2D {                                                    //@A4A
        //@B2D     notApplicable("Does not run natively");          //@A4A
        //@B2D     return;                                          //@A4A
        //@B2D }                            //@A4A

        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            //@B2D systemObject_.removePasswordCacheEntry(systemObject_.getSystemName(), 
            //@B2D                                       systemObject_.getUserId());
            /* p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId()); */ 
            JDReflectionUtil.callMethod_OSS(p, "getConnection",systemObject_.getSystemName(), systemObject_.getUserId());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");
        }
        finally
        {
            p.close();
            //@B2D try
            //@B2D {
            //@B2D     systemObject_.addPasswordCacheEntry(systemObject_.getSystemName(), 
            //@B2D                                         systemObject_.getUserId(), passwordChars);
            //@B2D }
            //@B2D catch (Exception f)
            //@B2D {
            //@B2D     failed(f, "Unexpected Exception during testcase cleanup");
            //@B2D }
        }
    }  


    /**
    getConnection(String, String, int) - Should fail because password has not been cached.
    **/
    @SuppressWarnings("deprecation")
    public void Var068()
    {
        //@B2D // if on the 400, do not run
        //@B2D if (runningNatively_)     //@A4A
        //@B2D {                                                    //@A4A
        //@B2D     notApplicable("Does not run natively");          //@A4A
        //@B2D     return;                                          //@A4A
        //@B2D }                            //@A4A

        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
        //@B2D     systemObject_.removePasswordCacheEntry(systemObject_.getSystemName(), 
        //@B2D                                            systemObject_.getUserId());
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 1);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");
        }
        finally
        {
            p.close();
            //@B2D try
            //@B2D {
            //@B2D     systemObject_.addPasswordCacheEntry(systemObject_.getSystemName(), 
            //@B2D                                         systemObject_.getUserId(), passwordChars);
            //@B2D }
            //@B2D catch (Exception f)
            //@B2D {
            //@B2D     failed(f, "Unexpected Exception during testcase cleanup");
            //@B2D }
        }
    }  


    /**
    getConnection(String, String) - Should fail after max is reached.
    **/
    public void Var069()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        try
        {
            p.setMaxConnections(1);
              p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");     //@A2C
            p.close();
        }
         PasswordVault.clearPassword(passwordChars);

    }  


    /**
    getSecureConnection(String, String) - Should successfully create a SecureAS400 instance.
    **/
    public void Var070()
    {
	notApplicable("SSLight Testcase");
    }


    /**
    getSecureConnection(String, String, int) - Should successfully create a SecureAS400 instance.
    **/
    public void Var071()
    {
	notApplicable("SSLight Testcase");
    }


    /**
    getSecureConnection(String, String, String) - Should successfully create a SecureAS400 instance.
    **/
    public void Var072()
    {
	notApplicable("SSLight Testcase");
    }


    /**
    getSecureConnection(String, String, String, int) - Should successfully create a SecureAS400 instance.
    **/
    public void Var073()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String) - Should fail if system name is null.
    **/
    public void Var074()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, int) - Should fail if system name is null.
    **/
    public void Var075()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, String) - Should fail if system name is null.
    **/
    public void Var076()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, String, int) - Should fail if system name is null.
    **/
    public void Var077()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String) - Should throw an exception if userId is null.
    **/
    public void Var078()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, int) - Should throw an exception if userId is null.
    **/
    public void Var079()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, String) - Should throw an exception if userId is null.
    **/
    public void Var080()
    {
	notApplicable("SSLight Testcase");
    }


    /**
    getSecureConnection(String, String, String, int) - Should throw an exception if userId is null.
    **/
    public void Var081()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, String) - Should throw an exception if password is null.
    Note:  For this to work, the GUI must be disabled:   
    **/
    public void Var082()
    {
	notApplicable("SSLight Testcase");


    }


    /**
    getSecureConnection(String, String, String, int) - Should throw an exception if password is null.
    **/
    public void Var083()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, int) - Should throw an exception if service < 0.
    **/
    public void Var084()
    { 
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, String, int) - Should throw an exception if service < 0.
    **/
    public void Var085()
    { 
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, int) - Should throw an exception if service > 7.
    **/
    public void Var086()
    { 
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, String, int) - Should throw an exception if service > 7.
    **/
    public void Var087()
    { 
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String) - Will fail with incorrect uid.
    **/
    public void Var088()
    { 
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, int) - Will fail with incorrect uid.
    **/
    public void Var089()
    { 
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, String) - Will fail with incorrect uid.
    **/
    public void Var090()
    { 
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String, String, int) - Will fail with incorrect uid.
    **/
    public void Var091()
    { 
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String, String) - Should fail because password has not been cached.
    **/
    public void Var092()
    {
	notApplicable("SSLight Testcase");

    }  



    /**
    getSecureConnection(String, String) - Should fail because password has not been cached.
    **/
    public void Var093()
    {
	notApplicable("SSLight Testcase");

    }  


    /**
    getSecureConnection(String, String) - Should fail after max is reached.
    **/
    public void Var094()
    {
	notApplicable("SSLight Testcase");

    }  


    /**
    returnConnectionToPool(AS400) - Passing a connection in the pool should successfully
    return it to the list and mark it as inactive. 
    **/
    public void Var095()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           AS400 newItem = p.getConnection(systemObject_.getSystemName(), 
                                            systemObject_.getUserId(), passwordChars);
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(newItem, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            p.returnConnectionToPool(newItem);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0, "check="+check);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
    returnConnectionToPool(AS400) - Passing item that is in the pool should successfully
    return it to the list and mark it as inactive even if there are other connections in the list. 
    **/
    public void Var096()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool(); 
            int service = AS400.COMMAND;
            int numOfConnections = 4;
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service,
                   numOfConnections);
            AS400 system = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(system, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            p.returnConnectionToPool(system);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == numOfConnections 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1, "check="+check);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
    returnConnectionToPool(AS400) - Passing a default system that isn't in the pool should be 
    not add it to the list. (There will be a warning trace message.) 
    **/
    public void Var097()
    {
      AS400 system = null; 

        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            system = new AS400();
            p.returnConnectionToPool(system);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally { 
          if (system != null) system.close(); 
        }
    }


    /**
    returnConnectionToPool(AS400) - Passing item that isn't in the pool should not add it to 
    the list.  (There will be a warning trace message.) 
    **/
    public void Var098()
    {
      AS400 system = null; 

        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(system, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            p.returnConnectionToPool(system);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1, "check="+check);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
          if (system != null) system.close(); 
        }
    }


    /**
    returnConnectionToPool(AS400) - Returning a connection that has been changed to a different
    system should remove the connection from its old list and not add it to a new list.  
    (There will be a warning trace message.)  
    **/
    public void Var099()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, AS400.COMMAND, 5);  //@A5A
            AS400 newItem = p.getConnection(systemObject_.getSystemName(), 
                                            systemObject_.getUserId(), passwordChars);
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(newItem, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            newItem.resetAllServices();
            newItem.setSystemName("diffSys");
            p.returnConnectionToPool(newItem);
	    PasswordVault.clearPassword(passwordChars);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 4 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0, "check="+check);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
    returnConnectionToPool(AS400) - Returning a connection that has already been returned should
    succeed.
    **/
    public void Var100()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                          systemObject_.getUserId(), passwordChars);
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(conn1, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            p.returnConnectionToPool(conn1);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                          systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(conn1); // return it again
            cmd = new CommandCall(conn2, "QSYS/CHGJOB INQMSGRPY(*SYSRPYL)");
	    cmd.run(); 
            cmd = new CommandCall(conn2, "QSYS/DLTLIB FRED");
            check = cmd.run();
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0, "check="+check);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
    returnConnectionToPool(AS400) - Reusing a connection that has been returned should not
    create more connections.
    **/
    public void Var101()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 newItem = p.getConnection(systemObject_.getSystemName(), 
                                            systemObject_.getUserId(), passwordChars);
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(newItem, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            p.returnConnectionToPool(newItem);
            AS400 newItem2 = p.getConnection(systemObject_.getSystemName(), 
                                             systemObject_.getUserId(), passwordChars);
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd2 = new CommandCall(newItem2, "QSYS/CRTLIB FRED");  //@A5A
            boolean check2 = cmd2.run();  //@A5A
            p.returnConnectionToPool(newItem2);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0, "check="+check+check2);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
    returnConnectionToPool(AS400) - Passing null for system should throw null pointer exception. 
    **/
    public void Var102()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            p.returnConnectionToPool((AS400)null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
    }


    /**
    setLog() - Should set log to null.
    **/
    public void Var103()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            p.setLog((Log)null);    //means to not log events
            succeeded();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
            p.close();
        }
    }


    /**
    setLog() - Should set log to an instance of EventLog (standard out).
    **/
    public void Var104()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
            EventLog n = new EventLog();  //standard out since no parameters.
            p.setLog(n);
            succeeded();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally
        {
            p.setLog((Log)null);
        }
    }


    /**
    CONNECTION_CLOSED() - Check that close event is fired when pool is closed.
    **/
    public void Var105()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();   
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.close();
            assertCondition(testListener.getClosed() == 1); 
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED() - Check that created event is fired when connection is created.
    **/
    public void Var106()
    {
        try
        {  
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars);
            assertCondition(testListener.getCreated() == 1); 
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED() - Check that created event is fired when connection is created after a fill()
    has completed.
    **/
    public void Var107()
    {
        try
        {  
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int numOfConnections = 1;
            int service = AS400.PRINT;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service,
                   numOfConnections);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            assertCondition(testListener.getCreated() == 1); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED - Check that created event is fired when connection is created after a fill()
    has completed for that particular service.
    **/
    public void Var108()
    {
        try
        {  
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int numOfConnections = 1;
            int service = AS400.COMMAND;
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service,
                   numOfConnections);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);
            assertCondition(testListener.getCreated() == 1); 
	    PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED - Check that created event is fired when connection is created after a fill
    has completed for a different service.
    **/
    public void Var109()
    {
        try
        {  
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int numOfConnections = 1;
            int service = AS400.PRINT;
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service,
                   numOfConnections);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, AS400.COMMAND);
            assertCondition(testListener.getCreated() == 1); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED - Check that created event is fired when secure connection is created.
    **/
    public void Var110()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    CONNECTION_CREATED - Check that created event is not fired when connection already exists.
    **/
    public void Var111()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int service = AS400.COMMAND;
            int numOfConnections = 1;
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service); //@B2C
            assertCondition(testListener.getCreated() == 1); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED - Check that created event is not fired when connection already exists.
    **/
    public void Var112()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int service = AS400.COMMAND;
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            if (
                JDReflectionUtil.callMethod_B(p, "isPretestConnections")
                /* p.isPretestConnections() */ 
                ) o.connectService(AS400.SIGNON); // We need at least 1 service connected, in order to pass the "pretest" on the next getConnection().
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);  //@B2C
 	    PasswordVault.clearPassword(passwordChars);
           assertCondition(testListener.getCreated() == 1); 
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_EXPIRED - Check that connection_expired event is fired if a connection exceeded max 
    inactivity.
    **/
    public void Var113()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
            p.setMaxInactivity(100);
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            Thread.sleep(5000);       //@A1C
            assertCondition(testListener.getExpired() == 1); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    Check that connection count goes down if a connection exceeded max inactivity.
    **/
    public void Var114()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setCleanupInterval(100);
            p.setRunMaintenance(true);
            p.setMaxInactivity(100);
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            Thread.sleep(300);
	    PasswordVault.clearPassword(passwordChars);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0); 
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_EXPIRED - Check that connection_expired event is fired if a connection
    exceeds max use count.
    **/
    public void Var115()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
            p.setMaxUseCount(2);      //@A2C It was setMaxCount(1) which meant every time connection was 
            //replaced, it still had been used the max. number of times!
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);   //@A2C
            p.returnConnectionToPool(o);
            Thread.sleep(5000);       //@A2C
            assertCondition(testListener.getExpired() == 1);      //@A2C
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    Check that connection is removed if it exceeds max use count.   //@B2C
    **/
    public void Var116()
    {
        AS400ConnectionPool p = null;
        try
        {
            p = new AS400ConnectionPool();
            p.setCleanupInterval(100);
            p.setRunMaintenance(true);
            p.setMaxUseCount(1);
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            // Check that expired connection isn't reallocated.     @D3A
            AS400 o2 = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o2);
            if (o2 == o) {
              failed("Connection that exceeded maxUseCount, was reallocated.");
            }
            else
            {
              Thread.sleep(200);
              assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0   //@B2C
                              && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            }
	    PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally
        {
          if (p != null) try { p.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
    CONNECTION_EXPIRED - Check that connection_expired event is fired if a connection lives past
    its expected lifetime.
    **/
    public void Var117()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
            p.setMaxLifetime(100);
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          AS400 con = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);   // @D1A - create AS400 object
            p.returnConnectionToPool(con);   //@D1A - return connection to pool, we no longer close active connections when max lifetime hits
            Thread.sleep(5000);    //@A2C
            assertCondition(testListener.getExpired() >= 1);       //@A2C The event should be fired several times.
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    Check that connection is removed if it lives past its expected lifetime.    //@B2C
    **/
    public void Var118()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setCleanupInterval(100);
            p.setRunMaintenance(true);
            p.setMaxLifetime(10);
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          AS400 con = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);   //@D1C 
            p.returnConnectionToPool(con);  //@D1A return connection to pool, we no longer close an active connection when max lifetime occurs
            Thread.sleep(200);
	    PasswordVault.clearPassword(passwordChars);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0   //@B2C
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0); 
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_EXPIRED - Check that connection_expired event is fired if a connection's 
    max usage time was exceeded.
    **/
    public void Var119()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(100);      // milliseconds
            p.setRunMaintenance(true);
            p.setMaxUseTime(10);            // milliseconds
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            Thread.sleep(200);   //@A2C
            assertCondition(testListener.getExpired() == 1);   
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_EXPIRED - Check that connection is removed if the max usage time was exceeded.
    **/
    public void Var120()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(100);
            p.setRunMaintenance(true);
            p.setMaxUseTime(10);
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            Thread.sleep(200);   //@A2C
            assertCondition(testListener.getExpired() == 1);
	    PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_RELEASED - Check that released event is fired once when getConnection() is called.
    **/
    public void Var121()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            assertCondition(testListener.getReleased() == 1); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_RELEASED - Check that released events are not fired when fill() is called.
    **/
    public void Var122()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int service = AS400.COMMAND;
            int numOfConnections = 4;
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
            assertCondition(testListener.getReleased() == 0); 
	    PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_RELEASED - Check that released event is fired when connection is given out
    after a fill().
    **/
    public void Var123()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int service = AS400.COMMAND;
            int numOfConnections = 4;
                  char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
      p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            assertCondition(testListener.getReleased() == 1); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_RELEASED - Check that released event is fired when connection is given out again.
    **/
    public void Var124()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            assertCondition(testListener.getReleased() == 2); 
 	    PasswordVault.clearPassword(passwordChars);
           p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_RETURNED - Check that returned event is fired when a connection is returned
    to the pool.
    **/
    public void Var125()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 a = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
            p.returnConnectionToPool(a);
            assertCondition(testListener.getReturned() == 1); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    MAINTENANCE_THREAD_RUN - Check that maintenance_thread_run event is fired according to 
    setCleanupInterval.
    **/
    public void Var126()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
            int numOfConnections = 2;
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, 
                   numOfConnections);
            Thread.sleep(200);
	    long testListenerMaintenance = testListener.getMaintenance();
	    // Sometimes it takes a while for the listeners to fire  -- allow it at least 5 seconds 
	    long endtime  = System.currentTimeMillis() + 5000;
	    while (testListenerMaintenance <= 2 &&
		   System.currentTimeMillis() < endtime) {
		Thread.sleep(200);
		testListenerMaintenance = testListener.getMaintenance();
	    }
	    PasswordVault.clearPassword(passwordChars);
            assertCondition( testListenerMaintenance > 2, "testListener.getMaintenance()="+testListenerMaintenance+" sb > 2"); 
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 


    /**
    Check that inactive connection is cleaned up if connections > maxConnections when a connection
    of a different type is requested (tests shutDownOldest()).
    **/
    public void Var127()
    {
	notApplicable("SSLight Testcase");
    } 


    /**
    Check that an exception is thrown if not enough connections can be cleaned up if connections > 
    maxConnections when a connection of a different type is requested (tests shutDownInactive()).
    **/
    public void Var128()
    {
	notApplicable("SSLight Testcase");

    } 


    /**
    Check that inactive connection is cleaned up if connections > maxConnections when a connection
    of a different type is requested and another instance of same type exists (tests shutDownInactive()).
    **/
    public void Var129()
    {
	notApplicable("SSLight Testcase");

    } 


    /**
    Check that exception is thrown if not enough connections can be cleaned up if 
    connections > maxConnections when a connection of a different type is requested and another 
    instance of same type exists (tests shutDownInactive()).
    **/
    public void Var130()
    {
	notApplicable("SSLight Testcase");

    } 


    /**
    Check that inactive connection is cleaned up if connections > maxConnections when a connection
    of a different type is requested and another instance of same type exists (tests shutDownInactive()).
    **/
    public void Var131()
    {
	notApplicable("SSLight Testcase");

    } 


    /**
    Check that exception is thrown if not enough connections can be cleaned up if 
    connections > maxConnections when a connection of a different type is requested and another instance 
    of same type exists (tests shutDownInactive()).
    **/
    public void Var132()
    {
	notApplicable("SSLight Testcase");

    } 


    /**
    Check that active connection is not cleaned up if maxConnections is changed to be below
    numberOfConnections.
    **/
    public void Var133()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.setMaxConnections(2);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 3); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 


    /**
    Check that active secure connection is not cleaned up if maxConnections is changed to be below
    numberOfConnections.
    **/
    public void Var134()
    {
	notApplicable("SSLight Testcase");

    } 


    /**
    Check that inactive connection is cleaned up if maxConnections is changed to be below
    numberOfConnections.
    **/
    public void Var135()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            p.setMaxConnections(2);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 2); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 


    /**
    Check that inactive secure connection is cleaned up if maxConnections is changed to be below
    numberOfConnections.
    **/
    public void Var136()
    {
	notApplicable("SSLight Testcase");

    } 


    /**
    Check that inactive connection is still cleaned up even if maxConnections is changed several times.
    **/
    public void Var137()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            p.setMaxConnections(5);
            p.setMaxConnections(3);
            p.setMaxConnections(2);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 2); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 


    /**
    Make sure maintenance thread can be turned on and off and function correctly.
    **/
    public void Var138()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.setRunMaintenance(false);
            p.setRunMaintenance(false);
            p.setRunMaintenance(true);
            p.setRunMaintenance(false);
            p.setRunMaintenance(false);
            p.setRunMaintenance(true);
            p.setRunMaintenance(true);
            p.returnConnectionToPool(o);
            p.setMaxConnections(1);
            Thread.sleep(1000);
            p.setRunMaintenance(false);
	    PasswordVault.clearPassword(passwordChars);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1); 
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 


    /**
    Make sure connections are not cleaned up if maintenance thread has been turned off.
    **/
    public void Var139()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.setRunMaintenance(false);
            p.returnConnectionToPool(o);
            p.setMaxConnections(1);
            Thread.sleep(1000);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 


    /**
    setThreadUsed(boolean)-Should complete successfully.
    **/
    public void Var140()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars);
            assertCondition(o.isThreadUsed());    
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setThreadUsed(boolean)-Should complete successfully.
    **/
    public void Var141()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setThreadUsed(true);
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            assertCondition(o.isThreadUsed());    
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setThreadUsed(boolean)-Should complete successfully.
    **/
    public void Var142()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setThreadUsed(false);
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars);
            assertCondition(!o.isThreadUsed());    
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    setThreadUsed(boolean)-Should throw exception if property is changed after connection filled.
    **/
    public void Var143()
    {
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
           AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.setThreadUsed(false);

	    failed("Did not throw exception."+o);    
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
 	    PasswordVault.clearPassword(passwordChars);

    }


    /**
    setThreadUsed(boolean)-Should throw exception if property is changed after connection filled.
    **/
    public void Var144()
    {
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.setThreadUsed(true);
            p.setThreadUsed(false);
           failed("Did not throw exception."+o);    
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
	    PasswordVault.clearPassword(passwordChars);

    }


    /**
    getConnection("localhost", "*CURRENT")-Should complete successfully.
    **/
    public void Var145()
    {
        // if not on the 400, do not run
        if (!runningNatively_)
        {
            notApplicable("Native-only variation");
            return;
        }
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            AS400 o = p.getConnection("localhost", "*CURRENT", "*CURRENT".toCharArray()); //@B2C
	    assertCondition(true, "o"+o);

            //@B2D CommandCall cmd = new CommandCall(o, "SNDMSG MSG('Hi Mom') TOUSR(" + systemObject_.getUserId() + ")");  
            //@B2D boolean check = cmd.run();
            //@B2D AS400Message[] messageList = cmd.getMessageList();
            //@B2D p.returnConnectionToPool(o);       //@A5A
            //@B2D assertCondition((check == true) && (messageList.length == 0) &&
            //@B2D                 (p.getAvailableConnectionCount("localhost", "*CURRENT") == 1) && //@A5A
            //@B2D                 (p.getActiveConnectionCount("localhost", "*CURRENT") == 0));   //@A5A
            //@B2D p.close();
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");  //@B2C
        }
    } 


    /**
    getConnection("localhost", "*CURRENT", int)-Should complete successfully.
    **/
    @SuppressWarnings("deprecation")
    public void Var146()
    {
        // if not on the 400, do not run
        if (!runningNatively_)
        {
            notApplicable("Native-only variation");
            return;
        }
        try
        {
            int service = AS400.COMMAND;
            AS400ConnectionPool p = new AS400ConnectionPool();
            AS400 o = p.getConnection("localhost", "*CURRENT", service);  
            //@B2D CommandCall cmd = new CommandCall(o, "SNDMSG MSG('Hi Mom') TOUSR(" + systemObject_.getUserId() + ")"); 
            //@B2D boolean check = cmd.run();
            //@B2D AS400Message[] messageList = cmd.getMessageList();
            //@B2D p.returnConnectionToPool(o);       //@A5A
            //@B2D assertCondition((check == true) && (messageList.length == 0) &&
            //@B2D                 p.getAvailableConnectionCount("localhost", "*CURRENT") == 1 && //@A5A
            //@B2D                 p.getActiveConnectionCount("localhost", "*CURRENT") == 0);   //@A5A
            //@B2D p.close();
            assertCondition(false, "should have thrown exception"+o); 
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException"); //@B2C
                    }
    } 


    /**
    getConnection("localhost", "*CURRENT", "*CURRENT")-Should complete successfully.
    **/
    public void Var147()
    {
        // if not on the 400, do not run
        if (!runningNatively_)
        {
            notApplicable("Native-only variation");
            return;
        }
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            AS400 o = p.getConnection("localhost", "*CURRENT", "*CURRENT".toCharArray());
            CommandCall cmd = new CommandCall(o, "SNDMSG MSG('Hi Mom') TOUSR(" + systemObject_.getUserId() + ")"); 
            boolean check = cmd.run();
            AS400Message[] messageList = cmd.getMessageList();
            p.returnConnectionToPool(o);       //@A5A
            assertCondition((check == true) && (messageList.length == 0) &&
                            p.getAvailableConnectionCount("localhost", "*CURRENT") == 1 && //@A5A
                            p.getActiveConnectionCount("localhost", "*CURRENT") == 0);   //@A5A
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 


    /**
    getConnection("localhost", "*CURRENT", "*CURRENT", int)-Should complete successfully.
    **/
    public void Var148()
    {
        // if not on the 400, do not run
        if (!runningNatively_)
        {
            notApplicable("Native-only variation");
            return;
        }
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
            AS400 o = p.getConnection("localhost", "*CURRENT", "*CURRENT".toCharArray(), service);
            CommandCall cmd = new CommandCall(o, "SNDMSG MSG('Hi Mom') TOUSR(" + systemObject_.getUserId() + ")"); 
            boolean check = cmd.run();     
            AS400Message[] messageList = cmd.getMessageList();
            p.returnConnectionToPool(o);       //@A5A
            assertCondition((check == true) && (messageList.length == 0) &&
                            p.getAvailableConnectionCount("localhost", "*CURRENT") == 1 && //@A5A
                            p.getActiveConnectionCount("localhost", "*CURRENT") == 0);   //@A5A
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 

    //@A5A
    /**
  getConnection()-returning connections to pool should work successfully even with localhost resolving!
  **/
    public void Var149()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 o = p.getConnection(systemName_, userId_, passwordChars, service);
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            cmd.run();
            p.returnConnectionToPool(o);
            assertCondition(p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 1);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    //@A5A
    /**
  getConnection()-returning connections to pool should work successfully even with localhost resolving!
  **/
    public void Var150()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        p.fill(systemName_, userId_, passwordChars, service, 4); 
            AS400 o = p.getConnection(systemName_, userId_, passwordChars, service);
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            cmd.run();
            p.returnConnectionToPool(o);
            assertCondition(p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 4);
	    PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    //@A5A
    /**
  Make sure addPasswordCacheEntry() works with *CURRENT, etc.
  **/
    public void Var151()
    {
        //@B2D try
        //@B2D {
        //@B2D      systemObject_.removePasswordCacheEntry(systemName_, userId_);
        //@B2D }
        //@B2D catch (Exception e)
        //@B2D {
        //@B2D     failed (e,"Unexpected Exception");
        //@B2D     return;
        //@B2D }
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       p.fill(systemName_, userId_, passwordChars, service, 4); 
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            cmd.run();
            p.returnConnectionToPool(o);
            assertCondition(p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 4);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    //@A6A
    /**
  Check removeFromPool() throws exception with null system.
  **/
    public void Var152()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
	char[] passwordChars =null; 
               passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
          p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            p.removeFromPool((String)null, systemObject_.getUserId());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
           assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
        PasswordVault.clearPassword(passwordChars);

    } 

    /**
Check removeFromPool() throws exception with null userId.
**/
    public void Var153()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
	char[] passwordChars = null; 
              passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
           p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            p.removeFromPool(systemObject_.getSystemName(), (String)null);
            failed("Did not throw exception.");
         }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
          PasswordVault.clearPassword(passwordChars);

    } 

    /**
  removeFromPool(String, String)-should complete successfully with no connections in the pool.
  **/
    public void Var154()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.removeFromPool(systemName_, userId_);
            assertCondition(p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 0);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
removeFromPool(String, String)-should complete successfully with one active connection in the pool.
**/
    public void Var155()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 o = p.getConnection(systemName_, userId_, passwordChars);   //@B2C
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            cmd.run();
            p.removeFromPool(systemName_, userId_);
            assertCondition(p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
  removeFromPool(String, String)-should complete successfully with one returned connection in the pool.
  **/
    public void Var156()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 o = p.getConnection(systemName_, userId_, passwordChars);  //@B2C
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            cmd.run();
            p.returnConnectionToPool(o);
            p.removeFromPool(systemName_, userId_);
         PasswordVault.clearPassword(passwordChars);
           assertCondition(p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 0);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
    removeFromPool(String, String)-should complete successfully with multiple connections in the pool.
    **/
    public void Var157()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         p.fill(systemName_, userId_, passwordChars, service, 4); 
            AS400 o = p.getConnection(systemName_, userId_, passwordChars);   //@B2C
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            cmd.run();
            p.returnConnectionToPool(o);
            p.removeFromPool(systemName_, userId_);
            assertCondition(p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
  removeFromPool(String, String)-should complete without exception with connections in the pool even if the 
  systemName/userId you specify is not in pool.
  **/
    public void Var158()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        p.fill(systemName_, userId_, passwordChars, service, 4); 
            AS400 o = p.getConnection(systemName_, userId_, passwordChars);  //@B2C
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            cmd.run();
            p.removeFromPool("myAS400", "myUserID");
	    PasswordVault.clearPassword(passwordChars);
            assertCondition(p.getActiveConnectionCount(systemName_, userId_) == 1 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 3);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
  removeFromPool(String, String)-should complete without exception while maintenance thread is running.
  **/
    public void Var159()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       AS400 o = p.getConnection(systemName_, userId_, passwordChars);
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            cmd.run();
            p.setCleanupInterval(100);
            p.setRunMaintenance(true);
            p.removeFromPool(systemName_, userId_);
            p.returnConnectionToPool(o);
            Thread.sleep(500);
            assertCondition(p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 0, "service="+service);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
removeFromPool() should complete without exception even with connections coming in and out of the pool.
**/
    public void Var160()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
            p.returnConnectionToPool(o);
            AS400 q = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
            p.returnConnectionToPool(q);
            AS400 r = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C;
            AS400 s = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
            p.returnConnectionToPool(r);
            p.returnConnectionToPool(s);
            p.removeFromPool(systemObject_.getSystemName(), systemObject_.getUserId());
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    } 

    /**
Check removeFromPool() does not affect connections in use--connection can still be used even though it is not
"visible" to the customer.
**/
    public void Var161()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
            p.removeFromPool(systemObject_.getSystemName(), systemObject_.getUserId());
            CommandCall cmd = new CommandCall(o, "SNDMSG MSG('Hi Mom') TOUSR(" + systemObject_.getUserId() + ")");
            boolean check = cmd.run();     
            AS400Message[] messageList = cmd.getMessageList();
            p.returnConnectionToPool(o);       //@A5A
	    PasswordVault.clearPassword(passwordChars);
            assertCondition(check && (messageList.length == 0));
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    } 

    /**
 Check what happens when a customer tries use a connection that has been "returned" to the pool 
 and cleaned up.
 **/
    public void Var162()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
            p.returnConnectionToPool(o);
            // Connection o should be cleaned up and deleted, but user still has reference.
            p.removeFromPool(systemObject_.getSystemName(), systemObject_.getUserId());
            CommandCall cmd = new CommandCall(o, "SNDMSG MSG('Hi Mom') TOUSR(" + systemObject_.getUserId() + ")");
            boolean check = cmd.run();     
            AS400Message[] messageList = cmd.getMessageList();
            assertCondition(check && (messageList.length == 0));
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    } 

    /**
  Check removeFromPool() does not cleanup connections for other system.
  **/
    public void Var163()
    {
	//
	// This testcase assumes too much.. update with correct information
	//

        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
               char[] pwrSyspasswordChars = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
         p.getConnection(systemObject_.getSystemName(), pwrSysUserID_, pwrSyspasswordChars);
            p.getConnection(systemObject_.getSystemName(), pwrSysUserID_, pwrSyspasswordChars);
            p.getConnection(systemObject_.getSystemName(), userId_, passwordChars);  //@B2C
            p.removeFromPool(systemObject_.getSystemName(), userId_);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), pwrSysUserID_) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), pwrSysUserID_) == 2 &&
                            p.getAvailableConnectionCount(systemObject_.getSystemName(), userId_) == 0 && //@B2A
                            p.getActiveConnectionCount(systemObject_.getSystemName(), userId_) == 0);     //@B2A
         PasswordVault.clearPassword(passwordChars);
         PasswordVault.clearPassword(pwrSyspasswordChars); 
         p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
   Make sure removeFromPool() can be called multiple times without exception with both active and available
   connections in the pool.
   **/
    public void Var164()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            p.removeFromPool(systemObject_.getSystemName(), systemObject_.getUserId());
            p.removeFromPool(systemObject_.getSystemName(), systemObject_.getUserId());
            p.removeFromPool(systemObject_.getSystemName(), systemObject_.getUserId());
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CLOSED() - Check that getSource() returns correct object when pool is closed.
    **/
    public void Var165()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();   
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
          PasswordVault.clearPassword(passwordChars);
           p.close();
            if (testListener.getSource() == p) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+p);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED() - Check that getSource() returns correct object when connection is created.
    **/
    public void Var166()
    {
        try
        {  
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 sys = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            if (testListener.getSource() == sys) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+sys);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED() - Check that getSource() returns correct object when connection is created after a fill()
    has completed.
    **/
    public void Var167()
    {
        try
        {  
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int numOfConnections = 1;
            int service = AS400.PRINT;
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service,
                   numOfConnections);
            AS400 sys = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            if (testListener.getSource() == sys) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+sys);
          PasswordVault.clearPassword(passwordChars);
           p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED - Check that getSource() returns correct object when connection is created after a fill()
    has completed for that particular service.
    **/
    public void Var168()
    {
        try
        {  
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int numOfConnections = 1;
            int service = AS400.COMMAND;
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service,
                   numOfConnections);
            AS400 sys = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);
            if (testListener.getSource() == sys) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+sys);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED - Check that getSource() returns correct object when connection is created after a fill
    has completed for a different service.
    **/
    public void Var169()
    {
        try
        {  
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int numOfConnections = 1;
            int service = AS400.PRINT;
                   char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
     p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service,
                   numOfConnections);
            AS400 sys = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, AS400.COMMAND);
            if (testListener.getSource() == sys) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+sys);
          PasswordVault.clearPassword(passwordChars);
           p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_CREATED - Check that getSource() returns correct object when secure connection is created.
    **/
    public void Var170()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    CONNECTION_EXPIRED - Check that getSource() returns correct object if a connection exceeded max 
    inactivity.
    **/
    public void Var171()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
            p.setMaxInactivity(100);
                     char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
   AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            Thread.sleep(5000);       //@A1C
            if (testListener.getSource() == p) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+p);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_EXPIRED - Check that getSource() returns correct object if a connection
    exceeds max use count.
    **/
    public void Var172()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
            p.setMaxUseCount(2);      //@A2C It was setMaxCount(1) which meant every time connection was 
            //replaced, it still had been used the max. number of times!
                     char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
   AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);   //@A2C
            Thread.sleep(5000);       //@A2C
          PasswordVault.clearPassword(passwordChars);
           if (testListener.getSource() == p) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+p);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_EXPIRED - Check that getSource() returns correct object if a connection lives past
    its expected lifetime.
    **/
    public void Var173()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
            p.setMaxLifetime(100);
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            Thread.sleep(5000);    //@A2C
            if (testListener.getSource() == p) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+p);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_EXPIRED - Check that getSource() returns correct object if a connection's 
    max usage time was exceeded.
    **/
    public void Var174()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(100);
            p.setRunMaintenance(true);
            p.setMaxUseTime(10);
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
          PasswordVault.clearPassword(passwordChars);
           Thread.sleep(5000);   //@A2C
            if (testListener.getSource() == p) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+p);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_EXPIRED - Check that getSource() returns correct object if the max usage time was exceeded.
    **/
    public void Var175()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(100);
            p.setRunMaintenance(true);
            p.setMaxUseTime(10);
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            Thread.sleep(300);
            if (testListener.getSource() == p) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+p);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_RELEASED - Check that getSource() returns correct object when getConnection() is called.
    **/
    public void Var176()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 sys = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            if (testListener.getSource() == sys) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+sys);
          PasswordVault.clearPassword(passwordChars);
           p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_RELEASED - Check that getSource() returns correct object when connection is given out
    after a fill().
    **/
    public void Var177()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            int service = AS400.COMMAND;
            int numOfConnections = 4;
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
            AS400 sys = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            if (testListener.getSource() == sys) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+sys);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_RELEASED - Check that getSource() returns correct object when connection is given out again.
    **/
    public void Var178()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            AS400 sys = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            if (testListener.getSource() == sys) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+sys);
         PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    CONNECTION_RETURNED - Check that getSource() returns correct object when a connection is returned
    to the pool.
    **/
    public void Var179()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       AS400 a = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars); //@B2C
            p.returnConnectionToPool(a);
            AS400 sys = a;
            if (testListener.getSource() == sys) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+sys);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    MAINTENANCE_THREAD_RUN - Check that getSource() returns correct object when maintenance_thread_run event is fired according to 
    setCleanupInterval.
    **/
    public void Var180()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
            int numOfConnections = 2;
            ExampleListener testListener = new ExampleListener();
            p.addConnectionPoolListener((ExampleListener)testListener);
            p.setCleanupInterval(50);
            p.setRunMaintenance(true);
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, 
                   numOfConnections);
            Thread.sleep(200);
            if (testListener.getSource() == p) succeeded();
            else failed("Source object mismatch: "+testListener.getSource()+" != "+p);
         PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 


    /**
    Test getUsers().
**/
    public void Var181()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            Enumeration<String> keys = p.getUsers();
            assertCondition (!keys.hasMoreElements());
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 

    /**
    Test getUsers().
**/
    public void Var182()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            Enumeration<String> keys = p.getUsers();
            assertCondition (keys.hasMoreElements() && 
                             (((String)keys.nextElement()).equals(systemObject_.getSystemName().toUpperCase() + "/" + systemObject_.getUserId().toUpperCase())));
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 

    /**
        Test getUsers(), getUsers(system), getConnectedUsers(system), and getSystemNames().
    **/
    public void Var183()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 conn1 = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            char[] pwrSyspasswordChars = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
            AS400 conn3 = p.getConnection(pwrSys_.getSystemName(), pwrSys_.getUserId(), pwrSyspasswordChars);
            conn3.connectService(AS400.COMMAND);
            Enumeration<String> keys = p.getUsers();
            String[] users = p.getUsers(systemObject_.getSystemName());
            String[] connectedUsers = p.getConnectedUsers(systemObject_.getSystemName());
            String[] systems = p.getSystemNames();

            // Convert the keys enumeration to an array, for inspection.
            Vector<String> keysV = new Vector<String>();
            while (keys.hasMoreElements()) {
              keysV.add(keys.nextElement());
            }
            String[] keyList = new String[keysV.size()];
            keysV.toArray(keyList);


            boolean keysSuccess = true;         //@D2A
            // verify list of keys is correct   //@D2A
            if(keyList.length != 2)
                keysSuccess = false;
            else
            {
                if(keyList[0].equals(pwrSys_.getSystemName().toUpperCase() + "/" + pwrSys_.getUserId().toUpperCase()))
                    keysSuccess = keyList[1].equals(systemObject_.getSystemName().toUpperCase() + "/" + systemObject_.getUserId().toUpperCase());
                else if (keyList[0].equals(systemObject_.getSystemName().toUpperCase() + "/" + systemObject_.getUserId().toUpperCase()))
                    keysSuccess = keyList[1].equals(pwrSys_.getSystemName().toUpperCase() + "/" + pwrSys_.getUserId().toUpperCase());
                else keysSuccess = false;
            }

            boolean userSuccess = true;        //@D2A
            //verify list of users is correct  //@D2A
            if(users == null)
                userSuccess = false;
            else{
                if(users.length != 2)
                    userSuccess = false;
                else
                    if(users[0].equals(pwrSys_.getUserId().toUpperCase()))
                        userSuccess = users[1].equals(systemObject_.getUserId().toUpperCase());
                    else if (users[0].equals(systemObject_.getUserId().toUpperCase()))
                        userSuccess = users[1].equals(pwrSys_.getUserId().toUpperCase());
                    else userSuccess = false;
            }
         PasswordVault.clearPassword(passwordChars);

            assertCondition (keysSuccess &&     //@D2C
                             userSuccess &&     //@D2C
                             connectedUsers != null &&
                             connectedUsers.length == 1 &&
                             connectedUsers[0].equals(pwrSys_.getUserId().toUpperCase()) &&
                             systems != null &&
                             systems.length == 1 &&
                             systems[0].equals(systemObject_.getSystemName().toUpperCase()), 
                             "connections are "+conn1+","+conn2+","+conn3); 
                             
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 

    /**
        Test getConnection with locale specified.
    **/
    public void Var184()
    {
        try
        {
            //Create a locale object with a language, country, and variant.  Note that
            //the language is always in lower case and the country is in upper case.
            Locale locale = new Locale("en", "DE", "WIN");

            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, locale);
            assertCondition ((o.getLocale().toString()).equals(locale.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 

    /**
    Test fill with locale specified.
**/
    public void Var185()
    {
        try
        {
            int numOfConnections = 5;
            int service = AS400.COMMAND;
            Locale locale = new Locale("en", "GB", "MAC");

            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections, locale);

            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, locale);
            assertCondition ((o.getLocale().toString()).equals(locale.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 4);
         PasswordVault.clearPassword(passwordChars);


       }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
        Test getConnection with no locale specified.
    **/
    public void Var186()
    {
        try
        {
            //The default in our environments will be "en", "US"
            Locale locale = new Locale("en", "US");
            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            assertCondition ((o.getLocale().toString()).equals(locale.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    } 


    /**
      Test fill with no locale specified.
  **/
    public void Var187()
    {
        try
        {
            int numOfConnections = 5;
            int service = AS400.COMMAND;
            Locale locale = new Locale("en", "US");

            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections);
    
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
        PasswordVault.clearPassword(passwordChars);
         assertCondition ((o.getLocale().toString()).equals(locale.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 4);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
        Test fill with switching of locales.
    **/
    public void Var188()
    {
        try
        {
            int numOfConnections = 5;
            int service = AS400.COMMAND;
            Locale defaultLocale = new Locale("en", "US");
            Locale locale = new Locale("en", "GB", "MAC");

            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections, locale);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, defaultLocale);
            assertCondition ((o.getLocale().toString()).equals(defaultLocale.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 5);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
        Test getConnection with switching of locales.
    **/
    public void Var189()
    {
        try
        {
            Locale defaultLocale = new Locale("en", "US");
            Locale locale = new Locale("en", "GB", "MAC");

            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 n = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, locale);
            p.returnConnectionToPool(n);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, defaultLocale);
         PasswordVault.clearPassword(passwordChars);
            assertCondition ((o.getLocale().toString()).equals(defaultLocale.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    Test getConnection with a locale and then no locale.
  **/
    public void Var190()
    {
        try
        {
            Locale defaultLocale = new Locale("en", "US");
            Locale locale = new Locale("en", "GB", "MAC");

            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 n = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, locale);
            p.returnConnectionToPool(n);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            assertCondition ((o.getLocale().toString()).equals(defaultLocale.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
      Test getConnection with no locale and then a locale.
    **/
    public void Var191()
    {

        try
        {
            // Locale defaultLocale = new Locale("en", "US");
            Locale locale = new Locale("en", "GB", "MAC");

            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 n = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(n);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, locale);
         PasswordVault.clearPassword(passwordChars);
            assertCondition ((o.getLocale().toString()).equals(locale.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
      Test getConnection with no locales.
    **/
    public void Var192()
    {
        try
        {
            Locale defaultLocale = new Locale("en", "US");
            // Locale locale = new Locale("en", "GB", "MAC");

            AS400ConnectionPool p = new AS400ConnectionPool();
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          AS400 n = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            if (JDReflectionUtil.callMethod_B(p, "isPretestConnections") ) n.connectService(AS400.SIGNON); // We need at least 1 service connected, in order to pass the "pretest" on the next getConnection().
            p.returnConnectionToPool(n);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            assertCondition ((o.getLocale().toString()).equals(defaultLocale.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
            Test fill with switching of locales.
        **/
    public void Var193()
    {
        try
        {
            int numOfConnections = 5;
            int service = AS400.COMMAND;
            Locale defaultLocale = new Locale("en", "US");
            Locale locale = new Locale("en", "GB", "MAC");

            AS400ConnectionPool p = new AS400ConnectionPool();
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections, locale);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections, defaultLocale);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, locale);
          PasswordVault.clearPassword(passwordChars);
           assertCondition ((o.getLocale().toString()).equals(locale.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 9);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
        Test fill with switching of locales.
    **/
    public void Var194()
    {
        try
        {
            int numOfConnections = 5;
            int service = AS400.COMMAND;
            Locale defaultLocale = new Locale("en", "US");
            Locale locale = new Locale("en", "GB", "MAC");
            Locale locale2 = new Locale("en", "US", "WIN");

            AS400ConnectionPool p = new AS400ConnectionPool();
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections, locale);
            p.fill(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service, numOfConnections, defaultLocale);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, locale2);
            assertCondition ((o.getLocale().toString()).equals(locale2.toString())
                             && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1
                             && p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 10);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    //@B2A
    /**
    setThreadUsed(boolean)-Should complete successfully.
    **/
    public void Var195()
    {
        try
        {
            int service = AS400.COMMAND;
            AS400ConnectionPool p = new AS400ConnectionPool();
            p.setThreadUsed(false);
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars, service);
         PasswordVault.clearPassword(passwordChars);
            assertCondition(!o.isThreadUsed());    
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
      Test getConnection with no SocketProperties and then a SocketProperties.
    **/
    public void Var196()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            SocketProperties poolProps0 = p.getSocketProperties();

            SocketProperties newProps = new SocketProperties();
            newProps.setSoLinger(12345);

               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 n1 = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);

            SocketProperties poolProps1 = p.getSocketProperties();

            p.setSocketProperties(newProps);
            SocketProperties poolProps2 = p.getSocketProperties();

            AS400 n2 = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);

            p.setSocketProperties(null);
            SocketProperties poolProps3 = p.getSocketProperties();

            AS400 n3 = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);

            assertCondition (poolProps0 == null &&
                             poolProps1 == null &&
                             poolProps2.equals(newProps) &&
                             poolProps2.getSoLinger() == 12345 &&
                             poolProps3 == null &&
                             n1.getSocketProperties().isSoLingerSet() == false &&
                             n2.getSocketProperties().isSoLingerSet() == true &&
                             n2.getSocketProperties().getSoLinger() == 12345 &&
                             n3.getSocketProperties().isSoLingerSet() == false);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

//@C1A  Test for JTOpen Bug 3863 - make sure a null pointer exception is not thrown when you use maintenance but not threads
    public void Var197(){
        try{
            AS400ConnectionPool pool=new AS400ConnectionPool();
            AS400 connection=null;

            //pool.setRunMaintenance(false);
            pool.setThreadUsed(false);
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           connection = pool.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            pool.returnConnectionToPool( connection );
            succeeded();
         PasswordVault.clearPassword(passwordChars);
        }catch(Exception e){
            failed (e, "Unexpected Exception");
        }
            
    }
    
 //$D4 Added variations to test methods with ProfileTokenCredential.
    /**
     fill(String, String, ProfileTokenCredential, int, int). - Should complete successfully.
    **/
    public void Var198()
    {
      AS400 system = null; 
       try
       { 
             AS400ConnectionPool p = new AS400ConnectionPool();
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
         PasswordVault.clearPassword(passwordChars);
	     char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
             ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	     PasswordVault.clearPassword(swapToPasswordChars); 
	     PasswordVault.clearPassword(swapToPasswordChars);
             int service = AS400.PRINT;
             int numOfConnections = 1;
             p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, service, 
                    numOfConnections);      
             assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == numOfConnections); 
             p.close();
             system.close(); 
       }
       catch (Exception e)
       {
            failed(e, "Unexpected Exception");
       }
    }

    /**
    fill(String, String, ProfileTokenCredential, int, int). - Should complete successfully.
    **/
    public void Var199()
    {
      AS400 system = null; 
      try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
      	    int service = AS400.FILE;
            int numOfConnections = 1;
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, 
                   service, numOfConnections);
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                        swapToUserID_, pt1, service);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                        swapToUserID_, pt1, service);
         PasswordVault.clearPassword(passwordChars);
            assertCondition(conn1.isConnected(service) && conn2.isConnected(service) && 
                            p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0);
            system.close(); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
      fill(String, String, ProfileTokenCredential, int, int). - Should complete successfully.
    **/
    public void Var200()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.FILE;
            int numOfConnections = 3;
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                          swapToUserID_, pt1, service);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                          swapToUserID_, pt1, service);
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, 
                   service, numOfConnections);
         PasswordVault.clearPassword(passwordChars);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == numOfConnections
                           && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 2, 
                             "connections are "+conn1+","+conn2);
            system.close(); 
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    fill(String, String,ProfileTokenCredential, int, int) - Should complete successfully (fill will use the already 
    created connection which is not in use-- it just makes sure that there are the correct number of 
    connections to that service).
    **/
    public void Var201()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.FILE;
            int numOfConnections = 3;
             swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, swapToPasswordChars, service);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service);
            p.returnConnectionToPool(o);
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, 
                   service, numOfConnections);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == numOfConnections++
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);
            system.close(); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    fill(String, String, ProfileTokenCredential, int, int) - Should complete successfully.
    **/
    public void Var202()
    {
      AS400 system = null; 
       try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.FILE;
            int numOfConnections = 3;
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                          swapToUserID_, pt1, service);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                          swapToUserID_, pt1, service);
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, 
                   service, numOfConnections);
            p.returnConnectionToPool(conn1);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 4
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1, 
                            "connections are "+conn1+","+conn2);
          PasswordVault.clearPassword(passwordChars);
          system.close(); 
          p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    fill(String, String, ProfileTokenCredential, int, int) - Should throw an exception if nonexistant 
    system name.
    **/
    public void Var203()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        char[] passwordChars = null; 
        AS400 system = null; 
        try
        { 
          String systemName = "badSystemName";
              passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
          ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	  PasswordVault.clearPassword(swapToPasswordChars); 
          int service = AS400.COMMAND;
          int numOfConnections = 1;
          p.fill(systemName, swapToUserID_, pt1, service, numOfConnections);      
          failed("Did not throw exception.");
         PasswordVault.clearPassword(passwordChars);
      }
      catch (Exception e)
      {
	  if (passwordChars != null) { 
	      PasswordVault.clearPassword(passwordChars);
	  }

          succeeded();  
         
      } finally {
        if (system != null) system.close(); 
        
      }
    }

    /**
    fill(String, String, ProfileTokenCredential, int, int) - Should throw an extended illegal argument exception if 
    numOfConnections < 1.
    **/
    public void Var204()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
             AS400 system = null; 
        try
        {
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            int numOfConnections = 0;
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, service, 
                   numOfConnections);
           failed("Did not throw exception.");
        }
        catch (Exception e)
        {
	       assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
            p.close();
           
            
        }
        finally { 
          if (system != null) system.close(); 
        }
         PasswordVault.clearPassword(passwordChars);

    }

    /**
    fill(String, String, ProfileTokenCredential, int, int) - Should throw an exception if requested # of 
    connections > maxConnections.
    **/
    public void Var205()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
                AS400 system=null; 
       try
        {
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            int numOfConnections = 2;
            p.setMaxConnections(1);       
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, service, 
                   numOfConnections);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");    //@A2C
            p.close();
        }
       finally {
         if (system != null) system.close(); 
       }
         PasswordVault.clearPassword(passwordChars);

    }

    /**
    fill(String, String, ProfileTokenCredential, int, int) - Should throw an exception since the service
    does not exists.
    **/
    public void Var206()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
	char[] passwordChars = null; 
	AS400 system = null; 
        try
        {
               passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
             system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
        	ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
		PasswordVault.clearPassword(swapToPasswordChars); 
            int service = 200;
            int numOfConnections = 2;
            p.setMaxConnections(1);       
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, service, 
                   numOfConnections);
		PasswordVault.clearPassword(passwordChars);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
	    if (passwordChars != null) { 
		PasswordVault.clearPassword(passwordChars);
	    }
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");  
            p.close();
        }
        finally {
          if (system != null) system.close(); 
        }
    }

    /**
       Test fill(String, String, ProfileTokenCredential, int, int, Locale). - Should complete successfully.
    **/
    public void Var207()
    {  
      AS400 system=null; 
        try
        { 
            AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.PRINT;
            int numOfConnections = 1;
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, service, 
                   numOfConnections, Locale.US);      
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == numOfConnections); 
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");      
        }
        finally {
          if (system != null) system.close(); 
        }
    }

    /**
    Test fill(String, String, ProfileTokenCredential, int, int, Locale). - Should complete successfully.
    **/
    public void Var208()
    {
      AS400 system=null; 

        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
         	ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
		PasswordVault.clearPassword(swapToPasswordChars); 
      	    int service = AS400.FILE;
            int numOfConnections = 1;
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, 
                   service, numOfConnections, Locale.US);
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                          swapToUserID_, pt1, service, Locale.US);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                          swapToUserID_, pt1, service, Locale.US);
            assertCondition(conn1.isConnected(service) && conn2.isConnected(service) && 
                            p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0);
          PasswordVault.clearPassword(passwordChars);
	  p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          if (system != null) system.close(); 
        }
    }

    /**
       Test fill(String, String, ProfileTokenCredential, int, int, Locale). - Should complete successfully.
    **/
    public void Var209()
    {
      AS400 system=null; 

        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.FILE;
            int numOfConnections = 3;
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                          swapToUserID_, pt1, service, Locale.US);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                          swapToUserID_, pt1, service, Locale.US);
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, 
                   service, numOfConnections, Locale.US);
           PasswordVault.clearPassword(passwordChars);
          assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == numOfConnections
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 2, 
                            "connections are "+conn1+","+conn2);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        } 
        finally {
          if (system != null) system.close(); 
        }
    }

    /**
    fill(String, String,ProfileTokenCredential, int, int, Locale) - Should complete successfully (fill will use the already 
    created connection which is not in use-- it just makes sure that there are the correct number of 
    connections to that service).
    **/
    public void Var210()
    {
      AS400 system=null; 

        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.FILE;
            int numOfConnections = 3;
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), pt1, service, Locale.US);
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service, Locale.US);
            p.returnConnectionToPool(o);
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, service, numOfConnections, Locale.US);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == numOfConnections++
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);
          PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          if (system != null) system.close(); 
        }
    }

    /**
    fill(String, String, ProfileTokenCredential, int, int) - Should complete successfully.
    **/
    public void Var211()
    {
      AS400 system=null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
        	ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
		PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.FILE;
            int numOfConnections = 3;
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                          swapToUserID_, pt1, service, Locale.US);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                          swapToUserID_, pt1, service, Locale.US);
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, 
                   service, numOfConnections, Locale.US);
            p.returnConnectionToPool(conn1);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 4
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1,
                            "connections are "+conn1+","+conn2);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
       {
            failed (e, "Unexpected Exception");
       }
        finally {
          if (system != null) system.close(); 
        }
    }

    /**
    fill(String, String, ProfileTokenCredential, int, int, Locale) - Should throw an exception if nonexistant 
    system name.
    **/
    public void Var212()
    {
AS400 system = null; 
        AS400ConnectionPool p = new AS400ConnectionPool();
char[] passwordChars =null; 
        try
        {
            String systemName = "badSystemName";
                passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
         	ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
		PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            int numOfConnections = 1;
          
            p.fill(systemName, swapToUserID_, pt1, service, numOfConnections, Locale.US);      
           PasswordVault.clearPassword(passwordChars);
           failed("Did not throw exception.");
        }
        catch (Exception e)
        {
	    if (passwordChars != null) {
	
		PasswordVault.clearPassword(passwordChars);
	    }
            succeeded();  
            p.close();
        }
        finally {
          if (system != null) system.close(); 
        }
    }

    /**
    fill(String, String, ProfileTokenCredential, int, int, Locale) - Should throw an extended illegal argument exception if 
    numOfConnections < 1.
    **/
    public void Var213()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
              AS400 system = null; 
        try
        {
          system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            int numOfConnections = 0;
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, service, 
                   numOfConnections, Locale.US);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
            p.close();
        }
        finally {
          if (system != null) system.close(); 
        }
        PasswordVault.clearPassword(passwordChars);

    }
    
    /**
    fill(String, String, ProfileTokenCredential, int, int, Locale) - Should throw an exception if requested # of 
    connections > maxConnections.
    **/
    public void Var214()
    {
 char[] passwordChars =  null; 
        AS400ConnectionPool p = new AS400ConnectionPool();
        AS400 system = null; 
        try
        {
              passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
        	ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);    
      PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            int numOfConnections = 2;
            p.setMaxConnections(1);       
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, service, 
                   numOfConnections, Locale.US);
		PasswordVault.clearPassword(passwordChars);
            failed("Did not throw exception.");
        }
        catch (Exception e)
       {
	    if (passwordChars != null) { 
		PasswordVault.clearPassword(passwordChars);
	    }
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");    //@A2C
            p.close();
        }
        finally {
          if (system != null) system.close(); 
        }
    }

    /**
    fill(String, String, ProfileTokenCredential, int, int, Locale) - Should throw an exception since
    the service does not exists.
    **/
    public void Var215()
    {
	char[] passwordChars = null ;
                passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400ConnectionPool p = new AS400ConnectionPool();
        AS400 system = null; 
         try
        {
         system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = 200;
            int numOfConnections = 2;
            p.setMaxConnections(1);       
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, service, 
                   numOfConnections, Locale.US);
            failed("Did not throw exception.");
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
            p.close();
        }
         finally {
           if (system != null) system.close(); 
         }
         PasswordVault.clearPassword(passwordChars);
   
    }
  
    /**
     getConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service) - Should complete successfully.
    **/
    public void Var216()
    {
    	AS400ConnectionPool p = new AS400ConnectionPool();
  	AS400 system = null; 
        try
        {           
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
            int service = AS400.CENTRAL;
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service);
            succeeded();
        PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
        	failed(e, "Unexpected exception.");
        }
      
        finally
        {
        	p.close();
        	if (system != null) system.close(); 
        } 
    }  

    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken) - 
    Check that functioning connection was returned.
    **/
    public void Var217()
    {
    	AS400ConnectionPool p = new AS400ConnectionPool();
    	AS400 system = null; 
        try
        {       
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1);
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1);            
            CommandCall cmd = new CommandCall(o);
            cmd.run("QSYS/CRTLIB FRED");
            succeeded(); 
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "UnexpectedException");
        }
       
        finally
        {
         try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        	p.close();
                  if (system != null) system.close(); 

        }
    }  

    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken) - Should complete successfully.
    **/
    public void Var218()
    {
     	AS400ConnectionPool p = new AS400ConnectionPool();
     	AS400 system = null; 
        try
        {   
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1);  

        PasswordVault.clearPassword(passwordChars);
            assertCondition(o instanceof AS400 && !(o instanceof SecureAS400)
          	   && p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
               && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);
        }
        catch (Exception e)
        {
        	 failed(e, "UnexpectedException");
        } 
        finally
        {
        	p.close();
                  if (system != null) system.close(); 
        }
    }

    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken) - Should complete successfully.
    **/
    public void Var219()
    {
    	AS400ConnectionPool p = new AS400ConnectionPool();
    	AS400 system = null; 
        try
        {        
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                                      pt1);
            assertCondition(o instanceof AS400 && !(o instanceof SecureAS400) && !(o.isConnected())
                            && p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
        finally
        {
        	p.close();        
                  if (system != null) system.close(); 
               

        }
    }

    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken) - Should complete successfully.
    **/
    public void Var220()
    {
    	AS400ConnectionPool p = new AS400ConnectionPool();
    	AS400 system = null; 
        try
        {        
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                                      pt1);
         PasswordVault.clearPassword(passwordChars);
           assertCondition(o instanceof AS400 && !(o instanceof SecureAS400) && !(o.isConnected())
                            && p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }

        finally
        {
        	p.close();
                  if (system != null) system.close(); 

        }
    }

    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken) - Should not create a new connection if an appropriate
    one already exists.
    **/
    public void Var221()
    {	
    	AS400ConnectionPool p = new AS400ConnectionPool();
    	AS400 system = null; 
        try
        {
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                                      pt1);
            if (JDReflectionUtil.callMethod_B(p, "isPretestConnections")) o.connectService(AS400.SIGNON); // We need at least 1 service connected, in order to pass the "pretest" on the next getConnection().
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                            pt1);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);      
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
        
      
        finally
        {
        	p.close();
                if (system != null) system.close(); 
        }
    }

    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken) - Should throw an exception if system name is null.
    **/
    public void Var222()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
                 AS400 system = null; 
       try
        {
              system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            p.getConnection((String)null, swapToUserID_,pt1);
         PasswordVault.clearPassword(passwordChars);
           failed("Did not throw exception.");
        }
        catch (Exception e)
        {
         PasswordVault.clearPassword(passwordChars);
           assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");  
            p.close();
        }
       finally {
         if (system != null) system.close(); 

       }
    }

    /**
     getConnection(String systemName, String userID, ProfileTokenCredential profileToken) - Should throw an exception if userId is null.
    **/
    public void Var223()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
                 AS400 system = null; 
      try
        {
        	system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            p.getConnection(systemObject_.getSystemName(), (String)null, pt1);
            failed("Did not throw exception.");
         }
        catch (Exception e)
        {
               assertExceptionIsInstanceOf(e, "java.lang.NullPointerException"); 
            p.close();
        } 
      finally {
        if (system != null) system.close(); 

      }
        PasswordVault.clearPassword(passwordChars);

    }

    /**
     getConnection(String systemName, String userID, ProfileTokenCredential profileToken) - Should throw an exception 
     if ProfileTokenCredential is null.
    **/
    public void Var224()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
                 AS400 system = null; 
       try
        {
          system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
            ProfileTokenCredential pt1 = null;
            p.getConnection(systemObject_.getSystemName(), systemObject_.getSystemName(), pt1);
            failed("Did not throw exception."+system);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException"); 
            p.close();
        }
       finally {
         if (system != null) system.close(); 

       }
        PasswordVault.clearPassword(passwordChars);

    }

    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken) - Should fail after max is reached.
    **/
    public void Var225()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
                AS400 system = null; 
        try
        {
           	 system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            p.setMaxConnections(1);
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1); 
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1); 
            failed("Did not throw exception.");
    
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");    
            p.close();
        }
        finally {
          if (system != null) system.close(); 

        }
        PasswordVault.clearPassword(passwordChars);

    }  
    
    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service) - Should complete successfully.
    **/
    public void Var226()
    {
      AS400 system = null; 
        try
        {
                  char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       	 system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_,pt1, service);
            assertCondition(true, "o="+o);
     PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          if (system != null) system.close(); 

        }
    }  
    
    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service)  - Check that functioning connection was returned.
    **/
    public void Var227()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        	 system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service); 
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service); 
            CommandCall cmd = new CommandCall(o);
            cmd.run("QSYS/CRTLIB FRED");
            succeeded();
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "UnexpectedException");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
            if (system != null) system.close(); 

        }
    }  
   
    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service) - Should complete successfully.
    **/
    public void Var228()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          	 system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                                      pt1, service);
            assertCondition(o instanceof AS400 && !(o instanceof SecureAS400) && (o.isConnected())
                            && p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);
	    PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
        finally {
          if (system != null) system.close(); 

        }
    }
   
    /** 
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service)
    Should not create a new connection if an appropriate one already exists.
    **/
    public void Var229()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         	system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                                      pt1, service);
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                            pt1, service);
            
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);      
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
        finally {
          if (system != null) system.close(); 

        }
   }
   
    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service) - Should throw an exception if system name is null.
    **/
    public void Var230()
    {
      AS400 system = null; 
        AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
       {
        	 system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            
            p.getConnection((String)null, swapToUserID_, pt1, service);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");  
            p.close();
        }
       finally {
         if (system != null) system.close(); 

       }
        PasswordVault.clearPassword(passwordChars);

    }
    

    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service) - Should throw an exception if userId is null.
    **/
    public void Var231()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
                 AS400 system = null; 
       try
        {
         	 system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            
            p.getConnection(systemObject_.getSystemName(), (String)null, pt1, service);
            failed("Did not throw exception.");
         }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");  
            p.close();
        }
       finally {
         if (system != null) system.close(); 

       }
        PasswordVault.clearPassword(passwordChars);
    }
    
    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service) - Should throw an exception if ProfileTokenCredential is null.
    **/
    public void Var232()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
                AS400 system = null; 
       try
        {
              system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
            ProfileTokenCredential pt1 = null;
            int service = AS400.COMMAND;
            
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service);
            failed("Did not throw exception."+system);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException"); 
            p.close();
        }
       finally {
         if (system != null) system.close(); 

       }
        PasswordVault.clearPassword(passwordChars);

    }
   
    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service)  - Should throw an exception if service number < 0.
    **/
    public void Var233()
    {
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       AS400ConnectionPool p = new AS400ConnectionPool();
       AS400 system = null; 
        try
        {
              system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = -1;
           
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");  
            p.close();
        }
        finally {
          if (system != null) system.close(); 

        }
         PasswordVault.clearPassword(passwordChars);

    }
   
    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service)  - Should throw an exception if service number > 7.
    **/
    public void Var234()
    {
                  char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
      AS400ConnectionPool p = new AS400ConnectionPool();
      AS400 system = null; 
        try
        {
             system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = 8;
           
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");  
            p.close();
        }
        finally {
          if (system != null) system.close(); 

        }
               PasswordVault.clearPassword(passwordChars);
    }
   
    /**
    getConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service) - Should fail after max is reached.
    **/
     public void Var235()
     {
        AS400ConnectionPool p = new AS400ConnectionPool();
        AS400 system = null; 
        try
        {
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
           
            p.setMaxConnections(1);
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service);
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service); 
            failed("Did not throw exception.");
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");     
            p.close();
        }
        finally {
          if (system != null) system.close(); 

        }
    }  
     
    /**
     getConnection(String systemName, String userID, ProfileTokenCredential profileToken, Locale locale) - Should complete successfully.
    **/
    public void Var236()
    
    {
      AS400 system = null; 
      try
         {
             AS400ConnectionPool p = new AS400ConnectionPool();
                  char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
             ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	     PasswordVault.clearPassword(swapToPasswordChars); 
             
             AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, Locale.US);
            
             assertCondition(o instanceof AS400 && !(o instanceof SecureAS400) && !(o.isConnected())
            		 && p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
            		 && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);
         PasswordVault.clearPassword(passwordChars);
            
             p.close();
           }
           catch (Exception e)
           {
               failed(e, "Unexpected Exception");
              
           }  
         finally {
           if (system != null) system.close(); 

         }
    }
    
    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken, Locale locale) - Check that functioning connection was returned.
    **/
    public void Var237()
    {
      AS400 system = null; 
       try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, Locale.US);
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1);
            CommandCall cmd = new CommandCall(o);
            cmd.run("QSYS/CRTLIB FRED");
            succeeded();
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "UnexpectedException");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
            if (system != null) system.close(); 

        }
    }  

    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken, Locale locale) - Should not create a new connection if an appropriate
    one already exists.
    **/
    public void Var238()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                                      pt1, Locale.US);
            
            if (JDReflectionUtil.callMethod_B(p, "isPretestConnections")) o.connectService(AS400.SIGNON); // We need at least 1 service connected, in order to pass the "pretest" on the next getConnection().
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                            pt1, Locale.US);
            
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);
         PasswordVault.clearPassword(passwordChars);  
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
        finally {
          if (system != null) system.close(); 

        }
    }

    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken, Locale locale) - Should throw an exception if system name is null.
    **/
    public void Var239()
    {
      AS400 system = null; 
        AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
             system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            
            p.getConnection((String)null, swapToUserID_, pt1, Locale.US);
            failed("Did not throw exception.");
         }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException"); 
            p.close();
        }
       finally {
         if (system != null) system.close(); 

       }
       PasswordVault.clearPassword(passwordChars);
    }

    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken, Locale locale) - Should throw an exception if ProfileTokenCredential is null.
    **/
    public void Var240()
    {
      AS400 system = null; 
        AS400ConnectionPool p = new AS400ConnectionPool();
	char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
      try
        {
             system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
            ProfileTokenCredential pt1 = null;  
            
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, Locale.US);
            failed("Did not throw exception."+system);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");  
            p.close();
        }
      finally {
        if (system != null) system.close(); 

      }
        PasswordVault.clearPassword(passwordChars);

    }
    
    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken, Locale locale) - Should throw an exception if userId is null.
    **/
    public void Var241()
    {
      AS400 system = null; 
        AS400ConnectionPool p = new AS400ConnectionPool();
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       try
        {
   
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            
            p.getConnection(systemObject_.getSystemName(), (String)null, pt1, Locale.US);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");  
            p.close();
        }
       finally {
         if (system != null) system.close(); 

       }
	PasswordVault.clearPassword(passwordChars);

    }

    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service, Locale locale) - Should complete successfully.
    **/
    public void Var242()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                                      pt1, service, Locale.US);
            
            assertCondition(o instanceof AS400 && !(o instanceof SecureAS400) && o.isConnected(service)
                            && p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);      
 	PasswordVault.clearPassword(passwordChars);
           p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
        finally {
          if (system != null) system.close(); 

        }
    }

    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service, Locale locale) - Should not create a new connection if an appropriate
    one already exists.
    **/
    public void Var243()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                  char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            
            AS400 o = p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                                      pt1, service, Locale.US);
            p.returnConnectionToPool(o);
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, 
                            pt1, service, Locale.US);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);      
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
        finally {
          if (system != null) system.close(); 

        }
    }
   
    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service, Locale locale) - Should throw an exception if system name is null.
    **/
    public void Var244()
    {
	char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400ConnectionPool p = new AS400ConnectionPool();
        AS400 system = null; 
        try
        {
                 
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            
            p.getConnection((String)null, swapToUserID_, pt1, service, Locale.US);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");  
            p.close();
        }
        finally {
          if (system != null) system.close(); 

        }
	if (passwordChars != null) { 
         PasswordVault.clearPassword(passwordChars);
	}

    }
    
    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service, Locale locale) - Should throw an exception if userId is null.
    **/
    public void Var245()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
                 AS400 system = null; 
        try
        {
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            
            p.getConnection(systemObject_.getSystemName(), (String)null, pt1, service, Locale.US);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");  
            p.close();
        }
        finally {
          if (system != null) system.close(); 

        }
         PasswordVault.clearPassword(passwordChars);
    }
    
    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service, Locale locale) - Should throw an exception if ProfileTokenCredential is null.
    **/
    public void Var246()
    {
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
        AS400ConnectionPool p = new AS400ConnectionPool();
        AS400 system = null; 
      try
        {
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
            ProfileTokenCredential pt1 = null;
            int service = AS400.COMMAND;
            
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service, Locale.US);
            failed("Did not throw exception."+system);

        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");  
            p.close();
        }
        finally {
          if (system != null) system.close(); 

        }
        PasswordVault.clearPassword(passwordChars);
    }
    
    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service, Locale locale) - Should throw an exception if service number < 0.
    **/
    public void Var247()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        AS400 system = null; 
       try
        {
                  char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
          system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = -1;
            
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service, Locale.US);
            failed("Did not throw exception.");
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException"); 
            p.close();
        }
        finally {
          if (system != null) system.close(); 

        }
    }

    /**
    getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service, Locale locale) - Should throw an exception if service number > 7.
    **/
    public void Var248()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
                AS400 system = null; 
      try
        {
             system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = 8;
            
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service, Locale.US);
              failed("Did not throw exception.");
        }
        catch (Exception e)
        {
               assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException"); 
            p.close();
        }
       finally {
         if (system != null) system.close(); 

       }
      PasswordVault.clearPassword(passwordChars);

    }
    



    /**
   getConnectiongetConnection(String systemName, String userID, ProfileTokenCredential profileToken,int service, Locale locale) - Should fail after max is reached.
    **/
    public void Var249()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        AS400 system = null; 
        try
        {
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            
            p.setMaxConnections(1);
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service, Locale.US ); 
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1, service, Locale.US ); 
            failed("Did not throw exception.");
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ConnectionPoolException");     
            p.close();
        }
        finally {
          if (system != null) system.close(); 

        }
    }  

    /**
    getSecureConnection getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken)
     - Should successfully create a SecureAS400 instance.
    **/
    public void Var250()
    {
	notApplicable("SSLight Testcase");

    }

    /**
    getSecureConnection getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken) - Should successfully create a SecureAS400 instance.
    **/
    public void Var251()
    {
	notApplicable("SSLight Testcase");

    }
    
    /**
    getSecureConnection getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken) - Should fail if system name is null.
    **/
    public void Var252()
    {
	notApplicable("SSLight Testcase");

    }
    
    /**
    getSecureConnection getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken)  - Should throw an exception if userId is null.
    **/
    public void Var253()
    {
	notApplicable("SSLight Testcase");

    }
    
    /**
    getSecureConnection getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken)  - Should throw an exception if ProfileTokenCredential is null.
    **/
    public void Var254()
    {
	notApplicable("SSLight Testcase");

    }


    /**
    getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service)  - Should successfully create a SecureAS400 instance.
    **/
    public void Var255()
    {
	notApplicable("SSLight Testcase");

    }

    /**
   getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service) - Should fail if system name is null.
    **/
    public void Var256()
    {
	notApplicable("SSLight Testcase");

    }

    /**
       getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service) - Should throw an exception if userId is null.
    **/
    public void Var257()
    {
	notApplicable("SSLight Testcase");

    }
    
    /**
    getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service) - Should throw an exception if ProfileTokenCredential is null.
    **/
    public void Var258()
    {
	notApplicable("SSLight Testcase");

    }

    /**
    getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service) - Should throw an exception if service < 0.
    **/
    public void Var259()
    {
	notApplicable("SSLight Testcase");

    }
   
    /**
    getSecureConnection(String systemName, String userID, ProfileTokenCredential profileToken, int service) - Should throw an exception if service > 7.
    **/
    public void Var260()
    {
	notApplicable("SSLight Testcase");

    }
    
    /**
    returnConnectionToPool(AS400) - Passing a connection in the pool should successfully
    return it to the list and mark it as inactive. 
    **/
    public void Var261()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            AS400 newItem = p.getConnection(systemObject_.getSystemName(), 
                                            swapToUserID_, pt1);
            
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(newItem, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            p.returnConnectionToPool(newItem);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0, 
                            "check = "+check);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
            if (system != null) system.close(); 


        }
    }
     
    /**
    returnConnectionToPool(AS400) - Passing a connection in the pool should successfully
    return it to the list and mark it as inactive. 
    **/
    public void Var262()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            AS400 newItem = p.getConnection(systemObject_.getSystemName(), 
                                            swapToUserID_, pt1, service);
            
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(newItem, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            p.returnConnectionToPool(newItem);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0, 
                              "check = "+check);
            p.close();
        PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        
            if (system != null) system.close(); 

          

        }
    }
    
    /**
    returnConnectionToPool(AS400) - Passing a connection in the pool should successfully
    return it to the list and mark it as inactive. 
    **/
    public void Var263()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                   char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            AS400 newItem = p.getConnection(systemObject_.getSystemName(), 
                                            swapToUserID_, pt1, service, Locale.US);
            
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(newItem, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            p.returnConnectionToPool(newItem);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0, "check="+check);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
          
            if (system != null) system.close(); 

          

        }
    }

    /**
    returnConnectionToPool(AS400) - Passing item that is in the pool should successfully
    return it to the list and mark it as inactive even if there are other connections in the list. 
    **/
    public void Var264()
    {
      AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool(); 
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            int service = AS400.COMMAND;
            int numOfConnections = 4;
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1);
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, service,
            		numOfConnections);
            AS400 system2 = p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1); //@B2C
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(system2, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            p.returnConnectionToPool(system2);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == numOfConnections 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1, "check="+check);
        PasswordVault.clearPassword(passwordChars);
            p.close();
            }
        catch (Exception e)
        {
             failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        
            if (system != null) system.close(); 

         

        }
    }



    /**
    returnConnectionToPool(AS400) - Passing item that isn't in the pool should not add it to 
    the list.  (There will be a warning trace message.) 
    **/
    public void Var265()
    {
      AS400 system = null; 
        try
        {
            CommandCall cmd0 = new CommandCall(pwrSys_, "QSYS/CHGJOB INQMSGRPY(*SYSRPYL)");
	    cmd0.run(); 
	    cmd0 = new CommandCall(pwrSys_, "QSYS/DLTLIB FRED");
            cmd0.run();

            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);  //@B2C
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
            ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1200);
	    PasswordVault.clearPassword(swapToPasswordChars); 
            p.getConnection(systemObject_.getSystemName(), swapToUserID_, pt1); 
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(system, "QSYS/CRTLIB FRED");  //@A5A
            boolean check = cmd.run();  //@A5A
            p.returnConnectionToPool(system);
            assertCondition(check &&
                            p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 1);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
    getConnection(String, String, String, int) - Verify connection pretesting.
     Verify that new connection gets created if a pooled connection fails a pretest.
     Verify that the pool cleanup process removes connection that has failed a pretest.
    **/
    public void Var266()
    {
        AS400ConnectionPool p = null;
        try
        {
            p = new AS400ConnectionPool();
            JDReflectionUtil.callMethod_V(p, "setPretestConnections", true); 
            /* p.setPretestConnections(true); */ 
            if (!JDReflectionUtil.callMethod_B(p, "isPretestConnections")) {
              failed ("setPretestConnections(true) failed to set property.");
              return;
            }
            int service = AS400.FILE;
            p.setCleanupInterval(1000);  // run cleanup every 900 milliseconds

              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);

            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                      passwordChars, service);
            o.connectService(service); // We need at least 1 service connected, in order to pass the "pretest" on the next getConnection().
            p.returnConnectionToPool(o);

            // Verify that the connected connection passes the pretest and can be re-allocated.
            o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                      passwordChars, service);

            if (!(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1))
            {
              failed ("[A] Incorrect connection counts: " +
                      p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) + ", " +
                      p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()));
              return;
            }
            p.returnConnectionToPool(o);
            if (!(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0))
            {
              failed ("[B] Incorrect connection counts: " +
                      p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) + ", " +
                      p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()));
              return;
            }


            Job[] jobs = o.getJobs(service);
            if (jobs.length == 0) {
              failed ("Unable to obtain job information.");
              return;
            }
            Job job = new Job(pwrSys_, jobs[0].getName(), jobs[0].getUser(), jobs[0].getNumber());
            job.end(0);
            Thread.sleep(100);  // wait for job to end
            p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
                            passwordChars, service);

            // Since the returned connection will fail the pretest, the pool manager should create a new connection in response to the second getConnection().
	    int availableConnectionCount = p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId());
            if (!(availableConnectionCount == 1 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1))
            {
	      // If the maintenance process runs, then the avaiableConnectionCount will be zero.
	      // Just print a warning.
		if (availableConnectionCount == 0) {
		    System.out.println("Warning.  Lost the race and availableConnectionCount is zero");
		} else { 
		    failed ("[C] Incorrect connection counts (sb 1,1): " +
			    p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) + ", " +
			    p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()));
		    return;
		}
            }

            // Allow the maintenance process to run.
            Thread.sleep(1200);
            // The maintenance process should have removed the failed connection from the pool by now.
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1);
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
        finally
        {
          if (p != null) try { p.close(); } catch (Throwable t) { t.printStackTrace(); }
        }
    }


    /**
  Check removeFromPool(AS400) throws exception with null system.
  **/
    public void Var267()
    {
              char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
       AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
             p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.removeFromPool((AS400)null);
            failed("Did not throw exception.");
         }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
            p.close();
        }
          PasswordVault.clearPassword(passwordChars);
  } 

    /**
  removeFromPool(AS400) should complete successfully if no connections in the pool.
  **/
    public void Var268()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 o = new AS400(systemName_, userId_, passwordChars);
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            cmd.run();
            p.removeFromPool(o);
            assertCondition(p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 0);
            p.close();
        PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
removeFromPool(AS400)-should complete successfully with one active connection in the pool.
**/
    public void Var269()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 o = p.getConnection(systemName_, userId_, passwordChars);
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            boolean check = cmd.run();
            p.removeFromPool(o);
            assertCondition(check &&
                            p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
  removeFromPool(AS400)-should complete successfully with one returned connection in the pool.
  **/
    public void Var270()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 o = p.getConnection(systemName_, userId_, passwordChars);
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            boolean check1 = cmd.run();
            p.returnConnectionToPool(o);
            o = p.getConnection(systemName_, userId_, passwordChars);
            cmd = new CommandCall(o, "QSYS/CHGJOB INQMSGRPY(*SYSRPYL)");
	    cmd.run(); 
            cmd = new CommandCall(o, "QSYS/DLTLIB FRED");
            boolean check2 = cmd.run();
            p.removeFromPool(o);
            assertCondition(check1 && check2 &&
                            p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 0);
         PasswordVault.clearPassword(passwordChars);
           p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
    removeFromPool(AS400)-should complete successfully with multiple connections in the pool.
    **/
    public void Var271()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            p.fill(systemName_, userId_, passwordChars, service, 4); 
            AS400 o = p.getConnection(systemName_, userId_, passwordChars);   //@B2C
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            boolean check = cmd.run();
            p.removeFromPool(o);
            assertCondition(check &&
                            p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 3);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
  removeFromPool(AS400)-should complete without exception with connections in the pool even if the 
  system you specify is not in pool.
  **/
    public void Var272()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            int service = AS400.COMMAND;
               char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           p.fill(systemName_, userId_, passwordChars, service, 4); 
              AS400 o = new AS400(systemName_, userId_, passwordChars);
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            boolean check = cmd.run();
            p.removeFromPool(o);
            assertCondition(check &&
                            p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 4,
			    " check="+check+
			    " activeConnectionCount="+p.getActiveConnectionCount(systemName_, userId_)+" sb 0"+
			    " availableConnectionCount="+p.getAvailableConnectionCount(systemName_, userId_)+" sb 4");
        PasswordVault.clearPassword(passwordChars);
	                   
            p.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
  removeFromPool(AS400)-should complete without exception while maintenance thread is running.
  **/
    public void Var273()
    {
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
            // int service = AS400.COMMAND;
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 o = p.getConnection(systemName_, userId_, passwordChars);
            CommandCall cmd = new CommandCall(o, "QSYS/CRTLIB FRED");
            boolean check = cmd.run();
            p.setCleanupInterval(100);
            p.setRunMaintenance(true);
            p.removeFromPool(o);
            Thread.sleep(500);
            assertCondition(check &&
                            p.getActiveConnectionCount(systemName_, userId_) == 0 &&
                            p.getAvailableConnectionCount(systemName_, userId_) == 0,
			    " check="+check+
			    " activeConnectionCount="+p.getActiveConnectionCount(systemName_, userId_)+" sb 0"+
			    " availableConnectionCount="+p.getAvailableConnectionCount(systemName_, userId_)+" sb 0");
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    /**
removeFromPool(AS400) should complete without exception even with connections coming in and out of the pool.
**/
    public void Var274()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(o);
            AS400 q = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(q);
            AS400 r = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 s = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.returnConnectionToPool(r);
            p.removeFromPool(s);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    } 

    /**
Check removeFromPool(AS400) does not affect connections in use-- other connection to same system/userid can still be used.
**/
    public void Var275()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
                 char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
           AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 q = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.removeFromPool(q);
            CommandCall cmd = new CommandCall(o, "SNDMSG MSG('Hi Mom') TOUSR(" + systemObject_.getUserId() + ")");
            boolean check = cmd.run();     
            AS400Message[] messageList = cmd.getMessageList();
            p.returnConnectionToPool(o);
            assertCondition(check && (messageList.length == 0));
       PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    } 

    /**
  Check removeFromPool(AS400) does not cleanup connections for other system.
  **/
    public void Var276()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
                char[] pwrSyspasswordChars = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
            p.getConnection(systemObject_.getSystemName(), pwrSysUserID_, pwrSyspasswordChars);
            p.getConnection(systemObject_.getSystemName(), pwrSysUserID_, pwrSyspasswordChars);
            AS400 o = p.getConnection(systemObject_.getSystemName(), userId_, passwordChars);
            p.removeFromPool(o);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), pwrSysUserID_) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), pwrSysUserID_) == 2 &&
                            p.getAvailableConnectionCount(systemObject_.getSystemName(), userId_) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), userId_) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        PasswordVault.clearPassword(pwrSyspasswordChars);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
   Make sure removeFromPool(AS400) can be called multiple times without exception with both active and available
   connections in the pool.
   **/
    public void Var277()
    {
        AS400ConnectionPool p = new AS400ConnectionPool();
        try
        {
                   char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
         AS400 o = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            AS400 q = p.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            p.removeFromPool(q);
            p.removeFromPool(q);
            p.removeFromPool(q);
            assertCondition(p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 &&
                            p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 1, 
                            "o="+o);
         PasswordVault.clearPassword(passwordChars);
            p.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
    returnConnectionToPool(AS400) - Removing a connection that has already been returned should
    succeed, and not interfere with other users of pool.
    **/
    public void Var278()
    {
        try
        {
	    char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            AS400ConnectionPool p = new AS400ConnectionPool();
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                          systemObject_.getUserId(), passwordChars);
            //@A5A Make sure connection gets used and system name resolved to localhost when running on 400. 
            CommandCall cmd = new CommandCall(conn1, "QSYS/CRTLIB FRED");
            boolean check1 = cmd.run();  //@A5A
            p.returnConnectionToPool(conn1);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
                                          systemObject_.getUserId(), passwordChars);
            p.removeFromPool(conn1);
            Thread.sleep(500);  // give it a chance to get disconnected
            cmd = new CommandCall(conn2, "QSYS/CHGJOB INQMSGRPY(*SYSRPYL)");
	    cmd.run(); 
            cmd = new CommandCall(conn2, "QSYS/DLTLIB FRED");
            boolean check2 = cmd.run(); // this should work, since the AS400 object will automatically reconnect services
            p.returnConnectionToPool(conn2);
            assertCondition(check1 && check2 &&
                            p.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0 
                            && p.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally {
          try { DLTLIB_FRED.run(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


    public void Var279()
    {
    	int MILLISECONDS_TO_TEST = 60000;  
        int SLEEP_MILLISECONDS = 1; 
        int REFRESH_SECONDS=1;
        int EXPIRATION_SECONDS = 6; 
        int connectionCount = 0; 
        AS400 system = null; 
        try
        {
            AS400ConnectionPool p = new AS400ConnectionPool();
                char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
            system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId() , passwordChars);
             char[] swapToPasswordChars =  PasswordVault.decryptPassword(encryptedSwapToPassword_);
      	    ProfileTokenCredential pt1 = system.getProfileToken(swapToUserID_, swapToPasswordChars, 
      	    		ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, EXPIRATION_SECONDS);
	    PasswordVault.clearPassword(swapToPasswordChars); 
      	    pt1.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
      	    
      	    pt1.startAutomaticRefresh(REFRESH_SECONDS, -1);
      	    int service = AS400.FILE;
            int numOfConnections = 1;
            p.fill(systemObject_.getSystemName(), swapToUserID_, pt1, 
                   service, numOfConnections);
            AS400 conn1 = p.getConnection(systemObject_.getSystemName(), 
                                        swapToUserID_, pt1, service);
            AS400 conn2 = p.getConnection(systemObject_.getSystemName(), 
        			swapToUserID_, pt1, service);
            long endTime = System.currentTimeMillis() + MILLISECONDS_TO_TEST;
            while (System.currentTimeMillis() < endTime) { 
            	if (SLEEP_MILLISECONDS > 0) { 
            	  Thread.sleep(SLEEP_MILLISECONDS);
            	}
            	conn2.disconnectAllServices();  
            	try { 
            	  conn2 = p.getConnection(systemObject_.getSystemName(), 
            			swapToUserID_, pt1, service);
            	  connectionCount++;
            	} catch (ConnectionPoolException e) {
            		// If we try to connect too quickly, we get an "Address already in use"
            		// exception.  Just ignore this and continue. 
            		if (e.toString().indexOf("Address already in use")>= 0) {
            			// skip
            		} else {
            			throw e; 
            		}
            	}
            }
            System.out.println("INFO:  Test established "+connectionCount+" connections in "+
            		(MILLISECONDS_TO_TEST/1000) +" seconds"); 
            assertCondition(conn1.isConnected(service) && conn2.isConnected(service) && 
            		p.getAvailableConnectionCount(systemObject_.getSystemName(), swapToUserID_) == 0);
            p.close();
         PasswordVault.clearPassword(passwordChars);
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception connectionCount="+connectionCount+"  Note:  on 8/10/2011 we hit Profile token or identity token is not valid, which would not reproduce.");
        }
        finally {
          if (system != null) system.close(); 

        }

    }


    public void Var280()
    {

	try { 
	//
	//  Using this  only works if running on an IBM i
	//
	String osName = System.getProperty("os.name");
	if (osName.indexOf("400") < 0) {
	    notApplicable("Testcase only runs on AS/400 not "+osName);
	    return; 
	} 

    	DefaultProfileTokenProvider tokenProvider = new DefaultProfileTokenProvider();
	tokenProvider.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
	tokenProvider.setTimeoutInterval(3600);
	tokenProvider.setUserId(userId_);
        char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);

	tokenProvider.setPassword(passwordChars);

	AS400 i5 = new AS400(systemObject_.getSystemName(), tokenProvider);
	String c1 = "QSYS/CRTLIB PIGM123";
        String c2 = "QSYS/DLTLIB PIGM123";
	CommandCall cmd = new CommandCall(i5);


	boolean result = false;
	try {
	    result = cmd.run(c1);
	} catch (Exception e) {
			// TODO Auto-generated catch block
	    e.printStackTrace();
	}

	boolean result2 = false; 
	try {
	  cmd.run("QSYS/CHGJOB INQMSGRPY(*SYSRPYL)"); 
	    result2 = cmd.run(c2);
	} catch (Exception e) {
			// TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// Cannot clear password yet because of bug
	// TODO:  Move earlier
	PasswordVault.clearPassword(passwordChars);

	assertCondition(result && result2, "result="+result+" result2="+result2+" userid="+systemObject_.getUserId()); 

	}
	catch (Exception e)
	{
	    failed (e, "Unexpected Exception ");
	}

    }



    public void Var281() {
	if (checkNotGroupTest()) {
	    try { 
	//
	//  Using PW_NOPWDCHK only works if running on an IBM i
	//  It does not work when using native optimizations (jt400native.jar fails with CPF4AA5)
        //  This is because the native API ( QsyGenPrfTkn ) does not allow the user to generate
        //  their own tokens -- see https://www.ibm.com/docs/en/i/7.4?topic=ssw_ibm_i_74/apis/qsygenprftkn.htm
	//
	    String osName = System.getProperty("os.name");
	    if (osName.indexOf("400") < 0) {
		notApplicable("Testcase only runs on AS/400 not "+osName);
		return; 
	    }
	     
	    if (isNative_ ) {
	       notApplicable("Testcase does not work with jt400native.jar when current user id is the userid for the AS400 object -- fails with CPF4AA5");
	       return; 
	    }
            
	    DefaultProfileTokenProvider tokenProvider = new DefaultProfileTokenProvider();
	    tokenProvider.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
	    tokenProvider.setTimeoutInterval(3600);
	    tokenProvider.setUserId(systemObject_.getUserId());
	    tokenProvider.setPasswordSpecialValue(ProfileTokenCredential.PW_NOPWDCHK);
	    AS400 i5 = new AS400(systemObject_.getSystemName(), tokenProvider);
	    String c1 = "QSYS/CRTLIB PIGM123";
	    String c2 = "QSYS/DLTLIB PIGM123";
	    CommandCall cmd = new CommandCall(i5);
	    boolean result = false;
	    try {
		result = cmd.run(c1);
	    } catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    boolean result2 = false; 
	    try {
		cmd.run("QSYS/CHGJOB INQMSGRPY(*SYSRPYL)"); 
		result2 = cmd.run(c2);
	    } catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
	    } 
	    assertCondition(result && result2, "Failure using PW_NOPWDCHK result="+result+" result2="+result2+" userid="+systemObject_.getUserId()); 

}
	catch (Exception e)
	{
	    failed (e, "Unexpected Exception ");
	}
	}
    }
}


class ExampleListener implements ConnectionPoolListener
{
    private int poolClosed_ = 0;
    private int connectionCreated_ = 0;
    private int connectionExpired_ = 0;
    private int connectionReleased_ = 0;
    private int connectionReturned_ = 0;
    private int maintenanceThreadRun_ = 0;

    private Object source_ = null; //@A7A

    public ExampleListener()
    {   
    }

    public void connectionPoolClosed(ConnectionPoolEvent event)
    {
        poolClosed_++;
        source_ = event.getSource();
    }
    public void connectionCreated(ConnectionPoolEvent event)
    {
        connectionCreated_++;
        source_ = event.getSource();
    }
    public void connectionExpired(ConnectionPoolEvent event)
    {
        connectionExpired_++;
        source_ = event.getSource();
    }
    public void connectionReleased(ConnectionPoolEvent event)
    {
        connectionReleased_++;
        source_ = event.getSource();
    }
    public void connectionReturned(ConnectionPoolEvent event)
    {
        connectionReturned_++;
        source_ = event.getSource();
    }
    public void maintenanceThreadRun(ConnectionPoolEvent event)
    {
        maintenanceThreadRun_++;
        source_ = event.getSource();
    }
    public long getClosed()
    {
        return poolClosed_;
    }
    public long getCreated()
    {
        return connectionCreated_;
    }
    public long getExpired()
    {
        return connectionExpired_;
    }
    public long getReleased()
    {
        return connectionReleased_;
    }
    public long getReturned()
    {
        return connectionReturned_;
    }
    public long getMaintenance()
    {
        return maintenanceThreadRun_;
    }
    public Object getSource() //@A7A
    {
        return source_; //@A7A
    }
}
