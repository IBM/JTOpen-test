///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSMiscTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.IFS;

import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileFilter;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSJavaFile;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIOException;
import com.ibm.as400.access.Trace;

import test.TestDriverStatic;

/**
Test methods not covered by other testcases.
**/
public class IFSMiscTestcase extends IFSGenericTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSMiscTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }

  private final static String dotFilesDirName_ = "/DotFiles";
  private boolean createdDotFilesDir_;
  private boolean brief_;

  private CommandCall commandCall_;
  private CommandCall commandCallPwrSys_;

  private static final int MAX_FILE_LENGTH = 2147483647;  // max allowable value for setLength() is (2Gig minus 1) or 0x7FFFFFFFF.

  // Blocking size used by IFSFileEnumeration.  
  private static final int MAXIMUM_GET_COUNT_ = 128;

/**
Constructor.
**/
  public IFSMiscTestcase (AS400 systemObject,
        String userid, 
        String password,
                   Hashtable<String,Vector<String>>namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSMiscTestcase",
            namesAndVars, runMode, fileOutputStream,  pwrSys);
        brief_ = TestDriverStatic.brief_;
    }


  /**
   @exception  Exception  If an exception occurs.
   **/
  public void setup()
    throws Exception
  {
    super.setup(); 

    commandCall_ = new CommandCall(systemObject_);
    commandCallPwrSys_ = new CommandCall(pwrSys_);


//    ifsDirName_ = IFSFile.separator;
    dirName_ = IFSFile.separator;

  }


  /**
   @exception  Exception  If an exception occurs.
   **/
  public void cleanup()
    throws Exception
  {
    IFSFile dir1 = new IFSFile(pwrSys_, dotFilesDirName_);
    if (dir1.exists()) {
      output_.println("Running cleanup - deleting directory " + dotFilesDirName_);
      try { dir1.setPatternMatching(IFSFile.PATTERN_POSIX_ALL); }
      catch (NoSuchMethodError e) {}
      IFSFile[] files = dir1.listFiles();
      for (int i=0; i<files.length; ++i)
      {
        files[i].delete();
      }
      dir1.delete();
    }
  }





