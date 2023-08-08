///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSCachedRowSet.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import com.ibm.as400.access.AS400;


/**
Testcase JDRSCachedRowSet.
**/
public class JDRSCachedRowSet
extends JDTestcase
{
    private String jndiName_ = "jdbc";
    static final String key2_           = "JDRSCachedRowSet";
    private static String COLLECTION_      = "JDTESTRS";
    private static String TABLE_          = COLLECTION_ + ".CACHEDRS";
    private static String select_         = "SELECT * FROM " + TABLE_;
    private Connection conn_;
    private Statement stmt_;
    ResultSet rs_;
    Object /* DB2CachedRowSet */  crs_;

/**
Constructor.
**/
    public JDRSCachedRowSet (AS400 systemObject,
                               Hashtable namesAndVars,
                               int runMode,
                               FileOutputStream fileOutputStream,
                               
                               String password)
    {
        super (systemObject, "JDRSCachedRowSet",
            namesAndVars, runMode, fileOutputStream,
            password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
       super.setup();

       COLLECTION_      = JDRSTest.COLLECTION; 
       TABLE_          = COLLECTION_ + ".CACHEDRS";
       select_         = "SELECT * FROM " + TABLE_;

       if (getDriver() == JDTestDriver.DRIVER_NATIVE) { 
       //Setup the datasource stuff
       DataSource ds;
       System.setProperty(Context.INITIAL_CONTEXT_FACTORY,  "com.sun.jndi.fscontext.RefFSContextFactory");

       ds = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBDataSource"); 

       JDReflectionUtil.callMethod_V(ds,"setDatabaseName",systemObject_.getSystemName());
       JDReflectionUtil.callMethod_V(ds,"setUser",systemObject_.getUserId ());
    		   JDReflectionUtil.callMethod_V(ds,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));

       Context context = new InitialContext();
       context.rebind(jndiName_,ds);

       //build a table to query
       conn_ = DriverManager.getConnection(baseURL_);
       stmt_ = conn_.createStatement ();
       JDSetupCollection.create (systemObject_,  conn_, COLLECTION_);
       boolean tableCreated;
       try {
            stmt_.executeUpdate("CREATE TABLE " + TABLE_ + "(C_ONE VARCHAR(50))");
            tableCreated = true;
       }
       catch (Exception e) {
            System.out.println("Possible Setup Failure: ");
            e.printStackTrace();
            tableCreated = false;
       }
       if (tableCreated)
       {
           stmt_.executeUpdate ("INSERT INTO " + TABLE_
                                   + " (C_ONE)"
                                   + " VALUES ('FIRST')");
           stmt_.executeUpdate ("INSERT INTO " + TABLE_
                                   + " (C_ONE)"
                                   + " VALUES ('SECOND')");
           stmt_.executeUpdate ("INSERT INTO " + TABLE_
                                   + " (C_ONE)"
                                   + " VALUES ('THIRD')");
           stmt_.executeUpdate ("INSERT INTO " + TABLE_
                                   + " (C_ONE)"
                                   + " VALUES ('FOURTH')");
           stmt_.executeUpdate ("INSERT INTO " + TABLE_
                                   + " (C_ONE)"
                                   + " VALUES ('FIFTH')");
       }
       }
    }
/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup () throws Exception
    {
        cleanupTable(stmt_, TABLE_);
        stmt_.close ();
        conn_.close ();
	super.cleanup(); 
    }

    public void cleanupConnections() throws Exception {
      conn_.close();
      super.cleanupConnections(); 
    }

/**
Positions the cursor on a particular row and return the
row set.

@param  rs      The row set.
@param  key     The key.  If null, positions to before the
                first row.

@exception SQLException If an exception occurs.
**/
    public void position (RowSet rs, String key) throws SQLException
    {
       rs.beforeFirst ();
       position0(rs, key);
    }

/**
Positions the cursor on a particular row and return the
row set.

@param  rs      The row set.
@param  key     The key.  If null, positions to before the
                first row.

@exception SQLException If an exception occurs.
**/
    public void position0(RowSet rs, String key) throws SQLException
    {
       if (key != null)
       {
          while (rs.next ())
          {
             String s = rs.getString ("C_KEY");
             if (s != null)
             {
                if (s.equals (key))
                   return;
             }
          }
          System.out.println ("Warning: Key " + key + " not found.");
       }
    }

/**
populate()
w/ connection open
**/
    public void Var001()
    {
	if (checkNative()) { 
	    try
	    {
		Connection conn = DriverManager.getConnection(baseURL_);

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(select_);
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet"); 
			
		JDReflectionUtil.callMethod_V(crs,"populate",rs);
		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    rowCount++;
		}
		assertCondition(rowCount == 5);
		JDReflectionUtil.callMethod_V(crs,"close");
		rs.close();
		stmt.close();
		conn.close();
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
populate()
w/ connection open, then closed
**/
    public void Var002()
    {
	if (checkNative()) {
	    try
	    {
		Connection conn = DriverManager.getConnection(baseURL_);

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(select_);
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"populate",rs);
		rs.close();
		stmt.close();
		conn.close();

	    //data should be cached and still available.
		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    rowCount++;
		}
		assertCondition(rowCount == 5);
		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
populate()
w/ connection closed
Should throw an exception.
**/
    public void Var003()
    {
	if (checkNative()) {
	    try
	    {
		Connection conn = DriverManager.getConnection(baseURL_);

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(select_);
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		rs.close();
		stmt.close();
		conn.close();
		JDReflectionUtil.callMethod_V(crs,"populate",rs);
		failed("No SQLException thrown");
	    }
	    catch (SQLException e)
	    {
		succeeded();
	    }
	    catch (Exception e)
	    {
		failed(e,"SQLException Expected");
	    }
	}
    }

/**
setDataSourceName(String)
setCommand()
execute()
**/
    public void Var004()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setDataSourceName",jndiName_);
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"execute");
		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    rowCount++;
		}
		assertCondition(rowCount == 5);
		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setUrl(String)
setCommand()
execute()
**/
    public void Var005()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setUrl(baseURL_);
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"execute");
		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    rowCount++;
		}
		assertCondition(rowCount == 5);
		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setConnection(String)
