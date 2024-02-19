///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FTPTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import test.MiscAH.AS400FTPBeans;
import test.MiscAH.AS400FTPTestcase;
import test.MiscAH.FTPBeans;
import test.MiscAH.FTPModeTestcase;
import test.MiscAH.FTPQuickVerification;
import test.MiscAH.FTPReconnect;
import test.MiscAH.FTPTestcase;

import com.ibm.as400.access.*;




public class FTPTest extends TestDriver
{
   AS400 PwrSys_ = null;
   String initialToken_ = null;
   public static final boolean DEBUG = false;


   public static void main(String args[])
   {
      try
      {

         System.out.println();
         System.out.println("Welcome to the FTP test cases! ");
         System.out.println();
         System.out.println("Considerations for the FTP testcases: ");
         System.out.println("  1) UserIDs are case sensitive (even at times on the AS/400). ");
         System.out.println("     The test cases use exactly what you type after -uid so type carefully. ");
         System.out.println();
         System.out.println("  2) -pwrsys format is uid,pwd and -directory TOKEN.  A CD TOKEN is done before ");
         System.out.println("     many variations to get in the right target directory.  Examples: ");
         System.out.println("     Unix:   token = ~uid ");
         System.out.println("     AS/400: token = / ");
         System.out.println();
         System.out.println("  3) In cmvc, the test directory has FTPTestDirArchive.zip. ");
         System.out.println("     This zip file has files and directories used in list and get/put tests. ");
         System.out.println("     Unzip this file into TWO PLACES: ");
         System.out.println("     a) The current directory on the client. ");
         System.out.println("     b) The directory pointed to by the -directory token of note 2 above ");
         System.out.println("     Result -- FTPTestDir is a directory in the current directory and ");
         System.out.println("     in the target directory.  Remember that case is important in Unix.");
         System.out.println("     Run FTPTest from within the parent directory of your FTPTestDir directory.");
         System.out.println();
         System.out.println("  4) Note: If you send the files in FTPTestDirArchive.zip separately to the iSeries,");
         System.out.println("     transfer them in 'binary' rather than 'ascii' mode, to preserve file sizes.");
         System.out.println();


         runApplication (new FTPTest(args));

      }
      catch (Exception e)
      {
         System.out.println("Program terminated abnormally.");
         e.printStackTrace();
      }

         System.exit(0);
   }





/**
This ctor used for applets.
@exception Exception Initialization errors may cause an exception.
**/
   public FTPTest()
          throws Exception
   {
      super();
   }





/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
   public FTPTest(String[] args)
          throws Exception
   {
      super(args);
   }





/**
Creates Testcase objects for all the testcases in this component.
**/
   public void createTestcases()
   {
      AS400 PwrSys_ = null;

      // Instantiate all testcases to be run.
      // boolean allTestcases = (namesAndVars_.size() == 0);
      
      
    	if ((pwrSys_.getUserId()).equals(""))
        {               	
          	System.out.println("\nERROR: -pwrSys option not specified. Many " +
            "variations may fail.");
            
        }
    	else
    	{
          char[] decryptedPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_); 
          PwrSys_ = new AS400( systemObject_.getSystemName(), pwrSysUserID_, decryptedPassword);
          PasswordVault.clearPassword(decryptedPassword);
 		
    	}	
    	
    	if (directory_ != null)
        {      
    		initialToken_ = directory_;        	       
        }
    	else
    	{
    		System.out.println("\nERROR: -directory option not specified. Many " +
            "variations may fail.");
    	}
    	
    	
    	//Changed misc_ to pwrSys_ so all test drivers are consistent.   

      /*// Setup the power user if provided.
      if (misc_ != null)
      {
         StringTokenizer miscTok = new StringTokenizer(misc_, ",");
         String uid = miscTok.nextToken();
         String pwd = miscTok.nextToken();
         initialToken_ = miscTok.nextToken();
         System.out.println(initialToken_);

         try
         {
            PwrSys_ = new AS400(systemObject_.getSystemName(), uid, pwd);
            PwrSys_.setGuiAvailable(false);
            PwrSys_.getVRM();
         }
         catch (Exception e)
         {
            System.out.println("could not connect power system ");
            e.printStackTrace();
            PwrSys_ = null;
         }
      }*/

