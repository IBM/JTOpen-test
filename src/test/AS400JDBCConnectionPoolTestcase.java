
///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCConnectionPoolTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;
import java.sql.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnectionPool;
import com.ibm.as400.access.AS400JDBCConnectionPoolDataSource;
import com.ibm.as400.access.AS400JDBCPooledConnection; //@A3A
import com.ibm.as400.access.ConnectionPoolEvent;
import com.ibm.as400.access.ConnectionPoolException;
import com.ibm.as400.access.ConnectionPoolListener;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.Trace;

/**
  Testcase AS400JDBCConnectionPoolTestcase.
 **/
public class AS400JDBCConnectionPoolTestcase extends Testcase
{
    private static final int PROBABILITY = 6;
    private static final int AGE = 1;
    // private String tag = null;
    // private boolean rejectChange = false;
    private String traceFileName_ = "/tmp/pool."+System.currentTimeMillis()+".log";
    private File testFile_;  
    // private long previousFileEnd_;
    private java.io.OutputStream os_ = null;   //@A2A
    // private PrintWriter originalPrintWriter_;

    private AS400JDBCConnectionPoolDataSource dataSource_;

    /**
      Constructor.  This is called from the AS400JDBCConnectionPoolTest constructor.
     **/
    public AS400JDBCConnectionPoolTestcase(AS400 systemObject,
                                           Vector variationsToRun,
                                           int runMode,
                                           FileOutputStream fileOutputStream,
                                           
                                           String password)
    {
        super(systemObject, "AS400JDBCConnectionPoolTestcase", variationsToRun,
              runMode, fileOutputStream, password);
        // originalPrintWriter_ = Trace.getPrintWriter();
    }


    public void setup()
    {
        try
        {
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
            dataSource_ = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
            PasswordVault.clearPassword(passwordChars);

            if (!isApplet_)
            {                           //@A2A
                File dummyFile = new File(traceFileName_);        //@A2A
                if (dummyFile.exists())               //@A2A
                {                                                 //@A2A
                    dummyFile.delete();                             //@A2A
                }                                                 //@A2A
                testFile_ = new File(traceFileName_);
                os_ = new FileOutputStream (testFile_);       //@A2A
            }                           //@A2A
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Setup error during Trace file creation.");
        }
    }

    /**
    *  Checks the status of the connections in the pool.
    **/
    private int checkConnections(AS400JDBCConnectionPool pool, long timeIntervalInMillis, int numberOfChecks )
    {
        MyPoolListener listener = new MyPoolListener();
        pool.addConnectionPoolListener(listener);

        System.out.println("   Testcase wait... (" + (numberOfChecks * timeIntervalInMillis) + " milliseconds)");

        int count = 0;
        for (int i=0; i< numberOfChecks; i++)
        {
            try
            {
                Date time = new Date();
                System.out.println("   Testcase wait...(" + timeIntervalInMillis + " milliseconds)");
                Thread.sleep(timeIntervalInMillis);

                if (listener.lastMaintenance() > time.getTime())
                    count++;
            }
            catch (InterruptedException inter) {
            }
        }
        return count;
    }

    /**
    *  Checks the trace log for the compare string.
    *  @param compare The compare string.
    **/
    private boolean checkLog(String compare)
    {
        boolean equal = false;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(traceFileName_));

            String line = reader.readLine();
            while (line != null)
            {
                if (line.indexOf(compare) != -1)
                {
                    equal = true;
                    break;
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch (Exception e)
        {
            System.out.println("Log check failed.");
            e.printStackTrace();
        }
        return equal;
    }    

    /**
    *  Displays the contents of the trace log.
    **/
    private void displayLog()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(traceFileName_));

