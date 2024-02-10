///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;    //@F1A
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;          //@F1A
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JD.JDSetupCollection;
import test.JD.Lob.JDLobAccess;
import test.JD.Lob.JDLobBlob;
import test.JD.Lob.JDLobBlobLocator;
import test.JD.Lob.JDLobClob;
import test.JD.Lob.JDLobClob5035;
import test.JD.Lob.JDLobClob8;
import test.JD.Lob.JDLobClobLocator;
import test.JD.Lob.JDLobClobLocator5035;
import test.JD.Lob.JDLobClobLocator8;
import test.JD.Lob.JDLobGraphicData;
import test.JD.Lob.JDLobInputStream;
import test.JD.Lob.JDLobLargeLob;
import test.JD.Lob.JDLobLargeLob2;
import test.JD.Lob.JDLobNClobLocator;
import test.JD.Lob.JDLobNClobLocator8;
import test.JD.Lob.JDLobReader;
import test.JD.Lob.JDLobRemoteClob;
import test.JD.Lob.JDLobThreshold;
import test.JD.Lob.JDLobVisibility;



/**
Test driver for the JDBC Lob classes.
**/
public class JDLobTest
extends JDTestDriver {



    /**
   * 
   */
  private static final long serialVersionUID = 1L;
    // Constants.
    public static  String COLLECTION     = "JDTESTLOB";



    /**
    Run the test as an application.  This should be called
    from the test driver's main().
    
    @param  args        The command line arguments.
    
    @exception Exception If an exception occurs.
    **/
    public static void main (String args[])
    throws Exception
    {
        runApplication (new JDLobTest (args));
    }



    /**
    Constructs an object for applets.
    
    @exception Exception If an exception occurs.
    **/
    public JDLobTest ()
    throws Exception
    {
        super();
    }



    /**
    Constructs an object for testing applications.
    
    @param      args        The command line arguments.
    
    @exception Exception If an exception occurs.
    **/
    public JDLobTest (String[] args)
    throws Exception
    {
        super (args);
    }



    /**
    Performs setup needed before running testcases.
    
    @exception Exception If an exception occurs.
    **/
    public void setup ()
    throws Exception
    {
        super.setup(); // @D1A

        // Initialization.
        String baseUrl = getBaseURL(); 
        char[] encryptedPassword = PasswordVault.getEncryptedPassword(password_);
        String password = PasswordVault.decryptPasswordLeak(encryptedPassword); 

        Connection connection = DriverManager.getConnection (baseUrl,
                                                             systemObject_.getUserId (), password);

        if (testLib_ != null) {  // @E1A
            COLLECTION = testLib_;
        }

        JDSetupCollection.create (systemObject_,
                                  connection, COLLECTION);
    }



    /**
    Performs setup needed after running testcases.
    
    @exception Exception If an exception occurs.
    **/
    public void cleanup ()
    throws Exception
    {
    }



    /**
    Cleanup - - this does not run automatically - - it is called by JDCleanup.
    **/
    public static void dropCollections(Connection c)
    {
        dropCollection(c, COLLECTION);
    }



    /**
    Simple implementation of Blob used for testing.
    **/
    public static class JDTestBlob
    implements Blob, Serializable {
        /**
       * 
       */
      private static final long serialVersionUID = 1L;
        private byte[] data_;

        public JDTestBlob (byte[] data) { data_ = data;}

        // We need this function to work now.
        public InputStream getBinaryStream () {
            return new ByteArrayInputStream(data_);
        }

        public InputStream getBinaryStream (long pos, long length) {

            return new ByteArrayInputStream(data_, (int)pos, (int) length);
        }

        public long position (byte[] pattern, long start) { return -1;}
        public long position (Blob pattern, long start) { return -1;}

        public byte[] getBytes (long start, int length)
        {
            // Not correct, but good enough for here.
            return data_;
        }

        public long length ()
        {
            return data_.length;
        }
        public int setBytes(long pos, byte[] bytes)                        //@F1A
        {                                                                  //@F1A
            // add code to test new methods                                //@F1A
            return 0;                                                      //@F1A
        }                                                                  //@F1A
        public int setBytes(long pos, byte[] bytes, int offest, int len)   //@F1A
        {                                                                  //@F1A
            // add code to test new methods                                //@F1A
            return 0;                                                      //@F1A
        }                                                                  //@F1A
        public OutputStream setBinaryStream(long pos)                      //@F1A
        {                                                                  //@F1A
            // add code to test new methods                                //@F1A
            return null;                                                   //@F1A
        }                                                                  //@F1A
        public void truncate(long len)                                     //@F1A
        {                                                                  //@F1A
            // add code to test new methods                                //@F1A
        }                                                                  //@F1A


	public void free() {} 
    }



    /**
    Simple implementation of Clob used for testing.
    **/
    public static class JDTestClob
    implements Clob, Serializable {
        /**
       * 
       */
      private static final long serialVersionUID = 1L;
        private String data_;

        public JDTestClob (String data) { data_ = data;}
        public InputStream getAsciiStream () { return null;}

        // We need this function to work now.
        public Reader getCharacterStream () {
            return new StringReader(data_);
        }


        public Reader getCharacterStream (long pos, long length) {
            return new StringReader(data_.substring((int)pos, (int)( pos + length)));
        }

        public long position (String pattern, long start) { return -1;}
        public long position (Clob pattern, long start) { return -1;}

        // Lob indexes are 1 based.  The string that is used to
        // represent the data is 0 based.  Account for that when
        // returning a substring.
        public String getSubString (long start, int length)
        {
            return data_.substring ((int) start - 1, (int) start + length - 1);
        }

        public long length ()
        {
            return data_.length ();
        }
        public int setString(long pos, String str)                        //@F1A
        {                                                                 //@F1A
            return 0;                                                     //@F1A
        }                                                                 //@F1A
        public int setString(long pos, String str, int offest, int len)   //@F1A
        {                                                                 //@F1A
            // add code to test new methods                               //@F1A
            return 0;                                                     //@F1A
        }                                                                 //@F1A
        public OutputStream setAsciiStream(long pos)                      //@F1A        
        {                                                                 //@F1A
            // add code to test new methods                               //@F1A
            return null;                                                  //@F1A
        }                                                                 //@F1A
        public Writer setCharacterStream(long pos)                        //@F1A
        {                                                                 //@F1A
            // add code to test new methods                               //@F1A
            return null;                                                  //@F1A
        }                                                                 //@F1A
        public void truncate(long len)                                    //@F1A
        {                                                                 //@F1A
            // add code to test new methods                               //@F1A
        }                                                                 //@F1A
	public void free() {} 
    }



    /**
    Creates the testcases.
    **/
    public void createTestcases2 ()
    {
    	
    	if(TestDriverStatic.pause_)
    	{ 
      		  	try 
      		  	{						
      		  		systemObject_.connectService(AS400.DATABASE);
      			}
      	     	catch (AS400SecurityException e) 
      	     	{
      	     		// TODO Auto-generated catch block
      				e.printStackTrace();
      			} 
      	     	catch (IOException e) 
      	     	{
      				// TODO Auto-generated catch block
      	     	    e.printStackTrace();
      			}
      				 	 	   
      	     	try
      	     	{
      	     	    Job[] jobs = systemObject_.getJobs(AS400.DATABASE);
      	     	    System.out.println("Host Server job(s): ");

      	     	    	for(int i = 0 ; i< jobs.length; i++)
      	     	    	{   	    	
      	     	    		System.out.println(jobs[i]);
      	     	    	}    	    
      	     	 }
      	     	 catch(Exception exc){}
      	     	    
      	     	 try 
      	     	 {
      	     	    	System.out.println ("Press ENTER to continue.");
      	     	    	System.in.read ();
      	     	 } 
      	     	 catch (Exception exc) {};   	   
    	} 
    	
    	
        addTestcase (new JDLobBlob (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

        addTestcase (new JDLobClob (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

        addTestcase (new JDLobClobLocator (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

        addTestcase (new JDLobNClobLocator (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));


        addTestcase (new JDLobBlobLocator (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

        addTestcase (new JDLobInputStream (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

        addTestcase (new JDLobReader (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_, 
                                      password_));

        addTestcase (new JDLobThreshold (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_));

        addTestcase (new JDLobGraphicData(systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));

        addTestcase (new JDLobLargeLob(systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));

	addTestcase (new JDLobLargeLob2(systemObject_,
					namesAndVars_, runMode_, fileOutputStream_, 
					password_));

	addTestcase (new JDLobVisibility(systemObject_,
					namesAndVars_, runMode_, fileOutputStream_, 
					password_));

        addTestcase (new JDLobAccess(systemObject_,
					namesAndVars_, runMode_, fileOutputStream_, 
					password_));

	addTestcase (new JDLobClobLocator8 (systemObject_,
					       namesAndVars_, runMode_, fileOutputStream_, 
					       password_));


	addTestcase (new JDLobNClobLocator8 (systemObject_,
					       namesAndVars_, runMode_, fileOutputStream_, 
					       password_));


	addTestcase (new JDLobClobLocator5035 (systemObject_,
					       namesAndVars_, runMode_, fileOutputStream_, 
					       password_));

	addTestcase (new JDLobClob5035 (systemObject_,
					       namesAndVars_, runMode_, fileOutputStream_, 
					       password_));

	addTestcase (new JDLobClob8 (systemObject_,
					       namesAndVars_, runMode_, fileOutputStream_, 
					       password_));


	addTestcase (new JDLobRemoteClob (systemObject_,
					       namesAndVars_, runMode_, fileOutputStream_, 
					       password_));

    }

}




