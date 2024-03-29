///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTestDriver_NativeDriverMethods.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

//import com.ibm.db2.jdbc.app.DB2ConnectionPoolDataSource;
//import com.ibm.db2.jdbc.app.DB2GenericDataSource;
//import com.ibm.db2.jdbc.app.DB2PooledConnection;
//import com.ibm.db2.jdbc.app.DB2Properties;
//import com.ibm.db2.jdbc.app.DB2XAConnection;
//import com.ibm.db2.jdbc.app.DB2XADataSource;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;



public class JDTestDriver_NativeDriverMethods
{
   private boolean defSchemaSet_ = false;

   public JDTestDriver_NativeDriverMethods ()
             throws Exception
   {
   }

   boolean getDefSchemaSet()
   {
      return defSchemaSet_;
   }

   void setDefSchemaSet(boolean value)
   {
      defSchemaSet_ = value;
   }

   public Connection getConnection (String url, int driver, int connectionType) throws Exception
   {
     int connType_ = connectionType;

         if (connType_ == Testcase.CONN_POOLED)
         {
        	Object dataSource = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPoolDataSource "); 

            JDReflectionUtil.callMethod_V(dataSource, "setDataSourceName","JDTestDriver");
            JDReflectionUtil.callMethod_V(dataSource, "setDescription", "JDBC Test Bucket, Pooled Connection Option");

            if (handleURLProcessing(url, dataSource))
            {
               Object pooledConn = JDReflectionUtil.callMethod_O(dataSource, "getPooledConnection"); 
               // Add a listener that always closes the connections so that
               // we can be sure that the behavior of close will actually close
               // the physical connection. Lots of testcases expect this.
               ConnectionEventListener cel = new ConnectionEventListener() {
                 public void connectionClosed(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
                public void connectionErrorOccurred(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
             }; 
               JDReflectionUtil.callMethod_V(pooledConn,"addConnectionEventListener",cel);
               return (Connection) JDReflectionUtil.callMethod_O(pooledConn, "getConnection"); 
               
            }
            else
               return null;
         }
         else if (connType_ == Testcase.CONN_XA)
         {
            System.out.println("XA Connection");
            Object xaDS = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(xaDS, "setDatasourceName","JDTestDriver");
            JDReflectionUtil.callMethod_V(xaDS,  "setDescription", "JDBC Test Bucket, Transaction Option");
            if (handleURLProcessing(url, xaDS))
            {
            	Object xaConn = JDReflectionUtil.callMethod_O(xaDS, "getXAConnection"); 
               
               // Add a listener that always closes the connections so that
               // we can be sure that the behavior of close will actually close
               // the physical connection. Lots of testcases expect this.
               ConnectionEventListener cel = new ConnectionEventListener() {
                 public void connectionClosed(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
                public void connectionErrorOccurred(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
             }; 
               JDReflectionUtil.callMethod_V(xaConn,"addConnectionEventListener",cel);
               return (Connection) JDReflectionUtil.callMethod_O(xaConn,"getConnection"); 
               
            }
            else
               return null;
         }

         return null;
   }


/**
Get a connection. Calls the DriverManager's getConnection() method
for default testing. Else returns pooled or XA connection

@param url The url
@param properties Properties for the connection
@exception  Exception  If an exception occurs.
**/

   public Connection getConnection (String url, Properties info, int driver, int connectionType) throws Exception
   {
      int connType_ = connectionType;

        if (connType_ == Testcase.CONN_POOLED)
        {
            Object dataSource = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPoolDataSource");
            JDReflectionUtil.callMethod_V(dataSource, "setDataSourceName","JDTestDriver");
            JDReflectionUtil.callMethod_V(dataSource, "setDescription","JDBC Test Bucket, Pooled Connection Option");
            
         
            // First set the values from the Properties, then
            // parse the url. This is done because when the same
            // property is specified in both the url and the Properties,
            // the one in url should be used.

            Enumeration<?> e =  info.propertyNames();

            while (e.hasMoreElements()) {
               String field = (String)e.nextElement();
               String value = info.getProperty(field);
               setDataSourceProperty(dataSource, field, value);
            }
            if (handleURLProcessing(url, dataSource)) {
            	Object pooledConn = JDReflectionUtil.callMethod_O(dataSource, "getPooledConnection"); 
               
               // Add a listener that always closes the connections so that
               // we can be sure that the behavior of close will actually close
               // the physical connection. Lots of testcases expect this.
               ConnectionEventListener cel = new ConnectionEventListener() {
                 public void connectionClosed(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
                public void connectionErrorOccurred(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
             }; 
               JDReflectionUtil.callMethod_V(pooledConn,"addConnectionEventListener",cel);
               return (Connection) JDReflectionUtil.callMethod_O(pooledConn,  "getConnection"); 
               
            }
            else
               return null;
         }
         else if (connType_ == Testcase.CONN_XA) {
            System.out.println("XA Connection");
            Object xaDS = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(xaDS, "setDataSourceName","JDTestDriver");
            JDReflectionUtil.callMethod_V(xaDS, "setDescription", "JDBC Test Bucket, Transaction Option");
            // First set the values from the Properties, then
            // parse the url. This is done because when the same
            // property is specified in both the url and the Properties,
            // the one in url should be used.

            Enumeration<?> e = info.propertyNames();

            while (e.hasMoreElements()) {
               String field = (String)e.nextElement();
               String value = info.getProperty(field);
               setDataSourceProperty(xaDS, field, value);
            }
            if (handleURLProcessing(url, xaDS)) {
               Object xaConn = JDReflectionUtil.callMethod_O(xaDS,  "getXAConnection");
               // Add a listener that always closes the connections so that
               // we can be sure that the behavior of close will actually close
               // the physical connection. Lots of testcases expect this.
               ConnectionEventListener cel = new ConnectionEventListener() {
                 public void connectionClosed(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
                public void connectionErrorOccurred(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
             }; 
               JDReflectionUtil.callMethod_V(xaConn,"addConnectionEventListener",cel);
               return (Connection) JDReflectionUtil.callMethod_O(xaConn,"getConnection"); 
          
            }
            else
               return null;
         }
         return null;
   }


/**
Get a connection. Calls the DriverManager's getConnection() method
for default testing. Else returns pooled or XA connection

@param url The url
@param uid The userid to connect with
@param pwd Password for the userid
@exception  Exception  If an exception occurs.
**/

   public Connection getConnection (String url, String uid, char[] encryptedPassword, int driver, int connectionType) throws Exception
   {
      int connType_ = connectionType;

         if (connType_ == Testcase.CONN_POOLED)
         {
             char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword);
        	 Object dataSource = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPoolDataSource"); 
        	 JDReflectionUtil.callMethod_V(dataSource,  "setDataSourceName","JDTestDriver");
        	 JDReflectionUtil.callMethod_V(dataSource,  "setDescription","JDBC Test Bucket, Pooled Connection Option");
            setDataSourceProperty(dataSource, "user", uid);
            try {
              JDReflectionUtil.callMethod_V(dataSource, "setPassword",passwordChars);
            } catch (Exception ex) { 
              System.out.println("Warning: unable to get native pooled connection using secure password");
              ex.printStackTrace(System.out);
              String pwd = PasswordVault.decryptPasswordLeak(encryptedPassword); 
              setDataSourceProperty(dataSource, "password", pwd);
            } finally {
              PasswordVault.clearPassword(passwordChars);; 
            }
            
            
            if (handleURLProcessing(url, dataSource)) {
            	Object pooledConn = JDReflectionUtil.callMethod_O(dataSource, "getPooledConnection"); 
               
               // Add a listener that always closes the connections so that
               // we can be sure that the behavior of close will actually close
               // the physical connection. Lots of testcases expect this.
               ConnectionEventListener cel = new ConnectionEventListener() {
                 public void connectionClosed(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
                public void connectionErrorOccurred(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
             }; 
               JDReflectionUtil.callMethod_V(pooledConn,"addConnectionEventListener",cel);
               return (Connection) JDReflectionUtil.callMethod_O(pooledConn,"getConnection");
            }
            else
               return null;
         }
         else if (connType_ == Testcase.CONN_XA) {
            System.out.println("XA Connection");
             Object xaDS = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(xaDS, "setDatasourceName","JDTestDriver");
            JDReflectionUtil.callMethod_V(xaDS,  "setDescription", "JDBC Test Bucket, Transaction Option");

            setDataSourceProperty(xaDS, "user", uid);
            char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword);
            try {
              JDReflectionUtil.callMethod_V(xaDS, "setPassword",passwordChars);
            } catch (Exception ex) { 
              System.out.println("Warning: unable to get native XA connection using secure password");
              ex.printStackTrace(System.out);
              String pwd = PasswordVault.decryptPasswordLeak(encryptedPassword); 
              setDataSourceProperty(xaDS, "password", pwd);
            } finally {
              PasswordVault.clearPassword(passwordChars);; 
            }
            
            if (handleURLProcessing(url, xaDS)) {
            	Object xaConn = JDReflectionUtil.callMethod_O(xaDS,  "getXAConnection"); 
               // Add a listener that always closes the connections so that
               // we can be sure that the behavior of close will actually close
               // the physical connection. Lots of testcases expect this.
               ConnectionEventListener cel = new ConnectionEventListener() {
                 public void connectionClosed(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
                public void connectionErrorOccurred(ConnectionEvent event) {
                   // Normally the listener would pool this thing.
                   // We'll just close it.
                   try {
                      PooledConnection pc = (PooledConnection)event.getSource();
                      pc.close();
                   }
                   catch (Exception ex) {
                   }
                }
             }; 
               JDReflectionUtil.callMethod_V(xaConn,"addConnectionEventListener",cel);
               return (Connection) JDReflectionUtil.callMethod_O(xaConn, "getConnection"); 
               
            }
         }
         return null;
   }



/**
(This is based extensively on DB2Driver.handleURLProcessing)
Deals with all work involved in parsing a Connection URL.

@param  url             The URL to be parsed.
@param  p               The connection properties.
@param  doingConnection whether or not to connection - should always be false.
                        If you want to actually connect, call the function
                        handleURLProcessingForConnection().
@return                 True or False: The url is valid for connections.
 * @throws Exception 
**/
   private boolean handleURLProcessing (String url,
                                        Object ds)
   throws Exception
   {


      // Check for null.
      if (url == null)
         return false;

      StringTokenizer semicolontok = new StringTokenizer (url, ";");
      while (semicolontok.hasMoreTokens ()) {

         String token = semicolontok.nextToken ();
         //System.out.println(token);
         // is this the base?
         if (token.toLowerCase().startsWith ("jdbc")) {

            StringTokenizer colontok = new StringTokenizer(token, ":");
            if (colontok.countTokens() < 3) {
               System.out.println("invalid url");
               return false;
            }

            /* firstpart =  */ colontok.nextToken();
            /* secondpart = */ colontok.nextToken();
            String remUrl = colontok.nextToken();
            StringTokenizer sysNameTok = new StringTokenizer(remUrl, "/");

            if (sysNameTok.hasMoreTokens()) {
               String dbName = sysNameTok.nextToken();
               if (dbName.equalsIgnoreCase("LOCALHOST"))
                  dbName = "*LOCAL";
               JDReflectionUtil.callMethod_V(ds, "setDatabaseName", dbName); 
               
            }
            else
               return false;

            if (sysNameTok.hasMoreTokens()) {
               String schema = sysNameTok.nextToken(); //cmvc
               System.out.println("schema=" + schema);
               JDReflectionUtil.callMethod_V(ds,  "setLibraries", schema); 
            
               defSchemaSet_ = true;
            }

         }
         else {
            // trying to set a property
            StringTokenizer propTok = new StringTokenizer(token, "=");
            if (propTok.countTokens() == 2) {
               String field = propTok.nextToken().toLowerCase();
               String value = propTok.nextToken();
               //System.out.println(field + "=" + value);

               setDataSourceProperty(ds, field, value);
            }
            else
               return false;

         }

      }
      return true;
   }


   private void setDataSourceProperty(Object ds, String field, String value)
   throws Exception {

      if (field.equals("access")) {
         JDReflectionUtil.callMethod_V(ds,"setAccess",value);
      }
      else if (field.equals("block size")) {
         JDReflectionUtil.callMethod_V(ds,"setBlockSize",Integer.valueOf(value).intValue());
      }
      else if (field.equals("blocking enabled")) {
         JDReflectionUtil.callMethod_V(ds,"setUseBlocking",Boolean.valueOf(value).booleanValue());
      }
      else if (field.equals("block criteria")) {     // cmvc
         JDReflectionUtil.callMethod_V(ds,"setUseBlocking",Boolean.valueOf(value).booleanValue());
      }
      else if (field.equals("date format")) {
         JDReflectionUtil.callMethod_V(ds,"setDateFormat",value);
      }
      else if (field.equals("date separator")) {
         JDReflectionUtil.callMethod_V(ds,"setDateSeparator",value);
      }
      else if (field.equals("decimal separator")) {
         JDReflectionUtil.callMethod_V(ds,"setDecimalSeparator",value);
      }
      else if (field.equals("do escape processing")) {
         throw new java.sql.SQLException("ESCAPE PROCESSING property not supported.");
      }
      else if (field.equals("libraries")) {
         if (!defSchemaSet_)
            JDReflectionUtil.callMethod_V(ds,"setLibraries",value);
      }
      else if (field.equals("naming")) {
         JDReflectionUtil.callMethod_V(ds,"setNamingOption",value);
      }
      else if (field.equals("password")) {
         JDReflectionUtil.callMethod_V(ds,"setPassword",value);
      }
      else if (field.equals("time format")) {
         JDReflectionUtil.callMethod_V(ds,"setTimeFormat",value);
      }
      else if (field.equals("time separator")) {
         JDReflectionUtil.callMethod_V(ds,"setTimeSeparator",value);
      }
 
      else if (field.equals("trace"))
      {
         JDReflectionUtil.callMethod_V(ds,"setTrace",Boolean.valueOf(value).booleanValue());
      }
      else if (field.equals("transaction isolation")) {
         JDReflectionUtil.callMethod_V(ds,"setTransactionIsolationLevel",value);
      }
      else if (field.equals("translate binary")) {
         JDReflectionUtil.callMethod_V(ds,"setTranslateBinary",Boolean.valueOf(value).booleanValue());
      }
      else if (field.equals("user")) {
         JDReflectionUtil.callMethod_V(ds,"setUser",value);
      }
      else if (field.equals("prefetch")) { 
         // there is no way to set prefetch using a datasource
         // and currently prefetch is ignored anyway (even when
         // specified in the url)
      }
      else if (field.equals("errors")) { 
         // there is no way to set errors using a datasource
         // and currently errors is ignored anyway (even when
         // specified in the url)
      }
      else if (field.equals("reuse objects")) { // cmvc
         if (value.equalsIgnoreCase("true")) {
            JDReflectionUtil.callMethod_V(ds,"setReuseObjects",true);
         }
         else {
            JDReflectionUtil.callMethod_V(ds,"setReuseObjects",false);
         }

      }
      else if (field.equals("lob threshold")) { // cmvc
	  JDReflectionUtil.callMethod_V(ds,"setLobThreshold",Integer.valueOf(value).intValue()); 
      }
      else if (field.equals("data truncation")) {
         JDReflectionUtil.callMethod_V(ds,"setDataTruncation",Boolean.valueOf(value).booleanValue());
      }
      else if (field.equals("direct map")) {
        try {
          JDReflectionUtil.callMethod_V(ds, "setDirectMap",Boolean.valueOf(value).booleanValue());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      else if (field.equals("query optimize goal")) {
        try {
          JDReflectionUtil.callMethod_V(ds, "setQueryOptimizeGoal",value); 
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      else if (field.equals("decfloat rounding mode")) {
        try {
          JDReflectionUtil.callMethod_V(ds, "setDecfloatRoundingMode",value); 
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      else if (field.equals("extended dynamic")) { // cmvc
         // ignored
      }
      else if (field.equals("package cache")) { // cmvc
         // ignored
      }
      else if (field.equals("package library")) { // cmvc
         // ignored
      }
      else if (field.equals("cursor hold")) {
         JDReflectionUtil.callMethod_V(ds,"setCursorHold",Boolean.valueOf(value).booleanValue()); 
      }
      else if (field.equals("cursorhold")) {
	  if ( value.equals("1")) {
	      JDReflectionUtil.callMethod_V(ds,"setCursorHold",true); 
	  } else { 
	      JDReflectionUtil.callMethod_V(ds,"setCursorHold",false); 
	  }
      } 

      else
         throw new java.sql.SQLException(field + " property not supported.");
   }

}