setCommand()
execute()
**/
    public void Var006()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setConnection",conn_);
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"execute");
		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    rowCount++;
		}
		assertCondition(rowCount == 5);
		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setConnection(String)
w/ a closed connection
setCommand()
execute()
**/
    public void Var007()
    {
	if (checkNative()) {
	    try
	    {
		Connection conn = DriverManager.getConnection(baseURL_);
		conn.close();
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setConnection",conn);
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"execute");
		failed("No SQLException thrown");
	    }
    catch (SQLException e)
	    {
		succeeded();
	    }
	    catch (Exception e)
	    {
		failed(e,"SQLException Expected");
	    }
	}
    }
/**
setCommand()
execute(String)
**/
    public void Var008()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		crs.execute(conn_);
		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    rowCount++;
		}
		assertCondition(rowCount == 5);
		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setCommand()
execute(String)
w/ a closed connection
**/
    public void Var009()
    {
	if (checkNative()) {
	    try
	    {
		Connection conn = DriverManager.getConnection(baseURL_);
		conn.close();
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		crs.execute(conn);
		failed("No SQLException thrown");
	    }
	    catch (SQLException e)
	    {
		succeeded();
	    }
	    catch (Exception e)
	    {
		failed(e,"SQLException Expected");
	    }
	}
    }

/**
setCommand()
setDataSourceName()
execute(int)
**/
    public void Var010()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"setDataSourceName",jndiName_);
		JDReflectionUtil.callMethod_V(crs,"execute",2);
		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    rowCount++;
		}
		assertCondition(rowCount == 5);
		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setCommand()
setDataSourceName()
execute(int)
w/ -1
**/
    public void Var011()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"setDataSourceName",jndiName_);
		JDReflectionUtil.callMethod_V(crs,"execute",-1);
		failed("No SQLException thrown");
	    }
	    catch (SQLException e)
	    {
		succeeded();
	    }
	    catch (Exception e)
	    {
		failed(e,"SQLException Expected");
	    }
	}
    }
/**
getCommand()
**/
    public void Var012()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getCommand()==null);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
getCommand()
**/
    public void Var013()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		assertCondition(crs.getCommand()==select_);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
getDataSourceName()
**/
    public void Var014()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getDataSourceName()==null);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