    Testcase[] testcases =
    {
      new FTPTestcase (systemObject_,
                       namesAndVars_, runMode_,
                       fileOutputStream_, password_,
                       userId_, initialToken_),

      new FTPModeTestcase (systemObject_,
                           namesAndVars_, runMode_,
                           fileOutputStream_,  password_,
                           userId_, initialToken_, FTP.ACTIVE_MODE),

      new FTPReconnect (systemObject_,
                        namesAndVars_, runMode_,
                        fileOutputStream_,  password_,
                        userId_, initialToken_, PwrSys_),

      new FTPBeans (systemObject_,
                    namesAndVars_, runMode_,
                    fileOutputStream_,  password_,
                    userId_, initialToken_),

      new AS400FTPTestcase (systemObject_,
                            namesAndVars_, runMode_,
                            fileOutputStream_,  password_,
                            userId_, initialToken_, PwrSys_),

      new AS400FTPBeans (systemObject_,
                         namesAndVars_, runMode_,
                         fileOutputStream_,  password_,
                         userId_, initialToken_),

      new FTPQuickVerification (systemObject_,
                                namesAndVars_, runMode_,
                                fileOutputStream_,  password_,
                                userId_, initialToken_, PwrSys_)
    };

    // Added following loop so that other parms (e.g. isNative_, isLocal_, onAS400_) are
    // available within the testcase.
    for (int i = 0; i < testcases.length; ++i)                     //@A1
    {
      testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_,
                                         password_, proxy_, mustUseSockets_, isNative_,
                                         isLocal_, onAS400_, namesAndVars_, runMode_,
                                         fileOutputStream_,  pwrSysUserID_,
                                         pwrSysPassword_);         //@A1
      addTestcase(testcases[i]);                                   //@A1
    }

