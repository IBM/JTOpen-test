///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDataSourceSocketOptionsTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDataSourceSocketOptionsTestcase.java
//
// Classes:      JDDataSourceSocketOptionsTestcase
//
////////////////////////////////////////////////////////////////////////
//
//
//  
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.DataSource;

import com.ibm.as400.access.*;

import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.util.Hashtable;

import java.sql.*;

/**
Testcase JDDataSource. This tests the following
methods of the AS400JDBCDataSource class:

<ul>
  <li>getKeepAlive
  <li>getReceiveBufferSize
  <li>getSendBufferSize
  <li>getSoLinger
  <li>getSoTimeout
  <li>getTcpNoDelay
  <li>setKeepAlive
  <li>setReceiveBufferSize
  <li>setSendBufferSize
  <li>setSoLinger
  <li>setSoTimeout
  <li>setTcpNoDelay
</ul>
**/

public class JDDataSourceSocketOptionsTestcase
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDataSourceSocketOptionsTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDataSourceTest.main(newArgs); 
   }

/**
Constructor
**/
    public JDDataSourceSocketOptionsTestcase(AS400 systemObject,
                                             Hashtable namesAndVars,
                                             int runMode,
                                             FileOutputStream fileOutputStream,
                                             
                                             String password)
    {
        super (systemObject, "JDDataSourceSocketOptionsTestcase",
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
    }


/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
    }


/**
Tests the getting and setting of TCP keepalive from the DataSource.
**/
    public void Var001()
    {
        if( checkJdbc20StdExt() )
        {
            Connection connection = null;
            try
            {
                char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource dataSource = new AS400JDBCDataSource(system_, userId_, charPassword);
                PasswordVault.clearPassword(charPassword);
                dataSource.setKeepAlive(true);
                if (dataSource.getKeepAlive() != true) {
                    failed("TCP keepalive was not set.");
                    return;
                }

                connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT lstnam FROM qiws.qcustcdt");
                rs.close(); 
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
                    if( connection != null )
                        connection.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Tests the getting and setting of the socket receive buffer size.
**/
    public void Var002()
    {
        if( checkJdbc20StdExt() )
        {
            Connection connection = null;
            try
            {
                char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource dataSource = new AS400JDBCDataSource(system_, userId_, charPassword);
                PasswordVault.clearPassword(charPassword);
                dataSource.setReceiveBufferSize(32768);
                if (dataSource.getReceiveBufferSize() != 32768) {
                    failed("TCP receive buffer size not set.");
                    return;
                }

                connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT lstnam FROM qiws.qcustcdt");
                rs.close(); 
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
                    if( connection != null )
                        connection.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Tests the getting and setting of the socket send buffer size.
**/
    public void Var003()
    {
        if( checkJdbc20StdExt() )
        {
            Connection connection = null;
            try
            {
                char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource dataSource = new AS400JDBCDataSource(system_, userId_, charPassword);
                PasswordVault.clearPassword(charPassword);
                dataSource.setSendBufferSize(32768);
                if (dataSource.getSendBufferSize() != 32768) {
                    failed("TCP send buffer size not set.");
                    return;
                }

                connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT lstnam FROM qiws.qcustcdt");
                rs.close(); 
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
                    if( connection != null )
                        connection.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Tests the getting and setting of the TCP SO_LINGER socket property.
**/
    public void Var004()
    {
        if( checkJdbc20StdExt() )
        {
            Connection connection = null;
            try
            {
                char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource dataSource = new AS400JDBCDataSource(system_, userId_, charPassword);
                PasswordVault.clearPassword(charPassword);
                dataSource.setSoLinger(10);
                if (dataSource.getSoLinger() != 10) {
                    failed("TCP SO_LINGER option not set.");
                    return;
                }

                connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT lstnam FROM qiws.qcustcdt");
                rs.close(); 
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
                    if( connection != null )
                        connection.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Tests the getting and setting of the TCP SO_TIMEOUT socket option.
**/
    public void Var005()
    {
        if( checkJdbc20StdExt() )
        {
            Connection connection = null;
            try
            {
                char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource dataSource = new AS400JDBCDataSource(system_, userId_, charPassword);
                PasswordVault.clearPassword(charPassword);
                dataSource.setSoTimeout(1000); //@pdc for slower networks
                if (dataSource.getSoTimeout() != 1000) {
                    failed("TCP SO_TIMEOUT option not set.");
                    return;
                }

                connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT lstnam FROM qiws.qcustcdt");
                rs.close(); 
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
                    if( connection != null )
                        connection.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }

/**
Tests getting and setting the TCP no delay socket option.
**/
    public void Var006()
    {
        if( checkJdbc20StdExt() )
        {
            Connection connection = null;
            try
            {
                char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource dataSource = new AS400JDBCDataSource(system_, userId_, charPassword);
                PasswordVault.clearPassword(charPassword);
                dataSource.setTcpNoDelay(true);
                if (dataSource.getTcpNoDelay() != true) {
                    failed("TCP no delay socket option not set.");
                    return;
                }

                connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT lstnam FROM qiws.qcustcdt");
                rs.close(); 
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
                    if( connection != null )
                        connection.close();
                }
                catch( SQLException sq )
                {
                }
            }
        }
    }
}
