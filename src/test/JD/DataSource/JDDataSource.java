///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDataSource.java
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
// File Name:    JDDataSource.java
//
// Classes:      JDDataSource
//
////////////////////////////////////////////////////////////////////////
//
//
//                 v5r2m0t   07/24/01   kcoover     Created.
//  
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.DataSource;

import com.ibm.as400.access.*;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;


/**
Testcase JDDataSource. This tests the following
methods of the JDBC class:

<ul>
<li>
</ul>
**/

public class JDDataSource
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDataSource";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDataSourceTest.main(newArgs); 
   }

    //private
    private static AS400JDBCDataSource ds;
    private static Connection c1 = null;
    private static Connection c2 = null;
    private static Connection c3 = null;
    private static Connection c4 = null;
    private static Connection c5 = null;


/**
Constructor
**/
    public JDDataSource(AS400 systemObject,
                        Hashtable<String,Vector<String>> namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
    {
        super (systemObject, "JDDataSource",
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
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                ds = new AS400JDBCDataSource(system_, userId_, passwordChars);
 PasswordVault.clearPassword(passwordChars);
            }
            catch( Exception e )
            {
                e.printStackTrace();
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
Getting 5 connections from a pooled connection and performing some actions,
should take less time than getting five regular connections and performing the same actions.
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
                beginTime = System.currentTimeMillis();
                output_.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                c1 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c2 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c3 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c4 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c5 = ds.getConnection(systemObject_.getUserId(), passwordChars);
 PasswordVault.clearPassword(passwordChars);
                s = c1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                rs.next();
                rs.getRow();
                rs.close();
                s.close();
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                output_.println("End Time " + endTime);
                output_.println("Total Time " + totalTime);
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
Getting 2 connections from a pooled connection should take less time than
getting 2 regular connections.
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
                beginTime = System.currentTimeMillis();
                output_.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                c1 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c2 = ds.getConnection(systemObject_.getUserId(), passwordChars);
 PasswordVault.clearPassword(passwordChars);
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                output_.println("End Time " + endTime);
                output_.println("Total Time " + totalTime);
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
Getting 5 connections from a pooled connection should take less time than getting 
five regular connections.
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

                beginTime = System.currentTimeMillis();
                output_.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                c1 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c2 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c3 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c4 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c5 = ds.getConnection(systemObject_.getUserId(), passwordChars);
 PasswordVault.clearPassword(passwordChars);
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                output_.println("End Time " + endTime);
                output_.println("Total Time " + totalTime);
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
Getting 5 connections from a pooled connection and performing some actions,
should take less time than getting five regular connections and performing the same actions.
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
                beginTime = System.currentTimeMillis();
                output_.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                c1 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c2 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c3 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c4 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c5 = ds.getConnection(systemObject_.getUserId(), passwordChars);
 PasswordVault.clearPassword(passwordChars);
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
                output_.println("End Time " + endTime);
                output_.println("Total Time " + totalTime+" count="+count);
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
Getting 3 connections from a pooled connection and performing some actions,
should take less time than getting 3 regular connections and performing the same actions.
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
                beginTime = System.currentTimeMillis();
                output_.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                c1 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c2 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c3 = ds.getConnection(systemObject_.getUserId(), passwordChars);
 PasswordVault.clearPassword(passwordChars);
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
                output_.println("End Time " + endTime);
                output_.println("Total Time " + totalTime+" count="+count);
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
Getting 5 connections from a pooled connection and performing some actions,
should take less time than getting five regular connections and performing the same actions.
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
                beginTime = System.currentTimeMillis();
                output_.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                c1 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c2 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c3 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c4 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c5 = ds.getConnection(systemObject_.getUserId(), passwordChars);
 PasswordVault.clearPassword(passwordChars);
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
                output_.println("End Time " + endTime);
                output_.println("Total Time " + totalTime+" count="+count);
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
Getting 5 connections from a pooled connection and performing some actions,
should take less time than getting five regular connections and performing the same actions.
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
                beginTime = System.currentTimeMillis();
                output_.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                c1 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c2 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c3 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c4 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c5 = ds.getConnection(systemObject_.getUserId(), passwordChars);
 PasswordVault.clearPassword(passwordChars);
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
                output_.println("End Time " + endTime);
                output_.println("Total Time " + totalTime);
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
Getting 5 connections from a pooled connection and performing some actions,
should take less time than getting five regular connections and performing the same actions.

- Test note:  This is the same as variation 1.  In running this testcase, variation 1 usually
fails and this variation usually is successful.
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
                beginTime = System.currentTimeMillis();
                output_.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                c1 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c2 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c3 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c4 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c5 = ds.getConnection(systemObject_.getUserId(), passwordChars);
 PasswordVault.clearPassword(passwordChars);
                s = c1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                rs.next();
                rs.getRow();
                rs.close();
                s.close();
                endTime = System.currentTimeMillis();
                totalTime = endTime - beginTime;
                output_.println("End Time " + endTime);
                output_.println("Total Time " + totalTime);
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
Getting 6 connections.
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
                beginTime = System.currentTimeMillis();
                output_.println("Start Time " + beginTime);
          char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_); 
                c1 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c2 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c3 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c4 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c5 = ds.getConnection(systemObject_.getUserId(), passwordChars);
                c6 = ds.getConnection(systemObject_.getUserId(), passwordChars);
 PasswordVault.clearPassword(passwordChars);
                endTime = System.currentTimeMillis();
                output_.println("End Time " + endTime);
                totalTime = endTime - beginTime;
                output_.println("Total Time " + totalTime);
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
      Test clone method.
      The purpose of clone is to keep around a copy of properties for iNav in a case where the DS gets corrupted.
    **/
        public void Var010()
        {
        	  if(getDriver() != JDTestDriver.DRIVER_TOOLBOX)
        	  {
        		  notApplicable ("JDBC Toolbox Driver variation.");
                  return;
              } 
        	  
            if( checkJdbc20StdExt() )
            {
                try
                {
		    // Note:  Clone is protected in earlier versions of the toolbox driver
		    // As such, reflection must be used to call it.
		    //
		    
                	//do several clones and verify that property changes get cascaded
                	//and that original ds properties do not get changed
		        // AS400JDBCDataSource newDS1 = (AS400JDBCDataSource) ds.clone();
			
			AS400JDBCDataSource newDS1 = (AS400JDBCDataSource) JDReflectionUtil.callMethod_O(ds, "clone");
			newDS1.setBlockSize(0);
			
                	// AS400JDBCDataSource newDS2 = (AS400JDBCDataSource) newDS1.clone();
                	AS400JDBCDataSource newDS2 = (AS400JDBCDataSource) JDReflectionUtil.callMethod_O(newDS1,"clone");

                	if(newDS2.getBlockSize() != 0)
                	{
                		failed("Property was not copied in cloned DataSource:  BlockSize");
                		return;
                	}
                	
                    newDS2.setBlockSize(128);
                    if(newDS1.getBlockSize() != 0)
                    {
                		failed("Property was changed in orig DataSource:  BlockSize");
                		return;
                    }
                     
                    
                    // AS400JDBCDataSource newDS3 = (AS400JDBCDataSource) ds.clone();
                    AS400JDBCDataSource newDS3 = (AS400JDBCDataSource) JDReflectionUtil.callMethod_O(ds, "clone");
                newDS3.setCursorSensitivity("asensitive");
                // AS400JDBCDataSource newDS4 = (AS400JDBCDataSource) newDS3.clone();
                AS400JDBCDataSource newDS4 = (AS400JDBCDataSource) JDReflectionUtil.callMethod_O(newDS3, "clone");
                	
                	if(newDS4.getCursorSensitivity().equals("asensitive") == false)
                	{
                		failed("Property was not copied in cloned DataSource:  CursorSensitivity");
                		return;
                	}
                	
                    newDS4.setCursorSensitivity("insensitive");
                    if(newDS3.getCursorSensitivity().equals("asensitive") == false)
                    {
                		failed("Property was changed in orig DataSource:  CursorSensitivity");
                		return;
                    }
                	
                 succeeded();
                }
                catch( Exception e )
                {
                    failed (e, "Unexpected Exception");
                }
               
            }
        }
}