/*
    addTestcase (new FTPTestcase (systemObject_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_, password_,
                                  userId_, initialToken_));

    addTestcase (new FTPModeTestcase (systemObject_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_, password_,
                                  userId_, initialToken_, FTP.ACTIVE_MODE));

    addTestcase (new FTPReconnect (systemObject_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_, password_,
                                  userId_, initialToken_));

    addTestcase (new FTPBeans (systemObject_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_, password_,
                                  userId_, initialToken_));

    addTestcase (new AS400FTPTestcase (systemObject_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_, password_,
                                  userId_, initialToken_, PwrSys_));

    addTestcase (new AS400FTPBeans (systemObject_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_, password_,
                                  userId_, initialToken_));

    addTestcase (new FTPQuickVerification (systemObject_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_, password_,
                                  userId_, initialToken_, PwrSys_));
*/

    // Check for invalid testcase names.
    for (Enumeration<String> e = namesAndVars_.keys (); e.hasMoreElements (); ) {
      String name = e.nextElement ();
      System.out.println ("Testcase " + name + " not found.");
    }

   }


   /**
    Performs setup needed before running testcases.
    @exception  Exception  If an exception occurs.
    **/
   public void setup() throws Exception                       // @A2A
   {


     if ((pwrSys_.getUserId()).equals(""))
     {               	
       System.out.println("\nERROR: -pwrSys option not specified. Many " +
                          "variations may fail.");

     }
     else
     {
       char[] decryptedPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_); 
       PwrSys_ = new AS400( systemObject_.getSystemName(), pwrSysUserID_, decryptedPassword);
       PasswordVault.clearPassword(decryptedPassword);

       CommandCall cmd = new CommandCall(PwrSys_);
       initialToken_ = directory_;

       // Verify that there's an FTPTestDir subdirectory in the current directory.
       File localFtpTestDir = new File("FTPTestDir");
       if (!localFtpTestDir.exists() || !localFtpTestDir.isDirectory()) {

           // Try to extract the test directory from test/FTPTestDirArchive.zip
           try {
             System.out.println("Attempting to extract test/FTPTestDirArchive.zip");

             StringBuffer sb = new StringBuffer();
             InputStream is = JDRunit.loadResourceIfExists("test/FTPTestDirArchive.zip", sb);
             if (is == null) {
               
               System.out.println("Unable to find resource " + sb.toString());
               throw new Exception("Unable to find resource");
             }

             // Look for local file first
             // Otherwise get as a resource
	       ZipInputStream zis = new ZipInputStream(is);
	       
	       ZipEntry entry = zis.getNextEntry(); 
	       while (entry != null ) {
		   if (entry.isDirectory()) {
		       String dirname = entry.getName();
		       File dirFile = new File(dirname);
		       if (! dirFile.exists()) {
			   System.out.println("Creating "+dirFile); 
			   dirFile.mkdirs(); 
		       }
		   } else {
		       String filename = entry.getName();
		       OutputStream os = new FileOutputStream(filename);
		       System.out.println("Creating "+filename); 
		       byte[] buffer = new byte[200];
		       int bytesRead = 0;
		       bytesRead = zis.read(buffer); 
		       while (bytesRead > 0) {	
			   os.write(buffer, 0, bytesRead); 
			   bytesRead = zis.read(buffer); 
		       } 
		       os.close();
		       
		   }
		   
		   entry = zis.getNextEntry(); 
	       }
	       zis.close(); 
	   } catch (Exception e) {
	       e.printStackTrace(); 
	       System.out.println("\nERROR: Local directory does not contain a FTPTestDir subdirectory tree. Many variations may fail.");
	   }
       }

       /// If on OS/400 make sure the stuff is unpacked in '/'
       if (JTOpenTestEnvironment.isOS400) { 
	   localFtpTestDir = new File("/FTPTestDir");
	   if (!localFtpTestDir.exists() || !localFtpTestDir.isDirectory()) {

	   // Try to extract the test directory from test/FTPTestDirArchive.zip
	       try {
		   System.out.println("Attempting to extract test/FtPTestDirArchive.zip to /"); 
		   ZipFile zipFile = new ZipFile( "test/FTPTestDirArchive.zip");
		   Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();
		   while (entries.hasMoreElements()) {
		       ZipEntry entry = (ZipEntry) entries.nextElement();
		       if (entry.isDirectory()) {
			   String dirname = "/"+entry.getName();
			   File dirFile = new File(dirname);
			   if (! dirFile.exists()) {
			       System.out.println("Creating "+dirFile); 
			       dirFile.mkdirs(); 
			   }
		       } else {
			   InputStream is = zipFile.getInputStream(entry);
			   String filename = "/"+entry.getName();
			   OutputStream os = new FileOutputStream(filename);
			   System.out.println("Creating "+filename); 
			   byte[] buffer = new byte[200];
			   int bytesRead = 0;
			   bytesRead = is.read(buffer); 
			   while (bytesRead > 0) {	
			       os.write(buffer, 0, bytesRead); 
			       bytesRead = is.read(buffer); 
			   } 
			   os.close();
			   is.close(); 

		       } 
		   }

	       } catch (Exception e) {
		   e.printStackTrace(); 
		   System.out.println("\nERROR: Local directory does not contain a FTPTestDir subdirectory tree. Many variations may fail.");
	       }
	   }
       }

       try
       {
         // Following was suggested by TCP Developers to prevent
         // "425 Not able to open data connection." errors
	   // changed from 2 to 16 to prevent hang in V7R3
         boolean cmdRc = cmd.run("CHGTCPA TCPCLOTIMO(16)");         
         if (cmdRc != true)
         {
           // Show the messages
           AS400Message[] messagelist = cmd.getMessageList();
           for (int i = 0; i < messagelist.length; ++i)
           {
             // Show each message.
             System.out.println(messagelist[i].getText());
           }
         }
       }
       catch (Exception e)
       {
         System.out.println("Setup failed " + e);
         e.printStackTrace();
       }

     }

     //Changed misc_ to pwrSys_ so all test drivers are consistent.   


     /*StringTokenizer miscTok = new StringTokenizer(misc_, ",");
      String uid = miscTok.nextToken();
      String pwd = miscTok.nextToken();

      AS400 PwrSys_ = null;
      PwrSys_ = new AS400(systemObject_.getSystemName(), uid, pwd);
      CommandCall cmd = new CommandCall(PwrSys_);

      try
      {
      // Following was suggested by TCP Developers to prevent
      // "425 Not able to open data connection." errors
      boolean cmdRc = cmd.run("CHGTCPA TCPCLOTIMO(16)");         // @A2A
      if (cmdRc != true)
      {
      // Show the messages
      AS400Message[] messagelist = cmd.getMessageList();
      for (int i = 0; i < messagelist.length; ++i)
      {
      // Show each message.
      System.out.println(messagelist[i].getText());
      }
      }
      }
      catch (Exception e)
      {
      System.out.println("Setup failed " + e);
      e.printStackTrace();
      }
      */

   }
}