            String line = reader.readLine();
            while (line != null)
            {
              System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        }
        catch (Exception e)
        {
            System.out.println("Log display failed.");
            e.printStackTrace();
        }
    }    

    /**
     @exception  Exception  If an exception occurs.
     **/
    public void cleanup() throws Exception
    {  
        try
        {
          if (isApplet_)                  //@A2A
            Trace.setPrintWriter(null);               //@A2A
          //else                    //@A2A
          //  Trace.setFileName(null);  // This seems to send further output into nowhere.
          if (!testFile_.delete())
            System.out.println("WARNING... Testcase cleanup failed to delete: " + traceFileName_);
        }
        catch (Throwable e) { e.printStackTrace(); }
    }

    /**
    *  Validates that the connection pool creates the specified number of initial connections.
    **/
    public void Var001()
    {
        int initialConnections = 10;
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(dataSource_);

            pool.setMaxConnections(25);           // Max connections.
            pool.setCleanupInterval(1000*60*3);   // 3min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*10);    // 10min Max time in pool.
            pool.setMaxLifetime(1000*60*60*24);   // 24hr  Max lifetime since created.
            pool.setMaxUseTime(-1);               // unlimited Max time can be active.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(initialConnections);

            assertCondition (pool.getAvailableConnectionCount() == initialConnections && pool.getActiveConnectionCount() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    *  Validates that a connection from the pool is usable.
    **/
    public void Var002()
    {
        int initialConnections = 5;

        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
          AS400JDBCConnectionPoolDataSource ds =          new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	  PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxConnections(25);           // Max connections.
            pool.setCleanupInterval(1000*60*3);   // 3min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*10);    // 10min Max time in pool.
            pool.setMaxLifetime(1000*60*60*24);   // 24hr  Max lifetime since created.
            pool.setMaxUseTime(-1);               // unlimited Max time can be active.
            pool.setMaxUseCount(3);               // Times connection can be reused.
            JDReflectionUtil.callMethod_V(pool, "setPretestConnections", true);     // Pretest connections.

            pool.fill(initialConnections);

            Connection c1 = pool.getConnection();
            if (c1 != null)
            {
                Statement s1 = c1.createStatement();
                ResultSet r1 = s1.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                while (r1.next()) {
                }
                succeeded();
            }
            else
            {
                failed("Unexpected Results.  Connection is null.");
            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that connections from the pool are usable.
    **/
    public void Var003()
    {
        int initialConnections = 5;

        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxConnections(25);           // Max connections.
            pool.setCleanupInterval(1000*60*3);   // 3min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*10);    // 10min Max time in pool.
            pool.setMaxLifetime(1000*60*60*24);   // 24hr  Max lifetime since created.
            pool.setMaxUseTime(-1);               // unlimited Max time can be active.
            pool.setMaxUseCount(3);               // Times connection can be reused.
            JDReflectionUtil.callMethod_V(pool, "setPretestConnections", true);     // Pretest connections.

            pool.fill(initialConnections);

            Connection c1 = pool.getConnection();
            Connection c2 = pool.getConnection();
            if (c1 != null && c2 != null)
            {
                Statement s1 = c1.createStatement();
                ResultSet r1 = s1.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

                Statement s2 = c2.createStatement();
                ResultSet r2 = s2.executeQuery("SELECT * FROM QIWS.QCUSTCDT WHERE CDTLMT > 1000");

                if (r1 != r2)
                    succeeded();
                else
                    failed("Unexpected results.  ResultSets are the same.");
            }
            else
            {
                failed("Unexpected Results.  Connection is null.");
            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that the maintenance is run as expected.
    **/
    public void Var004()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }

      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 4, PROBABILITY, AGE)) {
        return; 
      }
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.
            JDReflectionUtil.callMethod_V(pool, "setPretestConnections", true);     // Pretest connections.
            pool.setRunMaintenance(true);

            pool.fill(5);

            Connection c1 = pool.getConnection();
            Connection c2 = pool.getConnection();

            int num = 5;
            int count = checkConnections(pool, 1000*80, num);          // 1:20 check

            finishLongRunning("AS400JDBCConnectionPoolTestcase", 4, count == num );
            assertCondition(count == num, "c1="+c1+" c2="+c2);
        }
        catch (Exception e)
        {
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 4, false );
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that fill(int) throws an ExtendedIllegalArgumentException if the number of connections
    *  is greater than the maximum connections.
    **/
    public void Var005()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxConnections(3);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*10);    // 10min Max time in pool.
            pool.setMaxLifetime(1000*60*60*24);   // 24hr  Max lifetime since created.
            pool.setMaxUseTime(1000*60*5);        // 5min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(5);

            failed("Unexpected Results.");
        }
        catch (ConnectionPoolException il)     //@B1C
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that AS400JDBCConnectionPool(AS400JDBCDataSource) throws an NullPointerException
    *  if the dataSource is null.
    **/
    public void Var006()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(null);
            failed("Unexpected Results."+pool);
        }
        catch (NullPointerException n)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setDataSource(AS400JDBCDataSource) throws an NullPointerException
    *  if the dataSource is null.
    **/
    public void Var007()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            pool.setDataSource(null);

            failed("Unexpected Results.");
        }
        catch (NullPointerException n)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getDataSource() returns the expected dataSource.
    **/
    public void Var008()
    {
        // int initialConnections = 5;

        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);

            if (pool.getDataSource() == ds)
                succeeded();
            else
                failed("Unexpected results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getDataSource() returns the expected dataSource.
    **/
    public void Var009()
    {
        // int initialConnections = 5;

        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            pool.setDataSource(ds);

            if (pool.getDataSource() == ds)
                succeeded();
            else
                failed("Unexpected results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setDataSource(AS400JDBCDataSource) allows the dataSource to be reset
    *  if a pooled connection has not been created via fill(int) or getConnection().
    **/
    public void Var010()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource());

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool.setDataSource(ds);

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setDataSource(AS400JDBCDataSource) throws an ExtendedIllegalStateException  
    *  if a connection already exists via fill(int).
    **/
    public void Var011()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(dataSource_);
            pool.fill(2);

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool.setDataSource(ds);
            failed("Unexpected Results.");
        }
        catch (ExtendedIllegalStateException st)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setDataSource(AS400JDBCDataSource) throws an ExtendedIllegalStateException  
    *  if a connection already exists via getConnection().
    **/
    public void Var012()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(dataSource_);
            Connection c = pool.getConnection();

            pool.setDataSource(new AS400JDBCConnectionPoolDataSource());

            failed("Unexpected Results."+c);
        }
        catch (ExtendedIllegalStateException st)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that fill(int) throws an ExtendedIllegalStateException if the dataSource
    *  has not been set.
    **/
    public void Var013()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();

            pool.fill(10);

            failed("Unexpected Results.");
        }
        catch (ExtendedIllegalStateException st)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that fill(int) throws an ExtendedIllegalArgumentException if the numberOfConnections
    *  to add plus the current number of connections is greater than the maximum connections.
    **/
    public void Var014()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.setMaxConnections(10);
            pool.fill(7);

            Connection c = pool.getConnection();

            pool.fill(5);

            failed("Unexpected Results."+c);
        }
        catch (ConnectionPoolException arg) //@B1C
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }


    /**
    *  Validates that close() on a connection obtained from a PooledConnection will return the PooledConnection
    *  to the pool for reuse.
    **/
    public void Var015()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.fill(7);

            int active=2;
            Connection[] connections = new Connection[active];
            Statement s = null; 
            for (int i=0; i< active; i++)
            {
                connections[i] = pool.getConnection();
                s = connections[i].createStatement();
            }

            int activeBefore = pool.getActiveConnectionCount();
            int idleBefore = pool.getAvailableConnectionCount();

            connections[0].close();        // return.

            assertCondition( pool.getActiveConnectionCount() == (activeBefore-1) && 
                             pool.getAvailableConnectionCount() == (idleBefore+1), "statement is "+s); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }


    /**
    *  Validates that close() on a connection obtained from a PooledConnection will return the PooledConnection
    *  to the pool for reuse.
    **/
    public void Var016()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);

            int active=2;
            Connection[] connections = new Connection[active];
            Statement s = null; 
            for (int i=0; i< active; i++)
            {
                connections[i] = pool.getConnection();
                s = connections[i].createStatement();
            }

            int activeBefore = pool.getActiveConnectionCount();
            int idleBefore = pool.getAvailableConnectionCount();

            connections[0].close();        // return.

            assertCondition( pool.getActiveConnectionCount() == (activeBefore-1) && 
                             pool.getAvailableConnectionCount() == (idleBefore+1), "statement="+s); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setProperties works as expected after a pool is created and filled.
    **/
    public void Var017()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 17, PROBABILITY, AGE)) {
        return; 
      }

        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.setMaxConnections(25);           // Max connections.
            pool.setCleanupInterval(1000*60*3);   // 3min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*10);    // 10min Max time in pool.
            pool.setMaxLifetime(1000*60*60*24);   // 24hr  Max lifetime since created.
            pool.setMaxUseTime(-1);               // unlimited Max time can be active.
            pool.setMaxUseCount(3);               // Times connection can be reused.
            pool.setRunMaintenance(true);

            pool.fill(10);
            Connection[] c = new Connection[2];
            for (int i=0; i< c.length; i++)
            {
                c[i] = pool.getConnection();
            }

            System.out.println("   Testcase wait... (5 minutes)");
            Thread.sleep(1000*60*5);

            pool.setCleanupInterval(1000*60*1);   // 1min  Time interval daemon runs.
            pool.setMaxLifetime(1000*60*30);      // 30min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*5);        // 5min Max usage limit.
            pool.setMaxUseCount(1);               // 1 Time  connection can be reused.

            System.out.println("   Testcase wait... (5 minutes)");
            Thread.sleep(1000*60*5);

            finishLongRunning("AS400JDBCConnectionPoolTestcase", 17, true );

            succeeded();
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 17, false );

            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setProperties works as expected after fill/getConnection when the new max
    *  connections > the current number of total connections.
    **/
    public void Var018()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 

            pool.setMaxConnections(25);           // Max connections.
            pool.setCleanupInterval(1000*60*3);   // 3min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*10);    // 10min Max time in pool.
            pool.setMaxLifetime(1000*60*60*24);   // 24hr  Max lifetime since created.
            pool.setMaxUseTime(-1);               // unlimited Max time can be active.
            pool.setMaxUseCount(3);               // Times connection can be reused.
            pool.setRunMaintenance(true);

            pool.fill(15);
            Connection[] c = new Connection[5];
            for (int i=0; i< c.length; i++)
            {
                c[i] = pool.getConnection();
            }

            pool.setMaxConnections(10);            // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1min  Time interval daemon runs.
            pool.setMaxLifetime(1000*60*30);      // 30min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*5);        // 5min Max usage limit.
            pool.setMaxUseCount(1);               // 1 Time  connection can be reused.

            assertCondition( (pool.getActiveConnectionCount() + pool.getAvailableConnectionCount()) == pool.getMaxConnections());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that setProperties works as expected after fill/getConnection when the new max
    *  connections > the current number of active connections.
    **/
    public void Var019()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 

            pool.setMaxConnections(25);           // Max connections.
            pool.setCleanupInterval(1000*60*3);   // 3min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*10);    // 10min Max time in pool.
            pool.setMaxLifetime(1000*60*60*24);   // 24hr  Max lifetime since created.
            pool.setMaxUseTime(-1);               // unlimited Max time can be active.
            pool.setMaxUseCount(3);               // Times connection can be reused.
            pool.setRunMaintenance(true);

            pool.fill(15);

            Connection[] c = new Connection[10];
            for (int i=0; i< c.length; i++)
            {
                c[i] = pool.getConnection();
            }

            pool.setMaxConnections(8);            // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1min  Time interval daemon runs.
            pool.setMaxLifetime(1000*60*30);      // 30min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*5);        // 5min Max usage limit.
            pool.setMaxUseCount(1);               // 1 Time  connection can be reused.

            c[0].close();

            assertCondition( pool.getAvailableConnectionCount() == 0 && pool.getActiveConnectionCount() > pool.getMaxConnections());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that the pool can be closed and then re-filled and used.
    **/
    public void Var020()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.setRunMaintenance(true);

            pool.fill(15);

            Connection[] c = new Connection[8];
            for (int i=0; i< c.length; i++)
            {
                c[i] = pool.getConnection();
            }
            pool.close();

            pool.fill(5);

            Connection connection = pool.getConnection();
            connection.close();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that setRunMaintenance(false) works as expected if the pool properties are set.
    **/
    public void Var021()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 21, PROBABILITY, AGE)) {
        return; 
      }

        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 

            pool.setMaxConnections(6);            // Max connections.
            pool.setCleanupInterval(1000*65);     // 65sec  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*1);     // 1 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*5);        // 5min Max usage limit.
            pool.setMaxUseCount(1);               // 1 Time  connection can be reused.

            pool.setRunMaintenance(false);

            int initial = 6;
            pool.fill(initial);

            Connection[] c = new Connection[3];
            for (int i=0; i< c.length; i++)
            {
                c[i] = pool.getConnection();
            }

            System.out.println("   Testcase wait... (2 minutes)");
            Thread.sleep(1000*60*2);

            if (pool.getActiveConnectionCount() + pool.getAvailableConnectionCount() == initial) {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 21, true );

                succeeded();
            } else { 
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 21, false );
                failed("Unexpected results.");
            }
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 21, false );

            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates addPropertyChangeListener(null) throws a NullPointerException.
    **/
    public void Var022()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            pool.addPropertyChangeListener(null);

            failed("Unexpected results.");
        }
        catch (NullPointerException np)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception occurred.");
        }
    }

    /**
    *  Validates that a ConnectionPoolEvent.CONNECTION_EXPIRED event occurs as expected.
    **/
    public void Var023()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 23, PROBABILITY, AGE)) {
        return; 
      }

        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*40);         // 40 sec  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(2);

            Connection c1 = pool.getConnection();

            System.out.println("   Testcase wait... (1 minutes)");
            Thread.sleep(60000);    // wait 1 minute.

            c1.close();
            pool.close();

            if (poolListener.lastExpired() != 0) {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 23, true );
              succeeded();
            } else {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 23, false );
                failed("Unexpected Results.");
            }
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 23, false );

            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates removePropertyChangeListener(null) throws a NullPointerException.
    **/
    public void Var024()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            pool.removePropertyChangeListener(null);

            failed("Unexpected results.");
        }
        catch (NullPointerException np)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception occurred.");
        }
    }
    // Validates removePropertyChangeListener(PropertyChangeListener) removes the listener as expected.   
    public void Var025()
    {
        try
        {
            MyPropertyChangeListener listener = new MyPropertyChangeListener();

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.addPropertyChangeListener(listener);

            pool.setMaxConnections(10);
            boolean before = listener.isChanged();

            pool.removePropertyChangeListener(listener);

            pool.setMaxConnections(15);
            assertCondition(before && !listener.isChanged());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that close can be called with the empty pool.
    **/
    public void Var026()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            pool.close();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception occurred.");
        }
    }

    /**
    *  Validates that setDataSource(AS400JDBCDataSource) works as expected after a pool has been filled and closed.
    **/
    public void Var027()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.fill(2);

            Connection c = pool.getConnection();
            pool.close();

          passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool.setDataSource(ds);

            assertCondition(pool.getDataSource() == ds, "c= "+c);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that an active pooledConnection is invalid after the pool is closed.
    **/
    public void Var028()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.fill(2);

            Connection c = pool.getConnection();
            pool.close();

            Statement s = c.createStatement();

            failed("Unexpected Results."+s);
        }
        catch (SQLException ext)      //@A1C
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that the active and available connection count is zero after the close.
    **/
    public void Var029()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.fill(8);
            Connection c = pool.getConnection();

            pool.close();

            assertCondition(pool.getActiveConnectionCount() == 0 && pool.getAvailableConnectionCount() == 0, "c="+c);
        }
        catch (ExtendedIllegalArgumentException arg)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that fill(int) throws an ExtendedIllegalArgumentException if the parameter is less than one.
    **/
    public void Var030()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.fill(-1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalArgumentException arg)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that fill(int) fills the expected numberOfConnections in the pool.
    **/
    public void Var031()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            int expected = 10;
            pool.fill(expected);

            assertCondition(pool.getAvailableConnectionCount() == expected);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that fill(int) restarts the maintenance daemon if the pool had been reduced to zero connections.
    **/
    public void Var032()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 32, PROBABILITY, AGE)) {
        return; 
      }

        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.setCleanupInterval(1000*60);
            pool.setMaxUseCount(1);
            pool.setMaxInactivity(1000*30);
            pool.setRunMaintenance(true);

            int expected = 2;
            pool.fill(expected);

            Connection[] c = new Connection[expected];
            for (int i=0; i< c.length; i++)
            {
                c[i] = pool.getConnection();
                c[i].close();
            }

            if (pool.getAvailableConnectionCount() == 0 && pool.getActiveConnectionCount() == 0)
            {
                pool.fill(2);
                int before = pool.getAvailableConnectionCount();

                System.out.println("   Testcase wait... (2 minutes)");
                Thread.sleep(1000*120);

                boolean passed = pool.getAvailableConnectionCount() == 0 && before == 2;
                finishLongRunning("AS400JDBCConnectionPoolTestcase", 32, passed );

                assertCondition(passed);
            }
            else {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 32, false );

                failed("Unexpected results.  Testcase setup failure.");
            }
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 32, false );

            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that fill(int) throws an ExtendedIllegalStateException if the data source parameter is not set.
    **/
    public void Var033()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            pool.fill(10);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException a)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getActiveConnectionCount() initially returns zero.
    **/
    public void Var034()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            assertCondition(pool.getActiveConnectionCount() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception occurred.");
        }
    }
    /**
    *  Validates that getActiveConnectionCount() returns the expected value.
    **/
    public void Var035()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 

            pool.fill(10);

            int active = 4;
            Connection[] c = new Connection[active];
            for (int i=0; i< c.length; i++)
            {
                c[i] = pool.getConnection();
            }
            assertCondition(pool.getActiveConnectionCount() == active); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that getAvailableConnectionCount() initially returns zero.
    **/
    public void Var036()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            assertCondition(pool.getAvailableConnectionCount() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception occurred.");
        }
    }

    /**
    *  Validates that getAvailableConnectionCount() returns the expected value.
    **/
    public void Var037()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 

            int initial = 23;
            pool.fill(initial);

            int active = 11;
            Connection[] c = new Connection[active];
            for (int i=0; i< c.length; i++)
            {
                c[i] = pool.getConnection();
            }
            assertCondition(pool.getAvailableConnectionCount() == (initial-active) );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that getConnection returns a valid connection when the pool is empty.
    **/
    public void Var038()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 

            Connection c = pool.getConnection();

            assertCondition(pool.getActiveConnectionCount() == 1, "c="+c);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that getConnection returns a available connection from the pool.
    **/
    public void Var039()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.fill(3);

            int before = pool.getAvailableConnectionCount();

            Connection c = pool.getConnection();

            assertCondition(pool.getAvailableConnectionCount() == (before-1), "c="+c );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that getConnection creates and returns a valid connection when all connections are active.
    **/
    public void Var040()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 

            int count = 3;
            pool.fill(count);
            Connection[] c = new Connection[count];
            for (int i=0; i< count; i++)
                c[i] = pool.getConnection();

            int before = pool.getAvailableConnectionCount();

            Connection connection = pool.getConnection();

            assertCondition(before == 0 &&
                            pool.getAvailableConnectionCount() == 0 &&
                            pool.getActiveConnectionCount() == count+1, "c="+connection);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that getConnection throws an ExtendedIllegalStateException after the max connection limit is reached.
    **/
    public void Var041()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
            pool.setMaxConnections(5);

            int count = 5;
            pool.fill(count);
            Connection[] c = new Connection[count];
            for (int i=0; i< count; i++)
                c[i] = pool.getConnection();

            Connection connection = pool.getConnection();

            failed("Unexpected results."+connection);
        }
        catch (ExtendedIllegalStateException ill)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that getConnection throws an ExtendedIllegalStateException if the data source parameter is not set.
    **/
    public void Var042()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();

            Connection c = pool.getConnection();

            failed("Unexpected results."+c);
        }
        catch (ExtendedIllegalStateException st)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that isRunMaintenance returns false initially.
    **/
    public void Var043()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            if (pool.isRunMaintenance() == true)
                succeeded();
            else
                failed("Unexpected results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception occurred.");
        }
    }

    /**
    *  Validates that setRunMaintenance(false) works as expected.
    **/
    public void Var044()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 

            boolean before = pool.isRunMaintenance();

            pool.setRunMaintenance(false);

            assertCondition(!pool.isRunMaintenance() && pool.isRunMaintenance() != before);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that setting new maintenance properties uses the updated values.
    **/
    public void Var045()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 45, PROBABILITY, AGE)) {
        return; 
      }

        try
        {         
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.
            pool.setRunMaintenance(true);

            pool.fill(5);

            Connection c1 = pool.getConnection();
            Connection c2 = pool.getConnection();

            checkConnections(pool, 1000*75, 1);          // 75 sec check
            long check = poolListener.lastMaintenance();
            checkConnections(pool, 1000*75, 1);          // 75 sec check

            long diff1 = poolListener.lastMaintenance() - check;

            // Use new properties.
            pool.setCleanupInterval(1000*60*3);   // 3 min  Time interval daemon runs.
            // Note: This should interrupt the maintainer's wait(), and restart the cleanup cycle timer.

            long checkpoint1 = new java.util.Date().getTime();

            checkConnections(pool, 1000*70*3, 1);  // let it run for 3:30 (30 seconds longer than the cleanup interval)
            check = poolListener.lastMaintenance();

            long diff2 = check - checkpoint1;

            //ibm jvm has some slow reactions to events that can cause this to fail (time from event firing and actual event-queue removal is slow).  adding a bit of extra time for this.
            if ( (diff1 > 45000 && diff1 < 75000) && (diff2 < 30600 && diff2 > 29000) ) { //@pdc adding 1000 miliseconds fudge-factor (diff2 < 30100 && diff2 > 29500) 
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 45, true  );

                succeeded();
            
           } else {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 45, false );
              failed("Unexpected Results: diff1 == " + diff1 + ", diff2 == " + diff2+" c1="+c1+"c2="+c2);

            }

            pool.close();
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 45, false );

            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that a ConnectionPoolEvent.CONNECTION_CLOSED event occurs as expected.
    **/
    public void Var046()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 46, PROBABILITY, AGE)) {
        return; 
      }

        try
        {         
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.
            pool.setRunMaintenance(true);

            pool.fill(5);

            Connection c1 = pool.getConnection();

            checkConnections(pool, 70000, 1);          // 70 sec wait.

            pool.close();

            if (poolListener.lastMaintenance() != 0) {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 46, true );
                succeeded();
            } else {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 46, false );
              failed("Unexpected Results."+c1);

            }
        }
        catch (Exception e)
        {
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 46, false );
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that a ConnectionPoolEvent.CONNECTION_CLOSED event occurs as expected.
    **/
    public void Var047()
    {
        try
        {         
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(5);

            Connection c1 = pool.getConnection();
            Connection c2 = pool.getConnection();

            pool.close();

            if (poolListener.lastClosed() != 0)
                succeeded();
            else
                failed("Unexpected Results."+c1+c2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that a ConnectionPoolEvent.CONNECTION_CREATED event occurs as expected.
    **/
    public void Var048()
    {
        try
        {         
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(5);

            pool.close();

            if (poolListener.lastCreated() != 0)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that a ConnectionPoolEvent.CONNECTION_RELEASED event occurs as expected.
    **/
    public void Var049()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(5);

            Connection c1 = pool.getConnection();

            pool.close();

            if (poolListener.lastReleased() != 0)
                succeeded();
            else
                failed("Unexpected Results."+c1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that a ConnectionPoolEvent.CONNECTION_RETURNED event occurs as expected.
    **/
    public void Var050()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(5);

            Connection c1 = pool.getConnection();
            c1.close();

            pool.close();

            if (poolListener.lastReturned() != 0)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that removeConnectionPoolListener(ConnectionPoolListener) works as expected.
    **/
    public void Var051()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(3);

            Connection c1 = pool.getConnection();
            long before = poolListener.lastReleased();

            pool.removeConnectionPoolListener(poolListener);
            Connection c2 = pool.getConnection();

            pool.close();

            if (before != 0 && poolListener.lastReleased() == before)
                succeeded();
            else
                failed("Unexpected Results."+c1+c2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isClosed() returns the expected default value.
    **/
    public void Var052()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
            assertCondition(pool.isClosed());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that the default serialization works correctly.
    **/
    public void Var053()
    {
        try
        {
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();

            AS400JDBCConnectionPool pool2 = serialize(pool);

            assertCondition(pool.getDataSource() == pool2.getDataSource() &&
                            pool.isClosed() == pool2.isClosed() &&
                            pool.getCleanupInterval() == pool2.getCleanupInterval() &&
                            pool.getMaxConnections() == pool2.getMaxConnections() &&
                            pool.getMaxInactivity() == pool2.getMaxInactivity() &&
                            pool.getMaxLifetime() == pool2.getMaxLifetime() &&
                            pool.getMaxUseCount() == pool2.getMaxUseCount() &&
                            pool.getMaxUseTime() == pool2.getMaxUseTime() &&
                            pool.isRunMaintenance() == pool2.isRunMaintenance() &&
                            pool.isThreadUsed() == pool2.isThreadUsed());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that the serialization works correctly.
    **/
    public void Var054()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxConnections(77);
            pool.setMaxLifetime(1000*60*60*5);
            pool.setThreadUsed(false);
            pool.fill(3);
            Connection c = pool.getConnection();

            AS400JDBCConnectionPool pool2 = serialize(pool);

            assertCondition(pool2.getDataSource() != null &&
                            !pool.isClosed() && pool2.isClosed() &&
                            pool.getActiveConnectionCount() != pool2.getActiveConnectionCount() &&
                            pool.getAvailableConnectionCount() != pool2.getAvailableConnectionCount() &&
                            pool.getCleanupInterval() == pool2.getCleanupInterval() &&
                            pool.getMaxConnections() == pool2.getMaxConnections() &&
                            pool.getMaxInactivity() == pool2.getMaxInactivity() &&
                            pool.getMaxLifetime() == pool2.getMaxLifetime() &&
                            pool.getMaxUseCount() == pool2.getMaxUseCount() &&
                            pool.getMaxUseTime() == pool2.getMaxUseTime() &&
                            pool.isRunMaintenance() == pool2.isRunMaintenance() &&
                            pool.isThreadUsed() == pool2.isThreadUsed(), "c="+c);

            pool.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that an available connection is closed and removed from the pool if the maxLifetime
    *  has been reached.
    *
    *  AS400JDBCConnectionPool()
    *  setMaxLifetime(long);
    **/
    public void Var055()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 55, PROBABILITY, AGE)) {
        return; 
      }

        AS400JDBCConnectionPool pool = null;
        try
        {
            long threshold = 1000*60;

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxLifetime(threshold);
            pool.setCleanupInterval(threshold + 5000);
            pool.setRunMaintenance(true);
            pool.fill(2);

            Connection c = pool.getConnection();

            System.out.println("   Testcase wait... (30 seconds)");
            Thread.sleep(threshold + 30000);
            boolean passed = pool.getActiveConnectionCount() == 1 &&
            pool.getAvailableConnectionCount() == 0; 
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 55, passed );

            assertCondition(passed, "c="+c);
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 55, false );

            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }

    /**
    *  Validates that an active connection is closed and returned to the pool if the maxUseTime
    *  has been reached.
    **/
    public void Var056()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 56, PROBABILITY, AGE)) {
        return; 
      }

        AS400JDBCConnectionPool pool = null;
        try
        {
            long threshold = 1000*60;

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxUseTime(threshold);
            pool.setCleanupInterval(threshold + 5000);
            pool.setRunMaintenance(true);
            pool.fill(2);

            Connection c = pool.getConnection();

            System.out.println("   Testcase wait... (30 seconds)");
            Thread.sleep(threshold + 30000);

            boolean passed = pool.getActiveConnectionCount() == 0 &&
            pool.getAvailableConnectionCount() == 2; 
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 56, passed );

            assertCondition(passed, "c="+c);
        }
        catch (Exception e)
        {
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 56, false );

            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }

    /**
    *  Validates that an active connection is notified correctly that it is being closed because
    *  it has expired.
    **/
    public void Var057()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 57, PROBABILITY, AGE)) {
        return; 
      }

        AS400JDBCConnectionPool pool = null;
        try
        {
            long threshold = 1000*60;

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxUseTime(threshold);
            pool.setCleanupInterval(threshold + 5000);
            pool.setRunMaintenance(true);
            pool.fill(2);

            Connection c = pool.getConnection();

            System.out.println("   Testcase wait... (30 seconds)");
            Thread.sleep(threshold + 30000);

            c.createStatement();
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 57, false );
            failed("Did not throw exception -- check added 8/17/2011"); 
        }
        catch (SQLException e)     //@A1C
        {
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 57, true );

            succeeded();
        }
        catch (Exception all)
        {
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 57, false );
            failed(all, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }

    /**
    *  Validates that an inactive connection is removed from the pool after it has expired.
    **/
    public void Var058()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
        
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 58, PROBABILITY, AGE)) {
        return; 
      }

        AS400JDBCConnectionPool pool = null;
        try
        {
            long threshold = 1000*60;

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxInactivity(threshold);
            pool.setCleanupInterval(threshold + 5000);
            pool.setRunMaintenance(true);
            pool.fill(2);

            int before = pool.getAvailableConnectionCount();

            System.out.println("   Testcase wait... (30 seconds)");
            Thread.sleep(threshold + 30000);
            boolean passed = pool.getAvailableConnectionCount() == 0 && before != 0; 
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 58, passed );

            assertCondition(passed);
        }
        catch (Exception all)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 58, false);
            failed(all, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }


    /**
    *  Validates that an active connection is not closed after its maximum lifetime has been 
    *  reached until the connection is returned to the pool.
    *
    *  AS400JDBCConnectionPool()
    *  setMaxLifetime(long);
    **/
    public void Var059()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 59, PROBABILITY, AGE)) {
        return; 
      }

        AS400JDBCConnectionPool pool = null;
        try
        {
            long threshold = 1000*60;

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxLifetime(threshold);
            pool.setCleanupInterval(threshold + 5000);
            pool.setRunMaintenance(true);
            pool.fill(2);

            Connection c = pool.getConnection();

            System.out.println("   Testcase wait... (30 seconds)");
            Thread.sleep(threshold + 30000);

            int before = pool.getActiveConnectionCount();
            c.close();

            System.out.println("   Testcase wait... (15 seconds)");
            Thread.sleep(15000);

            boolean passed = before == 1 && pool.getActiveConnectionCount() == 0 &&
            pool.getAvailableConnectionCount() == 0; 
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 59, passed);

            assertCondition(passed);
        }
        catch (Exception e)
        {
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 59, false);
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }

    /**
    *  Validates that the pool maintenance can be traced.
    **/
    public void Var060()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 60, PROBABILITY, AGE)) {
        return; 
      }

        if (isApplet_)           //@A2A
        {                        //@A2A
            notApplicable();      //@A2A
            return;               //@A2A
        }                        //@A2A


        //@A4D Trace.setTraceOn(true);
        //@A4D Trace.setTraceInformationOn(true);
        //@A4D Trace.setTraceDiagnosticOn(true);
        //@A4D Trace.setTraceErrorOn(true);
        Trace.setTraceJDBCOn(true);    //@A4A

        AS400JDBCConnectionPool pool = null;
        try
        {
            DriverManager.setLogWriter(new PrintWriter(os_));  //@A2A
            Trace.setPrintWriter(new PrintWriter(os_, true));              //@A2C
            // previousFileEnd_ = testFile_.length();

            long threshold = 100*60;  //@A2C

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxLifetime(threshold);
            pool.setCleanupInterval(threshold + 5000);
            pool.setRunMaintenance(true);
            pool.fill(2);

            Connection c = pool.getConnection();

            // Check after 30 seconds. If not done, give it another 30 seconds.
            boolean cleanupFinished = false;
            for (int i=0; i<2 && !cleanupFinished; i++)
            {
              System.out.println("   Testcase wait... (30 seconds)");
              Thread.sleep(threshold + 30000);

              if (checkLog("ConnectionPool cleanup finished."))
              {
                cleanupFinished = true;
              }
            }

            c.close();

            if (checkLog("ConnectionPool cleanup...") &&
                checkLog("ConnectionPool cleanup finished."))
            {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 60, true);
              succeeded();
            }
            else
            {
              displayLog();
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 60, false);
              failed("The expected messages weren't found in trace log.");
            }
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 60, false);
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }

            Trace.setTraceOn(false);
        }
    }

    /**
    *  Validates connection pool is traced correctly.
    **/
    public void Var061()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 61, PROBABILITY, AGE)) {
        return; 
      }

        if (isApplet_)           //@A2A
        {                        //@A2A
            notApplicable();      //@A2A
            return;               //@A2A
        }                        //@A2A

        //@A4D Trace.setTraceOn(true);
        //@A4D Trace.setTraceInformationOn(true);
        //@A4D Trace.setTraceDiagnosticOn(true);
        //@A4D Trace.setTraceErrorOn(true);
        Trace.setTraceJDBCOn(true);    //@A4A

        AS400JDBCConnectionPool pool = null;
        try
        {
            DriverManager.setLogWriter(new PrintWriter(os_));  //@A2A
            Trace.setPrintWriter(new PrintWriter(os_, true));              //@A2C

            // previousFileEnd_ = testFile_.length();

            long threshold = 100*60; //@A2C

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxUseTime(threshold);
            pool.setCleanupInterval(threshold + 5000);
            pool.setRunMaintenance(true);
            pool.fill(2);

            Connection c = pool.getConnection();

            // Check after 30 seconds. If not done, give it another 30 seconds.
            boolean cleanupFinished = false;
            for (int i=0; i<2 && !cleanupFinished; i++)
            {
              System.out.println("   Testcase wait... (30 seconds)");
              Thread.sleep(threshold + 30000);
              if (checkLog("ConnectionPool cleanup finished."))
              {
                cleanupFinished = true;
              }

            }

            c.close();

            if (checkLog("Filling the pool with 2 connections.") &&
                checkLog("AS400JDBCPooledConnection.getConnection() was called") && //@A4C
                checkLog("ConnectionPool cleanup...") &&
                checkLog("ConnectionPool cleanup finished."))
            {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 61, true);
              succeeded();
            }
            else
            {
              displayLog();
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 61, false);
              failed("The expected messages weren't found in trace log.");
            }
        }
        catch (Exception e)
        {
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 61, false);
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }

            Trace.setTraceOn(false);
        }
    }


    /**
    *  Validates that the connection is closed through finalize (javascope).
    **/
    public void Var062()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 62, PROBABILITY, AGE)) {
        return; 
      }

        //@A2D Trace.setTraceOn(true);
        //@A2D Trace.setTraceInformationOn(true);

        AS400JDBCConnectionPool pool = null;
        try
        {
            //@A2D Trace.setPrintWriter(new PrintWriter(os_)); 	    	    
            //@A2D previousFileEnd_ = testFile_.length();

            long threshold = 1000*60;

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxUseTime(threshold);
            pool.setCleanupInterval(threshold + 5000);
            pool.setRunMaintenance(true);
            pool.fill(2);

            pool = null;
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 62, true);
            succeeded();
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 62, false);
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                System.gc();

                System.out.println("   Testcase wait... (10 seconds)");
                Thread.sleep(10000);
                System.gc();

                System.out.println("   Testcase wait... (25 seconds)");
                Thread.sleep(25000);
            }
            catch (InterruptedException ie) {
            }

        }
    }

    /**
    *  Validates that the maintenance is run in single-threaded mode.
    **/
    public void Var063()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 63, PROBABILITY, AGE)) {
        return; 
      }

        //@A2D Trace.setTraceOn(true);
        //@A2D Trace.setTraceInformationOn(true);
        //@A2D Trace.setTraceDiagnosticOn(true);
        //@A2D Trace.setTraceErrorOn(true);

        AS400JDBCConnectionPool pool = null;
        try
        {
            //@A2D Trace.setPrintWriter(new PrintWriter(os_)); 	    	    
            //@A2D previousFileEnd_ = testFile_.length();

            long threshold = 1000*60;

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.setCleanupInterval(threshold + 5000);
            pool.setThreadUsed(false);
            pool.setRunMaintenance(true);
            pool.fill(2);

            Connection c = pool.getConnection();

            System.out.println("   Testcase wait... (30 seconds)");
            Thread.sleep(threshold + 30000);
            c.close();

            System.out.println("   Testcase wait... (30 seconds)");
            Thread.sleep(threshold + 30000);
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 63, true);
            succeeded();
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 63, false);
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }

            //@A2D Trace.setTraceOn(false);
        }
    }


    /**
    *  Validates that the connection pool runs the maintenance daemon if an error occurs
    *  during a pool fill.
    **/
    public void Var064()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 64, PROBABILITY, AGE)) {
        return; 
      }

        AS400JDBCConnectionPool pool = null;
        try
        {
            long threshold = 1000*60;

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.setMaxUseTime(threshold);
            pool.setCleanupInterval(threshold + 5000);
            pool.setRunMaintenance(true);
            pool.fill(2);

            System.out.println("   Testcase wait... (30 seconds)");
            Thread.sleep(threshold + 30000);

            finishLongRunning("AS400JDBCConnectionPoolTestcase", 64, true);
            succeeded();
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 64, false);
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }


    /**
    *  Validates that a fill can be called multiple times.
    **/
    public void Var065()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            // long threshold = 1000*60;

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.fill(2);

            pool.fill(2);

            assertCondition(pool.getAvailableConnectionCount() == 4);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }

    /**
    *  Validates that fill works after the pool was closed.
    **/
    public void Var066()
    {
        AS400JDBCConnectionPool pool = null;
        try
        {
            // long threshold = 1000*60;

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 
            pool = new AS400JDBCConnectionPool(ds);
            pool.fill(2);
            pool.close();

            pool.fill(2);

            assertCondition(pool.getAvailableConnectionCount() == 2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }


    /**
    *  Validates that resetting the number of max connections will reduce the available
    *  connection in the pool and trace the information when a connection gets returned.
    **/
    public void Var067()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
        
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 67, PROBABILITY, AGE)) {
        return; 
      }

        //@A2D Trace.setTraceOn(true);
        //@A2D Trace.setTraceInformationOn(true);

        AS400JDBCConnectionPool pool = null;
        try
        {
            //@A2D Trace.setPrintWriter(new PrintWriter(os_)); 	    	    
            //@A2D previousFileEnd_ = testFile_.length();

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.setRunMaintenance(true);

            int expected = 10;
            pool.fill(expected);
            Connection c = pool.getConnection();

            int reduce = 5;
            pool.setMaxConnections(reduce);

            c.close();

            System.out.println("   Testcase wait... (90 seconds)");
            Thread.sleep(1000*90);

            boolean passed = pool.getAvailableConnectionCount() == (expected - reduce); 
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 67, passed);

            assertCondition(passed);
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 67, false);
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }

            //@A2D Trace.setTraceOn(false);
        }
    }


    /**
    *  Validates that a returned connection is traced.
    **/
    public void Var068()
    {
        Trace.setTraceOn(true);
        Trace.setTraceInformationOn(true);

        try
        {
            //@A2D Trace.setPrintWriter(new PrintWriter(os_)); 	    	    
            //@A2D previousFileEnd_ = testFile_.length();

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.setCleanupInterval(1000*60);
            pool.setMaxLifetime(1000*60);
            pool.setMaxInactivity(1000*30);
            pool.setRunMaintenance(true);

            pool.fill(2);

            Connection c = pool.getConnection();
            c.close();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            Trace.setTraceOn(false);
        }
    }

    /**
    *  Validates that isClosed() returns false after a previously closed pool is refilled.
    **/
    public void Var069()
    {
        AS400JDBCConnectionPool pool = null;

        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 

            pool.fill(5);
            pool.close();

            boolean before = pool.isClosed();    // should be true.

            pool.fill(2);

            assertCondition (!pool.isClosed() && before);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }

    /**
    *  Validates that isClosed() returns false after all pool connections have been closed by the users
    **/
    public void Var070()
    {
        AS400JDBCConnectionPool pool = null;

        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.fill(3);

            Connection c1 = pool.getConnection();
            Connection c2 = pool.getConnection();
            Connection c3 = pool.getConnection();

            c1.close();
            c2.close();
            c3.close();

            assertCondition (!pool.isClosed());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }


    /**
    *  Validates that isClosed() returns false after all pool connections have been closed by the users
    **/
    public void Var071()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 71, PROBABILITY, AGE)) {
        return; 
      }

        AS400JDBCConnectionPool pool = null;

        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.setRunMaintenance(true);
            pool.setCleanupInterval(1000*30);
            pool.setMaxLifetime(1000*15);
            pool.fill(3);

            Connection c1 = pool.getConnection();
            Connection c2 = pool.getConnection();
            Connection c3 = pool.getConnection();

            System.out.println("   Testcase wait... (45 seconds)");
            Thread.sleep(1000*45);

            c1.close();
            c2.close();
            c3.close();

            int before = pool.getAvailableConnectionCount();

            pool.fill(2);
            boolean passed  = !pool.isClosed() && (before == 0) && (pool.getAvailableConnectionCount() == 2); 
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 71, passed);

            assertCondition (passed );
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 71, false);

            failed(e, "Unexpected Exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }
        }
    }

    /**
    *  Validates that setRunMaintenance(false) works as expected after maintenance 
    *  has been run previously.
    **/
    public void Var072()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 72, PROBABILITY, AGE)) {
        return; 
      }

        AS400JDBCConnectionPool pool = null;
        try
        {
            pool = new AS400JDBCConnectionPool();
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool.setDataSource(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.setCleanupInterval(1000*30);
            pool.setRunMaintenance(true);
            pool.fill(2);

            boolean before = pool.isRunMaintenance();

            System.out.println("   Testcase wait... (45 seconds)");
            Thread.sleep(1000*45);
            pool.setRunMaintenance(false);

            boolean passed = !pool.isRunMaintenance() && pool.isRunMaintenance() != before; 
            finishLongRunning("AS400JDBCConnectionPoolTestcase", 72, passed);

            assertCondition(passed);
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 72, false);
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            pool.close();
        }
    }

    /**
    *  Validates that the maintenance thread doesn't run into problems with a connection
    *  being closed and returned to the pool.
    **/
    public void Var073()
    {
        Trace.setTraceOn(true);
        Trace.setTraceInformationOn(true);

        AS400JDBCConnectionPool pool = null;
        try
        {
            //@A2D Trace.setPrintWriter(new PrintWriter(os_)); 	    	    
            //@A2D previousFileEnd_ = testFile_.length();

          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            pool = new AS400JDBCConnectionPool(new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars));
	    PasswordVault.clearPassword(passwordChars); 
            pool.setRunMaintenance(true);
            pool.setCleanupInterval(20000);

            int expected = 20;
            pool.fill(expected);

            Connection c = pool.getConnection();

            System.out.println("   Testcase wait... (20 seconds)");
            Thread.sleep(19998);
            c.close();

            System.out.println("   Testcase wait... (10 seconds)");
            Thread.sleep(10000);

            assertCondition(pool.getAvailableConnectionCount() == expected );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            try
            {
                pool.close();
            }
            catch (Exception pc) {
            }

            Trace.setTraceOn(false);
        }
    }


    /**
    *  Tests getSource(). Validates that a ConnectionPoolEvent.CONNECTION_EXPIRED event occurs as expected.
    **/
    public void Var074()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 74, PROBABILITY, AGE)) {
        return; 
      }

        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*40);         // 40 sec  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(2);

            Connection c1 = pool.getConnection();

            System.out.println("   Testcase wait... (1 minutes)");
            Thread.sleep(60000);    // wait 1 minute.

            c1.close();
            pool.close();

            if (poolListener.getSource() == pool) {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 74, true);
              succeeded();
            } else  {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 74, false);

              failed("Source object mismatch: "+poolListener.getSource()+" != "+pool);
            }
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 74, false);

            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Tests getSource(). Validates that setting new maintenance properties uses the updated values.
    **/
    public void Var075()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 75, PROBABILITY, AGE)) {
        return; 
      }

        try
        {         
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.
            pool.setRunMaintenance(true);

            pool.fill(5);

            Connection c1 = pool.getConnection();
            Connection c2 = pool.getConnection();

            checkConnections(pool, 1000*75, 1);          // 75 sec check
            long check = poolListener.lastMaintenance();
            checkConnections(pool, 1000*75, 1);          // 75 sec check

            long diff1 = poolListener.lastMaintenance() - check;

            // Use new properties.
            pool.setCleanupInterval(1000*60*3);   // 3 min  Time interval daemon runs.

            long checkpoint1 = new java.util.Date().getTime();

            checkConnections(pool, 1000*70*3, 1);          // 3:30 check
            check = poolListener.lastMaintenance();

            long diff2 = check - checkpoint1;
  
            if (poolListener.getSource() == pool) {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 75, true);
              succeeded();
            }
            else {
                finishLongRunning("AS400JDBCConnectionPoolTestcase", 75, true);

                notApplicable("Unreliable TC:  Source object mismatch: "+poolListener.getSource()+" != "+pool+
                    " vars="+c1+c2+diff1+diff2);
                //failed("Source object mismatch: "+poolListener.getSource()+" != "+pool);
            }

            pool.close();
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 75, false);
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Tests getSource(). Validates that a ConnectionPoolEvent.CONNECTION_CLOSED event occurs as expected.
    **/
    public void Var076()
    {
      if (TestDriverStatic.brief_) {
        notApplicable("Skipping long-running variation");
        return;
      }
      if ( ! checkLongRunning("AS400JDBCConnectionPoolTestcase", 76, PROBABILITY, AGE)) {
        return; 
      }

        try
        {         
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.
            pool.setRunMaintenance(true);

            pool.fill(5);

            Connection c1 = pool.getConnection();

            checkConnections(pool, 70000, 1);          // 70 sec wait.

            pool.close();

            if (poolListener.getSource() == pool) { 
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 76, true);
              succeeded();
            }     else  {
              finishLongRunning("AS400JDBCConnectionPoolTestcase", 76, false);
              failed("Source object mismatch: "+poolListener.getSource()+" != "+pool+" c1="+c1);
            }
        }
        catch (Exception e)
        {
          finishLongRunning("AS400JDBCConnectionPoolTestcase", 76, false);
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Tests getSource(). Validates that a ConnectionPoolEvent.CONNECTION_CLOSED event occurs as expected.
    **/
    public void Var077()
    {
        try
        {         
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(5);

            Connection c1 = pool.getConnection();
            Connection c2 = pool.getConnection();

            pool.close();

            if (poolListener.getSource() == pool) succeeded();
            else failed("Source object mismatch: "+poolListener.getSource()+" != "+pool+" vars"+c1+c2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Tests getSource(). Validates that a ConnectionPoolEvent.CONNECTION_CREATED event occurs as expected.
    **/
    public void Var078()
    {
        try
        {         
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(5);

            pool.close();

            if (poolListener.getSource() == pool) succeeded();
            else failed("Source object mismatch: "+poolListener.getSource()+" != "+pool);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Tests getSource(). Validates that a ConnectionPoolEvent.CONNECTION_RELEASED event occurs as expected.
    **/
    public void Var079()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(5);

            Connection c1 = pool.getConnection();

            pool.close();

            if (poolListener.getSource() == pool) succeeded();
            else failed("Source object mismatch: "+poolListener.getSource()+" != "+pool+" "+c1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Tests getSource(). Validates that a ConnectionPoolEvent.CONNECTION_RETURNED event occurs as expected.
    **/
    public void Var080()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(5);

            Connection c1 = pool.getConnection();
            c1.close();

            pool.close();

            if (poolListener.getSource() == pool) succeeded();
            else failed("Source object mismatch: "+poolListener.getSource()+" != "+pool);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Tests getSource(). Validates that removeConnectionPoolListener(ConnectionPoolListener) works as expected.
    **/
    public void Var081()
    {
        try
        {
          char[] passwordChars =  PasswordVault.decryptPassword(encryptedPassword_); 
            AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
	    PasswordVault.clearPassword(passwordChars); 

            AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool(ds);
            MyPoolListener poolListener = new MyPoolListener();

            pool.addConnectionPoolListener((ConnectionPoolListener)poolListener);
            pool.setMaxConnections(10);           // Max connections.
            pool.setCleanupInterval(1000*60*1);   // 1 min  Time interval daemon runs.
            pool.setMaxInactivity(1000*60*5);     // 5 min Max time in pool.
            pool.setMaxLifetime(1000*60*30);      // 30 min  Max lifetime since created.
            pool.setMaxUseTime(1000*60*3);        // 3 min Max usage limit.
            pool.setMaxUseCount(3);               // Times connection can be reused.

            pool.fill(3);

            Connection c1 = pool.getConnection();

            if (poolListener.getSource() != null && poolListener.getSource() instanceof AS400JDBCPooledConnection) succeeded();
            else failed("Source object mismatch: "+poolListener.getSource()+" "+c1);

            pool.close();

        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
      Serializes and deserializes the connection pool object.
     **/
    private AS400JDBCConnectionPool serialize (AS400JDBCConnectionPool cp) throws Exception
    {
        // Serialize.
        String serializeFileName = "connectionpool.ser";
        ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFileName));
        out.writeObject (cp);
        out.flush ();
        out.close();

        // Deserialize.
        AS400JDBCConnectionPool cp2 = null;
        try {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFileName));
            cp2 = (AS400JDBCConnectionPool) in.readObject ();
            in.close();
        }
        finally {
            File f = new File (serializeFileName);
            f.delete();
        }
        return cp2;
    }


    class MyPropertyVetoListener implements VetoableChangeListener
    {
        private boolean veto_ = false;

        public MyPropertyVetoListener()
        {

        }
        public boolean isVetoed()
        {
            boolean value = veto_;
            veto_ = false;    // reset.
            return value;
        }
        public void vetoableChange(PropertyChangeEvent event)
        {
            veto_ = true;
        }
    }

    class MyPropertyChangeListener implements PropertyChangeListener
    {
        private boolean change_ = false;

        public MyPropertyChangeListener()
        {
        }
        public boolean isChanged()
        {
            boolean value = change_;
            change_ = false;    // reset.
            return value;
        }
        public void propertyChange(PropertyChangeEvent event)
        {
            change_ = true;
        }
    }

    class MyPoolListener implements ConnectionPoolListener
    {
        private long closed_ = 0;
        private long created_ = 0;
        private long expired_ = 0;
        private long released_ = 0;
        private long returned_ = 0;
        private long maintenance_ = 0;
        private Object source_ = null ; //@A3A

        public MyPoolListener()
        {

        }
        public void connectionPoolClosed(ConnectionPoolEvent event)
        {
            closed_ = new java.util.Date().getTime();
            source_ = event.getSource(); //@A3A
        }
        public void connectionCreated(ConnectionPoolEvent event)
        {
            created_ = new java.util.Date().getTime();
            source_ = event.getSource(); //@A3A
        }
        public void connectionExpired(ConnectionPoolEvent event)
        {
            expired_ = new java.util.Date().getTime();
            source_ = event.getSource(); //@A3A
        }
        public void connectionReleased(ConnectionPoolEvent event)
        {
            released_ = new java.util.Date().getTime();
            source_ = event.getSource(); //@A3A
        }
        public void connectionReturned(ConnectionPoolEvent event)
        {
            returned_ = new java.util.Date().getTime();
            source_ = event.getSource(); //@A3A
        }
        public void maintenanceThreadRun(ConnectionPoolEvent event)
        {
            maintenance_ = new java.util.Date().getTime();
            source_ = event.getSource(); //@A3A
        }
        public Object getSource() //@A3A
        {
            return source_; //@A3A
        }
        public long lastClosed()
        {
            return closed_;
        }
        public long lastCreated()
        {
            return created_;
        }
        public long lastExpired()
        {
            return expired_;
        }
        public long lastReleased()
        {
            return released_;
        }
        public long lastReturned()
        {
            return returned_;
        }
        public long lastMaintenance()
        {
            return maintenance_;
        }
    }
}
