///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionPoolDataSource.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionPoolDataSource.java
//
// Classes:      JDConnectionPoolDataSource
//
////////////////////////////////////////////////////////////////////////

package test.JD.DataSource;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnectionPoolDataSource;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;



/**
Testcase JDConnectionPoolDataSource. This tests the following
methods of the JDBC class:

<ul>
<li>
</ul>
**/

public class JDConnectionPoolDataSource
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionPoolDataSource";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDataSourceTest.main(newArgs); 
   }

    //private
    private AS400JDBCConnectionPoolDataSource cpd;
    private ConnectionPoolDataSource ds;
    private Connection c1 = null;
    private Connection c2 = null;
    private Connection c3 = null;
    private Connection c4 = null;
    private Connection c5 = null;


/**
Constructor
**/
    public JDConnectionPoolDataSource(AS400 systemObject,
                                      Hashtable namesAndVars,
                                      int runMode,
                                      FileOutputStream fileOutputStream,
                                      
                                      String password)
    {
        super (systemObject, "JDConnectionPoolDataSource",
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
        if( checkJdbc20StdExt() )
        {
            try
            {
                if(isToolboxDriver())
                {    
                    cpd = new AS400JDBCConnectionPoolDataSource();
                    cpd.setServerName(systemObject_.getSystemName());
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                    c1 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c2 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c3 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c4 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c5 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
		    PasswordVault.clearPassword(passwordChars); 
                }
                else if(getDriver() == JDTestDriver.DRIVER_NATIVE)
                {    
                    ds = (ConnectionPoolDataSource) JDReflectionUtil.createObject("DB2ConnectionPoolDataSource");
                    JDReflectionUtil.callMethod_V(ds,  "setDatabaseName", systemObject_.getSystemName());
                    c1 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c2 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c3 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c4 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c5 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                }

            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
            finally
            {
                c1.close();
                c2.close();
                c3.close();
                c4.close();
                c5.close();
                System.out.println();
            }
        }
    }


/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if( checkJdbc20StdExt() )
        {
            if( c1 != null )
                c1.close();
            if( c2 != null )
                c2.close();
            if( c3 != null )
                c3.close();
            if( c4 != null )
                c4.close();
            if( c5 != null )
                c5.close();
         }
    }


/**
Getting 5 connections from a pooled connection and performing some actions.
**/
    public void Var001()
    {
        if( checkJdbc20StdExt() )
        {
            try
            {
                long beginTime;
                long endTime;
                long totalTime;
                Statement s;
                ResultSet rs;
                
                if(isToolboxDriver())
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                    c1 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c2 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c3 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c4 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c5 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
		    PasswordVault.clearPassword(passwordChars); 
                }
                else 
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
                    c1 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c2 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c3 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c4 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c5 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                }                
                s = c1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                rs.next();
                rs.getRow();
                rs.close();
                s.close();
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                System.out.println("End Time " + endTime);
                System.out.println("Total Time " + totalTime);
                succeeded();
            }
            catch( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
            finally
            {
                try
                {
                    if( c1 != null )
                        c1.close();
                    if( c2 != null )
                        c2.close();
                    if( c3 != null )
                        c3.close();
                    if( c4 != null )
                        c4.close();
                    if( c5 != null )
                        c5.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Getting 2 connections from a pooled connection.
**/
    public void Var002()
    {
        if( checkJdbc20StdExt() )
        {
            try
            {
                long beginTime;
                long endTime;
                long totalTime;
                if(isToolboxDriver())
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                    c1 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c2 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
		    PasswordVault.clearPassword(passwordChars); 
                }
                else 
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
                    c1 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c2 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                }
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                System.out.println("End Time " + endTime);
                System.out.println("Total Time " + totalTime);
                succeeded();
            }
            catch( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
            finally
            {
                try
                {
                    if( c1 != null )
                        c1.close();
                    if( c2 != null )
                        c2.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Getting 5 connections from a pooled connection.
**/
    public void Var003()
    {
        if( checkJdbc20StdExt() )
        {
            try
            {
                long beginTime;
                long endTime;
                long totalTime;

                if(isToolboxDriver())
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                    c1 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c2 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c3 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c4 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c5 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
		    PasswordVault.clearPassword(passwordChars); 
                }
                else 
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
                    c1 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c2 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c3 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c4 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c5 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                }
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                System.out.println("End Time " + endTime);
                System.out.println("Total Time " + totalTime);
                succeeded();
            }
            catch( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
            finally
            {
                try
                {
                    if( c1 != null )
                        c1.close();
                    if( c2 != null )
                        c2.close();
                    if( c3 != null )
                        c3.close();
                    if( c4 != null )
                        c4.close();
                    if( c5 != null )
                        c5.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Getting 5 connections from a pooled connection and performing some actions.
**/
    public void Var004()
    {
        if( checkJdbc20StdExt() )
        {
            try
            {
                long beginTime;
                long endTime;
                long totalTime;
                int count = 0;
                Statement s;
                ResultSet rs;
                if(isToolboxDriver())
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                    c1 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c2 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c3 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c4 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c5 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
		    PasswordVault.clearPassword(passwordChars); 
                }
                else 
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
                    c1 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c2 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c3 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c4 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c5 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                }
                s = c1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                while( rs.next() )
                {
                    rs.getRow();
                    count++;
                }
                rs.close();
                s.close();
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                System.out.println("End Time " + endTime);
                System.out.println("Total Time " + totalTime+" count="+count);
                succeeded();
            }
            catch( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
            finally
            {
                try
                {
                    if( c1 != null )
                        c1.close();
                    if( c2 != null )
                        c2.close();
                    if( c3 != null )
                        c3.close();
                    if( c4 != null )
                        c4.close();
                    if( c5 != null )
                        c5.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Getting 3 connections from a pooled connection and performing some actions.
**/
    public void Var005()
    {
        if( checkJdbc20StdExt() )
        {
            try
            {
                long beginTime;
                long endTime;
                long totalTime;
                int count = 0;
                Statement s;
                ResultSet rs;
                if(isToolboxDriver())
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                    c1 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c2 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c3 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
		    PasswordVault.clearPassword(passwordChars); 
                }
                else 
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
                    c1 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c2 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c3 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                }
                s = c1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                while( rs.next() )
                {
                    rs.getRow();
                    count++;
                }
                rs.close();
                s.close();
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                System.out.println("End Time " + endTime);
                System.out.println("Total Time " + totalTime+" count="+count);
                succeeded();
            }
            catch( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
            finally
            {
                try
                {
                    if( c1 != null )
                        c1.close();
                    if( c2 != null )
                        c2.close();
                    if( c3 != null )
                        c3.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Getting 5 connections from a pooled connection and performing some actions.
**/
    public void Var006()
    {
        if( checkJdbc20StdExt() )
        {
            try
            {
                long beginTime;
                long endTime;
                long totalTime;
                int count = 0;
                Statement s;
                Statement s1;
                ResultSet rs;
                ResultSet rs1;
                if(isToolboxDriver())
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                    c1 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c2 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c3 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c4 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c5 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
		    PasswordVault.clearPassword(passwordChars); 
                }
                else 
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
                    c1 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c2 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c3 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c4 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c5 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                }
                s = c1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                while( rs.next() )
                {
                    rs.getRow();
                    count++;
                }
                rs.close();
                s.close();
                s1 = c3.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs1 = s1.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                while( rs1.next() )
                {
                    rs1.getRow();
                    count++;
                }
                rs1.close();
                s1.close();
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                System.out.println("End Time " + endTime);
                System.out.println("Total Time " + totalTime+" count="+count);
                succeeded();
            }
            catch( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
            finally
            {
                try
                {
                    if( c1 != null )
                        c1.close();
                    if( c2 != null )
                        c2.close();
                    if( c3 != null )
                        c3.close();
                    if( c4 != null )
                        c4.close();
                    if( c5 != null )
                        c5.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Getting 5 connections from a pooled connection and performing some actions.
**/
    public void Var007()
    {
        if( checkJdbc20StdExt() )
        {
            try
            {
                long beginTime;
                long endTime;
                long totalTime;
                Statement s;
                ResultSet rs;
                if(isToolboxDriver())
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                    c1 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c2 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c3 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c4 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c5 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
		    PasswordVault.clearPassword(passwordChars); 
                }
                else 
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
                    c1 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c2 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c3 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c4 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c5 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                }
                s = c1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                rs.next();
                rs.getRow();
                rs.absolute(12);
                rs.getRow();
                rs.close();
                s.close();
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                System.out.println("End Time " + endTime);
                System.out.println("Total Time " + totalTime);
                succeeded();
            }
            catch( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
            finally
            {
                try
                {
                    if( c1 != null )
                        c1.close();
                    if( c2 != null )
                        c2.close();
                    if( c3 != null )
                        c3.close();
                    if( c4 != null )
                        c4.close();
                    if( c5 != null )
                        c5.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Getting 5 connections from a pooled connection and performing some actions
**/
    public void Var008()
    {
        if( checkJdbc20StdExt() )
        {
            try
            {
                long beginTime;
                long endTime;
                long totalTime;
                Statement s;
                ResultSet rs;
                if(isToolboxDriver())
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                    c1 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c2 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c3 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c4 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c5 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
		    PasswordVault.clearPassword(passwordChars); 
                }
                else 
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
                    c1 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c2 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c3 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c4 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c5 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                }
                s = c1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                rs.next();
                rs.getRow();
                rs.close();
                s.close();
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                System.out.println("End Time " + endTime);
                System.out.println("Total Time " + totalTime);
                succeeded();
            }
            catch( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
            finally
            {
                try
                {
                    if( c1 != null )
                        c1.close();
                    if( c2 != null )
                        c2.close();
                    if( c3 != null )
                        c3.close();
                    if( c4 != null )
                        c4.close();
                    if( c5 != null )
                        c5.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Getting 6 connections from a pooled connection w/ only 5 connections should work.
**/
    public void Var009()
    {
        if( checkJdbc20StdExt() )
        {
            try
            {
                long beginTime;
                long endTime;
                long totalTime;
                Connection c6;
                if(isToolboxDriver())
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                    c1 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c2 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c3 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c4 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c5 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
                    c6 = cpd.getPooledConnection(systemObject_.getUserId(), passwordChars).getConnection();
		    PasswordVault.clearPassword(passwordChars); 
                }
                else 
                {   
                    beginTime = System.currentTimeMillis();
                    System.out.println("Start Time " + beginTime);
                    c1 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c2 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c3 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c4 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c5 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                    c6 = ds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))).getConnection();
                }
                endTime = System.currentTimeMillis();
                System.out.println("End Time " + endTime);
                totalTime = endTime - beginTime;
                System.out.println("Total Time " + totalTime);
                succeeded();
                c6.close();
            }
            catch( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
            finally
            {
                try
                {
                    if( c1 != null )
                        c1.close();
                    if( c2 != null )
                        c2.close();
                    if( c3 != null )
                        c3.close();
                    if( c4 != null )
                        c4.close();
                    if( c5 != null )
                        c5.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }



    /**
     * Make sure the cursor holdability (true)  is still correct when reusing a connection
     * See CPS 9KZFRD. 
     */
    public void Var010() {
      int i = 0;
      StringBuffer sb = new StringBuffer();
      int errorCount = 0;  
      try { 

        javax.sql.ConnectionPoolDataSource cpds; 
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
           AS400JDBCConnectionPoolDataSource ds1 = new AS400JDBCConnectionPoolDataSource();
           ds1.setServerName(systemObject_.getSystemName());
           ds1.setTransactionIsolation("read committed");
           sb.append("ds1.setAutoCommit(false)\n"); 
           ds1.setAutoCommit(false); 
           sb.append("ds1.setCursorHold(true)\n"); 
           ds1.setCursorHold(true); 
          cpds = ds1; 
        } else if (getDriver() == JDTestDriver.DRIVER_NATIVE){ 
          ConnectionPoolDataSource ds1 = (ConnectionPoolDataSource) JDReflectionUtil.createObject("DB2ConnectionPoolDataSource");

          JDReflectionUtil.callMethod_V(ds1,  "setDatabaseName", systemObject_.getSystemName());
          JDReflectionUtil.callMethod_V(ds1,  "setTransactionIsolation", "read committed");
          sb.append("ds1.setAutoCommit(false)\n"); 
          JDReflectionUtil.callMethod_V(ds1,  "setAutoCommit",false); 
          sb.append("ds1.setCursorHold(true)\n"); 
          JDReflectionUtil.callMethod_V(ds1,  "setCursorHold",true); 
          cpds = ds1; 
        } else {
           notApplicable("Toolbox or native driver testcases only ");
           return; 
        }
        PooledConnection pooledConnection = cpds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))); 
        for (i = 0; (errorCount < 5) && (i < 50); i++ ) { 
          sb.append("i="+i+"\n"); 
          Connection c = pooledConnection.getConnection();
          boolean autoCommitSetting = c.getAutoCommit();
          sb.append("autocommit setting is "+autoCommitSetting+"\n"); 
          if (autoCommitSetting == true) { 
            errorCount++;  
            sb.append("** FAILED ** autocommit setting is incorrect\n"); 
          }
          int holdability = c.getHoldability(); 
          if (holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            sb.append("holdability = ResultSet.CLOSE_CURSORS_AT_COMMIT\n");
            sb.append("*** FAILED ** holdability setting is incorrect\n");
            errorCount++ ; 
          } else if (holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT) {
            sb.append("holdability = ResultSet.HOLD_CURSORS_OVER_COMMIT\n");
          } else {
            sb.append("** WARNING ** holdability = "+holdability); 
          
          }
          Statement s = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs = s.executeQuery("Select * from sysibm.sqltables");
          rs.next(); 
          c.commit(); 
          rs.next();
          c.close(); 
        } 
        assertCondition(errorCount == 0,sb); 
      } catch (Exception e) { 
        failed(e, sb.toString());  
      }
    }

    
    /**
     * Make sure the cursor holdability (false) is still correct when reusing a connection
     * See CPS 9KZFRD. 
     */
    public void Var011() {
      int i = 0;
      StringBuffer sb = new StringBuffer();
      int errorCount = 0; 
      try { 

        javax.sql.ConnectionPoolDataSource cpds; 
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
           AS400JDBCConnectionPoolDataSource ds1 = new AS400JDBCConnectionPoolDataSource();
           ds1.setServerName(systemObject_.getSystemName());
           ds1.setTransactionIsolation("read committed");
           sb.append("ds1.setAutoCommit(false)\n"); 
           ds1.setAutoCommit(false); 
           sb.append("ds1.setCursorHold(false)\n"); 
           ds1.setCursorHold(false); 
          cpds = ds1; 
        } else if (getDriver() == JDTestDriver.DRIVER_NATIVE){ 
          ConnectionPoolDataSource ds1 = (ConnectionPoolDataSource) JDReflectionUtil.createObject("DB2ConnectionPoolDataSource");

          JDReflectionUtil.callMethod_V(ds1,  "setDatabaseName", systemObject_.getSystemName());
          JDReflectionUtil.callMethod_V(ds1,  "setTransactionIsolation", "read committed");
          sb.append("ds1.setAutoCommit(false)\n"); 
          JDReflectionUtil.callMethod_V(ds1,  "setAutoCommit",false); 
          sb.append("ds1.setCursorHold(false)\n"); 
          JDReflectionUtil.callMethod_V(ds1,  "setCursorHold",false); 
          cpds = ds1; 
        } else {
           notApplicable("Toolbox or native driver testcases only ");
           return; 
        }
        PooledConnection pooledConnection = cpds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))); 
        for (i = 0; (errorCount <10) && ( i < 50); i++ ) { 
          sb.append("i="+i+"\n"); 
          Connection c = pooledConnection.getConnection();
          boolean autoCommitSetting = c.getAutoCommit();
          sb.append("autocommit setting is "+autoCommitSetting+"\n"); 
          if (autoCommitSetting == true) { 
            errorCount++;  
            sb.append("** FAILED ** autocommit setting is incorrect\n"); 
          }
          int holdability = c.getHoldability(); 
          if (holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            sb.append("holdability = ResultSet.CLOSE_CURSORS_AT_COMMIT\n");
          } else if (holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT) {
            sb.append("holdability = ResultSet.HOLD_CURSORS_OVER_COMMIT\n");
            sb.append("*** FAILED ** holdability setting is incorrect\n");
            errorCount++; 
          } else {
            sb.append("*** FAILED ** holdability = "+holdability+"\n"); 
            errorCount++; 
          
          }
          Statement s = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs = s.executeQuery("Select * from sysibm.sqltables");
          rs.next(); 
          c.commit();
          try { 
            rs.next();
            sb.append("*** FAILED ** Exception not thrown on rs.next()\n");
            errorCount++; 
          } catch (SQLException e) { 
            String errorMessage = e.toString(); 
            if (errorMessage.indexOf("Cursor state not valid") >= 0) {
              sb.append("Correct exception thrown for rs.next()\n");
            } else {
              sb.append("*** FAILED ** wrong exception thrown "+errorMessage+"\n"); 
              printStackTraceToStringBuffer(e, sb);
              errorCount++; 
            }
          }
          c.close(); 
        } 
        assertCondition(errorCount == 0,sb); 
      } catch (Exception e) { 
        failed(e, sb.toString());  
      }
    }
    
    
    /**
     * Make sure the cursor holdability (true)  is still correct when reusing a connection
     * and it was set to false in the connection. 
     * See CPS 9KZFRD. 
     */
    public void Var012() {
      int i = 0;
      StringBuffer sb = new StringBuffer();
      int errorCount = 0; 
      try { 

        javax.sql.ConnectionPoolDataSource cpds; 
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
           AS400JDBCConnectionPoolDataSource ds1 = new AS400JDBCConnectionPoolDataSource();
           ds1.setServerName(systemObject_.getSystemName());
           ds1.setTransactionIsolation("read committed");
           sb.append("ds1.setAutoCommit(false)\n"); 
           ds1.setAutoCommit(false); 
           sb.append("ds1.setCursorHold(true)\n"); 
           ds1.setCursorHold(true); 
          cpds = ds1; 
        } else if (getDriver() == JDTestDriver.DRIVER_NATIVE){ 
          ConnectionPoolDataSource ds1  = (ConnectionPoolDataSource) JDReflectionUtil.createObject("DB2ConnectionPoolDataSource");
          JDReflectionUtil.callMethod_V(ds1,  "setDatabaseName", systemObject_.getSystemName());

          JDReflectionUtil.callMethod_V(ds1,  "setDatabaseName",systemObject_.getSystemName());
          JDReflectionUtil.callMethod_V(ds1,  "setTransactionIsolation", "read committed");
          sb.append("ds1.setAutoCommit(false)\n"); 
          JDReflectionUtil.callMethod_V(ds1,  "setAutoCommit",false); 
          sb.append("ds1.setCursorHold(true)\n"); 
          JDReflectionUtil.callMethod_V(ds1,  "setCursorHold",true); 
          cpds = ds1; 
        } else {
           notApplicable("Toolbox or native driver testcases only ");
           return; 
        }
        PooledConnection pooledConnection = cpds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))); 
        for (i = 0; errorCount < 10 && i < 50; i++ ) { 
          sb.append("i="+i+"\n"); 
          Connection c = pooledConnection.getConnection();
          boolean autoCommitSetting = c.getAutoCommit();
          sb.append("autocommit setting is "+autoCommitSetting+"\n"); 
          if (autoCommitSetting == true) { 
            errorCount++; 
            sb.append("** FAILED ** autocommit setting is incorrect\n"); 
          }
          int holdability = c.getHoldability(); 
          if (holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            sb.append("holdability = ResultSet.CLOSE_CURSORS_AT_COMMIT\n");
            sb.append("*** FAILED ** holdability setting is incorrect\n");
            errorCount++; 
          } else if (holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT) {
            sb.append("holdability = ResultSet.HOLD_CURSORS_OVER_COMMIT\n");
          } else {
            sb.append("** WARNING ** holdability = "+holdability); 
          
          }
          sb.append("Setting holdability to CLOSE_CURSORS_AT_COMMIT\n"); 
          c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
          c.close(); 
        } 
        assertCondition(errorCount == 0 ,sb); 
      } catch (Exception e) { 
        failed(e, sb.toString());  
      }
    }

    
    /**
     * Make sure the cursor holdability (false) is still correct when reusing a connection
     * and the connection has changed the value. 
     * See CPS 9KZFRD. 
     */
    public void Var013() {
      int i = 0;
      StringBuffer sb = new StringBuffer();
      int errorCount = 0; 
      try { 

        javax.sql.ConnectionPoolDataSource cpds; 
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
           AS400JDBCConnectionPoolDataSource ds1 = new AS400JDBCConnectionPoolDataSource();
           ds1.setServerName(systemObject_.getSystemName());
           ds1.setTransactionIsolation("read committed");
           sb.append("ds1.setAutoCommit(false)\n"); 
           ds1.setAutoCommit(false); 
           sb.append("ds1.setCursorHold(false)\n"); 
           ds1.setCursorHold(false); 
          cpds = ds1; 
        } else if (getDriver() == JDTestDriver.DRIVER_NATIVE){ 
          ConnectionPoolDataSource ds1 = (ConnectionPoolDataSource) JDReflectionUtil.createObject("DB2ConnectionPoolDataSource");

          JDReflectionUtil.callMethod_V(ds1,  "setDatabaseName", systemObject_.getSystemName());
          JDReflectionUtil.callMethod_V(ds1,  "setTransactionIsolation", "read committed");
          sb.append("ds1.setAutoCommit(false)\n"); 
          JDReflectionUtil.callMethod_V(ds1,  "setAutoCommit",false); 
          sb.append("ds1.setCursorHold(false)\n"); 
          JDReflectionUtil.callMethod_V(ds1,  "setCursorHold",false); 
          cpds = ds1; 
        } else {
           notApplicable("Toolbox or native driver testcases only ");
           return; 
        }
        PooledConnection pooledConnection = cpds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))); 
        for (i = 0; i < 50; i++ ) { 
          sb.append("i="+i+"\n"); 
          Connection c = pooledConnection.getConnection();
          int holdability = c.getHoldability(); 
          if (holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            sb.append("holdability = ResultSet.CLOSE_CURSORS_AT_COMMIT\n");
          } else if (holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT) {
            sb.append("holdability = ResultSet.HOLD_CURSORS_OVER_COMMIT\n");
            sb.append("*** FAILED ** holdability setting is incorrect\n");
            errorCount++; 
          } else {
            sb.append("** WARNING ** holdability = "+holdability); 
          
          }
          sb.append("Setting holdability to HOLD_CURSORS_OVER_COMMIT\n"); 
          c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT); 
          c.close(); 
        } 
        assertCondition(errorCount == 0,sb); 
      } catch (Exception e) { 
        failed(e, sb.toString());  
      }
    }
    
    

    public static final int SET_OPERATION_NONE = 0; 
    public static final int SET_OPERATION_TRANSACTION_ISOLATION = 1; 
    public static final int SET_OPERATION_SCHEMA = 2; 
    public static final int SET_OPERATION_AUTO_COMMIT = 3; 
    public static final int SET_OPERATION_READONLY = 4; 

    public void testStringDataSourceProperty(String dataSourceSetMethod,
        String dataSourceSetValue,
        int setOperation,
        String setValue, 
        String connectionGetMethod,
        String expectedGetValue) { 
      testStringDataSourceProperty(dataSourceSetMethod, dataSourceSetValue, 
          setOperation, setValue, 
          false,
          connectionGetMethod, expectedGetValue);
    }

    public void testStringDataSourceProperty(String dataSourceSetMethod,
					     String dataSourceSetValue,
					     int setOperation,
					     String setValue, 
					     boolean ignoreSetException, 
					     String connectionGetMethod,
					     String expectedGetValue) { 

	int i = 0;
	StringBuffer sb = new StringBuffer();
      int errorCount = 0;  
      try { 

        javax.sql.ConnectionPoolDataSource cpds; 
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
           AS400JDBCConnectionPoolDataSource ds1 = new AS400JDBCConnectionPoolDataSource();
           ds1.setServerName(systemObject_.getSystemName());

          cpds = ds1; 
        } else if (getDriver() == JDTestDriver.DRIVER_NATIVE){ 
          ConnectionPoolDataSource ds1 = (ConnectionPoolDataSource) JDReflectionUtil.createObject("DB2ConnectionPoolDataSource");

          cpds = ds1; 
        } else {
           notApplicable("Toolbox or native driver testcases only ");
           return; 
        }
      sb.append("Setting using " + dataSourceSetMethod + "("
          + dataSourceSetValue + ")\n");

      if (dataSourceSetValue.equals("true")) {
          boolean setValueBoolean = true;
          JDReflectionUtil.callMethod_V(cpds, dataSourceSetMethod,
              setValueBoolean);
      } else if ( dataSourceSetValue.equals("false")) {
          boolean setValueBoolean = true;
          JDReflectionUtil.callMethod_V(cpds, dataSourceSetMethod,
              setValueBoolean);

        } else {
          JDReflectionUtil.callMethod_V(cpds, dataSourceSetMethod,
              dataSourceSetValue);
        }
      PooledConnection pooledConnection = cpds.getPooledConnection(systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_))); 
        for (i = 0; errorCount < 10 && i < 50; i++ ) { 
          sb.append("i="+i+"\n"); 
          Connection c = pooledConnection.getConnection();
	  String retrievedValue="NOTSET";
	  Object value = JDReflectionUtil.callMethod_O(c, connectionGetMethod);
	  if (value instanceof String) {
	      retrievedValue = (String) value; 
        } else if (value instanceof Integer) {
          retrievedValue = value.toString();
        } else if (value instanceof Boolean) {
          retrievedValue = value.toString();
        } else {
          sb.append("Could not handle " + value + " of type "
              + value.getClass().getName() + "\n");
          errorCount++;
        }
        if (!expectedGetValue.equals(retrievedValue)) {
          errorCount++;
          sb.append("From " + connectionGetMethod + " got " + retrievedValue
              + " sb " + expectedGetValue + "\n");
        } else { 
          sb.append("From " + connectionGetMethod + " got " + retrievedValue
              + " as expected\n");
          
        }

        try {
          switch (setOperation) {
          case SET_OPERATION_NONE:
            break;
          case SET_OPERATION_TRANSACTION_ISOLATION:
            sb.append("Calling c.setTransactionIsolation(" + setValue + ")\n");
            c.setTransactionIsolation(Integer.parseInt(setValue));
            break;
          case SET_OPERATION_SCHEMA:
            sb.append("Calling c.setSchema(" + setValue + ")\n");
            JDReflectionUtil.callMethod_O(c, "setSchema", setValue);
            break;
          case SET_OPERATION_AUTO_COMMIT:
            sb.append("Calling c.setAutoCommit(" + setValue + ")\n");
            c.setAutoCommit( (new Boolean(setValue)).booleanValue());
            break;
          case SET_OPERATION_READONLY:
            sb.append("Calling c.setReadOnly(" + setValue + ")\n");
            c.setReadOnly(( new Boolean(setValue)).booleanValue());
            break;

          default:
            errorCount++;
            sb.append("Error unknown setOperation " + setOperation + "\n");
          }
        } catch (Exception e) {
          if (!ignoreSetException) {
            throw e;
          }
        }

          c.close(); 
        } 
        assertCondition(errorCount == 0,sb); 
      } catch (Exception e) { 
        failed(e, sb.toString());  
      }
    }

  public void Var014() {
    testStringDataSourceProperty("setTransactionIsolation", "none", SET_OPERATION_NONE, null,
        "getTransactionIsolation", "0");
  }

  public void Var015() {
    testStringDataSourceProperty("setTransactionIsolation",
        "read uncommitted", SET_OPERATION_NONE, null, "getTransactionIsolation", "1");
  }

  public void Var016() {
    testStringDataSourceProperty("setTransactionIsolation", "read committed", SET_OPERATION_NONE, null,
        "getTransactionIsolation", "2");
  }

  public void Var017() {
    testStringDataSourceProperty("setTransactionIsolation", "repeatable read", SET_OPERATION_NONE, null,
        "getTransactionIsolation", "4");
  }

  public void Var018() {
    testStringDataSourceProperty("setTransactionIsolation", "serializable", SET_OPERATION_NONE, null,
        "getTransactionIsolation", "8");
  }

  public void Var019() {
    testStringDataSourceProperty("setTransactionIsolation", "none", SET_OPERATION_TRANSACTION_ISOLATION, "8",
        "getTransactionIsolation", "0");
  }

  public void Var020() {
    testStringDataSourceProperty("setTransactionIsolation",
        "read uncommitted", SET_OPERATION_TRANSACTION_ISOLATION, "8", "getTransactionIsolation", "1");
  }

  public void Var021() {
    testStringDataSourceProperty("setTransactionIsolation", "read committed", SET_OPERATION_TRANSACTION_ISOLATION, "8",   "getTransactionIsolation", "2");
  }

  public void Var022() {
    testStringDataSourceProperty("setTransactionIsolation", "repeatable read", SET_OPERATION_TRANSACTION_ISOLATION, "8",
        "getTransactionIsolation", "4");
  }

  public void Var023() {
    testStringDataSourceProperty("setTransactionIsolation", "serializable", SET_OPERATION_TRANSACTION_ISOLATION, "4",
        "getTransactionIsolation", "8");
  }


  public void Var024() {
    testStringDataSourceProperty("setLibraries", "QGPL", SET_OPERATION_NONE, "",
        "getSchema", "QGPL");
  }

  public void Var025() {
    testStringDataSourceProperty("setLibraries", "QGPL", SET_OPERATION_SCHEMA, "QSYS",
        "getSchema", "QGPL");
  }
  
  public void Var026() {
    // Make sure default stays
    testStringDataSourceProperty("setTransactionIsolation", "serializable", SET_OPERATION_SCHEMA, "QSYS",
        "getSchema", systemObject_.getUserId().toUpperCase());
  }
  

  /* Make sure the default auto commit value is good */ 
  public void Var027() {
    testStringDataSourceProperty("setLibraries", "QGPL", SET_OPERATION_NONE, "",
        "getAutoCommit", "true");
  }

  /* Make sure default auto commit value persists */   

  public void Var028() {
    testStringDataSourceProperty("setLibraries", "QGPL", SET_OPERATION_AUTO_COMMIT, "false",
        "getAutoCommit", "true");
  }

  /* Make sure auto commit true is maintained */ 
  public void Var029() {
    testStringDataSourceProperty("setAutoCommit", "true", SET_OPERATION_NONE, "",
        "getAutoCommit", "true");
  }

  /* Make sure auto commit true is maintained  */   

  public void Var030() {
    testStringDataSourceProperty("setAutoCommit", "true", SET_OPERATION_AUTO_COMMIT, "false",
        "getAutoCommit", "true");
  }


  /* Make sure auto commit false is maintained */ 
  public void Var031() {
    testStringDataSourceProperty("setAutoCommit", "false", SET_OPERATION_NONE, "",
        "getAutoCommit", "false");
  }

  

  
  /* Make sure auto commit true is maintained  */   

  public void Var032() {
    testStringDataSourceProperty("setAutoCommit", "false", SET_OPERATION_AUTO_COMMIT, "true",
        "getAutoCommit", "false");
  }


  /* Make sure the default access value is good */ 
  public void Var033() {
    testStringDataSourceProperty("setLibraries", "QGPL", SET_OPERATION_NONE, "",
        "isReadOnly", "false");
  }

  /* Make sure default access value persists */   

  public void Var034() {
    testStringDataSourceProperty("setLibraries", "QGPL", SET_OPERATION_READONLY, "true",
        "isReadOnly", "false");
  }

  /* Make sure access all is maintained */ 
  public void Var035() {
    testStringDataSourceProperty("setAccess", "all", SET_OPERATION_NONE, "",
        "isReadOnly", "false");
  }

  /* Make sure access all is maintained  */   

  public void Var036() {
    testStringDataSourceProperty("setAccess", "all", SET_OPERATION_READONLY, "true",
        "isReadOnly", "false");
  }


  /* Make sure access read only  is maintained */ 
  public void Var037() {
    testStringDataSourceProperty("setAccess", "read only", SET_OPERATION_NONE, "",
        "isReadOnly", "true");
  }

  /* Make sure auto commit true is maintained  */   

  public void Var038() {
    testStringDataSourceProperty("setAccess", "read only", SET_OPERATION_READONLY, "false", true,
        "isReadOnly", "true");
  }

  
  
  
  
}
