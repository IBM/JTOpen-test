///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSCopyTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.IFS;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSRandomAccessFile;


/**
Test write methods for IFSFileInputStream, IFSFileOutputStream, and
IFSRandomAccessFile.
**/
public class IFSCopyTestcase extends IFSGenericTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSCopyTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }
  private IFSFile sourceFile_;  // file to copy
  private static final int SOURCE_FILE_LENGTH = 256;  // number of bytes


/**
Constructor.
**/
  public IFSCopyTestcase (AS400 systemObject,
                   String userid, 
                   String password,
                   Hashtable<String,Vector<String>> namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSCopyTestcase",  
            namesAndVars, runMode, fileOutputStream,  pwrSys);
    }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    super.setup(); 
    try { 

      // Create a file to copy.

      @SuppressWarnings("resource")
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      byte[] data = new byte[SOURCE_FILE_LENGTH];
      int i = 0;
      for (; i < data.length; i++)
      {
        data[i] = (byte) i;
        os.write(data[i]);
      }
      os.close();
      // Verify that the file got written correctly.
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      i = 0;
      while (i < data.length && raf.readByte() == data[i])
        i++;
      if (i != data.length) {
        output_.println("Failed to initialize file " + ifsPathName_);
        throw new Exception("Failed to initialize file " + ifsPathName_);
      }
      sourceFile_ = new IFSFile(systemObject_, ifsPathName_);

    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
  }

    /**
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
      deleteIFSFile(sourceFile_.getPath());
    }



  boolean filesMatch(IFSFile file1, IFSFile file2)
    throws IOException, AS400SecurityException
  {
    if (file1.isDirectory()) return directoriesMatch(file1, file2);

    long length1 = file1.length();
    if (length1 != file2.length()) {
      output_.println("Length of " + file1.getPath() + ": " + file1.length());
      output_.println("Length of " + file2.getPath() + ": " + file2.length());
      return false;
    }
    IFSFileInputStream stream1 = new IFSFileInputStream(file1);
    if (stream1.available() != length1) {
      output_.println("stream1.available() != length1");
      stream1.close(); 
      return false;
    }
    IFSFileInputStream stream2 = new IFSFileInputStream(file2);
    if (stream2.available() != length1) {
      output_.println("stream2.available() != length1");
      stream1.close(); 
      stream2.close(); 
      return false;
    }
    for (int i=0; i<length1; i++) {
      if (DEBUG) {
        int byte1 = stream1.read();
        int byte2 = stream2.read();
        ///output_.println("Byte " + i + ": " + byte1 + " ; " + byte2);
        if (byte1 != byte2) {
          stream1.close(); 
          stream2.close(); 
          return false;
        }
      }
      else {
        if (stream1.read() != stream2.read()) {
          stream1.close(); 
          stream2.close(); 
          return false;
        }
      }
      
    }
    stream1.close();
    stream2.close(); 
    return true;
  }


 boolean directoriesMatch(IFSFile dir1, IFSFile dir2)
    throws IOException, AS400SecurityException
  {
    if (!dir2.isDirectory()) {
      output_.println(dir2.getPath() + " is not a directory.");
    }
    long length1 = dir1.length();
    if (length1 != dir2.length()) {
      output_.println("FYI: Directory lengths mismatch: " + dir1.getPath() + " (" + length1 + ") , " + dir2.getPath() + " (" + dir2.length() + ")");
      ///return false;
    }
    IFSFile[] dir1Contents = dir1.listFiles();
    IFSFile[] dir2Contents = dir2.listFiles();
    if (DEBUG && dir1Contents.length == 0) {
      output_.println(dir1.getPath() + " contains " + dir1Contents.length + " files.");
      output_.println(dir2.getPath() + " contains " + dir2Contents.length + " files.");
    }
    if (dir1Contents.length != dir2Contents.length) {
      output_.println(dir1.getPath() + " contains " + dir1Contents.length + " files.");
      output_.println(dir2.getPath() + " contains " + dir2Contents.length + " files.");
      return false;
    }
    for (int i=0; i<dir1Contents.length; i++) {
      if (DEBUG) {
        output_.println("dir1Contents["+i+"], dir2Contents["+i+"] == " + dir1Contents[i] +" ," + dir2Contents[i]);
      }
      if (!filesMatch(dir1Contents[i], dir2Contents[i])) return false;
    }

    return true;
  }



/**
Copy a file to a nonexistent file, and check that the copied file matches the original.
**/
  public void Var001()
  {
    String targetPath = ifsPathName_ + "var001";
    try
    {
      sourceFile_.copyTo(targetPath);
      IFSFile targetFile = new IFSFile(systemObject_, targetPath);
      assertCondition(targetFile.length() == SOURCE_FILE_LENGTH &&
                      filesMatch(sourceFile_,targetFile));
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally {
      deleteIFSFile(targetPath);
    }
  }

/**
Copy a file to an existing file, verify that the file gets overwritten.
**/
  public void Var002()
  {
    String targetPath = ifsPathName_ + "var002";
    try
    {
      // Write the second file.
      @SuppressWarnings("resource")
      IFSFileOutputStream os = new IFSFileOutputStream(systemObject_, targetPath);
      byte[] data = new byte[256];
      int i = 255;
      for (; i > -1; i--)
      {
        data[i] = (byte) i;
        os.write(data[i]);
      }
      os.close();
      sourceFile_.copyTo(targetPath);
      IFSFile targetFile = new IFSFile(systemObject_, targetPath);
      assertCondition(targetFile.length() == SOURCE_FILE_LENGTH &&
                      filesMatch(sourceFile_,targetFile));
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally {
      deleteIFSFile(targetPath);
    }
  }

////**
///Copy a file to an existing file, specifying "no replace",
///verify that the copy fails.
///Note: The "no replace" option is only supported for OS/400 V5R3 and higher.
///**/
///  public void Var003()
///  {
///    if (systemVRM_ < 0x00050300) {  // Is the system pre-V5R3
///      notApplicable("The no-replace option is not supported prior to V5R3.");
///      return;
///    }
///    String targetPath = ifsPathName_ + "var003";
///    try
///    {
///      // Write the second file.
///      IFSFileOutputStream os = new IFSFileOutputStream(systemObject_, targetPath);
///      byte[] data = new byte[256];
///      int i = 255;
///      for (; i > -1; i--)
///      {
///        data[i] = (byte) i;
///        os.write(data[i]);
///      }
///      os.close();
///      // Attempt to copy, "no replace".
///      sourceFile_.copyTo(targetPath, false);
///      failed("Exception didn't occur.");
///    }
///    catch(Exception e)
///    {
///      assertExceptionIs(e, "ExtendedIOException", "Directory entry exists.");
///    }
///    finally {
///      deleteFile(targetPath);
///    }
///  }

/**
Ensure the NullPointerException is thrown if argument of
IFSFile.copyTo(String) is null.
**/
  public void Var003()
  {
    try
    {
      sourceFile_.copyTo(null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "path");
    }
  }


////**
///Ensure the NullPointerException is thrown if first argument of
///IFSFile.copyTo(String, boolean) is null.
///**/
///  public void Var004()
///  {
///    try
///    {
///      sourceFile_.copyTo(null, false);
///      failed("Exception didn't occur.");
///    }
///    catch(Exception e)
///    {
///      assertExceptionIs(e, "NullPointerException", "path");
///    }
///  }


/**
Copy a file to an empty directory.
Verify that the copied file matches the original.
Note: This variation is expected to succeed only if the server is V5R3 or higher.
(Prior to V5R3, must specify the target as a full path to file, rather than dir.)
But try it to pre-V5R3 anyway, to see what happens.
Note: The File Server team is planning to release a PTF to enable support in V5R2.
**/
  public void Var004()
  {
    String targetPath = "/var004Dir";  // directory
    String filePath = null; // file in directory
    try
    {
      createIFSDirectory(targetPath);
      filePath = targetPath + "/" + sourceFile_.getName(); // file in directory
      sourceFile_.copyTo(targetPath);
      IFSFile targetFile = new IFSFile(systemObject_, filePath);
      assertCondition(targetFile.length() == SOURCE_FILE_LENGTH &&
                      filesMatch(sourceFile_,targetFile));
    }
    catch(Exception e)
    {
      if (getSystemVRM() >= VRM_V5R3M0) {
        failed(e);
      }
      else {
        assertExceptionIs(e, "ExtendedIOException", "/var004Dir: Directory entry exists.");
      }
    }
    finally {
      if (filePath != null) deleteIFSFile(filePath);
      deleteIFSFile(targetPath);
    }
  }


/**
Copy a file to a directory that already has a file by that name, with different data.
Verify that the copied file matches the original.
Note: This variation is expected to succeed, only if the server is V5R3 or higher.
(Prior to V5R3, must specify the target as a full path to file, rather than dir.)
But try it to pre-V5R3 anyway, to see what happens.
**/
  public void Var005()
  {
    String targetPath = "/var005Dir";  // directory
    String filePath = null; // file in directory
    try
    {
      createIFSDirectory(targetPath);
      filePath = targetPath + "/" + sourceFile_.getName(); // file in directory
      createFile(filePath, "Some bogus data");
      sourceFile_.copyTo(targetPath);
      IFSFile targetFile = new IFSFile(systemObject_, filePath);
      assertCondition(targetFile.length() == SOURCE_FILE_LENGTH &&
                      filesMatch(sourceFile_,targetFile));
    }
    catch(Exception e)
    {
      if (getSystemVRM() >= VRM_V5R3M0) {
        failed(e);
      }
      else {
        assertExceptionIs(e, "ExtendedIOException", "/var005Dir: Directory entry exists.");
      }
    }
    finally {
      if (filePath != null) deleteIFSFile(filePath);
      deleteIFSFile(targetPath);
    }
  }


////**
///Copy a file to a directory that already has a file by that name,
///specify the "no replace" option.
///Verify that the copy fails, and file is unchanged.
///**/
///  public void Var007()
///  {
///    String targetPath = "/var007Dir";  // directory
///    String filePath = null; // file in directory
///    try
///    {
///      createDirectory(targetPath);
///      filePath = targetPath + "/" + sourceFile_.getName(); // file in directory
///      createFile(filePath, "Some bogus data");
///      sourceFile_.copyTo(targetPath, false);
///      failed("Exception didn't occur.");
///    }
///    catch(Exception e)
///    {
///      ///output_.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e1) {};
///      assertExceptionIs(e, "ExtendedIOException", "Directory entry exists.");
///    }
///    finally {
///      if (filePath != null) deleteFile(filePath);
///      deleteFile(targetPath);
///    }
///  }


/**
Copy a directory containing multiple files, to a nonexistent directory.
Verify that the copied files match the original.
**/
  public void Var006()
  {
    String sourceDirPath = "/var006SourceDir";
    String targetDirPath = "/var006TargetDir";
    // String filePath = null; // file in directory
    IFSFile sourceDir = null;
    IFSFile targetDir = null;
    try
    {
      createIFSDirectory(sourceDirPath);
      sourceDir = new IFSFile(systemObject_, sourceDirPath);
      targetDir = new IFSFile(systemObject_, targetDirPath);

      // Populate the source directory with some files.
      for (int i=0; i<5; i++) {
        createFile(sourceDirPath+"/file"+i, "Data: " + i);
      }
      String subDirPath = sourceDirPath+"/subdir1";
      createIFSDirectory(subDirPath);
      for (int i=0; i<4; i++) {
        createFile(subDirPath+"/file"+i, "Data: " + 10*i);
      }
      // Copy directory.
      sourceDir.copyTo(targetDirPath);

      // Verify that the target dir matches source dir.
      assertCondition(directoriesMatch(sourceDir,new IFSFile(systemObject_, targetDirPath)));
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally {
      try { deleteIFSDirectory(sourceDir); } catch (Exception e) { e.printStackTrace(); }
      try { deleteIFSDirectory(targetDir); } catch (Exception e) { e.printStackTrace(); }
    }
  }



/**
Copy a directory containing multiple files, to an existing directory.
Verify that an ObjectAlreadyExists exception is thrown.
**/
  public void Var007()
  {
    String sourceDirPath = "/var007SourceDir";
    String targetDirPath = "/var007TargetDir";
    // String filePath = null; // file in directory
    IFSFile sourceDir = null;
    IFSFile targetDir = null;
    try
    {
      createIFSDirectory(sourceDirPath);
      createIFSDirectory(targetDirPath);
      sourceDir = new IFSFile(systemObject_, sourceDirPath);
      targetDir = new IFSFile(systemObject_, targetDirPath);

      // Populate the source directory with some files.
      for (int i=0; i<5; i++) {
        createFile(sourceDirPath+"/file"+i, "Data: " + i);
      }
      String subDirPath = sourceDirPath+"/subdir1";
      createIFSDirectory(subDirPath);
      for (int i=0; i<4; i++) {
        createFile(subDirPath+"/file"+i, "Data: " + 10*i);
      }
      // Copy directory.
      sourceDir.copyTo(targetDirPath);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ObjectAlreadyExistsException", targetDirPath+": Object already exists.");
    }
    finally {
      if (sourceDir != null) deleteIFSDirectory(sourceDir);
      if (targetDir != null) deleteIFSDirectory(targetDir);
    }
  }


////**
///Copy a directory containing multiple files, to a nonempty directory,
///specify the "no replace" option.
///Verify that the same-name files files are not copied.
///Note: This variation is expected to succeed only if the server is V5R3 or higher.
///But try it to pre-V5R3 anyway, to see what happens.
///**/
///  public void Var007()
///  {
///    String sourceDirPath = "/var010SourceDir";
///    String targetDirPath = "/var010TargetDir";
///    String filePath = null; // file in directory
///    IFSFile sourceDir = null;
///    IFSFile targetDir = null;
///    try
///    {
///      createDirectory(sourceDirPath);
///      createDirectory(targetDirPath);
///      sourceDir = new IFSFile(systemObject_, sourceDirPath);
///      targetDir = new IFSFile(systemObject_, targetDirPath);
///
///      // Populate the source directory with some files.
///      for (int i=0; i<5; i++) {
///        createFile(sourceDirPath+"/file"+i, "Data: " + i);
///      }
///      String subDirPath = sourceDirPath+"/subdir1";
///      createDirectory(subDirPath);
///      for (int i=0; i<4; i++) {
///        createFile(subDirPath+"/file"+i, "Data: " + 10*i);
///      }
///
///      // Partially populate the target directory with some files.
///      for (int i=1; i<4; i++) {
///        createFile(targetDirPath+"/file"+i, "Data: " + (i+1));
///      }
///      subDirPath = targetDirPath+"/subdir1";
///      createDirectory(subDirPath);
///      for (int i=1; i<3; i++) {
///        createFile(subDirPath+"/file"+i, "Data: " + (10*i+1));
///      }
///
///      // Copy directory.
///      ///output_.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};
///      sourceDir.copyTo(targetDirPath);
///      ///output_.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};
///
///      // Verify that the target dir matches source dir.
///      assertCondition(!directoriesMatch(sourceDir,new IFSFile(systemObject_, targetDirPath)));
///    }
///    catch(Exception e)
///    {
///      ///output_.println ("Caught exception.  Press ENTER to continue"); try { System.in.read (); } catch (Exception e1) {};
///      failed(e);
///    }
///    finally {
///      deleteDirectory(sourceDir);
///      deleteDirectory(targetDir);
///    }
///  }




}