getDataSourceName()
**/
    public void Var015()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setDataSourceName",jndiName_);
		assertCondition(crs.getDataSourceName()==jndiName_);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
getEscapeProcessing()
it appears the default is for escape processing to be on.
**/
    public void Var016()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getEscapeProcessing());
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setExcapeProcessing()
getEscapeProcessing()
**/
    public void Var017()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setEscapeProcessing(false);
		assertCondition(!crs.getEscapeProcessing());
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
getMaxFieldSize()
default is 0
**/
    public void Var018()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getMaxFieldSize() == 0);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setMaxFieldSize()
getMaxFieldSize()
**/
    public void Var019()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setMaxFieldSize(16);
		assertCondition(crs.getMaxFieldSize() == 16);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setMaxFieldSize()
getMaxFieldSize()
w/ a negative size
**/
    public void Var020()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setMaxFieldSize(-16);
		failed("No SQLException thrown");
	    }
	    catch (SQLException e)
	    {
		succeeded();
	    }
	    catch (Exception e)
	    {
		failed(e,"SQLException Expected");
	    }
	}
    }
/**
getMaxRows()
default is 0
**/
    public void Var021()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getMaxRows() == 0);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setMaxRows()
getMaxRows()
**/
    public void Var022()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setMaxRows(16);
		assertCondition(crs.getMaxRows() == 16);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setMaxRows()
getMaxRows()
w/ a negative size
**/
    public void Var023()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setMaxRows(-16);
		failed("No SQLException thrown");
	    }
	    catch (SQLException e)
	    {
		succeeded();
	    }
	    catch (Exception e)
	    {
		failed(e,"SQLException Expected");
	    }
	}
    }
/**
getPassword()
**/
    public void Var024()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getPassword() == null);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
getPassword()
setPassword()
**/
    public void Var025()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setPassword("test");
		assertCondition(crs.getPassword().equals("test"));
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**

getQueryTimeout()
**/
    public void Var026()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getQueryTimeout() == 0);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setQueryTimeout()
getQueryTimeout()
**/
    public void Var027()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setQueryTimeout(5);
		assertCondition(crs.getQueryTimeout() == 5);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setQueryTimeout()
getQueryTimeout()
w/ a negative value
**/
    public void Var028()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setQueryTimeout(-5);
		failed("No SQLException thrown");
	    }
	    catch (SQLException e)
	    {
		succeeded();
	    }
	    catch (Exception e)
	    {
		failed(e,"SQLException Expected");
	    }
	}
    }
/**
getTransactionIsolation()
**/
    public void Var029()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setTransactionIsolation()
getTransactionIsolation()
**/
    public void Var030()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		assertCondition(crs.getTransactionIsolation() == Connection.TRANSACTION_REPEATABLE_READ);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
getUrl()
**/
    public void Var031()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getUrl() == null);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setUrl()
getUrl()
**/
    public void Var032()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setUrl(baseURL_);
		assertCondition(crs.getUrl().equals(baseURL_));
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
getUsername()
**/
    public void Var033()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getUsername() == null);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setUsername()
getUsername()
**/
    public void Var034()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setUsername("JAVA");
		assertCondition(crs.getUsername().equals("JAVA"));
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
isReadOnly()
default is true
**/
    public void Var035()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.isReadOnly());
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
setReadOnly()
isReadOnly()
**/
    public void Var036()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setReadOnly(false);
		assertCondition(!crs.isReadOnly());
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
getTableName()
**/
    public void Var037()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		assertCondition(crs.getTableName() == null);
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }

/**
setTableName()
getTableName()
**/
    public void Var038()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		crs.setTableName(TABLE_);
		assertCondition(crs.getTableName().equals(TABLE_));
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }

/**
afterLast()
insertRow()
acceptChanges()
**/
    public void Var039()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setDataSourceName",jndiName_);
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"execute");
		crs.afterLast();
		crs.moveToInsertRow();
		crs.updateString(1,"SIXTH");
		crs.insertRow();
		crs.moveToCurrentRow();
		boolean check1 = crs.rowInserted();
		crs.setTableName(TABLE_);
		crs.acceptChanges();
		JDReflectionUtil.callMethod_V(crs,"execute");

		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    rowCount++;
		}
		assertCondition((rowCount == 6) && check1);

		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
