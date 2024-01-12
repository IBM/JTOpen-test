///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSGenericTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.IFS;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Hashtable;

import jcifs.smb.SmbException;
import test.IFSTests;
import test.JCIFSUtility;
import test.Testcase;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.IFSTextFileInputStream;

public class IFSGenericTestcase extends Testcase
{
  static final String FILE_SEPARATOR = System.getProperty("file.separator");
  static final char FILE_SEPARATOR_CHAR = FILE_SEPARATOR.charAt(0);

  static String ifsDirName_ = "/";
  static String ifsDirPath_ = "/" + ifsDirName_;

  static String ifsFileName_ = "TestFile";
  static String ifsFilePath_ = ifsDirPath_ + FILE_SEPARATOR + ifsFileName_; 
  static String fileName_ = "File";
  static String ifsPathName_ = ifsDirName_ + fileName_;
  
  String mappedDrive_=null;
  String operatingSystem_=null;
  String dirName_=null;
  String collection_ = "JDIFSCOL"; 

  boolean DOS_ = false;
  boolean OS2_ = false;
  boolean linux_ = false ;
  boolean NT_ = false;
  boolean OS400_ = false;
  boolean UNIX_ = false;
  boolean AIX_ = false; 

  
  static boolean DEBUG = false;

  boolean isClassic; 


  static {
      if (System.getProperty("debug") != null) {
	  DEBUG = true; 
      }
      if (System.getProperty("DEBUG") != null) {
	  DEBUG = true; 
      } 

  } 


/**
Constructor.
**/
  public IFSGenericTestcase (AS400 systemObject,
                   String userid, 
                   String password,
                   String testname, 
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   String   driveLetter,
                   AS400    pwrSys
                   )
    {
        super(systemObject,testname,namesAndVars,runMode,fileOutputStream,password);
        mappedDrive_ = driveLetter;
        userId_=userid;
        isClassic = System.getProperty("java.vm.name").indexOf("Classic") >= 0;
	pwrSys_ = pwrSys; 
	if (systemName_ == null) {
	  systemName_ = systemObject.getSystemName(); 
	}

    }


  
  /**
  @exception  Exception  If an exception occurs.
  **/
 protected void setup()
   throws Exception
 {

   lockSystem("IFSFILE",600); 
   collection_ = IFSTests.COLLECTION;


   if (mappedDrive_ == null)
   {
       mappedDrive_ = "MAPPED DRIVE SHOULD NOT BE USED. CHANGE TESTCASE"; 
   }

   // Determine operating system we're running under
   operatingSystem_ = System.getProperty("os.name");
   if (operatingSystem_.indexOf("Windows") >= 0 ||
       operatingSystem_.indexOf("DOS") >= 0 ||
       operatingSystem_.indexOf("OS/2") >= 0)
   {
     DOS_ = true;
   }
   else
   {
     DOS_ = false;
   }

   // Are we in OS/2? If so, need different commands for deleting stuff...
   //if (operatingSystem_.indexOf("OS/2") >= 0) {
   //  OS2_ = true;
   //}

   if (operatingSystem_.indexOf("Windows NT") >= 0) {
     NT_ = true;
   }

   if (operatingSystem_.indexOf("Linux") >= 0) {
     linux_ = true;
   }

   if (operatingSystem_.equals("OS/400")) {
      OS400_ = true;
      mappedDrive_ = "/"; 
   }  else    {                                     
      OS400_ = false;
   }

      if ((operatingSystem_.equalsIgnoreCase("AIX"))   ||
          (operatingSystem_.equalsIgnoreCase("SUNOS")) ||
          (operatingSystem_.equalsIgnoreCase("LINUX")))
        UNIX_ = true;

    if (operatingSystem_.indexOf("AIX") >= 0) {    
      AIX_ = true;                                 
    }                                              


   output_.println("Running under: " + operatingSystem_);
   output_.println("DOS-based file structure: " + DOS_);
   output_.println("Executing " + (isApplet_ ? "applet." : "application."));
   output_.println("NT Flag:    " + NT_);
   output_.println("Linux Flag: " + linux_);
   output_.println("AIX Flag:   " + AIX_); 

  
   ifsDirName_ = IFSFile.separator + "IFSTEST."+ System.getProperty("user.name") + IFSFile.separator ;
   {
   IFSFile file = new IFSFile(systemObject_, ifsDirName_);
   if (! file.exists()) {
     file.mkdir(); 
   }
   }
   
   String someOtherFilename = ifsDirName_ + "someOtherFile"; 
   {
     IFSFile file = new IFSFile(systemObject_, someOtherFilename); 
     if (! file.exists()) {
       file.createNewFile(); 
     }
   }
   fileName_ = "File"+collection_; 
   ifsPathName_ = ifsDirName_ + fileName_;
   dirName_ = convertToPCName("");

  ifsFileName_ = "TestFile"+collection_;
  ifsDirPath_ = FILE_SEPARATOR + ifsFileName_; 





   // Check to see if we're connected properly
   try
   {
     if (isApplet_)
     {
       IFSFile file = new IFSFile(systemObject_, IFSFile.separator, "QSYS.LIB");
       if (!file.exists())
       {
         output_.println("Unable to locate QSYS.LIB in " + IFSFile.separator + " on " + systemObject_.getSystemName() + ".");
         throw new Exception("Unable to locate QSYS.LIB");
       }
     }
     else
     {
       File file = new File(dirName_, "QSYS.LIB");
       if (!file.exists())
       {
         output_.println("WARNING:  Unable to locate QSYS.LIB in " + dirName_ + ".");
         output_.println("WARNING:  Check to make sure that " + mappedDrive_ + " is mapped to the root directory on " + systemObject_.getSystemName() + ".");
         output_.println("WARNING:  Unable to locate QSYS.LIB");


	 // TODO:  Use JCIFS instead. 
       }
     }
   }
   catch(Exception e)
   {
     e.printStackTrace(output_);
     throw e;
   }
 }



protected void cleanup() throws Exception { 
  unlockSystem(); 
}


