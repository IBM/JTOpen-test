///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConnectionPoolPropertiesTestcase.java
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
import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnectionPool;

/**
  Testcase ConnectionPoolPropertiesTestcase.
 **/
public class ConnectionPoolPropertiesTestcase extends Testcase
{

   /**
     Constructor.  This is called from the ConnectionPoolPropertiesTest constructor.
    **/
   public ConnectionPoolPropertiesTestcase(AS400 systemObject,
                                           Vector variationsToRun,
                                           int runMode,
                                           FileOutputStream fileOutputStream,
                                           
                                           String password)
   {
      super(systemObject, "ConnectionPoolPropertiesTestcase", variationsToRun,
            runMode, fileOutputStream, password);
   }
   

   /**
   *  Validate that the default number of maximum connections is unlimited.
   *
   *  ConnectionPoolProperties()
   *  getMaxConnections()
   **/
   public void Var001()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();

         assertCondition(pool.getMaxConnections() == -1); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }


   /**
   *  Validate that the default cleanup interval is 5 minutes.
   *
   *  ConnectionPoolProperties()
   *  getCleanupInterval()
   **/
   public void Var002()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();

         assertCondition(pool.getCleanupInterval() == 300000); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate that the default maximum idle time is unlimited.
   *
   *  ConnectionPoolProperties()
   *  getMaxInactivity()
   **/
   public void Var003()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         assertCondition(pool.getMaxInactivity() == 60*60*1000); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate that the default maximum connection life is 24 hours.
   *
   *  ConnectionPoolProperties()
   *  getMaxLifetime()
   **/
   public void Var004()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();

         assertCondition(pool.getMaxLifetime() == 1000*60*60*24);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate that the default maximum time a connection can be used is unlimited.
   *
   *  ConnectionPoolProperties()
   *  getMaxUseTime()
   **/
   public void Var005()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();

         assertCondition(pool.getMaxUseTime() == -1); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate that the default maximum times a connection can be used is -1.
   *
   *  ConnectionPoolProperties()
   *  getMaxUseCount()
   **/
   public void Var006()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();

         assertCondition(pool.getMaxUseCount() == -1); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }


   /**
   *  Validate that setMaxConnections(int) sets the maximum number of connections as expected.
   *
   *  ConnectionPoolProperties()
   *  setMaxConnections(int)
   *  getMaxConnections()
   **/
   public void Var007()
   {
      int max = 10;
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         pool.setMaxConnections(max);

         assertCondition(pool.getMaxConnections() == max); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }


   /**
   *  Validate that setCleanupInterval(long) sets the maintenance interval as expected.
   *
   *  ConnectionPoolProperties()
   *  setCleanupInterval(long)
   *  getCleanupInterval()
   **/
   public void Var008()
   {
      long interval = 600000;
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         pool.setCleanupInterval(interval);

         assertCondition(pool.getCleanupInterval() == interval); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate that setMaxInactivity sets th maximum idle time as expected.
   *
   *  ConnectionPoolProperties()
   *  setMaxInactivity(long)
   *  getMaxInactivity()
   **/
   public void Var009()
   {
      long max = 12 * 60 * 60 * 1000;

      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         pool.setMaxInactivity(max);

         assertCondition(pool.getMaxInactivity() == max); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate that setMaxLifetime sets the maximum connection life as expected.
   *
   *  ConnectionPoolProperties()
   *  setMaxLifetime(long)
   *  getMaxLifetime()
   **/
   public void Var010()
   {
      long max = 12 * 60 * 60 * 1000;

      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         pool.setMaxLifetime(max);

         assertCondition(pool.getMaxLifetime() == max); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate that setMaxUseTime(long) sets the maximum time a connection can be used as expected.
   *
   *  ConnectionPoolProperties()
   *  setMaxUseTime(long)
   *  getMaxUseTime()
   **/
   public void Var011()
   {
      long max = 1000 * 60 * 60 * 24;
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         pool.setMaxUseTime(max);

         assertCondition(pool.getMaxUseTime() == max); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate that setMaxUseCount(int) sets the maximum times a connection can be used as expected.
   *
   *  ConnectionPoolProperties()
   *  setMaxUseCount(int)
   *  getMaxUseCount()
   **/
   public void Var012()
   {
      int max = 10;
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         pool.setMaxUseCount(10);

         assertCondition(pool.getMaxUseCount() == max); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }


   /**
   *  Validate addPropertyChangeListener() notifies the listener that was added as expected.
   *
   *  ConnectionPoolProperties()
   *  setMaxConnections(int)
   *  addPropertyChangeListener()
   **/
   public void Var013()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         PropertiesListener listener = new PropertiesListener();
         pool.addPropertyChangeListener(listener);

         pool.setMaxConnections(100);

         assertCondition(listener.isPropertyEventFired()); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate addPropertyChangeListener() notifies the listener that was added as expected.
   *
   *  ConnectionPoolProperties()
   *  setMaxUseCount(int)
   *  addPropertyChangeListener()
   **/
   public void Var014()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         PropertiesListener listener = new PropertiesListener();
         pool.addPropertyChangeListener(listener);

         pool.setMaxUseCount(250);

         assertCondition(listener.isPropertyEventFired()); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate addPropertyChangeListener() notifies the listener that was added as expected.
   *
   *  ConnectionPoolProperties()
   *  setMaxUseTime(long)
   *  addPropertyChangeListener()
   **/
   public void Var015()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         PropertiesListener listener = new PropertiesListener();
         pool.addPropertyChangeListener(listener);

         pool.setMaxUseTime(1000*60*60);

         assertCondition(listener.isPropertyEventFired()); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate addPropertyChangeListener() notifies the listener that was added as expected.
   *
   *  ConnectionPoolProperties()
   *  setMaxLifetime(long)
   *  addPropertyChangeListener()
   **/
   public void Var016()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         PropertiesListener listener = new PropertiesListener();
         pool.addPropertyChangeListener(listener);

         pool.setMaxLifetime(1000*60*60);

         assertCondition(listener.isPropertyEventFired()); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate addPropertyChangeListener() notifies the listener that was added as expected.
   *
   *  ConnectionPoolProperties()
   *  setMaxInactivity(long)
   *  addPropertyChangeListener()
   **/
   public void Var017()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         PropertiesListener listener = new PropertiesListener();
         pool.addPropertyChangeListener(listener);

         pool.setMaxInactivity(1000*60*60*24);

         assertCondition(listener.isPropertyEventFired()); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

   /**
   *  Validate addPropertyChangeListener() notifies the listener that was added as expected.
   *
   *  ConnectionPoolProperties()
   *  setCleanupInterval(int)
   *  addPropertyChangeListener()
   **/
   public void Var018()
   {
      try
      {
         AS400JDBCConnectionPool pool = new AS400JDBCConnectionPool();
         PropertiesListener listener = new PropertiesListener();
         pool.addPropertyChangeListener(listener);

         pool.setCleanupInterval(100);

         assertCondition(listener.isPropertyEventFired()); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception occurred.");
      }
   }

    
   class PropertiesListener implements PropertyChangeListener, VetoableChangeListener
   {
      private boolean property_ = false;
      private boolean veto_ = false;

      public PropertiesListener()
      {
      }
      public boolean isPropertyEventFired()
      {
         return property_;
      }
      public boolean isVetoEventFired()
      {
         return veto_;
      }
      public void propertyChange(PropertyChangeEvent event)
      {
         property_ = true;
      }
      public void vetoableChange(PropertyChangeEvent event)
      {
         veto_ = true;
      }
   }
}