afterLast()
insertRow()
cancelRowInsert()
acceptChanges()
rowInserted()
**/
    public void Var040()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setDataSourceName",jndiName_);
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"execute");
		crs.afterLast();
		crs.moveToInsertRow();
		crs.updateString(1,"SEVENTH");
		crs.insertRow();
		crs.moveToCurrentRow();
		boolean check1 = crs.rowInserted();
		crs.last();
		JDReflectionUtil.callMethod_V(crs,"cancelRowInsert");
		boolean check2 = crs.rowInserted();
		crs.setTableName(TABLE_);
		crs.acceptChanges();
		JDReflectionUtil.callMethod_V(crs,"execute");

		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    System.out.println(crs.getString(1));
		    rowCount++;
		}
		System.out.println("rowCount = " + rowCount);
		System.out.println("check1 = " + check1);
		System.out.println("check2 = " + check2);
		assertCondition((rowCount == 6) && check1 && !check2);

		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
deleteRow()
acceptChanges()
**/
    public void Var041()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setDataSourceName",jndiName_);
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"execute");
		JDReflectionUtil.callMethod_B(crs,"next");
		crs.deleteRow();
		boolean check1 = crs.rowDeleted();
		crs.setTableName(TABLE_);
		crs.acceptChanges();
		JDReflectionUtil.callMethod_V(crs,"execute");

		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    rowCount++;
		    System.out.println(crs.getString(1));
		}
		assertCondition((rowCount == 5) && check1);

		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
deleteRow()
cancelRowDelete()
acceptChanges()
**/
    public void Var042()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setDataSourceName",jndiName_);
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"execute");
		JDReflectionUtil.callMethod_B(crs,"next");
		crs.deleteRow();
		boolean check1 = crs.rowDeleted();
		crs.setShowDeleted(true);
		crs.beforeFirst();
		JDReflectionUtil.callMethod_B(crs,"next");
		JDReflectionUtil.callMethod_V(crs,"cancelRowDelete");
		boolean check2 = crs.rowDeleted();
		crs.setTableName(TABLE_);
		crs.acceptChanges();
		JDReflectionUtil.callMethod_V(crs,"execute");
		int rowCount = 0;
		while (JDReflectionUtil.callMethod_B(crs,"next"))
		{
		    rowCount++;
		    System.out.println(crs.getString(1));
		}
		assertCondition((rowCount == 5) && check1 && !check2);

		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
updateString()
cancelRowUpdates()
acceptChanges()
**/
    public void Var043()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setDataSourceName",jndiName_);
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"execute");
		JDReflectionUtil.callMethod_B(crs,"next");
		crs.updateString(1, "ZERO");
		crs.updateRow();
		boolean check1 = crs.rowUpdated();
		crs.setTableName(TABLE_);
		crs.acceptChanges();
		JDReflectionUtil.callMethod_V(crs,"execute");
		JDReflectionUtil.callMethod_B(crs,"next");
		assertCondition(crs.getString(1).equals("ZERO")  && check1);

		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }

/**
updateString()
cancelRowUpdates()
acceptChanges()
**/
    public void Var044()
    {
	if (checkNative()) {
	    try
	    {
		CachedRowSet crs = (CachedRowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2CachedRowSet");
		JDReflectionUtil.callMethod_V(crs,"setDataSourceName",jndiName_);
		JDReflectionUtil.callMethod_V(crs,"setCommand",select_);
		JDReflectionUtil.callMethod_V(crs,"execute");
		JDReflectionUtil.callMethod_B(crs,"next");
		crs.updateString(1, "FIRST");
		crs.cancelRowUpdates();
		crs.updateRow();
		boolean check1 = crs.rowUpdated();
		crs.setTableName(TABLE_);
		crs.acceptChanges();
		JDReflectionUtil.callMethod_V(crs,"execute");
		JDReflectionUtil.callMethod_B(crs,"next");
		assertCondition(crs.getString(1).equals("ZERO") && !check1);

		JDReflectionUtil.callMethod_V(crs,"close");
	    }
	    catch (Exception e)
	    {
		failed(e,"Unexpected Exception");
	    }
	}
    }
}