  final String convertToPCName(String name)
  {



    String result = name;
    if (name.indexOf(mappedDrive_) != 0)
    {
      result = mappedDrive_;
      if (name.indexOf("/") == 0 || name.indexOf("\\") == 0)
        result += name;
      else
        result += File.separator + name;
    }
    if (result.startsWith("//")) result = result.substring(1);

    if (File.separatorChar != '/')
    {
      result = result.replace('/', File.separatorChar);
    }


    return result;
  }



  final void createDirectory(String dirName)
  {
     createDirectory(systemObject_, dirName);
  }
  
  final void createDirectory(AS400 system, String dirName)
  {
    try
    {
        // Always create using IFS file and do not rely on mapped file.
        if (dirName.indexOf(FILE_SEPARATOR_CHAR) >= 0) { 
           dirName = dirName.replace(FILE_SEPARATOR_CHAR, '/');
        }
        IFSFile dir = new IFSFile(system, dirName);
        if (!dir.exists())
        {
          dir.mkdirs();
        }
    }
    catch(Exception e)
    {
      if (DEBUG)
        e.printStackTrace(output_);
    }
  }

  final void createFile(String pathName)
  {
    try
    {
      if (isApplet_ || IFSTests.IsRunningOnOS400)
      {
        IFSFile file = new IFSFile(systemObject_, pathName);
        if (file.exists())
        {
          file.delete();
        }
        IFSRandomAccessFile raf =
          new IFSRandomAccessFile(systemObject_, pathName, "rw");
        raf.close();
      }
      else
      {
        // Just use JCIFS to write the file 
        /* 
	      File file = new File(convertToPCName(pathName));
	      if (file.exists())
	      {
		  file.delete();
	      }
	      RandomAccessFile raf = new RandomAccessFile(file, "rw");
	      raf.close();
          */
           try { 
	      JCIFSUtility.createFile(systemObject_.getSystemName(),
				      userId_,
				      encryptedPassword_,
				      pathName);
           } catch (Exception e) {
             String message = e.toString();
             // If hitting weird access denied error.  Just use IFSRAF to create it. 
             if (message.indexOf("Access is denied")>= 0) {
               IFSFile file = new IFSFile(systemObject_, pathName);
               if (file.exists())
               {
                 file.delete();
               }
               IFSRandomAccessFile raf =
                 new IFSRandomAccessFile(systemObject_, pathName, "rw");
               raf.close();
               
             } else {
               throw e; 
             }
           }
	   
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

/**
Create a file to be used for the testcases.
**/
  public void createFileInDirectory(String directory, String file)
  {
     try
     {
        IFSFile aDirectory = new IFSFile(systemObject_, directory);
        // create the directory
        if (aDirectory.mkdir())
        {
           IFSFile aFile = new IFSFile(systemObject_, file);
           aFile.createNewFile(); 
          
        }
        else     // Else the create directory failed.
        {
           if (aDirectory.exists())
           {
              if (aDirectory.isDirectory())
              {
                 // create the file.
                 IFSFile aFile = new IFSFile(systemObject_, file);
                 aFile.createNewFile(); 
              }
           }
           else
              System.out.println("Setup - create file - failed");
        }
     }
     catch(Exception e)
     {
        System.out.println(e);
     }
  }

  final void createFile(String pathName, String data)
  {
    try
    {
      if (isApplet_ || IFSTests.IsRunningOnOS400)
      {
        IFSFile file = new IFSFile(systemObject_, pathName);
        if (file.exists())
        {
          file.delete();
        }
        IFSRandomAccessFile raf =
          new IFSRandomAccessFile(systemObject_, pathName, "rw");
        raf.writeChars(data);
        raf.close();
      }
      else
      {

        // Just use JCIFS to write to the file 
        
        JCIFSUtility.createFile(systemObject_.getSystemName(),
                                userId_,
                                encryptedPassword_,
                                pathName, 
                                data); 

        
        /* 
        File file = new File(convertToPCName(pathName));
        if (file.exists())
        {
          file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.writeChars(data);
        raf.close();
        */ 
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  void createFileWriteChars(String pathName, String data)
  {
    try
    {
      {
        IFSFile file = new IFSFile(systemObject_, pathName);
        if (file.exists())
        {
          file.delete();
        }
        IFSRandomAccessFile raf =
          new IFSRandomAccessFile(systemObject_, pathName, "rw");
        raf.writeChars(data);
        raf.close();
      }
    }
    catch(Exception e)
    { e.printStackTrace(); }
  }


  final void createFile(String pathName, byte[] data)
  {
    try
    {
      if (isApplet_ || IFSTests.IsRunningOnOS400)
      {
        IFSFile file = new IFSFile(systemObject_, pathName);
        if (file.exists())
        {
          file.delete();
        }
        IFSRandomAccessFile raf =
          new IFSRandomAccessFile(systemObject_, pathName, "rw");
        raf.write(data);
        raf.close();
      }
      else
      {
        // Just use JCIFS to write to the file 
        
        JCIFSUtility.createFile(systemObject_.getSystemName(),
                                userId_,
                                encryptedPassword_,
                                pathName, 
                                data); 

        /* 
        File file = new File(convertToPCName(pathName));
        if (file.exists())
        {
          file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.write(data);
        raf.close();
        */ 
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  final void deleteFile(String pathName)
  {
    try
    {
      if (isApplet_ || IFSTests.IsRunningOnOS400)
      {
        IFSFile file = new IFSFile(systemObject_, pathName);
        if (file.exists())
        {
          file.delete();
        }
      }
      else
      {
        JCIFSUtility.deleteFile(systemObject_.getSystemName(),
            userId_,
            encryptedPassword_,
            pathName); 
        /* 
        File file = new File(convertToPCName(pathName));
        if (file.exists())
        {
          file.delete();
        }
        */ 
      }
    }
    catch(Exception e)
    {}
  }

  
  void createIFSDirectory(String dirName)
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, dirName);
      if (!dir.exists())
      {
        dir.mkdir();
      }
    }
    catch(Exception e)
    {}
  }



  final void createIFSFile(String pathName)
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, pathName);
      if (file.exists())
      {
        file.delete();
      }
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, pathName, "rw");
      raf.close();
    }
    catch(Exception e)
    {
      System.out.println("Exception in createFile: <" + e.getMessage() + ">");
    }
  }

  final void createIFSFile(AS400 as400, String pathName)
  {
    try
    {
      IFSFile file = new IFSFile(as400, pathName.replace(File.separatorChar, IFSFile.separatorChar));
      if (file.exists())
      {
        file.delete();
      }
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, pathName.replace(File.separatorChar, IFSFile.separatorChar), "rw");
        int i = 0;
        for (; i < 256; i++)
        {
          raf.write ((byte)i);
        }

      raf.close();
    }
    catch(Exception e)
    {
      System.out.println("Exception in createFile: <" + e.getMessage() + ">");
      e.printStackTrace(); 
    }
  }





  String[] listDirectory(String pathName) throws Exception {

    if ( IFSTests.IsRunningOnOS400 ) { 
     File file2 = new File(convertToPCName(pathName));
     String[] names2 = file2.list();
     return names2; 

    } else { 
      return JCIFSUtility.listDirectory(systemObject_.getSystemName(),
          userId_,
          encryptedPassword_,
          pathName); 
    }
  }


  final boolean deleteDirectory(String pathName)
  {

   // Try to delete using CIFS first
      try {

	  return JCIFSUtility.deleteDirectory(systemObject_.getSystemName(),
					      userId_,
					      encryptedPassword_,
					      pathName); 


      } catch (Exception pass1Exception) {
	  pass1Exception.printStackTrace();

	  String pcPathName = convertToPCName(pathName);
	  if (DEBUG) output_.print("Deleting directory tree starting at: "+pcPathName+"...");
	  try
	  {
      // Try to delete it like normal first.
	      deleteFile(pathName);

	      if (!isApplet_)
	      {
	// Try to delete it using a command process.
		  Runtime myRuntime = Runtime.getRuntime();
		  Process myProcess = null;
		  int rc = -1;  //@A1C

		  if (DOS_ && !OS2_)
		  {
	  //myProcess = myRuntime.exec("command.com /c deltree /y "+pcPathName);
		      myProcess = myRuntime.exec("cmd.exe /c rmdir /s /q "+pcPathName);
	  //rc = myProcess.waitFor();              //@A1D

	  // Note: On Windows NT, the waitFor() after deltree will occasionally hang,
	  // so we do the following instead.         @A1A
		      for (int i = 0; i < 20 && rc != 0; i++)  //@A1A
		      {                                        //@A1A
			  try                                   //@A1A
			  {                                     //@A1A
			      rc = myProcess.exitValue();        //@A1A
			  } catch (Exception e) {}              //@A1A
			  Thread.sleep(1000);                   //@A1A
		      }                                        //@A1A
		  }
		  else if (DOS_ && OS2_)
		  {
		      myProcess = myRuntime.exec("cmd.exe /c xdel "+pcPathName+"/s /e /y");
		      rc = myProcess.waitFor();
		      myProcess = myRuntime.exec("cmd.exe /c rd "+pcPathName);
		      rc = myProcess.waitFor();
		  }
		  else // assume UNIX file structure if not DOS or OS/2
		  {
		      myProcess = myRuntime.exec("rm -r "+pcPathName);
		      rc = myProcess.waitFor();
		  }
	      }

	      if (isApplet_ || IFSTests.IsRunningOnOS400 || linux_)
	      {
		  IFSFile stillThere = new IFSFile(systemObject_, pathName);
		  if (stillThere.exists())
		  {
		      output_.print("Unable to cleanup.\n");
		      return false;
		  }
		  else
		  {
		      if (DEBUG) output_.print("OK.\n");
		      return true;
		  }
	      }
	      else
	      {
		  File stillThere = new File(pcPathName);
		  if (stillThere.exists())
		  {
		      output_.print("Unable to cleanup.\n");
		      return false;
		  }
		  else
		  {
		      if (DEBUG) output_.print("OK.\n");
		      return true;
		  }
	      }
	  }
	  catch(Exception e)
	  {
	      if (DEBUG) e.printStackTrace(output_);
	      return false;
	  }
      }
  }

  final void deleteIFSDirectory(IFSFile dir)
  {
    if (dir == null) return;
    try
    {
      IFSFile dirContents[] = dir.listFiles();
      for (int i=0; i<dirContents.length; i++) {
        deleteIFSFile(dirContents[i]);
      }
      dir.delete();
    }
    catch(Exception e)
    {e.printStackTrace();}
  }

  final void deleteIFSFile(IFSFile file)
  {
    try
    {
      if (file.isDirectory()) deleteIFSDirectory(file);
      else if (file.exists())
      {
        file.delete();
      }
    }
    catch(Exception e)
    {
 System.out.println("Exception in createFile: <" + e.getMessage() + ">");
e.printStackTrace();
}
  }

  final void deleteIFSFile(String pathName)
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, pathName);
      if (file.exists())
      {
        file.delete();
      }
    }
    catch(Exception e)
    {e.printStackTrace();}
  }



  InputStream getNonIFSInputStream(String ifsPathName) throws FileNotFoundException, SmbException, MalformedURLException, UnknownHostException, SQLException {
      InputStream fis = null; 
      if (IFSTests.IsRunningOnOS400) {
	  fis = new FileInputStream(ifsPathName); 
      } else {

          fis = JCIFSUtility.getFileInputStream(systemObject_.getSystemName(),
                                userId_,
                                encryptedPassword_,
                                ifsPathName);

      } 

      return fis;

  }



  public void checkReadWriteAccess(String ifsPathNameX) throws Exception {
      if (isApplet_)
      {
          IFSFileInputStream is2 = new IFSFileInputStream(systemObject_,	ifsPathNameX);
          IFSFileOutputStream is3 =
            new IFSFileOutputStream(systemObject_, ifsPathNameX);
          is2.close();
          is3.close();
      }
      else
      {
          InputStream is2 = JCIFSUtility.getFileInputStream(systemName_, userId_, encryptedPassword_, ifsPathNameX); 

          JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_, ifsPathNameX); 
          is2.close();
          
      }
  }



  public void checkReadAccess(String ifsPathNameX) throws Exception { 
      if (isApplet_)
      {
          IFSFileInputStream is2 = new IFSFileInputStream(systemObject_, ifsPathNameX);
          is2.close();
      }
      else
      {
        
          InputStream is2 = JCIFSUtility.getFileInputStream(systemName_, userId_, encryptedPassword_, ifsPathNameX); 
          is2.close();
      }
  }



  public void checkWriteAccess(String ifsPathNameX) throws Exception { 
      // if (isApplet_ || NT_ || linux_)
      // {
          IFSFileOutputStream os =
            new IFSFileOutputStream(systemObject_, ifsPathNameX);
          os.close();
      // }
      // else
      //{
          // FileOutputStream os = new FileOutputStream(convertToPCName(ifsPathNameX));
      //    OutputStream os = JCIFSUtility.getFileOutputStream(systemName_, userId_, password_, ifsPathNameX); 
      //    os.close();
      // }
  }



  public boolean checkFileExists(String ifsPathNameX) throws Exception { 
      if (isApplet_ || linux_)
      {
  	IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
  	return (file.exists());
      }
      else
      {
        boolean exists = JCIFSUtility.fileExists(systemName_, userId_, encryptedPassword_, ifsPathNameX);
        return exists; 
  	// File file = new File(convertToPCName(ifsPathNameX));
  	// return (file.exists());
      }
  }



  public long checkFileLength(String ifsPathNameX) throws Exception { 
      if (isApplet_ || linux_)
      {
  	IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
  	return (file.length());
      }
      else
      {
        return JCIFSUtility.fileLength(systemName_, userId_, encryptedPassword_, ifsPathNameX);   
  	// File file = new File(convertToPCName(ifsPathNameX));
  	// return (file.length());
      }
  }

  public boolean checkExpectedRead2(String ifsPathNameX, int x1, int x2) throws Exception  {  
    boolean passed = false; 
    if (isApplet_)
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      passed = is.read() == x1 && is.read() == x2;
      is.close();
    }
    else
    {
      passed = JCIFSUtility.checkExpectedRead2(systemName_, userId_, encryptedPassword_, ifsPathNameX, x1, x2); 
      // FileInputStream is = new FileInputStream(convertToPCName(ifsPathNameX));
      // passed = (is.read() == x1 && is.read() == x2);
      // is.close();
    }
    return passed; 
    }

  public DataInput openDataInput(String ifsPathNameX, String mode) throws Exception  {  
    return JCIFSUtility.openDataInput(systemName_, userId_, encryptedPassword_, ifsPathNameX, mode); 
    }

//  public DataOutput openDataOutput(String ifsPathNameX, String mode) throws Exception  {  
//    return JCIFSUtility.openDataOutput(systemName_, userId_, encryptedPassword_, ifsPathNameX, mode); 
//     }
  
  
}