/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSRandomAccessFile.seek(long) if argument one is invalid.
**/
  public void Var001()
  {
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.seek(-1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Test IFSRandomAccessFile.seek(long).
**/
  public void Var002()
  {
    byte[] dataIn = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    createFile(ifsPathName_, dataIn);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      byte[] data1 = new byte[4];
      byte[] data2 = new byte[4];
      file.read(data1);
      file.seek(0);
      file.read(data2);
      file.seek(9);
      int i = file.read();
      assertCondition(areEqual(data1, data2) && i == dataIn[9]);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSRandomAccessFile.skipBytes(int) if argument one is invalid.
**/
  public void Var003()
  {
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.skipBytes(-1L);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Test skipBytes(int).
**/
  public void Var004()
  {
    byte[] dataIn = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    createFile(ifsPathName_, dataIn);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      file.read();
      assertCondition(file.skipBytes(1L) == 1 && file.read() == 2 &&
             file.skipBytes(2L) == 2 && file.read() == 5 &&
             file.skipBytes(3L) == 3 && file.read() == 9);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument one of
IFSFileInputStream.skip(long) is invalid.
**/
  public void Var005()
  {
    createFile(ifsPathName_, "abcdefghijklmnopqrstuvwxyz");
    IFSFileInputStream in = null;
    try
    {
      in = new IFSFileInputStream(systemObject_, ifsPathName_);
      byte[] data = new byte[26];
      in.skip(-1);
      failed("Exception didn't occur."+data);
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
    try { in.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Test IFSFileInputStream.skip(long).
**/
  public void Var006()
  {
    byte[] dataIn = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    createFile(ifsPathName_, dataIn);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      is.read();
      assertCondition(is.skip(1) == 1 && is.read() == 2 &&
             is.skip(2) == 2 && is.read() == 5 &&
             is.skip(3) == 3 && is.read() == 9);
      is.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFile.canRead() returns false if called for a non-existent file.
**/
  public void Var007()
  {
    String fileName = ifsPathName_ + "m07";
    deleteFile(fileName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      assertCondition(file.canRead() == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that IFSFile.canRead() returns true if called for a file that exists.
**/
  public void Var008()
  {
    String fileName = ifsPathName_ + "m08";
    createFile(fileName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      assertCondition(file.canRead() == true);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(fileName);
  }

/**
Ensure that IFSFile.canWrite() returns false if called for a non-existent file.
**/
  public void Var009()
  {
    String fileName = ifsPathName_ + "m09";
    deleteFile(fileName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      assertCondition(file.canWrite() == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(fileName);
  }

/**
Ensure that IFSFile.canWrite() returns true if called for a file that exists.
**/
  public void Var010()
  {
    // String fileName = ifsPathName_ + "m10";
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      assertCondition(file.canRead() == true);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that IFSFile.canWrite returns false if called for a read-only file.
**/
  public void Var011()
  {
    String library="JTIFSMISC"; 
    String cmdStr1 = "QSYS/CRTLIB "+library; 
    String cmdStr2 = "QSYS/CRTPF FILE("+library+"/FILE) RCDLEN(132) ALWUPD(*NO)";
    CommandCall cmd = new CommandCall(systemObject_);
    try
    {
      cmd.setCommand(cmdStr1);
      if (!cmd.run()) { output_.println("WARNING:  Unable to run "+cmdStr1);} 
      cmd.setCommand(cmdStr2);
      if (!cmd.run()) { output_.println("WARNING:  Unable to run "+cmdStr2);} 
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      output_.println("Unable to setup variation.");
    }

    try
    {
      IFSFile file =
        new IFSFile(systemObject_, "/QSYS.LIB/"+library+".lib/FILE.FILE/FILE.MBR");
      if (!file.canRead()) failed("Unable to read file.");
      else assertCondition(file.canWrite() == false, "Able to write file");
    }
    catch(Exception e)
    {
      failed(e);
    }

    cmdStr1 = "QSYS/DLTF FILE("+library+"/FILE)";
    cmdStr2 = "QSYS/DLTLIB "+library;
    try
    {
	cmd.setCommand("QSYS/CHGJOB INQMSGRPY(*SYSRPYL)");
	cmd.run(); 
      cmd.setCommand(cmdStr1);
      cmd.run();
      cmd.setCommand(cmdStr2);
      cmd.run();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      output_.println("Unable to cleanup variation.");
    }
  }

/**
Ensure that IFSFile.equals() returns true if called on itself or another
IFSFile object having the same path name.
**/
  public void Var012()
  {
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, ifsPathName_);
      IFSFile file2 = new IFSFile(systemObject_, ifsPathName_);
      assertCondition(file1.equals(file1) && file1.equals(file2));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that IFSFile.equals() returns false if called on another IFSFile object
having a different path name.
**/
  public void Var013()
  {
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, ifsPathName_);
      IFSFile file2 = new IFSFile(systemObject_, ifsPathName_);
      assertCondition(file1.equals(file1) && file1.equals(file2));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that IFSFile.exists() returns false if the file does not exist.
**/
  public void Var014()
  {
    String fileName = ifsPathName_ + "m14";
    deleteFile(fileName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      assertCondition(file.exists() == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(fileName);
  }

/**
Ensure that IFSFile.exists() returns true if the file exists.
**/
  public void Var015()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      assertCondition(file.exists() == true);

    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Test getAbsolutePath().
**/
  public void Var016()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      assertCondition(file.getAbsolutePath().equals(ifsPathName_), "file.getAbsolutePath="+file.getAbsolutePath()+" ifsPathName_="+ifsPathName_);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that IFSFile.getFreeSpace() returns the number of bytes available
in the file system.
**/
  public void Var017(int runMode)
  {
	  notApplicable("Attended testcase"); 
  }

/**
Test IFSFile.getName().
**/
  public void Var018()
  {
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, "/Directory/File.txt");
      IFSFile file2 = new IFSFile(systemObject_, "File.txt");
      assertCondition(file1.getName().equals("File.txt") &&
             file2.getName().equals("File.txt"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test getParent().
**/
  public void Var019()
  {
    try
    {
      String expectedParentFile = ifsDirName_; 
      int l = expectedParentFile.length(); 
      if (expectedParentFile.lastIndexOf('/') == l - 1) {
        expectedParentFile = expectedParentFile.substring(0,l-1); 
      }

      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      assertCondition(file.getParent().equals(expectedParentFile), "file.getParent()="+file.getParent()+" sb "+expectedParentFile);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test getPath().
**/
  public void Var020()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      assertCondition(file.getPath().equals(ifsPathName_),  "file.getPath="+file.getPath()+" ifsPathName_="+ifsPathName_);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test isAbsolute().
**/
  public void Var021()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      assertCondition(file.isAbsolute());
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that IFSFile.isDirectory() returns false if the directory doesn't
exist.
**/
  public void Var022()
  {

    // ensure directory doesn't exist
    try
    {
      deleteIFSDirectory (ifsDirName_ + "NoDir");
      IFSFile stillThere = new IFSFile(systemObject_, ifsDirName_ + "NoDir");
      if (stillThere.exists())
      {
        failed("File "+ifsDirName_ + "NoDir"+" still exists");
        return;
      }
    }
    catch(Exception e)
    {

      failed(e,"Unable to setup variation by deleting "+ifsDirName_ + "NoDir.");
      return;
    }

    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsDirName_ + "NoDir");
      assertCondition(file.isDirectory() == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that IFSFile.isDirectory returns true if the directory exists.
**/
  public void Var023()
  {
    createDirectory(ifsDirName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsDirName_);
      assertCondition(file.isDirectory() == true);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that IFSFile.list() returns the same directory entry names as
File.list().
**/
  public void Var024()
  {
 
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, ifsDirName_);
      file1.setPatternMatching(IFSFile.PATTERN_POSIX_ALL);       //@D1A
      String[] names1 = file1.list();
      if (names1 == null) {
	  failed("names1 is null for ifsDirName_="+ifsDirName_); return; 
      } 
      Arrays.sort(names1);
      // File file2 = new File(convertToPCName(ifsDirName_));
      // String[] names2 = file2.list();
      String[] names2 = listDirectory(ifsDirName_);
      if (names2 == null) {
	  failed("names2 is null ifsDirName_="+ifsDirName_); return; 
      } 
      Arrays.sort(names2);
      int mismatchCount=0;
      if (names1.length == names2.length)
      {
        boolean mismatch = false;
        for (int i=0; i<names1.length; i++)
        {
          if (! names1[i].equals(names2[i])) {
            mismatch = true;
            mismatchCount++;
            output_.println("MISMATCH: IFSFile.list(), File.list() = " + names1[i] + ", " + names2[i]);
          }
        }
        if (mismatch)
        {
          output_.println("  Number mismatches: ("+mismatchCount+"/"+names1.length+")");
          // Some names mismatch due to ccsid differences ?? don't fail the variation
          // failed("Data does not match.");
        }
        succeeded();
      }
      else
      {
        failed("List length mismatch: IFSFile.list() = " + Integer.toString(names1.length) +
               " File.list() = " + Integer.toString(names2.length));
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that NullPointerException is NOT thrown if argument one of
IFSFile.list(IFSFileFilter) is null.
**/
  public void Var025()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      file.list((IFSFileFilter) null);
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that NullPointerException is thrown by IFSFile.list(String) if
parameter one is null.
**/
  public void Var026()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsDirName_);
      file.list((String) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "pattern");
    }
  }

/**
Ensure that IFSFile.list("*") returns the same files as File.list().
**/
  public void Var027()
  {

    try
    {
      IFSFile dir1 = new IFSFile(systemObject_, ifsDirName_);
      dir1.setPatternMatching(IFSFile.PATTERN_POSIX_ALL);   //@D1A
      String[] list1 = dir1.list("*");
      
      if (list1 == null) {
	  failed("list1 is null for *"); return; 
      } 
      Arrays.sort(list1); 
      // File dir2 = new File(convertToPCName(ifsDirName_));
      // String[] list2 = dir2.list();

      String[] list2 = listDirectory(ifsDirName_);

      if (list2 == null) {
	  failed("list2 is null for ifsDirName_="+ifsDirName_); return; 
      } 
      Arrays.sort(list2);
      int mismatchCount=0;
      int i = -1;
      if (list1.length == list2.length)
      {
        for (i = 0; i < list1.length; i++)
          if (!list1[i].equals(list2[i])) {
            output_.println(" ["+i+"] '"+list1[i]+"' != '"+list2[i]+"'");
            mismatchCount++;
          }
      }
      if (mismatchCount != 0) {
        output_.println("  Number mismatches: ("+mismatchCount+"/"+list1.length+")");
      }
      assertCondition(list1.length == list2.length);  
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that NullPointerException is NOT thrown if argument one of
IFSFile.list(IFSFileFilter, String) is null.
**/
  public void Var028()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      file.list((IFSFileFilter) null, "*.exe");
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that NullPointerException is thrown if argument two of
IFSFile.list(IFSFileFilter, String) is null.
**/
  public void Var029()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      file.list(new IFSFilter83(output_), (String) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "NullPointerException", "pattern"));
    }
  }


/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSFile.setLastModified(long) if parameter one is < -2.
**/
  public void Var030()
  {
    IFSFile file = null;
    try
    {
      file = new IFSFile(systemObject_, ifsPathName_);
      file.setLastModified(-2);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
  }

/**
Test IFSFile.setLastModified(long).
**/
  public void Var031()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      long millisSince1970 = System.currentTimeMillis();

      // The byte stream server appears to only be accurate to the
      // second.  So, floor this value at the thousands place (ie.
      // 1234 goes to 1000, 456789 goes to 456, etc.).
      millisSince1970 = millisSince1970 / 1000L * 1000L;
      Date date = new Date(millisSince1970);

      file.setLastModified(date.getTime());
      Date modificationDate = new Date(file.lastModified());
      assertCondition(date.equals(modificationDate) ||
             date.getTime() == modificationDate.getTime()-1);
                                        // tolerate off-by-1-millisecond
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that we can read/write a database file member.
   **/
  public void Var032()
  {
    try
    {
      CommandCall cmd =
        new CommandCall(systemObject_, "QSYS/CRTPF FILE(FILE032) RCDLEN(132)");
      if (cmd.run())
      {
        IFSRandomAccessFile file =
          new IFSRandomAccessFile(systemObject_,
                                  "/QSYS.LIB/QGPL.LIB/FILE032.FILE/FILE032.MBR",
                                  "rw");
        String data = "abc";
        file.writeBytes(data);
        file.seek(0);
        String s = file.readLine();
        file.close();
        assertCondition(s.equals(data));
      }
      else
      {
        failed("Failure creating database file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    try
    {
      CommandCall cmd =
        new CommandCall(systemObject_, "QSYS/DLTF FILE(FILE032)");
      cmd.run();
    }
    catch(Exception e) {}
  }

  /**
   Ensure that we can read/write a user space.  User spaces are only
   accessible from the byte stream server on v3r7 and newer releases.
   **/
  public void Var033()
  {
    try
    {
      if (systemObject_.getVersion() < 3 ||
          (systemObject_.getVersion() == 3 && systemObject_.getRelease() < 7))
      {
        notApplicable();
        return;
      }

      CommandCall cmd =
        new CommandCall(systemObject_,
                        "QDEVELOP/CRTUSRSPC USRSPC(QGPL/USRSPC) SIZE(10)");
      cmd.run();
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_,
                                "/QSYS.LIB/QGPL.LIB/USRSPC.USRSPC", "rw");
      String data = "abc\n";
      file.writeBytes(data);
      file.seek(0);
      String s = file.readLine();
      file.close();
      assertCondition(s.equals(data));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/QSYS.LIB/QGPL.LIB/usrspc.usrspc");
  }


  /**
   Ensure that IFSFile.list(IFSFileFilter) works.
   **/
  public void Var034()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, "/");
      IFSFilter83 filter = new IFSFilter83(output_);
      String[] list = file.list(filter);
      assertCondition(list.length > 0);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.hashCode() returns the same value for the same object
   that objects that are equal (according to IFSFile.equals()) return the
   same value.
   **/
  public void Var035()
  {
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, "/abcd");
      IFSFile file2 = new IFSFile(systemObject_, "/abcd");
      assertCondition(file1.hashCode() == file2.hashCode() &&
             file1.equals(file2) && file1.hashCode() == file1.hashCode());
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that a leading slash is prepended to a file name if one is
   not specified.
   **/
  public void Var036()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, "foobar");
      assertCondition(file.getPath().equals("/foobar"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Verify that getPath() returns the path that was set.
Specify a filename with old-unicode characters.
**/
  public void Var037()
  {

    String dir1Name = "/Directory37";
    // Note: Since mkdir() goes through java.io.File, and therefore is
    // sensitive to quirks of the local file system, we've had only mixed
    // success with setting unusual characters in directory names.

    String file1NameOld = "\u3506\u3507\u3508"; // Old unicode; map to new: b000,b001,b004

    String file1NameNew = "\ub000\ub001\ub004"; // New unicode; map to old: 3506,3507,3508

    String file1Path = dir1Name + "/" + file1NameOld;

    IFSFile dir1 = null;
    IFSFile file1 = null;
    try
    {
      createDirectory(dir1Name);
      createIFSFile(file1Path);
      dir1  = new IFSFile(systemObject_, dir1Name);
      file1 = new IFSFile(systemObject_, dir1, file1NameOld);
      String[] fileNames = dir1.list();
      if (fileNames == null) {
        failed("No files were created in directory");
        return;
      }
      if (DEBUG) {
        for (int i=0; i<fileNames.length; i++)
          output_.println("--> " + fileNames[i]);
      }
      if (fileNames.length != 1)
        failed("Wrong number of files in directory: " + fileNames.length);
      else {
        String fileName = fileNames[0];
        // Note: Upon return, if the AS/400 only speaks "old Unicode",
        // then the Toolbox converts the name to new-Unicode
        // when building a String to represent it.
        String expectedName;
        // Prior to V5R1, only old Unicode was supported; so when the
        // system reports filenames back to us, it tags the files with
        // the "old Unicode" ccsid value, which clues Toolbox to convert
        // to new Unicode when composing String's for them.
        // If we're running to a V5R1 or later system, our filename
        // is handled as a new-Unicode filename throughout the loop,
        // and no conversion happens.
        if (systemObject_.getVersion() < 5)
          expectedName = file1NameNew;
        else
          expectedName = file1NameOld;
        if (!fileName.equals(expectedName)) {
          failed("Filename mismatch: Expected |"+expectedName+"|, " +
                 "got |" + fileName + "|");
          char[] chars = expectedName.toCharArray();
          byte[] bytes = dumpCharArray(chars);
          Trace.log(Trace.ERROR, "Expected:", bytes);
          chars = fileName.toCharArray();
          bytes = dumpCharArray(chars);
          Trace.log(Trace.ERROR, "Got:", bytes);
        }
        else succeeded();
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      if (file1 != null) deleteIFSFile(file1.getPath());
      if (dir1 != null) deleteDirectory(dir1.getPath());
    }
  }

/**
Verify that getPath() returns the path that was set.
Specify a filename with new-unicode characters.
**/
  public void Var038()
  {

    String dir1Name = "/Directory38";
    // Note: Since mkdir() goes through java.io.File, and therefore is
    // sensitive to quirks of the local file system, we've had only mixed
    // success with setting unusual characters in directory names.

    String file1NameNew = "\ub000\ub001\ub004"; // new unicode; map to old: 3506,3507,3508

    String file1Path = dir1Name + "/" + file1NameNew;

    IFSFile dir1 = null;
    IFSFile file1 = null;
    try
    {
      createDirectory(dir1Name);
      createIFSFile(file1Path);
      dir1  = new IFSFile(systemObject_, dir1Name);
      file1 = new IFSFile(systemObject_, dir1, file1NameNew);
      String[] fileNames = dir1.list();
      if (fileNames == null) {
        failed("No files were created in directory");
        return;
      }
      if (DEBUG) {
        for (int i=0; i<fileNames.length; i++)
          output_.println("--> " + fileNames[i]);
      }
      if (fileNames.length != 1)
        failed("Wrong number of files in directory: " + fileNames.length);
      else {
        String fileName = fileNames[0];
        if (!fileName.equals(file1NameNew)) {
          failed("Filename mismatch: Expected |"+file1NameNew+"|, " +
                 "got |" + fileName + "|");
          char[] chars = file1NameNew.toCharArray();
          byte[] bytes = dumpCharArray(chars);
          Trace.log(Trace.ERROR, "Expected:", bytes);
          chars = fileName.toCharArray();
          bytes = dumpCharArray(chars);
          Trace.log(Trace.ERROR, "Got:", bytes);
        }
        else succeeded();
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      if (file1 != null) deleteIFSFile(file1.getPath());
      if (dir1 != null) deleteDirectory(dir1.getPath());
    }
  }

/**
Verify that getCCSID() returns -1 for a nonexistent file.
**/
  public void Var039()
  {
    String fileName = ifsPathName_ + "m39";
    deleteFile(fileName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      int ccsid = file.getCCSID(); 
      assertCondition(ccsid == -1, "Not existant file returns CCSID of "+ccsid);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
Verify that getCCSID() returns -1 for a directory.
April 2017 -- get CCSID now returns a value 
**/
  public void Var040()
  {
    String fileName = "/QIBM";
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      int systemCcsid = systemObject_.getCcsid();
      assertCondition(file.getCCSID() > 0, "Update April 2017,  CCSID is > 0 systemCcsid="+systemCcsid);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Verify that getCCSID() returns valid CCSID for a valid file.
**/
  public void Var041()
  {
    String fileName = ifsPathName_ + "m41";
    createFile(fileName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      if (DEBUG) {
        output_.println("Reported system CCSID == " + systemObject_.getCcsid());
        output_.println("Reported file CCSID == " + file.getCCSID());
      }
      assertCondition(file.getCCSID() > 0);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
Ensure that IFSFile.getSubtype() returns the correct subtype of the file.
**/
  public void Var042()
  {
      String createCommand = "QSYS/CRTPF FILE(QGPL/FILE042) RCDLEN(132)"; 
    IFSRandomAccessFile raFile = null;
    try
    {
      CommandCall cmd =
        new CommandCall(systemObject_, createCommand);
      if (cmd.run())
      {
        String fileName = "/QSYS.LIB/QGPL.LIB/FILE042.FILE";
        String memberName = fileName + "/FILE042.MBR";
        raFile = new IFSRandomAccessFile(systemObject_, memberName, "rw");
        raFile.close();
        raFile = null;
        IFSFile file = new IFSFile(systemObject_, fileName);
        String subtype = file.getSubtype();
        assertCondition(subtype != null && subtype.length() != 0);
      }
      else
      {
        failed("Failure creating database file  using "+createCommand);
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      if (raFile != null) try { raFile.close(); } catch (Exception e) {};
      try
      {
        CommandCall cmd =
          new CommandCall(systemObject_, "QSYS/DLTF FILE(QGPL/FILE042)");
        cmd.run();
      }
      catch(Exception e) {}
    }
  }

/**
Run IFSFile.getSubtype() against everything in QSYS.LIB/QGPL.LIB.
**/
  public void Var043()
  {
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }

    if (runMode_ == UNATTENDED) {
	notApplicable("Attended TC");
	return;
    } 

    // Make this an interactive test.. It is highly like that something is locked.
    // If something is locked you will get message
    // com.ibm.as400.access.ExtendedIOException: /QSYS.LIB/ZDANEWDA.LIB: File in use.
    //	at com.ibm.as400.access.IFSFileDescriptorImplRemote.listAttributes(IFSFileDescriptorImplRemote.java:797)
    //	at com.ibm.as400.access.IFSFileImplRemote.getSubtype(IFSFileImplRemote.java:1345)
    //	at com.ibm.as400.access.IFSFile.getSubtype(IFSFile.java:1724)

    boolean ok = true;
    try
    {

    {
      if (DEBUG) output_.println("Directory: /QIBM/ProdData/HTTP/Public/jt400");
      IFSFile dir = new IFSFile(systemObject_, "/QIBM/ProdData/HTTP/Public/jt400");
      Enumeration<IFSFile> enumeration = dir.enumerateFiles();
      while (enumeration.hasMoreElements())
      {
        IFSFile file = (IFSFile)enumeration.nextElement();
        String subtype = file.getSubtype();
        if (subtype == null) {
          output_.println("Null subtype for " + file.getPath());
          ok = false;
        }
        else if (DEBUG && subtype != null &&
                 (subtype.equals("PF") || subtype.equals("LF")))
        {
          output_.print(file.getPath()+": "+subtype+": ");
          output_.println(file.isSourcePhysicalFile());
        }
      }
    }

      {
        if (DEBUG) output_.println("Directory: /QSYS.LIB");
        IFSFile dir = new IFSFile(systemObject_, "/QSYS.LIB");
        Enumeration<IFSFile> enumeration = dir.enumerateFiles();
        while (enumeration.hasMoreElements())
        {
          IFSFile file = (IFSFile)enumeration.nextElement();
          String subtype = file.getSubtype();
          if (subtype == null) {
            output_.print(" Null subtype");
            ok = false;
          }
          else if (DEBUG && subtype != null &&
                   (subtype.equals("PF") || subtype.equals("LF")))
          {
            output_.print(file.getPath()+": "+subtype+": ");
            output_.println(file.isSourcePhysicalFile());
          }
        }
      }

      {
        if (DEBUG) output_.println("Directory: /QSYS.LIB/QGPL.LIB");
        IFSFile dir = new IFSFile(systemObject_, "/QSYS.LIB/QGPL.LIB");
        Enumeration<IFSFile> enumeration = dir.enumerateFiles();
        while (enumeration.hasMoreElements())
        {
          try
          {
            IFSFile file = (IFSFile)enumeration.nextElement();
            String subtype = file.getSubtype();
            if (subtype == null) {
              output_.println("Null subtype for " + file.getPath());
              ok = false;
            }
            else if (DEBUG && subtype != null &&
                     (subtype.equals("PF") || subtype.equals("LF")))
            {
              output_.print(file.getPath()+": "+subtype+": ");
              output_.println(file.isSourcePhysicalFile());
              if (file.isSourcePhysicalFile() && subtype.equals("LF")) {
                output_.println("Found a Logical File that is a source physical file: " + file.getPath());
                ok = false;
              }
            }
          }
          catch (AS400Exception e) {
            output_.println(e.getAS400Message().toString());
          }
        }
      }

      assertCondition(ok);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Verify that IFSFile.setLastModified(-1) sets the last modified date to the current system time on the server, for an empty file.
**/
  public void Var044()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);

      file.setLastModified(-1);  // set to current system date on the server

      // Tolerate +/- 2 minutes of slack, due to differences in system clocks.
      long curSysValTime = getSysValTime(systemObject_); //Testcase.getSysValTime()  @D4A
      long difference = Math.abs(file.lastModified() - curSysValTime); //@D4C
      if (file.length() != 0) { // make sure the file is still empty
        failed("File |"+file.getPath() + "| is not still empty; length=="+file.length());
      }
      else
      {
        // Now that we are using SysVal time rather than client time,
        // change the testcase to expect less than a couple seconds 
        // difference rather than 2 minutes.
        if (difference < 2000) // Change from 120000 @D4C
          succeeded();
        else
          failed("Incorrect lastModified value");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Verify that IFSFile.setLastModified(-1) sets the last modified date to the current system time on the server, for an non-empty file.
**/
  public void Var045()
  {

    byte[] data = new byte[10];
    for (int i=0; i<data.length; i++) {
      data[i] = (byte)i;
    }
    createFile(ifsPathName_, data);

    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);

      file.setLastModified(-1);  // set to current system date on the server
      //long now = System.currentTimeMillis();                                    //@D4D
      long curSysValTime = getSysValTime(systemObject_); //Testcase.getSysValTime()  @D4A

      // Tolerate +/- 2 minutes of slack, due to differences in system clocks.
      long difference = Math.abs(file.lastModified() - curSysValTime); //@D4C

      // Verify that we didn't change any of the data.
      IFSRandomAccessFile raFile = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      byte[] data1 = new byte[data.length];
      raFile.readFully(data1);
      raFile.close();

      if (file.length() != data.length ||
          !areEqual(data1, data)) {
        failed("File data is not correct");
      }
      else
      {
        // Now that we are using SysVal time rather than client time,
        // change the testcase to expect less than a couple seconds 
        // difference rather than 2 minutes.
        if (difference < 2000) // Change from 120000 @D4C
          succeeded();
        else
          failed("Incorrect lastModified value");
      }

    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Test IFSFile.setLength().
Note to tester: This can be a long-running variation.
**/
  public void Var046()
  {
    try
    {
      IFSFileOutputStream os =
         new IFSFileOutputStream(systemObject_, ifsPathName_);
      byte[] data = new byte[256];
      for (int i = 0; i < data.length; i++)
      {
        data[i] = (byte) i;
      }
      os.write(data);
      os.close(); 
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSRandomAccessFile raFile = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");

      long lengthInit = raFile.length();

      //file.setLength(MAX_FILE_LENGTH);       // This takes a *long* time!
      //long lengthMax = raFile.length();
      file.setLength(500);
      long length500 = raFile.length();
      file.setLength(100);
      long length100 = raFile.length();
      file.setLength(0);
      long length0 = raFile.length();

      assertCondition(lengthInit == 256 &&
             //lengthMax  == MAX_FILE_LENGTH &&
             length500  == 500 &&
             length100  == 100 &&
             length0    == 0);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSFile.setLength() if parameter is < 0.
**/
  public void Var047()
  {
    IFSFile file = null;
    try
    {
      file = new IFSFile(systemObject_, ifsPathName_);
      file.setLength(-1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSFile.setLength() if parameter exceeds maximum allowable value.
**/
  public void Var048()
  {
    IFSFile file = null;
    try
    {
      file = new IFSFile(systemObject_, ifsPathName_);
      file.setLength(MAX_FILE_LENGTH+1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
  }

/**
Test IFSRandomAccessFile.setLength().
Note to tester: This can be a long-running variation.
**/
  public void Var049()
  {
    try
    {
      IFSFileOutputStream os =
         new IFSFileOutputStream(systemObject_, ifsPathName_);
      byte[] data = new byte[256];
      for (int i = 0; i < data.length; i++)
      {
        data[i] = (byte) i;
      }
      os.write(data);
      os.close(); 
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSRandomAccessFile raFile = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");

      long lengthInit = file.length();

      //raFile.setLength(MAX_FILE_LENGTH);       // This takes a *long* time!
      //long lengthMax = file.length();
      raFile.setLength(500L);
      long length500 = file.length();
      raFile.setLength(0L);
      long length0 = file.length();
      raFile.setLength(100L);
      long length100 = file.length();

      assertCondition(lengthInit == 256 &&
             //lengthMax  == MAX_FILE_LENGTH &&
             length500  == 500 &&
             length100  == 100 &&
             length0    == 0);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSRandomAccessFile.setLength() if parameter is < 0.
**/
  public void Var050()
  {
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.setLength(-1L);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSRandomAccessFile.setLength() if parameter exceeds maximum allowable value.

Note:  This previously reported failure because the method with an integer parameter was used, 
resulting in a negative size. 
IFS files can be very large, so MAX_FILE_LENGTH+1 is still valid. 
**/
  public void Var051()
  {
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.setLength((long) MAX_FILE_LENGTH+1);
      file.setLength((long)0); 
      assertCondition(true); 
    }
    catch(Exception e)
    {
      failed(e, "Setting file size to large then small"); 
    }
  }

  boolean verifyFileCount(IFSFile dir, int expectedCount) throws IOException
  {
    boolean result = true;
    Enumeration<?> enumeration = dir.enumerateFiles("*");
    int counter = 0;
    if (DEBUG) output_.println("Enumerating files");
    while (enumeration.hasMoreElements())
    {
      IFSFile f = (IFSFile)enumeration.nextElement();
      ++counter;
      if (DEBUG) output_.println(counter+": "+f.getName());
    }
    if (DEBUG) output_.println("enumerateFiles() returned " + counter + " files.");
    if (counter != expectedCount) {
      output_.println("Incorrect number of files reported.  Expected " + expectedCount + ", got " + counter);
      result = false;
    }
    return result;
  }

/**
Test IFSFile.enumerateFiles() against various numbers of files under 'root'.
Note to tester: This can be a long-running variation.
**/
  public void Var052()
  {
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    boolean ok = true;
    IFSFile dir1 = null;
    try
    {
      deleteIFSDirectory("/Directory52");
      // Set up a directory under 'root'.
      dir1 = new IFSFile(systemObject_, "/Directory52");
      // Make sure directory is empty at the start. @D2A
      if (dir1 != null) deleteDirectory(dir1.getPath()); //@D2A
      dir1.mkdir();
      int fileCount = 0;

      // Start out with empty directory, and check reported count.
      if (!verifyFileCount(dir1, 0))   ok = false;

      // Populate the directory with MAXIMUM_GET_COUNT_-3 files,
      // and check the reported file count.
      fileCount += populateDirectory(dir1, 1, MAXIMUM_GET_COUNT_-3);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_-3))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_-2))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_-1))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_+1))   ok = false;

      // Add files to bring the file count up to (2*MAXIMUM_GET_COUNT - 1),
      // and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, MAXIMUM_GET_COUNT_-2);
      if (!verifyFileCount(dir1, (2*MAXIMUM_GET_COUNT_)-1))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, 2*MAXIMUM_GET_COUNT_))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, (2*MAXIMUM_GET_COUNT_)+1))   ok = false;

      assertCondition(ok && fileCount==(2*MAXIMUM_GET_COUNT_)+1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      if (dir1 != null) deleteDirectory(dir1.getPath());
    }
  }

/**
Test IFSFile.enumerateFiles() against various numbers of files under 'QSYS.LIB'.
Note to tester: This can be a long-running variation.
**/
  public void Var053()
  {
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    boolean ok = true;
    IFSFile dir1 = null;
    String libName = "IFSLIB53";
    try
    {
      // Set up a library.
      // NOTE: If we receive invalid counts here, add a DLTLIB prior to CRTLIB
      //       to verify the LIB is empty at the start.
	try { 
	    commandCallPwrSys_.run("QSYS/DLTLIB LIB(" + libName + ")");
	} catch (Exception e) {

	    e.printStackTrace();

	}

	commandCall_.run("QSYS/CRTLIB LIB(" + libName + ")");

	dir1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB");
      int fileCount = 0;

      // Start out with empty directory, and check reported count.
      if (!verifyFileCount(dir1, 0))   ok = false;

      output_.println(" Var053: Step 1"+"fileCount="+fileCount); 
      // Populate the library with MAXIMUM_GET_COUNT_-3 files,
      // and check the reported file count.
      fileCount += populateLibrary(libName, 1, MAXIMUM_GET_COUNT_-3);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_-3))   ok = false;

      output_.println(" Var053: Step 2"+"fileCount="+fileCount); 
      // Add one more file to the library, and check the reported count.
      fileCount += populateLibrary(libName, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_-2))   ok = false;

      output_.println(" Var053: Step 3"+"fileCount="+fileCount);
      // Add one more file to the library, and check the reported count.
      fileCount += populateLibrary(libName, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_-1))   ok = false;

      output_.println(" Var053: Step 4"+"fileCount="+fileCount); 
      // Add one more file to the library, and check the reported count.
      fileCount += populateLibrary(libName, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_))   ok = false;

      output_.println(" Var053: Step 5"+"fileCount="+fileCount); 
      // Add one more file to the library, and check the reported count.
      fileCount += populateLibrary(libName, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_+1))   ok = false;

      output_.println(" Var053: Step 6"+"fileCount="+fileCount); 
      // Add files to bring the file count up to (2*MAXIMUM_GET_COUNT - 1),
      // and check the reported count.
      fileCount += populateLibrary(libName, fileCount+1, MAXIMUM_GET_COUNT_-2);
      if (!verifyFileCount(dir1, (2*MAXIMUM_GET_COUNT_)-1))   ok = false;

      output_.println(" Var053: Step 7"+"fileCount="+fileCount); 
      // Add one more file to the library, and check the reported count.
      fileCount += populateLibrary(libName, fileCount+1, 1);
      if (!verifyFileCount(dir1, 2*MAXIMUM_GET_COUNT_))   ok = false;

      output_.println(" Var053: Step 8"+"fileCount="+fileCount); 
      // Add one more file to the library, and check the reported count.
      fileCount += populateLibrary(libName, fileCount+1, 1);
      if (!verifyFileCount(dir1, (2*MAXIMUM_GET_COUNT_)+1))   ok = false;

      output_.println(" Var053: Step 9"+"fileCount="+fileCount); 

      assertCondition(ok && fileCount==(2*MAXIMUM_GET_COUNT_)+1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
	deleteLibrary(commandCall_,libName);
    }
  }

/**
Test IFSFile.enumerateFiles() against various numbers of files under QDLS.
Note to tester: This can be a long-running variation.
**/
  public void Var054()
  {
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
    boolean ok = true;
    IFSFile dir1 = null;
    String folderName = "IFSFLR54";
    try
    {
      // Set up a folder.
      String cmdString = "QSYS/CRTFLR FLR(" + folderName + ")";
      if (!commandCallPwrSys_.run(cmdString)) {
        output_.println("Setup failed: " + cmdString);
        output_.println("You may need to do a ADDDIRE for user profile " + commandCallPwrSys_.getSystem().getUserId());
        AS400Message[] messagelist = commandCallPwrSys_.getMessageList();
        for (int i = 0; i < messagelist.length; ++i) {
          output_.println(messagelist[i].getText());
        }
        ok = false;
      }
      //commandCallPwrSys_.run("QSYS/CHGDLOAUT DLO("+folderName+") USRAUT((*PUBLIC *ALL))");   // This doesn't help.
      //commandCallPwrSys_.run("QSYS/ADDDLOAUT DLO("+folderName+") USRAUT(("+systemObject_.getUserId()+" *ALL))");   // This doesn't help either.

      //dir1 = new IFSFile(systemObject_, "/QDLS/"+folderName);
      dir1 = new IFSFile(pwrSys_, "/QDLS/"+folderName);
      int fileCount = 0;

      // Start out with empty directory, and check reported count.
      if (!verifyFileCount(dir1, 0))   ok = false;

      // Populate the directory with MAXIMUM_GET_COUNT_-3 files,
      // and check the reported file count.
      fileCount += populateDirectory(dir1, 1, MAXIMUM_GET_COUNT_-3);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_-3))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_-2))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_-1))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, MAXIMUM_GET_COUNT_+1))   ok = false;

      // Add files to bring the file count up to (2*MAXIMUM_GET_COUNT - 1),
      // and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, MAXIMUM_GET_COUNT_-2);
      if (!verifyFileCount(dir1, (2*MAXIMUM_GET_COUNT_)-1))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, 2*MAXIMUM_GET_COUNT_))   ok = false;

      // Add one more file to the directory, and check the reported count.
      fileCount += populateDirectory(dir1, fileCount+1, 1);
      if (!verifyFileCount(dir1, (2*MAXIMUM_GET_COUNT_)+1))   ok = false;

      assertCondition(ok && fileCount==(2*MAXIMUM_GET_COUNT_)+1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      try {
        try { Thread.sleep(500); }  // let the dust settle, just in case
        catch (Exception e) {}
        commandCallPwrSys_.run("QSYS/DLTDLO DLO(*ALL) FLR("+folderName+")");  // This deletes the folder's contents and then the folder.
      }
      catch (Exception e) { e.printStackTrace(); }
    }
  }

/**
Test fix for SE09560. Make sure enumerateFiles() returns the correct number of entries
when there are more than 128 items in a directory.
**/
  public void Var055()
  {
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }
      // boolean ok = true;
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "/tmp/enumtest");
      dir.mkdir();
      IFSFile[] files = dir.listFiles();
      for (int i=0; i<files.length; ++i)
      {
        files[i].delete();
      }
      dir.delete();
      dir.mkdir();
      for (int i=0; i<130; ++i)
      {
        IFSFile sub = new IFSFile(systemObject_, "/tmp/enumtest/dir"+i);
        sub.mkdir();
      }
      files = dir.listFiles();
      if (files.length != 130)
      {
        failed("Unable to setup directory structure: "+files.length);
      }
      else
      {
        Enumeration<?> enumeration = dir.enumerateFiles();
        int num = 0;
        while (enumeration.hasMoreElements())
        {
          enumeration.nextElement();
          ++num;
        }
        if (num != 130)
        {
          failed("Wrong number of directories returned: "+num+" != 130");
        }
        else
        {
          succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      // cleanup
      try
      {
        IFSFile dir = new IFSFile(systemObject_, "/tmp/enumtest");
        dir.mkdir();
        IFSFile[] files = dir.listFiles();
        for (int i=0; i<files.length; ++i)
        {
          files[i].delete();
        }
        dir.delete();
      }
      catch(Exception e) {}
    }
  }

  private void createAndPopulateDotFilesDir() throws Exception
  {
    String dirName = dotFilesDirName_;
    IFSFile dir1 = new IFSFile(systemObject_, dirName);
    dir1.mkdir();

    // Populate the directory with a variety of files, one of which has a name starting with a period.
    createFile(dirName+"/.dotAtBeginning");
    createFile(dirName+"/dot.InMiddle");
    createFile(dirName+"/dotAtEnd.");
    createFile(dirName+"/noDot");

    createdDotFilesDir_ = true;
  }

/**
Test IFSFile.setPatternMatching() and getPatternMatching().
**/
  public void Var056()
  {
    if (!checkMethodExists("getPatternMatching", null)) {
      notApplicable("No method named getPatternMatching()");
      return;
    }

    boolean ok = true;
    IFSFile dir1 = null;
    try
    {
      // Set up a directory under 'root'.
      if (!createdDotFilesDir_) createAndPopulateDotFilesDir();

      dir1 = new IFSFile(systemObject_, dotFilesDirName_);

      // Verify that list() and listFiles() return same number of entries.
      String[] names1 = dir1.list();
      IFSFile[] files1 = dir1.listFiles();
      if (names1.length != files1.length) {
        output_.println("IFSFile.list() returned " + names1.length + " files, but IFSFile.listFiles() returned " + files1.length + " files.");
        ok = false;
      }

      // Verify that the default pattern-matching is "POSIX".
      if (dir1.getPatternMatching() != IFSFile.PATTERN_POSIX) {
        output_.println("IFSFile.getPatternMatching() returned " + dir1.getPatternMatching() + " instead of POSIX.");
        ok = false;
      }

      // Verify that setPatternMatching() changes the setting.
      dir1.setPatternMatching(IFSFile.PATTERN_POSIX_ALL);
      if (dir1.getPatternMatching() != IFSFile.PATTERN_POSIX_ALL) {
        output_.println("IFSFile.getPatternMatching() returned " + dir1.getPatternMatching() + " instead of POSIX-all.");
        ok = false;
      }

      // Verify that list() and listFiles() return same number of entries (4).
      String[] names2 = dir1.list();
      IFSFile[] files2 = dir1.listFiles();
      if (names2.length != 4 || files2.length != 4) {
        output_.println("Expected 4 files. IFSFile.list() returned " + names2.length + " files. IFSFile.listFiles() returned " + files2.length + " files.");
        ok = false;
      }

      dir1.setPatternMatching(IFSFile.PATTERN_POSIX);
      if (dir1.getPatternMatching() != IFSFile.PATTERN_POSIX) {
        output_.println("IFSFile.getPatternMatching() returned " + dir1.getPatternMatching() + " instead of POSIX.");
        ok = false;
      }

      // Verify that list() and listFiles() return same number of entries.
      String[] names3 = dir1.list();
      IFSFile[] files3 = dir1.listFiles();

      // Note: Prior to V5R2, "POSIX" pattern matching didn't exclude .* files.
      if (systemObject_.getVRM() >= 0x00050200) { // is system V5R2 or later
        if (names3.length != 3 || files3.length != 3) {
          output_.println("Expected 3 files. IFSFile.list() returned " + names3.length + " files. IFSFile.listFiles() returned " + files3.length + " files.");
          ok = false;
        }
      }  // if V5R2 or later

      dir1.setPatternMatching(IFSFile.PATTERN_OS2);
      if (dir1.getPatternMatching() != IFSFile.PATTERN_OS2) {
        output_.println("IFSFile.getPatternMatching() returned " + dir1.getPatternMatching() + " instead of OS2.");
        ok = false;
      }

      // Verify that list() and listFiles() return same number of entries (3).
      String[] names4 = dir1.list();
      IFSFile[] files4 = dir1.listFiles();
      if (names4.length != 4 || files4.length != 4) {
        output_.println("Expected 4 files. IFSFile.list() returned " + names4.length + " files. IFSFile.listFiles() returned " + files4.length + " files.");
        ok = false;
      }

      assertCondition(ok /* && fileCount==(2*MAXIMUM_GET_COUNT_)+1 */ );
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
Test IFSJavaFile.setPatternMatching() and getPatternMatching().
**/
  public void Var057()
  {
    if (!checkMethodExists("getPatternMatching", null)) {
      notApplicable("No method named getPatternMatching()");
      return;
    }

    boolean ok = true;
    IFSJavaFile dir1 = null;
    try
    {
      // Set up a directory under 'root'.
      if (!createdDotFilesDir_) createAndPopulateDotFilesDir();

      String localSysDirName = dotFilesDirName_.replace('/', IFSJavaFile.separatorChar);  // use local file system's separator char
      dir1 = new IFSJavaFile(systemObject_, localSysDirName);

      // Verify that list() and listFiles() return same number of entries.
      String[] names1 = dir1.list();
      File[] files1 = dir1.listFiles();
      if (names1.length != files1.length) {
        output_.println("IFSJavaFile.list() returned " + names1.length + " files, but IFSJavaFile.listFiles() returned " + files1.length + " files.");
        ok = false;
      }

      // Verify that the default pattern-matching is "POSIX".
      if (dir1.getPatternMatching() != IFSFile.PATTERN_POSIX) {
        output_.println("IFSJavaFile.getPatternMatching() returned " + dir1.getPatternMatching() + " instead of POSIX.");
        ok = false;
      }

      // Verify that setPatternMatching() changes the setting.
      dir1.setPatternMatching(IFSFile.PATTERN_POSIX_ALL);
      if (dir1.getPatternMatching() != IFSFile.PATTERN_POSIX_ALL) {
        output_.println("IFSJavaFile.getPatternMatching() returned " + dir1.getPatternMatching() + " instead of POSIX-all.");
        ok = false;
      }

      // Note: Prior to V5R2, "POSIX" pattern matching wasn't actually supported; its behavior matched "POSIX-all".

      if (systemObject_.getVRM() >= 0x00050200) { // is system V5R2 or later
        // TBD: Verify that POSIX excludes .* files, but POSIX_ALL includes them.
        // Under construction.

        // Verify that list() and listFiles() return same number of entries (4).
        String[] names2 = dir1.list();
        File[] files2 = dir1.listFiles();
        if (names2.length != 4 || files2.length != 4) {
          output_.println("Expected 4 files. IFSJavaFile.list() returned " + names2.length + " files. IFSJavaFile.listFiles() returned " + files2.length + " files.");
          ok = false;
        }

        dir1.setPatternMatching(IFSFile.PATTERN_POSIX);
        if (dir1.getPatternMatching() != IFSFile.PATTERN_POSIX) {
          output_.println("IFSJavaFile.getPatternMatching() returned " + dir1.getPatternMatching() + " instead of POSIX.");
          ok = false;
        }

        // Verify that list() and listFiles() return same number of entries (3).
        String[] names3 = dir1.list();
        File[] files3 = dir1.listFiles();
        if (names3.length != 3 || files3.length != 3) {
          output_.println("Expected 3 files. IFSJavaFile.list() returned " + names3.length + " files. IFSJavaFile.listFiles() returned " + files3.length + " files.");
          ok = false;
        }

      }  // if V5R2 or later

      assertCondition(ok /* && fileCount==(2*MAXIMUM_GET_COUNT_)+1 */ );
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Verify that setCCSID() throws exception for a nonexistent file.
**/
  public void Var058()
  {
    Class<?>[] parmTypes = { int.class };
    if (!checkMethodExists("setCCSID", parmTypes)) {
      notApplicable("No method named setCCSID()");
      return;
    }

    String fileName = ifsPathName_ + "m58";
    deleteFile(fileName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      file.setCCSID(37);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                         ExtendedIOException.FILE_NOT_FOUND);
    }
  }

/**
Verify that setCCSID() throws exception for a directory.
**/
  public void Var059()
  {
    Class<?>[] parmTypes = { int.class };
    if (!checkMethodExists("setCCSID", parmTypes)) {
      notApplicable("No method named setCCSID()");
      return;
    }

    String fileName = "/QIBM";
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      file.setCCSID(37);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                         ExtendedIOException.DIR_ENTRY_EXISTS);
    }
  }

/**
Verify that setCCSID() successfully resets the data CCSID of a valid file.
**/
  public void Var060()
  {
    Class<?>[] parmTypes = { int.class };
    if (!checkMethodExists("setCCSID", parmTypes)) {
      notApplicable("No method named setCCSID()");
      return;
    }

    boolean success = true;
    String fileName = ifsPathName_ + "m60";
    createFile(fileName);
    try
    {
      IFSFile file1 = new IFSFile(pwrSys_, fileName);
      int oldCCSID = file1.getCCSID();
      int newCCSID;
      int currentCCSID;
      if (oldCCSID == 37) {
        newCCSID = 500;
      }
      else {
        newCCSID = 37;
      }
      file1.setCCSID(newCCSID);
      output_.println(file1.getPath() + ": setCCSID("+newCCSID+")");
      IFSFile file2 = new IFSFile(systemObject_, fileName);
      currentCCSID = file2.getCCSID();
      if (currentCCSID != newCCSID) {
        output_.println("ERROR: CCSID is " + currentCCSID + " after setCCSID("+newCCSID+"); original CCSID was " + oldCCSID);
        success = false;
      }
      file1.setCCSID(oldCCSID);
      IFSFile file3 = new IFSFile(systemObject_, fileName);
      currentCCSID = file3.getCCSID();
      if (currentCCSID != oldCCSID) {
        output_.println("ERROR: CCSID is " + currentCCSID + " after resetting CCSID to "+oldCCSID);
        success = false;
      }
      assertCondition(success);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
Verify that static method IFSFile.getFreeSpace(AS400) returns the same result as instance method IFSFile.getFreeSpace().
**/
  public void Var061()
  {
    String fileName = ifsPathName_ + "m61";
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      long freeSpace = file.getFreeSpace();
      long freeSpace_static = IFSFile.getFreeSpace(systemObject_);
      if (DEBUG) output_.println(freeSpace);
      boolean succeeded = (freeSpace == freeSpace_static &&
                           freeSpace > 0);
      if (!succeeded) {
        output_.println("Instance method returned " + freeSpace + "; static method returned " + freeSpace_static);
      }
      assertCondition(succeeded);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
Verify that IFSFile.getPathPointedTo() returns the correct value.
**/
  public void Var062()
  {
    if (!checkMethodExists("getPathPointedTo", null)) {
      notApplicable("No method named getPathPointedTo()");
      return;
    }
    boolean succeeded = true;
    String fileName = ifsPathName_ + "m62";
    String symlinkName = fileName + ".symlink";
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      // Create a symlink to the file.
      String cmdStr = "ADDLNK OBJ('" + fileName + "') NEWLNK('" + symlinkName + "') LNKTYPE(*SYMBOLIC)";
      CommandCall cmd = new CommandCall(systemObject_);
      if (!cmd.run(cmdStr)) {
        output_.println("Failed to create symbolic link.");
      }

      IFSFile symlink = new IFSFile(systemObject_, symlinkName);
      String pathPointedTo = symlink.getPathPointedTo();
      if (!pathPointedTo.equals(fileName)) {
        succeeded = false;
        output_.println("Unexpected result from getPathPointedTo().  Expected: |"+fileName+"|. Got: |"+pathPointedTo+"|");
      }

      pathPointedTo = file.getPathPointedTo();
      if (pathPointedTo != null) {
        succeeded = false;
        output_.println("Unexpected result from getPathPointedTo().  Expected: null. Got: |"+pathPointedTo+"|");
      }
      assertCondition(succeeded);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(symlinkName);
      deleteFile(fileName);
    }
  }

/**
Test getParentFile().
**/
  public void Var063()
  {
    boolean succeeded = true;
    try
    {
      String expectedParentFile = ifsDirName_; 
      int l = expectedParentFile.length(); 
      if (expectedParentFile.lastIndexOf('/') == l - 1) {
        expectedParentFile = expectedParentFile.substring(0,l-1); 
      }
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      if (!(file.getParentFile().getPath()).equals(expectedParentFile))
      {
        succeeded = false;
        output_.println("file.getParentFile(): |" + file.getParentFile() + "|; expectedParentFile: |" + expectedParentFile + "|");
      }

      file = new IFSFile(systemObject_, "/");
      if (file.getParentFile() != null)
      {
        succeeded = false;
        output_.println("file.getParentFile(/): Expected null, but got |" + file.getParentFile() + "|");
      }

      file = new IFSFile();
      if (file.getParentFile() != null)
      {
        succeeded = false;
        output_.println("file.getParentFile(): Expected null, but got |" + file.getParentFile() + "|");
      }

      file = new IFSFile();
      file.setPath(ifsPathName_);
      if (!(file.getParentFile().getPath()).equals(expectedParentFile))
      {
        succeeded = false;
        output_.println("file.getParentFile() [2]: |" + file.getParentFile() + "|; expectedParentFile: |" + expectedParentFile + "|");
      }

      assertCondition(succeeded);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }



  // Verifies that a method exists in class IFSFile.  Returns false if method not found.
  static boolean checkMethodExists(String methodName, Class<?>[] parmTypes)
  {
    try {
      Class.forName("com.ibm.as400.access.IFSFile").getDeclaredMethod(methodName, parmTypes);
    }
    catch (NoSuchMethodException e) {
      return false;
    }
    catch (ClassNotFoundException e) { // quiet the compiler
      e.printStackTrace();
      return false;
    }
    return true;
  }

  

  void pause()
  {
    output_.println ("Toolbox is paused. Press ENTER to continue.");
    try { System.in.read (); } catch (Exception e) {};  }

  int populateDirectory(IFSFile dir, int startIndex, int numFiles) throws IOException
  {
    int counter = 0;
    int endIndex = startIndex+numFiles;  // non-inclusive
    for (int i=startIndex; i<endIndex; ++i)
    {
      IFSFile f = new IFSFile(dir.getSystem(), dir, "file"+i);
      f.createNewFile();
      counter++;
    }
    if (DEBUG) output_.println("Created " + counter + " files in directory " + dir.getPath());
    return counter;
  }

  final int populateLibrary(String libName, int startIndex, int numFiles) throws Exception
  {
    int counter = 0;
    int endIndex = startIndex+numFiles;  // non-inclusive
    for (int i=startIndex; i<endIndex; ++i)
    {
      String command = "QSYS/CPYF FROMFILE(QIWS/QCUSTCDT) TOFILE("+libName+"/TEST"+i+") CRTFILE(*YES)"; 
      boolean b = commandCall_.run(command);
      if (DEBUG) output_.println(i+": "+b+" "+command);
      counter++;
    }
    if (DEBUG) output_.println("Created " + counter + " files in library " + libName);
    return counter;
  }

  /**
   * Convenience function for tracing character strings.
  **/
  static final byte[] dumpCharArray(char[] charArray)
  {
    byte[] retData = new byte[charArray.length*2];
    int inPos = 0;
    int outPos = 0;
    while(inPos < charArray.length)
    {
      retData[outPos++] = (byte)(charArray[inPos] >> 8);
      retData[outPos++] = (byte)charArray[inPos++];
    }
    return retData;
  }



}

class Filter83
{
  protected boolean accept(String fileName)
  {
    // Accept names that follow the old DOS 8.3 file name structure.
    // 1-8 characters, [followed by a period, followed by a 1-3 character
    // suffix].  Break the file name into name and extension.  Only one
    // period is allowed.
    String extension = "";
    int index = fileName.indexOf('.');
    if (index != -1)
      if (index + 1 < fileName.length())
        if (fileName.indexOf('.', index + 1) != -1)
          return false; // Too many periods
        else
          extension = fileName.substring(index + 1, fileName.length());
      else
        return false; // Period with no extension.
    else
      index = fileName.length(); // No extension
    String name = fileName.substring(0, index);

    return (name.length() <= 8) && (extension.length() <= 3);
  }
}

class IFSFilter83 extends Filter83 implements IFSFileFilter
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSMiscTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }
  PrintWriter output_;

  public IFSFilter83(PrintWriter output)
  {
    output_ = output;
  }

  public boolean accept(IFSFile file)
  {
    if (IFSMiscTestcase.DEBUG) output_.print(".");
    output_.flush();
    return accept(file.getName());
  }
}

class NativeFilter83 extends Filter83 implements FilenameFilter
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSMiscTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }
  PrintWriter output_;

  public NativeFilter83(PrintWriter output)
  {
    output_ = output;
  }

  public boolean accept(File dir, String fileName)
  {
    return accept(fileName);
  }
}

