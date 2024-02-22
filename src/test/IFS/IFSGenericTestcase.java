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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector; 


import jcifs.smb.SmbException;
import test.IFSTests;
import test.JCIFSUtility;
import test.JTOpenTestEnvironment;
import test.Testcase;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.IFSTextFileInputStream;

public class IFSGenericTestcase extends Testcase
{
  /* public static final String FILE_SEPARATOR = System.getProperty("file.separator"); */
  /* public static final char FILE_SEPARATOR_CHAR = FILE_SEPARATOR.charAt(0); */

  public static String ifsDirName_ = "/";
  public static String ifsDirPath_ = "/" + ifsDirName_;

  public static String ifsFileName_ = "TestFile";
  public static String ifsFilePath_ = ifsDirPath_ + '/' + ifsFileName_; 
  public static String fileName_ = "File";
  public static String ifsPathName_ = ifsDirName_ + fileName_;
  
  public String dirName_=null;
  public String collection_ = "JDIFSCOL"; 

  public boolean DOS_ = false;
  public boolean OS2_ = false;
  public boolean linux_ = false ;
  public boolean NT_ = false;
  public boolean OS400_ = false;
  public boolean UNIX_ = false;
  public boolean AIX_ = false; 
  public static boolean DEBUG = false;



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
                   AS400    pwrSys
                   )
    {
        super(systemObject,testname,namesAndVars,runMode,fileOutputStream,password);
        userId_=userid;
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



   // Determine operating system we're running under
     DOS_ = JTOpenTestEnvironment.isWindows; 

     
   if (JTOpenTestEnvironment.isOS400) {
      OS400_ = true;
   }  else    {                                     
      OS400_ = false;
   }

      if (JTOpenTestEnvironment.isAIX   ||
          JTOpenTestEnvironment.isLinux)
        UNIX_ = true;



   output_.println("Running under: " + JTOpenTestEnvironment.osVersion);
   output_.println("DOS-based file structure: " + DOS_);
   output_.println("Executing " + (isApplet_ ? "applet." : "application."));
   output_.println("Linux Flag: " + JTOpenTestEnvironment.isLinux);
   output_.println("AIX Flag:   " + JTOpenTestEnvironment.isAIX); 

  
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
   dirName_ =  IFSFile.separator; 

  ifsFileName_ = "TestFile"+collection_;
  ifsDirPath_ = IFSFile.separator + ifsFileName_; 





   // Check to see if we're connected properly
   try
   {
       IFSFile file = new IFSFile(systemObject_, IFSFile.separator, "QSYS.LIB");
       if (!file.exists())
       {
         output_.println("Unable to locate QSYS.LIB in " + IFSFile.separator + " on " + systemObject_.getSystemName() + ".");
         throw new Exception("Unable to locate QSYS.LIB");
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




  public final void createDirectory(String dirName)
  {
     createDirectory(systemObject_, dirName);
  }
  
  public final void createDirectory(AS400 system, String dirName)
  {
    try
    {
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

  public final void createFile(String pathName)
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
              IFSFile file = new IFSFile(systemObject_, pathName);
              if (!file.exists())  {
                throw new Exception("JDCIFSUtility.createFile did not create "+pathName);
              }
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

               file = new IFSFile(systemObject_, pathName);
               if (!file.exists())  {
                 throw new Exception("IFSRandomAccessFile did not create "+pathName);
               }

               
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

  public final void createFile(String pathName, String data)
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

  public void createFileWriteChars(String pathName, String data)
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


  public final void createFile(String pathName, byte[] data)
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

  public final void deleteFile(String pathName) {
    try { 
      deleteFileStatus(pathName); 
    } catch (Exception e) { 
      
    }
  }
  public final boolean deleteFileStatus(String pathName) throws IOException
  {
        IFSFile file = new IFSFile(systemObject_, pathName);
        if (file.exists())        {
          boolean deleted = file.delete();
          return deleted; 
        }
        return false; 
  }
  
  public void createIFSDirectory(String dirName)
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



  public final void createIFSFile(String pathName)
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

  public final void createIFSFile(AS400 as400, String pathName)
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





  public String[] listDirectory(String pathName) throws Exception {

    if ( IFSTests.IsRunningOnOS400 ) { 
     File file2 = new File(pathName);
     String[] names2 = file2.list();
     return names2; 

    } else { 
      return JCIFSUtility.listDirectory(systemObject_.getSystemName(),
          userId_,
          encryptedPassword_,
          pathName); 
    }
  }

  public String[] listQSYSDirectory(String pathName) throws Exception {

    if ( IFSTests.IsRunningOnOS400 ) { 
     File file2 = new File(pathName);
     String[] names2 = file2.list();
     return names2; 

    } else { 
      String[] sampleStringArray = new String[0]; 
      Vector<String> answer = new Vector<String>();
      Connection c = new AS400JDBCDriver().connect(pwrSys_);
      Statement s = c.createStatement(); 
      String sql="SELECT SUBSTRING(PATH_NAME,LENGTH('"+pathName+"')+2) FROM "
          + "TABLE(QSYS2.IFS_OBJECT_STATISTICS('"+pathName+"','NO'))";
      ResultSet rs = s.executeQuery(sql); 
      while (rs.next()) { 
        String string = rs.getString(1); 
        if (string != null && string.length() > 0) { 
          answer.add(string); 
        }
      }
      String[] names2 = answer.toArray(sampleStringArray); 
      c.close(); 
      return names2; 
    }
  }

  
  public final void deleteDirectory(String pathName) 
  {
              deleteFile(pathName);
  }


  public final boolean deleteDirectoryStatus(String pathName) throws IOException {
    if (DEBUG)
      output_.print("Deleting directory tree starting at: " + pathName + "...");
    if (pathName.indexOf('\\') >= 0) {
      pathName = pathName.replace('\\', '/');
    }
    deleteFileStatus(pathName);
    IFSFile stillThere = new IFSFile(systemObject_, pathName);
    if (stillThere.exists()) {
      output_.print("Unable to cleanup.\n");
      return false;
    } else {
      if (DEBUG)
        output_.print("OK.\n");
      return true;
    }
  }

  
  public final void deleteIFSDirectory(String pathName) { 
    IFSFile file = new IFSFile(systemObject_, pathName);
    deleteIFSDirectory(file); 
  }

  public final void deleteIFSDirectory(IFSFile dir)
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

  public final void deleteIFSFile(IFSFile file)
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

  public final void deleteIFSFile(String pathName)
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



  public InputStream getNonIFSInputStream(String ifsPathName) throws FileNotFoundException, SmbException, MalformedURLException, UnknownHostException, SQLException {
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
      if (isApplet_ || JTOpenTestEnvironment.isLinux)
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
      if (isApplet_ || JTOpenTestEnvironment.isLinux)
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





